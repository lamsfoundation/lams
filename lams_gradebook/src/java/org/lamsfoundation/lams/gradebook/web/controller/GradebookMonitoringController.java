/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.gradebook.web.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.gradebook.service.IGradebookFullService;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.gradebook.util.GradebookUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Handles the monitor interfaces for gradebook.
 * This is where marking for an activity/lesson takes place
 *
 * @author lfoxton
 */
@Controller
@RequestMapping("/gradebookMonitoring")
public class GradebookMonitoringController {
    private static Logger log = Logger.getLogger(GradebookMonitoringController.class);

    @Autowired
    private IGradebookFullService gradebookService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IEventNotificationService eventNotificationService;

    private static final DateFormat RELEASE_MARKS_SCHEDULE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @RequestMapping("")
    public String unspecified(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (log.isDebugEnabled()) {
	    log.debug("Getting gradebook for lesson " + lessonId);
	}
	UserDTO user = getUser();
	if (user == null) {
	    log.error("User missing from session. ");
	    return "error";
	}
	if (!securityService.isLessonMonitor(lessonId, user.getUserID(), "get lesson gradebook", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Lesson lesson = lessonService.getLesson(lessonId);
	boolean marksReleased = lesson.getMarksReleased();
	LessonDetailsDTO lessonDetatilsDTO = lesson.getLessonDetails();
	request.setAttribute("lessonDetails", lessonDetatilsDTO);
	request.setAttribute("marksReleased", marksReleased);

	List<String[]> weights = gradebookService.getWeights(lesson.getLearningDesign());
	if (weights.size() > 0) {
	    request.setAttribute("weights", weights);
	}

	return "gradebookMonitor";
    }

    @RequestMapping("/courseMonitor")
    public String courseMonitor(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	if (log.isDebugEnabled()) {
	    log.debug("Getting gradebook for organisation " + organisationID);
	}

	UserDTO user = getUser();
	if (user == null) {
	    log.error("User missing from session. ");
	    return "error";
	}
	if (!securityService.hasOrgRole(organisationID, user.getUserID(),
		new String[] { Role.GROUP_MANAGER, Role.MONITOR }, "get course gradebook page", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a course manager in the organisation");
	    return null;
	}

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationID);
	request.setAttribute("organisationID", organisationID);
	request.setAttribute("organisationName", organisation.getName());

	return "gradebookCourseMonitor";
    }

    /**
     * Updates a user's mark or feedback for an entire lesson.
     */
    @RequestMapping(path = "/updateUserLessonGradebookData", method = RequestMethod.POST)
    public void updateUserLessonGradebookData(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "update lesson gradebook", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	Integer userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_ID);
	User learner = (User) userManagementService.findById(User.class, userID);
	if (learner == null) {
	    log.error("User with ID " + userID + " could not be found to update his lesson gradebook");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User could not be found");
	}

	String markStr = WebUtil.readStrParam(request, GradebookConstants.PARAM_MARK, true);
	String feedback = WebUtil.readStrParam(request, GradebookConstants.PARAM_FEEDBACK, true);
	Lesson lesson = lessonService.getLesson(lessonID);
	if ((markStr != null) && !markStr.equals("")) {
	    Double mark = Double.parseDouble(markStr);
	    gradebookService.updateUserLessonGradebookMark(lesson, learner, mark);
	}
	if (feedback != null) {
	    gradebookService.updateUserLessonGradebookFeedback(lesson, learner, feedback);
	}
    }

    /**
     * Updates a user's mark or feedback for an activity, then aggregates their total lesson mark.
     */
    @RequestMapping(path = "/updateUserActivityGradebookData", method = RequestMethod.POST)
    @ResponseBody
    public void updateUserActivityGradebookData(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "update activity gradebook", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);

	Long activityID = null;
	Integer userID = null;

	// Fetch the id based on which grid it came from
	if (view == GBGridView.MON_ACTIVITY) {
	    String rowID = WebUtil.readStrParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	    activityID = getActivityFromParameter(rowID);
	    userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_ID);
	} else if (view == GBGridView.MON_USER) {
	    String rowID = WebUtil.readStrParam(request, GradebookConstants.PARAM_ID);
	    activityID = getActivityFromParameter(rowID);
	    userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);
	}

	Activity activity = gradebookService.getActivityById(activityID);
	if ((activity == null) || !activity.isToolActivity()) {
	    log.error("Activity with ID " + activityID + " could not be found or it is not a Tool Activity");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong activity");
	}

	User learner = (User) userManagementService.findById(User.class, userID);
	if (learner == null) {
	    log.error("User with ID " + userID + " could not be found to update his activity gradebook");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User could not be found");
	}

	String markStr = WebUtil.readStrParam(request, GradebookConstants.PARAM_MARK, true);
	String feedback = WebUtil.readStrParam(request, GradebookConstants.PARAM_FEEDBACK, true);
	Lesson lesson = lessonService.getLesson(lessonID);
	if ((markStr != null) && !markStr.equals("")) {
	    Double mark = Double.parseDouble(markStr);
	    gradebookService.updateGradebookUserActivityMark(lesson, learner, activity, mark, true, true);
	}

	if (feedback != null) {
	    gradebookService.updateGradebookUserActivityFeedback(activity, learner, feedback);
	}

    }

    private Long getActivityFromParameter(String rowID) {
	Long activityID;
	// Splitting the rowID param to get the activity/group id pair
	String[] split = rowID.split("_");
	if (split.length == 2) {
	    activityID = Long.parseLong(split[0]);
	} else {
	    activityID = Long.parseLong(rowID);
	}
	return activityID;
    }

    /**
     * Toggles the release mark flag for a lesson.
     */
    @RequestMapping(path = "/toggleReleaseMarks", method = RequestMethod.POST)
    @ResponseBody
    public String toggleReleaseMarks(@RequestParam long lessonID, HttpServletResponse response) throws IOException {
	if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "toggle release marks", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	gradebookService.toggleMarksReleased(lessonID);
	return "success";
    }

    @RequestMapping("/displayReleaseMarksPanel")
    public String displayReleaseMarksPanel(@RequestParam long lessonID, Model model) {
	Lesson lesson = lessonService.getLesson(lessonID);
	if (lesson.getMarksReleased()) {
	    model.addAttribute("marksReleased", true);
	} else {
	    model.addAttribute("marksReleased", false);

	    Map<String, Object> scheduleData = gradebookService.getReleaseMarksSchedule(lessonID,
		    getUser().getUserID());
	    if (scheduleData != null) {
		Date scheduleDate = (Date) scheduleData.get("userTimeZoneScheduleDate");
		if (scheduleDate != null) {
		    model.addAttribute("releaseMarksScheduleDate",
			    RELEASE_MARKS_SCHEDULE_DATE_FORMAT.format(scheduleDate));
		    model.addAttribute("releaseMarksSendEmails", scheduleData.get("sendEmails"));
		}
	    }
	}

	return "releaseLessonMarks";
    }

    @RequestMapping("/getReleaseMarksEmailContent")
    @ResponseBody
    public String getReleaseMarksEmailContent(@RequestParam long lessonID, @RequestParam int userID) {
	return gradebookService.getReleaseMarksEmailContent(lessonID, userID);
    }

    @RequestMapping("/sendReleaseMarksEmails")
    @ResponseBody
    public String sendReleaseMarksEmails(@RequestParam long lessonID,
	    @RequestParam(name = "includedLearners") String includedLearnersString)
	    throws JsonProcessingException, IOException {

	if (StringUtils.isBlank(includedLearnersString)) {
	    return "list of recipients is empty";
	}
	ArrayNode includedLearners = JsonUtil.readArray(includedLearnersString);
	try {
	    Set<Integer> recipientIDs = new HashSet<>();
	    // we send emails only to selected learners
	    for (int learnerIndex = 0; learnerIndex < includedLearners.size(); learnerIndex++) {
		recipientIDs.add(includedLearners.get(learnerIndex).asInt());
	    }

	    if (recipientIDs.isEmpty()) {
		return "list of recipients it empty";
	    }

	    gradebookService.sendReleaseMarksEmails(lessonID, recipientIDs, eventNotificationService);

	} catch (Exception e) {
	    log.error("Error while sending emails with released marks for lesson with ID " + lessonID, e);
	    return e.getMessage();
	}

	return "success";
    }

    @RequestMapping(path = "/scheduleReleaseMarks", method = RequestMethod.POST)
    @ResponseBody
    public String scheduleReleaseMarks(@RequestParam long lessonID, @RequestParam boolean sendEmails,
	    @RequestParam(name = "scheduleDate", required = false) String scheduleDateString)
	    throws ParseException, SchedulerException {
	try {
	    Date scheduleDate = null;
	    if (StringUtils.isNotBlank(scheduleDateString)) {
		scheduleDate = RELEASE_MARKS_SCHEDULE_DATE_FORMAT.parse(scheduleDateString);

		// set seconds and miliseconds to 0
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(scheduleDate);
		calendarDate.set(Calendar.SECOND, 0);
		calendarDate.set(Calendar.MILLISECOND, 0);
		scheduleDate = calendarDate.getTime();

		// if schedule is less then in 5 seconds or in the past, refuse it
		if (scheduleDate.getTime() - 5000 < new Date().getTime()) {
		    return "schedule date must be in future";
		}
	    }
	    gradebookService.scheduleReleaseMarks(lessonID, scheduleDate == null ? null : getUser().getUserID(),
		    sendEmails, scheduleDate);
	    return "success";
	} catch (Exception e) {
	    return e.getMessage();
	}
    }

    /**
     * Exports Lesson Gradebook into excel.
     */
    @RequestMapping("/exportExcelLessonGradebook")
    @ResponseBody
    public void exportExcelLessonGradebook(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "export lesson gradebook spreadsheet",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	if (log.isDebugEnabled()) {
	    log.debug("Exporting to a spreadsheet lesson: " + lessonID);
	}
	Lesson lesson = lessonService.getLesson(lessonID);
	String fileName = lesson.getLessonName().replaceAll(" ", "_") + ".xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	log.debug("Exporting to a spreadsheet gradebook lesson: " + lessonID);
	ServletOutputStream out = response.getOutputStream();

	List<ExcelSheet> sheets = gradebookService.exportLessonGradebook(lesson);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	ExcelUtil.createExcel(out, sheets, gradebookService.getMessage("gradebook.export.dateheader"), true);
    }

    /**
     * Exports Course Gradebook into excel.
     */
    @RequestMapping("/exportExcelCourseGradebook")
    @ResponseBody
    public void exportExcelCourseGradebook(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	UserDTO user = getUser();
	if (!securityService.hasOrgRole(organisationID, user.getUserID(), new String[] { Role.GROUP_MANAGER },
		"get course gradebook spreadsheet", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	}

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationID);
	if (log.isDebugEnabled()) {
	    log.debug("Exporting to a spreadsheet course: " + organisationID);
	}
	List<ExcelSheet> sheets = gradebookService.exportCourseGradebook(user.getUserID(), organisationID);

	String fileName = organisation.getName().replaceAll(" ", "_") + ".xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, gradebookService.getMessage("gradebook.export.dateheader"), true);
    }

    /**
     * Exports selected lessons Gradebook into excel.
     */
    @RequestMapping("/exportExcelSelectedLessons")
    @ResponseBody
    public void exportExcelSelectedLessons(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	UserDTO user = getUser();
	if (!securityService.isGroupMonitor(organisationID, user.getUserID(),
		"export selected lessons gradebook spreadsheet", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	}

	boolean simplified = WebUtil.readBooleanParam(request, "simplified", false);

	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationID);
	String[] lessonIds = request.getParameterValues(AttributeNames.PARAM_LESSON_ID);
	if (log.isDebugEnabled()) {
	    log.debug("Exporting to a spreadsheet lessons " + Arrays.toString(lessonIds) + " from course: "
		    + organisationID);
	}
	List<ExcelSheet> sheets = gradebookService.exportSelectedLessonsGradebook(user.getUserID(), organisationID,
		lessonIds, simplified);

	String fileName = organisation.getName().replaceAll(" ", "_") + ".xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, null, false);
    }

    /**
     * Get the raw marks for display in a histogram.
     */
    @RequestMapping("/getMarkChartData")
    @ResponseBody
    public String getMarkChartData(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "export lesson gradebook spreadsheet",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	List<Number> results = gradebookService.getMarksArray(lessonID);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (results != null) {
	    responseJSON.set("data", JsonUtil.readArray(results));
	} else {
	    responseJSON.set("data", JsonUtil.readArray(new Float[0]));
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

}