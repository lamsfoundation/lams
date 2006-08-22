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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.tool.forum.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.forum.service.ForumServiceProxy;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * User: conradb Date: 24/06/2005 Time: 10:54:09
 */
public class LearningAction extends Action {
	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private IForumService forumService;

	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String param = mapping.getParameter();
		// --------------Forum Level ------------------
		if (param.equals("viewForum")) {
			return viewForm(mapping, form, request, response);
		}
		if (param.equals("finish")) {
			return finish(mapping, form, request, response);
		}

		// --------------Topic Level ------------------
		if (param.equals("viewTopic")) {
			return viewTopic(mapping, form, request, response);
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
		if (param.equals("editTopic")) {
			return editTopic(mapping, form, request, response);
		}
		if (param.equals("updateTopic")) {
			return updateTopic(mapping, form, request, response);
		}
		if (param.equals("deleteAttachment")) {
			return deleteAttachment(mapping, form, request, response);
		}
		if (param.equals("updateMessageHideFlag")) {
			return updateMessageHideFlag(mapping, form, request, response);
		}
		return mapping.findForward("error");
	}

	// ==========================================================================================
	// Forum level methods
	// ==========================================================================================
	/**
	 * Display root topics of a forum. This page will be the initial page of
	 * Learner page.
	 * 
	 */
	private ActionForward viewForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		//initial Session Map 
		String sessionMapID = request.getParameter(ForumConstants.ATTR_SESSION_MAP_ID);
		SessionMap<String,Object> sessionMap;
		//refresh forum page, not initial enter
		if(sessionMapID != null){
			sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		}else{
			sessionMap = new SessionMap<String,Object>();
			request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		}
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		
		// set the mode into http session
		ToolAccessMode mode = null;
		try {
			mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, MODE_OPTIONAL);
		} catch (Exception exp) {
			//set it as default mode
			mode = ToolAccessMode.LEARNER;
		}
		if (mode == null) {
			throw new ForumException("Mode is required.");
		}

		// get sessionId from HttpServletRequest
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

		// Try to get ForumID according to sessionId
		forumService = getForumManager();
		ForumToolSession session = forumService.getSessionBySessionId(sessionId);
		if (session == null || session.getForum() == null) {
			log.error("Failed on getting session by given sessionID:" + sessionId);
			return mapping.findForward("error");
		}
		
		boolean lock = session.getForum().getLockWhenFinished();
		lock = lock && (session.getStatus() == ForumConstants.SESSION_STATUS_FINISHED ? true : false);
		
		Forum forum = session.getForum();
		//add define later support
		if(forum.isDefineLater()){
			return mapping.findForward("defineLater");
		}
		//add run offline support
		if(forum.getRunOffline()){
			return mapping.findForward("runOffline");
		}
		
		Long forumId = forum.getUid();
		Boolean allowRichEditor = new Boolean(forum.isAllowRichEditor());
		int allowNumber = forum.getLimitedChar();
		
		sessionMap.put(ForumConstants.FORUM_ID, forumId);
		sessionMap.put(AttributeNames.ATTR_MODE, mode);
		sessionMap.put(ForumConstants.ATTR_FINISHED_LOCK, new Boolean(lock));
		sessionMap.put(ForumConstants.ATTR_ALLOW_EDIT, forum.isAllowEdit());
		sessionMap.put(ForumConstants.ATTR_ALLOW_UPLOAD,forum.isAllowUpload());
		sessionMap.put(ForumConstants.ATTR_ALLOW_NEW_TOPICS,forum.isAllowNewTopic());
		sessionMap.put(ForumConstants.ATTR_ALLOW_RICH_EDITOR,allowRichEditor);
		sessionMap.put(ForumConstants.ATTR_LIMITED_CHARS,new Integer(allowNumber));
		sessionMap.put(ForumConstants.ATTR_FORUM_TITLE,forum.getTitle());
		sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		sessionMap.put(ForumConstants.ATTR_FORUM_INSTRCUTION,forum.getInstructions());
		
		// get all root topic to display on init page
		List rootTopics = forumService.getRootTopics(sessionId);
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, rootTopics);
		
		//set contentInUse flag to true!
		forum.setContentInUse(true);
		forum.setDefineLater(false);
		forumService.updateForum(forum);
		
		return mapping.findForward("success");
	}


	/**
	 * Learner click "finish" button in forum page, this method will turn on
	 * session status flag for this learner.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward finish(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long sessionId = WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID);
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, MODE_OPTIONAL);
		
		if (mode == ToolAccessMode.LEARNER || mode==ToolAccessMode.AUTHOR) {
			forumService = getForumManager();
			ForumToolSession session = forumService.getSessionBySessionId(sessionId);
			Forum forum = session.getForum();
			// get session from shared session.
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());
			if(!forum.getRunOffline() && !forum.isAllowNewTopic()){
				int postNum = forumService.getTopicsNum(userID,sessionId);
				if(postNum < forum.getMinimumReply()){
					ActionMessages errors = new ActionMessages();
					errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.less.mini.post",(forum.getMinimumReply() - postNum)));
					saveErrors(request, errors);
					// get all root topic to display on init page
					List rootTopics = forumService.getRootTopics(sessionId);
					request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, rootTopics);								
					return mapping.findForward("success");
				}
			}

			String nextActivityUrl;
			try {
				ToolSessionManager sessionMgrService = ForumServiceProxy.getToolSessionManager(getServlet().getServletContext());
				nextActivityUrl = sessionMgrService.leaveToolSession(sessionId,
						userID);
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
		
		return mapping.findForward("success");
	}

	// ==========================================================================================
	// Topic level methods
	// ==========================================================================================

	/**
	 * Display read-only page for a special topic. Topic will arrange by Tree
	 * structure.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		Long rootTopicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);
		forumService = getForumManager();
		// get root topic list
		List<MessageDTO> msgDtoList = forumService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		
		String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		sessionMap.put(ForumConstants.ATTR_ROOT_TOPIC_UID, rootTopicId);
		
		//transfer SessionMapID as well
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
	private ActionForward newTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//transfer SessionMapID as well
		((MessageForm)form).setSessionMapID(WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID));

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
	public ActionForward createTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, PersistenceException {

		MessageForm messageForm = (MessageForm) form;
		SessionMap sessionMap = getSessionMap(request, messageForm);
		Long forumId = (Long) sessionMap.get(ForumConstants.FORUM_ID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

		Message message = messageForm.getMessage();
		message.setIsAuthored(false);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		ForumUser forumUser = getCurrentUser(request,sessionId);
		message.setCreatedBy(forumUser);
		message.setModifiedBy(forumUser);
		setAttachment(messageForm, message);
		
		// save message into database
		forumService = getForumManager();
		forumService.createRootTopic(forumId, sessionId, message);

		// echo back current root topic to fourm init page
		List rootTopics = forumService.getRootTopics(sessionId);
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, rootTopics);
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID,messageForm.getSessionMapID());
		
		return mapping.findForward("success");
	}


	/**
	 * Dipslay replay topic page. Message form subject will include parent
	 * topics same subject.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newReplyTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String sessionMapID = request.getParameter(ForumConstants.ATTR_SESSION_MAP_ID);
		
		MessageForm msgForm = (MessageForm) form;
		msgForm.setSessionMapID(sessionMapID);
		
		Long parentId = WebUtil.readLongParam(request, ForumConstants.ATTR_PARENT_TOPIC_ID);
		// get parent topic, it can decide default subject of reply.
		MessageDTO topic = getTopic(parentId);

		if (topic != null && topic.getMessage() != null) {
			// echo back current topic subject to web page
			msgForm.getMessage().setSubject("Re:" + topic.getMessage().getSubject());
		}
		SessionMap sessionMap = getSessionMap(request, msgForm);
		sessionMap.put(ForumConstants.ATTR_PARENT_TOPIC_ID, parentId);
		
		return mapping.findForward("success");
	}


	/**
	 * Create a replayed topic for a parent topic.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward replyTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		
		MessageForm messageForm = (MessageForm) form;
		SessionMap sessionMap = getSessionMap(request, messageForm);
		Long parentId = (Long) sessionMap.get(ForumConstants.ATTR_PARENT_TOPIC_ID);
		Long sessionId =(Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		Message message = messageForm.getMessage();
		message.setIsAuthored(false);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		ForumUser forumUser = getCurrentUser(request,sessionId);
		message.setCreatedBy(forumUser);
		message.setModifiedBy(forumUser);
		setAttachment(messageForm, message);

		
		// save message into database
		forumService = getForumManager();
		forumService.replyTopic(parentId, sessionId, message);

		// echo back this topic thread into page
		Long rootTopicId = forumService.getRootTopicId(parentId);
		List msgDtoList = forumService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID,messageForm.getSessionMapID());
		
		//check whether allow more posts for this user
		ForumToolSession session = forumService.getSessionBySessionId(sessionId);
		Forum forum = session.getForum();
		if(forum != null){
			if(!forum.isAllowNewTopic()){
				int posts = forumService.getTopicsNum(forumUser.getUserId(), sessionId);
				if(posts >= forum.getMaximumReply())
					sessionMap.put(ForumConstants.ATTR_NO_MORE_POSTS, Boolean.TRUE);
			}
		}
		return mapping.findForward("success");
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
	public ActionForward editTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws PersistenceException {
		Long topicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);

		MessageDTO topic = getTopic(topicId);
		
		// echo current topic content to web page
		MessageForm msgForm = (MessageForm) form;
		if (topic != null) {
			msgForm.setMessage(topic.getMessage());
			request.setAttribute(ForumConstants.AUTHORING_TOPIC, topic);
		}

		//cache this topicID, using in Update topic
		SessionMap sessionMap = getSessionMap(request, msgForm);
		sessionMap.put(ForumConstants.ATTR_TOPIC_ID, topicId);
		
		return mapping.findForward("success");
	}
	/**
	 * Delete attachment from topic. This method only reset attachment information in memory. The finally update 
	 * will happen in <code>updateTopic</code> method. So topic can keep this attachment if user choose "Cancel" edit
	 * topic.
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		//only reset not attachment flag.
		MessageDTO dto = new MessageDTO();
		dto.setHasAttachment(false);
		request.setAttribute(ForumConstants.AUTHORING_TOPIC, dto);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID));
		request.setAttribute(ForumConstants.ATTR_ALLOW_UPLOAD, sessionMap.get(ForumConstants.ATTR_ALLOW_UPLOAD));
		
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
	public ActionForward updateTopic(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws PersistenceException {

		forumService = getForumManager();
		
		MessageForm messageForm = (MessageForm) form;
		SessionMap sessionMap = getSessionMap(request, messageForm);
		Long topicId = (Long) sessionMap.get(ForumConstants.ATTR_TOPIC_ID);
		Message message = messageForm.getMessage();		
		
		boolean makeAuditEntry = ToolAccessMode.TEACHER.equals((ToolAccessMode)sessionMap.get(AttributeNames.ATTR_MODE));
		String oldMessageString = null;

		// get PO from database and sync with Form
		Message messagePO = forumService.getMessage(topicId);
		if ( makeAuditEntry ) {
			oldMessageString = messagePO.toString();
		}
		messagePO.setSubject(message.getSubject());
		messagePO.setBody(message.getBody());
		messagePO.setUpdated(new Date());
		messagePO.setModifiedBy(getCurrentUser(request,(Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID)));
		setAttachment(messageForm, messagePO);

		if ( makeAuditEntry ) {
			forumService.getAuditService().logChange(ForumConstants.TOOL_SIGNATURE,
		 			messagePO.getCreatedBy().getUserId(), messagePO.getCreatedBy().getLoginName(),
		 			oldMessageString, messagePO.toString());
		 } 

		// save message into database
 	    // if we are in monitoring then we are probably editing some else's entry so log the change.
		forumService.updateTopic(messagePO);

		// echo back this topic thread into page
		Long rootTopicId = forumService.getRootTopicId(topicId);
		List msgDtoList = forumService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID,messageForm.getSessionMapID());

		return mapping.findForward("success");
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
	public ActionForward updateMessageHideFlag(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Long msgId = new Long(WebUtil.readLongParam(request,ForumConstants.ATTR_TOPIC_ID));
		Boolean hideFlag = new Boolean(WebUtil.readBooleanParam(request, "hideFlag"));
		forumService = getForumManager();
		
		// TODO Skipping permissions for now, currently having issues with default learning designs not having an create_by field
//		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		// check if the user has permission to hide posts.
//		ForumToolSession toolSession = forumService
//				.getSessionBySessionId(sessionId);
//		
//		Forum forum = toolSession.getForum();
//		ForumUser currentUser = getCurrentUser(request,sessionId);
//		ForumUser forumCreatedBy = forum.getCreatedBy();
	
		// we should be looking at whether a user is a teacher and more specifically staff
//		if (currentUser.getUserId().equals(forumCreatedBy.getUserId())) {
			forumService.updateMessageHideFlag(msgId, hideFlag.booleanValue());
//		} else {
//			log.info(currentUser + "does not have permission to hide/show postings in forum: " + forum.getUid());
//			log.info("Forum created by :" + forumCreatedBy.getUid() + ", Current User is: " + currentUser.getUid());
//		}

		
		// echo back this topic thread into page
		Long rootTopicId = forumService.getRootTopicId(msgId);
		List msgDtoList = forumService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID,WebUtil.readLongParam(request,ForumConstants.ATTR_SESSION_MAP_ID));

		return mapping.findForward("success");
	}

	// ==========================================================================================
	// Utility methods
	// ==========================================================================================
	/**
	 * This method will set flag in message DTO:
	 * <li>If this topic is created by current login user, then set Author mark
	 * true.</li>
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
					&& currUserId.equals(dto.getMessage().getCreatedBy()
							.getUserId()))
				dto.setAuthor(true);
			else
				dto.setAuthor(false);
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
	 * Get login user information from system level session. Check it whether it
	 * exists in database or not, and save it if it does not exists. Return an
	 * instance of PO of ForumUser.
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
		ForumUser forumUser = forumService.getUserByUserAndSession(new Long(
				user.getUserID().intValue()), sessionId);
		if (forumUser == null) {
			// if user not exist, create new one in database
			ForumToolSession session = forumService
					.getSessionBySessionId(sessionId);
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
					.getRequiredWebApplicationContext(getServlet()
							.getServletContext());
			forumService = (IForumService) wac
					.getBean(ForumConstants.FORUM_SERVICE);
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
			Attachment att = forumService.uploadAttachment(messageForm
					.getAttachmentFile());
			Set attSet = message.getAttachments();
			if(attSet == null)
				attSet = new HashSet();
			// only allow one attachment, so replace whatever
			attSet.clear();
			attSet.add(att);
			message.setAttachments(attSet);
		}else if(!messageForm.isHasAttachment()){
			//user already called deleteAttachment in AJAX call
			if(message.getAttachments() != null){
				Set atts = message.getAttachments();
				atts.clear();
				message.setAttachments(atts);
			}else
				message.setAttachments(null);
		}
	}

	private SessionMap getSessionMap(HttpServletRequest request, MessageForm messageForm) {
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(messageForm.getSessionMapID());
		return sessionMap;
	}
	
}
