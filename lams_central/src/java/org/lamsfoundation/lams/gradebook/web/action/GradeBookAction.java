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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRowDTO;
import org.lamsfoundation.lams.gradebook.service.IGradeBookService;
import org.lamsfoundation.lams.gradebook.util.GradeBookConstants;
import org.lamsfoundation.lams.gradebook.util.GradeBookUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
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
 * Handles the general requests for content in gradebook
 * 
 * 
 * @struts.action path="/gradebook/gradebook" parameter="dispatch"
 *                scope="request" validate="false"
 * 
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 */
public class GradeBookAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(GradeBookAction.class);

    private static IGradeBookService gradeBookService;
    private static IUserManagementService userService;
    private static ILessonService lessonService;
    private static IMonitoringService monitoringService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return null;
    }

    /**
     * Returns an xml representation of the activity grid for gradebook
     * 
     * This has two modes, userView and activityView
     * 
     * User view will get the grid data for a specified user, which is all their
     * activity marks/outputs etc
     * 
     * Activity view will get the grid data for all activities, without user
     * info, instead there is an average mark for each activity
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings(value = { "unchecked", "unused" })
    public ActionForward getActivityGridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	initServices();

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradeBookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradeBookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradeBookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_STRING, true);
	String view = WebUtil.readStrParam(request, GradeBookConstants.PARAM_METHOD);

	// Getting the lesson id
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	Lesson lesson = lessonService.getLesson(lessonID);

	if (lesson != null) {

	    List<GradeBookGridRowDTO> gradeBookActivityDTOs = new ArrayList<GradeBookGridRowDTO>();

	    // Get the user gradebook list from the db
	    // A slightly different list is needed for userview or activity view
	    if (view.equals(GradeBookConstants.VIEW_USER)) {
		Integer userID = WebUtil.readIntParam(request, GradeBookConstants.PARAM_USERID);
		User learner = (User) userService.findById(User.class, userID);
		if (learner != null) {
		    gradeBookActivityDTOs = gradeBookService.getGBActivityRowsForLearner(lesson, learner);
		} else {
		    // return null and the grid will report the error
		    logger.error("No learner found for: " + userID);
		    return null;
		}
	    } else if (view.equals(GradeBookConstants.VIEW_ACTIVITY)) {
		gradeBookActivityDTOs = gradeBookService.getGBActivityRowsForLesson(lesson);
	    }

	    if (sortBy == null) {
		sortBy = GradeBookConstants.PARAM_ID;
	    }

	    String ret = GradeBookUtil.toGridXML(gradeBookActivityDTOs, view, sortBy, isSearch, searchField,
		    searchOper, searchString, sortOrder, rowLimit, page);

	    response.setContentType(GradeBookConstants.CONTENT_TYPE_TEXTXML);
	    PrintWriter out = response.getWriter();
	    out.print(ret);
	} else {
	    logger.error("No lesson could be found for: " + lessonID);
	}

	return null;
    }

    /**
     * Returns an xml representation of the user grid for gradebook
     * 
     * This has three modes: userView, activityView and courseMonitorView
     * 
     * User view will get all the learners in a lesson and print their gradebook
     * data with their mark for the entire lesson
     * 
     * Activity view will take an extra parameter (activityID) and instead show
     * the user's mark just for one activity
     * 
     * Course monitor view gets the same as the user view, but the link is set
     * to the lesson level gradebook instead of learner
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward getUserGridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	initServices();

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradeBookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradeBookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradeBookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_STRING, true);
	String view = WebUtil.readStrParam(request, GradeBookConstants.PARAM_METHOD);

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	Lesson lesson = lessonService.getLesson(lessonID);

	if (lesson != null) {

	    // Get the user gradebook list from the db
	    List<GBUserGridRowDTO> gradeBookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	    if (view.equals(GradeBookConstants.VIEW_USER) || view.equals("courseMonitorView")) {
		gradeBookUserDTOs = gradeBookService.getGBUserRowsForLesson(lesson);
	    } else if (view.equals(GradeBookConstants.VIEW_ACTIVITY)) {
		Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

		Activity activity = monitoringService.getActivityById(activityID);
		if (activity != null && activity instanceof ToolActivity) {
		    gradeBookUserDTOs = gradeBookService.getGBUserRowsForActivity(lesson, (ToolActivity)activity);
		} else {
		    // return null and the grid will report an error
		    logger.error("No activity found for: " + activityID);
		    return null;
		}
	    }
	    
	    String ret = GradeBookUtil.toGridXML(gradeBookUserDTOs, view, sortBy, isSearch, searchField, searchOper,
		    searchString, sortOrder, rowLimit, page);

	    response.setContentType(GradeBookConstants.CONTENT_TYPE_TEXTXML);
	    PrintWriter out = response.getWriter();
	    out.print(ret);
	} else {
	    logger.error("No lesson could be found for: " + lessonID);
	}

	return null;
    }

    /**
     * Returns an xml representation of the lesson grid for a course for
     * gradebook
     * 
     * This has two modes, learnerView and monitorView
     * 
     * Learner view will get the data specific to one user
     * 
     * Monitor will get the data average for whole lessons
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward getCourseGridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	initServices();

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradeBookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradeBookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradeBookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SEARCH_STRING, true);
	String view = WebUtil.readStrParam(request, GradeBookConstants.PARAM_METHOD);

	User user = getRealUser();

	Integer courseID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Organisation organisation = (Organisation) userService.findById(Organisation.class, courseID);

	if (organisation != null && user != null) {

	    Set<Lesson> lessons = (Set<Lesson>) organisation.getLessons();
	    if (lessons != null) {

		List<GBLessonGridRowDTO> gradeBookLessonDTOs = new ArrayList<GBLessonGridRowDTO>();

		gradeBookLessonDTOs = gradeBookService.getGBLessonRows(organisation, user);

		if (sortBy == null) {
		    sortBy = GradeBookConstants.PARAM_ID;
		}

		// String ret = GradeBookUtil.toGridXML(gradeBookLessonDTOs, page, totalPages, method);
		String ret = GradeBookUtil.toGridXML(gradeBookLessonDTOs, view, sortBy, isSearch, searchField,
			searchOper, searchString, sortOrder, rowLimit, page);

		response.setContentType(GradeBookConstants.CONTENT_TYPE_TEXTXML);
		PrintWriter out = response.getWriter();
		out.print(ret);

	    }

	} else {
	    // Grid will handle error, just log and return null
	    logger.error("Error: request for course gradebook data with null user or course. CourseID: " + courseID);
	}

	return null;
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser() {
	UserDTO userDTO = getUser();
	if (userDTO != null) {
	    return getUserService().getUserByLogin(userDTO.getLogin());
	} else {
	    return null;
	}
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
