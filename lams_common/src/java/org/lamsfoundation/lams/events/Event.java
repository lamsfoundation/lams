package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * A event that users can subscribe to and at some point can be triggered, notifing the users.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_notification_event")
public class Event {

    /**
     * Unique ID for Hibernate needs.
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    /**
     * Name of the event.
     */
    @Column
    private String name;

    /**
     * Scope of the event. For events that are common for the whole LAMS environment,
     * {@link EventNotificationService#CORE_EVENTS_SCOPE} should be used. For tools their signature should be used.
     */
    @Column
    private String scope;

    /**
     * Identifier for a session in which the event is valid. or events that are common for the whole LAMS environment
     * <code>null</code> can be used. For tools their content ID should be used.
     */
    @Column(name = "event_session_id")
    private Long eventSessionId;

    /**
     * Set of users that are subscribed to this event, along with the required data.
     */
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private Set<Subscription> subscriptions = new HashSet<Subscription>();

    /**
     * Message that will be send when the event is triggered.
     */
    @Column
    private String message;

    /**
     * Subject of the message that will be send when the event is triggered.
     */
    @Column
    private String subject;

    /**
     * Boolean indicates whether the message should be sent as text/html content or regular text/plain one
     */
    @Column(name = "html_format")
    private boolean htmlFormat;

    /**
     * If sending notifications fails, this property holds the time of this failure.
     */
    @Column(name = "fail_time")
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
    public Event(String scope, String name, Long sessionId, String subject, String message, boolean isHtmlContentType,
	    Date failTime) throws InvalidParameterException {
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

    public Long getEventSessionId() {
	return eventSessionId;
    }

    public Date getFailTime() {
	return failTime;
    }

    public String getMessage() {
	return message;
    }

    public String getName() {
	return name;
    }

    public String getScope() {
	return scope;
    }

    public String getSubject() {
	return subject;
    }

    public Set<Subscription> getSubscriptions() {
	return subscriptions;
    }

    public Long getUid() {
	return uid;
    }

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