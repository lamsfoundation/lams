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


package org.lamsfoundation.lams.gradebook.web.action;

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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
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
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author lfoxton
 *
 *         Handles the monitor interfaces for gradebook
 *
 *         This is where marking for an activity/lesson takes place
 */
public class GradebookMonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(GradebookMonitoringAction.class);

    private static IGradebookService gradebookService;
    private static IUserManagementService userService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	try {
	    Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    if (GradebookMonitoringAction.log.isDebugEnabled()) {
		GradebookMonitoringAction.log.debug("Getting gradebook for lesson " + lessonId);
	    }
	    UserDTO user = getUser();
	    if (user == null) {
		GradebookMonitoringAction.log.error("User missing from session. ");
		return mapping.findForward("error");
	    }
	    if (!getSecurityService().isLessonMonitor(lessonId, user.getUserID(), "get lesson gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    Lesson lesson = getLessonService().getLesson(lessonId);
	    boolean marksReleased = (lesson.getMarksReleased() != null) && lesson.getMarksReleased();
	    LessonDetailsDTO lessonDetatilsDTO = lesson.getLessonDetails();
	    request.setAttribute("lessonDetails", lessonDetatilsDTO);
	    request.setAttribute("marksReleased", marksReleased);
	    
	    request.setAttribute("isInTabs", WebUtil.readBooleanParam(request, "isInTabs", false));

	    return mapping.findForward("monitorgradebook");
	} catch (Exception e) {
	    GradebookMonitoringAction.log.error("Failed to load lesson gradebook", e);
	    return mapping.findForward("error");
	}
    }

    public ActionForward courseMonitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	try {
	    Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	    if (GradebookMonitoringAction.log.isDebugEnabled()) {
		GradebookMonitoringAction.log.debug("Getting gradebook for organisation " + organisationID);
	    }

	    UserDTO user = getUser();
	    if (user == null) {
		GradebookMonitoringAction.log.error("User missing from session. ");
		return mapping.findForward("error");
	    }
	    if (!getSecurityService().hasOrgRole(organisationID, user.getUserID(),
		    new String[] { Role.GROUP_MANAGER, Role.GROUP_ADMIN }, "get course gradebook page", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
		return null;
	    }

	    Organisation organisation = (Organisation) getUserService().findById(Organisation.class, organisationID);
	    request.setAttribute("organisationID", organisationID);
	    request.setAttribute("organisationName", organisation.getName());

	    return mapping.findForward("monitorcoursegradebook");
	} catch (Exception e) {
	    GradebookMonitoringAction.log.error("Failed to load course gradebook", e);
	    return mapping.findForward("error");
	}
    }

    /**
     * Updates a user's mark or feedback for an entire lesson
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward updateUserLessonGradebookData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonID, getUser().getUserID(), "update lesson gradebook", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Integer userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_ID);
	User learner = (User) getUserService().findById(User.class, userID);
	if (learner == null) {
	    GradebookMonitoringAction.log
		    .error("User with ID " + userID + " could not be found to update his lesson gradebook");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User could not be found");
	    return null;
	}

	String markStr = WebUtil.readStrParam(request, GradebookConstants.PARAM_MARK, true);
	String feedback = WebUtil.readStrParam(request, GradebookConstants.PARAM_FEEDBACK, true);
	Lesson lesson = getLessonService().getLesson(lessonID);
	if ((markStr != null) && !markStr.equals("")) {
	    Double mark = Double.parseDouble(markStr);
	    getGradebookService().updateUserLessonGradebookMark(lesson, learner, mark);
	}
	if (feedback != null) {
	    getGradebookService().updateUserLessonGradebookFeedback(lesson, learner, feedback);
	}

	return null;
    }

    /**
     * Updates a user's mark or feedback for an activity, then aggregates their total lesson mark
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward updateUserActivityGradebookData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonID, getUser().getUserID(), "update activity gradebook",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);

	Long activityID = null;
	Integer userID = null;

	// Fetch the id based on which grid it came from
	if (view == GBGridView.MON_ACTIVITY) {
	    String rowID = WebUtil.readStrParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	    // Splitting the rowID param to get the activity/group id pair
	    String[] split = rowID.split("_");
	    if (split.length == 2) {
		activityID = Long.parseLong(split[0]);
	    } else {
		activityID = Long.parseLong(rowID);
	    }

	    userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_ID);
	} else if (view == GBGridView.MON_USER) {
	    activityID = WebUtil.readLongParam(request, GradebookConstants.PARAM_ID);
	    userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);
	}

	Activity activity = getGradebookService().getActivityById(activityID);
	if ((activity == null) || !activity.isToolActivity()) {
	    GradebookMonitoringAction.log
		    .error("Activity with ID " + activityID + " could not be found or it is not a Tool Activity");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Wrong activity");
	    return null;
	}

	User learner = (User) getUserService().findById(User.class, userID);
	if (learner == null) {
	    GradebookMonitoringAction.log
		    .error("User with ID " + userID + " could not be found to update his activity gradebook");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User could not be found");
	    return null;
	}

	String markStr = WebUtil.readStrParam(request, GradebookConstants.PARAM_MARK, true);
	String feedback = WebUtil.readStrParam(request, GradebookConstants.PARAM_FEEDBACK, true);
	Lesson lesson = getLessonService().getLesson(lessonID);
	if ((markStr != null) && !markStr.equals("")) {
	    Double mark = Double.parseDouble(markStr);
	    getGradebookService().updateUserActivityGradebookMark(lesson, learner, activity, mark, true, true);
	}

	if (feedback != null) {
	    getGradebookService().updateUserActivityGradebookFeedback(activity, learner, feedback);
	}

	return null;
    }

    /**
     * Toggles the release mark flag for a lesson
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward toggleReleaseMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonID, getUser().getUserID(), "toggle release marks", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	getGradebookService().toggleMarksReleased(lessonID);
	writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_PLAIN, LamsDispatchAction.ENCODING_UTF8,
		"success");
	return null;
    }

    /**
     * Exports Lesson Gradebook into excel.
     */
    public ActionForward exportExcelLessonGradebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonID, getUser().getUserID(),
		"export lesson gradebook spreadsheet", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	if (GradebookMonitoringAction.log.isDebugEnabled()) {
	    GradebookMonitoringAction.log.debug("Exporting to a spreadsheet lesson: " + lessonID);
	}
	Lesson lesson = getLessonService().getLesson(lessonID);
	String fileName = lesson.getLessonName().replaceAll(" ", "_") + ".xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	GradebookMonitoringAction.log.debug("Exporting to a spreadsheet gradebook lesson: " + lessonID);
	ServletOutputStream out = response.getOutputStream();

	LinkedHashMap<String, ExcelCell[][]> dataToExport = getGradebookService().exportLessonGradebook(lesson);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	ExcelUtil.createExcel(out, dataToExport, getGradebookService().getMessage("gradebook.export.dateheader"), true);

	return null;
    }

    /**
     * Exports Course Gradebook into excel.
     */
    public ActionForward exportExcelCourseGradebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	UserDTO user = getUser();
	if (!getSecurityService().hasOrgRole(organisationID, user.getUserID(),
		new String[] { Role.GROUP_MANAGER, Role.GROUP_ADMIN }, "get course gradebook spreadsheet", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	Organisation organisation = (Organisation) getUserService().findById(Organisation.class, organisationID);
	if (GradebookMonitoringAction.log.isDebugEnabled()) {
	    GradebookMonitoringAction.log.debug("Exporting to a spreadsheet course: " + organisationID);
	}
	LinkedHashMap<String, ExcelCell[][]> dataToExport = getGradebookService()
		.exportCourseGradebook(user.getUserID(), organisationID);

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
	ExcelUtil.createExcel(out, dataToExport, getGradebookService().getMessage("gradebook.export.dateheader"), true);

	return null;
    }

    /**
     * Exports selected lessons Gradebook into excel.
     */
    public ActionForward exportExcelSelectedLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	UserDTO user = getUser();
	if (!getSecurityService().isGroupMonitor(organisationID, user.getUserID(),
		"export selected lessons gradebook spreadsheet", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}
	
	boolean simplified = WebUtil.readBooleanParam(request, "simplified", false);

	Organisation organisation = (Organisation) getUserService().findById(Organisation.class, organisationID);
	String[] lessonIds = request.getParameterValues(AttributeNames.PARAM_LESSON_ID);
	if (GradebookMonitoringAction.log.isDebugEnabled()) {
	    GradebookMonitoringAction.log.debug("Exporting to a spreadsheet lessons " + Arrays.toString(lessonIds)
		    + " from course: " + organisationID);
	}
	LinkedHashMap<String, ExcelCell[][]> dataToExport = getGradebookService()
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

	return null;
    }
    
    /**
     * Get the raw marks for display in a histogram.
     */
    public ActionForward getMarkChartData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonID, getUser().getUserID(),
		"export lesson gradebook spreadsheet", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	List<Number> results = getGradebookService().getMarksArray(lessonID);
	
	JSONObject responseJSON = new JSONObject();
	if ( results != null )
	    responseJSON.put("data", results);
	else 
	    responseJSON.put("data", new Float[0]);

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;

    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private IUserManagementService getUserService() {
	if (GradebookMonitoringAction.userService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GradebookMonitoringAction.userService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return GradebookMonitoringAction.userService;
    }

    private ILessonService getLessonService() {
	if (GradebookMonitoringAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GradebookMonitoringAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return GradebookMonitoringAction.lessonService;
    }

    private IGradebookService getGradebookService() {
	if (GradebookMonitoringAction.gradebookService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GradebookMonitoringAction.gradebookService = (IGradebookService) ctx.getBean("gradebookService");
	}
	return GradebookMonitoringAction.gradebookService;
    }

    private ISecurityService getSecurityService() {
	if (GradebookMonitoringAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    GradebookMonitoringAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return GradebookMonitoringAction.securityService;
    }
}