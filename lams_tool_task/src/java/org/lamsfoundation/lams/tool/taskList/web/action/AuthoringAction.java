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
package org.lamsfoundation.lams.tool.taskList.web.action;

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
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.service.TaskListException;
import org.lamsfoundation.lams.tool.taskList.service.UploadTaskListFileException;
import org.lamsfoundation.lams.tool.taskList.util.TaskListConditionComparator;
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

/**
 * The main action in author mode. All the authoring operations are located in
 * here. Except for operations dealing with TaskListCondition that are located
 * in <code>AuthoringTaskListConditionAction</code> action.
 * 
 * @author Steve.Ni
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.web.action.AuthoringTaskListConditionAction
 */
public class AuthoringAction extends Action {
	
	private static Logger log = Logger.getLogger(AuthoringAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String param = mapping.getParameter();
		
		//----------------------- solid taskList methods ---------------------------
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
			ITaskListService service = getTaskListService();
			TaskList taskList = service.getTaskListByContentId(contentId);
			
			taskList.setDefineLater(true);
			service.saveOrUpdateTaskList(taskList);
			
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
        
        //----------------------- Add taskListItem methods ---------------------------
        if (param.equals("newItemInit")) {
        	return newItemlInit(mapping, form, request, response);
        }
        if (param.equals("editItemInit")) {
        	return editItemInit(mapping, form, request, response);
        }
        if (param.equals("saveOrUpdateItem")) {
        	return saveOrUpdateItem(mapping, form, request, response);
        }
        if (param.equals("removeItem")) {
        	return removeItem(mapping, form, request, response);
        }
        if (param.equals("upItem")) {
        	return upItem(mapping, form, request, response);
        }
        if (param.equals("downItem")) {
        	return downItem(mapping, form, request, response);
        }
        
        return mapping.findForward(TaskListConstants.ERROR);
	}
	
	//**********************************************************
	// 				Solid taskList methods
	//**********************************************************

	/**
	 * Read taskList data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * @throws ServletException 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		// save toolContentID into HTTPSession
		Long contentId = new Long(WebUtil.readLongParam(request,TaskListConstants.PARAM_TOOL_CONTENT_ID));
		
		// get back the taskList and item list and display them on page
		ITaskListService service = getTaskListService();

		List<TaskListItem> items = null;
		TaskList taskList = null;
		TaskListForm taskListForm = (TaskListForm)form;
		
		// Get contentFolderID and save to form.
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		taskListForm.setContentFolderID(contentFolderID);
				
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		taskListForm.setSessionMapID(sessionMap.getSessionID());
		
		try {
			taskList = service.getTaskListByContentId(contentId);
			//if taskList does not exist, try to use default content instead.
			if(taskList == null){
				taskList = service.getDefaultContent(contentId);
				if(taskList.getTaskListItems() != null){
					items = new ArrayList<TaskListItem>(taskList.getTaskListItems());
				}else
					items = null;
			}else
				items = service.getAuthoredItems(taskList.getUid());
			
			taskListForm.setTaskList(taskList);

			//initialize instruction attachment list
			List attachmentList = getAttachmentList(sessionMap);
			attachmentList.clear();
			attachmentList.addAll(taskList.getAttachments());
		} catch (Exception e) {
			log.error(e);
			throw new ServletException(e);
		}
		
		//initialize conditions list
		SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
		conditionList.clear();
		conditionList.addAll(taskList.getConditions());
		
		//init it to avoid null exception in following handling
		if(items == null)
			items = new ArrayList<TaskListItem>();
		else{
			TaskListUser taskListUser = null;
			//handle system default question: createBy is null, now set it to current user
			for (TaskListItem item : items) {
				if(item.getCreateBy() == null){
					if(taskListUser == null){
						//get back login user DTO
						HttpSession ss = SessionManager.getSession();
						UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
						taskListUser = new TaskListUser(user,taskList);
					}
					item.setCreateBy(taskListUser);
				}
			}
		}
		//init taskList item list
		SortedSet<TaskListItem> taskListItemList = getTaskListItemList(sessionMap);
		taskListItemList.clear();
		taskListItemList.addAll(items);
		
		sessionMap.put(TaskListConstants.ATTR_RESOURCE_FORM, taskListForm);
		return mapping.findForward(TaskListConstants.SUCCESS);
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
	private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		TaskListForm existForm = (TaskListForm) sessionMap.get(TaskListConstants.ATTR_RESOURCE_FORM);
		
		TaskListForm taskListForm = (TaskListForm )form;
		try {
			PropertyUtils.copyProperties(taskListForm, existForm);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		ToolAccessMode mode = getAccessMode(request);
		if(mode.isAuthor())
			return mapping.findForward(TaskListConstants.SUCCESS);
		else
			return mapping.findForward(TaskListConstants.DEFINE_LATER);
	}
	
	/**
	 * This method will persist all inforamtion in this authoring page, include all taskList item, information etc.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException 
	 */
	private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,	HttpServletResponse response) throws Exception {
		TaskListForm taskListForm = (TaskListForm)(form);
		
		//get back sessionMAP
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(taskListForm.getSessionMapID());
		
		ToolAccessMode mode = getAccessMode(request);
    	
		ActionMessages errors = validate(taskListForm, mapping, request);
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			if(mode.isAuthor())
	    		return mapping.findForward("author");
	    	else
	    		return mapping.findForward("monitor");			
		}
			
		
		TaskList taskList = taskListForm.getTaskList();
		ITaskListService service = getTaskListService();
		
		//**********************************Get TaskList PO*********************
		TaskList taskListPO = service.getTaskListByContentId(taskListForm.getTaskList().getContentId());
		if(taskListPO == null){
			//new TaskList, create it.
			taskListPO = taskList;
			taskListPO.setCreated(new Timestamp(new Date().getTime()));
			taskListPO.setUpdated(new Timestamp(new Date().getTime()));
		}else{
			if(mode.isAuthor()){
				Long uid = taskListPO.getUid();
				PropertyUtils.copyProperties(taskListPO,taskList);
				//get back UID
				taskListPO.setUid(uid);
			}else{ //if it is Teacher, then just update basic tab content (definelater)
				taskListPO.setInstructions(taskList.getInstructions());
				taskListPO.setTitle(taskList.getTitle());
//				change define later status
				taskListPO.setDefineLater(false);
			}
			taskListPO.setUpdated(new Timestamp(new Date().getTime()));
		}
		
		//*******************************Handle user*******************
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		TaskListUser taskListUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue())
						,taskListForm.getTaskList().getContentId());
		if(taskListUser == null){
			taskListUser = new TaskListUser(user,taskListPO);
		}
		
		taskListPO.setCreatedBy(taskListUser);
		
		//**********************************Handle Authoring Instruction Attachement *********************
    	//merge attachment info
		//so far, attPOSet will be empty if content is existed. because PropertyUtils.copyProperties() is executed
		Set attPOSet = taskListPO.getAttachments();
		if(attPOSet == null)
			attPOSet = new HashSet();
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		
		//current attachemnt in authoring instruction tab.
		Iterator iter = attachmentList.iterator();
		while(iter.hasNext()){
			TaskListAttachment newAtt = (TaskListAttachment) iter.next();
			attPOSet.add(newAtt);
		}
		attachmentList.clear();
		
		//deleted attachment. 2 possible types: one is persist another is non-persist before.
		iter = deleteAttachmentList.iterator();
		while(iter.hasNext()){
			TaskListAttachment delAtt = (TaskListAttachment) iter.next();
			iter.remove();
			//it is an existed att, then delete it from current attachmentPO
			if(delAtt.getUid() != null){
				Iterator attIter = attPOSet.iterator();
				while(attIter.hasNext()){
					TaskListAttachment att = (TaskListAttachment) attIter.next();
					if(delAtt.getUid().equals(att.getUid())){
						attIter.remove();
						break;
					}
				}
				service.deleteTaskListAttachment(delAtt.getUid());
			}//end remove from persist value
		}
		
		//copy back
		taskListPO.setAttachments(attPOSet);
		//************************* Handle taskList items *******************
		
		//Handle taskList items
		Set itemList = new LinkedHashSet();
		SortedSet<TaskListItem> items = getTaskListItemList(sessionMap);
    	for(TaskListItem item : items){
    		if(item != null){
				//This flushs user UID info to message if this user is a new user. 
				item.setCreateBy(taskListUser);
				itemList.add(item);
    		}
    	}
    	taskListPO.setTaskListItems(itemList);

    	
		//Handle taskList conditions. Also delete conditions that don't contain any taskLIstItems.
		SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
		SortedSet<TaskListCondition> conditionListWithoutEmptyElements = new TreeSet<TaskListCondition>(conditionList);
		List delTaskListConditionList = getDeletedTaskListConditionList(sessionMap);
		for (TaskListCondition condition:conditionList) {
			if (condition.getTaskListItems().size() == 0) {
				conditionListWithoutEmptyElements.remove(condition);
				delTaskListConditionList.add(condition);
			}
		}
		conditionList.clear();
		conditionList.addAll(conditionListWithoutEmptyElements);
		taskListPO.setConditions(conditionList);
		
    	//delete TaskListConditions from database.
    	iter = delTaskListConditionList.iterator();
    	while(iter.hasNext()){
    		TaskListCondition condition = (TaskListCondition) iter.next();
    		iter.remove();
    		
    		if(condition.getUid() != null)
    			service.deleteTaskListCondition(condition.getUid());
    	}

    	
    	// delete TaskListItems from database. This should be done after
		// TaskListConditions have been deleted from the database. This is due
		// to prevent errors with foreign keys.
    	List delTaskListItemList = getDeletedTaskListItemList(sessionMap);
    	iter = delTaskListItemList.iterator();
    	while(iter.hasNext()){
    		TaskListItem item = (TaskListItem) iter.next();
    		iter.remove();
    		if(item.getUid() != null)
    			service.deleteTaskListItem(item.getUid());
    	}
		
		//if MinimumNumberTasksComplete is bigger than available items, then set it topics size
		if(taskListPO.getMinimumNumberTasks() > items.size())
			taskListPO.setMinimumNumberTasks(items.size());
		
		//**********************************************
		//finally persist taskListPO again
		service.saveOrUpdateTaskList(taskListPO);
		
		service.getTaskListByContentId(taskListPO.getContentId());
		
		//initialize attachmentList again
		attachmentList = getAttachmentList(sessionMap);
		attachmentList.addAll(taskList.getAttachments());
		taskListForm.setTaskList(taskListPO);
		
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
	 * @throws UploadTaskListFileException 
	 */
	private ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws UploadTaskListFileException {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);
	}
	
	/**
	 * Handle upload offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws UploadTaskListFileException 
	 */
	private ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws UploadTaskListFileException {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,request);
	}
	
	/**
	 * Common method to upload online or offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param type
	 * @param request
	 * @return
	 * @throws UploadTaskListFileException 
	 */
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type,HttpServletRequest request) throws UploadTaskListFileException {

		TaskListForm taskListForm = (TaskListForm) form;
		//get back sessionMAP
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(taskListForm.getSessionMapID());

		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) taskListForm.getOfflineFile();
		else
			file = (FormFile) taskListForm.getOnlineFile();
		
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
		TaskListAttachment  att = service.uploadInstructionFile(file, type);
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		TaskListAttachment existAtt;
		while(iter.hasNext()){
			existAtt = (TaskListAttachment) iter.next();
			if(StringUtils.equals(existAtt.getFileName(),att.getFileName())
					&& StringUtils.equals(existAtt.getFileType(),att.getFileType())){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
				break;
			}
		}
		//add to attachmentList
		attachmentList.add(att);

		return mapping.findForward(TaskListConstants.SUCCESS);

	}
	
	/**
	 * Delete offline instruction file from current TaskList authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping,request, response,form, IToolContentHandler.TYPE_OFFLINE);
	}
	
	/**
	 * Delete online instruction file from current TaskList authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
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
		Long versionID = new Long(WebUtil.readLongParam(request,TaskListConstants.PARAM_FILE_VERSION_ID));
		Long uuID = new Long(WebUtil.readLongParam(request,TaskListConstants.PARAM_FILE_UUID));
		
		//get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		TaskListAttachment existAtt;
		while(iter.hasNext()){
			existAtt = (TaskListAttachment) iter.next();
			if(existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
			}
		}
		
		request.setAttribute(TaskListConstants.ATTR_FILE_TYPE_FLAG, type);
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(TaskListConstants.SUCCESS);

	}
	
	//**********************************************************
	// 				Add taskListItem methods
	//**********************************************************
	
	/**
	 * Display empty page for new taskList item.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newItemlInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		((TaskListItemForm)form).setSessionMapID(sessionMapID);
		
		return mapping.findForward("task");
	}
	
	/**
	 * Display edit page for existed taskList item.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward editItemInit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//	get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX),-1);
		TaskListItem item = null;
		if(itemIdx != -1){
			SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
			List<TaskListItem> rList = new ArrayList<TaskListItem>(taskListList);
			item = rList.get(itemIdx);
			if(item != null){
				populateItemToForm(itemIdx, item,(TaskListItemForm) form,request);
			}
		}		
		
		return (item==null) ? null : mapping.findForward("task"); 
	}
	
	/**
	 * This method will get necessary information from taskList item form and save or update into 
	 * <code>HttpSession</code> TaskListItemList. Notice, this save is not persist them into database,  
	 * just save <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring 
	 * page is being persisted.
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 */
	private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		TaskListItemForm itemForm = (TaskListItemForm)form;
		ActionErrors errors = validateTaskListItem(itemForm);
		
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			return mapping.findForward("task");
		}
		
		try {
			extractFormToTaskListItem(request, itemForm);
		} catch (Exception e) {
			//any upload exception will display as normal error message rather then throw exception directly
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(TaskListConstants.ERROR_MSG_UPLOAD_FAILED,e.getMessage()));
			if(!errors.isEmpty()){
				this.addErrors(request,errors);
				return mapping.findForward("task");
			}
		}
		//set session map ID so that itemlist.jsp can get sessionMAP
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, itemForm.getSessionMapID());
		//return null to close this window
		return mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Remove taskList item from HttpSession list and update page display. As authoring rule, all persist only happen when 
	 * user submit whole page. So this remove is just impact HttpSession values.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//	get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX),-1);
		if(itemIdx != -1){
			SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
			List<TaskListItem> rList = new ArrayList<TaskListItem>(taskListList);
			TaskListItem item = rList.remove(itemIdx);
			taskListList.clear();
			taskListList.addAll(rList);
			//add to delList
			List delList = getDeletedTaskListItemList(sessionMap);
			delList.add(item);
			
			//delete tasklistitems that still may be contained in Conditions 
			SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
			for (TaskListCondition condition:conditionList) {
				Set<TaskListItem> itemList = condition.getTaskListItems();
				if (itemList.contains(item)) itemList.remove(item);
			}
			
		}	
		
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Move up current item.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward upItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return switchItem(mapping, request, true);
	}
	
	/**
	 * Move down current item.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward downItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return switchItem(mapping, request, false);
	}

	private ActionForward switchItem(ActionMapping mapping, HttpServletRequest request, boolean up) {
//		get back sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(sessionMapID);
		
		int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX),-1);
		if(itemIdx != -1){
			SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
			List<TaskListItem> rList = new ArrayList<TaskListItem>(taskListList);
			//get current and the target item, and switch their sequnece
			TaskListItem item = rList.get(itemIdx);
			TaskListItem repItem;
			if(up)
				repItem = rList.get(--itemIdx);
			else
				repItem = rList.get(++itemIdx);
			int upSeqId = repItem.getSequenceId();
			repItem.setSequenceId(item.getSequenceId());
			item.setSequenceId(upSeqId);
			
			//put back list, it will be sorted again
			taskListList.clear();
			taskListList.addAll(rList);
		}	
		
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(TaskListConstants.SUCCESS);
	}

	//*************************************************************************************
	// 							Private methods for internal needs  
	//*************************************************************************************
	/**
	 * Return TaskListService bean.
	 */
	private ITaskListService getTaskListService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ITaskListService) wac.getBean(TaskListConstants.RESOURCE_SERVICE);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,TaskListConstants.ATT_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,TaskListConstants.ATTR_DELETED_ATTACHMENT_LIST);
	}
	/**
	 * List save current taskList items.
	 * @param request
	 * @return
	 */
	private SortedSet<TaskListItem> getTaskListItemList(SessionMap sessionMap) {
		SortedSet<TaskListItem> list = (SortedSet<TaskListItem>) sessionMap.get(TaskListConstants.ATTR_RESOURCE_ITEM_LIST);
		if(list == null){
			list = new TreeSet<TaskListItem>(new TaskListItemComparator());
			sessionMap.put(TaskListConstants.ATTR_RESOURCE_ITEM_LIST,list);
		}
		return list;
	}
	/**
	 * List save current taskList items.
	 * @param request
	 * @return
	 */
	private SortedSet<TaskListCondition> getTaskListConditionList(SessionMap sessionMap) {
		SortedSet<TaskListCondition> list = (SortedSet<TaskListCondition>) sessionMap.get(TaskListConstants.ATTR_CONDITION_LIST);
		if(list == null){
			list = new TreeSet<TaskListCondition>(new TaskListConditionComparator());
			sessionMap.put(TaskListConstants.ATTR_CONDITION_LIST,list);
		}
		return list;
	}
	/**
	 * List save deleted taskList items, which could be persisted or non-persisted items. 
	 * @param request
	 * @return
	 */
	private List getDeletedTaskListConditionList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,TaskListConstants.ATTR_DELETED_CONDITION_LIST);
	}
	/**
	 * List save deleted taskList items, which could be persisted or non-persisted items. 
	 * @param request
	 * @return
	 */
	private List getDeletedTaskListItemList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,TaskListConstants.ATTR_DELETED_RESOURCE_ITEM_LIST);
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
	 * This method will populate taskList item information to its form for edit use.
	 * @param itemIdx
	 * @param item
	 * @param form
	 * @param request
	 */
	private void populateItemToForm(int itemIdx, TaskListItem item, TaskListItemForm form, HttpServletRequest request) {
		form.setDescription(item.getDescription());
		form.setTitle(item.getTitle());
		if(itemIdx >=0)
			form.setItemIndex(new Integer(itemIdx).toString());
		form.setRequired(item.isRequired());
		form.setCommentsAllowed(item.isCommentsAllowed());
		form.setCommentsRequired(item.isCommentsRequired());
		form.setFilesAllowed(item.isFilesAllowed());
		form.setFilesRequired(item.isFilesRequired());
		// The next 2 options always will be true (as it set in jsp). Waiting
		// for the final decision -- if this options will be needed later.
		form.setCommentsFilesAllowed(item.isCommentsFilesAllowed());
		form.setShowCommentsToAll(item.getShowCommentsToAll());
		form.setChildTask(item.isChildTask());
		form.setParentTaskName(item.getParentTaskName());
	}
	
	/**
	 * Extract web from content to taskList item.
	 * @param request
	 * @param itemForm
	 * @throws TaskListException 
	 */
	private void extractFormToTaskListItem(HttpServletRequest request, TaskListItemForm itemForm) 
		throws Exception {
		/* BE CAREFUL: This method will copy necessary info from request form to a old or new TaskListItem instance.
		 * It gets all info EXCEPT TaskListItem.createDate and TaskListItem.createBy, which need be set when persisting 
		 * this taskList item.
		 */
		
		SessionMap sessionMap = (SessionMap)request.getSession().getAttribute(itemForm.getSessionMapID());
		//check whether it is "edit(old item)" or "add(new item)"
		SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
		int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(),-1);
		TaskListItem item = null;
		
		if(itemIdx == -1){ //add
			item = new TaskListItem();
			item.setCreateDate(new Timestamp(new Date().getTime()));
			int maxSeq = 1;
			if(taskListList != null && taskListList.size() > 0){
				TaskListItem last = taskListList.last();
				maxSeq = last.getSequenceId()+1;
			}
			item.setSequenceId(maxSeq);
			taskListList.add(item);
		}else{ //edit
			List<TaskListItem> rList = new ArrayList<TaskListItem>(taskListList);
			item = rList.get(itemIdx);
		}

		item.setTitle(itemForm.getTitle());
		item.setDescription(itemForm.getDescription());
		item.setCreateByAuthor(true);
		
		item.setRequired(itemForm.isRequired());
		item.setCommentsAllowed(itemForm.isCommentsAllowed());
		item.setCommentsRequired(itemForm.isCommentsRequired());
		item.setFilesAllowed(itemForm.isFilesAllowed());
		item.setFilesRequired(itemForm.isFilesRequired());
		// The next 2 options always will be true (as it set in jsp). Waiting
		// for the final decision -- if this options will be needed later.
		item.setCommentsFilesAllowed(itemForm.isCommentsFilesAllowed());
		item.setShowCommentsToAll(itemForm.getShowCommentsToAll());
		item.setChildTask(itemForm.isChildTask());
		item.setParentTaskName(itemForm.getParentTaskName());
	}

	/**
	 * Vaidate taskList item regards to their type (url/file/learning object/website zip file)
	 * @param itemForm
	 * @return
	 */
	private ActionErrors validateTaskListItem(TaskListItemForm itemForm) {
		ActionErrors errors = new ActionErrors();
		if(StringUtils.isBlank(itemForm.getTitle()))
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(TaskListConstants.ERROR_MSG_TITLE_BLANK));
		
		return errors;
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
	
	
	private ActionMessages validate(TaskListForm taskListForm, ActionMapping mapping, HttpServletRequest request) {
		ActionMessages errors = new ActionMessages();
		//		if (StringUtils.isBlank(taskListForm.getTaskList().getTitle())) {
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
