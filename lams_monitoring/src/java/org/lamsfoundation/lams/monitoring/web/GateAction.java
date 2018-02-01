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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceException;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * <p>
 * The action servlet that allows the teacher to view the status of sync gate, scheduling gate and permission gate. The
 * teacher can also force the gate to open through this servlet.
 * </p>
 *
 * <p>
 * Regarding view gate status, followings contents should be shown by calling this action servlet:
 * <li>1.View the status of an sync gate, the lams should show how many learners are waiting and the size of the total
 * class.</li>
 * <li>2.View the status of the permission gate, the lams shows the number of the learners waiting in front of the
 * gates.</li>
 * <li>3.View the status of the schedule gate, the lams shows the gate status. If the schedule has been triggerred. The
 * teacher should be able to change the trigger.</li>
 * </p>
 *
 * @author Jacky Fang
 * @since 2005-4-15
 */
public class GateAction extends LamsDispatchAction {
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    // private static Logger log = Logger.getLogger(GateAction.class);

    private IMonitoringService monitoringService;
    private ILearnerService learnerService;
    private ILessonService lessonService;
    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------
    private static final String VIEW_SYNCH_GATE = "viewSynchGate";
    private static final String VIEW_PERMISSION_GATE = "viewPermissionGate";
    private static final String VIEW_SCHEDULE_GATE = "viewScheduleGate";
    private static final String VIEW_CONDITION_GATE = "viewConditionGate";

    // Gate Form fields
    private static final String ACTIVITY_FORM_FIELD = "activityId";
    private static final String TOTAL_LEARNERS_FORM_FIELD = "totalLearners";
    private static final String USER_ID = "userId";
    private static final String SCHEDULE_DATE = "scheduleDate";

    private static final DateFormat SCHEDULING_DATETIME_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm");
    // ---------------------------------------------------------------------
    // Struts Dispatch Method
    // ---------------------------------------------------------------------
    /**
     * <p>
     * The dispatch method that allows the teacher to view the status of the gate. It is expecting the caller passed in
     * lesson id and gate activity id as http parameter. Otherwise, the utility method will generate some exception.
     * </p>
     *
     * <p>
     * Based on the lesson id and gate activity id, it sets up the gate form to show the waiting learners and the total
     * waiting learners. Regarding schedule gate, it also shows the estimated gate opening time and gate closing time.
     * </p>
     *
     * <b>Note:</b> gate form attribute <code>waitingLearners</code> got setup after the view is dispatch to ensure
     * there won't be casting exception occur if the activity id is not a gate by chance.
     *
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
    public ActionForward viewGate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	DynaActionForm gateForm = (DynaActionForm) form;

	// if this is the initial call then activity id will be in the request, otherwise
	// get it from the form (if being called from openGate.jsp
	Long gateIdLong = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	if (gateIdLong == null) {
	    gateIdLong = (Long) gateForm.get(GateAction.ACTIVITY_FORM_FIELD);
	}
	long gateId = gateIdLong != null ? gateIdLong.longValue() : -1;

	monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
	learnerService = MonitoringServiceProxy.getLearnerService(getServlet().getServletContext());

	GateActivity gate = (GateActivity) monitoringService.getActivityById(gateId);

	if (gate == null) {
	    throw new MonitoringServiceException("Gate activity missing. Activity id" + gateId);
	}

	gateForm.set(GateAction.ACTIVITY_FORM_FIELD, gateIdLong);

	return findViewByGateType(mapping, gateForm, gate);
    }

    /**
     * Open the gate if is closed.
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
    public ActionForward openGate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());

	DynaActionForm gateForm = (DynaActionForm) form;

	GateActivity gate = monitoringService.openGate((Long) gateForm.get(GateAction.ACTIVITY_FORM_FIELD));

	return findViewByGateType(mapping, gateForm, gate);
    }

    /**
     * Allows a single learner to pass the gate.
     *
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * @param form
     *            he ActionForm class that will contain any data submitted by the end-user via a form.
     * @param request
     *            A standard Servlet HttpServletRequest class.
     * @param response
     *            A standard Servlet HttpServletRequest class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward openGateForSingleUser(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());

	DynaActionForm gateForm = (DynaActionForm) form;
	Long gateIdLong = (Long) gateForm.get(GateAction.ACTIVITY_FORM_FIELD);
	String userId = (String) gateForm.get(GateAction.USER_ID);
	String[] userIdsString = userId.split(",");
	List<Integer> userIds = new LinkedList<Integer>();
	for (String userIdString : userIdsString) {
	    if (StringUtils.isNotBlank(userIdString)) {
		userIds.add(Integer.valueOf(userIdString));
	    }
	}
	GateActivity gate = monitoringService.openGateForSingleUser(gateIdLong, userIds.toArray(new Integer[] {}));
	return findViewByGateType(mapping, gateForm, gate);
    }

    public ActionForward scheduleGate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ParseException {
	monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());

	DynaActionForm gateForm = (DynaActionForm) form;
	Long gateId = (Long) gateForm.get(GateAction.ACTIVITY_FORM_FIELD);
	String dateAsString = (String) gateForm.get(GateAction.SCHEDULE_DATE);
	GateActivity gate = null;
	if (dateAsString != null && dateAsString.trim().length() > 0) {
	    gate = monitoringService.scheduleGate(gateId, SCHEDULING_DATETIME_FORMAT.parse(dateAsString), getUserId());
	} else {
	    gate = (GateActivity) monitoringService.getActivityById(gateId);
	}
	return findViewByGateType(mapping, gateForm, gate);
    }
    // ---------------------------------------------------------------------
    // Helper Methods
    // ---------------------------------------------------------------------
    private Integer getUserId() {
 	HttpSession ss = SessionManager.getSession();
 	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
 	return user != null ? user.getUserID() : null;
     }

    /**
     * Dispatch view the according to the gate type.
     *
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * @param gateForm
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param permissionGate
     *            the gate acitivty object
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     */
    private ActionForward findViewByGateType(ActionMapping mapping, DynaActionForm gateForm, GateActivity gate) {
	lessonService = MonitoringServiceProxy.getLessonService(getServlet().getServletContext());

	// reset all the other fields, so that the following code only has to set up its own values (LDEV-1237)
	gateForm.set("gate", null);
	gateForm.set("waitingLearnerList", null);
	gateForm.set("allowedToPassLearnerList", null);
	gateForm.set("forbiddenLearnerList", null);
	gateForm.set("startingTime", null);
	gateForm.set("endingTime", null);
	gateForm.set(GateAction.SCHEDULE_DATE, null);

	gateForm.set("gate", gate);

	// setup the total learners
	int totalLearners = 0;
	for (Group group : learnerService.getGroupsForGate(gate)) {
	    // users collection is extra-lazy, so checking its size will not trigger full load
	    totalLearners += group.getUsers().size();
	}
	gateForm.set(GateAction.TOTAL_LEARNERS_FORM_FIELD, totalLearners);

	// dispatch the view according to the type of the gate.
	if (gate.isSynchGate()) {
	    Integer waitingLearnerCount = lessonService.getCountLearnersInCurrentActivity(gate);
	    gateForm.set("waitingLearners", waitingLearnerCount);
	    return mapping.findForward(GateAction.VIEW_SYNCH_GATE);
	} else if (gate.isScheduleGate()) {
	    Integer waitingLearnerCount = lessonService.getCountLearnersInCurrentActivity(gate);
	    gateForm.set("waitingLearners", waitingLearnerCount);
	    return viewScheduleGate(mapping, gateForm, (ScheduleGateActivity) gate);
	} else if (gate.isPermissionGate() || gate.isSystemGate() || gate.isConditionGate()) {
	    List<User> waitingLearnersList = monitoringService.getLearnersAttemptedActivity(gate);
	    gateForm.set("waitingLearners", waitingLearnersList.size());
	    gateForm.set("waitingLearnerList", waitingLearnersList);
	    gateForm.set("allowedToPassLearnerList", gate.getAllowedToPassLearners());
	    Set<Group> learnerGroups = learnerService.getGroupsForGate(gate);
	    Collection<User> forbiddenUsers = new HashSet<User>();
	    for (Group learnerGroup : learnerGroups) {
		// only here users are fetched from DB as it is an extra-lazy collection
		forbiddenUsers.addAll(learnerGroup.getUsers());
	    }
	    forbiddenUsers.removeAll(gate.getAllowedToPassLearners());
	    gateForm.set("forbiddenLearnerList", forbiddenUsers);
	    if (gate.isConditionGate()) {
		return mapping.findForward(GateAction.VIEW_CONDITION_GATE);
	    }

	    return mapping.findForward(GateAction.VIEW_PERMISSION_GATE);
	} else {
	    throw new MonitoringServiceException("Invalid gate activity. " + "gate id [" + gate.getActivityId()
		    + "] - the type [" + gate.getActivityTypeId() + "] is not a gate type");
	}
    }

    /**
     * Set up the form attributes specific to the schedule gate and navigate to the schedule gate view.
     *
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * @param gateForm
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param permissionGate
     *            the gate acitivty object
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     */
    private ActionForward viewScheduleGate(ActionMapping mapping, DynaActionForm gateForm,
	    ScheduleGateActivity scheduleGate) {
	if (Boolean.TRUE.equals(scheduleGate.getGateActivityCompletionBased())) {
	    gateForm.set("activityCompletionBased", true);
	} else {
	    gateForm.set("activityCompletionBased", false);
	    learnerService = MonitoringServiceProxy.getLearnerService(getServlet().getServletContext());
	    Lesson lesson = learnerService.getLessonByActivity(scheduleGate);
	    Calendar startingTime = new GregorianCalendar(TimeZone.getDefault());
	    startingTime.setTime(lesson.getStartDateTime());
	    startingTime.add(Calendar.MINUTE, scheduleGate.getGateStartTimeOffset().intValue());
	    gateForm.set("startingTime", startingTime.getTime());
	}

	return mapping.findForward(GateAction.VIEW_SCHEDULE_GATE);
    }
}