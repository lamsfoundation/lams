/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.admin.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.LogEventType;
import org.lamsfoundation.lams.logevent.dto.LogEventTypeDTO;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Report on events in the log event table. Used for auditing.
 */
public class LogEventAction extends LamsDispatchAction {

    private static ILogEventService logEventService;
    private MessageService messageService;
    private static SimpleDateFormat START_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD");

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "EventLogAdmin");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	// user timezone
	HttpSession ss = SessionManager.getSession();
	org.lamsfoundation.lams.usermanagement.dto.UserDTO user = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		.getAttribute(AttributeNames.USER);
	TimeZone userTimeZone = user.getTimeZone();

	logEventService = getLogEventService();

	if (messageService == null) {
	    messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	}

	// get the log type data and return display for user selection. Also get the start and stop dates from the log.
	// TODO check conversion the dates to the user's timezone
	List<LogEventType> types = logEventService.getEventTypes();
	List<LogEventTypeDTO> convertedTypes = new ArrayList<LogEventTypeDTO>(types.size());
	for (LogEventType type : types) {
	    convertedTypes.add(new LogEventTypeDTO(type, messageService.getMessage(type.getDescriptionI18NKey()),
		    messageService.getMessage(type.getAreaI18NKey())));
	}
	request.setAttribute("eventLogTypes", convertedTypes);
	
	// jsp page expects date of the first audit log entry as YYYY-DD-MM. 
	Date oldestDate = logEventService.getOldestEventDate();
//	oldestDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, oldestDate);
	request.setAttribute("startDate", START_DATE_FORMAT.format(oldestDate != null ? oldestDate : new Date()) );
	return mapping.findForward("success");
    }

    /**
     * The initial method for monitoring. List all users according to given Content ID.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return 
     * @return
     * @throws JSONException 
     * @throws ServletException 
     * @throws IOException 
     */
    public ActionForward getEventLog(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, ServletException, IOException {
	
	// check permission
	if (!request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("errorName", "EventLogAdmin");
	    request.setAttribute("errorMessage", AdminServiceProxy.getMessageService(getServlet().getServletContext())
		    .getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	logEventService = getLogEventService();

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = ILogEventService.SORT_BY_DATE_ASC;
	if ((isSort1 != null) && isSort1.equals(1)) {
	    sorting =  ILogEventService.SORT_BY_DATE_DESC;
	}
	
	Long dateParameter = WebUtil.readLongParam(request, "startDate", true);
	Date startDate = null;
	if (dateParameter != null) {
	    startDate = new Date(dateParameter);
	    // TODO if using time zones then convert to server timezone
//	    HttpSession ss = SessionManager.getSession();
//	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
//		    .getAttribute(AttributeNames.USER);
//	    TimeZone teacherTimeZone = teacher.getTimeZone();
//	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}

	dateParameter = WebUtil.readLongParam(request, "endDate", true);
	Date endDate = null;
	if (dateParameter != null) {
	    endDate = new Date(dateParameter);
	}
	
	String area = WebUtil.readStrParam(request,  "area", true);
	Integer typeId = WebUtil.readIntParam(request,  "typeId", true);
	List<Object[]> events = logEventService.getEventsForTablesorter(page, size, sorting, null, startDate, endDate, area, typeId);
	    
	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", logEventService.countEventsWithRestrictions(null, startDate, endDate, area, typeId));

	for (Object[] eventDetails : events) {
	    if (eventDetails.length > 0) {
		LogEvent event = (LogEvent) eventDetails[0];
		JSONObject responseRow = new JSONObject();

		responseRow.put("dateOccurred", event.getOccurredDateTime());
		responseRow.put("typeId", event.getLogEventTypeId());
		responseRow.put("description", event.getDescription());
		User user = event.getUser();
		if (user != null) {
		    responseRow.put("userPortraitId", user.getPortraitUuid());
		    responseRow.put("userId", user.getUserId());
		    responseRow.put("userName", user.getLogin());
		}
		if ( eventDetails.length > 1 && eventDetails[1] != null ) {
		    responseRow.put("lesson", eventDetails[1] );
		}
		rows.put(responseRow);
	    }
	}
	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    private ILogEventService getLogEventService() throws ServletException {
	if (logEventService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    logEventService = (ILogEventService) ctx.getBean("logEventService");
	}
	return logEventService;
    }

}
