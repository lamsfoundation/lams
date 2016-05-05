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
package org.lamsfoundation.lams.tool.survey.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.service.SurveyApplicationException;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.tool.survey.web.form.QuestionForm;
import org.lamsfoundation.lams.tool.survey.web.form.SurveyForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 * @version $Revision$
 */
public class AuthoringAction extends Action {
    private static final int INIT_INSTRUCTION_COUNT = 2;
    private static final String INSTRUCTION_ITEM_DESC_PREFIX = "instructionItemDesc";
    private static final String INSTRUCTION_ITEM_COUNT = "instructionCount";
    private static final int SHORT_TITLE_LENGTH = 60;

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Survey Author function ---------------------------
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
	    ISurveyService service = getSurveyService();
	    Survey survey = service.getSurveyByContentId(contentId);

	    boolean isEditable = SurveyWebUtils.isSurveyEditable(survey);
	    if (!isEditable) {
		request.setAttribute(SurveyConstants.PAGE_EDITABLE, new Boolean(isEditable));
		return mapping.findForward("forbidden");
	    }

	    if (!survey.isContentInUse()) {
		survey.setDefineLater(true);
		service.saveOrUpdateSurvey(survey);
	    }

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}

	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	// ----------------------- Add survey item function ---------------------------
	if (param.equals("newItemInit")) {
	    return newItemlInit(mapping, form, request, response);
	}
	if (param.equals("copyItemInit")) {
	    return copyItemlInit(mapping, form, request, response);
	}
	if (param.equals("editItemInit")) {
	    return editItemInit(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateItem")) {
	    return saveOrUpdateItem(mapping, form, request, response);
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
	// -----------------------Survey Item Instruction function ---------------------------
	if (param.equals("newInstruction")) {
	    return newInstruction(mapping, form, request, response);
	}
	if (param.equals("removeInstruction")) {
	    return removeInstruction(mapping, form, request, response);
	}
	return mapping.findForward(SurveyConstants.ERROR);
    }

    /**
     * Remove survey item from HttpSession list and update page display. As authoring rule, all persist only happen when
     * user submit whole page. So this remove is just impact HttpSession values.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
	    List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
	    SurveyQuestion item = rList.remove(itemIdx);
	    surveyList.clear();
	    surveyList.addAll(rList);
	    // add to delList
	    List delList = getDeletedSurveyItemList(sessionMap);
	    delList.add(item);

	    SortedSet<SurveyCondition> list = getSurveyConditionSet(sessionMap);
	    Iterator<SurveyCondition> conditionIter = list.iterator();

	    while (conditionIter.hasNext()) {
		SurveyCondition condition = conditionIter.next();
		Set<SurveyQuestion> questions = condition.getQuestions();
		if (questions.contains(item)) {
		    questions.remove(item);
		}
	    }
	}

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(SurveyConstants.SUCCESS);
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
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ITEM_INDEX), -1);
	if (itemIdx != -1) {
	    SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
	    List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
	    // get current and the target item, and switch their sequnece
	    SurveyQuestion item = rList.get(itemIdx);
	    SurveyQuestion repItem;
	    if (up) {
		repItem = rList.get(--itemIdx);
	    } else {
		repItem = rList.get(++itemIdx);
	    }
	    int upSeqId = repItem.getSequenceId();
	    repItem.setSequenceId(item.getSequenceId());
	    item.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    surveyList.clear();
	    surveyList.addAll(rList);
	}

	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Display edit page for existed survey item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editItemInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	QuestionForm itemForm = (QuestionForm) form;
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ITEM_INDEX), -1);
	SurveyQuestion item = null;
	if (itemIdx != -1) {
	    SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
	    List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
	    item = rList.get(itemIdx);
	    if (item != null) {
		populateItemToForm(itemIdx, item, itemForm, request);
	    }
	}
	if (itemForm.getItemType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    return mapping.findForward(SurveyConstants.FORWARD_OPEN_QUESTION);
	} else {
	    return mapping.findForward(SurveyConstants.FORWARD_CHOICE_QUESTION);
	}
    }

    /**
     * Display empty page for new survey item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newItemlInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	QuestionForm questionForm = (QuestionForm) form;

	List instructionList = new ArrayList(AuthoringAction.INIT_INSTRUCTION_COUNT);
	for (int idx = 0; idx < AuthoringAction.INIT_INSTRUCTION_COUNT; idx++) {
	    instructionList.add("");
	}
	request.setAttribute("instructionList", instructionList);
	if (questionForm.getItemType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    return mapping.findForward(SurveyConstants.FORWARD_OPEN_QUESTION);
	} else {
	    return mapping.findForward(SurveyConstants.FORWARD_CHOICE_QUESTION);
	}
    }

    /**
     * Create a new question based on existing one.
     */
    private ActionForward copyItemlInit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	QuestionForm itemForm = (QuestionForm) form;
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int itemIdx = NumberUtils.stringToInt(request.getParameter(SurveyConstants.PARAM_ITEM_INDEX), -1);
	SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
	List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
	SurveyQuestion item = rList.get(itemIdx);
	if (item != null) {
	    populateItemToForm(-1, item, itemForm, request);
	    itemForm.setItemIndex(null);
	}

	if (itemForm.getItemType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    return mapping.findForward(SurveyConstants.FORWARD_OPEN_QUESTION);
	} else {
	    return mapping.findForward(SurveyConstants.FORWARD_CHOICE_QUESTION);
	}
    }

    /**
     * This method will get necessary information from survey item form and save or update into <code>HttpSession</code>
     * SurveyItemList. Notice, this save is not persist them into database, just save <code>HttpSession</code>
     * temporarily. Only they will be persist when the entire authoring page is being persisted.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward saveOrUpdateItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// get instructions:
	List<String> instructionList = getInstructionsFromRequest(request);

	QuestionForm itemForm = (QuestionForm) form;
	ActionErrors errors = validateSurveyItem(itemForm, instructionList);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    // add at least 2 instruction list
	    for (int idx = instructionList.size(); idx < 2; idx++) {
		instructionList.add("");
	    }
	    request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST, instructionList);
	    if (itemForm.getItemType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
		return mapping.findForward(SurveyConstants.FORWARD_OPEN_QUESTION);
	    } else {
		return mapping.findForward(SurveyConstants.FORWARD_CHOICE_QUESTION);
	    }
	}

	try {
	    extractFormToSurveyItem(request, instructionList, itemForm);
	} catch (Exception e) {
	    AuthoringAction.log.error("Uploading failed. The exception is " + e.toString());
	    throw e;
	}
	// set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, itemForm.getSessionMapID());
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, itemForm.getContentFolderID());
	// return null to close this window
	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Read survey data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     * @throws ServletException
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	// get back the survey and item list and display them on page
	ISurveyService service = getSurveyService();

	List<SurveyQuestion> questions = null;
	Survey survey = null;
	SurveyForm surveyForm = (SurveyForm) form;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	surveyForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	surveyForm.setSessionMapID(sessionMap.getSessionID());

	survey = service.getSurveyByContentId(contentId);
	// if survey does not exist, try to use default content instead.
	if (survey == null) {
	    survey = service.getDefaultContent(contentId);
	    if (survey.getQuestions() != null) {
		questions = new ArrayList<SurveyQuestion>(survey.getQuestions());
	    } else {
		questions = null;
	    }
	} else {
	    questions = new ArrayList<SurveyQuestion>(survey.getQuestions());
	}

	surveyForm.setSurvey(survey);

	// init it to avoid null exception in following handling
	if (questions == null) {
	    questions = new ArrayList();
	} else {
	    SurveyUser surveyUser = null;
	    // handle system default question: createBy is null, now set it to current user
	    for (SurveyQuestion question : questions) {
		if (question.getCreateBy() == null) {
		    if (surveyUser == null) {
			// get back login user DTO
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			surveyUser = new SurveyUser(user, survey);
		    }
		    question.setCreateBy(surveyUser);
		}
	    }
	}
	// init survey item list
	SortedSet<SurveyQuestion> surveyItemList = getSurveyItemList(sessionMap);
	surveyItemList.clear();
	retriveQuestionListForDisplay(questions);
	surveyItemList.addAll(questions);

	// init condition set
	SortedSet<SurveyCondition> conditionSet = getSurveyConditionSet(sessionMap);
	conditionSet.clear();
	conditionSet.addAll(survey.getConditions());

	sessionMap.put(SurveyConstants.ATTR_SURVEY_FORM, surveyForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return mapping.findForward(SurveyConstants.SUCCESS);
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
	String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	SurveyForm existForm = (SurveyForm) sessionMap.get(SurveyConstants.ATTR_SURVEY_FORM);

	SurveyForm surveyForm = (SurveyForm) form;
	try {
	    PropertyUtils.copyProperties(surveyForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor()) {
	    return mapping.findForward(SurveyConstants.SUCCESS);
	} else {
	    return mapping.findForward(SurveyConstants.DEFINE_LATER);
	}
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all survey item, information etc.
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
	SurveyForm surveyForm = (SurveyForm) form;

	// get back sessionMAP
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(surveyForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);

	ActionMessages errors = validate(surveyForm, mapping, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    if (mode.isAuthor()) {
		return mapping.findForward("author");
	    } else {
		return mapping.findForward("monitor");
	    }
	}

	Survey survey = surveyForm.getSurvey();
	ISurveyService service = getSurveyService();

	// **********************************Get Survey PO*********************
	Survey surveyPO = service.getSurveyByContentId(surveyForm.getSurvey().getContentId());
	if (surveyPO == null) {
	    // new Survey, create it.
	    surveyPO = survey;
	    surveyPO.setCreated(new Timestamp(new Date().getTime()));
	    surveyPO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    if (mode.isAuthor()) {
		Long uid = surveyPO.getUid();
		PropertyUtils.copyProperties(surveyPO, survey);
		// get back UID
		surveyPO.setUid(uid);
	    } else { // if it is Teacher, then just update basic tab content (definelater)
		surveyPO.setInstructions(survey.getInstructions());
		surveyPO.setTitle(survey.getTitle());
		// change define later status
		surveyPO.setDefineLater(false);
	    }
	    surveyPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	SurveyUser surveyUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		surveyForm.getSurvey().getContentId());
	if (surveyUser == null) {
	    surveyUser = new SurveyUser(user, surveyPO);
	}

	surveyPO.setCreatedBy(surveyUser);

	// ************************* Handle survey questions *******************
	Set questionList = new LinkedHashSet();
	SortedSet topics = getSurveyItemList(sessionMap);
	Iterator iter = topics.iterator();
	while (iter.hasNext()) {
	    SurveyQuestion item = (SurveyQuestion) iter.next();
	    if (item != null) {
		// This flushs user UID info to message if this user is a new user.
		item.setCreateBy(surveyUser);
		questionList.add(item);
	    }
	}
	surveyPO.setQuestions(questionList);
	// delete instruction file from database.
	List delSurveyItemList = getDeletedSurveyItemList(sessionMap);
	iter = delSurveyItemList.iterator();
	while (iter.hasNext()) {
	    SurveyQuestion item = (SurveyQuestion) iter.next();
	    iter.remove();
	    if (item.getUid() != null) {
		service.deleteQuestion(item.getUid());
	    }
	}

	// ******************************** Handle conditions ****************
	Set<SurveyCondition> conditions = getSurveyConditionSet(sessionMap);
	List delConditions = getDeletedSurveyConditionList(sessionMap);

	// delete conditions that don't contain any questions
	Iterator<SurveyCondition> conditionIter = conditions.iterator();
	while (conditionIter.hasNext()) {
	    SurveyCondition condition = conditionIter.next();
	    if (condition.getQuestions().isEmpty()) {
		conditionIter.remove();
		delConditions.add(condition);

		//reorder remaining conditions
		for (SurveyCondition otherCondition : conditions) {
		    if (otherCondition.getOrderId() > condition.getOrderId()) {
			otherCondition.setOrderId(otherCondition.getOrderId() - 1);
		    }
		}
	    }
	}
	surveyPO.setConditions(conditions);

	//permanently remove conditions from DB
	iter = delConditions.iterator();
	while (iter.hasNext()) {
	    SurveyCondition condition = (SurveyCondition) iter.next();
	    iter.remove();
	    service.deleteCondition(condition);
	}

	// finally persist surveyPO again
	service.saveOrUpdateSurvey(surveyPO);

	surveyForm.setSurvey(surveyPO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor()) {
	    return mapping.findForward("author");
	} else {
	    return mapping.findForward("monitor");
	}
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return SurveyService bean.
     */
    private ISurveyService getSurveyService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
    }

    /**
     * List save current survey items.
     *
     * @param request
     * @return
     */
    private SortedSet<SurveyQuestion> getSurveyItemList(SessionMap sessionMap) {
	SortedSet<SurveyQuestion> list = (SortedSet<SurveyQuestion>) sessionMap.get(SurveyConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<SurveyQuestion>(new QuestionsComparator());
	    sessionMap.put(SurveyConstants.ATTR_QUESTION_LIST, list);
	}
	return list;
    }

    /**
     * Set of conditions.
     *
     * @param request
     * @return
     */
    private SortedSet<SurveyCondition> getSurveyConditionSet(SessionMap sessionMap) {
	SortedSet<SurveyCondition> set = (SortedSet<SurveyCondition>) sessionMap
		.get(SurveyConstants.ATTR_CONDITION_SET);
	if (set == null) {
	    set = new TreeSet<SurveyCondition>(new TextSearchConditionComparator());
	    sessionMap.put(SurveyConstants.ATTR_CONDITION_SET, set);
	}
	return set;
    }

    /**
     * List save deleted survey items, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedSurveyItemList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, SurveyConstants.ATTR_DELETED_QUESTION_LIST);
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
     * Get survey items instruction from <code>HttpRequest</code>
     *
     * @param request
     */
    private List<String> getInstructionsFromRequest(HttpServletRequest request) {
	String list = request.getParameter("instructionList");
	List<String> instructionList = new ArrayList<String>();
	// for open text entry question
	if (list == null) {
	    return instructionList;
	}

	String[] params = list.split("&");
	Map<String, String> paramMap = new HashMap<String, String>();
	String[] pair;
	for (String item : params) {
	    pair = item.split("=");
	    if (pair == null || pair.length != 2) {
		continue;
	    }
	    try {
		paramMap.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		AuthoringAction.log.error("Error occurs when decode instruction string:" + e.toString());
	    }
	}

	int count = NumberUtils.stringToInt(paramMap.get(AuthoringAction.INSTRUCTION_ITEM_COUNT));
	for (int idx = 0; idx < count; idx++) {
	    String item = paramMap.get(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX + idx);
	    if (item == null) {
		continue;
	    }
	    instructionList.add(item);
	}
	return instructionList;
    }

    /**
     * This method will populate survey item information to its form for edit use.
     *
     * @param itemIdx
     * @param item
     * @param form
     * @param request
     */
    private void populateItemToForm(int itemIdx, SurveyQuestion item, QuestionForm form, HttpServletRequest request) {
	if (itemIdx >= 0) {
	    form.setItemIndex(new Integer(itemIdx).toString());
	}

	// set questions
	form.setQuestion(item);

	// set options
	Set<SurveyOption> instructionList = item.getOptions();
	List instructions = new ArrayList();
	for (SurveyOption in : instructionList) {
	    instructions.add(in.getDescription());
	}

	request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST, instructions);

    }

    /**
     * Extract web from content to survey item.
     *
     * @param request
     * @param instructionList
     * @param itemForm
     * @throws SurveyApplicationException
     */
    private void extractFormToSurveyItem(HttpServletRequest request, List<String> instructionList,
	    QuestionForm itemForm) throws Exception {
	/*
	 * BE CAREFUL: This method will copy nessary info from request form to a old or new SurveyItem instance. It gets
	 * all info EXCEPT SurveyItem.createDate and SurveyItem.createBy, which need be set when persisting this survey
	 * item.
	 */

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(itemForm.getSessionMapID());
	// check whether it is "edit(old item)" or "add(new item)"
	SortedSet<SurveyQuestion> surveyList = getSurveyItemList(sessionMap);
	int itemIdx = NumberUtils.stringToInt(itemForm.getItemIndex(), -1);
	SurveyQuestion item = itemForm.getQuestion();

	if (itemIdx == -1) { // add
	    item.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (surveyList != null && surveyList.size() > 0) {
		SurveyQuestion last = surveyList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    item.setSequenceId(maxSeq);
	    surveyList.add(item);
	} else { // edit
	    List<SurveyQuestion> rList = new ArrayList<SurveyQuestion>(surveyList);
	    item = rList.get(itemIdx);
	    item.setDescription(itemForm.getQuestion().getDescription());
	    item.setOptional(itemForm.getQuestion().isOptional());
	    item.setAppendText(itemForm.getQuestion().isAppendText());
	    item.setAllowMultipleAnswer(itemForm.getQuestion().isAllowMultipleAnswer());

	}
	AuthoringAction.retriveQuestionForDisplay(item);
	short type = getQuestionType(itemForm);
	item.setType(type);

	// set instrcutions
	Set instructions = new LinkedHashSet();
	int idx = 0;
	for (String ins : instructionList) {
	    SurveyOption rii = new SurveyOption();
	    rii.setDescription(ins);
	    rii.setSequenceId(idx++);
	    instructions.add(rii);
	}
	item.setOptions(instructions);

    }

    private short getQuestionType(QuestionForm itemForm) {
	// set question type
	short type;
	if (itemForm.getItemType() == SurveyConstants.SURVEY_TYPE_TEXT_ENTRY) {
	    type = SurveyConstants.SURVEY_TYPE_TEXT_ENTRY;
	} else if (itemForm.getQuestion().isAllowMultipleAnswer()) {
	    type = SurveyConstants.SURVEY_TYPE_MULTIPLE_CHOICES;
	} else {
	    type = SurveyConstants.SURVEY_TYPE_SINGLE_CHOICE;
	}
	return type;
    }

    private void retriveQuestionListForDisplay(List<SurveyQuestion> list) {
	for (SurveyQuestion item : list) {
	    AuthoringAction.retriveQuestionForDisplay(item);
	}
    }

    public static void retriveQuestionForDisplay(SurveyQuestion item) {
	String desc = item.getDescription();
	desc = desc.replaceAll("<(.|\n)*?>", "");
	item.setShortTitle(StringUtils.abbreviate(desc, AuthoringAction.SHORT_TITLE_LENGTH));
    }

    /**
     * Vaidate survey item regards to their type (url/file/learning object/website zip file)
     *
     * @param itemForm
     * @param instructionList
     * @return
     */
    private ActionErrors validateSurveyItem(QuestionForm itemForm, List<String> instructionList) {
	ActionErrors errors = new ActionErrors();
	if (StringUtils.isBlank(itemForm.getQuestion().getDescription())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(SurveyConstants.ERROR_MSG_DESC_BLANK));
	}

	short type = getQuestionType(itemForm);
	if (type != SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    if (instructionList == null || instructionList.size() < 2) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(SurveyConstants.ERROR_MSG_LESS_OPTIONS));
	    }
	}

	return errors;
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

    private ActionMessages validate(SurveyForm surveyForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	// if (StringUtils.isBlank(surveyForm.getSurvey().getTitle())) {
	// ActionMessage error = new ActionMessage("error.title.empty");
	// errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	// }

	// define it later mode(TEACHER) skip below validation.
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return errors;
	}

	// Some other validation outside basic Tab.

	return errors;
    }

    /**
     * Ajax call, will add one more input line for new survey item instruction.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	int count = NumberUtils.stringToInt(request.getParameter(AuthoringAction.INSTRUCTION_ITEM_COUNT), 0);
	List instructionList = new ArrayList(++count);
	for (int idx = 0; idx < count; idx++) {
	    String item = request.getParameter(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX + idx);
	    if (item == null) {
		instructionList.add("");
	    } else {
		instructionList.add(item);
	    }
	}
	request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST, instructionList);
	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Ajax call, remove the given line of instruction of survey item.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward removeInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	int count = NumberUtils.stringToInt(request.getParameter(AuthoringAction.INSTRUCTION_ITEM_COUNT), 0);
	int removeIdx = NumberUtils.stringToInt(request.getParameter("removeIdx"), -1);
	List instructionList = new ArrayList(count - 1);
	for (int idx = 0; idx < count; idx++) {
	    String item = request.getParameter(AuthoringAction.INSTRUCTION_ITEM_DESC_PREFIX + idx);
	    if (idx == removeIdx) {
		continue;
	    }
	    if (item == null) {
		instructionList.add("");
	    } else {
		instructionList.add(item);
	    }
	}
	request.setAttribute(SurveyConstants.ATTR_INSTRUCTION_LIST, instructionList);
	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted items.
     *
     * @param request
     * @return
     */
    private List getDeletedSurveyConditionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, SurveyConstants.ATTR_DELETED_CONDITION_LIST);
    }
}
