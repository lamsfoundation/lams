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
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.flux.FluxRegistry;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.logevent.LearnerInteractionEvent;
import org.lamsfoundation.lams.logevent.service.ILearnerInteractionService;
import org.lamsfoundation.lams.qb.dto.QbStatsActivityDTO;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.service.IRatingService;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.dto.GradeStatsDTO;
import org.lamsfoundation.lams.tool.assessment.dto.OptionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.TblAssessmentQuestionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummaryItem;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentSessionComparator;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    public static Logger log = Logger.getLogger(MonitoringController.class);

    private static final Comparator<User> USER_NAME_COMPARATOR = Comparator.comparing(User::getFirstName)
	    .thenComparing(User::getLastName).thenComparing(User::getLogin);

    @Autowired
    @Qualifier("laasseAssessmentService")
    private IAssessmentService service;

    @Autowired
    private IUserManagementService userManagementService;

    @Autowired
    private IQbService qbService;

    @Autowired
    private IRatingService ratingService;

    @Autowired
    private ILearnerInteractionService learnerInteractionService;

    @RequestMapping("/summary")
    public String summary(HttpServletRequest request, HttpServletResponse response) {

	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<GradeStatsDTO> sessionDtos = service.getSessionDtos(contentId, false);

	Assessment assessment = service.getAssessmentByContentId(contentId);

	//set SubmissionDeadline, if any
	if (assessment.getSubmissionDeadline() != null) {
	    Date submissionDeadline = assessment.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(AssessmentConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(AssessmentConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	// Create reflectList if reflection is enabled.
	if (assessment.isReflectOnActivity()) {
	    List<ReflectDTO> reflectList = service.getReflectList(assessment.getContentId());
	    // Add reflectList to sessionMap
	    sessionMap.put(AssessmentConstants.ATTR_REFLECT_LIST, reflectList);
	}

	//prepare list of the questions to display in question drop down menu, filtering out questions that aren't supposed to be answered
	List<AssessmentQuestion> questionList = new LinkedList<>();
	//in case there is at least one random question - we need to show all questions in a drop down select
	if (assessment.hasRandomQuestion()) {
	    questionList.addAll(assessment.getQuestions());

	    //show only questions from question list otherwise
	} else {
	    for (QuestionReference reference : assessment.getQuestionReferences()) {
		questionList.add(reference.getQuestion());
	    }
	}

	//prepare toolOutputDefinitions and activityEvaluation
	List<String> toolOutputDefinitions = new ArrayList<>();
	toolOutputDefinitions.add(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE);
	toolOutputDefinitions.add(AssessmentConstants.OUTPUT_NAME_BEST_SCORE);
	toolOutputDefinitions.add(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE);
	toolOutputDefinitions.add(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE);
	String activityEvaluation = service.getActivityEvaluation(contentId);
	sessionMap.put(AssessmentConstants.ATTR_TOOL_OUTPUT_DEFINITIONS, toolOutputDefinitions);
	sessionMap.put(AssessmentConstants.ATTR_ACTIVITY_EVALUATION, activityEvaluation);

	// cache into sessionMap
	boolean isGroupedActivity = service.isGroupedActivity(contentId);
	sessionMap.put(AssessmentConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	sessionMap.put("sessionDtos", sessionDtos);
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, assessment);
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_LIST, questionList);
	sessionMap.put(AssessmentConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	// display student choices only if all questions are multiple choice
	boolean displayStudentChoices = true;
	boolean vsaPresent = false;
	int maxOptionsInQuestion = 0;

	for (AssessmentQuestion question : assessment.getQuestions()) {
	    if (displayStudentChoices && question.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE) {
		int optionsInQuestion = question.getQbQuestion().getQbOptions().size();
		if (optionsInQuestion > maxOptionsInQuestion) {
		    maxOptionsInQuestion = optionsInQuestion;
		}
	    } else {
		displayStudentChoices = false;
	    }

	    if (question.getType() == QbQuestion.TYPE_VERY_SHORT_ANSWERS) {
		vsaPresent = true;
	    }
	}

	request.setAttribute("vsaPresent", vsaPresent);

	request.setAttribute("displayStudentChoices", displayStudentChoices);
	if (displayStudentChoices) {
	    request.setAttribute("maxOptionsInQuestion", maxOptionsInQuestion);

	    int totalNumberOfUsers = service.getCountUsersByContentId(contentId);

	    Set<QuestionDTO> questionDtos = new TreeSet<>();
	    for (AssessmentQuestion question : assessment.getQuestions()) {
		QuestionDTO questionDto = new QuestionDTO(question);
		questionDtos.add(questionDto);

		// build candidate dtos
		for (OptionDTO optionDto : questionDto.getOptionDtos()) {
		    int optionAttemptCount = service.countAttemptsPerOption(contentId, optionDto.getUid());

		    float percentage = (float) (optionAttemptCount * 100) / totalNumberOfUsers;
		    optionDto.setPercentage(percentage);
		}
	    }
	    request.setAttribute("questions", questionDtos);
	}

	List<TblAssessmentQuestionDTO> tblQuestionDtos = TblMonitoringController.getTblAssessmentQuestionDtos(contentId,
		true, service);
	request.setAttribute("questionDtos", tblQuestionDtos);
	SortedSet<AssessmentSession> sessions = new TreeSet<>(new AssessmentSessionComparator());
	sessions.addAll(service.getSessionsByContentId(assessment.getContentId()));

	request.setAttribute("sessions", sessions);

	// lists all code styles used in this assessment
	Set<Integer> codeStyles = questionList.stream().filter(q -> q.getQbQuestion().getCodeStyle() != null)
		.collect(Collectors.mapping(q -> q.getQbQuestion().getCodeStyle(), Collectors.toSet()));

	if (!codeStyles.isEmpty()) {
	    request.setAttribute(AssessmentConstants.ATTR_CODE_STYLES, codeStyles);
	}

	return "pages/monitoring/monitoring";
    }

    @RequestMapping(path = "/getCompletionChartsData", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public Flux<String> getCompletionChartsData(@RequestParam long toolContentId)
	    throws JsonProcessingException, IOException {
	return service.getCompletionChartsDataFlux(toolContentId);
    }

    @RequestMapping("/userMasterDetail")
    public String userMasterDetail(HttpServletRequest request, HttpServletResponse response) {
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	Long sessionId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_SESSION_ID);
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	AssessmentResultDTO result = service.getUserMasterDetail(sessionId, userId);

	request.setAttribute(AssessmentConstants.ATTR_ASSESSMENT_RESULT, result);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (result == null) ? null : "pages/monitoring/parts/masterDetailLoadUp";
    }

    @RequestMapping("/questionSummary")
    public String questionSummary(HttpServletRequest request, HttpServletResponse response) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long questionUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_UID);
	if (questionUid.equals(-1l)) {
	    return null;
	}
	Long contentId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);

	QuestionSummary questionSummary = service.getQuestionSummary(contentId, questionUid);
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_SUMMARY, questionSummary);

	return "pages/monitoring/parts/questionsummary";
    }

    @RequestMapping("/userSummary")
    public String userSummary(HttpServletRequest request, HttpServletResponse response) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	Long sessionId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_SESSION_ID);
	Long contentId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);

	UserSummary userSummary = service.getUserSummary(contentId, userId, sessionId);
	request.setAttribute(AssessmentConstants.ATTR_USER_SUMMARY, userSummary);

	Map<Long, LearnerInteractionEvent> learnerInteractions = learnerInteractionService
		.getFirstLearnerInteractions(contentId, userId.intValue());
	request.setAttribute("learnerInteractions", learnerInteractions);

	Assessment assessment = service.getAssessmentByContentId(contentId);
	boolean questionEtherpadEnabled = assessment.isUseSelectLeaderToolOuput()
		&& assessment.isQuestionEtherpadEnabled()
		&& StringUtils.isNotBlank(Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY));
	request.setAttribute(AssessmentConstants.ATTR_IS_QUESTION_ETHERPAD_ENABLED, questionEtherpadEnabled);
	request.setAttribute(AssessmentConstants.ATTR_TOOL_SESSION_ID, sessionId);

	if (userSummary.getUserSummaryItems() != null) {
	    // lists all code styles used in this assessment
	    Set<Integer> codeStyles = userSummary.getUserSummaryItems().stream().map(UserSummaryItem::getQuestionDto)
		    .filter(q -> q.getCodeStyle() != null)
		    .collect(Collectors.mapping(q -> q.getCodeStyle(), Collectors.toSet()));

	    if (!codeStyles.isEmpty()) {
		request.setAttribute(AssessmentConstants.ATTR_CODE_STYLES, codeStyles);
	    }
	}

	return "pages/monitoring/parts/usersummary";
    }

    @RequestMapping(path = "/saveUserGrade", method = RequestMethod.POST)
    @ResponseBody
    public String saveUserGrade(HttpServletRequest request, HttpServletResponse response) {
	String responseText = null;
	if ((request.getParameter(AssessmentConstants.PARAM_NOT_A_NUMBER) == null)
		&& !StringUtils.isEmpty(request.getParameter(AssessmentConstants.PARAM_QUESTION_RESULT_UID))) {
	    Long questionResultUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_RESULT_UID);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);

	    String column = request.getParameter(AssessmentConstants.PARAM_COLUMN);
	    Float newGrade = null;
	    String markerComment = null;
	    if (column.equals(AssessmentConstants.PARAM_GRADE)) {
		String gradeString = request.getParameter(AssessmentConstants.PARAM_GRADE);
		if (StringUtils.isNotBlank(gradeString) && !gradeString.strip().equals("-")) {
		    newGrade = Float.valueOf(gradeString);
		    responseText = teacher.getLastName() + " " + teacher.getFirstName();
		}
	    } else if (column.equals(AssessmentConstants.PARAM_MARKER_COMMENT)) {
		markerComment = request.getParameter(AssessmentConstants.PARAM_MARKER_COMMENT);
	    }

	    service.changeQuestionResultMark(questionResultUid, newGrade, markerComment, teacher.getUserID());
	}
	return responseText;
    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = service.getAssessmentByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, AssessmentConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());

	}
	assessment.setSubmissionDeadline(tzSubmissionDeadline);
	service.saveOrUpdateAssessment(assessment);

	return formattedDate;
    }

    /**
     * Set tool's activityEvaluation
     */
    @RequestMapping(path = "/setActivityEvaluation", method = RequestMethod.POST)
    @ResponseBody
    public String setActivityEvaluation(HttpServletRequest request, HttpServletResponse response) throws IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long contentID = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);
	String activityEvaluation = WebUtil.readStrParam(request, AssessmentConstants.ATTR_ACTIVITY_EVALUATION, true);
	service.setActivityEvaluation(contentID, activityEvaluation);

	// update the session ready for stats tab to be reloaded otherwise flicking between tabs
	// causes the old value to be redisplayed
	sessionMap.put(AssessmentConstants.ATTR_ACTIVITY_EVALUATION, activityEvaluation);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("success", "true");
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Refreshes user list.
     */
    @RequestMapping("/getUsers")
    public String getUsers(HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);

	Long sessionId = WebUtil.readLongParam(request, "sessionId", true);

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	List<AssessmentUserDTO> userDtos = new ArrayList<>();
	int countSessionUsers = 0;
	if (sessionId == null) {
	    userDtos = service.getPagedUsersByContentId(assessment.getContentId(), page - 1, rowLimit, sortBy,
		    sortOrder, searchString);
	    countSessionUsers = service.getCountUsersByContentId(assessment.getContentId(), searchString);
	} else
	//in case of UseSelectLeaderToolOuput - display only one user
	if (assessment.isUseSelectLeaderToolOuput()) {

	    AssessmentSession session = service.getSessionBySessionId(sessionId);
	    AssessmentUser groupLeader = session.getGroupLeader();

	    if (groupLeader != null) {

		Float assessmentResult = service.getLastTotalScoreByUser(assessment.getUid(), groupLeader.getUserId());
		String portraitId = service.getPortraitId(groupLeader.getUserId());

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setUserId(groupLeader.getUserId());
		userDto.setSessionId(sessionId);
		userDto.setFirstName(groupLeader.getFirstName());
		userDto.setLastName(groupLeader.getLastName());
		userDto.setGrade(assessmentResult == null ? 0 : assessmentResult);
		userDto.setPortraitId(portraitId);
		userDtos.add(userDto);
		countSessionUsers = 1;
	    }

	} else {
	    // Get the user list from the db
	    userDtos = service.getPagedUsersBySession(sessionId, page - 1, rowLimit, sortBy, sortOrder, searchString);
	    countSessionUsers = service.getCountUsersBySession(sessionId, searchString);
	}

	int totalPages = Double.valueOf(Math.ceil(Double.valueOf(countSessionUsers) / Double.valueOf(rowLimit)))
		.intValue();

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 1;
	for (AssessmentUserDTO userDto : userDtos) {

	    ArrayNode userData = JsonNodeFactory.instance.arrayNode();
	    userData.add(userDto.getUserId());
	    userData.add(userDto.getSessionId());
	    String fullName = HtmlUtils.htmlEscape(userDto.getFirstName() + " " + userDto.getLastName());
	    userData.add(fullName);
	    userData.add(userDto.getGrade());
	    if (userDto.getPortraitId() != null) {
		userData.add(userDto.getPortraitId());
	    }

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.set("cell", userData);

	    rows.add(userRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.set("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responseJSON.toString()));
	return null;
    }

    /**
     * Refreshes user list.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/getUsersByQuestion")
    public String getUsersByQuestion(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);

	Long sessionId = WebUtil.readLongParam(request, "sessionId");
	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	AssessmentQuestion question = service.getAssessmentQuestionByUid(questionUid);

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	List<AssessmentUserDTO> userDtos = new ArrayList<>();
	int countSessionUsers = 0;
	//in case of UseSelectLeaderToolOuput - display only one user
	if (assessment.isUseSelectLeaderToolOuput()) {

	    AssessmentSession session = service.getSessionBySessionId(sessionId);
	    AssessmentUser groupLeader = session.getGroupLeader();

	    if (groupLeader != null) {

		AssessmentResult assessmentResult = service.getLastFinishedAssessmentResult(assessment.getUid(),
			groupLeader.getUserId());
		Long questionResultUid = null;
		if (assessmentResult != null) {
		    for (AssessmentQuestionResult dbQuestionResult : assessmentResult.getQuestionResults()) {
			if (dbQuestionResult.getQbToolQuestion().getUid().equals(questionUid)) {
			    questionResultUid = dbQuestionResult.getUid();
			    break;
			}
		    }
		}

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setQuestionResultUid(questionResultUid);
		userDto.setFirstName(groupLeader.getFirstName());
		userDto.setLastName(groupLeader.getLastName());
		userDtos.add(userDto);
		countSessionUsers = 1;
	    }

	} else {
	    // Get the user list from the db
	    userDtos = service.getPagedUsersBySessionAndQuestion(sessionId, questionUid, page - 1, rowLimit, sortBy,
		    sortOrder, searchString);
	    countSessionUsers = service.getCountUsersBySession(sessionId, searchString);
	}

	int totalPages = Double.valueOf(Math.ceil(Double.valueOf(countSessionUsers) / Double.valueOf(rowLimit)))
		.intValue();

	Long ratingCriteriaId = null;
	if (question.isGroupsAnswersDisclosed()) {
	    // Assessment currently supports only one place for ratings.
	    // It is rating other groups' answers on results page.
	    // Criterion gets automatically created in learner and there must be only one.
	    List<RatingCriteria> criteria = ratingService.getCriteriasByToolContentId(assessment.getContentId());
	    if (criteria.size() >= 2) {
		throw new IllegalArgumentException("There can be only one criterion for an Assessment activity. "
			+ "If other criteria are introduced, the criterion for rating other groups' answers needs to become uniquely identifiable.");
	    }

	    if (!criteria.isEmpty()) {
		ratingCriteriaId = criteria.get(0).getRatingCriteriaId();
	    }
	}

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 1;
	for (AssessmentUserDTO userDto : userDtos) {

	    Long questionResultUid = userDto.getQuestionResultUid();
	    String fullName = HtmlUtils.htmlEscape(userDto.getFirstName() + " " + userDto.getLastName());

	    ArrayNode userData = JsonNodeFactory.instance.arrayNode();
	    if (questionResultUid != null) {
		AssessmentQuestionResult questionResult = service.getAssessmentQuestionResultByUid(questionResultUid);

		userData.add(questionResultUid);
		userData.add(questionResult.getMaxMark());
		userData.add(fullName);

		String response = AssessmentEscapeUtils.printResponsesForJqgrid(questionResult);
		if (StringUtils.isNotBlank(questionResult.getJustification())) {
		    response += "<br><i>" + service.getMessage("label.answer.justification") + "</i><br>"
			    + questionResult.getJustificationEscaped();
		}
		userData.add(response);

		// show confidence levels if this feature is turned ON
		if (assessment.isEnableConfidenceLevels()) {
		    userData.add(questionResult.getQbQuestion().getType().equals(QbQuestion.TYPE_MARK_HEDGING) ? -1
			    : questionResult.getConfidenceLevel());
		}

		// show average rating
		if (question.isGroupsAnswersDisclosed()) {
		    String starString = "-";
		    if (ratingCriteriaId != null) {
			List<Rating> ratings = ratingService.getRatingsByCriteriasAndItems(
				Arrays.asList(ratingCriteriaId), Arrays.asList(questionResultUid));
			if (!ratings.isEmpty()) {
			    int numberOfVotes = ratings.size();
			    double ratingSum = ratings.stream().mapToDouble(Rating::getRating).sum();
			    String averageRating = NumberUtil
				    .formatLocalisedNumberForceDecimalPlaces(ratingSum / numberOfVotes, null, 2);

			    starString = "<div class='rating-stars-holder'>";
			    starString += "<div class='rating-stars-disabled rating-stars-new' data-average='"
				    + averageRating + "' data-id='" + ratingCriteriaId + "'>";
			    starString += "</div>";
			    starString += "<div class='rating-stars-caption' id='rating-stars-caption-"
				    + ratingCriteriaId + "' >";
			    String msg = service.getMessage("label.average.rating",
				    new Object[] { averageRating, numberOfVotes });
			    starString += msg;
			    starString += "</div>";
			}
		    }
		    userData.add(starString);
		}

		//LDEV_NTU-11 Swapping Mark and Response columns in Assessment Monitor
		userData.add(questionResult.getQbQuestion().getType().equals(QbQuestion.TYPE_ESSAY)
			&& questionResult.getMarkedBy() == null ? "-" : questionResult.getMark().toString());
		userData.add(
			questionResult.getMarkedBy() == null
				? (questionResult.getQbQuestion().getType().equals(QbQuestion.TYPE_ESSAY)
					? service.getMessage("label.monitoring.user.summary.grade.required")
					: "")
				: questionResult.getMarkedBy().getFullName());
		userData.add(questionResult.getMarkerComment());
	    } else {
		userData.add("");
		userData.add("");
		userData.add(fullName);
		userData.add("-");
		if (assessment.isEnableConfidenceLevels()) {
		    userData.add(-1);
		}
		if (question.isGroupsAnswersDisclosed()) {
		    userData.add("-");
		}
		userData.add("-");
		userData.add("");
		userData.add("");
	    }

	    userData.add(userDto.getUserId());
	    if (userDto.getPortraitId() == null) {
		userData.add("");
	    } else {
		userData.add(userDto.getPortraitId());
	    }

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.set("cell", userData);

	    rows.add(userRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.set("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responseJSON.toString()));
	return null;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/getAnswerRatings")
    @ResponseBody
    public String getAnswerRatings(@RequestParam long questionResultUid, Locale locale, HttpServletResponse response)
	    throws IOException {
	AssessmentQuestionResult questionResult = service.getAssessmentQuestionResultByUid(questionResultUid);
	long toolContentId = questionResult.getAssessmentResult().getAssessment().getContentId();
	List<RatingCriteria> criteria = ratingService.getCriteriasByToolContentId(toolContentId);
	if (criteria.size() >= 2) {
	    throw new IllegalArgumentException("There can be only one criterion for an Assessment activity. "
		    + "If other criteria are introduced, the criterion for rating other groups' answers needs to become uniquely identifiable.");
	}
	if (criteria.isEmpty()) {
	    // criteria were not yet created in learner results page
	    return null;
	}

	Long ratingCriteriaId = criteria.get(0).getRatingCriteriaId();
	List<Rating> ratings = ratingService.getRatingsByCriteriasAndItems(Arrays.asList(ratingCriteriaId),
		Arrays.asList(questionResultUid));
	List<RatingCommentDTO> comments = ratingService.getCommentsByCriteriaAndItem(ratingCriteriaId, null,
		questionResultUid);
	Map<Long, RatingCommentDTO> commentsByUserId = comments.stream()
		.collect(Collectors.toMap(RatingCommentDTO::getUserId, Function.identity()));

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 0;

	for (Rating rating : ratings) {
	    ArrayNode ratingJSON = JsonNodeFactory.instance.arrayNode();
	    User learner = rating.getLearner();
	    long userId = learner.getUserId();
	    RatingCommentDTO comment = commentsByUserId.get(userId);

	    AssessmentSession session = service.getSessionBySessionId(rating.getToolSessionId());

	    ratingJSON.add(rating.getUid());
	    ratingJSON.add(comment.getUserFullName() + "<BR>" + session.getSessionName() + "<BR>"
		    + DateUtil.convertToStringForJSON(comment.getPostedDate(), locale));
	    ratingJSON.add(NumberUtil.formatLocalisedNumberForceDecimalPlaces(rating.getRating(), null, 2));
	    ratingJSON.add(comment.getComment());
	    ratingJSON.add(userId);
	    ratingJSON.add(learner.getPortraitUuid() == null ? "" : learner.getPortraitUuid().toString());

	    ObjectNode ratingRow = JsonNodeFactory.instance.objectNode();
	    ratingRow.put("id", i++);
	    ratingRow.set("cell", ratingJSON);

	    rows.add(ratingRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", 1);
	responseJSON.put("page", 1);
	responseJSON.put("records", rows.size());
	responseJSON.set("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Get the mark summary with data arranged in bands. Can be displayed graphically or in a table.
     */
    @RequestMapping("/getMarkChartData")
    public String getMarkChartData(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long contentId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = service.getAssessmentByContentId(contentId);
	List<Float> results = null;

	if (assessment != null) {
	    if (assessment.isUseSelectLeaderToolOuput()) {
		results = service.getMarksArrayForLeaders(contentId);
	    } else {
		Long sessionId = WebUtil.readLongParam(request, AssessmentConstants.ATTR_TOOL_SESSION_ID, true);
		results = sessionId == null ? service.getMarksArrayByContentId(contentId)
			: service.getMarksArray(sessionId);
	    }
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (results != null) {
	    responseJSON.set("data", JsonUtil.readArray(results));
	} else {
	    responseJSON.set("data", JsonUtil.readArray(new Float[0]));
	}

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().write(responseJSON.toString());
	return null;

    }

    /**
     * Excel Summary Export.
     */
    @RequestMapping(path = "/exportSummary", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    @ResponseStatus(HttpStatus.OK)
    public void exportSummary(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	String fileName = null;
	Long contentId = null;
	if (sessionMapID != null) {
	    SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		    .getAttribute(sessionMapID);
	    request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    contentId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);
	} else {
	    contentId = WebUtil.readLongParam(request, "toolContentID");
	    fileName = WebUtil.readStrParam(request, "fileName");
	}

	Assessment assessment = service.getAssessmentByContentId(contentId);
	if (assessment == null) {
	    return;
	}

	List<ExcelSheet> sheets = service.exportSummary(assessment, contentId);

	// Setting the filename if it wasn't passed in the request
	if (fileName == null) {
	    fileName = "assessment_" + assessment.getUid() + "_export.xlsx";
	}

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	log.debug("Exporting assessment to a spreadsheet: " + assessment.getContentId());

	// set cookie that will tell JS script that export has been finished
	WebUtil.setFileDownloadTokenCookie(request, response);

	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, service.getMessage("label.export.exported.on"), true);
    }

    @RequestMapping("/statistic")
    public String statistic(HttpServletRequest request, HttpServletResponse response) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = service.getAssessmentByContentId(contentId);
	if (assessment != null) {
	    if (assessment.isUseSelectLeaderToolOuput()) {
		GradeStatsDTO leaderDto = service.getStatsDtoForLeaders(contentId);
		sessionMap.put("leaderDto", leaderDto);
	    } else {
		List<GradeStatsDTO> sessionDtos = service.getSessionDtos(contentId, true);
		sessionMap.put("sessionDtos", sessionDtos);

		GradeStatsDTO activityDto = service.getStatsDtoForActivity(contentId);
		sessionMap.put("activityDto", activityDto);
	    }

	    List<QbStatsActivityDTO> qbStats = new LinkedList<>();
	    for (AssessmentQuestion question : assessment.getQuestions()) {
		QbStatsActivityDTO questionStats = qbService.getActivityStatsByContentId(assessment.getContentId(),
			question.getQbQuestion().getUid());
		questionStats.setQbQuestion(question.getQbQuestion());
		qbStats.add(questionStats);
	    }
	    request.setAttribute("qbStats", qbStats);
	}
	return "pages/monitoring/statisticpart";
    }

    /**
     * Allows displaying correct answers to learners
     *
     * @throws IOException
     */
    @RequestMapping(path = "/discloseCorrectAnswers", method = RequestMethod.POST)
    public void discloseCorrectAnswers(@RequestParam long questionUid, @RequestParam long toolContentID,
	    @RequestParam(required = false) boolean skipLearnersNotification, HttpServletResponse response)
	    throws IOException {

	AssessmentQuestion question = service.getAssessmentQuestionByUid(questionUid);
	if (question.isCorrectAnswersDisclosed()) {
	    log.warn(
		    "Trying to disclose correct answers when they are already disclosed for Assessment tool content ID "
			    + toolContentID + " and question UID: " + questionUid);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    return;
	}
	question.setCorrectAnswersDisclosed(true);
	service.updateAssessmentQuestion(question);

	if (!skipLearnersNotification) {
	    service.notifyLearnersOnAnswerDisclose(toolContentID);
	}

	if (log.isDebugEnabled()) {
	    log.debug("Disclosed correct answers for Assessment tool content ID " + toolContentID + " and question ID "
		    + questionUid);
	}
    }

    /**
     * Allows displaying other groups' answers to learners
     *
     * @throws IOException
     */
    @RequestMapping(path = "/discloseGroupsAnswers", method = RequestMethod.POST)
    public void discloseGroupsAnswers(@RequestParam long questionUid, @RequestParam long toolContentID,
	    @RequestParam(required = false) boolean skipLearnersNotification, HttpServletResponse response)
	    throws IOException {

	AssessmentQuestion question = service.getAssessmentQuestionByUid(questionUid);
	if (question.isGroupsAnswersDisclosed()) {
	    log.warn("Trying to disclose group answers when they are already disclosed for Assessment tool content ID "
		    + toolContentID + " and question UID: " + questionUid);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    return;
	}

	question.setGroupsAnswersDisclosed(true);
	service.updateAssessmentQuestion(question);

	if (!skipLearnersNotification) {
	    service.notifyLearnersOnAnswerDisclose(toolContentID);
	}

	if (log.isDebugEnabled()) {
	    log.debug("Disclosed other groups' answers for Assessment tool content ID " + toolContentID
		    + " and question ID " + questionUid);
	}
    }

    @RequestMapping(path = "/updateTimeLimit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateTimeLimit(@RequestParam(name = AssessmentConstants.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam int relativeTimeLimit, @RequestParam(required = false) Long absoluteTimeLimit) {
	if (relativeTimeLimit < 0) {
	    throw new InvalidParameterException(
		    "Relative time limit must not be negative and it is " + relativeTimeLimit);
	}
	if (absoluteTimeLimit != null && relativeTimeLimit != 0) {
	    throw new InvalidParameterException(
		    "Relative time limit must not be provided when absolute time limit is set");
	}

	Assessment assessment = service.getAssessmentByContentId(toolContentId);
	assessment.setRelativeTimeLimit(relativeTimeLimit);
	// set time limit as seconds from start of epoch, using current server time zone
	assessment.setAbsoluteTimeLimit(absoluteTimeLimit == null ? null
		: LocalDateTime.ofEpochSecond(absoluteTimeLimit, 0, OffsetDateTime.now().getOffset()));
	service.saveOrUpdateAssessment(assessment);

	// update monitoring UI where time limits are reflected on dashboard
	FluxRegistry.emit(CommonConstants.ACTIVITY_TIME_LIMIT_CHANGED_SINK_NAME, Set.of(toolContentId));
    }

    @RequestMapping(path = "/getPossibleIndividualTimeLimits", method = RequestMethod.GET)
    @ResponseBody
    public String getPossibleIndividualTimeLimits(
	    @RequestParam(name = AssessmentConstants.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam(name = "term") String searchString) {
	Assessment assessment = service.getAssessmentByContentId(toolContentId);
	Map<Integer, Integer> timeLimitAdjustments = assessment.getTimeLimitAdjustments();

	List<User> users = service.getPossibleIndividualTimeLimitUsers(toolContentId, searchString);
	Grouping grouping = service.getGrouping(toolContentId);

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	String groupLabel = service.getMessage("monitoring.label.group") + " \"";
	if (grouping != null) {
	    Set<Group> groups = grouping.getGroups();
	    for (Group group : groups) {
		if (!group.getUsers().isEmpty()
			&& group.getGroupName().toLowerCase().contains(searchString.toLowerCase())) {
		    ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
		    groupJSON.put("label", groupLabel + group.getGroupName() + "\"");
		    groupJSON.put("value", "group-" + group.getGroupId());
		    responseJSON.add(groupJSON);
		}
	    }
	}

	for (User user : users) {
	    if (!timeLimitAdjustments.containsKey(user.getUserId())) {
		// this format is required by jQuery UI autocomplete
		ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
		userJSON.put("value", "user-" + user.getUserId());

		String name = user.getFirstName() + " " + user.getLastName() + " (" + user.getLogin() + ")";
		if (grouping != null) {
		    Group group = grouping.getGroupBy(user);
		    if (group != null && !group.isNull()) {
			name += " - " + group.getGroupName();
		    }
		}

		userJSON.put("label", name);
		responseJSON.add(userJSON);
	    }
	}
	return responseJSON.toString();
    }

    @RequestMapping(path = "/getExistingIndividualTimeLimits", method = RequestMethod.GET)
    @ResponseBody
    public String getExistingIndividualTimeLimits(
	    @RequestParam(name = AssessmentConstants.PARAM_TOOL_CONTENT_ID) long toolContentId) {
	Assessment assessment = service.getAssessmentByContentId(toolContentId);
	Map<Integer, Integer> timeLimitAdjustments = assessment.getTimeLimitAdjustments();
	Grouping grouping = service.getGrouping(toolContentId);
	// find User objects based on their userIDs and sort by name
	List<User> users = assessment.getTimeLimitAdjustments().keySet().stream()
		.map(userId -> userManagementService.getUserById(userId)).sorted(USER_NAME_COMPARATOR)
		.collect(Collectors.toList());

	if (grouping != null) {
	    // Make a map group -> its users who have a time limit set
	    // key are sorted by group name, users in each group are sorted by name
	    List<User> groupedUsers = grouping.getGroups().stream()
		    .collect(Collectors.toMap(Group::getGroupName, group -> {
			return group.getUsers().stream()
				.filter(user -> timeLimitAdjustments.containsKey(user.getUserId()))
				.collect(Collectors.toCollection(() -> new TreeSet<>(USER_NAME_COMPARATOR)));
		    }, (s1, s2) -> {
			s1.addAll(s2);
			return s1;
		    }, TreeMap::new)).values().stream().flatMap(Set::stream).collect(Collectors.toList());

	    // from general user list remove grouped users
	    users.removeAll(groupedUsers);
	    // at the end of list, add remaining, not yet grouped users
	    groupedUsers.addAll(users);
	    users = groupedUsers;
	}

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	for (User user : users) {
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("userId", user.getUserId());
	    userJSON.put("adjustment", timeLimitAdjustments.get(user.getUserId().intValue()));

	    String name = user.getFirstName() + " " + user.getLastName() + " (" + user.getLogin() + ")";
	    if (grouping != null) {
		Group group = grouping.getGroupBy(user);
		if (group != null && !group.isNull()) {
		    name += " - " + group.getGroupName();
		}
	    }
	    userJSON.put("name", name);

	    responseJSON.add(userJSON);
	}
	return responseJSON.toString();
    }

    @RequestMapping(path = "/updateIndividualTimeLimit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateIndividualTimeLimit(
	    @RequestParam(name = AssessmentConstants.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam String itemId, @RequestParam(required = false) Integer adjustment) {
	Assessment assessment = service.getAssessmentByContentId(toolContentId);
	Map<Integer, Integer> timeLimitAdjustments = assessment.getTimeLimitAdjustments();
	Set<Integer> userIds = null;

	// itemId can user-<userId> or group-<groupId>
	String[] itemIdParts = itemId.split("-");
	if (itemIdParts[0].equalsIgnoreCase("group")) {
	    // add all users from a group, except for ones who are already added
	    Group group = (Group) userManagementService.findById(Group.class, Long.valueOf(itemIdParts[1]));
	    userIds = group.getUsers().stream().map(User::getUserId)
		    .filter(userId -> !timeLimitAdjustments.containsKey(userId)).collect(Collectors.toSet());
	} else {
	    // adjust for a single user
	    userIds = new HashSet<>();
	    userIds.add(Integer.valueOf(itemIdParts[1]));
	}

	for (Integer userId : userIds) {
	    if (adjustment == null) {
		timeLimitAdjustments.remove(userId);
	    } else {
		timeLimitAdjustments.put(userId, adjustment);
	    }
	}
	service.saveOrUpdateAssessment(assessment);
    }

    @RequestMapping(path = "/displayChangeLeaderForGroupDialogFromActivity")
    public String displayChangeLeaderForGroupDialogFromActivity(
	    // tell Change Leader dialog in Leader Selection tool which learner has already reached this activity
	    @RequestParam(name = AssessmentConstants.PARAM_TOOL_SESSION_ID) long toolSessionId) {
	String availableLearners = service.getUsersBySession(toolSessionId).stream()
		.collect(Collectors.mapping(user -> Long.toString(user.getUserId()), Collectors.joining(",")));

	return new StringBuilder("redirect:").append(Configuration.get(ConfigurationKeys.SERVER_URL))
		.append("tool/lalead11/monitoring/displayChangeLeaderForGroupDialogFromActivity.do?toolSessionId=")
		.append(toolSessionId).append("&availableLearners=").append(availableLearners).toString();
    }

    @RequestMapping(path = "/changeLeaderForGroup", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void changeLeaderForGroup(@RequestParam(name = AssessmentConstants.PARAM_TOOL_SESSION_ID) long toolSessionId,
	    @RequestParam long leaderUserId) {
	service.changeLeaderForGroup(toolSessionId, leaderUserId);
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }
}