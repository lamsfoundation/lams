/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.OpenTextAnswerDTO;
import org.lamsfoundation.lams.tool.vote.dto.SummarySessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.form.VoteMonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ozgur Demirtas
 */
public class MonitoringAction extends LamsDispatchAction implements VoteAppConstants {
    private static Logger logger = Logger.getLogger(MonitoringAction.class.getName());

    /**
     * main content/question content management and workflow logic
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);
	return null;
    }

    public ActionForward hideOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	return toggleHideShow(request, response, false);
    }

    public ActionForward showOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	return toggleHideShow(request, response, true);
    }

    private ActionForward toggleHideShow(HttpServletRequest request, HttpServletResponse response, boolean show)
	    throws IOException, ServletException, ToolException {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	Long currentUid = WebUtil.readLongParam(request, "currentUid");
	logger.info("Current Uid" + currentUid);

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(currentUid);

	voteUsrAttempt.setVisible(show);
	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	String nextActionMethod;
	if (show) {
	    nextActionMethod = "hideOptionVote";
	    voteService.showOpenVote(voteUsrAttempt);
	} else {
	    nextActionMethod = "showOpenVote";
	    voteService.hideOpenVote(voteUsrAttempt);
	}

	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("currentUid", currentUid);
	responsedata.put("nextActionMethod", nextActionMethod);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    public ActionForward getVoteNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	MonitoringAction.repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	Long questionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_QUESTION_UID, false);
	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);

	VoteQueContent nomination = voteService.getQuestionByUid(questionUid);
	request.setAttribute("nominationText", nomination.getQuestion());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.ATTR_QUESTION_UID, questionUid);
	if (sessionUid != null) {
	    request.setAttribute(VoteAppConstants.ATTR_SESSION_UID, sessionUid);
	}
	return mapping.findForward(VoteAppConstants.VOTE_NOMINATION_VIEWER);
    }

    public ActionForward getVoteNominationsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);
	if (sessionUid == 0L) {
	    sessionUid = null;
	    logger.info("Setting sessionUid to null");
	}

	Long questionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_QUESTION_UID, false);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	Integer sortByDate = WebUtil.readIntParam(request, "column[1]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = VoteAppConstants.SORT_BY_DEFAULT;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC;
	} else if (sortByDate != null) {
	    sorting = sortByDate.equals(0) ? VoteAppConstants.SORT_BY_DATE_ASC : VoteAppConstants.SORT_BY_DATE_DESC;
	}

	//return user list according to the given sessionID
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	List<Object[]> users = voteService.getUserAttemptsForTablesorter(sessionUid, questionUid, page, size, sorting,
		searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", voteService.getCountUsersBySession(sessionUid, questionUid, searchString));

	for (Object[] userAndAnswers : users) {

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME, StringEscapeUtils.escapeHtml((String) userAndAnswers[1]));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME,
		    DateUtil.convertToStringForJSON((Date) userAndAnswers[2], request.getLocale()));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME_TIMEAGO,
		    DateUtil.convertToStringForTimeagoJSON((Date) userAndAnswers[2]));
	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    public ActionForward getReflectionsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = VoteAppConstants.SORT_BY_DEFAULT;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC;
	}

	//return user list according to the given sessionID
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	List<Object[]> users = voteService.getUserReflectionsForTablesorter(sessionUid, page, size, sorting,
		searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", voteService.getCountUsersBySession(sessionUid, null, searchString));

	for (Object[] userAndReflection : users) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME,
		    StringEscapeUtils.escapeHtml((String) userAndReflection[1]));
	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		String reflection = StringEscapeUtils.escapeHtml((String) userAndReflection[2]);
		responseRow.put(VoteAppConstants.NOTEBOOK, reflection.replaceAll("\n", "<br>"));
	    }
	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    public ActionForward statistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	request.setAttribute("isGroupedActivity", voteService.isGroupedActivity(toolContentID));
	request.setAttribute(VoteAppConstants.VOTE_STATS_DTO, voteService.getStatisticsBySession(toolContentID));
	return mapping.findForward(VoteAppConstants.STATISTICS);
    }

    public ActionForward getOpenTextNominationsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);
	if (sessionUid == 0L) {
	    logger.info("Setting sessionUid to null");
	    sessionUid = null;
	}

	Long contentUid = WebUtil.readLongParam(request, VoteAppConstants.TOOL_CONTENT_UID, false);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByEntry = WebUtil.readIntParam(request, "column[0]", true);
	Integer sortByName = WebUtil.readIntParam(request, "column[1]", true);
	Integer sortByDate = WebUtil.readIntParam(request, "column[2]", true);
	Integer sortByVisible = WebUtil.readIntParam(request, "column[3]", true);
	String searchStringVote = request.getParameter("fcol[0]");
	String searchStringUsername = request.getParameter("fcol[1]");

	int sorting = VoteAppConstants.SORT_BY_DEFAULT;
	if (sortByEntry != null) {
	    sorting = sortByEntry.equals(0) ? VoteAppConstants.SORT_BY_ENTRY_ASC : VoteAppConstants.SORT_BY_ENTRY_DESC;
	}
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC;
	} else if (sortByDate != null) {
	    sorting = sortByDate.equals(0) ? VoteAppConstants.SORT_BY_DATE_ASC : VoteAppConstants.SORT_BY_DATE_DESC;
	} else if (sortByVisible != null) {
	    sorting = sortByVisible.equals(0) ? VoteAppConstants.SORT_BY_VISIBLE_ASC
		    : VoteAppConstants.SORT_BY_VISIBLE_DESC;
	}

	//return user list according to the given sessionID
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	List<OpenTextAnswerDTO> users = voteService.getUserOpenTextAttemptsForTablesorter(sessionUid, contentUid, page,
		size, sorting, searchStringVote, searchStringUsername);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", voteService.getCountUsersForOpenTextEntries(sessionUid, contentUid,
		searchStringVote, searchStringUsername));

	for (OpenTextAnswerDTO userAndAttempt : users) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    responseRow.put("uid", userAndAttempt.getUserUid());
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME,
		    StringEscapeUtils.escapeHtml(userAndAttempt.getFullName()));

	    responseRow.put("userEntryUid", userAndAttempt.getUserEntryUid());
	    responseRow.put("userEntry", StringEscapeUtils.escapeHtml(userAndAttempt.getUserEntry()));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME,
		    DateUtil.convertToStringForJSON(userAndAttempt.getAttemptTime(), request.getLocale()));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME_TIMEAGO,
		    DateUtil.convertToStringForTimeagoJSON(userAndAttempt.getAttemptTime()));
	    responseRow.put("visible", userAndAttempt.isVisible());

	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService VoteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	//String uid = request.getParameter("uid");

	String userId = request.getParameter("userId");

	String userName = request.getParameter("userName");

	String sessionId = request.getParameter("sessionId");

	NotebookEntry notebookEntry = VoteService.getEntry(new Long(sessionId), CoreNotebookConstants.NOTEBOOK_TOOL,
		VoteAppConstants.MY_SIGNATURE, new Integer(userId));

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();
	if (notebookEntry != null) {
	    //String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
	    voteGeneralLearnerFlowDTO.setNotebookEntry(notebookEntry.getEntry());
	    voteGeneralLearnerFlowDTO.setUserName(userName);
	}
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	return mapping.findForward(VoteAppConstants.LEARNER_NOTEBOOK);
    }

    /**
     * Set Submission Deadline
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteContent voteContent = voteService.getVoteContent(contentID);

	Long dateParameter = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SUBMISSION_DEADLINE, true);
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
	voteContent.setSubmissionDeadline(tzSubmissionDeadline);
	voteService.updateVote(voteContent);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, VoteApplicationException {
	VoteUtils.cleanUpUserExceptions(request);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	ActionForward validateParameters = validateParameters(request, mapping, voteMonitoringForm);
	if (validateParameters != null) {
	    return validateParameters;
	}

	// initialiseMonitoringData
	voteGeneralMonitoringDTO.setRequestLearningReport(Boolean.FALSE.toString());

	/* we have made sure TOOL_CONTENT_ID is passed */
	String toolContentID = voteMonitoringForm.getToolContentID();
	logger.warn("Make sure ToolContentId is passed" + toolContentID);
	VoteContent voteContent = voteService.getVoteContent(new Long(toolContentID));

	if (voteContent == null) {
	    VoteUtils.cleanUpUserExceptions(request);
	    logger.error("Vote Content does not exist");
	    voteGeneralMonitoringDTO.setUserExceptionContentDoesNotExist(Boolean.TRUE.toString());
	    return (mapping.findForward(VoteAppConstants.ERROR_LIST));
	}

	voteGeneralMonitoringDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralMonitoringDTO.setActivityInstructions(voteContent.getInstructions());

	/* this section is related to summary tab. Starts here. */

	SortedSet<SummarySessionDTO> sessionDTOs = voteService.getMonitoringSessionDTOs(new Long(toolContentID));
	request.setAttribute("sessionDTOs", sessionDTOs);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	// setting up the advanced summary for LDEV-1662
	request.setAttribute("useSelectLeaderToolOuput", voteContent.isUseSelectLeaderToolOuput());
	request.setAttribute("lockOnFinish", voteContent.isLockOnFinish());
	request.setAttribute("allowText", voteContent.isAllowText());
	request.setAttribute("maxNominationCount", voteContent.getMaxNominationCount());
	request.setAttribute("minNominationCount", voteContent.getMinNominationCount());
	request.setAttribute("showResults", voteContent.isShowResults());
	request.setAttribute("reflect", voteContent.isReflect());
	request.setAttribute("reflectionSubject", voteContent.getReflectionSubject());
	request.setAttribute("toolContentID", voteContent.getVoteContentId());

	// setting up the SubmissionDeadline
	if (voteContent.getSubmissionDeadline() != null) {
	    Date submissionDeadline = voteContent.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	voteMonitoringForm.setCurrentTab("1");
	voteGeneralMonitoringDTO.setCurrentTab("1");

	if (sessionDTOs.size() > 0) {
	    VoteUtils.cleanUpUserExceptions(request);
	    voteGeneralMonitoringDTO.setUserExceptionContentInUse(Boolean.TRUE.toString());
	}

	/*
	 * get the nominations section is needed for the Edit tab's View Only mode, starts here
	 */
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, voteContent.getTitle());
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, voteContent.getInstructions());

	voteMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	List<VoteQuestionDTO> listQuestionDTO = new LinkedList<>();

	Iterator<VoteQueContent> queIterator = voteContent.getVoteQueContents().iterator();
	while (queIterator.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = new VoteQuestionDTO();

	    VoteQueContent voteQueContent = queIterator.next();
	    if (voteQueContent != null) {

		voteQuestionDTO.setQuestion(voteQueContent.getQuestion());
		voteQuestionDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		listQuestionDTO.add(voteQuestionDTO);
	    }
	}

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, listQuestionDTO);
	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, listQuestionDTO);

	// this section is needed for Edit Activity screen
	voteGeneralAuthoringDTO.setActivityTitle(voteGeneralMonitoringDTO.getActivityTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(voteGeneralMonitoringDTO.getActivityInstructions());

	MonitoringAction.repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	boolean isGroupedActivity = voteService.isGroupedActivity(new Long(toolContentID));
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	request.setAttribute("isAllowText", voteContent.isAllowText());

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    private ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping,
	    VoteMonitoringForm voteMonitoringForm) {

	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	if ((strToolContentId == null) || (strToolContentId.length() == 0)) {
	    VoteUtils.cleanUpUserExceptions(request);
	    return (mapping.findForward(VoteAppConstants.ERROR_LIST));
	} else {
	    try {
		voteMonitoringForm.setToolContentID(strToolContentId);
	    } catch (NumberFormatException e) {
		logger.error("Number Format Exception");
		VoteUtils.cleanUpUserExceptions(request);
		return (mapping.findForward(VoteAppConstants.ERROR_LIST));
	    }
	}
	return null;
    }

    public static Map<String, VoteMonitoredUserDTO> convertToVoteMonitoredUserDTOMap(List<VoteMonitoredUserDTO> list) {
	Map<String, VoteMonitoredUserDTO> map = new TreeMap<>(new VoteComparator());

	Iterator<VoteMonitoredUserDTO> listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    ;
	    VoteMonitoredUserDTO data = listIterator.next();

	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteMonitoringForm.setToolContentID(toolContentID);
	voteGeneralMonitoringDTO.setToolContentID(toolContentID);

	String responseId = request.getParameter(VoteAppConstants.RESPONSE_ID);
	voteMonitoringForm.setResponseId(responseId);
	voteGeneralMonitoringDTO.setResponseId(responseId);

	String currentUid = request.getParameter(VoteAppConstants.CURRENT_UID);
	voteMonitoringForm.setCurrentUid(currentUid);
    }
}
