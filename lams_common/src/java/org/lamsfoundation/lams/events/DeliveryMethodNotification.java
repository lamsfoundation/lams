package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;

/**
 * Allows sending mail from the configured mail server.
 *
 * @author Marcin Cieslak
 *
 */
public class DeliveryMethodNotification extends AbstractDeliveryMethod {
    private static final Logger log = Logger.getLogger(DeliveryMethodNotification.class);

    public static final String LAST_OPERATION_SEEN = "seen";

    private static DeliveryMethodNotification instance = null;

    protected static DeliveryMethodNotification getInstance() {
	if (DeliveryMethodNotification.instance == null) {
	    DeliveryMethodNotification.instance = new DeliveryMethodNotification();
	}
	return DeliveryMethodNotification.instance;
    }

    private DeliveryMethodNotification() {
	super((short) 2);
    }

    @Override
    protected String send(Integer fromUserId, Integer toUserId, String subject, String message, boolean isHtmlFormat)
	    throws InvalidParameterException {
	return DeliveryMethodNotification.LAST_OPERATION_SEEN;
    }

    @Override
    protected String send(Integer fromUserId, Integer toUserId, String subject, String message, boolean isHtmlFormat, String filename)
	    throws InvalidParameterException {
	return DeliveryMethodNotification.LAST_OPERATION_SEEN;
    }

    @Override
    protected boolean lastOperationFailed(Subscription subscription) {
	return (subscription.getLastOperationMessage() != null)
		&& !DeliveryMethodNotification.LAST_OPERATION_SEEN.equals(subscription.getLastOperationMessage());
    }

    @Override
    protected Logger getLog() {
	return DeliveryMethodNotification.log;
    }
}