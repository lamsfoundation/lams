/*
 * Created on 27/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.DummyLearnerService;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;
import org.lamsfoundation.lams.learning.web.form.ActivityForm;


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
		setSessionBean(sessionBean, request);

		DummyLearnerService learnerService = (DummyLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		learnerService.clearProgress();
		activityForm.setActivityId(new Long(1));
		
		ActionForward forward = mapping.findForward("displayLesson");
		return forward;
	}
	
}
