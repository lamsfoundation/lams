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

package org.lamsfoundation.lams.tool.assessment.web.controller;

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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.QTIUtil;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentForm;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentQuestionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    @Qualifier("laasseAssessmentService")
    private IAssessmentService service;

    /**
     * Read assessment data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
     */
    @RequestMapping("/start")
    public String start(@ModelAttribute("assessmentForm") AssessmentForm assessmentForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	return readDatabaseData(assessmentForm, request, mode);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute("assessmentForm") AssessmentForm assessmentForm,
	    HttpServletRequest request) throws ServletException {
	// update define later flag to true
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Assessment assessment = service.getAssessmentByContentId(contentId);

	assessment.setDefineLater(true);
	service.saveOrUpdateAssessment(assessment);

	//audit log the teacher has started editing activity in monitor
	service.auditLogStartEditingActivityInMonitor(contentId);

	return readDatabaseData(assessmentForm, request, ToolAccessMode.TEACHER);
    }

    /**
     * Common method for "start" and "defineLater"
     */
    private String readDatabaseData(AssessmentForm assessmentForm, HttpServletRequest request, ToolAccessMode mode)
	    throws ServletException {
	// save toolContentID into HTTPSession
	Long contentId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_TOOL_CONTENT_ID);

	List<AssessmentQuestion> questions = null;
	Assessment assessment = null;

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
	    log.error(e);
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

	boolean isAssessmentAttempted = assessment.getUid() == null ? false
		: service.isAssessmentAttempted(assessment.getUid());
	sessionMap.put(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED, isAssessmentAttempted && mode.isTeacher());
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT_FORM, assessmentForm);
	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    @RequestMapping("/init")
    public String init(@ModelAttribute("assessmentForm") AssessmentForm assessmentForm, HttpServletRequest request)
	    throws ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	AssessmentForm existForm = (AssessmentForm) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT_FORM);

	try {
	    PropertyUtils.copyProperties(assessmentForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all assessment question, information
     * etc.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/updateContent")
    public String updateContent(@ModelAttribute("assessmentForm") AssessmentForm assessmentForm,
	    HttpServletRequest request) throws Exception {
	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(assessmentForm.getSessionMapID());
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Assessment assessment = assessmentForm.getAssessment();
	Assessment assessmentPO = service.getAssessmentByContentId(assessmentForm.getAssessment().getContentId());

	Set<AssessmentQuestion> oldQuestions = (assessmentPO == null) ? new HashSet<>() : assessmentPO.getQuestions();
	Set<QuestionReference> oldReferences = (assessmentPO == null) ? new HashSet<>()
		: assessmentPO.getQuestionReferences();
	
	//allow using old and modified questions and references altogether
	if (mode.isTeacher()) {
	    for (AssessmentQuestion question : oldQuestions) {
		service.releaseFromCache(question);
	    }
	    for (QuestionReference reference : oldReferences) {
		service.releaseFromCache(reference);
	    }
	}
	
	AssessmentUser assessmentUser = null;

	if (assessmentPO == null) {
	    // new Assessment, create it
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
	    assessmentUser = service.getUserCreatedAssessment(user.getUserID().longValue(),
		    assessmentPO.getContentId());
	    if (assessmentUser == null) {
		assessmentUser = new AssessmentUser(user, assessmentPO);
	    }
	}
	assessmentPO.setCreatedBy(assessmentUser);

	// ************************* Handle assessment questions *******************
	Set<AssessmentQuestion> newQuestions = getQuestionList(sessionMap);
	for (AssessmentQuestion question : newQuestions) {
	    removeNewLineCharacters(question);
	}
	assessmentPO.setQuestions(newQuestions);

	Set<QuestionReference> newReferences = updateQuestionReferencesGrades(request, sessionMap, true);	
	
	//recalculate results in case content is edited from monitoring and it's been already attempted by a student
	boolean isAuthoringRestricted = (boolean) sessionMap.get(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED);
	if (isAuthoringRestricted) {
	    service.recalculateUserAnswers(assessmentPO.getUid(), assessmentPO.getContentId(), oldQuestions,
		    newQuestions, oldReferences, newReferences);
	}

	// delete References from database
	List<QuestionReference> deletedReferences = getDeletedQuestionReferences(sessionMap);
	Iterator<QuestionReference> iterRef = deletedReferences.iterator();
	while (iterRef.hasNext()) {
	    QuestionReference reference = iterRef.next();
	    iterRef.remove();
	    if (reference.getUid() != null) {
		service.deleteQuestionReference(reference.getUid());
	    }
	}

	// delete Questions from database
	List<AssessmentQuestion> deletedQuestions = getDeletedQuestionList(sessionMap);
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

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return "pages/authoring/authoring";
    }

    /**
     * Display empty page for new assessment question.
     */
    @RequestMapping("/newQuestionInit")
    public String newQuestionInit(@ModelAttribute("assessmentQuestionForm") AssessmentQuestionForm questionForm,
	    HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	updateQuestionReferencesGrades(request, sessionMap, false);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	questionForm.setSessionMapID(sessionMapID);
	questionForm.setSequenceId(-1);//which signifies it's a new question
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
	return findForward(type);
    }

    /**
     * Display edit page for existed assessment question.
     */
    @RequestMapping("/editQuestion")
    public String editQuestion(@ModelAttribute("assessmentQuestionForm") AssessmentQuestionForm questionForm,
	    HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	updateQuestionReferencesGrades(request, sessionMap, false);

	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	int questionSequenceId = WebUtil.readIntParam(request, AssessmentConstants.PARAM_QUESTION_SEQUENCE_ID);
	AssessmentQuestion question = null;
	for (AssessmentQuestion questionIter : questionList) {
	    if (questionIter.getSequenceId() == questionSequenceId) {
		question = questionIter;
		break;
	    }
	}
	if (question == null) {
	    throw new RuntimeException("Question with sequenceId:" + questionSequenceId + " was not found!");
	}
	populateQuestionToForm(question, questionForm, request);

	sessionMap.put(AssessmentConstants.ATTR_QUESTION_TYPE, question.getType());
	
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	questionForm.setContentFolderID(contentFolderID);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return findForward(question.getType());
    }

    /**
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/saveOrUpdateQuestion")
    public String saveOrUpdateQuestion(@ModelAttribute("assessmentQuestionForm") AssessmentQuestionForm questionForm,
	    HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(questionForm.getSessionMapID());
	boolean isAuthoringRestricted = (boolean) sessionMap.get(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED);
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	
	// check whether it is "edit(old Question)" or "add(new Question)"
	extractFormToAssessmentQuestion(request, questionList, questionForm, isAuthoringRestricted);

	reinitializeAvailableQuestions(sessionMap);

	// set session map ID so that questionlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, questionForm.getSessionMapID());
	
	//in case of edit in monitor and at least one attempted user, we show authoring page with restricted options 
	if (isAuthoringRestricted) {
	    return "pages/authoring/parts/questionlistRestricted";    
	} else {
	    return "pages/authoring/parts/questionlist";
	}
    }

    /**
     * Parses questions extracted from IMS QTI file and adds them as new items.
     */
    @RequestMapping("/saveQTI")
    public String saveQTI(HttpServletRequest request) throws UnsupportedEncodingException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);

	QTIUtil.saveQTI(request, questionList, contentFolderID);

	reinitializeAvailableQuestions(sessionMap);
	return "pages/authoring/parts/questionlist";
    }

    /**
     * Prepares Assessment content for QTI packing
     */
    @RequestMapping("/exportQTI")
    public String exportQTI(HttpServletRequest request, HttpServletResponse response)
	    throws UnsupportedEncodingException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	
	List<Question> questions = QTIUtil.exportQTI(questionList);

	String title = request.getParameter("title");
	QuestionExporter exporter = new QuestionExporter(title, questions.toArray(Question.QUESTION_ARRAY_TYPE));
	exporter.exportQTIPackage(request, response);

	return null;
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping("/removeQuestion")
    public String removeQuestion(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	updateQuestionReferencesGrades(request, sessionMap, false);

	int deletedQuestionSequenceId = NumberUtils.toInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_SEQUENCE_ID), -1);
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	Iterator<AssessmentQuestion> iterator = questionList.iterator();
	AssessmentQuestion deletedQuestion = null;
	while (iterator.hasNext()) {
	    AssessmentQuestion questionIter = iterator.next();
	    if (questionIter.getSequenceId() == deletedQuestionSequenceId) {
		deletedQuestion = questionIter;
		iterator.remove();
	    }
	}
	
	// add to delList
	List<AssessmentQuestion> delList = getDeletedQuestionList(sessionMap);
	delList.add(deletedQuestion);

	// remove according questionReference, if exists
	SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);
	QuestionReference questionReferenceToDelete = null;
	for (QuestionReference questionReference : questionReferences) {
	    if ((questionReference.getQuestion() != null)
		    && (questionReference.getQuestion().getSequenceId() == deletedQuestionSequenceId)) {
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

	reinitializeAvailableQuestions(sessionMap);
	return "pages/authoring/parts/questionlist";
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping("/addQuestionReference")
    public String addQuestionReference(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
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

	return "pages/authoring/parts/questionlist";
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping("/removeQuestionReference")
    public String removeQuestionReference(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
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

	return "pages/authoring/parts/questionlist";
    }

    /**
     * Move up current question reference.
     */
    @RequestMapping("/upQuestionReference")
    public String upQuestionReference(HttpServletRequest request) {
	return switchQuestionReferences(request, true);
    }

    /**
     * Move down current question reference.
     */
    @RequestMapping("/downQuestionReference")
    public String downQuestionReference(HttpServletRequest request) {
	return switchQuestionReferences(request, false);
    }

    private String switchQuestionReferences(HttpServletRequest request, boolean up) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
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

	//in case of edit in monitor and at least one attempted user, we show authoring page with restricted options
	boolean isAuthoringRestricted = (boolean) sessionMap.get(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED);
	if (isAuthoringRestricted) {
	    return "pages/authoring/parts/questionlistRestricted";    
	} else {
	    return "pages/authoring/parts/questionlist";
	}
    }

    /**
     * Initializes import questions page.
     */
    @RequestMapping("/importInit")
    public String importInit(HttpServletRequest request) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	return "pages/authoring/importQuestions";
    }

    /**
     * Imports questions into question bank from uploaded xml file.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/importQuestions")
    public String importQuestions(@RequestParam("UPLOAD_FILE") MultipartFile file, HttpServletRequest request) throws ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<AssessmentQuestion> oldQuestions = getQuestionList(sessionMap);

	List<String> toolsErrorMsgs = new ArrayList<>();
	try {
	    String uploadPath = FileUtil.createTempDirectory("_uploaded_2questions_xml");

	    // filename on the client
	    String filename = FileUtil.getFileName(file.getOriginalFilename());
	    File destinationFile = new File(uploadPath, filename);
	    file.transferTo(destinationFile);

	    String fileExtension = FileUtil.getFileExtension(filename);
	    if (!fileExtension.equalsIgnoreCase("xml")) {
		throw new RuntimeException("Wrong file extension. Xml is expected");
	    }
	    // String learningDesignPath = ZipFileUtil.expandZip(new FileInputStream(designFile), filename2);

	    // import learning design
	    String fullFilePath = destinationFile.getAbsolutePath();// FileUtil.getFullPath(learningDesignPath,
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
	    log.error("Error occured during import", e);
	    toolsErrorMsgs.add(e.getClass().getName() + " " + e.getMessage());
	}

	if (toolsErrorMsgs.size() > 0) {
	    request.setAttribute("toolsErrorMessages", toolsErrorMsgs);
	}

	reinitializeAvailableQuestions(sessionMap);
	return "pages/authoring/parts/questionlist";
    }

    /**
     * Exports xml format questions from question bank.
     */
    @RequestMapping("/exportQuestions")
    public String exportQuestions(HttpServletRequest request, HttpServletResponse response) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

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
		log.debug("Exporting assessment questions to an xml: " + assessment.getContentId());

		OutputStream out = null;
		try {
		    out = response.getOutputStream();
		    out.write(resultedXml.getBytes());
		    int count = resultedXml.getBytes().length;
		    log.debug("Wrote out " + count + " bytes");
		    response.setContentLength(count);
		    out.flush();
		} catch (Exception e) {
		    log.error("Exception occured writing out file:" + e.getMessage());
		    throw new ExportToolContentException(e);
		} finally {
		    try {
			if (out != null) {
			    out.close();
			}
		    } catch (Exception e) {
			log.error("Error Closing file. File already written out - no exception being thrown.", e);
		    }
		}

	    } catch (Exception e) {
		errors = "Unable to export tool content: " + e.toString();
		log.error(errors);
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
     */
    @RequestMapping("/addOption")
    public String addOption(HttpServletRequest request) {
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
	return "pages/authoring/parts/optionlist";
    }

    /**
     * Ajax call, remove the given line of instruction of resource item.
     */
    @RequestMapping("/removeOption")
    public String removeOption(HttpServletRequest request) {
	Set<AssessmentQuestionOption> optionList = getOptionsFromRequest(request, false);
	int optionIndex = NumberUtils.toInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<AssessmentQuestionOption> rList = new ArrayList<>(optionList);
	    rList.remove(optionIndex);
	    optionList.clear();
	    optionList.addAll(rList);
	}

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_TYPE,
		WebUtil.readIntParam(request, AssessmentConstants.ATTR_QUESTION_TYPE));
	request.setAttribute(AssessmentConstants.ATTR_OPTION_LIST, optionList);
	return "pages/authoring/parts/optionlist";
    }

    /**
     * Move up current option.
     */
    @RequestMapping("/upOption")
    public String upOption(HttpServletRequest request) {
	return switchOption(request, true);
    }

    /**
     * Move down current option.
     */
    @RequestMapping("/downOption")
    public String downOption(HttpServletRequest request) {
	return switchOption(request, false);
    }

    private String switchOption(HttpServletRequest request, boolean up) {
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
	return "pages/authoring/parts/optionlist";
    }

    /**
     * Ajax call, will add one more input line for new Unit.
     */
    @RequestMapping("/newUnit")
    public String newUnit(HttpServletRequest request) {
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
	return "pages/authoring/parts/unitlist";
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    @RequestMapping("/initOverallFeedback")
    public String initOverallFeedback(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
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
	return "pages/authoring/parts/overallfeedbacklist";
    }

    /**
     * Ajax call, will add one more input line for new OverallFeedback.
     */
    @RequestMapping("/newOverallFeedback")
    public String newOverallFeedback(HttpServletRequest request) {
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
	return "pages/authoring/parts/overallfeedbacklist";
    }

    // *************************************************************************************
    // Private methods
    // *************************************************************************************

    /**
     * refreshes set of all available questions for adding to question list
     */
    @SuppressWarnings("unchecked")
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
     * List save current assessment questions.
     */
    @SuppressWarnings("unchecked")
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
     */
    @SuppressWarnings("unchecked")
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
     */
    @SuppressWarnings("unchecked")
    private List<AssessmentQuestion> getDeletedQuestionList(SessionMap<String, Object> sessionMap) {
	return (List<AssessmentQuestion>) getListFromSession(sessionMap, AssessmentConstants.ATTR_DELETED_QUESTION_LIST);
    }

    /**
     * List save deleted assessment questions, which could be persisted or non-persisted questions.
     */
    @SuppressWarnings("unchecked")
    private List<QuestionReference> getDeletedQuestionReferences(SessionMap<String, Object> sessionMap) {
	return (List<QuestionReference>) getListFromSession(sessionMap, AssessmentConstants.ATTR_DELETED_QUESTION_REFERENCES);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     */
    private List<?> getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List<?> list = (List<?>) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList<>();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * Get back jsp name.
     */
    private String findForward(short type) {
	String forward;
	switch (type) {
	    case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:
		forward = "pages/authoring/parts/addmultiplechoice";
		break;
	    case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
		forward = "pages/authoring/parts/addmatchingpairs";
		break;
	    case AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER:
		forward = "pages/authoring/parts/addshortanswer";
		break;
	    case AssessmentConstants.QUESTION_TYPE_NUMERICAL:
		forward = "pages/authoring/parts/addnumerical";
		break;
	    case AssessmentConstants.QUESTION_TYPE_TRUE_FALSE:
		forward = "pages/authoring/parts/addtruefalse";
		break;
	    case AssessmentConstants.QUESTION_TYPE_ESSAY:
		forward = "pages/authoring/parts/addessay";
		break;
	    case AssessmentConstants.QUESTION_TYPE_ORDERING:
		forward = "pages/authoring/parts/addordering";
		break;
	    case AssessmentConstants.QUESTION_TYPE_MARK_HEDGING:
		forward = "pages/authoring/parts/addmarkhedging";
		break;
	    default:
		forward = null;
		break;
	}
	return forward;
    }

    /**
     * This method will populate assessment question information to its form for edit use.
     */
    private void populateQuestionToForm(AssessmentQuestion question, AssessmentQuestionForm form,
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
	form.setPrefixAnswersWithLetters(question.isPrefixAnswersWithLetters());
	form.setCaseSensitive(question.isCaseSensitive());
	form.setCorrectAnswer(question.getCorrectAnswer());
	form.setAllowRichEditor(question.isAllowRichEditor());
	form.setMaxWordsLimit(question.getMaxWordsLimit());
	form.setMinWordsLimit(question.getMinWordsLimit());
	form.setHedgingJustificationEnabled(question.isHedgingJustificationEnabled());
	form.setSequenceId(question.getSequenceId());

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
     * BE CAREFUL: This method will copy necessary info from request form to an old or new AssessmentQuestion
     * instance. It gets all info EXCEPT AssessmentQuestion.createDate, which need be set when
     * persisting this assessment Question.
     */
    private void extractFormToAssessmentQuestion(HttpServletRequest request, SortedSet<AssessmentQuestion> questionList,
	    AssessmentQuestionForm questionForm, boolean isAuthoringRestricted) {

	//find according question
	AssessmentQuestion question = null;
	// add
	if (questionForm.getSequenceId() == -1) { 
	    question = new AssessmentQuestion();
	    int maxSeq = 1;
	    if ((questionList != null) && (questionList.size() > 0)) {
		AssessmentQuestion last = questionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    question.setSequenceId(maxSeq);
	    questionList.add(question);
	    
	// edit
	} else {
	    for (AssessmentQuestion questionIter : questionList) {
		if (questionIter.getSequenceId() == questionForm.getSequenceId()) {
		    question = questionIter;
		    break;
		}
	    }
	}
	
	short type = questionForm.getQuestionType();
	question.setType(type);

	question.setTitle(questionForm.getTitle());
	question.setQuestion(questionForm.getQuestion());

	if (!isAuthoringRestricted) {
	    question.setDefaultGrade(Integer.parseInt(questionForm.getDefaultGrade()));
	}
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
	    question.setPrefixAnswersWithLetters(questionForm.isPrefixAnswersWithLetters());
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
		    grade = Integer.valueOf(gradeStr);
		}

		questionReference.setDefaultGrade(grade);
	    } catch (Exception e) {
		log.debug(e.getMessage());
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
	    AssessmentQuestionOption option = new AssessmentQuestionOption();
	    String uid = paramMap.get(AssessmentConstants.ATTR_OPTION_UID_PREFIX + i);
	    if (uid != null) {
		option.setUid(NumberUtils.toLong(uid));
	    }
	    String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
	    option.setSequenceId(NumberUtils.toInt(sequenceId));
	    
	    if ((questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		    || (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)) {
		String optionString = paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i);
		if ((optionString == null) && isForSaving) {
		    continue;
		}

		option.setOptionString(optionString);
		float grade = Float.valueOf(paramMap.get(AssessmentConstants.ATTR_OPTION_GRADE_PREFIX + i));
		option.setGrade(grade);
		option.setFeedback(paramMap.get(AssessmentConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		String question = paramMap.get(AssessmentConstants.ATTR_OPTION_QUESTION_PREFIX + i);
		if ((question == null) && isForSaving) {
		    continue;
		}

		option.setOptionString(paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i));
		option.setQuestion(question);

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
		String optionFloatStr = paramMap.get(AssessmentConstants.ATTR_OPTION_FLOAT_PREFIX + i);
		String acceptedErrorStr = paramMap.get(AssessmentConstants.ATTR_OPTION_ACCEPTED_ERROR_PREFIX + i);
		String gradeStr = paramMap.get(AssessmentConstants.ATTR_OPTION_GRADE_PREFIX + i);
		if (optionFloatStr.equals("0.0") && optionFloatStr.equals("0.0") && gradeStr.equals("0.0")
			&& isForSaving) {
		    continue;
		}

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

	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		String optionString = paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i);
		if ((optionString == null) && isForSaving) {
		    continue;
		}

		option.setOptionString(optionString);
		//TODO check the following line is not required
		//option.setAnswerInt(i);
		
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MARK_HEDGING) {
		String optionString = paramMap.get(AssessmentConstants.ATTR_OPTION_STRING_PREFIX + i);
		if ((optionString == null) && isForSaving) {
		    continue;
		}

		option.setOptionString(optionString);
		if ((correctOptionIndex != null) && correctOptionIndex.equals(Integer.valueOf(sequenceId))) {
		    option.setCorrect(true);
		}
		option.setFeedback(paramMap.get(AssessmentConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
	    }
	    
	    optionList.add(option);
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
		log.error("Error occurs when decode instruction string:" + e.toString());
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
    
    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    } 
}
