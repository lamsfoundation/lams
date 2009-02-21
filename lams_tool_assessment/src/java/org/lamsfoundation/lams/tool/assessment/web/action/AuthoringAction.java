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
package org.lamsfoundation.lams.tool.assessment.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentAnswerOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentAttachment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOverallFeedback;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUnit;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.service.UploadAssessmentFileException;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentAnswerOptionComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentOverallFeedbackComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentQuestionComparator;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentUnitComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentForm;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentQuestionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.NumberUtil;
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

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Assessment Author functions ---------------------------
	if (param.equals("start")) {
	    ToolAccessMode mode = getAccessMode(request);
	    // teacher mode "check for new" button enter.
	    if (mode != null)
		request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    else
		request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    IAssessmentService service = getAssessmentService();
	    Assessment assessment = service.getAssessmentByContentId(contentId);

	    assessment.setDefineLater(true);
	    service.saveOrUpdateAssessment(assessment);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}
	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}
	if (param.equals("uploadOnlineFile")) {
	    return uploadOnline(mapping, form, request, response);
	}
	if (param.equals("uploadOfflineFile")) {
	    return uploadOffline(mapping, form, request, response);
	}
	if (param.equals("deleteOnlineFile")) {
	    return deleteOnlineFile(mapping, form, request, response);
	}
	if (param.equals("deleteOfflineFile")) {
	    return deleteOfflineFile(mapping, form, request, response);
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
	if (param.equals("removeQuestion")) {
	    return removeQuestion(mapping, form, request, response);
	}
	if (param.equals("upQuestion")) {
	    return upQuestion(mapping, form, request, response);
	}
	if (param.equals("downQuestion")) {
	    return downQuestion(mapping, form, request, response);
	}	
	// -----------------------Assessment Answer Option functions ---------------------------
	if (param.equals("newOption")) {
	    return newOption(mapping, form, request, response);
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
	SessionMap sessionMap = new SessionMap();
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
		if (assessment.getQuestions() != null) {
		    questions = new ArrayList<AssessmentQuestion>(assessment.getQuestions());
		} else
		    questions = null;
	    } else
		questions = service.getAuthoredQuestions(assessment.getUid());

	    assessmentForm.setAssessment(assessment);

	    // initialize instruction attachment list
	    List attachmentList = getAttachmentList(sessionMap);
	    attachmentList.clear();
	    attachmentList.addAll(assessment.getAttachments());
	} catch (Exception e) {
	    log.error(e);
	    throw new ServletException(e);
	}

	// init it to avoid null exception in following handling
	if (questions == null)
	    questions = new ArrayList<AssessmentQuestion>();
	else {
	    AssessmentUser assessmentUser = null;
	    // handle system default question: createBy is null, now set it to current user
	    for (AssessmentQuestion question : questions) {
		if (question.getCreateBy() == null) {
		    if (assessmentUser == null) {
			// get back login user DTO
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			assessmentUser = new AssessmentUser(user, assessment);
		    }
		    question.setCreateBy(assessmentUser);
		}
	    }
	}
	// init assessment question list
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	questionList.clear();
	questionList.addAll(questions);
	
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	AssessmentForm existForm = (AssessmentForm) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT_FORM);
	
	AssessmentForm assessmentForm = (AssessmentForm) form;
	try {
	    PropertyUtils.copyProperties(assessmentForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor())
	    return mapping.findForward(AssessmentConstants.SUCCESS);
	else
	    return mapping.findForward(AssessmentConstants.DEFINE_LATER);
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all assessment question, information etc.
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(assessmentForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);

	Assessment assessment = assessmentForm.getAssessment();
	IAssessmentService service = getAssessmentService();

	// **********************************Get Assessment PO*********************
	Assessment assessmentPO = service.getAssessmentByContentId(assessmentForm.getAssessment().getContentId());
	if (assessmentPO == null) {
	    // new Assessment, create it.
	    assessmentPO = assessment;
	    assessmentPO.setCreated(new Timestamp(new Date().getTime()));
	    assessmentPO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    if (mode.isAuthor()) {
		Long uid = assessmentPO.getUid();
		PropertyUtils.copyProperties(assessmentPO, assessment);
		// get back UID
		assessmentPO.setUid(uid);
	    } else { // if it is Teacher, then just update basic tab content (definelater)
		assessmentPO.setInstructions(assessment.getInstructions());
		assessmentPO.setTitle(assessment.getTitle());
		// change define later status
		assessmentPO.setDefineLater(false);
	    }
	    assessmentPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	AssessmentUser assessmentUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		assessmentForm.getAssessment().getContentId());
	if (assessmentUser == null) {
	    assessmentUser = new AssessmentUser(user, assessmentPO);
	}

	assessmentPO.setCreatedBy(assessmentUser);

	// **********************************Handle Authoring Instruction Attachement *********************
	// merge attachment info
	// so far, attPOSet will be empty if content is existed. because PropertyUtils.copyProperties() is executed
	Set attPOSet = assessmentPO.getAttachments();
	if (attPOSet == null)
	    attPOSet = new HashSet();
	List attachmentList = getAttachmentList(sessionMap);
	List deleteAttachmentList = getDeletedAttachmentList(sessionMap);

	// current attachemnt in authoring instruction tab.
	Iterator iter = attachmentList.iterator();
	while (iter.hasNext()) {
	    AssessmentAttachment newAtt = (AssessmentAttachment) iter.next();
	    attPOSet.add(newAtt);
	}
	attachmentList.clear();

	// deleted attachment. 2 possible types: one is persist another is non-persist before.
	iter = deleteAttachmentList.iterator();
	while (iter.hasNext()) {
	    AssessmentAttachment delAtt = (AssessmentAttachment) iter.next();
	    iter.remove();
	    // it is an existed att, then delete it from current attachmentPO
	    if (delAtt.getUid() != null) {
		Iterator attIter = attPOSet.iterator();
		while (attIter.hasNext()) {
		    AssessmentAttachment att = (AssessmentAttachment) attIter.next();
		    if (delAtt.getUid().equals(att.getUid())) {
			attIter.remove();
			break;
		    }
		}
		service.deleteAssessmentAttachment(delAtt.getUid());
	    }// end remove from persist value
	}

	// copy back
	assessmentPO.setAttachments(attPOSet);
	// ************************* Handle assessment questions *******************
	// Handle assessment questions
	Set questionList = new LinkedHashSet();
	SortedSet topics = getQuestionList(sessionMap);
	iter = topics.iterator();
	while (iter.hasNext()) {
	    AssessmentQuestion question = (AssessmentQuestion) iter.next();
	    if (question != null) {
		// This flushs user UID info to message if this user is a new user.
		question.setCreateBy(assessmentUser);
		questionList.add(question);
	    }
	}
	assessmentPO.setQuestions(questionList);
	// delete instructino file from database.
	List deletedQuestionList = getDeletedQuestionList(sessionMap);
	iter = deletedQuestionList.iterator();
	while (iter.hasNext()) {
	    AssessmentQuestion question = (AssessmentQuestion) iter.next();
	    iter.remove();
	    if (question.getUid() != null)
		service.deleteAssessmentQuestion(question.getUid());
	}
	// handle assessment question attachment file:
	List delQuestionAttList = getDeletedQuestionAttachmentList(sessionMap);
	iter = delQuestionAttList.iterator();
	while (iter.hasNext()) {
	    AssessmentQuestion delAtt = (AssessmentQuestion) iter.next();
	    iter.remove();
	}
	
	// ************************* Handle assessment overall feedbacks *******************
	TreeSet<AssessmentOverallFeedback> overallFeedbackList = getOverallFeedbacksFromForm(request, true);
	assessmentPO.setOverallFeedbacks(overallFeedbackList);

	// **********************************************
	// finally persist assessmentPO again
	service.saveOrUpdateAssessment(assessmentPO);

	// initialize attachmentList again
	attachmentList = getAttachmentList(sessionMap);
	attachmentList.addAll(assessment.getAttachments());
	assessmentForm.setAssessment(assessmentPO);

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor()) {
	    return mapping.findForward("author");
	}
	else {
	    return mapping.findForward("monitor");
	}
    }

    /**
     * Handle upload online instruction files request.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws UploadAssessmentFileException
     */
    public ActionForward uploadOnline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UploadAssessmentFileException {
	return uploadFile(mapping, form, IToolContentHandler.TYPE_ONLINE, request);
    }

    /**
     * Handle upload offline instruction files request.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws UploadAssessmentFileException
     */
    public ActionForward uploadOffline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws UploadAssessmentFileException {
	return uploadFile(mapping, form, IToolContentHandler.TYPE_OFFLINE, request);
    }

    /**
     * Common method to upload online or offline instruction files request.
     * 
     * @param mapping
     * @param form
     * @param type
     * @param request
     * @return
     * @throws UploadAssessmentFileException
     */
    private ActionForward uploadFile(ActionMapping mapping, ActionForm form, String type, HttpServletRequest request)
	    throws UploadAssessmentFileException {

	AssessmentForm assessmentForm = (AssessmentForm) form;
	// get back sessionMAP
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(assessmentForm.getSessionMapID());

	FormFile file;
	if (StringUtils.equals(IToolContentHandler.TYPE_OFFLINE, type))
	    file = (FormFile) assessmentForm.getOfflineFile();
	else
	    file = (FormFile) assessmentForm.getOnlineFile();

	if (file == null || StringUtils.isBlank(file.getFileName()))
	    return mapping.findForward(AssessmentConstants.SUCCESS);

	// validate file size
	ActionMessages errors = new ActionMessages();
	FileValidatorUtil.validateFileSize(file, true, errors);
	if (!errors.isEmpty()) {
	    this.saveErrors(request, errors);
	    return mapping.findForward(AssessmentConstants.SUCCESS);
	}

	IAssessmentService service = getAssessmentService();
	// upload to repository
	AssessmentAttachment att = service.uploadInstructionFile(file, type);
	// handle session value
	List attachmentList = getAttachmentList(sessionMap);
	List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
	// first check exist attachment and delete old one (if exist) to deletedAttachmentList
	Iterator iter = attachmentList.iterator();
	AssessmentAttachment existAtt;
	while (iter.hasNext()) {
	    existAtt = (AssessmentAttachment) iter.next();
	    if (StringUtils.equals(existAtt.getFileName(), att.getFileName())
		    && StringUtils.equals(existAtt.getFileType(), att.getFileType())) {
		// if there is same name attachment, delete old one
		deleteAttachmentList.add(existAtt);
		iter.remove();
		break;
	    }
	}
	// add to attachmentList
	attachmentList.add(att);

	return mapping.findForward(AssessmentConstants.SUCCESS);

    }

    /**
     * Delete offline instruction file from current Assessment authoring page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward deleteOfflineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, request, response, form, IToolContentHandler.TYPE_OFFLINE);
    }

    /**
     * Delete online instruction file from current Assessment authoring page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward deleteOnlineFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return deleteFile(mapping, request, response, form, IToolContentHandler.TYPE_ONLINE);
    }

    /**
     * General method to delete file (online or offline)
     * 
     * @param mapping
     * @param request
     * @param response
     * @param form
     * @param type
     * @return
     */
    private ActionForward deleteFile(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
	    ActionForm form, String type) {
	Long versionID = new Long(WebUtil.readLongParam(request, AssessmentConstants.PARAM_FILE_VERSION_ID));
	Long uuID = new Long(WebUtil.readLongParam(request, AssessmentConstants.PARAM_FILE_UUID));

	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	// handle session value
	List attachmentList = getAttachmentList(sessionMap);
	List deleteAttachmentList = getDeletedAttachmentList(sessionMap);
	// first check exist attachment and delete old one (if exist) to deletedAttachmentList
	Iterator iter = attachmentList.iterator();
	AssessmentAttachment existAtt;
	while (iter.hasNext()) {
	    existAtt = (AssessmentAttachment) iter.next();
	    if (existAtt.getFileUuid().equals(uuID) && existAtt.getFileVersionId().equals(versionID)) {
		// if there is same name attachment, delete old one
		deleteAttachmentList.add(existAtt);
		iter.remove();
	    }
	}

	request.setAttribute(AssessmentConstants.ATTR_FILE_TYPE_FLAG, type);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	AssessmentQuestionForm questionForm = (AssessmentQuestionForm) form; 
	questionForm.setSessionMapID(sessionMapID);
	questionForm.setContentFolderID(contentFolderID);
	questionForm.setDefaultGrade("1");
	questionForm.setPenaltyFactor("0.1");
	
	List<AssessmentAnswerOption> optionList = new ArrayList<AssessmentAnswerOption>();
	for (int i = 0; i < AssessmentConstants.INITIAL_OPTIONS_NUMBER; i++) {
	    AssessmentAnswerOption option = new AssessmentAnswerOption();
	    option.setSequenceId(i+1);
	    option.setGrade(0);
	    optionList.add(option);
	}
	request.setAttribute(AssessmentConstants.ATTR_OPTION_LIST, optionList);
	
	List<AssessmentUnit> unitList = new ArrayList<AssessmentUnit>();
	for (int i = 0; i < AssessmentConstants.INITIAL_UNITS_NUMBER; i++) {
	    AssessmentUnit unit = new AssessmentUnit();
	    unit.setSequenceId(i+1);
	    unit.setMultiplier(0);
	    unitList.add(unit);
	}
	request.setAttribute(AssessmentConstants.ATTR_UNIT_LIST, unitList);	

	short type = (short) NumberUtils.stringToInt(request.getParameter(AssessmentConstants.ATTR_QUESTION_TYPE));
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	String contentFolderID = (String) sessionMap.get(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	int questionIdx = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_INDEX), -1);
	AssessmentQuestion question = null;
	if (questionIdx != -1) {
	    SortedSet<AssessmentQuestion> assessmentList = getQuestionList(sessionMap);
	    List<AssessmentQuestion> rList = new ArrayList<AssessmentQuestion>(assessmentList);
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

	// set session map ID so that questionlist.jsp can get sessionMAP
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, questionForm.getSessionMapID());
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Remove assessment question from HttpSession list and update page display. As authoring rule, all persist only happen
     * when user submit whole page. So this remove is just impact HttpSession values.
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int questionIdx = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_INDEX), -1);
	if (questionIdx != -1) {
	    SortedSet<AssessmentQuestion> assessmentList = getQuestionList(sessionMap);
	    List<AssessmentQuestion> rList = new ArrayList<AssessmentQuestion>(assessmentList);
	    AssessmentQuestion question = rList.remove(questionIdx);
	    assessmentList.clear();
	    assessmentList.addAll(rList);
	    // add to delList
	    List delList = getDeletedQuestionList(sessionMap);
	    delList.add(question);
	}

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }
    
    /**
     * Move up current question.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward upQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchQuestion(mapping, request, true);
    }

    /**
     * Move down current question.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return switchQuestion(mapping, request, false);
    }

    private ActionForward switchQuestion(ActionMapping mapping, HttpServletRequest request, boolean up) {
	// get back sessionMAP
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	int questionIdx = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_QUESTION_INDEX), -1);
	if (questionIdx != -1) {
	    SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	    List<AssessmentQuestion> rList = new ArrayList<AssessmentQuestion>(questionList);
	    // get current and the target item, and switch their sequnece
	    AssessmentQuestion question = rList.get(questionIdx);
	    AssessmentQuestion repQuestion;
	    if (up)
		repQuestion = rList.get(--questionIdx);
	    else
		repQuestion = rList.get(++questionIdx);
	    int upSeqId = repQuestion.getSequenceId();
	    repQuestion.setSequenceId(question.getSequenceId());
	    question.setSequenceId(upSeqId);

	    // put back list, it will be sorted again
	    questionList.clear();
	    questionList.addAll(rList);
	}

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
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
    private ActionForward newOption(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	TreeSet<AssessmentAnswerOption> optionList = getOptionsFromRequest(request, false);
	AssessmentAnswerOption option = new AssessmentAnswerOption();
	int maxSeq = 1;
	if (optionList != null && optionList.size() > 0) {
	    AssessmentAnswerOption last = optionList.last();
	    maxSeq = last.getSequenceId() + 1;
	}
	option.setSequenceId(maxSeq);
	option.setGrade(0);
	optionList.add(option);
	
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, WebUtil.readStrParam(request,
		AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_TYPE, WebUtil.readIntParam(request,
		AssessmentConstants.ATTR_QUESTION_TYPE));
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
	Set<AssessmentAnswerOption> optionList = getOptionsFromRequest(request, false);
	int optionIndex = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<AssessmentAnswerOption> rList = new ArrayList<AssessmentAnswerOption>(optionList);
	    AssessmentAnswerOption question = rList.remove(optionIndex);
	    optionList.clear();
	    optionList.addAll(rList);
	}

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, WebUtil.readStrParam(request,
		AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_TYPE, WebUtil.readIntParam(request,
		AssessmentConstants.ATTR_QUESTION_TYPE));
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
	Set<AssessmentAnswerOption> optionList = getOptionsFromRequest(request, false);
	
	int optionIndex = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<AssessmentAnswerOption> rList = new ArrayList<AssessmentAnswerOption>(optionList);
	    
	    // get current and the target item, and switch their sequnece
	    AssessmentAnswerOption option = rList.get(optionIndex);
	    AssessmentAnswerOption repOption;
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

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, WebUtil.readStrParam(request,
		AttributeNames.PARAM_CONTENT_FOLDER_ID));
	request.setAttribute(AssessmentConstants.ATTR_QUESTION_TYPE, WebUtil.readIntParam(request,
		AssessmentConstants.ATTR_QUESTION_TYPE));
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
	if (unitList != null && unitList.size() > 0) {
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
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);	
	AssessmentForm assessmentForm = (AssessmentForm) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT_FORM);
	Assessment assessment = assessmentForm.getAssessment();

	// initial Overall feedbacks list
	SortedSet<AssessmentOverallFeedback> overallFeedbackList = new TreeSet<AssessmentOverallFeedback>(
		new AssessmentOverallFeedbackComparator());
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
	if (overallFeedbackList != null && overallFeedbackList.size() > 0) {
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
     * Return AssessmentService bean.
     */
    private IAssessmentService getAssessmentService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
    }

    /**
     * @param request
     * @return
     */
    private List getAttachmentList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, AssessmentConstants.ATT_ATTACHMENT_LIST);
    }

    /**
     * @param request
     * @return
     */
    private List getDeletedAttachmentList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, AssessmentConstants.ATTR_DELETED_ATTACHMENT_LIST);
    }

    /**
     * List save current assessment questions.
     * 
     * @param request
     * @return
     */
    private SortedSet<AssessmentQuestion> getQuestionList(SessionMap sessionMap) {
	SortedSet<AssessmentQuestion> list = (SortedSet<AssessmentQuestion>) sessionMap
		.get(AssessmentConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<AssessmentQuestion>(new AssessmentQuestionComparator());
	    sessionMap.put(AssessmentConstants.ATTR_QUESTION_LIST, list);
	}
	return list;
    }

    /**
     * List save deleted assessment questions, which could be persisted or non-persisted questions.
     * 
     * @param request
     * @return
     */
    private List getDeletedQuestionList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, AssessmentConstants.ATTR_DELETED_QUESTION_LIST);
    }

    /**
     * If a assessment question has attahment file, and the user edit this question and change the attachment to new file, then
     * the old file need be deleted when submitting the whole authoring page. Save the file uuid and version id into
     * Assessmentquestion object for temporarily use.
     * 
     * @param request
     * @return
     */
    private List getDeletedQuestionAttachmentList(SessionMap sessionMap) {
	return getListFromSession(sessionMap, AssessmentConstants.ATTR_DELETED_QUESTION_ATTACHMENT_LIST);
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
	form.setGeneralFeedback(question.getGeneralFeedback());
	form.setFeedback(question.getFeedback());
	form.setMultipleAnswersAllowed(question.isMultipleAnswersAllowed());
	form.setFeedbackOnCorrect(question.getFeedbackOnCorrect());
	form.setFeedbackOnPartiallyCorrect(question.getFeedbackOnPartiallyCorrect());
	form.setFeedbackOnIncorrect(question.getFeedbackOnIncorrect());
	form.setShuffle(question.isShuffle());
	form.setCaseSensitive(question.isCaseSensitive());
	form.setCorrectAnswer(question.getCorrectAnswer());
	if (questionIdx >= 0) {
	    form.setQuestionIndex(new Integer(questionIdx).toString());
	}
	
	short questionType = question.getType();
	if ((questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)
		|| (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL)) {
	    Set<AssessmentAnswerOption> optionList = question.getAnswerOptions();
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
	 * BE CAREFUL: This method will copy nessary info from request form to an old or new AssessmentQuestion instance. It
	 * gets all info EXCEPT AssessmentQuestion.createDate and AssessmentQuestion.createBy, which need be set when persisting
	 * this assessment Question.
	 */
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(questionForm.getSessionMapID());
	// check whether it is "edit(old Question)" or "add(new Question)"
	SortedSet<AssessmentQuestion> questionList = getQuestionList(sessionMap);
	int questionIdx = NumberUtils.stringToInt(questionForm.getQuestionIndex(), -1);
	AssessmentQuestion question = null;

	if (questionIdx == -1) { // add
	    question = new AssessmentQuestion();
	    question.setCreateDate(new Timestamp(new Date().getTime()));
	    int maxSeq = 1;
	    if (questionList != null && questionList.size() > 0) {
		AssessmentQuestion last = questionList.last();
		maxSeq = last.getSequenceId() + 1;
	    }
	    question.setSequenceId(maxSeq);
	    questionList.add(question);
	} else { // edit
	    List<AssessmentQuestion> rList = new ArrayList<AssessmentQuestion>(questionList);
	    question = rList.get(questionIdx);
	}
	short type = questionForm.getQuestionType();
	question.setType(questionForm.getQuestionType());

	question.setTitle(questionForm.getTitle());
	question.setQuestion(questionForm.getQuestion());
	question.setCreateByAuthor(true);
	question.setHide(false);
	
	question.setDefaultGrade(Integer.parseInt(questionForm.getDefaultGrade()));
	question.setGeneralFeedback(questionForm.getGeneralFeedback());
	
	if (type == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
	    question.setMultipleAnswersAllowed(questionForm.isMultipleAnswersAllowed());
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
	    question.setPenaltyFactor(1);
	    question.setCorrectAnswer(questionForm.isCorrectAnswer());
	    question.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    question.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());	    
	} else if ((type == AssessmentConstants.QUESTION_TYPE_ESSAY)) {
	    question.setFeedback(questionForm.getFeedback());
	} else if (type == AssessmentConstants.QUESTION_TYPE_ORDERING) {
	    question.setPenaltyFactor(Float.parseFloat(questionForm.getPenaltyFactor()));
	    question.setFeedbackOnCorrect(questionForm.getFeedbackOnCorrect());
	    question.setFeedbackOnIncorrect(questionForm.getFeedbackOnIncorrect());
	}
	
	// set options
	if ((type == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		|| (type == AssessmentConstants.QUESTION_TYPE_ORDERING)
		|| (type == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS)
		|| (type == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)
		|| (type == AssessmentConstants.QUESTION_TYPE_NUMERICAL)) {
	    Set<AssessmentAnswerOption> optionList = getOptionsFromRequest(request, true);
	    Set options = new LinkedHashSet();
	    int seqId = 0;
	    for (AssessmentAnswerOption option : optionList) {
		option.setSequenceId(seqId++);
		options.add(option);
	    }
	    question.setAnswerOptions(options);
	}
	// set units
	if (type == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
	    Set<AssessmentUnit> unitList = getUnitsFromRequest(request, true);
	    Set units = new LinkedHashSet();
	    int seqId = 0;
	    for (AssessmentUnit unit : unitList) {
		unit.setSequenceId(seqId++);
		units.add(unit);
	    }
	    question.setUnits(units);
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
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString()))
	    mode = ToolAccessMode.TEACHER;
	else
	    mode = ToolAccessMode.AUTHOR;
	return mode;
    }
    
    /**
     * Get answer options from <code>HttpRequest</code>
     * 
     * @param request
     * @param isForSaving whether the blank options will be preserved or not 
     * 
     */
    private TreeSet<AssessmentAnswerOption> getOptionsFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, AssessmentConstants.ATTR_OPTION_LIST);

	int count = NumberUtils.stringToInt(paramMap.get(AssessmentConstants.ATTR_OPTION_COUNT));
	int questionType = WebUtil.readIntParam(request,AssessmentConstants.ATTR_QUESTION_TYPE);
	TreeSet<AssessmentAnswerOption> optionList = new TreeSet<AssessmentAnswerOption>(
		new AssessmentAnswerOptionComparator());
	for (int i = 0; i < count; i++) {
	    if ((questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE)
		    || (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER)) {
		String answerString = paramMap.get(AssessmentConstants.ATTR_OPTION_ANSWER_PREFIX + i);
		if ((answerString == null) && isForSaving) {
		    continue;
		}

		AssessmentAnswerOption option = new AssessmentAnswerOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.stringToInt(sequenceId));
		option.setAnswerString(answerString);
		float grade = Float.valueOf(paramMap.get(AssessmentConstants.ATTR_OPTION_GRADE_PREFIX + i));
		option.setGrade(grade);
		option.setFeedback((String) paramMap.get(AssessmentConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
		optionList.add(option);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		String question = paramMap.get(AssessmentConstants.ATTR_OPTION_QUESTION_PREFIX + i);
		if ((question == null) && isForSaving) {
		    continue;
		}
		
		AssessmentAnswerOption option = new AssessmentAnswerOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);		
		option.setSequenceId(NumberUtils.stringToInt(sequenceId));
		option.setAnswerString((String) paramMap.get(AssessmentConstants.ATTR_OPTION_ANSWER_PREFIX + i));
		option.setQuestion(question);
		optionList.add(option);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
		String answerString = paramMap.get(AssessmentConstants.ATTR_OPTION_ANSWER_PREFIX + i);
		if ((answerString == null) && isForSaving) {
		    continue;
		}
		
		AssessmentAnswerOption option = new AssessmentAnswerOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.stringToInt(sequenceId));
		option.setAnswerString(answerString);
		String acceptedErrorStr = paramMap.get(AssessmentConstants.ATTR_OPTION_ACCEPTED_ERROR_PREFIX + i);
		if (isForSaving && !StringUtils.isBlank(acceptedErrorStr)) {
		    float acceptedError = Float.valueOf(acceptedErrorStr);
		    option.setAcceptedError(acceptedError);
		} else {
		    option.setAcceptedErrorStr(acceptedErrorStr);
		}
		float grade = Float.valueOf(paramMap.get(AssessmentConstants.ATTR_OPTION_GRADE_PREFIX + i));
		option.setGrade(grade);    
		option.setFeedback((String) paramMap.get(AssessmentConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
		optionList.add(option);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		String answerString = paramMap.get(AssessmentConstants.ATTR_OPTION_ANSWER_PREFIX + i);
		if ((answerString == null) && isForSaving) {
		    continue;
		}

		AssessmentAnswerOption option = new AssessmentAnswerOption();
		String sequenceId = paramMap.get(AssessmentConstants.ATTR_OPTION_SEQUENCE_ID_PREFIX + i);
		option.setSequenceId(NumberUtils.stringToInt(sequenceId));
		option.setAnswerString(answerString);
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

	int count = NumberUtils.stringToInt(paramMap.get(AssessmentConstants.ATTR_UNIT_COUNT));
	TreeSet<AssessmentUnit> unitList = new TreeSet<AssessmentUnit>(new AssessmentUnitComparator());
	for (int i = 0; i < count; i++) {
	    String unitStr = paramMap.get(AssessmentConstants.ATTR_UNIT_UNIT_PREFIX + i);
	    if (StringUtils.isBlank(unitStr) && isForSaving) {
		continue;
	    }
	    
	    AssessmentUnit unit = new AssessmentUnit();
	    String sequenceId = paramMap.get(AssessmentConstants.ATTR_UNIT_SEQUENCE_ID_PREFIX + i);	    
	    unit.setSequenceId(NumberUtils.stringToInt(sequenceId));
	    unit.setUnit(unitStr);
	    String multiplierStr = (String) paramMap.get(AssessmentConstants.ATTR_UNIT_MULTIPLIER_PREFIX + i);
	    if (isForSaving && !StringUtils.isBlank(multiplierStr)) {
		float multiplier = Float.valueOf(multiplierStr);
		unit.setMultiplier(multiplier);
	    } else {
		unit.setMultiplierStr(multiplierStr);
	    }	    
	    unitList.add(unit);
	}
	return unitList;
    }
    
    /**
     * Get overall feedbacks from <code>HttpRequest</code>
     * 
     * @param request
     */
    private TreeSet<AssessmentOverallFeedback> getOverallFeedbacksFromRequest(HttpServletRequest request, boolean skipBlankOverallFeedbacks) {
	int count = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_COUNT));
	TreeSet<AssessmentOverallFeedback> overallFeedbackList = new TreeSet<AssessmentOverallFeedback>(
		new AssessmentOverallFeedbackComparator());
	for (int i = 0; i < count; i++) {
	    String gradeBoundaryStr = request
		    .getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i);
	    String feedback = request.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_FEEDBACK_PREFIX + i);
	    String sequenceId = request.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_SEQUENCE_ID_PREFIX + i);

	    if ((StringUtils.isBlank(feedback) || StringUtils.isBlank(gradeBoundaryStr)) && skipBlankOverallFeedbacks) {
		continue;
	    }
	    AssessmentOverallFeedback overallFeedback = new AssessmentOverallFeedback();
	    overallFeedback.setSequenceId(NumberUtils.stringToInt(sequenceId));
	    if (!StringUtils.isBlank(gradeBoundaryStr)) {
		int gradeBoundary = NumberUtils.stringToInt(request
			.getParameter(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i));
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
    private TreeSet<AssessmentOverallFeedback> getOverallFeedbacksFromForm(HttpServletRequest request, boolean skipBlankOverallFeedbacks) {
	Map<String, String> paramMap = splitRequestParameter(request, AssessmentConstants.ATTR_OVERALL_FEEDBACK_LIST);
	
	int count = NumberUtils.stringToInt(paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_COUNT));
	TreeSet<AssessmentOverallFeedback> overallFeedbackList = new TreeSet<AssessmentOverallFeedback>(
		new AssessmentOverallFeedbackComparator());
	for (int i = 0; i < count; i++) {
	    String gradeBoundaryStr = paramMap
		    .get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i);
	    String feedback = paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_FEEDBACK_PREFIX + i);
	    String sequenceId = paramMap.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_SEQUENCE_ID_PREFIX + i);

	    if ((StringUtils.isBlank(feedback) || StringUtils.isBlank(gradeBoundaryStr)) && skipBlankOverallFeedbacks) {
		continue;
	    }
	    AssessmentOverallFeedback overallFeedback = new AssessmentOverallFeedback();
	    overallFeedback.setSequenceId(NumberUtils.stringToInt(sequenceId));
	    if (!StringUtils.isBlank(gradeBoundaryStr)) {
		int gradeBoundary = NumberUtils.stringToInt(paramMap
			.get(AssessmentConstants.ATTR_OVERALL_FEEDBACK_GRADE_BOUNDARY_PREFIX + i));
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
     * @param parameterName parameterName
     */
    private Map<String, String> splitRequestParameter(HttpServletRequest request, String parameterName) {
	String list = request.getParameter(parameterName);
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
}
