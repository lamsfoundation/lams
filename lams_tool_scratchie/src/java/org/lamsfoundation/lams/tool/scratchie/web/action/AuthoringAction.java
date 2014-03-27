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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.scratchie.web.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieAnswer;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieAnswerComparator;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchieForm;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchieItemForm;
import org.lamsfoundation.lams.tool.scratchie.web.form.ScratchiePedagogicalPlannerForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Andrey Balan
 */
public class AuthoringAction extends Action {

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// -----------------------Scratchie Author functions -----------
	String param = mapping.getParameter();
	if (param.equals("start")) {
	    ToolAccessMode mode = getAccessMode(request);
	    // teacher mode "check for new" button enter.
	    if (mode != null) {
		request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    } else {
		request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR.toString());
	    }
	    return start(mapping, form, request, response);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    IScratchieService service = getScratchieService();
	    Scratchie scratchie = service.getScratchieByContentId(contentId);

	    scratchie.setDefineLater(true);
	    service.saveOrUpdateScratchie(scratchie);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}

	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	// ----------------------- Scratchie item functions ---------
	if (param.equals("addItem")) {
	    return addItem(mapping, form, request, response);
	}
	if (param.equals("editItem")) {
	    return editItem(mapping, form, request, response);
	}
	if (param.equals("saveItem")) {
	    return saveItem(mapping, form, request, response);
	}
	if (param.equals("saveQTI")) {
	    return saveQTI(mapping, form, request, response);
	}
	if (param.equals("exportQTI")) {
	    return exportQTI(mapping, form, request, response);
	}
	if (param.equals("removeItem")) {
	    return removeItem(mapping, form, request, response);
	}
	if (param.equals("upItem")) {
	    return upItem(mapping, form, request, response);
	}
	if (param.equals("downItem")) {
	    return downItem(mapping, form, request, response);
	}
	// ----------------------- Answers functions ---------------
	if (param.equals("addAnswer")) {
	    return addAnswer(mapping, form, request, response);
	}
	if (param.equals("removeAnswer")) {
	    return removeAnswer(mapping, form, request, response);
	}
	if (param.equals("upAnswer")) {
	    return upAnswer(mapping, form, request, response);
	}
	if (param.equals("downAnswer")) {
	    return downAnswer(mapping, form, request, response);
	}
	// -----------------------PedagogicalPlanner functions ---------
	if (param.equals("initPedagogicalPlannerForm")) {
	    return initPedagogicalPlannerForm(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdatePedagogicalPlannerForm")) {
	    return saveOrUpdatePedagogicalPlannerForm(mapping, form, request, response);
	}

	return mapping.findForward(ScratchieConstants.ERROR);
    }

    /**
     * Read scratchie data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     * 
     * @throws ServletException
     * 
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, ScratchieConstants.PARAM_TOOL_CONTENT_ID));

	// get back the scratchie and item list and display them on page
	IScratchieService service = getScratchieService();

	List<ScratchieItem> items = null;
	Scratchie scratchie = null;
	ScratchieForm scratchieForm = (ScratchieForm) form;

	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	scratchieForm.setSessionMapID(sessionMap.getSessionID());

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	scratchieForm.setContentFolderID(contentFolderID);

	try {
	    scratchie = service.getScratchieByContentId(contentId);
	    // if scratchie does not exist, try to use default content instead.
	    if (scratchie == null) {
		scratchie = service.getDefaultContent(contentId);
		if (scratchie.getScratchieItems() != null) {
		    items = new ArrayList<ScratchieItem>(scratchie.getScratchieItems());
		} else {
		    items = null;
		}
	    } else {
		items = service.getAuthoredItems(scratchie.getUid());
	    }

	    scratchieForm.setScratchie(scratchie);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (items == null) {
	    items = new ArrayList<ScratchieItem>();
	} else {
	    ScratchieUser scratchieUser = null;
	    // handle system default question: createBy is null, now set it to
	    // current user
	    for (ScratchieItem item : items) {

		// sort answers by order id. it's needed only for the default answers. rest could be skipped
		TreeSet<ScratchieAnswer> answerList = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
		answerList.addAll(item.getAnswers());
		item.setAnswers(answerList);
	    }
	}
	// init scratchie item list
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	itemList.clear();
	itemList.addAll(items);

	// If there is no order id, set it up
	int i = 1;
	for (ScratchieItem scratchieItem : itemList) {
	    if (scratchieItem.getOrderId() == null || scratchieItem.getOrderId() != i) {
		scratchieItem.setOrderId(i);
	    }
	    i++;
	}

	sessionMap.put(ScratchieConstants.ATTR_RESOURCE_FORM, scratchieForm);
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     */
    private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	ScratchieForm existForm = (ScratchieForm) sessionMap.get(ScratchieConstants.ATTR_RESOURCE_FORM);

	ScratchieForm scratchieForm = (ScratchieForm) form;
	try {
	    PropertyUtils.copyProperties(scratchieForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor()) {
	    return mapping.findForward(ScratchieConstants.SUCCESS);
	} else {
	    return mapping.findForward(ScratchieConstants.DEFINE_LATER);
	}
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all scratchie item, information etc.
     */
    private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	ScratchieForm scratchieForm = (ScratchieForm) form;

	// get back sessionMAP
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(scratchieForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);

	Scratchie scratchie = scratchieForm.getScratchie();
	IScratchieService service = getScratchieService();

	// **********************************Get Scratchie
	// PO*********************
	Scratchie scratchiePO = service.getScratchieByContentId(scratchieForm.getScratchie().getContentId());
	if (scratchiePO == null) {
	    // new Scratchie, create it.
	    scratchiePO = scratchie;
	    scratchiePO.setCreated(new Timestamp(new Date().getTime()));
	    scratchiePO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    if (mode.isAuthor()) {
		Long uid = scratchiePO.getUid();
		PropertyUtils.copyProperties(scratchiePO, scratchie);
		// get back UID
		scratchiePO.setUid(uid);
	    } else { // if it is Teacher, then just update basic tab content
		     // (definelater)
		scratchiePO.setInstructions(scratchie.getInstructions());
		scratchiePO.setTitle(scratchie.getTitle());
		// change define later status
		scratchiePO.setDefineLater(false);
	    }
	    scratchiePO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// ************************* Handle scratchie items *******************
	// Handle scratchie items
	Set itemList = new LinkedHashSet();
	SortedSet topics = getItemList(sessionMap);
	Iterator iter = topics.iterator();
	while (iter.hasNext()) {
	    ScratchieItem item = (ScratchieItem) iter.next();
	    if (item != null) {
		removeNewLineCharacters(item);
		itemList.add(item);
	    }
	}
	scratchiePO.setScratchieItems(itemList);
	// delete instructino file from database.
	List delScratchieItemList = getDeletedItemList(sessionMap);
	iter = delScratchieItemList.iterator();
	while (iter.hasNext()) {
	    ScratchieItem item = (ScratchieItem) iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		service.deleteScratchieItem(item.getUid());
	    }
	}

	// **********************************************
	// finally persist scratchiePO again
	service.saveOrUpdateScratchie(scratchiePO);

	scratchieForm.setScratchie(scratchiePO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor()) {
	    return mapping.findForward("author");
	} else {
	    return mapping.findForward("monitor");
	}
    }

    /**
     * Ajax call, will add one more input line for new resource item instruction.
     */
    private ActionForward addItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	ScratchieItemForm itemForm = (ScratchieItemForm) form;
	itemForm.setSessionMapID(sessionMapID);
	itemForm.setContentFolderID(contentFolderID);

	List<ScratchieAnswer> answerList = new ArrayList<ScratchieAnswer>();
	for (int i = 0; i < ScratchieConstants.INITIAL_ANSWERS_NUMBER; i++) {
	    ScratchieAnswer answer = new ScratchieAnswer();
	    answer.setOrderId(i + 1);
	    answerList.add(answer);
	}
	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Display edit page for existed scratchie item.
     */
    private ActionForward editItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(ScratchieConstants.PARAM_ITEM_INDEX), -1);
	ScratchieItem item = null;
	if (itemIdx != -1) {
	    SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	    List<ScratchieItem> rList = new ArrayList<ScratchieItem>(itemList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		ScratchieItemForm itemForm = (ScratchieItemForm) form;
		itemForm.setTitle(item.getTitle());
		itemForm.setDescription(item.getDescription());
		if (itemIdx >= 0) {
		    itemForm.setItemIndex(new Integer(itemIdx).toString());
		}

		Set<ScratchieAnswer> answerList = item.getAnswers();
		request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);

		itemForm.setContentFolderID(contentFolderID);
	    }
	}
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * This method will get necessary information from assessment question form and save or update into
     * <code>HttpSession</code> AssessmentQuestionList. Notice, this save is not persist them into database, just save
     * <code>HttpSession</code> temporarily. Only they will be persist when the entire authoring page is being
     * persisted.
     */
    private ActionForward saveItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	ScratchieItemForm itemForm = (ScratchieItemForm) form;
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(itemForm.getSessionMapID());
	// check whether it is "edit(old Question)" or "add(new Question)"
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(), -1);
	ScratchieItem item = null;

	if (itemIdx == -1) { // add
	    item = new ScratchieItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (itemList != null && itemList.size() > 0) {
		ScratchieItem last = itemList.last();
		maxSeq = last.getOrderId() + 1;
	    }
	    item.setOrderId(maxSeq);
	    itemList.add(item);
	} else { // edit
	    List<ScratchieItem> rList = new ArrayList<ScratchieItem>(itemList);
	    item = rList.get(itemIdx);
	}

	item.setTitle(itemForm.getTitle());
	item.setDescription(itemForm.getDescription());

	// set options
	Set<ScratchieAnswer> answerList = getAnswersFromRequest(request, true);
	Set<ScratchieAnswer> answers = new LinkedHashSet<ScratchieAnswer>();
	int orderId = 0;
	for (ScratchieAnswer answer : answerList) {
	    answer.setOrderId(orderId++);
	    answers.add(answer);
	}
	item.setAnswers(answers);

	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, itemForm.getSessionMapID());
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    /**
     * Parses questions extracted from IMS QTI file and adds them as new items.
     */
    @SuppressWarnings("rawtypes")
    private ActionForward saveQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UnsupportedEncodingException {
	// big part of code was taken from saveItem() method
	String sessionMapId = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapId);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	Question[] questions = QuestionParser.parseQuestionChoiceForm(request);
	for (Question question : questions) {
	    ScratchieItem item = new ScratchieItem();
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (itemList != null && itemList.size() > 0) {
		ScratchieItem last = itemList.last();
		maxSeq = last.getOrderId() + 1;
	    }
	    item.setOrderId(maxSeq);
	    item.setTitle(question.getTitle());
	    item.setDescription(QuestionParser.processHTMLField(question.getText(), false, contentFolderID,
			question.getResourcesFolderPath()));

	    TreeSet<ScratchieAnswer> answerList = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
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
		    ScratchieAnswer scratchieAnswer = new ScratchieAnswer();
		    scratchieAnswer.setDescription(answerText);
		    scratchieAnswer.setOrderId(orderId++);

		    if ((answer.getScore() != null) && (answer.getScore() > 0)) {
			if (correctAnswer == null) {
			    scratchieAnswer.setCorrect(true);
			    correctAnswer = answerText;
			} else {
			    log.warn("Choosing only first correct answer, despite another one was found: " + answerText);
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

	    item.setAnswers(answerList);
	    itemList.add(item);
	    if (log.isDebugEnabled()) {
		log.debug("Added question: " + question.getText());
	    }
	}

	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapId);
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }
    
    /**
     * Prepares Scratchie content for QTI packing
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ActionForward exportQTI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UnsupportedEncodingException {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);
	List<Question> questions = new LinkedList<Question>();

	for (ScratchieItem item : itemList) {
	    Question question = new Question();
	    
	    question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);
	    question.setTitle(item.getTitle());
	    question.setText(item.getDescription());
	    List<Answer> answers = new ArrayList<Answer>();

	    for (ScratchieAnswer itemAnswer : (Set<ScratchieAnswer>) item.getAnswers()) {
		Answer answer = new Answer();

		answer.setText(itemAnswer.getDescription());
		// there is no LAMS interface to adjust, so use the default 1 point 
		Float score = itemAnswer.isCorrect() ? 1F : 0;
		answer.setScore(score);
		// answer order ID is 0-based, but questions is 1-based...
		answers.add(itemAnswer.getOrderId(), answer);
	    }

	    question.setAnswers(answers);
	    questions.add(item.getOrderId() - 1, question);
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
    private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	int itemIndex = NumberUtils.stringToInt(request.getParameter(ScratchieConstants.PARAM_ITEM_INDEX), -1);
	if (itemIndex != -1) {
	    List<ScratchieItem> rList = new ArrayList<ScratchieItem>(itemList);
	    ScratchieItem item = rList.remove(itemIndex);
	    itemList.clear();
	    itemList.addAll(rList);

	    // add to delList
	    List delList = getDeletedItemList(sessionMap);
	    delList.add(item);
	}

	request.setAttribute(ScratchieConstants.ATTR_ITEM_LIST, itemList);
	return mapping.findForward(ScratchieConstants.SUCCESS);
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
    private ActionForward upItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(mapping, request, true);
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
    private ActionForward downItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchItem(mapping, request, false);
    }

    private ActionForward switchItem(ActionMapping mapping, HttpServletRequest request, boolean up) {
	String sessionMapID = WebUtil.readStrParam(request, ScratchieConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	SortedSet<ScratchieItem> itemList = getItemList(sessionMap);

	int itemIndex = NumberUtils.stringToInt(request.getParameter(ScratchieConstants.PARAM_ITEM_INDEX), -1);
	if (itemIndex != -1) {
	    List<ScratchieItem> rList = new ArrayList<ScratchieItem>(itemList);

	    // get current and the target item, and switch their sequnece
	    ScratchieItem item = rList.get(itemIndex);
	    ScratchieItem repOption;
	    if (up) {
		repOption = rList.get(--itemIndex);
	    } else {
		repOption = rList.get(++itemIndex);
	    }

	    int upSeqId = repOption.getOrderId();
	    repOption.setOrderId(item.getOrderId());
	    item.setOrderId(upSeqId);

	    // put back list, it will be sorted again
	    itemList.clear();
	    itemList.addAll(rList);
	}

	request.setAttribute(ScratchieConstants.ATTR_ITEM_LIST, itemList);
	return mapping.findForward(ScratchieConstants.SUCCESS);
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
    private ActionForward addAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SortedSet<ScratchieAnswer> answerList = getAnswersFromRequest(request, false);

	ScratchieAnswer answer = new ScratchieAnswer();
	int maxSeq = 1;
	if (answerList != null && answerList.size() > 0) {
	    ScratchieAnswer last = answerList.last();
	    maxSeq = last.getOrderId() + 1;
	}
	answer.setOrderId(maxSeq);
	answerList.add(answer);

	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	return mapping.findForward(ScratchieConstants.SUCCESS);
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
    private ActionForward removeAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	SortedSet<ScratchieAnswer> answerList = getAnswersFromRequest(request, false);

	int answerIndex = NumberUtils.stringToInt(request.getParameter(ScratchieConstants.PARAM_ANSWER_INDEX), -1);
	if (answerIndex != -1) {
	    List<ScratchieAnswer> rList = new ArrayList<ScratchieAnswer>(answerList);
	    rList.remove(answerIndex);
	    answerList.clear();
	    answerList.addAll(rList);
	}

	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	return mapping.findForward(ScratchieConstants.SUCCESS);
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
    private ActionForward upAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchAnswer(mapping, request, true);
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
    private ActionForward downAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchAnswer(mapping, request, false);
    }

    private ActionForward switchAnswer(ActionMapping mapping, HttpServletRequest request, boolean up) {
	SortedSet<ScratchieAnswer> answerList = getAnswersFromRequest(request, false);

	int answerIndex = NumberUtils.stringToInt(request.getParameter(ScratchieConstants.PARAM_ANSWER_INDEX), -1);
	if (answerIndex != -1) {
	    List<ScratchieAnswer> rList = new ArrayList<ScratchieAnswer>(answerList);

	    // get current and the target item, and switch their sequnece
	    ScratchieAnswer answer = rList.get(answerIndex);
	    ScratchieAnswer repAnswer;
	    if (up) {
		repAnswer = rList.get(--answerIndex);
	    } else {
		repAnswer = rList.get(++answerIndex);
	    }

	    int upSeqId = repAnswer.getOrderId();
	    repAnswer.setOrderId(answer.getOrderId());
	    answer.setOrderId(upSeqId);

	    // put back list, it will be sorted again
	    answerList.clear();
	    answerList.addAll(rList);
	}

	request.setAttribute(ScratchieConstants.ATTR_ANSWER_LIST, answerList);
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    // ----------------------- PedagogicalPlannerForm ---------------

    public ActionForward initPedagogicalPlannerForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ScratchiePedagogicalPlannerForm plannerForm = (ScratchiePedagogicalPlannerForm) form;
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = getScratchieService().getScratchieByContentId(toolContentID);
	plannerForm.fillForm(scratchie);
	String contentFolderId = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	plannerForm.setContentFolderID(contentFolderId);
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    public ActionForward saveOrUpdatePedagogicalPlannerForm(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException {
	ScratchiePedagogicalPlannerForm plannerForm = (ScratchiePedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    Scratchie scratchie = getScratchieService().getScratchieByContentId(plannerForm.getToolContentID());
	    scratchie.setInstructions(plannerForm.getInstructions());
	    getScratchieService().saveOrUpdateScratchie(scratchie);
	} else {
	    saveErrors(request, errors);
	}
	return mapping.findForward(ScratchieConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return ScratchieService bean.
     */
    private IScratchieService getScratchieService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IScratchieService) wac.getBean(ScratchieConstants.SCRATCHIE_SERVICE);
    }

    /**
     * List save current scratchie items.
     * 
     * @param request
     * @return
     */
    private SortedSet<ScratchieItem> getItemList(SessionMap sessionMap) {
	SortedSet<ScratchieItem> list = (SortedSet<ScratchieItem>) sessionMap.get(ScratchieConstants.ATTR_ITEM_LIST);
	if (list == null) {
	    list = new TreeSet<ScratchieItem>(new ScratchieItemComparator());
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
    private List getDeletedItemList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, ScratchieConstants.ATTR_DELETED_ITEM_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     * 
     * @param request
     * @param name
     * @return
     */
    private List getListFromSession(SessionMap sessionMap, String name) {
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
    private TreeSet<ScratchieAnswer> getAnswersFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, ScratchieConstants.ATTR_ANSWER_LIST);
	Integer correctAnswerIndex = (paramMap.get(ScratchieConstants.ATTR_ANSWER_CORRECT) == null) ? null
		: NumberUtils.stringToInt(paramMap.get(ScratchieConstants.ATTR_ANSWER_CORRECT));

	int count = NumberUtils.stringToInt(paramMap.get(ScratchieConstants.ATTR_ANSWER_COUNT));
	TreeSet<ScratchieAnswer> answerList = new TreeSet<ScratchieAnswer>(new ScratchieAnswerComparator());
	for (int i = 0; i < count; i++) {

	    String answerDescription = paramMap.get(ScratchieConstants.ATTR_ANSWER_DESCRIPTION_PREFIX + i);
	    if ((answerDescription == null) && isForSaving) {
		continue;
	    }

	    ScratchieAnswer answer = new ScratchieAnswer();
	    String orderIdStr = paramMap.get(ScratchieConstants.ATTR_ANSWER_ORDER_ID_PREFIX + i);
	    Integer orderId = NumberUtils.stringToInt(orderIdStr);
	    answer.setOrderId(orderId);
	    answer.setDescription(answerDescription);
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
	Map<String, String> paramMap = new HashMap<String, String>();
	String[] pair;
	for (String item : params) {
	    pair = item.split("=");
	    if (pair == null || pair.length != 2)
		continue;
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
	Set<ScratchieAnswer> answers = item.getAnswers();
	if (answers != null) {
	    for (ScratchieAnswer answer : answers) {
		String answerDescription = answer.getDescription();
		if (answerDescription != null) {
		    answerDescription = answerDescription.replaceAll("[\n\r\f]", "");
		    answer.setDescription(answerDescription);
		}
	    }

	}
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

}
