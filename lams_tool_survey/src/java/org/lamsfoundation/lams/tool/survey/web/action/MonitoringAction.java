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
package org.lamsfoundation.lams.tool.survey.web.action;

import static org.lamsfoundation.lams.tool.survey.SurveyConstants.CHART_TYPE;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.ATTR_QUESTION;
import static org.lamsfoundation.lams.tool.survey.SurveyConstants.ATTR_ANSWER_LIST;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.util.SurveyUserComparator;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
	public static Logger log = Logger.getLogger(MonitoringAction.class);
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String param = mapping.getParameter();

		if (param.equals("summary")) {
			return summary(mapping, form, request, response);
		}

		if (param.equals("viewChartReport")) {
			return viewChartReport(mapping, form, request, response);
		}

		if (param.equals("listAnswers")) {
			return listAnswers(mapping, form, request, response);
		}

		if (param.equals("viewReflection")) {
			return viewReflection(mapping, form, request, response);
		}

		return mapping.findForward(SurveyConstants.ERROR);
	}


	/**
	 * Summary page action.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */

	private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		//initial Session Map 
		SessionMap sessionMap = new SessionMap();
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
		
		Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
		ISurveyService service = getSurveyService();
		
		SortedMap<SurveySession,List<SurveyQuestion>> summary = service.getSummary(contentId);
		Survey survey = service.getSurveyByContentId(contentId);
		
		Map<Long,Set<ReflectDTO> >relectList = service.getReflectList(contentId);
		//cache into sessionMap
		sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, summary);
		sessionMap.put(SurveyConstants.PAGE_EDITABLE, new Boolean(SurveyWebUtils.isSurveyEditable(survey)));
		sessionMap.put(SurveyConstants.ATTR_SURVEY, survey);
		sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);
		sessionMap.put(SurveyConstants.ATTR_REFLECT_LIST, relectList);
		
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	/**
	 * Display pie chart or bar chart for one question.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward viewChartReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		String type= WebUtil.readStrParam(request, CHART_TYPE);
		
		Long questionUid = WebUtil.readLongParam(request, SurveyConstants.ATTR_QUESTION_UID);
		ISurveyService service = getSurveyService();
		//get question
		SurveyQuestion question = service.getQuestion(questionUid);

		//set all attribute to request for show
		request.setAttribute( AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
		request.setAttribute( CHART_TYPE, type);
		request.setAttribute( ATTR_QUESTION, question);
		
		return mapping.findForward(SurveyConstants.SUCCESS);
	}



	private ActionForward listAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		Long questionUid = WebUtil.readLongParam(request, SurveyConstants.ATTR_QUESTION_UID);

		//get user list 
		ISurveyService service = getSurveyService();
		
		SortedMap<SurveyUser,SurveyQuestion> userAnswerMap = new TreeMap<SurveyUser, SurveyQuestion>(new SurveyUserComparator());
//		get all users with their answers whatever they answer or not
		List<SurveyUser> users = service.getSessionUsers(sessionId);
		for (SurveyUser user : users) {
			List<AnswerDTO> questionAnswers = service.getQuestionAnswers(sessionId, user.getUid());
			for (AnswerDTO questionAnswer : questionAnswers) {
				if(questionUid.equals(questionAnswer.getUid())){
					userAnswerMap.put(user,questionAnswer);
					break;
				}
			}
		}
		//set all attribute to request for show
		request.setAttribute( ATTR_ANSWER_LIST, userAnswerMap);
		
		return mapping.findForward(SurveyConstants.SUCCESS);
	}
	
	private ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		
		Long uid = WebUtil.readLongParam(request, SurveyConstants.ATTR_USER_UID); 
		Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
		
		ISurveyService service = getSurveyService();
		SurveyUser user = service.getUser(uid);
		NotebookEntry notebookEntry = service.getEntry(sessionID, 
				CoreNotebookConstants.NOTEBOOK_TOOL, 
				SurveyConstants.TOOL_SIGNATURE, user.getUserId().intValue());
		
		SurveySession session = service.getSurveySessionBySessionId(sessionID);
		
		ReflectDTO refDTO = new ReflectDTO(user);
		if(notebookEntry == null){
			refDTO.setFinishReflection(false);
			refDTO.setReflect(null);
		}else{
			refDTO.setFinishReflection(true);
			refDTO.setReflect(notebookEntry.getEntry());
		}
		refDTO.setReflectInstrctions(session.getSurvey().getReflectInstructions());
		
		request.setAttribute("userDTO", refDTO);
		return mapping.findForward("success");
	}	

	// *************************************************************************************
	// Private method
	// *************************************************************************************
	private ISurveyService getSurveyService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
				.getServletContext());
		return (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
	}	
}
