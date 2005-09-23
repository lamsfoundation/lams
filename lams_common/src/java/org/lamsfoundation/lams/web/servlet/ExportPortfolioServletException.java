/*
 * Created on Sep 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.web.servlet;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportPortfolioServletException extends Exception{
	
	public ExportPortfolioServletException()
	{
		super();
	}
	
	 /**
     * @param message
     */
    public ExportPortfolioServletException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ExportPortfolioServletException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ExportPortfolioServletException(String message, Throwable cause) {
        super(message, cause);
    }

}
