/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.tool.sbmt.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * @author Manpreet Minhas
 * @author Steve Ni
 * 
 * @struts.action path="/authoring" 
 * 				  name="SbmtAuthoringForm" 
 * 				  parameter="dispatch"
 *                input="/authoring/authoring.jsp" 
 *                scope="session" 
 *                validate="true"
 * 
 * @struts.action-forward name="success" path="/authoring/authoring.jsp"
 * @struts.action-exception 
 * 				type="org.lamsfoundation.lams.tool.sbmt.exception.SubmitFilesException" 
 * 				key="authoring.exception"
 * 				path="/authoring/authoring.jsp"

 */
public class AuthoringAction extends LamsDispatchAction {
	private Logger log = Logger.getLogger(AuthoringAction.class);

	public ISubmitFilesService submitFilesService;
	/**
	 * Update all content for submit tool except online/offline instruction files list. 
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
			
			if(persistContent == null || content.getContentID() == null 
					|| !content.getContentID().equals(persistContent.getContentID())){
				//new content
				persistContent = content;
			}
				
			//keep Set type attribute for persist content becuase this update only 
			//include updating simple properties from web page(i.e. text value, list value, etc)
			Set attPOSet = persistContent.getInstructionFiles();
			if(attPOSet == null)
				attPOSet = new HashSet();
			List attachmentList = getAttachmentList(request);
			List deleteAttachmentList = getDeletedAttachmentList(request);
			Iterator iter = attachmentList.iterator();
			while(iter.hasNext()){
				InstructionFiles newAtt = (InstructionFiles) iter.next();
				//add new attachment, UID is not null
				if(newAtt.getUid() == null)
					attPOSet.add(newAtt);
			}
			attachmentList.clear();
			
			iter = deleteAttachmentList.iterator();
			while(iter.hasNext()){
				InstructionFiles delAtt = (InstructionFiles) iter.next();
				//delete from repository
				submitFilesService.deleteFromRepository(delAtt.getUuID(),delAtt.getVersionID());
				//it is an existed att, then delete it from current attachmentPO
				if(delAtt.getUid() != null){
					Iterator attIter = attPOSet.iterator();
					while(attIter.hasNext()){
						InstructionFiles att = (InstructionFiles) attIter.next();
						if(delAtt.getUid().equals(att.getUid())){
							attIter.remove();
							break;
						}
					}
					submitFilesService.deleteInstructionFile(content.getContentID(), delAtt.getUuID(), delAtt
							.getVersionID(), delAtt.getType());
				}//end remove from persist value
			}
			deleteAttachmentList.clear();
			
			//copy back
			content.setInstructionFiles(attPOSet);
			content.setToolSession(persistContent.getToolSession());
			//copy web page value into persist content, as above, the "Set" type value kept.
			PropertyUtils.copyProperties(persistContent,content);
			
			submitFilesService.saveOrUpdateContent(persistContent);
			request.setAttribute("sbmtSuccess", new Boolean(true));
			setUp(request, persistContent);
		} catch (Exception e) {
			log.error(e);
		}
		return mapping.findForward("success");
	}
	/**
	 * Handle upload online instruction files request. Once the file uploaded successfully, database
	 * will update accordingly. 
	 * 
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
	 * Handle upload offline instruction files request. Once the file uploaded successfully, database
	 * will update accordingly. 
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

		//upload to repository
		InstructionFiles att = submitFilesService.uploadFileToContent(content.getContentID(), file, type);
		
		//handle session value
		List attachmentList = getAttachmentList(request);
		List deleteAttachmentList = getDeletedAttachmentList(request);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		InstructionFiles existAtt;
		while(iter.hasNext()){
			existAtt = (InstructionFiles) iter.next();
			if(StringUtils.equals(existAtt.getName(),att.getName())){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
				break;
			}
		}
		//add to attachmentList
		attachmentList.add(att);
		
		//add new uploaded file into DTO becuase content instruction file list comes from persistCotent.
		//so it is not need refresh content again.
		//content change, so call setup again.
		content.setInstructionFiles(new HashSet(attachmentList));
		setUp(request,content);
		return mapping.getInputForward();

	}

	/**
	 * This page will display initial submit tool content. Or just a blank page if the toolContentID does not
	 * exist before. 
	 *  
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		//get back the upload file list and display them on page
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		
		SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
		//if this content does not exist(empty without id), create a content by default content record.
		if(persistContent == null){
			persistContent = submitFilesService.createDefaultContent(contentID);
		}
		//if find out wrong content(id not match),  
		//then reset the contentID to current value in order to keep it on HTML page.
		if(!contentID.equals(persistContent.getContentID())){
			persistContent = new SubmitFilesContent();
			persistContent.setContentID(contentID);
		}
		setUp(request,persistContent);

		//initialize attachmentList
		List attachmentList = getAttachmentList(request);
		attachmentList.addAll(persistContent.getInstructionFiles());
		
		//set back STRUTS component value
		DynaActionForm authForm = (DynaActionForm) form;
		authForm.set(AttributeNames.PARAM_TOOL_CONTENT_ID,contentID);
		authForm.set("title",persistContent.getTitle());
		authForm.set("lockOnFinished",persistContent.isLockOnFinished()?"1":null);
		return mapping.getInputForward();
	}
	/**
	 * Depreciated: Just for STRUTS LookupDispatchAction mapping function.
	 */
	protected Map getKeyMethodMap() {
		Map map = new HashMap();
		map.put("label.authoring.upload.online.button", "uploadOnline");
		map.put("label.authoring.upload.offline.button", "uploadOffline");
		map.put("label.authoring.save.button", "updateContent");
		
		return map;
	}

	/**
	 * The private method to get content from ActionForm parameters (web page).
	 * 
	 * @param form
	 * @return
	 */
	private SubmitFilesContent getContent(ActionForm form) {
		DynaActionForm authForm = (DynaActionForm) form;
		Long contentID = (Long) authForm.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
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
		String currTab = request.getParameter("currentTab");
		request.setAttribute("currentTab",currTab);
		AuthoringDTO authorDto = new AuthoringDTO(content);
		request.setAttribute(SbmtConstants.AUTHORING_DTO,authorDto);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,SbmtConstants.ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,SbmtConstants.DELETED_ATTACHMENT_LIST);
	}
	/**
	 * Get <code>java.util.List</code> from HttpSession by given name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	private List getListFromSession(HttpServletRequest request,String name) {
		List list = (List) request.getSession().getAttribute(name);
		if(list == null){
			list = new ArrayList();
			request.getSession().setAttribute(name,list);
		}
		return list;
	}
}
