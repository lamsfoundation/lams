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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummaryUserDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
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
			return viewReflection(mapping, request);
		}

		if (param.equals("listRecords")) {
			return listRecords(mapping, request);
		}
		if (param.equals("changeView")) {
			return changeView(mapping, request);
		}

		return mapping.findForward(DacoConstants.ERROR);
	}

	protected ActionForward listRecords(ActionMapping mapping, HttpServletRequest request) {
		String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Long userUid = WebUtil.readLongParam(request, DacoConstants.USER_UID, true);
		Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);
		IDacoService service = getDacoService();
		sessionMap.put(DacoConstants.ATTR_MONITORING_SUMMARY, service.getMonitoringSummary(daco.getContentId(), userUid));
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);

		request.setAttribute(DacoConstants.USER_UID, userUid);
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	protected ActionForward summary(ActionMapping mapping, HttpServletRequest request) {
		// initial Session Map
		IDacoService service = getDacoService();
		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID, true);

		boolean newSession = sessionMapID == null || request.getSession().getAttribute(sessionMapID) == null;
		SessionMap sessionMap = null;
		if (newSession) {
			sessionMap = new SessionMap();
			sessionMapID = sessionMap.getSessionID();
			request.getSession().setAttribute(sessionMapID, sessionMap);
			sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
		}
		else {
			sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		}

		Long contentId = sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID) == null ? WebUtil.readLongParam(request,
				AttributeNames.PARAM_TOOL_CONTENT_ID) : (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
		Daco daco = service.getDacoByContentId(contentId);
		daco.toDTO();

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
			List<QuestionSummaryDTO> summaries = service.getQuestionSummaries(userUid);
			sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);

			Integer totalRecordCount = service.getGroupRecordCount(service.getUser(userUid).getSession().getSessionId());
			sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);
			monitoringSummaryList = service.getMonitoringSummary(contentId, userUid);
		}

		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		sessionMap.put(DacoConstants.USER_UID, userUid);
		sessionMap.put(DacoConstants.PAGE_EDITABLE, !daco.isContentInUse());
		sessionMap.put(DacoConstants.ATTR_MONITORING_SUMMARY, monitoringSummaryList);

		if (newSession) {
			sessionMap.put(DacoConstants.ATTR_DACO, daco);
			sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);
			sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, WebUtil.readStrParam(request,
					AttributeNames.PARAM_CONTENT_FOLDER_ID));

			if (daco.isNotifyTeachersOnLearnerEntry()) {
				//Since we don't know if the event exists, we just try to create it.
				service.getEventNotificationService().createEvent(DacoConstants.TOOL_SIGNATURE,
						DacoConstants.EVENT_NAME_NOTIFY_TEACHERS_ON_LEARNER_ENTRY, contentId,
						service.getLocalisedMessage("event.learnerentry.subject", null),
						service.getLocalisedMessage("event.learnerentry.body", null));

				//Now we subscribe the teacher
				HttpSession ss = SessionManager.getSession();
				UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
				service.getEventNotificationService().subscribe(DacoConstants.TOOL_SIGNATURE,
						DacoConstants.EVENT_NAME_NOTIFY_TEACHERS_ON_LEARNER_ENTRY, contentId, user.getUserID().longValue(),
						IEventNotificationService.DELIVERY_METHOD_MAIL, IEventNotificationService.PERIODICITY_SINGLE);
			}
		}
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	protected ActionForward viewReflection(ActionMapping mapping, HttpServletRequest request) {
		String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		Integer userId = WebUtil.readIntParam(request, DacoConstants.USER_ID);
		Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

		IDacoService service = getDacoService();
		DacoUser user = service.getUserByUserIdAndSessionId(userId.longValue(), sessionId);
		NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
				DacoConstants.TOOL_SIGNATURE, userId);

		MonitoringSummaryUserDTO userDTO = new MonitoringSummaryUserDTO(null, userId, user.getLastName() + " "
				+ user.getFirstName(), null);
		userDTO.setReflectionEntry(notebookEntry.getEntry());
		sessionMap.put(DacoConstants.ATTR_USER, userDTO);
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
		return mapping.findForward(DacoConstants.SUCCESS);
	}

	private IDacoService getDacoService() {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		return (IDacoService) wac.getBean(DacoConstants.DACO_SERVICE);
	}

	protected ActionForward changeView(ActionMapping mapping, HttpServletRequest request) {
		String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
		SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
		request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);

		String currentView = (String) sessionMap.get(DacoConstants.ATTR_LEARNING_VIEW);
		Long userUid = WebUtil.readLongParam(request, DacoConstants.USER_UID, true);
		request.setAttribute(DacoConstants.USER_UID, userUid);
		if (DacoConstants.LEARNING_VIEW_HORIZONTAL.equals(currentView)) {
			sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
		}
		else {
			sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_HORIZONTAL);
		}
		return mapping.findForward(DacoConstants.SUCCESS);
	}
}
