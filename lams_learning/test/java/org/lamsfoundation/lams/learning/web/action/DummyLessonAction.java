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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.DummyLearnerService;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/test/DummyLesson" name="activityForm"
 *                validate="false" scope="request"
 * 
 * @struts:action-forward name="displayLesson" path="/DisplayActivity.do" redirect="true"
 * 
 */
public class DummyLessonAction extends Action {
	
	/**
	 * Gets the session bean from session.
	 * @return SessionBean for this request, null if no session.
	 */
	protected static SessionBean getSessionBean(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		SessionBean sessionBean = (SessionBean)session.getAttribute(SessionBean.NAME);
		return sessionBean;
	}
	
	/**
	 * Sets the session bean for this session.
	 */
	protected void setSessionBean(SessionBean sessionBean, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.setAttribute(SessionBean.NAME, sessionBean);
	}

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActivityForm activityForm = (ActivityForm) form;
		
		SessionBean sessionBean = new SessionBean();
		IUserManagementService userService = getUserService(this.servlet.getServletContext());
		User user = userService.getUserById(new Integer(1));
		sessionBean.setLearner(user);
		setSessionBean(sessionBean, request);

		DummyLearnerService learnerService = (DummyLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		learnerService.clearProgress();
		activityForm.setActivityId(new Long(1));
		
		ActionForward forward = mapping.findForward("displayLesson");
		return forward;
	}
	

    private IUserManagementService getUserService(ServletContext servletContext) {
        return (IUserManagementService)getDomainService("userManagementService", servletContext);
    }
    
    private static Object getDomainService(String name, ServletContext servletContext) {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        return wac.getBean(name);
    }
	
}
