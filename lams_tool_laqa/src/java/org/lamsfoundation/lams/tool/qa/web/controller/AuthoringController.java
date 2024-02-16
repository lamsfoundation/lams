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

package org.lamsfoundation.lams.tool.qa.web.controller;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.model.QaCondition;
import org.lamsfoundation.lams.tool.qa.model.QaContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaQuestionComparator;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Q&A Tool's authoring methods. Additionally, there is one more method that initializes authoring and it's located in
 * QaStarterAction.java.
 *
 * @author Ozgur Demirtas
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController implements QaAppConstants {
    private static Logger logger = Logger.getLogger(AuthoringController.class.getName());

    @Autowired
    private IQaService qaService;

    @Autowired
    private IQbService qbService;

    @Autowired
    private IUserManagementService userManagementService;

    @Autowired
    @Qualifier("qaMessageService")
    private MessageService messageService;

    @RequestMapping("/authoring")
    public String execute(@ModelAttribute("authoringForm") QaAuthoringForm form, HttpServletRequest request,
	    @RequestParam Long toolContentID) throws IOException, ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	return readDatabaseData(form, request, mode);
    }

    /**
     * Set the defineLater flag so that learners cannot use content while we are editing. This flag is released when
     * updateContent is called.
     */
    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String definelater(@ModelAttribute("authoringForm") QaAuthoringForm form, HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	qaService.setDefineLater(toolContentID, true);

	return readDatabaseData(form, request, ToolAccessMode.TEACHER);
    }

    /**
     * Common method for "unspecified" and "defineLater"
     */
    private String readDatabaseData(QaAuthoringForm form, HttpServletRequest request, ToolAccessMode mode) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	form.setContentFolderID(contentFolderID);

	form.setToolContentID(toolContentID.toString());

	SessionMap<String, Object> sessionMap = new SessionMap<>();
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	form.setSessionMapID(sessionMap.getSessionID());

	QaContent qa = qaService.getQaContent(toolContentID);
	if (qa == null) {
	    /* fetch default content */
	    long defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    qa = qaService.getQaContent(defaultContentID);
	    qa = QaContent.newInstance(qa, toolContentID);
	}

	form.setQa(qa);

	/*
	 * get the existing question content
	 */
	Set<QaQueContent> qaQuestions = getQuestions(sessionMap);
	qaQuestions.clear();
	qaQuestions.addAll(qa.getQaQueContents());

	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	//process conditions
	for (QaCondition condition : qa.getConditions()) {
	    for (QaQueContent qaQuestion : qaQuestions) {
		for (QaQueContent conditionQuestion : condition.getQuestions()) {
		    if (qaQuestion.getDisplayOrder() == conditionQuestion.getDisplayOrder()) {
			condition.temporaryQaQuestions.add(qaQuestion);
		    }
		}
	    }
	}
	SortedSet<QaCondition> conditionList = AuthoringConditionController.getConditions(sessionMap);
	conditionList.clear();
	conditionList.addAll(qa.getConditions());

	// get rating criterias from DB
	List<RatingCriteria> ratingCriterias = qaService.getRatingCriterias(qa.getQaContentId());
	sessionMap.put(AttributeNames.ATTR_RATING_CRITERIAS, ratingCriterias);

	return "authoring/AuthoringTabsHolder";
    }

    /**
     * submits content into the tool database
     *
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping(path = "/submitAllContent", method = RequestMethod.POST)
    public String submitAllContent(@ModelAttribute("authoringForm") QaAuthoringForm form, HttpServletRequest request)
	    throws IOException, ServletException, IllegalAccessException, InvocationTargetException,
	    NoSuchMethodException {
	SessionMap<String, Object> sessionMap = getSessionMap(form, request);
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	Long toolContentID = form.getQa().getQaContentId();
	QaContent qa = form.getQa();
	QaContent qaPO = qaService.getQaContent(toolContentID);

	if (qaPO == null) {
	    // new Scratchie, create it.
	    qaPO = qa;
	    qaPO.setCreationDate(new Timestamp(new Date().getTime()));
	    qaPO.setUpdateDate(new Timestamp(new Date().getTime()));

	} else {
	    // copyProperties() below sets qaPO's conditions to empty collection
	    // but the conditions still exist in Hibernate cache, so we need to evict them now
	    for (QaCondition condition : qaPO.getConditions()) {
		qaService.releaseFromCache(condition);
	    }

	    qaPO.getQaQueContents().clear();
	    qaPO.getConditions().clear();
	    Long uid = qaPO.getUid();
	    PropertyUtils.copyProperties(qaPO, qa);

	    // set back UID
	    qaPO.setUid(uid);

	    // if it is Teacher (from monitor) - change define later status
	    if (mode.isTeacher()) {
		qaPO.setDefineLater(false);

		// audit log the teacher has started editing activity in monitor
		qaService.auditLogStartEditingActivityInMonitor(toolContentID);

		// recalculate User Answers - not required, as long as any question modification is minor and does not lead to changing question version or uid
	    }

	    qaPO.setUpdateDate(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		userId = 0;
	    }
	}
	qaPO.setCreatedBy(userId);
	qaService.saveOrUpdateQaContent(qaPO);

	// ************************* Handle Q&A questions *******************
	Set<QaQueContent> items = new LinkedHashSet<>();
	Set<QaQueContent> newItems = getQuestions(sessionMap);
	for (QaQueContent question : newItems) {
	    question.setQaContent(qaPO);
	    question.setToolContentId(qaPO.getQaContentId());
	    qaService.saveOrUpdate(question);
	    items.add(question);
	}
	qaPO.setQaQueContents(items);

	// ************************* Handle Q&A conditions *******************
	SortedSet<QaCondition> conditions = AuthoringConditionController.getConditions(sessionMap);
	for (QaCondition condition : conditions) {
	    condition.setQuestions(new TreeSet<>(new QaQuestionComparator()));
	    for (QaQueContent qaQuestion : condition.temporaryQaQuestions) {
		for (QaQueContent question : newItems) {
		    if (qaQuestion.getDisplayOrder() == question.getDisplayOrder()) {
			condition.getQuestions().add(question);
		    }
		}
	    }
	}
	qaPO.setConditions(conditions);

	//reorder questions so displayOrder numbers come in strictly sequential order (it's required for proper work of learning)
	int displayOrder = 1;
	for (QaQueContent question : newItems) {
	    question.setDisplayOrder(displayOrder++);
	    qaService.saveOrUpdate(question);
	}

	qaService.saveOrUpdateQaContent(qaPO);

	// ************************* Handle rating criterias *******************
	List<RatingCriteria> oldCriterias = (List<RatingCriteria>) sessionMap.get(AttributeNames.ATTR_RATING_CRITERIAS);
	qaService.saveRatingCriterias(request, oldCriterias, toolContentID);

	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	// remove deleted questions
	List<QaQueContent> deletedQuestions = getDeletedQuestions(sessionMap);
	for (QaQueContent deletedQuestion : deletedQuestions) {
	    QaQueContent removeableQuestion = qaService.getQuestionByUid(deletedQuestion.getUid());
	    if (removeableQuestion != null) {
		qaService.removeQuestion(removeableQuestion);
	    }
	}

	// delete conditions from database
	List<QaCondition> delConditionList = AuthoringConditionController.getDeletedQaConditionList(sessionMap);
	Iterator<QaCondition> delIter = delConditionList.iterator();
	while (delIter.hasNext()) {
	    QaCondition condition = delIter.next();
	    delIter.remove();
	    qaService.deleteCondition(condition);
	}

	return "authoring/AuthoringTabsHolder";
    }

    /**
     * Adds QbQuestion, selected in the question bank, to the current question list.
     */
    @RequestMapping(value = "/importQbQuestion", method = RequestMethod.POST)
    private String importQbQuestion(@ModelAttribute("newQuestionForm") QaAuthoringForm form, HttpServletRequest request,
	    @RequestParam String sessionMapID, @RequestParam Long qbQuestionUid) {
	SessionMap<String, Object> sessionMap = getSessionMap(form, request);
	SortedSet<QaQueContent> qaQuestions = getQuestions(sessionMap);

	//check whether this QB question is a duplicate
	for (QaQueContent qaQuestion : qaQuestions) {
	    if (qbQuestionUid.equals(qaQuestion.getQbQuestion().getUid())) {
		//let jsp know it's a duplicate
		return "forward:/authoring/showDuplicateQuestionError.do";
	    }
	}

	//get QbQuestion from DB
	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);

	//create new ScratchieItem and assign imported qbQuestion to it
	QaQueContent item = new QaQueContent();
	item.setQbQuestion(qbQuestion);
	int maxSeq = 1;
	if (qaQuestions != null && qaQuestions.size() > 0) {
	    QaQueContent last = qaQuestions.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	item.setDisplayOrder(maxSeq);
	qaQuestions.add(item);

	return "authoring/itemlist";
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
     * saveQuestion
     */
    @RequestMapping(value = "/saveQuestion", method = RequestMethod.POST)
    public String saveQuestion(@ModelAttribute("newQuestionForm") QbQuestionForm form, HttpServletRequest request,
	    @RequestParam(name = "newVersion", required = false) boolean enforceNewVersion)
	    throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(form, request);
	SortedSet<QaQueContent> qaQuestions = getQuestions(sessionMap);
	int itemIdx = NumberUtils.toInt(form.getItemIndex(), -1);

	// check whether it is "edit(old Question)" or "add(new Question)"
	QbQuestion qbQuestion;
	QaQueContent qaQuestion;
	boolean isDefaultQuestion = false;
	final boolean isAddingQuestion = itemIdx == -1;
	if (isAddingQuestion) { // add

	    String title = form.getTitle();
	    boolean duplicates = checkDuplicateQuestions(qaQuestions, title);
	    if (duplicates) {
		return "authoring/itemlist";
	    }

	    qbQuestion = new QbQuestion();
	    qbQuestion.setType(QbQuestion.TYPE_ESSAY);
	    qbQuestion.setQuestionId(form.getQuestionId());
	    qbQuestion.setContentFolderId(form.getContentFolderID());

	    qaQuestion = new QaQueContent();
	    int maxSeq = 1;
	    if (qaQuestions != null && qaQuestions.size() > 0) {
		QaQueContent last = qaQuestions.last();
		maxSeq = last.getDisplayOrder() + 1;
	    }
	    qaQuestion.setDisplayOrder(maxSeq);
	    qaQuestions.add(qaQuestion);

	    // edit
	} else {
	    List<QaQueContent> rList = new ArrayList<>(qaQuestions);
	    qaQuestion = rList.get(itemIdx);
	    long qbQuestionUid = qaQuestion.getQbQuestion().getUid();
	    qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	    qbService.releaseFromCache(qbQuestion);

	    // if it is a default question, do not modify it
	    // treat it as a new question
	    isDefaultQuestion = qbService.isQuestionDefaultInTool(qbQuestionUid, QaAppConstants.MY_SIGNATURE);
	    if (isDefaultQuestion) {
		String questionContentFolderID = FileUtil.generateUniqueContentFolderID();
		qbQuestion.setContentFolderId(questionContentFolderID);
		qbQuestion.setQuestionId(qbService.generateNextQuestionId()); // generate a new question ID right away, so another user won't "take it"
	    }
	}

	QbQuestion oldQbQuestion = qbQuestion.clone();
	// evict everything manually as we do not use DTOs, just real entities
	// without eviction changes would be saved immediately into DB
	qaService.releaseFromCache(oldQbQuestion);

	qbQuestion.setName(form.getTitle());
	qbQuestion.setDescription(form.getDescription());
	qbQuestion.setMinWordsLimit(form.getMinWordsLimit());
	qbQuestion.setFeedback(form.getFeedback());

	int isQbQuestionModified = isDefaultQuestion ? IQbService.QUESTION_MODIFIED_ID_BUMP
		: qbQuestion.isQbQuestionModified(oldQbQuestion);
	if (isQbQuestionModified < IQbService.QUESTION_MODIFIED_VERSION_BUMP && enforceNewVersion) {
	    isQbQuestionModified = IQbService.QUESTION_MODIFIED_VERSION_BUMP;
	}
	QbQuestion updatedQuestion = null;
	switch (isQbQuestionModified) {
	    case IQbService.QUESTION_MODIFIED_VERSION_BUMP: {
		// new version of the old question gets created
		updatedQuestion = qbQuestion.clone();
		updatedQuestion.clearID();
		updatedQuestion.setUuid((UUID) null);
		updatedQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()) + 1);
		updatedQuestion.setCreateDate(new Date());
		updatedQuestion.setUuid(UUID.randomUUID());
	    }
		break;
	    case IQbService.QUESTION_MODIFIED_ID_BUMP: {
		// new question gets created
		updatedQuestion = qbQuestion.clone();
		updatedQuestion.clearID();
		updatedQuestion.setUuid((UUID) null);
		updatedQuestion.setVersion(1);
		updatedQuestion.setCreateDate(new Date());
		updatedQuestion.setUuid(UUID.randomUUID());
	    }
		break;
	    case IQbService.QUESTION_MODIFIED_NONE: {
		// save the old question anyway, as it may contain some minor changes (like title or description change)
		updatedQuestion = qbQuestion;
	    }
		break;
	}
	userManagementService.save(updatedQuestion);
	qaQuestion.setQbQuestion(updatedQuestion);
	request.setAttribute("qbQuestionModified", isQbQuestionModified);

	//take care about question's collections. add to collection first
	Long oldCollectionUid = form.getOldCollectionUid();
	Long newCollectionUid = form.getNewCollectionUid();
	if (isAddingQuestion || isDefaultQuestion
		|| (newCollectionUid != null && !newCollectionUid.equals(oldCollectionUid))) {
	    qbService.addQuestionToCollection(newCollectionUid, updatedQuestion.getQuestionId(), false);
	}
	//remove from the old collection, if needed
	if (!isAddingQuestion && newCollectionUid != null && !newCollectionUid.equals(oldCollectionUid)) {
	    qbService.removeQuestionFromCollectionByQuestionId(oldCollectionUid, updatedQuestion.getQuestionId(),
		    false);
	}

	return "authoring/itemlist";
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    @RequestMapping("/newQuestionBox")
    private String newQuestionBox(@ModelAttribute("newQuestionForm") QbQuestionForm form, HttpServletRequest request,
	    @RequestParam String sessionMapID) {
	form.setSessionMapID(sessionMapID);
	String questionContentFolderID = FileUtil.generateUniqueContentFolderID();
	form.setContentFolderID(questionContentFolderID);
	form.setQuestionId(qbService.generateNextQuestionId()); // generate a new question ID right away, so another user won't "take it"

	request.setAttribute("isNewQuestion", true);
	QbUtils.fillFormWithUserCollections(qbService, form, null);
	return "authoring/newQuestionBox";
    }

    /**
     * Display edit page for existed scratchie item.
     */
    @RequestMapping("/newEditableQuestionBox")
    private String newEditableQuestionBox(@ModelAttribute("newQuestionForm") QbQuestionForm form,
	    HttpServletRequest request, @RequestParam String sessionMapID, @RequestParam Integer questionIndex) {
	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Set<QaQueContent> qaQuestions = getQuestions(sessionMap);

	List<QaQueContent> rList = new ArrayList<>(qaQuestions);
	QaQueContent qaQuestion = rList.get(questionIndex);
	QbQuestion qbQuestion = qaQuestion.getQbQuestion();

	form.setQuestionId(qbQuestion.getQuestionId());
	form.setTitle(qbQuestion.getName());
	form.setDescription(qbQuestion.getDescription());
	if (questionIndex >= 0) {
	    form.setItemIndex(String.valueOf(questionIndex));
	}
	form.setMinWordsLimit(qbQuestion.getMinWordsLimit());
	form.setFeedback(qbQuestion.getFeedback());

	form.setContentFolderID(qbQuestion.getContentFolderId());
	form.setSessionMapID(sessionMapID);
	QbUtils.fillFormWithUserCollections(qbService, QaAppConstants.MY_SIGNATURE, form, qbQuestion.getUid());
	request.setAttribute("isNewQuestion", false);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return "authoring/newQuestionBox";
    }

    /**
     * removes a question from the questions map
     */
    @RequestMapping(path = "/removeQuestion", method = RequestMethod.POST)
    public String removeQuestion(@ModelAttribute("newQuestionForm") QaAuthoringForm form, HttpServletRequest request)
	    throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(form, request);
	int questionIndex = NumberUtils.toInt(request.getParameter("questionIndex"), -1);
	Set<QaQueContent> qaQuestions = getQuestions(sessionMap);

	if (questionIndex != -1) {
	    List<QaQueContent> rList = new ArrayList<>(qaQuestions);
	    QaQueContent questionToDelete = rList.remove(questionIndex);
	    qaQuestions.clear();
	    qaQuestions.addAll(rList);

	    // add to delList
	    List<QaQueContent> delList = getDeletedQuestions(sessionMap);
	    delList.add(questionToDelete);

	    //take care about conditions
	    SortedSet<QaCondition> conditions = AuthoringConditionController.getConditions(sessionMap);
	    Iterator<QaCondition> conditionIter = conditions.iterator();
	    while (conditionIter.hasNext()) {
		QaCondition condition = conditionIter.next();
		Iterator<QaQueContent> questionToDeleteIter = condition.temporaryQaQuestions.iterator();
		while (questionToDeleteIter.hasNext()) {
		    if (questionToDeleteIter.next() == questionToDelete) {
			questionToDeleteIter.remove();
		    }
		}
		if (condition.temporaryQaQuestions.isEmpty()) {
		    conditionIter.remove();
		}
	    }
	}

	return "authoring/itemlist";
    }

    @RequestMapping(path = "/toggleQuestionRequired", method = RequestMethod.POST)
    @ResponseBody
    public String toggleQuestionRequired(@ModelAttribute("newQuestionForm") QaAuthoringForm form,
	    HttpServletRequest request) throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(form, request);
	int questionIndex = NumberUtils.toInt(request.getParameter("questionIndex"), -1);
	Set<QaQueContent> qaQuestions = getQuestions(sessionMap);

	List<QaQueContent> rList = new ArrayList<>(qaQuestions);
	QaQueContent question = rList.get(questionIndex);
	question.setAnswerRequired(!question.isAnswerRequired());

	return String.valueOf(question.isAnswerRequired());
    }

    /**
     * moves a question down in the list
     */
    @RequestMapping("/moveQuestionDown")
    public String moveQuestionDown(@ModelAttribute("newQuestionForm") QaAuthoringForm form, HttpServletRequest request)
	    throws IOException, ServletException {
	swapQuestions(form, request, "down");

	return "authoring/itemlist";
    }

    /**
     * moves a question up in the list
     */
    @RequestMapping("/moveQuestionUp")
    public String moveQuestionUp(@ModelAttribute("newQuestionForm") QaAuthoringForm form, HttpServletRequest request)
	    throws IOException, ServletException {
	swapQuestions(form, request, "up");

	return "authoring/itemlist";
    }

    private Set<QaQueContent> swapQuestions(QaAuthoringForm form, HttpServletRequest request, String direction) {
	SessionMap<String, Object> sessionMap = getSessionMap(form, request);
	Set<QaQueContent> questions = getQuestions(sessionMap);
	int originalQuestionIndex = WebUtil.readIntParam(request, "questionIndex");
	int replacedQuestionIndex = direction.equals("down") ? originalQuestionIndex + 1 : originalQuestionIndex - 1;

	List<QaQueContent> rList = new ArrayList<>(questions);
	// get current and the target item, and switch their sequence
	QaQueContent originalQuestion = rList.get(originalQuestionIndex);
	QaQueContent replacedQuestion = rList.get(replacedQuestionIndex);

	int upSeqId = replacedQuestion.getDisplayOrder();
	replacedQuestion.setDisplayOrder(originalQuestion.getDisplayOrder());
	originalQuestion.setDisplayOrder(upSeqId);

	// put back list, it will be sorted again
	questions.clear();
	questions.addAll(rList);

	return questions;
    }

    private boolean checkDuplicateQuestions(Set<QaQueContent> questions, String newQuestion) {
	for (QaQueContent qaQuestion : questions) {
	    if (qaQuestion.getQbQuestion().getName().equals(newQuestion)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * List current Q&A questions.
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private SortedSet<QaQueContent> getQuestions(SessionMap<String, Object> sessionMap) {
	SortedSet<QaQueContent> list = (SortedSet<QaQueContent>) sessionMap.get(QaAppConstants.LIST_QUESTIONS);
	if (list == null) {
	    list = new TreeSet<>(new QaQuestionComparator());
	    sessionMap.put(QaAppConstants.LIST_QUESTIONS, list);
	}
	return list;
    }

    /**
     * List save deleted scratchie items, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<QaQueContent> getDeletedQuestions(SessionMap<String, Object> sessionMap) {
	List<QaQueContent> list = (List<QaQueContent>) sessionMap.get(QaAppConstants.LIST_DELETED_QUESTIONS);
	if (list == null) {
	    list = new ArrayList<>();
	    sessionMap.put(QaAppConstants.LIST_DELETED_QUESTIONS, list);
	}
	return list;
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(QaAuthoringForm form, HttpServletRequest request) {
	String sessionMapID = form.getSessionMapID();
	request.setAttribute(QaAppConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(QbQuestionForm form, HttpServletRequest request) {
	String sessionMapID = form.getSessionMapID();
	request.setAttribute(QaAppConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }
}