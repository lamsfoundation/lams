package org.lamsfoundation.lams.tool.forum.web.actions;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.permissions.Permission;
import org.lamsfoundation.lams.tool.forum.permissions.PermissionManager;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 24/06/2005
 * Time: 10:54:09
 * To change this template use File | Settings | File Templates.
 */
public class LearningAction extends Action {
  	private static Logger log = Logger.getLogger(LearningAction.class);
	private IForumService forumService;


   public final ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
  		String param = mapping.getParameter();
	  	if (param.equals("viewForum")) {
       		return viewForm(mapping, form, request, response);
        }
	  	if (param.equals("viewThread")) {
       		return viewThread(mapping, form, request, response);
        }
	  	if (param.equals("newTopic")) {
       		return newTopic(mapping, form, request, response);
        }
	  	if (param.equals("replyTopic")) {
	  		return replyTopic(mapping, form, request, response);
	  	}
	  	if (param.equals("createTopic")) {
       		return createTopic(mapping, form, request, response);
        }
        if (param.equals("deleteTopic")) {
       		return deleteTopic(mapping, form, request, response);
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
   
	private ActionForward viewForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long forumId = new Long(WebUtil.readLongParam(request,"forumId"));
		forumService = getForumManager();
		List rootTopics = forumService.getRootTopics(forumId);
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_LIST,MessageDTO.getMessageDTO(rootTopics));
		return mapping.findForward("success");
	}

	private ActionForward viewThread(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long topicId = new Long(WebUtil.readLongParam(request,"topicId"));
		forumService = getForumManager();
		List topics = forumService.getTopics(topicId);
		request.setAttribute(ForumConstants.AUTHORING_TOPICS,topics);
		return mapping.findForward("success");
	}

	private ActionForward replyTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long parentId = new Long(WebUtil.readLongParam(request,"parentId"));
		
		MessageForm messageForm = (MessageForm) form;
		Message message = messageForm.getMessage();
		message.setIsAuthored(false);
		message.setCreated(new Date());
		message.setUpdated(new Date());
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
//		get login user (author)
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		message.setCreatedBy(new ForumUser(user));
		//save message into database
		forumService = getForumManager();
		forumService.replyToMessage(parentId,message);
		return mapping.findForward("success");
	}
	
	private ActionForward newTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
		if (topics == null) {
			topics = new ArrayList();
		}
		request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, topics);
		return mapping.findForward("success");
	}
	public ActionForward createTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, PersistenceException {
		
		List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
		Long forumId = (Long) request.getSession().getAttribute(ForumConstants.FORUM_ID);
		//get login user (author)
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		MessageForm messageForm = (MessageForm) form;
		Message message = messageForm.getMessage();
		message.setIsAuthored(true);
		message.setCreated(new Date());
		message.setUpdated(new Date());
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
		message.setCreatedBy(new ForumUser(user));
		
		if (topics == null) {
			topics = new ArrayList();
		}
		//save message into database
		forumService = getForumManager();
		forumService.createMessage(forumId,message);
		
		topics.add(MessageDTO.getMessageDTO(message,user.getFirstName()+" "+user.getLastName()));
		
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"CREATE_SUCCESS");
		request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, topics);
		return mapping.findForward("success");
	}

    public ActionForward deleteTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	
    	List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
		String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);

		if(topicIdx != -1){
			MessageDTO topic = (MessageDTO) topics.remove(topicIdx);
			if (topic != null && topic.getMessage() != null && topic.getMessage().getUid() != null) {
				getForumManager().deleteMessage(topic.getMessage().getUid());
			}
			request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST,topics);
    	}
		
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"DELETE_SUCCESS");
		return mapping.getInputForward();
	}
    
  	private IForumService getForumManager() {
  	    if ( forumService == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	      forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
  	    }
  	    return forumService;
  	}
  	public ActionForward editTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	MessageForm msgForm = (MessageForm)form;
    	List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
    	String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		if(topicIdx != -1){
			MessageDTO topic = (MessageDTO) topics.get(topicIdx);
			if (topic != null) {
				msgForm.setMessage(topic.getMessage());
			}
			request.setAttribute(ForumConstants.AUTHORING_TOPICS,topic);
    	}
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
    	return mapping.findForward("success");
    }
    
    public ActionForward updateTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	//get value from HttpSession
		List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
		Long forumId = (Long) request.getSession().getAttribute(ForumConstants.FORUM_ID);
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
			//save message into database
			forumService = getForumManager();
			forumService.createMessage(forumId,newMsg.getMessage());
		}
		
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
		request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, topics);
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"EDIT_SUCCESS");
		return mapping.findForward("success");
    }
    
    public ActionForward deleteAttachment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
		Long versionID = new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuid"));
		forumService = getForumManager();
		forumService.deleteFromRepository(uuID,versionID);
//		get value from HttpSession
    	List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
    	Long forumId = (Long) request.getSession().getAttribute(ForumConstants.FORUM_ID);
    	//get param from HttpServletRequest
		String topicIndex = (String) request.getParameter(ForumConstants.AUTHORING_TOPICS_INDEX);
		
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		
		if(topicIdx != -1){
			MessageDTO newMsg = (MessageDTO) topics.get(topicIdx);
			if(newMsg.getMessage()== null)
				newMsg.setMessage(new Message());
			newMsg.getMessage().setUpdated(new Date());
			newMsg.setHasAttachment(false);
			newMsg.getMessage().setAttachments(null);
			//save message into database
			forumService = getForumManager();
			forumService.createMessage(forumId,newMsg.getMessage());
			
		}
		request.setAttribute(ForumConstants.SUCCESS_FLAG,"ATT_SUCCESS_FLAG");
		request.setAttribute(ForumConstants.AUTHORING_TOPICS_INDEX,topicIndex);
    	return mapping.findForward("success");
    }
    
}
