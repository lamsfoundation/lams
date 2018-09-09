/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.kaltura.web.actions;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.kaltura.model.Kaltura;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaItem;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaUser;
import org.lamsfoundation.lams.tool.kaltura.service.IKalturaService;
import org.lamsfoundation.lams.tool.kaltura.service.KalturaServiceProxy;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaException;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaItemComparator;
import org.lamsfoundation.lams.tool.kaltura.web.forms.AuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Andrey Balan
 *
 *
 *
 *
 *
 *
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(AuthoringAction.class);

    public IKalturaService kalturaService;

    // Authoring SessionMap key names
    private static final String KEY_TOOL_CONTENT_ID = "toolContentID";
    private static final String KEY_CONTENT_FOLDER_ID = "contentFolderID";
    private static final String KEY_MODE = "mode";

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// Extract toolContentID from parameters.
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	// set up kalturaService
	if (kalturaService == null) {
	    kalturaService = KalturaServiceProxy.getKalturaService(this.getServlet().getServletContext());
	}

	// retrieving Kaltura with given toolContentID
	Kaltura kaltura = kalturaService.getKalturaByContentId(toolContentID);
	if (kaltura == null) {
	    kaltura = kalturaService.copyDefaultContent(toolContentID);
	    kaltura.setCreateDate(new Date());
	    kalturaService.saveOrUpdateKaltura(kaltura);
	}

	if (mode.isTeacher()) {
	    // Set the defineLater flag so that learners cannot use content
	    // while we
	    // are editing. This flag is released when updateContent is called.
	    kaltura.setDefineLater(true);
	    kalturaService.saveOrUpdateKaltura(kaltura);
	    
	    //audit log the teacher has started editing activity in monitor
	    kalturaService.auditLogStartEditingActivityInMonitor(toolContentID);
	}

	// Set up the authForm.
	AuthoringForm authForm = (AuthoringForm) form;
	authForm.setTitle(kaltura.getTitle());
	authForm.setInstructions(kaltura.getInstructions());
	authForm.setLockOnFinished(kaltura.isLockOnFinished());
	authForm.setAllowContributeVideos(kaltura.isAllowContributeVideos());
	authForm.setAllowSeeingOtherUsersRecordings(kaltura.isAllowSeeingOtherUsersRecordings());
	authForm.setLearnerContributionLimit(kaltura.getLearnerContributionLimit());
	authForm.setAllowComments(kaltura.isAllowComments());
	authForm.setAllowRatings(kaltura.isAllowRatings());
	authForm.setReflectOnActivity(kaltura.isReflectOnActivity());
	authForm.setReflectInstructions(kaltura.getReflectInstructions());

	// Set up sessionMap
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP, sessionMap);
	authForm.setSessionMapID(sessionMap.getSessionID());

	sessionMap.put(AuthoringAction.KEY_MODE, mode);
	sessionMap.put(AuthoringAction.KEY_CONTENT_FOLDER_ID, contentFolderID);
	sessionMap.put(AuthoringAction.KEY_TOOL_CONTENT_ID, toolContentID);

	Set<KalturaItem> items = kaltura.getKalturaItems();
	// init taskList item list
	SortedSet<KalturaItem> taskListItemList = getItemList(sessionMap);
	taskListItemList.clear();
	taskListItemList.addAll(items);

	return mapping.findForward("success");
    }

    public ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get authForm and session map.
	AuthoringForm authForm = (AuthoringForm) form;
	SessionMap<String, Object> sessionMap = getSessionMap(request, authForm);

	// get kaltura content.
	Long toolContentId = (Long) sessionMap.get(AuthoringAction.KEY_TOOL_CONTENT_ID);
	Kaltura kaltura = kalturaService.getKalturaByContentId(toolContentId);

	// update kaltura content using form inputs
	kaltura.setTitle(authForm.getTitle());
	kaltura.setInstructions(authForm.getInstructions());
	kaltura.setLockOnFinished(authForm.isLockOnFinished());
	kaltura.setAllowContributeVideos(authForm.isAllowContributeVideos());
	kaltura.setLearnerContributionLimit(authForm.getLearnerContributionLimit());
	kaltura.setAllowSeeingOtherUsersRecordings(authForm.isAllowSeeingOtherUsersRecordings());
	kaltura.setAllowComments(authForm.isAllowComments());
	kaltura.setAllowRatings(authForm.isAllowRatings());
	kaltura.setReflectOnActivity(authForm.isReflectOnActivity());
	kaltura.setReflectInstructions(authForm.getReflectInstructions());

	// *******************************Handle user*******************
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	KalturaUser kalturaUser = kalturaService.getUserByUserIdAndContentId(new Long(user.getUserID().intValue()),
		toolContentId);
	if (kalturaUser == null) {
	    kalturaUser = new KalturaUser(user, kaltura);
	}

	kaltura.setCreatedBy(kalturaUser);

	// ************************* Handle taskList items *******************
	Set itemList = new LinkedHashSet();
	SortedSet<KalturaItem> items = getItemList(sessionMap);
	for (KalturaItem item : items) {
	    if (item != null) {
		// This flushs user UID info to message if this user is a new user.
		item.setCreatedBy(kalturaUser);
		itemList.add(item);
	    }
	}
	kaltura.setKalturaItems(itemList);

	// delete KalturaItems from database.
	for (KalturaItem item : getDeletedItemList(sessionMap)) {
	    if (item.getUid() != null) {
		kalturaService.deleteKalturaItem(item.getUid());
	    }
	}

	// set the update date
	kaltura.setUpdateDate(new Date());

	// releasing defineLater flag so that learner can start using the tool.
	kaltura.setDefineLater(false);

	kalturaService.saveOrUpdateKaltura(kaltura);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// add the sessionMapID to form
	authForm.setSessionMapID(sessionMap.getSessionID());

	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP, sessionMap);

	return mapping.findForward("success");
    }

    /**
     * Stores uploaded entryId(s).
     */
    public ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<KalturaItem> itemList = getItemList(sessionMap);
	KalturaItem item = new KalturaItem();
	item.setCreateDate(new Timestamp(new Date().getTime()));
	int maxSeq = 1;
	if (itemList != null && itemList.size() > 0) {
	    KalturaItem last = itemList.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	item.setSequenceId(maxSeq);
	itemList.add(item);

	String title = WebUtil.readStrParam(request, KalturaConstants.PARAM_ITEM_TITLE, true);
	if (StringUtils.isBlank(title)) {
	    String itemLocalized = kalturaService.getLocalisedMessage("label.authoring.item", null);
	    title = itemLocalized + " " + maxSeq;
	}
	item.setTitle(title);

	int duration = WebUtil.readIntParam(request, KalturaConstants.PARAM_ITEM_DURATION);
	item.setDuration(duration);

	String entryId = WebUtil.readStrParam(request, KalturaConstants.PARAM_ITEM_ENTRY_ID);
	if (StringUtils.isBlank(entryId)) {
	    String errorMsg = "Add item failed due to missing entityId (received from Kaltura server).";
	    logger.error(errorMsg);
	    throw new KalturaException(errorMsg);
	}
	item.setEntryId(entryId);

	item.setCreateByAuthor(true);
	item.setHidden(false);

	return mapping.findForward(KalturaConstants.ITEM_LIST);
    }

    /**
     * Preview uploaded entryId.
     */
    public ActionForward preview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(KalturaConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<KalturaItem> itemList = getItemList(sessionMap);
	    List<KalturaItem> rList = new ArrayList<KalturaItem>(itemList);
	    KalturaItem item = rList.get(itemIdx);
	    request.setAttribute(KalturaConstants.ATTR_ITEM, item);
	}

	return mapping.findForward(KalturaConstants.PREVIEW);
    }

    /**
     * Remove kaltura item from HttpSession list and update page display. As authoring rule, all persist only happen
     * when user submit whole page. So this remove is just impact HttpSession values.
     */
    public ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(KalturaConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<KalturaItem> itemList = getItemList(sessionMap);
	    List<KalturaItem> rList = new ArrayList<KalturaItem>(itemList);
	    KalturaItem item = rList.remove(itemIdx);
	    itemList.clear();
	    itemList.addAll(rList);
	    // add to delList
	    List<KalturaItem> delList = getDeletedItemList(sessionMap);
	    delList.add(item);
	}

	return mapping.findForward(KalturaConstants.ITEM_LIST);
    }

    /**
     * Move up current item.
     */
    public ActionForward upItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(mapping, request, true);
    }

    /**
     * Move down current item.
     */
    public ActionForward downItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(mapping, request, false);
    }

    private ActionForward switchItem(ActionMapping mapping, HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(KalturaConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<KalturaItem> kalturaList = getItemList(sessionMap);
	    List<KalturaItem> rList = new ArrayList<KalturaItem>(kalturaList);
	    // get current and the target item, and switch their sequnece
	    KalturaItem item = rList.get(itemIdx);
	    KalturaItem repItem;
	    if (up) {
		repItem = rList.get(--itemIdx);
	    } else {
		repItem = rList.get(++itemIdx);
	    }
	    int upSeqId = repItem.getSequenceId();
	    repItem.setSequenceId(item.getSequenceId());
	    item.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    kalturaList.clear();
	    kalturaList.addAll(rList);
	}

	return mapping.findForward(KalturaConstants.ITEM_LIST);
    }

    /**
     * Retrieve the SessionMap from the HttpSession.
     */
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request, AuthoringForm authForm) {
	return (SessionMap<String, Object>) request.getSession().getAttribute(authForm.getSessionMapID());
    }

    /**
     * List save current taskList items.
     */
    private SortedSet<KalturaItem> getItemList(SessionMap<String, Object> sessionMap) {
	SortedSet<KalturaItem> list = (SortedSet<KalturaItem>) sessionMap.get(KalturaConstants.ATTR_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<KalturaItem>(new KalturaItemComparator());
	    sessionMap.put(KalturaConstants.ATTR_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted taskList items, which could be persisted or non-persisted items.
     */
    private List<KalturaItem> getDeletedItemList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, KalturaConstants.ATTR_DELETED_ITEM_LIST);
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
}
