package org.lamsfoundation.bb.integration.util;

/**
 * Exception that originated at LAMS server.
 */
public class LamsServerException extends Exception {
    
    public LamsServerException() {
	super();
    }

    /**
     * @param message
     */
    public LamsServerException(String message) {
	super(message);
    }

    /**
     * @param cause
     */
    public LamsServerException(Throwable cause) {
	super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public LamsServerException(String message, Throwable cause) {
	super(message, cause);
    }

}
