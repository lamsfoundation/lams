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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MessageForm;
import org.lamsfoundation.lams.util.WebUtil;
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
	  	if (param.equals("initPage")) {
       		return initPage(mapping, form, request, response);
        }
	  	if (param.equals("newTopic")) {
       		return newTopic(mapping, form, request, response);
        }
	  	if (param.equals("createTopic")) {
       		return createTopic(mapping, form, request, response);
        }
        if (param.equals("deleteTopic")) {
       		return deleteTopic(mapping, form, request, response);
        }
        if (param.equals("viewTopic")) {
       		return viewTopic(mapping, form, request, response);
        }
        if (param.equals("finishTopic")) {
       		return finishTopic(mapping, form, request, response);
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
        return mapping.findForward("error");
	}

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

		
		Long contentId = new Long(WebUtil.readLongParam(request,AttributeNames.TOOL_CONTENT_ID));
		ForumForm forumForm = (ForumForm)form;
		//get back the topic list and display them on page
		forumService = getForumManager();
		
		List topics = null;
		Forum forum = null;
		try {
			forum = forumService.getForumByContentId(contentId);
			if(forum != null){
				topics = forumService.getTopics(forum.getUid());
				forumForm.setForum(forum);
			}
			forumForm.setToolContentID(contentId);
		} catch (PersistenceException e) {
			log.error(e);
			return mapping.findForward("error");
		}
		
		//set back STRUTS component value
		request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, topics);
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
		MessageForm messageForm = (MessageForm) form;
		Message message = messageForm.getMessage();
		message.setIsAuthored(true);
		message.setCreated(new Date());
//		TODO: get user from shared session
//		message.setCreatedBy();
		if (topics == null) {
			topics = new ArrayList();
		}
		topics.add(message);
		
		request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST, topics);
		return mapping.findForward("success");
	}

    public ActionForward deleteTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	
    	List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
		String topicIndex = (String) request.getParameter("topicIndex");
		int topicIdx = NumberUtils.stringToInt(topicIndex,-1);
		if(topicIdx != -1){
			Message topic = (Message) topics.remove(topicIdx);
			if (topic != null && topic.getUid() != null) {
				getForumManager().deleteMessage(topic.getUid());
			}
			request.getSession().setAttribute(ForumConstants.AUTHORING_TOPICS_LIST,topics);
    	}

		StringBuffer sb = new StringBuffer();
		sb.append("<table width=\"100%\" cellspacing=\"8\" align=\"CENTER\" cellspacing=\"3\" cellpadding=\"3\" class=\"form\">");
		sb.append("<tr>");
		sb.append("<td valign=\"MIDDLE\"><b>Topic</b></td>");
		sb.append("<td colspan=\"2\" />");
		sb.append("</tr>");
		Iterator iter = topics.iterator();
		for(int idx=0;iter.hasNext();idx++){
			Message msg = (Message) iter.next();
			sb.append("<tr>");
			sb.append("<td valign=\"MIDDLE\">");
			sb.append(msg.getSubject());
			sb.append("</td>");
			sb.append("<td colspan=\"2\" valign=\"MIDDLE\"><a href=\"javascript:loadDoc('");
			sb.append(ForumConstants.TOOL_URL_BASE);
			sb.append("authoring/deleteTopic.do?topicIndex=");
			sb.append(idx);
			sb.append("\','topiclist')\"><b>Delete</b></a></td>"); 
		    sb.append("</tr>");
		}
		sb.append("</table>");
		try {
			PrintWriter out = response.getWriter();
			out.print(sb.toString());
			out.flush();
		} catch (IOException e) {
			log.error(e);
		}
		return null;
	}
    public ActionForward viewTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	//TODO
    	return null;
    	
    }
    
    public ActionForward finishTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws PersistenceException {
    	List topics = (List) request.getSession().getAttribute(ForumConstants.AUTHORING_TOPICS_LIST);
    	ForumForm forumForm = (ForumForm)form;
    	//TODO: topic sequence not write
    	forumForm.getForum().setMessages(new HashSet(topics));
    	
    	return mapping.findForward("success");
    	
    }
	/**
	 * Update all content for submit tool except online/offline instruction
	 * files list.
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
		Set topics = forum.getMessages();
		Set attachment = forum.getAttachments();
		try {
			forumService = getForumManager();
			Forum persistContent = forumService.getForumByContentId(forumForm.getToolContentID());
			if(persistContent != null && forum.getContentId().equals(persistContent.getContentId())){
				//keep Set type attribute for persist content becuase this update only 
				//include updating simple properties from web page(i.e. text value, list value, etc)
				
				//copy web page value into persist content, as above, the "Set" type value kept.
				PropertyUtils.copyProperties(persistContent,forum);
				forumService.editForum(persistContent,attachment,topics);
			}else{
				forum.setContentId(forumForm.getToolContentID());
				forumService.createForum(forum,attachment,topics);
			}
		} catch (Exception e) {
			log.error(e);
		}
		return mapping.findForward("success");
	}
	/**
	 * Handle upload online instruction files request. Once the file uploaded successfully, database
	 * will update accordingly. 
	 * 
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
	 * Handle upload offline instruction files request. Once the file uploaded successfully, database
	 * will update accordingly. 
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
		
		Forum content = getContent(form);
		forumService = getForumManager();
		Attachment att = forumService.uploadInstructionFile(content.getContentId(), file, type);
		//update session
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

	public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		return deleteFile(request, response,form, IToolContentHandler.TYPE_OFFLINE);
	}
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
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.TOOL_CONTENT_ID));
		Long versionID = new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuID"));
		
		forumService = getForumManager();
		forumService.deleteFromRepository(uuID,versionID);
		Attachment attachment = null;
		List attachmentList;
		if(StringUtils.equals(IToolContentHandler.TYPE_OFFLINE,type)){
			attachmentList = ((ForumForm)form).getOfflineFileList();
		}else{
			attachmentList = ((ForumForm)form).getOnlineFileList();
		}
		Iterator iter = attachmentList.iterator();
		while(iter.hasNext()){
			Attachment att = (Attachment) iter.next();
			if(versionID.equals(att.getFileVersionId()) && uuID.equals(att.getFileUuid())){
				iter.remove();
				attachment = att;
				break;
			}
		}
		if (attachment!= null && attachment.getUid() != null) {
			forumService.deleteInstructionFile(contentID,uuID,versionID,type);
		}
		
		StringBuffer sb = new StringBuffer();
		iter = attachmentList.iterator();
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

	/**
	 * The private method to get content from ActionForm parameters (web page).
	 * 
	 * @param form
	 * @return
	 */
	private Forum getContent(ActionForm form) {
		ForumForm authForm = (ForumForm) form;
		Forum forum = authForm.getForum();
		
		Forum content = new Forum();
		try {
			PropertyUtils.copyProperties(content,forum);
		} catch (Exception e) {
			log.error(e);
		}
		content.setContentId(authForm.getToolContentID());
		return content;
	}

  	private IForumService getForumManager() {
  	    if ( forumService == null ) {
  	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
  	      forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
  	    }
  	    return forumService;
  	}
}
