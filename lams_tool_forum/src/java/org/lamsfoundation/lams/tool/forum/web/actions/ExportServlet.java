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

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.service.ForumServiceProxy;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumToolContentHandler;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ExportServlet  extends AbstractExportPortfolioServlet {
	private static final long serialVersionUID = -4529093489007108143L;
	private static Logger logger = Logger.getLogger(ExportServlet.class);
	private final String FILENAME = "forum_main.html";
	ForumToolContentHandler handler;
	
	
	private class StringComparator implements Comparator<String>{
		public int compare(String o1, String o2) {
			if(o1 != null && o2 != null){
				int c = o1.compareTo(o2);
				//to ensure String does not overlap even they have duplicated name.
				return c==0?1:c;
			}else if(o1 != null)
				return 1;
			else
				return -1;
		}
	}
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
		
		if (StringUtils.equals(mode,ToolAccessMode.LEARNER.toString())){
			 request.getSession().setAttribute(AttributeNames.PARAM_MODE, ToolAccessMode.LEARNER);
			learner(request,response,directoryName,cookies);
		}else if (StringUtils.equals(mode,ToolAccessMode.TEACHER.toString())){
			request.getSession().setAttribute(AttributeNames.PARAM_MODE, ToolAccessMode.TEACHER);
			teacher(request,response,directoryName,cookies);
		}
		
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		writeResponseToFile(basePath+"/jsps/export/exportportfolio.jsp",directoryName,FILENAME,cookies);
		
		return FILENAME;
	}
    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
        
        IForumService forumService = ForumServiceProxy.getForumService(getServletContext());
        
        if (userID == null || toolSessionID == null)
        {
            String error = "Tool session Id or user Id is null. Unable to continue";
            logger.error(error);
            throw new ForumException(error);
        }
        
        ForumUser learner = forumService.getUserByUserAndSession(userID,toolSessionID);
        
        if (learner == null)
        {
            String error="The user with user id " + userID + " does not exist in this session or session may not exist.";
            logger.error(error);
            throw new ForumException(error);
        }
        
		// get root topic list and its children topics
        List<MessageDTO> msgDtoList = getSessionTopicList(toolSessionID, directoryName, forumService);
        setAuthorMark(msgDtoList);
        
        ForumToolSession session = forumService.getSessionBySessionId(toolSessionID);
        
        //put all message into Map. Key is session name, value is list of all topics in this session.
        Map sessionTopicMap = new TreeMap();
        sessionTopicMap.put(session.getSessionName(), msgDtoList);
        
		request.getSession().setAttribute(ForumConstants.ATTR_TOOL_CONTENT_TOPICS, sessionTopicMap);
		
		//set forum title 
		request.setAttribute(ForumConstants.FORUM_TITLE, session.getForum().getTitle());
		
    }

    
	public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
    {
    	IForumService forumService = ForumServiceProxy.getForumService(getServletContext());
       
        //check if toolContentId exists in db or not
        if (toolContentID==null)
        {
            String error="Tool Content Id is missing. Unable to continue";
            logger.error(error);
            throw new ForumException(error);
        }

        Forum content = forumService.getForumByContentId(toolContentID);
        
        if (content == null)
        {
            String error="Data is missing from the database. Unable to Continue";
            logger.error(error);
            throw new ForumException(error);
        }
		//return FileDetailsDTO list according to the given sessionID
        List sessionList = forumService.getSessionsByContentId(toolContentID);
        Iterator iter = sessionList.iterator();
        //put all message into Map. Key is session name, value is list of all topics in this session.
        Map<String,List<MessageDTO>> topicsByUser = new TreeMap<String,List<MessageDTO>>(this.new StringComparator());
        while(iter.hasNext()){
        	ForumToolSession session = (ForumToolSession) iter.next();
        	List<MessageDTO> sessionMsgDTO = getSessionTopicList(session.getSessionId(), directoryName, forumService);
    		topicsByUser.put(session.getSessionName(),sessionMsgDTO);	
        }
		request.getSession().setAttribute(ForumConstants.ATTR_TOOL_CONTENT_TOPICS,topicsByUser);
    }

	/**
	 * Get all topics with their children message for a special ToolSessionID.
	 * @param toolSessionID
	 * @param directoryName
	 * @param forumService
	 * @return
	 */
	private List<MessageDTO> getSessionTopicList(Long toolSessionID, String directoryName, IForumService forumService) {
		List<MessageDTO> rootTopics = forumService.getRootTopics(toolSessionID);
		List<MessageDTO> msgDtoList = new ArrayList<MessageDTO>();
		for(MessageDTO msgDto : rootTopics){
			List<MessageDTO> topics = forumService.getTopicThread(msgDto.getMessage().getUid());
			//check attachement, if it has, save it into local directory.
			for(MessageDTO topic:topics){
				if(topic.getHasAttachment()){
					Iterator iter = topic.getMessage().getAttachments().iterator();
					while(iter.hasNext()){
						Attachment att = (Attachment) iter.next();
						topic.setAttachmentName(att.getFileName());
						int idx= 1;
						String userName = topic.getAuthor();
						String localDir;
						while(true){
							localDir = FileUtil.getFullPath(directoryName,userName + "/" + idx);
							File local = new File(localDir);
							if(!local.exists()){
								local.mkdirs();
								break;
							}
							idx++;
						}
						topic.setAttachmentLocalUrl(userName + "/" + idx + "/" + att.getFileName());
						try {
							handler = getToolContentHandler();
							handler.saveFile(att.getFileUuid(), FileUtil.getFullPath(directoryName, topic.getAttachmentLocalUrl()));
						} catch (Exception e) {
							logger.error("Export forum topic attachment failed: " + e.toString());
						}
					}
				}
			}
			msgDtoList.addAll(topics);
		}
		return msgDtoList;
	}
    private ForumToolContentHandler getToolContentHandler() {
  	    if ( handler == null ) {
    	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    	      handler = (ForumToolContentHandler) wac.getBean("forumToolContentHandler");
    	    }
    	    return handler;
	}
    
	/**
	 * If this topic is created by current login user, then set Author mark
	 * true.
	 * 
	 * @param msgDtoList
	 */
	private void setAuthorMark(List msgDtoList) {
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

}
