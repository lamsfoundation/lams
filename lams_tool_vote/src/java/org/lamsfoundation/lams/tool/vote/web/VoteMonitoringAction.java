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
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
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
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;

	String currentUid = voteMonitoringForm.getCurrentUid();

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(new Long(currentUid));

	voteUsrAttempt.setVisible(false);
	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	voteService.hideOpenVote(voteUsrAttempt);

	String toolContentID = voteMonitoringForm.getToolContentID();
	String contentFolderID = voteMonitoringForm.getContentFolderID();
	
	ActionRedirect redirect = new ActionRedirect(
		mapping.findForwardConfig(VoteAppConstants.MONITORING_STARTER_REDIRECT));
	redirect.addParameter(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);
	redirect.addParameter(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return redirect;
    }

    public ActionForward showOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;

	String currentUid = voteMonitoringForm.getCurrentUid();

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(new Long(currentUid));
	voteUsrAttempt.setVisible(true);

	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	voteService.showOpenVote(voteUsrAttempt);

	String toolContentID = voteMonitoringForm.getToolContentID();
	String contentFolderID = voteMonitoringForm.getContentFolderID();
	
	ActionRedirect redirect = new ActionRedirect(
		mapping.findForwardConfig(VoteAppConstants.MONITORING_STARTER_REDIRECT));
	redirect.addParameter(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);
	redirect.addParameter(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return redirect;
    }

    public ActionForward getVoteNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	MonitoringUtil.repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	String questionUid = request.getParameter("questionUid");
	String sessionUid = request.getParameter("sessionUid");

	List<VoteUsrAttempt> userAttempts;
	//in regular case when we need info for particular session
	if (StringUtils.isNotBlank(sessionUid)) {
	    userAttempts = voteService.getAttemptsForQuestionContentAndSessionUid(new Long(questionUid),
		    new Long(sessionUid));
	    
	//in case of All sessions
	} else {
	    userAttempts = voteService.getStandardAttemptsByQuestionUid(new Long(questionUid));
	}
	
	List listVotedLearnersDTO = new LinkedList();

	VoteContent voteContent = null;
	Iterator userIterator = userAttempts.iterator();
	while (userIterator.hasNext()) {
	    VoteUsrAttempt voteUsrAttempt = (VoteUsrAttempt) userIterator.next();

	    if (voteUsrAttempt != null) {
		voteContent = voteUsrAttempt.getVoteQueContent().getVoteContent();
	    }

	    VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
	    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
	    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
	    listVotedLearnersDTO.add(voteMonitoredUserDTO);
	}

	voteGeneralMonitoringDTO.setMapStudentsVoted(listVotedLearnersDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	return mapping.findForward(VoteAppConstants.VOTE_NOMINATION_VIEWER);
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
