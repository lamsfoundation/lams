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

package org.lamsfoundation.lams.tool.scribe.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
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
import org.lamsfoundation.lams.tool.scribe.dao.IScribeDAO;
import org.lamsfoundation.lams.tool.scribe.dao.IScribeHeadingDAO;
import org.lamsfoundation.lams.tool.scribe.dao.IScribeSessionDAO;
import org.lamsfoundation.lams.tool.scribe.dao.IScribeUserDAO;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeHeading;
import org.lamsfoundation.lams.tool.scribe.model.ScribeReportEntry;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.util.ScribeException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.JsonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * An implementation of the IScribeService interface.
 *
 * As a requirement, all LAMS tool's service bean must implement ToolContentManager and ToolSessionManager.
 */

public class ScribeService implements ToolSessionManager, ToolContentManager, ToolRestManager, IScribeService {

    private static Logger logger = Logger.getLogger(ScribeService.class.getName());

    private IScribeDAO scribeDAO = null;

    private IScribeSessionDAO scribeSessionDAO = null;

    private IScribeHeadingDAO scribeHeadingDAO = null;

    private IScribeUserDAO scribeUserDAO = null;

    private ILamsToolService toolService;

    private IToolContentHandler scribeToolContentHandler = null;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    public ScribeService() {
	super();
    }

    /* ************ Methods from ToolSessionManager ************* */

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	if (ScribeService.logger.isDebugEnabled()) {
	    ScribeService.logger.debug(
		    "entering method createToolSession:" + " toolSessionId = " + toolSessionId + " toolSessionName = "
			    + toolSessionName + " toolContentId = " + toolContentId);
	}

	ScribeSession session = new ScribeSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Scribe scribe = scribeDAO.getByContentId(toolContentId);
	session.setScribe(scribe);

	session.setForceComplete(false);
	session.setReportSubmitted(false);
	scribeSessionDAO.saveOrUpdate(session);
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
	scribeSessionDAO.deleteBySessionID(toolSessionId);
	// TODO check if cascade worked
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return new TreeMap<>();
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return null;
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

	if (ScribeService.logger.isDebugEnabled()) {
	    ScribeService.logger.debug(
		    "entering method copyToolContent:" + " fromContentId=" + fromContentId + " toContentId="
			    + toContentId);
	}

	if (toContentId == null) {
	    String error = "Failed to copy tool content: toContentID is null";
	    throw new ToolException(error);
	}

	Scribe fromContent = null;
	if (fromContentId != null) {
	    fromContent = scribeDAO.getByContentId(fromContentId);
	}
	if (fromContent == null) {
	    // create the fromContent using the default tool content
	    fromContent = getDefaultContent();
	}
	Scribe toContent = Scribe.newInstance(fromContent, toContentId);
	scribeDAO.saveOrUpdate(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Scribe scribe = scribeDAO.getByContentId(toolContentId);
	if (scribe == null) {
	    throw new ToolException("Could not find tool with toolContentID: " + toolContentId);
	}
	scribe.setDefineLater(false);
	scribeDAO.saveOrUpdate(scribe);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Scribe scribe = scribeDAO.getByContentId(toolContentId);
	if (scribe == null) {
	    ScribeService.logger.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (ScribeSession session : (Set<ScribeSession>) scribe.getScribeSessions()) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	scribeDAO.delete(scribe);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void removeLearnerContent(Long toolContentId, Integer userId, boolean resetActivityCompletionOnly)
	    throws ToolException {
	if (logger.isDebugEnabled()) {
	    if (resetActivityCompletionOnly) {
		logger.debug(
			"Resetting Scribe completion for user ID " + userId + " and toolContentId " + toolContentId);
	    } else {
		logger.debug("Removing Scribe content for user ID " + userId + " and toolContentId " + toolContentId);
	    }
	}

	Scribe scribe = scribeDAO.getByContentId(toolContentId);
	if (scribe == null) {
	    ScribeService.logger.warn(
		    "Did not find activity with toolContentId: " + toolContentId + " to remove learner content");
	    return;
	}

	for (ScribeSession session : scribe.getScribeSessions()) {
	    if ((session.getAppointedScribe() != null) && session.getAppointedScribe().getUserId()
		    .equals(userId.longValue())) {

		for (ScribeUser user : session.getScribeUsers()) {
		    if (user.getUserId().equals(userId.longValue())) {
			if (resetActivityCompletionOnly) {
			    user.setFinishedActivity(false);
			    scribeUserDAO.saveOrUpdate(user);
			} else {
			    scribeUserDAO.delete(user);
			}
		    } else {
			user.setReportApproved(false);
			scribeUserDAO.saveOrUpdate(user);
		    }
		}

		session.setAppointedScribe(null);
		session.setForceComplete(false);
		session.setReportSubmitted(false);
		session.getScribeReportEntries().clear();
		scribeSessionDAO.update(session);
	    } else {
		ScribeUser user = scribeUserDAO.getByUserIdAndSessionId(userId.longValue(), session.getSessionId());
		if (user != null) {
		    if (resetActivityCompletionOnly) {
			user.setFinishedActivity(false);
		    } else {
			scribeUserDAO.delete(user);
		    }
		}
	    }
	    if (!resetActivityCompletionOnly) {
		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			ScribeConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    scribeDAO.delete(entry);
		}
	    }
	}
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Scribe scribe = scribeDAO.getByContentId(toolContentId);
	if (scribe == null) {
	    scribe = getDefaultContent();
	}
	if (scribe == null) {
	    throw new DataMissingException("Unable to find default content for the scribe tool");
	}

	// set ResourceToolContentHandler as null to avoid copy file node in
	// repository again.
	scribe = Scribe.newInstance(scribe, toolContentId);
	scribe.setScribeSessions(null);
	// wipe out the links from ScribeHeading back to Scribe, or it will try to
	// include the hibernate object version of the Scribe within the XML
	Set<ScribeHeading> headings = scribe.getScribeHeadings();
	for (ScribeHeading heading : headings) {
	    heading.setScribe(null);
	}
	try {
	    exportContentService.exportToolContent(toolContentId, scribe, scribeToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {
	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(ScribeImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, scribeToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Scribe)) {
		throw new ImportToolContentException(
			"Import Scribe tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Scribe scribe = (Scribe) toolPOJO;

	    // reset it to new toolContentId
	    scribe.setToolContentId(toolContentId);
	    scribe.setCreateBy(new Long(newUserUid.longValue()));

	    scribeDAO.saveOrUpdate(scribe);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	return new TreeMap<>();
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getScribeByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getScribeByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	Scribe scribe = scribeDAO.getByContentId(toolContentId);
	for (ScribeSession session : (Set<ScribeSession>) scribe.getScribeSessions()) {
	    if ((session.getAppointedScribe() != null) || !session.getScribeUsers().isEmpty()) {
		return true;

	    }
	}

	return false;
    }

    /* ********** IScribeService Methods ************************************** */

    @Override
    public void createReportEntry(Long toolSessionId) {
	// creating scribeReports for each heading and add to session.
	ScribeSession session = scribeSessionDAO.getBySessionId(toolSessionId);

	// these heading report already copied from content, the skipit.
	Set entries = session.getScribeReportEntries();
	if ((entries != null) && (entries.size() > 0)) {
	    return;
	}

	Scribe scribe = session.getScribe();
	Set reports = session.getScribeReportEntries();
	if (reports == null) {
	    reports = new HashSet();
	    session.setScribeReportEntries(reports);
	}
	for (Iterator iter = scribe.getScribeHeadings().iterator(); iter.hasNext(); ) {
	    ScribeHeading heading = (ScribeHeading) iter.next();

	    ScribeReportEntry report = new ScribeReportEntry();
	    report.setScribeHeading(heading);

	    reports.add(report);
	}
	scribeSessionDAO.update(session);

    }

    @Override
    public void deleteHeadingReport(Long uid) {
	scribeHeadingDAO.deleteReport(uid);

    }

    @Override
    public Long getDefaultContentIdBySignature(String toolSignature) {
	Long toolContentId = null;
	toolContentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (toolContentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    ScribeService.logger.error(error);
	    throw new ScribeException(error);
	}
	return toolContentId;
    }

    @Override
    public Scribe getDefaultContent() {
	Long defaultContentID = getDefaultContentIdBySignature(ScribeConstants.TOOL_SIGNATURE);
	Scribe defaultContent = getScribeByContentId(defaultContentID);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    ScribeService.logger.error(error);
	    throw new ScribeException(error);
	}
	return defaultContent;
    }

    @Override
    public Scribe copyDefaultContent(Long newContentID) {

	if (newContentID == null) {
	    String error = "Cannot copy the Scribe tools default content: + " + "newContentID is null";
	    ScribeService.logger.error(error);
	    throw new ScribeException(error);
	}

	Scribe defaultContent = getDefaultContent();
	// create new scribe using the newContentID
	Scribe newContent = new Scribe();
	newContent = Scribe.newInstance(defaultContent, newContentID);
	scribeDAO.saveOrUpdate(newContent);
	return newContent;
    }

    @Override
    public Scribe getScribeByContentId(Long toolContentID) {
	Scribe scribe = scribeDAO.getByContentId(toolContentID);
	if (scribe == null) {
	    ScribeService.logger.debug("Could not find the content with toolContentID:" + toolContentID);
	}
	return scribe;
    }

    @Override
    public ScribeSession getSessionBySessionId(Long toolSessionId) {
	ScribeSession scribeSession = scribeSessionDAO.getBySessionId(toolSessionId);
	if (scribeSession == null) {
	    ScribeService.logger.debug("Could not find the scribe session with toolSessionID:" + toolSessionId);
	}
	return scribeSession;
    }

    @Override
    public ScribeUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId) {
	return scribeUserDAO.getByUserIdAndSessionId(userId, toolSessionId);
    }

    @Override
    public ScribeUser getUserByLoginNameAndSessionId(String loginName, Long toolSessionId) {
	return scribeUserDAO.getByLoginNameAndSessionId(loginName, toolSessionId);
    }

    @Override
    public ScribeUser getUserByUID(Long uid) {
	return scribeUserDAO.getByUID(uid);
    }

    @Override
    public void saveOrUpdateScribe(Scribe scribe) {
	scribeDAO.saveOrUpdate(scribe);
    }

    @Override
    public void saveOrUpdateScribeSession(ScribeSession scribeSession) {
	scribeSessionDAO.saveOrUpdate(scribeSession);
    }

    @Override
    public void saveOrUpdateScribeUser(ScribeUser scribeUser) {
	scribeUserDAO.saveOrUpdate(scribeUser);
    }

    @Override
    public ScribeUser createScribeUser(UserDTO user, ScribeSession scribeSession) {
	ScribeUser scribeUser = new ScribeUser(user, scribeSession);
	saveOrUpdateScribeUser(scribeUser);
	return scribeUser;
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
    public void submitReport(Long toolSessionId, String userName, ObjectNode requestJSON) {
	ScribeSession scribeSession = getSessionBySessionId(toolSessionId);
	ScribeUser scribe = scribeSession.getAppointedScribe();
	if ((scribe == null) || !scribe.getLoginName().equals(userName)) {
	    return;
	}

	for (ScribeUser learner : (Set<ScribeUser>) scribeSession.getScribeUsers()) {
	    learner.setReportApproved(false);
	    saveOrUpdateScribeUser(learner);
	}

	ArrayNode reportsJSON = JsonUtil.optArray(requestJSON, "reports");
	for (JsonNode reportJSON : reportsJSON) {
	    Long uid = JsonUtil.optLong(reportJSON, "uid");
	    String text = JsonUtil.optString(reportJSON, "text");
	    for (ScribeReportEntry report : (Set<ScribeReportEntry>) scribeSession.getScribeReportEntries()) {
		if (report.getUid().equals(uid)) {
		    report.setEntryText(text);
		    break;
		}
	    }
	}

	scribeSession.setReportSubmitted(true);
	saveOrUpdateScribeSession(scribeSession);
    }

    /* ********** Used by Spring to "inject" the linked objects ************* */

    public IScribeDAO getScribeDAO() {
	return scribeDAO;
    }

    public void setScribeDAO(IScribeDAO scribeDAO) {
	this.scribeDAO = scribeDAO;
    }

    public IToolContentHandler getScribeToolContentHandler() {
	return scribeToolContentHandler;
    }

    public void setScribeToolContentHandler(IToolContentHandler scribeToolContentHandler) {
	this.scribeToolContentHandler = scribeToolContentHandler;
    }

    public IScribeSessionDAO getScribeSessionDAO() {
	return scribeSessionDAO;
    }

    public void setScribeSessionDAO(IScribeSessionDAO sessionDAO) {
	scribeSessionDAO = sessionDAO;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public IScribeUserDAO getScribeUserDAO() {
	return scribeUserDAO;
    }

    public void setScribeUserDAO(IScribeUserDAO userDAO) {
	scribeUserDAO = userDAO;
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

    // =========================================================================================

    public void setScribeHeadingDAO(IScribeHeadingDAO scribeHeadingDAO) {
	this.scribeHeadingDAO = scribeHeadingDAO;
    }

    @Override
    public void deleteHeading(Long headingUid) {
	scribeHeadingDAO.deleteById(ScribeHeading.class, headingUid);
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return null;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	ScribeUser learner = getUserByUserIdAndSessionId(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	return new ToolCompletionStatus(learner.isFinishedActivity()
		? ToolCompletionStatus.ACTIVITY_COMPLETED
		: ToolCompletionStatus.ACTIVITY_ATTEMPTED, null, null);
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: "title", "instructions",
     * "questions". Questions must contain a ArrayNode of ObjectNode objects, which have the following mandatory fields:
     * "displayOrder", "questionText" There must be at least one topic object in the "questions" array.
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	Date updateDate = new Date();

	Scribe scribe = new Scribe();
	scribe.setCreateBy(userID.longValue());
	scribe.setCreateDate(updateDate);
	scribe.setUpdateDate(updateDate);

	scribe.setToolContentId(toolContentID);
	scribe.setContentInUse(false);
	scribe.setDefineLater(false);

	scribe.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	scribe.setInstructions(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));

	scribe.setAutoSelectScribe(JsonUtil.optBoolean(toolContentJSON, "autoSelectScribe", Boolean.FALSE));
	scribe.setLockOnFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	scribe.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	scribe.setReflectOnActivity(JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	scribe.setShowAggregatedReports(JsonUtil.optBoolean(toolContentJSON, "showAggregatedReports", Boolean.FALSE));

	if (scribe.getScribeHeadings() == null) {
	    scribe.setScribeHeadings(new HashSet());
	}

	ArrayNode topics = JsonUtil.optArray(toolContentJSON, RestTags.QUESTIONS);
	for (JsonNode topic : topics) {
	    ScribeHeading heading = new ScribeHeading();
	    heading.setDisplayOrder(JsonUtil.optInt(topic, RestTags.DISPLAY_ORDER));
	    heading.setHeadingText(JsonUtil.optString(topic, RestTags.QUESTION_TEXT));
	    heading.setScribe(scribe);
	    scribe.getScribeHeadings().add(heading);
	}

	// must have at least one heading - it should be supplied by the caller but have a backup just in case.
	// if we don't, the scribe can't actually enter any text!
	if (scribe.getScribeHeadings().size() == 0) {
	    ScribeHeading heading = new ScribeHeading();
	    heading.setDisplayOrder(1);

	    Scribe defaultContent = getDefaultContent();
	    Set defaultHeadings = defaultContent.getScribeHeadings();
	    if ((defaultHeadings != null) && (defaultHeadings.size() > 0)) {
		Iterator iter = defaultHeadings.iterator();
		if (iter.hasNext()) {
		    heading.setHeadingText(((ScribeHeading) iter.next()).getHeadingText());
		}
	    }
	    if (heading.getHeadingText() == null) {
		heading.setHeadingText("Heading");
	    }

	    heading.setScribe(scribe);
	    scribe.getScribeHeadings().add(heading);
	}
	saveOrUpdateScribe(scribe);

    }
}