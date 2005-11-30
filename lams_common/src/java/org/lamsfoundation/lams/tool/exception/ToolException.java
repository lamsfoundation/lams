/*
 * ToolException.java
 *
 * Created on 11 January 2005, 13:55
 */

package org.lamsfoundation.lams.tool.exception;

/**
 * Type of exception thrown by Tool operations and interfaces.
 * @author chris
 */
public class ToolException extends java.lang.Exception
{
    
    /**
     * Creates a new instance of <code>ToolException</code> without detail message.
     */
    public ToolException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>ToolException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ToolException(String msg)
    {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>ToolException</code>
     * for wrapping both the customized error message and 
     * throwable exception object.
     * @param message
     * @param cause
     */
    public ToolException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs an instance of <code>ToolException</code>
     * for wrapping an throwable exception object.
     * @param message
     * @param cause
     */
    public ToolException(Throwable cause)
    {
        super(cause);
    }

}
