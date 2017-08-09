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

package org.lamsfoundation.lams.tool.assessment.web.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.OptionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOverallFeedback;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AnswerIntComparator;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, IllegalAccessException,
	    InstantiationException, InvocationTargetException, NoSuchMethodException {

	String param = mapping.getParameter();
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("nextPage")) {
	    return nextPage(mapping, form, request, response);
	}
	if (param.equals("submitSingleMarkHedgingQuestion")) {
	    return submitSingleMarkHedgingQuestion(mapping, form, request, response);
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
	if (param.equals("launchTimeLimit")) {
	    return launchTimeLimit(mapping, form, request, response);
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
	    HttpServletResponse response) throws ServletException, IllegalAccessException, InstantiationException,
	    InvocationTargetException, NoSuchMethodException {

	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long toolSessionId = new Long(request.getParameter(AssessmentConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	// get back the assessment and question list and display them on page
	IAssessmentService service = getAssessmentService();
	AssessmentUser user = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // assessmentUser may be null if the user was force completed.
	    user = getSpecifiedUser(service, toolSessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionId);
	}

	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);

	// support for leader select feature
	AssessmentUser groupLeader = assessment.isUseSelectLeaderToolOuput()
		? service.checkLeaderSelectToolForSessionLeader(user, new Long(toolSessionId).longValue())
		: null;
	if (assessment.isUseSelectLeaderToolOuput() && !mode.isTeacher()) {

	    // forwards to the leaderSelection page
	    if (groupLeader == null) {
		List<AssessmentUser> groupUsers = service.getUsersBySession(new Long(toolSessionId).longValue());
		request.setAttribute(AssessmentConstants.ATTR_GROUP_USERS, groupUsers);
		request.setAttribute(AssessmentConstants.ATTR_ASSESSMENT, assessment);

		return mapping.findForward(AssessmentConstants.WAIT_FOR_LEADER);
	    }

	    // forwards to the waitForLeader pages
	    boolean isNonLeader = !user.getUserId().equals(groupLeader.getUserId());
	    if (assessment.getTimeLimit() != 0 && isNonLeader && !user.isSessionFinished()) {
		AssessmentResult lastLeaderResult = service.getLastAssessmentResult(assessment.getUid(),
			groupLeader.getUserId());

		//show waitForLeaderLaunchTimeLimit page if the leader hasn't started activity or hasn't pressed OK button to launch time limit
		if (lastLeaderResult == null || lastLeaderResult.getTimeLimitLaunchedDate() == null) {
		    request.setAttribute(AssessmentConstants.PARAM_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.launch.time.limit");
		    return mapping.findForward(AssessmentConstants.WAIT_FOR_LEADER_TIME_LIMIT);
		}

		//if the time is up and leader hasn't submitted response - show waitForLeaderFinish page
		boolean isTimeLimitExceeded = service.checkTimeLimitExceeded(assessment, groupLeader);
		if (isTimeLimitExceeded && !groupLeader.isSessionFinished()) {
		    request.setAttribute(AssessmentConstants.PARAM_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.finish");
		    return mapping.findForward(AssessmentConstants.WAIT_FOR_LEADER_TIME_LIMIT);
		}
	    }

	    // check if leader has submitted all answers
	    if (groupLeader.isSessionFinished()) {

		// in case user joins the lesson after leader has answers some answers already - we need to make sure
		// he has the same scratches as leader
		service.copyAnswersFromLeader(user, groupLeader);
	    }
	}

	sessionMap.put(AssessmentConstants.ATTR_GROUP_LEADER, groupLeader);
	boolean isUserLeader = service.isUserGroupLeader(user, new Long(toolSessionId));
	sessionMap.put(AssessmentConstants.ATTR_IS_USER_LEADER, isUserLeader);

	Set<QuestionReference> questionReferences = new TreeSet<>(new SequencableComparator());
	questionReferences.addAll(assessment.getQuestionReferences());
	HashMap<Long, AssessmentQuestion> questionToReferenceMap = new HashMap<>();
	ArrayList<AssessmentQuestion> takenQuestion = new ArrayList<>();

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
		AssessmentQuestion question = (AssessmentQuestion) availableQuestions.toArray()[rand
			.nextInt(availableQuestions.size())];
		takenQuestion.add(question);
		questionToReferenceMap.put(questionReference.getUid(), question);
		availableQuestions.remove(question);
	    }
	}

	AssessmentResult lastResult = service.getLastAssessmentResult(assessment.getUid(), user.getUserId());
	boolean hasEditRight = !assessment.isUseSelectLeaderToolOuput()
		|| assessment.isUseSelectLeaderToolOuput() && isUserLeader;
	//showResults if either session or the last result is finished
	boolean showResults = user.isSessionFinished() || (lastResult != null) && (lastResult.getFinishDate() != null);

	// get notebook entry
	String entryText = new String();
	AssessmentUser notebookCreator = (groupLeader == null) ? user : groupLeader;
	NotebookEntry notebookEntry = service.getEntry(toolSessionId, notebookCreator.getUserId().intValue());
	if (notebookEntry != null) {
	    entryText = notebookEntry.getEntry();
	}

	// basic information
	sessionMap.put(AssessmentConstants.ATTR_TITLE, assessment.getTitle());
	sessionMap.put(AssessmentConstants.ATTR_INSTRUCTIONS, assessment.getInstructions());
	sessionMap.put(AssessmentConstants.ATTR_SHOW_RESULTS, showResults);
	sessionMap.put(AssessmentConstants.ATTR_HAS_EDIT_RIGHT, hasEditRight);
	sessionMap.put(AssessmentConstants.ATTR_USER_FINISHED, user.isSessionFinished());
	sessionMap.put(AttributeNames.ATTR_LEARNER_CONTENT_FOLDER,
		service.getLearnerContentFolder(toolSessionId, user.getUserId()));
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AssessmentConstants.ATTR_USER, user);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ON, assessment.isReflectOnActivity());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_INSTRUCTION, assessment.getReflectInstructions());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ENTRY, entryText);

	//time limit
	boolean isTimeLimitEnabled = hasEditRight && !showResults && assessment.getTimeLimit() != 0;
	long secondsLeft = isTimeLimitEnabled ? service.getSecondsLeft(assessment, user) : 0;
	request.setAttribute(AssessmentConstants.ATTR_SECONDS_LEFT, secondsLeft);
	boolean isTimeLimitNotLaunched = (lastResult == null) || (lastResult.getTimeLimitLaunchedDate() == null);
	sessionMap.put(AssessmentConstants.ATTR_IS_TIME_LIMIT_NOT_LAUNCHED, isTimeLimitNotLaunched);

	ActivityPositionDTO activityPosition = LearningWebUtil
		.putActivityPositionInRequestByToolSessionId(toolSessionId, request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// add define later support
	if (assessment.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

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
	LinkedList<QuestionDTO> questionDtos = new LinkedList<>();
	for (QuestionReference questionReference : questionReferences) {
	    AssessmentQuestion question = questionToReferenceMap.get(questionReference.getUid());

	    QuestionDTO questionDto = question.getQuestionDTO();
	    questionDto.setGrade(questionReference.getDefaultGrade());

	    questionDtos.add(questionDto);
	}

	// shuffling
	if (assessment.isShuffled()) {
	    ArrayList<QuestionDTO> shuffledList = new ArrayList<>(questionDtos);
	    Collections.shuffle(shuffledList);
	    questionDtos = new LinkedList<>(shuffledList);
	}
	for (QuestionDTO questionDto : questionDtos) {
	    if (questionDto.isShuffle() || (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING)) {
		ArrayList<OptionDTO> shuffledList = new ArrayList<>(questionDto.getOptionDtos());
		Collections.shuffle(shuffledList);
		questionDto.setOptionDtos(new LinkedHashSet<>(shuffledList));
	    }
	    if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		//sort answer options alphanumerically (as per LDEV-4326)
		ArrayList<OptionDTO> optionsSortedByOptionString = new ArrayList<>(
			questionDto.getOptionDtos());
		optionsSortedByOptionString.sort(new Comparator<OptionDTO>() {
		    @Override
		    public int compare(OptionDTO o1, OptionDTO o2) {
			String optionString1 = o1.getOptionString() != null ? o1.getOptionString() : "";
			String optionString2 = o2.getOptionString() != null ? o2.getOptionString() : "";

			return AlphanumComparator.compareAlphnumerically(optionString1, optionString2);
		    }
		});
		questionDto.setMatchingPairOptions(new LinkedHashSet<>(optionsSortedByOptionString));
	    }
	}

	//paging
	List<Set<QuestionDTO>> pagedQuestionDtos = new ArrayList<>();
	int maxQuestionsPerPage = ((assessment.getQuestionsPerPage() != 0) && hasEditRight)
		? assessment.getQuestionsPerPage()
		: questionDtos.size();
	LinkedHashSet<QuestionDTO> questionsForOnePage = new LinkedHashSet<>();
	pagedQuestionDtos.add(questionsForOnePage);
	int count = 0;
	for (QuestionDTO questionDto : questionDtos) {
	    questionsForOnePage.add(questionDto);
	    count++;
	    if ((questionsForOnePage.size() == maxQuestionsPerPage) && (count != questionDtos.size())) {
		questionsForOnePage = new LinkedHashSet<>();
		pagedQuestionDtos.add(questionsForOnePage);
	    }
	}

	sessionMap.put(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS, pagedQuestionDtos);
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_NUMBERING_OFFSET, 1);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, assessment);

	// loadupLastAttempt for display purpose
	loadupLastAttempt(sessionMap);

	if (showResults) {

	    // display results page
	    showResults(mapping, sessionMap);
	    return mapping.findForward(AssessmentConstants.SHOW_RESULTS);

	} else {
	    // set attempt started
	    if (hasEditRight) {
		service.setAttemptStarted(assessment, user, toolSessionId);
	    }

	    return mapping.findForward(AssessmentConstants.LEARNING);
	}
    }

    /**
     * Checks Leader Progress
     */
    private ActionForward checkLeaderProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	IAssessmentService service = getAssessmentService();
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	AssessmentSession session = service.getAssessmentSessionBySessionId(toolSessionId);
	AssessmentUser leader = session.getGroupLeader();

	//in case of time limit - prevent user from seeing questions page longer than time limit allows
	boolean isTimeLimitExceeded = service.checkTimeLimitExceeded(session.getAssessment(), leader);
	boolean isLeaderResponseFinalized = leader.isSessionFinished();

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put("isPageRefreshRequested", isLeaderResponseFinalized || isTimeLimitExceeded);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(ObjectNode);
	return null;
    }

    /**
     * Shows next page. It's available only to leaders as non-leaders see all questions on one page.
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	return nextPage(mapping, request, false, -1);
    }

    /**
     * Auxiliary method to be called by nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
     * HttpServletResponse response) or submitAll.
     *
     * @param mapping
     * @param request
     * @param isAnswersValidationFailed
     *            submitAll() method may set it as true in case some of the pages miss required answers
     * @param pageNumberWithUnasweredQuestions
     *            page number with questions required to be answered
     * @return
     */
    private ActionForward nextPage(ActionMapping mapping, HttpServletRequest request, boolean isAnswersValidationFailed,
	    int pageNumberWithUnasweredQuestions)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	IAssessmentService service = getAssessmentService();
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	AssessmentUser user = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	int oldPageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);

	//if AnswersValidationFailed - get pageNumber as request parameter and as method parameter otherwise
	int pageNumberToOpen;
	if (isAnswersValidationFailed) {
	    pageNumberToOpen = pageNumberWithUnasweredQuestions;
	} else {
	    pageNumberToOpen = WebUtil.readIntParam(request, AssessmentConstants.ATTR_PAGE_NUMBER);
	}

	int questionNumberingOffset = 0;
	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	for (int i = 0; i < pageNumberToOpen - 1; i++) {
	    Set<QuestionDTO> questionsForOnePage = pagedQuestionDtos.get(i);
	    questionNumberingOffset += questionsForOnePage.size();
	}
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_NUMBERING_OFFSET, ++questionNumberingOffset);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, pageNumberToOpen);

	boolean showResults = (Boolean) sessionMap.get(AssessmentConstants.ATTR_SHOW_RESULTS);
	if (showResults) {
	    request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	    return mapping.findForward(AssessmentConstants.SHOW_RESULTS);

	} else {
	    //get user answers from request and store them into sessionMap
	    storeUserAnswersIntoSessionMap(request, oldPageNumber);
	    // store results from sessionMap into DB
	    storeUserAnswersIntoDatabase(sessionMap, true);

	    long secondsLeft = service.getSecondsLeft(assessment, user);
	    request.setAttribute(AssessmentConstants.ATTR_SECONDS_LEFT, secondsLeft);

	    // use redirect to prevent form resubmission
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(AssessmentConstants.LEARNING));
	    redirect.addParameter(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	    redirect.addParameter(AssessmentConstants.ATTR_IS_ANSWERS_VALIDATION_FAILED, isAnswersValidationFailed);
	    return redirect;
	}
    }

    /**
     * Handling submittion of MarkHedging type of Questions (in case of leader aware tool).
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private ActionForward submitSingleMarkHedgingQuestion(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	IAssessmentService service = getAssessmentService();
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);
	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);

	//get user answers from request and store them into sessionMap
	storeUserAnswersIntoSessionMap(request, pageNumber);

	// store results from sessionMap into DB
	Long singleMarkHedgingQuestionUid = WebUtil.readLongParam(request, "singleMarkHedgingQuestionUid");
	boolean isResultsStored = service.storeUserAnswers(assessment, userId, pagedQuestionDtos,
		singleMarkHedgingQuestionUid, false);
	// result was not stored in case user was prohibited from submitting (or autosubmitting) answers (e.g. when
	// using 2 browsers). Then show last stored results
	if (!isResultsStored) {
	    //loadupLastAttempt(sessionMap);
	}

	//find according question in order to get its mark
	QuestionDTO questionDto = null;
	for (Set<QuestionDTO> questionsForOnePage : pagedQuestionDtos) {
	    for (QuestionDTO questionDtoIter : questionsForOnePage) {
		if (questionDtoIter.getUid().equals(singleMarkHedgingQuestionUid)) {
		    questionDto = questionDtoIter;
		    questionDto.setResponseSubmitted(true);
		}
	    }
	}

	// populate info for displaying results page
	//prepareResultsPageData(sessionMap);

	request.setAttribute("assessment", assessment);
	request.setAttribute("question", questionDto);
	long questionIndex = WebUtil.readLongParam(request, "questionIndex");
	request.setAttribute("questionIndex", questionIndex);
	request.setAttribute("isLeadershipEnabled", assessment.isUseSelectLeaderToolOuput());
	request.setAttribute("isUserLeader", sessionMap.get(AssessmentConstants.ATTR_IS_USER_LEADER));

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private ActionForward submitAll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);

	//get user answers from request and store them into sessionMap
	storeUserAnswersIntoSessionMap(request, pageNumber);

	boolean isTimelimitExpired = WebUtil.readBooleanParam(request, "isTimelimitExpired", false);
	if (!isTimelimitExpired) {

	    // check all required questions got answered
	    int pageNumberWithUnasweredQuestions = validateAnswers(sessionMap);
	    // if some were not then forward to nextPage()
	    if (pageNumberWithUnasweredQuestions != 0) {
		return nextPage(mapping, request, true, pageNumberWithUnasweredQuestions);
	    }
	}

	//store results from sessionMap into DB
	boolean isResultsStored = storeUserAnswersIntoDatabase(sessionMap, false);
	// result was not stored in case user was prohibited from submitting (or autosubmitting) answers (e.g. when
	// using 2 browsers). Then show last stored results
	if (!isResultsStored) {
	    loadupLastAttempt(sessionMap);
	}

	sessionMap.put(AssessmentConstants.ATTR_SHOW_RESULTS, true);

	// populate info for displaying results page
	showResults(mapping, sessionMap);

	//use redirect to prevent form resubmission
	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig(AssessmentConstants.SHOW_RESULTS));
	redirect.addParameter(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return redirect;
    }

    /**
     * User pressed Resubmit button.
     */
    private ActionForward resubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	IAssessmentService service = getAssessmentService();

	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);
	AssessmentUser assessmentUser = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	Long userId = assessmentUser.getUserId();
	service.unsetSessionFinished(toolSessionId, userId);

	Date lastAttemptStartingDate = service.getLastAssessmentResult(assessment.getUid(), userId).getStartDate();

	// set attempt started: create a new one + mark previous as not being the latest any longer
	service.setAttemptStarted(assessment, assessmentUser, toolSessionId);

	// in case of content was modified in monitor - redirect to start.do in order to refresh info from the DB
	if (assessment.isContentModifiedInMonitor(lastAttemptStartingDate)) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("learningStartMethod"));
	    redirect.addParameter(AttributeNames.PARAM_MODE, mode.toString());
	    redirect.addParameter(AssessmentConstants.PARAM_TOOL_SESSION_ID, toolSessionId);
	    return redirect;

	    //otherwise use data from SessionMap
	} else {

	    sessionMap.put(AssessmentConstants.ATTR_SHOW_RESULTS, false);
	    sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
	    sessionMap.put(AssessmentConstants.ATTR_QUESTION_NUMBERING_OFFSET, 1);
	    request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	    // clear isUserFailed indicator
	    sessionMap.put(AssessmentConstants.ATTR_IS_USER_FAILED, false);

	    // time limit feature
	    sessionMap.put(AssessmentConstants.ATTR_IS_TIME_LIMIT_NOT_LAUNCHED, true);
	    request.setAttribute(AssessmentConstants.ATTR_SECONDS_LEFT, assessment.getTimeLimit() * 60);

	    return mapping.findForward(AssessmentConstants.LEARNING);
	}

    }

    /**
     * Finish learning session.
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
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
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);
	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	Set<QuestionDTO> questionsForOnePage = pagedQuestionDtos.get(pageNumber - 1);
	Long questionUid = new Long(request.getParameter(AssessmentConstants.PARAM_QUESTION_UID));

	QuestionDTO questionDto = null;
	for (QuestionDTO questionDtoIter : questionsForOnePage) {
	    if (questionDtoIter.getUid().equals(questionUid)) {
		questionDto = questionDtoIter;
		break;
	    }
	}

	Set<OptionDTO> optionDtoList = questionDto.getOptionDtos();

	int optionIndex = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<OptionDTO> rList = new ArrayList<>(optionDtoList);

	    // get current and the target item, and switch their sequnece
	    OptionDTO option = rList.remove(optionIndex);
	    if (up) {
		rList.add(--optionIndex, option);
	    } else {
		rList.add(++optionIndex, option);
	    }

	    // put back list
	    optionDtoList = new LinkedHashSet<>(rList);
	    questionDto.setOptionDtos(optionDtoList);
	}

	request.setAttribute(AssessmentConstants.ATTR_QUESTION_FOR_ORDERING, questionDto);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * auto saves responses
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private ActionForward autoSaveAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	IAssessmentService service = getAssessmentService();
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);

	//get user answers from request and store them into sessionMap
	storeUserAnswersIntoSessionMap(request, pageNumber);
	//store results from sessionMap into DB
	storeUserAnswersIntoDatabase(sessionMap, true);

	return null;
    }

    /**
     * Stores date when user has started activity with time limit
     */
    private ActionForward launchTimeLimit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IAssessmentService service = getAssessmentService();
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	sessionMap.put(AssessmentConstants.ATTR_IS_TIME_LIMIT_NOT_LAUNCHED, false);

	service.launchTimeLimit(assessmentUid, userId);

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
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
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
     * @param pageNumber
     *            number of the page to process
     */
    private void storeUserAnswersIntoSessionMap(HttpServletRequest request, int pageNumber) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	Set<QuestionDTO> questionsForOnePage = pagedQuestionDtos.get(pageNumber - 1);

	for (int i = 0; i < questionsForOnePage.size(); i++) {
	    Long questionUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_UID + i);
	    QuestionDTO questionDto = null;
	    for (QuestionDTO sessionQuestion : questionsForOnePage) {
		if (sessionQuestion.getUid().equals(questionUid)) {
		    questionDto = sessionQuestion;
		    break;
		}
	    }

	    // in case learner goes to the next page and refreshes it right after this. And thus it's not possible to know
	    // previous page number in this case. but anyway no need to save answers
	    if (questionDto == null) {
		break;
	    }

	    int questionType = questionDto.getType();
	    if (questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    boolean answerBoolean = false;
		    if (questionDto.isMultipleAnswersAllowed()) {
			String answerString = request.getParameter(
				AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + optionDto.getSequenceId());
			answerBoolean = !StringUtils.isBlank(answerString);
		    } else {
			String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
			if (answerString != null) {
			    int optionSequenceId = Integer.parseInt(answerString);
			    answerBoolean = (optionDto.getSequenceId() == optionSequenceId);
			}
		    }
		    optionDto.setAnswerBoolean(answerBoolean);
		}

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    int answerInt = WebUtil.readIntParam(request,
			    AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + optionDto.getSequenceId());
		    optionDto.setAnswerInt(answerInt);
		}

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		questionDto.setAnswerString(answerString);

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		questionDto.setAnswerString(answerString);

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		if (answerString != null) {
		    questionDto.setAnswerBoolean(Boolean.parseBoolean(answerString));
		    questionDto.setAnswerString("answered");
		}

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ESSAY) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		answerString = answerString.replaceAll("[\n\r\f]", "");
		questionDto.setAnswerString(answerString);

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {

		//store hedging marks
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    Integer markHedging = WebUtil.readIntParam(request,
			    AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + optionDto.getSequenceId(), true);
		    if (markHedging != null) {
			optionDto.setAnswerInt(markHedging);
		    }
		}

		//store justification of hedging if enabled
		if (questionDto.isHedgingJustificationEnabled()) {
		    String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		    answerString = answerString.replaceAll("[\n\r\f]", "");
		    questionDto.setAnswerString(answerString);
		}
	    }
	}
    }

    /**
     * Checks whether all required questions were answered and all essay question with min words limit have fullfilled
     * that.
     *
     * @param sessionMap
     * @return 0 if all questions were answered OK, or a number of the first page that contains questions with issues
     */
    private int validateAnswers(SessionMap<String, Object> sessionMap) {

	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);

	//array of missing required questions
	boolean isAllQuestionsAnswered = true;
	boolean isAllQuestionsReachedMinWordsLimit = true;

	//iterate through all pages to find first that contains missing required questions
	int pageCount;
	for (pageCount = 0; pageCount < pagedQuestionDtos.size(); pageCount++) {
	    Set<QuestionDTO> questionsForOnePage = pagedQuestionDtos.get(pageCount);

	    for (QuestionDTO questionDto : questionsForOnePage) {
		int questionType = questionDto.getType();

		//enforce all hedging marks question type to be answered as well
		if (questionDto.isAnswerRequired()
			|| (questionType == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING)) {

		    boolean isAnswered = false;

		    if (questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {

			for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			    isAnswered |= optionDto.getAnswerBoolean();
			}

		    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
			for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			    isAnswered |= optionDto.getAnswerInt() != 0;
			}

		    } else if ((questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)
			    || (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL)
			    || (questionType == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE)
			    || (questionType == AssessmentConstants.QUESTION_TYPE_ESSAY)) {
			isAnswered |= StringUtils.isNotBlank(questionDto.getAnswerString());

		    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {
			isAnswered = true;

		    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {

			//verify sum of all hedging marks is equal to question's grade
			int sumMarkHedging = 0;
			for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			    sumMarkHedging += optionDto.getAnswerInt();
			}
			isAnswered = sumMarkHedging == questionDto.getGrade();

			//verify justification of hedging is provided if it was enabled
			if (questionDto.isHedgingJustificationEnabled()) {
			    isAnswered &= StringUtils.isNotBlank(questionDto.getAnswerString());
			}
		    }

		    // check all questions were answered
		    if (!isAnswered) {
			isAllQuestionsAnswered = false;
			break;
		    }

		}

		if ((questionDto.getType() == AssessmentConstants.QUESTION_TYPE_ESSAY)
			&& (questionDto.getMinWordsLimit() > 0)) {

		    String answer = new String(questionDto.getAnswerString());

		    boolean isMinWordsLimitReached = ValidationUtil.isMinWordsLimitReached(answer,
			    questionDto.getMinWordsLimit(), questionDto.isAllowRichEditor());

		    // check min words limit is reached
		    if (!isMinWordsLimitReached) {
			isAllQuestionsReachedMinWordsLimit = false;
			break;
		    }

		}
	    }

	    //if found answers required to be fixed by learners, stop here
	    if (!isAllQuestionsAnswered || !isAllQuestionsReachedMinWordsLimit) {
		return pageCount + 1;
	    }
	}

	return 0;
    }

    /**
     * Prepare data for displaying results page
     */
    private void showResults(ActionMapping mapping, SessionMap<String, Object> sessionMap) {
	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();

	int dbResultCount = service.getAssessmentResultCount(assessment.getUid(), userId);
	if (dbResultCount > 0) {

	    // release object from the cache (it's required when we have modified result object in the same request)
	    AssessmentResult result = service.getLastFinishedAssessmentResultNotFromChache(assessment.getUid(), userId);

	    for (Set<QuestionDTO> questionsForOnePage : pagedQuestionDtos) {
		for (QuestionDTO questionDto : questionsForOnePage) {

		    // find corresponding questionResult
		    for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
			if (questionDto.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {

			    // copy questionResult's info to the question
			    questionDto.setMark(questionResult.getMark());
			    questionDto.setResponseSubmitted(questionResult.getFinishDate() != null);
			    questionDto.setPenalty(questionResult.getPenalty());
			    questionDto.setQuestionFeedback(null);
			    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
				if (optionDto.getUid().equals(questionResult.getSubmittedOptionUid())) {
				    questionDto.setQuestionFeedback(optionDto.getFeedback());
				    break;
				}
			    }
			    // required for showing right/wrong answers icons on results page correctly
			    if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER
				    || questionDto.getType() == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
				boolean isAnsweredCorrectly = false;
				for (OptionDTO optionDto : questionDto.getOptionDtos()) {
				    if (optionDto.getUid().equals(questionResult.getSubmittedOptionUid())) {
					isAnsweredCorrectly = optionDto.getGrade() > 0;
					break;
				    }
				}
				questionDto.setAnswerBoolean(isAnsweredCorrectly);
			    }

			    // required for markandpenalty area and if it's on - on question's summary page
			    List<Object[]> questionResults = service
				    .getAssessmentQuestionResultList(assessment.getUid(), userId, questionDto.getUid());
			    questionDto.setQuestionResults(questionResults);
			}
		    }
		}
	    }

	    Date timeTaken = new Date(result.getFinishDate().getTime() - result.getStartDate().getTime());
	    result.setTimeTaken(timeTaken);
	    if (assessment.isAllowOverallFeedbackAfterQuestion()) {
		int percentageCorrectAnswers = (int) (result.getGrade() * 100 / result.getMaximumGrade());
		ArrayList<AssessmentOverallFeedback> overallFeedbacks = new ArrayList<AssessmentOverallFeedback>(
			assessment.getOverallFeedbacks());
		int lastBorder = 0;
		for (int i = overallFeedbacks.size() - 1; i >= 0; i--) {
		    AssessmentOverallFeedback overallFeedback = overallFeedbacks.get(i);
		    if ((percentageCorrectAnswers >= lastBorder)
			    && (percentageCorrectAnswers <= overallFeedback.getGradeBoundary())) {
			result.setOverallFeedback(overallFeedback.getFeedback());
			break;
		    }
		    lastBorder = overallFeedback.getGradeBoundary();
		}
	    }

	    // calculate whether user has failed this attempt
	    int passingMark = assessment.getPassingMark();
	    double gradeRoundedTo2DecimalPlaces = Math.round(result.getGrade() * 100.0) / 100.0;
	    boolean isUserFailed = ((passingMark != 0) && (passingMark > gradeRoundedTo2DecimalPlaces));
	    sessionMap.put(AssessmentConstants.ATTR_IS_USER_FAILED, isUserFailed);

	    sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT_RESULT, result);
	}

	//calculate whether isResubmitAllowed
	int attemptsAllowed = assessment.getAttemptsAllowed();
	boolean isResubmitAllowed = ((attemptsAllowed > dbResultCount) | (attemptsAllowed == 0));
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, isResubmitAllowed);
    }

    private void loadupLastAttempt(SessionMap<String, Object> sessionMap) {
	IAssessmentService service = getAssessmentService();

	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	//get the latest result (it can be unfinished one)
	AssessmentResult lastResult = service.getLastAssessmentResult(assessmentUid, userId);
	//if there is no results yet - no action required
	if (lastResult == null) {
	    return;
	}

	//get the latest finished result (required for mark hedging type of questions only)
	AssessmentResult lastFinishedResult = null;
	if (lastResult.getFinishDate() == null) {
	    lastFinishedResult = service.getLastFinishedAssessmentResult(assessmentUid, userId);
	}

	for (Set<QuestionDTO> questionsForOnePage : pagedQuestionDtos) {
	    for (QuestionDTO questionDto : questionsForOnePage) {

		//load last finished results for hedging type of questions (in order to prevent retry)
		Set<AssessmentQuestionResult> questionResults = lastResult.getQuestionResults();
		if ((questionDto.getType() == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING)
			&& (lastResult.getFinishDate() == null) && (lastFinishedResult != null)) {
		    questionResults = lastFinishedResult.getQuestionResults();
		}

		for (AssessmentQuestionResult questionResult : questionResults) {
		    if (questionDto.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
			questionDto.setAnswerBoolean(questionResult.getAnswerBoolean());
			questionDto.setAnswerFloat(questionResult.getAnswerFloat());
			questionDto.setAnswerString(questionResult.getAnswerString());
			questionDto.setMark(questionResult.getMark());
			questionDto.setResponseSubmitted(questionResult.getFinishDate() != null);
			questionDto.setPenalty(questionResult.getPenalty());

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
			if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_ORDERING) {

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

			// set answerTotalGrade to let jsp know whether the question was answered correctly/partly/incorrectly even if mark=0
			if (questionDto.getType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
			    float totalGrade = 0;
			    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
				if (optionDto.getAnswerBoolean()) {
				    totalGrade += optionDto.getGrade();
				}
			    }
			    questionDto.setAnswerTotalGrade(totalGrade);
			}

			break;
		    }
		}
	    }
	}
    }

    /**
     * Store user answers in DB in last unfinished attempt and notify teachers about it.
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private boolean storeUserAnswersIntoDatabase(SessionMap<String, Object> sessionMap, boolean isAutosave)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	IAssessmentService service = getAssessmentService();
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);

	boolean isResultsStored = service.storeUserAnswers(assessment, userId, pagedQuestionDtos, null, isAutosave);

	// notify teachers
	if ((mode != null) && !mode.isTeacher() && !isAutosave && isResultsStored
		&& assessment.isNotifyTeachersOnAttemptCompletion()) {
	    AssessmentUser assessmentUser = getCurrentUser(toolSessionId);
	    String fullName = assessmentUser.getLastName() + " " + assessmentUser.getFirstName();
	    service.notifyTeachersOnAttemptCompletion(toolSessionId, fullName);
	}

	return isResultsStored;
    }

    private IAssessmentService getAssessmentService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
    }

    private AssessmentUser getCurrentUser(Long sessionId) {
	IAssessmentService service = getAssessmentService();

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
	    LearningAction.log.error(
		    "Unable to find specified user for assessment activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return assessmentUser;
    }

}
