/****************************************************************
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
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.survey.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionRedirect;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.service.SurveyApplicationException;
import org.lamsfoundation.lams.tool.survey.util.IntegerComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.tool.survey.web.form.AnswerForm;
import org.lamsfoundation.lams.tool.survey.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {

	String param = mapping.getParameter();
	// -----------------------Survey Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("previousQuestion")) {
	    return previousQuestion(mapping, form, request, response);
	}
	if (param.equals("nextQuestion")) {
	    return nextQuestion(mapping, form, request, response);
	}
	if (param.equals("doSurvey")) {
	    return doSurvey(mapping, form, request, response);
	}

	if (param.equals("retake")) {
	    return retake(mapping, form, request, response);
	}

	if (param.equals("showOtherUsersAnswers")) {
	    return showOtherUsersAnswers(mapping, form, request, response);
	}

	if (param.equals("getOpenResponses")) {
	    return getOpenResponses(mapping, form, request, response);
	}

	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(SurveyConstants.ERROR);
    }

    /**
     * Read survey data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AnswerForm answerForm = (AnswerForm) form;
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	String sessionMapID = sessionMap.getSessionID();
	request.getSession().setAttribute(sessionMapID, sessionMap);
	answerForm.setSessionMapID(sessionMapID);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	Long sessionId = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));
	// it will be use when submissionDeadline or lock on finish page.
	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	// get back the survey and question list and display them on page
	ISurveyService service = getSurveyService();
	SurveyUser surveyUser = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    surveyUser = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	    // setting Learner
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, true);
	    answerForm.setUserID(userID);
	} else {
	    surveyUser = getCurrentUser(service, sessionId);
	}

	List<AnswerDTO> answers = service.getQuestionAnswers(sessionId, surveyUser.getUid());
	Survey survey = service.getSurveyBySessionId(sessionId);

	// get notebook entry
	String entryText = new String();
	NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		SurveyConstants.TOOL_SIGNATURE, surveyUser.getUserId().intValue());

	if (notebookEntry != null) {
	    entryText = notebookEntry.getEntry();
	}

	// get session from shared session.
	HttpSession ss = SessionManager.getSession();

	// basic information
	sessionMap.put(SurveyConstants.ATTR_TITLE, survey.getTitle());
	sessionMap.put(SurveyConstants.ATTR_SURVEY_INSTRUCTION, survey.getInstructions());
	// check whehter finish lock is on/off
	boolean lock = survey.getLockWhenFinished() && surveyUser.isSessionFinished();
	sessionMap.put(SurveyConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(SurveyConstants.ATTR_LOCK_ON_FINISH, survey.getLockWhenFinished());
	sessionMap.put(SurveyConstants.ATTR_SHOW_ON_ONE_PAGE, survey.isShowOnePage());
	sessionMap.put(SurveyConstants.ATTR_SHOW_OTHER_USERS_ANSWERS, survey.isShowOtherUsersAnswers());
	sessionMap.put(SurveyConstants.ATTR_USER_FINISHED, surveyUser.isSessionFinished());
	sessionMap.put(SurveyConstants.ATTR_USER, surveyUser);

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(SurveyConstants.ATTR_REFLECTION_ON, survey.isReflectOnActivity());
	sessionMap.put(SurveyConstants.ATTR_REFLECTION_INSTRUCTION, survey.getReflectInstructions());
	sessionMap.put(SurveyConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (survey.isDefineLater()) {
	    return mapping.findForward(SurveyConstants.DEFINE_LATER);
	}

	// set contentInUse flag to true!
	survey.setContentInUse(true);
	survey.setDefineLater(false);
	service.saveOrUpdateSurvey(survey);

	ActivityPositionDTO activityPosition = WebUtil.putActivityPositionInRequestByToolSessionId(sessionId, request,
		getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	// check if there is submission deadline
	Date submissionDeadline = survey.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    // store submission deadline to sessionMap
	    sessionMap.put(SurveyConstants.ATTR_SUBMISSION_DEADLINE, submissionDeadline);

	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());

	    // calculate whether submission deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return mapping.findForward("submissionDeadline");
	    }
	}

	// init survey item list
	SortedMap<Integer, AnswerDTO> surveyItemList = getQuestionList(sessionMap);
	surveyItemList.clear();
	if (answers != null) {
	    for (AnswerDTO answer : answers) {
		surveyItemList.put(answer.getSequenceId(), answer);
	    }
	}
	if (survey.isShowOnePage()) {
	    answerForm.setQuestionSeqID(null);
	} else {
	    if (surveyItemList.size() > 0) {
		answerForm.setQuestionSeqID(surveyItemList.firstKey());
	    }
	}
	sessionMap.put(SurveyConstants.ATTR_TOTAL_QUESTIONS, surveyItemList.size());
	answerForm.setCurrentIdx(1);

	if (surveyItemList.size() < 2) {
	    answerForm.setPosition(SurveyConstants.POSITION_ONLY_ONE);
	} else {
	    answerForm.setPosition(SurveyConstants.POSITION_FIRST);
	}

	// if session is finished go to result pages.
	if (surveyUser.isSessionFinished() && !survey.isShowOtherUsersAnswers()) {
	    return mapping.findForward(SurveyConstants.FORWARD_RESULT);

	    //if show other users is ON and response is finalized - show results page with other users answers
	} else if (survey.isShowOtherUsersAnswers() && surveyUser.isResponseFinalized()) {
	    ActionRedirect redirect = new ActionRedirect(mapping.findForwardConfig("resultOtherUsers"));
	    redirect.addParameter(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	    return redirect;

	} else {
	    return mapping.findForward(SurveyConstants.SUCCESS);
	}
    }

    private ActionForward nextQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AnswerForm answerForm = (AnswerForm) form;
	Integer questionSeqID = answerForm.getQuestionSeqID();
	String sessionMapID = answerForm.getSessionMapID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);

	ActionErrors errors = getAnswer(request, surveyItemMap.get(questionSeqID));
	if (!errors.isEmpty()) {
	    return mapping.getInputForward();
	}

	// go to next question
	boolean next = false;
	for (Map.Entry<Integer, AnswerDTO> entry : surveyItemMap.entrySet()) {
	    if (entry.getKey().equals(questionSeqID)) {
		next = true;
		// failure tolerance: if arrive last one
		questionSeqID = entry.getKey();
		continue;
	    }
	    if (next) {
		questionSeqID = entry.getKey();
		break;
	    }
	}
	// get current question index of total questions
	int currIdx = new ArrayList<Integer>(surveyItemMap.keySet()).indexOf(questionSeqID) + 1;
	answerForm.setCurrentIdx(currIdx);
	// failure tolerance
	if (questionSeqID.equals(surveyItemMap.lastKey())) {
	    answerForm.setPosition(SurveyConstants.POSITION_LAST);
	} else {
	    answerForm.setPosition(SurveyConstants.POSITION_INSIDE);
	}
	answerForm.setQuestionSeqID(questionSeqID);
	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    private ActionForward previousQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AnswerForm answerForm = (AnswerForm) form;
	Integer questionSeqID = answerForm.getQuestionSeqID();
	String sessionMapID = answerForm.getSessionMapID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);

	ActionErrors errors = getAnswer(request, surveyItemMap.get(questionSeqID));
	if (!errors.isEmpty()) {
	    return mapping.getInputForward();
	}

	SortedMap<Integer, AnswerDTO> subMap = surveyItemMap.headMap(questionSeqID);
	if (subMap.isEmpty()) {
	    questionSeqID = surveyItemMap.firstKey();
	} else {
	    questionSeqID = subMap.lastKey();
	}

	// get current question index of total questions
	int currIdx = new ArrayList<Integer>(surveyItemMap.keySet()).indexOf(questionSeqID) + 1;
	answerForm.setCurrentIdx(currIdx);

	if (questionSeqID.equals(surveyItemMap.firstKey())) {
	    answerForm.setPosition(SurveyConstants.POSITION_FIRST);
	} else {
	    answerForm.setPosition(SurveyConstants.POSITION_INSIDE);
	}
	answerForm.setQuestionSeqID(questionSeqID);
	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    private ActionForward retake(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AnswerForm answerForm = (AnswerForm) form;
	Integer questionSeqID = answerForm.getQuestionSeqID();
	
	String sessionMapID = answerForm.getSessionMapID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);
	Collection<AnswerDTO> surveyItemList = surveyItemMap.values();

	if ( surveyItemList.size() < 2 || ( questionSeqID != null && questionSeqID > 0 ) ) {
	    answerForm.setPosition(SurveyConstants.POSITION_ONLY_ONE);
	} else {
	    answerForm.setPosition(SurveyConstants.POSITION_FIRST);
	}
	if ( questionSeqID == null || questionSeqID <= 0 ) {
	    Boolean onePage = (Boolean) sessionMap.get(SurveyConstants.ATTR_SHOW_ON_ONE_PAGE);
	    if ( ! onePage && surveyItemList.size() > 0) {
		answerForm.setQuestionSeqID(surveyItemMap.firstKey());
		questionSeqID = surveyItemMap.firstKey();
	    }
	}

	// get current question index of total questions
	int currIdx = new ArrayList<Integer>(surveyItemMap.keySet()).indexOf(questionSeqID) + 1;
	answerForm.setCurrentIdx(currIdx);

	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    private ActionForward showOtherUsersAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ISurveyService service = getSurveyService();
	String sessionMapID = request.getParameter("sessionMapID");
	request.setAttribute("sessionMapID", sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	List<AnswerDTO> answerDtos = new ArrayList<AnswerDTO>();
	for (SurveyQuestion question : surveyItemMap.values()) {
	    AnswerDTO answerDto = service.getQuestionResponse(sessionId, question.getUid());
	    answerDtos.add(answerDto);
	}
	request.setAttribute("answerDtos", answerDtos);

	SurveyUser surveyLearner = (SurveyUser) sessionMap.get(SurveyConstants.ATTR_USER);
	service.setResponseFinalized(surveyLearner.getUid());

	int countFinishedUser = service.getCountFinishedUsers(sessionId);
	request.setAttribute(SurveyConstants.ATTR_COUNT_FINISHED_USERS, countFinishedUser);

	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Get OpenResponses.
     */
    private ActionForward getOpenResponses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	ISurveyService service = getSurveyService();

	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	Long sessionId = WebUtil.readLongParam(request, "sessionId");

	//paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);

	int sorting = SurveyConstants.SORT_BY_DEAFAULT;
	if (isSort1 != null && isSort1.equals(0)) {
	    sorting = SurveyConstants.SORT_BY_ANSWER_ASC;
	} else if (isSort1 != null && isSort1.equals(1)) {
	    sorting = SurveyConstants.SORT_BY_ANSWER_DESC;
	}

	List<String> responses = service.getOpenResponsesForTablesorter(sessionId, questionUid, page, size, sorting);

	JSONArray rows = new JSONArray();

	JSONObject responcedata = new JSONObject();
	responcedata.put("total_rows", service.getCountResponsesBySessionAndQuestion(sessionId, questionUid));

	for (String response : responses) {
	    //JSONArray cell=new JSONArray();
	    //cell.put(StringEscapeUtils.escapeHtml(user.getFirstName()) + " " + StringEscapeUtils.escapeHtml(user.getLastName()) + " [" + StringEscapeUtils.escapeHtml(user.getLogin()) + "]");

	    JSONObject responseRow = new JSONObject();
	    responseRow.put("answer", StringEscapeUtils.escapeCsv(response));
//	    responseRow.put("attemptTime", response.getAttemptTime());

	    rows.put(responseRow);
	}
	responcedata.put("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    private ActionForward doSurvey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ISurveyService service = getSurveyService();

	AnswerForm answerForm = (AnswerForm) form;
	Integer questionSeqID = answerForm.getQuestionSeqID();
	String sessionMapID = answerForm.getSessionMapID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// validate
	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);
	Collection<AnswerDTO> surveyItemList = surveyItemMap.values();

	SurveyUser surveyLearner = (SurveyUser) sessionMap.get(SurveyConstants.ATTR_USER);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ActionErrors errors;
	if ((questionSeqID == null) || questionSeqID.equals(0)) {
	    errors = getAnswers(request);
	} else {
	    errors = getAnswer(request, surveyItemMap.get(questionSeqID));
	}
	if (!errors.isEmpty()) {
	    return mapping.getInputForward();
	}

	List<SurveyAnswer> answerList = new ArrayList<SurveyAnswer>();
	for (AnswerDTO question : surveyItemList) {
	    if (question.getAnswer() != null) {
		question.getAnswer().setUser(surveyLearner);
		answerList.add(question.getAnswer());
	    }
	}

	service.updateAnswerList(answerList);

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Survey survey = service.getSurveyBySessionId(sessionId);
	if (survey.isNotifyTeachersOnAnswerSumbit()) {
	    service.notifyTeachersOnAnswerSumbit(sessionId, surveyLearner);
	}

	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Finish learning session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ISurveyService service = getSurveyService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(SurveyConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (SurveyApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Display empty reflection form.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	ISurveyService submitFilesService = getSurveyService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SurveyConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ISurveyService service = getSurveyService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		SurveyConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL, SurveyConstants.TOOL_SIGNATURE,
		    userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Get answer by special question.
     */
    private ActionErrors getAnswer(HttpServletRequest request, AnswerDTO answerDto) {
	ActionErrors errors = new ActionErrors();
	// get sessionMap
	String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	SurveyAnswer answer = getAnswerFromPage(request, answerDto, sessionID);
	answerDto.setAnswer(answer);
	validateAnswers(request, answerDto, errors, answer);
	if (!errors.isEmpty()) {
	    addErrors(request, errors);
	}
	return errors;
    }

    /**
     * Get all answer for all questions in this page
     *
     * @param request
     * @return
     */
    private ActionErrors getAnswers(HttpServletRequest request) {
	ActionErrors errors = new ActionErrors();
	// get sessionMap
	String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Collection<AnswerDTO> answerDtoList = getQuestionList(sessionMap).values();

	for (AnswerDTO answerDto : answerDtoList) {
	    SurveyAnswer answer = getAnswerFromPage(request, answerDto, sessionID);
	    answerDto.setAnswer(answer);
	    validateAnswers(request, answerDto, errors, answer);
	}
	if (!errors.isEmpty()) {
	    addErrors(request, errors);
	}
	return errors;
    }

    private void validateAnswers(HttpServletRequest request, AnswerDTO question, ActionErrors errors,
	    SurveyAnswer answer) {
	boolean isAnswerEmpty = ((answer.getChoices() == null) && StringUtils.isBlank(answer.getAnswerText()));

	// for mandatory questions, answer can not be null.
	if (!question.isOptional() && isAnswerEmpty) {
	    errors.add(SurveyConstants.ERROR_MSG_KEY + question.getUid(),
		    new ActionMessage(SurveyConstants.ERROR_MSG_MANDATORY_QUESTION));
	}
	if ((question.getType() == SurveyConstants.QUESTION_TYPE_SINGLE_CHOICE) && question.isAppendText()
		&& !isAnswerEmpty) {
	    // for single choice, user only can choose one option or open text (if it has)
	    if (!StringUtils.isBlank(answer.getAnswerChoices()) && !StringUtils.isBlank(answer.getAnswerText())) {
		errors.add(SurveyConstants.ERROR_MSG_KEY + question.getUid(),
			new ActionMessage(SurveyConstants.ERROR_MSG_SINGLE_CHOICE));
	    }
	}
    }

    private SurveyAnswer getAnswerFromPage(HttpServletRequest request, AnswerDTO question, Long sessionID) {

	String[] choiceList = request.getParameterValues(SurveyConstants.PREFIX_QUESTION_CHOICE + question.getUid());
	String textEntry = request.getParameter(SurveyConstants.PREFIX_QUESTION_TEXT + question.getUid());

	SurveyAnswer answer = question.getAnswer();
	if (answer == null) {
	    answer = new SurveyAnswer();
	}
	answer.setAnswerChoices(SurveyWebUtils.getChoicesStr(choiceList));
	answer.setChoices(choiceList);

	answer.setAnswerText(textEntry);

	ISurveyService service = getSurveyService();
	answer.setUser(getCurrentUser(service, sessionID));
	answer.setUpdateDate(new Timestamp(new Date().getTime()));
	answer.setSurveyQuestion(question);
	return answer;
    }

    private ISurveyService getSurveyService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
    }

    /**
     * List save current survey items.
     *
     * @param request
     * @return
     */
    private SortedMap<Integer, AnswerDTO> getQuestionList(SessionMap<String, Object> sessionMap) {
	SortedMap<Integer, AnswerDTO> list = (SortedMap<Integer, AnswerDTO>) sessionMap
		.get(SurveyConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeMap<Integer, AnswerDTO>(new IntegerComparator());
	    sessionMap.put(SurveyConstants.ATTR_QUESTION_LIST, list);
	}
	return list;
    }

    private SurveyUser getCurrentUser(ISurveyService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	SurveyUser surveyUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);

	if (surveyUser == null) {
	    SurveySession session = service.getSurveySessionBySessionId(sessionId);
	    surveyUser = new SurveyUser(user, session);
	    service.createUser(surveyUser);
	}
	return surveyUser;
    }

    private SurveyUser getSpecifiedUser(ISurveyService service, Long sessionId, Integer userId) {
	return service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
    }

}
