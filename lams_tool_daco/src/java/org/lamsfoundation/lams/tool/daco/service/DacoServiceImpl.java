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

package org.lamsfoundation.lams.tool.daco.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoQuestionDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoSessionDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoUserDAO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummaryUserDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummarySingleAnswerDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.util.DacoToolContentHandler;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

/**
 * @author Dapeng.Ni
 */
public class DacoServiceImpl implements IDacoService, ToolContentManager, ToolSessionManager {
    private static Logger log = Logger.getLogger(DacoServiceImpl.class.getName());

    private DacoDAO dacoDao;

    private DacoQuestionDAO dacoQuestionDao;

    private DacoUserDAO dacoUserDao;

    private DacoSessionDAO dacoSessionDao;

    private DacoAnswerDAO dacoAnswerDao;

    // tool service
    private DacoToolContentHandler dacoToolContentHandler;

    private MessageService messageService;

    // system services
    private IRepositoryService repositoryService;

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private DacoOutputFactory dacoOutputFactory;

    @Override
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the Data Collection tool seession");
	}

	Daco daco = null;
	if (fromContentId != null) {
	    daco = dacoDao.getByContentId(fromContentId);
	}
	if (daco == null) {
	    try {
		daco = getDefaultDaco();
	    } catch (DacoApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Daco toContent = Daco.newInstance(daco, toContentId);
	dacoDao.saveObject(toContent);
    }

    @Override
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	DacoSession session = new DacoSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Daco daco = dacoDao.getByContentId(toolContentId);
	session.setDaco(daco);
	dacoSessionDao.saveObject(session);
    }

    @Override
    public void createUser(DacoUser dacoUser) {
	dacoUserDao.saveObject(dacoUser);
    }

    @Override
    public void deleteDacoAnswer(Long uid) {
	dacoAnswerDao.removeObject(DacoAnswer.class, uid);
    }

    @Override
    public void deleteDacoQuestion(Long uid) {
	dacoQuestionDao.removeObject(DacoQuestion.class, uid);
    }

    @Override
    public void deleteDacoRecord(List<DacoAnswer> record) {
	for (DacoAnswer answer : record) {
	    deleteDacoAnswer(answer.getUid());
	}
    }

    @Override
    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws DacoApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new DacoApplicationException(
		    "Exception occured while deleting files from" + " the repository " + e.getMessage());
	}
    }

    @Override
    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Daco toolContentObj = dacoDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultDaco();
	    } catch (DacoApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the daco tool");
	}

	// set DacoToolContentHandler as null to avoid copy file node in
	// repository again.
	toolContentObj = Daco.newInstance(toolContentObj, toolContentId);
	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, dacoToolContentHandler, rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws DacoApplicationException {
	DacoUser user = dacoUserDao.getUserByUserIdAndSessionId(userId, toolSessionId);
	user.setSessionFinished(true);
	dacoUserDao.saveObject(user);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new DacoApplicationException(e);
	} catch (ToolException e) {
	    throw new DacoApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public List<List<DacoAnswer>> getDacoAnswersByUser(DacoUser user) {
	Set<DacoAnswer> answers = user.getAnswers();
	List<List<DacoAnswer>> result = new LinkedList<List<DacoAnswer>>();
	if ((answers != null) && (answers.size() > 0)) {

	    int recordId = 1;

	    List<DacoAnswer> record = new LinkedList<DacoAnswer>();
	    for (DacoAnswer answer : answers) {
		if (recordId != answer.getRecordId()) {
		    recordId = answer.getRecordId();
		    /*
		     * LDEV-3771: need to check we aren't adding a blank record
		     * if there isn't a record for record id 1 due to deletion.
		     */
		    if (record.size() > 0) {
			result.add(record);
		    }
		    record = new LinkedList<DacoAnswer>();
		}
		record.add(answer);
	    }
	    result.add(record);
	}
	return result;
    }

    @Override
    public Daco getDacoByContentId(Long contentId) {
	Daco daco = dacoDao.getByContentId(contentId);
	return daco;
    }

    @Override
    public Daco getDacoBySessionId(Long sessionId) {
	DacoSession session = dacoSessionDao.getSessionBySessionId(sessionId);
	return session.getDaco();
    }

    @Override
    public DacoQuestion getDacoQuestionByUid(Long questionUid) {
	return dacoQuestionDao.getByUid(questionUid);
    }

    @Override
    public Daco getDefaultContent(Long contentId) throws DacoApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DacoServiceImpl.log.error(error);
	    throw new DacoApplicationException(error);
	}

	Daco defaultContent = getDefaultDaco();
	// save default content by given ID.
	Daco content = new Daco();
	content = Daco.newInstance(defaultContent, contentId);
	return content;
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Daco getDefaultDaco() throws DacoApplicationException {
	Long defaultDacoId = getToolDefaultContentIdBySignature(DacoConstants.TOOL_SIGNATURE);
	Daco defaultDaco = getDacoByContentId(defaultDacoId);
	if (defaultDaco == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DacoServiceImpl.log.error(error);
	    throw new DacoApplicationException(error);
	}

	return defaultDaco;
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
    public void uploadDacoAnswerFile(DacoAnswer answer, FormFile file) throws UploadDacoFileException {
	try {
	    InputStream is = file.getInputStream();
	    String fileName = file.getFileName();
	    String fileType = file.getContentType();
	    // For file only upload one sigle file
	    if ((answer.getQuestion().getType() == DacoConstants.QUESTION_TYPE_FILE)
		    || (answer.getQuestion().getType() == DacoConstants.QUESTION_TYPE_IMAGE)) {
		NodeKey nodeKey = processFile(file);
		answer.setFileUuid(nodeKey.getUuid());
		answer.setFileVersionId(nodeKey.getVersion());
	    }

	    // create the package from the directory contents
	    answer.setFileType(fileType);
	    answer.setFileName(fileName);
	} catch (FileNotFoundException e) {
	    DacoServiceImpl.log.error(messageService.getMessage("error.msg.file.not.found") + ":" + e.toString());
	    throw new UploadDacoFileException(messageService.getMessage("error.msg.file.not.found"));
	} catch (IOException e) {
	    DacoServiceImpl.log.error(messageService.getMessage("error.msg.io.exception") + ":" + e.toString());
	    throw new UploadDacoFileException(messageService.getMessage("error.msg.io.exception"));
	}
    }

    // *******************************************************************************
    // Service method
    // *******************************************************************************
    /**
     * Try to get the file. If forceLogin = false and an access denied exception occurs, call this method again to get a
     * new ticket and retry file lookup. If forceLogin = true and it then fails then throw exception.
     *
     * @param uuid
     * @param versionId
     * @param relativePath
     * @param attemptCount
     * @return file node
     * @throws ImscpApplicationException
     */
    private IVersionedNode getFile(Long uuid, Long versionId, String relativePath) throws DacoApplicationException {

	ITicket tic = getRepositoryLoginTicket();

	try {

	    return repositoryService.getFileItem(tic, uuid, versionId, relativePath);

	} catch (AccessDeniedException e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + ".";

	    error = error + "AccessDeniedException: " + e.getMessage() + " Unable to retry further.";
	    DacoServiceImpl.log.error(error);
	    throw new DacoApplicationException(error, e);

	} catch (Exception e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + "." + " Exception: " + e.getMessage();
	    DacoServiceImpl.log.error(error);
	    throw new DacoApplicationException(error, e);

	}
    }

    @Override
    public IVersionedNode getFileNode(Long answerUid, String relPathString) throws DacoApplicationException {
	DacoAnswer answer = (DacoAnswer) dacoAnswerDao.getObject(DacoQuestion.class, answerUid);
	if (answer == null) {
	    throw new DacoApplicationException("Reource question " + answerUid + " not found.");
	}

	return getFile(answer.getFileUuid(), answer.getFileVersionId(), relPathString);
    }

    @Override
    public Integer getGroupRecordCount(Long sessionId) {
	List<DacoUser> users = dacoUserDao.getBySessionId(sessionId);

	Integer groupRecordCount = 0;
	for (DacoUser user : users) {
	    groupRecordCount += dacoAnswerDao.getUserRecordCount(user.getUserId(), sessionId);
	}
	return groupRecordCount;
    }

    @Override
    public Integer getGroupRecordCount(MonitoringSummarySessionDTO monitoringSummary) {
	if (monitoringSummary == null) {
	    return null;
	}
	int groupRecordCount = 0;
	for (MonitoringSummaryUserDTO user : monitoringSummary.getUsers()) {
	    groupRecordCount += user.getRecords().size();
	}
	return groupRecordCount;
    }

    @Override
    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    @Override
    public List<MonitoringSummarySessionDTO> getMonitoringSummary(Long contentId, Long userUid) {
	List<DacoSession> sessions = dacoSessionDao.getByContentId(contentId);
	List<MonitoringSummarySessionDTO> result = new ArrayList<MonitoringSummarySessionDTO>(sessions.size());
	Daco daco = getDacoByContentId(contentId);
	for (DacoSession session : sessions) {
	    // for each session a monitoring summary is created but don't include users as the paging fetches them
	    MonitoringSummarySessionDTO monitoringRecordList = new MonitoringSummarySessionDTO(session.getSessionId(),
		    session.getSessionName());
	    result.add(monitoringRecordList);
	}
	return result;
    }

    @Override
    public List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting,
	    String searchString, boolean getNotebookEntries) {
	return dacoUserDao.getUsersForTablesorter(sessionId, page, size, sorting, searchString, getNotebookEntries,
		coreNotebookService);
    }

    @Override
    public int getCountUsersBySession(final Long sessionId, String searchString) {
	return dacoUserDao.getCountUsersBySession(sessionId, searchString);
    }

    @Override
    public List<MonitoringSummarySessionDTO> getSessionStatistics(Long toolContentUid) {
	return dacoSessionDao.statistics(toolContentUid);
    }

    @Override
    public MonitoringSummarySessionDTO getAnswersAsRecords(final Long sessionId, final Long userId, int sorting) {
	DacoSession session = dacoSessionDao.getSessionBySessionId(sessionId);
	MonitoringSummarySessionDTO monitoringRecordList = new MonitoringSummarySessionDTO(session.getSessionId(),
		session.getSessionName());

	List<MonitoringSummaryUserDTO> monitoringUsers = new ArrayList<MonitoringSummaryUserDTO>();
	if (userId == null) {
	    List<DacoUser> users = dacoUserDao.getBySessionId(sessionId, sorting);
	    for (DacoUser user : users) {
		monitoringUsers.add(getAnswersAsRecordsForUser(user));
	    }
	} else {
	    monitoringUsers.add(getAnswersAsRecordsForUser(getUserByUserIdAndSessionId(userId, sessionId)));
	}

	monitoringRecordList.setUsers(monitoringUsers);
	return monitoringRecordList;
    }

    // called by getAnswersAsRecords
    private MonitoringSummaryUserDTO getAnswersAsRecordsForUser(DacoUser user) {
	MonitoringSummaryUserDTO monitoringUser = new MonitoringSummaryUserDTO(user.getUid(),
		user.getUserId().intValue(), user.getFullName(), user.getLoginName());
	List<List<DacoAnswer>> records = getDacoAnswersByUser(user);
	monitoringUser.setRecords(records);
	monitoringUser.setRecordCount(records.size());
	return monitoringUser;
    }

    @Override
    public List<MonitoringSummarySessionDTO> getExportPortfolioSummary(Long contentId, Long userUid) {
	List<DacoSession> sessions = dacoSessionDao.getByContentId(contentId);
	List<MonitoringSummarySessionDTO> result = new ArrayList<MonitoringSummarySessionDTO>(sessions.size());
	Daco daco = getDacoByContentId(contentId);
	for (DacoSession session : sessions) {
	    // for each session a monitoring summary is created
	    MonitoringSummarySessionDTO monitoringRecordList = new MonitoringSummarySessionDTO(session.getSessionId(),
		    session.getSessionName());
	    List<DacoUser> users = dacoUserDao.getBySessionId(session.getSessionId());
	    List<MonitoringSummaryUserDTO> monitoringUsers = new ArrayList<MonitoringSummaryUserDTO>(users.size());
	    for (DacoUser user : users) {
		MonitoringSummaryUserDTO monitoringUser = new MonitoringSummaryUserDTO(user.getUid(),
			user.getUserId().intValue(), user.getFullName(), user.getLoginName());
		List<List<DacoAnswer>> records = getDacoAnswersByUser(user);
		/*
		 * If the user provided as "userUid" matches current user UID, the summary is filled with additional
		 * data. NULL matches all users. UID < 0 matches no users, so only the brief description of users is
		 * filled in.
		 */
		if ((userUid == null) || userUid.equals(user.getUid())) {
		    monitoringUser.setRecords(records);
		    NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			    DacoConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		    if (entry != null) {
			monitoringUser.setReflectionEntry(entry.getEntry());
		    }
		} else {
		    monitoringUser.setRecordCount(records.size());
		}
		monitoringUsers.add(monitoringUser);
	    }
	    monitoringRecordList.setUsers(monitoringUsers);
	    result.add(monitoringRecordList);
	}
	return result;
    }

    @Override
    public List<QuestionSummaryDTO> getQuestionSummaries(Long userUid) {
	List<QuestionSummaryDTO> result = new ArrayList<QuestionSummaryDTO>();
	DacoUser user = (DacoUser) dacoUserDao.getObject(DacoUser.class, userUid);
	// Blank structure is created
	Set<DacoQuestion> questions = user.getDaco().getDacoQuestions();
	if (questions.size() > 0) {
	    for (DacoQuestion question : questions) {
		switch (question.getType()) {
		    case DacoConstants.QUESTION_TYPE_NUMBER: {
			/*
			 * For numbers, first "single answer" is a summary for the whole question. Other
			 * "single answers"
			 * are summaries for the real answers provided by a learner.
			 */
			QuestionSummaryDTO summary = new QuestionSummaryDTO();
			summary.addUserSummarySingleAnswer(0, new QuestionSummarySingleAnswerDTO());
			summary.addGroupSummarySingleAnswer(0, new QuestionSummarySingleAnswerDTO());
			summary.setQuestionUid(question.getUid());
			result.add(summary);
		    }
			break;
		    case DacoConstants.QUESTION_TYPE_RADIO:
		    case DacoConstants.QUESTION_TYPE_DROPDOWN:
		    case DacoConstants.QUESTION_TYPE_CHECKBOX: {
			int answerOptionCount = question.getAnswerOptions().size();
			QuestionSummaryDTO summary = new QuestionSummaryDTO();
			summary.setQuestionUid(question.getUid());
			for (int answerOption = 0; answerOption < answerOptionCount; answerOption++) {
			    QuestionSummarySingleAnswerDTO singleAnswer = new QuestionSummarySingleAnswerDTO(
				    String.valueOf(answerOption + 1), null, "0%", "0");
			    summary.addUserSummarySingleAnswer(answerOption, singleAnswer);
			    singleAnswer = (QuestionSummarySingleAnswerDTO) singleAnswer.clone();
			    summary.addGroupSummarySingleAnswer(answerOption, singleAnswer);
			}
			result.add(summary);
		    }
			break;
		    default:
			result.add(null);
			break;
		}
	    }
	    result = dacoAnswerDao.getQuestionSummaries(userUid, result);
	}
	return result;
    }

    @Override
    public void notifyTeachersOnLearnerEntry(Long sessionId, DacoUser dacoUser) {
	String message = getLocalisedMessage("event.learnerentry.body", new Object[] { dacoUser.getFullName() });
	eventNotificationService.notifyLessonMonitors(sessionId, message, false);
    }

    @Override
    public void notifyTeachersOnRecordSumbit(Long sessionId, DacoUser dacoUser) {
	String message = getLocalisedMessage("event.recordsubmit.body", new Object[] { dacoUser.getFullName() });
	eventNotificationService.notifyLessonMonitors(sessionId, message, false);
    }

    /**
     * This method verifies the credentials of the Daco Tool and gives it the <code>Ticket</code> to login and access
     * the Content Repository.
     *
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     *
     * @return ITicket The ticket for repostory access
     * @throws DacoApplicationException
     */
    private ITicket getRepositoryLoginTicket() throws DacoApplicationException {
	ICredentials credentials = new SimpleCredentials(dacoToolContentHandler.getRepositoryUser(),
		dacoToolContentHandler.getRepositoryId());
	try {
	    ITicket ticket = repositoryService.login(credentials, dacoToolContentHandler.getRepositoryWorkspaceName());
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new DacoApplicationException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new DacoApplicationException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new DacoApplicationException("Login failed." + e.getMessage());
	}
    }

    @Override
    public DacoSession getSessionBySessionId(Long sessionId) {
	return dacoSessionDao.getSessionBySessionId(sessionId);
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws DacoApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    DacoServiceImpl.log.error(error);
	    throw new DacoApplicationException(error);
	}
	return contentId;
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return dacoOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return dacoOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	// no actions required
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Daco daco = getDacoByContentId(toolContentId);
	if (daco == null) {
	    try {
		daco = getDefaultDaco();
	    } catch (DacoApplicationException e) {
		DacoServiceImpl.log.error(e.getMessage());
	    }
	}
	return getDacoOutputFactory().getToolOutputDefinitions(daco, definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getDacoByContentId(toolContentId).getTitle();
    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getDacoByContentId(toolContentId).isDefineLater();
    }

    @Override
    public DacoUser getUser(Long uid) {
	return (DacoUser) dacoUserDao.getObject(DacoUser.class, uid);
    }

    @Override
    public DacoUser getUserByUserIdAndContentId(Long userId, Long contentId) {
	return dacoUserDao.getUserByUserIdAndContentId(userId, contentId);
    }

    @Override
    public DacoUser getUserByUserIdAndSessionId(Long userId, Long sessionId) {
	return dacoUserDao.getUserByUserIdAndSessionId(userId, sessionId);
    }

    /*
     * ===============Methods implemented from ToolContentImport102Manager ===============
     */
    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(DacoImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, dacoToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Daco)) {
		throw new ImportToolContentException(
			"Import Share daco tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Daco toolContentObj = (Daco) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    DacoUser user = dacoUserDao.getUserByUserIdAndContentId(new Long(newUserUid.longValue()), toolContentId);
	    if (user == null) {
		user = new DacoUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setDaco(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all dacoQuestion createBy user
	    Set<DacoQuestion> questions = toolContentObj.getDacoQuestions();
	    for (DacoQuestion question : questions) {
		question.setCreateBy(user);
	    }
	    dacoDao.saveObject(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    DacoServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    DacoServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	DacoSession session = dacoSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(DacoConstants.SESSION_COMPLETED);
	    dacoSessionDao.saveObject(session);
	} else {
	    DacoServiceImpl.log.error("Fail to leave tool Session.Could not find shared daco "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared daco session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    /**
     * Process an uploaded file.
     *
     * @throws DacoApplicationException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file) throws UploadDacoFileException {
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = dacoToolContentHandler.uploadFile(file.getInputStream(), fileName, file.getContentType());
	    } catch (InvalidParameterException e) {
		throw new UploadDacoFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	    } catch (FileNotFoundException e) {
		throw new UploadDacoFileException(messageService.getMessage("error.msg.file.not.found"));
	    } catch (RepositoryCheckedException e) {
		throw new UploadDacoFileException(messageService.getMessage("error.msg.repository"));
	    } catch (IOException e) {
		throw new UploadDacoFileException(messageService.getMessage("error.msg.io.exception"));
	    }
	}
	return node;
    }

    private NodeKey processPackage(String packageDirectory, String initFile) throws UploadDacoFileException {
	NodeKey node = null;
	try {
	    node = dacoToolContentHandler.uploadPackage(packageDirectory, initFile);
	} catch (InvalidParameterException e) {
	    throw new UploadDacoFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	} catch (RepositoryCheckedException e) {
	    throw new UploadDacoFileException(messageService.getMessage("error.msg.repository"));
	}
	return node;
    }

    @Override
    public void releaseAnswersFromCache(Collection<DacoAnswer> answers) {
	for (DacoAnswer answer : answers) {
	    dacoAnswerDao.releaseFromCache(answer);
	}
    }

    @Override
    public void releaseDacoFromCache(Daco daco) {
	dacoDao.releaseFromCache(daco);
	for (DacoQuestion question : daco.getDacoQuestions()) {
	    dacoQuestionDao.releaseFromCache(question);
	}
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Daco daco = dacoDao.getByContentId(toolContentId);
	if (daco == null) {
	    DacoServiceImpl.log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (DacoSession session : dacoSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, DacoConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	dacoDao.removeObject(Daco.class, daco.getUid());
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (DacoServiceImpl.log.isDebugEnabled()) {
	    DacoServiceImpl.log
		    .debug("Removing Daco data for user ID " + userId + " and toolContentId " + toolContentId);
	}
	List<DacoSession> sessions = dacoSessionDao.getByContentId(toolContentId);
	for (DacoSession session : sessions) {
	    DacoUser user = dacoUserDao.getUserByUserIdAndSessionId(userId.longValue(), session.getSessionId());
	    if (user != null) {
		for (DacoAnswer answer : user.getAnswers()) {
		    if (answer.getFileUuid() != null) {
			try {
			    dacoToolContentHandler.deleteFile(answer.getFileUuid());
			} catch (Exception e) {
			    throw new ToolException("Error while removing Daco file", e);
			}
		    }
		    dacoAnswerDao.removeObject(DacoAnswer.class, answer.getUid());
		}

		NotebookEntry entry = getEntry(session.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
			DacoConstants.TOOL_SIGNATURE, userId);
		if (entry != null) {
		    dacoDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		dacoUserDao.removeObject(DacoUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	dacoSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public void saveOrUpdateAnswer(DacoAnswer answer) {
	dacoAnswerDao.saveObject(answer);
    }

    @Override
    public void saveOrUpdateDaco(Daco daco) {
	dacoDao.saveObject(daco);
    }

    public void saveOrUpdateDacoQuestion(DacoQuestion question) {
	dacoQuestionDao.saveObject(question);
    }

    @Override
    public void saveOrUpdateDacoSession(DacoSession resSession) {
	dacoSessionDao.saveObject(resSession);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Daco daco = dacoDao.getByContentId(toolContentId);
	if (daco == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	daco.setDefineLater(false);
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************

    public ICoreNotebookService getCoreNotebookService() {
	return coreNotebookService;
    }

    public IUserManagementService getUserManagementService() {
	return userManagementService;
    }

    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
	this.coreNotebookService = coreNotebookService;
    }

    public void setDacoDao(DacoDAO dacoDao) {
	this.dacoDao = dacoDao;
    }

    public void setDacoQuestionDao(DacoQuestionDAO dacoQuestionDao) {
	this.dacoQuestionDao = dacoQuestionDao;
    }

    public void setDacoSessionDao(DacoSessionDAO dacoSessionDao) {
	this.dacoSessionDao = dacoSessionDao;
    }

    public void setDacoToolContentHandler(DacoToolContentHandler dacoToolContentHandler) {
	this.dacoToolContentHandler = dacoToolContentHandler;
    }

    public void setDacoUserDao(DacoUserDAO dacoUserDao) {
	this.dacoUserDao = dacoUserDao;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public DacoAnswerDAO getDacoAnswerDao() {
	return dacoAnswerDao;
    }

    public void setDacoAnswerDao(DacoAnswerDAO dacoAnswerDao) {
	this.dacoAnswerDao = dacoAnswerDao;
    }

    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    @Override
    public int getRecordNum(Long userID, Long sessionId) {
	return dacoAnswerDao.getUserRecordCount(userID, sessionId);
    }

    public DacoOutputFactory getDacoOutputFactory() {
	return dacoOutputFactory;
    }

    public void setDacoOutputFactory(DacoOutputFactory dacoOutputFactory) {
	this.dacoOutputFactory = dacoOutputFactory;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getDacoOutputFactory().getSupportedDefinitionClasses(definitionType);
    }
}