/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.exception;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesException extends RuntimeException {
	
	/**
     * Default Constructor
     */
    public SubmitFilesException()
    {
        super();
    }

    /**
     * Constructor for customized error message
     * @param message
     */
    public SubmitFilesException(String message)
    {
        super(message);
    }

    /**
     * Constructor for wrapping the throwable object
     * @param cause
     */
    public SubmitFilesException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor for wrapping both the customized error message and 
     * throwable exception object.
     * @param message
     * @param cause
     */
    public SubmitFilesException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
