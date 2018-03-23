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

package org.lamsfoundation.lams.tool.assessment.web.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOverallFeedback;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUnit;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentForm;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentQuestionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * @author Andrey Balan
 */
public class AuthoringAction extends Action {

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Assessment Author functions ---------------------------
	if (param.equals("start")) {
	    ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	    request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    IAssessmentService service = getAssessmentService();
	    Assessment assessment = service.getAssessmentByContentId(contentId);

	    assessment.setDefineLater(true);
	    service.saveOrUpdateAssessment(assessment);

	    //audit log the teacher has started editing activity in monitor
	    service.auditLogStartEditingActivityInMonitor(contentId);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	// ----------------------- Add assessment question functions ---------------------------
	if (param.equals("newQuestionInit")) {
	    return newQuestionInit(mapping, form, request, response);
	}
	if (param.equals("editQuestion")) {
	    return editQuestion(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateQuestion")) {
	    return saveOrUpdateQuestion(mapping, form, request, response);
	}
	if (param.equals("saveQTI")) {
	    return saveQTI(mapping, form, request, response);
	}
	if (param.equals("exportQTI")) {
	    return exportQTI(mapping, form, request, response);
	}
	if (param.equals("removeQuestion")) {
	    return removeQuestion(mapping, form, request, response);
	}
	// ----------------------- Question Reference functions ---------------------------
	if (param.equals("addQuestionReference")) {
	    return addQuestionReference(mapping, form, request, response);
	}
	if (param.equals("removeQuestionReference")) {
	    return removeQuestionReference(mapping, form, request, response);
	}
	if (param.equals("upQuestionReference")) {
	    return upQuestionReference(mapping, form, request, response);
	}
	if (param.equals("downQuestionReference")) {
	    return downQuestionReference(mapping, form, request, response);
	}
	// ----------------------- Import/Export Questions functions ---------------------------
	if (param.equals("importInit")) {
	    return importInit(mapping, form, request, response);
	}
	if (param.equals("importQuestions")) {
	    return importQuestions(mapping, form, request, response);
	}
	if (param.equals("exportQuestions")) {
	    return exportQuestions(mapping, form, request, response);
	}
	// -----------------------Assessment Answer Option functions ---------------------------
	if (param.equals("addOption")) {
	    return addOption(mapping, form, request, response);
	}
	if (param.equals("removeOption")) {
	    return removeOption(mapping, form, request, response);
	}
	if (param.equals("upOption")) {
	    return upOption(mapping, form, request, response);
	}
	if (param.equals("downOption")) {
	    return downOption(mapping, form, request, response);
	}
	// -----------------------Assessment Unit functions ---------------------------
	if (param.equals("newUnit")) {
	    return newUnit(mapping, form, request, response);
	}
	// -----------------------Assessment Overall Feedback functions ---------------------------
	if (param.equals("initOverallFeedback")) {
	    return initOverallFeedback(mapping, form, request, response);
	}
	if (param.equals("newOverallFeedback")) {
	    return newOverallFeedback(mapping, form, request, response);
	}

	return mapping.findForward(AssessmentConstants.ERROR);
    }

    /**
     * Read assessment data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
     *
     * @throws ServletException
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, AssessmentConstants.PARAM_TOOL_CONTENT_ID));

	// get back the assessment and question list and display them on page
	IAssessmentService service = getAssessmentService();

	List<AssessmentQuestion> questions = null;
	Assessment assessment = null;
	AssessmentForm assessmentForm = (AssessmentForm) form;

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	assessmentForm.setSessionMapID(sessionMap.getSessionID());

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	assessmentForm.setContentFolderID(contentFolderID);

	try {
	    assessment = service.getAssessmentByContentId(contentId);
	    // if assessment does not exist, try to use default content instead.
	    if (assessment == null) {
		assessment = service.getDefaultContent(contentId);
	    }

	    assessmentForm.setAssessment(assessment);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	if (assessment.getQuestions() != null) {
	    questions = new ArrayList<AssessmentQuestion>(assessment.getQuestions());
	} else {
	    questions = new ArrayList<>();
	}

	// init assessment question list
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	questionList.clear();
	questionList.addAll(questions);

	// init question references
	SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);
	references.clear();
	if (assessment.getQuestionReferences() != null) {
	    references.addAll(assessment.getQuestionReferences());
	}

	// init available questions
	reinitializeAvailableQuestions(sessionMap);

	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT_FORM, assessmentForm);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	AssessmentForm existForm = (AssessmentForm) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT_FORM);

	AssessmentForm assessmentForm = (AssessmentForm) form;
	try {
	    PropertyUtils.copyProperties(assessmentForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all assessment question, information
     * etc.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	AssessmentForm assessmentForm = (AssessmentForm) (form);

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(assessmentForm.getSessionMapID());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	Assessment assessment = assessmentForm.getAssessment();
	IAssessmentService service = getAssessmentService();

	// **********************************Get Assessment PO*********************
	Assessment assessmentPO = service.getAssessmentByContentId(assessmentForm.getAssessment().getContentId());

	//allow using old and modified questions and references altogether
	if (mode.isTeacher()) {
	    for (AssessmentQuestion question : (Set<AssessmentQuestion>) assessment.getQuestions()) {
		service.releaseFromCache(question);
	    }
	    for (QuestionReference reference : (Set<QuestionReference>) assessment.getQuestionReferences()) {
		service.releaseFromCache(reference);
	    }
	}

	Set<AssessmentQuestion> oldQuestions = (assessmentPO == null) ? new HashSet<>() : assessmentPO.getQuestions();
	Set<QuestionReference> oldReferences = (assessmentPO == null) ? new HashSet<>()
		: assessmentPO.getQuestionReferences();
	AssessmentUser assessmentUser = null;

	if (assessmentPO == null) {
	    // new Assessment, create it.
	    assessmentPO = assessment;
	    assessmentPO.setCreated(new Timestamp(new Date().getTime()));

	} else {
	    Long uid = assessmentPO.getUid();
	    assessmentUser = assessmentPO.getCreatedBy();
	    PropertyUtils.copyProperties(assessmentPO, assessment);

	    // copyProperties() above may result in "collection assigned to two objects in a session" exception
	    service.releaseFromCache(assessment);
	    assessmentForm.setAssessment(null);
	    assessment = null;
	    // set back UID
	    assessmentPO.setUid(uid);

	    // if it is Teacher (from monitor) - change define later status
	    if (mode.isTeacher()) {
		assessmentPO.setDefineLater(false);
		assessmentPO.setUpdated(new Timestamp(new Date().getTime()));
	    }

	}

	// *******************************Handle user*******************
	if (assessmentUser == null) {
	    // try to get form system session
	    HttpSession ss = SessionManager.getSession();
	    // get back login user DTO
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    assessmentUser = service.getUserCreatedAssessment(new Long(user.getUserID().intValue()),
		    assessmentPO.getContentId());
	    if (assessmentUser == null) {
		assessmentUser = new AssessmentUser(user, assessmentPO);
	    }
	}
	assessmentPO.setCreatedBy(assessmentUser);

	// ************************* Handle assessment questions *******************
	// Handle assessment questions
	Set<AssessmentQuestion> questions = new LinkedHashSet<>();
	Set<AssessmentQuestion> newQuestions = getQuestionList(sessionMap);
	for (AssessmentQuestion question : newQuestions) {
	    removeNewLineCharacters(question);
	    questions.add(question);
	}
	assessmentPO.setQuestions(questions);

	List<AssessmentQuestion> deletedQuestions = getDeletedQuestionList(sessionMap);
	Set<QuestionReference> newReferences = updateQuestionReferencesGrades(request, sessionMap, true);
	List<QuestionReference> deletedReferences = getDeletedQuestionReferences(sessionMap);
	//recalculate results in case content is edited from monitoring
	if (mode.isTeacher()) {
	    service.recalculateUserAnswers(assessmentPO, oldQuestions, newQuestions, deletedQuestions, oldReferences,
		    newReferences, deletedReferences);
	}

	// delete References from database.
	Iterator<QuestionReference> iterRef = deletedReferences.iterator();
	while (iterRef.hasNext()) {
	    QuestionReference reference = iterRef.next();
	    iterRef.remove();
	    if (reference.getUid() != null) {
		service.deleteQuestionReference(reference.getUid());
	    }
	}

	// delete Questions from database.
	Iterator<AssessmentQuestion> iter = deletedQuestions.iterator();
	while (iter.hasNext()) {
	    AssessmentQuestion question = iter.next();
	    iter.remove();
	    if (question.getUid() != null) {
		service.deleteAssessmentQuestion(question.getUid());
	    }
	}

	// Handle question references
	assessmentPO.setQuestionReferences(newReferences);

	// ************************* Handle assessment overall feedbacks *******************
	TreeSet<AssessmentOverallFeedback> overallFeedbackList = getOverallFeedbacksFromForm(request, true);
	assessmentPO.setOverallFeedbacks(overallFeedbackList);

	// **********************************************
	// finally persist assessmentPO again
	service.saveOrUpdateAssessment(assessmentPO);

	assessmentForm.setAssessment(assessmentPO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return mapping.findForward("author");
    }

    /**
     * Display empty page for new assessment question.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newQuestionInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	updateQuestionReferencesGrades(request, sessionMap, false);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	AssessmentQuestionForm questionForm = (AssessmentQuestionForm) form;
	questionForm.setSessionMapID(sessionMapID);
	questionForm.setContentFolderID(contentFolderID);
	questionForm.setDefaultGrade("1");
	questionForm.setPenaltyFactor("0");
	questionForm.setAnswerRequired(true);

	List<AssessmentQuestionOption> optionList = new ArrayList<>();
	for (int i = 0; i < AssessmentConstants.INITIAL_OPTIONS_NUMBER; i++) {
	    AssessmentQuestionOption option = new AssessmentQuestionOption();
	    option.setSequenceId(i + 1);
	    option.setGrade(0);
	    optionList.add(option);
	}
	request.setAttribute(AssessmentConstants.ATTR_OPTION_LIST, optionList);

	List<AssessmentUnit> unitList = new ArrayList<>();
	AssessmentUnit unit = new AssessmentUnit();
	unit.setSequenceId(1);
	unit.setMultiplier(1);
	unitList.add(unit);
	for (int i = 1; i < AssessmentConstants.INITIAL_UNITS_NUMBER; i++) {
	    unit = new AssessmentUnit();
	    unit.setSequenceId(i + 1);
	    unit.setMultiplier(0);
	    unitList.add(unit);
	}
	request.setAttribute(AssessmentConstants.ATTR_UNIT_LIST, unitList);

	short type = (short) NumberUtils.toInt(request.getParameter(AssessmentConstants.ATTR_QUESTION_TYPE));
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_TYPE, type);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return findForward(type, mapping);
    }

    /**
     * Display edit page for existed assessment question.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	updateQuestionReferencesGrades(request, sessionMap, false);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);

	int questionIdx = NumberUtils.toInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_INDEX), -1);
	AssessmentQuestion question = null;
	if (questionIdx != -1) {
	    SortedSet<AssessmentQuestion> assessmentList = getQuestionList(sessionMap);
	    List<AssessmentQuestion> rList = new ArrayList<>(assessmentList);
	    question = rList.get(questionIdx);
	    if (question != null) {
		AssessmentQuestionForm questionForm = (AssessmentQuestionForm) form;
		populateQuestionToForm(questionIdx, question, questionForm, request);
		questionForm.setContentFolderID(contentFolderID);
	    }
	}
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_TYPE, question.getType());
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return findForward(question == null ? -1 : question.getType(), mapping);
    }

    /**
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveOrUpdateQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	AssessmentQuestionForm questionForm = (AssessmentQuestionForm) form;
	extractFormToAssessmentQuestion(request, questionForm);

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(questionForm.getSessionMapID());
	reinitializeAvailableQuestions(sessionMap);

	// set session map ID so that questionlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, questionForm.getSessionMapID());
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Parses questions extracted from IMS QTI file and adds them as new items.
     *
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("rawtypes")
    private ActionForward saveQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UnsupportedEncodingException {
	String sessionMapId = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);

	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);
	for (Question question : questions) {
	    AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
	    int maxSeq = 0;
	    if ((questionList != null) && (questionList.size() > 0)) {
		AssessmentQuestion last = questionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    assessmentQuestion.setSequenceId(maxSeq);
	    assessmentQuestion.setTitle(question.getTitle());
	    assessmentQuestion.setQuestion(QuestionParser.processHTMLField(question.getText(), false, contentFolderID,
		    question.getResourcesFolderPath()));
	    assessmentQuestion.setGeneralFeedback(QuestionParser.processHTMLField(question.getFeedback(), false,
		    contentFolderID, question.getResourcesFolderPath()));
	    assessmentQuestion.setPenaltyFactor(0);

	    int questionGrade = 1;

	    // options are different depending on the type
	    if (Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType())
		    || Question.QUESTION_TYPE_FILL_IN_BLANK.equals(question.getType())
		    || Question.QUESTION_TYPE_MARK_HEDGING.equals(question.getType())) {
		boolean isMultipleChoice = Question.QUESTION_TYPE_MULTIPLE_CHOICE.equals(question.getType());
		boolean isMarkHedgingType = Question.QUESTION_TYPE_MARK_HEDGING.equals(question.getType());

		// setting answers is very similar in both types, so they were put together here
		if (isMarkHedgingType) {
		    assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MARK_HEDGING);

		} else if (isMultipleChoice) {
		    assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE);
		    assessmentQuestion.setMultipleAnswersAllowed(false);
		    assessmentQuestion.setShuffle(false);

		} else {
		    assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER);
		    assessmentQuestion.setCaseSensitive(false);
		}

		String correctAnswer = null;
		if (question.getAnswers() != null) {
		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
		    int orderId = 0;
		    for (Answer answer : question.getAnswers()) {
			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());
			if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			    AuthoringAction.log
				    .warn("Skipping an answer with same text as the correct answer: " + answerText);
			    continue;
			}
			AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			assessmentAnswer.setOptionString(answerText);
			assessmentAnswer.setSequenceId(orderId++);
			assessmentAnswer.setFeedback(answer.getFeedback());

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // for fill in blanks question all answers are correct and get full grade
			    if (!isMultipleChoice && !isMarkHedgingType || correctAnswer == null) {
				// whatever the correct answer holds, it becomes the question score
				questionGrade = new Double(Math.ceil(answer.getScore())).intValue();
				// 100% goes to the correct answer
				assessmentAnswer.setGrade(1);
				correctAnswer = answerText;
			    } else {
				AuthoringAction.log
					.warn("Choosing only first correct answer, despite another one was found: "
						+ answerText);
				assessmentAnswer.setGrade(0);
			    }
			} else {
			    assessmentAnswer.setGrade(0);
			}

			optionList.add(assessmentAnswer);
		    }

		    assessmentQuestion.setOptions(optionList);
		}

		if (correctAnswer == null) {
		    AuthoringAction.log.warn("No correct answer found for question: " + question.getText());
		    continue;
		}

	    } else if (Question.QUESTION_TYPE_MULTIPLE_RESPONSE.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE);
		assessmentQuestion.setMultipleAnswersAllowed(true);
		assessmentQuestion.setShuffle(false);

		if (question.getAnswers() != null) {
		    float totalScore = 0;
		    for (Answer answer : question.getAnswers()) {
			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // the question score information is stored as sum of answer scores
			    totalScore += answer.getScore();
			}
		    }
		    questionGrade = new Double(Math.round(totalScore)).intValue();

		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
		    int orderId = 1;
		    for (Answer answer : question.getAnswers()) {
			String answerText = answer.getText();
			AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			assessmentAnswer.setOptionString(answerText);
			assessmentAnswer.setSequenceId(orderId++);
			assessmentAnswer.setFeedback(answer.getFeedback());

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // set the factor of score for correct answers
			    assessmentAnswer.setGrade(answer.getScore() / totalScore);
			} else {
			    assessmentAnswer.setGrade(0);
			}

			optionList.add(assessmentAnswer);
		    }

		    assessmentQuestion.setOptions(optionList);
		}

	    } else if (Question.QUESTION_TYPE_TRUE_FALSE.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_TRUE_FALSE);

		if (question.getAnswers() == null) {
		    AuthoringAction.log.warn("Answers missing from true-false question: " + question.getText());
		    continue;
		} else {
		    for (Answer answer : question.getAnswers()) {
			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    assessmentQuestion.setCorrectAnswer(Boolean.parseBoolean(answer.getText()));
			    questionGrade = new Double(Math.ceil(answer.getScore())).intValue();
			}
			if (!StringUtils.isBlank(answer.getFeedback())) {
			    // set feedback for true/false answers
			    if (Boolean.parseBoolean(answer.getText())) {
				assessmentQuestion.setFeedbackOnCorrect(answer.getFeedback());
			    } else {
				assessmentQuestion.setFeedbackOnIncorrect(answer.getFeedback());
			    }
			}
		    }
		}
	    } else if (Question.QUESTION_TYPE_MATCHING.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS);
		assessmentQuestion.setShuffle(true);

		if (question.getAnswers() != null) {
		    // the question score information is stored as sum of answer scores
		    float totalScore = 0;
		    for (Answer answer : question.getAnswers()) {
			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    totalScore += answer.getScore();
			}
		    }
		    questionGrade = new Double(Math.round(totalScore)).intValue();

		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
		    int orderId = 1;
		    for (int answerIndex = 0; answerIndex < question.getAnswers().size(); answerIndex++) {
			// QTI allows answers without a match, but LAMS assessment tool does not
			Integer matchAnswerIndex = question.getMatchMap() == null ? null
				: question.getMatchMap().get(answerIndex);
			Answer matchAnswer = (matchAnswerIndex == null) || (question.getMatchAnswers() == null) ? null
				: question.getMatchAnswers().get(matchAnswerIndex);
			if (matchAnswer != null) {
			    Answer answer = question.getAnswers().get(answerIndex);
			    String answerText = answer.getText();
			    AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			    assessmentAnswer.setQuestion(answerText);
			    assessmentAnswer.setOptionString(matchAnswer.getText());
			    assessmentAnswer.setSequenceId(orderId++);
			    assessmentAnswer.setFeedback(answer.getFeedback());

			    optionList.add(assessmentAnswer);
			}
		    }

		    assessmentQuestion.setOptions(optionList);
		}
	    } else if (Question.QUESTION_TYPE_ESSAY.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_ESSAY);
		assessmentQuestion.setAllowRichEditor(false);

	    } else if (Question.QUESTION_TYPE_ESSAY.equals(question.getType())) {
		assessmentQuestion.setType(AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE);
		assessmentQuestion.setShuffle(false);

		String correctAnswer = null;
		if (question.getAnswers() != null) {
		    TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
		    int orderId = 1;
		    for (Answer answer : question.getAnswers()) {
			String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
				question.getResourcesFolderPath());
			if ((correctAnswer != null) && correctAnswer.equals(answerText)) {
			    AuthoringAction.log
				    .warn("Skipping an answer with same text as the correct answer: " + answerText);
			    continue;
			}
			AssessmentQuestionOption assessmentAnswer = new AssessmentQuestionOption();
			assessmentAnswer.setOptionString(answerText);
			assessmentAnswer.setSequenceId(orderId++);
			assessmentAnswer.setFeedback(answer.getFeedback());

			if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			    // for fill in blanks question all answers are correct and get full grade
			    if (correctAnswer == null) {
				// whatever the correct answer holds, it becomes the question score
				questionGrade = new Double(Math.ceil(answer.getScore())).intValue();
				// 100% goes to the correct answer
				assessmentAnswer.setGrade(1);
				correctAnswer = answerText;
			    } else {
				AuthoringAction.log
					.warn("Choosing only first correct answer, despite another one was found: "
						+ answerText);
				assessmentAnswer.setGrade(0);
			    }
			} else {
			    assessmentAnswer.setGrade(0);
			}

			optionList.add(assessmentAnswer);
		    }

		    assessmentQuestion.setOptions(optionList);
		}

		if (correctAnswer == null) {
		    AuthoringAction.log.warn("No correct answer found for question: " + question.getText());
		    continue;
		}

	    } else {
		AuthoringAction.log.warn("Unknow QTI question type: " + question.getType());
		continue;
	    }

	    assessmentQuestion.setDefaultGrade(questionGrade);

	    questionList.add(assessmentQuestion);
	    if (AuthoringAction.log.isDebugEnabled()) {
		AuthoringAction.log.debug("Added question: " + assessmentQuestion.getTitle());
	    }
	}

	reinitializeAvailableQuestions(sessionMap);

	// set session map ID so that questionlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Prepares Assessment content for QTI packing
     */
    @SuppressWarnings("rawtypes")
    private ActionForward exportQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UnsupportedEncodingException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	List<Question> questions = new LinkedList<>();
	for (AssessmentQuestion assessmentQuestion : questionList) {
	    Question question = new Question();
	    List<Answer> answers = new ArrayList<>();

	    switch (assessmentQuestion.getType()) {

		case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:

		    if (assessmentQuestion.isMultipleAnswersAllowed()) {
			question.setType(Question.QUESTION_TYPE_MULTIPLE_RESPONSE);
			int correctAnswerCount = 0;

			for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			    if (assessmentAnswer.getGrade() > 0) {
				correctAnswerCount++;
			    }
			}

			Float correctAnswerScore = correctAnswerCount > 0
				? new Integer(100 / correctAnswerCount).floatValue()
				: null;
			int incorrectAnswerCount = assessmentQuestion.getOptions().size() - correctAnswerCount;
			Float incorrectAnswerScore = incorrectAnswerCount > 0
				? new Integer(-100 / incorrectAnswerCount).floatValue()
				: null;

			for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			    Answer answer = new Answer();
			    boolean isCorrectAnswer = assessmentAnswer.getGrade() > 0;

			    answer.setText(assessmentAnswer.getOptionString());
			    answer.setScore(isCorrectAnswer ? correctAnswerScore : incorrectAnswerScore);
			    answer.setFeedback(isCorrectAnswer ? assessmentQuestion.getFeedbackOnCorrect()
				    : assessmentQuestion.getFeedbackOnIncorrect());

			    answers.add(assessmentAnswer.getSequenceId(), answer);
			}

		    } else {
			question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);

			for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			    Answer answer = new Answer();
			    boolean isCorrectAnswer = assessmentAnswer.getGrade() == 1F;

			    answer.setText(assessmentAnswer.getOptionString());
			    answer.setScore(
				    isCorrectAnswer ? new Integer(assessmentQuestion.getDefaultGrade()).floatValue()
					    : 0);
			    answer.setFeedback(isCorrectAnswer ? assessmentQuestion.getFeedbackOnCorrect()
				    : assessmentQuestion.getFeedbackOnIncorrect());

			    answers.add(assessmentAnswer.getSequenceId(), answer);
			}
		    }
		    break;

		case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
		    question.setType(Question.QUESTION_TYPE_FILL_IN_BLANK);

		    for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			// only answer which has more than 0% is considered a correct one
			if (assessmentAnswer.getGrade() > 0) {
			    Answer answer = new Answer();
			    answer.setText(assessmentAnswer.getOptionString());
			    answer.setScore(new Integer(assessmentQuestion.getDefaultGrade()).floatValue());

			    answers.add(answer);
			}
		    }
		    break;

		case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
		    question.setType(Question.QUESTION_TYPE_TRUE_FALSE);
		    boolean isTrueCorrect = assessmentQuestion.getCorrectAnswer();

		    // true/false question is basically the same for QTI, just with special answers
		    Answer trueAnswer = new Answer();
		    trueAnswer.setText("True");
		    trueAnswer.setScore(
			    isTrueCorrect ? new Integer(assessmentQuestion.getDefaultGrade()).floatValue() : 0);
		    trueAnswer.setFeedback(isTrueCorrect ? assessmentQuestion.getFeedbackOnCorrect()
			    : assessmentQuestion.getFeedbackOnIncorrect());
		    answers.add(trueAnswer);

		    Answer falseAnswer = new Answer();
		    falseAnswer.setText("False");
		    falseAnswer.setScore(
			    !isTrueCorrect ? new Integer(assessmentQuestion.getDefaultGrade()).floatValue() : 0);
		    falseAnswer.setFeedback(!isTrueCorrect ? assessmentQuestion.getFeedbackOnCorrect()
			    : assessmentQuestion.getFeedbackOnIncorrect());
		    answers.add(falseAnswer);
		    break;

		case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
		    question.setType(Question.QUESTION_TYPE_MATCHING);

		    int answerIndex = 0;
		    float score = assessmentQuestion.getDefaultGrade() / assessmentQuestion.getOptions().size();
		    question.setMatchAnswers(new ArrayList<Answer>(assessmentQuestion.getOptions().size()));
		    question.setMatchMap(new TreeMap<Integer, Integer>());
		    for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			Answer answer = new Answer();

			answer.setText(assessmentAnswer.getQuestion());
			answer.setScore(score);
			answer.setFeedback(assessmentAnswer.getFeedback());
			answers.add(answer);

			Answer matchingAnswer = new Answer();
			matchingAnswer.setText(assessmentAnswer.getOptionString());
			question.getMatchAnswers().add(matchingAnswer);
			question.getMatchMap().put(answerIndex, answerIndex);
			answerIndex++;
		    }

		    break;

		case AssessmentConstants.QUESTION_TYPE_ESSAY:
		    // not much to do with essay
		    question.setType(Question.QUESTION_TYPE_ESSAY);
		    answers = null;
		    break;

		case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:

		    question.setType(Question.QUESTION_TYPE_MARK_HEDGING);

		    for (AssessmentQuestionOption assessmentAnswer : assessmentQuestion.getOptions()) {
			Answer answer = new Answer();
			boolean isCorrectAnswer = assessmentAnswer.isCorrect();

			answer.setText(assessmentAnswer.getOptionString());
			answer.setScore(
				isCorrectAnswer ? new Integer(assessmentQuestion.getDefaultGrade()).floatValue() : 0);
			answer.setFeedback(isCorrectAnswer ? assessmentQuestion.getFeedbackOnCorrect()
				: assessmentQuestion.getFeedbackOnIncorrect());

			answers.add(assessmentAnswer.getSequenceId(), answer);
		    }
		    break;

		default:
		    continue;
	    }

	    question.setTitle(assessmentQuestion.getTitle());
	    question.setText(assessmentQuestion.getQuestion());
	    question.setFeedback(assessmentQuestion.getGeneralFeedback());
	    question.setAnswers(answers);

	    questions.add(question);
	}

	String title = request.getParameter("title");
	QuestionExporter exporter = new QuestionExporter(title, questions.toArray(Question.QUESTION_ARRAY_TYPE));
	exporter.exportQTIPackage(request, response);

	return null;
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	updateQuestionReferencesGrades(request, sessionMap, false);

	int questionIdx = NumberUtils.toInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_INDEX), -1);
	if (questionIdx != -1) {
	    SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	    List<AssessmentQuestion> rList = new ArrayList<>(questionList);
	    AssessmentQuestion question = rList.remove(questionIdx);
	    questionList.clear();
	    questionList.addAll(rList);
	    // add to delList
	    List<AssessmentQuestion> delList = getDeletedQuestionList(sessionMap);
	    delList.add(question);

	    // remove according questionReference, if exists
	    SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);
	    QuestionReference questionReferenceToDelete = null;
	    for (QuestionReference questionReference : questionReferences) {
		if ((questionReference.getQuestion() != null)
			&& (questionReference.getQuestion().getSequenceId() == question.getSequenceId())) {
		    questionReferenceToDelete = questionReference;
		}
	    }
	    // check if we need to delete random question reference
	    if ((questionReferenceToDelete == null) && (questionReferences.size() > questionList.size())) {
		// find the first random question
		for (QuestionReference questionReference : questionReferences) {
		    if (questionReference.isRandomQuestion()) {
			questionReferenceToDelete = questionReference;
			break;
		    }
		}
	    }
	    if (questionReferenceToDelete != null) {
		questionReferences.remove(questionReferenceToDelete);
		// add to delList
		List<QuestionReference> delReferencesList = getDeletedQuestionReferences(sessionMap);
		delReferencesList.add(questionReferenceToDelete);
	    }

	}

	reinitializeAvailableQuestions(sessionMap);

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward addQuestionReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	updateQuestionReferencesGrades(request, sessionMap, false);

	SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);
	int questionIdx = NumberUtils.toInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_INDEX), -1);

	// set SequenceId
	QuestionReference reference = new QuestionReference();
	int maxSeq = 1;
	if ((references != null) && (references.size() > 0)) {
	    QuestionReference last = references.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	reference.setSequenceId(maxSeq);

	// set isRandomQuestion
	boolean isRandomQuestion = (questionIdx == -1);
	reference.setRandomQuestion(isRandomQuestion);

	if (isRandomQuestion) {
	    reference.setDefaultGrade(1);
	} else {
	    SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	    AssessmentQuestion question = null;
	    for (AssessmentQuestion questionFromList : questionList) {
		if (questionFromList.getSequenceId() == questionIdx) {
		    question = questionFromList;
		    break;
		}
	    }
	    reference.setQuestion(question);

	    reference.setDefaultGrade(question.getDefaultGrade());
	}
	references.add(reference);

	reinitializeAvailableQuestions(sessionMap);

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeQuestionReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	updateQuestionReferencesGrades(request, sessionMap, false);

	int questionReferenceIdx = NumberUtils
		.toInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_REFERENCE_INDEX), -1);
	if (questionReferenceIdx != -1) {
	    SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);
	    List<QuestionReference> rList = new ArrayList<>(questionReferences);
	    QuestionReference questionReference = rList.remove(questionReferenceIdx);
	    questionReferences.clear();
	    questionReferences.addAll(rList);
	    // add to delList
	    List<QuestionReference> delList = getDeletedQuestionReferences(sessionMap);
	    delList.add(questionReference);
	}

	reinitializeAvailableQuestions(sessionMap);

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Move up current question reference.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upQuestionReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchQuestionReferences(mapping, request, true);
    }

    /**
     * Move down current question reference.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downQuestionReference(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchQuestionReferences(mapping, request, false);
    }

    private ActionForward switchQuestionReferences(ActionMapping mapping, HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	updateQuestionReferencesGrades(request, sessionMap, false);

	int questionReferenceIdx = NumberUtils
		.toInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_REFERENCE_INDEX), -1);
	if (questionReferenceIdx != -1) {
	    SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);
	    List<QuestionReference> rList = new ArrayList<>(references);
	    // get current and the target item, and switch their sequnece
	    QuestionReference reference = rList.get(questionReferenceIdx);
	    QuestionReference repReference;
	    if (up) {
		repReference = rList.get(--questionReferenceIdx);
	    } else {
		repReference = rList.get(++questionReferenceIdx);
	    }
	    int upSeqId = repReference.getSequenceId();
	    repReference.setSequenceId(reference.getSequenceId());
	    reference.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    references.clear();
	    references.addAll(rList);
	}

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Initializes import questions page.
     */
    private ActionForward importInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Imports questions into question bank from uploaded xml file.
     */
    private ActionForward importQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	SortedSet<AssessmentQuestion> oldQuestions = getQuestionList(sessionMap);

	List<String> toolsErrorMsgs = new ArrayList<>();
	try {
	    File designFile = null;
	    Map<String, String> params = new HashMap<>();
	    String filename = null;

	    String uploadPath = FileUtil.createTempDirectory("_uploaded_2questions_xml");

	    DiskFileUpload fu = new DiskFileUpload();
	    // maximum size that will be stored in memory
	    fu.setSizeThreshold(4096);
	    // the location for saving data that is larger than getSizeThreshold()
	    // fu.setRepositoryPath(uploadPath);

	    List fileItems = fu.parseRequest(request);
	    Iterator iter = fileItems.iterator();
	    while (iter.hasNext()) {
		FileItem fi = (FileItem) iter.next();
		// UPLOAD_FILE is input field from HTML page
		if (!fi.getFieldName().equalsIgnoreCase("UPLOAD_FILE")) {
		    params.put(fi.getFieldName(), fi.getString());
		} else {
		    // filename on the client
		    filename = FileUtil.getFileName(fi.getName());
		    designFile = new File(uploadPath + filename);
		    fi.write(designFile);
		}
	    }

	    String filename2 = designFile.getName();
	    String fileExtension = (filename2 != null) && (filename2.length() >= 4)
		    ? filename2.substring(filename2.length() - 4)
		    : "";
	    if (!fileExtension.equalsIgnoreCase(".xml")) {
		throw new RuntimeException("Wrong file extension. Xml is expected");
	    }
	    // String learningDesignPath = ZipFileUtil.expandZip(new FileInputStream(designFile), filename2);

	    // import learning design
	    String fullFilePath = designFile.getAbsolutePath();// FileUtil.getFullPath(learningDesignPath,
							       // ExportToolContentService.LEARNING_DESIGN_FILE_NAME);
	    List<AssessmentQuestion> questions = (List<AssessmentQuestion>) FileUtil.getObjectFromXML(null,
		    fullFilePath);
	    if (questions != null) {
		for (AssessmentQuestion question : questions) {
		    int maxSeq = 1;
		    if ((oldQuestions != null) && (oldQuestions.size() > 0)) {
			AssessmentQuestion last = oldQuestions.last();
			maxSeq = last.getSequenceId() + 1;
		    }
		    question.setSequenceId(maxSeq);
		    oldQuestions.add(question);
		}
	    }

	} catch (Exception e) {
	    AuthoringAction.log.error("Error occured during import", e);
	    toolsErrorMsgs.add(e.getClass().getName() + " " + e.getMessage());
	}

	if (toolsErrorMsgs.size() > 0) {
	    request.setAttribute("toolsErrorMessages", toolsErrorMsgs);
	}

	reinitializeAvailableQuestions(sessionMap);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Exports xml format questions from question bank.
     */
    private ActionForward exportQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	AssessmentForm assessmentForm = (AssessmentForm) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT_FORM);
	Assessment assessment = assessmentForm.getAssessment();

	String errors = null;
	if (assessment != null) {
	    try {
		ArrayList<AssessmentQuestion> questionsToExport = new ArrayList<>();

		for (AssessmentQuestion question : getQuestionList(sessionMap)) {
		    AssessmentQuestion clonedQuestion = (AssessmentQuestion) question.clone();
		    questionsToExport.add(clonedQuestion);
		}
		// exporting XML
		XStream designXml = new XStream(new StaxDriver());
		designXml.addPermission(AnyTypePermission.ANY);
		String resultedXml = designXml.toXML(questionsToExport);

		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition",
			"attachment;filename=" + AssessmentConstants.EXPORT_QUESTIONS_FILENAME);
		AuthoringAction.log.debug("Exporting assessment questions to an xml: " + assessment.getContentId());

		OutputStream out = null;
		try {
		    out = response.getOutputStream();
		    out.write(resultedXml.getBytes());
		    int count = resultedXml.getBytes().length;
		    AuthoringAction.log.debug("Wrote out " + count + " bytes");
		    response.setContentLength(count);
		    out.flush();
		} catch (Exception e) {
		    AuthoringAction.log.error("Exception occured writing out file:" + e.getMessage());
		    throw new ExportToolContentException(e);
		} finally {
		    try {
			if (out != null) {
			    out.close();
			}
		    } catch (Exception e) {
			AuthoringAction.log
				.error("Error Closing file. File already written out - no exception being thrown.", e);
		    }
		}

	    } catch (Exception e) {
		errors = "Unable to export tool content: " + e.toString();
		AuthoringAction.log.error(errors);
	    }

	}
	if (errors != null) {
	    try {
		PrintWriter out = response.getWriter();
		out.write(errors);
		out.flush();
	    } catch (IOException e) {
	    }
	}
	return null;
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward addOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	TreeSet<AssessmentQuestionOption> optionList = getOptionsFromRequest(request, false);
	AssessmentQuestionOption option = new AssessmentQuestionOption();
	int maxSeq = 1;
	if ((optionList != null) && (optionList.size() > 0)) {
	    AssessmentQuestionOption last = optionList.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	option.setSequenceId(maxSeq);
	option.setGrade(0);
	optionList.add(option);

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_TYPE,
		WebUtil.readIntParam(request, AssessmentConstants.ATTR_QUESTION_TYPE));
	request.setAttribute(AssessmentConstants.ATTR_OPTION_LIST, optionList);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Ajax call, remove the given line of instruction of resource item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Set<AssessmentQuestionOption> optionList = getOptionsFromRequest(request, false);
	int optionIndex = NumberUtils.toInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<AssessmentQuestionOption> rList = new ArrayList<>(optionList);
	    AssessmentQuestionOption question = rList.remove(optionIndex);
	    optionList.clear();
	    optionList.addAll(rList);
	}

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_TYPE,
		WebUtil.readIntParam(request, AssessmentConstants.ATTR_QUESTION_TYPE));
	request.setAttribute(AssessmentConstants.ATTR_OPTION_LIST, optionList);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Move up current option.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchOption(mapping, request, true);
    }

    /**
     * Move down current option.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchOption(mapping, request, false);
    }

    private ActionForward switchOption(ActionMapping mapping, HttpServletRequest request, boolean up) {
	Set<AssessmentQuestionOption> optionList = getOptionsFromRequest(request, false);

	int optionIndex = NumberUtils.toInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<AssessmentQuestionOption> rList = new ArrayList<>(optionList);

	    // get current and the target item, and switch their sequnece
	    AssessmentQuestionOption option = rList.get(optionIndex);
	    AssessmentQuestionOption repOption;
	    if (up) {
		repOption = rList.get(--optionIndex);
	    } else {
		repOption = rList.get(++optionIndex);
	    }

	    int upSeqId = repOption.getSequenceId();
	    repOption.setSequenceId(option.getSequenceId());
	    option.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    optionList.clear();
	    optionList.addAll(rList);
	}

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_TYPE,
		WebUtil.readIntParam(request, AssessmentConstants.ATTR_QUESTION_TYPE));
	request.setAttribute(AssessmentConstants.ATTR_OPTION_LIST, optionList);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Ajax call, will add one more input line for new Unit.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	TreeSet<AssessmentUnit> unitList = getUnitsFromRequest(request, false);
	AssessmentUnit unit = new AssessmentUnit();
	int maxSeq = 1;
	if ((unitList != null) && (unitList.size() > 0)) {
	    AssessmentUnit last = unitList.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	unit.setSequenceId(maxSeq);
	unitList.add(unit);

	request.setAttribute(AssessmentConstants.ATTR_UNIT_LIST, unitList);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward initOverallFeedback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	AssessmentForm assessmentForm = (AssessmentForm) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT_FORM);
	Assessment assessment = assessmentForm.getAssessment();

	// initial Overall feedbacks list
	SortedSet<AssessmentOverallFeedback> overallFeedbackList = new TreeSet<>(new SequencableComparator());
	if (!assessment.getOverallFeedbacks().isEmpty()) {
	    overallFeedbackList.addAll(assessment.getOverallFeedbacks());
	} else {
	    for (int i = 1; i <= AssessmentConstants.INITIAL_OVERALL_FEEDBACK_NUMBER; i++) {
		AssessmentOverallFeedback overallFeedback = new AssessmentOverallFeedback();
		if (i == 1) {
		    overallFeedback.setGradeBoundary(100);
		}
		overallFeedback.setSequenceId(i);
		overallFeedbackList.add(overallFeedback);
	    }
	}

	request.setAttribute(AssessmentConstants.ATTR_OVERALL_FEEDBACK_LIST, overallFeedbackList);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Ajax call, will add one more input line for new OverallFeedback.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newOverallFeedback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	TreeSet<AssessmentOverallFeedback> overallFeedbackList = getOverallFeedbacksFromRequest(request, false);
	AssessmentOverallFeedback overallFeedback = new AssessmentOverallFeedback();
	int maxSeq = 1;
	if ((overallFeedbackList != null) && (overallFeedbackList.size() > 0)) {
	    AssessmentOverallFeedback last = overallFeedbackList.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	overallFeedback.setSequenceId(maxSeq);
	overallFeedbackList.add(overallFeedback);

	request.setAttribute(AssessmentConstants.ATTR_OVERALL_FEEDBACK_LIST, overallFeedbackList);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * refreshes set of all available questions for adding to question list
     *
     * @param sessionMap
     */
    private void reinitializeAvailableQuestions(SessionMap<String, Object> sessionMap) {
	SortedSet<AssessmentQuestion> bankQuestions = getQuestionList(sessionMap);
	SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);
	Set<AssessmentQuestion> questionsFromList = new LinkedHashSet<>();
	for (QuestionReference reference : references) {
	    questionsFromList.add(reference.getQuestion());
	}

	Set<AssessmentQuestion> availableQuestions = new TreeSet<>(new SequencableComparator());
	availableQuestions.addAll(CollectionUtils.subtract(bankQuestions, questionsFromList));

	sessionMap.put(AssessmentConstants.ATTR_AVAILABLE_QUESTIONS, availableQuestions);
    }

    /**
     * Return AssessmentService bean.
     */
    private IAssessmentService getAssessmentService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
    }

    /**
     * List save current assessment questions.
     *
     * @param request
     * @return
     */
    private SortedSet<AssessmentQuestion> getQuestionList(SessionMap<String, Object> sessionMap) {
	SortedSet<AssessmentQuestion> list = (SortedSet<AssessmentQuestion>) sessionMap
		.get(AssessmentConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<>(new SequencableComparator());
	    sessionMap.put(AssessmentConstants.ATTR_QUESTION_LIST, list);
	}
	return list;
    }

    /**
     * List save current question references.
     *
     * @param request
     * @return
     */
    private SortedSet<QuestionReference> getQuestionReferences(SessionMap<String, Object> sessionMap) {
	SortedSet<QuestionReference> list = (SortedSet<QuestionReference>) sessionMap
		.get(AssessmentConstants.ATTR_QUESTION_REFERENCES);
	if (list == null) {
	    list = new TreeSet<>(new SequencableComparator());
	    sessionMap.put(AssessmentConstants.ATTR_QUESTION_REFERENCES, list);
	}
	return list;
    }

    /**
     * List save deleted assessment questions, which could be persisted or non-persisted questions.
     *
     * @param request
     * @return
     */
    private List<AssessmentQuestion> getDeletedQuestionList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, AssessmentConstants.ATTR_DELETED_QUESTION_LIST);
    }

    /**
     * List save deleted assessment questions, which could be persisted or non-persisted questions.
     *
     * @param request
     * @return
     */
    private List<QuestionReference> getDeletedQuestionReferences(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, AssessmentConstants.ATTR_DELETED_QUESTION_REFERENCES);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     *
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * Get back relative <code>ActionForward</code> from request.
     *
     * @param type
     * @param mapping
     * @return
     */
    private ActionForward findForward(short type, ActionMapping mapping) {
	ActionForward forward;
	switch (type) {
	    case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:
		forward = mapping.findForward("multiplechoice");
		break;
	    case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
		forward = mapping.findForward("matchingpairs");
		break;
	    case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
		forward = mapping.findForward("shortanswer");
		break;
	    case AssessmentConstants.QUESTION_TYPE_NUMERICAL:
		forward = mapping.findForward("numerical");
		break;
	    case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
		forward = mapping.findForward("truefalse");
		break;
	    case AssessmentConstants.QUESTION_TYPE_ESSAY:
		forward = mapping.findForward("essay");
		break;
	    case AssessmentConstants.QUESTION_TYPE_ORDERING:
		forward = mapping.findForward("ordering");
		break;
	    case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:
		forward = mapping.findForward("markhedging");
		break;
	    default:
		forward = null;
		break;
	}
	return forward;
    }

    /**
     * This method will populate assessment question information to its form for edit use.
     *
     * @param questionIdx
     * @param question
     * @param form
     * @param request
     */
    private void populateQuestionToForm(int questionIdx, AssessmentQuestion question, AssessmentQuestionForm form,
	    HttpServletRequest request) {
	form.setTitle(question.getTitle());
	form.setQuestion(question.getQuestion());
	form.setDefaultGrade(String.valueOf(question.getDefaultGrade()));
	form.setPenaltyFactor(String.valueOf(question.getPenaltyFactor()));
	form.setAnswerRequired(question.isAnswerRequired());
	form.setGeneralFeedback(question.getGeneralFeedback());
	form.setFeedback(question.getFeedback());
	form.setMultipleAnswersAllowed(question.isMultipleAnswersAllowed());
	form.setIncorrectAnswerNullifiesMark(question.isIncorrectAnswerNullifiesMark());
	form.setFeedbackOnCorrect(question.getFeedbackOnCorrect());
	form.setFeedbackOnPartiallyCorrect(question.getFeedbackOnPartiallyCorrect());
	form.setFeedbackOnIncorrect(question.getFeedbackOnIncorrect());
	form.setShuffle(question.isShuffle());
	form.setCaseSensitive(question.isCaseSensitive());
	form.setCorrectAnswer(question.getCorrectAnswer());
	form.setAllowRichEditor(question.isAllowRichEditor());
	form.setMaxWordsLimit(question.getMaxWordsLimit());
	form.setMinWordsLimit(question.getMinWordsLimit());
	form.setHedgingJustificationEnabled(question.isHedgingJustificationEnabled());
	if (questionIdx >= 0) {
	    form.setQuestionIndex(new Integer(questionIdx).toString());
	}

	short questionType = question.getType();
	if ((questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING)) {
	    Set<AssessmentQuestionOption> optionList = question.getOptions();
	    request.setAttribute(AssessmentConstants.ATTR_OPTION_LIST, optionList);
	}
	if (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    Set<AssessmentUnit> unitList = question.getUnits();
	    request.setAttribute(AssessmentConstants.ATTR_UNIT_LIST, unitList);
	}
    }

    /**
     * Extract web form content to assessment question.
     *
     * @param request
     * @param optionList
     * @param questionForm
     * @throws AssessmentApplicationException
     */
    private void extractFormToAssessmentQuestion(HttpServletRequest request, AssessmentQuestionForm questionForm) {
	/*
	 * BE CAREFUL: This method will copy nessary info from request form to an old or new AssessmentQuestion
	 * instance. It
	 * gets all info EXCEPT AssessmentQuestion.createDate, which need be set when
	 * persisting
	 * this assessment Question.
	 */
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(questionForm.getSessionMapID());
	// check whether it is "edit(old Question)" or "add(new Question)"
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	int questionIdx = NumberUtils.toInt(questionForm.getQuestionIndex(), -1);
	AssessmentQuestion question = null;

	if (questionIdx == -1) { // add
	    question = new AssessmentQuestion();
	    int maxSeq = 1;
	    if ((questionList != null) && (questionList.size() > 0)) {
		AssessmentQuestion last = questionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    question.setSequenceId(maxSeq);
	    questionList.add(question);
	} else { // edit
	    List<AssessmentQuestion> rList = new ArrayList<>(questionList);
	    question = rList.get(questionIdx);
	}
	short type = questionForm.getQuestionType();
	question.setType(questionForm.getQuestionType());

	question.setTitle(questionForm.getTitle());
	question.setQuestion(questionForm.getQuestion());

	question.setDefaultGrade(Integer.parseInt(questionForm.getDefaultGrade()));
	question.setGeneralFeedback(questionForm.getGeneralFeedback());
	question.setAnswerRequired(questionForm.isAnswerRequired());

	if (type == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
	    question.setMultipleAnswersAllowed(questionForm.isMultipleAnswersAllowed());
	    boolean incorrectAnswerNullifiesMark = questionForm.isMultipleAnswersAllowed()
		    ? questionForm.isIncorrectAnswerNullifiesMark()
		    : false;
	    question.setIncorrectAnswerNullifiesMark(incorrectAnswerNullifiesMark);
	    question.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    question.setShuffle(questionForm.isShuffle());
	    question.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    question.setFeedbackOnPartiallyCorrect(questionForm.getFeedbackOnPartiallyCorrect());
	    question.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	} else if ((type == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS)) {
	    question.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    question.setShuffle(questionForm.isShuffle());
	} else if ((type == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)) {
	    question.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    question.setCaseSensitive(questionForm.isCaseSensitive());
	} else if ((type == AssessmentConstants.QUESTION_TYPE_NUMERICAL)) {
	    question.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	} else if ((type == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE)) {
	    question.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    question.setCorrectAnswer(questionForm.isCorrectAnswer());
	    question.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    question.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	} else if ((type == AssessmentConstants.QUESTION_TYPE_ESSAY)) {
	    question.setAllowRichEditor(questionForm.isAllowRichEditor());
	    question.setMaxWordsLimit(questionForm.getMaxWordsLimit());
	    question.setMinWordsLimit(questionForm.getMinWordsLimit());
	} else if (type == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    question.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    question.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    question.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	} else if (type == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {
	    question.setShuffle(questionForm.isShuffle());
	    question.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    question.setFeedbackOnPartiallyCorrect(questionForm.getFeedbackOnPartiallyCorrect());
	    question.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	    question.setHedgingJustificationEnabled(questionForm.isHedgingJustificationEnabled());
	}

	// set options
	if ((type == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		|| (type == AssessmentConstants.QUESTION_TYPE_ORDERING)
		|| (type == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS)
		|| (type == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)
		|| (type == AssessmentConstants.QUESTION_TYPE_NUMERICAL)
		|| (type == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING)) {
	    Set<AssessmentQuestionOption> optionList = getOptionsFromRequest(request, true);
	    Set<AssessmentQuestionOption> options = new LinkedHashSet<>();
	    int seqId = 0;
	    for (AssessmentQuestionOption option : optionList) {
		option.setSequenceId(seqId++);
		options.add(option);
	    }
	    question.setOptions(options);
	}
	// set units
	if (type == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    Set<AssessmentUnit> unitList = getUnitsFromRequest(request, true);
	    Set<AssessmentUnit> units = new LinkedHashSet<>();
	    int seqId = 0;
	    for (AssessmentUnit unit : unitList) {
		unit.setSequenceId(seqId++);
		units.add(unit);
	    }
	    question.setUnits(units);
	}

    }

    private Set<QuestionReference> updateQuestionReferencesGrades(HttpServletRequest request,
	    SessionMap<String, Object> sessionMap, boolean isFormSubmit) {
	Map<String, String> paramMap = splitRequestParameter(request,
		AssessmentConstants.ATTR_QUESTION_REFERENCES_GRADES);

	SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);
	for (QuestionReference questionReference : questionReferences) {
	    try {
		int grade;
		if (isFormSubmit) {
		    grade = WebUtil.readIntParam(request,
			    AssessmentConstants.PARAM_GRADE + questionReference.getSequenceId());
		} else {
		    String gradeStr = paramMap.get(AssessmentConstants.PARAM_GRADE + questionReference.getSequenceId());
		    grade = new Integer(gradeStr);
		}

		questionReference.setDefaultGrade(grade);
	    } catch (Exception e) {
		AuthoringAction.log.debug(e.getMessage());
	    }
	}

	return questionReferences;
    }

    /**
     * Get answer options from <code>HttpRequest</code>
     *
     * @param request
     * @param isForSaving
     *            whether the blank options will be preserved or not
     */
    private TreeSet<AssessmentQuestionOption> getOptionsFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, AssessmentConstants.ATTR_OPTION_LIST);

	int count = NumberUtils.toInt(paramMap.get(AssessmentConstants.ATTR_OPTION_COUNT));
	int questionType = WebUtil.readIntParam(request, AssessmentConstants.ATTR_QUESTION_TYPE);
	Integer correctOptionIndex = (paramMap.get(AssessmentConstants.ATTR_OPTION_CORRECT) == null) ? null
		: NumberUtils.toInt(paramMap.get(AssessmentConstants.ATTR_OPTION_CORRECT));
	TreeSet<AssessmentQuestionOption> optionList = new TreeSet<>(new SequencableComparator());
	for (int i = 0; i < count; i++) {
	    if ((questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		    || (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)) {
		String optionString = paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i);
		if ((optionString == null) && isForSaving) {
		    continue;
		}

		AssessmentQuestionOption option = new AssessmentQuestionOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.toInt(sequenceId));
		option.setOptionString(optionString);
		float grade = Float.valueOf(paramMap.get(AssessmentConstants.ATTR_OPTION_GRADE_PREFIX + i));
		option.setGrade(grade);
		option.setFeedback(paramMap.get(AssessmentConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
		optionList.add(option);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		String question = paramMap.get(AssessmentConstants.ATTR_OPTION_QUESTION_PREFIX + i);
		if ((question == null) && isForSaving) {
		    continue;
		}

		AssessmentQuestionOption option = new AssessmentQuestionOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.toInt(sequenceId));
		option.setOptionString(paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i));
		option.setQuestion(question);
		optionList.add(option);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
		String optionFloatStr = paramMap.get(AssessmentConstants.ATTR_OPTION_FLOAT_PREFIX + i);
		String acceptedErrorStr = paramMap.get(AssessmentConstants.ATTR_OPTION_ACCEPTED_ERROR_PREFIX + i);
		String gradeStr = paramMap.get(AssessmentConstants.ATTR_OPTION_GRADE_PREFIX + i);
		if (optionFloatStr.equals("0.0") && optionFloatStr.equals("0.0") && gradeStr.equals("0.0")
			&& isForSaving) {
		    continue;
		}

		AssessmentQuestionOption option = new AssessmentQuestionOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.toInt(sequenceId));
		try {
		    float optionFloat = Float.valueOf(optionFloatStr);
		    option.setOptionFloat(optionFloat);
		} catch (Exception e) {
		    option.setOptionFloat(0);
		}
		try {
		    float acceptedError = Float.valueOf(acceptedErrorStr);
		    option.setAcceptedError(acceptedError);
		} catch (Exception e) {
		    option.setAcceptedError(0);
		}
		float grade = Float.valueOf(paramMap.get(AssessmentConstants.ATTR_OPTION_GRADE_PREFIX + i));
		option.setGrade(grade);
		option.setFeedback(paramMap.get(AssessmentConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
		optionList.add(option);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		String optionString = paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i);
		if ((optionString == null) && isForSaving) {
		    continue;
		}

		AssessmentQuestionOption option = new AssessmentQuestionOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.toInt(sequenceId));
		option.setOptionString(optionString);
		//TODO check the following line is not required
//		option.setAnswerInt(i);
		optionList.add(option);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {
		String optionString = paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i);
		if ((optionString == null) && isForSaving) {
		    continue;
		}

		AssessmentQuestionOption option = new AssessmentQuestionOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.toInt(sequenceId));
		option.setOptionString(optionString);
		if ((correctOptionIndex != null) && correctOptionIndex.equals(new Integer(sequenceId))) {
		    option.setCorrect(true);
		}
		option.setFeedback(paramMap.get(AssessmentConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
		optionList.add(option);
	    }
	}
	return optionList;
    }

    /**
     * Get units from <code>HttpRequest</code>
     *
     * @param request
     */
    private TreeSet<AssessmentUnit> getUnitsFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, AssessmentConstants.ATTR_UNIT_LIST);

	int count = NumberUtils.toInt(paramMap.get(AssessmentConstants.ATTR_UNIT_COUNT));
	TreeSet<AssessmentUnit> unitList = new TreeSet<>(new SequencableComparator());
	for (int i = 0; i < count; i++) {
	    String unitStr = paramMap.get(AssessmentConstants.ATTR_UNIT_UNIT_PREFIX + i);
	    if (StringUtils.isBlank(unitStr) && isForSaving) {
		continue;
	    }

	    AssessmentUnit unit = new AssessmentUnit();
	    String sequenceId = paramMap.get(AssessmentConstants.ATTR_UNIT_SEQUENCE_ID_PREFIX + i);
	    unit.setSequenceId(NumberUtils.toInt(sequenceId));
	    unit.setUnit(unitStr);
	    float multiplier = Float.valueOf(paramMap.get(AssessmentConstants.ATTR_UNIT_MULTIPLIER_PREFIX + i));
	    unit.setMultiplier(multiplier);
	    unitList.add(unit);
	}

	return unitList;
    }

    /**
     * Get overall feedbacks from <code>HttpRequest</code>
     *
     * @param request
     */
    private TreeSet<AssessmentOverallFeedback> getOverallFeedbacksFromRequest(HttpServletRequest request,
	    boolean skipBlankOverallFeedbacks) {
	int count = NumberUtils.toInt(request.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_COUNT));
	TreeSet<AssessmentOverallFeedback> overallFeedbackList = new TreeSet<>(new SequencableComparator());
	for (int i = 0; i < count; i++) {
	    String gradeBoundaryStr = request
		    .getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i);
	    String feedback = request.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_FEEDBACK_PREFIX + i);
	    String sequenceId = request.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_SEQUENCE_ID_PREFIX + i);

	    if ((StringUtils.isBlank(feedback) || StringUtils.isBlank(gradeBoundaryStr)) && skipBlankOverallFeedbacks) {
		continue;
	    }
	    AssessmentOverallFeedback overallFeedback = new AssessmentOverallFeedback();
	    overallFeedback.setSequenceId(NumberUtils.toInt(sequenceId));
	    if (!StringUtils.isBlank(gradeBoundaryStr)) {
		int gradeBoundary = NumberUtils.toInt(
			request.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i));
		overallFeedback.setGradeBoundary(gradeBoundary);
	    }
	    overallFeedback.setFeedback(feedback);
	    overallFeedbackList.add(overallFeedback);
	}
	return overallFeedbackList;
    }

    /**
     * Get overall feedbacks from <code>HttpRequest</code>
     *
     * @param request
     */
    private TreeSet<AssessmentOverallFeedback> getOverallFeedbacksFromForm(HttpServletRequest request,
	    boolean skipBlankOverallFeedbacks) {
	Map<String, String> paramMap = splitRequestParameter(request, AssessmentConstants.ATTR_OVERALL_FEEDBACK_LIST);

	int count = NumberUtils.toInt(paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_COUNT));
	TreeSet<AssessmentOverallFeedback> overallFeedbackList = new TreeSet<>(new SequencableComparator());
	for (int i = 0; i < count; i++) {
	    String gradeBoundaryStr = paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i);
	    String feedback = paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_FEEDBACK_PREFIX + i);
	    String sequenceId = paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_SEQUENCE_ID_PREFIX + i);

	    if ((StringUtils.isBlank(feedback) || StringUtils.isBlank(gradeBoundaryStr)) && skipBlankOverallFeedbacks) {
		continue;
	    }
	    AssessmentOverallFeedback overallFeedback = new AssessmentOverallFeedback();
	    overallFeedback.setSequenceId(NumberUtils.toInt(sequenceId));
	    if (!StringUtils.isBlank(gradeBoundaryStr)) {
		int gradeBoundary = NumberUtils
			.toInt(paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i));
		overallFeedback.setGradeBoundary(gradeBoundary);
	    }
	    overallFeedback.setFeedback(feedback);
	    overallFeedbackList.add(overallFeedback);
	}
	return overallFeedbackList;
    }

    /**
     * Split Request Parameter from <code>HttpRequest</code>
     *
     * @param request
     * @param parameterName
     *            parameterName
     */
    private Map<String, String> splitRequestParameter(HttpServletRequest request, String parameterName) {
	String list = request.getParameter(parameterName);
	if (list == null) {
	    return null;
	}

	String[] params = list.split("&");
	Map<String, String> paramMap = new HashMap<>();
	String[] pair;
	for (String item : params) {
	    pair = item.split("=");
	    if ((pair == null) || (pair.length != 2)) {
		continue;
	    }
	    try {
		paramMap.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		AuthoringAction.log.error("Error occurs when decode instruction string:" + e.toString());
	    }
	}
	return paramMap;
    }

    /**
     * Removes redundant new line characters from options left by CKEditor (otherwise it will break Javascript in
     * monitor)
     *
     * @param question
     */
    private void removeNewLineCharacters(AssessmentQuestion question) {
	Set<AssessmentQuestionOption> options = question.getOptions();
	if (options != null) {
	    for (AssessmentQuestionOption option : options) {
		String optionString = option.getOptionString();
		if (optionString != null) {
		    option.setOptionString(optionString.replaceAll("[\n\r\f]", ""));
		}

		String questionStr = option.getQuestion();
		if (questionStr != null) {
		    option.setQuestion(questionStr.replaceAll("[\n\r\f]", ""));
		}
	    }

	}
    }
}
