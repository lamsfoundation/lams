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
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
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
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * <p>
 * Action class that controls the logic of tool behavior.
 * </p>
 * 
 * <p>
 * Note that Struts action class only has the responsibility to navigate page flow. All database operation should go to
 * service layer and data transformation from domain model to struts form bean should go to form bean class. This ensure
 * clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is identified. No system exception error
 * handling code should appear in the Struts action class as all of them are handled in
 * <code>CustomStrutsExceptionHandler<code>.
 * 
 *  @author Ozgur Demirtas
 * 
 *    <!--Monitoring Main Action: interacts with the Monitoring module user -->
 <action 	path="/monitoring" 
 type="org.lamsfoundation.lams.tool.vote.web.VoteMonitoringAction" 
 name="VoteMonitoringForm" 
 scope="request"
 input="/monitoring/MonitoringMaincontent.jsp"
 parameter="dispatch"
 unknown="false"
 validate="false"> 

 <forward
 name="loadMonitoring"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward
 name="loadMonitoringEditActivity"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward
 name="refreshMonitoring"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward 
 name="voteNominationViewer" 
 path="/monitoring/VoteNominationViewer.jsp" 
 redirect="false"
 />	      

 <forward
 name="learnerNotebook"
 path="/monitoring/LearnerNotebook.jsp"
 redirect="false"
 />


 <forward
 name="newNominationBox"
 path="/monitoring/newNominationBox.jsp"
 redirect="false"
 />

 <forward
 name="editNominationBox"
 path="/monitoring/editNominationBox.jsp"
 redirect="false"
 />


 <forward
 name="errorList"
 path="/VoteErrorBox.jsp"
 redirect="false"
 />
 </action>  

 *
 */
public class VoteMonitoringAction extends LamsDispatchAction implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteMonitoringAction.class.getName());

    /**
     * <p>
     * Default struts dispatch method.
     * </p>
     * 
     * <p>
     * It is assuming that progress engine should pass in the tool access mode and the tool session id as http
     * parameters.
     * </p>
     * 
     * @param mapping
     *                An ActionMapping class that will be used by the Action class to tell the ActionServlet where to
     *                send the end-user.
     * 
     * @param form
     *                The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *                A standard Servlet HttpServletRequest class.
     * @param response
     *                A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * @throws IOException
     * @throws ServletException
     * @throws VoteApplicationException
     *                 the known runtime exception
     * 
     * unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * throws IOException, ServletException
     * 
     * main content/question content management and workflow logic
     * 
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);
	VoteMonitoringAction.logger.debug("dispatching unspecified...");
	return null;
    }

    public ActionForward submitSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, IVoteService voteService, MessageService messageService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException {
	VoteMonitoringAction.logger.debug("calling submitSession...voteGeneralMonitoringDTO:"
		+ voteGeneralMonitoringDTO);
	commonSubmitSessionCode(form, request, voteService, messageService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post commonSubmitSessionCode: " + voteGeneralMonitoringDTO);
	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    protected void commonSubmitSessionCode(ActionForm form, HttpServletRequest request, IVoteService voteService,
	    MessageService messageService, VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException,
	    ServletException {
	VoteMonitoringAction.logger.debug("starting  commonSubmitSessionCode...voteGeneralMonitoringDTO:"
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("voteService:" + voteService);
	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("done repopulateRequestParameters");

	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	VoteMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);

	if (currentMonitoredToolSession.equals("All")) {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));

	    VoteMonitoringAction.logger.debug("generate DTO for All sessions: ");
	    List listVoteAllSessionsDTO = MonitoringUtil.prepareChartDTO(request, voteService, voteMonitoringForm,
		    voteContent.getVoteContentId(), messageService);
	    VoteMonitoringAction.logger.debug("listVoteAllSessionsDTO: " + listVoteAllSessionsDTO);
	    voteGeneralMonitoringDTO.setListVoteAllSessionsDTO(listVoteAllSessionsDTO);
	} else {
	    VoteMonitoringAction.logger.debug("preparing chart data for content id: " + voteContent.getVoteContentId());
	    VoteMonitoringAction.logger.debug("preparing chart data for currentMonitoredToolSession: "
		    + currentMonitoredToolSession);
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));

	    VoteSession voteSession = voteService.retrieveVoteSession(new Long(currentMonitoredToolSession));
	    VoteMonitoringAction.logger.debug("voteSession uid:" + voteSession.getUid());
	    MonitoringUtil.prepareChartData(request, voteService, voteMonitoringForm, voteContent.getVoteContentId()
		    .toString(), voteSession.getUid().toString(), null, voteGeneralMonitoringDTO, getMessageService());

	    VoteMonitoringAction.logger.debug("post prepareChartData, voteGeneralMonitoringDTO:"
		    + voteGeneralMonitoringDTO);

	    refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true,
		    null, voteGeneralMonitoringDTO, null);
	    VoteMonitoringAction.logger.debug("session_name: " + voteSession.getSession_name());
	    voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
	    VoteMonitoringAction.logger.debug("post refreshSummaryData, voteGeneralMonitoringDTO:"
		    + voteGeneralMonitoringDTO);
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	}
	VoteMonitoringAction.logger.debug("SELECTION_CASE: " + voteGeneralMonitoringDTO.getSelectionCase());

	VoteMonitoringAction.logger.debug("SELECTION_CASE: " + request.getAttribute(VoteAppConstants.SELECTION_CASE));
	request.setAttribute(VoteAppConstants.CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);
	
	boolean isGroupedActivity = voteService.isGroupedActivity(new Long(toolContentID));
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	voteGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, voteContent, voteService);
	VoteMonitoringAction.logger.debug("summaryToolSessions: " + summaryToolSessions);
	voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
	VoteMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);

	VoteMonitoringAction.logger.debug("calling initInstructionsContent.");
	// initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initStatsContent.");
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initStatsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	/* setting editable screen properties */
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());

	Map mapOptionsContent = new TreeMap(new VoteComparator());
	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	Long mapIndex = new Long(1);
	VoteMonitoringAction.logger.debug("mapOptionsContent: " + mapOptionsContent);
	while (queIterator.hasNext()) {
	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {
		VoteMonitoringAction.logger.debug("question: " + voteQueContent.getQuestion());
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

	VoteMonitoringAction.logger.debug("mapOptionsContent: " + mapOptionsContent);
	int maxIndex = mapOptionsContent.size();
	VoteMonitoringAction.logger.debug("maxIndex: " + maxIndex);
	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

	voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    VoteMonitoringAction.logger
		    .debug("monitoring url does not allow editActivity since the content is in use.");
	    voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	}

	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of commonSubmitSessionCode, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

    }

    public ActionForward submitSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, MessageService messageService) throws IOException, ServletException {
	VoteMonitoringAction.logger.debug("dispathcing submitSession..");
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	commonSubmitSessionCode(form, request, voteService, messageService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post commonSubmitSessionCode: " + voteGeneralMonitoringDTO);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public void refreshSummaryData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService,
	    boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId,
	    boolean showUserEntriesBySession, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO, ExportPortfolioDTO exportPortfolioDTO) {
	VoteMonitoringAction.logger.debug("doing refreshSummaryData." + voteGeneralLearnerFlowDTO);
	VoteMonitoringAction.logger.debug("voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("exportPortfolioDTO:" + exportPortfolioDTO);

	if (voteService == null) {
	    VoteMonitoringAction.logger.debug("will retrieve voteService");
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    VoteMonitoringAction.logger.debug("retrieving voteService from session: " + voteService);
	}
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	VoteMonitoringAction.logger.debug("isUserNamesVisible: " + isUserNamesVisible);
	VoteMonitoringAction.logger.debug("isLearnerRequest: " + isLearnerRequest);

	/* this section is related to summary tab. Starts here. */
	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, voteContent, voteService);
	VoteMonitoringAction.logger.debug("summaryToolSessions: " + summaryToolSessions);

	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    if (voteGeneralMonitoringDTO != null) {
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	    }
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	} else {
	    if (voteGeneralMonitoringDTO != null) {
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    }
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	}

	String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	VoteMonitoringAction.logger.debug("userExceptionNoToolSessions: " + userExceptionNoToolSessions);
	if (exportPortfolioDTO != null) {
	    exportPortfolioDTO.setUserExceptionNoToolSessions(userExceptionNoToolSessions);
	}

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
	VoteMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	VoteMonitoringAction.logger.debug("currentSessionId: " + currentSessionId);

	if (currentSessionId != null && !currentSessionId.equals("All")) {
	    VoteSession voteSession = voteService.retrieveVoteSession(new Long(currentSessionId));
	    VoteMonitoringAction.logger.debug("voteSession:" + voteSession);
	    if (voteGeneralMonitoringDTO != null) {
		voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
	    }
	} else {
	    voteGeneralMonitoringDTO.setGroupName("All Groups");
	}

	VoteMonitoringAction.logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	List listMonitoredAnswersContainerDTO = MonitoringUtil.buildGroupsQuestionData(request, voteContent,
		isUserNamesVisible, isLearnerRequest, currentSessionId, userId, voteService);
	VoteMonitoringAction.logger.debug("listMonitoredAnswersContainerDTO: " + listMonitoredAnswersContainerDTO);
	/* ends here. */

	VoteMonitoringAction.logger.debug("decide processing user entered values based on isLearnerRequest: "
		+ isLearnerRequest);

	List listUserEntries = processUserEnteredNominations(voteService, voteContent, currentSessionId,
		showUserEntriesBySession, userId, isLearnerRequest);
	VoteMonitoringAction.logger.debug("listUserEntries: " + listUserEntries);

	if (voteGeneralLearnerFlowDTO != null) {
	    VoteMonitoringAction.logger.debug("placing dtos within the voteGeneralLearnerFlowDTO: ");
	    voteGeneralLearnerFlowDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    voteGeneralLearnerFlowDTO.setListUserEntries(listUserEntries);
	    ;
	}

	if (exportPortfolioDTO != null) {
	    VoteMonitoringAction.logger.debug("placing dtos within the exportPortfolioDTO: ");
	    exportPortfolioDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	    exportPortfolioDTO.setListUserEntries(listUserEntries);
	    ;
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

	VoteMonitoringAction.logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
	VoteMonitoringAction.logger.debug("final voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    VoteMonitoringAction.logger
		    .debug("monitoring url does not allow editActivity since the content is in use.");
	    voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	}

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
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
	VoteMonitoringAction.logger.debug("start getting user entries, showUserEntriesBySession: "
		+ showUserEntriesBySession);
	VoteMonitoringAction.logger.debug("start getting user entries, currentSessionId: " + currentSessionId);
	VoteMonitoringAction.logger.debug("start getting user entries, showUserEntriesByUserId: "
		+ showUserEntriesByUserId);
	VoteMonitoringAction.logger.debug("start getting user entries, userId: " + userId);
	VoteMonitoringAction.logger.debug("start getting user entries, voteContent: " + voteContent);
	VoteMonitoringAction.logger.debug("start getting user entries, voteContent id: "
		+ voteContent.getVoteContentId());

	Set userEntries = voteService.getUserEntries();
	VoteMonitoringAction.logger.debug("userEntries: " + userEntries);

	List listUserEntries = new LinkedList();

	Iterator itListNominations = userEntries.iterator();
	while (itListNominations.hasNext()) {
	    String userEntry = (String) itListNominations.next();
	    VoteMonitoringAction.logger.debug("userEntry:..." + userEntry);

	    if (userEntry != null && userEntry.length() > 0) {
		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
		VoteMonitoringAction.logger.debug("adding user entry : " + userEntry);
		voteMonitoredAnswersDTO.setQuestion(userEntry);

		List userRecords = voteService.getUserRecords(userEntry);
		VoteMonitoringAction.logger.debug("userRecords: " + userRecords);

		VoteMonitoringAction.logger.debug("start processing user records: ");

		List listMonitoredUserContainerDTO = new LinkedList();

		Iterator itUserRecords = userRecords.iterator();
		while (itUserRecords.hasNext()) {
		    VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
		    VoteMonitoringAction.logger.debug("new DTO created");
		    VoteUsrAttempt voteUsrAttempt = (VoteUsrAttempt) itUserRecords.next();
		    VoteMonitoringAction.logger.debug("voteUsrAttempt: " + voteUsrAttempt);

		    VoteSession localUserSession = voteUsrAttempt.getVoteQueUsr().getVoteSession();
		    VoteMonitoringAction.logger.debug("localUserSession: " + localUserSession);
		    VoteMonitoringAction.logger.debug("localUserSession's content id: "
			    + localUserSession.getVoteContentId());
		    VoteMonitoringAction.logger.debug("incoming content uid versus localUserSession's content id: "
			    + voteContent.getUid() + " versus " + localUserSession.getVoteContentId());

		    if (showUserEntriesBySession == false) {
			VoteMonitoringAction.logger.debug("showUserEntriesBySession is false");
			VoteMonitoringAction.logger.debug("show user  entries  by same content only");
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
			VoteMonitoringAction.logger
				.debug("showUserEntriesBySession is true: the case with learner export portfolio");
			VoteMonitoringAction.logger
				.debug("show user  entries  by same content and same session and same user");
			String userSessionId = voteUsrAttempt.getVoteQueUsr().getVoteSession().getVoteSessionId()
				.toString();
			VoteMonitoringAction.logger.debug("userSessionId versus currentSessionId: " + userSessionId
				+ " versus " + currentSessionId);

			if (showUserEntriesByUserId == true) {
			    if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString())) {
				VoteMonitoringAction.logger.debug("showUserEntriesByUserId is true");
				if (userSessionId.equals(currentSessionId)) {
				    String localUserId = voteUsrAttempt.getVoteQueUsr().getQueUsrId().toString();
				    if (userId.equals(localUserId)) {
					VoteMonitoringAction.logger.debug("this is requested by user id: " + userId);
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
					VoteMonitoringAction.logger
						.debug("overriding the nomination text with 'Nomination Hidden' if needed");
					VoteMonitoringAction.logger.debug("is entry visible: "
						+ voteUsrAttempt.isVisible());
					if (voteUsrAttempt.isVisible() == false) {
					    voteMonitoredAnswersDTO.setQuestion("Nomination Hidden");
					    VoteMonitoringAction.logger.debug("overwrote the nomination text");
					}

				    }
				}
			    }
			} else {
			    VoteMonitoringAction.logger.debug("showUserEntriesByUserId is false");
			    VoteMonitoringAction.logger.debug("show user  entries  by same content and same session");
			    VoteMonitoringAction.logger
				    .debug("voteContent.getVoteContentId() versus localUserSession.getVoteContentId().toString(): "
					    + voteContent.getVoteContentId()
					    + " versus "
					    + localUserSession.getVoteContentId());
			    if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString())) {
				if (userSessionId.equals(currentSessionId)) {
				    VoteMonitoringAction.logger.debug("this is requested by session id: "
					    + currentSessionId);
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

		VoteMonitoringAction.logger.debug("final listMonitoredUserContainerDTO: "
			+ listMonitoredUserContainerDTO);

		VoteMonitoringAction.logger.debug("final listMonitoredUserContainerDTO size: "
			+ listMonitoredUserContainerDTO.size());
		if (listMonitoredUserContainerDTO.size() > 0) {
		    VoteMonitoringAction.logger.debug("adding user entry's data");
		    Map mapMonitoredUserContainerDTO = MonitoringUtil
			    .convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);
		    VoteMonitoringAction.logger.debug("final user entry mapMonitoredUserContainerDTO:..."
			    + mapMonitoredUserContainerDTO);

		    voteMonitoredAnswersDTO.setQuestionAttempts(mapMonitoredUserContainerDTO);
		    listUserEntries.add(voteMonitoredAnswersDTO);
		}
	    }
	}

	VoteMonitoringAction.logger.debug("finish getting user entries: " + listUserEntries);
	return listUserEntries;
    }

    public void initSummaryContent(String toolContentID, HttpServletRequest request, IVoteService voteService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException {
	VoteMonitoringAction.logger.debug("start  initSummaryContent...toolContentID: " + toolContentID);
	VoteMonitoringAction.logger.debug("dispatching getSummary...voteGeneralMonitoringDTO:"
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("voteService: " + voteService);
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);
	
	if (voteContent.getSubmissionDeadline() != null) {
		Date submissionDeadline = voteContent.getSubmissionDeadline();
		HttpSession ss = SessionManager.getSession();
		UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
		TimeZone teacherTimeZone = teacher.getTimeZone();
		Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
		request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}	
	

	/* this section is related to summary tab. Starts here. */
	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, voteContent, voteService);
	VoteMonitoringAction.logger.debug("summaryToolSessions: " + summaryToolSessions);
	voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
	VoteMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	/* ends here. */

	/* true means there is at least 1 response */
	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	}

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	voteGeneralMonitoringDTO.setCurrentMonitoringTab("summary");

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("end  initSummaryContent...");
    }

    public void initStatsContent(String toolContentID, HttpServletRequest request, IVoteService voteService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException {
	VoteMonitoringAction.logger.debug("starting  initStatsContent...:" + toolContentID);
	VoteMonitoringAction.logger.debug("dispatching getStats..." + request);
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);

	if (voteService.studentActivityOccurredStandardAndOpen(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	}

	refreshStatsData(request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post refreshStatsData, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	// prepareReflectionData(request, voteContent, voteService, null,false);
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	voteGeneralMonitoringDTO.setCurrentMonitoringTab("stats");

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("ending  initStatsContent...");
    }

    public void refreshStatsData(HttpServletRequest request, IVoteService voteService,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {
	VoteMonitoringAction.logger.debug("starting refreshStatsData: " + voteService);
	/* it is possible that no users has ever logged in for the activity yet */
	if (voteService == null) {
	    VoteMonitoringAction.logger.debug("will retrieve voteService");
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    VoteMonitoringAction.logger.debug("retrieving voteService from session: " + voteService);
	}

	int countAllUsers = voteService.getTotalNumberOfUsers();
	VoteMonitoringAction.logger.debug("countAllUsers: " + countAllUsers);

	if (countAllUsers == 0) {
	    VoteMonitoringAction.logger.debug("error: countAllUsers is 0");
	    voteGeneralMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
	}

	voteGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());

	int countSessionComplete = voteService.countSessionComplete();
	VoteMonitoringAction.logger.debug("countSessionComplete: " + countSessionComplete);
	voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());

	VoteMonitoringAction.logger.debug("end of refreshStatsData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
    }

    /**
     * calls learning action endLearning functionality ActionForward endLearning(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ToolException
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
	VoteMonitoringAction.logger.debug("dispatching proxy endLearning to learning module...");

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService :" + voteService);

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	VoteMonitoringAction.logger.debug("voteMonitoringForm :" + voteMonitoringForm);
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	VoteLearningAction voteLearningAction = new VoteLearningAction();

	return null;
    }

    public ActionForward viewOpenVotes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteMonitoringAction.logger.debug("dispatching viewOpenVotes...");

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService :" + voteService);

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	VoteMonitoringAction.logger.debug("voteMonitoringForm :" + voteMonitoringForm);
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	if (voteService == null) {
	    VoteMonitoringAction.logger.debug("will retrieve voteService");
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	}
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	voteMonitoringForm.setShowOpenVotesSection(new Boolean(true).toString());
	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(true).toString());

	VoteMonitoringAction.logger.debug("showOpen votes set to true: ");

	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);

	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	VoteMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true,
		null, voteGeneralMonitoringDTO, null);

	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initInstructionsContent.");
	VoteMonitoringAction.logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initStatsContent.");
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initStatsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	VoteMonitoringAction.logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (currentMonitoredToolSession.equals("All")) {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	} else {
	    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	}

	voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("ending editActivityQuestions, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));
	}

	VoteMonitoringAction.logger.debug("SELECTION_CASE: " + request.getAttribute(VoteAppConstants.SELECTION_CASE));

	VoteMonitoringAction.logger
		.debug("ending viewOpenVotes, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("fwd'ing to LOAD_MONITORING: " + VoteAppConstants.LOAD_MONITORING);
	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public ActionForward closeOpenVotes(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteMonitoringAction.logger.debug("dispatching closeOpenVotes...");
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService :" + voteService);

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	VoteMonitoringAction.logger.debug("voteMonitoringForm :" + voteMonitoringForm);
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	voteMonitoringForm.setShowOpenVotesSection(new Boolean(false).toString());
	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(false).toString());

	VoteMonitoringAction.logger.debug("showOpen votes set to false: ");

	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);

	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initInstructionsContent.");
	VoteMonitoringAction.logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initStatsContent.");
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initStatsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);

	if (voteContent != null) {
	    if (voteService.studentActivityOccurredGlobal(voteContent)) {
		VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	    } else {
		VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    }
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);
	VoteMonitoringAction.logger.debug("ending closeOpenVotes, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(VoteAppConstants.SELECTION_CASE, new Long(1));
	}

	VoteMonitoringAction.logger.debug("SELECTION_CASE: " + request.getAttribute(VoteAppConstants.SELECTION_CASE));

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public ActionForward hideOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteMonitoringAction.logger.debug("dispatching hideOpenVote...");
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService :" + voteService);

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	VoteMonitoringAction.logger.debug("voteMonitoringForm :" + voteMonitoringForm);
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	String currentUid = voteMonitoringForm.getCurrentUid();
	VoteMonitoringAction.logger.debug("currentUid: " + currentUid);

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(new Long(currentUid));
	VoteMonitoringAction.logger.debug("voteUsrAttempt: " + voteUsrAttempt);

	voteUsrAttempt.setVisible(false);
	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	VoteMonitoringAction.logger.debug("hiding the user entry : " + voteUsrAttempt.getUserEntry());
	voteService.hideOpenVote(voteUsrAttempt);

	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);

	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initInstructionsContent.");
	VoteMonitoringAction.logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initStatsContent.");
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initStatsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	VoteMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

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
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("ending editActivityQuestions, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("submitting session to refresh the data from the database: ");
	return submitSession(mapping, form, request, response, voteService, getMessageService(),
		voteGeneralMonitoringDTO);
    }

    public ActionForward showOpenVote(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteMonitoringAction.logger.debug("dispatching showOpenVote...");
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService :" + voteService);

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	VoteMonitoringAction.logger.debug("voteMonitoringForm :" + voteMonitoringForm);
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	String currentUid = voteMonitoringForm.getCurrentUid();
	VoteMonitoringAction.logger.debug("currentUid: " + currentUid);

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(new Long(currentUid));
	VoteMonitoringAction.logger.debug("voteUsrAttempt: " + voteUsrAttempt);
	voteUsrAttempt.setVisible(true);

	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	voteService.showOpenVote(voteUsrAttempt);
	VoteMonitoringAction.logger.debug("voteUsrAttempt: " + voteUsrAttempt);

	String toolContentID = voteMonitoringForm.getToolContentID();
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);

	initSummaryContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initInstructionsContent.");
	VoteMonitoringAction.logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("calling initStatsContent.");
	initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
	VoteMonitoringAction.logger.debug("post initStatsContent, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);

	String currentMonitoredToolSession = voteMonitoringForm.getSelectedToolSessionId();
	VoteMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

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

	VoteMonitoringAction.logger.debug("SELECTION_CASE: " + request.getAttribute(VoteAppConstants.SELECTION_CASE));

	voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("submitting session to refresh the data from the database: ");
	return submitSession(mapping, form, request, response, voteService, getMessageService(),
		voteGeneralMonitoringDTO);
    }

    public ActionForward getVoteNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteMonitoringAction.logger.debug("dispatching getVoteNomination...");

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService :" + voteService);

	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	VoteMonitoringAction.logger.debug("voteMonitoringForm :" + voteMonitoringForm);
	voteMonitoringForm.setVoteService(voteService);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	String questionUid = request.getParameter("questionUid");
	String sessionUid = request.getParameter("sessionUid");

	VoteMonitoringAction.logger.debug("questionUid: " + questionUid);
	VoteMonitoringAction.logger.debug("sessionUid: " + sessionUid);

	List userNames = voteService.getStandardAttemptUsersForQuestionContentAndSessionUid(new Long(questionUid),
		new Long(sessionUid));
	VoteMonitoringAction.logger.debug("userNames: " + userNames);
	List listVotedLearnersDTO = new LinkedList();

	VoteContent voteContent = null;
	Iterator userIterator = userNames.iterator();
	while (userIterator.hasNext()) {
	    VoteUsrAttempt voteUsrAttempt = (VoteUsrAttempt) userIterator.next();

	    if (voteUsrAttempt != null) {
		VoteMonitoringAction.logger.debug("used voteContent is: ");
		voteContent = voteUsrAttempt.getVoteQueContent().getVoteContent();
	    }

	    VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
	    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
	    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
	    listVotedLearnersDTO.add(voteMonitoredUserDTO);
	}
	VoteMonitoringAction.logger.debug("listVoteAllSessionsDTO: " + listVotedLearnersDTO);
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	voteGeneralMonitoringDTO.setMapStudentsVoted(listVotedLearnersDTO);
	voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	VoteMonitoringAction.logger.debug("ending getVoteNomination, voteGeneralMonitoringDTO: "
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	VoteMonitoringAction.logger.debug("fdwing to: " + VoteAppConstants.VOTE_NOMINATION_VIEWER);
	return mapping.findForward(VoteAppConstants.VOTE_NOMINATION_VIEWER);
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteMonitoringAction.logger.debug("dispatching openNotebook...");
	IVoteService VoteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("VoteService: " + VoteService);

	String uid = request.getParameter("uid");
	VoteMonitoringAction.logger.debug("uid: " + uid);

	String userId = request.getParameter("userId");
	VoteMonitoringAction.logger.debug("userId: " + userId);

	String userName = request.getParameter("userName");
	VoteMonitoringAction.logger.debug("userName: " + userName);

	String sessionId = request.getParameter("sessionId");
	VoteMonitoringAction.logger.debug("sessionId: " + sessionId);

	NotebookEntry notebookEntry = VoteService.getEntry(new Long(sessionId), CoreNotebookConstants.NOTEBOOK_TOOL,
		VoteAppConstants.MY_SIGNATURE, new Integer(userId));

	VoteMonitoringAction.logger.debug("notebookEntry: " + notebookEntry);

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();
	if (notebookEntry != null) {
	    String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
	    voteGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	    voteGeneralLearnerFlowDTO.setUserName(userName);
	}

	VoteMonitoringAction.logger.debug("voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	VoteMonitoringAction.logger.debug("fwding to : " + VoteAppConstants.LEARNER_NOTEBOOK);
	return mapping.findForward(VoteAppConstants.LEARNER_NOTEBOOK);
    }

    protected void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {
	VoteMonitoringAction.logger.debug("starting repopulateRequestParameters");

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);
	voteMonitoringForm.setToolContentID(toolContentID);
	voteGeneralMonitoringDTO.setToolContentID(toolContentID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);
	voteMonitoringForm.setActiveModule(activeModule);
	voteGeneralMonitoringDTO.setActiveModule(activeModule);

	String defineLaterInEditMode = request.getParameter(VoteAppConstants.DEFINE_LATER_IN_EDIT_MODE);
	VoteMonitoringAction.logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
	voteMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

	String isToolSessionChanged = request.getParameter(VoteAppConstants.IS_TOOL_SESSION_CHANGED);
	VoteMonitoringAction.logger.debug("isToolSessionChanged: " + isToolSessionChanged);
	voteMonitoringForm.setIsToolSessionChanged(isToolSessionChanged);
	voteGeneralMonitoringDTO.setIsToolSessionChanged(isToolSessionChanged);

	String responseId = request.getParameter(VoteAppConstants.RESPONSE_ID);
	VoteMonitoringAction.logger.debug("responseId: " + responseId);
	voteMonitoringForm.setResponseId(responseId);
	voteGeneralMonitoringDTO.setResponseId(responseId);

	String currentUid = request.getParameter(VoteAppConstants.CURRENT_UID);
	VoteMonitoringAction.logger.debug("currentUid: " + currentUid);
	voteMonitoringForm.setCurrentUid(currentUid);
	voteGeneralMonitoringDTO.setCurrentUid(currentUid);
    }

    protected void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm,
	    VoteGeneralAuthoringDTO voteGeneralAuthoringDTO) {
	VoteMonitoringAction.logger.debug("starting repopulateRequestParameters");

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteMonitoringForm.setContentFolderID(contentFolderID);
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("toolContentID: " + toolContentID);
	voteMonitoringForm.setToolContentID(toolContentID);
	voteGeneralAuthoringDTO.setToolContentID(toolContentID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);
	voteMonitoringForm.setActiveModule(activeModule);
	voteGeneralAuthoringDTO.setActiveModule(activeModule);

	String defineLaterInEditMode = request.getParameter(VoteAppConstants.DEFINE_LATER_IN_EDIT_MODE);
	VoteMonitoringAction.logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
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
	VoteMonitoringAction.logger.debug("dispatching editActivityQuestions...");

	VoteMonitoringForm VoteMonitoringForm = (VoteMonitoringForm) form;
	VoteMonitoringAction.logger.debug("VoteMonitoringForm: " + VoteMonitoringForm);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	VoteGeneralMonitoringDTO generalMonitoringDTO = new VoteGeneralMonitoringDTO();

	generalMonitoringDTO.setMonitoredContentInUse(new Boolean(false).toString());

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	generalMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	VoteMonitoringAction.logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	VoteMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	VoteMonitoringForm.setContentFolderID(contentFolderID);

	String httpSessionID = request.getParameter("httpSessionID");
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);
	VoteMonitoringForm.setHttpSessionID(httpSessionID);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("existing voteContent:" + voteContent);

	VoteMonitoringForm.setTitle(voteContent.getTitle());

	VoteUtils.setDefineLater(request, true, strToolContentID, voteService);

	prepareEditActivityScreenData(request, voteContent);

	// prepareReflectionData(request, voteContent, voteService, null, false, "All");
	prepareReflectionData(request, voteContent, voteService, null, false, "All");

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	generalMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	generalMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	generalMonitoringDTO.setAttachmentList(attachmentList);
	generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("final generalMonitoringDTO: " + generalMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	List listNominationContentDTO = new LinkedList();

	Iterator queIterator = voteContent.getVoteQueContents().iterator();
	while (queIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();

	    VoteQueContent voteQueContent = (VoteQueContent) queIterator.next();
	    if (voteQueContent != null) {
		VoteMonitoringAction.logger.debug("question: " + voteQueContent.getQuestion());
		VoteMonitoringAction.logger.debug("displayorder: "
			+ new Integer(voteQueContent.getDisplayOrder()).toString());

		voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
		voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		listNominationContentDTO.add(voteNominationContentDTO);
	    }
	}
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);
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
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = generalMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug(": " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
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
	VoteMonitoringAction.logger.debug("starting prepareEditActivityScreenData: " + voteContent);
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

	VoteMonitoringAction.logger.debug("final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
    }

    /**
     * submits content into the tool database ActionForward submitAllContent(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
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

	VoteMonitoringAction.logger.debug("dispathcing submitAllContent :" + form);

	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	Map mapNominationContent = AuthoringUtil.extractMapNominationContent(listNominationContentDTO);
	VoteMonitoringAction.logger.debug("extracted mapNominationContent: " + mapNominationContent);

	Map mapFeedback = AuthoringUtil.extractMapFeedback(listNominationContentDTO);
	VoteMonitoringAction.logger.debug("extracted mapFeedback: " + mapFeedback);

	ActionMessages errors = new ActionMessages();
	VoteMonitoringAction.logger.debug("mapNominationContent size: " + mapNominationContent.size());

	if (mapNominationContent.size() == 0) {
	    ActionMessage error = new ActionMessage("nominations.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}
	VoteMonitoringAction.logger.debug("errors: " + errors);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);

	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);

	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	VoteMonitoringAction.logger.debug("there are no issues with input, continue and submit data");

	VoteContent voteContentTest = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContentTest: " + voteContentTest);

	VoteMonitoringAction.logger.debug("errors: " + errors);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    VoteMonitoringAction.logger.debug("errors saved: " + errors);
	}

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	VoteContent voteContent = voteContentTest;
	if (errors.isEmpty()) {
	    VoteMonitoringAction.logger.debug("errors is empty: " + errors);
	    /* to remove deleted entries in the questions table based on mapNominationContent */
	    authoringUtil.removeRedundantNominations(mapNominationContent, voteService, voteAuthoringForm, request,
		    strToolContentID);
	    VoteMonitoringAction.logger.debug("end of removing unused entries... ");

	    voteContent = authoringUtil.saveOrUpdateVoteContent(mapNominationContent, mapFeedback, voteService,
		    voteAuthoringForm, request, voteContentTest, strToolContentID, null);
	    VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	    long defaultContentID = 0;
	    VoteMonitoringAction.logger.debug("attempt retrieving tool with signatute : "
		    + VoteAppConstants.MY_SIGNATURE);
	    defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
	    VoteMonitoringAction.logger.debug("retrieved tool default contentId: " + defaultContentID);

	    if (voteContent != null) {
		voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	    }
	    VoteMonitoringAction.logger.debug("updated voteGeneralAuthoringDTO to: " + voteGeneralAuthoringDTO);

	    authoringUtil.reOrganizeDisplayOrder(mapNominationContent, voteService, voteAuthoringForm, voteContent);

	    VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	    VoteUtils.setDefineLater(request, false, strToolContentID, voteService);
	    VoteMonitoringAction.logger.debug("define later set to false");

	    // VoteUtils.setFormProperties(request, voteService,
	    // voteAuthoringForm, voteGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule,
	    // sessionMap, httpSessionID);

	    voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("errors is not empty: " + errors);

	    if (voteContent != null) {
		long defaultContentID = 0;
		VoteMonitoringAction.logger.debug("attempt retrieving tool with signatute : "
			+ VoteAppConstants.MY_SIGNATURE);
		defaultContentID = voteService.getToolDefaultContentIdBySignature(VoteAppConstants.MY_SIGNATURE);
		VoteMonitoringAction.logger.debug("retrieved tool default contentId: " + defaultContentID);

		if (voteContent != null) {
		    voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		}

		// VoteUtils.setFormProperties(request, voteService,
		// voteAuthoringForm, voteGeneralAuthoringDTO, strToolContentID, defaultContentIdStr, activeModule,
		// sessionMap, httpSessionID);

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

	VoteMonitoringAction.logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("forwarding to :" + VoteAppConstants.LOAD_MONITORING);
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

	VoteMonitoringAction.logger.debug("dispathcing saveSingleNomination");
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	String editNominationBoxRequest = request.getParameter("editNominationBoxRequest");
	VoteMonitoringAction.logger.debug("editNominationBoxRequest: " + editNominationBoxRequest);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	String newNomination = request.getParameter("newNomination");
	VoteMonitoringAction.logger.debug("newNomination: " + newNomination);

	String editableNominationIndex = request.getParameter("editableNominationIndex");
	VoteMonitoringAction.logger.debug("editableNominationIndex: " + editableNominationIndex);

	if (newNomination != null && newNomination.length() > 0) {
	    if (editNominationBoxRequest != null && editNominationBoxRequest.equals("false")) {
		VoteMonitoringAction.logger.debug("request for add and save");
		boolean duplicates = AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);
		VoteMonitoringAction.logger.debug("duplicates: " + duplicates);

		if (!duplicates) {
		    VoteNominationContentDTO voteNominationContentDTO = null;
		    Iterator listIterator = listNominationContentDTO.iterator();
		    while (listIterator.hasNext()) {
			voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
			VoteMonitoringAction.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
			VoteMonitoringAction.logger.debug("voteNominationContentDTO question:"
				+ voteNominationContentDTO.getQuestion());

			String question = voteNominationContentDTO.getQuestion();
			String displayOrder = voteNominationContentDTO.getDisplayOrder();
			VoteMonitoringAction.logger.debug("displayOrder:" + displayOrder);

			if (displayOrder != null && !displayOrder.equals("")) {
			    if (displayOrder.equals(editableNominationIndex)) {
				break;
			    }

			}
		    }
		    VoteMonitoringAction.logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);

		    voteNominationContentDTO.setQuestion(newNomination);
		    voteNominationContentDTO.setDisplayOrder(editableNominationIndex);

		    listNominationContentDTO = AuthoringUtil.reorderUpdateListNominationContentDTO(
			    listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
		    VoteMonitoringAction.logger
			    .debug("post reorderUpdateListNominationContentDTO listNominationContentDTO: "
				    + listNominationContentDTO);
		} else {
		    VoteMonitoringAction.logger.debug("duplicate question entry, not adding");
		}
	    } else {
		VoteMonitoringAction.logger.debug("request for edit and save.");
		VoteNominationContentDTO voteNominationContentDTO = null;
		Iterator listIterator = listNominationContentDTO.iterator();
		while (listIterator.hasNext()) {
		    voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
		    VoteMonitoringAction.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
		    VoteMonitoringAction.logger.debug("voteNominationContentDTO question:"
			    + voteNominationContentDTO.getQuestion());

		    String question = voteNominationContentDTO.getQuestion();
		    String displayOrder = voteNominationContentDTO.getDisplayOrder();
		    VoteMonitoringAction.logger.debug("displayOrder:" + displayOrder);

		    if (displayOrder != null && !displayOrder.equals("")) {
			if (displayOrder.equals(editableNominationIndex)) {
			    break;
			}

		    }
		}
		VoteMonitoringAction.logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);

		voteNominationContentDTO.setQuestion(newNomination);
		voteNominationContentDTO.setDisplayOrder(editableNominationIndex);

		listNominationContentDTO = AuthoringUtil.reorderUpdateListNominationContentDTO(
			listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
		VoteMonitoringAction.logger.debug("post reorderUpdateListQuestionContentDTO listQuestionContentDTO: "
			+ listNominationContentDTO);
	    }
	} else {
	    VoteMonitoringAction.logger.debug("entry blank, not adding");
	}

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	VoteMonitoringAction.logger.debug("listNominationContentDTO now: " + listNominationContentDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);
	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);
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
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	request.getSession().setAttribute(httpSessionID, sessionMap);
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO.getMapNominationContent(); "
		+ voteGeneralAuthoringDTO.getMapNominationContent());

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("fwd ing to LOAD_MONITORING: " + VoteAppConstants.LOAD_MONITORING);
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

	VoteMonitoringAction.logger.debug("dispathcing addSingleNomination");
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	String newNomination = request.getParameter("newNomination");
	VoteMonitoringAction.logger.debug("newNomination: " + newNomination);

	int listSize = listNominationContentDTO.size();
	VoteMonitoringAction.logger.debug("listSize: " + listSize);

	if (newNomination != null && newNomination.length() > 0) {
	    boolean duplicates = AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);
	    VoteMonitoringAction.logger.debug("duplicates: " + duplicates);

	    if (!duplicates) {
		VoteNominationContentDTO voteNominationContentDTO = new VoteNominationContentDTO();
		voteNominationContentDTO.setDisplayOrder(new Long(listSize + 1).toString());
		voteNominationContentDTO.setNomination(newNomination);

		listNominationContentDTO.add(voteNominationContentDTO);
		VoteMonitoringAction.logger.debug("updated listNominationContentDTO: " + listNominationContentDTO);
	    } else {
		VoteMonitoringAction.logger.debug("entry duplicate, not adding");

	    }
	} else {
	    VoteMonitoringAction.logger.debug("entry blank, not adding");
	}

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);
	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);
	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

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
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO.getMapNominationContent(); "
		+ voteGeneralAuthoringDTO.getMapNominationContent());
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("fwd ing to LOAD_MONITORING: " + VoteAppConstants.LOAD_MONITORING);
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
	VoteMonitoringAction.logger.debug("dispathcing newNominationBox");
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);
	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("fwd ing to newNominationBox: ");
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
	VoteMonitoringAction.logger.debug("dispathcing newEditableNominationBox");
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	VoteMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	voteAuthoringForm.setEditableNominationIndex(questionIndex);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	String editableNomination = "";
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    VoteMonitoringAction.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    VoteMonitoringAction.logger.debug("voteNominationContentDTO question:"
		    + voteNominationContentDTO.getNomination());
	    String question = voteNominationContentDTO.getNomination();
	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableNomination = voteNominationContentDTO.getNomination();
		    VoteMonitoringAction.logger.debug("editableNomination found :" + editableNomination);
		    break;
		}

	    }
	}
	VoteMonitoringAction.logger.debug("editableNomination found :" + editableNomination);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);
	voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	voteAuthoringForm.setTitle(richTextTitle);

	voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	voteGeneralAuthoringDTO.setEditableNominationText(editableNomination);
	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("fwd ing to editNominationBox: ");
	return mapping.findForward("editNominationBox");
    }

    /**
     * removes a question from the questions map ActionForward removeNomination(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
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
	VoteMonitoringAction.logger.debug("dispatching removeNomination");
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	VoteMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	VoteNominationContentDTO voteNominationContentDTO = null;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    VoteMonitoringAction.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    VoteMonitoringAction.logger.debug("voteNominationContentDTO question:"
		    + voteNominationContentDTO.getNomination());

	    String question = voteNominationContentDTO.getNomination();
	    String displayOrder = voteNominationContentDTO.getDisplayOrder();
	    VoteMonitoringAction.logger.debug("displayOrder:" + displayOrder);

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	VoteMonitoringAction.logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);
	voteNominationContentDTO.setNomination("");
	VoteMonitoringAction.logger.debug("listNominationContentDTO after remove:" + listNominationContentDTO);

	listNominationContentDTO = AuthoringUtil.reorderListNominationContentDTO(listNominationContentDTO,
		questionIndex);
	VoteMonitoringAction.logger.debug("listNominationContentDTO reordered:" + listNominationContentDTO);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	if (voteContent == null) {
	    VoteMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    voteContent = voteService.retrieveVote(new Long(defaultContentIdStr));

	}
	VoteMonitoringAction.logger.debug("final voteContent: " + voteContent);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
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
	VoteMonitoringAction.logger.debug("voteNominationContentDTO now: " + voteNominationContentDTO);
	VoteMonitoringAction.logger.debug("listNominationContentDTO now: " + listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	VoteMonitoringAction.logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("fwd ing to LOAD_MONITORING: " + VoteAppConstants.LOAD_MONITORING);
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
	VoteMonitoringAction.logger.debug("dispatching moveNominationDown");
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	VoteMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	listNominationContentDTO = AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "down");
	VoteMonitoringAction.logger.debug("listNominationContentDTO after swap: " + listNominationContentDTO);

	listNominationContentDTO = AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);
	VoteMonitoringAction.logger.debug("listNominationContentDTO after reordersimple: " + listNominationContentDTO);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
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
	VoteMonitoringAction.logger.debug("listNominationContentDTO now: " + listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	VoteMonitoringAction.logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("fwd ing to LOAD_MONITORING: " + VoteAppConstants.LOAD_MONITORING);
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
	VoteMonitoringAction.logger.debug("dispatching moveNominationUp");
	VoteMonitoringForm voteAuthoringForm = (VoteMonitoringForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	VoteMonitoringAction.logger.debug("voteService: " + voteService);

	String httpSessionID = voteAuthoringForm.getHttpSessionID();
	VoteMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	VoteMonitoringAction.logger.debug("sessionMap: " + sessionMap);

	String questionIndex = request.getParameter("questionIndex");
	VoteMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	List listNominationContentDTO = (List) sessionMap.get(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY);
	VoteMonitoringAction.logger.debug("listNominationContentDTO: " + listNominationContentDTO);

	listNominationContentDTO = AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "up");
	VoteMonitoringAction.logger.debug("listNominationContentDTO after swap: " + listNominationContentDTO);

	listNominationContentDTO = AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);
	VoteMonitoringAction.logger.debug("listNominationContentDTO after reordersimple: " + listNominationContentDTO);

	sessionMap.put(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	VoteMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	voteAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	VoteMonitoringAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	VoteMonitoringAction.logger.debug("richTextTitle: " + richTextTitle);

	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);
	VoteMonitoringAction.logger.debug("richTextInstructions: " + richTextInstructions);

	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(VoteAppConstants.DEFAULT_CONTENT_ID_STR);
	VoteMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	VoteContent voteContent = voteService.retrieveVote(new Long(strToolContentID));
	VoteMonitoringAction.logger.debug("voteContent: " + voteContent);

	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteMonitoringAction.logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
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
	VoteMonitoringAction.logger.debug("listNominationContentDTO now: " + listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	VoteMonitoringAction.logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    // prepareReflectionData(request, voteContent, voteService, null,false);
	    prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    EditActivityDTO editActivityDTO = new EditActivityDTO();
	    boolean isContentInUse = VoteUtils.isContentInUse(voteContent);
	    VoteMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	    if (isContentInUse == true) {
		editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	    }
	    request.setAttribute(VoteAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	}

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(voteService, voteContent);
	VoteMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    VoteMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	voteGeneralMonitoringDTO.setOnlineInstructions(voteContent.getOnlineInstructions());
	voteGeneralMonitoringDTO.setOfflineInstructions(voteContent.getOfflineInstructions());

	List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
	VoteMonitoringAction.logger.debug("attachmentList: " + attachmentList);
	voteGeneralMonitoringDTO.setAttachmentList(attachmentList);
	voteGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	VoteMonitoringAction.logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO"
		+ voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (notebookEntriesExist) {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = voteGeneralAuthoringDTO.getUserExceptionNoToolSessions();
	    VoteMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		VoteMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(VoteAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(VoteAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	VoteMonitoringAction.logger.debug("fwd ing to LOAD_MONITORING: " + VoteAppConstants.LOAD_MONITORING);
	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    public void prepareReflectionData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService,
	    String userID, boolean exportMode, String currentSessionId) {
	VoteMonitoringAction.logger.debug("starting prepareReflectionData: " + voteContent);
	VoteMonitoringAction.logger.debug("currentSessionId: " + currentSessionId);
	VoteMonitoringAction.logger.debug("userID: " + userID);
	VoteMonitoringAction.logger.debug("exportMode: " + exportMode);

	List reflectionsContainerDTO = new LinkedList();

	reflectionsContainerDTO = getReflectionList(voteContent, userID, voteService);

	VoteMonitoringAction.logger.debug("reflectionsContainerDTO: " + reflectionsContainerDTO);
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
	VoteMonitoringAction.logger.debug("getting reflections for all sessions");
	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    VoteMonitoringAction.logger.debug("all users mode");
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();
		VoteMonitoringAction.logger.debug("voteSession: " + voteSession);
		VoteMonitoringAction.logger.debug("voteSession sessionId: " + voteSession.getVoteSessionId());

		for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
		    VoteQueUsr user = (VoteQueUsr) userIter.next();
		    VoteMonitoringAction.logger.debug("user: " + user);

		    NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
				    .getQueUsrId().toString()));

		    VoteMonitoringAction.logger.debug("notebookEntry: " + notebookEntry);

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
	    VoteMonitoringAction.logger.debug("single user mode");
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();
		VoteMonitoringAction.logger.debug("voteSession: " + voteSession);
		for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
		    VoteQueUsr user = (VoteQueUsr) userIter.next();
		    VoteMonitoringAction.logger.debug("user: " + user);

		    if (user.getQueUsrId().toString().equals(userID)) {
			VoteMonitoringAction.logger.debug("getting reflection for user with  userID: " + userID);
			NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			VoteMonitoringAction.logger.debug("notebookEntry: " + notebookEntry);

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
     * getReflectionListForSession(VoteContent voteContent, String userID, IVoteService voteService, String
     * currentSessionId)
     * 
     * @param voteContent
     * @param userID
     * @param voteService
     * @param currentSessionId
     * @return
     */
    public List getReflectionListForSession(VoteContent voteContent, String userID, IVoteService voteService,
	    String currentSessionId) {
	VoteMonitoringAction.logger.debug("getting reflections for a specific session");
	VoteMonitoringAction.logger.debug("currentSessionId: " + currentSessionId);

	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    VoteMonitoringAction.logger.debug("all users mode");
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();
		VoteMonitoringAction.logger.debug("voteSession: " + voteSession);
		VoteMonitoringAction.logger.debug("voteSession sessionId: " + voteSession.getVoteSessionId());

		if (currentSessionId.equals(voteSession.getVoteSessionId())) {

		    for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
			VoteQueUsr user = (VoteQueUsr) userIter.next();
			VoteMonitoringAction.logger.debug("user: " + user);

			NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			VoteMonitoringAction.logger.debug("notebookEntry: " + notebookEntry);

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
	    VoteMonitoringAction.logger.debug("single user mode");
	    for (Iterator sessionIter = voteContent.getVoteSessions().iterator(); sessionIter.hasNext();) {
		VoteSession voteSession = (VoteSession) sessionIter.next();
		VoteMonitoringAction.logger.debug("voteSession: " + voteSession);

		if (currentSessionId.equals(voteSession.getVoteSessionId())) {
		    for (Iterator userIter = voteSession.getVoteQueUsers().iterator(); userIter.hasNext();) {
			VoteQueUsr user = (VoteQueUsr) userIter.next();
			VoteMonitoringAction.logger.debug("user: " + user);

			if (user.getQueUsrId().toString().equals(userID)) {
			    VoteMonitoringAction.logger.debug("getting reflection for user with  userID: " + userID);
			    NotebookEntry notebookEntry = voteService.getEntry(voteSession.getVoteSessionId(),
				    CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(
					    user.getQueUsrId().toString()));

			    VoteMonitoringAction.logger.debug("notebookEntry: " + notebookEntry);

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
