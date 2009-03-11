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

/* $Id$ */
package org.lamsfoundation.lams.tool.assessment.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.events.DeliveryMethodMail;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOverallFeedback;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("nextPage")) {
	    return nextPage(mapping, form, request, response);
	}
	if (param.equals("submitAll")) {
	    return submitAll(mapping, form, request, response);
	}
	if (param.equals("finishTest")) {
	    return finishTest(mapping, form, request, response);
	}
	if (param.equals("resubmit")) {
	    return resubmit(mapping, form, request, response);
	}	
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("upOption")) {
	    return upOption(mapping, form, request, response);
	}
	if (param.equals("downOption")) {
	    return downOption(mapping, form, request, response);
	}	

	return mapping.findForward(AssessmentConstants.ERROR);
    }

    /**
     * Read assessment data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
     * @throws ServletException 
     * 
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// initialize Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long toolSessionId = new Long(request.getParameter(AssessmentConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	// get back the assessment and question list and display them on page
	IAssessmentService service = getAssessmentService();
	AssessmentUser assessmentUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // assessmentUser may be null if the user was force completed.
	    assessmentUser = getSpecifiedUser(service, toolSessionId, WebUtil.readIntParam(request,
		    AttributeNames.PARAM_USER_ID, false));
	} else {
	    assessmentUser = getCurrentUser(service, toolSessionId);
	}

	List<AssessmentQuestion> questionsFromDB = service.getAssessmentQuestionsBySessionId(toolSessionId);
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);

	// check whehter finish lock is on/off
	// TODO!! assessment.getTimeLimit()
	HttpSession ss = SessionManager.getSession();
	//TODO check this user out
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(userDTO.getUserID().longValue());
	AssessmentUser user = service.getUserByIDAndSession(userID, toolSessionId);
	int dbResultCount = service.getAssessmentResultCount(assessment.getUid(), userID);
	int attemptsAllowed = assessment.getAttemptsAllowed();
	boolean finishedLock = ((assessmentUser != null) && assessmentUser.isSessionFinished())
		|| ((attemptsAllowed <= dbResultCount) && (attemptsAllowed != 0));

	// basic information
	sessionMap.put(AssessmentConstants.ATTR_TITLE, assessment.getTitle());
	sessionMap.put(AssessmentConstants.ATTR_INSTRUCTIONS, assessment.getInstructions());
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, false);
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, finishedLock);
	//sessionMap.put(AssessmentConstants.ATTR_LOCK_ON_FINISH, assessment.getTimeLimit());
	sessionMap.put(AssessmentConstants.ATTR_USER_FINISHED, assessmentUser != null
		&& assessmentUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AssessmentConstants.ATTR_USER, assessmentUser);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);

	// add define later support
	if (assessment.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	assessment.setContentInUse(true);
	assessment.setDefineLater(false);
	service.saveOrUpdateAssessment(assessment);

	// add run offline support
	if (assessment.getRunOffline()) {
	    sessionMap.put(AssessmentConstants.PARAM_RUN_OFFLINE, true);
	    return mapping.findForward("runOffline");
	} else {
	    sessionMap.put(AssessmentConstants.PARAM_RUN_OFFLINE, false);
	}

	
	Set<AssessmentQuestion> questionList = new TreeSet<AssessmentQuestion>(new SequencableComparator());
	if (questionsFromDB != null) {
	    // remove hidden questions.
	    for (AssessmentQuestion question : questionsFromDB) {
		// becuase in webpage will use this login name. Here is just
		// initialize it to avoid session close error in proxy object.
		if (question.getCreateBy() != null) {
		    question.getCreateBy().getLoginName();
		}
		if (!question.isHide()) {
		    questionList.add(question);
		}
	    }
	}

	// shuffling
	if (assessment.isShuffled()) {
	    ArrayList<AssessmentQuestion> shuffledList = new ArrayList<AssessmentQuestion>(questionList);
	    Collections.shuffle(shuffledList);
	    questionList = new LinkedHashSet<AssessmentQuestion>(shuffledList);
	}
	for (AssessmentQuestion question : questionList) {
	    if (question.isShuffle() || (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING)) {
		ArrayList<AssessmentQuestionOption> shuffledList = new ArrayList<AssessmentQuestionOption>(question.getQuestionOptions());
		Collections.shuffle(shuffledList);
		question.setQuestionOptions(new LinkedHashSet<AssessmentQuestionOption>(shuffledList));
	    }
	    if (question.getType() == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		ArrayList<AssessmentQuestionOption> shuffledList = new ArrayList<AssessmentQuestionOption>(question.getQuestionOptions());
		Collections.shuffle(shuffledList);
		question.setMatchingPairOptions(new LinkedHashSet<AssessmentQuestionOption>(shuffledList));
	    }	    
	}
	
	//setAttemptStarted
	if (! finishedLock) {
	    service.setAttemptStarted(assessment, assessmentUser, toolSessionId);
	}
	
	//paging
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = new ArrayList<LinkedHashSet<AssessmentQuestion>>();
	int maxQuestionsPerPage = (assessment.getQuestionsPerPage() != 0) ? assessment.getQuestionsPerPage()
		: questionList.size();
	LinkedHashSet<AssessmentQuestion> questionsForOnePage = new LinkedHashSet<AssessmentQuestion>();
	pagedQuestions.add(questionsForOnePage);

	int count = 0;
	for (AssessmentQuestion question : questionList) {
	    questionsForOnePage.add(question);
	    count++;
	    if ((questionsForOnePage.size() == maxQuestionsPerPage) && (count != questionList.size())) {
		questionsForOnePage = new LinkedHashSet<AssessmentQuestion>();
		pagedQuestions.add(questionsForOnePage);
	    }
	}
	
	sessionMap.put(AssessmentConstants.ATTR_PAGED_QUESTIONS, pagedQuestions);
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_NUMBERING_OFFSET, 1);	
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, assessment);
	
	// loadupLastAttempt for display purpose
	if (dbResultCount > 0) {
	    loadupLastAttempt(sessionMap);    
	    if (finishedLock) {
		loadupResultMarks(sessionMap);
	    }
	}

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }
    
    /**
     * Display same entire authoring page content from HttpSession variable.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	boolean finishedLock = (Boolean) sessionMap.get(AssessmentConstants.ATTR_FINISHED_LOCK);
	if (! finishedLock) {
	    preserveUserAnswers(request);   
	    request.setAttribute(AssessmentConstants.PARAM_SECONDS_LEFT, 
		    request.getParameter(AssessmentConstants.PARAM_SECONDS_LEFT));
	}
	
	int pageNumber = WebUtil.readIntParam(request, AssessmentConstants.ATTR_PAGE_NUMBER);
	int questionNumberingOffset = 0;
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	for (int i = 0; i < pageNumber-1; i++) {
	    LinkedHashSet<AssessmentQuestion> questionsForOnePage = pagedQuestions.get(i);
	    questionNumberingOffset += questionsForOnePage.size();
	}
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_NUMBERING_OFFSET, ++questionNumberingOffset);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, pageNumber);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);	
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }
    
    /**
     * Display same entire authoring page content from HttpSession variable.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward submitAll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	preserveUserAnswers(request);
	processUserAnswers(sessionMap);
	loadupResultMarks(sessionMap);
	
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, isResubmitAllowed(sessionMap));
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, true);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }    
    
    /**
     * Display same entire authoring page content from HttpSession variable.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward finishTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	preserveUserAnswers(request);
	processUserAnswers(sessionMap);
	loadupResultMarks(sessionMap);
	
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	IAssessmentService service = getAssessmentService();
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());
	    service.finishToolSession(sessionId, userID);
	} catch (AssessmentApplicationException e) {
	    LearningAction.log.error("Failed finishing tool session:" + e.getMessage());
	}
	
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, false);
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, true);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }   
    
    /**
     * Display same entire authoring page content from HttpSession variable.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward resubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	AssessmentUser assessmentUser = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	IAssessmentService service = getAssessmentService();
	service.setAttemptStarted(assessment, assessmentUser, toolSessionId);
	loadupLastAttempt(sessionMap);
	
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, false);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);	
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Finish learning session.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IAssessmentService service = getAssessmentService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(AssessmentConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (AssessmentApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }
    
    /**
     * Move up current option.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchOption(mapping, request, true);
    }

    /**
     * Move down current option.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchOption(mapping, request, false);
    }

    private ActionForward switchOption(ActionMapping mapping, HttpServletRequest request, boolean up) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	LinkedHashSet<AssessmentQuestion> questionsForOnePage = pagedQuestions.get(pageNumber-1);
	Long questionUid = new Long(request.getParameter(AssessmentConstants.PARAM_QUESTION_UID));
	
	AssessmentQuestion question = null;
	for (AssessmentQuestion tempQuestion : questionsForOnePage) {
	    if (tempQuestion.getUid().equals(questionUid)) {
		question = tempQuestion;
		break;
	    }
	}
	
	Set<AssessmentQuestionOption> optionList = question.getQuestionOptions();
	
	int optionIndex = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<AssessmentQuestionOption> rList = new ArrayList<AssessmentQuestionOption>(optionList);
	    
	    // get current and the target item, and switch their sequnece
	    AssessmentQuestionOption option = rList.remove(optionIndex);
	    if (up) {
		rList.add(--optionIndex, option);
	    } else {
		rList.add(++optionIndex, option);
	    }
		
	    // put back list
	    optionList = new LinkedHashSet<AssessmentQuestionOption>(rList);
	    question.setQuestionOptions(optionList);
	}

	request.setAttribute(AssessmentConstants.ATTR_QUESTION_FOR_ORDERING, question);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }    

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    
    private void preserveUserAnswers(HttpServletRequest request){
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	LinkedHashSet<AssessmentQuestion> questionsForOnePage = pagedQuestions.get(pageNumber-1);
	int count = questionsForOnePage.size(); 
	
	for (int i = 0; i < count; i++) {
	    Long assessmentQuestionUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_UID + i);
	    AssessmentQuestion question = null;
	    for (AssessmentQuestion sessionQuestion : questionsForOnePage) {
		if (sessionQuestion.getUid().equals(assessmentQuestionUid)) {
		    question = sessionQuestion;
		    break;
		}
	    }
	    int questionType = question.getType();

	    if (questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
		for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		    boolean answerBoolean = false;
		    if (question.isMultipleAnswersAllowed()) {
			String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_"
				+ option.getSequenceId());
			answerBoolean = !StringUtils.isBlank(answerString);
		    } else {
			String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
			if (answerString != null) {
			    int optionSequenceId = Integer.parseInt(answerString);
			    answerBoolean = (option.getSequenceId() == optionSequenceId);
			}
		    }
		    option.setAnswerBoolean(answerBoolean);
		}
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		    int answerInt = WebUtil.readIntParam(request, AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + option.getSequenceId()); 
		    option.setAnswerInt(answerInt);
		}
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		question.setAnswerString(answerString);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		question.setAnswerString(answerString);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		if (answerString != null) {
		    question.setAnswerBoolean(Boolean.parseBoolean(answerString));
		    question.setAnswerString("not null");
		}
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ESSAY) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		question.setAnswerString(answerString);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    }
	}
    }
    
    private void loadupResultMarks(SessionMap sessionMap){
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	AssessmentResult result = service.getLastAssessmentResult(assessment.getUid(), userId);
	
	for (LinkedHashSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
		    if (question.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
			question.setMark(questionResult.getMark());
			question.setPenalty(questionResult.getPenalty());

			question.setQuestionFeedback(null);
			for (AssessmentQuestionOption questionOption : question.getQuestionOptions()) {
			    if (questionOption.getUid().equals(questionResult.getSubmittedOptionUid())) {
				question.setQuestionFeedback(questionOption.getFeedback());
				break;
			    }
			}

			List<AssessmentQuestionResult> questionResults = service.getAssessmentQuestionResultList(
				assessment.getUid(), userId, question.getUid());
			question.setQuestionResults(questionResults);
		    }
		}
	    }
	}
	
	Date timeTaken = new Date(result.getFinishDate().getTime() - result.getStartDate().getTime()); 
	result.setTimeTaken(timeTaken);
	if (assessment.isAllowOverallFeedbackAfterQuestion()) {
	    int percentageCorrectAnswers = (int) (result.getGrade() * 100 / result.getMaximumGrade());
	    ArrayList<AssessmentOverallFeedback> overallFeedbacks = new ArrayList<AssessmentOverallFeedback>(assessment.getOverallFeedbacks());
	    int lastBorder = 0;
	    for (int i = overallFeedbacks.size()-1; i >= 0; i--) {
		AssessmentOverallFeedback overallFeedback = overallFeedbacks.get(i);
		if ((percentageCorrectAnswers >= lastBorder) && (percentageCorrectAnswers <= overallFeedback.getGradeBoundary())) {
		    result.setOverallFeedback(overallFeedback.getFeedback());
		    break;
		}
		lastBorder = overallFeedback.getGradeBoundary();
	    }
	}
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT_RESULT, result);
    }    
    
    private void loadupLastAttempt(SessionMap sessionMap){
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	AssessmentResult result = service.getLastAssessmentResult(assessmentUid,userId);
	
	for(LinkedHashSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
		    if (question.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
			question.setAnswerBoolean(questionResult.getAnswerBoolean());
			question.setAnswerFloat(questionResult.getAnswerFloat());
			question.setAnswerString(questionResult.getAnswerString());
			question.setMark(questionResult.getMark());
			question.setPenalty(questionResult.getPenalty());
			
			for (AssessmentQuestionOption questionOption : question.getQuestionOptions()) {
			    for (AssessmentOptionAnswer optionAnswer : questionResult.getOptionAnswers()) {
				if (questionOption.getUid().equals(optionAnswer.getQuestionOptionUid())) {
				    questionOption.setAnswerBoolean(optionAnswer.getAnswerBoolean());
				    questionOption.setAnswerInt(optionAnswer.getAnswerInt());
				    break;
				}
			    }			    
			}
			break;
		    }
		}		
	    }
	}
    }
    
    /**
     * Get answer options from <code>HttpRequest</code>
     * 
     * @param request
     * 
     */
    private void processUserAnswers(SessionMap sessionMap) {
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	service.processUserAnswers(assessmentUid, userId, pagedQuestions);
	
	// notify teachers
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	if ((mode != null) && !mode.isTeacher()) {
	    Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	    Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	    if (assessment.isNotifyTeachersOnAttemptCompletion()) {
		List<User> monitoringUsers = service.getMonitorsByToolSessionId(toolSessionId);
		if (monitoringUsers != null && !monitoringUsers.isEmpty()) {
		    Long[] monitoringUsersIds = new Long[monitoringUsers.size()];
		    for (int i = 0; i < monitoringUsersIds.length; i++) {
			monitoringUsersIds[i] = monitoringUsers.get(i).getUserId().longValue();
		    }
		    AssessmentUser assessmentUser = getCurrentUser(service, toolSessionId);
		    String fullName = assessmentUser.getLastName() + " " + assessmentUser.getFirstName();
		    service.getEventNotificationService().sendMessage(monitoringUsersIds,
			    DeliveryMethodMail.getInstance(),
			    service.getLocalisedMessage("event.learner.completes.attempt.subject", null),
			    service.getLocalisedMessage("event.learner.completes.attempt.body", new Object[] { fullName }));
		}
	    }
	}
    }
    
    /**
     * Checks if the resubmit action allowed.
     * 
     * @param request
     * 
     */
    private boolean isResubmitAllowed(SessionMap sessionMap) {
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	IAssessmentService service = getAssessmentService();
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(userDTO.getUserID().longValue());
	AssessmentUser user = service.getUserByIDAndSession(userID, toolSessionId);
	
	int dbResultCount = service.getAssessmentResultCount(assessment.getUid(), userID);
	int attemptsAllowed = assessment.getAttemptsAllowed();
	return ((attemptsAllowed > dbResultCount) | (attemptsAllowed == 0)) && !user.isSessionFinished();
    }
    
    private IAssessmentService getAssessmentService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
    }

    private AssessmentUser getCurrentUser(IAssessmentService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	AssessmentUser assessmentUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);

	if (assessmentUser == null) {
	    AssessmentSession session = service.getAssessmentSessionBySessionId(sessionId);
	    assessmentUser = new AssessmentUser(user, session);
	    service.createUser(assessmentUser);
	}
	return assessmentUser;
    }

    private AssessmentUser getSpecifiedUser(IAssessmentService service, Long sessionId, Integer userId) {
	AssessmentUser assessmentUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (assessmentUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for assessment activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return assessmentUser;
    }

}
