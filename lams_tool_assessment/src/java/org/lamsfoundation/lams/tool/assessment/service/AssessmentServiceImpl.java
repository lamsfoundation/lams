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

package org.lamsfoundation.lams.tool.assessment.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;
import org.lamsfoundation.lams.confidencelevel.VsaAnswerDTO;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LearnerInteractionEvent;
import org.lamsfoundation.lams.logevent.service.ILearnerInteractionService;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbQuestionUnit;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolCompletionStatus;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentConfigDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentQuestionResultDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentResultDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentSessionDAO;
import org.lamsfoundation.lams.tool.assessment.dao.AssessmentUserDAO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.assessment.dto.OptionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.SessionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummaryItem;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.util.AnswerIntComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils.AssessmentExcelCell;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.controller.LearningWebsocketServer;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ICommonAssessmentService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.service.IQbToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
public class AssessmentServiceImpl implements IAssessmentService, ICommonAssessmentService, ToolContentManager,
	ToolSessionManager, ToolRestManager, IQbToolService {
    private static Logger log = Logger.getLogger(AssessmentServiceImpl.class.getName());

    private AssessmentDAO assessmentDao;

    private AssessmentQuestionDAO assessmentQuestionDao;

    private AssessmentUserDAO assessmentUserDao;

    private AssessmentSessionDAO assessmentSessionDao;

    private AssessmentQuestionResultDAO assessmentQuestionResultDao;

    private AssessmentResultDAO assessmentResultDao;

    private AssessmentConfigDAO assessmentConfigDao;

    // tool service
    private IToolContentHandler assessmentToolContentHandler;

    private MessageService messageService;

    private AssessmentOutputFactory assessmentOutputFactory;

    // system services

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private ILogEventService logEventService;

    private IUserManagementService userManagementService;

    private ILessonService lessonService;

    private IExportToolContentService exportContentService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    private IQbService qbService;

    private IOutcomeService outcomeService;

    private ILearnerInteractionService learnerInteractionService;

    // *******************************************************************************
    // Service method
    // *******************************************************************************
    @Override
    public boolean isUserGroupLeader(Long userId, Long toolSessionId) {
	AssessmentSession session = getSessionBySessionId(toolSessionId);
	AssessmentUser groupLeader = session == null ? null : session.getGroupLeader();

	return (groupLeader != null) && userId.equals(groupLeader.getUserId());
    }

    @Override
    public AssessmentUser checkLeaderSelectToolForSessionLeader(AssessmentUser user, Long toolSessionId) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}

	AssessmentSession assessmentSession = getSessionBySessionId(toolSessionId);
	AssessmentUser leader = assessmentSession.getGroupLeader();
	// check leader select tool for a leader only in case Assessment tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {

	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getUserId().intValue());
	    // set leader only if current user is the leader
	    if (user.getUserId().equals(leaderUserId)) {
		leader = user;
		// set group leader
		assessmentSession.setGroupLeader(leader);
		assessmentSessionDao.saveObject(assessmentSession);
	    }
	}

	return leader;
    }

    @Override
    public void copyAnswersFromLeader(AssessmentUser user, AssessmentUser leader) {

	if ((user == null) || (leader == null) || user.getUid().equals(leader.getUid())) {
	    return;
	}
	Long assessmentUid = leader.getSession().getAssessment().getUid();

	AssessmentResult leaderResult = assessmentResultDao.getLastFinishedAssessmentResult(assessmentUid,
		leader.getUserId());
	if (leaderResult == null) {
	    return;
	}
	Set<AssessmentQuestionResult> leaderQuestionResults = leaderResult.getQuestionResults();

	// if response doesn't exist create new empty objects which we populate on the next step
	AssessmentResult userResult = assessmentResultDao.getLastAssessmentResult(assessmentUid, user.getUserId());
	if (userResult == null) {
	    userResult = new AssessmentResult();
	    userResult.setAssessment(leaderResult.getAssessment());
	    userResult.setUser(user);
	    userResult.setSessionId(leaderResult.getSessionId());

	    Set<AssessmentQuestionResult> userQuestionResults = userResult.getQuestionResults();
	    for (AssessmentQuestionResult leaderQuestionResult : leaderQuestionResults) {
		AssessmentQuestionResult userQuestionResult = new AssessmentQuestionResult();
		userQuestionResult.setQbToolQuestion(leaderQuestionResult.getQbToolQuestion());
		userQuestionResult.setAssessmentResult(userResult);
		userQuestionResults.add(userQuestionResult);

		Set<AssessmentOptionAnswer> leaderOptionAnswers = leaderQuestionResult.getOptionAnswers();
		Set<AssessmentOptionAnswer> userOptionAnswers = userQuestionResult.getOptionAnswers();
		for (AssessmentOptionAnswer leaderOptionAnswer : leaderOptionAnswers) {
		    AssessmentOptionAnswer userOptionAnswer = new AssessmentOptionAnswer();
		    userOptionAnswer.setOptionUid(leaderOptionAnswer.getOptionUid());
		    userOptionAnswers.add(userOptionAnswer);
		}

		userQuestionResult.setJustification(leaderQuestionResult.getJustification());
	    }
	}

	// copy results from leader to user in both cases (when there is no userResult yet and when if it's been changed
	// by the leader)
	userResult.setStartDate(leaderResult.getStartDate());
	userResult.setLatest(leaderResult.isLatest());
	userResult.setFinishDate(leaderResult.getFinishDate());
	userResult.setMaximumGrade(leaderResult.getMaximumGrade());
	userResult.setGrade(leaderResult.getGrade());

	Set<AssessmentQuestionResult> userQuestionResults = userResult.getQuestionResults();
	for (AssessmentQuestionResult leaderQuestionResult : leaderQuestionResults) {
	    for (AssessmentQuestionResult userQuestionResult : userQuestionResults) {
		if (userQuestionResult.getQbToolQuestion().getUid()
			.equals(leaderQuestionResult.getQbToolQuestion().getUid())) {

		    userQuestionResult.setAnswer(leaderQuestionResult.getAnswer());
		    userQuestionResult.setAnswerFloat(leaderQuestionResult.getAnswerFloat());
		    userQuestionResult.setAnswerBoolean(leaderQuestionResult.getAnswerBoolean());
		    userQuestionResult.setQbOption(leaderQuestionResult.getQbOption());
		    userQuestionResult.setMark(leaderQuestionResult.getMark());
		    userQuestionResult.setMaxMark(leaderQuestionResult.getMaxMark());
		    userQuestionResult.setPenalty(leaderQuestionResult.getPenalty());
		    userQuestionResult.setConfidenceLevel(leaderQuestionResult.getConfidenceLevel());

		    Set<AssessmentOptionAnswer> leaderOptionAnswers = leaderQuestionResult.getOptionAnswers();
		    Set<AssessmentOptionAnswer> userOptionAnswers = userQuestionResult.getOptionAnswers();
		    for (AssessmentOptionAnswer leaderOptionAnswer : leaderOptionAnswers) {
			for (AssessmentOptionAnswer userOptionAnswer : userOptionAnswers) {
			    if (userOptionAnswer.getOptionUid().equals(leaderOptionAnswer.getOptionUid())) {

				userOptionAnswer.setAnswerBoolean(leaderOptionAnswer.getAnswerBoolean());
				userOptionAnswer.setAnswerInt(leaderOptionAnswer.getAnswerInt());

			    }
			}

		    }

		}
	    }
	}

	assessmentResultDao.saveObject(userResult);
    }

    @Override
    public LocalDateTime launchTimeLimit(Long assessmentUid, Long userId) {
	AssessmentResult lastResult = getLastAssessmentResult(assessmentUid, userId);
	LocalDateTime launchedDate = LocalDateTime.now();
	lastResult.setTimeLimitLaunchedDate(launchedDate);
	assessmentResultDao.saveObject(lastResult);
	return launchedDate;
    }

    @Override
    public boolean checkTimeLimitExceeded(long assessmentUid, long userId) {
	Long secondsLeft = LearningWebsocketServer.getSecondsLeft(assessmentUid, userId);
	return secondsLeft != null && secondsLeft.equals(0);
    }

    @Override
    public List<AssessmentUser> getUsersBySession(Long toolSessionID) {
	return assessmentUserDao.getBySessionID(toolSessionID);
    }

    @Override
    public List<AssessmentUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return assessmentUserDao.getPagedUsersBySession(sessionId, page, size, sortBy, sortOrder, searchString,
		userManagementService);
    }

    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	return assessmentUserDao.getCountUsersBySession(sessionId, searchString);
    }

    @Override
    public int getCountUsersByContentId(Long contentId) {
	return assessmentUserDao.getCountUsersByContentId(contentId);
    }

    /**
     * How many learners can possibly access this activity
     */
    @Override
    public int getCountLessonLearnersByContentId(long contentId) {
	long lessonId = lessonService.getLessonByToolContentId(contentId).getLessonId();
	return lessonService.getCountLessonLearners(lessonId, null);
    }

    /**
     * How many learners have already finished answering questions.
     * They are either on results page or left the activity completely.
     */
    @Override
    public int getCountLearnersWithFinishedCurrentAttempt(long contentId) {
	return assessmentResultDao.countLastFinishedAssessmentResults(contentId);
    }

    @Override
    public List<AssessmentUserDTO> getPagedUsersBySessionAndQuestion(Long sessionId, Long questionUid, int page,
	    int size, String sortBy, String sortOrder, String searchString) {
	return assessmentUserDao.getPagedUsersBySessionAndQuestion(sessionId, questionUid, page, size, sortBy,
		sortOrder, searchString, userManagementService);
    }

    @Override
    public String getPortraitId(Long userId) {
	if (userId != null) {
	    User user = (User) userManagementService.findById(User.class, userId.intValue());
	    return user == null || user.getPortraitUuid() == null ? null : user.getPortraitUuid().toString();
	}
	return null;
    }

    @Override
    public Assessment getAssessmentByContentId(Long contentId) {
	return assessmentDao.getByContentId(contentId);
    }

    @Override
    public Assessment getDefaultContent(Long contentId) throws AssessmentApplicationException {
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new AssessmentApplicationException(error);
	}

	Assessment defaultContent = getDefaultAssessment();
	// save default content by given ID.
	Assessment content = new Assessment();
	content = Assessment.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public void createUser(AssessmentUser assessmentUser) {
	// make sure the user was not created in the meantime
	AssessmentUser user = getUserByIDAndSession(assessmentUser.getUserId(),
		assessmentUser.getSession().getSessionId());
	if (user == null) {
	    user = assessmentUser;
	}
	// Save it no matter if the user already exists.
	// At checkLeaderSelectToolForSessionLeader() the user is added to session.
	// Sometimes session save is earlier that user save in another thread, leading to an exception.
	assessmentUserDao.saveObject(user);
    }

    @Override
    public AssessmentUser getUserCreatedAssessment(Long userId, Long contentId) {
	return assessmentUserDao.getUserCreatedAssessment(userId, contentId);
    }

    @Override
    public AssessmentUser getUserByIdAndContent(Long userId, Long contentId) {
	return assessmentUserDao.getUserByIdAndContent(userId, contentId);
    }

    @Override
    public AssessmentUser getUserByLoginAndContent(String login, Long contentId) {
	return assessmentUserDao.getUserByLoginAndContent(login, contentId);
    }

    @Override
    public AssessmentUser getUserByIDAndSession(Long userId, Long sessionId) {
	return assessmentUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public void saveOrUpdateAssessment(Assessment assessment) {
	for (QuestionReference reference : assessment.getQuestionReferences()) {
	    assessmentQuestionDao.saveObject(reference);
	}

	for (AssessmentQuestion question : assessment.getQuestions()) {
	    assessmentQuestionDao.saveObject(question);
	}

	//store object in DB
	assessmentDao.saveObject(assessment);
    }

    @Override
    public void updateAssessmentQuestion(AssessmentQuestion question) {
	assessmentQuestionDao.update(question);
    }

    @Override
    public void releaseFromCache(Object object) {
	assessmentDao.releaseFromCache(object);
    }

    @Override
    public void deleteAssessmentQuestion(Long uid) {
	assessmentQuestionDao.removeObject(AssessmentQuestion.class, uid);
    }

    @Override
    public void deleteQuestionReference(Long uid) {
	//releaseFromCache associated AssessmentQuestion, otherwise it's treated as "A different object with the same identifier value was already associated with the session: AssessmentQuestion"
	QuestionReference reference = (QuestionReference) assessmentQuestionDao.getObject(QuestionReference.class, uid);
	if (reference.getQuestion() != null) {
	    assessmentDao.releaseFromCache(reference.getQuestion());
	}

	assessmentQuestionDao.removeObject(QuestionReference.class, uid);
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
    public AssessmentSession getSessionBySessionId(Long sessionId) {
	return assessmentSessionDao.getSessionBySessionId(sessionId);
    }

    @Override
    public List<AssessmentSession> getSessionsByContentId(Long toolContentId) {
	return assessmentSessionDao.getByContentId(toolContentId);
    }

    @Override
    public void setAttemptStarted(Assessment assessment, AssessmentUser assessmentUser, Long toolSessionId,
	    List<Set<QuestionDTO>> pagedQuestionDtos) {
	//create list of all questions that user is going to answer (it will exclude random questions that user not going to answer)
	Set<AssessmentQuestion> questions = new TreeSet<>();
	if (pagedQuestionDtos != null) {
	    for (Set<QuestionDTO> questionsForOnePage : pagedQuestionDtos) {
		for (QuestionDTO questionDto : questionsForOnePage) {
		    AssessmentQuestion question = assessmentQuestionDao.getByUid(questionDto.getUid());
		    questions.add(question);
		}
	    }
	}

	AssessmentResult lastResult = getLastAssessmentResult(assessment.getUid(), assessmentUser.getUserId());
	if (lastResult != null) {

	    // don't instantiate new attempt if the previous one wasn't finished and thus continue working with it
	    if (lastResult.getFinishDate() == null) {

		//check all required questionResults exist, it can be missing in case of random question - create new one then
		Set<AssessmentQuestionResult> questionResults = lastResult.getQuestionResults();
		Set<AssessmentQuestionResult> updatedQuestionResults = new TreeSet<>();
		for (AssessmentQuestion question : questions) {

		    // get questionResult from DB instance of AssessmentResult
		    AssessmentQuestionResult questionResult = null;
		    for (AssessmentQuestionResult questionResultIter : questionResults) {
			if (question.getUid().equals(questionResultIter.getQbToolQuestion().getUid())) {
			    questionResult = questionResultIter;
			}
		    }
		    if (questionResult == null) {
			questionResult = createQuestionResultObject(question);
		    }
		    updatedQuestionResults.add(questionResult);
		}
		lastResult.setQuestionResults(updatedQuestionResults);
		assessmentResultDao.saveObject(lastResult);
		return;

		// mark previous attempt as being not the latest any longer
	    } else {
		lastResult.setLatest(null);
		assessmentResultDao.saveObject(lastResult);
		assessmentResultDao.flush();
	    }
	}

	AssessmentResult result = new AssessmentResult();
	result.setAssessment(assessment);
	result.setUser(assessmentUser);
	result.setSessionId(toolSessionId);
	result.setStartDate(new Timestamp(new Date().getTime()));
	result.setLatest(true);

	// create questionResult for each question
	Set<AssessmentQuestionResult> questionResults = result.getQuestionResults();
	for (AssessmentQuestion question : questions) {
	    AssessmentQuestionResult questionResult = createQuestionResultObject(question);
	    questionResults.add(questionResult);
	}

	assessmentResultDao.insert(result);
    }

    /*
     * Auxiliary method for setAttemptStarted(). Simply init new AssessmentQuestionResult object and fills it in with
     * values.
     */
    private AssessmentQuestionResult createQuestionResultObject(AssessmentQuestion question) {
	AssessmentQuestionResult questionResult = new AssessmentQuestionResult();
	questionResult.setQbToolQuestion(question);

	// create optionAnswer for each option
	Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
	for (QbOption option : question.getQbQuestion().getQbOptions()) {
	    AssessmentOptionAnswer optionAnswer = new AssessmentOptionAnswer();
	    optionAnswer.setOptionUid(option.getUid());
	    optionAnswers.add(optionAnswer);
	}

	return questionResult;
    }

    @Override
    public void storeSingleMarkHedgingQuestion(Assessment assessment, Long userId,
	    List<Set<QuestionDTO>> pagedQuestions, Long singleMarkHedgingQuestionUid)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	AssessmentResult result = assessmentResultDao.getLastAssessmentResult(assessment.getUid(), userId);

	// prohibit users from submitting (or autosubmitting) answers after result is finished but Resubmit button is
	// not pressed (e.g. using 2 browsers)
	if (result.getFinishDate() != null) {
	    return;
	}

	// search for the question corresponding to single MarkHedging question
	QuestionDTO questionDto = null;
	for (Set<QuestionDTO> questionsForOnePage : pagedQuestions) {
	    for (QuestionDTO questionDtoIter : questionsForOnePage) {
		if (questionDtoIter.getUid().equals(singleMarkHedgingQuestionUid)) {
		    questionDto = questionDtoIter;
		}
	    }
	}

	AssessmentQuestionResult questionResult = storeUserAnswer(result, questionDto);
	questionResult.setFinishDate(new Date());

	float mark = 0;
	for (OptionDTO optionDto : questionDto.getOptionDtos()) {
	    if (optionDto.isCorrect()) {
		//if hedgingMark is a default '-1', change it to '0'
		int hedgingMark = optionDto.getAnswerInt() == -1 ? 0 : optionDto.getAnswerInt();
		mark += hedgingMark;
		break;
	    }
	}
	questionResult.setMark(mark);
	questionResult.setMaxMark((float) questionDto.getMaxMark());
	assessmentResultDao.saveObject(questionResult);

	//for displaying purposes calculate mark and set it to questionDto
	questionDto.setMark(mark);
    }

    @Override
    public boolean storeUserAnswers(Assessment assessment, Long userId, List<Set<QuestionDTO>> pagedQuestions,
	    boolean isAutosave) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	AssessmentResult result = assessmentResultDao.getLastAssessmentResult(assessment.getUid(), userId);

	// prohibit users from submitting (or autosubmitting) answers after result is finished but Resubmit button is
	// not pressed (e.g. using 2 browsers)
	if (result.getFinishDate() != null) {
	    return false;
	}

	// store all answers (in all pages)
	for (Set<QuestionDTO> questionsForOnePage : pagedQuestions) {
	    for (QuestionDTO questionDto : questionsForOnePage) {
		storeUserAnswer(result, questionDto);
	    }
	}

	// store finished date only on user hitting submit all answers button (and not submit mark hedging
	// question)
	int maximumGrade = 0;
	float grade = 0;

	//sum up user mark and max mark for all questions
	for (Set<QuestionDTO> questionsForOnePage : pagedQuestions) {
	    for (QuestionDTO questionDto : questionsForOnePage) {
		// get questionResult from DB instance of AssessmentResult
		AssessmentQuestionResult questionResult = null;
		for (AssessmentQuestionResult questionResultIter : result.getQuestionResults()) {
		    if (questionDto.getUid().equals(questionResultIter.getQbToolQuestion().getUid())) {
			questionResult = questionResultIter;
		    }
		}
		calculateAnswerMark(assessment.getUid(), userId, questionResult, questionDto);
		if (!isAutosave) {
		    questionResult.setFinishDate(new Date());
		}

		grade += questionResult.getMark();
		maximumGrade += questionDto.getMaxMark();
	    }
	}

	result.setMaximumGrade(maximumGrade);
	result.setGrade(grade);
	if (!isAutosave) {
	    result.setFinishDate(new Timestamp(new Date().getTime()));
	}
	assessmentResultDao.update(result);

	return true;
    }

    /**
     * Stores given AssessmentQuestion's answer.
     */
    private AssessmentQuestionResult storeUserAnswer(AssessmentResult assessmentResult, QuestionDTO questionDto)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	Assessment assessment = assessmentResult.getAssessment();

	// get questionResult from DB instance of AssessmentResult
	AssessmentQuestionResult questionResult = null;
	for (AssessmentQuestionResult questionResultIter : assessmentResult.getQuestionResults()) {
	    if (questionDto.getUid().equals(questionResultIter.getQbToolQuestion().getUid())) {
		questionResult = questionResultIter;
	    }
	}

	//if teacher modified question in monitor - update questionDto now
	if (assessment.isContentModifiedInMonitor(assessmentResult.getStartDate())) {
	    AssessmentQuestion modifiedQuestion = assessmentQuestionDao.getByUid(questionDto.getUid());
	    QuestionDTO updatedQuestionDto = modifiedQuestion.getQuestionDTO();
	    PropertyUtils.copyProperties(questionDto, updatedQuestionDto);
	}

	// store question answer values
	questionResult.setAnswerBoolean(questionDto.getAnswerBoolean());
	questionResult.setAnswerFloat(questionDto.getAnswerFloat());
	questionResult.setAnswer(questionDto.getAnswer());

	int j = 0;
	for (OptionDTO optionDto : questionDto.getOptionDtos()) {

	    // find according optionAnswer
	    AssessmentOptionAnswer optionAnswer = null;
	    for (AssessmentOptionAnswer optionAnswerIter : questionResult.getOptionAnswers()) {
		if (optionDto.getUid().equals(optionAnswerIter.getOptionUid())) {
		    optionAnswer = optionAnswerIter;
		}
	    }

	    // store option answer values
	    optionAnswer.setAnswerBoolean(optionDto.getAnswerBoolean());
	    optionAnswer.setAnswerInt(optionDto.getAnswerInt());
	    if (questionDto.getType() == QbQuestion.TYPE_ORDERING) {
		optionAnswer.setAnswerInt(j++);
	    }
	}

	// store confidence levels entered by the learner
	if (assessment.isEnableConfidenceLevels()) {
	    questionResult.setConfidenceLevel(questionDto.getConfidenceLevel());
	}

	// store justification entered by the learner
	if (assessment.isAllowAnswerJustification()) {
	    questionResult.setJustification(questionDto.getJustification());
	}

	return questionResult;
    }

    /**
     *
     * @return grade that user scored by answering that question
     */
    private void calculateAnswerMark(Long assessmentUid, Long userId, AssessmentQuestionResult questionResult,
	    QuestionDTO questionDto) {
	//calculate both mark and maxMark
	float mark = 0;
	float maxMark = questionDto.getMaxMark();
	if (questionDto.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE) {
	    boolean isMarkNullified = false;
	    float optionMaxMark = 0;
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getAnswerBoolean()) {
		    optionMaxMark += optionDto.getMaxMark();
		    mark += optionDto.getMaxMark() * maxMark;

		    // if option of "incorrect answer nullifies mark" is ON check if selected answer has a zero grade
		    // and if so nullify question's mark
		    if (questionDto.isIncorrectAnswerNullifiesMark() && (optionDto.getMaxMark() == 0)) {
			isMarkNullified = true;
		    }
		}
	    }
	    // set optionMaxMark to let jsp know whether the question was answered correctly/partly/incorrectly even if mark=0
	    questionDto.setOptionMaxMark(optionMaxMark);

	    if (isMarkNullified) {
		mark = 0;
	    }

	} else if (questionDto.getType() == QbQuestion.TYPE_MATCHING_PAIRS) {
	    float maxMarkForCorrectAnswer = maxMark / questionDto.getOptionDtos().size();
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getAnswerInt() == optionDto.getUid()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }

	} else if (questionDto.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS) {

	    //clear previous answer
	    questionResult.setQbOption(null);

	    if (questionDto.getAnswer() != null) {
		boolean isQuestionCaseSensitive = questionDto.isCaseSensitive();
		String normalisedQuestionAnswer = AssessmentEscapeUtils.normaliseVSAnswer(questionDto.getAnswer());

		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    Collection<String> optionAnswers = AssessmentEscapeUtils.normaliseVSOption(optionDto.getName());
		    boolean isAnswerMatchedCurrentOption = false;
		    for (String optionAnswer : optionAnswers) {
			String normalisedOptionAnswer = AssessmentEscapeUtils.normaliseVSAnswer(optionAnswer);

			// check is item unraveled
			if (isQuestionCaseSensitive ? normalisedQuestionAnswer.equals(normalisedOptionAnswer)
				: normalisedQuestionAnswer.equalsIgnoreCase(normalisedOptionAnswer)) {
			    isAnswerMatchedCurrentOption = true;
			    break;
			}
		    }

		    if (isAnswerMatchedCurrentOption) {
			mark = optionDto.getMaxMark() * maxMark;
			QbOption qbOption = qbService.getOptionByUid(optionDto.getUid());
			questionResult.setQbOption(qbOption);
			break;
		    }
		}
	    }

	} else if (questionDto.getType() == QbQuestion.TYPE_NUMERICAL) {
	    String answer = questionDto.getAnswer();
	    if (answer != null) {
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    boolean isAnswerMatchedCurrentOption = false;
		    try {
			float answerFloat = Float.valueOf(questionDto.getAnswer());
			isAnswerMatchedCurrentOption = ((answerFloat >= (optionDto.getNumericalOption()
				- optionDto.getAcceptedError()))
				&& (answerFloat <= (optionDto.getNumericalOption() + optionDto.getAcceptedError())));
		    } catch (Exception e) {
		    }

		    if (!isAnswerMatchedCurrentOption) {
			for (QbQuestionUnit unit : questionDto.getUnits()) {
			    String regex = ".*" + unit.getName() + "$";
			    Pattern pattern = Pattern.compile(regex,
				    java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.UNICODE_CASE);
			    if (pattern.matcher(answer).matches()) {
				String answerFloatStr = answer.substring(0, answer.length() - unit.getName().length());
				try {
				    float answerFloat = Float.valueOf(answerFloatStr);
				    answerFloat = answerFloat / unit.getMultiplier();
				    isAnswerMatchedCurrentOption = ((answerFloat >= (optionDto.getNumericalOption()
					    - optionDto.getAcceptedError()))
					    && (answerFloat <= (optionDto.getNumericalOption()
						    + optionDto.getAcceptedError())));
				    if (isAnswerMatchedCurrentOption) {
					break;
				    }
				} catch (Exception e) {
				}
			    }
			}
		    }
		    if (isAnswerMatchedCurrentOption) {
			mark = optionDto.getMaxMark() * maxMark;
			QbOption qbOption = qbService.getOptionByUid(optionDto.getUid());
			questionResult.setQbOption(qbOption);
			break;
		    }
		}
	    }

	} else if (questionDto.getType() == QbQuestion.TYPE_TRUE_FALSE) {
	    if ((questionDto.getAnswerBoolean() == questionDto.getCorrectAnswer())
		    && (questionDto.getAnswer() != null)) {
		mark = maxMark;
	    }

	} else if (questionDto.getType() == QbQuestion.TYPE_ORDERING) {
	    float maxMarkForCorrectAnswer = maxMark / questionDto.getOptionDtos().size();

	    Set<OptionDTO> originalOptions = (new QuestionDTO(questionResult.getQbToolQuestion())).getOptionDtos();
	    ArrayList<OptionDTO> optionsInCorrectOrder = new ArrayList<>(originalOptions);

	    int i = 0;
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getUid().equals(optionsInCorrectOrder.get(i++).getUid())) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }

	} else if (questionDto.getType() == QbQuestion.TYPE_MARK_HEDGING) {
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.isCorrect()) {
		    //if hedgingMark is a default '-1', change it to '0'
		    int hedgingMark = optionDto.getAnswerInt() == -1 ? 0 : optionDto.getAnswerInt();
		    mark += hedgingMark;
		    break;
		}
	    }
	}

	//total mark can't be more than maxMark
	if (mark > maxMark) {
	    mark = maxMark;

	    // in case options have negative marks (<0), their total mark can't be less than -maxMark
	} else if (mark < -maxMark) {
	    mark = -maxMark;
	}

	// calculate penalty
	if (mark > 0) {
	    // calculate number of wrong answers
	    int numberWrongAnswers = assessmentQuestionResultDao.getNumberWrongAnswersDoneBefore(assessmentUid, userId,
		    questionDto.getUid());

	    // calculate penalty itself
	    float penalty = questionDto.getPenaltyFactor() * numberWrongAnswers;
	    mark -= penalty;
	    if (penalty > maxMark) {
		penalty = maxMark;
	    }
	    questionResult.setPenalty(penalty);

	    // don't let penalty make mark less than 0
	    if (mark < 0) {
		mark = 0;
	    }
	}

	questionResult.setMark(mark);
	questionResult.setMaxMark(maxMark);
    }

    @Override
    public void loadupLastAttempt(Long assessmentUid, Long userId, List<Set<QuestionDTO>> pagedQuestionDtos) {
	//get the latest result (it can be unfinished one)
	AssessmentResult lastResult = getLastAssessmentResult(assessmentUid, userId);
	//if there is no results yet - no action required
	if (lastResult == null) {
	    return;
	}

	//get the latest finished result (required for mark hedging type of questions only)
	AssessmentResult lastFinishedResult = null;
	if (lastResult.getFinishDate() == null) {
	    lastFinishedResult = getLastFinishedAssessmentResult(assessmentUid, userId);
	}

	for (Set<QuestionDTO> questionsForOnePage : pagedQuestionDtos) {
	    for (QuestionDTO questionDto : questionsForOnePage) {

		//load last finished results for hedging type of questions (in order to prevent retry)
		Set<AssessmentQuestionResult> questionResults = lastResult.getQuestionResults();
		if ((questionDto.getType() == QbQuestion.TYPE_MARK_HEDGING) && (lastResult.getFinishDate() == null)
			&& (lastFinishedResult != null)) {
		    questionResults = lastFinishedResult.getQuestionResults();
		}

		for (AssessmentQuestionResult questionResult : questionResults) {
		    if (questionDto.getUid().equals(questionResult.getQbToolQuestion().getUid())) {
			loadupQuestionResultIntoQuestionDto(questionDto, questionResult);
			break;
		    }
		}
	    }
	}
    }

    /**
     * Loads up all information from questionResult into questionDto.
     */
    private void loadupQuestionResultIntoQuestionDto(QuestionDTO questionDto, AssessmentQuestionResult questionResult) {
	questionDto.setAnswerBoolean(questionResult.getAnswerBoolean());
	questionDto.setAnswerFloat(questionResult.getAnswerFloat());
	questionDto.setAnswer(questionResult.getAnswer());
	questionDto.setJustification(questionResult.getJustification());
	questionDto.setMark(questionResult.getMark());
	questionDto.setResponseSubmitted(questionResult.getFinishDate() != null);
	questionDto.setPenalty(questionResult.getPenalty());
	questionDto.setConfidenceLevel(questionResult.getConfidenceLevel());

	for (OptionDTO optionDto : questionDto.getOptionDtos()) {

	    for (AssessmentOptionAnswer optionAnswer : questionResult.getOptionAnswers()) {
		if (optionDto.getUid().equals(optionAnswer.getOptionUid())) {
		    optionDto.setAnswerBoolean(optionAnswer.getAnswerBoolean());
		    optionDto.setAnswerInt(optionAnswer.getAnswerInt());
		    break;
		}
	    }
	}

	//sort ordering type of question in order to show how learner has sorted them
	if (questionDto.getType() == QbQuestion.TYPE_ORDERING) {

	    //don't sort ordering type of questions that haven't been submitted to not break their shuffled order
	    boolean isOptionAnswersNeverSubmitted = true;
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getAnswerInt() != 0) {
		    isOptionAnswersNeverSubmitted = false;
		}
	    }

	    if (!isOptionAnswersNeverSubmitted) {
		TreeSet<OptionDTO> orderedSet = new TreeSet<>(new AnswerIntComparator());
		orderedSet.addAll(questionDto.getOptionDtos());
		questionDto.setOptionDtos(orderedSet);
	    }
	}

	// set optionMaxMark to let jsp know whether the question was answered correctly/partly/incorrectly even if mark=0
	if (questionDto.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE) {
	    float optionMaxMark = 0;
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getAnswerBoolean()) {
		    optionMaxMark += optionDto.getMaxMark();
		}
	    }
	    questionDto.setOptionMaxMark(optionMaxMark);
	}

    }

    @Override
    public AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastAssessmentResult(assessmentUid, userId);
    }

    @Override
    public Boolean isLastAttemptFinishedByUser(AssessmentUser user) {
	return assessmentResultDao.isLastAttemptFinishedByUser(user);
    }

    @Override
    public AssessmentResult getLastFinishedAssessmentResult(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastFinishedAssessmentResult(assessmentUid, userId);
    }

    @Override
    public Float getLastTotalScoreByUser(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastTotalScoreByUser(assessmentUid, userId);
    }

    @Override
    public List<AssessmentUserDTO> getLastTotalScoresByContentId(Long toolContentId) {
	return assessmentResultDao.getLastTotalScoresByContentId(toolContentId);
    }

    @Override
    public Float getBestTotalScoreByUser(Long sessionId, Long userId) {
	return assessmentResultDao.getBestTotalScoreByUser(sessionId, userId);
    }

    @Override
    public List<AssessmentUserDTO> getBestTotalScoresByContentId(Long toolContentId) {
	return assessmentResultDao.getBestTotalScoresByContentId(toolContentId);
    }

    @Override
    public Float getFirstTotalScoreByUser(Long sessionId, Long userId) {
	return assessmentResultDao.getFirstTotalScoreByUser(sessionId, userId);
    }

    @Override
    public List<AssessmentUserDTO> getFirstTotalScoresByContentId(Long toolContentId) {
	return assessmentResultDao.getFirstTotalScoresByContentId(toolContentId);
    }

    @Override
    public Float getAvergeTotalScoreByUser(Long sessionId, Long userId) {
	return assessmentResultDao.getAvergeTotalScoreByUser(sessionId, userId);
    }

    @Override
    public List<AssessmentUserDTO> getAverageTotalScoresByContentId(Long toolContentId) {
	return assessmentResultDao.getAverageTotalScoresByContentId(toolContentId);
    }

    @Override
    public Integer getLastFinishedAssessmentResultTimeTaken(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastFinishedAssessmentResultTimeTaken(assessmentUid, userId);
    }

    @Override
    public int countAttemptsPerOption(Long toolContentId, Long optionUid) {
	return assessmentResultDao.countAttemptsPerOption(toolContentId, optionUid);
    }

    @Override
    public AssessmentResult getLastFinishedAssessmentResultNotFromChache(Long assessmentUid, Long userId) {
	AssessmentResult finishedResult = getLastFinishedAssessmentResult(assessmentUid, userId);

	//in case user played tricks with accessing Assessment using two tabs, finishedResult can be null and thus we need to request the last *not finished* result
	if (finishedResult == null) {
	    AssessmentResult notFinishedResult = getLastAssessmentResult(assessmentUid, userId);
	    assessmentDao.releaseFromCache(notFinishedResult);
	    return getLastAssessmentResult(assessmentUid, userId);

	} else {
	    assessmentDao.releaseFromCache(finishedResult);
	    return getLastFinishedAssessmentResult(assessmentUid, userId);
	}
    }

    @Override
    public int getAssessmentResultCount(Long assessmentUid, Long userId) {
	return assessmentResultDao.getAssessmentResultCount(assessmentUid, userId);
    }

    @Override
    public boolean isAssessmentAttempted(Long assessmentUid) {
	return assessmentResultDao.isAssessmentAttempted(assessmentUid);
    }

    @Override
    public AssessmentQuestionResult getAssessmentQuestionResultByUid(Long questionResultUid) {
	return assessmentQuestionResultDao.getAssessmentQuestionResultByUid(questionResultUid);
    }

    @Override
    public List<Object[]> getAssessmentQuestionResultList(Long assessmentUid, Long userId, Long questionUid) {
	return assessmentQuestionResultDao.getAssessmentQuestionResultList(assessmentUid, userId, questionUid);
    }

    @Override
    public Float getQuestionResultMark(Long assessmentUid, Long userId, int questionDisplayOrder) {
	return assessmentQuestionResultDao.getQuestionResultMark(assessmentUid, userId, questionDisplayOrder);
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
    public List<ReflectDTO> getReflectList(Long contentId) {
	List<ReflectDTO> reflectList = new LinkedList<>();

	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(contentId);
	for (AssessmentSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    // get all users in this session
	    List<AssessmentUser> users = assessmentUserDao.getBySessionID(sessionId);
	    for (AssessmentUser user : users) {

		NotebookEntry entry = getEntry(sessionId, user.getUserId().intValue());
		if (entry != null) {
		    ReflectDTO ref = new ReflectDTO(user);
		    ref.setReflect(entry.getEntry());
		    Date postedDate = (entry.getLastModified() != null) ? entry.getLastModified()
			    : entry.getCreateDate();
		    ref.setDate(postedDate);
		    reflectList.add(ref);
		}

	    }
	}

	return reflectList;
    }

    @Override
    public String finishToolSession(Long toolSessionId, Long userId) throws AssessmentApplicationException {
	//mark user as finished
	AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	assessmentUserDao.saveObject(user);

	//if this is a leader finishes, complete all non-leaders as well, also copy leader results to them
	AssessmentSession session = user.getSession();
	Assessment assessment = session.getAssessment();
	if (assessment.isUseSelectLeaderToolOuput() && isUserGroupLeader(userId, toolSessionId)) {
	    session.getAssessmentUsers().forEach(sessionUser -> {
		//finish non-leader
		sessionUser.setSessionFinished(true);
		assessmentUserDao.saveObject(user);

		//copy answers from leader to non-leaders
		copyAnswersFromLeader(sessionUser, session.getGroupLeader());
	    });
	}

	String nextUrl = null;
	try {
	    nextUrl = leaveToolSession(toolSessionId, userId);
	} catch (DataMissingException e) {
	    throw new AssessmentApplicationException(e);
	} catch (ToolException e) {
	    throw new AssessmentApplicationException(e);
	}
	return nextUrl;
    }

    @Override
    public void unsetSessionFinished(Long toolSessionId, Long userId) {
	AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(false);
	assessmentUserDao.saveObject(user);
    }

    @Override
    public List<SessionDTO> getSessionDtos(Long contentId, boolean includeStatistics) {
	List<SessionDTO> sessionDtos = new ArrayList<>();

	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(contentId);
	for (AssessmentSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    SessionDTO sessionDto = new SessionDTO(sessionId, session.getSessionName());

	    //for statistics tab
	    if (includeStatistics) {
		int countUsers = assessmentUserDao.getCountUsersBySession(sessionId, "");
		sessionDto.setNumberLearners(countUsers);
		Object[] markStats = assessmentUserDao.getStatsMarksBySession(sessionId);
		if (markStats != null) {
		    sessionDto.setMinMark(markStats[0] != null
			    ? NumberUtil.formatLocalisedNumber((Float) markStats[0], (Locale) null, 2)
			    : "0.00");
		    sessionDto.setAvgMark(markStats[1] != null
			    ? NumberUtil.formatLocalisedNumber((Float) markStats[1], (Locale) null, 2)
			    : "0.00");
		    sessionDto.setMaxMark(markStats[2] != null
			    ? NumberUtil.formatLocalisedNumber((Float) markStats[2], (Locale) null, 2)
			    : "0.00");
		}
	    } else if (session.getAssessment().isUseSelectLeaderToolOuput()) {
		sessionDto.setNumberLearners(session.getAssessmentUsers().size());

		AssessmentUser leader = session.getGroupLeader();
		if (leader != null) {
		    if (leader.isSessionFinished()) {
			sessionDto.setLeaderFinished(true);
		    } else {
			AssessmentResult result = getLastAssessmentResult(session.getAssessment().getUid(),
				leader.getUserId());
			if (result != null && result.getFinishDate() != null) {
			    leader.setSessionFinished(true);
			}
		    }
		}
	    }

	    sessionDtos.add(sessionDto);
	}

	return sessionDtos;
    }

    @Override
    public LeaderResultsDTO getLeaderResultsDTOForLeaders(Long contentId) {
	LeaderResultsDTO newDto = new LeaderResultsDTO(contentId);
	Object[] markStats = assessmentUserDao.getStatsMarksForLeaders(contentId);
	if (markStats != null) {
	    newDto.setMinMark(
		    markStats[0] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[0], (Locale) null, 2)
			    : "0.00");
	    newDto.setAvgMark(
		    markStats[1] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[1], (Locale) null, 2)
			    : "0.00");
	    newDto.setMaxMark(
		    markStats[2] != null ? NumberUtil.formatLocalisedNumber((Float) markStats[2], (Locale) null, 2)
			    : "0.00");
	    newDto.setNumberGroupsLeaderFinished((Integer) markStats[3]);
	}
	return newDto;
    }

    @Override
    public AssessmentResultDTO getUserMasterDetail(Long sessionId, Long userId) {
	AssessmentResultDTO resultDto = new AssessmentResultDTO();
	resultDto.setSessionId(sessionId);

	AssessmentResult lastFinishedResult = assessmentResultDao.getLastFinishedAssessmentResultByUser(sessionId,
		userId);
	if (lastFinishedResult != null) {
	    Assessment assessment = lastFinishedResult.getAssessment();
	    Set<QuestionReference> questionReferences = lastFinishedResult.getAssessment().getQuestionReferences();
	    Set<AssessmentQuestionResult> questionResults = lastFinishedResult.getQuestionResults();

	    //prepare list of the questions to display in user master detail table, filtering out questions that aren't supposed to be answered
	    SortedSet<AssessmentQuestionResult> questionResultsToDisplay = new TreeSet<>();
	    //in case there is at least one random question - we need to show all questions
	    if (assessment.hasRandomQuestion()) {
		questionResultsToDisplay.addAll(questionResults);

		//otherwise show only questions from the question list
	    } else {
		for (QuestionReference reference : questionReferences) {
		    for (AssessmentQuestionResult questionResult : questionResults) {
			if (reference.getQuestion().getUid().equals(questionResult.getQbToolQuestion().getUid())) {
			    questionResultsToDisplay.add(questionResult);
			}
		    }
		}
	    }

	    resultDto.setQuestionResults(questionResultsToDisplay);

	    //escaping
	    AssessmentEscapeUtils.escapeQuotes(resultDto);
	}

	return resultDto;
    }

    @Override
    public UserSummary getUserSummary(Long contentId, Long userId, Long sessionId) {
	Assessment assessment = assessmentDao.getByContentId(contentId);

	UserSummary userSummary = new UserSummary();
	AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId, sessionId);
	userSummary.setUser(user);
	List<AssessmentResult> results = assessmentResultDao.getFinishedAssessmentResultsByUser(sessionId, userId);
	userSummary.setNumberOfAttempts(results.size());

	AssessmentResult lastFinishedResult = assessmentResultDao.getLastFinishedAssessmentResultByUser(sessionId,
		userId);
	long timeTaken = lastFinishedResult == null ? 0
		: lastFinishedResult.getFinishDate().getTime() - lastFinishedResult.getStartDate().getTime();
	userSummary.setTimeOfLastAttempt(new Date(timeTaken));
	if (lastFinishedResult != null) {
	    userSummary.setLastAttemptGrade(lastFinishedResult.getGrade());
	}

	if (!results.isEmpty()) {

	    //prepare list of the questions to display, filtering out questions that aren't supposed to be answered
	    Set<AssessmentQuestion> questions = new TreeSet<>();
	    //in case there is at least one random question - we need to show all questions in a drop down select
	    if (assessment.hasRandomQuestion()) {
		questions.addAll(assessment.getQuestions());

		//otherwise show only questions from the question list
	    } else {
		for (QuestionReference reference : assessment.getQuestionReferences()) {
		    questions.add(reference.getQuestion());
		}
	    }

	    //prepare list of UserSummaryItems
	    ArrayList<UserSummaryItem> userSummaryItems = new ArrayList<>();
	    for (AssessmentQuestion question : questions) {
		UserSummaryItem userSummaryItem = new UserSummaryItem(question);

		//find all questionResults that correspond to the current question
		List<AssessmentQuestionResult> questionResults = new ArrayList<>();
		for (AssessmentResult result : results) {
		    for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
			if (question.getUid().equals(questionResult.getQbToolQuestion().getUid())) {

			    // for displaying purposes only (no saving occurs)
			    questionResult.setFinishDate(result.getFinishDate());

			    questionResults.add(questionResult);
			    break;
			}
		    }
		}

		userSummaryItem.setQuestionResults(questionResults);
		userSummaryItems.add(userSummaryItem);
	    }
	    userSummary.setUserSummaryItems(userSummaryItems);
	    AssessmentEscapeUtils.escapeQuotes(userSummary);
	}

	return userSummary;
    }

    @Override
    public QuestionSummary getQuestionSummary(Long contentId, Long questionUid) {
	AssessmentQuestion question = assessmentQuestionDao.getByUid(questionUid);
	QbQuestion qbQuestion = question.getQbQuestion();
	List<AssessmentQuestionResult> allQuestionResults = assessmentQuestionResultDao
		.getQuestionResultsByQuestionUid(questionUid);

	QuestionSummary questionSummary = new QuestionSummary(question);

	//prepare extra data for VSA type of questions, so teachers can allocate answers into groups
	if (question.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS) {
	    //find all questionResults that are not allocated into groups yet
	    List<AssessmentQuestionResult> notAllocatedQuestionResults = new ArrayList<>();
	    for (AssessmentQuestionResult questionResult : allQuestionResults) {
		String answer = questionResult.getAnswer();
		if (StringUtils.isBlank(answer)) {
		    continue;
		}

		boolean isAnswerAllocated = false;
		boolean isQuestionCaseSensitive = question.getQbQuestion().isCaseSensitive();
		String normalisedAnswer = AssessmentEscapeUtils.normaliseVSAnswer(answer);
		for (QbOption option : qbQuestion.getQbOptions()) {
		    Collection<String> alternatives = AssessmentEscapeUtils.normaliseVSOption(option.getName());
		    for (String alternative : alternatives) {
			if (isQuestionCaseSensitive ? normalisedAnswer.equals(alternative)
				: normalisedAnswer.equalsIgnoreCase(alternative)) {
			    isAnswerAllocated = true;
			    break;
			}
		    }
		    if (isAnswerAllocated) {
			break;
		    }
		}

		if (!isAnswerAllocated) {
		    notAllocatedQuestionResults.add(questionResult);
		}
	    }
	    questionSummary.setNotAllocatedQuestionResults(notAllocatedQuestionResults);

	    //check is it a TBL case, i.e. only two option groups available, one has 0%, second - 100%
	    boolean isCompatibleWithTbl = qbQuestion.isVsaAndCompatibleWithTbl();
	    questionSummary.setTbl(isCompatibleWithTbl);
	}

	return questionSummary;
    }

    public static boolean isAnswersEqual(AssessmentQuestion question, String answer1, String answer2) {
	if (answer1 == null || answer2 == null) {
	    return false;
	}
	String normalisedAnswer1 = AssessmentEscapeUtils.normaliseVSAnswer(answer1);
	String normalisedAnswer2 = AssessmentEscapeUtils.normaliseVSAnswer(answer2);

	return question.getQbQuestion().isCaseSensitive() ? normalisedAnswer1.equals(normalisedAnswer2)
		: normalisedAnswer1.equalsIgnoreCase(normalisedAnswer2);
    }

    @Override
    public Long allocateAnswerToOption(Long questionUid, Long targetOptionUid, Long previousOptionUid, String answer) {
	AssessmentQuestion assessmentQuestion = assessmentQuestionDao.getByUid(questionUid);
	QbQuestion qbQuestion = assessmentQuestion.getQbQuestion();
	String normalisedAnswer = AssessmentEscapeUtils.normaliseVSAnswer(answer);

	//adding
	if (previousOptionUid.equals(-1L)) {
	    //search for duplicates and, if found, return false
	    QbOption targetOption = null;
	    for (QbOption option : qbQuestion.getQbOptions()) {
		String name = option.getName();
		Collection<String> alternatives = AssessmentEscapeUtils.normaliseVSOption(name);
		if (alternatives.contains(normalisedAnswer)) {
		    return option.getUid();
		}
		if (option.getUid().equals(targetOptionUid)) {
		    targetOption = option;
		}
	    }

	    String name = targetOption.getName();
	    name += "\r\n" + answer;
	    targetOption.setName(name);
	    assessmentDao.saveObject(targetOption);

	    if (log.isDebugEnabled()) {
		log.debug("Adding answer \"" + answer + "\" to option " + targetOptionUid + " in question "
			+ questionUid);
	    }
	    return null;
	}

	//removing
	if (targetOptionUid.equals(-1L)) {
	    for (QbOption previousOption : qbQuestion.getQbOptions()) {
		if (previousOption.getUid().equals(previousOptionUid)) {
		    String name = previousOption.getName();
		    String[] alternatives = name.split(",");

		    StringBuilder nameWithoutUserAnswer = new StringBuilder();
		    for (String alternative : alternatives) {
			String normalisedAlternative = AssessmentEscapeUtils.normaliseVSAnswer(alternative);
			if (!normalisedAlternative.equals(normalisedAnswer)) {
			    nameWithoutUserAnswer.append(alternative).append("\r\n");
			}
		    }
		    if (nameWithoutUserAnswer.length() > 2) {
			previousOption.setName(nameWithoutUserAnswer.substring(0, nameWithoutUserAnswer.length() - 2));
			assessmentDao.saveObject(previousOption);
		    }
		    break;
		}
	    }
	    return null;
	}

	//moving from one to another
	for (QbOption targetOption : qbQuestion.getQbOptions()) {
	    if (targetOption.getUid().equals(targetOptionUid)) {
		String name = targetOption.getName();
		name += "\r\n" + answer;
		targetOption.setName(name);
		assessmentDao.saveObject(targetOption);
		break;
	    }
	}

	for (QbOption previousOption : qbQuestion.getQbOptions()) {
	    if (previousOption.getUid().equals(previousOptionUid)) {
		String name = previousOption.getName();
		String[] alternatives = name.split(",");

		StringBuilder nameWithoutUserAnswer = new StringBuilder();
		for (String alternative : alternatives) {
		    String normalisedAlternative = AssessmentEscapeUtils.normaliseVSAnswer(alternative);
		    if (!normalisedAlternative.equals(normalisedAnswer)) {
			nameWithoutUserAnswer.append(alternative).append("\r\n");
		    }
		}
		previousOption.setName(nameWithoutUserAnswer.length() > 2
			? nameWithoutUserAnswer.substring(0, nameWithoutUserAnswer.length() - 2)
			: "");
		assessmentDao.saveObject(previousOption);
		break;
	    }
	}
	return null;
    }

    @Override
    public void recalculateMarksForAllocatedAnswer(Long questionUid, String answer) {
	AssessmentQuestion assessmentQuestion = assessmentQuestionDao.getByUid(questionUid);
	QbQuestion qbQuestion = assessmentQuestion.getQbQuestion();
	// get all finished user results
	List<AssessmentResult> assessmentResults = assessmentResultDao
		.getAssessmentResultsByQbQuestionAndAnswer(qbQuestion.getUid(), answer);
	//stores userId->lastFinishedAssessmentResult
	Map<Long, AssessmentResult> lastFinishedAssessmentResults = new LinkedHashMap<>();
	for (AssessmentResult assessmentResult : assessmentResults) {
	    Long userId = assessmentResult.getUser().getUserId();
	    lastFinishedAssessmentResults.put(userId, assessmentResult);
	}

	for (AssessmentResult assessmentResult : assessmentResults) {
	    AssessmentUser user = assessmentResult.getUser();
	    float assessmentMark = assessmentResult.getGrade();
	    int assessmentMaxMark = assessmentResult.getMaximumGrade();

	    for (AssessmentQuestionResult questionResult : assessmentResult.getQuestionResults()) {
		if (questionResult.getQbQuestion().getUid().equals(qbQuestion.getUid())) {
		    Float oldQuestionAnswerMark = questionResult.getMark();
		    int oldResultMaxMark = questionResult.getMaxMark() == null ? 0
			    : questionResult.getMaxMark().intValue();

		    //actually recalculate marks
		    QuestionDTO questionDto = new QuestionDTO(assessmentQuestion);
		    questionDto.setMaxMark(oldResultMaxMark);
		    loadupQuestionResultIntoQuestionDto(questionDto, questionResult);
		    calculateAnswerMark(assessmentResult.getAssessment().getUid(), user.getUserId(), questionResult,
			    questionDto);
		    assessmentQuestionResultDao.saveObject(questionResult);

		    float newQuestionAnswerMark = questionResult.getMark();
		    assessmentMark += newQuestionAnswerMark - oldQuestionAnswerMark;
		    break;
		}
	    }

	    // store new mark and maxMark if they were changed
	    AssessmentResult lastFinishedAssessmentResult = lastFinishedAssessmentResults.get(user.getUserId());
	    storeAssessmentResultMarkAndMaxMark(assessmentResult, lastFinishedAssessmentResult, assessmentMark,
		    assessmentMaxMark, user);
	}

	//recalculate marks in all Scratchie activities, that use modified QbQuestion
	toolService.recalculateScratchieMarksForVsaQuestion(qbQuestion.getUid(), answer);
    }

    @Override
    public Map<Long, QuestionSummary> getQuestionSummaryForExport(Assessment assessment) {
	Map<Long, QuestionSummary> questionSummaries = new LinkedHashMap<>();

	if (assessment.getQuestions() == null) {
	    return questionSummaries;
	}

	SortedSet<AssessmentSession> sessions = new TreeSet<>(new AssessmentSessionComparator());
	sessions.addAll(assessmentSessionDao.getByContentId(assessment.getContentId()));

	List<AssessmentResult> assessmentResults = assessmentResultDao
		.getLastFinishedAssessmentResults(assessment.getContentId());
	Map<Long, AssessmentResult> userUidToResultMap = new HashMap<>();
	for (AssessmentResult assessmentResult : assessmentResults) {
	    userUidToResultMap.put(assessmentResult.getUser().getUid(), assessmentResult);
	}

	Map<Long, List<AssessmentUser>> sessionIdToUsersMap = new HashMap<>();
	for (AssessmentSession session : sessions) {

	    Long sessionId = session.getSessionId();
	    List<AssessmentUser> users = new ArrayList<>();

	    // in case of leader aware tool show only leaders' responses
	    if (assessment.isUseSelectLeaderToolOuput()) {
		AssessmentUser leader = session.getGroupLeader();
		if (leader != null) {
		    users.add(leader);
		}
	    } else {
		users = assessmentUserDao.getBySessionID(sessionId);
	    }

	    sessionIdToUsersMap.put(sessionId, users);
	}

	for (AssessmentQuestion question : assessment.getQuestions()) {
	    Long questionUid = question.getUid();
	    QuestionSummary questionSummary = new QuestionSummary(question);

	    List<List<AssessmentQuestionResult>> questionResults = new ArrayList<>();

	    for (AssessmentSession session : sessions) {

		Long sessionId = session.getSessionId();
		List<AssessmentUser> users = sessionIdToUsersMap.get(sessionId);

		ArrayList<AssessmentQuestionResult> sessionQuestionResults = new ArrayList<>();
		for (AssessmentUser user : users) {
		    AssessmentResult assessmentResult = userUidToResultMap.get(user.getUid());
		    AssessmentQuestionResult questionResult = null;
		    if (assessmentResult == null) {
			questionResult = new AssessmentQuestionResult();
			questionResult.setQbToolQuestion(question);
		    } else {
			for (AssessmentQuestionResult dbQuestionResult : assessmentResult.getQuestionResults()) {
			    if (dbQuestionResult.getQbToolQuestion().getUid().equals(questionUid)) {
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

	    AssessmentEscapeUtils.escapeQuotes(questionSummary);

	    questionSummaries.put(questionUid, questionSummary);
	}

	return questionSummaries;
    }

    @Override
    public List<ExcelSheet> exportSummary(Assessment assessment, List<SessionDTO> sessionDtos) {
	List<ExcelSheet> sheets = new LinkedList<>();

	// -------------- First tab: Summary ----------------------------------------------------
	ExcelSheet summarySheet = new ExcelSheet(getMessage("label.export.summary"));
	sheets.add(summarySheet);

	if (sessionDtos != null) {
	    for (SessionDTO sessionDTO : sessionDtos) {
		Long sessionId = sessionDTO.getSessionId();

		summarySheet.addEmptyRow();

		ExcelRow sessionTitleRow = summarySheet.initRow();
		sessionTitleRow.addCell(sessionDTO.getSessionName(), true);

		List<AssessmentUserDTO> userDtos = new ArrayList<>();
		// in case of UseSelectLeaderToolOuput - display only one user
		if (assessment.isUseSelectLeaderToolOuput()) {

		    AssessmentSession session = getSessionBySessionId(sessionId);
		    AssessmentUser groupLeader = session.getGroupLeader();

		    if (groupLeader != null) {

			float assessmentResult = getLastTotalScoreByUser(assessment.getUid(), groupLeader.getUserId());

			AssessmentUserDTO userDto = new AssessmentUserDTO();
			userDto.setFirstName(groupLeader.getFirstName());
			userDto.setLastName(groupLeader.getLastName());
			userDto.setGrade(assessmentResult);
			userDtos.add(userDto);
		    }

		} else {
		    int countSessionUsers = sessionDTO.getNumberLearners();

		    // Get the user list from the db
		    userDtos = getPagedUsersBySession(sessionId, 0, countSessionUsers, "userName", "ASC", "");
		}

		float minGrade = -9999999;
		float maxGrade = 0;
		for (AssessmentUserDTO userDto : userDtos) {
		    float grade = userDto.getGrade();
		    if (grade < minGrade || minGrade == -9999999) {
			minGrade = grade;
		    }
		    if (grade > maxGrade) {
			maxGrade = grade;
		    }
		}
		if (minGrade == -9999999) {
		    minGrade = 0;
		}

		LinkedHashMap<String, Integer> markSummary = getMarksSummaryForSession(userDtos, minGrade, maxGrade,
			10);
		// work out total marks so we can do percentages. need as float for the correct divisions
		int totalNumEntries = 0;
		for (Map.Entry<String, Integer> entry : markSummary.entrySet()) {
		    totalNumEntries += entry.getValue();
		}

		// Mark Summary Min, Max + Grouped Percentages
		summarySheet.addEmptyRow();
		ExcelRow minMaxRow = summarySheet.initRow();
		minMaxRow.addCell(getMessage("label.number.learners"), true);
		minMaxRow.addCell(totalNumEntries);

		minMaxRow = summarySheet.initRow();
		minMaxRow.addCell(getMessage("label.lowest.mark"), true);
		minMaxRow.addCell((double) minGrade);

		minMaxRow = summarySheet.initRow();
		minMaxRow.addCell(getMessage("label.highest.mark"), true);
		minMaxRow.addCell((double) maxGrade);
		summarySheet.addEmptyRow();

		ExcelRow binSummaryRow = summarySheet.initRow();
		binSummaryRow.addCell(getMessage("label.authoring.basic.list.header.mark"), true,
			ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		binSummaryRow.addCell(getMessage("label.number.learners"), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		binSummaryRow.addCell(getMessage("label.percentage"), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		float totalNumEntriesAsFloat = totalNumEntries;
		for (Map.Entry<String, Integer> entry : markSummary.entrySet()) {
		    binSummaryRow = summarySheet.initRow();
		    binSummaryRow.addCell(entry.getKey());
		    binSummaryRow.addCell(entry.getValue());
		    binSummaryRow.addCell(Math.round(entry.getValue() / totalNumEntriesAsFloat * 100));
		}
		summarySheet.addEmptyRow();
		summarySheet.addEmptyRow();

		ExcelRow summaryTitleRow = summarySheet.initRow();
		summaryTitleRow.addCell(getMessage("label.export.user.id"), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		summaryTitleRow.addCell(getMessage("label.monitoring.summary.user.name"), true,
			ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		summaryTitleRow.addCell(getMessage("label.monitoring.summary.total"), true,
			ExcelCell.BORDER_STYLE_BOTTOM_THIN);

		for (AssessmentUserDTO userDto : userDtos) {
		    ExcelRow userResultRow = summarySheet.initRow();
		    userResultRow.addCell(userDto.getLogin());
		    userResultRow.addCell(userDto.getFirstName() + " " + userDto.getLastName());
		    userResultRow.addCell(userDto.getGrade());
		}
		summarySheet.addEmptyRow();
	    }
	}

	// ------------------------------------------------------------------
	// -------------- Second tab: Question Summary ----------------------
	ExcelSheet questionSummarySheet = new ExcelSheet(getMessage("lable.export.summary.by.question"));
	sheets.add(questionSummarySheet);

	// Create the question summary
	ExcelRow summaryTitleRow = questionSummarySheet.initRow();
	summaryTitleRow.addCell(getMessage("label.export.question.summary"), true);
	questionSummarySheet.addEmptyRow();

	Map<Long, QuestionSummary> questionSummaries = getQuestionSummaryForExport(assessment);

	if (assessment.getQuestions() != null) {
	    Set<AssessmentQuestion> questions = assessment.getQuestions();

	    // question row title
	    ExcelRow questionTitleRow = new ExcelRow();
	    questionTitleRow.addCell(getMessage("label.monitoring.question.summary.question"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.authoring.basic.list.header.type"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.authoring.basic.penalty.factor"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.monitoring.question.summary.default.mark"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.export.user.id"), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.monitoring.user.summary.user.name"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.export.date.attempted"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.export.time.attempted"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.authoring.basic.option.answer"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.export.time.taken"), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow.addCell(getMessage("label.export.mark"), true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    if (assessment.isAllowAnswerJustification()) {
		questionTitleRow.addCell(getMessage("label.answer.justification"), true,
			ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    }

	    int questionNumber = 1;

	    for (AssessmentQuestion question : questions) {
		ExcelRow questionTitle = questionSummarySheet.initRow();
		questionTitle.addCell(getMessage("label.monitoring.question.summary.question") + " " + questionNumber++,
			true);

		// set up the summary table data for the top of the question area.
		boolean doSummaryTable = question.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
			|| question.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS
			|| question.getType() == QbQuestion.TYPE_NUMERICAL
			|| question.getType() == QbQuestion.TYPE_TRUE_FALSE;
		// For MC, Numeric & Short Answer Key is optionUid, Value is number of answers
		// For True/False Key 0 is false and Key 1 is true
		Map<Long, Integer> summaryOfAnswers = new HashMap<>();
		Integer summaryNACount = 0;
		Long trueKey = 1L;
		Long falseKey = 0L;
		if (doSummaryTable) {
		    questionSummarySheet.addRow(startSummaryTable(question, summaryOfAnswers, trueKey, falseKey));
		}

		//we need to create questionSummaryTabTemp variable prior to writing it to sheet, in order to fill summaryOfAnswers
		ArrayList<ExcelRow> questionSummaryTabTemp = new ArrayList<>();

		//add question title row
		if (question.getType() == QbQuestion.TYPE_MARK_HEDGING) {
		    List<QbOption> options = question.getQbQuestion().getQbOptions();
		    // question row title
		    ExcelRow hedgeQuestionTitleRow = new ExcelRow();
		    hedgeQuestionTitleRow.addCell(getMessage("label.monitoring.question.summary.question"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.authoring.basic.list.header.type"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.authoring.basic.penalty.factor"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.monitoring.question.summary.default.mark"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.export.user.id"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.monitoring.user.summary.user.name"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.export.date.attempted"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.export.time.attempted"), true);
		    for (QbOption option : options) {
			ExcelCell cell = hedgeQuestionTitleRow.addCell(option.getName().replaceAll("\\<.*?\\>", ""),
				true);
			if (option.isCorrect()) {
			    cell.setColor(IndexedColors.GREEN);
			}
		    }
		    hedgeQuestionTitleRow.addCell(getMessage("label.export.time.taken"), true);
		    hedgeQuestionTitleRow.addCell(getMessage("label.export.mark"), true);
		    questionSummaryTabTemp.add(hedgeQuestionTitleRow);
		} else {
		    questionSummaryTabTemp.add(questionTitleRow);
		}

		QuestionSummary questionSummary = questionSummaries.get(question.getUid());
		List<List<AssessmentQuestionResult>> allResultsForQuestion = questionSummary
			.getQuestionResultsPerSession();

		int markCount = 0;
		Float markTotal = 0.0F;
		int timeTakenCount = 0;
		int timeTakenTotal = 0;
		for (List<AssessmentQuestionResult> resultList : allResultsForQuestion) {
		    for (AssessmentQuestionResult questionResult : resultList) {
			ExcelRow userResultRow = new ExcelRow();
			userResultRow.addCell(questionResult.getQbQuestion().getName());
			userResultRow.addCell(AssessmentServiceImpl
				.getQuestionTypeLanguageLabel(questionResult.getQbQuestion().getType()));
			userResultRow.addCell(Float.valueOf(questionResult.getQbQuestion().getPenaltyFactor()));

			Float maxMark = (questionResult.getMaxMark() == null) ? 0
				: Float.valueOf(questionResult.getMaxMark());
			userResultRow.addCell(maxMark);
			userResultRow.addCell(questionResult.getUser().getLoginName());
			userResultRow.addCell(questionResult.getUser().getFullName());

			//date and time
			ExcelCell dateCell = userResultRow.addCell(questionResult.getFinishDate());
			dateCell.setDataFormat(ExcelCell.CELL_FORMAT_DATE);
			ExcelCell timeCell = userResultRow.addCell(questionResult.getFinishDate());
			timeCell.setDataFormat(ExcelCell.CELL_FORMAT_TIME);

			//answer
			if (question.getType() == QbQuestion.TYPE_MARK_HEDGING) {
			    Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
			    for (QbOption option : question.getQbQuestion().getQbOptions()) {
				for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
				    if (option.getUid().equals(optionAnswer.getOptionUid())) {
					userResultRow.addCell(optionAnswer.getAnswerInt());
					break;
				    }
				}
			    }

			} else {
			    AssessmentExcelCell assessmentCell = AssessmentEscapeUtils
				    .addResponseCellForExcelExport(questionResult, false);
			    userResultRow.addCell(assessmentCell.value,
				    assessmentCell.isHighlighted ? IndexedColors.GREEN : IndexedColors.AUTOMATIC);

			    if (doSummaryTable) {
				summaryNACount = updateSummaryCounts(question, questionResult, summaryOfAnswers,
					summaryNACount);
			    }
			}

			//time taken
			if (questionResult.getAssessmentResult() != null) {
			    Date startDate = questionResult.getAssessmentResult().getStartDate();
			    Date finishDate = questionResult.getFinishDate();
			    if ((startDate != null) && (finishDate != null)) {
				Long seconds = (finishDate.getTime() - startDate.getTime()) / 1000;
				userResultRow.addCell(seconds);
				timeTakenCount++;
				timeTakenTotal += seconds;
			    }
			}

			//mark
			userResultRow.addCell(questionResult.getMark());

			if (assessment.isAllowAnswerJustification()) {
			    userResultRow.addCell(AssessmentEscapeUtils
				    .escapeStringForExcelExport(questionResult.getJustification()));
			}

			questionSummaryTabTemp.add(userResultRow);

			//calculating markCount & markTotal
			if (questionResult.getMark() != null) {
			    markCount++;
			    markTotal += questionResult.getMark();
			}
		    }
		}

		if (doSummaryTable) {
		    questionSummarySheet
			    .addRow(outputSummaryTable(question, summaryOfAnswers, summaryNACount, trueKey, falseKey));
		    questionSummarySheet.addEmptyRow();
		}
		questionSummarySheet.getRows().addAll(questionSummaryTabTemp);

		// Calculating the averages
		ExcelRow averageRow = questionSummarySheet.initRow();
		averageRow.addEmptyCells(8);
		averageRow.addCell(getMessage("label.export.average"), true);
		if (timeTakenTotal > 0) {
		    averageRow.addCell(Long.valueOf(timeTakenTotal / timeTakenCount));
		}
		Float averageMark = markTotal > 0 ? Float.valueOf(markTotal / markCount) : 0.0F;
		averageRow.addCell(averageMark);

		questionSummarySheet.addEmptyRow();
	    }
	}

	/*
	 * ------------------------------------------------------------------
	 * // -------------- Third tab: User Summary ---------------------------
	 *
	 * ExcelSheet userSummarySheet = new ExcelSheet(getMessage("label.export.summary.by.user"));
	 * sheets.add(userSummarySheet);
	 *
	 * // Create the question summary
	 * ExcelRow userSummaryTitle = userSummarySheet.initRow();
	 * userSummaryTitle.addCell(getMessage("label.export.user.summary"), true);
	 *
	 * ExcelRow summaryRowTitle = userSummarySheet.initRow();
	 * summaryRowTitle.addCell("#", true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	 * summaryRowTitle.addCell(getMessage("label.monitoring.question.summary.question"), true,
	 * ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	 * summaryRowTitle.addCell(getMessage("label.authoring.basic.list.header.type"), true,
	 * ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	 * summaryRowTitle.addCell(getMessage("label.authoring.basic.penalty.factor"), true,
	 * ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	 * summaryRowTitle.addCell(getMessage("label.monitoring.question.summary.default.mark"), true,
	 * ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	 * summaryRowTitle.addCell(getMessage("label.monitoring.question.summary.average.mark"), true,
	 * ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	 *
	 * Float totalGradesPossible = 0F;
	 * Float totalAverage = 0F;
	 * int questionIndex = 1;
	 * if (assessment.getQuestionReferences() != null) {
	 * Set<QuestionReference> questionReferences = new TreeSet<>(new SequencableComparator());
	 * questionReferences.addAll(assessment.getQuestionReferences());
	 *
	 * int randomQuestionsCount = 1;
	 * for (QuestionReference questionReference : questionReferences) {
	 *
	 * String title;
	 * String questionType;
	 * Float penaltyFactor;
	 * Float averageMark = null;
	 * if (questionReference.isRandomQuestion()) {
	 *
	 * title = getMessage("label.authoring.basic.type.random.question") + randomQuestionsCount++;
	 * questionType = getMessage("label.authoring.basic.type.random.question");
	 * penaltyFactor = null;
	 * averageMark = null;
	 * } else {
	 *
	 * AssessmentQuestion question = questionReference.getQuestion();
	 * title = question.getQbQuestion().getName();
	 * questionType = AssessmentServiceImpl.getQuestionTypeLanguageLabel(question.getType());
	 * penaltyFactor = question.getQbQuestion().getPenaltyFactor();
	 *
	 * QuestionSummary questionSummary = questionSummaries.get(question.getUid());
	 * if (questionSummary != null) {
	 * averageMark = questionSummary.getAverageMark();
	 * totalAverage += questionSummary.getAverageMark();
	 * }
	 * }
	 *
	 * int maxGrade = questionReference.getMaxMark();
	 * totalGradesPossible += maxGrade;
	 *
	 * ExcelRow questCellRow = userSummarySheet.initRow();
	 * questCellRow.addCell(questionIndex++);
	 * questCellRow.addCell(title);
	 * questCellRow.addCell(questionType);
	 * questCellRow.addCell(penaltyFactor);
	 * questCellRow.addCell(maxGrade);
	 * questCellRow.addCell(averageMark);
	 * }
	 *
	 * if (totalGradesPossible.floatValue() > 0) {
	 * ExcelRow totalCellRow = userSummarySheet.initRow();
	 * totalCellRow.addEmptyCells(2);
	 * totalCellRow.addCell(getMessage("label.monitoring.summary.total"), true);
	 * totalCellRow.addCell(totalGradesPossible);
	 * totalCellRow.addCell(totalAverage);
	 * }
	 * userSummarySheet.addEmptyRow();
	 * }
	 *
	 * if (sessionDtos != null) {
	 * List<AssessmentResult> assessmentResults = assessmentResultDao
	 * .getLastFinishedAssessmentResults(assessment.getContentId());
	 * Map<Long, AssessmentResult> userUidToResultMap = new HashMap<>();
	 * for (AssessmentResult assessmentResult : assessmentResults) {
	 * userUidToResultMap.put(assessmentResult.getUser().getUid(), assessmentResult);
	 * }
	 *
	 * for (SessionDTO sessionDTO : sessionDtos) {
	 * userSummarySheet.addEmptyRow();
	 *
	 * ExcelRow sessionTitle = userSummarySheet.initRow();
	 * sessionTitle.addCell(sessionDTO.getSessionName(), true);
	 *
	 * AssessmentSession assessmentSession = getSessionBySessionId(sessionDTO.getSessionId());
	 * Set<AssessmentUser> assessmentUsers = assessmentSession.getAssessmentUsers();
	 * if (assessmentUsers != null) {
	 * for (AssessmentUser assessmentUser : assessmentUsers) {
	 * ExcelRow userTitleRow = userSummarySheet.initRow();
	 * userTitleRow.addCell(getMessage("label.export.user.id"), true);
	 * userTitleRow.addCell(getMessage("label.monitoring.user.summary.full.name"), true);
	 * userTitleRow.addCell(getMessage("label.export.date.attempted"), true);
	 * userTitleRow.addCell(getMessage("label.monitoring.question.summary.question"), true);
	 * userTitleRow.addCell(getMessage("label.authoring.basic.option.answer"), true);
	 * if (assessment.isEnableConfidenceLevels()) {
	 * userTitleRow.addCell(getMessage("label.confidence"), true);
	 * }
	 * userTitleRow.addCell(getMessage("label.export.mark"), true);
	 *
	 * if (assessment.isAllowAnswerJustification()) {
	 * userTitleRow.addCell(getMessage("label.answer.justification"), true);
	 * }
	 *
	 * AssessmentResult assessmentResult = userUidToResultMap.get(assessmentUser.getUid());
	 * if (assessmentResult != null) {
	 * Set<AssessmentQuestionResult> questionResults = assessmentResult.getQuestionResults();
	 * if (questionResults != null) {
	 * for (AssessmentQuestionResult questionResult : questionResults) {
	 * ExcelRow userResultRow = userSummarySheet.initRow();
	 * userResultRow.addCell(assessmentUser.getLoginName());
	 * userResultRow.addCell(assessmentUser.getFullName());
	 * userResultRow.addCell(assessmentResult.getStartDate());
	 * userResultRow.addCell(questionResult.getQbQuestion().getName());
	 *
	 * AssessmentEscapeUtils.addResponseCellForExcelExport(questionResult,
	 * userResultRow, false);
	 *
	 * if (assessment.isEnableConfidenceLevels()) {
	 * String confidenceLevel = null;
	 *
	 * switch (assessment.getConfidenceLevelsType()) {
	 * case 2:
	 * confidenceLevel = new String[] { getMessage("label.not.confident"),
	 * getMessage("label.confident"),
	 * getMessage("label.very.confident") }[questionResult
	 * .getConfidenceLevel() / 5];
	 * break;
	 * case 3:
	 * confidenceLevel = new String[] { getMessage("label.not.sure"),
	 * getMessage("label.sure"),
	 * getMessage("label.very.sure") }[questionResult
	 * .getConfidenceLevel() / 5];
	 * break;
	 * default:
	 * confidenceLevel = questionResult.getConfidenceLevel() * 10 + "%";
	 * }
	 *
	 * userResultRow.addCell(confidenceLevel);
	 * }
	 *
	 * userResultRow.addCell(questionResult.getMark());
	 *
	 * if (assessment.isAllowAnswerJustification()) {
	 * userResultRow.addCell(AssessmentEscapeUtils
	 * .escapeStringForExcelExport(questionResult.getJustification()));
	 * }
	 * }
	 * }
	 *
	 * ExcelRow userTotalRow = userSummarySheet.initRow();
	 * userTotalRow.addEmptyCells(assessment.isEnableConfidenceLevels() ? 5 : 4);
	 * userTotalRow.addCell(getMessage("label.monitoring.summary.total"), true);
	 * userTotalRow.addCell(assessmentResult.getGrade());
	 * userSummarySheet.addEmptyRow();
	 * }
	 * }
	 * }
	 * }
	 * }
	 */

	// ------------------------------------------------------------------
	// -------------- Third tab: Learner summary ---------------------------

	ExcelSheet userSummarySheet = new ExcelSheet(getMessage("label.export.summary.by.user"));
	sheets.add(userSummarySheet);

	if (sessionDtos != null && assessment.getQuestionReferences() != null) {
	    // if there are multiple session, then the activity has to be grouped
	    boolean isActivityGrouped = sessionDtos.size() > 1;

	    // Row with just "Questions" header
	    ExcelRow userSummaryTitle = userSummarySheet.initRow();
	    // if there is no grouping, then we skip "Group" column
	    int questionLeftPadding = isActivityGrouped ? 3 : 2;
	    userSummaryTitle.addEmptyCells(questionLeftPadding);
	    userSummaryTitle.addCell(getMessage("label.export.questions"), true, ExcelCell.BORDER_STYLE_LEFT_THIN);

	    // Row with question titles
	    ExcelRow questionTitlesRow = userSummarySheet.initRow();
	    questionTitlesRow.addEmptyCells(questionLeftPadding);

	    Set<QuestionReference> questionReferences = new TreeSet<>(new SequencableComparator());
	    questionReferences.addAll(assessment.getQuestionReferences());

	    int questionCounter = 1;
	    // print out all question titles
	    for (QuestionReference questionReference : questionReferences) {
		AssessmentQuestion question = questionReference.getQuestion();
		String title = question.getQbQuestion().getName();
		// leave pure text of title
		title = WebUtil.removeHTMLtags(title).strip();
		// add question numbering in front of title
		title = questionCounter + ". " + title;
		questionCounter++;

		// shorten long title
		if (title.length() > 80) {
		    title = title.substring(0, 80) + "...";
		}
		questionTitlesRow.addCell(title, true, ExcelCell.BORDER_STYLE_LEFT_THIN);

		int columnShift = 1;
		// currently only MCQ and True/False questions have learner interaction logged
		// for other question types, do not include the column
		boolean addAnsweredDateColumn = QbQuestion.TYPE_MULTIPLE_CHOICE == question.getType()
			|| QbQuestion.TYPE_TRUE_FALSE == question.getType();
		if (addAnsweredDateColumn) {
		    columnShift++;
		}
		if (assessment.isEnableConfidenceLevels()) {
		    columnShift++;
		}
		questionTitlesRow.addEmptyCells(columnShift);
		userSummarySheet.addMergedCells(5, questionLeftPadding, questionLeftPadding + columnShift);

		questionLeftPadding += columnShift + 1;
	    }
	    questionTitlesRow.addCell("", ExcelCell.BORDER_STYLE_LEFT_THIN);

	    // Row with column header below question titles
	    ExcelRow userSummaryUserHeadersRow = userSummarySheet.initRow();
	    if (isActivityGrouped) {
		userSummaryUserHeadersRow.addCell(getMessage("label.export.group"), true);
	    }
	    userSummaryUserHeadersRow.addCell(getMessage("label.export.user.id"), true);
	    userSummaryUserHeadersRow.addCell(getMessage("label.monitoring.user.summary.full.name"), true);

	    for (QuestionReference questionReference : questionReferences) {
		userSummaryUserHeadersRow.addCell(getMessage("label.export.mark"), ExcelCell.BORDER_STYLE_LEFT_THIN);
		userSummaryUserHeadersRow.addCell(getMessage("label.authoring.basic.option.answer"));

		AssessmentQuestion question = questionReference.getQuestion();
		boolean addAnsweredDateColumn = QbQuestion.TYPE_MULTIPLE_CHOICE == question.getType()
			|| QbQuestion.TYPE_TRUE_FALSE == question.getType();
		if (addAnsweredDateColumn) {
		    userSummaryUserHeadersRow.addCell(getMessage("monitor.summary.after.date"));
		}
		if (assessment.isEnableConfidenceLevels()) {
		    userSummaryUserHeadersRow.addCell(getMessage("label.confidence"));
		}
	    }

	    // a single column at the end of previous headers
	    userSummaryUserHeadersRow.addCell(getMessage("label.monitoring.summary.total"),
		    ExcelCell.BORDER_STYLE_LEFT_THIN);

	    List<AssessmentResult> assessmentResults = assessmentResultDao
		    .getLastFinishedAssessmentResults(assessment.getContentId());
	    Map<Long, AssessmentResult> userUidToResultMap = assessmentResults.stream()
		    .collect(Collectors.toMap(r -> r.getUser().getUid(), r -> r));

	    for (SessionDTO sessionDTO : sessionDtos) {
		AssessmentSession assessmentSession = getSessionBySessionId(sessionDTO.getSessionId());
		Set<AssessmentUser> assessmentUsers = assessmentSession.getAssessmentUsers();
		for (AssessmentUser assessmentUser : assessmentUsers) {
		    ExcelRow userResultRow = userSummarySheet.initRow();
		    if (isActivityGrouped) {
			userResultRow.addCell(sessionDTO.getSessionName());
		    }
		    userResultRow.addCell(assessmentUser.getLoginName());
		    userResultRow.addCell(assessmentUser.getFullName());

		    AssessmentResult assessmentResult = userUidToResultMap.get(assessmentUser.getUid());
		    if (assessmentResult == null) {
			continue;
		    }

		    Set<AssessmentQuestionResult> questionResults = assessmentResult.getQuestionResults();
		    if (questionResults == null || questionResults.isEmpty()) {
			continue;
		    }
		    Map<Long, AssessmentQuestionResult> questionResultsMap = questionResults.stream().collect(Collectors
			    .toMap(questionResult -> questionResult.getQbToolQuestion().getUid(), Function.identity()));

		    // get information when a learner started interaction with given questions
		    Map<Long, LearnerInteractionEvent> learnerInteractions = learnerInteractionService
			    .getFirstLearnerInteractions(assessment.getContentId(),
				    assessmentUser.getUserId().intValue());

		    // follow question reference ordering, to QbToolQuestion's
		    for (QuestionReference questionReference : questionReferences) {
			AssessmentQuestionResult questionResult = questionResultsMap
				.get(questionReference.getQuestion().getUid());
			// mark
			userResultRow.addCell(questionResult.getMark(), ExcelCell.BORDER_STYLE_LEFT_THIN);

			// option chosen or full answer
			AssessmentExcelCell assessmentCell = AssessmentEscapeUtils
				.addResponseCellForExcelExport(questionResult, true);
			userResultRow.addCell(assessmentCell.value,
				assessmentCell.isHighlighted ? IndexedColors.GREEN : IndexedColors.AUTOMATIC);

			// learner interaction
			QbQuestion question = questionResult.getQbQuestion();
			boolean addAnsweredDateColumn = QbQuestion.TYPE_MULTIPLE_CHOICE == question.getType()
				|| QbQuestion.TYPE_TRUE_FALSE == question.getType();
			if (addAnsweredDateColumn) {
			    // only put interaction time into sheet if auto submit picked up answer
			    LearnerInteractionEvent interaction = assessmentCell.value == null ? null
				    : learnerInteractions.get(questionResult.getQbToolQuestion().getUid());
			    if (interaction == null) {
				userResultRow.addEmptyCell();
			    } else {
				userResultRow.addCell(interaction.getFormattedDate());
			    }
			}

			// confidence level
			if (assessment.isEnableConfidenceLevels()) {
			    String confidenceLevel = null;

			    switch (assessment.getConfidenceLevelsType()) {
				case 2:
				    confidenceLevel = new String[] { getMessage("label.not.confident"),
					    getMessage("label.confident"),
					    getMessage("label.very.confident") }[questionResult.getConfidenceLevel()
						    / 5];
				    break;
				case 3:
				    confidenceLevel = new String[] { getMessage("label.not.sure"),
					    getMessage("label.sure"),
					    getMessage("label.very.sure") }[questionResult.getConfidenceLevel() / 5];
				    break;
				default:
				    confidenceLevel = questionResult.getConfidenceLevel() * 10 + "%";
			    }

			    userResultRow.addCell(confidenceLevel);
			}
		    }
		    userResultRow.addCell(assessmentResult.getGrade(), ExcelCell.BORDER_STYLE_LEFT_THIN);
		}

		userSummarySheet.addEmptyRow();
		userSummarySheet.addEmptyRow();
	    }
	}
	return sheets;
    }

    private ExcelRow startSummaryTable(AssessmentQuestion question, Map<Long, Integer> summaryOfAnswers, Long trueKey,
	    Long falseKey) {
	ExcelRow summaryTableRow;
	int i = 0;
	if (question.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
		|| question.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS
		|| question.getType() == QbQuestion.TYPE_NUMERICAL) {

	    summaryTableRow = new ExcelRow();
	    List<QbOption> options = question.getQbQuestion().getQbOptions();
	    for (QbOption option : options) {
		summaryOfAnswers.put(option.getUid(), 0);
		StringBuilder bldr = new StringBuilder(getMessage("label.authoring.basic.option.answer")).append(" ")
			.append(i + 1).append(" - ");
		if (question.getType() == QbQuestion.TYPE_NUMERICAL) {
		    bldr.append(option.getNumericalOption()).append(" +- ").append(option.getAcceptedError());
		} else {
		    bldr.append(option.getName().replaceAll("\\<.*?\\>", ""));
		}
		summaryTableRow.addCell(bldr.toString());
		i++;
	    }
	    if (question.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE) {
		summaryTableRow.addCell(getMessage("label.not.answered"));
	    } else {
		summaryTableRow.addCell(getMessage("label.other"));
	    }
	} else {
	    summaryTableRow = new ExcelRow();
	    summaryTableRow.addCell(getMessage("label.authoring.true.false.true"));
	    summaryTableRow.addCell(getMessage("label.authoring.true.false.false"));
	    summaryTableRow.addCell(getMessage("label.not.answered"));
	    summaryOfAnswers.put(trueKey, 0);
	    summaryOfAnswers.put(falseKey, 0);
	}
	return summaryTableRow;
    }

    private Integer updateSummaryCounts(AssessmentQuestion question, AssessmentQuestionResult questionResult,
	    Map<Long, Integer> summaryOfAnswers, Integer summaryNACount) {
	if (question.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE) {
	    boolean foundOption = false;
	    Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
	    if (optionAnswers != null) {
		for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
		    if (optionAnswer.getAnswerBoolean()) {
			Integer currentCount = summaryOfAnswers.get(optionAnswer.getOptionUid());
			if (currentCount == null) {
			    log.error(
				    "Assessment Export: Unable to count answer in summary, refers to an unexpected option. QuestionResult "
					    + questionResult.getUid() + " OptionUid " + optionAnswer.getOptionUid()
					    + " question " + question.getUid());
			} else {
			    summaryOfAnswers.put(optionAnswer.getOptionUid(), currentCount + 1);
			    foundOption = true;
			}
		    }
		}
	    }
	    if (!foundOption) {
		summaryNACount++;
	    }
	} else if (question.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS
		|| question.getType() == QbQuestion.TYPE_NUMERICAL) {
	    Long submittedOptionUid = questionResult.getQbOption() == null ? null
		    : questionResult.getQbOption().getUid();
	    if (submittedOptionUid != null) {
		Integer currentCount = summaryOfAnswers.get(submittedOptionUid);
		if (currentCount == null) {
		    log.error(
			    "Assessment Export: Unable to count answer in summary, refers to an unexpected option. QuestionResult "
				    + questionResult.getUid() + " chosen optionUid " + submittedOptionUid + " question "
				    + question.getUid());
		} else {
		    summaryOfAnswers.put(submittedOptionUid, currentCount + 1);
		}
	    } else {
		summaryNACount++;
	    }
	} else if (question.getType() == QbQuestion.TYPE_TRUE_FALSE) {
	    if (questionResult.getAnswer() == null) {
		summaryNACount++;
	    } else {
		long key = questionResult.getAnswerBoolean() ? 1 : 0;
		Integer currentCount = summaryOfAnswers.get(key);
		summaryOfAnswers.put(key, currentCount + 1);
	    }
	}
	return summaryNACount;
    }

    private ExcelRow outputSummaryTable(AssessmentQuestion question, Map<Long, Integer> summaryOfAnswers,
	    Integer summaryNACount, Long trueKey, Long falseKey) {
	ExcelRow summaryTableRow = new ExcelRow();
	int total = summaryNACount;
	for (int value : summaryOfAnswers.values()) {
	    total += value;
	}
	if (question.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE
		|| question.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS
		|| question.getType() == QbQuestion.TYPE_NUMERICAL) {
	    for (QbOption option : question.getQbQuestion().getQbOptions()) {
		Double optionPercentage = total == 0 || summaryOfAnswers.get(option.getUid()) == null ? 0
			: (double) summaryOfAnswers.get(option.getUid()) / total;
		ExcelCell optionCell = summaryTableRow.addPercentageCell(optionPercentage);
		if (option.getMaxMark() > 0) {
		    optionCell.setColor(IndexedColors.GREEN);
		}
	    }

	} else {
	    Double correctPercentage = total == 0 || summaryOfAnswers.get(trueKey) == null ? 0
		    : (double) summaryOfAnswers.get(trueKey) / total;
	    ExcelCell correctCell = summaryTableRow.addPercentageCell(correctPercentage);

	    Double wrongPercentage = total == 0 || summaryOfAnswers.get(falseKey) == null ? 0
		    : (double) summaryOfAnswers.get(falseKey) / total;
	    ExcelCell wrongCell = summaryTableRow.addPercentageCell(wrongPercentage);

	    if (question.getQbQuestion().getCorrectAnswer()) {
		correctCell.setColor(IndexedColors.GREEN);
	    } else {
		wrongCell.setColor(IndexedColors.GREEN);
	    }
	}

	Double summaryNAPercentage = total == 0 ? 0 : (double) summaryNACount / total;
	summaryTableRow.addPercentageCell(summaryNAPercentage);

	return summaryTableRow;
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    public static String getQuestionTypeLanguageLabel(int type) {
	switch (type) {
	    case QbQuestion.TYPE_ESSAY:
		return "Essay";
	    case QbQuestion.TYPE_MATCHING_PAIRS:
		return "Matching Pairs";
	    case QbQuestion.TYPE_MULTIPLE_CHOICE:
		return "Multiple Choice";
	    case QbQuestion.TYPE_NUMERICAL:
		return "Numerical";
	    case QbQuestion.TYPE_ORDERING:
		return "Ordering";
	    case QbQuestion.TYPE_VERY_SHORT_ANSWERS:
		return "Short Answer";
	    case QbQuestion.TYPE_TRUE_FALSE:
		return "True/False";
	    case QbQuestion.TYPE_MARK_HEDGING:
		return "Mark Hedging";
	    default:
		return null;
	}
    }

    @Override
    public void changeQuestionResultMark(Long questionResultUid, float newMark) {
	AssessmentQuestionResult questionResult = assessmentQuestionResultDao
		.getAssessmentQuestionResultByUid(questionResultUid);
	float oldMark = questionResult.getMark();
	AssessmentResult assessmentResult = questionResult.getAssessmentResult();
	float assessmentMark = (assessmentResult.getGrade() - oldMark) + newMark;

	Long toolSessionId = assessmentResult.getSessionId();
	Assessment assessment = assessmentResult.getAssessment();
	Long questionUid = questionResult.getQbToolQuestion().getUid();

	// When changing a mark for user and isUseSelectLeaderToolOuput is true, the mark should be propagated to all
	// students within the group
	List<AssessmentUser> users = new ArrayList<>();
	if (assessment.isUseSelectLeaderToolOuput()) {
	    users = getUsersBySession(toolSessionId);
	} else {
	    users = new ArrayList<>();
	    AssessmentUser user = assessmentResult.getUser();
	    users.add(user);
	}

	for (AssessmentUser user : users) {
	    Long userId = user.getUserId();

	    List<Object[]> questionResults = assessmentQuestionResultDao
		    .getAssessmentQuestionResultList(assessment.getUid(), userId, questionUid);

	    if ((questionResults == null) || questionResults.isEmpty()) {
		log.warn("User with uid: " + user.getUid()
			+ " doesn't have any results despite the fact group leader has some.");
		continue;
	    }

	    Object[] lastAssessmentQuestionResultObj = questionResults.get(questionResults.size() - 1);
	    AssessmentQuestionResult lastAssessmentQuestionResult = (AssessmentQuestionResult) lastAssessmentQuestionResultObj[0];

	    lastAssessmentQuestionResult.setMark(newMark);
	    assessmentQuestionResultDao.saveObject(lastAssessmentQuestionResult);

	    AssessmentResult result = lastAssessmentQuestionResult.getAssessmentResult();
	    result.setGrade(assessmentMark);
	    assessmentResultDao.saveObject(result);

	    // propagade changes to Gradebook
	    toolService.updateActivityMark(Double.valueOf(assessmentMark), null, userId.intValue(), toolSessionId,
		    false);

	    // records mark change with audit service
	    logEventService.logMarkChange(userId, user.getLoginName(), assessment.getContentId(), "" + oldMark,
		    "" + assessmentMark);
	}

    }

    @SuppressWarnings("unchecked")
    @Override
    public void recalculateUserAnswers(final Long assessmentUid, final Long toolContentId,
	    Set<AssessmentQuestion> oldQuestions, Set<AssessmentQuestion> newQuestions,
	    Set<QuestionReference> oldReferences, Set<QuestionReference> newReferences) {

	// create list of modified questions
	List<AssessmentQuestion> modifiedQuestions = new ArrayList<>();
	for (AssessmentQuestion oldQuestion : oldQuestions) {

	    if (QbQuestion.TYPE_ESSAY == oldQuestion.getType()
		    || QbQuestion.TYPE_MATCHING_PAIRS == oldQuestion.getType()
		    || QbQuestion.TYPE_ORDERING == oldQuestion.getType()) {
		continue;
	    }

	    for (AssessmentQuestion newQuestion : newQuestions) {
		if (oldQuestion.getDisplayOrder() == newQuestion.getDisplayOrder()) {

		    boolean isQuestionModified = false;

		    // title or question is different - do nothing. Also question grade can't be changed

		    //QbQuestion.TYPE_TRUE_FALSE
		    if (oldQuestion.getQbQuestion().getCorrectAnswer() != newQuestion.getQbQuestion()
			    .getCorrectAnswer()) {
			isQuestionModified = true;
		    }

		    // options are different
		    List<QbOption> oldOptions = oldQuestion.getQbQuestion().getQbOptions();
		    List<QbOption> newOptions = newQuestion.getQbQuestion().getQbOptions();
		    for (QbOption oldOption : oldOptions) {
			for (QbOption newOption : newOptions) {
			    if (oldOption.getDisplayOrder() == newOption.getDisplayOrder()) {

				//short answer
				if (((oldQuestion.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS)
					&& !StringUtils.equals(oldOption.getName(), newOption.getName()))
					//numbering
					|| (oldOption.getNumericalOption() != newOption.getNumericalOption())
					|| (oldOption.getAcceptedError() != newOption.getAcceptedError())
					//option grade
					|| (oldOption.getMaxMark() != newOption.getMaxMark())
					//changed correct option
					|| (oldOption.isCorrect() != newOption.isCorrect())) {
				    isQuestionModified = true;
				}
			    }
			}
		    }
		    if (oldOptions.size() != newOptions.size()) {
			isQuestionModified = true;
		    }

		    if (isQuestionModified) {
			modifiedQuestions.add(newQuestion);
		    }
		}
	    }
	}

	// create list of references with modified grades.
	// modifiedReferences holds pairs newReference -> oldReference.getMaxMark()
	Map<QuestionReference, Integer> modifiedReferences = new HashMap<>();
	for (QuestionReference oldReference : oldReferences) {
	    for (QuestionReference newReference : newReferences) {
		if (oldReference.getUid().equals(newReference.getUid())
			&& (oldReference.getMaxMark() != newReference.getMaxMark())) {
		    modifiedReferences.put(newReference, oldReference.getMaxMark());
		}
	    }
	}

	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(toolContentId);
	for (AssessmentSession session : sessionList) {
	    Long toolSessionId = session.getSessionId();
	    Set<AssessmentUser> sessionUsers = session.getAssessmentUsers();

	    for (AssessmentUser user : sessionUsers) {

		// get all finished user results
		List<AssessmentResult> assessmentResults = assessmentResultDao.getAssessmentResults(assessmentUid,
			user.getUserId());
		AssessmentResult lastFinishedAssessmentResult = (assessmentResults.isEmpty()) ? null
			: assessmentResults.get(assessmentResults.size() - 1);

		//add autosave assessmentResult as well
		AssessmentResult lastAssessmentResult = getLastAssessmentResult(assessmentUid, user.getUserId());
		if (lastAssessmentResult != null && lastAssessmentResult.getFinishDate() == null) {
		    assessmentResults.add(lastAssessmentResult);
		}

		for (AssessmentResult assessmentResult : assessmentResults) {

		    float assessmentMark = assessmentResult.getGrade();
		    int assessmentMaxMark = assessmentResult.getMaximumGrade();
		    Set<AssessmentQuestionResult> questionResults = assessmentResult.getQuestionResults();

		    // [+] if the question is modified
		    for (AssessmentQuestionResult questionResult : questionResults) {
			QbToolQuestion oldQuestion = questionResult.getQbToolQuestion();
			Float oldQuestionAnswerMark = questionResult.getMark();
			int oldResultMaxMark = questionResult.getMaxMark() == null ? 0
				: questionResult.getMaxMark().intValue();

			//check whether according question was modified
			for (AssessmentQuestion modifiedQuestion : modifiedQuestions) {
			    if (oldQuestion.getDisplayOrder() == modifiedQuestion.getDisplayOrder()) {

				//update questionResult's qbQuestion with the new one
				questionResult.setQbToolQuestion(modifiedQuestion);
				//update questionResult's qbOption - it seems to be redundant, as it's done in loadupQuestionResultIntoQuestionDto()
//				for (QbOption newOption : modifiedQuestion.getQbQuestion().getQbOptions()) {
//				    if (questionResult.getQbOption().getDisplayOrder() == newOption.getDisplayOrder()) {
//					questionResult.setQbOption(newOption);
//					break;
//				    }
//				}
				//update questionResult's optionAnswers
				for (AssessmentOptionAnswer oldOptionAnswer : questionResult.getOptionAnswers()) {

				    //find according old qbOption
				    QbOption oldOption = null;
				    for (QbOption oldOptionIter : oldQuestion.getQbQuestion().getQbOptions()) {
					if (oldOptionIter.getUid().equals(oldOptionAnswer.getOptionUid())) {
					    oldOption = oldOptionIter;
					}
				    }

				    //update
				    for (QbOption newOption : modifiedQuestion.getQbQuestion().getQbOptions()) {
					if (oldOption.getDisplayOrder() == newOption.getDisplayOrder()) {
					    oldOptionAnswer.setOptionUid(newOption.getUid());
					    break;
					}
				    }
				}

				//actually recalculate marks
				QuestionDTO modifiedQuestionDto = new QuestionDTO(modifiedQuestion);
				modifiedQuestionDto.setMaxMark(oldResultMaxMark);
				loadupQuestionResultIntoQuestionDto(modifiedQuestionDto, questionResult);
				calculateAnswerMark(assessmentUid, user.getUserId(), questionResult,
					modifiedQuestionDto);
				assessmentQuestionResultDao.saveObject(questionResult);

				float newQuestionAnswerMark = questionResult.getMark();
				assessmentMark += newQuestionAnswerMark - oldQuestionAnswerMark;
				break;
			    }
			}
		    }

		    // [+] if the question reference mark is modified
		    for (AssessmentQuestionResult questionResult : questionResults) {
			Long questionUid = questionResult.getQbToolQuestion().getUid();

			for (QuestionReference modifiedReference : modifiedReferences.keySet()) {
			    if (!modifiedReference.isRandomQuestion()
				    && questionUid.equals(modifiedReference.getQuestion().getUid())) {
				int newReferenceMaxMark = modifiedReference.getMaxMark();
				int oldReferenceMaxMark = modifiedReferences.get(modifiedReference);

				// update question answer's mark
				Float oldQuestionAnswerMark = questionResult.getMark();
				float newQuestionAnswerMark = (oldQuestionAnswerMark * newReferenceMaxMark)
					/ oldReferenceMaxMark;
				questionResult.setMark(newQuestionAnswerMark);
				questionResult.setMaxMark((float) newReferenceMaxMark);
				assessmentQuestionResultDao.saveObject(questionResult);

				assessmentMark += newQuestionAnswerMark - oldQuestionAnswerMark;
				assessmentMaxMark += newReferenceMaxMark - oldReferenceMaxMark;
				break;
			    }
			}
		    }

		    // find all question results from random question references
		    ArrayList<AssessmentQuestionResult> nonRandomQuestionResults = new ArrayList<>();
		    for (AssessmentQuestionResult questionResult : questionResults) {
			for (QuestionReference reference : newReferences) {
			    if (!reference.isRandomQuestion() && questionResult.getQbToolQuestion().getUid()
				    .equals(reference.getQuestion().getUid())) {
				nonRandomQuestionResults.add(questionResult);
			    }
			}
		    }
		    Collection<AssessmentQuestionResult> randomQuestionResults = CollectionUtils
			    .subtract(questionResults, nonRandomQuestionResults);

		    // [+] if the question reference mark is modified (in case of random question references)
		    for (QuestionReference modifiedReference : modifiedReferences.keySet()) {

			// in case of random question reference - search for the answer with the same maxmark (it does not matter to which random reference this question belong originally as the only thing that differentiate those references is maxMark)
			if (modifiedReference.isRandomQuestion()) {

			    for (AssessmentQuestionResult randomQuestionResult : randomQuestionResults) {
				int newReferenceMaxMark = modifiedReference.getMaxMark();
				int oldReferenceMaxMark = modifiedReferences.get(modifiedReference);

				if (randomQuestionResult.getMaxMark() != null
					&& randomQuestionResult.getMaxMark().intValue() == oldReferenceMaxMark) {

				    // update question answer's mark
				    Float oldQuestionResultMark = randomQuestionResult.getMark();
				    float newQuestionResultMark = (oldQuestionResultMark * newReferenceMaxMark)
					    / oldReferenceMaxMark;
				    randomQuestionResult.setMark(newQuestionResultMark);
				    randomQuestionResult.setMaxMark((float) newReferenceMaxMark);
				    assessmentQuestionResultDao.saveObject(randomQuestionResult);

				    nonRandomQuestionResults.add(randomQuestionResult);

				    assessmentMark += newQuestionResultMark - oldQuestionResultMark;
				    assessmentMaxMark += newReferenceMaxMark - oldReferenceMaxMark;
				    break;
				}
			    }
			}

		    }

		    // store new mark and maxMark if they were changed
		    storeAssessmentResultMarkAndMaxMark(assessmentResult, lastFinishedAssessmentResult, assessmentMark,
			    assessmentMaxMark, user);
		}
	    }
	}
    }

    /**
     * Store new mark and maxMark if they were changed
     */
    private void storeAssessmentResultMarkAndMaxMark(AssessmentResult assessmentResult,
	    AssessmentResult lastFinishedAssessmentResult, float newAssessmentMark, int newAssessmentMaxMark,
	    AssessmentUser user) {
	// store new mark and maxMark if they were changed
	if ((assessmentResult.getGrade() != newAssessmentMark)
		|| (assessmentResult.getMaximumGrade() != newAssessmentMaxMark)) {

	    // marks can't be below zero
	    newAssessmentMark = (newAssessmentMark < 0) ? 0 : newAssessmentMark;
	    newAssessmentMaxMark = (newAssessmentMaxMark < 0) ? 0 : newAssessmentMaxMark;

	    assessmentResult.setGrade(newAssessmentMark);
	    assessmentResult.setMaximumGrade(newAssessmentMaxMark);
	    assessmentResultDao.saveObject(assessmentResult);

	    // if this is the last finished assessment result - propagade total mark to Gradebook
	    if (lastFinishedAssessmentResult != null
		    && lastFinishedAssessmentResult.getUid().equals(assessmentResult.getUid())) {
		toolService.updateActivityMark(Double.valueOf(newAssessmentMark), null, user.getUserId().intValue(),
			user.getSession().getSessionId(), false);
	    }
	}
    }

    @Override
    public String getMessage(String key) {
	return messageService.getMessage(key);
    }

    @Override
    public String getMessage(String key, Object[] args) {
	return messageService.getMessage(key, args);
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
    public String getActivityEvaluation(Long toolContentId) {
	return toolService.getActivityEvaluation(toolContentId);
    }

    @Override
    public void setActivityEvaluation(Long toolContentId, String toolOutputDefinition) {
	toolService.setActivityEvaluation(toolContentId, toolOutputDefinition);
    }

    @Override
    public String getLearnerContentFolder(Long toolSessionId, Long userId) {
	return toolService.getLearnerContentFolder(toolSessionId, userId);
    }

    @Override
    public void notifyTeachersOnAttemptCompletion(Long sessionId, String userName) {
	String message = messageService.getMessage("event.learner.completes.attempt.body", new Object[] { userName });
	eventNotificationService.notifyLessonMonitors(sessionId, message, false);
    }

    @Override
    public List<Number> getMarksArray(Long sessionId) {
	return assessmentUserDao.getRawUserMarksBySession(sessionId);
    }

    @Override
    public List<Number> getMarksArrayForLeaders(Long toolContentId) {
	return assessmentUserDao.getRawLeaderMarksByToolContentId(toolContentId);
    }

    private LinkedHashMap<String, Integer> getMarksSummaryForSession(List<AssessmentUserDTO> userDtos, float minGrade,
	    float maxGrade, Integer numBuckets) {

	LinkedHashMap<String, Integer> summary = new LinkedHashMap<>();
	TreeMap<Integer, Integer> inProgress = new TreeMap<>();

	if (numBuckets == null) {
	    numBuckets = 10;
	}

	int bucketSize = 1;
	int intMinGrade = (int) Math.floor(minGrade);
	float gradeDifference = maxGrade - minGrade;
	if (gradeDifference <= 10) {
	    for (int i = intMinGrade; i <= (int) Math.ceil(maxGrade); i++) {
		inProgress.put(i, 0);
	    }
	} else {
	    int intGradeDifference = (int) Math.ceil(gradeDifference);
	    bucketSize = (int) Math.ceil(intGradeDifference / numBuckets);
	    for (int i = intMinGrade; i <= maxGrade; i = i + bucketSize) {
		inProgress.put(i, 0);
	    }
	}

	for (AssessmentUserDTO userDto : userDtos) {
	    float grade = userDto.getGrade();
	    int bucketStart = intMinGrade;
	    int bucketStop = bucketStart + bucketSize;
	    boolean looking = true;
	    while (bucketStart <= maxGrade && looking) {
		if (grade >= bucketStart && grade < bucketStop) {
		    inProgress.put(bucketStart, inProgress.get(bucketStart) + 1);
		    looking = false;
		} else {
		    bucketStart = bucketStop;
		    bucketStop = bucketStart + bucketSize;
		}
	    }
	}

	for (Map.Entry<Integer, Integer> entry : inProgress.entrySet()) {
	    String key;
	    if (bucketSize == 1) {
		key = entry.getKey().toString();
	    } else {
		if (maxGrade >= entry.getKey() && maxGrade <= entry.getKey() + bucketSize - 1) {
		    if ((int) maxGrade == entry.getKey()) {
			key = NumberUtil.formatLocalisedNumber(maxGrade, (Locale) null, 2);
		    } else {
			key = new StringBuilder().append(entry.getKey()).append(" - ")
				.append(NumberUtil.formatLocalisedNumber(maxGrade, (Locale) null, 2)).toString();
		    }
		} else {
		    key = new StringBuilder().append(entry.getKey()).append(" - ")
			    .append(entry.getKey() + bucketSize - .01).toString();
		}
	    }
	    summary.put(key, entry.getValue());
	}

	return summary;
    }

    @Override
    public List<QbToolQuestion> replaceQuestions(long toolContentId, String newActivityName,
	    List<QbQuestion> newQuestions) {
	Assessment assessment = getAssessmentByContentId(toolContentId);
	if (newActivityName != null) {
	    assessment.setTitle(newActivityName);
	    assessmentDao.update(assessment);
	}

	// remove all existing question references
	for (QuestionReference oldReference : assessment.getQuestionReferences()) {
	    assessmentQuestionDao.delete(oldReference);
	}
	assessment.getQuestionReferences().clear();

	// remove all existing questions
	for (AssessmentQuestion oldQuestion : assessment.getQuestions()) {
	    assessmentQuestionDao.delete(oldQuestion);
	}
	// this is needed, otherwise Hibernate wants to re-save the deleted Assessment questions
	assessment.getQuestions().clear();

	List<QbToolQuestion> result = new ArrayList<>(newQuestions.size());
	// populate Assessment with new questions and references
	int displayOrder = 1;
	for (QbQuestion qbQuestion : newQuestions) {
	    AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
	    assessmentQuestion.setDisplayOrder(displayOrder);
	    assessmentQuestion.setQbQuestion(qbQuestion);
	    assessmentQuestion.setToolContentId(toolContentId);
	    assessmentQuestionDao.insert(assessmentQuestion);
	    assessment.getQuestions().add(assessmentQuestion);
	    result.add(assessmentQuestion);

	    QuestionReference questionReference = new QuestionReference();
	    questionReference.setQuestion(assessmentQuestion);
	    questionReference.setSequenceId(displayOrder);
	    assessmentQuestionDao.insert(questionReference);
	    assessment.getQuestionReferences().add(questionReference);

	    displayOrder++;
	}
	assessmentDao.update(assessment);

	return result;
    }

    @Override
    public void replaceQuestion(long toolContentId, long oldQbQuestionUid, long newQbQuestionUid) {
	Assessment assessment = getAssessmentByContentId(toolContentId);
	QbQuestion newQbQuestion = null;
	for (AssessmentQuestion assessmentQuestion : assessment.getQuestions()) {
	    if (assessmentQuestion.getQbQuestion().getUid().equals(oldQbQuestionUid)) {
		if (newQbQuestion == null) {
		    newQbQuestion = qbService.getQuestionByUid(newQbQuestionUid);
		}
		assessmentQuestion.setQbQuestion(newQbQuestion);
	    }
	}
    }

    // *****************************************************************************
    // private methods
    // *****************************************************************************

    private Assessment getDefaultAssessment() throws AssessmentApplicationException {
	Long defaultAssessmentId = getToolDefaultContentIdBySignature(AssessmentConstants.TOOL_SIGNATURE);
	Assessment defaultAssessment = getAssessmentByContentId(defaultAssessmentId);
	if (defaultAssessment == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new AssessmentApplicationException(error);
	}

	return defaultAssessment;
    }

    private Long getToolDefaultContentIdBySignature(String toolSignature) throws AssessmentApplicationException {
	Long contentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	if (contentId == null) {
	    String error = messageService.getMessage("error.msg.default.content.not.find");
	    log.error(error);
	    throw new AssessmentApplicationException(error);
	}
	return contentId;
    }

    // *****************************************************************************
    // set methods for Spring Bean
    // *****************************************************************************

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
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

    public void setAssessmentToolContentHandler(IToolContentHandler assessmentToolContentHandler) {
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

    public void setAssessmentConfigDao(AssessmentConfigDAO assessmentConfigDao) {
	this.assessmentConfigDao = assessmentConfigDao;
    }

    // *******************************************************************************
    // ToolContentManager, ToolSessionManager methods
    // *******************************************************************************

    @Override
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

	toolContentObj = Assessment.newInstance(toolContentObj, toolContentId);
	for (AssessmentQuestion assessmentQuestion : toolContentObj.getQuestions()) {
	    qbService.prepareQuestionForExport(assessmentQuestion.getQbQuestion());
	}
	// do not export time limits if the LD gets exported from a running sequence
	toolContentObj.setTimeLimitAdjustments(null);
	toolContentObj.setAbsoluteTimeLimit(null);

	try {
	    exportContentService.exportToolContent(toolContentId, toolContentObj, assessmentToolContentHandler,
		    rootPath);
	} catch (ExportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath, String fromVersion,
	    String toVersion) throws ToolException {

	try {
	    // register version filter class
	    exportContentService.registerImportVersionFilterClass(AssessmentImportContentVersionFilter.class);

	    Object toolPOJO = exportContentService.importToolContent(toolContentPath, assessmentToolContentHandler,
		    fromVersion, toVersion);
	    if (!(toolPOJO instanceof Assessment)) {
		throw new ImportToolContentException(
			"Import Share assessment tool content failed. Deserialized object is " + toolPOJO);
	    }
	    Assessment toolContentObj = (Assessment) toolPOJO;

	    // reset it to new toolContentId
	    toolContentObj.setContentId(toolContentId);
	    AssessmentUser user = assessmentUserDao.getUserCreatedAssessment(newUserUid.longValue(), toolContentId);
	    if (user == null) {
		user = new AssessmentUser();
		UserDTO sysUser = ((User) userManagementService.findById(User.class, newUserUid)).getUserDTO();
		user.setFirstName(sysUser.getFirstName());
		user.setLastName(sysUser.getLastName());
		user.setLoginName(sysUser.getLogin());
		user.setUserId(newUserUid.longValue());
		user.setAssessment(toolContentObj);
	    }
	    toolContentObj.setCreatedBy(user);

	    long userQbCollectionUid = qbService.getUserPrivateCollection(newUserUid).getUid();

	    // we need to save QB questions and options first
	    for (AssessmentQuestion assessmentQuestion : toolContentObj.getQuestions()) {
		QbQuestion qbQuestion = assessmentQuestion.getQbQuestion();

		// try to match the question to an existing QB question in DB
		QbQuestion existingQuestion = qbService.getQuestionByUUID(qbQuestion.getUuid());
		if (existingQuestion == null) {
		    // none found, create a new QB question
		    qbService.insertQuestion(qbQuestion);
		    qbService.addQuestionToCollection(userQbCollectionUid, qbQuestion.getQuestionId(), false);
		} else {
		    // found, use the existing one
		    assessmentQuestion.setQbQuestion(existingQuestion);
		}

		assessmentDao.insert(assessmentQuestion);
		// in case an imported question had a question ID which is the highest
		qbService.updateMaxQuestionId();
	    }

	    saveOrUpdateAssessment(toolContentObj);
	} catch (ImportToolContentException e) {
	    throw new ToolException(e);
	}
    }

    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Long toolContentId, int definitionType)
	    throws ToolException {
	Assessment assessment = getAssessmentByContentId(toolContentId);
	if (assessment == null) {
	    assessment = getDefaultAssessment();
	}
	return getAssessmentOutputFactory().getToolOutputDefinitions(assessment, definitionType);
    }

    @Override
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

	Assessment toContent = Assessment.newInstance(assessment, toContentId);
	saveOrUpdateAssessment(toContent);
    }

    @Override
    public void resetDefineLater(Long toolContentId) throws DataMissingException, ToolException {
	Assessment assessment = assessmentDao.getByContentId(toolContentId);
	if (assessment == null) {
	    throw new ToolException("No found tool content by given content ID:" + toolContentId);
	}
	assessment.setDefineLater(false);
    }

    @Override
    public void removeToolContent(Long toolContentId) throws ToolException {
	Assessment assessment = assessmentDao.getByContentId(toolContentId);
	if (assessment == null) {
	    log.warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
	    return;
	}

	for (AssessmentSession session : assessmentSessionDao.getByContentId(toolContentId)) {
	    List<NotebookEntry> entries = coreNotebookService.getEntry(session.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, AssessmentConstants.TOOL_SIGNATURE);
	    for (NotebookEntry entry : entries) {
		coreNotebookService.deleteEntry(entry);
	    }
	}

	assessmentDao.delete(assessment);
    }

    @Override
    public void removeLearnerContent(Long toolContentId, Integer userId) throws ToolException {
	if (log.isDebugEnabled()) {
	    log.debug("Removing Assessment results for user ID " + userId + " and toolContentId " + toolContentId);
	}

	List<AssessmentSession> sessions = assessmentSessionDao.getByContentId(toolContentId);
	for (AssessmentSession session : sessions) {
	    List<AssessmentResult> results = assessmentResultDao.getAssessmentResultsBySession(session.getSessionId(),
		    userId.longValue());
	    for (AssessmentResult result : results) {
		for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
		    assessmentQuestionResultDao.removeObject(AssessmentQuestionResult.class, questionResult.getUid());
		}
		assessmentResultDao.removeObject(AssessmentResult.class, result.getUid());
	    }

	    AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId.longValue(),
		    session.getSessionId());
	    if (user != null) {
		NotebookEntry entry = getEntry(session.getSessionId(), userId);
		if (entry != null) {
		    assessmentDao.removeObject(NotebookEntry.class, entry.getUid());
		}

		if ((session.getGroupLeader() != null) && session.getGroupLeader().getUid().equals(user.getUid())) {
		    session.setGroupLeader(null);
		}

		// propagade changes to Gradebook
		toolService.removeActivityMark(userId, session.getSessionId());

		assessmentUserDao.removeObject(AssessmentUser.class, user.getUid());
	    }
	}
    }

    @Override
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException {
	AssessmentSession session = new AssessmentSession();
	session.setSessionId(toolSessionId);
	session.setSessionName(toolSessionName);
	Assessment assessment = assessmentDao.getByContentId(toolContentId);
	session.setAssessment(assessment);
	assessmentSessionDao.saveObject(session);
    }

    @Override
    public String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException {
	if (toolSessionId == null) {
	    log.error("Fail to leave tool Session based on null tool session id.");
	    throw new ToolException("Fail to remove tool Session based on null tool session id.");
	}
	if (learnerId == null) {
	    log.error("Fail to leave tool Session based on null learner.");
	    throw new ToolException("Fail to remove tool Session based on null learner.");
	}

	AssessmentSession session = assessmentSessionDao.getSessionBySessionId(toolSessionId);
	if (session != null) {
	    session.setStatus(AssessmentConstants.COMPLETED);
	    assessmentSessionDao.saveObject(session);
	} else {
	    log.error("Fail to leave tool Session.Could not find shared assessment " + "session by given session id: "
		    + toolSessionId);
	    throw new DataMissingException("Fail to leave tool Session."
		    + "Could not find shared assessment session by given session id: " + toolSessionId);
	}
	return toolService.completeToolSession(toolSessionId, learnerId);
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public ToolSessionExportOutputData exportToolSession(List<Long> toolSessionIds)
	    throws DataMissingException, ToolException {
	return null;
    }

    @Override
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException {
	assessmentSessionDao.deleteBySessionId(toolSessionId);
    }

    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return assessmentOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return assessmentOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return assessmentOutputFactory.getToolOutputs(name, this, toolContentId);
    }

    @Override
    public List<ConfidenceLevelDTO> getConfidenceLevels(Long toolSessionId) {
	List<ConfidenceLevelDTO> confidenceLevelDtos = new ArrayList<>();
	if (toolSessionId == null) {
	    return confidenceLevelDtos;
	}

	Assessment assessment = getAssessmentBySessionId(toolSessionId);
	//in case Assessment is leader aware return all leaders confidences, otherwise - confidences from the users from the same group as requestor
	List<Object[]> assessmentResultsAndPortraits = assessment.isUseSelectLeaderToolOuput()
		? assessmentResultDao.getLeadersLastFinishedAssessmentResults(assessment.getContentId())
		: assessmentResultDao.getLastFinishedAssessmentResultsBySession(toolSessionId);

	for (Object[] assessmentResultsAndPortraitIter : assessmentResultsAndPortraits) {
	    AssessmentResult assessmentResult = (AssessmentResult) assessmentResultsAndPortraitIter[0];
	    UUID portraitUuid = (UUID) assessmentResultsAndPortraitIter[1];
	    AssessmentUser user = assessmentResult.getUser();

	    //fill in question's and user answer's hashes
	    for (AssessmentQuestionResult questionResult : assessmentResult.getQuestionResults()) {
		QbQuestion qbQuestion = questionResult.getQbQuestion();

		List<String> answers = new LinkedList<>();
		List<Long> optionUids = new LinkedList<>();

		if (qbQuestion.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE) {

		    for (QbOption option : qbQuestion.getQbOptions()) {
			for (AssessmentOptionAnswer optionAnswer : questionResult.getOptionAnswers()) {
			    if (optionAnswer.getAnswerBoolean()
				    && (optionAnswer.getOptionUid().equals(option.getUid()))) {
				optionUids.add(option.getUid());
			    }
			}
		    }

		} else if (qbQuestion.getType() == QbQuestion.TYPE_MATCHING_PAIRS) {

		} else if (qbQuestion.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS) {
		    answers.add(questionResult.getAnswer());

		} else if (qbQuestion.getType() == QbQuestion.TYPE_NUMERICAL) {
		    answers.add(questionResult.getAnswer());

		} else if (qbQuestion.getType() == QbQuestion.TYPE_TRUE_FALSE) {
		    if (questionResult.getAnswer() != null) {
			answers.add("" + questionResult.getAnswerBoolean());
		    }

		} else if (qbQuestion.getType() == QbQuestion.TYPE_ESSAY) {
		    answers.add(questionResult.getAnswer());

		} else if (qbQuestion.getType() == QbQuestion.TYPE_ORDERING) {

		} else if (qbQuestion.getType() == QbQuestion.TYPE_MARK_HEDGING) {

		}

		for (Long optionUid : optionUids) {
		    ConfidenceLevelDTO confidenceLevelDto = new ConfidenceLevelDTO();
		    confidenceLevelDto.setUserId(user.getUserId().intValue());
		    String userName = StringUtils.isBlank(user.getFirstName())
			    && StringUtils.isBlank(user.getLastName()) ? user.getLoginName()
				    : user.getFirstName() + " " + user.getLastName();
		    confidenceLevelDto.setUserName(userName);
		    confidenceLevelDto.setPortraitUuid(portraitUuid == null ? null : portraitUuid.toString());
		    confidenceLevelDto.setLevel(questionResult.getConfidenceLevel());
		    confidenceLevelDto.setType(assessment.getConfidenceLevelsType());
		    confidenceLevelDto.setQbQuestionUid(qbQuestion.getUid());
		    confidenceLevelDto.setQbOptionUid(optionUid);

		    confidenceLevelDtos.add(confidenceLevelDto);
		}
		for (String answer : answers) {
//		    ConfidenceLevelDTO confidenceLevelDto = new ConfidenceLevelDTO();
//		    confidenceLevelDto.setUserId(userId.intValue());
//		    confidenceLevelDto.setPortraitUuid(portraitUuid);
//		    confidenceLevelDto.setLevel(questionResult.getConfidenceLevel());
//		    confidenceLevelDto.setQbQuestionUid(qbQuestion.getUid());
//		    confidenceLevelDto.setQbOptionUid(optionUid);
//
//		    confidenceLevelDtos.add(confidenceLevelDto);
		}
	    }

	}

	return confidenceLevelDtos;
    }

    @Override
    public Collection<VsaAnswerDTO> getVsaAnswers(Long toolSessionId) {
	if (toolSessionId == null) {
	    return new ArrayList<>();
	}

	Map<String, VsaAnswerDTO> uid_answerToVsaAnswerDtoMap = new LinkedHashMap<>();

	Assessment assessment = getAssessmentBySessionId(toolSessionId);
	//in case Assessment is leader aware return all leaders confidences, otherwise - confidences from the users from the same group as requestor
	List<Object[]> assessmentResultsAndPortraits = assessment.isUseSelectLeaderToolOuput()
		? assessmentResultDao.getLeadersLastFinishedAssessmentResults(assessment.getContentId())
		: assessmentResultDao.getLastFinishedAssessmentResultsBySession(toolSessionId);

	for (Object[] assessmentResultsAndPortraitIter : assessmentResultsAndPortraits) {
	    AssessmentResult assessmentResult = (AssessmentResult) assessmentResultsAndPortraitIter[0];
	    UUID portraitUuid = (UUID) assessmentResultsAndPortraitIter[1];
	    AssessmentUser user = assessmentResult.getUser();

	    //fill in question's and user answer's hashes
	    for (AssessmentQuestionResult questionResult : assessmentResult.getQuestionResults()) {
		QbQuestion qbQuestion = questionResult.getQbQuestion();

		if (qbQuestion.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS) {
		    //uid_answer should be unique in the final list
		    String uid_answer = qbQuestion.getUid() + "_" + questionResult.getAnswer();

		    //find VsaAnswerDTO in the map, or create the new one if it doesn't exist
		    VsaAnswerDTO vsaAnswerDTO;
		    if (uid_answerToVsaAnswerDtoMap.containsKey(uid_answer)) {
			vsaAnswerDTO = uid_answerToVsaAnswerDtoMap.get(uid_answer);

		    } else {
			vsaAnswerDTO = new VsaAnswerDTO();
			vsaAnswerDTO.setQbQuestionUid(qbQuestion.getUid());
			vsaAnswerDTO.setAnswer(questionResult.getAnswer());
			vsaAnswerDTO.setCorrect(questionResult.getMark() > 0);
			vsaAnswerDTO.setUserId(user.getUserId());
			uid_answerToVsaAnswerDtoMap.put(uid_answer, vsaAnswerDTO);

			//find and store optionUid
			for (QbOption option : qbQuestion.getQbOptions()) {
			    for (AssessmentOptionAnswer optionAnswer : questionResult.getOptionAnswers()) {
				if (optionAnswer.getAnswerBoolean()
					&& (optionAnswer.getOptionUid().equals(option.getUid()))) {
				    Long optionUid = option.getUid();
				    vsaAnswerDTO.setQbOptionUid(optionUid);
				    break;
				}
			    }
			}
		    }

		    ConfidenceLevelDTO confidenceLevelDto = new ConfidenceLevelDTO();
		    confidenceLevelDto.setUserId(user.getUserId().intValue());
		    String userName = StringUtils.isBlank(user.getFirstName())
			    && StringUtils.isBlank(user.getLastName()) ? user.getLoginName()
				    : user.getFirstName() + " " + user.getLastName();
		    confidenceLevelDto.setUserName(userName);
		    confidenceLevelDto.setPortraitUuid(portraitUuid == null ? null : portraitUuid.toString());
		    confidenceLevelDto.setLevel(questionResult.getConfidenceLevel());
		    confidenceLevelDto.setType(assessment.getConfidenceLevelsType());

		    vsaAnswerDTO.getConfidenceLevels().add(confidenceLevelDto);
		}
	    }

	}

	return uid_answerToVsaAnswerDtoMap.values();
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	AssessmentSession session = getSessionBySessionId(toolSessionId);
	if ((session == null) || (session.getAssessment() == null)) {
	    return;
	}
	Assessment assessment = session.getAssessment();

	AssessmentUser assessmentUser = getUserByIDAndSession(userId, toolSessionId);
	// create user if he hasn't accessed this activity yet
	if (assessmentUser == null) {
	    assessmentUser = new AssessmentUser(user.getUserDTO(), session);
	    createUser(assessmentUser);

	    setAttemptStarted(assessment, assessmentUser, toolSessionId, null);
	}

	//finalize the latest result, if it's still active
	AssessmentResult lastAssessmentResult = getLastAssessmentResult(assessment.getUid(), userId);
	if (lastAssessmentResult != null && lastAssessmentResult.getFinishDate() == null) {
	    lastAssessmentResult.setFinishDate(new Date());
	    lastAssessmentResult.getQuestionResults()
		    .forEach(questionResult -> questionResult.setFinishDate(new Date()));
	    assessmentResultDao.update(lastAssessmentResult);
	}

	//if this is a leader finishes, complete all non-leaders as well, also copy leader results to them
	AssessmentUser groupLeader = checkLeaderSelectToolForSessionLeader(assessmentUser, toolSessionId);
	if (isUserGroupLeader(userId, toolSessionId)) {
	    session.getAssessmentUsers().forEach(sessionUser -> {
		//finish non-leader
		sessionUser.setSessionFinished(true);
		assessmentUserDao.saveObject(user);

		//copy answers from leader to non-leaders
		copyAnswersFromLeader(sessionUser, groupLeader);
	    });

	} else {
	    assessmentUser.setSessionFinished(true);
	    assessmentUserDao.saveObject(user);
	}

    }

    @Override
    public boolean isContentEdited(Long toolContentId) {
	return getAssessmentByContentId(toolContentId).isDefineLater();
    }

    @Override
    public boolean isReadOnly(Long toolContentId) {
	for (AssessmentSession session : assessmentSessionDao.getByContentId(toolContentId)) {
	    if (!session.getAssessmentUsers().isEmpty()) {
		return true;
	    }
	}
	return false;
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

    public void setLearnerService(ILearnerService learnerService) {
	this.learnerService = learnerService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public IEventNotificationService getEventNotificationService() {
	return eventNotificationService;
    }

    public void setEventNotificationService(IEventNotificationService eventNotificationService) {
	this.eventNotificationService = eventNotificationService;
    }

    public void setQbService(IQbService qbService) {
	this.qbService = qbService;
    }

    public void setOutcomeService(IOutcomeService outcomeService) {
	this.outcomeService = outcomeService;
    }

    public AssessmentOutputFactory getAssessmentOutputFactory() {
	return assessmentOutputFactory;
    }

    public void setAssessmentOutputFactory(AssessmentOutputFactory assessmentOutputFactory) {
	this.assessmentOutputFactory = assessmentOutputFactory;
    }

    public void setLearnerInteractionService(ILearnerInteractionService learnerInteractionService) {
	this.learnerInteractionService = learnerInteractionService;
    }

    @Override
    public Class<?>[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getAssessmentOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    @Override
    public String getToolContentTitle(Long toolContentId) {
	return getAssessmentByContentId(toolContentId).getTitle();
    }

    @Override
    public ToolCompletionStatus getCompletionStatus(Long learnerId, Long toolSessionId) {
	AssessmentUser learner = getUserByIDAndSession(learnerId, toolSessionId);
	if (learner == null) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_NOT_ATTEMPTED, null, null);
	}

	Assessment assessment = getAssessmentBySessionId(toolSessionId);
	List<AssessmentResult> results = assessmentResultDao.getAssessmentResults(assessment.getUid(),
		learner.getUserId());
	Date startDate = null;
	Date finishDate = null;
	for (AssessmentResult result : results) {
	    if (startDate == null || (result.getStartDate() != null && result.getStartDate().before(startDate))) {
		startDate = result.getStartDate();
	    }
	    if (finishDate == null || (result.getFinishDate() != null && result.getFinishDate().after(finishDate))) {
		finishDate = result.getFinishDate();
	    }
	}

	if (learner.isSessionFinished()) {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_COMPLETED, startDate, finishDate);
	} else {
	    return new ToolCompletionStatus(ToolCompletionStatus.ACTIVITY_ATTEMPTED, startDate, null);
	}
    }
    // ****************** REST methods *************************

    /**
     * Rest call to create a new Assessment content. Required fields in toolContentJSON: "title", "instructions",
     * "questions", "firstName", "lastName", "lastName", "questions" and "references".
     *
     * The questions entry should be a ArrayNode containing JSON objects, which in turn must contain "questionTitle",
     * "questionText", "displayOrder" (Integer), "type" (Integer). If the type is Multiple Choice, Numerical or Matching
     * Pairs then a ArrayNode "answers" is required.
     *
     * The answers entry should be ArrayNode containing JSON objects, which in turn must contain "answerText" or
     * "answerFloat", "displayOrder" (Integer), "grade" (Integer).
     *
     * The references entry should be a ArrayNode containing JSON objects, which in turn must contain "displayOrder"
     * (Integer), "questionDisplayOrder" (Integer - to match to the question). It may also have "maxMark" (Integer)
     * and "randomQuestion" (Boolean)
     *
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, ObjectNode toolContentJSON)
	    throws IOException {

	Assessment assessment = new Assessment();
	assessment.setContentId(toolContentID);
	assessment.setTitle(toolContentJSON.get(RestTags.TITLE).asText());
	assessment.setInstructions(toolContentJSON.get(RestTags.INSTRUCTIONS).asText());
	assessment.setCreated(new Date());

	assessment.setReflectOnActivity(
		JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	assessment.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS));
	assessment.setAllowGradesAfterAttempt(
		JsonUtil.optBoolean(toolContentJSON, "allowGradesAfterAttempt", Boolean.FALSE));
	assessment
		.setAllowHistoryResponses(JsonUtil.optBoolean(toolContentJSON, "allowHistoryResponses", Boolean.FALSE));
	assessment.setAllowOverallFeedbackAfterQuestion(
		JsonUtil.optBoolean(toolContentJSON, "allowOverallFeedbackAfterQuestion", Boolean.FALSE));
	assessment
		.setAllowQuestionFeedback(JsonUtil.optBoolean(toolContentJSON, "allowQuestionFeedback", Boolean.FALSE));
	assessment.setAllowDiscloseAnswers(JsonUtil.optBoolean(toolContentJSON, "allowDiscloseAnswers", Boolean.FALSE));
	assessment.setAllowRightAnswersAfterQuestion(
		JsonUtil.optBoolean(toolContentJSON, "allowRightAnswersAfterQuestion", Boolean.FALSE));
	assessment.setAllowWrongAnswersAfterQuestion(
		JsonUtil.optBoolean(toolContentJSON, "allowWrongAnswersAfterQuestion", Boolean.FALSE));
	assessment.setEnableConfidenceLevels(
		JsonUtil.optBoolean(toolContentJSON, RestTags.ENABLE_CONFIDENCE_LEVELS, Boolean.FALSE));
	if (assessment.isEnableConfidenceLevels()) {
	    assessment.setConfidenceLevelsType(ConfidenceLevelDTO.CONFIDENCE_LEVELS_TYPE_0_TO_100);
	}
	assessment.setAttemptsAllowed(JsonUtil.optInt(toolContentJSON, "attemptsAllows", 1));
	assessment.setDefineLater(false);
	assessment.setDisplaySummary(JsonUtil.optBoolean(toolContentJSON, "displaySummary", Boolean.FALSE));
	assessment.setNotifyTeachersOnAttemptCompletion(
		JsonUtil.optBoolean(toolContentJSON, "notifyTeachersOnAttemptCompletion", Boolean.FALSE));
	assessment.setNumbered(JsonUtil.optBoolean(toolContentJSON, "numbered", Boolean.TRUE));
	assessment.setPassingMark(JsonUtil.optInt(toolContentJSON, "passingMark", 0));
	assessment.setQuestionsPerPage(JsonUtil.optInt(toolContentJSON, "questionsPerPage", 0));
	assessment.setReflectInstructions(JsonUtil.optString(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, ""));
	assessment.setReflectOnActivity(
		JsonUtil.optBoolean(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	assessment.setShuffled(JsonUtil.optBoolean(toolContentJSON, "shuffled", Boolean.FALSE));
	assessment.setRelativeTimeLimit(JsonUtil.optInt(toolContentJSON, "timeLimit", 0));
	assessment.setUseSelectLeaderToolOuput(
		JsonUtil.optBoolean(toolContentJSON, RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, Boolean.FALSE));
	assessment.setAllowAnswerJustification(
		JsonUtil.optBoolean(toolContentJSON, "allowAnswerJustification", Boolean.FALSE));

	// submission deadline set in monitoring

	if (toolContentJSON.has("overallFeedback")) {
	    throw new IOException(
		    "Assessment Tool does not support Overall Feedback for REST Authoring. " + toolContentJSON);
	}

	AssessmentUser assessmentUser = getUserCreatedAssessment(userID.longValue(), toolContentID);
	if (assessmentUser == null) {
	    assessmentUser = new AssessmentUser();
	    assessmentUser.setFirstName(toolContentJSON.get("firstName").asText());
	    assessmentUser.setLastName(toolContentJSON.get("lastName").asText());
	    assessmentUser.setLoginName(toolContentJSON.get("loginName").asText());
	    assessmentUser.setAssessment(assessment);
	}
	assessment.setCreatedBy(assessmentUser);

	ArrayNode questions = JsonUtil.optArray(toolContentJSON, "questions");
	Set<AssessmentQuestion> newQuestionSet = assessment.getQuestions(); // the Assessment constructor will set up the collection

	if (questions != null) {

	    QbCollection collection = null;
	    Set<String> collectionUUIDs = null;

	    for (JsonNode questionJSONData : questions) {
		AssessmentQuestion question = new AssessmentQuestion();
		Integer type = JsonUtil.optInt(questionJSONData, "type");
		question.setToolContentId(toolContentID);
		question.setDisplayOrder(JsonUtil.optInt(questionJSONData, RestTags.DISPLAY_ORDER));
		question.setAnswerRequired(JsonUtil.optBoolean(questionJSONData, "answerRequired", Boolean.FALSE));

		QbQuestion oldQbQuestion = null;
		QbQuestion qbQuestion = null;
		String uuid = JsonUtil.optString(questionJSONData, RestTags.QUESTION_UUID);

		// try to match the question to an existing QB question in DB
		if (StringUtils.isNotBlank(uuid)) {
		    oldQbQuestion = qbService.getQuestionByUUID(UUID.fromString(uuid));
		}
		boolean isModification = oldQbQuestion != null;

		// are we modifying an existing question or creating a new one
		if (isModification) {
		    qbQuestion = oldQbQuestion.clone();
		    qbService.releaseFromCache(oldQbQuestion);
		} else {
		    qbQuestion = new QbQuestion();
		    qbQuestion.setQuestionId(qbService.generateNextQuestionId());
		    qbQuestion.setContentFolderId(FileUtil.generateUniqueContentFolderID());
		}

		qbQuestion.setType(type);
		qbQuestion.setName(JsonUtil.optString(questionJSONData, RestTags.QUESTION_TITLE));
		qbQuestion.setDescription(JsonUtil.optString(questionJSONData, RestTags.QUESTION_TEXT));

		qbQuestion.setAllowRichEditor(
			JsonUtil.optBoolean(questionJSONData, RestTags.ALLOW_RICH_TEXT_EDITOR, Boolean.FALSE));
		qbQuestion.setCaseSensitive(JsonUtil.optBoolean(questionJSONData, "caseSensitive", Boolean.FALSE));
		qbQuestion.setCorrectAnswer(JsonUtil.optBoolean(questionJSONData, "correctAnswer", Boolean.FALSE));
		qbQuestion.setMaxMark(JsonUtil.optInt(questionJSONData, "defaultGrade", 1));
		qbQuestion.setFeedback(JsonUtil.optString(questionJSONData, "feedback"));
		qbQuestion.setFeedbackOnCorrect(JsonUtil.optString(questionJSONData, "feedbackOnCorrect"));
		qbQuestion.setFeedbackOnIncorrect(JsonUtil.optString(questionJSONData, "feedbackOnIncorrect"));
		qbQuestion.setFeedbackOnPartiallyCorrect(
			JsonUtil.optString(questionJSONData, "feedbackOnPartiallyCorrect"));
		qbQuestion.setMaxWordsLimit(JsonUtil.optInt(questionJSONData, "maxWordsLimit", 0));
		qbQuestion.setMinWordsLimit(JsonUtil.optInt(questionJSONData, "minWordsLimit", 0));
		qbQuestion.setMultipleAnswersAllowed(
			JsonUtil.optBoolean(questionJSONData, "multipleAnswersAllowed", Boolean.FALSE));
		qbQuestion.setIncorrectAnswerNullifiesMark(
			JsonUtil.optBoolean(questionJSONData, "incorrectAnswerNullifiesMark", Boolean.FALSE));
		qbQuestion.setPenaltyFactor(JsonUtil.optDouble(questionJSONData, "penaltyFactor", 0.0).floatValue());

		if (!isModification) {
		    // UUID normally gets generated in the DB, but we need it immediately,
		    // so we generate it programmatically.
		    // Re-reading the QbQuestion we just saved does not help as it is read from Hibernate cache,
		    // not from DB where UUID is filed
		    qbQuestion.setUuid(UUID.randomUUID());
		    assessmentDao.insert(qbQuestion);
		}

		if ((type == QbQuestion.TYPE_MATCHING_PAIRS) || (type == QbQuestion.TYPE_MULTIPLE_CHOICE)
			|| (type == QbQuestion.TYPE_NUMERICAL) || (type == QbQuestion.TYPE_MARK_HEDGING)) {

		    if (!questionJSONData.has(RestTags.ANSWERS)) {
			throw new IOException("REST Authoring is missing answers for a question of type " + type
				+ ". Data:" + toolContentJSON);
		    }

		    List<QbOption> optionList = new ArrayList<>();
		    ArrayNode optionsData = JsonUtil.optArray(questionJSONData, RestTags.ANSWERS);
		    for (JsonNode answerData : optionsData) {
			int displayOrder = JsonUtil.optInt(answerData, RestTags.DISPLAY_ORDER);
			QbOption option = null;
			// check if existing question gets modified or do we create a new one
			for (QbOption existingOption : qbQuestion.getQbOptions()) {
			    if (existingOption.getDisplayOrder() == displayOrder) {
				option = existingOption;
				break;
			    }
			}
			if (option == null) {
			    option = new QbOption();
			    option.setDisplayOrder(displayOrder);
			    option.setQbQuestion(qbQuestion);
			}

			Boolean correct = JsonUtil.optBoolean(answerData, RestTags.CORRECT, null);
			if (correct == null) {
			    Double grade = JsonUtil.optDouble(answerData, "grade");
			    option.setMaxMark(grade == null ? 0 : grade.floatValue());
			} else {
			    option.setMaxMark(correct ? 1 : 0);
			}
			option.setAcceptedError(JsonUtil.optDouble(answerData, "acceptedError", 0.0).floatValue());
			option.setFeedback(JsonUtil.optString(answerData, "feedback"));
			option.setName(JsonUtil.optString(answerData, RestTags.ANSWER_TEXT));
			option.setNumericalOption(JsonUtil.optDouble(answerData, "answerFloat", 0.0).floatValue());
			// option.setQuestion(question); can't find the use for this field yet!
			optionList.add(option);
		    }
		    qbQuestion.setQbOptions(optionList);
		}

		Long collectionUid = JsonUtil.optLong(questionJSONData, RestTags.COLLECTION_UID);
		boolean addToCollection = collectionUid != null;
		if (addToCollection) {
		    // check if it is the same collection - there is a good chance it is
		    if (collection == null || collectionUid != collection.getUid()) {
			collection = qbService.getCollection(collectionUid);
			if (collection == null) {
			    addToCollection = false;
			} else {
			    collectionUUIDs = qbService.getCollectionQuestions(collection.getUid()).stream()
				    .filter(q -> q.getUuid() != null)
				    .collect(Collectors.mapping(q -> q.getUuid().toString(), Collectors.toSet()));
			}
		    }
		}

		if (isModification) {
		    if (collectionUUIDs != null) {
			addToCollection &= !collectionUUIDs.contains(uuid);
		    }

		    int isModified = qbQuestion.isQbQuestionModified(oldQbQuestion);
		    if (isModified == IQbService.QUESTION_MODIFIED_VERSION_BUMP) {
			qbQuestion.clearID();
			qbQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()) + 1);
			qbQuestion.setCreateDate(new Date());
			qbQuestion.setUuid(UUID.randomUUID());
			assessmentDao.insert(qbQuestion);
		    } else if (isModified == IQbService.QUESTION_MODIFIED_NONE) {
			// Changes to question and option content does not count as version bump,
			// but rather as update of the original question
			// They should probably be marked as "update" rather than "none"
			assessmentDao.update(qbQuestion);
		    } else {
			throw new IllegalArgumentException(
				"Implement other Question Bank modification levels in Assessment tool");
		    }
		} else {
		    // only process learning outcomes when this is not a modification, i.e. it is a new question
		    ArrayNode learningOutcomesJSON = JsonUtil.optArray(questionJSONData, RestTags.LEARNING_OUTCOMES);
		    if (learningOutcomesJSON != null && learningOutcomesJSON.size() > 0) {
			for (JsonNode learningOutcomeJSON : learningOutcomesJSON) {
			    String learningOutcomeText = learningOutcomeJSON.asText();
			    learningOutcomeText = learningOutcomeText.strip();
			    List<Outcome> learningOutcomes = userManagementService.findByProperty(Outcome.class, "name",
				    learningOutcomeText);
			    Outcome learningOutcome = null;
			    if (learningOutcomes.isEmpty()) {
				learningOutcome = outcomeService.createOutcome(learningOutcomeText, userID);
			    } else {
				learningOutcome = learningOutcomes.get(0);
			    }

			    OutcomeMapping outcomeMapping = new OutcomeMapping();
			    outcomeMapping.setOutcome(learningOutcome);
			    outcomeMapping.setQbQuestionId(qbQuestion.getQuestionId());
			    userManagementService.save(outcomeMapping);
			}
		    }
		}

		// Store it back into JSON so Scratchie can read it
		// and use the same questions, not create new ones
		uuid = qbQuestion.getUuid().toString();
		ObjectNode questionData = (ObjectNode) questionJSONData;
		questionData.put(RestTags.QUESTION_UUID, uuid);

		// question.setUnits(units); Needed for numerical type question
		question.setQbQuestion(qbQuestion);
		checkType(question.getType());
		newQuestionSet.add(question);

		// all questions need to end up in user's private collection
		if (addToCollection) {
		    qbService.addQuestionToCollection(collectionUid, qbQuestion.getQuestionId(), false);
		    collectionUUIDs.add(uuid);
		}
	    }
	}

	// **************************** Now set up the references to the questions in the bank *********************
	ArrayNode references = JsonUtil.optArray(toolContentJSON, "references");
	if (references != null) {
	    Set<QuestionReference> newReferenceSet = assessment.getQuestionReferences(); // the Assessment constructor will set up the

	    // collection
	    for (JsonNode referenceJSONData : references) {
		QuestionReference reference = new QuestionReference();
		reference.setMaxMark(JsonUtil.optInt(referenceJSONData, "maxMark", 1));
		reference.setSequenceId(JsonUtil.optInt(referenceJSONData, RestTags.DISPLAY_ORDER));
		AssessmentQuestion matchingQuestion = matchQuestion(newQuestionSet,
			JsonUtil.optInt(referenceJSONData, "questionDisplayOrder"));
		if (matchingQuestion == null) {
		    throw new IOException("Unable to find matching question for displayOrder "
			    + referenceJSONData.get("questionDisplayOrder") + ". Data:" + toolContentJSON);
		}
		reference.setQuestion(matchingQuestion);
		reference.setRandomQuestion(JsonUtil.optBoolean(referenceJSONData, "randomQuestion", Boolean.FALSE));
		newReferenceSet.add(reference);
	    }
	}

	saveOrUpdateAssessment(assessment);

    }

    // find the question that matches this sequence id - used by the REST calls only.
    AssessmentQuestion matchQuestion(Set<AssessmentQuestion> newReferenceSet, Integer displayOrder) {
	if (displayOrder != null) {
	    for (AssessmentQuestion question : newReferenceSet) {
		if (displayOrder.equals(question.getDisplayOrder())) {
		    return question;
		}
	    }
	}
	return null;
    }

    // TODO Implement REST support for all types and then remove checkType method
    void checkType(Integer type) throws IOException {
	if ((type != QbQuestion.TYPE_ESSAY) && (type != QbQuestion.TYPE_MULTIPLE_CHOICE)) {
	    throw new IOException(
		    "Assessment Tool does not support REST Authoring for anything but Essay Type and Multiple Choice. Found type "
			    + type);
	}
    }

    @Override
    public AssessmentQuestion getAssessmentQuestionByUid(Long questionUid) {
	return assessmentQuestionDao.getByUid(questionUid);
    }

    @Override
    public void notifyLearnersOnAnswerDisclose(long toolContentId) {
	List<AssessmentSession> sessions = assessmentSessionDao.getByContentId(toolContentId);
	Set<Integer> userIds = new HashSet<>();
	for (AssessmentSession session : sessions) {
	    for (AssessmentUser user : session.getAssessmentUsers()) {
		userIds.add(user.getUserId().intValue());
	    }
	}

	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "assessment-results-refresh-" + toolContentId);
	learnerService.createCommandForLearners(toolContentId, userIds, jsonCommand.toString());
    }

    @Override
    public void setConfigValue(String key, String value) {
	assessmentConfigDao.setConfigValue(key, value);
    }

    @Override
    public String getConfigValue(String key) {
	return assessmentConfigDao.getConfigValue(key);
    }

    @Override
    public Collection<User> getAllGroupUsers(Long toolSessionId) {
	return toolService.getToolSession(toolSessionId).getLearners();
    }

    @Override
    public Grouping getGrouping(long toolContentId) {
	ToolActivity toolActivity = (ToolActivity) userManagementService
		.findByProperty(ToolActivity.class, "toolContentId", toolContentId).get(0);
	return toolActivity.getApplyGrouping() ? toolActivity.getGrouping() : null;
    }

    @Override
    public List<User> getPossibleIndividualTimeLimitUsers(long toolContentId, String searchString) {
	Lesson lesson = lessonService.getLessonByToolContentId(toolContentId);
	return lessonService.getLessonLearners(lesson.getLessonId(), searchString, null, null, true);
    }

    @Override
    public Map<Integer, Integer> getCountAnsweredQuestionsByUsers(long toolContentId) {
	Map<Integer, Integer> answeredQuestions = assessmentResultDao.countAnsweredQuestionsByUsers(toolContentId);
	if (answeredQuestions.isEmpty()) {
	    return answeredQuestions;
	}

	Assessment assessment = getAssessmentByContentId(toolContentId);
	int questionCount = assessment.getQuestions().size();

	// list all question counts, from 0 to maximum possible questions
	Map<Integer, Integer> result = new HashMap<>();
	for (int i = 0; i <= questionCount; i++) {
	    result.put(i, 0);
	}
	result.putAll(answeredQuestions);
	return result;
    }

    @Override
    public void changeLeaderForGroup(long toolSessionId, long leaderUserId) {
	AssessmentSession session = getSessionBySessionId(toolSessionId);
	if (AssessmentConstants.COMPLETED == session.getStatus()) {
	    throw new InvalidParameterException("Attempting to assing a new leader with user ID " + leaderUserId
		    + " to a finished session wtih ID " + toolSessionId);
	}

	AssessmentUser existingLeader = session.getGroupLeader();
	if (existingLeader == null || existingLeader.getUserId().equals(leaderUserId)) {
	    return;
	}
	Assessment assessment = session.getAssessment();
	AssessmentUser newLeader = getUserByIdAndContent(leaderUserId, assessment.getContentId());
	if (newLeader == null) {
	    return;
	}
	if (!newLeader.getSession().getSessionId().equals(toolSessionId)) {
	    throw new InvalidParameterException("User with ID " + leaderUserId + " belongs to session with ID "
		    + newLeader.getSession().getSessionId() + " and not to session with ID " + toolSessionId);
	}

	AssessmentResult newLeaderResult = getLastAssessmentResult(assessment.getUid(), leaderUserId);
	if (newLeaderResult != null) {
	    assessmentDao.delete(newLeaderResult);
	}

	AssessmentResult existingLeaderResult = getLastAssessmentResult(assessment.getUid(),
		existingLeader.getUserId());
	if (existingLeaderResult != null) {
	    if (existingLeaderResult.getFinishDate() != null) {
		throw new InvalidParameterException("Attempting to assing a finished result of leader with user ID "
			+ existingLeader.getUserId() + " to a new leader with user ID " + leaderUserId
			+ " in session wtih ID " + toolSessionId);
	    }

	    existingLeaderResult.setUser(newLeader);
	    assessmentDao.update(existingLeaderResult);
	}

	session.setGroupLeader(newLeader);
	assessmentDao.update(session);

	Set<Integer> userIds = session.getAssessmentUsers().stream().collect(
		Collectors.mapping(assessmentUser -> assessmentUser.getUserId().intValue(), Collectors.toSet()));

	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("hookTrigger", "assessment-leader-change-refresh-" + toolSessionId);
	learnerService.createCommandForLearners(assessment.getContentId(), userIds, jsonCommand.toString());
    }
}