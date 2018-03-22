/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.web.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * @author Jacky
 *
 */
public class CustomStrutsExceptionHandler extends ExceptionHandler {

    //---------------------------------------------------------------------
    // Instance Data
    //---------------------------------------------------------------------
    private static Logger logger = Logger.getLogger(CustomStrutsExceptionHandler.class);
    private static final String UNKNOWN_EXCEPTION = "Unknown runtime exception!";

    // commons logging reference
    /**
     * Handle the exception. Standard execute method with addition of logging
     * the stacktrace.
     */
    @Override
    public ActionForward execute(Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance,
	    HttpServletRequest request, HttpServletResponse response) {
	// write the exception information to the log file
	logger.fatal("fatal System exception: [" + ex + "] :" + ex.getMessage(), ex);
	ActionForward forward = null;
	/*
	 * Get the path for the forward either from the exception element or
	 * from the input attribute.
	 */

	String path = null;
	if (ae.getPath() != null) {
	    path = ae.getPath();
	} else {
	    path = mapping.getInput();
	}
	// Construct the forward object
	forward = new ActionForward(path);

	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	PrintStream os = new PrintStream(bos);
	ex.printStackTrace(os);
	String errorStack = new String(bos.toByteArray());
	logger.fatal(errorStack);
	
	if ( Configuration.getAsBoolean(ConfigurationKeys.ERROR_STACK_TRACE)  ) {
	    String errorMessage = ex.getMessage() == null ? UNKNOWN_EXCEPTION : ex.getMessage();
	    request.setAttribute("errorMessage", errorMessage);
	    request.setAttribute("errorName", ex.getClass().getName());
	    request.setAttribute("errorStack", errorStack);
	}
	
	// process the exception as normal
	return forward;
    }

}