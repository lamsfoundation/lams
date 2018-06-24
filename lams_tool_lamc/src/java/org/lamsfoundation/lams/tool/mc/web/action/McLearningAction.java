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

package org.lamsfoundation.lams.tool.mc.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.mc.dto.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.tool.mc.util.LearningUtil;
import org.lamsfoundation.lams.tool.mc.util.McComparator;
import org.lamsfoundation.lams.tool.mc.web.form.McLearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ozgur Demirtas
 */
public class McLearningAction extends LamsDispatchAction {
    private static Logger logger = Logger.getLogger(McLearningAction.class.getName());

    private static IMcService mcService;

    /**
     * main content/question content management and workflow logic
     *
     * if the passed toolContentId exists in the db, we need to get the relevant data into the Map if not, create the
     * default Map
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;
	LearningUtil.saveFormRequestData(request, mcLearningForm);
	return null;
    }

    /**
     * responds to learner activity in learner mode.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward displayMc(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(toolSessionID);

	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	mcLearningForm.setToolContentID(toolContentId);

	LearningUtil.saveFormRequestData(request, mcLearningForm);

	if (mcLearningForm.getNextQuestionSelected() != null && !mcLearningForm.getNextQuestionSelected().equals("")) {
	    mcLearningForm.resetParameters();
	    return getNextOptions(mapping, form, request, response);
	}
	if (mcLearningForm.getContinueOptionsCombined() != null) {
	    return continueOptionsCombined(mapping, form, request, response);
	} else if (mcLearningForm.getNextOptions() != null) {
	    return getNextOptions(mapping, form, request, response);
	} else if (mcLearningForm.getRedoQuestions() != null) {
	    return redoQuestions(request, mcLearningForm, mapping);
	} else if (mcLearningForm.getViewAnswers() != null) {
	    return viewAnswers(mapping, mcLearningForm, request, response);
	} else if (mcLearningForm.getSubmitReflection() != null) {
	    return submitReflection(mapping, form, request, response);
	} else if (mcLearningForm.getForwardtoReflection() != null) {
	    return forwardtoReflection(mapping, form, request, response);
	} else if (mcLearningForm.getLearnerFinished() != null) {
	    return endLearning(mapping, form, request, response);
	}

	return mapping.findForward(McAppConstants.LOAD_LEARNER);
    }

    /**
     * ActionForward endLearning(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     *
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward endLearning(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(toolSessionID);

	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	mcLearningForm.setToolContentID(toolContentId);

	LearningUtil.saveFormRequestData(request, mcLearningForm);
	// requested learner finished, the learner should be directed to next activity

	HttpSession ss = SessionManager.getSession();
	UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);

	String nextUrl = null;
	try {
	    nextUrl = mcService.leaveToolSession(new Long(toolSessionID), userDto.getUserID().longValue());
	} catch (DataMissingException e) {
	    McLearningAction.logger.error("failure getting nextUrl: " + e);
	    return mapping.findForward(McAppConstants.LEARNING_STARTER);
	} catch (ToolException e) {
	    McLearningAction.logger.error("failure getting nextUrl: " + e);
	    return mapping.findForward(McAppConstants.LEARNING_STARTER);
	} catch (Exception e) {
	    McLearningAction.logger.error("unknown exception getting nextUrl: " + e);
	    return mapping.findForward(McAppConstants.LEARNING_STARTER);
	}

	response.sendRedirect(nextUrl);

	return null;
    }

    /**
     *
     */
    protected List<AnswerDTO> buildAnswerDtos(List<String> answers, McContent content, HttpServletRequest request) {

	List<AnswerDTO> answerDtos = new LinkedList<AnswerDTO>();

	for (McQueContent question : (Set<McQueContent>) content.getMcQueContents()) {
	    String questionUid = question.getUid().toString();
	    int questionMark = question.getMark().intValue();

	    AnswerDTO answerDto = new AnswerDTO();
	    answerDto.setQuestion(question.getQuestion());
	    answerDto.setDisplayOrder(question.getDisplayOrder().toString());
	    answerDto.setQuestionUid(question.getUid());
	    answerDto.setFeedback(question.getFeedback() != null ? question.getFeedback() : "");

	    //search for according answer
	    McOptsContent answerOption = null;
	    for (String answer : answers) {
		int hyphenPosition = answer.indexOf("-");
		String answeredQuestionUid = answer.substring(0, hyphenPosition);

		if (questionUid.equals(answeredQuestionUid)) {
		    String answeredOptionUid = answer.substring(hyphenPosition + 1);
		    answerOption = question.getOptionsContentByUID(new Long(answeredOptionUid));
		    answerDto.setAnswerOption(answerOption);
		    break;
		}
	    }

	    boolean isCorrect = (answerOption != null) && answerOption.isCorrectOption();
	    answerDto.setAttemptCorrect(isCorrect);
	    if (isCorrect) {
		answerDto.setFeedbackCorrect(question.getFeedback());
		answerDto.setMark(questionMark);
	    } else {
		answerDto.setFeedbackIncorrect(question.getFeedback());
		answerDto.setMark(0);
	    }
	    
	    //handle confidence levels
	    if (content.isEnableConfidenceLevels()) {
		int confidenceLevel = WebUtil.readIntParam(request, "confidenceLevel" + question.getUid());
		answerDto.setConfidenceLevel(confidenceLevel);
	    }

	    answerDtos.add(answerDto);
	}

	return answerDtos;
    }

    /**
     * responses to learner when they answer all the questions on a single page
     */
    public ActionForward continueOptionsCombined(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}

	String httpSessionID = mcLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession session = mcService.getMcSessionById(new Long(toolSessionID));
	String toolContentId = session.getMcContent().getMcContentId().toString();
	McContent mcContent = mcService.getMcContent(new Long(toolContentId));

	List<String> answers = McLearningAction.parseLearnerAnswers(mcLearningForm, request,
		mcContent.isQuestionsSequenced());
	if (mcContent.isQuestionsSequenced()) {
	    sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, answers);
	}

	mcLearningForm.resetCa(mapping, request);

	McQueUsr user = getCurrentUser(toolSessionID);

	//prohibit users from submitting answers after response is finalized but Resubmit button is not pressed (e.g. using 2 browsers)
	if (user.isResponseFinalised()) {
	    return viewAnswers(mapping, mcLearningForm, request, response);
	}

	/* process the answers */
	List<AnswerDTO> answerDtos = buildAnswerDtos(answers, mcContent, request);
	mcService.saveUserAttempt(user, answerDtos);

	//calculate total learner mark
	int learnerMark = 0;
	for (AnswerDTO answerDto : answerDtos) {
	    learnerMark += answerDto.getMark();
	}

	Integer numberOfAttempts = user.getNumberOfAttempts() + 1;
	user.setNumberOfAttempts(numberOfAttempts);
	user.setLastAttemptTotalMark(learnerMark);
	user.setResponseFinalised(true);
	mcService.updateMcQueUsr(user);

	return viewAnswers(mapping, mcLearningForm, request, response);
    }

    /**
     * takes the learner to the next set of questions
     *
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward getNextOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));
	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	McContent mcContent = mcService.getMcContent(new Long(toolContentId));

	McQueUsr user = getCurrentUser(toolSessionID);

	String httpSessionID = mcLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	//prohibit users from submitting answers after response is finalized but Resubmit button is not pressed (e.g. using 2 browsers)
	if (user.isResponseFinalised()) {
	    return viewAnswers(mapping, mcLearningForm, request, response);
	}

	//parse learner input
	List<String> answers = McLearningAction.parseLearnerAnswers(mcLearningForm, request,
		mcContent.isQuestionsSequenced());
	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, answers);

	//save user attempt
	List<AnswerDTO> answerDtos = buildAnswerDtos(answers, mcContent, request);
	mcService.saveUserAttempt(user, answerDtos);

	List<AnswerDTO> learnerAnswersDTOList = mcService.getAnswersFromDatabase(mcContent, user);
	request.setAttribute(McAppConstants.LEARNER_ANSWERS_DTO_LIST, learnerAnswersDTOList);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

	Integer totalQuestionCount = mcGeneralLearnerFlowDTO.getTotalQuestionCount();
	Integer questionIndex = mcLearningForm.getQuestionIndex();
	if (totalQuestionCount.equals(questionIndex)) {
	    mcGeneralLearnerFlowDTO.setTotalCountReached(new Boolean(true).toString());
	}

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());
	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcContent.getTotalMarksPossible());
	mcGeneralLearnerFlowDTO.setQuestionIndex(questionIndex);
	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request,
		getServlet().getServletContext());

	request.setAttribute("sessionMapID", sessionMap.getSessionID());

	return mapping.findForward(McAppConstants.LOAD_LEARNER);
    }

    /**
     * allows the learner to view their answer history
     *
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward viewAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;

	// may have to get service from the form - if class has been created by starter action, rather than by struts
	if (mcService == null) {
	    if (getServlet() != null) {
		mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	    } else {
		mcService = mcLearningForm.getMcService();
	    }
	}

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	McContent mcContent = mcService.getMcContent(new Long(toolContentId));

	String sessionMapID = mcLearningForm.getHttpSessionID();
	request.setAttribute("sessionMapID", sessionMapID);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

	Map mapQuestionsUidContent = new TreeMap(new McComparator());
	if (mcContent != null) {
	    List list = mcService.refreshQuestionContent(mcContent.getUid());

	    Iterator listIterator = list.iterator();
	    Long mapIndex = new Long(1);
	    while (listIterator.hasNext()) {
		McQueContent mcQueContent = (McQueContent) listIterator.next();
		mapQuestionsUidContent.put(mapIndex.toString(), mcQueContent.getUid());
		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	//builds a map to hold all the candidate answers for all the questions by accessing the db
	Map mapStartupGeneralOptionsContent = new TreeMap(new McComparator());
	Iterator itMap = mapQuestionsUidContent.entrySet().iterator();
	Long mapIndex = new Long(1);
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    String currentQuestionUid = pairs.getValue().toString();
	    List listQuestionOptions = mcService.findOptionsByQuestionUid(new Long(currentQuestionUid));

	    //builds a questions map from questions list
	    Map<String, String> mapOptsContent = new TreeMap<String, String>();
	    Iterator<McOptsContent> iter = listQuestionOptions.iterator();
	    int mapIndex2 = 0;
	    while (iter.hasNext()) {
		McOptsContent option = iter.next();
		String stringIndex = mcContent.isPrefixAnswersWithLetters() ? option.formatPrefixLetter(mapIndex2++) : Integer.toString(++mapIndex2);
		mapOptsContent.put(stringIndex, option.getMcQueOptionText());
	    }

	    mapStartupGeneralOptionsContent.put(mapIndex.toString(), mapOptsContent);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	mcGeneralLearnerFlowDTO.setMapGeneralOptionsContent(mapStartupGeneralOptionsContent);

	//builds a map to hold question texts
	Map mapQuestionsContent = new TreeMap(new McComparator());
	List list = mcService.refreshQuestionContent(mcContent.getUid());
	Iterator iter = list.iterator();
	Long mapIndex3 = new Long(1);
	while (iter.hasNext()) {
	    McQueContent question = (McQueContent) iter.next();
	    mapQuestionsContent.put(mapIndex3.toString(), question.getQuestion());
	    mapIndex3 = new Long(mapIndex3.longValue() + 1);
	}
	mcGeneralLearnerFlowDTO.setMapQuestionsContent(mapQuestionsContent);

	//rebuildFeedbackMapfromDB
	Map mapFeedbackContent = new TreeMap(new McComparator());
	List list2 = mcService.refreshQuestionContent(mcContent.getUid());
	Iterator iter2 = list2.iterator();
	Long mapIndex4 = new Long(1);
	while (iter2.hasNext()) {
	    McQueContent question = (McQueContent) iter2.next();

	    String feedback = question.getFeedback();

	    mapFeedbackContent.put(mapIndex4.toString(), feedback);
	    mapIndex4 = new Long(mapIndex4.longValue() + 1);
	}
	mcGeneralLearnerFlowDTO.setMapFeedbackContent(mapFeedbackContent);

	McQueUsr user = getCurrentUser(toolSessionID);

	Long toolContentUID = mcContent.getUid();

	//create attemptMap for displaying on jsp
	Map<String, McUsrAttempt> attemptMap = new TreeMap<String, McUsrAttempt>(new McComparator());
	for (int i = 1; i <= mcContent.getMcQueContents().size(); i++) {
	    McQueContent question = mcService.getQuestionByDisplayOrder(new Long(i), toolContentUID);

	    McUsrAttempt userAttempt = mcService.getUserAttemptByQuestion(user.getUid(), question.getUid());

	    if (userAttempt != null) {
		attemptMap.put(new Integer(i).toString(), userAttempt);
	    }
	}
	mcGeneralLearnerFlowDTO.setAttemptMap(attemptMap);
	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());

	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.TOOL_SIGNATURE, new Integer(user.getQueUsrId().intValue()));
	request.setAttribute("notebookEntry", notebookEntry);
	if (notebookEntry != null) {
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntry.getEntry());
	}

	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());
	mcGeneralLearnerFlowDTO.setPassMarkApplicable(new Boolean(mcContent.isPassMarkApplicable()).toString());
	mcGeneralLearnerFlowDTO.setUserOverPassMark(new Boolean(user.isLastAttemptMarkPassed()).toString());
	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcContent.getTotalMarksPossible());
	mcGeneralLearnerFlowDTO.setShowMarks(new Boolean(mcContent.isShowMarks()).toString());
	mcGeneralLearnerFlowDTO.setDisplayAnswers(new Boolean(mcContent.isDisplayAnswers()).toString());
	mcGeneralLearnerFlowDTO.setDisplayFeedbackOnly(((Boolean)mcContent.isDisplayFeedbackOnly()).toString());	
	mcGeneralLearnerFlowDTO.setLearnerMark(user.getLastAttemptTotalMark());

	Object[] markStatistics = null;
	if (mcContent.isShowMarks()) {
	    markStatistics = mcService.getMarkStatistics(mcSession);
	}
	if (markStatistics != null) {
	    mcGeneralLearnerFlowDTO
		    .setLowestMark(markStatistics[0] != null ? ((Float) markStatistics[0]).intValue() : 0);
	    mcGeneralLearnerFlowDTO
		    .setAverageMark(markStatistics[1] != null ? ((Float) markStatistics[1]).intValue() : 0);
	    mcGeneralLearnerFlowDTO.setTopMark(markStatistics[2] != null ? ((Float) markStatistics[2]).intValue() : 0);
	} else {
	    mcGeneralLearnerFlowDTO.setLowestMark(0);
	    mcGeneralLearnerFlowDTO.setAverageMark(0);
	    mcGeneralLearnerFlowDTO.setTopMark(0);
	}

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request,
		getServlet().getServletContext());

	return mapping.findForward(McAppConstants.VIEW_ANSWERS);
    }

    /**
     *
     */
    public ActionForward redoQuestions(HttpServletRequest request, McLearningForm mcLearningForm,
	    ActionMapping mapping) {
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));
	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	McContent mcContent = mcService.getMcContent(new Long(toolContentId));
	McQueUsr mcQueUsr = getCurrentUser(toolSessionID);

	//in order to track whether redo button is pressed store this info
	mcQueUsr.setResponseFinalised(false);
	mcService.updateMcQueUsr(mcQueUsr);

	//clear sessionMap
	String httpSessionID = mcLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	List<String> sequentialCheckedCa = new LinkedList<String>();
	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, sequentialCheckedCa);

	List<AnswerDTO> learnerAnswersDTOList = mcService.getAnswersFromDatabase(mcContent, mcQueUsr);
	request.setAttribute(McAppConstants.LEARNER_ANSWERS_DTO_LIST, learnerAnswersDTOList);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
	mcGeneralLearnerFlowDTO.setQuestionIndex(new Integer(1));
	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());

	String passMarkApplicable = new Boolean(mcContent.isPassMarkApplicable()).toString();
	mcGeneralLearnerFlowDTO.setPassMarkApplicable(passMarkApplicable);
	mcLearningForm.setPassMarkApplicable(passMarkApplicable);

	String userOverPassMark = Boolean.FALSE.toString();
	mcGeneralLearnerFlowDTO.setUserOverPassMark(userOverPassMark);
	mcLearningForm.setUserOverPassMark(userOverPassMark);

	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcContent.getTotalMarksPossible());

	// should we show the marks for each question - we show the marks if any of the questions
	// have a mark > 1.
	Boolean showMarks = LearningUtil.isShowMarksOnQuestion(learnerAnswersDTOList);
	mcGeneralLearnerFlowDTO.setShowMarks(showMarks.toString());

	request.setAttribute("sessionMapID", mcLearningForm.getHttpSessionID());

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
	return mapping.findForward(McAppConstants.LOAD_LEARNER);
    }

    /**
     * checks Leader Progress
     */
    public ActionForward checkLeaderProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession session = mcService.getMcSessionById(toolSessionId);
	McQueUsr leader = session.getGroupLeader();

	boolean isLeaderResponseFinalized = leader.isResponseFinalised();

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();
	ObjectNode.put("isLeaderResponseFinalized", isLeaderResponseFinalized);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(ObjectNode);
	return null;
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
	McLearningForm mcLearningForm = (McLearningForm) form;

	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(toolSessionID);

	Long userID = mcLearningForm.getUserID();

	String reflectionEntry = request.getParameter(McAppConstants.ENTRY_TEXT);
	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.TOOL_SIGNATURE, userID.intValue());

	if (notebookEntry != null) {
	    notebookEntry.setEntry(reflectionEntry);
	    notebookEntry.setLastModified(new Date());
	    mcService.updateEntry(notebookEntry);
	} else {
	    mcService.createNotebookEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		    McAppConstants.TOOL_SIGNATURE, userID.intValue(), reflectionEntry);
	}

	return endLearning(mapping, form, request, response);
    }

    /**
     *
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
	McLearningForm mcLearningForm = (McLearningForm) form;
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));

	McContent mcContent = mcSession.getMcContent();

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = new McGeneralLearnerFlowDTO();
	mcGeneralLearnerFlowDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());

	Long userID = mcLearningForm.getUserID();

	// attempt getting notebookEntry
	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.TOOL_SIGNATURE, userID.intValue());

	if (notebookEntry != null) {
	    String notebookEntryPresentable = notebookEntry.getEntry();
	    mcLearningForm.setEntryText(notebookEntryPresentable);

	}

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	return mapping.findForward(McAppConstants.NOTEBOOK);
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
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}
	McLearningForm mcLearningForm = (McLearningForm) form;
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));
	McContent mcContent = mcSession.getMcContent();

	Long userID = mcLearningForm.getUserID();
	McQueUsr user = mcService.getMcUserBySession(userID, mcSession.getUid());

	//prohibit users from autosaving answers after response is finalized but Resubmit button is not pressed (e.g. using 2 browsers)
	if (user.isResponseFinalised()) {
	    return null;
	}

	List<String> answers = McLearningAction.parseLearnerAnswers(mcLearningForm, request,
		mcContent.isQuestionsSequenced());

	List<AnswerDTO> answerDtos = buildAnswerDtos(answers, mcContent, request);
	mcService.saveUserAttempt(user, answerDtos);

	return null;
    }

    private static List<String> parseLearnerAnswers(McLearningForm mcLearningForm, HttpServletRequest request,
	    boolean isQuestionsSequenced) {
	String httpSessionID = mcLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	List<String> answers = new LinkedList<String>();
	if (isQuestionsSequenced) {

	    List<String> previousAnswers = (List<String>) sessionMap
		    .get(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY);
	    answers.addAll(previousAnswers);

	    /* checkedCa refers to candidate answers */
	    String[] checkedCa = mcLearningForm.getCheckedCa();

	    if (checkedCa != null) {
		for (int i = 0; i < checkedCa.length; i++) {
		    String currentCa = checkedCa[i];
		    answers.add(currentCa);
		}
	    }

	} else {
	    Map parameters = request.getParameterMap();
	    Iterator<String> iter = parameters.keySet().iterator();
	    while (iter.hasNext()) {
		String key = iter.next();
		if (key.startsWith("checkedCa")) {
		    String currentCheckedCa = request.getParameter(key);
		    if (currentCheckedCa != null) {
			answers.add(currentCheckedCa);
		    }
		}
	    }
	}

	return answers;
    }

    private McQueUsr getCurrentUser(String toolSessionId) {
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionId));

	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userId = new Long(toolUser.getUserID().longValue());

	return mcService.getMcUserBySession(userId, mcSession.getUid());
    }

}
