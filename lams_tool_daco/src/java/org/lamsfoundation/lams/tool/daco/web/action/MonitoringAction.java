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
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoSession;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
	public static Logger log = Logger.getLogger(MonitoringAction.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String param = mapping.getParameter();

		if (param.equals("summary")) {
			return summary(mapping, request);
		}
		if (param.equals("viewReflection")) {
			return viewReflection(mapping, form, request, response);
		}

		if (param.equals("listRecords")) {
			return listRecords(mapping, request);
		}

		return mapping.findForward(DacoConstants.ERROR);
	}

	protected ActionForward listRecords(ActionMapping mapping, HttpServletRequest request) {
		String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long userUid = WebUtil.readLongParam(request, DacoConstants.USER_UID, true);
		Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);
		IDacoService service = getDacoService();
		request.setAttribute(DacoConstants.ATTR_MONITORING_SUMMARY, service.getMonitoringSummary(daco.getContentId(), userUid));
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
		request.setAttribute(DacoConstants.USER_UID, userUid);
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	protected ActionForward summary(ActionMapping mapping, HttpServletRequest request) {
		// initial Session Map
		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID, true);

		boolean newSession = sessionMapID == null || request.getSession().getAttribute(sessionMapID) == null;
		SessionMap sessionMap = null;
		if (newSession) {
			sessionMap = new SessionMap();
			sessionMapID = sessionMap.getSessionID();
			request.getSession().setAttribute(sessionMapID, sessionMap);

		}
		else {
			sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		}

		IDacoService service = getDacoService();
		Long contentId = sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID) == null ? WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID) : (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
		Daco daco = service.getDacoByContentId(contentId);

		daco.toDTO();
		Map<Long, Set<ReflectDTO>> relectList = service.getReflectList(contentId);

		List<MonitoringSummarySessionDTO> monitoringSummaryList = service.getMonitoringSummary(contentId,
				DacoConstants.MONITORING_SUMMARY_MATCH_NONE);

		Long userUid = WebUtil.readLongParam(request, DacoConstants.USER_UID, true);
		if (userUid == null) {
			userUid = (Long) sessionMap.get(DacoConstants.USER_UID);
			request.setAttribute(DacoConstants.ATTR_MONITORING_CURRENT_TAB, 1);
		}
		else {
			request.setAttribute(DacoConstants.ATTR_MONITORING_CURRENT_TAB, 4);
		}

		if (userUid == null && !monitoringSummaryList.isEmpty() && !monitoringSummaryList.get(0).getUsers().isEmpty()) {
			userUid = monitoringSummaryList.get(0).getUsers().get(0).getUid();
		}
		if (userUid != null) {
			List<QuestionSummaryDTO> summaries = service.getQuestionSummaries(daco.getUid(), userUid);
			sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);
			Integer totalRecordCount = service.getTotalRecordCount(daco.getContentId());
			sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);
			monitoringSummaryList = service.getMonitoringSummary(contentId, userUid);
		}

		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		sessionMap.put(DacoConstants.USER_UID, userUid);
		sessionMap.put(DacoConstants.PAGE_EDITABLE, !daco.isContentInUse());
		sessionMap.put(DacoConstants.ATTR_REFLECT_LIST, relectList);
		sessionMap.put(DacoConstants.ATTR_MONITORING_SUMMARY, monitoringSummaryList);
		if (newSession) {
			sessionMap.put(DacoConstants.ATTR_DACO, daco);
			sessionMap.put(DacoConstants.TOOL_CONTENT_ID, contentId);
			sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, WebUtil.readStrParam(request,
					AttributeNames.PARAM_CONTENT_FOLDER_ID));
		}
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	protected ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		Long uid = WebUtil.readLongParam(request, DacoConstants.USER_UID);
		Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

		IDacoService service = getDacoService();
		DacoUser user = service.getUser(uid);
		NotebookEntry notebookEntry = service.getEntry(sessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
				DacoConstants.TOOL_SIGNATURE, user.getUserId().intValue());

		DacoSession session = service.getDacoSessionBySessionId(sessionID);

		ReflectDTO refDTO = new ReflectDTO(user);
		if (notebookEntry == null) {
			refDTO.setFinishReflection(false);
			refDTO.setReflect(null);
		}
		else {
			refDTO.setFinishReflection(true);
			refDTO.setReflect(notebookEntry.getEntry());
		}
		refDTO.setReflectInstrctions(session.getDaco().getReflectInstructions());

		request.setAttribute("userDTO", refDTO);
		return mapping.findForward("success");
	}

	private IDacoService getDacoService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		return (IDacoService) wac.getBean(DacoConstants.DACO_SERVICE);
	}
}
