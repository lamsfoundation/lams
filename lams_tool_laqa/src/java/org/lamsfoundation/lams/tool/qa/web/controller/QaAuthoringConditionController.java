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

package org.lamsfoundation.lams.tool.qa.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.model.QaCondition;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaQuestionComparator;
import org.lamsfoundation.lams.tool.qa.web.form.QaConditionForm;
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
 * Auxiliary action in author mode. It contains operations with QaCondition. The
 * rest of operations are located in <code>QaAction</code> action.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.qa.web.controller.QaAction
 */
@Controller
@RequestMapping("/authoringConditions")
public class QaAuthoringConditionController {

    @Autowired
    private IQaService qaService;

    @Autowired
    @Qualifier("qaMessageService")
    private MessageService messageService;

    /**
     * Display empty page for a new condition.
     *
     * @param QaConditionForm
     * @param request
     * @return
     */
    @RequestMapping("/newConditionInit")
    private String newConditionInit(@ModelAttribute("QaConditionForm") QaConditionForm QaConditionForm,
	    HttpServletRequest request) {

	populateFormWithPossibleItems(QaConditionForm, request);
	QaConditionForm.setOrderId(-1);
	return "authoring/addCondition";
    }

    /**
     * Display edit page for an existing condition.
     *
     * @param QaConditionForm
     * @param request
     * @return
     */
    @RequestMapping("/editCondition")
    private String editCondition(@ModelAttribute("QaConditionForm") QaConditionForm QaConditionForm,
	    HttpServletRequest request) {

	String sessionMapID = QaConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(QaAppConstants.PARAM_ORDER_ID), -1);
	QaCondition condition = null;
	if (orderId != -1) {
	    SortedSet<QaCondition> conditionSet = getConditions(sessionMap);
	    List<QaCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, QaConditionForm, request);
	    }
	}

	populateFormWithPossibleItems(QaConditionForm, request);
	return condition == null ? null : "authoring/addCondition";
    }

    /**
     * This method will get necessary information from condition form and save
     * or update into <code>HttpSession</code> condition list. Notice, this
     * save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when
     * the entire authoring page is being persisted.
     *
     * @param QaConditionForm
     * @param request
     * @return
     * @throws ServletException
     */
    @RequestMapping(value = "/saveOrUpdateCondition")
    private String saveOrUpdateCondition(@ModelAttribute("QaConditionForm") QaConditionForm QaConditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = validateQaCondition(QaConditionForm, request);

	if (!errorMap.isEmpty()) {
	    populateFormWithPossibleItems(QaConditionForm, request);
	    request.setAttribute("errorMap", errorMap);
	    return "authoring/addCondition";
	}

	try {
	    extractFormToQaCondition(request, QaConditionForm);
	} catch (Exception e) {

	    errorMap.add("GLOBAL", messageService.getMessage("error.condition", new Object[] { e.getMessage() }));
	    if (!errorMap.isEmpty()) {
		populateFormWithPossibleItems(QaConditionForm, request);
		request.setAttribute("errorMap", errorMap);
		return "authoring/addCondition";
	    }
	}

	request.setAttribute(QaAppConstants.ATTR_SESSION_MAP_ID, QaConditionForm.getSessionMapID());

	return "authoring/conditionList";
    }

    /**
     * Remove condition from HttpSession list and update page display. As
     * authoring rule, all persist only happen when user submit whole page. So
     * this remove is just impact HttpSession values.
     *
     * @param request
     * @return
     */
    @RequestMapping("/removeCondition")
    private String removeCondition(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, QaAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(QaAppConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<QaCondition> conditionSet = getConditions(sessionMap);
	    List<QaCondition> conditionList = new ArrayList<>(conditionSet);
	    QaCondition condition = conditionList.remove(orderId);
	    for (QaCondition otherCondition : conditionSet) {
		if (otherCondition.getOrderId() > orderId) {
		    otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		}
	    }
	    conditionSet.clear();
	    conditionSet.addAll(conditionList);
	    // add to delList
	    List deletedList = QaAuthoringConditionController.getDeletedQaConditionList(sessionMap);
	    deletedList.add(condition);
	}

	request.setAttribute(QaAppConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "authoring/conditionList";
    }

    /**
     * Move up current item.
     *
     * @param request
     * @return
     */
    @RequestMapping("/upCondition")
    private String upCondition(HttpServletRequest request) {
	return switchItem(request, true);
    }

    /**
     * Move down current item.
     *
     * @param request
     * @return
     */
    @RequestMapping("/downCondition")
    private String downCondition(HttpServletRequest request) {
	return switchItem(request, false);
    }

    private String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, QaAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(QaAppConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<QaCondition> conditionSet = getConditions(sessionMap);
	    List<QaCondition> conditionList = new ArrayList<>(conditionSet);
	    // get current and the target item, and switch their sequnece
	    QaCondition condition = conditionList.get(orderId);
	    QaCondition repCondition;
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

	request.setAttribute(QaAppConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "authoring/conditionList";
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
    @SuppressWarnings("unchecked")
    public static SortedSet<QaCondition> getConditions(SessionMap<String, Object> sessionMap) {
	SortedSet<QaCondition> list = (SortedSet<QaCondition>) sessionMap.get(QaAppConstants.ATTR_CONDITION_SET);
	if (list == null) {
	    list = new TreeSet<>(new TextSearchConditionComparator());
	    sessionMap.put(QaAppConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    /**
     * List save current taskList items.
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private SortedSet<QaQueContent> getQuestions(SessionMap<String, Object> sessionMap) {
	SortedSet<QaQueContent> list = (SortedSet<QaQueContent>) sessionMap.get(QaAppConstants.LIST_QUESTIONS);
	if (list == null) {
	    list = new TreeSet<>(new QaQuestionComparator());
	    sessionMap.put(QaAppConstants.LIST_QUESTIONS, list);
	}
	return list;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted
     * items.
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<QaCondition> getDeletedQaConditionList(SessionMap<String, Object> sessionMap) {
	List<QaCondition> list = (List<QaCondition>) sessionMap.get(QaAppConstants.ATTR_DELETED_CONDITION_LIST);
	if (list == null) {
	    list = new ArrayList<>();
	    sessionMap.put(QaAppConstants.ATTR_DELETED_CONDITION_LIST, list);
	}
	return list;
    }
    
    /**
     * This method will populate condition information to its form for edit use.
     *
     * @param orderId
     * @param condition
     * @param form
     * @param request
     */
    private void populateConditionToForm(int orderId, QaCondition condition, QaConditionForm form,
	    HttpServletRequest request) {
	form.populateForm(condition);
	if (orderId >= 0) {
	    form.setOrderId(orderId + 1);
	}
    }

    /**
     * This method will populate questions to choose to the form for edit use.
     *
     * @param sequenceId
     * @param condition
     * @param form
     * @param request
     */
    private void populateFormWithPossibleItems(@ModelAttribute("QaConditionForm") QaConditionForm QaConditionForm,
	    HttpServletRequest request) {
	// get back sessionMAP
	String sessionMapID = QaConditionForm.getSessionMapID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	SortedSet<QaQueContent> questions = getQuestions(sessionMap);

	// Initialise the LabelValueBeans in the possibleOptions array.
	Map<String, String> possibleItems = new HashMap<>(questions.size());
	int i = 0;
	for (QaQueContent question : questions) {
	    String nonHTMLQuestion = question.getQbQuestion().getName();
	    if (nonHTMLQuestion != null) {
		nonHTMLQuestion = WebUtil.removeHTMLtags(nonHTMLQuestion);
		// we don't want to cite the whole question, so we just leave some first characters; it should be enough
		// to recognise the question by a teacher
		if (nonHTMLQuestion.length() > QaAppConstants.QUESTION_CUTOFF_INDEX) {
		    nonHTMLQuestion = nonHTMLQuestion.substring(0, QaAppConstants.QUESTION_CUTOFF_INDEX) + "...";
		}
	    }
	    possibleItems.put(nonHTMLQuestion, new Integer(question.getDisplayOrder()).toString());
	}
	QaConditionForm.setPossibleItems(possibleItems);
    }

    /**
     * Extract form content to QaCondition.
     *
     * @param request
     * @param form
     * @throws QaException
     */
    private void extractFormToQaCondition(HttpServletRequest request, QaConditionForm form) throws Exception {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<QaCondition> conditionSet = getConditions(sessionMap);
	int orderId = form.getOrderId();
	QaCondition condition = null;

	if (orderId == -1) { // add
	    String properConditionName = qaService.createConditionName(conditionSet);
	    condition = form.extractCondition();
	    condition.setName(properConditionName);
	    int maxOrderId = 1;
	    if (conditionSet != null && conditionSet.size() > 0) {
		QaCondition last = conditionSet.last();
		maxOrderId = last.getOrderId() + 1;
	    }
	    condition.setOrderId(maxOrderId);
	    conditionSet.add(condition);
	} else { // edit
	    List<QaCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    form.extractCondition(condition);
	}

	Integer[] selectedItems = form.getSelectedItems();
	SortedSet<QaQueContent> questions = getQuestions(sessionMap);

	condition.temporaryQaQuestions.clear();
	for (Integer selectedItem : selectedItems) {
	    for (QaQueContent question : questions) {
		if (selectedItem.equals(new Integer(question.getDisplayOrder()))) {
		    condition.temporaryQaQuestions.add(question);
		}
	    }
	}

    }

    /**
     * Validate QaCondition
     *
     * @param QaConditionForm
     * @return
     */
    private MultiValueMap<String, String> validateQaCondition(QaConditionForm QaConditionForm,
	    HttpServletRequest request) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	String formConditionName = QaConditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {

	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.name.blank"));
	} else {

	    Integer formConditionOrderId = QaConditionForm.getOrderId();

	    String sessionMapID = QaConditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<QaCondition> conditionSet = getConditions(sessionMap);
	    for (QaCondition condition : conditionSet) {
		if (formConditionName.equals(condition.getDisplayName())
			&& !formConditionOrderId.equals(condition.getOrderId())) {

		    errorMap.add("GLOBAL", messageService.getMessage("error.condition.duplicated.name"));
		    break;
		}
	    }
	}

	// should be selected at least one question
	Integer[] selectedItems = QaConditionForm.getSelectedItems();
	if (selectedItems == null || selectedItems.length == 0) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.no.questions.selected"));
	}

	return errorMap;
    }

}