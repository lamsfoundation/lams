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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;
import org.lamsfoundation.lams.tool.survey.web.form.SurveyConditionForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Auxiliary action in author mode. It contains operations with SurveyCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.survey.web.controller.AuthoringAction
 */
public class AuthoringConditionAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();

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
	return null;
    }

    /**
     * Display empty page for a new condition.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newConditionInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	populateFormWithPossibleItems(form, request);
	((SurveyConditionForm) form).setOrderId(-1);
	return mapping.findForward("addcondition");
    }

    /**
     * Display edit page for an existing condition.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SurveyConditionForm SurveyConditionForm = (SurveyConditionForm) form;
	String sessionMapID = SurveyConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ORDER_ID), -1);
	SurveyCondition condition = null;
	if (orderId != -1) {
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    List<SurveyCondition> conditionList = new ArrayList<SurveyCondition>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, SurveyConditionForm, request);
	    }
	}

	populateFormWithPossibleItems(form, request);
	return condition == null ? null : mapping.findForward("addcondition");
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
    private ActionForward saveOrUpdateCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SurveyConditionForm conditionForm = (SurveyConditionForm) form;
	ActionErrors errors = validateSurveyCondition(conditionForm, request);

	if (!errors.isEmpty()) {
	    populateFormWithPossibleItems(form, request);
	    this.addErrors(request, errors);
	    return mapping.findForward("addcondition");
	}

	try {
	    extractFormToSurveyCondition(request, conditionForm);
	} catch (Exception e) {

	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition", e.getMessage()));
	    if (!errors.isEmpty()) {
		populateFormWithPossibleItems(form, request);
		this.addErrors(request, errors);
		return mapping.findForward("addcondition");
	    }
	}

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, conditionForm.getSessionMapID());

	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Remove condition from HttpSession list and update page display. As authoring rule, all persist only happen when
     * user submit whole page. So this remove is just impact HttpSession values.
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
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    List<SurveyCondition> conditionList = new ArrayList<SurveyCondition>(conditionSet);
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
	return mapping.findForward(SurveyConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    List<SurveyCondition> conditionList = new ArrayList<SurveyCondition>(conditionSet);
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
	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************
    /**
     * Return SurveyService bean.
     */
    private ISurveyService getSurveyService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
    }

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
	    list = new TreeSet<SurveyCondition>(new TextSearchConditionComparator());
	    sessionMap.put(SurveyConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    private SortedSet<SurveyQuestion> getSurveyQuestionSet(SessionMap sessionMap) {
	SortedSet<SurveyQuestion> list = (SortedSet<SurveyQuestion>) sessionMap.get(SurveyConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<SurveyQuestion>(new QuestionsComparator());
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
    private void populateFormWithPossibleItems(ActionForm form, HttpServletRequest request) {
	SurveyConditionForm conditionForm = (SurveyConditionForm) form;
	// get back sessionMAP
	String sessionMapID = conditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Set<SurveyQuestion> questions = getSurveyQuestionSet(sessionMap);

	// Initialise the LabelValueBeans in the possibleOptions array.

	List<LabelValueBean> lvBeans = new LinkedList<LabelValueBean>();
	int i = 0;
	for (SurveyQuestion question : questions) {
	    if (question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
		lvBeans.add(
			new LabelValueBean(question.getShortTitle(), new Integer(question.getSequenceId()).toString()));
	    }
	}
	conditionForm.setPossibleItems(lvBeans.toArray(new LabelValueBean[] {}));
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
	    String properConditionName = getSurveyService().createConditionName(conditionSet);
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
	    List<SurveyCondition> conditionList = new ArrayList<SurveyCondition>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    form.extractCondition(condition);
	}

	Integer[] selectedItems = form.getSelectedItems();
	Set<SurveyQuestion> conditionQuestions = new TreeSet<SurveyQuestion>(new QuestionsComparator());
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
    private ActionErrors validateSurveyCondition(SurveyConditionForm conditionForm, HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();

	String formConditionName = conditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {

	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition.name.blank"));
	} else {

	    Integer formConditionOrderId = conditionForm.getOrderId();

	    String sessionMapID = conditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	    for (SurveyCondition condition : conditionSet) {
		if (formConditionName.equals(condition.getDisplayName())
			&& !formConditionOrderId.equals(condition.getOrderId())) {

		    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition.duplicated.name"));
		    break;
		}
	    }
	}

	// should be selected at least one question
	Integer[] selectedItems = conditionForm.getSelectedItems();
	if (selectedItems == null || selectedItems.length == 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition.no.questions.selected"));
	}

	return errors;
    }

    private ActionMessages validate(SurveyConditionForm form, ActionMapping mapping, HttpServletRequest request) {
	return new ActionMessages();
    }
}