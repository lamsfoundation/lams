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
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
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
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.Summary;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummaryItem;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentAttachment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUnit;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentQuestionResultComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
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

    private AssessmentOutputFactory assessmentOutputFactory;

    // system services
    private IRepositoryService repositoryService;

    private ILamsToolService toolService;

    private ILearnerService learnerService;
    
    private IAuditService auditService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;
    
    private IGradebookService gradebookService;

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

    @Override
    public Assessment getAssessmentByContentId(Long contentId) {
	Assessment rs = assessmentDao.getByContentId(contentId);
	if (rs == null) {
	    AssessmentServiceImpl.log.debug("Could not find the content by given ID:" + contentId);
	}
	return rs;
    }

    @Override
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

    @Override
    public List getAuthoredQuestions(Long assessmentUid) {
	return assessmentQuestionDao.getAuthoringQuestions(assessmentUid);
    }

    @Override
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

    @Override
    public void createUser(AssessmentUser assessmentUser) {
	assessmentUserDao.saveObject(assessmentUser);
    }

    @Override
    public AssessmentUser getUserByIDAndContent(Long userId, Long contentId) {
	return assessmentUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public AssessmentUser getUserByIDAndSession(Long userId, Long sessionId) {
	return assessmentUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public void deleteFromRepository(Long fileUuid, Long fileVersionId) throws AssessmentApplicationException {
	ITicket ticket = getRepositoryLoginTicket();
	try {
	    repositoryService.deleteVersion(ticket, fileUuid, fileVersionId);
	} catch (Exception e) {
	    throw new AssessmentApplicationException("Exception occured while deleting files from" + " the repository "
		    + e.getMessage());
	}
    }

    @Override
    public void saveOrUpdateAssessment(Assessment assessment) {
	assessmentDao.saveObject(assessment);
    }

    @Override
    public void deleteAssessmentAttachment(Long attachmentUid) {
	assessmentAttachmentDao.removeObject(AssessmentAttachment.class, attachmentUid);
    }

    @Override
    public void deleteAssessmentQuestion(Long uid) {
	assessmentQuestionDao.removeObject(AssessmentQuestion.class, uid);
    }
    
    @Override
    public void deleteQuestionReference(Long uid) {
	assessmentQuestionDao.removeObject(QuestionReference.class, uid);
    }

    @Override
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

    @Override
    public Assessment getAssessmentBySessionId(Long sessionId) {
	AssessmentSession session = assessmentSessionDao.getSessionBySessionId(sessionId);
	// to skip CGLib problem
	Long contentId = session.getAssessment().getContentId();
	Assessment res = assessmentDao.getByContentId(contentId);
	return res;
    }

    @Override
    public AssessmentSession getAssessmentSessionBySessionId(Long sessionId) {
	return assessmentSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public void saveOrUpdateAssessmentSession(AssessmentSession resSession) {
	assessmentSessionDao.saveObject(resSession);
    }

    @Override
    public void setAttemptStarted(Assessment assessment, AssessmentUser assessmentUser, Long toolSessionId) {
	AssessmentResult result = new AssessmentResult();
	result.setAssessment(assessment);
	result.setUser(assessmentUser);
	result.setSessionId(toolSessionId);
	result.setStartDate(new Timestamp(new Date().getTime()));
	assessmentResultDao.saveObject(result);
    }

    @Override
    public void processUserAnswers(Long assessmentUid, Long userId,
	    ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions) {
	SortedSet<AssessmentQuestionResult> questionResultList = new TreeSet<AssessmentQuestionResult>(
		new AssessmentQuestionResultComparator());
	int maximumGrade = 0;
	float grade = 0;
	for (LinkedHashSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		int numberWrongAnswers = assessmentQuestionResultDao.getNumberWrongAnswersDoneBefore(assessmentUid,
			userId, question.getUid());
		AssessmentQuestionResult processedAnswer = this.processUserAnswer(question, numberWrongAnswers);
		questionResultList.add(processedAnswer);

		maximumGrade += question.getGrade();
		grade += processedAnswer.getMark();
	    }
	}
	AssessmentResult result = assessmentResultDao.getLastAssessmentResult(assessmentUid, userId);
	result.setQuestionResults(questionResultList);
	result.setMaximumGrade(maximumGrade);
	result.setGrade(grade);
	result.setFinishDate(new Timestamp(new Date().getTime()));
	assessmentResultDao.saveObject(result);
    }

    private AssessmentQuestionResult processUserAnswer(AssessmentQuestion question, int numberWrongAnswers) {
	AssessmentQuestionResult questionResult = new AssessmentQuestionResult();

	Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
	int j = 0;
	for (AssessmentQuestionOption questionOption : question.getQuestionOptions()) {
	    AssessmentOptionAnswer optionAnswer = new AssessmentOptionAnswer();
	    optionAnswer.setQuestionOptionUid(questionOption.getUid());
	    optionAnswer.setAnswerBoolean(questionOption.getAnswerBoolean());
	    optionAnswer.setAnswerInt(questionOption.getAnswerInt());
	    optionAnswers.add(optionAnswer);

	    if (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		optionAnswer.setAnswerInt(j++);
	    }
	}
	questionResult.setAssessmentQuestion(question);
	questionResult.setAnswerBoolean(question.getAnswerBoolean());
	questionResult.setAnswerFloat(question.getAnswerFloat());
	questionResult.setAnswerString(question.getAnswerString());
	questionResult.setFinishDate(new Date());

	float mark = 0;
	float maxMark = question.getGrade();
	if (question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		if (option.getAnswerBoolean()) {
		    mark += option.getGrade() * maxMark;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
	    float maxMarkForCorrectAnswer = maxMark / question.getQuestionOptions().size();
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		if (option.getAnswerInt() == option.getUid()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER) {
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		String optionString = option.getOptionString().replaceAll("\\*", ".*");
		Pattern pattern;
		if (question.isCaseSensitive()) {
		    pattern = Pattern.compile(optionString);
		} else {
		    pattern = Pattern.compile(optionString, java.util.regex.Pattern.CASE_INSENSITIVE
			    | java.util.regex.Pattern.UNICODE_CASE);
		}
		boolean isAnswerCorrect = (question.getAnswerString() != null) ? pattern.matcher(
			question.getAnswerString()).matches() : false;

		if (isAnswerCorrect) {
		    mark = option.getGrade() * maxMark;
		    questionResult.setSubmittedOptionUid(option.getUid());
		    break;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    String answerString = question.getAnswerString();
	    if (answerString != null) {
		for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		    boolean isAnswerCorrect = false;
		    try {
			float answerFloat = Float.valueOf(question.getAnswerString());
			isAnswerCorrect = ((answerFloat >= (option.getOptionFloat() - option.getAcceptedError())) && (answerFloat <= (option
				.getOptionFloat() + option.getAcceptedError())));
		    } catch (Exception e) {
		    }

		    if (!isAnswerCorrect) {
			for (AssessmentUnit unit : question.getUnits()) {
			    String regex = ".*" + unit.getUnit() + "$";
			    Pattern pattern = Pattern.compile(regex, java.util.regex.Pattern.CASE_INSENSITIVE
				    | java.util.regex.Pattern.UNICODE_CASE);
			    if (pattern.matcher(answerString).matches()) {
				String answerFloatStr = answerString.substring(0, answerString.length()
					- unit.getUnit().length());
				try {
				    float answerFloat = Float.valueOf(answerFloatStr);
				    answerFloat = answerFloat / unit.getMultiplier();
				    isAnswerCorrect = ((answerFloat >= (option.getOptionFloat() - option
					    .getAcceptedError())) && (answerFloat <= (option.getOptionFloat() + option
					    .getAcceptedError())));
				    if (isAnswerCorrect) {
					break;
				    }
				} catch (Exception e) {
				}
			    }
			}
		    }
		    if (isAnswerCorrect) {
			mark = option.getGrade() * maxMark;
			questionResult.setSubmittedOptionUid(option.getUid());
			break;
		    }
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
	    if (question.getAnswerBoolean() == question.getCorrectAnswer() && question.getAnswerString() != null) {
		mark = maxMark;
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    float maxMarkForCorrectAnswer = maxMark / question.getQuestionOptions().size();
	    TreeSet<AssessmentQuestionOption> correctOptionSet = new TreeSet<AssessmentQuestionOption>(
		    new SequencableComparator());
	    correctOptionSet.addAll(question.getQuestionOptions());
	    ArrayList<AssessmentQuestionOption> correctOptionList = new ArrayList<AssessmentQuestionOption>(
		    correctOptionSet);
	    int i = 0;
	    for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		if (option.getUid() == correctOptionList.get(i++).getUid()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }
	}
	if (mark > maxMark) {
	    mark = maxMark;
	}
	if (mark > 0) {
	    float penalty = question.getPenaltyFactor() * numberWrongAnswers;
	    mark -= penalty;
	    if (penalty > maxMark) {
		penalty = maxMark;
	    }
	    questionResult.setPenalty(penalty);
	}
	if (mark < 0) {
	    mark = 0;
	}
	questionResult.setMark(mark);
	return questionResult;
    }

    @Override
    public AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastAssessmentResult(assessmentUid, userId);
    }

    @Override
    public AssessmentResult getLastFinishedAssessmentResult(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastFinishedAssessmentResult(assessmentUid, userId);
    }

    @Override
    public int getAssessmentResultCount(Long assessmentUid, Long userId) {
	return assessmentResultDao.getAssessmentResultCount(assessmentUid, userId);
    }

    @Override
    public List<AssessmentQuestionResult> getAssessmentQuestionResultList(Long assessmentUid, Long userId,
	    Long questionUid) {
	return assessmentQuestionResultDao.getAssessmentQuestionResultList(assessmentUid, userId, questionUid);
    }
    
    @Override
    public Long createNotebookEntry(Long sessionId, Integer userId, String entryText) {
	return coreNotebookService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		AssessmentConstants.TOOL_SIGNATURE, userId, "", entryText);
    }

    @Override
    public NotebookEntry getEntry(Long sessionId, Integer userId) {
	List<NotebookEntry> list = coreNotebookService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		AssessmentConstants.TOOL_SIGNATURE, userId);
	if (list == null || list.isEmpty()) {
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
    public Map<Long, Set<ReflectDTO>> getReflectList(Long contentId) {
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

		NotebookEntry entry = getEntry(sessionId, user.getUserId().intValue());
		if (entry != null) {
		    ref.setReflect(entry.getEntry());
		}

		ref.setHasRefection(hasRefection);
		list.add(ref);
	    }
	    map.put(sessionId, list);
	}

	return map;
    }


    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws AssessmentApplicationException {
	AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	assessmentUserDao.saveObject(user);

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

    @Override
    public AssessmentQuestion getAssessmentQuestionByUid(Long questionUid) {
	return assessmentQuestionDao.getByUid(questionUid);
    }

    @Override
    public List<Summary> getSummaryList(Long contentId) {
	List<Summary> summaryList = new ArrayList<Summary>();

	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(contentId);
	for (AssessmentSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // one new summary for one session.
	    Summary summary = new Summary(sessionId, session.getSessionName());

	    List<AssessmentUser> users = assessmentUserDao.getBySessionID(sessionId);
	    ArrayList<AssessmentResult> assessmentResults = new ArrayList<AssessmentResult>();
	    for (AssessmentUser user : users) {
		AssessmentResult assessmentResult = assessmentResultDao.getLastFinishedAssessmentResultBySessionId(
			sessionId, user.getUserId());
		if (assessmentResult == null) {
		    assessmentResult = new AssessmentResult();
		    assessmentResult.setUser(user);
		} else {
		    Set<AssessmentQuestionResult> sortedQuestionResults = new TreeSet<AssessmentQuestionResult>(
			    new AssessmentQuestionResultComparator());
		    sortedQuestionResults.addAll(assessmentResult.getQuestionResults());
		    assessmentResult.setQuestionResults(sortedQuestionResults);
		}
		assessmentResults.add(assessmentResult);
	    }
	    summary.setAssessmentResults(assessmentResults);
	    summaryList.add(summary);
	}

	escapeQuotes(summaryList);

	return summaryList;
    }

    @Override
    public AssessmentResult getUserMasterDetail(Long sessionId, Long userId) {
	AssessmentResult lastFinishedResult = assessmentResultDao.getLastFinishedAssessmentResultBySessionId(sessionId,
		userId);
	if (lastFinishedResult != null) {
	    SortedSet<AssessmentQuestionResult> questionResults = new TreeSet<AssessmentQuestionResult>(
		    new AssessmentQuestionResultComparator());
	    questionResults.addAll(lastFinishedResult.getQuestionResults());
	    lastFinishedResult.setQuestionResults(questionResults);
	    escapeQuotes(lastFinishedResult);
	}

	return lastFinishedResult;
    }

    @Override
    public UserSummary getUserSummary(Long contentId, Long userId, Long sessionId) {
	UserSummary userSummary = new UserSummary();
	AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	userSummary.setUser(user);
	List<AssessmentResult> results = assessmentResultDao.getAssessmentResultsBySession(sessionId, userId);
	userSummary.setNumberOfAttempts(results.size());

	AssessmentResult lastFinishedResult = assessmentResultDao.getLastFinishedAssessmentResultBySessionId(sessionId,
		userId);
	long timeTaken = lastFinishedResult == null ? 0 : lastFinishedResult.getFinishDate().getTime()
		- lastFinishedResult.getStartDate().getTime();
	userSummary.setTimeOfLastAttempt(new Date(timeTaken));
	if (lastFinishedResult != null) {
	    userSummary.setLastAttemptGrade(lastFinishedResult.getGrade());
	}

	Assessment assessment = assessmentDao.getByContentId(contentId);
	ArrayList<UserSummaryItem> userSummaryItems = new ArrayList<UserSummaryItem>();
	Set<AssessmentQuestion> questions = assessment.getQuestions();
	for (AssessmentQuestion question : questions) {
	    UserSummaryItem userSummaryItem = new UserSummaryItem(question);
	    List<AssessmentQuestionResult> questionResultsForSummary = new ArrayList<AssessmentQuestionResult>();

	    for (AssessmentResult result : results) {
		for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
		    if (question.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
			questionResult.setFinishDate(result.getFinishDate());
			questionResultsForSummary.add(questionResult);
			break;
		    }
		}
	    }
	    
	    //skip questions without answers
	    if (questionResultsForSummary.isEmpty()) {
		continue;
	    } else {
		userSummaryItem.setQuestionResults(questionResultsForSummary);
		userSummaryItems.add(userSummaryItem);
	    }
	}
	userSummary.setUserSummaryItems(userSummaryItems);

	escapeQuotes(userSummary);

	return userSummary;
    }

    @Override
    public QuestionSummary getQuestionSummary(Long contentId, Long questionUid) {
	QuestionSummary questionSummary = new QuestionSummary();
	AssessmentQuestion question = assessmentQuestionDao.getByUid(questionUid);
	questionSummary.setQuestion(question);

	List<List<AssessmentQuestionResult>> questionResults = new ArrayList<List<AssessmentQuestionResult>>();
	SortedSet<AssessmentSession> sessionList = new TreeSet<AssessmentSession>(new AssessmentSessionComparator());
	sessionList.addAll(assessmentSessionDao.getByContentId(contentId));
	for (AssessmentSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    List<AssessmentUser> users = assessmentUserDao.getBySessionID(sessionId);
	    // Set<AssessmentQuestionResult> sessionQuestionResults = new TreeSet<AssessmentQuestionResult>(
	    // new AssessmentQuestionResultComparator());
	    ArrayList<AssessmentQuestionResult> sessionQuestionResults = new ArrayList<AssessmentQuestionResult>();
	    for (AssessmentUser user : users) {
		AssessmentResult assessmentResult = assessmentResultDao.getLastFinishedAssessmentResultBySessionId(
			sessionId, user.getUserId());
		AssessmentQuestionResult questionResult = null;
		if (assessmentResult == null) {
		    questionResult = new AssessmentQuestionResult();
		    questionResult.setAssessmentQuestion(question);
		} else {
		    for (AssessmentQuestionResult dbQuestionResult : assessmentResult.getQuestionResults()) {
			if (dbQuestionResult.getAssessmentQuestion().getUid().equals(questionUid)) {
			    questionResult = dbQuestionResult;
			    break;
			}
		    }
		    if (questionResult == null) {
			continue;
		    }
		}
		questionResult.setUser(user);
		sessionQuestionResults.add(questionResult);
	    }
	    questionResults.add(sessionQuestionResults);
	}
	questionSummary.setQuestionResultsPerSession(questionResults);

	int count = 0;
	float total = 0;
	for (List<AssessmentQuestionResult> sessionQuestionResults : questionResults) {
	    for (AssessmentQuestionResult questionResult : sessionQuestionResults) {
		if (questionResult.getUid() != null) {
		    count++;
		    total += questionResult.getMark();
		}
	    }
	}
	float averageMark = (count == 0) ? 0 : total / count;
	questionSummary.setAverageMark(averageMark);

	escapeQuotes(questionSummary);

	return questionSummary;
    }

    @Override
    public void changeQuestionResultMark(Long questionResultUid, float newMark) {
	AssessmentQuestionResult questionResult = assessmentQuestionResultDao
		.getAssessmentQuestionResultByUid(questionResultUid);
	float oldMark = questionResult.getMark();
	questionResult.setMark(newMark);
	assessmentQuestionResultDao.saveObject(questionResult);

	AssessmentResult result = questionResult.getAssessmentResult();
	float totalMark = result.getGrade() - oldMark + newMark;
	result.setGrade(totalMark);
	assessmentResultDao.saveObject(result);

	// propagade changes to Gradebook
	Integer userId = result.getUser().getUserId().intValue();
	Long toolSessionId = result.getUser().getSession().getSessionId();
	gradebookService.updateActivityMark(new Double(totalMark), null, userId, toolSessionId, true);
	
	//records mark change with audit service
	auditService.logMarkChange(AssessmentConstants.TOOL_SIGNATURE, result.getUser().getUserId(), result.getUser()
		.getLoginName(), "" + oldMark, "" + totalMark);
    }

    @Override
    public AssessmentUser getUser(Long uid) {
	return (AssessmentUser) assessmentUserDao.getObject(AssessmentUser.class, uid);
    }
    
    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }
    
    @Override
    public boolean isGroupedActivity(long toolContentID) {
	return toolService.isGroupedActivity(toolContentID);
    }
    
    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {
	return toolService.getLearnerContentFolder(toolSessionId, userId);
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************

    /**
     * Escapes all characters that may brake JS code on assigning Java value to JS String variable (particularly
     * escapes all quotes in the following way \").
     */
    private static void escapeQuotes(Object object) {
	if (object instanceof UserSummary) {
	    UserSummary userSummary = (UserSummary) object;
	    for (UserSummaryItem userSummaryItem : userSummary.getUserSummaryItems()) {
		for (AssessmentQuestionResult questionResult : userSummaryItem.getQuestionResults()) {
		    escapeQuotesInQuestionResult(questionResult);
		}
	    }
	} else if (object instanceof QuestionSummary) {
	    QuestionSummary questionSummary = (QuestionSummary) object;

	    for (List<AssessmentQuestionResult> sessionQuestionResults : questionSummary.getQuestionResultsPerSession()) {
		for (AssessmentQuestionResult questionResult : sessionQuestionResults) {
		    escapeQuotesInQuestionResult(questionResult);
		}
	    }
	} else if (object instanceof List) {
	    List<Summary> summaryList = (List<Summary>) object;

	    for (Summary summary : summaryList) {
		for (AssessmentResult result : summary.getAssessmentResults()) {
		    for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
			escapeQuotesInQuestionResult(questionResult);
		    }
		}
	    }
	} else if (object instanceof AssessmentResult) {
	    AssessmentResult assessmentResult = (AssessmentResult) object;

	    for (AssessmentQuestionResult questionResult : assessmentResult.getQuestionResults()) {
		escapeQuotesInQuestionResult(questionResult);
	    }
	}
    }

    private static void escapeQuotesInQuestionResult(AssessmentQuestionResult questionResult) {
	String answerString = questionResult.getAnswerString();
	if (answerString != null) {
	    String answerStringEscaped = StringEscapeUtils.escapeJavaScript(answerString);
	    questionResult.setAnswerStringEscaped(answerStringEscaped);
	}

	AssessmentQuestion question = questionResult.getAssessmentQuestion();
	String title = question.getTitle();
	if (title != null) {
	    String titleEscaped = StringEscapeUtils.escapeJavaScript(title);
	    question.setTitleEscaped(titleEscaped);
	}

	for (AssessmentQuestionOption questionOption : question.getQuestionOptions()) {
	    String questionStr = questionOption.getQuestion();
	    if (questionStr != null) {
		String questionEscaped = StringEscapeUtils.escapeJavaScript(questionStr);
		questionOption.setQuestionEscaped(questionEscaped);
	    }

	    String optionStr = questionOption.getOptionString();
	    if (optionStr != null) {
		String optionEscaped = StringEscapeUtils.escapeJavaScript(optionStr);
		questionOption.setOptionStringEscaped(optionEscaped);
	    }
	}
    }

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

    public void setAssessmentQuestionResultDao(AssessmentQuestionResultDAO assessmentQuestionResultDao) {
	this.assessmentQuestionResultDao = assessmentQuestionResultDao;
    }

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
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Assessment assessment = getAssessmentByContentId(toolContentId);
	if (assessment == null) {
	    assessment = getDefaultAssessment();
	}
	return getAssessmentOutputFactory().getToolOutputDefinitions(assessment, definitionType);
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
	return assessmentOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     * 
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return assessmentOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    /* ===============Methods implemented from ToolContentImport102Manager =============== */

    /**
     * Import the data for a 1.0.2 Noticeboard or HTMLNoticeboard
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues) {
    }

    /**
     * Set the description, throws away the title value as this is not supported in 2.0
     */
    public void setReflectiveData(Long toolContentId, String title, String description) throws ToolException,
	    DataMissingException {

	Assessment toolContentObj = getAssessmentByContentId(toolContentId);
	if (toolContentObj == null) {
	    throw new DataMissingException("Unable to set reflective data titled " + title
		    + " on activity toolContentId " + toolContentId + " as the tool content does not exist.");
	}
    }

    /* =================================================================================== */

    public IExportToolContentService getExportContentService() {
	return exportContentService;
    }

    public void setExportContentService(IExportToolContentService exportContentService) {
	this.exportContentService = exportContentService;
    }
    
    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
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

    public AssessmentOutputFactory getAssessmentOutputFactory() {
	return assessmentOutputFactory;
    }

    public void setAssessmentOutputFactory(AssessmentOutputFactory assessmentOutputFactory) {
	this.assessmentOutputFactory = assessmentOutputFactory;
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
     *                tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId) {
	return getLessonService().getMonitorsByToolSessionId(sessionId);
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getAssessmentOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    public String getToolContentTitle(Long toolContentId) {
	return getAssessmentByContentId(toolContentId).getTitle();
    }
}
