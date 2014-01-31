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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
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
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
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
	    HttpServletResponse response) throws IOException, ServletException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, JSONException {

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
	if (param.equals("autoSaveAnswers")) {
	    return autoSaveAnswers(mapping, form, request, response);
	}
	if (param.equals("checkLeaderProgress")) {
	    return checkLeaderProgress(mapping, form, request, response);
	}
	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(AssessmentConstants.ERROR);
    }

    /**
     * Read assessment data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page". 
     */
    @SuppressWarnings("unchecked")
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
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
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // assessmentUser may be null if the user was force completed.
	    assessmentUser = getSpecifiedUser(service, toolSessionId, WebUtil.readIntParam(request,
		    AttributeNames.PARAM_USER_ID, false));
	} else {
	    assessmentUser = getCurrentUser(service, toolSessionId);
	}
	
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);
	
	//*LKC* added the next chunk
	AssessmentUser groupLeader = null;
	if (assessment.isUseSelectLeaderToolOuput()) {
	    groupLeader = service.checkLeaderSelectToolForSessionLeader(assessmentUser, new Long(toolSessionId).longValue());

	    // forwards to the leaderSelection page
	    if (groupLeader == null && !mode.isTeacher()) {

		List<AssessmentUser> groupUsers = service.getUsersBySession(new Long(toolSessionId).longValue());
		request.setAttribute(AssessmentConstants.ATTR_GROUP_USERS, groupUsers);
		request.setAttribute(AssessmentConstants.ATTR_ASSESSMENT, assessment);

		return mapping.findForward(AssessmentConstants.WAIT_FOR_LEADER);
	    }

	    // check if leader has submitted all answers
	    if (groupLeader.isSessionFinished() && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		// in case user joins the lesson after leader has answers some answers already - we need to make sure
		// he has the same scratches as leader
		service.copyAnswersFromLeader(assessmentUser, groupLeader);

		//service.finishToolSession(toolSessionId, assessmentUser.getUserId());
//		user.setSessionFinished(true);
//		assessmentUserDao.saveObject(user);
//		assessmentUser.setSessionFinished(true);
//		service.saupdateAssessmentUser(assessmentUser);
	    }
	}
	
	sessionMap.put(AssessmentConstants.ATTR_GROUP_LEADER, groupLeader);
	boolean isUserLeader = service.isUserGroupLeader(assessmentUser, new Long(toolSessionId));
	sessionMap.put(AssessmentConstants.ATTR_IS_USER_LEADER, isUserLeader);

	Set<QuestionReference> questionReferences = new TreeSet<QuestionReference>(new SequencableComparator());
	questionReferences.addAll(assessment.getQuestionReferences());
	HashMap<Long, AssessmentQuestion> questionToReferenceMap = new HashMap<Long, AssessmentQuestion>();
	ArrayList<AssessmentQuestion> takenQuestion = new ArrayList<AssessmentQuestion>();
	
	//add non-random questions
	for (QuestionReference questionReference : questionReferences) {
	    if (!questionReference.isRandomQuestion()) {
		AssessmentQuestion question = questionReference.getQuestion();
		takenQuestion.add(question);
		questionToReferenceMap.put(questionReference.getUid(), question);
	    }
	}
	
	Set<AssessmentQuestion> allQuestions = assessment.getQuestions();
	Collection<AssessmentQuestion> availableQuestions = CollectionUtils.subtract(allQuestions, takenQuestion);
	//add random questions (actually replacing them with real ones)
	for (QuestionReference questionReference : questionReferences) {
	    if (questionReference.isRandomQuestion()) {
		//pick a random element
		Random rand = new Random(System.currentTimeMillis());
		AssessmentQuestion question = (AssessmentQuestion) availableQuestions.toArray()[rand.nextInt(availableQuestions.size())];
		takenQuestion.add(question);
		questionToReferenceMap.put(questionReference.getUid(), question);
		availableQuestions.remove(question);
	    }
	}

	int dbResultCount = service.getAssessmentResultCount(assessment.getUid(), assessmentUser.getUserId());
	int attemptsAllowed = assessment.getAttemptsAllowed();
	boolean isResubmitAllowed = ((attemptsAllowed > dbResultCount) | (attemptsAllowed == 0));
	
	AssessmentResult lastResult = service.getLastAssessmentResult(assessment.getUid(), assessmentUser.getUserId());
	boolean hasEditRight = !assessment.isUseSelectLeaderToolOuput() || assessment.isUseSelectLeaderToolOuput() && isUserLeader;
	boolean isLastResultFinished = (lastResult != null) && (lastResult.getFinishDate() != null);
	//finishedLockForMonitor is a lock for displaying results page for teacher only if user see it, and displaying learner page if user see it accordingly
	boolean finishedLockForMonitor =  (mode != null) && mode.isTeacher() && isLastResultFinished;	
	boolean finishedLock = assessmentUser.isSessionFinished() || finishedLockForMonitor || isLastResultFinished;

	// get notebook entry
	String entryText = new String();
	AssessmentUser notebookCreator = (groupLeader == null) ? assessmentUser : groupLeader;
	NotebookEntry notebookEntry = service.getEntry(toolSessionId, notebookCreator.getUserId().intValue());
	if (notebookEntry != null) {
	    entryText = notebookEntry.getEntry();
	}
	
	// basic information
	sessionMap.put(AssessmentConstants.ATTR_TITLE, assessment.getTitle());
	sessionMap.put(AssessmentConstants.ATTR_INSTRUCTIONS, assessment.getInstructions());
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, isResubmitAllowed);
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, finishedLock);
	sessionMap.put(AssessmentConstants.ATTR_USER_FINISHED, assessmentUser.isSessionFinished());
	sessionMap.put(AttributeNames.ATTR_LEARNER_CONTENT_FOLDER,
		service.getLearnerContentFolder(toolSessionId, assessmentUser.getUserId()));
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AssessmentConstants.ATTR_USER, assessmentUser);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ON, assessment.isReflectOnActivity());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_INSTRUCTION, assessment.getReflectInstructions());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ENTRY, entryText);

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionId, request, getServlet()
		.getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);
	
	// add define later support
	if (assessment.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	assessment.setContentInUse(true);
	assessment.setDefineLater(false);
	service.saveOrUpdateAssessment(assessment);

	//check if there is submission deadline
	Date submissionDeadline = assessment.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    //store submission deadline to sessionMap
	    sessionMap.put(AssessmentConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);
	   
	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    
	    //calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}
	
	//sort questions
	LinkedList<AssessmentQuestion> questionList = new LinkedList<AssessmentQuestion>();
	for (QuestionReference questionReference : questionReferences) {
	    AssessmentQuestion question = questionToReferenceMap.get(questionReference.getUid());
	    // becuase in webpage will use this login name. Here is just initialize it to avoid session close error in proxy object.
	    if (question.getCreateBy() != null) {
		question.getCreateBy().getLoginName();
	    }
	    question.setGrade(questionReference.getDefaultGrade());
	    
	    questionList.add(question);
	}

	// shuffling
	if (assessment.isShuffled()) {
	    ArrayList<AssessmentQuestion> shuffledList = new ArrayList<AssessmentQuestion>(questionList);
	    Collections.shuffle(shuffledList);
	    questionList = new LinkedList<AssessmentQuestion>(shuffledList);
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
	if (!finishedLock && hasEditRight) {
	    service.setAttemptStarted(assessment, assessmentUser, toolSessionId);
	}
	
	//paging
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = new ArrayList<LinkedHashSet<AssessmentQuestion>>();
	int maxQuestionsPerPage = ((assessment.getQuestionsPerPage() != 0) && hasEditRight) ? assessment.getQuestionsPerPage()
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
	loadupLastAttempt(sessionMap);
	
	//check if need to display results page
	if ((dbResultCount > 0) && finishedLock) {
	    // display results page
	    prepareResultsPageData(sessionMap);
	}

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }
    
    /**
     * Checks Leader Progress
     */
    private ActionForward checkLeaderProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	
	IAssessmentService service = getAssessmentService();
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	AssessmentSession session = service.getAssessmentSessionBySessionId(toolSessionId);
	AssessmentUser leader = session.getGroupLeader();
	
	boolean isLeaderResponseFinalized = leader.isSessionFinished();
	
	JSONObject JSONObject = new JSONObject();
	JSONObject.put("isLeaderResponseFinalized", isLeaderResponseFinalized);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    
    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    private ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	
	boolean finishedLock = (Boolean) sessionMap.get(AssessmentConstants.ATTR_FINISHED_LOCK);
	if (! finishedLock) {
	    //get user answers from request and store them into sessionMap
	    storeUserAnswersIntoSessionMap(request);
	    // store results from sessionMap into DB
	    storeUserAnswersIntoDatabase(sessionMap, true);
		
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
     */
    private ActionForward submitAll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	//get user answers from request and store them into sessionMap
	storeUserAnswersIntoSessionMap(request);
	//store results from sessionMap into DB
	storeUserAnswersIntoDatabase(sessionMap, false);
	
	// populate info for displaying results page
	prepareResultsPageData(sessionMap);
	
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	//calculate whether isResubmitAllowed
	IAssessmentService service = getAssessmentService();
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(userDTO.getUserID().longValue());
	int dbResultCount = service.getAssessmentResultCount(assessment.getUid(), userID);
	int attemptsAllowed = assessment.getAttemptsAllowed();
	boolean isResubmitAllowed = ((attemptsAllowed > dbResultCount) | (attemptsAllowed == 0));
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, isResubmitAllowed);
	
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, true);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	
	return mapping.findForward(AssessmentConstants.SUCCESS);
    } 
    
    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    private ActionForward resubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	AssessmentUser assessmentUser = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	IAssessmentService service = getAssessmentService();
	service.setAttemptStarted(assessment, assessmentUser, toolSessionId);
	
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, false);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_NUMBERING_OFFSET, 1);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	//clear isUserFailed indicator
	sessionMap.put(AssessmentConstants.ATTR_IS_USER_FAILED, false);
	
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Finish learning session.
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	IAssessmentService service = getAssessmentService();
	
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());
	    Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(AssessmentConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (AssessmentApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }
    
    /**
     * Move up current option.
     */
    private ActionForward upOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchOption(mapping, request, true);
    }

    /**
     * Move down current option.
     */
    private ActionForward downOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchOption(mapping, request, false);
    }

    private ActionForward switchOption(ActionMapping mapping, HttpServletRequest request, boolean up) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
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
    
    /**
     * auto saves responses 
     */
    private ActionForward autoSaveAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IAssessmentService service = getAssessmentService();
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	
	//get user answers from request and store them into sessionMap
	storeUserAnswersIntoSessionMap(request);
	//store results from sessionMap into DB
	storeUserAnswersIntoDatabase(sessionMap, true);
	
	return null;
    }
    
    /**
     * Display empty reflection form.
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IAssessmentService service = getAssessmentService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = service.getEntry(toolSessionID, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database.
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IAssessmentService service = getAssessmentService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    
    /**
     * Get back user answers from request and store it into sessionMap.
     * 
     * @param request
     */
    private void storeUserAnswersIntoSessionMap(HttpServletRequest request){
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
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
	    
	    // in case learner goes to the next page and refreshes it right after this. And thus it's not possible to know
	    // previous page number in this case. but anyway no need to save answers
	    if (question == null) {
		break;
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
		    question.setAnswerString("answered");
		}
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ESSAY) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		answerString = answerString.replaceAll("[\n\r\f]", "");
		question.setAnswerString(answerString);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    }
	}
    }
    
    /**
     * Prepare data for displaying results page
     */
    private void prepareResultsPageData(SessionMap<String, Object> sessionMap){
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	AssessmentResult result = service.getLastFinishedAssessmentResult(assessment.getUid(), userId);
	
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

			List<Object[]> questionResults = service.getAssessmentQuestionResultList(assessment.getUid(),
				userId, question.getUid());
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
	
	//calculate whether user has failed this attempt
	int passingMark = assessment.getPassingMark();
	boolean isUserFailed = ((passingMark != 0) && (passingMark > result.getGrade()));
	sessionMap.put(AssessmentConstants.ATTR_IS_USER_FAILED, isUserFailed);
	
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT_RESULT, result);
    }    
    
    private void loadupLastAttempt(SessionMap<String, Object> sessionMap){
	IAssessmentService service = getAssessmentService();
	
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	//get the latest result (it can be unfinished one)
	AssessmentResult lastResult = service.getLastAssessmentResult(assessmentUid,userId);
	//if there is no results yet - no action required
	if (lastResult == null) {
	    return;
	}
	
	for(LinkedHashSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		for (AssessmentQuestionResult questionResult : lastResult.getQuestionResults()) {
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
				    if (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
					questionOption.setSequenceId(optionAnswer.getAnswerInt());
				    } else {
					questionOption.setAnswerInt(optionAnswer.getAnswerInt());
				    }
				    
				    break;
				}
			    }			    
			}
			break;
		    }
		}
		
		//sort ordering type of question
		if (question.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		    TreeSet<AssessmentQuestionOption> orderedSet = new TreeSet<AssessmentQuestionOption>(new SequencableComparator());
		    orderedSet.addAll(question.getQuestionOptions());
		    question.setQuestionOptions(orderedSet);
		}
	    }
	}
    }
    
    /**
     * Store user answers in DB in last unfinished attempt and notify teachers about it.
     */
    private void storeUserAnswersIntoDatabase(SessionMap<String, Object> sessionMap, boolean isAutosave) {
	ArrayList<LinkedHashSet<AssessmentQuestion>> pagedQuestions = (ArrayList<LinkedHashSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	service.storeUserAnswers(assessmentUid, userId, pagedQuestions, isAutosave);
	
	// notify teachers
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	if ((mode != null) && !mode.isTeacher() && !isAutosave) {
	    
	    Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	    if (assessment.isNotifyTeachersOnAttemptCompletion()) {
		
		Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		final boolean isHtmlFormat = false;
		List<User> monitoringUsers = service.getMonitorsByToolSessionId(toolSessionId);
		if (monitoringUsers != null && !monitoringUsers.isEmpty()) {
		    Integer[] monitoringUsersIds = new Integer[monitoringUsers.size()];
		    for (int i = 0; i < monitoringUsersIds.length; i++) {
			monitoringUsersIds[i] = monitoringUsers.get(i).getUserId();
		    }
		    AssessmentUser assessmentUser = getCurrentUser(service, toolSessionId);
		    String fullName = assessmentUser.getLastName() + " " + assessmentUser.getFirstName();
		    service.getEventNotificationService().sendMessage(null, monitoringUsersIds,
			    IEventNotificationService.DELIVERY_METHOD_MAIL,
			    service.getLocalisedMessage("event.learner.completes.attempt.subject", null),
			    service.getLocalisedMessage("event.learner.completes.attempt.body", new Object[] { fullName }),
			    isHtmlFormat);
		}
		
	    }
	}
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
