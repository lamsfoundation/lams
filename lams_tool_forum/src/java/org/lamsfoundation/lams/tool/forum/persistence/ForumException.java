package org.lamsfoundation.lams.tool.forum.persistence;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 14/06/2005
 * Time: 12:33:12
 * To change this template use File | Settings | File Templates.
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
}
