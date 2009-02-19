/***************************************************************************
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
USA

http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

/**
 * @author Ozgur Demirtas
 * 
 <action
 path="/learning"
 type="org.lamsfoundation.lams.tool.qa.web.QaLearningAction"
 name="QaLearningForm"
 scope="request"
 input="/learning/AnswersContent.jsp"
 parameter="method"
 unknown="false"
 validate="false">

 <forward
 name="loadLearner"
 path="/learning/AnswersContent.jsp"	        
 redirect="false"
 />

 <forward
 name="loadMonitoring"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward
 name="refreshMonitoring"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward
 name="learningStarter"
 path="/learningIndex.jsp"
 redirect="false"
 />

 <forward
 name="individualLearnerRep"
 path="/learning/LearnerRep.jsp"
 redirect="false"
 />


 <forward
 name="learnerRep"
 path="/monitoring/LearnerRep.jsp"
 redirect="false"
 />

 <forward
 name="individualLearnerResults"
 path="/learning/IndividualLearnerResults.jsp"
 redirect="false"
 />


 <forward
 name="viewAllResults"
 path="/learning/AllResults.jsp"
 redirect="false"
 />

 <forward
 name="notebook"
 path="/learning/Notebook.jsp"
 redirect="false"
 />   

 </action>

 * 
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

public class QaLearningAction extends LamsDispatchAction implements QaAppConstants {
    static Logger logger = Logger.getLogger(QaLearningAction.class.getName());

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	logger.debug("dispatching unspecified...");
	return null;
    }

    /**
     * submits users responses ActionForward submitAnswersContent(ActionMapping
     * mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response) throws IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitAnswersContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	logger.debug("dispatching submitAnswersContent...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	String toolContentID = qaSession.getQaContent().getQaContentId().toString();

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	String totalQuestionCount = generalLearnerFlowDTO.getTotalQuestionCount().toString();
	int intTotalQuestionCount = new Integer(totalQuestionCount).intValue();

	String questionListingMode = generalLearnerFlowDTO.getQuestionListingMode();

	Map mapAnswers = new TreeMap(new QaComparator());
	Map mapAnswersPresentable = new TreeMap(new QaComparator());
	Map mapFeedback = new TreeMap(new QaComparator());
	/* if the listing mode is QUESTION_LISTING_MODE_COMBINED populate  the answers here*/
	if (questionListingMode.equalsIgnoreCase(QUESTION_LISTING_MODE_COMBINED)) {
	    logger.debug("the listing mode is combined.");
	    for (int questionIndex = INITIAL_QUESTION_COUNT.intValue(); questionIndex <= intTotalQuestionCount; questionIndex++) {
		String answer = request.getParameter("answer" + questionIndex);
		String answerPresentable = QaUtils.replaceNewLines(answer);

		mapAnswers.put(new Long(questionIndex).toString(), answer);
		mapAnswersPresentable.put(new Long(questionIndex).toString(), answerPresentable);
	    }
	} else {
	    logger.debug("the listing mode is sequential");
	    if (totalQuestionCount.equals("1")) {
		String answerPresentable = QaUtils.replaceNewLines(qaLearningForm.getAnswer());

		mapAnswers.put(new Long(1).toString(), qaLearningForm.getAnswer());
		mapAnswersPresentable.put(new Long(1).toString(), answerPresentable);
	    } else {
		logger.debug("the listing mode is sequential and there are multiple questions");
		logger.debug("populating mapAnswers...");
		mapAnswers = populateAnswersMap(qaLearningForm, request, generalLearnerFlowDTO, true, true);

		String httpSessionID = qaLearningForm.getHttpSessionID();

		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

		mapAnswersPresentable = (Map) sessionMap.get(MAP_ALL_RESULTS_KEY);
		mapAnswersPresentable = MonitoringUtil.removeNewLinesMap(mapAnswersPresentable);
	    }

	}

	generalLearnerFlowDTO.setMapAnswers(mapAnswers);

	generalLearnerFlowDTO.setMapAnswersPresentable(mapAnswersPresentable);

	/*mapAnswers will be used in the viewAllAnswers screen*/
	SessionMap sessionMap = new SessionMap();
	sessionMap.put(MAP_ALL_RESULTS_KEY, mapAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	boolean showOtherAnswers = qaContent.isShowOtherAnswers();
	generalLearnerFlowDTO.setShowOtherAnswers(new Boolean(showOtherAnswers).toString());

	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	qaLearningForm.resetAll();
	logger.debug("fwd'ing to." + INDIVIDUAL_LEARNER_RESULTS);
	return (mapping.findForward(INDIVIDUAL_LEARNER_RESULTS));
    }

    /**
     * enables retaking the activity
     * 
     * ActionForward redoQuestions(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward redoQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	logger.debug("dispatching redoQuestions...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	String toolContentID = qaSession.getQaContent().getQaContentId().toString();

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	Map mapQuestions = new TreeMap(new QaComparator());
	Map mapAnswers = new TreeMap(new QaComparator());

	generalLearnerFlowDTO.setCurrentQuestionIndex(new Integer(1));
	qaLearningForm.setCurrentQuestionIndex(new Integer(1).toString());

	String httpSessionID = qaLearningForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	generalLearnerFlowDTO.setActivityTitle(qaContent.getTitle());
	generalLearnerFlowDTO.setActivityInstructions(qaContent.getInstructions());

	logger.debug("using TOOL_CONTENT_ID: " + qaContent.getQaContentId());
	generalLearnerFlowDTO.setToolContentID(qaContent.getQaContentId().toString());

	/*
	 * The content we retrieved above must have been created before in Authoring time. 
	 * And the passed tool session id refers to it.
	 */

	generalLearnerFlowDTO.setReportTitleLearner(qaContent.getReportTitle());

	/*
	 * Is the tool activity been checked as Run Offline in the property inspector?
	 */
	generalLearnerFlowDTO.setActivityOffline(new Boolean(qaContent.isRunOffline()).toString());

	generalLearnerFlowDTO.setUserNameVisible(new Boolean(qaContent.isUsernameVisible()).toString());
	generalLearnerFlowDTO.setShowOtherAnswers(new Boolean(qaContent.isShowOtherAnswers()).toString());

	/*
	 * Learning mode requires this setting for jsp to generate the user's report 
	 */
	String feedBackType = "";
	if (qaContent.isQuestionsSequenced()) {
	    generalLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_SEQUENTIAL);
	    feedBackType = FEEDBACK_TYPE_SEQUENTIAL;
	} else {
	    generalLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_COMBINED);
	    feedBackType = FEEDBACK_TYPE_COMBINED;
	}

	HttpSession ss = SessionManager.getSession();
	/* get back login user DTO */
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());

	String userName = toolUser.getLogin();
	String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	Long userId = new Long(toolUser.getUserID().longValue());
	logger.debug("userId: " + userId);

	QaQueUsr qaQueUsr = qaService.getQaUserBySession(userId, qaSession.getUid());

	Long qaQueUsrUid = null;
	boolean isResponseFinalized = false;
	if (qaQueUsr != null) {
	    qaQueUsrUid = qaQueUsr.getUid();
	    logger.debug("qaQueUsrUid: " + qaQueUsrUid);

	    isResponseFinalized = qaQueUsr.isResponseFinalized();
	}

	Map mapAnswersFromDb = new TreeMap(new QaComparator());
	/*
	 * fetch question content from content
	 */
	Iterator contentIterator = qaContent.getQaQueContents().iterator();

	int questionCount = 0;
	while (contentIterator.hasNext()) {
	    QaQueContent qaQueContent = (QaQueContent) contentIterator.next();
	    if (qaQueContent != null) {
		Long questionUid = qaQueContent.getUid();


		int displayOrder = qaQueContent.getDisplayOrder();
		if (displayOrder != 0) {
		    ++questionCount;

		    mapQuestions.put(new Integer(displayOrder).toString(), qaQueContent.getQuestion());

		    if (qaQueUsr != null) {
			List listUserAttempts = qaService
				.getAttemptsForUserAndQuestionContent(qaQueUsrUid, questionUid);

			if (listUserAttempts.size() > 1) {
			    logger.debug("Warning: There should not be more than 1 attempts for the question.");
			}

			Iterator itAttempts = listUserAttempts.iterator();
			while (itAttempts.hasNext()) {
			    QaUsrResp qaUsrResp = (QaUsrResp) itAttempts.next();
			    mapAnswersFromDb.put(new Integer(questionCount).toString(), qaUsrResp.getAnswer());
			}
		    }
		}
	    }
	}

	mapAnswers = (Map) sessionMap.get(MAP_ALL_RESULTS_KEY);

	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	generalLearnerFlowDTO.setMapQuestionContentLearner(mapQuestions);
	generalLearnerFlowDTO.setMapQuestions(mapQuestions);

	if (mapAnswers == null) {
	    mapAnswers = new TreeMap(new QaComparator());
	    // maybe we have come in from the review screen, if so get the answers from db.
	    if (mapAnswersFromDb.size() > 0) {
		Iterator itMapAnswers = mapAnswersFromDb.entrySet().iterator();
		while (itMapAnswers.hasNext()) {
		    Map.Entry pairs = (Map.Entry) itMapAnswers.next();
		    mapAnswers.put(pairs.getKey().toString(), pairs.getValue());
		}
		mapAnswers.putAll(mapAnswersFromDb);
	    } else {
		Iterator itMapQuestions = mapQuestions.entrySet().iterator();
		while (itMapQuestions.hasNext()) {
		    Map.Entry pairs = (Map.Entry) itMapQuestions.next();
		    mapAnswers.put(pairs.getKey(), "");

		}
	    }
	}

	String currentAnswer = (String) mapAnswers.get("1");
	generalLearnerFlowDTO.setCurrentAnswer(currentAnswer);

	sessionMap.put(MAP_SEQUENTIAL_ANSWERS_KEY, mapAnswers);

	generalLearnerFlowDTO.setMapAnswers(mapAnswers);
	sessionMap.put(MAP_ALL_RESULTS_KEY, mapAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	generalLearnerFlowDTO.setTotalQuestionCount(new Integer(mapQuestions.size()));
	qaLearningForm.setTotalQuestionCount(new Integer(mapQuestions.size()).toString());
	generalLearnerFlowDTO.setRemainingQuestionCount(new Integer(mapQuestions.size()).toString());

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	qaLearningForm.resetAll();
	logger.debug("forwarding to: " + LOAD_LEARNER);
	return (mapping.findForward(LOAD_LEARNER));
    }

    /**
     * Stores all results and moves onto the next step. If view other users
     * answers = true, then goes to the view all answers screen, otherwise goes
     * straight to the reflection screen (if any).
     * 
     * returns Learner Report for a session ActionForward
     * viewAllResults(ActionMapping mapping, ActionForm form, HttpServletRequest
     * request, HttpServletResponse response) throws IOException,
     * ServletException)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward storeAllResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	logger.debug("dispatching viewAllResults...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	String toolContentID = qaSession.getQaContent().getQaContentId().toString();

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	String isUserNamesVisibleBoolean = generalLearnerFlowDTO.getUserNameVisible();
	boolean isUserNamesVisible = new Boolean(isUserNamesVisibleBoolean).booleanValue();

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);
	generalLearnerFlowDTO.setHttpSessionID(httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	Map mapAnswers = (Map) sessionMap.get(MAP_ALL_RESULTS_KEY);

	/*recreate the users and responses*/
	LearningUtil learningUtil = new LearningUtil();
	learningUtil.createUsersAndResponses(mapAnswers, request, qaService, new Long(toolContentID), new Long(
		toolSessionID));
	qaLearningForm.resetUserActions();
	qaLearningForm.setSubmitAnswersContent(null);

	learningUtil.setContentInUse(new Long(toolContentID).longValue(), qaService);
	logger.debug("content has been set in use");

	if (qaContent.isShowOtherAnswers()) {
	    return prepareViewAllAnswers(mapping, request, qaLearningForm, qaService, toolSessionID, userID, qaSession,
		    toolContentID, qaContent, generalLearnerFlowDTO, isUserNamesVisible);
	} else if (qaContent.isReflect()) {
	    return forwardtoReflection(mapping, request, qaContent, qaService, toolSessionID, userID, qaLearningForm);
	} else {
	    return endLearning(mapping, qaLearningForm, request, response);
	}
    }

    /** Set up the data for the view all answers screen */
    private ActionForward prepareViewAllAnswers(ActionMapping mapping, HttpServletRequest request,
	    QaLearningForm qaLearningForm, IQaService qaService, String toolSessionID, String userID,
	    QaSession qaSession, String toolContentID, QaContent qaContent,
	    GeneralLearnerFlowDTO generalLearnerFlowDTO, boolean isUserNamesVisible) {
	logger.debug("start generating learning report...");
	logger.debug("toolContentID: " + toolContentID);

	QaMonitoringAction qaMonitoringAction = new QaMonitoringAction();
	qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, isUserNamesVisible, true, toolSessionID,
		userID, generalLearnerFlowDTO, false, toolSessionID);

	generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(false).toString());

	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());

	qaLearningForm.resetAll();

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	HttpSession ss = SessionManager.getSession();
	/* get back login user DTO */
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());

	String userName = toolUser.getLogin();
	String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	Long userId = new Long(toolUser.getUserID().longValue());
	logger.debug("userId: " + userId);

	QaQueUsr qaQueUsr = qaService.getQaUserBySession(userId, qaSession.getUid());

	generalLearnerFlowDTO.setUserUid(qaQueUsr.getUid().toString());

	int sessionUserCount = 0;
	if (qaSession.getQaQueUsers() != null) {
	    sessionUserCount = qaSession.getQaQueUsers().size();
	}

	if (sessionUserCount > 1) {
	    logger.debug("there are multiple user responses");
	    generalLearnerFlowDTO.setExistMultipleUserResponses(new Boolean(true).toString());
	}

	boolean usernameVisible = qaContent.isUsernameVisible();
	generalLearnerFlowDTO.setUserNameVisible(new Boolean(usernameVisible).toString());

	NotebookEntry notebookEntry = qaService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    //String notebookEntryPresentable=QaUtils.replaceNewLines(notebookEntry.getEntry());
	    String notebookEntryPresentable = notebookEntry.getEntry();
	    qaLearningForm.setEntryText(notebookEntryPresentable);
	}

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	logger.debug("fwd'ing to INDIVIDUAL_LEARNER_REPORT: " + INDIVIDUAL_LEARNER_REPORT);
	return (mapping.findForward(INDIVIDUAL_LEARNER_REPORT));
    }

    /**
     * ActionForward refreshAllResults(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward refreshAllResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	logger.debug("dispatching refreshAllResults...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	String toolContentID = qaSession.getQaContent().getQaContentId().toString();

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	String isUserNamesVisibleBoolean = generalLearnerFlowDTO.getUserNameVisible();
	boolean isUserNamesVisible = new Boolean(isUserNamesVisibleBoolean).booleanValue();

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);
	generalLearnerFlowDTO.setHttpSessionID(httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	Map mapAnswers = (Map) sessionMap.get(MAP_ALL_RESULTS_KEY);

	/*recreate the users and responses*/
	LearningUtil learningUtil = new LearningUtil();

	qaLearningForm.resetUserActions();
	qaLearningForm.setSubmitAnswersContent(null);

	learningUtil.setContentInUse(new Long(toolContentID).longValue(), qaService);

	logger.debug("start generating learning report...");
	logger.debug("toolContentID: " + toolContentID);

	QaMonitoringAction qaMonitoringAction = new QaMonitoringAction();
	qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, isUserNamesVisible, true, toolSessionID,
		userID, generalLearnerFlowDTO, false, toolSessionID);

	generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(false).toString());

	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());
	//generalLearnerFlowDTO.setNotebookEntriesVisible(new Boolean(false).toString());

	qaLearningForm.resetAll();

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	HttpSession ss = SessionManager.getSession();
	/* get back login user DTO */
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());

	String userName = toolUser.getLogin();
	String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	Long userId = new Long(toolUser.getUserID().longValue());
	logger.debug("userId: " + userId);

	QaQueUsr qaQueUsr = qaService.getQaUserBySession(userId, qaSession.getUid());
	logger.debug("qaQueUsr: " + qaQueUsr);
	logger.debug("qaQueUsr uid : " + qaQueUsr.getUid());

	generalLearnerFlowDTO.setUserUid(qaQueUsr.getUid().toString());

	int sessionUserCount = 0;
	if (qaSession.getQaQueUsers() != null) {
	    sessionUserCount = qaSession.getQaQueUsers().size();
	}

	if (sessionUserCount > 1) {
	    logger.debug("there are multiple user responses");
	    generalLearnerFlowDTO.setExistMultipleUserResponses(new Boolean(true).toString());
	}

	boolean usernameVisible = qaContent.isUsernameVisible();
	generalLearnerFlowDTO.setUserNameVisible(new Boolean(usernameVisible).toString());

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	logger.debug("fwd'ing to INDIVIDUAL_LEARNER_REPORT: " + INDIVIDUAL_LEARNER_REPORT);
	return (mapping.findForward(INDIVIDUAL_LEARNER_REPORT));
    }

    /**
     * moves to the next question and modifies the map ActionForward
     * getNextQuestion(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException, ToolException
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
    public ActionForward getNextQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	logger.debug("dispatching getNextQuestion...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	String toolContentID = qaSession.getQaContent().getQaContentId().toString();

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	populateAnswersMap(qaLearningForm, request, generalLearnerFlowDTO, true, false);

	qaLearningForm.resetAll();
	return (mapping.findForward(LOAD_LEARNER));
    }

    public Map populateAnswersMap(ActionForm form, HttpServletRequest request,
	    GeneralLearnerFlowDTO generalLearnerFlowDTO, boolean getNextQuestion, boolean learnerDone) {
	logger.debug("learnerDone: " + learnerDone);
	logger.debug("getNextQuestion: " + getNextQuestion);
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	String httpSessionID = qaLearningForm.getHttpSessionID();
	logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	Map mapSequentialAnswers = (Map) sessionMap.get(MAP_SEQUENTIAL_ANSWERS_KEY);

	String currentQuestionIndex = qaLearningForm.getCurrentQuestionIndex();

	Map mapAnswers = (Map) sessionMap.get(MAP_ALL_RESULTS_KEY);

	if (mapAnswers == null)
	    mapAnswers = new TreeMap(new QaComparator());

	logger.debug("getting answer for question: " + currentQuestionIndex + "as: " + qaLearningForm.getAnswer());
	logger.debug("mapSequentialAnswers size:" + mapSequentialAnswers.size());

	if (mapSequentialAnswers.size() >= new Integer(currentQuestionIndex).intValue()) {
	    logger.debug("mapSequentialAnswers size:" + mapSequentialAnswers.size() + " and currentQuestionIndex: "
		    + currentQuestionIndex);
	    mapSequentialAnswers.remove(new Long(currentQuestionIndex).toString());
	}
	mapSequentialAnswers.put(new Long(currentQuestionIndex).toString(), qaLearningForm.getAnswer());

	mapAnswers.put(currentQuestionIndex, qaLearningForm.getAnswer());

	sessionMap.put(MAP_ALL_RESULTS_KEY, mapAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());
	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	int intCurrentQuestionIndex = new Integer(currentQuestionIndex).intValue();
	logger.debug("intCurrentQuestionIndex:" + intCurrentQuestionIndex);

	if (getNextQuestion)
	    intCurrentQuestionIndex++;
	else
	    intCurrentQuestionIndex--;

	LearningUtil learningUtil = new LearningUtil();
	logger.debug("current map size:" + mapSequentialAnswers.size());

	String currentAnswer = "";
	if (mapAnswers.size() >= intCurrentQuestionIndex) {
	    currentAnswer = (String) mapAnswers.get(new Long(intCurrentQuestionIndex).toString());
	}
	generalLearnerFlowDTO.setCurrentAnswer(currentAnswer);

	logger.debug("currentQuestionIndex will be: " + intCurrentQuestionIndex);
	generalLearnerFlowDTO.setCurrentQuestionIndex(new Integer(intCurrentQuestionIndex));

	String totalQuestionCount = qaLearningForm.getTotalQuestionCount();
	logger.debug("totalQuestionCount: " + totalQuestionCount);

	String userFeedback = learningUtil
		.feedBackAnswersProgress(request, intCurrentQuestionIndex, totalQuestionCount);
	generalLearnerFlowDTO.setUserFeedback(userFeedback);

	String remQCount = learningUtil.getRemainingQuestionCount(intCurrentQuestionIndex, totalQuestionCount);
	logger.debug("remQCount: " + remQCount);
	generalLearnerFlowDTO.setRemainingQuestionCount(remQCount);

	qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */

	sessionMap.put(MAP_SEQUENTIAL_ANSWERS_KEY, mapSequentialAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	return mapSequentialAnswers;
    }

    /**
     * moves to the previous question and modifies the map ActionForward
     * getPreviousQuestion(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException, ToolException
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
    public ActionForward getPreviousQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	logger.debug("dispatching getPreviousQuestion...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	String toolContentID = qaSession.getQaContent().getQaContentId().toString();

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	populateAnswersMap(qaLearningForm, request, generalLearnerFlowDTO, false, false);

	qaLearningForm.resetAll();
	return (mapping.findForward(LOAD_LEARNER));
    }

    /**
     * finishes the user's tool activity endLearning(HttpServletRequest request,
     * IQaService qaService, HttpServletResponse response) throws IOException,
     * ToolException
     * 
     * @param request
     * @param qaService
     * @param response
     * @throws IOException
     * @throws ToolException
     */
    public ActionForward endLearning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	logger.debug("dispatching endLearning...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	logger.debug("userID: " + userID);
	qaLearningForm.setUserID(userID);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	QaQueUsr qaQueUsr = qaService.getQaUserBySession(new Long(userID), qaSession.getUid());

	/* it is possible that qaQueUsr can be null if the content is set as runoffline and reflection is on*/
	if (qaQueUsr == null) {
	    logger.debug("attempt creating  user record since it must exist for the runOffline + reflection screens");
	    HttpSession ss = SessionManager.getSession();

	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    logger.debug("retrieving toolUser userId: " + toolUser.getUserID());

	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	    Long userId = new Long(toolUser.getUserID().longValue());

	    qaQueUsr = new QaQueUsr(userId, userName, fullName, null, qaSession, new TreeSet());


	    logger.debug("session uid: " + qaSession.getUid());
	    qaService.createQaQueUsr(qaQueUsr);
	}

	qaQueUsr.setLearnerFinished(true);
	logger.debug("learner is finished:" + qaQueUsr.getUid());
	qaService.updateQaQueUsr(qaQueUsr);

	/*
	 * The learner is done with the tool session. The tool needs to clean-up.
	 */
	HttpSession ss = SessionManager.getSession();
	/*get back login user DTO*/
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	logger.debug("set status to COMPLETE");

	qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
	qaSession.setSession_status(COMPLETED);
	qaService.updateQaSession(qaSession);

	String httpSessionID = qaLearningForm.getHttpSessionID();
	logger.debug("removing map with httpSessionID: " + httpSessionID);
	//request.getSession().removeAttribute(httpSessionID);
	qaLearningForm.setHttpSessionID(httpSessionID);

	qaLearningForm.resetAll();

	String nextActivityUrl = qaService.leaveToolSession(new Long(toolSessionID), new Long(user.getUserID()
		.longValue()));
	response.sendRedirect(nextActivityUrl);

	return null;
    }

    public ActionForward updateReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	logger.debug("dispatching updateReflection...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	qaLearningForm.setUserID(userID);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	QaContent qaContent = qaSession.getQaContent();

	QaQueUsr qaQueUsr = qaService.getQaUserBySession(new Long(userID), qaSession.getUid());

	String entryText = request.getParameter("entryText");
	qaLearningForm.setEntryText(entryText);

	NotebookEntry notebookEntryLocal = new NotebookEntry();
	notebookEntryLocal.setEntry(entryText);
	//notebookEntry.setUser(qaQueUsr);
	User user = new User();
	user.setUserId(new Integer(userID));
	notebookEntryLocal.setUser(user);

	qaService.updateEntry(notebookEntryLocal);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = new GeneralLearnerFlowDTO();

	generalLearnerFlowDTO.setNotebookEntry(entryText);
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
	Boolean isUserNamesVisibleBoolean = new Boolean(qaContent.isUsernameVisible());
	boolean isUserNamesVisible = isUserNamesVisibleBoolean.booleanValue();

	QaMonitoringAction qaMonitoringAction = new QaMonitoringAction();
	qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, isUserNamesVisible, true, toolSessionID,
		userID, generalLearnerFlowDTO, false, toolSessionID);

	int sessionUserCount = 0;
	if (qaSession.getQaQueUsers() != null) {
	    sessionUserCount = qaSession.getQaQueUsers().size();
	}

	if (sessionUserCount > 1) {
	    logger.debug("there are multiple user responses");
	    generalLearnerFlowDTO.setExistMultipleUserResponses(new Boolean(true).toString());
	}

	boolean isLearnerFinished = qaQueUsr.isLearnerFinished();
	generalLearnerFlowDTO.setRequestLearningReportViewOnly(new Boolean(isLearnerFinished).toString());

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	NotebookEntry notebookEntry = qaService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		MY_SIGNATURE, new Integer(userID));


	if (notebookEntry != null) {
	    String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
	    qaLearningForm.setEntryText(notebookEntryPresentable);
	}

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	logger.debug("fwd'ing to." + REVISITED_LEARNER_REP);
	return (mapping.findForward(REVISITED_LEARNER_REP));
    }

    /**
     * ActionForward submitReflection(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException, ToolException
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
    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	logger.debug("dispatching submitReflection...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	logger.debug("userID: " + userID);
	qaLearningForm.setUserID(userID);

	String reflectionEntry = request.getParameter(ENTRY_TEXT);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	QaQueUsr qaQueUsr = qaService.getQaUserBySession(new Long(userID), qaSession.getUid());

	/* it is possible that qaQueUsr can be null if the content is set as runoffline and reflection is on*/
	if (qaQueUsr == null) {
	    logger.debug("attempt creating  user record since it must exist for the runOffline + reflection screens");
	    HttpSession ss = SessionManager.getSession();

	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    logger.debug("retrieving toolUser userId: " + toolUser.getUserID());

	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	    Long userId = new Long(toolUser.getUserID().longValue());

	    qaQueUsr = new QaQueUsr(userId, userName, fullName, null, qaSession, new TreeSet());

	    qaService.createQaQueUsr(qaQueUsr);
	}

	qaService.createNotebookEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE,
		new Integer(userID), reflectionEntry);

	qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */
	return endLearning(mapping, form, request, response);
    }

    /**
     * ActionForward forwardtoReflection(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException, ToolException
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
    public ActionForward forwardtoReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	logger.debug("dispatching forwardtoReflection...");
	QaLearningForm qaLearningForm = (QaLearningForm) form;
	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());

	QaContent qaContent = qaSession.getQaContent();

	String userID = request.getParameter("userID");
	logger.debug("userID: " + userID);
	qaLearningForm.setUserID(userID);

	return forwardtoReflection(mapping, request, qaContent, qaService, toolSessionID, userID, qaLearningForm);
    }

    private ActionForward forwardtoReflection(ActionMapping mapping, HttpServletRequest request, QaContent qaContent,
	    IQaService qaService, String toolSessionID, String userID, QaLearningForm qaLearningForm) {

	GeneralLearnerFlowDTO generalLearnerFlowDTO = new GeneralLearnerFlowDTO();
	generalLearnerFlowDTO.setActivityTitle(qaContent.getTitle());
	String reflectionSubject = qaContent.getReflectionSubject();
	reflectionSubject = QaUtils.replaceNewLines(reflectionSubject);
	generalLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	logger.debug("attempt getting notebookEntry: ");
	NotebookEntry notebookEntry = qaService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    //String notebookEntryPresentable=QaUtils.replaceNewLines(notebookEntry.getEntry());
	    String notebookEntryPresentable = notebookEntry.getEntry();
	    generalLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	    qaLearningForm.setEntryText(notebookEntryPresentable);
	}

	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
	qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */

	qaLearningForm.resetAll();
	logger.debug("fwd'ing to: " + NOTEBOOK);
	return (mapping.findForward(NOTEBOOK));
    }
}