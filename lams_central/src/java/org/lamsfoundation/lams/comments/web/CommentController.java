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

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Fiona Malikoff
 */
@Controller
@RequestMapping("/comments")
public class CommentController {
    private static Logger log = Logger.getLogger(CommentController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private ILamsCoreToolService lamsCoreToolService;
    @Autowired
    private ISecurityService securityService;

    /**
     * Display the comments for a given external id (usually tool session id). The session comments will be
     * arranged by Tree structure and loaded thread by thread (with paging).
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/init")
    private String init(HttpServletRequest request) throws ServletException {

	// initial Session Map
	String sessionMapID = request.getParameter(CommentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap;

	Long externalId;
	Long externalSecondaryId;
	int externalType;
	String externalSignature;
	String mode;
	boolean likeAndDislike;
	boolean allowAnonymous;
	boolean readOnly;
	Integer pageSize;
	Integer sortBy;

	// refresh forum page, not initial enter
	if (sessionMapID != null) {
	    sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	    externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	    externalSecondaryId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SECONDARY_ID);
	    externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	    externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);
	    mode = (String) sessionMap.get(AttributeNames.ATTR_MODE);
	    pageSize = (Integer) sessionMap.get(CommentConstants.PAGE_SIZE);
	    sortBy = (Integer) sessionMap.get(CommentConstants.ATTR_SORT_BY);

	} else {
	    sessionMap = new SessionMap<>();
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	    externalId = WebUtil.readLongParam(request, CommentConstants.ATTR_EXTERNAL_ID);
	    externalSecondaryId = WebUtil.readLongParam(request, CommentConstants.ATTR_EXTERNAL_SECONDARY_ID, true);
	    externalType = WebUtil.readIntParam(request, CommentConstants.ATTR_EXTERNAL_TYPE);
	    externalSignature = WebUtil.readStrParam(request, CommentConstants.ATTR_EXTERNAL_SIG);
	    likeAndDislike = WebUtil.readBooleanParam(request, CommentConstants.ATTR_LIKE_AND_DISLIKE);
	    allowAnonymous = WebUtil.readBooleanParam(request, CommentConstants.ATTR_ANONYMOUS);
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
	    if (externalSecondaryId != null) {
		sessionMap.put(CommentConstants.ATTR_EXTERNAL_SECONDARY_ID, externalSecondaryId);
	    }
	    sessionMap.put(CommentConstants.ATTR_EXTERNAL_SIG, externalSignature);
	    sessionMap.put(CommentConstants.ATTR_LIKE_AND_DISLIKE, likeAndDislike);
	    sessionMap.put(CommentConstants.ATTR_ANONYMOUS, allowAnonymous);
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

	Comment rootComment = commentService.createOrGetRoot(externalId, externalSecondaryId, externalType,
		externalSignature, user);
	sessionMap.put(CommentConstants.ATTR_ROOT_COMMENT_UID, rootComment.getUid());

	prepareViewTopicData(request, sessionMap, pageSize, sortBy, true);
	return "comments/comments";
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
	GroupedToolSession toolSession = (GroupedToolSession) lamsCoreToolService.getToolSessionById(toolSessionId);
	return toolSession.getSessionGroup().getUsers().contains(user);
    }

    private boolean monitorInToolSession(Long toolSessionId, User user, SessionMap<String, Object> sessionMap) {

	if (ToolAccessMode.TEACHER
		.equals(WebUtil.getToolAccessMode((String) sessionMap.get(AttributeNames.ATTR_MODE)))) {
	    GroupedToolSession toolSession = (GroupedToolSession) lamsCoreToolService.getToolSessionById(toolSessionId);
	    return securityService.isLessonMonitor(toolSession.getLesson().getLessonId(), user.getUserId(),
		    "Comment Monitoring Tasks");
	} else {
	    return false;
	}
    }

    /**
     * Display the comments for a given external id (usually tool session id). The session comments will be
     * arranged by Tree structure and loaded thread by thread (with paging). This may set a new value of sort by, so
     * make sure the session is updated.
     */
    @RequestMapping("/viewTopic")
    @SuppressWarnings("unchecked")
    private String viewTopic(HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, CommentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Integer pageSize = WebUtil.readIntParam(request, CommentConstants.PAGE_SIZE, true);
	Integer sortBy = WebUtil.readIntParam(request, CommentConstants.ATTR_SORT_BY, true);
	Boolean sticky = WebUtil.readBooleanParam(request, CommentConstants.ATTR_STICKY, false);
	if (sortBy != null) {
	    sessionMap.put(CommentConstants.ATTR_SORT_BY, sortBy);
	}

	prepareViewTopicData(request, sessionMap, pageSize, sortBy, sticky);
	return (sticky ? "comments/allviewwrapper" : "comments/topicviewwrapper");
    }

    private void prepareViewTopicData(HttpServletRequest request, SessionMap<String, Object> sessionMap,
	    Integer pageSize, Integer sortBy, boolean includeSticky) {

	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Long externalSecondaryId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SECONDARY_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

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

	List<CommentDTO> msgDtoList = commentService.getTopicThread(externalId, externalSecondaryId, externalType,
		externalSignature, lastMsgSeqId, pageSize, sortBy, currentLikeCount, user.getUserID());
	updateMessageFlag(msgDtoList, user.getUserID());
	request.setAttribute(CommentConstants.ATTR_COMMENT_THREAD, msgDtoList);

	if (includeSticky) {
	    List<CommentDTO> stickyList = commentService.getTopicStickyThread(externalId, externalSecondaryId,
		    externalType, externalSignature, sortBy, currentLikeCount, user.getUserID());
	    updateMessageFlag(stickyList, user.getUserID());
	    request.setAttribute(CommentConstants.ATTR_STICKY, stickyList);
	}

	// transfer SessionMapID as well
	request.setAttribute(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
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
    @RequestMapping("/viewTopicThread")
    private String viewTopicThread(HttpServletRequest request) {

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

	return "comments/topicviewwrapper";
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
    @RequestMapping("/newComment")
    @ResponseBody
    private String newComment(HttpServletRequest request, HttpServletResponse response)
	    throws InterruptedException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Long externalSecondaryId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SECONDARY_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

	String commentText = request.getParameter(CommentConstants.ATTR_BODY);
	if (commentText != null) {
	    commentText = commentText.trim();
	}

	Boolean commentAnonymous = WebUtil.readBooleanParam(request, CommentConstants.ATTR_COMMENT_ANONYMOUS_NEW,
		false);

	ObjectNode responseJSON;

	if (!validateText(commentText)) {
	    responseJSON = getFailedValidationJSON();
	} else {

	    User user = getCurrentUser(request);
	    ToolAccessMode mode = WebUtil.getToolAccessMode((String) sessionMap.get(AttributeNames.ATTR_MODE));
	    boolean isMonitor = ToolAccessMode.TEACHER.equals(mode)
		    && monitorInToolSession(externalId, user, sessionMap);
	    if (!isMonitor && !learnerInToolSession(externalId, user)) {
		throwException("New comment: User does not have the rights to access the comments. ", user.getLogin(),
			externalId, externalType, externalSignature);
	    }

	    Comment rootSeq = commentService.getRoot(externalId, externalSecondaryId, externalType, externalSignature);

	    // save message into database
	    Comment newComment = commentService.createReply(rootSeq, commentText, user, isMonitor, commentAnonymous);

	    responseJSON = JsonNodeFactory.instance.objectNode();
	    responseJSON.put(CommentConstants.ATTR_COMMENT_ID, newComment.getUid());
	    responseJSON.put(CommentConstants.ATTR_THREAD_ID, newComment.getThreadComment().getUid());
	    responseJSON.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    responseJSON.put(CommentConstants.ATTR_PARENT_COMMENT_ID, newComment.getParent().getUid());

	}
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
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
    @RequestMapping("/newReplyTopic")
    private String newReplyTopic(HttpServletRequest request) {

	request.setAttribute(CommentConstants.ATTR_SESSION_MAP_ID,
		request.getParameter(CommentConstants.ATTR_SESSION_MAP_ID));
	request.setAttribute(CommentConstants.ATTR_PARENT_COMMENT_ID,
		request.getParameter(CommentConstants.ATTR_PARENT_COMMENT_ID));
	return "comments/reply";
    }

    /**
     * Create a reply to a parent topic.
     */
    @RequestMapping("/replyTopicInline")
    @ResponseBody
    private String replyTopicInline(HttpServletRequest request, HttpServletResponse response)
	    throws InterruptedException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

	Long parentId = WebUtil.readLongParam(request, CommentConstants.ATTR_PARENT_COMMENT_ID);
	String commentText = WebUtil.readStrParam(request, CommentConstants.ATTR_BODY, true);
	if (commentText != null) {
	    commentText = commentText.trim();
	}

	Boolean commentAnonymous = WebUtil.readBooleanParam(request, CommentConstants.ATTR_COMMENT_ANONYMOUS_REPLY,
		false);

	ObjectNode responseJSON;
	if (!validateText(commentText)) {
	    responseJSON = getFailedValidationJSON();

	} else {

	    User user = getCurrentUser(request);
	    ToolAccessMode mode = WebUtil.getToolAccessMode((String) sessionMap.get(AttributeNames.ATTR_MODE));
	    boolean isMonitor = ToolAccessMode.TEACHER.equals(mode)
		    && monitorInToolSession(externalId, user, sessionMap);
	    if (!isMonitor && !learnerInToolSession(externalId, user)) {
		throwException("New comment: User does not have the rights to access the comments. ", user.getLogin(),
			externalId, externalType, externalSignature);
	    }

	    // save message into database
	    Comment newComment = commentService.createReply(parentId, commentText.trim(), user, isMonitor,
		    commentAnonymous);

	    responseJSON = JsonNodeFactory.instance.objectNode();
	    responseJSON.put(CommentConstants.ATTR_COMMENT_ID, newComment.getUid());
	    responseJSON.put(CommentConstants.ATTR_THREAD_ID, newComment.getThreadComment().getUid());
	    responseJSON.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    responseJSON.put(CommentConstants.ATTR_PARENT_COMMENT_ID, newComment.getParent().getUid());
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    private boolean validateText(String commentText) {
	return commentText != null && commentText.length() > 0
		&& commentText.length() < CommentConstants.MAX_BODY_LENGTH;
    }

    private ObjectNode getFailedValidationJSON() {
	MessageService msgService = commentService.getMessageService();
	ObjectNode resultJSON = JsonNodeFactory.instance.objectNode();
	resultJSON.put(CommentConstants.ATTR_ERR_MESSAGE, msgService.getMessage(CommentConstants.KEY_BODY_VALIDATION));
	return resultJSON;
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
    @RequestMapping("/editTopic")
    public String editTopic(HttpServletRequest request) {

	Long commentId = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);
	CommentDTO comment = commentService.getComment(commentId);

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	sessionMap.put(CommentConstants.ATTR_COMMENT_ID, commentId);
	request.setAttribute(CommentConstants.ATTR_COMMENT_ID, commentId);
	request.setAttribute(CommentConstants.ATTR_COMMENT, comment);
	request.setAttribute(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return "comments/edit";
    }

    /**
     * Update a topic.
     *
     * @throws ServletException
     */
    @RequestMapping("/updateTopicInline")
    @ResponseBody
    public String updateTopicInline(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long commentId = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);

	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	Integer externalType = (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE);
	String externalSignature = (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG);

	String commentText = request.getParameter(CommentConstants.ATTR_BODY);
	if (commentText != null) {
	    commentText = commentText.trim();
	}

	// Don't update anonymous if it is monitoring
	boolean isMonitoring = ToolAccessMode.TEACHER
		.equals(WebUtil.getToolAccessMode((String) sessionMap.get(AttributeNames.ATTR_MODE)));
	Boolean commentAnonymous = isMonitoring ? null
		: WebUtil.readBooleanParam(request, CommentConstants.ATTR_COMMENT_ANONYMOUS_EDIT, false);

	ObjectNode ObjectNode;

	if (!validateText(commentText)) {
	    ObjectNode = getFailedValidationJSON();

	} else {

	    CommentDTO originalComment = commentService.getComment(commentId);

	    User user = getCurrentUser(request);
	    if (!originalComment.getComment().getCreatedBy().equals(user)
		    && !monitorInToolSession(externalId, user, sessionMap)) {
		throwException(
			"Update comment: User does not have the rights to update the comment " + commentId + ". ",
			user.getLogin(), externalId, externalType, externalSignature);
	    }

	    Comment updatedComment = commentService.updateComment(commentId, commentText, user, commentAnonymous,
		    isMonitoring);

	    ObjectNode = JsonNodeFactory.instance.objectNode();
	    ObjectNode.put(CommentConstants.ATTR_COMMENT_ID, commentId);
	    ObjectNode.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    ObjectNode.put(CommentConstants.ATTR_THREAD_ID, updatedComment.getThreadComment().getUid());
	    ObjectNode.put(CommentConstants.ATTR_PARENT_COMMENT_ID, updatedComment.getParent().getUid());
	}

	response.setContentType("application/json;charset=utf-8");
	return ObjectNode.toString();
    }

    /**
     * Stores user's like.
     */
    @RequestMapping(path = ("/like"), method = RequestMethod.POST)
    @ResponseBody
    private String like(HttpServletRequest request, HttpServletResponse response, boolean isLike)
	    throws InterruptedException, IOException, ServletException {
	return updateLikeCount(request, response, true);
    }

    /**
     * Stores user's dislike.
     */
    @RequestMapping(path = ("/dislike"), method = RequestMethod.POST)
    @ResponseBody
    private String dislike(HttpServletRequest request, HttpServletResponse response, boolean isLike)
	    throws InterruptedException, IOException, ServletException {
	return updateLikeCount(request, response, false);
    }

    /**
     * Update the likes/dislikes
     */
    private String updateLikeCount(HttpServletRequest request, HttpServletResponse response, boolean isLike)
	    throws InterruptedException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long messageUid = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);

	User user = getCurrentUser(request);
	if (!learnerInToolSession(externalId, user)) {
	    throwException(
		    "Update comment: User does not have the rights to like/dislike the comment " + messageUid + ". ",
		    user.getLogin(), externalId, (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE),
		    (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG));
	}

	boolean added = commentService.addLike(messageUid, user, isLike ? CommentLike.LIKE : CommentLike.DISLIKE);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put(CommentConstants.ATTR_COMMENT_ID, messageUid);
	responseJSON.put(CommentConstants.ATTR_STATUS, added);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Update hide flag
     */
    @RequestMapping("/hide")
    @ResponseBody
    private String hideComment(HttpServletRequest request, HttpServletResponse response, boolean isLike)
	    throws InterruptedException, IOException, ServletException {

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long commentId = WebUtil.readLongParam(request, CommentConstants.ATTR_COMMENT_ID);
	Long externalId = (Long) sessionMap.get(CommentConstants.ATTR_EXTERNAL_ID);
	boolean status = WebUtil.readBooleanParam(request, CommentConstants.ATTR_HIDE_FLAG);

	User user = getCurrentUser(request);
	if (!monitorInToolSession(externalId, user, sessionMap)) {
	    throwException("Update comment: User does not have the rights to hide the comment " + commentId + ". ",
		    user.getLogin(), externalId, (Integer) sessionMap.get(CommentConstants.ATTR_EXTERNAL_TYPE),
		    (String) sessionMap.get(CommentConstants.ATTR_EXTERNAL_SIG));
	}

	Comment updatedComment = commentService.hideComment(commentId, user, status);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put(CommentConstants.ATTR_COMMENT_ID, updatedComment.getUid());
	responseJSON.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	responseJSON.put(CommentConstants.ATTR_THREAD_ID, updatedComment.getThreadComment().getUid());
	responseJSON.put(CommentConstants.ATTR_PARENT_COMMENT_ID, updatedComment.getParent().getUid());

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Make a topic sticky - the topic should be level 1 only.
     *
     * @throws ServletException
     */
    @RequestMapping("/makeSticky")
    @ResponseBody
    public String makeSticky(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

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

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put(CommentConstants.ATTR_COMMENT_ID, commentId);
	responseJSON.put(CommentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	responseJSON.put(CommentConstants.ATTR_THREAD_ID, updatedComment.getThreadComment().getUid());
	responseJSON.put(CommentConstants.ATTR_PARENT_COMMENT_ID, updatedComment.getParent().getUid());

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Get login user information from system level session.
     */
    private User getCurrentUser(HttpServletRequest request) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	return userManagementService.getUserByLogin(user.getLogin());
    }

}