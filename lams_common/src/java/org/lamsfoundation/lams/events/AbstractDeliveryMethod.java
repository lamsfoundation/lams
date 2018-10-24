package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * Provides methods to notify users of an event.
 *
 * @author Marcin Cieslak
 *
 */
public abstract class AbstractDeliveryMethod {
    /**
     * Unique identifier of the delivery method.
     */
    protected final short id;

    /**
     * Maximum time for the message to be send.
     */
    protected long sendTimeout = Long.MAX_VALUE;

    protected final Map<Long, String> errors = new HashMap<Long, String>();
    protected static final long ERROR_SILENCE_TIMEOUT = 5000;

    /**
     * Standard constructor.
     *
     * @param id
     *            ID of the delivery method
     */
    protected AbstractDeliveryMethod(short id) throws InvalidParameterException {
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

    protected abstract String send(Integer fromUserId, Integer toUserId, String subject, String message, 
	    boolean isHtmlFormat, String attachmentFilename) throws InvalidParameterException;
    
    protected abstract Logger getLog();

    protected abstract boolean lastOperationFailed(Subscription subscription);

    @Override
    public boolean equals(Object o) {
	return (o instanceof AbstractDeliveryMethod) && (((AbstractDeliveryMethod) o).id == this.id);
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

    /**
     * Checks if the error was already logged recently. If not, logs it.
     */
    protected synchronized void logError(String error) {
	Iterator<Entry<Long, String>> errorIterator = errors.entrySet().iterator();
	boolean logError = true;
	long currentTime = System.currentTimeMillis();
	// same errors younger than this will be silenced
	long silencePeriod = currentTime - AbstractDeliveryMethod.ERROR_SILENCE_TIMEOUT;
	while (errorIterator.hasNext()) {
	    Entry<Long, String> entry = errorIterator.next();
	    if (entry.getKey() < silencePeriod) {
		// clear the map from garbage
		errorIterator.remove();
	    } else if (error.equals(entry.getValue())) {
		logError = false;
		// do not break, let the clean up go through the whole map
	    }
	}
	if (logError) {
	    errors.put(currentTime, error);
	    getLog().error("Error while notifying users: " + error);
	}
    }
}