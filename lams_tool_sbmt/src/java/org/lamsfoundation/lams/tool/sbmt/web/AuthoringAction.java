/*
 * Created on May 19, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.actions.LookupDispatchAction;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtToolContentHandler;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manpreet Minhas
 * 
 * @struts.action path="/authoring" name="SbmtAuthoringForm" parameter="action"
 *                input="/authoring/authoring.jsp" scope="request" validate="true"
 * 
 * @struts.action-forward name="success" path="/authoring/success.jsp"
 * 
 */
public class AuthoringAction extends LookupDispatchAction {
	private Logger log = Logger.getLogger(AuthoringAction.class);

	public ISubmitFilesService submitFilesService;

	public ActionForward updateContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		DynaActionForm authForm = (DynaActionForm) form;
		Long contentID = (Long) authForm.get("toolContentID");
		String title = (String) authForm.get("title");
		String instructions = (String) authForm.get("instructions");
		String online_instruction = (String) authForm.get("onlineInstruction");
		String offline_instruction = (String) authForm.get("offlineInstruction");
		String value = (String) authForm.get("lockOnFinished");
		//todo:need confirm
		boolean lock_on_finished = false; 

		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		try {
			SubmitFilesContent content = new SubmitFilesContent();
			content.setContentID(contentID);
			content.setContentInUse(false);
			content.setDefineLater(false);
			content.setInstruction(instructions);
			content.setOfflineInstruction(offline_instruction);
			content.setOnlineInstruction(online_instruction);
			content.setRunOffline(false);
			content.setRunOfflineInstruction("");
			content.setTitle(title);
			content.setLockOnFinished(lock_on_finished);
			// content.setInstrctionFiles()
			// content.setToolSession();
			submitFilesService.addSubmitFilesContent(content);
		} catch (Exception e) {
			log.error(e);
		}
		return mapping.findForward("success");
	}

	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);

	}

	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,request);

	}

	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type,HttpServletRequest request) {

		DynaActionForm authForm = (DynaActionForm) form;
		FormFile file = (FormFile) authForm.get("filename");
		Long contentID = (Long) authForm.get("toolContentID");
		submitFilesService.uploadFileToContent(contentID, file, type);
		//get back the upload file list and display them on page
		SubmitFilesContent content = submitFilesService.getSubmitFilesContent(contentID);
		AuthoringDTO dto = new AuthoringDTO(content);
		request.setAttribute("onlinefiles",dto.getOnlineFiles());
		request.setAttribute("offlinefiles",dto.getOfflineFiles());
		
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

		Long contentID = new Long(WebUtil.readLongParam(request,
				"toolContentID"));
		request.setAttribute("toolContentID", contentID);
		return mapping.getInputForward();
	}

}
