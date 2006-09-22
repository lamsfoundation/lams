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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.survey.web.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
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
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.service.SurveyApplicationException;
import org.lamsfoundation.lams.tool.survey.service.UploadSurveyFileException;
import org.lamsfoundation.lams.tool.survey.util.IntegerComparator;
import org.lamsfoundation.lams.tool.survey.util.QuestionsComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.tool.survey.web.form.AnswerForm;
import org.lamsfoundation.lams.tool.survey.web.form.ReflectionForm;
import org.lamsfoundation.lams.tool.survey.web.form.QuestionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;
/**
 * 
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class LearningAction extends Action {

	private static Logger log = Logger.getLogger(LearningAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String param = mapping.getParameter();
		//-----------------------Survey Learner function ---------------------------
		if(param.equals("start")){
			return start(mapping, form, request, response);
		}
		if(param.equals("previousQuestion")){
			return previousQuestion(mapping, form, request, response);
		}
		if(param.equals("nextQuestion")){
			return nextQuestion(mapping, form, request, response);
		}
		if(param.equals("doSurvey")){
			return doSurvey(mapping, form, request, response);
		}
		if(param.equals("finish")){
			return finish(mapping, form, request, response);
		}
        
		//================ Reflection =======================
		if (param.equals("newReflection")) {
			return newReflection(mapping, form, request, response);
		}
		if (param.equals("submitReflection")) {
			return submitReflection(mapping, form, request, response);
		}
		
		return  mapping.findForward(SurveyConstants.ERROR);
	}

	

	/**
	 * Read survey data from database and put them into HttpSession. It will redirect to init.do directly after this
	 * method run successfully. 
	 *  
	 * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
	 * 
	 */
	private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		AnswerForm answerForm = (AnswerForm) form;
		//initial Session Map 
		SessionMap<String,Object> sessionMap = new SessionMap<String,Object>();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		answerForm.setSessionMapID(sessionMap.getSessionID());
		
		//save toolContentID into HTTPSession
		ToolAccessMode mode = WebUtil.readToolAccessModeParam(request,AttributeNames.PARAM_MODE, true);
		Long sessionId =  new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));
		
		
//		get back the survey and question list and display them on page
		ISurveyService service = getSurveyService();
		SurveyUser surveyUser = getCurrentUser(service,sessionId);

		List<SurveyQuestion> questions = null;
		Survey survey;
		questions = service.getQuestionAnswer(sessionId,surveyUser.getUid());
		survey = service.getSurveyBySessionId(sessionId);

		//check whehter finish lock is on/off
		boolean lock = survey.getLockWhenFinished() && surveyUser.isSessionFinished();
		
		//basic information
		sessionMap.put(SurveyConstants.ATTR_TITLE,survey.getTitle());
		sessionMap.put(SurveyConstants.ATTR_SURVEY_INSTRUCTION,survey.getInstructions());
		sessionMap.put(SurveyConstants.ATTR_FINISH_LOCK,lock);
		sessionMap.put(SurveyConstants.ATTR_SHOW_ON_ONE_PAGE,survey.isShowOnePage());
		
		sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID,sessionId);
		sessionMap.put(AttributeNames.ATTR_MODE,mode);
		//reflection information
		sessionMap.put(SurveyConstants.ATTR_REFLECTION_ON,survey.isReflectOnActivity());
		sessionMap.put(SurveyConstants.ATTR_REFLECTION_INSTRUCTION,survey.getReflectInstructions());
		
		//add define later support
		if(survey.isDefineLater()){
			return mapping.findForward("defineLater");
		}
		
		//set contentInUse flag to true!
		survey.setContentInUse(true);
		survey.setDefineLater(false);
		service.saveOrUpdateSurvey(survey);
		
		//add run offline support
		if(survey.getRunOffline()){
			sessionMap.put(SurveyConstants.PARAM_RUN_OFFLINE, true);
			return mapping.findForward("runOffline");
		}else
			sessionMap.put(SurveyConstants.PARAM_RUN_OFFLINE, false);
				
		//init survey item list
		SortedMap<Integer, SurveyQuestion> surveyItemList = getQuestionList(sessionMap);
		surveyItemList.clear();
		if(questions != null){
			//remove hidden items.
			for(SurveyQuestion question : questions){
				surveyItemList.put(question.getSequenceId(),question);
			}
		}
		if(survey.isShowOnePage()){
			answerForm.setQuestionSeqID(null);
		}else{
			if(surveyItemList.size() > 0){
				answerForm.setQuestionSeqID(surveyItemList.firstKey());
			}
		}
		if(surveyItemList.size() < 2)
			answerForm.setPosition(SurveyConstants.POSITION_ONLY_ONE);
		else
			answerForm.setPosition(SurveyConstants.POSITION_FIRST);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}

	private ActionForward nextQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		AnswerForm answerForm = (AnswerForm) form;
		Integer questionSeqID = answerForm.getQuestionSeqID();
		String sessionMapID = answerForm.getSessionMapID();
		
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		SortedMap<Integer, SurveyQuestion> surveyItemMap = getQuestionList(sessionMap);
		
		ActionErrors errors = getAnswer(request, surveyItemMap.get(questionSeqID));
		if(!errors.isEmpty()){
			return mapping.getInputForward();
		}
		
		//go to next question
		boolean next = false;
		for(Map.Entry<Integer, SurveyQuestion> entry: surveyItemMap.entrySet()){
			if(entry.getKey().equals(questionSeqID)){
				next = true;
				//failure tolerance: if arrive last one
				questionSeqID = entry.getKey();
				continue;
			}
			if(next){
				questionSeqID = entry.getKey();
				break;
			}
		}
		//failure tolerance
		if(questionSeqID.equals(surveyItemMap.lastKey()))
			answerForm.setPosition(SurveyConstants.POSITION_LAST);
		else
			answerForm.setPosition(SurveyConstants.POSITION_INSIDE);
		answerForm.setQuestionSeqID(questionSeqID);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}


	private ActionForward previousQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		AnswerForm answerForm = (AnswerForm) form;
		Integer questionSeqID = answerForm.getQuestionSeqID();
		String sessionMapID = answerForm.getSessionMapID();
		
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		SortedMap<Integer, SurveyQuestion> surveyItemMap = getQuestionList(sessionMap);
		
		ActionErrors errors = getAnswer(request, surveyItemMap.get(questionSeqID));
		if(!errors.isEmpty()){
			return mapping.getInputForward();
		}
		
		SortedMap<Integer, SurveyQuestion> subMap = surveyItemMap.headMap(questionSeqID);
		if(subMap.isEmpty())
			questionSeqID = surveyItemMap.firstKey();
		else
			questionSeqID = subMap.lastKey();
		if(questionSeqID.equals(surveyItemMap.firstKey())){
			answerForm.setPosition(SurveyConstants.POSITION_FIRST);
		}else{
			answerForm.setPosition(SurveyConstants.POSITION_INSIDE);
		}
		answerForm.setQuestionSeqID(questionSeqID);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}

	private ActionForward doSurvey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		AnswerForm answerForm = (AnswerForm) form;
		Integer questionSeqID = answerForm.getQuestionSeqID();
		String sessionMapID = answerForm.getSessionMapID();
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		//validate
		SortedMap<Integer, SurveyQuestion> surveyItemMap = getQuestionList(sessionMap);
		Collection<SurveyQuestion> surveyItemList = surveyItemMap.values();
		
		ActionErrors errors;
		if(questionSeqID == null || questionSeqID.equals(0))
			errors = getAnswers(request);
		else
			errors = getAnswer(request, surveyItemMap.get(questionSeqID));
		if(!errors.isEmpty()){
			return mapping.getInputForward();
		}
		
		List<SurveyAnswer> answerList = new ArrayList<SurveyAnswer>();
		for(SurveyQuestion question : surveyItemList){
			if(question.getAnswer() != null)
				answerList.add(question.getAnswer());
		}
		
		ISurveyService service = getSurveyService();
		service.updateAnswerList(answerList);
		return mapping.findForward(SurveyConstants.SUCCESS);
	}



	/**
	 * Finish learning session. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		//get back SessionMap
		String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		
		//get mode and ToolSessionID from sessionMAP
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		ISurveyService service = getSurveyService();
		// get sessionId from HttpServletRequest
		String nextActivityUrl = null ;
		try {
			HttpSession ss = SessionManager.getSession();
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			Long userID = new Long(user.getUserID().longValue());
			
			nextActivityUrl = service.finishToolSession(sessionId,userID);
			request.setAttribute(SurveyConstants.ATTR_NEXT_ACTIVITY_URL,nextActivityUrl);
		} catch (SurveyApplicationException e) {
			log.error("Failed get next activity url:" + e.getMessage());
		}
		
		return  mapping.findForward(SurveyConstants.SUCCESS);
	}

	
	
	/**
	 * Display empty reflection form.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		//get session value
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);

		ReflectionForm refForm = (ReflectionForm) form;
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		
		refForm.setUserID(user.getUserID());
		refForm.setSessionMapID(sessionMapID);
		
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Submit reflection form input database.
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
		
		String sessionMapID = WebUtil.readStrParam(request, SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		ISurveyService service = getSurveyService();
		service.createNotebookEntry(sessionId, 
				CoreNotebookConstants.NOTEBOOK_TOOL,
				SurveyConstants.TOOL_SIGNATURE, 
				userId,
				refForm.getEntryText());

		return finish(mapping, form, request, response);
	}
	

	//*************************************************************************************
	// Private method 
	//*************************************************************************************
	/**
	 * Get answer by special question.
	 */
	private ActionErrors getAnswer(HttpServletRequest request,SurveyQuestion question) {
		ActionErrors errors = new ActionErrors();
		//get sessionMap
		String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		SurveyAnswer answer = getAnswerFromPage(request,question, sessionID);
		question.setAnswer(answer);
		//for mandatory questions, answer can not be null.
		if(!question.isOptional() && answer == null){
			errors.add(SurveyConstants.ERROR_MSG_KEY+ question.getUid(),new ActionMessage(SurveyConstants.ERROR_MSG_MANDATORY_QUESTION));
			addErrors(request, errors);
		}

		return errors;
	}
	/**
	 * Get all answer for all questions in this page
	 * @param request
	 * @return
	 */
	private ActionErrors getAnswers(HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		//get sessionMap
		String sessionMapID = request.getParameter(SurveyConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long sessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
		Collection<SurveyQuestion> questionList = getQuestionList(sessionMap).values();
		
		for(SurveyQuestion question :questionList){
			SurveyAnswer answer = getAnswerFromPage(request,question, sessionID);
			question.setAnswer(answer);
			//for mandatory questions, answer can not be null.
			if(!question.isOptional() && answer == null){
				errors.add(SurveyConstants.ERROR_MSG_KEY + question.getUid(),new ActionMessage(SurveyConstants.ERROR_MSG_MANDATORY_QUESTION));
				addErrors(request, errors);
			}
		}
		return errors;
	}


	private SurveyAnswer getAnswerFromPage(HttpServletRequest request, SurveyQuestion question, Long sessionID) {
		
		String[] choiceList = request.getParameterValues(SurveyConstants.PREFIX_QUESTION_CHOICE+question.getUid());
		String textEntry = request.getParameter(SurveyConstants.PREFIX_QUESTION_TEXT+question.getUid());
		if(choiceList == null && StringUtils.isBlank(textEntry))
			return null;
		
		
		SurveyAnswer answer = question.getAnswer();
		if(answer == null)
			answer = new SurveyAnswer();
		answer.setAnswerChoices(SurveyWebUtils.getChoicesStr(choiceList));
		answer.setChoices(choiceList);
		
		answer.setAnswerText(textEntry);
		
		ISurveyService service = getSurveyService();
		answer.setUser(getCurrentUser(service, sessionID));
		answer.setUpdateDate(new Timestamp(new Date().getTime()));
		answer.setSurveyQuestion(question);
		return answer;
	}



	

	private ISurveyService getSurveyService() {
	      WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
	      return (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
	}
	/**
	 * List save current survey items.
	 * @param request
	 * @return
	 */
	private SortedMap<Integer, SurveyQuestion> getQuestionList(SessionMap sessionMap) {
		SortedMap<Integer, SurveyQuestion> list = (SortedMap<Integer, SurveyQuestion>) sessionMap.get(SurveyConstants.ATTR_QUESTION_LIST);
		if(list == null){
			list = new TreeMap<Integer,SurveyQuestion>(new IntegerComparator());
			sessionMap.put(SurveyConstants.ATTR_QUESTION_LIST,list);
		}
		return list;
	}	

	private SurveyUser getCurrentUser(ISurveyService service, Long sessionId) {
		//try to get form system session
		HttpSession ss = SessionManager.getSession();
		//get back login user DTO
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		SurveyUser surveyUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),sessionId);
		
		if(surveyUser == null){
			SurveySession session = service.getSurveySessionBySessionId(sessionId);
			surveyUser = new SurveyUser(user,session);
			service.createUser(surveyUser);
		}
		return surveyUser;
	}

}
