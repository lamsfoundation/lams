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

package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/** 
 * 
 * <p>The action servlet that interacts with learner to start a lams learner
 * module, join a user to the lesson and allows a user to exit a lesson.</p>
 * 
 * <p>It is also responsible for the interaction between lams server and 
 * flash. Flash will call method implemented in this class to get progress
 * data or trigger a lams server calculation here</p>
 * 
 * <b>Note:</b>It needs to extend the <code>LamsDispatchAction</code> which has
 * been customized to accomodate struts features to solve duplicate 
 * submission problem.
 * 
 * @author Jacky Fang
 * @since 3/03/2005
 * @version 1.1
 * 
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/learner" 
 *                parameter="method" 
 *                validate="false"
 * @struts.action-exception key="error.system.survey" scope="request"
 *                          type="org.lamsfoundation.lams.learning.service.LearnerServiceException"
 *                          path=".systemError"
 * 							handler="org.lamsfoundation.lams.util.CustomStrutsExceptionHandler"
 * @struts:action-forward name="" path=""
 * 
 * ----------------XDoclet Tags--------------------
 * 
 */
public class LearnerAction extends LamsDispatchAction 
{
	
    
    private static final String PARAM_USERDATA = "userId";
    private static final String ATTR_USERDATA = "user";


    /**
     * 
     * 
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
    public ActionForward getActiveLessons(ActionMapping mapping,
                                          ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws IOException,
                                                                          ServletException
    {

        User learner = getUserData(request);
        
        ILearnerService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
        
        List lessons = learnerService.getActiveLessonsFor(learner);
        
        
        return null;
    }

    
    /**
     * Helper method to retrieve the user data. We always load up from http
     * session first to optimize the performance. If no session cache available,
     * we load it from data source.
     * @param request A standard Servlet HttpServletRequest class.
     * @param surveyService the service facade of survey tool
     * @return the user data value object
     */
    private User getUserData(HttpServletRequest request)
    {
        //retrieve complete user data from http session
        User currentUser = (User) request.getSession()
                                              .getAttribute(ATTR_USERDATA);
        //if no session cache available, retrieve it from data source
        if (currentUser == null)
        {
            int userId = WebUtil.readIntParam(request,PARAM_USERDATA);
            currentUser = LearnerServiceProxy.getUserManagementService(getServlet().getServletContext())
            								 .getUserById(new Integer(userId));
            //create session cache
            request.getSession().setAttribute(ATTR_USERDATA, currentUser);
        }
        return currentUser;
    }
}