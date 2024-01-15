package org.lamsfoundation.lams.web.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Controls activity time limits. Can be used in tools to set relative and absolute time limits for learners.
 *
 * @author Marcin Cieslak
 */
public abstract class AbstractTimeLimitWebsocketServer extends ServerEndpointConfig.Configurator {

    /**
     * By default Endpoint instances are created for each request. We need persistent data to cache time settings. We
     * can not use static fields as each subclass needs to have own set of fields. This class registers and retrieves
     * Endpoints as singletons.
     */
    public static class EndpointConfigurator extends ServerEndpointConfig.Configurator {
	private static final Map<String, AbstractTimeLimitWebsocketServer> TIME_LIMIT_ENDPOINT_INSTANCES = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {

	    // is there an instance already
	    AbstractTimeLimitWebsocketServer instance = AbstractTimeLimitWebsocketServer.getInstance(
		    endpointClass.getName());
	    if (instance == null) {
		// every subclass must have a static getInstance() method
		try {
		    Method getInstanceMethod = endpointClass.getMethod("getInstance");
		    instance = (AbstractTimeLimitWebsocketServer) getInstanceMethod.invoke(endpointClass);
		} catch (Exception e) {
		    throw new InstantiationException(
			    "Error while instantinating time limit websocket server " + endpointClass.getName());
		}
	    }

	    return (T) instance;
	}
    }

    protected static class TimeCache {
	public int relativeTimeLimit;
	public int absoluteTimeLimit;
	public LocalDateTime absoluteTimeLimitFinish;
	// mapping of user ID (not UID) and when the learner entered the activity
	public final Map<Integer, LocalDateTime> timeLimitLaunchedDate = new ConcurrentHashMap<>();
	public Map<Integer, Integer> timeLimitAdjustment = new HashMap<>();

	public TimeCache() {
	}

	@Override
	public String toString() {
	    final StringBuilder sb = new StringBuilder("{");
	    sb.append("relativeTimeLimit=").append(relativeTimeLimit);
	    sb.append(", absoluteTimeLimit=").append(absoluteTimeLimit);
	    sb.append(", absoluteTimeLimitFinish=").append(absoluteTimeLimitFinish);
	    sb.append(", timeLimitLaunchedDate=").append(timeLimitLaunchedDate);
	    sb.append(", timeLimitAdjustment=").append(timeLimitAdjustment);
	    sb.append('}');
	    return sb.toString();
	}
    }

    /**
     * A singleton which updates Learners with their time limit
     */
    protected Runnable sendWorker = new Runnable() {
	@Override
	public void run() {
	    try {
		// websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		HibernateSessionManager.openSession();

		Iterator<Entry<Long, Set<Session>>> entryIterator = websockets.entrySet().iterator();
		// go through activities and update registered learners with time if needed
		while (entryIterator.hasNext()) {
		    Entry<Long, Set<Session>> entry = entryIterator.next();
		    // processActivity method can be overwritten if needed
		    boolean processed = processActivity(entry.getKey(), entry.getValue());
		    if (!processed) {
			entryIterator.remove();
		    }
		}
	    } catch (IllegalStateException e) {
		// do nothing as server is probably shutting down and we could not obtain Hibernate session
	    } catch (Exception e) {
		// error caught, but carry on
		log.error("Error in time limit worker thread", e);
		// extra details for debugging
		e.printStackTrace();
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}
    };

    protected static IUserManagementService userManagementService;

    protected Logger log = getLog();

    // how ofter the thread runs in seconds
    protected long CHECK_INTERVAL = 3;
    protected ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    protected Map<Long, Set<Session>> websockets = new ConcurrentHashMap<>();
    protected Map<Long, TimeCache> timeCaches = new ConcurrentHashMap<>();

    // each concrete subclass has to implement these methods

    protected abstract Logger getLog();

    protected abstract TimeCache getExistingTimeSettings(long toolContentId, Collection<Integer> userIds);

    protected abstract LocalDateTime launchUserTimeLimit(long toolContentId, int userId);

    // methods for server instantination

    protected AbstractTimeLimitWebsocketServer() {
	// run the singleton thread in given periods
	executor.scheduleAtFixedRate(sendWorker, 0, CHECK_INTERVAL, TimeUnit.SECONDS);

	if (userManagementService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(
		    SessionManager.getServletContext());
	    userManagementService = (IUserManagementService) wac.getBean("userManagementService");
	}
    }

    /**
     * Gets Endpoint singleton for concrete subclass
     */
    protected static AbstractTimeLimitWebsocketServer getInstance(String className) {
	return EndpointConfigurator.TIME_LIMIT_ENDPOINT_INSTANCES.get(className);
    }

    /**
     * Stores given Endpoint instance in singleton register
     */
    protected static void registerInstance(AbstractTimeLimitWebsocketServer instance) {
	String endpointClassName = instance.getClass().getName();
	AbstractTimeLimitWebsocketServer existingInstance = EndpointConfigurator.TIME_LIMIT_ENDPOINT_INSTANCES.get(
		endpointClassName);
	if (existingInstance != null) {
	    throw new IllegalStateException("Endpoint " + endpointClassName + " already existing in the pool.");
	}
	EndpointConfigurator.TIME_LIMIT_ENDPOINT_INSTANCES.put(endpointClassName, instance);
    }

    /**
     * Main logic method
     */
    protected boolean processActivity(long toolContentId, Collection<Session> websockets) throws IOException {
	// if all learners left the activity, remove the obsolete mapping
	if (websockets == null || websockets.isEmpty()) {
	    timeCaches.remove(toolContentId);
	    return false;
	}

	TimeCache timeCache = timeCaches.get(toolContentId);
	// first time a learner entered the activity, so there is no cache yet
	if (timeCache == null) {
	    timeCache = new TimeCache();
	    timeCaches.put(toolContentId, timeCache);
	}

	// get only currently active users, not all activity participants
	Collection<Integer> userIds = websockets.stream()
		.filter(w -> w.getUserProperties() != null && w.getUserProperties().get("userId") != null)
		.collect(Collectors.mapping(w -> (Integer) w.getUserProperties().get("userId"), Collectors.toSet()));
	// get activity data from DB
	TimeCache existingTimeSettings = getExistingTimeSettings(toolContentId, userIds);

	boolean updateAllUsers = false;
	// compare relative and absolute time limits with cache
	// it they changed, update all learners
	if (timeCache.relativeTimeLimit != existingTimeSettings.relativeTimeLimit) {
	    timeCache.relativeTimeLimit = existingTimeSettings.relativeTimeLimit;
	    updateAllUsers = true;
	}
	if (timeCache.absoluteTimeLimit != existingTimeSettings.absoluteTimeLimit) {
	    timeCache.absoluteTimeLimit = existingTimeSettings.absoluteTimeLimit;
	    updateAllUsers = true;
	}

	if (timeCache.absoluteTimeLimitFinish == null
		? existingTimeSettings.absoluteTimeLimitFinish != null
		: !timeCache.absoluteTimeLimitFinish.equals(existingTimeSettings.absoluteTimeLimitFinish)) {
	    timeCache.absoluteTimeLimitFinish = existingTimeSettings.absoluteTimeLimitFinish;
	    updateAllUsers = true;
	}

	if (!existingTimeSettings.timeLimitAdjustment.equals(timeCache.timeLimitAdjustment)) {
	    timeCache.timeLimitAdjustment = existingTimeSettings.timeLimitAdjustment;
	    updateAllUsers = true;
	}

	if (updateAllUsers && log.isDebugEnabled()) {
	    log.debug("Time limit settings changed for activity with tool content ID " + toolContentId + ": "
		    + existingTimeSettings);
	}

	for (Session websocket : websockets) {
	    Integer userId = (Integer) websocket.getUserProperties().get("userId");

	    if (userId == null) {
		// apparently onClose() for this websocket has been run and the map is not available anymore
		continue;
	    }

	    boolean updateUser = updateAllUsers;

	    // check if there is a point in updating learner launch date
	    if (timeCache.relativeTimeLimit > 0 || timeCache.absoluteTimeLimit > 0
		    || timeCache.absoluteTimeLimitFinish != null) {
		LocalDateTime existingLaunchDate = existingTimeSettings.timeLimitLaunchedDate.get(userId);

		if (existingLaunchDate == null) {
		    // learner entered the activity, so store his launch date in cache and DB
		    existingLaunchDate = launchUserTimeLimit(toolContentId, userId);
		}

		LocalDateTime launchedDate = timeCache.timeLimitLaunchedDate.get(userId);
		if (existingLaunchDate == null) {
		    if (launchedDate != null) {
			updateUser = true;
			timeCache.timeLimitLaunchedDate.remove(userId);
		    }
		} else if (launchedDate == null || !launchedDate.equals(existingLaunchDate)) {
		    // user (re)entered the activity, so update him with time limit
		    updateUser = true;
		    timeCache.timeLimitLaunchedDate.put(userId, existingLaunchDate);
		}
	    }

	    if (updateUser) {
		Long secondsLeft = getSecondsLeft(timeCache, userId);
		sendUpdate(websocket, secondsLeft);
	    }
	}

	return true;
    }

    /**
     * Registers a learner for processing.
     */
    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Long toolContentId = Long.valueOf(
		websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	String login = websocket.getUserPrincipal().getName();
	Integer userId = getUserId(login);
	if (userId == null) {
	    throw new SecurityException(
		    "User \"" + login + "\" is not a participant in activity with tool content ID " + toolContentId);
	}

	websocket.getUserProperties().put("userId", userId);

	Set<Session> toolContentWebsockets = websockets.get(toolContentId);
	if (toolContentWebsockets == null) {
	    toolContentWebsockets = ConcurrentHashMap.newKeySet();
	    websockets.put(toolContentId, toolContentWebsockets);
	}
	toolContentWebsockets.add(websocket);

	TimeCache timeCache = timeCaches.get(toolContentId);
	if (timeCache != null) {
	    // clear cached learner data, so he gets updated with current time limit via websocket
	    timeCache.timeLimitLaunchedDate.remove(userId);
	}

	if (log.isDebugEnabled()) {
	    log.debug("User " + login + " entered activity with tool content ID: " + toolContentId);
	}
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session websocket, CloseReason reason) {
	Long toolContentID = Long.valueOf(
		websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	Set<Session> sessionWebsockets = websockets.get(toolContentID);
	if (sessionWebsockets != null) {
	    websockets.get(toolContentID).remove(websocket);

	    if (log.isDebugEnabled()) {
		// If there was something wrong with the connection, put it into logs.
		log.debug("User " + websocket.getUserPrincipal().getName() + " left activity with tool content ID: "
			+ toolContentID + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
			|| reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE)) ? ". Abnormal close. Code: "
			+ reason.getCloseCode() + ". Reason: " + reason.getReasonPhrase() : ""));
	    }
	}
    }

    protected static Long getSecondsLeft(AbstractTimeLimitWebsocketServer instance, long toolContentId, int userId,
	    boolean getCurrentLaunchedDateByDefault) {
	// get time from cache
	TimeCache timeCache = instance.timeCaches.get(toolContentId);
	if (timeCache == null) {
	    // if the requested learner is first to enter the activity, there is no cache yet,
	    // so fetch data straight from DB
	    timeCache = instance.getExistingTimeSettings(toolContentId, Arrays.asList(userId));
	    LocalDateTime timeLimitLaunchedDate = timeCache.timeLimitLaunchedDate.get(userId);
	    if (timeLimitLaunchedDate == null && getCurrentLaunchedDateByDefault) {
		// temporarily set launch date to now
		// it will properly set by next sendWorker run
		timeCache.timeLimitLaunchedDate.put(userId, LocalDateTime.now());
	    }
	}
	return instance.getSecondsLeft(timeCache, userId);
    }

    protected Long getSecondsLeft(TimeCache timeCache, int userId) {
	if (timeCache.relativeTimeLimit == 0 && timeCache.absoluteTimeLimitFinish == null) {
	    // no time limit is set at all
	    return null;
	}

	// when user entered the activity
	LocalDateTime launchedDate = timeCache.timeLimitLaunchedDate.get(userId);
	// what is the time limit for him
	LocalDateTime finish = null;
	if (timeCache.absoluteTimeLimitFinish != null) {
	    // the limit is same for everyone
	    finish = timeCache.absoluteTimeLimitFinish;
	} else if (launchedDate == null) {
	    return null;
	} else {
	    // the limit is his entry plus relative time limit
	    finish = launchedDate.plusSeconds(timeCache.relativeTimeLimit);
	}

	LocalDateTime now = LocalDateTime.now();
	long secondsLeft = Duration.between(now, finish).toSeconds();

	Integer adjustment = timeCache.timeLimitAdjustment.get(userId);
	if (adjustment != null) {
	    secondsLeft += adjustment * 60;
	}

	return Math.max(0, secondsLeft);
    }

    protected void sendUpdate(Session websocket, Long secondsLeft) throws IOException {
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (secondsLeft == null) {
	    // time limit feature was disabled, so destroy counter on learner screen
	    responseJSON.put("clearTimer", true);
	} else {
	    responseJSON.put("secondsLeft", secondsLeft);
	}
	String response = responseJSON.toString();

	if (websocket.isOpen()) {
	    websocket.getBasicRemote().sendText(response);
	}
    }

    protected Integer getUserId(String login) {
	User user = userManagementService.getUserByLogin(login);
	return user == null ? null : user.getUserId();
    }
}