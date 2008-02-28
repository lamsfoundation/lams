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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.taskList.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.service.UploadTaskListFileException;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ViewItemAction extends Action {
	
	private static final Logger log = Logger.getLogger(ViewItemAction.class);
	private static final String DEFUALT_PROTOCOL_REFIX = "http://";
	private static final String ALLOW_PROTOCOL_REFIX = new String("[http://|https://|ftp://|nntp://]");
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
	    //-----------------------Display Learning Object function ---------------------------
	  	if (param.equals("reviewItem")) {
       		return reviewItem(mapping, form, request, response);
        }

        return mapping.findForward(TaskListConstants.ERROR);
	}
//	/**
//	 * Open url in popup window page.
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	private ActionForward openUrlPopup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//		String url = request.getParameter(TaskListConstants.PARAM_OPEN_URL_POPUP);
//		String title = request.getParameter(TaskListConstants.PARAM_TITLE);
//		request.setAttribute(TaskListConstants.PARAM_OPEN_URL_POPUP,url);
//		request.setAttribute(TaskListConstants.PARAM_TITLE,title);
//		return mapping.findForward(TaskListConstants.SUCCESS);
//	}
	
//	/**
//	 * Return next instrucion to page. It need four input parameters, mode, itemIndex or itemUid, and insIdx.
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	private ActionForward nextInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//		String mode = request.getParameter(AttributeNames.ATTR_MODE);
//		
//		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
//		SessionMap sesionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
//
//		TaskListItem item = getTaskListItem(request, sesionMap, mode);
//		if(item == null){
//			return mapping.findForward(TaskListConstants.ERROR);
//		}
//		
//		int currIns = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_CURRENT_INSTRUCTION_INDEX),0);
//		
//		Set instructions = item.getItemInstructions();
//		InstructionNavDTO navDto = new InstructionNavDTO();
//		//For Learner upload item, its instruction will display description/comment fields in ReosourceItem.
//		if(!item.isCreateByAuthor()){
//			List<TaskListItemInstruction> navItems = new ArrayList<TaskListItemInstruction>(1);
//			//create a new instruction and put TaskListItem description into it: just for display use.
//			TaskListItemInstruction ins = new TaskListItemInstruction();
//			ins.setSequenceId(1);
//			ins.setDescription(item.getDescription());
//			navItems.add(ins);
//			navDto.setAllInstructions(navItems);
//			instructions.add(ins);
//		}else{
//			navDto.setAllInstructions(new ArrayList(instructions));
//		}
//		navDto.setTitle(item.getTitle());
//		navDto.setType(item.getType());
//		navDto.setTotal(instructions.size());
//		if(instructions.size() > 0){
//			navDto.setInstruction((TaskListItemInstruction) new ArrayList(instructions).get(currIns));
//			navDto.setCurrent(currIns+1);
//		}else{
//			navDto.setCurrent(0);
//			navDto.setInstruction(null);
//		}
//		
//		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID,sessionMapID);
//		request.setAttribute(TaskListConstants.ATTR_RESOURCE_INSTRUCTION,navDto);
//		return mapping.findForward(TaskListConstants.SUCCESS);
//	}


	/**
	 * Display main frame to display instrcution and item content.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward reviewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String mode = request.getParameter(AttributeNames.ATTR_MODE);
		
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		TaskListItem item = getTaskListItem(request,sessionMap, mode);

		String idStr = request.getParameter(TaskListConstants.ATTR_TOOL_SESSION_ID);
		Long sessionId = NumberUtils.createLong(idStr);
		//mark this item access flag if it is learner
		if(ToolAccessMode.LEARNER.toString().equals(mode)){
			ITaskListService service = getTaskListService();			
			HttpSession ss = SessionManager.getSession();
			//get back login user DTO
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			service.setItemAccess(item.getUid(),new Long(user.getUserID().intValue()),sessionId);
		}
		
		if(item == null){
			return mapping.findForward(TaskListConstants.ERROR);
		}

		//these attribute will be use to instruction navigator page
		int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX));
		request.setAttribute(TaskListConstants.PARAM_ITEM_INDEX,itemIdx);
		Long itemUid = NumberUtils.createLong(request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID));
		request.setAttribute(TaskListConstants.PARAM_RESOURCE_ITEM_UID,itemUid);
		request.setAttribute(TaskListConstants.ATTR_TOOL_SESSION_ID,sessionId);
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID,sessionMapID);
		
		
		//TODO!!!!!!!!!!!!!!!!!!!!!!!!!
//		sessionMap.put(TaskListConstants.ATTR_RESOURCE,taskList);
		
		return mapping.findForward(TaskListConstants.SUCCESS);
		
		
	}
	
	
	//*************************************************************************************
	// Private methods
	//*************************************************************************************
	/**
	 * Return resoruce item according to ToolAccessMode.
	 * @param request
	 * @param sessionMap 
	 * @param mode
	 * @return
	 */
	private TaskListItem getTaskListItem(HttpServletRequest request, SessionMap sessionMap, String mode) {
		TaskListItem item = null;		
		if(TaskListConstants.MODE_AUTHOR_SESSION.equals(mode)){
			int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX),0);
			//authoring: does not save item yet, so only has ItemList from session and identity by Index
			List<TaskListItem>  taskListList = new ArrayList<TaskListItem>(getTaskListItemList(sessionMap));
			item = taskListList.get(itemIdx);
		}else{
			Long itemUid = NumberUtils.createLong(request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID));
			
			//	get back the taskList and item list and display them on page
			ITaskListService service = getTaskListService();			
			item = service.getTaskListItemByUid(itemUid);
		}
		return item;
	}
	
	private ITaskListService getTaskListService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ITaskListService) wac.getBean(TaskListConstants.RESOURCE_SERVICE);
	}
	
	/**
	 * List save current taskList items.
	 * @param request
	 * @return
	 */
	private SortedSet<TaskListItem> getTaskListItemList(SessionMap sessionMap) {
		SortedSet<TaskListItem> list = (SortedSet) sessionMap.get(TaskListConstants.ATTR_RESOURCE_ITEM_LIST);
		if(list == null){
			list = new TreeSet<TaskListItem>(new TaskListItemComparator());
			sessionMap.put(TaskListConstants.ATTR_RESOURCE_ITEM_LIST,list);
		}
		return list;
	}	
	
	
	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	/**
//	 * Read taskList data from database and put them into HttpSession. It will redirect to init.do directly after this
//	 * method run successfully. 
//	 *  
//	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
//	 * @throws ServletException 
//	 * 
//	 */
//	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
//		
//		String mode = request.getParameter(AttributeNames.ATTR_MODE);
//		
//		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
//		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
//		
//		TaskListItem item = getTaskListItem(request,sessionMap, mode);
//
//		String idStr = request.getParameter(TaskListConstants.ATTR_TOOL_SESSION_ID);
//		Long sessionId = NumberUtils.createLong(idStr);
//		//mark this item access flag if it is learner
//		if(ToolAccessMode.LEARNER.toString().equals(mode)){
//			ITaskListService service = getTaskListService();			
//			HttpSession ss = SessionManager.getSession();
//			//get back login user DTO
//			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
//			service.setItemAccess(item.getUid(),new Long(user.getUserID().intValue()),sessionId);
//		}
//		
//		if(item == null){
//			return mapping.findForward(TaskListConstants.ERROR);
//		}
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		//save toolContentID into HTTPSession
//		Long contentId = new Long(WebUtil.readLongParam(request,TaskListConstants.PARAM_TOOL_CONTENT_ID));
//		
////		get back the taskList and item list and display them on page
//		ITaskListService service = getTaskListService();
//
//		List<TaskListItem> items = null;
//		TaskList taskList = null;
//		TaskListForm taskListForm = (TaskListForm)form;
//		
//		// Get contentFolderID and save to form.
//		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
//		taskListForm.setContentFolderID(contentFolderID);
//				
//		//initial Session Map 
//		SessionMap sessionMap = new SessionMap();
//		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
//		taskListForm.setSessionMapID(sessionMap.getSessionID());
//		
//		try {
//			taskList = service.getTaskListByContentId(contentId);
//			//if taskList does not exist, try to use default content instead.
//			if(taskList == null){
//				taskList = service.getDefaultContent(contentId);
//				if(taskList.getTaskListItems() != null){
//					items = new ArrayList<TaskListItem>(taskList.getTaskListItems());
//				}else
//					items = null;
//			}else
//				items = service.getAuthoredItems(taskList.getUid());
//			
//			taskListForm.setTaskList(taskList);
//
//			//initialize instruction attachment list
//			List attachmentList = getAttachmentList(sessionMap);
//			attachmentList.clear();
//			attachmentList.addAll(taskList.getAttachments());
//		} catch (Exception e) {
//			log.error(e);
//			throw new ServletException(e);
//		}
//		
//		//init it to avoid null exception in following handling
//		if(items == null)
//			items = new ArrayList<TaskListItem>();
//		else{
//			TaskListUser taskListUser = null;
//			//handle system default question: createBy is null, now set it to current user
//			for (TaskListItem item : items) {
//				if(item.getCreateBy() == null){
//					if(taskListUser == null){
//						//get back login user DTO
//						HttpSession ss = SessionManager.getSession();
//						UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
//						taskListUser = new TaskListUser(user,taskList);
//					}
//					item.setCreateBy(taskListUser);
//				}
//			}
//		}
//		//init taskList item list
//		SortedSet<TaskListItem> taskListItemList = getTaskListItemList(sessionMap);
//		taskListItemList.clear();
//		taskListItemList.addAll(items);
//		
//		sessionMap.put(TaskListConstants.ATTR_RESOURCE_FORM, taskListForm);
//		return mapping.findForward(TaskListConstants.SUCCESS);
//	}
//
//
//	/**
//	 * Display same entire authoring page content from HttpSession variable.
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws ServletException 
//	 */
//	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
//			HttpServletResponse response) throws ServletException {
//		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
//		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
//		TaskListForm existForm = (TaskListForm) sessionMap.get(TaskListConstants.ATTR_RESOURCE_FORM);
//		
//		TaskListForm taskListForm = (TaskListForm )form;
//		try {
//			PropertyUtils.copyProperties(taskListForm, existForm);
//		} catch (Exception e) {
//			throw new ServletException(e);
//		}
//		
//		ToolAccessMode mode = getAccessMode(request);
//		if(mode.isAuthor())
//			return mapping.findForward(TaskListConstants.SUCCESS);
//		else
//			return mapping.findForward(TaskListConstants.DEFINE_LATER);
//	}
//	/**
//	 * This method will persist all inforamtion in this authoring page, include all taskList item, information etc.
//	 * 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws ServletException 
//	 */
//	private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		TaskListForm taskListForm = (TaskListForm)(form);
//		
//		//get back sessionMAP
//		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(taskListForm.getSessionMapID());
//		
//		ToolAccessMode mode = getAccessMode(request);
//    	
//		ActionMessages errors = validate(taskListForm, mapping, request);
//		if(!errors.isEmpty()){
//			saveErrors(request, errors);
//			if(mode.isAuthor())
//	    		return mapping.findForward("author");
//	    	else
//	    		return mapping.findForward("monitor");			
//		}
//			
//		
//		TaskList taskList = taskListForm.getTaskList();
//		ITaskListService service = getTaskListService();
//		
//		//**********************************Get TaskList PO*********************
//		TaskList taskListPO = service.getTaskListByContentId(taskListForm.getTaskList().getContentId());
//		if(taskListPO == null){
//			//new TaskList, create it.
//			taskListPO = taskList;
//			taskListPO.setCreated(new Timestamp(new Date().getTime()));
//			taskListPO.setUpdated(new Timestamp(new Date().getTime()));
//		}else{
//			if(mode.isAuthor()){
//				Long uid = taskListPO.getUid();
//				PropertyUtils.copyProperties(taskListPO,taskList);
//				//get back UID
//				taskListPO.setUid(uid);
//			}else{ //if it is Teacher, then just update basic tab content (definelater)
//				taskListPO.setInstructions(taskList.getInstructions());
//				taskListPO.setTitle(taskList.getTitle());
////				change define later status
//				taskListPO.setDefineLater(false);
//			}
//			taskListPO.setUpdated(new Timestamp(new Date().getTime()));
//		}
//		
//		//*******************************Handle user*******************
//		//try to get form system session
//		HttpSession ss = SessionManager.getSession();
//		//get back login user DTO
//		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
//		TaskListUser taskListUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue())
//						,taskListForm.getTaskList().getContentId());
//		if(taskListUser == null){
//			taskListUser = new TaskListUser(user,taskListPO);
//		}
//		
//		taskListPO.setCreatedBy(taskListUser);
//		
//		//**********************************Handle Authoring Instruction Attachement *********************
//    	//merge attachment info
//		//so far, attPOSet will be empty if content is existed. because PropertyUtils.copyProperties() is executed
//		Set attPOSet = taskListPO.getAttachments();
//		if(attPOSet == null)
//			attPOSet = new HashSet();
//		List attachmentList = getAttachmentList(sessionMap);
//		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
//		
//		//current attachemnt in authoring instruction tab.
//		Iterator iter = attachmentList.iterator();
//		while(iter.hasNext()){
//			TaskListAttachment newAtt = (TaskListAttachment) iter.next();
//			attPOSet.add(newAtt);
//		}
//		attachmentList.clear();
//		
//		//deleted attachment. 2 possible types: one is persist another is non-persist before.
//		iter = deleteAttachmentList.iterator();
//		while(iter.hasNext()){
//			TaskListAttachment delAtt = (TaskListAttachment) iter.next();
//			iter.remove();
//			//it is an existed att, then delete it from current attachmentPO
//			if(delAtt.getUid() != null){
//				Iterator attIter = attPOSet.iterator();
//				while(attIter.hasNext()){
//					TaskListAttachment att = (TaskListAttachment) attIter.next();
//					if(delAtt.getUid().equals(att.getUid())){
//						attIter.remove();
//						break;
//					}
//				}
//				service.deleteTaskListAttachment(delAtt.getUid());
//			}//end remove from persist value
//		}
//		
//		//copy back
//		taskListPO.setAttachments(attPOSet);
//		//************************* Handle taskList items *******************
//		//Handle taskList items
//		Set itemList = new LinkedHashSet();
//		SortedSet topics = getTaskListItemList(sessionMap);
//    	iter = topics.iterator();
//    	while(iter.hasNext()){
//    		TaskListItem item = (TaskListItem) iter.next();
//    		if(item != null){
//				//This flushs user UID info to message if this user is a new user. 
//				item.setCreateBy(taskListUser);
//				itemList.add(item);
//    		}
//    	}
//    	taskListPO.setTaskListItems(itemList);
//    	//delete instructino file from database.
//    	List delTaskListItemList = getDeletedTaskListItemList(sessionMap);
//    	iter = delTaskListItemList.iterator();
//    	while(iter.hasNext()){
//    		TaskListItem item = (TaskListItem) iter.next();
//    		iter.remove();
//    		if(item.getUid() != null)
//    			service.deleteTaskListItem(item.getUid());
//    	}
//    	//handle taskList item attachment file:
//    	List delItemAttList = getDeletedItemAttachmentList(sessionMap);
//		iter = delItemAttList.iterator();
//		while(iter.hasNext()){
//			TaskListItem delAtt = (TaskListItem) iter.next();
//			iter.remove();
//		}
//		
//		//**********************************************
//		//finally persist taskListPO again
//		service.saveOrUpdateTaskList(taskListPO);
//		
//		//initialize attachmentList again
//		attachmentList = getAttachmentList(sessionMap);
//		attachmentList.addAll(taskList.getAttachments());
//		taskListForm.setTaskList(taskListPO);
//		
//		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
//    	if(mode.isAuthor())
//    		return mapping.findForward("author");
//    	else
//    		return mapping.findForward("monitor");
//	}
//
//	/**
//	 * Handle upload online instruction files request. 
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws UploadTaskListFileException 
//	 */
//	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) throws UploadTaskListFileException {
//		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);
//	}
//	/**
//	 * Common method to upload online or offline instruction files request.
//	 * @param mapping
//	 * @param form
//	 * @param type
//	 * @param request
//	 * @return
//	 * @throws UploadTaskListFileException 
//	 */
//	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
//			String type,HttpServletRequest request) throws UploadTaskListFileException {
//
//		TaskListForm taskListForm = (TaskListForm) form;
//		//get back sessionMAP
//		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(taskListForm.getSessionMapID());
//
//		FormFile file;
//		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
//			file = (FormFile) taskListForm.getOfflineFile();
//		else
//			file = (FormFile) taskListForm.getOnlineFile();
//		
//		if(file == null || StringUtils.isBlank(file.getFileName()))
//			return mapping.findForward(TaskListConstants.SUCCESS);
//		
//		//validate file size
//		ActionMessages errors = new ActionMessages();
//		FileValidatorUtil.validateFileSize(file, true, errors );
//		if(!errors.isEmpty()){
//			this.saveErrors(request, errors);
//			return mapping.findForward(TaskListConstants.SUCCESS);
//		}
//		
//		ITaskListService service = getTaskListService();
//		//upload to repository
//		TaskListAttachment  att = service.uploadInstructionFile(file, type);
//		//handle session value
//		List attachmentList = getAttachmentList(sessionMap);
//		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
//		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
//		Iterator iter = attachmentList.iterator();
//		TaskListAttachment existAtt;
//		while(iter.hasNext()){
//			existAtt = (TaskListAttachment) iter.next();
//			if(StringUtils.equals(existAtt.getFileName(),att.getFileName())){
//				//if there is same name attachment, delete old one
//				deleteAttachmentList.add(existAtt);
//				iter.remove();
//				break;
//			}
//		}
//		//add to attachmentList
//		attachmentList.add(att);
//
//		return mapping.findForward(TaskListConstants.SUCCESS);
//
//	}
//	/**
//	 * Delete online instruction file from current TaskList authoring page.
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) {
//		return deleteFile(mapping, request, response,form, IToolContentHandler.TYPE_ONLINE);
//	}
//
//	/**
//	 * General method to delete file (online or offline)
//	 * @param mapping 
//	 * @param request
//	 * @param response
//	 * @param form 
//	 * @param type 
//	 * @return
//	 */
//	private ActionForward deleteFile(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form, String type) {
//		Long versionID = new Long(WebUtil.readLongParam(request,TaskListConstants.PARAM_FILE_VERSION_ID));
//		Long uuID = new Long(WebUtil.readLongParam(request,TaskListConstants.PARAM_FILE_UUID));
//		
//		//get back sessionMAP
//		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
//		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
//		
//		//handle session value
//		List attachmentList = getAttachmentList(sessionMap);
//		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
//		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
//		Iterator iter = attachmentList.iterator();
//		TaskListAttachment existAtt;
//		while(iter.hasNext()){
//			existAtt = (TaskListAttachment) iter.next();
//			if(existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)){
//				//if there is same name attachment, delete old one
//				deleteAttachmentList.add(existAtt);
//				iter.remove();
//			}
//		}
//
//		request.setAttribute(TaskListConstants.ATTR_FILE_TYPE_FLAG, type);
//		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
//		return mapping.findForward(TaskListConstants.SUCCESS);
//
//	}
	
}
