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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.chat.dao.hibernate.ChatUserDAO;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.service.ChatServiceProxy;
import org.lamsfoundation.lams.tool.chat.service.IChatService;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/learning"
 * @struts.action-forward name="success" path="tiles:/learning/main"
 */
public class LearningAction extends Action {

	private static Logger log = Logger.getLogger(LearningAction.class);

	private static final boolean MODE_OPTIONAL = false;

	private IChatService chatService;

public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 'toolSessionID' and 'mode' paramters are expected to be present.
		// TODO need to catch exceptions and handle errors.
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
				MODE_OPTIONAL);		
		Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		
		// set up chatService
		if (chatService == null) {
			chatService = ChatServiceProxy.getChatService(this.getServlet()
					.getServletContext());
		}
		
		// Retreive the session
		ChatSession chatSession = chatService.getSessionBySessionId(toolSessionID);
		if (chatSession == null) {
			throw new ChatException("Cannot retreive session with toolSessionId" + toolSessionID);
		}
		
		// Retrieve the current user
		ChatUser chatUser = getCurrentUser(toolSessionID);
		
		// Create the room if it doesnt exist
		if (chatSession.getJabberRoom() == null) {
			chatService.createJabberRoom(chatSession);
			chatService.saveOrUpdateChatSession(chatSession);
		}		
		
		request.setAttribute("XMPPDOMAIN", ChatConstants.XMPPDOMAIN);
		request.setAttribute("USERNAME", chatUser.getUserId());
		request.setAttribute("PASSWORD", chatUser.getUserId());
		request.setAttribute("CONFERENCEROOM", chatSession.getJabberRoom());
		request.setAttribute("NICK", chatUser.getLoginName());
		
		return mapping.findForward("success");
		
	}	private ChatUser getCurrentUser(Long toolSessionId) {
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
}
