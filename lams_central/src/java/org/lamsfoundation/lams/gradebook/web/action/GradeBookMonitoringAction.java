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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.service.IGradeBookService;
import org.lamsfoundation.lams.gradebook.util.GradeBookConstants;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author lfoxton
 * 
 * Handles the monitor interface for gradebook
 * 
 * This is where marking for an activity/lesson takes place
 * 
 * 
 * @struts.action path="/gradebook/gradebookMonitoring" parameter="dispatch"
 *                scope="request" name="monitoringForm" validate="false"
 * 
 * @struts:action-forward name="monitorgradebook"
 *                        path="/gradebook/gradeBookMonitor.jsp"
 * @struts:action-forward name="monitorcoursegradebook"
 *                        path="/gradebook/gradeBookCourseMonitor.jsp"
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 */
public class GradeBookMonitoringAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(GradeBookMonitoringAction.class);

    private static IGradeBookService gradeBookService;
    private static IUserManagementService userService;
    private static ILessonService lessonService;
    private static IMonitoringService monitoringService;

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

		LessonDetailsDTO lessonDetatilsDTO = lesson.getLessonDetails();
		request.setAttribute("lessonDetails", lessonDetatilsDTO);

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
		
		Organisation organisation = (Organisation)userService.findById(Organisation.class, oranisationID);
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
    public ActionForward updateUserLessonGradeBookData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	initServices();
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer userID = WebUtil.readIntParam(request, "id");
	String markStr = WebUtil.readStrParam(request, "mark", true);
	String feedback =  WebUtil.readStrParam(request, "feedback", true);
	Lesson lesson = lessonService.getLesson(lessonID);
	User learner = (User)userService.findById(User.class, userID);

	if (lesson != null || learner != null) {
	    
	    if (markStr != null && !markStr.equals("")) {
		Double mark = Double.parseDouble(markStr);
		gradeBookService.updateUserLessonGradeBookMark(lesson, learner, mark);
	    }
	    
	    if (feedback != null) {
		gradeBookService.updateUserLessonGradeBookFeedback(lesson, learner, feedback);
	    }

	} else {
	    logger.error("No lesson could be found for: " + lessonID);
	}
	return null;
    }

    /**
     * Updates a user's mark or feedback for an activity, 
     * then aggregates their total lesson mark
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward updateUserActivityGradeBookData(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	initServices();
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	
	String method = WebUtil.readStrParam(request, "method");

	Long activityID = null;
	Integer userID = null;
	
	// Fetch the id based on which grid it came from
	if (method.equals("activityView")) {
	    activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	    userID = WebUtil.readIntParam(request, "id");
	} else if (method.equals("userView")) {
	    activityID = WebUtil.readLongParam(request, "id");
	    userID = WebUtil.readIntParam(request, "userID");
	}

	String markStr = WebUtil.readStrParam(request, "mark", true);
	String feedback =  WebUtil.readStrParam(request, "feedback", true);

	Activity activity = monitoringService.getActivityById(activityID);
	User learner = (User)userService.findById(User.class, userID);
	Lesson lesson = lessonService.getLesson(lessonID);

	if (lesson != null && activity != null && learner != null && activity.isToolActivity()) {
	    
	    if (markStr != null && !markStr.equals("")) {
		Double mark = Double.parseDouble(markStr);
		gradeBookService.updateUserActivityGradeBookMark(lesson, learner, activity, mark);
	    }
	    
	    if (feedback != null) {
		gradeBookService.updateUserActivityGradeBookFeedback(activity, learner, feedback);
	    }
	    
	} else {
	    logger.error("Lesson or activity missing for lesson: " + lessonID + "and activity: " + activityID);
	}
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
	getMonitoringServiceService();
	getGradeBookService();
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
    
    private IMonitoringService getMonitoringServiceService() {
	if (monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    monitoringService = (IMonitoringService) ctx.getBean("monitoringService");
	}
	return monitoringService;
    }
    
    private IGradeBookService getGradeBookService() {
	if (gradeBookService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    gradeBookService = (IGradeBookService) ctx.getBean("gradeBookService");
	}
	return gradeBookService;
    }
}
