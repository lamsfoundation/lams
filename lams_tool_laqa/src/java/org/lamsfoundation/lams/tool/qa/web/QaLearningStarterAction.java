/****************************************************************
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
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dto.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.util.QaComparator;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaLearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * This class is used to load the default content and initialize the presentation Map for Learner mode.
 * It is important that ALL the session attributes created in this action gets removed by:
 * QaUtils.cleanupSession(request)
 *
 * @author Ozgur Demirtas
 *
 */
public class QaLearningStarterAction extends Action implements QaAppConstants {
    private static Logger logger = Logger.getLogger(QaLearningStarterAction.class.getName());

    private static IQaService qaService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, QaApplicationException {

	QaUtils.cleanUpSessionAbsolute(request);
	if (qaService == null) {
	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	}

	QaLearningForm qaLearningForm = (QaLearningForm) form;
	/* validate learning mode parameters */
	validateParameters(request, mapping, qaLearningForm);
	String mode = qaLearningForm.getMode();
	String toolSessionID = qaLearningForm.getToolSessionID();

	/*
	 * By now, the passed tool session id MUST exist in the db by calling:
	 * public void createToolSession(Long toolSessionId, Long toolContentId) by the core.
	 * 
	 * make sure this session exists in tool's session table by now.
	 */
	QaSession qaSession = qaService.getSessionById(new Long(toolSessionID).longValue());
	if (qaSession == null) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("No session found");
	}

	QaContent qaContent = qaSession.getQaContent();
	if (qaContent == null) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("No QA content found");
	}

	QaQueUsr user = null;
	if ((mode != null) && mode.equals(ToolAccessMode.TEACHER.toString())) {
	    // monitoring mode - user is specified in URL
	    // assessmentUser may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionID, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(toolSessionID);
	}
	Long userId = user.getQueUsrId();
	qaLearningForm.setUserID(user.getQueUsrId().toString());

	QaQueUsr groupLeader = null;
	if (qaContent.isUseSelectLeaderToolOuput()) {
	    groupLeader = qaService.checkLeaderSelectToolForSessionLeader(user, new Long(toolSessionID).longValue());

	    // forwards to the leaderSelection page
	    if (groupLeader == null && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		List<QaQueUsr> groupUsers = qaService.getUsersBySessionId(new Long(toolSessionID).longValue());
		request.setAttribute(ATTR_GROUP_USERS, groupUsers);
		request.setAttribute(TOOL_SESSION_ID, toolSessionID);
		request.setAttribute(ATTR_CONTENT, qaContent);

		return mapping.findForward(WAIT_FOR_LEADER);
	    }

	    // check if leader has submitted all answers
	    if (groupLeader.isResponseFinalized() && !mode.equals(ToolAccessMode.TEACHER.toString())) {

		// in case user joins the lesson after leader has answers some answers already - we need to make sure
		// he has the same scratches as leader
		qaService.copyAnswersFromLeader(user, groupLeader);

		user.setResponseFinalized(true);
		qaService.updateUser(user);
	    }
	}

	/* holds the question contents for a given tool session and relevant content */
	Map mapQuestionStrings = new TreeMap(new QaComparator());
	Map<Integer, QaQuestionDTO> mapQuestions = new TreeMap<Integer, QaQuestionDTO>();

	String httpSessionID = qaLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);
	if (sessionMap == null) {
	    sessionMap = new SessionMap<String, Object>();
	    Map mapSequentialAnswers = new HashMap();
	    sessionMap.put(MAP_SEQUENTIAL_ANSWERS_KEY, mapSequentialAnswers);
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	    qaLearningForm.setHttpSessionID(sessionMap.getSessionID());

	    sessionMap.put(AttributeNames.ATTR_LEARNER_CONTENT_FOLDER,
		    qaService.getLearnerContentFolder(new Long(toolSessionID), user.getQueUsrId()));
	}
	String sessionMapId = sessionMap.getSessionID();
	sessionMap.put(IS_DISABLED, qaContent.isLockWhenFinished() && user.isLearnerFinished()
		|| (mode != null) && mode.equals(ToolAccessMode.TEACHER.toString()));

	sessionMap.put(ATTR_GROUP_LEADER, groupLeader);
	boolean isUserLeader = qaService.isUserGroupLeader(user, new Long(toolSessionID));
	sessionMap.put(ATTR_IS_USER_LEADER, isUserLeader);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(ATTR_CONTENT, qaContent);
	sessionMap.put(AttributeNames.USER, user);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);
	generalLearnerFlowDTO.setUserUid(user.getQueUsrId().toString());
	generalLearnerFlowDTO.setHttpSessionID(sessionMapId);
	generalLearnerFlowDTO.setToolSessionID(toolSessionID);
	generalLearnerFlowDTO.setToolContentID(qaContent.getQaContentId().toString());
	generalLearnerFlowDTO.setReportTitleLearner(qaContent.getReportTitle());

	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());
	generalLearnerFlowDTO.setReflectionSubject(qaContent.getReflectionSubject());

	NotebookEntry notebookEntry = qaService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		MY_SIGNATURE, userId.intValue());
	if (notebookEntry != null) {
	    //String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
	    qaLearningForm.setEntryText(notebookEntry.getEntry());
	    generalLearnerFlowDTO.setNotebookEntry(notebookEntry.getEntry());
	}

	/*
	 * Is the tool activity been checked as Define Later in the property inspector?
	 */
	if (qaContent.isDefineLater()) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    return (mapping.findForward(DEFINE_LATER));
	}

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(
		new Long(toolSessionID), request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	/*
	 * fetch question content from content
	 */
	Iterator contentIterator = qaContent.getQaQueContents().iterator();
	while (contentIterator.hasNext()) {
	    QaQueContent qaQuestion = (QaQueContent) contentIterator.next();
	    if (qaQuestion != null) {
		int displayOrder = qaQuestion.getDisplayOrder();

		if (displayOrder != 0) {
		    /*
		     * add the question to the questions Map in the displayOrder
		     */
		    QaQuestionDTO questionDTO = new QaQuestionDTO(qaQuestion);
		    mapQuestions.put(displayOrder, questionDTO);

		    mapQuestionStrings.put(new Integer(displayOrder).toString(), qaQuestion.getQuestion());

		}
	    }
	}
	generalLearnerFlowDTO.setMapQuestions(mapQuestionStrings);
	generalLearnerFlowDTO.setMapQuestionContentLearner(mapQuestions);
	generalLearnerFlowDTO.setTotalQuestionCount(new Integer(mapQuestions.size()));
	qaLearningForm.setTotalQuestionCount(new Integer(mapQuestions.size()).toString());

	String feedBackType = "";
	if (qaContent.isQuestionsSequenced()) {
	    feedBackType = FEEDBACK_TYPE_SEQUENTIAL;
	} else {
	    feedBackType = FEEDBACK_TYPE_COMBINED;
	}
	String userFeedback = feedBackType + generalLearnerFlowDTO.getTotalQuestionCount() + QUESTIONS;
	generalLearnerFlowDTO.setUserFeedback(userFeedback);

	generalLearnerFlowDTO.setRemainingQuestionCount(generalLearnerFlowDTO.getTotalQuestionCount().toString());
	generalLearnerFlowDTO.setInitialScreen(new Boolean(true).toString());

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	/*
	 * by now, we know that the mode is either teacher or learner
	 * check if the mode is teacher and request is for Learner Progress
	 */
	if (mode.equals("teacher")) {
	    //start generating learner progress report for toolSessionID

	    /*
	     * the report should have the all entries for the users in this tool session,
	     * and display under the "my answers" section the answers for the user id in the url
	     */
//	    Long learnerProgressUserId = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
	    generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
	    generalLearnerFlowDTO.setTeacherViewOnly(new Boolean(true).toString());

	    QaLearningAction.refreshSummaryData(request, qaContent, qaService, sessionMapId, user,
		    generalLearnerFlowDTO);
	    request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	    return (mapping.findForward(LEARNER_REP));
	}

	//check if there is submission deadline
	Date submissionDeadline = qaContent.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    sessionMap.put(QaAppConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {

		//if ShowOtherAnswersAfterDeadline is enabled - show others answers
		if (qaContent.isShowOtherAnswersAfterDeadline()) {
		    generalLearnerFlowDTO.setLockWhenFinished(Boolean.TRUE.toString());
		    generalLearnerFlowDTO.setNoReeditAllowed(true);
		    //only for ActionForward refreshAllResults(..) method
		    sessionMap.put("noRefresh", true);
		    /*
		     * the report should have all the users' entries OR the report should have only the current
		     * session's entries
		     */
		    generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());

		    QaLearningAction.refreshSummaryData(request, qaContent, qaService, sessionMapId, user,
			    generalLearnerFlowDTO);

		    if (user.isLearnerFinished()) {
			generalLearnerFlowDTO.setRequestLearningReportViewOnly(new Boolean(true).toString());
			return (mapping.findForward(REVISITED_LEARNER_REP));
		    } else {
			generalLearnerFlowDTO.setRequestLearningReportViewOnly(new Boolean(false).toString());
			return (mapping.findForward(LEARNER_REP));
		    }

		    // show submissionDeadline page otherwise
		} else {
		    return mapping.findForward("submissionDeadline");
		}
	    }
	}

	/*
	 * Verify that userId does not already exist in the db.
	 * If it does exist and the passed tool session id exists in the db, that means the user already responded to
	 * the content and
	 * his answers must be displayed read-only
	 *
	 * if the user's tool session id AND user id exists in the tool tables go to learner's report.
	 */
	/*
	 * if the 'All Responses' has been clicked no more user entry is accepted, and isResponseFinalized() returns
	 * true
	 */
	Long currentToolSessionID = new Long(qaLearningForm.getToolSessionID());

	//if Response is Finalized
	if (user.isResponseFinalized()) {
	    QaSession checkSession = user.getQaSession();

	    if (checkSession != null) {
		Long checkQaSessionId = checkSession.getQaSessionId();

		if (checkQaSessionId.toString().equals(currentToolSessionID.toString())) {

		    // the learner is in the same session and has already responsed to this content

		    generalLearnerFlowDTO.setLockWhenFinished(new Boolean(qaContent.isLockWhenFinished()).toString());
		    generalLearnerFlowDTO.setNoReeditAllowed(qaContent.isNoReeditAllowed());
		    /*
		     * the report should have all the users' entries OR the report should have only the current
		     * session's entries
		     */
		    generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());

		    QaLearningAction.refreshSummaryData(request, qaContent, qaService, sessionMapId, user,
			    generalLearnerFlowDTO);

		    if (user.isLearnerFinished()) {
			generalLearnerFlowDTO.setRequestLearningReportViewOnly(new Boolean(true).toString());
			return (mapping.findForward(REVISITED_LEARNER_REP));
		    } else {
			generalLearnerFlowDTO.setRequestLearningReportViewOnly(new Boolean(false).toString());
			return (mapping.findForward(LEARNER_REP));
		    }
		}
	    }
	}

	//**---- showing AnswersContent.jsp ----**
	LearningUtil.populateAnswers(sessionMap, qaContent, user, mapQuestions, generalLearnerFlowDTO, qaService);

	return (mapping.findForward(LOAD_LEARNER));
    }

    /**
     * validates the learning mode parameters
     *
     * @param request
     * @param mapping
     * @return ActionForward
     */
    protected void validateParameters(HttpServletRequest request, ActionMapping mapping,
	    QaLearningForm qaLearningForm) {
	/*
	 * process incoming tool session id and later derive toolContentId from it.
	 */
	String strToolSessionId = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	long toolSessionId = 0;
	if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) {
	    ActionMessages errors = new ActionMessages();
	    errors.add(Globals.ERROR_KEY, new ActionMessage("error.toolSessionId.required"));
	    logger.error("error.toolSessionId.required");
	    saveErrors(request, errors);
	    return;
	} else {
	    try {
		toolSessionId = new Long(strToolSessionId).longValue();
		qaLearningForm.setToolSessionID(new Long(toolSessionId).toString());
	    } catch (NumberFormatException e) {
		logger.error("add error.sessionId.numberFormatException to ActionMessages.");
		return;
	    }
	}

	/* mode can be learner, teacher or author */
	String mode = request.getParameter(MODE);
	if ((mode == null) || (mode.length() == 0)) {
	    logger.error("Mode is empty");
	    return;
	}
	if ((!mode.equals("learner")) && (!mode.equals("teacher")) && (!mode.equals("author"))) {
	    logger.error("Wrong mode");
	    return;
	}
	qaLearningForm.setMode(mode);
    }

    private QaQueUsr getCurrentUser(String toolSessionId) {
	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userId = toolUser.getUserID();

	QaQueUsr qaUser = qaService.getUserByIdAndSession(userId.longValue(), new Long(toolSessionId));
	if (qaUser == null) {
	    qaUser = qaService.createUser(new Long(toolSessionId), userId);
	}

	return qaUser;
    }

    private QaQueUsr getSpecifiedUser(String toolSessionId, Integer userId) {
	QaQueUsr qaUser = qaService.getUserByIdAndSession(userId.longValue(), new Long(toolSessionId));
	if (qaUser == null) {
	    qaUser = qaService.createUser(new Long(toolSessionId), userId);
	}
	return qaUser;
    }
}