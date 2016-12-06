package org.lamsfoundation.lams.tool.scratchie.web.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.service.ScratchieServiceProxy;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

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
		    // synchronize websockets as a new Learner entering the activity could modify this collection
		    synchronized (LearningWebsocketServer.websockets) {
			Iterator<Entry<Long, Set<Session>>> entryIterator = LearningWebsocketServer.websockets
				.entrySet().iterator();
			// go through activities and update registered learners with reports and vote count
			while (entryIterator.hasNext()) {
			    Entry<Long, Set<Session>> entry = entryIterator.next();
			    Long toolSessionId = entry.getKey();
			    try {
				send(toolSessionId);
			    } catch (JSONException e) {
				LearningWebsocketServer.log.error("Error while building Scratchie answer JSON", e);
			    }
			    // if all learners left the activity, remove the obsolete mapping
			    Set<Session> sessionWebsockets = entry.getValue();
			    if (sessionWebsockets.isEmpty()) {
				entryIterator.remove();
				LearningWebsocketServer.cache.remove(toolSessionId);
			    }
			}
		    }

		    Thread.sleep(SendWorker.CHECK_INTERVAL);
		} catch (InterruptedException e) {
		    LearningWebsocketServer.log.warn("Stopping Scratchie worker thread");
		    stopFlag = true;
		} catch (Exception e) {
		    // error caught, but carry on
		    LearningWebsocketServer.log.error("Error in Scratchie worker thread", e);
		}
	    }
	}

	/**
	 * Feeds websockets with scratched answers.
	 */
	@SuppressWarnings("unchecked")
	private void send(Long toolSessionId) throws JSONException, IOException {
	    JSONObject responseJSON = new JSONObject();
	    // websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
	    // A new session needs to be created on each thread run as the session keeps stale Hibernate data (single transaction).
	    HibernateSessionManager.bindHibernateSessionToCurrentThread(true);

	    Collection<ScratchieItem> items = LearningWebsocketServer.getScratchieService()
		    .getItemsWithIndicatedScratches(toolSessionId);
	    Map<Long, Map<Long, Boolean>> sessionCache = null;
	    for (ScratchieItem item : items) {
		Long itemUid = item.getUid();
		// do not init variables below until it's really needed
		Map<Long, Boolean> itemCache = null;
		JSONObject itemJSON = null;
		for (ScratchieAnswer answer : (Set<ScratchieAnswer>) item.getAnswers()) {
		    if (answer.isScratched()) {
			// answer is scratched, check if it is present in cache
			if (itemCache == null) {
			    // init required cache variables
			    if (sessionCache == null) {
				sessionCache = LearningWebsocketServer.cache.get(toolSessionId);
				if (sessionCache == null) {
				    sessionCache = new TreeMap<Long, Map<Long, Boolean>>();
				    LearningWebsocketServer.cache.put(toolSessionId, sessionCache);
				}
			    }
			    itemCache = sessionCache.get(itemUid);
			    if (itemCache == null) {
				itemCache = new TreeMap<Long, Boolean>();
				sessionCache.put(itemUid, itemCache);
			    }
			}

			Long answerUid = answer.getUid();
			Boolean answerStoredIsCorrect = answer.isCorrect();
			Boolean answerCache = itemCache.get(answerUid);
			// check if the correct answer is stored in cache
			if ((answerCache == null) || !answerCache.equals(answerStoredIsCorrect)) {
			    // send only updates, nothing Learners are already aware of
			    itemCache.put(answerUid, answerStoredIsCorrect);
			    if (itemJSON == null) {
				itemJSON = new JSONObject();
			    }
			    itemJSON.put(answerUid.toString(), answerStoredIsCorrect);
			}
		    }
		}
		if (itemJSON != null) {
		    responseJSON.put(itemUid.toString(), itemJSON);
		}
	    }

	    // are there any updates to send?
	    if (responseJSON.length() == 0) {
		return;
	    }

	    String response = responseJSON.toString();
	    // make a copy of the websocket collection so it does not get blocked while sending messages
	    Set<Session> sessionWebsockets = new HashSet<Session>(
		    LearningWebsocketServer.websockets.get(toolSessionId));
	    for (Session websocket : sessionWebsockets) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

    private static Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static IScratchieService scratchieService;

    private static final SendWorker sendWorker = new SendWorker();
    // maps toolSessionId -> itemUid -> answerUid -> isCorrect
    private static final Map<Long, Map<Long, Map<Long, Boolean>>> cache = Collections
	    .synchronizedMap(new TreeMap<Long, Map<Long, Map<Long, Boolean>>>());
    private static final Map<Long, Set<Session>> websockets = Collections
	    .synchronizedMap(new TreeMap<Long, Set<Session>>());

    static {
	// run the singleton thread
	LearningWebsocketServer.sendWorker.start();
    }

    /**
     * Registeres the Learner for processing by SendWorker.
     */
    @OnOpen
    public void registerUser(Session websocket) throws JSONException, IOException {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = Collections.synchronizedSet(new HashSet<Session>());
	    LearningWebsocketServer.websockets.put(toolSessionId, sessionWebsockets);
	}
	sessionWebsockets.add(websocket);

	if (LearningWebsocketServer.log.isDebugEnabled()) {
	    LearningWebsocketServer.log.debug("User " + websocket.getUserPrincipal().getName()
		    + " entered Scratchie with toolSessionId: " + toolSessionId);
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

	if (LearningWebsocketServer.log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    LearningWebsocketServer.log.debug("User " + websocket.getUserPrincipal().getName()
		    + " left Scratchie with Tool Session ID: " + toolSessionId
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
    public static void sendCloseRequest(Long toolSessionId) throws JSONException, IOException {
	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    return;
	}
	// make a copy of the websocket collection so it does not get blocked while sending messages
	sessionWebsockets = new HashSet<Session>(sessionWebsockets);

	JSONObject responseJSON = new JSONObject();
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
    public static void sendPageRefreshRequest(Long toolSessionId) throws JSONException, IOException {
	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    return;
	}
	// make a copy of the websocket collection so it does not get blocked while sending messages
	sessionWebsockets = new HashSet<Session>(sessionWebsockets);

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("pageRefresh", true);
	String response = responseJSON.toString();

	for (Session websocket : sessionWebsockets) {
	    if (websocket.isOpen()) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

    private static IScratchieService getScratchieService() {
	if (LearningWebsocketServer.scratchieService == null) {
	    LearningWebsocketServer.scratchieService = ScratchieServiceProxy
		    .getScratchieService(SessionManager.getServletContext());
	}
	return LearningWebsocketServer.scratchieService;
    }
}