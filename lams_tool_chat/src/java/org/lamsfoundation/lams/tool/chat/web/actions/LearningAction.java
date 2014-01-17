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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.chat.web.actions;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.chat.dto.ChatDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatUserDTO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
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
 * @struts.action path="/learning" parameter="dispatch" scope="request" name="learningForm"
 * @struts.action-forward name="learning" path="tiles:/learning/main"
 * @struts.action-forward name="submissionDeadline" path="tiles:/learning/submissionDeadline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 * @struts.action-forward name="notebook" path="tiles:/learning/notebook"
 */
public class LearningAction extends LamsDispatchAction {

    /**
     * Keeps information of users present in a Chat session. Needs to work with DB so presence is visible on clustered
     * environment.
     */
    private class Roster {
	private long lastCheckTime = 0;
	// users who currently poll messasages
	private final Map<Long, Date> activeUsers = new HashMap<Long, Date>();
	private final Set<String> roster = new HashSet<String>();

	private synchronized JSONArray getRosterJSON(ChatUser user) {
	    long currentTime = System.currentTimeMillis();
	    activeUsers.put(user.getUid(), new Date(currentTime));

	    if (currentTime - lastCheckTime > ChatConstants.PRESENCE_IDLE_TIMEOUT) {
		// store active users
		chatService.updateUserPresence(activeUsers);
		activeUsers.clear();

		// read active users from all nodes
		List<ChatUser> storedActiveUsers = chatService.getUsersActiveBySessionId(user.getChatSession()
			.getSessionId());
		roster.clear();
		for (ChatUser activeUser : storedActiveUsers) {
		    roster.add(activeUser.getNickname());
		}

		lastCheckTime = currentTime;
	    } else {
		roster.add(user.getNickname());
	    }

	    return new JSONArray(roster);
	}
    }

    private static Logger log = Logger.getLogger(LearningAction.class);

    private IChatService chatService;

    private static final Map<Long, Roster> rosters = Collections.synchronizedMap(new HashMap<Long, Roster>());

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up chatService
	if (chatService == null) {
	    chatService = ChatServiceProxy.getChatService(this.getServlet().getServletContext());
	}

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
	    return mapping.findForward("defineLater");
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

	// Ensure that the content is use flag is set.
	if (!chat.isContentInUse()) {
	    chat.setContentInUse(new Boolean(true));
	    chatService.saveOrUpdateChat(chat);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request, getServlet()
		.getServletContext());

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
	ChatUser chatUser = chatService.getUserByUID(lrnForm.getChatUserUID());
	if (chatUser != null) {
	    chatUser.setFinishedActivity(true);
	    chatService.saveOrUpdateChatUser(chatUser);
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
	ChatUser chatUser = chatService.getUserByUID(lrnForm.getChatUserUID());
	ChatDTO chatDTO = new ChatDTO(chatUser.getChatSession().getChat());

	request.setAttribute("chatDTO", chatDTO);

	NotebookEntry notebookEntry = chatService.getEntry(chatUser.getChatSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ChatConstants.TOOL_SIGNATURE, chatUser.getUserId().intValue());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(chatUser.getChatSession().getSessionId(), request,
		getServlet().getServletContext());

	return mapping.findForward("notebook");
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	ChatUser chatUser = chatService.getUserByUID(lrnForm.getChatUserUID());
	Long toolSessionID = chatUser.getChatSession().getSessionId();
	Integer userID = chatUser.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = chatService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		ChatConstants.TOOL_SIGNATURE, userID);

	if (entry == null) {
	    // create new entry
	    chatService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ChatConstants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    chatService.updateEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }

    /**
     * Get data displayed in Learner Chat screen.
     */
    public ActionForward getChatContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	ChatUser chatUser = getCurrentUser(toolSessionID);
	Long lastMessageUid = WebUtil.readLongParam(request, "lastMessageUid", true);

	// build JSON object for Javascript to understand
	JSONObject responseJSON = new JSONObject();
	getMessages(lastMessageUid, chatUser, responseJSON);
	getRoster(chatUser, responseJSON);

	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Stores message sent by user.
     */
    public ActionForward sendMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	String message = request.getParameter("message");
	if (StringUtils.isBlank(message)) {
	    return null;
	}
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	ChatUser toChatUser = null;
	String toUser = request.getParameter(AttributeNames.USER);
	if (!StringUtils.isBlank(toUser)) {
	    toChatUser = chatService.getUserByNicknameAndSessionID(toUser, toolSessionID);
	    if (toChatUser == null) {
		// there should be an user, but he could not be found, so don't send the message to everyone
		LearningAction.log.error("Could not find nick: " + toUser + " in session: " + toolSessionID);
		return null;
	    }
	}

	ChatUser chatUser = getCurrentUser(toolSessionID);

	ChatMessage chatMessage = new ChatMessage();
	chatMessage.setFromUser(chatUser);
	chatMessage.setChatSession(chatUser.getChatSession());
	chatMessage.setToUser(toChatUser);
	chatMessage.setType(toChatUser == null ? ChatMessage.MESSAGE_TYPE_PUBLIC : ChatMessage.MESSAGE_TYPE_PRIVATE);
	chatMessage.setBody(message);
	chatMessage.setSendDate(new Date());
	chatMessage.setHidden(Boolean.FALSE);
	chatService.saveOrUpdateChatMessage(chatMessage);

	return null;
    }

    private void getMessages(Long lastMessageUid, ChatUser chatUser, JSONObject responseJSON) throws JSONException {
	List<ChatMessage> messages = chatService.getMessagesForUser(chatUser);
	if (!messages.isEmpty()) {
	    // if last message which is already displayed on Chat screen
	    // is the same as newest message in DB, there is nothing new for user, so don't send him anything
	    ChatMessage lastMessage = messages.get(messages.size() - 1);
	    Long currentLastMessageUid = lastMessage.getUid();
	    if ((lastMessageUid == null) || (currentLastMessageUid > lastMessageUid)) {
		responseJSON.put("lastMessageUid", currentLastMessageUid);

		for (ChatMessage message : messages) {
		    // all messasges need to be written out, not only new ones,
		    // as old ones could have been edited or hidden by Monitor
		    if (!message.isHidden()) {
			String filteredMessage = chatService.filterMessage(message.getBody(), chatUser.getChatSession()
				.getChat());

			JSONObject messageJSON = new JSONObject();
			messageJSON.put("body", filteredMessage);
			messageJSON.put("from", message.getFromUser().getNickname());
			messageJSON.put("type", message.getType());
			responseJSON.append("messages", messageJSON);
		    }
		}
	    }
	}
    }

    /**
     * Gets users currently using the Chat instance.
     */
    private void getRoster(ChatUser chatUser, JSONObject responseJSON) throws JSONException {
	Long sessionId = chatUser.getChatSession().getSessionId();
	// this is equivalent of a chat room
	Roster sessionRoster = LearningAction.rosters.get(sessionId);
	if (sessionRoster == null) {
	    sessionRoster = new Roster();
	    LearningAction.rosters.put(sessionId, sessionRoster);
	}

	responseJSON.put("roster", sessionRoster.getRosterJSON(chatUser));
    }

    private ChatUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	ChatUser chatUser = chatService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (chatUser == null) {
	    ChatSession chatSession = chatService.getSessionBySessionId(toolSessionId);
	    chatUser = chatService.createChatUser(user, chatSession);
	}

	return chatUser;
    }
}