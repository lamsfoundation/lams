/*
 * LearnerServiceException.java
 *
 * Created on 12 January 2005, 10:46
 */
package org.lamsfoundation.lams.learning.service;


/**
 * Type of exception thrown by the learning service.
 * @author chris
 */
public class LearnerServiceException extends java.lang.RuntimeException
{
    
    /**
     * Creates a new instance of <code>LearnerServiceException</code> without detail message.
     */
    public LearnerServiceException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>LearnerServiceException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public LearnerServiceException(String msg)
    {
        super(msg);
    }
}
