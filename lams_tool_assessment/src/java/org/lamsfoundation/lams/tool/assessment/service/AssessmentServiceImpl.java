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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
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
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
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
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUnit;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentQuestionResultComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentToolContentHandler;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
public class AssessmentServiceImpl
	implements IAssessmentService, ToolContentManager, ToolSessionManager, ToolRestManager {
    private static Logger log = Logger.getLogger(AssessmentServiceImpl.class.getName());

    private AssessmentDAO assessmentDao;

    private AssessmentQuestionDAO assessmentQuestionDao;

    private AssessmentUserDAO assessmentUserDao;

    private AssessmentSessionDAO assessmentSessionDao;

    private AssessmentQuestionResultDAO assessmentQuestionResultDao;

    private AssessmentResultDAO assessmentResultDao;

    // tool service
    private AssessmentToolContentHandler assessmentToolContentHandler;

    private MessageService messageService;

    private AssessmentOutputFactory assessmentOutputFactory;

    // system services

    private ILamsToolService toolService;

    private ILearnerService learnerService;

    private IAuditService auditService;

    private IUserManagementService userManagementService;

    private IExportToolContentService exportContentService;

    private IGradebookService gradebookService;

    private ICoreNotebookService coreNotebookService;

    private IEventNotificationService eventNotificationService;

    // *******************************************************************************
    // Service method
    // *******************************************************************************
    @Override
    public boolean isUserGroupLeader(AssessmentUser user, Long toolSessionId) {

	AssessmentSession session = getAssessmentSessionBySessionId(toolSessionId);
	AssessmentUser groupLeader = session.getGroupLeader();

	return (groupLeader != null) && user.getUserId().equals(groupLeader.getUserId());
    }

    @Override
    public AssessmentUser checkLeaderSelectToolForSessionLeader(AssessmentUser user, Long toolSessionId) {
	if ((user == null) || (toolSessionId == null)) {
	    return null;
	}

	AssessmentSession assessmentSession = getAssessmentSessionBySessionId(toolSessionId);
	AssessmentUser leader = assessmentSession.getGroupLeader();
	// check leader select tool for a leader only in case QA tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {

	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getUserId().intValue());
	    if (leaderUserId != null) {
		leader = getUserByIDAndSession(leaderUserId, toolSessionId);

		// create new user in a DB
		if (leader == null) {
		    AssessmentServiceImpl.log.debug("creating new user with userId: " + leaderUserId);
		    User leaderDto = (User) getUserManagementService().findById(User.class, leaderUserId.intValue());
		    String userName = leaderDto.getLogin();
		    String fullName = leaderDto.getFirstName() + " " + leaderDto.getLastName();
		    leader = new AssessmentUser(leaderDto.getUserDTO(), assessmentSession);
		    createUser(leader);
		}

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
	AssessmentResult userResult = assessmentResultDao.getLastAssessmentResult(assessmentUid, user.getUserId());
	Set<AssessmentQuestionResult> leaderQuestionResults = leaderResult.getQuestionResults();

	// if response doesn't exist create new empty objects which we populate on the next step
	if (userResult == null) {
	    userResult = new AssessmentResult();
	    userResult.setAssessment(leaderResult.getAssessment());
	    userResult.setUser(user);
	    userResult.setSessionId(leaderResult.getSessionId());

	    Set<AssessmentQuestionResult> userQuestionResults = userResult.getQuestionResults();
	    for (AssessmentQuestionResult leaderQuestionResult : leaderQuestionResults) {
		AssessmentQuestionResult userQuestionResult = new AssessmentQuestionResult();
		userQuestionResult.setAssessmentQuestion(leaderQuestionResult.getAssessmentQuestion());
		userQuestionResult.setAssessmentResult(userResult);
		userQuestionResults.add(userQuestionResult);

		Set<AssessmentOptionAnswer> leaderOptionAnswers = leaderQuestionResult.getOptionAnswers();
		Set<AssessmentOptionAnswer> userOptionAnswers = userQuestionResult.getOptionAnswers();
		for (AssessmentOptionAnswer leaderOptionAnswer : leaderOptionAnswers) {
		    AssessmentOptionAnswer userOptionAnswer = new AssessmentOptionAnswer();
		    userOptionAnswer.setOptionUid(leaderOptionAnswer.getOptionUid());
		    userOptionAnswers.add(userOptionAnswer);
		}
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
		if (userQuestionResult.getAssessmentQuestion().getUid()
			.equals(leaderQuestionResult.getAssessmentQuestion().getUid())) {

		    userQuestionResult.setAnswerString(leaderQuestionResult.getAnswerString());
		    userQuestionResult.setAnswerFloat(leaderQuestionResult.getAnswerFloat());
		    userQuestionResult.setAnswerBoolean(leaderQuestionResult.getAnswerBoolean());
		    userQuestionResult.setSubmittedOptionUid(leaderQuestionResult.getSubmittedOptionUid());
		    userQuestionResult.setMark(leaderQuestionResult.getMark());
		    userQuestionResult.setMaxMark(leaderQuestionResult.getMaxMark());
		    userQuestionResult.setPenalty(leaderQuestionResult.getPenalty());

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
    public void launchTimeLimit(Long assessmentUid, Long userId) {
	AssessmentResult lastResult = getLastAssessmentResult(assessmentUid, userId);
	lastResult.setTimeLimitLaunchedDate(new Date());
	assessmentResultDao.saveObject(lastResult);
    }

    @Override
    public long getSecondsLeft(Assessment assessment, AssessmentUser user) {
	AssessmentResult lastResult = getLastAssessmentResult(assessment.getUid(), user.getUserId());

	long secondsLeft = 1;
	if (assessment.getTimeLimit() != 0) {
	    // if user has pressed OK button already - calculate remaining time, and full time otherwise
	    boolean isTimeLimitNotLaunched = (lastResult == null) || (lastResult.getTimeLimitLaunchedDate() == null);
	    secondsLeft = isTimeLimitNotLaunched ? assessment.getTimeLimit() * 60
		    : assessment.getTimeLimit() * 60
			    - (System.currentTimeMillis() - lastResult.getTimeLimitLaunchedDate().getTime()) / 1000;
	    // change negative or zero number to 1
	    secondsLeft = Math.max(1, secondsLeft);
	}

	return secondsLeft;
    }

    @Override
    public boolean checkTimeLimitExceeded(Assessment assessment, AssessmentUser groupLeader) {
	int timeLimit = assessment.getTimeLimit();
	if (timeLimit == 0) {
	    return false;
	}

	AssessmentResult lastLeaderResult = getLastAssessmentResult(assessment.getUid(), groupLeader.getUserId());

	//check if the time limit is exceeded
	return (lastLeaderResult != null) && (lastLeaderResult.getTimeLimitLaunchedDate() != null)
		&& lastLeaderResult.getTimeLimitLaunchedDate().getTime() + timeLimit * 60000 < System
			.currentTimeMillis();
    }

    @Override
    public List<AssessmentUser> getUsersBySession(Long toolSessionID) {
	return assessmentUserDao.getBySessionID(toolSessionID);
    }

    @Override
    public List<AssessmentUserDTO> getPagedUsersBySession(Long sessionId, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return assessmentUserDao.getPagedUsersBySession(sessionId, page, size, sortBy, sortOrder, searchString);
    }

    @Override
    public int getCountUsersBySession(Long sessionId, String searchString) {
	return assessmentUserDao.getCountUsersBySession(sessionId, searchString);
    }

    @Override
    public List<AssessmentUserDTO> getPagedUsersBySessionAndQuestion(Long sessionId, Long questionUid, int page,
	    int size, String sortBy, String sortOrder, String searchString) {
	return assessmentUserDao.getPagedUsersBySessionAndQuestion(sessionId, questionUid, page, size, sortBy,
		sortOrder, searchString);
    }

    @Override
    public Assessment getAssessmentByContentId(Long contentId) {
	Assessment rs = assessmentDao.getByContentId(contentId);
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
    public AssessmentUser getUserByIDAndContent(Long userId, Long contentId) {
	return assessmentUserDao.getUserByUserIDAndContentID(userId, contentId);
    }

    @Override
    public AssessmentUser getUserByIDAndSession(Long userId, Long sessionId) {
	return assessmentUserDao.getUserByUserIDAndSessionID(userId, sessionId);
    }

    @Override
    public void saveOrUpdateAssessment(Assessment assessment) {
	assessmentDao.saveObject(assessment);
    }

    @Override
    public void releaseFromCache(Object object) {
	assessmentQuestionDao.evict(object);
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
    public void setAttemptStarted(Assessment assessment, AssessmentUser assessmentUser, Long toolSessionId) {
	Set<AssessmentQuestion> questions = assessment.getQuestions();

	AssessmentResult lastResult = getLastAssessmentResult(assessment.getUid(), assessmentUser.getUserId());
	if (lastResult != null) {

	    // don't instantiate new attempt if the previous one wasn't finished and thus continue working with it
	    if (lastResult.getFinishDate() == null) {

		//check all required questionResults exist, it can be missing in case of random question - create new one then
		Set<AssessmentQuestionResult> questionResults = lastResult.getQuestionResults();
		Set<AssessmentQuestionResult> updatedQuestionResults = new TreeSet(
			new AssessmentQuestionResultComparator());
		for (AssessmentQuestion question : questions) {

		    // get questionResult from DB instance of AssessmentResult
		    AssessmentQuestionResult questionResult = null;
		    for (AssessmentQuestionResult questionResultIter : questionResults) {
			if (question.getUid().equals(questionResultIter.getAssessmentQuestion().getUid())) {
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
		lastResult.setLatest(false);
		assessmentResultDao.saveObject(lastResult);
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
     *
     * @param question
     *
     * @param questionResults
     *
     * @return
     */
    private AssessmentQuestionResult createQuestionResultObject(AssessmentQuestion question) {
	AssessmentQuestionResult questionResult = new AssessmentQuestionResult();
	questionResult.setAssessmentQuestion(question);

	// create optionAnswer for each option
	Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
	for (AssessmentQuestionOption option : question.getOptions()) {
	    AssessmentOptionAnswer optionAnswer = new AssessmentOptionAnswer();
	    optionAnswer.setOptionUid(option.getUid());
	    optionAnswers.add(optionAnswer);
	}

	return questionResult;
    }

    @Override
    public boolean storeUserAnswers(Assessment assessment, Long userId, List<Set<QuestionDTO>> pagedQuestions,
	    Long singleMarkHedgingQuestionUid, boolean isAutosave)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	int maximumGrade = 0;
	float grade = 0;

	Set<AssessmentQuestion> questions = assessment.getQuestions();
	AssessmentResult result = assessmentResultDao.getLastAssessmentResult(assessment.getUid(), userId);

	// prohibit users from submitting (or autosubmitting) answers after result is finished but Resubmit button is
	// not pressed (e.g. using 2 browsers)
	if (result.getFinishDate() != null) {
	    return false;
	}

	// store all answers (in all pages)
	for (Set<QuestionDTO> questionsForOnePage : pagedQuestions) {
	    for (QuestionDTO questionDto : questionsForOnePage) {

		// in case single MarkHedging question needs to be stored -- search for that question
		if ((singleMarkHedgingQuestionUid != null)
			&& !questionDto.getUid().equals(singleMarkHedgingQuestionUid)) {
		    continue;
		}

		// In case if assessment was modified in monitor after result has been started check question still exists in DB as
		// it could be deleted
		if (assessment.isContentModifiedInMonitor(result.getStartDate())) {

		    Set<QuestionReference> references = assessment.getQuestionReferences();

		    boolean isQuestionExists = false;
		    for (QuestionReference reference : references) {
			if (!reference.isRandomQuestion()
				&& reference.getQuestion().getUid().equals(questionDto.getUid())) {
			    isQuestionExists = true;
			    break;
			}
			if (reference.isRandomQuestion()) {
			    for (AssessmentQuestion questionDb : questions) {
				if (questionDb.getUid().equals(questionDto.getUid())) {
				    isQuestionExists = true;
				    break;
				}
			    }
			}
		    }
		    if (!isQuestionExists) {
			continue;
		    }
		}

		float userQeustionGrade = storeUserAnswer(result, questionDto, isAutosave);
		grade += userQeustionGrade;

		maximumGrade += questionDto.getGrade();
	    }
	}

	// store grades and finished date only on user hitting submit all answers button (and not submit mark hedging
	// question)
	boolean isStoreResult = !isAutosave && (singleMarkHedgingQuestionUid == null);
	if (isStoreResult) {
	    result.setMaximumGrade(maximumGrade);
	    result.setGrade(grade);
	    result.setFinishDate(new Timestamp(new Date().getTime()));
	    assessmentResultDao.update(result);
	}

	return true;
    }

    /**
     * Stores given AssessmentQuestion's answer.
     *
     * @param isAutosave
     *            in case of autosave there is no need to calculate marks
     * @return grade that user scored by answering that question
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private float storeUserAnswer(AssessmentResult assessmentResult, QuestionDTO questionDto, boolean isAutosave)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	Assessment assessment = assessmentResult.getAssessment();

	AssessmentQuestionResult questionResult = null;
	// get questionResult from DB instance of AssessmentResult
	for (AssessmentQuestionResult questionResultIter : assessmentResult.getQuestionResults()) {
	    if (questionDto.getUid().equals(questionResultIter.getAssessmentQuestion().getUid())) {
		questionResult = questionResultIter;
	    }
	}

	//if teacher edited content in monitor (modified question) it led to removal if autosaved questionResult
	if (assessment.isContentModifiedInMonitor(assessmentResult.getStartDate()) && questionResult == null) {
	    //update questionDto
	    AssessmentQuestion modifiedQuestion = assessmentQuestionDao.getByUid(questionDto.getUid());
	    QuestionDTO updatedQuestionDto = modifiedQuestion.getQuestionDTO();
	    PropertyUtils.copyProperties(questionDto, updatedQuestionDto);
	    return 0;
	}

	// store question answer values
	questionResult.setAnswerBoolean(questionDto.getAnswerBoolean());
	questionResult.setAnswerFloat(questionDto.getAnswerFloat());
	questionResult.setAnswerString(questionDto.getAnswerString());

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
	    if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		optionAnswer.setAnswerInt(j++);
	    }
	}

	float mark = 0;
	float maxMark = questionDto.getGrade();
	if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
	    boolean isMarkNullified = false;
	    float totalGrade = 0;
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getAnswerBoolean()) {
		    totalGrade += optionDto.getGrade();
		    mark += optionDto.getGrade() * maxMark;

		    // if option of "incorrect answer nullifies mark" is ON check if selected answer has a zero grade
		    // and if so nullify question's mark
		    if (questionDto.isIncorrectAnswerNullifiesMark() && (optionDto.getGrade() == 0)) {
			isMarkNullified = true;
		    }
		}
	    }
	    // set answerTotalGrade to let jsp know whether the question was answered correctly/partly/incorrectly even if mark=0
	    questionDto.setAnswerTotalGrade(totalGrade);

	    if (isMarkNullified) {
		mark = 0;
	    }

	} else if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
	    float maxMarkForCorrectAnswer = maxMark / questionDto.getOptionDtos().size();
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getAnswerInt() == optionDto.getUid()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }

	} else if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER) {
	    //clear previous answer
	    questionResult.setSubmittedOptionUid(null);
	    
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {

		//prepare regex which takes into account only * special character
		String regexWithOnlyAsteriskSymbolActive = "\\Q";
		String optionString = optionDto.getOptionString().trim();
		for (int i = 0; i < optionString.length(); i++) {
		    //everything in between \\Q and \\E are taken literally no matter which characters it contains
		    if (optionString.charAt(i) == '*') {
			regexWithOnlyAsteriskSymbolActive += "\\E.*\\Q";
		    } else {
			regexWithOnlyAsteriskSymbolActive += optionString.charAt(i);
		    }
		}
		regexWithOnlyAsteriskSymbolActive += "\\E";

		//check whether answer matches regex
		Pattern pattern;
		if (questionDto.isCaseSensitive()) {
		    pattern = Pattern.compile(regexWithOnlyAsteriskSymbolActive);
		} else {
		    pattern = Pattern.compile(regexWithOnlyAsteriskSymbolActive,
			    java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.UNICODE_CASE);
		}
		boolean isAnswerMatchedCurrentOption = (questionDto.getAnswerString() != null)
			? pattern.matcher(questionDto.getAnswerString().trim()).matches()
			: false;

		if (isAnswerMatchedCurrentOption) {
		    mark = optionDto.getGrade() * maxMark;
		    questionResult.setSubmittedOptionUid(optionDto.getUid());
		    break;
		}
	    }

	} else if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    String answerString = questionDto.getAnswerString();
	    if (answerString != null) {
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    boolean isAnswerMatchedCurrentOption = false;
		    try {
			float answerFloat = Float.valueOf(questionDto.getAnswerString());
			isAnswerMatchedCurrentOption = ((answerFloat >= (optionDto.getOptionFloat()
				- optionDto.getAcceptedError()))
				&& (answerFloat <= (optionDto.getOptionFloat() + optionDto.getAcceptedError())));
		    } catch (Exception e) {
		    }

		    if (!isAnswerMatchedCurrentOption) {
			for (AssessmentUnit unit : questionDto.getUnits()) {
			    String regex = ".*" + unit.getUnit() + "$";
			    Pattern pattern = Pattern.compile(regex,
				    java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.UNICODE_CASE);
			    if (pattern.matcher(answerString).matches()) {
				String answerFloatStr = answerString.substring(0,
					answerString.length() - unit.getUnit().length());
				try {
				    float answerFloat = Float.valueOf(answerFloatStr);
				    answerFloat = answerFloat / unit.getMultiplier();
				    isAnswerMatchedCurrentOption = ((answerFloat >= (optionDto.getOptionFloat()
					    - optionDto.getAcceptedError()))
					    && (answerFloat <= (optionDto.getOptionFloat()
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
			mark = optionDto.getGrade() * maxMark;
			questionResult.setSubmittedOptionUid(optionDto.getUid());
			break;
		    }
		}
	    }

	} else if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
	    if ((questionDto.getAnswerBoolean() == questionDto.getCorrectAnswer())
		    && (questionDto.getAnswerString() != null)) {
		mark = maxMark;
	    }

	} else if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    float maxMarkForCorrectAnswer = maxMark / questionDto.getOptionDtos().size();
	    TreeSet<OptionDTO> correctOptionSet = new TreeSet<>(new SequencableComparator());
	    correctOptionSet.addAll(questionDto.getOptionDtos());
	    ArrayList<OptionDTO> correctOptionList = new ArrayList<>(correctOptionSet);
	    int i = 0;
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.getUid() == correctOptionList.get(i++).getUid()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }

	} else if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {
	    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		if (optionDto.isCorrect()) {
		    //if hedgingMark is a default '-1', change it to '0'
		    int hedgingMark = optionDto.getAnswerInt() == -1 ? 0 : optionDto.getAnswerInt();
		    mark += hedgingMark;
		    break;
		}
	    }
	}

	// we start calculating and storing marks only in case it's not an autosave request
	if (!isAutosave) {

	    questionResult.setFinishDate(new Date());

	    if (mark > maxMark) {
		mark = maxMark;

		// in case options have negative grades (<0), their total mark can't be less than -maxMark
	    } else if (mark < -maxMark) {
		mark = -maxMark;
	    }

	    // calculate penalty
	    if (mark > 0) {
		// calculate number of wrong answers
		Long assessmentUid = assessmentResult.getAssessment().getUid();
		Long userId = assessmentResult.getUser().getUserId();
		int numberWrongAnswers = assessmentQuestionResultDao.getNumberWrongAnswersDoneBefore(assessmentUid,
			userId, questionDto.getUid());

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
	    // for displaying purposes in case of submitSingleMarkHedgingQuestion() Ajax call
	    questionDto.setMark(mark);
	}

	return mark;
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
    public AssessmentResult getLastFinishedAssessmentResultNotFromChache(Long assessmentUid, Long userId) {
	AssessmentResult result = getLastFinishedAssessmentResult(assessmentUid, userId);
	assessmentQuestionDao.evict(result);
	return getLastFinishedAssessmentResult(assessmentUid, userId);
    }

    @Override
    public int getAssessmentResultCount(Long assessmentUid, Long userId) {
	return assessmentResultDao.getAssessmentResultCount(assessmentUid, userId);
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
    public Float getQuestionResultMark(Long assessmentUid, Long userId, int questionSequenceId) {
	return assessmentQuestionResultDao.getQuestionResultMark(assessmentUid, userId, questionSequenceId);
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
	AssessmentUser user = assessmentUserDao.getUserByUserIDAndSessionID(userId, toolSessionId);
	user.setSessionFinished(true);
	assessmentUserDao.saveObject(user);

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
	    SortedSet<AssessmentQuestionResult> questionResultsToDisplay = new TreeSet<>(
		    new AssessmentQuestionResultComparator());
	    //in case there is at least one random question - we need to show all questions
	    if (assessment.hasRandomQuestion()) {
		questionResultsToDisplay.addAll(questionResults);

		//otherwise show only questions from the question list
	    } else {
		for (QuestionReference reference : questionReferences) {
		    for (AssessmentQuestionResult questionResult : questionResults) {
			if (reference.getQuestion().getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
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
		for (QuestionReference reference : (Set<QuestionReference>) assessment.getQuestionReferences()) {
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
			if (question.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {

			    // for displaying purposes only (no saving occurrs)
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
	QuestionSummary questionSummary = new QuestionSummary();
	AssessmentQuestion question = assessmentQuestionDao.getByUid(questionUid);
	questionSummary.setQuestion(question);

	return questionSummary;
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

	for (AssessmentQuestion question : (Set<AssessmentQuestion>) assessment.getQuestions()) {
	    Long questionUid = question.getUid();
	    QuestionSummary questionSummary = new QuestionSummary();
	    questionSummary.setQuestion(question);

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

	    AssessmentEscapeUtils.escapeQuotes(questionSummary);

	    questionSummaries.put(questionUid, questionSummary);
	}

	return questionSummaries;
    }

    @Override
    public LinkedHashMap<String, ExcelCell[][]> exportSummary(Assessment assessment, List<SessionDTO> sessionDtos,
	    boolean showUserNames) {
	LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<>();
	final ExcelCell[] EMPTY_ROW = new ExcelCell[0];

	// -------------- First tab: Summary ----------------------------------------------------
	if (showUserNames) {
	    ArrayList<ExcelCell[]> summaryTab = new ArrayList<>();

	    if (sessionDtos != null) {
		for (SessionDTO sessionDTO : sessionDtos) {
		    Long sessionId = sessionDTO.getSessionId();

		    summaryTab.add(EMPTY_ROW);

		    ExcelCell[] sessionTitle = new ExcelCell[1];
		    sessionTitle[0] = new ExcelCell(sessionDTO.getSessionName(), true);
		    summaryTab.add(sessionTitle);

		    List<AssessmentUserDTO> userDtos = new ArrayList<>();
		    // in case of UseSelectLeaderToolOuput - display only one user
		    if (assessment.isUseSelectLeaderToolOuput()) {

			AssessmentSession session = getAssessmentSessionBySessionId(sessionId);
			AssessmentUser groupLeader = session.getGroupLeader();

			if (groupLeader != null) {

			    float assessmentResult = getLastTotalScoreByUser(assessment.getUid(),
				    groupLeader.getUserId());

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

		    ArrayList<ExcelCell[]> summaryTabLearnerList = new ArrayList<>();

		    ExcelCell[] summaryRowTitle = new ExcelCell[3];
		    summaryRowTitle[0] = new ExcelCell(getMessage("label.export.user.id"), true,
			    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		    summaryRowTitle[1] = new ExcelCell(getMessage("label.monitoring.summary.user.name"), true,
			    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		    summaryRowTitle[2] = new ExcelCell(getMessage("label.monitoring.summary.total"), true,
			    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		    summaryTabLearnerList.add(summaryRowTitle);

		    float minGrade = -9999999;
		    float maxGrade = 0;

		    for (AssessmentUserDTO userDto : userDtos) {
			float grade = userDto.getGrade();

			ExcelCell[] userResultRow = new ExcelCell[3];
			userResultRow[0] = new ExcelCell(userDto.getLogin(), false);
			userResultRow[1] = new ExcelCell(userDto.getFirstName() + " " + userDto.getLastName(), false);
			userResultRow[2] = new ExcelCell(grade, false);
			summaryTabLearnerList.add(userResultRow);

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
		    summaryTab.add(EMPTY_ROW);
		    ExcelCell[] minMaxRow = new ExcelCell[2];
		    minMaxRow[0] = new ExcelCell(getMessage("label.number.learners"), true);
		    minMaxRow[1] = new ExcelCell(totalNumEntries, false);
		    summaryTab.add(minMaxRow);
		    minMaxRow = new ExcelCell[2];
		    minMaxRow[0] = new ExcelCell(getMessage("label.lowest.mark"), true);
		    minMaxRow[1] = new ExcelCell((double) minGrade, false);
		    summaryTab.add(minMaxRow);
		    minMaxRow = new ExcelCell[2];
		    minMaxRow[0] = new ExcelCell(getMessage("label.highest.mark"), true);
		    minMaxRow[1] = new ExcelCell((double) maxGrade, false);
		    summaryTab.add(minMaxRow);

		    summaryTab.add(EMPTY_ROW);
		    ExcelCell[] binSummaryRow = new ExcelCell[3];
		    binSummaryRow[0] = new ExcelCell(getMessage("label.authoring.basic.list.header.mark"), true,
			    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		    binSummaryRow[1] = new ExcelCell(getMessage("label.number.learners"), true,
			    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		    binSummaryRow[2] = new ExcelCell(getMessage("label.percentage"), true,
			    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
		    summaryTab.add(binSummaryRow);
		    float totalNumEntriesAsFloat = totalNumEntries;
		    for (Map.Entry<String, Integer> entry : markSummary.entrySet()) {
			binSummaryRow = new ExcelCell[3];
			binSummaryRow[0] = new ExcelCell(entry.getKey(), false);
			binSummaryRow[1] = new ExcelCell(entry.getValue(), false);
			binSummaryRow[2] = new ExcelCell(Math.round(entry.getValue() / totalNumEntriesAsFloat * 100),
				false);
			summaryTab.add(binSummaryRow);
		    }
		    summaryTab.add(EMPTY_ROW);

		    summaryTab.add(EMPTY_ROW);
		    summaryTab.addAll(summaryTabLearnerList);
		    summaryTab.add(EMPTY_ROW);
		}
	    }

	    dataToExport.put(getMessage("label.export.summary"), summaryTab.toArray(new ExcelCell[][] {}));
	}

	// ------------------------------------------------------------------
	// -------------- Second tab: Question Summary ----------------------

	ArrayList<ExcelCell[]> questionSummaryTab = new ArrayList<>();

	// Create the question summary
	ExcelCell[] summaryTitle = new ExcelCell[1];
	summaryTitle[0] = new ExcelCell(getMessage("label.export.question.summary"), true);
	questionSummaryTab.add(summaryTitle);
	questionSummaryTab.add(EMPTY_ROW);

	Map<Long, QuestionSummary> questionSummaries = getQuestionSummaryForExport(assessment);

	if (assessment.getQuestions() != null) {
	    Set<AssessmentQuestion> questions = assessment.getQuestions();

	    int count = 0;
	    // question row title
	    ExcelCell[] questionTitleRow = showUserNames ? new ExcelCell[10] : new ExcelCell[9];
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.monitoring.question.summary.question"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.authoring.basic.list.header.type"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.authoring.basic.penalty.factor"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.monitoring.question.summary.default.mark"),
		    true, ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.export.user.id"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    if (showUserNames) {
		questionTitleRow[count++] = new ExcelCell(getMessage("label.monitoring.user.summary.user.name"), true,
			ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    }
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.export.date.attempted"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.authoring.basic.option.answer"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.export.time.taken"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	    questionTitleRow[count++] = new ExcelCell(getMessage("label.export.mark"), true,
		    ExcelCell.BORDER_STYLE_BOTTOM_THIN);

	    int questionNumber = 1;

	    for (AssessmentQuestion question : questions) {
		int colsNum = showUserNames ? 10 : 9;

		ExcelCell[] questionTitle = new ExcelCell[1];
		questionTitle[0] = new ExcelCell(
			getMessage("label.monitoring.question.summary.question") + " " + questionNumber++, true);
		questionSummaryTab.add(questionTitle);

		// set up the summary table data for the top of the question area.
		boolean doSummaryTable = question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE
			|| question.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER
			|| question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL
			|| question.getType() == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE;
		// For MC, Numeric & Short Answer Key is optionUid, Value is number of answers
		// For True/False Key 0 is false and Key 1 is true
		Map<Long, Integer> summaryOfAnswers = new HashMap<>();
		Integer summaryNACount = 0;
		Long trueKey = 1L;
		Long falseKey = 0L;
		if (doSummaryTable) {
		    questionSummaryTab.add(startSummaryTable(question, summaryOfAnswers, trueKey, falseKey));
		}

		ArrayList<ExcelCell[]> questionSummaryTabTemp = new ArrayList<>();

		//add question title row
		if (question.getType() == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {
		    count = 0;
		    Set<AssessmentQuestionOption> options = question.getOptions();
		    colsNum += options.size() - 1;
		    // question row title
		    ExcelCell[] hedgeQuestionTitleRow = new ExcelCell[colsNum];
		    hedgeQuestionTitleRow[count++] = new ExcelCell(
			    getMessage("label.monitoring.question.summary.question"), true);
		    hedgeQuestionTitleRow[count++] = new ExcelCell(getMessage("label.authoring.basic.list.header.type"),
			    true);
		    hedgeQuestionTitleRow[count++] = new ExcelCell(getMessage("label.authoring.basic.penalty.factor"),
			    true);
		    hedgeQuestionTitleRow[count++] = new ExcelCell(
			    getMessage("label.monitoring.question.summary.default.mark"), true);
		    hedgeQuestionTitleRow[count++] = new ExcelCell(getMessage("label.export.user.id"), true);
		    if (showUserNames) {
			hedgeQuestionTitleRow[count++] = new ExcelCell(
				getMessage("label.monitoring.user.summary.user.name"), true);
		    }
		    hedgeQuestionTitleRow[count++] = new ExcelCell(getMessage("label.export.date.attempted"), true);
		    for (AssessmentQuestionOption option : options) {
			hedgeQuestionTitleRow[count++] = new ExcelCell(
				option.getOptionString().replaceAll("\\<.*?\\>", ""), true);
			if (option.isCorrect()) {
			    hedgeQuestionTitleRow[count - 1].setColor(IndexedColors.GREEN);
			}
		    }
		    hedgeQuestionTitleRow[count++] = new ExcelCell(getMessage("label.export.time.taken"), true);
		    hedgeQuestionTitleRow[count++] = new ExcelCell(getMessage("label.export.mark"), true);
		    questionSummaryTabTemp.add(hedgeQuestionTitleRow);
		} else {
		    questionSummaryTabTemp.add(questionTitleRow);
		}

		QuestionSummary questionSummary = questionSummaries.get(question.getUid());

		List<List<AssessmentQuestionResult>> allResultsForQuestion = questionSummary
			.getQuestionResultsPerSession();

		int markCount = 0;
		Float markTotal = new Float(0.0);
		int timeTakenCount = 0;
		int timeTakenTotal = 0;
		for (List<AssessmentQuestionResult> resultList : allResultsForQuestion) {
		    for (AssessmentQuestionResult questionResult : resultList) {

			ExcelCell[] userResultRow = new ExcelCell[colsNum];
			count = 0;
			userResultRow[count++] = new ExcelCell(questionResult.getAssessmentQuestion().getTitle(),
				false);
			userResultRow[count++] = new ExcelCell(
				getQuestionTypeLanguageLabel(questionResult.getAssessmentQuestion().getType()), false);
			userResultRow[count++] = new ExcelCell(
				new Float(questionResult.getAssessmentQuestion().getPenaltyFactor()), false);
			Float maxMark = (questionResult.getMaxMark() == null) ? 0
				: new Float(questionResult.getMaxMark());
			userResultRow[count++] = new ExcelCell(maxMark, false);
			if (showUserNames) {
			    userResultRow[count++] = new ExcelCell(questionResult.getUser().getLoginName(), false);
			    userResultRow[count++] = new ExcelCell(questionResult.getUser().getFullName(), false);
			} else {
			    userResultRow[count++] = new ExcelCell(questionResult.getUser().getUserId(), false);
			}
			userResultRow[count++] = new ExcelCell(questionResult.getFinishDate(), false);
			//answer
			if (question.getType() == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {

			    Set<AssessmentOptionAnswer> optionAnswers = questionResult.getOptionAnswers();
			    for (AssessmentQuestionOption option : question.getOptions()) {
				for (AssessmentOptionAnswer optionAnswer : optionAnswers) {
				    if (option.getUid().equals(optionAnswer.getOptionUid())) {
					userResultRow[count++] = new ExcelCell(optionAnswer.getAnswerInt(), false);
					break;
				    }
				}
			    }
			} else {
			    userResultRow[count++] = new ExcelCell(
				    AssessmentEscapeUtils.printResponsesForExcelExport(questionResult), false);

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
				userResultRow[count++] = new ExcelCell(seconds, false);
				timeTakenCount++;
				timeTakenTotal += seconds;
			    }
			}
			//mark
			userResultRow[count++] = new ExcelCell(questionResult.getMark(), false);
			questionSummaryTabTemp.add(userResultRow);

			//calculating markCount & markTotal
			if (questionResult.getMark() != null) {
			    markCount++;
			    markTotal += questionResult.getMark();
			}
		    }
		}

		if (doSummaryTable) {
		    questionSummaryTab
			    .add(outputSummaryTable(question, summaryOfAnswers, summaryNACount, trueKey, falseKey));
		    questionSummaryTab.add(EMPTY_ROW);
		}

		// Calculating the averages
		ExcelCell[] averageRow;

		if (showUserNames) {
		    averageRow = new ExcelCell[10];

		    averageRow[7] = new ExcelCell(getMessage("label.export.average"), true);

		    if (timeTakenTotal > 0) {
			averageRow[8] = new ExcelCell(new Long(timeTakenTotal / timeTakenCount), false);
		    }
		    if (markTotal > 0) {
			Float averageMark = new Float(markTotal / markCount);
			averageRow[9] = new ExcelCell(averageMark, false);
		    } else {
			averageRow[9] = new ExcelCell(new Float(0.0), false);
		    }
		} else {
		    averageRow = new ExcelCell[9];
		    averageRow[6] = new ExcelCell(getMessage("label.	.average"), true);

		    if (timeTakenTotal > 0) {
			averageRow[7] = new ExcelCell(new Long(timeTakenTotal / timeTakenCount), false);
		    }
		    if (markTotal > 0) {
			Float averageMark = new Float(markTotal / markCount);
			averageRow[8] = new ExcelCell(averageMark, false);
		    } else {
			averageRow[8] = new ExcelCell(new Float(0.0), false);
		    }
		}

		questionSummaryTab.addAll(questionSummaryTabTemp);
		questionSummaryTab.add(averageRow);
		questionSummaryTab.add(EMPTY_ROW);

	    }

	}
	dataToExport.put(getMessage("lable.export.summary.by.question"),
		questionSummaryTab.toArray(new ExcelCell[][] {}));

	// ------------------------------------------------------------------
	// -------------- Third tab: User Summary ---------------------------

	ArrayList<ExcelCell[]> userSummaryTab = new ArrayList<>();

	// Create the question summary
	ExcelCell[] userSummaryTitle = new ExcelCell[1];
	userSummaryTitle[0] = new ExcelCell(getMessage("label.export.user.summary"), true);
	userSummaryTab.add(userSummaryTitle);

	ExcelCell[] summaryRowTitle = new ExcelCell[5];
	summaryRowTitle[0] = new ExcelCell(getMessage("label.monitoring.question.summary.question"), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	summaryRowTitle[1] = new ExcelCell(getMessage("label.authoring.basic.list.header.type"), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	summaryRowTitle[2] = new ExcelCell(getMessage("label.authoring.basic.penalty.factor"), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	summaryRowTitle[3] = new ExcelCell(getMessage("label.monitoring.question.summary.default.mark"), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	summaryRowTitle[4] = new ExcelCell(getMessage("label.monitoring.question.summary.average.mark"), true,
		ExcelCell.BORDER_STYLE_BOTTOM_THIN);
	userSummaryTab.add(summaryRowTitle);
	Float totalGradesPossible = new Float(0);
	Float totalAverage = new Float(0);
	if (assessment.getQuestionReferences() != null) {
	    Set<QuestionReference> questionReferences = new TreeSet<>(new SequencableComparator());
	    questionReferences.addAll(assessment.getQuestionReferences());

	    int randomQuestionsCount = 1;
	    for (QuestionReference questionReference : questionReferences) {

		String title;
		String questionType;
		Float penaltyFactor;
		Float averageMark = null;
		if (questionReference.isRandomQuestion()) {

		    title = getMessage("label.authoring.basic.type.random.question") + randomQuestionsCount++;
		    questionType = getMessage("label.authoring.basic.type.random.question");
		    penaltyFactor = null;
		    averageMark = null;
		} else {

		    AssessmentQuestion question = questionReference.getQuestion();
		    title = question.getTitle();
		    questionType = getQuestionTypeLanguageLabel(question.getType());
		    penaltyFactor = question.getPenaltyFactor();

		    QuestionSummary questionSummary = questionSummaries.get(question.getUid());
		    if (questionSummary != null) {
			averageMark = questionSummary.getAverageMark();
			totalAverage += questionSummary.getAverageMark();
		    }
		}

		int maxGrade = questionReference.getDefaultGrade();
		totalGradesPossible += maxGrade;

		ExcelCell[] questCell = new ExcelCell[5];
		questCell[0] = new ExcelCell(title, false);
		questCell[1] = new ExcelCell(questionType, false);
		questCell[2] = new ExcelCell(penaltyFactor, false);
		questCell[3] = new ExcelCell(maxGrade, false);
		questCell[4] = new ExcelCell(averageMark, false);

		userSummaryTab.add(questCell);
	    }

	    if (totalGradesPossible.floatValue() > 0) {
		ExcelCell[] totalCell = new ExcelCell[5];
		totalCell[2] = new ExcelCell(getMessage("label.monitoring.summary.total"), true);
		totalCell[3] = new ExcelCell(totalGradesPossible, false);
		totalCell[4] = new ExcelCell(totalAverage, false);
		userSummaryTab.add(totalCell);
	    }
	    userSummaryTab.add(EMPTY_ROW);
	}

	if (sessionDtos != null) {
	    List<AssessmentResult> assessmentResults = assessmentResultDao
		    .getLastFinishedAssessmentResults(assessment.getContentId());
	    Map<Long, AssessmentResult> userUidToResultMap = new HashMap<>();
	    for (AssessmentResult assessmentResult : assessmentResults) {
		userUidToResultMap.put(assessmentResult.getUser().getUid(), assessmentResult);
	    }

	    for (SessionDTO sessionDTO : sessionDtos) {

		userSummaryTab.add(EMPTY_ROW);

		ExcelCell[] sessionTitle = new ExcelCell[1];
		sessionTitle[0] = new ExcelCell(sessionDTO.getSessionName(), true);
		userSummaryTab.add(sessionTitle);

		AssessmentSession assessmentSession = getAssessmentSessionBySessionId(sessionDTO.getSessionId());

		Set<AssessmentUser> assessmentUsers = assessmentSession.getAssessmentUsers();

		if (assessmentUsers != null) {

		    for (AssessmentUser assessmentUser : assessmentUsers) {

			if (showUserNames) {
			    ExcelCell[] userTitleRow = new ExcelCell[6];
			    userTitleRow[0] = new ExcelCell(getMessage("label.export.user.id"), true);
			    userTitleRow[1] = new ExcelCell(getMessage("label.monitoring.user.summary.user.name"),
				    true);
			    userTitleRow[2] = new ExcelCell(getMessage("label.export.date.attempted"), true);
			    userTitleRow[3] = new ExcelCell(getMessage("label.monitoring.question.summary.question"),
				    true);
			    userTitleRow[4] = new ExcelCell(getMessage("label.authoring.basic.option.answer"), true);
			    userTitleRow[5] = new ExcelCell(getMessage("label.export.mark"), true);
			    userSummaryTab.add(userTitleRow);
			} else {
			    ExcelCell[] userTitleRow = new ExcelCell[5];
			    userTitleRow[0] = new ExcelCell(getMessage("label.export.user.id"), true);
			    userTitleRow[1] = new ExcelCell(getMessage("label.export.date.attempted"), true);
			    userTitleRow[2] = new ExcelCell(getMessage("label.monitoring.question.summary.question"),
				    true);
			    userTitleRow[3] = new ExcelCell(getMessage("label.authoring.basic.option.answer"), true);
			    userTitleRow[4] = new ExcelCell(getMessage("label.export.mark"), true);
			    userSummaryTab.add(userTitleRow);
			}

			AssessmentResult assessmentResult = userUidToResultMap.get(assessmentUser.getUid());

			if (assessmentResult != null) {
			    Set<AssessmentQuestionResult> questionResults = assessmentResult.getQuestionResults();

			    if (questionResults != null) {

				for (AssessmentQuestionResult questionResult : questionResults) {

				    if (showUserNames) {
					ExcelCell[] userResultRow = new ExcelCell[6];
					userResultRow[0] = new ExcelCell(assessmentUser.getLoginName(), false);
					userResultRow[1] = new ExcelCell(assessmentUser.getFullName(), false);
					userResultRow[2] = new ExcelCell(assessmentResult.getStartDate(), false);
					userResultRow[3] = new ExcelCell(
						questionResult.getAssessmentQuestion().getTitle(), false);
					userResultRow[4] = new ExcelCell(
						AssessmentEscapeUtils.printResponsesForExcelExport(questionResult),
						false);
					userResultRow[5] = new ExcelCell(questionResult.getMark(), false);
					userSummaryTab.add(userResultRow);
				    } else {
					ExcelCell[] userResultRow = new ExcelCell[5];
					userResultRow[0] = new ExcelCell(assessmentUser.getUserId(), false);
					userResultRow[1] = new ExcelCell(assessmentResult.getStartDate(), false);
					userResultRow[2] = new ExcelCell(
						questionResult.getAssessmentQuestion().getTitle(), false);
					userResultRow[3] = new ExcelCell(
						AssessmentEscapeUtils.printResponsesForExcelExport(questionResult),
						false);
					userResultRow[4] = new ExcelCell(questionResult.getMark(), false);
					userSummaryTab.add(userResultRow);
				    }
				}
			    }

			    ExcelCell[] userTotalRow;
			    if (showUserNames) {
				userTotalRow = new ExcelCell[6];
				userTotalRow[4] = new ExcelCell(getMessage("label.monitoring.summary.total"), true);
				userTotalRow[5] = new ExcelCell(assessmentResult.getGrade(), false);
			    } else {
				userTotalRow = new ExcelCell[5];
				userTotalRow[3] = new ExcelCell(getMessage("label.monitoring.summary.total"), true);
				userTotalRow[4] = new ExcelCell(assessmentResult.getGrade(), false);
			    }

			    userSummaryTab.add(userTotalRow);
			    userSummaryTab.add(EMPTY_ROW);
			}
		    }
		}
	    }
	}
	dataToExport.put(getMessage("label.export.summary.by.user"), userSummaryTab.toArray(new ExcelCell[][] {}));

	return dataToExport;
    }

    private ExcelCell[] startSummaryTable(AssessmentQuestion question, Map<Long, Integer> summaryOfAnswers,
	    Long trueKey, Long falseKey) {
	ExcelCell[] summaryTable;
	int i = 0;
	if (question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE
		|| question.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER
		|| question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    summaryTable = new ExcelCell[question.getOptions().size() + 1];
	    for (AssessmentQuestionOption option : question.getOptions()) {
		summaryOfAnswers.put(option.getUid(), 0);
		StringBuilder bldr = new StringBuilder(getMessage("label.authoring.basic.option.answer")).append(" ")
			.append(i + 1).append(" - ");
		if (question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
		    bldr.append(option.getOptionFloat()).append(" +- ").append(option.getAcceptedError());
		} else {
		    bldr.append(option.getOptionString().replaceAll("\\<.*?\\>", ""));
		}
		summaryTable[i] = new ExcelCell(bldr.toString(), false);
		i++;
	    }
	    if (question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
		summaryTable[i++] = new ExcelCell(getMessage("label.not.answered"), false);
	    } else {
		summaryTable[i++] = new ExcelCell(getMessage("label.other"), false);
	    }
	} else {
	    summaryTable = new ExcelCell[3];
	    summaryTable[0] = new ExcelCell(getMessage("label.authoring.true.false.true"), false);
	    summaryTable[1] = new ExcelCell(getMessage("label.authoring.true.false.false"), false);
	    summaryTable[2] = new ExcelCell(getMessage("label.not.answered"), false);
	    summaryOfAnswers.put(trueKey, 0);
	    summaryOfAnswers.put(falseKey, 0);
	}
	return summaryTable;
    }

    private Integer updateSummaryCounts(AssessmentQuestion question, AssessmentQuestionResult questionResult,
	    Map<Long, Integer> summaryOfAnswers, Integer summaryNACount) {
	if (question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
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
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER
		|| question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    Long submittedUid = questionResult.getSubmittedOptionUid();
	    if (submittedUid != null) {
		Integer currentCount = summaryOfAnswers.get(submittedUid);
		if (currentCount == null) {
		    log.error(
			    "Assessment Export: Unable to count answer in summary, refers to an unexpected option. QuestionResult "
				    + questionResult.getUid() + " submittedOptionUid " + submittedUid + " question "
				    + question.getUid());
		} else {
		    summaryOfAnswers.put(submittedUid, currentCount + 1);
		}
	    } else {
		summaryNACount++;
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
	    if (questionResult.getAnswerString() == null) {
		summaryNACount++;
	    } else {
		long key = questionResult.getAnswerBoolean() ? 1 : 0;
		Integer currentCount = summaryOfAnswers.get(key);
		summaryOfAnswers.put(key, currentCount + 1);
	    }
	}
	return summaryNACount;
    }

    private String valueAsPercentage(Integer value, int total) {
	Double percentage = (double) value / total * 100;
	return NumberUtil.formatLocalisedNumber(percentage, (Locale) null, 2) + "%";
    }

    private ExcelCell[] outputSummaryTable(AssessmentQuestion question, Map<Long, Integer> summaryOfAnswers,
	    Integer summaryNACount, Long trueKey, Long falseKey) {
	ExcelCell[] summaryTable = new ExcelCell[summaryOfAnswers.size() + 1];
	int total = summaryNACount;
	for (int value : summaryOfAnswers.values()) {
	    total += value;
	}
	int i = 0;
	if (question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE
		|| question.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER
		|| question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    for (AssessmentQuestionOption option : question.getOptions()) {
		summaryTable[i] = new ExcelCell(valueAsPercentage(summaryOfAnswers.get(option.getUid()), total), false);
		if (option.getGrade() > 0) {
		    summaryTable[i].setColor(IndexedColors.GREEN);
		}
		i++;
	    }
	    summaryTable[i++] = new ExcelCell(valueAsPercentage(summaryNACount, total), false);
	} else {
	    summaryTable = new ExcelCell[3];
	    summaryTable[0] = new ExcelCell(valueAsPercentage(summaryOfAnswers.get(trueKey), total), false);
	    summaryTable[1] = new ExcelCell(valueAsPercentage(summaryOfAnswers.get(falseKey), total), false);
	    summaryTable[2] = new ExcelCell(valueAsPercentage(summaryNACount, total), false);
	    summaryTable[question.getCorrectAnswer() ? 0 : 1].setColor(IndexedColors.GREEN);
	}
	return summaryTable;
    }

    /**
     * Used only for excell export (for getUserSummaryData() method).
     */
    private String getQuestionTypeLanguageLabel(short type) {
	switch (type) {
	    case AssessmentConstants.QUESTION_TYPE_ESSAY:
		return "Essay";
	    case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
		return "Matching Pairs";
	    case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:
		return "Multiple Choice";
	    case AssessmentConstants.QUESTION_TYPE_NUMERICAL:
		return "Numerical";
	    case AssessmentConstants.QUESTION_TYPE_ORDERING:
		return "Ordering";
	    case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
		return "Short Answer";
	    case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
		return "True/False";
	    case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:
		return "Mark Hedging";
	    default:
		return null;
	}
    }

    @Override
    public void changeQuestionResultMark(Long questionResultUid, float newMark) {
	AssessmentQuestionResult questionAnswer = assessmentQuestionResultDao
		.getAssessmentQuestionResultByUid(questionResultUid);
	float oldMark = questionAnswer.getMark();
	AssessmentResult assessmentResult = questionAnswer.getAssessmentResult();
	float totalMark = (assessmentResult.getGrade() - oldMark) + newMark;

	Long toolSessionId = assessmentResult.getSessionId();
	Assessment assessment = assessmentResult.getAssessment();
	Long questionUid = questionAnswer.getAssessmentQuestion().getUid();

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
		AssessmentServiceImpl.log.warn("User with uid: " + user.getUid()
			+ " doesn't have any results despite the fact group leader has some.");
		continue;
	    }

	    Object[] lastAssessmentQuestionResultObj = questionResults.get(questionResults.size() - 1);
	    AssessmentQuestionResult lastAssessmentQuestionResult = (AssessmentQuestionResult) lastAssessmentQuestionResultObj[0];

	    lastAssessmentQuestionResult.setMark(newMark);
	    assessmentQuestionResultDao.saveObject(lastAssessmentQuestionResult);

	    AssessmentResult result = lastAssessmentQuestionResult.getAssessmentResult();
	    result.setGrade(totalMark);
	    assessmentResultDao.saveObject(result);

	    // propagade changes to Gradebook
	    gradebookService.updateActivityMark(new Double(totalMark), null, userId.intValue(), toolSessionId, false);

	    // records mark change with audit service
	    auditService.logMarkChange(AssessmentConstants.TOOL_SIGNATURE, userId, user.getLoginName(), "" + oldMark,
		    "" + totalMark);
	}

    }

    @Override
    public void recalculateUserAnswers(Assessment assessment, Set<AssessmentQuestion> oldQuestions,
	    Set<AssessmentQuestion> newQuestions, List<AssessmentQuestion> deletedQuestions,
	    Set<QuestionReference> oldReferences, Set<QuestionReference> newReferences,
	    List<QuestionReference> deletedReferences) {

	// create list of modified questions
	List<AssessmentQuestion> modifiedQuestions = new ArrayList<>();
	for (AssessmentQuestion oldQuestion : oldQuestions) {
	    for (AssessmentQuestion newQuestion : newQuestions) {
		if (oldQuestion.getUid().equals(newQuestion.getUid())) {

		    boolean isQuestionModified = false;

		    // title or question is different
		    if (!oldQuestion.getTitle().equals(newQuestion.getTitle())
			    || !oldQuestion.getQuestion().equals(newQuestion.getQuestion())
			    || (oldQuestion.getCorrectAnswer() != newQuestion.getCorrectAnswer())) {
			isQuestionModified = true;
		    }

		    // options are different
		    Set<AssessmentQuestionOption> oldOptions = oldQuestion.getOptions();
		    Set<AssessmentQuestionOption> newOptions = newQuestion.getOptions();
		    for (AssessmentQuestionOption oldOption : oldOptions) {
			for (AssessmentQuestionOption newOption : newOptions) {
			    if (oldOption.getUid().equals(newOption.getUid())) {

				if (((oldQuestion.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING)
					&& (oldOption.getSequenceId() != newOption.getSequenceId()))
					|| !StringUtils.equals(oldOption.getQuestion(), newOption.getQuestion())
					|| !StringUtils.equals(oldOption.getOptionString(), newOption.getOptionString())
					|| (oldOption.getOptionFloat() != newOption.getOptionFloat())
					|| (oldOption.getAcceptedError() != newOption.getAcceptedError())
					|| (oldOption.getGrade() != newOption.getGrade())) {
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

	// create list of modified references
	// modifiedReferences holds pairs newReference -> oldReference.getDefaultGrade()
	Map<QuestionReference, Integer> modifiedReferences = new HashMap<>();
	for (QuestionReference oldReference : oldReferences) {
	    for (QuestionReference newReference : newReferences) {
		if (oldReference.getUid().equals(newReference.getUid())
			&& (oldReference.getDefaultGrade() != newReference.getDefaultGrade())) {
		    modifiedReferences.put(newReference, oldReference.getDefaultGrade());
		}
	    }
	}

	// create list of added references
	List<QuestionReference> addedReferences = new ArrayList<>();
	for (QuestionReference newReference : newReferences) {
	    boolean isNewReferenceMetInOldReferences = false;

	    for (QuestionReference oldReference : oldReferences) {
		if (oldReference.getUid().equals(newReference.getUid())) {
		    isNewReferenceMetInOldReferences = true;
		}
	    }

	    // if the new reference was not met in old references then it's the newly added reference
	    if (!isNewReferenceMetInOldReferences) {
		addedReferences.add(newReference);
	    }
	}

	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(assessment.getContentId());
	for (AssessmentSession session : sessionList) {
	    Long toolSessionId = session.getSessionId();
	    Set<AssessmentUser> sessionUsers = session.getAssessmentUsers();

	    for (AssessmentUser user : sessionUsers) {

		// get all finished user results
		List<AssessmentResult> assessmentResults = assessmentResultDao.getAssessmentResults(assessment.getUid(),
			user.getUserId());
		AssessmentResult lastFinishedAssessmentResult = (assessmentResults.isEmpty()) ? null
			: assessmentResults.get(assessmentResults.size() - 1);

		//add autosave assessmentResult as well
		AssessmentResult lastAssessmentResult = getLastAssessmentResult(assessment.getUid(), user.getUserId());
		if (lastAssessmentResult != null && lastAssessmentResult.getFinishDate() == null) {
		    assessmentResults.add(lastAssessmentResult);
		}

		for (AssessmentResult assessmentResult : assessmentResults) {

		    float assessmentMark = assessmentResult.getGrade();
		    int assessmentMaxMark = assessmentResult.getMaximumGrade();

		    Set<AssessmentQuestionResult> questionAnswers = assessmentResult.getQuestionResults();
		    Iterator<AssessmentQuestionResult> iter = questionAnswers.iterator();
		    while (iter.hasNext()) {
			AssessmentQuestionResult questionAnswer = iter.next();
			AssessmentQuestion question = questionAnswer.getAssessmentQuestion();

			boolean isRemoveQuestionResult = false;

			// [+] if the question reference was removed
			for (QuestionReference deletedReference : deletedReferences) {
			    if (!deletedReference.isRandomQuestion()
				    && question.getUid().equals(deletedReference.getQuestion().getUid())) {
				isRemoveQuestionResult = true;
				assessmentMaxMark -= deletedReference.getDefaultGrade();
				break;
			    }
			}

			// [+] if the question reference mark is modified
			for (QuestionReference modifiedReference : modifiedReferences.keySet()) {
			    if (!modifiedReference.isRandomQuestion()
				    && question.getUid().equals(modifiedReference.getQuestion().getUid())) {
				int newReferenceGrade = modifiedReference.getDefaultGrade();
				int oldReferenceGrade = modifiedReferences.get(modifiedReference);

				// update question answer's mark
				Float oldQuestionAnswerMark = questionAnswer.getMark();
				float newQuestionAnswerMark = (oldQuestionAnswerMark * newReferenceGrade)
					/ oldReferenceGrade;
				questionAnswer.setMark(newQuestionAnswerMark);
				questionAnswer.setMaxMark((float) newReferenceGrade);
				assessmentQuestionResultDao.saveObject(questionAnswer);

				assessmentMark += newQuestionAnswerMark - oldQuestionAnswerMark;
				assessmentMaxMark += newReferenceGrade - oldReferenceGrade;
				break;
			    }

			}

			// [+] if the question is modified
			for (AssessmentQuestion modifiedQuestion : modifiedQuestions) {
			    if (question.getUid().equals(modifiedQuestion.getUid())) {
				isRemoveQuestionResult = true;
				break;
			    }
			}

			// [+] if the question was removed
			for (AssessmentQuestion deletedQuestion : deletedQuestions) {
			    if (question.getUid().equals(deletedQuestion.getUid())) {
				isRemoveQuestionResult = true;
				break;
			    }
			}

			if (isRemoveQuestionResult) {

			    assessmentMark -= questionAnswer.getMark();
			    iter.remove();
			    assessmentQuestionResultDao.removeObject(AssessmentQuestionResult.class,
				    questionAnswer.getUid());
			}

			// [+] doing nothing if the new question was added

		    }

		    // find all question answers from random question reference
		    ArrayList<AssessmentQuestionResult> nonRandomQuestionAnswers = new ArrayList<>();
		    for (AssessmentQuestionResult questionAnswer : questionAnswers) {
			for (QuestionReference reference : newReferences) {
			    if (!reference.isRandomQuestion() && questionAnswer.getAssessmentQuestion().getUid()
				    .equals(reference.getQuestion().getUid())) {
				nonRandomQuestionAnswers.add(questionAnswer);
			    }
			}
		    }
		    Collection<AssessmentQuestionResult> randomQuestionAnswers = CollectionUtils
			    .subtract(questionAnswers, nonRandomQuestionAnswers);

		    // [+] if the question reference was removed (in case of random question references)
		    for (QuestionReference deletedReference : deletedReferences) {

			// in case of random question reference - search for the answer with the same maxmark
			if (deletedReference.isRandomQuestion()) {

			    Iterator<AssessmentQuestionResult> iter2 = randomQuestionAnswers.iterator();
			    while (iter2.hasNext()) {
				AssessmentQuestionResult randomQuestionAnswer = iter2.next();
				if (randomQuestionAnswer.getMaxMark().intValue() == deletedReference
					.getDefaultGrade()) {

				    assessmentMark -= randomQuestionAnswer.getMark();
				    assessmentMaxMark -= deletedReference.getDefaultGrade();
				    iter2.remove();
				    questionAnswers.remove(randomQuestionAnswer);
				    assessmentQuestionResultDao.removeObject(AssessmentQuestionResult.class,
					    randomQuestionAnswer.getUid());
				    break;
				}
			    }
			}
		    }

		    // [+] if the question reference mark is modified (in case of random question references)
		    for (QuestionReference modifiedReference : modifiedReferences.keySet()) {

			// in case of random question reference - search for the answer with the same maxmark
			if (modifiedReference.isRandomQuestion()) {

			    for (AssessmentQuestionResult randomQuestionAnswer : randomQuestionAnswers) {
				int newReferenceGrade = modifiedReference.getDefaultGrade();
				int oldReferenceGrade = modifiedReferences.get(modifiedReference);

				if (randomQuestionAnswer.getMaxMark().intValue() == oldReferenceGrade) {

				    // update question answer's mark
				    Float oldQuestionAnswerMark = randomQuestionAnswer.getMark();
				    float newQuestionAnswerMark = (oldQuestionAnswerMark * newReferenceGrade)
					    / oldReferenceGrade;
				    randomQuestionAnswer.setMark(newQuestionAnswerMark);
				    randomQuestionAnswer.setMaxMark((float) newReferenceGrade);
				    assessmentQuestionResultDao.saveObject(randomQuestionAnswer);

				    nonRandomQuestionAnswers.add(randomQuestionAnswer);

				    assessmentMark += newQuestionAnswerMark - oldQuestionAnswerMark;
				    assessmentMaxMark += newReferenceGrade - oldReferenceGrade;
				    break;
				}
			    }
			}

		    }

		    // [+] if the new question reference was added
		    for (QuestionReference addedReference : addedReferences) {
			assessmentMaxMark += addedReference.getDefaultGrade();
		    }

		    // store new mark and maxMark if they were changed
		    if ((assessmentResult.getGrade() != assessmentMark)
			    || (assessmentResult.getMaximumGrade() != assessmentMaxMark)) {

			// marks can't be below zero
			assessmentMark = (assessmentMark < 0) ? 0 : assessmentMark;
			assessmentMaxMark = (assessmentMaxMark < 0) ? 0 : assessmentMaxMark;

			assessmentResult.setGrade(assessmentMark);
			assessmentResult.setMaximumGrade(assessmentMaxMark);
			assessmentResultDao.saveObject(assessmentResult);

			// if this is the last finished assessment result - propagade total mark to Gradebook
			if (lastFinishedAssessmentResult != null
				&& lastFinishedAssessmentResult.getUid().equals(assessmentResult.getUid())) {
			    gradebookService.updateActivityMark(new Double(assessmentMark), null,
				    user.getUserId().intValue(), toolSessionId, false);
			}
		    }

		}

	    }
	}

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
    public void auditLogStartEditingActivityInMonitor(long toolContentID) {
	toolService.auditLogStartEditingActivityInMonitor(toolContentID);
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

	    assessmentDao.saveObject(toolContentObj);
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
	assessmentDao.saveObject(toContent);
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
	    AssessmentServiceImpl.log
		    .warn("Can not remove the tool content as it does not exist, ID: " + toolContentId);
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
	if (AssessmentServiceImpl.log.isDebugEnabled()) {
	    AssessmentServiceImpl.log
		    .debug("Removing Assessment results for user ID " + userId + " and toolContentId " + toolContentId);
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
		gradebookService.updateActivityMark(null, null, userId, session.getSessionId(), false);

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
	assessmentSessionDao.deleteBySessionId(toolSessionId);
    }

    /**
     * Get the tool output for the given tool output names.
     *
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.util.List<String>, java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, Long toolSessionId, Long learnerId) {
	return assessmentOutputFactory.getToolOutput(names, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     *
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long,
     *      java.lang.Long)
     */
    @Override
    public ToolOutput getToolOutput(String name, Long toolSessionId, Long learnerId) {
	return assessmentOutputFactory.getToolOutput(name, this, toolSessionId, learnerId);
    }

    /**
     * Get the tool output for the given tool output name.
     *
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#getToolOutput(java.lang.String, java.lang.Long)
     */
    @Override
    public List<ToolOutput> getToolOutputs(String name, Long toolContentId) {
	return assessmentOutputFactory.getToolOutputs(name, this, toolContentId);
    }

    @Override
    public void forceCompleteUser(Long toolSessionId, User user) {
	Long userId = user.getUserId().longValue();

	AssessmentSession session = getAssessmentSessionBySessionId(toolSessionId);
	if ((session == null) || (session.getAssessment() == null)) {
	    return;
	}
	Assessment assessment = session.getAssessment();

	// copy answers only in case leader aware feature is ON
	if (assessment.isUseSelectLeaderToolOuput()) {

	    AssessmentUser assessmentUser = getUserByIDAndSession(userId, toolSessionId);
	    // create user if he hasn't accessed this activity yet
	    if (assessmentUser == null) {
		assessmentUser = new AssessmentUser(user.getUserDTO(), session);
		createUser(assessmentUser);
	    }

	    AssessmentUser groupLeader = session.getGroupLeader();

	    // check if leader has submitted answers
	    if ((groupLeader != null) && groupLeader.isSessionFinished()) {

		// we need to make sure specified user has the same scratches as a leader
		copyAnswersFromLeader(assessmentUser, groupLeader);
	    }

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

    public AssessmentOutputFactory getAssessmentOutputFactory() {
	return assessmentOutputFactory;
    }

    public void setAssessmentOutputFactory(AssessmentOutputFactory assessmentOutputFactory) {
	this.assessmentOutputFactory = assessmentOutputFactory;
    }

    @Override
    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
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
     * (Integer), "questionDisplayOrder" (Integer - to match to the question). It may also have "defaultGrade" (Integer)
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
	assessment.setAllowRightAnswersAfterQuestion(
		JsonUtil.optBoolean(toolContentJSON, "allowRightAnswersAfterQuestion", Boolean.FALSE));
	assessment.setAllowWrongAnswersAfterQuestion(
		JsonUtil.optBoolean(toolContentJSON, "allowWrongAnswersAfterQuestion", Boolean.FALSE));
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
	assessment.setTimeLimit(JsonUtil.optInt(toolContentJSON, "timeLimit", 0));
	assessment.setUseSelectLeaderToolOuput(
		JsonUtil.optBoolean(toolContentJSON, RestTags.USE_SELECT_LEADER_TOOL_OUTPUT, Boolean.FALSE));
	// submission deadline set in monitoring

	if (toolContentJSON.has("overallFeedback")) {
	    throw new IOException(
		    "Assessment Tool does not support Overall Feedback for REST Authoring. " + toolContentJSON);
	}

	AssessmentUser assessmentUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (assessmentUser == null) {
	    assessmentUser = new AssessmentUser();
	    assessmentUser.setFirstName(toolContentJSON.get("firstName").asText());
	    assessmentUser.setLastName(toolContentJSON.get("lastName").asText());
	    assessmentUser.setLoginName(toolContentJSON.get("loginName").asText());
	    assessmentUser.setAssessment(assessment);
	}
	assessment.setCreatedBy(assessmentUser);

	// **************************** Set the question bank *********************
	ArrayNode questions = JsonUtil.optArray(toolContentJSON, "questions");
	Set newQuestionSet = assessment.getQuestions(); // the Assessment constructor will set up the collection
	for (JsonNode questionJSONData : questions) {
	    AssessmentQuestion question = new AssessmentQuestion();
	    short type = JsonUtil.optInt(questionJSONData, "type").shortValue();
	    question.setType(type);
	    question.setTitle(questionJSONData.get(RestTags.QUESTION_TITLE).asText());
	    question.setQuestion(questionJSONData.get(RestTags.QUESTION_TEXT).asText());
	    question.setSequenceId(JsonUtil.optInt(questionJSONData, RestTags.DISPLAY_ORDER));

	    question.setAllowRichEditor(
		    JsonUtil.optBoolean(questionJSONData, RestTags.ALLOW_RICH_TEXT_EDITOR, Boolean.FALSE));
	    question.setAnswerRequired(JsonUtil.optBoolean(questionJSONData, "answerRequired", Boolean.FALSE));
	    question.setCaseSensitive(JsonUtil.optBoolean(questionJSONData, "caseSensitive", Boolean.FALSE));
	    question.setCorrectAnswer(JsonUtil.optBoolean(questionJSONData, "correctAnswer", Boolean.FALSE));
	    question.setDefaultGrade(JsonUtil.optInt(questionJSONData, "defaultGrade", 1));
	    question.setFeedback(JsonUtil.optString(questionJSONData, "feedback"));
	    question.setFeedbackOnCorrect(JsonUtil.optString(questionJSONData, "feedbackOnCorrect"));
	    question.setFeedbackOnIncorrect(JsonUtil.optString(questionJSONData, "feedbackOnIncorrect"));
	    question.setFeedbackOnPartiallyCorrect(JsonUtil.optString(questionJSONData, "feedbackOnPartiallyCorrect"));
	    question.setGeneralFeedback(JsonUtil.optString(questionJSONData, "generalFeedback", ""));
	    question.setMaxWordsLimit(JsonUtil.optInt(questionJSONData, "maxWordsLimit", 0));
	    question.setMinWordsLimit(JsonUtil.optInt(questionJSONData, "minWordsLimit", 0));
	    question.setMultipleAnswersAllowed(
		    JsonUtil.optBoolean(questionJSONData, "multipleAnswersAllowed", Boolean.FALSE));
	    question.setIncorrectAnswerNullifiesMark(
		    JsonUtil.optBoolean(questionJSONData, "incorrectAnswerNullifiesMark", Boolean.FALSE));
	    question.setPenaltyFactor(JsonUtil.optDouble(questionJSONData, "penaltyFactor", 0.0).floatValue());
	    // question.setUnits(units); Needed for numerical type question

	    if ((type == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS)
		    || (type == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		    || (type == AssessmentConstants.QUESTION_TYPE_NUMERICAL)
		    || (type == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING)) {

		if (!questionJSONData.has(RestTags.ANSWERS)) {
		    throw new IOException("REST Authoring is missing answers for a question of type " + type + ". Data:"
			    + toolContentJSON);
		}

		Set<AssessmentQuestionOption> optionList = new LinkedHashSet<>();
		ArrayNode optionsData = JsonUtil.optArray(questionJSONData, RestTags.ANSWERS);
		for (JsonNode answerData : optionsData) {
		    AssessmentQuestionOption option = new AssessmentQuestionOption();
		    option.setSequenceId(JsonUtil.optInt(answerData, RestTags.DISPLAY_ORDER));
		    option.setGrade(answerData.get("grade").floatValue());
		    option.setCorrect(JsonUtil.optBoolean(answerData, "correct", false));
		    option.setAcceptedError(JsonUtil.optDouble(answerData, "acceptedError", 0.0).floatValue());
		    option.setFeedback(JsonUtil.optString(answerData, "feedback"));
		    option.setOptionString(JsonUtil.optString(answerData, RestTags.ANSWER_TEXT));
		    option.setOptionFloat(JsonUtil.optDouble(answerData, "answerFloat", 0.0).floatValue());
		    // option.setQuestion(question); can't find the use for this field yet!
		    optionList.add(option);
		}
		question.setOptions(optionList);
	    }

	    checkType(question.getType());
	    newQuestionSet.add(question);
	}

	// **************************** Now set up the references to the questions in the bank *********************
	ArrayNode references = JsonUtil.optArray(toolContentJSON, "references");
	Set newReferenceSet = assessment.getQuestionReferences(); // the Assessment constructor will set up the

	;// collection
	for (JsonNode referenceJSONData : references) {
	    QuestionReference reference = new QuestionReference();
	    reference.setType((short) 0);
	    reference.setDefaultGrade(JsonUtil.optInt(referenceJSONData, "defaultGrade", 1));
	    reference.setSequenceId(JsonUtil.optInt(referenceJSONData, RestTags.DISPLAY_ORDER));
	    AssessmentQuestion matchingQuestion = matchQuestion(newQuestionSet,
		    JsonUtil.optInt(referenceJSONData, "questionDisplayOrder"));
	    if (matchingQuestion == null) {
		throw new IOException("Unable to find matching question for displayOrder "
			+ referenceJSONData.get("questionDisplayOrder") + ". Data:" + toolContentJSON);
	    }
	    reference.setQuestion(matchingQuestion);
	    reference.setRandomQuestion(JsonUtil.optBoolean(referenceJSONData, "randomQuestion", Boolean.FALSE));
	    reference.setTitle(null);
	    newReferenceSet.add(reference);
	}

	saveOrUpdateAssessment(assessment);

    }

    // find the question that matches this sequence id - used by the REST calls only.
    AssessmentQuestion matchQuestion(Set<AssessmentQuestion> newReferenceSet, Integer displayOrder) {
	if (displayOrder != null) {
	    for (AssessmentQuestion question : newReferenceSet) {
		if (displayOrder.equals(question.getSequenceId())) {
		    return question;
		}
	    }
	}
	return null;
    }

    // TODO Implement REST support for all types and then remove checkType method
    void checkType(short type) throws IOException {
	if ((type != AssessmentConstants.QUESTION_TYPE_ESSAY)
		&& (type != AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)) {
	    throw new IOException(
		    "Assessment Tool does not support REST Authoring for anything but Essay Type and Multiple Choice. Found type "
			    + type);
	}
	// public static final short QUESTION_TYPE_MATCHING_PAIRS = 2;
	// public static final short QUESTION_TYPE_SHORT_ANSWER = 3;
	// public static final short QUESTION_TYPE_NUMERICAL = 4;
	// public static final short QUESTION_TYPE_TRUE_FALSE = 5;
	// public static final short QUESTION_TYPE_ORDERING = 7;
    }
}
