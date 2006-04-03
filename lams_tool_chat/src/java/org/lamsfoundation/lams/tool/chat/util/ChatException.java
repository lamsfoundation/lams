package org.lamsfoundation.lams.tool.chat.util;

/**
 * 
 * @author Anthony Sukkar
 *
 */
public class ChatException extends RuntimeException {

	public ChatException(String message) {
		super(message);
	}

	public ChatException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChatException() {
		super();

	}

	public ChatException(Throwable cause) {
		super(cause);

	}

}
