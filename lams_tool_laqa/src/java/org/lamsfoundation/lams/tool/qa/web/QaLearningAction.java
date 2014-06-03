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


/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.qa.dto.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaComparator;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaLearningForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Ozgur Demirtas
 */
public class QaLearningAction extends LamsDispatchAction implements QaAppConstants {
    private static Logger logger = Logger.getLogger(QaLearningAction.class.getName());

    private static IQaService qaService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaLearningAction.logger.warn("dispatching unspecified...");
	return null;
    }

    /**
     * submits users responses
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
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	String totalQuestionCount = generalLearnerFlowDTO.getTotalQuestionCount().toString();
	int intTotalQuestionCount = new Integer(totalQuestionCount).intValue();

	String questionListingMode = generalLearnerFlowDTO.getQuestionListingMode();

	Map<String, String> mapAnswers = new TreeMap<String, String>(new QaComparator());
	Map<String, String> mapAnswersPresentable = new TreeMap<String, String>(new QaComparator());

	String forwardName = QaAppConstants.INDIVIDUAL_LEARNER_RESULTS;
	ActionMessages errors = new ActionMessages();

	String httpSessionID = qaLearningForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	/* if the listing mode is QUESTION_LISTING_MODE_COMBINED populate  the answers here*/
	if (questionListingMode.equalsIgnoreCase(QaAppConstants.QUESTION_LISTING_MODE_COMBINED)) {

	    for (int questionIndex = QaAppConstants.INITIAL_QUESTION_COUNT.intValue(); questionIndex <= intTotalQuestionCount; questionIndex++) {
		// TestHarness can not send "answerX" fields, so stick to the original, unfiltered field
		boolean isTestHarness = Boolean.valueOf(request.getParameter("testHarness"));
		String answerParamName = "answer" + questionIndex + (isTestHarness ? "__textarea" : "");
		String answer = request.getParameter(answerParamName);
		// String answerPresentable = QaUtils.replaceNewLines(answer);

		String questionIndexString = new Integer(questionIndex).toString();
		mapAnswers.put(questionIndexString, answer);
		mapAnswersPresentable.put(questionIndexString, answer);

		Map<Integer, QaQuestionDTO> questionContentMap = generalLearnerFlowDTO.getMapQuestionContentLearner();
		QaQuestionDTO dto = questionContentMap.get(questionIndexString);
		if (dto.isRequired() && isEmpty(answer)) {
		    errors.add(Globals.ERROR_KEY, new ActionMessage("error.required", questionIndexString));
		    forwardName = QaAppConstants.LOAD_LEARNER;
		}

		// store
		QaLearningAction.qaService.updateResponseWithNewAnswer(answer, toolSessionID, new Long(questionIndex));
	    }
	    saveErrors(request, errors);

	} else {
	    mapAnswers = storeSequentialAnswer(qaLearningForm, request, generalLearnerFlowDTO, true);

	    mapAnswersPresentable = (Map) sessionMap.get(QaAppConstants.MAP_ALL_RESULTS_KEY);
	    mapAnswersPresentable = MonitoringUtil.removeNewLinesMap(mapAnswersPresentable);

	    // only need to check the final question as the others will have been checked when the user clicked next.
	    Map<Integer, QaQuestionDTO> questionMap = generalLearnerFlowDTO.getMapQuestionContentLearner();
	    int numQuestions = questionMap.size();
	    String finalQuestionIndex = new Integer(numQuestions).toString();
	    QaQuestionDTO dto = questionMap.get(finalQuestionIndex);
	    if (dto.isRequired() && isEmpty(mapAnswersPresentable.get(finalQuestionIndex))) {
		errors.add(Globals.ERROR_KEY, new ActionMessage("error.required", finalQuestionIndex));
		forwardName = QaAppConstants.LOAD_LEARNER;
	    }
	}

	generalLearnerFlowDTO.setMapAnswers(mapAnswers);
	generalLearnerFlowDTO.setMapAnswersPresentable(mapAnswersPresentable);

	/*mapAnswers will be used in the viewAllAnswers screen*/
	if (sessionMap == null) {
	    sessionMap = new SessionMap();
	}

	sessionMap.put(QaAppConstants.MAP_ALL_RESULTS_KEY, mapAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	qaLearningForm.resetAll();
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());
	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	// notify teachers on response submit
	if (errors.isEmpty() && qaContent.isNotifyTeachersOnResponseSubmit()) {
	    qaService.notifyTeachersOnResponseSubmit(new Long(toolSessionID));
	}
	
	return (mapping.findForward(forwardName));
    }

    public ActionForward checkLeaderProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	QaSession session = qaService.getSessionById(toolSessionId);
	QaQueUsr leader = session.getGroupLeader();
	
	boolean isLeaderResponseFinalized = leader.isResponseFinalized();
	
	JSONObject JSONObject = new JSONObject();
	JSONObject.put("isLeaderResponseFinalized", isLeaderResponseFinalized);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }
    
    /**
     * auto saves responses
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward autoSaveAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	QaContent qaContent = qaSession.getQaContent();
	int intTotalQuestionCount = qaContent.getQaQueContents().size();

	if (!qaContent.isQuestionsSequenced()) {

	    for (int questionIndex = QaAppConstants.INITIAL_QUESTION_COUNT.intValue(); questionIndex <= intTotalQuestionCount; questionIndex++) {
		String newAnswer = request.getParameter("answer" + questionIndex);
		QaLearningAction.qaService.updateResponseWithNewAnswer(newAnswer, toolSessionID,
			new Long(questionIndex));
	    }

	} else {
	    String currentQuestionIndex = qaLearningForm.getCurrentQuestionIndex();
	    String newAnswer = qaLearningForm.getAnswer();
	    QaQueContent currentQuestion = QaLearningAction.qaService.getQuestionByContentAndDisplayOrder(new Long(
		    currentQuestionIndex), qaContent.getUid());

	    boolean isRequiredQuestionMissed = currentQuestion.isRequired() && isEmpty(newAnswer);
	    if (!isRequiredQuestionMissed) {
		QaLearningAction.qaService.updateResponseWithNewAnswer(newAnswer, toolSessionID, new Long(
			currentQuestionIndex));
	    }
	}
	return null;
    }

    /**
     * enables retaking the activity
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
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	qaLearningForm.setCurrentQuestionIndex(new Integer(1).toString());

	String httpSessionID = qaLearningForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setToolContentID(qaContent.getQaContentId().toString());

	// create mapQuestions
	Map<Integer, QaQuestionDTO> mapQuestions = generalLearnerFlowDTO.getMapQuestionContentLearner();
	generalLearnerFlowDTO.setMapQuestions(mapQuestions);
	generalLearnerFlowDTO.setTotalQuestionCount(new Integer(mapQuestions.size()));
	generalLearnerFlowDTO.setRemainingQuestionCount(new Integer(mapQuestions.size()).toString());
	qaLearningForm.setTotalQuestionCount(new Integer(mapQuestions.size()).toString());

	// populate answers
	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	LearningUtil.populateAnswers(sessionMap, qaContent, qaQueUsr, mapQuestions, generalLearnerFlowDTO,
		QaLearningAction.qaService);

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
	qaLearningForm.resetAll();
	return (mapping.findForward(QaAppConstants.LOAD_LEARNER));
    }

    /**
     * Stores all results and moves onto the next step. If view other users answers = true, then goes to the view all
     * answers screen, otherwise goes straight to the reflection screen (if any).
     * 
     * @return Learner Report for a session
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward storeAllResults(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;
	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	String userID = request.getParameter("userID");
	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	Long toolContentID = qaSession.getQaContent().getQaContentId();
	QaContent qaContent = qaSession.getQaContent();

	String httpSessionID = qaLearningForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	Map mapAnswers = (Map) sessionMap.get(QaAppConstants.MAP_ALL_RESULTS_KEY);

	// LearningUtil.storeResponses(mapAnswers, qaService, toolContentID, new Long(toolSessionID));
	// mark response as finalised
	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	qaQueUsr.setResponseFinalized(true);
	QaLearningAction.qaService.updateQaQueUsr(qaQueUsr);

	qaLearningForm.resetUserActions();
	qaLearningForm.setSubmitAnswersContent(null);

	if (qaContent.isShowOtherAnswers()) {
	    GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    generalLearnerFlowDTO.setHttpSessionID(httpSessionID);
	    String isUserNamesVisibleBoolean = generalLearnerFlowDTO.getUserNameVisible();
	    boolean isUserNamesVisible = new Boolean(isUserNamesVisibleBoolean).booleanValue();

	    return prepareViewAllAnswers(mapping, request, qaLearningForm, toolSessionID, userID, qaSession, qaContent,
		    generalLearnerFlowDTO, isUserNamesVisible);

	} else if (qaContent.isReflect()) {
	    return forwardtoReflection(mapping, request, qaContent, toolSessionID, userID, qaLearningForm);

	} else {
	    return endLearning(mapping, qaLearningForm, request, response);
	}
    }

    /** Set up the data for the view all answers screen */
    private ActionForward prepareViewAllAnswers(ActionMapping mapping, HttpServletRequest request,
	    QaLearningForm qaLearningForm, String toolSessionID, String userID, QaSession qaSession,
	    QaContent qaContent, GeneralLearnerFlowDTO generalLearnerFlowDTO, boolean isUserNamesVisible) {

	QaLearningAction.refreshSummaryData(request, qaContent, QaLearningAction.qaService, isUserNamesVisible,
		toolSessionID, userID, generalLearnerFlowDTO);

	generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(false).toString());

	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());

	qaLearningForm.resetAll();

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	boolean useSelectLeaderToolOuput = qaContent.isUseSelectLeaderToolOuput();
	generalLearnerFlowDTO.setUseSelectLeaderToolOuput(new Boolean(useSelectLeaderToolOuput).toString());
	
	boolean allowRichEditor = qaContent.isAllowRichEditor();
	generalLearnerFlowDTO.setAllowRichEditor(new Boolean(allowRichEditor).toString());

	generalLearnerFlowDTO.setAllowRateAnswers(new Boolean(qaContent.isAllowRateAnswers()).toString());

	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	generalLearnerFlowDTO.setUserUid(qaQueUsr.getQueUsrId().toString());

	int sessionUserCount = 0;
	if (qaSession.getQaQueUsers() != null) {
	    sessionUserCount = qaSession.getQaQueUsers().size();
	}

	if (sessionUserCount > 1) {
	    // there are multiple user responses
	    generalLearnerFlowDTO.setExistMultipleUserResponses(new Boolean(true).toString());
	}

	boolean usernameVisible = qaContent.isUsernameVisible();
	generalLearnerFlowDTO.setUserNameVisible(new Boolean(usernameVisible).toString());

	NotebookEntry notebookEntry = QaLearningAction.qaService.getEntry(new Long(toolSessionID),
		CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    // String notebookEntryPresentable=QaUtils.replaceNewLines(notebookEntry.getEntry());
	    String notebookEntryPresentable = notebookEntry.getEntry();
	    qaLearningForm.setEntryText(notebookEntryPresentable);
	}

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
	return (mapping.findForward(QaAppConstants.INDIVIDUAL_LEARNER_REPORT));
    }

    /**
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
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");

	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	String isUserNamesVisibleBoolean = generalLearnerFlowDTO.getUserNameVisible();
	boolean isUserNamesVisible = new Boolean(isUserNamesVisibleBoolean).booleanValue();

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);
	generalLearnerFlowDTO.setHttpSessionID(httpSessionID);

	/*recreate the users and responses*/
	qaLearningForm.resetUserActions();
	qaLearningForm.setSubmitAnswersContent(null);

	QaLearningAction.refreshSummaryData(request, qaContent, QaLearningAction.qaService, isUserNamesVisible,
		toolSessionID, userID, generalLearnerFlowDTO);

	generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(false).toString());

	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());
	// generalLearnerFlowDTO.setNotebookEntriesVisible(new Boolean(false).toString());

	qaLearningForm.resetAll();

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	boolean allowRichEditor = qaContent.isAllowRichEditor();
	generalLearnerFlowDTO.setAllowRichEditor(new Boolean(allowRichEditor).toString());

	boolean useSelectLeaderToolOuput = qaContent.isUseSelectLeaderToolOuput();
	generalLearnerFlowDTO.setUseSelectLeaderToolOuput(new Boolean(useSelectLeaderToolOuput).toString());
	
	generalLearnerFlowDTO.setAllowRateAnswers(new Boolean(qaContent.isAllowRateAnswers()).toString());

	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	generalLearnerFlowDTO.setUserUid(qaQueUsr.getQueUsrId().toString());

	int sessionUserCount = 0;
	if (qaSession.getQaQueUsers() != null) {
	    sessionUserCount = qaSession.getQaQueUsers().size();
	}

	if (sessionUserCount > 1) {
	    generalLearnerFlowDTO.setExistMultipleUserResponses(new Boolean(true).toString());
	}

	boolean usernameVisible = qaContent.isUsernameVisible();
	generalLearnerFlowDTO.setUserNameVisible(new Boolean(usernameVisible).toString());

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	return (mapping.findForward(QaAppConstants.INDIVIDUAL_LEARNER_REPORT));
    }

    /**
     * moves to the next question and modifies the map ActionForward
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
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);
	String httpSessionID = qaLearningForm.getHttpSessionID();
	qaLearningForm.setHttpSessionID(httpSessionID);

	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	storeSequentialAnswer(qaLearningForm, request, generalLearnerFlowDTO, true);

	qaLearningForm.resetAll();
	return (mapping.findForward(QaAppConstants.LOAD_LEARNER));
    }

    /**
     * Get the answer from the form and copy into DTO. Set up the next question. If the current question is required and
     * the answer is blank, then just persist the error and don't change questions.
     * 
     * @param form
     * @param request
     * @param generalLearnerFlowDTO
     * @param getNextQuestion
     * @return
     */
    private Map storeSequentialAnswer(ActionForm form, HttpServletRequest request,
	    GeneralLearnerFlowDTO generalLearnerFlowDTO, boolean getNextQuestion) {
	QaLearningForm qaLearningForm = (QaLearningForm) form;
	String httpSessionID = qaLearningForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String currentQuestionIndex = qaLearningForm.getCurrentQuestionIndex();

	Map mapAnswers = (Map) sessionMap.get(QaAppConstants.MAP_ALL_RESULTS_KEY);
	if (mapAnswers == null) {
	    mapAnswers = new TreeMap(new QaComparator());
	}

	String newAnswer = qaLearningForm.getAnswer();
	Map mapSequentialAnswers = (Map) sessionMap.get(QaAppConstants.MAP_SEQUENTIAL_ANSWERS_KEY);
	if (mapSequentialAnswers.size() >= new Integer(currentQuestionIndex).intValue()) {
	    mapSequentialAnswers.remove(new Long(currentQuestionIndex).toString());
	}
	mapSequentialAnswers.put(new Long(currentQuestionIndex).toString(), newAnswer);
	mapAnswers.put(currentQuestionIndex, newAnswer);

	int nextQuestionOffset = getNextQuestion ? 1 : -1;

	// is this question required and are they trying to go to the next question?
	// if so, check if the answer is blank and generate an error if it is blank.
	Map<Integer, QaQuestionDTO> questionContentMap = generalLearnerFlowDTO.getMapQuestionContentLearner();
	QaQuestionDTO dto = questionContentMap.get(currentQuestionIndex);
	boolean isRequiredQuestionMissed = dto.isRequired() && isEmpty(newAnswer);
	if (getNextQuestion && isRequiredQuestionMissed) {
	    ActionMessages errors = new ActionMessages();
	    errors.add(Globals.ERROR_KEY, new ActionMessage("error.required", currentQuestionIndex));
	    saveErrors(request, errors);
	    nextQuestionOffset = 0;
	}

	// store
	if (!isRequiredQuestionMissed) {
	    QaLearningAction.qaService.updateResponseWithNewAnswer(newAnswer, qaLearningForm.getToolSessionID(),
		    new Long(currentQuestionIndex));
	}

	sessionMap.put(QaAppConstants.MAP_ALL_RESULTS_KEY, mapAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());
	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	int intCurrentQuestionIndex = new Integer(currentQuestionIndex).intValue() + nextQuestionOffset;
	String currentAnswer = "";
	if (mapAnswers.size() >= intCurrentQuestionIndex) {
	    currentAnswer = (String) mapAnswers.get(new Long(intCurrentQuestionIndex).toString());
	}
	generalLearnerFlowDTO.setCurrentAnswer(currentAnswer);

	// currentQuestionIndex will be:
	generalLearnerFlowDTO.setCurrentQuestionIndex(new Integer(intCurrentQuestionIndex));

	String totalQuestionCount = qaLearningForm.getTotalQuestionCount();

	String userFeedback = LearningUtil
		.feedBackAnswersProgress(request, intCurrentQuestionIndex, totalQuestionCount);
	generalLearnerFlowDTO.setUserFeedback(userFeedback);

	String remQCount = LearningUtil.getRemainingQuestionCount(intCurrentQuestionIndex, totalQuestionCount);
	generalLearnerFlowDTO.setRemainingQuestionCount(remQCount);

	qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */

	sessionMap.put(QaAppConstants.MAP_SEQUENTIAL_ANSWERS_KEY, mapSequentialAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	return mapSequentialAnswers;
    }

    /**
     * Is this string empty? Need to strip out all HTML tags first otherwise an empty DIV might look like a valid answer
     * Smileys and math functions only put in an img tag so explicitly look for that.
     */
    private boolean isEmpty(String answer) {
	if ((answer.indexOf("<img") > -1) || (answer.indexOf("<IMG") > -1)) {
	    return false;
	} else {
	    return StringUtils.isBlank(WebUtil.removeHTMLtags(answer));
	}
    }

    /**
     * moves to the previous question and modifies the map ActionForward
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

	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;
	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String httpSessionID = qaLearningForm.getHttpSessionID();
	qaLearningForm.setHttpSessionID(httpSessionID);
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);
	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);

	storeSequentialAnswer(qaLearningForm, request, generalLearnerFlowDTO, false);

	qaLearningForm.resetAll();
	return (mapping.findForward(QaAppConstants.LOAD_LEARNER));
    }

    /**
     * Rates answers submitted by other learners.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward rateResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	float rating = Float.parseFloat(request.getParameter("rate"));
	Long responseId = WebUtil.readLongParam(request, "idBox");
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	Long userId = new Long(user.getUserID().intValue());

	AverageRatingDTO averageRatingDTO = qaService.rateResponse(responseId, userId, toolSessionID, rating);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put("averageRating", averageRatingDTO.getRating());
	JSONObject.put("numberOfVotes", averageRatingDTO.getNumberOfVotes());
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * finishes the user's tool activity
     * 
     * @param request
     * @param qaService
     * @param response
     * @throws IOException
     * @throws ToolException
     */
    public ActionForward endLearning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	qaLearningForm.setUserID(userID);

	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());

	QaQueUsr qaQueUsr = QaLearningAction.qaService.getUserByIdAndSession(new Long(userID),
		qaSession.getQaSessionId());
	qaQueUsr.setLearnerFinished(true);
	QaLearningAction.qaService.updateQaQueUsr(qaQueUsr);

	/*
	 * The learner is done with the tool session. The tool needs to clean-up.
	 */
	HttpSession ss = SessionManager.getSession();
	/*get back login user DTO*/
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
	qaSession.setSession_status(QaAppConstants.COMPLETED);
	QaLearningAction.qaService.updateQaSession(qaSession);

	String httpSessionID = qaLearningForm.getHttpSessionID();
	// request.getSession().removeAttribute(httpSessionID);
	qaLearningForm.setHttpSessionID(httpSessionID);

	qaLearningForm.resetAll();

	String nextActivityUrl = QaLearningAction.qaService.leaveToolSession(new Long(toolSessionID), new Long(user
		.getUserID().longValue()));
	response.sendRedirect(nextActivityUrl);

	return null;
    }

    public ActionForward updateReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	qaLearningForm.setUserID(userID);

	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());

	QaContent qaContent = qaSession.getQaContent();

	QaQueUsr qaQueUsr = QaLearningAction.qaService.getUserByIdAndSession(new Long(userID),
		qaSession.getQaSessionId());

	String entryText = request.getParameter("entryText");
	qaLearningForm.setEntryText(entryText);

	NotebookEntry notebookEntryLocal = new NotebookEntry();
	notebookEntryLocal.setEntry(entryText);
	// notebookEntry.setUser(qaQueUsr);
	User user = new User();
	user.setUserId(new Integer(userID));
	notebookEntryLocal.setUser(user);

	QaLearningAction.qaService.updateEntry(notebookEntryLocal);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = new GeneralLearnerFlowDTO();

	generalLearnerFlowDTO.setNotebookEntry(entryText);
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
	Boolean isUserNamesVisibleBoolean = new Boolean(qaContent.isUsernameVisible());
	boolean isUserNamesVisible = isUserNamesVisibleBoolean.booleanValue();

	QaLearningAction.refreshSummaryData(request, qaContent, QaLearningAction.qaService, isUserNamesVisible,
		toolSessionID, userID, generalLearnerFlowDTO);

	int sessionUserCount = 0;
	if (qaSession.getQaQueUsers() != null) {
	    sessionUserCount = qaSession.getQaQueUsers().size();
	}

	if (sessionUserCount > 1) {
	    // there are multiple user responses
	    generalLearnerFlowDTO.setExistMultipleUserResponses(new Boolean(true).toString());
	}

	boolean isLearnerFinished = qaQueUsr.isLearnerFinished();
	generalLearnerFlowDTO.setRequestLearningReportViewOnly(new Boolean(isLearnerFinished).toString());

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	boolean allowRichEditor = qaContent.isAllowRichEditor();
	generalLearnerFlowDTO.setAllowRichEditor(new Boolean(allowRichEditor).toString());

	boolean useSelectLeaderToolOuput = qaContent.isUseSelectLeaderToolOuput();
	generalLearnerFlowDTO.setUseSelectLeaderToolOuput(new Boolean(useSelectLeaderToolOuput).toString());
	
	generalLearnerFlowDTO.setAllowRateAnswers(new Boolean(qaContent.isAllowRateAnswers()).toString());

	NotebookEntry notebookEntry = QaLearningAction.qaService.getEntry(new Long(toolSessionID),
		CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    // String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
	    qaLearningForm.setEntryText(notebookEntry.getEntry());
	}

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	return (mapping.findForward(QaAppConstants.REVISITED_LEARNER_REP));
    }

    /**
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
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	LearningUtil.saveFormRequestData(request, qaLearningForm);

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	qaLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	qaLearningForm.setUserID(userID);

	String reflectionEntry = request.getParameter(QaAppConstants.ENTRY_TEXT);

	QaLearningAction.qaService.createNotebookEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		QaAppConstants.MY_SIGNATURE, new Integer(userID), reflectionEntry);

	qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */
	return endLearning(mapping, form, request, response);
    }

    /**
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
	initializeQAService();
	QaLearningForm qaLearningForm = (QaLearningForm) form;

	String httpSessionID = qaLearningForm.getHttpSessionID();

	qaLearningForm.setHttpSessionID(httpSessionID);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());

	QaContent qaContent = qaSession.getQaContent();

	String userID = request.getParameter("userID");
	qaLearningForm.setUserID(userID);

	return forwardtoReflection(mapping, request, qaContent, toolSessionID, userID, qaLearningForm);
    }

    private ActionForward forwardtoReflection(ActionMapping mapping, HttpServletRequest request, QaContent qaContent,
	    String toolSessionID, String userID, QaLearningForm qaLearningForm) {

	GeneralLearnerFlowDTO generalLearnerFlowDTO = new GeneralLearnerFlowDTO();
	generalLearnerFlowDTO.setActivityTitle(qaContent.getTitle());
	String reflectionSubject = qaContent.getReflectionSubject();
	// reflectionSubject = QaUtils.replaceNewLines(reflectionSubject);
	generalLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	// attempt getting notebookEntry
	NotebookEntry notebookEntry = QaLearningAction.qaService.getEntry(new Long(toolSessionID),
		CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    // String notebookEntryPresentable=QaUtils.replaceNewLines(notebookEntry.getEntry());
	    String notebookEntryPresentable = notebookEntry.getEntry();
	    generalLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	    qaLearningForm.setEntryText(notebookEntryPresentable);
	}

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
	qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */

	qaLearningForm.resetAll();
	return (mapping.findForward(QaAppConstants.NOTEBOOK));
    }
    
    /**
     * populates data for summary screen, view all results screen and export
     * portfolio.
     * 
     * User id is needed if isUserNamesVisible is false && learnerRequest is
     * true, as it is required to work out if the data being analysed is the
     * current user.
     * 
     * @param request
     * @param qaContent
     * @param qaService
     * @param isUserNamesVisible
     * @param isLearnerRequest
     * @param userId
     */
    public static void refreshSummaryData(HttpServletRequest request, QaContent qaContent, IQaService qaService,
	    boolean isUserNamesVisible, String sessionId, String userId,
	    GeneralLearnerFlowDTO generalLearnerFlowDTO) {

	List listMonitoredAnswersContainerDTO = MonitoringUtil.buildGroupsQuestionData(request, qaContent, qaService,
		isUserNamesVisible, true, sessionId, userId);

	if (generalLearnerFlowDTO != null) {
	    generalLearnerFlowDTO.setListMonitoredAnswersContainerDTO(listMonitoredAnswersContainerDTO);
	    generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
	    request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
	}

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);
    }



    private void initializeQAService() {
	if (QaLearningAction.qaService == null) {
	    QaLearningAction.qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	}
    }

    private QaQueUsr getCurrentUser(String toolSessionID) {

	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(toolUser.getUserID().longValue());

	return QaLearningAction.qaService.getUserByIdAndSession(userId, new Long(toolSessionID));
    }

}
