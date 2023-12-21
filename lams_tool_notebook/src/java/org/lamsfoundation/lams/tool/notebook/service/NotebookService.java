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

package org.lamsfoundation.lams.tool.notebook.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookSessionDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookUserDAO;
import org.lamsfoundation.lams.tool.notebook.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;

/**
 * An implementation of the INotebookService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class NotebookService implements ToolSessionManager, ToolContentManager, INotebookService, ToolRestManager {

    private static Logger logger = Logger.getLogger(NotebookService.class.getName());

    private INotebookDAO notebookDAO = null;

    private INotebookSessionDAO notebookSessionDAO = null;

    private INotebookUserDAO notebookUserDAO = null;

    private ILamsToolService toolService;

    private IToolContentHandler notebookToolContentHandler = null;

    private ILogEventService logEventService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private IUserManagementService userManagementService;

    private MessageService messageService;

    private NotebookOutputFactory notebookOutputFactory;

    private Random generator = new Random();

    public NotebookService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */
    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (NotebookService.logger.isDebugEnabled()) {
	    NotebookService.logger.debug(
		    "entering method createToolSession:" + " toolSessionId = " + toolSessionId + " toolSessionName = "
			    + toolSessionName + " toolContentId = " + toolContentId);
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
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	notebookSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getNotebookOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getNotebookOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return new ArrayList<>();
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

	if (NotebookService.logger.isDebugEnabled()) {
	    NotebookService.logger.debug(
		    "entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
			    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Notebook fromContent = null;
	if (fromContentId != null) {
	    fromContent = notebookDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Notebook toContent = Notebook.newInstance(fromContent, toContentId);
	notebookDAO.saveOrUpdate(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Notebook notebook = notebookDAO.getByContentId(toolContentId);
	if (notebook == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	notebook.setDefineLater(false);
	notebookDAO.saveOrUpdate(notebook);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Notebook notebook = notebookDAO.getByContentId(toolContentId);
	if (notebook == null) {
	    NotebookService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (NotebookSession session : notebook.getNotebookSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, NotebookConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	notebookDAO.delete(notebook);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {
	if (logger.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		logger.debug(
			"Resetting Notebook completion for user ID " + userId + " and toolContentId " + toolContentId);
	    } else {
		logger.debug("Removing Notebook entries for user ID " + userId + " and toolContentId " + toolContentId);
	    }
	}

	Notebook notebook = notebookDAO.getByContentId(toolContentId);
	if (!resetActivityCompletionOnly) {
	    if (notebook == null) {
		NotebookService.logger.warn(
			"Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
		return;
	    }
	}

	for (NotebookSession session : notebook.getNotebookSessions()) {
	    NotebookUser user = notebookUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		if (resetActivityCompletionOnly) {
		    user.setFinishedActivity(false);
		    saveOrUpdateNotebookUser(user);
		} else {
		    if (user.getEntryUID() != null) {
			NotebookEntry entry = coreNotebookService.getEntry(user.getEntryUID());
			notebookDAO.delete(entry);
		    }

		    notebookUserDAO.delete(user);
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
	Notebook notebook = notebookDAO.getByContentId(toolContentId);
	if (notebook == null) {
	    notebook = getDefaultContent();
	}
	if (notebook == null) {
	    throw new DataMissingException("Unable to find default content for the notebook tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	notebook = Notebook.newInstance(notebook, toolContentId);
	notebook.setNotebookSessions(null);
	try {
	    exportContentService.exportToolContent(toolContentId, notebook, notebookToolContentHandler, rootPath);
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
	    exportContentService.registerImportVersionFilterClass(NotebookImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, notebookToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Notebook)) {
		throw new ImportToolContentException(
			"Import Notebook tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Notebook notebook = (Notebook) toolPOJO;

	    // reset it to new toolContentId
	    notebook.setToolContentId(toolContentId);
	    notebook.setCreateBy(new Long(newUserUid.longValue()));

	    notebookDAO.saveOrUpdate(notebook);
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
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Notebook notebook = getNotebookDAO().getByContentId(toolContentId);
	if (notebook == null) {
	    notebook = getDefaultContent();
	}
	return getNotebookOutputFactory().getToolOutputDefinitions(notebook, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getNotebookByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getNotebookByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Notebook notebook = notebookDAO.getByContentId(toolContentId);
	for (NotebookSession session : notebook.getNotebookSessions()) {
	    if (!session.getNotebookUsers().isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    /* ********** INotebookService Methods ********************************* */

    @Override
    public String finishToolSession(NotebookUser notebookUser, Boolean isContentEditable, String entryText) {
	Long userId = notebookUser.getUserId();
	Long toolSessionId = notebookUser.getNotebookSession().getSessionId();

	// learningForm.getContentEditable() will be null if the deadline has passed
	if (isContentEditable != null && isContentEditable) {
	    // TODO fix idType to use real value not 999
	    if (notebookUser.getEntryUID() == null) {
		Long entryUID = coreNotebookService.createNotebookEntry(toolSessionId,
			CoreNotebookConstants.NOTEBOOK_TOOL, NotebookConstants.TOOL_SIGNATURE,
			notebookUser.getUserId().intValue(), "", entryText);
		notebookUser.setEntryUID(entryUID);

	    } else {
		// update existing entry.
		coreNotebookService.updateEntry(notebookUser.getEntryUID(), "", entryText);
	    }

	    notebookUser.setFinishedActivity(true);
	    saveOrUpdateNotebookUser(notebookUser);
	}

	return leaveToolSession(toolSessionId, userId);
    }

    @Override
    public NotebookEntry getEntry(Long uid) {
	return coreNotebookService.getEntry(uid);
    }

    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    NotebookService.logger.error(error);
	    throw new NotebookException(error);
	}
	return toolContentId;
    }

    @Override
    public Notebook getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(NotebookConstants.TOOL_SIGNATURE);
	Notebook defaultContent = getNotebookByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    NotebookService.logger.error(error);
	    throw new NotebookException(error);
	}
	return defaultContent;
    }

    @Override
    public Notebook copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Notebook tools default content: + " + "newContentID is null";
	    NotebookService.logger.error(error);
	    throw new NotebookException(error);
	}

	Notebook defaultContent = getDefaultContent();
	// create new notebook using the newContentID
	Notebook newContent = new Notebook();
	newContent = Notebook.newInstance(defaultContent, newContentID);
	notebookDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Notebook getNotebookByContentId(Long toolContentID) {
	Notebook notebook = notebookDAO.getByContentId(toolContentID);
	if (notebook == null) {
	    NotebookService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return notebook;
    }

    @Override
    public NotebookSession getSessionBySessionId(Long toolSessionId) {
	NotebookSession notebookSession = notebookSessionDAO.getBySessionId(toolSessionId);
	if (notebookSession == null) {
	    NotebookService.logger.debug("Could not find the notebook session with toolSessionID:" + toolSessionId);
	}
	return notebookSession;
    }

    @Override
    public NotebookUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return notebookUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public NotebookUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return notebookUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public NotebookUser getUserByUID(Long uid) {
	return notebookUserDAO.getByUID(uid);
    }

    @Override
    public void saveOrUpdateNotebook(Notebook notebook) {
	notebookDAO.saveOrUpdate(notebook);
    }

    @Override
    public void saveOrUpdateNotebookSession(NotebookSession notebookSession) {
	notebookSessionDAO.saveOrUpdate(notebookSession);
    }

    @Override
    public void saveOrUpdateNotebookUser(NotebookUser notebookUser) {
	notebookUserDAO.saveOrUpdate(notebookUser);
    }

    @Override
    public NotebookUser createNotebookUser(UserDTO user, NotebookSession notebookSession) {
	NotebookUser notebookUser = new NotebookUser(user, notebookSession);
	saveOrUpdateNotebookUser(notebookUser);
	return notebookUser;
    }

    @Override
    public boolean notifyUser(Integer userId, String comment) {
	boolean isHtmlFormat = false;

	return eventNotificationService.sendMessage(null, userId, IEventNotificationService.DELIVERY_METHOD_MAIL,
		getLocalisedMessage("event.teacher.comment.subject", new Object[] {}),
		getLocalisedMessage("event.teacher.comment.body", new Object[] { comment }), isHtmlFormat);
    }

    private String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    @Override
    public List<Object[]> getUsersEntriesDates(final Long sessionId, Integer page, Integer size, int sorting,
	    String searchString) {
	return notebookUserDAO.getUsersEntriesDates(sessionId, page, size, sorting, searchString, coreNotebookService,
		userManagementService);
    }

    @Override
    public int getCountUsersBySession(final Long sessionId, String searchString) {
	return notebookUserDAO.getCountUsersBySession(sessionId, searchString);
    }

    @Override
    public List<StatisticDTO> getStatisticsBySession(final Long contentId) {
	return notebookUserDAO.getStatisticsBySession(contentId);
    }

    // =========================================================================================
    /* ********** Used by Spring to "inject" the linked objects ************* */

    public INotebookDAO getNotebookDAO() {
	return notebookDAO;
    }

    public void setNotebookDAO(INotebookDAO notebookDAO) {
	this.notebookDAO = notebookDAO;
    }

    public IToolContentHandler getNotebookToolContentHandler() {
	return notebookToolContentHandler;
    }

    public void setNotebookToolContentHandler(IToolContentHandler notebookToolContentHandler) {
	this.notebookToolContentHandler = notebookToolContentHandler;
    }

    public INotebookSessionDAO getNotebookSessionDAO() {
	return notebookSessionDAO;
    }

    public void setNotebookSessionDAO(INotebookSessionDAO sessionDAO) {
	notebookSessionDAO = sessionDAO;
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
	notebookUserDAO = userDAO;
    }

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public ILogEventService getLogEventService() {
	return logEventService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public NotebookOutputFactory getNotebookOutputFactory() {
	return notebookOutputFactory;
    }

    public void setNotebookOutputFactory(NotebookOutputFactory notebookOutputFactory) {
	this.notebookOutputFactory = notebookOutputFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createConditionName(Collection<NotebookCondition> existingConditions) {
	String uniqueNumber = null;
	do {
	    uniqueNumber = String.valueOf(Math.abs(generator.nextInt()));
	    for (NotebookCondition condition : existingConditions) {
		String[] splitedName = getNotebookOutputFactory().splitConditionName(condition.getName());
		if (uniqueNumber.equals(splitedName[1])) {
		    uniqueNumber = null;
		}
	    }
	} while (uniqueNumber == null);
	return getNotebookOutputFactory().buildUserEntryConditionName(uniqueNumber);
    }

    @Override
    public void releaseConditionsFromCache(Notebook notebook) {
	if (notebook.getConditions() != null) {
	    for (NotebookCondition condition : notebook.getConditions()) {
		getNotebookDAO().releaseFromCache(condition);
	    }
	}
    }

    @Override
    public void deleteCondition(NotebookCondition condition) {
	if ((condition != null) && (condition.getConditionId() != null)) {
	    notebookDAO.delete(condition);
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
    public boolean isLastActivity(Long toolSessionId) {
	return toolService.isLastActivity(toolSessionId);
    }

    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {
	return toolService.getLearnerContentFolder(toolSessionId, userId);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getNotebookOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	// db doesn't have a start/finish date for learner, and session start/finish is null
	NotebookUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isFinishedActivity()
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    // ****************** REST methods *************************

    /**
     * Rest call to create a new Notebook content. Required fields in toolContentJSON: "title", "instructions".
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {
	Date updateDate = new Date();

	Notebook nb = new Notebook();
	nb.setToolContentId(toolContentID);
	nb.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	nb.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));
	nb.setCreateBy(userID.longValue());
	nb.setCreateDate(updateDate);
	nb.setUpdateDate(updateDate);

	nb.setLockOnFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	nb.setAllowRichEditor(JsonUtil.optBoolean(toolContentJSON, RestTags.ALLOW_RICH_TEXT_EDITOR, Boolean.FALSE));
	// submissionDeadline is set in monitoring

	nb.setContentInUse(false);
	nb.setDefineLater(false);
	this.saveOrUpdateNotebook(nb);

	// TODO
	// nb.setConditions(conditions);

    }

}