package org.lamsfoundation.lams.tool.leaderselection.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Sends leader has been selected event to the learners.
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
		    // go through activities and update registered learners with selected leader
		    while (entryIterator.hasNext()) {
			Entry<Long, Set<Session>> entry = entryIterator.next();
			Long toolSessionId = entry.getKey();
			// if all learners left the activity, remove the obsolete mapping
			Set<Session> sessionWebsockets = entry.getValue();
			if (sessionWebsockets.isEmpty()) {
			    entryIterator.remove();
			    continue;
			}

			LeaderselectionUser leader = LearningWebsocketServer.getLeaderService()
				.getSessionBySessionId(toolSessionId).getGroupLeader();
			Long existingLeaderUid = leaders.get(toolSessionId);

			// check if a leader has been selected or it has changed
			if ((existingLeaderUid == null && leader != null) || (existingLeaderUid != null
				&& (leader == null || !leader.getUid().equals(existingLeaderUid)))) {
			    leaders.put(toolSessionId, leader == null ? null : leader.getUid());
			    LearningWebsocketServer.sendPageRefreshRequest(toolSessionId);
			}
		    }
		} catch (IllegalStateException e) {
		    // do nothing as server is probably shutting down and we could not obtain Hibernate session
		} catch (Exception e) {
		    // error caught, but carry on
		    log.error("Error in Leader worker thread", e);
		} finally {
		    try {
			HibernateSessionManager.closeSession();
			Thread.sleep(SendWorker.CHECK_INTERVAL);
		    } catch (IllegalStateException | InterruptedException e) {
			stopFlag = true;
			LearningWebsocketServer.log.warn("Stopping Leader worker thread");
		    }
		}
	    }
	}
    };

    private static final Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static final SendWorker sendWorker = new SendWorker();
    private static final Map<Long, Set<Session>> websockets = new ConcurrentHashMap<>();
    private static final Map<Long, Long> leaders = new HashMap<>();
    private static ILeaderselectionService leaderService;

    static {
	// run the singleton thread
	LearningWebsocketServer.sendWorker.start();
    }

    /**
     * Registeres the Learner for processing.
     */
    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	String login = websocket.getUserPrincipal().getName();
	LeaderselectionUser user = LearningWebsocketServer.getLeaderService().getUserByLoginAndSessionId(login,
		toolSessionId);
	if (user == null) {
	    throw new SecurityException("User \"" + login
		    + "\" is not a participant in Leader Selection activity with tool session ID " + toolSessionId);
	}

	Set<Session> sessionWebsockets = websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = ConcurrentHashMap.newKeySet();
	    websockets.put(toolSessionId, sessionWebsockets);
	}
	sessionWebsockets.add(websocket);

	if (log.isDebugEnabled()) {
	    log.debug("User " + login + " entered Leader Selection with toolSessionId: " + toolSessionId);
	}
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session websocket) {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	websockets.get(toolSessionId).remove(websocket);

	if (log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    log.debug("User " + websocket.getUserPrincipal().getName() + " left Leader Selection with Tool Session ID: "
		    + toolSessionId);
	}
    }

    /**
     * This method is called when leader has been selected and all non-leaders should refresh their pages in order
     * to see new leader name and a Finish button.
     */
    public static void sendPageRefreshRequest(Long toolSessionId) throws IOException {
	Set<Session> sessionWebsockets = websockets.get(toolSessionId);
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

    private static ILeaderselectionService getLeaderService() {
	if (leaderService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    leaderService = (ILeaderselectionService) wac.getBean("leaderselectionService");
	}
	return leaderService;
    }
}