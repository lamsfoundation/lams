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
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.tool.mc.web.form.McAuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Action class that controls the logic of tool behavior.
 *
 * @author Ozgur Demirtas
 */
public class McAction extends LamsDispatchAction {
    private static Logger logger = Logger.getLogger(McAction.class.getName());

    /**
     * submits content into the tool database
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String sessionMapId = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	String strToolContentID = (String) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.QUESTION_DTOS);
	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	List<McQuestionDTO> deletedQuestionDTOs = (List<McQuestionDTO>) sessionMap
		.get(McAppConstants.LIST_DELETED_QUESTION_DTOS);
	
	if (questionDTOs.isEmpty()) {
	    ActionMessages errors = new ActionMessages();
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    saveErrors(request, errors);
	    McAction.logger.debug("errors saved: " + errors);
	    return mapping.findForward(McAppConstants.LOAD_AUTHORING);
	}

	// in case request is from monitoring module - prepare for recalculate User Answers
	if (mode.isTeacher()) {
	    Set<McQueContent> oldQuestions = mcContent.getMcQueContents();
	    mcService.releaseQuestionsFromCache(mcContent);
	    mcService.setDefineLater(strToolContentID, false);

	    // audit log the teacher has started editing activity in monitor
	    mcService.auditLogStartEditingActivityInMonitor(new Long(strToolContentID));

	    // recalculate User Answers
	    mcService.recalculateUserAnswers(mcContent, oldQuestions, questionDTOs, deletedQuestionDTOs);
	}

	// remove deleted questions
	for (McQuestionDTO deletedQuestionDTO : deletedQuestionDTOs) {
	    McQueContent removeableQuestion = mcService.getQuestionByUid(deletedQuestionDTO.getUid());
	    if (removeableQuestion != null) {
		// Set<McUsrAttempt> attempts = removeableQuestion.getMcUsrAttempts();
		// Iterator<McUsrAttempt> iter = attempts.iterator();
		// while (iter.hasNext()) {
		// McUsrAttempt attempt = iter.next();
		// iter.remove();
		// }
		// mcService.updateQuestion(removeableQuestion);
		mcContent.getMcQueContents().remove(removeableQuestion);
		mcService.removeMcQueContent(removeableQuestion);
	    }
	}

	// store content
	mcContent = AuthoringUtil.saveOrUpdateMcContent(mcService, request, mcContent, strToolContentID, questionDTOs);

	// store questions
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

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return mapping.findForward(McAppConstants.LOAD_AUTHORING);
    }
    
    /**
     * opens up an new screen within the current page for editing a question
     */
    public ActionForward editQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	List<McQuestionDTO> questionDtos = (List) sessionMap.get(McAppConstants.QUESTION_DTOS);

	Integer questionIndex = WebUtil.readIntParam(request, "questionIndex", true);
	
	McQuestionDTO questionDto = null;
	//editing existing question
	if (questionIndex != null) {
	    mcAuthoringForm.setQuestionIndex(questionIndex);
	    
	    //find according questionDto
	    for (McQuestionDTO questionDtoIter : questionDtos) {
		Integer displayOrder = questionDtoIter.getDisplayOrder();

		if ((displayOrder != null) && displayOrder.equals(questionIndex)) {
		    questionDto = questionDtoIter;
		    break;
		}
	    }
	    
	//adding new question
	} else {
	    // prepare question for adding new question page
	    questionDto = new McQuestionDTO();
	    List<McOptionDTO> newOptions = new ArrayList<McOptionDTO>();
	    McOptionDTO newOption1 = new McOptionDTO();
	    newOption1.setCorrect("Correct");
	    McOptionDTO newOption2 = new McOptionDTO();
	    newOptions.add(newOption1);
	    newOptions.add(newOption2);
	    questionDto.setOptionDtos(newOptions);
	}
	sessionMap.put(McAppConstants.QUESTION_DTO, questionDto);

	return (mapping.findForward("editQuestionBox"));
    }

    /**
     * removes a question from the questions map
     */
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	
	Integer questionIndexToDelete = WebUtil.readIntParam(request, "questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndexToDelete);

	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.QUESTION_DTOS);

	//exclude Question with questionIndex From List
	List<McQuestionDTO> tempQuestionDtos = new LinkedList<McQuestionDTO>();
	int queIndex = 0;
	for (McQuestionDTO questionDTO : questionDTOs) {

	    String questionText = questionDTO.getQuestion();
	    Integer displayOrder = questionDTO.getDisplayOrder();
	    if ((questionText != null) && !questionText.isEmpty()) {

		if (!displayOrder.equals(questionIndexToDelete)) {
		    ++queIndex;
		    questionDTO.setDisplayOrder(queIndex);
		    tempQuestionDtos.add(questionDTO);
		    
		} else {
		    List<McQuestionDTO> deletedQuestionDTOs = (List<McQuestionDTO>) sessionMap
			    .get(McAppConstants.LIST_DELETED_QUESTION_DTOS);
		    deletedQuestionDTOs.add(questionDTO);
		    sessionMap.put(McAppConstants.LIST_DELETED_QUESTION_DTOS, deletedQuestionDTOs);
		}
	    }
	}
	questionDTOs = tempQuestionDtos;
	sessionMap.put(McAppConstants.QUESTION_DTOS, questionDTOs);

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

	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	Integer questionIndex = WebUtil.readIntParam(request, "questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.QUESTION_DTOS);
	questionDTOs = McAction.swapQuestions(questionDTOs, questionIndex, "down");
	questionDTOs = McAction.reorderQuestionDtos(questionDTOs);
	sessionMap.put(McAppConstants.QUESTION_DTOS, questionDTOs);

	return (mapping.findForward("itemList"));
    }

    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	Integer questionIndex = WebUtil.readIntParam(request, "questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List<McQuestionDTO> questionDTOs = (List) sessionMap.get(McAppConstants.QUESTION_DTOS);
	questionDTOs = McAction.swapQuestions(questionDTOs, questionIndex, "up");
	questionDTOs = McAction.reorderQuestionDtos(questionDTOs);
	sessionMap.put(McAppConstants.QUESTION_DTOS, questionDTOs);

	return (mapping.findForward("itemList"));
    }

    /*
     * swappes McQuestionDTO questions in the list. Auxiliary method for moveQuestionDown() and moveQuestionUp()
     */
    private static List<McQuestionDTO> swapQuestions(List<McQuestionDTO> questionDTOs, Integer originalQuestionIndex,
	    String direction) {

	int replacedQuestionIndex = direction.equals("down") ? originalQuestionIndex + 1 : originalQuestionIndex - 1;

	McQuestionDTO mainQuestion = questionDTOs.get(originalQuestionIndex - 1);
	McQuestionDTO replacedQuestion = questionDTOs.get(replacedQuestionIndex - 1);
	if ((mainQuestion == null) || (replacedQuestion == null)) {
	    return questionDTOs;
	}

	List<McQuestionDTO> questionDtos = new LinkedList<McQuestionDTO>();

	Iterator<McQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = iter.next();
	    McQuestionDTO tempQuestion = new McQuestionDTO();

	    if ((!questionDto.getDisplayOrder().equals(originalQuestionIndex))
		    && !questionDto.getDisplayOrder().equals(replacedQuestionIndex)) {
		// normal copy
		tempQuestion = questionDto;

	    } else if (questionDto.getDisplayOrder().equals(originalQuestionIndex)) {
		// move type 1
		tempQuestion = replacedQuestion;

	    } else if (questionDto.getDisplayOrder().equals(replacedQuestionIndex)) {
		// move type 2
		tempQuestion = mainQuestion;
	    }

	    questionDtos.add(tempQuestion);
	}

	return questionDtos;
    }

    /*
     *Auxiliary method for moveQuestionDown() and moveQuestionUp()
     */
    private static List<McQuestionDTO> reorderQuestionDtos(List<McQuestionDTO> questionDTOs) {
	List<McQuestionDTO> tempQuestionDtos = new LinkedList<McQuestionDTO>();

	int queIndex = 0;
	Iterator<McQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = iter.next();

	    String question = questionDto.getQuestion();
	    String feedback = questionDto.getFeedback();
	    String mark = questionDto.getMark();

	    List<McOptionDTO> optionDtos = questionDto.getOptionDtos();
	    if ((question != null) && (!question.equals(""))) {
		++queIndex;

		questionDto.setQuestion(question);
		questionDto.setDisplayOrder(queIndex);
		questionDto.setFeedback(feedback);
		questionDto.setOptionDtos(optionDtos);
		questionDto.setMark(mark);
		tempQuestionDtos.add(questionDto);
	    }
	}

	return tempQuestionDtos;
    }

    public ActionForward saveQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	String mark = request.getParameter("mark");

	List<McOptionDTO> options = McAction.repopulateOptionDTOs(request, false);
	
	//remove blank options
	List<McOptionDTO> optionsWithoutEmptyOnes = new LinkedList<McOptionDTO>();
	for (McOptionDTO optionDTO : options) {
	    String optionText = optionDTO.getCandidateAnswer();
	    if ((optionText != null) && (optionText.length() > 0)) {
		optionsWithoutEmptyOnes.add(optionDTO);
	    }
	}
	options = optionsWithoutEmptyOnes;

	List<McQuestionDTO> questionDTOs = (List<McQuestionDTO>) sessionMap.get(McAppConstants.QUESTION_DTOS);

	String newQuestion = request.getParameter("newQuestion");
	String feedback = request.getParameter("feedback");
	Integer questionIndex = WebUtil.readIntParam(request, "questionIndex", true);
	mcAuthoringForm.setQuestionIndex(questionIndex);

	if ((newQuestion != null) && (newQuestion.length() > 0)) {
	    // adding new question
	    if (questionIndex == null) {

		boolean duplicates = AuthoringUtil.checkDuplicateQuestions(questionDTOs, newQuestion);
		if (!duplicates) {
		    
		    //finding max displayOrder
		    int maxDisplayOrder = 0;
		    for (McQuestionDTO questionDTO : questionDTOs) {
			int displayOrder = new Integer(questionDTO.getDisplayOrder());
			if (displayOrder > maxDisplayOrder) {
			    maxDisplayOrder = displayOrder;
			}
		    }

		    McQuestionDTO questionDTO = new McQuestionDTO();
		    questionDTO.setQuestion(newQuestion);
		    questionDTO.setFeedback(feedback);
		    questionDTO.setDisplayOrder(maxDisplayOrder + 1);
		    questionDTO.setOptionDtos(options);
		    questionDTO.setMark(mark);

		    questionDTOs.add(questionDTO);
		} else {
		    // duplicate question entry, not adding
		}

	    // updating existing question
	    } else {
		McQuestionDTO questionDto = null;
		for (McQuestionDTO questionDtoIter : questionDTOs) {
		    Integer displayOrder = questionDtoIter.getDisplayOrder();

		    if ((displayOrder != null) && displayOrder.equals(questionIndex)) {
			questionDto = questionDtoIter;
			break;
		    }
		}

		questionDto.setQuestion(newQuestion);
		questionDto.setFeedback(feedback);
		questionDto.setDisplayOrder(questionIndex);
		questionDto.setOptionDtos(options);
		questionDto.setMark(mark);
	    }
	} else {
	    // entry blank, not adding
	}

	request.setAttribute(McAppConstants.QUESTION_DTOS, questionDTOs);
	sessionMap.put(McAppConstants.QUESTION_DTOS, questionDTOs);

	return (mapping.findForward("itemList"));
    }

    /**
     * Parses questions extracted from IMS QTI file and adds them to currently edited question.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionForward saveQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// big part of code was taken from addSingleQuestion() and saveQuestion() methods
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);

	List<McQuestionDTO> questionDtos = (List<McQuestionDTO>) sessionMap.get(McAppConstants.QUESTION_DTOS);
	// proper parsing
	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);

	for (Question question : questions) {
	    // quietly do same verification as in other question-adding methods
	    String questionText = question.getText();
	    if (StringUtils.isBlank(questionText)) {
		logger.warn("Skipping a blank question.");
		continue;
	    }

	    questionText = QuestionParser.processHTMLField(questionText, false, contentFolderID,
		    question.getResourcesFolderPath());

	    if (AuthoringUtil.checkDuplicateQuestions(questionDtos, questionText)) {
		logger.warn("Skipping duplicate question: " + questionText);
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
			logger.warn("Skipping a blank answer");
			continue;
		    }
		    if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			logger
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
			    logger.warn(
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
		logger.warn("No correct answer found for question: " + questionText);
		continue;
	    }

	    McQuestionDTO questionDto = new McQuestionDTO();
	    questionDto.setDisplayOrder(questionDtos.size() + 1);
	    questionDto.setQuestion(questionText);
	    questionDto.setFeedback(QuestionParser.processHTMLField(question.getFeedback(), true, null, null));
	    questionDto.setOptionDtos(optionDtos);
	    questionDto.setMark(correctAnswerScore.toString());

	    questionDtos.add(questionDto);

	    if (logger.isDebugEnabled()) {
		logger.debug("Added question: " + questionText);
	    }
	}

	sessionMap.put(McAppConstants.QUESTION_DTOS, questionDtos);

	return mapping.findForward("itemList");
    }

    /**
     * Prepares MC questions for QTI packing
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ActionForward exportQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	List<McQuestionDTO> questionDtos = (List<McQuestionDTO>) sessionMap.get(McAppConstants.QUESTION_DTOS);
	List<Question> questions = new LinkedList<Question>();

	for (McQuestionDTO mcQuestion : questionDtos) {
	    Question question = new Question();

	    question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);
	    question.setTitle("Question " + mcQuestion.getDisplayOrder());
	    question.setText(mcQuestion.getQuestion());
	    question.setFeedback(mcQuestion.getFeedback());
	    List<Answer> answers = new ArrayList<Answer>();

	    for (McOptionDTO mcAnswer : mcQuestion.getOptionDtos()) {
		Answer answer = new Answer();
		answer.setText(mcAnswer.getCandidateAnswer());
		answer.setScore(
			"Correct".equalsIgnoreCase(mcAnswer.getCorrect()) ? Float.parseFloat(mcQuestion.getMark()) : 0);

		answers.add(answer);
		question.setAnswers(answers);
	    }

	    // put the question in the right place
	    questions.add(mcQuestion.getDisplayOrder() - 1, question);
	}

	String title = request.getParameter("title");
	QuestionExporter exporter = new QuestionExporter(title, questions.toArray(Question.QUESTION_ARRAY_TYPE));
	exporter.exportQTIPackage(request, response);

	return null;
    }

    public ActionForward moveCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);

	//moveAddedCandidateUp
	McQuestionDTO questionDto = (McQuestionDTO) sessionMap.get(McAppConstants.QUESTION_DTO);
	List<McOptionDTO> listCandidates = new LinkedList<McOptionDTO>();
	listCandidates = McAction.swapOptions(optionDtos, candidateIndex, "up");
	questionDto.setOptionDtos(listCandidates);
	sessionMap.put(McAppConstants.QUESTION_DTO, questionDto);

	return (mapping.findForward("candidateAnswersList"));
    }

    public ActionForward moveCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);

	//moveAddedCandidateDown
	McQuestionDTO questionDto = (McQuestionDTO) sessionMap.get(McAppConstants.QUESTION_DTO);
	List<McOptionDTO> swapedOptions = new LinkedList<McOptionDTO>();
	swapedOptions = McAction.swapOptions(optionDtos, candidateIndex, "down");
	questionDto.setOptionDtos(swapedOptions);
	sessionMap.put(McAppConstants.QUESTION_DTO, questionDto);

	return (mapping.findForward("candidateAnswersList"));
    }
    
    /*
     * swaps options in the list
     */
    private static List<McOptionDTO> swapOptions(List<McOptionDTO> optionDtos, String optionIndex, String direction) {

	int intOptionIndex = new Integer(optionIndex).intValue();
	int intOriginalOptionIndex = intOptionIndex;

	int replacedOptionIndex = 0;
	if (direction.equals("down")) {
	    replacedOptionIndex = ++intOptionIndex;
	} else {
	    replacedOptionIndex = --intOptionIndex;
	}

	McOptionDTO mainOption = AuthoringUtil.getOptionAtDisplayOrder(optionDtos, intOriginalOptionIndex);
	McOptionDTO replacedOption = AuthoringUtil.getOptionAtDisplayOrder(optionDtos, replacedOptionIndex);
	if ((mainOption == null) || (replacedOption == null)) {
	    return optionDtos;
	}

	List<McOptionDTO> newOptionDtos = new LinkedList<McOptionDTO>();

	int queIndex = 1;
	for (McOptionDTO option : optionDtos) {

	    McOptionDTO tempOption = new McOptionDTO();
	    if ((!new Integer(queIndex).toString().equals(new Integer(intOriginalOptionIndex).toString()))
		    && !new Integer(queIndex).toString().equals(new Integer(replacedOptionIndex).toString())) {
		// normal copy
		tempOption = option;
	    } else if (new Integer(queIndex).toString().equals(new Integer(intOriginalOptionIndex).toString())) {
		// move type 1
		tempOption = replacedOption;
	    } else if (new Integer(queIndex).toString().equals(new Integer(replacedOptionIndex).toString())) {
		// move type 2
		tempOption = mainOption;
	    }

	    newOptionDtos.add(tempOption);
	    queIndex++;
	}

	return newOptionDtos;
    }

    public ActionForward removeCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	String candidateIndexToRemove = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndexToRemove);

	// removeAddedCandidate
	McQuestionDTO questionDto = (McQuestionDTO) sessionMap.get(McAppConstants.QUESTION_DTO);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, false);
	List<McOptionDTO> listFinalCandidatesDTO = new LinkedList<McOptionDTO>();
	int caIndex = 0;
	for (McOptionDTO mcOptionDTO : optionDtos) {
	    caIndex++;

	    if (caIndex != new Integer(candidateIndexToRemove).intValue()) {
		listFinalCandidatesDTO.add(mcOptionDTO);
	    }
	}

	questionDto.setOptionDtos(listFinalCandidatesDTO);
	sessionMap.put(McAppConstants.QUESTION_DTO, questionDto);

	return (mapping.findForward("candidateAnswersList"));
    }

    public ActionForward newCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	String sessionMapId = request.getParameter(McAppConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(McAppConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	String candidateIndex = request.getParameter("candidateIndex");
	request.setAttribute("candidateIndex", candidateIndex);

	List<McOptionDTO> optionDtos = McAction.repopulateOptionDTOs(request, true);

	//newAddedCandidateBox
	McQuestionDTO questionDto = (McQuestionDTO) sessionMap.get(McAppConstants.QUESTION_DTO);
	questionDto.setOptionDtos(optionDtos);
	sessionMap.put(McAppConstants.QUESTION_DTO, questionDto);

	return (mapping.findForward("candidateAnswersList"));
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
