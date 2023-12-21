/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.noticeboard.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
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
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardUserDAO;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.model.NoticeboardUser;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.JsonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An implementation of the NoticeboardService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 *
 * @author mtruong
 */
public class NoticeboardService
	implements INoticeboardService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(NoticeboardService.class);

    private ILamsToolService toolService;
    private IExportToolContentService exportContentService;
    private IToolContentHandler nbToolContentHandler;
    private ICoreNotebookService coreNotebookService;

    private INoticeboardContentDAO nbContentDAO;
    private INoticeboardSessionDAO nbSessionDAO;
    private INoticeboardUserDAO nbUserDAO;

    @Override
    public NoticeboardContent retrieveNoticeboard(Long nbContentId) throws NbApplicationException {
	if (nbContentId == null) {
	    throw new NbApplicationException("Tool content ID is missing");
	}

	return nbContentDAO.findNbContentById(nbContentId);
    }

    @Override
    public NoticeboardContent retrieveNoticeboardBySessionID(Long nbSessionId) {
	if (nbSessionId == null) {
	    throw new NbApplicationException("Tool session ID is missing");
	}

	return nbContentDAO.getNbContentBySession(nbSessionId);
    }

    @Override
    public void saveNoticeboard(NoticeboardContent nbContent) {
	if (nbContent.getUid() == null) {
	    nbContentDAO.saveNbContent(nbContent);
	} else {
	    nbContentDAO.updateNbContent(nbContent);
	}
    }

    @Override
    public void removeNoticeboardSessionsFromContent(NoticeboardContent nbContent) {
	nbContent.getNbSessions().clear();
	nbContentDAO.removeNbSessions(nbContent);
    }

    @Override
    public void removeNoticeboard(Long nbContentId) {
	if (nbContentId == null) {
	    throw new NbApplicationException("Tool content ID is missing");
	}
	nbContentDAO.removeNoticeboard(nbContentId);
    }

    @Override
    public void removeNoticeboard(NoticeboardContent nbContent) {
	nbContentDAO.removeNoticeboard(nbContent);
    }

    @Override
    public NoticeboardSession retrieveNoticeboardSession(Long nbSessionId) {
	if (nbSessionId == null) {
	    throw new NbApplicationException("Tool session ID is missing");
	}

	return nbSessionDAO.findNbSessionById(nbSessionId);
    }

    @Override
    public void saveNoticeboardSession(NoticeboardSession nbSession) {
	NoticeboardContent content = nbSession.getNbContent();
	nbSessionDAO.saveNbSession(nbSession);
    }

    @Override
    public void updateNoticeboardSession(NoticeboardSession nbSession) {
	nbSessionDAO.updateNbSession(nbSession);
    }

    @Override
    public void removeSession(Long nbSessionId) {
	if (nbSessionId == null) {
	    throw new NbApplicationException("Tool session id is missing");
	}

	NoticeboardSession sessionToDelete = retrieveNoticeboardSession(nbSessionId);
	NoticeboardContent contentReferredBySession = sessionToDelete.getNbContent();
	// un-associate the session from content
	contentReferredBySession.getNbSessions().remove(sessionToDelete);
	nbSessionDAO.removeNbSession(nbSessionId);
    }

    @Override
    public void removeSession(NoticeboardSession nbSession) {
	NoticeboardContent contentReferredBySession = nbSession.getNbContent();
	// un-associate the session from content
	contentReferredBySession.getNbSessions().remove(nbSession);
	nbSessionDAO.removeNbSession(nbSession);
    }

    @Override
    public void removeNoticeboardUsersFromSession(NoticeboardSession nbSession) {
	nbSession.getNbUsers().clear();
	nbSessionDAO.removeNbUsers(nbSession);
    }

    @Override
    public NoticeboardSession retrieveNbSessionByUserID(Long userId) {
	if (userId == null) {
	    throw new NbApplicationException("Tool session ID is missing");
	}
	return nbSessionDAO.getNbSessionByUser(userId);
    }

    @Override
    public NoticeboardUser retrieveNoticeboardUser(Long nbUserId, Long nbSessionId) {
	if (nbUserId == null) {
	    throw new NbApplicationException("User ID is missing");
	}

	return nbUserDAO.getNbUser(nbUserId, nbSessionId);
    }

    @Override
    public void saveNoticeboardUser(NoticeboardUser nbUser) {
	NoticeboardSession session = nbUser.getNbSession();
	session.getNbUsers().add(nbUser);
	nbUserDAO.saveNbUser(nbUser);
    }

    @Override
    public NoticeboardUser retrieveNbUserBySession(Long userId, Long sessionId) {
	return nbUserDAO.getNbUserBySession(userId, sessionId);
    }

    @Override
    public void updateNoticeboardUser(NoticeboardUser nbUser) {
	nbUserDAO.updateNbUser(nbUser);
    }

    @Override
    public void removeUser(NoticeboardUser nbUser) {
	nbUserDAO.delete(nbUser);
    }

    @Override
    public void removeUser(Long nbUserId, Long toolSessionId) {
	NoticeboardUser user = retrieveNoticeboardUser(nbUserId, toolSessionId);
	nbUserDAO.delete(user);
    }

    @Override
    public void addSession(Long nbContentId, NoticeboardSession session) {
	if ((nbContentId == null) || (session == null)) {
	    throw new NbApplicationException("Tool content ID or session is missing");
	}

	nbContentDAO.addNbSession(nbContentId, session);
    }

    @Override
    public void addUser(Long nbSessionId, NoticeboardUser user) {
	if (nbSessionId == null) {
	    throw new NbApplicationException("Tool session ID is missing");
	}
	nbSessionDAO.addNbUsers(nbSessionId, user);
    }

    @Override
    public int getNumberOfUsersInSession(NoticeboardSession session) {
	return nbUserDAO.getNumberOfUsers(session);
    }

    @Override
    public int calculateTotalNumberOfUsers(Long toolContentId) {
	if (toolContentId == null) {
	    throw new NbApplicationException("Tool content id is missing");
	}

	int totalNumberOfUsers = 0;
	NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
	for (NoticeboardSession session : nbContent.getNbSessions()) {
	    totalNumberOfUsers += getNumberOfUsersInSession(session);
	}

	return totalNumberOfUsers;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<NoticeboardUser> getUsersBySession(Long sessionId) {
	if (sessionId == null) {
	    throw new NbApplicationException("Session ID is missing");
	}
	return nbUserDAO.getNbUsersBySession(sessionId);
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
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to copy Noticeboard tool content. Missing parameter: toContentId");
	}
	if (fromContentId == null) {
	    // use the default content Id
	    fromContentId = getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE);
	}

	// fromContentId might not have any content, in this case use default content
	// default content id might not have any contnet, throw exception
	NoticeboardContent originalNb = null;

	try {
	    if ((originalNb = retrieveNoticeboard(fromContentId)) == null) // the id given does not have content, use
	    // default content
	    {
		// use default content id to grab contents
		NoticeboardContent defaultContent = retrieveNoticeboard(
			getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE));

		if (defaultContent != null) {
		    NoticeboardContent newContent = NoticeboardContent.newInstance(defaultContent, toContentId);
		    saveNoticeboard(newContent);
		} else {
		    throw new ToolException("Default content is missing. Unable to copy tool content");
		}
	    } else {
		NoticeboardContent newNbContent = NoticeboardContent.newInstance(originalNb, toContentId);
		saveNoticeboard(newNbContent);
	    }
	} catch (RepositoryCheckedException e) {
	    log.error(
		    "Unable to copy the tool content due to a content repository error. fromContentId " + fromContentId
			    + " toContentId " + toContentId);
	    throw new ToolException(e);
	}

    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	NoticeboardContent nbContent = getAndCheckIDandObject(toolContentId);
	nbContent.setDefineLater(false);
	saveNoticeboard(nbContent);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
	if (nbContent == null) {
	    log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (NoticeboardSession session : nbContent.getNbSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getNbSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, NoticeboardConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	removeNoticeboard(toolContentId);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {
	if (log.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		log.debug("Resetting Noticeboard user completion for user ID " + userId + " and toolContentId "
			+ toolContentId);
	    } else {
		log.debug("Removing Noticeboard user for user ID " + userId + " and toolContentId " + toolContentId);
	    }
	}

	NoticeboardContent nbContent = nbContentDAO.findNbContentById(toolContentId);
	if (nbContent == null) {
	    log.warn("Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (NoticeboardSession session : nbContent.getNbSessions()) {
	    NoticeboardUser user = nbUserDAO.getNbUser(userId.longValue(), session.getNbSessionId());
	    if (user != null) {
		if (resetActivityCompletionOnly) {
		    user.setUserStatus(NoticeboardUser.INCOMPLETE);
		    nbUserDAO.updateNbUser(user);
		} else {
		    NotebookEntry entry = getEntry(session.getNbSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			    NoticeboardConstants.TOOL_SIGNATURE, userId);
		    if (entry != null) {
			nbContentDAO.delete(entry);
		    }

		    nbUserDAO.delete(user);
		}
	    }
	}
    }

    private NoticeboardContent getAndCheckIDandObject(Long toolContentId) throws ToolException, DataMissingException {
	if (toolContentId == null) {
	    throw new ToolException("Tool content ID is missing. Unable to continue");
	}

	NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
	if (nbContent == null) {
	    throw new DataMissingException("No tool content matches this tool content id");
	}

	return nbContent;
    }

    private NoticeboardSession getAndCheckSessionIDandObject(Long toolSessionId)
	    throws ToolException, DataMissingException {
	if (toolSessionId == null) {
	    throw new ToolException("Tool session ID is missing.");
	}

	NoticeboardSession nbSession = retrieveNoticeboardSession(toolSessionId);
	if (nbSession == null) {
	    throw new DataMissingException("No tool session matches this tool session id");
	}

	return nbSession;
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	NoticeboardContent toolContentObj = nbContentDAO.findNbContentById(toolContentId);
	if (toolContentObj == null) {
	    Long defaultContentId = getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE);
	    toolContentObj = retrieveNoticeboard(defaultContentId);
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the noticeboard tool");
	}

	try {
	    // set ResourceToolContentHandler as null to avoid copy file node in repository again.
	    toolContentObj = NoticeboardContent.newInstance(toolContentObj, toolContentId);
	    toolContentObj.setNbSessions(null);
	    exportContentService.exportToolContent(toolContentId, toolContentObj, nbToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	} catch (ItemNotFoundException e) {
	    throw new ToolException(e);
	} catch (RepositoryCheckedException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(NoticeboardImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, nbToolContentHandler, fromVersion,
		    toVersion);
	    if (!(toolPOJO instanceof NoticeboardContent)) {
		throw new ImportToolContentException(
			"Import Noteice board tool content failed. Deserialized object is " + toolPOJO);
	    }
	    NoticeboardContent toolContentObj = (NoticeboardContent) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setNbContentId(toolContentId);
	    nbContentDAO.saveNbContent(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return new TreeMap<String, ToolOutputDefinition>();
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return retrieveNoticeboard(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return retrieveNoticeboard(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	NoticeboardContent nbContent = nbContentDAO.findNbContentById(toolContentId);
	for (NoticeboardSession session : nbContent.getNbSessions()) {
	    if (!session.getNbUsers().isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if ((toolSessionId == null) || (toolContentId == null)) {
	    String error = "Failed to create tool session. The tool session id or tool content id is invalid";
	    throw new ToolException(error);
	}

	NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
	NoticeboardSession nbSession = new NoticeboardSession(toolSessionId, toolSessionName, nbContent,
		new Date(System.currentTimeMillis()), NoticeboardSession.NOT_ATTEMPTED);

	nbContent.getNbSessions().add(nbSession);
	saveNoticeboard(nbContent);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	getAndCheckSessionIDandObject(toolSessionId);

	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws ToolException, DataMissingException {
	getAndCheckSessionIDandObject(toolSessionId);
	throw new UnsupportedOperationException("not yet implemented");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws ToolException, DataMissingException {
	Iterator<Long> i = toolSessionIds.iterator();
	if (i.hasNext()) {
	    Long id = i.next();
	    getAndCheckSessionIDandObject(id);
	}

	throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	NoticeboardSession session = getAndCheckSessionIDandObject(toolSessionId);
	removeSession(session);
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
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	return false;
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	//no actions required
    }

    @Override
    public Long getToolDefaultContentIdBySignature(String toolSignature) {
	return toolService.getToolDefaultContentIdBySignature(toolSignature);
    }

    public void setNbContentDAO(INoticeboardContentDAO nbContentDAO) {
	this.nbContentDAO = nbContentDAO;
    }

    public void setNbSessionDAO(INoticeboardSessionDAO nbSessionDAO) {
	this.nbSessionDAO = nbSessionDAO;
    }

    public void setNbUserDAO(INoticeboardUserDAO nbUserDAO) {
	this.nbUserDAO = nbUserDAO;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IToolContentHandler getNbToolContentHandler() {
	return nbToolContentHandler;
    }

    public void setNbToolContentHandler(IToolContentHandler nbToolContentHandler) {
	this.nbToolContentHandler = nbToolContentHandler;
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

    /* =============== Wrappers Methods for Notebook Service (Reflective Option) =============== */

    @Override
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry) {
	return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
    }

    @Override
    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
	if ((list == null) || list.isEmpty()) {
	    return null;
	} else {
	    return list.get(0);
	}
    }

    @Override
    public void updateEntry(NotebookEntry notebookEntry) {
	coreNotebookService.updateEntry(notebookEntry);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	// db doesn't have a start/finish date for learner, and session start/finish is null
	NoticeboardUser learner = retrieveNoticeboardUser(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(NoticeboardUser.COMPLETED.equals(learner.getUserStatus())
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    // ****************** REST methods *************************

    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {
	Date updateDate = new Date();

	NoticeboardContent noticeboard = new NoticeboardContent();
	noticeboard.setNbContentId(toolContentID);
	noticeboard.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	noticeboard.setContent(JsonUtil.optString(toolContentJSON, "content"));
	noticeboard.setReflectOnActivity(
		JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	noticeboard.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));

	noticeboard.setCreatorUserId(userID.longValue());
	noticeboard.setDateCreated(updateDate);
	noticeboard.setDateUpdated(updateDate);
	noticeboard.setContentInUse(false);

	saveNoticeboard(noticeboard);
    }
}