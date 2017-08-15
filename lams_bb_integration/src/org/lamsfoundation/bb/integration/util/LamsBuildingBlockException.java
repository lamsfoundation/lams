package org.lamsfoundation.bb.integration.util;

/**
 * Exception that originated in LAMS building block.
 */
public class LamsBuildingBlockException extends RuntimeException {
    
    private static final long serialVersionUID = -6836893578878806461L;

    public LamsBuildingBlockException() {
	super();
    }

    /**
     * @param message
     */
    public LamsBuildingBlockException(String message) {
	super(message);
    }

    /**
     * @param cause
     */
    public LamsBuildingBlockException(Throwable cause) {
	super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public LamsBuildingBlockException(String message, Throwable cause) {
	super(message, cause);
    }

}
