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

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderCommentDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderRatingDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderRecordingDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderSessionDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderUserDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderCommentDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRatingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderComment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderCondition;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRating;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderConstants;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * An implementation of the IVideoRecorderService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class VideoRecorderService
	implements ToolSessionManager, ToolContentManager, IVideoRecorderService, ToolContentImport102Manager {

    private static Logger logger = Logger.getLogger(VideoRecorderService.class.getName());

    private IVideoRecorderDAO videoRecorderDAO = null;

    private IVideoRecorderSessionDAO videoRecorderSessionDAO = null;

    private IVideoRecorderRecordingDAO videoRecorderRecordingDAO = null;

    private IVideoRecorderUserDAO videoRecorderUserDAO = null;

    private IVideoRecorderRatingDAO videoRecorderRatingDAO = null;

    private IVideoRecorderCommentDAO videoRecorderCommentDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler videoRecorderToolContentHandler = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private VideoRecorderOutputFactory videoRecorderOutputFactory;

    private MessageService messageService;

    public VideoRecorderService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (VideoRecorderService.logger.isDebugEnabled()) {
	    VideoRecorderService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	VideoRecorderSession session = new VideoRecorderSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	session.setContentFolderId(FileUtil.generateUniqueContentFolderID());

	// learner starts
	// TODO need to also set other fields.
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	session.setVideoRecorder(videoRecorder);
	videoRecorderSessionDAO.saveOrUpdate(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	videoRecorderSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getVideoRecorderOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getVideoRecorderOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    /* ************ Methods from ToolContentManager ************************* */

    @Override
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

	List<VideoRecorderRecording> list = videoRecorderRecordingDAO.getByToolContentId(fromContentId);

	VideoRecorderRecording vrr;
	if (!list.isEmpty()) {
	    vrr = list.get(0);
	    VideoRecorderRecording clonedRecording = new VideoRecorderRecording(vrr);
	    clonedRecording.setUid(null);
	    clonedRecording.setToolContentId(toContentId);
	    videoRecorderRecordingDAO.saveOrUpdate(clonedRecording);
	}

	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}

	VideoRecorder toContent = VideoRecorder.newInstance(fromContent, toContentId);
	videoRecorderDAO.saveOrUpdate(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	if (videoRecorder == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	videoRecorder.setDefineLater(false);
	videoRecorderDAO.saveOrUpdate(videoRecorder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	if (videoRecorder == null) {
	    VideoRecorderService.logger
		    .warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (VideoRecorderSession session : (Set<VideoRecorderSession>) videoRecorder.getVideoRecorderSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, VideoRecorderConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	videoRecorderDAO.delete(videoRecorder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (VideoRecorderService.logger.isDebugEnabled()) {
	    VideoRecorderService.logger.debug(
		    "Removing Video Recorder contents for user ID " + userId + " and toolContentId " + toolContentId);
	}

	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentId);
	if (videoRecorder == null) {
	    VideoRecorderService.logger
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (VideoRecorderSession session : (Set<VideoRecorderSession>) videoRecorder.getVideoRecorderSessions()) {
	    List<VideoRecorderComment> comments = videoRecorderCommentDAO.getCommentsByUserId(userId.longValue());
	    videoRecorderCommentDAO.deleteAll(comments);

	    List<VideoRecorderRating> ratings = videoRecorderRatingDAO.getRatingsByUserId(userId.longValue());
	    videoRecorderRatingDAO.deleteAll(ratings);

	    List<VideoRecorderRecording> recordings = videoRecorderRecordingDAO
		    .getBySessionAndUserId(session.getSessionId(), userId.longValue());
	    videoRecorderRecordingDAO.deleteAll(recordings);

	    VideoRecorderUser user = videoRecorderUserDAO.getByUserIdAndSessionId(userId.longValue(),
		    session.getSessionId());

	    if (user != null) {
		if (user.getEntryUID() != null) {
		    NotebookEntry entry = coreNotebookService.getEntry(user.getEntryUID());
		    videoRecorderDAO.delete(entry);
		    user.setEntryUID(null);
		}
		user.setFinishedActivity(false);
		videoRecorderUserDAO.update(user);
	    }
	}
    }

    @Override
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
	videoRecorder = VideoRecorder.newInstance(videoRecorder, toolContentId);
	videoRecorder.setToolContentId(null);
	videoRecorder.setVideoRecorderSessions(null);

	VideoRecorderRecording authorRecording = getFirstRecordingByToolContentId(toolContentId);
	if (authorRecording != null) {
	    authorRecording = (VideoRecorderRecording) authorRecording.clone();

	    authorRecording.setToolContentId(null);
	    authorRecording.setComments(null);
	    authorRecording.setRatings(null);

	}

	videoRecorder.setAuthorRecording(authorRecording);
	try {
	    exportContentService.exportToolContent(toolContentId, videoRecorder, videoRecorderToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(VideoRecorderImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, videoRecorderToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof VideoRecorder)) {
		throw new ImportToolContentException(
			"Import VideoRecorder tool content failed. Deserialized object is " + toolPOJO);
	    }
	    VideoRecorder videoRecorder = (VideoRecorder) toolPOJO;

	    VideoRecorderRecording recording = videoRecorder.getAuthorRecording();

	    // reset it to new toolContentId
	    videoRecorder.setToolContentId(toolContentId);

	    if (recording != null) {
		videoRecorder.getAuthorRecording().setToolContentId(toolContentId);
	    }

	    videoRecorderDAO.saveOrUpdate(videoRecorder);

	    if (recording != null) {
		videoRecorderRecordingDAO.saveOrUpdate(recording);
	    }
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	VideoRecorder videoRecorder = getVideoRecorderDAO().getByContentId(toolContentId);
	if (videoRecorder == null) {
	    videoRecorder = getDefaultContent();
	}
	return getVideoRecorderOutputFactory().getToolOutputDefinitions(videoRecorder, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getVideoRecorderByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getVideoRecorderByContentId(toolContentId).isDefineLater();
    }

    /* ********** IVideoRecorderService Methods ********************************* */

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    @Override
    public NotebookEntry getEntry(Long uid) {
	return coreNotebookService.getEntry(uid);
    }

    @Override
    public void updateEntry(Long uid, String entry) {
	coreNotebookService.updateEntry(uid, "", entry);
    }

    @Override
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

    @Override
    public VideoRecorder getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(VideoRecorderConstants.TOOL_SIGNATURE);
	VideoRecorder defaultContent = getVideoRecorderByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    VideoRecorderService.logger.error(error);
	    throw new VideoRecorderException(error);
	}
	if (defaultContent.getConditions().isEmpty()) {
	    // needed?
	}
	return defaultContent;
    }

    @Override
    public VideoRecorder copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the VideoRecorder tools default content: + " + "newContentID is null";
	    VideoRecorderService.logger.error(error);
	    throw new VideoRecorderException(error);
	}

	VideoRecorder defaultContent = getDefaultContent();
	// create new videoRecorder using the newContentID
	VideoRecorder newContent = new VideoRecorder();
	newContent = VideoRecorder.newInstance(defaultContent, newContentID);
	videoRecorderDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public VideoRecorder getVideoRecorderByContentId(Long toolContentID) {
	VideoRecorder videoRecorder = videoRecorderDAO.getByContentId(toolContentID);
	if (videoRecorder == null) {
	    VideoRecorderService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return videoRecorder;
    }

    @Override
    public VideoRecorderSession getSessionBySessionId(Long toolSessionId) {
	VideoRecorderSession videoRecorderSession = videoRecorderSessionDAO.getBySessionId(toolSessionId);
	if (videoRecorderSession == null) {
	    VideoRecorderService.logger
		    .debug("Could not find the videoRecorder session with toolSessionID:" + toolSessionId);
	}
	return videoRecorderSession;
    }

    @Override
    public VideoRecorderUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return videoRecorderUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public VideoRecorderUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return videoRecorderUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public VideoRecorderUser getUserByUID(Long uid) {
	return videoRecorderUserDAO.getByUID(uid);
    }

    @Override
    public VideoRecorderRecording getRecordingById(Long recordingId) {
	return videoRecorderRecordingDAO.getRecordingById(recordingId);
    }

    @Override
    public void deleteVideoRecorderRecording(VideoRecorderRecording videoRecorderRecording) {
	videoRecorderRecordingDAO.delete(videoRecorderRecording);
	return;
    }

    @Override
    public VideoRecorderRating getRatingById(Long ratingId) {
	return videoRecorderRatingDAO.getRatingById(ratingId);
    }

    @Override
    public VideoRecorderComment getCommentById(Long commentId) {
	return videoRecorderCommentDAO.getCommentById(commentId);
    }

    @Override
    public Set<VideoRecorderRatingDTO> getRatingsByToolSessionId(Long toolSessionId) {
	List<VideoRecorderRating> list = videoRecorderRatingDAO.getRatingsByToolSessionId(toolSessionId);
	return VideoRecorderRatingDTO.getVideoRecorderRatingDTOs(list);
    }

    @Override
    public List<VideoRecorderComment> getCommentsByUserId(Long userId) {
	return videoRecorderCommentDAO.getCommentsByUserId(userId);
    }

    @Override
    public Set<VideoRecorderCommentDTO> getCommentsByToolSessionId(Long toolSessionId) {
	List<VideoRecorderComment> list = videoRecorderCommentDAO.getCommentsByToolSessionId(toolSessionId);
	return VideoRecorderCommentDTO.getVideoRecorderCommentDTOs(list);
    }

    @Override
    public List<VideoRecorderRecordingDTO> getRecordingsByToolSessionId(Long toolSessionId, Long toolContentId) {
	List<VideoRecorderRecording> list = videoRecorderRecordingDAO.getByToolSessionId(toolSessionId);
	list.addAll(videoRecorderRecordingDAO.getByToolContentId(toolContentId));

	return VideoRecorderRecordingDTO.getVideoRecorderRecordingDTOs(list);
    }

    @Override
    public List<VideoRecorderRecordingDTO> getRecordingsByToolSessionIdAndUserUid(Long toolSessionId, Long userId,
	    Long toolContentId) {
	List<VideoRecorderRecording> list = videoRecorderRecordingDAO.getBySessionAndUserUid(toolSessionId, userId);
	list.addAll(videoRecorderRecordingDAO.getByToolContentId(toolContentId));

	return VideoRecorderRecordingDTO.getVideoRecorderRecordingDTOs(list);
    }

    @Override
    public List<VideoRecorderRecordingDTO> getRecordingsByToolContentId(Long toolContentId) {
	List<VideoRecorderRecording> list = videoRecorderRecordingDAO.getByToolContentId(toolContentId);
	return VideoRecorderRecordingDTO.getVideoRecorderRecordingDTOs(list);
    }

    @Override
    public VideoRecorderRecording getFirstRecordingByToolContentId(Long toolContentId) {
	List<VideoRecorderRecording> list = videoRecorderRecordingDAO.getByToolContentId(toolContentId);
	if (!list.isEmpty()) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public void saveOrUpdateVideoRecorder(VideoRecorder videoRecorder) {
	videoRecorderDAO.saveOrUpdate(videoRecorder);
    }

    @Override
    public void saveOrUpdateVideoRecorderSession(VideoRecorderSession videoRecorderSession) {
	videoRecorderSessionDAO.saveOrUpdate(videoRecorderSession);
    }

    @Override
    public void saveOrUpdateVideoRecorderUser(VideoRecorderUser videoRecorderUser) {
	videoRecorderUserDAO.saveOrUpdate(videoRecorderUser);
    }

    @Override
    public void saveOrUpdateVideoRecorderRecording(VideoRecorderRecording videoRecorderRecording) {
	videoRecorderRecordingDAO.saveOrUpdate(videoRecorderRecording);
    }

    @Override
    public void saveOrUpdateVideoRecorderComment(VideoRecorderComment videoRecorderComment) {
	videoRecorderCommentDAO.saveOrUpdate(videoRecorderComment);
    }

    @Override
    public void saveOrUpdateVideoRecorderRating(VideoRecorderRating videoRecorderRating) {
	videoRecorderRatingDAO.saveOrUpdate(videoRecorderRating);
    }

    @Override
    public VideoRecorderUser createVideoRecorderUser(UserDTO user, VideoRecorderSession videoRecorderSession) {
	VideoRecorderUser videoRecorderUser = new VideoRecorderUser(user, videoRecorderSession);
	saveOrUpdateVideoRecorderUser(videoRecorderUser);
	return videoRecorderUser;
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 VideoRecorder
     */
    @Override
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	VideoRecorder videoRecorder = new VideoRecorder();
	videoRecorder.setContentInUse(Boolean.FALSE);
	videoRecorder.setCreateBy(new Long(user.getUserID().longValue()));
	videoRecorder.setCreateDate(now);
	videoRecorder.setDefineLater(Boolean.FALSE);
	videoRecorder.setInstructions(
		WebUtil.convertNewlines((String) importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
	videoRecorder.setLockOnFinished(Boolean.TRUE);
	videoRecorder.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	videoRecorder.setToolContentId(toolContentId);
	videoRecorder.setUpdateDate(now);
	videoRecorderDAO.saveOrUpdate(videoRecorder);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    @Override
    public void setReflectiveData(Long toolContentId, String title, String description)
	    throws ToolException, DataMissingException {

	VideoRecorderService.logger.warn(
		"Setting the reflective field on a videoRecorder. This doesn't make sense as the videoRecorder is for reflection and we don't reflect on reflection!");
	VideoRecorder videoRecorder = getVideoRecorderByContentId(toolContentId);
	if (videoRecorder == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	videoRecorder.setInstructions(description);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

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

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public MessageService getMessageService() {
	return messageService;
    }

    public IVideoRecorderCommentDAO getVideoRecorderCommentDAO() {
	return videoRecorderCommentDAO;
    }

    public void setVideoRecorderCommentDAO(IVideoRecorderCommentDAO commentDAO) {
	videoRecorderCommentDAO = commentDAO;
    }

    public IVideoRecorderRatingDAO getVideoRecorderRatingDAO() {
	return videoRecorderRatingDAO;
    }

    public void setVideoRecorderRatingDAO(IVideoRecorderRatingDAO ratingDAO) {
	videoRecorderRatingDAO = ratingDAO;
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

    @Override
    public void releaseConditionsFromCache(VideoRecorder videoRecorder) {
	if (videoRecorder.getConditions() != null) {
	    for (VideoRecorderCondition condition : videoRecorder.getConditions()) {
		getVideoRecorderDAO().releaseFromCache(condition);
	    }
	}
    }

    @Override
    public void deleteCondition(VideoRecorderCondition condition) {
	if ((condition != null) && (condition.getConditionId() != null)) {
	    videoRecorderDAO.delete(condition);
	}
    }

    @Override
    public Long getNbRecordings(Long userID, Long sessionId) {
	return videoRecorderRecordingDAO.getNbRecordings(userID, sessionId);
    }

    @Override
    public Long getNbComments(Long userID, Long sessionId) {
	return videoRecorderCommentDAO.getNbComments(userID, sessionId);
    }

    @Override
    public Long getNbRatings(Long userID, Long sessionId) {
	return videoRecorderRatingDAO.getNbRatings(userID, sessionId);
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    /**
     * @return String of xml with all needed language elements
     */
    @Override
    public String getLanguageXML() {
	ArrayList<String> languageCollection = new ArrayList<String>();
	languageCollection.add(new String("button.ok"));
	languageCollection.add(new String("button.save"));
	languageCollection.add(new String("button.cancel"));
	languageCollection.add(new String("button.yes"));
	languageCollection.add(new String("button.no"));
	languageCollection.add(new String("videorecorder.stop.recording"));
	languageCollection.add(new String("videorecorder.start.recording"));
	languageCollection.add(new String("videorecorder.stop.camera"));
	languageCollection.add(new String("videorecorder.view.camera"));
	languageCollection.add(new String("videorecorder.author"));
	languageCollection.add(new String("videorecorder.description"));
	languageCollection.add(new String("videorecorder.title"));
	languageCollection.add(new String("videorecorder.new.recording.details"));
	languageCollection.add(new String("videorecorder.comment"));
	languageCollection.add(new String("videorecorder.date"));
	languageCollection.add(new String("videorecorder.rating"));
	languageCollection.add(new String("videorecorder.play"));
	languageCollection.add(new String("videorecorder.stop"));
	languageCollection.add(new String("videorecorder.pause"));
	languageCollection.add(new String("videorecorder.resume"));
	languageCollection.add(new String("videorecorder.video.information"));
	languageCollection.add(new String("videorecorder.sort.by"));
	languageCollection.add(new String("videorecorder.recording.controls"));
	languageCollection.add(new String("videorecorder.videos"));
	languageCollection.add(new String("videorecorder.add.comment"));
	languageCollection.add(new String("videorecorder.video"));
	languageCollection.add(new String("videorecorder.export.video"));
	languageCollection.add(new String("videorecorder.delete.video"));
	languageCollection.add(new String("videorecorder.enter.something.here"));
	languageCollection.add(new String("videorecorder.message.sure.delete"));
	languageCollection.add(new String("videorecorder.confirm"));
	languageCollection.add(new String("videorecorder.update"));
	languageCollection.add(new String("videorecorder.refresh"));
	languageCollection.add(new String("videorecorder.web.application.not.available"));
	languageCollection.add(new String("videorecorder.net.connection.not.connected"));
	languageCollection.add(new String("videorecorder.net.connection.closed"));
	languageCollection.add(new String("videorecorder.playing"));
	languageCollection.add(new String("videorecorder.ready"));
	languageCollection.add(new String("videorecorder.paused"));
	languageCollection.add(new String("videorecorder.recording"));
	languageCollection.add(new String("videorecorder.buffering"));
	languageCollection.add(new String("videorecorder.waiting"));
	languageCollection.add(new String("videorecorder.tooltip.sort.author"));
	languageCollection.add(new String("videorecorder.tooltip.sort.title"));
	languageCollection.add(new String("videorecorder.tooltip.sort.date"));
	languageCollection.add(new String("videorecorder.tooltip.play"));
	languageCollection.add(new String("videorecorder.tooltip.pause"));
	languageCollection.add(new String("videorecorder.tooltip.resume"));
	languageCollection.add(new String("videorecorder.tooltip.start.camera"));
	languageCollection.add(new String("videorecorder.tooltip.stop.camera"));
	languageCollection.add(new String("videorecorder.tooltip.add.comment"));
	languageCollection.add(new String("videorecorder.tooltip.add.rating"));
	languageCollection.add(new String("videorecorder.tooltip.refresh"));
	languageCollection.add(new String("videorecorder.tooltip.save.recording"));
	languageCollection.add(new String("videorecorder.tooltip.save.comment"));
	languageCollection.add(new String("videorecorder.tooltip.cancel.comment"));
	languageCollection.add(new String("videorecorder.tooltip.start.recording"));
	languageCollection.add(new String("videorecorder.tooltip.stop.recording"));
	languageCollection.add(new String("videorecorder.tooltip.delete.recording"));
	languageCollection.add(new String("videorecorder.tooltip.export.recording"));
	languageCollection.add(new String("videorecorder.tooltip.click.to.ready.recording"));
	languageCollection.add(new String("videorecorder.tooltip.rate.recording"));
	languageCollection.add(new String("videorecorder.tooltip.already.rated"));
	languageCollection.add(new String("videorecorder.disabled"));
	languageCollection.add(new String("videorecorder.camera.not.available"));
	languageCollection.add(new String("videorecorder.mic.not.available"));

	String languageOutput = "<xml><language>";

	for (int i = 0; i < languageCollection.size(); i++) {
	    languageOutput += "<entry key='" + languageCollection.get(i) + "'><name>"
		    + messageService.getMessage(languageCollection.get(i)) + "</name></entry>";
	}

	languageOutput += "</language></xml>";

	return languageOutput;
    }

    @Override
    public String getLanguageXMLForFCK() {
	ArrayList<String> languageCollection = new ArrayList<String>();
	languageCollection.add(new String("button.ok"));
	languageCollection.add(new String("button.save"));
	languageCollection.add(new String("button.cancel"));
	languageCollection.add(new String("button.yes"));
	languageCollection.add(new String("button.no"));
	languageCollection.add(new String("videorecorder.video.player"));
	languageCollection.add(new String("videorecorder.video.recorder"));
	languageCollection.add(new String("videorecorder.web.application.not.available"));
	languageCollection.add(new String("videorecorder.net.connection.not.connected"));
	languageCollection.add(new String("videorecorder.net.connection.closed"));
	languageCollection.add(new String("videorecorder.playing"));
	languageCollection.add(new String("videorecorder.ready"));
	languageCollection.add(new String("videorecorder.paused"));
	languageCollection.add(new String("videorecorder.recording"));
	languageCollection.add(new String("videorecorder.buffering"));
	languageCollection.add(new String("videorecorder.waiting"));
	languageCollection.add(new String("videorecorder.description"));
	languageCollection.add(new String("videorecorder.title"));
	languageCollection.add(new String("videorecorder.new.recording.details"));
	languageCollection.add(new String("videorecorder.recording.complete.authoring"));
	languageCollection.add(new String("videorecorder.enter.something.here"));
	languageCollection.add(new String("videorecorder.recording.complete.fck"));
	languageCollection.add(new String("videorecorder.tooltip.play"));
	languageCollection.add(new String("videorecorder.tooltip.pause"));
	languageCollection.add(new String("videorecorder.tooltip.resume"));
	languageCollection.add(new String("videorecorder.tooltip.save.recording"));
	languageCollection.add(new String("videorecorder.tooltip.start.recording"));
	languageCollection.add(new String("videorecorder.tooltip.start.recording.again"));
	languageCollection.add(new String("videorecorder.tooltip.start.recording.next"));
	languageCollection.add(new String("videorecorder.tooltip.stop.recording"));
	languageCollection.add(new String("videorecorder.disabled"));
	languageCollection.add(new String("videorecorder.camera.not.available"));
	languageCollection.add(new String("videorecorder.mic.not.available"));

	String languageOutput = "<xml><language>";

	for (int i = 0; i < languageCollection.size(); i++) {
	    languageOutput += "<entry key='" + languageCollection.get(i) + "'><name>"
		    + messageService.getMessage(languageCollection.get(i)) + "</name></entry>";
	}

	languageOutput += "</language></xml>";

	return languageOutput;
    }

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getVideoRecorderOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}