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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

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
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerStarterDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * 
 * @author Ozgur Demirtas
 *
 <!--Learning Starter Action: initializes the Learning module -->
 <action 	path="/learningStarter" 
 type="org.lamsfoundation.lams.tool.mc.web.McLearningStarterAction" 
 name="McLearningForm" 
 scope="request"
 validate="false"
 unknown="false"
 input="/learningIndex.jsp"> 

 <forward
 name="loadLearner"
 path="/learning/AnswersContent.jsp"
 redirect="false"
 />

 <forward
 name="viewAnswers"
 path="/learning/ViewAnswers.jsp"
 redirect="false"
 />

 <forward
 name="redoQuestions"
 path="/learning/RedoQuestions.jsp"
 redirect="false"
 />

 <forward
 name="preview"
 path="/learning/Preview.jsp"
 redirect="false"
 />

 <forward
 name="learningStarter"
 path="/learningIndex.jsp"
 redirect="false"
 />

 <forward
 name="defineLater"
 path="/learning/defineLater.jsp"
 redirect="false"
 />

 <forward
 name="runOffline"
 path="/learning/RunOffline.jsp"
 redirect="false"
 />

 <forward
 name="errorList"
 path="/McErrorBox.jsp"
 redirect="false"
 />
 </action>  
 *
 */

/**
 * 
 * Note: Because of MCQ's learning reporting structure, Show Learner Report is always ON even if in authoring it is set
 * to false.
 */
public class McLearningStarterAction extends Action implements McAppConstants {
    static Logger logger = Logger.getLogger(McLearningStarterAction.class.getName());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, McApplicationException {

	/*
	 * By now, the passed tool session id MUST exist in the db through the calling of: public void
	 * createToolSession(Long toolSessionId, Long toolContentId) by the container.
	 * 
	 * make sure this session exists in tool's session table by now.
	 */

	McUtils.cleanUpSessionAbsolute(request);

	Map mapQuestionsContent = new TreeMap(new McComparator());
	Map mapAnswers = new TreeMap(new McComparator());

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	McLearningForm mcLearningForm = (McLearningForm) form;
	mcLearningForm.setMcService(mcService);
	mcLearningForm.setPassMarkApplicable(new Boolean(false).toString());
	mcLearningForm.setUserOverPassMark(new Boolean(false).toString());

	ActionForward validateParameters = validateParameters(request, mcLearningForm, mapping);
	if (validateParameters != null) {
	    return validateParameters;
	}

	SessionMap sessionMap = new SessionMap();
	List sequentialCheckedCa = new LinkedList();
	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, sequentialCheckedCa);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	mcLearningForm.setHttpSessionID(sessionMap.getSessionID());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(new Long(toolSessionID).toString());

	/*
	 * by now, we made sure that the passed tool session id exists in the db as a new record Make sure we can
	 * retrieve it and the relavent content
	 */

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	if (mcSession == null) {
	    McUtils.cleanUpSessionAbsolute(request);
	    return (mapping.findForward(McAppConstants.ERROR_LIST));
	}

	/*
	 * find out what content this tool session is referring to get the content for this tool session Each passed
	 * tool session id points to a particular content. Many to one mapping.
	 */
	McContent mcContent = mcSession.getMcContent();

	if (mcContent == null) {
	    McUtils.cleanUpSessionAbsolute(request);
	    persistError(request, "error.content.doesNotExist");
	    return (mapping.findForward(McAppConstants.ERROR_LIST));
	}

	/*
	 * The content we retrieved above must have been created before in Authoring time. And the passed tool session
	 * id already refers to it.
	 */

	McLearnerStarterDTO mcLearnerStarterDTO = new McLearnerStarterDTO();
	if (mcContent.isQuestionsSequenced()) {
	    mcLearnerStarterDTO.setQuestionListingMode(McAppConstants.QUESTION_LISTING_MODE_SEQUENTIAL);
	    mcLearningForm.setQuestionListingMode(McAppConstants.QUESTION_LISTING_MODE_SEQUENTIAL);
	} else {
	    mcLearnerStarterDTO.setQuestionListingMode(McAppConstants.QUESTION_LISTING_MODE_COMBINED);
	    mcLearningForm.setQuestionListingMode(McAppConstants.QUESTION_LISTING_MODE_COMBINED);
	}

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
	    mcLearnerStarterDTO.setSubmissionDeadline(submissionDeadline);

	    // calculate whether submission deadline has passed, and if so forward to "runOffline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		request.setAttribute(McAppConstants.MC_LEARNER_STARTER_DTO, mcLearnerStarterDTO);
		return mapping.findForward(McAppConstants.RUN_OFFLINE);
	    }
	}

	/*
	 * Is the tool activity been checked as Run Offline in the property inspector?
	 */
	mcLearnerStarterDTO.setToolActivityOffline(new Boolean(mcContent.isRunOffline()).toString());
	mcLearnerStarterDTO.setActivityTitle(mcContent.getTitle());
	request.setAttribute(McAppConstants.MC_LEARNER_STARTER_DTO, mcLearnerStarterDTO);

	mcLearningForm.setToolContentID(mcContent.getMcContentId().toString());
	commonContentSetup(request, mcContent, mcService, mcLearningForm, toolSessionID);

	/*
	 * find out if the content is set to run offline or online. If it is set to run offline , the learners are
	 * informed about that.
	 */
	boolean isRunOffline = McUtils.isRunOffline(mcContent);
	if (isRunOffline == true) {
	    return (mapping.findForward(McAppConstants.RUN_OFFLINE));
	}

	/* find out if the content is being modified at the moment. */
	boolean isDefineLater = McUtils.isDefineLater(mcContent);
	if (isDefineLater == true) {
	    return (mapping.findForward(McAppConstants.DEFINE_LATER));
	}

	/*
	 * Is the request for a preview by the author? Preview The tool must be able to show the specified content as if
	 * it was running in a lesson. It will be the learner url with tool access mode set to ToolAccessMode.AUTHOR 3
	 * modes are: author teacher learner
	 */

	/* handle PREVIEW mode */
	// String mode=mcLearningForm.getLearningMode();
	String mode = request.getParameter(McAppConstants.MODE);

	/*
	 * by now, we know that the mode is either teacher or learner check if the mode is teacher and request is for
	 * Learner Progress
	 */
	String userId = request.getParameter(McAppConstants.USER_ID);

	if ((userId != null) && (mode.equals("teacher"))) {

	    /* LEARNER_PROGRESS for jsp */
	    mcLearningForm.setLearnerProgress(new Boolean(true).toString());
	    mcLearningForm.setLearnerProgressUserId(userId);

	    McLearningAction mcLearningAction = new McLearningAction();
	    /*
	     * pay attention that this userId is the learner's userId passed by the request parameter. It is differerent
	     * than USER_ID kept in the session of the current system user
	     */

	    McQueUsr mcQueUsr = mcService.getMcUserBySession(new Long(userId), mcSession.getUid());
	    if (mcQueUsr == null) {
		McLearningStarterAction.logger.error("error.learner.required");
		persistError(request, "error.learner.required");
		McLearningStarterAction.logger.error("forwarding to: " + McAppConstants.SIMPLE_LEARNING_ERROR);
		return (mapping.findForward(McAppConstants.SIMPLE_LEARNING_ERROR));
	    }

	    /* check whether the user's session really referrs to the session id passed to the url */
	    Long sessionUid = mcQueUsr.getMcSessionId();
	    McSession mcSessionLocal = mcService.getMcSessionByUID(sessionUid);

	    toolSessionID = mcLearningForm.getToolSessionID();

	    if ((mcSessionLocal == null)
		    || (mcSessionLocal.getMcSessionId().longValue() != new Long(toolSessionID).longValue())) {
		McLearningStarterAction.logger.error("error.learner.sessionId.inconsistent");
	    }
	    LearningUtil.saveFormRequestData(request, mcLearningForm, true);

	    request.setAttribute(McAppConstants.REQUEST_BY_STARTER, new Boolean(true).toString());
	    return mcLearningAction.viewAnswers(mapping, mcLearningForm, request, response);
	}

	/* by now, we know that the mode is learner */
	/*
	 * verify that userId does not already exist in the db. If it does exist, that means, that user already
	 * responded to the content and his answers must be displayed read-only
	 */

	Integer userID = null;
	HttpSession ss = SessionManager.getSession();
	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		userID = user.getUserID();
	    }
	}

	McQueUsr mcQueUsr = mcService.getMcUserBySession(new Long(userID.longValue()), mcSession.getUid());

	request.setAttribute(McAppConstants.MC_LEARNER_STARTER_DTO, mcLearnerStarterDTO);

	/* if the user's session id AND user id exists in the tool tables go to redo questions. */
	if (mcQueUsr != null) {
	    Long sessionUid = mcQueUsr.getMcSessionId();
	    McSession mcUserSession = mcService.getMcSessionByUID(sessionUid);
	    String userSessionId = mcUserSession.getMcSessionId().toString();

	    if (toolSessionID.equals(userSessionId)) {
		McLearningAction mcLearningAction = new McLearningAction();
		request.setAttribute(McAppConstants.REQUEST_BY_STARTER, (Boolean.TRUE).toString());
		mcLearningAction.prepareViewAnswersData(mapping, mcLearningForm, request, getServlet()
			.getServletContext());
		return mapping.findForward(McAppConstants.VIEW_ANSWERS);
	    }
	} else if (mode.equals("teacher")) {
	    McLearningAction mcLearningAction = new McLearningAction();
	    mcLearningForm.setLearnerProgress(new Boolean(true).toString());
	    mcLearningForm.setLearnerProgressUserId(userId);
	    return mcLearningAction.viewAnswers(mapping, mcLearningForm, request, response);
	}
	request.setAttribute(McAppConstants.MC_LEARNER_STARTER_DTO, mcLearnerStarterDTO);
	return (mapping.findForward(McAppConstants.LOAD_LEARNER));
    }

    /**
     * sets up question and candidate answers maps commonContentSetup(HttpServletRequest request, McContent mcContent)
     * 
     * @param request
     * @param mcContent
     */
    protected void commonContentSetup(HttpServletRequest request, McContent mcContent, IMcService mcService,
	    McLearningForm mcLearningForm, String toolSessionID) {
	Map mapQuestionsContent = new TreeMap(new McComparator());

	boolean randomize = mcContent.isRandomize();

	List<McLearnerAnswersDTO> listQuestionAndCandidateAnswersDTO = LearningUtil
		.buildQuestionAndCandidateAnswersDTO(request, mcContent, randomize, mcService);

	request.setAttribute(McAppConstants.LIST_QUESTION_CANDIDATEANSWERS_DTO, listQuestionAndCandidateAnswersDTO);
	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
	mcGeneralLearnerFlowDTO.setTotalCountReached(new Boolean(false).toString());
	mcGeneralLearnerFlowDTO.setQuestionIndex(new Integer(1));

	// should we show the marks for each question - we show the marks if any of the questions
	// have a mark > 1.
	Boolean showMarks = LearningUtil.isShowMarksOnQuestion(listQuestionAndCandidateAnswersDTO);
	mcGeneralLearnerFlowDTO.setShowMarks(showMarks.toString());

	Boolean displayAnswers = mcContent.isDisplayAnswers();
	mcGeneralLearnerFlowDTO.setDisplayAnswers(displayAnswers.toString());
	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());
	String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	String userID = mcLearningForm.getUserID();
	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	}

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
    }

    protected ActionForward validateParameters(HttpServletRequest request, McLearningForm mcLearningForm,
	    ActionMapping mapping) {
	/*
	 * obtain and setup the current user's data
	 */

	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if ((user != null) && (user.getUserID() != null)) {
		userID = user.getUserID().toString();
	    }
	}

	mcLearningForm.setUserID(userID);

	/*
	 * process incoming tool session id and later derive toolContentId from it.
	 */
	String strToolSessionId = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	long toolSessionId = 0;
	if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) {
	    McLearningStarterAction.logger.error("error.toolSessionId.required");
	} else {
	    try {
		toolSessionId = new Long(strToolSessionId).longValue();
	    } catch (NumberFormatException e) {
		McLearningStarterAction.logger.error("error.sessionId.numberFormatException");
	    }
	}

	/* mode can be learner, teacher or author */
	String mode = request.getParameter(McAppConstants.MODE);

	if ((mode == null) || (mode.length() == 0)) {
	    McLearningStarterAction.logger.error("error.mode.required");
	}

	if ((!mode.equals("learner")) && (!mode.equals("teacher")) && (!mode.equals("author"))) {
	    McLearningStarterAction.logger.error("error.mode.invalid");
	}

	return null;
    }

    /**
     * persists error messages to request scope
     * 
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	saveErrors(request, errors);
    }
}
