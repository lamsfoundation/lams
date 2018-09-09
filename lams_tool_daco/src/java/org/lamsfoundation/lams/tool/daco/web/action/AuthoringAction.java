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

package org.lamsfoundation.lams.tool.daco.web.action;

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
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswerOption;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.DacoApplicationException;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.tool.daco.util.DacoQuestionComparator;
import org.lamsfoundation.lams.tool.daco.web.form.DacoForm;
import org.lamsfoundation.lams.tool.daco.web.form.DacoQuestionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Marcin Cieslak
 */
public class AuthoringAction extends Action {

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Daco Author function
	// ---------------------------
	if (param.equals("start")) {
	    ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	    request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    return start(mapping, form, request);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    IDacoService service = getDacoService();
	    Daco daco = service.getDacoByContentId(contentId);

	    daco.setDefineLater(true);
	    service.saveOrUpdateDaco(daco);
	    
	    //audit log the teacher has started editing activity in monitor
	    service.auditLogStartEditingActivityInMonitor(contentId);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	// ----------------------- Add daco question function
	// ---------------------------
	if (param.equals("newQuestion")) {
	    return newQuestion(mapping, form, request);
	}
	if (param.equals("editQuestion")) {
	    return editQuestion(mapping, form, request);
	}
	if (param.equals("saveOrUpdateQuestion")) {
	    return saveOrUpdateQuestion(mapping, form, request);
	}
	if (param.equals("removeQuestion")) {
	    return removeQuestion(mapping, form, request);
	}
	if (param.equals("newAnswerOption")) {
	    return newAnswerOption(mapping, form, request);
	}
	if (param.equals("removeAnswerOption")) {
	    return removeAnswerOption(mapping, form, request);
	}

	return mapping.findForward(DacoConstants.ERROR);
    }

    /**
     * Display edit page for existed daco question.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    protected ActionForward editQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	int questionIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_QUESTION_INDEX), -1);
	DacoQuestion question = null;
	DacoQuestionForm questionForm = (DacoQuestionForm) form;
	if (questionIndex != -1) {
	    SortedSet<DacoQuestion> questionSet = getQuestionList(sessionMap);
	    List<DacoQuestion> questionList = new ArrayList<DacoQuestion>(questionSet);
	    question = questionList.get(questionIndex);
	    if (question != null) {
		populateQuestionToForm(questionIndex, question, questionForm, request);
	    }
	}
	return findForward(question == null ? -1 : question.getType(), mapping);
    }

    /**
     * Extract web from content to daco question.
     * 
     * @param request
     * @param questionForm
     * @param answerOptionList
     * @throws DacoApplicationException
     */
    protected void extractFormToDacoQuestion(HttpServletRequest request, List<String> answerOptionList,
	    DacoQuestionForm questionForm) throws Exception {
	/*
	 * BE CAREFUL: This method will copy nessary info from request form to a old or new DacoQuestion instance. It
	 * gets all
	 * info EXCEPT DacoQuestion.createDate and DacoQuestion.createBy, which need be set when persisting this daco
	 * question.
	 */

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(questionForm.getSessionMapID());
	// check whether it is "edit(old question)" or "add(new question)"
	SortedSet<DacoQuestion> questionSet = getQuestionList(sessionMap);
	int questionIndex = NumberUtils.stringToInt(questionForm.getQuestionIndex(), -1);
	DacoQuestion question = null;
	if (questionIndex == -1) { // add
	    question = new DacoQuestion();
	    question.setCreateDate(new Timestamp(new Date().getTime()));
	    questionSet.add(question);

	} else { // edit
	    List<DacoQuestion> questionList = new ArrayList<DacoQuestion>(questionSet);
	    question = questionList.get(questionIndex);
	}

	question.setType(questionForm.getQuestionType());

	String constraint = questionForm.getMax();
	if (!StringUtils.isBlank(constraint)) {
	    question.setMax(Float.parseFloat(constraint));
	} else {
	    question.setMax(null);
	}
	constraint = questionForm.getMin();
	if (!StringUtils.isBlank(constraint)) {
	    question.setMin(Float.parseFloat(constraint));
	} else {
	    question.setMin(null);
	}
	constraint = questionForm.getDigitsDecimal();
	if (!StringUtils.isBlank(constraint)) {
	    question.setDigitsDecimal(Short.parseShort(constraint));
	}

	Set<DacoAnswerOption> answerOptions = new LinkedHashSet<DacoAnswerOption>();
	if (answerOptionList != null) {
	    int index = 1;
	    for (String ins : answerOptionList) {
		DacoAnswerOption answerOption = new DacoAnswerOption();
		answerOption.setAnswerOption(ins.trim());
		answerOption.setSequenceNumber(index++);
		answerOptions.add(answerOption);
	    }
	}
	question.setRequired(questionForm.isQuestionRequired());
	question.setAnswerOptions(answerOptions);

	Short summary = questionForm.getSummary();
	if (summary != null && summary > 0) {
	    question.setSummary(questionForm.getSummary());
	} else {
	    question.setSummary(null);
	}

	question.setDescription(
		StringUtils.isBlank(questionForm.getDescription()) ? null : questionForm.getDescription().trim());

    }

    /**
     * Get back relative <code>ActionForward</code> from request.
     * 
     * @param type
     * @param mapping
     * @return
     */
    protected ActionForward findForward(short type, ActionMapping mapping) {
	ActionForward forward;
	switch (type) {
	    case DacoConstants.QUESTION_TYPE_TEXTFIELD:
		forward = mapping.findForward("textfield");
		break;
	    case DacoConstants.QUESTION_TYPE_TEXTAREA:
		forward = mapping.findForward("textarea");
		break;
	    case DacoConstants.QUESTION_TYPE_NUMBER:
		forward = mapping.findForward("number");
		break;
	    case DacoConstants.QUESTION_TYPE_DATE:
		forward = mapping.findForward("date");
		break;
	    case DacoConstants.QUESTION_TYPE_FILE:
		forward = mapping.findForward("file");
		break;
	    case DacoConstants.QUESTION_TYPE_IMAGE:
		forward = mapping.findForward("image");
		break;
	    case DacoConstants.QUESTION_TYPE_RADIO:
		forward = mapping.findForward("radio");
		break;
	    case DacoConstants.QUESTION_TYPE_DROPDOWN:
		forward = mapping.findForward("dropdown");
		break;
	    case DacoConstants.QUESTION_TYPE_CHECKBOX:
		forward = mapping.findForward("checkbox");
		break;
	    case DacoConstants.QUESTION_TYPE_LONGLAT:
		forward = mapping.findForward("longlat");
		break;
	    default:
		forward = null;
		break;
	}
	return forward;
    }

    /**
     * Get answer options from <code>HttpRequest</code>
     * 
     * @param request
     */
    protected List<String> getAnswerOptionsFromRequest(HttpServletRequest request) {
	String list = request.getParameter(DacoConstants.ATTR_ANSWER_OPTION_LIST);
	if (list == null) {
	    return null;
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
		AuthoringAction.log.error("Error occurs when decode answer options string:" + e.toString());
	    }
	}

	int count = NumberUtils.stringToInt(paramMap.get(DacoConstants.ANSWER_OPTION_COUNT));
	List<String> answerOptionList = new ArrayList<String>();
	for (int index = 1; index <= count; index++) {
	    String item = paramMap.get(DacoConstants.ANSWER_OPTION_DESC_PREFIX + index);
	    if (item != null) {
		answerOptionList.add(item);
	    }
	}
	return answerOptionList;
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return DacoService bean.
     */
    protected IDacoService getDacoService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IDacoService) wac.getBean(DacoConstants.DACO_SERVICE);
    }

    /**
     * List save deleted daco questions, which could be persisted or non-persisted questions.
     * 
     * @param request
     * @return
     */
    protected List getDeletedDacoQuestionList(SessionMap<String, Object> sessionMap) {
	return getListFromSession(sessionMap, DacoConstants.ATTR_DELETED_QUESTION_LIST);
    }

    /**
     * Get <code>java.util.List</code> from HttpSession by given name.
     * 
     * @param request
     * @param name
     * @return
     */
    protected List getListFromSession(SessionMap<String, Object> sessionMap, String name) {
	List list = (List) sessionMap.get(name);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(name, list);
	}
	return list;
    }

    /**
     * Get longitude/latitude maps from <code>HttpRequest</code>
     * 
     * @param request
     */
    protected List<String> geSelectedMapsFromRequest(HttpServletRequest request) {
	String list = request.getParameter(DacoConstants.PARAM_LONGLAT_MAPS_SELECTED);
	if (list == null) {
	    return null;
	}
	String[] params = list.split("&");
	List<String> longlatMaps = new ArrayList<String>();
	for (String item : params) {
	    longlatMaps.add(item);
	}
	return longlatMaps;
    }

    /**
     * List save current daco questions.
     * 
     * @param request
     * @return
     */
    protected SortedSet<DacoQuestion> getQuestionList(SessionMap<String, Object> sessionMap) {

	SortedSet<DacoQuestion> list = (SortedSet<DacoQuestion>) sessionMap.get(DacoConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<DacoQuestion>(new DacoQuestionComparator());
	    sessionMap.put(DacoConstants.ATTR_QUESTION_LIST, list);
	}
	return list;
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
    protected ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	DacoForm existForm = (DacoForm) sessionMap.get(DacoConstants.ATTR_DACO_FORM);

	DacoForm dacoForm = (DacoForm) form;
	try {
	    PropertyUtils.copyProperties(dacoForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    /**
     * Ajax call, will add one more input line for new answer option.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    protected ActionForward newAnswerOption(ActionMapping mapping, ActionForm form, HttpServletRequest request) {
	int count = NumberUtils.stringToInt(request.getParameter(DacoConstants.ANSWER_OPTION_COUNT), 0);

	List answerOptionList = new ArrayList(++count);
	for (int index = 1; index <= count; index++) {
	    String answerOption = request.getParameter(DacoConstants.ANSWER_OPTION_DESC_PREFIX + index);
	    answerOptionList.add(answerOption == null ? "" : answerOption);
	}
	request.setAttribute(DacoConstants.ATTR_ANSWER_OPTION_LIST, answerOptionList);
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    /**
     * Display empty page for new daco question.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    protected ActionForward newQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	DacoQuestionForm questionForm = (DacoQuestionForm) form;
	questionForm.setSessionMapID(sessionMapID);

	short type = (short) NumberUtils.stringToInt(request.getParameter(DacoConstants.QUESTION_TYPE));
	if (type == DacoConstants.QUESTION_TYPE_RADIO || type == DacoConstants.QUESTION_TYPE_DROPDOWN
		|| type == DacoConstants.QUESTION_TYPE_CHECKBOX) {
	    List answerOptionList = new ArrayList(DacoConstants.INIT_ANSWER_OPTION_COUNT);
	    for (int index = 0; index < DacoConstants.INIT_ANSWER_OPTION_COUNT; index++) {
		answerOptionList.add("");
	    }
	    request.setAttribute(DacoConstants.ATTR_ANSWER_OPTION_LIST, answerOptionList);
	}
	return findForward(type, mapping);
    }

    /**
     * This method will populate daco question information to its form for edit use.
     * 
     * @param questionIndex
     * @param question
     * @param form
     * @param request
     */
    protected void populateQuestionToForm(int questionIndex, DacoQuestion question, DacoQuestionForm form,
	    HttpServletRequest request) {
	form.setDescription(question.getDescription());
	form.setQuestionRequired(question.isRequired());
	form.setSummary(question.getSummary());
	if (questionIndex >= 0) {
	    form.setQuestionIndex(new Integer(questionIndex).toString());
	}

	Float min = question.getMin();
	Float max = question.getMax();
	short questionType = question.getType();

	if (questionType == DacoConstants.QUESTION_TYPE_NUMBER) {
	    Short digitsDecimal = question.getDigitsDecimal();
	    if (digitsDecimal != null) {
		form.setDigitsDecimal(digitsDecimal.toString());
		if (digitsDecimal == 0) {
		    form.setMin(min == null ? null : String.valueOf(Math.round(min)));
		    form.setMax(max == null ? null : String.valueOf(Math.round(max)));
		} else {
		    form.setMin(min == null ? null : String.valueOf(round(min, digitsDecimal)));
		    form.setMax(max == null ? null : String.valueOf(round(max, digitsDecimal)));
		}
	    } else {
		form.setMin(min == null ? null : String.valueOf(min));
		form.setMax(max == null ? null : String.valueOf(max));
		form.setDigitsDecimal(null);
	    }
	} else {
	    form.setMin(min == null ? null : String.valueOf(min.intValue()));
	    form.setMax(max == null ? null : String.valueOf(max.intValue()));
	}

	if (questionType == DacoConstants.QUESTION_TYPE_RADIO || questionType == DacoConstants.QUESTION_TYPE_DROPDOWN
		|| questionType == DacoConstants.QUESTION_TYPE_CHECKBOX) {
	    Set<DacoAnswerOption> answerOptionList = question.getAnswerOptions();
	    List answerOptions = new ArrayList();
	    for (DacoAnswerOption in : answerOptionList) {
		answerOptions.add(in.getAnswerOption());
	    }
	    request.setAttribute(DacoConstants.ATTR_ANSWER_OPTION_LIST, answerOptions);
	} else if (questionType == DacoConstants.QUESTION_TYPE_LONGLAT) {

	    Set<DacoAnswerOption> answerOptionList = question.getAnswerOptions();
	    List selectedMaps = new ArrayList();
	    for (DacoAnswerOption in : answerOptionList) {
		selectedMaps.add(in.getAnswerOption());
	    }
	    request.setAttribute(DacoConstants.PARAM_LONGLAT_MAPS_SELECTED, selectedMaps);
	}
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
    protected ActionForward removeAnswerOption(ActionMapping mapping, ActionForm form, HttpServletRequest request) {
	int count = NumberUtils.stringToInt(request.getParameter(DacoConstants.ANSWER_OPTION_COUNT), 0);
	int removeIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_ANSWER_OPTION_INDEX), -1);
	List answerOptionList = new ArrayList(count - 1);
	for (int index = 1; index <= count; index++) {
	    if (index != removeIndex) {
		String answerOption = request.getParameter(DacoConstants.ANSWER_OPTION_DESC_PREFIX + index);
		answerOptionList.add(answerOption == null ? "" : answerOption);
	    }
	}
	request.setAttribute(DacoConstants.ATTR_ANSWER_OPTION_LIST, answerOptionList);
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    /**
     * Remove daco question from HttpSession list and update page display. As authoring rule, all persist only happen
     * when user
     * submit whole page. So this remove is just impact HttpSession values.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    protected ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request) {

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	int questionIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_QUESTION_INDEX), -1);
	if (questionIndex != -1) {
	    SortedSet<DacoQuestion> questionSet = getQuestionList(sessionMap);
	    List<DacoQuestion> questionList = new ArrayList<DacoQuestion>(questionSet);
	    DacoQuestion question = questionList.remove(questionIndex);
	    questionSet.clear();
	    questionSet.addAll(questionList);
	    // add to delList
	    List deletedList = getDeletedDacoQuestionList(sessionMap);
	    deletedList.add(question);
	}

	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    /**
     * This method will get necessary information from daco question form and save or update into
     * <code>HttpSession</code>
     * DacoQuestionList. Notice, this save is not persist them into database, just save <code>HttpSession</code>
     * temporarily.
     * Only they will be persist when the entire authoring page is being persisted.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    protected ActionForward saveOrUpdateQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request) {
	List<String> answerOptionList = getAnswerOptionsFromRequest(request);
	List<String> longlatMaps = geSelectedMapsFromRequest(request);
	DacoQuestionForm questionForm = (DacoQuestionForm) form;
	ActionErrors errors = validateDacoQuestionForm(questionForm, answerOptionList);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    ensureMinimumAnswerOptions(answerOptionList);
	    request.setAttribute(DacoConstants.ATTR_ANSWER_OPTION_LIST, answerOptionList);
	    request.setAttribute(DacoConstants.PARAM_LONGLAT_MAPS_SELECTED, longlatMaps);
	    return findForward(questionForm.getQuestionType(), mapping);
	}

	try {
	    List<String> listToSave = answerOptionList == null ? longlatMaps : answerOptionList;
	    extractFormToDacoQuestion(request, listToSave, questionForm);
	} catch (Exception e) {
	    e.printStackTrace();
	    // any upload exception will display as normal error message rather
	    // then throw exception directly
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(DacoConstants.ERROR_MSG_UPLOAD_FAILED, e.getMessage()));
	    if (!errors.isEmpty()) {
		this.addErrors(request, errors);
		request.setAttribute(DacoConstants.ATTR_ANSWER_OPTION_LIST, answerOptionList);
		request.setAttribute(DacoConstants.PARAM_LONGLAT_MAPS_SELECTED, longlatMaps);
		return findForward(questionForm.getQuestionType(), mapping);
	    }
	}
	// set session map ID so that questionlist.jsp can get sessionMAP
	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, questionForm.getSessionMapID());
	// return null to close this window
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    /**
     * Read daco data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run
     * successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
     * 
     * @throws ServletException
     * 
     */
    protected ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request)
	    throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	// get back the daco and question list and display them on page
	IDacoService service = getDacoService();

	List<DacoQuestion> questions = null;
	Daco daco = null;
	DacoForm dacoForm = (DacoForm) form;

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	dacoForm.setSessionMapID(sessionMap.getSessionID());

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	dacoForm.setContentFolderID(contentFolderID);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	try {
	    daco = service.getDacoByContentId(contentId);
	    // if daco does not exist, try to use default content instead.
	    if (daco == null) {
		daco = service.getDefaultContent(contentId);
		if (daco.getDacoQuestions() == null) {
		    questions = null;
		} else {

		    questions = new ArrayList<DacoQuestion>(daco.getDacoQuestions());
		}
	    } else {

		questions = new ArrayList<DacoQuestion>(daco.getDacoQuestions());
	    }

	    dacoForm.setDaco(daco);
	} catch (Exception e) {
	    AuthoringAction.log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (questions == null) {
	    questions = new ArrayList<DacoQuestion>();
	} else {
	    DacoUser dacoUser = null;
	    // handle system default question: createBy is null, now set it to
	    // current user
	    for (DacoQuestion question : questions) {
		if (question.getCreateBy() == null) {
		    if (dacoUser == null) {
			// get back login user DTO
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			dacoUser = new DacoUser(user, daco);
		    }
		    question.setCreateBy(dacoUser);
		}
	    }
	}

	// init daco question list
	SortedSet<DacoQuestion> dacoQuestionList = getQuestionList(sessionMap);
	dacoQuestionList.clear();
	dacoQuestionList.addAll(questions);

	sessionMap.put(DacoConstants.ATTR_DACO_FORM, dacoForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));

	return mapping.findForward(DacoConstants.SUCCESS);
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all daco question, information etc.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    protected ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	DacoForm dacoForm = (DacoForm) form;

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(dacoForm.getSessionMapID());

	ToolAccessMode toolAccessMode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, toolAccessMode.toString());

	ActionMessages errors = validateDacoForm(dacoForm, mapping, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    return mapping.findForward(DacoConstants.SUCCESS);
	}

	Daco daco = dacoForm.getDaco();
	IDacoService service = getDacoService();

	// **********************************Get Daco PO*********************
	Daco dacoPO = service.getDacoByContentId(dacoForm.getDaco().getContentId());
	if (dacoPO == null) {
	    // new Daco, create it.
	    dacoPO = daco;
	    dacoPO.setCreated(new Timestamp(new Date().getTime()));
	    dacoPO.setUpdated(new Timestamp(new Date().getTime()));
	    
	} else {
	    service.releaseDacoFromCache(dacoPO);
	    
	    Long uid = dacoPO.getUid();
	    PropertyUtils.copyProperties(dacoPO, daco);
	    // get back UID
	    dacoPO.setUid(uid);
	    
	    // if it's a teacher - change define later status
	    if (toolAccessMode.isTeacher()) {
		dacoPO.setDefineLater(false);
	    }
	    dacoPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	DacoUser dacoUser = service.getUserByUserIdAndContentId(new Long(user.getUserID().intValue()),
		dacoForm.getDaco().getContentId());
	if (dacoUser == null) {
	    dacoUser = new DacoUser(user, dacoPO);
	}

	dacoPO.setCreatedBy(dacoUser);

	// ************************* Handle daco questions *******************
	// Handle daco questions
	SortedSet<DacoQuestion> formQuestionSet = getQuestionList(sessionMap);
	Set<DacoQuestion> questionSet = new LinkedHashSet<DacoQuestion>(formQuestionSet.size());

	for (DacoQuestion question : formQuestionSet) {
	    // This flushs user UID info to message if this user is a new
	    // user.
	    question.setCreateBy(dacoUser);
	    question.setDaco(dacoPO);
	    questionSet.add(question);
	}

	dacoPO.setDacoQuestions(questionSet);
	service.saveOrUpdateDaco(dacoPO);

	// delete questions from database
	List<DacoQuestion> deletedQuestionList = getDeletedDacoQuestionList(sessionMap);
	Iterator<DacoQuestion> iter = deletedQuestionList.iterator();
	while (iter.hasNext()) {
	    DacoQuestion question = iter.next();
	    iter.remove();
	    if (question.getUid() != null) {
		service.deleteDacoQuestion(question.getUid());
	    }
	}
	dacoForm.setDaco(dacoPO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	return mapping.findForward(DacoConstants.SUCCESS);
    }

    protected ActionMessages validateDacoForm(DacoForm dacoForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	Short min = dacoForm.getDaco().getMinRecords();
	Short max = dacoForm.getDaco().getMaxRecords();
	if (min != null && max != null && min > 0 && max > 0 && min > max) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(DacoConstants.ERROR_MSG_RECORDLIMIT_MIN_TOOHIGH_MAX));
	}

	return errors;
    }

    /**
     * Vaidate daco question regards to their type
     * (textfield/textarea/number/date/file/image/radio/dropdown/checkbox/longlat)
     * 
     * @param questionForm
     * @return
     */
    protected ActionErrors validateDacoQuestionForm(DacoQuestionForm questionForm, List<String> answerOptionList) {
	ActionErrors errors = new ActionErrors();
	if (StringUtils.isBlank(questionForm.getDescription())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_DESC_BLANK));
	}

	String constraint = questionForm.getMax();
	Float max = null;
	if (!StringUtils.isBlank(constraint)) {
	    if (questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_TEXTFIELD
		    || questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_TEXTAREA
		    || questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_CHECKBOX) {
		try {
		    max = (float) Integer.parseInt(constraint);
		    if (max < 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage(DacoConstants.ERROR_MSG_MAX_NEGATIVE));
		    } else if (questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_CHECKBOX
			    && max > answerOptionList.size()) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage(DacoConstants.ERROR_MSG_MAX_TOOHIGH_ANSWEROPTION));
		    }
		} catch (NumberFormatException e) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(DacoConstants.ERROR_MSG_MAX_NUMBER_INT));
		}
	    } else if (questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_NUMBER) {
		try {
		    max = Float.parseFloat(constraint);

		} catch (NumberFormatException e) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(DacoConstants.ERROR_MSG_MAX_NUMBER_FLOAT));
		}
	    }
	}

	constraint = questionForm.getMin();
	if (!StringUtils.isBlank(constraint)) {
	    Float min = null;
	    if (questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_TEXTFIELD
		    || questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_TEXTAREA
		    || questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_CHECKBOX) {
		try {
		    min = (float) Integer.parseInt(constraint);
		    if (min < 0) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage(DacoConstants.ERROR_MSG_MIN_NEGATIVE));
		    } else if (questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_CHECKBOX
			    && min > answerOptionList.size()) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage(DacoConstants.ERROR_MSG_MIN_TOOHIGH_ANSWEROPTION));
		    }
		} catch (NumberFormatException e) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(DacoConstants.ERROR_MSG_MIN_NUMBER_INT));
		}
	    }
	    if (questionForm.getQuestionType() == DacoConstants.QUESTION_TYPE_NUMBER) {
		try {
		    min = Float.parseFloat(constraint);

		} catch (NumberFormatException e) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(DacoConstants.ERROR_MSG_MIN_NUMBER_FLOAT));
		}
	    }
	    if (min != null && max != null && min > max) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_MIN_TOOHIGH_MAX));
	    }
	}

	constraint = questionForm.getDigitsDecimal();
	if (!StringUtils.isBlank(constraint)) {
	    try {
		Short digitsDecimal = Short.parseShort(constraint);
		if (digitsDecimal < 0) {
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(DacoConstants.ERROR_MSG_DIGITSDECIMAL_NONNEGATIVE));
		}
	    } catch (NumberFormatException e) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(DacoConstants.ERROR_MSG_DIGITSDECIMAL_INT));
	    }
	}

	if (answerOptionList != null) {
	    if (answerOptionList.size() < DacoConstants.ANSWER_OPTION_MINIMUM_COUNT) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			DacoConstants.ERROR_MSG_ANSWEROPTION_NOTENOUGH, DacoConstants.ANSWER_OPTION_MINIMUM_COUNT));
	    }
	    String ordinal = getDacoService().getLocalisedMessage("label.authoring.basic.answeroption.ordinal", null);
	    for (int firstOptionNumber = 0; firstOptionNumber < answerOptionList.size(); firstOptionNumber++) {
		String firstOption = answerOptionList.get(firstOptionNumber);
		for (int secondOptionNumber = firstOptionNumber + 1; secondOptionNumber < answerOptionList
			.size(); secondOptionNumber++) {
		    String secondOption = answerOptionList.get(secondOptionNumber);
		    if (firstOption.trim().equals(secondOption.trim())) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,
				new ActionMessage(DacoConstants.ERROR_MSG_ANSWEROPTION_REPEAT,
					ordinal.charAt(firstOptionNumber) + ")",
					ordinal.charAt(secondOptionNumber) + ")"));
		    }
		}
	    }
	}

	return errors;
    }

    protected void ensureMinimumAnswerOptions(List<String> answerOptionList) {
	if (answerOptionList != null) {
	    while (answerOptionList.size() < DacoConstants.ANSWER_OPTION_MINIMUM_COUNT) {
		answerOptionList.add("");
	    }
	}
    }

    public final double round(double number, int positions) {
	double shift = Math.pow(10, positions);
	shift = Math.round(number * shift) / shift;
	return shift;
    }
}