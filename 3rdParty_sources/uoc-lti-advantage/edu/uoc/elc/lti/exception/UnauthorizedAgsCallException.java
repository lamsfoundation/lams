package edu.uoc.elc.lti.exception;

/**
 * @author xaracil@uoc.edu
 */
public class UnauthorizedAgsCallException extends RuntimeException {
	public UnauthorizedAgsCallException() {
	}

	public UnauthorizedAgsCallException(String message) {
		super(message);
	}
}
