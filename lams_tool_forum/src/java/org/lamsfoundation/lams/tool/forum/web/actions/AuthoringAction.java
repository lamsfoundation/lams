/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.web.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
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
        if (param.equals("finishTopic")) {
       		return finishTopic(mapping, form, request, response);
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

		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		ForumForm forumForm = (ForumForm)form;
		//get back the topic list and display them on page
		forumService = getForumManager();

		List topics = null;
		Forum forum = null;
		try {
			forum = forumService.getForumByContentId(contentId);
			//if forum does not exist, try to use default content instead.
			if(forum == null){
				forum = forumService.getDefaultForum(); 
			}
			topics = forumService.getAuthoredTopics(contentId);
			//initialize attachmentList
			List attachmentList = getAttachmentList(request);
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
			topics = new ArrayList();
		request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, topics);
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

		ForumForm forumForm = (ForumForm)(form);
		Forum forum = forumForm.getForum();
		try {
			forumService = getForumManager();
			//if there are new user
			ForumUser forumUser = (ForumUser) request.getSession().getAttribute(ForumConstants.NEW_FORUM_USER);
			if(forumUser != null){
				forumService.createUser(forumUser);
			}
			request.getSession().setAttribute(ForumConstants.NEW_FORUM_USER,null);
			
			Forum forumPO = forumService.getForumByContentId(forumForm.getToolContentID());
			if(forumPO != null && forumForm.getToolContentID().equals(forum.getContentId()) ){
				//merge web page change into PO
				
		    	//merge attachment info
				Set attPOSet = forumPO.getAttachments();
				List attachmentList = getAttachmentList(request);
				List deleteAttachmentList = getDeletedAttachmentList(request);
				Iterator iter = attachmentList.iterator();
				while(iter.hasNext()){
					Attachment newAtt = (Attachment) iter.next();
					//add new attachment, UID is not null
					if(newAtt.getUid() == null)
						attPOSet.add(newAtt);
				}
				attachmentList.clear();
				
				iter = deleteAttachmentList.iterator();
				while(iter.hasNext()){
					Attachment delAtt = (Attachment) iter.next();
					//delete from repository
					forumService.deleteFromRepository(delAtt.getFileUuid(),delAtt.getFileVersionId());
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
				deleteAttachmentList.clear();
				
				//copy back
				forumPO.setAttachments(attPOSet);
			}else{
				//new Forum, create it.
				forumPO = forum;
				forumPO.setContentId(forumForm.getToolContentID());
			}
			forum = forumService.updateForum(forumPO);
			
			//delete message attachment
			List topicDeleteAttachmentList = getTopicDeletedAttachmentList(request);
			Iterator iter = topicDeleteAttachmentList.iterator();
			while(iter.hasNext()){
				Attachment delAtt = (Attachment) iter.next();
				//delete from repository
				forumService.deleteFromRepository(delAtt.getFileUuid(),delAtt.getFileVersionId());
			}
			topicDeleteAttachmentList.clear();
			
			//Handle message
			List topics = getTopicList(request);
	    	iter = topics.iterator();
	    	while(iter.hasNext()){
	    		MessageDTO dto = (MessageDTO) iter.next();
	    		if(dto.getMessage() != null)
	    			forumService.createRootTopic(forum.getUid(),null,dto.getMessage());
	    	}
	    	//delete them from database.
	    	List delTopics = getDeletedTopicList(request);
	    	iter = delTopics.iterator();
	    	while(iter.hasNext()){
	    		MessageDTO dto = (MessageDTO) iter.next();
	    		if(dto.getMessage() != null)
	    			forumService.deleteTopic(dto.getMessage().getUid());
	    	}
	    	delTopics.clear();

			//initialize attachmentList again
			List attachmentList = getAttachmentList(request);
			attachmentList.addAll(forum.getAttachments());
		} catch (Exception e) {
			log.error(e);
		}
		return mapping.findForward("success");
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
		
		FormFile file;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type))
			file = (FormFile) forumForm.getOfflineFile();
		else
			file = (FormFile) forumForm.getOnlineFile();
		
		forumService = getForumManager();
		//upload to repository
		Attachment att = forumService.uploadInstructionFile(file, type);
		
		//handle session value
		List attachmentList = getAttachmentList(request);
		List deleteAttachmentList = getDeletedAttachmentList(request);
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
		
		//update Html FORM, this will echo back to web page for display
		List list;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type)){
			list = forumForm.getOfflineFileList();
			if(list == null){
				list = new ArrayList();
				forumForm.setOfflineFileList(list);
			}
		}else{
			list = forumForm.getOnlineFileList();
			if(list == null){
				list = new ArrayList();
				forumForm.setOnlineFileList(list);
			}
		}
		list.add(att);
		
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
		return deleteFile(request, response,form, IToolContentHandler.TYPE_OFFLINE);
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
		return deleteFile(request, response,form, IToolContentHandler.TYPE_ONLINE);
	}

	/**
	 * @param request
	 * @param response
	 * @param form 
	 * @param type 
	 * @return
	 */
	private ActionForward deleteFile(HttpServletRequest request, HttpServletResponse response, ActionForm form, String type) {
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		Long versionID = new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuID"));
		
		//handle session value
		List attachmentList = getAttachmentList(request);
		List deleteAttachmentList = getDeletedAttachmentList(request);
		//first check exist attachment and delete old one (if exist) to deletedAttachmentList
		Iterator iter = attachmentList.iterator();
		Attachment existAtt;
		while(iter.hasNext()){
			existAtt = (Attachment) iter.next();
			if(existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)){
				//if there is same name attachment, delete old one
				deleteAttachmentList.add(existAtt);
				iter.remove();
				break;
			}
		}
		
		//handle web page display
		List leftAttachments;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type)){
			leftAttachments = ((ForumForm)form).getOfflineFileList();
		}else{
			leftAttachments = ((ForumForm)form).getOnlineFileList();
		}
		iter = leftAttachments.iterator();
		while(iter.hasNext()){
			Attachment att = (Attachment) iter.next();
			if(versionID.equals(att.getFileVersionId()) && uuID.equals(att.getFileUuid())){
				iter.remove();
				break;
			}
		}
		StringBuffer sb = new StringBuffer();
		iter = leftAttachments.iterator();
		while(iter.hasNext()){
			Attachment file = (Attachment) iter.next();
			sb.append("<li>").append(file.getFileName()).append("\r\n");
			sb.append(" <a href=\"javascript:launchInstructionsPopup('download/?uuid=").append(file.getFileUuid()).append("&preferDownload=false')\">");
			sb.append(this.getResources(request).getMessage("label.view"));
			sb.append("</a>\r\n");
			sb.append(" <a href=\"../download/?uuid=").append(file.getFileUuid()).append("&preferDownload=true\">");
			sb.append(this.getResources(request).getMessage("label.download"));
			sb.append("</a>\r\n");
			sb.append("<a href=\"javascript:loadDoc('");
			sb.append(ForumConstants.TOOL_URL_BASE);
			sb.append("deletefile.do?method=");
			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
				sb.append("deleteOffline");
			else
				sb.append("deleteOnline");
			sb.append("File&toolContentID=").append(contentID);
			sb.append("&uuID=").append(file.getFileUuid()).append("&versionID=").append(file.getFileVersionId()).append("','");
			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
				sb.append("offlinefile");
			else
				sb.append("onlinefile");
			sb.append("')\">");
			
			if(StringUtils.equals(type,IToolContentHandler.TYPE_OFFLINE))
				sb.append(this.getResources(request).getMessage("label.authoring.offline.delete"));
			else
				sb.append(this.getResources(request).getMessage("label.authoring.online.delete"));
			sb.append("</a></li>\r\n");
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			log.error(e);
		}
		return null;
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
		
		List topics = getTopicList(request);
		//get login user (author)
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		//get message info from web page
		MessageForm messageForm = (MessageForm) form;
		Message message = messageForm.getMessage();
		//init some basic variables for first time create
		message.setIsAuthored(true);
		message.setCreated(new Date());
		message.setUpdated(new Date());
		message.setLastReplyDate(new Date());
		//check whether this user exist or not
		ForumUser forumUser = forumService.getUserByUserAndSession(new Long(user.getUserID().intValue()),null);
		if(forumUser == null){
			//if user not exist, create new one in database
			forumUser = new ForumUser(user,null);
			request.getSession().setAttribute(ForumConstants.NEW_FORUM_USER,forumUser);
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
		topics.add(MessageDTO.getMessageDTO(message));
		
		//echo back to web page
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"CREATE_SUCCESS");
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
    	
    	List topics = getTopicList(request);
		String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);

		if(topicIdx != -1){
			Object obj = topics.remove(topicIdx);
			List delList = getDeletedTopicList(request);
			delList.add(obj);
    	}
		
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"DELETE_SUCCESS");
		return mapping.getInputForward();
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
    	
    	List topics = getTopicList(request);
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
    	
    	MessageForm msgForm = (MessageForm)form;
    	List topics = getTopicList(request);
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
    	//get value from HttpSession
    	List topics = getTopicList(request);
		//get param from HttpServletRequest
		String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		
		if(topicIdx != -1){
			MessageForm messageForm = (MessageForm) form;
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
			}
		}
		
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"EDIT_SUCCESS");
		return mapping.findForward("success");
    }
    /**
     * Delete a topic's attachment file. This update will be submit to database only when user
     * save whole authoring page.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws PersistenceException
     */
    public ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {

//		get value from HttpSession
    	List topics = getTopicList(request);
    	//get param from HttpServletRequest
		String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		
		if(topicIdx != -1){
			MessageDTO newMsg = (MessageDTO) topics.get(topicIdx);
			if(newMsg.getMessage()== null)
				newMsg.setMessage(new Message());
			//add delete topic attachment to HTTPSession
			Set attSet = newMsg.getMessage().getAttachments();
			if(attSet != null){
				//only one attachment for topic
				Attachment att = (Attachment) attSet.iterator().next();
				if(att != null){
					List topicDeletedAttachmentList = getTopicDeletedAttachmentList(request);
					topicDeletedAttachmentList.add(att);
				}
			}
			//set other infor about attachment
			newMsg.getMessage().setUpdated(new Date());
			newMsg.setHasAttachment(false);
			newMsg.getMessage().setAttachments(null);
		}
		
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"ATT_SUCCESS_FLAG");
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
    	return mapping.findForward("success");
    }
    
    public ActionForward finishTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	List topics = getTopicList(request);
    	
    	ForumForm forumForm = (ForumForm)form;
    	Forum forum = forumForm.getForum();
    	Set msgSet = new HashSet();
		Iterator iter = topics.iterator();
		while(iter.hasNext()){
			MessageDTO dto = (MessageDTO) iter.next();
			msgSet.add(dto.getMessage());
		}
    	forum.setMessages(msgSet);
    	
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
	private List getAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ForumConstants.ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ForumConstants.DELETED_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getTopicList(HttpServletRequest request) {
		return getListFromSession(request,ForumConstants.AUTHORING_TOPICS_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getTopicDeletedAttachmentList(HttpServletRequest request) {
		return getListFromSession(request,ForumConstants.DELETED_ATTACHMENT_LIST);
	}
	/**
	 * @param request
	 * @return
	 */
	private List getDeletedTopicList(HttpServletRequest request) {
		return getListFromSession(request,ForumConstants.DELETED_AUTHORING_TOPICS_LIST);
	}
	/**
	 * Get <code>java.util.List</code> from HttpSession by given name.
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	private List getListFromSession(HttpServletRequest request,String name) {
		List list = (List) request.getSession().getAttribute(name);
		if(list == null){
			list = new ArrayList();
			request.getSession().setAttribute(name,list);
		}
		return list;
	}
}
