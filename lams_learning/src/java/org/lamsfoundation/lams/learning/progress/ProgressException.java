/*
 * ProgressException.java
 *
 * Created on 07 December 2004, 10:59
 */

package org.lamsfoundation.lams.learning.progress;

/**
 * The type of exception thrown by the ProgressEngine.
 * @author  chris
 */
public class ProgressException extends java.lang.Exception
{
    
    /**
     * Creates a new instance of <code>ProgressException</code> without detail message.
     */
    public ProgressException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>ProgressException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ProgressException(String msg)
    {
        super(msg);
    }
}
