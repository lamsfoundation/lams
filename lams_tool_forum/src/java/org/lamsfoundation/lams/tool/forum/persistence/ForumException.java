package org.lamsfoundation.lams.tool.forum.persistence;

import java.io.IOException;

/**
 * User: conradb
 * Date: 14/06/2005
 * Time: 12:33:12
 */
public class ForumException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7408922611270822369L;

	public ForumException(String message) {
        super(message);
    }

    public ForumException(String message, Throwable cause) {
        super(message, cause);
    }

	public ForumException() {
		super();
		
	}

	public ForumException(Throwable cause) {
		super(cause);
		
	}

}
