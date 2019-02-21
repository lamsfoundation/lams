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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.util.ExcelCell;
import org.lamsfoundation.lams.util.ExcelUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("")
    public String unspecified(HttpServletRequest request, HttpServletResponse response) throws Exception {
	try {
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
	    boolean marksReleased = (lesson.getMarksReleased() != null) && lesson.getMarksReleased();
	    LessonDetailsDTO lessonDetatilsDTO = lesson.getLessonDetails();
	    request.setAttribute("lessonDetails", lessonDetatilsDTO);
	    request.setAttribute("marksReleased", marksReleased);

	    List<String[]> weights = gradebookService.getWeights(lesson.getLearningDesign());
	    if (weights.size() > 0) {
		request.setAttribute("weights", weights);
	    }

	    request.setAttribute("isInTabs", WebUtil.readBooleanParam(request, "isInTabs", false));

	    return "gradebookMonitor";
	} catch (Exception e) {
	    log.error("Failed to load lesson gradebook", e);
	    return "error";
	}
    }

    @RequestMapping("/courseMonitor")
    public String courseMonitor(HttpServletRequest request, HttpServletResponse response) throws Exception {

	try {
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
		response.sendError(HttpServletResponse.SC_FORBIDDEN,
			"User is not a course manager in the organisation");
		return null;
	    }

	    Organisation organisation = (Organisation) userManagementService.findById(Organisation.class,
		    organisationID);
	    request.setAttribute("organisationID", organisationID);
	    request.setAttribute("organisationName", organisation.getName());

	    return "gradebookCourseMonitor";
	} catch (Exception e) {
	    log.error("Failed to load course gradebook", e);
	    return "error";
	}
    }

    /**
     * Updates a user's mark or feedback for an entire lesson.
     */
    @RequestMapping(path = "/updateUserLessonGradebookData", method = RequestMethod.POST)
    public void updateUserLessonGradebookData(HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
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
	    throws Exception {
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
    @RequestMapping("/toggleReleaseMarks")
    @ResponseBody
    public String toggleReleaseMarks(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "toggle release marks", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	}

	gradebookService.toggleMarksReleased(lessonID);
	response.setContentType("text/plain; charset=utf-8");
	return "success";

    }

    /**
     * Exports Lesson Gradebook into excel.
     */
    @RequestMapping("/exportExcelLessonGradebook")
    @ResponseBody
    public void exportExcelLessonGradebook(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

	LinkedHashMap<String, ExcelCell[][]> dataToExport = gradebookService.exportLessonGradebook(lesson);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	ExcelUtil.createExcel(out, dataToExport, gradebookService.getMessage("gradebook.export.dateheader"), true);

    }

    /**
     * Exports Course Gradebook into excel.
     */
    @RequestMapping("/exportExcelCourseGradebook")
    @ResponseBody
    public void exportExcelCourseGradebook(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	LinkedHashMap<String, ExcelCell[][]> dataToExport = gradebookService.exportCourseGradebook(user.getUserID(),
		organisationID);

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
	ExcelUtil.createExcel(out, dataToExport, gradebookService.getMessage("gradebook.export.dateheader"), true);

    }

    /**
     * Exports selected lessons Gradebook into excel.
     */
    @RequestMapping("/exportExcelSelectedLessons")
    @ResponseBody
    public void exportExcelSelectedLessons(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
	LinkedHashMap<String, ExcelCell[][]> dataToExport = gradebookService
		.exportSelectedLessonsGradebook(user.getUserID(), organisationID, lessonIds, simplified);

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
	ExcelUtil.createExcel(out, dataToExport, null, false);

    }

    /**
     * Get the raw marks for display in a histogram.
     */
    @RequestMapping("/getMarkChartData")
    @ResponseBody
    public String getMarkChartData(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

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