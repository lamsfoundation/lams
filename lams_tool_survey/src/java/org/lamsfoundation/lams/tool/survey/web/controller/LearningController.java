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

package org.lamsfoundation.lams.tool.survey.web.controller;

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
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Steve.Ni
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    @Autowired
    @Qualifier("lasurvSurveyService")
    private ISurveyService surveyService;

    @Autowired
    @Qualifier("lasurvMessageService")
    private MessageService messageService;

    private static Logger log = Logger.getLogger(LearningController.class);

    /**
     * Read survey data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     */
    @RequestMapping(value = "/start")
    private String start(AnswerForm surveyForm, HttpServletRequest request) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	String sessionMapID = sessionMap.getSessionID();
	request.getSession().setAttribute(sessionMapID, sessionMap);
	surveyForm.setSessionMapID(sessionMapID);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	Long sessionId = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));
	// it will be use when submissionDeadline or lock on finish page.
	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	// get back the survey and question list and display them on page
	SurveyUser surveyUser = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    surveyUser = getSpecifiedUser(surveyService, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	    // setting Learner
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, true);
	    surveyForm.setUserID(userID);
	} else {
	    surveyUser = getCurrentUser(surveyService, sessionId);
	}

	List<AnswerDTO> answers = surveyService.getQuestionAnswers(sessionId, surveyUser.getUid());
	Survey survey = surveyService.getSurveyBySessionId(sessionId);

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

	// add define later support
	if (survey.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	// set contentInUse flag to true!
	survey.setContentInUse(true);
	survey.setDefineLater(false);
	surveyService.saveOrUpdateSurvey(survey);

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, surveyService.isLastActivity(sessionId));

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
		return "pages/learning/submissionDeadline";
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
	    surveyForm.setQuestionSeqID(null);
	} else {
	    if (surveyItemList.size() > 0) {
		surveyForm.setQuestionSeqID(surveyItemList.firstKey());
	    }
	}
	sessionMap.put(SurveyConstants.ATTR_TOTAL_QUESTIONS, surveyItemList.size());
	surveyForm.setCurrentIdx(1);

	if (surveyItemList.size() < 2) {
	    surveyForm.setPosition(SurveyConstants.POSITION_ONLY_ONE);
	} else {
	    surveyForm.setPosition(SurveyConstants.POSITION_FIRST);
	}

	// if session is finished go to result pages
	if ((surveyUser.isSessionFinished() || mode.isTeacher()) && !survey.isShowOtherUsersAnswers()) {
	    return "pages/learning/result";

	//if show other users is ON and response is finalized - show results page with other users answers
	} else if ((survey.isShowOtherUsersAnswers() || mode.isTeacher()) && surveyUser.isResponseFinalized()) {
	    String redirectURL = "redirect:/learning/showOtherUsersAnswers.do";
	    redirectURL = WebUtil.appendParameterToURL(redirectURL, SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	    return redirectURL;
	    
	} else {
	    request.setAttribute("surveyForm", surveyForm);
	    return "pages/learning/learning";
	}
    }

    @RequestMapping(value = "/nextQuestion")
    private String nextQuestion(@ModelAttribute("surveyForm") AnswerForm surveyForm, HttpServletRequest request) {
	Integer questionSeqID = surveyForm.getQuestionSeqID();
	String sessionMapID = surveyForm.getSessionMapID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	getAnswer(request, surveyItemMap.get(questionSeqID), errorMap);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/learning/learning";
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
	int currIdx = new ArrayList<>(surveyItemMap.keySet()).indexOf(questionSeqID) + 1;
	surveyForm.setCurrentIdx(currIdx);
	// failure tolerance
	if (questionSeqID.equals(surveyItemMap.lastKey())) {
	    surveyForm.setPosition(SurveyConstants.POSITION_LAST);
	} else {
	    surveyForm.setPosition(SurveyConstants.POSITION_INSIDE);
	}
	surveyForm.setQuestionSeqID(questionSeqID);
	request.setAttribute("surveyForm", surveyForm);
	return "pages/learning/learning";
    }

    @RequestMapping(value = "/previousQuestion")
    private String previousQuestion(@ModelAttribute("surveyForm") AnswerForm surveyForm, HttpServletRequest request) {
	Integer questionSeqID = surveyForm.getQuestionSeqID();
	String sessionMapID = surveyForm.getSessionMapID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	getAnswer(request, surveyItemMap.get(questionSeqID), errorMap);

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/learning/learning";
	}

	SortedMap<Integer, AnswerDTO> subMap = surveyItemMap.headMap(questionSeqID);
	if (subMap.isEmpty()) {
	    questionSeqID = surveyItemMap.firstKey();
	} else {
	    questionSeqID = subMap.lastKey();
	}

	// get current question index of total questions
	int currIdx = new ArrayList<>(surveyItemMap.keySet()).indexOf(questionSeqID) + 1;
	surveyForm.setCurrentIdx(currIdx);

	if (questionSeqID.equals(surveyItemMap.firstKey())) {
	    surveyForm.setPosition(SurveyConstants.POSITION_FIRST);
	} else {
	    surveyForm.setPosition(SurveyConstants.POSITION_INSIDE);
	}
	surveyForm.setQuestionSeqID(questionSeqID);
	request.setAttribute("surveyForm", surveyForm);
	return "pages/learning/learning";
    }

    @RequestMapping(value = "/retake")
    private String retake(AnswerForm surveyForm, HttpServletRequest request, HttpServletResponse response) {
	Integer questionSeqID = surveyForm.getQuestionSeqID();

	String sessionMapID = surveyForm.getSessionMapID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);
	Collection<AnswerDTO> surveyItemList = surveyItemMap.values();

	if (surveyItemList.size() < 2 || (questionSeqID != null && questionSeqID > 0)) {
	    surveyForm.setPosition(SurveyConstants.POSITION_ONLY_ONE);
	} else {
	    surveyForm.setPosition(SurveyConstants.POSITION_FIRST);
	}
	if (questionSeqID == null || questionSeqID <= 0) {
	    Boolean onePage = (Boolean) sessionMap.get(SurveyConstants.ATTR_SHOW_ON_ONE_PAGE);
	    if (!onePage && surveyItemList.size() > 0) {
		surveyForm.setQuestionSeqID(surveyItemMap.firstKey());
		questionSeqID = surveyItemMap.firstKey();
	    }
	}

	// get current question index of total questions
	int currIdx = new ArrayList<>(surveyItemMap.keySet()).indexOf(questionSeqID) + 1;
	surveyForm.setCurrentIdx(currIdx);
	request.setAttribute("surveyForm", surveyForm);

	return "pages/learning/learning";
    }

    @RequestMapping(value = "/showOtherUsersAnswers")
    private String showOtherUsersAnswers(HttpServletRequest request) {
	String sessionMapID = request.getParameter("sessionMapID");
	request.setAttribute("sessionMapID", sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	List<AnswerDTO> answerDtos = new ArrayList<>();
	for (SurveyQuestion question : surveyItemMap.values()) {
	    AnswerDTO answerDto = surveyService.getQuestionResponse(sessionId, question.getUid());
	    answerDtos.add(answerDto);
	}
	request.setAttribute("answerDtos", answerDtos);

	SurveyUser surveyLearner = (SurveyUser) sessionMap.get(SurveyConstants.ATTR_USER);
	surveyService.setResponseFinalized(surveyLearner.getUid());

	int countFinishedUser = surveyService.getCountFinishedUsers(sessionId);
	request.setAttribute(SurveyConstants.ATTR_COUNT_FINISHED_USERS, countFinishedUser);

	return "pages/learning/resultOtherUsers";
    }

    /**
     * Get OpenResponses.
     */

    @RequestMapping(value = "/getOpenResponses")
    private String getOpenResponses(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {

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

	List<String> responses = surveyService.getOpenResponsesForTablesorter(sessionId, questionUid, page, size,
		sorting);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();

	ObjectNode responcedata = JsonNodeFactory.instance.objectNode();
	responcedata.put("total_rows", surveyService.getCountResponsesBySessionAndQuestion(sessionId, questionUid));

	for (String response : responses) {
	    //ArrayNode cell=JsonNodeFactory.instance.arrayNode();
	    //cell.put(HtmlUtils.htmlEscape(user.getFirstName()) + " " + HtmlUtils.htmlEscape(user.getLastName()) + " [" + HtmlUtils.htmlEscape(user.getLogin()) + "]");

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put("answer", StringEscapeUtils.escapeCsv(response));
//	    responseRow.put("attemptTime", response.getAttemptTime());

	    rows.add(responseRow);
	}
	responcedata.set("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    @RequestMapping(value = "/doSurvey", method = RequestMethod.POST)
    private String doSurvey(@ModelAttribute("surveyForm") AnswerForm surveyForm, HttpServletRequest request) {

	Integer questionSeqID = surveyForm.getQuestionSeqID();
	String sessionMapID = surveyForm.getSessionMapID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// validate
	SortedMap<Integer, AnswerDTO> surveyItemMap = getQuestionList(sessionMap);
	Collection<AnswerDTO> surveyItemList = surveyItemMap.values();

	SurveyUser surveyLearner = (SurveyUser) sessionMap.get(SurveyConstants.ATTR_USER);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if ((questionSeqID == null) || questionSeqID.equals(0)) {
	    getAnswers(request, errorMap);
	} else {
	    getAnswer(request, surveyItemMap.get(questionSeqID), errorMap);
	}
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    return "pages/learning/learning";
	}

	List<SurveyAnswer> answerList = new ArrayList<>();
	for (AnswerDTO question : surveyItemList) {
	    if (question.getAnswer() != null) {
		question.getAnswer().setUser(surveyLearner);
		answerList.add(question.getAnswer());
	    }
	}

	surveyService.updateAnswerList(answerList);

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Survey survey = surveyService.getSurveyBySessionId(sessionId);
	if (survey.isNotifyTeachersOnAnswerSumbit()) {
	    surveyService.notifyTeachersOnAnswerSumbit(sessionId, surveyLearner);
	}

	request.setAttribute("errorMap", errorMap);
	return "pages/learning/result";
    }

    /**
     * Finish learning session.
     */
    @RequestMapping(value = "/finish")
    private void finish(HttpServletRequest request, HttpServletResponse response) throws IOException, SurveyApplicationException {

	// get back SessionMap
	String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// get sessionId from HttpServletRequest
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	String nextActivityUrl = surveyService.finishToolSession(sessionId, userID);
	response.sendRedirect(nextActivityUrl);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Get answer by special question.
     */
    private void getAnswer(HttpServletRequest request, AnswerDTO answerDto, MultiValueMap<String, String> errorMap) {
	// get sessionMap
	String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	SurveyAnswer answer = getAnswerFromPage(request, answerDto, sessionID);
	answerDto.setAnswer(answer);
	validateAnswers(request, answerDto, errorMap, answer);
    }

    /**
     * Get all answer for all questions in this page
     *
     * @param request
     * @return
     */
    private void getAnswers(HttpServletRequest request, MultiValueMap<String, String> errorMap) {
	// get sessionMap
	String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	Collection<AnswerDTO> answerDtoList = getQuestionList(sessionMap).values();

	for (AnswerDTO answerDto : answerDtoList) {
	    SurveyAnswer answer = getAnswerFromPage(request, answerDto, sessionID);
	    answerDto.setAnswer(answer);
	    validateAnswers(request, answerDto, errorMap, answer);
	}

    }

    private void validateAnswers(HttpServletRequest request, AnswerDTO question, MultiValueMap<String, String> errorMap,
	    SurveyAnswer answer) {
	boolean isAnswerEmpty = ((answer.getChoices() == null) && StringUtils.isBlank(answer.getAnswerText()));

	// for mandatory questions, answer can not be null.

	if (!question.isOptional() && isAnswerEmpty) {
	    errorMap.add(SurveyConstants.ERROR_MSG_KEY + question.getUid(),
		    messageService.getMessage(SurveyConstants.ERROR_MSG_MANDATORY_QUESTION));
	}
	if ((question.getType() == SurveyConstants.QUESTION_TYPE_SINGLE_CHOICE) && question.isAppendText()
		&& !isAnswerEmpty) {
	    // for single choice, user only can choose one option or open text (if it has)
	    if (!StringUtils.isBlank(answer.getAnswerChoices()) && !StringUtils.isBlank(answer.getAnswerText())) {
		errorMap.add(SurveyConstants.ERROR_MSG_KEY + question.getUid(),
			messageService.getMessage(SurveyConstants.ERROR_MSG_SINGLE_CHOICE));
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

	answer.setUser(getCurrentUser(surveyService, sessionID));
	answer.setUpdateDate(new Timestamp(new Date().getTime()));
	answer.setSurveyQuestion(question);
	return answer;
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
	    list = new TreeMap<>(new IntegerComparator());
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
