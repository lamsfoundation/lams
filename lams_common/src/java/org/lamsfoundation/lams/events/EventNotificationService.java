package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
    }

    private EventDAO eventDAO = null;
    private MessageService messageService = null;
    private ILessonService lessonService = null;
    private ILamsToolService toolService = null;
    private IUserManagementService userManagementService = null;

    @Override
    public void createEvent(String scope, String name, Long eventSessionId, String defaultSubject,
	    String defaultMessage, boolean isHtmlFormat) throws InvalidParameterException {
	if (!eventExists(scope, name, eventSessionId)) {
	    Event event = new Event(scope, name, eventSessionId, defaultSubject, defaultMessage, isHtmlFormat);
	    eventDAO.saveEvent(event);
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
		if (subscription.getUserId().equals(userId)) {
		    return true;
		}
	    }
	}
	return false;
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
	subscription.setLastOperationTime(new Date());
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
		if (event.getFailTime() != null) {
		    if (!event.getSubscriptions().isEmpty() && ((System.currentTimeMillis()
			    - event.getFailTime().getTime()) >= IEventNotificationService.RESEND_TIME_LIMIT)) {
			StringBuilder body = new StringBuilder(messageService.getMessage("mail.resend.abandon.body1"))
				.append(event.getMessage())
				.append(messageService.getMessage("mail.resend.abandon.body2"));
			for (Subscription subscription : event.getSubscriptions()) {
			    User user = (User) userManagementService.findById(User.class, subscription.getUserId());
			    body.append(user.getLogin()).append('\n');
			}
			IEventNotificationService.DELIVERY_METHOD_MAIL.notifyAdmin(
				messageService.getMessage("mail.resend.abandon.subject"), body.toString(),
				event.isHtmlFormat());
			eventDAO.deleteEvent(event);
		    }
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

	EventNotificationService.log.error("Error occured while sending message: " + result);
	Event event = new Event(IEventNotificationService.SINGLE_MESSAGE_SCOPE,
		String.valueOf(System.currentTimeMillis()), null, subject, message, isHtmlFormat);
	subscribe(event, toUserId, deliveryMethod, null);
	event.setFailTime(new Date());
	eventDAO.saveEvent(event);
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
	    Event event = null;
	    for (Integer id : toUserIds) {
		String result = deliveryMethod.send(fromUserId, id, subject, message, isHtmlFormat);
		if (result != null) {
		    EventNotificationService.log.warn("Error occured while sending message: " + result);
		    event = new Event(IEventNotificationService.SINGLE_MESSAGE_SCOPE,
			    String.valueOf(System.currentTimeMillis()), null, subject, message, isHtmlFormat);
		    subscribe(event, id, deliveryMethod, null);
		}
	    }
	    if (event != null) {
		event.setFailTime(new Date());
		eventDAO.saveEvent(event);
	    }
	}).start();
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
	IEventNotificationService.DELIVERY_METHOD_MAIL.setUserManagementService(userManagementService);
    }

    /**
     * See {@link IEventNotificationService#subscribe(String, String, Long, Long, AbstractDeliveryMethod, Long)
     * 
     */
    protected void subscribe(Event event, Integer userId, AbstractDeliveryMethod deliveryMethod, Long periodicity)
	    throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method can not be null.");
	}
	boolean substriptionFound = false;
	List<Subscription> subscriptionList = new ArrayList<Subscription>(event.getSubscriptions());
	for (int index = 0; index < subscriptionList.size(); index++) {
	    Subscription subscription = subscriptionList.get(index);
	    if (subscription.getUserId().equals(userId)
		    && subscription.getDeliveryMethod().equals(deliveryMethod.getId())) {
		substriptionFound = true;
		if (!subscription.getPeriodicity().equals(periodicity)) {
		    subscription.setPeriodicity(periodicity);
		}
		break;
	    }
	}
	if (!substriptionFound) {
	    event.getSubscriptions().add(new Subscription(userId, deliveryMethod, periodicity));
	}
	eventDAO.saveEvent(event);
    }

    @Override
    public void subscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod, Long periodicity) throws InvalidParameterException {
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
	subscribe(event, userId, deliveryMethod, periodicity);
    }

    /**
     * See {@link IEventNotificationService#trigger(String, String, Long, String, String)
     */
    private void trigger(Event event, String subject, String message) {
	final String subjectToSend = subject == null ? event.getSubject() : subject;
	final String messageToSend = message == null ? event.getMessage() : message;

	// create a new thread to send the messages as it can take some time
	new Thread(() -> {
	    Event eventFailCopy = null;
	    Iterator<Subscription> subscriptionIterator = event.getSubscriptions().iterator();
	    while (subscriptionIterator.hasNext()) {
		Subscription subscription = subscriptionIterator.next();
		if ((event.getFailTime() != null) || subscription.isEligibleForNotification()) {
		    notifyUser(subscription, subjectToSend, messageToSend, event.isHtmlFormat());
		    if (subscription.getLastOperationSuccessful()) {
			if (event.getFailTime() != null) {
			    subscriptionIterator.remove();
			}
		    } else if (event.getFailTime() == null) {
			if (eventFailCopy == null) {
			    eventFailCopy = (Event) event.clone();
			}
			subscribe(eventFailCopy, subscription.getUserId(), subscription.getDeliveryMethod(),
				subscription.getPeriodicity());
		    }
		}
	    }
	    if (event.getSubscriptions().isEmpty()) {
		eventDAO.deleteEvent(event);
	    } else {
		eventDAO.saveEvent(event);
	    }

	    /*
	     * if any of the notifications failed,
	     * a copy of the event is created in order to repeat the attempt later 
	     */
	    if (eventFailCopy != null) {
		eventFailCopy.setFailTime(new Date());
		eventFailCopy.setSubject(subjectToSend);
		eventFailCopy.setMessage(messageToSend);
		eventDAO.saveEvent(eventFailCopy);
	    }
	}).start();
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

    /**
     * See {@link IEventNotificationService#triggerForSingleUser(String, String, Long, Long)}
     */
    protected boolean triggerForSingleUser(Event event, Integer userId, String subject, String message)
	    throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}

	for (Subscription subscription : event.getSubscriptions()) {
	    if (subscription.getUserId().equals(userId) && subscription.isEligibleForNotification()) {
		notifyUser(subscription, subject, message, event.isHtmlFormat());
		return subscription.getLastOperationSuccessful();
	    }
	}
	return false;
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
	    eventDAO.deleteEvent(event);
	} else {
	    eventDAO.saveEvent(event);
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
	    eventDAO.deleteEvent(event);
	} else {
	    eventDAO.saveEvent(event);
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