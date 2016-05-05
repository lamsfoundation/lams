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

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.service.ForumServiceProxy;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumBundler;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.lamsfoundation.lams.tool.forum.util.ForumToolContentHandler;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ExportServlet extends AbstractExportPortfolioServlet {
    private static final long serialVersionUID = -4529093489007108143L;
    private static Logger logger = Logger.getLogger(ExportServlet.class);
    private final String FILENAME = "forum_main.html";
    ForumToolContentHandler handler;

    private static IForumService forumService;

    private class StringComparator implements Comparator<String> {
	@Override
	public int compare(String o1, String o2) {
	    if (o1 != null && o2 != null) {
		return o1.compareTo(o2);
	    } else if (o1 != null) {
		return 1;
	    } else {
		return -1;
	    }
	}
    }

    @Override
    public void init() throws ServletException {
	forumService = ForumServiceProxy.getForumService(getServletContext());
	super.init();
    }

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	// initial sessionMap
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
	    sessionMap.put(AttributeNames.PARAM_MODE, ToolAccessMode.LEARNER);
	    learner(request, response, directoryName, cookies, sessionMap);
	} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
	    sessionMap.put(AttributeNames.PARAM_MODE, ToolAccessMode.TEACHER);
	    teacher(request, response, directoryName, cookies, sessionMap);
	}

	// Attempting to export required images
	try {
	    ForumBundler forumBundler = new ForumBundler();
	    forumBundler.bundle(request, cookies, directoryName);

	} catch (Exception e) {
	    logger.error("Could not export Q&A javascript files, some files may be missing in export portfolio", e);
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/jsps/export/exportportfolio.jsp?sessionMapID=" + sessionMap.getSessionID(),
		directoryName, FILENAME, cookies);

	return FILENAME;
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) {
	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new ForumException(error);
	}

	ForumUser forumUser = forumService.getUserByUserAndSession(userID, toolSessionID);
	ForumToolSession session = forumUser.getSession();
	Forum content = session.getForum();

	if (forumUser == null) {
	    String error = "The user with user id " + userID
		    + " does not exist in this session or session may not exist.";
	    logger.error(error);
	    throw new ForumException(error);
	}

	// Get Messages
	// Get root topic list and its children topics
	List<MessageDTO> msgDtoList = getSessionTopicList(toolSessionID, directoryName);
	// Set author flag, to decide if display mark of topics.Only author allow see his own mark. 
	setAuthorMark(msgDtoList);

	Set<org.lamsfoundation.lams.tool.forum.dto.UserDTO> userDTOSet = null;
	if (content.isReflectOnActivity()) {
	    // Get user reflection entries
	    userDTOSet = new TreeSet<org.lamsfoundation.lams.tool.forum.dto.UserDTO>(
		    new LastNameAlphabeticComparator());
	    userDTOSet.add(getReflectionEntry(forumUser));
	}

	// Store both in an object array
	Object[] pair = { msgDtoList, userDTOSet };

	// Add array to Map
	// put all message into Map. Key is session name, value is list of all topics in this session.
	Map<String, Object[]> sessionTopicMap = new TreeMap<String, Object[]>();
	sessionTopicMap.put(session.getSessionName(), pair);

	sessionMap.put(ForumConstants.ATTR_TOOL_CONTENT_TOPICS, sessionTopicMap);
	sessionMap.put(ForumConstants.ATTR_ALLOW_RATE_MESSAGES, content.isAllowRateMessages());

	// Set forum title 
	sessionMap.put(ForumConstants.ATTR_FORUM_TITLE, content.getTitle());

    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies, HashMap sessionMap) {
	//check if toolContentId exists in db or not
	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new ForumException(error);
	}

	Forum content = forumService.getForumByContentId(toolContentID);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new ForumException(error);
	}
	// Return FileDetailsDTO list according to the given sessionID
	List<ForumToolSession> sessionList = forumService.getSessionsByContentId(toolContentID);

	// put all message into Map. Key is session name, value is list of all topics in this session.
	Map<String, Object[]> topicsByUser = new TreeMap<String, Object[]>(this.new StringComparator());
	for (ForumToolSession session : sessionList) {
	    // Get Messages
	    List<MessageDTO> sessionMsgDTO = getSessionTopicList(session.getSessionId(), directoryName);

	    Set<org.lamsfoundation.lams.tool.forum.dto.UserDTO> userDTOSet = null;
	    if (content.isReflectOnActivity()) {
		// Get user reflection entries
		List<ForumUser> forumUserList = forumService.getUsersBySessionId(session.getSessionId());
		userDTOSet = new TreeSet<org.lamsfoundation.lams.tool.forum.dto.UserDTO>(
			new LastNameAlphabeticComparator());
		for (ForumUser forumUser : forumUserList) {
		    userDTOSet.add(getReflectionEntry(forumUser));
		}
	    }

	    // Store both in an object array
	    Object[] pair = { sessionMsgDTO, userDTOSet };

	    // Add array to map
	    topicsByUser.put(session.getSessionName(), pair);
	}
	sessionMap.put(ForumConstants.ATTR_TOOL_CONTENT_TOPICS, topicsByUser);
	sessionMap.put(ForumConstants.ATTR_ALLOW_RATE_MESSAGES, content.isAllowRateMessages());

	// Set forum title 
	sessionMap.put(ForumConstants.ATTR_FORUM_TITLE, content.getTitle());

    }

    /**
     * Get all topics with their children message for a special ToolSessionID.
     * 
     * @param toolSessionID
     * @param directoryName
     * @param forumService
     * @return
     */
    private List<MessageDTO> getSessionTopicList(Long toolSessionID, String directoryName) {
	List<MessageDTO> rootTopics = forumService.getRootTopics(toolSessionID);
	List<MessageDTO> msgDtoList = new ArrayList<MessageDTO>();
	for (MessageDTO msgDto : rootTopics) {
	    List<MessageDTO> topics = forumService.getTopicThread(msgDto.getMessage().getUid());
	    //check attachement, if it has, save it into local directory.
	    for (MessageDTO topic : topics) {
		if (topic.getHasAttachment()) {
		    Iterator iter = topic.getMessage().getAttachments().iterator();
		    while (iter.hasNext()) {
			Attachment att = (Attachment) iter.next();
			topic.setAttachmentName(att.getFileName());
			int idx = 1;
			String userName = topic.getAuthor();
			String localDir;
			while (true) {
			    localDir = FileUtil.getFullPath(directoryName, userName + "/" + idx);
			    File local = new File(localDir);
			    if (!local.exists()) {
				local.mkdirs();
				break;
			    }
			    idx++;
			}
			topic.setAttachmentLocalUrl(userName + "/" + idx + "/" + att.getFileUuid() + '.'
				+ FileUtil.getFileExtension(att.getFileName()));
			try {
			    handler = getToolContentHandler();
			    handler.saveFile(att.getFileUuid(),
				    FileUtil.getFullPath(directoryName, topic.getAttachmentLocalUrl()));
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
	if (handler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(this.getServletContext());
	    handler = (ForumToolContentHandler) wac.getBean(ForumConstants.TOOL_CONTENT_HANDLER_NAME);
	}
	return handler;
    }

    /**
     * If this topic is created by current login user, then set Author mark
     * true.
     * 
     * @param msgDtoList
     */
    private void setAuthorMark(List<MessageDTO> msgDtoList) {
	// set current user to web page, so that can display "edit" button
	// correct. Only author allowed to edit.
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long currUserId = new Long(user.getUserID().intValue());
	Iterator<MessageDTO> iter = msgDtoList.iterator();
	while (iter.hasNext()) {
	    MessageDTO dto = iter.next();
	    if (dto.getMessage().getCreatedBy() != null
		    && currUserId.equals(dto.getMessage().getCreatedBy().getUserId())) {
		dto.setAuthor(true);
	    } else {
		dto.setAuthor(false);
	    }
	}
    }

    /**
     * Retrieves the reflection entry for the forumUser and stores it in a UserDTO
     * 
     * @param forumUser
     * @param userDTOList
     */
    private org.lamsfoundation.lams.tool.forum.dto.UserDTO getReflectionEntry(ForumUser forumUser) {
	org.lamsfoundation.lams.tool.forum.dto.UserDTO userDTO = new org.lamsfoundation.lams.tool.forum.dto.UserDTO();
	userDTO.setFullName(forumUser.getFirstName() + " " + forumUser.getLastName());
	NotebookEntry notebookEntry = forumService.getEntry(forumUser.getSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ForumConstants.TOOL_SIGNATURE, forumUser.getUserId().intValue());

	// check notebookEntry is not null
	if (notebookEntry != null) {
	    userDTO.setReflect(notebookEntry.getEntry());
	    logger.debug("Could not find notebookEntry for ForumUser: " + forumUser.getUid());
	}
	return userDTO;
    }
}
