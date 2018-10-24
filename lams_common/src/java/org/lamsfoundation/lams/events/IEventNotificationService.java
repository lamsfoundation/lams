package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
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
    static final String CORE_SCOPE = "CORE";

    /**
     * Scope for the events that are meant for given lesson staff.
     */
    static final String LESSON_MONITORS_SCOPE = "LESSON_MONITORS";

    /**
     * Scope for the events that are meant for given lesson learners.
     */
    static final String LESSON_LEARNERS_SCOPE = "LESSON_LEARNERS";

    /**
     * Scope for events that were created after {@link #sendMessage(Long, AbstractDeliveryMethod, String, String)}
     * failed.
     */
    static final String SINGLE_MESSAGE_SCOPE = "SINGLE_MESSAGE";

    /**
     * Period after which the thread gives up on attempting to resend messages. Currently - 2 days.
     */
    static final long RESEND_TIME_LIMIT = 2 * 24 * 60 * 60 * 1000;

    /**
     * Allows sending mail to users using the configured SMTP server. Currently it is the only delivery method
     * available.
     */
    static final DeliveryMethodMail DELIVERY_METHOD_MAIL = DeliveryMethodMail.getInstance();

    static final DeliveryMethodNotification DELIVERY_METHOD_NOTIFICATION = DeliveryMethodNotification.getInstance();

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
     */
    void createEvent(String scope, String name, Long eventSessionId, String defaultSubject, String defaultMessage,
	    boolean isHtmlFormat);

    void createLessonEvent(String scope, String name, Long toolSessionId, String defaultSubject, String defaultMessage,
	    boolean isHtmlFormat, AbstractDeliveryMethod deliveryMethod);

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

    List<Subscription> getNotificationSubscriptions(Long lessonId, Integer userId, boolean pendingOnly, Integer limit,
	    Integer offset);

    long getNotificationPendingCount(Long lessonId, Integer userId);

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
     */
    void notifyLessonMonitors(Long sessionId, String message, boolean isHtmlFormat);

    /**
     * Notify lesson monitors with the specified message and subject
     *
     * @param lessonId
     *            tool session to which monitors belong
     * @param subject
     *            subject to be sent
     * @param message
     *            message to be sent
     * @isHtmlFormat whether email is required to of HTML format
     */
    void notifyLessonMonitors(Long lessonId, String subject, String message, boolean isHtmlFormat);

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
     * @throws InvalidParameterException
     *             if userId array or delivery method are <code>null</code>
     */
    void sendMessage(Integer fromUserId, Integer[] toUserIds, AbstractDeliveryMethod deliveryMethod, String subject,
	    String message, boolean isHtmlFormat) throws InvalidParameterException;

    /**
     * Registeres an user for notification of the event.
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
     *            method of messaged delivery to use how often the user should be notified (in seconds)
     * @throws InvalidParameterException
     *             if scope, userId or delivery method are <code>null</code>, or name is blank
     */
    void subscribe(String scope, String name, Long eventSessionId, Integer userId,
	    AbstractDeliveryMethod deliveryMethod) throws InvalidParameterException;

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

    void triggerLessonEvent(String scope, String name, Long toolContentId, String subject, String message);

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

    void triggerForSingleUser(Long subscriptionUid, String subject, String message);

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
     * Do not call from other classes. Internal use only.
     */
    void triggerInternal(Event eventData, String subject, String message);

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