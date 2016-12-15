package org.lamsfoundation.lams.learning.presence;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.presence.model.PresenceChatMessage;
import org.lamsfoundation.lams.learning.presence.model.PresenceChatUser;
import org.lamsfoundation.lams.learning.presence.service.IPresenceChatService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Receives, processes and sends Lesson Chat messages to Learners.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/presenceChatWebsocket")
public class PresenceWebsocketServer {

    /**
     * Identifies a single connection. There can be more than one connection for the same user: multiple windows open or
     * the same user in an another role.
     */
    private static class Websocket {
	private final Session session;
	private final String nickName;

	private Websocket(Session session) {
	    this.session = session;
	    this.nickName = session.getRequestParameterMap().get("nickname").get(0);
	}
    }

    /**
     * A singleton which updates Learners with messages and roster.
     */
    private static class SendWorker extends Thread {
	private boolean stopFlag = false;
	// how ofter the thread runs
	private static final long CHECK_INTERVAL = 2000;
	// mapping lessonId -> timestamp when the check was last performed, so the thread does not run too often
	private final Map<Long, Long> lastSendTimes = new TreeMap<Long, Long>();

	@Override
	public void run() {
	    while (!stopFlag) {
		try {
		    // websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		    // A new session needs to be created on each thread run as the session keeps stale Hibernate data (single transaction).
		    HibernateSessionManager.bindHibernateSessionToCurrentThread(true);

		    // synchronize websockets as a new Learner entering chat could modify this collection
		    synchronized (PresenceWebsocketServer.websockets) {
			Iterator<Entry<Long, Set<Websocket>>> entryIterator = PresenceWebsocketServer.websockets
				.entrySet().iterator();
			// go through lessons and update registered learners with messages and roster
			while (entryIterator.hasNext()) {
			    Entry<Long, Set<Websocket>> entry = entryIterator.next();
			    Long lessonId = entry.getKey();
			    Long lastSendTime = lastSendTimes.get(lessonId);
			    if ((lastSendTime == null)
				    || ((System.currentTimeMillis() - lastSendTime) >= SendWorker.CHECK_INTERVAL)) {
				send(lessonId, null);
			    }

			    // if all learners left the chat, remove the obsolete mapping
			    Set<Websocket> lessonWebsockets = entry.getValue();
			    if (lessonWebsockets.isEmpty()) {
				entryIterator.remove();
				PresenceWebsocketServer.rosters.remove(lessonId);
				lastSendTimes.remove(lessonId);
			    }
			}
		    }

		    Thread.sleep(SendWorker.CHECK_INTERVAL);
		} catch (InterruptedException e) {
		    PresenceWebsocketServer.log.warn("Stopping Presence Chat worker thread");
		    stopFlag = true;
		} catch (Exception e) {
		    // error caught, but carry on
		    PresenceWebsocketServer.log.error("Error in Presence Chat worker thread", e);
		}
	    }
	}

	/**
	 * Feeds opened websockets with messages and roster.
	 */
	private void send(Long lessonId, String nickName) {
	    Long lastSendTime = lastSendTimes.get(lessonId);
	    // fetch messages a bit earlier than the last run, in case there was a lag somewhere
	    // JS code will filter out duplicates
	    lastSendTime = (lastSendTime == null) || (nickName != null) ? 0 : lastSendTime - 1000;
	    List<PresenceChatMessage> messages = PresenceWebsocketServer.getPresenceChatService()
		    .getNewMessages(lessonId, new Date(lastSendTime));
	    // update the timestamp, if this is a regular run
	    if (nickName == null) {
		lastSendTimes.put(lessonId, System.currentTimeMillis());
	    }

	    Set<Websocket> lessonWebsockets = new HashSet<Websocket>(PresenceWebsocketServer.websockets.get(lessonId));
	    Roster roster = PresenceWebsocketServer.rosters.get(lessonId);
	    JSONArray rosterJSON = roster.getRosterJSON();
	    // make a copy of the websocket collection so it does not get blocked while sending messages
	    for (Websocket websocket : lessonWebsockets) {
		// if this run is meant only for one learner, skip the others
		if ((nickName != null) && !nickName.equals(websocket.nickName)) {
		    continue;
		}

		// the connection is valid, carry on
		JSONObject responseJSON = new JSONObject();

		try {
		    // if it is just roster, skip messages
		    if (roster.imEnabled) {
			JSONArray messagesJSON = PresenceWebsocketServer.filterMessages(messages, websocket.nickName);
			responseJSON.put("messages", messagesJSON);
		    }
		    responseJSON.put("roster", rosterJSON);

		    // send the payload to the Learner's browser
		    if (websocket.session.isOpen()) {
			websocket.session.getBasicRemote().sendText(responseJSON.toString());
		    }
		} catch (Exception e) {
		    PresenceWebsocketServer.log.error("Error while building message JSON", e);
		}
	    }
	}
    }

    /**
     * Keeps information of learners present in a lesson. Needs to work with DB so presence is visible in clustered
     * environment.
     */
    private static class Roster {
	private final Long lessonId;
	private final boolean imEnabled;
	// timestamp when DB was last hit
	private long lastDBCheckTime = 0;

	// Learners who are currently active
	private final Set<String> activeUsers = new TreeSet<String>();

	private Roster(Long lessonId, boolean imEnabled) {
	    this.lessonId = lessonId;
	    this.imEnabled = imEnabled;
	}

	/**
	 * Checks which Learners
	 */
	private JSONArray getRosterJSON() {
	    Set<String> localActiveUsers = new TreeSet<String>();
	    Set<Websocket> sessionWebsockets = PresenceWebsocketServer.websockets.get(lessonId);
	    // find out who is active locally
	    for (Websocket websocket : sessionWebsockets) {
		localActiveUsers.add(websocket.nickName);
	    }

	    // is it time to sync with the DB yet?
	    long currentTime = System.currentTimeMillis();
	    if ((currentTime - lastDBCheckTime) > IPresenceChatService.PRESENCE_IDLE_TIMEOUT) {
		// store Learners active on this node
		PresenceWebsocketServer.getPresenceChatService().updateUserPresence(lessonId, localActiveUsers);

		// read active Learners from all nodes
		List<PresenceChatUser> storedActiveUsers = PresenceWebsocketServer.getPresenceChatService()
			.getUsersActiveByLessonId(lessonId);
		// refresh current collection
		activeUsers.clear();
		for (PresenceChatUser activeUser : storedActiveUsers) {
		    activeUsers.add(activeUser.getNickname());
		}

		lastDBCheckTime = currentTime;
	    } else {
		// add users active on this node; no duplicates - it is a set, not a list
		activeUsers.addAll(localActiveUsers);
	    }

	    return new JSONArray(activeUsers);
	}
    }

    private static Logger log = Logger.getLogger(PresenceWebsocketServer.class);

    private static IPresenceChatService presenceChatService;

    private static final SendWorker sendWorker = new SendWorker();
    private static final Map<Long, Roster> rosters = Collections.synchronizedMap(new TreeMap<Long, Roster>());
    private static final Map<Long, Set<Websocket>> websockets = Collections
	    .synchronizedMap(new TreeMap<Long, Set<Websocket>>());

    static {
	// run the singleton thread
	PresenceWebsocketServer.sendWorker.start();
    }

    /**
     * Registeres the Learner for processing by SendWorker.
     */
    @OnOpen
    public void registerUser(Session session) throws IOException {
	Long lessonId = Long.valueOf(session.getRequestParameterMap().get(AttributeNames.PARAM_LESSON_ID).get(0));
	Set<Websocket> sessionWebsockets = PresenceWebsocketServer.websockets.get(lessonId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = Collections.synchronizedSet(new HashSet<Websocket>());
	    PresenceWebsocketServer.websockets.put(lessonId, sessionWebsockets);
	}
	Websocket websocket = new Websocket(session);
	sessionWebsockets.add(websocket);

	Roster roster = PresenceWebsocketServer.rosters.get(lessonId);
	if (roster == null) {
	    boolean imEnabled = Boolean.valueOf(session.getRequestParameterMap().get("imEnabled").get(0));
	    // build a new roster object
	    roster = new Roster(lessonId, imEnabled);
	    PresenceWebsocketServer.rosters.put(lessonId, roster);
	}

	// websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
	HibernateSessionManager.bindHibernateSessionToCurrentThread(false);
	// update the chat window immediatelly
	PresenceWebsocketServer.sendWorker.send(lessonId, websocket.nickName);

	if (PresenceWebsocketServer.log.isDebugEnabled()) {
	    PresenceWebsocketServer.log
		    .debug("User " + websocket.nickName + " entered Presence Chat with lesson ID: " + lessonId);
	}
    }

    /**
     * If there was something wrong with the connection, put it into logs.
     */
    @OnClose
    public void unregisterUser(Session session, CloseReason reason) {
	Long lessonId = Long.valueOf(session.getRequestParameterMap().get(AttributeNames.PARAM_LESSON_ID).get(0));
	Set<Websocket> lessonWebsockets = PresenceWebsocketServer.websockets.get(lessonId);
	synchronized (lessonWebsockets) {
	    Iterator<Websocket> websocketIterator = lessonWebsockets.iterator();
	    while (websocketIterator.hasNext()) {
		Websocket websocket = websocketIterator.next();
		if (websocket.session.equals(session)) {
		    websocketIterator.remove();
		    break;
		}
	    }
	}

	if (PresenceWebsocketServer.log.isDebugEnabled()) {
	    PresenceWebsocketServer.log.debug(
		    "User " + session.getUserPrincipal().getName() + " left Presence Chat with lessonId: " + lessonId
			    + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
				    || reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE))
					    ? ". Abnormal close. Code: " + reason.getCloseCode() + ". Reason: "
						    + reason.getReasonPhrase()
					    : ""));
	}
    }

    /**
     * Receives a message sent by Learner via a websocket.
     *
     * @throws IOException
     */
    @OnMessage
    public void receiveRequest(String input, Session session) throws JSONException, IOException {
	if (StringUtils.isBlank(input)) {
	    return;
	}
	JSONObject requestJSON = new JSONObject(input);
	switch (requestJSON.getString("type")) {
	    case "message":
		PresenceWebsocketServer.storeMessage(requestJSON, session);
		break;
	    case "fetchConversation":
		PresenceWebsocketServer.sendConversation(requestJSON, session);
		break;
	}
    }

    /**
     * Stores a message sent by a Learner.
     */
    private static void storeMessage(JSONObject requestJSON, Session session) throws JSONException {
	String message = requestJSON.getString("message");
	if (StringUtils.isBlank(message)) {
	    return;
	}
	Long lessonId = requestJSON.getLong("lessonID");

	// websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
	HibernateSessionManager.bindHibernateSessionToCurrentThread(false);

	String from = session.getRequestParameterMap().get("nickname").get(0);
	String to = requestJSON.getString("to");
	if (StringUtils.isBlank(to)) {
	    to = null;
	}

	PresenceWebsocketServer.getPresenceChatService().createPresenceChatMessage(lessonId, from, to, new Date(),
		message);
    }

    /**
     * Sends the whole message history between the current and the chosen learner.
     */
    private static void sendConversation(JSONObject requestJSON, Session session) throws JSONException, IOException {
	Long lessonId = requestJSON.getLong("lessonID");

	// websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
	HibernateSessionManager.bindHibernateSessionToCurrentThread(false);

	String to = requestJSON.getString("to");
	String from = session.getRequestParameterMap().get("nickname").get(0);

	List<PresenceChatMessage> messages = PresenceWebsocketServer.getPresenceChatService()
		.getMessagesByConversation(lessonId, from, to);
	JSONArray messagesJSON = new JSONArray();

	for (PresenceChatMessage message : messages) {
	    JSONObject messageJSON = PresenceWebsocketServer.buildMessageJSON(message);
	    messagesJSON.put(messageJSON);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("messages", messagesJSON);
	// send the payload to the Learner's browser
	session.getBasicRemote().sendText(responseJSON.toString());
    }

    /**
     * Filteres messages meant for the given user (group or personal).
     */
    private static JSONArray filterMessages(List<PresenceChatMessage> messages, String nickName) throws JSONException {
	JSONArray messagesJSON = new JSONArray();

	for (PresenceChatMessage message : messages) {
	    String messageTo = message.getTo();
	    if ((messageTo == null) || message.getTo().equals(nickName) || message.getFrom().equals(nickName)) {
		JSONObject messageJSON = PresenceWebsocketServer.buildMessageJSON(message);
		messagesJSON.put(messageJSON);
	    }
	}

	return messagesJSON;
    }

    private static JSONObject buildMessageJSON(PresenceChatMessage message) throws JSONException {
	JSONObject messageJSON = new JSONObject();
	messageJSON.put("uid", message.getUid());
	messageJSON.put("from", message.getFrom());
	messageJSON.put("to", message.getTo());
	messageJSON.put("dateSent", message.getDateSent());
	messageJSON.put("message", message.getMessage());
	return messageJSON;
    }

    public static int getActiveUserCount(long lessonId) {
	Set<Websocket> lessonWebsockets = PresenceWebsocketServer.websockets.get(lessonId);
	if (lessonWebsockets == null) {
	    return 0;
	}
	// there can be few websockets (browser windows) for a single learner
	Set<String> activeNicknames = new TreeSet<String>();
	synchronized (lessonWebsockets) {
	    for (Websocket websocket : lessonWebsockets) {
		activeNicknames.add(websocket.nickName);
	    }
	}
	return activeNicknames.size();
    }

    private static IPresenceChatService getPresenceChatService() {
	if (PresenceWebsocketServer.presenceChatService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    PresenceWebsocketServer.presenceChatService = (IPresenceChatService) ctx.getBean("presenceChatService");
	}
	return PresenceWebsocketServer.presenceChatService;
    }
}