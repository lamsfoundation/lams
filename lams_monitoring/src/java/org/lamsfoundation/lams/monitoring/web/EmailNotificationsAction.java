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
 
/* $Id$ */  
package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.events.DeliveryMethodMail;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.lesson.util.LessonComparator;
import org.lamsfoundation.lams.monitoring.MonitoringConstants;
import org.lamsfoundation.lams.monitoring.dto.EmailScheduleMessageJobDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * Responsible for "Email notification" functionality.
 * </p>
 * 
 * @author Andrey Balan
 * 
 *  ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/emailNotifications" parameter="method" validate="false"
 * @struts.action-forward name = "lessonView" path = "/emailnotifications/lessonNotifications.jsp"
 * @struts.action-forward name = "courseView" path = "/emailnotifications/courseNotifications.jsp"
 * @struts.action-forward name = "userList" path = "/emailnotifications/userList.jsp"
 * @struts.action-forward name = "scheduledEmailList" path = "/emailnotifications/scheduledEmailList.jsp"
 * 
 * ----------------XDoclet Tags--------------------
 */
public class EmailNotificationsAction extends LamsDispatchAction {
    
    // ---------------------------------------------------------------------
    // Class level constants
    // ---------------------------------------------------------------------
    
    private static final String TRIGGER_PREFIX_NAME = "emailMessageOnScheduleTrigger:";
    private static IEventNotificationService eventNotificationService;
    private static IUserManagementService userManagementService;

    // ---------------------------------------------------------------------
    // Struts Dispatch Method
    // ---------------------------------------------------------------------
    
    /**
     * Shows "Email notification" page for particular lesson.
     */
    public ActionForward getLessonView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	
	ICoreLearnerService learnerService = MonitoringServiceProxy.getLearnerService(getServlet()
		.getServletContext());

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = learnerService.getLesson(lessonId);
	Set activities = lesson.getLearningDesign().getActivities();
	
	request.setAttribute("lesson", lesson);
	request.setAttribute("activities", activities);

	return mapping.findForward("lessonView");
    }
    
    /**
     * Shows "Email notification" page for particular course.
     */
    public ActionForward getCourseView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	
	ICoreLearnerService learnerService = MonitoringServiceProxy.getLearnerService(getServlet()
		.getServletContext());

	int orgId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	
	// getting the organisation
	Organisation org = (Organisation) learnerService.getUserManagementService().findById(Organisation.class, orgId);
	
	//sort and filter lesson list
	Set<Lesson> lessons = new TreeSet<Lesson>(new LessonComparator());
	for (Lesson lesson : (Set<Lesson>)org.getLessons()) {
	    if (!Lesson.REMOVED_STATE.equals(lesson.getLessonStateId())
		    && !Lesson.FINISHED_STATE.equals(lesson.getLessonStateId())) {
		lessons.add(lesson);
	    }
	}
	
	Lesson firstLesson = null;
	Iterator<Lesson> lessonIter = lessons.iterator();
	if (lessonIter.hasNext()) {
	    firstLesson = lessonIter.next();
	}
	
	request.setAttribute("org", org);
	request.setAttribute("lessons", lessons);
	request.setAttribute("firstLesson", firstLesson);

	return mapping.findForward("courseView");
    }
    
    /**
     * Renders a page listing all scheduled emails.
     */
    public ActionForward showScheduledEmails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, SchedulerException {
	getUserManagementService();
	Scheduler scheduler = getScheduler();
	TreeSet<EmailScheduleMessageJobDTO> scheduleList = new TreeSet<EmailScheduleMessageJobDTO>();
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	boolean isLessonNotifications = (lessonId != null);
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	
	String[] triggerNames = scheduler.getTriggerNames(Scheduler.DEFAULT_GROUP);
	for (String triggerName : triggerNames) {
	    if (triggerName.startsWith(TRIGGER_PREFIX_NAME)) {
		Trigger trigger = scheduler.getTrigger(triggerName, Scheduler.DEFAULT_GROUP);
		JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobName(), Scheduler.DEFAULT_GROUP);
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		
		//filter triggers
		if (isLessonNotifications) {
		    Object jobLessonId = jobDataMap.get(AttributeNames.PARAM_LESSON_ID);
		    if ((jobLessonId == null) || (! lessonId.equals(jobLessonId))) {
			continue;
		    }
		} else {
		    Object jobOrganisationId = jobDataMap.get(AttributeNames.PARAM_ORGANISATION_ID);
		    if ((jobOrganisationId == null) || (! organisationId.equals(jobOrganisationId))) {
			continue;
		    }
		}
		
		Date triggerDate = trigger.getNextFireTime();
		String emailBody = WebUtil.convertNewlines((String) jobDataMap.get("emailBody"));
		int searchType = (Integer) jobDataMap.get("searchType");
		EmailScheduleMessageJobDTO emailScheduleJobDTO = new EmailScheduleMessageJobDTO();
		emailScheduleJobDTO.setTriggerDate(triggerDate);
		emailScheduleJobDTO.setEmailBody(emailBody);
		emailScheduleJobDTO.setSearchType(searchType);
		scheduleList.add(emailScheduleJobDTO);
	    }
	}
	
	request.setAttribute("scheduleList", scheduleList);
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(AttributeNames.PARAM_ORGANISATION_ID, organisationId);

	return mapping.findForward("scheduledEmailList");
    }

    /**
     * Method called via Ajax. It either emails selected users or schedules these emails to be sent on specified date.
     */
    public ActionForward emailUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {

	JSONObject JSONObject = new JSONObject();
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	getEventNotificationService();
	
	String emailBody = WebUtil.readStrParam(request, "emailBody");
	Long scheduleDateParameter = WebUtil.readLongParam(request, "scheduleDate", true);
	
	//check if we send email instantly
	if (scheduleDateParameter == null) {
	    boolean isSuccessfullySent = true;
	    String[] userIdStrs = request.getParameterValues("userId");
	    for (String userIdStr : userIdStrs) {
		int userId = Integer.parseInt(userIdStr);
		boolean isHtmlFormat = false;
		isSuccessfullySent &= eventNotificationService.sendMessage(null, 
			userId,
			IEventNotificationService.DELIVERY_METHOD_MAIL,
			monitoringService.getMessageService().getMessage("event.emailnotifications.email.subject",
				new Object[] {}), emailBody, isHtmlFormat);
	    }

	    JSONObject.put("isSuccessfullySent", isSuccessfullySent);
	} else {
	    try {
		Calendar now = Calendar.getInstance();
		
		// calculate scheduleDate
		Date scheduleDateTeacherTimezone = new Date(scheduleDateParameter);
		HttpSession ss = SessionManager.getSession();
		UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
		TimeZone teacherTimeZone = teacher.getTimeZone();
		Date scheduleDate = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, scheduleDateTeacherTimezone);
		
		JobDetail emailScheduleMessageJob = getEmailScheduleMessageJob();
		emailScheduleMessageJob.setName("emailScheduleMessageJob:" + now.getTimeInMillis());
		emailScheduleMessageJob.setDescription("schedule email message to user(s)");
		emailScheduleMessageJob.getJobDataMap().put("emailBody", emailBody);
		
		copySearchParametersFromRequestToMap(request, emailScheduleMessageJob.getJobDataMap());

		// create customized triggers
		Trigger startLessonTrigger = new SimpleTrigger(TRIGGER_PREFIX_NAME + now.getTimeInMillis(),
			Scheduler.DEFAULT_GROUP, scheduleDate);
		// start the scheduling job
		Scheduler scheduler = getScheduler();
		scheduler.scheduleJob(emailScheduleMessageJob, startLessonTrigger);
		JSONObject.put("isSuccessfullyScheduled", true);
		log.debug("Emails have been successfully scheduled to be sent on " + scheduleDate + " [search type is " + request.getParameter("searchType") + "]");
	    } catch (SchedulerException e) {
		log.error("Error occurred at " + "[emailScheduleMessage]- fail to email scheduling", e);
	    }
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
    }
    
    /**
     * Refreshes user list.
     */
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet()
		.getServletContext());
	ICoreLearnerService learnerService = MonitoringServiceProxy.getLearnerService(getServlet()
		.getServletContext());
	
	//TODO check checkOwnerOrStaffMember
	
	Map map = new HashMap();
	copySearchParametersFromRequestToMap(request, map);
	
	int searchType = (Integer) map.get("searchType");
	Long lessonId = (Long) map.get(AttributeNames.PARAM_LESSON_ID);
	Integer orgId = (Integer) map.get(AttributeNames.PARAM_ORGANISATION_ID);
	Long activityId = (Long) map.get(AttributeNames.PARAM_ACTIVITY_ID);
	Integer xDaystoFinish = (Integer) map.get("daysToDeadline");
	String[] lessonIds = (String[]) map.get("lessonIDs");
	Collection<User> users = monitoringService.getUsersByEmailNotificationSearchType(searchType, lessonId, lessonIds, activityId, xDaystoFinish, orgId);
	
	JSONArray cellarray = new JSONArray();

	JSONObject responcedata = new JSONObject();
	responcedata.put("total", "" + users.size());
	responcedata.put("page", "" + 1);
	responcedata.put("records", "" + users.size());
	
	for (User user : users) {
	    JSONArray cell=new JSONArray();
	    cell.put(StringEscapeUtils.escapeHtml(user.getFirstName()) + " " + StringEscapeUtils.escapeHtml(user.getLastName()) + " [" + StringEscapeUtils.escapeHtml(user.getLogin()) + "]");
	    
	    JSONObject cellobj = new JSONObject();
	    cellobj.put("id", "" + user.getUserId());
	    cellobj.put("cell", cell);
	    cellarray.put(cellobj);
	}
	responcedata.put("rows", cellarray);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responcedata.toString()));
	return null;
     }
    
    /**
     * Copies search parameters from request to specified map. Validates parameters along the way.
     * 
     * @param request
     * @param map specified map
     */
    private void copySearchParametersFromRequestToMap(HttpServletRequest request, Map map) {
	int searchType = WebUtil.readIntParam(request, "searchType");
	Assert.notNull(searchType);
	map.put("searchType", searchType);
	
	switch (searchType) {
	case MonitoringConstants.LESSON_TYPE_ASSIGNED_TO_LESSON:
	case MonitoringConstants.LESSON_TYPE_HAVENT_FINISHED_LESSON:
	case MonitoringConstants.LESSON_TYPE_HAVE_FINISHED_LESSON:
	case MonitoringConstants.LESSON_TYPE_HAVENT_STARTED_LESSON:
	case MonitoringConstants.LESSON_TYPE_HAVE_STARTED_LESSON:
	case MonitoringConstants.LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY:
	case MonitoringConstants.LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE:
	    Long lessonId = (Long) WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
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
	    Long lessonId = (Long) WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    Assert.notNull(lessonId);
	    map.put(AttributeNames.PARAM_LESSON_ID, lessonId);
	    break;
	case MonitoringConstants.COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS:
	case MonitoringConstants.COURSE_TYPE_HAVENT_FINISHED_THESE_LESSONS:
	    String[]  lessonIds = request.getParameterValues(AttributeNames.PARAM_LESSON_ID);
	    Assert.notNull(lessonIds);
	    map.put("lessonIDs", lessonIds);
	    break;
	}

    }
    
    private IEventNotificationService getEventNotificationService() {
	if (eventNotificationService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    eventNotificationService = (IEventNotificationService) ctx.getBean("eventNotificationService");
	}
	return eventNotificationService;
    }
    
    private IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }
    
    /**
     * 
     * @return the bean that defines emailScheduleMessageJob.
     */
    private JobDetail getEmailScheduleMessageJob() {
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (JobDetail) ctx.getBean(MonitoringConstants.JOB_EMAIL_MESSAGE);
    }
    
    /**
     * 
     * @return the bean that defines Scheduler.
     */
    private Scheduler getScheduler() {
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (Scheduler) ctx.getBean("scheduler");
    }
}
