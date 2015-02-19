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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.gradebook.util.GradebookUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
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
 *         Handles the general requests for content in gradebook
 * 
 * 
 * @struts.action path="/gradebook" parameter="dispatch" scope="request" validate="false"
 * 
 * @struts:action-forward name="error" path=".error"
 * @struts:action-forward name="message" path=".message"
 */
public class GradebookAction extends LamsDispatchAction {

    private static Logger logger = Logger.getLogger(GradebookAction.class);

    private static IGradebookService gradebookService;
    private static IUserManagementService userService;
    private static ILessonService lessonService;
    private static ISecurityService securityService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return null;
    }

    /**
     * Returns an xml representation of the activity grid for gradebook
     * 
     * This has two modes, userView and activityView
     * 
     * User view will get the grid data for a specified user, which is all their activity marks/outputs etc
     * 
     * Activity view will get the grid data for all activities, without user info, instead there is an average mark for
     * each activity
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getActivityGridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradebookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradebookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradebookConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradebookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_STRING, true);
	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonParticipant(lessonID, getUser().getUserID(), "get activity gradebook data",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a learner in the lesson");
	    return null;
	}

	// Getting userID param, it is passed differently from different views
	Integer userID = null;
	if (view == GBGridView.MON_USER) {
	    userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);
	} else if (view == GBGridView.LRN_ACTIVITY) {
	    UserDTO userDTO = getUser();
	    if (userDTO != null) {
		userID = userDTO.getUserID();
	    }
	}

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<GradebookGridRowDTO>();

	// Get the user gradebook list from the db
	// A slightly different list is needed for userview or activity view
	if ((view == GBGridView.MON_USER) || (view == GBGridView.LRN_ACTIVITY)) {
	    gradebookActivityDTOs = getGradebookService().getGBActivityRowsForLearner(lessonID, userID);
	} else if (view == GBGridView.MON_ACTIVITY) {
	    gradebookActivityDTOs = getGradebookService().getGBActivityRowsForLesson(lessonID);
	}

	if ((sortBy == null) || sortBy.equals("")) {
	    sortBy = GradebookConstants.PARAM_START_DATE;
	}

	String ret = GradebookUtil.toGridXML(gradebookActivityDTOs, view, sortBy, isSearch, searchField, searchOper,
		searchString, sortOrder, rowLimit, page);

	writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_XML, LamsDispatchAction.ENCODING_UTF8, ret);
	return null;
    }

    /**
     * Returns an xml representation of the user grid for gradebook
     * 
     * This has three modes: userView, activityView and courseMonitorView
     * 
     * User view will get all the learners in a lesson and print their gradebook data with their mark for the entire
     * lesson
     * 
     * Activity view will take an extra parameter (activityID) and instead show the user's mark just for one activity
     * 
     * Course monitor view gets the same as the user view, but the link is set to the lesson level gradebook instead of
     * learner
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getUserGridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradebookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradebookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradebookConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradebookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_STRING, true);
	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	UserDTO user = getUser();

	// Get the user gradebook list from the db
	List<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	// if leesonID is specified show results based on lesson
	if (lessonID != null) {
	    if (!getSecurityService().isLessonMonitor(lessonID, user.getUserID(), "get gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    Lesson lesson = getLessonService().getLesson(lessonID);
	    if ((view == GBGridView.MON_USER) || (view == GBGridView.MON_COURSE)) {
		gradebookUserDTOs = getGradebookService().getGBUserRowsForLesson(lesson);
	    } else if (view == GBGridView.MON_ACTIVITY) {
		String rowID = WebUtil.readStrParam(request, AttributeNames.PARAM_ACTIVITY_ID);

		Long activityID = null;

		// Splitting the rowID param to get the activity/group id pair
		String[] split = rowID.split("_");
		if (split.length == 2) {
		    activityID = Long.parseLong(split[0]);
		} else {
		    activityID = Long.parseLong(rowID);
		}

		// Getting the group id if it is there
		Long groupId = WebUtil.readLongParam(request, GradebookConstants.PARAM_GROUP_ID, true);

		Activity activity = getGradebookService().getActivityById(activityID);
		if ((activity != null) && (activity instanceof ToolActivity)) {
		    gradebookUserDTOs = getGradebookService().getGBUserRowsForActivity(lesson, (ToolActivity) activity,
			    groupId);
		} else {
		    // return null and the grid will report an error
		    GradebookAction.logger.error("No activity found for: " + activityID);
		    return null;
		}
	    }

	    // if organisationID is specified (but not lessonID) then show results for organisation
	} else if (organisationID != null) {
	    if (!getSecurityService().isGroupMonitor(organisationID, user.getUserID(), "get gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
		return null;
	    }

	    Organisation org = (Organisation) getUserService().findById(Organisation.class, organisationID);
	    gradebookUserDTOs = getGradebookService().getGBUserRowsForOrganisation(org);
	} else {
	    LamsDispatchAction.log.error("Missing parameters: either lessonID or organisationID should be specified.");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
	    return null;
	}

	String ret = GradebookUtil.toGridXML(gradebookUserDTOs, view, sortBy, isSearch, searchField, searchOper,
		searchString, sortOrder, rowLimit, page);
	writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_XML, LamsDispatchAction.ENCODING_UTF8, ret);
	return null;
    }

    /**
     * Returns an xml representation of the lesson grid for a course for gradebook
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
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, GradebookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradebookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradebookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradebookConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradebookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_STRING, true);
	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);
	Integer courseID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Organisation organisation = (Organisation) getUserService().findById(Organisation.class, courseID);

	// in case of toolbar searching (which uses different parameters than a single field searching) get those
	// parameters
	if (isSearch && (searchField == null)) {
	    searchField = GradebookConstants.PARAM_ROW_NAME;
	    searchOper = GradebookConstants.SEARCH_CONTAINS;
	    searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_ROW_NAME, true);
	}

	Set<Lesson> lessons = organisation.getLessons();
	if (lessons == null) {
	    return null;
	}

	User user;
	User viewer = getRealUser();
	if (view == GBGridView.MON_USER) {
	    if (!getSecurityService().isGroupMonitor(courseID, viewer.getUserId(), "get course gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
		return null;
	    }

	    Integer userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);
	    user = (User) getUserService().findById(User.class, userID);
	} else {
	    if (!getSecurityService().hasOrgRole(courseID, viewer.getUserId(),
		    new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.LEARNER },
		    "get course gradebook for learner", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
		return null;
	    }

	    user = getRealUser();
	}

	if ((organisation == null) || (user == null) || (viewer == null)) {
	    // Grid will handle error, just log and return null
	    GradebookAction.logger
		    .error("Error: request for course gradebook data with null course or user. CourseID: " + courseID);
	    return null;
	}
	List<GBLessonGridRowDTO> gradebookLessonDTOs = getGradebookService().getGBLessonRows(organisation, user,
		viewer, view);

	if (sortBy == null) {
	    sortBy = GradebookConstants.PARAM_ID;
	}

	// String ret = GradebookUtil.toGridXML(gradebookLessonDTOs, page, totalPages, method);
	String ret = GradebookUtil.toGridXML(gradebookLessonDTOs, view, sortBy, isSearch, searchField, searchOper,
		searchString, sortOrder, rowLimit, page);

	writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_XML, LamsDispatchAction.ENCODING_UTF8, ret);
	return null;
    }

    /**
     * Gets the total mark for a user's lesson and writes the result in the response
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getLessonMarkAggregate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);

	if (getUser().getUserID().equals(userID)) {
	    if (!getSecurityService().isLessonParticipant(lessonID, userID, "get lesson mark aggregate", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the lesson");
		return null;
	    }
	} else {
	    if (!getSecurityService().isLessonMonitor(lessonID, getUser().getUserID(), "get lesson mark aggregate",
		    false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }
	}

	Lesson lesson = getLessonService().getLesson(lessonID);
	User learner = (User) getUserService().findById(User.class, userID);

	if ((lesson != null) && (learner != null)) {
	    GradebookUserLesson lessonMark = getGradebookService().getGradebookUserLesson(lessonID, userID);
	    if (lessonMark.getMark() != null) {
		writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_PLAIN, LamsDispatchAction.ENCODING_UTF8,
			GradebookUtil.niceFormatting(lessonMark.getMark()));
	    }
	} else {
	    // Grid will handle error, just log and return null
	    GradebookAction.logger
		    .error("Error: request for course gradebook data with null user or lesson. lessonID: " + lessonID);
	}
	return null;
    }

    /**
     * Gets the average mark for an activity and writes the result in the response
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public ActionForward getActivityMarkAverage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String rowID = WebUtil.readStrParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	Long activityID = null;
	Long groupID = null;

	// Splitting the rowID param to get the activity/group id pair
	String[] split = rowID.split("_");
	if (split.length == 2) {
	    activityID = Long.parseLong(split[0]);
	    groupID = Long.parseLong(split[1]);
	} else {
	    activityID = Long.parseLong(rowID);
	}

	Activity activity = getGradebookService().getActivityById(activityID);
	if (activity == null) {
	    LamsDispatchAction.log.error("Activity with ID: " + activityID
		    + " could not be found when getting activity mark average");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
	    return null;
	}
	Integer userID = getUser().getUserID();
	for (Lesson lesson : (Set<Lesson>) activity.getLearningDesign().getLessons()) {
	    if (!getSecurityService().isLessonMonitor(lesson.getLessonId(), userID, "get activity mark average", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }
	}

	Double averageMark = getGradebookService().getAverageMarkForActivity(activityID, groupID);
	if (averageMark != null) {
	    writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_PLAIN, LamsDispatchAction.ENCODING_UTF8,
		    GradebookUtil.niceFormatting(averageMark));
	} else {
	    writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_PLAIN, LamsDispatchAction.ENCODING_UTF8,
		    GradebookConstants.CELL_EMPTY);
	}

	return null;
    }

    /**
     * Gets the average mark for lesson and writes the result in the response
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ActionForward getLessonMarkAverage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!getSecurityService().isLessonMonitor(lessonID, getUser().getUserID(), "get lesson mark average", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Lesson lesson = getLessonService().getLesson(lessonID);
	Double averageMark = getGradebookService().getAverageMarkForLesson(lessonID);

	if (averageMark != null) {
	    writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_PLAIN, LamsDispatchAction.ENCODING_UTF8,
		    GradebookUtil.niceFormatting(averageMark));
	} else {
	    writeResponse(response, LamsDispatchAction.CONTENT_TYPE_TEXT_PLAIN, LamsDispatchAction.ENCODING_UTF8,
		    GradebookConstants.CELL_EMPTY);
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

    private IUserManagementService getUserService() {
	if (GradebookAction.userService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    GradebookAction.userService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return GradebookAction.userService;
    }

    private ILessonService getLessonService() {
	if (GradebookAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    GradebookAction.lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return GradebookAction.lessonService;
    }

    private IGradebookService getGradebookService() {
	if (GradebookAction.gradebookService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    GradebookAction.gradebookService = (IGradebookService) ctx.getBean("gradebookService");
	}
	return GradebookAction.gradebookService;
    }

    private ISecurityService getSecurityService() {
	if (GradebookAction.securityService == null) {
	    WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    GradebookAction.securityService = (ISecurityService) webContext.getBean("securityService");
	}

	return GradebookAction.securityService;
    }
}