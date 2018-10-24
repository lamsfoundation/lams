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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.taskList.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.util.TaskListConditionComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListConditionForm;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
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
 * Auxiliary action in author mode. It contains operations with TaskListCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.web.controller.AuthoringController
 */

@Controller
@RequestMapping("/authoringCondition")
public class AuthoringTaskListConditionController {

    @Autowired
    @Qualifier("lataskMessageService")
    private MessageService messageService;

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    @RequestMapping("/showConditions")
    public String showConditions(@ModelAttribute TaskListForm taskListForm, HttpServletRequest request)
	    throws ServletException {

	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	TaskListForm existForm = (TaskListForm) sessionMap.get(TaskListConstants.ATTR_TASKLIST_FORM);

	try {
	    PropertyUtils.copyProperties(taskListForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	return "pages/authoring/conditions";
    }

    /**
     * Display empty page for new taskList item.
     */
    @RequestMapping("/newConditionInit")
    public String newConditionInit(@ModelAttribute TaskListConditionForm taskListConditionForm,
	    HttpServletRequest request) {

	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	taskListConditionForm.setSessionMapID(sessionMapID);
	populateFormWithPossibleItems(taskListConditionForm, request);
	return "pages/authoring/parts/addcondition";
    }

    /**
     * Display edit page for existed taskList item.
     */
    @RequestMapping(path = "/editCondition", method = RequestMethod.POST)
    public String editCondition(@ModelAttribute TaskListConditionForm taskListConditionForm,
	    HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_SEQUENCE_ID), -1);
	TaskListCondition item = null;
	if (sequenceId != -1) {
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    List<TaskListCondition> rList = new ArrayList<>(conditionList);
	    item = rList.get(sequenceId);
	    if (item != null) {
		populateConditionToForm(sequenceId, item, taskListConditionForm, request);
	    }
	}

	populateFormWithPossibleItems(taskListConditionForm, request);
	return item == null ? null : "pages/authoring/parts/addcondition";
    }

    /**
     * This method will get necessary information from taskList item form and save or update into
     * <code>HttpSession</code> TaskListItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @RequestMapping(path = "/saveOrUpdateCondition")
    public String saveOrUpdateCondition(@ModelAttribute TaskListConditionForm taskListConditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateTaskListCondition(taskListConditionForm, request);

	if (!errorMap.isEmpty()) {
	    populateFormWithPossibleItems(taskListConditionForm, request);
	    request.setAttribute("errorMap", errorMap);
	    return "pages/authoring/parts/addcondition";
	}

	try {
	    extractFormToTaskListCondition(request, taskListConditionForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errorMap.add("GLOBAL", messageService.getMessage("error.upload.faile"));
	    if (!errorMap.isEmpty()) {
		populateFormWithPossibleItems(taskListConditionForm, request);
		request.setAttribute("errorMap", errorMap);
		return "pages/authoring/parts/addcondition";
	    }

	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, taskListConditionForm.getSessionMapID());
	// return null to close this window
	return "pages/authoring/parts/conditionlist";
    }

    /**
     * Remove taskList item from HttpSession list and update page display. As authoring rule, all persist only happen
     * when user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping(path = "/removeCondition", method = RequestMethod.POST)
    public String removeCondition(@ModelAttribute TaskListConditionForm taskListConditionForm,
	    HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_SEQUENCE_ID), -1);
	if (sequenceId != -1) {
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    List<TaskListCondition> rList = new ArrayList<>(conditionList);
	    TaskListCondition condition = rList.remove(sequenceId);
	    for (TaskListCondition otherCondition : conditionList) {
		if (otherCondition.getSequenceId() > sequenceId) {
		    otherCondition.setSequenceId(otherCondition.getSequenceId() - 1);
		}
	    }
	    conditionList.clear();
	    conditionList.addAll(rList);
	    // add to delList
	    List delList = getDeletedTaskListConditionList(sessionMap);
	    delList.add(condition);
	}

	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/conditionlist";
    }

    /**
     * Move up current item.
     */
    @RequestMapping("/upCondition")
    public String upCondition(HttpServletRequest request) {
	return switchItem(request, true);
    }

    /**
     * Move down current item.
     */
    @RequestMapping("/downCondition")
    public String downCondition(HttpServletRequest request) {
	return switchItem(request, false);
    }

    public String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_SEQUENCE_ID), -1);
	if (sequenceId != -1) {
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    List<TaskListCondition> rList = new ArrayList<>(conditionList);
	    // get current and the target item, and switch their sequnece
	    TaskListCondition condition = rList.get(sequenceId);
	    TaskListCondition repCondition;
	    if (up) {
		repCondition = rList.get(--sequenceId);
	    } else {
		repCondition = rList.get(++sequenceId);
	    }
	    int upSeqId = repCondition.getSequenceId();
	    repCondition.setSequenceId(condition.getSequenceId());
	    condition.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    conditionList.clear();
	    conditionList.addAll(rList);
	}

	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/conditionlist";
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************

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
     * List save deleted taskList items, which could be persisted or non-persisted items.
     */
    private List getDeletedTaskListConditionList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, TaskListConstants.ATTR_DELETED_CONDITION_LIST);
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
    private void populateConditionToForm(int sequenceId, TaskListCondition condition,
	    TaskListConditionForm taskListConditionForm, HttpServletRequest request) {
	if (sequenceId >= 0) {
	    taskListConditionForm.setSequenceId(new Integer(sequenceId).toString());
	}
	taskListConditionForm.setName(condition.getName());

	Set<TaskListItem> itemList = condition.getTaskListItems();
	String[] selectedItems = new String[itemList.size()];
	int i = 0;
	for (TaskListItem item : itemList) {
	    selectedItems[i++] = (new Integer(item.getSequenceId())).toString();
	}
	taskListConditionForm.setSelectedItems(selectedItems);
    }

    /**
     * This method will populate taskList item information to its form for edit use.
     */
    private void populateFormWithPossibleItems(TaskListConditionForm taskListConditionForm,
	    HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	SortedSet<TaskListItem> itemList = getTaskListItemList(sessionMap);

	// Initialise the LabelValueBeans in the possibleOptions array.
	Map<String, String> possibleItems = new HashMap<>(itemList.size());

	for (TaskListItem item : itemList) {
	    possibleItems.put(String.valueOf(item.getSequenceId()), item.getTitle());
	}
	taskListConditionForm.setPossibleItems(possibleItems);

    }

    /**
     * Extract form content to taskListContent.
     */
    private void extractFormToTaskListCondition(HttpServletRequest request, TaskListConditionForm form)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to a old or new TaskListItem instance. It
	 * gets all info EXCEPT TaskListItem.createDate and TaskListItem.createBy, which need be set when persisting
	 * this taskList item.
	 */

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	int sequenceId = NumberUtils.stringToInt(form.getSequenceId(), -1);
	TaskListCondition condition = null;

	if (sequenceId == -1) { // add
	    condition = new TaskListCondition();
	    int maxSeq = 1;
	    if (conditionList != null && conditionList.size() > 0) {
		TaskListCondition last = conditionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    condition.setSequenceId(maxSeq);
	    conditionList.add(condition);
	} else { // edit
	    List<TaskListCondition> rList = new ArrayList<>(conditionList);
	    condition = rList.get(sequenceId);
	}

	condition.setName(form.getName());

	String[] selectedItems = form.getSelectedItems();
	SortedSet<TaskListItem> itemList = getTaskListItemList(sessionMap);
	SortedSet<TaskListItem> conditionItemList = new TreeSet<>(new TaskListItemComparator());

	for (String selectedItem : selectedItems) {
	    for (TaskListItem item : itemList) {
		if (selectedItem.equals((new Integer(item.getSequenceId())).toString())) {
		    conditionItemList.add(item);
		}
	    }
	}
	condition.setTaskListItems(conditionItemList);

    }

    /**
     * Validate taskListCondition
     */
    private MultiValueMap validateTaskListCondition(TaskListConditionForm taskListConditionForm,
	    HttpServletRequest request) {

	String formConditionName = taskListConditionForm.getName();
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	if (StringUtils.isBlank(formConditionName)) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.name.blank"));
	} else if (StringUtils.contains(formConditionName, '#')) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.name.contains.wrong.symbol"));
	} else {

	    String formConditionSequenceId = taskListConditionForm.getSequenceId();

	    String sessionMapID = taskListConditionForm.getSessionMapID();
	    SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		    .getAttribute(sessionMapID);
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    for (TaskListCondition condition : conditionList) {
		if (formConditionName.equals(condition.getName())
			&& !formConditionSequenceId.equals((new Integer(condition.getSequenceId() - 1)).toString())) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.condition.duplicated.name"));
		    ;
		    break;
		}
	    }
	}

	// should be selected at least one TaskListItem
	String[] selectedItems = taskListConditionForm.getSelectedItems();
	if (selectedItems == null || selectedItems.length == 0) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.no.tasklistitems.selected"));
	}
	return errorMap;
    }

}
