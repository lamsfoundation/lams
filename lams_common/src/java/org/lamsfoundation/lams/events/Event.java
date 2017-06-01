package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * A event that users can subscribe to and at some point can be triggered, notifing the users.
 *
 *
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
    private String name;

    /**
     * Scope of the event. For events that are common for the whole LAMS environment,
     * {@link EventNotificationService#CORE_EVENTS_SCOPE} should be used. For tools their signature should be used.
     */
    private String scope;

    /**
     * Identifier for a session in which the event is valid. or events that are common for the whole LAMS environment
     * <code>null</code> can be used. For tools their content ID should be used.
     */
    private Long eventSessionId;

    /**
     * Set of users that are subscribed to this event, along with the required data.
     */
    private Set<Subscription> subscriptions = new HashSet<Subscription>();

    /**
     * Message that will be send when the event is triggered.
     */
    private String message;

    /**
     * Subject of the message that will be send when the event is triggered.
     */
    private String subject;

    /**
     * Boolean indicates whether the message should be sent as text/html content or regular text/plain one
     */
    private boolean htmlFormat;

    /**
     * If sending notifications fails, this property holds the time of this failure.
     */
    private Date failTime;

    public Event() {
    }

    /**
     * Basic constructor used by EventNotificationService.
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
    public Event(String scope, String name, Long sessionId, String subject, String message, boolean isHtmlContentType)
	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Event scope can not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Event name can not be blank.");
	}
	this.scope = scope;
	this.name = name;
	this.eventSessionId = sessionId;
	this.subject = subject;
	this.message = message;
	this.htmlFormat = isHtmlContentType;
    }

    /**
     * Constructor used by EventNotificationService when the event is for resending (after an initial send now failure)
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
     * @param failTime
     * @throws InvalidParameterException
     *             if scope is <code>null</code> or name is blank
     */
    public Event(String scope, String name, Long sessionId, String subject, String message, boolean isHtmlContentType, Date failTime) 	    throws InvalidParameterException {
	if (scope == null) {
	    throw new InvalidParameterException("Event scope can not be null.");
	}
	if (StringUtils.isBlank(name)) {
	    throw new InvalidParameterException("Event name can not be blank.");
	}
	this.scope = scope;
	this.name = name;
	this.eventSessionId = sessionId;
	this.subject = subject;
	this.message = message;
	this.htmlFormat = isHtmlContentType;
	this.failTime = failTime;
    }

    @Override
    public Object clone() {
	return new Event(scope, name, eventSessionId, subject, message, htmlFormat, failTime);
    }

    /**
     *
     * @return
     */
    public Long getEventSessionId() {
	return eventSessionId;
    }

    /**
     *
     * @return
     */
    public Date getFailTime() {
	return failTime;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
	return message;
    }

    /**
     *
     * @return
     */
    public String getName() {
	return name;
    }

    /**
     *
     * @return
     */
    public String getScope() {
	return scope;
    }

    /**
     *
     * @return
     */
    public String getSubject() {
	return subject;
    }

    /**
     *
     *
     *
     *
     *
     * @return
     */
    public Set<Subscription> getSubscriptions() {
	return subscriptions;
    }

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    /**
     *
     * @return
     */
    public boolean isHtmlFormat() {
	return htmlFormat;
    }

    public void setEventSessionId(Long sessionId) {
	eventSessionId = sessionId;
    }

    public void setFailTime(Date failTime) {
	this.failTime = failTime;
    }

    public void setHtmlFormat(boolean isHtmlContentType) {
	this.htmlFormat = isHtmlContentType;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setScope(String scope) {
	this.scope = scope;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
	this.subscriptions = subscriptions;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }
}