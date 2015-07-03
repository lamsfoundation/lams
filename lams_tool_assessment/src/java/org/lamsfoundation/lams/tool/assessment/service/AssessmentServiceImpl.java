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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.rest.RestTags;
import org.lamsfoundation.lams.rest.ToolRestManager;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
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
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * @author Andrey Balan
 */
public class AssessmentServiceImpl implements IAssessmentService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager, ToolRestManager{
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

    private ILessonService lessonService;
    
    private IActivityDAO activityDAO;
    
    private IUserManagementService userService;

    // *******************************************************************************
    // Service method
    // *******************************************************************************
    @Override
    public boolean isUserGroupLeader(AssessmentUser user, Long toolSessionId) {

	AssessmentSession session = getAssessmentSessionBySessionId(toolSessionId);
	AssessmentUser groupLeader = session.getGroupLeader();
	
	boolean isUserLeader = (groupLeader != null) && user.getUid().equals(groupLeader.getUid());
	return isUserLeader;
    }
    
    @Override
    public AssessmentUser checkLeaderSelectToolForSessionLeader(AssessmentUser user, Long toolSessionId) {
	if (user == null || toolSessionId == null) {
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
		    log.debug("creating new user with userId: " + leaderUserId);
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

	AssessmentResult leaderResult = assessmentResultDao.getLastFinishedAssessmentResult(assessmentUid, leader.getUserId());
	AssessmentResult userResult = assessmentResultDao.getLastAssessmentResult(assessmentUid, user.getUserId());
	Set<AssessmentQuestionResult> leaderQuestionResults = leaderResult.getQuestionResults();

	// if response doesn't exist create new empty objects which we populate on the next step
	if (userResult == null) {
	    userResult = new AssessmentResult();
	    userResult.setAssessment(leaderResult.getAssessment());
	    userResult.setUser(user);
	    userResult.setSessionId(leaderResult.getSessionId());
	    
	    Set<AssessmentQuestionResult> userQuestionResults = userResult.getQuestionResults();
	    for (AssessmentQuestionResult leaderQuestionResult: leaderQuestionResults) {
		AssessmentQuestionResult userQuestionResult = new AssessmentQuestionResult();
		userQuestionResult.setAssessmentQuestion(leaderQuestionResult.getAssessmentQuestion());
		userQuestionResult.setAssessmentResult(userResult);
		userQuestionResults.add(userQuestionResult);
		
		Set<AssessmentOptionAnswer> leaderOptionAnswers = leaderQuestionResult.getOptionAnswers();
		Set<AssessmentOptionAnswer> userOptionAnswers = userQuestionResult.getOptionAnswers();
		for (AssessmentOptionAnswer leaderOptionAnswer: leaderOptionAnswers) {
		    AssessmentOptionAnswer userOptionAnswer = new AssessmentOptionAnswer();
		    userOptionAnswer.setOptionUid(leaderOptionAnswer.getOptionUid());
		    userOptionAnswers.add(userOptionAnswer);
		}
	    }
	}

	//copy results from leader to user in both cases (when there is no userResult yet and when if it's been changed by the leader)
	userResult.setStartDate(leaderResult.getStartDate());
	userResult.setFinishDate(leaderResult.getFinishDate());
	userResult.setMaximumGrade(leaderResult.getMaximumGrade());
	userResult.setGrade(leaderResult.getGrade());
	
	Set<AssessmentQuestionResult> userQuestionResults = userResult.getQuestionResults();
	for (AssessmentQuestionResult leaderQuestionResult : leaderQuestionResults) {
	    for (AssessmentQuestionResult userQuestionResult : userQuestionResults) {
		if (userQuestionResult.getAssessmentQuestion().getUid().equals(leaderQuestionResult.getAssessmentQuestion().getUid())) {
		    
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
    public List<AssessmentUser> getUsersBySession(Long toolSessionID) {
	return assessmentUserDao.getBySessionID(toolSessionID);
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
	content = Assessment.newInstance(defaultContent, contentId);
	return content;
    }

    @Override
    public List getAuthoredQuestions(Long assessmentUid) {
	return assessmentQuestionDao.getAuthoringQuestions(assessmentUid);
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
    public void saveOrUpdateAssessment(Assessment assessment) {
	assessmentDao.saveObject(assessment);
    }
    
    @Override
    public void releaseQuestionsAndReferencesFromCache(Assessment assessment) {
	for (AssessmentQuestion question : (Set<AssessmentQuestion>)assessment.getQuestions()) {
	    assessmentQuestionDao.evict(question);
	}
	for (QuestionReference reference : (Set<QuestionReference>)assessment.getQuestionReferences()) {
	    assessmentQuestionDao.evict(reference);
	}
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
	AssessmentResult lastResult = getLastAssessmentResult(assessment.getUid(), assessmentUser.getUserId());
	//don't instantiate new attempt if the previous one wasn't finished and thus continue working with it
	if ((lastResult != null) && (lastResult.getFinishDate() == null)) {
	    return;
	}
	
	AssessmentResult result = new AssessmentResult();
	result.setAssessment(assessment);
	result.setUser(assessmentUser);
	result.setSessionId(toolSessionId);
	result.setStartDate(new Timestamp(new Date().getTime()));
	assessmentResultDao.saveObject(result);
    }

    @Override
    public boolean storeUserAnswers(Long assessmentUid, Long userId,
	    ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions, Long singleMarkHedgingQuestionUid,
	    boolean isAutosave) {
	
	int maximumGrade = 0;
	float grade = 0;

	AssessmentResult result = assessmentResultDao.getLastAssessmentResult(assessmentUid, userId);
	Assessment assessment = result.getAssessment();
	
	//prohibit users from submitting (or autosubmitting) answers after result is finished but Resubmit button is not pressed (e.g. using 2 browsers) 
	if (result.getFinishDate() != null) {
	    return false;
	}
	
	//store all answers (in all pages)
	for (LinkedHashSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		
		// In case if assessment was updated after result has been started check question still exists in DB as
		// it could be deleted if modified in monitor.
		if ((assessment.getUpdated() != null) && assessment.getUpdated().after(result.getStartDate())) {

		    Set<QuestionReference> references = assessment.getQuestionReferences();
		    Set<AssessmentQuestion> questions = assessment.getQuestions();
		    
		    boolean isQuestionExists = false;
		    for (QuestionReference reference : references) {
			if (!reference.isRandomQuestion() && reference.getQuestion().getUid().equals(question.getUid())) {
			    isQuestionExists = true;
			    break;
			}
			if (reference.isRandomQuestion()) {
			    for (AssessmentQuestion questionDb : questions) {
				if (questionDb.getUid().equals(question.getUid())) {
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
		
		float userQeustionGrade = storeUserAnswer(result, question, isAutosave);
		grade += userQeustionGrade;

		maximumGrade += question.getGrade();
	    }
	}
	
	//store grades and finished date only on user hitting submit all answers button 
	if (!isAutosave) {
	    result.setMaximumGrade(maximumGrade);
	    result.setGrade(grade);
	    result.setFinishDate(new Timestamp(new Date().getTime()));
	    assessmentResultDao.saveObject(result);
	}
	
	return true;
    }

    /**
     * Stores given AssessmentQuestion's answer.
     * 
     * @param isAutosave in case of autosave there is no need to calculate marks
     * @return grade that user scored by answering that question
     */
    private float storeUserAnswer(AssessmentResult assessmentResult, AssessmentQuestion question, boolean isAutosave) {
	
	AssessmentQuestionResult questionAnswer = null;
	// get questionResult from DB instance of AssessmentResult
	for (AssessmentQuestionResult dbQuestionAnswer : assessmentResult.getQuestionResults()) {
	    if (question.getUid().equals(dbQuestionAnswer.getAssessmentQuestion().getUid())) {
		questionAnswer = dbQuestionAnswer;
	    }
	}
	
	//create new questionAnswer if it's nonexistent
	if (questionAnswer == null) {
	    questionAnswer = new AssessmentQuestionResult();
	    questionAnswer.setAssessmentQuestion(question);
	    questionAnswer.setAssessmentResult(assessmentResult);
	    
	    Set<AssessmentOptionAnswer> optionAnswers = questionAnswer.getOptionAnswers();
	    for (AssessmentQuestionOption option : question.getOptions()) {
		AssessmentOptionAnswer optionAnswer = new AssessmentOptionAnswer();
		optionAnswer.setOptionUid(option.getUid());
		optionAnswers.add(optionAnswer);
	    }
	    
	    assessmentQuestionResultDao.saveObject(questionAnswer);
	}
	
	//store question answer values
	questionAnswer.setAnswerBoolean(question.getAnswerBoolean());
	questionAnswer.setAnswerFloat(question.getAnswerFloat());
	questionAnswer.setAnswerString(question.getAnswerString());
	questionAnswer.setFinishDate(new Date());

	int j = 0;
	for (AssessmentQuestionOption option : question.getOptions()) {
	    
	    // get optionAnswer from questionAnswer
	    AssessmentOptionAnswer optionAnswer = null;
	    for (AssessmentOptionAnswer dbOptionAnswer : questionAnswer.getOptionAnswers()) {
		if (option.getUid().equals(dbOptionAnswer.getOptionUid())) {
		    optionAnswer = dbOptionAnswer;
		}
	    }
	    
	    //store option answer values
	    optionAnswer.setAnswerBoolean(option.getAnswerBoolean());
	    optionAnswer.setAnswerInt(option.getAnswerInt());
	    if (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		optionAnswer.setAnswerInt(j++);
	    }
	}

	float mark = 0;
	float maxMark = question.getGrade();
	if (question.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
	    for (AssessmentQuestionOption option : question.getOptions()) {
		if (option.getAnswerBoolean()) {
		    mark += option.getGrade() * maxMark;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
	    float maxMarkForCorrectAnswer = maxMark / question.getOptions().size();
	    for (AssessmentQuestionOption option : question.getOptions()) {
		if (option.getAnswerInt() == option.getUid()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER) {
	    for (AssessmentQuestionOption option : question.getOptions()) {
		String optionString = option.getOptionString().trim().replaceAll("\\*", ".*");
		Pattern pattern;
		if (question.isCaseSensitive()) {
		    pattern = Pattern.compile(optionString);
		} else {
		    pattern = Pattern.compile(optionString, java.util.regex.Pattern.CASE_INSENSITIVE
			    | java.util.regex.Pattern.UNICODE_CASE);
		}
		boolean isAnswerCorrect = (question.getAnswerString() != null) ? pattern.matcher(
			question.getAnswerString().trim()).matches() : false;

		if (isAnswerCorrect) {
		    mark = option.getGrade() * maxMark;
		    questionAnswer.setSubmittedOptionUid(option.getUid());
		    break;
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    String answerString = question.getAnswerString();
	    if (answerString != null) {
		for (AssessmentQuestionOption option : question.getOptions()) {
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
			questionAnswer.setSubmittedOptionUid(option.getUid());
			break;
		    }
		}
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
	    if (question.getAnswerBoolean() == question.getCorrectAnswer() && question.getAnswerString() != null) {
		mark = maxMark;
	    }
	} else if (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    float maxMarkForCorrectAnswer = maxMark / question.getOptions().size();
	    TreeSet<AssessmentQuestionOption> correctOptionSet = new TreeSet<AssessmentQuestionOption>(
		    new SequencableComparator());
	    correctOptionSet.addAll(question.getOptions());
	    ArrayList<AssessmentQuestionOption> correctOptionList = new ArrayList<AssessmentQuestionOption>(
		    correctOptionSet);
	    int i = 0;
	    for (AssessmentQuestionOption option : question.getOptions()) {
		if (option.getUid() == correctOptionList.get(i++).getUid()) {
		    mark += maxMarkForCorrectAnswer;
		}
	    }
	}
	
	//we start calculating and storing marks only in case it's not an autosave request
	if (!isAutosave) {

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
			userId, question.getUid());

		// calculate penalty itself
		float penalty = question.getPenaltyFactor() * numberWrongAnswers;
		mark -= penalty;
		if (penalty > maxMark) {
		    penalty = maxMark;
		}
		questionAnswer.setPenalty(penalty);
		
		//don't let penalty make mark less than 0
		if (mark < 0) {
		    mark = 0;
		}
	    }
	    
	    questionAnswer.setMark(mark);
	    questionAnswer.setMaxMark(maxMark);
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
    public Float getLastFinishedAssessmentResultGrade(Long assessmentUid, Long userId) {
	return assessmentResultDao.getLastFinishedAssessmentResultGrade(assessmentUid, userId);
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
    public List<Object[]> getAssessmentQuestionResultList(Long assessmentUid, Long userId,
	    Long questionUid) {
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
    public List<ReflectDTO> getReflectList(Long contentId) {
	List<ReflectDTO> reflectList = new LinkedList<ReflectDTO>();

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
		    Date postedDate = (entry.getLastModified() != null) ? entry.getLastModified() : entry
			    .getCreateDate();
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
    public AssessmentQuestion getAssessmentQuestionByUid(Long questionUid) {
	return assessmentQuestionDao.getByUid(questionUid);
    }

    @Override
    public List<Summary> getSummaryList(Long contentId) {
	List<Summary> summaryList = new ArrayList<Summary>();

	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(contentId);
	for (AssessmentSession session : sessionList) {
	    Long sessionId = session.getSessionId();
	    Assessment assessment = session.getAssessment();
	    // one new summary for one session.
	    Summary summary = new Summary(sessionId, session.getSessionName());

	    List<AssessmentUser> users = new LinkedList<AssessmentUser>();
	    if (assessment.isUseSelectLeaderToolOuput()) {
		AssessmentUser groupLeader = session.getGroupLeader();
		if (groupLeader != null) {
		    users.add(groupLeader);
		}
	    } else {
		users = assessmentUserDao.getBySessionID(sessionId);
	    }
	    
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
	    
	    Assessment assessment = session.getAssessment();
	    Long sessionId = session.getSessionId();
	    List<AssessmentUser> users = new ArrayList<AssessmentUser>();
	    
	    //in case of leader aware tool show only leaders' responses
	    if (assessment.isUseSelectLeaderToolOuput()) {
		AssessmentUser leader = session.getGroupLeader();
		if (leader != null) {
		    users.add(leader);
		}
	    } else {
		users = assessmentUserDao.getBySessionID(sessionId);
	    }
	    
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
	AssessmentQuestionResult questionAnswer = assessmentQuestionResultDao
		.getAssessmentQuestionResultByUid(questionResultUid);
	float oldMark = questionAnswer.getMark();
	AssessmentResult assessmentResult = questionAnswer.getAssessmentResult();
	float totalMark = assessmentResult.getGrade() - oldMark + newMark;
	
	Long toolSessionId = assessmentResult.getSessionId();
	Assessment assessment = assessmentResult.getAssessment();
	Long questionUid = questionAnswer.getAssessmentQuestion().getUid();
	
	// When changing a mark for user and isUseSelectLeaderToolOuput is true, the mark should be propagated to all
	// students within the group
	List<AssessmentUser> users = new ArrayList<AssessmentUser>();
	if (assessment.isUseSelectLeaderToolOuput()) {
	    users = getUsersBySession(toolSessionId);
	} else {
	    users = new ArrayList<AssessmentUser>();
	    AssessmentUser user = assessmentResult.getUser();
	    users.add(user);
	}
	
	for (AssessmentUser user : users) {
	    Long userId = user.getUserId();
	    
	    List<Object[]> questionResults = assessmentQuestionResultDao
		    .getAssessmentQuestionResultList(assessment.getUid(), userId, questionUid);

	    if (questionResults == null || questionResults.isEmpty()) {
		log.warn("User with uid: " + user.getUid() + " doesn't have any results despite the fact group leader has some.");
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
	    gradebookService.updateActivityMark(new Double(totalMark), null, userId.intValue(), toolSessionId, true);

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

	//create list of modified questions
	List<AssessmentQuestion> modifiedQuestions = new ArrayList<AssessmentQuestion>();
	for (AssessmentQuestion oldQuestion : oldQuestions) {
	    for (AssessmentQuestion newQuestion : newQuestions) {
		if (oldQuestion.getUid().equals(newQuestion.getUid())) {
		    
		    boolean isQuestionModified = false;

		    //title or question is different
		    if (!oldQuestion.getTitle().equals(newQuestion.getTitle())
			    || !oldQuestion.getQuestion().equals(newQuestion.getQuestion())
			    || (oldQuestion.getCorrectAnswer() != newQuestion.getCorrectAnswer())) {
			isQuestionModified = true;
		    }
		    
		    //options are different
		    Set<AssessmentQuestionOption> oldOptions = oldQuestion.getOptions();
		    Set<AssessmentQuestionOption> newOptions = newQuestion.getOptions();
		    for (AssessmentQuestionOption oldOption : oldOptions) {
			for (AssessmentQuestionOption newOption : newOptions) {
			    if (oldOption.getUid().equals(newOption.getUid())) {
				
				if (((oldQuestion.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) && (oldOption
					.getSequenceId() != newOption.getSequenceId()))
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
		    
		    if (isQuestionModified) {
			modifiedQuestions.add(newQuestion);
		    }
		}
	    }    
	}

	//create list of modified references
	//modifiedReferences holds pairs newReference -> oldReference.getDefaultGrade()
	Map<QuestionReference, Integer> modifiedReferences= new HashMap<QuestionReference, Integer>();
	for (QuestionReference oldReference : oldReferences) {
	    for (QuestionReference newReference : newReferences) {
		if (oldReference.getUid().equals(newReference.getUid())
			&& (oldReference.getDefaultGrade() != newReference.getDefaultGrade())) {
		    modifiedReferences.put(newReference, oldReference.getDefaultGrade());
		}
	    }
	}
	
	//create list of added references
	List<QuestionReference> addedReferences= new ArrayList<QuestionReference>();
	for (QuestionReference newReference : newReferences) {
	    boolean isNewReferenceMetInOldReferences = false;
	    
	    for (QuestionReference oldReference : oldReferences) {
		if (oldReference.getUid().equals(newReference.getUid())) {
		    isNewReferenceMetInOldReferences = true;
		}
	    }
	    
	    //if the new reference was not met in old references then it's the newly added reference
	    if (!isNewReferenceMetInOldReferences) {
		addedReferences.add(newReference);
	    }
	}
	
	List<AssessmentSession> sessionList = assessmentSessionDao.getByContentId(assessment.getContentId());
	for (AssessmentSession session : sessionList) {
	    Long toolSessionId = session.getSessionId();
	    Set<AssessmentUser> sessionUsers = session.getAssessmentUsers();
	    
	    for (AssessmentUser user : sessionUsers) {
		
		//get all finished user results
		List<AssessmentResult> assessmentResults = assessmentResultDao.getAssessmentResults(
			assessment.getUid(), user.getUserId());
		AssessmentResult lastAssessmentResult = (assessmentResults.isEmpty()) ? null : assessmentResults
			.get(assessmentResults.size() - 1);
		
		for (AssessmentResult assessmentResult : assessmentResults) {

		    float assessmentMark = assessmentResult.getGrade();
		    int assessmentMaxMark = assessmentResult.getMaximumGrade();

		    Set<AssessmentQuestionResult> questionAnswers = assessmentResult.getQuestionResults();
		    Iterator<AssessmentQuestionResult> iter = questionAnswers.iterator();
		    while (iter.hasNext()) {
			AssessmentQuestionResult questionAnswer = iter.next();
			AssessmentQuestion question = questionAnswer.getAssessmentQuestion();
			
			boolean isRemoveQuestionResult = false;

			//[+] if the question reference was removed
			for (QuestionReference deletedReference : deletedReferences) {
			    if (!deletedReference.isRandomQuestion()
				    && question.getUid().equals(deletedReference.getQuestion().getUid())) {
				isRemoveQuestionResult = true;
				assessmentMaxMark -= deletedReference.getDefaultGrade();
				break;
			    }
			}
    		    
			//[+] if the question reference mark is modified
			for (QuestionReference modifiedReference : modifiedReferences.keySet()) {
			    if (!modifiedReference.isRandomQuestion()
				    && question.getUid().equals(modifiedReference.getQuestion().getUid())) {
				int newReferenceGrade = modifiedReference.getDefaultGrade();
				int oldReferenceGrade = modifiedReferences.get(modifiedReference);

				// update question answer's mark
				Float oldQuestionAnswerMark = questionAnswer.getMark();
				float newQuestionAnswerMark = oldQuestionAnswerMark * newReferenceGrade
					/ oldReferenceGrade;
				questionAnswer.setMark(newQuestionAnswerMark);
				questionAnswer.setMaxMark((float) newReferenceGrade);
				assessmentQuestionResultDao.saveObject(questionAnswer);

				assessmentMark += newQuestionAnswerMark - oldQuestionAnswerMark;
				assessmentMaxMark += newReferenceGrade - oldReferenceGrade;
				break;
			    }

			}

			//[+] if the question is modified
			for (AssessmentQuestion modifiedQuestion : modifiedQuestions) {
			    if (question.getUid().equals(modifiedQuestion.getUid())) {
				isRemoveQuestionResult = true;
				break;
			    }
			}
			
			//[+] if the question was removed 
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
			
			//[+] doing nothing if the new question was added

		    }
		    
		    //find all question answers from random question reference
		    ArrayList<AssessmentQuestionResult> nonRandomQuestionAnswers = new ArrayList<AssessmentQuestionResult>();
		    for (AssessmentQuestionResult questionAnswer : questionAnswers) {
			for (QuestionReference reference : newReferences) {
			    if (!reference.isRandomQuestion()
				    && questionAnswer.getAssessmentQuestion().getUid().equals(reference.getQuestion().getUid())) {
				nonRandomQuestionAnswers.add(questionAnswer);
			    }
			}
		    }
		    Collection<AssessmentQuestionResult> randomQuestionAnswers = CollectionUtils.subtract(
			    questionAnswers, nonRandomQuestionAnswers);
		    
		    // [+] if the question reference was removed (in case of random question references)
		    for (QuestionReference deletedReference : deletedReferences) {
			
			//in case of random question reference - search for the answer with the same maxmark
			if (deletedReference.isRandomQuestion()) {
			    
			    Iterator<AssessmentQuestionResult> iter2 = randomQuestionAnswers.iterator();
			    while (iter2.hasNext()) {
				AssessmentQuestionResult randomQuestionAnswer = iter2.next();
				if (randomQuestionAnswer.getMaxMark().intValue() == deletedReference.getDefaultGrade()) {

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
			
			//in case of random question reference - search for the answer with the same maxmark
			if (modifiedReference.isRandomQuestion()) {
			    
			    for (AssessmentQuestionResult randomQuestionAnswer : randomQuestionAnswers) {
				int newReferenceGrade = modifiedReference.getDefaultGrade();
				int oldReferenceGrade = modifiedReferences.get(modifiedReference);
				    
				if (randomQuestionAnswer.getMaxMark().intValue() == oldReferenceGrade) {

				    // update question answer's mark
				    Float oldQuestionAnswerMark = randomQuestionAnswer.getMark();
				    float newQuestionAnswerMark = oldQuestionAnswerMark * newReferenceGrade
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

			// if this is the last assessment result - propagade total mark to Gradebook
			if (lastAssessmentResult.getUid().equals(assessmentResult.getUid())) {
			    gradebookService.updateActivityMark(new Double(assessmentMark), null, user.getUserId()
				    .intValue(), toolSessionId, true);
			}
		    }

		}

	    }
	}
	

    }
    
    @Override
    public void recalculateMarkForLesson(UserDTO requestUserDTO, Long lessonId) {
	
	User requestUser = userService.getUserByLogin(requestUserDTO.getLogin());
	Lesson lesson = lessonService.getLesson(lessonId);
	Organisation organisation = lesson.getOrganisation();
	
	// skip doing anything if the user doesn't have permission
	Integer organisationToCheckPermission = (organisation.getOrganisationType().getOrganisationTypeId()
		.equals(OrganisationType.COURSE_TYPE)) ? organisation.getOrganisationId() : organisation
		.getParentOrganisation().getOrganisationId();
	boolean isGroupManager = userService.isUserInRole(requestUser.getUserId(), organisationToCheckPermission,
		Role.GROUP_MANAGER);
	if (!(lesson.getLessonClass().isStaffMember(requestUser) || isGroupManager)) {
	    return;
	}

	//get all lesson activities
	Set<Activity> lessonActivities = new TreeSet<Activity>();
	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 * 
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = activityDAO.getActivityByActivityId(lesson.getLearningDesign().getFirstActivity()
		.getActivityId());
	lessonActivities.add(firstActivity);
	lessonActivities.addAll(lesson.getLearningDesign().getActivities());

	//iterate through all assessment activities in the lesson
	for (Activity activity : lessonActivities) {
	    
	    // check if it's assessment activity
	    if ((activity instanceof ToolActivity)
		    && ((ToolActivity) activity).getTool().getToolSignature()
			    .equals(AssessmentConstants.TOOL_SIGNATURE)) {
		ToolActivity assessmentActivity = (ToolActivity) activity;

		for (ToolSession toolSession : (Set<ToolSession>) assessmentActivity.getToolSessions()) {
		    Long toolSessionId = toolSession.getToolSessionId();
		    AssessmentSession assessmentSession = getAssessmentSessionBySessionId(toolSessionId);
		    Assessment assessment = assessmentSession.getAssessment();

		    if (assessment.isUseSelectLeaderToolOuput()) {

			AssessmentUser leader = assessmentSession.getGroupLeader();
			if (leader == null) {
			    continue;
			}

			AssessmentResult leaderLastResult = getLastFinishedAssessmentResult(assessment.getUid(),
				leader.getUserId());
			if (leaderLastResult == null) {
			    continue;
			}
			Double mark = new Double(leaderLastResult.getGrade());

			// update marks for all learners in a group
			List<AssessmentUser> users = getUsersBySession(toolSessionId);
			for (AssessmentUser user : users) {
			    copyAnswersFromLeader(user, leader);

			    // propagade total mark to Gradebook
			    gradebookService.updateActivityMark(mark, null, user.getUserId().intValue(), toolSessionId,
				    true);
			}
		    } else {

			// update marks for all learners in a group
			List<AssessmentUser> users = getUsersBySession(toolSessionId);
			for (AssessmentUser user : users) {
			    AssessmentResult userLastResult = getLastFinishedAssessmentResult(assessment.getUid(),
				    user.getUserId());
			    if (userLastResult == null) {
				continue;
			    }
			    Double mark = new Double(userLastResult.getGrade());

			    // propagade total mark to Gradebook
			    gradebookService.updateActivityMark(mark, null, user.getUserId().intValue(), toolSessionId,
				    true);
			}
		    }

		}
	    }
	}

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
    
    @Override
    public void notifyTeachersOnAttemptCompletion(Long sessionId, String userName) {
	String message = getLocalisedMessage("event.learner.completes.attempt.body", new Object[] { userName });
	eventNotificationService.notifyLessonMonitors(sessionId, message, false);
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

	for (AssessmentQuestionOption option : question.getOptions()) {
	    String questionStr = option.getQuestion();
	    if (questionStr != null) {
		String questionEscaped = StringEscapeUtils.escapeJavaScript(questionStr);
		option.setQuestionEscaped(questionEscaped);
	    }

	    String optionStr = option.getOptionString();
	    if (optionStr != null) {
		String optionEscaped = StringEscapeUtils.escapeJavaScript(optionStr);
		option.setOptionStringEscaped(optionEscaped);
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

		// propagade changes to Gradebook
		gradebookService.updateActivityMark(null, null, userId, session.getSessionId(), false);

		assessmentUserDao.removeObject(AssessmentUser.class, user.getUid());
	    }
	}
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
    

    public boolean isContentEdited(Long toolContentId) {
	return getAssessmentByContentId(toolContentId).isDefineLater();
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

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }

    public Class[] getSupportedToolOutputDefinitionClasses(int definitionType) {
	return getAssessmentOutputFactory().getSupportedDefinitionClasses(definitionType);
    }

    public String getToolContentTitle(Long toolContentId) {
	return getAssessmentByContentId(toolContentId).getTitle();
    }

    
    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }
    
    public void setUserService(IUserManagementService userService) {
	this.userService = userService;
    }
    
    // ****************** REST methods *************************

    /**
     * Rest call to create a new Assessment content. Required fields in toolContentJSON: "title", "instructions",
     * "questions", "firstName", "lastName", "lastName", "questions" and "references".
     * 
     * The questions entry should be a JSONArray containing JSON objects, which in turn must contain "questionTitle",
     * "questionText", "displayOrder" (Integer), "type" (Integer). If the type is Multiple Choice, Numerical or Matching
     * Pairs then a JSONArray "answers" is required.
     * 
     * The answers entry should be JSONArray containing JSON objects, which in turn must contain "answerText" or
     * "answerFloat", "displayOrder" (Integer), "grade" (Integer).
     * 
     * The references entry should be a JSONArray containing JSON objects, which in turn must contain "displayOrder" (Integer), 
     * "questionDisplayOrder" (Integer - to match to the question). It may also have "defaultGrade" (Integer) and "randomQuestion" (Boolean)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void createRestToolContent(Integer userID, Long toolContentID, JSONObject toolContentJSON)
	    throws JSONException {

	Date updateDate = new Date();

	Assessment assessment = new Assessment();
	assessment.setContentId(toolContentID);
	assessment.setTitle(toolContentJSON.getString(RestTags.TITLE));
	assessment.setInstructions(toolContentJSON.getString(RestTags.INSTRUCTIONS));
	assessment.setCreated(updateDate);

	assessment.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	assessment.setReflectInstructions(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, (String) null));
	assessment.setAllowGradesAfterAttempt(JsonUtil.opt(toolContentJSON, "allowGradesAfterAttempt", Boolean.FALSE));
	assessment.setAllowHistoryResponses(JsonUtil.opt(toolContentJSON, "allowHistoryResponses", Boolean.FALSE));
	assessment.setAllowOverallFeedbackAfterQuestion(JsonUtil.opt(toolContentJSON,
		"allowOverallFeedbackAfterQuestion", Boolean.FALSE));
	assessment.setAllowQuestionFeedback(JsonUtil.opt(toolContentJSON, "allowQuestionFeedback", Boolean.FALSE));
	assessment.setAllowRightAnswersAfterQuestion(JsonUtil.opt(toolContentJSON, "allowRightAnswersAfterQuestion",
		Boolean.FALSE));
	assessment.setAllowWrongAnswersAfterQuestion(JsonUtil.opt(toolContentJSON, "allowWrongAnswersAfterQuestion",
		Boolean.FALSE));
	assessment.setAttemptsAllowed(JsonUtil.opt(toolContentJSON, "attemptsAllows", 1));
	assessment.setDefineLater(false);
	assessment.setDisplaySummary(JsonUtil.opt(toolContentJSON, "displaySummary", Boolean.FALSE));
	assessment.setNotifyTeachersOnAttemptCompletion(JsonUtil.opt(toolContentJSON,
		"notifyTeachersOnAttemptCompletion", Boolean.FALSE));
	assessment.setNumbered(JsonUtil.opt(toolContentJSON, "numbered", Boolean.TRUE));
	assessment.setPassingMark(JsonUtil.opt(toolContentJSON, "passingMark", 0));
	assessment.setQuestionsPerPage(JsonUtil.opt(toolContentJSON, "questionsPerPage", 0));
	assessment.setReflectInstructions(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_INSTRUCTIONS, ""));
	assessment.setReflectOnActivity(JsonUtil.opt(toolContentJSON, RestTags.REFLECT_ON_ACTIVITY, Boolean.FALSE));
	assessment.setShuffled(JsonUtil.opt(toolContentJSON, "shuffled", Boolean.FALSE));
	assessment.setSubmissionDeadline(JsonUtil.opt(toolContentJSON, "submissionDeadline", (Date) null));
	assessment.setTimeLimit(JsonUtil.opt(toolContentJSON, "timeLimit", 0));
	assessment
		.setUseSelectLeaderToolOuput(JsonUtil.opt(toolContentJSON, "useSelectLeaderToolOuput", Boolean.FALSE));

	if (toolContentJSON.has("overallFeedback")) {
	    throw new JSONException("Assessment Tool does not support Overall Feedback for REST Authoring. "
		    + toolContentJSON);
	}

	AssessmentUser assessmentUser = getUserByIDAndContent(userID.longValue(), toolContentID);
	if (assessmentUser == null) {
	    assessmentUser = new AssessmentUser();
	    assessmentUser.setFirstName(toolContentJSON.getString("firstName"));
	    assessmentUser.setLastName(toolContentJSON.getString("lastName"));
	    assessmentUser.setLoginName(toolContentJSON.getString("loginName"));
	    assessmentUser.setAssessment(assessment);
	}
	assessment.setCreatedBy(assessmentUser);

	// **************************** Set the question bank *********************
	JSONArray questions = toolContentJSON.getJSONArray("questions");
	Set newQuestionSet = assessment.getQuestions(); // the Assessment constructor will set up the collection
	for (int i = 0; i < questions.length(); i++) {
	    JSONObject questionJSONData = (JSONObject) questions.get(i);
	    AssessmentQuestion question = new AssessmentQuestion();
	    short type = (short) questionJSONData.getInt("type");
	    question.setType(type);
	    question.setTitle(questionJSONData.getString(RestTags.QUESTION_TITLE));
	    question.setQuestion(questionJSONData.getString(RestTags.QUESTION_TEXT));
	    question.setCreateBy(assessmentUser);
	    question.setCreateDate(updateDate);
	    question.setSequenceId(questionJSONData.getInt(RestTags.DISPLAY_ORDER));

	    question.setAllowRichEditor(JsonUtil.opt(questionJSONData, RestTags.ALLOW_RICH_TEXT_EDITOR, Boolean.FALSE));
	    question.setAnswerRequired(JsonUtil.opt(questionJSONData, "answerRequired", Boolean.FALSE));
	    question.setCaseSensitive(JsonUtil.opt(questionJSONData, "caseSensitive", Boolean.FALSE));
	    question.setCorrectAnswer(JsonUtil.opt(questionJSONData, "correctAnswer", Boolean.FALSE));
	    question.setDefaultGrade(JsonUtil.opt(questionJSONData, "defaultGrade", 1));
	    question.setFeedback(JsonUtil.opt(questionJSONData, "feedback", (String) null));
	    question.setFeedbackOnCorrect(JsonUtil.opt(questionJSONData, "feedbackOnCorrect", (String) null));
	    question.setFeedbackOnIncorrect(JsonUtil.opt(questionJSONData, "feedbackOnIncorrect", (String) null));
	    question.setFeedbackOnPartiallyCorrect(JsonUtil.opt(questionJSONData, "feedbackOnPartiallyCorrect",
		    (String) null));
	    question.setGeneralFeedback(JsonUtil.opt(questionJSONData, "generalFeedback", ""));
	    question.setMaxWordsLimit(JsonUtil.opt(questionJSONData, "maxWordsLimit", 0));
	    question.setMinWordsLimit(JsonUtil.opt(questionJSONData, "minWordsLimit", 0));
	    question.setMultipleAnswersAllowed(JsonUtil.opt(questionJSONData, "multipleAnswersAllowed", Boolean.FALSE));
	    question.setPenaltyFactor(Float.parseFloat(JsonUtil.opt(questionJSONData, "penaltyFactor", "0.0")));
	    // question.setUnits(units); Needed for numerical type question

	    if (type == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS
		    || type == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE
		    || type == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {

		if (!questionJSONData.has(RestTags.ANSWERS))
		    throw new JSONException("REST Authoring is missing answers for a question of type " + type
			    + ". Data:" + toolContentJSON);

		Set<AssessmentQuestionOption> optionList = new LinkedHashSet<AssessmentQuestionOption>();
		JSONArray optionsData = (JSONArray) questionJSONData.getJSONArray(RestTags.ANSWERS);
		for (int j = 0; j < optionsData.length(); j++) {
		    JSONObject answerData = (JSONObject) optionsData.get(j);
		    AssessmentQuestionOption option = new AssessmentQuestionOption();
		    option.setSequenceId(answerData.getInt(RestTags.DISPLAY_ORDER));
		    option.setGrade(Float.parseFloat(answerData.getString("grade")));
		    option.setAcceptedError(Float.parseFloat(JsonUtil.opt(answerData, "acceptedError", "0.0")));
		    option.setFeedback(JsonUtil.opt(answerData, "feedback", (String) null));
		    option.setOptionString(JsonUtil.opt(answerData, RestTags.ANSWER_TEXT, (String) null));
		    option.setOptionFloat(Float.parseFloat(JsonUtil.opt(answerData, "answerFloat", "0.0")));
		    // option.setQuestion(question); can't find the use for this field yet!
		    optionList.add(option);
		}
		question.setOptions(optionList);
	    }

	    checkType(question.getType());
	    newQuestionSet.add(question);
	}

	// **************************** Now set up the references to the questions in the bank *********************
	JSONArray references = toolContentJSON.getJSONArray("references");
	Set newReferenceSet = assessment.getQuestionReferences(); // the Assessment constructor will set up the
								  // collection
	for (int i = 0; i < references.length(); i++) {
	    JSONObject referenceJSONData = (JSONObject) references.get(i);
	    QuestionReference reference = new QuestionReference();
	    reference.setType((short) 0);
	    reference.setDefaultGrade(JsonUtil.opt(referenceJSONData, "defaultGrade", 1));
	    reference.setSequenceId(referenceJSONData.getInt(RestTags.DISPLAY_ORDER));
	    AssessmentQuestion matchingQuestion = matchQuestion(newQuestionSet,
		    referenceJSONData.getInt("questionDisplayOrder"));
	    if (matchingQuestion == null) {
		throw new JSONException("Unable to find matching question for displayOrder "
			+ referenceJSONData.get("questionDisplayOrder") + ". Data:" + toolContentJSON);
	    }
	    reference.setQuestion(matchingQuestion);
	    reference.setRandomQuestion(JsonUtil.opt(referenceJSONData, "randomQuestion", Boolean.FALSE));
	    reference.setTitle(null);
	    newReferenceSet.add(reference);
	}

	saveOrUpdateAssessment(assessment);

    }

    // find the question that matches this sequence id - used by the REST calls only.
    AssessmentQuestion matchQuestion(Set<AssessmentQuestion> newReferenceSet, Integer displayOrder) {
	if ( displayOrder != null ) {
	    for ( AssessmentQuestion question : newReferenceSet ) {
		if ( displayOrder.equals(question.getSequenceId()) )
			return question;
	    }
	}
	return null;
    }
    // TODO Implement REST support for all types and then remove checkType method
    void checkType(short type) throws JSONException {
	if (type != AssessmentConstants.QUESTION_TYPE_ESSAY) {
	    throw new JSONException(
		    "Assessment Tool does not support REST Authoring for anything but Essay Type. Found type " + type);
	}
	// public static final short QUESTION_TYPE_MULTIPLE_CHOICE = 1;
	// public static final short QUESTION_TYPE_MATCHING_PAIRS = 2;
	// public static final short QUESTION_TYPE_SHORT_ANSWER = 3;
	// public static final short QUESTION_TYPE_NUMERICAL = 4;
	// public static final short QUESTION_TYPE_TRUE_FALSE = 5;
	// public static final short QUESTION_TYPE_ORDERING = 7;
    }
}
