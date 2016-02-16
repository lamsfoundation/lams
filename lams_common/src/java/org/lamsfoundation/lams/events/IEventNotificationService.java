package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides tools for managing events and notifing users.
 * 
 * @author Marcin Cieslak
 * 
 */
public interface IEventNotificationService {

    /**
     * Scope for the events that are common for the whole LAMS environment.
     */
    static final String CORE_EVENTS_SCOPE = "CORE";

    /**
     * Scope for events that were created after {@link #sendMessage(Long, AbstractDeliveryMethod, String, String)}
     * failed.
     */
    static final String SINGLE_MESSAGE_SCOPE = "SINGLE_MESSAGE";

    /**
     * User should be notified only once. Used when subscribing a user to an event.
     */
    static final long PERIODICITY_SINGLE = 0;

    /**
     * User should be notified daily. Used when subscribing a user to an event.
     */
    static final long PERIODICITY_DAILY = 24 * 60 * 60;

    /**
     * User should be notified weekly. Used when subscribing a user to an event.
     */
    static final long PERIODICITY_WEEKLY = IEventNotificationService.PERIODICITY_DAILY * 7;

    /**
     * User should be notified monthly. Used when subscribing a user to an event.
     */
    static final long PERIODICITY_MONTHLY = IEventNotificationService.PERIODICITY_WEEKLY * 4;

    /**
     * Period after which the thread gives up on attempting to resend messages. Currently - 2 days.
     */
    static final long RESEND_TIME_LIMIT = 2 * 24 * 60 * 60 * 1000;

    /**
     * Allows sending mail to users using the configured SMTP server. Currently it is the only delivery method
     * available.
     */
    static final DeliveryMethodMail DELIVERY_METHOD_MAIL = DeliveryMethodMail.getInstance();

    static final Set<AbstractDeliveryMethod> availableDeliveryMethods = new HashSet<AbstractDeliveryMethod>(2);

    /**
     * Creates an event and saves it into the database.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param defaultSubject
     *            subject of the message send to users; it can be altered when triggering the event
     * @param defaultMessage
     *            body of the message send to users; it can be altered when triggering the event
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     * @return <code>true</code> if the event did not exist and was correctly created
     * @throws InvalidParameterException
     *             if scope was <code>null</code> or name was blank
     */
    void createEvent(String scope, String name, Long eventSessionId, String defaultSubject, String defaultMessage,
	    boolean isHtmlFormat) throws InvalidParameterException;

    /**
     * Checks if event with the given parameters exists in the database.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @return <code>true</code> if the event exists
     * @throws InvalidParameterException
     *             if scope was <code>null</code> or name was blank
     */
    boolean eventExists(String scope, String name, Long eventSessionId) throws InvalidParameterException;

    /**
     * Gets the available delivery methods that can be used when subscribing an user to an event.
     * 
     * @return set of available delivery methods in the system
     */
    Set<AbstractDeliveryMethod> getAvailableDeliveryMethods();

    /**
     * Checks if an user is subscribed to the given event.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param userId
     *            ID of the user
     * @return <code>true</code> if the event exists and the user is subscribed (with at least one delivery method) to
     *         the event
     * @throws InvalidParameterException
     *             if scope or user ID were <code>null</code>, name was blank or event does not exist
     */
    boolean isSubscribed(String scope, String name, Long eventSessionId, Long userId) throws InvalidParameterException;

    /**
     * Notify lesson monitors with the specified message
     * 
     * @param sessionId
     *            tool session to which monitors belong
     * @param message
     *            message to be sent
     * @isHtmlFormat whether email is required to of HTML format
     * @return
     */
    boolean notifyLessonMonitors(Long sessionId, String message, boolean isHtmlFormat);

    void resendMessages();

    /**
     * Sends a single message to the given users.If it fails, an event is created for the needs of the resending
     * mechanism.
     * 
     * @param toUserId
     *            ID of users to send the message to
     * @param deliveryMethod
     *            method of messaged delivery to use
     * @param subject
     *            subject of the message to send
     * @param message
     *            body of the message to send
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     * @return <code>true</code> if the message was succefully send to the user
     * @throws InvalidParameterException
     *             if userId or delivery method are <code>null</code>
     */
    boolean sendMessage(Integer fromUserId, Integer toUserId, AbstractDeliveryMethod deliveryMethod, String subject,
	    String message, boolean isHtmlFormat) throws InvalidParameterException;

    /**
     * 
     * Sends a single message to the given user. If it fails, an event is created for the needs of the resending
     * mechanism.
     * 
     * @param toUserIds
     *            IDs of users to send the message to
     * @param deliveryMethod
     *            method of messaged delivery to use
     * @param subject
     *            subject of the message to send
     * @param message
     *            body of the message to send
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     * @return <code>true</code> if the message was succefully send to all the users; as in the current implementation a
     *         separate thread is used for sending messages, this method always returns <code>true</code>
     * @throws InvalidParameterException
     *             if userId array or delivery method are <code>null</code>
     */
    boolean sendMessage(Integer fromUserId, Integer[] toUserIds, AbstractDeliveryMethod deliveryMethod, String subject,
	    String message, boolean isHtmlFormat) throws InvalidParameterException;

    /**
     * Registeres an user for notification of the event. If a subscription with given user ID and delivery method
     * already exists, only periodicity is updated.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param userId
     *            ID of the user
     * @param deliveryMethod
     *            method of messaged delivery to use
     * @param periodicity
     *            how often the user should be notified (in seconds)
     * @throws InvalidParameterException
     *             if scope, userId or delivery method are <code>null</code>, or name is blank
     */
    void subscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod, Long periodicity) throws InvalidParameterException;

    /**
     * Triggers the event with the default (or previously set) subject and message. Each subscribed user is notified.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @throws InvalidParameterException
     *             if scope is <code>null</code> or name is blank
     */
    void trigger(String scope, String name, Long eventSessionId) throws InvalidParameterException;

    /**
     * Triggers the event with the default subject and message, modifying placeholders (<code>{0}, {1}, {2}</code>...)
     * in the message body with the <code>parameterValues</code>. Each subscribed user is notified.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param parameterValues
     *            values that should replace placeholders in the message body; for each object its text representation
     *            is acquired by <code>toString()</code> method; then, the first string replaces <code>{0}</code> tag,
     *            second one the <code>{1}</code> tag and so on.
     * @throws InvalidParameterException
     *             if scope is <code>null</code> or name is blank
     */
    void trigger(String scope, String name, Long eventSessionId, Object[] parameterValues)
	    throws InvalidParameterException;

    /**
     * Triggers the event with given subject and message. Each subscribed user is notified. Default message and subject
     * are overridden.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param subject
     *            subject of the message to send
     * @param message
     *            body of the message to send
     * @throws InvalidParameterException
     *             if scope is <code>null</code> or name is blank
     */
    void trigger(String scope, String name, Long eventSessionId, String subject, String message)
	    throws InvalidParameterException;

    /**
     * Notifies only a single user of the event using the default subject and message. Does not set the event as
     * "triggered".
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param userId
     *            ID of the user
     * @throws InvalidParameterException
     *             if scope or userId are <code>null</code> or name is blank
     */
    void triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId)
	    throws InvalidParameterException;

    /**
     * Notifies only a single user of the event using the default subject and message, modifying placeholders (
     * <code>{0}, {1}, {2}</code>...) in the message body with the <code>parameterValues</code>. Does not set the event
     * as "triggered".
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param userId
     *            ID of the user
     * @param parameterValues
     *            values that should replace placeholders in the message body; for each object its text representation
     *            is acquired by <code>toString()</code> method; then, the first string replaces <code>{0}</code> tag,
     *            second one the <code>{1}</code> tag and so on.
     * @throws InvalidParameterException
     *             if scope or userId are <code>null</code> or name is blank
     */
    void triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId, Object[] parameterValues)
	    throws InvalidParameterException;

    /**
     * Notifies only a single user of the event using the given subject and message. Does not set the event as
     * "triggered". Default subject and message are NOT overridden.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param userId
     *            ID of the user
     * @param subject
     *            subject of the message to send
     * @param message
     *            body of the message to send
     * @throws InvalidParameterException
     *             if scope or userId are <code>null</code> or name is blank
     */
    void triggerForSingleUser(String scope, String name, Long eventSessionId, Integer userId, String subject,
	    String message) throws InvalidParameterException;

    /**
     * Unregister an user from notification of the event.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param userId
     *            ID of the user
     * @throws InvalidParameterException
     *             if scope or userId are <code>null</code> or name is blank
     */
    void unsubscribe(String scope, String name, Long eventSessionId, Integer userId) throws InvalidParameterException;

    /**
     * Unregister delivery method of the user from notification of the event.
     * 
     * @param scope
     *            scope of the event
     * @param name
     *            name of the event
     * @param eventSessionId
     *            session ID of the event
     * @param userId
     *            ID of the user
     * @param deliveryMethod
     *            delivery method which should be unregistered
     * @throws InvalidParameterException
     *             if scope, userId or delivery method are <code>null</code> or name is blank
     */
    void unsubscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod) throws InvalidParameterException;
}