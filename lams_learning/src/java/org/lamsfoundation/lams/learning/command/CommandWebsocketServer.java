package org.lamsfoundation.lams.learning.command;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Receives, processes and sends messages and commands to Learners.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/commandWebsocket")
public class CommandWebsocketServer {

    private static ILearnerService learnerService;

    /**
     * A singleton which updates Learners with messages and commands.
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

		    // synchronize websockets as a new Learner entering Learner interface could modify this collection
		    Iterator<Entry<Long, Map<String, Session>>> entryIterator = null;
		    synchronized (CommandWebsocketServer.websockets) {
			entryIterator = CommandWebsocketServer.websockets.entrySet().iterator();
		    }

		    Entry<Long, Map<String, Session>> entry = null;
		    // go through lessons and update registered learners with messages
		    do {
			synchronized (CommandWebsocketServer.websockets) {
			    entry = entryIterator.hasNext() ? entryIterator.next() : null;
			}

			if (entry != null) {
			    Long lessonId = entry.getKey();
			    Long lastSendTime = lastSendTimes.get(lessonId);
			    if ((lastSendTime == null)
				    || ((System.currentTimeMillis() - lastSendTime) >= SendWorker.CHECK_INTERVAL)) {
				send(lessonId);
			    }

			    // if all learners left the lesson, remove the obsolete mapping
			    Map<String, Session> lessonWebsockets = entry.getValue();
			    if (lessonWebsockets.isEmpty()) {
				synchronized (CommandWebsocketServer.websockets) {
				    entryIterator.remove();
				}
				lastSendTimes.remove(lessonId);
			    }
			}
		    } while (entry != null);

		    Thread.sleep(SendWorker.CHECK_INTERVAL);
		} catch (InterruptedException e) {
		    CommandWebsocketServer.log.warn("Stopping Command Websocket Server worker thread");
		    stopFlag = true;
		} catch (Exception e) {
		    // error caught, but carry on
		    CommandWebsocketServer.log.error("Error in Command Websocket Server worker thread", e);
		}
	    }
	}

	/**
	 * Feeds opened websockets with messages.
	 */
	private void send(Long lessonId) throws IOException {
	    Long lastSendTime = lastSendTimes.get(lessonId);
	    lastSendTimes.put(lessonId, System.currentTimeMillis());

	    List<Command> commands = CommandWebsocketServer.getLearnerService().getCommandsForLesson(lessonId,
		    new Date(lastSendTime));
	    Map<String, Session> lessonWebsockets = CommandWebsocketServer.websockets.get(lessonId);
	    for (Command command : commands) {
		Session websocket = lessonWebsockets.get(command.getUserName());
		if (websocket != null && websocket.isOpen()) {
		    websocket.getBasicRemote().sendText(command.getCommandText());
		}
	    }
	}
    }

    private static Logger log = Logger.getLogger(CommandWebsocketServer.class);

    private static final SendWorker sendWorker = new SendWorker();
    private static final Map<Long, Map<String, Session>> websockets = Collections
	    .synchronizedMap(new TreeMap<Long, Map<String, Session>>());

    static {
	// run the singleton thread
	CommandWebsocketServer.sendWorker.start();
    }

    /**
     * Registeres the Learner for processing by SendWorker.
     */
    @OnOpen
    public void registerUser(Session websocket) throws IOException {
	Long lessonId = Long.valueOf(websocket.getRequestParameterMap().get(AttributeNames.PARAM_LESSON_ID).get(0));
	Map<String, Session> sessionWebsockets = CommandWebsocketServer.websockets.get(lessonId);
	if (sessionWebsockets == null) {
	    sessionWebsockets = Collections.synchronizedMap(new TreeMap<String, Session>());
	    CommandWebsocketServer.websockets.put(lessonId, sessionWebsockets);
	}

	String login = websocket.getUserPrincipal().getName();
	sessionWebsockets.put(login, websocket);
    }

    /**
     * Removes Learner websocket from the collection.
     */
    @OnClose
    public void unregisterUser(Session session, CloseReason reason) {
	Long lessonId = Long.valueOf(session.getRequestParameterMap().get(AttributeNames.PARAM_LESSON_ID).get(0));
	Map<String, Session> lessonWebsockets = CommandWebsocketServer.websockets.get(lessonId);
	String login = session.getUserPrincipal().getName();
	lessonWebsockets.remove(login);
    }

    private static ILearnerService getLearnerService() {
	if (learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    learnerService = (ILearnerService) ctx.getBean("learnerService");
	}
	return learnerService;
    }
}