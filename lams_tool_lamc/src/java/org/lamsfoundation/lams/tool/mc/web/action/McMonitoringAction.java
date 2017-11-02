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

package org.lamsfoundation.lams.tool.mc.web.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.mc.dto.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.dto.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * * @author Ozgur Demirtas
 */
public class McMonitoringAction extends LamsDispatchAction {
    private static Logger logger = Logger.getLogger(McMonitoringAction.class.getName());

    /**
     * displayAnswers
     */
    public ActionForward displayAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));
	mcContent.setDisplayAnswers(new Boolean(true));
	mcService.updateMc(mcContent);
	
	// use redirect to prevent resubmition of the same request
	ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("monitoringStarterRedirect"));
	redirect.addParameter(McAppConstants.TOOL_CONTENT_ID, strToolContentID);
	redirect.addParameter(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return redirect;
    }

    /**
     * allows viewing users reflection data
     */
    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
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

	return mapping.findForward(McAppConstants.LEARNER_NOTEBOOK);
    }

    /**
     * downloadMarks
     */
    public ActionForward downloadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	MessageService messageService = McServiceProxy.getMessageService(getServlet().getServletContext());

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);

	McContent mcContent = mcService.getMcContent(new Long(toolContentID));

	byte[] spreadsheet = null;

	try {
	    spreadsheet = mcService.prepareSessionDataSpreadsheet(mcContent);
	} catch (Exception e) {
	    log.error("Error preparing spreadsheet: ", e);
	    request.setAttribute("errorName", messageService.getMessage("error.monitoring.spreadsheet.download"));
	    request.setAttribute("errorMessage", e);
	    return mapping.findForward("error");
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
     * @throws IOException 
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	IMcService service = McServiceProxy.getMcService(getServlet().getServletContext());

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = service.getMcContent(contentID);

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
	service.updateMc(mcContent);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    /**
     * Set tool's activityEvaluation
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public ActionForward setActivityEvaluation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	IMcService service = McServiceProxy.getMcService(getServlet().getServletContext());

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String activityEvaluation = WebUtil.readStrParam(request, McAppConstants.ATTR_ACTIVITY_EVALUATION);
	service.setActivityEvaluation(contentID, activityEvaluation);

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("success", "true");
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responseJSON.toString()));
	return null;
    }

    /**
     * Populate user jqgrid table on summary page.
     */
    public ActionForward userMasterDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

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
	return (userAttempts == null || userAttempts.isEmpty()) ? null
		: mapping.findForward(McAppConstants.USER_MASTER_DETAIL);
    }

    /**
     * Return paged users for jqGrid.
     */
    public ActionForward getPagedUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

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

	List<McUserMarkDTO> userDtos = new ArrayList<McUserMarkDTO>();
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
		userDto.setPortraitId(portraitId==null ? null : portraitId.toString());
		userDtos.add(userDto);
		countVisitLogs = 1;

	} else {
	    userDtos = mcService.getPagedUsersBySession(sessionId, page - 1, rowLimit, sortBy,
	    		sortOrder, searchString);
	    countVisitLogs = mcService.getCountPagedUsersBySession(sessionId, searchString);
	}

	int totalPages = new Double(
		Math.ceil(new Integer(countVisitLogs).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();

	JSONArray rows = new JSONArray();
	int i = 1;
	for (McUserMarkDTO userDto : userDtos) {

	    JSONArray visitLogData = new JSONArray();
	    Long userUid = Long.parseLong(userDto.getQueUsrId());
	    visitLogData.put(userUid);
	    visitLogData.put(userDto.getUserId());

	    String fullName = StringEscapeUtils.escapeHtml(userDto.getFullName());
	    if (groupLeader != null && groupLeader.getUid().equals(userUid)) {
		fullName += " (" + mcService.getLocalizedMessage("label.monitoring.group.leader") + ")";
	    }

	    visitLogData.put(fullName);
	    Long totalMark = (userDto.getTotalMark() == null) ? 0 : userDto.getTotalMark();
	    visitLogData.put(totalMark);

	    visitLogData.put(userDto.getPortraitId());

	    JSONObject userRow = new JSONObject();
	    userRow.put("id", i++);
	    userRow.put("cell", visitLogData);

	    rows.put(userRow);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countVisitLogs);
	responseJSON.put("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    public ActionForward saveUserMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if ((request.getParameter(McAppConstants.PARAM_NOT_A_NUMBER) == null)
		&& !StringUtils.isEmpty(request.getParameter(McAppConstants.PARAM_USER_ATTEMPT_UID))) {
	    IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	    Long userAttemptUid = WebUtil.readLongParam(request, McAppConstants.PARAM_USER_ATTEMPT_UID);
	    Integer newGrade = Integer.valueOf(request.getParameter(McAppConstants.PARAM_GRADE));
	    mcService.changeUserAttemptMark(userAttemptUid, newGrade);
	}

	return null;
    }
    
    /**
     * Get the mark summary with data arranged in bands. Can be displayed graphically or in a table.
     */
    public ActionForward getMarkChartData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(contentID);
	List<Number> results = null;
	
	if ( mcContent != null ) {
	    if ( mcContent.isUseSelectLeaderToolOuput() ) {
		results = mcService.getMarksArrayForLeaders(contentID);
	    } else {
		Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		results = mcService.getMarksArray(sessionID);
	    }
	}
	
	JSONObject responseJSON = new JSONObject();
	if ( results != null )
	    responseJSON.put("data", results);
	else 
	    responseJSON.put("data", new Float[0]);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().write(responseJSON.toString());
	return null;

    }
    
    public ActionForward statistic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, contentID);
	McContent mcContent = mcService.getMcContent(contentID);
	if ( mcContent != null ) {
	    if ( mcContent.isUseSelectLeaderToolOuput() ) {
		LeaderResultsDTO leaderDto = mcService.getLeaderResultsDTOForLeaders(contentID);
		request.setAttribute("leaderDto", leaderDto);
	    } else {
		List<SessionDTO> sessionDtos = mcService.getSessionDtos(contentID, true);
		request.setAttribute("sessionDtos", sessionDtos);
	    }
	    request.setAttribute("useSelectLeaderToolOutput", mcContent.isUseSelectLeaderToolOuput());
	}
	
	// prepare toolOutputDefinitions and activityEvaluation
	List<String> toolOutputDefinitions = new ArrayList<String>();
	toolOutputDefinitions.add(McAppConstants.OUTPUT_NAME_LEARNER_MARK);
	toolOutputDefinitions.add(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT);
	String activityEvaluation = mcService.getActivityEvaluation(contentID);
	request.setAttribute(McAppConstants.ATTR_TOOL_OUTPUT_DEFINITIONS, toolOutputDefinitions);
	request.setAttribute(McAppConstants.ATTR_ACTIVITY_EVALUATION, activityEvaluation);

	return mapping.findForward(McAppConstants.STATISTICS);
    }


}
