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

package org.lamsfoundation.lams.tool.sbmt.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.exception.FileException;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.LoginException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.exception.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
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
import org.lamsfoundation.lams.tool.sbmt.SbmtConstants;
import org.lamsfoundation.lams.tool.sbmt.SubmissionDetails;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmissionDetailsDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesSessionDAO;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitUserDAO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtToolContentHandler;
import org.lamsfoundation.lams.tool.sbmt.util.SubmitFilesException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesService
	implements ToolContentManager, ToolSessionManager, ISubmitFilesService, ToolRestManager {

    private static Logger log = Logger.getLogger(SubmitFilesService.class);

    private ISubmitFilesContentDAO submitFilesContentDAO;

    private ISubmitFilesReportDAO submitFilesReportDAO;

    private ISubmitFilesSessionDAO submitFilesSessionDAO;

    private ISubmissionDetailsDAO submissionDetailsDAO;

    private ISubmitUserDAO submitUserDAO;

    private IToolContentHandler sbmtToolContentHandler;

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IRepositoryService repositoryService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IUserManagementService userManagementService;

    private IEventNotificationService eventNotificationService;

    private MessageService messageService;

    private IGradebookService gradebookService;

    private ILogEventService logEventService;

    private SubmitFilesOutputFactory submitFilesOutputFactory;

    private class FileDtoComparator implements Comparator<FileDetailsDTO> {

	@Override
	public int compare(FileDetailsDTO o1, FileDetailsDTO o2) {
	    if ((o1 != null) && (o2 != null) && (o1.getDateOfSubmission() != null)
		    && (o2.getDateOfSubmission() != null)) {
		// don't use Date.comparaTo() directly, because the date could be Timestamp or Date (depeneds the object
		// is persist or not)
		return (o1.getDateOfSubmission().getTime() - o2.getDateOfSubmission().getTime()) > 0 ? 1 : -1;
	    } else if (o1 != null) {
		return 1;
	    } else {
		return -1;
	    }
	}

    }

    @Override
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, idType, signature, userID);
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
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SubmitFiles tool seession");
	}

	SubmitFilesContent fromContent = null;
	if (fromContentId != null) {
	    fromContent = submitFilesContentDAO.getContentByID(fromContentId);
	}
	if (fromContent == null) {
	    fromContent = getDefaultSubmit();
	}
	SubmitFilesContent toContent = SubmitFilesContent.newInstance(fromContent, toContentId);

	submitFilesContentDAO.saveOrUpdate(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	SubmitFilesContent content = getSubmitFilesContent(toolContentId);
	if (content == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	content.setDefineLater(false);
	submitFilesContentDAO.saveOrUpdate(content);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	SubmitFilesContent submitFilesContent = submitFilesContentDAO.getContentByID(toolContentId);
	if (submitFilesContent == null) {
	    SubmitFilesService.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (SubmitFilesSession session : submitFilesSessionDAO.getSubmitFilesSessionByContentID(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionID(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, SbmtConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	submitFilesContentDAO.delete(submitFilesContent);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (SubmitFilesService.log.isDebugEnabled()) {
	    SubmitFilesService.log.debug(
		    "Removing Submit Files content for user ID " + userId + " and toolContentId " + toolContentId);
	}

	List<SubmitFilesSession> sessions = submitFilesSessionDAO.getSubmitFilesSessionByContentID(toolContentId);
	for (SubmitFilesSession session : sessions) {
	    List<SubmissionDetails> submissions = submissionDetailsDAO.getBySessionAndLearner(session.getSessionID(),
		    userId);
	    submissionDetailsDAO.deleteAll(submissions);

	    SubmitUser user = submitUserDAO.getLearner(session.getSessionID(), userId);
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionID(), CoreNotebookConstants.NOTEBOOK_TOOL,
			SbmtConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    submitFilesContentDAO.delete(entry);
		}

		gradebookService.removeActivityMark(user.getUserID(), session.getSessionID());

		submitUserDAO.delete(user);
	    }
	}
    }

    @Override
    public void copyLearnerContent(SubmitUser fromUser, SubmitUser toUser) throws ToolException {

	if ((fromUser == null) || (toUser == null) || fromUser.getUid().equals(toUser.getUid())) {
	    return;
	}

	List<SubmissionDetails> leaderSubmissions = submissionDetailsDAO.getBySessionAndLearner(fromUser.getSessionID(),
		fromUser.getUserID());

	List<SubmissionDetails> existingSubmissions = submissionDetailsDAO
		.getBySessionAndLearner(toUser.getSessionID(), toUser.getUserID());

	// Note: uuid never changes once a file is uploaded as this is the key from the content repository.
	for (SubmissionDetails leadersubmission : leaderSubmissions) {
	    SubmissionDetails usersubmission = null;
	    for (SubmissionDetails exSubCheck : existingSubmissions) {
		if (exSubCheck.getUuid().equals(leadersubmission.getUuid())) {
		    usersubmission = exSubCheck;
		    break;
		}
	    }
	    if (usersubmission == null) {
		usersubmission = new SubmissionDetails();
	    }
	    usersubmission.setLearner(toUser);
	    usersubmission.setDateOfSubmission(leadersubmission.getDateOfSubmission());
	    usersubmission.setFileDescription(leadersubmission.getFileDescription());
	    usersubmission.setFilePath(leadersubmission.getFilePath());
	    usersubmission.setSubmitFileSession(leadersubmission.getSubmitFileSession());
	    usersubmission.setUuid(leadersubmission.getUuid());
	    usersubmission.setVersionID(leadersubmission.getVersionID());
	    usersubmission.setRemoved(leadersubmission.isRemoved());
	    // submissionID is set by Hibernate

	    if (leadersubmission.getReport() != null) {
		SubmitFilesReport leaderReport = leadersubmission.getReport();
		SubmitFilesReport userreport = usersubmission.getReport();
		if (userreport == null) {
		    userreport = new SubmitFilesReport();
		    usersubmission.setReport(userreport);
		}
		userreport.setComments(leaderReport.getComments());
		userreport.setDateMarksReleased(leaderReport.getDateMarksReleased());
		userreport.setMarkFileName(leaderReport.getMarkFileName());
		userreport.setMarkFileUUID(leaderReport.getMarkFileUUID());
		userreport.setMarkFileVersionID(leaderReport.getMarkFileVersionID());
		userreport.setMarks(leaderReport.getMarks());
		// reportID is set by Hibernate to match submissionID
	    } else {
		usersubmission.setReport(null);
	    }
	    
	    submissionDetailsDAO.save(usersubmission);
	}
    }

    @Override
    public List<SubmitFilesSession> getSessionsByContentID(Long toolContentID) {
	return submitFilesSessionDAO.getSubmitFilesSessionByContentID(toolContentID);
    }

    /**
     * Export the XML fragment for the tool's content, along with any files needed for the content.
     *
     * @throws ExportToolContentException
     */
    @Override
    public void exportToolContent(Long toolContentId, String toPath) throws ToolException, DataMissingException {
	SubmitFilesContent toolContentObj = submitFilesContentDAO.getContentByID(toolContentId);
	if (toolContentObj == null) {
	    toolContentObj = getDefaultSubmit();
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the submit files tool");
	}

	// set toolContentHandler as null to avoid duplicate file node in repository.
	toolContentObj = SubmitFilesContent.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, sbmtToolContentHandler, toPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(SubmitFilesImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, sbmtToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof SubmitFilesContent)) {
		throw new ImportToolContentException(
			"Import Submit tool content failed. Deserialized object is " + toolPOJO);
	    }
	    SubmitFilesContent toolContentObj = (SubmitFilesContent) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentID(toolContentId);

	    SubmitUser user = submitUserDAO.getContentUser(toolContentId, newUserUid);
	    if (user == null) {
		user = new SubmitUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLogin(sysUser.getLogin());
		user.setUserID(newUserUid);
		user.setContentID(toolContentId);
		submitUserDAO.saveOrUpdateUser(user);
	    }

	    toolContentObj.setCreatedBy(user);
	    toolContentObj.setCreated(new Date());
	    toolContentObj.setUpdated(new Date());

	    submitFilesContentDAO.saveOrUpdate(toolContentObj);
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
	SubmitFilesContent content = getSubmitFilesContent(toolContentId);
	if (content == null) {
	    content = getDefaultSubmit();
	}
	return getSubmitFilesOutputFactory().getToolOutputDefinitions(content, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getSubmitFilesContent(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getSubmitFilesContent(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	List<SubmitFilesSession> sessions = submitFilesSessionDAO.getSubmitFilesSessionByContentID(toolContentId);
	for (SubmitFilesSession session : sessions) {
	    if (!submitUserDAO.getUsersBySession(session.getSessionID()).isEmpty()) {
		return true;
	    }
	}

	return false;
    }

    @Override
    public void saveOrUpdateContent(SubmitFilesContent submitFilesContent) {
	submitFilesContent.setUpdated(new Date());
	submitFilesContentDAO.saveOrUpdate(submitFilesContent);
    }

    @Override
    public SubmitFilesContent getSubmitFilesContent(Long contentID) {
	SubmitFilesContent content = null;
	try {
	    content = submitFilesContentDAO.getContentByID(contentID);
	} catch (Exception e) {
	    SubmitFilesService.log.error("Could not find the content by given ID:" + contentID + ". Excpetion is " + e);
	}

	return content;
    }

    @Override
    public SubmitFilesReport getSubmitFilesReport(Long reportID) {
	return submitFilesReportDAO.getReportByID(reportID);
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
    private ITicket getRepositoryLoginTicket() throws SubmitFilesException {
	ICredentials credentials = new SimpleCredentials(SbmtToolContentHandler.repositoryUser,
		SbmtToolContentHandler.repositoryId);
	try {
	    ITicket ticket = repositoryService.login(credentials, SbmtToolContentHandler.repositoryWorkspaceName);
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new SubmitFilesException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new SubmitFilesException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new SubmitFilesException("Login failed." + e.getMessage());
	}
    }

    @Override
    public void deleteFromRepository(Long uuid, Long versionID) throws SubmitFilesException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, uuid, versionID);
	} catch (Exception e) {
	    throw new SubmitFilesException(
		    "Exception occured while deleting files from" + " the repository " + e.getMessage());
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) {
	// pre-condition validation
	if ((toolSessionId == null) || (toolContentId == null)) {
	    throw new SubmitFilesException(
		    "Fail to create a submission session" + " based on null toolSessionId or toolContentId");
	}

	SubmitFilesService.log.debug("Start to create submission session based on toolSessionId["
		+ toolSessionId.longValue() + "] and toolContentId[" + toolContentId.longValue() + "]");
	try {
	    SubmitFilesContent submitContent = getSubmitFilesContent(toolContentId);
	    if ((submitContent == null) || !toolContentId.equals(submitContent.getContentID())) {
		submitContent = new SubmitFilesContent();
		submitContent.setContentID(toolContentId);
	    }
	    SubmitFilesSession submitSession = new SubmitFilesSession();

	    submitSession.setSessionID(toolSessionId);
	    submitSession.setSessionName(toolSessionName);
	    submitSession.setStatus(new Integer(SubmitFilesSession.INCOMPLETE));
	    submitSession.setContent(submitContent);
	    submitFilesSessionDAO.createSession(submitSession);
	    SubmitFilesService.log.debug("Submit File session created");
	} catch (DataAccessException e) {
	    throw new SubmitFilesException(
		    "Exception occured when lams is creating" + " a submission Session: " + e.getMessage(), e);
	}

    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    SubmitFilesService.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    SubmitFilesService.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(toolSessionId);
	if (session != null) {
	    session.setStatus(new Integer(SubmitFilesSession.COMPLETED));
	    submitFilesSessionDAO.update(session);
	} else {
	    SubmitFilesService.log.error("Fail to leave tool Session.Could not find submit file "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find submit file session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    SubmitFilesService.log.error("Fail to remove tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}

	SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(toolSessionId);
	if (session != null) {
	    removeToolSession(session);
	} else {
	    SubmitFilesService.log.error("Could not find submit file session by given session id: " + toolSessionId);
	    throw new DataMissingException("Could not find submit file session by given session id: " + toolSessionId);
	}
    }

    private void removeToolSession(SubmitFilesSession session) {
	Set filesUploaded = session.getSubmissionDetails();
	if (filesUploaded != null) {
	    Iterator fileIterator = filesUploaded.iterator();
	    while (fileIterator.hasNext()) {
		SubmissionDetails details = (SubmissionDetails) fileIterator.next();
		deleteFromRepository(details.getUuid(), details.getVersionID());
		submissionDetailsDAO.delete(details);
	    }
	}
	submitFilesSessionDAO.delete(session);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return getSubmitFilesOutputFactory().getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return getSubmitFilesOutputFactory().getToolOutput(name, this, toolSessionId, learnerId);
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
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    @Override
    public void uploadFileToSession(Long sessionID, MultipartFile file, String fileDescription, Integer userID)
	    throws SubmitFilesException {

	if ((file == null) || StringUtils.isEmpty(file.getName())) {
	    throw new SubmitFilesException("Could not find upload file: " + file);
	}

	SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(sessionID);
	if (session == null) {
	    throw new SubmitFilesException("No such session with a sessionID of: " + sessionID + " found.");
	}

	NodeKey nodeKey = processFile(file);

	SubmissionDetails details = new SubmissionDetails();
	details.setFileDescription(fileDescription);
	details.setFilePath(file.getName());
	details.setDateOfSubmission(new Date());

	SubmitUser learner = submitUserDAO.getLearner(sessionID, userID);
	details.setLearner(learner);
	details.setUuid(nodeKey.getUuid());
	details.setVersionID(nodeKey.getVersion());
	SubmitFilesReport report = new SubmitFilesReport();
	details.setReport(report);
	details.setSubmitFileSession(session);

	// update session, then insert the detail too.
	Set detailSet = session.getSubmissionDetails();
	detailSet.add(details);
	session.setSubmissionDetails(detailSet);
	submissionDetailsDAO.saveOrUpdate(session);

    }

    /**
     * Process an uploaded file.
     *
     * @param forumForm
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(MultipartFile file) {
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(file.getName())) {
	    String fileName = file.getName();
	    try {
		node = getSbmtToolContentHandler().uploadFile(file.getInputStream(), fileName, file.getContentType());
	    } catch (InvalidParameterException e) {
		throw new SubmitFilesException(
			"FileNotFoundException occured while trying to upload File" + e.getMessage());
	    } catch (FileNotFoundException e) {
		throw new SubmitFilesException(
			"FileNotFoundException occured while trying to upload File" + e.getMessage());
	    } catch (RepositoryCheckedException e) {
		throw new SubmitFilesException(
			"FileNotFoundException occured while trying to upload File" + e.getMessage());
	    } catch (IOException e) {
		throw new SubmitFilesException(
			"FileNotFoundException occured while trying to upload File" + e.getMessage());
	    }
	}
	return node;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getFilesUploadedByUserForContent(java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public List getFilesUploadedByUser(Integer userID, Long sessionID, Locale currentLocale,
	    boolean includeRemovedFiles) {
	List<SubmissionDetails> list = submissionDetailsDAO.getBySessionAndLearner(sessionID, userID);
	SortedSet details = new TreeSet(this.new FileDtoComparator());
	if (list == null) {
	    return new ArrayList(details);
	}

	NumberFormat numberFormat = currentLocale != null ? NumberFormat.getInstance(currentLocale) : null;
	for (SubmissionDetails submissionDetails : list) {
	    if (includeRemovedFiles || !submissionDetails.isRemoved()) {
		FileDetailsDTO detailDto = new FileDetailsDTO(submissionDetails, numberFormat);
		details.add(detailDto);
	    }
	}
	return new ArrayList(details);
    }

    
    @Override
    public SubmissionDetails getSubmissionDetail(Long detailId) {
	return submissionDetailsDAO.getSubmissionDetailsByID(detailId);
    }

    /**
     * This method save SubmissionDetails list into a map container: key is user id, value is a list container, which
     * contains all <code>FileDetailsDTO</code> object belong to this user.
     */
    @Override
    public SortedMap getFilesUploadedBySession(Long sessionID, Locale currentLocale) {
	List list = submissionDetailsDAO.getSubmissionDetailsBySession(sessionID);
	if (list != null) {
	    SortedMap map = new TreeMap(new LastNameAlphabeticComparator());
	    Iterator iterator = list.iterator();
	    List userFileList;
	    NumberFormat numberFormat = currentLocale != null ? NumberFormat.getInstance(currentLocale) : null;
	    while (iterator.hasNext()) {
		SubmissionDetails submissionDetails = (SubmissionDetails) iterator.next();
		SubmitUser learner = submissionDetails.getLearner();
		if (learner == null) {
		    SubmitFilesService.log
			    .error("Could not find learer for special submission item:" + submissionDetails);
		    return null;
		}
		SubmitUserDTO submitUserDTO = new SubmitUserDTO(learner);

		FileDetailsDTO detailDto = new FileDetailsDTO(submissionDetails, numberFormat);
		userFileList = (List) map.get(submitUserDTO);
		// if it is first time to this user, creating a new ArrayList for this user.
		if (userFileList == null) {
		    userFileList = new ArrayList();
		}
		userFileList.add(detailDto);
		map.put(submitUserDTO, userFileList);
	    }
	    return map;
	} else {
	    return null;
	}
    }

    @Override
    public FileDetailsDTO getFileDetails(Long detailID, Locale currentLocale) {
	SubmissionDetails details = submissionDetailsDAO.getSubmissionDetailsByID(detailID);
	return new FileDetailsDTO(details, currentLocale != null ? NumberFormat.getInstance(currentLocale) : null);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getUsersBySession(java.lang.Long)
     */
    @Override
    public List<SubmitUser> getUsersBySession(Long sessionID) {
	return submitUserDAO.getUsersBySession(sessionID);
    }

    @Override
    public void updateMarks(Long reportID, Float marks, String comments, MultipartFile markFile, Long sessionID)
	    throws InvalidParameterException, RepositoryCheckedException {

	SubmitFilesSession session = getSessionById(sessionID);
	SubmitFilesContent content = session.getContent();
	if (content.isUseSelectLeaderToolOuput()) {

	    // can share the mark file across users
	    NodeKey nodeKey = null;
	    if ((markFile != null) && !StringUtils.isEmpty(markFile.getName())) {
		nodeKey = this.processFile(markFile);
	    }

	    List<Long> reportIDs = submitUserDAO.getReportsForGroup(sessionID, reportID);
	    for (Long reportIDGroup : reportIDs) {

		IToolContentHandler toolContentHandler = getSbmtToolContentHandler();
		SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportIDGroup);
		if (report != null) {
		    report.setComments(comments);
		    report.setMarks(marks);

		    // If there is a new file, delete the existing and add the mark file
		    if ( nodeKey != null) {

			// Delete the existing
			if (report.getMarkFileUUID() != null) {
			    toolContentHandler.deleteFile(report.getMarkFileUUID());
			    report.setMarkFileName(null);
			    report.setMarkFileUUID(null);
			    report.setMarkFileVersionID(null);
			}

			report.setMarkFileName(markFile.getName());
			report.setMarkFileUUID(nodeKey.getUuid());
			report.setMarkFileVersionID(nodeKey.getVersion());
		    }

		    submitFilesReportDAO.update(report);
		}

	    }

	} else {
	    IToolContentHandler toolContentHandler = getSbmtToolContentHandler();
	    SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportID);
	    if (report != null) {
		report.setComments(comments);
		report.setMarks(marks);

		// If there is a new file, delete the existing and add the mark file
		if ((markFile != null) && !StringUtils.isEmpty(markFile.getName())) {

		    // Delete the existing
		    if (report.getMarkFileUUID() != null) {
			toolContentHandler.deleteFile(report.getMarkFileUUID());
			report.setMarkFileName(null);
			report.setMarkFileUUID(null);
			report.setMarkFileVersionID(null);
		    }

		    // Add the new file
		    NodeKey nodeKey = this.processFile(markFile);
		    // NodeKey nodeKey = toolContentHandler.uploadFile(marksFileInputStream, marksFileName, null,
		    // IToolContentHandler.TYPE_ONLINE);

		    report.setMarkFileName(markFile.getName());
		    report.setMarkFileUUID(nodeKey.getUuid());
		    report.setMarkFileVersionID(nodeKey.getVersion());
		}

		submitFilesReportDAO.update(report);
	    }
	}

    }

    @Override
    public void removeMarkFile(Long reportID, Long markFileUUID, Long markFileVersionID, Long sessionID) {

	SubmitFilesSession session = getSessionById(sessionID);
	SubmitFilesContent content = session.getContent();

	if (content.isUseSelectLeaderToolOuput()) {
	    List<Long> reportIDs = submitUserDAO.getReportsForGroup(sessionID, reportID);
	    for (Long reportIDGroup : reportIDs) {
		SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportIDGroup);
		if (report != null) {
		    report.setMarkFileName(null);
		    report.setMarkFileUUID(null);
		    report.setMarkFileVersionID(null);
		    submitFilesReportDAO.update(report);
		}
	    }
	    
	} else {
	    SubmitFilesReport report = submitFilesReportDAO.getReportByID(reportID);
	    if (report != null) {
		report.setMarkFileName(null);
		report.setMarkFileUUID(null);
		report.setMarkFileVersionID(null);
		submitFilesReportDAO.update(report);
	    }
	}

	deleteFromRepository(markFileUUID, markFileVersionID);

    }

    @Override
    public void removeLearnerFile(Long detailID, UserDTO monitor) {

	SubmissionDetails detail = submissionDetailsDAO.getSubmissionDetailsByID(detailID);

	if (detail != null) {
	    if (monitor != null) {

		auditRemoveRestore(monitor, detail, "audit.file.delete");
	    }

	    detail.setRemoved(true);
	    submissionDetailsDAO.update(detail);
	}
    }

    @Override
    public void restoreLearnerFile(Long detailID, UserDTO monitor) {

	SubmissionDetails detail = submissionDetailsDAO.getSubmissionDetailsByID(detailID);

	if (detail != null) {

	    auditRemoveRestore(monitor, detail, "audit.file.restore");

	    detail.setRemoved(false);
	    submissionDetailsDAO.update(detail);
	}

    }

    private void auditRemoveRestore(UserDTO monitor, SubmissionDetails detail, String i18nKey) {
	SubmitUser learner = detail.getLearner();
	StringBuilder instructorTxt = new StringBuilder(monitor.getLogin()).append(" (").append(monitor.getUserID())
		.append(") ");
	StringBuilder learnerTxt = new StringBuilder(learner.getLogin()).append("  (").append(learner.getUserID())
		.append(") ");

	String auditMsg = getLocalisedMessage(i18nKey,
		new Object[] { instructorTxt.toString(), detail.getFilePath(), learnerTxt.toString() });
	Long toolContentId = null;
	if (detail.getSubmitFileSession() != null && detail.getSubmitFileSession().getContent() != null) {
	    toolContentId = detail.getSubmitFileSession().getContent().getContentID();
	}
	logEventService.logChangeLearnerArbitraryChange(learner.getUserID().longValue(), learner.getLogin(),
		toolContentId, auditMsg);
    }

    @Override
    public IVersionedNode downloadFile(Long uuid, Long versionID) throws SubmitFilesException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    IVersionedNode node = repositoryService.getFileItem(ticket, uuid, null);
	    return node;
	} catch (AccessDeniedException ae) {
	    throw new SubmitFilesException(
		    "AccessDeniedException occured while trying to download file " + ae.getMessage());
	} catch (FileException fe) {
	    throw new SubmitFilesException("FileException occured while trying to download file " + fe.getMessage());
	} catch (ItemNotFoundException ie) {
	    throw new SubmitFilesException(
		    "ItemNotFoundException occured while trying to download file " + ie.getMessage());
	}
    }

    @Override
    public SubmitFilesSession getSessionById(Long sessionID) {
	return submitFilesSessionDAO.getSessionByID(sessionID);
    }

    @Override
    public boolean releaseMarksForSession(Long sessionID) {
	List list = submissionDetailsDAO.getSubmissionDetailsBySession(sessionID);
	Iterator iter = list.iterator();
	SubmissionDetails details;
	SubmitFilesReport report;
	SubmitFilesSession session = getSessionById(sessionID);

	SubmitFilesContent content = session.getContent();
	boolean notifyLearnersOnMarkRelease = getEventNotificationService().eventExists(SbmtConstants.TOOL_SIGNATURE,
		SbmtConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, content.getContentID());
	Map<Integer, StringBuilder> notificationMessages = null;
	Object[] notificationMessageParameters = null;
	if (notifyLearnersOnMarkRelease) {
	    notificationMessages = new TreeMap<>();
	    notificationMessageParameters = new Object[3];
	}
	while (iter.hasNext()) {
	    details = (SubmissionDetails) iter.next();
	    report = details.getReport();
	    report.setDateMarksReleased(new Date());
	    if (notifyLearnersOnMarkRelease && !details.isRemoved()) {
		SubmitUser user = details.getLearner();
		StringBuilder notificationMessage = notificationMessages.get(user.getUserID());
		if (notificationMessage == null) {
		    notificationMessage = new StringBuilder();
		}
		notificationMessageParameters[0] = details.getFilePath();
		notificationMessageParameters[1] = details.getDateOfSubmission();
		notificationMessageParameters[2] = report.getMarks();
		notificationMessage
			.append(getLocalisedMessage("event.mark.release.mark", notificationMessageParameters));
		notificationMessages.put(user.getUserID(), notificationMessage);
	    }
	    submitFilesReportDAO.updateReport(report);
	}
	// current there is no false return
	if (notifyLearnersOnMarkRelease) {
	    notificationMessageParameters = new Object[1];
	    for (Integer userID : notificationMessages.keySet()) {
		notificationMessageParameters[0] = notificationMessages.get(userID).toString();
		getEventNotificationService().triggerForSingleUser(SbmtConstants.TOOL_SIGNATURE,
			SbmtConstants.EVENT_NAME_NOTIFY_LEARNERS_ON_MARK_RELEASE, content.getContentID(), userID,
			notificationMessageParameters);
	    }
	}

	// pushing outputs to gradebook
	List<SubmitUser> users = getUsersBySession(sessionID);

	if (users != null) {
	    for (SubmitUser user : users) {
		// send marks to gradebook where applicable
		sendMarksToGradebook(user, sessionID);
	    }
	}

	return true;
    }

    @Override
    public void finishSubmission(Long sessionID, Integer userID) {
	SubmitUser learner = submitUserDAO.getLearner(sessionID, userID);
	learner.setFinished(true);
	submitUserDAO.saveOrUpdateUser(learner);

	SubmitFilesContent content = getSessionById(sessionID).getContent();

	if (content.isUseSelectLeaderToolOuput()) {
	    SubmitFilesSession sbmtFilesSession = submitFilesSessionDAO.getSessionByID(sessionID);
	    if (sbmtFilesSession.getGroupLeader().getUserID().equals(learner.getUserID())) {
		sbmtFilesSession.setGroupLeader(learner);
		submitFilesSessionDAO.insertOrUpdate(sbmtFilesSession);
	    }
	}
    }

    /**
     * Sends marks to gradebook
     *
     * @param user
     * @param toolSessionID
     */
    @SuppressWarnings("unchecked")
    public void sendMarksToGradebook(SubmitUser user, Long toolSessionID) {
	submissionDetailsDAO.getSubmissionDetailsBySession(toolSessionID);
	List<SubmissionDetails> detailsList = submissionDetailsDAO.getBySessionAndLearner(toolSessionID,
		user.getUserID());
	if (detailsList != null) {
	    Float totalMark = null;
	    for (SubmissionDetails details : detailsList) {
		if (!details.isRemoved()) {
		    SubmitFilesReport report = details.getReport();
		    if (report != null) {
			if (totalMark == null) {
			    totalMark = details.getReport().getMarks();
			} else if (report.getMarks() != null) {
			    totalMark += report.getMarks();
			}
		    }
		}
	    }
	    if (totalMark != null) {
		Double mark = new Double(totalMark);
		gradebookService.updateActivityMark(mark, null, user.getUserID(), toolSessionID, false);
	    }
	}
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#getToolDefaultContentIdBySignature(java.lang.Long)
     */
    @Override
    public Long getToolDefaultContentIdBySignature(String toolSignature) {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = "Could not retrieve default content id for this tool";
	    SubmitFilesService.log.error(error);
	    throw new SubmitFilesException(error);
	}
	return contentId;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService#createDefaultContent(java.lang.Long)
     */
    @Override
    public SubmitFilesContent createDefaultContent(Long contentID) {
	if (contentID == null) {
	    String error = "Could not retrieve default content id for this tool";
	    SubmitFilesService.log.error(error);
	    throw new SubmitFilesException(error);
	}
	SubmitFilesContent defaultContent = getDefaultSubmit();

	// save default content by given ID.
	SubmitFilesContent content = new SubmitFilesContent();
	content = SubmitFilesContent.newInstance(defaultContent, contentID);
	content.setContentID(contentID);

	return content;
    }

    private SubmitFilesContent getDefaultSubmit() {
	Long defaultToolContentId = getToolDefaultContentIdBySignature(SbmtConstants.TOOL_SIGNATURE);
	SubmitFilesContent defaultContent = getSubmitFilesContent(defaultToolContentId);
	if (defaultContent == null) {
	    String error = "Could not retrieve default content record for this tool";
	    SubmitFilesService.log.error(error);
	    throw new SubmitFilesException(error);
	}
	return defaultContent;
    }

    @Override
    public List getSubmitFilesSessionByContentID(Long contentID) {
	List learners = submitFilesSessionDAO.getSubmitFilesSessionByContentID(contentID);
	if (learners == null) {
	    learners = new ArrayList(); // return sized 0 list rather than null value
	}
	return learners;
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
    public SubmitUser getUserByUid(Long learnerID) {
	return (SubmitUser) submitUserDAO.find(SubmitUser.class, learnerID);

    }

    @Override
    public SubmitUser createSessionUser(UserDTO userDto, Long sessionID) {
	SubmitUser learner = submitUserDAO.getLearner(sessionID, userDto.getUserID());
	if (learner != null) {
	    return learner;
	}
	learner = new SubmitUser();
	learner.setUserID(userDto.getUserID());
	learner.setFirstName(userDto.getFirstName());
	learner.setLastName(userDto.getLastName());
	learner.setLogin(userDto.getLogin());
	learner.setSessionID(sessionID);
	learner.setFinished(false);

	submitUserDAO.saveOrUpdateUser(learner);

	return learner;
    }

    @Override
    public SubmitUser getSessionUser(Long sessionID, Integer userID) {
	return submitUserDAO.getLearner(sessionID, userID);
    }

    @Override
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries) {
	return submitUserDAO.getUsersForTablesorter(sessionId, page, size, sorting, searchString, getNotebookEntries,
		coreNotebookService, userManagementService);
    }

    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	return submitUserDAO.getCountUsersBySession(sessionId, searchString);
    }

    @Override
    public List<StatisticDTO> getStatisticsBySession(final Long contentId) {
	return submitUserDAO.getStatisticsBySession(contentId);
    }

    @Override
    public List<StatisticDTO> getLeaderStatisticsBySession(final Long contentId) {
	return submitUserDAO.getLeaderStatisticsBySession(contentId);
    }

    public SubmitUser createContentUser(Integer userId, String firstName, String lastName, String loginName,
	    Long contentId) {
	SubmitUser author = submitUserDAO.getContentUser(contentId, userId);
	if (author != null) {
	    return author;
	}
	author = new SubmitUser();
	author.setUserID(userId);
	author.setFirstName(firstName);
	author.setLastName(lastName);
	author.setLogin(loginName);
	author.setContentID(contentId);
	author.setFinished(false);

	submitUserDAO.saveOrUpdateUser(author);
	return author;

    }

    @Override
    public SubmitUser createContentUser(UserDTO userDto, Long contentId) {
	SubmitUser learner = submitUserDAO.getContentUser(contentId, userDto.getUserID());
	if (learner != null) {
	    return learner;
	}
	learner = new SubmitUser();
	learner.setUserID(userDto.getUserID());
	learner.setFirstName(userDto.getFirstName());
	learner.setLastName(userDto.getLastName());
	learner.setLogin(userDto.getLogin());
	learner.setContentID(contentId);
	learner.setFinished(false);

	submitUserDAO.saveOrUpdateUser(learner);
	return learner;

    }

    @Override
    public SubmitUser getContentUser(Long contentId, Integer userID) {
	return submitUserDAO.getContentUser(contentId, userID);
    }

    /*******************************************************************************************************************
     * Property Injection Methods
     ******************************************************************************************************************/

    /**
     * @param submitFilesContentDAO
     *            The submitFilesContentDAO to set.
     */
    public void setSubmitFilesContentDAO(ISubmitFilesContentDAO submitFilesContentDAO) {
	this.submitFilesContentDAO = submitFilesContentDAO;
    }

    /**
     * @param submitFilesReportDAO
     *            The submitFilesReportDAO to set.
     */
    public void setSubmitFilesReportDAO(ISubmitFilesReportDAO submitFilesReportDAO) {
	this.submitFilesReportDAO = submitFilesReportDAO;
    }

    /**
     * @param submitFilesSessionDAO
     *            The submitFilesSessionDAO to set.
     */
    public void setSubmitFilesSessionDAO(ISubmitFilesSessionDAO submitFilesSessionDAO) {
	this.submitFilesSessionDAO = submitFilesSessionDAO;
    }

    /**
     * @param submissionDetailsDAO
     *            The submissionDetailsDAO to set.
     */
    public void setSubmissionDetailsDAO(ISubmissionDetailsDAO submissionDetailsDAO) {
	this.submissionDetailsDAO = submissionDetailsDAO;
    }

    /**
     * @return Returns the sbmtToolContentHandler.
     */
    public IToolContentHandler getSbmtToolContentHandler() {
	return sbmtToolContentHandler;
    }

    /**
     * @param sbmtToolContentHandler
     *            The sbmtToolContentHandler to set.
     */
    public void setSbmtToolContentHandler(IToolContentHandler sbmtToolContentHandler) {
	this.sbmtToolContentHandler = sbmtToolContentHandler;
    }

    /**
     * @return Returns the learnerDAO.
     */
    public ISubmitUserDAO getSubmitUserDAO() {
	return submitUserDAO;
    }

    /**
     * @param learnerDAO
     *            The learnerDAO to set.
     */
    public void setSubmitUserDAO(ISubmitUserDAO learnerDAO) {
	submitUserDAO = learnerDAO;
    }

    public ILearnerService getLearnerService() {
	return learnerService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public IRepositoryService getRepositoryService() {
	return repositoryService;
    }

    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    public ILamsToolService getToolService() {
	return toolService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
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

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    @Override
    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    @Override
    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public ILogEventService getLogEventService() {
	return logEventService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getSubmitFilesOutputFactory().getSupportedDefinitionClasses(definitionType);

    }

    public SubmitFilesOutputFactory getSubmitFilesOutputFactory() {
	return submitFilesOutputFactory;
    }

    public void setSubmitFilesOutputFactory(SubmitFilesOutputFactory submitFilesOutputFactory) {
	this.submitFilesOutputFactory = submitFilesOutputFactory;
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	SubmitUser learner = getSessionUser(toolSessionId, learnerId.intValue());
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Date startDate = null;
	Date endDate = null;
	List<SubmissionDetails> list = submissionDetailsDAO.getBySessionAndLearner(toolSessionId, learnerId.intValue());
	for (SubmissionDetails detail : list) {
	    Date newDate = detail.getDateOfSubmission();
	    if (newDate != null) {
		if (startDate == null || newDate.before(startDate)) {
		    startDate = newDate;
		}
		if (endDate == null || newDate.after(endDate)) {
		    endDate = newDate;
		}
	    }
	}

	if (learner.isFinished()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, endDate);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
	}
    }

    // ****************** REST methods *************************

    /**
     * Used by the Rest calls to create content. Mandatory fields in toolContentJSON: title, instructions
     */
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON) {

	SubmitFilesContent content = new SubmitFilesContent();
	Date updateDate = new Date();
	content.setCreated(updateDate);
	content.setUpdated(updateDate);

	content.setContentID(toolContentID);
	content.setTitle(JsonUtil.optString(toolContentJSON, RestTags.TITLE));
	content.setInstruction(JsonUtil.optString(toolContentJSON, RestTags.INSTRUCTIONS));

	content.setContentInUse(false);
	content.setDefineLater(false);
	content.setNotifyTeachersOnFileSubmit(
		JsonUtil.optBoolean(toolContentJSON, "notifyTeachersOnFileSubmit", Boolean.FALSE));
	content.setNotifyLearnersOnMarkRelease(
		JsonUtil.optBoolean(toolContentJSON, "notifyLearnersOnMarkRelease", Boolean.FALSE));
	content.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	content.setReflectOnActivity(JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	content.setLockOnFinished(JsonUtil.optBoolean(toolContentJSON, RestTags.LOCK_WHEN_FINISHED, Boolean.FALSE));
	content.setLimitUpload(JsonUtil.optBoolean(toolContentJSON, "limitUpload", Boolean.FALSE));
	content.setUseSelectLeaderToolOuput(
		JsonUtil.optBoolean(toolContentJSON, "useSelectLeaderToolOuput", Boolean.FALSE));
	content.setLimitUploadNumber(JsonUtil.optInt(toolContentJSON, "limitUploadNumber", 0));
	// submissionDeadline is set in monitoring

	SubmitUser user = getContentUser(toolContentID, userID);
	if (user == null) {
	    user = createContentUser(userID, JsonUtil.optString(toolContentJSON, "firstName"),
		    JsonUtil.optString(toolContentJSON, "lastName"), JsonUtil.optString(toolContentJSON, "loginName"),
		    toolContentID);
	}
	content.setCreatedBy(user);
	saveOrUpdateContent(content);

    }

    @Override
    public SubmitUser checkLeaderSelectToolForSessionLeader(SubmitUser user, Long toolSessionId) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}

	SubmitFilesSession submitFileSession = getSessionById(toolSessionId);

	SubmitUser leader = submitFileSession.getGroupLeader();
	// check leader select tool for a leader only in case QA tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {
	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getUserID().intValue());
	    if (leaderUserId != null) {
		leader = submitUserDAO.getLearner(toolSessionId, leaderUserId.intValue());
		// create new user in a DB
		if (leader == null) {
		    SubmitFilesService.log.debug("creating new user with userId: " + leaderUserId);
		    User leaderDto = (User) getUserManagementService().findById(User.class, leaderUserId.intValue());
		    String userName = leaderDto.getLogin();
		    String fullName = leaderDto.getFirstName() + " " + leaderDto.getLastName();
		    //  leader = new SubmitUser(leaderDto.getUserDTO(), submitFileSession);
		    leader = new SubmitUser();
		    leader.setLogin(leaderDto.getLogin());
		    leader.setFirstName(leaderDto.getFirstName());
		    leader.setLastName(leaderDto.getLastName());
		    leader.setUserID(leaderDto.getUserId());

		    createUser(leader);
		}

		// set group leader
		submitFileSession.setGroupLeader(leader);
		submitFilesSessionDAO.insertOrUpdate(submitFileSession);
	    }
	}

	return leader;
    }

    @Override
    public void createUser(SubmitUser submitUser) {
	// make sure the user was not created in the meantime
	SubmitUser user = submitUserDAO.getLearner(submitUser.getSessionID(), submitUser.getUserID());
	if (user == null) {
	    user = submitUser;
	}
	// Save it no matter if the user already exists.
	// At checkLeaderSelectToolForSessionLeader() the user is added to session.
	// Sometimes session save is earlier that user save in another thread, leading to an exception.
	submitUserDAO.insertOrUpdate(user);
    }

    @Override
    public boolean isUserGroupLeader(SubmitUser user, Long toolSessionId) {

	SubmitFilesSession session = submitFilesSessionDAO.getSessionByID(toolSessionId);
	SubmitUser groupLeader = session.getGroupLeader();

	return (groupLeader != null) && user.getUserID().equals(groupLeader.getUserID());
    }


}