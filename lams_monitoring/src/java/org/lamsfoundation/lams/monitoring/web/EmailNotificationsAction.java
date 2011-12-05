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
 
/* $Id$ */  
package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * The action servlet that provide all the monitoring functionalities. It interact with the teacher via flash and JSP
 * monitoring interface.
 * </p>
 * 
 * @author Andrey Balan
 * 
 *         ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/emailNotifications" parameter="method" validate="false"
 * @struts.action-forward name = "lessonView" path = "/emailnotifications/lessonNotifications.jsp"
 * @struts.action-forward name = "courseView" path = "/emailnotifications/courseNotifications.jsp"
 * @struts.action-forward name = "userList" path = "/emailnotifications/userList.jsp"
 * 
 *                        ----------------XDoclet Tags--------------------
 */
public class EmailNotificationsAction extends LamsDispatchAction {

    // ---------------------------------------------------------------------
    // Class level constants
    // ---------------------------------------------------------------------
    private static final int LESSON_TYPE_ASSIGNED_TO_LESSON = 0;
    private static final int LESSON_TYPE_HAVENT_FINISHED_LESSON = 1;
    private static final int LESSON_TYPE_HAVE_FINISHED_LESSON = 2;
    private static final int LESSON_TYPE_HAVENT_STARTED_LESSON = 3;
    private static final int LESSON_TYPE_HAVE_STARTED_LESSON = 4;
    private static final int LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY = 5;
    private static final int LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE = 6;
    
    private static final int COURSE_TYPE_HAVE_STARTED_X_LESSONS = 7;
    private static final int COURSE_TYPE_HAVE_FINISHED_X_LESSONS = 8;
    private static final int COURSE_TYPE_HAVE_FINISHED_PARTICULAR_LESSON = 9;
    private static final int COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS = 10;
    
    private static ILessonService lessonService;
    private static IGroupUserDAO groupUserDAO;
    private static ILearnerProgressDAO learnerProgressDAO;

    // ---------------------------------------------------------------------
    // Struts Dispatch Method
    // ---------------------------------------------------------------------
    /**
     * This STRUTS action method will initialize a lesson for specific learning design with the given lesson title and
     * lesson description.
     * <p>
     * If initialization is successed, this method will return a WDDX message which includes the ID of new lesson.
     * 
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * 
     * @param form
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getLessonView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	ICoreLearnerService learnerService = MonitoringServiceProxy.getLearnerService(getServlet()
		.getServletContext());

	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = learnerService.getLesson(lessonId);
	Set activities = lesson.getLearningDesign().getActivities();
	Set users = lesson.getAllLearners();
	// activityDAO getActivitiesByLearningDesignId(Long learningDesignId);
	
	request.setAttribute("lesson", lesson);
	request.setAttribute("activities", activities);
	request.setAttribute("users", users);

	return mapping.findForward("lessonView");
    }
    
    public ActionForward getCourseView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	ICoreLearnerService learnerService = MonitoringServiceProxy.getLearnerService(getServlet()
		.getServletContext());

	int orgId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	
	// getting the organisation
	Organisation org = (Organisation) learnerService.getUserManagementService().findById(Organisation.class, orgId);
	Set lessons = org.getLessons();
//	Set users = lesson.getAllLearners();
	// activityDAO getActivitiesByLearningDesignId(Long learningDesignId);
	
	request.setAttribute("org", org);
	request.setAttribute("lessons", lessons);
//	request.setAttribute("users", users);

	return mapping.findForward("courseView");
    }

    /**
     * The Struts dispatch method that starts a lesson that has been created beforehand. Most likely, the request to
     * start lesson should be triggered by the flash component. This method will delegate to the Spring service bean to
     * complete all the steps for starting a lesson. Finally, a wddx acknowledgement message will be serialized and sent
     * back to the flash component.
     * 
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * 
     * @param form
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward emailUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	
	    long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	    String[] userIds = request.getParameterValues("userId");
	    
	return null;
    }
    
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	ICoreLearnerService learnerService = MonitoringServiceProxy.getLearnerService(getServlet()
		.getServletContext());
	
	int searchType = WebUtil.readIntParam(request, "searchType");
	Lesson lesson = null;
	Long orgId;
	Long activityId = null;
	int xDaystoFinish = 0;
	int numberLessons = 0;
	String[] lessonIds = null;
	
	//TODO check checkOwnerOrStaffMember
	
	//validate request
	switch (searchType) {
	case LESSON_TYPE_ASSIGNED_TO_LESSON:
	case LESSON_TYPE_HAVENT_FINISHED_LESSON:
	case LESSON_TYPE_HAVE_FINISHED_LESSON:
	case LESSON_TYPE_HAVENT_STARTED_LESSON:
	case LESSON_TYPE_HAVE_STARTED_LESSON:
	case LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY:
	case LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE:
	    Long lessonId = WebUtil.readLongParam(request, "lessonId");
	    lesson  = learnerService.getLesson(lessonId);
	    break;

	case COURSE_TYPE_HAVE_STARTED_X_LESSONS:
	case COURSE_TYPE_HAVE_FINISHED_X_LESSONS:
	case COURSE_TYPE_HAVE_FINISHED_PARTICULAR_LESSON:
	case COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS:
	    orgId = WebUtil.readLongParam(request, "orgId");
	    Organisation org = (Organisation) learnerService.getUserManagementService().findById(Organisation.class, orgId);
	    break;
	}
	
	switch (searchType) {
	case LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY:
	    activityId = WebUtil.readLongParam(request, "activityId");
	    break;
	case LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE:
	    xDaystoFinish = WebUtil.readIntParam(request, "daysToDeadline");
	    break;

	case COURSE_TYPE_HAVE_STARTED_X_LESSONS:
	case COURSE_TYPE_HAVE_FINISHED_X_LESSONS:
	    numberLessons = WebUtil.readIntParam(request, "numberLessons");
	    break;
	case COURSE_TYPE_HAVE_FINISHED_PARTICULAR_LESSON:
	    Long lessonId = WebUtil.readLongParam(request, "lessonId");
	    lesson  = learnerService.getLesson(lessonId);
	    break;
	case COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS:
	    lessonIds = request.getParameterValues("lessonId");
	    break;
	}
	
	Collection<User> users = getUsers(searchType, lesson, lessonIds, activityId, xDaystoFinish);
	request.setAttribute("users", users);

	return mapping.findForward("userList");
    }
    
    public Collection<User> getUsers(int searchType, Lesson lesson, String[] lessonIds, Long activityId, int xDaystoFinish) throws IOException, ServletException {
	getLessonService();
	
	ICoreLearnerService learnerService = MonitoringServiceProxy.getLearnerService(getServlet()
		.getServletContext());
	
	Long lessonId = lesson.getLessonId();
	
	Collection<User> users = new LinkedList<User>();
	switch (searchType) {
	case LESSON_TYPE_ASSIGNED_TO_LESSON:
	    users = lesson.getAllLearners();
	    break;
	    
	case LESSON_TYPE_HAVENT_FINISHED_LESSON:
	    Set<User> allUsers = lesson.getAllLearners();
	    List<User> usersCompletedLesson = getUsersCompletedLesson(lessonId);
	    users = CollectionUtils.subtract(allUsers, usersCompletedLesson);
	    break;
	    
	case LESSON_TYPE_HAVE_FINISHED_LESSON:
	    users = getUsersCompletedLesson(lessonId);
	    break;
	    
	case LESSON_TYPE_HAVENT_STARTED_LESSON:
	    allUsers = lesson.getAllLearners();
	    List<User> usersStartedLesson = lessonService.getActiveLessonLearners(lessonId);
	    users = CollectionUtils.subtract(allUsers, usersStartedLesson);
	    break;
	    
	case LESSON_TYPE_HAVE_STARTED_LESSON:
	    users = lessonService.getActiveLessonLearners(lessonId);
	    break;

	case LESSON_TYPE_HAVENT_REACHED_PARTICULAR_ACTIVITY:
	    Activity activity = learnerService.getActivity(activityId);
	    allUsers = lesson.getAllLearners();
	    List<User> usersAttemptedActivity = lessonService.getLearnersHaveAttemptedActivity(activity);
	    users = CollectionUtils.subtract(allUsers, usersAttemptedActivity);
	    break;
	   
	case LESSON_TYPE_LESS_THAN_X_DAYS_TO_DEADLINE:
	    Date now = new Date();
	    Calendar currentTimePlusXDays = Calendar.getInstance();
	    currentTimePlusXDays.setTime(now);
	    currentTimePlusXDays.add(Calendar.DATE, xDaystoFinish);

	    Date scheduleEndDate = lesson.getScheduleEndDate();
	    if (scheduleEndDate != null) {
		if (now.before(scheduleEndDate) && currentTimePlusXDays.getTime().after(scheduleEndDate)) {
		    users = lesson.getAllLearners();
		}
		
	    } else if (lesson.isScheduledToCloseForIndividuals()) {
		getGroupUserDAO().getUsersWithLessonEndingSoonerThan(lesson, currentTimePlusXDays.getTime());
	    }
	    break;

	case COURSE_TYPE_HAVE_STARTED_X_LESSONS:
	    break;
	    
	case COURSE_TYPE_HAVE_FINISHED_X_LESSONS:
	    break;
	    
	case COURSE_TYPE_HAVE_FINISHED_PARTICULAR_LESSON:	    
	    users = getUsersCompletedLesson(lessonId);
	    break;
	    
	case COURSE_TYPE_HAVE_FINISHED_THESE_LESSONS:
	    int i = 0;
	    for (String lessonIdStr : lessonIds) {
		lessonId = Long.parseLong(lessonIdStr);
		List<User> completedLesson = getUsersCompletedLesson(lessonId);
		if (i++ == 0) {
		    users = completedLesson;
		} else {
		    users = CollectionUtils.intersection(users, completedLesson);
		}
	    }
	    break;
	}
	
	return users;
    }
    
    private List<User> getUsersCompletedLesson(Long lessonId) {
	List<User> usersCompletedLesson = new LinkedList<User>();
	
	List<LearnerProgress> completedLearnerProgresses = getLearnerProgressDAO().getCompletedLearnerProgressForLesson(lessonId);
	for (LearnerProgress learnerProgress : completedLearnerProgresses) {
	    usersCompletedLesson.add(learnerProgress.getUser());
	}
	return usersCompletedLesson;
    }
    
    private ILessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    lessonService = (ILessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }
    
    private IGroupUserDAO getGroupUserDAO() {
	if (groupUserDAO == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    groupUserDAO = (IGroupUserDAO) ctx.getBean("groupUserDAO");
	}
	return groupUserDAO;
    }
    
    private ILearnerProgressDAO getLearnerProgressDAO() {
	if (learnerProgressDAO == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    learnerProgressDAO = (ILearnerProgressDAO) ctx.getBean("learnerProgressDAO");
	}
	return learnerProgressDAO;
    }
}
