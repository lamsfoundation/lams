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

import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.chat.dao.IChatAttachmentDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatSessionDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatUserDAO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;

/**
* An implementation of the NoticeboardService interface.
* 
* As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
*/

public class ChatService implements ToolSessionManager, ToolContentManager,
		IChatService {

	private IChatDAO chatDAO=null;
	private IChatSessionDAO chatSessionDAO = null;
	private IChatUserDAO chatUserDAO=null;
	private IChatAttachmentDAO chatAttachmentDAO = null;
	
	private ILearnerService learnerService;
	private ILamsToolService toolService;
	private IToolContentHandler chatToolContentHandler = null;

	public ChatService() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* ************ Methods from ToolSessionManager, ToolContentManager ***********************/
	public void createToolSession(Long toolSessionId, String toolSessionName,
			Long toolContentId) throws ToolException {
		
	}

	public String leaveToolSession(Long toolSessionId, Long learnerId)
			throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		// Do tool status stuff first e.g. set learner to complete within the tool
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
		// TODO Auto-generated method stub

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

	/* ******************* Used by Spring to "inject" the linked objects **************************/
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

	public void setChatToolContentHandler(IToolContentHandler chatToolContentHandler) {
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
