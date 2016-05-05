package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * A event that users can subscribe to and at some point can be triggered, notifing the users.
 *
 * @hibernate.class table="lams_events"
 * @author Marcin Cieslak
 *
 */
public class Event {

    // --------- persistent fields -------------
    /**
     * Unique ID for Hibernate needs.
     */
    private Long uid;

    /**
     * Name of the event.
     */
    protected String name;

    /**
     * Scope of the event. For events that are common for the whole LAMS environment,
     * {@link EventNotificationService#CORE_EVENTS_SCOPE} should be used. For tools their signature should be used.
     */
    protected String scope;

    /**
     * Identifier for a session in which the event is valid. or events that are common for the whole LAMS environment
     * <code>null</code> can be used. For tools their content ID should be used.
     */
    protected Long eventSessionId;

    /**
     * Was the event triggered (did it happen).
     */
    protected Boolean triggered = false;

    /**
     * Set of users that are subscribed to this event, along with the required data.
     */
    protected Set<Subscription> subscriptions = new HashSet<Subscription>();

    /**
     * Default message that will be send when the event is triggered and no other message is or was provided.
     */
    protected String defaultMessage;

    /**
     * Default subject of the message that will be send when the event is triggered and no other message is or was
     * provided.
     */
    protected String defaultSubject;

    /**
     * Message that will be send when the event is triggered.
     */
    protected String message;

    /**
     * Subject of the message that will be send when the event is triggered.
     */
    protected String subject;

    /**
     * Boolean indicates whether the message should be sent as text/html content or regular text/plain one
     */
    protected boolean htmlFormat;

    /**
     * If sending notifications fails, this property holds the time of this failure.
     */
    protected Date failTime;

    // -------------- non-persistent fields --------------
    /**
     * Label that identifies the event.
     */
    protected String fullSignature;

    /**
     * Used for event instance management in EventNotificationService.
     */
    protected int referenceCounter;

    /**
     * Thread that notifies users of the event. Since sending messages to multiple users can take some time, another
     * thread is responsible for that, so the main one does not get blocked.
     */
    protected Thread notificationThread;

    /**
     * Should the event be deleted from the database.
     */
    protected boolean deleted;

    public Event() {

    }

    /**
     * Standard constructor used by EventNotificationService.
     *
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param defaultSubject
     *            subject of the message to send
     * @param defaultMessage
     *            body of the message to send
     * @throws InvalidParameterException
     *             if scope is <code>null</code> or name is blank
     */
    public Event(String scope, String name, Long sessionId, String defaultSubject, String defaultMessage,
	    boolean isHtmlContentType) throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Event scope can not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Event name can not be blank.");
	}
	this.scope = scope;
	this.name = name;
	eventSessionId = sessionId;
	this.defaultSubject = defaultSubject;
	this.defaultMessage = defaultMessage;
	this.htmlFormat = isHtmlContentType;
	fullSignature = Event.createFullSignature(scope, name, sessionId);
    }

    /**
     * Build a string that identifies the event.
     *
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @return event signature of the event
     * @throws InvalidParameterException
     *             if scope is <code>null</code> or name is blank
     */
    protected static String createFullSignature(String scope, String name, Long sessionId)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Scope should not be null.");
	}
	if (StringUtils.isEmpty(name)) {
	    throw new InvalidParameterException("Name should not be blank.");
	}
	return scope + '_' + name + (sessionId == null ? "" : "_" + sessionId);
    }

    @Override
    public Object clone() {
	Event clone = new Event(scope, name, eventSessionId, defaultSubject, defaultMessage, htmlFormat);
	for (Subscription subscription : getSubscriptions()) {
	    clone.getSubscriptions().add((Subscription) subscription.clone());
	}
	return clone;
    }

    /**
     * Full signature is compared.
     */
    @Override
    public boolean equals(Object o) {
	return ((o instanceof Event) && ((Event) o).getFullSignature().equalsIgnoreCase(getFullSignature()))
		|| ((o instanceof CharSequence) && ((CharSequence) o).toString().equalsIgnoreCase(getFullSignature()));
    }

    @Override
    public int hashCode() {
	return getFullSignature().hashCode();
    }

    /**
     * @hibernate.property column="default_message"
     * @return
     */
    protected String getDefaultMessage() {
	return defaultMessage;
    }

    /**
     * @hibernate.property column="default_subject"
     * @return
     */
    protected String getDefaultSubject() {
	return defaultSubject;
    }

    protected String getFullSignature() {
	if (fullSignature == null) {
	    fullSignature = Event.createFullSignature(getScope(), getName(), getEventSessionId());
	}
	return fullSignature;
    }

    /**
     * @hibernate.property column="name" length="128"
     * @return
     */
    protected String getName() {
	return name;
    }

    /**
     * @hibernate.property column="scope" length="128"
     * @return
     */
    protected String getScope() {
	return scope;
    }

    /**
     * @hibernate.property column="event_session_id"
     * @return
     */
    protected Long getEventSessionId() {
	return eventSessionId;
    }

    /**
     *
     * @hibernate.set cascade="all-delete-orphan" order-by="last_operation_time desc" outer-join="true"
     * @hibernate.collection-key column="event_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.events.Subscription"
     *
     * @return
     */
    protected Set<Subscription> getSubscriptions() {
	return subscriptions;
    }

    /**
     * @hibernate.id column="uid" generator-class="native"
     */
    protected Long getUid() {
	return uid;
    }

    /**
     * @hibernate.property column="triggered"
     * @return
     */
    protected boolean isTriggered() {
	return triggered;
    }

    /**
     * See {@link IEventNotificationService#triggerForSingleUser(String, String, Long, Long)}
     */
    protected boolean triggerForSingleUser(Integer userId, String subject, String message)
	    throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}

	List<Subscription> subscriptionList = new ArrayList<Subscription>(getSubscriptions());
	for (int index = 0; index < subscriptionList.size(); index++) {
	    Subscription subscription = subscriptionList.get(index);
	    if (subscription.getUserId().equals(userId) && subscription.isEligibleForNotification()) {
		subscription.notifyUser(subject, message, this.isHtmlFormat());
		return subscription.getLastOperationSuccessful();
	    }
	}
	return false;
    }

    protected void setDefaultMessage(String defaultMessage) {
	this.defaultMessage = defaultMessage;
    }

    protected void setDefaultSubject(String defaultSubject) {
	this.defaultSubject = defaultSubject;
    }

    protected void setName(String name) {
	this.name = name;
    }

    protected void setScope(String scope) {
	this.scope = scope;
    }

    protected void setEventSessionId(Long sessionId) {
	eventSessionId = sessionId;
    }

    protected void setSubscriptions(Set<Subscription> subscriptions) {
	this.subscriptions = subscriptions;
    }

    protected void setTriggered(boolean triggered) {
	this.triggered = triggered;
    }

    protected void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * See {@link IEventNotificationService#subscribe(String, String, Long, Long, AbstractDeliveryMethod, Long)
     *
     */
    protected boolean subscribe(Integer userId, AbstractDeliveryMethod deliveryMethod, Long periodicity)
	    throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method can not be null.");
	}
	boolean substriptionFound = false;
	Subscription toSubscribe = new Subscription(userId, deliveryMethod, periodicity);
	List<Subscription> subscriptionList = new ArrayList<Subscription>(getSubscriptions());
	for (int index = 0; index < subscriptionList.size(); index++) {
	    Subscription subscription = subscriptionList.get(index);
	    if (subscription.equals(toSubscribe)) {
		substriptionFound = true;
		if (!subscription.getPeriodicity().equals(periodicity)) {
		    subscription.setPeriodicity(periodicity);
		}
	    }
	}
	if (!substriptionFound) {
	    getSubscriptions().add(new Subscription(userId, deliveryMethod, periodicity));
	    return true;
	}
	return false;

    }

    /**
     * See {@link IEventNotificationService#trigger(String, String, Long, String, String)
     */
    protected void trigger(String subject, String message) {
	if ((notificationThread == null) || !notificationThread.isAlive()) {
	    triggered = true;

	    if ((subject != null) && subject.equals(getDefaultSubject())) {
		setSubject(null);
	    } else {
		setSubject(subject);
	    }

	    if ((message != null) && message.equals(getDefaultMessage())) {
		setMessage(null);
	    } else {
		setMessage(message);
	    }

	    final String subjectToSend = getSubject() == null ? getDefaultSubject() : getSubject();
	    final String messageToSend = getMessage() == null ? getDefaultMessage() : getMessage();
	    final boolean isHtmlContentType = isHtmlFormat();
	    final Event finalRef = this;
	    notificationThread = new Thread(new Runnable() {
		@Override
		public void run() {
		    List<Subscription> subscriptionList = new ArrayList<Subscription>(getSubscriptions());
		    Event eventFailCopy = null;
		    for (int index = 0; index < subscriptionList.size(); index++) {
			Subscription subscription = subscriptionList.get(index);
			if ((getFailTime() != null) || subscription.isEligibleForNotification()) {
			    subscription.notifyUser(subjectToSend, messageToSend, isHtmlContentType);
			    if (subscription.getLastOperationSuccessful()) {
				if (getFailTime() != null) {
				    getSubscriptions().remove(subscription);
				}
			    } else if (getFailTime() == null) {
				if (eventFailCopy == null) {
				    eventFailCopy = (Event) finalRef.clone();
				    eventFailCopy.referenceCounter++;
				    eventFailCopy.getSubscriptions().clear();
				}
				eventFailCopy.subscribe(subscription.getUserId(), subscription.getDeliveryMethod(),
					subscription.getPeriodicity());
			    }
			}
		    }
		    EventNotificationService.getInstance().saveEvent(finalRef);

		    /*
		     * if any of the notifications failed,
		     * a copy of the event is created in order to repeat the attempt later
		     */
		    if ((eventFailCopy != null) && !eventFailCopy.getSubscriptions().isEmpty()) {
			eventFailCopy.setFailTime(new Date());
			eventFailCopy.setDefaultSubject(subjectToSend);
			eventFailCopy.setDefaultMessage(messageToSend);
			eventFailCopy.setTriggered(true);
			EventNotificationService.getInstance().saveEvent(eventFailCopy);
		    }
		}
	    }, "LAMS_" + getFullSignature() + "_notification_thread");
	    notificationThread.start();
	}
    }

    /**
     * See {@link IEventNotificationService#unsubscribe(String, String, Long, Long)
     */
    protected boolean unsubscribe(Integer userId) throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}
	List<Subscription> subscriptionList = new ArrayList<Subscription>(getSubscriptions());
	boolean subscriptionFound = false;
	for (int index = 0; index < subscriptionList.size(); index++) {
	    Subscription subscription = subscriptionList.get(index);
	    if (subscription.getUserId().equals(userId)) {
		getSubscriptions().remove(subscription);
		subscriptionFound = true;
	    }
	}
	return subscriptionFound;
    }

    /**
     * See {@link IEventNotificationService#unsubscribe(String, String, Long, Long, AbstractDeliveryMethod)
     */
    protected boolean unsubscribe(Integer userId, AbstractDeliveryMethod deliveryMethod)
	    throws InvalidParameterException {
	if (userId == null) {
	    throw new InvalidParameterException("User ID can not be null.");
	}
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method can not be null.");
	}
	List<Subscription> subscriptionList = new ArrayList<Subscription>(getSubscriptions());
	for (int index = 0; index < subscriptionList.size(); index++) {
	    Subscription subscription = subscriptionList.get(index);
	    if (subscription.getUserId().equals(userId) && subscription.getDeliveryMethod().equals(deliveryMethod)) {
		getSubscriptions().remove(subscription);
		return true;
	    }
	}
	return false;
    }

    /**
     * @hibernate.property column="fail_time"
     * @return
     */
    protected Date getFailTime() {
	return failTime;
    }

    protected void setFailTime(Date failTime) {
	this.failTime = failTime;
    }

    /**
     * @hibernate.property column="message"
     * @return
     */
    protected String getMessage() {
	return message;
    }

    protected void setMessage(String message) {
	this.message = message;
    }

    /**
     * @hibernate.property column="subject"
     * @return
     */
    protected String getSubject() {
	return subject;
    }

    protected void setSubject(String subject) {
	this.subject = subject;
    }

    /**
     * @hibernate.property column="html_format" length="1"
     * @return
     */
    protected boolean isHtmlFormat() {
	return htmlFormat;
    }

    protected void setHtmlFormat(boolean isHtmlContentType) {
	this.htmlFormat = isHtmlContentType;
    }
}