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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
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
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * @author Ozgur Demirtas
 *
 * Note:  Because of Voting learning reporting structure, Show Learner Report is always ON even if in authoring it is set to false.
 */
public class VoteLearningStarterAction extends Action implements VoteAppConstants {
    private static Logger logger = Logger.getLogger(VoteLearningStarterAction.class.getName());
    
    private static IVoteService voteService;

    /*
     * By now, the passed tool session id MUST exist in the db through the calling of: public void
     * createToolSession(Long toolSessionID, Long toolContentId) by the container.
     * 
     * 
     * make sure this session exists in tool's session table by now.
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, VoteApplicationException {

	VoteUtils.cleanUpSessionAbsolute(request);
	
	if (voteService == null) {
	    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	}

	Map mapQuestionsContent = new TreeMap(new VoteComparator());

	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO = new VoteGeneralLearnerFlowDTO();
	VoteLearningForm voteLearningForm = (VoteLearningForm) form;

	voteLearningForm.setRevisitingUser(new Boolean(false).toString());
	voteLearningForm.setUserEntry("");
	voteLearningForm.setCastVoteCount(0);
	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());

	voteGeneralLearnerFlowDTO.setRevisitingUser(new Boolean(false).toString());
	voteGeneralLearnerFlowDTO.setUserEntry("");
	voteGeneralLearnerFlowDTO.setCastVoteCount("0");
	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());

	/*
	 * save time zone information to session scope.
	 */
	VoteUtils.saveTimeZone(request);
	ActionForward validateParameters = validateParameters(request, mapping, voteLearningForm);
	if (validateParameters != null) {
	    return validateParameters;
	}

	String toolSessionID = voteLearningForm.getToolSessionID();

	/*
	 * by now, we made sure that the passed tool session id exists in the db as a new record Make sure we can
	 * retrieve it and the relavent content
	 */

	VoteSession voteSession = voteService.retrieveVoteSession(new Long(toolSessionID));

	if (voteSession == null) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    VoteLearningStarterAction.logger.error("error: The tool expects voteSession.");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}

	/*
	 * find out what content this tool session is referring to get the content for this tool session Each passed
	 * tool session id points to a particular content. Many to one mapping.
	 */
	VoteContent voteContent = voteSession.getVoteContent();
	if (voteContent == null) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    VoteLearningStarterAction.logger.error("error: The tool expects voteContent.");
	    persistInRequestError(request, "error.content.doesNotExist");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}

	/*
	 * The content we retrieved above must have been created before in Authoring time. And the passed tool session
	 * id already refers to it.
	 */
	setupAttributes(request, voteContent, voteLearningForm, voteGeneralLearnerFlowDTO);

	voteLearningForm.setToolContentID(voteContent.getVoteContentId().toString());
	voteGeneralLearnerFlowDTO.setToolContentID(voteContent.getVoteContentId().toString());

	voteLearningForm.setToolContentUID(voteContent.getUid().toString());
	voteGeneralLearnerFlowDTO.setToolContentUID(voteContent.getUid().toString());

	voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
	String reflectionSubject = VoteUtils.replaceNewLines(voteContent.getReflectionSubject());
	voteGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	String mode = voteLearningForm.getLearningMode();
	voteGeneralLearnerFlowDTO.setLearningMode(mode);
	
	String userId = voteLearningForm.getUserID();
	NotebookEntry notebookEntry = voteService.getEntry(new Long(toolSessionID),
		CoreNotebookConstants.NOTEBOOK_TOOL, VoteAppConstants.MY_SIGNATURE, new Integer(userId));
	if (notebookEntry != null) {
	    String notebookEntryPresentable = VoteUtils.replaceNewLines(notebookEntry.getEntry());
	    voteGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	}

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

	/* handle PREVIEW mode */
	if (mode != null && mode.equals("author")) {
	    commonContentSetup(request, voteService, voteContent, voteGeneralLearnerFlowDTO);
	    request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
	}
	
	VoteQueUsr user = null;
	if ((mode != null) && mode.equals(ToolAccessMode.TEACHER.toString())) {
	    // monitoring mode - user is specified in URL
	    // user may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionID, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionID);
	}
	
	// check if there is submission deadline
	Date submissionDeadline = voteContent.getSubmissionDeadline();

	if (submissionDeadline != null) {

	    request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    voteGeneralLearnerFlowDTO.setSubmissionDeadline(tzSubmissionDeadline);

	    // calculate whether deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request, getServlet()
		.getServletContext());

	/* find out if the content is being modified at the moment. */
	boolean isDefineLater = VoteUtils.isDefineLater(voteContent);
	if (isDefineLater == true) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    return mapping.findForward(VoteAppConstants.DEFINE_LATER);
	}
	
	//process group leader
	VoteQueUsr groupLeader = null;
	if (voteContent.isUseSelectLeaderToolOuput()) {
	    groupLeader = voteService.checkLeaderSelectToolForSessionLeader(user, new Long(toolSessionID));
	    
	    // forwards to the leaderSelection page
	    if (groupLeader == null && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		Set<VoteQueUsr> groupUsers = voteSession.getVoteQueUsers();
		request.setAttribute(ATTR_GROUP_USERS, groupUsers);
		request.setAttribute(TOOL_SESSION_ID, toolSessionID);
		request.setAttribute(ATTR_CONTENT, voteContent);

		return mapping.findForward(WAIT_FOR_LEADER);
	    }

	    // check if leader has submitted all answers
	    if (groupLeader.isResponseFinalised() && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		// in case user joins the lesson after leader has answers some answers already - we need to make sure
		// he has the same scratches as leader
		voteService.copyAnswersFromLeader(user, groupLeader);

		user.setFinalScreenRequested(true);
		user.setResponseFinalised(true);
		voteService.updateVoteUser(user);
	    }
	    
	    // store group leader information
	    voteLearningForm.setGroupLeaderName(groupLeader.getFullname());
	    boolean isUserLeader = voteService.isUserGroupLeader(user, new Long(toolSessionID));
	    voteLearningForm.setIsUserLeader(isUserLeader);
	}

	if (mode.equals("teacher")) {

	    Long sessionUid = user.getVoteSessionId();
	    putMapQuestionsContentIntoRequest(request, voteService, user);
	    Set<String> userAttempts = voteService.getAttemptsForUserAndSessionUseOpenAnswer(user.getUid(), sessionUid);
	    request.setAttribute(VoteAppConstants.LIST_GENERAL_CHECKED_OPTIONS_CONTENT, userAttempts);

	    // since this is progress view, present a screen which can not be edited
	    voteLearningForm.setReportViewOnly(new Boolean(true).toString());
	    voteGeneralLearnerFlowDTO.setReportViewOnly(new Boolean(true).toString());

	    voteService.prepareChartData(request, voteContent.getVoteContentId(), voteSession.getUid(),
		    voteGeneralLearnerFlowDTO);

	    boolean isGroupedActivity = voteService.isGroupedActivity(new Long(voteLearningForm.getToolContentID()));
	    request.setAttribute("isGroupedActivity", isGroupedActivity);

	    return mapping.findForward(VoteAppConstants.EXIT_PAGE);
	}

	/*
	 * fetch question content from content
	 */
	mapQuestionsContent = LearningUtil.buildQuestionContentMap(request, voteService, voteContent, null);
	request.setAttribute(VoteAppConstants.MAP_QUESTION_CONTENT_LEARNER, mapQuestionsContent);
	request.setAttribute(VoteAppConstants.MAP_OPTIONS_CONTENT, mapQuestionsContent);

	/*
	 * the user's session id AND user id exists in the tool tables goto this screen if the OverAll Results scren has
	 * been already called up by this user
	 */
	if (user.isFinalScreenRequested()) {
	    Long sessionUid = user.getVoteSessionId();
	    VoteSession voteUserSession = voteService.getVoteSessionByUID(sessionUid);
	    String userSessionId = voteUserSession.getVoteSessionId().toString();

	    if (toolSessionID.toString().equals(userSessionId)) {
		// the learner has already responsed to this content, just generate a read-only report. Use redo
		// questions for this
		putMapQuestionsContentIntoRequest(request, voteService, user);

		boolean isResponseFinalised = user.isResponseFinalised();
		if (isResponseFinalised) {
		    // since the response is finalised present a screen which can not be edited
		    voteLearningForm.setReportViewOnly(new Boolean(true).toString());
		    voteGeneralLearnerFlowDTO.setReportViewOnly(new Boolean(true).toString());
		}

		Set userAttempts = voteService.getAttemptsForUserAndSessionUseOpenAnswer(user.getUid(),
			sessionUid);
		request.setAttribute(VoteAppConstants.LIST_GENERAL_CHECKED_OPTIONS_CONTENT, userAttempts);

		voteService.prepareChartData(request, voteContent.getVoteContentId(), voteSession.getUid(),
			voteGeneralLearnerFlowDTO);

		String isContentLockOnFinish = voteLearningForm.getLockOnFinish();
		if (isContentLockOnFinish.equals(new Boolean(true).toString()) && isResponseFinalised == true) {
		    // user with session id: userSessionId should not redo votes. session is locked
		    return mapping.findForward(VoteAppConstants.EXIT_PAGE);
		}

		voteLearningForm.setRevisitingUser(new Boolean(true).toString());
		voteGeneralLearnerFlowDTO.setRevisitingUser(new Boolean(true).toString());
		request.setAttribute(VoteAppConstants.VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);

		if (isContentLockOnFinish.equals(new Boolean(false).toString()) && isResponseFinalised == true) {
		    // isContentLockOnFinish is false, enable redo of votes
		    return mapping.findForward(VoteAppConstants.REVISITED_ALL_NOMINATIONS);
		}

		return mapping.findForward(VoteAppConstants.ALL_NOMINATIONS);
	    }
	}
	// presenting standard learner screen..
	return mapping.findForward(VoteAppConstants.LOAD_LEARNER);
    }

    /**
     * Build the attempts map and put it in the request, based on the supplied user. If the user is null then the map
     * will be set but it will be empty TODO This shouldn't go in the request, it should go in our special session map.
     */
    private void putMapQuestionsContentIntoRequest(HttpServletRequest request, IVoteService voteService,
	    VoteQueUsr user) {
	List attempts = null;
	if (user != null) {
	    attempts = voteService.getAttemptsForUser(user.getUid());
	}
	Map localMapQuestionsContent = new TreeMap(new VoteComparator());

	if (attempts != null) {

	    Iterator listIterator = attempts.iterator();
	    int order = 0;
	    while (listIterator.hasNext()) {
		VoteUsrAttempt attempt = (VoteUsrAttempt) listIterator.next();
		VoteQueContent voteQueContent = attempt.getVoteQueContent();
		order++;
		if (voteQueContent != null) {
		    String entry = voteQueContent.getQuestion();

		    String voteQueContentId = attempt.getVoteQueContent().getUid().toString();
		    if (entry != null) {
			if (entry.equals("sample nomination") && voteQueContentId.equals("1")) {
			    localMapQuestionsContent.put(new Integer(order).toString(), attempt.getUserEntry());
			} else {
			    localMapQuestionsContent.put(new Integer(order).toString(), voteQueContent.getQuestion());
			}
		    }
		}
	    }
	}

	request.setAttribute(VoteAppConstants.MAP_GENERAL_CHECKED_OPTIONS_CONTENT, localMapQuestionsContent);
    }

    /**
     * sets up question and candidate answers maps commonContentSetup(HttpServletRequest request, VoteContent
     * voteContent)
     * 
     * @param request
     * @param voteContent
     */
    protected void commonContentSetup(HttpServletRequest request, IVoteService voteService, VoteContent voteContent,
	    VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO) {
	Map mapQuestionsContent = LearningUtil.buildQuestionContentMap(request, voteService, voteContent, null);

	request.setAttribute(VoteAppConstants.MAP_QUESTION_CONTENT_LEARNER, mapQuestionsContent);
	voteGeneralLearnerFlowDTO.setTotalQuestionCount(new Long(mapQuestionsContent.size()).toString());
    }

    /**
     * sets up session scope attributes based on content linked to the passed tool session id
     * setupAttributes(HttpServletRequest request, VoteContent voteContent)
     * 
     * @param request
     * @param voteContent
     */
    protected void setupAttributes(HttpServletRequest request, VoteContent voteContent,
	    VoteLearningForm voteLearningForm, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO) {

	Map mapGeneralCheckedOptionsContent = new TreeMap(new VoteComparator());
	request.setAttribute(VoteAppConstants.MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);

	voteLearningForm.setActivityTitle(voteContent.getTitle());
	voteLearningForm.setActivityInstructions(voteContent.getInstructions());
	voteLearningForm.setMaxNominationCount(voteContent.getMaxNominationCount());
	voteLearningForm.setMinNominationCount(voteContent.getMinNominationCount());
	voteLearningForm.setUseSelectLeaderToolOuput(new Boolean(voteContent.isUseSelectLeaderToolOuput()).toString());
	voteLearningForm.setAllowTextEntry(new Boolean(voteContent.isAllowText()).toString());
	voteLearningForm.setShowResults(new Boolean(voteContent.isShowResults()).toString());
	voteLearningForm.setLockOnFinish(new Boolean(voteContent.isLockOnFinish()).toString());

	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());
	voteGeneralLearnerFlowDTO.setMaxNominationCount(voteContent.getMaxNominationCount());
	voteGeneralLearnerFlowDTO.setMinNominationCount(voteContent.getMinNominationCount());
	voteGeneralLearnerFlowDTO.setUseSelectLeaderToolOuput(new Boolean(voteContent.isUseSelectLeaderToolOuput())
		.toString());
	voteGeneralLearnerFlowDTO.setAllowTextEntry(new Boolean(voteContent.isAllowText()).toString());
	voteGeneralLearnerFlowDTO.setLockOnFinish(new Boolean(voteContent.isLockOnFinish()).toString());
	voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());
    }

    protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping,
	    VoteLearningForm voteLearningForm) {
	/*
	 * obtain and setup the current user's data
	 */

	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null && user.getUserID() != null) {
		userID = user.getUserID().toString();
		voteLearningForm.setUserID(userID);
	    }
	}

	/*
	 * process incoming tool session id and later derive toolContentId from it.
	 */
	String strToolSessionId = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	long toolSessionID = 0;
	if (strToolSessionId == null || strToolSessionId.length() == 0) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    // persistInRequestError(request, "error.toolSessionId.required");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	} else {
	    try {
		toolSessionID = new Long(strToolSessionId).longValue();
		voteLearningForm.setToolSessionID(new Long(toolSessionID).toString());
	    } catch (NumberFormatException e) {
		VoteUtils.cleanUpSessionAbsolute(request);
		// persistInRequestError(request, "error.sessionId.numberFormatException");
		VoteLearningStarterAction.logger.error("add error.sessionId.numberFormatException to ActionMessages.");
		return mapping.findForward(VoteAppConstants.ERROR_LIST);
	    }
	}

	/* mode can be learner, teacher or author */
	String mode = request.getParameter(VoteAppConstants.MODE);

	if (mode == null || mode.length() == 0) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    VoteLearningStarterAction.logger.error("mode missing: ");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}

	if (!mode.equals("learner") && !mode.equals("teacher") && !mode.equals("author")) {
	    VoteUtils.cleanUpSessionAbsolute(request);
	    VoteLearningStarterAction.logger.error("mode invalid: ");
	    return mapping.findForward(VoteAppConstants.ERROR_LIST);
	}
	voteLearningForm.setLearningMode(mode);

	return null;
    }

    boolean isSessionCompleted(String userSessionId, IVoteService voteService) {
	VoteSession voteSession = voteService.retrieveVoteSession(new Long(userSessionId));
	if (voteSession.getSessionStatus() != null && voteSession.getSessionStatus().equals(VoteAppConstants.COMPLETED)) {
	    return true;
	}
	return false;
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
    
    private VoteQueUsr getCurrentUser(String toolSessionId) {

	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(toolUser.getUserID().longValue());

	VoteSession session = voteService.retrieveVoteSession(new Long(toolSessionId));
	VoteQueUsr user = voteService.getVoteUserBySession(userId, session.getUid());
	if (user == null) {
	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	    user = new VoteQueUsr(userId, userName, fullName, session, new TreeSet());
	    voteService.createVoteQueUsr(user);
	}

	return user;
    }

    private VoteQueUsr getSpecifiedUser(String toolSessionId, Integer userId) {
	VoteSession session = voteService.retrieveVoteSession(new Long(toolSessionId));
	VoteQueUsr user = voteService.getVoteUserBySession(new Long(userId.intValue()), session.getUid());
	if (user == null) {
	    logger.error("Unable to find specified user for Vote activity. Screens are likely to fail. SessionId="
		    + new Long(toolSessionId) + " UserId=" + userId);
	}
	return user;
    }

    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	return VoteServiceProxy.getMessageService(getServlet().getServletContext());
    }
}
