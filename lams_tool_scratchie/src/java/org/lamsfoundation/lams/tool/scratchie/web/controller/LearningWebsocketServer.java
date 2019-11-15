package org.lamsfoundation.lams.tool.scratchie.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.OptionDTO;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Sends Scratchies actions to non-leaders.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

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

			ScratchieSession toolSession = LearningWebsocketServer.getScratchieService()
				.getScratchieSessionBySessionId(toolSessionId);
			boolean timeLimitUp = false;
			boolean scratchingFinished = toolSession.isScratchingFinished();
			// is Scratchie time limited?
			if (toolSession.getTimeLimitLaunchedDate() != null) {
			    // missing whole cache is a marker that we already checked time limit and it is up
			    if (LearningWebsocketServer.cache.get(toolSessionId) == null) {
				timeLimitUp = true;
			    } else {
				// calculate whether time limit is up
				Calendar currentTime = new GregorianCalendar(TimeZone.getDefault());
				Calendar timeLimitFinishDate = new GregorianCalendar(TimeZone.getDefault());
				timeLimitFinishDate.setTime(toolSession.getTimeLimitLaunchedDate());
				timeLimitFinishDate.add(Calendar.MINUTE, toolSession.getScratchie().getTimeLimit());
				//adding 5 extra seconds to let leader auto-submit results and store them in DB
				timeLimitFinishDate.add(Calendar.SECOND, 5);
				if (timeLimitFinishDate.compareTo(currentTime) <= 0) {
				    // time is up
				    if (!scratchingFinished) {
					// Leader did not finish scratching yet? Pity. We do it for him
					LearningWebsocketServer.getScratchieService()
						.setScratchingFinished(toolSessionId);
					scratchingFinished = true;
				    }
				    // mark time limit as up with this trick
				    LearningWebsocketServer.cache.remove(toolSessionId);
				    timeLimitUp = true;
				}
			    }
			}

			boolean isWaitingForLeaderToSubmit = LearningWebsocketServer.getScratchieService()
				.isWaitingForLeaderToSubmitNotebook(toolSession);
			if (timeLimitUp) {
			    // time limit is up
			    if (isWaitingForLeaderToSubmit) {
				// if Leader did not finish burning questions or notebook, push non-leaders to wait page
				LearningWebsocketServer.sendPageRefreshRequest(toolSessionId);
			    } else {
				// if Leader finished everything, non-leaders see Finish button
				LearningWebsocketServer.sendCloseRequest(toolSessionId);
			    }
			} else if (scratchingFinished && !isWaitingForLeaderToSubmit) {
			    // time limit not set or not up yet, but everything is finished
			    // show non-leaders Finish button
			    LearningWebsocketServer.sendCloseRequest(toolSessionId);
			} else {
			    // regular send of scratched items
			    SendWorker.send(toolSessionId);
			}
		    }
		} catch (IllegalStateException e) {
		    // do nothing as server is probably shutting down and we could not obtain Hibernate session
		} catch (Exception e) {
		    // error caught, but carry on
		    log.error("Error in Scratchie worker thread", e);
		} finally {
		    try {
			HibernateSessionManager.closeSession();
			Thread.sleep(SendWorker.CHECK_INTERVAL);
		    } catch (IllegalStateException | InterruptedException e) {
			stopFlag = true;
			LearningWebsocketServer.log.warn("Stopping Scratchie worker thread");
		    }
		}
	    }
	}

	/**
	 * Feeds websockets with scratched options.
	 */
	private static void send(Long toolSessionId) throws IOException {
	    ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();

	    Collection<ScratchieItem> items = LearningWebsocketServer.getScratchieService()
		    .getItemsWithIndicatedScratches(toolSessionId);
	    Map<Long, Map<String, Boolean>> sessionCache = LearningWebsocketServer.cache.get(toolSessionId);
	    for (ScratchieItem item : items) {
		Long itemUid = item.getUid();
		// do not init variables below until it's really needed
		Map<String, Boolean> itemCache = null;
		ObjectNode itemJSON = null;
		for (OptionDTO optionDto : item.getOptionDtos()) {
		    if (optionDto.isScratched()) {
			// answer is scratched, check if it is present in cache
			if (itemCache == null) {
			    itemCache = sessionCache.get(itemUid);
			    if (itemCache == null) {
				itemCache = new TreeMap<>();
				sessionCache.put(itemUid, itemCache);
			    }
			}

			boolean isMCQItem = QbQuestion.TYPE_MULTIPLE_CHOICE == item.getQbQuestion().getType();
			String optionUidOrAnswer = isMCQItem
				? optionDto.getQbOptionUid().toString()
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
	    if (responseJSON.size() == 0) {
		return;
	    }

	    String response = responseJSON.toString();
	    Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	    for (Session websocket : sessionWebsockets) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

    private static final Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static IScratchieService scratchieService;

    private static final SendWorker sendWorker = new SendWorker();
    // maps toolSessionId -> itemUid -> optionUid -> isCorrect
    private static final Map<Long, Map<Long, Map<String, Boolean>>> cache = new ConcurrentHashMap<>();
    private static final Map<Long, Set<Session>> websockets = new ConcurrentHashMap<>();

    static {
	// run the singleton thread
	LearningWebsocketServer.sendWorker.start();
    }

    /**
     * Registeres the Learner for processing by SendWorker.
     */
    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = ConcurrentHashMap.newKeySet();
	    LearningWebsocketServer.websockets.put(toolSessionId, sessionWebsockets);

	    Map<Long, Map<String, Boolean>> sessionCache = new TreeMap<>();
	    LearningWebsocketServer.cache.put(toolSessionId, sessionCache);
	}
	sessionWebsockets.add(websocket);

	if (log.isDebugEnabled()) {
	    log.debug("User " + websocket.getUserPrincipal().getName() + " entered Scratchie with toolSessionId: "
		    + toolSessionId);
	}
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session websocket, CloseReason reason) {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	LearningWebsocketServer.websockets.get(toolSessionId).remove(websocket);

	if (log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    log.debug("User " + websocket.getUserPrincipal().getName() + " left Scratchie with Tool Session ID: "
		    + toolSessionId
		    + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
			    || reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE))
				    ? ". Abnormal close. Code: " + reason.getCloseCode() + ". Reason: "
					    + reason.getReasonPhrase()
				    : ""));
	}
    }

    /**
     * The leader finished scratching and also . Non-leaders will have
     * Finish button displayed.
     */
    public static void sendCloseRequest(Long toolSessionId) throws IOException {
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

    /**
     * The time limit is expired but leader hasn't submitted required notebook/burning questions yet. Non-leaders
     * will need to refresh the page in order to stop showing them questions page.
     */
    public static void sendPageRefreshRequest(Long toolSessionId) throws IOException {
	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    return;
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("pageRefresh", true);
	String response = responseJSON.toString();

	for (Session websocket : sessionWebsockets) {
	    if (websocket.isOpen()) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

    private static IScratchieService getScratchieService() {
	if (scratchieService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    scratchieService = (IScratchieService) wac.getBean(ScratchieConstants.SCRATCHIE_SERVICE);
	}
	return scratchieService;
    }
}