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
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * User: conradb
 * Date: 24/06/2005
 * Time: 10:54:09
 */
public class LearningAction extends Action {
  	private static Logger log = Logger.getLogger(LearningAction.class);
    private static final boolean MODE_OPTIONAL = false;
	private IForumService forumService;


   public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
  		String param = mapping.getParameter();
  		//--------------Forum Level ------------------
	  	if (param.equals("viewForum")) {
       		return viewForm(mapping, form, request, response);
        }
	  	if (param.equals("newTopic")) {
	  		return newTopic(mapping, form, request, response);
	  	}
	  	if (param.equals("finish")) {
	  		return finish(mapping, form, request, response);
	  	}
	  	
	  	//--------------Topic Level ------------------
	  	if (param.equals("viewTopic")) {
       		return viewTopic(mapping, form, request, response);
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
        
		return mapping.findForward("error");
    }
	//==========================================================================================
	// Forum level  methods
	//==========================================================================================
   /**
    * Display root topics of a forum. This page will be the initial page of Learner page.
    *  
    */
	private ActionForward viewForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
        //set the mode into http session
		ToolAccessMode mode = null;
		try{
	        mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,MODE_OPTIONAL);
	        request.getSession().setAttribute(AttributeNames.ATTR_MODE, mode);
		}catch(Exception exp){
			//check wether it already existed in Session
			mode = (ToolAccessMode) request.getSession().getAttribute(AttributeNames.ATTR_MODE);
		}
		if(mode == null){
			throw new ForumException("Mode is required.");
		}
        
		//get sessionId from HttpServletRequest
		String sessionIdStr = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
		Long sessionId;
		if(StringUtils.isEmpty(sessionIdStr))
			//if SessionID in request is empty, then try to get it from session. This happens when client "refresh"
			//page after create a new topic
			sessionId = (Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
		else
			sessionId = new Long(Long.parseLong(sessionIdStr));
		//cache this sessionId into HttpSession
		request.getSession().setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		
		//Try to get ForumID according to sessionId
		forumService = getForumManager();
		ForumToolSession session = forumService.getSessionBySessionId(sessionId);
		if(session == null || session.getForum() == null){
			log.error("Failed on getting session by given sessionID:" + sessionId);
			return mapping.findForward("error");
		}
		Long forumId = session.getForum().getUid();
		Boolean allowEdit = new Boolean(session.getForum().isAllowEdit());
		Boolean allowRichEditor = new Boolean(session.getForum().isAllowRichEditor());
		int allowNumber = session.getForum().getLimitedChar();
		request.getSession().setAttribute(ForumConstants.FORUM_ID, forumId);
		request.getSession().setAttribute(ForumConstants.ALLOW_EDIT, allowEdit);
		request.getSession().setAttribute(ForumConstants.ALLOW_RICH_EDITOR, allowRichEditor);
		request.getSession().setAttribute(ForumConstants.LIMITED_CHARS, new Integer(allowNumber));
		boolean lock = session.getForum().getLockWhenFinished();
		lock = lock && (session.getStatus() == ForumConstants.SESSION_STATUS_FINISHED ? true:false);
		request.getSession().setAttribute(ForumConstants.FINISHEDLOCK,new Boolean(lock));

		//get all root topic to display on init page
		List rootTopics = forumService.getRootTopics(sessionId);
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST,rootTopics);
		return mapping.findForward("success");
	}
	/**
	 * Display empty page for a new topic in forum
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("success");
	}
	/**
	 * Learner click "finish" button in forum page, this method will turn on session status flag for this learner.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long sessionId = (Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
		ToolAccessMode mode = (ToolAccessMode) request.getSession().getAttribute(AttributeNames.ATTR_MODE);
		if (mode == ToolAccessMode.LEARNER) {
			//get sessionId from HttpServletRequest
			forumService = getForumManager();
			ForumToolSession session = forumService.getSessionBySessionId(sessionId);
			session.setStatus(ForumConstants.SESSION_STATUS_FINISHED);
			forumService.updateSession(session);
			
			ToolSessionManager sessionMgrService = ForumServiceProxy.getToolSessionManager(getServlet().getServletContext());

			//get back login user DTO
			//get session from shared session.
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());

			String nextActivityUrl;
			try {
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
		//get all root topic to display on init page
		List rootTopics = forumService.getRootTopics(sessionId);
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST,rootTopics);
		
		return mapping.findForward("success");
	}

	//==========================================================================================
	// Topic level  methods
	//==========================================================================================
	
	/**
	 * Display read-only page for a special topic. Topic will arrange by Tree structure.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long topicId = new Long(WebUtil.readLongParam(request,"topicId"));
		
		forumService = getForumManager();
		//get root topic list
		List msgDtoList = forumService.getTopicThread(topicId);
		
		setAuthorMark(msgDtoList);
		
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD,msgDtoList);
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
		
		Message message = messageForm.getMessage();
		message.setIsAuthored(false);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		ForumUser forumUser = getCurrentUser(request);
		message.setCreatedBy(forumUser);
		message.setModifiedBy(forumUser);
		setAttachment(messageForm, message);
		
		//save message into database
		forumService = getForumManager();
		Long forumId = (Long) request.getSession().getAttribute(ForumConstants.FORUM_ID);
		Long sessionId = (Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
		forumService.createRootTopic(forumId,sessionId,message);
		
		//echo back current root topic to fourm init page
		List rootTopics = forumService.getRootTopics(sessionId);
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST,rootTopics);
		return mapping.findForward("success");
	}
	/**
	 * Dipslay replay topic page. Message form subject will include parent topics same subject.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newReplyTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long parentId = new Long(WebUtil.readLongParam(request,"parentId"));
		
		//get parent topic, it can decide default subject of reply.
    	MessageDTO topic = getTopic(parentId);
    	
    	if(topic != null && topic.getMessage() != null){
        	//echo back current topic subject to web page
    		MessageForm msgForm = (MessageForm)form;
    		msgForm.getMessage().setSubject("Re:"+topic.getMessage().getSubject());
    	}
    	
    	//cache this parentId in order to create reply
    	request.getSession().setAttribute("parentId",parentId);
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
	private ActionForward replyTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long parentId = (Long) request.getSession().getAttribute("parentId");
		Long sessionId = (Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
		MessageForm messageForm = (MessageForm) form;
		Message message = messageForm.getMessage();
		message.setIsAuthored(false);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		ForumUser forumUser = getCurrentUser(request);
		message.setCreatedBy(forumUser);
		message.setModifiedBy(forumUser);
		setAttachment(messageForm, message);
		
		//save message into database
		forumService = getForumManager();
		forumService.replyTopic(parentId,sessionId,message);
		
		//echo back this topic thread into page
		forumService = getForumManager();
		Long rootTopicId = forumService.getRootTopicId(parentId);
		List msgDtoList = forumService.getTopicThread(rootTopicId);
		setAuthorMark(msgDtoList);
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD,msgDtoList);
		
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
  	public ActionForward editTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
  		Long topicId = new Long(WebUtil.readLongParam(request,"topicId"));
  		
    	MessageDTO topic = getTopic(topicId);
    	//echo current topic content to web page
    	if(topic != null){
    		MessageForm msgForm = (MessageForm)form;
    		msgForm.setMessage(topic.getMessage());
    		request.setAttribute(ForumConstants.AUTHORING_TOPIC,topic);
    	}
    	
    	//cache this topicId in order to create reply
    	request.getSession().setAttribute("topicId",topicId);
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
    	//get value from HttpSession
		Long topicId = (Long) request.getSession().getAttribute("topicId");
		forumService = getForumManager();
		
		MessageForm messageForm = (MessageForm) form;
		Message message = messageForm.getMessage();
		//get PO from database and sync with Form
		Message messagePO = forumService.getMessage(topicId);
		messagePO.setSubject(message.getSubject());
		messagePO.setBody(message.getBody());
		messagePO.setUpdated(new Date());
		messagePO.setModifiedBy(getCurrentUser(request));
		setAttachment(messageForm, messagePO);
		
		//save message into database
		forumService.updateTopic(messagePO);

		//echo back this topic thread into page
		forumService = getForumManager();
		Long rootTopicId = forumService.getRootTopicId(topicId);
		List msgDtoList = forumService.getTopicThread(rootTopicId);
		setAuthorMark(msgDtoList);
		request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD,msgDtoList);
		
		return mapping.findForward("success");
    }

    /**
     * Only delete the attachemnt file for current topic.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	//get value from HttpSession
    	Long topicId = (Long) request.getSession().getAttribute("topicId");
		Long versionID = new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuid"));
		forumService = getForumManager();
		forumService.deleteFromRepository(uuID,versionID);
//		get value from HttpSession
		Message messagePO = forumService.getMessage(topicId);
		messagePO.setUpdated(new Date());
		messagePO.setModifiedBy(getCurrentUser(request));
		messagePO.setAttachments(null);
		//save message into database
		forumService.updateTopic(messagePO);
		
    	return mapping.findForward("success");
    }

	//==========================================================================================
	// Utility methods
	//==========================================================================================
	/**
	 * If this topic is created by current login user, then set Author mark true.
	 * 
	 * @param msgDtoList
	 */
	private void setAuthorMark(List msgDtoList) {
		//set current user to web page, so that can display "edit" button correct. Only author alow to edit.
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

		Long currUserId = new Long(user.getUserID().intValue());
		Iterator iter = msgDtoList.iterator();
		while(iter.hasNext()){
			MessageDTO dto = (MessageDTO) iter.next();
			if(dto.getMessage().getCreatedBy() != null 
					&& currUserId.equals(dto.getMessage().getCreatedBy().getUserId()))
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
		//get Topic content according to TopicID
		forumService = getForumManager();
		MessageDTO topic = MessageDTO.getMessageDTO(forumService.getMessage(topicId));
		return topic;
	}
	
	/**
	 * Get login user information from system level session. Check it whether it exists in database or not, and save it
	 * if it does not exists. Return an instance of PO of ForumUser.
	 * @param request 
	 * @return
	 * 		Current user instance
	 */
	private ForumUser getCurrentUser(HttpServletRequest request) {
//			get login user (author)
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		//check whether this user exist or not
		Long sessionId = (Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
		ForumUser forumUser = forumService.getUserByUserAndSession(new Long(user.getUserID().intValue()),sessionId);
		if(forumUser == null){
			//if user not exist, create new one in database
			ForumToolSession session = forumService.getSessionBySessionId(sessionId);
			forumUser = new ForumUser(user,session);
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
  	    if ( forumService == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	      forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
  	    }
  	    return forumService;
  	}
	/**
	 * @param messageForm
	 * @param message
	 */
	private void setAttachment(MessageForm messageForm, Message message) {
		if(messageForm.getAttachmentFile() != null 
			&&  !StringUtils.isEmpty(messageForm.getAttachmentFile().getFileName())){
			forumService = getForumManager();
			Attachment att = forumService.uploadAttachment(messageForm.getAttachmentFile());
			//only allow one attachment, so replace whatever
			Set attSet = attSet = new HashSet();
			attSet.add(att);
			message.setAttachments(attSet);
		}
	}
}
