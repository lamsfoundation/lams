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

package org.lamsfoundation.lams.tool.videoRecorder.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

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
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderAttachmentDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderRecordingDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderSessionDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderUserDAO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderAttachment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderCondition;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderConstants;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IVideoRecorderService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class VideoRecorderService implements ToolSessionManager, ToolContentManager, IVideoRecorderService,
	ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(VideoRecorderService.class.getName());

    private IVideoRecorderDAO videoRecorderDAO = null;

    private IVideoRecorderSessionDAO videoRecorderSessionDAO = null;
    
    private IVideoRecorderRecordingDAO videoRecorderRecordingDAO = null;

    private IVideoRecorderUserDAO videoRecorderUserDAO = null;

    private IVideoRecorderAttachmentDAO videoRecorderAttachmentDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler videoRecorderToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private VideoRecorderOutputFactory videoRecorderOutputFactory;

    private Random generator = new Random();

    public VideoRecorderService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (VideoRecorderService.logger.isDebugEnabled()) {
	    VideoRecorderService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	VideoRecorderSession session = new VideoRecorderSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	// TODO need to also set other fields.
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	session.setVideoRecorder(videoRecorder);
	videoRecorderSessionDAO.saveOrUpdate(session);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	videoRecorderSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getVideoRecorderOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getVideoRecorderOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ************ Methods from ToolContentManager ************************* */

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (VideoRecorderService.logger.isDebugEnabled()) {
	    VideoRecorderService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	VideoRecorder fromContent = null;
	if (fromContentId != null) {
	    fromContent = videoRecorderDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	VideoRecorder toContent = VideoRecorder.newInstance(fromContent, toContentId, videoRecorderToolContentHandler);
	videoRecorderDAO.saveOrUpdate(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	if (videoRecorder == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	videoRecorder.setDefineLater(value);
	videoRecorderDAO.saveOrUpdate(videoRecorder);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	if (videoRecorder == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	videoRecorder.setRunOffline(value);
	videoRecorderDAO.saveOrUpdate(videoRecorder);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws DataMissingException
     *                 if no tool content matches the toolSessionId
     * @throws ToolException
     *                 if any other error occurs
     */

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	if (videoRecorder == null) {
	    videoRecorder = getDefaultContent();
	}
	if (videoRecorder == null) {
	    throw new DataMissingException("Unable to find default content for the videoRecorder tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	videoRecorder = VideoRecorder.newInstance(videoRecorder, toolContentId, null);
	videoRecorder.setToolContentHandler(null);
	videoRecorder.setVideoRecorderSessions(null);
	Set<VideoRecorderAttachment> atts = videoRecorder.getVideoRecorderAttachments();
	for (VideoRecorderAttachment att : atts) {
	    att.setVideoRecorder(null);
	}
	try {
	    exportContentService.registerFileClassForExport(VideoRecorderAttachment.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, videoRecorder, videoRecorderToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws ToolException
     *                 if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    exportContentService.registerFileClassForImport(VideoRecorderAttachment.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null, null);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, videoRecorderToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof VideoRecorder)) {
		throw new ImportToolContentException("Import VideoRecorder tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    VideoRecorder videoRecorder = (VideoRecorder) toolPOJO;

	    // reset it to new toolContentId
	    videoRecorder.setToolContentId(toolContentId);
	    videoRecorder.setCreateBy(new Long(newUserUid.longValue()));

	    videoRecorderDAO.saveOrUpdate(videoRecorder);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     * 
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId) throws ToolException {
	VideoRecorder videoRecorder = getVideoRecorderDAO().getByContentId(toolContentId);
	if (videoRecorder == null) {
	    videoRecorder = getDefaultContent();
	}
	return getVideoRecorderOutputFactory().getToolOutputDefinitions(videoRecorder);
    }

    /* ********** IVideoRecorderService Methods ********************************* */

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
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
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    VideoRecorderService.logger.error(error);
	    throw new VideoRecorderException(error);
	}
	return toolContentId;
    }

    public VideoRecorder getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(VideoRecorderConstants.TOOL_SIGNATURE);
	VideoRecorder defaultContent = getVideoRecorderByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    VideoRecorderService.logger.error(error);
	    throw new VideoRecorderException(error);
	}
	if (defaultContent.getConditions().isEmpty()) {
	    defaultContent.getConditions()
		    .add(getVideoRecorderOutputFactory().createDefaultComplexCondition(defaultContent));
	}
	return defaultContent;
    }

    public VideoRecorder copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the VideoRecorder tools default content: + " + "newContentID is null";
	    VideoRecorderService.logger.error(error);
	    throw new VideoRecorderException(error);
	}

	VideoRecorder defaultContent = getDefaultContent();
	// create new videoRecorder using the newContentID
	VideoRecorder newContent = new VideoRecorder();
	newContent = VideoRecorder.newInstance(defaultContent, newContentID, videoRecorderToolContentHandler);
	videoRecorderDAO.saveOrUpdate(newContent);
	return newContent;
    }

    public VideoRecorder getVideoRecorderByContentId(Long toolContentID) {
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentID);
	if (videoRecorder == null) {
	    VideoRecorderService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return videoRecorder;
    }

    public VideoRecorderSession getSessionBySessionId(Long toolSessionId) {
	VideoRecorderSession videoRecorderSession = videoRecorderSessionDAO.getBySessionId(toolSessionId);
	if (videoRecorderSession == null) {
	    VideoRecorderService.logger.debug("Could not find the videoRecorder session with toolSessionID:" + toolSessionId);
	}
	return videoRecorderSession;
    }

    public VideoRecorderUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return videoRecorderUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public VideoRecorderUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return videoRecorderUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public VideoRecorderUser getUserByUID(Long uid) {
	return videoRecorderUserDAO.getByUID(uid);
    }
    
    public VideoRecorderRecording getRecordingById(Long recordingId){
	return videoRecorderRecordingDAO.getRecordingById(recordingId);
    }

    public List<VideoRecorderRecordingDTO> getRecordingsByToolSessionId(Long toolSessionId){
    	List<VideoRecorderRecording> list = videoRecorderRecordingDAO.getByToolSessionId(toolSessionId);
    	
    	return VideoRecorderRecordingDTO.getVideoRecorderRecordingDTO(list);
    }
    
    public List<VideoRecorderRecordingDTO> getRecordingsByToolSessionIdAndUserId(Long toolSessionId, Long userId){
    	List<VideoRecorderRecording> list = videoRecorderRecordingDAO.getBySessionAndUserIds(toolSessionId, userId);
    	
    	return VideoRecorderRecordingDTO.getVideoRecorderRecordingDTO(list);
    }
    
    public VideoRecorderAttachment uploadFileToContent(Long toolContentId, FormFile file, String type) {
	if (file == null || StringUtils.isEmpty(file.getFileName())) {
	    throw new VideoRecorderException("Could not find upload file: " + file);
	}

	NodeKey nodeKey = processFile(file, type);

	VideoRecorderAttachment attachment = new VideoRecorderAttachment(nodeKey.getVersion(), type, file.getFileName(), nodeKey
		.getUuid(), new Date());
	return attachment;
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws VideoRecorderException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new VideoRecorderException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type) {
	videoRecorderDAO.deleteInstructionFile(contentID, uuid, versionID, type);

    }

    public void saveOrUpdateVideoRecorder(VideoRecorder videoRecorder) {
	videoRecorderDAO.saveOrUpdate(videoRecorder);
    }

    public void saveOrUpdateVideoRecorderSession(VideoRecorderSession videoRecorderSession) {
	videoRecorderSessionDAO.saveOrUpdate(videoRecorderSession);
    }

    public void saveOrUpdateVideoRecorderUser(VideoRecorderUser videoRecorderUser) {
	videoRecorderUserDAO.saveOrUpdate(videoRecorderUser);
    }
    
    public void saveOrUpdateVideoRecorderRecording(VideoRecorderRecording videoRecorderRecording){
    videoRecorderRecordingDAO.saveOrUpdate(videoRecorderRecording);
    }

    public VideoRecorderUser createVideoRecorderUser(UserDTO user, VideoRecorderSession videoRecorderSession) {
	VideoRecorderUser videoRecorderUser = new VideoRecorderUser(user, videoRecorderSession);
	saveOrUpdateVideoRecorderUser(videoRecorderUser);
	return videoRecorderUser;
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
		node = getVideoRecorderToolContentHandler().uploadFile(file.getInputStream(), fileName,
			file.getContentType(), type);
	    } catch (InvalidParameterException e) {
		throw new VideoRecorderException("InvalidParameterException occured while trying to upload File"
			+ e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new VideoRecorderException("FileNotFoundException occured while trying to upload File"
			+ e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new VideoRecorderException("RepositoryCheckedException occured while trying to upload File"
			+ e.getMessage());
	    } catch (IOException e) {
		throw new VideoRecorderException("IOException occured while trying to upload File" + e.getMessage());
	    }
	}
	return node;
    }

    /**
     * This method verifies the credentials of the SubmitFiles Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws SubmitFilesException
     */
    private ITicket getRepositoryLoginTicket() throws VideoRecorderException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(VideoRecorderToolContentHandler.repositoryUser,
		VideoRecorderToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, VideoRecorderToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new VideoRecorderException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new VideoRecorderException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new VideoRecorderException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 VideoRecorder
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	VideoRecorder videoRecorder = new VideoRecorder();
	videoRecorder.setContentInUse(Boolean.FALSE);
	videoRecorder.setCreateBy(new Long(user.getUserID().longValue()));
	videoRecorder.setCreateDate(now);
	videoRecorder.setDefineLater(Boolean.FALSE);
	videoRecorder.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	videoRecorder.setLockOnFinished(Boolean.TRUE);
	videoRecorder.setOfflineInstructions(null);
	videoRecorder.setOnlineInstructions(null);
	videoRecorder.setRunOffline(Boolean.FALSE);
	videoRecorder.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	videoRecorder.setToolContentId(toolContentId);
	videoRecorder.setUpdateDate(now);
	// leave as empty, no need to set them to anything.
	// setVideoRecorderAttachments(Set videoRecorderAttachments);
	// setVideoRecorderSessions(Set videoRecorderSessions);
	videoRecorderDAO.saveOrUpdate(videoRecorder);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	VideoRecorderService.logger
		.warn("Setting the reflective field on a videoRecorder. This doesn't make sense as the videoRecorder is for reflection and we don't reflect on reflection!");
	VideoRecorder videoRecorder = getVideoRecorderByContentId(toolContentId);
	if (videoRecorder == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	videoRecorder.setInstructions(description);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IVideoRecorderAttachmentDAO getVideoRecorderAttachmentDAO() {
	return videoRecorderAttachmentDAO;
    }

    public void setVideoRecorderAttachmentDAO(IVideoRecorderAttachmentDAO attachmentDAO) {
	videoRecorderAttachmentDAO = attachmentDAO;
    }

    public IVideoRecorderDAO getVideoRecorderDAO() {
	return videoRecorderDAO;
    }

    public void setVideoRecorderDAO(IVideoRecorderDAO videoRecorderDAO) {
	this.videoRecorderDAO = videoRecorderDAO;
    }

    public IToolContentHandler getVideoRecorderToolContentHandler() {
	return videoRecorderToolContentHandler;
    }

    public void setVideoRecorderToolContentHandler(IToolContentHandler videoRecorderToolContentHandler) {
	this.videoRecorderToolContentHandler = videoRecorderToolContentHandler;
    }

    public IVideoRecorderSessionDAO getVideoRecorderSessionDAO() {
	return videoRecorderSessionDAO;
    }

    public void setVideoRecorderSessionDAO(IVideoRecorderSessionDAO sessionDAO) {
	videoRecorderSessionDAO = sessionDAO;
    }

    public IVideoRecorderRecordingDAO getVideoRecorderRecordingDAO() {
	return videoRecorderRecordingDAO;
    }

    public void setVideoRecorderRecordingDAO(IVideoRecorderRecordingDAO videoRecorderRecordingDAO) {
    this.videoRecorderRecordingDAO = videoRecorderRecordingDAO;
    }
        
    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IVideoRecorderUserDAO getVideoRecorderUserDAO() {
	return videoRecorderUserDAO;
    }

    public void setVideoRecorderUserDAO(IVideoRecorderUserDAO userDAO) {
	videoRecorderUserDAO = userDAO;
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

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public VideoRecorderOutputFactory getVideoRecorderOutputFactory() {
	return videoRecorderOutputFactory;
    }

    public void setVideoRecorderOutputFactory(VideoRecorderOutputFactory videoRecorderOutputFactory) {
	this.videoRecorderOutputFactory = videoRecorderOutputFactory;
    }

    /**
     * {@inheritDoc}
     */
    public String createConditionName(Collection<VideoRecorderCondition> existingConditions) {
	String uniqueNumber = null;
	do {
	    uniqueNumber = String.valueOf(Math.abs(generator.nextInt()));
	    for (VideoRecorderCondition condition : existingConditions) {
		String[] splitedName = getVideoRecorderOutputFactory().splitConditionName(condition.getName());
		if (uniqueNumber.equals(splitedName[1])) {
		    uniqueNumber = null;
		}
	    }
	} while (uniqueNumber == null);
	return getVideoRecorderOutputFactory().buildConditionName(uniqueNumber);
    }

    public void releaseConditionsFromCache(VideoRecorder videoRecorder) {
	if (videoRecorder.getConditions() != null) {
	    for (VideoRecorderCondition condition : videoRecorder.getConditions()) {
		getVideoRecorderDAO().releaseFromCache(condition);
	    }
	}
    }

    public void deleteCondition(VideoRecorderCondition condition) {
	if (condition != null && condition.getConditionId() != null) {
	    videoRecorderDAO.delete(condition);
	}
    }
}
