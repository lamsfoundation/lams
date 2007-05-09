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

package org.lamsfoundation.lams.tool.notebook.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookAttachmentDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookSessionDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookUserDAO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookAttachment;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.tool.notebook.util.NotebookToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the INotebookService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class NotebookService implements ToolSessionManager, ToolContentManager,
		INotebookService,  ToolContentImport102Manager {

	static Logger logger = Logger.getLogger(NotebookService.class.getName());

	private INotebookDAO notebookDAO = null;

	private INotebookSessionDAO notebookSessionDAO = null;

	private INotebookUserDAO notebookUserDAO = null;

	private INotebookAttachmentDAO notebookAttachmentDAO = null;

	private ILearnerService learnerService;

	private ILamsToolService toolService;

	private IToolContentHandler notebookToolContentHandler = null;

	private IRepositoryService repositoryService = null;

	private IAuditService auditService = null;

	private IExportToolContentService exportContentService;

	private ICoreNotebookService coreNotebookService;

	public NotebookService() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* ************ Methods from ToolSessionManager ************* */
	public void createToolSession(Long toolSessionId, String toolSessionName,
			Long toolContentId) throws ToolException {
		if (logger.isDebugEnabled()) {
			logger.debug("entering method createToolSession:"
					+ " toolSessionId = " + toolSessionId
					+ " toolSessionName = " + toolSessionName
					+ " toolContentId = " + toolContentId);
		}

		NotebookSession session = new NotebookSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		// learner starts
		// TODO need to also set other fields.
		Notebook notebook = notebookDAO.getByContentId(toolContentId);
		session.setNotebook(notebook);
		notebookSessionDAO.saveOrUpdate(session);
	}

	public String leaveToolSession(Long toolSessionId, Long learnerId)
			throws DataMissingException, ToolException {
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
		notebookSessionDAO.deleteBySessionID(toolSessionId);
		// TODO check if cascade worked
	}

	/* ************ Methods from ToolContentManager ************************* */

	public void copyToolContent(Long fromContentId, Long toContentId)
			throws ToolException {

		if (logger.isDebugEnabled()) {
			logger.debug("entering method copyToolContent:" + " fromContentId="
					+ fromContentId + " toContentId=" + toContentId);
		}

		if (toContentId == null) {
			String error = "Failed to copy tool content: toContentID is null";
			throw new ToolException(error);
		}

		Notebook fromContent = null;
		if ( fromContentId != null ) 		{
			fromContent = notebookDAO.getByContentId(fromContentId);
		}
		if (fromContent == null) {
			// create the fromContent using the default tool content
			fromContent = getDefaultContent();
		}
		Notebook toContent = Notebook.newInstance(fromContent, toContentId,
				notebookToolContentHandler);
		notebookDAO.saveOrUpdate(toContent);
	}

	public void setAsDefineLater(Long toolContentId, boolean value)
			throws DataMissingException, ToolException {
		Notebook notebook = notebookDAO.getByContentId(toolContentId);
		if (notebook == null) {
			throw new ToolException("Could not find tool with toolContentID: "
					+ toolContentId);
		}
		notebook.setDefineLater(value);
		notebookDAO.saveOrUpdate(notebook);
	}

	public void setAsRunOffline(Long toolContentId, boolean value)
			throws DataMissingException, ToolException {
		Notebook notebook = notebookDAO.getByContentId(toolContentId);
		if (notebook == null) {
			throw new ToolException("Could not find tool with toolContentID: "
					+ toolContentId);
		}
		notebook.setRunOffline(value);
		notebookDAO.saveOrUpdate(notebook);
	}

	public void removeToolContent(Long toolContentId, boolean removeSessionData)
			throws SessionDataExistsException, ToolException {
		// TODO Auto-generated method stub
	}

	/**
	 * Export the XML fragment for the tool's content, along with any files
	 * needed for the content.
	 * 
	 * @throws DataMissingException
	 *             if no tool content matches the toolSessionId
	 * @throws ToolException
	 *             if any other error occurs
	 */

	public void exportToolContent(Long toolContentId, String rootPath)
			throws DataMissingException, ToolException {
		Notebook notebook = notebookDAO.getByContentId(toolContentId);
		if (notebook == null) {
			notebook = getDefaultContent();
		}
		if (notebook == null)
 			throw new DataMissingException("Unable to find default content for the notebook tool");

		// set ResourceToolContentHandler as null to avoid copy file node in
		// repository again.
		notebook = Notebook.newInstance(notebook, toolContentId,
				null);
		notebook.setToolContentHandler(null);
		notebook.setNotebookSessions(null);
		Set<NotebookAttachment> atts = notebook.getNotebookAttachments();
		for (NotebookAttachment att : atts) {
			att.setNotebook(null);
		}
		try {
			exportContentService.registerFileClassForExport(
					NotebookAttachment.class.getName(), "fileUuid",
					"fileVersionId");
			exportContentService.exportToolContent(toolContentId,
					notebook, notebookToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		}
	}

	/**
	 * Import the XML fragment for the tool's content, along with any files
	 * needed for the content.
	 * 
	 * @throws ToolException
	 *             if any other error occurs
	 */
	public void importToolContent(Long toolContentId, Integer newUserUid,
			String toolContentPath,String fromVersion,String toVersion) throws ToolException {
		try {
			exportContentService.registerFileClassForImport(
					NotebookAttachment.class.getName(), "fileUuid",
					"fileVersionId", "fileName", "fileType", null, null);

			Object toolPOJO = exportContentService.importToolContent(
					toolContentPath, notebookToolContentHandler,fromVersion,toVersion);
			if (!(toolPOJO instanceof Notebook))
				throw new ImportToolContentException(
						"Import Notebook tool content failed. Deserialized object is "
								+ toolPOJO);
			Notebook notebook = (Notebook) toolPOJO;

			// reset it to new toolContentId
			notebook.setToolContentId(toolContentId);
			notebook.setCreateBy(new Long(newUserUid.longValue()));

			notebookDAO.saveOrUpdate(notebook);
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}

	/* ********** INotebookService Methods ********************************* */

	public Long createNotebookEntry(Long id, Integer idType, String signature,
			Integer userID, String entry) {
		return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
	}

	public NotebookEntry getEntry(Long uid) {
		return coreNotebookService.getEntry(uid);
	}

	public void updateEntry(Long uid, String entry) {
		coreNotebookService.updateEntry(uid, "", entry);
	}
	
	public Long getDefaultContentIdBySignature(String toolSignature) {
		Long toolContentId = null;
		toolContentId = new Long(toolService
				.getToolDefaultContentIdBySignature(toolSignature));
		if (toolContentId == null) {
			String error = "Could not retrieve default content id for this tool";
			logger.error(error);
			throw new NotebookException(error);
		}
		return toolContentId;
	}

	public Notebook getDefaultContent() {
		Long defaultContentID = getDefaultContentIdBySignature(NotebookConstants.TOOL_SIGNATURE);
		Notebook defaultContent = getNotebookByContentId(defaultContentID);
		if (defaultContent == null) {
			String error = "Could not retrieve default content record for this tool";
			logger.error(error);
			throw new NotebookException(error);
		}
		return defaultContent;
	}

	public Notebook copyDefaultContent(Long newContentID) {

		if (newContentID == null) {
			String error = "Cannot copy the Notebook tools default content: + "
					+ "newContentID is null";
			logger.error(error);
			throw new NotebookException(error);
		}

		Notebook defaultContent = getDefaultContent();
		// create new notebook using the newContentID
		Notebook newContent = new Notebook();
		newContent = Notebook.newInstance(defaultContent, newContentID,
				notebookToolContentHandler);
		notebookDAO.saveOrUpdate(newContent);
		return newContent;
	}

	public Notebook getNotebookByContentId(Long toolContentID) {
		Notebook notebook = (Notebook) notebookDAO
				.getByContentId(toolContentID);
		if (notebook == null) {
			logger.debug("Could not find the content with toolContentID:"
					+ toolContentID);
		}
		return notebook;
	}

	public NotebookSession getSessionBySessionId(Long toolSessionId) {
		NotebookSession notebookSession = notebookSessionDAO
				.getBySessionId(toolSessionId);
		if (notebookSession == null) {
			logger
					.debug("Could not find the notebook session with toolSessionID:"
							+ toolSessionId);
		}
		return notebookSession;
	}

	public NotebookUser getUserByUserIdAndSessionId(Long userId,
			Long toolSessionId) {
		return notebookUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
	}

	public NotebookUser getUserByLoginNameAndSessionId(String loginName,
			Long toolSessionId) {
		return notebookUserDAO.getByLoginNameAndSessionId(loginName,
				toolSessionId);
	}

	public NotebookUser getUserByUID(Long uid) {
		return notebookUserDAO.getByUID(uid);
	}

	public NotebookAttachment uploadFileToContent(Long toolContentId,
			FormFile file, String type) {
		if (file == null || StringUtils.isEmpty(file.getFileName()))
			throw new NotebookException("Could not find upload file: " + file);

		NodeKey nodeKey = processFile(file, type);

		NotebookAttachment attachment = new NotebookAttachment();
		attachment.setFileType(type);
		attachment.setFileUuid(nodeKey.getUuid());
		attachment.setFileVersionId(nodeKey.getVersion());
		attachment.setFileName(file.getFileName());

		return attachment;
	}

	public void deleteFromRepository(Long uuid, Long versionID)
			throws NotebookException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, uuid, versionID);
		} catch (Exception e) {
			throw new NotebookException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	public void deleteInstructionFile(Long contentID, Long uuid,
			Long versionID, String type) {
		notebookDAO.deleteInstructionFile(contentID, uuid, versionID, type);

	}

	public void saveOrUpdateNotebook(Notebook notebook) {
		notebookDAO.saveOrUpdate(notebook);
	}

	public void saveOrUpdateNotebookSession(NotebookSession notebookSession) {
		notebookSessionDAO.saveOrUpdate(notebookSession);
	}

	public void saveOrUpdateNotebookUser(NotebookUser notebookUser) {
		notebookUserDAO.saveOrUpdate(notebookUser);
	}

	public NotebookUser createNotebookUser(UserDTO user,
			NotebookSession notebookSession) {
		NotebookUser notebookUser = new NotebookUser(user, notebookSession);
		saveOrUpdateNotebookUser(notebookUser);
		return notebookUser;
	}

	public IAuditService getAuditService() {
		return auditService;
	}

	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}

	private NodeKey processFile(FormFile file, String type) {
		NodeKey node = null;
		if (file != null && !StringUtils.isEmpty(file.getFileName())) {
			String fileName = file.getFileName();
			try {
				node = getNotebookToolContentHandler().uploadFile(
						file.getInputStream(), fileName, file.getContentType(),
						type);
			} catch (InvalidParameterException e) {
				throw new NotebookException(
						"InvalidParameterException occured while trying to upload File"
								+ e.getMessage());
			} catch (FileNotFoundException e) {
				throw new NotebookException(
						"FileNotFoundException occured while trying to upload File"
								+ e.getMessage());
			} catch (RepositoryCheckedException e) {
				throw new NotebookException(
						"RepositoryCheckedException occured while trying to upload File"
								+ e.getMessage());
			} catch (IOException e) {
				throw new NotebookException(
						"IOException occured while trying to upload File"
								+ e.getMessage());
			}
		}
		return node;
	}

	/**
	 * This method verifies the credentials of the SubmitFiles Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SubmitFilesException
	 */
	private ITicket getRepositoryLoginTicket() throws NotebookException {
		repositoryService = RepositoryProxy.getRepositoryService();
		ICredentials credentials = new SimpleCredentials(
				NotebookToolContentHandler.repositoryUser,
				NotebookToolContentHandler.repositoryId);
		try {
			ITicket ticket = repositoryService.login(credentials,
					NotebookToolContentHandler.repositoryWorkspaceName);
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new NotebookException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new NotebookException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new NotebookException("Login failed." + e.getMessage());
		}
	}

	/* ===============Methods implemented from ToolContentImport102Manager =============== */
	

    /**
     * Import the data for a 1.0.2 Notebook
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues)
    {
    	Date now = new Date();
    	Notebook notebook = new Notebook();
    	notebook.setContentInUse(Boolean.FALSE);
    	notebook.setCreateBy(new Long(user.getUserID().longValue()));
    	notebook.setCreateDate(now);
    	notebook.setDefineLater(Boolean.FALSE);
    	notebook.setInstructions(WebUtil.convertNewlines((String)importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
    	notebook.setLockOnFinished(Boolean.TRUE);
    	notebook.setOfflineInstructions(null);
    	notebook.setOnlineInstructions(null);
    	notebook.setRunOffline(Boolean.FALSE);
    	notebook.setTitle((String)importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
    	notebook.setToolContentId(toolContentId);
    	notebook.setUpdateDate(now);
    	notebook.setAllowRichEditor(Boolean.FALSE);
    	// leave as empty, no need to set them to anything.
    	//setNotebookAttachments(Set notebookAttachments);
    	//setNotebookSessions(Set notebookSessions);
    	notebookDAO.saveOrUpdate(notebook);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {

    	logger.warn("Setting the reflective field on a notebook. This doesn't make sense as the notebook is for reflection and we don't reflect on reflection!");
    	Notebook notebook = getNotebookByContentId(toolContentId);
    	if ( notebook == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}
    	
    	notebook.setInstructions(description);
    }
    
    //=========================================================================================
	/* ********** Used by Spring to "inject" the linked objects ************* */

	public INotebookAttachmentDAO getNotebookAttachmentDAO() {
		return notebookAttachmentDAO;
	}

	public void setNotebookAttachmentDAO(INotebookAttachmentDAO attachmentDAO) {
		this.notebookAttachmentDAO = attachmentDAO;
	}

	public INotebookDAO getNotebookDAO() {
		return notebookDAO;
	}

	public void setNotebookDAO(INotebookDAO notebookDAO) {
		this.notebookDAO = notebookDAO;
	}

	public IToolContentHandler getNotebookToolContentHandler() {
		return notebookToolContentHandler;
	}

	public void setNotebookToolContentHandler(
			IToolContentHandler notebookToolContentHandler) {
		this.notebookToolContentHandler = notebookToolContentHandler;
	}

	public INotebookSessionDAO getNotebookSessionDAO() {
		return notebookSessionDAO;
	}

	public void setNotebookSessionDAO(INotebookSessionDAO sessionDAO) {
		this.notebookSessionDAO = sessionDAO;
	}

	public ILamsToolService getToolService() {
		return toolService;
	}

	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

	public INotebookUserDAO getNotebookUserDAO() {
		return notebookUserDAO;
	}

	public void setNotebookUserDAO(INotebookUserDAO userDAO) {
		this.notebookUserDAO = userDAO;
	}

	public ILearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public IExportToolContentService getExportContentService() {
		return exportContentService;
	}

	public void setExportContentService(
			IExportToolContentService exportContentService) {
		this.exportContentService = exportContentService;
	}

	public ICoreNotebookService getCoreNotebookService() {
		return coreNotebookService;
	}

	public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
		this.coreNotebookService = coreNotebookService;
	}
}
