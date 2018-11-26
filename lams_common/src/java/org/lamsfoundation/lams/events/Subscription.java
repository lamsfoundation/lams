package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Subscription for an event notification. This class binds an user to an event and stores some information on the
 * notification attempts.
 *
 *
 * @author Marcin Cieslak
 *
 */
@Entity
@Table(name = "lams_notification_subscription")
public class Subscription {
    /**
     * Unique ID for Hibernate needs.
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    /**
     * ID of the subscribed user
     */
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_uid")
    private Event event;

    /**
     * ID of the delivery method used to send a message for this subscription.
     */
    @Column(name = "delivery_method_id")
    private Short deliveryMethodId;

    /**
     * Message returned by a delivery methond during the last notification attempt
     */
    @Column(name = "last_operation_message")
    private String lastOperationMessage;

    /**
     * Delivery method used to send a message.
     */
    @Transient
    private AbstractDeliveryMethod deliveryMethod;

    /**
     * For Hibernate usage.
     */
    public Subscription() {
    }

    /**
     * Standard consctructor used by Events.
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

    public Short getDeliveryMethodId() {
	return deliveryMethodId;
    }

    public Event getEvent() {
	return event;
    }

    public String getLastOperationMessage() {
	return lastOperationMessage;
    }

    public Long getUid() {
	return uid;
    }

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