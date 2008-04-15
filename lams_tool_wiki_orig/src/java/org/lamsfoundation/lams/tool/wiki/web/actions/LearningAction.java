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

/* $$Id$$ */	
package org.lamsfoundation.lams.tool.wiki.web.actions;

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
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.wiki.dto.MessageDTO;
import org.lamsfoundation.lams.tool.wiki.persistence.Attachment;
import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiException;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiToolSession;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiUser;
import org.lamsfoundation.lams.tool.wiki.persistence.Message;
import org.lamsfoundation.lams.tool.wiki.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.wiki.service.WikiServiceProxy;
import org.lamsfoundation.lams.tool.wiki.service.IWikiService;
import org.lamsfoundation.lams.tool.wiki.util.WikiConstants;
import org.lamsfoundation.lams.tool.wiki.web.forms.MessageForm;
import org.lamsfoundation.lams.tool.wiki.web.forms.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * User: conradb Date: 24/06/2005 Time: 10:54:09
 */
public class LearningAction extends Action {
	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private IWikiService wikiService;

	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String param = mapping.getParameter();
		// --------------Wiki Level ------------------
		if (param.equals("viewWiki")) {
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
		
		//================ Reflection =======================
		if (param.equals("newReflection")) {
			return newReflection(mapping, form, request, response);
		}
		if (param.equals("submitReflection")) {
			return submitReflection(mapping, form, request, response);
		}
		
		
		return mapping.findForward("error");
	}



	// ==========================================================================================
	// Wiki level methods
	// ==========================================================================================
	/**
	 * Display root topics of a wiki. This page will be the initial page of
	 * Learner page.
	 * @throws Exception 
	 * 
	 */
	private ActionForward viewForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//initial Session Map 
		String sessionMapID = request.getParameter(WikiConstants.ATTR_SESSION_MAP_ID);
		SessionMap<String,Object> sessionMap;
		//refresh wiki page, not initial enter
		if(sessionMapID != null){
			sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		}else{
			sessionMap = new SessionMap<String,Object>();
			request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		}
		request.setAttribute(WikiConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		
		// set the mode into http session
		ToolAccessMode mode = null;
		try {
			mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, MODE_OPTIONAL);
		} catch (Exception exp) {
		}
		if (mode == null) {
			//set it as default mode
			mode = ToolAccessMode.LEARNER;
		}

		// get sessionId from HttpServletRequest
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

		// Try to get WikiID according to sessionId
		wikiService = getWikiManager();
		WikiToolSession session = wikiService.getSessionBySessionId(sessionId);

		if (session == null || session.getWiki() == null) {
			log.error("Failed on getting session by given sessionID:" + sessionId);
			throw new Exception("Failed on getting session by given sessionID:" + sessionId);
		}

		Wiki wiki = session.getWiki();
		//lock on finish
		WikiUser wikiUser = getCurrentUser(request,sessionId);
		boolean lock =  wiki.getLockWhenFinished() && wikiUser.isSessionFinished();

		//add define later support
		if(wiki.isDefineLater()){
			return mapping.findForward("defineLater");
		}
		
		//add run offline support
		if(wiki.getRunOffline()){
			return mapping.findForward("runOffline");
		}
				
		//set contentInUse flag to true!
		if ( ! wiki.isContentInUse() ) {
			wiki.setContentInUse(true);
			wiki.setDefineLater(false);
			wikiService.updateWiki(wiki);
		}
		
		//set some option flag to HttpSession
		// if allowRichEditor = true then don't restrict the number of chars
		// if isLimitedInput = false then don't restrict the number of chars
		// Indicate don't restrict number of chars by allowNumber = 0
		Long wikiId = wiki.getUid();
		//Boolean allowRichEditor = new Boolean(wiki.isAllowRichEditor());
		Boolean allowRichEditor = new Boolean(true);
		//int allowNumber = wiki.isLimitedInput() || wiki.isAllowRichEditor() ? wiki.getLimitedChar() : 0;
		int allowNumber = wiki.getLimitedChar();
		
		// get notebook entry
		String entryText = new String();
		NotebookEntry notebookEntry = wikiService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL, 
				WikiConstants.TOOL_SIGNATURE, wikiUser.getUserId().intValue());
		
		if (notebookEntry != null) {
			entryText = notebookEntry.getEntry();
		}
		
		sessionMap.put(WikiConstants.FORUM_ID, wikiId);
		sessionMap.put(AttributeNames.ATTR_MODE, mode);
		sessionMap.put(WikiConstants.ATTR_FINISHED_LOCK, new Boolean(lock));
		sessionMap.put(WikiConstants.ATTR_USER_FINISHED, wikiUser.isSessionFinished());
		sessionMap.put(WikiConstants.ATTR_ALLOW_EDIT, wiki.isAllowEdit());
		sessionMap.put(WikiConstants.ATTR_ALLOW_UPLOAD,wiki.isAllowUpload());
		sessionMap.put(WikiConstants.ATTR_ALLOW_NEW_WIKI_PAGES,wiki.isAllowNewWikiPage());
		sessionMap.put(WikiConstants.ATTR_ALLOW_ATTACH_IMAGES,wiki.isAllowAttachImage());
		sessionMap.put(WikiConstants.ATTR_ALLOW_INSERT_LINKS,wiki.isAllowInsertLink());
		sessionMap.put(WikiConstants.ATTR_ALLOW_RICH_EDITOR,allowRichEditor);
		sessionMap.put(WikiConstants.ATTR_LIMITED_CHARS,new Integer(allowNumber));
		sessionMap.put(WikiConstants.ATTR_REFLECTION_ON,wiki.isReflectOnActivity());
		sessionMap.put(WikiConstants.ATTR_REFLECTION_INSTRUCTION,wiki.getReflectInstructions());
		sessionMap.put(WikiConstants.ATTR_REFLECTION_ENTRY, entryText);
		sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		sessionMap.put(WikiConstants.ATTR_FORUM_TITLE,wiki.getTitle());
		sessionMap.put(WikiConstants.ATTR_FORUM_INSTRCUTION,wiki.getInstructions());
		
		// get all root topic to display on init page
		List rootTopics = wikiService.getRootTopics(sessionId);
		request.setAttribute(WikiConstants.AUTHORING_TOPICS_LIST, rootTopics);
		
		return mapping.findForward("success");
	}


	/**
	 * Learner click "finish" button in wiki page, this method will turn on
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
		String sessionMapID = WebUtil.readStrParam(request, WikiConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		wikiService = getWikiManager();
		
		if (mode == ToolAccessMode.LEARNER || mode==ToolAccessMode.AUTHOR) {
			if(!validateBeforeFinish(request, sessionMapID))
				return mapping.getInputForward();

			String nextActivityUrl;
			try {
//				 get session from shared session.
				HttpSession ss = SessionManager.getSession();
				UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
				Long userID = new Long(user.getUserID().longValue());
				
				//finish current session for user
				wikiService.finishUserSession(getCurrentUser(request, sessionId));
				ToolSessionManager sessionMgrService = WikiServiceProxy.getToolSessionManager(getServlet().getServletContext());
				nextActivityUrl = sessionMgrService.leaveToolSession(sessionId,userID);
				response.sendRedirect(nextActivityUrl);
			} catch (DataMissingException e) {
				throw new WikiException(e);
			} catch (ToolException e) {
				throw new WikiException(e);
			} catch (IOException e) {
				throw new WikiException(e);
			}
			return null;

		}
		// get all root topic to display on init page
		List rootTopics = wikiService.getRootTopics(sessionId);
		request.setAttribute(WikiConstants.AUTHORING_TOPICS_LIST, rootTopics);
		
		return mapping.getInputForward();
	}



	/**
	 * Submit reflection form input database.
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
		
		String sessionMapID = WebUtil.readStrParam(request, WikiConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		// check for existing notebook entry
		NotebookEntry entry = wikiService.getEntry(sessionId,
				CoreNotebookConstants.NOTEBOOK_TOOL,
				WikiConstants.TOOL_SIGNATURE, userId);

		if (entry == null) {
			// create new entry
			wikiService.createNotebookEntry(sessionId,
					CoreNotebookConstants.NOTEBOOK_TOOL,
					WikiConstants.TOOL_SIGNATURE, userId, refForm
							.getEntryText());
		} else {
			// update existing entry
			entry.setEntry(refForm.getEntryText());
			entry.setLastModified(new Date());
			wikiService.updateEntry(entry);
		}

		return finish(mapping, form, request, response);
	}
	
	/**
	 * Display empty reflection form.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		//get session value
		String sessionMapID = WebUtil.readStrParam(request, WikiConstants.ATTR_SESSION_MAP_ID);
		if(!validateBeforeFinish(request, sessionMapID))
			return mapping.getInputForward();

		ReflectionForm refForm = (ReflectionForm) form;
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		refForm.setUserID(user.getUserID());
		refForm.setSessionMapID(sessionMapID);
		
		// get the existing reflection entry
		IWikiService submitFilesService = getWikiManager();
		
		SessionMap map = (SessionMap)request.getSession().getAttribute(sessionMapID);
		Long toolSessionID = (Long)map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL, WikiConstants.TOOL_SIGNATURE, user.getUserID());
		
		if (entry != null) {
			refForm.setEntryText(entry.getEntry());
		}
		
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
		
		Long rootTopicId = WebUtil.readLongParam(request, WikiConstants.ATTR_TOPIC_ID);
		wikiService = getWikiManager();
		// get root topic list
		List<MessageDTO> msgDtoList = wikiService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		request.setAttribute(WikiConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		
		String sessionMapID = WebUtil.readStrParam(request, WikiConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		sessionMap.put(WikiConstants.ATTR_ROOT_TOPIC_UID, rootTopicId);
		
		//transfer SessionMapID as well
		request.setAttribute(WikiConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		
		return mapping.findForward("success");

	}


	/**
	 * Display empty page for a new topic in wiki
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
		((MessageForm)form).setSessionMapID(WebUtil.readStrParam(request, WikiConstants.ATTR_SESSION_MAP_ID));

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
		Long wikiId = (Long) sessionMap.get(WikiConstants.FORUM_ID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

		Message message = messageForm.getMessage();
		message.setIsAuthored(false);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		WikiUser wikiUser = getCurrentUser(request,sessionId);
		message.setCreatedBy(wikiUser);
		message.setModifiedBy(wikiUser);
		setAttachment(messageForm, message);
		
		// save message into database
		wikiService = getWikiManager();
		wikiService.createRootTopic(wikiId, sessionId, message);

		// echo back current root topic to fourm init page
		List rootTopics = wikiService.getRootTopics(sessionId);
		request.setAttribute(WikiConstants.AUTHORING_TOPICS_LIST, rootTopics);
		request.setAttribute(WikiConstants.ATTR_SESSION_MAP_ID,messageForm.getSessionMapID());
		
		return mapping.findForward("success");
	}


	/**
	 * Display replay topic page. Message form subject will include parent
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
		String sessionMapID = request.getParameter(WikiConstants.ATTR_SESSION_MAP_ID);
		
		MessageForm msgForm = (MessageForm) form;
		msgForm.setSessionMapID(sessionMapID);
		
		Long parentId = WebUtil.readLongParam(request, WikiConstants.ATTR_PARENT_TOPIC_ID);
		// get parent topic, it can decide default subject of reply.
		MessageDTO topic = getTopic(parentId);

		if (topic != null && topic.getMessage() != null) {
			String reTitle = topic.getMessage().getSubject();
			
			MessageDTO originalMessage = MessageDTO.getMessageDTO(topic.getMessage());
			
			request.setAttribute(WikiConstants.ATTR_ORIGINAL_MESSAGE, originalMessage);
			
			// echo back current topic subject to web page
			if(reTitle != null && !reTitle.trim().startsWith("Re:"))
				msgForm.getMessage().setSubject("Re:" + reTitle);
			else
				msgForm.getMessage().setSubject(reTitle);
		}
		SessionMap sessionMap = getSessionMap(request, msgForm);
		sessionMap.put(WikiConstants.ATTR_PARENT_TOPIC_ID, parentId);
	
		
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
		Long parentId = (Long) sessionMap.get(WikiConstants.ATTR_PARENT_TOPIC_ID);
		Long sessionId =(Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		Message message = messageForm.getMessage();
		message.setIsAuthored(false);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		WikiUser wikiUser = getCurrentUser(request,sessionId);
		message.setCreatedBy(wikiUser);
		message.setModifiedBy(wikiUser);
		setAttachment(messageForm, message);

		
		// save message into database
		wikiService = getWikiManager();
		wikiService.replyTopic(parentId, sessionId, message);

		// echo back this topic thread into page
		Long rootTopicId = wikiService.getRootTopicId(parentId);
		List msgDtoList = wikiService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		
		request.setAttribute(WikiConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		request.setAttribute(WikiConstants.ATTR_SESSION_MAP_ID,messageForm.getSessionMapID());
		
		//check whether allow more posts for this user
		WikiToolSession session = wikiService.getSessionBySessionId(sessionId);
		Wiki wiki = session.getWiki();
		if(wiki != null){
			if(!wiki.isAllowNewWikiPage()){
				int posts = wikiService.getTopicsNum(wikiUser.getUserId(), sessionId);
				if(wiki.getMaximumReply() != 0 && (posts >= wiki.getMaximumReply()))
					sessionMap.put(WikiConstants.ATTR_NO_MORE_POSTS, Boolean.TRUE);
			}
		}
		sessionMap.remove(WikiConstants.ATTR_ORIGINAL_MESSAGE);
		
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
		Long topicId = WebUtil.readLongParam(request, WikiConstants.ATTR_TOPIC_ID);

		MessageDTO topic = getTopic(topicId);
		
		// echo current topic content to web page
		MessageForm msgForm = (MessageForm) form;
		if (topic != null) {
			msgForm.setMessage(topic.getMessage());
			request.setAttribute(WikiConstants.AUTHORING_TOPIC, topic);
		}

		//cache this topicID, using in Update topic
		SessionMap sessionMap = getSessionMap(request, msgForm);
		sessionMap.put(WikiConstants.ATTR_TOPIC_ID, topicId);
		
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
		request.setAttribute(WikiConstants.AUTHORING_TOPIC, dto);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(WebUtil.readStrParam(request, WikiConstants.ATTR_SESSION_MAP_ID));
		request.setAttribute(WikiConstants.ATTR_ALLOW_UPLOAD, sessionMap.get(WikiConstants.ATTR_ALLOW_UPLOAD));
		
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

		wikiService = getWikiManager();
		
		MessageForm messageForm = (MessageForm) form;
		SessionMap sessionMap = getSessionMap(request, messageForm);
		Long topicId = (Long) sessionMap.get(WikiConstants.ATTR_TOPIC_ID);
		Message message = messageForm.getMessage();		
		
		boolean makeAuditEntry = ToolAccessMode.TEACHER.equals((ToolAccessMode)sessionMap.get(AttributeNames.ATTR_MODE));
		String oldMessageString = null;

		// get PO from database and sync with Form
		Message messagePO = wikiService.getMessage(topicId);
		if ( makeAuditEntry ) {
			oldMessageString = messagePO.toString();
		}
		messagePO.setSubject(message.getSubject());
		messagePO.setBody(message.getBody());
		messagePO.setUpdated(new Date());
		messagePO.setModifiedBy(getCurrentUser(request,(Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID)));
		setAttachment(messageForm, messagePO);

		if ( makeAuditEntry ) {
			Long userId = 0L;
    		String loginName = "Default";
    		if(message.getCreatedBy() != null){
    			userId = message.getCreatedBy().getUserId();
    			loginName = message.getCreatedBy().getLoginName();
    		}
			wikiService.getAuditService().logChange(WikiConstants.TOOL_SIGNATURE,
					userId,loginName,
		 			oldMessageString, messagePO.toString());
		 } 

		// save message into database
 	    // if we are in monitoring then we are probably editing some else's entry so log the change.
		wikiService.updateTopic(messagePO);

		// echo back this topic thread into page
		Long rootTopicId = wikiService.getRootTopicId(topicId);
		List msgDtoList = wikiService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		
		request.setAttribute(WikiConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		request.setAttribute(WikiConstants.ATTR_SESSION_MAP_ID,messageForm.getSessionMapID());

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
		
		Long msgId = new Long(WebUtil.readLongParam(request,WikiConstants.ATTR_TOPIC_ID));
		Boolean hideFlag = new Boolean(WebUtil.readBooleanParam(request, "hideFlag"));
		wikiService = getWikiManager();
		
		// TODO Skipping permissions for now, currently having issues with default learning designs not having an create_by field
//		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		// check if the user has permission to hide posts.
//		WikiToolSession toolSession = wikiService
//				.getSessionBySessionId(sessionId);
//		
//		Wiki wiki = toolSession.getWiki();
//		WikiUser currentUser = getCurrentUser(request,sessionId);
//		WikiUser wikiCreatedBy = wiki.getCreatedBy();
	
		// we should be looking at whether a user is a teacher and more specifically staff
//		if (currentUser.getUserId().equals(wikiCreatedBy.getUserId())) {
			wikiService.updateMessageHideFlag(msgId, hideFlag.booleanValue());
//		} else {
//			log.info(currentUser + "does not have permission to hide/show postings in wiki: " + wiki.getUid());
//			log.info("Wiki created by :" + wikiCreatedBy.getUid() + ", Current User is: " + currentUser.getUid());
//		}

		
		// echo back this topic thread into page
		Long rootTopicId = wikiService.getRootTopicId(msgId);
		List msgDtoList = wikiService.getTopicThread(rootTopicId);
		updateMesssageFlag(msgDtoList);
		request.setAttribute(WikiConstants.AUTHORING_TOPIC_THREAD, msgDtoList);
		request.setAttribute(WikiConstants.ATTR_SESSION_MAP_ID,WebUtil.readStrParam(request,WikiConstants.ATTR_SESSION_MAP_ID));

		return mapping.findForward("success");
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
		
		WikiToolSession session = wikiService.getSessionBySessionId(sessionId);
		Wiki wiki = session.getWiki();
		// get session from shared session.
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		Long userID = new Long(user.getUserID().longValue());
		if(!wiki.getRunOffline() && !wiki.isAllowNewWikiPage()){
			int postNum = wikiService.getTopicsNum(userID,sessionId);
			if(postNum < wiki.getMinimumReply()){
				//create error
				ActionMessages errors = new ActionMessages();
				errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.less.mini.post",(wiki.getMinimumReply() - postNum)));
				saveErrors(request, errors);
				
				// get all root topic to display on init page
				List rootTopics = wikiService.getRootTopics(sessionId);
				request.setAttribute(WikiConstants.AUTHORING_TOPICS_LIST, rootTopics);
				request.setAttribute(WikiConstants.ATTR_SESSION_MAP_ID, sessionMapID );
				return false;
			}
		}
		return true;
	}
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
		wikiService = getWikiManager();
		MessageDTO topic = MessageDTO.getMessageDTO(wikiService.getMessage(topicId));
		return topic;
	}

	/**
	 * Get login user information from system level session. Check it whether it
	 * exists in database or not, and save it if it does not exists. Return an
	 * instance of PO of WikiUser.
	 * 
	 * @param request
	 * @param sessionId 
	 * @return Current user instance
	 */
	private WikiUser getCurrentUser(HttpServletRequest request, Long sessionId) {
		// get login user (author)
		HttpSession ss = SessionManager.getSession();
		// get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		WikiUser wikiUser = wikiService.getUserByUserAndSession(new Long(
				user.getUserID().intValue()), sessionId);
		if (wikiUser == null) {
			// if user not exist, create new one in database
			WikiToolSession session = wikiService
					.getSessionBySessionId(sessionId);
			wikiUser = new WikiUser(user, session);
			wikiService.createUser(wikiUser);
		}
		return wikiUser;
	}

	/**
	 * Get Wiki Service.
	 * 
	 * @return
	 */
	private IWikiService getWikiManager() {
		if (wikiService == null) {
			WebApplicationContext wac = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServlet()
							.getServletContext());
			wikiService = (IWikiService) wac
					.getBean(WikiConstants.FORUM_SERVICE);
		}
		return wikiService;
	}

	/**
	 * @param messageForm
	 * @param message
	 */
	private void setAttachment(MessageForm messageForm, Message message) {
		if (messageForm.getAttachmentFile() != null
				&& !StringUtils.isBlank(messageForm.getAttachmentFile().getFileName())) {
			wikiService = getWikiManager();
			Attachment att = wikiService.uploadAttachment(messageForm
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
