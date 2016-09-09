/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.comments.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.comments.Comment;
import org.lamsfoundation.lams.comments.CommentConstants;
import org.lamsfoundation.lams.comments.CommentLike;
import org.lamsfoundation.lams.comments.dto.CommentDTO;
import org.lamsfoundation.lams.comments.service.ICommentService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.GroupedToolSession;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Fiona Malikoff
 */
public class CommentAction extends Action {

    private static Logger log = Logger.getLogger(CommentAction.class);

    private static IUserManagementService userService;
    private static ICommentService commentService;
    private static ILamsCoreToolService coreToolService;
    private static ISecurityService securityService;

    @Override
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String param = mapping.getParameter();
	if (param.equals("init")) {
	    return init(mapping, form, request, response);
	}
	if (param.equals("viewTopic")) {
	    return viewTopic(mapping, form, request, response);
	}
	if (param.equals("viewTopicThread")) {
	    return viewTopicThread(mapping, form, request, response);
	}

	if (param.equals("newComment")) {
	    return newComment(mapping, form, request, response);
	}

	if (param.equals("newReplyTopic")) {
	    return newReplyTopic(mapping, form, request, response);
	}
	if (param.equals("replyTopicInline")) {
	    return replyTopicInline(mapping, form, request, response);
	}

	if (param.equals("editTopic")) {
	    return editTopic(mapping, form, request, response);
	}
	if (param.equals("updateTopicInline")) {
	    return updateTopicInline(mapping, form, request, response);
	}

	if (param.equals("like")) {
	    return updateLikeCount(mapping, form, request, response, true);
	}
	if (param.equals("dislike")) {
	    return updateLikeCount(mapping, form, request, response, false);
	}
	if (param.equals("hide")) {
	    return hideComment(mapping, form, request, response, false);
	}
	if (param.equals("makeSticky")) {
	    return makeSticky(mapping, form, request, response);
	}

	return mapping.findForward("error");
    }

    /**
     * Display the comments for a given external id (usually tool session id). The session comments will be
     * arranged by Tree structure and loaded thread by thread (with paging).
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    private ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// initial Session Map
	String sessionMapID = request.getParameter(CommentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap;

	Long externalId;
	int externalType;
	String externalSignature;
	String mode;
	boolean likeAndDislike;
	boolean readOnly;
	Integer pageSize;
	Integer sortBy;

	// refresh forum page, not initial enter
	if (sessionMapID != null) {
	    sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	    externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	    externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	    externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);
	    mode = (String) sessionMap.get(AttributeNames.ATTR_MODE);
	    pageSize = (Integer) sessionMap.get(CommentConstants.PAGE_SIZE);
	    sortBy = (Integer) sessionMap.get(CommentConstants.ATTR_SORT_BY);

	} else {
	    sessionMap = new SessionMap<String, Object>();
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	    externalId = WebUtil.readLongParam(request, CommentConstants.ATTR_EXTERNAL_ID);
	    externalType = WebUtil.readIntParam(request, CommentConstants.ATTR_EXTERNAL_TYPE);
	    externalSignature = WebUtil.readStrParam(request, CommentConstants.ATTR_EXTERNAL_SIG);
	    likeAndDislike = WebUtil.readBooleanParam(request, CommentConstants.ATTR_LIKE_AND_DISLIKE);
	    readOnly = WebUtil.readBooleanParam(request, CommentConstants.ATTR_READ_ONLY);
	    pageSize = WebUtil.readIntParam(request, CommentConstants.PAGE_SIZE, true);
	    if (pageSize == null) {
		pageSize = CommentConstants.DEFAULT_PAGE_SIZE;
	    }
	    sortBy = WebUtil.readIntParam(request, CommentConstants.ATTR_SORT_BY, true);
	    if (sortBy == null) {
		sortBy = ICommentService.SORT_BY_DATE;
	    }

	    sessionMap.put(CommentConstants.ATTR_EXTERNAL_ID, externalId);
	    sessionMap.put(CommentConstants.ATTR_EXTERNAL_TYPE, externalType);
	    sessionMap.put(CommentConstants.ATTR_EXTERNAL_SIG, externalSignature);
	    sessionMap.put(CommentConstants.ATTR_LIKE_AND_DISLIKE, likeAndDislike);
	    sessionMap.put(CommentConstants.ATTR_READ_ONLY, readOnly);
	    sessionMap.put(CommentConstants.PAGE_SIZE, pageSize);
	    sessionMap.put(CommentConstants.ATTR_SORT_BY, sortBy);

	    mode = request.getParameter(AttributeNames.ATTR_MODE);
	    sessionMap.put(AttributeNames.ATTR_MODE, mode != null ? mode : ToolAccessMode.LEARNER.toString());
	}

	User user = getCurrentUser(request);
	if (externalType != Comment.EXTERNAL_TYPE_TOOL) {
	    throwException("Unknown comment type ", user.getLogin(), externalId, externalType, externalSignature);
	}

	Comment rootComment = getCommentService().createOrGetRoot(externalId, externalType, externalSignature, user);
	sessionMap.put(CommentConstants.ATTR_ROOT_COMMENT_UID, rootComment.getUid());
	return viewTopicImpl(mapping, form, request, response, sessionMap, pageSize, sortBy, true);
    }

    private void throwException(String msg, String loginName, Long externalId, Integer externalType,
	    String externalSignature) throws ServletException {
	String error = msg + " User " + loginName + " " + externalId + ":" + externalType + ":" + externalSignature;
	log.error(error);
	throw new ServletException(error);
    }

    private void throwException(String msg, String loginName) throws ServletException {
	String error = msg + " User " + loginName;
	log.error(error);
	throw new ServletException(error);
    }

    private boolean learnerInToolSession(Long toolSessionId, User user) {
	GroupedToolSession toolSession = (GroupedToolSession) getCoreToolService().getToolSessionById(toolSessionId);
	return toolSession.getSessionGroup().getUsers().contains(user);
    }

    private boolean monitorInToolSession(Long toolSessionId, User user, SessionMap<String, Object> sessionMap) {

	if (ToolAccessMode.TEACHER
		.equals(WebUtil.getToolAccessMode((String) sessionMap.get(AttributeNames.ATTR_MODE)))) {
	    GroupedToolSession toolSession = (GroupedToolSession) getCoreToolService()
		    .getToolSessionById(toolSessionId);
	    return getSecurityService().isLessonMonitor(toolSession.getLesson().getLessonId(), user.getUserId(),
		    "Comment Monitoring Tasks", false);
	} else {
	    return false;
	}
    }

    /**
     * Display the comments for a given external id (usually tool session id). The session comments will be
     * arranged by Tree structure and loaded thread by thread (with paging). This may set a new value of sort by, so
     * make sure the session is updated.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    private ActionForward viewTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, CommentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Integer pageSize = WebUtil.readIntParam(request, CommentConstants.PAGE_SIZE, true);
	Integer sortBy = WebUtil.readIntParam(request, CommentConstants.ATTR_SORT_BY, true);
	Boolean sticky = WebUtil.readBooleanParam(request, CommentConstants.ATTR_STICKY, false);
	if (sortBy != null) {
	    sessionMap.put(CommentConstants.ATTR_SORT_BY, sortBy);
	}
	return viewTopicImpl(mapping, form, request, response, sessionMap, pageSize, sortBy, sticky);

    }

    private ActionForward viewTopicImpl(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, SessionMap<String, Object> sessionMap, Integer pageSize, Integer sortBy,
	    boolean includeSticky) {

	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

	commentService = getCommentService();

	// get user and check they are in the tool session....
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long lastMsgSeqId = WebUtil.readLongParam(request, CommentConstants.PAGE_LAST_ID, true);
	String currentLikeCount = WebUtil.readStrParam(request, CommentConstants.ATTR_LIKE_COUNT, true);

	if (pageSize == null) {
	    pageSize = (Integer) sessionMap.get(CommentConstants.PAGE_SIZE);
	}
	if (sortBy == null) {
	    sortBy = (Integer) sessionMap.get(CommentConstants.ATTR_SORT_BY);
	}

	List<CommentDTO> msgDtoList = commentService.getTopicThread(externalId, externalType, externalSignature,
		lastMsgSeqId, pageSize, sortBy, currentLikeCount, user.getUserID());
	updateMessageFlag(msgDtoList, user.getUserID());
	request.setAttribute(CommentConstants.ATTR_COMMENT_THREAD, msgDtoList);

	if (includeSticky) {
	    List<CommentDTO> stickyList = commentService.getTopicStickyThread(externalId, externalType,
		    externalSignature, sortBy, currentLikeCount, user.getUserID());
	    updateMessageFlag(stickyList, user.getUserID());
	    request.setAttribute(CommentConstants.ATTR_STICKY, stickyList);
	}

	// transfer SessionMapID as well
	request.setAttribute(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	return mapping.findForward(includeSticky ? "successAll" : "success");
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapId = WebUtil.readStrParam(request, CommentConstants.ATTR_SESSION_MAP_ID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapId);
    }

    /**
     * Display the messages for a particular thread in a particular topic. Returns all messages for this thread - does
     * not need paging.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward viewTopicThread(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	commentService = getCommentService();

	Long highlightMessageUid = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID, true);
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	// get forum user and forum
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	Long threadId = WebUtil.readLongParam(request, CommentConstants.ATTR_THREAD_ID, true);
	Integer sortBy = WebUtil.readIntParam(request, CommentConstants.ATTR_SORT_BY, true);
	if (sortBy != null) {
	    sessionMap.put(CommentConstants.ATTR_SORT_BY, sortBy);
	}

	List<CommentDTO> msgDtoList = commentService.getThread(threadId, sortBy, user.getUserID());
	updateMessageFlag(msgDtoList, user.getUserID());
	request.setAttribute(CommentConstants.ATTR_COMMENT_THREAD, msgDtoList);

	if (highlightMessageUid != null) {
	    request.setAttribute(CommentConstants.ATTR_COMMENT_ID, highlightMessageUid);
	}
	// transfer SessionMapID as well
	request.setAttribute(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// don't want to try to scroll as this is a single thread, completely displayed.
	request.setAttribute(CommentConstants.ATTR_NO_MORE_PAGES, true);

	return mapping.findForward("success");
    }

    /**
     * This method will set the author and voted (has done like/dislike) flag in message DTO
     *
     * @param msgDtoList
     */
    private void updateMessageFlag(List<CommentDTO> msgDtoList, Integer currUserId) {
	Iterator<CommentDTO> iter = msgDtoList.iterator();
	while (iter.hasNext()) {
	    CommentDTO dto = iter.next();
	    Comment comment = dto.getComment();
	    if (comment.getCreatedBy() != null && currUserId.equals(comment.getCreatedBy().getUserId())) {
		dto.setIsAuthor(true);
	    } else {
		dto.setIsAuthor(false);
	    }
	}
    }

    /**
     * Create a new comment (not a reply)
     */
    private ActionForward newComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InterruptedException, JSONException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

	String commentText = request.getParameter(CommentConstants.ATTR_BODY);
	if (commentText != null) {
	    commentText = commentText.trim();
	}

	JSONObject JSONObject;

	if (!validateText(commentText)) {
	    JSONObject = getFailedValidationJSON();

	} else {

	    commentService = getCommentService();

	    User user = getCurrentUser(request);
	    if (!learnerInToolSession(externalId, user) && !monitorInToolSession(externalId, user, sessionMap)) {
		throwException("New comment: User does not have the rights to access the comments. ", user.getLogin(),
			externalId, externalType, externalSignature);
	    }

	    Comment rootSeq = commentService.getRoot(externalId, externalType, externalSignature);

	    // save message into database
	    Comment newComment = commentService.createReply(rootSeq, commentText, user);

	    JSONObject = new JSONObject();
	    JSONObject.put(CommentConstants.ATTR_COMMENT_ID, newComment.getUid());
	    JSONObject.put(CommentConstants.ATTR_THREAD_ID, newComment.getThreadComment().getUid());
	    JSONObject.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    JSONObject.put(CommentConstants.ATTR_PARENT_COMMENT_ID, newComment.getParent().getUid());

	}
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * Display replay topic page. Message form subject will include parent topics same subject.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newReplyTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute(CommentConstants.ATTR_SESSION_MAP_ID,
		request.getParameter(CommentConstants.ATTR_SESSION_MAP_ID));
	request.setAttribute(CommentConstants.ATTR_PARENT_COMMENT_ID,
		request.getParameter(CommentConstants.ATTR_PARENT_COMMENT_ID));
	return mapping.findForward("success");
    }

    /**
     * Create a reply to a parent topic.
     */
    private ActionForward replyTopicInline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InterruptedException, JSONException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

	Long parentId = WebUtil.readLongParam(request, CommentConstants.ATTR_PARENT_COMMENT_ID);
	String commentText = WebUtil.readStrParam(request, CommentConstants.ATTR_BODY, true);

	if (commentText != null) {
	    commentText = commentText.trim();
	}

	JSONObject JSONObject;

	if (!validateText(commentText)) {
	    JSONObject = getFailedValidationJSON();

	} else {

	    commentService = getCommentService();
	    User user = getCurrentUser(request);
	    if (!learnerInToolSession(externalId, user) && !monitorInToolSession(externalId, user, sessionMap)) {
		throwException("New comment: User does not have the rights to access the comments. ", user.getLogin(),
			externalId, externalType, externalSignature);
	    }

	    // save message into database
	    Comment newComment = commentService.createReply(parentId, commentText.trim(), user);

	    JSONObject = new JSONObject();
	    JSONObject.put(CommentConstants.ATTR_COMMENT_ID, newComment.getUid());
	    JSONObject.put(CommentConstants.ATTR_THREAD_ID, newComment.getThreadComment().getUid());
	    JSONObject.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    JSONObject.put(CommentConstants.ATTR_PARENT_COMMENT_ID, newComment.getParent().getUid());

	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    private boolean validateText(String commentText) {
	return commentText != null && commentText.length() > 0
		&& commentText.length() < CommentConstants.MAX_BODY_LENGTH;
    }

    private JSONObject getFailedValidationJSON() throws JSONException {
	MessageService msgService = getCommentService().getMessageService();
	JSONObject JSONObject = new JSONObject();
	JSONObject.put(CommentConstants.ATTR_ERR_MESSAGE, msgService.getMessage(CommentConstants.KEY_BODY_VALIDATION));
	return JSONObject;
    }

    /**
     * Display a editable form for a topic in order to update it.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward editTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long commentId = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);
	CommentDTO comment = getCommentService().getComment(commentId);

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	sessionMap.put(CommentConstants.ATTR_COMMENT_ID, commentId);
	request.setAttribute(CommentConstants.ATTR_COMMENT_ID, commentId);
	request.setAttribute(CommentConstants.ATTR_COMMENT, comment);
	request.setAttribute(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return mapping.findForward("success");
    }

    /**
     * Update a topic.
     *
     * @throws ServletException
     */
    public ActionForward updateTopicInline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException, ServletException {

	commentService = getCommentService();
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long commentId = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);

	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

	String commentText = request.getParameter(CommentConstants.ATTR_BODY);
	if (commentText != null) {
	    commentText = commentText.trim();
	}

	JSONObject JSONObject;

	if (!validateText(commentText)) {
	    JSONObject = getFailedValidationJSON();

	} else {

	    CommentDTO originalComment = commentService.getComment(commentId);

	    User user = getCurrentUser(request);
	    if (!originalComment.getComment().getCreatedBy().equals(user)
		    && !monitorInToolSession(externalId, user, sessionMap)) {
		throwException(
			"Update comment: User does not have the rights to update the comment " + commentId + ". ",
			user.getLogin(), externalId, externalType, externalSignature);
	    }

	    Comment updatedComment = commentService.updateComment(commentId, commentText, user, ToolAccessMode.TEACHER
		    .equals(WebUtil.getToolAccessMode((String) sessionMap.get(AttributeNames.ATTR_MODE))));

	    JSONObject = new JSONObject();
	    JSONObject.put(CommentConstants.ATTR_COMMENT_ID, commentId);
	    JSONObject.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    JSONObject.put(CommentConstants.ATTR_THREAD_ID, updatedComment.getThreadComment().getUid());
	    JSONObject.put(CommentConstants.ATTR_PARENT_COMMENT_ID, updatedComment.getParent().getUid());

	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * Update the likes/dislikes
     */
    private ActionForward updateLikeCount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, boolean isLike)
	    throws InterruptedException, JSONException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long messageUid = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);

	commentService = getCommentService();

	User user = getCurrentUser(request);
	if (!learnerInToolSession(externalId, user)) {
	    throwException(
		    "Update comment: User does not have the rights to like/dislike the comment " + messageUid + ". ",
		    user.getLogin(), externalId, (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE),
		    (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG));
	}

	boolean added = commentService.addLike(messageUid, user, isLike ? CommentLike.LIKE : CommentLike.DISLIKE);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(CommentConstants.ATTR_COMMENT_ID, messageUid);
	JSONObject.put(CommentConstants.ATTR_STATUS, added);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * Update hide flag
     */
    private ActionForward hideComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, boolean isLike)
	    throws InterruptedException, JSONException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long commentId = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	boolean status = WebUtil.readBooleanParam(request, CommentConstants.ATTR_HIDE_FLAG);

	commentService = getCommentService();

	User user = getCurrentUser(request);
	if (!monitorInToolSession(externalId, user, sessionMap)) {
	    throwException("Update comment: User does not have the rights to hide the comment " + commentId + ". ",
		    user.getLogin(), externalId, (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE),
		    (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG));
	}

	Comment updatedComment = commentService.hideComment(commentId, user, status);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(CommentConstants.ATTR_COMMENT_ID, updatedComment.getUid());
	JSONObject.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	JSONObject.put(CommentConstants.ATTR_THREAD_ID, updatedComment.getThreadComment().getUid());
	JSONObject.put(CommentConstants.ATTR_PARENT_COMMENT_ID, updatedComment.getParent().getUid());

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * Make a topic sticky - the topic should be level 1 only.
     *
     * @throws ServletException
     */
    public ActionForward makeSticky(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException, ServletException {

	commentService = getCommentService();
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long commentId = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);
	Boolean sticky = WebUtil.readBooleanParam(request, CommentConstants.ATTR_STICKY);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);

	CommentDTO originalComment = commentService.getComment(commentId);
	User user = getCurrentUser(request);

	if (!monitorInToolSession(externalId, user, sessionMap)) {
	    throwException(
		    "Make comment sticky: User does not have the rights to make the comment stick to the top of the list "
			    + commentId + ". ",
		    user.getLogin());
	}
	if (originalComment.getComment().getCommentLevel() != 1) {
	    throwException("Make comment sticky: Comment much be level 1 to stick to the top of the list " + commentId
		    + " level " + originalComment.getLevel() + ". ", user.getLogin());
	}

	Comment updatedComment = commentService.updateSticky(commentId, sticky);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(CommentConstants.ATTR_COMMENT_ID, commentId);
	JSONObject.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	JSONObject.put(CommentConstants.ATTR_THREAD_ID, updatedComment.getThreadComment().getUid());
	JSONObject.put(CommentConstants.ATTR_PARENT_COMMENT_ID, updatedComment.getParent().getUid());

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * Get login user information from system level session.
     */
    private User getCurrentUser(HttpServletRequest request) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	return getUserService().getUserByLogin(user.getLogin());
    }

    private IUserManagementService getUserService() {
	if (CommentAction.userService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    CommentAction.userService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return CommentAction.userService;
    }

    private ICommentService getCommentService() {
	if (CommentAction.commentService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    CommentAction.commentService = (ICommentService) ctx.getBean("commentService");
	}
	return CommentAction.commentService;
    }

    private ILamsCoreToolService getCoreToolService() {
	if (CommentAction.coreToolService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    CommentAction.coreToolService = (ILamsCoreToolService) ctx.getBean("lamsCoreToolService");
	}
	return CommentAction.coreToolService;
    }

    private ISecurityService getSecurityService() {
	if (CommentAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    CommentAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return CommentAction.securityService;
    }

}