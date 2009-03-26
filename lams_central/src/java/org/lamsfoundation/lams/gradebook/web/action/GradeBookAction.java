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
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.gradebook.dto.GBUserGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.GradeBookGridRowDTO;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBUserFullNameComparator;
import org.lamsfoundation.lams.gradebook.dto.comparators.GBUserMarkComparator;
import org.lamsfoundation.lams.gradebook.service.IGradeBookService;
import org.lamsfoundation.lams.gradebook.util.GradeBookConstants;
import org.lamsfoundation.lams.gradebook.util.GradeBookUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author lfoxton
 * 
 * Handles the general requests for content in gradebook
 * 
 * 
 * @struts.action path="/gradebook/gradebook" parameter="dispatch"
 *                scope="request" name="gradeBookForm" validate="false"
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
	int page = WebUtil.readIntParam(request, GradeBookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradeBookConstants.PARAM_ROWS);

	// Leaving the sorting params here if we decide to sort the activities in some way
	String sortOrder = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SIDX, true);

	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	String method = WebUtil.readStrParam(request, "method");

	Lesson lesson = lessonService.getLesson(lessonID);

	if (lesson != null) {

	    List<GradeBookGridRowDTO> gradeBookActivityDTOs = new ArrayList<GradeBookGridRowDTO>();

	    // Get the user gradebook list from the db
	    // A slightly different list is needed for userview or activity view
	    if (method.equals("userView")) {
		String login = WebUtil.readStrParam(request, GradeBookConstants.PARAM_LOGIN);
		User learner = userService.getUserByLogin(login);
		if (learner != null) {
		    gradeBookActivityDTOs = gradeBookService.getGBActivityRowsForLearner(lesson, learner);
		} else {
		    // return null and the grid will report the error
		    logger.error("No learner found for: " + login);
		    return null;
		}
	    } else if (method.equals("activityView")) {
		gradeBookActivityDTOs = gradeBookService.getGBActivityRowsForLesson(lesson);
	    }

	    // Work out the sublist to fetch based on rowlimit and current page.
	    int totalPages = 1;
	    if (rowLimit < gradeBookActivityDTOs.size()) {

		totalPages = new Double(Math.ceil(new Integer(gradeBookActivityDTOs.size()).doubleValue()
			/ new Integer(rowLimit).doubleValue())).intValue();
		int firstRow = (page - 1) * rowLimit;
		int lastRow = firstRow + rowLimit;

		if (lastRow > gradeBookActivityDTOs.size()) {
		    gradeBookActivityDTOs = gradeBookActivityDTOs.subList(firstRow, gradeBookActivityDTOs.size());
		} else {
		    gradeBookActivityDTOs = gradeBookActivityDTOs.subList(firstRow, lastRow);
		}

	    }

	    String ret = "";
	    if (method.equals("userView")) {
		ret = GradeBookUtil.toGridXML(gradeBookActivityDTOs, page, rowLimit,
			GradeBookUtil.GRID_TYPE_MONITOR_USER_VIEW);
	    } else if (method.equals("activityView")) {
		ret = GradeBookUtil.toGridXML(gradeBookActivityDTOs, page, totalPages,
			GradeBookUtil.GRID_TYPE_MONITOR_ACTIVITY_VIEW);
	    }

	    response.setContentType("text/xml");
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
     * This has two modes, userView and activityView
     * 
     * User view will get all the learners in a lesson and print their gradebook
     * data with their mark for the entire lesson]
     * 
     * Activity view will take an extra parameter (activityID) and instead show
     * the user's mark just for one activity
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
	int page = WebUtil.readIntParam(request, GradeBookConstants.PARAM_PAGE);
	int rowLimit = WebUtil.readIntParam(request, GradeBookConstants.PARAM_ROWS);
	String sortOrder = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SORD);
	String sortBy = WebUtil.readStrParam(request, GradeBookConstants.PARAM_SIDX, true);
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Boolean isSearch = WebUtil.readBooleanParam(request, "_search");
	String method = WebUtil.readStrParam(request, "method");

	Lesson lesson = lessonService.getLesson(lessonID);

	if (lesson != null) {

	    // Get the user gradebook list from the db
	    List<GBUserGridRowDTO> gradeBookUserDTOs = new ArrayList<GBUserGridRowDTO>();

	    if (method.equals("userView")) {
		gradeBookUserDTOs = gradeBookService.getGBUserRowsForLesson(lesson);
	    } else if (method.equals("activityView")) {
		Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

		Activity activity = monitoringService.getActivityById(activityID);
		if (activity != null) {
		    gradeBookUserDTOs = gradeBookService.getGBUserRowsForActivity(lesson, activity);
		} else {
		    // return null and the grid will report an error
		    logger.error("No activity found for: " + activityID);
		    return null;
		}
	    }

	    // Sort the list appropriately
	    if (sortBy != null) {
		if (sortBy.equals("fullName")) {
		    Collections.sort(gradeBookUserDTOs, new GBUserFullNameComparator());
		} else if (sortBy.equals("mark")) {
		    Collections.sort(gradeBookUserDTOs, new GBUserMarkComparator());
		} else {
		    Collections.sort(gradeBookUserDTOs, new GBUserFullNameComparator());
		}
	    } else {
		Collections.sort(gradeBookUserDTOs, new GBUserFullNameComparator());
	    }

	    // if it is a search fix up the set
	    if (isSearch) {
		String searchField = WebUtil.readStrParam(request, "searchField");
		String searchOper = WebUtil.readStrParam(request, "searchOper");
		String searchString = WebUtil.readStrParam(request, "searchString");
		gradeBookUserDTOs = doUserSearch(gradeBookUserDTOs, searchField, searchOper, searchString.toLowerCase());
	    }
	    
	    
	    // Reverse the order if requested
	    if (sortOrder != null && sortOrder.equals("desc")) {
		Collections.reverse(gradeBookUserDTOs);
	    }

	    // Work out the sublist to fetch based on rowlimit and current page.
	    int totalPages = 1;
	    if (rowLimit < gradeBookUserDTOs.size()) {

		totalPages = new Double(Math.ceil(new Integer(gradeBookUserDTOs.size()).doubleValue()
			/ new Integer(rowLimit).doubleValue())).intValue();
		int firstRow = (page - 1) * rowLimit;
		int lastRow = firstRow + rowLimit;

		if (lastRow > gradeBookUserDTOs.size()) {
		    gradeBookUserDTOs = gradeBookUserDTOs.subList(firstRow, gradeBookUserDTOs.size());
		} else {
		    gradeBookUserDTOs = gradeBookUserDTOs.subList(firstRow, lastRow);
		}

	    }

	    String ret = "";

	    if (method.equals("userView")) {
		ret = GradeBookUtil.toGridXML(gradeBookUserDTOs, page, totalPages,
			GradeBookUtil.GRID_TYPE_MONITOR_USER_VIEW);
	    } else if (method.equals("activityView")) {
		ret = GradeBookUtil.toGridXML(gradeBookUserDTOs, page, totalPages,
			GradeBookUtil.GRID_TYPE_MONITOR_ACTIVITY_VIEW);
	    }

	    response.setContentType("text/xml");
	    PrintWriter out = response.getWriter();
	    out.print(ret);
	} else {
	    logger.error("No lesson could be found for: " + lessonID);
	}

	return null;
    }

    private List<GBUserGridRowDTO> doUserSearch(List<GBUserGridRowDTO> gradeBookUserDTOs, String searchField,
	    String searchOper, String searchString) {
	List<GBUserGridRowDTO> ret = new ArrayList<GBUserGridRowDTO>();
	
	if (searchField.equals("fullName")){
	    for (GBUserGridRowDTO userRow : gradeBookUserDTOs) {
		
		String fullName = userRow.getLastName() + " " + userRow.getFirstName();
		fullName = fullName.toLowerCase();
		
		if (searchOper.equals("eq")) {
		    if (fullName.equals(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals("ne")){
		    if (!fullName.equals(searchString)) {
			ret.add(userRow);
		    } 
		} else if (searchOper.equals("bw")){
		    if (fullName.startsWith(searchString)) {
			ret.add(userRow);
		    }
		} else if (searchOper.equals("ew")){
		    if (fullName.endsWith(searchString)) {
			ret.add(userRow);
		    }
		}else if (searchOper.equals("cn")){
		    if (fullName.contains(searchString)) {
			ret.add(userRow);
		    }
		}
	    }
	}
	return ret; 
    }

    private UserDTO getUser() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    public static IGradeBookService getGradeBookService() {
	return gradeBookService;
    }

    public static IUserManagementService getUserService() {
	return userService;
    }

    public static ILessonService getLessonService() {
	return lessonService;
    }

    private ActionForward displayMessage(ActionMapping mapping, HttpServletRequest req, String messageKey) {
	req.setAttribute("messageKey", messageKey);
	return mapping.findForward("message");
    }

    private void initServices() {
	ServletContext context = this.getServlet().getServletContext();

	if (gradeBookService == null)
	    gradeBookService = (IGradeBookService) WebApplicationContextUtils.getRequiredWebApplicationContext(context)
		    .getBean("gradeBookService");

	if (userService == null)
	    userService = (IUserManagementService) WebApplicationContextUtils.getRequiredWebApplicationContext(context)
		    .getBean("userManagementService");

	if (lessonService == null)
	    lessonService = (ILessonService) WebApplicationContextUtils.getRequiredWebApplicationContext(context)
		    .getBean("lessonService");

	if (monitoringService == null)
	    monitoringService = (IMonitoringService) WebApplicationContextUtils.getRequiredWebApplicationContext(
		    context).getBean("monitoringService");
    }
}
