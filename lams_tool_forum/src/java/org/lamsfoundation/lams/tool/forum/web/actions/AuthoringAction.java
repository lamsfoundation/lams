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

package org.lamsfoundation.lams.tool.forum.web.actions;

import static org.lamsfoundation.lams.tool.forum.util.ForumConstants.OLD_FORUM_STYLE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.DateComparator;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumWebUtils;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthoringAction extends Action {
	private static Logger log = Logger.getLogger(AuthoringAction.class);
	private IForumService forumService;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, PersistenceException {
		
		String param = mapping.getParameter();
		//-----------------------Forum Author function ---------------------------
	  	if (param.equals("initPage")) {
	  		request.setAttribute(AttributeNames.ATTR_MODE,ToolAccessMode.AUTHOR);
       		return initPage(mapping, form, request, response);
        }

		// ***************** Monitoring define later screen ********************
		if (param.equals("defineLater")){
			//update define later flag to true
			request.setAttribute(AttributeNames.ATTR_MODE,ToolAccessMode.TEACHER);
			forumService = getForumManager();
			Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
			Forum forum = forumService.getForumByContentId(contentId);
			
			boolean isForumEditable = ForumWebUtils.isForumEditable(forum);
			if(!isForumEditable){
				request.setAttribute(ForumConstants.PAGE_EDITABLE, new Boolean(isForumEditable));
				return mapping.findForward("forbidden");
			}
			
			if(!forum.isContentInUse()){
				forum.setDefineLater(true);
				forumService.updateForum(forum);
			}
			
       		return initPage(mapping, form, request, response);
		}
        if (param.equals("updateContent")) {
       		return updateContent(mapping, form, request, response);
        }
        if (param.equals("uploadOnlineFile")) {
       		return uploadOnline(mapping, form, request, response);
        }
        if (param.equals("uploadOfflineFile")) {
       		return uploadOffline(mapping, form, request, response);
        }
        if (param.equals("deleteOnlineFile")) {
        	return deleteOnlineFile(mapping, form, request, response);
        }
        if (param.equals("deleteOfflineFile")) {
        	return deleteOfflineFile(mapping, form, request, response);
        }
        //-----------------------Topic function ---------------------------
	  	if (param.equals("newTopic")) {
       		return newTopic(mapping, form, request, response);
        }
	  	if (param.equals("createTopic")) {
       		return createTopic(mapping, form, request, response);
        }
	  	if (param.equals("editTopic")) {
	  		return editTopic(mapping, form, request, response);
	  	}
	  	if (param.equals("updateTopic")) {
	  		return updateTopic(mapping, form, request, response);
	  	}
        if (param.equals("viewTopic")) {
       		return viewTopic(mapping, form, request, response);
        }
        if (param.equals("deleteTopic")) {
        	return deleteTopic(mapping, form, request, response);
        }
        if (param.equals("deleteAttachment")) {
        	return deleteAttachment(mapping, form, request, response);
        }
        return mapping.findForward("error");
	}
	//******************************************************************************************************************
	//              Forum Author functions
	//******************************************************************************************************************

	/**
	 * This page will display initial submit tool content. Or just a blank page if the toolContentID does not
	 * exist before. 
	 *  
	 * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward initPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		
		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		ForumForm forumForm = (ForumForm)form;
		forumForm.setSessionMapID(sessionMap.getSessionID());
		forumForm.setContentFolderID(contentFolderID);
		sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
		
		//get back the topic list and display them on page
		forumService = getForumManager();

		List<MessageDTO> topics = null;
		Forum forum = null;
		try {
			forum = forumService.getForumByContentId(contentId);
			//if forum does not exist, try to use default content instead.
			if(forum == null){
				forum = forumService.getDefaultContent(contentId);
				if(forum.getMessages() != null){
					TreeMap map = new TreeMap(new DateComparator());
					//sorted by create date
					Iterator iter = forum.getMessages().iterator();
					while(iter.hasNext()){
						Message topic = (Message) iter.next();
						//contentFolderID != -1 means it is sysadmin: LDEV-906 
						if(topic.getCreatedBy() == null && !StringUtils.equals(contentFolderID,"-1")){
							//get login user (author)
							HttpSession ss = SessionManager.getSession();
							//get back login user DTO
							UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
							ForumUser fuser = new ForumUser(user,null);
							topic.setCreatedBy(fuser);
						}
						map.put(topic.getCreated(),topic);
					}
					topics = MessageDTO.getMessageDTO(new ArrayList(map.values()));
				}else
					topics = null;
			}else{
				topics = forumService.getAuthoredTopics(forum.getUid());
				//failure tolerance: if current contentID is defaultID, the createBy will be null.
				//contentFolderID != -1 means it is sysadmin: LDEV-906 
				if(!StringUtils.equals(contentFolderID,"-1"))
					for (MessageDTO messageDTO : topics) {
						if(StringUtils.isBlank(messageDTO.getAuthor())){
							//get login user (author)
							HttpSession ss = SessionManager.getSession();
							//get back login user DTO
							UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
							ForumUser fuser = new ForumUser(user,null);
							messageDTO.setAuthor(fuser.getFirstName()+" "+fuser.getLastName());
						}
					}
			}
			//initialize attachmentList
			List attachmentList = getAttachmentList(sessionMap);
			attachmentList.addAll(forum.getAttachments());

			//tear down PO to normal object using clone() method
			forumForm.setForum((Forum) forum.clone());
		} catch (Exception e) {
			log.error(e);
			return mapping.findForward("error");
		}
		
		//set back STRUTS component value
		//init it to avoid null exception in following handling
		if(topics == null)
			topics = new ArrayList<MessageDTO>();
		
		sessionMap.put(ForumConstants.AUTHORING_TOPICS_LIST, topics);
		return mapping.findForward("success");
	}
	/**
	 * Update all content for forum. These contents contain
	 * <ol>
	 * 	<li>Forum authoring infomation, e.g. online/offline instruction, title, advacnce options, etc.</li>
	 *  <li>Uploaded offline/online instruction files</li>
	 *  <li>Topics author created</li>
	 *  <li>Topics' attachment file</li>
	 *  <li>Author user information</li>
	 * </ol>
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateContent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		
		ToolAccessMode mode = getAccessMode(request); 
		ForumForm forumForm = (ForumForm)(form);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(forumForm.getSessionMapID());
		//validation
		ActionMessages errors = validate(forumForm, mapping, request);
		if(!errors.isEmpty()){
			saveErrors(request, errors);
			if(mode.isAuthor())
	    		return mapping.findForward("author");
	    	else
	    		return mapping.findForward("monitor");			
		}
			
		Forum forum = forumForm.getForum();
		//get back tool content ID
		forum.setContentId(forumForm.getToolContentID());
		try {
			forumService = getForumManager();
			
			//*******************************Handle user*******************
			String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
			ForumUser forumUser = null;
			//check whether it is sysadmin:LDEV-906 
			if(!StringUtils.equals(contentFolderID,"-1" )){
				//try to get form system session
				HttpSession ss = SessionManager.getSession();
				//get back login user DTO
				UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
				forumUser = forumService.getUserByID(new Long(user.getUserID().intValue()));
				if(forumUser == null){
					forumUser = new ForumUser(user,null);
					forumService.createUser(forumUser);
				}
			}			
			//**********************************Get Forum PO*********************
			Forum forumPO = forumService.getForumByContentId(forumForm.getToolContentID());
			if(forumPO == null){
				//new Forum, create it.
				forumPO = forum;
				forumPO.setContentId(forumForm.getToolContentID());
			}else{
				if(mode.isAuthor()){
					Long uid = forumPO.getUid();
					PropertyUtils.copyProperties(forumPO,forum);
					//get back UID
					forumPO.setUid(uid);
				}else{
//					if it is Teacher, then just update basic tab content (definelater)
					forumPO.setInstructions(forum.getInstructions());
					forumPO.setTitle(forum.getTitle());
//					change define later status
					forumPO.setDefineLater(false);
				}
			}
			forumPO.setCreatedBy(forumUser);
			
			//**********************************Handle Attachement*********************
	    	//merge attachment info
			Set attPOSet = forumPO.getAttachments();
			if(attPOSet == null)
				attPOSet = new HashSet();
			List attachmentList = getAttachmentList(sessionMap);
			List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
			Iterator iter = attachmentList.iterator();
			while(iter.hasNext()){
				Attachment newAtt = (Attachment) iter.next();
				attPOSet.add(newAtt);
			}
			attachmentList.clear();
			
			iter = deleteAttachmentList.iterator();
			while(iter.hasNext()){
				Attachment delAtt = (Attachment) iter.next();
				iter.remove();
				//it is an existed att, then delete it from current attachmentPO
				if(delAtt.getUid() != null){
					Iterator attIter = attPOSet.iterator();
					while(attIter.hasNext()){
						Attachment att = (Attachment) attIter.next();
						if(delAtt.getUid().equals(att.getUid())){
							attIter.remove();
							break;
						}
					}
					forumService.deleteForumAttachment(delAtt.getUid());
				}//end remove from persist value
			}
			
			//copy back
			forumPO.setAttachments(attPOSet);
			forum = forumService.updateForum(forumPO);
			
			//********************************Handle topic*******************
			//delete message attachment
			List topicDeleteAttachmentList = getTopicDeletedAttachmentList(sessionMap);
			iter = topicDeleteAttachmentList.iterator();
			while(iter.hasNext()){
				Attachment delAtt = (Attachment) iter.next();
				iter.remove();
			}
			
			//Handle message
			List topics = getTopicList(sessionMap);
	    	iter = topics.iterator();
	    	while(iter.hasNext()){
	    		MessageDTO dto = (MessageDTO) iter.next();
	    		if(dto.getMessage() != null){
    				//This flushs user UID info to message if this user is a new user. 
    				dto.getMessage().setCreatedBy(forumUser);
    				dto.getMessage().setModifiedBy(forumUser);
	    			forumService.createRootTopic(forum.getUid(),null,dto.getMessage());
	    		}
	    	}
	    	//delete them from database.
	    	List delTopics = getDeletedTopicList(sessionMap);
	    	iter = delTopics.iterator();
	    	while(iter.hasNext()){
	    		MessageDTO dto = (MessageDTO) iter.next();
	    		iter.remove();
	    		if(dto.getMessage() != null && dto.getMessage().getUid() != null)
	    			forumService.deleteTopic(dto.getMessage().getUid());
	    	}

			//initialize attachmentList again
			attachmentList = getAttachmentList(sessionMap);
			attachmentList.addAll(forum.getAttachments());
		} catch (Exception e) {
			log.error(e);
		}
		
		
    	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
    	if(mode.isAuthor())
    		return mapping.findForward("author");
    	else
    		return mapping.findForward("monitor");
	}

	/**
	 * Handle upload online instruction files request. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadOnline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE,request);
	}
	/**
	 * Handle upload offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward uploadOffline(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE,request);
	}
	/**
	 * Common method to upload online or offline instruction files request.
	 * @param mapping
	 * @param form
	 * @param type
	 * @param request
	 * @return
	 */
	private ActionForward uploadFile(ActionMapping mapping, ActionForm form,
			String type,HttpServletRequest request) {

		ForumForm forumForm = (ForumForm) form;
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(forumForm.getSessionMapID());
		
		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) forumForm.getOfflineFile();
		else
			file = (FormFile) forumForm.getOnlineFile();
		
		if(file == null || StringUtils.isBlank(file.getFileName()))
			return mapping.findForward("success");
		
		ActionMessages errors = new ActionMessages();
		FileValidatorUtil.validateFileSize(file, true, errors );
		if(!errors.isEmpty()){
			this.saveErrors(request, errors);
			return mapping.findForward("success");
		}
		
		forumService = getForumManager();
		//upload to repository
		Attachment att = forumService.uploadInstructionFile(file, type);
		
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		Attachment existAtt;
		while(iter.hasNext()){
			existAtt = (Attachment) iter.next();
			if(StringUtils.equals(existAtt.getFileName(),att.getFileName())){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
				break;
			}
		}
		//add to attachmentList
		attachmentList.add(att);
		
		return mapping.findForward("success");

	}


	/**
	 * Delete offline instruction file from current Forum authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, form,request, response, IToolContentHandler.TYPE_OFFLINE);
	}
	/**
	 * Delete online instruction file from current Forum authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(mapping, form,request, response, IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * @param request
	 * @param response
	 * @param form 
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(ActionMapping mapping,ActionForm form,HttpServletRequest request, HttpServletResponse response, String type){
		Long versionID = new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuID"));
		//get sessionMAP
		String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		//handle session value
		List attachmentList = getAttachmentList(sessionMap);
		List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		Attachment existAtt;
		while(iter.hasNext()){
			existAtt = (Attachment) iter.next();
			if(existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
			}
				
		}
		
		request.setAttribute(ForumConstants.ATTR_FILE_TYPE_FLAG, type);
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward("success");
	}
	
	//******************************************************************************************************************
	//              Topic functions
	//******************************************************************************************************************
	/**
	 * Display emtpy topic page for user input topic information. This page will contain all topics list which 
	 * this author posted before.
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID );
		((MessageForm)form).setSessionMapID(sessionMapID);
		
		return mapping.findForward("success");
	}
	/**
	 * Create a topic in memory. This topic will be saved when user save entire authoring page.
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
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(messageForm.getSessionMapID());
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, messageForm.getSessionMapID());
		
		List topics = getTopicList(sessionMap);
		//get login user (author)
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		//get message info from web page
		Message message = messageForm.getMessage();
		//init some basic variables for first time create
		message.setIsAuthored(true);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		
		//check whether this user exist or not
		ForumUser forumUser = forumService.getUserByID(new Long(user.getUserID().intValue()));
		String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
		//check whether it is sysadmin:LDEV-906 
		if(forumUser == null && !StringUtils.equals(contentFolderID,"-1" )){
			//if user not exist, create new one in database
			forumUser = new ForumUser(user,null);
		}
		message.setCreatedBy(forumUser);
		//same person with create at first time
		message.setModifiedBy(forumUser);
		
		//set attachment of this topic
		Set attSet = null;
		if(messageForm.getAttachmentFile() != null 
			&&  !StringUtils.isEmpty(messageForm.getAttachmentFile().getFileName())){
			forumService = getForumManager();
			Attachment att = forumService.uploadAttachment(messageForm.getAttachmentFile());
			//only allow one attachment, so replace whatever
			attSet = new HashSet();
			attSet.add(att);
		}
		message.setAttachments(attSet);
		
		//save the new message into HttpSession
		if(OLD_FORUM_STYLE)
			topics.add(MessageDTO.getMessageDTO(message));
		else
			topics.add(0,MessageDTO.getMessageDTO(message));
		
		
		return mapping.findForward("success");
	}
    /**
     * Diplay a special topic information. Only display author created root message content,  even this topic already
     * has some reply messages from learner in some cases. 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward viewTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	
    	//get SessionMAP
		String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID );
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		
    	List topics = getTopicList(sessionMap);
    	String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		
		//if topicIndex is empty, try to get it from Request Attribute again.
		//This may be caused by reresh this page, back from edit page etc.
		if(topicIdx == -1){
			topicIndex = (String) request.getAttribute(ForumConstants.AUTHORING_TOPICS_INDEX);
			topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		}
		
		if(topicIdx != -1){
			MessageDTO topic = (MessageDTO) topics.get(topicIdx);
			request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
			request.setAttribute(ForumConstants.AUTHORING_TOPIC,topic);
    	}
		
    	return mapping.findForward("success");
    	
    }
	/**
	 * Delete a topic form current topic list. But database record will be deleted only when user save whole authoring 
	 * page. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws PersistenceException
	 */
    public ActionForward deleteTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
		//get SessionMAP
		String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID );
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID,sessionMapID);
    	List topics = getTopicList(sessionMap);
		String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);

		if(topicIdx != -1){
			Object obj = topics.remove(topicIdx);
			List delList = getDeletedTopicList(sessionMap);
			delList.add(obj);
    	}
		
		return mapping.findForward("success");
	}

    /**
     * Display a HTML FORM which contains subject, body and attachment information from a special topic. This page
     * is ready for user update this topic.
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
    	
//    	get SessionMAP
		String sessionMapID = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID );
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		MessageForm msgForm = (MessageForm)form;
		msgForm.setSessionMapID(sessionMapID);
		
    	List topics = getTopicList(sessionMap);
    	String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		if(topicIdx != -1){
			MessageDTO topic = (MessageDTO) topics.get(topicIdx);
			if (topic != null) {
				//check whehter the edit topic and the current user are same person, if not, forbidden to edit topic
				if(topic.getMessage() != null && topic.getMessage().getCreatedBy() != null){
					//get login user (author)
					HttpSession ss = SessionManager.getSession();
					//get back login user DTO
					UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
					Long topicAuthorId = topic.getMessage().getCreatedBy().getUserId();
					if(!(new Long(user.getUserID().intValue()).equals(topicAuthorId)))
						return mapping.findForward("forbiden");
				}
				//update message to HTML Form to echo back to web page: for subject, body display
				msgForm.setMessage(topic.getMessage());
			}
			//echo back to web page: for attachment display
			request.setAttribute(ForumConstants.AUTHORING_TOPIC,topic);
    	}
		
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
    	return mapping.findForward("success");
    }
    
    /**
     * Submit user updated inforamion in a topic to memory. This update will be submit to database only when user
     * save whole authoring page.
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
    	MessageForm messageForm = (MessageForm) form;
    	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(messageForm.getSessionMapID());
    	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, messageForm.getSessionMapID());
    	
    	//get value from HttpSession
    	List topics = getTopicList(sessionMap);
		//get param from HttpServletRequest
		String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		
		if(topicIdx != -1){
			Message message = messageForm.getMessage();
			
			MessageDTO newMsg = (MessageDTO) topics.get(topicIdx);
			if(newMsg.getMessage()== null)
				newMsg.setMessage(new Message());
			
			newMsg.getMessage().setSubject(message.getSubject());
			newMsg.getMessage().setBody(message.getBody());
			newMsg.getMessage().setUpdated(new Date());
			//update attachment
			if(messageForm.getAttachmentFile() != null 
					&&  !StringUtils.isEmpty(messageForm.getAttachmentFile().getFileName())){
				forumService = getForumManager();
				Attachment att = forumService.uploadAttachment(messageForm.getAttachmentFile());
				//only allow one attachment, so replace whatever
				Set attSet = attSet = new HashSet();
				attSet.add(att);
				newMsg.setHasAttachment(true);
				newMsg.getMessage().setAttachments(attSet);
			}else if(!messageForm.isHasAttachment()){
				Set att = newMsg.getMessage().getAttachments();
				if(att != null && att.size() > 0){
					List delTopicAtt = getTopicDeletedAttachmentList(sessionMap);
					delTopicAtt.add(att.iterator().next());
				}
				newMsg.setHasAttachment(false);
				newMsg.getMessage().setAttachments(null);
			}
		}
		
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
		return mapping.findForward("success");
    }
	/**
	 * Remove message attachment.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("itemAttachment", null);
    	return mapping.findForward("success");
    }

	//******************************************************************************************************************
	//              Private method for internal functions
	//******************************************************************************************************************
  	private IForumService getForumManager() {
  	    if ( forumService == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	      forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
  	    }
  	    return forumService;
  	}
	/**
	 * @param request
	 * @return
	 */
	private List getAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,ForumConstants.ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,ForumConstants.DELETED_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getTopicList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,ForumConstants.AUTHORING_TOPICS_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getTopicDeletedAttachmentList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,ForumConstants.DELETED_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedTopicList(SessionMap sessionMap) {
		return getListFromSession(sessionMap,ForumConstants.DELETED_AUTHORING_TOPICS_LIST);
	}
	/**
	 * Get <code>java.util.List</code> from HttpSession by given name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	private List getListFromSession(SessionMap sessionMap,String name) {
		List list = (List) sessionMap.get(name);
		if(list == null){
			list = new ArrayList();
			sessionMap.put(name,list);
		}
		return list;
	}
	
	/**
	 * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
	 * @param request
	 * @return
	 */
	private ToolAccessMode getAccessMode(HttpServletRequest request) {
		ToolAccessMode mode;
		String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
		if(StringUtils.equalsIgnoreCase(modeStr,ToolAccessMode.TEACHER.toString()))
			mode = ToolAccessMode.TEACHER;
		else
			mode = ToolAccessMode.AUTHOR;
		return mode;
	}
	
	  /**
     * Forum validation method from STRUCT interface.
     * 
     */
    public ActionMessages validate( ForumForm form,ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
    	ActionMessages errors = new ActionMessages();
    	
    	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(form.getSessionMapID());
		ActionMessage ae;
		try {
			String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
//			if (StringUtils.isBlank(form.getForum().getTitle())) {
//				ActionMessage error = new ActionMessage("error.title.empty");
//				errors.add(ActionMessages.GLOBAL_MESSAGE, error);
//			}
			boolean allowNewTopic = form.getForum().isAllowNewTopic();
//			define it later mode(TEACHER): need read out AllowNewTopic flag rather than get from HTML form
			//becuase defineLater does not include this field
			if(StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())){
				forumService = getForumManager();
				Forum forumPO = forumService.getForumByContentId(form.getToolContentID());
				if(forumPO != null)
					allowNewTopic = forumPO.isAllowNewTopic();
				else{
					//failure tolerance
					log.error("ERROR: Can not found Forum by toolContentID:"+ form.getToolContentID());
					allowNewTopic = true;
				}
			}
			if(!allowNewTopic){
				List topics = getTopicList(sessionMap);
				if(topics.size() == 0){
					ActionMessage error = new ActionMessage("error.must.have.topic");
					errors.add(ActionMessages.GLOBAL_MESSAGE, error);
				}
			}
			//define it later mode(TEACHER) skip below validation.
			if(StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())){
				return errors;
			}
			if(!form.getForum().isAllowRichEditor()){
				if(form.getForum().getLimitedChar() <=0){
					ActionMessage error = new ActionMessage("error.limit.char.less.zero");
					errors.add(ActionMessages.GLOBAL_MESSAGE, error);
				}
			}
			if(!form.getForum().isAllowNewTopic()){
				if(form.getForum().getMaximumReply() !=0 
						&& (form.getForum().getMaximumReply() < form.getForum().getMinimumReply())){
					ActionMessage error = new ActionMessage("error.min.less.max");
					errors.add(ActionMessages.GLOBAL_MESSAGE, error);
				}
			}
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		return errors;
	}
    private float convertToMeg( int numBytes ) {
        return numBytes != 0 ? numBytes / 1024 / 1024 : 0;
    }
    

}
