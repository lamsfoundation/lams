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

/* $Id$ */
package org.lamsfoundation.lams.tool.chat.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.chat.Chat;
import org.lamsfoundation.lams.tool.chat.ChatSession;
import org.lamsfoundation.lams.tool.chat.dao.IChatAttachmentDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatSessionDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatUserDAO;
import org.lamsfoundation.lams.tool.chat.util.ChatConstants;
import org.lamsfoundation.lams.tool.chat.util.ChatException;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;

/**
 * An implementation of the NoticeboardService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class ChatService implements ToolSessionManager, ToolContentManager,
		IChatService {

	private IChatDAO chatDAO = null;

	private IChatSessionDAO chatSessionDAO = null;

	private IChatUserDAO chatUserDAO = null;

	private IChatAttachmentDAO chatAttachmentDAO = null;

	private ILearnerService learnerService;

	private ILamsToolService toolService;

	private IToolContentHandler chatToolContentHandler = null;

	public ChatService() {
		super();
		// TODO Auto-generated constructor stub
	}

	static Logger log = Logger.getLogger(ChatService.class.getName());

	/* ************ Methods from ToolSessionManager, ToolContentManager ***** */
	public void createToolSession(Long toolSessionId, String toolSessionName,
			Long toolContentId) throws ToolException {
		ChatSession session = new ChatSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		Chat chat = chatDAO.getByContentId(toolContentId);
		session.setChat(chat);
		chatSessionDAO.saveOrUpdate(session);
	}

	public String leaveToolSession(Long toolSessionId, Long learnerId)
			throws DataMissingException, ToolException {

		if (toolSessionId == null) {
			log
					.error("Fail to leave tool Session based on null tool session id.");
			throw new ToolException(
					"Fail to remove tool Session based on null tool session id.");
		}
		if (learnerId == null) {
			log.error("Fail to leave tool Session based on null learner.");
			throw new ToolException(
					"Fail to remove tool Session based on null learner.");
		}

		ChatSession session = chatSessionDAO.getBySessionId(toolSessionId);
		if (session != null) {
			session.setStatus(ChatConstants.COMPLETED);
			chatSessionDAO.saveOrUpdate(session);
		} else {
			log.error("Fail to leave tool Session.Could not find submit file "
					+ "session by given session id: " + toolSessionId);
			throw new DataMissingException(
					"Fail to leave tool Session."
							+ "Could not find submit file session by given session id: "
							+ toolSessionId);
		}
		return learnerService.completeToolSession(toolSessionId, learnerId);
	}

	public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		return null;
	}

	public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeToolSession(Long toolSessionId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub

	}

	public void copyToolContent(Long fromContentId, Long toContentId)
			throws ToolException {
		if (fromContentId == null || toContentId == null)
			throw new ToolException(
					"Failed to create the SubmitFiles tool seession");

		Chat fromContent = chatDAO.getByContentId(fromContentId);
		if (fromContent == null) {
			fromContent = getDefaultContent(fromContentId);
		}
		Chat toContent = Chat.newInstance(fromContent, toContentId,
				chatToolContentHandler);
		chatDAO.saveOrUpdate(toContent);
	}

	public void setAsDefineLater(Long toolContentId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub

	}

	public void setAsRunOffline(Long toolContentId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub

	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData)
			throws SessionDataExistsException, ToolException {
		// TODO Auto-generated method stub

	}

	/* ********** My Methods ************************************************ */

	private Chat getDefaultContent(Long contentId) {
		// TODO This method signature should be added to IChatService

		if (contentId == null) {
			String error = "Could not retrieve default content id for Forum tool";
			log.error(error);
			throw new ChatException(error);
		}

		Chat defaultContent = getDefaultForum();
		// save default content by given ID.
		Chat content = new Chat();
		content = Chat.newInstance(defaultContent, contentId,
				chatToolContentHandler);
		return null;
	}

	private Chat getDefaultForum() {
		// TODO Auto-generated method stub
		return null;
	}

	/* ********** Used by Spring to "inject" the linked objects ************* */

	public IChatAttachmentDAO getChatAttachmentDAO() {
		return chatAttachmentDAO;
	}

	public void setChatAttachmentDAO(IChatAttachmentDAO attachmentDAO) {
		this.chatAttachmentDAO = attachmentDAO;
	}

	public IChatDAO getChatDAO() {
		return chatDAO;
	}

	public void setChatDAO(IChatDAO chatDAO) {
		this.chatDAO = chatDAO;
	}

	public IToolContentHandler getChatToolContentHandler() {
		return chatToolContentHandler;
	}

	public void setChatToolContentHandler(
			IToolContentHandler chatToolContentHandler) {
		this.chatToolContentHandler = chatToolContentHandler;
	}

	public IChatSessionDAO getChatSessionDAO() {
		return chatSessionDAO;
	}

	public void setChatSessionDAO(IChatSessionDAO sessionDAO) {
		this.chatSessionDAO = sessionDAO;
	}

	public ILamsToolService getToolService() {
		return toolService;
	}

	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

	public IChatUserDAO getChatUserDAO() {
		return chatUserDAO;
	}

	public void setChatUserDAO(IChatUserDAO userDAO) {
		this.chatUserDAO = userDAO;
	}

	public ILearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}

}
