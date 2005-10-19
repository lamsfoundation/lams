package org.lamsfoundation.lams.tool.forum.core;

/**
 * User: conradb
 * Date: 6/06/2005
 * Time: 10:51:43
 */
public class PersistenceException extends RuntimeException {

	private static final long serialVersionUID = 3903937111808861090L;

	public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
