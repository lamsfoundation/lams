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



package org.lamsfoundation.lams.tool.chat.web.servlets;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.chat.dto.ChatDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatMessageDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatSessionDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatUserDTO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.service.ChatServiceProxy;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "chat_main.html";

    private IChatService chatService;

    @Override
    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (chatService == null) {
	    chatService = ChatServiceProxy.getChatService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
	    }
	} catch (ChatException e) {
	    logger.error("Cannot perform export for chat tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws ChatException {

	logger.debug("doExportTeacher: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new ChatException(error);
	}

	ChatSession chatSession = chatService.getSessionBySessionId(toolSessionID);

	// get all messages for current user and filter.
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// get the chat user
	ChatUser chatUser = chatService.getUserByUserIdAndSessionId(new Long(user.getUserID()), toolSessionID);

	// get messages for this user.
	List messageList = chatService.getMessagesForUser(chatUser);

	// construct session DTO.
	ChatSessionDTO sessionDTO = new ChatSessionDTO(chatSession, messageList);

	// if reflectOnActivity is enabled add userDTO.
	if (chatSession.getChat().isReflectOnActivity()) {
	    ChatUserDTO chatUserDTO = new ChatUserDTO(chatUser);

	    // get the entry.
	    NotebookEntry entry = chatService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ChatConstants.TOOL_SIGNATURE, chatUser.getUserId().intValue());

	    if (entry != null) {
		chatUserDTO.finishedReflection = true;
		chatUserDTO.notebookEntry = entry.getEntry();
	    } else {
		chatUserDTO.finishedReflection = false;
	    }
	    sessionDTO.getUserDTOs().add(chatUserDTO);
	}

	// filter messages
	for (ChatMessageDTO msg : sessionDTO.getMessageDTOs()) {
	    msg.setBody(chatService.filterMessage(msg.getBody(), chatSession.getChat()));
	}

	ChatDTO chatDTO = new ChatDTO(chatSession.getChat());
	chatDTO.getSessionDTOs().add(sessionDTO);

	request.getSession().setAttribute("chatDTO", chatDTO);
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws ChatException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new ChatException(error);
	}

	Chat chat = chatService.getChatByContentId(toolContentID);
	ChatDTO chatDTO = new ChatDTO(chat);
	for (Iterator iter = chat.getChatSessions().iterator(); iter.hasNext();) {
	    // NB session DTO will contain all messages in session unfiltered.

	    ChatSession session = (ChatSession) iter.next();
	    ChatSessionDTO sessionDTO = new ChatSessionDTO(session);

	    // if reflectOnActivity is enabled add all userDTO.
	    if (session.getChat().isReflectOnActivity()) {

		for (Iterator iterator = session.getChatUsers().iterator(); iterator.hasNext();) {
		    ChatUser user = (ChatUser) iterator.next();
		    ChatUserDTO userDTO = new ChatUserDTO(user);
		    // get the entry.
		    NotebookEntry entry = chatService.getEntry(session.getSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, ChatConstants.TOOL_SIGNATURE,
			    user.getUserId().intValue());
		    if (entry != null) {
			userDTO.finishedReflection = true;
			userDTO.notebookEntry = entry.getEntry();
		    } else {
			userDTO.finishedReflection = false;
		    }
		    sessionDTO.getUserDTOs().add(userDTO);
		}
	    }
	    chatDTO.getSessionDTOs().add(sessionDTO);
	}
	request.getSession().setAttribute("chatDTO", chatDTO);
    }
}
