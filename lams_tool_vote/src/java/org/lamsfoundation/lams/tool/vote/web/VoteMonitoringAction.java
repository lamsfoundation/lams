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

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.EditActivityDTO;
import org.lamsfoundation.lams.tool.vote.dto.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteAllGroupsDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteStringComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.form.VoteMonitoringForm;
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
	
	ActionRedirect redirect = new ActionRedirect(
		mapping.findForwardConfig(VoteAppConstants.MONITORING_STARTER_REDIRECT));
	redirect.addParameter(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);
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
	
	ActionRedirect redirect = new ActionRedirect(
		mapping.findForwardConfig(VoteAppConstants.MONITORING_STARTER_REDIRECT));
	redirect.addParameter(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);
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
	    userAttempts = voteService.getStandardAttemptsForQuestionContentAndContentUid(new Long(questionUid));
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

	List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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
	
	List listQuestions = voteService.getAllQuestionEntries(voteContent.getUid());
	List listAllGroupsContainerDTO = new LinkedList();

	Iterator iteratorSession = voteContent.getVoteSessions().iterator();
	while (iteratorSession.hasNext()) {
	    VoteSession voteSession = (VoteSession) iteratorSession.next();
	    String currentSessionId = voteSession.getVoteSessionId().toString();

	    String currentSessionName = voteSession.getSession_name();

	    VoteAllGroupsDTO voteAllGroupsDTO = new VoteAllGroupsDTO();
	    List listMonitoredAnswersContainerDTO = new LinkedList();

	    if (voteSession != null) {
		Iterator itListQuestions = listQuestions.iterator();
		while (itListQuestions.hasNext()) {
		    VoteQueContent voteQueContent = (VoteQueContent) itListQuestions.next();

		    if (voteQueContent != null) {
			VoteMonitoredAnswersDTO voteMonitoredAnswersDTO = new VoteMonitoredAnswersDTO();
			voteMonitoredAnswersDTO.setQuestionUid(voteQueContent.getUid().toString());
			voteMonitoredAnswersDTO.setQuestion(voteQueContent.getQuestion());
			voteMonitoredAnswersDTO.setSessionId(currentSessionId);
			voteMonitoredAnswersDTO.setSessionName(currentSessionName);

			String questionUid = voteQueContent.getUid().toString();

			List listMonitoredAttemptsContainerDTO = new LinkedList();

			Map<String, String> summaryToolSessions = MonitoringUtil.populateToolSessionsId(voteContent,
				voteService);

			Iterator itMap = summaryToolSessions.entrySet().iterator();

			/* request is for monitoring summary */

			if (currentSessionId != null) {
			    if (currentSessionId.equals("All")) {
				// **summary request is for All**
				while (itMap.hasNext()) {
				    Map.Entry pairs = (Map.Entry) itMap.next();
				    if (!(pairs.getValue().toString().equals("None"))
					    && !(pairs.getValue().toString().equals("All"))) {
					VoteSession voteSession2 = voteService.retrieveVoteSession(new Long(pairs
						.getValue().toString()));
					if (voteSession2 != null) {
					    List<VoteQueUsr> listUsers = voteService.getUserBySessionOnly(voteSession2);
					    Map sessionUsersAttempts = VoteMonitoringAction.populateSessionUsersAttempts(
						    request, voteService, voteSession2.getVoteSessionId(), listUsers,
						    questionUid);
					    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
					}
				    }
				}
			    } else if (!currentSessionId.equals("All")) {
				// **summary request is for currentSessionId** currentSessionId

				List listUsers = voteService.getUserBySessionOnly(voteSession);

				Map sessionUsersAttempts = VoteMonitoringAction.populateSessionUsersAttempts(request,
					voteService, new Long(currentSessionId), listUsers, questionUid);
				listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			    }
			}

			Map<String, Map> questionAttemptData = MonitoringUtil.convertToMap(listMonitoredAttemptsContainerDTO);
			
			
			voteMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);

			listMonitoredAnswersContainerDTO.add(voteMonitoredAnswersDTO);
		    }
		}
	    }
	    voteAllGroupsDTO.setGroupData(listMonitoredAnswersContainerDTO);
	    voteAllGroupsDTO.setSessionName(currentSessionName);
	    voteAllGroupsDTO.setSessionId(currentSessionId);

	    listAllGroupsContainerDTO.add(voteAllGroupsDTO);

	}
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsContainerDTO);

	return mapping.findForward(VoteAppConstants.LOAD_MONITORING);
    }

    private static Map populateSessionUsersAttempts(HttpServletRequest request, IVoteService voteService,
	    Long sessionId, List<VoteQueUsr> users, String questionUid) {

	List listMonitoredUserContainerDTO = new LinkedList();
	Iterator itUsers = users.iterator();

	while (itUsers.hasNext()) {
	    VoteQueUsr voteQueUsr = (VoteQueUsr) itUsers.next();

	    if (voteQueUsr != null) {
		List listUserAttempts = voteService.getAttemptsForUserAndQuestionContent(voteQueUsr.getUid(), new Long(
			questionUid));

		Iterator itAttempts = listUserAttempts.iterator();
		while (itAttempts.hasNext()) {
		    VoteUsrAttempt voteUsrResp = (VoteUsrAttempt) itAttempts.next();

		    if (voteUsrResp != null) {
			VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
			voteMonitoredUserDTO.setAttemptTime(voteUsrResp.getAttemptTime());
			// voteMonitoredUserDTO.setTimeZone(voteUsrResp.getTimezone());
			voteMonitoredUserDTO.setUid(voteUsrResp.getUid().toString());
			voteMonitoredUserDTO.setUserName(voteQueUsr.getFullname());
			voteMonitoredUserDTO.setQueUsrId(voteQueUsr.getUid().toString());
			voteMonitoredUserDTO.setSessionId(sessionId.toString());
			voteMonitoredUserDTO.setResponse(voteUsrResp.getUserEntry());

			String responsePresentable = VoteUtils.replaceNewLines(voteUsrResp.getUserEntry());
			voteMonitoredUserDTO.setResponsePresentable(responsePresentable);

			voteMonitoredUserDTO.setQuestionUid(questionUid);
			voteMonitoredUserDTO.setVisible(new Boolean(voteUsrResp.isVisible()).toString());
			listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
		    }
		}
	    }
	}
	
	//convertToMcMonitoredUserDTOMap
	Map mapMonitoredUserContainerDTO = new TreeMap(new VoteStringComparator());
	Iterator listIterator = listMonitoredUserContainerDTO.iterator();
	Long mapIndex = new Long(1);
	while (listIterator.hasNext()) {
	    VoteMonitoredUserDTO data = (VoteMonitoredUserDTO) listIterator.next();
	    mapMonitoredUserContainerDTO.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return mapMonitoredUserContainerDTO;
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
	voteAuthoringForm.setCurrentTab("2");

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	if (voteService.studentActivityOccurredGlobal(voteContent)) {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	if (voteContent != null) {
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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
	voteAuthoringForm.setCurrentTab("2");

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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
	voteAuthoringForm.setCurrentTab("2");

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
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
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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
	voteAuthoringForm.setCurrentTab("2");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);

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
	voteAuthoringForm.setCurrentTab("2");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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
	voteAuthoringForm.setCurrentTab("2");

	request.setAttribute(VoteAppConstants.LIST_NOMINATION_CONTENT_DTO, listNominationContentDTO);

	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	request.setAttribute(VoteAppConstants.TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));

	if (voteContent != null) {
	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(voteContent, null);
	    request.setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

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
