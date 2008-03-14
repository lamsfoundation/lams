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
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.service.UploadTaskListFileException;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemAttachmentComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemCommentComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
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
		
		//mark this item access flag if it is learner
		if(ToolAccessMode.LEARNER.toString().equals(mode)){
			ITaskListService service = getTaskListService();			
			HttpSession ss = SessionManager.getSession();
			//get back login user DTO
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			service.setItemAccess(item.getUid(),new Long(user.getUserID().intValue()),sessionId);
			
			//put user login into session
			sessionMap.put(TaskListConstants.ATTR_USER_LOGIN, user.getLogin());
		}
		
		if(item == null){
			return mapping.findForward(TaskListConstants.ERROR);
		}
		
		//these attribute will be use to instruction navigator page		
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
//		request.setAttribute(AttributeNames.ATTR_MODE,mode);
//		request.setAttribute(TaskListConstants.ATTR_TOOL_SESSION_ID,sessionId);
//		int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX));
//		request.setAttribute(TaskListConstants.PARAM_ITEM_INDEX,itemIdx);
		Long itemUid = NumberUtils.createLong(request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID));
//		request.setAttribute(TaskListConstants.PARAM_RESOURCE_ITEM_UID,itemUid);
		
		sessionMap.put(TaskListConstants.PARAM_RESOURCE_ITEM_UID,itemUid);
		
		//basic information
		sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM, item);
		sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM_TITLE, item.getTitle());
		sessionMap.put(TaskListConstants.ATTR_TASK_LIST_ITEM_DESCRIPTION,item.getDescription());
		sessionMap.put(TaskListConstants.ATTR_TOOL_SESSION_ID,sessionId);
		sessionMap.put(AttributeNames.ATTR_MODE,mode);
		
		//init taskList item list
		SortedSet<TaskListItemComment> commentList = getCommentList(sessionMap);
		Set<TaskListItemComment> dbComments = item.getComments();
		commentList.clear();
		if(dbComments != null){
			for(TaskListItemComment comment : dbComments){
				commentList.add(comment);
			}
		}
		
		//init taskList item list
		SortedSet<TaskListItemAttachment> attachmentList = getAttachmentList(sessionMap);
		Set<TaskListItemAttachment> dbAttachments = item.getUploadedFileList();
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
		TaskListItemForm taskListItemForm = (TaskListItemForm) form;
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(taskListItemForm.getSessionMapID());
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);

		String commentMessage = taskListItemForm.getComment();
		if(commentMessage == null || StringUtils.isBlank(commentMessage))
			return mapping.findForward(TaskListConstants.SUCCESS);
		
		TaskListItemComment comment = new TaskListItemComment();
		comment.setComment(commentMessage);
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		ITaskListService service = getTaskListService();
		TaskListUser taskListUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),sessionId);
		comment.setCreateBy(taskListUser);
		comment.setCreateDate(new Timestamp(new Date().getTime()));
		
		//handle session value
		SortedSet<TaskListItemComment> commentList = getCommentList(sessionMap);
		commentList.add(comment);
		
		//finally persist taskListPO again
		TaskListItem httpSessionItem = (TaskListItem) sessionMap.get(TaskListConstants.ATTR_TASK_LIST_ITEM);
		TaskListItem dbItem = service.getTaskListItemByUid(httpSessionItem.getUid());
		Set<TaskListItemComment> dbComments = dbItem.getComments();
		if(dbComments == null){
			dbComments = new HashSet<TaskListItemComment>();
			dbItem.setComments(dbComments);
		}
		dbComments.add(comment);
		service.saveOrUpdateTaskListItem(dbItem);
				
		form.reset(mapping, request);
		
		return mapping.findForward(TaskListConstants.SUCCESS);
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
		
		//upload to repository
		ITaskListService service = getTaskListService();
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		TaskListUser taskListUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),sessionId);
		TaskListItemAttachment  att = service.uploadTaskListItemFile(file, IToolContentHandler.TYPE_ONLINE, taskListUser);
		//handle session value
		SortedSet<TaskListItemAttachment> attachmentList = getAttachmentList(sessionMap);
		attachmentList.add(att);
		
		//finally persist taskListPO again
		TaskListItem httpSessionItem = (TaskListItem) sessionMap.get(TaskListConstants.ATTR_TASK_LIST_ITEM);
		TaskListItem dbItem = service.getTaskListItemByUid(httpSessionItem.getUid());
		Set<TaskListItemAttachment> dbAttachments = dbItem.getUploadedFileList();
		if(dbAttachments == null){
			dbAttachments = new HashSet<TaskListItemAttachment>();
			dbItem.setUploadedFileList(dbAttachments);
		}
		dbAttachments.add(att);
		service.saveOrUpdateTaskListItem(dbItem);
				
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
		Long itemUid = NumberUtils.createLong(request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID));
		
		//	get back the taskList and item list and display them on page
		ITaskListService service = getTaskListService();			
		TaskListItem item = service.getTaskListItemByUid(itemUid);

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

}
