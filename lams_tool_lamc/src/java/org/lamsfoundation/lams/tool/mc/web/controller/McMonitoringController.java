/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.mc.dto.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.dto.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.dto.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McOptsContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.util.McSessionComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
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

/**
 * * @author Ozgur Demirtas
 */
@Controller
@RequestMapping("/monitoring")
public class McMonitoringController {

    private static Logger logger = Logger.getLogger(McMonitoringController.class.getName());

    @Autowired
    private IMcService mcService;

    @Autowired
    @Qualifier("lamcMessageService")
    private MessageService messageService;

    @RequestMapping("/monitoring")
    public String execute(HttpServletRequest request) {

	McGeneralMonitoringDTO mcGeneralMonitoringDTO = new McGeneralMonitoringDTO();

	String toolContentID = null;
	try {
	    Long toolContentIDLong = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	    toolContentID = toolContentIDLong.toString();
	} catch (IllegalArgumentException e) {
	    logger.error("Unable to start monitoring as tool content id is missing");
	    throw (e);
	}

	McContent mcContent = mcService.getMcContent(new Long(toolContentID));
	mcGeneralMonitoringDTO.setToolContentID(toolContentID);
	mcGeneralMonitoringDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralMonitoringDTO.setActivityInstructions(mcContent.getInstructions());

	//set up sessionDTOs list
	Set<McSession> sessions = new TreeSet<>(new McSessionComparator());
	sessions.addAll(mcContent.getMcSessions());
	List<SessionDTO> sessionDtos = new LinkedList<>();
	for (McSession session : sessions) {
	    SessionDTO sessionDto = new SessionDTO();
	    sessionDto.setSessionId(session.getMcSessionId());
	    sessionDto.setSessionName(session.getSession_name());

	    sessionDtos.add(sessionDto);
	}
	request.setAttribute(McAppConstants.SESSION_DTOS, sessionDtos);

	// setting up the advanced summary

	request.setAttribute(McAppConstants.ATTR_CONTENT, mcContent);
	request.setAttribute("questionsSequenced", mcContent.isQuestionsSequenced());
	request.setAttribute("enableConfidenceLevels", mcContent.isEnableConfidenceLevels());
	request.setAttribute("showMarks", mcContent.isShowMarks());
	request.setAttribute("useSelectLeaderToolOuput", mcContent.isUseSelectLeaderToolOuput());
	request.setAttribute("prefixAnswersWithLetters", mcContent.isPrefixAnswersWithLetters());
	request.setAttribute("randomize", mcContent.isRandomize());
	request.setAttribute("displayAnswers", mcContent.isDisplayAnswers());
	request.setAttribute("displayFeedbackOnly", mcContent.isDisplayFeedbackOnly());
	request.setAttribute("retries", mcContent.isRetries());
	request.setAttribute("reflect", mcContent.isReflect());
	request.setAttribute("reflectionSubject", mcContent.getReflectionSubject());
	request.setAttribute("passMark", mcContent.getPassMark());
	request.setAttribute("toolContentID", mcContent.getMcContentId());
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	// setting up Date and time restriction in activities
	HttpSession ss = SessionManager.getSession();
	Date submissionDeadline = mcContent.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    request.setAttribute(McAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(McAppConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	boolean isGroupedActivity = mcService.isGroupedActivity(new Long(mcContent.getMcContentId()));
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	/* this section is needed for Edit Activity screen, from here... */

	mcGeneralMonitoringDTO.setDisplayAnswers(new Boolean(mcContent.isDisplayAnswers()).toString());
	mcGeneralMonitoringDTO.setDisplayFeedbackOnly(new Boolean(mcContent.isDisplayFeedbackOnly()).toString());

	List<ReflectionDTO> reflectionsContainerDTO = mcService.getReflectionList(mcContent, null);
	request.setAttribute(McAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	request.setAttribute(McAppConstants.MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);

	//count users
	int countSessionComplete = 0;
	int countAllUsers = 0;
	Iterator iteratorSession = mcContent.getMcSessions().iterator();
	while (iteratorSession.hasNext()) {
	    McSession mcSession = (McSession) iteratorSession.next();

	    if (mcSession != null) {

		if (mcSession.getSessionStatus().equals(McAppConstants.COMPLETED)) {
		    countSessionComplete++;
		}
		countAllUsers += mcSession.getMcQueUsers().size();
	    }
	}
	mcGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers));
	mcGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete));

	return "monitoring/MonitoringMaincontent";
    }

    /**
     * Turn on displayAnswers
     */
    @RequestMapping("/displayAnswers")
    public String displayAnswers(HttpServletRequest request) {

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));
	mcContent.setDisplayAnswers(new Boolean(true));
	mcContent.setDisplayFeedbackOnly(new Boolean(false));
	mcService.updateMc(mcContent);

	// use redirect to prevent resubmition of the same request
	String redirect = "redirect:/monitoring/monitoring.do";
	redirect = WebUtil.appendParameterToURL(redirect, McAppConstants.TOOL_CONTENT_ID, strToolContentID);
	redirect = WebUtil.appendParameterToURL(redirect, AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return redirect;
    }

    /**
     * Turn on displayFeedbackOnly
     */
    @RequestMapping("/displayFeedbackOnly")
    public String displayFeedbackOnly(HttpServletRequest request) {

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));
	mcContent.setDisplayFeedbackOnly(new Boolean(true));
	mcService.updateMc(mcContent);

	// use redirect to prevent resubmition of the same request
	String redirect = "redirect:/monitoring/monitoring.do";
	redirect = WebUtil.appendParameterToURL(redirect, McAppConstants.TOOL_CONTENT_ID, strToolContentID);
	redirect = WebUtil.appendParameterToURL(redirect, AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return redirect;
    }

    /**
     * allows viewing users reflection data
     */
    @RequestMapping("/openNotebook")
    public String openNotebook(HttpServletRequest request) {

	String userId = request.getParameter("userId");
	String userName = request.getParameter("userName");
	String sessionId = request.getParameter("sessionId");
	NotebookEntry notebookEntry = mcService.getEntry(new Long(sessionId), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.TOOL_SIGNATURE, new Integer(userId));

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = new McGeneralLearnerFlowDTO();
	if (notebookEntry != null) {
	    // String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntry.getEntry());
	    mcGeneralLearnerFlowDTO.setUserName(userName);
	}

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	return "monitoring/LearnerNotebook";
    }

    /**
     * downloadMarks
     */
    @RequestMapping(path = "/downloadMarks", method = RequestMethod.POST)
    public String downloadMarks(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	McContent mcContent = mcService.getMcContent(toolContentID);

	byte[] spreadsheet = null;
	try {
	    spreadsheet = mcService.prepareSessionDataSpreadsheet(mcContent);
	} catch (Exception e) {
	    logger.error("Error preparing spreadsheet: ", e);
	    request.setAttribute("errorName", messageService.getMessage("error.monitoring.spreadsheet.download"));
	    request.setAttribute("errorMessage", e);
	    return "error";
	}

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	// construct download file response header
	OutputStream out = response.getOutputStream();
	String fileName = "lams_mcq.xls";
	String mineType = "application/vnd.ms-excel";
	String header = "attachment; filename=\"" + fileName + "\";";
	response.setContentType(mineType);
	response.setHeader("Content-Disposition", header);

	// write response
	try {
	    out.write(spreadsheet);
	    out.flush();
	} finally {
	    try {
		if (out != null) {
		    out.close();
		}
	    } catch (IOException e) {
	    }
	}

	return null;
    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(contentID);

	Long dateParameter = WebUtil.readLongParam(request, McAppConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	mcContent.setSubmissionDeadline(tzSubmissionDeadline);
	mcService.updateMc(mcContent);
	
	return formattedDate;
    }

    /**
     * Set tool's activityEvaluation
     */
    @RequestMapping("/setActivityEvaluation")
    @ResponseBody
    public String setActivityEvaluation(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String activityEvaluation = WebUtil.readStrParam(request, McAppConstants.ATTR_ACTIVITY_EVALUATION);
	mcService.setActivityEvaluation(contentID, activityEvaluation);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("success", "true");
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    /**
     * Populate user jqgrid table on summary page.
     */
    @RequestMapping("/userMasterDetail")
    public String userMasterDetail(HttpServletRequest request) {

	Long userUid = WebUtil.readLongParam(request, McAppConstants.USER_UID);
	McQueUsr user = mcService.getMcUserByUID(userUid);
	List<McUsrAttempt> userAttempts = mcService.getFinalizedUserAttempts(user);

	// Escapes all characters that may brake JS code on assigning Java value to JS String variable (particularly
	// escapes all quotes in the following way \").
	if (userAttempts != null) {
	    for (McUsrAttempt userAttempt : userAttempts) {
		McQueContent question = userAttempt.getMcQueContent();
		McOptsContent option = userAttempt.getMcOptionsContent();

		String questionText = question.getQuestion();
		if (questionText != null) {
		    String escapedQuestion = StringEscapeUtils.escapeJavaScript(questionText);
		    question.setEscapedQuestion(escapedQuestion);
		}

		String optionText = option.getMcQueOptionText();
		if (optionText != null) {
		    String escapedOptionText = StringEscapeUtils.escapeJavaScript(optionText);
		    option.setEscapedOptionText(escapedOptionText);
		}
	    }
	}

	request.setAttribute(McAppConstants.ATTR_CONTENT, user.getMcSession().getMcContent());
	request.setAttribute(McAppConstants.USER_ATTEMPTS, userAttempts);
	request.setAttribute(McAppConstants.TOOL_SESSION_ID, user.getMcSession().getMcSessionId());
	return "monitoring/masterDetailLoadUp";
    }

    /**
     * Return paged users for jqGrid.
     */
    @RequestMapping("/getPagedUsers")
    @ResponseBody
    public String getPagedUsers(HttpServletRequest request, HttpServletResponse response) {

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession session = mcService.getMcSessionById(sessionId);
	//find group leader, if any
	McQueUsr groupLeader = session.getGroupLeader();

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, AttributeNames.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, AttributeNames.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, AttributeNames.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, AttributeNames.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	List<McUserMarkDTO> userDtos = new ArrayList<>();
	int countVisitLogs = 0;
	//in case of UseSelectLeaderToolOuput - display only one user
	if (groupLeader != null) {

	    Integer totalMark = groupLeader.getLastAttemptTotalMark();
	    Long portraitId = mcService.getPortraitId(groupLeader.getQueUsrId());

	    McUserMarkDTO userDto = new McUserMarkDTO();
	    userDto.setQueUsrId(groupLeader.getUid().toString());
	    userDto.setUserId(groupLeader.getQueUsrId().toString());
	    userDto.setFullName(groupLeader.getFullname());
	    userDto.setTotalMark(totalMark != null ? totalMark.longValue() : null);
	    userDto.setPortraitId(portraitId == null ? null : portraitId.toString());
	    userDtos.add(userDto);
	    countVisitLogs = 1;

	} else {
	    userDtos = mcService.getPagedUsersBySession(sessionId, page - 1, rowLimit, sortBy, sortOrder, searchString);
	    countVisitLogs = mcService.getCountPagedUsersBySession(sessionId, searchString);
	}

	int totalPages = new Double(
		Math.ceil(new Integer(countVisitLogs).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 1;
	for (McUserMarkDTO userDto : userDtos) {

	    ArrayNode visitLogData = JsonNodeFactory.instance.arrayNode();
	    Long userUid = Long.parseLong(userDto.getQueUsrId());
	    visitLogData.add(userUid);
	    visitLogData.add(userDto.getUserId());

	    String fullName = HtmlUtils.htmlEscape(userDto.getFullName());
	    if (groupLeader != null && groupLeader.getUid().equals(userUid)) {
		fullName += " (" + mcService.getLocalizedMessage("label.monitoring.group.leader") + ")";
	    }

	    visitLogData.add(fullName);
	    Long totalMark = (userDto.getTotalMark() == null) ? 0 : userDto.getTotalMark();
	    visitLogData.add(totalMark);

	    visitLogData.add(userDto.getPortraitId());

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.set("cell", visitLogData);

	    rows.add(userRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countVisitLogs);
	responseJSON.set("rows", rows);
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    @RequestMapping(path = "/saveUserMark", method = RequestMethod.POST)
    public String saveUserMark(HttpServletRequest request) {

	if ((request.getParameter(McAppConstants.PARAM_NOT_A_NUMBER) == null)
		&& !StringUtils.isEmpty(request.getParameter(McAppConstants.PARAM_USER_ATTEMPT_UID))) {

	    Long userAttemptUid = WebUtil.readLongParam(request, McAppConstants.PARAM_USER_ATTEMPT_UID);
	    Integer newGrade = Integer.valueOf(request.getParameter(McAppConstants.PARAM_GRADE));
	    mcService.changeUserAttemptMark(userAttemptUid, newGrade);
	}

	return null;
    }

    /**
     * Get the mark summary with data arranged in bands. Can be displayed graphically or in a table.
     */
    @RequestMapping("/getMarkChartData")
    @ResponseBody
    public String getMarkChartData(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(contentID);
	List<Number> results = null;

	if (mcContent != null) {
	    if (mcContent.isUseSelectLeaderToolOuput()) {
		results = mcService.getMarksArrayForLeaders(contentID);
	    } else {
		Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		results = mcService.getMarksArray(sessionID);
	    }
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (results != null) {
	    responseJSON.set("data", JsonUtil.readArray(results));
	} else {
	    responseJSON.set("data", JsonUtil.readArray(new Float[0]));
	}
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();

    }

    @RequestMapping("/statistic")
    public String statistic(HttpServletRequest request) {

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, contentID);
	McContent mcContent = mcService.getMcContent(contentID);
	if (mcContent != null) {
	    if (mcContent.isUseSelectLeaderToolOuput()) {
		LeaderResultsDTO leaderDto = mcService.getLeaderResultsDTOForLeaders(contentID);
		request.setAttribute("leaderDto", leaderDto);
	    } else {
		List<SessionDTO> sessionDtos = mcService.getSessionDtos(contentID, true);
		request.setAttribute("sessionDtos", sessionDtos);
	    }
	    request.setAttribute("useSelectLeaderToolOutput", mcContent.isUseSelectLeaderToolOuput());
	}

	// prepare toolOutputDefinitions and activityEvaluation
	List<String> toolOutputDefinitions = new ArrayList<>();
	toolOutputDefinitions.add(McAppConstants.OUTPUT_NAME_LEARNER_MARK);
	toolOutputDefinitions.add(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT);
	String activityEvaluation = mcService.getActivityEvaluation(contentID);
	request.setAttribute(McAppConstants.ATTR_TOOL_OUTPUT_DEFINITIONS, toolOutputDefinitions);
	request.setAttribute(McAppConstants.ATTR_ACTIVITY_EVALUATION, activityEvaluation);

	return "monitoring/parts/statsPart";
    }

}
