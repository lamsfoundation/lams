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

package org.lamsfoundation.lams.tool.forum.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.forum.ForumConstants;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ConditionTopicComparator;
import org.lamsfoundation.lams.tool.forum.util.MessageDtoComparator;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumConditionForm;
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
 * Auxiliary action in author mode. It contains operations with ForumCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.forum.web.controller.AuthoringController
 */
@Controller
@RequestMapping("/authoringCondition")
public class AuthoringConditionController {

    @Autowired
    private IForumService forumService;

    @Autowired
    @Qualifier("forumMessageService")
    private MessageService messageService;

    /**
     * Display empty page for a new condition.
     */
    @RequestMapping("/newConditionInit")
    public String newConditionInit(@ModelAttribute ForumConditionForm forumConditionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	populateFormWithPossibleItems(forumConditionForm, request);
	forumConditionForm.setOrderId(-1);
	return "jsps/authoring/addCondition";
    }

    /**
     * Display edit page for an existing condition.
     */
    @RequestMapping("/editCondition")
    public String editCondition(@ModelAttribute ForumConditionForm forumConditionForm, HttpServletRequest request) {

	String sessionMapID = forumConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ForumConstants.PARAM_ORDER_ID), -1);
	ForumCondition condition = null;
	if (orderId != -1) {
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    List<ForumCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, forumConditionForm, request);
	    }
	}

	populateFormWithPossibleItems(forumConditionForm, request);
	return condition == null ? null : "jsps/authoring/addCondition";
    }

    /**
     * This method will get necessary information from condition form and save or update into <code>HttpSession</code>
     * condition list. Notice, this save is not persist them into database, just save <code>HttpSession</code>
     * temporarily. Only they will be persist when the entire authoring page is being persisted.
     */
    @RequestMapping("/saveOrUpdateCondition")
    public String saveOrUpdateCondition(@ModelAttribute ForumConditionForm forumConditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateForumCondition(forumConditionForm, request);

	if (!errorMap.isEmpty()) {
	    populateFormWithPossibleItems(forumConditionForm, request);
	    request.setAttribute("errorMap", errorMap);
	    return "jsps/authoring/addCondition";
	}

	try {
	    extractFormToForumCondition(request, forumConditionForm);
	} catch (Exception e) {

	    errorMap.add("GLOBAL", messageService.getMessage("error.condition"));
	    if (!errorMap.isEmpty()) {
		populateFormWithPossibleItems(forumConditionForm, request);
		request.setAttribute("errorMap", errorMap);
		return "jsps/authoring/addCondition";
	    }
	}

	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, forumConditionForm.getSessionMapID());

	return "jsps/authoring/conditionList";
    }

    /**
     * Remove condition from HttpSession list and update page display. As authoring rule, all persist only happen when
     * user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping("/removeCondition")
    public String removeCondition(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ForumConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    List<ForumCondition> conditionList = new ArrayList<>(conditionSet);
	    ForumCondition condition = conditionList.remove(orderId);

	    for (ForumCondition otherCondition : conditionSet) {
		if (otherCondition.getOrderId() > orderId) {
		    otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		}
	    }
	    conditionSet.clear();
	    conditionSet.addAll(conditionList);
	    // add to delList
	    List deletedList = getDeletedForumConditionList(sessionMap);
	    deletedList.add(condition);
	}

	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "jsps/authoring/conditionList";
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

    private String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ForumConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    List<ForumCondition> conditionList = new ArrayList<>(conditionSet);
	    // get current and the target item, and switch their sequnece
	    ForumCondition condition = conditionList.get(orderId);
	    ForumCondition repCondition;
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

	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "jsps/authoring/conditionList";
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************

    /**
     * List containing Forum conditions.
     */
    private SortedSet<ForumCondition> getForumConditionSet(SessionMap sessionMap) {
	SortedSet<ForumCondition> list = (SortedSet<ForumCondition>) sessionMap.get(ForumConstants.ATTR_CONDITION_SET);
	if (list == null) {
	    list = new TreeSet<>(new TextSearchConditionComparator());
	    sessionMap.put(ForumConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    private SortedSet<MessageDTO> getMessageDTOList(SessionMap sessionMap) {
	SortedSet<MessageDTO> topics = (SortedSet<MessageDTO>) sessionMap.get(ForumConstants.AUTHORING_TOPICS_LIST);
	if (topics == null) {
	    topics = new TreeSet<>(new MessageDtoComparator());
	    sessionMap.put(ForumConstants.AUTHORING_TOPICS_LIST, topics);
	}
	return topics;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted items.
     */
    private List getDeletedForumConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.ATTR_DELETED_CONDITION_LIST);
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
     * This method will populate condition information to its form for edit use.
     */
    private void populateConditionToForm(int orderId, ForumCondition condition, ForumConditionForm form,
	    HttpServletRequest request) {
	form.populateForm(condition);
	if (orderId >= 0) {
	    form.setOrderId(orderId + 1);
	}
    }

    /**
     * This method will populate questions to choose to the form for edit use.
     */
    private void populateFormWithPossibleItems(ForumConditionForm conditionForm, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = conditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Set<MessageDTO> messageDTOs = getMessageDTOList(sessionMap);

	// Initialise the LabelValueBeans in the possibleOptions array.

	Map<String, String> possibleItems = new HashMap<>(messageDTOs.size());

	for (MessageDTO messageDTO : messageDTOs) {
	    Message topic = messageDTO.getMessage();
	    possibleItems.put(topic.getSubject(), new Long(topic.getCreated().getTime()).toString());
	}
	conditionForm.setPossibleItems(possibleItems);
    }

    /**
     * Extract form content to ForumCondition.
     */
    private void extractFormToForumCondition(HttpServletRequest request, ForumConditionForm form) throws Exception {

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	int orderId = form.getOrderId();
	ForumCondition condition = null;

	if (orderId == -1) { // add
	    String properConditionName = forumService.createTextSearchConditionName(conditionSet);
	    condition = form.extractCondition();
	    condition.setName(properConditionName);
	    int maxOrderId = 1;
	    if (conditionSet != null && conditionSet.size() > 0) {
		ForumCondition last = conditionSet.last();
		maxOrderId = last.getOrderId() + 1;
	    }
	    condition.setOrderId(maxOrderId);
	    conditionSet.add(condition);
	} else { // edit
	    List<ForumCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    form.extractCondition(condition);
	}

	Long[] selectedItems = form.getSelectedItems();
	Set<Message> conditionTopics = new TreeSet<>(new ConditionTopicComparator());
	Set<MessageDTO> messageDTOs = getMessageDTOList(sessionMap);

	for (Long selectedItem : selectedItems) {
	    for (MessageDTO messageDTO : messageDTOs) {
		Message topic = messageDTO.getMessage();
		if (selectedItem.equals(topic.getCreated().getTime())) {
		    conditionTopics.add(topic);
		}
	    }
	}
	condition.setTopics(conditionTopics);
    }

    /**
     * Validate ForumCondition
     */
    private MultiValueMap<String, String> validateForumCondition(ForumConditionForm conditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	String formConditionName = conditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {

	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.name.blank"));
	} else {

	    Integer formConditionOrderId = conditionForm.getOrderId();

	    String sessionMapID = conditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    for (ForumCondition condition : conditionSet) {
		if (formConditionName.equals(condition.getDisplayName())
			&& !formConditionOrderId.equals(condition.getOrderId())) {

		    errorMap.add("GLOBAL", messageService.getMessage("error.condition.duplicated.name"));
		    break;
		}
	    }
	}

	// should be selected at least one question
	Long[] selectedItems = conditionForm.getSelectedItems();
	if (selectedItems == null || selectedItems.length == 0) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.no.questions.selected"));
	}

	return errorMap;
    }

}