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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.service.ForumServiceProxy;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

public class ExportServlet  extends AbstractExportPortfolioServlet {
	private static final long serialVersionUID = -4529093489007108143L;
	private static Logger logger = Logger.getLogger(ExportServlet.class);
	private final String FILENAME = "forum_main.html";
	
	
	public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
	{
		if (StringUtils.equals(mode,ToolAccessMode.LEARNER.toString())){
			learner(request,response,directoryName,cookies);
		}else if (StringUtils.equals(mode,ToolAccessMode.TEACHER.toString())){
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
        
       Forum content = forumService.getForumByContentId(toolSessionID);
        
        if (content == null)
        {
            String error="The content for this activity has not been defined yet.";
            logger.error(error);
            throw new ForumException(error);
        }
        List topicList = forumService.getAllTopicsFromSession(toolSessionID);
		Map topicsByUser = getTopicsSortedByAuthor(topicList);	
		request.getSession().setAttribute("report",topicsByUser);
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
        Map topicsByUser = new HashMap();
        while(iter.hasNext()){
        	ForumToolSession session = (ForumToolSession) iter.next();
            List topicList = forumService.getAllTopicsFromSession(session.getSessionId());
    		topicsByUser.putAll(getTopicsSortedByAuthor(topicList));	
        }
		request.getSession().setAttribute("report",topicsByUser);
    }
	/**
	 * @param topicList
	 * @return
	 */
	private Map getTopicsSortedByAuthor(List topicList) {
		Map topicsByUser = new HashMap();
		Iterator iter = topicList.iterator();
		while(iter.hasNext()){
			MessageDTO dto = (MessageDTO) iter.next();
			dto.getMessage().getReport();
			List list = (List) topicsByUser.get(dto.getMessage().getCreatedBy());
			if(list == null){
				list = new ArrayList();
				topicsByUser.put(dto.getMessage().getCreatedBy(),list);
			}
			list.add(dto);
		}
		return topicsByUser;
	}
}
