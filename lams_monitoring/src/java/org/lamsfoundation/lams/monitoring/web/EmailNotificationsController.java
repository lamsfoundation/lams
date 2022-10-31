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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.EmailNotificationArchive;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.dto.EmailScheduleMessageJobDTO;
import org.lamsfoundation.lams.monitoring.quartz.job.EmailScheduleMessageJob;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>
 * Responsible for "Email notification" functionality.
 * </p>
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/emailNotifications")
public class EmailNotificationsController {

    private static Logger log = Logger.getLogger(EmailNotificationsController.class);

    // ---------------------------------------------------------------------
    // Class level constants
    // ---------------------------------------------------------------------

    private static final String TRIGGER_PREFIX_NAME = "emailMessageOnScheduleTrigger:";
    private static final String JOB_PREFIX_NAME = "emailScheduleMessageJob:";

    @Autowired
    private IEventNotificationService eventNotificationService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILogEventService logEventService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ILearnerService learnerService;

    /**
     * Shows "Email notification" page for particular lesson.
     */
    @RequestMapping("/getLessonView")
    public String getLessonView(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(),
		"show lesson email notifications")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Lesson lesson = learnerService.getLesson(lessonId);
	if (!lesson.getEnableLessonNotifications()) {
	    logEventService.logEvent(LogEvent.TYPE_NOTIFICATION, getCurrentUser().getUserID(), null, lessonId, null,
		    "Attempted to send notification when notifications are disabled in lesson " + lessonId);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Notifications are disabled in the lesson");
	    return null;
	}

	Set<Activity> activities = lesson.getLearningDesign().getActivities();
	request.setAttribute("lesson", lesson);
	request.setAttribute("activities", activities);

	boolean newUI = WebUtil.readBooleanParam(request, "newUI", false);

	return "emailnotifications/lessonNotifications" + (newUI ? "5" : "");
    }

    /**
     * Shows "Email notification" page for particular course.
     */
    @RequestMapping("/getCourseView")
    public String getCourseView(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	int orgId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	if (!securityService.isGroupMonitor(orgId, getCurrentUser().getUserID(), "show course email notifications")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	// getting the organisation
	Organisation org = (Organisation) userManagementService.findById(Organisation.class, orgId);

	boolean isGroupMonitor = securityService.hasOrgRole(orgId, getCurrentUser().getUserID(),
		new String[] { Role.GROUP_MANAGER }, "show course email notifications");
	Integer userRole = isGroupMonitor ? Role.ROLE_GROUP_MANAGER : Role.ROLE_MONITOR;
	Map<Long, IndexLessonBean> staffMap = lessonService
		.getLessonsByOrgAndUserWithCompletedFlag(getCurrentUser().getUserID(), orgId, userRole);

	// Already sorted, just double check that it does not contain finished or removed lessons
	// This call should not be returning REMOVED lessons anyway so test for finished ones.
	ArrayList<IndexLessonBean> lessons = new ArrayList<>(staffMap.size());
	for (IndexLessonBean lesson : staffMap.values()) {
	    if (!Lesson.FINISHED_STATE.equals(lesson.getState())) {
		lessons.add(lesson);
	    }
	}

	IndexLessonBean firstLesson = lessons.size() > 0 ? lessons.get(0) : null;

	request.setAttribute("org", org);
	request.setAttribute("lessons", lessons);
	request.setAttribute("firstLesson", firstLesson);

	boolean newUI = WebUtil.readBooleanParam(request, "newUI", false);

	return "emailnotifications/courseNotifications" + (newUI ? "5" : "");
    }

    /**
     * Renders a page listing all scheduled emails.
     */
    @RequestMapping("/showScheduledEmails")
    public String showScheduledEmails(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, SchedulerException {

	TreeSet<EmailScheduleMessageJobDTO> scheduleList = new TreeSet<>();
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	boolean isLessonNotifications = (lessonId != null);
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	if (isLessonNotifications) {
	    if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(),
		    "show scheduled lesson email notifications")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return null;
	    }
	} else {
	    if (!securityService.isGroupMonitor(organisationId, getCurrentUser().getUserID(),
		    "show scheduled course email notifications")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the organisation");
		return null;
	    }
	}

	Set<TriggerKey> triggerKeys = scheduler
		.getTriggerKeys(GroupMatcher.triggerGroupEquals(Scheduler.DEFAULT_GROUP));
	for (TriggerKey triggerKey : triggerKeys) {
	    String triggerName = triggerKey.getName();
	    if (triggerName.startsWith(EmailNotificationsController.TRIGGER_PREFIX_NAME)) {
		Trigger trigger = scheduler.getTrigger(triggerKey);
		JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
		JobDataMap jobDataMap = jobDetail.getJobDataMap();

		// filter triggers
		if (isLessonNotifications) {
		    Object jobLessonId = jobDataMap.get(AttributeNames.PARAM_LESSON_ID);
		    if ((jobLessonId == null) || (!lessonId.equals(jobLessonId))) {
			continue;
		    }
		} else {
		    Object jobOrganisationId = jobDataMap.get(AttributeNames.PARAM_ORGANISATION_ID);
		    if ((jobOrganisationId == null) || (!organisationId.equals(jobOrganisationId))) {
			continue;
		    }
		}

		Date triggerDate = trigger.getNextFireTime();
		String emailBody = WebUtil.convertNewlines((String) jobDataMap.get("emailBody"));
		int searchType = (Integer) jobDataMap.get("searchType");
		EmailScheduleMessageJobDTO emailScheduleJobDTO = new EmailScheduleMessageJobDTO();
		emailScheduleJobDTO.setTriggerName(triggerName);
		emailScheduleJobDTO.setTriggerDate(triggerDate);
		emailScheduleJobDTO.setEmailBody(emailBody);
		emailScheduleJobDTO.setSearchType(searchType);
		scheduleList.add(emailScheduleJobDTO);
	    }
	}

	request.setAttribute("scheduleList", scheduleList);
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, organisationId);

	boolean newUI = WebUtil.readBooleanParam(request, "newUI", false);

	return "emailnotifications/scheduledEmailList" + (newUI ? "5" : "");
    }

    /**
     * Renders a page listing all archived emails.
     */
    @RequestMapping("/showArchivedEmails")
    public String showArchivedEmails(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, SchedulerException {

	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	boolean isLessonNotifications = (lessonId != null);
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	if (isLessonNotifications) {
	    if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(),
		    "show archived lesson email notifications")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return null;
	    }

	    List<EmailNotificationArchive> notifications = monitoringService.getArchivedEmailNotifications(lessonId);
	    request.setAttribute("notifications", notifications);
	} else {
	    if (!securityService.isGroupMonitor(organisationId, getCurrentUser().getUserID(),
		    "show archived course email notifications")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the organisation");
		return null;
	    }
	    List<EmailNotificationArchive> notifications = monitoringService
		    .getArchivedEmailNotifications(organisationId);
	    request.setAttribute("notifications", notifications);
	}

	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, organisationId);

	boolean newUI = WebUtil.readBooleanParam(request, "newUI", false);

	return "emailnotifications/archivedEmailList" + (newUI ? "5" : "");
    }

    @RequestMapping("getArchivedRecipients")
    @ResponseBody
    public String getArchivedRecipients(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long emailNotificationUid = WebUtil.readLongParam(request, "emailNotificationUid");
	EmailNotificationArchive notification = (EmailNotificationArchive) userManagementService
		.findById(EmailNotificationArchive.class, emailNotificationUid);

	Long lessonId = notification.getLessonId();
	Integer organisationId = notification.getOrganisationId();
	boolean isLessonNotifications = (lessonId != null);
	// check if the user is allowed to fetch this data
	if (isLessonNotifications) {
	    if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(),
		    "show archived lesson email notification participants")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return null;
	    }
	} else {
	    if (!securityService.isGroupMonitor(organisationId, getCurrentUser().getUserID(),
		    "show archived course email notification participants")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the organisation");
		return null;
	    }
	}

	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);

	// get only recipients we want on the page
	List<User> recipients = monitoringService.getArchivedEmailNotificationRecipients(emailNotificationUid, rowLimit,
		(page - 1) * rowLimit);

	// build JSON which is understood by jqGrid
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put(CommonConstants.ELEMENT_PAGE, page);
	responseJSON.put(CommonConstants.ELEMENT_TOTAL, ((notification.getRecipients().size() - 1) / rowLimit) + 1);
	responseJSON.put(CommonConstants.ELEMENT_RECORDS, recipients.size());

	ArrayNode rowsJSON = JsonNodeFactory.instance.arrayNode();
	for (User recipient : recipients) {
	    ObjectNode rowJSON = JsonNodeFactory.instance.objectNode();
	    rowJSON.put(CommonConstants.ELEMENT_ID, recipient.getUserId());

	    ArrayNode cellJSON = JsonNodeFactory.instance.arrayNode();
	    cellJSON.add(new StringBuilder(recipient.getLastName()).append(", ").append(recipient.getFirstName())
		    .append(" [").append(recipient.getLogin()).append("]").toString());

	    rowJSON.set(CommonConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.add(rowJSON);
	}

	responseJSON.set(CommonConstants.ELEMENT_ROWS, rowsJSON);
	response.setContentType("application/json;charset=UTF-8");

	return responseJSON.toString();
    }

    /**
     * Delete a scheduled emails.
     *
     * @throws JSONException
     */
    @RequestMapping(path = "/deleteNotification", method = RequestMethod.POST)
    @ResponseBody
    public String deleteNotification(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, SchedulerException {

	String inputTriggerName = WebUtil.readStrParam(request, "triggerName");
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Integer userId = getCurrentUser().getUserID();
	boolean isLessonNotifications = (lessonId != null);
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);

	ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
	String error = null;

	try {
	    // if this method throws an Exception, there will be no deleteNotification=true in the JSON reply
	    if (isLessonNotifications) {
		if (!securityService.isLessonMonitor(lessonId, userId, "show scheduled lesson email notifications")) {
		    error = "Unable to delete notification: the user is not a monitor in the lesson";
		}
	    } else {
		if (!securityService.isGroupMonitor(organisationId, userId,
			"show scheduled course course email notifications")) {
		    error = "Unable to delete notification: the user is not a monitor in the organisation";
		}
	    }

	    if (error == null) {
		Set<TriggerKey> triggerKeys = scheduler
			.getTriggerKeys(GroupMatcher.triggerGroupEquals(Scheduler.DEFAULT_GROUP));
		for (TriggerKey triggerKey : triggerKeys) {
		    String triggerName = triggerKey.getName();
		    if (triggerName.equals(inputTriggerName)) {
			Trigger trigger = scheduler.getTrigger(triggerKey);

			JobKey jobKey = trigger.getJobKey();

			JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
			JobDataMap jobDataMap = jobDetail.getJobDataMap();
			logEventService.logEvent(LogEvent.TYPE_NOTIFICATION, userId, null, lessonId, null,
				"Deleting unsent scheduled notification " + jobKey + " "
					+ jobDataMap.getString("emailBody"));

			scheduler.deleteJob(jobKey);

		    }
		}

	    }

	} catch (Exception e) {
	    String[] msg = new String[1];
	    msg[0] = e.getMessage();
	    error = monitoringService.getMessageService().getMessage("error.system.error", msg);
	}

	jsonObject.put("deleteNotification", error == null ? "true" : error);
	response.setContentType("application/json;charset=utf-8");
	return jsonObject.toString();

    }

    /**
     * Exports the given archived email notification to excel.
     */
    @RequestMapping("exportArchivedNotification")
    @ResponseStatus(HttpStatus.OK)
    public void exportArchivedNotification(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {

	Long emailNotificationUid = WebUtil.readLongParam(request, "emailNotificationUid");
	EmailNotificationArchive notification = (EmailNotificationArchive) userManagementService
		.findById(EmailNotificationArchive.class, emailNotificationUid);

	Long lessonId = notification.getLessonId();
	Integer organisationId = notification.getOrganisationId();
	boolean isLessonNotifications = (lessonId != null);
	// check if the user is allowed to fetch this data
	if (isLessonNotifications) {
	    if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(),
		    "export archived lesson email notification")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return;
	    }
	} else {
	    if (!securityService.isGroupMonitor(organisationId, getCurrentUser().getUserID(),
		    "export archived course email notification")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the organisation");
		return;
	    }
	}

	List<ExcelSheet> sheets = monitoringService.exportArchivedEmailNotification(emailNotificationUid);
	String fileName = "email_notification_"
		+ FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(notification.getSentOn()) + ".xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	ExcelUtil.createExcel(response.getOutputStream(), sheets,
		monitoringService.getMessageService().getMessage("export.dateheader"), false);
    }

    /**
     * Method called via Ajax. It either emails selected users or schedules these emails to be sent on specified date.
     */
    @RequestMapping(path = "/emailUsers", method = RequestMethod.POST)
    @ResponseBody
    public String emailUsers(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

	ObjectNode ObjectNode = JsonNodeFactory.instance.objectNode();

	String emailBody = WebUtil.readStrParam(request, "emailBody");
	Long scheduleDateParameter = WebUtil.readLongParam(request, "scheduleDate", true);

	String scheduleDateStr = "";
	String emailClauseStr = "";
	// check if we need to send email instantly
	if (scheduleDateParameter == null) {
	    boolean isSuccessfullySent = true;
	    String[] userIdStrs = request.getParameterValues("userId");
	    Set<Integer> userIdInts = new HashSet<>();
	    for (String userIdStr : userIdStrs) {
		int userId = Integer.parseInt(userIdStr);
		userIdInts.add(userId);
		boolean isHtmlFormat = false;
		isSuccessfullySent &= eventNotificationService.sendMessage(null, userId,
			IEventNotificationService.DELIVERY_METHOD_MAIL, monitoringService.getMessageService()
				.getMessage("event.emailnotifications.email.subject", new Object[] {}),
			emailBody, isHtmlFormat);
	    }
	    monitoringService.archiveEmailNotification(
		    WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true),
		    WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true),
		    WebUtil.readIntParam(request, "searchType", true), emailBody, userIdInts);

	    ObjectNode.put("isSuccessfullySent", isSuccessfullySent);

	    //prepare data for audit log
	    scheduleDateStr = "now";
	    emailClauseStr = "for users (userIds: " + StringUtils.join(userIdStrs, ",") + ")";

	} else {
	    try {
		Calendar now = Calendar.getInstance();

		// calculate scheduleDate
		Date scheduleDateTeacherTimezone = new Date(scheduleDateParameter);
		TimeZone teacherTimeZone = getCurrentUser().getTimeZone();
		Date scheduleDate = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, scheduleDateTeacherTimezone);

		// build job detail based on the bean class
		JobDetail emailScheduleMessageJob = JobBuilder.newJob(EmailScheduleMessageJob.class)
			.withIdentity(EmailNotificationsController.JOB_PREFIX_NAME + now.getTimeInMillis())
			.withDescription("schedule email message to user(s)").usingJobData("emailBody", emailBody)
			.build();

		Map<String, Object> searchParameters = new HashMap<>();
		copySearchParametersFromRequestToMap(request, searchParameters);
		searchParameters.forEach(emailScheduleMessageJob.getJobDataMap()::putIfAbsent);

		// create customized triggers
		Trigger startLessonTrigger = TriggerBuilder.newTrigger()
			.withIdentity(EmailNotificationsController.TRIGGER_PREFIX_NAME + now.getTimeInMillis())
			.startAt(scheduleDate).build();
		// start the scheduling job
		scheduler.scheduleJob(emailScheduleMessageJob, startLessonTrigger);
		ObjectNode.put("isSuccessfullyScheduled", true);

		//prepare data for audit log
		scheduleDateStr = "on " + scheduleDate;
		Object lessonIdObj = searchParameters.get(AttributeNames.PARAM_LESSON_ID);
		Object lessonIDsObj = searchParameters.get("lessonIDs");
		Object organisationIdObj = searchParameters.get(AttributeNames.PARAM_ORGANISATION_ID);
		if (lessonIdObj != null) {
		    emailClauseStr = "for lesson (lessonId: " + lessonIdObj + ")";
		} else if (lessonIDsObj != null) {
		    emailClauseStr = "for lessons (lessonIDs: " + StringUtils.join((String[]) lessonIDsObj, ",") + ")";
		} else if (organisationIdObj != null) {
		    emailClauseStr = "for organisation (organisationId: " + organisationIdObj + ")";
		}

	    } catch (SchedulerException e) {
		EmailNotificationsController.log
			.error("Error occurred at " + "[emailScheduleMessage]- fail to email scheduling", e);
	    }
	}

	//audit log
	logEventService.logEvent(LogEvent.TYPE_NOTIFICATION, getCurrentUser().getUserID(), null, null, null,
		"User " + getCurrentUser().getLogin() + " set a notification " + emailClauseStr + " " + scheduleDateStr
			+ " with the following notice:  " + emailBody);

	response.setContentType("application/json;charset=utf-8");
	return ObjectNode.toString();
    }

    /**
     * Refreshes user list.
     */
    @RequestMapping("/getUsers")
    @ResponseBody
    public String getUsers(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	Map<String, Object> map = new HashMap<>();
	copySearchParametersFromRequestToMap(request, map);
	Long lessonId = (Long) map.get(AttributeNames.PARAM_LESSON_ID);
	Integer orgId = (Integer) map.get(AttributeNames.PARAM_ORGANISATION_ID);

	if (lessonId != null) {
	    if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(),
		    "get users for lesson email notifications")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the lesson");
		return null;
	    }
	} else if (orgId != null) {
	    if (!securityService.isGroupMonitor(orgId, getCurrentUser().getUserID(),
		    "get users for course email notifications")) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a monitor in the organisation");
		return null;
	    }
	}

	int searchType = (Integer) map.get("searchType");
	Long activityId = (Long) map.get(AttributeNames.PARAM_ACTIVITY_ID);
	Integer xDaystoFinish = (Integer) map.get("daysToDeadline");
	String[] lessonIds = (String[]) map.get("lessonIDs");
	Collection<User> users = monitoringService.getUsersByEmailNotificationSearchType(searchType, lessonId,
		lessonIds, activityId, xDaystoFinish, orgId);

	ArrayNode cellarray = JsonNodeFactory.instance.arrayNode();

	ObjectNode responseDate = JsonNodeFactory.instance.objectNode();
	responseDate.put("total", "" + users.size());
	responseDate.put("page", "" + 1);
	responseDate.put("records", "" + users.size());

	for (User user : users) {
	    ArrayNode cell = JsonNodeFactory.instance.arrayNode();
	    cell.add(new StringBuilder(user.getLastName()).append(", ").append(user.getFirstName()).append(" (")
		    .append(user.getLogin()).append(")").toString());

	    ObjectNode cellobj = JsonNodeFactory.instance.objectNode();
	    cellobj.put("id", "" + user.getUserId());
	    cellobj.set("cell", cell);
	    cellarray.add(cellobj);
	}
	responseDate.set("rows", cellarray);
	response.setContentType("application/json;charset=utf-8");
	return responseDate.toString();
    }

    /**
     * Copies search parameters from request to specified map. Validates parameters along the way.
     *
     * @param request
     * @param map
     *            specified map
     */
    private void copySearchParametersFromRequestToMap(HttpServletRequest request, Map<String, Object> map) {
	int searchType = WebUtil.readIntParam(request, "searchType");
	map.put("searchType", searchType);

	switch (searchType) {
	    case MonitoringConstants.LESSON_TYPE_ASSIGNED_TO_LESSON:
	    case MonitoringConstants.LESSON_TYPE_HAVENT_FINISHED_LESSON:
	    case MonitoringConstants.LESSON_TYPE_HAVE_FINISHED_LESSON:
	    case MonitoringConstants.LESSON_TYPE_HAVENT_STARTED_LESSON:
	    case MonitoringConstants.LESSON_TYPE_HAVE_STARTED_LESSON:
	    case MonitoringConstants.LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY:
	    case MonitoringConstants.LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE:
		Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
		Assert.notNull(lessonId);
		map.put(AttributeNames.PARAM_LESSON_ID, lessonId);
		break;

	    case MonitoringConstants.COURSE_TYPE_HAVENT_STARTED_ANY_LESSONS:
	    case MonitoringConstants.COURSE_TYPE_HAVE_FINISHED_PARTICULAR_LESSON:
	    case MonitoringConstants.COURSE_TYPE_HAVENT_STARTED_PARTICULAR_LESSON:
	    case MonitoringConstants.COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS:
	    case MonitoringConstants.COURSE_TYPE_HAVENT_FINISHED_THESE_LESSONS:
		Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
		Assert.notNull(organisationId);
		map.put(AttributeNames.PARAM_ORGANISATION_ID, organisationId);
		break;
	}

	switch (searchType) {
	    case MonitoringConstants.LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY:
		Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
		Assert.notNull(activityId);
		map.put(AttributeNames.PARAM_ACTIVITY_ID, activityId);
		break;

	    case MonitoringConstants.LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE:
		Integer xDaystoFinish = WebUtil.readIntParam(request, "daysToDeadline");
		Assert.notNull(xDaystoFinish);
		map.put("daysToDeadline", xDaystoFinish);
		break;

	    case MonitoringConstants.COURSE_TYPE_HAVE_FINISHED_PARTICULAR_LESSON:
	    case MonitoringConstants.COURSE_TYPE_HAVENT_STARTED_PARTICULAR_LESSON:
		Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
		Assert.notNull(lessonId);
		map.put(AttributeNames.PARAM_LESSON_ID, lessonId);
		break;
	    case MonitoringConstants.COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS:
	    case MonitoringConstants.COURSE_TYPE_HAVENT_FINISHED_THESE_LESSONS:
		String[] lessonIds = request.getParameterValues(AttributeNames.PARAM_LESSON_ID);
		Assert.notNull(lessonIds);
		map.put("lessonIDs", lessonIds);
		break;
	}

    }

    private UserDTO getCurrentUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}
