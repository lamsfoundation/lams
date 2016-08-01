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


package org.lamsfoundation.lams.tool.chat.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.chat.dto.ChatDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatUserDTO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.service.ChatServiceProxy;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.tool.chat.web.forms.LearningForm;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 *
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);
    private static IChatService chatService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up chatService
	if (LearningAction.chatService == null) {
	    LearningAction.chatService = ChatServiceProxy.getChatService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	ChatSession chatSession = LearningAction.chatService.getSessionBySessionId(toolSessionID);
	if (chatSession == null) {
	    throw new ChatException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Chat chat = chatSession.getChat();

	// Retrieve the current user
	ChatUser chatUser = getCurrentUser(toolSessionID);

	// check defineLater
	if (chat.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	request.setAttribute("MODE", mode.toString());

	ChatDTO chatDTO = new ChatDTO(chat);
	request.setAttribute("chatDTO", chatDTO);

	ChatUserDTO chatUserDTO = new ChatUserDTO(chatUser);
	if (chatUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = LearningAction.chatService.getEntry(toolSessionID,
		    CoreNotebookConstants.NOTEBOOK_TOOL, ChatConstants.TOOL_SIGNATURE, chatUser.getUserId().intValue());
	    if (notebookEntry != null) {
		chatUserDTO.notebookEntry = notebookEntry.getEntry();
	    }
	}
	request.setAttribute("chatUserDTO", chatUserDTO);

	// Ensure that the content is use flag is set.
	if (!chat.isContentInUse()) {
	    chat.setContentInUse(new Boolean(true));
	    LearningAction.chatService.saveOrUpdateChat(chat);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	/* Check if submission deadline is null */

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
		return mapping.findForward("submissionDeadline");
	    }

	}

	return mapping.findForward("learning");
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	ChatUser chatUser = LearningAction.chatService.getUserByUID(lrnForm.getChatUserUID());
	if (chatUser != null) {
	    chatUser.setFinishedActivity(true);
	    LearningAction.chatService.saveOrUpdateChatUser(chatUser);
	} else {
	    LearningAction.log.error("finishActivity(): couldn't find ChatUser with uid: " + lrnForm.getChatUserUID());
	}

	ToolSessionManager sessionMgrService = ChatServiceProxy.getChatSessionManager(getServlet().getServletContext());

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());
	Long toolSessionID = chatUser.getChatSession().getSessionId();

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, userID);
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new ChatException(e);
	} catch (ToolException e) {
	    throw new ChatException(e);
	} catch (IOException e) {
	    throw new ChatException(e);
	}

	return null; // TODO need to return proper page.
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	ChatUser chatUser = LearningAction.chatService.getUserByUID(lrnForm.getChatUserUID());
	ChatDTO chatDTO = new ChatDTO(chatUser.getChatSession().getChat());

	request.setAttribute("chatDTO", chatDTO);

	NotebookEntry notebookEntry = LearningAction.chatService.getEntry(chatUser.getChatSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ChatConstants.TOOL_SIGNATURE, chatUser.getUserId().intValue());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, chatUser.getChatSession().getSessionId());
	LearningWebUtil.putActivityPositionInRequestByToolSessionId(chatUser.getChatSession().getSessionId(), request,
		getServlet().getServletContext());

	return mapping.findForward("notebook");
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	ChatUser chatUser = LearningAction.chatService.getUserByUID(lrnForm.getChatUserUID());
	Long toolSessionID = chatUser.getChatSession().getSessionId();
	Integer userID = chatUser.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = LearningAction.chatService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ChatConstants.TOOL_SIGNATURE, userID);

	if (entry == null) {
	    // create new entry
	    LearningAction.chatService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ChatConstants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    LearningAction.chatService.updateEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }

    private ChatUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	ChatUser chatUser = LearningAction.chatService
		.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()), toolSessionId);

	if (chatUser == null) {
	    ChatSession chatSession = LearningAction.chatService.getSessionBySessionId(toolSessionId);
	    chatUser = LearningAction.chatService.createChatUser(user, chatSession);
	}

	return chatUser;
    }
}