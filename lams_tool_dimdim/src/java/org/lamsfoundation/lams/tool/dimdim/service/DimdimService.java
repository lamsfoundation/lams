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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.lamsfoundation.lams.tool.dimdim.dao.IDimdimAttachmentDAO;
import org.lamsfoundation.lams.tool.dimdim.dao.IDimdimConfigDAO;
import org.lamsfoundation.lams.tool.dimdim.dao.IDimdimDAO;
import org.lamsfoundation.lams.tool.dimdim.dao.IDimdimSessionDAO;
import org.lamsfoundation.lams.tool.dimdim.dao.IDimdimUserDAO;
import org.lamsfoundation.lams.tool.dimdim.model.Dimdim;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimAttachment;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimConfig;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimSession;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;
import org.lamsfoundation.lams.tool.dimdim.util.Constants;
import org.lamsfoundation.lams.tool.dimdim.util.DimdimException;
import org.lamsfoundation.lams.tool.dimdim.util.DimdimToolContentHandler;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the IDimdimService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class DimdimService implements ToolSessionManager, ToolContentManager, IDimdimService,
	ToolContentImport102Manager {

    private static final Logger logger = Logger.getLogger(DimdimService.class);

    private IDimdimDAO dimdimDAO = null;

    private IDimdimSessionDAO dimdimSessionDAO = null;

    private IDimdimUserDAO dimdimUserDAO = null;

    private IDimdimAttachmentDAO dimdimAttachmentDAO = null;

    private IDimdimConfigDAO dimdimConfigDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler dimdimToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    public DimdimService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* Methods from ToolSessionManager */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	DimdimSession session = new DimdimSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	// TODO need to also set other fields.
	Dimdim dimdim = getDimdimByContentId(toolContentId);
	session.setDimdim(dimdim);
	dimdimSessionDAO.insertOrUpdate(session);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    @SuppressWarnings("unchecked")
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	dimdimSessionDAO.deleteByProperty(DimdimSession.class, "sessionId", toolSessionId);
	// TODO check if cascade worked
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util .List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return new TreeMap<String, ToolOutput>();
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
    }

    /* Methods from ToolContentManager */

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (logger.isDebugEnabled()) {
	    logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
		    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Dimdim fromContent = null;
	if (fromContentId != null) {
	    fromContent = getDimdimByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Dimdim toContent = Dimdim.newInstance(fromContent, toContentId, dimdimToolContentHandler);
	saveOrUpdateDimdim(toContent);
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Dimdim dimdim = getDimdimByContentId(toolContentId);
	if (dimdim == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	dimdim.setDefineLater(value);
	saveOrUpdateDimdim(dimdim);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Dimdim dimdim = getDimdimByContentId(toolContentId);
	if (dimdim == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	dimdim.setRunOffline(value);
	saveOrUpdateDimdim(dimdim);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	// TODO Auto-generated method stub
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws DataMissingException
     *             if no tool content matches the toolSessionId
     * @throws ToolException
     *             if any other error occurs
     */

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Dimdim dimdim = getDimdimByContentId(toolContentId);
	if (dimdim == null) {
	    dimdim = getDefaultContent();
	}
	if (dimdim == null)
	    throw new DataMissingException("Unable to find default content for the dimdim tool");

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	dimdim = Dimdim.newInstance(dimdim, toolContentId, null);
	dimdim.setToolContentHandler(null);
	dimdim.setDimdimSessions(null);
	Set<DimdimAttachment> atts = dimdim.getDimdimAttachments();
	for (DimdimAttachment att : atts) {
	    att.setDimdim(null);
	}
	try {
	    exportContentService.registerFileClassForExport(DimdimAttachment.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, dimdim, dimdimToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    /**
     * Import the XML fragment for the tool's content, along with any files needed for the content.
     * 
     * @throws ToolException
     *             if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    exportContentService.registerFileClassForImport(DimdimAttachment.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null, null);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, dimdimToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Dimdim))
		throw new ImportToolContentException("Import Dimdim tool content failed. Deserialized object is "
			+ toolPOJO);
	    Dimdim dimdim = (Dimdim) toolPOJO;

	    // reset it to new toolContentId
	    dimdim.setToolContentId(toolContentId);
	    dimdim.setCreateBy(new Long(newUserUid.longValue()));

	    saveOrUpdateDimdim(dimdim);
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
	return new TreeMap<String, ToolOutputDefinition>();
    }

    /* IDimdimService Methods */

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {

	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if (list == null || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    public NotebookEntry getNotebookEntry(Long uid) {
	return coreNotebookService.getEntry(uid);
    }

    public void updateNotebookEntry(Long uid, String entry) {
	coreNotebookService.updateEntry(uid, "", entry);
    }

    public void updateNotebookEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    logger.error(error);
	    throw new DimdimException(error);
	}
	return toolContentId;
    }

    public Dimdim getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(Constants.TOOL_SIGNATURE);
	Dimdim defaultContent = getDimdimByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    logger.error(error);
	    throw new DimdimException(error);
	}
	return defaultContent;
    }

    public Dimdim copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Dimdim tools default content: + " + "newContentID is null";
	    logger.error(error);
	    throw new DimdimException(error);
	}

	Dimdim defaultContent = getDefaultContent();
	// create new dimdim using the newContentID
	Dimdim newContent = new Dimdim();
	newContent = Dimdim.newInstance(defaultContent, newContentID, dimdimToolContentHandler);
	saveOrUpdateDimdim(newContent);
	return newContent;
    }

    @SuppressWarnings("unchecked")
    public Dimdim getDimdimByContentId(Long toolContentID) {
	List<Dimdim> list = dimdimDAO.findByProperty(Dimdim.class, "toolContentId", toolContentID);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @SuppressWarnings("unchecked")
    public DimdimSession getSessionBySessionId(Long toolSessionId) {
	List<DimdimSession> list = dimdimSessionDAO.findByProperty(DimdimSession.class, "sessionId", toolSessionId);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    public DimdimUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	// TODO fix this
	return dimdimUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    @SuppressWarnings("unchecked")
    public DimdimUser getUserByUID(Long uid) {
	List<DimdimUser> list = dimdimUserDAO.findByProperty(DimdimUser.class, "uid", uid);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}

    }

    public DimdimAttachment uploadFileToContent(Long toolContentId, FormFile file, String type) {
	if (file == null || StringUtils.isEmpty(file.getFileName()))
	    throw new DimdimException("Could not find upload file: " + file);

	NodeKey nodeKey = processFile(file, type);

	DimdimAttachment attachment = new DimdimAttachment(nodeKey.getVersion(), type, file.getFileName(), nodeKey
		.getUuid(), new Date());
	return attachment;
    }

    public void deleteFromRepository(Long uuid, Long versionID) throws DimdimException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new DimdimException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    /**
     * 
     * @param url
     * @return
     */
    private String sendDimdimRequest(URL url) throws Exception {
	URLConnection connection = url.openConnection();

	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	String dimdimResponse = "";
	String line = "";

	while ((line = in.readLine()) != null)
	    dimdimResponse += line;
	in.close();

	logger.debug(dimdimResponse + "1");

	// Extract the connect url from the json string.
	Pattern pattern = Pattern.compile("url:\"(.*?)\"");
	Matcher matcher = pattern.matcher(dimdimResponse);

	matcher.find();
	String connectURL = matcher.group(1);

	return connectURL;
    }

    public String getDimdimJoinConferenceURL(UserDTO userDTO, String meetingKey) throws Exception {

	// Get Dimdim server url
	DimdimConfig serverURL = getConfigEntry(Constants.CONFIG_SERVER_URL);
	if (serverURL == null) {
	    throw new DimdimException("Dimdim server url not found");
	}

	URL url = new URL(serverURL.getValue() + "/dimdim/JoinConferenceCheck.action?" + "email="
		+ URLEncoder.encode(userDTO.getEmail(), "UTF8") + "&displayName="
		+ URLEncoder.encode(userDTO.getFirstName() + " " + userDTO.getLastName(), "UTF8") + "&confKey="
		+ URLEncoder.encode(meetingKey, "UTF8"));

	String connectURL = sendDimdimRequest(url);

	return serverURL.getValue() + connectURL;
    }

    public String getDimdimStartConferenceURL(UserDTO userDTO, String meetingKey) throws Exception {

	// Get Dimdim server url
	DimdimConfig serverURL = getConfigEntry(Constants.CONFIG_SERVER_URL);
	if (serverURL == null) {
	    logger.error("Dimdim server URL is null, configure using dimdim tool management");
	    throw new DimdimException("Dimdim server url not found");
	}

	// get dimdim url

	URL url = new URL(serverURL.getValue() + "/dimdim/StartNewConferenceCheck.action?" + "email="
		+ URLEncoder.encode(userDTO.getEmail(), "UTF8") + "&displayName="
		+ URLEncoder.encode(userDTO.getFirstName() + " " + userDTO.getLastName(), "UTF8") + "&confKey="
		+ URLEncoder.encode(meetingKey, "UTF8") + "&lobby=false" + "&networkProfile=3" + "&meetingHours=99"
		+ "&maxAttendeeMikes=0" + "&returnUrl=asdf" + "&presenterAV=av" + "&privateChatEnabled=true"
		+ "&publicChatEnabled=true" + "&screenShareEnabled=true" + "&whiteboardEnabled=true");

	String connectURL = sendDimdimRequest(url);

	return serverURL.getValue() + connectURL;

    }

    public void saveOrUpdateDimdim(Dimdim dimdim) {
	dimdimDAO.insertOrUpdate(dimdim);
    }

    public void saveOrUpdateDimdimSession(DimdimSession dimdimSession) {
	dimdimSessionDAO.insertOrUpdate(dimdimSession);
    }

    public void saveOrUpdateDimdimUser(DimdimUser dimdimUser) {
	dimdimUserDAO.insertOrUpdate(dimdimUser);
    }

    public DimdimUser createDimdimUser(UserDTO user, DimdimSession dimdimSession) {
	DimdimUser dimdimUser = new DimdimUser(user, dimdimSession);
	saveOrUpdateDimdimUser(dimdimUser);
	return dimdimUser;
    }

    @SuppressWarnings("unchecked")
    public DimdimConfig getConfigEntry(String key) {
	dimdimConfigDAO.findByProperty(DimdimConfig.class, "key", key);
	List<DimdimConfig> list = (List<DimdimConfig>) dimdimConfigDAO.findByProperty(DimdimConfig.class, "key", key);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    public void saveOrUpdateConfigEntry(DimdimConfig dimdimConfig) {
	dimdimConfigDAO.insertOrUpdate(dimdimConfig);
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
		node = getDimdimToolContentHandler().uploadFile(file.getInputStream(), fileName, file.getContentType(),
			type);
	    } catch (InvalidParameterException e) {
		throw new DimdimException("InvalidParameterException occured while trying to upload File"
			+ e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new DimdimException("FileNotFoundException occured while trying to upload File" + e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new DimdimException("RepositoryCheckedException occured while trying to upload File"
			+ e.getMessage());
	    } catch (IOException e) {
		throw new DimdimException("IOException occured while trying to upload File" + e.getMessage());
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
    private ITicket getRepositoryLoginTicket() throws DimdimException {
	repositoryService = RepositoryProxy.getRepositoryService();
	ICredentials credentials = new SimpleCredentials(DimdimToolContentHandler.repositoryUser,
		DimdimToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, DimdimToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new DimdimException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new DimdimException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new DimdimException("Login failed." + e.getMessage());
	}
    }

    /*
     * ===============Methods implemented from ToolContentImport102Manager ===============
     */

    /**
     * Import the data for a 1.0.2 Dimdim
     */
    @SuppressWarnings("unchecked")
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Dimdim dimdim = new Dimdim();
	dimdim.setContentInUse(Boolean.FALSE);
	dimdim.setCreateBy(new Long(user.getUserID().longValue()));
	dimdim.setCreateDate(now);
	dimdim.setDefineLater(Boolean.FALSE);
	dimdim.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	dimdim.setLockOnFinished(Boolean.TRUE);
	dimdim.setOfflineInstructions(null);
	dimdim.setOnlineInstructions(null);
	dimdim.setReflectInstructions(null);
	dimdim.setReflectOnActivity(Boolean.FALSE);
	dimdim.setRunOffline(Boolean.FALSE);
	dimdim.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	dimdim.setToolContentId(toolContentId);
	dimdim.setUpdateDate(now);
	// leave as empty, no need to set them to anything.
	// setDimdimAttachments(Set dimdimAttachments);
	// setDimdimSessions(Set dimdimSessions);
	saveOrUpdateDimdim(dimdim);
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	logger
		.warn("Setting the reflective field on a dimdim. This doesn't make sense as the dimdim is for reflection and we don't reflect on reflection!");
	Dimdim dimdim = getDimdimByContentId(toolContentId);
	if (dimdim == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	dimdim.setReflectOnActivity(Boolean.TRUE);
	dimdim.setReflectInstructions(description);
    }

    // ==========================================================================
    // ===============
    /* Used by Spring to "inject" the linked objects */

    public IDimdimAttachmentDAO getDimdimAttachmentDAO() {
	return dimdimAttachmentDAO;
    }

    public void setDimdimAttachmentDAO(IDimdimAttachmentDAO attachmentDAO) {
	this.dimdimAttachmentDAO = attachmentDAO;
    }

    public IDimdimDAO getDimdimDAO() {
	return dimdimDAO;
    }

    public void setDimdimDAO(IDimdimDAO dimdimDAO) {
	this.dimdimDAO = dimdimDAO;
    }

    public IToolContentHandler getDimdimToolContentHandler() {
	return dimdimToolContentHandler;
    }

    public void setDimdimToolContentHandler(IToolContentHandler dimdimToolContentHandler) {
	this.dimdimToolContentHandler = dimdimToolContentHandler;
    }

    public IDimdimSessionDAO getDimdimSessionDAO() {
	return dimdimSessionDAO;
    }

    public void setDimdimSessionDAO(IDimdimSessionDAO sessionDAO) {
	this.dimdimSessionDAO = sessionDAO;
    }

    public IDimdimConfigDAO getDimdimConfigDAO() {
	return dimdimConfigDAO;
    }

    public void setDimdimConfigDAO(IDimdimConfigDAO dimdimConfigDAO) {
	this.dimdimConfigDAO = dimdimConfigDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IDimdimUserDAO getDimdimUserDAO() {
	return dimdimUserDAO;
    }

    public void setDimdimUserDAO(IDimdimUserDAO userDAO) {
	this.dimdimUserDAO = userDAO;
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
}
