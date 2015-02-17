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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.forum.web.rest;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ForumRestServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(ForumRestServlet.class);

    private static IAuthoringService authoringService;
    private static IUserManagementService userManagementService;
    private static IForumService forumService;
    private static Long toolID;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
	String requestBody = IOUtils.toString(request.getReader());
	JSONObject requestJSON = null;
	try {
	    requestJSON = new JSONObject(requestBody);
	    String title = requestJSON.getString("title");
	    String instructons = requestJSON.getString("instructions");
	    String topicSubject = requestJSON.getString("topicSubject");
	    String topicBody = requestJSON.getString("topicBody");
	    Date updateDate = new Date();
	    Long toolContentID = getAuthoringService().insertToolContentID(getToolID());
	    User user = (User) getUserManagementService().findById(User.class, 5);
	    UserDTO userDTO = user.getUserDTO();
	    ForumUser forumUser = new ForumUser(userDTO, null);

	    Forum forum = new Forum();
	    forum.setTitle(title);
	    forum.setInstructions(instructons);
	    forum.setContentId(toolContentID);
	    forum.setReflectOnActivity(false);
	    forum.setCreated(updateDate);
	    forum.setUpdated(updateDate);
	    forum.setCreatedBy(forumUser);

	    Message topic = new Message();
	    topic.setForum(forum);
	    topic.setSubject(topicSubject);
	    topic.setBody(topicBody);
	    topic.setCreated(updateDate);
	    topic.setUpdated(updateDate);
	    topic.setCreatedBy(forumUser);
	    topic.setIsAuthored(true);

	    getForumService().createUser(forumUser);
	    getForumService().updateForum(forum);
	    getForumService().createRootTopic(forum.getUid(), null, topic);

	    response.setContentType("application/json;charset=utf-8");
	    response.getWriter().print("{\"toolContentID\":" + toolContentID + "}");
	} catch (JSONException e) {
	    throw new IOException("Error while parsing JSON from request", e);
	}
    }

    private Long getToolID() {
	if (ForumRestServlet.toolID == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext());
	    IToolDAO toolDAO = (IToolDAO) ctx.getBean("toolDAO");
	    ForumRestServlet.toolID = toolDAO.getToolBySignature(ForumConstants.TOOL_SIGNATURE).getToolId();
	}
	return ForumRestServlet.toolID;
    }

    private IAuthoringService getAuthoringService() {
	if (ForumRestServlet.authoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext());
	    ForumRestServlet.authoringService = (IAuthoringService) ctx.getBean("authoringService");
	}
	return ForumRestServlet.authoringService;
    }

    private IUserManagementService getUserManagementService() {
	if (ForumRestServlet.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext());
	    ForumRestServlet.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return ForumRestServlet.userManagementService;
    }

    private IForumService getForumService() {
	if (ForumRestServlet.forumService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext());
	    ForumRestServlet.forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
	}
	return ForumRestServlet.forumService;
    }
}