/*
 * Created on 27/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.TestLearnerService;
import org.lamsfoundation.lams.learning.web.form.TestForm;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/toolTest" name="testForm" parameter="method"
 *                validate="false" scope="request"
 * @struts:action-forward name="display" path="/toolTest.jsp"
 * @struts:action-forward name="next" path=".requestDisplay"
 * 
 */
public class ToolTestAction extends DispatchAction {

	public ActionForward display(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		ActionForward forward = mapping.findForward("display");
		return forward;
	}

	public ActionForward finish(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		TestForm testForm = (TestForm)form;
		
		if (testForm.getActivityId() == null) {
			System.out.println("foobar");
			return null;
		}
		long activityId = testForm.getActivityId().longValue();
		
		TestLearnerService learnerService = (TestLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		String url = learnerService.completeToolActivity(activityId);
		try {
			response.sendRedirect(url);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
