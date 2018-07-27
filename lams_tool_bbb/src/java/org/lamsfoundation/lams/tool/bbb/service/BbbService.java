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


package org.lamsfoundation.lams.tool.bbb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.bbb.dao.IBbbConfigDAO;
import org.lamsfoundation.lams.tool.bbb.dao.IBbbDAO;
import org.lamsfoundation.lams.tool.bbb.dao.IBbbSessionDAO;
import org.lamsfoundation.lams.tool.bbb.dao.IBbbUserDAO;
import org.lamsfoundation.lams.tool.bbb.model.Bbb;
import org.lamsfoundation.lams.tool.bbb.model.BbbConfig;
import org.lamsfoundation.lams.tool.bbb.model.BbbSession;
import org.lamsfoundation.lams.tool.bbb.model.BbbUser;
import org.lamsfoundation.lams.tool.bbb.util.BbbException;
import org.lamsfoundation.lams.tool.bbb.util.BbbUtil;
import org.lamsfoundation.lams.tool.bbb.util.Constants;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * An implementation of the IBbbService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class BbbService implements ToolSessionManager, ToolContentManager, IBbbService {

    private static final Logger logger = Logger.getLogger(BbbService.class);

    private IBbbDAO bbbDAO = null;

    private IBbbSessionDAO bbbSessionDAO = null;

    private IBbbUserDAO bbbUserDAO = null;

    private IBbbConfigDAO bbbConfigDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler bbbToolContentHandler = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    public BbbService() {
	super();
	// TODO Auto-generated constructor stub
    }

    /* Methods from ToolSessionManager */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (BbbService.logger.isDebugEnabled()) {
	    BbbService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	BbbSession session = new BbbSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	Bbb bbb = getBbbByContentId(toolContentId);
	session.setBbb(bbb);
	bbbSessionDAO.insertOrUpdate(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	bbbSessionDAO.deleteByProperty(BbbSession.class, "sessionId", toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return new TreeMap<String, ToolOutput>();
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
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
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    /* Methods from ToolContentManager */

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (BbbService.logger.isDebugEnabled()) {
	    BbbService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Bbb fromContent = null;
	if (fromContentId != null) {
	    fromContent = getBbbByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Bbb toContent = Bbb.newInstance(fromContent, toContentId, bbbToolContentHandler);
	saveOrUpdateBbb(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Bbb bbb = getBbbByContentId(toolContentId);
	if (bbb == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	bbb.setDefineLater(false);
	saveOrUpdateBbb(bbb);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Bbb bbb = getBbbByContentId(toolContentId);
	if (bbb == null) {
	    BbbService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (BbbSession session : bbb.getBbbSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, Constants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	bbbDAO.delete(bbb);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (BbbService.logger.isDebugEnabled()) {
	    BbbService.logger.debug("Resetting Web Conference completion flag for user ID " + userId
		    + " and toolContentId " + toolContentId);
	}

	Bbb bbb = getBbbByContentId(toolContentId);
	if (bbb == null) {
	    BbbService.logger
		    .warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (BbbSession session : bbb.getBbbSessions()) {
	    for (BbbUser user : session.getBbbUsers()) {
		if (user.getUserId().equals(userId.longValue())) {
		    if (user.getNotebookEntryUID() != null) {
			NotebookEntry entry = coreNotebookService.getEntry(user.getNotebookEntryUID());
			bbbDAO.delete(entry);
			user.setNotebookEntryUID(null);
		    }
		    user.setFinishedActivity(false);
		    bbbUserDAO.update(user);
		}
	    }
	}
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws DataMissingException
     *             if no tool content matches the toolSessionId
     * @throws ToolException
     *             if any other error occurs
     */

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Bbb bbb = getBbbByContentId(toolContentId);
	if (bbb == null) {
	    bbb = getDefaultContent();
	}
	if (bbb == null) {
	    throw new DataMissingException("Unable to find default content for the bbb tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	bbb = Bbb.newInstance(bbb, toolContentId, null);
	bbb.setToolContentHandler(null);
	bbb.setBbbSessions(null);
	try {
	    exportContentService.exportToolContent(toolContentId, bbb, bbbToolContentHandler, rootPath);
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
    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(BbbImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, bbbToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Bbb)) {
		throw new ImportToolContentException(
			"Import Bbb tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Bbb bbb = (Bbb) toolPOJO;

	    // reset it to new toolContentId
	    bbb.setToolContentId(toolContentId);
	    bbb.setCreateBy(new Long(newUserUid.longValue()));

	    saveOrUpdateBbb(bbb);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
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
	return new TreeMap<String, ToolOutputDefinition>();
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getBbbByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getBbbByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Bbb content = getBbbByContentId(toolContentId);
	for (BbbSession session : content.getBbbSessions()) {
	    for (BbbUser user : session.getBbbUsers()) {
		if (user.getNotebookEntryUID() != null) {
		    // we don't remove users in removeLearnerContent()
		    // we just set their notebook entry to NULL
		    return true;
		}
	    }
	}

	return false;
    }

    /* IBbbService Methods */

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {

	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public NotebookEntry getNotebookEntry(Long uid) {
	return coreNotebookService.getEntry(uid);
    }

    @Override
    public void updateNotebookEntry(Long uid, String entry) {
	coreNotebookService.updateEntry(uid, "", entry);
    }

    @Override
    public void updateNotebookEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    BbbService.logger.error(error);
	    throw new BbbException(error);
	}
	return toolContentId;
    }

    @Override
    public Bbb getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(Constants.TOOL_SIGNATURE);
	Bbb defaultContent = getBbbByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    BbbService.logger.error(error);
	    throw new BbbException(error);
	}
	return defaultContent;
    }

    @Override
    public Bbb copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Bbb tools default content: + " + "newContentID is null";
	    BbbService.logger.error(error);
	    throw new BbbException(error);
	}

	Bbb defaultContent = getDefaultContent();
	// create new bbb using the newContentID
	Bbb newContent = new Bbb();
	newContent = Bbb.newInstance(defaultContent, newContentID, bbbToolContentHandler);
	saveOrUpdateBbb(newContent);
	return newContent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Bbb getBbbByContentId(Long toolContentID) {
	List<Bbb> list = bbbDAO.findByProperty(Bbb.class, "toolContentId", toolContentID);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
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
    @SuppressWarnings("unchecked")
    public BbbSession getSessionBySessionId(Long toolSessionId) {
	List<BbbSession> list = bbbSessionDAO.findByProperty(BbbSession.class, "sessionId", toolSessionId);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public BbbUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("userId", userId);
	map.put("bbbSession.sessionId", toolSessionId);
	List<BbbUser> list = bbbUserDAO.findByProperties(BbbUser.class, map);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public BbbUser getUserByUID(Long uid) {
	List<BbbUser> list = bbbUserDAO.findByProperty(BbbUser.class, "uid", uid);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public String getJoinMeetingURL(UserDTO userDTO, String meetingKey, String password) throws Exception {

	// Get Bbb details
	String serverURL = getConfigValue(Constants.CFG_SERVER_URL);
	String securitySalt = getConfigValue(Constants.CFG_SECURITYSALT);
	// Get Join parameter
	String joinParam = Constants.BBB_JOIN_PARAM;

	if (serverURL == null) {
	    BbbService.logger.error("Config item : '" + Constants.CFG_SERVER_URL + "' not defined");
	    throw new BbbException("Server url not defined");
	}

	String queryString = "fullName="
		+ URLEncoder.encode(userDTO.getFirstName() + " " + userDTO.getLastName(), "UTF8") + "&meetingID="
		+ URLEncoder.encode(meetingKey, "UTF8") + "&password=" + URLEncoder.encode(password, "UTF8");

	String checkSum = DigestUtils.shaHex("join" + queryString + securitySalt);

	String url = serverURL + joinParam + queryString + "&checksum=" + checkSum;

	return url;
    }

    @Override
    public Boolean isMeetingRunning(String meetingKey) throws Exception {
	String serverURL = getConfigValue(Constants.CFG_SERVER_URL);
	String securitySalt = getConfigValue(Constants.CFG_SECURITYSALT);
	String meetingRunning = Constants.BBB_MEETING_RUNNING_PARAM;

	String queryString = "meetingID=" + URLEncoder.encode(meetingKey, "UTF8");

	String checkSum = DigestUtils.shaHex("isMeetingRunning" + queryString + securitySalt);

	URL url;
	url = new URL(serverURL + meetingRunning + queryString + "&checksum=" + URLEncoder.encode(checkSum, "UTF8"));

	BbbService.logger.debug("isMeetingRunningURL=" + url);

	String response;
	response = sendRequest(url);

	if (response.contains("true")) {
	    return true;
	} else {
	    return false;
	}

    }

    @Override
    public String startConference(String meetingKey, String atendeePassword, String moderatorPassword, String returnURL,
	    String welcomeMessage) throws Exception {

	String serverURL = getConfigValue(Constants.CFG_SERVER_URL);
	String securitySalt = getConfigValue(Constants.CFG_SECURITYSALT);
	String createParam = Constants.BBB_CREATE_PARAM;

	if (serverURL == null) {
	    BbbService.logger.error("Config item : '" + Constants.CFG_SERVER_URL + "' not defined");
	    throw new BbbException("Standard server url not defined");
	}

	String queryString = "name=" + URLEncoder.encode(meetingKey, "UTF8") + "&meetingID="
		+ URLEncoder.encode(meetingKey, "UTF8") + "&attendeePW=" + URLEncoder.encode(atendeePassword, "UTF8")
		+ "&moderatorPW=" + URLEncoder.encode(moderatorPassword, "UTF8") + "&logoutURL="
		+ URLEncoder.encode(returnURL, "UTF8") + "&welcome=" + URLEncoder.encode(welcomeMessage, "UTF8");

	BbbService.logger.debug("queryString = " + queryString);

	String checkSum = DigestUtils.shaHex("create" + queryString + securitySalt);

	BbbService.logger.debug("checksum = " + checkSum);

	URL url;
	url = new URL(serverURL + createParam + queryString + "&checksum=" + URLEncoder.encode(checkSum, "UTF8"));

	BbbService.logger.info("url = " + url);

	String response;
	response = sendRequest(url);

	if (BbbUtil.getResponse(response) == Constants.RESPONSE_SUCCESS) {
	    return Constants.RESPONSE_SUCCESS;
	} else {
	    BbbService.logger.error("BBB returns fail when creating a meeting room");
	    throw new BbbException("Standard server url not defined");

	}
    }

    @Override
    public void saveOrUpdateBbb(Bbb bbb) {
	bbbDAO.insertOrUpdate(bbb);
    }

    @Override
    public void saveOrUpdateBbbSession(BbbSession bbbSession) {
	bbbSessionDAO.insertOrUpdate(bbbSession);
    }

    @Override
    public void saveOrUpdateBbbUser(BbbUser bbbUser) {
	bbbUserDAO.insertOrUpdate(bbbUser);
    }

    @Override
    public BbbUser createBbbUser(UserDTO user, BbbSession bbbSession) {
	BbbUser bbbUser = new BbbUser(user, bbbSession);
	saveOrUpdateBbbUser(bbbUser);
	return bbbUser;
    }

    @Override
    @SuppressWarnings("unchecked")
    public BbbConfig getConfig(String key) {
	List<BbbConfig> list = bbbConfigDAO.findByProperty(BbbConfig.class, "key", key);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getConfigValue(String key) {
	List<BbbConfig> list = bbbConfigDAO.findByProperty(BbbConfig.class, "key", key);
	if (list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0).getValue();
	}
    }

    @Override
    public void saveOrUpdateConfigEntry(BbbConfig bbbConfig) {
	bbbConfigDAO.insertOrUpdate(bbbConfig);
    }

    private String sendRequest(URL url) throws IOException {

	if (BbbService.logger.isDebugEnabled()) {
	    BbbService.logger.debug("request = " + url);
	}

	URLConnection connection = url.openConnection();

	BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	String response = "";
	String line = "";

	while ((line = in.readLine()) != null) {
	    response += line;
	}
	in.close();

	if (BbbService.logger.isDebugEnabled()) {
	    BbbService.logger.debug("response = " + response);
	}

	return response;
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description)
	    throws ToolException, DataMissingException {

	BbbService.logger.warn(
		"Setting the reflective field on a bbb. This doesn't make sense as the bbb is for reflection and we don't reflect on reflection!");
	Bbb bbb = getBbbByContentId(toolContentId);
	if (bbb == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	bbb.setReflectOnActivity(Boolean.TRUE);
	bbb.setReflectInstructions(description);
    }

    // =========================================================================================
    /* Used by Spring to "inject" the linked objects */

    public IBbbDAO getBbbDAO() {
	return bbbDAO;
    }

    public void setBbbDAO(IBbbDAO bbbDAO) {
	this.bbbDAO = bbbDAO;
    }

    public IToolContentHandler getBbbToolContentHandler() {
	return bbbToolContentHandler;
    }

    public void setBbbToolContentHandler(IToolContentHandler bbbToolContentHandler) {
	this.bbbToolContentHandler = bbbToolContentHandler;
    }

    public IBbbSessionDAO getBbbSessionDAO() {
	return bbbSessionDAO;
    }

    public void setBbbSessionDAO(IBbbSessionDAO sessionDAO) {
	this.bbbSessionDAO = sessionDAO;
    }

    public IBbbConfigDAO getBbbConfigDAO() {
	return bbbConfigDAO;
    }

    public void setBbbConfigDAO(IBbbConfigDAO bbbConfigDAO) {
	this.bbbConfigDAO = bbbConfigDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IBbbUserDAO getBbbUserDAO() {
	return bbbUserDAO;
    }

    public void setBbbUserDAO(IBbbUserDAO userDAO) {
	this.bbbUserDAO = userDAO;
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

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	BbbUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isFinishedActivity() ? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
      }
}
