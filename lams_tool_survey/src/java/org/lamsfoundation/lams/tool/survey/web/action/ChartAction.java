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
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

	// if excludeUserId received exclude this user's answers
	AnswerDTO answer = getSurveyService().getQuestionResponse(sessionId, questionUid);
	if (answer.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
	    ChartAction.logger.error("Error question type : Text entry can not generate chart.");
	    response.getWriter().print(resource.getMessage(SurveyConstants.ERROR_MSG_CHART_ERROR));
	    return null;
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	Set<SurveyOption> options = answer.getOptions();
	for (SurveyOption option : options) {
	    ObjectNode nomination = JsonNodeFactory.instance.objectNode();
	    // nominations' names and values go separately
	    nomination.put("name", option.getDescription());
	    nomination.put("value", (Double) option.getResponse());
	    responseJSON.withArray("data").add(nomination);
	}

	if (answer.isAppendText()) {
	    ObjectNode nomination = JsonNodeFactory.instance.objectNode();
	    nomination.put("name", resource.getMessage(SurveyConstants.MSG_OPEN_RESPONSE));
	    nomination.put("value", (Double) answer.getOpenResponse());
	    responseJSON.withArray("data").add(nomination);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private ISurveyService getSurveyService() {
	if (ChartAction.surveyService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    ChartAction.surveyService = (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
	}
	return ChartAction.surveyService;
    }
}