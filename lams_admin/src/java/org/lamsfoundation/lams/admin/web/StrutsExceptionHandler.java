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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.admin.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.ModuleException;

/**
 * @version
 *
 * <p>
 * <a href="StrutsExceptionHandler.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 12:07:53 on 30/06/2006
 */
public class StrutsExceptionHandler extends ExceptionHandler {

	private static Logger log = Logger.getLogger(StrutsExceptionHandler.class);
    
	public ActionForward execute(
            Exception ex,
            ExceptionConfig ae,
            ActionMapping mapping,
            ActionForm formInstance,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {

            ActionForward forward = null;
            ActionMessage error = null;
            String property = null;

            // Build the forward from the exception mapping if it exists
            // or from the form input
            if (ae.getPath() != null) {
                forward = new ActionForward(ae.getPath());
            } else {
                forward = mapping.getInputForward();
            }

            // Figure out the error
            if (ex instanceof ModuleException) {
                error = ((ModuleException) ex).getActionMessage();
                property = ((ModuleException) ex).getProperty();
            } else {
                error = new ActionMessage(ae.getKey(), ex.getMessage());
                property = error.getKey();
            }

            log.debug(ex);
            ex.printStackTrace();
            // Store the exception
            request.setAttribute(Globals.EXCEPTION_KEY, ex);
            this.storeException(request, property, error, forward, ae.getScope());

            return forward;

        }
}
