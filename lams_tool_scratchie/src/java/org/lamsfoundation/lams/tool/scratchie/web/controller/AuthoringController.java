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
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.QbConstants;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchieForm;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchiePedagogicalPlannerForm;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    private IScratchieService scratchieService;
    @Autowired
    private IQbService qbService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("scratchieMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    private String start(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	return starting(authoringForm, request, mode);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    private String definelater(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request,
	    @RequestParam Long toolContentID) throws ServletException {
	// update define later flag to true
	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentID);

	scratchie.setDefineLater(true);
	scratchieService.saveOrUpdateScratchie(scratchie);

	//audit log the teacher has started editing activity in monitor
	scratchieService.auditLogStartEditingActivityInMonitor(toolContentID);

	return starting(authoringForm, request, ToolAccessMode.TEACHER);
    }

    private String starting(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request,
	    ToolAccessMode mode) throws ServletException {
	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	authoringForm.setSessionMapID(sessionMap.getSessionID());

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	authoringForm.setContentFolderID(contentFolderID);

	// save toolContentID into HTTPSession
	Long contentId = WebUtil.readLongParam(request, ScratchieConstants.PARAM_TOOL_CONTENT_ID);
	sessionMap.put(ScratchieConstants.PARAM_TOOL_CONTENT_ID, contentId);

	Scratchie scratchie = null;
	try {
	    scratchie = scratchieService.getScratchieByContentId(contentId);
	    // if scratchie does not exist, try to use default content instead.
	    if (scratchie == null) {
		scratchie = scratchieService.getDefaultContent(contentId);
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

	// init scratchie item list
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	itemList.clear();
	itemList.addAll(scratchie.getScratchieItems());
	request.setAttribute(ScratchieConstants.ATTR_ITEM_LIST, itemList);

	// If there is no order id, set it up
	int i = 1;
	for (ScratchieItem scratchieItem : itemList) {
	    if (scratchieItem.getDisplayOrder() != i) {
		scratchieItem.setDisplayOrder(i);
	    }
	    i++;

	    // prepare other version data for displaying
	    qbService.fillVersionMap(scratchieItem.getQbQuestion());
	}

	//display confidence providing activities
	Set<ToolActivity> confidenceLevelsActivities = scratchieService
		.getActivitiesProvidingConfidenceLevels(contentId);
	sessionMap.put(ScratchieConstants.ATTR_ACTIVITIES_PROVIDING_CONFIDENCE_LEVELS, confidenceLevelsActivities);

	//display activities providing VSA answers
	Set<ToolActivity> activitiesProvidingVsaAnswers = scratchieService.getActivitiesProvidingVsaAnswers(contentId);
	sessionMap.put(ScratchieConstants.ATTR_ACTIVITIES_PROVIDING_VSA_ANSWERS, activitiesProvidingVsaAnswers);

	String notifyCloseURL = request.getParameter("notifyCloseURL");
	if (StringUtils.isNotBlank(notifyCloseURL)) {
	    try {
		request.setAttribute("notifyCloseURL", URLEncoder.encode(notifyCloseURL, "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		throw new ServletException("Exception while encoding notifyCloseURL: " + notifyCloseURL, e);
	    }
	}
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	sessionMap.put(ScratchieConstants.ATTR_RESOURCE_FORM, authoringForm);

	boolean questionEtherpadEnabled = StringUtils.isNotBlank(Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY));
	sessionMap.put(ScratchieConstants.ATTR_IS_QUESTION_ETHERPAD_ENABLED, questionEtherpadEnabled);

	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    @RequestMapping("/init")
    private String initPage(@ModelAttribute("authoringForm") ScratchieForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	ScratchieForm existForm = (ScratchieForm) sessionMap.get(ScratchieConstants.ATTR_RESOURCE_FORM);

	try {
	    PropertyUtils.copyProperties(authoringForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all scratchie item, information etc.
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    private String updateContent(@ModelAttribute("authoringForm") ScratchieForm authoringForm,
	    HttpServletRequest request) throws Exception {
	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	Scratchie scratchie = authoringForm.getScratchie();

	// **********************************Get Scratchie PO*********************
	Scratchie scratchiePO = scratchieService.getScratchieByContentId(authoringForm.getScratchie().getContentId());

	//allow using old and modified questions together
	Set<ScratchieItem> oldItems = (scratchiePO == null) ? new HashSet<>()
		: new HashSet<>(scratchiePO.getScratchieItems());

	if (scratchiePO == null) {
	    // new Scratchie, create it.
	    scratchiePO = scratchie;
	    scratchiePO.setCreated(new Timestamp(new Date().getTime()));
	    scratchiePO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    // copyProperties() below sets scratchiePO's items to empty collection
	    // but the items still exist in Hibernate cache, so we need to evict them now
	    for (ScratchieItem item : oldItems) {
		scratchieService.releaseFromCache(item);
	    }
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
	Set<ScratchieItem> items = new TreeSet<>();
	SortedSet<ScratchieItem> newItems = getItemList(sessionMap);
	Iterator<ScratchieItem> iter = newItems.iterator();
	while (iter.hasNext()) {
	    ScratchieItem item = iter.next();
	    if (item != null) {
		item.setToolContentId(scratchiePO.getContentId());
		items.add(item);
	    }
	}
	scratchiePO.setScratchieItems(items);

	// **********************************************
	// finally persist scratchiePO again
	scratchieService.saveOrUpdateScratchie(scratchiePO);
	authoringForm.setScratchie(scratchiePO);

	//recalculate results in case content is edited from monitoring
	if (mode.isTeacher()) {
	    scratchieService.recalculateUserAnswers(scratchiePO, oldItems, newItems);
	}

	// delete items from database
	List<ScratchieItem> deletedItems = getDeletedItemList(sessionMap);
	iter = deletedItems.iterator();
	while (iter.hasNext()) {
	    ScratchieItem item = iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		scratchieService.deleteScratchieItem(item.getUid());
	    }
	}

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "pages/authoring/authoring";
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    @RequestMapping("/addItem")
    private String addItem(@ModelAttribute("scratchieItemForm") QbQuestionForm form, HttpServletRequest request,
	    @RequestParam Integer questionType) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	form.setSessionMapID(sessionMap.getSessionID());

	List<QbOption> optionList = new ArrayList<>();
	int initialOptionsNumber = questionType == QbQuestion.TYPE_MULTIPLE_CHOICE
		? ScratchieConstants.INITIAL_MCQ_OPTIONS_NUMBER
		: 2;
	for (int i = 0; i < initialOptionsNumber; i++) {
	    QbOption option = new QbOption();
	    option.setDisplayOrder(i + 1);
	    optionList.add(option);
	}
	request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);

	QbUtils.fillFormWithUserCollections(qbService, form, null);

	// generate a new contentFolderID for new question
	String contentFolderID = FileUtil.generateUniqueContentFolderID();
	form.setContentFolderID(contentFolderID);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	return questionType == QbQuestion.TYPE_MULTIPLE_CHOICE ? "pages/authoring/parts/addMcq"
		: "pages/authoring/parts/addVsa";
    }

    /**
     * Display edit page for existed scratchie item.
     */
    @RequestMapping("/editItem")
    private String editItem(@ModelAttribute("scratchieItemForm") QbQuestionForm form, HttpServletRequest request) {
	// get back sessionMAP
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	int itemIdx = WebUtil.readIntParam(request, ScratchieConstants.PARAM_ITEM_INDEX);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	List<ScratchieItem> rList = new ArrayList<>(itemList);
	ScratchieItem item = rList.get(itemIdx);
	QbQuestion qbQuestion = item.getQbQuestion();

	form.setTitle(qbQuestion.getName());
	form.setDescription(qbQuestion.getDescription());
	if (itemIdx >= 0) {
	    form.setItemIndex(String.valueOf(itemIdx));
	}
	form.setQuestionType(qbQuestion.getType());
	boolean isMcqQuestionType = qbQuestion.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE;
	if (!isMcqQuestionType) {
	    form.setFeedback(qbQuestion.getFeedback());
	    form.setCaseSensitive(qbQuestion.isCaseSensitive());
	    form.setAutocompleteEnabled(qbQuestion.isAutocompleteEnabled());
	}

	List<QbOption> optionList = qbQuestion.getQbOptions();
	request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);

	form.setSessionMapID(sessionMap.getSessionID());
	QbUtils.fillFormWithUserCollections(qbService, ScratchieConstants.TOOL_SIGNATURE, form, qbQuestion.getUid());

	form.setContentFolderID(qbQuestion.getContentFolderId());
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, qbQuestion.getContentFolderId());
	return isMcqQuestionType ? "pages/authoring/parts/addMcq" : "pages/authoring/parts/addVsa";
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
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
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
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	//check whether this QB question is a duplicate
	for (ScratchieItem item : itemList) {
	    if (qbQuestionUid.equals(item.getQbQuestion().getUid())) {
		//let jsp know it's a duplicate
		return "forward:/authoring/showDuplicateQuestionError.do";
	    }
	}

	QbQuestion qbQuestion = qbService.getQuestionByUid(qbQuestionUid);

	//create new ScratchieItem and assign imported qbQuestion to it
	ScratchieItem item = new ScratchieItem();
	item.setQbQuestion(qbQuestion);
	int maxSeq = 1;
	if (itemList != null && itemList.size() > 0) {
	    ScratchieItem last = itemList.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	item.setDisplayOrder(maxSeq);
	itemList.add(item);
	ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

	// prepare other version data for displaying
	qbService.fillVersionMap(item.getQbQuestion());

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/authoring/parts/itemlist";
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
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    @RequestMapping(value = "/saveItem", method = RequestMethod.POST)
    private String saveItem(@ModelAttribute("scratchieItemForm") QbQuestionForm form, HttpServletRequest request,
	    @RequestParam(name = "newVersion", required = false) boolean enforceNewVersion) {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(form.getSessionMapID());
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	int itemIdx = NumberUtils.toInt(form.getItemIndex(), -1);

	// check whether it is "edit(old Question)" or "add(new Question)"
	QbQuestion qbQuestion;
	ScratchieItem item;
	boolean isDefaultQuestion = false;
	final boolean isAddingQuestion = itemIdx == -1;
	if (isAddingQuestion) { // add
	    qbQuestion = new QbQuestion();
	    qbQuestion.setType(form.getQuestionType());
	    qbQuestion.setMaxMark(1);
	    qbQuestion.setPenaltyFactor(0);

	    item = new ScratchieItem();
	    int maxSeq = 1;
	    if (itemList != null && itemList.size() > 0) {
		ScratchieItem last = itemList.last();
		maxSeq = last.getDisplayOrder() + 1;
	    }
	    item.setDisplayOrder(maxSeq);
	    itemList.add(item);

	    // edit
	} else {
	    List<ScratchieItem> rList = new ArrayList<>(itemList);
	    item = rList.get(itemIdx);
	    long qbQuestionUid = item.getQbQuestion().getUid();
	    qbQuestion = qbService.getQuestionByUid(qbQuestionUid);
	    qbService.releaseFromCache(qbQuestion);

	    // if it is a default question, do not modify it
	    // treat it as a new question
	    isDefaultQuestion = qbService.isQuestionDefaultInTool(qbQuestionUid, ScratchieConstants.TOOL_SIGNATURE);
	    if (isDefaultQuestion) {
		String questionContentFolderID = FileUtil.generateUniqueContentFolderID();
		qbQuestion.setContentFolderId(questionContentFolderID);
		qbQuestion.setQuestionId(qbService.generateNextQuestionId()); // generate a new question ID right away, so another user won't "take it"
	    }
	}

	QbQuestion oldQbQuestion = qbQuestion.clone();
	// evict everything manually as we do not use DTOs, just real entities
	// without eviction changes would be saved immediately into DB
	scratchieService.releaseFromCache(oldQbQuestion);

	qbQuestion.setName(form.getTitle().strip());
	qbQuestion.setDescription(form.getDescription().strip());
	qbQuestion.setContentFolderId(form.getContentFolderID());

	boolean isMcqQuestionType = qbQuestion.getType() == QbQuestion.TYPE_MULTIPLE_CHOICE;
	//handle VSA question type
	if (!isMcqQuestionType) {
	    qbQuestion.setFeedback(form.getFeedback());
	    qbQuestion.setCaseSensitive(form.isCaseSensitive());
	    qbQuestion.setAutocompleteEnabled(form.isAutocompleteEnabled());
	}

	// set options
	Set<QbOption> optionList = getOptionsFromRequest(request, isMcqQuestionType, true);
	List<QbOption> options = new ArrayList<>(optionList);
	qbQuestion.setQbOptions(options);

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
		updatedQuestion.setVersion(qbService.getMaxQuestionVersion(qbQuestion.getQuestionId()) + 1);
		updatedQuestion.setCreateDate(new Date());
	    }
		break;
	    case IQbService.QUESTION_MODIFIED_ID_BUMP: {
		// new question gets created
		updatedQuestion = qbQuestion.clone();
		updatedQuestion.clearID();
		updatedQuestion.setQuestionId(qbService.generateNextQuestionId());
		updatedQuestion.setVersion(1);
		updatedQuestion.setCreateDate(new Date());
	    }
		break;
	    case IQbService.QUESTION_MODIFIED_NONE: {
		// save the old question anyway, as it may contain some minor changes (like title or description change)
		updatedQuestion = qbQuestion;
	    }
		break;
	}
	userManagementService.save(updatedQuestion);
	item.setQbQuestion(updatedQuestion);

	request.setAttribute("qbQuestionModified", isQbQuestionModified);
	if (isQbQuestionModified == IQbService.QUESTION_MODIFIED_VERSION_BUMP) {
	    request.setAttribute("oldQbQuestionUid", qbQuestion.getUid());
	    request.setAttribute("newQbQuestionUid", updatedQuestion.getUid());
	}

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

	// prepare other version data for displaying
	qbService.fillVersionMap(item.getQbQuestion());

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, form.getSessionMapID());
	return "pages/authoring/parts/itemlist";
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
    @RequestMapping(path = "/removeItem", method = RequestMethod.POST)
    private String removeItem(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
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
     * Caches references order, along with this caches MaxMarks.
     */
    @RequestMapping("/cacheItemsOrder")
    @ResponseStatus(HttpStatus.OK)
    public void cacheItemsOrder(HttpServletRequest request) throws ServletException, IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	//cache references' sequenceIds
	Map<String, String> sequenceIdsParamMap = splitRequestParameter(request,
		ScratchieConstants.ATTR_REFERENCES_SEQUENCE_IDS);

	Set<ScratchieItem> updatedItemList = new TreeSet<>(new ScratchieItemComparator());
	for (ScratchieItem item : itemList) {
	    String newSequenceId = sequenceIdsParamMap
		    .get(ScratchieConstants.PARAM_SEQUENCE_ID + item.getDisplayOrder());
	    item.setDisplayOrder(Integer.valueOf(newSequenceId));
	    updatedItemList.add(item);
	}
	itemList.clear();
	itemList.addAll(updatedItemList);
    }

    @RequestMapping("/changeItemQuestionVersion")
    private String changeItemQuestionVersion(@RequestParam int itemIndex, @RequestParam long newQbQuestionUid,
	    HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	List<ScratchieItem> rList = new ArrayList<>(itemList);
	ScratchieItem item = rList.get(itemIndex);
	QbQuestion newQbQuestion = qbService.getQuestionByUid(newQbQuestionUid);
	qbService.fillVersionMap(newQbQuestion);
	item.setQbQuestion(newQbQuestion);

	request.setAttribute(ScratchieConstants.ATTR_ITEM_LIST, itemList);
	return "pages/authoring/parts/itemlist";
    }

    // ----------------------- Options functions ---------------

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

	SortedSet<QbOption> optionList = getOptionsFromRequest(request, true, false);

	QbOption option = new QbOption();
	int maxSeq = 1;
	if (optionList != null && optionList.size() > 0) {
	    QbOption last = optionList.last();
	    maxSeq = last.getDisplayOrder() + 1;
	}
	option.setDisplayOrder(maxSeq);
	optionList.add(option);

	request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	return "pages/authoring/parts/optionlist";
    }

    /**
     * Ajax call, remove the given option.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/removeAnswer")
    private String removeAnswer(HttpServletRequest request) {

	SortedSet<QbOption> optionList = getOptionsFromRequest(request, true, false);

	int optionIndex = NumberUtils.toInt(request.getParameter(ScratchieConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<QbOption> rList = new ArrayList<>(optionList);
	    rList.remove(optionIndex);
	    optionList.clear();
	    optionList.addAll(rList);
	}

	request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	return "pages/authoring/parts/optionlist";
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
    @RequestMapping("/upAnswer")
    private String upAnswer(HttpServletRequest request) {
	return switchOption(request, true);
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
    @RequestMapping("/downAnswer")
    private String downAnswer(HttpServletRequest request) {
	return switchOption(request, false);
    }

    private String switchOption(HttpServletRequest request, boolean up) {
	SortedSet<QbOption> optionList = getOptionsFromRequest(request, true, false);

	int optionIndex = NumberUtils.toInt(request.getParameter(ScratchieConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<QbOption> rList = new ArrayList<>(optionList);

	    // get current and the target item, and switch their sequnece
	    QbOption option = rList.get(optionIndex);
	    QbOption repOption;
	    if (up) {
		repOption = rList.get(--optionIndex);
	    } else {
		repOption = rList.get(++optionIndex);
	    }

	    int upSeqId = repOption.getDisplayOrder();
	    repOption.setDisplayOrder(option.getDisplayOrder());
	    option.setDisplayOrder(upSeqId);

	    // put back list, it will be sorted again
	    optionList.clear();
	    optionList.addAll(rList);
	}

	request.setAttribute(QbConstants.ATTR_OPTION_LIST, optionList);
	return "pages/authoring/parts/optionlist";
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
    @SuppressWarnings("unchecked")
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
    @SuppressWarnings("unchecked")
    private List<ScratchieItem> getDeletedItemList(SessionMap<String, Object> sessionMap) {
	List<ScratchieItem> list = (List<ScratchieItem>) sessionMap.get(ScratchieConstants.ATTR_DELETED_ITEM_LIST);
	if (list == null) {
	    list = new ArrayList<>();
	    sessionMap.put(ScratchieConstants.ATTR_DELETED_ITEM_LIST, list);
	}
	return list;
    }

    /**
     * Get options from <code>HttpRequest</code>
     *
     * @param request
     * @param isForSaving
     *            whether the blank options will be preserved or not
     *
     */
    private TreeSet<QbOption> getOptionsFromRequest(HttpServletRequest request, boolean isMcqQuestion,
	    boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, QbConstants.ATTR_OPTION_LIST);
	Integer correctOptionIndex = (paramMap.get(QbConstants.ATTR_OPTION_CORRECT) == null) ? null
		: NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_CORRECT));

	int count = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_COUNT));
	TreeSet<QbOption> optionList = new TreeSet<>();
	for (int i = 0; i < count; i++) {

	    String optionName = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
	    if ((optionName == null) && isForSaving && isMcqQuestion) {
		continue;
	    }

	    QbOption option = null;
	    String uidStr = paramMap.get(QbConstants.ATTR_OPTION_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		option = scratchieService.getQbOptionByUid(uid);
		scratchieService.releaseFromCache(option.getQbQuestion());
	    } else {
		option = new QbOption();
	    }

	    Integer displayOrder = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_DISPLAY_ORDER_PREFIX + i));
	    option.setDisplayOrder(displayOrder);
	    option.setName(optionName);
	    option.setCorrect((correctOptionIndex != null) && correctOptionIndex.equals(displayOrder) && isMcqQuestion);

	    //handle VSA question type
	    if (!isMcqQuestion) {
		float maxMark = i == 0 ? 1 : 0;
		option.setMaxMark(maxMark);

		String feedback = paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i);
		option.setFeedback(feedback);
	    }

	    optionList.add(option);

	}

	return optionList;
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

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }
}