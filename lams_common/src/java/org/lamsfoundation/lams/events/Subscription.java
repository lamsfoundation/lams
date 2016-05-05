package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.Date;

import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Subscription for an event notification. This class binds an user to an event and stores some information on the
 * notification attempts.
 *
 * @hibernate.class table="lams_event_subscriptions"
 * @author Marcin Cieslak
 *
 */
class Subscription {
    // ------ persistent fields -------
    /**
     * Unique ID for Hibernate needs.
     */
    private Long uid;

    /**
     * ID of the subscribed user
     */
    protected Integer userId;

    protected Event event;

    /**
     * ID of the delivery method used to send a message for this subscription.
     */
    protected Short deliveryMethodId;

    /**
     * How often should the message be resend
     */
    protected Long periodicity;

    /**
     * Time of the notification attempt
     */
    protected Date lastOperationTime;

    /**
     * Message returned by a delivery methond during the last notification attempt
     */
    protected String lastOperationMessage;

    // --------- non-persitent fields ----------
    /**
     * Delivery method used to send a message.
     */
    protected AbstractDeliveryMethod deliveryMethod;

    /**
     * For Hibernate usage.
     */
    public Subscription() {

    }

    /**
     * Standard consctructor used by Events.
     *
     * @param userId
     * @param deliveryMethod
     * @param periodicity
     */
    protected Subscription(Integer userId, AbstractDeliveryMethod deliveryMethod, Long periodicity) {
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method can not be null.");
	}
	this.userId = userId;
	this.deliveryMethod = deliveryMethod;
	setPeriodicity(periodicity);
	deliveryMethodId = deliveryMethod.getId();
    }

    /**
     * Only user ID and delivery method count.
     */
    @Override
    public boolean equals(Object o) {
	if (!(o instanceof Subscription)) {
	    return false;
	}
	Subscription other = (Subscription) o;
	return other.getUserId().equals(getUserId()) && other.getDeliveryMethod().equals(getDeliveryMethod());
    }

    protected AbstractDeliveryMethod getDeliveryMethod() {
	if (deliveryMethod == null) {
	    for (AbstractDeliveryMethod delivery : EventNotificationService.getInstance()
		    .getAvailableDeliveryMethods()) {
		if (delivery.getId() == deliveryMethodId) {
		    deliveryMethod = delivery;
		}
	    }
	}
	return deliveryMethod;
    }

    /**
     * @hibernate.property column="last_operation_message"
     * @return
     */
    protected String getLastOperationMessage() {
	return lastOperationMessage;
    }

    protected boolean getLastOperationSuccessful() {
	return lastOperationMessage == null;
    }

    /**
     * @hibernate.property column="periodicity"
     * @return
     */
    protected Long getPeriodicity() {
	return periodicity;
    }

    /**
     * @hibernate.property column="user_id"
     * @return
     */
    protected Integer getUserId() {
	return userId;
    }

    /**
     * @hibernate.many-to-one
     * @hibernate.column name="event_uid"
     * @return
     */
    public Event getEvent() {
	return event;
    }

    public void setEvent(Event event) {
	this.event = event;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUserId()).append(getDeliveryMethod()).toHashCode();
    }

    /**
     * States if a message should be send to the user or rather this subscription should be skipped.
     *
     * @return if the message should be send
     */
    protected boolean isEligibleForNotification() {
	return !getLastOperationSuccessful() || lastOperationTime == null
		|| System.currentTimeMillis() - lastOperationTime.getTime() > periodicity;
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
    protected void notifyUser(String subject, String message, boolean isHtmlFormat) {
	lastOperationTime = new Date();
	lastOperationMessage = deliveryMethod.send(null, userId, subject, message, isHtmlFormat);
    }

    protected void setPeriodicity(Long periodicity) {
	this.periodicity = periodicity == null ? IEventNotificationService.PERIODICITY_SINGLE : periodicity;
    }

    /**
     * @hibernate.property column="delivery_method_id"
     * @return
     */
    protected Short getDeliveryMethodId() {
	return deliveryMethodId;
    }

    /**
     * @hibernate.property column="last_operation_time"
     * @return
     */
    protected Date getLastOperationTime() {
	return lastOperationTime;
    }

    protected void setDeliveryMethodId(Short deliveryMethodId) {
	this.deliveryMethodId = deliveryMethodId;
    }

    protected void setLastOperationTime(Date lastOperationTime) {
	this.lastOperationTime = lastOperationTime;
    }

    protected void setUserId(Integer userId) {
	this.userId = userId;
    }

    /**
     * @hibernate.id column="uid" generator-class="native"
     */
    private Long getUid() {
	return uid;
    }

    private void setUid(Long uid) {
	this.uid = uid;
    }

    @Override
    public Object clone() {
	return new Subscription(userId, deliveryMethod, periodicity);
    }

    protected void setLastOperationMessage(String lastOperationMessage) {
	this.lastOperationMessage = lastOperationMessage;
    }
}