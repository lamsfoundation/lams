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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.peerreview.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.tool.peerreview.service.PeerreviewApplicationException;
import org.lamsfoundation.lams.tool.peerreview.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {

	String param = mapping.getParameter();
	// -----------------------Peerreview Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("getUsers")) {
	    return getUsers(mapping, form, request, response);
	}
	if (param.equals("showResults")) {
	    return showResults(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(PeerreviewConstants.ERROR);
    }

    /**
     * Read peerreview data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     * 
     * @throws IOException
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	IPeerreviewService service = getPeerreviewService();

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));

	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(PeerreviewConstants.PARAM_TOOL_SESSION_ID, sessionId);

	// If user already exists go straight to the normal screen, otherwise go to a refresh screen
	// until the user is created. The user will be created by the UserCreateThread(), which should
	// always be run as even if this user exists, others may have been added to the lesson/group
	// and need to be included for this user. If it is an update, the user won't see them this time
	// but they will if they choose to refresh the activity.
	PeerreviewUser user = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // peerreviewUser may be null if the user was force completed.
	    user = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(service, sessionId);
	}

	try {
	    Thread t = new Thread(new UserCreateThread(sessionId, service));
	    t.start();
	} catch (Throwable e) {
	    throw new IOException(e);
	}

	if (user == null) {
	    // goto refresh screen TODO create a specialised page
	    request.setAttribute(PeerreviewConstants.ATTR_CREATING_USERS, "true");
	    return mapping.findForward("defineLater");
	} else {
	    // goto standard screen
	    return startRating(mapping, form, request, response, service, sessionMap, sessionId, user, mode);
	}

    }

    private class UserCreateThread implements Runnable {
	private Long toolSessionId;
	private IPeerreviewService service;

	private Logger log = Logger.getLogger(UserCreateThread.class);

	public UserCreateThread(Long toolSessionId, IPeerreviewService service) {
	    this.toolSessionId = toolSessionId;
	    this.service = service;
	}

	@Override
	public void run() {
	    try {
		service.createUsersFromLesson(toolSessionId);
	    } catch (Throwable e) {
		String message = e.getMessage() != null ? e.getMessage() : e.getClass().getName();
		this.log.error(
			"Exception thrown creating Peer Review users for session " + toolSessionId + ": " + message, e);
		e.printStackTrace();
	    }
	}
    } // end Thread class

    /**
     * Read peerreview data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     * 
     * @throws IOException
     *
     */
    private ActionForward startRating(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, IPeerreviewService service, SessionMap sessionMap, Long sessionId,
	    PeerreviewUser user, ToolAccessMode mode) throws IOException {

	Peerreview peerreview = service.getPeerreviewBySessionId(sessionId);

	// get notebook entry
	String entryText = new String();
	NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		PeerreviewConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	if (notebookEntry != null) {
	    entryText = notebookEntry.getEntry();
	}

	// basic information
	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW, peerreview);
	sessionMap.put(PeerreviewConstants.ATTR_TITLE, peerreview.getTitle());
	sessionMap.put(PeerreviewConstants.ATTR_RESOURCE_INSTRUCTION, peerreview.getInstructions());
	sessionMap.put(PeerreviewConstants.ATTR_LOCK_ON_FINISH, peerreview.getLockWhenFinished());
	sessionMap.put(PeerreviewConstants.ATTR_USER, user);

	sessionMap.put(PeerreviewConstants.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(PeerreviewConstants.ATTR_REFLECTION_ON, peerreview.isReflectOnActivity());
	sessionMap.put(PeerreviewConstants.ATTR_REFLECTION_INSTRUCTION, peerreview.getReflectInstructions());
	sessionMap.put(PeerreviewConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (peerreview.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// handle rating criterias
	boolean isCommentsEnabled = service.isCommentsEnabled(peerreview.getContentId());
	sessionMap.put("isCommentsEnabled", isCommentsEnabled);

	int commentsMinWordsLimit = service.getCommentsMinWordsLimit(peerreview.getContentId());
	sessionMap.put("commentsMinWordsLimit", commentsMinWordsLimit);

	// store how many items are rated
	int countRatedUsers = service.getCountItemsRatedByUser(peerreview.getContentId(), user.getUserId().intValue());
	sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedUsers);

	// set contentInUse flag to true!
	peerreview.setContentInUse(true);
	peerreview.setDefineLater(false);
	service.saveOrUpdatePeerreview(peerreview);

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	//markUser as not Finished if it's redo
	boolean isRedo = WebUtil.readBooleanParam(request, "isRedo", false);
	if (!mode.isTeacher() && !peerreview.getLockWhenFinished() && isRedo && user.isSessionFinished()) {
	    user.setSessionFinished(false);
	    service.createUser(user);
	}
	sessionMap.put("isDisabled",
		peerreview.getLockWhenFinished() && user.isSessionFinished() || (mode != null) && mode.isTeacher());
	sessionMap.put(PeerreviewConstants.ATTR_USER_FINISHED, user.isSessionFinished());
	sessionMap.put("isSessionCompleted", user.getSession().getStatus() == PeerreviewConstants.COMPLETED);

	// show results page
	if (user.isSessionFinished() && peerreview.isShowRatingsLeftForUser()) {

	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("showResults"));
	    redirect.addParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    return redirect;

	} else {
	    return mapping.findForward(PeerreviewConstants.SUCCESS);
	}

    }

    /**
     * Displays page with user's ratings left for others and ratings others left for him.
     */
    private ActionForward showResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IPeerreviewService service = getPeerreviewService();

	// get back SessionMap
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(PeerreviewConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);
	Peerreview peerreview = (Peerreview) sessionMap.get(PeerreviewConstants.ATTR_PEERREVIEW);
	PeerreviewUser user = (PeerreviewUser) sessionMap.get(PeerreviewConstants.ATTR_USER);

	//markUserFinished if it hasn't been done previously
	if (!mode.isTeacher() && !user.isSessionFinished()) {
	    service.markUserFinished(sessionId, user.getUserId());
	    sessionMap.put(PeerreviewConstants.ATTR_USER_FINISHED, true);
	}

	// ratings left by the user
	List<Long> itemIds = new LinkedList<Long>();
	List<PeerreviewUser> sessionUsers = service.getUsersBySession(sessionId);
	for (PeerreviewUser userIter : sessionUsers) {
	    //excempt user himself
	    if (!user.getUserId().equals(userIter.getUserId())) {
		itemIds.add(userIter.getUserId());
	    }
	}

	//filter out not rated by user
	List<ItemRatingDTO> itemRatingDtos = service.getRatingCriteriaDtos(peerreview.getContentId(), itemIds, false,
		user.getUserId());
	List<ItemRatingDTO> ratedByUser = new ArrayList<ItemRatingDTO>();
	for (ItemRatingDTO itemRatingDto : itemRatingDtos) {
	    boolean isRatedByUser = itemRatingDto.getCommentPostedByUser() != null;

	    for (ItemRatingCriteriaDTO criteriaDto : itemRatingDto.getCriteriaDtos()) {
		if (StringUtils.isNotBlank(criteriaDto.getUserRating())) {
		    isRatedByUser = true;
		}
	    }

	    if (isRatedByUser) {
		ratedByUser.add(itemRatingDto);
	    }
	}
	request.setAttribute("itemRatingDtos", ratedByUser);

	// ratings left by others for this user
	List<Long> userIdList = Collections.singletonList(user.getUserId());
	List<ItemRatingDTO> userRatingDtos = service.getRatingCriteriaDtos(peerreview.getContentId(), userIdList, true,
		user.getUserId());
	ItemRatingDTO userRatingDto = null;
	if (userRatingDtos.size() == 1) {
	    userRatingDto = userRatingDtos.get(0);
	}
	request.setAttribute("itemRatingDto", userRatingDto);

	//user name map
	HashMap<Long, String> userNameMap = new HashMap<Long, String>();
	for (PeerreviewUser userIter : sessionUsers) {
	    userNameMap.put(userIter.getUserId(), userIter.getFirstName() + " " + userIter.getLastName());
	}
	request.setAttribute("userNameMap", userNameMap);

	// check whether finish lock is enabled
	sessionMap.put(PeerreviewConstants.ATTR_FINISH_LOCK, peerreview.getLockWhenFinished());

	// store how many items are rated
	int countRatedUsers = service.getCountItemsRatedByUser(peerreview.getContentId(), user.getUserId().intValue());
	sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedUsers);

	return mapping.findForward(PeerreviewConstants.SUCCESS);
    }

    /**
     * Refreshes user list.
     */
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	IPeerreviewService service = getPeerreviewService();

	// get back SessionMap
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Peerreview peerreview = (Peerreview) sessionMap.get(PeerreviewConstants.ATTR_PEERREVIEW);

	Long toolContentId = WebUtil.readLongParam(request, "toolContentId");
	Long toolSessionId = WebUtil.readLongParam(request, "toolSessionId");

	// in case of monitoring we show all results. in case of learning - don't show results from the current user
	boolean isMonitoring = WebUtil.readBooleanParam(request, "isMonitoring", false);
	Long userId = isMonitoring ? -1 : WebUtil.readLongParam(request, "userId");

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);

	int sorting = PeerreviewConstants.SORT_BY_NO;
	if (isSort1 != null && isSort1.equals(0)) {
	    sorting = PeerreviewConstants.SORT_BY_USERNAME_ASC;
	} else if (isSort1 != null && isSort1.equals(1)) {
	    sorting = PeerreviewConstants.SORT_BY_USERNAME_DESC;
	}

	List<PeerreviewUser> users = service.getUsersForTablesorter(toolSessionId, userId, page, size, sorting);

	JSONObject responcedata = new JSONObject();
	JSONArray rows = new JSONArray();

	responcedata.put("total_rows", service.getCountUsersBySession(toolSessionId, userId));

	// handle rating criterias
	List<ItemRatingDTO> itemRatingDtos = null;
	if (!users.isEmpty()) {
	    // create itemIds list
	    List<Long> itemIds = new LinkedList<Long>();
	    for (PeerreviewUser user : users) {
		itemIds.add(user.getUserId());
	    }

	    // all comments required only for monitoring
	    boolean isCommentsByOtherUsersRequired = isMonitoring;
	    boolean isCountUsersRatedEachItem = peerreview.getMaximumRatesPerUser() != 0;
	    itemRatingDtos = service.getRatingCriteriaDtos(toolContentId, itemIds, isCommentsByOtherUsersRequired,
		    userId, isCountUsersRatedEachItem);

	    // store how many items are rated
	    int countRatedQuestions = service.getCountItemsRatedByUser(toolContentId, userId.intValue());
	    responcedata.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedQuestions);
	}

	for (PeerreviewUser user : users) {

	    JSONObject userRow = new JSONObject();
	    userRow.put("userId", user.getUserId().toString());
	    userRow.put("userName", StringEscapeUtils.escapeCsv(user.getFirstName() + " " + user.getLastName()));

	    // find corresponding itemRatingDto
	    ItemRatingDTO itemRatingDto = null;
	    for (ItemRatingDTO itemRatingDtoIter : itemRatingDtos) {
		if (user.getUserId().equals(itemRatingDtoIter.getItemId())) {
		    itemRatingDto = itemRatingDtoIter;
		    break;
		}
	    }
	    userRow.put("ratesPerUser", itemRatingDto.getCountUsersRatedEachItem());

	    JSONArray criteriasRows = new JSONArray();
	    for (ItemRatingCriteriaDTO criteriaDto : itemRatingDto.getCriteriaDtos()) {
		JSONObject criteriasRow = new JSONObject();
		criteriasRow.put("ratingCriteriaId", criteriaDto.getRatingCriteria().getRatingCriteriaId());
		criteriasRow.put("title", StringEscapeUtils.escapeHtml(criteriaDto.getRatingCriteria().getTitle()));
		criteriasRow.put("averageRating", criteriaDto.getAverageRating());
		criteriasRow.put("numberOfVotes", criteriaDto.getNumberOfVotes());
		criteriasRow.put("userRating", criteriaDto.getUserRating());

		criteriasRows.put(criteriasRow);
	    }
	    userRow.put("criteriaDtos", criteriasRows);

	    // handle comments
	    userRow.put("commentsCriteriaId", itemRatingDto.getCommentsCriteriaId());
	    String commentPostedByUser = itemRatingDto.getCommentPostedByUser() == null ? ""
		    : itemRatingDto.getCommentPostedByUser().getComment();
	    userRow.put("commentPostedByUser", StringEscapeUtils.escapeCsv(commentPostedByUser));
	    if (itemRatingDto.getCommentDtos() != null) {
		JSONArray comments = new JSONArray();
		for (RatingCommentDTO commentDto : itemRatingDto.getCommentDtos()) {
		    comments.put(StringEscapeUtils.escapeCsv(commentDto.getComment()));
		}
		userRow.put("comments", comments);
	    }

	    rows.put(userRow);
	}
	responcedata.put("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    /**
     * Finish learning session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);

	IPeerreviewService service = getPeerreviewService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(PeerreviewConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (PeerreviewApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(PeerreviewConstants.SUCCESS);
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
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, PeerreviewConstants.ATTR_SESSION_MAP_ID);

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IPeerreviewService service = getPeerreviewService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = service.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		PeerreviewConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(PeerreviewConstants.SUCCESS);
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
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(PeerreviewConstants.PARAM_TOOL_SESSION_ID);

	IPeerreviewService service = getPeerreviewService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		PeerreviewConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    PeerreviewConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    private IPeerreviewService getPeerreviewService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IPeerreviewService) wac.getBean(PeerreviewConstants.PEERREVIEW_SERVICE);
    }

    private PeerreviewUser getCurrentUser(IPeerreviewService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);
    }

    private PeerreviewUser getSpecifiedUser(IPeerreviewService service, Long sessionId, Integer userId) {
	PeerreviewUser peerreviewUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (peerreviewUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for peerreview activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return peerreviewUser;
    }

}
