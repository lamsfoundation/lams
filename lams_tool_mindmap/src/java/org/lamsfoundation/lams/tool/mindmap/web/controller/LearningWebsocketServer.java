package org.lamsfoundation.lams.tool.mindmap.web.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mindmap.dto.NotifyActionJSON;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapNode;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapRequest;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapUser;
import org.lamsfoundation.lams.tool.mindmap.service.IMindmapService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Sends Node changes to Learners.
 *
 * @author Fiona Malikoff, based on code from Marcin Cieslak
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

    /**
     * A singleton which updates Learners with node changes.
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
			    LearningWebsocketServer.globalIdCache.remove(toolSessionId);
			    continue;
			}
			SendWorker.send(toolSessionId, null, null);
		    }
		} catch (IllegalStateException e) {
		    // do nothing as server is probably shutting down and we could not obtain Hibernate session
		} catch (Exception e) {
		    // error caught, but carry on
		    LearningWebsocketServer.log.error("Error in Mindmap worker thread", e);
		} finally {
		    try {
			HibernateSessionManager.closeSession();
			Thread.sleep(SendWorker.CHECK_INTERVAL);
		    } catch (IllegalStateException | InterruptedException e) {
			stopFlag = true;
			LearningWebsocketServer.log.warn("Stopping Mindmap worker thread");
		    }
		}
	    }
	}

	/**
	 * Feeds websockets with reports and votes.
	 */
	private static void send(Long toolSessionId, Session newWebsocket, Long userLastGlobalId) throws IOException {
	    Long previousGlobalId = LearningWebsocketServer.globalIdCache.get(toolSessionId);
	    if (previousGlobalId == null) {
		// first time run, create the cache
		previousGlobalId = 0L;
		LearningWebsocketServer.globalIdCache.put(toolSessionId, previousGlobalId);
	    }

	    MindmapSession mindmapSession = LearningWebsocketServer.getMindmapService()
		    .getSessionBySessionId(toolSessionId);

	    ObjectNode responseJSON = null;
	    // new user joined. Send everything from their initial globalId sent when the websocket was created
	    if (newWebsocket != null) {
		responseJSON = SendWorker.getServerActionJSON(mindmapSession.getMindmap().getUid(),
			mindmapSession.getSessionId(), userLastGlobalId);
		newWebsocket.getBasicRemote().sendText(responseJSON.toString());

		// send all requests since previousGlobalId
	    } else {
		Long currentMaxGlobalId = LearningWebsocketServer.getMindmapService().getLastGlobalIdByMindmapId(
			mindmapSession.getMindmap().getUid(), mindmapSession.getSessionId());
		if (currentMaxGlobalId > previousGlobalId) {
		    responseJSON = SendWorker.getServerActionJSON(mindmapSession.getMindmap().getUid(),
			    mindmapSession.getSessionId(), previousGlobalId);
		    LearningWebsocketServer.globalIdCache.put(toolSessionId, currentMaxGlobalId);
		    Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
		    for (Session websocket : sessionWebsockets) {
			websocket.getBasicRemote().sendText(responseJSON.toString());
		    }
		}
	    }
	}

	private static final String RESPONSE_JSON_ACTIONS = "actions";

	private static ObjectNode getServerActionJSON(Long mindmapId, Long toolSessionId, Long lastActionId) {

	    ObjectNode sendObjectNode = JsonNodeFactory.instance.objectNode();
	    ArrayNode actions = JsonNodeFactory.instance.arrayNode();
	    sendObjectNode.set(RESPONSE_JSON_ACTIONS, actions);

	    List<MindmapRequest> requestsList = mindmapService.getLastRequestsAfterGlobalId(lastActionId, mindmapId,
		    toolSessionId);
	    for (Iterator<MindmapRequest> iterator = requestsList.iterator(); iterator.hasNext();) {
		MindmapRequest mindmapRequest = iterator.next();
		int requestType = mindmapRequest.getType();

		ObjectNode notifyRequestModel = null;
		MindmapNode mindmapNode = null;
		MindmapNode childMindmapNode = null;
		if ((requestType != 0)) {

		    mindmapNode = mindmapService.getMindmapNodeByUniqueIdSessionId(mindmapRequest.getNodeId(),
			    mindmapId, toolSessionId);
		    if (mindmapNode == null) {
			LearningWebsocketServer.log.error(
				"getServerActionJSON(): Error finding node while sending data about adding child, changing text or color. Cannot send request to clients. Request details:"
					+ mindmapRequest);
			continue;
		    }

		    if (requestType == 1) {
			childMindmapNode = mindmapService.getMindmapNodeByUniqueIdSessionId(
				mindmapRequest.getNodeChildId(), mindmapId, toolSessionId);
			if (childMindmapNode == null) {
			    LearningWebsocketServer.log.error(
				    "pollServerAction(): Error finding node  while sending data about creating a child node. Cannot send request to clients. Cannot send request to clients. Request details:"
					    + mindmapRequest);
			    continue;
			}
		    }
		}

		// delete node
		if (requestType == 0) {
		    notifyRequestModel = new NotifyActionJSON(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			    mindmapRequest.getType(), null, null, null, null);
		}
		// create node
		else if (requestType == 1) {
		    String creator = null;
		    MindmapUser mindmapUser = childMindmapNode.getUser();
		    if (mindmapUser != null) {
			creator = mindmapUser.getFirstName() + " " + mindmapUser.getLastName();
		    } else {
			creator = "Student";
		    }
		    notifyRequestModel = new NotifyActionJSON(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			    mindmapRequest.getType(), mindmapRequest.getNodeChildId(), childMindmapNode.getText(),
			    childMindmapNode.getColor(), creator);
		}
		// change color
		else if (requestType == 2) {
		    notifyRequestModel = new NotifyActionJSON(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			    mindmapRequest.getType(), null, null, mindmapNode.getColor(), null);
		}
		// change text
		else if (requestType == 3) {
		    notifyRequestModel = new NotifyActionJSON(mindmapRequest.getGlobalId(), mindmapRequest.getNodeId(),
			    mindmapRequest.getType(), null, mindmapNode.getText(), null, null);
		}

		actions.add(notifyRequestModel);
	    }

	    return sendObjectNode;
	}
    }

    private static Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static IMindmapService mindmapService;

    private static final SendWorker sendWorker = new SendWorker();
    // maps toolSessionId -> cached session data
    private static final Map<Long, Long> globalIdCache = new ConcurrentHashMap<>();
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
	String login = websocket.getUserPrincipal().getName();
	MindmapUser user = LearningWebsocketServer.getMindmapService().getUserByLoginAndSessionId(login, toolSessionId);
	if (user == null) {
	    throw new SecurityException("User \"" + login
		    + "\" is not a participant in Mindmap activity with tool session ID " + toolSessionId);
	}

	Long lastActionId = Long.valueOf(websocket.getRequestParameterMap().get("lastActionId").get(0));
	Set<Session> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = ConcurrentHashMap.newKeySet();
	    LearningWebsocketServer.websockets.put(toolSessionId, sessionWebsockets);
	}
	sessionWebsockets.add(websocket);

	if (LearningWebsocketServer.log.isDebugEnabled()) {
	    LearningWebsocketServer.log.debug("User " + login + " entered Mindmap with toolSessionId: " + toolSessionId
		    + " lastActionId: " + lastActionId);
	}

	new Thread(() -> {
	    try {
		HibernateSessionManager.openSession();
		SendWorker.send(toolSessionId, websocket, lastActionId);
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
    public void unregisterUser(Session websocket, CloseReason reason) {
	Long toolSessionId = Long
		.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	LearningWebsocketServer.websockets.get(toolSessionId).remove(websocket);

	if (LearningWebsocketServer.log.isDebugEnabled()) {
	    // If there was something wrong with the connection, put it into logs.
	    LearningWebsocketServer.log.debug("User " + websocket.getUserPrincipal().getName()
		    + " left Mindmap with Tool Session ID: " + toolSessionId
		    + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
			    || reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE))
				    ? ". Abnormal close. Code: " + reason.getCloseCode() + ". Reason: "
					    + reason.getReasonPhrase()
				    : ""));
	}
    }

    /**
     * Receives a message sent by Learner via a websocket.
     */
    @OnMessage
    public void receiveRequest(String input, Session websocket) {
	if (StringUtils.isBlank(input)) {
	    return;
	}
	if (input.equalsIgnoreCase("ping")) {
	    // just a ping every few minutes
	    return;
	}
	log.warn("Unexpected request received by Mindmap websocket. Message is being ignored. Message was: " + input);
    }

    private static IMindmapService getMindmapService() {
	if (LearningWebsocketServer.mindmapService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    LearningWebsocketServer.mindmapService = (IMindmapService) wac.getBean("mindmapService");
	}
	return LearningWebsocketServer.mindmapService;
    }

}