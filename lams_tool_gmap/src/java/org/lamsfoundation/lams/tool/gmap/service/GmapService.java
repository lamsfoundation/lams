/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.tool.gmap.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapAttachmentDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapSessionDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapUserDAO;
import org.lamsfoundation.lams.tool.gmap.dao.IGmapMarkerDAO;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapAttachment;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.util.GmapConstants;
import org.lamsfoundation.lams.tool.gmap.util.GmapException;
import org.lamsfoundation.lams.tool.gmap.util.GmapToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IGmapService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement
 * ToolContentManager and ToolSessionManager.
 */

public class GmapService implements ToolSessionManager, ToolContentManager,
		IGmapService,  ToolContentImport102Manager {

	static Logger logger = Logger.getLogger(GmapService.class.getName());

	private IGmapDAO gmapDAO = null;
	
	private IGmapMarkerDAO gmapMarkerDAO = null;

	private IGmapSessionDAO gmapSessionDAO = null;

	private IGmapUserDAO gmapUserDAO = null;

	private IGmapAttachmentDAO gmapAttachmentDAO = null;

	private ILearnerService learnerService;

	private ILamsToolService toolService;

	private IToolContentHandler gmapToolContentHandler = null;

	private IRepositoryService repositoryService = null;

	private IAuditService auditService = null;

	private IExportToolContentService exportContentService;

	private ICoreNotebookService coreNotebookService;

	public GmapService() {
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

		GmapSession session = new GmapSession();
		session.setSessionId(toolSessionId);
		session.setSessionName(toolSessionName);
		// learner starts
		// TODO need to also set other fields.
		Gmap gmap = gmapDAO.getByContentId(toolContentId);
		session.setGmap(gmap);
		gmapSessionDAO.saveOrUpdate(session);
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
		gmapSessionDAO.deleteBySessionID(toolSessionId);
		// TODO check if cascade worked
	}

	/** 
	 * Get the tool output for the given tool output names.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long, java.lang.Long)
	 */
	public SortedMap<String, ToolOutput> getToolOutput(List<String> names,
			Long toolSessionId, Long learnerId) {
		return new TreeMap<String,ToolOutput>();
	}

	/** 
	 * Get the tool output for the given tool output name.
	 * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long, java.lang.Long)
	 */
	public ToolOutput getToolOutput(String name, Long toolSessionId,
			Long learnerId) {
		return null;
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

		Gmap fromContent = null;
		if ( fromContentId != null ) 		{
			fromContent = gmapDAO.getByContentId(fromContentId);
		}
		if (fromContent == null) {
			// create the fromContent using the default tool content
			fromContent = getDefaultContent();
		}
		Gmap toContent = Gmap.newInstance(fromContent, toContentId,
				gmapToolContentHandler);
		gmapDAO.saveOrUpdate(toContent);
	}

	public void setAsDefineLater(Long toolContentId, boolean value)
			throws DataMissingException, ToolException {
		Gmap gmap = gmapDAO.getByContentId(toolContentId);
		if (gmap == null) {
			throw new ToolException("Could not find tool with toolContentID: "
					+ toolContentId);
		}
		gmap.setDefineLater(value);
		gmapDAO.saveOrUpdate(gmap);
	}

	public void setAsRunOffline(Long toolContentId, boolean value)
			throws DataMissingException, ToolException {
		Gmap gmap = gmapDAO.getByContentId(toolContentId);
		if (gmap == null) {
			throw new ToolException("Could not find tool with toolContentID: "
					+ toolContentId);
		}
		gmap.setRunOffline(value);
		gmapDAO.saveOrUpdate(gmap);
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
		Gmap gmap = gmapDAO.getByContentId(toolContentId);
		if (gmap == null) {
			gmap = getDefaultContent();
		}
		if (gmap == null)
 			throw new DataMissingException("Unable to find default content for the gmap tool");

		// set ResourceToolContentHandler as null to avoid copy file node in
		// repository again.
		gmap = Gmap.newInstance(gmap, toolContentId,
				null);
		gmap.setToolContentHandler(null);
		gmap.setGmapSessions(null);
		gmap.setCreateBy(null);
		
		Set<GmapAttachment> atts = gmap.getGmapAttachments();
		for (GmapAttachment att : atts) {
			att.setGmap(null);
		}
		
		Set<GmapMarker> markers = gmap.getGmapMarkers();
		Set<GmapMarker> authorItems = new HashSet<GmapMarker>();
		
		for (GmapMarker gmapMarker:markers)
		{
			if (gmapMarker.isAuthored())
			{
				gmapMarker.setCreatedBy(null);
				gmapMarker.setGmap(null);
				gmapMarker.setUpdatedBy(null);
				authorItems.add(gmapMarker);
			}
		}
		gmap.setGmapMarkers(authorItems);

		try {
			exportContentService.registerFileClassForExport(
					GmapAttachment.class.getName(), "fileUuid",
					"fileVersionId");
			exportContentService.exportToolContent(toolContentId,
					gmap, gmapToolContentHandler, rootPath);
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
					GmapAttachment.class.getName(), "fileUuid",
					"fileVersionId", "fileName", "fileType", null, null);

			Object toolPOJO = exportContentService.importToolContent(
					toolContentPath, gmapToolContentHandler,fromVersion,toVersion);
			if (!(toolPOJO instanceof Gmap))
				throw new ImportToolContentException(
						"Import Gmap tool content failed. Deserialized object is "
								+ toolPOJO);
			Gmap gmap = (Gmap) toolPOJO;

			// reset it to new toolContentId
			gmap.setToolContentId(toolContentId);
			gmap.setCreateBy(new Long(newUserUid.longValue()));

			gmapDAO.saveOrUpdate(gmap);
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}

	/** Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions that are always
	 * available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created for a particular activity
	 * such as the answer to the third question contains the word Koala and hence the need for the toolContentId
	 * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
	 */
	public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
		return new TreeMap<String, ToolOutputDefinition>();
	}

	/* ********** IGmapService Methods ********************************* */

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
			throw new GmapException(error);
		}
		return toolContentId;
	}

	public Gmap getDefaultContent() {
		Long defaultContentID = getDefaultContentIdBySignature(GmapConstants.TOOL_SIGNATURE);
		Gmap defaultContent = getGmapByContentId(defaultContentID);
		if (defaultContent == null) {
			String error = "Could not retrieve default content record for this tool";
			logger.error(error);
			throw new GmapException(error);
		}
		return defaultContent;
	}

	public Gmap copyDefaultContent(Long newContentID) {

		if (newContentID == null) {
			String error = "Cannot copy the Gmap tools default content: + "
					+ "newContentID is null";
			logger.error(error);
			throw new GmapException(error);
		}

		Gmap defaultContent = getDefaultContent();
		// create new gmap using the newContentID
		Gmap newContent = new Gmap();
		newContent = Gmap.newInstance(defaultContent, newContentID,
				gmapToolContentHandler);
		gmapDAO.saveOrUpdate(newContent);
		return newContent;
	}

	public Gmap getGmapByContentId(Long toolContentID) {
		Gmap gmap = (Gmap) gmapDAO
				.getByContentId(toolContentID);
		if (gmap == null) {
			logger.debug("Could not find the content with toolContentID:"
					+ toolContentID);
		}
		return gmap;
	}

	public GmapSession getSessionBySessionId(Long toolSessionId) {
		GmapSession gmapSession = gmapSessionDAO
				.getBySessionId(toolSessionId);
		if (gmapSession == null) {
			logger
					.debug("Could not find the gmap session with toolSessionID:"
							+ toolSessionId);
		}
		return gmapSession;
	}

	public GmapUser getUserByUserIdAndSessionId(Long userId,
			Long toolSessionId) {
		return gmapUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
	}

	public GmapUser getUserByLoginNameAndSessionId(String loginName,
			Long toolSessionId) {
		return gmapUserDAO.getByLoginNameAndSessionId(loginName,
				toolSessionId);
	}

	public GmapUser getUserByUID(Long uid) {
		return gmapUserDAO.getByUID(uid);
	}

	public GmapAttachment uploadFileToContent(Long toolContentId,
			FormFile file, String type) {
		if (file == null || StringUtils.isEmpty(file.getFileName()))
			throw new GmapException("Could not find upload file: " + file);

		NodeKey nodeKey = processFile(file, type);

		GmapAttachment attachment = new GmapAttachment();
		attachment.setFileType(type);
		attachment.setFileUuid(nodeKey.getUuid());
		attachment.setFileVersionId(nodeKey.getVersion());
		attachment.setFileName(file.getFileName());

		return attachment;
	}

	public void deleteFromRepository(Long uuid, Long versionID)
			throws GmapException {
		ITicket ticket = getRepositoryLoginTicket();
		try {
			repositoryService.deleteVersion(ticket, uuid, versionID);
		} catch (Exception e) {
			throw new GmapException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}

	public void deleteInstructionFile(Long contentID, Long uuid,
			Long versionID, String type) {
		gmapDAO.deleteInstructionFile(contentID, uuid, versionID, type);

	}

	public void saveOrUpdateGmap(Gmap gmap) {
		gmapDAO.saveOrUpdate(gmap);
	}
	
	public void saveOrUpdateGmapMarker(GmapMarker gmapMarker) {
		gmapMarkerDAO.saveOrUpdate(gmapMarker);
	}
	
	public void saveOrUpdateGmapSession(GmapSession gmapSession) {
		gmapSessionDAO.saveOrUpdate(gmapSession);
	}

	public void saveOrUpdateGmapUser(GmapUser gmapUser) {
		gmapUserDAO.saveOrUpdate(gmapUser);
	}

	public GmapUser createGmapUser(UserDTO user,
			GmapSession gmapSession) {
		GmapUser gmapUser = new GmapUser(user, gmapSession);
		saveOrUpdateGmapUser(gmapUser);
		return gmapUser;
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
				node = getGmapToolContentHandler().uploadFile(
						file.getInputStream(), fileName, file.getContentType(),
						type);
			} catch (InvalidParameterException e) {
				throw new GmapException(
						"InvalidParameterException occured while trying to upload File"
								+ e.getMessage());
			} catch (FileNotFoundException e) {
				throw new GmapException(
						"FileNotFoundException occured while trying to upload File"
								+ e.getMessage());
			} catch (RepositoryCheckedException e) {
				throw new GmapException(
						"RepositoryCheckedException occured while trying to upload File"
								+ e.getMessage());
			} catch (IOException e) {
				throw new GmapException(
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
	private ITicket getRepositoryLoginTicket() throws GmapException {
		repositoryService = RepositoryProxy.getRepositoryService();
		ICredentials credentials = new SimpleCredentials(
				GmapToolContentHandler.repositoryUser,
				GmapToolContentHandler.repositoryId);
		try {
			ITicket ticket = repositoryService.login(credentials,
					GmapToolContentHandler.repositoryWorkspaceName);
			return ticket;
		} catch (AccessDeniedException ae) {
			throw new GmapException("Access Denied to repository."
					+ ae.getMessage());
		} catch (WorkspaceNotFoundException we) {
			throw new GmapException("Workspace not found."
					+ we.getMessage());
		} catch (LoginException e) {
			throw new GmapException("Login failed." + e.getMessage());
		}
	}

	/* ===============Methods implemented from ToolContentImport102Manager =============== */
	

    /**
     * Import the data for a 1.0.2 Gmap
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues)
    {
    	Date now = new Date();
    	
    	Gmap gmap = new Gmap();
    	gmap.setContentInUse(Boolean.FALSE);
    	gmap.setCreateBy(new Long(user.getUserID().longValue()));
    	gmap.setCreateDate(now);
    	gmap.setDefineLater(Boolean.FALSE);
    	gmap.setInstructions(WebUtil.convertNewlines((String)importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
    	gmap.setLockOnFinished(Boolean.TRUE);
    	gmap.setOfflineInstructions(null);
    	gmap.setOnlineInstructions(null);
    	gmap.setRunOffline(Boolean.FALSE);
    	gmap.setTitle((String)importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
    	gmap.setToolContentId(toolContentId);
    	gmap.setUpdateDate(now);
    	//gmap.setAllowRichEditor(Boolean.FALSE);
    	// leave as empty, no need to set them to anything.
    	//setGmapAttachments(Set gmapAttachments);
    	//setGmapSessions(Set gmapSessions);
    	gmapDAO.saveOrUpdate(gmap);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {

    	logger.warn("Setting the reflective field on a gmap. This doesn't make sense as the gmap is for reflection and we don't reflect on reflection!");
    	Gmap gmap = getGmapByContentId(toolContentId);
    	if ( gmap == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}
    	
    	gmap.setInstructions(description);
    }
    
    //=========================================================================================
	/* ********** Used by Spring to "inject" the linked objects ************* */

	public IGmapAttachmentDAO getGmapAttachmentDAO() {
		return gmapAttachmentDAO;
	}

	public void setGmapAttachmentDAO(IGmapAttachmentDAO attachmentDAO) {
		this.gmapAttachmentDAO = attachmentDAO;
	}

	public IGmapDAO getGmapDAO() {
		return gmapDAO;
	}

	public void setGmapDAO(IGmapDAO gmapDAO) {
		this.gmapDAO = gmapDAO;
	}

	public IToolContentHandler getGmapToolContentHandler() {
		return gmapToolContentHandler;
	}

	public void setGmapToolContentHandler(
			IToolContentHandler gmapToolContentHandler) {
		this.gmapToolContentHandler = gmapToolContentHandler;
	}

	public IGmapSessionDAO getGmapSessionDAO() {
		return gmapSessionDAO;
	}

	public void setGmapSessionDAO(IGmapSessionDAO sessionDAO) {
		this.gmapSessionDAO = sessionDAO;
	}

	public ILamsToolService getToolService() {
		return toolService;
	}

	public void setToolService(ILamsToolService toolService) {
		this.toolService = toolService;
	}

	public IGmapUserDAO getGmapUserDAO() {
		return gmapUserDAO;
	}

	public void setGmapUserDAO(IGmapUserDAO userDAO) {
		this.gmapUserDAO = userDAO;
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

	public IGmapMarkerDAO getGmapMarkerDAO() {
		return gmapMarkerDAO;
	}

	public void setGmapMarkerDAO(IGmapMarkerDAO gmapMarkerDAO) {
		this.gmapMarkerDAO = gmapMarkerDAO;
	}
}
