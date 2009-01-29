/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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

/* $Id$ */
package org.lamsfoundation.lams.tool.assessment.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.events.DeliveryMethodMail;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.service.UploadAssessmentFileException;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentQuestionComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.AssessmentQuestionForm;
import org.lamsfoundation.lams.tool.assessment.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author Andrey Balan
 * 
 * @version $Revision$
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	// -----------------------Assessment Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("complete")) {
	    return complete(mapping, form, request, response);
	}

	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("addfile")) {
	    return addQuestion(mapping, form, request, response);
	}
	if (param.equals("addurl")) {
	    return addQuestion(mapping, form, request, response);
	}
	if (param.equals("saveOrUpdateQuestion")) {
	    return saveOrUpdateQuestion(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(AssessmentConstants.ERROR);
    }

    /**
     * Initial page for add assessment question (single file or URL).
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward addQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	AssessmentQuestionForm questionForm = (AssessmentQuestionForm) form;
	questionForm.setMode(WebUtil.readStrParam(request, AttributeNames.ATTR_MODE));
	questionForm.setSessionMapID(WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID));
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Read assessment data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
     * 
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(AssessmentConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	// get back the assessment and question list and display them on page
	IAssessmentService service = getAssessmentService();
	AssessmentUser assessmentUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // assessmentUser may be null if the user was force completed.
	    assessmentUser = getSpecifiedUser(service, sessionId, WebUtil.readIntParam(request,
		    AttributeNames.PARAM_USER_ID, false));
	} else {
	    assessmentUser = getCurrentUser(service, sessionId);
	}

	List<AssessmentQuestion> questions = null;
	Assessment assessment;
	questions = service.getAssessmentQuestionsBySessionId(sessionId);
	assessment = service.getAssessmentBySessionId(sessionId);

	// check whehter finish lock is on/off
	//TODO!!
	boolean lock = true;//assessment.getTimeLimit() && assessmentUser != null && assessmentUser.isSessionFinished();

	// check whether there is only one assessment question and run auto flag is true or not.
	boolean runAuto = false;
	int questionsNumber = 0;
	if (assessment.getQuestions() != null) {
	    questionsNumber = assessment.getQuestions().size();
//	    if (assessment.isRunAuto() && questionsNumber == 1) {
//		AssessmentQuestion question = (AssessmentQuestion) assessment.getAssessmentQuestions().iterator().next();
//		// only visible question can be run auto.
//		if (!question.isHide()) {
//		    runAuto = true;
//		    request.setAttribute(AssessmentConstants.ATTR_QUESTION_UID, question.getUid());
//		}
//	    }
	}

	// get notebook entry
	String entryText = new String();
	if (assessmentUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    AssessmentConstants.TOOL_SIGNATURE, assessmentUser.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// basic information
	sessionMap.put(AssessmentConstants.ATTR_TITLE, assessment.getTitle());
	sessionMap.put(AssessmentConstants.ATTR_INSTRUCTIONS, assessment.getInstructions());
	sessionMap.put(AssessmentConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(AssessmentConstants.ATTR_LOCK_ON_FINISH, assessment.getTimeLimit());
	sessionMap.put(AssessmentConstants.ATTR_USER_FINISHED, assessmentUser != null
		&& assessmentUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ON, assessment.isReflectOnActivity());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_INSTRUCTION, assessment.getReflectInstructions());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ENTRY, entryText);
	sessionMap.put(AssessmentConstants.ATTR_RUN_AUTO, new Boolean(runAuto));

	// add define later support
	if (assessment.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	assessment.setContentInUse(true);
	assessment.setDefineLater(false);
	service.saveOrUpdateAssessment(assessment);

	// add run offline support
	if (assessment.getRunOffline()) {
	    sessionMap.put(AssessmentConstants.PARAM_RUN_OFFLINE, true);
	    return mapping.findForward("runOffline");
	} else {
	    sessionMap.put(AssessmentConstants.PARAM_RUN_OFFLINE, false);
	}

	// init assessment question list
	SortedSet<AssessmentQuestion> assessmentQuestionList = getAssessmentQuestionList(sessionMap);
	assessmentQuestionList.clear();
	if (questions != null) {
	    // remove hidden questions.
	    for (AssessmentQuestion question : questions) {
		// becuase in webpage will use this login name. Here is just
		// initial it to avoid session close error in proxy object.
		if (question.getCreateBy() != null) {
		    question.getCreateBy().getLoginName();
		}
		if (!question.isHide()) {
		    assessmentQuestionList.add(question);
		}
	    }
	}

	// set complete flag for display purpose
	if (assessmentUser != null) {
	    service.retrieveComplete(assessmentQuestionList, assessmentUser);
	}
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, assessment);

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Mark assessment question as complete status.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward complete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);

	doComplete(request);

	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Finish learning session.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// auto run mode, when use finish the only one assessment question, mark it as complete then finish this activity as
	// well.
	String assessmentQuestionUid = request.getParameter(AssessmentConstants.PARAM_QUESTION_UID);
	if (assessmentQuestionUid != null) {
	    doComplete(request);
	    // NOTE:So far this flag is useless(31/08/2006).
	    // set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
	    request.setAttribute(AssessmentConstants.ATTR_RUN_AUTO, true);
	} else {
	    request.setAttribute(AssessmentConstants.ATTR_RUN_AUTO, false);
	}

	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	IAssessmentService service = getAssessmentService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(AssessmentConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (AssessmentApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Save file or url assessment question into database.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward saveOrUpdateQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get back SessionMap
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long sessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);

	String mode = request.getParameter(AttributeNames.ATTR_MODE);
	AssessmentQuestionForm questionForm = (AssessmentQuestionForm) form;
	ActionErrors errors = validateAssessmentQuestion(questionForm);

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return findForward(questionForm.getQuestionType(), mapping);
	}
	short type = questionForm.getQuestionType();

	// create a new Assessmentquestion
	AssessmentQuestion question = new AssessmentQuestion();
	IAssessmentService service = getAssessmentService();
	AssessmentUser assessmentUser = getCurrentUser(service, sessionId);
	question.setType(type);
	question.setTitle(questionForm.getTitle());
	question.setQuestion(questionForm.getQuestion());
	question.setCreateDate(new Timestamp(new Date().getTime()));
	question.setCreateByAuthor(false);
	question.setCreateBy(assessmentUser);

	// special attribute for URL or FILE
	if (type == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
//	    try {
//		service.uploadAssessmentQuestionFile(question, questionForm.getFile());
//	    } catch (UploadAssessmentFileException e) {
//		LearningAction.log.error("Failed upload Assessment File " + e.toString());
//		return mapping.findForward(AssessmentConstants.ERROR);
//	    }
	} else if (type == AssessmentConstants.QUESTION_TYPE_CHOICE) {
//	    question.setUrl(questionForm.getUrl());
//	    question.setOpenUrlNewWindow(questionForm.isOpenUrlNewWindow());
	}
	// save and update session

	AssessmentSession session = service.getAssessmentSessionBySessionId(sessionId);
	if (session == null) {
	    LearningAction.log.error("Failed update AssessmentSession by ID[" + sessionId + "]");
	    return mapping.findForward(AssessmentConstants.ERROR);
	}
	Set<AssessmentQuestion> questions = session.getAssessmentQuestions();
	if (questions == null) {
	    questions = new HashSet<AssessmentQuestion>();
	    session.setAssessmentQuestions(questions);
	}
	questions.add(question);
	service.saveOrUpdateAssessmentSession(session);

	// update session value
	SortedSet<AssessmentQuestion> assessmentQuestionList = getAssessmentQuestionList(sessionMap);
	assessmentQuestionList.add(question);

	// URL or file upload
	request.setAttribute(AssessmentConstants.ATTR_ADD_ASSESSMENT_TYPE, new Short(type));
	request.setAttribute(AttributeNames.ATTR_MODE, mode);

	Assessment assessment = session.getAssessment();
	if (assessment.isNotifyTeachersOnAttemptCompletion()) {
	    List<User> monitoringUsers = service.getMonitorsByToolSessionId(sessionId);
	    if (monitoringUsers != null && !monitoringUsers.isEmpty()) {
		Long[] monitoringUsersIds = new Long[monitoringUsers.size()];
		for (int i = 0; i < monitoringUsersIds.length; i++) {
		    monitoringUsersIds[i] = monitoringUsers.get(i).getUserId().longValue();
		}
		String fullName = assessmentUser.getLastName() + " " + assessmentUser.getFirstName();
		service.getEventNotificationService().sendMessage(monitoringUsersIds, DeliveryMethodMail.getInstance(),
			service.getLocalisedMessage("event.assigment.submit.subject", null),
			service.getLocalisedMessage("event.assigment.submit.body", new Object[] { fullName }));
	    }
	}
	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Display empty reflection form.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	if (!validateBeforeFinish(request, sessionMapID)) {
	    return mapping.getInputForward();
	}

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IAssessmentService submitFilesService = getAssessmentService();

	SessionMap map = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		AssessmentConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(AssessmentConstants.SUCCESS);
    }

    /**
     * Submit reflection form input database.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IAssessmentService service = getAssessmentService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		AssessmentConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    AssessmentConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private boolean validateBeforeFinish(HttpServletRequest request, String sessionMapID) {
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());

	IAssessmentService service = getAssessmentService();
	//TODO
	int miniViewFlag = 0;//service.checkMiniView(sessionId, userID);
	// if current user view less than reqired view count number, then just return error message.
	// if it is runOffline content, then need not check minimum view count
	Boolean runOffline = (Boolean) sessionMap.get(AssessmentConstants.PARAM_RUN_OFFLINE);
	if (miniViewFlag > 0 && !runOffline) {
	    ActionErrors errors = new ActionErrors();
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("lable.learning.minimum.view.number.less",
		    miniViewFlag));
	    this.addErrors(request, errors);
	    return false;
	}

	return true;
    }

    private IAssessmentService getAssessmentService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
    }

    /**
     * List save current assessment questions.
     * 
     * @param request
     * @return
     */
    private SortedSet<AssessmentQuestion> getAssessmentQuestionList(SessionMap sessionMap) {
	SortedSet<AssessmentQuestion> list = (SortedSet<AssessmentQuestion>) sessionMap
		.get(AssessmentConstants.ATTR_QUESTION_LIST);
	if (list == null) {
	    list = new TreeSet<AssessmentQuestion>(new AssessmentQuestionComparator());
	    sessionMap.put(AssessmentConstants.ATTR_QUESTION_LIST, list);
	}
	return list;
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
     * Return <code>ActionForward</code> according to assessment question type.
     * 
     * @param type
     * @param mapping
     * @return
     */
    private ActionForward findForward(short type, ActionMapping mapping) {
	ActionForward forward;
	switch (type) {
	case AssessmentConstants.QUESTION_TYPE_CHOICE:
	    forward = mapping.findForward("url");
	    break;
	case AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE:
	    forward = mapping.findForward("file");
	    break;
	case AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS:
	    forward = mapping.findForward("website");
	    break;
	default:
	    forward = null;
	    break;
	}
	return forward;
    }

    private AssessmentUser getCurrentUser(IAssessmentService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	AssessmentUser assessmentUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);

	if (assessmentUser == null) {
	    AssessmentSession session = service.getAssessmentSessionBySessionId(sessionId);
	    assessmentUser = new AssessmentUser(user, session);
	    service.createUser(assessmentUser);
	}
	return assessmentUser;
    }

    private AssessmentUser getSpecifiedUser(IAssessmentService service, Long sessionId, Integer userId) {
	AssessmentUser assessmentUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (assessmentUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for assessment activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return assessmentUser;
    }

    /**
     * @param questionForm
     * @return
     */
    private ActionErrors validateAssessmentQuestion(AssessmentQuestionForm questionForm) {
	ActionErrors errors = new ActionErrors();
	if (StringUtils.isBlank(questionForm.getTitle())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(AssessmentConstants.ERROR_MSG_QUESTION_NAME_BLANK));
	}

	if (questionForm.getQuestionType() == AssessmentConstants.QUESTION_TYPE_CHOICE) {
//	    if (StringUtils.isBlank(questionForm.getUrl())) {
//		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(AssessmentConstants.ERROR_MSG_URL_BLANK));
//		// URL validation: Commom URL validate(1.3.0) work not very well: it can not support http://address:port
//		// format!!!
//		// UrlValidator validator = new UrlValidator();
//		// if(!validator.isValid(questionForm.getUrl()))
//		// errors.add(ActionMessages.GLOBAL_MESSAGE,new
//		// ActionMessage(AssessmentConstants.ERROR_MSG_INVALID_URL));
//	    }
	}
	// if(questionForm.getquestionType() == AssessmentConstants.RESOURCE_TYPE_WEBSITE
	// ||questionForm.getquestionType() == AssessmentConstants.RESOURCE_TYPE_LEARNING_OBJECT){
	// if(StringUtils.isBlank(questionForm.getDescription()))
	// errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(AssessmentConstants.ERROR_MSG_DESC_BLANK));
	// }
//	if (questionForm.getQuestionType() == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS
//		|| questionForm.getQuestionType() == AssessmentConstants.QUESTION_TYPE_FILL_THE_GAP
//		|| questionForm.getQuestionType() == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
//
//	    if (questionForm.getFile() != null && FileUtil.isExecutableFile(questionForm.getFile().getFileName())) {
//		ActionMessage msg = new ActionMessage("error.attachment.executable");
//		errors.add(ActionMessages.GLOBAL_MESSAGE, msg);
//	    }
//
//	    // validate question size
//	    FileValidatorUtil.validateFileSize(questionForm.getFile(), false, errors);
//
//	    // for edit validate: file already exist
//	    if (!questionForm.isHasFile()
//		    && (questionForm.getFile() == null || StringUtils.isEmpty(questionForm.getFile().getFileName()))) {
//		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(AssessmentConstants.ERROR_MSG_FILE_BLANK));
//	    }
//	}
	return errors;
    }

    /**
     * Set complete flag for given assessment question.
     * 
     * @param request
     * @param sessionId
     */
    private void doComplete(HttpServletRequest request) {
	// get back sessionMap
	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long assessmentQuestionUid = new Long(request.getParameter(AssessmentConstants.PARAM_QUESTION_UID));
	IAssessmentService service = getAssessmentService();
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	Long sessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	service.setQuestionComplete(assessmentQuestionUid, new Long(user.getUserID().intValue()), sessionId);

	// set assessment question complete tag
	SortedSet<AssessmentQuestion> assessmentQuestionList = getAssessmentQuestionList(sessionMap);
	for (AssessmentQuestion question : assessmentQuestionList) {
	    if (question.getUid().equals(assessmentQuestionUid)) {
		question.setComplete(true);
		break;
	    }
	}
    }

}
