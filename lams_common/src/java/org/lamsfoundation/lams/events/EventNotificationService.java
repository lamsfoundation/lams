package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
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
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * Provides tools for managing events and notifing users.
 *
 * @author Marcin Cieslak
 *
 */
class EventNotificationService implements IEventNotificationService {
    /**
     * The only instance of this class, since it is a singleton.
     */
    private static EventNotificationService instance;

    /**
     * Contains message delivery methods that are available for programmers.
     */
    protected final static Set<AbstractDeliveryMethod> availableDeliveryMethods = new HashSet<AbstractDeliveryMethod>(
	    2);

    /**
     * Contains events that are currently in use. Prevents multiple loading of an event from the database.
     */
    protected final static Set<Event> eventPool = new HashSet<Event>();

    protected static final Logger log = Logger.getLogger(EventNotificationService.class);

    /**
     * How often the attempts to resend messages should be taken (in miliseconds). Currently - every hour.
     */
    private static final long RESEND_FREQUENCY = 60 * 60 * 1000;

    /**
     * Interface to contact the database.
     */
    protected EventDAO eventDAO;

    /**
     * Interface to receive contact details of the users.
     */
    protected IUserManagementService userManagementService;

    protected MessageService messageService;

    protected ILessonService lessonService;

    protected ILamsToolService toolService;

    /**
     * Quartz scheduler used for resending messages.
     */
    private Scheduler scheduler;
    /**
     * Name of the Quartz job that resends messages.
     */
    private final static String RESEND_MESSAGES_JOB_NAME = "Resend Messages Job";
    /**
     * Name of the Quartz trigger that resends messages.
     */
    private final static String RESEND_MESSAGES_TRIGGER_NAME = "Resend Messages Job trigger";

    /**
     * Default constructor. Should be called only once, since this class in a singleton.
     *
     * @param scheduler
     *            scheduler injected by Spring
     */
    public EventNotificationService(Scheduler scheduler) {
	if (EventNotificationService.instance == null) {
	    EventNotificationService.instance = this;
	    this.scheduler = scheduler;
	    EventNotificationService.availableDeliveryMethods.add(IEventNotificationService.DELIVERY_METHOD_MAIL);
	    try {
		JobDetail resendMessagesJobDetail = getScheduler()
			.getJobDetail(EventNotificationService.RESEND_MESSAGES_JOB_NAME, Scheduler.DEFAULT_GROUP);
		if (resendMessagesJobDetail == null) {
		    resendMessagesJobDetail = new JobDetail(EventNotificationService.RESEND_MESSAGES_JOB_NAME,
			    Scheduler.DEFAULT_GROUP, ResendMessagesJob.class);
		    resendMessagesJobDetail.setDescription("");
		    Trigger resendMessageTrigger = new SimpleTrigger(
			    EventNotificationService.RESEND_MESSAGES_TRIGGER_NAME, Scheduler.DEFAULT_GROUP,
			    SimpleTrigger.REPEAT_INDEFINITELY, EventNotificationService.RESEND_FREQUENCY);
		    getScheduler().scheduleJob(resendMessagesJobDetail, resendMessageTrigger);
		}
		getScheduler().start();
	    } catch (SchedulerException e) {
		EventNotificationService.log.error(e.getMessage());
	    }
	}
    }

    /**
     * Gets the only existing instance of the class.
     *
     * @return instance of this class
     */
    public static EventNotificationService getInstance() {
	return EventNotificationService.instance;
    }

    /**
     * Allows to plug-in a delivery method.
     *
     * @param deliveryMethod
     *            delivery method to add
     * @return <code>true</code> if the delivery method with the same ID did not exist and the given delivery method was
     *         added successfuly
     */
    public boolean addDeliveryMethod(AbstractDeliveryMethod deliveryMethod) {
	return EventNotificationService.availableDeliveryMethods.add(deliveryMethod);
    }

    @Override
    public boolean createEvent(String scope, String name, Long eventSessionId, String defaultSubject,
	    String defaultMessage, boolean isHtmlFormat) throws InvalidParameterException {
	Event event = getEvent(scope, name, eventSessionId);
	if (event != null) {
	    saveEvent(event);
	    return false;
	}
	event = new Event(scope, name, eventSessionId, defaultSubject, defaultMessage, isHtmlFormat);
	event.referenceCounter++;
	saveEvent(event);
	return true;
    }

    @Override
    public boolean deleteEvent(String scope, String name, Long eventSessionId) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    return false;
	} else {
	    event.deleted = true;
	    saveEvent(event);
	    return true;
	}
    }

    @Override
    public boolean eventExists(String scope, String name, Long eventSessionId) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    return false;
	} else {
	    saveEvent(event);
	    return true;
	}
    }

    @Override
    public Set<AbstractDeliveryMethod> getAvailableDeliveryMethods() {
	return EventNotificationService.availableDeliveryMethods;
    }

    @Override
    public boolean isSubscribed(String scope, String name, Long eventSessionId, Long userId)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	boolean isSubscribed = false;
	if (event != null) {
	    for (Subscription subscription : event.getSubscriptions()) {
		if (subscription.getUserId().equals(Integer.valueOf(userId.intValue()))) {
		    isSubscribed = true;
		    break;
		}
	    }
	    saveEvent(event);
	}
	return isSubscribed;
    }

    @Override
    public boolean sendMessage(Integer fromUserId, Integer toUserId, AbstractDeliveryMethod deliveryMethod,
	    String subject, String message, boolean isHtmlFormat) throws InvalidParameterException {
	Event eventFailCopy = new Event(IEventNotificationService.SINGLE_MESSAGE_SCOPE,
		String.valueOf(System.currentTimeMillis()), null, subject, message, isHtmlFormat);
	String result = deliveryMethod.send(fromUserId, toUserId, subject, message, isHtmlFormat);
	if (result != null) {
	    EventNotificationService.log.warn("Error occured while sending message: " + result);
	    eventFailCopy.subscribe(toUserId, deliveryMethod, null);
	}

	if (!eventFailCopy.subscriptions.isEmpty()) {
	    eventFailCopy.setFailTime(new Date());
	    eventFailCopy.referenceCounter++;
	    saveEvent(eventFailCopy);
	    return false;
	}
	return true;
    }

    @Override
    public boolean sendMessage(final Integer fromUserId, final Integer[] toUserIds,
	    final AbstractDeliveryMethod deliveryMethod, final String subject, final String message,
	    final boolean isHtmlFormat) throws InvalidParameterException {
	if (toUserIds == null) {
	    throw new InvalidParameterException("User IDs array must not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method must not be null.");
	}
	if (toUserIds.length > 0) {
	    if (toUserIds.length == 1) {
		return sendMessage(fromUserId, toUserIds[0], deliveryMethod, subject, message, isHtmlFormat);
	    } else {
		final Event event = new Event(IEventNotificationService.SINGLE_MESSAGE_SCOPE,
			String.valueOf(System.currentTimeMillis()), null, subject, message, isHtmlFormat);
		event.referenceCounter++;
		event.notificationThread = new Thread(new Runnable() {
		    @Override
		    public void run() {
			for (Integer id : toUserIds) {
			    String result = deliveryMethod.send(fromUserId, id, subject, message, isHtmlFormat);
			    if (result != null) {
				EventNotificationService.log.warn("Error occured while sending message: " + result);
				event.subscribe(id, deliveryMethod, null);
			    }
			}
			if (!event.subscriptions.isEmpty()) {
			    event.setFailTime(new Date());
			    event.setTriggered(true);
			    saveEvent(event);
			}
		    }
		}, "LAMS_single_message_send_thread");
		event.notificationThread.start();

	    }
	}
	return true;
    }

    @Override
    public boolean notifyLessonMonitors(Long sessionId, String message, boolean isHtmlFormat) {
	final String NEW_LINE_CHARACTER = "\r\n";

	List<User> monitoringUsers = lessonService.getMonitorsByToolSessionId(sessionId);
	if (monitoringUsers == null || monitoringUsers.isEmpty()) {
	    return true;
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
	String emailSubject = toolName + " " + messageService.getMessage("email.notifications.tool") + ": "
		+ activityTitle + " " + messageService.getMessage("email.notifications.activity") + " - " + lessonName
		+ " " + messageService.getMessage("email.notifications.lesson");

	String courseName = lesson.getOrganisation().getName();
	String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL).trim();
	String emailBody = messageService.getMessage("email.notifications.course") + ": " + courseName
		+ NEW_LINE_CHARACTER + messageService.getMessage("email.notifications.lesson.caption") + ": "
		+ lessonName + NEW_LINE_CHARACTER + NEW_LINE_CHARACTER + message + NEW_LINE_CHARACTER
		+ NEW_LINE_CHARACTER + serverUrl;

	return sendMessage(null, monitoringUsersIds, IEventNotificationService.DELIVERY_METHOD_MAIL, emailSubject,
		emailBody, isHtmlFormat);
    }

    @Override
    public boolean subscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod, Long periodicity) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method should not be null.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	boolean newSubscription = true;
	try {
	    newSubscription = event.subscribe(userId, deliveryMethod, periodicity);
	} catch (Exception e) {
	    EventNotificationService.log.error(e);
	}
	saveEvent(event);
	return newSubscription;

    }

    @Override
    public boolean trigger(String scope, String name, Long eventSessionId) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	try {
	    event.trigger(event.getDefaultSubject(), event.getDefaultMessage());
	} catch (Exception e) {
	    EventNotificationService.log.error(e.getMessage());
	}
	return true;
    }

    @Override
    public boolean trigger(String scope, String name, Long eventSessionId, Object[] parameterValues)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	String body = event.getDefaultMessage();
	if ((parameterValues != null) && (parameterValues.length > 0)) {
	    for (int index = 0; index < parameterValues.length; index++) {
		Object value = parameterValues[index];
		String replacement = value == null ? "" : value.toString();
		body = body.replace("{" + index + "}", replacement);
	    }
	}
	try {
	    event.trigger(event.getDefaultSubject(), body);
	} catch (Exception e) {
	    EventNotificationService.log.error(e.getMessage());
	}
	return true;
    }

    @Override
    public boolean trigger(String scope, String name, Long eventSessionId, String title, String message)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	try {
	    event.trigger(title, message);
	} catch (Exception e) {
	    EventNotificationService.log.error(e.getMessage());
	}
	return true;
    }

    @Override
    public boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	boolean notificationSuccessful = false;
	try {
	    notificationSuccessful = event.triggerForSingleUser(userId, event.getDefaultSubject(),
		    event.getDefaultMessage());
	} catch (Exception e) {
	    EventNotificationService.log.error(e.getMessage());
	}
	saveEvent(event);
	return notificationSuccessful;
    }

    @Override
    public boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId,
	    Object[] parameterValues) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	String body = event.getDefaultMessage();
	if ((parameterValues != null) && (parameterValues.length > 0)) {
	    for (int index = 0; index < parameterValues.length; index++) {
		Object value = parameterValues[index];
		String replacement = value == null ? "" : value.toString();
		body = body.replace("{" + index + "}", replacement);
	    }
	}
	boolean notificationSuccessful = false;
	try {
	    notificationSuccessful = event.triggerForSingleUser(userId, event.getDefaultSubject(), body);
	} catch (Exception e) {
	    EventNotificationService.log.error(e.getMessage());
	}
	saveEvent(event);
	return notificationSuccessful;
    }

    @Override
    public boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId, String subject,
	    String message) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	boolean notificationSuccessful = false;
	try {
	    notificationSuccessful = event.triggerForSingleUser(userId, subject, message);
	} catch (Exception e) {

	    EventNotificationService.log.error(e.getMessage());
	} finally {
	    saveEvent(event);
	}
	return notificationSuccessful;
    }

    @Override
    public boolean unsubscribe(String scope, String name, Long eventSessionId, Integer userId)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	boolean subscriptionFound = false;
	try {
	    subscriptionFound = event.unsubscribe(userId);
	} catch (Exception e) {

	    EventNotificationService.log.error(e.getMessage());
	} finally {
	    saveEvent(event);
	}
	return subscriptionFound;
    }

    @Override
    public boolean unsubscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	if (userId == null) {
	    throw new InvalidParameterException("User ID should not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery nethod should not be null.");
	}
	Event event = getEvent(scope, name, eventSessionId);
	if (event == null) {
	    throw new InvalidParameterException("An event with the given parameters does not exist.");
	}
	boolean subscriptionFound = false;
	try {
	    subscriptionFound = event.unsubscribe(userId, deliveryMethod);
	} catch (Exception e) {

	    EventNotificationService.log.error(e.getMessage());
	} finally {
	    saveEvent(event);
	}
	return subscriptionFound;
    }

    /**
     * Gets the event, either from the database or the event pool.
     *
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @return event if it exists and it is not deleted; <code>null</code> if it does not exist
     */
    protected Event getEvent(String scope, String name, Long eventSessionId) {
	String fullSignature = Event.createFullSignature(scope, name, eventSessionId);

	for (Event event : EventNotificationService.eventPool) {
	    if (event.equals(fullSignature)) {
		if (event.deleted) {
		    return null;
		}
		event.referenceCounter++;
		return event;
	    }
	}

	Event result = getEventDAO().getEvent(scope, name, eventSessionId);
	if (result == null) {
	    return null;
	}
	result.referenceCounter++;
	EventNotificationService.eventPool.add(result);
	return result;
    }

    /**
     * Saves the event into the database
     *
     * @param event
     *            event to be saved
     */
    protected void saveEvent(Event event) {
	event.referenceCounter--;
	if (event.referenceCounter <= 0) {
	    EventNotificationService.eventPool.remove(event);
	    if (event.deleted) {
		getEventDAO().deleteEvent(event);
	    } else {
		getEventDAO().saveEvent(event);
	    }
	}
    }

    public EventDAO getEventDAO() {
	return eventDAO;
    }

    public void setEventDAO(EventDAO eventDAO) {
	this.eventDAO = eventDAO;
    }

    protected IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    private Scheduler getScheduler() {
	return scheduler;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    protected MessageService getMessageService() {
	return messageService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    protected ILessonService getLessonService() {
	return lessonService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    protected ILamsToolService getToolService() {
	return toolService;
    }
}
