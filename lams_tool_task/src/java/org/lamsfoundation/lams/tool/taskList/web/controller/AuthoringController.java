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

package org.lamsfoundation.lams.tool.taskList.web.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.util.TaskListConditionComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListForm;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListItemForm;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListPedagogicalPlannerForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The main action in author mode. All the authoring operations are located in
 * here. Except for operations dealing with TaskListCondition that are located
 * in <code>AuthoringTaskListConditionAction</code> action.
 *
 * @author Steve.Ni
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.web.controller.AuthoringTaskListConditionController
 */

@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    @Qualifier("lataskTaskListService")
    private ITaskListService taskListService;

    @Autowired
    @Qualifier("lataskMessageService")
    private MessageService messageService;

    /**
     * Read taskList data from database and put them into HttpSession. It will
     * redirect to init.do directly after this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item
     * lost when user "refresh page",
     */
    @RequestMapping("/start")
    public String start(@ModelAttribute TaskListForm taskListForm, HttpServletRequest request) throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return readDatabaseData(taskListForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String defineLater(@ModelAttribute TaskListForm taskListForm, HttpServletRequest request)
	    throws ServletException {
	// update define later flag to true
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	TaskList taskList = taskListService.getTaskListByContentId(contentId);

	taskList.setDefineLater(true);
	taskListService.saveOrUpdateTaskList(taskList);

	// audit log the teacher has started editing activity in monitor
	taskListService.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return readDatabaseData(taskListForm, request);
    }
    
    /**
     * Common method for "start" and "defineLater"
     */
    private String readDatabaseData(TaskListForm taskListForm, HttpServletRequest request) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = WebUtil.readLongParam(request, TaskListConstants.PARAM_TOOL_CONTENT_ID);

	List<TaskListItem> items = null;
	TaskList taskList = null;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	taskListForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	taskListForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    taskList = taskListService.getTaskListByContentId(contentId);
	    // if taskList does not exist, try to use default content instead.
	    if (taskList == null) {
		taskList = taskListService.getDefaultContent(contentId);
		if (taskList.getTaskListItems() != null) {
		    items = new ArrayList<TaskListItem>(taskList.getTaskListItems());
		} else {
		    items = null;
		}
	    } else {
		items = taskListService.getAuthoredItems(taskList.getUid());
	    }

	    taskListForm.setTaskList(taskList);
	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	// initialize conditions list
	SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	conditionList.clear();
	conditionList.addAll(taskList.getConditions());

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<>();
	} else {
	    TaskListUser taskListUser = null;
	    // handle system default question: createBy is null, now set it to current user
	    for (TaskListItem item : items) {
		if (item.getCreateBy() == null) {
		    if (taskListUser == null) {
			// get back login user DTO
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			taskListUser = new TaskListUser(user, taskList);
		    }
		    item.setCreateBy(taskListUser);
		}
	    }
	}
	// init taskList item list
	SortedSet<TaskListItem> taskListItemList = getTaskListItemList(sessionMap);
	taskListItemList.clear();
	taskListItemList.addAll(items);

	sessionMap.put(TaskListConstants.ATTR_TASKLIST_FORM, taskListForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	request.setAttribute("startForm", taskListForm);
	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    @RequestMapping(path = "/init", method = RequestMethod.POST)
    public String initPage(@ModelAttribute TaskListForm startForm, HttpServletRequest request) throws ServletException {

	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	TaskListForm existForm = (TaskListForm) sessionMap.get(TaskListConstants.ATTR_TASKLIST_FORM);

	try {
	    PropertyUtils.copyProperties(startForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	request.setAttribute("taskListForm", startForm);
	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all
     * taskList item, information etc.
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute TaskListForm taskListForm, HttpServletRequest request)
	    throws Exception {

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(taskListForm.getSessionMapID());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	TaskList taskList = taskListForm.getTaskList();

	// **********************************Get TaskList PO*********************
	TaskList taskListPO = taskListService.getTaskListByContentId(taskListForm.getTaskList().getContentId());
	if (taskListPO == null) {
	    // new TaskList, create it
	    taskListPO = taskList;
	    taskListPO.setCreated(new Timestamp(new Date().getTime()));
	    taskListPO.setUpdated(new Timestamp(new Date().getTime()));

	} else {
	    Long uid = taskListPO.getUid();
	    PropertyUtils.copyProperties(taskListPO, taskList);
	    // get back UID
	    taskListPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		taskListPO.setDefineLater(false);
	    }

	    taskListPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TaskListUser taskListUser = taskListService.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		taskListForm.getTaskList().getContentId());
	if (taskListUser == null) {
	    taskListUser = new TaskListUser(user, taskListPO);
	}

	taskListPO.setCreatedBy(taskListUser);

	// ************************* Handle taskList items *******************
	Set itemList = new LinkedHashSet();
	SortedSet<TaskListItem> items = getTaskListItemList(sessionMap);
	for (TaskListItem item : items) {
	    if (item != null) {
		if (item.getUid() == null) {
		    // This flushs user UID info to message if this user is a new user.
		    item.setCreateBy(taskListUser);
		    itemList.add(item);
		} else {
		    // Do not update if it is not null otherwise Edit Activity will overwrite the
		    // learner id for learner entered tasks
		    // But taskListUser & .getCreatedBy() are a stale lazy loaded object so need to
		    // the real things from the session
		    TaskListItem itemPO = taskListService.getTaskListItemByUid(item.getUid());
		    if (itemPO != null) {
			updateTaskListItemFromSession(itemPO, item);
			itemList.add(itemPO);
		    } else {
			// something weird happened. Uid exists but can't find the task. Make it an
			// authored task
			item.setCreateBy(taskListUser);
			item.setCreateByAuthor(true);
			itemList.add(item);
		    }
		}
	    }
	}
	taskListPO.setTaskListItems(itemList);

	// ************************* Handle taskList conditions *******************
	SortedSet<TaskListCondition> conditions = getTaskListConditionList(sessionMap);
	SortedSet<TaskListCondition> conditionListWithoutEmptyElements = new TreeSet<>(conditions);
	List delConditions = getDeletedTaskListConditionList(sessionMap);

	for (TaskListCondition condition : conditions) {
	    if (condition.getTaskListItems().size() == 0) {
		conditionListWithoutEmptyElements.remove(condition);
		delConditions.add(condition);

		// reorder remaining conditions
		for (TaskListCondition otherCondition : conditionListWithoutEmptyElements) {
		    if (otherCondition.getSequenceId() > condition.getSequenceId()) {
			otherCondition.setSequenceId(otherCondition.getSequenceId() - 1);
		    }
		}
	    }
	}
	conditions.clear();
	conditions.addAll(conditionListWithoutEmptyElements);
	taskListPO.setConditions(conditions);

	// delete TaskListConditions from database.
	Iterator iter = delConditions.iterator();
	while (iter.hasNext()) {
	    TaskListCondition condition = (TaskListCondition) iter.next();
	    iter.remove();

	    if (condition.getUid() != null) {
		taskListService.deleteTaskListCondition(condition.getUid());
	    }
	}

	// delete TaskListItems from database. This should be done after
	// TaskListConditions have been deleted from the database. This is due
	// to prevent errors with foreign keys.
	List delTaskListItems = getDeletedTaskListItemList(sessionMap);
	iter = delTaskListItems.iterator();
	while (iter.hasNext()) {
	    TaskListItem item = (TaskListItem) iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		taskListService.deleteTaskListItem(item.getUid());
	    }
	}

	// if MinimumNumberTasksComplete is bigger than available items, then set it
	// topics size
	if (taskListPO.getMinimumNumberTasks() > items.size()) {
	    taskListPO.setMinimumNumberTasks(items.size());
	}

	// **********************************************
	// finally persist taskListPO again
	taskListService.saveOrUpdateTaskList(taskListPO);

	taskListService.getTaskListByContentId(taskListPO.getContentId());

	taskListForm.setTaskList(taskListPO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return "pages/authoring/authoring";
    }

    // **********************************************************
    // Add taskListItem methods
    // **********************************************************

    /**
     * Display empty page for new taskList item.
     */
    @RequestMapping("/newItemInit")
    public String newItemlInit(@ModelAttribute TaskListItemForm taskListItemForm, HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	taskListItemForm.setSessionMapID(sessionMapID);

	return "pages/authoring/parts/addtask";
    }

    /**
     * Display edit page for existed taskList item.
     */
    @RequestMapping("editItemInit")
    public String editItemInit(@ModelAttribute TaskListItemForm taskListItemForm, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX), -1);
	TaskListItem item = null;
	if (itemIdx != -1) {
	    SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
	    List<TaskListItem> rList = new ArrayList<>(taskListList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, taskListItemForm, request);
	    }
	}

	return item == null ? null : "pages/authoring/parts/addtask";
    }

    /**
     * This method will get necessary information from taskList item form and save
     * or update into <code>HttpSession</code> TaskListItemList. Notice, this save
     * is not persist them into database, just save <code>HttpSession</code>
     * temporarily. Only they will be persist when the entire authoring page is
     * being persisted.
     */
    @RequestMapping(path = "/saveOrUpdateItem", method = RequestMethod.POST)
    public String saveOrUpdateItem(@ModelAttribute TaskListItemForm taskListItemForm, HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateTaskListItem(taskListItemForm);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/authoring/parts/addtask";
	}

	try {
	    extractFormToTaskListItem(request, taskListItemForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw
	    // exception directly
	    errorMap.add("GLOBAL", messageService.getMessage(e.getMessage()));
	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
		return "pages/authoring/parts/addtask";
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, taskListItemForm.getSessionMapID());
	// return null to close this window
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Remove taskList item from HttpSession list and update page display. As
     * authoring rule, all persist only happen when user submit whole page. So this
     * remove is just impact HttpSession values.
     */
    @RequestMapping("/removeItem")
    public String removeItem(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
	    List<TaskListItem> rList = new ArrayList<>(taskListList);
	    TaskListItem item = rList.remove(itemIdx);
	    taskListList.clear();
	    taskListList.addAll(rList);
	    // add to delList
	    List delList = getDeletedTaskListItemList(sessionMap);
	    delList.add(item);

	    // delete tasklistitems that still may be contained in Conditions
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    for (TaskListCondition condition : conditionList) {
		Set<TaskListItem> itemList = condition.getTaskListItems();
		if (itemList.contains(item)) {
		    itemList.remove(item);
		}
	    }

	}

	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Move up current item.
     */
    @RequestMapping("/upItem")
    public String upItem(HttpServletRequest request) {
	return switchItem(request, true);
    }

    /**
     * Move down current item.
     */
    @RequestMapping("/downItem")
    public String downItem(HttpServletRequest request) {
	return switchItem(request, false);
    }

    private String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
	    List<TaskListItem> rList = new ArrayList<>(taskListList);
	    // get current and the target item, and switch their sequnece
	    TaskListItem item = rList.get(itemIdx);
	    TaskListItem repItem;
	    if (up) {
		repItem = rList.get(--itemIdx);
	    } else {
		repItem = rList.get(++itemIdx);
	    }
	    int upSeqId = repItem.getSequenceId();
	    repItem.setSequenceId(item.getSequenceId());
	    item.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    taskListList.clear();
	    taskListList.addAll(rList);
	}

	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************

    /**
     * List save current taskList items.
     */
    private SortedSet<TaskListItem> getTaskListItemList(SessionMap<String, Object> sessionMap) {
	SortedSet<TaskListItem> list = (SortedSet<TaskListItem>) sessionMap
		.get(TaskListConstants.ATTR_TASKLIST_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<>(new TaskListItemComparator());
	    sessionMap.put(TaskListConstants.ATTR_TASKLIST_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * List save current taskList items.
     */
    private SortedSet<TaskListCondition> getTaskListConditionList(SessionMap<String, Object> sessionMap) {
	SortedSet<TaskListCondition> list = (SortedSet<TaskListCondition>) sessionMap
		.get(TaskListConstants.ATTR_CONDITION_LIST);
	if (list == null) {
	    list = new TreeSet<>(new TaskListConditionComparator());
	    sessionMap.put(TaskListConstants.ATTR_CONDITION_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted
     * items.
     */
    private List getDeletedTaskListConditionList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, TaskListConstants.ATTR_DELETED_CONDITION_LIST);
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted
     * items.
     */
    private List getDeletedTaskListItemList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, TaskListConstants.ATTR_DELETED_TASKLIST_ITEM_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     */
    private List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * This method will populate taskList item information to its form for edit use.
     */
    private void populateItemToForm(int itemIdx, TaskListItem item, TaskListItemForm taskListItemForm,
	    HttpServletRequest request) {
	taskListItemForm.setDescription(item.getDescription());
	taskListItemForm.setTitle(item.getTitle());
	if (itemIdx >= 0) {
	    taskListItemForm.setItemIndex(new Integer(itemIdx).toString());
	}
	taskListItemForm.setRequired(item.isRequired());
	taskListItemForm.setCommentsAllowed(item.isCommentsAllowed());
	taskListItemForm.setCommentsRequired(item.isCommentsRequired());
	taskListItemForm.setFilesAllowed(item.isFilesAllowed());
	taskListItemForm.setFilesRequired(item.isFilesRequired());
	taskListItemForm.setChildTask(item.isChildTask());
	taskListItemForm.setParentTaskName(item.getParentTaskName());
    }

    /**
     * Extract web from content to taskList item.
     */
    private void extractFormToTaskListItem(HttpServletRequest request, TaskListItemForm itemForm) throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to a old
	 * or new TaskListItem instance. It gets all info EXCEPT TaskListItem.createDate
	 * and TaskListItem.createBy, which need be set when persisting this taskList
	 * item.
	 */

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(itemForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<TaskListItem> taskListList = getTaskListItemList(sessionMap);
	int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(), -1);
	TaskListItem item = null;

	if (itemIdx == -1) { // add
	    item = new TaskListItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (taskListList != null && taskListList.size() > 0) {
		TaskListItem last = taskListList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    item.setSequenceId(maxSeq);
	    taskListList.add(item);
	} else { // edit
	    List<TaskListItem> rList = new ArrayList<>(taskListList);
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
	item.setChildTask(itemForm.isChildTask());
	item.setParentTaskName(itemForm.getParentTaskName());
    }

    /**
     * Extract session request version of a taskListItem update the DB version of
     * the taskListItem.
     */
    private void updateTaskListItemFromSession(TaskListItem itemPO, TaskListItem itemFromSession) throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from session to an existing
	 * TaskListItem instance. It gets all info EXCEPT uid, createDate, createBy,
	 * createByAuthor, attachments and comments which need be left the same as they
	 * were previously. If you change them then LiveEdit will change Learner created
	 * items.
	 */

	itemPO.setTitle(itemFromSession.getTitle());
	itemPO.setDescription(itemFromSession.getDescription());
	itemPO.setSequenceId(itemFromSession.getSequenceId());
	itemPO.setRequired(itemFromSession.isRequired());
	itemPO.setCommentsAllowed(itemFromSession.isCommentsAllowed());
	itemPO.setCommentsRequired(itemFromSession.isCommentsRequired());
	itemPO.setFilesAllowed(itemFromSession.isFilesAllowed());
	itemPO.setFilesRequired(itemFromSession.isFilesRequired());
	itemPO.setChildTask(itemFromSession.isChildTask());
	itemPO.setParentTaskName(itemFromSession.getParentTaskName());
    }

    /**
     * Vaidate taskList item regards to their type (url/file/learning object/website
     * zip file)
     */
    private MultiValueMap validateTaskListItem(TaskListItemForm itemForm) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	if (StringUtils.isBlank(itemForm.getTitle())) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.resource.item.title.blank"));
	}
	return errorMap;
    }

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(@ModelAttribute TaskListPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	TaskList taskList = taskListService.getTaskListByContentId(toolContentID);
	plannerForm.fillForm(taskList);
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping(path = "/saveOrUpdatePedagogicalPlannerForm", method = RequestMethod.POST)
    public String saveOrUpdatePedagogicalPlannerForm(@ModelAttribute TaskListPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) throws IOException {

	MultiValueMap<String, String> errorMap = plannerForm.validate(messageService);

	if (errorMap.isEmpty()) {
	    TaskList taskList = taskListService.getTaskListByContentId(plannerForm.getToolContentID());

	    int itemIndex = 0;
	    String item = null;
	    TaskListItem taskListItem = null;
	    List<TaskListItem> newItems = new LinkedList<>();
	    Iterator<TaskListItem> taskListTopicIterator = taskList.getTaskListItems().iterator();
	    do {
		item = plannerForm.getTaskListItem(itemIndex);
		if (StringUtils.isEmpty(item)) {
		    plannerForm.removeTaskListItem(itemIndex);
		} else {
		    if (taskListTopicIterator.hasNext()) {
			taskListItem = taskListTopicIterator.next();
			taskListItem.setTitle(item);
		    } else {
			taskListItem = new TaskListItem();
			taskListItem.setCreateByAuthor(true);
			Date currentDate = new Date();
			taskListItem.setCreateDate(currentDate);

			HttpSession session = SessionManager.getSession();
			UserDTO user = (UserDTO) session.getAttribute(AttributeNames.USER);
			TaskListUser taskListUser = taskListService.getUserByIDAndContent(
				new Long(user.getUserID().intValue()), plannerForm.getToolContentID());
			taskListItem.setCreateBy(taskListUser);

			taskListItem.setTitle(item);

			newItems.add(taskListItem);
		    }
		    itemIndex++;
		}

	    } while (item != null);
	    while (taskListTopicIterator.hasNext()) {
		taskListItem = taskListTopicIterator.next();
		taskListTopicIterator.remove();
		taskListService.deleteTaskListItem(taskListItem.getUid());
	    }
	    taskList.getTaskListItems().addAll(newItems);
	    taskListService.saveOrUpdateTaskList(taskList);
	} else {
	    request.setAttribute("errorMap", errorMap);
	}

	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping("/createPedagogicalPlannerItem")
    public String createPedagogicalPlannerItem(@ModelAttribute TaskListPedagogicalPlannerForm plannerForm,
	    HttpServletRequest request) {
	plannerForm.setTaskListItem(plannerForm.getTaskListItemCount().intValue(), "");
	return "pages/authoring/pedagogicalPlannerForm";
    }
}
