package org.lamsfoundation.lams.etherpad;

public class EtherpadException extends Exception {
    public EtherpadException(String message, Throwable cause) {
	super(message, cause);
    }

    public EtherpadException(String message) {
	super(message);
    }
}