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

package org.lamsfoundation.lams.tool.notebook.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.tool.notebook.web.forms.NotebookConditionForm;
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
 * Auxiliary action in author mode. It contains operations with NotebookCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.notebook.web.action.AuthoringAction
 *
 */
@Controller
@RequestMapping("/authoringCondition")
public class AuthoringNotebookConditionController {

    @Autowired
    private INotebookService notebookService;

    @Autowired
    @Qualifier("notebookMessageService")
    private MessageService messageService;

    /**
     * Display empty page for new taskList item.
     *
     * @param notebookConditionForm
     * @param request
     * @return
     */
    @RequestMapping(value = "newConditionInit")
    private String newConditionInit(@ModelAttribute NotebookConditionForm notebookConditionForm,
	    HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, NotebookConstants.ATTR_SESSION_MAP_ID);
	notebookConditionForm.setSessionMapID(sessionMapID);
	notebookConditionForm.setOrderId(-1);
	request.setAttribute("notebookConditionForm", notebookConditionForm);
	return "pages/authoring/addCondition";
    }

    /**
     * Display edit page for existed taskList item.
     *
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/editCondition")
    private String editCondition(NotebookConditionForm notebookConditionForm, HttpServletRequest request) {

	String sessionMapID = notebookConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(NotebookConstants.PARAM_ORDER_ID), -1);
	NotebookCondition condition = null;
	if (orderId != -1) {
	    SortedSet<NotebookCondition> conditionSet = getNotebookConditionSet(sessionMap);
	    List<NotebookCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, notebookConditionForm, request);
	    }
	}
	return condition == null ? null : "pages/authoring/addCondition";
    }

    /**
     * This method will get necessary information from taskList item form and save or update into
     * <code>HttpSession</code> NotebookItemList. Notice, this save is not persist them into database, just save
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
	@RequestMapping(path = "/saveOrUpdateCondition", method = RequestMethod.POST)
    private String saveOrUpdateCondition(
	    @ModelAttribute("notebookConditionForm") NotebookConditionForm notebookConditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	validateNotebookCondition(notebookConditionForm, errorMap, request);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/authoring/addCondition";
	}

	try {
	    extractFormToNotebookCondition(request, notebookConditionForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errorMap.add("GLOBAL", messageService.getMessage(NotebookConstants.ERROR_MSG_CONDITION, new Object[] { e.getMessage() }));
	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
		return "pages/authoring/addCondition";
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(NotebookConstants.ATTR_SESSION_MAP_ID, notebookConditionForm.getSessionMapID());
	request.setAttribute("notebookConditionForm", notebookConditionForm);
	// return null to close this window
	return "pages/authoring/conditionList";
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
    @RequestMapping(path = "/removeCondition", method = RequestMethod.POST)
    private String removeCondition(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, NotebookConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(NotebookConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<NotebookCondition> conditionSet = getNotebookConditionSet(sessionMap);
	    List<NotebookCondition> conditionList = new ArrayList<>(conditionSet);
	    NotebookCondition condition = conditionList.remove(orderId);
	    for (NotebookCondition otherCondition : conditionSet) {
		if (otherCondition.getOrderId() > orderId) {
		    otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		}
	    }
	    conditionSet.clear();
	    conditionSet.addAll(conditionList);
	    // add to delList
	    List delList = getDeletedNotebookConditionList(sessionMap);
	    delList.add(condition);
	}

	request.setAttribute(NotebookConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/conditionList";
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
    @RequestMapping(value = "/upCondition")
    private String upCondition(HttpServletRequest request) {
	return switchItem(request, true);
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
    @RequestMapping(value = "/downCondition")
    private String downCondition(HttpServletRequest request) {
	return switchItem(request, false);
    }

    private String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, NotebookConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(NotebookConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<NotebookCondition> conditionSet = getNotebookConditionSet(sessionMap);
	    List<NotebookCondition> conditionList = new ArrayList<>(conditionSet);
	    // get current and the target item, and switch their sequnece
	    NotebookCondition condition = conditionList.get(orderId);
	    NotebookCondition repCondition;
	    if (up) {
		repCondition = conditionList.get(--orderId);
	    } else {
		repCondition = conditionList.get(++orderId);
	    }
	    int upSeqId = repCondition.getOrderId();
	    repCondition.setOrderId(condition.getOrderId());
	    condition.setOrderId(upSeqId);

	    // put back list, it will be sorted again
	    conditionSet.clear();
	    conditionSet.addAll(conditionList);
	}

	request.setAttribute(NotebookConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/conditionList";
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************

    /**
     * List save current taskList items.
     *
     * @param request
     * @return
     */
    private SortedSet<NotebookCondition> getNotebookConditionSet(SessionMap sessionMap) {
	SortedSet<NotebookCondition> set = (SortedSet<NotebookCondition>) sessionMap
		.get(NotebookConstants.ATTR_CONDITION_SET);
	if (set == null) {
	    set = new TreeSet<>(new TextSearchConditionComparator());
	    sessionMap.put(NotebookConstants.ATTR_CONDITION_SET, set);
	}
	return set;
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedNotebookConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, NotebookConstants.ATTR_DELETED_CONDITION_LIST);
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
     * @param orderId
     * @param condition
     * @param notebookConditionForm
     * @param request
     */
    private void populateConditionToForm(int orderId, NotebookCondition condition,
	    NotebookConditionForm notebookConditionForm, HttpServletRequest request) {
	notebookConditionForm.populateForm(condition);
	if (orderId >= 0) {
	    notebookConditionForm.setOrderId(orderId + 1);
	}
    }

    /**
     * Extract form content to taskListContent.
     *
     * @param request
     * @param notebookConditionForm
     * @throws NotebookException
     */
    private void extractFormToNotebookCondition(HttpServletRequest request, NotebookConditionForm notebookConditionForm)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to a old or new NotebookItem instance. It
	 * gets all info EXCEPT NotebookItem.createDate and NotebookItem.createBy, which need be set when persisting
	 * this taskList item.
	 */

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(notebookConditionForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<NotebookCondition> conditionSet = getNotebookConditionSet(sessionMap);
	int orderId = notebookConditionForm.getOrderId();
	NotebookCondition condition = null;

	if (orderId == -1) { // add
	    String properConditionName = notebookService.createConditionName(conditionSet);
	    condition = notebookConditionForm.extractCondition();
	    condition.setName(properConditionName);
	    int maxSeq = 1;
	    if (conditionSet != null && conditionSet.size() > 0) {
		NotebookCondition last = conditionSet.last();
		maxSeq = last.getOrderId() + 1;
	    }
	    condition.setOrderId(maxSeq);
	    conditionSet.add(condition);
	} else { // edit
	    List<NotebookCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    notebookConditionForm.extractCondition(condition);
	}
    }

    /**
     * Validate taskListCondition
     *
     * @param notebookConditionForm
     * @return
     */
    private void validateNotebookCondition(NotebookConditionForm notebookConditionForm,
	    MultiValueMap<String, String> errorMap, HttpServletRequest request) {

	String formConditionName = notebookConditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {
	    errorMap.add("GLOBAL", messageService.getMessage(NotebookConstants.ERROR_MSG_NAME_BLANK));
	} else {

	    Integer formConditionSequenceId = notebookConditionForm.getOrderId();

	    String sessionMapID = notebookConditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<NotebookCondition> conditionList = getNotebookConditionSet(sessionMap);
	    for (NotebookCondition condition : conditionList) {
		if (formConditionName.equals(condition.getName())
			&& !formConditionSequenceId.equals(new Integer(condition.getOrderId() - 1).toString())) {
		    errorMap.add("GLOBAL", messageService.getMessage(NotebookConstants.ERROR_MSG_NAME_DUPLICATED));
		    break;
		}
	    }
	}
    }

}
