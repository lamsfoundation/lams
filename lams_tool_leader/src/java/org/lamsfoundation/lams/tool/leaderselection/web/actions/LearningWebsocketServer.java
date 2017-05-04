package org.lamsfoundation.lams.tool.leaderselection.web.actions;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
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
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Sends leader has been selected event to the learners.
 *
 * @author Marcin Cieslak
 * @author Andrey Balan
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

    private static Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static final Map<Long, Set<Session>> websockets = new ConcurrentHashMap<Long, Set<Session>>();

    /**
     * Registeres the Learner for processing.
     */
    @OnOpen
    public void registerUser(Session websocket) throws JSONException, IOException {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	Set<Session> sessionWebsockets = websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = ConcurrentHashMap.newKeySet();
	    websockets.put(toolSessionId, sessionWebsockets);
	}
	sessionWebsockets.add(websocket);

	if (log.isDebugEnabled()) {
	    log.debug("User " + websocket.getUserPrincipal().getName()
		    + " entered Leader Selection with toolSessionId: " + toolSessionId);
	}
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session websocket, CloseReason reason) {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	websockets.get(toolSessionId).remove(websocket);

	if (log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    log.debug("User " + websocket.getUserPrincipal().getName() + " left Leader Selection with Tool Session ID: "
		    + toolSessionId
		    + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
			    || reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE))
				    ? ". Abnormal close. Code: " + reason.getCloseCode() + ". Reason: "
					    + reason.getReasonPhrase()
				    : ""));
	}
    }

    /**
     * This method is called when leader has just been selected and all non-leaders should refresh their pages in order
     * to see new leader name and a Finish button.
     */
    public static void sendPageRefreshRequest(Long toolSessionId) throws JSONException, IOException {
	Set<Session> sessionWebsockets = websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    return;
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("pageRefresh", true);
	String response = responseJSON.toString();

	for (Session websocket : sessionWebsockets) {
	    if (websocket.isOpen()) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }
}