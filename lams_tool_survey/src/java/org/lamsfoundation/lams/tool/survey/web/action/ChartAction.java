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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.survey.web.action;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Display chart image by request.
 * 
 * @author Steve.Ni
 */
public class ChartAction extends Action {

    private static Logger logger = Logger.getLogger(ChartAction.class);

    private static ISurveyService surveyService;
    private MessageResources resource;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	resource = getResources(request);

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long questionUid = WebUtil.readLongParam(request, SurveyConstants.ATTR_QUESTION_UID);
	Long excludeUserId = WebUtil.readLongParam(request, SurveyConstants.ATTR_USER_ID, true);

	// if excludeUserId received exclude this user's answers
	AnswerDTO answer = (excludeUserId == null) ? getSurveyService().getQuestionResponse(sessionId, questionUid)
		: getSurveyService().getQuestionResponse(sessionId, questionUid, excludeUserId);
	if (answer.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    ChartAction.logger.error("Error question type : Text entry can not generate chart.");
	    response.getWriter().print(resource.getMessage(SurveyConstants.ERROR_MSG_CHART_ERROR));
	    return null;
	}

	JSONObject responseJSON = new JSONObject();
	Set<SurveyOption> options = answer.getOptions();
	try {
	    for (SurveyOption option : options) {
		JSONObject nomination = new JSONObject();
		// nominations' names and values go separately
		nomination.put("name", option.getDescription());
		nomination.put("value", (Number) option.getResponse());
		responseJSON.append("data", nomination);
	    }

	    if (answer.isAppendText()) {
		JSONObject nomination = new JSONObject();
		nomination.put("name", resource.getMessage(SurveyConstants.MSG_OPEN_RESPONSE));
		nomination.put("value", (Number) answer.getOpenResponse());
		responseJSON.append("data", nomination);
	    }
	} catch (JSONException e) {
	    ChartAction.logger.error("Error while generating pie chart for Survey Tool: " + sessionId);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private ISurveyService getSurveyService() {
	if (ChartAction.surveyService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    ChartAction.surveyService = (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
	}
	return ChartAction.surveyService;
    }
}