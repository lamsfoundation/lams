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
package org.lamsfoundation.lams.tool.assessment.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

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
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentAttachmentDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionResultDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentResultDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentSessionDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentUserDAO;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.Summary;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentAttachment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentQuestionResultComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentToolContentHandler;
import org.lamsfoundation.lams.tool.assessment.util.ReflectDTOComparator;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * 
 * @author Andrey Balan
 * 
 */
public class AssessmentServiceImpl implements IAssessmentService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager {
    static Logger log = Logger.getLogger(AssessmentServiceImpl.class.getName());

    private AssessmentDAO assessmentDao;

    private AssessmentQuestionDAO assessmentQuestionDao;

    private AssessmentAttachmentDAO assessmentAttachmentDao;

    private AssessmentUserDAO assessmentUserDao;

    private AssessmentSessionDAO assessmentSessionDao;

    private AssessmentQuestionResultDAO assessmentQuestionResultDao;
    
    private AssessmentResultDAO assessmentResultDao;

    // tool service
    private AssessmentToolContentHandler assessmentToolContentHandler;

    private MessageService messageService;

    // system services
    private IRepositoryService repositoryService;

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IAuditService auditService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private ILessonService lessonService;

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
     */
    private IVersionedNode getFile(Long uuid, Long versionId, String relativePath)
	    throws AssessmentApplicationException {

	ITicket tic = getRepositoryLoginTicket();

	try {

	    return repositoryService.getFileItem(tic, uuid, versionId, relativePath);

	} catch (AccessDeniedException e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + ".";

	    error = error + "AccessDeniedException: " + e.getMessage() + " Unable to retry further.";
	    AssessmentServiceImpl.log.error(error);
	    throw new AssessmentApplicationException(error, e);

	} catch (Exception e) {

	    String error = "Unable to access repository to get file uuid " + uuid + " version id " + versionId
		    + " path " + relativePath + "." + " Exception: " + e.getMessage();
	    AssessmentServiceImpl.log.error(error);
	    throw new AssessmentApplicationException(error, e);

	}
    }

    /**
     * This method verifies the credentials of the Assessment Tool and gives it the <code>Ticket</code> to login and
     * access the Content Repository.
     * 
     * A valid ticket is needed in order to access the content from the repository. This method would be called evertime
     * the tool needs to upload/download files from the content repository.
     * 
     * @return ITicket The ticket for repostory access
     * @throws AssessmentApplicationException
     */
    private ITicket getRepositoryLoginTicket() throws AssessmentApplicationException {
	ICredentials credentials = new SimpleCredentials(assessmentToolContentHandler.getRepositoryUser(),
		assessmentToolContentHandler.getRepositoryId());
	try {
	    ITicket ticket = repositoryService.login(credentials, assessmentToolContentHandler
		    .getRepositoryWorkspaceName());
	    return ticket;
	} catch (AccessDeniedException ae) {
	    throw new AssessmentApplicationException("Access Denied to repository." + ae.getMessage());
	} catch (WorkspaceNotFoundException we) {
	    throw new AssessmentApplicationException("Workspace not found." + we.getMessage());
	} catch (LoginException e) {
	    throw new AssessmentApplicationException("Login failed." + e.getMessage());
	}
    }

    public Assessment getAssessmentByContentId(Long contentId) {
	Assessment rs = assessmentDao.getByContentId(contentId);
	if (rs == null) {
	    AssessmentServiceImpl.log.error("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    public Assessment getDefaultContent(Long contentId) throws AssessmentApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    AssessmentServiceImpl.log.error(error);
	    throw new AssessmentApplicationException(error);
	}

	Assessment defaultContent = getDefaultAssessment();
	// save default content by given ID.
	Assessment content = new Assessment();
	content = Assessment.newInstance(defaultContent, contentId, assessmentToolContentHandler);
	return content;
    }

    public List getAuthoredQuestions(Long assessmentUid) {
	return assessmentQuestionDao.getAuthoringQuestions(assessmentUid);
    }

    public AssessmentAttachment uploadInstructionFile(FormFile uploadFile, String fileType)
	    throws UploadAssessmentFileException {
	if (uploadFile == null || StringUtils.isEmpty(uploadFile.getFileName())) {
	    throw new UploadAssessmentFileException(messageService.getMessage("error.msg.upload.file.not.found",
		    new Object[] { uploadFile }));
	}

	// upload file to repository
	NodeKey nodeKey = processFile(uploadFile, fileType);

	// create new attachement
	AssessmentAttachment file = new AssessmentAttachment();
	file.setFileType(fileType);
	file.setFileUuid(nodeKey.getUuid());
	file.setFileVersionId(nodeKey.getVersion());
	file.setFileName(uploadFile.getFileName());
	file.setCreated(new Date());

	return file;
    }

    public void createUser(AssessmentUser assessmentUser) {
	assessmentUserDao.saveObject(assessmentUser);
    }

    public AssessmentUser getUserByIDAndContent(Long userId, Long contentId) {

	return assessmentUserDao.getUserByUserIDAndContentID(userId, contentId);

    }

    public AssessmentUser getUserByIDAndSession(Long userId, Long sessionId) {

	return assessmentUserDao.getUserByUserIDAndSessionID(userId, sessionId);

    }

    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws AssessmentApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new AssessmentApplicationException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    public void saveOrUpdateAssessment(Assessment assessment) {
	assessmentDao.saveObject(assessment);
    }

    public void deleteAssessmentAttachment(Long attachmentUid) {
	assessmentAttachmentDao.removeObject(AssessmentAttachment.class, attachmentUid);

    }

    public void saveOrUpdateAssessmentQuestion(AssessmentQuestion question) {
	assessmentQuestionDao.saveObject(question);
    }

    public void deleteAssessmentQuestion(Long uid) {
	assessmentQuestionDao.removeObject(AssessmentQuestion.class, uid);
    }

    public List<AssessmentQuestion> getAssessmentQuestionsBySessionId(Long sessionId) {
	AssessmentSession session = assessmentSessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    AssessmentServiceImpl.log.error("Failed get AssessmentSession by ID [" + sessionId + "]");
	    return null;
	}
	// add assessment questions from Authoring
	Assessment assessment = session.getAssessment();
	List<AssessmentQuestion> questions = new ArrayList<AssessmentQuestion>();
	questions.addAll(assessment.getQuestions());

	// add assessment questions from AssessmentSession
	questions.addAll(session.getAssessmentQuestions());

	return questions;
    }

    public List<Summary> exportBySessionId(Long sessionId, boolean skipHide) {
	AssessmentSession session = assessmentSessionDao.getSessionBySessionId(sessionId);
	if (session == null) {
	    AssessmentServiceImpl.log.error("Failed get AssessmentSession by ID [" + sessionId + "]");
	    return null;
	}
	// initial assessment questions list
	List<Summary> questionList = new ArrayList();
	Set<AssessmentQuestion> resList = session.getAssessment().getQuestions();
	for (AssessmentQuestion question : resList) {
	    if (skipHide && question.isHide()) {
		continue;
	    }
	    // if question is create by author
	    if (question.isCreateByAuthor()) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), question, false);
		questionList.add(sum);
	    }
	}

	// get this session's all assessment questions
	Set<AssessmentQuestion> sessList = session.getAssessmentQuestions();
	for (AssessmentQuestion question : sessList) {
	    if (skipHide && question.isHide()) {
		continue;
	    }

	    // to skip all question create by author
	    if (!question.isCreateByAuthor()) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), question, false);
		questionList.add(sum);
	    }
	}

	return questionList;
    }

    public List<List<Summary>> exportByContentId(Long contentId) {
	Assessment assessment = assessmentDao.getByContentId(contentId);
	List<List<Summary>> groupList = new ArrayList();

	// create init assessment questions list
	List<Summary> initList = new ArrayList();
	groupList.add(initList);
	Set<AssessmentQuestion> resList = assessment.getQuestions();
	for (AssessmentQuestion question : resList) {
	    if (question.isCreateByAuthor()) {
		Summary sum = new Summary(null, null, question, true);
		initList.add(sum);
	    }
	}

	// session by session
	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(contentId);
	for (AssessmentSession session : sessionList) {
	    List<Summary> group = new ArrayList<Summary>();
	    // get this session's all assessment questions
	    Set<AssessmentQuestion> sessList = session.getAssessmentQuestions();
	    for (AssessmentQuestion question : sessList) {
		// to skip all question create by author
		if (!question.isCreateByAuthor()) {
		    Summary sum = new Summary(session.getSessionId(), session.getSessionName(), question, false);
		    group.add(sum);
		}
	    }
	    if (group.size() == 0) {
		group.add(new Summary(session.getSessionId(), session.getSessionName(), null, false));
	    }
	    groupList.add(group);
	}

	return groupList;
    }

    public Assessment getAssessmentBySessionId(Long sessionId) {
	AssessmentSession session = assessmentSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getAssessment().getContentId();
	Assessment res = assessmentDao.getByContentId(contentId);
	return res;
    }

    public AssessmentSession getAssessmentSessionBySessionId(Long sessionId) {
	return assessmentSessionDao.getSessionBySessionId(sessionId);
    }

    public void saveOrUpdateAssessmentSession(AssessmentSession resSession) {
	assessmentSessionDao.saveObject(resSession);
    }
    
    public void saveOrUpdateAssessmentResult(AssessmentResult assessmentResult) {
	assessmentResultDao.saveObject(assessmentResult);
    }
    
    public void setAttemptStarted(Assessment assessment, AssessmentUser assessmentUser, Long toolSessionId) { 
	AssessmentResult result = new AssessmentResult();
	result.setAssessment(assessment);
	result.setUser(assessmentUser);
	result.setSessionId(toolSessionId);
	result.setStartDate(new Timestamp(new Date().getTime()));
	assessmentResultDao.saveObject(result);
    }
    
    public void processUserAnswers(Long assessmentUid, Long userId, ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions) {
	SortedSet<AssessmentQuestionResult> questionResultList = new TreeSet<AssessmentQuestionResult>(
		new AssessmentQuestionResultComparator());
	for (LinkedHashSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		AssessmentQuestionResult processedAnswer = this.processUserAnswer(question);
		questionResultList.add(processedAnswer);
	    }
	}
	AssessmentResult result = assessmentResultDao.getLastAssessmentResult(assessmentUid, userId);
	result.setQuestionResults(questionResultList);
	assessmentResultDao.saveObject(result);
    }
    
    private AssessmentQuestionResult processUserAnswer(AssessmentQuestion question) {
	AssessmentQuestionResult questionResult = new AssessmentQuestionResult();

	Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
	for (AssessmentQuestionOption questionOption : question.getQuestionOptions()) {
	    AssessmentOptionAnswer optionAnswer = new AssessmentOptionAnswer();
	    optionAnswer.setSequenceId(questionOption.getSequenceId());
	    optionAnswer.setAnswerBoolean(questionOption.getAnswerBoolean());
	    optionAnswer.setAnswerInt(questionOption.getAnswerInt());
	    optionAnswers.add(optionAnswer);
	}	
	questionResult.setAssessmentQuestion(question);
	questionResult.setAnswerBoolean(question.getAnswerBoolean());
	questionResult.setAnswerFloat(question.getAnswerFloat());
	questionResult.setAnswerString(question.getAnswerString());
	
	float mark = 0;
	float maxMark = question.getDefaultGrade();
	if (question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		if (option.getAnswerBoolean()) {
		    mark += option.getGrade()*maxMark;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
	    float maxMarkForCorrectAnswer = maxMark / question.getQuestionOptions().size();
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		if (option.getAnswerInt() == option.getSequenceId()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER) {
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		String optionString = option.getOptionString().replaceAll("\\*", ".*");
		Pattern pattern;
		if (question.isCaseSensitive()) {
		    pattern = Pattern.compile(optionString, java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.UNICODE_CASE);
		} else {
		    pattern = Pattern.compile(optionString);
		}		
		boolean isAnswerCorrect = pattern.matcher(question.getAnswerString()).matches();
		
		if (isAnswerCorrect) {
		    mark = option.getGrade()*maxMark;
		    //for display purposes
		    option.setAnswerBoolean(true);
		    break;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    try {
		float answerFloat = Float.valueOf(question.getAnswerString());
		for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		    if ((answerFloat >= (option.getOptionFloat() - option.getAcceptedError()))
			    && (answerFloat <= (option.getOptionFloat() + option.getAcceptedError()))) {
			mark = option.getGrade() * maxMark;
			//for display purposes
			option.setAnswerBoolean(true);
			break;
		    }
		}
	    } catch (Exception e) {
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
	    if (question.getAnswerBoolean() == question.getCorrectAnswer()) {
		mark = maxMark;
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    float maxMarkForCorrectAnswer = maxMark / question.getQuestionOptions().size();
	    TreeSet<AssessmentQuestionOption> correctOptionSet = new TreeSet<AssessmentQuestionOption>(new SequencableComparator());
	    correctOptionSet.addAll(question.getQuestionOptions());
	    ArrayList<AssessmentQuestionOption> correctOptionList = new ArrayList<AssessmentQuestionOption>(correctOptionSet);
	    int i = 0;
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		if (option.getUid() == correctOptionList.get(i++).getUid()) {
		    mark += maxMarkForCorrectAnswer*maxMark;
		}
	    }
	}
	if (mark > maxMark) {
	    mark = maxMark;
	} else if (mark < 0) {
	    mark = 0;
	}
	questionResult.setMark(mark);
	return questionResult;
    }
    
    public AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastAssessmentResult(assessmentUid, userId);
    }
    
    public int getAssessmentResultCount(Long toolSessionId, Long userId) {
	return assessmentResultDao.getAssessmentResultCount(toolSessionId, userId);
    }

    public String finishToolSession(Long toolSessionId, Long userId) throws AssessmentApplicationException {
	AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	assessmentUserDao.saveObject(user);

	// AssessmentSession session = assessmentSessionDao.getSessionBySessionId(toolSessionId);
	// session.setStatus(AssessmentConstants.COMPLETED);
	// assessmentSessionDao.saveObject(session);

	String nextUrl = null;
	try {
	    nextUrl = this.leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new AssessmentApplicationException(e);
	} catch (ToolException e) {
	    throw new AssessmentApplicationException(e);
	}
	return nextUrl;
    }

    public AssessmentQuestion getAssessmentQuestionByUid(Long questionUid) {
	return assessmentQuestionDao.getByUid(questionUid);
    }

    public List<List<Summary>> getSummary(Long contentId) {
	List<List<Summary>> groupList = new ArrayList<List<Summary>>();
	List<Summary> group = new ArrayList<Summary>();

	// get all question which is accessed by user
	Map<Long, Integer> visitCountMap =null; 
	    //assessmentQuestionResultDao.getSummary(contentId);

	Assessment assessment = assessmentDao.getByContentId(contentId);
	Set<AssessmentQuestion> resQuestionList = assessment.getQuestions();

	// get all sessions in a assessment and retrieve all assessment questions under this session
	// plus initial assessment questions by author creating (resquestionList)
	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(contentId);
	for (AssessmentSession session : sessionList) {
	    // one new group for one session.
	    group = new ArrayList<Summary>();
	    // firstly, put all initial assessment question into this group.
	    for (AssessmentQuestion question : resQuestionList) {
		Summary sum = new Summary(session.getSessionId(), session.getSessionName(), question);
		// set viewNumber according visit log
		if (visitCountMap.containsKey(question.getUid())) {
		    sum.setViewNumber(visitCountMap.get(question.getUid()).intValue());
		}
		group.add(sum);
	    }
	    // get this session's all assessment questions
	    Set<AssessmentQuestion> sessQuestionList = session.getAssessmentQuestions();
	    for (AssessmentQuestion question : sessQuestionList) {
		// to skip all question create by author
		if (!question.isCreateByAuthor()) {
		    Summary sum = new Summary(session.getSessionId(), session.getSessionName(), question);
		    // set viewNumber according visit log
		    if (visitCountMap.containsKey(question.getUid())) {
			sum.setViewNumber(visitCountMap.get(question.getUid()).intValue());
		    }
		    group.add(sum);
		}
	    }
	    // so far no any question available, so just put session name info to Summary
	    if (group.size() == 0) {
		group.add(new Summary(session.getSessionId(), session.getSessionName(), null));
	    }
	    groupList.add(group);
	}

	return groupList;

    }

    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry) {
	Map<Long, Set<ReflectDTO>> map = new HashMap<Long, Set<ReflectDTO>>();

	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(contentId);
	for (AssessmentSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    boolean hasRefection = session.getAssessment().isReflectOnActivity();
	    Set<ReflectDTO> list = new TreeSet<ReflectDTO>(new ReflectDTOComparator());
	    // get all users in this session
	    List<AssessmentUser> users = assessmentUserDao.getBySessionID(sessionId);
	    for (AssessmentUser user : users) {
		ReflectDTO ref = new ReflectDTO(user);

		if (setEntry) {
		    NotebookEntry entry = getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
			    AssessmentConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		    if (entry != null) {
			ref.setReflect(entry.getEntry());
		    }
		}

		ref.setHasRefection(hasRefection);
		list.add(ref);
	    }
	    map.put(sessionId, list);
	}

	return map;
    }

//    public List<AssessmentUser> getUserListBySessionQuestion(Long sessionId, Long questionUid) {
//	List<AssessmentQuestionResult> logList = assessmentQuestionResultDao.getAssessmentQuestionResultBySession(
//		sessionId, questionUid);
//	List<AssessmentUser> userList = new ArrayList(logList.size());
//	for (AssessmentQuestionResult visit : logList) {
////	    AssessmentUser user = visit.getUser();
////	    user.setAccessDate(visit.getStartDate());
////	    userList.add(user);
//	}
//	return userList;
//    }

    public void setQuestionVisible(Long questionUid, boolean visible) {
	AssessmentQuestion question = assessmentQuestionDao.getByUid(questionUid);
	if (question != null) {
	    // createBy should be null for system default value.
	    Long userId = 0L;
	    String loginName = "No user";
	    if (question.getCreateBy() != null) {
		userId = question.getCreateBy().getUserId();
		loginName = question.getCreateBy().getLoginName();
	    }
	    if (visible) {
		auditService.logShowEntry(AssessmentConstants.TOOL_SIGNATURE, userId, loginName, question.toString());
	    } else {
		auditService.logHideEntry(AssessmentConstants.TOOL_SIGNATURE, userId, loginName, question.toString());
	    }
	    question.setHide(!visible);
	    assessmentQuestionDao.saveObject(question);
	}
    }

    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, notebookToolType, toolSignature, userId, "",
		entryText);
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

    public AssessmentUser getUser(Long uid) {
	return (AssessmentUser) assessmentUserDao.getObject(AssessmentUser.class, uid);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************
    private Assessment getDefaultAssessment() throws AssessmentApplicationException {
	Long defaultAssessmentId = getToolDefaultContentIdBySignature(AssessmentConstants.TOOL_SIGNATURE);
	Assessment defaultAssessment = getAssessmentByContentId(defaultAssessmentId);
	if (defaultAssessment == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    AssessmentServiceImpl.log.error(error);
	    throw new AssessmentApplicationException(error);
	}

	return defaultAssessment;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws AssessmentApplicationException {
	Long contentId = null;
	contentId = new Long(toolService.getToolDefaultContentIdBySignature(toolSignature));
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    AssessmentServiceImpl.log.error(error);
	    throw new AssessmentApplicationException(error);
	}
	return contentId;
    }

    /**
     * Process an uploaded file.
     * 
     * @throws AssessmentApplicationException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws RepositoryCheckedException
     * @throws InvalidParameterException
     */
    private NodeKey processFile(FormFile file, String fileType) throws UploadAssessmentFileException {
	NodeKey node = null;
	if (file != null && !StringUtils.isEmpty(file.getFileName())) {
	    String fileName = file.getFileName();
	    try {
		node = assessmentToolContentHandler.uploadFile(file.getInputStream(), fileName, file.getContentType(),
			fileType);
	    } catch (InvalidParameterException e) {
		throw new UploadAssessmentFileException(messageService.getMessage("error.msg.invaid.param.upload"));
	    } catch (FileNotFoundException e) {
		throw new UploadAssessmentFileException(messageService.getMessage("error.msg.file.not.found"));
	    } catch (RepositoryCheckedException e) {
		throw new UploadAssessmentFileException(messageService.getMessage("error.msg.repository"));
	    } catch (IOException e) {
		throw new UploadAssessmentFileException(messageService.getMessage("error.msg.io.exception"));
	    }
	}
	return node;
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************
    public void setAuditService(IAuditService auditService) {
	this.auditService = auditService;
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

    public void setAssessmentAttachmentDao(AssessmentAttachmentDAO assessmentAttachmentDao) {
	this.assessmentAttachmentDao = assessmentAttachmentDao;
    }

    public void setAssessmentDao(AssessmentDAO assessmentDao) {
	this.assessmentDao = assessmentDao;
    }

    public void setAssessmentQuestionDao(AssessmentQuestionDAO assessmentQuestionDao) {
	this.assessmentQuestionDao = assessmentQuestionDao;
    }

    public void setAssessmentSessionDao(AssessmentSessionDAO assessmentSessionDao) {
	this.assessmentSessionDao = assessmentSessionDao;
    }

    public void setAssessmentToolContentHandler(AssessmentToolContentHandler assessmentToolContentHandler) {
	this.assessmentToolContentHandler = assessmentToolContentHandler;
    }

    public void setAssessmentUserDao(AssessmentUserDAO assessmentUserDao) {
	this.assessmentUserDao = assessmentUserDao;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }

//    public AssessmentQuestionResultDAO getAssessmentQuestionResultDao() {
//	return assessmentQuestionResultDao;
//    }

    public void setAssessmentQuestionResultDao(AssessmentQuestionResultDAO assessmentQuestionResultDao) {
	this.assessmentQuestionResultDao = assessmentQuestionResultDao;
    }
    
//    public AssessmentResultDAO getAssessmentResultDao() {
//	return assessmentResultDao;
//    }

    public void setAssessmentResultDao(AssessmentResultDAO assessmentResultDao) {
	this.assessmentResultDao = assessmentResultDao;
    }


    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
	Assessment toolContentObj = assessmentDao.getByContentId(toolContentId);
	if (toolContentObj == null) {
	    try {
		toolContentObj = getDefaultAssessment();
	    } catch (AssessmentApplicationException e) {
		throw new DataMissingException(e.getMessage());
	    }
	}
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to find default content for the assessment tool");
	}

	// set AssessmentToolContentHandler as null to avoid copy file node in repository again.
	toolContentObj = Assessment.newInstance(toolContentObj, toolContentId, null);
	toolContentObj.setToolContentHandler(null);
	toolContentObj.setOfflineFileList(null);
	toolContentObj.setOnlineFileList(null);
	try {
	    exportContentService.registerFileClassForExport(AssessmentAttachment.class.getName(), "fileUuid",
		    "fileVersionId");
	    exportContentService.exportToolContent(toolContentId, toolContentObj, assessmentToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    exportContentService.registerFileClassForImport(AssessmentAttachment.class.getName(), "fileUuid",
		    "fileVersionId", "fileName", "fileType", null, null);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, assessmentToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Assessment)) {
		throw new ImportToolContentException(
			"Import Share assessment tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Assessment toolContentObj = (Assessment) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    AssessmentUser user = assessmentUserDao.getUserByUserIDAndContentID(new Long(newUserUid.longValue()),
		    toolContentId);
	    if (user == null) {
		user = new AssessmentUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(new Long(newUserUid.longValue()));
		user.setAssessment(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    // reset all assessmentquestion createBy user
	    Set<AssessmentQuestion> questions = toolContentObj.getQuestions();
	    for (AssessmentQuestion question : questions) {
		question.setCreateBy(user);
	    }
	    assessmentDao.saveObject(toolContentObj);
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

    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException {
	if (toContentId == null) {
	    throw new ToolException("Failed to create the SharedAssessmentFiles tool seession");
	}

	Assessment assessment = null;
	if (fromContentId != null) {
	    assessment = assessmentDao.getByContentId(fromContentId);
	}
	if (assessment == null) {
	    try {
		assessment = getDefaultAssessment();
	    } catch (AssessmentApplicationException e) {
		throw new ToolException(e);
	    }
	}

	Assessment toContent = Assessment.newInstance(assessment, toContentId, assessmentToolContentHandler);
	assessmentDao.saveObject(toContent);

	// save assessment questions as well
	Set questions = toContent.getQuestions();
	if (questions != null) {
	    Iterator iter = questions.iterator();
	    while (iter.hasNext()) {
		AssessmentQuestion question = (AssessmentQuestion) iter.next();
		// createRootTopic(toContent.getUid(),null,msg);
	    }
	}
    }

    public void setAsDefineLater(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Assessment assessment = assessmentDao.getByContentId(toolContentId);
	if (assessment == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	assessment.setDefineLater(value);
    }

    public void setAsRunOffline(Long toolContentId, boolean value) throws DataMissingException, ToolException {
	Assessment assessment = assessmentDao.getByContentId(toolContentId);
	if (assessment == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	assessment.setRunOffline(value);
    }

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException {
	Assessment assessment = assessmentDao.getByContentId(toolContentId);
	if (removeSessionData) {
	    List list = assessmentSessionDao.getByContentId(toolContentId);
	    Iterator iter = list.iterator();
	    while (iter.hasNext()) {
		AssessmentSession session = (AssessmentSession) iter.next();
		assessmentSessionDao.delete(session);
	    }
	}
	assessmentDao.delete(assessment);
    }

    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	AssessmentSession session = new AssessmentSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Assessment assessment = assessmentDao.getByContentId(toolContentId);
	session.setAssessment(assessment);
	assessmentSessionDao.saveObject(session);
    }

    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    AssessmentServiceImpl.log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    AssessmentServiceImpl.log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	AssessmentSession session = assessmentSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(AssessmentConstants.COMPLETED);
	    assessmentSessionDao.saveObject(session);
	} else {
	    AssessmentServiceImpl.log.error("Fail to leave tool Session.Could not find shared assessment "
		    + "session by given session id: " + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared assessment session by given session id: " + toolSessionId);
	}
	return learnerService.completeToolSession(toolSessionId, learnerId);
    }

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	return null;
    }

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException {
	return null;
    }

    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	assessmentSessionDao.deleteBySessionId(toolSessionId);
    }

    /**
     * Get the tool output for the given tool output names.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
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

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
    }

    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	Assessment toolContentObj = getAssessmentByContentId(toolContentId);
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}

	toolContentObj.setReflectOnActivity(Boolean.TRUE);
	toolContentObj.setReflectInstructions(description);
    }

    /* =================================================================================== */

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
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

    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public String getLocalisedMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
    }

    public ILessonService getLessonService() {
	return lessonService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    /**
     * Finds out which lesson the given tool content belongs to and returns its monitoring users.
     * 
     * @param sessionId
     *            tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return getLessonService().getMonitorsByToolSessionId(sessionId);
    }
}
