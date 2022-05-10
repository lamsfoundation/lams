package org.lamsfoundation.lams.tool.scribe.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scribe.model.ScribeReportEntry;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.util.ScribeUtils;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Receives, processes and sends Scribe reports and votes to Learners.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

    // caches what was sent to users already so it can be compared with DB
    private static class ScribeSessionCache {
	private int numberOfVotes = 0;
	private int numberOfLearners = 0;
	private final Map<Long, String> reports = new TreeMap<>();
    }

    /**
     * A singleton which updates Learners with reports and votes.
     */
    private static class SendWorker extends Thread {
	private boolean stopFlag = false;
	// how ofter the thread runs
	private static final long CHECK_INTERVAL = 3000;

	@Override
	public void run() {
	    while (!stopFlag) {
		try {
		    // websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		    HibernateSessionManager.openSession();

		    Iterator<Entry<Long, Set<Session>>> entryIterator = LearningWebsocketServer.websockets.entrySet()
			    .iterator();
		    // go through activities and update registered learners with reports and vote count
		    while (entryIterator.hasNext()) {
			Entry<Long, Set<Session>> entry = entryIterator.next();
			Long toolSessionId = entry.getKey();
			// if all learners left the activity, remove the obsolete mapping
			Set<Session> sessionWebsockets = entry.getValue();
			if (sessionWebsockets.isEmpty()) {
			    entryIterator.remove();
			    LearningWebsocketServer.cache.remove(toolSessionId);
			    continue;
			}

			ScribeSession scribeSession = LearningWebsocketServer.getScribeService()
				.getSessionBySessionId(toolSessionId);
			if (scribeSession.isForceComplete()) {
			    LearningWebsocketServer.sendCloseRequest(toolSessionId);
			    continue;
			}

			SendWorker.send(toolSessionId, null);
		    }
		} catch (IllegalStateException e) {
		    // do nothing as server is probably shutting down and we could not obtain Hibernate session
		} catch (Exception e) {
		    // error caught, but carry on
		    LearningWebsocketServer.log.error("Error in Scribe worker thread", e);
		} finally {
		    try {
			HibernateSessionManager.closeSession();
			Thread.sleep(SendWorker.CHECK_INTERVAL);
		    } catch (IllegalStateException | InterruptedException e) {
			stopFlag = true;
			LearningWebsocketServer.log.warn("Stopping Scribe worker thread");
		    }
		}
	    }
	}

	/**
	 * Feeds websockets with reports and votes.
	 */
	private static void send(Long toolSessionId, Session newWebsocket) throws IOException {
	    ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	    ScribeSessionCache sessionCache = LearningWebsocketServer.cache.get(toolSessionId);
	    if (sessionCache == null) {
		// first time run, create the cache
		sessionCache = new ScribeSessionCache();
		LearningWebsocketServer.cache.put(toolSessionId, sessionCache);
	    }

	    boolean send = false;
	    ScribeSession scribeSession = LearningWebsocketServer.getScribeService()
		    .getSessionBySessionId(toolSessionId);
	    Set<ScribeUser> learners = scribeSession.getScribeUsers();
	    if (sessionCache.numberOfLearners != learners.size()) {
		// new users have arrived
		sessionCache.numberOfLearners = learners.size();
		send = true;
	    }

	    // collect users who agreed on the report
	    Set<String> learnersApproved = new TreeSet<>();
	    for (ScribeUser user : learners) {
		if (user.isReportApproved()) {
		    learnersApproved.add(user.getLoginName());
		}
	    }
	    if (sessionCache.numberOfVotes != learnersApproved.size()) {
		sessionCache.numberOfVotes = learnersApproved.size();
		send = true;
	    }

	    ArrayNode reportsJSON = JsonNodeFactory.instance.arrayNode();
	    synchronized (sessionCache) {
		for (ScribeReportEntry storedReport : scribeSession.getScribeReportEntries()) {
		    Long uid = storedReport.getUid();
		    String cachedReportText = sessionCache.reports.get(uid);
		    String storedReportText = HtmlUtils.htmlEscape(storedReport.getEntryText());
		    storedReportText = storedReportText != null ? storedReportText.replaceAll("\n", "<br>") : null;
		    if (cachedReportText == null ? storedReportText != null
			    : (storedReportText == null) || (cachedReportText.length() != storedReportText.length())
				    || !cachedReportText.equals(storedReportText)) {
			// we could be storing hash instead of full report
			// but to build hash each report char needs to be processed anyway
			sessionCache.reports.put(uid, storedReportText);

			ObjectNode reportJSON = JsonNodeFactory.instance.objectNode();
			reportJSON.put("uid", uid);
			reportJSON.put("text", storedReportText);
			reportsJSON.add(reportJSON);
		    }
		}
	    }
	    if (reportsJSON.size() > 0) {
		responseJSON.set("reports", reportsJSON);
		send = true;
	    }

	    // always send to an user who just entered, otherwise only if there is new data
	    if (!send && (newWebsocket == null)) {
		return;
	    }

	    responseJSON.put("numberOfLearners", sessionCache.numberOfLearners);
	    responseJSON.put("numberOfVotes", sessionCache.numberOfVotes);
	    int votePercentage = ScribeUtils.calculateVotePercentage(sessionCache.numberOfVotes,
		    sessionCache.numberOfLearners);
	    responseJSON.put("votePercentage", votePercentage);

	    // either send only to the new user or to everyone
	    if (newWebsocket == null) {
		Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
		for (Session websocket : sessionWebsockets) {
		    String userName = websocket.getUserPrincipal().getName();
		    responseJSON.put("approved", learnersApproved.contains(userName));
		    websocket.getBasicRemote().sendText(responseJSON.toString());
		}
	    } else {
		String userName = newWebsocket.getUserPrincipal().getName();
		responseJSON.put("approved", learnersApproved.contains(userName));
		newWebsocket.getBasicRemote().sendText(responseJSON.toString());
	    }
	}
    }

    private static Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static IScribeService scribeService;

    private static final SendWorker sendWorker = new SendWorker();
    // maps toolSessionId -> cached session data
    private static final Map<Long, ScribeSessionCache> cache = new ConcurrentHashMap<>();
    private static final Map<Long, Set<Session>> websockets = new ConcurrentHashMap<>();

    static {
	// run the singleton thread
	LearningWebsocketServer.sendWorker.start();
    }

    /**
     * Registers the Learner for processing by SendWorker.
     */
    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	String login = websocket.getUserPrincipal().getName();
	ScribeUser user = LearningWebsocketServer.getScribeService().getUserByLoginNameAndSessionId(login,
		toolSessionId);
	if (user == null) {
	    throw new SecurityException("User \"" + login
		    + "\" is not a participant in Scribe activity with tool session ID " + toolSessionId);
	}

	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = ConcurrentHashMap.newKeySet();
	    LearningWebsocketServer.websockets.put(toolSessionId, sessionWebsockets);
	}
	sessionWebsockets.add(websocket);

	if (LearningWebsocketServer.log.isDebugEnabled()) {
	    LearningWebsocketServer.log.debug("User " + login + " entered Scribe with toolSessionId: " + toolSessionId);
	}

	new Thread(() -> {
	    try {
		HibernateSessionManager.openSession();
		SendWorker.send(toolSessionId, websocket);
	    } catch (Exception e) {
		log.error("Error while sending messages", e);
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}).start();
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session websocket) {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	LearningWebsocketServer.websockets.get(toolSessionId).remove(websocket);

	if (LearningWebsocketServer.log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    LearningWebsocketServer.log.debug("User " + websocket.getUserPrincipal().getName()
		    + " left Scribe with Tool Session ID: " + toolSessionId);
	}
    }

    /**
     * Receives a message sent by Learner via a websocket.
     *
     * @throws IOException
     * @throws JsonProcessingException
     */
    @OnMessage
    public void receiveRequest(String input, Session websocket) throws JsonProcessingException, IOException {
	if (StringUtils.isBlank(input)) {
	    return;
	}
	if (input.equalsIgnoreCase("ping")) {
	    // just a ping every few minutes
	    return;
	}

	ObjectNode requestJSON = JsonUtil.readObject(input);
	switch (JsonUtil.optString(requestJSON, "type")) {
	    case "vote":
		LearningWebsocketServer.vote(websocket);
		break;
	    case "submitReport":
		LearningWebsocketServer.submitReport(requestJSON, websocket);
		break;
	}
    }

    /**
     * User has approved a report.
     */
    private static void vote(Session websocket) {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	String userName = websocket.getUserPrincipal().getName();
	ScribeUser learner = LearningWebsocketServer.getScribeService().getUserByLoginNameAndSessionId(userName,
		toolSessionId);
	if (learner != null) {
	    learner.setReportApproved(true);
	    LearningWebsocketServer.getScribeService().saveOrUpdateScribeUser(learner);
	}
    }

    /**
     * The scribe has submitted a report.
     */
    private static void submitReport(ObjectNode requestJSON, Session websocket) {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	String userName = websocket.getUserPrincipal().getName();
	LearningWebsocketServer.getScribeService().submitReport(toolSessionId, userName, requestJSON);
    }

    /**
     * The scribe or a Monitor has force completed the activity. Browsers will refresh and display report summary.
     */
    static void sendCloseRequest(Long toolSessionId) throws IOException {
	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    return;
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("close", true);
	String response = responseJSON.toString();

	for (Session websocket : sessionWebsockets) {
	    if (websocket.isOpen()) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

    private static IScribeService getScribeService() {
	if (LearningWebsocketServer.scribeService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    LearningWebsocketServer.scribeService = (IScribeService) wac.getBean("lascrbScribeService");
	}
	return LearningWebsocketServer.scribeService;
    }
}