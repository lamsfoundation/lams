/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/**
 * 
 * @author Jacky Fang
 * @since  2005-4-7
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action name = "GroupingForm"
 * 				  path="/grouping" 
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.learner" scope="request"
 *                          type="org.lamsfoundation.lams.learning.service.LearnerServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="viewGrouping" path="/grouping.do?method=viewGrouping"
 * @struts:action-forward name="showGroup" path=".grouping"
 * ----------------XDoclet Tags--------------------
 */
public class GateAction extends LamsDispatchAction
{

    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(GateAction.class);

	
    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------    

	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward checkUpGate(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        return mapping.findForward(null);
    }
}
