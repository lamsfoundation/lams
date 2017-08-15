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


package org.lamsfoundation.lams.tool.qa.web.action;

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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dto.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.LearningUtil;
import org.lamsfoundation.lams.tool.qa.util.QaComparator;
import org.lamsfoundation.lams.tool.qa.util.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.web.form.QaLearningForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.ValidationUtil;
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

	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	//prohibit users from submitting answers after response is finalized but Resubmit button is not pressed (e.g. using 2 browsers)
	if (qaQueUsr.isResponseFinalized()) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("learningStarter"));
	    redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	    redirect.addParameter(QaAppConstants.MODE, "learner");
	    return redirect;
	}

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);

	String totalQuestionCount = generalLearnerFlowDTO.getTotalQuestionCount().toString();
	int intTotalQuestionCount = new Integer(totalQuestionCount).intValue();

	String questionListingMode = generalLearnerFlowDTO.getQuestionListingMode();

	Map<String, String> mapAnswers = new TreeMap<String, String>(new QaComparator());
	Map<String, String> mapAnswersPresentable = new TreeMap<String, String>(new QaComparator());

	String forwardName = QaAppConstants.INDIVIDUAL_LEARNER_RESULTS;
	ActionMessages errors = new ActionMessages();

	String httpSessionID = qaLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	/* if the listing mode is QUESTION_LISTING_MODE_COMBINED populate the answers here */
	if (questionListingMode.equalsIgnoreCase(QaAppConstants.QUESTION_LISTING_MODE_COMBINED)) {

	    for (int questionIndex = QaAppConstants.INITIAL_QUESTION_COUNT
		    .intValue(); questionIndex <= intTotalQuestionCount; questionIndex++) {
		// TestHarness can not send "answerX" fields, so stick to the original, unfiltered field
		boolean isTestHarness = Boolean.valueOf(request.getParameter("testHarness"));
		String answerParamName = "answer" + questionIndex + (isTestHarness ? "__textarea" : "");
		String answer = request.getParameter(answerParamName);

		Integer questionIndexInteger = new Integer(questionIndex);
		mapAnswers.put(questionIndexInteger.toString(), answer);
		mapAnswersPresentable.put(questionIndexInteger.toString(), answer);

		//validate
		ActionMessages newErrors = validateQuestionAnswer(answer, questionIndexInteger, generalLearnerFlowDTO);
		errors.add(newErrors);

		// store
		if (errors.isEmpty()) {
		    QaLearningAction.qaService.updateResponseWithNewAnswer(answer, toolSessionID,
			    new Long(questionIndex), false);
		}
	    }

	} else {
	    Object[] results = storeSequentialAnswer(qaLearningForm, request, generalLearnerFlowDTO, true);
	    mapAnswers = (Map<String, String>) results[0];
	    errors = (ActionMessages) results[1];

	    mapAnswersPresentable = (Map) sessionMap.get(QaAppConstants.MAP_ALL_RESULTS_KEY);
	    mapAnswersPresentable = QaLearningAction.removeNewLinesMap(mapAnswersPresentable);
	}

	//finalize response so user won't need to edit his answers again, if coming back to the activity after leaving activity at this point
	if (errors.isEmpty()) {
	    qaQueUsr.setResponseFinalized(true);
	    QaLearningAction.qaService.updateUser(qaQueUsr);

	    //in case of errors - prompt learner to enter answers again
	} else {
	    saveErrors(request, errors);
	    forwardName = QaAppConstants.LOAD_LEARNER;
	}

	generalLearnerFlowDTO.setMapAnswers(mapAnswers);
	generalLearnerFlowDTO.setMapAnswersPresentable(mapAnswersPresentable);

	/* mapAnswers will be used in the viewAllAnswers screen */
	if (sessionMap == null) {
	    sessionMap = new SessionMap<String, Object>();
	}

	sessionMap.put(QaAppConstants.MAP_ALL_RESULTS_KEY, mapAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	qaLearningForm.resetAll();
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());
	generalLearnerFlowDTO.setNoReeditAllowed(qaContent.isNoReeditAllowed());
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
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	//prohibit users from autosaving answers after response is finalized but Resubmit button is not pressed (e.g. using 2 browsers)
	if (qaQueUsr.isResponseFinalized()) {
	    return null;
	}

	LearningUtil.saveFormRequestData(request, qaLearningForm);
	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	QaContent qaContent = qaSession.getQaContent();
	int intTotalQuestionCount = qaContent.getQaQueContents().size();

	if (!qaContent.isQuestionsSequenced()) {

	    for (int questionIndex = QaAppConstants.INITIAL_QUESTION_COUNT
		    .intValue(); questionIndex <= intTotalQuestionCount; questionIndex++) {
		String newAnswer = request.getParameter("answer" + questionIndex);
		QaLearningAction.qaService.updateResponseWithNewAnswer(newAnswer, toolSessionID,
			new Long(questionIndex), true);
	    }

	} else {
	    String currentQuestionIndex = qaLearningForm.getCurrentQuestionIndex();
	    String newAnswer = qaLearningForm.getAnswer();
	    QaQueContent currentQuestion = QaLearningAction.qaService
		    .getQuestionByContentAndDisplayOrder(new Long(currentQuestionIndex), qaContent.getUid());

	    boolean isRequiredQuestionMissed = currentQuestion.isRequired() && isEmpty(newAnswer);
	    if (!isRequiredQuestionMissed) {
		QaLearningAction.qaService.updateResponseWithNewAnswer(newAnswer, toolSessionID,
			new Long(currentQuestionIndex), true);
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

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);

	qaLearningForm.setCurrentQuestionIndex(new Integer(1).toString());

	String httpSessionID = qaLearningForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);
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

	//in order to track whether redo button is pressed store this info
	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	qaQueUsr.setResponseFinalized(false);
	QaLearningAction.qaService.updateUser(qaQueUsr);

	// populate answers
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
	QaQueUsr user = qaService.getUserByIdAndSession(new Long(userID), new Long(toolSessionID));
	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());
	QaContent qaContent = qaSession.getQaContent();

	// LearningUtil.storeResponses(mapAnswers, qaService, toolContentID, new Long(toolSessionID));

	qaLearningForm.resetUserActions();
	qaLearningForm.setSubmitAnswersContent(null);

	if (qaContent.isShowOtherAnswers()) {
	    GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);
	    String httpSessionID = qaLearningForm.getHttpSessionID();
	    generalLearnerFlowDTO.setHttpSessionID(httpSessionID);

	    /** Set up the data for the view all answers screen */
	    QaLearningAction.refreshSummaryData(request, qaContent, qaSession, QaLearningAction.qaService, httpSessionID, user,
		    generalLearnerFlowDTO);

	    generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
	    generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(false).toString());

	    generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());

	    qaLearningForm.resetAll();

	    boolean lockWhenFinished = qaContent.isLockWhenFinished();
	    generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());
	    generalLearnerFlowDTO.setNoReeditAllowed(qaContent.isNoReeditAllowed());

	    boolean useSelectLeaderToolOuput = qaContent.isUseSelectLeaderToolOuput();
	    generalLearnerFlowDTO.setUseSelectLeaderToolOuput(new Boolean(useSelectLeaderToolOuput).toString());

	    boolean allowRichEditor = qaContent.isAllowRichEditor();
	    generalLearnerFlowDTO.setAllowRichEditor(new Boolean(allowRichEditor).toString());
	    generalLearnerFlowDTO.setUserUid(user.getQueUsrId().toString());

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
	    return (mapping.findForward(QaAppConstants.LEARNER_REP));

	} else if (qaContent.isReflect()) {
	    return forwardtoReflection(mapping, request, qaContent, toolSessionID, userID, qaLearningForm);

	} else {
	    return endLearning(mapping, qaLearningForm, request, response);
	}
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
	QaQueUsr user = qaService.getUserByIdAndSession(new Long(userID), new Long(toolSessionID));

	QaSession qaSession = QaLearningAction.qaService.getSessionById(new Long(toolSessionID).longValue());

	QaContent qaContent = qaSession.getQaContent();

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);

	String httpSessionID = qaLearningForm.getHttpSessionID();
	qaLearningForm.setHttpSessionID(httpSessionID);
	generalLearnerFlowDTO.setHttpSessionID(httpSessionID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	/* recreate the users and responses */
	qaLearningForm.resetUserActions();
	qaLearningForm.setSubmitAnswersContent(null);

	QaLearningAction.refreshSummaryData(request, qaContent, qaSession, QaLearningAction.qaService, httpSessionID, user,
		generalLearnerFlowDTO);

	generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(false).toString());

	generalLearnerFlowDTO.setReflection(new Boolean(qaContent.isReflect()).toString());
	// generalLearnerFlowDTO.setNotebookEntriesVisible(new Boolean(false).toString());

	qaLearningForm.resetAll();

	boolean lockWhenFinished;
	boolean noReeditAllowed;
	if (sessionMap.get("noRefresh") != null && (boolean) sessionMap.get("noRefresh")) {
	    lockWhenFinished = true;
	    noReeditAllowed = true;
	} else {
	    lockWhenFinished = qaContent.isLockWhenFinished();
	    noReeditAllowed = qaContent.isNoReeditAllowed();
	}
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());
	generalLearnerFlowDTO.setNoReeditAllowed(noReeditAllowed);

	boolean allowRichEditor = qaContent.isAllowRichEditor();
	generalLearnerFlowDTO.setAllowRichEditor(new Boolean(allowRichEditor).toString());

	boolean useSelectLeaderToolOuput = qaContent.isUseSelectLeaderToolOuput();
	generalLearnerFlowDTO.setUseSelectLeaderToolOuput(new Boolean(useSelectLeaderToolOuput).toString());

	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	generalLearnerFlowDTO.setUserUid(qaQueUsr.getQueUsrId().toString());

	boolean usernameVisible = qaContent.isUsernameVisible();
	generalLearnerFlowDTO.setUserNameVisible(new Boolean(usernameVisible).toString());

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	return (mapping.findForward(QaAppConstants.LEARNER_REP));
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

	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	//prohibit users from submitting answers after response is finalized but Resubmit button is not pressed (e.g. using 2 browsers)
	if (qaQueUsr.isResponseFinalized()) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("learningStarter"));
	    redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	    redirect.addParameter(QaAppConstants.MODE, "learner");
	    return redirect;
	}

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);

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
    private Object[] storeSequentialAnswer(ActionForm form, HttpServletRequest request,
	    GeneralLearnerFlowDTO generalLearnerFlowDTO, boolean getNextQuestion) {
	QaLearningForm qaLearningForm = (QaLearningForm) form;
	String httpSessionID = qaLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String currentQuestionIndex = qaLearningForm.getCurrentQuestionIndex();

	Map<String, String> mapAnswers = (Map<String, String>) sessionMap.get(QaAppConstants.MAP_ALL_RESULTS_KEY);
	if (mapAnswers == null) {
	    mapAnswers = new TreeMap<String, String>(new QaComparator());
	}

	String newAnswer = qaLearningForm.getAnswer();
	Map<String, String> mapSequentialAnswers = (Map<String, String>) sessionMap
		.get(QaAppConstants.MAP_SEQUENTIAL_ANSWERS_KEY);
	if (mapSequentialAnswers.size() >= new Integer(currentQuestionIndex).intValue()) {
	    mapSequentialAnswers.remove(new Long(currentQuestionIndex).toString());
	}
	mapSequentialAnswers.put(new Long(currentQuestionIndex).toString(), newAnswer);
	mapAnswers.put(currentQuestionIndex, newAnswer);

	int nextQuestionOffset = getNextQuestion ? 1 : -1;

	// validation only if trying to go to the next question
	ActionMessages errors = new ActionMessages();
	if (getNextQuestion) {
	    errors = validateQuestionAnswer(newAnswer, new Integer(currentQuestionIndex), generalLearnerFlowDTO);
	}

	// store
	if (errors.isEmpty()) {
	    QaLearningAction.qaService.updateResponseWithNewAnswer(newAnswer, qaLearningForm.getToolSessionID(),
		    new Long(currentQuestionIndex), false);
	} else {
	    saveErrors(request, errors);
	    nextQuestionOffset = 0;
	}

	int intCurrentQuestionIndex = new Integer(currentQuestionIndex).intValue() + nextQuestionOffset;
	String currentAnswer = "";
	if (mapAnswers.size() >= intCurrentQuestionIndex) {
	    currentAnswer = mapAnswers.get(new Long(intCurrentQuestionIndex).toString());
	}
	generalLearnerFlowDTO.setCurrentAnswer(currentAnswer);

	// currentQuestionIndex will be:
	generalLearnerFlowDTO.setCurrentQuestionIndex(new Integer(intCurrentQuestionIndex));

	String totalQuestionCount = qaLearningForm.getTotalQuestionCount();

	int remainingQuestionCount = new Long(totalQuestionCount).intValue()
		- new Integer(currentQuestionIndex).intValue() + 1;
	String userFeedback = "";
	if (remainingQuestionCount != 0) {
	    userFeedback = "Remaining question count: " + remainingQuestionCount;
	} else {
	    userFeedback = "End of the questions.";
	}
	generalLearnerFlowDTO.setUserFeedback(userFeedback);
	generalLearnerFlowDTO.setRemainingQuestionCount("" + remainingQuestionCount);

	qaLearningForm.resetUserActions(); /* resets all except submitAnswersContent */

	sessionMap.put(QaAppConstants.MAP_ALL_RESULTS_KEY, mapAnswers);
	sessionMap.put(QaAppConstants.MAP_SEQUENTIAL_ANSWERS_KEY, mapSequentialAnswers);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
	generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	return new Object[] { mapSequentialAnswers, errors };
    }

    private ActionMessages validateQuestionAnswer(String newAnswer, Integer questionIndex,
	    GeneralLearnerFlowDTO generalLearnerFlowDTO) {
	ActionMessages errors = new ActionMessages();

	Map<Integer, QaQuestionDTO> questionMap = generalLearnerFlowDTO.getMapQuestionContentLearner();
	QaQuestionDTO dto = questionMap.get(questionIndex);

	// if so, check if the answer is blank and generate an error if it is blank.
	boolean isRequiredQuestionMissed = dto.isRequired() && isEmpty(newAnswer);
	if (isRequiredQuestionMissed) {
	    errors.add(Globals.ERROR_KEY, new ActionMessage("error.required", questionIndex));
	}

	boolean isMinWordsLimitReached = ValidationUtil.isMinWordsLimitReached(newAnswer, dto.getMinWordsLimit(),
		Boolean.parseBoolean(generalLearnerFlowDTO.getAllowRichEditor()));
	if (!isMinWordsLimitReached) {
	    errors.add(Globals.ERROR_KEY,
		    new ActionMessage("label.minimum.number.words", ": " + dto.getMinWordsLimit()));
	}

	return errors;
    }

    /**
     * Is this string empty? Need to strip out all HTML tags first otherwise an empty DIV might look like a valid answer
     * Smileys and math functions only put in an img tag so explicitly look for that.
     */
    private boolean isEmpty(String answer) {
	if ((answer != null) && ((answer.indexOf("<img") > -1) || (answer.indexOf("<IMG") > -1))) {
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

	QaQueUsr qaQueUsr = getCurrentUser(toolSessionID);
	//prohibit users from submitting answers after response is finalized but Resubmit button is not pressed (e.g. using 2 browsers)
	if (qaQueUsr.isResponseFinalized()) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("learningStarter"));
	    redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionID);
	    redirect.addParameter(QaAppConstants.MODE, "learner");
	    return redirect;
	}

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);

	storeSequentialAnswer(qaLearningForm, request, generalLearnerFlowDTO, false);

	qaLearningForm.resetAll();
	return (mapping.findForward(QaAppConstants.LOAD_LEARNER));
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
	QaLearningAction.qaService.updateUser(qaQueUsr);

	/*
	 * The learner is done with the tool session. The tool needs to clean-up.
	 */
	HttpSession ss = SessionManager.getSession();
	/* get back login user DTO */
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
	qaSession.setSession_status(QaAppConstants.COMPLETED);
	QaLearningAction.qaService.updateSession(qaSession);

	String httpSessionID = qaLearningForm.getHttpSessionID();
	// request.getSession().removeAttribute(httpSessionID);
	qaLearningForm.setHttpSessionID(httpSessionID);

	qaLearningForm.resetAll();

	String nextActivityUrl = QaLearningAction.qaService.leaveToolSession(new Long(toolSessionID),
		new Long(user.getUserID().longValue()));
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

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaService, qaContent);

	generalLearnerFlowDTO.setNotebookEntry(entryText);
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());

	QaLearningAction.refreshSummaryData(request, qaContent, qaSession, QaLearningAction.qaService, httpSessionID, qaQueUsr,
		generalLearnerFlowDTO);

	boolean isLearnerFinished = qaQueUsr.isLearnerFinished();
	generalLearnerFlowDTO.setRequestLearningReportViewOnly(new Boolean(isLearnerFinished).toString());

	boolean lockWhenFinished = qaContent.isLockWhenFinished();
	generalLearnerFlowDTO.setLockWhenFinished(new Boolean(lockWhenFinished).toString());

	boolean allowRichEditor = qaContent.isAllowRichEditor();
	generalLearnerFlowDTO.setAllowRichEditor(new Boolean(allowRichEditor).toString());

	boolean useSelectLeaderToolOuput = qaContent.isUseSelectLeaderToolOuput();
	generalLearnerFlowDTO.setUseSelectLeaderToolOuput(new Boolean(useSelectLeaderToolOuput).toString());

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

	qaLearningForm.resetUserActions(); /* resets all except submitAnswersContent */
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
	qaLearningForm.resetUserActions(); /* resets all except submitAnswersContent */

	qaLearningForm.resetAll();
	return (mapping.findForward(QaAppConstants.NOTEBOOK));
    }

    /**
     * populates data for summary screen, view all results screen.
     *
     * User id is needed if isUserNamesVisible is false && learnerRequest is true, as it is required to work out if the
     * data being analysed is the current user.
     */
    public static void refreshSummaryData(HttpServletRequest request, QaContent qaContent, QaSession qaSession, IQaService qaService,
	    String httpSessionID, QaQueUsr user, GeneralLearnerFlowDTO generalLearnerFlowDTO) {

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);
	Long userId = user.getQueUsrId();
	Set<QaQueContent> questions = qaContent.getQaQueContents();
	generalLearnerFlowDTO.setQuestions(questions);
	generalLearnerFlowDTO.setUserNameVisible(new Boolean(qaContent.isUsernameVisible()).toString());

	// potentially empty list if the user starts the lesson after the time restriction has expired.
	List<QaUsrResp> userResponses = qaService.getResponsesByUserUid(user.getUid());

	//handle rating criterias
	int commentsMinWordsLimit = 0;
	boolean isCommentsEnabled = false;
	int countRatedQuestions = 0;
	if (qaContent.isAllowRateAnswers() ) {

	    if ( userResponses.isEmpty()) {
		Set<LearnerItemRatingCriteria> criterias = qaContent.getRatingCriterias();
		for ( LearnerItemRatingCriteria criteria : criterias ) {
		    if ( criteria.isCommentRating() ) {
			isCommentsEnabled = true;
			break;
		    }
		}
		
	    } else {
		// create itemIds list 
		List<Long> itemIds = new LinkedList<Long>();
		for (QaUsrResp responseIter : userResponses) {
		    itemIds.add(responseIter.getResponseId());
		}

		List<ItemRatingDTO> itemRatingDtos = qaService.getRatingCriteriaDtos(qaContent.getQaContentId(), qaSession.getQaSessionId(), itemIds,
			true, userId);
		sessionMap.put(AttributeNames.ATTR_ITEM_RATING_DTOS, itemRatingDtos);

		if (itemRatingDtos.size() > 0) {
		    commentsMinWordsLimit = itemRatingDtos.get(0).getCommentsMinWordsLimit();
		    isCommentsEnabled = itemRatingDtos.get(0).isCommentsEnabled();
		}

		//map itemRatingDto to corresponding response
		for (QaUsrResp response : userResponses) {

		    //find corresponding itemRatingDto
		    ItemRatingDTO itemRatingDto = null;
		    for (ItemRatingDTO itemRatingDtoIter : itemRatingDtos) {
			if (itemRatingDtoIter.getItemId().equals(response.getResponseId())) {
			    itemRatingDto = itemRatingDtoIter;
			    break;
			}
		    }

		    response.setItemRatingDto(itemRatingDto);
		}

		// store how many items are rated
		countRatedQuestions = qaService.getCountItemsRatedByUser(qaContent.getQaContentId(), userId.intValue());
	    }
	}
	
	request.setAttribute(TOOL_SESSION_ID, qaSession.getQaSessionId());
	
	sessionMap.put("commentsMinWordsLimit", commentsMinWordsLimit);
	sessionMap.put("isCommentsEnabled", isCommentsEnabled);
	sessionMap.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedQuestions);

	generalLearnerFlowDTO.setUserResponses(userResponses);
	generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
    }

    /**
     * Refreshes user list.
     */
    public ActionForward getResponses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	initializeQAService();

	// teacher timezone
	HttpSession ss = SessionManager.getSession();
	UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TimeZone userTimeZone = userDto.getTimeZone();
	
	boolean isAllowRateAnswers = WebUtil.readBooleanParam(request, "isAllowRateAnswers");
	boolean isAllowRichEditor = WebUtil.readBooleanParam(request, "isAllowRichEditor");
	boolean isOnlyLeadersIncluded = WebUtil.readBooleanParam(request, "isOnlyLeadersIncluded", false);
	Long qaContentId = WebUtil.readLongParam(request, "qaContentId");

	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	Long qaSessionId = WebUtil.readLongParam(request, "qaSessionId");

	//in case of monitoring we show all results. in case of learning - don't show results from the current user
	boolean isMonitoring = WebUtil.readBooleanParam(request, "isMonitoring", false);
	Long userId = isMonitoring ? -1 : WebUtil.readLongParam(request, "userId");

	//paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByCol1 = WebUtil.readIntParam(request, "column[0]", true);
	Integer sortByCol2 = WebUtil.readIntParam(request, "column[1]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = QaAppConstants.SORT_BY_NO;
	if (sortByCol1 != null) {
	    if (isMonitoring) {
		sorting = sortByCol1.equals(0) ? QaAppConstants.SORT_BY_USERNAME_ASC
			: QaAppConstants.SORT_BY_USERNAME_DESC;
	    } else {
		sorting = sortByCol1.equals(0) ? QaAppConstants.SORT_BY_ANSWER_ASC : QaAppConstants.SORT_BY_ANSWER_DESC;
	    }

	} else if (sortByCol2 != null) {
	    sorting = sortByCol2.equals(0) ? QaAppConstants.SORT_BY_RATING_ASC : QaAppConstants.SORT_BY_RATING_DESC;
	}

	List<QaUsrResp> responses = QaLearningAction.qaService.getResponsesForTablesorter(qaContentId, qaSessionId,
		questionUid, userId, isOnlyLeadersIncluded, page, size, sorting, searchString);

	JSONObject responcedata = new JSONObject();
	JSONArray rows = new JSONArray();

	responcedata.put("total_rows", QaLearningAction.qaService.getCountResponsesBySessionAndQuestion(qaSessionId,
		questionUid, userId, isOnlyLeadersIncluded, searchString));

	// handle rating criterias - even though we may have searched on ratings earlier we can't use the average ratings
	// calculated as they may have been averages over more than one criteria.
	List<ItemRatingDTO> itemRatingDtos = null;
	if (isAllowRateAnswers && !responses.isEmpty()) {
	    //create itemIds list
	    List<Long> itemIds = new LinkedList<Long>();
	    for (QaUsrResp response : responses) {
		itemIds.add(response.getResponseId());
	    }

	    //all comments required only for monitoring
	    boolean isCommentsByOtherUsersRequired = isMonitoring;
	    itemRatingDtos = QaLearningAction.qaService.getRatingCriteriaDtos(qaContentId, qaSessionId, itemIds,
		    isCommentsByOtherUsersRequired, userId);

	    // store how many items are rated
	    int countRatedQuestions = QaLearningAction.qaService.getCountItemsRatedByUser(qaContentId,
		    userId.intValue());
	    responcedata.put(AttributeNames.ATTR_COUNT_RATED_ITEMS, countRatedQuestions);
	}

	for (QaUsrResp response : responses) {
	    QaQueUsr user = response.getQaQueUser();

	    /*  LDEV-3891: This code has been commented out, as the escapeCsv puts double quotes in the string, which goes through to the 
	     * client and wrecks img src entries. It appears the browser cannot process the string with all the double quotes. 
	     * This is the second time it is being fixed - the escapeCsv was removed in LDEV-3448 and then added back in 
	     * when Peer Review was added (LDEV-3480). If escapeCsv needs to be added in again, make sure it does not break 
	     * learner added images being seen in monitoring.
	    //remove leading and trailing quotes
	    String answer = StringEscapeUtils.escapeCsv(response.getAnswer());
	    if (isAllowRichEditor && answer.startsWith("\"") && answer.length() >= 3) {
		answer = answer.substring(1, answer.length() - 1);
	    }
	     */

	    JSONObject responseRow = new JSONObject();
	    responseRow.put("responseUid", response.getResponseId().toString());
	    responseRow.put("answer", response.getAnswer());
	    responseRow.put("userName", StringEscapeUtils.escapeCsv(user.getFullname()));
	    responseRow.put("visible", new Boolean(response.isVisible()).toString());

	    // format attemptTime - got straight from server time to other timezones in formatter
	    // as trying to convert dates runs into tz issues - any Date object created is still
	    // in the server time zone.
	    Date attemptTime = response.getAttemptTime();
	    responseRow.put("attemptTime", DateUtil.convertToStringForJSON(attemptTime, request.getLocale()));
	    responseRow.put("timeAgo", DateUtil.convertToStringForTimeagoJSON(attemptTime)); 

	    if (isAllowRateAnswers) {

		//find corresponding itemRatingDto
		ItemRatingDTO itemRatingDto = null;
		for (ItemRatingDTO itemRatingDtoIter : itemRatingDtos) {
		    if (response.getResponseId().equals(itemRatingDtoIter.getItemId())) {
			itemRatingDto = itemRatingDtoIter;
			break;
		    }
		}

		boolean isItemAuthoredByUser = response.getQaQueUser().getQueUsrId().equals(userId);
		responseRow.put("isItemAuthoredByUser", isItemAuthoredByUser);

		JSONArray criteriasRows = new JSONArray();
		for (ItemRatingCriteriaDTO criteriaDto : itemRatingDto.getCriteriaDtos()) {
		    JSONObject criteriasRow = new JSONObject();
		    criteriasRow.put("ratingCriteriaId", criteriaDto.getRatingCriteria().getRatingCriteriaId());
		    criteriasRow.put("title", criteriaDto.getRatingCriteria().getTitle());
		    criteriasRow.put("averageRating", criteriaDto.getAverageRating());
		    criteriasRow.put("numberOfVotes", criteriaDto.getNumberOfVotes());
		    criteriasRow.put("userRating", criteriaDto.getUserRating());

		    criteriasRows.put(criteriasRow);
		}
		responseRow.put("criteriaDtos", criteriasRows);

		//handle comments
		responseRow.put("commentsCriteriaId", itemRatingDto.getCommentsCriteriaId());
		String commentPostedByUser = itemRatingDto.getCommentPostedByUser() == null ? ""
			: itemRatingDto.getCommentPostedByUser().getComment();
		responseRow.put("commentPostedByUser", commentPostedByUser);
		if (itemRatingDto.getCommentDtos() != null) {

		    JSONArray comments = new JSONArray();
		    for (RatingCommentDTO commentDto : itemRatingDto.getCommentDtos()) {
			JSONObject comment = new JSONObject();
			comment.put("comment", StringEscapeUtils.escapeCsv(commentDto.getComment()));

			if (isMonitoring) {
			    // format attemptTime
			    Date postedDate = commentDto.getPostedDate();
			    postedDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, postedDate);
			    comment.put("postedDate", DateUtil.convertToStringForJSON(postedDate, request.getLocale()));

			    comment.put("userFullName", StringEscapeUtils.escapeCsv(commentDto.getUserFullName()));
			}

			comments.put(comment);
		    }
		    responseRow.put("comments", comments);
		}
	    }

	    rows.put(responseRow);
	}
	responcedata.put("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    private static Map removeNewLinesMap(Map map) {
	Map newMap = new TreeMap(new QaStringComparator());

	Iterator itMap = map.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    String newText = "";
	    if (pairs.getValue().toString() != null) {
		newText = pairs.getValue().toString().replaceAll("\n", "<br>");
	    }
	    newMap.put(pairs.getKey(), newText);
	}
	return newMap;
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
