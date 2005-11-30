/*
 * LamsToolServiceException.java
 *
 * Created on 11 January 2005, 13:56
 */

package org.lamsfoundation.lams.tool.exception;


/**
 * Type of ToolException thrown by the LamsToolService
 * @author chris
 */
public class LamsToolServiceException extends ToolException
{
    
    /**
     * Creates a new instance of <code>LamsToolServiceException</code> without detail message.
     */
    public LamsToolServiceException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>LamsToolServiceException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public LamsToolServiceException(String msg)
    {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>LamsToolServiceException</code>
     * for wrapping both the customized error message and 
     * throwable exception object.
     * @param message
     * @param cause
     */
    public LamsToolServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs an instance of <code>LamsToolServiceException</code>
     * for wrapping an throwable exception object.
     * @param message
     * @param cause
     */
    public LamsToolServiceException(Throwable cause)
    {
        super(cause);
    }

}
