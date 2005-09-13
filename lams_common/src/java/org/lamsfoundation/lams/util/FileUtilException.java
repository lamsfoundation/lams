/*
 * Created on Sep 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.util;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileUtilException extends Exception{
	
	public FileUtilException()
	{
		super();
	}
	
	 /**
     * @param message
     */
    public FileUtilException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public FileUtilException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public FileUtilException(String message, Throwable cause) {
        super(message, cause);
    }

}
