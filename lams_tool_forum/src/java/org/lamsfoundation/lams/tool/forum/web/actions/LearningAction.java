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

package org.lamsfoundation.lams.tool.forum.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.MessageSeq;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.forum.service.ForumServiceProxy;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.tool.forum.web.forms.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * User: conradb Date: 24/06/2005 Time: 10:54:09
 */
public class LearningAction extends Action {
    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private IForumService forumService;

    @Override
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String param = mapping.getParameter();
	// --------------Forum Level ------------------
	if (param.equals("viewForum")) {
	    return viewForum(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}

	// --------------Topic Level ------------------
	if (param.equals("viewTopic") || param.equals("viewTopicNext")) {
	    return viewTopic(mapping, form, request, response);
	}
	if (param.equals("viewTopicThread")) {
	    return viewTopicThread(mapping, form, request, response);
	}
	if (param.equals("viewMessage")) {
	    return viewMessage(mapping, form, request, response);
	}
	if (param.equals("newTopic")) {
	    return newTopic(mapping, form, request, response);
	}
	if (param.equals("createTopic")) {
	    return createTopic(mapping, form, request, response);
	}
	if (param.equals("newReplyTopic")) {
	    return newReplyTopic(mapping, form, request, response);
	}
	if (param.equals("replyTopic")) {
	    return replyTopic(mapping, form, request, response);
	}
	if (param.equals("replyTopicInline")) {
	    return replyTopicInline(mapping, form, request, response);
	}
	if (param.equals("editTopic")) {
	    return editTopic(mapping, form, request, response);
	}
	if (param.equals("updateTopic")) {
	    return updateTopic(mapping, form, request, response);
	}
	if (param.equals("updateTopicInline")) {
	    return updateTopicInline(mapping, form, request, response);
	}
	if (param.equals("deleteAttachment")) {
	    return deleteAttachment(mapping, form, request, response);
	}
	if (param.equals("updateMessageHideFlag")) {
	    return updateMessageHideFlag(mapping, form, request, response);
	}
	if (param.equals("rateMessage")) {
	    return rateMessage(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward("error");
    }

    // ==========================================================================================
    // Forum level methods
    // ==========================================================================================
    /**
     * Display root topics of a forum. This page will be the initial page of Learner page.
     *
     * @throws Exception
     *
     */
    private ActionForward viewForum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// initial Session Map
	String sessionMapID = request.getParameter(ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap;
	// refresh forum page, not initial enter
	if (sessionMapID != null) {
	    sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	} else {
	    sessionMap = new SessionMap<>();
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	}
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	// set the mode into http session
	ToolAccessMode mode = null;
	try {
	    mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, LearningAction.MODE_OPTIONAL);
	} catch (Exception exp) {
	}
	if (mode == null) {
	    // set it as default mode
	    mode = ToolAccessMode.LEARNER;
	}

	// get sessionId from HttpServletRequest
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// Try to get ForumID according to sessionId
	forumService = getForumManager();
	ForumToolSession session = forumService.getSessionBySessionId(sessionId);

	if (session == null || session.getForum() == null) {
	    LearningAction.log.error("Failed on getting session by given sessionID:" + sessionId);
	    throw new Exception("Failed on getting session by given sessionID:" + sessionId);
	}

	// get session from shared session.
	HttpSession ss = SessionManager.getSession();

	Forum forum = session.getForum();
	// lock on finish
	ForumUser forumUser = getCurrentUser(request, sessionId);
	boolean lock = forum.getLockWhenFinished() && forumUser.isSessionFinished();

	// set some option flag to HttpSession
	// if allowRichEditor = true then don't restrict the number of chars
	// if isLimitedInput = false then don't restrict the number of chars
	// Indicate don't restrict number of chars by allowNumber = 0
	Long forumId = forum.getUid();
	Boolean allowRichEditor = new Boolean(forum.isAllowRichEditor());
	int minCharacters = forum.isLimitedMinCharacters() ? forum.getMinCharacters() : 0;
	int maxCharacters = forum.isLimitedMaxCharacters() || forum.isAllowRichEditor() ? forum.getMaxCharacters() : 0;

	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(ForumConstants.ATTR_FORUM_ID, forumId);
	sessionMap.put(ForumConstants.ATTR_FORUM_UID, forum.getUid());
	sessionMap.put(ForumConstants.ATTR_USER_UID, forumUser.getUid());
	sessionMap.put(ForumConstants.ATTR_FINISHED_LOCK, new Boolean(lock));
	sessionMap.put(ForumConstants.ATTR_LOCK_WHEN_FINISHED, forum.getLockWhenFinished());
	sessionMap.put(ForumConstants.ATTR_USER_FINISHED, forumUser.isSessionFinished());
	sessionMap.put(ForumConstants.ATTR_ALLOW_EDIT, forum.isAllowEdit());

	sessionMap.put(ForumConstants.ATTR_ALLOW_UPLOAD, forum.isAllowUpload());
	int uploadMaxFileSize = Configuration.getAsInt(ConfigurationKeys.UPLOAD_FILE_MAX_SIZE);
	// it defaults to -1 if property was not found
	if (uploadMaxFileSize > 0) {
	    sessionMap.put(ForumConstants.ATTR_UPLOAD_MAX_FILE_SIZE, FileValidatorUtil.formatSize(uploadMaxFileSize));
	}

	sessionMap.put(ForumConstants.ATTR_ALLOW_RATE_MESSAGES, forum.isAllowRateMessages());
	sessionMap.put(ForumConstants.ATTR_MINIMUM_RATE, forum.getMinimumRate());
	sessionMap.put(ForumConstants.ATTR_MAXIMUM_RATE, forum.getMaximumRate());
	sessionMap.put(ForumConstants.ATTR_ALLOW_NEW_TOPICS, forum.isAllowNewTopic());
	sessionMap.put(ForumConstants.ATTR_ALLOW_RICH_EDITOR, allowRichEditor);
	sessionMap.put(ForumConstants.ATTR_MIN_CHARACTERS, new Integer(minCharacters));
	sessionMap.put(ForumConstants.ATTR_MAX_CHARACTERS, new Integer(maxCharacters));
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(ForumConstants.ATTR_FORUM_TITLE, forum.getTitle());
	sessionMap.put(ForumConstants.ATTR_FORUM_INSTRCUTION, forum.getInstructions());
	sessionMap.put(AttributeNames.ATTR_LEARNER_CONTENT_FOLDER,
		forumService.getLearnerContentFolder(sessionId, forumUser.getUserId()));
	sessionMap.put(ForumConstants.ATTR_MINIMUM_REPLY, forum.getMinimumReply());
	sessionMap.put(ForumConstants.ATTR_MAXIMUM_REPLY, forum.getMaximumReply());

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	int numOfRatings = forumService.getNumOfRatingsByUserAndForum(forumUser.getUid(), forum.getUid());
	boolean noMoreRatings = (forum.getMaximumRate() != 0) && (numOfRatings >= forum.getMaximumRate())
		&& forum.isAllowRateMessages();
	boolean isMinRatingsCompleted = (forum.getMinimumRate() == 0)
		|| (numOfRatings >= forum.getMinimumRate()) && forum.isAllowRateMessages();
	sessionMap.put(ForumConstants.ATTR_NO_MORE_RATINGSS, noMoreRatings);
	sessionMap.put(ForumConstants.ATTR_IS_MIN_RATINGS_COMPLETED, isMinRatingsCompleted);
	sessionMap.put(ForumConstants.ATTR_NUM_OF_RATINGS, numOfRatings);

	// Should we show the reflection or not? We shouldn't show it when the screen is accessed
	// from the Monitoring Summary screen, but we should when accessed from the Learner Progress screen.
	// Need to constantly past this value on, rather than hiding just the once, as the View Forum
	// screen has a refresh button.
	boolean hideReflection = WebUtil.readBooleanParam(request, ForumConstants.ATTR_HIDE_REFLECTION, false);
	sessionMap.put(ForumConstants.ATTR_HIDE_REFLECTION, hideReflection);

	if (hideReflection) {
	    sessionMap.put(ForumConstants.ATTR_REFLECTION_ON, false);
	    sessionMap.put(ForumConstants.ATTR_REFLECTION_INSTRUCTION, "");
	    sessionMap.put(ForumConstants.ATTR_REFLECTION_ENTRY, "");
	} else {
	    sessionMap.put(ForumConstants.ATTR_REFLECTION_ON, forum.isReflectOnActivity());
	    sessionMap.put(ForumConstants.ATTR_REFLECTION_INSTRUCTION, forum.getReflectInstructions());

	    NotebookEntry notebookEntry = forumService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ForumConstants.TOOL_SIGNATURE, forumUser.getUserId().intValue());
	    sessionMap.put(ForumConstants.ATTR_REFLECTION_ENTRY, notebookEntry != null ? notebookEntry.getEntry() : "");
	}

	// add define later support
	if (forum.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	forum.setContentInUse(true);
	forum.setDefineLater(false);
	forumService.updateForum(forum);

	// get all root topic to display on init page
	List<MessageDTO> rootTopics = forumService.getRootTopics(sessionId);
	if (!forum.isAllowNewTopic()) {
	    // add the number post the learner has made for each topic.
	    for (MessageDTO messageDTO : rootTopics) {
		int numOfPosts = forumService.getNumOfPostsByTopic(forumUser.getUserId(),
			messageDTO.getMessage().getUid());
		messageDTO.setNumOfPosts(numOfPosts);
	    }
	}
	request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, rootTopics);

	// update new messages number
	for (MessageDTO messageDTO : rootTopics) {
	    int numOfNewPosts = forumService.getNewMessagesNum(messageDTO.getMessage(), forumUser.getUid());
	    messageDTO.setNewPostingsNum(numOfNewPosts);
	}

	if (forum.isNotifyLearnersOnMarkRelease()) {
	    boolean isHtmlFormat = false;

	    forumService.getEventNotificationService().createEvent(ForumConstants.TOOL_SIGNATURE,
		    ForumConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, forum.getContentId(),
		    forumService.getLocalisedMessage("event.mark.release.subject", null),
		    forumService.getLocalisedMessage("event.mark.release.body", null), isHtmlFormat);

	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    forumService.getEventNotificationService().subscribe(ForumConstants.TOOL_SIGNATURE,
		    ForumConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, forum.getContentId(), user.getUserID(),
		    IEventNotificationService.DELIVERY_METHOD_MAIL);
	}

	// check if there is submission deadline
	Date submissionDeadline = forum.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    sessionMap.put(ForumConstants.ATTR_SUBMISSION_DEADLINE, forum.getSubmissionDeadline());
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}

	return mapping.findForward("success");
    }

    /**
     * Learner click "finish" button in forum page, this method will turn on session status flag for this learner.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	forumService = getForumManager();

	if (mode == ToolAccessMode.LEARNER || mode == ToolAccessMode.AUTHOR) {
	    if (!validateBeforeFinish(request, sessionMapID)) {
		return mapping.getInputForward();
	    }

	    String nextActivityUrl;
	    try {
		// get session from shared session.
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long userID = new Long(user.getUserID().longValue());

		// finish current session for user
		forumService.finishUserSession(getCurrentUser(request, sessionId));
		ToolSessionManager sessionMgrService = ForumServiceProxy
			.getToolSessionManager(getServlet().getServletContext());
		nextActivityUrl = sessionMgrService.leaveToolSession(sessionId, userID);
		response.sendRedirect(nextActivityUrl);
	    } catch (DataMissingException e) {
		throw new ForumException(e);
	    } catch (ToolException e) {
		throw new ForumException(e);
	    } catch (IOException e) {
		throw new ForumException(e);
	    }
	    return null;

	}
	// get all root topic to display on init page
	List rootTopics = forumService.getRootTopics(sessionId);
	request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, rootTopics);

	return mapping.getInputForward();
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

	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = forumService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		ForumConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    forumService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ForumConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    forumService.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
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
	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IForumService submitFilesService = getForumManager();

	SessionMap map = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ForumConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward("success");
    }

    // ==========================================================================================
    // Topic level methods
    // ==========================================================================================

    /**
     * Display the messages for a particular topic. The Topic will arranged by Tree structure and loaded thread by
     * thread (with paging).
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward viewTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	forumService = getForumManager();

	Long rootTopicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);

	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	sessionMap.put(ForumConstants.ATTR_ROOT_TOPIC_UID, rootTopicId);

	// get forum user and forum
	// if coming from topic list, the toolSessionId is in the SessionMap.
	// if coming from the monitoring statistics window, it is passed in as a parameter.
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	if (toolSessionId != null) {
	    sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	    String mode = WebUtil.readStrParam(request, AttributeNames.PARAM_MODE, true);
	    if (mode != null) {
		sessionMap.put(AttributeNames.PARAM_MODE, mode);
	    }
	} else {
	    toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	}
	ForumUser forumUser = getCurrentUser(request, toolSessionId);
	Forum forum = forumUser.getSession().getForum();

	Long lastMsgSeqId = WebUtil.readLongParam(request, ForumConstants.PAGE_LAST_ID, true);
	Long pageSize = WebUtil.readLongParam(request, ForumConstants.PAGE_SIZE, true);

	setupViewTopicPagedDTOList(request, rootTopicId, sessionMapID, forumUser, forum, lastMsgSeqId, pageSize);

	// Should we show the reflection or not? We shouldn't show it when the View Forum screen is accessed
	// from the Monitoring Summary screen, but we should when accessed from the Learner Progress screen.
	// Need to constantly past this value on, rather than hiding just the once, as the View Forum
	// screen has a refresh button. Need to pass it through the view topic screen and dependent screens
	// as it has a link from the view topic screen back to View Forum screen.
	boolean hideReflection = WebUtil.readBooleanParam(request, ForumConstants.ATTR_HIDE_REFLECTION, false);
	sessionMap.put(ForumConstants.ATTR_HIDE_REFLECTION, hideReflection);

	return mapping.findForward("success");
    }

    private void setupViewTopicPagedDTOList(HttpServletRequest request, Long rootTopicId, String sessionMapID,
	    ForumUser forumUser, Forum forum, Long lastMsgSeqId, Long pageSize) {
	// get root topic list
	List<MessageDTO> msgDtoList = forumService.getTopicThread(rootTopicId, lastMsgSeqId, pageSize);
	updateMesssageFlag(msgDtoList);
	request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);

	// check if we can still make posts in this topic
	int numOfPosts = forumService.getNumOfPostsByTopic(forumUser.getUserId(), rootTopicId);
	boolean noMorePosts = forum.getMaximumReply() != 0 && numOfPosts >= forum.getMaximumReply()
		&& !forum.isAllowNewTopic() ? Boolean.TRUE : Boolean.FALSE;
	request.setAttribute(ForumConstants.ATTR_NO_MORE_POSTS, noMorePosts);
	request.setAttribute(ForumConstants.ATTR_NUM_OF_POSTS, numOfPosts);

	// transfer SessionMapID as well
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	// Saving or updating user timestamp
	forumService.saveTimestamp(rootTopicId, forumUser);
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

	forumService = getForumManager();

	Long rootTopicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);
	Long highlightMessageUid = WebUtil.readLongParam(request, ForumConstants.ATTR_MESS_ID, true);

	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	sessionMap.put(ForumConstants.ATTR_ROOT_TOPIC_UID, rootTopicId);

	// get forum user and forum
	ForumUser forumUser = getCurrentUser(request, (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID));
	Forum forum = forumUser.getSession().getForum();

	Long threadId = WebUtil.readLongParam(request, ForumConstants.ATTR_THREAD_ID, true);
	List<MessageDTO> msgDtoList = forumService.getThread(threadId);
	updateMesssageFlag(msgDtoList);
	request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);

	// check if we can still make posts in this topic
	int numOfPosts = forumService.getNumOfPostsByTopic(forumUser.getUserId(), rootTopicId);
	boolean noMorePosts = forum.getMaximumReply() != 0 && numOfPosts >= forum.getMaximumReply()
		&& !forum.isAllowNewTopic() ? Boolean.TRUE : Boolean.FALSE;
	request.setAttribute(ForumConstants.ATTR_NO_MORE_POSTS, noMorePosts);
	request.setAttribute(ForumConstants.ATTR_NUM_OF_POSTS, numOfPosts);
	request.setAttribute(ForumConstants.ATTR_NO_MORE_PAGES, true);

	if (highlightMessageUid != null) {
	    request.setAttribute(ForumConstants.ATTR_MESS_ID, highlightMessageUid);
	}
	// transfer SessionMapID as well
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return mapping.findForward("success");
    }

    /**
     * Display a single message.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward viewMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	forumService = getForumManager();

	Long rootTopicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);
	Long messageUid = WebUtil.readLongParam(request, ForumConstants.ATTR_MESS_ID, true);

	String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	sessionMap.put(ForumConstants.ATTR_ROOT_TOPIC_UID, rootTopicId);

	// get forum user and forum
	ForumUser forumUser = getCurrentUser(request, (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID));
	Forum forum = forumUser.getSession().getForum();

	List<MessageDTO> msgDtoList = forumService.getMessageAsDTO(messageUid);
	updateMesssageFlag(msgDtoList);
	request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);

	// check if we can still make posts in this topic
	int numOfPosts = forumService.getNumOfPostsByTopic(forumUser.getUserId(), rootTopicId);
	boolean noMorePosts = forum.getMaximumReply() != 0 && numOfPosts >= forum.getMaximumReply()
		&& !forum.isAllowNewTopic() ? Boolean.TRUE : Boolean.FALSE;
	request.setAttribute(ForumConstants.ATTR_NO_MORE_POSTS, noMorePosts);
	request.setAttribute(ForumConstants.ATTR_NUM_OF_POSTS, numOfPosts);
	request.setAttribute(ForumConstants.ATTR_NO_MORE_PAGES, true);

	if (messageUid != null) {
	    request.setAttribute(ForumConstants.ATTR_MESS_ID, messageUid);
	}
	// transfer SessionMapID as well
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return mapping.findForward("success");
    }

    /**
     * Display empty page for a new topic in forum
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// transfer SessionMapID as well
	((MessageForm) form).setSessionMapID(WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID));

	return mapping.findForward("success");
    }

    /**
     * Create a new root topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws PersistenceException
     */
    public ActionForward createTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, PersistenceException {

	MessageForm messageForm = (MessageForm) form;
	SessionMap sessionMap = getSessionMap(request, messageForm);
	Long forumId = (Long) sessionMap.get(ForumConstants.ATTR_FORUM_ID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	List<MessageDTO> rootTopics = forumService.getRootTopics(sessionId);

	Message message = messageForm.getMessage();
	message.setIsAuthored(false);
	message.setCreated(new Date());
	message.setUpdated(new Date());
	message.setLastReplyDate(new Date());
	int maxSeq = 1;
	if (rootTopics.size() > 0) {
	    MessageDTO last = rootTopics.get(rootTopics.size() - 1);
	    maxSeq = last.getMessage().getSequenceId() + 1;
	}
	message.setSequenceId(maxSeq);
	ForumUser forumUser = getCurrentUser(request, sessionId);
	message.setCreatedBy(forumUser);
	message.setModifiedBy(forumUser);
	setAttachment(messageForm, message);
	setMonitorMode(sessionMap, message);

	// save message into database
	forumService = getForumManager();
	forumService.createRootTopic(forumId, sessionId, message);

	rootTopics.add(MessageDTO.getMessageDTO(message));
	// echo back current root topic to forum init page
	request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, rootTopics);
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, messageForm.getSessionMapID());

	forumService.saveTimestamp(message.getUid(), forumUser);

	// update new messages number
	for (MessageDTO messageDTO : rootTopics) {
	    int numOfNewPosts = forumService.getNewMessagesNum(messageDTO.getMessage(), forumUser.getUid());
	    messageDTO.setNewPostingsNum(numOfNewPosts);
	}

	// notify learners and teachers
	forumService.sendNotificationsOnNewPosting(forumId, sessionId, message);

	return mapping.findForward("success");
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
	MessageForm msgForm = (MessageForm) form;
	String sessionMapID = request.getParameter(ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = getSessionMap(request, msgForm);
	msgForm.setSessionMapID(sessionMapID);

	Long parentId = WebUtil.readLongParam(request, ForumConstants.ATTR_PARENT_TOPIC_ID);
	sessionMap.put(ForumConstants.ATTR_PARENT_TOPIC_ID, parentId);

	// get parent topic, it can decide default subject of reply.
	MessageDTO topic = getTopic(parentId);
	if (topic != null && topic.getMessage() != null) {
	    String reTitle = topic.getMessage().getSubject();

	    MessageDTO originalMessage = MessageDTO.getMessageDTO(topic.getMessage());

	    sessionMap.put(ForumConstants.ATTR_ORIGINAL_MESSAGE, originalMessage);

	    // echo back current topic subject to web page
	    if (reTitle != null && !reTitle.trim().startsWith("Re:")) {
		msgForm.getMessage().setSubject("Re:" + reTitle);
	    } else {
		msgForm.getMessage().setSubject(reTitle);
	    }
	}

	// Should we show the reflection or not? We shouldn't show it when the View Forum screen is accessed
	// from the Monitoring Summary screen, but we should when accessed from the Learner Progress screen.
	// Need to constantly past this value on, rather than hiding just the once, as the View Forum
	// screen has a refresh button. Need to pass it through the view topic screen and dependent screens
	// as it has a link from the view topic screen back to View Forum screen.
	boolean hideReflection = WebUtil.readBooleanParam(request, ForumConstants.ATTR_HIDE_REFLECTION, false);
	sessionMap.put(ForumConstants.ATTR_HIDE_REFLECTION, hideReflection);

	return mapping.findForward("success");
    }

    /**
     * Create a replayed topic for a parent topic.
     */
    private ActionForward replyTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InterruptedException {

	MessageForm messageForm = (MessageForm) form;
	SessionMap sessionMap = getSessionMap(request, messageForm);
	Long parentId = (Long) sessionMap.get(ForumConstants.ATTR_PARENT_TOPIC_ID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	Message message = messageForm.getMessage();
	boolean isTestHarness = Boolean.valueOf(request.getParameter("testHarness"));
	if (isTestHarness) {
	    message.setBody(request.getParameter("message.body__textarea"));
	}
	message.setIsAuthored(false);
	message.setCreated(new Date());
	message.setUpdated(new Date());
	message.setLastReplyDate(new Date());
	ForumUser forumUser = getCurrentUser(request, sessionId);
	message.setCreatedBy(forumUser);
	message.setModifiedBy(forumUser);
	setAttachment(messageForm, message);
	setMonitorMode(sessionMap, message);

	// save message into database
	forumService = getForumManager();

	forumService.replyTopic(parentId, sessionId, message);

	// echo back this topic thread into page
	Long rootTopicId = forumService.getRootTopicId(parentId);

	// check whether allow more posts for this user
	ForumToolSession session = forumService.getSessionBySessionId(sessionId);
	Forum forum = session.getForum();

	setupViewTopicPagedDTOList(request, rootTopicId, messageForm.getSessionMapID(), forumUser, forum, null, null);

	// notify learners and teachers
	Long forumId = (Long) sessionMap.get(ForumConstants.ATTR_FORUM_ID);
	forumService.sendNotificationsOnNewPosting(forumId, sessionId, message);
	sessionMap.remove(ForumConstants.ATTR_ORIGINAL_MESSAGE);
	return mapping.findForward("success");
    }

    /**
     * Create a replayed topic for a parent topic.
     */
    private ActionForward replyTopicInline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws InterruptedException, IOException {

	MessageForm messageForm = (MessageForm) form;
	SessionMap sessionMap = getSessionMap(request, messageForm);
	Long parentId = (Long) sessionMap.get(ForumConstants.ATTR_PARENT_TOPIC_ID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	Message message = messageForm.getMessage();
	boolean isTestHarness = Boolean.valueOf(request.getParameter("testHarness"));
	if (isTestHarness) {
	    message.setBody(request.getParameter("message.body__textarea"));
	}
	message.setIsAuthored(false);
	message.setCreated(new Date());
	message.setUpdated(new Date());
	message.setLastReplyDate(new Date());
	ForumUser forumUser = getCurrentUser(request, sessionId);
	message.setCreatedBy(forumUser);
	message.setModifiedBy(forumUser);
	setAttachment(messageForm, message);
	setMonitorMode(sessionMap, message);

	// save message into database
	forumService = getForumManager();
	MessageSeq newMessageSeq = forumService.replyTopic(parentId, sessionId, message);

	// check whether allow more posts for this user
	Long rootTopicId = forumService.getRootTopicId(parentId);
	ForumToolSession session = forumService.getSessionBySessionId(sessionId);
	Forum forum = session.getForum();

	int numOfPosts = forumService.getNumOfPostsByTopic(forumUser.getUserId(), rootTopicId);
	boolean noMorePosts = forum.getMaximumReply() != 0 && numOfPosts >= forum.getMaximumReply()
		&& !forum.isAllowNewTopic() ? Boolean.TRUE : Boolean.FALSE;

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put(ForumConstants.ATTR_MESS_ID, newMessageSeq.getMessage().getUid());
	ObjectNode.put(ForumConstants.ATTR_NO_MORE_POSTS, noMorePosts);
	ObjectNode.put(ForumConstants.ATTR_NUM_OF_POSTS, numOfPosts);
	ObjectNode.put(ForumConstants.ATTR_THREAD_ID, newMessageSeq.getThreadMessage().getUid());
	ObjectNode.put(ForumConstants.ATTR_SESSION_MAP_ID, messageForm.getSessionMapID());
	ObjectNode.put(ForumConstants.ATTR_ROOT_TOPIC_UID, rootTopicId);
	ObjectNode.put(ForumConstants.ATTR_PARENT_TOPIC_ID, newMessageSeq.getMessage().getParent().getUid());
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);
	return null;
    }

    private void setMonitorMode(SessionMap<String, Object> sessionMap, Message message) {
	message.setIsMonitor(ToolAccessMode.TEACHER.equals(sessionMap.get(AttributeNames.ATTR_MODE)));
    }

    /**
     * Display a editable form for a special topic in order to update it.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward editTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws PersistenceException {
	Long topicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);

	MessageDTO topic = getTopic(topicId);

	// echo current topic content to web page
	MessageForm msgForm = (MessageForm) form;
	if (topic != null) {
	    msgForm.setMessage(topic.getMessage());
	    request.setAttribute(ForumConstants.AUTHORING_TOPIC, topic);
	}

	// cache this topicID, using in Update topic
	SessionMap sessionMap = getSessionMap(request, msgForm);
	sessionMap.put(ForumConstants.ATTR_TOPIC_ID, topicId);

	// Should we show the reflection or not? We shouldn't show it when the View Forum screen is accessed
	// from the Monitoring Summary screen, but we should when accessed from the Learner Progress screen.
	// Need to constantly past this value on, rather than hiding just the once, as the View Forum
	// screen has a refresh button. Need to pass it through the view topic screen and dependent screens
	// as it has a link from the view topic screen back to View Forum screen.
	boolean hideReflection = WebUtil.readBooleanParam(request, ForumConstants.ATTR_HIDE_REFLECTION, false);
	sessionMap.put(ForumConstants.ATTR_HIDE_REFLECTION, hideReflection);

	return mapping.findForward("success");
    }

    /**
     * Delete attachment from topic. This method only reset attachment information in memory. The finally update will
     * happen in <code>updateTopic</code> method. So topic can keep this attachment if user choose "Cancel" edit topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// only reset not attachment flag.
	MessageDTO dto = new MessageDTO();
	dto.setHasAttachment(false);
	request.setAttribute(ForumConstants.AUTHORING_TOPIC, dto);
	String sessionMapId = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapId);
	request.setAttribute(ForumConstants.ATTR_ALLOW_UPLOAD, sessionMap.get(ForumConstants.ATTR_ALLOW_UPLOAD));
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	return mapping.findForward("success");
    }

    /**
     * Update a topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward updateTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws PersistenceException {

	forumService = getForumManager();

	MessageForm messageForm = (MessageForm) form;
	SessionMap sessionMap = getSessionMap(request, messageForm);
	Long topicId = (Long) sessionMap.get(ForumConstants.ATTR_TOPIC_ID);
	Message message = messageForm.getMessage();

	doUpdateTopic(request, messageForm, sessionMap, topicId, message);

	// echo back this topic thread into page
	Long rootTopicId = forumService.getRootTopicId(topicId);
	ForumUser forumUser = getCurrentUser(request, (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID));
	Forum forum = forumUser.getSession().getForum();
	setupViewTopicPagedDTOList(request, rootTopicId, messageForm.getSessionMapID(), forumUser, forum, null, null);

	return mapping.findForward("success");
    }

    private void doUpdateTopic(HttpServletRequest request, MessageForm messageForm, SessionMap sessionMap, Long topicId,
	    Message message) {
	boolean makeAuditEntry = ToolAccessMode.TEACHER.equals(sessionMap.get(AttributeNames.ATTR_MODE));
	String oldMessageString = null;

	// get PO from database and sync with Form
	Message messagePO = forumService.getMessage(topicId);
	if (makeAuditEntry) {
	    oldMessageString = messagePO.toString();
	}
	messagePO.setSubject(message.getSubject());
	messagePO.setBody(message.getBody());
	messagePO.setUpdated(new Date());
	messagePO.setModifiedBy(getCurrentUser(request, (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID)));
	setAttachment(messageForm, messagePO);

	if (makeAuditEntry) {
	    Long userId = 0L;
	    String loginName = "Default";
	    if (message.getCreatedBy() != null) {
		userId = message.getCreatedBy().getUserId();
		loginName = message.getCreatedBy().getLoginName();
	    }
	    forumService.getAuditService().logChange(ForumConstants.TOOL_SIGNATURE, userId, loginName, oldMessageString,
		    messagePO.toString());
	}

	// save message into database
	// if we are in monitoring then we are probably editing some else's entry so log the change.
	forumService.updateTopic(messagePO);
    }

    /**
     * Update a topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     * @throws JSONException
     * @throws IOException
     */
    public ActionForward updateTopicInline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws PersistenceException, IOException {

	forumService = getForumManager();

	MessageForm messageForm = (MessageForm) form;
	SessionMap sessionMap = getSessionMap(request, messageForm);
	Long topicId = (Long) sessionMap.get(ForumConstants.ATTR_TOPIC_ID);
	Message message = messageForm.getMessage();

	doUpdateTopic(request, messageForm, sessionMap, topicId, message);

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put(ForumConstants.ATTR_MESS_ID, topicId);
	ObjectNode.put(ForumConstants.ATTR_SESSION_MAP_ID, messageForm.getSessionMapID());
	Long rootTopicId = forumService.getRootTopicId(topicId);
	ObjectNode.put(ForumConstants.ATTR_ROOT_TOPIC_UID, rootTopicId);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);
	return null;
    }

    /**
     * Sets the visibility of a message by updating the hide flag for a message
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward updateMessageHideFlag(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long msgId = new Long(WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID));
	Boolean hideFlag = new Boolean(WebUtil.readBooleanParam(request, "hideFlag"));
	forumService = getForumManager();

	// TODO Skipping permissions for now, currently having issues with default learning designs not having an
	// create_by field
	// Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	// check if the user has permission to hide posts.
	// ForumToolSession toolSession = forumService
	// .getSessionBySessionId(sessionId);
	//
	// Forum forum = toolSession.getForum();
	// ForumUser currentUser = getCurrentUser(request,sessionId);
	// ForumUser forumCreatedBy = forum.getCreatedBy();

	// we should be looking at whether a user is a teacher and more specifically staff
	// if (currentUser.getUserId().equals(forumCreatedBy.getUserId())) {
	forumService.updateMessageHideFlag(msgId, hideFlag.booleanValue());
	// } else {
	// log.info(currentUser + "does not have permission to hide/show postings in forum: " + forum.getUid());
	// log.info("Forum created by :" + forumCreatedBy.getUid() + ", Current User is: " + currentUser.getUid());
	// }

	// echo back this topic thread into page
	Long rootTopicId = forumService.getRootTopicId(msgId);
	List msgDtoList = forumService.getTopicThread(rootTopicId);
	updateMesssageFlag(msgDtoList);
	request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID,
		WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID));

	return mapping.findForward("success");
    }

    /**
     * Rates postings submitted by other learners.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward rateMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	forumService = getForumManager();
	String sessionMapId = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	Long forumUid = (Long) sessionMap.get(ForumConstants.ATTR_FORUM_UID);
	Long userUid = (Long) sessionMap.get(ForumConstants.ATTR_USER_UID);
	boolean isAllowRateMessages = (Boolean) sessionMap.get(ForumConstants.ATTR_ALLOW_RATE_MESSAGES);
	int forumMaximumRate = (Integer) sessionMap.get(ForumConstants.ATTR_MAXIMUM_RATE);
	int forumMinimumRate = (Integer) sessionMap.get(ForumConstants.ATTR_MINIMUM_RATE);

	float rating = Float.parseFloat(request.getParameter("rate"));
	Long responseId = WebUtil.readLongParam(request, "idBox");
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	Long userId = new Long(user.getUserID().intValue());

	AverageRatingDTO averageRatingDTO = forumService.rateMessage(responseId, userId, toolSessionID, rating);

	//refresh numOfRatings and noMoreRatings
	int numOfRatings = forumService.getNumOfRatingsByUserAndForum(userUid, forumUid);
	boolean noMoreRatings = (forumMaximumRate != 0) && (numOfRatings >= forumMaximumRate) && isAllowRateMessages;
	boolean isMinRatingsCompleted = (forumMinimumRate != 0) && (numOfRatings >= forumMinimumRate)
		&& isAllowRateMessages;
	sessionMap.put(ForumConstants.ATTR_NO_MORE_RATINGSS, noMoreRatings);
	sessionMap.put(ForumConstants.ATTR_IS_MIN_RATINGS_COMPLETED, isMinRatingsCompleted);
	sessionMap.put(ForumConstants.ATTR_NUM_OF_RATINGS, numOfRatings);

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put("averageRating", averageRatingDTO.getRating());
	ObjectNode.put("numberOfVotes", averageRatingDTO.getNumberOfVotes());
	ObjectNode.put(ForumConstants.ATTR_NO_MORE_RATINGSS, noMoreRatings);
	ObjectNode.put(ForumConstants.ATTR_NUM_OF_RATINGS, numOfRatings);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(ObjectNode);
	return null;
    }

    // ==========================================================================================
    // Utility methods
    // ==========================================================================================
    /**
     * Validation method to check whether user posts meet minimum number.
     */
    private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ForumToolSession session = forumService.getSessionBySessionId(sessionId);
	Forum forum = session.getForum();
	// get session from shared session.
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());
	if (!forum.isAllowNewTopic()) {

	    List<MessageDTO> list = forumService.getRootTopics(sessionId);
	    for (MessageDTO msgDto : list) {
		Long topicId = msgDto.getMessage().getUid();
		int numOfPostsInTopic = forumService.getNumOfPostsByTopic(userID, topicId);
		if (numOfPostsInTopic < forum.getMinimumReply()) {
		    // create error
		    ActionMessages errors = new ActionMessages();
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage("error.less.mini.post", forum.getMinimumReply()));
		    saveErrors(request, errors);

		    // get all root topic to display on init page
		    List rootTopics = forumService.getRootTopics(sessionId);
		    request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, rootTopics);
		    request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		    return false;
		}

	    }
	}
	return true;
    }

    /**
     * This method will set flag in message DTO:
     * <li>If this topic is created by current login user, then set Author mark true.</li>
     *
     * @param msgDtoList
     */
    private void updateMesssageFlag(List msgDtoList) {
	// set current user to web page, so that can display "edit" button
	// correct. Only author alow to edit.
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long currUserId = new Long(user.getUserID().intValue());
	Iterator iter = msgDtoList.iterator();
	while (iter.hasNext()) {
	    MessageDTO dto = (MessageDTO) iter.next();
	    if (dto.getMessage().getCreatedBy() != null
		    && currUserId.equals(dto.getMessage().getCreatedBy().getUserId())) {
		dto.setAuthor(true);
	    } else {
		dto.setAuthor(false);
	    }
	}
    }

    /**
     * @param topicId
     * @return
     */
    private MessageDTO getTopic(Long topicId) {
	// get Topic content according to TopicID
	forumService = getForumManager();
	MessageDTO topic = MessageDTO.getMessageDTO(forumService.getMessage(topicId));
	return topic;
    }

    /**
     * Get login user information from system level session. Check it whether it exists in database or not, and save it
     * if it does not exists. Return an instance of PO of ForumUser.
     *
     * @param request
     * @param sessionId
     * @return Current user instance
     */
    private ForumUser getCurrentUser(HttpServletRequest request, Long sessionId) {
	// get login user (author)
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	ForumUser forumUser = forumService.getUserByUserAndSession(new Long(user.getUserID().intValue()), sessionId);
	if (forumUser == null) {
	    // if user not exist, create new one in database
	    ForumToolSession session = forumService.getSessionBySessionId(sessionId);
	    forumUser = new ForumUser(user, session);
	    forumService.createUser(forumUser);
	}
	return forumUser;
    }

    /**
     * Get Forum Service.
     *
     * @return
     */
    private IForumService getForumManager() {
	if (forumService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
	}
	return forumService;
    }

    /**
     * @param messageForm
     * @param message
     */
    private void setAttachment(MessageForm messageForm, Message message) {
	if (messageForm.getAttachmentFile() != null
		&& !StringUtils.isBlank(messageForm.getAttachmentFile().getFileName())) {
	    forumService = getForumManager();
	    Attachment att = forumService.uploadAttachment(messageForm.getAttachmentFile());
	    Set attSet = message.getAttachments();
	    if (attSet == null) {
		attSet = new HashSet();
	    }
	    // only allow one attachment, so replace whatever
	    attSet.clear();
	    attSet.add(att);
	    message.setAttachments(attSet);
	} else if (!messageForm.isHasAttachment()) {
	    // user already called deleteAttachment in AJAX call
	    if (message.getAttachments() != null) {
		Set atts = message.getAttachments();
		atts.clear();
		message.setAttachments(atts);
	    } else {
		message.setAttachments(null);
	    }
	}
    }

    private SessionMap getSessionMap(HttpServletRequest request, MessageForm messageForm) {
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(messageForm.getSessionMapID());
	return sessionMap;
    }

}
