/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
*/

package org.lamsfoundation.lams.learning.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.export.ExportPortfolioException;

/**
 * Copied from Jacky's class in the survey tool.
 * 
 * @author mtruong
 */
public class CustomStrutsExceptionHandler extends ExceptionHandler {

    //---------------------------------------------------------------------
    // Instance Data
    //---------------------------------------------------------------------
    private static Logger logger = Logger.getLogger(CustomStrutsExceptionHandler.class);
 
	    // commons logging reference
	    /**
	     * Handle the exception. Standard execute method with addition of logging
	     * the stacktrace.
	     */
    	private static final String ERROR_LEARNER = "error.system.learner";
    	private static final String ERROR_EXPORTPORTFOLIO = "error.system.exportPortfolio";
    	
	    public ActionForward execute(Exception ex,
	                                 ExceptionConfig ae,
	                                 ActionMapping mapping,
	                                 ActionForm formInstance,
	                                 HttpServletRequest request,
	                                 HttpServletResponse response) 
	    {
	        // write the exception information to the log file
	        logger.error("fatal System exception: [" + ex.getMessage() + "] ", ex);
	        ActionForward forward = null;
	        String property = null;

	        /*
	         * Get the path for the forward either from the exception element or
	         * from the input attribute.
	         */

	        String path = null;
	        if (ae.getPath() != null)
	        {
	            path = ae.getPath();
	        }
	        else
	        {
	            path = mapping.getInput();
	        }
	        // Construct the forward object
	        forward = new ActionForward(path);

	        // some exceptions do have null messages, so be careful!
	        String exceptionMessage = ex.getMessage(); 
	        String errorMessage = exceptionMessage == null || exceptionMessage.equals("null") ? ex.getClass().getName() : ex.getMessage();
	        ActionMessage error = null;
	        if (LearnerServiceException.class.isInstance(ex) ) {
		        error = new ActionMessage(ERROR_LEARNER, errorMessage );
	        }	        
	        if (ExportPortfolioException.class.isInstance(ex) ) {
		        error = new ActionMessage(ERROR_EXPORTPORTFOLIO, errorMessage );
	        }
	        storeException(request, property, error, forward, ae.getScope( ));
	        // process the exception as normal
	        return forward;
	     

	    }
}
