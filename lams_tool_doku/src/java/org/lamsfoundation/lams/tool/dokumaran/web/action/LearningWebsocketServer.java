package org.lamsfoundation.lams.tool.dokumaran.web.action;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
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
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Sends time limit start and +1min events to learners.
 *
 * @author Marcin Cieslak
 * @author Andrey Balan
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

    private static Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static final Map<Long, Set<Session>> websockets = Collections
	    .synchronizedMap(new TreeMap<Long, Set<Session>>());

    /**
     * Registeres the Learner for processing by SendWorker.
     */
    @OnOpen
    public void registerUser(Session websocket) throws JSONException, IOException {
	Long toolContentID = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_CONTENT_ID).get(0));
	Set<Session> toolContentWebsockets = websockets.get(toolContentID);
	if (toolContentWebsockets == null) {
	    toolContentWebsockets = Collections.synchronizedSet(new HashSet<Session>());
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
    public static void sendAddOneMinuteRequest(Long toolContentID) throws JSONException, IOException {
	Set<Session> toolContentWebsockets = websockets.get(toolContentID);
	if (toolContentWebsockets == null) {
	    return;
	}
	// make a copy of the websocket collection so it does not get blocked while sending messages
	toolContentWebsockets = new HashSet<Session>(toolContentWebsockets);

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("addOneMinute", true);
	String response = responseJSON.toString();

	for (Session websocket : toolContentWebsockets) {
	    if (websocket.isOpen()) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

    /**
     * Monitor has launched time limit. All learners will need to refresh the page in order to stop showing them
     * waitForTimeLimitLaunch page.
     */
    public static void sendPageRefreshRequest(Long toolContentID) throws JSONException, IOException {
	Set<Session> toolContentWebsockets = websockets.get(toolContentID);
	if (toolContentWebsockets == null) {
	    return;
	}
	// make a copy of the websocket collection so it does not get blocked while sending messages
	toolContentWebsockets = new HashSet<Session>(toolContentWebsockets);

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("pageRefresh", true);
	String response = responseJSON.toString();

	for (Session websocket : toolContentWebsockets) {
	    if (websocket.isOpen()) {
		websocket.getBasicRemote().sendText(response);
	    }
	}
    }

}