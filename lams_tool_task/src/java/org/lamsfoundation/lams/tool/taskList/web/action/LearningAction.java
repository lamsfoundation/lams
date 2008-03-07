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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.service.TaskListApplicationException;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
import org.lamsfoundation.lams.tool.taskList.web.form.ReflectionForm;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListItemForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * 
 * @author Steve.Ni
 * 	
 * @version $Revision$
 */
public class LearningAction extends Action {

	private static Logger log = Logger.getLogger(LearningAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
		//-----------------------TaskList Learner function ---------------------------
		if(param.equals("start")){
			return start(mapping, form, request, response);
		}

		if(param.equals("complete")){
			return complete(mapping, form, request, response);
		}

		if(param.equals("finish")){
			return finish(mapping, form, request, response);
		}
		
		if (param.equals("addtask")) {
			return addTask(mapping, form, request, response);
		}
        if (param.equals("saveNewTask")) {
        	return saveNewTask(mapping, form, request, response);
        }
        
		//================ Reflection =======================
		if (param.equals("newReflection")) {
			return newReflection(mapping, form, request, response);
		}
		if (param.equals("submitReflection")) {
			return submitReflection(mapping, form, request, response);
		}
		
		return  mapping.findForward(TaskListConstants.ERROR);
	}
	
	/**
	 * Initial page for add taskList item (single file or URL).
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward addTask(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		TaskListItemForm itemForm = (TaskListItemForm) form;
		itemForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
		itemForm.setSessionMapID(WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID));
		return mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Read taskList data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		
		//save toolContentID into HTTPSession
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, true);
		
		Long sessionId =  new Long(request.getParameter(TaskListConstants.PARAM_TOOL_SESSION_ID));
		
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		request.setAttribute(AttributeNames.ATTR_MODE,mode);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionId);
		
//		get back the taskList and item list and display them on page
		ITaskListService service = getTaskListService();
		TaskListUser taskListUser = null;
		if ( mode != null && mode.isTeacher() ) {
			// monitoring mode - user is specified in URL
			// taskListUser may be null if the user was force completed.
			taskListUser = getSpecifiedUser(service, sessionId, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
		} else {
			taskListUser = getCurrentUser(service,sessionId);
		}

		List<TaskListItem> items = null;
		TaskList taskList;
		items = service.getTaskListItemsBySessionId(sessionId);
		taskList = service.getTaskListBySessionId(sessionId);

		//check whehter finish lock is on/off
		boolean lock = taskList.getLockWhenFinished() && taskListUser !=null && taskListUser.isSessionFinished();
		
		// get notebook entry
		String entryText = new String();
		if ( taskListUser != null ) {
			NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL, 
					TaskListConstants.TOOL_SIGNATURE, taskListUser.getUserId().intValue());
			if (notebookEntry != null) {
				entryText = notebookEntry.getEntry();
			}
		}
		
		//basic information
		sessionMap.put(TaskListConstants.ATTR_TITLE,taskList.getTitle());
//		sessionMap.put(TaskListConstants.ATTR_RESOURCE_INSTRUCTION,taskList.getInstructions());
		sessionMap.put(TaskListConstants.ATTR_FINISH_LOCK,lock);
		sessionMap.put(TaskListConstants.ATTR_USER_FINISHED, taskListUser !=null && taskListUser.isSessionFinished());
		
		sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID,sessionId);
		sessionMap.put(AttributeNames.ATTR_MODE,mode);
		//reflection information
		sessionMap.put(TaskListConstants.ATTR_REFLECTION_ON,taskList.isReflectOnActivity());
		sessionMap.put(TaskListConstants.ATTR_REFLECTION_INSTRUCTION,taskList.getReflectInstructions());
		sessionMap.put(TaskListConstants.ATTR_REFLECTION_ENTRY, entryText);
		
		//add define later support
		if(taskList.isDefineLater()){
			return mapping.findForward("defineLater");
		}
		
		//set contentInUse flag to true!
		taskList.setContentInUse(true);
		taskList.setDefineLater(false);
		service.saveOrUpdateTaskList(taskList);
		
		//add run offline support
		if(taskList.getRunOffline()){
			sessionMap.put(TaskListConstants.PARAM_RUN_OFFLINE, true);
			return mapping.findForward("runOffline");
		}else
			sessionMap.put(TaskListConstants.PARAM_RUN_OFFLINE, false);
				
		//init taskList item list
		SortedSet<TaskListItem> taskListItemList = getTaskListItemList(sessionMap);
		taskListItemList.clear();
		if(items != null){
			//remove hidden items.
			for(TaskListItem item : items){
				//becuase in webpage will use this login name. Here is just 
				//initial it to avoid session close error in proxy object. 
				if(item.getCreateBy() != null)
					item.getCreateBy().getLoginName();
				if(!item.isHide()){
					taskListItemList.add(item);
				}
			}
		}
		
		//set complete flag for display purpose
		if ( taskListUser !=null )
			service.retrieveComplete(taskListItemList, taskListUser);
		
		sessionMap.put(TaskListConstants.ATTR_RESOURCE,taskList);
		
		return mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Mark taskList item as complete status. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward complete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String mode = request.getParameter(AttributeNames.ATTR_MODE);
		String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
		
		doComplete(request);
		
		request.setAttribute(AttributeNames.ATTR_MODE,mode);
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID,sessionMapID);
		return  mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Finish learning session. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//get back SessionMap
		String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		//get mode and ToolSessionID from sessionMAP
		ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		//auto run mode, when use finish the only one taskList item, mark it as complete then finish this activity as well.
		String taskListItemUid = request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID);
		if(taskListItemUid != null){
			doComplete(request);
			//NOTE:So far this flag is useless(31/08/2006).
			//set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
			request.setAttribute(TaskListConstants.ATTR_RUN_AUTO,true);
		}else
			request.setAttribute(TaskListConstants.ATTR_RUN_AUTO,false);
		

		ITaskListService service = getTaskListService();
		// get sessionId from HttpServletRequest
		String nextActivityUrl = null ;
		try {
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());
			
			nextActivityUrl = service.finishToolSession(sessionId,userID);
			request.setAttribute(TaskListConstants.ATTR_NEXT_ACTIVITY_URL,nextActivityUrl);
		} catch (TaskListApplicationException e) {
			log.error("Failed get next activity url:" + e.getMessage());
		}
		
		return  mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Save new user task into database.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward saveNewTask(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		//get back SessionMap
		String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		
		Long sessionId = (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);
		
		String mode = request.getParameter(AttributeNames.ATTR_MODE);
		TaskListItemForm itemForm = (TaskListItemForm)form;
		ActionErrors errors = validateTaskListItem(itemForm);
		
		if(!errors.isEmpty()){
			this.addErrors(request,errors);
			return mapping.findForward("task");
		}
		
		//create a new TaskListItem
		TaskListItem item = new TaskListItem(); 
		ITaskListService service = getTaskListService();
		TaskListUser taskListUser = getCurrentUser(service,sessionId);
		item.setTitle(itemForm.getTitle());
		item.setDescription(itemForm.getDescription());
		item.setCreateDate(new Timestamp(new Date().getTime()));
		item.setCreateByAuthor(false);
		item.setCreateBy(taskListUser);
		
		//setting SequenceId
		SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
		int maxSeq = 1;
		if(taskListList != null && taskListList.size() > 0){
			TaskListItem last = taskListList.last();
			maxSeq = last.getSequenceId()+1;
		}
		item.setSequenceId(maxSeq);
		
		//save and update session
		TaskListSession resSession = service.getTaskListSessionBySessionId(sessionId);
		if(resSession == null){
			log.error("Failed update TaskListSession by ID[" + sessionId + "]");
			return  mapping.findForward(TaskListConstants.ERROR);
		}
		Set<TaskListItem> items = resSession.getTaskListItems();
		if(items == null){
			items = new HashSet<TaskListItem>();
			resSession.setTaskListItems(items);
		}
		items.add(item);
		service.saveOrUpdateTaskListSession(resSession);
		
		//update session value
		SortedSet<TaskListItem> taskListItemList = getTaskListItemList(sessionMap);
		taskListItemList.add(item);
		
		//URL or file upload
		request.setAttribute(AttributeNames.ATTR_MODE,mode);
		itemForm.reset(mapping, request);		
		return  mapping.findForward(TaskListConstants.SUCCESS);
	}
	
	/**
	 * Display empty reflection form.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,	HttpServletResponse response) {
		
		//get session value
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);

		ReflectionForm refForm = (ReflectionForm) form;
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		refForm.setUserID(user.getUserID());
		refForm.setSessionMapID(sessionMapID);
		
//		 get the existing reflection entry
		ITaskListService submitFilesService = getTaskListService();
		
		SessionMap map = (SessionMap)request.getSession().getAttribute(sessionMapID);
		Long toolSessionID = (Long)map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL, TaskListConstants.TOOL_SIGNATURE, user.getUserID());
		
		if (entry != null) {
			refForm.setEntryText(entry.getEntry());		
		}
		
		return mapping.findForward(TaskListConstants.SUCCESS);
	}
	/**
	 * Submit reflection form input database.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		ReflectionForm refForm = (ReflectionForm) form;
		Integer userId = refForm.getUserID();
		
		String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		ITaskListService service = getTaskListService();

		// check for existing notebook entry
		NotebookEntry entry = service.getEntry(sessionId,
				CoreNotebookConstants.NOTEBOOK_TOOL,
				TaskListConstants.TOOL_SIGNATURE, userId);

		if (entry == null) {
			// create new entry
			service.createNotebookEntry(sessionId,
					CoreNotebookConstants.NOTEBOOK_TOOL,
					TaskListConstants.TOOL_SIGNATURE, userId, refForm
							.getEntryText());
		} else {
			// update existing entry
			entry.setEntry(refForm.getEntryText());
			entry.setLastModified(new Date());
			service.updateEntry(entry);
		}

		return finish(mapping, form, request, response);
	}
	

	//*************************************************************************************
	// Private method 
	//*************************************************************************************
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
		SortedSet<TaskListItem> list = (SortedSet<TaskListItem>) sessionMap.get(TaskListConstants.ATTR_RESOURCE_ITEM_LIST);
		if(list == null){
			list = new TreeSet<TaskListItem>(new TaskListItemComparator());
			sessionMap.put(TaskListConstants.ATTR_RESOURCE_ITEM_LIST,list);
		}
		return list;
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

//	/**
//	 * Return <code>ActionForward</code> according to taskList item type.
//	 * @param type
//	 * @param mapping
//	 * @return
//	 */
//	private ActionForward findForward(short type, ActionMapping mapping) {
//		ActionForward forward;
//		switch (type) {
//		case TaskListConstants.RESOURCE_TYPE_URL:
//			forward = mapping.findForward("url");
//			break;
//		case TaskListConstants.RESOURCE_TYPE_FILE:
//			forward = mapping.findForward("file");
//			break;
//		case TaskListConstants.RESOURCE_TYPE_WEBSITE:
//			forward = mapping.findForward("website");
//			break;
//		case TaskListConstants.RESOURCE_TYPE_LEARNING_OBJECT:
//			forward = mapping.findForward("learningobject");
//			break;
//		default:
//			forward = null;
//			break;
//		}
//		return forward;
//	}

	private TaskListUser getCurrentUser(ITaskListService service, Long sessionId) {
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		TaskListUser taskListUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),sessionId);
		
		if(taskListUser == null){
			TaskListSession session = service.getTaskListSessionBySessionId(sessionId);
			taskListUser = new TaskListUser(user,session);
			service.createUser(taskListUser);
		}
		return taskListUser;
	}
	private TaskListUser getSpecifiedUser(ITaskListService service, Long sessionId, Integer userId) {
		TaskListUser taskListUser = service.getUserByIDAndSession(new Long(userId.intValue()),sessionId);
		if ( taskListUser == null ) {
			log.error("Unable to find specified user for taskList activity. Screens are likely to fail. SessionId="
					+sessionId+" UserId="+userId);
		}
		return taskListUser;
	}

	/**
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
	 * Set complete flag for given taskList item.
	 * @param request
	 * @param sessionId 
	 */
	private void doComplete(HttpServletRequest request) {
		//get back sessionMap
		String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		Long taskListItemUid = new Long(request.getParameter(TaskListConstants.PARAM_RESOURCE_ITEM_UID));
		ITaskListService service = getTaskListService();
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		Long sessionId =  (Long) sessionMap.get(TaskListConstants.ATTR_TOOL_SESSION_ID);
		service.setItemComplete(taskListItemUid,new Long(user.getUserID().intValue()),sessionId);
		
		//set taskList item complete tag
		SortedSet<TaskListItem> taskListItemList = getTaskListItemList(sessionMap);
		for(TaskListItem item:taskListItemList){
			if(item.getUid().equals(taskListItemUid)){
				item.setComplete(true);
				break;
			}
		}
	}

}
