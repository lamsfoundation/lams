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
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.kaltura.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.kaltura.model.Kaltura;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaComment;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaItem;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaSession;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaUser;
import org.lamsfoundation.lams.tool.kaltura.service.IKalturaService;
import org.lamsfoundation.lams.tool.kaltura.service.KalturaServiceProxy;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaCommentComparator;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaConstants;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaException;
import org.lamsfoundation.lams.tool.kaltura.util.KalturaItemComparator;
import org.lamsfoundation.lams.tool.kaltura.web.forms.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
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
 *
 *
 *
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private IKalturaService service;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initKalturaService();

	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	String sessionMapId = sessionMap.getSessionID();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);
	sessionMap.put(AttributeNames.PARAM_MODE, mode);
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	// Retrieve the session and content.
	KalturaSession kalturaSession = service.getSessionBySessionId(toolSessionId);
	if (kalturaSession == null) {
	    throw new KalturaException("Cannot retrieve session with toolSessionID" + toolSessionId);
	}
	Kaltura kaltura = kalturaSession.getKaltura();
	//init getCreatedBy to avoid session close error in proxy object
	if (kaltura.getCreatedBy() != null) {
	    kaltura.getCreatedBy().getLoginName();
	}

	//if this parameter true - this request is sent when Monitoring Group
	boolean isGroupMonitoring = WebUtil.readBooleanParam(request, KalturaConstants.ATTR_IS_GROUP_MONITORING, false);
	sessionMap.put(KalturaConstants.ATTR_IS_GROUP_MONITORING, isGroupMonitoring);

	KalturaUser user;
	if (mode.equals(ToolAccessMode.TEACHER) && !isGroupMonitoring) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = service.getUserByUserIdAndSessionId(userID, toolSessionId);
	} else {
	    //in case of lerning and group monitoring create new user
	    user = getCurrentUser(toolSessionId);
	}

	// check defineLater
	if (kaltura.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// Set the content in use flag.
	if (!kaltura.isContentInUse()) {
	    kaltura.setContentInUse(new Boolean(true));
	    service.saveOrUpdateKaltura(kaltura);
	}

	ActivityPositionDTO activityPosition = WebUtil.putActivityPositionInRequestByToolSessionId(toolSessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// reflection information
	String entryText = new String();
	NotebookEntry notebookEntry = service.getEntry(toolSessionId, user.getUserId().intValue());
	if (notebookEntry != null) {
	    entryText = notebookEntry.getEntry();
	}
	sessionMap.put(KalturaConstants.ATTR_REFLECTION_ON, kaltura.isReflectOnActivity());
	sessionMap.put(KalturaConstants.ATTR_REFLECTION_INSTRUCTION, kaltura.getReflectInstructions());
	sessionMap.put(KalturaConstants.ATTR_REFLECTION_ENTRY, entryText);

	// check whehter finish lock is on/off
	boolean lockOnFinish = kaltura.isLockOnFinished() && user != null && user.isFinishedActivity();
	sessionMap.put(KalturaConstants.ATTR_FINISHED_LOCK, lockOnFinish);
	sessionMap.put(KalturaConstants.ATTR_LOCK_ON_FINISH, kaltura.isLockOnFinished());
	sessionMap.put(KalturaConstants.ATTR_USER_FINISHED, user != null && user.isFinishedActivity());

	// date and time restriction
	Date submissionDeadline = kaltura.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    sessionMap.put(KalturaConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}

	sessionMap.put(KalturaConstants.ATTR_KALTURA, kaltura);
	sessionMap.put(AttributeNames.USER, user);

	TreeSet<KalturaItem> items = getItems(mode, kaltura, toolSessionId, user.getUserId());
	//skip the next parameter if the tool doesn't contain videos
	if (items.isEmpty()) {
	    KalturaItem item = new KalturaItem();
	    item.setUid(new Long(-1));
	    sessionMap.put(KalturaConstants.ATTR_ITEM, item);
	    sessionMap.put(KalturaConstants.ATTR_IS_ALLOW_UPLOADS, isAllowUpload(sessionMap, items));
	    return mapping.findForward(KalturaConstants.SUCCESS);
	}

	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(KalturaConstants.VIEW_ITEM));
	redirect.addParameter(KalturaConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	redirect.addParameter(KalturaConstants.PARAM_ITEM_UID, items.first().getUid());
	return redirect;
    }

    public ActionForward viewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initKalturaService();

	// get back SessionMap
	String sessionMapID = request.getParameter(KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	KalturaUser user = (KalturaUser) sessionMap.get(AttributeNames.USER);
	Long userId = user.getUserId();
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Kaltura kaltura = (Kaltura) sessionMap.get(KalturaConstants.ATTR_KALTURA);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);

	//get back item
	Long itemUid = WebUtil.readLongParam(request, KalturaConstants.PARAM_ITEM_UID);
	KalturaItem item = service.getKalturaItem(itemUid);
	if (mode.isLearner()) {
	    service.logItemWatched(item.getUid(), userId, toolSessionId);
	}

	//items from DB
	TreeSet<KalturaItem> items = getItems(mode, kaltura, toolSessionId, userId);

	//filter comments and store current item comments to sessionMap
	if (kaltura.isAllowComments()) {

	    //filter out comments from the other groups. We need this to display number of comments
	    for (KalturaItem dbItem : items) {
		Set<KalturaComment> groupComments = getGroupComments(dbItem, toolSessionId, mode);
		//set to groupComments to avoid session flushing changes to DB
		dbItem.setGroupComments(groupComments);
	    }
	}

	if (kaltura.isAllowRatings()) {
	    boolean isUserItemAuthor = (item.getCreatedBy() == null) && (kaltura.getCreatedBy() != null)
		    && kaltura.getCreatedBy().getUserId().equals(userId)
		    || (item.getCreatedBy() != null) && item.getCreatedBy().equals(user);
	    sessionMap.put(KalturaConstants.ATTR_IS_USER_ITEM_AUTHOR, isUserItemAuthor);

	    for (KalturaItem dbItem : items) {
		AverageRatingDTO averageRatingDto = service.getAverageRatingDto(dbItem.getUid(), toolSessionId);
		dbItem.setAverageRatingDto(averageRatingDto);
	    }
	}

	sessionMap.put(KalturaConstants.ATTR_IS_ALLOW_UPLOADS, isAllowUpload(sessionMap, items));

	//refresh items
	items.remove(item);
	sessionMap.put(KalturaConstants.ATTR_ITEM, item);
	sessionMap.put(KalturaConstants.ATTR_ITEMS, items);

	return mapping.findForward(KalturaConstants.SUCCESS);
    }

    /**
     * Stores uploaded entryId(s).
     */
    public ActionForward saveNewItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	initKalturaService();

	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	KalturaUser user = (KalturaUser) sessionMap.get(AttributeNames.USER);
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	KalturaSession kalturaSession = service.getSessionBySessionId(toolSessionId);
	Kaltura kaltura = kalturaSession.getKaltura();
	TreeSet<KalturaItem> allItems = new TreeSet<KalturaItem>(new KalturaItemComparator());
	allItems.addAll(kaltura.getKalturaItems());

	// check user can upload item
	boolean isAllowUpload = isAllowUpload(sessionMap, allItems);
	if (!isAllowUpload) {
	    return null;
	}

	KalturaItem item = new KalturaItem();
	item.setCreateDate(new Timestamp(new Date().getTime()));
	int maxSeq = 1;
	if (allItems != null && allItems.size() > 0) {
	    KalturaItem last = allItems.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	item.setSequenceId(maxSeq);

	String title = WebUtil.readStrParam(request, KalturaConstants.PARAM_ITEM_TITLE, true);
	if (StringUtils.isBlank(title)) {
	    String itemLocalized = service.getLocalisedMessage("label.authoring.item", null);
	    title = itemLocalized + " " + maxSeq;
	}
	item.setTitle(title);

	int duration = WebUtil.readIntParam(request, KalturaConstants.PARAM_ITEM_DURATION);
	item.setDuration(duration);

	String entryId = WebUtil.readStrParam(request, KalturaConstants.PARAM_ITEM_ENTRY_ID);
	if (StringUtils.isBlank(entryId)) {
	    String errorMsg = "Add item failed due to missing entityId (received from Kaltura server).";
	    log.error(errorMsg);
	    throw new KalturaException(errorMsg);
	}
	item.setEntryId(entryId);

	item.setCreatedBy(user);
	item.setCreateByAuthor(false);
	item.setHidden(false);
	item.setKalturaUid(kaltura.getUid());
	service.saveKalturaItem(item);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(KalturaConstants.PARAM_ITEM_UID, item.getUid());
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * Comment current item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward commentItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initKalturaService();
	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(KalturaConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	String commentMessage = WebUtil.readStrParam(request, KalturaConstants.ATTR_COMMENT, true);

	if (StringUtils.isBlank(commentMessage)) {
	    ActionErrors errors = new ActionErrors();
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(KalturaConstants.ERROR_MSG_COMMENT_BLANK));
	    this.addErrors(request, errors);
	    return mapping.findForward(KalturaConstants.COMMENT_LIST);
	}

	KalturaComment comment = new KalturaComment();
	comment.setComment(commentMessage);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	KalturaUser kalturaUser = service.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);
	comment.setCreateBy(kalturaUser);
	comment.setCreateDate(new Timestamp(new Date().getTime()));

	// persist Kaltura changes in DB
	Long currentItemUid = new Long(request.getParameter(KalturaConstants.PARAM_ITEM_UID));
	KalturaItem item = service.getKalturaItem(currentItemUid);
	Set<KalturaComment> dbComments = item.getComments();
	dbComments.add(comment);
	service.saveKalturaItem(item);

	//refresh comments in the sessisonMap
	KalturaItem sessionMapItem = (KalturaItem) sessionMap.get(KalturaConstants.ATTR_ITEM);
	Set<KalturaComment> groupComments = getGroupComments(item, toolSessionId, mode);
	sessionMapItem.setGroupComments(groupComments);

	return mapping.findForward(KalturaConstants.COMMENT_LIST);
    }

    /**
     * Rates items submitted by other learners.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward rateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	initKalturaService();
	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	KalturaUser user = (KalturaUser) sessionMap.get(AttributeNames.USER);

	float rating = Float.parseFloat(request.getParameter("rate"));
	Long itemUid = WebUtil.readLongParam(request, "idBox");

	AverageRatingDTO averageRatingDto = service.rateMessage(itemUid, user.getUserId(), toolSessionId, rating);

	//refresh averageRatingDto in sessionMap
	KalturaItem item = (KalturaItem) sessionMap.get(KalturaConstants.ATTR_ITEM);
	item.setAverageRatingDto(averageRatingDto);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put("averageRating", averageRatingDto.getRating());
	JSONObject.put("numberOfVotes", averageRatingDto.getNumberOfVotes());
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * Display empty reflection form.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	initKalturaService();

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = service.getEntry(toolSessionID, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(KalturaConstants.NOTEBOOK);
    }

    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initKalturaService();

	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL, KalturaConstants.TOOL_SIGNATURE,
		    userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initKalturaService();

	// get back SessionMap
	String sessionMapID = request.getParameter(KalturaConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(userDTO.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(KalturaConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (KalturaException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(KalturaConstants.FINISH);
    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************

    /**
     * Gets items from the DB. IF the mode is learner filters them based on the group sessionId
     */
    private TreeSet<KalturaItem> getItems(ToolAccessMode mode, Kaltura kaltura, Long toolSessionId, Long userId) {

	// Create set of images, along with this filtering out items added by users from other groups
	TreeSet<KalturaItem> items = new TreeSet<KalturaItem>(new KalturaItemComparator());
	items.addAll(service.getGroupItems(kaltura.getToolContentId(), toolSessionId, userId, mode.isTeacher()));

	for (KalturaItem item : items) {
	    // initialize login name to avoid session close error in proxy object
	    if (item.getCreatedBy() != null) {
		item.getCreatedBy().getLoginName();
	    }
	}

	return items;
    }

    /**
     * Checks whether further upload is allowed.
     */
    private boolean isAllowUpload(SessionMap<String, Object> sessionMap, Set<KalturaItem> items) {
	Kaltura kaltura = (Kaltura) sessionMap.get(KalturaConstants.ATTR_KALTURA);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	KalturaUser user = (KalturaUser) sessionMap.get(AttributeNames.USER);

	boolean isAllowUpload = false;
	if (kaltura.isAllowContributeVideos() && !mode.equals(ToolAccessMode.TEACHER)) {
	    int numberOfUploadedItems = 0;
	    for (KalturaItem item : items) {
		if (item.getCreatedBy() != null && item.getCreatedBy().equals(user)) {
		    numberOfUploadedItems++;
		}
	    }
	    int learnerContributionLimit = kaltura.getLearnerContributionLimit();
	    isAllowUpload = (learnerContributionLimit == Kaltura.TYPE_LEARNER_CONTRIBUTION_LIMIT_UNLIMITED)
		    || (numberOfUploadedItems < kaltura.getLearnerContributionLimit());
	}

	return isAllowUpload;
    }

    /**
     * Returns all comments done by teacher and learners of the specified group.
     */
    private Set<KalturaComment> getGroupComments(KalturaItem item, Long sessionId, ToolAccessMode mode) {
	TreeSet<KalturaComment> comments = new TreeSet<KalturaComment>(new KalturaCommentComparator());
	Set<KalturaComment> itemComments = item.getComments();

	//only authored items can be seen by different groups
	if (item.isCreateByAuthor()) {
	    for (KalturaComment comment : itemComments) {

		//skip hidden comments
		if (comment.isHidden() && mode.isLearner()) {
		    continue;

		    // if made by teacher - add
		} else if (comment.getCreateBy() == null) {
		    comments.add(comment);

		    // if made by learner of the specified group - also add
		} else if (comment.getCreateBy().getSession().getSessionId().equals(sessionId)) {
		    comments.add(comment);
		}
	    }

	} else {
	    comments.addAll(itemComments);
	}

	return comments;
    }

    private void initKalturaService() {
	if (service == null) {
	    service = KalturaServiceProxy.getKalturaService(this.getServlet().getServletContext());
	}
    }

    private KalturaUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	KalturaUser kalturaUser = service.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (kalturaUser == null) {
	    KalturaSession kalturaSession = service.getSessionBySessionId(toolSessionId);
	    kalturaUser = service.createKalturaUser(user, kalturaSession);
	}

	return kalturaUser;
    }
}
