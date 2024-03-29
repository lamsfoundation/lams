package org.lamsfoundation.lams.learning.command;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.command.model.Command;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Receives, processes and sends messages and commands to Learners.
 *
 * @author Marcin Cieslak
 */
@ServerEndpoint("/commandWebsocket")
public class CommandWebsocketServer {

    private static ILearnerFullService learnerService;

    /**
     * A singleton which updates Learners with messages and commands.
     */
    private static class SendWorker extends Thread {
	private boolean stopFlag = false;
	// mapping lessonId -> timestamp when the check was last performed, so the thread does not run too often
	private final Map<Long, Long> lastSendTimes = new TreeMap<>();

	@Override
	public void run() {
	    while (!stopFlag) {
		try {
		    // websocket communication bypasses standard HTTP filters, so Hibernate session needs to be initialised manually
		    HibernateSessionManager.openSession();

		    Iterator<Entry<Long, Map<String, Session>>> entryIterator = CommandWebsocketServer.websockets.entrySet()
			    .iterator();

		    // go through lessons and update registered learners with messages
		    while (entryIterator.hasNext()) {
			Entry<Long, Map<String, Session>> entry = entryIterator.next();
			if (entry != null) {
			    Long lessonId = entry.getKey();
			    Map<String, Session> lessonWebsockets = entry.getValue();
			    // if all learners left the lesson, remove the obsolete mapping
			    if (lessonWebsockets == null || lessonWebsockets.isEmpty()) {
				entryIterator.remove();
				lastSendTimes.remove(lessonId);
				continue;
			    }

			    send(lessonId, lessonWebsockets);
			}
		    }
		} catch (Exception e) {
		    // error caught, but carry on
		    CommandWebsocketServer.log.error("Error in Command Websocket Server worker thread", e);
		} finally {
		    try {
			HibernateSessionManager.closeSession();
			Thread.sleep(ILearnerService.COMMAND_WEBSOCKET_CHECK_INTERVAL);
		    } catch (IllegalStateException | InterruptedException e) {
			stopFlag = true;
			log.warn("Stopping Command Websocket worker thread");
		    }
		}
	    }
	}

	/**
	 * Feeds opened websockets with commands.
	 */
	private void send(Long lessonId, Map<String, Session> lessonWebsockets) throws IOException {
	    Long lastSendTime = lastSendTimes.get(lessonId);
	    if (lastSendTime == null) {
		lastSendTime = System.currentTimeMillis() - ILearnerService.COMMAND_WEBSOCKET_CHECK_INTERVAL;
	    }
	    lastSendTimes.put(lessonId, System.currentTimeMillis());

	    List<Command> commands = CommandWebsocketServer.getLearnerService()
		    .getCommandsForLesson(lessonId, new Date(lastSendTime));
	    for (Command command : commands) {
		try {
		    Session websocket = lessonWebsockets.get(command.getUserName());
		    if (websocket != null && websocket.isOpen()) {
			websocket.getBasicRemote().sendText(command.getCommandText());
		    }
		} catch (Exception e) {
		    CommandWebsocketServer.log.error(
			    "Error while sending command " + command.getCommandText() + "\" for \""
				    + command.getUserName() + "\"", e);
		}
	    }
	}
    }

    private static Logger log = Logger.getLogger(CommandWebsocketServer.class);

    private static final SendWorker sendWorker = new SendWorker();
    private static final Map<Long, Map<String, Session>> websockets = new ConcurrentHashMap<>();

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
	    sessionWebsockets = new ConcurrentHashMap<>();
	    CommandWebsocketServer.websockets.put(lessonId, sessionWebsockets);
	}

	String login = websocket.getUserPrincipal().getName();
	sessionWebsockets.put(login, websocket);
    }

    /**
     * Removes Learner websocket from the collection.
     */
    @OnClose
    public void unregisterUser(Session session) {
	String login = session.getUserPrincipal().getName();
	if (login == null) {
	    return;
	}

	Long lessonId = Long.valueOf(session.getRequestParameterMap().get(AttributeNames.PARAM_LESSON_ID).get(0));
	Map<String, Session> lessonWebsockets = CommandWebsocketServer.websockets.get(lessonId);
	if (lessonWebsockets == null) {
	    return;
	}

	lessonWebsockets.remove(login);
    }

    private static ILearnerFullService getLearnerService() {
	if (learnerService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(
		    SessionManager.getServletContext());
	    learnerService = (ILearnerFullService) ctx.getBean("learnerService");
	}
	return learnerService;
    }
}