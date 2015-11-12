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
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionSummary;
import org.lamsfoundation.lams.tool.assessment.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.assessment.dto.SessionDTO;
import org.lamsfoundation.lams.tool.assessment.dto.UserSummary;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentServiceImpl;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentEscapeUtils;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    public static final ExcelCell[] EMPTY_ROW = new ExcelCell[0];

    private IAssessmentService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	String param = mapping.getParameter();
	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}
	if (param.equals("userMasterDetail")) {
	    return userMasterDetail(mapping, form, request, response);
	}
	if (param.equals("questionSummary")) {
	    return questionSummary(mapping, form, request, response);
	}
	if (param.equals("userSummary")) {
	    return userSummary(mapping, form, request, response);
	}
	if (param.equals("saveUserGrade")) {
	    return saveUserGrade(mapping, form, request, response);
	}
	if (param.equals("setSubmissionDeadline")) {
	    return setSubmissionDeadline(mapping, form, request, response);
	}
	if (param.equals("getUsers")) {
	    return getUsers(mapping, form, request, response);
	}
	if (param.equals("getUsersByQuestion")) {
	    return getUsersByQuestion(mapping, form, request, response);
	}
	if (param.equals("exportSummary")) {
	    return exportSummary(mapping, form, request, response);
	}

	return mapping.findForward(AssessmentConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initAssessmentService();
	
	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<SessionDTO> sessionDtos = service.getSessionDtos(contentId);

	Assessment assessment = service.getAssessmentByContentId(contentId);
	
	//set SubmissionDeadline, if any
	if (assessment.getSubmissionDeadline() != null) {
	    Date submissionDeadline = assessment.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(AssessmentConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}
	
	// Create reflectList if reflection is enabled.
	if (assessment.isReflectOnActivity()) {
	    List<ReflectDTO> reflectList = service.getReflectList(assessment.getContentId());
	    // Add reflectList to sessionMap
	    sessionMap.put(AssessmentConstants.ATTR_REFLECT_LIST, reflectList);
	}
	
	//create list of questions to display in question drop down menu
	Set<AssessmentQuestion> questionList = new TreeSet<AssessmentQuestion>();
	boolean hasRandomQuestion = false;
	for (QuestionReference reference : (Set<QuestionReference>)assessment.getQuestionReferences()) {
	    hasRandomQuestion |= reference.isRandomQuestion();
	}
	//in case there is at least one random question - we need to show all questions in a drop down select
	if (hasRandomQuestion) {
	    questionList.addAll(assessment.getQuestions());
	
	//show only questions from question list otherwise
	} else {
	    for (QuestionReference reference : (Set<QuestionReference>) assessment.getQuestionReferences()) {
		questionList.add(reference.getQuestion());
	    }
	}

	// cache into sessionMap
	boolean isGroupedActivity = service.isGroupedActivity(contentId);
	sessionMap.put(AssessmentConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	sessionMap.put("sessionDtos", sessionDtos);
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, assessment);
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_LIST, questionList);
	sessionMap.put(AssessmentConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, WebUtil.readStrParam(request,
		AttributeNames.PARAM_CONTENT_FOLDER_ID));
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    private ActionForward userMasterDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initAssessmentService();
	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	Long sessionId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_SESSION_ID);
	AssessmentResult result = service.getUserMasterDetail(sessionId, userId);

	request.setAttribute(AssessmentConstants.ATTR_ASSESSMENT_RESULT, result);
	return (result == null) ? null : mapping.findForward(AssessmentConstants.SUCCESS);
    }

    private ActionForward questionSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initAssessmentService();
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long questionUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_UID);
	if (questionUid.equals(-1)) {
	    return null;
	}
	Long contentId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);
	QuestionSummary questionSummary = service.getQuestionSummary(contentId, questionUid);

	request.setAttribute(AssessmentConstants.ATTR_QUESTION_SUMMARY, questionSummary);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    private ActionForward userSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initAssessmentService();
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long userId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	Long sessionId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_SESSION_ID);
	Long contentId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);
	UserSummary userSummary = service.getUserSummary(contentId, userId, sessionId);

	request.setAttribute(AssessmentConstants.ATTR_USER_SUMMARY, userSummary);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    private ActionForward saveUserGrade(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if ((request.getParameter(AssessmentConstants.PARAM_NOT_A_NUMBER) == null)
		&& !StringUtils.isEmpty(request.getParameter(AssessmentConstants.PARAM_QUESTION_RESULT_UID))) {
	    initAssessmentService();
	    Long questionResultUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_RESULT_UID);
	    float newGrade = Float.valueOf(request.getParameter(AssessmentConstants.PARAM_GRADE));
	    service.changeQuestionResultMark(questionResultUid, newGrade);
	}

	return null;
    }
    
    /**
     * Set Submission Deadline
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	initAssessmentService();
	
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = service.getAssessmentByContentId(contentID);
	
	Long dateParameter = WebUtil.readLongParam(request, AssessmentConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	assessment.setSubmissionDeadline(tzSubmissionDeadline);
	service.saveOrUpdateAssessment(assessment);

	return null;
    }
    
    /**
     * Refreshes user list.
     */
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	initAssessmentService();
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	
	Long sessionId = WebUtil.readLongParam(request, "sessionId");
	
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradebookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradebookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradebookConstants.PARAM_SIDX, true);
	if (sortBy == "") {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);
	
	List<AssessmentUserDTO> userDtos = new ArrayList<AssessmentUserDTO>();
	int countSessionUsers = 0;
	//in case of UseSelectLeaderToolOuput - display only one user
	if (assessment.isUseSelectLeaderToolOuput()) {
	    
	    AssessmentSession session = service.getAssessmentSessionBySessionId(sessionId);
	    AssessmentUser groupLeader = session.getGroupLeader();
	    
	    if (groupLeader != null) {

		float assessmentResult = service.getLastFinishedAssessmentResultGrade(assessment.getUid(),
			groupLeader.getUserId());

		AssessmentUserDTO userDto = new AssessmentUserDTO();
		userDto.setUserId(groupLeader.getUserId());
		userDto.setFirstName(groupLeader.getFirstName());
		userDto.setLastName(groupLeader.getLastName());
		userDto.setGrade(assessmentResult);
		userDtos.add(userDto);
		countSessionUsers = 1;
	    }
	    
	} else {
	    // Get the user list from the db
	    userDtos = service.getPagedUsersBySession(sessionId, page - 1, rowLimit, sortBy, sortOrder, searchString);
	    countSessionUsers = service.getCountUsersBySession(sessionId, searchString);
	}

	int totalPages = new Double(Math.ceil(new Integer(countSessionUsers).doubleValue()
		/ new Integer(rowLimit).doubleValue())).intValue();

	JSONArray rows = new JSONArray();
	int i = 1;
	for (AssessmentUserDTO userDto : userDtos) {

	    JSONArray userData = new JSONArray();
	    userData.put(i);
	    userData.put(userDto.getUserId());
	    userData.put(sessionId);
	    String fullName = StringEscapeUtils.escapeHtml(userDto.getFirstName() + " "
		    + userDto.getLastName());
	    userData.put(fullName);
	    userData.put(userDto.getGrade());

	    JSONObject userRow = new JSONObject();
	    userRow.put("id", i++);
	    userRow.put("cell", userData);

	    rows.put(userRow);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.put("rows", rows);
	
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responseJSON.toString()));
	return null;
     }
    
    /**
     * Refreshes user list.
     */
    public ActionForward getUsersByQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	initAssessmentService();
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	
	Long sessionId = WebUtil.readLongParam(request, "sessionId");
	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradebookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradebookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradebookConstants.PARAM_SIDX, true);
	if (sortBy == "") {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);
	
	List<AssessmentUserDTO> userDtos = new ArrayList<AssessmentUserDTO>();
	int countSessionUsers = 0;
	//in case of UseSelectLeaderToolOuput - display only one user
	if (assessment.isUseSelectLeaderToolOuput()) {
	    
	    AssessmentSession session = service.getAssessmentSessionBySessionId(sessionId);
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
	    userDtos = service.getPagedUsersBySessionAndQuestion(sessionId, questionUid, page - 1, rowLimit, sortBy, sortOrder, searchString);
	    countSessionUsers = service.getCountUsersBySession(sessionId, searchString);
	}

	int totalPages = new Double(Math.ceil(new Integer(countSessionUsers).doubleValue()
		/ new Integer(rowLimit).doubleValue())).intValue();

	JSONArray rows = new JSONArray();
	int i = 1;
	for (AssessmentUserDTO userDto : userDtos) {
	    
	    Long questionResultUid = userDto.getQuestionResultUid();
	    String fullName = StringEscapeUtils.escapeHtml(userDto.getFirstName() + " "
		    + userDto.getLastName());
	    
	    JSONArray userData = new JSONArray();
	    if (questionResultUid != null) {
		AssessmentQuestionResult questionResult = service.getAssessmentQuestionResultByUid(questionResultUid);
		
		userData.put(questionResultUid);
		userData.put(questionResult.getMaxMark());
		userData.put(fullName);
		userData.put(AssessmentEscapeUtils.printResponsesForJqgrid(questionResult));
		userData.put(questionResult.getMark());
		
	    } else {
		userData.put("");
		userData.put("");
		userData.put(fullName);
		userData.put("-");
		userData.put("-");
	    }

	    JSONObject userRow = new JSONObject();
	    userRow.put("id", i++);
	    userRow.put("cell", userData);

	    rows.put(userRow);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.put("rows", rows);
	
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responseJSON.toString()));
	return null;
    }

    /**
     * Export Excel format survey data.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    private ActionForward exportSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	initAssessmentService();
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	String fileName = null;
	boolean showUserNames = true;

	Long contentId = null;
	if (sessionMap != null) {
	    request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	    contentId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_CONTENT_ID);
	    showUserNames = true;
	} else {
	    contentId = WebUtil.readLongParam(request, "toolContentID");
	    fileName = WebUtil.readStrParam(request, "fileName");
	    showUserNames = false;
	}

	Assessment assessment = service.getAssessmentByContentId(contentId);
	
	if (assessment != null) {
	    LinkedHashMap<String, ExcelCell[][]> dataToExport = new LinkedHashMap<String, ExcelCell[][]>();

	    ExcelCell[][] questionSummaryData = getQuestionSummaryData(assessment, showUserNames);
	    dataToExport.put(service.getMessage("lable.export.summary.by.question"), questionSummaryData);

	    ExcelCell[][] userSummaryData = getUserSummaryData(assessment, showUserNames);
	    dataToExport.put(service.getMessage("label.export.summary.by.user"), userSummaryData);

	    // Setting the filename if it wasn't passed in the request
	    if (fileName == null) {
		fileName = "assessment_" + assessment.getUid() + "_export.xlsx";
	    }

	    response.setContentType("application/x-download");
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	    log.debug("Exporting assessment to a spreadsheet: " + assessment.getContentId());

	    ServletOutputStream out = response.getOutputStream();
	    ExcelUtil.createExcel(out, dataToExport, service.getMessage("label.export.exported.on"), true);
	}
	
	return null;
    }

    @SuppressWarnings("unchecked")
    private ExcelCell[][] getUserSummaryData(Assessment assessment, boolean showUserNames) {
	initAssessmentService();
	ArrayList<ExcelCell[]> data = new ArrayList<ExcelCell[]>();

	if (assessment != null) {
	    // Create the question summary
	    ExcelCell[] summaryTitle = new ExcelCell[1];
	    summaryTitle[0] = new ExcelCell(service.getMessage("label.export.user.summary"), true);
	    data.add(summaryTitle);

	    // Adding the question summary -------------------------------------
	    ExcelCell[] summaryRowTitle = new ExcelCell[5];
	    summaryRowTitle[0] = new ExcelCell(service.getMessage("label.monitoring.question.summary.question"), true);
	    summaryRowTitle[1] = new ExcelCell(service.getMessage("label.authoring.basic.list.header.type"), true);
	    summaryRowTitle[2] = new ExcelCell(service.getMessage("label.authoring.basic.penalty.factor"), true);
	    summaryRowTitle[3] = new ExcelCell(service.getMessage("label.monitoring.question.summary.default.mark"),
		    true);
	    summaryRowTitle[4] = new ExcelCell(service.getMessage("label.monitoring.question.summary.average.mark"),
		    true);
	    data.add(summaryRowTitle);
	    Float totalGradesPossible = new Float(0);
	    Float totalAverage = new Float(0);
	    if (assessment.getQuestionReferences() != null) {
		Set<QuestionReference> questionReferences = new TreeSet<QuestionReference>(new SequencableComparator());
		questionReferences.addAll(assessment.getQuestionReferences());

		int randomQuestionsCount = 1;
		for (QuestionReference questionReference : questionReferences) {
		    
		    String title;
		    String questionType;
		    Float penaltyFactor;
		    Float averageMark = null;
		    if (questionReference.isRandomQuestion()) {

			title = service.getMessage("label.authoring.basic.type.random.question")
				+ randomQuestionsCount++;
			questionType = service.getMessage("label.authoring.basic.type.random.question");
			penaltyFactor = null;
			averageMark = null;
		    } else {

			AssessmentQuestion question = questionReference.getQuestion();
			title = question.getTitle();
			questionType = getQuestionTypeLanguageLabel(question.getType());
			penaltyFactor = question.getPenaltyFactor();

			QuestionSummary questionSummary = service.getQuestionDataForExport(assessment.getContentId(),
				question.getUid());
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

		    data.add(questCell);

		}
		
		if (totalGradesPossible.floatValue() > 0) {
		    ExcelCell[] totalCell = new ExcelCell[5];
		    totalCell[2] = new ExcelCell(service.getMessage("label.monitoring.summary.total"), true);
		    totalCell[3] = new ExcelCell(totalGradesPossible, false);
		    totalCell[4] = new ExcelCell(totalAverage, false);
		    data.add(totalCell);
		}
		data.add(EMPTY_ROW);
	    }
	    //------------------------------------------------------------------

	    // Adding the user results/marks summary ---------------------------

	    List<SessionDTO> summaryList = service.getSessionDataForExport(assessment.getContentId());
	    if (summaryList != null) {
		for (SessionDTO sessionDTO : summaryList) {

		    data.add(EMPTY_ROW);

		    ExcelCell[] sessionTitle = new ExcelCell[1];
		    sessionTitle[0] = new ExcelCell(sessionDTO.getSessionName(), true);
		    data.add(sessionTitle);

		    //List<AssessmentResult> assessmentResults = summary.getAssessmentResults();

		    AssessmentSession assessmentSession = service.getAssessmentSessionBySessionId(sessionDTO
			    .getSessionId());

		    Set<AssessmentUser> assessmentUsers = assessmentSession.getAssessmentUsers();

		    if (assessmentUsers != null) {

			for (AssessmentUser assessmentUser : assessmentUsers) {

			    if (showUserNames) {
				ExcelCell[] userTitleRow = new ExcelCell[6];
				userTitleRow[0] = new ExcelCell(service.getMessage("label.export.user.id"), true);
				userTitleRow[1] = new ExcelCell(service
					.getMessage("label.monitoring.user.summary.user.name"), true);
				userTitleRow[2] = new ExcelCell(service.getMessage("label.export.date.attempted"), true);
				userTitleRow[3] = new ExcelCell(service
					.getMessage("label.monitoring.question.summary.question"), true);
				userTitleRow[4] = new ExcelCell(service
					.getMessage("label.authoring.basic.option.answer"), true);
				userTitleRow[5] = new ExcelCell(service.getMessage("label.export.mark"), true);
				data.add(userTitleRow);
			    } else {
				ExcelCell[] userTitleRow = new ExcelCell[5];
				userTitleRow[0] = new ExcelCell(service.getMessage("label.export.user.id"), true);
				userTitleRow[1] = new ExcelCell(service.getMessage("label.export.date.attempted"), true);
				userTitleRow[2] = new ExcelCell(service
					.getMessage("label.monitoring.question.summary.question"), true);
				userTitleRow[3] = new ExcelCell(service
					.getMessage("label.authoring.basic.option.answer"), true);
				userTitleRow[4] = new ExcelCell(service.getMessage("label.export.mark"), true);
				data.add(userTitleRow);
			    }

			    AssessmentResult assessmentResult = service.getLastFinishedAssessmentResult(assessment.getUid(),
				    assessmentUser.getUserId());

			    if (assessmentResult != null) {
				Set<AssessmentQuestionResult> questionResults = assessmentResult
					.getQuestionResults();

				if (questionResults != null) {

				    for (AssessmentQuestionResult questionResult : questionResults) {

					if (showUserNames) {
					    ExcelCell[] userResultRow = new ExcelCell[6];
					    userResultRow[0] = new ExcelCell(assessmentUser.getUserId(), false);
					    userResultRow[1] = new ExcelCell(assessmentUser.getFullName(), false);
					    userResultRow[2] = new ExcelCell(assessmentResult.getStartDate(), false);
					    userResultRow[3] = new ExcelCell(questionResult
						    .getAssessmentQuestion().getTitle(), false);
					    userResultRow[4] = new ExcelCell(AssessmentEscapeUtils.printResponsesForExcelExport(questionResult),
						    false);
					    userResultRow[5] = new ExcelCell(questionResult.getMark(), false);
					    data.add(userResultRow);
					} else {
					    ExcelCell[] userResultRow = new ExcelCell[5];
					    userResultRow[0] = new ExcelCell(assessmentUser.getUserId(), false);
					    userResultRow[1] = new ExcelCell(assessmentResult.getStartDate(), false);
					    userResultRow[2] = new ExcelCell(questionResult
						    .getAssessmentQuestion().getTitle(), false);
					    userResultRow[3] = new ExcelCell(AssessmentEscapeUtils.printResponsesForExcelExport(questionResult),
						    false);
					    userResultRow[4] = new ExcelCell(questionResult.getMark(), false);
					    data.add(userResultRow);
					}
				    }
				}

				ExcelCell[] userTotalRow;
				if (showUserNames) {
				    userTotalRow = new ExcelCell[6];
				    userTotalRow[4] = new ExcelCell(service
					    .getMessage("label.monitoring.summary.total"), true);
				    userTotalRow[5] = new ExcelCell(assessmentResult.getGrade(), false);
				} else {
				    userTotalRow = new ExcelCell[5];
				    userTotalRow[3] = new ExcelCell(service
					    .getMessage("label.monitoring.summary.total"), true);
				    userTotalRow[4] = new ExcelCell(assessmentResult.getGrade(), false);
				}

				data.add(userTotalRow);
				data.add(EMPTY_ROW);
			    }
			}
		    }
		}
	    }

	    //------------------------------------------------------------------

	}

	return data.toArray(new ExcelCell[][] {});
    }

    @SuppressWarnings("unchecked")
    private ExcelCell[][] getQuestionSummaryData(Assessment assessment, boolean showUserNames) {
	initAssessmentService();
	ArrayList<ExcelCell[]> data = new ArrayList<ExcelCell[]>();

	if (assessment != null) {
	    // Create the question summary
	    ExcelCell[] summaryTitle = new ExcelCell[1];
	    summaryTitle[0] = new ExcelCell(service.getMessage("label.export.question.summary"), true);
	    data.add(summaryTitle);

	    if (assessment.getQuestions() != null) {
		Set<AssessmentQuestion> questions = (Set<AssessmentQuestion>) assessment.getQuestions();

		for (AssessmentQuestion question : questions) {

		    // Adding the question summary 
		    if (showUserNames) {
			ExcelCell[] summaryRowTitle = new ExcelCell[10];
			summaryRowTitle[0] = new ExcelCell(service
				.getMessage("label.monitoring.question.summary.question"), true);
			summaryRowTitle[1] = new ExcelCell(
				service.getMessage("label.authoring.basic.list.header.type"), true);
			summaryRowTitle[2] = new ExcelCell(service.getMessage("label.authoring.basic.penalty.factor"),
				true);
			summaryRowTitle[3] = new ExcelCell(service
				.getMessage("label.monitoring.question.summary.default.mark"), true);
			summaryRowTitle[4] = new ExcelCell(service.getMessage("label.export.user.id"), true);
			summaryRowTitle[5] = new ExcelCell(service
				.getMessage("label.monitoring.user.summary.user.name"), true);
			summaryRowTitle[6] = new ExcelCell(service.getMessage("label.export.date.attempted"), true);
			summaryRowTitle[7] = new ExcelCell(service.getMessage("label.authoring.basic.option.answer"),
				true);
			summaryRowTitle[8] = new ExcelCell(service.getMessage("label.export.time.taken"), true);
			summaryRowTitle[9] = new ExcelCell(service.getMessage("label.export.mark"), true);
			data.add(summaryRowTitle);
		    } else {
			ExcelCell[] summaryRowTitle = new ExcelCell[9];
			summaryRowTitle[0] = new ExcelCell(service
				.getMessage("label.monitoring.question.summary.question"), true);
			summaryRowTitle[1] = new ExcelCell(
				service.getMessage("label.authoring.basic.list.header.type"), true);
			summaryRowTitle[2] = new ExcelCell(service.getMessage("label.authoring.basic.penalty.factor"),
				true);
			summaryRowTitle[3] = new ExcelCell(service
				.getMessage("label.monitoring.question.summary.default.mark"), true);
			summaryRowTitle[4] = new ExcelCell(service.getMessage("label.export.user.id"), true);
			summaryRowTitle[5] = new ExcelCell(service.getMessage("label.export.date.attempted"), true);
			summaryRowTitle[6] = new ExcelCell(service.getMessage("label.authoring.basic.option.answer"),
				true);
			summaryRowTitle[7] = new ExcelCell(service.getMessage("label.export.time.taken"), true);
			summaryRowTitle[8] = new ExcelCell(service.getMessage("label.export.mark"), true);
			data.add(summaryRowTitle);
		    }

		    QuestionSummary questionSummary = service.getQuestionDataForExport(assessment.getContentId(), question
			    .getUid());

		    List<List<AssessmentQuestionResult>> allResultsForQuestion = questionSummary
			    .getQuestionResultsPerSession();

		    int markCount = 0;
		    Float markTotal = new Float(0.0);
		    int timeTakenCount = 0;
		    int timeTakenTotal = 0;
		    for (List<AssessmentQuestionResult> resultList : allResultsForQuestion) {
			for (AssessmentQuestionResult questionResult : resultList) {

			    if (showUserNames) {
				ExcelCell[] userResultRow = new ExcelCell[10];
				userResultRow[0] = new ExcelCell(questionResult.getAssessmentQuestion().getTitle(),
					false);
				userResultRow[1] = new ExcelCell(getQuestionTypeLanguageLabel(questionResult
					.getAssessmentQuestion().getType()), false);
				userResultRow[2] = new ExcelCell(new Float(questionResult.getAssessmentQuestion()
					.getPenaltyFactor()), false);
				Float maxMark = (questionResult.getMaxMark() == null) ? 0 : new Float(
					questionResult.getMaxMark());
				userResultRow[3] = new ExcelCell(maxMark, false);
				userResultRow[4] = new ExcelCell(questionResult.getUser().getUserId(), false);
				userResultRow[5] = new ExcelCell(questionResult.getUser().getFullName(), false);
				userResultRow[6] = new ExcelCell(questionResult.getFinishDate(), false);
				userResultRow[7] = new ExcelCell(AssessmentEscapeUtils.printResponsesForExcelExport(questionResult), false);

				AssessmentResult assessmentResult = questionResult.getAssessmentResult();
				Date finishDate = questionResult.getFinishDate();
				if (assessmentResult != null && finishDate != null) {
				    Date startDate = assessmentResult.getStartDate();
				    if (startDate != null) {
					Long seconds = (finishDate.getTime() - startDate.getTime()) / 1000;
					userResultRow[8] = new ExcelCell(seconds, false);
					timeTakenCount++;
					timeTakenTotal += seconds;
				    }
				}

				Float mark = questionResult.getMark();
				if (mark != null) {
				    userResultRow[9] = new ExcelCell(questionResult.getMark(), false);
				    markCount++;
				    markTotal += questionResult.getMark();
				}

				data.add(userResultRow);
			    } else {
				ExcelCell[] userResultRow = new ExcelCell[9];
				userResultRow[0] = new ExcelCell(questionResult.getAssessmentQuestion().getTitle(),
					false);
				userResultRow[1] = new ExcelCell(getQuestionTypeLanguageLabel(questionResult
					.getAssessmentQuestion().getType()), false);
				userResultRow[2] = new ExcelCell(new Float(questionResult.getAssessmentQuestion()
					.getPenaltyFactor()), false);
				Float maxMark = (questionResult.getMaxMark() == null) ? 0 : new Float(
					questionResult.getMaxMark());
				userResultRow[3] = new ExcelCell(maxMark, false);
				userResultRow[4] = new ExcelCell(questionResult.getUser().getUserId(), false);
				userResultRow[5] = new ExcelCell(questionResult.getFinishDate(), false);
				userResultRow[6] = new ExcelCell(AssessmentEscapeUtils.printResponsesForExcelExport(questionResult), false);

				if (questionResult.getAssessmentResult() != null) {
				    Date startDate = questionResult.getAssessmentResult().getStartDate();
				    Date finishDate = questionResult.getFinishDate();
				    if (startDate != null && finishDate != null) {
					Long seconds = (finishDate.getTime() - startDate.getTime()) / 1000;
					userResultRow[7] = new ExcelCell(seconds, false);
					timeTakenCount++;
					timeTakenTotal += seconds;
				    }
				}

				userResultRow[8] = new ExcelCell(questionResult.getMark(), false);

				if (questionResult.getMark() != null) {
				    markCount++;
				    markTotal += questionResult.getMark();
				}

				data.add(userResultRow);
			    }

			}
		    }

		    // Calculating the averages
		    ExcelCell[] averageRow;

		    if (showUserNames) {
			averageRow = new ExcelCell[10];

			averageRow[7] = new ExcelCell(service.getMessage("label.export.average"), true);

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
			averageRow[6] = new ExcelCell(service.getMessage("label.export.average"), true);

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

		    data.add(averageRow);
		    data.add(EMPTY_ROW);
		}

	    }
	}

	return data.toArray(new ExcelCell[][] {});
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

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private IAssessmentService initAssessmentService() {
	if (service == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    service = (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
	}
	return service;
    }

}
