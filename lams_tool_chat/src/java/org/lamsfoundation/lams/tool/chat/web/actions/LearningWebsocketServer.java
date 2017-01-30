package org.lamsfoundation.lams.tool.chat.web.actions;

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
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Receives, processes and sends Chat messages to Learners.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/learningWebsocket")
public class LearningWebsocketServer {

    /**
     * Identifies a single connection. There can be more than one connection for the same user: multiple windows open or
     * the same user in an another role.
     */
    private static class Websocket {
	private Session session;
	private String userName;
	private String nickName;
	private String hash;

	private Websocket(Session session, String nickName) {
	    this.session = session;
	    this.userName = session.getUserPrincipal().getName();
	    this.nickName = nickName;
	}
    }

    /**
     * A singleton which updates Learners with messages and presence.
     */
    private static class SendWorker extends Thread {
	private boolean stopFlag = false;
	// how ofter the thread runs
	private static final long CHECK_INTERVAL = 2000;
	// mapping toolSessionId -> timestamp when the check was last performed, so the thread does not run too often
	private final Map<Long, Long> lastSendTimes = new TreeMap<Long, Long>();

	@Override
	public void run() {
	    while (!stopFlag) {
		try {
		    // websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		    HibernateSessionManager.openSession();
		    // synchronize websockets as a new Learner entering Chat could modify this collection
		    synchronized (LearningWebsocketServer.websockets) {
			Iterator<Entry<Long, Set<Websocket>>> entryIterator = LearningWebsocketServer.websockets
				.entrySet().iterator();
			// go throus Tool Session and update registered users with messages and roster
			while (entryIterator.hasNext()) {
			    Entry<Long, Set<Websocket>> entry = entryIterator.next();
			    Long toolSessionId = entry.getKey();
			    Long lastSendTime = lastSendTimes.get(toolSessionId);
			    if ((lastSendTime == null)
				    || ((System.currentTimeMillis() - lastSendTime) >= SendWorker.CHECK_INTERVAL)) {
				send(toolSessionId);
			    }
			    // if all users left the chat, remove the obsolete mapping
			    Set<Websocket> sessionWebsockets = entry.getValue();
			    if (sessionWebsockets.isEmpty()) {
				entryIterator.remove();
				LearningWebsocketServer.rosters.remove(toolSessionId);
				lastSendTimes.remove(toolSessionId);
			    }
			}
		    }
		} catch (Exception e) {
		    // error caught, but carry on
		    LearningWebsocketServer.log.error("Error in Chat worker thread", e);
		} finally {
		    HibernateSessionManager.closeSession();
		    try {
			Thread.sleep(SendWorker.CHECK_INTERVAL);
		    } catch (InterruptedException e) {
			LearningWebsocketServer.log.warn("Stopping Chat worker thread");
			stopFlag = true;
		    }
		}
	    }
	}

	/**
	 * Feeds opened websockets with messages and roster.
	 */
	private void send(Long toolSessionId) {
	    // update the timestamp
	    lastSendTimes.put(toolSessionId, System.currentTimeMillis());

	    ChatSession chatSession = LearningWebsocketServer.getChatService().getSessionBySessionId(toolSessionId);
	    List<ChatMessage> messages = LearningWebsocketServer.getChatService().getLastestMessages(chatSession, null,
		    true);
	    // make a copy of the websocket collection so it does not get blocked while sending messages
	    Set<Websocket> sessionWebsockets = new HashSet<Websocket>(
		    LearningWebsocketServer.websockets.get(toolSessionId));
	    Roster roster = null;
	    JSONArray rosterJSON = null;
	    String rosterString = null;
	    for (Websocket websocket : sessionWebsockets) {
		// the connection is valid, carry on
		JSONObject responseJSON = new JSONObject();
		// fetch roster only once, but messages are personalised
		if (rosterJSON == null) {
		    roster = LearningWebsocketServer.rosters.get(toolSessionId);
		    if (roster == null) {
			// build a new roster object
			roster = new Roster(toolSessionId);
			LearningWebsocketServer.rosters.put(toolSessionId, roster);
		    }

		    rosterJSON = roster.getRosterJSON();
		    rosterString = rosterJSON.toString();
		}

		try {
		    String userName = websocket.userName;
		    JSONArray messagesJSON = LearningWebsocketServer.getMessages(chatSession, messages, userName);
		    // if hash of roster and messages is the same as before, do not send the message, save the bandwidth
		    String hash = HashUtil.sha1(rosterString + messagesJSON.toString());
		    if ((websocket.hash == null) || !websocket.hash.equals(hash)) {
			websocket.hash = hash;

			responseJSON.put("messages", messagesJSON);
			responseJSON.put("roster", rosterJSON);

			// send the payload to the Learner's browser
			if (websocket.session.isOpen()) {
			    websocket.session.getBasicRemote().sendText(responseJSON.toString());
			}
		    }
		} catch (Exception e) {
		    LearningWebsocketServer.log.error("Error while building message JSON", e);
		}
	    }
	}
    }

    /**
     * Keeps information of users present in a Chat session. Needs to work with DB so presence is visible in clustered
     * environment.
     */
    private static class Roster {
	private Long toolSessionId = null;
	// timestamp when DB was last hit
	private long lastDBCheckTime = 0;

	// Learners who are currently active
	private final Set<String> activeUsers = new TreeSet<String>();

	private Roster(Long toolSessionId) {
	    this.toolSessionId = toolSessionId;
	}

	/**
	 * Checks which Learners
	 */
	private JSONArray getRosterJSON() {
	    Set<String> localActiveUsers = new TreeSet<String>();
	    Set<Websocket> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	    // find out who is active locally
	    for (Websocket websocket : sessionWebsockets) {
		localActiveUsers.add(websocket.nickName);
	    }

	    // is it time to sync with the DB yet?
	    long currentTime = System.currentTimeMillis();
	    if ((currentTime - lastDBCheckTime) > ChatConstants.PRESENCE_IDLE_TIMEOUT) {
		// store Learners active on this node
		LearningWebsocketServer.getChatService().updateUserPresence(toolSessionId, localActiveUsers);

		// read active Learners from all nodes
		List<ChatUser> storedActiveUsers = LearningWebsocketServer.getChatService()
			.getUsersActiveBySessionId(toolSessionId);
		// refresh current collection
		activeUsers.clear();
		for (ChatUser activeUser : storedActiveUsers) {
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

    private static Logger log = Logger.getLogger(LearningWebsocketServer.class);

    private static IChatService chatService;

    private static final SendWorker sendWorker = new SendWorker();
    private static final Map<Long, Roster> rosters = Collections.synchronizedMap(new TreeMap<Long, Roster>());
    private static final Map<Long, Set<Websocket>> websockets = Collections
	    .synchronizedMap(new TreeMap<Long, Set<Websocket>>());

    static {
	// run the singleton thread
	LearningWebsocketServer.sendWorker.start();
    }

    /**
     * Registeres the Learner for processing by SendWorker.
     */
    @OnOpen
    public void registerUser(Session session) throws IOException {
	Long toolSessionId = Long
		.valueOf(session.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	Set<Websocket> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = Collections.synchronizedSet(new HashSet<Websocket>());
	    LearningWebsocketServer.websockets.put(toolSessionId, sessionWebsockets);
	}
	final Set<Websocket> finalSessionWebsockets = sessionWebsockets;

	String userName = session.getUserPrincipal().getName();
	new Thread(() -> {
	    try {
		// websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		HibernateSessionManager.openSession();
		ChatUser chatUser = LearningWebsocketServer.getChatService().getUserByLoginNameAndSessionId(userName,
			toolSessionId);
		Websocket websocket = new Websocket(session, chatUser.getNickname());
		finalSessionWebsockets.add(websocket);

		// update the chat window immediatelly
		LearningWebsocketServer.sendWorker.send(toolSessionId);

		if (LearningWebsocketServer.log.isDebugEnabled()) {
		    LearningWebsocketServer.log
			    .debug("User " + userName + " entered Chat with toolSessionId: " + toolSessionId);
		}
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}).start();
    }

    /**
     * When user leaves the activity.
     */
    @OnClose
    public void unregisterUser(Session session, CloseReason reason) {
	Long toolSessionId = Long
		.valueOf(session.getRequestParameterMap().get(AttributeNames.PARAM_TOOL_SESSION_ID).get(0));
	Set<Websocket> sessionWebsockets = LearningWebsocketServer.websockets.get(toolSessionId);
	synchronized (sessionWebsockets) {
	    Iterator<Websocket> websocketIterator = sessionWebsockets.iterator();
	    while (websocketIterator.hasNext()) {
		Websocket websocket = websocketIterator.next();
		if (websocket.session.equals(session)) {
		    websocketIterator.remove();
		    break;
		}
	    }
	}

	if (LearningWebsocketServer.log.isDebugEnabled()) {
	    LearningWebsocketServer.log.debug(
		    "User " + session.getUserPrincipal().getName() + " left Chat with toolSessionId: " + toolSessionId
			    + (!(reason.getCloseCode().equals(CloseCodes.GOING_AWAY)
				    || reason.getCloseCode().equals(CloseCodes.NORMAL_CLOSURE))
					    ? ". Abnormal close. Code: " + reason.getCloseCode() + ". Reason: "
						    + reason.getReasonPhrase()
					    : ""));
	}
    }

    /**
     * Stores a message sent by a Learner.
     */
    @OnMessage
    public void receiveMessage(String input, Session session) throws JSONException {
	if (StringUtils.isBlank(input)) {
	    return;
	}
	JSONObject messageJSON = new JSONObject(input);
	String message = messageJSON.getString("message");
	if (StringUtils.isBlank(message)) {
	    return;
	}

	Long toolSessionId = messageJSON.getLong("toolSessionID");
	String toUser = messageJSON.getString("toUser");
	new Thread(() -> {
	    try {
		// websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		HibernateSessionManager.openSession();

		ChatUser toChatUser = null;
		if (!StringUtils.isBlank(toUser)) {
		    toChatUser = LearningWebsocketServer.getChatService().getUserByNicknameAndSessionID(toUser,
			    toolSessionId);
		    if (toChatUser == null) {
			// there should be an user, but he could not be found, so don't send the message to everyone
			LearningWebsocketServer.log
				.error("Could not find nick: " + toUser + " in session: " + toolSessionId);
			return;
		    }
		}

		ChatUser chatUser = LearningWebsocketServer.getChatService()
			.getUserByLoginNameAndSessionId(session.getUserPrincipal().getName(), toolSessionId);

		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setFromUser(chatUser);
		chatMessage.setChatSession(chatUser.getChatSession());
		chatMessage.setToUser(toChatUser);
		chatMessage.setType(
			toChatUser == null ? ChatMessage.MESSAGE_TYPE_PUBLIC : ChatMessage.MESSAGE_TYPE_PRIVATE);
		chatMessage.setBody(message);
		chatMessage.setSendDate(new Date());
		chatMessage.setHidden(Boolean.FALSE);
		LearningWebsocketServer.getChatService().saveOrUpdateChatMessage(chatMessage);
	    } catch (Exception e) {
		log.error("Error in thread", e);
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}).start();
    }

    /**
     * Filteres messages meant for the given user (group or personal).
     */
    private static JSONArray getMessages(ChatSession chatSession, List<ChatMessage> messages, String userName)
	    throws JSONException {
	JSONArray messagesJSON = new JSONArray();

	for (ChatMessage message : messages) {
	    // all messasges need to be written out, not only new ones,
	    // as old ones could have been edited or hidden by Monitor
	    if (!message.isHidden() && (message.getType().equals(ChatMessage.MESSAGE_TYPE_PUBLIC)
		    || message.getFromUser().getLoginName().equals(userName)
		    || message.getToUser().getLoginName().equals(userName))) {
		String filteredMessage = LearningWebsocketServer.getChatService().filterMessage(message.getBody(),
			chatSession.getChat());
		JSONObject messageJSON = new JSONObject();
		messageJSON.put("body", filteredMessage);
		messageJSON.put("from", message.getFromUser().getNickname());
		messageJSON.put("type", message.getType());
		messagesJSON.put(messageJSON);
	    }
	}

	return messagesJSON;
    }

    private static IChatService getChatService() {
	if (LearningWebsocketServer.chatService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(SessionManager.getServletContext());
	    LearningWebsocketServer.chatService = (IChatService) wac.getBean("chatService");
	}
	return LearningWebsocketServer.chatService;
    }
}