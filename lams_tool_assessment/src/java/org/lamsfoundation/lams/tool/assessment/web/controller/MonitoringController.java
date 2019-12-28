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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentResultDTO;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.SessionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    public static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    @Qualifier("laasseAssessmentService")
    private IAssessmentService service;

    @RequestMapping("/summary")
    public String summary(HttpServletRequest request, HttpServletResponse response) {

	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<SessionDTO> sessionDtos = service.getSessionDtos(contentId, false);

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
	Set<AssessmentQuestion> questionList = new TreeSet<>();
	//in case there is at least one random question - we need to show all questions in a drop down select
	if (assessment.hasRandomQuestion()) {
	    questionList.addAll(assessment.getQuestions());

	    //show only questions from question list otherwise
	} else {
	    for (QuestionReference reference : (Set<QuestionReference>) assessment.getQuestionReferences()) {
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
	return "pages/monitoring/monitoring";
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
	return "pages/monitoring/parts/usersummary";
    }

    @RequestMapping(path = "/saveUserGrade", method = RequestMethod.POST)
    public void saveUserGrade(HttpServletRequest request, HttpServletResponse response) {

	if ((request.getParameter(AssessmentConstants.PARAM_NOT_A_NUMBER) == null)
		&& !StringUtils.isEmpty(request.getParameter(AssessmentConstants.PARAM_QUESTION_RESULT_UID))) {
	    Long questionResultUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_RESULT_UID);
	    float newGrade = Float.valueOf(request.getParameter(AssessmentConstants.PARAM_GRADE));
	    service.changeQuestionResultMark(questionResultUid, newGrade);
	}
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
    @RequestMapping("/setActivityEvaluation")
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

	Long sessionId = WebUtil.readLongParam(request, "sessionId");

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

		float assessmentResult = service.getLastTotalScoreByUser(assessment.getUid(), groupLeader.getUserId());
		Long portraitId = service.getPortraitId(groupLeader.getUserId());

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setUserId(groupLeader.getUserId());
		userDto.setFirstName(groupLeader.getFirstName());
		userDto.setLastName(groupLeader.getLastName());
		userDto.setGrade(assessmentResult);
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
	    userData.add(sessionId);
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
    @RequestMapping("/getUsersByQuestion")
    public String getUsersByQuestion(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);

	Long sessionId = WebUtil.readLongParam(request, "sessionId");
	Long questionUid = WebUtil.readLongParam(request, "questionUid");

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
			if (dbQuestionResult.getAssessmentQuestion().getUid().equals(questionUid)) {
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
		//LDEV_NTU-11 Swapping Mark and Response columns in Assessment Monitor
		userData.add(questionResult.getMark());
		// show confidence levels if this feature is turned ON
		if (assessment.isEnableConfidenceLevels()) {
		    userData.add(questionResult.getConfidenceLevel());
		}

		userData.add(AssessmentEscapeUtils.printResponsesForJqgrid(questionResult));
		if (userDto.getPortraitId() != null) {
		    userData.add(userDto.getPortraitId());
		}

	    } else {
		userData.add("");
		userData.add("");
		userData.add(fullName);
		userData.add("-");
		if (assessment.isEnableConfidenceLevels()) {
		    userData.add(-1);
		}
		userData.add("-");
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
     * Get the mark summary with data arranged in bands. Can be displayed graphically or in a table.
     */
    @RequestMapping("/getMarkChartData")
    public String getMarkChartData(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long contentId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = service.getAssessmentByContentId(contentId);
	List<Number> results = null;

	if (assessment != null) {
	    if (assessment.isUseSelectLeaderToolOuput()) {
		results = service.getMarksArrayForLeaders(contentId);
	    } else {
		Long sessionId = WebUtil.readLongParam(request, AssessmentConstants.ATTR_TOOL_SESSION_ID);
		results = service.getMarksArray(sessionId);
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
    @SuppressWarnings("unchecked")
    @RequestMapping("/exportSummary")
    public String exportSummary(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	String fileName = null;
	boolean showUserNames = true;

	Long contentId = null;
	List<SessionDTO> sessionDtos;
	if (sessionMapID != null) {
	    SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		    .getAttribute(sessionMapID);
	    request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    contentId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);
	    showUserNames = true;
	    sessionDtos = (List<SessionDTO>) sessionMap.get("sessionDtos");

	} else {
	    contentId = WebUtil.readLongParam(request, "toolContentID");
	    fileName = WebUtil.readStrParam(request, "fileName");
	    showUserNames = false;
	    sessionDtos = service.getSessionDtos(contentId, true);
	}

	Assessment assessment = service.getAssessmentByContentId(contentId);
	if (assessment == null) {
	    return null;
	}

	List<ExcelSheet> sheets = service.exportSummary(assessment, sessionDtos, showUserNames);

	// Setting the filename if it wasn't passed in the request
	if (fileName == null) {
	    fileName = "assessment_" + assessment.getUid() + "_export.xlsx";
	}

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	log.debug("Exporting assessment to a spreadsheet: " + assessment.getContentId());

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, service.getMessage("label.export.exported.on"), true);

	return null;
    }

    @RequestMapping("/statistic")
    public String statistic(HttpServletRequest request, HttpServletResponse response) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = service.getAssessmentByContentId(contentId);
	if (assessment != null) {
	    if (assessment.isUseSelectLeaderToolOuput()) {
		LeaderResultsDTO leaderDto = service.getLeaderResultsDTOForLeaders(contentId);
		sessionMap.put("leaderDto", leaderDto);
	    } else {
		List<SessionDTO> sessionDtos = service.getSessionDtos(contentId, true);
		sessionMap.put("sessionDtos", sessionDtos);
	    }
	}
	return "pages/monitoring/statisticpart";
    }

    /**
     * Allows displaying correct answers to learners
     */
    @RequestMapping(path = "/discloseCorrectAnswers", method = RequestMethod.POST)
    public void discloseCorrectAnswers(HttpServletRequest request, HttpServletResponse response) {
	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	Long toolContentId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_TOOL_CONTENT_ID);

	AssessmentQuestion question = service.getAssessmentQuestionByUid(questionUid);
	question.setCorrectAnswersDisclosed(true);
	service.updateAssessmentQuestion(question);

	service.notifyLearnersOnAnswerDisclose(toolContentId);

	if (log.isDebugEnabled()) {
	    log.debug("Disclosed correct answers for Assessment tool content ID " + toolContentId + " and question ID "
		    + questionUid);
	}
    }

    /**
     * Allows displaying other groups' answers to learners
     */
    @RequestMapping(path = "/discloseGroupsAnswers", method = RequestMethod.POST)
    public void discloseGroupsAnswers(HttpServletRequest request, HttpServletResponse response) {
	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	Long toolContentId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_TOOL_CONTENT_ID);

	AssessmentQuestion question = service.getAssessmentQuestionByUid(questionUid);
	question.setGroupsAnswersDisclosed(true);
	service.updateAssessmentQuestion(question);

	service.notifyLearnersOnAnswerDisclose(toolContentId);

	if (log.isDebugEnabled()) {
	    log.debug("Disclosed other groups' answers for Assessment tool content ID " + toolContentId
		    + " and question ID " + questionUid);
	}
    }
    
    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    } 

}
