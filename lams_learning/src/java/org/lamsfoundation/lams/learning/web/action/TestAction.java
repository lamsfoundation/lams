/*
 * Created on 27/01/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;


/**
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/test" name="testForm"
 *                validate="false" scope="request"
 * 
 */
public class TestAction extends Action {

	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		ILearnerService learnerService = LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		//LearnerProgress progress = learnerService.getProgress(null, null);
		return null;
	}
	
}
