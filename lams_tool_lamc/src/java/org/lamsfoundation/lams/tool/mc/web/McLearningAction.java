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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McLearnerAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
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
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * *
 * 
 * @author Ozgur Demirtas
 * 
 * 
 *         <!--Learning Main Action: interacts with the Learning module user --> <action path="/learning"
 *         type="org.lamsfoundation.lams.tool.mc.web.McLearningAction" name="McLearningForm" scope="request"
 *         input="/learning/AnswersContent.jsp" parameter="method" u7nknown="false" validate="false"> <forward
 *         name="loadLearner" path="/learning/AnswersContent.jsp" redirect="false" />
 * 
 *         <forward name="individualReport" path="/learning/IndividualLearnerResults.jsp" redirect="false" />
 * 
 *         <forward name="redoQuestions" path="/learning/RedoQuestions.jsp" redirect="false" />
 * 
 *         <forward name="viewAnswers" path="/learning/ViewAnswers.jsp" redirect="false" />
 * 
 *         <forward name="errorList" path="/McErrorBox.jsp" redirect="false" />
 * 
 *         <forward name="starter" path="/index.jsp" redirect="false" />
 * 
 *         <forward name="learningStarter" path="/learningIndex.jsp" redirect="false" />
 * 
 *         <forward name="preview" path="/learning/Preview.jsp" redirect="false" /> </action>
 * 
 */
public class McLearningAction extends LamsDispatchAction implements McAppConstants {
    static Logger logger = Logger.getLogger(McLearningAction.class.getName());

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
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(toolSessionID);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	mcLearningForm.setToolContentID(toolContentId);

	if (mcLearningForm.getNextQuestionSelected() != null && !mcLearningForm.getNextQuestionSelected().equals("")) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    mcLearningForm.resetParameters();
	    setContentInUse(request, toolContentId, mcService);
	    return getNextOptions(mapping, form, request, response);
	}

	if (mcLearningForm.getContinueOptionsCombined() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
	    return continueOptionsCombined(mapping, form, request, response);
	} else if (mcLearningForm.getNextOptions() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
	    return getNextOptions(mapping, form, request, response);
	} else if (mcLearningForm.getRedoQuestions() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
	    return redoQuestions(mapping, form, request, response);
	} else if (mcLearningForm.getRedoQuestionsOk() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
	    return redoQuestions(request, mcLearningForm, mapping);
	} else if (mcLearningForm.getViewAnswers() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
	    mcLearningForm.setLearnerProgress(new Boolean(false).toString());
	    return viewAnswers(mapping, mcLearningForm, request, response);
	} else if (mcLearningForm.getSubmitReflection() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
	    return submitReflection(mapping, form, request, response);
	} else if (mcLearningForm.getForwardtoReflection() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
	    return forwardtoReflection(mapping, form, request, response);
	} else if (mcLearningForm.getLearnerFinished() != null) {
	    LearningUtil.saveFormRequestData(request, mcLearningForm, false);
	    setContentInUse(request, toolContentId, mcService);
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
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(toolSessionID);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

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

	/* it is possible that mcQueUsr can be null if the content is set as runoffline and reflection is on */
	if (mcQueUsr == null) {

	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);

	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	    Long userId = new Long(toolUser.getUserID().longValue());

	    mcQueUsr = new McQueUsr(userId, userName, fullName, mcSession, new TreeSet());
	    mcService.createMcQueUsr(mcQueUsr);

	    mcService.createMcQueUsr(mcQueUsr);
	}

	mcQueUsr.setResponseFinalised(true);
	mcService.updateMcQueUsr(mcQueUsr);

	mcService.updateMcQueUsr(mcQueUsr);

	List userAttempts = mcService.getLatestAttemptsForAUser(mcQueUsr.getUid());

	Iterator itAttempts = userAttempts.iterator();
	while (itAttempts.hasNext()) {
	    McUsrAttempt mcUsrAttempt = (McUsrAttempt) itAttempts.next();
	    mcUsrAttempt.setFinished(true);
	    mcService.updateMcUsrAttempt(mcUsrAttempt);
	}

	response.sendRedirect(nextUrl);

	return null;
    }

    /**
     * 
     * 
     * @param learnerInput
     * @param mcContent
     * @param mcService
     * @return
     */
    protected Set parseLearnerInput(List learnerInput, McContent mcContent, IMcService mcService) {

	Set questionUids = new HashSet();

	Iterator listLearnerInputIterator = learnerInput.iterator();
	while (listLearnerInputIterator.hasNext()) {
	    String input = (String) listLearnerInputIterator.next();
	    int pos = input.indexOf("-");
	    String questionUid = input.substring(0, pos);
	    questionUids.add(questionUid);
	}

	List questionEntriesOrdered = mcService.getAllQuestionEntries(mcContent.getUid());

	Set questionOrderedUids = new TreeSet(new McComparator());
	Iterator questionEntriesOrderedIterator = questionEntriesOrdered.iterator();
	while (questionEntriesOrderedIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) questionEntriesOrderedIterator.next();

	    Iterator questionUidsIterator = questionUids.iterator();
	    while (questionUidsIterator.hasNext()) {
		String questionUid = (String) questionUidsIterator.next();

		if (questionUid.equals(mcQueContent.getUid().toString())) {
		    questionOrderedUids.add(questionUid);
		}
	    }
	}
	return questionOrderedUids;
    }

    /**
     * 
     * 
     * @param learnerInput
     * @param mcTempDataHolderDTO
     * @param mcService
     * @param mcContent
     * @return
     */
    protected List buildSelectedQuestionAndCandidateAnswersDTO(List learnerInput,
	    McTempDataHolderDTO mcTempDataHolderDTO, IMcService mcService, McContent mcContent) {

	int learnerMarks = 0;
	int totalMarksPossible = 0;

	Set questionUids = parseLearnerInput(learnerInput, mcContent, mcService);

	List questionAndCandidateAnswersList = new LinkedList();

	Iterator questionIterator = mcContent.getMcQueContents().iterator();
	while (questionIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) questionIterator.next();
	    String currentQuestionUid = mcQueContent.getUid().toString();
	    int currentMark = mcQueContent.getMark().intValue();
	    totalMarksPossible += currentMark;

	    McLearnerAnswersDTO mcLearnerAnswersDTO = new McLearnerAnswersDTO();
	    mcLearnerAnswersDTO.setQuestion(mcQueContent.getQuestion());
	    mcLearnerAnswersDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
	    mcLearnerAnswersDTO.setQuestionUid(mcQueContent.getUid());
	    mcLearnerAnswersDTO.setFeedback(mcQueContent.getFeedback() != null ? mcQueContent.getFeedback() : "");

	    Map<String, String> caMap = new TreeMap<String, String>(new McStringComparator());
	    List<String> caIds = new LinkedList<String>();
	    long mapIndex = new Long(1);

	    Iterator listLearnerInputIterator = learnerInput.iterator();
	    while (listLearnerInputIterator.hasNext()) {
		String input = (String) listLearnerInputIterator.next();
		int pos = input.indexOf("-");
		String localQuestionUid = input.substring(0, pos);

		if (currentQuestionUid.equals(localQuestionUid)) {
		    String caUid = input.substring(pos + 1);
		    McOptsContent mcOptsContent = mcQueContent.getOptionsContentByUID(new Long(caUid));
		    String mapIndexAsString = new Long(mapIndex).toString();
		    caMap.put(mapIndexAsString, mcOptsContent.getMcQueOptionText());
		    caIds.add(caUid);
		    mapIndex++;
		}
	    }
	    mcLearnerAnswersDTO.setCandidateAnswers(caMap);

	    List correctOptions = mcService.getPersistedSelectedOptions(mcQueContent.getUid());
	    boolean compareResult = LearningUtil.isQuestionCorrect(correctOptions, caIds);
	    mcLearnerAnswersDTO.setAttemptCorrect(new Boolean(compareResult).toString());
	    if (compareResult) {
		mcLearnerAnswersDTO.setFeedbackCorrect(mcQueContent.getFeedback());
		mcLearnerAnswersDTO.setMark(new Integer(currentMark));
		learnerMarks += currentMark;
	    } else {
		mcLearnerAnswersDTO.setFeedbackIncorrect(mcQueContent.getFeedback());
		mcLearnerAnswersDTO.setMark(new Integer(0));
	    }

	    questionAndCandidateAnswersList.add(mcLearnerAnswersDTO);

	}// end question iterator

	mcTempDataHolderDTO.setLearnerMark(new Integer(learnerMarks));
	mcTempDataHolderDTO.setTotalMarksPossible(new Integer(totalMarksPossible));

	return questionAndCandidateAnswersList;
    }

    /**
     * responses to learner when they answer all the questions on a single page
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward continueOptionsCombined(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcLearningForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionListingMode = mcLearningForm.getQuestionListingMode();

	List learnerInput = new LinkedList();
	if (questionListingMode.equals(McAppConstants.QUESTION_LISTING_MODE_SEQUENTIAL)) {

	    List sequentialCheckedCa = (List) sessionMap.get(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY);

	    Iterator sequentialCheckedCaIterator = sequentialCheckedCa.iterator();

	    while (sequentialCheckedCaIterator.hasNext()) {
		String input = (String) sequentialCheckedCaIterator.next();
	    }

	    /* checkedCa refers to candidate answers */
	    String[] checkedCa = mcLearningForm.getCheckedCa();

	    if (checkedCa != null) {
		mcLearningForm.resetCa(mapping, request);

		for (int i = 0; i < checkedCa.length; i++) {
		    String currentCa = checkedCa[i];
		    sequentialCheckedCa.add(currentCa);
		}
	    }

	    sequentialCheckedCaIterator = sequentialCheckedCa.iterator();

	    while (sequentialCheckedCaIterator.hasNext()) {
		String input = (String) sequentialCheckedCaIterator.next();
	    }

	    sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, sequentialCheckedCa);
	    request.getSession().setAttribute(httpSessionID, sessionMap);

	    learnerInput = sequentialCheckedCa;

	} else {
	    Map parameters = request.getParameterMap();
	    Iterator iter = parameters.keySet().iterator();
	    while (iter.hasNext()) {
		String key = (String) iter.next();
		if (key.startsWith("checkedCa")) {
		    String currentCheckedCa = request.getParameter(key);
		    if (currentCheckedCa != null) {
			learnerInput.add(currentCheckedCa);
		    }
		}
	    }

	    mcLearningForm.resetCa(mapping, request);
	}

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();

	/* process the answers */
	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

	McTempDataHolderDTO mcTempDataHolderDTO = new McTempDataHolderDTO();

	boolean allQuestionsChecked = allQuestionsChecked(mcService, learnerInput, mcContent, mcTempDataHolderDTO);

	if (!allQuestionsChecked) {
	    // there are no selected answers for any questions
	    ActionMessages errors = new ActionMessages();

	    ActionMessage error = new ActionMessage("answers.submitted.none");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    saveErrors(request, errors);

	    McLearningStarterAction mcLearningStarterAction = new McLearningStarterAction();
	    mcLearningStarterAction.commonContentSetup(request, mcContent, mcService, mcLearningForm, toolSessionID);

	    mcGeneralLearnerFlowDTO.setQuestionIndex(mcTempDataHolderDTO.getDisplayOrder());

	    request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	    return mapping.findForward(McAppConstants.LOAD_LEARNER);
	}

	List selectedQuestionAndCandidateAnswersDTO = buildSelectedQuestionAndCandidateAnswersDTO(learnerInput,
		mcTempDataHolderDTO, mcService, mcContent);
	request.setAttribute(McAppConstants.LIST_SELECTED_QUESTION_CANDIDATEANSWERS_DTO,
		selectedQuestionAndCandidateAnswersDTO);

	mcGeneralLearnerFlowDTO.setQuestionListingMode(McAppConstants.QUESTION_LISTING_MODE_COMBINED);

	Integer learnerMark = mcTempDataHolderDTO.getLearnerMark();
	mcGeneralLearnerFlowDTO.setLearnerMark(learnerMark);
	int totalQuestionCount = mcContent.getMcQueContents().size();
	mcGeneralLearnerFlowDTO.setTotalQuestionCount(new Integer(totalQuestionCount));
	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcTempDataHolderDTO.getTotalMarksPossible());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	Long toolSessionUid = mcSession.getUid();

	boolean isUserDefined = false;

	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null && user.getUserID() != null) {
		userID = user.getUserID().toString();
	    }
	}

	McQueUsr existingMcQueUsr = mcService.getMcUserBySession(new Long(userID), mcSession.getUid());

	if (existingMcQueUsr != null) {
	    isUserDefined = true;
	}

	McQueUsr mcQueUsr = null;
	if (isUserDefined == false) {
	    mcQueUsr = LearningUtil.createUser(request, mcService, new Long(toolSessionID));
	} else {
	    mcQueUsr = existingMcQueUsr;
	}

	Integer currentHighestAttemptOrder = mcQueUsr.getLastAttemptOrder();
	int newHighestAttempOrder = currentHighestAttemptOrder != null ? currentHighestAttemptOrder.intValue() + 1 : 1;

	// Have to work out in advance if passed so that we can store it against the attempts
	boolean passed = mcQueUsr.isMarkPassed(learnerMark);
	mcGeneralLearnerFlowDTO.setUserOverPassMark(new Boolean(passed).toString());
	mcGeneralLearnerFlowDTO.setPassMarkApplicable(new Boolean(mcContent.getPassMark() != null).toString());

	LearningUtil.createLearnerAttempt(request, mcQueUsr, selectedQuestionAndCandidateAnswersDTO, passed,
		newHighestAttempOrder, null, mcService);

	mcQueUsr.setLastAttemptOrder(newHighestAttempOrder);
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
	    Integer zero = new Integer(0);
	    mcGeneralLearnerFlowDTO.setTopMark(zero);
	    mcGeneralLearnerFlowDTO.setLowestMark(zero);
	    mcGeneralLearnerFlowDTO.setAverageMark(zero);
	}

	Map mapQuestionMarks = LearningUtil.buildMarksMap(request, mcContent.getMcContentId(), mcService);

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());

	String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	mcGeneralLearnerFlowDTO.setLatestAttemptMark(mcQueUsr.getLastAttemptTotalMark());

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	return mapping.findForward(McAppConstants.INDIVIDUAL_REPORT);
    }

    public boolean allQuestionsChecked(IMcService mcService, List learnerInput, McContent mcContent,
	    McTempDataHolderDTO mcTempDataHolderDTO) {

	boolean questionSelected = false;
	Iterator listIterator = mcContent.getMcQueContents().iterator();
	while (listIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) listIterator.next();
	    String uid = mcQueContent.getUid().toString();

	    questionSelected = false;
	    Iterator learnerInputIterator = learnerInput.iterator();
	    while (learnerInputIterator.hasNext() && !questionSelected) {
		String learnerInputLine = (String) learnerInputIterator.next();

		int sepIndex = learnerInputLine.indexOf("-");

		String selectedUid = learnerInputLine.substring(0, sepIndex);

		if (uid.equals(selectedUid)) {
		    questionSelected = true;
		}

	    }

	    if (questionSelected == false) {
		mcTempDataHolderDTO.setDisplayOrder(mcQueContent.getDisplayOrder());
		return false;
	    }
	}

	return true;
    }

    /**
     * 
     * continueOptionsCombined(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response)
     * 
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
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String questionIndex = mcLearningForm.getQuestionIndex();

	String httpSessionID = mcLearningForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	List sequentialCheckedCa = (List) sessionMap.get(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY);

	Iterator sequentialCheckedCaIterator = sequentialCheckedCa.iterator();
	while (sequentialCheckedCaIterator.hasNext()) {
	    String input = (String) sequentialCheckedCaIterator.next();
	}

	/* checkedCa refers to candidate answers */
	String[] checkedCa = mcLearningForm.getCheckedCa();

	if (checkedCa != null) {
	    mcLearningForm.resetCa(mapping, request);

	    for (int i = 0; i < checkedCa.length; i++) {
		String currentCa = checkedCa[i];
		sequentialCheckedCa.add(currentCa);
	    }
	}

	sequentialCheckedCaIterator = sequentialCheckedCa.iterator();
	while (sequentialCheckedCaIterator.hasNext()) {
	    String input = (String) sequentialCheckedCaIterator.next();
	}

	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, sequentialCheckedCa);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();

	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));

	boolean randomize = mcContent.isRandomize();

	List listQuestionAndCandidateAnswersDTO = LearningUtil.buildQuestionAndCandidateAnswersDTO(request, mcContent,
		randomize, mcService);
	request.setAttribute(McAppConstants.LIST_QUESTION_CANDIDATEANSWERS_DTO, listQuestionAndCandidateAnswersDTO);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);

	Integer totalQuestionCount = mcGeneralLearnerFlowDTO.getTotalQuestionCount();
	Integer intQuestionIndex = new Integer(questionIndex);
	if (totalQuestionCount.equals(intQuestionIndex)) {
	    mcGeneralLearnerFlowDTO.setTotalCountReached(new Boolean(true).toString());
	}

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());

	String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());

	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcContent.getTotalMarksPossible());

	mcGeneralLearnerFlowDTO.setQuestionIndex(new Integer(questionIndex));
	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request, getServlet()
		.getServletContext());

	return mapping.findForward(McAppConstants.LOAD_LEARNER);
    }

    /**
     * 
     * redoQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * allows the learner to take the activity again
     * 
     * @param request
     * @param form
     * @param mapping
     * @return ActionForward
     */
    public ActionForward redoQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McLearningForm mcLearningForm = (McLearningForm) form;
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();
	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));

	boolean randomize = mcContent.isRandomize();

	List listQuestionAndCandidateAnswersDTO = LearningUtil.buildQuestionAndCandidateAnswersDTO(request, mcContent,
		randomize, mcService);
	request.setAttribute(McAppConstants.LIST_QUESTION_CANDIDATEANSWERS_DTO, listQuestionAndCandidateAnswersDTO);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
	mcGeneralLearnerFlowDTO.setCurrentQuestionIndex(new Integer(1));
	mcGeneralLearnerFlowDTO.setTotalCountReached(new Boolean(false).toString());

	/* use existing session to extract PASSMARK_APPLICABLE and USER_OVER_PASSMARK */
	String httpSessionID = mcLearningForm.getHttpSessionID();

	/* create a new session */
	SessionMap sessionMap = new SessionMap();
	List sequentialCheckedCa = new LinkedList();
	sessionMap.put(McAppConstants.QUESTION_AND_CANDIDATE_ANSWERS_KEY, sequentialCheckedCa);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	mcLearningForm.setHttpSessionID(sessionMap.getSessionID());

	String userID = "";
	HttpSession ss = SessionManager.getSession();

	if (ss != null) {
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null && user.getUserID() != null) {
		userID = user.getUserID().toString();
	    }
	}

	McQueUsr mcQueUsr = mcService.getMcUserBySession(new Long(userID), mcSession.getUid());

	mcGeneralLearnerFlowDTO.setLatestAttemptMark(mcQueUsr.getLastAttemptTotalMark());

	String passMarkApplicable = new Boolean(mcContent.isPassMarkApplicable()).toString();
	mcGeneralLearnerFlowDTO.setPassMarkApplicable(passMarkApplicable);
	mcLearningForm.setPassMarkApplicable(passMarkApplicable);

	String userOverPassMark = new Boolean(mcQueUsr.isLastAttemptMarkPassed()).toString();
	mcGeneralLearnerFlowDTO.setUserOverPassMark(userOverPassMark);
	mcLearningForm.setUserOverPassMark(userOverPassMark);

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());

	String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	mcGeneralLearnerFlowDTO.setRetries(new Boolean(mcContent.isRetries()).toString());

	mcGeneralLearnerFlowDTO.setTotalMarksPossible(mcContent.getTotalMarksPossible());

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(new Long(toolSessionID), request, getServlet()
		.getServletContext());

	return mapping.findForward(McAppConstants.REDO_QUESTIONS);
    }

    /**
     * void prepareViewAnswersData(ActionMapping mapping, McLearningForm mcLearningForm, HttpServletRequest request,
     * HttpServletResponse response)
     * 
     * @param mapping
     * @param mcLearningForm
     * @param request
     * @param response
     */
    public void prepareViewAnswersData(ActionMapping mapping, McLearningForm mcLearningForm,
	    HttpServletRequest request, ServletContext servletContext) {

	// may have to get service from the form - if class has been created by starter action, rather than by struts
	IMcService mcService = null;
	if (getServlet() != null) {
	    mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	} else {
	    mcService = mcLearningForm.getMcService();
	}

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

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
	    mcQueUsr = LearningUtil.getUser(request, mcService, toolSessionID);
	} else {
	    mcQueUsr = mcService.getMcUserBySession(new Long(learnerProgressUserId), mcSession.getUid());
	}

	Long toolContentUID = mcContent.getUid();

	Map[] attemptMaps = LearningUtil.getAttemptMapsForUser(mcContent.getMcQueContents().size(), toolContentUID,
		mcContent.isRetries(), mcService, mcQueUsr);
	mcGeneralLearnerFlowDTO.setMapFinalAnswersIsContent(attemptMaps[0]);
	mcGeneralLearnerFlowDTO.setMapFinalAnswersContent(attemptMaps[1]);
	mcGeneralLearnerFlowDTO.setMapQueAttempts(attemptMaps[2]);
	mcGeneralLearnerFlowDTO.setMapQueCorrectAttempts(attemptMaps[3]);
	mcGeneralLearnerFlowDTO.setMapQueIncorrectAttempts(attemptMaps[4]);

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());
	String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

	NotebookEntry notebookEntry = mcService.getEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
		McAppConstants.MY_SIGNATURE, new Integer(mcQueUsr.getQueUsrId().intValue()));
	if (notebookEntry != null) {
	    String notebookEntryPresentable = notebookEntry.getEntry();
	    notebookEntryPresentable = McUtils.replaceNewLines(notebookEntryPresentable);
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
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
     * indicates that some learners are using the content
     * 
     * @param request
     * @param toolContentId
     * @param mcService
     */
    protected void setContentInUse(HttpServletRequest request, String toolContentId, IMcService mcService) {

	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));
	mcContent.setContentInUse(true);
	mcService.saveMcContent(mcContent);
    }

    /**
     * 
     * 
     * @param request
     * @param mcLearningForm
     * @param mapping
     * @return
     */
    public ActionForward redoQuestions(HttpServletRequest request, McLearningForm mcLearningForm, ActionMapping mapping) {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	/* reset the checked options MAP */
	Map mapGeneralCheckedOptionsContent = new TreeMap(new McComparator());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	String toolContentId = mcSession.getMcContent().getMcContentId().toString();

	McContent mcContent = mcService.retrieveMc(new Long(toolContentId));

	boolean randomize = mcContent.isRandomize();

	List<McLearnerAnswersDTO> listQuestionAndCandidateAnswersDTO = LearningUtil
		.buildQuestionAndCandidateAnswersDTO(request, mcContent, randomize, mcService);
	request.setAttribute(McAppConstants.LIST_QUESTION_CANDIDATEANSWERS_DTO, listQuestionAndCandidateAnswersDTO);

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = LearningUtil.buildMcGeneralLearnerFlowDTO(mcContent);
	mcGeneralLearnerFlowDTO.setQuestionIndex(new Integer(1));

	mcGeneralLearnerFlowDTO.setReflection(new Boolean(mcContent.isReflect()).toString());

	String reflectionSubject = McUtils.replaceNewLines(mcContent.getReflectionSubject());
	mcGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

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
	Boolean showMarks = LearningUtil.isShowMarksOnQuestion(listQuestionAndCandidateAnswersDTO);
	mcGeneralLearnerFlowDTO.setShowMarks(showMarks.toString());

	request.setAttribute(McAppConstants.MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);
	return mapping.findForward(McAppConstants.LOAD_LEARNER);
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

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	mcLearningForm.setToolSessionID(toolSessionID);

	String userID = request.getParameter("userID");
	mcLearningForm.setUserID(userID);

	String reflectionEntry = request.getParameter(McAppConstants.ENTRY_TEXT);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	McQueUsr mcQueUsr = mcService.getMcUserBySession(new Long(userID), mcSession.getUid());

	/* it is possible that mcQueUsr can be null if the content is set as runoffline and reflection is on */
	if (mcQueUsr == null) {
	    // attempt creating user record since it must exist for the runOffline + reflection screens
	    HttpSession ss = SessionManager.getSession();

	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);

	    String userName = toolUser.getLogin();
	    String fullName = toolUser.getFirstName() + " " + toolUser.getLastName();

	    Long userId = new Long(toolUser.getUserID().longValue());

	    mcQueUsr = new McQueUsr(userId, userName, fullName, mcSession, new TreeSet());
	    mcService.createMcQueUsr(mcQueUsr);
	    mcService.createMcQueUsr(mcQueUsr);
	}
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
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String toolSessionID = request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);

	McSession mcSession = mcService.retrieveMcSession(new Long(toolSessionID));

	McContent mcContent = mcSession.getMcContent();

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = new McGeneralLearnerFlowDTO();
	mcGeneralLearnerFlowDTO.setActivityTitle(mcContent.getTitle());
	String reflectionSubject = mcContent.getReflectionSubject();

	reflectionSubject = McUtils.replaceNewLines(reflectionSubject);
	mcGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);

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

}
