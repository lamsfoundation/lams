/*
 * Created on 27/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.DummyLearnerService;
import org.lamsfoundation.lams.learning.web.form.DummyToolForm;
import org.lamsfoundation.lams.learning.web.util.ActionMappings;
import org.lamsfoundation.lams.learning.web.util.ActionMappingsWithToolWait;


/**
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/test/DummyTool" name="dummyToolForm" parameter="method"
 *                validate="false" scope="request"
 * @struts:action-forward name="display" path="/test/dummyTool.jsp"
 * @struts:action-forward name="next" path=".requestDisplay"
 * 
 */
public class DummyToolAction extends DispatchAction {
	
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
		DummyToolForm testForm = (DummyToolForm)form;
		
		if (testForm.getActivityId() == null) {
			return null;
		}
		long activityId = testForm.getActivityId().longValue();
		
		DummyLearnerService learnerService = (DummyLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
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
