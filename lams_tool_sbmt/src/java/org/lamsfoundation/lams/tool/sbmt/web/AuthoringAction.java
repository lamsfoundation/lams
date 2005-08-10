/*
 * Created on May 19, 2005
 *
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author Manpreet Minhas
 * 
 * @struts.action path="/authoring" 
 * 				  name="SbmtAuthoringForm" 
 * 				  parameter="action"
 *                input="/authoring/authoring.jsp" 
 *                scope="request" 
 *                validate="true"
 * 
 * @struts.action-forward name="success" path="/authoring/success.jsp"
 * @struts.action-exception 
 * 				type="org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException" 
 * 				key="authoring.exception"
 * 				path="/authoring/authoring.jsp"

 */
public class AuthoringAction extends LookupDispatchAction {
	private Logger log = Logger.getLogger(AuthoringAction.class);

	public ISubmitFilesService submitFilesService;
	/**
	 * Update content
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		SubmitFilesContent content = getContent(form);
		
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		try {
			SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(content.getContentID());
			if(content.getContentID().equals(persistContent.getContentID())){
				copyContentSimpleProperty(persistContent,content);
				submitFilesService.updateSubmitFilesContent(persistContent);
			}else
				submitFilesService.addSubmitFilesContent(content);
		} catch (Exception e) {
			log.error(e);
		}
		return mapping.findForward("success");
	}

	private void copyContentSimpleProperty(SubmitFilesContent target, SubmitFilesContent src) {
		target.setContentID(src.getContentID());
		target.setContentInUse(src.isContentInUse());
		target.setDefineLater(src.isDefineLater());
		target.setRunOffline(src.isRunOffline());
		target.setInstruction(src.getInstruction());
		target.setOfflineInstruction(src.getOfflineInstruction());
		target.setOnlineInstruction(src.getOnlineInstruction());
		target.setRunOfflineInstruction(src.getRunOfflineInstruction());
		target.setTitle(src.getTitle());
		target.setLockOnFinished(src.isLockOnFinished());
	}

	/**
	 * Handle upload online instruction files request.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);
	}
	/**
	 * Handle upload offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,request);
	}
	/**
	 * Common method to upload online or offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param type
	 * @param request
	 * @return
	 */
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type,HttpServletRequest request) {

		DynaActionForm authForm = (DynaActionForm) form;
		
		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) authForm.get("offlineFile");
		else
			file = (FormFile) authForm.get("onlineFile");
		
		SubmitFilesContent content = getContent(form);
		//Call setUp() as early as possible , so never loss the screen value if any exception happen.
		setUp(request,content);
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		//send back the upload file list and display them on page
		SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(content.getContentID());
		content.setInstructionFiles(persistContent.getInstructionFiles());
		//content change, so call setup again.
		setUp(request,content);
		
		submitFilesService.uploadFileToContent(content.getContentID(), file, type);
		//add new uploaded file into DTO becuase content instruction file list comes from persistCotent.
		//so it is not need refresh content again.
		//content change, so call setup again.
		setUp(request,content);
		return mapping.getInputForward();

	}
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("label.authoring.upload.online.button", "uploadOnline");
		map.put("label.authoring.upload.offline.button", "uploadOffline");
		map.put("label.authoring.save.button", "updateContent");
		
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long contentID = new Long(WebUtil.readLongParam(request,"toolContentID"));
		
		//get back the upload file list and display them on page
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		
		SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
		//if this content does not exist, then reset the contentID to current value to keep it on HTML page.
		if(persistContent == null)
			persistContent = new SubmitFilesContent();
		persistContent.setContentID(contentID);
		setUp(request,persistContent);
		
		//set back STRUTS component value
		DynaActionForm authForm = (DynaActionForm) form;
		authForm.set("toolContentID",contentID);
		authForm.set("title",persistContent.getTitle());
		authForm.set("lockOnFinished",persistContent.isLockOnFinished()?"1":null);
		return mapping.getInputForward();
	}

	/**
	 * Get content from ActionForm parameters.
	 * 
	 * @param form
	 * @return
	 */
	private SubmitFilesContent getContent(ActionForm form) {
		DynaActionForm authForm = (DynaActionForm) form;
		Long contentID = (Long) authForm.get("toolContentID");
		String title = (String) authForm.get("title");
		String instructions = (String) authForm.get("instructions");
		String online_instruction = (String) authForm.get("onlineInstruction");
		String offline_instruction = (String) authForm.get("offlineInstruction");
		String value = (String) authForm.get("lockOnFinished");
		boolean lock_on_finished = StringUtils.isEmpty(value)?false:true;
		SubmitFilesContent content = new SubmitFilesContent();
		content.setContentID(contentID);
		content.setContentInUse(false);
		content.setDefineLater(false);
		content.setRunOffline(false);
		content.setInstruction(instructions);
		content.setOfflineInstruction(offline_instruction);
		content.setOnlineInstruction(online_instruction);
		content.setRunOfflineInstruction("");
		content.setTitle(title);
		content.setLockOnFinished(lock_on_finished);
		// content.setInstrctionFiles()
		// content.setToolSession();
		return content;
	}
	/**
	 * This method will set initial values in authroing web page. Any method which handle request/response
	 * will call setUp() as early as possible , so never loss the screen value if any exception happen. 
	 * @param request
	 * @param content
	 */
	private void setUp(HttpServletRequest request, SubmitFilesContent content) {
		AuthoringDTO authorDto = new AuthoringDTO(content);
		request.setAttribute(SbmtConstants.AUTHORING_DTO,authorDto);
	}

}
