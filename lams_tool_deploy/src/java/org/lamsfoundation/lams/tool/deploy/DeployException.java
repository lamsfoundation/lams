/*
 * DeployException.java
 *
 * Created on 29 March 2005, 16:46
 */

package org.lamsfoundation.lams.tool.deploy;

/**
 *
 * @author chris
 */
public class DeployException extends java.lang.RuntimeException
{
    
    /**
     * Creates a new instance of <code>DeployException</code> without detail message.
     */
    public DeployException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>DeployException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DeployException(String msg)
    {
        super(msg);
    }
    
    public DeployException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
    
    public DeployException(Throwable cause)
    {
        super(cause);
    }
}
