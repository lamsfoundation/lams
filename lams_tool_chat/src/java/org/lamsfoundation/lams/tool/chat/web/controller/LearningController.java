/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.chat.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.chat.dto.ChatDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatUserDTO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.tool.chat.web.forms.LearningForm;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/learning")
public class LearningController {

    @Autowired
    private IChatService chatService;
    @Autowired
    @Qualifier("chatMessageService")
    private MessageService messageService;

    @RequestMapping("/learning")
    public String unspecified(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// Retrieve the session and content.
	ChatSession chatSession = chatService.getSessionBySessionId(toolSessionID);
	if (chatSession == null) {
	    throw new ChatException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Chat chat = chatSession.getChat();

	// Retrieve the current user
	ChatUser chatUser = getCurrentUser(toolSessionID);

	// check defineLater
	if (chat.isDefineLater()) {
	    request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	    return "pages/learning/defineLater";
	}

	request.setAttribute("MODE", mode.toString());

	ChatDTO chatDTO = new ChatDTO(chat);
	request.setAttribute("chatDTO", chatDTO);

	ChatUserDTO chatUserDTO = new ChatUserDTO(chatUser);
	if (chatUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = chatService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ChatConstants.TOOL_SIGNATURE, chatUser.getUserId().intValue());
	    if (notebookEntry != null) {
		chatUserDTO.notebookEntry = notebookEntry.getEntry();
	    }
	}
	request.setAttribute("chatUserDTO", chatUserDTO);

	// Ensure that the content is use flag is set
	if (!chat.isContentInUse()) {
	    chat.setContentInUse(true);
	    chatService.saveOrUpdateChat(chat);
	}

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, chatService.isLastActivity(toolSessionID));

	// Check if submission deadline is null
	Date submissionDeadline = chatDTO.getSubmissionDeadline();
	request.setAttribute("chatDTO", chatDTO);
	if (submissionDeadline != null) {

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    request.setAttribute("submissionDeadline", submissionDeadline);

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return "pages/learning/submissionDeadline";
	    }
	}

	return "pages/learning/learning";
    }

    @RequestMapping("/finishActivity")
    public void finishActivity(@ModelAttribute LearningForm learningForm, HttpServletResponse response) {
	Long userUid = learningForm.getChatUserUID();

	try {
	    String nextActivityUrl = chatService.finishToolSession(userUid);
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new ChatException(e);
	} catch (ToolException e) {
	    throw new ChatException(e);
	} catch (IOException e) {
	    throw new ChatException(e);
	}
    }

    @RequestMapping("/openNotebook")
    public String openNotebook(@ModelAttribute LearningForm learningForm, HttpServletRequest request,
	    HttpServletResponse response) {

	// set the finished flag
	ChatUser chatUser = chatService.getUserByUID(learningForm.getChatUserUID());
	ChatDTO chatDTO = new ChatDTO(chatUser.getChatSession().getChat());

	request.setAttribute("chatDTO", chatDTO);

	NotebookEntry notebookEntry = chatService.getEntry(chatUser.getChatSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ChatConstants.TOOL_SIGNATURE, chatUser.getUserId().intValue());

	if (notebookEntry != null) {
	    learningForm.setEntryText(notebookEntry.getEntry());
	}

	Long toolSessionId = chatUser.getChatSession().getSessionId();
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, chatService.isLastActivity(toolSessionId));

	return "pages/learning/notebook";
    }

    @RequestMapping("/submitReflection")
    public void submitReflection(@ModelAttribute LearningForm learningForm, HttpServletResponse response) {

	// save the reflection entry and call the notebook.
	ChatUser chatUser = chatService.getUserByUID(learningForm.getChatUserUID());
	Long toolSessionID = chatUser.getChatSession().getSessionId();
	Integer userID = chatUser.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = chatService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ChatConstants.TOOL_SIGNATURE, userID);

	if (entry == null) {
	    // create new entry
	    chatService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ChatConstants.TOOL_SIGNATURE, userID, learningForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(learningForm.getEntryText());
	    entry.setLastModified(new Date());
	    chatService.updateEntry(entry);
	}

	finishActivity(learningForm, response);
    }

    private ChatUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	ChatUser chatUser = chatService.getUserByUserIdAndSessionId(user.getUserID().longValue(), toolSessionId);

	if (chatUser == null) {
	    ChatSession chatSession = chatService.getSessionBySessionId(toolSessionId);
	    chatUser = chatService.createChatUser(user, chatSession);
	}

	return chatUser;
    }
}