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

package org.lamsfoundation.lams.tool.survey.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;
import org.lamsfoundation.lams.tool.survey.web.form.SurveyConditionForm;
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
 * Auxiliary action in author mode. It contains operations with SurveyCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.survey.web.controller.AuthoringAction
 */
@Controller
@RequestMapping("/authoringCondition")
public class AuthoringConditionController {

    @Autowired
    @Qualifier("lasurvSurveyService")
    private ISurveyService surveyService;

    @Autowired
    @Qualifier("lasurvMessageService")
    private MessageService messageService;

    /**
     * Display empty page for a new condition.
     *
     * @param surveyConditionForm
     * @param request
     * @return
     */

    @RequestMapping(value = "/newConditionInit")
    public String newConditionInit(HttpServletRequest request) {

	SurveyConditionForm surveyConditionForm = new SurveyConditionForm();
	String sessionMapId = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
	surveyConditionForm.setSessionMapID(sessionMapId);
	populateFormWithPossibleItems(surveyConditionForm, request);
	surveyConditionForm.setOrderId(-1);
	request.setAttribute("surveyConditionForm", surveyConditionForm);
	return "pages/authoring/addCondition";
    }

    /**
     * Display edit page for an existing condition.
     *
     * @param SurveyConditionForm
     * @param request
     * @return
     */
    @RequestMapping(value = "/editCondition")
    public String editCondition(SurveyConditionForm surveyConditionForm, HttpServletRequest request) {

	String sessionMapID = surveyConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ORDER_ID), -1);
	SurveyCondition condition = null;
	if (orderId != -1) {
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    List<SurveyCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, surveyConditionForm, request);
	    }
	}

	populateFormWithPossibleItems(surveyConditionForm, request);
	request.setAttribute("surveyConditionForm", surveyConditionForm);
	return condition == null ? null : "pages/authoring/addCondition";
    }

    /**
     * This method will get necessary information from condition form and save or update into <code>HttpSession</code>
     * condition list. Notice, this save is not persist them into database, just save <code>HttpSession</code>
     * temporarily. Only they will be persist when the entire authoring page is being persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @RequestMapping(value = "/saveOrUpdateCondition")
    public String saveOrUpdateCondition(@ModelAttribute("surveyConditionForm") SurveyConditionForm surveyConditionForm,
	    HttpServletRequest request) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	validateSurveyCondition(surveyConditionForm, request, errorMap);
	if (!errorMap.isEmpty()) {
	    populateFormWithPossibleItems(surveyConditionForm, request);
	    request.setAttribute("errorMap", errorMap);
	    return "pages/authoring/addCondition";
	}

	try {
	    extractFormToSurveyCondition(request, surveyConditionForm);
	} catch (Exception e) {

	    errorMap.add("GLOBAL", messageService.getMessage("error.condition"));
	    if (!errorMap.isEmpty()) {
		populateFormWithPossibleItems(surveyConditionForm, request);
		request.setAttribute("errorMap", errorMap);
		return "pages/authoring/addCondition";
	    }
	}

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, surveyConditionForm.getSessionMapID());
	request.setAttribute("surveyConditionForm", surveyConditionForm);
	return "pages/authoring/conditionList";
    }

    /**
     * Remove condition from HttpSession list and update page display. As authoring rule, all persist only happen when
     * user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param surveyConditionForm
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/removeCondition")
    public String removeCondition(HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    List<SurveyCondition> conditionList = new ArrayList<>(conditionSet);
	    SurveyCondition condition = conditionList.remove(orderId);
	    for (SurveyCondition otherCondition : conditionSet) {
		if (otherCondition.getOrderId() > orderId) {
		    otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		}
	    }
	    conditionSet.clear();
	    conditionSet.addAll(conditionList);
	    // add to delList
	    List deletedList = getDeletedSurveyConditionList(sessionMap);
	    deletedList.add(condition);
	}

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
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

    @RequestMapping(value = "/switchItem")
    public String switchItem(HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    List<SurveyCondition> conditionList = new ArrayList<>(conditionSet);
	    // get current and the target item, and switch their sequnece
	    SurveyCondition condition = conditionList.get(orderId);
	    SurveyCondition repCondition;
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

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/conditionList";
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************

    /**
     * List containing survey conditions.
     *
     * @param request
     * @return
     */
    private SortedSet<SurveyCondition> getSurveyConditionSet(SessionMap sessionMap) {
	SortedSet<SurveyCondition> list = (SortedSet<SurveyCondition>) sessionMap
		.get(SurveyConstants.ATTR_CONDITION_SET);
	if (list == null) {
	    list = new TreeSet<>(new TextSearchConditionComparator());
	    sessionMap.put(SurveyConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    private SortedSet<SurveyQuestion> getSurveyQuestionSet(SessionMap sessionMap) {
	SortedSet<SurveyQuestion> list = (SortedSet<SurveyQuestion>) sessionMap.get(SurveyConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<>(new QuestionsComparator());
	    sessionMap.put(SurveyConstants.ATTR_QUESTION_LIST, list);
	}
	return list;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedSurveyConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, SurveyConstants.ATTR_DELETED_CONDITION_LIST);
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
     * This method will populate condition information to its form for edit use.
     *
     * @param orderId
     * @param condition
     * @param form
     * @param request
     */
    private void populateConditionToForm(int orderId, SurveyCondition condition, SurveyConditionForm form,
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
    private void populateFormWithPossibleItems(SurveyConditionForm conditionForm, HttpServletRequest request) {
	// get back sessionMAP
	String sessionMapID = conditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Set<SurveyQuestion> questions = getSurveyQuestionSet(sessionMap);

	// Initialise the LabelValueBeans in the possibleOptions array.

//	List<LabelValueBean> lvBeans = new LinkedList<>();
	Map<String, String> possibleItems = new HashMap<>(questions.size());
	for (SurveyQuestion question : questions) {
	    if (question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
		possibleItems.put(question.getShortTitle(), new Integer(question.getSequenceId()).toString());
	    }
	}
	conditionForm.setPossibleItems(possibleItems);
    }

    /**
     * Extract form content to SurveyCondition.
     *
     * @param request
     * @param form
     * @throws QaException
     */
    private void extractFormToSurveyCondition(HttpServletRequest request, SurveyConditionForm form) throws Exception {

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	int orderId = form.getOrderId();
	SurveyCondition condition = null;

	if (orderId == -1) { // add
	    String properConditionName = surveyService.createConditionName(conditionSet);
	    condition = form.extractCondition();
	    condition.setName(properConditionName);
	    int maxOrderId = 1;
	    if (conditionSet != null && conditionSet.size() > 0) {
		SurveyCondition last = conditionSet.last();
		maxOrderId = last.getOrderId() + 1;
	    }
	    condition.setOrderId(maxOrderId);
	    conditionSet.add(condition);
	} else { // edit
	    List<SurveyCondition> conditionList = new ArrayList<>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    form.extractCondition(condition);
	}

	Integer[] selectedItems = form.getSelectedItems();
	Set<SurveyQuestion> conditionQuestions = new TreeSet<>(new QuestionsComparator());
	Set<SurveyQuestion> questions = getSurveyQuestionSet(sessionMap);

	for (Integer selectedItem : selectedItems) {
	    for (SurveyQuestion question : questions) {
		if (selectedItem.equals(new Integer(question.getSequenceId()))) {
		    conditionQuestions.add(question);
		}
	    }
	}
	condition.setQuestions(conditionQuestions);
    }

    /**
     * Validate SurveyCondition
     *
     * @param conditionForm
     * @return
     */
    private void validateSurveyCondition(SurveyConditionForm conditionForm, HttpServletRequest request,
	    MultiValueMap<String, String> errorMap) {

	String formConditionName = conditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {

	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.name.blank"));
	} else {

	    Integer formConditionOrderId = conditionForm.getOrderId();

	    String sessionMapID = conditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    for (SurveyCondition condition : conditionSet) {
		if (formConditionName.equals(condition.getDisplayName())
			&& !formConditionOrderId.equals(condition.getOrderId())) {

		    errorMap.add("GLOBAL", messageService.getMessage("error.condition.duplicated.name"));
		    break;
		}
	    }
	}

	// should be selected at least one question
	Integer[] selectedItems = conditionForm.getSelectedItems();
	if (selectedItems == null || selectedItems.length == 0) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.condition.no.questions.selected"));
	}

    }

}