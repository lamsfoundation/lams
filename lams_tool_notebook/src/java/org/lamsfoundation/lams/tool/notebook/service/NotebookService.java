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

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
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
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookSessionDAO;
import org.lamsfoundation.lams.tool.notebook.dao.INotebookUserDAO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookCondition;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * An implementation of the INotebookService interface.
 * 
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class NotebookService implements ToolSessionManager, ToolContentManager, INotebookService,
	ToolContentImport102Manager, ToolRestManager {

    static Logger logger = Logger.getLogger(NotebookService.class.getName());

    private INotebookDAO notebookDAO = null;

    private INotebookSessionDAO notebookSessionDAO = null;

    private INotebookUserDAO notebookUserDAO = null;

    private ILearnerService learnerService;

    private ILamsToolService toolService;

    private IToolContentHandler notebookToolContentHandler = null;

    private IAuditService auditService = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;
    
    private IEventNotificationService eventNotificationService;
    
    private MessageService messageService;

    private NotebookOutputFactory notebookOutputFactory;

    private Random generator = new Random();

    public NotebookService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (NotebookService.logger.isDebugEnabled()) {
	    NotebookService.logger.debug("entering method createToolSession:" + " toolSessionId = " + toolSessionId
		    + " toolSessionName = " + toolSessionName + " toolContentId = " + toolContentId);
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

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	// TODO Auto-generated method stub
	return null;
    }

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	notebookSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getNotebookOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getNotebookOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ************ Methods from ToolContentManager ************************* */

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {

	if (NotebookService.logger.isDebugEnabled()) {
	    NotebookService.logger.debug("entering method copyToolContent:" + " fromContentId=" + fromContentId
		    + " toContentId=" + toContentId);
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
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
    }
    
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (logger.isDebugEnabled()) {
	    logger.debug("Removing Notebook entries for user ID " + userId + " and toolContentId " + toolContentId);
	}

	Notebook notebook = notebookDAO.getByContentId(toolContentId);
	if (notebook == null) {
	    logger.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}
	
	for (NotebookSession session : (Set<NotebookSession>) notebook.getNotebookSessions()) {
	    NotebookUser user = notebookUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		if (user.getEntryUID() != null) {
		    NotebookEntry entry = coreNotebookService.getEntry(user.getEntryUID());
		    notebookDAO.delete(entry);
		}

		notebookUserDAO.delete(user);
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
     *                 if any other error occurs
     */
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(NotebookImportContentVersionFilter.class);
	
	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, notebookToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Notebook)) {
		throw new ImportToolContentException("Import Notebook tool content failed. Deserialized object is "
			+ toolPOJO);
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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Notebook notebook = getNotebookDAO().getByContentId(toolContentId);
	if (notebook == null) {
	    notebook = getDefaultContent();
	}
	return getNotebookOutputFactory().getToolOutputDefinitions(notebook, definitionType);
    }
    
    public String getToolContentTitle(Long toolContentId) {
	return getNotebookByContentId(toolContentId).getTitle();
    }
    
    public boolean isContentEdited(Long toolContentId) {
	return getNotebookByContentId(toolContentId).isDefineLater();
    }
   
    /* ********** INotebookService Methods ********************************* */

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
	    NotebookService.logger.error(error);
	    throw new NotebookException(error);
	}
	return toolContentId;
    }

    public Notebook getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(NotebookConstants.TOOL_SIGNATURE);
	Notebook defaultContent = getNotebookByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    NotebookService.logger.error(error);
	    throw new NotebookException(error);
	}
	if (defaultContent.getConditions().isEmpty()) {
	    defaultContent.getConditions()
		    .add(getNotebookOutputFactory().createDefaultUserEntryCondition(defaultContent));
	}
	return defaultContent;
    }

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

    public Notebook getNotebookByContentId(Long toolContentID) {
	Notebook notebook = notebookDAO.getByContentId(toolContentID);
	if (notebook == null) {
	    NotebookService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return notebook;
    }

    public NotebookSession getSessionBySessionId(Long toolSessionId) {
	NotebookSession notebookSession = notebookSessionDAO.getBySessionId(toolSessionId);
	if (notebookSession == null) {
	    NotebookService.logger.debug("Could not find the notebook session with toolSessionID:" + toolSessionId);
	}
	return notebookSession;
    }

    public NotebookUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return notebookUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    public NotebookUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return notebookUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    public NotebookUser getUserByUID(Long uid) {
	return notebookUserDAO.getByUID(uid);
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

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Notebook
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
	Date now = new Date();
	Notebook notebook = new Notebook();
	notebook.setContentInUse(Boolean.FALSE);
	notebook.setCreateBy(new Long(user.getUserID().longValue()));
	notebook.setCreateDate(now);
	notebook.setDefineLater(Boolean.FALSE);
	notebook.setInstructions(WebUtil.convertNewlines((String) importValues
		.get(ToolContentImport102Manager.CONTENT_BODY)));
	notebook.setLockOnFinished(Boolean.TRUE);
	notebook.setTitle((String) importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
	notebook.setToolContentId(toolContentId);
	notebook.setUpdateDate(now);
	notebook.setAllowRichEditor(Boolean.FALSE);
	// leave as empty, no need to set them to anything.
	// setNotebookSessions(Set notebookSessions);
	notebookDAO.saveOrUpdate(notebook);
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	NotebookService.logger
		.warn("Setting the reflective field on a notebook. This doesn't make sense as the notebook is for reflection and we don't reflect on reflection!");
	Notebook notebook = getNotebookByContentId(toolContentId);
	if (notebook == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	notebook.setInstructions(description);
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

    public IAuditService getAuditService() {
	return auditService;
    }

    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
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

    public void releaseConditionsFromCache(Notebook notebook) {
	if (notebook.getConditions() != null) {
	    for (NotebookCondition condition : notebook.getConditions()) {
		getNotebookDAO().releaseFromCache(condition);
	    }
	}
    }

    public void deleteCondition(NotebookCondition condition) {
	if (condition != null && condition.getConditionId() != null) {
	    notebookDAO.delete(condition);
	}
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }
    
    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {
	return toolService.getLearnerContentFolder(toolSessionId, userId);
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getNotebookOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
    
    // ****************** REST methods *************************

    /** Rest call to create a new Notebook content. Required fields in toolContentJSON: "title", "instructions".
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON) throws JSONException {
	Date updateDate = new Date();

	Notebook nb = new Notebook();
	nb.setToolContentId(toolContentID);
	nb.setTitle(toolContentJSON.getString(RestTags.TITLE));
	nb.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));
	nb.setCreateBy(userID.longValue());
	nb.setCreateDate(updateDate);
	nb.setUpdateDate(updateDate);

	nb.setLockOnFinished(JsonUtil.opt(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	nb.setAllowRichEditor(JsonUtil.opt(toolContentJSON, RestTags.ALLOW_RICH_TEXT_EDITOR, Boolean.FALSE));
	// submissionDeadline is set in monitoring

	nb.setContentInUse(false);
	nb.setDefineLater(false);
	this.saveOrUpdateNotebook(nb);
	
	// TODO
	// nb.setConditions(conditions);
	
    }
    
}