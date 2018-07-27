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


package org.lamsfoundation.lams.tool.forum.web.actions;

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
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.ForumCondition;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ConditionTopicComparator;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.MessageDtoComparator;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumConditionForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Auxiliary action in author mode. It contains operations with ForumCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 *
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.forum.web.actions.AuthoringAction
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
	((ForumConditionForm) form).setOrderId(-1);
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

	ForumConditionForm ForumConditionForm = (ForumConditionForm) form;
	String sessionMapID = ForumConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ForumConstants.PARAM_ORDER_ID), -1);
	ForumCondition condition = null;
	if (orderId != -1) {
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    List<ForumCondition> conditionList = new ArrayList<ForumCondition>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, ForumConditionForm, request);
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

	ForumConditionForm conditionForm = (ForumConditionForm) form;
	ActionErrors errors = validateForumCondition(conditionForm, request);

	if (!errors.isEmpty()) {
	    populateFormWithPossibleItems(form, request);
	    this.addErrors(request, errors);
	    return mapping.findForward("addcondition");
	}

	try {
	    extractFormToForumCondition(request, conditionForm);
	} catch (Exception e) {

	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition", e.getMessage()));
	    if (!errors.isEmpty()) {
		populateFormWithPossibleItems(form, request);
		this.addErrors(request, errors);
		return mapping.findForward("addcondition");
	    }
	}

	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, conditionForm.getSessionMapID());

	return mapping.findForward(ForumConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ForumConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    List<ForumCondition> conditionList = new ArrayList<ForumCondition>(conditionSet);
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
	return mapping.findForward(ForumConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(ForumConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    List<ForumCondition> conditionList = new ArrayList<ForumCondition>(conditionSet);
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
	return mapping.findForward(ForumConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private methods for internal needs
    // *************************************************************************************
    /**
     * Return ForumService bean.
     */
    private IForumService getForumService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
    }

    /**
     * List containing Forum conditions.
     *
     * @param request
     * @return
     */
    private SortedSet<ForumCondition> getForumConditionSet(SessionMap sessionMap) {
	SortedSet<ForumCondition> list = (SortedSet<ForumCondition>) sessionMap.get(ForumConstants.ATTR_CONDITION_SET);
	if (list == null) {
	    list = new TreeSet<ForumCondition>(new TextSearchConditionComparator());
	    sessionMap.put(ForumConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    private SortedSet<MessageDTO> getMessageDTOList(SessionMap sessionMap) {
	SortedSet<MessageDTO> topics = (SortedSet<MessageDTO>) sessionMap.get(ForumConstants.AUTHORING_TOPICS_LIST);
	if (topics == null) {
	    topics = new TreeSet<MessageDTO>(new MessageDtoComparator());
	    sessionMap.put(ForumConstants.AUTHORING_TOPICS_LIST, topics);
	}
	return topics;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedForumConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ForumConstants.ATTR_DELETED_CONDITION_LIST);
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
    private void populateConditionToForm(int orderId, ForumCondition condition, ForumConditionForm form,
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
	ForumConditionForm conditionForm = (ForumConditionForm) form;
	// get back sessionMAP
	String sessionMapID = conditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Set<MessageDTO> messageDTOs = getMessageDTOList(sessionMap);

	// Initialise the LabelValueBeans in the possibleOptions array.

	List<LabelValueBean> lvBeans = new LinkedList<LabelValueBean>();

	for (MessageDTO messageDTO : messageDTOs) {
	    Message topic = messageDTO.getMessage();
	    lvBeans.add(new LabelValueBean(topic.getSubject(), new Long(topic.getCreated().getTime()).toString()));
	}
	conditionForm.setPossibleItems(lvBeans.toArray(new LabelValueBean[] {}));
    }

    /**
     * Extract form content to ForumCondition.
     *
     * @param request
     * @param form
     * @throws Exception
     */
    private void extractFormToForumCondition(HttpServletRequest request, ForumConditionForm form) throws Exception {

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	int orderId = form.getOrderId();
	ForumCondition condition = null;

	if (orderId == -1) { // add
	    String properConditionName = getForumService().createTextSearchConditionName(conditionSet);
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
	    List<ForumCondition> conditionList = new ArrayList<ForumCondition>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    form.extractCondition(condition);
	}

	Long[] selectedItems = form.getSelectedItems();
	Set<Message> conditionTopics = new TreeSet<Message>(new ConditionTopicComparator());
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
     *
     * @param conditionForm
     * @return
     */
    private ActionErrors validateForumCondition(ForumConditionForm conditionForm, HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();

	String formConditionName = conditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {

	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition.name.blank"));
	} else {

	    Integer formConditionOrderId = conditionForm.getOrderId();

	    String sessionMapID = conditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<ForumCondition> conditionSet = getForumConditionSet(sessionMap);
	    for (ForumCondition condition : conditionSet) {
		if (formConditionName.equals(condition.getDisplayName())
			&& !formConditionOrderId.equals(condition.getOrderId())) {

		    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition.duplicated.name"));
		    break;
		}
	    }
	}

	// should be selected at least one question
	Long[] selectedItems = conditionForm.getSelectedItems();
	if (selectedItems == null || selectedItems.length == 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.condition.no.questions.selected"));
	}

	return errors;
    }

    private ActionMessages validate(ForumConditionForm form, ActionMapping mapping, HttpServletRequest request) {
	return new ActionMessages();
    }
}