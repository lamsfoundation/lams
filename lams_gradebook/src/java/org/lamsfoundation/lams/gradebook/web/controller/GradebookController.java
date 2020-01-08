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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.dto.GBLessonGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradebookGridRowDTO;
import org.lamsfoundation.lams.gradebook.service.IGradebookFullService;
import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.GradebookConstants;
import org.lamsfoundation.lams.gradebook.util.GradebookUtil;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Handles the general requests for content in gradebook
 *
 * @author lfoxton
 */
@Controller
@RequestMapping("/gradebook")
public class GradebookController {
    private static Logger logger = Logger.getLogger(GradebookController.class);

    @Autowired
    private IGradebookFullService gradebookService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ILearnerService learnerService;

    @RequestMapping("")
    @ResponseBody
    public void unspecified() throws Exception {
    }

    /**
     * Returns an xml representation of the activity grid for gradebook
     *
     * This has two modes, userView and activityView
     *
     * User view will get the grid data for a specified user, which is all their activity marks/outputs etc
     *
     * Activity view will get the grid data for all activities, without user info, instead there is an average mark for
     * each activity.
     */
    @RequestMapping("/getActivityGridData")
    @ResponseBody
    public String getActivityGridData(HttpServletRequest request, HttpServletResponse response) throws Exception {
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradebookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_STRING, true);
	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonParticipant(lessonID, getUser().getUserID(), "get activity gradebook data",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a learner in the lesson");
	    return null;
	}

	// Getting userID param, it is passed differently from different views
	UserDTO currentUserDTO = getUser();
	Integer userID = null;
	if (view == GBGridView.MON_USER) {
	    userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);
	} else if (view == GBGridView.LRN_ACTIVITY) {
	    if (currentUserDTO != null) {
		userID = currentUserDTO.getUserID();
	    }
	}

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<>();

	// Get the user gradebook list from the db
	// A slightly different list is needed for userview or activity view
	if ((view == GBGridView.MON_USER) || (view == GBGridView.LRN_ACTIVITY)) {//2nd level && from personal marks page (2nd level or 1st)
	    gradebookActivityDTOs = gradebookService.getGBActivityRowsForLearner(lessonID, userID,
		    currentUserDTO.getTimeZone());
	} else if (view == GBGridView.MON_ACTIVITY) {
	    gradebookActivityDTOs = gradebookService.getGBActivityRowsForLesson(lessonID, currentUserDTO.getTimeZone(),
		    true);
	}

	if ((sortBy == null) || sortBy.equals("")) {
	    sortBy = GradebookConstants.PARAM_START_DATE;
	}

	String ret = GradebookUtil.toGridXML(gradebookActivityDTOs, view, sortBy, isSearch, searchField, searchOper,
		searchString, sortOrder, rowLimit, page);

	response.setContentType("text/xml; charset=utf-8");
	return ret;
    }

    @RequestMapping("/getActivityArchiveGridData")
    @ResponseBody
    public String getActivityArchiveGridData(HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	if (!securityService.isLessonParticipant(lessonID, getUser().getUserID(), "get activity archive gradebook data",
		false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a learner in the lesson");
	    return null;
	}

	// Getting userID param, it is passed differently from different views
	UserDTO currentUserDTO = getUser();
	Integer userID = null;
	if (view == GBGridView.MON_USER || view == GBGridView.MON_ACTIVITY) {
	    userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);
	} else if (view == GBGridView.LRN_ACTIVITY) {
	    if (currentUserDTO != null) {
		userID = currentUserDTO.getUserID();
	    }
	}

	List<GradebookGridRowDTO> gradebookActivityDTOs = new ArrayList<>();

	// Get the user gradebook list from the db
	// A slightly different list is needed for userview or activity view
	if ((view == GBGridView.MON_USER) || (view == GBGridView.LRN_ACTIVITY) || (view == GBGridView.MON_ACTIVITY)) {//2nd level && from personal marks page (2nd level or 1st)
	    gradebookActivityDTOs = gradebookService.getGBActivityArchiveRowsForLearner(activityID, userID,
		    currentUserDTO.getTimeZone());
	}

	String ret = GradebookUtil.toGridXML(gradebookActivityDTOs, view, GradebookConstants.PARAM_ID, false, null,
		null, null, GradebookConstants.SORT_DESC, 100, 1);

	response.setContentType("text/xml; charset=utf-8");
	return ret;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/getLessonCompleteGridData")
    @ResponseBody
    public String getLessonCompleteGridData(HttpServletRequest request, HttpServletResponse response) throws Exception {
	// Getting the params passed in from the jqGrid
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	UserDTO currentUserDTO = getUser();
	Integer userId = currentUserDTO.getUserID();
	if (!securityService.isLessonParticipant(lessonId, userId, "get lesson complete gradebook data", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a learner in the lesson");
	}

	List<GradebookGridRowDTO> gradebookActivityDTOs = gradebookService.getGBLessonComplete(lessonId, userId);

	ObjectNode resultJSON = JsonNodeFactory.instance.objectNode();
	resultJSON.put(CommonConstants.ELEMENT_RECORDS, gradebookActivityDTOs.size());

	ArrayNode rowsJSON = JsonNodeFactory.instance.arrayNode();
	for (GradebookGridRowDTO gradebookActivityDTO : gradebookActivityDTOs) {
	    ObjectNode rowJSON = JsonNodeFactory.instance.objectNode();
	    String id = gradebookActivityDTO.getId();
	    String[] idParts = id.split("_");
	    if (idParts.length > 1) {
		// if activity is grouped, use just the real activity ID and leave out group ID
		// as we know there will be no ID clash in this single learner gradebook table
		id = idParts[0];
	    }
	    rowJSON.put(GradebookConstants.ELEMENT_ID, id);

	    ArrayNode cellJSON = JsonNodeFactory.instance.arrayNode();
	    cellJSON.add(gradebookActivityDTO.getRowName());
	    cellJSON.add(gradebookActivityDTO.getStatus());
	    cellJSON.add(gradebookActivityDTO.getAverageMark() == null ? GradebookConstants.CELL_EMPTY
		    : GradebookUtil.niceFormatting(gradebookActivityDTO.getAverageMark()));
	    cellJSON.add(gradebookActivityDTO.getMark() == null ? GradebookConstants.CELL_EMPTY
		    : GradebookUtil.niceFormatting(gradebookActivityDTO.getMark()));

	    rowJSON.set(CommonConstants.ELEMENT_CELL, cellJSON);
	    rowsJSON.add(rowJSON);
	}
	resultJSON.set(CommonConstants.ELEMENT_ROWS, rowsJSON);

	// make a mapping of activity ID -> URL, same as in progress bar
	ObjectNode activityURLJSON = JsonNodeFactory.instance.objectNode();
	Object[] ret = learnerService.getStructuredActivityURLs(userId, lessonId);
	for (ActivityURL activity : (List<ActivityURL>) ret[0]) {
	    String url = activity.getUrl();
	    if (url != null) {
		if (url.startsWith("learner.do")) {
		    url = "learning/" + url;
		}
		String serverUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		if (!url.startsWith(serverUrl)) {
		    // monitor mode URLs should be prepended with server URL
		    url = serverUrl + url;
		}
		activityURLJSON.put(activity.getActivityId().toString(), activity.getUrl());
	    }
	}
	resultJSON.set("urls", activityURLJSON);

	boolean isWeighted = gradebookService.isWeightedMarks(lessonId);
	GradebookUserLesson gradebookUserLesson = gradebookService.getGradebookUserLesson(lessonId, userId);
	resultJSON.put("learnerLessonMark",
		gradebookUserLesson == null || gradebookUserLesson.getMark() == null ? GradebookConstants.CELL_EMPTY
			: GradebookUtil.niceFormatting(gradebookUserLesson.getMark(), isWeighted));
	Double averageLessonMark = gradebookService.getAverageMarkForLesson(lessonId);
	resultJSON.put("averageLessonMark", averageLessonMark == null ? GradebookConstants.CELL_EMPTY
		: GradebookUtil.niceFormatting(averageLessonMark, isWeighted));

	response.setContentType("application/json;charset=utf-8");
	return resultJSON.toString();
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
     * learner.
     */
    @RequestMapping("/getUserGridData")
    @ResponseBody
    public String getUserGridData(HttpServletRequest request, HttpServletResponse response) throws Exception {

	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradebookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_FIELD, true);
	String searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_STRING, true);
	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, true);
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	UserDTO user = getUser();

	// in case of toolbar searching (which uses different parameters than a single field searching) get those
	// parameters
	if (isSearch && (searchField == null)) {
	    searchField = GradebookConstants.PARAM_ROW_NAME;
	    searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_ROW_NAME, true);
	}

	// Get the user gradebook list from the db
	List<GBUserGridRowDTO> gradebookUserDTOs = new ArrayList<>();

	int totalUsers = 0;
	// if leesonID is specified show results based on lesson
	if (lessonID != null) {
	    if (!securityService.isLessonMonitor(lessonID, user.getUserID(), "get gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    Lesson lesson = lessonService.getLesson(lessonID);
	    //GBGridView.MON_USER - 1st table of gradebook lesson monitor
	    //GBGridView.MON_COURSE - Subgrid of 1st table of gradebook course monitor
	    if (view == GBGridView.MON_USER || view == GBGridView.MON_COURSE) {
		gradebookUserDTOs = gradebookService.getGBUserRowsForLesson(lesson, page - 1, rowLimit, sortBy,
			sortOrder, searchString, user.getTimeZone());
		totalUsers = lesson.getAllLearners().size();

		// Subgrid of 2nd table of gradebook lesson monitor
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

		Activity activity = gradebookService.getActivityById(activityID);
		if ((activity != null) && (activity instanceof ToolActivity)) {
		    gradebookUserDTOs = gradebookService.getGBUserRowsForActivity(lesson, (ToolActivity) activity,
			    groupId, page - 1, rowLimit, sortBy, sortOrder, searchString, user.getTimeZone());

		    //calculate totalUsers
		    totalUsers = lesson.getAllLearners().size();
		    if (groupId != null) {
			Group group = (Group) userManagementService.findById(Group.class, groupId);
			if (group != null) {
			    totalUsers = group.getUsers().size();
			}
		    }

		} else {
		    // return null and the grid will report an error
		    logger.error("No activity found for: " + activityID);
		    return null;
		}
	    }

	    // 2nd table of gradebook course monitor
	    // if organisationID is specified (but not lessonID) then show results for organisation
	} else if (organisationID != null) {
	    if (!securityService.isGroupMonitor(organisationID, user.getUserID(), "get gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
		return null;
	    }

	    Organisation org = (Organisation) userManagementService.findById(Organisation.class, organisationID);
	    gradebookUserDTOs = gradebookService.getGBUserRowsForOrganisation(org, page - 1, rowLimit, sortOrder,
		    searchString);
	    totalUsers = gradebookService.getCountUsersByOrganisation(organisationID, searchString);

	} else {
	    logger.error("Missing parameters: either lessonID or organisationID should be specified.");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
	    return null;
	}

	//calculate totalPages
	int totalPages = new Double(
		Math.ceil(new Integer(totalUsers).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();
	String ret = GradebookUtil.toGridXML(gradebookUserDTOs, page, totalPages, view);

	response.setContentType("text/xml; charset=utf-8");
	return ret;
    }

    /**
     * Returns an xml representation of the lesson grid for a course for gradebook
     *
     * This has two modes, learnerView and monitorView
     *
     * Learner view will get the data specific to one user
     *
     * Monitor will get the data average for whole lessons.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/getCourseGridData")
    @ResponseBody
    public String getCourseGridData(HttpServletRequest request, HttpServletResponse response) throws Exception {
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, CommonConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, CommonConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, CommonConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, CommonConstants.PARAM_SIDX, true);
	Boolean isSearch = WebUtil.readBooleanParam(request, GradebookConstants.PARAM_SEARCH);
	String searchField = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_FIELD, true);
	String searchOper = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_OPERATION, true);
	String searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_SEARCH_STRING, true);
	GBGridView view = GradebookUtil.readGBGridViewParam(request, GradebookConstants.PARAM_VIEW, false);
	Integer courseID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, courseID);

	// in case of toolbar searching (which uses different parameters than a single field searching) get those
	// parameters
	if (isSearch && (searchField == null)) {
	    searchField = GradebookConstants.PARAM_ROW_NAME;
	    searchOper = GradebookConstants.SEARCH_CONTAINS;
	    searchString = WebUtil.readStrParam(request, GradebookConstants.PARAM_ROW_NAME, true);
	}

	if (sortBy == null) {
	    sortBy = GradebookConstants.PARAM_ID;
	}

	if (sortOrder == null) {
	    sortOrder = GradebookConstants.SORT_ASC;
	}

	Set<Lesson> lessons = organisation.getLessons();
	if (lessons == null) {
	    return null;
	}

	User user;
	User viewer = getRealUser();
	boolean isGroupManager = false;
	if (view == GBGridView.MON_USER) {
	    Integer userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);
	    user = (User) userManagementService.findById(User.class, userID);
	} else {
	    user = viewer;
	}

	//permission check
	if (view == GBGridView.MON_USER) {
	    if (!securityService.isGroupMonitor(courseID, viewer.getUserId(), "get course gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
		return null;
	    }

	} else if (view == GBGridView.MON_COURSE || view == GBGridView.LIST) {
	    isGroupManager = userManagementService.hasRoleInOrganisation(viewer, Role.ROLE_GROUP_MANAGER, organisation);
	    if (!isGroupManager && !securityService.hasOrgRole(courseID, viewer.getUserId(),
		    new String[] { Role.MONITOR }, "get course gradebook", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN,
			"User is not a group manager or monitor in the organisation");
		return null;
	    }

	} else if (view == GBGridView.LRN_COURSE) {
	    if (!securityService.hasOrgRole(courseID, viewer.getUserId(), new String[] { Role.LEARNER },
		    "get course gradebook for learner", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a learner in the organisation");
		return null;
	    }

	} else {
	    return null;
	}

	if ((organisation == null) || (user == null) || (viewer == null)) {
	    // Grid will handle error, just log and return null
	    logger.error("Error: request for course gradebook data with null course or user. CourseID: " + courseID);
	    return null;
	}
	List<GBLessonGridRowDTO> gradebookLessonDTOs = gradebookService.getGBLessonRows(organisation, user, viewer,
		isGroupManager, view, page - 1, rowLimit, sortBy, sortOrder, searchString, getUser().getTimeZone());

	String ret;
	if (view == GBGridView.MON_COURSE || view == GBGridView.LIST) {
	    int totalPages = new Double(
		    Math.ceil(new Integer(lessons.size()).doubleValue() / new Integer(rowLimit).doubleValue()))
			    .intValue();
	    ret = GradebookUtil.toGridXML(gradebookLessonDTOs, page, totalPages, view);

	} else {
	    ret = GradebookUtil.toGridXML(gradebookLessonDTOs, view, sortBy, isSearch, searchField, searchOper,
		    searchString, sortOrder, rowLimit, page);
	}

	response.setContentType("text/xml; charset=utf-8");
	return ret;
    }

    /**
     * Gets the total mark for a user's lesson and writes the result in the response.
     */
    @RequestMapping("/getLessonMarkAggregate")
    @ResponseBody
    public String getLessonMarkAggregate(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Integer userID = WebUtil.readIntParam(request, GradebookConstants.PARAM_USERID);

	if (getUser().getUserID().equals(userID)) {
	    if (!securityService.isLessonParticipant(lessonID, userID, "get lesson mark aggregate", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the lesson");
		return null;
	    }
	} else {
	    if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "get lesson mark aggregate", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }
	}

	Lesson lesson = lessonService.getLesson(lessonID);
	User learner = (User) userManagementService.findById(User.class, userID);

	if ((lesson != null) && (learner != null)) {
	    GradebookUserLesson lessonMark = gradebookService.getGradebookUserLesson(lessonID, userID);
	    if (lessonMark.getMark() != null) {
		response.setContentType("text/plain; charset=utf-8");

		return GradebookUtil.niceFormatting(lessonMark.getMark());
	    }
	} else {
	    // Grid will handle error, just log and return null
	    logger.error("Error: request for course gradebook data with null user or lesson. lessonID: " + lessonID);
	}
	return null;
    }

    /**
     * Gets the average mark for an activity and writes the result in the response
     */
    @RequestMapping("/getActivityMarkAverage")
    @ResponseBody
    public String getActivityMarkAverage(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

	Activity activity = gradebookService.getActivityById(activityID);
	if (activity == null) {
	    logger.error("Activity with ID: " + activityID + " could not be found when getting activity mark average");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
	    return null;
	}
	Integer userID = getUser().getUserID();
	for (Lesson lesson : activity.getLearningDesign().getLessons()) {
	    if (!securityService.isLessonMonitor(lesson.getLessonId(), userID, "get activity mark average", false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }
	}

	Double averageMark = gradebookService.getAverageMarkForActivity(activityID, groupID);
	response.setContentType("text/plain, charset=utf-8");
	if (averageMark != null) {
	    return GradebookUtil.niceFormatting(averageMark);

	} else {
	    return GradebookConstants.CELL_EMPTY;
	}
    }

    /**
     * Gets the average mark for lesson and writes the result in the response
     */
    @RequestMapping("/getAverageMarkForLesson")
    @ResponseBody
    public String getAverageMarkForLesson(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	if (!securityService.isLessonMonitor(lessonID, getUser().getUserID(), "get lesson mark average", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
	    return null;
	}

	Double averageMark = gradebookService.getAverageMarkForLesson(lessonID);

	response.setContentType("text/plain; charset=utf-8");
	if (averageMark != null) {
	    return GradebookUtil.niceFormatting(averageMark);

	} else {
	    return GradebookConstants.CELL_EMPTY;
	}
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private User getRealUser() {
	UserDTO userDTO = getUser();
	if (userDTO != null) {
	    return userManagementService.getUserByLogin(userDTO.getLogin());
	} else {
	    return null;
	}
    }

}