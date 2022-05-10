package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.dao.EventDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.hibernate.HibernateSessionManager;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Provides tools for managing events and notifing users.
 *
 * @author Marcin Cieslak
 *
 */
public class EventNotificationService implements IEventNotificationService {

    private static final Logger log = Logger.getLogger(EventNotificationService.class);

    static {
	IEventNotificationService.availableDeliveryMethods.add(IEventNotificationService.DELIVERY_METHOD_MAIL);
	IEventNotificationService.availableDeliveryMethods.add(IEventNotificationService.DELIVERY_METHOD_NOTIFICATION);
    }

    private EventDAO eventDAO = null;
    private MessageService messageService = null;
    private ILessonService lessonService = null;
    private ILamsToolService toolService = null;
    private IUserManagementService userManagementService = null;

    @Override
    public void createEvent(String scope, String name, Long eventSessionId, String defaultSubject,
	    String defaultMessage, boolean isHtmlFormat) {
	if (!eventExists(scope, name, eventSessionId)) {
	    Event event = new Event(scope, name, eventSessionId, defaultSubject, defaultMessage, isHtmlFormat);
	    eventDAO.insertOrUpdate(event);
	}
    }

    @Override
    public void createLessonEvent(String scope, String name, Long toolContentId, String defaultSubject,
	    String defaultMessage, boolean isHtmlFormat, AbstractDeliveryMethod deliveryMethod)
	    throws InvalidParameterException {
	Lesson lesson = lessonService.getLessonByToolContentId(toolContentId);
	if (!eventExists(scope, name, lesson.getLessonId())) {
	    Event event = new Event(scope, name, lesson.getLessonId(), defaultSubject, defaultMessage, isHtmlFormat);
	    Set<User> users = null;
	    if (scope.equals(IEventNotificationService.LESSON_MONITORS_SCOPE)) {
		users = lesson.getLessonClass().getStaffGroup().getUsers();
	    } else if (scope.equals(IEventNotificationService.LESSON_LEARNERS_SCOPE)) {
		users = lesson.getLessonClass().getLearners();
	    }
	    if (users != null) {
		for (User user : users) {
		    event.getSubscriptions().add(new Subscription(user.getUserId(), deliveryMethod));
		}
		eventDAO.insert(event);
	    }
	}
    }

    @Override
    public boolean eventExists(String scope, String name, Long eventSessionId) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	return eventDAO.getEvent(scope, name, eventSessionId) != null;
    }

    @Override
    public Set<AbstractDeliveryMethod> getAvailableDeliveryMethods() {
	return IEventNotificationService.availableDeliveryMethods;
    }

    @Override
    public boolean isSubscribed(String scope, String name, Long eventSessionId, Long userId)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event != null) {
	    for (Subscription subscription : event.getSubscriptions()) {
		if (subscription.getUserId().equals(Integer.valueOf(userId.intValue()))) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public void notifyLessonMonitors(Long lessonId, String subject, String message, boolean isHtmlFormat) {
	Map<User, Boolean> monitoringUsers = lessonService.getUsersWithLessonParticipation(lessonId, "MONITOR", null,
		null, null, true, true);
	if (monitoringUsers.isEmpty()) {
	    return;
	}

	ArrayList<Integer> monitoringUsersIds = new ArrayList<>();
	for (Map.Entry<User, Boolean> entry : monitoringUsers.entrySet()) {
	    if (entry.getValue()) {
		monitoringUsersIds.add(entry.getKey().getUserId());
	    }
	}

	sendMessage(null, monitoringUsersIds.toArray(new Integer[monitoringUsersIds.size()]),
		IEventNotificationService.DELIVERY_METHOD_MAIL, subject, message, isHtmlFormat);
    }

    @Override
    public void notifyLessonMonitors(Long sessionId, String message, boolean isHtmlFormat) {
	List<User> monitoringUsers = lessonService.getMonitorsByToolSessionId(sessionId);
	if (monitoringUsers.isEmpty()) {
	    return;
	}

	Integer[] monitoringUsersIds = new Integer[monitoringUsers.size()];
	for (int i = 0; i < monitoringUsersIds.length; i++) {
	    monitoringUsersIds[i] = monitoringUsers.get(i).getUserId();
	}

	ToolSession toolSession = toolService.getToolSession(sessionId);
	Lesson lesson = toolSession.getLesson();
	ToolActivity toolActivity = toolSession.getToolActivity();
	String lessonName = lesson.getLessonName();
	String activityTitle = toolActivity.getTitle();
	String toolName = toolActivity.getTool().getToolDisplayName();
	String emailSubject = new StringBuilder(toolName).append(" ")
		.append(messageService.getMessage("email.notifications.tool")).append(": ").append(activityTitle)
		.append(" ").append(messageService.getMessage("email.notifications.activity")).append(" - ")
		.append(lessonName).append(" ").append(messageService.getMessage("email.notifications.lesson"))
		.toString();

	String courseName = lesson.getOrganisation().getName();
	String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL).trim();
	String emailBody = new StringBuilder(messageService.getMessage("email.notifications.course")).append(": ")
		.append(courseName).append('\n').append(messageService.getMessage("email.notifications.lesson.caption"))
		.append(": ").append(lessonName).append("\n\n").append(message).append("\n\n").append(serverUrl)
		.toString();

	sendMessage(null, monitoringUsersIds, IEventNotificationService.DELIVERY_METHOD_MAIL, emailSubject, emailBody,
		isHtmlFormat);
    }

    /**
     * Sends the message to the user. Properties storing information of the last notification attempt are updated.
     *
     * @param subject
     *            subject of the message; <code>null</code> if not applicable
     * @param message
     *            message to send
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     */
    public void notifyUser(Subscription subscription, String subject, String message, boolean isHtmlFormat) {
	log.debug("EventNotificationService notifyUser " + this.toString());
	subscription.setLastOperationMessage(
		subscription.getDeliveryMethod().send(null, subscription.getUserId(), subject, message, isHtmlFormat));
    }

    @Override
    public void resendMessages() {
	try {
	    EventNotificationService.log.debug("The resend messages thread is running..");
	    List<Event> events = eventDAO.getEventsToResend();
	    for (Event event : events) {
		trigger(event, null, null);
		if ((event.getFailTime() != null) && !event.getSubscriptions().isEmpty() && ((System.currentTimeMillis()
			- event.getFailTime().getTime()) >= IEventNotificationService.RESEND_TIME_LIMIT)) {
		    StringBuilder body = new StringBuilder(messageService.getMessage("mail.resend.abandon.body1"))
			    .append(event.getMessage()).append(messageService.getMessage("mail.resend.abandon.body2"));
		    for (Subscription subscription : event.getSubscriptions()) {
			User user = (User) userManagementService.findById(User.class, subscription.getUserId());
			body.append(user.getLogin()).append('\n');
		    }
		    IEventNotificationService.DELIVERY_METHOD_MAIL.notifyAdmin(
			    messageService.getMessage("mail.resend.abandon.subject"), body.toString(),
			    event.isHtmlFormat());
		    eventDAO.delete(event);
		}
	    }

	} catch (Exception e) {
	    EventNotificationService.log.error("Error while resending messages", e);
	}
    }

    @Override
    public boolean sendMessage(Integer fromUserId, Integer toUserId, AbstractDeliveryMethod deliveryMethod,
	    String subject, String message, boolean isHtmlFormat) throws InvalidParameterException {
	String result = deliveryMethod.send(fromUserId, toUserId, subject, message, isHtmlFormat);
	if (result == null) {
	    return true;
	}

	Event event = new Event(IEventNotificationService.SINGLE_MESSAGE_SCOPE,
		String.valueOf(System.currentTimeMillis()), null, subject, message, isHtmlFormat, new Date());
	subscribe(event, toUserId, deliveryMethod);
	eventDAO.insertOrUpdate(event);
	return false;
    }

    @Override
    public void sendMessage(final Integer fromUserId, final Integer[] toUserIds,
	    final AbstractDeliveryMethod deliveryMethod, final String subject, final String message,
	    final boolean isHtmlFormat) throws InvalidParameterException {
	if (toUserIds == null) {
	    throw new InvalidParameterException("User IDs array must not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method must not be null.");
	}
	// create a new thread to send the messages as it can take some time
	new Thread(() -> {
	    try {
		HibernateSessionManager.openSession();
		// use proxy bean instead of concrete implementation of service
		// otherwise there is no transaction for the new session
		WebApplicationContext ctx = WebApplicationContextUtils
			.getWebApplicationContext(SessionManager.getServletContext());
		IEventNotificationService eventNotificationService = (IEventNotificationService) ctx
			.getBean("eventNotificationService");
		for (Integer id : toUserIds) {
		    eventNotificationService.sendMessage(fromUserId, id, deliveryMethod, subject, message,
			    isHtmlFormat);
		}
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}).start();
    }

    @Override
    public List<Subscription> getNotificationSubscriptions(Long lessonId, Integer userId, boolean pendingOnly,
	    Integer limit, Integer offset) {
	return eventDAO.getLessonEventSubscriptions(lessonId, userId, pendingOnly, limit, offset);
    }

    @Override
    public long getNotificationPendingCount(Long lessonId, Integer userId) {
	return eventDAO.getPendingNotificationCount(lessonId, userId);
    }

    public void setEventDAO(EventDAO eventDAO) {
	this.eventDAO = eventDAO;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
	DeliveryMethodMail.setUserManagementService(userManagementService);
    }

    /**
     * See {@link IEventNotificationService#subscribe(String, String, Long, Long, AbstractDeliveryMethod, Long)
     *
     */
    private void subscribe(Event event, Integer userId, AbstractDeliveryMethod deliveryMethod)
	    throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method can not be null.");
	}
	boolean substriptionFound = false;
	List<Subscription> subscriptionList = new ArrayList<>(event.getSubscriptions());
	for (int index = 0; index < subscriptionList.size(); index++) {
	    Subscription subscription = subscriptionList.get(index);
	    if (subscription.getUserId().equals(userId) && subscription.getDeliveryMethod().equals(deliveryMethod)) {
		substriptionFound = true;
		break;
	    }
	}
	if (!substriptionFound) {
	    event.getSubscriptions().add(new Subscription(userId, deliveryMethod));
	}
	eventDAO.insertOrUpdate(event);
    }

    @Override
    public void subscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method should not be null.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	subscribe(event, userId, deliveryMethod);
    }

    /**
     * See {@link IEventNotificationService#trigger(String, String, Long, String, String)
     */
    private void trigger(Event event, String subject, String message) {
	final String subjectToSend = subject == null ? event.getSubject() : subject;
	final String messageToSend = message == null ? event.getMessage() : message;

	// create a new thread to send the messages as it can take some time
	new Thread(() -> {
	    try {
		HibernateSessionManager.openSession();
		// use proxy bean instead of concrete implementation of service
		// otherwise there is no transaction for the new session
		WebApplicationContext ctx = WebApplicationContextUtils
			.getWebApplicationContext(SessionManager.getServletContext());
		IEventNotificationService eventNotificationService = (IEventNotificationService) ctx
			.getBean("eventNotificationService");
		eventNotificationService.triggerInternal(event, subjectToSend, messageToSend);
	    } finally {
		HibernateSessionManager.closeSession();
	    }
	}).start();
    }

    @Override
    public void triggerInternal(Event eventData, String subject, String message) {
	// fetch the event again so it is associated with current session
	Event event = eventDAO.find(Event.class, eventData.getUid());
	Event eventFailCopy = null;
	Iterator<Subscription> subscriptionIterator = event.getSubscriptions().iterator();
	while (subscriptionIterator.hasNext()) {
	    Subscription subscription = subscriptionIterator.next();
	    notifyUser(subscription, subject, message, event.isHtmlFormat());
	    if (!subscription.getDeliveryMethod().lastOperationFailed(subscription)) {
		if (event.getFailTime() != null) {
		    subscriptionIterator.remove();
		}
	    } else if (event.getFailTime() == null) {
		if (eventFailCopy == null) {
		    eventFailCopy = (Event) event.clone();
		}
		subscribe(eventFailCopy, subscription.getUserId(), subscription.getDeliveryMethod());
	    }
	}
	if (event.getSubscriptions().isEmpty()) {
	    log.debug("Deleting event " + event.getUid() + " " + event.getFailTime());
	    eventDAO.delete(event);
	} else {
	    eventDAO.insertOrUpdate(event);
	}

	/*
	 * if any of the notifications failed,
	 * a copy of the event is created in order to repeat the attempt later
	 */
	if (eventFailCopy != null) {
	    eventFailCopy.setFailTime(new Date());
	    eventFailCopy.setSubject(subject);
	    eventFailCopy.setMessage(message);
	    eventDAO.insertOrUpdate(eventFailCopy);
	}
    }

    @Override
    public void trigger(String scope, String name, Long eventSessionId) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	trigger(event, null, null);
    }

    @Override
    public void trigger(String scope, String name, Long eventSessionId, Object[] parameterValues)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	String message = event.getMessage();
	if ((parameterValues != null) && (parameterValues.length > 0)) {
	    for (int index = 0; index < parameterValues.length; index++) {
		Object value = parameterValues[index];
		String replacement = value == null ? "" : value.toString();
		message = message.replace("{" + index + "}", replacement);
	    }
	}
	trigger(event, null, message);
    }

    @Override
    public void triggerLessonEvent(String scope, String name, Long toolContentId, String subject, String message) {
	Lesson lesson = lessonService.getLessonByToolContentId(toolContentId);
	trigger(scope, name, lesson.getLessonId(), subject, message);
    }

    @Override
    public void trigger(String scope, String name, Long eventSessionId, String title, String message)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	trigger(event, title, message);
    }

    private void triggerForSingleUser(Event event, Integer userId, String subject, String message) {
	for (Subscription subscription : event.getSubscriptions()) {
	    if (subscription.getUserId().equals(userId)) {
		triggerForSingleUser(subscription.getUid(), subject, message);
	    }
	}
    }

    @Override
    public void triggerForSingleUser(Long subscriptionUid, String subject, String message) {
	Subscription subscription = eventDAO.find(Subscription.class, subscriptionUid);
	Event event = subscription.getEvent();
	String subjectToSend = subject == null ? event.getSubject() : subject;
	String messageToSend = message == null ? event.getMessage() : message;
	notifyUser(subscription, subjectToSend, messageToSend, event.isHtmlFormat());
	if (subscription.getDeliveryMethod().lastOperationFailed(subscription)) {
	    Event eventFailCopy = (Event) event.clone();
	    eventFailCopy.setFailTime(new Date());
	    eventFailCopy.setSubject(subjectToSend);
	    eventFailCopy.setMessage(messageToSend);
	    subscribe(eventFailCopy, subscription.getUserId(), subscription.getDeliveryMethod());
	    eventDAO.insertOrUpdate(eventFailCopy);
	}
    }

    @Override
    public void triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	triggerForSingleUser(event, userId, null, null);
    }

    @Override
    public void triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId,
	    Object[] parameterValues) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	String message = event.getMessage();
	if ((parameterValues != null) && (parameterValues.length > 0)) {
	    for (int index = 0; index < parameterValues.length; index++) {
		Object value = parameterValues[index];
		String replacement = value == null ? "" : value.toString();
		message = message.replace("{" + index + "}", replacement);
	    }
	}
	triggerForSingleUser(event, userId, null, message);
    }

    @Override
    public void triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId, String subject,
	    String message) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	triggerForSingleUser(event, userId, subject, message);
    }

    /**
     * See {@link IEventNotificationService#unsubscribe(String, String, Long, Long)
     */
    protected void unsubscribe(Event event, Integer userId) throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}
	Iterator<Subscription> subscriptionIterator = event.getSubscriptions().iterator();
	while (subscriptionIterator.hasNext()) {
	    Subscription subscription = subscriptionIterator.next();
	    if (subscription.getUserId().equals(userId)) {
		subscriptionIterator.remove();
	    }
	}
	if (event.getSubscriptions().isEmpty()) {
	    eventDAO.delete(event);
	} else {
	    eventDAO.insertOrUpdate(event);
	}
    }

    /**
     * See {@link IEventNotificationService#unsubscribe(String, String, Long, Long, AbstractDeliveryMethod)
     */
    protected void unsubscribe(Event event, Integer userId, AbstractDeliveryMethod deliveryMethod)
	    throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method can not be null.");
	}
	Iterator<Subscription> subscriptionIterator = event.getSubscriptions().iterator();
	while (subscriptionIterator.hasNext()) {
	    Subscription subscription = subscriptionIterator.next();
	    if (subscription.getUserId().equals(userId) && subscription.getDeliveryMethod().equals(deliveryMethod)) {
		subscriptionIterator.remove();
	    }
	}
	if (event.getSubscriptions().isEmpty()) {
	    eventDAO.delete(event);
	} else {
	    eventDAO.insertOrUpdate(event);
	}
    }

    @Override
    public void unsubscribe(String scope, String name, Long eventSessionId, Integer userId)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	unsubscribe(event, userId);
    }

    @Override
    public void unsubscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery nethod should not be null.");
	}
	Event event = eventDAO.getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	unsubscribe(event, userId, deliveryMethod);
    }
}