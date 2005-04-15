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

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/**
 * 
 * <p>The action servlet that provide all the monitoring functionalities. It
 * interact with the teacher via flash and JSP monitoring interface.</p>
 * 
 * @author Jacky Fang
 * @since  2005-4-15
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/monitoring" 
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.monitor" scope="request"
 *                          type="org.lamsfoundation.lams.monitoring.service.MonitoringServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="scheduler" path="/TestScheduler.jsp"
 * 
 * ----------------XDoclet Tags--------------------
 */
public class MonitoringAction extends LamsDispatchAction
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(MonitoringAction.class);
	
	private IMonitoringService monitoringService;
    //---------------------------------------------------------------------
    // Class level constants - Struts forward
    //---------------------------------------------------------------------
    private static final String SCHEDULER = "scheduler";

    //---------------------------------------------------------------------
    // Class level constants - session attributes
    //---------------------------------------------------------------------
    private static final String PARAM_LESSON_ID = "lessonId";
    
    //---------------------------------------------------------------------
    // Struts Dispatch Method
    //---------------------------------------------------------------------
    /**
     * The Struts dispatch method that starts a lesson that has been created
     * beforehand. Most likely, the request to start lesson should be triggered
     * by the flash component. This method will delegate to the Spring service
     * bean to complete all the steps for starting a lesson. Finally, a wddx
     * acknowledgement message will be serialized and sent back to the flash
     * component.
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward startLesson(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        this.monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());

        long lessonId = WebUtil.readLongParam(request,PARAM_LESSON_ID);

        monitoringService.startlesson(lessonId);

        return mapping.findForward(SCHEDULER);
    }

}
