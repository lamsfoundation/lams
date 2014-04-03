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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McTempDataHolderDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.dao.DataIntegrityViolationException;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

/**
 * @author Ozgur Demirtas
 */
public class McLearningAction extends LamsDispatchAction implements McAppConstants {
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
	LearningUtil.saveFormRequestData(request, mcLearningForm, false);
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
	
	//setContentInUse
	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));
	mcContent.setContentInUse(true);
	mcService.saveMcContent(mcContent);
	
	LearningUtil.saveFormRequestData(request, mcLearningForm, false);

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
	    mcLearningForm.setLearnerProgress(new Boolean(false).toString());
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

	LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	// requested learner finished, the learner should be directed to next activity

	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null && user.getUserID() != null) {
		userID = user.getUserID().toString();
	    }
	}

	// attempting to leave/complete session with toolSessionId:

	McUtils.cleanUpSessionAbsolute(request);

	String nextUrl = null;
	try {
	    nextUrl = mcService.leaveToolSession(new Long(toolSessionID), new Long(userID));
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

	McQueUsr mcQueUsr = mcService.getMcUserBySession(new Long(userID), mcSession.getUid());
	mcQueUsr.setResponseFinalised(true);
	mcService.updateMcQueUsr(mcQueUsr);

	response.sendRedirect(nextUrl);

	return null;
    }
    
    /**
     * 
     */
    protected List<McLearnerAnswersDTO> buildSelectedQuestionAndCandidateAnswersDTO(List<String> learnerInput,
	    McTempDataHolderDTO mcTempDataHolderDTO, McContent content) {

	int learnerMark = 0;
	int totalMarksPossible = 0;

	List<McLearnerAnswersDTO> questionAndCandidateAnswersList = new LinkedList<McLearnerAnswersDTO>();

	for (McQueContent question : (Set<McQueContent>)content.getMcQueContents()) {
	    String questionUid = question.getUid().toString();
	    int currentMark = question.getMark().intValue();
	    totalMarksPossible += currentMark;

	    McLearnerAnswersDTO mcLearnerAnswersDTO = new McLearnerAnswersDTO();
	    mcLearnerAnswersDTO.setQuestion(question.getQuestion());
	    mcLearnerAnswersDTO.setDisplayOrder(question.getDisplayOrder().toString());
	    mcLearnerAnswersDTO.setQuestionUid(question.getUid());
	    mcLearnerAnswersDTO.setFeedback(question.getFeedback() != null ? question.getFeedback() : "");

	    //search for according answer
	    McOptsContent answerOption = null;
	    for (String input : learnerInput) {
		int pos = input.indexOf("-");
		String inputQuestionUid = input.substring(0, pos);

		if (questionUid.equals(inputQuestionUid)) {
		    String answerOptionUid = input.substring(pos + 1);
		    answerOption = question.getOptionsContentByUID(new Long(answerOptionUid));
		    mcLearnerAnswersDTO.setAnswerOption(answerOption);
		    break;
		}
	    }

	    boolean isCorrect = (answerOption != null) && answerOption.isCorrectOption();
	    mcLearnerAnswersDTO.setAttemptCorrect(new Boolean(isCorrect).toString());
	    if (isCorrect) {
		mcLearnerAnswersDTO.setFeedbackCorrect(question.getFeedback());
		mcLearnerAnswersDTO.setMark(new Integer(currentMark));
		learnerMark += currentMark;
	    } else {
		mcLearnerAnswersDTO.setFeedbackIncorrect(question.getFeedback());
		mcLearnerAnswersDTO.setMark(new Integer(0));
	    }

	    questionAndCandidateAnswersList.add(mcLearnerAnswersDTO);

	}

	mcTempDataHolderDTO.setLearnerMark(new Integer(learnerMark));
	mcTempDataHolderDTO.setTotalMarksPossible(new Integer(totalMarksPossible));

	return questionAndCandidateAnswersList;
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

	String questionListingMode = mcLearningForm.getQuestionListingMode();

	List<String> learnerInput = McLearningAction.parseLearnerAnswers(mcLearningForm, request);
	
	if (questionListingMode.equals(McAppConstants.QUESTION_LISTING_MODE_SEQUENTIAL)) {
	    sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, learnerInput);
	}
	
	mcLearningForm.resetCa(mapping, request);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));
	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	/* process the answers */
	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

	McTempDataHolderDTO mcTempDataHolderDTO = new McTempDataHolderDTO();
	List<McLearnerAnswersDTO> selectedQuestionAndCandidateAnswersDTO = buildSelectedQuestionAndCandidateAnswersDTO(
		learnerInput, mcTempDataHolderDTO, mcContent);
	request.setAttribute(McAppConstants.LIST_SELECTED_QUESTION_CANDIDATEANSWERS_DTO,
		selectedQuestionAndCandidateAnswersDTO);

	mcGeneralLearnerFlowDTO.setQuestionListingMode(McAppConstants.QUESTION_LISTING_MODE_COMBINED);

	Integer learnerMark = mcTempDataHolderDTO.getLearnerMark();
	mcGeneralLearnerFlowDTO.setLearnerMark(learnerMark);
	int totalQuestionCount = mcContent.getMcQueContents().size();
	mcGeneralLearnerFlowDTO.setTotalQuestionCount(new Integer(totalQuestionCount));
	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcTempDataHolderDTO.getTotalMarksPossible());

	request.getSession().setAttribute(httpSessionID, sessionMap);
	
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	String userID = user.getUserID().toString();
	McQueUsr mcQueUsr = mcService.getMcUserBySession(new Long(userID), mcSession.getUid());

	// Have to work out in advance if passed so that we can store it against the attempts
	boolean passed = mcQueUsr.isMarkPassed(learnerMark);
	mcGeneralLearnerFlowDTO.setUserOverPassMark(new Boolean(passed).toString());
	mcGeneralLearnerFlowDTO.setPassMarkApplicable(new Boolean(mcContent.getPassMark() != null).toString());

	McLearningAction.saveUserAttempt(mcQueUsr, selectedQuestionAndCandidateAnswersDTO);

	Integer numberOfAttempts = mcQueUsr.getNumberOfAttempts() + 1;
	mcQueUsr.setNumberOfAttempts(numberOfAttempts);
	mcQueUsr.setLastAttemptTotalMark(learnerMark);
	mcService.updateMcQueUsr(mcQueUsr);

	mcGeneralLearnerFlowDTO.setDisplayAnswers(new Boolean(mcContent.isDisplayAnswers()).toString());

	mcGeneralLearnerFlowDTO.setShowMarks(new Boolean(mcContent.isShowMarks()).toString());
	if (mcContent.isShowMarks()) {
	    Integer[] markStatistics = mcService.getMarkStatistics(mcSession);
	    mcGeneralLearnerFlowDTO.setTopMark(markStatistics[0]);
	    mcGeneralLearnerFlowDTO.setLowestMark(markStatistics[1]);
	    mcGeneralLearnerFlowDTO.setAverageMark(markStatistics[2]);
	} else {
	    mcGeneralLearnerFlowDTO.setTopMark(0);
	    mcGeneralLearnerFlowDTO.setLowestMark(0);
	    mcGeneralLearnerFlowDTO.setAverageMark(0);
	}

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());

	//String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());

	mcGeneralLearnerFlowDTO.setLatestAttemptMark(mcQueUsr.getLastAttemptTotalMark());

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	return mapping.findForward(McAppConstants.INDIVIDUAL_REPORT);
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
	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));
	
	HttpSession ss = SessionManager.getSession();
	UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	String userID = userDto.getUserID().toString();
	McQueUsr user = mcService.getMcUserBySession(new Long(userID), mcSession.getUid());

	String httpSessionID = mcLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	
	//parse learner input
	List<String> learnerInput = McLearningAction.parseLearnerAnswers(mcLearningForm, request);
	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, learnerInput);
	
	//save user attempt
	List<McLearnerAnswersDTO> selectedQuestionAndCandidateAnswersDTO = buildSelectedQuestionAndCandidateAnswersDTO(
		learnerInput, new McTempDataHolderDTO(), mcContent);
	McLearningAction.saveUserAttempt(user, selectedQuestionAndCandidateAnswersDTO);

	McQueUsr mcQueUsr = getCurrentUser(toolSessionID);
	List<McLearnerAnswersDTO> learnerAnswersDTOList = mcService.buildLearnerAnswersDTOList(mcContent, mcQueUsr);
	request.setAttribute(McAppConstants.LEARNER_ANSWERS_DTO_LIST, learnerAnswersDTOList);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

	Integer totalQuestionCount = mcGeneralLearnerFlowDTO.getTotalQuestionCount();
	String questionIndex = mcLearningForm.getQuestionIndex();
	Integer intQuestionIndex = new Integer(questionIndex);
	if (totalQuestionCount.equals(intQuestionIndex)) {
	    mcGeneralLearnerFlowDTO.setTotalCountReached(new Boolean(true).toString());
	}

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());

	// String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());

	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());

	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcContent.getTotalMarksPossible());

	mcGeneralLearnerFlowDTO.setQuestionIndex(new Integer(questionIndex));
	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request, getServlet()
		.getServletContext());
	
	request.setAttribute("sessionMapID", sessionMap.getSessionID());

	return mapping.findForward(McAppConstants.LOAD_LEARNER);
    }

    /**
     *  prepareViewAnswersData
     */
    public void prepareViewAnswersData(ActionMapping mapping, McLearningForm mcLearningForm,
	    HttpServletRequest request, ServletContext servletContext) {

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
	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

	Map mapQuestionsUidContent = AuthoringUtil.rebuildQuestionUidMapfromDB(request, new Long(toolContentId),
		mcService);

	Map mapStartupGeneralOptionsContent = AuthoringUtil.rebuildStartupGeneralOptionsContentMapfromDB(request,
		mapQuestionsUidContent, mcService);
	mcGeneralLearnerFlowDTO.setMapGeneralOptionsContent(mapStartupGeneralOptionsContent);

	Map mapQuestionsContent = AuthoringUtil.rebuildQuestionMapfromDB(request, new Long(toolContentId), mcService);
	mcGeneralLearnerFlowDTO.setMapQuestionsContent(mapQuestionsContent);

	Map mapFeedbackContent = AuthoringUtil.rebuildFeedbackMapfromDB(request, new Long(toolContentId), mcService);
	mcGeneralLearnerFlowDTO.setMapFeedbackContent(mapFeedbackContent);

	// Set up the user details. If this is the learner progress screen then we that id,
	// otherwise we use the user from the session.

	String learnerProgress = mcLearningForm.getLearnerProgress();
	String learnerProgressUserId = mcLearningForm.getLearnerProgressUserId();
	mcGeneralLearnerFlowDTO.setLearnerProgressUserId(learnerProgressUserId);
	mcGeneralLearnerFlowDTO.setLearnerProgress(learnerProgress);

	Boolean learnerProgressOn = Boolean.parseBoolean(learnerProgress);

	McQueUsr mcQueUsr = null;
	if (learnerProgressOn.equals(Boolean.FALSE)) {
	    mcQueUsr = getCurrentUser(toolSessionID);
	} else {
	    mcQueUsr = mcService.getMcUserBySession(new Long(learnerProgressUserId), mcSession.getUid());
	}

	Long toolContentUID = mcContent.getUid();

	//create attemptMap for displaying on jsp
	Map<String, McUsrAttempt> attemptMap = new TreeMap<String, McUsrAttempt>(new McComparator());
	for (int i = 1; i <= mcContent.getMcQueContents().size(); i++) {
	    McQueContent question = mcService.getQuestionByDisplayOrder(new Long(i), toolContentUID);

	    McUsrAttempt userAttempt = mcService.getUserAttemptByQuestion(mcQueUsr.getUid(),
		    question.getUid());

	    if (userAttempt != null) {
		attemptMap.put(new Integer(i).toString(), userAttempt);
	    }
	}
	mcGeneralLearnerFlowDTO.setAttemptMap(attemptMap);

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());
	// String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(mcContent.getReflectionSubject());

	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.MY_SIGNATURE, new Integer(mcQueUsr.getQueUsrId().intValue()));
	if (notebookEntry != null) {
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntry.getEntry());
	}

	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());
	mcGeneralLearnerFlowDTO.setPassMarkApplicable(new Boolean(mcContent.isPassMarkApplicable()).toString());
	mcGeneralLearnerFlowDTO.setUserOverPassMark(new Boolean(mcQueUsr.isLastAttemptMarkPassed()).toString());
	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcContent.getTotalMarksPossible());
	mcGeneralLearnerFlowDTO.setShowMarks(new Boolean(mcContent.isShowMarks()).toString());
	mcGeneralLearnerFlowDTO.setDisplayAnswers(new Boolean(mcContent.isDisplayAnswers()).toString());
	if (mcContent.isShowMarks()) {
	    Integer[] markStatistics = mcService.getMarkStatistics(mcSession);
	    mcGeneralLearnerFlowDTO.setTopMark(markStatistics[0]);
	    mcGeneralLearnerFlowDTO.setLowestMark(markStatistics[1]);
	    mcGeneralLearnerFlowDTO.setAverageMark(markStatistics[2]);
	} else {
	    Integer zero = new Integer(0);
	    mcGeneralLearnerFlowDTO.setTopMark(zero);
	    mcGeneralLearnerFlowDTO.setLowestMark(zero);
	    mcGeneralLearnerFlowDTO.setAverageMark(zero);
	}

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request, servletContext);
    }

    /**
     * allows the learner to view their answer history
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward viewAnswers(ActionMapping mapping, McLearningForm mcLearningForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	prepareViewAnswersData(mapping, mcLearningForm, request, getServlet().getServletContext());
	return mapping.findForward(McAppConstants.VIEW_ANSWERS);
    }

    /**
     * 
     */
    public ActionForward redoQuestions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping) {
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}
	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession mcSession = mcService.getMcSessionById(new Long(toolSessionID));
	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));
	McQueUsr mcQueUsr = getCurrentUser(toolSessionID);
	
	//clear sessionMap
	String httpSessionID = mcLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	List<String> sequentialCheckedCa = new LinkedList<String>();
	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, sequentialCheckedCa);
	
	List<McLearnerAnswersDTO> learnerAnswersDTOList = mcService.buildLearnerAnswersDTOList(mcContent, mcQueUsr);
	request.setAttribute(McAppConstants.LEARNER_ANSWERS_DTO_LIST, learnerAnswersDTOList);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
	mcGeneralLearnerFlowDTO.setQuestionIndex(new Integer(1));

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());

	//String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
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
	    HttpServletResponse response) throws JSONException, IOException {
	
	if (mcService == null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	}
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession session = mcService.getMcSessionById(toolSessionId);
	McQueUsr leader = session.getGroupLeader();
	
	boolean isLeaderResponseFinalized = leader.isResponseFinalised();
	
	JSONObject JSONObject = new JSONObject();
	JSONObject.put("isLeaderResponseFinalized", isLeaderResponseFinalized);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }

    /**
     * submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
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

	String userID = request.getParameter("userID");
	mcLearningForm.setUserID(userID);

	String reflectionEntry = request.getParameter(McAppConstants.ENTRY_TEXT);
	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.MY_SIGNATURE, new Integer(userID));

	if (notebookEntry != null) {
	    notebookEntry.setEntry(reflectionEntry);
	    mcService.updateEntry(notebookEntry);
	} else {
	    mcService.createNotebookEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		    McAppConstants.MY_SIGNATURE, new Integer(userID), reflectionEntry);
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

	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null && user.getUserID() != null) {
		userID = user.getUserID().toString();
	    }
	}

	// attempt getting notebookEntry
	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.MY_SIGNATURE, new Integer(userID));

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
	
	HttpSession ss = SessionManager.getSession();
	UserDTO userDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	String userID = userDto.getUserID().toString();
	McQueUsr user = mcService.getMcUserBySession(new Long(userID), mcSession.getUid());

	List<String> learnerInput = McLearningAction.parseLearnerAnswers(mcLearningForm, request);
	
	List<McLearnerAnswersDTO> selectedQuestionAndCandidateAnswersDTO = buildSelectedQuestionAndCandidateAnswersDTO(
		learnerInput, new McTempDataHolderDTO(), mcContent);
	McLearningAction.saveUserAttempt(user, selectedQuestionAndCandidateAnswersDTO);
	
	return null;
    }
    
    /**
     * Makes a call to mcService.saveUserAttempt(). This method is designed purely for exception handling purposes. It
     * needs to be performed inside Action class as otherwise Hibernate tries to flush the session which leads to another exception.
     */
    private static void saveUserAttempt(McQueUsr user, List<McLearnerAnswersDTO> selectedQuestionAndCandidateAnswersDTO) {
	try {
	    mcService.saveUserAttempt(user, selectedQuestionAndCandidateAnswersDTO);

	} catch (DataIntegrityViolationException e) {

	    // log DB exceptions occurred due to creating non-unique McUsrAttempt. And propagate all the other exceptions
	    if (e.getRootCause() instanceof MySQLIntegrityConstraintViolationException) {
		String rootCauseMessage = e.getRootCause().getMessage();
		
		Pattern pattern = Pattern.compile("Duplicate entry.*attempt_unique_index");
		if ((rootCauseMessage != null) && pattern.matcher(rootCauseMessage).find()) {
		    logger.error("Prevented creation of McUsrAttempt which was not unique for user and question: " + rootCauseMessage);
		    return;
		}
	    }
	    
	    throw e;
	}
    }
    
    private static List<String> parseLearnerAnswers(McLearningForm mcLearningForm, HttpServletRequest request) {
	String httpSessionID = mcLearningForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	
	String questionListingMode = mcLearningForm.getQuestionListingMode();
	
	List<String> learnerInput = new LinkedList<String>();
	if (questionListingMode.equals(McAppConstants.QUESTION_LISTING_MODE_SEQUENTIAL)) {

	    List<String> previousLearnerInput = (List<String>) sessionMap.get(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY);
	    learnerInput.addAll(previousLearnerInput);

	    /* checkedCa refers to candidate answers */
	    String[] checkedCa = mcLearningForm.getCheckedCa();

	    if (checkedCa != null) {
		for (int i = 0; i < checkedCa.length; i++) {
		    String currentCa = checkedCa[i];
		    learnerInput.add(currentCa);
		}
	    }

	} else {
	    Map parameters = request.getParameterMap();
	    Iterator<String> iter = parameters.keySet().iterator();
	    while (iter.hasNext()) {
		String key = (String) iter.next();
		if (key.startsWith("checkedCa")) {
		    String currentCheckedCa = request.getParameter(key);
		    if (currentCheckedCa != null) {
			learnerInput.add(currentCheckedCa);
		    }
		}
	    }
	}
	
	return learnerInput;
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
