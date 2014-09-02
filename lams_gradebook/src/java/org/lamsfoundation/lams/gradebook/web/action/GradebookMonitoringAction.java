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

/* $Id$ */
package org.lamsfoundation.lams.gradebook.web.action;

import java.util.LinkedHashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.gradebook.util.GradebookUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
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
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author lfoxton
 * 
 *         Handles the monitor interfaces for gradebook
 * 
 *         This is where marking for an activity/lesson takes place
 * 
 * 
 * @struts.action path="/gradebookMonitoring" parameter="dispatch" scope="request"
 *                name="monitoringForm" validate="false"
 * 
 * @struts:action-forward name="monitorgradebook" path="/gradebookMonitor.jsp"
 * @struts:action-forward name="monitorcoursegradebook" path="/gradebookCourseMonitor.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 */
public class GradebookMonitoringAction extends LamsDispatchAction {

	private static Logger logger = Logger.getLogger(GradebookMonitoringAction.class);

	private static IGradebookService gradebookService;
	private static IUserManagementService userService;
	private static ILessonService lessonService;

	@SuppressWarnings("unchecked")
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			initServices();
			Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
			logger.debug("request monitorGradebook for lesson: " + lessonId.toString());
			UserDTO user = getUser();
			if (user == null) {
				logger.error("User missing from session. ");
				return mapping.findForward("error");
			} else {
				Lesson lesson = lessonId != null ? lessonService.getLesson(lessonId) : null;
				if (lesson == null) {
					logger.error("Lesson " + lessonId + " does not exist. Unable to monitor lesson");
					return mapping.findForward("error");
				}

				if (lesson.getLessonClass() == null || !lesson.getLessonClass().isStaffMember(getRealUser(user))) {
					logger.error("User " + user.getLogin()
							+ " is not a monitor in the requested lesson. Cannot access the lesson for monitor.");
					return displayMessage(mapping, request, "error.authorisation");
				}

				logger.debug("user is staff");

				boolean marksReleased = lesson.getMarksReleased() != null && lesson.getMarksReleased();

				LessonDetailsDTO lessonDetatilsDTO = lesson.getLessonDetails();
				request.setAttribute("lessonDetails", lessonDetatilsDTO);
				request.setAttribute("marksReleased", marksReleased);

				return mapping.findForward("monitorgradebook");
			}
		} catch (Exception e) {
			logger.error("Failed to load gradebook monitor", e);
			return mapping.findForward("error");
		}
	}

	@SuppressWarnings("unchecked")
	public ActionForward courseMonitor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			initServices();
			Integer oranisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);

			logger.debug("request monitorGradebook for organisation: " + oranisationID.toString());
			UserDTO user = getUser();
			if (user == null) {
				logger.error("User missing from session. ");
				return mapping.findForward("error");
			} else {

				Organisation organisation = (Organisation) userService.findById(Organisation.class, oranisationID);
				if (organisation == null) {
					logger.error("Organisation " + oranisationID + " does not exist. Unable to load gradebook");
					return mapping.findForward("error");
				}

				// Validate whether this user is a monitor for this organisation
				if (!userService.isUserInRole(user.getUserID(), oranisationID, Role.MONITOR)) {
					logger.error("User " + user.getLogin()
							+ " is not a monitor in the requested course. Cannot access the course for gradebook.");
					return displayMessage(mapping, request, "error.authorisation");
				}

				logger.debug("user is staff");

				request.setAttribute("organisationID", oranisationID);
				request.setAttribute("organisationName", organisation.getName());

				return mapping.findForward("monitorcoursegradebook");
			}
		} catch (Exception e) {
			logger.error("Failed to load gradebook monitor", e);
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
		initServices();
		Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
		Integer userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_ID);
		String markStr = WebUtil.readStrParam(request, GradebookConstants.PARAM_MARK, true);
		String feedback = WebUtil.readStrParam(request, GradebookConstants.PARAM_FEEDBACK, true);
		Lesson lesson = lessonService.getLesson(lessonID);
		User learner = (User) userService.findById(User.class, userID);

		if (lesson != null || learner != null) {

			if (markStr != null && !markStr.equals("")) {
				Double mark = Double.parseDouble(markStr);
				gradebookService.updateUserLessonGradebookMark(lesson, learner, mark);
			}

			if (feedback != null) {
				gradebookService.updateUserLessonGradebookFeedback(lesson, learner, feedback);
			}

		} else {
			logger.error("No lesson could be found for: " + lessonID);
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
		initServices();
		Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

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

		String markStr = WebUtil.readStrParam(request, GradebookConstants.PARAM_MARK, true);
		String feedback = WebUtil.readStrParam(request, GradebookConstants.PARAM_FEEDBACK, true);

		Activity activity = (Activity) userService.findById(Activity.class, activityID);
		User learner = (User) userService.findById(User.class, userID);
		Lesson lesson = lessonService.getLesson(lessonID);

		if (lesson != null && activity != null && learner != null && activity.isToolActivity()) {

			if (markStr != null && !markStr.equals("")) {
				Double mark = Double.parseDouble(markStr);
				gradebookService.updateUserActivityGradebookMark(lesson, learner, activity, mark, true, true);
			}

			if (feedback != null) {
				gradebookService.updateUserActivityGradebookFeedback(activity, learner, feedback);
			}

		} else {
			logger.error("Lesson or activity missing for lesson: " + lessonID + "and activity: " + activityID);
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
	initServices();
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	
	gradebookService.toggleMarksReleased(lessonID);

	writeResponse(response, CONTENT_TYPE_TEXT_PLAIN, ENCODING_UTF8, "success");
	return null;
    }

    /**
     * Exports Lesson Gradebook into excel.
     */
    public ActionForward exportExcelLessonGradebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonID);

	if (lesson == null) {
	    String errorMsg = "Attempt to retrieve gradebook data for null lesson";
	    logger.error(errorMsg);
	    throw new Exception(errorMsg);
	}
	
	String fileName = lesson.getLessonName().replaceAll(" ", "_") + ".xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	logger.debug("Exporting to a spreadsheet gradebook lesson: " + lessonID);
	ServletOutputStream out = response.getOutputStream();

	LinkedHashMap<String, ExcelCell[][]> dataToExport = gradebookService.exportLessonGradebook(lesson);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	ExcelUtil.createExcel(out, dataToExport, gradebookService.getMessage("gradebook.export.dateheader"), true);

	return null;
    }
	
    /**
     * Exports Course Gradebook into excel.
     */
    public ActionForward exportExcelCourseGradebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();
	Integer oranisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	User user = getRealUser(getUser());

	Organisation organisation = (Organisation) userService.findById(Organisation.class, oranisationID);
	if (organisation == null || user == null) {
	    String errorMsg = "Organisation " + oranisationID + " does not exist or user is null. Unable to load gradebook";
	    logger.error(errorMsg);
	    throw new Exception(errorMsg);
	}
	
	Integer organisationId = organisation.getOrganisationId();
	logger.debug("Exporting to a spreadsheet course: " + organisationId);

	LinkedHashMap<String, ExcelCell[][]> dataToExport = gradebookService.exportCourseGradebook(user.getUserId(), organisationId);

	String fileName = organisation.getName().replaceAll(" ", "_") + ".xlsx";
	fileName  = FileUtil.encodeFilenameForDownload(request, fileName);
	
	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);	
	
	//set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie); 
	
	//Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, dataToExport, gradebookService.getMessage("gradebook.export.dateheader"), true);

	return null;
    }
    
    /**
     * Exports selected lessons Gradebook into excel.
     */
    public ActionForward exportExcelSelectedLessons(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	initServices();
	Integer oranisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	User user = getRealUser(getUser());

	Organisation organisation = (Organisation) userService.findById(Organisation.class, oranisationID);
	if (organisation == null || user == null) {
	    String errorMsg = "Organisation " + oranisationID + " does not exist or user is null. Unable to load gradebook";
	    logger.error(errorMsg);
	    throw new Exception(errorMsg);
	}
	
	Integer organisationId = organisation.getOrganisationId();
	logger.debug("Exporting to a spreadsheet course: " + organisationId);

	String[] lessonIds = request.getParameterValues(AttributeNames.PARAM_LESSON_ID);
	Assert.notNull(lessonIds);

	LinkedHashMap<String, ExcelCell[][]> dataToExport = gradebookService.exportSelectedLessonsGradebook(user.getUserId(), organisationId, lessonIds);

	String fileName = organisation.getName().replaceAll(" ", "_") + ".xlsx";
	fileName  = FileUtil.encodeFilenameForDownload(request, fileName);
	
	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);	
	
	//set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie); 
	
	//Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, dataToExport, null, false);

	return null;
    }

	private UserDTO getUser() {
		HttpSession ss = SessionManager.getSession();
		return (UserDTO) ss.getAttribute(AttributeNames.USER);
	}

	private User getRealUser(UserDTO dto) {
		return getUserService().getUserByLogin(dto.getLogin());
	}

	private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
		req.setAttribute("messageKey", messageKey);
		return mapping.findForward("message");
	}

	private void initServices() {
		getUserService();
		getLessonService();
		getGradebookService();
	}

	private IUserManagementService getUserService() {
		if (userService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
					.getServletContext());
			userService = (IUserManagementService) ctx.getBean("userManagementService");
		}
		return userService;
	}

	private ILessonService getLessonService() {
		if (lessonService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
					.getServletContext());
			lessonService = (ILessonService) ctx.getBean("lessonService");
		}
		return lessonService;
	}

	private IGradebookService getGradebookService() {
		if (gradebookService == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
					.getServletContext());
			gradebookService = (IGradebookService) ctx.getBean("gradebookService");
		}
		return gradebookService;
	}
}
