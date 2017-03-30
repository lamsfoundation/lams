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


package org.lamsfoundation.lams.tool.taskList.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.model.TaskListCondition;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.tool.taskList.service.TaskListException;
import org.lamsfoundation.lams.tool.taskList.util.TaskListConditionComparator;
import org.lamsfoundation.lams.tool.taskList.util.TaskListItemComparator;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListConditionForm;
import org.lamsfoundation.lams.tool.taskList.web.form.TaskListForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Auxiliary action in author mode. It contains operations with TaskListCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.taskList.web.action.AuthoringAction
 */
public class AuthoringTaskListConditionAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();

	if (param.equals("showConditions")) {
	    return showConditions(mapping, form, request, response);
	}
	if (param.equals("newConditionInit")) {
	    return newConditionInit(mapping, form, request, response);
	}
	if (param.equals("editCondition")) {
	    return editCondition(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateCondition")) {
	    return saveOrUpdateCondition(mapping, form, request, response);
	}
	if (param.equals("removeCondition")) {
	    return removeCondition(mapping, form, request, response);
	}
	if (param.equals("upCondition")) {
	    return upCondition(mapping, form, request, response);
	}
	if (param.equals("downCondition")) {
	    return downCondition(mapping, form, request, response);
	}

	return mapping.findForward(TaskListConstants.ERROR);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward showConditions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	TaskListForm existForm = (TaskListForm) sessionMap.get(TaskListConstants.ATTR_TASKLIST_FORM);

	TaskListForm taskListForm = (TaskListForm) form;
	try {
	    PropertyUtils.copyProperties(taskListForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	if (mode.isAuthor()) {
	    return mapping.findForward(TaskListConstants.SUCCESS);
	} else {
	    return mapping.findForward(TaskListConstants.DEFINE_LATER);
	}
    }

    /**
     * Display empty page for new taskList item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newConditionInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	((TaskListConditionForm) form).setSessionMapID(sessionMapID);

	populateFormWithPossibleItems(form, request);
	return mapping.findForward("addcondition");
    }

    /**
     * Display edit page for existed taskList item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_SEQUENCE_ID), -1);
	TaskListCondition item = null;
	if (sequenceId != -1) {
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    List<TaskListCondition> rList = new ArrayList<TaskListCondition>(conditionList);
	    item = rList.get(sequenceId);
	    if (item != null) {
		populateConditionToForm(sequenceId, item, (TaskListConditionForm) form, request);
	    }
	}

	populateFormWithPossibleItems(form, request);
	return item == null ? null : mapping.findForward("addcondition");
    }

    /**
     * This method will get necessary information from taskList item form and save or update into
     * <code>HttpSession</code> TaskListItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveOrUpdateCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	TaskListConditionForm conditionForm = (TaskListConditionForm) form;
	ActionErrors errors = validateTaskListCondition(conditionForm, request);

	if (!errors.isEmpty()) {
	    populateFormWithPossibleItems(form, request);
	    this.addErrors(request, errors);
	    return mapping.findForward("addcondition");
	}

	try {
	    extractFormToTaskListCondition(request, conditionForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(TaskListConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	    if (!errors.isEmpty()) {
		populateFormWithPossibleItems(form, request);
		this.addErrors(request, errors);
		return mapping.findForward("addcondition");
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, conditionForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    /**
     * Remove taskList item from HttpSession list and update page display. As authoring rule, all persist only happen
     * when user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_SEQUENCE_ID), -1);
	if (sequenceId != -1) {
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    List<TaskListCondition> rList = new ArrayList<TaskListCondition>(conditionList);
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
    private ActionForward upCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
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
    private ActionForward downCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(mapping, request, false);
    }

    private ActionForward switchItem(ActionMapping mapping, HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int sequenceId = NumberUtils.stringToInt(request.getParameter(TaskListConstants.PARAM_SEQUENCE_ID), -1);
	if (sequenceId != -1) {
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    List<TaskListCondition> rList = new ArrayList<TaskListCondition>(conditionList);
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
	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************
    /**
     * Return TaskListService bean.
     */
    private ITaskListService getTaskListService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ITaskListService) wac.getBean(TaskListConstants.TASKLIST_SERVICE);
    }

    /**
     * List save current taskList items.
     *
     * @param request
     * @return
     */
    private SortedSet<TaskListCondition> getTaskListConditionList(SessionMap sessionMap) {
	SortedSet<TaskListCondition> list = (SortedSet<TaskListCondition>) sessionMap
		.get(TaskListConstants.ATTR_CONDITION_LIST);
	if (list == null) {
	    list = new TreeSet<TaskListCondition>(new TaskListConditionComparator());
	    sessionMap.put(TaskListConstants.ATTR_CONDITION_LIST, list);
	}
	return list;
    }

    /**
     * List save current taskList items.
     *
     * @param request
     * @return
     */
    private SortedSet<TaskListItem> getTaskListItemList(SessionMap sessionMap) {
	SortedSet<TaskListItem> list = (SortedSet<TaskListItem>) sessionMap
		.get(TaskListConstants.ATTR_TASKLIST_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<TaskListItem>(new TaskListItemComparator());
	    sessionMap.put(TaskListConstants.ATTR_TASKLIST_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedTaskListConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, TaskListConstants.ATTR_DELETED_CONDITION_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * This method will populate taskList item information to its form for edit use.
     *
     * @param sequenceId
     * @param condition
     * @param form
     * @param request
     */
    private void populateConditionToForm(int sequenceId, TaskListCondition condition, TaskListConditionForm form,
	    HttpServletRequest request) {
	if (sequenceId >= 0) {
	    form.setSequenceId(new Integer(sequenceId).toString());
	}
	form.setName(condition.getName());

	Set<TaskListItem> itemList = condition.getTaskListItems();
	String[] selectedItems = new String[itemList.size()];
	int i = 0;
	for (TaskListItem item : itemList) {
	    selectedItems[i++] = (new Integer(item.getSequenceId())).toString();
	}
	form.setSelectedItems(selectedItems);
    }

    /**
     * This method will populate taskList item information to its form for edit use.
     *
     * @param sequenceId
     * @param condition
     * @param form
     * @param request
     */
    private void populateFormWithPossibleItems(ActionForm form, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	SortedSet<TaskListItem> itemList = getTaskListItemList(sessionMap);

	// Initialise the LabelValueBeans in the possibleOptions array.
	LabelValueBean[] lvBeans = new LabelValueBean[itemList.size()];

	int i = 0;
	for (TaskListItem item : itemList) {
	    lvBeans[i++] = new LabelValueBean(item.getTitle(), (new Integer(item.getSequenceId())).toString());
	}
	((TaskListConditionForm) form).setPossibleItems(lvBeans);
    }

    /**
     * Extract form content to taskListContent.
     *
     * @param request
     * @param form
     * @throws TaskListException
     */
    private void extractFormToTaskListCondition(HttpServletRequest request, TaskListConditionForm form)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to a old or new TaskListItem instance. It
	 * gets all info EXCEPT TaskListItem.createDate and TaskListItem.createBy, which need be set when persisting
	 * this taskList item.
	 */

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
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
	    List<TaskListCondition> rList = new ArrayList<TaskListCondition>(conditionList);
	    condition = rList.get(sequenceId);
	}

	condition.setName(form.getName());

	String[] selectedItems = form.getSelectedItems();
	SortedSet<TaskListItem> itemList = getTaskListItemList(sessionMap);
	SortedSet<TaskListItem> conditionItemList = new TreeSet<TaskListItem>(new TaskListItemComparator());

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
     *
     * @param conditionForm
     * @return
     */
    private ActionErrors validateTaskListCondition(TaskListConditionForm conditionForm, HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();

	String formConditionName = conditionForm.getName();
	if (StringUtils.isBlank(formConditionName)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(TaskListConstants.ERROR_MSG_NAME_BLANK));
	} else if (StringUtils.contains(formConditionName, '#')) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(TaskListConstants.ERROR_MSG_NAME_CONTAINS_WRONG_SYMBOL));
	} else {

	    String formConditionSequenceId = conditionForm.getSequenceId();

	    String sessionMapID = conditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<TaskListCondition> conditionList = getTaskListConditionList(sessionMap);
	    for (TaskListCondition condition : conditionList) {
		if (formConditionName.equals(condition.getName())
			&& !formConditionSequenceId.equals((new Integer(condition.getSequenceId() - 1)).toString())) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(TaskListConstants.ERROR_MSG_NAME_DUPLICATED));
		    break;
		}
	    }
	}

	// should be selected at least one TaskListItem
	String[] selectedItems = conditionForm.getSelectedItems();
	if (selectedItems == null || selectedItems.length == 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(TaskListConstants.ERROR_MSG_NO_TASK_LIST_ITEMS));
	}

	return errors;
    }

    private ActionMessages validate(TaskListForm taskListForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	// if (StringUtils.isBlank(taskListForm.getTaskList().getTitle())) {
	// ActionMessage error = new ActionMessage("error.resource.item.title.blank");
	// errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	// }

	// define it later mode(TEACHER) skip below validation.
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return errors;
	}

	// Some other validation outside basic Tab.

	return errors;
    }

}
