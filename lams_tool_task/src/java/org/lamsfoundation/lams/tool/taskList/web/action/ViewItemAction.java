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
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.service.UploadTaskListFileException;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemAttachmentComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemCommentComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListForm;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListItemForm;
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
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, UploadTaskListFileException {
		
		String param = mapping.getParameter();
	    //-----------------------Display Learning Object function ---------------------------
	  	if (param.equals("reviewTask")) {
       		return reviewTask(mapping, form, request, response);
        }
        if (param.equals("addNewComment")) {
        	return addNewComment(mapping, form, request, response);
        }
        if (param.equals("uploadFile")) {
       		return uploadFile(mapping, form, request, response);
        }

        return mapping.findForward(TaskListConstants.ERROR);
	}

	/**
	 * Read taskList data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * @throws ServletException 
	 * 
	 */
	private ActionForward reviewTask(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String mode = request.getParameter(AttributeNames.ATTR_MODE);
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		TaskListItem item = getTaskListItem(request,sessionMap, mode);
		Long sessionId = NumberUtils.createLong(request.getParameter(TaskListConstants.ATTR_TOOL_SESSION_ID));
		Long sessionId2 =  new Long(request.getParameter(TaskListConstants.PARAM_TOOL_SESSION_ID));
		
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
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		request.setAttribute(AttributeNames.ATTR_MODE,mode);
		request.setAttribute(TaskListConstants.ATTR_TOOL_SESSION_ID,sessionId);
		int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX));
		request.setAttribute(TaskListConstants.PARAM_ITEM_INDEX,itemIdx);
		Long itemUid = NumberUtils.createLong(request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID));
		request.setAttribute(TaskListConstants.PARAM_RESOURCE_ITEM_UID,itemUid);
		
		//basic information
		sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM, item);
		sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM_TITLE, item.getTitle());
		sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM_DESCRIPTION,item.getDescription());
		sessionMap.put(TaskListConstants.ATTR_TOOL_SESSION_ID,sessionId);
		
		//init taskList item list
		SortedSet<TaskListItemComment> commentList = getCommentList(sessionMap);
		List<TaskListItemComment> dbComments = item.getComments();
		commentList.clear();
		if(dbComments != null){
			for(TaskListItemComment comment : dbComments){
				commentList.add(comment);
			}
		}
		
		//init taskList item list
		SortedSet<TaskListItemAttachment> attachmentList = getAttachmentList(sessionMap);
		List<TaskListItemAttachment> dbAttachments = item.getUploadedFileList();
		attachmentList.clear();
		if(dbAttachments != null){
			for(TaskListItemAttachment comment : dbAttachments){
				attachmentList.add(comment);
			}
		}
		
		return mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Adds new user commment.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addNewComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
//		//get back SessionMap
//		String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
//		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
//		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
//		
//		Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);
//		
//		String mode = request.getParameter(AttributeNames.ATTR_MODE);
//		TaskListItemForm itemForm = (TaskListItemForm)form;
//		ActionErrors errors = validateTaskListItem(itemForm);
//		
//		if(!errors.isEmpty()){
//			this.addErrors(request,errors);
//			return mapping.findForward("task");
//		}
//		
//		//create a new TaskListItem
//		TaskListItem item = new TaskListItem(); 
//		ITaskListService service = getTaskListService();
//		TaskListUser taskListUser = getCurrentUser(service,sessionId);
//		item.setTitle(itemForm.getTitle());
//		item.setDescription(itemForm.getDescription());
//		item.setCreateDate(new Timestamp(new Date().getTime()));
//		item.setCreateByAuthor(false);
//		item.setCreateBy(taskListUser);
//		
//		//setting SequenceId
//		SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
//		int maxSeq = 1;
//		if(taskListList != null && taskListList.size() > 0){
//			TaskListItem last = taskListList.last();
//			maxSeq = last.getSequenceId()+1;
//		}
//		item.setSequenceId(maxSeq);
//		
//		//save and update session
//		TaskListSession resSession = service.getTaskListSessionBySessionId(sessionId);
//		if(resSession == null){
//			log.error("Failed update TaskListSession by ID[" + sessionId + "]");
//			return  mapping.findForward(TaskListConstants.ERROR);
//		}
//		Set<TaskListItem> items = resSession.getTaskListItems();
//		if(items == null){
//			items = new HashSet<TaskListItem>();
//			resSession.setTaskListItems(items);
//		}
//		items.add(item);
//		service.saveOrUpdateTaskListSession(resSession);
//		
//		//update session value
//		SortedSet<TaskListItem> taskListItemList = getTaskListItemList(sessionMap);
//		taskListItemList.add(item);
//		
//		//URL or file upload
//		request.setAttribute(AttributeNames.ATTR_MODE,mode);
//		itemForm.reset(mapping, request);		
		return  mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Uploads specified file to repository and associates it with current TaskListItem.
	 * @param mapping
	 * @param form
	 * @param type
	 * @param request
	 * @return
	 * @throws UploadTaskListFileException 
	 */
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws UploadTaskListFileException {
		
		TaskListItemForm taskListItemForm = (TaskListItemForm) form;
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(taskListItemForm.getSessionMapID());
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);

		FormFile file = (FormFile) taskListItemForm.getUploadedFile();
		
		if(file == null || StringUtils.isBlank(file.getFileName()))
			return mapping.findForward(TaskListConstants.SUCCESS);
		
		//validate file size
		ActionMessages errors = new ActionMessages();
		FileValidatorUtil.validateFileSize(file, true, errors );
		if(!errors.isEmpty()){
			this.saveErrors(request, errors);
			return mapping.findForward(TaskListConstants.SUCCESS);
		}
		
		ITaskListService service = getTaskListService();
		//upload to repository
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		TaskListItemAttachment  att = service.uploadTaskListItemFile(file, IToolContentHandler.TYPE_ONLINE, user.getLogin());
		//handle session value
		 SortedSet<TaskListItemAttachment> attachmentList = getAttachmentList(sessionMap);
		//add to attachmentList
		attachmentList.add(att);
		
		
		//finally persist taskListPO again
		TaskListItem item = (TaskListItem) sessionMap.get(TaskListConstants.ATTR_TASK_LIST_ITEM);
		List<TaskListItemAttachment> dbAttachments = item.getUploadedFileList();
		if(dbAttachments == null){
			dbAttachments = new ArrayList<TaskListItemAttachment>();
			item.setUploadedFileList(dbAttachments);
		}
		dbAttachments.add(att);
		
		//save content to DB
		TaskList taskList = service.getTaskListBySessionId(sessionId);
		service.saveOrUpdateTaskList(taskList);
		
		form.reset(mapping, request);
		
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
	 * Returns list of taskList items.
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
	
	/**
	 * Returns list of taskitem attachments.
	 * @param sessionMap
	 * @return
	 */
	private SortedSet<TaskListItemAttachment> getAttachmentList(SessionMap sessionMap) {
		SortedSet<TaskListItemAttachment> list = (SortedSet) sessionMap.get(TaskListConstants.ATTR_TASK_LIST_ITEM_ATTACHMENT_LIST);
		if(list == null){
			list = new TreeSet<TaskListItemAttachment>(new TaskListItemAttachmentComparator());
			sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM_ATTACHMENT_LIST,list);
		}
		return list;
	}
	
	/**
	 * Returns list of taskitem comments.
	 * @param sessionMap
	 * @return
	 */
	private SortedSet<TaskListItemComment> getCommentList(SessionMap sessionMap) {
		SortedSet<TaskListItemComment> list = (SortedSet) sessionMap.get(TaskListConstants.ATTR_TASK_LIST_ITEM_COMMENT_LIST);
		if(list == null){
			list = new TreeSet<TaskListItemComment>(new TaskListItemCommentComparator());
			sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM_COMMENT_LIST,list);
		}
		return list;
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
//
//
//	/**
//	 * Display main frame to display instrcution and item content.
//	 * @param mapping
//	 * @param form
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	private ActionForward reviewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
//		//these attribute will be use to instruction navigator page
//		int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX));
//		request.setAttribute(TaskListConstants.PARAM_ITEM_INDEX,itemIdx);
//		Long itemUid = NumberUtils.createLong(request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID));
//		request.setAttribute(TaskListConstants.PARAM_RESOURCE_ITEM_UID,itemUid);
//		request.setAttribute(TaskListConstants.ATTR_TOOL_SESSION_ID,sessionId);
//		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID,sessionMapID);
//		
//		
//		//TODO!!!!!!!!!!!!!!!!!!!!!!!!!
////		sessionMap.put(TaskListConstants.ATTR_RESOURCE,taskList);
//		
//		return mapping.findForward(TaskListConstants.SUCCESS);
//		
//		
//	}
}
