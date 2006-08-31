/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.form.AuthoringForm;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtWebUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Manpreet Minhas
 * @author Steve Ni
 * 
 * @struts.action path="/authoring" 
 * 				  name="SbmtAuthoringForm" 
 * 				  parameter="dispatch"
 *                input="/authoring/authoring.jsp" 
 *                scope="request" 
 *                validate="false"
 * 
 * @struts.action-forward name="success" path="/authoring/authoring.jsp"
 * @struts.action-forward name="instructionFileList" path="/authoring/parts/instructionfilelist.jsp"
 */
public class AuthoringAction extends LamsDispatchAction {
	private Logger log = Logger.getLogger(AuthoringAction.class);

	public ISubmitFilesService submitFilesService;
	
	/**
	 * This page will display initial submit tool content. Or just a blank page if the toolContentID does not
	 * exist before.
	 *  
	 * <BR>
	 * Define later will use this method to initial page as well. 
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		ToolAccessMode mode = SbmtWebUtils.getAccessMode(request);
		
		//when first time open flash icon on authoring page: mode will be null 
		if(mode == null)
			mode = ToolAccessMode.AUTHOR;
		
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		//get back the upload file list and display them on page
		submitFilesService = getService();
		
		SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
		
		if(mode.isTeacher()){
			boolean isForumEditable = SbmtWebUtils.isSbmtEditable(persistContent);
			if(!isForumEditable){
				request.setAttribute(SbmtConstants.PAGE_EDITABLE, new Boolean(isForumEditable));
				return mapping.findForward("forbidden");
			}
			
			if(!persistContent.isContentInUse()){
				persistContent.setDefineLater(true);
				submitFilesService.saveOrUpdateContent(persistContent);
			}
		}
		
		//if this content does not exist(empty without id), create a content by default content record.
		if(persistContent == null){
			persistContent = submitFilesService.createDefaultContent(contentID);
		}

		//initialize attachmentList
		List attachmentList = getAttachmentList(sessionMap);
		attachmentList.clear();
		attachmentList.addAll(persistContent.getInstructionFiles());
		List delAttachmentList = getDeletedAttachmentList(sessionMap);
		delAttachmentList.clear();
		
		//set back STRUTS component value
		AuthoringForm authForm = (AuthoringForm) form;
		authForm.initContentValue(persistContent);
		//session map
		authForm.setSessionMapID(sessionMap.getSessionID());
		//current tab
		authForm.setCurrentTab(request.getParameter("currentTab"));
		authForm.setContentFolderID(contentFolderID);
		
		return mapping.findForward("success");
	}

	
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

		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(authForm.getSessionMapID());
		
		ToolAccessMode mode = SbmtWebUtils.getAccessMode(request);
		ActionMessages errors = validate(authForm, mapping, request);
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			return mapping.getInputForward();
		}
		
		SubmitFilesContent content = getContent(form);
		
		submitFilesService = getService();
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
			List attachmentList = getAttachmentList(sessionMap);
			List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
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
			if(mode.isAuthor()){
				Long uid = persistContent.getContentID();
				PropertyUtils.copyProperties(persistContent,content);
				persistContent.setContentID(uid);
			}else{
//				if it is Teacher, then just update basic tab content (definelater)
				persistContent.setInstruction(content.getInstruction());
				persistContent.setTitle(content.getTitle());
				persistContent.setDefineLater(content.isDefineLater());
				persistContent.setRunOffline(content.isRunOffline());
			}
			
			submitFilesService.saveOrUpdateContent(persistContent);
		} catch (Exception e) {
			log.error(e);
		}
		
		//to jump to common success page in lams_central
		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
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

		AuthoringForm authForm = (AuthoringForm) form;
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(authForm.getSessionMapID());
		
		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) authForm.getOfflineFile();
		else
			file = (FormFile) authForm.getOnlineFile();
		
		if(file == null || StringUtils.isBlank(file.getFileName())){
			ActionMessages msg = new ActionMessages();
			msg.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sbmt.web.action.upload.exception"));
			saveErrors(request, msg);
			return mapping.getInputForward();
		}
			
		//upload to repository
		InstructionFiles att = submitFilesService.uploadFileToContent(authForm.getToolContentID(), file, type);
		
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		
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
		
		return mapping.getInputForward();

	}

	public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, form,request, response,IToolContentHandler.TYPE_OFFLINE);
	}
	public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, form,request, response,IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * @param request
	 * @param response
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response, String type) {
		Long versionID = new Long(WebUtil.readLongParam(request,"fileVersionId"));
		Long uuID = new Long(WebUtil.readLongParam(request,"fileUuid"));
		String sessionMapID = WebUtil.readStrParam(request, SbmtConstants.ATTR_SESSION_MAP_ID);
		
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		InstructionFiles existAtt;
		while(iter.hasNext()){
			existAtt = (InstructionFiles) iter.next();
			if(existAtt.getUuID().equals(uuID) && existAtt.getVersionID().equals(versionID)){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
			}
		}
		
		request.setAttribute(SbmtConstants.ATTR_FILE_TYPE_FLAG, type);
		request.setAttribute(SbmtConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward("instructionFileList");
	}

	//***********************************************************
	//  Private/protected methods
	//***********************************************************
	/**
	 * The private method to get content from ActionForm parameters (web page).
	 * 
	 * @param form
	 * @return
	 */
	private SubmitFilesContent getContent(ActionForm form) {
		AuthoringForm authForm = (AuthoringForm) form;
		Long contentID = authForm.getToolContentID();
		String title = authForm.getTitle();
		String instructions = authForm.getInstructions();
		String online_instruction = authForm.getOnlineInstruction();
		String offline_instruction = authForm.getOfflineInstruction();
		boolean lock_on_finished = authForm.isLockOnFinished();
		
		SubmitFilesContent content = new SubmitFilesContent();
		content.setContentID(contentID);
		content.setInstruction(instructions);
		content.setOfflineInstruction(offline_instruction);
		content.setOnlineInstruction(online_instruction);
		content.setTitle(title);
		content.setLockOnFinished(lock_on_finished);
		// content.setInstrctionFiles()
		// content.setToolSession();
		return content;
	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,SbmtConstants.ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,SbmtConstants.DELETED_ATTACHMENT_LIST);
	}
	/**
	 * Get <code>java.util.List</code> from HttpSession by given name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	private List getListFromSession(SessionMap sessionMap,String name) {
		List list = (List) sessionMap.get(name);
		if(list == null){
			list = new ArrayList();
			sessionMap.put(name,list);
		}
		return list;
	}
	

	/**
	 * Get submit file service bean.
	 * @return
	 */
	private ISubmitFilesService getService() {
		if(submitFilesService == null)
			return SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
		else
			return submitFilesService;
	}
		
	private ActionMessages validate(AuthoringForm sbmtForm, ActionMapping mapping, HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();
		if (StringUtils.isBlank(sbmtForm.getTitle())) {
			ActionMessage error = new ActionMessage("error.title.blank");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
		}
		//define it later mode(TEACHER) skip below validation.
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if(StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())){
			return errors;
		}

		//Some other validation outside basic Tab.
		
		return errors;
	}


}