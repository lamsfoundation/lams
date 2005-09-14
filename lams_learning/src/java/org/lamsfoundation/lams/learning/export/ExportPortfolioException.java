/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.export;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportPortfolioException extends java.lang.RuntimeException {
	
	public ExportPortfolioException()
	{		
	}
	
	public ExportPortfolioException(String msg)
	{
		super(msg);
	}
	
	/**
     * Constructor for wrapping the throwable object
     * @param cause
     */
    public ExportPortfolioException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor for wrapping both the customized error message and 
     * throwable exception object.
     * @param message
     * @param cause
     */
    public ExportPortfolioException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
