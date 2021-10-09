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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.QbConstants;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOverallFeedback;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    @Autowired
    private IQbService qbService;

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
	Long contentId = WebUtil.readLongParam(request, AssessmentConstants.PARAM_TOOL_CONTENT_ID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	assessmentForm.setSessionMapID(sessionMap.getSessionID());

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);
	assessmentForm.setContentFolderID(contentFolderID);

	Assessment assessment = null;
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

	// init RandomPoolQuestions
	List<AssessmentQuestion> randomPoolQuestions = getRandomPoolQuestions(sessionMap);
	randomPoolQuestions.clear();
	for (AssessmentQuestion question : assessment.getQuestions()) {
	    // since we are iterating anyway, here fill version information to each question
	    qbService.fillVersionMap(question.getQbQuestion());

	    if (question.isRandomQuestion()) {
		randomPoolQuestions.add(question);
	    }
	}

	// init question references
	SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);
	references.clear();
	references.addAll(assessment.getQuestionReferences());
	//init references' qbQuestions to avoid no session exception
	for (QuestionReference reference : references) {
	    if (!reference.isRandomQuestion()) {
		reference.getQuestion().getQbQuestion();
	    }
	}

	boolean isAssessmentAttempted = assessment.getUid() == null ? false
		: service.isAssessmentAttempted(assessment.getUid());
	sessionMap.put(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED, isAssessmentAttempted && mode.isTeacher());
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT_FORM, assessmentForm);

	boolean questionEtherpadEnabled = StringUtils.isNotBlank(Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY));
	sessionMap.put(AssessmentConstants.ATTR_IS_QUESTION_ETHERPAD_ENABLED, questionEtherpadEnabled);

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
    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
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

	AssessmentUser assessmentUser = null;

	if (assessmentPO == null) {
	    // new Assessment, create it
	    assessmentPO = assessment;
	    assessmentPO.setCreated(new Timestamp(new Date().getTime()));

	} else {
	    // copyProperties() below sets assessmentPO items to empty collection
	    // but the items still exist in Hibernate cache, so we need to evict them now
	    for (AssessmentQuestion question : oldQuestions) {
		service.releaseFromCache(question);
	    }
	    for (QuestionReference reference : oldReferences) {
		service.releaseFromCache(reference);
	    }

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
	List<AssessmentQuestion> newRandomQuestions = getRandomPoolQuestions(sessionMap);
	Set<QuestionReference> newReferences = getQuestionReferences(sessionMap);

	TreeSet<AssessmentQuestion> newQuestions = new TreeSet<>();
	newQuestions.addAll(newRandomQuestions);
	for (QuestionReference reference : newReferences) {
	    if (!reference.isRandomQuestion()) {
		AssessmentQuestion question = reference.getQuestion();
		newQuestions.add(question);
	    }
	}

	for (AssessmentQuestion question : newQuestions) {
	    question.setToolContentId(assessmentPO.getContentId());
	}

	assessmentPO.setQuestions(newQuestions);

	// recalculate results in case content is edited from monitoring and it's been already attempted by a student
	boolean isAuthoringRestricted = (boolean) sessionMap.get(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED);
	if (isAuthoringRestricted) {
	    service.recalculateUserAnswers(assessmentPO.getUid(), assessmentPO.getContentId(), oldQuestions,
		    newQuestions, oldReferences, newReferences);
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

	// delete References from database
	List<QuestionReference> deletedReferences = getDeletedQuestionReferences(sessionMap);
	Iterator<QuestionReference> iterRef = deletedReferences.iterator();
	while (iterRef.hasNext()) {
	    QuestionReference reference = iterRef.next();
	    iterRef.remove();
	    if (reference.getUid() != null) {
		service.deleteQuestionReference(reference.getUid());

		//TODO maybe check and delete orphaned AssessmentQuestion
		//service.deleteAssessmentQuestion(question.getUid());
	    }
	}

	// delete References from database
	List<AssessmentQuestion> deletedRandomPoolQuestions = getDeletedRandomPoolQuestions(sessionMap);
	Iterator<AssessmentQuestion> iterDeletedQuestions = deletedRandomPoolQuestions.iterator();
	while (iterDeletedQuestions.hasNext()) {
	    AssessmentQuestion delQuestion = iterDeletedQuestions.next();
	    iterDeletedQuestions.remove();
	    if (delQuestion.getUid() != null) {
		service.deleteAssessmentQuestion(delQuestion.getUid());
	    }
	}

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return "pages/authoring/authoring";
    }

    /**
     * Display empty page for new assessment question.
     */
    @RequestMapping("/initNewReference")
    public String initNewReference(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Integer type = NumberUtils.toInt(request.getParameter(QbConstants.ATTR_QUESTION_TYPE));
	if (type.equals(-1)) {//randomQuestion case
	    request.setAttribute("isReferenceNew", true);//which signifies it's a new question

	    //prepare data for displaying collections
	    Integer userId = getUserId();
	    Collection<QbCollection> userCollections = qbService.getUserCollections(userId);
	    request.setAttribute("userCollections", userCollections);

	    List<AssessmentQuestion> randomPoolQuestions = getRandomPoolQuestions(sessionMap);
	    request.setAttribute("randomPoolQuestions", randomPoolQuestions);
	    return "pages/authoring/randomQuestion";

	} else {
	    String url = "redirect:" + Configuration.get(ConfigurationKeys.SERVER_URL) + "qb/edit/initNewQuestion.do?"
		    + request.getQueryString();
	    boolean isAuthoringRestricted = (boolean) sessionMap.get(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED);
	    url = WebUtil.appendParameterToURL(url, AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED,
		    String.valueOf(isAuthoringRestricted));

	    return url;
	}
    }

    /**
     * Display edit page for existing assessment question.
     */
    @RequestMapping("/editReference")
    public String editReference(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam int referenceSequenceId) throws ServletException, IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);

	//find reference to edit
	List<QuestionReference> rList = new ArrayList<>(questionReferences);
	QuestionReference questionReference = rList.get(referenceSequenceId);

	if (questionReference.isRandomQuestion()) {
	    request.setAttribute("isReferenceNew", false);

	    //prepare data for displaying collections
	    Integer userId = getUserId();
	    Collection<QbCollection> userCollections = qbService.getUserCollections(userId);
	    request.setAttribute("userCollections", userCollections);

	    List<AssessmentQuestion> randomPoolQuestions = getRandomPoolQuestions(sessionMap);
	    request.setAttribute("randomPoolQuestions", randomPoolQuestions);

	    return "pages/authoring/randomQuestion";

	} else {
	    boolean isAuthoringRestricted = (boolean) sessionMap.get(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED);
	    AssessmentQuestion question = questionReference.getQuestion();

	    String url = "redirect:" + Configuration.get(ConfigurationKeys.SERVER_URL) + "qb/edit/editQuestion.do?"
		    + request.getQueryString();
	    url = WebUtil.appendParameterToURL(url, AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED,
		    String.valueOf(isAuthoringRestricted));
	    url = WebUtil.appendParameterToURL(url, "qbQuestionUid", question.getQbQuestion().getUid().toString());

	    return url;
	}
    }

    /**
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/saveOrUpdateReference")
    public String saveOrUpdateReference(@ModelAttribute("assessmentQuestionForm") QbQuestionForm form,
	    HttpServletRequest request, @RequestParam Long qbQuestionUid,
	    @RequestParam int questionModificationStatus) {
	String sessionMapId = form.getSessionMapID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);

	// add
	Long oldQbQuestionUid = form.getUid();
	if (oldQbQuestionUid == -1) {
	    AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
	    assessmentQuestion.setQbQuestion(qbQuestion);
	    assessmentQuestion.setRandomQuestion(false);
	    assessmentQuestion.setDisplayOrder(getNextDisplayOrder(sessionMap));

	    //create new QuestionReference
	    QuestionReference reference = new QuestionReference();
	    reference.setQuestion(assessmentQuestion);
	    reference.setRandomQuestion(false);
	    // set SequenceId
	    int maxSeq = 1;
	    if ((references != null) && (references.size() > 0)) {
		QuestionReference last = references.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    reference.setSequenceId(maxSeq);
	    //set maxMark
	    int maxMark = qbQuestion.getMaxMark() == null ? 1 : qbQuestion.getMaxMark();
	    reference.setMaxMark(maxMark);
	    references.add(reference);

	    // edit
	} else {
	    //replace QbQuestion with the new version of it
	    for (QuestionReference reference : references) {
		if (!reference.isRandomQuestion()
			&& oldQbQuestionUid.equals(reference.getQuestion().getQbQuestion().getUid())) {
		    AssessmentQuestion assessmentQuestion = reference.getQuestion();
		    assessmentQuestion.setQbQuestion(qbQuestion);
		    break;
		}
	    }

	    request.setAttribute("oldQbQuestionUid", oldQbQuestionUid);
	    request.setAttribute("newQbQuestionUid", qbQuestionUid);
	}
	request.setAttribute("qbQuestionModified", questionModificationStatus);
	qbService.fillVersionMap(qbQuestion);

	// set session map ID so that questionlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	//in case of edit in monitor and at least one attempted user, we show authoring page with restricted options
	boolean isAuthoringRestricted = (boolean) sessionMap.get(AssessmentConstants.ATTR_IS_AUTHORING_RESTRICTED);
	if (isAuthoringRestricted) {
	    return "pages/authoring/parts/questionlistRestricted";
	} else {
	    return "pages/authoring/parts/questionlist";
	}
    }

    /**
     * Adds random QuestionReference into reference list.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/saveOrUpdateRandomReference", method = RequestMethod.POST)
    private String saveOrUpdateRandomReference(HttpServletRequest request, @RequestParam String questionUids,
	    @RequestParam boolean isReferenceNew) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);

	// add
	if (isReferenceNew) {
	    //create new QuestionReference
	    QuestionReference reference = new QuestionReference();
	    reference.setRandomQuestion(true);
	    // set sequenceId
	    int maxSeq = 1;
	    if ((references != null) && (references.size() > 0)) {
		QuestionReference last = references.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    reference.setSequenceId(maxSeq);
	    reference.setMaxMark(1);
	    references.add(reference);

	    // edit
	} else {
	    //do nothing
	}

	List<String> newQuestionUids = Arrays.asList(questionUids.split(","));
	List<AssessmentQuestion> randomPoolQuestions = getRandomPoolQuestions(sessionMap);

	//remove obsolete questions
	for (AssessmentQuestion randomPoolQuestion : randomPoolQuestions) {
	    if (!newQuestionUids.contains(randomPoolQuestion.getQbQuestion().getUid().toString())) {
		randomPoolQuestions.remove(randomPoolQuestion);

		// add to delList
		List<AssessmentQuestion> deletedRandomPoolQuestions = getDeletedRandomPoolQuestions(sessionMap);
		deletedRandomPoolQuestions.add(randomPoolQuestion);
		break;
	    }
	}

	//add newly selected questions
	for (String newQuestionUid : newQuestionUids) {
	    Long newQuestionUidLong = Long.valueOf(newQuestionUid);

	    //check if according AssessmentQuestion exists already
	    boolean isAssessmentQuestionExist = false;
	    for (AssessmentQuestion randomPoolQuestion : randomPoolQuestions) {
		if (newQuestionUidLong.equals(randomPoolQuestion.getQbQuestion().getUid())) {
		    isAssessmentQuestionExist = true;
		    break;
		}
	    }

	    if (!isAssessmentQuestionExist) {
		AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
		QbQuestion qbQuestion = qbService.getQuestionByUid(newQuestionUidLong);
		assessmentQuestion.setQbQuestion(qbQuestion);
		assessmentQuestion.setRandomQuestion(true);
		assessmentQuestion.setDisplayOrder(getNextDisplayOrder(sessionMap));
		randomPoolQuestions.add(assessmentQuestion);
	    }
	}

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/questionlist";
    }

    /**
     * Adds multiple QbQuestions, imported from QTI
     */
    @RequestMapping(value = "/importQbQuestions", method = RequestMethod.POST)
    private String importQbQuestions(HttpServletRequest request, @RequestParam String sessionMapID,
	    @RequestParam String qbQuestionUids) {
	// get a list of QB question UIDs and add each of them to the activity
	for (String qbQuestionUid : qbQuestionUids.split(",")) {
	    if (StringUtils.isNotBlank(qbQuestionUid)) {
		importQbQuestion(request, sessionMapID, Long.valueOf(qbQuestionUid));
	    }
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/questionlist";
    }

    /**
     * QB callback handler which adds selected QbQuestion into question list.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/importQbQuestion", method = RequestMethod.POST)
    private String importQbQuestion(HttpServletRequest request, @RequestParam String sessionMapID,
	    @RequestParam Long qbQuestionUid) {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedSet<QuestionReference> references = getQuestionReferences(sessionMap);

	//check whether this question is a duplicate
	for (QuestionReference reference : references) {
	    if (!reference.isRandomQuestion()
		    && qbQuestionUid.equals(reference.getQuestion().getQbQuestion().getUid())) {
		//let jsp know it's a duplicate
		return "forward:/authoring/showDuplicateQuestionError.do";
	    }
	}

	//create new ScratchieItem and assign imported qbQuestion to it
	AssessmentQuestion assessmentQuestion = new AssessmentQuestion();
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	assessmentQuestion.setQbQuestion(qbQuestion);
	assessmentQuestion.setDisplayOrder(getNextDisplayOrder(sessionMap));
	qbService.fillVersionMap(qbQuestion);

	QuestionReference reference = new QuestionReference();
	reference.setQuestion(assessmentQuestion);
	reference.setRandomQuestion(false);

	// set SequenceId
	int maxSeq = 1;
	if (!references.isEmpty()) {
	    QuestionReference last = references.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	reference.setSequenceId(maxSeq);

	//set maxMark
	int maxMark = qbQuestion.getMaxMark() == null ? 1 : qbQuestion.getMaxMark();
	reference.setMaxMark(maxMark);

	references.add(reference);

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/questionlist";
    }

    /**
     * Shows "This question has already been added" error message in a browser.
     */
    @RequestMapping("/showDuplicateQuestionError")
    @ResponseBody
    public String showDuplicateQuestionError(HttpServletResponse response) throws IOException {
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("isDuplicated", true);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only
     * happen when user submit whole page. So this remove is just impact HttpSession values.
     */
    @RequestMapping("/removeQuestionReference")
    public String removeQuestionReference(HttpServletRequest request, @RequestParam int referenceSequenceId) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);

	//find reference to delete
	List<QuestionReference> rList = new ArrayList<>(questionReferences);
	QuestionReference questionReference = rList.remove(referenceSequenceId);

	questionReferences.remove(questionReference);

	// add to delList
	List<QuestionReference> delList = getDeletedQuestionReferences(sessionMap);
	delList.add(questionReference);

	return "pages/authoring/parts/questionlist";
    }

    @RequestMapping(path = "/toggleQuestionRequired", method = RequestMethod.POST)
    @ResponseBody
    public String toggleQuestionRequired(HttpServletRequest request, @RequestParam int referenceSequenceId) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);

	List<QuestionReference> rList = new ArrayList<>(questionReferences);
	QuestionReference questionReference = rList.remove(referenceSequenceId);
	AssessmentQuestion question = questionReference.getQuestion();
	question.setAnswerRequired(!question.isAnswerRequired());

	return String.valueOf(question.isAnswerRequired());
    }

    @RequestMapping("/changeItemQuestionVersion")
    private String changeItemQuestionVersion(@RequestParam int referenceSequenceId, @RequestParam long newQbQuestionUid,
	    HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);

	List<QuestionReference> rList = new ArrayList<>(questionReferences);
	QuestionReference questionReference = rList.remove(referenceSequenceId);
	AssessmentQuestion question = questionReference.getQuestion();
	QbQuestion newQbQuestion = qbService.getQuestionByUid(newQbQuestionUid);
	qbService.fillVersionMap(newQbQuestion);
	question.setQbQuestion(newQbQuestion);

	return "pages/authoring/parts/questionlist";
    }

    @RequestMapping("/getAllQbQuestionUids")
    @ResponseBody
    public String getAllQbQuestionUids(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(required = false) String collectionUids) {
	// Getting the params passed in from the jqGrid
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "name";//"smth_else";
	}
	String searchString = WebUtil.readStrParam(request, "searchString", true);

	List<BigInteger> questionUids = qbService.getAllQuestionUids(collectionUids, sortBy, sortOrder, searchString);
	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	for (BigInteger questionUid : questionUids) {
	    rows.add(questionUid);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.set("questionUids", rows);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping("/getPagedRandomQuestions")
    @ResponseBody
    private String getPagedRandomQuestions(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam(required = false) String collectionUids) {
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "name";//"smth_else";
	}
	String searchString = WebUtil.readStrParam(request, "searchString", true);

	// Get the user list from the db
	List<QbQuestion> questions = qbService.getPagedQuestions(null, collectionUids, page - 1, rowLimit, sortBy,
		sortOrder, searchString);
	int countQuestions = qbService.getCountQuestions(null, collectionUids, searchString);
	int totalPages = Double.valueOf(Math.ceil(Double.valueOf(countQuestions) / Double.valueOf(rowLimit)))
		.intValue();

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	for (QbQuestion question : questions) {
	    ArrayNode questionData = JsonNodeFactory.instance.arrayNode();
	    questionData.add(question.getUid());

	    String title = question.getName() == null ? ""
		    : question.getName().replaceAll("\\<.*?\\>", "").replaceAll("\\n", " ").trim();
	    questionData.add(HtmlUtils.htmlEscape(title));
	    String description = question.getDescription() == null ? ""
		    : question.getDescription().replaceAll("\\<.*?\\>", "").trim();
	    questionData.add(HtmlUtils.htmlEscape(description));

	    ObjectNode questionRow = JsonNodeFactory.instance.objectNode();
	    questionRow.put("id", question.getUid());
	    questionRow.set("cell", questionData);

	    rows.add(questionRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countQuestions);
	responseJSON.set("rows", rows);

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Caches references order, along with this caches MaxMarks.
     */
    @RequestMapping("/cacheReferencesOrder")
    @ResponseStatus(HttpStatus.OK)
    public void cacheReferencesOrder(HttpServletRequest request) throws ServletException, IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Set<QuestionReference> questionReferences = getQuestionReferences(sessionMap);

	//cache references' sequenceIds
	Map<String, String> sequenceIdsParamMap = splitRequestParameter(request,
		AssessmentConstants.ATTR_REFERENCES_SEQUENCE_IDS);
	List<QuestionReference> updatedQuestionReferences = new LinkedList<>();
	for (QuestionReference questionReference : questionReferences.toArray(new QuestionReference[0])) {
	    String newSequenceId = sequenceIdsParamMap
		    .get(AssessmentConstants.PARAM_SEQUENCE_ID + questionReference.getSequenceId());

	    questionReference.setSequenceId(Integer.valueOf(newSequenceId));
	    updatedQuestionReferences.add(questionReference);
	}
	questionReferences.clear();
	questionReferences.addAll(updatedQuestionReferences);
    }

    /**
     * change reference's MaxMark
     */
    @RequestMapping("/changeMaxMark")
    @ResponseStatus(HttpStatus.OK)
    public void changeMaxMark(HttpServletRequest request, @RequestParam int referenceSequenceId,
	    @RequestParam int maxMark) throws ServletException, IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<QuestionReference> questionReferences = getQuestionReferences(sessionMap);

	//find reference to edit
	List<QuestionReference> rList = new ArrayList<>(questionReferences);
	QuestionReference questionReference = rList.get(referenceSequenceId);
	questionReference.setMaxMark(maxMark);
    }

    /**
     * Ajax call, will add one more input line for a new OverallFeedback.
     */
    @RequestMapping("/initOverallFeedback")
    public String initOverallFeedback(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	AssessmentForm assessmentForm = (AssessmentForm) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT_FORM);
	Assessment assessment = service.getAssessmentByContentId(assessmentForm.getAssessment().getContentId());

	// initial Overall feedbacks list
	SortedSet<AssessmentOverallFeedback> overallFeedbackList = new TreeSet<>(new SequencableComparator());
	if (assessment != null && !assessment.getOverallFeedbacks().isEmpty()) {
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
    private List<QuestionReference> getDeletedQuestionReferences(SessionMap<String, Object> sessionMap) {
	return (List<QuestionReference>) getListFromSession(sessionMap,
		AssessmentConstants.ATTR_DELETED_QUESTION_REFERENCES);
    }

    /**
     * List current assessment questions.
     */
    @SuppressWarnings("unchecked")
    private List<AssessmentQuestion> getRandomPoolQuestions(SessionMap<String, Object> sessionMap) {
	return (List<AssessmentQuestion>) getListFromSession(sessionMap,
		AssessmentConstants.ATTR_RANDOM_POOL_QUESTIONS);
    }

    /**
     * List current assessment questions.
     */
    @SuppressWarnings("unchecked")
    private List<AssessmentQuestion> getDeletedRandomPoolQuestions(SessionMap<String, Object> sessionMap) {
	return (List<AssessmentQuestion>) getListFromSession(sessionMap,
		AssessmentConstants.ATTR_DELETED_RANDOM_POOL_QUESTIONS);
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

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }

    private int getNextDisplayOrder(SessionMap<String, Object> sessionMap) {
	int maxDisplayOrder = 1;

	List<AssessmentQuestion> randomPoolQuestions = getRandomPoolQuestions(sessionMap);
	for (AssessmentQuestion randomPoolQuestion : randomPoolQuestions) {
	    if (randomPoolQuestion.getDisplayOrder() > maxDisplayOrder) {
		maxDisplayOrder = randomPoolQuestion.getDisplayOrder();
	    }
	}

	Set<QuestionReference> references = getQuestionReferences(sessionMap);
	for (QuestionReference reference : references) {
	    AssessmentQuestion question = reference.getQuestion();
	    if (question != null && question.getDisplayOrder() > maxDisplayOrder) {
		maxDisplayOrder = question.getDisplayOrder();
	    }
	}

	return maxDisplayOrder + 1;
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}
