package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.dao.EventDAO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * Provides tools for managing events and notifing users.
 * @author Marcin Cieslak
 *
 */
class EventNotificationService implements IEventNotificationService {
	/**
	 * The only instance of this class, since it is a singleton.
	 */
	private static EventNotificationService instance;

	/**
	 * Scope for events that were created after {@link #sendMessage(Long, AbstractDeliveryMethod, String, String)} failed.
	 */
	protected static final String SINGLE_MESSAGE_SCOPE = "SINGLE_MESSAGE";

	/**
	 * Contains message delivery methods that are available for programmers.
	 */
	protected final static Set<AbstractDeliveryMethod> availableDeliveryMethods = new HashSet<AbstractDeliveryMethod>(2);

	/**
	 * Contains events that are currently in use.
	 * Prevents multiple loading of an event from the database.
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
	 * @param scheduler scheduler injected by Spring
	 */
	public EventNotificationService(Scheduler scheduler) {
		if (EventNotificationService.instance == null) {
			EventNotificationService.instance = this;
			this.scheduler = scheduler;
			EventNotificationService.availableDeliveryMethods.add(IEventNotificationService.DELIVERY_METHOD_MAIL);
			try {
				JobDetail resendMessagesJobDetail = getScheduler().getJobDetail(
						EventNotificationService.RESEND_MESSAGES_JOB_NAME, Scheduler.DEFAULT_GROUP);
				if (resendMessagesJobDetail == null) {
					resendMessagesJobDetail = new JobDetail(EventNotificationService.RESEND_MESSAGES_JOB_NAME,
							Scheduler.DEFAULT_GROUP, ResendMessagesJob.class);
					resendMessagesJobDetail.setDescription("");
					Trigger resendMessageTrigger = new SimpleTrigger(EventNotificationService.RESEND_MESSAGES_TRIGGER_NAME,
							Scheduler.DEFAULT_GROUP, SimpleTrigger.REPEAT_INDEFINITELY, EventNotificationService.RESEND_FREQUENCY);
					getScheduler().scheduleJob(resendMessagesJobDetail, resendMessageTrigger);
				}
				getScheduler().start();
			}
			catch (SchedulerException e) {
				EventNotificationService.log.error(e.getMessage());
			}
		}
	}

	/**
	 * Gets the only existing instance of the class.
	 * @return instance of this class
	 */
	public static EventNotificationService getInstance() {
		return EventNotificationService.instance;
	}

	/**
	 * Allows to plug-in a delivery method.
	 * @param deliveryMethod delivery method to add
	 * @return <code>true</code> if the delivery method with the same ID did not exist and the given delivery method was added successfuly
	 */
	public boolean addDeliveryMethod(AbstractDeliveryMethod deliveryMethod) {
		return EventNotificationService.availableDeliveryMethods.add(deliveryMethod);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#createEvent(java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
	 */
	public boolean createEvent(String scope, String name, Long eventSessionId, String defaultSubject, String defaultMessage)
			throws InvalidParameterException {
		Event event = getEvent(scope, name, eventSessionId);
		if (event != null) {
			saveEvent(event);
			return false;
		}
		event = new Event(scope, name, eventSessionId, defaultSubject, defaultMessage);
		event.referenceCounter++;
		saveEvent(event);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#deleteEvent(java.lang.String, java.lang.String, java.lang.Long, java.lang.String)
	 */
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
		}
		else {
			event.deleted = true;
			saveEvent(event);
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#eventExists(java.lang.String, java.lang.String, java.lang.Long)
	 */
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
		}
		else {
			saveEvent(event);
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#getAvailableDeliveryMethods()
	 */
	public Set<AbstractDeliveryMethod> getAvailableDeliveryMethods() {
		return EventNotificationService.availableDeliveryMethods;
	}

	public EventDAO getEventDAO() {
		return eventDAO;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#isSubscribed(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public boolean isSubscribed(String scope, String name, Long eventSessionId, Long userId) throws InvalidParameterException {
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
				if (subscription.getUserId().equals(userId)) {
					isSubscribed = true;
					break;
				}
			}
			saveEvent(event);
		}
		return isSubscribed;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#sendMessage(java.lang.Long, org.lamsfoundation.lams.events.AbstractDeliveryMethod, java.lang.String, java.lang.String)
	 */
	public boolean sendMessage(Long userId, AbstractDeliveryMethod deliveryMethod, String subject, String message)
			throws InvalidParameterException {
		Event eventFailCopy = new Event(EventNotificationService.SINGLE_MESSAGE_SCOPE,
				String.valueOf(System.currentTimeMillis()), null, subject, message);
		String result = deliveryMethod.send(userId, subject, message);
		if (result != null) {
		    	EventNotificationService.log.warn(messageService.getMessage("mail.error.occurred.while.sending.message",
		    		new Object[] { result }));		    
			eventFailCopy.subscribe(userId, deliveryMethod, null);
		}

		if (!eventFailCopy.subscriptions.isEmpty()) {
			eventFailCopy.setFailTime(new Date());
			eventFailCopy.referenceCounter++;
			saveEvent(eventFailCopy);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#sendMessage(java.lang.Long[], org.lamsfoundation.lams.events.AbstractDeliveryMethod, java.lang.String, java.lang.String)
	 */
	public boolean sendMessage(final Long[] userId, final AbstractDeliveryMethod deliveryMethod, final String subject,
			final String message) throws InvalidParameterException {
		if (userId == null) {
			throw new InvalidParameterException("User IDs array should not be null.");
		}
		if (deliveryMethod == null) {
			throw new InvalidParameterException("Delivery method should not be null.");
		}
		if (userId.length > 0) {
			if (userId.length == 1) {
				return sendMessage(userId[0], deliveryMethod, subject, message);
			}
			else {
				final Event event = new Event(EventNotificationService.SINGLE_MESSAGE_SCOPE, String.valueOf(System
						.currentTimeMillis()), null, subject, message);
				event.referenceCounter++;
				event.notificationThread = new Thread(new Runnable() {
					public void run() {
						for (Long id : userId) {
							String result = deliveryMethod.send(id, subject, message);
							if (result != null) {
							    	EventNotificationService.log.warn(messageService.getMessage(
							    		"mail.error.occurred.while.sending.message",
							    		new Object[] { result }));							    
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

	public void setEventDAO(EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	public void setUserManagementService(IUserManagementService userManagementService) {
		this.userManagementService = userManagementService;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#subscribe(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, org.lamsfoundation.lams.events.AbstractDeliveryMethod, java.lang.Long)
	 */
	public boolean subscribe(String scope, String name, Long eventSessionId, Long userId, AbstractDeliveryMethod deliveryMethod,
			Long periodicity) throws InvalidParameterException {
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
		}
		catch (Exception e) {
			EventNotificationService.log.error(e.getMessage());
		}
		saveEvent(event);
		return newSubscription;

	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#trigger(java.lang.String, java.lang.String, java.lang.Long)
	 */
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
		}
		catch (Exception e) {
			EventNotificationService.log.error(e.getMessage());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#trigger(java.lang.String, java.lang.String, java.lang.Long, java.lang.Object[] parameterValues)
	 */
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
		if (parameterValues != null && parameterValues.length > 0) {
			for (int index = 0; index < parameterValues.length; index++) {
				Object value = parameterValues[index];
				String replacement = value == null ? "" : value.toString();
				body = body.replace("{" + index + "}", replacement);
			}
		}
		try {
			event.trigger(event.getDefaultSubject(), body);
		}
		catch (Exception e) {
			EventNotificationService.log.error(e.getMessage());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#trigger(java.lang.String, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
	 */
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
		}
		catch (Exception e) {
			EventNotificationService.log.error(e.getMessage());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#triggerForSingleUser(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Long userId)
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
			notificationSuccessful = event.triggerForSingleUser(userId, event.getDefaultSubject(), event.getDefaultMessage());
		}
		catch (Exception e) {
			EventNotificationService.log.error(e.getMessage());
		}
		saveEvent(event);
		return notificationSuccessful;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#triggerForSingleUser(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Long userId, Object[] parameterValues)
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
		String body = event.getDefaultMessage();
		if (parameterValues != null && parameterValues.length > 0) {
			for (int index = 0; index < parameterValues.length; index++) {
				Object value = parameterValues[index];
				String replacement = value == null ? "" : value.toString();
				body = body.replace("{" + index + "}", replacement);
			}
		}
		boolean notificationSuccessful = false;
		try {
			notificationSuccessful = event.triggerForSingleUser(userId, event.getDefaultSubject(), body);
		}
		catch (Exception e) {
			EventNotificationService.log.error(e.getMessage());
		}
		saveEvent(event);
		return notificationSuccessful;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#triggerForSingleUser(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, java.lang.String, java.lang.String)
	 */
	public boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Long userId, String subject,
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
		}
		catch (Exception e) {

			EventNotificationService.log.error(e.getMessage());
		}
		finally {
			saveEvent(event);
		}
		return notificationSuccessful;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#unsubscribe(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public boolean unsubscribe(String scope, String name, Long eventSessionId, Long userId) throws InvalidParameterException {
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
		}
		catch (Exception e) {

			EventNotificationService.log.error(e.getMessage());
		}
		finally {
			saveEvent(event);
		}
		return subscriptionFound;
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.events.IEventNotificationService#unsubscribe(java.lang.String, java.lang.String, java.lang.Long, java.lang.Long, org.lamsfoundation.lams.events.AbstractDeliveryMethod)
	 */
	public boolean unsubscribe(String scope, String name, Long eventSessionId, Long userId, AbstractDeliveryMethod deliveryMethod)
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
		}
		catch (Exception e) {

			EventNotificationService.log.error(e.getMessage());
		}
		finally {
			saveEvent(event);
		}
		return subscriptionFound;
	}

	/**
	 * Gets the event, either from the database or the event pool.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
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

	protected IUserManagementService getUserManagementService() {
		return userManagementService;
	}

	/**
	 * Saves the event into the database
	 * @param event event to be saved
	 */
	protected void saveEvent(Event event) {
		event.referenceCounter--;
		if (event.referenceCounter <= 0) {
			EventNotificationService.eventPool.remove(event);
			if (event.deleted) {
				getEventDAO().deleteEvent(event);
			}
			else {
				getEventDAO().saveEvent(event);
			}
		}
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
}