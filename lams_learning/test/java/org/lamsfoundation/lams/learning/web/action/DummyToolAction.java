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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.service.DummyLearnerService;
import org.lamsfoundation.lams.learning.web.form.DummyToolForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.ActivityMappingWithToolWait;


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
	
	public ActionForward unspecified(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		return display(mapping, form, request, response);
	}
	
	public ActionForward finish(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) {
		DummyToolForm testForm = (DummyToolForm)form;
		
		if (testForm.getToolSessionId() == null) {
			return null;
		}
		long toolSessionId = testForm.getToolSessionId().longValue();
		
		DummyLearnerService learnerService = (DummyLearnerService)LearnerServiceProxy.getLearnerService(this.getServlet().getServletContext());
		learnerService.setRequest(request);
		String url = learnerService.completeToolActivity(toolSessionId);
		try {
			response.sendRedirect(url);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
