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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentOptionAnswer;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;
import org.lamsfoundation.lams.tool.assessment.service.AssessmentApplicationException;
import org.lamsfoundation.lams.tool.assessment.service.IAssessmentService;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentQuestionResultComparator;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;
import org.lamsfoundation.lams.tool.assessment.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author Andrey Balan
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
	if (param.equals("nextPage")) {
	    return nextPage(mapping, form, request, response);
	}
	if (param.equals("submitAll")) {
	    return submitAll(mapping, form, request, response);
	}
	if (param.equals("finishTest")) {
	    return finishTest(mapping, form, request, response);
	}
	if (param.equals("resubmit")) {
	    return resubmit(mapping, form, request, response);
	}	
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}
	if (param.equals("upOption")) {
	    return upOption(mapping, form, request, response);
	}
	if (param.equals("downOption")) {
	    return downOption(mapping, form, request, response);
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
     * Read assessment data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce question lost when user "refresh page",
     * @throws ServletException 
     * 
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// initialize Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long toolSessionId = new Long(request.getParameter(AssessmentConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	// get back the assessment and question list and display them on page
	IAssessmentService service = getAssessmentService();
	AssessmentUser assessmentUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // assessmentUser may be null if the user was force completed.
	    assessmentUser = getSpecifiedUser(service, toolSessionId, WebUtil.readIntParam(request,
		    AttributeNames.PARAM_USER_ID, false));
	} else {
	    assessmentUser = getCurrentUser(service, toolSessionId);
	}

	List<AssessmentQuestion> questionsFromDB = service.getAssessmentQuestionsBySessionId(toolSessionId);
	Assessment assessment = service.getAssessmentBySessionId(toolSessionId);

	// check whehter finish lock is on/off
	// TODO!! assessment.getTimeLimit()
	boolean finishedLock = (assessmentUser != null && assessmentUser.isSessionFinished());

	// get notebook entry
	String entryText = new String();
	if (assessmentUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    AssessmentConstants.TOOL_SIGNATURE, assessmentUser.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	// basic information
	sessionMap.put(AssessmentConstants.ATTR_TITLE, assessment.getTitle());
	sessionMap.put(AssessmentConstants.ATTR_INSTRUCTIONS, assessment.getInstructions());
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, false);
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, finishedLock);
	//sessionMap.put(AssessmentConstants.ATTR_LOCK_ON_FINISH, assessment.getTimeLimit());
	sessionMap.put(AssessmentConstants.ATTR_USER_FINISHED, assessmentUser != null
		&& assessmentUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AssessmentConstants.ATTR_USER, assessmentUser);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// reflection information
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ON, assessment.isReflectOnActivity());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_INSTRUCTION, assessment.getReflectInstructions());
	sessionMap.put(AssessmentConstants.ATTR_REFLECTION_ENTRY, entryText);

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

	SortedSet<AssessmentQuestion> questionList = new TreeSet<AssessmentQuestion>(new SequencableComparator());
	if (questionsFromDB != null) {
	    // remove hidden questions.
	    for (AssessmentQuestion question : questionsFromDB) {
		// becuase in webpage will use this login name. Here is just
		// initialize it to avoid session close error in proxy object.
		if (question.getCreateBy() != null) {
		    question.getCreateBy().getLoginName();
		}
		if (!question.isHide()) {
		    questionList.add(question);
		}
	    }
	}
	
	// TODO it moght need to be changed 
	//setAttemptStarted
//	if (mode != null && mode.isLearner()) {
	    service.setAttemptStarted(assessment, assessmentUser, toolSessionId);
//	}
	
	//paging
	ArrayList<SortedSet<AssessmentQuestion>> pagedQuestions = new ArrayList<SortedSet<AssessmentQuestion>>();
	int maxQuestionsPerPage = (assessment.getQuestionsPerPage() != 0) ? assessment.getQuestionsPerPage()
		: questionList.size();
	SortedSet<AssessmentQuestion> questionsForOnePage = new TreeSet<AssessmentQuestion>(
		new SequencableComparator());
	pagedQuestions.add(questionsForOnePage);

	int count = 0;
	for (AssessmentQuestion question : questionList) {
	    questionsForOnePage.add(question);
	    count++;
	    if ((questionsForOnePage.size() == maxQuestionsPerPage) && (count != questionList.size())) {
		questionsForOnePage = new TreeSet<AssessmentQuestion>(new SequencableComparator());
		pagedQuestions.add(questionsForOnePage);
	    }
	}
	
	sessionMap.put(AssessmentConstants.ATTR_PAGED_QUESTIONS, pagedQuestions);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
	sessionMap.put(AssessmentConstants.ATTR_ASSESSMENT, assessment);
	sessionMap.put(AssessmentConstants.ATTR_QUESTION_LIST, questionList);
	
	// set complete flag for display purpose
	//TODO check this out
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());
	int dbResultCount = service.getAssessmentResultCount(toolSessionId, userID);
	if (dbResultCount > 0) {
	    loadupLastAttempt(sessionMap);    
	}

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
    private ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	boolean finishedLock = (Boolean) sessionMap.get(AssessmentConstants.ATTR_FINISHED_LOCK);
	if (! finishedLock) {
	    preserveUserAnswers(request);    
	}
	
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, WebUtil.readIntParam(request, AssessmentConstants.ATTR_PAGE_NUMBER));
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);	
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
    private ActionForward submitAll(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	preserveUserAnswers(request);
	processUserAnswers(sessionMap);
	loadupResultMarks(sessionMap);
	
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, isResubmitAllowed(sessionMap));
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, true);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER));
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
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
    private ActionForward finishTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	preserveUserAnswers(request);
	processUserAnswers(sessionMap);
	loadupResultMarks(sessionMap);
	
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	IAssessmentService service = getAssessmentService();
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());
	    service.finishToolSession(sessionId, userID);
	} catch (AssessmentApplicationException e) {
	    LearningAction.log.error("Failed finishing tool session:" + e.getMessage());
	}
	
	sessionMap.put(AssessmentConstants.ATTR_IS_RESUBMIT_ALLOWED, isResubmitAllowed(sessionMap));
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, true);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER));
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
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
    private ActionForward resubmit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	Long toolSessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
	AssessmentUser assessmentUser = (AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER);
	IAssessmentService service = getAssessmentService();
	service.setAttemptStarted(assessment, assessmentUser, toolSessionId);
	loadupLastAttempt(sessionMap);
	
	sessionMap.put(AssessmentConstants.ATTR_FINISHED_LOCK, false);
	sessionMap.put(AssessmentConstants.ATTR_PAGE_NUMBER, 1);
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

//	// auto run mode, when use finish the only one assessment question, mark it as complete then finish this
//	// activity as
//	// well.
//	String assessmentQuestionUid = request.getParameter(AssessmentConstants.PARAM_QUESTION_UID);
//	if (assessmentQuestionUid != null) {
//	    doComplete(request);
//	    // NOTE:So far this flag is useless(31/08/2006).
//	    // set flag, then finish page can know redir target is parent(AUTO_RUN) or self(normal)
////	    request.setAttribute(AssessmentConstants.ATTR_RUN_AUTO, true);
//	} else {
////	    request.setAttribute(AssessmentConstants.ATTR_RUN_AUTO, false);
//	}

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
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);
	ArrayList<SortedSet<AssessmentQuestion>> pagedResults = (ArrayList<SortedSet<AssessmentQuestion>>) sessionMap.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	SortedSet<AssessmentQuestion> questionsForOnePage = (SortedSet<AssessmentQuestion>) pagedResults.get(pageNumber-1);
	Long questionUid = new Long(request.getParameter(AssessmentConstants.PARAM_QUESTION_UID));
	
	AssessmentQuestion question = null;
	for (AssessmentQuestion tempQuestion : questionsForOnePage) {
	    if (tempQuestion.getUid().equals(questionUid)) {
		question = tempQuestion;
		break;
	    }
	}
	
	Set<AssessmentQuestionOption> optionList = question.getQuestionOptions();
	
	int optionIndex = NumberUtils.stringToInt(request.getParameter(AssessmentConstants.PARAM_OPTION_INDEX), -1);
	if (optionIndex != -1) {
	    List<AssessmentQuestionOption> rList = new ArrayList<AssessmentQuestionOption>(optionList);
	    
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
	    optionList = new TreeSet<AssessmentQuestionOption>(new SequencableComparator());
	    optionList.addAll(rList);
	    question.setQuestionOptions(optionList);
	}

	request.setAttribute(AssessmentConstants.ATTR_QUESTION_FOR_ORDERING, question);
	request.setAttribute(AssessmentConstants.ATTR_SESSION_MAP_ID, sessionMapID);
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
    
    private void preserveUserAnswers(HttpServletRequest request){
	String sessionMapID = WebUtil.readStrParam(request, AssessmentConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	int pageNumber = (Integer) sessionMap.get(AssessmentConstants.ATTR_PAGE_NUMBER);
	ArrayList<SortedSet<AssessmentQuestion>> pagedQuestions = (ArrayList<SortedSet<AssessmentQuestion>>) sessionMap.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	SortedSet<AssessmentQuestion> questionsForOnePage = (SortedSet<AssessmentQuestion>) pagedQuestions.get(pageNumber-1);
	int count = questionsForOnePage.size(); 
	
	for (int i = 0; i < count; i++) {
	    Long assessmentQuestionUid = WebUtil.readLongParam(request, AssessmentConstants.PARAM_QUESTION_UID + i);
	    AssessmentQuestion question = null;
	    for (AssessmentQuestion sessionQuestion : questionsForOnePage) {
		if (sessionQuestion.getUid().equals(assessmentQuestionUid)) {
		    question = sessionQuestion;
		    break;
		}
	    }
	    int questionType = question.getType();

	    if (questionType == AssessmentConstants.QUESTION_TYPE_MULTIPLE_CHOICE) {
		for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		    boolean answerBoolean = false;
		    if (question.isMultipleAnswersAllowed()) {
			String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_"
				+ option.getSequenceId());
			answerBoolean = !StringUtils.isBlank(answerString);
		    } else {
			String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
			if (answerString != null) {
			    int optionSequenceId = Integer.parseInt(answerString);
			    answerBoolean = (option.getSequenceId() == optionSequenceId);
			}
		    }
		    option.setAnswerBoolean(answerBoolean);
		}
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_MATCHING_PAIRS) {
		for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		    int answerInt = WebUtil.readIntParam(request, AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_" + option.getSequenceId()); 
		    option.setAnswerInt(answerInt);
		}
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_SHORT_ANSWER) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		question.setAnswerString(answerString);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_NUMERICAL) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		question.setAnswerString(answerString);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_TRUE_FALSE) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		if (answerString != null) {
		    question.setAnswerBoolean(Boolean.parseBoolean(answerString));
		    question.setAnswerString("not null");
		}
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ESSAY) {
		String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i);
		question.setAnswerString(answerString);
	    } else if (questionType == AssessmentConstants.QUESTION_TYPE_ORDERING) {
		//TODO correct answer
		for (AssessmentQuestionOption option : question.getQuestionOptions()) {
		    String answerString = request.getParameter(AssessmentConstants.ATTR_QUESTION_PREFIX + i + "_"
				+ option.getSequenceId());
		    option.setAnswerInt(option.getSequenceId());
		}
	    }
	}
    }
    
    private void loadupResultMarks(SessionMap sessionMap){
	ArrayList<SortedSet<AssessmentQuestion>> pagedQuestions = (ArrayList<SortedSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	AssessmentResult result = service.getLastAssessmentResult(assessmentUid,userId);
	
	for(SortedSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
		    if (question.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
			question.setMark(questionResult.getMark());
			question.setPenalty(questionResult.getPenalty());
		    }
		}		
	    }
	}
    }    
    
    private void loadupLastAttempt(SessionMap sessionMap){
	ArrayList<SortedSet<AssessmentQuestion>> pagedQuestions = (ArrayList<SortedSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	AssessmentResult result = service.getLastAssessmentResult(assessmentUid,userId);
	
	for(SortedSet<AssessmentQuestion> questionsForOnePage : pagedQuestions) {
	    for (AssessmentQuestion question : questionsForOnePage) {
		for (AssessmentQuestionResult questionResult : result.getQuestionResults()) {
		    if (question.getUid().equals(questionResult.getAssessmentQuestion().getUid())) {
			question.setAnswerBoolean(questionResult.getAnswerBoolean());
			question.setAnswerFloat(questionResult.getAnswerFloat());
			question.setAnswerString(questionResult.getAnswerString());
			question.setMark(questionResult.getMark());
			question.setPenalty(questionResult.getPenalty());
			
			for (AssessmentQuestionOption questionOption : question.getQuestionOptions()) {
			    for (AssessmentOptionAnswer optionAnswer : questionResult.getOptionAnswers()) {
				if (questionOption.getSequenceId() == optionAnswer.getSequenceId()) {
				    questionOption.setAnswerBoolean(optionAnswer.getAnswerBoolean());
				    questionOption.setAnswerInt(optionAnswer.getAnswerInt());
				    break;
				}
			    }			    
			}
			break;
		    }
		}		
	    }
	}
    }
    
    /**
     * Get answer options from <code>HttpRequest</code>
     * 
     * @param request
     * 
     */
    private void processUserAnswers(SessionMap sessionMap) {
	ArrayList<SortedSet<AssessmentQuestion>> pagedQuestions = (ArrayList<SortedSet<AssessmentQuestion>>) sessionMap
		.get(AssessmentConstants.ATTR_PAGED_QUESTIONS);
	Long assessmentUid = ((Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT)).getUid();
	Long userId = ((AssessmentUser) sessionMap.get(AssessmentConstants.ATTR_USER)).getUserId();
	IAssessmentService service = getAssessmentService();
	service.processUserAnswers(assessmentUid, userId, pagedQuestions);
    }
    
    /**
     * Checks if the resubmit action allowed.
     * 
     * @param request
     * 
     */
    private boolean isResubmitAllowed(SessionMap sessionMap) {
	Long toolSessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	IAssessmentService service = getAssessmentService();
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(userDTO.getUserID().longValue());
	AssessmentUser user = service.getUserByIDAndSession(userID, toolSessionId);
	
	int dbResultCount = service.getAssessmentResultCount(toolSessionId, userID);
	Assessment assessment = (Assessment) sessionMap.get(AssessmentConstants.ATTR_ASSESSMENT);
	int attemptsAllowed = assessment.getAttemptsAllowed();
	return ((attemptsAllowed > dbResultCount) | (attemptsAllowed == 0)) && !user.isSessionFinished();
    }
    
    private IAssessmentService getAssessmentService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (IAssessmentService) wac.getBean(AssessmentConstants.ASSESSMENT_SERVICE);
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


//    /**
//     * Set complete flag for given assessment question.
//     * 
//     * @param request
//     * @param sessionId
//     */
//    private void doComplete(HttpServletRequest request) {
//	// get back sessionMap
//	String sessionMapID = request.getParameter(AssessmentConstants.ATTR_SESSION_MAP_ID);
//	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
//
//	Long assessmentQuestionUid = new Long(request.getParameter(AssessmentConstants.PARAM_QUESTION_UID));
//	IAssessmentService service = getAssessmentService();
//	HttpSession ss = SessionManager.getSession();
//	// get back login user DTO
//	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
//
//	Long sessionId = (Long) sessionMap.get(AssessmentConstants.ATTR_TOOL_SESSION_ID);
//	service.setQuestionComplete(assessmentQuestionUid, new Long(user.getUserID().intValue()), sessionId);
//
//	SortedSet<AssessmentQuestion> questionList = (SortedSet<AssessmentQuestion>) sessionMap
//		.get(AssessmentConstants.ATTR_QUESTION_LIST);
//	SortedSet<AssessmentQuestion> assessmentQuestionList = questionList;
//	// set assessment question complete tag	
//	for (AssessmentQuestion question : assessmentQuestionList) {
//	    if (question.getUid().equals(assessmentQuestionUid)) {
//		question.setComplete(true);
//		break;
//	    }
//	}
//    }

}
