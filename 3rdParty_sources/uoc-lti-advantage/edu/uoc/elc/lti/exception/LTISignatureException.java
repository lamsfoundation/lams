package edu.uoc.elc.lti.exception;

/**
 * @author xaracil@uoc.edu
 */
public class LTISignatureException extends RuntimeException {
	public LTISignatureException(String message) {
		super(message);
	}

	public LTISignatureException(Throwable cause) {
		super(cause);
	}
}
