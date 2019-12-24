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

package org.lamsfoundation.lams.tool.taskList.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.taskList.TaskListConstants;
import org.lamsfoundation.lams.tool.taskList.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.taskList.dto.SessionDTO;
import org.lamsfoundation.lams.tool.taskList.dto.TaskListUserDTO;
import org.lamsfoundation.lams.tool.taskList.model.TaskList;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListSession;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;
import org.lamsfoundation.lams.tool.taskList.service.ITaskListService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    public static Logger log = Logger.getLogger(MonitoringController.class);
    private static String TOOL_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + TaskListConstants.TOOL_SIGNATURE + "/";

    @Autowired
    @Qualifier("lataskTaskListService")
    private ITaskListService taskListService;

    @RequestMapping("/summary")
    public String summary(HttpServletRequest request) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);

	TaskList taskList = taskListService.getTaskListByContentId(contentId);

	List<SessionDTO> sessionDtos = taskListService.getSummary(contentId);

	// cache into sessionMap
	sessionMap.put(TaskListConstants.ATTR_SESSION_DTOS, sessionDtos);
	sessionMap.put(TaskListConstants.ATTR_MONITOR_VERIFICATION_REQUIRED, taskList.isMonitorVerificationRequired());
	sessionMap.put(TaskListConstants.PAGE_EDITABLE, taskList.isContentInUse());
	sessionMap.put(TaskListConstants.ATTR_TASKLIST, taskList);
	sessionMap.put(TaskListConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	sessionMap.put(TaskListConstants.ATTR_IS_GROUPED_ACTIVITY, taskListService.isGroupedActivity(contentId));

	if (taskList.getSubmissionDeadline() != null) {
	    Date submissionDeadline = taskList.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    sessionMap.put(TaskListConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    sessionMap.put(TaskListConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	// Create reflectList if reflection is enabled.
	if (taskList.isReflectOnActivity()) {
	    List<ReflectDTO> reflectList = taskListService.getReflectList(taskList.getContentId());
	    // Add reflectList to sessionMap
	    sessionMap.put(TaskListConstants.ATTR_REFLECT_LIST, reflectList);
	}

	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/itemSummary")
    public String itemSummary(HttpServletRequest request) {

	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long itemUid = WebUtil.readLongParam(request, TaskListConstants.PARAM_ITEM_UID);

	TaskListItem item = taskListService.getTaskListItemByUid(itemUid);

	// create sessionList depending on whether the item was created by author or by learner
	List<SessionDTO> sessionDtos = new ArrayList<>();
	if (item.isCreateByAuthor()) {
	    List<TaskListSession> sessionList = taskListService.getSessionsByContentId(contentId);
	    for (TaskListSession session : sessionList) {
		SessionDTO sessionDto = new SessionDTO(session);
		sessionDtos.add(sessionDto);
	    }

	} else {
	    TaskListSession userSession = item.getCreateBy().getSession();
	    SessionDTO sessionDto = new SessionDTO(userSession);
	    sessionDtos.add(sessionDto);
	}

	request.setAttribute(TaskListConstants.ATTR_SESSION_DTOS, sessionDtos);
	request.setAttribute(TaskListConstants.ATTR_TASK_LIST_ITEM, item);

	return "pages/monitoring/itemsummary";
    }

    /**
     * Refreshes user list.
     */
    @RequestMapping("/getPagedUsers")
    @ResponseBody
    public String getPagedUsers(HttpServletRequest request, HttpServletResponse res) {

	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	TaskList tasklist = (TaskList) sessionMap.get(TaskListConstants.ATTR_TASKLIST);
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	List<SessionDTO> sessionDtos = (List<SessionDTO>) sessionMap.get(TaskListConstants.ATTR_SESSION_DTOS);

	//find according sessionDto
	SessionDTO sessionDto = null;
	for (SessionDTO sessionDtoIter : sessionDtos) {
	    if (sessionDtoIter.getSessionId().equals(sessionId)) {
		sessionDto = sessionDtoIter;
	    }
	}
	List<TaskListItem> items = sessionDto.getTaskListItems();

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, AttributeNames.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, AttributeNames.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, AttributeNames.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, AttributeNames.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	// Get the user list from the db
	Collection<TaskListUserDTO> userDtos = taskListService.getPagedUsersBySession(sessionId, page - 1, rowLimit,
		sortBy, sortOrder, searchString);
	int countSessionUsers = taskListService.getCountPagedUsersBySession(sessionId, searchString);

	int totalPages = new Double(
		Math.ceil(new Integer(countSessionUsers).doubleValue() / new Integer(rowLimit).doubleValue()))
			.intValue();

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	int i = 1;
	for (TaskListUserDTO userDto : userDtos) {

	    ArrayNode userData = JsonNodeFactory.instance.arrayNode();
	    userData.add(userDto.getUserId());
	    String fullName = HtmlUtils.htmlEscape(userDto.getFullName());
	    userData.add(fullName);

	    Set<Long> completedTaskUids = userDto.getCompletedTaskUids();
	    for (TaskListItem item : items) {
		String completionImage = completedTaskUids.contains(item.getUid()) ? "<i class=\"fa fa-check\"></i>"
			: "<i class=\"fa fa-minus\"></i>";
		userData.add(completionImage);
	    }

	    if (tasklist.isMonitorVerificationRequired()) {
		String label = HtmlUtils.htmlEscape(taskListService.getMessage("label.confirm"));

		String verificationStatus = userDto.isVerifiedByMonitor() ? "<i class=\"fa fa-check\"></i>"
			: "<a id='verif-" + userDto.getUserId()
				+ "' href='javascript:;' onclick='return setVerifiedByMonitor(this, "
				+ userDto.getUserId() + ");'>" + label + "</a>";
		userData.add(verificationStatus);
	    }

	    userData.add(userDto.getPortraitId());

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.put("cell", userData);

	    rows.add(userRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.set("rows", rows);
	res.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    /**
     * Refreshes user list.
     */
    @RequestMapping("/getPagedUsersByItem")
    @ResponseBody
    public String getPagedUsersByItem(HttpServletRequest request, HttpServletResponse response) {

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long itemUid = WebUtil.readLongParam(request, TaskListConstants.PARAM_ITEM_UID);

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, AttributeNames.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, AttributeNames.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, AttributeNames.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, AttributeNames.PARAM_SIDX, true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	// Get the user list from the db
	Collection<TaskListUserDTO> userDtos = taskListService.getPagedUsersBySessionAndItem(sessionId, itemUid,
		page - 1, rowLimit, sortBy, sortOrder, searchString);
	int countSessionUsers = taskListService.getCountPagedUsersBySession(sessionId, searchString);

	int totalPages = new Double(
		Math.ceil(new Integer(countSessionUsers).doubleValue() / new Integer(rowLimit).doubleValue()))
			.intValue();

	//date formatters
	DateFormat dateFormatter = new SimpleDateFormat("d-MMM-yyyy h:mm a");
	HttpSession ss = SessionManager.getSession();
	UserDTO monitorDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TimeZone monitorTimeZone = monitorDto.getTimeZone();

	//get all comments and attachments
	TaskListItem item = taskListService.getTaskListItemByUid(itemUid);
	Set<TaskListItemComment> itemComments = item.getComments();
	Set<TaskListItemAttachment> itemAttachments = item.getAttachments();
	String label = HtmlUtils.htmlEscape(taskListService.getMessage("label.download"));

	int i = 0;
	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	for (TaskListUserDTO userDto : userDtos) {

	    ArrayNode userData = JsonNodeFactory.instance.arrayNode();
	    String fullName = HtmlUtils.htmlEscape(userDto.getFullName());
	    userData.add(fullName);

	    String completionImage = userDto.isCompleted() ? "<i class=\"fa fa-check\"></i>"
		    : "<i class=\"fa fa-minus\"></i>";
	    userData.add(completionImage);

	    String accessDate = (userDto.getAccessDate() == null) ? ""
		    : dateFormatter
			    .format(DateUtil.convertToTimeZoneFromDefault(monitorTimeZone, userDto.getAccessDate()));
	    userData.add(accessDate);

	    // fill up with comments and attachments made by this user
	    if (item.isCommentsAllowed() || item.isFilesAllowed()) {
		String commentsFiles = "<ul>";

		ArrayList<String> userComments = new ArrayList<>();
		for (TaskListItemComment comment : itemComments) {
		    if (userDto.getUserId().equals(comment.getCreateBy().getUserId())) {
			userComments.add(comment.getComment());
		    }
		}
		if (!userComments.isEmpty()) {
		    commentsFiles += "<li>";
		    for (String userComment : userComments) {
			commentsFiles += HtmlUtils.htmlEscape(userComment);
		    }
		    commentsFiles += "</li>";
		}

		ArrayList<TaskListItemAttachment> userAttachments = new ArrayList<>();
		for (TaskListItemAttachment attachment : itemAttachments) {
		    if (userDto.getUserId().equals(attachment.getCreateBy().getUserId())) {
			userAttachments.add(attachment);
		    }
		}
		if (!userAttachments.isEmpty()) {
		    commentsFiles += "<li>";
		    for (TaskListItemAttachment userAttachment : userAttachments) {
			commentsFiles += HtmlUtils.htmlEscape(userAttachment.getFileName()) + " ";
			commentsFiles += "<a href='" + TOOL_URL + "/download/?uuid=" + userAttachment.getFileUuid()
				+ "&versionID=" + userAttachment.getFileVersionId() + "&preferDownload=true'>" + label
				+ "</a>";
		    }
		    commentsFiles += "</li>";
		}

		commentsFiles += "</ul>";

//
//		<c:forEach var="attachment" items="${visitLogSummary.attachments}">
//			<li>
//				<c:out value="${attachment.fileName}" />
//
//				<c:set var="downloadURL">
//					<html:rewrite page="/download/?uuid=${attachment.fileUuid}&versionID=${attachment.fileVersionId}&preferDownload=true" />
//				</c:set>
//				<html:link href="${downloadURL}">
//					<fmt:message key="label.download" />
//				</html:link>
//
//			</li>
//		</c:forEach>
		userData.add(commentsFiles);
	    }

	    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
	    userRow.put("id", i++);
	    userRow.set("cell", userData);

	    rows.add(userRow);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.set("rows", rows);
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    /**
     * Mark taskList user as verified.
     */
    @RequestMapping("/setVerifiedByMonitor")
    public String setVerifiedByMonitor(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long userUid = WebUtil.readLongParam(request, TaskListConstants.ATTR_USER_UID);
	TaskListUser user = taskListService.getUser(userUid);
	user.setVerifiedByMonitor(true);
	taskListService.createUser(user);

	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	out.write(userUid.toString());
	out.flush();
	out.close();
	return "pages/monitoring/monitoring";
    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	TaskList taskList = taskListService.getTaskListByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, TaskListConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	taskList.setSubmissionDeadline(tzSubmissionDeadline);
	taskListService.saveOrUpdateTaskList(taskList);
	
	return formattedDate;
    }

}
