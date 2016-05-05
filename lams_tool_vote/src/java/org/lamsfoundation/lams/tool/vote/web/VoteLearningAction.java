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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.util.VoteUtils;
import org.lamsfoundation.lams.tool.vote.web.form.VoteLearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Ozgur Demirtas
 */
public class VoteLearningAction extends LamsDispatchAction implements VoteAppConstants {
    static Logger logger = Logger.getLogger(VoteLearningAction.class.getName());

    /**
     *
     * main content/question content management and workflow logic
     *
     * if the passed toolContentID exists in the db, we need to get the relevant
     * data into the Map if not, create the default Map
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());

	repopulateRequestParameters(request, voteLearningForm);

	VoteUtils.cleanUpUserExceptions(request);
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());
	return null;
    }

    public ActionForward viewAllResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	Long toolContentID = voteContent.getVoteContentId();

	Long toolSessionUid = voteSession.getUid();

	VoteQueUsr existingVoteQueUsr = voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());

	existingVoteQueUsr.setFinalScreenRequested(true);
	voteService.updateVoteUser(existingVoteQueUsr);

	Set<String> userAttempts = voteService.getAttemptsForUserAndSession(existingVoteQueUsr.getUid(),
		toolSessionUid);
	request.setAttribute(LIST_GENERAL_CHECKED_OPTIONS_CONTENT, userAttempts);

	voteService.prepareChartData(request, toolContentID, toolSessionUid, voteGeneralLearnerFlowDTO);

	voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
	//String reflectionSubject = VoteUtils.replaceNewLines(voteContent.getReflectionSubject());
	voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());

	voteLearningForm.resetCommands();

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request,
		getServlet().getServletContext());

	return (mapping.findForward(ALL_NOMINATIONS));
    }

    public ActionForward viewAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);

	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	if (revisitingUser.equals("true")) {
	    /* get back login user DTO */
	    HttpSession ss = SessionManager.getSession();
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    long userId = toolUser.getUserID().longValue();
	    VoteQueUsr voteQueUsr = voteService.getUserByUserId(userId);

	    List attempts = voteService.getAttemptsForUser(voteQueUsr.getUid());

	    Map mapQuestionsContent = new TreeMap(new VoteComparator());
	    Iterator listIterator = attempts.iterator();
	    int order = 0;
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();
		VoteQueContent voteQueContent = attempt.getVoteQueContent();
		order++;
		if (voteQueContent != null) {
		    mapQuestionsContent.put(new Integer(order).toString(), voteQueContent.getQuestion());
		}
	    }
	    request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapQuestionsContent);
	} else {
	    //this is not a revisiting user
	}

	voteLearningForm.resetCommands();

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request,
		getServlet().getServletContext());
	return (mapping.findForward(VIEW_ANSWERS));
    }

    public ActionForward redoQuestionsOk(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();

	repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	VoteContent voteContent = voteSession.getVoteContent();
	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
	//String reflectionSubject = VoteUtils.replaceNewLines(voteContent.getReflectionSubject());
	voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());

	voteLearningForm.resetCommands();

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
	return redoQuestions(mapping, form, request, response);
    }

    public ActionForward learnerFinished(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));
	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	VoteQueUsr voteQueUsr = voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());

	voteQueUsr.setResponseFinalised(true);
	if (!voteSession.getVoteContent().isShowResults()) {
	    // if not isShowResults then we will have skipped the final screen.
	    voteQueUsr.setFinalScreenRequested(true);
	}
	voteService.updateVoteUser(voteQueUsr);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
	//String reflectionSubject = VoteUtils.replaceNewLines(voteContent.getReflectionSubject());
	voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	VoteUtils.cleanUpUserExceptions(request);

	String nextUrl = null;
	try {
	    nextUrl = voteService.leaveToolSession(new Long(toolSessionID), new Long(userID));
	} catch (DataMissingException e) {
	    logger.error("failure getting nextUrl: " + e);
	    voteLearningForm.resetCommands();
	    //throw new ServletException(e);
	    return (mapping.findForward(LEARNING_STARTER));
	} catch (ToolException e) {
	    logger.error("failure getting nextUrl: " + e);
	    voteLearningForm.resetCommands();
	    //throw new ServletException(e);
	    return (mapping.findForward(LEARNING_STARTER));
	} catch (Exception e) {
	    logger.error("unknown exception getting nextUrl: " + e);
	    voteLearningForm.resetCommands();
	    //throw new ServletException(e);
	    return (mapping.findForward(LEARNING_STARTER));
	}

	voteLearningForm.resetCommands();

	/* pay attention here */
	response.sendRedirect(nextUrl);

	return null;

    }

    public ActionForward continueOptionsCombined(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	String maxNominationCount = request.getParameter(MAX_NOMINATION_COUNT);
	voteLearningForm.setMaxNominationCount(maxNominationCount);

	String minNominationCount = request.getParameter(MIN_NOMINATION_COUNT);
	voteLearningForm.setMinNominationCount(minNominationCount);

	String userEntry = request.getParameter(USER_ENTRY);
	voteLearningForm.setUserEntry(userEntry);

	VoteSession session = voteService.getSessionBySessionId(new Long(toolSessionID));
	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	voteLearningForm.setMinNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMinNominationCountReached(new Boolean(false).toString());

	Collection<String> voteDisplayOrderIds = voteLearningForm.votesAsCollection();

	// check number of votes
	int castVoteCount = voteDisplayOrderIds != null ? voteDisplayOrderIds.size() : 0;
	if ((userEntry != null) && (userEntry.length() > 0)) {
	    ++castVoteCount;
	}

	int intMaxNominationCount = 0;
	if (maxNominationCount != null) {
	    intMaxNominationCount = new Integer(maxNominationCount).intValue();
	}

	if (castVoteCount > intMaxNominationCount) {
	    voteLearningForm.setMaxNominationCountReached(new Boolean(true).toString());
	    voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(true).toString());
	    persistInRequestError(request, "error.maxNominationCount.reached");
	    return (mapping.findForward(LOAD_LEARNER));
	}

	VoteContent voteContent = session.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	Long toolContentID = voteContent.getVoteContentId();
	Long voteContentUid = voteContent.getUid();

	boolean userEntryAvailable = false;
	if ((userEntry != null) && (userEntry.length() > 0)) {
	    userEntryAvailable = true;
	}

	Long toolSessionUid = session.getUid();

	VoteQueUsr user = voteService.getVoteUserBySession(new Long(userID), session.getUid());
	if (user == null) {
	    throw new VoteApplicationException(
		    "User with userId= " + userID + " and sessionUid= " + session.getUid() + " doesn't exist.");
	}

	voteService.removeAttemptsForUserandSession(user.getUid(), session.getUid());

	/*
	 * to minimize changes to working code, convert the String[] array to the mapGeneralCheckedOptionsContent
	 * structure
	 */
	Map<String, String> mapGeneralCheckedOptionsContent = voteService.buildQuestionMap(voteContent,
		voteDisplayOrderIds);

	if (mapGeneralCheckedOptionsContent.size() > 0) {
	    voteService.createAttempt(user, mapGeneralCheckedOptionsContent, "", session, voteContentUid);
	}

	if ((mapGeneralCheckedOptionsContent.size() == 0 && (userEntryAvailable == true))) {
	    Map mapLeanerCheckedOptionsContent = new TreeMap(new VoteComparator());

	    if (userEntry.length() > 0) {
		voteService.createAttempt(user, mapLeanerCheckedOptionsContent, userEntry, session, voteContentUid);
	    }
	}

	if ((mapGeneralCheckedOptionsContent.size() > 0) && (userEntryAvailable == true)) {
	    Map mapLeanerCheckedOptionsContent = new TreeMap(new VoteComparator());

	    if (userEntry.length() > 0) {
		voteService.createAttempt(user, mapLeanerCheckedOptionsContent, userEntry, session, voteContentUid);
	    }
	}

	/* put the map in the request ready for the next screen */
	request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
	voteLearningForm.setMapGeneralCheckedOptionsContent(mapGeneralCheckedOptionsContent);
	voteGeneralLearnerFlowDTO.setMapGeneralCheckedOptionsContent(mapGeneralCheckedOptionsContent);

	voteLearningForm.setNominationsSubmited(new Boolean(true).toString());
	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(true).toString());

	voteService.prepareChartData(request, toolContentID, toolSessionUid, voteGeneralLearnerFlowDTO);

	voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
	voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());

	voteLearningForm.resetCommands();
	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	return (mapping.findForward(INDIVIDUAL_REPORT));
    }

    public ActionForward redoQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	VoteUtils.cleanUpUserExceptions(request);

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	repopulateRequestParameters(request, voteLearningForm);

	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));

	VoteContent voteContent = voteSession.getVoteContent();

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());

	Map<String, String> mapQuestionsContent = voteService.buildQuestionMap(voteContent, null);
	request.setAttribute(MAP_QUESTION_CONTENT_LEARNER, mapQuestionsContent);

	Map mapGeneralCheckedOptionsContent = new TreeMap(new VoteComparator());
	request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);

	voteLearningForm.setUserEntry("");

	voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
	voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());
	voteLearningForm.resetCommands();
	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	return (mapping.findForward(LOAD_LEARNER));
    }

    /**
     * persists error messages to request scope
     *
     * @param request
     * @param message
     */
    public void persistInRequestError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	saveErrors(request, errors);
    }

    /**
     * checks Leader Progress
     */
    public ActionForward checkLeaderProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	VoteSession session = voteService.getSessionBySessionId(toolSessionId);
	VoteQueUsr leader = session.getGroupLeader();

	boolean isLeaderResponseFinalized = leader.isResponseFinalised();

	JSONObject JSONObject = new JSONObject();
	JSONObject.put("isLeaderResponseFinalized", isLeaderResponseFinalized);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;

	repopulateRequestParameters(request, voteLearningForm);

	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	voteLearningForm.setUserID(userID);

	String reflectionEntry = request.getParameter(ENTRY_TEXT);
	logger.info("reflection entry: " + reflectionEntry);

	voteService.createNotebookEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE,
		new Integer(userID), reflectionEntry);

	voteLearningForm.resetUserActions(); /* resets all except submitAnswersContent */
	return learnerFinished(mapping, form, request, response);
    }

    public ActionForward forwardtoReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	VoteSession voteSession = voteService.getSessionBySessionId(new Long(toolSessionID));

	VoteContent voteContent = voteSession.getVoteContent();
	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();
	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());

	String reflectionSubject = voteContent.getReflectionSubject();
	//reflectionSubject = VoteUtils.replaceNewLines(reflectionSubject);

	voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());

	String userID = request.getParameter("userID");
	voteLearningForm.setUserID(userID);

	NotebookEntry notebookEntry = voteService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    String notebookEntryPresentable = notebookEntry.getEntry();
	    voteGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	    voteLearningForm.setEntryText(notebookEntryPresentable);
	}

	request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	voteLearningForm.resetCommands();

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request,
		getServlet().getServletContext());

	return (mapping.findForward(NOTEBOOK));
    }

    protected void repopulateRequestParameters(HttpServletRequest request, VoteLearningForm voteLearningForm) {
	String toolSessionID = request.getParameter(TOOL_SESSION_ID);
	voteLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter(USER_ID);
	voteLearningForm.setUserID(userID);

	String revisitingUser = request.getParameter(REVISITING_USER);
	voteLearningForm.setRevisitingUser(revisitingUser);

	String previewOnly = request.getParameter(PREVIEW_ONLY);
	voteLearningForm.setPreviewOnly(previewOnly);

	String maxNominationCount = request.getParameter(MAX_NOMINATION_COUNT);
	voteLearningForm.setMaxNominationCount(maxNominationCount);

	String minNominationCount = request.getParameter(MIN_NOMINATION_COUNT);
	voteLearningForm.setMinNominationCount(minNominationCount);

	String useSelectLeaderToolOuput = request.getParameter(USE_SELECT_LEADER_TOOL_OUTPUT);
	voteLearningForm.setUseSelectLeaderToolOuput(useSelectLeaderToolOuput);

	String allowTextEntry = request.getParameter(ALLOW_TEXT_ENTRY);
	voteLearningForm.setAllowTextEntry(allowTextEntry);

	String showResults = request.getParameter(SHOW_RESULTS);
	voteLearningForm.setShowResults(showResults);

	String lockOnFinish = request.getParameter(LOCK_ON_FINISH);
	voteLearningForm.setLockOnFinish(lockOnFinish);

	String reportViewOnly = request.getParameter(REPORT_VIEW_ONLY);
	voteLearningForm.setReportViewOnly(reportViewOnly);

	String userEntry = request.getParameter(USER_ENTRY);
	voteLearningForm.setUserEntry(userEntry);

	String groupLeaderName = request.getParameter(ATTR_GROUP_LEADER_NAME);
	voteLearningForm.setGroupLeaderName(groupLeaderName);

	boolean isUserLeader = WebUtil.readBooleanParam(request, "userLeader");
	voteLearningForm.setIsUserLeader(isUserLeader);
    }

    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	return VoteServiceProxy.getMessageService(getServlet().getServletContext());
    }
}
