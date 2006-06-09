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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.chat.web.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.chat.dto.ChatMessageDTO;
import org.lamsfoundation.lams.tool.chat.dto.ChatSessionDTO;
import org.lamsfoundation.lams.tool.chat.dto.MonitoringDTO;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.service.ChatServiceProxy;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.tool.chat.web.forms.MonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/monitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts.action-forward name="success" path="tiles:/monitoring/main"
 * @struts.action-forward name="chat_client"
 *                        path="tiles:/monitoring/chat_client"
 * @struts.action-forward name="chat_history"
 *                        path="tiles:/monitoring/chat_history"
 * 
 */
public class MonitoringAction extends LamsDispatchAction {

	private static Logger log = Logger.getLogger(MonitoringAction.class);

	public IChatService chatService;

	public ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("excuting monitoring action");

		Long toolContentID = new Long(WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID));

		// set up chatService
		if (chatService == null) {
			chatService = ChatServiceProxy.getChatService(this.getServlet()
					.getServletContext());
		}
		Chat chat = chatService.getChatByContentId(toolContentID);
		
		// constructing DTOs
		Set<ChatSessionDTO> chatSessionDTOs = new TreeSet<ChatSessionDTO>();
		for (Iterator iter = chat.getChatSessions().iterator(); iter.hasNext();) {
			ChatSession chatSession = (ChatSession) iter.next();
			List latestMessages = chatService.getLastestMessages(chatSession, ChatConstants.MONITORING_SUMMARY_MAX_MESSAGES);
			
			ChatSessionDTO chatSessionDTO = new ChatSessionDTO(chatSession, latestMessages);
			chatSessionDTOs.add(chatSessionDTO);
		}
		MonitoringDTO dto = new MonitoringDTO(chat);
		dto.setChatSessions(chatSessionDTOs);
		dto.setChatEditable(this.isChatEditable(chat));

		request.setAttribute("monitoringDTO", dto);
		return mapping.findForward("success");
	}

	public ActionForward openChatClient(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		// TODO this method is almost a copy of LearningAction.unspecified.
		Long toolSessionID = WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_SESSION_ID);

		// set up chatService
		if (chatService == null) {
			chatService = ChatServiceProxy.getChatService(this.getServlet()
					.getServletContext());
		}

		// Retreive the session
		ChatSession chatSession = chatService
				.getSessionBySessionId(toolSessionID);
		if (chatSession == null) {
			throw new ChatException(
					"Cannot retrieve session with toolSessionId"
							+ toolSessionID);
		}

		// Retrieve the current user
		ChatUser chatUser = getCurrentUser(toolSessionID);

		// Create the room if it doesnt exist
		if (chatSession.getJabberRoom() == null) {
			chatService.createJabberRoom(chatSession);
			chatService.saveOrUpdateChatSession(chatSession);
		}
		
		// set the teachers visibility
		
		request.setAttribute("XMPPDOMAIN", ChatConstants.XMPPDOMAIN);
		request.setAttribute("USERNAME", chatUser.getUserId());
		request.setAttribute("PASSWORD", chatUser.getUserId());
		request.setAttribute("CONFERENCEROOM", chatSession.getJabberRoom());
		request.setAttribute("NICK", chatUser.getJabberNickname());
		request.setAttribute("MODE", "teacher");
		
		request.setAttribute("chatTitle", chatSession.getChat().getTitle());
		request.setAttribute("chatInstructions", chatSession.getChat().getInstructions());

		return mapping.findForward("chat_client");
	}

	public ActionForward openChatHistory(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		MonitoringForm monitoringForm = (MonitoringForm) form;
		// TODO check for null from chatService. forward to appropriate page.
		ChatSession chatSession = chatService.getSessionBySessionId(monitoringForm.getToolSessionID());
		ChatSessionDTO sessionDTO = new ChatSessionDTO(chatSession);
		request.setAttribute("sessionDTO", sessionDTO);		
		return mapping.findForward("chat_history");
	}
	
	public ActionForward editMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		MonitoringForm monitoringForm = (MonitoringForm) form;
		ChatMessage chatMessage = chatService.getMessageByUID(monitoringForm.getMessageUID());
		
		boolean hasChanged = false;
		if (chatMessage.getHidden().booleanValue() != monitoringForm.isMessageHidden() ) {
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
		return openChatHistory(mapping, form, request, response);
	}
	/* Private Methods */

	private ChatUser getCurrentUser(Long toolSessionId) {
		UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(
				AttributeNames.USER);

		// attempt to retrieve user using userId and toolSessionId
		ChatUser chatUser = chatService.getUserByUserIdAndSessionId(new Long(
				user.getUserID().intValue()), toolSessionId);

		if (chatUser == null) {
			ChatSession chatSession = chatService
					.getSessionBySessionId(toolSessionId);
			chatUser = chatService.createChatUser(user, chatSession);
		}

		return chatUser;
	}

	// TODO need to double check this.
	private boolean isChatEditable(Chat chat) {
		if ((chat.getDefineLater() == true) && (chat.getContentInUse() == true)) {
			log
					.error("An exception has occurred: There is a bug in this tool, conflicting flags are set");
			return false;
		} else if ((chat.getDefineLater() == true)
				&& (chat.getContentInUse() == false))
			return true;
		else if ((chat.getDefineLater() == false)
				&& (chat.getContentInUse() == false))
			return true;
		else
			// (content.isContentInUse()==true && content.isDefineLater() ==
			// false)
			return false;
	}
}
