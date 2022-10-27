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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.monitoring.quartz.job.EmailProgressMessageJob;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Responsible for "Email Progress" functionality.
 */
@Controller
@RequestMapping("/emailProgress")
public class EmailProgressController {
    private static Logger log = Logger.getLogger(EmailNotificationsController.class);
    
    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private IEventNotificationService eventNotificationService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private Scheduler scheduler;

    // ---------------------------------------------------------------------
    // Class level constants
    // ---------------------------------------------------------------------

    private static final String TRIGGER_PREFIX_NAME = "emailProgressMessageTrigger:";
    private static final String JOB_PREFIX_NAME = "emailProgressMessageJob:";

    /**
     * Gets learners or monitors of the lesson and organisation containing it.
     *
     * @throws SchedulerException
     */
    @RequestMapping("/getEmailProgressDates")
    @ResponseBody
    public String getEmailProgressDates(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, SchedulerException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(), "get class members")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	ArrayNode datesJSON = JsonNodeFactory.instance.arrayNode();

	// find all the current dates set up to send the emails
	String triggerPrefix = getTriggerPrefix(lessonId);
	SortedSet<Date> currentDatesSet = new TreeSet<>();
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
	    datesJSON.add(createDateJSON(request.getLocale(), user, date, null));
	}
	responseJSON.set("dates", datesJSON);

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    private ObjectNode createDateJSON(Locale locale, UserDTO user, Date date, String result) {
	ObjectNode dateJSON = JsonNodeFactory.instance.objectNode();
	if (result != null) {
	    dateJSON.put("result", result);
	}
	if (date != null) {
	    dateJSON.put("id", date.getTime());
	    dateJSON.put("ms", date.getTime());
	    dateJSON.put("date", DateUtil.convertToStringForJSON(date, locale));
	}
	return dateJSON;
    }

    private String getTriggerPrefix(Long lessonId) {
	return new StringBuilder(EmailProgressController.TRIGGER_PREFIX_NAME).append(lessonId).append(":").toString();
    }

    private String getTriggerName(Long lessonId, Date date) {
	return new StringBuilder(EmailProgressController.TRIGGER_PREFIX_NAME).append(lessonId).append(":")
		.append(date.getTime()).toString();
    }

    private String getJobName(Long lessonId, Date date) {
	return new StringBuilder(EmailProgressController.JOB_PREFIX_NAME).append(lessonId).append(":")
		.append(date.getTime()).toString();
    }

    /**
     * Add or remove a date for the email progress
     */
    @RequestMapping(path = "/updateEmailProgressDate", method = RequestMethod.POST)
    @ResponseBody
    public String updateEmailProgressDate(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonId, getCurrentUser().getUserID(), "get class members")) {
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
	ObjectNode dateJSON = null;

	try {
	    Set<TriggerKey> triggerKeys = scheduler
		    .getTriggerKeys(GroupMatcher.triggerGroupEquals(Scheduler.DEFAULT_GROUP));
	    Trigger trigger = null;

	    for (TriggerKey triggerKey : triggerKeys) {
		if (scheduledTriggerName.equals(triggerKey.getName())) {
		    trigger = scheduler.getTrigger(triggerKey);
		    break;
		}
	    }

	    if (add) {
		if (trigger == null) {
		    String desc = new StringBuilder("Send progress email. Lesson ").append(lessonId).append(" on ")
			    .append(newDate).toString();
		    // build job detail based on the bean class
		    JobDetail EmailProgressMessageJob = JobBuilder.newJob(EmailProgressMessageJob.class)
			    .withIdentity(getJobName(lessonId, newDate)).withDescription(desc)
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
	    EmailProgressController.log.error("Error occurred at " + "[EmailProgressAction]- fail to email scheduling",
		    e);
	}

	if (dateJSON != null) {
	    response.setContentType("application/json;charset=utf-8");
	}
	return dateJSON.toString();
    }

    @RequestMapping(path = "/sendLessonProgressEmail", method = RequestMethod.POST)
    @ResponseBody
    public String sendLessonProgressEmail(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer monitorUserId = getCurrentUser().getUserID();
	if (!securityService.isLessonMonitor(lessonId, monitorUserId, "get lesson progress")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	String parts[] = monitoringService.generateLessonProgressEmail(lessonId, monitorUserId);
	String error = null;
	int sent = 0;

	try {
	    if (eventNotificationService.sendMessage(null, monitorUserId,
		    IEventNotificationService.DELIVERY_METHOD_MAIL, parts[0], parts[1], true)) {
		sent = 1;
	    }

	} catch (InvalidParameterException ipe) {
	    error = ipe.getMessage();
	} catch (Exception e) {
	    error = e.getMessage();
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("sent", sent);
	if (error != null) {
	    responseJSON.put("error", error);
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    private UserDTO getCurrentUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }
}
