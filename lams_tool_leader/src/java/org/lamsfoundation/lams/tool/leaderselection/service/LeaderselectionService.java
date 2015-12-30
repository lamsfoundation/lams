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

package org.lamsfoundation.lams.tool.leaderselection.service;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.LoginException;
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
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.leaderselection.dao.ILeaderselectionDAO;
import org.lamsfoundation.lams.tool.leaderselection.dao.ILeaderselectionSessionDAO;
import org.lamsfoundation.lams.tool.leaderselection.dao.ILeaderselectionUserDAO;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionException;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionToolContentHandler;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * An implementation of the ILeaderselectionService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class LeaderselectionService implements ToolSessionManager, ToolContentManager, ILeaderselectionService,
	ToolContentImport102Manager, ToolRestManager {

    private static Logger logger = Logger.getLogger(LeaderselectionService.class.getName());

    private ILeaderselectionDAO leaderselectionDAO = null;

    private ILeaderselectionSessionDAO leaderselectionSessionDAO = null;

    private ILeaderselectionUserDAO leaderselectionUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler leaderselectionToolContentHandler = null;

    private IRepositoryService repositoryService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private LeaderselectionOutputFactory leaderselectionOutputFactory;

    public LeaderselectionService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (LeaderselectionService.logger.isDebugEnabled()) {
	    LeaderselectionService.logger.debug("entering method createToolSession:" + " toolSessionId = "
		    + toolSessionId + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
	}

	LeaderselectionSession session = new LeaderselectionSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	// learner starts
	// TODO need to also set other fields.
	Leaderselection leaderselection = leaderselectionDAO.getByContentId(toolContentId);
	session.setLeaderselection(leaderselection);
	leaderselectionSessionDAO.saveOrUpdate(session);
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
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	leaderselectionSessionDAO.deleteBySessionID(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getLeaderselectionOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getLeaderselectionOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    /* ************ Methods from ToolContentManager ************************* */
    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (LeaderselectionService.logger.isDebugEnabled()) {
	    LeaderselectionService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Leaderselection fromContent = null;
	if (fromContentId != null) {
	    fromContent = leaderselectionDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Leaderselection toContent = Leaderselection.newInstance(fromContent, toContentId,
		leaderselectionToolContentHandler);
	leaderselectionDAO.saveOrUpdate(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Leaderselection content = leaderselectionDAO.getByContentId(toolContentId);
	if (content == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	content.setDefineLater(false);
	leaderselectionDAO.saveOrUpdate(content);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Leaderselection content = leaderselectionDAO.getByContentId(toolContentId);
	if (content == null) {
	    LeaderselectionService.logger
		    .warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}
	for (LeaderselectionSession session : (Set<LeaderselectionSession>) content.getLeaderselectionSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, LeaderselectionConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	leaderselectionDAO.delete(content);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (LeaderselectionService.logger.isDebugEnabled()) {
	    LeaderselectionService.logger.debug(
		    "Removing Leader Selection state for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Leaderselection selection = leaderselectionDAO.getByContentId(toolContentId);
	if (selection == null) {
	    return;
	}

	for (LeaderselectionSession session : (Set<LeaderselectionSession>) selection.getLeaderselectionSessions()) {
	    /*
	    if (session.getGroupLeader() != null && session.getGroupLeader().getUserId().equals(userId.longValue())) {
	    session.setGroupLeader(null);
	    leaderselectionSessionDAO.update(session);
	    }
	    */

	    LeaderselectionUser user = leaderselectionUserDAO.getByUserIdAndSessionId(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		user.setFinishedActivity(false);
		leaderselectionDAO.update(user);
	    }
	}
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Leaderselection content = leaderselectionDAO.getByContentId(toolContentId);
	if (content == null) {
	    content = getDefaultContent();
	}
	if (content == null) {
	    throw new DataMissingException("Unable to find default content for the leaderselection tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	content = Leaderselection.newInstance(content, toolContentId, null);
	content.setLeaderselectionSessions(null);
	try {
	    exportContentService.exportToolContent(toolContentId, content, leaderselectionToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(LeaderselectionImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, leaderselectionToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Leaderselection)) {
		throw new ImportToolContentException(
			"Import Leaderselection tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Leaderselection content = (Leaderselection) toolPOJO;

	    // reset it to new toolContentId
	    content.setToolContentId(toolContentId);
	    content.setCreateBy(new Long(newUserUid.longValue()));

	    leaderselectionDAO.saveOrUpdate(content);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Leaderselection content = getLeaderselectionDAO().getByContentId(toolContentId);
	if (content == null) {
	    content = getDefaultContent();
	}
	return getLeaderselectionOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getContentByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getContentByContentId(toolContentId).isDefineLater();
    }

    /* ********** ILeaderselectionService Methods ********************************* */

    @Override
    public void setGroupLeader(Long userUid, Long toolSessionId) {
	if ((userUid == null) || (toolSessionId == null)) {
	    return;
	}

	LeaderselectionSession session = getSessionBySessionId(toolSessionId);
	LeaderselectionUser newLeader = getUserByUID(userUid);
	if ((session == null) || (newLeader == null)) {
	    LeaderselectionService.logger
		    .error("Wrong parameters supplied. SessionId=" + toolSessionId + " UserId=" + userUid);
	    return;
	}

	session.setGroupLeader(newLeader);
	saveOrUpdateSession(session);
    }

    @Override
    public boolean isUserLeader(Long userId, Long toolSessionId) {
	if ((userId == null) || (toolSessionId == null)) {
	    throw new LeaderselectionException("Wrong parameters supplied: userId or toolSessionId is null. SessionId="
		    + toolSessionId + " UserId=" + userId);
	}

	LeaderselectionSession session = getSessionBySessionId(toolSessionId);
	LeaderselectionUser groupLeader = session.getGroupLeader();
	boolean isUserLeader = (groupLeader != null) && groupLeader.getUserId().equals(userId);
	return isUserLeader;
    }

    @Override
    public List<LeaderselectionUser> getUsersBySession(Long toolSessionId) {
	return leaderselectionUserDAO.getBySessionId(toolSessionId);
    }

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
	    LeaderselectionService.logger.error(error);
	    throw new LeaderselectionException(error);
	}
	return toolContentId;
    }

    @Override
    public Leaderselection getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(LeaderselectionConstants.TOOL_SIGNATURE);
	Leaderselection defaultContent = getContentByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    LeaderselectionService.logger.error(error);
	    throw new LeaderselectionException(error);
	}
	return defaultContent;
    }

    @Override
    public Leaderselection copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Leaderselection tools default content: + " + "newContentID is null";
	    LeaderselectionService.logger.error(error);
	    throw new LeaderselectionException(error);
	}

	Leaderselection defaultContent = getDefaultContent();
	// create new leaderselection using the newContentID
	Leaderselection newContent = new Leaderselection();
	newContent = Leaderselection.newInstance(defaultContent, newContentID, leaderselectionToolContentHandler);
	leaderselectionDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Leaderselection getContentByContentId(Long toolContentID) {
	Leaderselection leaderselection = leaderselectionDAO.getByContentId(toolContentID);
	if (leaderselection == null) {
	    LeaderselectionService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return leaderselection;
    }

    @Override
    public LeaderselectionSession getSessionBySessionId(Long toolSessionId) {
	LeaderselectionSession leaderselectionSession = leaderselectionSessionDAO.getBySessionId(toolSessionId);
	if (leaderselectionSession == null) {
	    LeaderselectionService.logger
		    .debug("Could not find the leaderselection session with toolSessionID:" + toolSessionId);
	}
	return leaderselectionSession;
    }

    @Override
    public LeaderselectionUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return leaderselectionUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    @Override
    public LeaderselectionUser getUserByUID(Long uid) {
	return leaderselectionUserDAO.getByUID(uid);
    }

    @Override
    public void saveOrUpdateLeaderselection(Leaderselection leaderselection) {
	leaderselectionDAO.saveOrUpdate(leaderselection);
    }

    @Override
    public void saveOrUpdateSession(LeaderselectionSession leaderselectionSession) {
	leaderselectionSessionDAO.saveOrUpdate(leaderselectionSession);
    }

    @Override
    public void saveOrUpdateUser(LeaderselectionUser leaderselectionUser) {
	leaderselectionUserDAO.saveOrUpdate(leaderselectionUser);
    }

    @Override
    public LeaderselectionUser createLeaderselectionUser(UserDTO user, LeaderselectionSession leaderselectionSession) {
	LeaderselectionUser leaderselectionUser = new LeaderselectionUser(user, leaderselectionSession);
	saveOrUpdateUser(leaderselectionUser);
	return leaderselectionUser;
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
    private ITicket getRepositoryLoginTicket() throws LeaderselectionException {
	ICredentials credentials = new SimpleCredentials(LeaderselectionToolContentHandler.repositoryUser,
		LeaderselectionToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials,
		    LeaderselectionToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new LeaderselectionException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new LeaderselectionException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new LeaderselectionException("Login failed." + e.getMessage());
	}
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    @Override
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
    }

    @Override
    public void setReflectiveData(Long toolContentId, String title, String description)
	    throws ToolException, DataMissingException {

	Leaderselection leaderselection = getContentByContentId(toolContentId);
	if (leaderselection == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	leaderselection.setInstructions(description);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public ILeaderselectionDAO getLeaderselectionDAO() {
	return leaderselectionDAO;
    }

    public void setLeaderselectionDAO(ILeaderselectionDAO leaderselectionDAO) {
	this.leaderselectionDAO = leaderselectionDAO;
    }

    public IToolContentHandler getLeaderselectionToolContentHandler() {
	return leaderselectionToolContentHandler;
    }

    public void setLeaderselectionToolContentHandler(IToolContentHandler leaderselectionToolContentHandler) {
	this.leaderselectionToolContentHandler = leaderselectionToolContentHandler;
    }

    public ILeaderselectionSessionDAO getLeaderselectionSessionDAO() {
	return leaderselectionSessionDAO;
    }

    public void setLeaderselectionSessionDAO(ILeaderselectionSessionDAO sessionDAO) {
	leaderselectionSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public ILeaderselectionUserDAO getLeaderselectionUserDAO() {
	return leaderselectionUserDAO;
    }

    public void setLeaderselectionUserDAO(ILeaderselectionUserDAO userDAO) {
	leaderselectionUserDAO = userDAO;
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

    public IRepositoryService getRepositoryService() {
	return repositoryService;
    }

    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    public LeaderselectionOutputFactory getLeaderselectionOutputFactory() {
	return leaderselectionOutputFactory;
    }

    public void setLeaderselectionOutputFactory(LeaderselectionOutputFactory leaderselectionOutputFactory) {
	this.leaderselectionOutputFactory = leaderselectionOutputFactory;
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getLeaderselectionOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    // ****************** REST methods *************************

    /**
     * Rest call to create a new Learner Selection content. Required fields in toolContentJSON: "title", "instructions".
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {
	Date updateDate = new Date();

	Leaderselection leaderselection = new Leaderselection();
	leaderselection.setToolContentId(toolContentID);
	leaderselection.setTitle(toolContentJSON.getString(RestTags.TITLE));
	leaderselection.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));
	leaderselection.setCreateBy(userID.longValue());
	leaderselection.setCreateDate(updateDate);
	leaderselection.setUpdateDate(updateDate);
	leaderselection.setContentInUse(false);
	leaderselection.setDefineLater(false);
	saveOrUpdateLeaderselection(leaderselection);
    }

}
