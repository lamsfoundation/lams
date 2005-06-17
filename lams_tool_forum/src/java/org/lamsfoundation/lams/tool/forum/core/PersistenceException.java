package org.lamsfoundation.lams.tool.forum.core;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 6/06/2005
 * Time: 10:51:43
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceException extends Exception {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
