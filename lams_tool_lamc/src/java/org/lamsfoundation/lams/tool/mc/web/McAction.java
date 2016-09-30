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

package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Action class that controls the logic of tool behavior.
 *
 * @author Ozgur Demirtas
 */
public class McAction extends LamsDispatchAction implements McAppConstants {
    private static Logger logger = Logger.getLogger(McAction.class.getName());

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return (mapping.findForward(McAppConstants.LOAD_AUTHORING));
    }

    /**
     * submits content into the tool database
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	ActionMessages errors = new ActionMessages();
	if (questionDTOs.isEmpty()) {
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	}

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	// there are no issues with input, continue and submit data

	McContent mcContentTest = mcService.getMcContent(new Long(strToolContentID));

	McContent mcContent = mcContentTest;
	if (errors.isEmpty()) {

	    ToolAccessMode mode = getAccessMode(request);
	    request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	    List<McQuestionDTO> deletedQuestionDTOs = (List<McQuestionDTO>) sessionMap.get(LIST_DELETED_QUESTION_DTOS);

	    // in case request is from monitoring module - prepare for recalculate User Answers
	    if (mode.isTeacher()) {
		Set<McQueContent> oldQuestions = mcContent.getMcQueContents();
		mcService.releaseQuestionsFromCache(mcContent);
		mcService.setDefineLater(strToolContentID, false);

		// recalculate User Answers
		mcService.recalculateUserAnswers(mcContent, oldQuestions, questionDTOs, deletedQuestionDTOs);
	    }

	    // remove deleted questions
	    for (McQuestionDTO deletedQuestionDTO : deletedQuestionDTOs) {
		McQueContent removeableQuestion = mcService.getQuestionByUid(deletedQuestionDTO.getUid());
		if (removeableQuestion != null) {
//		    Set<McUsrAttempt> attempts = removeableQuestion.getMcUsrAttempts();
//		    Iterator<McUsrAttempt> iter = attempts.iterator();
//		    while (iter.hasNext()) {
//			McUsrAttempt attempt = iter.next();
//			iter.remove();
//		    }
//		    mcService.updateQuestion(removeableQuestion);
		    mcContent.getMcQueContents().remove(removeableQuestion);
		    mcService.removeMcQueContent(removeableQuestion);
		}
	    }

	    //store content
	    mcContent = AuthoringUtil.saveOrUpdateMcContent(mcService, request, mcContentTest, strToolContentID);

	    //store questions
	    mcContent = mcService.createQuestions(questionDTOs, mcContent);

	    if (mcContent != null) {

		// sorts the questions by the display order
		List<McQueContent> sortedQuestions = mcService.getAllQuestionsSorted(mcContent.getUid().longValue());
		int displayOrder = 1;
		for (McQueContent question : sortedQuestions) {
		    McQueContent existingQuestion = mcService.getQuestionByUid(question.getUid());
		    existingQuestion.setDisplayOrder(new Integer(displayOrder));
		    mcService.updateQuestion(existingQuestion);
		    displayOrder++;
		}
	    }

	    McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	    request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	} else {
	    // errors is not empty

	    if (mcContent != null) {
		McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
			httpSessionID);
	    }
	}

	mcAuthoringForm.resetUserAction();

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	// generating dyn pass map using questionDTOs
	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setCurrentTab("1");

	return mapping.findForward(McAppConstants.LOAD_AUTHORING);
    }

    public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	String mark = request.getParameter("mark");

	String passmark = request.getParameter("passmark");

	List<McOptionDTO> options = McAction.repopulateOptionDTOs(request, false);
	options = AuthoringUtil.removeBlankOptions(options);

	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setMarkValue(mark);
	mcGeneralAuthoringDTO.setPassMarkValue(passmark);

	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String newQuestion = request.getParameter("newQuestion");

	String feedback = request.getParameter("feedback");

	String editableQuestionIndex = request.getParameter("editableQuestionIndex");
	mcAuthoringForm.setQuestionIndex(editableQuestionIndex);

	if ((newQuestion != null) && (newQuestion.length() > 0)) {
	    if ((editQuestionBoxRequest != null) && (editQuestionBoxRequest.equals("false"))) {
		// request for add and save
		boolean duplicates = AuthoringUtil.checkDuplicateQuestions(questionDTOs, newQuestion);

		if (!duplicates) {
		    McQuestionDTO questionDTO = null;
		    Iterator<McQuestionDTO> iter = questionDTOs.iterator();
		    while (iter.hasNext()) {
			questionDTO = iter.next();

			String displayOrder = questionDTO.getDisplayOrder();

			if ((displayOrder != null) && (!displayOrder.equals(""))) {
			    if (displayOrder.equals(editableQuestionIndex)) {
				break;
			    }

			}
		    }

		    questionDTO.setQuestion(newQuestion);
		    questionDTO.setFeedback(feedback);
		    questionDTO.setDisplayOrder(editableQuestionIndex);
		    questionDTO.setListCandidateAnswersDTO(options);
		    questionDTO.setMark(mark);

		    questionDTOs = AuthoringUtil.reorderUpdateQuestionDtos(questionDTOs, questionDTO,
			    editableQuestionIndex);
		    // post reorderUpdateListQuestionContentDTO questionDTOs
		} else {
		    // duplicate question entry, not adding
		}
	    } else {
		// request for edit and save
		McQuestionDTO questionDTO = null;
		Iterator<McQuestionDTO> iter = questionDTOs.iterator();
		while (iter.hasNext()) {
		    questionDTO = iter.next();

		    String displayOrder = questionDTO.getDisplayOrder();

		    if ((displayOrder != null) && (!displayOrder.equals(""))) {
			if (displayOrder.equals(editableQuestionIndex)) {
			    break;
			}

		    }
		}

		questionDTO.setQuestion(newQuestion);
		questionDTO.setFeedback(feedback);
		questionDTO.setDisplayOrder(editableQuestionIndex);
		questionDTO.setListCandidateAnswersDTO(options);
		questionDTO.setMark(mark);

		questionDTOs = AuthoringUtil.reorderUpdateQuestionDtos(questionDTOs, questionDTO,
			editableQuestionIndex);
	    }
	} else {
	    // entry blank, not adding
	}

	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, strToolContentID, mcService,
		httpSessionID, questionDTOs);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return (mapping.findForward("itemList"));

    }

    /**
     * Parses questions extracted from IMS QTI file and adds them to currently edited question.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionForward saveQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// big part of code was taken from addSingleQuestion() and saveSingleQuestion() methods
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = request.getParameter("httpSessionID");
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);
	// proper parsing
	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);

	for (Question question : questions) {
	    // quietly do same verification as in other question-adding methods
	    String questionText = question.getText();
	    if (StringUtils.isBlank(questionText)) {
		LamsDispatchAction.log.warn("Skipping a blank question.");
		continue;
	    }

	    questionText = QuestionParser.processHTMLField(questionText, false, contentFolderID,
		    question.getResourcesFolderPath());

	    if (AuthoringUtil.checkDuplicateQuestions(questionDTOs, questionText)) {
		LamsDispatchAction.log.warn("Skipping duplicate question: " + questionText);
		continue;
	    }

	    List<McOptionDTO> optionDtos = new ArrayList<McOptionDTO>();
	    String correctAnswer = null;
	    Integer correctAnswerScore = 1;

	    if (question.getAnswers() != null) {
		for (Answer answer : question.getAnswers()) {
		    McOptionDTO optionDto = new McOptionDTO();
		    String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
			    question.getResourcesFolderPath());
		    if (answerText == null) {
			LamsDispatchAction.log.warn("Skipping a blank answer");
			continue;
		    }
		    if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			LamsDispatchAction.log
				.warn("Skipping an answer with same text as the correct answer: " + answerText);

			continue;
		    }

		    optionDto.setCandidateAnswer(answerText);

		    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			if (correctAnswer == null) {
			    optionDto.setCorrect("Correct");
			    correctAnswer = optionDto.getCandidateAnswer();
			    // marks are integer numbers
			    correctAnswerScore = Math.min(new Double(Math.ceil(answer.getScore())).intValue(), 10);
			} else {
			    // there can be only one correct answer in a MCQ question
			    LamsDispatchAction.log.warn(
				    "Choosing only first correct answer, despite another one was found: " + answerText);
			    optionDto.setCorrect("Incorrect");
			}
		    } else {
			optionDto.setCorrect("Incorrect");
		    }

		    optionDtos.add(optionDto);
		}
	    }

	    if (correctAnswer == null) {
		LamsDispatchAction.log.warn("No correct answer found for question: " + questionText);
		continue;
	    }

	    McQuestionDTO questionDto = new McQuestionDTO();
	    questionDto.setDisplayOrder(String.valueOf(questionDTOs.size() + 1));
	    questionDto.setQuestion(questionText);
	    questionDto.setFeedback(QuestionParser.processHTMLField(question.getFeedback(), true, null, null));
	    questionDto.setListCandidateAnswersDTO(optionDtos);
	    questionDto.setMark(correctAnswerScore.toString());

	    questionDTOs.add(questionDto);

	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Added question: " + questionText);
	    }
	}

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, strToolContentID, mcService,
		httpSessionID, questionDTOs);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	return mapping.findForward(McAppConstants.LOAD_AUTHORING);
    }

    /**
     * Prepares MC questions for QTI packing
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionForward exportQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String httpSessionID = request.getParameter("httpSessionID");
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);
	List<Question> questions = new LinkedList<Question>();

	for (McQuestionDTO mcQuestion : questionDTOs) {
	    Question question = new Question();

	    question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);
	    question.setTitle("Question " + mcQuestion.getDisplayOrder());
	    question.setText(mcQuestion.getQuestion());
	    question.setFeedback(mcQuestion.getFeedback());
	    List<Answer> answers = new ArrayList<Answer>();

	    for (McOptionDTO mcAnswer : mcQuestion.getListCandidateAnswersDTO()) {
		Answer answer = new Answer();
		answer.setText(mcAnswer.getCandidateAnswer());
		answer.setScore(
			"Correct".equalsIgnoreCase(mcAnswer.getCorrect()) ? Float.parseFloat(mcQuestion.getMark()) : 0);

		answers.add(answer);
		question.setAnswers(answers);
	    }

	    // put the question in the right place
	    questions.add(Integer.parseInt(mcQuestion.getDisplayOrder()) - 1, question);
	}

	String title = request.getParameter("title");
	QuestionExporter exporter = new QuestionExporter(title, questions.toArray(Question.QUESTION_ARRAY_TYPE));
	exporter.exportQTIPackage(request, response);

	return null;
    }

    private void commonSaveCode(HttpServletRequest request, McGeneralAuthoringDTO mcGeneralAuthoringDTO,
	    McAuthoringForm mcAuthoringForm, SessionMap<String, Object> sessionMap, String strToolContentID,
	    IMcService mcService, String httpSessionID, List<McQuestionDTO> questionDTOs) {
	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setCurrentTab("1");

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);
    }

    public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	McQuestionDTO newQuestionDTO = (McQuestionDTO) sessionMap.get(McAppConstants.NEW_QUESTION_DTO);
	request.setAttribute(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);

	int listSize = questionDTOs.size();

	String newQuestionParam = request.getParameter("newQuestion");

	String feedback = request.getParameter("feedback");

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	String passmark = request.getParameter("passmark");
	mcGeneralAuthoringDTO.setPassMarkValue(passmark);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);
	optionDtos = AuthoringUtil.removeBlankOptions(optionDtos);

	if ((newQuestionParam != null) && (newQuestionParam.length() > 0)) {
	    boolean duplicates = AuthoringUtil.checkDuplicateQuestions(questionDTOs, newQuestionParam);

	    if (!duplicates) {
		McQuestionDTO questionDto = new McQuestionDTO();
		questionDto.setDisplayOrder(new Long(listSize + 1).toString());
		questionDto.setFeedback(feedback);
		questionDto.setQuestion(newQuestionParam);
		questionDto.setMark(mark);

		questionDto.setListCandidateAnswersDTO(optionDtos);

		questionDTOs.add(questionDto);
	    } else {
		// entry duplicate, not adding
	    }
	} else {
	    // entry blank, not adding
	}

	mcGeneralAuthoringDTO.setMarkValue(mark);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestionParam);
	mcAuthoringForm.setFeedback(feedback);

	mcGeneralAuthoringDTO.setMarkValue(mark);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, strToolContentID, mcService,
		httpSessionID, questionDTOs);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return (mapping.findForward("itemList"));
    }

    /**
     * opens up an new screen within the current page for adding a new question
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	//prepare question for adding new question page
	McQuestionDTO newQuestionDTO = new McQuestionDTO();
	List<McOptionDTO> newOptions = new ArrayList<McOptionDTO>();
	McOptionDTO newOption1 = new McOptionDTO();
	newOption1.setCorrect("Correct");
	McOptionDTO newOption2 = new McOptionDTO();
	newOptions.add(newOption1);
	newOptions.add(newOption2);
	newQuestionDTO.setListCandidateAnswersDTO(newOptions);
	sessionMap.put(NEW_QUESTION_DTO, newQuestionDTO);
	request.setAttribute(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String newQuestionParam = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestionParam);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);

	return (mapping.findForward("newQuestionBox"));
    }

    /**
     * opens up an new screen within the current page for editing a question
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	request.setAttribute(McAppConstants.CURRENT_EDITABLE_QUESTION_INDEX, questionIndex);

	mcAuthoringForm.setEditableQuestionIndex(questionIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	String editableQuestion = "";
	String editableFeedback = "";
	String editableMark = "";
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = (McQuestionDTO) iter.next();
	    String displayOrder = questionDto.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = questionDto.getFeedback();
		    editableQuestion = questionDto.getQuestion();
		    editableMark = questionDto.getMark();

		    List candidates = questionDto.getListCandidateAnswersDTO();

		    break;
		}

	    }
	}

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = (McGeneralAuthoringDTO) request
		.getAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO);

	if (mcGeneralAuthoringDTO == null) {
	    mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	}

	mcGeneralAuthoringDTO.setMarkValue(editableMark);
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setEditableQuestionText(editableQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionFeedback(editableFeedback);
	mcAuthoringForm.setFeedback(editableFeedback);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String requestNewEditableQuestionBox = (String) request.getAttribute("requestNewEditableQuestionBox");

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	String newQuestion = request.getParameter("newQuestion");
	String feedback = request.getParameter("feedback");
	// if ((editQuestionBoxRequest != null) && (editQuestionBoxRequest.equals("false")))
	if ((requestNewEditableQuestionBox != null) && requestNewEditableQuestionBox.equals("true")) {
	    // String newQuestion=request.getParameter("newQuestion");
	    // logger.debug("newQuestion: " + newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	    mcGeneralAuthoringDTO.setEditableQuestionFeedback(feedback);

	    //  mcAuthoringForm.setFeedback(feedback);
	}

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	return (mapping.findForward("editQuestionBox"));
    }

    /**
     * removes a question from the questions map
     */
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	String questionIndexToDelete = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndexToDelete);

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);
	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	//exclude Question with questionIndex From List
	List<McQuestionDTO> listFinalQuestionContentDTO = new LinkedList<McQuestionDTO>();
	int queIndex = 0;
	for (McQuestionDTO questionDTO : questionDTOs) {

	    String questionText = questionDTO.getQuestion();
	    String displayOrder = questionDTO.getDisplayOrder();
	    if ((questionText != null) && (!questionText.isEmpty()) && !displayOrder.equals(questionIndexToDelete)) {

		++queIndex;
		questionDTO.setDisplayOrder(new Integer(queIndex).toString());
		listFinalQuestionContentDTO.add(questionDTO);
	    }
	    if ((questionText != null) && (!questionText.isEmpty()) && displayOrder.equals(questionIndexToDelete)) {
		List<McQuestionDTO> deletedQuestionDTOs = (List<McQuestionDTO>) sessionMap
			.get(LIST_DELETED_QUESTION_DTOS);
		;
		deletedQuestionDTOs.add(questionDTO);
		sessionMap.put(LIST_DELETED_QUESTION_DTOS, deletedQuestionDTOs);
	    }
	}
	questionDTOs = listFinalQuestionContentDTO;
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return (mapping.findForward("itemList"));
    }

    /**
     * moves a question down in the list
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	questionDTOs = AuthoringUtil.swapQuestions(questionDTOs, questionIndex, "down");

	questionDTOs = AuthoringUtil.reorderSimpleQuestionDtos(questionDTOs);

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);
	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	return (mapping.findForward("itemList"));
    }

    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	questionDTOs = AuthoringUtil.swapQuestions(questionDTOs, questionIndex, "up");

	questionDTOs = AuthoringUtil.reorderSimpleQuestionDtos(questionDTOs);

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setCurrentTab("1");

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return (mapping.findForward("itemList"));
    }

    /**
     *
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
     * moves a candidate dwn in the list
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	request.setAttribute("questionIndex", questionIndex);
	request.setAttribute(McAppConstants.CURRENT_EDITABLE_QUESTION_INDEX, questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	List candidates = new LinkedList();
	List listCandidates = new LinkedList();
	String editableQuestion = "";
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = (McQuestionDTO) iter.next();

	    String question = questionDto.getQuestion();
	    String displayOrder = questionDto.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    editableQuestion = questionDto.getQuestion();

		    candidates = questionDto.getListCandidateAnswersDTO();
		    // candidates found
		    // but we are using the repopulated optionDtos here

		    listCandidates = AuthoringUtil.swapOptions(optionDtos, candidateIndex, "down");

		    questionDto.setListCandidateAnswersDTO(listCandidates);

		    break;
		}

	    }
	}

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return (mapping.findForward("candidateAnswersList"));
    }

    /**
     * moves a candidate up in the list
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	request.setAttribute("questionIndex", questionIndex);
	request.setAttribute(McAppConstants.CURRENT_EDITABLE_QUESTION_INDEX, questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	List candidates = new LinkedList();
	List listCandidates = new LinkedList();
	String editableQuestion = "";
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = (McQuestionDTO) iter.next();

	    String question = questionDto.getQuestion();
	    String displayOrder = questionDto.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    editableQuestion = questionDto.getQuestion();

		    candidates = questionDto.getListCandidateAnswersDTO();

		    listCandidates = AuthoringUtil.swapOptions(optionDtos, candidateIndex, "up");

		    questionDto.setListCandidateAnswersDTO(listCandidates);

		    break;
		}

	    }
	}

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return (mapping.findForward("candidateAnswersList"));
    }

    /**
     * removes a candidate from the list
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	request.setAttribute("questionIndex", questionIndex);
	request.setAttribute(McAppConstants.CURRENT_EDITABLE_QUESTION_INDEX, questionIndex);

	String optionIndexToRemove = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", optionIndexToRemove);

	//find question
	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);
	McQuestionDTO questionDto = null;
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    questionDto = (McQuestionDTO) iter.next();
	    String displayOrder = questionDto.getDisplayOrder();
	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}
	    }
	}

	//update options
	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);
	List<McOptionDTO> listFinalCandidatesDTO = new LinkedList<McOptionDTO>();
	McOptionDTO mcOptionDTO = null;
	Iterator listCaIterator = optionDtos.iterator();
	int caIndex = 0;
	while (listCaIterator.hasNext()) {
	    caIndex++;
	    mcOptionDTO = (McOptionDTO) listCaIterator.next();
	    if (caIndex != new Integer(optionIndexToRemove).intValue()) {
		listFinalCandidatesDTO.add(mcOptionDTO);
	    }
	}

	questionDto.setListCandidateAnswersDTO(listFinalCandidatesDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return (mapping.findForward("candidateAnswersList"));
    }

    /**
     * enables adding a new candidate answer
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	request.setAttribute("questionIndex", questionIndex);
	request.setAttribute(McAppConstants.CURRENT_EDITABLE_QUESTION_INDEX, questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, true);

	String newQuestion = request.getParameter("newQuestion");

	String mark = request.getParameter("mark");

	String passmark = request.getParameter("passmark");

	String feedback = request.getParameter("feedback");

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	McQuestionDTO mcQuestionContentDTOLocal = null;
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    mcQuestionContentDTOLocal = (McQuestionDTO) iter.next();

	    String question = mcQuestionContentDTOLocal.getQuestion();
	    String displayOrder = mcQuestionContentDTOLocal.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	if (mcQuestionContentDTOLocal != null) {
	    mcQuestionContentDTOLocal.setListCandidateAnswersDTO(optionDtos);
	}

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());
	return (mapping.findForward("candidateAnswersList"));
    }

    public ActionForward updateMarksList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	ToolAccessMode mode = getAccessMode(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setCurrentTab("2");

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return (mapping.findForward(McAppConstants.LOAD_AUTHORING));
    }

    public ActionForward moveAddedCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	//moveAddedCandidateUp
	McQuestionDTO newQuestionDTO = (McQuestionDTO) sessionMap.get(McAppConstants.NEW_QUESTION_DTO);
	List listCandidates = new LinkedList();
	listCandidates = AuthoringUtil.swapOptions(optionDtos, candidateIndex, "up");
	newQuestionDTO.setListCandidateAnswersDTO(listCandidates);
	request.setAttribute(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);
	sessionMap.put(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	return (mapping.findForward("candidateAnswersAddList"));

    }

    public ActionForward moveAddedCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	//moveAddedCandidateDown
	McQuestionDTO newQuestionDTO = (McQuestionDTO) sessionMap.get(McAppConstants.NEW_QUESTION_DTO);
	List listCandidates = new LinkedList();
	listCandidates = AuthoringUtil.swapOptions(optionDtos, candidateIndex, "down");
	newQuestionDTO.setListCandidateAnswersDTO(listCandidates);
	request.setAttribute(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);
	sessionMap.put(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return (mapping.findForward("candidateAnswersAddList"));
    }

    public ActionForward removeAddedCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String candidateIndexToRemove = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndexToRemove);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);
	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	// removeAddedCandidate
	McQuestionDTO newQuestionDTO = (McQuestionDTO) sessionMap.get(McAppConstants.NEW_QUESTION_DTO);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);
	List<McOptionDTO> listFinalCandidatesDTO = new LinkedList<McOptionDTO>();
	int caIndex = 0;
	for (McOptionDTO mcOptionDTO : optionDtos) {
	    caIndex++;

	    if (caIndex != new Integer(candidateIndexToRemove).intValue()) {
		listFinalCandidatesDTO.add(mcOptionDTO);
	    }
	}

	newQuestionDTO.setListCandidateAnswersDTO(listFinalCandidatesDTO);
	request.setAttribute(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);
	sessionMap.put(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	return (mapping.findForward("candidateAnswersAddList"));

    }

    public ActionForward newAddedCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.LIST_QUESTION_DTOS);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, true);

	String newQuestion = request.getParameter("newQuestion");

	String mark = request.getParameter("mark");

	String passmark = request.getParameter("passmark");

	String feedback = request.getParameter("feedback");

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	//newAddedCandidateBox
	McQuestionDTO newQuestionDTO = (McQuestionDTO) sessionMap.get(McAppConstants.NEW_QUESTION_DTO);
	newQuestionDTO.setListCandidateAnswersDTO(optionDtos);
	request.setAttribute(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);
	sessionMap.put(McAppConstants.NEW_QUESTION_DTO, newQuestionDTO);

	sessionMap.put(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);

	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	sessionMap.put(McAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(McAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(McAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	Map marksMap = AuthoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = AuthoringUtil.buildDynamicPassMarkMap(questionDTOs, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = AuthoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(McAppConstants.MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(McAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return (mapping.findForward("candidateAnswersAddList"));

    }

    protected boolean existsContent(long toolContentID, IMcService mcService) {
	McContent mcContent = mcService.getMcContent(new Long(toolContentID));
	if (mcContent == null) {
	    return false;
	}

	return true;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     *
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }

    /**
     * repopulateOptionsBox
     */
    private static List<McOptionDTO> repopulateOptionDTOs(HttpServletRequest request, boolean isAddBlankOptions) {

	String correct = request.getParameter("correct");

	/* check this logic again */
	int intCorrect = 0;
	if (correct != null) {
	    intCorrect = new Integer(correct).intValue();
	}

	List<McOptionDTO> optionDtos = new LinkedList<McOptionDTO>();

	for (int i = 0; i < McAppConstants.MAX_OPTION_COUNT; i++) {
	    String optionText = request.getParameter("ca" + i);
	    Long optionUid = WebUtil.readLongParam(request, "caUid" + i, true);

	    String isCorrect = "Incorrect";

	    if (i == intCorrect) {
		isCorrect = "Correct";
	    }

	    if (optionText != null) {
		McOptionDTO optionDTO = new McOptionDTO();
		optionDTO.setUid(optionUid);
		optionDTO.setCandidateAnswer(optionText);
		optionDTO.setCorrect(isCorrect);
		optionDtos.add(optionDTO);
	    }
	}

	if (isAddBlankOptions) {
	    McOptionDTO optionDTO = new McOptionDTO();
	    optionDTO.setCandidateAnswer("");
	    optionDTO.setCorrect("Incorrect");
	    optionDtos.add(optionDTO);
	}

	return optionDtos;
    }

}
