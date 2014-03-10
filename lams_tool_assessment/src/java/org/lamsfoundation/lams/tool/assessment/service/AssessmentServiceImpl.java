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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.tool.assessment.util.ReflectDTOComparator;
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
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * @author Andrey Balan
 */
public class AssessmentServiceImpl implements IAssessmentService, ToolContentManager, ToolSessionManager,
	ToolContentImport102Manager {
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

	AssessmentSession session = this.getAssessmentSessionBySessionId(toolSessionId);
	AssessmentUser groupLeader = session.getGroupLeader();
	
	boolean isUserLeader = (groupLeader != null) && user.getUid().equals(groupLeader.getUid());
	return isUserLeader;
    }
    
    @Override
    public AssessmentUser checkLeaderSelectToolForSessionLeader(AssessmentUser user, Long toolSessionId) {
	if (user == null || toolSessionId == null) {
	    return null;
	}

	AssessmentSession assessmentSession = this.getAssessmentSessionBySessionId(toolSessionId);
	AssessmentUser leader = assessmentSession.getGroupLeader();
	// check leader select tool for a leader only in case QA tool doesn't know it. As otherwise it will screw
	// up previous scratches done
	if (leader == null) {

	    Long leaderUserId = toolService.getLeaderUserId(toolSessionId, user.getUserId().intValue());
	    if (leaderUserId != null) {
		leader = this.getUserByIDAndSession(leaderUserId, toolSessionId);

		// create new user in a DB
		if (leader == null) {
		    log.debug("creating new user with userId: " + leaderUserId);
		    User leaderDto = (User) getUserManagementService().findById(User.class, leaderUserId.intValue());
		    String userName = leaderDto.getLogin();
		    String fullName = leaderDto.getFirstName() + " " + leaderDto.getLastName();
		    leader = new AssessmentUser(leaderDto.getUserDTO(), assessmentSession);
		    this.createUser(leader);
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
		    userOptionAnswer.setQuestionOptionUid(leaderOptionAnswer.getQuestionOptionUid());
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
			    if (userOptionAnswer.getQuestionOptionUid().equals(leaderOptionAnswer.getQuestionOptionUid())) {
				
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
	AssessmentResult lastResult = this.getLastAssessmentResult(assessment.getUid(), assessmentUser.getUserId());
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
    public void storeUserAnswers(Long assessmentUid, Long userId,
	    ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions, boolean isAutosave) {
	
	SortedSet<AssessmentQuestionResult> questionResultList = new TreeSet<AssessmentQuestionResult>(
		new AssessmentQuestionResultComparator());
	int maximumGrade = 0;
	float grade = 0;
	
	for (LinkedHashSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		int numberWrongAnswers = assessmentQuestionResultDao.getNumberWrongAnswersDoneBefore(assessmentUid,
			userId, question.getUid());
		AssessmentQuestionResult processedAnswer = this.storeUserAnswer(question, numberWrongAnswers);
		questionResultList.add(processedAnswer);

		maximumGrade += question.getGrade();
		grade += processedAnswer.getMark();
	    }
	}
	
	AssessmentResult result = assessmentResultDao.getLastAssessmentResult(assessmentUid, userId);
	result.setQuestionResults(questionResultList);
	//store grades and finished date only on user hitting submit all answers button 
	if (!isAutosave) {
	    result.setMaximumGrade(maximumGrade);
	    result.setGrade(grade);
	    result.setFinishDate(new Timestamp(new Date().getTime()));
	}
	assessmentResultDao.saveObject(result);
    }

    private AssessmentQuestionResult storeUserAnswer(AssessmentQuestion question, int numberWrongAnswers) {
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
	questionResult.setMaxMark(maxMark);
	
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
    public List<Object[]> getAssessmentQuestionResultList(Long assessmentUid, Long userId,
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
	AssessmentQuestionResult questionResult = assessmentQuestionResultDao
		.getAssessmentQuestionResultByUid(questionResultUid);
	float oldMark = questionResult.getMark();
	AssessmentResult assessmentResult = questionResult.getAssessmentResult();
	float totalMark = assessmentResult.getGrade() - oldMark + newMark;
	
	Long toolSessionId = questionResult.getAssessmentResult().getSessionId();
	Assessment assessment = questionResult.getAssessmentResult().getAssessment();
	Long questionUid = questionResult.getAssessmentQuestion().getUid();
	
	// When changing a mark for user and isUseSelectLeaderToolOuput is true, the mark should be propagated to all
	// students within the group
	List<AssessmentUser> users = new ArrayList<AssessmentUser>();
	if (assessment.isUseSelectLeaderToolOuput()) {
	    users = this.getUsersBySession(toolSessionId);
	} else {
	    users = new ArrayList<AssessmentUser>();
	    AssessmentUser leader = questionResult.getUser();
	    users.add(leader);
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
		    AssessmentSession assessmentSession = this.getAssessmentSessionBySessionId(toolSessionId);
		    Assessment assessment = assessmentSession.getAssessment();

		    if (assessment.isUseSelectLeaderToolOuput()) {

			AssessmentUser leader = assessmentSession.getGroupLeader();
			if (leader == null) {
			    continue;
			}

			AssessmentResult leaderLastResult = this.getLastFinishedAssessmentResult(assessment.getUid(),
				leader.getUserId());
			if (leaderLastResult == null) {
			    continue;
			}
			Double mark = new Double(leaderLastResult.getGrade());

			// update marks for all learners in a group
			List<AssessmentUser> users = this.getUsersBySession(toolSessionId);
			for (AssessmentUser user : users) {
			    this.copyAnswersFromLeader(user, leader);

			    // propagade total mark to Gradebook
			    gradebookService.updateActivityMark(mark, null, user.getUserId().intValue(), toolSessionId,
				    true);
			}
		    } else {

			// update marks for all learners in a group
			List<AssessmentUser> users = this.getUsersBySession(toolSessionId);
			for (AssessmentUser user : users) {
			    AssessmentResult userLastResult = this.getLastFinishedAssessmentResult(assessment.getUid(),
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
}
