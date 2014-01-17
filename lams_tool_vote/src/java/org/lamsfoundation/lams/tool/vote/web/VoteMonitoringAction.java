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
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.EditActivityDTO;
import org.lamsfoundation.lams.tool.vote.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

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

    public ActionForward submitSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, IVoteService voteService, MessageService messageService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException {
	commonSubmitSessionCode(form, request, voteService, messageService, voteGeneralMonitoringDTO);
	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    protected void commonSubmitSessionCode(ActionForm form, HttpServletRequest request, IVoteService voteService,
	    MessageService messageService, VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException,
	    ServletException {
	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	if (currentMonitoredToolSession.equals("All")) {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));

	    List listVoteAllSessionsDTO = MonitoringUtil.prepareChartDTO(request, voteService, voteMonitoringForm,
		    voteContent.getVoteContentId(), messageService);
	    voteGeneralMonitoringDTO.setListVoteAllSessionsDTO(listVoteAllSessionsDTO);
	} else {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));

	    VoteSession voteSession = voteService.retrieveVoteSession(new Long(currentMonitoredToolSession));
	    MonitoringUtil.prepareChartData(request, voteService, voteMonitoringForm, voteContent.getVoteContentId()
		    .toString(), voteSession.getUid().toString(), null, voteGeneralMonitoringDTO, getMessageService());

	    refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true,
		    null, voteGeneralMonitoringDTO, null);
	    voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	}
	request.setAttribute(VoteAppConstants.CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);
	
	boolean isGroupedActivity = voteService.isGroupedActivity(new Long(toolContentID));
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	voteGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, voteContent, voteService);
	voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
	voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);

	// initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	/* setting editable screen properties */
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());

	Map mapOptionsContent = new TreeMap(new VoteComparator());
	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	Long mapIndex = new Long(1);
	while (queIterator.hasNext()) {
	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {
		mapOptionsContent.put(mapIndex.toString(), voteQueContent.getQuestion());
		/**
		 * make the first entry the default(first) one for jsp
		 */
		if (mapIndex.longValue() == 1) {
		    voteGeneralAuthoringDTO.setDefaultOptionContent(voteQueContent.getQuestion());
		}

		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	int maxIndex = mapOptionsContent.size();
	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

	voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    //monitoring url does not allow editActivity since the content is in use
	    voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

    }

    public ActionForward submitSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	commonSubmitSessionCode(form, request, voteService, getMessageService(), voteGeneralMonitoringDTO);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public void refreshSummaryData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService,
	    boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId,
	    boolean showUserEntriesBySession, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO, ExportPortfolioDTO exportPortfolioDTO) {

	if (voteService == null) {
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	}

	/* this section is related to summary tab. Starts here. */
	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, voteContent, voteService);

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

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);

	if (currentSessionId != null && !currentSessionId.equals("All")) {
	    VoteSession voteSession = voteService.retrieveVoteSession(new Long(currentSessionId));
	    if (voteGeneralMonitoringDTO != null) {
		voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
	    }
	} else {
	    voteGeneralMonitoringDTO.setGroupName("All Groups");
	}

	List listMonitoredAnswersContainerDTO = MonitoringUtil.buildGroupsQuestionData(request, voteContent,
		isUserNamesVisible, isLearnerRequest, currentSessionId, userId, voteService);
	/* ends here. */

	List listUserEntries = processUserEnteredNominations(voteService, voteContent, currentSessionId,
		showUserEntriesBySession, userId, isLearnerRequest);

	if (voteGeneralLearnerFlowDTO != null) {
	    voteGeneralLearnerFlowDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    voteGeneralLearnerFlowDTO.setListUserEntries(listUserEntries);
	}

	if (exportPortfolioDTO != null) {
	    exportPortfolioDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    exportPortfolioDTO.setListUserEntries(listUserEntries);
	}

	if (voteGeneralMonitoringDTO != null) {
	    voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);
	    voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    voteGeneralMonitoringDTO.setCurrentMonitoredToolSession("All");
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

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	isContentInUse = VoteUtils.isContentInUse(voteContent);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

    }

    public List processUserEnteredNominations(IVoteService voteService, VoteContent voteContent,
	    String currentSessionId, boolean showUserEntriesBySession, String userId, boolean showUserEntriesByUserId) {
	Set userEntries = voteService.getUserEntries();

	List listUserEntries = new LinkedList();

	Iterator itListNominations = userEntries.iterator();
	while (itListNominations.hasNext()) {
	    String userEntry = (String) itListNominations.next();

	    if (userEntry != null && userEntry.length() > 0) {
		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
		voteMonitoredAnswersDTO.setQuestion(userEntry);

		List userRecords = voteService.getUserRecords(userEntry);
		List listMonitoredUserContainerDTO = new LinkedList();

		Iterator itUserRecords = userRecords.iterator();
		while (itUserRecords.hasNext()) {
		    VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
		    VoteUsrAttempt voteUsrAttempt = (VoteUsrAttempt) itUserRecords.next();

		    VoteSession localUserSession = voteUsrAttempt.getVoteQueUsr().getVoteSession();

		    if (showUserEntriesBySession == false) {
			if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString())) {
			    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
			    voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
			    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
			    voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
			    voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
			    voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
			    voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());
			    listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
			}
		    } else {
			//showUserEntriesBySession is true: the case with learner export portfolio
			//show user  entries  by same content and same session and same user
			String userSessionId = voteUsrAttempt.getVoteQueUsr().getVoteSession().getVoteSessionId()
				.toString();

			if (showUserEntriesByUserId == true) {
			    if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString())) {
				if (userSessionId.equals(currentSessionId)) {
				    String localUserId = voteUsrAttempt.getVoteQueUsr().getQueUsrId().toString();
				    if (userId.equals(localUserId)) {
					voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
					voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
					voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
					voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid()
						.toString());
					voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
					listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
					voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
					voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible())
						.toString());
					if (voteUsrAttempt.isVisible() == false) {
					    voteMonitoredAnswersDTO.setQuestion("Nomination Hidden");
					}

				    }
				}
			    }
			} else {
			    //showUserEntriesByUserId is false
			   //show user  entries  by same content and same session
			    if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString())) {
				if (userSessionId.equals(currentSessionId)) {
				    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
				    voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
				    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
				    voteMonitoredUserDTO
					    .setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
				    voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
				    listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
				    voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
				    voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());
				}
			    }
			}
		    }

		}

		if (listMonitoredUserContainerDTO.size() > 0) {
		    Map mapMonitoredUserContainerDTO = MonitoringUtil
			    .convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);

		    voteMonitoredAnswersDTO.setQuestionAttempts(mapMonitoredUserContainerDTO);
		    listUserEntries.add(voteMonitoredAnswersDTO);
		}
	    }
	}

	return listUserEntries;
    }

    public void initSummaryContent(String toolContentID, HttpServletRequest request, IVoteService voteService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException {
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	
	/* this section is related to summary tab. Starts here. */
	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, voteContent, voteService);
	voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
	voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	/* ends here. */

	/* true means there is at least 1 response */
	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	voteGeneralMonitoringDTO.setCurrentMonitoringTab("summary");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

    }

    public void initStatsContent(String toolContentID, HttpServletRequest request, IVoteService voteService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException {
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));

	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	refreshStatsData(request, voteService, voteGeneralMonitoringDTO);

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	voteGeneralMonitoringDTO.setCurrentMonitoringTab("stats");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
    }

    public void refreshStatsData(HttpServletRequest request, IVoteService voteService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {
	/* it is possible that no users has ever logged in for the activity yet */
	if (voteService == null) {
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	}

	int countAllUsers = voteService.getTotalNumberOfUsers();
	if (countAllUsers == 0) {
	    voteGeneralMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
	}

	voteGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());

	int countSessionComplete = voteService.countSessionComplete();
	voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
    }

    /**
     * calls learning action endLearning functionality 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward endLearning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	VoteLearningAction voteLearningAction = new VoteLearningAction();

	return null;
    }

    public ActionForward viewOpenVotes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	if (voteService == null) {
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	}

	voteMonitoringForm.setShowOpenVotesSection(new Boolean(true).toString());
	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(true).toString());

	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true,
		null, voteGeneralMonitoringDTO, null);

	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);

	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (currentMonitoredToolSession.equals("All")) {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	} else {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	}

	voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    if (userExceptionNoToolSessions.equals("true")) {
		//there are no online student activity but there are reflections
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));
	}

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public ActionForward closeOpenVotes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	voteMonitoringForm.setShowOpenVotesSection(new Boolean(false).toString());
	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(false).toString());

	String toolContentID = voteMonitoringForm.getToolContentID();
	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	if (voteContent != null) {
	    if (voteService.studentActivityOccurredGlobal(voteContent)) {
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	    } else {
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    }
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));
	}

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public ActionForward hideOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	String currentUid = voteMonitoringForm.getCurrentUid();

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(new Long(currentUid));

	voteUsrAttempt.setVisible(false);
	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	voteService.hideOpenVote(voteUsrAttempt);

	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true,
		null, voteGeneralMonitoringDTO, null);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));
	}

	voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);

	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	return submitSession(mapping, form, request, response, voteService, getMessageService(),
		voteGeneralMonitoringDTO);
    }

    public ActionForward showOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	String currentUid = voteMonitoringForm.getCurrentUid();

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(new Long(currentUid));
	voteUsrAttempt.setVisible(true);

	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	voteService.showOpenVote(voteUsrAttempt);

	String toolContentID = voteMonitoringForm.getToolContentID();

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));

	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);

	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();

	refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true,
		null, voteGeneralMonitoringDTO, null);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));
	}

	voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
	return submitSession(mapping, form, request, response, voteService, getMessageService(),
		voteGeneralMonitoringDTO);
    }

    public ActionForward getVoteNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	String questionUid = request.getParameter("questionUid");
	String sessionUid = request.getParameter("sessionUid");

	List userNames = voteService.getStandardAttemptUsersForQuestionContentAndSessionUid(new Long(questionUid),
		new Long(sessionUid));
	List listVotedLearnersDTO = new LinkedList();

	VoteContent voteContent = null;
	Iterator userIterator = userNames.iterator();
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
	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	voteGeneralMonitoringDTO.setMapStudentsVoted(listVotedLearnersDTO);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
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
	    String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
	    voteGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	    voteGeneralLearnerFlowDTO.setUserName(userName);
	}
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	return mapping.findForward(VoteAppConstants.LEARNER_NOTEBOOK);
    }

    protected void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteMonitoringForm.setToolContentID(toolContentID);
	voteGeneralMonitoringDTO.setToolContentID(toolContentID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	voteMonitoringForm.setActiveModule(activeModule);
	voteGeneralMonitoringDTO.setActiveModule(activeModule);

	String defineLaterInEditMode = request.getParameter(VoteAppConstants.DEFINE_LATER_IN_EDIT_MODE);
	voteMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

	String isToolSessionChanged = request.getParameter(VoteAppConstants.IS_TOOL_SESSION_CHANGED);
	voteMonitoringForm.setIsToolSessionChanged(isToolSessionChanged);
	voteGeneralMonitoringDTO.setIsToolSessionChanged(isToolSessionChanged);

	String responseId = request.getParameter(VoteAppConstants.RESPONSE_ID);
	voteMonitoringForm.setResponseId(responseId);
	voteGeneralMonitoringDTO.setResponseId(responseId);

	String currentUid = request.getParameter(VoteAppConstants.CURRENT_UID);
	voteMonitoringForm.setCurrentUid(currentUid);
	voteGeneralMonitoringDTO.setCurrentUid(currentUid);
    }

    protected void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm,
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO) {

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteMonitoringForm.setContentFolderID(contentFolderID);
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteMonitoringForm.setToolContentID(toolContentID);
	voteGeneralAuthoringDTO.setToolContentID(toolContentID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	voteMonitoringForm.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	String defineLaterInEditMode = request.getParameter(VoteAppConstants.DEFINE_LATER_IN_EDIT_MODE);
	voteMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
	voteGeneralAuthoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

    }

    /**
     * used in define later mode to switch from view-only to editable screen
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward editActivityQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	VoteMonitoringForm VoteMonitoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteGeneralMonitoringDTO generalMonitoringDTO = new VoteGeneralMonitoringDTO();

	generalMonitoringDTO.setMonitoredContentInUse(new Boolean(false).toString());

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	generalMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringForm.setContentFolderID(contentFolderID);

	String httpSessionID = request.getParameter("httpSessionID");
	VoteMonitoringForm.setHttpSessionID(httpSessionID);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteMonitoringForm.setTitle(voteContent.getTitle());

	VoteUtils.setDefineLater(request, true, strToolContentID, voteService);

	prepareEditActivityScreenData(request, voteContent);

	// prepareReflectionData(request, voteContent, voteService, null, false, "All");
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	List listNominationContentDTO = new LinkedList();

	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	while (queIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();

	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {
		voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
		voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		listNominationContentDTO.add(voteNominationContentDTO);
	    }
	}
	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	VoteGeneralAuthoringDTO VoteGeneralAuthoringDTO = (VoteGeneralAuthoringDTO) request
		.getAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO);
	VoteGeneralAuthoringDTO.setActiveModule(VoteAppConstants.MONITORING);

	VoteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	VoteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
	VoteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, VoteGeneralAuthoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = generalMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
	MonitoringUtil.generateGroupsSessionData(request, voteService, voteContent);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public void prepareEditActivityScreenData(HttpServletRequest request, VoteContent voteContent) {
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	if (voteContent.getTitle() == null) {
	    voteGeneralAuthoringDTO.setActivityTitle(VoteAppConstants.DEFAULT_VOTING_TITLE);
	} else {
	    voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
	}

	if (voteContent.getInstructions() == null) {
	    voteGeneralAuthoringDTO.setActivityInstructions(VoteAppConstants.DEFAULT_VOTING_INSTRUCTIONS);
	} else {
	    voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
    }

    /**
     * submits content into the tool database 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	Map mapNominationContent = AuthoringUtil.extractMapNominationContent(listNominationContentDTO);

	Map mapFeedback = AuthoringUtil.extractMapFeedback(listNominationContentDTO);

	ActionMessages errors = new ActionMessages();

	if (mapNominationContent.size() == 0) {
	    ActionMessage error = new ActionMessage("nominations.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	AuthoringUtil authoringUtil = new AuthoringUtil();

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	VoteContent voteContentTest = voteService.retrieveVote(new Long(strToolContentID));
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	}

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	VoteContent voteContent = voteContentTest;
	if (errors.isEmpty()) {
	    /* to remove deleted entries in the questions table based on mapNominationContent */
	    authoringUtil.removeRedundantNominations(mapNominationContent, voteService, voteAuthoringForm, request,
		    strToolContentID);

	    voteContent = authoringUtil.saveOrUpdateVoteContent(mapNominationContent, mapFeedback, voteService,
		    voteAuthoringForm, request, voteContentTest, strToolContentID, null);

	    long defaultContentID = 0;
	    defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    if (voteContent != null) {
		voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	    }

	    authoringUtil.reOrganizeDisplayOrder(mapNominationContent, voteService, voteAuthoringForm, voteContent);

	    VoteUtils.setDefineLater(request, false, strToolContentID, voteService);

	    // VoteUtils.setFormProperties(request, voteService,
	    // voteAuthoringForm, voteGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule,
	    // sessionMap, httpSessionID);

	    voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	} else {
	    if (voteContent != null) {
		long defaultContentID = 0;
		defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
		if (voteContent != null) {
		    voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		}

	    }

	    voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	}

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());

	voteAuthoringForm.resetUserAction();
	voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setCurrentTab("3");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    /**
     * saveSingleNomination
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward saveSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);

	String editNominationBoxRequest = request.getParameter("editNominationBoxRequest");

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	String newNomination = request.getParameter("newNomination");

	String editableNominationIndex = request.getParameter("editableNominationIndex");

	if (newNomination != null && newNomination.length() > 0) {
	    if (editNominationBoxRequest != null && editNominationBoxRequest.equals("false")) {
		boolean duplicates = AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);

		if (!duplicates) {
		    VoteNominationContentDTO voteNominationContentDTO = null;
		    Iterator listIterator = listNominationContentDTO.iterator();
		    while (listIterator.hasNext()) {
			voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

			String question = voteNominationContentDTO.getQuestion();
			String displayOrder = voteNominationContentDTO.getDisplayOrder();

			if (displayOrder != null && !displayOrder.equals("")) {
			    if (displayOrder.equals(editableNominationIndex)) {
				break;
			    }

			}
		    }

		    voteNominationContentDTO.setQuestion(newNomination);
		    voteNominationContentDTO.setDisplayOrder(editableNominationIndex);

		    listNominationContentDTO = AuthoringUtil.reorderUpdateListNominationContentDTO(
			    listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
		} else {
		    //duplicate question entry, not adding
		}
	    } else {
		VoteNominationContentDTO voteNominationContentDTO = null;
		Iterator listIterator = listNominationContentDTO.iterator();
		while (listIterator.hasNext()) {
		    voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

		    String question = voteNominationContentDTO.getQuestion();
		    String displayOrder = voteNominationContentDTO.getDisplayOrder();

		    if (displayOrder != null && !displayOrder.equals("")) {
			if (displayOrder.equals(editableNominationIndex)) {
			    break;
			}

		    }
		}

		voteNominationContentDTO.setQuestion(newNomination);
		voteNominationContentDTO.setDisplayOrder(editableNominationIndex);

		listNominationContentDTO = AuthoringUtil.reorderUpdateListNominationContentDTO(
			listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
	    }
	} else {
	    //entry blank, not adding
	}

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	// VoteUtils.setFormProperties(request, voteService,
	// voteAuthoringForm, voteGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule, sessionMap,
	// httpSessionID);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setCurrentTab("3");

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    /**
     * addSingleNomination
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	String newNomination = request.getParameter("newNomination");

	int listSize = listNominationContentDTO.size();

	if (newNomination != null && newNomination.length() > 0) {
	    boolean duplicates = AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);

	    if (!duplicates) {
		VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();
		voteNominationContentDTO.setDisplayOrder(new Long(listSize + 1).toString());
		voteNominationContentDTO.setNomination(newNomination);

		listNominationContentDTO.add(voteNominationContentDTO);
	    }
	}

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setCurrentTab("3");

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    /**
     * opens up an new screen within the current page for adding a new question
     * 
     * newNominationBox
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
	return mapping.findForward("newNominationBox");
    }

    /**
     * opens up an new screen within the current page for editing a question newEditableNominationBox
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	voteAuthoringForm.setEditableNominationIndex(questionIndex);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	String editableNomination = "";
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    String question = voteNominationContentDTO.getNomination();
	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableNomination = voteNominationContentDTO.getNomination();
		    break;
		}

	    }
	}

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	voteGeneralAuthoringDTO.setEditableNominationText(editableNomination);
	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
	return mapping.findForward("editNominationBox");
    }

    /**
     * removes a question from the questions map 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	VoteNominationContentDTO voteNominationContentDTO = null;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    String question = voteNominationContentDTO.getNomination();
	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	voteNominationContentDTO.setNomination("");

	listNominationContentDTO = AuthoringUtil.reorderListNominationContentDTO(listNominationContentDTO,
		questionIndex);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	if (voteContent == null) {
	    voteContent = voteService.retrieveVote(new Long(defaultContentIdStr));
	}

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setCurrentTab("3");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    /**
     * moves a question down in the list moveNominationDown
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	listNominationContentDTO = AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "down");

	listNominationContentDTO = AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setCurrentTab("3");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    /**
     * moves a question up in the list moveNominationUp
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String httpSessionID = voteAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);

	listNominationContentDTO = AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "up");

	listNominationContentDTO = AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
	voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setToolContentID(strToolContentID);
	voteAuthoringForm.setHttpSessionID(httpSessionID);
	voteAuthoringForm.setActiveModule(activeModule);
	voteAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	voteAuthoringForm.setCurrentTab("3");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    if (userExceptionNoToolSessions.equals("true")) {
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public void prepareReflectionData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService,
	    String userID, boolean exportMode, String currentSessionId) {
	List reflectionsContainerDTO = new LinkedList();

	reflectionsContainerDTO = getReflectionList(voteContent, userID, voteService);
	request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	if (exportMode) {
	    request.getSession().setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	}
    }

    /**
     * returns reflection data for all sessions
     * 
     * getReflectionList
     * 
     * @param voteContent
     * @param userID
     * @param voteService
     * @return
     */
    public List getReflectionList(VoteContent voteContent, String userID, IVoteService voteService) {
	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();

		for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
		    VoteQueUsr user = (VoteQueUsr) userIter.next();

		    NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
				    .getQueUsrId().toString()));

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
			reflectionDTO.setUserName(user.getFullname());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntryPresentable);
			reflectionsContainerDTO.add(reflectionDTO);
		    }
		}
	    }
	} else {
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();
		for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
		    VoteQueUsr user = (VoteQueUsr) userIter.next();
		    if (user.getQueUsrId().toString().equals(userID)) {
			NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntryPresentable);
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	}

	return reflectionsContainerDTO;
    }

    /**
     * returns reflection data for a specific session
     * 
     * @param voteContent
     * @param userID
     * @param voteService
     * @param currentSessionId
     * @return
     */
    public List getReflectionListForSession(VoteContent voteContent, String userID, IVoteService voteService,
	    String currentSessionId) {

	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();

		if (currentSessionId.equals(voteSession.getVoteSessionId())) {

		    for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
			VoteQueUsr user = (VoteQueUsr) userIter.next();

			NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntryPresentable);
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	} else {
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();

		if (currentSessionId.equals(voteSession.getVoteSessionId())) {
		    for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
			VoteQueUsr user = (VoteQueUsr) userIter.next();

			if (user.getQueUsrId().toString().equals(userID)) {
			    NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
				    CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(
					    user.getQueUsrId().toString()));

			    if (notebookEntry != null) {
				ReflectionDTO reflectionDTO = new ReflectionDTO();
				reflectionDTO.setUserId(user.getQueUsrId().toString());
				reflectionDTO.setSessionId(voteSession.getVoteSessionId().toString());
				reflectionDTO.setUserName(user.getFullname());
				reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
				String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
				reflectionDTO.setEntry(notebookEntryPresentable);
				reflectionsContainerDTO.add(reflectionDTO);
			    }
			}
		    }

		}
	    }
	}

	return reflectionsContainerDTO;
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
    	VoteContent voteContent = voteService.retrieveVote(contentID);
	
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


    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	return VoteServiceProxy.getMessageService(getServlet().getServletContext());
    }
}
