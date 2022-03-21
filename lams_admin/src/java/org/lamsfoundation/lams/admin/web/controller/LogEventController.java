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
package org.lamsfoundation.lams.admin.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.LogEventType;
import org.lamsfoundation.lams.logevent.dto.LogEventTypeDTO;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Report on events in the log event table. Used for auditing.
 */
@Controller
@RequestMapping("/logevent")
public class LogEventController {
    private static SimpleDateFormat START_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd");

    @Autowired
    private ILogEventService logEventService;

    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/start")
    public String unspecified(HttpServletRequest request) throws Exception {

	// check permission
	if (!request.isUserInRole(Role.APPADMIN)) {
	    request.setAttribute("errorName", "EventLogAdmin");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	// get the log type data and return display for user selection. Also get the start and stop dates from the log.
	// TODO check conversion the dates to the user's timezone
	List<LogEventType> types = logEventService.getEventTypes();
	List<LogEventTypeDTO> convertedTypes = new ArrayList<>(types.size());
	for (LogEventType type : types) {
	    convertedTypes.add(new LogEventTypeDTO(type, messageService.getMessage(type.getDescriptionI18NKey()),
		    messageService.getMessage(type.getAreaI18NKey())));
	}
	request.setAttribute("eventLogTypes", convertedTypes);

	// jsp page expects date of the first audit log entry as YYYY-DD-MM.
	Date oldestDate = logEventService.getOldestEventDate();
//	oldestDate = DateUtil.convertToTimeZoneFromDefault(userTimeZone, oldestDate);
	request.setAttribute("startDate", START_DATE_FORMAT.format(oldestDate != null ? oldestDate : new Date()));
	return "logevent";
    }

    /**
     * The initial method for monitoring. List all users according to given Content ID.
     */
    @RequestMapping("/getEventLog")
    @ResponseBody
    public String getEventLog(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	// check permission
	if (!request.isUserInRole(Role.APPADMIN)) {
	    request.setAttribute("errorName", "EventLogAdmin");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSortDate = WebUtil.readIntParam(request, "column[0]", true);
	Integer isSortUser = WebUtil.readIntParam(request, "column[2]", true);
	Integer isSortTarget = WebUtil.readIntParam(request, "column[3]", true);
	String searchUser = request.getParameter("fcol[2]");
	String searchTarget = request.getParameter("fcol[3]");
	String searchRemarks = request.getParameter("fcol[4]");

	int sorting = ILogEventService.SORT_BY_DATE_DESC;
	if (isSortDate != null) {
	    sorting = isSortDate.equals(1) ? ILogEventService.SORT_BY_DATE_DESC : ILogEventService.SORT_BY_DATE_ASC;
	} else if (isSortUser != null) {
	    sorting = isSortUser.equals(1) ? ILogEventService.SORT_BY_USER_DESC : ILogEventService.SORT_BY_USER_ASC;
	} else if (isSortTarget != null) {
	    sorting = isSortTarget.equals(1) ? ILogEventService.SORT_BY_TARGET_DESC
		    : ILogEventService.SORT_BY_TARGET_ASC;
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

	String area = WebUtil.readStrParam(request, "area", true);
	Integer typeId = WebUtil.readIntParam(request, "typeId", true);
	List<Object[]> events = logEventService.getEventsForTablesorter(page, size, sorting, searchUser, searchTarget,
		searchRemarks, startDate, endDate, area, typeId);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", logEventService.countEventsWithRestrictions(searchUser, searchTarget,
		searchRemarks, startDate, endDate, area, typeId));

	for (Object[] eventDetails : events) {
	    if (eventDetails.length > 0) {
		LogEvent event = (LogEvent) eventDetails[0];
		ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

		responseRow.put("dateOccurred",
			event.getOccurredDateTime() != null
				? DateUtil.convertToStringForJSON(event.getOccurredDateTime(), request.getLocale())
				: "");
		responseRow.put("typeId", event.getLogEventTypeId());
		responseRow.put("description", event.getDescription());
		if (event.getLessonId() != null) {
		    responseRow.put("lessonId", event.getLessonId());
		}
		if (event.getActivityId() != null) {
		    responseRow.put("activityId", event.getActivityId());
		}

		User user = event.getUser();
		if (user != null) {
		    responseRow.put("userPortraitId",
			    user.getPortraitUuid() == null ? null : user.getPortraitUuid().toString());
		    responseRow.put("userId", user.getUserId());
		    responseRow.put("userName", user.getLogin());
		}
		User targetUser = event.getTargetUser();
		if (targetUser != null) {
		    responseRow.put("targetUserPortraitId",
			    targetUser.getPortraitUuid() == null ? null : targetUser.getPortraitUuid().toString());
		    responseRow.put("targetUserId", targetUser.getUserId());
		    responseRow.put("targetUserName", targetUser.getLogin());
		}
		if (eventDetails.length > 1 && eventDetails[1] != null) {
		    responseRow.put("lessonName", JsonUtil.toString(eventDetails[1]));
		}
		if (eventDetails.length > 2 && eventDetails[2] != null) {
		    responseRow.put("activityName", JsonUtil.toString(eventDetails[2]));
		}
		rows.add(responseRow);
	    }
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	return responsedata.toString();
    }

}
