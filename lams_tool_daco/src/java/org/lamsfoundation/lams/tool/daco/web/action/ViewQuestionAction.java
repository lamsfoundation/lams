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
package org.lamsfoundation.lams.tool.daco.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.tool.daco.util.DacoQuestionComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ViewQuestionAction extends Action {

	private static final Logger log = Logger.getLogger(ViewQuestionAction.class);
	private static final String DEFUALT_PROTOCOL_REFIX = "http://";
	private static final String ALLOW_PROTOCOL_REFIX = new String("[http://|https://|ftp://|nntp://]");

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String param = mapping.getParameter();
		// -----------------------Display Learning Object function
		// ---------------------------
		if (param.equals("reviewQuestion")) {
			return reviewQuestion(mapping, form, request, response);
		}
		// for preview top frame html page use:
		if (param.equals("nextInstruction")) {
			return nextInstruction(mapping, form, request, response);
		}
		return mapping.findForward(DacoConstants.ERROR);
	}

	/**
	 * Return next instrucion to page. It need four input parameters, mode, questionIndex or questionUid, and insIndex.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward nextInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String mode = request.getParameter(AttributeNames.ATTR_MODE);

		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sesionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

		DacoQuestion question = getDacoQuestion(request, sesionMap, mode);
		if (question == null) {
			return mapping.findForward(DacoConstants.ERROR);
		}

		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	/**
	 * Display main frame to display instrcution and question content.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward reviewQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		String mode = request.getParameter(AttributeNames.ATTR_MODE);

		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

		DacoQuestion question = getDacoQuestion(request, sessionMap, mode);

		String idStr = request.getParameter(DacoConstants.ATTR_TOOL_SESSION_ID);
		Long sessionId = NumberUtils.createLong(idStr);
		// mark this question access flag if it is learner
		if (ToolAccessMode.LEARNER.toString().equals(mode)) {
			IDacoService service = getDacoService();
			HttpSession ss = SessionManager.getSession();
			// get back login user DTO
			UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
			service.setQuestionAccess(question.getUid(), new Long(user.getUserID().intValue()), sessionId);
		}

		if (question == null) {
			return mapping.findForward(DacoConstants.ERROR);
		}
		// set url to content frame
		request.setAttribute(DacoConstants.ATTR_QUESTION_REVIEW_URL, getReviewUrl(question, sessionMapID));

		// these attribute will be use to instruction navigator page
		request.setAttribute(AttributeNames.ATTR_MODE, mode);
		int questionIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_QUESTION_INDEX));
		request.setAttribute(DacoConstants.PARAM_QUESTION_INDEX, questionIndex);
		Long questionUid = NumberUtils.createLong(request.getParameter(DacoConstants.PARAM_DACO_QUESTION_UID));
		request.setAttribute(DacoConstants.PARAM_DACO_QUESTION_UID, questionUid);
		request.setAttribute(DacoConstants.ATTR_TOOL_SESSION_ID, sessionId);
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);

		return mapping.findForward(DacoConstants.SUCCESS);

	}

	// *************************************************************************************
	// Private method
	// *************************************************************************************
	/**
	 * Return question according to ToolAccessMode.
	 * 
	 * @param request
	 * @param sessionMap
	 * @param mode
	 * @return
	 */
	private DacoQuestion getDacoQuestion(HttpServletRequest request, SessionMap sessionMap, String mode) {
		DacoQuestion question = null;
		if (DacoConstants.MODE_AUTHOR_SESSION.equals(mode)) {
			int questionIndex = NumberUtils.stringToInt(request.getParameter(DacoConstants.PARAM_QUESTION_INDEX), 0);
			// authoring: does not save question yet, so only has QuestionList
			// from session and identity by Index
			List<DacoQuestion> dacoList = new ArrayList<DacoQuestion>(getDacoQuestionList(sessionMap));
			question = dacoList.get(questionIndex);
		}
		else {
			Long questionUid = NumberUtils.createLong(request.getParameter(DacoConstants.PARAM_DACO_QUESTION_UID));
			// get back the daco and question list and display them on page
			IDacoService service = getDacoService();
			question = service.getDacoQuestionByUid(questionUid);
		}
		return question;
	}

	private IDacoService getDacoService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		return (IDacoService) wac.getBean(DacoConstants.DACO_SERVICE);
	}

	private Object getReviewUrl(DacoQuestion question, String sessionMapID) {
		return "";
	}

	/**
	 * List save current daco questions.
	 * 
	 * @param request
	 * @return
	 */
	private SortedSet<DacoQuestion> getDacoQuestionList(SessionMap sessionMap) {
		SortedSet<DacoQuestion> list = (SortedSet) sessionMap.get(DacoConstants.ATTR_QUESTION_LIST);
		if (list == null) {
			list = new TreeSet<DacoQuestion>(new DacoQuestionComparator());
			sessionMap.put(DacoConstants.ATTR_QUESTION_LIST, list);
		}
		return list;
	}

}
