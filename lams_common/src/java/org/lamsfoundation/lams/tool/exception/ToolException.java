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
}
