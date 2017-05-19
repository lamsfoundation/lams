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

package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

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
import org.apache.struts.action.ActionRedirect;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.AnswerDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Note: Because of MCQ's learning reporting structure, Show Learner Report is always ON even if in authoring it is set
 * to false.
 *
 * @author Ozgur Demirtas
 */
public class McLearningStarterAction extends Action implements McAppConstants {

    private static Logger logger = Logger.getLogger(McLearningStarterAction.class.getName());

    private static IMcService mcService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, McApplicationException {

	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}

	McLearningForm mcLearningForm = (McLearningForm) form;
	mcLearningForm.setMcService(mcService);
	mcLearningForm.setPassMarkApplicable(new Boolean(false).toString());
	mcLearningForm.setUserOverPassMark(new Boolean(false).toString());

	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	List<String> sequentialCheckedCa = new LinkedList<String>();
	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, sequentialCheckedCa);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	mcLearningForm.setHttpSessionID(sessionMap.getSessionID());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(new Long(toolSessionID).toString());

	/*
	 * by now, we made sure that the passed tool session id exists in the db as a new record Make sure we can
	 * retrieve it and the relavent content
	 */
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));
	if (mcSession == null) {
	    return (mapping.findForward(McAppConstants.ERROR_LIST));
	}

	/*
	 * find out what content this tool session is referring to get the content for this tool session Each passed
	 * tool session id points to a particular content. Many to one mapping.
	 */
	McContent mcContent = mcSession.getMcContent();
	if (mcContent == null) {
	    ActionMessages errors = new ActionMessages();
	    errors.add(Globals.ERROR_KEY, new ActionMessage("error.content.doesNotExist"));
	    saveErrors(request, errors);
	    return (mapping.findForward(McAppConstants.ERROR_LIST));
	}

	String mode = request.getParameter(McAppConstants.MODE);
	McQueUsr user = null;
	if ((mode != null) && mode.equals(ToolAccessMode.TEACHER.toString())) {
	    // monitoring mode - user is specified in URL
	    // user may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionID, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionID);
	}
	Long userID = user.getQueUsrId();
	mcLearningForm.setUserID(userID);

	/*
	 * Is there a deadline set?
	 */
	Date submissionDeadline = mcContent.getSubmissionDeadline();
	if (submissionDeadline != null) {

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    request.setAttribute("submissionDeadline", submissionDeadline);

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}

	mcLearningForm.setToolContentID(mcContent.getMcContentId().toString());

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
	mcGeneralLearnerFlowDTO.setTotalCountReached(new Boolean(false).toString());
	mcGeneralLearnerFlowDTO.setQuestionIndex(new Integer(1));

	Boolean displayAnswers = mcContent.isDisplayAnswers();
	mcGeneralLearnerFlowDTO.setDisplayAnswers(displayAnswers.toString());
	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());
	// String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());

	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.MY_SIGNATURE, userID.intValue());

	if (notebookEntry != null) {
	    // String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntry.getEntry());
	}
	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	List<AnswerDTO> learnerAnswersDTOList = mcService.getAnswersFromDatabase(mcContent, user);
	request.setAttribute(McAppConstants.LEARNER_ANSWERS_DTO_LIST, learnerAnswersDTOList);
	// should we show the marks for each question - we show the marks if any of the questions
	// have a mark > 1.
	Boolean showMarks = LearningUtil.isShowMarksOnQuestion(learnerAnswersDTOList);
	mcGeneralLearnerFlowDTO.setShowMarks(showMarks.toString());

	/* find out if the content is being modified at the moment. */
	boolean isDefineLater = mcContent.isDefineLater();
	if (isDefineLater == true) {
	    return (mapping.findForward("defineLater"));
	}

	McQueUsr groupLeader = null;
	if (mcContent.isUseSelectLeaderToolOuput()) {
	    groupLeader = mcService.checkLeaderSelectToolForSessionLeader(user, new Long(toolSessionID));

	    // forwards to the leaderSelection page
	    if (groupLeader == null && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		Set<McQueUsr> groupUsers = mcSession.getMcQueUsers();// mcService.getUsersBySession(new
								     // Long(toolSessionID).longValue());
		request.setAttribute(ATTR_GROUP_USERS, groupUsers);
		request.setAttribute(TOOL_SESSION_ID, toolSessionID);
		request.setAttribute(ATTR_CONTENT, mcContent);

		return mapping.findForward(WAIT_FOR_LEADER);
	    }

	    // check if leader has submitted all answers
	    if (groupLeader.isResponseFinalised() && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		// in case user joins the lesson after leader has answers some answers already - we need to make sure
		// he has the same scratches as leader
		mcService.copyAnswersFromLeader(user, groupLeader);

		user.setResponseFinalised(true);
		mcService.updateMcQueUsr(user);
	    }
	}

	sessionMap.put(ATTR_GROUP_LEADER, groupLeader);
	boolean isUserLeader = mcSession.isUserGroupLeader(user);
	sessionMap.put(ATTR_IS_USER_LEADER, isUserLeader);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(ATTR_CONTENT, mcContent);
	request.setAttribute("sessionMapID", sessionMap.getSessionID());

	/* user has already submitted response once OR it's a monitor - go to viewAnswers page. */
	if (user.isResponseFinalised() || mode.equals("teacher")) {

	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("viewAnswersRedirect"));
	    redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	    redirect.addParameter("userID", userID);
	    redirect.addParameter(MODE, mode);
	    redirect.addParameter("httpSessionID", sessionMap.getSessionID());
	    return redirect;
	}

	return (mapping.findForward(McAppConstants.LOAD_LEARNER));
    }

    private McQueUsr getCurrentUser(String toolSessionId) {

	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(toolUser.getUserID().longValue());

	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionId));
	McQueUsr qaUser = mcService.getMcUserBySession(userId, mcSession.getUid());
	if (qaUser == null) {
	    qaUser = mcService.createMcUser(new Long(toolSessionId));
	}

	return qaUser;
    }

    private McQueUsr getSpecifiedUser(String toolSessionId, Integer userId) {
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionId));
	McQueUsr qaUser = mcService.getMcUserBySession(new Long(userId.intValue()), mcSession.getUid());
	if (qaUser == null) {
	    logger.error("Unable to find specified user for Q&A activity. Screens are likely to fail. SessionId="
		    + new Long(toolSessionId) + " UserId=" + userId);
	}
	return qaUser;
    }
}
