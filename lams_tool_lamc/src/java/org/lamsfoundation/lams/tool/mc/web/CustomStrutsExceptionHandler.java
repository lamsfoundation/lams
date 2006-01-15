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

package org.lamsfoundation.lams.tool.mc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;

/**
 * Struts action level exception handler 
 * @author Ozgur Demirtas
 */
public class CustomStrutsExceptionHandler extends ExceptionHandler {
    private static Logger logger = Logger.getLogger(CustomStrutsExceptionHandler.class);
 
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
	        logger.error("path: " + path);
	        // Construct the forward object
	        forward = new ActionForward(path);
	        logger.error("forward: " + forward);

	        /* some exceptions do have null messages, so be careful! */
	        String exceptionMessage = ex.getMessage();
	        logger.error("exceptionMessage: " + exceptionMessage);
	        String errorMessage = exceptionMessage == null || exceptionMessage.equals("null") ? ex.getClass().getName() : ex.getMessage();
	        logger.error("errorMessage: " + errorMessage);
	        ActionMessage error = null;
	        if (McApplicationException.class.isInstance(ex) ) {
		        error = new ActionMessage(McAppConstants.ERROR_MCAPPLICATION, errorMessage );
	        }        
	        storeException(request, property, error, forward, ae.getScope( ));
	        /* process the exception as normal */
	        return forward;
	    }
}
