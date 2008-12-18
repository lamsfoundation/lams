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

package org.lamsfoundation.lams.tool.videoRecorder.web.actions;

import java.util.ArrayList;
import java.util.List;
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
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderCondition;
import org.lamsfoundation.lams.tool.videoRecorder.service.IVideoRecorderService;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderServiceProxy;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderConstants;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.tool.videoRecorder.web.forms.AuthoringForm;
import org.lamsfoundation.lams.tool.videoRecorder.web.forms.VideoRecorderConditionForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Auxiliary action in author mode. It contains operations with VideoRecorderCondition. The rest of operations are located in
 * <code>AuthoringAction</code> action.
 * 
 * @author Marcin Cieslak
 * @see org.lamsfoundation.lams.tool.videoRecorder.web.action.AuthoringAction
 * 
 */
public class AuthoringVideoRecorderConditionAction extends Action {
    public IVideoRecorderService videoRecorderService;

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
	String sessionMapID = WebUtil.readStrParam(request, VideoRecorderConstants.ATTR_SESSION_MAP_ID);
	((VideoRecorderConditionForm) form).setSessionMapID(sessionMapID);
	((VideoRecorderConditionForm) form).setOrderId(-1);
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

	VideoRecorderConditionForm videoRecorderConditionForm = (VideoRecorderConditionForm) form;
	String sessionMapID = videoRecorderConditionForm.getSessionMapID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(VideoRecorderConstants.PARAM_ORDER_ID), -1);
	VideoRecorderCondition condition = null;
	if (orderId != -1) {
	    SortedSet<VideoRecorderCondition> conditionSet = getVideoRecorderConditionSet(sessionMap);
	    List<VideoRecorderCondition> conditionList = new ArrayList<VideoRecorderCondition>(conditionSet);
	    condition = conditionList.get(orderId);
	    if (condition != null) {
		populateConditionToForm(orderId, condition, (VideoRecorderConditionForm) form, request);
	    }
	}
	return condition == null ? null : mapping.findForward("addcondition");
    }

    /**
     * This method will get necessary information from taskList item form and save or update into
     * <code>HttpSession</code> VideoRecorderItemList. Notice, this save is not persist them into database, just save
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

	VideoRecorderConditionForm conditionForm = (VideoRecorderConditionForm) form;
	ActionErrors errors = validateVideoRecorderCondition(conditionForm, request);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return mapping.findForward("addcondition");
	}

	try {
	    extractFormToVideoRecorderCondition(request, conditionForm);
	} catch (Exception e) {
	    // any upload exception will display as normal error message rather then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(VideoRecorderConstants.ERROR_MSG_CONDITION, e
		    .getMessage()));
	    if (!errors.isEmpty()) {
		this.addErrors(request, errors);
		return mapping.findForward("addcondition");
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(VideoRecorderConstants.ATTR_SESSION_MAP_ID, conditionForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(VideoRecorderConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, VideoRecorderConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(VideoRecorderConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<VideoRecorderCondition> conditionSet = getVideoRecorderConditionSet(sessionMap);
	    List<VideoRecorderCondition> conditionList = new ArrayList<VideoRecorderCondition>(conditionSet);
	    VideoRecorderCondition condition = conditionList.remove(orderId);
	    for (VideoRecorderCondition otherCondition : conditionSet) {
		if (otherCondition.getOrderId() > orderId) {
		    otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		}
	    }
	    conditionSet.clear();
	    conditionSet.addAll(conditionList);
	    // add to delList
	    List delList = getDeletedVideoRecorderConditionList(sessionMap);
	    delList.add(condition);
	}

	request.setAttribute(VideoRecorderConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(VideoRecorderConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, VideoRecorderConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int orderId = NumberUtils.stringToInt(request.getParameter(VideoRecorderConstants.PARAM_ORDER_ID), -1);
	if (orderId != -1) {
	    SortedSet<VideoRecorderCondition> conditionSet = getVideoRecorderConditionSet(sessionMap);
	    List<VideoRecorderCondition> conditionList = new ArrayList<VideoRecorderCondition>(conditionSet);
	    // get current and the target item, and switch their sequnece
	    VideoRecorderCondition condition = conditionList.get(orderId);
	    VideoRecorderCondition repCondition;
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

	request.setAttribute(VideoRecorderConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(VideoRecorderConstants.SUCCESS);
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
    private SortedSet<VideoRecorderCondition> getVideoRecorderConditionSet(SessionMap sessionMap) {
	SortedSet<VideoRecorderCondition> set = (SortedSet<VideoRecorderCondition>) sessionMap
		.get(VideoRecorderConstants.ATTR_CONDITION_SET);
	if (set == null) {
	    set = new TreeSet<VideoRecorderCondition>(new TextSearchConditionComparator());
	    sessionMap.put(VideoRecorderConstants.ATTR_CONDITION_SET, set);
	}
	return set;
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted items.
     * 
     * @param request
     * @return
     */
    private List getDeletedVideoRecorderConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, VideoRecorderConstants.ATTR_DELETED_CONDITION_LIST);
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
     * @param form
     * @param request
     */
    private void populateConditionToForm(int orderId, VideoRecorderCondition condition, VideoRecorderConditionForm form,
	    HttpServletRequest request) {
	form.populateForm(condition);
	if (orderId >= 0) {
	    form.setOrderId(orderId + 1);
	}
    }

    /**
     * Extract form content to taskListContent.
     * 
     * @param request
     * @param form
     * @throws VideoRecorderException
     */
    private void extractFormToVideoRecorderCondition(HttpServletRequest request, VideoRecorderConditionForm form)
	    throws Exception {
	/*
	 * BE CAREFUL: This method will copy necessary info from request form to a old or new VideoRecorderItem instance. It
	 * gets all info EXCEPT VideoRecorderItem.createDate and VideoRecorderItem.createBy, which need be set when persisting
	 * this taskList item.
	 */

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<VideoRecorderCondition> conditionSet = getVideoRecorderConditionSet(sessionMap);
	int orderId = form.getOrderId();
	VideoRecorderCondition condition = null;

	if (orderId == -1) { // add
	    String properConditionName = getVideoRecorderService().createConditionName(conditionSet);
	    condition = form.extractCondition();
	    condition.setName(properConditionName);
	    int maxSeq = 1;
	    if (conditionSet != null && conditionSet.size() > 0) {
		VideoRecorderCondition last = conditionSet.last();
		maxSeq = last.getOrderId() + 1;
	    }
	    condition.setOrderId(maxSeq);
	    conditionSet.add(condition);
	} else { // edit
	    List<VideoRecorderCondition> conditionList = new ArrayList<VideoRecorderCondition>(conditionSet);
	    condition = conditionList.get(orderId - 1);
	    form.extractCondition(condition);
	}
    }

    /**
     * Validate taskListCondition
     * 
     * @param conditionForm
     * @return
     */
    private ActionErrors validateVideoRecorderCondition(VideoRecorderConditionForm conditionForm, HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();

	String formConditionName = conditionForm.getDisplayName();
	if (StringUtils.isBlank(formConditionName)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(VideoRecorderConstants.ERROR_MSG_NAME_BLANK));
	} else {

	    Integer formConditionSequenceId = conditionForm.getOrderId();

	    String sessionMapID = conditionForm.getSessionMapID();
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	    SortedSet<VideoRecorderCondition> conditionList = getVideoRecorderConditionSet(sessionMap);
	    for (VideoRecorderCondition condition : conditionList) {
		if (formConditionName.equals(condition.getName())
			&& !formConditionSequenceId.equals(new Integer(condition.getOrderId() - 1).toString())) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			    VideoRecorderConstants.ERROR_MSG_NAME_DUPLICATED));
		    break;
		}
	    }
	}
	return errors;
    }

    private ActionMessages validate(AuthoringForm taskListForm, ActionMapping mapping, HttpServletRequest request) {
	return new ActionMessages();
    }

    private IVideoRecorderService getVideoRecorderService() {
	if (videoRecorderService == null) {
	    videoRecorderService = VideoRecorderServiceProxy.getVideoRecorderService(this.getServlet().getServletContext());
	}
	return videoRecorderService;
    }
}
