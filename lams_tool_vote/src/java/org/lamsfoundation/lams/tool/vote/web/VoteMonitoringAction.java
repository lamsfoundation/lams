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
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.OpenTextAnswerDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.form.VoteMonitoringForm;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 *  @author Ozgur Demirtas
 */
public class VoteMonitoringAction extends LamsDispatchAction implements VoteAppConstants {

    /**
     * main content/question content management and workflow logic
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);
	return null;
    }

    public ActionForward hideOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException, JSONException {
	return toggleHideShow(request, response, false);
    }

    public ActionForward showOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
		    HttpServletResponse response) throws IOException, ServletException, ToolException, JSONException {
	return toggleHideShow(request, response, true);
    }
		
    private ActionForward toggleHideShow(HttpServletRequest request,  HttpServletResponse response, boolean show) 
	    throws IOException, ServletException, ToolException, JSONException {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	Long currentUid = WebUtil.readLongParam(request, "currentUid");
	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(currentUid);

	voteUsrAttempt.setVisible(show);
	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	String nextActionMethod;
	if ( show ) {  
	    nextActionMethod = "hideOptionVote";
	    voteService.showOpenVote(voteUsrAttempt);
	} else { 
	    nextActionMethod = "showOpenVote";
	    voteService.hideOpenVote(voteUsrAttempt);   
	}
	
	JSONObject responsedata = new JSONObject();
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
	MonitoringUtil.repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	Long questionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_QUESTION_UID, false);
	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);

	VoteQueContent nomination = voteService.getQuestionByUid(questionUid);
	request.setAttribute("nominationText", nomination.getQuestion());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.ATTR_QUESTION_UID, questionUid);
	if ( sessionUid != null )
	    request.setAttribute(VoteAppConstants.ATTR_SESSION_UID, sessionUid);
	return mapping.findForward(VoteAppConstants.VOTE_NOMINATION_VIEWER);
    }

    public ActionForward getVoteNominationsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException, JSONException {
	
	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);
	if ( sessionUid == 0L )
	    sessionUid = null;
	
	Long questionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_QUESTION_UID, false);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	Integer sortByDate = WebUtil.readIntParam(request, "column[1]", true);
	String searchString = request.getParameter("fcol[0]"); 
	
	int sorting = VoteAppConstants.SORT_BY_DEFAULT;
	if ( sortByName != null ) 
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC; 
	else if ( sortByDate != null )
	    sorting = sortByDate.equals(0) ? VoteAppConstants.SORT_BY_DATE_ASC : VoteAppConstants.SORT_BY_DATE_DESC; 
	    
	//return user list according to the given sessionID
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	List<Object[]> users = voteService.getUserAttemptsForTablesorter(sessionUid, questionUid, page, size, sorting, searchString);
	
	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", voteService.getCountUsersBySession(sessionUid, questionUid, searchString));

	for (Object[] userAndAnswers: users) {

	    JSONObject responseRow = new JSONObject();
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME, StringEscapeUtils.escapeHtml((String) userAndAnswers[1]));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME, DateUtil.convertToStringForJSON((Date) userAndAnswers[2], request.getLocale()));
	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    public ActionForward getReflectionsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException, JSONException {
	
	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]"); 
	
	int sorting = VoteAppConstants.SORT_BY_DEFAULT;
	if ( sortByName != null ) 
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC; 
	    
	//return user list according to the given sessionID
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	List<Object[]> users = voteService.getUserReflectionsForTablesorter(sessionUid, page, size, sorting, searchString);
	
	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", voteService.getCountUsersBySession(sessionUid, null, searchString));

	for (Object[] userAndReflection: users) {
	    JSONObject responseRow = new JSONObject();
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME, StringEscapeUtils.escapeHtml((String) userAndReflection[1]));
	    if ( userAndReflection.length > 2 && userAndReflection[2] != null) {
		String reflection = StringEscapeUtils.escapeHtml((String)userAndReflection[2]);
		responseRow.put(VoteAppConstants.NOTEBOOK, reflection.replaceAll("\n", "<br>"));
	    }
	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    public ActionForward statistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	
	request.setAttribute("isGroupedActivity",  voteService.isGroupedActivity(toolContentID));
	request.setAttribute(VoteAppConstants.VOTE_STATS_DTO, voteService.getStatisticsBySession(toolContentID));
	return mapping.findForward(VoteAppConstants.STATISTICS);
    }

    
    public ActionForward getOpenTextNominationsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException, JSONException {
	
	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);
	if ( sessionUid == 0L )
	    sessionUid = null;

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
	if ( sortByEntry != null ) 
	    sorting = sortByEntry.equals(0) ? VoteAppConstants.SORT_BY_ENTRY_ASC : VoteAppConstants.SORT_BY_ENTRY_DESC; 
	if ( sortByName != null ) 
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC; 
	else if ( sortByDate != null )
	    sorting = sortByDate.equals(0) ? VoteAppConstants.SORT_BY_DATE_ASC : VoteAppConstants.SORT_BY_DATE_DESC; 
	else if ( sortByVisible != null )
	    sorting = sortByVisible.equals(0) ? VoteAppConstants.SORT_BY_VISIBLE_ASC : VoteAppConstants.SORT_BY_VISIBLE_DESC; 
	    
	//return user list according to the given sessionID
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	List<OpenTextAnswerDTO> users = voteService.getUserOpenTextAttemptsForTablesorter(sessionUid, contentUid, page, size, sorting, searchStringVote, searchStringUsername);
	
	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", voteService.getCountUsersForOpenTextEntries(sessionUid, contentUid, searchStringVote, searchStringUsername));
	
	for (OpenTextAnswerDTO userAndAttempt: users) {
	    JSONObject responseRow = new JSONObject();

	    responseRow.put("uid", userAndAttempt.getUserUid());
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME, StringEscapeUtils.escapeHtml(userAndAttempt.getFullName()));

	    responseRow.put("userEntryUid", userAndAttempt.getUserEntryUid());
	    responseRow.put("userEntry", StringEscapeUtils.escapeHtml(userAndAttempt.getUserEntry()));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME, DateUtil.convertToStringForJSON(userAndAttempt.getAttemptTime(), request.getLocale()));
	    responseRow.put("visible", userAndAttempt.isVisible());

	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }
				
    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService VoteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String uid = request.getParameter("uid");

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
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		
    	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
    	VoteContent voteContent = voteService.getVoteContent(contentID);
	
    	Long dateParameter = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SUBMISSION_DEADLINE, true);
    	Date tzSubmissionDeadline = null;
    	if (dateParameter != null) {
    		Date submissionDeadline = new Date(dateParameter);
		    HttpSession ss = SessionManager.getSession();
		    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss.getAttribute(AttributeNames.USER);
		    TimeZone teacherTimeZone = teacher.getTimeZone();
		    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
    	}
    	voteContent.setSubmissionDeadline(tzSubmissionDeadline);
    	voteService.updateVote(voteContent);
    	return null;
    }
}
