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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.vote.EditActivityDTO;
import org.lamsfoundation.lams.tool.vote.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.SessionDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Starts up the monitoring module
 * 
 * @author Ozgur Demirtas
 */
public class VoteMonitoringStarterAction extends Action implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteMonitoringStarterAction.class.getName());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, VoteApplicationException {
	VoteUtils.cleanUpSessionAbsolute(request);

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

	//initialiseMonitoringData
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());

	VoteUtils.saveTimeZone(request);

	/* we have made sure TOOL_CONTENT_ID is passed */
	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));

	if (voteContent == null) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    voteGeneralMonitoringDTO.setUserExceptionContentDoesNotExist(new Boolean(true).toString());
	    return (mapping.findForward(ERROR_LIST));
	}

	voteGeneralMonitoringDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralMonitoringDTO.setActivityInstructions(voteContent.getInstructions());

	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    voteGeneralMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
	}

	/*
	 * get the nominations section is needed for the Edit tab's View Only mode, starts here
	 */
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	sessionMap.put(ACTIVITY_TITLE_KEY, voteContent.getTitle());
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, voteContent.getInstructions());

	voteMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	List listNominationContentDTO = new LinkedList();

	Map mapOptionsContent = new TreeMap(new VoteComparator());
	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	Long mapIndex = new Long(1);
	while (queIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();

	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {
		mapOptionsContent.put(mapIndex.toString(), voteQueContent.getQuestion());

		voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
		voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		listNominationContentDTO.add(voteNominationContentDTO);
		
		//make the first entry the default(first) one for jsp
		if (mapIndex.longValue() == 1) {
		    voteGeneralAuthoringDTO.setDefaultOptionContent(voteQueContent.getQuestion());
		}

		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}
	voteGeneralMonitoringDTO.setMapOptionsContent(mapOptionsContent);
	/* ends here */

	request.setAttribute(LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	VoteMonitoringStarterAction.refreshSummaryData(request, voteContent, voteService, true, false, null, null, false,
		null, voteGeneralMonitoringDTO, null);

	voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());

	voteMonitoringForm.setCurrentTab("1");
	voteGeneralMonitoringDTO.setCurrentTab("1");

	/* true means there is at least 1 response */
	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	voteMonitoringForm.setActiveModule(MONITORING);
	voteGeneralMonitoringDTO.setActiveModule(MONITORING);

	voteGeneralMonitoringDTO.setIsPortfolioExport(new Boolean(false).toString());

	// this section is needed for Edit Activity screen
	voteGeneralAuthoringDTO.setActivityTitle(voteGeneralMonitoringDTO.getActivityTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(voteGeneralMonitoringDTO.getActivityInstructions());
	voteGeneralAuthoringDTO.setDefaultOptionContent(voteGeneralMonitoringDTO.getDefaultOptionContent());
	voteGeneralAuthoringDTO.setMapOptionsContent(voteGeneralMonitoringDTO.getMapOptionsContent());
	voteGeneralAuthoringDTO.setActiveModule(MONITORING);
	voteGeneralAuthoringDTO.setMaxOptionIndex(mapOptionsContent.size());

	MonitoringUtil.repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	List<SessionDTO> sessionDTOs = voteService.getSessionDTOs(new Long(toolContentID));
	voteGeneralMonitoringDTO.setSessionDTOs(sessionDTOs);
	
	boolean isGroupedActivity = voteService.isGroupedActivity(new Long(toolContentID));
	request.setAttribute("isGroupedActivity", isGroupedActivity);


	//refreshStatsData
	/* it is possible that no users has ever logged in for the activity yet */
	int countAllUsers = voteService.getTotalNumberOfUsers();
	if (countAllUsers == 0) {
	    voteGeneralMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
	}
	voteGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());
	int countSessionComplete = voteService.countSessionComplete();
	voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());

	List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	
	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public static void refreshSummaryData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService,
	    boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId,
	    boolean showUserEntriesBySession, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO, ExportPortfolioDTO exportPortfolioDTO) {

	/* this section is related to summary tab. Starts here. */

	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    if (voteGeneralMonitoringDTO != null) {
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	    }
	} else {
	    if (voteGeneralMonitoringDTO != null) {
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    }
	}

	String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	if (exportPortfolioDTO != null) {
	    exportPortfolioDTO.setUserExceptionNoToolSessions(userExceptionNoToolSessions);
	}
	
	List listQuestions = voteService.getAllQuestionEntries(voteContent.getUid());

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator itListQuestions = listQuestions.iterator();
	while (itListQuestions.hasNext()) {
	    VoteQueContent voteQueContent = (VoteQueContent) itListQuestions.next();

	    if (voteQueContent != null) {
		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
		voteMonitoredAnswersDTO.setQuestionUid(voteQueContent.getUid().toString());
		voteMonitoredAnswersDTO.setQuestion(voteQueContent.getQuestion());

		Map<String, Map> questionAttemptData = MonitoringUtil.buildGroupsAttemptData(request, voteContent, voteQueContent,
			voteQueContent.getUid().toString(), isUserNamesVisible, isLearnerRequest, currentSessionId,
			userId, voteService);
		voteMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		listMonitoredAnswersContainerDTO.add(voteMonitoredAnswersDTO);

	    }
	}
	/* ends here. */

	List<VoteMonitoredAnswersDTO> listUserEntries = voteService.processUserEnteredNominations(voteContent,
		currentSessionId, showUserEntriesBySession, userId, isLearnerRequest);

	if (voteGeneralLearnerFlowDTO != null) {
	    voteGeneralLearnerFlowDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    voteGeneralLearnerFlowDTO.setListUserEntries(listUserEntries);
	}

	if (exportPortfolioDTO != null) {
	    exportPortfolioDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    exportPortfolioDTO.setListUserEntries(listUserEntries);
	}

	if (voteGeneralMonitoringDTO != null) {
	    voteGeneralMonitoringDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    voteGeneralMonitoringDTO.setListUserEntries(listUserEntries);

	    voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());
	    if (listUserEntries.size() > 0) {
		voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(true).toString());
	    }
	}

	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    //monitoring url does not allow editActivity since the content is in use
	    voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	}

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    //monitoring url does not allow editActivity since the content is in use
	    voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	// find out if there are any reflection entries
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

    }

    private ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping,
	    VoteMonitoringForm voteMonitoringForm) {

	String strToolContentId = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	if ((strToolContentId == null) || (strToolContentId.length() == 0)) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    return (mapping.findForward(ERROR_LIST));
	} else {
	    try {
		voteMonitoringForm.setToolContentID(strToolContentId);
	    } catch (NumberFormatException e) {
		logger.error("add error.numberFormatException to ActionMessages.");
		VoteUtils.cleanUpSessionAbsolute(request);
		return (mapping.findForward(ERROR_LIST));
	    }
	}
	return null;
    }
}
