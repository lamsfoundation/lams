/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;

/**
 * @author Manpreet Minhas
 * 
 * @struts.action 
 * 				path="/tool/sbmt/authoring" 
 * 				parameter="method"
 * 				name="SbmtAuthoringForm"
 * 				input="/sbmtAuthoring.jsp" 
 * 				scope="request" 
 * 				validate="true"
 * 
 * @struts.action-forward name="success" path="/Login.jsp"
 * 				  
 */
public class AuthoringAction extends DispatchAction {
	
	public ISubmitFilesService submitFilesService;
	public static Logger logger = Logger.getLogger(AuthoringAction.class);
	
	public ActionForward updateContent(ActionMapping mapping,
										 ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response){
		
		DynaActionForm authForm= (DynaActionForm)form;		
		Long contentID = (Long)authForm.get("contentID");
		String title = (String) authForm.get("title");
		String instructions = (String)authForm.get("instructions");
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
		submitFilesService.addSubmitFilesContent(contentID,title,instructions);
		return mapping.findForward("success");
	}
}
