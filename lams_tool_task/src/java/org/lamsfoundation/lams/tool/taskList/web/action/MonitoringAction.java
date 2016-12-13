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


package org.lamsfoundation.lams.tool.taskList.web.action;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);
    private static String TOOL_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + TaskListConstants.TOOL_SIGNATURE + "/";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	String param = mapping.getParameter();

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}
	if (param.equals("itemSummary")) {
	    return itemSummary(mapping, form, request, response);
	}
	if (param.equals("getPagedUsers")) {
	    return getPagedUsers(mapping, form, request, response);
	}
	if (param.equals("getPagedUsersByItem")) {
	    return getPagedUsersByItem(mapping, form, request, response);
	}
	if (param.equals("setVerifiedByMonitor")) {
	    return setVerifiedByMonitor(mapping, form, request, response);
	}
	if (param.equals("setSubmissionDeadline")) {
	    return setSubmissionDeadline(mapping, form, request, response);
	}

	return mapping.findForward(TaskListConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);

	ITaskListService service = getTaskListService();
	TaskList taskList = service.getTaskListByContentId(contentId);

	List<SessionDTO> sessionDtos = service.getSummary(contentId);

	// cache into sessionMap
	sessionMap.put(TaskListConstants.ATTR_SESSION_DTOS, sessionDtos);
	sessionMap.put(TaskListConstants.ATTR_MONITOR_VERIFICATION_REQUIRED, taskList.isMonitorVerificationRequired());
	sessionMap.put(TaskListConstants.PAGE_EDITABLE, taskList.isContentInUse());
	sessionMap.put(TaskListConstants.ATTR_TASKLIST, taskList);
	sessionMap.put(TaskListConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	sessionMap.put(TaskListConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));

	if (taskList.getSubmissionDeadline() != null) {
	    Date submissionDeadline = taskList.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    sessionMap.put(TaskListConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    sessionMap.put(TaskListConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING, DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	// Create reflectList if reflection is enabled.
	if (taskList.isReflectOnActivity()) {
	    List<ReflectDTO> reflectList = service.getReflectList(taskList.getContentId());
	    // Add reflectList to sessionMap
	    sessionMap.put(TaskListConstants.ATTR_REFLECT_LIST, reflectList);
	}

	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    private ActionForward itemSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ITaskListService service = getTaskListService();
	String sessionMapID = request.getParameter(TaskListConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(TaskListConstants.ATTR_SESSION_MAP_ID, sessionMapID);

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long itemUid = WebUtil.readLongParam(request, TaskListConstants.PARAM_ITEM_UID);

	TaskListItem item = service.getTaskListItemByUid(itemUid);

	// create sessionList depending on whether the item was created by author or by learner
	List<SessionDTO> sessionDtos = new ArrayList<SessionDTO>();
	if (item.isCreateByAuthor()) {
	    List<TaskListSession> sessionList = service.getSessionsByContentId(contentId);
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

	return mapping.findForward(TaskListConstants.SUCCESS);
    }

    /**
     * Refreshes user list.
     */
    public ActionForward getPagedUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	ITaskListService service = getTaskListService();

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
	if (sortBy == "") {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	// Get the user list from the db
	Collection<TaskListUserDTO> userDtos = service.getPagedUsersBySession(sessionId, page - 1, rowLimit, sortBy,
		sortOrder, searchString);
	int countSessionUsers = service.getCountPagedUsersBySession(sessionId, searchString);

	int totalPages = new Double(
		Math.ceil(new Integer(countSessionUsers).doubleValue() / new Integer(rowLimit).doubleValue()))
			.intValue();

	JSONArray rows = new JSONArray();
	int i = 1;
	for (TaskListUserDTO userDto : userDtos) {

	    JSONArray userData = new JSONArray();
	    userData.put(userDto.getUserId());
	    String fullName = StringEscapeUtils.escapeHtml(userDto.getFullName());
	    userData.put(fullName);

	    Set<Long> completedTaskUids = userDto.getCompletedTaskUids();
	    for (TaskListItem item : items) {
		String completionImage = completedTaskUids.contains(item.getUid())
			? "<i class=\"fa fa-check\"></i>"
			: "<i class=\"fa fa-minus\"></i>";
		userData.put(completionImage);
	    }

	    if (tasklist.isMonitorVerificationRequired()) {
		String label = StringEscapeUtils.escapeHtml(service.getMessage("label.confirm"));

		String verificationStatus = userDto.isVerifiedByMonitor()
			? "<i class=\"fa fa-check\"></i>"
			: "<a id='verif-" + userDto.getUserId()
				+ "' href='javascript:;' onclick='return setVerifiedByMonitor(this, "
				+ userDto.getUserId() + ");'>" + label + "</a>";
		userData.put(verificationStatus);
	    }

	    JSONObject userRow = new JSONObject();
	    userRow.put("id", i++);
	    userRow.put("cell", userData);

	    rows.put(userRow);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.put("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responseJSON.toString()));
	return null;
    }

    /**
     * Refreshes user list.
     */
    public ActionForward getPagedUsersByItem(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	ITaskListService service = getTaskListService();

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long itemUid = WebUtil.readLongParam(request, TaskListConstants.PARAM_ITEM_UID);

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, AttributeNames.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, AttributeNames.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, AttributeNames.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, AttributeNames.PARAM_SIDX, true);
	if (sortBy == "") {
	    sortBy = "userName";
	}
	String searchString = WebUtil.readStrParam(request, "userName", true);

	// Get the user list from the db
	Collection<TaskListUserDTO> userDtos = service.getPagedUsersBySessionAndItem(sessionId, itemUid, page - 1,
		rowLimit, sortBy, sortOrder, searchString);
	int countSessionUsers = service.getCountPagedUsersBySession(sessionId, searchString);

	int totalPages = new Double(
		Math.ceil(new Integer(countSessionUsers).doubleValue() / new Integer(rowLimit).doubleValue()))
			.intValue();

	//date formatters
	DateFormat dateFormatter = new SimpleDateFormat("d-MMM-yyyy h:mm a");
	HttpSession ss = SessionManager.getSession();
	UserDTO monitorDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	TimeZone monitorTimeZone = monitorDto.getTimeZone();

	//get all comments and attachments
	TaskListItem item = service.getTaskListItemByUid(itemUid);
	Set<TaskListItemComment> itemComments = item.getComments();
	Set<TaskListItemAttachment> itemAttachments = item.getAttachments();
	String label = StringEscapeUtils.escapeHtml(service.getMessage("label.download"));

	int i = 0;
	JSONArray rows = new JSONArray();
	for (TaskListUserDTO userDto : userDtos) {

	    JSONArray userData = new JSONArray();
	    String fullName = StringEscapeUtils.escapeHtml(userDto.getFullName());
	    userData.put(fullName);

	    String completionImage = userDto.isCompleted()
		    ? "<i class=\"fa fa-check\"></i>"
		    : "<i class=\"fa fa-minus\"></i>";
	    userData.put(completionImage);

	    String accessDate = (userDto.getAccessDate() == null) ? ""
		    : dateFormatter
			    .format(DateUtil.convertToTimeZoneFromDefault(monitorTimeZone, userDto.getAccessDate()));
	    userData.put(accessDate);

	    // fill up with comments and attachments made by this user
	    if (item.isCommentsAllowed() || item.isFilesAllowed()) {
		String commentsFiles = "<ul>";

		ArrayList<String> userComments = new ArrayList<String>();
		for (TaskListItemComment comment : itemComments) {
		    if (userDto.getUserId().equals(comment.getCreateBy().getUserId())) {
			userComments.add(comment.getComment());
		    }
		}
		if (!userComments.isEmpty()) {
		    commentsFiles += "<li>";
		    for (String userComment : userComments) {
			commentsFiles += StringEscapeUtils.escapeHtml(userComment);
		    }
		    commentsFiles += "</li>";
		}

		ArrayList<TaskListItemAttachment> userAttachments = new ArrayList<TaskListItemAttachment>();
		for (TaskListItemAttachment attachment : itemAttachments) {
		    if (userDto.getUserId().equals(attachment.getCreateBy().getUserId())) {
			userAttachments.add(attachment);
		    }
		}
		if (!userAttachments.isEmpty()) {
		    commentsFiles += "<li>";
		    for (TaskListItemAttachment userAttachment : userAttachments) {
			commentsFiles += StringEscapeUtils.escapeHtml(userAttachment.getFileName()) + " ";
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
		userData.put(commentsFiles);
	    }

	    JSONObject userRow = new JSONObject();
	    userRow.put("id", i++);
	    userRow.put("cell", userData);

	    rows.put(userRow);
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("total", totalPages);
	responseJSON.put("page", page);
	responseJSON.put("records", countSessionUsers);
	responseJSON.put("rows", rows);

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responseJSON.toString()));
	return null;
    }

    /**
     * Mark taskList user as verified.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    private ActionForward setVerifiedByMonitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	Long userUid = WebUtil.readLongParam(request, TaskListConstants.ATTR_USER_UID);
	ITaskListService service = getTaskListService();
	TaskListUser user = service.getUser(userUid);
	user.setVerifiedByMonitor(true);
	service.createUser(user);

	response.setContentType("text/html");
	PrintWriter out = response.getWriter();
	out.write(userUid.toString());
	out.flush();
	out.close();
	return null;
    }

    /**
     * Set Submission Deadline
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	ITaskListService service = getTaskListService();
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	TaskList taskList = service.getTaskListByContentId(contentID);

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
	    formattedDate = DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale());
	}
	taskList.setSubmissionDeadline(tzSubmissionDeadline);
	service.saveOrUpdateTaskList(taskList);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private ITaskListService getTaskListService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ITaskListService) wac.getBean(TaskListConstants.TASKLIST_SERVICE);
    }

}
