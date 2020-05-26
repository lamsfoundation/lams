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

package org.lamsfoundation.lams.tool.assessment.web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.OptionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOverallFeedback;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    @Qualifier("laasseAssessmentService")
    private IAssessmentService service;

    @Autowired
    private IRatingService ratingService;

    @Autowired
    private IUserManagementService userManagementService;

    /**
     * Read assessment data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page".
     */
    @RequestMapping("/start")
    public String start(HttpServletRequest request) throws ServletException, IllegalAccessException,
	    InstantiationException, InvocationTargetException, NoSuchMethodException {

	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	Long toolSessionId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_TOOL_SESSION_ID);

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	// get back the assessment and question list and display them on page
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
		? service.checkLeaderSelectToolForSessionLeader(user, toolSessionId)
		: null;
	if (assessment.isUseSelectLeaderToolOuput() && !mode.isTeacher()) {

	    // forwards to the leaderSelection page
	    if (groupLeader == null) {
		List<AssessmentUser> groupUsers = service.getUsersBySession(toolSessionId);
		request.setAttribute(AssessmentConstants.ATTR_GROUP_USERS, groupUsers);
		request.setAttribute(AssessmentConstants.ATTR_ASSESSMENT, assessment);

		return "pages/learning/waitforleader";
	    }

	    AssessmentResult lastLeaderResult = service.getLastAssessmentResult(assessment.getUid(),
		    groupLeader.getUserId());
	    boolean isLastAttemptFinishedByLeader = lastLeaderResult != null
		    && lastLeaderResult.getFinishDate() != null;

	    // forwards to the waitForLeader pages
	    boolean isNonLeader = !user.getUserId().equals(groupLeader.getUserId());
	    if (assessment.getTimeLimit() != 0 && isNonLeader && !isLastAttemptFinishedByLeader) {

		//show waitForLeaderLaunchTimeLimit page if the leader hasn't started activity or hasn't pressed OK button to launch time limit
		if (lastLeaderResult == null || lastLeaderResult.getTimeLimitLaunchedDate() == null) {
		    request.setAttribute(AssessmentConstants.PARAM_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.launch.time.limit");
		    return "pages/learning/waitForLeaderTimeLimit";
		}

		//if the time is up and leader hasn't submitted response - show waitForLeaderFinish page
		boolean isTimeLimitExceeded = service.checkTimeLimitExceeded(assessment, groupLeader);
		if (isTimeLimitExceeded) {
		    request.setAttribute(AssessmentConstants.PARAM_WAITING_MESSAGE_KEY,
			    "label.waiting.for.leader.finish");
		    return "pages/learning/waitForLeaderTimeLimit";
		}
	    }

	    // check if leader has submitted all answers
	    if (isLastAttemptFinishedByLeader) {

		// in case user joins the lesson after leader has answers some answers already - we need to make sure
		// he has the same scratches as leader
		service.copyAnswersFromLeader(user, groupLeader);
	    }
	}

	sessionMap.put(AssessmentConstants.ATTR_GROUP_LEADER, groupLeader);
	boolean isUserLeader = service.isUserGroupLeader(user.getUserId(), toolSessionId);
	sessionMap.put(AssessmentConstants.ATTR_IS_USER_LEADER, isUserLeader);

	Set<QuestionReference> questionReferences = new TreeSet<>(new SequencableComparator());
	questionReferences.addAll(assessment.getQuestionReferences());
	HashMap<Long, AssessmentQuestion> questionToReferenceMap = new HashMap<>();

	//add non-random questions
	for (QuestionReference questionReference : questionReferences) {
	    if (!questionReference.isRandomQuestion()) {
		AssessmentQuestion question = questionReference.getQuestion();
		questionToReferenceMap.put(questionReference.getUid(), question);
	    }
	}

	// init random pool questions
	List<AssessmentQuestion> availableRandomQuestions = new ArrayList<>();
	for (AssessmentQuestion question : assessment.getQuestions()) {
	    if (question.isRandomQuestion()) {
		availableRandomQuestions.add(question);
	    }
	}
	//add random questions (actually replacing them with real ones)
	AssessmentResult lastResult = service.getLastAssessmentResult(assessment.getUid(), user.getUserId());
	for (QuestionReference questionReference : questionReferences) {
	    if (questionReference.isRandomQuestion()) {

		//find random question that will be shown to the user
		AssessmentQuestion randomQuestion = null;
		if (lastResult == null) {
		    //pick element randomly
		    Random rand = new Random(System.currentTimeMillis());
		    randomQuestion = (AssessmentQuestion) availableRandomQuestions.toArray()[rand
			    .nextInt(availableRandomQuestions.size())];
		    availableRandomQuestions.remove(randomQuestion);

		} else {
		    //pick element from the last result
		    for (Iterator<AssessmentQuestion> iter = availableRandomQuestions.iterator(); iter.hasNext();) {
			AssessmentQuestion availableRandomQuestion = iter.next();

			for (AssessmentQuestionResult questionResult : lastResult.getQuestionResults()) {
			    if (availableRandomQuestion.getUid().equals(questionResult.getQbToolQuestion().getUid())) {
				randomQuestion = availableRandomQuestion;
				iter.remove();
				break;
			    }
			}
		    }
		}
		if (randomQuestion == null) {
		    throw new RuntimeException("Random question is null. Something went wrong. questionReference's uid:"
			    + questionReference.getUid());
		}
		questionToReferenceMap.put(questionReference.getUid(), randomQuestion);
	    }
	}

	//user is allowed to answer questions if assessment activity doesn't have leaders or he is the leader
	boolean hasEditRight = !assessment.isUseSelectLeaderToolOuput()
		|| assessment.isUseSelectLeaderToolOuput() && isUserLeader;

	//showResults if user has finished the last result
	boolean showResults = (lastResult != null) && (lastResult.getFinishDate() != null);

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
	sessionMap.put(AssessmentConstants.ATTR_SECONDS_LEFT, secondsLeft);
	boolean isTimeLimitNotLaunched = (lastResult == null) || (lastResult.getTimeLimitLaunchedDate() == null);
	sessionMap.put(AssessmentConstants.ATTR_IS_TIME_LIMIT_NOT_LAUNCHED, isTimeLimitNotLaunched);

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, service.isLastActivity(toolSessionId));

	// add define later support
	if (assessment.isDefineLater()) {
	    return "pages/learning/definelater";
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
		return "pages/learning/submissionDeadline";
	    }
	}

	//sort questions
	LinkedList<QuestionDTO> questionDtos = new LinkedList<>();
	for (QuestionReference questionReference : questionReferences) {
	    AssessmentQuestion question = questionToReferenceMap.get(questionReference.getUid());

	    QuestionDTO questionDto = question.getQuestionDTO();
	    questionDto.setMaxMark(questionReference.getMaxMark());

	    questionDtos.add(questionDto);
	}

	// shuffling
	if (assessment.isShuffled()) {
	    ArrayList<QuestionDTO> shuffledList = new ArrayList<>(questionDtos);
	    Collections.shuffle(shuffledList);
	    questionDtos = new LinkedList<>(shuffledList);
	}
	for (QuestionDTO questionDto : questionDtos) {
	    if (questionDto.isShuffle() || (questionDto.getType() == QbQuestion.TYPE_ORDERING)) {
		ArrayList<OptionDTO> shuffledList = new ArrayList<>(questionDto.getOptionDtos());
		Collections.shuffle(shuffledList);
		questionDto.setOptionDtos(new LinkedHashSet<>(shuffledList));
	    }
	    if (questionDto.getType() == QbQuestion.TYPE_MATCHING_PAIRS) {
		//sort answer options alphanumerically (as per LDEV-4326)
		ArrayList<OptionDTO> optionsSortedByName = new ArrayList<>(questionDto.getOptionDtos());
		optionsSortedByName.sort(new Comparator<OptionDTO>() {
		    @Override
		    public int compare(OptionDTO o1, OptionDTO o2) {
			String name1 = o1.getName() != null ? o1.getName() : "";
			String name2 = o2.getName() != null ? o2.getName() : "";

			return AlphanumComparator.compareAlphnumerically(name1, name2);
		    }
		});
		questionDto.setMatchingPairOptions(new LinkedHashSet<>(optionsSortedByName));
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

	// loadupLastAttempt for displaying purposes
	service.loadupLastAttempt(assessment.getUid(), user.getUserId(), pagedQuestionDtos);

	if (showResults) {

	    // display results page
	    showResults(request, sessionMap);
	    return "pages/learning/results";

	} else {
	    // set attempt started
	    if (hasEditRight) {
		service.setAttemptStarted(assessment, user, toolSessionId, pagedQuestionDtos);
	    }

	    // display Etherpads after each question
	    boolean questionEtherpadEnabled = assessment.isUseSelectLeaderToolOuput()
		    && assessment.isQuestionEtherpadEnabled()
		    && StringUtils.isNotBlank(Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY));
	    request.setAttribute(AssessmentConstants.ATTR_IS_QUESTION_ETHERPAD_ENABLED, questionEtherpadEnabled);
	    if (questionEtherpadEnabled) {
		// get all users from the group, even if they did not reach the Scratchie yet
		// order them by first and last name
		Collection<User> allGroupUsers = service.getAllGroupUsers(toolSessionId).stream()
			.sorted(Comparator.comparing(u -> u.getFirstName() + u.getLastName()))
			.collect(Collectors.toList());
		request.setAttribute(AssessmentConstants.ATTR_ALL_GROUP_USERS, allGroupUsers);
	    }

	    return "pages/learning/learning";
	}
    }

    /**
     * Checks Leader Progress
     */
    @RequestMapping("/checkLeaderProgress")
    @ResponseBody
    public String checkLeaderProgress(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	AssessmentSession session = service.getSessionBySessionId(toolSessionId);
	AssessmentUser leader = session.getGroupLeader();

	//in case of time limit - prevent user from seeing questions page longer than time limit allows
	boolean isTimeLimitExceeded = service.checkTimeLimitExceeded(session.getAssessment(), leader);
	boolean isLeaderResponseFinalized = service.isLastAttemptFinishedByUser(leader);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("isPageRefreshRequested", isLeaderResponseFinalized || isTimeLimitExceeded);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Shows next page. It's available only to leaders as non-leaders see all questions on one page.
     */
    @RequestMapping("/nextPage")
    public String nextPage(HttpServletRequest request)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	return nextPage(request, false, -1);
    }

    /**
     * Auxiliary method to be called by nextPage(HttpServletRequest request,
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
    @SuppressWarnings("unchecked")
    private String nextPage(HttpServletRequest request, boolean isAnswersValidationFailed,
	    int pageNumberWithUnasweredQuestions)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = getSessionMap(request);
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
	    return "pages/learning/results";

	} else {
	    //get user answers from request and store them into sessionMap
	    storeUserAnswersIntoSessionMap(request, oldPageNumber);
	    // store results from sessionMap into DB
	    storeUserAnswersIntoDatabase(sessionMap, true);

	    long secondsLeft = service.getSecondsLeft(assessment, user);
	    sessionMap.put(AssessmentConstants.ATTR_SECONDS_LEFT, secondsLeft);

	    // use redirect to prevent form resubmission
	    String redirectURL = "redirect:/pages/learning/learning.jsp";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AssessmentConstants.ATTR_SESSION_MAP_ID,
		    sessionMapID);
	    redirectURL = WebUtil.appendParameterToURL(redirectURL,
		    AssessmentConstants.ATTR_IS_ANSWERS_VALIDATION_FAILED, "" + isAnswersValidationFailed);
	    return redirectURL;
	}
    }

    /**
     * Ajax call to get the remaining seconds. Needed when the page is reloaded in the browser to check with the server
     * what the current values should be! Otherwise the learner can keep hitting reload after a page change or submit
     * all (when questions are spread across pages) and increase their time!
     *
     * @return
     * @throws JSONException
     * @throws IOException
     */
    @RequestMapping("/getSecondsLeft")
    @ResponseBody
    public String getSecondsLeft(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	AssessmentUser user = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	long secondsLeft = service.getSecondsLeft(assessment, user);
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put(AssessmentConstants.ATTR_SECONDS_LEFT, secondsLeft);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Handling submittion of MarkHedging type of Questions (in case of leader aware tool)
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/submitSingleMarkHedgingQuestion")
    public String submitSingleMarkHedgingQuestion(HttpServletRequest request)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
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
	service.storeSingleMarkHedgingQuestion(assessment, userId, pagedQuestionDtos, singleMarkHedgingQuestionUid);

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

	return "pages/learning/results/markhedging";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/submitAll")
    public String submitAll(HttpServletRequest request)
	    throws ServletException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);

	//get user answers from request and store them into sessionMap
	storeUserAnswersIntoSessionMap(request, pageNumber);

	boolean isTimelimitExpired = WebUtil.readBooleanParam(request, "isTimelimitExpired", false);
	if (!isTimelimitExpired) {

	    // check all required questions got answered
	    int pageNumberWithUnasweredQuestions = validateAnswers(sessionMap);
	    // if some were not then forward to nextPage()
	    if (pageNumberWithUnasweredQuestions != 0) {
		return nextPage(request, true, pageNumberWithUnasweredQuestions);
	    }
	}

	//store results from sessionMap into DB
	boolean isResultsStored = storeUserAnswersIntoDatabase(sessionMap, false);
	// result was not stored in case user was prohibited from submitting (or autosubmitting) answers (e.g. when
	// using 2 browsers). Then show last stored results
	if (!isResultsStored) {
	    List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		    .get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	    Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	    Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	    service.loadupLastAttempt(assessmentUid, userId, pagedQuestionDtos);
	}

	String redirectURL = "redirect:/learning/start.do";
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	if (mode != null) {
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.ATTR_MODE, mode.toString());
	}
	redirectURL = WebUtil.appendParameterToURL(redirectURL, AssessmentConstants.ATTR_TOOL_SESSION_ID,
		((Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID)).toString());
	return redirectURL;
    }

    /**
     * User pressed Resubmit button.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/resubmit")
    public String resubmit(HttpServletRequest request) throws ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);
	AssessmentUser assessmentUser = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);

	Long userId = assessmentUser.getUserId();
	service.unsetSessionFinished(toolSessionId, userId);

	Date lastAttemptStartingDate = service.getLastAssessmentResult(assessment.getUid(), userId).getStartDate();

	// set attempt started: create a new one + mark previous one as not being the latest any longer
	service.setAttemptStarted(assessment, assessmentUser, toolSessionId, pagedQuestionDtos);

	// in case of content was modified in monitor - redirect to start.do in order to refresh info from the DB
	if (assessment.isContentModifiedInMonitor(lastAttemptStartingDate)) {
	    String redirectURL = "redirect:/learning/start.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AttributeNames.PARAM_MODE, mode.toString());
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, AssessmentConstants.PARAM_TOOL_SESSION_ID,
		    toolSessionId.toString());
	    return redirectURL;

	    //otherwise use data from SessionMap
	} else {
	    sessionMap.put(AssessmentConstants.ATTR_SHOW_RESULTS, false);
	    sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
	    sessionMap.put(AssessmentConstants.ATTR_QUESTION_NUMBERING_OFFSET, 1);
	    // clear isUserFailed indicator
	    sessionMap.put(AssessmentConstants.ATTR_IS_USER_FAILED, false);

	    // time limit feature
	    sessionMap.put(AssessmentConstants.ATTR_IS_TIME_LIMIT_NOT_LAUNCHED, true);
	    sessionMap.put(AssessmentConstants.ATTR_SECONDS_LEFT, assessment.getTimeLimit() * 60);

	    return "pages/learning/learning";
	}

    }

    /**
     * Finish learning session.
     */
    @RequestMapping("/finish")
    public String finish(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = user.getUserID().longValue();
	    Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(AssessmentConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (AssessmentApplicationException e) {
	    log.error("Failed get next activity url:" + e.getMessage());
	}

	return "pages/learning/finish";
    }

    /**
     * auto saves responses
     */
    @RequestMapping("/autoSaveAnswers")
    @ResponseStatus(HttpStatus.OK)
    public void autoSaveAnswers(HttpServletRequest request)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);

	//get user answers from request and store them into sessionMap
	storeUserAnswersIntoSessionMap(request, pageNumber);
	//store results from sessionMap into DB
	storeUserAnswersIntoDatabase(sessionMap, true);
    }

    /**
     * Stores date when user has started activity with time limit
     */
    @RequestMapping("/launchTimeLimit")
    @ResponseStatus(HttpStatus.OK)
    public void launchTimeLimit(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	sessionMap.put(AssessmentConstants.ATTR_IS_TIME_LIMIT_NOT_LAUNCHED, false);

	service.launchTimeLimit(assessmentUid, userId);
    }

    @RequestMapping("/vsaAutocomplete")
    @ResponseBody
    public String vsaAutocomplete(HttpServletRequest request, HttpServletResponse response) {
	String userAnswer = WebUtil.readStrParam(request, "term", true);
	Long questionUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_UID);
	AssessmentQuestion question = service.getAssessmentQuestionByUid(questionUid);
	QbQuestion qbQuestion = question.getQbQuestion();

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	if (StringUtils.isNotBlank(userAnswer)) {
	    userAnswer = userAnswer.trim();
	    userAnswer = qbQuestion.isCaseSensitive() ? userAnswer : userAnswer.toLowerCase();

	    for (QbOption option : qbQuestion.getQbOptions()) {

		//filter out options not starting with 'term' and containing '*'
		String optionTitle = qbQuestion.isCaseSensitive() ? option.getName() : option.getName().toLowerCase();
		int i = 0;
		for (String optionAnswer : optionTitle.split("\\r\\n")) {
		    if (optionAnswer.startsWith(userAnswer) && !optionAnswer.contains("*")) {
			ObjectNode optionJSON = JsonNodeFactory.instance.objectNode();
			String originalOptionAnswer = option.getName().split("\\r\\n")[i];
			optionJSON.put("label", originalOptionAnswer);
			responseJSON.add(optionJSON);
		    }
		    i++;
		}
	    }
	}
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Display empty reflection form.
     */
    @RequestMapping("/newReflection")
    public String newReflection(@ModelAttribute("reflectionForm") ReflectionForm refForm, HttpServletRequest request) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long toolSessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = service.getEntry(toolSessionID, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return "pages/learning/notebook";
    }

    /**
     * Submit reflection form input database.
     */
    @RequestMapping("/submitReflection")
    public String submitReflection(@ModelAttribute("reflectionForm") ReflectionForm refForm,
	    HttpServletRequest request) {
	Integer userId = refForm.getUserID();

	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

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

	return finish(request);
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
    @SuppressWarnings("unchecked")
    private void storeUserAnswersIntoSessionMap(HttpServletRequest request, int pageNumber) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
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
	    if (questionType == QbQuestion.TYPE_MULTIPLE_CHOICE) {
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    boolean answerBoolean = false;
		    if (questionDto.isMultipleAnswersAllowed()) {
			String answer = request
				.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + optionDto.getUid());
			answerBoolean = StringUtils.isNotBlank(answer);
		    } else {
			String optionUidSelectedStr = request
				.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
			if (optionUidSelectedStr != null) {
			    Long optionUidSelected = Long.parseLong(optionUidSelectedStr);
			    answerBoolean = optionDto.getUid().equals(optionUidSelected);
			}
		    }
		    optionDto.setAnswerBoolean(answerBoolean);
		}

	    } else if (questionType == QbQuestion.TYPE_MATCHING_PAIRS) {
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    int answerInt = WebUtil.readIntParam(request,
			    AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + optionDto.getUid());
		    optionDto.setAnswerInt(answerInt);
		}

	    } else if (questionType == QbQuestion.TYPE_VERY_SHORT_ANSWERS) {
		String answer = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		questionDto.setAnswer(answer);

	    } else if (questionType == QbQuestion.TYPE_NUMERICAL) {
		String answer = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		questionDto.setAnswer(answer);

	    } else if (questionType == QbQuestion.TYPE_TRUE_FALSE) {
		String answer = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		if (answer != null) {
		    questionDto.setAnswerBoolean(Boolean.parseBoolean(answer));
		    questionDto.setAnswer("answered");
		}

	    } else if (questionType == QbQuestion.TYPE_ESSAY) {
		String answer = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		answer = answer.replaceAll("[\n\r\f]", "");
		questionDto.setAnswer(answer);

	    } else if (questionType == QbQuestion.TYPE_ORDERING) {
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    int answerSequenceId = WebUtil.readIntParam(request,
			    AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + optionDto.getUid());
		    optionDto.setDisplayOrder(answerSequenceId);
		}
		//sort accrording to the new sequenceIds
		Set<OptionDTO> sortedOptions = new TreeSet<>();
		sortedOptions.addAll(questionDto.getOptionDtos());
		questionDto.setOptionDtos(sortedOptions);

	    } else if (questionType == QbQuestion.TYPE_MARK_HEDGING) {

		//store hedging marks
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    Integer markHedging = WebUtil.readIntParam(request,
			    AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + optionDto.getUid(), true);
		    if (markHedging != null) {
			optionDto.setAnswerInt(markHedging);
		    }
		}

		//store justification of hedging if enabled
		if (questionDto.isHedgingJustificationEnabled()) {
		    String answer = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
//		    answer = answer.replaceAll("[\n\r\f]", "");
		    questionDto.setAnswer(answer);
		}
	    }

	    // store confidence level entered by the learner
	    if (assessment.isEnableConfidenceLevels()) {
		int confidenceLevel = WebUtil.readIntParam(request,
			AssessmentConstants.ATTR_CONFIDENCE_LEVEL_PREFIX + i);
		questionDto.setConfidenceLevel(confidenceLevel);
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
    @SuppressWarnings("unchecked")
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
		if (questionDto.isAnswerRequired() || (questionType == QbQuestion.TYPE_MARK_HEDGING)) {

		    boolean isAnswered = false;

		    if (questionType == QbQuestion.TYPE_MULTIPLE_CHOICE) {

			for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			    isAnswered |= optionDto.getAnswerBoolean();
			}

		    } else if (questionType == QbQuestion.TYPE_MATCHING_PAIRS) {
			for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			    isAnswered |= optionDto.getAnswerInt() != 0;
			}

		    } else if ((questionType == QbQuestion.TYPE_VERY_SHORT_ANSWERS)
			    || (questionType == QbQuestion.TYPE_NUMERICAL)
			    || (questionType == QbQuestion.TYPE_TRUE_FALSE)
			    || (questionType == QbQuestion.TYPE_ESSAY)) {
			isAnswered |= StringUtils.isNotBlank(questionDto.getAnswer());

		    } else if (questionType == QbQuestion.TYPE_ORDERING) {
			isAnswered = true;

		    } else if (questionType == QbQuestion.TYPE_MARK_HEDGING) {

			//verify sum of all hedging marks is equal to question's maxMark
			int sumMarkHedging = 0;
			for (OptionDTO optionDto : questionDto.getOptionDtos()) {
			    sumMarkHedging += optionDto.getAnswerInt();
			}
			isAnswered = sumMarkHedging == questionDto.getMaxMark();

			//verify justification of hedging is provided if it was enabled
			if (questionDto.isHedgingJustificationEnabled()) {
			    isAnswered &= StringUtils.isNotBlank(questionDto.getAnswer());
			}
		    }

		    // check all questions were answered
		    if (!isAnswered) {
			isAllQuestionsAnswered = false;
			break;
		    }

		}

		if ((questionDto.getType() == QbQuestion.TYPE_ESSAY) && (questionDto.getMinWordsLimit() > 0)) {

		    if (questionDto.getAnswer() == null) {
			isAllQuestionsReachedMinWordsLimit = false;
			break;

		    } else {
			boolean isMinWordsLimitReached = ValidationUtil.isMinWordsLimitReached(questionDto.getAnswer(),
				questionDto.getMinWordsLimit(), questionDto.isAllowRichEditor());
			// check min words limit is reached
			if (!isMinWordsLimitReached) {
			    isAllQuestionsReachedMinWordsLimit = false;
			    break;
			}
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
    @SuppressWarnings("unchecked")
    private void showResults(HttpServletRequest request, SessionMap<String, Object> sessionMap) {
	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	AssessmentUser user = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	Long userId = user.getUserId();

	int dbResultCount = service.getAssessmentResultCount(assessment.getUid(), userId);
	if (dbResultCount > 0) {

	    // release object from the cache (it's required when we have modified result object in the same request)
	    AssessmentResult result = service.getLastFinishedAssessmentResultNotFromChache(assessment.getUid(), userId);

	    for (Set<QuestionDTO> questionsForOnePage : pagedQuestionDtos) {
		for (QuestionDTO questionDto : questionsForOnePage) {

		    // find corresponding questionResult
		    for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
			if (questionDto.getUid().equals(questionResult.getQbToolQuestion().getUid())) {

			    // copy questionResult's info to the question
			    questionDto.setMark(questionResult.getMark());
			    questionDto.setResponseSubmitted(questionResult.getFinishDate() != null);
			    questionDto.setPenalty(questionResult.getPenalty());

			    //question feedback
			    questionDto.setQuestionFeedback(null);
			    for (OptionDTO optionDto : questionDto.getOptionDtos()) {
				if (questionResult.getQbOption() != null
					&& optionDto.getUid().equals(questionResult.getQbOption().getUid())) {
				    questionDto.setQuestionFeedback(optionDto.getFeedback());
				    break;
				}
			    }

			    // required for showing right/wrong answers icons on results page correctly
			    if ((questionDto.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS
				    || questionDto.getType() == QbQuestion.TYPE_NUMERICAL)
				    && questionResult.getQbOption() != null) {
				boolean isAnsweredCorrectly = false;
				for (OptionDTO optionDto : questionDto.getOptionDtos()) {
				    if (optionDto.getUid().equals(questionResult.getQbOption().getUid())) {
					isAnsweredCorrectly = optionDto.getMaxMark() > 0;
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

	    Date timeTaken = result.getFinishDate() == null ? new Date(0)
		    : new Date(result.getFinishDate().getTime() - result.getStartDate().getTime());
	    result.setTimeTaken(timeTaken);
	    if (assessment.isAllowOverallFeedbackAfterQuestion()) {
		int percentageCorrectAnswers = (int) (result.getGrade() * 100 / result.getMaximumGrade());
		ArrayList<AssessmentOverallFeedback> overallFeedbacks = new ArrayList<>(
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

	    // if answers are going to be disclosed, prepare data for the table in results page
	    if (assessment.isAllowDiscloseAnswers()) {
		// such entities should not go into session map, but as request attributes instead
		SortedSet<AssessmentSession> sessions = new TreeSet<>(new AssessmentSessionComparator());
		sessions.addAll(service.getSessionsByContentId(assessment.getContentId()));

		Long userSessionId = user.getSession().getSessionId();
		Integer userSessionIndex = null;
		int sessionIndex = 0;
		// find user session in order to put it first
		List<AssessmentSession> sessionList = new ArrayList<>();
		for (AssessmentSession session : sessions) {
		    if (userSessionId.equals(session.getSessionId())) {
			userSessionIndex = sessionIndex;
		    } else {
			sessionList.add(session);
		    }
		    sessionIndex++;
		}

		request.setAttribute("sessions", sessionList);

		Map<Long, QuestionSummary> questionSummaries = service.getQuestionSummaryForExport(assessment);
		request.setAttribute("questionSummaries", questionSummaries);

		// question summaries need to be in the same order as sessions, i.e. user group first
		if (userSessionIndex != null) {
		    sessionList.add(0, user.getSession());
		    for (QuestionSummary summary : questionSummaries.values()) {
			List<List<AssessmentQuestionResult>> questionResultsPerSession = summary
				.getQuestionResultsPerSession();
			if (questionResultsPerSession != null) {
			    List<AssessmentQuestionResult> questionResults = questionResultsPerSession
				    .remove((int) userSessionIndex);
			    questionResultsPerSession.add(0, questionResults);
			}
		    }
		}

		// Assessment currently supports only one place for ratings.
		// It is rating other groups' answers on results page.
		// Criterion gets automatically created and there must be only one.
		List<RatingCriteria> criteria = ratingService.getCriteriasByToolContentId(assessment.getContentId());
		if (criteria.size() >= 2) {
		    throw new IllegalArgumentException("There can be only one criterion for an Assessment activity. "
			    + "If other criteria are introduced, the criterion for rating other groups' answers needs to become uniquely identifiable.");
		}
		ToolActivityRatingCriteria criterion = null;
		if (criteria.isEmpty()) {
		    criterion = (ToolActivityRatingCriteria) RatingCriteria
			    .getRatingCriteriaInstance(RatingCriteria.TOOL_ACTIVITY_CRITERIA_TYPE);
		    criterion.setTitle(service.getMessage("label.answer.rating.title"));
		    criterion.setOrderId(1);
		    criterion.setCommentsEnabled(true);
		    criterion.setRatingStyle(RatingCriteria.RATING_STYLE_STAR);
		    criterion.setToolContentId(assessment.getContentId());

		    userManagementService.save(criterion);
		} else {
		    criterion = (ToolActivityRatingCriteria) criteria.get(0);
		}

		// Item IDs are AssessmentQuestionResults UIDs, i.e. a user answer for a particular question
		// Get all item IDs no matter which session they belong to.
		Set<Long> itemIds = questionSummaries.values().stream()
			.flatMap(s -> s.getQuestionResultsPerSession().stream())
			.collect(Collectors.mapping(l -> l.get(l.size() - 1).getUid(), Collectors.toSet()));

		List<ItemRatingDTO> itemRatingDtos = ratingService.getRatingCriteriaDtos(assessment.getContentId(),
			null, itemIds, true, userId);
		// Mapping of Item ID -> DTO
		Map<Long, ItemRatingDTO> itemRatingDtoMap = itemRatingDtos.stream()
			.collect(Collectors.toMap(ItemRatingDTO::getItemId, Function.identity()));

		request.setAttribute("itemRatingDtos", itemRatingDtoMap);
	    }
	}

	//calculate whether isResubmitAllowed
	int attemptsAllowed = assessment.getAttemptsAllowed();
	boolean isResubmitAllowed = ((attemptsAllowed > dbResultCount) | (attemptsAllowed == 0));
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, isResubmitAllowed);
    }

    /**
     * Store user answers in DB in last unfinished attempt and notify teachers about it.
     */
    @SuppressWarnings("unchecked")
    private boolean storeUserAnswersIntoDatabase(SessionMap<String, Object> sessionMap, boolean isAutosave)
	    throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	List<Set<QuestionDTO>> pagedQuestionDtos = (List<Set<QuestionDTO>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTION_DTOS);
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);

	boolean isResultsStored = service.storeUserAnswers(assessment, userId, pagedQuestionDtos, isAutosave);

	// notify teachers
	if ((mode != null) && !mode.isTeacher() && !isAutosave && isResultsStored
		&& assessment.isNotifyTeachersOnAttemptCompletion()) {
	    AssessmentUser assessmentUser = getCurrentUser(toolSessionId);
	    String fullName = assessmentUser.getLastName() + " " + assessmentUser.getFirstName();
	    service.notifyTeachersOnAttemptCompletion(toolSessionId, fullName);
	}

	return isResultsStored;
    }

    private AssessmentUser getCurrentUser(Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	AssessmentUser assessmentUser = service.getUserByIDAndSession(user.getUserID().longValue(), sessionId);

	if (assessmentUser == null) {
	    AssessmentSession session = service.getSessionBySessionId(sessionId);
	    assessmentUser = new AssessmentUser(user, session);
	    service.createUser(assessmentUser);
	}
	return assessmentUser;
    }

    private AssessmentUser getSpecifiedUser(IAssessmentService service, Long sessionId, Integer userId) {
	AssessmentUser assessmentUser = service.getUserByIDAndSession(userId.longValue(), sessionId);
	if (assessmentUser == null) {
	    log.error("Unable to find specified user for assessment activity. Screens are likely to fail. SessionId="
		    + sessionId + " UserId=" + userId);
	}
	return assessmentUser;
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }
}
