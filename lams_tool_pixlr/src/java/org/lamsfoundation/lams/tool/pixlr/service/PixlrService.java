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

package org.lamsfoundation.lams.tool.pixlr.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrConfigItemDAO;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrDAO;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrSessionDAO;
import org.lamsfoundation.lams.tool.pixlr.dao.IPixlrUserDAO;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrConfigItem;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrSession;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrUser;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrConstants;
import org.lamsfoundation.lams.tool.pixlr.util.PixlrException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

/**
 * An implementation of the IPixlrService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class PixlrService implements ToolSessionManager, ToolContentManager, IPixlrService {

    private static Logger logger = Logger.getLogger(PixlrService.class.getName());

    public static final String EXPORT_IMAGE_FILE_NAME = "authorImage";

    private IPixlrDAO pixlrDAO = null;

    private IPixlrSessionDAO pixlrSessionDAO = null;

    private IPixlrUserDAO pixlrUserDAO = null;

    private ILamsToolService toolService;

    private IToolContentHandler pixlrToolContentHandler = null;

    private ILogEventService logEventService = null;

    private IExportToolContentService exportContentService;

    private PixlrOutputFactory pixlrOutputFactory;

    private IPixlrConfigItemDAO pixlrConfigItemDAO;

    public PixlrService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (PixlrService.logger.isDebugEnabled()) {
	    PixlrService.logger.debug(
		    "entering method createToolSession:" + " toolSessionId = " + toolSessionId + " toolSessionName = "
			    + toolSessionName + " toolContentId = " + toolContentId);
	}

	PixlrSession session = new PixlrSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	// TODO need to also set other fields.
	Pixlr pixlr = pixlrDAO.getByContentId(toolContentId);
	session.setPixlr(pixlr);
	pixlrSessionDAO.saveOrUpdate(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ToolSessionExportOutputData exportToolSession(List ToolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	pixlrSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getPixlrOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getPixlrOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<ToolOutput>();
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	return null;
    }

    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /* ************ Methods from ToolContentManager ************************* */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (PixlrService.logger.isDebugEnabled()) {
	    PixlrService.logger.debug(
		    "entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
			    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Pixlr fromContent = null;
	if (fromContentId != null) {
	    fromContent = pixlrDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Pixlr toContent = Pixlr.newInstance(fromContent, toContentId);

	try {
	    toContent.setImageFileName(copyImage(toContent));
	} catch (Exception e) {
	    PixlrService.logger.error("Could not copy image for tool content copy", e);
	    throw new ToolException(e);
	}

	pixlrDAO.saveOrUpdate(toContent);
    }

    public String copyImage(Pixlr toContent) throws Exception {

	String realBaseDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator + FileUtil.LAMS_WWW_DIR
		+ File.separator + "images" + File.separator + "pixlr";

	File existingFile = new File(realBaseDir + File.separator + toContent.getImageFileName());

	if (existingFile.exists() && existingFile.canRead()) {
	    String ext = getFileExtension(toContent.getImageFileName());
	    String newFileName = FileUtil.generateUniqueContentFolderID() + ext;

	    String newFilePath = realBaseDir + File.separator + newFileName;
	    copyFile(existingFile, newFilePath);
	    return newFileName;
	} else {
	    // if cant find or read the file, just copy the default image file
	    if (existingFile.exists() && existingFile.canRead()) {
		File existingFile2 = new File(getDefaultContent().getImageFileName());
		String ext = getFileExtension(toContent.getImageFileName());
		String newFileName = FileUtil.generateUniqueContentFolderID() + ext;
		String newFilePath = realBaseDir + File.separator + newFileName;
		copyFile(existingFile2, newFilePath);
		return newFileName;
	    } else {
		throw new PixlrException("Could not find file to copy");
	    }
	}
    }

    public void copyFile(File srcFile, String destPath) throws Exception {
	if (srcFile.exists() && srcFile.canRead()) {
	    File newFile = new File(destPath);
	    FileOutputStream out = new FileOutputStream(newFile);
	    FileInputStream in = new FileInputStream(srcFile);
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	}

    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Pixlr pixlr = pixlrDAO.getByContentId(toolContentId);
	if (pixlr == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	pixlr.setDefineLater(false);
	pixlrDAO.saveOrUpdate(pixlr);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws SessionDataExistsException, ToolException {
	Pixlr pixlr = pixlrDAO.getByContentId(toolContentId);
	if (pixlr == null) {
	    PixlrService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	pixlrDAO.delete(pixlr);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {
	if (logger.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		logger.debug(
			"Resetting Pixlr completion for user ID " + userId + " and toolContentId " + toolContentId);
	    } else {
		logger.debug("Removing Pixlr image for user ID " + userId + " and toolContentId " + toolContentId);
	    }
	}

	Pixlr pixlr = pixlrDAO.getByContentId(toolContentId);
	if (pixlr != null) {
	    for (PixlrSession session : pixlr.getPixlrSessions()) {
		PixlrUser user = pixlrUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
		if (resetActivityCompletionOnly) {
		    user.setFinishedActivity(false);
		    pixlrUserDAO.saveOrUpdate(user);
		} else {
		    pixlrUserDAO.delete(user);
		}
	    }
	}
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws DataMissingException
     * 	if no tool content matches the toolSessionId
     * @throws ToolException
     * 	if any other error occurs
     */

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Pixlr pixlr = pixlrDAO.getByContentId(toolContentId);
	if (pixlr == null) {
	    pixlr = getDefaultContent();
	}
	if (pixlr == null) {
	    throw new DataMissingException("Unable to find default content for the pixlr tool");
	}

	pixlr = Pixlr.newInstance(pixlr, toolContentId);
	pixlr.setPixlrSessions(null);

	// bundling the author image in export
	try {
	    if (pixlr.getImageFileName() != null) {
		File imageFile = new File(
			PixlrConstants.LAMS_PIXLR_BASE_DIR + File.separator + pixlr.getImageFileName());
		if (imageFile.exists()) {

		    String ext = getFileExtension(pixlr.getImageFileName());

		    String tempDir = rootPath + File.separator + toolContentId.toString();
		    File tempDirFile = new File(tempDir);
		    if (!tempDirFile.exists()) {
			tempDirFile.mkdirs();
		    }
		    String newFilePath = tempDir + File.separator + PixlrService.EXPORT_IMAGE_FILE_NAME + ext;

		    copyFile(imageFile, newFilePath);
		    pixlr.setImageFileName(PixlrService.EXPORT_IMAGE_FILE_NAME + ext);
		}
	    }

	} catch (Exception e) {
	    PixlrService.logger.error("Could not export pixlr image, image may be missing in export", e);
	}

	try {
	    exportContentService.exportToolContent(toolContentId, pixlr, pixlrToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws ToolException
     * 	if any other error occurs
     */
    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(PixlrImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, pixlrToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Pixlr)) {
		throw new ImportToolContentException(
			"Import Pixlr tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Pixlr pixlr = (Pixlr) toolPOJO;

	    // reset it to new toolContentId
	    pixlr.setToolContentId(toolContentId);
	    pixlr.setCreateBy(new Long(newUserUid.longValue()));

	    // Copying the image file into lams_www.war/images/pixlr
	    File imageFile = new File(toolContentPath + File.separator + pixlr.getImageFileName());

	    if (imageFile.exists() && imageFile.canRead()) {

		String newFileName =
			FileUtil.generateUniqueContentFolderID() + getFileExtension(pixlr.getImageFileName());

		String newFilePath = PixlrConstants.LAMS_PIXLR_BASE_DIR + File.separator + newFileName;

		copyFile(imageFile, newFilePath);
		pixlr.setImageFileName(newFileName);
	    } else {
		pixlr.setImageFileName(getDefaultContent().getImageFileName());
	    }

	    pixlrDAO.saveOrUpdate(pixlr);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	} catch (Exception e) {
	    PixlrService.logger.error("Error during import possibly because of file copy error", e);
	    throw new ToolException(e);
	}
    }

    @Override
    public String getFileExtension(String fileName) {
	String ext = "";
	int i = fileName.lastIndexOf('.');
	if ((i > 0) && (i < (fileName.length() - 1))) {
	    ext += "." + fileName.substring(i + 1).toLowerCase();
	}
	return ext;
    }

    /**
     * Get the definitions for possible output for an activity, based on the toolContentId. These may be definitions
     * that are always available for the tool (e.g. number of marks for Multiple Choice) or a custom definition created
     * for a particular activity such as the answer to the third question contains the word Koala and hence the need for
     * the toolContentId
     *
     * @return SortedMap of ToolOutputDefinitions with the key being the name of each definition
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Pixlr pixlr = getPixlrDAO().getByContentId(toolContentId);
	if (pixlr == null) {
	    pixlr = getDefaultContent();
	}
	return getPixlrOutputFactory().getToolOutputDefinitions(pixlr, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getPixlrByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getPixlrByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Pixlr pixlr = pixlrDAO.getByContentId(toolContentId);
	for (PixlrSession session : pixlr.getPixlrSessions()) {
	    if (!session.getPixlrUsers().isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    /* ********** IPixlrService Methods ********************************* */
    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    PixlrService.logger.error(error);
	    throw new PixlrException(error);
	}
	return toolContentId;
    }

    @Override
    public Pixlr getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(PixlrConstants.TOOL_SIGNATURE);
	Pixlr defaultContent = getPixlrByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    PixlrService.logger.error(error);
	    throw new PixlrException(error);
	}
	return defaultContent;
    }

    @Override
    public Pixlr copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Pixlr tools default content: + " + "newContentID is null";
	    PixlrService.logger.error(error);
	    throw new PixlrException(error);
	}

	Pixlr defaultContent = getDefaultContent();
	// create new pixlr using the newContentID
	Pixlr newContent = new Pixlr();
	newContent = Pixlr.newInstance(defaultContent, newContentID);
	pixlrDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Pixlr getPixlrByContentId(Long toolContentID) {
	Pixlr pixlr = pixlrDAO.getByContentId(toolContentID);
	if (pixlr == null) {
	    PixlrService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return pixlr;
    }

    @Override
    public PixlrSession getSessionBySessionId(Long toolSessionId) {
	PixlrSession pixlrSession = pixlrSessionDAO.getBySessionId(toolSessionId);
	if (pixlrSession == null) {
	    PixlrService.logger.debug("Could not find the pixlr session with toolSessionID:" + toolSessionId);
	}
	return pixlrSession;
    }

    @Override
    public PixlrUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return pixlrUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public PixlrUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return pixlrUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public PixlrUser getUserByUID(Long uid) {
	return pixlrUserDAO.getByUID(uid);
    }

    @Override
    public void saveOrUpdatePixlr(Pixlr pixlr) {
	pixlrDAO.saveOrUpdate(pixlr);
    }

    @Override
    public void saveOrUpdatePixlrSession(PixlrSession pixlrSession) {
	pixlrSessionDAO.saveOrUpdate(pixlrSession);
    }

    @Override
    public void saveOrUpdatePixlrUser(PixlrUser pixlrUser) {
	pixlrUserDAO.saveOrUpdate(pixlrUser);
    }

    @Override
    public PixlrUser createPixlrUser(UserDTO user, PixlrSession pixlrSession) {
	PixlrUser pixlrUser = new PixlrUser(user, pixlrSession);
	saveOrUpdatePixlrUser(pixlrUser);
	return pixlrUser;
    }

    public ILogEventService getLogEventService() {
	return logEventService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
    }

    @Override
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
    }

    @Override
    public PixlrConfigItem getConfigItem(String key) {
	return pixlrConfigItemDAO.getConfigItemByKey(key);
    }

    @Override
    public void saveOrUpdatePixlrConfigItem(PixlrConfigItem item) {
	pixlrConfigItemDAO.saveOrUpdate(item);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IPixlrDAO getPixlrDAO() {
	return pixlrDAO;
    }

    public void setPixlrDAO(IPixlrDAO pixlrDAO) {
	this.pixlrDAO = pixlrDAO;
    }

    public IToolContentHandler getPixlrToolContentHandler() {
	return pixlrToolContentHandler;
    }

    public void setPixlrToolContentHandler(IToolContentHandler pixlrToolContentHandler) {
	this.pixlrToolContentHandler = pixlrToolContentHandler;
    }

    public IPixlrSessionDAO getPixlrSessionDAO() {
	return pixlrSessionDAO;
    }

    public void setPixlrSessionDAO(IPixlrSessionDAO sessionDAO) {
	pixlrSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IPixlrUserDAO getPixlrUserDAO() {
	return pixlrUserDAO;
    }

    public void setPixlrUserDAO(IPixlrUserDAO userDAO) {
	pixlrUserDAO = userDAO;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public PixlrOutputFactory getPixlrOutputFactory() {
	return pixlrOutputFactory;
    }

    public void setPixlrOutputFactory(PixlrOutputFactory pixlrOutputFactory) {
	this.pixlrOutputFactory = pixlrOutputFactory;
    }

    public IPixlrConfigItemDAO getPixlrConfigItemDAO() {
	return pixlrConfigItemDAO;
    }

    public void setPixlrConfigItemDAO(IPixlrConfigItemDAO pixlrConfigItemDAO) {
	this.pixlrConfigItemDAO = pixlrConfigItemDAO;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getPixlrOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	// db doesn't have a start/finish date for learner, and session start/finish is null
	PixlrUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isFinishedActivity()
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }
}