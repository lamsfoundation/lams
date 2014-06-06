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

package org.lamsfoundation.lams.tool.pixlr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
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
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
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
import org.lamsfoundation.lams.tool.pixlr.util.PixlrToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IPixlrService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class PixlrService implements ToolSessionManager, ToolContentManager, IPixlrService, ToolContentImport102Manager {

    static Logger logger = Logger.getLogger(PixlrService.class.getName());

    public static final String EXPORT_IMAGE_FILE_NAME = "authorImage";

    private IPixlrDAO pixlrDAO = null;

    private IPixlrSessionDAO pixlrSessionDAO = null;

    private IPixlrUserDAO pixlrUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler pixlrToolContentHandler = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private PixlrOutputFactory pixlrOutputFactory;

    private IPixlrConfigItemDAO pixlrConfigItemDAO;

    public PixlrService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (PixlrService.logger.isDebugEnabled()) {
	    PixlrService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
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

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    @SuppressWarnings("unchecked")
    public ToolSessionExportOutputData exportToolSession(List ToolSessionIds) throws DataMissingException,
	    ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	pixlrSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getPixlrOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getPixlrOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ************ Methods from ToolContentManager ************************* */

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (PixlrService.logger.isDebugEnabled()) {
	    PixlrService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
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

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }

    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Removing Pixlr image for user ID " + userId + " and toolContentId " + toolContentId);

	    Pixlr pixlr = pixlrDAO.getByContentId(toolContentId);
	    if (pixlr != null) {
		for (PixlrSession session : pixlr.getPixlrSessions()) {
		    PixlrUser user = pixlrUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
		    NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			    PixlrConstants.TOOL_SIGNATURE, userId);
		    if (entry != null) {
			pixlrDAO.delete(entry);
		    }

		    pixlrUserDAO.delete(user);
		}
	    }
	}
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
		File imageFile = new File(PixlrConstants.LAMS_PIXLR_BASE_DIR + File.separator
			+ pixlr.getImageFileName());
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
     *                 if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(PixlrImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, pixlrToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Pixlr)) {
		throw new ImportToolContentException("Import Pixlr tool content failed. Deserialized object is "
			+ toolPOJO);
	    }
	    Pixlr pixlr = (Pixlr) toolPOJO;

	    // reset it to new toolContentId
	    pixlr.setToolContentId(toolContentId);
	    pixlr.setCreateBy(new Long(newUserUid.longValue()));

	    // Copying the image file into lams_www.war/images/pixlr
	    File imageFile = new File(toolContentPath + File.separator + pixlr.getImageFileName());

	    if (imageFile.exists() && imageFile.canRead()) {

		String newFileName = FileUtil.generateUniqueContentFolderID()
			+ getFileExtension(pixlr.getImageFileName());

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

    public String getFileExtension(String fileName) {
	String ext = "";
	int i = fileName.lastIndexOf('.');
	if (i > 0 && i < fileName.length() - 1) {
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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Pixlr pixlr = getPixlrDAO().getByContentId(toolContentId);
	if (pixlr == null) {
	    pixlr = getDefaultContent();
	}
	return getPixlrOutputFactory().getToolOutputDefinitions(pixlr, definitionType);
    }
    
    public String getToolContentTitle(Long toolContentId) {
	return getPixlrByContentId(toolContentId).getTitle();
    }
    
    public boolean isContentEdited(Long toolContentId) {
	return getPixlrByContentId(toolContentId).isDefineLater();
    }

    /* ********** IPixlrService Methods ********************************* */

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

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

    public Pixlr getPixlrByContentId(Long toolContentID) {
	Pixlr pixlr = pixlrDAO.getByContentId(toolContentID);
	if (pixlr == null) {
	    PixlrService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return pixlr;
    }

    public PixlrSession getSessionBySessionId(Long toolSessionId) {
	PixlrSession pixlrSession = pixlrSessionDAO.getBySessionId(toolSessionId);
	if (pixlrSession == null) {
	    PixlrService.logger.debug("Could not find the pixlr session with toolSessionID:" + toolSessionId);
	}
	return pixlrSession;
    }

    public PixlrUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return pixlrUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public PixlrUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return pixlrUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public PixlrUser getUserByUID(Long uid) {
	return pixlrUserDAO.getByUID(uid);
    }

    public void saveOrUpdatePixlr(Pixlr pixlr) {
	pixlrDAO.saveOrUpdate(pixlr);
    }

    public void saveOrUpdatePixlrSession(PixlrSession pixlrSession) {
	pixlrSessionDAO.saveOrUpdate(pixlrSession);
    }

    public void saveOrUpdatePixlrUser(PixlrUser pixlrUser) {
	pixlrUserDAO.saveOrUpdate(pixlrUser);
    }

    public PixlrUser createPixlrUser(UserDTO user, PixlrSession pixlrSession) {
	PixlrUser pixlrUser = new PixlrUser(user, pixlrSession);
	saveOrUpdatePixlrUser(pixlrUser);
	return pixlrUser;
    }

    public IAuditService getAuditService() {
	return auditService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
    }
    
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    public PixlrConfigItem getConfigItem(String key) {
	return pixlrConfigItemDAO.getConfigItemByKey(key);
    }

    public void saveOrUpdatePixlrConfigItem(PixlrConfigItem item) {
	pixlrConfigItemDAO.saveOrUpdate(item);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Pixlr
     */
    @SuppressWarnings("unchecked")
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Pixlr pixlr = new Pixlr();
	pixlr.setContentInUse(Boolean.FALSE);
	pixlr.setCreateBy(new Long(user.getUserID().longValue()));
	pixlr.setCreateDate(now);
	pixlr.setDefineLater(Boolean.FALSE);
	pixlr.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	pixlr.setLockOnFinished(Boolean.TRUE);
	pixlr.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	pixlr.setToolContentId(toolContentId);
	pixlr.setUpdateDate(now);
	pixlr.setReflectOnActivity(Boolean.FALSE);
	// leave as empty, no need to set them to anything.
	// setPixlrAttachments(Set pixlrAttachments);
	// setPixlrSessions(Set pixlrSessions);
	pixlrDAO.saveOrUpdate(pixlr);
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	PixlrService.logger
		.warn("Setting the reflective field on a pixlr. This doesn't make sense as the pixlr is for reflection and we don't reflect on reflection!");
	Pixlr pixlr = getPixlrByContentId(toolContentId);
	if (pixlr == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	pixlr.setInstructions(description);
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

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getPixlrOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}
