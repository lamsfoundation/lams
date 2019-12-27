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

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.chat.dto.ChatDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatSessionDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatUserDTO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.web.forms.MonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    private static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    private IChatService chatService;

    @Autowired
    @Qualifier("chatMessageService")
    private MessageService messageService;

    @RequestMapping("/monitoring")
    public String unspecified(HttpServletRequest request) {

	MonitoringController.log.info("excuting monitoring action");

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Chat chat = chatService.getChatByContentId(toolContentID);
	ChatDTO chatDTO = new ChatDTO(chat);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	chatDTO.setCurrentTab(currentTab);

	/* Check if submission deadline is null */

	Date submissionDeadline = chatDTO.getSubmissionDeadline();

	if (submissionDeadline != null) {

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    request.setAttribute("submissionDeadline", tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute("submissionDateString",
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));

	}

	Map<Long, Integer> sessCountMap = chatService.getMessageCountBySession(chat.getUid());

	for (Iterator sessIter = chat.getChatSessions().iterator(); sessIter.hasNext();) {
	    ChatSession session = (ChatSession) sessIter.next();

	    List latestMessages = chatService.getLastestMessages(session, ChatConstants.MONITORING_SUMMARY_MAX_MESSAGES,
		    false);
	    ChatSessionDTO sessionDTO = new ChatSessionDTO(session, latestMessages);

	    Integer count = sessCountMap.get(session.getUid());
	    if (count == null) {
		count = 0;
	    }
	    sessionDTO.setNumberOfPosts(count);

	    // constructing userDTOs
	    Map<Long, Integer> userCountMap = chatService.getMessageCountByFromUser(session.getUid());

	    sessionDTO.setNumberOfLearners(userCountMap.size());

	    for (Iterator userIter = session.getChatUsers().iterator(); userIter.hasNext();) {
		ChatUser user = (ChatUser) userIter.next();
		ChatUserDTO userDTO = new ChatUserDTO(user);
		count = userCountMap.get(user.getUid());
		if (count == null) {
		    count = 0;
		}
		userDTO.setPostCount(count);

		// get the notebook entry.
		NotebookEntry notebookEntry = chatService.getEntry(session.getSessionId(),
			CoreNotebookConstants.NOTEBOOK_TOOL, ChatConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		if (notebookEntry != null) {
		    userDTO.finishedReflection = true;
		} else {
		    userDTO.finishedReflection = false;
		}

		sessionDTO.getUserDTOs().add(userDTO);
	    }

	    chatDTO.getSessionDTOs().add(sessionDTO);
	}

	boolean isGroupedActivity = chatService.isGroupedActivity(toolContentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	request.setAttribute("monitoringDTO", chatDTO);
	request.setAttribute("contentFolderID", contentFolderID);

	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/openChatHistory")
    public String openChatHistory(@ModelAttribute MonitoringForm monitoringForm, HttpServletRequest request,
	    HttpServletResponse response) {

	// TODO check for null from chatService. forward to appropriate page.
	ChatSession chatSession = chatService.getSessionBySessionId(monitoringForm.getToolSessionID());
	ChatSessionDTO sessionDTO = new ChatSessionDTO(chatSession);
	request.setAttribute("sessionDTO", sessionDTO);
	return "pages/monitoring/chatHistory";
    }

    @RequestMapping("/openNotebook")
    public String openNotebook(HttpServletRequest request) {

	Long uid = WebUtil.readLongParam(request, "uid", false);

	ChatUser chatUser = chatService.getUserByUID(uid);
	NotebookEntry notebookEntry = chatService.getEntry(chatUser.getChatSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ChatConstants.TOOL_SIGNATURE, chatUser.getUserId().intValue());

	ChatUserDTO chatUserDTO = new ChatUserDTO(chatUser);
	chatUserDTO.setNotebookEntry(notebookEntry.getEntry());

	request.setAttribute("chatUserDTO", chatUserDTO);

	return "pages/monitoring/notebook";
    }

    @RequestMapping("/editMessage")
    public String editMessage(@ModelAttribute MonitoringForm monitoringForm, HttpServletRequest request,
	    HttpServletResponse response) {
	ChatMessage chatMessage = chatService.getMessageByUID(monitoringForm.getMessageUID());

	boolean hasChanged = false;
	if (chatMessage.isHidden() != monitoringForm.isMessageHidden()) {
	    hasChanged = true;
	    chatService.auditHideShowMessage(chatMessage, monitoringForm.isMessageHidden());
	}

	if (!chatMessage.getBody().equals(monitoringForm.getMessageBody())) {
	    hasChanged = true;
	    chatService.auditEditMessage(chatMessage, monitoringForm.getMessageBody());
	}

	if (hasChanged) {
	    chatMessage.setBody(monitoringForm.getMessageBody());
	    chatMessage.setHidden(monitoringForm.isMessageHidden());
	    chatService.saveOrUpdateChatMessage(chatMessage);
	}
	return openChatHistory(monitoringForm, request, response);
    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Chat chat = chatService.getChatByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, ChatConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	chat.setSubmissionDeadline(tzSubmissionDeadline);
	chatService.saveOrUpdateChat(chat);

	return formattedDate;
    }
}
