package org.lamsfoundation.lams.tool.scratchie.web.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.OptionDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.web.controller.AbstractTimeLimitWebsocketServer;
import org.lamsfoundation.lams.web.controller.AbstractTimeLimitWebsocketServer.EndpointConfigurator;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Sends Scratchies actions to non-leaders and time limit to leaders.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint(value = "/learningWebsocket", configurator = EndpointConfigurator.class)
public class LearningWebsocketServer extends AbstractTimeLimitWebsocketServer {

    private static final Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private IScratchieService scratchieService;

    // maps toolContentId -> toolSessionId -> itemUid -> optionUid -> isCorrect
    private final Map<Long, Map<Long, Map<Long, Map<String, Boolean>>>> scratchCache = new ConcurrentHashMap<>();

    public LearningWebsocketServer() {
	if (scratchieService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    scratchieService = (IScratchieService) wac.getBean(ScratchieConstants.SCRATCHIE_SERVICE);
	}
    }

    @Override
    protected Logger getLog() {
	return LearningWebsocketServer.log;
    }

    @Override
    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	super.registerUser(websocket);

	Long toolContentId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	websocket.getUserProperties().put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	Map<Long, Map<Long, Map<String, Boolean>>> sessionCache = scratchCache.get(toolContentId);
	Map<Long, Map<String, Boolean>> questionCache = null;
	if (sessionCache == null) {
	    sessionCache = new ConcurrentHashMap<>();
	    scratchCache.put(toolContentId, sessionCache);
	} else {
	    questionCache = sessionCache.get(toolSessionId);
	}

	if (questionCache == null) {
	    questionCache = new HashMap<>();
	    sessionCache.put(toolSessionId, questionCache);
	}
    }

    /**
     * Fetches or creates a singleton of this websocket server.
     */
    public static LearningWebsocketServer getInstance() {
	LearningWebsocketServer result = (LearningWebsocketServer) AbstractTimeLimitWebsocketServer
		.getInstance(LearningWebsocketServer.class.getName());
	if (result == null) {
	    result = new LearningWebsocketServer();
	    AbstractTimeLimitWebsocketServer.registerInstance(result);
	}
	return result;
    }

    @Override
    protected TimeCache getExistingTimeSettings(long toolContentId, Collection<Integer> userIds) {
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);
	TimeCache existingTimeSettings = new TimeCache();

	existingTimeSettings.absoluteTimeLimit = scratchie.getAbsoluteTimeLimit();
	existingTimeSettings.relativeTimeLimit = scratchie.getRelativeTimeLimit() * 60;
	existingTimeSettings.timeLimitAdjustment = new HashMap<>();

	Map<Long, LocalDateTime> sessionLaunchDates = new HashMap<>();
	Map<Long, Integer> sessionTimeLimitAdjustments = new HashMap<>();

	for (Integer userId : userIds) {
	    ScratchieUser user = scratchieService.getUserByUserIDAndContentID(userId.longValue(), toolContentId);
	    if (user == null) {
		continue;
	    }

	    ScratchieSession session = user.getSession();
	    LocalDateTime sessionLaunchDate = null;
	    Integer timeLimitAdjustment = null;

	    if (sessionLaunchDates.containsKey(session.getUid())) {
		sessionLaunchDate = sessionLaunchDates.get(session.getUid());
		timeLimitAdjustment = sessionTimeLimitAdjustments.get(session.getUid());
	    } else {
		Date launchDate = session.getTimeLimitLaunchedDate();
		if (launchDate != null) {
		    sessionLaunchDate = launchDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		    timeLimitAdjustment = session.getTimeLimitAdjustment();

		    sessionLaunchDates.put(session.getUid(), sessionLaunchDate);
		    sessionTimeLimitAdjustments.put(session.getUid(), timeLimitAdjustment);
		}
	    }

	    if (sessionLaunchDate != null) {
		existingTimeSettings.timeLimitLaunchedDate.put(userId, sessionLaunchDate);
		if (timeLimitAdjustment != null) {
		    existingTimeSettings.timeLimitAdjustment.put(userId, timeLimitAdjustment);
		}
	    }
	}

	return existingTimeSettings;
    }

    @Override
    protected LocalDateTime launchUserTimeLimit(long toolContentId, int userId) {
	return scratchieService.launchTimeLimit(toolContentId, userId);
    }

    @Override
    protected boolean processActivity(long toolContentId, Collection<Session> websockets) throws IOException {
	boolean result = super.processActivity(toolContentId, websockets);
	if (!result) {
	    // there are no more learners on the learner screen, so remove cached answers
	    scratchCache.remove(toolContentId);
	    return false;
	}

	Map<Long, Map<Long, Map<String, Boolean>>> sessionScratchCache = scratchCache.get(toolContentId);

	for (Long toolSessionId : sessionScratchCache.keySet()) {
	    ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();

	    Collection<ScratchieItem> items = scratchieService.getItemsWithIndicatedScratches(toolSessionId);
	    Map<Long, Map<String, Boolean>> questionCache = sessionScratchCache.get(toolSessionId);
	    for (ScratchieItem item : items) {
		Long itemUid = item.getUid();
		// do not init variables below until it's really needed
		Map<String, Boolean> itemCache = null;
		ObjectNode itemJSON = null;
		for (OptionDTO optionDto : item.getOptionDtos()) {
		    if (optionDto.isScratched()) {
			// answer is scratched, check if it is present in cache
			if (itemCache == null) {
			    itemCache = questionCache.get(itemUid);
			    if (itemCache == null) {
				itemCache = new TreeMap<>();
				questionCache.put(itemUid, itemCache);
			    }
			}

			boolean isMCQItem = QbQuestion.TYPE_MULTIPLE_CHOICE == item.getQbQuestion().getType()
				|| QbQuestion.TYPE_MARK_HEDGING == item.getQbQuestion().getType();
			String optionUidOrAnswer = isMCQItem ? optionDto.getQbOptionUid().toString()
				: optionDto.getAnswer();
			Boolean isCorrectStoredAnswer = optionDto.isCorrect();

			Boolean answerCache = itemCache.get(optionUidOrAnswer);
			// check if the correct answer is stored in cache
			if ((answerCache == null) || !answerCache.equals(isCorrectStoredAnswer)) {
			    // send only updates, nothing Learners are already aware of
			    itemCache.put(optionUidOrAnswer, isCorrectStoredAnswer);
			    if (itemJSON == null) {
				itemJSON = JsonNodeFactory.instance.objectNode();
			    }
			    ObjectNode optionPropertiesJSON = JsonNodeFactory.instance.objectNode();
			    optionPropertiesJSON.put("isCorrect", isCorrectStoredAnswer);
			    optionPropertiesJSON.put("isVSA", !isMCQItem);
			    itemJSON.set(optionUidOrAnswer, optionPropertiesJSON);
			}
		    }
		}
		if (itemJSON != null) {
		    responseJSON.set(itemUid.toString(), itemJSON);
		}
	    }

	    // are there any updates to send?
	    if (responseJSON.size() > 0) {
		String response = responseJSON.toString();
		for (Session websocket : websockets) {
		    long userSessionId = (Long) websocket.getUserProperties().get(AttributeNames.PARAM_TOOL_SESSION_ID);
		    if (toolSessionId.equals(userSessionId)) {
			websocket.getBasicRemote().sendText(response);
		    }
		}
	    }
	}

	return true;
    }

    public static Long getSecondsLeft(long toolContentId, int userId) {
	LearningWebsocketServer instance = LearningWebsocketServer.getInstance();
	// get time limit launch date only if it exists; only leader launches the timer
	return AbstractTimeLimitWebsocketServer.getSecondsLeft(instance, toolContentId, userId, false);
    }

    /**
     * The time limit is expired but leader hasn't submitted required notebook/burning questions yet. Non-leaders
     * will need to refresh the page in order to stop showing them questions page.
     */
    public void sendPageRefreshRequest(Long toolContentId, long toolSessionId) throws IOException {
	if (toolContentId == null) {
	    // get tool content ID from seession
	    ScratchieSession scratchieSession = scratchieService.getScratchieSessionBySessionId(toolSessionId);
	    toolContentId = scratchieSession.getScratchie().getContentId();
	}

	Set<Session> sessionWebsockets = websockets.get(toolContentId);
	if (sessionWebsockets == null) {
	    return;
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("pageRefresh", true);
	String response = responseJSON.toString();

	for (Session websocket : sessionWebsockets) {
	    long userSessionId = (Long) websocket.getUserProperties().get(AttributeNames.PARAM_TOOL_SESSION_ID);
	    if (toolSessionId == userSessionId) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }
}