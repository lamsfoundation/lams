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
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author Manpreet Minhas
 * 
 * @struts.action 
 * 				path="/authoring" 
 * 				parameter="method"
 * 				name="SbmtAuthoringForm"
 * 				input="/sbmtAuthoring.jsp" 
 * 				scope="request" 
 * 				validate="true"
 * 
 * @struts.action-forward name="success" path="/Login.jsp"
 * @struts.action-forward name="initpage" path="/sbmtAuthoring.jsp"
 * 
 */
public class AuthoringAction extends DispatchAction {
	private Logger log = Logger.getLogger(AuthoringAction.class);
	public ISubmitFilesService submitFilesService;
	public static Logger logger = Logger.getLogger(AuthoringAction.class);
	
	public ActionForward updateContent(ActionMapping mapping,
										 ActionForm form,
										 HttpServletRequest request,
										 HttpServletResponse response){
		
		DynaActionForm authForm= (DynaActionForm)form;		
		Long contentID = (Long)authForm.get("toolContentID");
		String title = (String) authForm.get("title");
		String instructions = (String)authForm.get("instructions");
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
		try {
			submitFilesService.addSubmitFilesContent(contentID,title,instructions);
		} catch (Exception e) {
			log.error(e);
		}
		return mapping.findForward("success");
	}
	
	public ActionForward initPage(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response){
		
		Long contentID = new Long(WebUtil.readLongParam(request,"toolContentID"));
		request.setAttribute("toolContentID",contentID);
		return mapping.findForward("initpage");
	}

		
}
