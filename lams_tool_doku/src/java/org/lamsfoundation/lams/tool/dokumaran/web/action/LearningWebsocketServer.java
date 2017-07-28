package org.lamsfoundation.lams.tool.dokumaran.web.action;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Sends time limit start and +1min events to learners.
 *
 * @author Marcin Cieslak
 * @author Andrey Balan
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

    /**
     * A singleton which updates Learners with Leader selection.
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
			Long toolContentId = entry.getKey();
			// if all learners left the activity, remove the obsolete mapping
			Set<Session> dokuWebsockets = entry.getValue();
			if (dokuWebsockets.isEmpty()) {
			    entryIterator.remove();
			    timeLimitCache.remove(toolContentId);
			    continue;
			}

			Dokumaran dokumaran = getDokumaranService().getDokumaranByContentId(toolContentId);
			int timeLimit = dokumaran.getTimeLimit();
			if (dokumaran.getTimeLimitLaunchedDate() != null && timeLimit != 0) {
			    Integer cachedTimeLimit = timeLimitCache.get(toolContentId);
			    if (cachedTimeLimit == null || !cachedTimeLimit.equals(timeLimit)) {
				timeLimitCache.put(toolContentId, timeLimit);
				sendAddTimeRequest(toolContentId,
					timeLimit - (cachedTimeLimit == null ? 0 : cachedTimeLimit));
			    }
			}
		    }
		} catch (Exception e) {
		    // error caught, but carry on
		    LearningWebsocketServer.log.error("Error in Dokumaran worker thread", e);
		} finally {
		    HibernateSessionManager.closeSession();
		    try {
			Thread.sleep(SendWorker.CHECK_INTERVAL);
		    } catch (InterruptedException e) {
			LearningWebsocketServer.log.warn("Stopping Dokumaran worker thread");
			stopFlag = true;
		    }
		}
	    }
	}
    };

    private static final Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static final SendWorker sendWorker = new SendWorker();
    private static final Map<Long, Set<Session>> websockets = new ConcurrentHashMap<Long, Set<Session>>();
    private static final Map<Long, Integer> timeLimitCache = new ConcurrentHashMap<Long, Integer>();

    private static IDokumaranService dokumaranService;

    static {
	// run the singleton thread
	LearningWebsocketServer.sendWorker.start();
    }

    /**
     * Registeres the Learner for processing.
     */
    @OnOpen
    public void registerUser(Session websocket) throws JSONException, IOException {
	Long toolContentID = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	Set<Session> toolContentWebsockets = websockets.get(toolContentID);
	if (toolContentWebsockets == null) {
	    toolContentWebsockets = ConcurrentHashMap.newKeySet();
	    websockets.put(toolContentID, toolContentWebsockets);
	}
	toolContentWebsockets.add(websocket);

	if (log.isDebugEnabled()) {
	    log.debug("User " + websocket.getUserPrincipal().getName() + " entered Dokumaran with toolContentId: "
		    + toolContentID);
	}
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session websocket, CloseReason reason) {
	Long toolContentID = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	websockets.get(toolContentID).remove(websocket);

	if (log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    log.debug("User " + websocket.getUserPrincipal().getName() + " left Dokumaran with Tool Content ID: "
		    + toolContentID
		    + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
			    || reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE))
				    ? ". Abnormal close. Code: " + reason.getCloseCode() + ". Reason: "
					    + reason.getReasonPhrase()
				    : ""));
	}
    }

    /**
     * Monitor has added one more minute to the time limit. All learners will need
     * to add +1 minute to their countdown counters.
     */
    public static void sendAddTimeRequest(Long toolContentId, int timeLimit) throws JSONException, IOException {
	Set<Session> toolContentWebsockets = websockets.get(toolContentId);
	if (toolContentWebsockets == null) {
	    return;
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("addTime", timeLimit);
	String response = responseJSON.toString();

	for (Session websocket : toolContentWebsockets) {
	    if (websocket.isOpen()) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

    private static IDokumaranService getDokumaranService() {
	if (dokumaranService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    dokumaranService = (IDokumaranService) wac.getBean(DokumaranConstants.RESOURCE_SERVICE);
	}
	return dokumaranService;
    }
}