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

package org.lamsfoundation.lams.tool.chat.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.web.forms.ChatConditionForm;
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

/**
 * Auxiliary action in author mode. It contains operations with ChatCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.chat.web.action.AuthoringAction
 *
 */
@Controller
@RequestMapping("/authoringCondition")
public class AuthoringChatConditionController {

    @Autowired
    private IChatService chatService;

    @Autowired
    @Qualifier("chatMessageService")
    private MessageService messageService;

    /**
     * Display empty page for new taskList item.
     */
    @RequestMapping("/newConditionInit")
    public String newConditionInit(@ModelAttribute ChatConditionForm chatConditionForm, HttpServletRequest request) {

	String sessionMapID = request.getParameter(ChatConstants.ATTR_SESSION_MAP_ID);
	chatConditionForm.setSessionMapID(sessionMapID);
	chatConditionForm.setOrderId(-1);
	return "pages/authoring/addCondition";
    }

    /**
     * Display edit page for existed taskList item.
     */
    @RequestMapping("/editCondition")
    public String editCondition(@ModelAttribute ChatConditionForm chatConditionForm, HttpServletRequest request) {

	String sessionMapID = chatConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ChatConstants.PARAM_ORDER_ID), -1);
	ChatCondition condition = null;
	if (orderId != -1) {
	    SortedSet<ChatCondition> conditionSet = getChatConditionSet(sessionMap);
	    List<ChatCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, chatConditionForm, request);
	    }
	}
	return condition == null ? null : "pages/authoring/addCondition";
    }

    /**
     * This method will get necessary information from taskList item form and save or update into
     * <code>HttpSession</code> ChatItemList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @RequestMapping("/saveOrUpdateCondition")
    public String saveOrUpdateCondition(@ModelAttribute ChatConditionForm chatConditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateChatCondition(chatConditionForm, request);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/authoring/addCondition";
	}

	try {
	    extractFormToChatCondition(request, chatConditionForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition"));
	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
		return "pages/authoring/addCondition";
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ChatConstants.ATTR_SESSION_MAP_ID, chatConditionForm.getSessionMapID());
	// return null to close this window
	return "pages/authoring/conditionList";
    }

    /**
     * Remove taskList item from HttpSession list and update page display. As authoring rule, all persist only happen
     * when user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping("/removeCondition")
    public String removeCondition(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ChatConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ChatConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<ChatCondition> conditionSet = getChatConditionSet(sessionMap);
	    List<ChatCondition> conditionList = new ArrayList<>(conditionSet);
	    ChatCondition condition = conditionList.remove(orderId);
	    for (ChatCondition otherCondition : conditionSet) {
		if (otherCondition.getOrderId() > orderId) {
		    otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		}
	    }
	    conditionSet.clear();
	    conditionSet.addAll(conditionList);
	    // add to delList
	    List delList = getDeletedChatConditionList(sessionMap);
	    delList.add(condition);
	}

	request.setAttribute(ChatConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/conditionList";
    }

    /**
     * Move up current item.
     */
    @RequestMapping("/upCondition")
    public String upCondition(@ModelAttribute ChatConditionForm chatConditionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(request, true);
    }

    /**
     * Move down current item.
     */
    @RequestMapping("/downCondition")
    public String downCondition(@ModelAttribute ChatConditionForm chatConditionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(request, false);
    }

    @RequestMapping("/switchItem")
    public String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ChatConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ChatConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<ChatCondition> conditionSet = getChatConditionSet(sessionMap);
	    List<ChatCondition> conditionList = new ArrayList<>(conditionSet);
	    // get current and the target item, and switch their sequnece
	    ChatCondition condition = conditionList.get(orderId);
	    ChatCondition repCondition;
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

	request.setAttribute(ChatConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/conditionList";
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************

    /**
     * List save current taskList items.
     */
    private SortedSet<ChatCondition> getChatConditionSet(SessionMap sessionMap) {
	SortedSet<ChatCondition> set = (SortedSet<ChatCondition>) sessionMap.get(ChatConstants.ATTR_CONDITION_SET);
	if (set == null) {
	    set = new TreeSet<>(new TextSearchConditionComparator());
	    sessionMap.put(ChatConstants.ATTR_CONDITION_SET, set);
	}
	return set;
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted items.
     */
    private List getDeletedChatConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ChatConstants.ATTR_DELETED_CONDITION_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
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
     */
    private void populateConditionToForm(int orderId, ChatCondition condition, ChatConditionForm chatConditionForm,
	    HttpServletRequest request) {
	chatConditionForm.populateForm(condition);
	if (orderId >= 0) {
	    chatConditionForm.setOrderId(orderId + 1);
	}
    }

    /**
     * Extract form content to taskListContent.
     */
    private void extractFormToChatCondition(HttpServletRequest request, ChatConditionForm chatConditionForm)
	    throws Exception {

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(chatConditionForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ChatCondition> conditionSet = getChatConditionSet(sessionMap);
	int orderId = chatConditionForm.getOrderId();
	ChatCondition condition = null;

	if (orderId == -1) { // add
	    String properConditionName = chatService.createConditionName(conditionSet);
	    condition = chatConditionForm.extractCondition();
	    condition.setName(properConditionName);
	    int maxSeq = 1;
	    if (conditionSet != null && conditionSet.size() > 0) {
		ChatCondition last = conditionSet.last();
		maxSeq = last.getOrderId() + 1;
	    }
	    condition.setOrderId(maxSeq);
	    conditionSet.add(condition);
	} else { // edit
	    List<ChatCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    chatConditionForm.extractCondition(condition);
	}
    }

    /**
     * Validate taskListCondition
     */
    private MultiValueMap<String, String> validateChatCondition(ChatConditionForm chatConditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	String formConditionName = chatConditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.name.blank"));
	} else {

	    Integer formConditionSequenceId = chatConditionForm.getOrderId();

	    String sessionMapID = chatConditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<ChatCondition> conditionList = getChatConditionSet(sessionMap);
	    for (ChatCondition condition : conditionList) {
		if (formConditionName.equals(condition.getName())
			&& !formConditionSequenceId.equals(new Integer(condition.getOrderId() - 1).toString())) {
		    errorMap.add("GLOBAL", messageService.getMessage("error.condition.duplicated.name"));
		    break;
		}
	    }
	}
	return errorMap;
    }

}