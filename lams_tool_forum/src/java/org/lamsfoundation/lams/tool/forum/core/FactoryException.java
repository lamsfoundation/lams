package org.lamsfoundation.lams.tool.forum.core;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 3/06/2005
 * Time: 15:39:19
 * To change this template use File | Settings | File Templates.
 */
/**
 * FactoryException
 */
public class FactoryException extends RuntimeException {
    public FactoryException() {
        super();
    }

    public FactoryException(String message) {
        super(message);
    }

    public FactoryException(Throwable cause) {
        super(cause);
    }

    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
