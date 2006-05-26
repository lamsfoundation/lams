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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */ 
package org.lamsfoundation.lams.tool.example.service;

import java.util.List;

import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.example.dao.IExampleAttachmentDAO;
import org.lamsfoundation.lams.tool.example.dao.IExampleDAO;
import org.lamsfoundation.lams.tool.example.dao.IExampleSessionDAO;
import org.lamsfoundation.lams.tool.example.dao.IExampleUserDAO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
* An implementation of the NoticeboardService interface.
* 
* As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
*/

public class ExampleService implements ToolSessionManager, ToolContentManager,
		IExampleService {

	private IExampleDAO exampleDAO=null;
	private IExampleSessionDAO exampleSessionDAO = null;
	private IExampleUserDAO exampleUserDAO=null;
	private IExampleAttachmentDAO exampleAttachmentDAO = null;
	
	private ILearnerService learnerService;
	private IAuditService auditService;
	private ILamsToolService toolService;
	private IToolContentHandler exampleToolContentHandler = null;

	public ExampleService() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* ************ Methods from ToolSessionManager, ToolContentManager ***********************/
	public void createToolSession(Long toolSessionId, String toolSessionName,
			Long toolContentId) throws ToolException {
		// TODO Auto-generated method stub

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
	
	/**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
 	public String exportToolContent(Long toolContentId) throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public String importToolContent(Long toolContentId, String reference, String directory) throws ToolException {
		// TODO Auto-generated method stub
		return null;
	}


	/* ******************* Used by Spring to "inject" the linked objects **************************/
	public IExampleAttachmentDAO getExampleAttachmentDAO() {
		return exampleAttachmentDAO;
	}

	public void setExampleAttachmentDAO(IExampleAttachmentDAO attachmentDAO) {
		this.exampleAttachmentDAO = attachmentDAO;
	}

	public IExampleDAO getExampleDAO() {
		return exampleDAO;
	}

	public void setExampleDAO(IExampleDAO exampleDAO) {
		this.exampleDAO = exampleDAO;
	}

	public IToolContentHandler getExampleToolContentHandler() {
		return exampleToolContentHandler;
	}

	public void setExampleToolContentHandler(IToolContentHandler exampleToolContentHandler) {
		this.exampleToolContentHandler = exampleToolContentHandler;
	}

	public IExampleSessionDAO getExampleSessionDAO() {
		return exampleSessionDAO;
	}

	public void setExampleSessionDAO(IExampleSessionDAO sessionDAO) {
		this.exampleSessionDAO = sessionDAO;
	}

	public ILamsToolService getToolService() {
		return toolService;
	}

	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

	public IExampleUserDAO getExampleUserDAO() {
		return exampleUserDAO;
	}

	public void setExampleUserDAO(IExampleUserDAO userDAO) {
		this.exampleUserDAO = userDAO;
	}

	public ILearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public IAuditService getAuditService() {
		return auditService;
	}

	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}
}
