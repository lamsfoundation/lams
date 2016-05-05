package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;

import org.apache.commons.lang.StringUtils;

/**
 * Provides methods to notify users of an event.
 *
 * @author Marcin Cieslak
 *
 */
public abstract class AbstractDeliveryMethod {
    /**
     * Short name for the delivery method
     */
    protected final String signature;

    /**
     * Short description of the delivery method.
     */
    protected final String description;

    /**
     * Unique identifier of the delivery method.
     */
    protected final short id;

    /**
     * Maximum time for the message to be send.
     */
    protected long sendTimeout = Long.MAX_VALUE;

    /**
     * Standard constructor.
     *
     * @param id
     *            ID of the delivery method
     * @param signature
     *            signature of the delivery method
     * @param description
     *            short description of the delivery method
     * @throws InvalidParameterException
     *             if signature is blank
     */
    protected AbstractDeliveryMethod(short id, String signature, String description) throws InvalidParameterException {
	if (StringUtils.isEmpty(signature)) {
	    throw new InvalidParameterException("Signature must not be blank.");
	}
	this.signature = signature;
	this.description = description;
	this.id = id;
    }

    /**
     * Sends the message to the user.
     *
     * @param toUserId
     *            ID of the user
     * @param subject
     *            subject of the message
     * @param message
     *            text of the message
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     * @return <code>null</code> if the operation was successful; error message if it failed
     * @throws InvalidParameterException
     */
    protected abstract String send(Integer fromUserId, Integer toUserId, String subject, String message,
	    boolean isHtmlFormat) throws InvalidParameterException;

    public String getSignature() {
	return signature;
    }

    public String getDescription() {
	return description;
    }

    @Override
    public boolean equals(Object o) {
	return (o instanceof AbstractDeliveryMethod)
		&& ((AbstractDeliveryMethod) o).signature.equalsIgnoreCase(signature);
    }

    public short getId() {
	return id;
    }

    public long getSendTimeout() {
	return sendTimeout;
    }

    public void setSendTimeout(long sendTimeout) {
	this.sendTimeout = sendTimeout;
    }
}