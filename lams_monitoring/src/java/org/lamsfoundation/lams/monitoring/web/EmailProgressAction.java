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
import java.security.InvalidParameterException;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.monitoring.quartz.job.EmailProgressMessageJob;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Responsible for "Email Progress" functionality.
 */
public class EmailProgressAction extends LamsDispatchAction {

    // ---------------------------------------------------------------------
    // Class level constants
    // ---------------------------------------------------------------------

    private static final String TRIGGER_PREFIX_NAME = "emailProgressMessageTrigger:";
    private static final String JOB_PREFIX_NAME = "emailProgressMessageJob:";

    private static IEventNotificationService eventNotificationService;
    private static IMonitoringFullService monitoringService;
    private static ISecurityService securityService;

    // ---------------------------------------------------------------------
    // Struts Dispatch Method
    // ---------------------------------------------------------------------

    /**
     * Gets learners or monitors of the lesson and organisation containing it.
     * 
     * @throws SchedulerException
     */
    public ActionForward getEmailProgressDates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException, SchedulerException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getCurrentUser().getUserID(), "get class members", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	JSONObject responseJSON = new JSONObject();
	JSONArray datesJSON = new JSONArray();

	// find all the current dates set up to send the emails
	Scheduler scheduler = getScheduler();
	String triggerPrefix = getTriggerPrefix(lessonId);
	SortedSet<Date> currentDatesSet = new TreeSet<Date>();
	Set<TriggerKey> triggerKeys = scheduler
		.getTriggerKeys(GroupMatcher.triggerGroupEquals(Scheduler.DEFAULT_GROUP));
	for (TriggerKey triggerKey : triggerKeys) {
	    String triggerName = triggerKey.getName();
	    if (triggerName.startsWith(triggerPrefix)) {
		Trigger trigger = scheduler.getTrigger(triggerKey);
		JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
		JobDataMap jobDataMap = jobDetail.getJobDataMap();

		// get only the trigger for the current lesson

		Object jobLessonId = jobDataMap.get(AttributeNames.PARAM_LESSON_ID);
		if (lessonId.equals(jobLessonId)) {

		    Date triggerDate = trigger.getNextFireTime();
		    currentDatesSet.add(triggerDate);
		}
	    }
	}

	for (Date date : currentDatesSet) {
	    datesJSON.put(createDateJSON(request.getLocale(), user, date, null));
	}
	responseJSON.put("dates", datesJSON);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private JSONObject createDateJSON(Locale locale, UserDTO user, Date date, String result) throws JSONException {
	JSONObject dateJSON = new JSONObject();
	if (result != null) {
	    dateJSON.put("result", result);
	}
	if (date != null) {
	    dateJSON.put("id", date.getTime());
	    dateJSON.put("ms", date.getTime());
	    dateJSON.put("date", DateUtil.convertToStringForJSON(date, locale) );
	}
	return dateJSON;
    }

    private String getTriggerPrefix(Long lessonId) {
	return new StringBuilder(EmailProgressAction.TRIGGER_PREFIX_NAME).append(lessonId).append(":").toString();
    }

    private String getTriggerName(Long lessonId, Date date) {
	return new StringBuilder(EmailProgressAction.TRIGGER_PREFIX_NAME).append(lessonId).append(":")
		.append(date.getTime()).toString();
    }

    private String getJobName(Long lessonId, Date date) {
	return new StringBuilder(EmailProgressAction.JOB_PREFIX_NAME).append(lessonId).append(":")
		.append(date.getTime()).toString();
    }

    /**
     * Add or remove a date for the email progress
     */
    public ActionForward updateEmailProgressDate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonId, getCurrentUser().getUserID(), "get class members", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	// as we are using ms since UTC 0 calculated on the client, this will be correctly set up as server date 
	// and does not need changing (assuming the user's LAMS timezone matches the user's computer timezone).
	long dateId = WebUtil.readLongParam(request, "id");
	Date newDate = new Date(dateId); 
	boolean add = WebUtil.readBooleanParam(request, "add");

	// calculate scheduleDate
	String scheduledTriggerName = getTriggerName(lessonId, newDate);
	JSONObject dateJSON = null;

	try {
	    Scheduler scheduler = getScheduler();
	    Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher
		    .triggerGroupEquals(Scheduler.DEFAULT_GROUP));
	    Trigger trigger = null;

	    for (TriggerKey triggerKey : triggerKeys) {
		if (scheduledTriggerName.equals(triggerKey.getName())) {
		    trigger = scheduler.getTrigger(triggerKey);
		    break;
		}
	    }
		
	    if (add) {
		if (trigger == null) {
		    String desc = new StringBuilder("Send progress email. Lesson ")
		    	.append(lessonId).append(" on ").append(newDate).toString();
		    // build job detail based on the bean class
		    JobDetail EmailProgressMessageJob = JobBuilder.newJob(EmailProgressMessageJob.class)
			    .withIdentity(getJobName(lessonId, newDate))
			    .withDescription(desc)
			    .usingJobData(AttributeNames.PARAM_LESSON_ID, lessonId).build();

		    // create customized triggers
		    Trigger startLessonTrigger = TriggerBuilder.newTrigger().withIdentity(scheduledTriggerName)
			    .startAt(newDate).build();
		    // start the scheduling job
		    scheduler.scheduleJob(EmailProgressMessageJob, startLessonTrigger);

		    dateJSON = createDateJSON(request.getLocale(), user, newDate, "added");

		} else {
		    dateJSON = createDateJSON(request.getLocale(), user, newDate, "none");
		}
	    } else if (!add) {
		if (trigger != null) {
		    // remove trigger
		    scheduler.deleteJob(trigger.getJobKey());

		    dateJSON = createDateJSON(request.getLocale(), user, null, "deleted");
		} else {
		    dateJSON = createDateJSON(request.getLocale(), user, null, "none");
		}
	    }
	} catch (SchedulerException e) {
	    LamsDispatchAction.log.error("Error occurred at " + "[EmailProgressAction]- fail to email scheduling", e);
	}

	if (dateJSON != null) {
	    response.setContentType("application/json;charset=utf-8");
	    response.getWriter().write(dateJSON.toString());
	}
	return null;
    }

    public ActionForward sendLessonProgressEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {

	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer monitorUserId = getCurrentUser().getUserID();
	if (!getSecurityService().isLessonMonitor(lessonId, monitorUserId, "get lesson progress", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String parts[] = getMonitoringService().generateLessonProgressEmail(lessonId, monitorUserId);
	String error = null;
	int sent = 0;

	try {
	    if (getEventNotificationService().sendMessage(null, monitorUserId,
		    IEventNotificationService.DELIVERY_METHOD_MAIL, parts[0], parts[1], true)) {
		sent = 1;
	    } 

	} catch (InvalidParameterException ipe) {
	    error = ipe.getMessage();
	} catch (Exception e) {
	    error = e.getMessage();
	}

	JSONObject responseJSON = new JSONObject();
	responseJSON.put("sent", sent);
	if (error != null) {
	    responseJSON.put("error", error);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    private UserDTO getCurrentUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private IEventNotificationService getEventNotificationService() {
	if (eventNotificationService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    eventNotificationService = (IEventNotificationService) ctx.getBean("eventNotificationService");
	}
	return eventNotificationService;
    }

    private IMonitoringFullService getMonitoringService() {
	if (EmailProgressAction.monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    EmailProgressAction.monitoringService = (IMonitoringFullService) ctx.getBean("monitoringService");
	}
	return EmailProgressAction.monitoringService;
    }

    private ISecurityService getSecurityService() {
	if (securityService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    securityService = (ISecurityService) webContext.getBean("securityService");
	}

	return securityService;
    }

    /**
     *
     * @return the bean that defines Scheduler.
     */
    private Scheduler getScheduler() {
	WebApplicationContext ctx = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (Scheduler) ctx.getBean("scheduler");
    }
}
