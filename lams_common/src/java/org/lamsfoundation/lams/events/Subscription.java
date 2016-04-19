package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;

/**
 * Subscription for an event notification. This class binds an user to an event and stores some information on the
 * notification attempts.
 * 
 * @hibernate.class table="lams_notification_subscription"
 * @author Marcin Cieslak
 * 
 */
public class Subscription {
    /**
     * Unique ID for Hibernate needs.
     */
    private Long uid;

    /**
     * ID of the subscribed user
     */
    private Integer userId;

    private Event event;

    /**
     * ID of the delivery method used to send a message for this subscription.
     */
    private Short deliveryMethodId;

    /**
     * Message returned by a delivery methond during the last notification attempt
     */
    private String lastOperationMessage;

    // --------- non-persitent fields ----------
    /**
     * Delivery method used to send a message.
     */
    private AbstractDeliveryMethod deliveryMethod;

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
    public Subscription(Integer userId, AbstractDeliveryMethod deliveryMethod) {
	if (deliveryMethod == null) {
	    throw new InvalidParameterException("Delivery method can not be null.");
	}
	this.userId = userId;
	this.deliveryMethod = deliveryMethod;
	deliveryMethodId = deliveryMethod.getId();
    }

    @Override
    public Object clone() {
	return new Subscription(userId, deliveryMethod);
    }

    public AbstractDeliveryMethod getDeliveryMethod() {
	if (deliveryMethod == null) {
	    for (AbstractDeliveryMethod delivery : IEventNotificationService.availableDeliveryMethods) {
		if (delivery.getId() == deliveryMethodId) {
		    deliveryMethod = delivery;
		}
	    }
	}
	return deliveryMethod;
    }

    /**
     * @hibernate.property column="delivery_method_id"
     * @return
     */
    public Short getDeliveryMethodId() {
	return deliveryMethodId;
    }

    /**
     * @hibernate.many-to-one
     * @hibernate.column name="event_uid"
     * @return
     */
    public Event getEvent() {
	return event;
    }

    /**
     * @hibernate.property column="last_operation_message"
     * @return
     */
    public String getLastOperationMessage() {
	return lastOperationMessage;
    }

    /**
     * @hibernate.id column="uid" generator-class="native"
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @hibernate.property column="user_id"
     * @return
     */
    public Integer getUserId() {
	return userId;
    }

    public void setDeliveryMethodId(Short deliveryMethodId) {
	this.deliveryMethodId = deliveryMethodId;
    }

    public void setEvent(Event event) {
	this.event = event;
    }

    public void setLastOperationMessage(String lastOperationMessage) {
	this.lastOperationMessage = lastOperationMessage;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }
}