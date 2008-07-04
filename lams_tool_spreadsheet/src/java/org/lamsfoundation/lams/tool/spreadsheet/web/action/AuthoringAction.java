/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.spreadsheet.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetAttachment;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.tool.spreadsheet.service.SpreadsheetApplicationException;
import org.lamsfoundation.lams.tool.spreadsheet.service.UploadSpreadsheetFileException;
import org.lamsfoundation.lams.tool.spreadsheet.util.SpreadsheetWebUtils;
import org.lamsfoundation.lams.tool.spreadsheet.web.form.SpreadsheetForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthoringAction extends Action {

	private static Logger log = Logger.getLogger(AuthoringAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String param = mapping.getParameter();
		//-----------------------Spreadsheet Author function ---------------------------
		if(param.equals("start")){
			ToolAccessMode mode = getAccessMode(request);
			//teacher mode "check for new" button enter.
			if(mode != null)
				request.setAttribute(AttributeNames.ATTR_MODE,mode.toString());
			else
				request.setAttribute(AttributeNames.ATTR_MODE,ToolAccessMode.AUTHOR.toString());
			return start(mapping, form, request, response);
		}
		if (param.equals("definelater")) {
			//update define later flag to true
			Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
			ISpreadsheetService service = getSpreadsheetService();
			Spreadsheet spreadsheet = service.getSpreadsheetByContentId(contentId);
			
			spreadsheet.setDefineLater(true);
			service.saveOrUpdateSpreadsheet(spreadsheet);
			
			request.setAttribute(AttributeNames.ATTR_MODE,ToolAccessMode.TEACHER.toString());
			return start(mapping, form, request, response);
		}		
	  	if (param.equals("initPage")) {
       		return initPage(mapping, form, request, response);
        }

	  	if (param.equals("updateContent")) {
       		return updateContent(mapping, form, request, response);
        }
        if (param.equals("uploadOnlineFile")) {
       		return uploadOnline(mapping, form, request, response);
        }
        if (param.equals("uploadOfflineFile")) {
       		return uploadOffline(mapping, form, request, response);
        }
        if (param.equals("deleteOnlineFile")) {
        	return deleteOnlineFile(mapping, form, request, response);
        }
        if (param.equals("deleteOfflineFile")) {
        	return deleteOfflineFile(mapping, form, request, response);
        }

        return mapping.findForward(SpreadsheetConstants.ERROR);
	}

	/**
	 * Read spreadsheet data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * @throws ServletException 
	 * 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		//save toolContentID into HTTPSession
		Long contentId = new Long(WebUtil.readLongParam(request,SpreadsheetConstants.PARAM_TOOL_CONTENT_ID));
		
//		get back the spreadsheet and item list and display them on page
		ISpreadsheetService service = getSpreadsheetService();

		Spreadsheet spreadsheet = null;
		SpreadsheetForm spreadsheetForm = (SpreadsheetForm)form;
		
		// Get contentFolderID and save to form.
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		spreadsheetForm.setContentFolderID(contentFolderID);
				
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		spreadsheetForm.setSessionMapID(sessionMap.getSessionID());
		
		try {
			spreadsheet = service.getSpreadsheetByContentId(contentId);
			//if spreadsheet does not exist, try to use default content instead.
			if(spreadsheet == null){
				spreadsheet = service.getDefaultContent(contentId);
			}
			spreadsheetForm.setSpreadsheet(spreadsheet);

			//initialize instruction attachment list
			List attachmentList = getAttachmentList(sessionMap);
			attachmentList.clear();
			attachmentList.addAll(spreadsheet.getAttachments());
		} catch (Exception e) {
			log.error(e);
			throw new ServletException(e);
		}
		
		sessionMap.put(SpreadsheetConstants.ATTR_RESOURCE_FORM, spreadsheetForm);
		return mapping.findForward(SpreadsheetConstants.SUCCESS);
	}


	/**
	 * Display same entire authoring page content from HttpSession variable.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException 
	 */
	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws ServletException {
		String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		SpreadsheetForm existForm = (SpreadsheetForm) sessionMap.get(SpreadsheetConstants.ATTR_RESOURCE_FORM);
		
		SpreadsheetForm spreadsheetForm = (SpreadsheetForm )form;
		try {
			PropertyUtils.copyProperties(spreadsheetForm, existForm);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		ToolAccessMode mode = getAccessMode(request);
		if(mode.isAuthor())
			return mapping.findForward(SpreadsheetConstants.SUCCESS);
		else
			return mapping.findForward(SpreadsheetConstants.DEFINE_LATER);
	}
	/**
	 * This method will persist all inforamtion in this authoring page, include all spreadsheet item, information etc.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException 
	 */
	private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
		SpreadsheetForm spreadsheetForm = (SpreadsheetForm)(form);
		
		//get back sessionMAP
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(spreadsheetForm.getSessionMapID());
		
		ToolAccessMode mode = getAccessMode(request);
    	
		ActionMessages errors = validate(spreadsheetForm, mapping, request);
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			if(mode.isAuthor())
	    		return mapping.findForward("author");
	    	else
	    		return mapping.findForward("monitor");			
		}
			
		Spreadsheet spreadsheet = spreadsheetForm.getSpreadsheet();
		ISpreadsheetService service = getSpreadsheetService();
		
		//**********************************Get Spreadsheet PO*********************
		Spreadsheet spreadsheetPO = service.getSpreadsheetByContentId(spreadsheetForm.getSpreadsheet().getContentId());
		if(spreadsheetPO == null){
			//new Spreadsheet, create it.
			spreadsheetPO = spreadsheet;
			spreadsheetPO.setCreated(new Timestamp(new Date().getTime()));
			spreadsheetPO.setUpdated(new Timestamp(new Date().getTime()));
		}else{
			if(mode.isAuthor()){
				Long uid = spreadsheetPO.getUid();
				PropertyUtils.copyProperties(spreadsheetPO,spreadsheet);
				//get back UID
				spreadsheetPO.setUid(uid);
			}else{ //if it is Teacher, then just update basic tab content (definelater)
				spreadsheetPO.setInstructions(spreadsheet.getInstructions());
				spreadsheetPO.setCode(spreadsheet.getCode());
				spreadsheetPO.setTitle(spreadsheet.getTitle());
//				change define later status
				spreadsheetPO.setDefineLater(false);
			}
			spreadsheetPO.setUpdated(new Timestamp(new Date().getTime()));
		}
		
		//*******************************Handle user*******************
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		SpreadsheetUser spreadsheetUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue())
						,spreadsheetForm.getSpreadsheet().getContentId());
		if(spreadsheetUser == null){
			spreadsheetUser = new SpreadsheetUser(user,spreadsheetPO);
		}

		spreadsheetPO.setCreatedBy(spreadsheetUser);
		
		//**********************************Handle Authoring Instruction Attachement *********************
    	//merge attachment info
		//so far, attPOSet will be empty if content is existed. because PropertyUtils.copyProperties() is executed
		Set attPOSet = spreadsheetPO.getAttachments();
		if(attPOSet == null)
			attPOSet = new HashSet();
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		
		//current attachemnt in authoring instruction tab.
		Iterator iter = attachmentList.iterator();
		while(iter.hasNext()){
			SpreadsheetAttachment newAtt = (SpreadsheetAttachment) iter.next();
			attPOSet.add(newAtt);
		}
		attachmentList.clear();
		
		//deleted attachment. 2 possible types: one is persist another is non-persist before.
		iter = deleteAttachmentList.iterator();
		while(iter.hasNext()){
			SpreadsheetAttachment delAtt = (SpreadsheetAttachment) iter.next();
			iter.remove();
			//it is an existed att, then delete it from current attachmentPO
			if(delAtt.getUid() != null){
				Iterator attIter = attPOSet.iterator();
				while(attIter.hasNext()){
					SpreadsheetAttachment att = (SpreadsheetAttachment) attIter.next();
					if(delAtt.getUid().equals(att.getUid())){
						attIter.remove();
						break;
					}
				}
				service.deleteSpreadsheetAttachment(delAtt.getUid());
			}//end remove from persist value
		}
		
		//copy back
		spreadsheetPO.setAttachments(attPOSet);
		
		//finally persist spreadsheetPO again
		service.saveOrUpdateSpreadsheet(spreadsheetPO);
		
		//initialize attachmentList again
		attachmentList = getAttachmentList(sessionMap);
		attachmentList.addAll(spreadsheet.getAttachments());
		spreadsheetForm.setSpreadsheet(spreadsheetPO);
		
		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
    	if(mode.isAuthor())
    		return mapping.findForward("author");
    	else
    		return mapping.findForward("monitor");
	}

	/**
	 * Handle upload online instruction files request. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UploadSpreadsheetFileException 
	 */
	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws UploadSpreadsheetFileException {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);
	}
	/**
	 * Handle upload offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UploadSpreadsheetFileException 
	 */
	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws UploadSpreadsheetFileException {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,request);
	}
	/**
	 * Common method to upload online or offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param type
	 * @param request
	 * @return
	 * @throws UploadSpreadsheetFileException 
	 */
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type,HttpServletRequest request) throws UploadSpreadsheetFileException {

		SpreadsheetForm spreadsheetForm = (SpreadsheetForm) form;
		//get back sessionMAP
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(spreadsheetForm.getSessionMapID());

		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) spreadsheetForm.getOfflineFile();
		else
			file = (FormFile) spreadsheetForm.getOnlineFile();
		
		if(file == null || StringUtils.isBlank(file.getFileName()))
			return mapping.findForward(SpreadsheetConstants.SUCCESS);
		
		//validate file size
		ActionMessages errors = new ActionMessages();
		FileValidatorUtil.validateFileSize(file, true, errors );
		if(!errors.isEmpty()){
			this.saveErrors(request, errors);
			return mapping.findForward(SpreadsheetConstants.SUCCESS);
		}
		
		ISpreadsheetService service = getSpreadsheetService();
		//upload to repository
		SpreadsheetAttachment  att = service.uploadInstructionFile(file, type);
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		SpreadsheetAttachment existAtt;
		while(iter.hasNext()){
			existAtt = (SpreadsheetAttachment) iter.next();
			if (StringUtils.equals(existAtt.getFileName(), att.getFileName())
					&& StringUtils.equals(existAtt.getFileType(), att.getFileType())) {
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
				break;
			}
		}
		//add to attachmentList
		attachmentList.add(att);

		return mapping.findForward(SpreadsheetConstants.SUCCESS);

	}
	/**
	 * Delete offline instruction file from current Spreadsheet authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping,request, response,form, IToolContentHandler.TYPE_OFFLINE);
	}
	/**
	 * Delete online instruction file from current Spreadsheet authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, request, response,form, IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * General method to delete file (online or offline)
	 * @param mapping 
	 * @param request
	 * @param response
	 * @param form 
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form, String type) {
		Long versionID = new Long(WebUtil.readLongParam(request,SpreadsheetConstants.PARAM_FILE_VERSION_ID));
		Long uuID = new Long(WebUtil.readLongParam(request,SpreadsheetConstants.PARAM_FILE_UUID));
		
		//get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		SpreadsheetAttachment existAtt;
		while(iter.hasNext()){
			existAtt = (SpreadsheetAttachment) iter.next();
			if(existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
			}
		}

		request.setAttribute(SpreadsheetConstants.ATTR_FILE_TYPE_FLAG, type);
		request.setAttribute(SpreadsheetConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(SpreadsheetConstants.SUCCESS);

	}
	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	/**
	 * Return SpreadsheetService bean.
	 */
	private ISpreadsheetService getSpreadsheetService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ISpreadsheetService) wac.getBean(SpreadsheetConstants.RESOURCE_SERVICE);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,SpreadsheetConstants.ATT_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,SpreadsheetConstants.ATTR_DELETED_ATTACHMENT_LIST);
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
	 * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
	 * @param request
	 * @return
	 */
	private ToolAccessMode getAccessMode(HttpServletRequest request) {
		ToolAccessMode mode;
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if(StringUtils.equalsIgnoreCase(modeStr,ToolAccessMode.TEACHER.toString()))
			mode = ToolAccessMode.TEACHER;
		else
			mode = ToolAccessMode.AUTHOR;
		return mode;
	}
	
	
	private ActionMessages validate(SpreadsheetForm spreadsheetForm, ActionMapping mapping, HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();
//		if (StringUtils.isBlank(spreadsheetForm.getSpreadsheet().getTitle())) {
//			ActionMessage error = new ActionMessage("error.resource.item.title.blank");
//			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
//		}
		
		//define it later mode(TEACHER) skip below validation.
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if(StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())){
			return errors;
		}

		//Some other validation outside basic Tab.
		
		return errors;
	}

}
