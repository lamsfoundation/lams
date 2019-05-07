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

package org.lamsfoundation.lams.tool.scratchie.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchieForm;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchieItemForm;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchiePedagogicalPlannerForm;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    private IScratchieService scratchieService;

    @Autowired
    private IQbService qbService;

    @Autowired
    @Qualifier("scratchieMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    private String start(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return starting(authoringForm, request);

    }

    @RequestMapping("/definelater")
    private String definelater(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	// update define later flag to true
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Scratchie scratchie = scratchieService.getScratchieByContentId(contentId);

	scratchie.setDefineLater(true);
	scratchieService.saveOrUpdateScratchie(scratchie);

	//audit log the teacher has started editing activity in monitor
	scratchieService.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return starting(authoringForm, request);

    }

    private String starting(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	List<ScratchieItem> items = null;
	Scratchie scratchie = null;

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	authoringForm.setSessionMapID(sessionMap.getSessionID());

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	authoringForm.setContentFolderID(contentFolderID);
	
	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, ScratchieConstants.PARAM_TOOL_CONTENT_ID));
	sessionMap.put(ScratchieConstants.PARAM_TOOL_CONTENT_ID, contentId);

	try {
	    scratchie = scratchieService.getScratchieByContentId(contentId);
	    // if scratchie does not exist, try to use default content instead.
	    if (scratchie == null) {
		scratchie = scratchieService.getDefaultContent(contentId);
		if (scratchie.getScratchieItems() != null) {
		    items = new ArrayList<>(scratchie.getScratchieItems());
		} else {
		    items = null;
		}
	    } else {
		items = scratchieService.getAuthoredItems(scratchie.getUid());
	    }

	    authoringForm.setScratchie(scratchie);
	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	ScratchieConfigItem isEnabledExtraPointOption = scratchieService
		.getConfigItem(ScratchieConfigItem.KEY_IS_ENABLED_EXTRA_POINT_OPTION);
	sessionMap.put(ScratchieConfigItem.KEY_IS_ENABLED_EXTRA_POINT_OPTION,
		new Boolean(isEnabledExtraPointOption.getConfigValue()));

	//prepare advanced option allowing to overwrite default preset marks
	ScratchieConfigItem defaultPresetMarksConfigItem = scratchieService
		.getConfigItem(ScratchieConfigItem.KEY_PRESET_MARKS);
	String defaultPresetMarks = defaultPresetMarksConfigItem == null ? ""
		: defaultPresetMarksConfigItem.getConfigValue();
	boolean presetMarksOverwritten = scratchie.getPresetMarks() != null
		&& !scratchie.getPresetMarks().equals(defaultPresetMarks);
	sessionMap.put(ScratchieConstants.ATTR_IS_PRESET_MARKS_OVERWRITTEN, presetMarksOverwritten);
	sessionMap.put(ScratchieConstants.ATTR_DEFAULT_PRESET_MARKS, defaultPresetMarks);

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<>();
	}
	// init scratchie item list
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	itemList.clear();
	itemList.addAll(items);
	request.setAttribute(ScratchieConstants.ATTR_ITEM_LIST, itemList);

	// If there is no order id, set it up
	int i = 1;
	for (ScratchieItem scratchieItem : itemList) {
	    if (scratchieItem.getDisplayOrder() != i) {
		scratchieItem.setDisplayOrder(i);
	    }
	    i++;
	}

	//display confidence providing activities
	Set<ToolActivity> confidenceLevelsActivities = scratchieService
		.getPrecedingConfidenceLevelsActivities(contentId);
	sessionMap.put(ScratchieConstants.ATTR_CONFIDENCE_LEVELS_ACTIVITIES, confidenceLevelsActivities);

	sessionMap.put(ScratchieConstants.ATTR_RESOURCE_FORM, authoringForm);
	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    @RequestMapping("/init")
    private String initPage(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	ScratchieForm existForm = (ScratchieForm) sessionMap.get(ScratchieConstants.ATTR_RESOURCE_FORM);

	try {
	    PropertyUtils.copyProperties(authoringForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	authoringForm.setMode(mode.toString());
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all scratchie item, information etc.
     */
    @RequestMapping("/update")
    private String updateContent(@ModelAttribute("authoringForm") ScratchieForm authoringForm,
	    HttpServletRequest request) throws Exception {

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);

	Scratchie scratchie = authoringForm.getScratchie();

	// **********************************Get Scratchie PO*********************
	Scratchie scratchiePO = scratchieService.getScratchieByContentId(authoringForm.getScratchie().getContentId());

	Set<ScratchieItem> oldItems = null;

	//allow using old and modified questions and references altogether
	if (mode.isTeacher()) {
	    oldItems = (scratchiePO == null) ? new HashSet<>() : scratchiePO.getScratchieItems();
	}

	if (scratchiePO == null) {
	    // new Scratchie, create it.
	    scratchiePO = scratchie;
	    scratchiePO.setCreated(new Timestamp(new Date().getTime()));
	    scratchiePO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    // copyProperties() below sets scratchiePO's items to empty collection
	    // but the items still exist in Hibernate cache, so we need to evict them now
	    scratchieService.releaseItemsFromCache(scratchiePO);
	    scratchiePO.getScratchieItems().clear();
	    Long uid = scratchiePO.getUid();
	    PropertyUtils.copyProperties(scratchiePO, scratchie);
	    // set back UID
	    scratchiePO.setUid(uid);

	    // if it is Teacher (from monitor) - change define later status
	    if (mode.isTeacher()) {
		scratchiePO.setDefineLater(false);
	    }

	    scratchiePO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// ************************* Handle scratchie items *******************
	Set<ScratchieItem> items = new LinkedHashSet<>();
	SortedSet<ScratchieItem> newItems = getItemList(sessionMap);
	Iterator<ScratchieItem> iter = newItems.iterator();
	while (iter.hasNext()) {
	    ScratchieItem item = iter.next();
	    if (item != null) {
		item.setToolContentId(scratchiePO.getContentId());
		removeNewLineCharacters(item);
		items.add(item);
		// modification status was already set in AuthoringController#saveItem()
		switch (item.getQbQuestionModified()) {
		    case IQbService.QUESTION_MODIFIED_VERSION_BUMP: {
			// new version of the old question gets created
			QbQuestion qbQuestion = item.getQbQuestion().clone();
			item.setQbQuestion(qbQuestion);
			qbQuestion.clearID();
			qbQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()));
			qbQuestion.setCreateDate(new Date());
		    }
			break;
		    case IQbService.QUESTION_MODIFIED_ID_BUMP: {
			// new question gets created
			QbQuestion qbQuestion = item.getQbQuestion().clone();
			item.setQbQuestion(qbQuestion);
			qbQuestion.clearID();
			qbQuestion.setVersion(1);
			qbQuestion.setQuestionId(qbService.getMaxQuestionId());
			qbQuestion.setCreateDate(new Date());
		    }
			break;
		}
	    }
	}
	scratchiePO.setScratchieItems(items);

	//recalculate results in case content is edited from monitoring
	List<ScratchieItem> deletedItems = getDeletedItemList(sessionMap);
	if (mode.isTeacher()) {
	    scratchieService.recalculateUserAnswers(scratchiePO, oldItems, newItems, deletedItems);
	}

	// delete items from database.
	iter = deletedItems.iterator();
	while (iter.hasNext()) {
	    ScratchieItem item = iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		scratchieService.deleteScratchieItem(item.getUid());
	    }
	}

	// **********************************************
	// finally persist scratchiePO again
	scratchieService.saveOrUpdateScratchie(scratchiePO);

	authoringForm.setScratchie(scratchiePO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "pages/authoring/authoring";
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    @RequestMapping("/addItem")
    private String addItem(@ModelAttribute("scratchieItemForm") ScratchieItemForm scratchieItemForm,
	    HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	scratchieItemForm.setSessionMapID(sessionMapID);
	scratchieItemForm.setContentFolderID(contentFolderID);

	List<QbOption> answerList = new ArrayList<>();
	for (int i = 0; i < ScratchieConstants.INITIAL_ANSWERS_NUMBER; i++) {
	    QbOption answer = new QbOption();
	    answer.setDisplayOrder(i + 1);
	    answerList.add(answer);
	}
	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return "pages/authoring/parts/additem";
    }

    /**
     * Display edit page for existed scratchie item.
     */
    @RequestMapping("/editItem")
    private String editItem(@ModelAttribute("scratchieItemForm") ScratchieItemForm scratchieItemForm,
	    HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);

	int itemIdx = NumberUtils.toInt(request.getParameter(ScratchieConstants.PARAM_ITEM_INDEX), -1);
	ScratchieItem item = null;
	if (itemIdx != -1) {
	    SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	    List<ScratchieItem> rList = new ArrayList<>(itemList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		scratchieItemForm.setTitle(item.getQbQuestion().getName());
		scratchieItemForm.setDescription(item.getQbQuestion().getDescription());
		if (itemIdx >= 0) {
		    scratchieItemForm.setItemIndex(new Integer(itemIdx).toString());
		}

		List<QbOption> answerList = item.getQbQuestion().getQbOptions();
		request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);

		scratchieItemForm.setContentFolderID(contentFolderID);
	    }
	}
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return "pages/authoring/parts/additem";
    }
    
    /**
     * QB callback handler which adds selected QbQuestion into question list.
     */
    @RequestMapping(value = "/importQbQuestion", method = RequestMethod.POST)
    private String importQbQuestion(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	
	Long qbQuestionUid = WebUtil.readLongParam(request, "qbQuestionUid");
	QbQuestion qbQuestion = qbService.getQbQuestionByUid(qbQuestionUid);
	
	//create new ScratchieItem and assign imported qbQuestion to it
	ScratchieItem item = new ScratchieItem();
	item.setQbQuestion(qbQuestion);
	int maxSeq = 1;
	if (itemList != null && itemList.size() > 0) {
	    ScratchieItem last = itemList.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	item.setDisplayOrder(maxSeq);
	item.setQbQuestionModified(IQbService.QUESTION_MODIFIED_NONE);
	itemList.add(item);


	// evict everything manually as we do not use DTOs, just real entities
	// without eviction changes would be saved immediately into DB
//	scratchieService.releaseFromCache(item);
//	scratchieService.releaseFromCache(item);
//	scratchieService.releaseFromCache(item.getQbQuestion());
	
//	request.setAttribute("qbQuestionModified", item.getQbQuestionModified());

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
    }

    /**
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    private String saveItem(@ModelAttribute("scratchieItemForm") ScratchieItemForm scratchieItemForm,
	    HttpServletRequest request) {

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(scratchieItemForm.getSessionMapID());
	// check whether it is "edit(old Question)" or "add(new Question)"
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	int itemIdx = NumberUtils.toInt(scratchieItemForm.getItemIndex(), -1);
	ScratchieItem item = null;

	if (itemIdx == -1) { // add
	    item = new ScratchieItem();
	    QbQuestion qbQuestion = new QbQuestion();
	    qbQuestion.setType(QbQuestion.TYPE_MULTIPLE_CHOICE);
	    item.setQbQuestion(qbQuestion);
	    int maxSeq = 1;
	    if (itemList != null && itemList.size() > 0) {
		ScratchieItem last = itemList.last();
		maxSeq = last.getDisplayOrder() + 1;
	    }
	    item.setDisplayOrder(maxSeq);
	    itemList.add(item);
	} else { // edit
	    List<ScratchieItem> rList = new ArrayList<>(itemList);
	    item = rList.get(itemIdx);
	}

	QbQuestion baseLine = item.getQbQuestion().clone();
	// evict everything manually as we do not use DTOs, just real entities
	// without eviction changes would be saved immediately into DB
	scratchieService.releaseFromCache(item);
	scratchieService.releaseFromCache(baseLine);
	scratchieService.releaseFromCache(item.getQbQuestion());

	item.getQbQuestion().setName(scratchieItemForm.getTitle());
	item.getQbQuestion().setDescription(scratchieItemForm.getDescription());

	// set options
	Set<QbOption> answerList = getAnswersFromRequest(request, true);
	List<QbOption> answers = new ArrayList<>();
	int orderId = 0;
	for (QbOption answer : answerList) {
	    answer.setDisplayOrder(orderId++);
	    answers.add(answer);
	}
	item.getQbQuestion().setQbOptions(answers);

	item.setQbQuestionModified(scratchieService.isQbQuestionModified(baseLine, item.getQbQuestion()));
	request.setAttribute("qbQuestionModified", item.getQbQuestionModified());

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, scratchieItemForm.getSessionMapID());
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Parses questions extracted from IMS QTI file and adds them as new items.
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping("/saveQTI")
    private String saveQTI(HttpServletRequest request) throws UnsupportedEncodingException {
	// big part of code was taken from saveItem() method
	String sessionMapId = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);
	for (Question question : questions) {
	    ScratchieItem item = new ScratchieItem();
	    int maxSeq = 1;
	    if (itemList != null && itemList.size() > 0) {
		ScratchieItem last = itemList.last();
		maxSeq = last.getDisplayOrder() + 1;
	    }
	    item.setDisplayOrder(maxSeq);
	    item.getQbQuestion().setName(question.getTitle());
	    item.getQbQuestion().setDescription(QuestionParser.processHTMLField(question.getText(), false,
		    contentFolderID, question.getResourcesFolderPath()));

	    TreeSet<QbOption> answerList = new TreeSet<>();
	    String correctAnswer = null;
	    int orderId = 1;
	    if (question.getAnswers() != null) {
		for (Answer answer : question.getAnswers()) {
		    String answerText = QuestionParser.processHTMLField(answer.getText(), false, contentFolderID,
			    question.getResourcesFolderPath());
		    if (correctAnswer != null && correctAnswer.equals(answerText)) {
			log.warn("Skipping an answer with same text as the correct answer: " + answerText);
			continue;
		    }
		    QbOption scratchieAnswer = new QbOption();
		    scratchieAnswer.setName(answerText);
		    scratchieAnswer.setDisplayOrder(orderId++);

		    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			if (correctAnswer == null) {
			    scratchieAnswer.setCorrect(true);
			    correctAnswer = answerText;
			} else {
			    log.warn(
				    "Choosing only first correct answer, despite another one was found: " + answerText);
			    scratchieAnswer.setCorrect(false);
			}
		    } else {
			scratchieAnswer.setCorrect(false);
		    }

		    answerList.add(scratchieAnswer);
		}
	    }

	    if (correctAnswer == null) {
		log.warn("No correct answer found for question: " + question.getText());
		continue;
	    }

	    item.getQbQuestion().setQbOptions(new ArrayList<>(answerList));
	    itemList.add(item);
	    if (log.isDebugEnabled()) {
		log.debug("Added question: " + question.getText());
	    }
	}

	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Prepares Scratchie content for QTI packing
     */
    @SuppressWarnings({ "unchecked" })
    @RequestMapping("/exportQTI")
    private String exportQTI(HttpServletRequest request, HttpServletResponse response)
	    throws UnsupportedEncodingException {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	List<Question> questions = new LinkedList<>();

	for (ScratchieItem item : itemList) {
	    Question question = new Question();

	    question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);
	    question.setTitle(item.getQbQuestion().getName());
	    question.setText(item.getQbQuestion().getDescription());

	    List<Answer> answers = new ArrayList<>();
	    Set<QbOption> scratchieAnswers = new TreeSet<>();
	    scratchieAnswers.addAll(item.getQbQuestion().getQbOptions());

	    for (QbOption itemAnswer : scratchieAnswers) {
		Answer answer = new Answer();
		answer.setText(itemAnswer.getName());
		// there is no LAMS interface to adjust, so use the default 1 point
		Float score = itemAnswer.isCorrect() ? 1F : 0;
		answer.setScore(score);
		answers.add(answer);
	    }

	    question.setAnswers(answers);
	    questions.add(question);
	}

	String title = request.getParameter("title");
	QuestionExporter exporter = new QuestionExporter(title, questions.toArray(Question.QUESTION_ARRAY_TYPE));
	exporter.exportQTIPackage(request, response);

	return null;
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
    @RequestMapping("/removeItem")
    private String removeItem(HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	int itemIndex = NumberUtils.toInt(request.getParameter(ScratchieConstants.PARAM_ITEM_INDEX), -1);
	if (itemIndex != -1) {
	    List<ScratchieItem> rList = new ArrayList<>(itemList);
	    ScratchieItem item = rList.remove(itemIndex);
	    itemList.clear();
	    itemList.addAll(rList);

	    // add to delList
	    List<ScratchieItem> delList = getDeletedItemList(sessionMap);
	    delList.add(item);
	}

	request.setAttribute(ScratchieConstants.ATTR_ITEM_LIST, itemList);
	return "pages/authoring/parts/itemlist";
    }

    /**
     * Move up current item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/upItem")
    private String upItem(HttpServletRequest request) {
	return switchItem(request, true);
    }

    /**
     * Move down current item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downItem")
    private String downItem(HttpServletRequest request) {
	return switchItem(request, false);
    }

    private String switchItem(HttpServletRequest request, boolean up) {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	int itemIndex = NumberUtils.toInt(request.getParameter(ScratchieConstants.PARAM_ITEM_INDEX), -1);
	if (itemIndex != -1) {
	    List<ScratchieItem> rList = new ArrayList<>(itemList);

	    // get current and the target item, and switch their sequnece
	    ScratchieItem item = rList.get(itemIndex);
	    ScratchieItem repOption;
	    if (up) {
		repOption = rList.get(--itemIndex);
	    } else {
		repOption = rList.get(++itemIndex);
	    }

	    int upSeqId = repOption.getDisplayOrder();
	    repOption.setDisplayOrder(item.getDisplayOrder());
	    item.setDisplayOrder(upSeqId);

	    // put back list, it will be sorted again
	    itemList.clear();
	    itemList.addAll(rList);
	}

	request.setAttribute(ScratchieConstants.ATTR_ITEM_LIST, itemList);
	return "pages/authoring/parts/itemlist";
    }

    // ----------------------- Answers functions ---------------

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addAnswer")
    private String addAnswer(HttpServletRequest request) {

	SortedSet<QbOption> answerList = getAnswersFromRequest(request, false);

	QbOption answer = new QbOption();
	int maxSeq = 1;
	if (answerList != null && answerList.size() > 0) {
	    QbOption last = answerList.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	answer.setDisplayOrder(maxSeq);
	answerList.add(answer);

	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	return "pages/authoring/parts/answerlist";
    }

    /**
     * Ajax call, remove the given answer.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/removeAnswer")
    private String removeAnswer(HttpServletRequest request) {

	SortedSet<QbOption> answerList = getAnswersFromRequest(request, false);

	int answerIndex = NumberUtils.toInt(request.getParameter(ScratchieConstants.PARAM_ANSWER_INDEX), -1);
	if (answerIndex != -1) {
	    List<QbOption> rList = new ArrayList<>(answerList);
	    rList.remove(answerIndex);
	    answerList.clear();
	    answerList.addAll(rList);
	}

	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	return "pages/authoring/parts/answerlist";
    }

    /**
     * Move up current answer.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/upAnswer")
    private String upAnswer(HttpServletRequest request) {
	return switchAnswer(request, true);
    }

    /**
     * Move down current answer.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downAnswer")
    private String downAnswer(HttpServletRequest request) {
	return switchAnswer(request, false);
    }

    private String switchAnswer(HttpServletRequest request, boolean up) {
	SortedSet<QbOption> answerList = getAnswersFromRequest(request, false);

	int answerIndex = NumberUtils.toInt(request.getParameter(ScratchieConstants.PARAM_ANSWER_INDEX), -1);
	if (answerIndex != -1) {
	    List<QbOption> rList = new ArrayList<>(answerList);

	    // get current and the target item, and switch their sequnece
	    QbOption answer = rList.get(answerIndex);
	    QbOption repAnswer;
	    if (up) {
		repAnswer = rList.get(--answerIndex);
	    } else {
		repAnswer = rList.get(++answerIndex);
	    }

	    int upSeqId = repAnswer.getDisplayOrder();
	    repAnswer.setDisplayOrder(answer.getDisplayOrder());
	    answer.setDisplayOrder(upSeqId);

	    // put back list, it will be sorted again
	    answerList.clear();
	    answerList.addAll(rList);
	}

	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);
	return "pages/authoring/parts/answerlist";
    }

    // ----------------------- PedagogicalPlannerForm ---------------

    @RequestMapping("/initPedagogicalPlannerForm")
    public String initPedagogicalPlannerForm(
	    @ModelAttribute("pedagogicalPlannerForm") ScratchiePedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentID);
	pedagogicalPlannerForm.fillForm(scratchie);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	pedagogicalPlannerForm.setContentFolderID(contentFolderId);
	return "pages/authoring/pedagogicalPlannerForm";
    }

    @RequestMapping(value = "/saveOrUpdatePedagogicalPlannerForm", method = RequestMethod.POST)
    public String saveOrUpdatePedagogicalPlannerForm(
	    @ModelAttribute("pedagogicalPlannerForm") ScratchiePedagogicalPlannerForm pedagogicalPlannerForm,
	    HttpServletRequest request) throws IOException {
	MultiValueMap<String, String> errorMap = pedagogicalPlannerForm.validate(messageService);
	if (errorMap.isEmpty()) {
	    Scratchie scratchie = scratchieService.getScratchieByContentId(pedagogicalPlannerForm.getToolContentID());
	    scratchie.setInstructions(pedagogicalPlannerForm.getInstructions());
	    scratchieService.saveOrUpdateScratchie(scratchie);
	} else {
	    request.setAttribute("errorMap", errorMap);
	}
	return "pages/authoring/pedagogicalPlannerForm";
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * List save current scratchie items.
     *
     * @param request
     * @return
     */
    private SortedSet<ScratchieItem> getItemList(SessionMap<String, Object> sessionMap) {
	SortedSet<ScratchieItem> list = (SortedSet<ScratchieItem>) sessionMap.get(ScratchieConstants.ATTR_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<>(new ScratchieItemComparator());
	    sessionMap.put(ScratchieConstants.ATTR_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted scratchie items, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List<ScratchieItem> getDeletedItemList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, ScratchieConstants.ATTR_DELETED_ITEM_LIST);
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
     * Get answer options from <code>HttpRequest</code>
     *
     * @param request
     * @param isForSaving
     *            whether the blank options will be preserved or not
     *
     */
    private TreeSet<QbOption> getAnswersFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, ScratchieConstants.ATTR_ANSWER_LIST);
	Integer correctAnswerIndex = (paramMap.get(ScratchieConstants.ATTR_ANSWER_CORRECT) == null) ? null
		: NumberUtils.toInt(paramMap.get(ScratchieConstants.ATTR_ANSWER_CORRECT));

	int count = NumberUtils.toInt(paramMap.get(ScratchieConstants.ATTR_ANSWER_COUNT));
	TreeSet<QbOption> answerList = new TreeSet<>();
	for (int i = 0; i < count; i++) {

	    String answerDescription = paramMap.get(ScratchieConstants.ATTR_ANSWER_DESCRIPTION_PREFIX + i);
	    if ((answerDescription == null) && isForSaving) {
		continue;
	    }

	    QbOption answer = null;
	    String uidStr = paramMap.get(ScratchieConstants.ATTR_ANSWER_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		answer = scratchieService.getQbOptionByUid(uid);
		scratchieService.releaseFromCache(answer.getQbQuestion());
	    } else {
		answer = new QbOption();
	    }

	    String orderIdStr = paramMap.get(ScratchieConstants.ATTR_ANSWER_ORDER_ID_PREFIX + i);
	    Integer orderId = NumberUtils.toInt(orderIdStr);
	    answer.setDisplayOrder(orderId);
	    answer.setName(answerDescription);
	    if ((correctAnswerIndex != null) && correctAnswerIndex.equals(orderId)) {
		answer.setCorrect(true);
	    }
	    answerList.add(answer);

	}

	return answerList;
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
	    if (pair == null || pair.length != 2) {
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
     */
    private void removeNewLineCharacters(ScratchieItem item) {
	Collection<QbOption> answers = item.getQbQuestion().getQbOptions();
	if (answers != null) {
	    for (QbOption answer : answers) {
		String answerDescription = answer.getName();
		if (answerDescription != null) {
		    answerDescription = answerDescription.replaceAll("[\n\r\f]", "");
		    answer.setName(answerDescription);
		}
	    }

	}
    }
}