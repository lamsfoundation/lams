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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.PasswordGateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceException;
import org.lamsfoundation.lams.monitoring.web.form.GateForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserDetails;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
 */
@Controller
@RequestMapping("/gate")
public class GateController {
    private static final DateFormat SCHEDULING_DATETIME_FORMAT = new SimpleDateFormat("MM/dd/yy HH:mm");

    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private ILearnerService learnerService;
    @Autowired
    private ILessonService lessonService;

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
     */
    @RequestMapping("/viewGate")
    public String viewGate(@ModelAttribute GateForm gateForm, HttpServletRequest request,
	    HttpServletResponse response) {
	// if this is the initial call then activity id will be in the request, otherwise
	// get it from the form (if being called from openGate.jsp
	Long gateIdLong = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	if (gateIdLong == null) {
	    gateIdLong = gateForm.getActivityId();
	}
	long gateId = gateIdLong != null ? gateIdLong.longValue() : -1;

	GateActivity gate = (GateActivity) monitoringService.getActivityById(gateId);

	if (gate == null) {
	    throw new MonitoringServiceException("Gate activity missing. Activity id" + gateId);
	}

	gateForm.setActivityId(gateIdLong);

	return findViewByGateType(gateForm, gate);
    }

    /**
     * Open the gate if is closed.
     */
    @RequestMapping(path = "/openGate", method = RequestMethod.POST)
    public String openGate(@ModelAttribute GateForm gateForm, HttpServletRequest request, Model model) {
	GateActivity gate = monitoringService.openGate(gateForm.getActivityId(), getUserId());
	model.addAttribute("gateJustToggled", true);
	return findViewByGateType(gateForm, gate);
    }

    /**
     * Close the gate again.
     */
    @RequestMapping(path = "/closeGate", method = RequestMethod.POST)
    public String closeGate(@ModelAttribute GateForm gateForm, Model model) {
	GateActivity gate = monitoringService.closeGate(gateForm.getActivityId());
	model.addAttribute("gateJustToggled", true);
	return findViewByGateType(gateForm, gate);
    }

    /**
     * Allows a single learner to pass the gate.
     */
    @RequestMapping(path = "/openGateForSingleUser", method = RequestMethod.POST)
    public String openGateForSingleUser(@ModelAttribute GateForm gateForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Long gateIdLong = gateForm.getActivityId();
	String userId = gateForm.getUserId();
	String[] userIdsString = userId.split(",");
	List<Integer> userIds = new LinkedList<>();
	for (String userIdString : userIdsString) {
	    if (StringUtils.isNotBlank(userIdString)) {
		userIds.add(Integer.valueOf(userIdString));
	    }
	}
	GateActivity gate = monitoringService.openGateForSingleUser(gateIdLong, userIds.toArray(new Integer[] {}));
	return findViewByGateType(gateForm, gate);
    }

    /**
     * Allows a single learner to pass the gate.
     */
    @RequestMapping(path = "/changeGatePassword", method = RequestMethod.POST)
    public String changeGatePassword(@ModelAttribute GateForm gateForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Long gateIdLong = gateForm.getActivityId();

	GateActivity gate = monitoringService.changeGatePassword(gateIdLong, gateForm.getKey());
	return findViewByGateType(gateForm, gate);
    }

    @RequestMapping(path = "/scheduleGate", method = RequestMethod.POST)
    public String scheduleGate(@ModelAttribute GateForm gateForm, HttpServletRequest request,
	    HttpServletResponse response) throws ParseException {
	Long gateId = gateForm.getActivityId();
	String dateAsString = gateForm.getScheduleDate();
	GateActivity gate = null;
	if (dateAsString != null && dateAsString.trim().length() > 0) {
	    gate = monitoringService.scheduleGate(gateId, SCHEDULING_DATETIME_FORMAT.parse(dateAsString), getUserId());
	} else {
	    gate = (GateActivity) monitoringService.getActivityById(gateId);
	}
	return findViewByGateType(gateForm, gate);
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
     */
    private String findViewByGateType(@ModelAttribute GateForm gateForm, GateActivity gate) {
	// reset all the other fields, so that the following code only has to set up its own values (LDEV-1237)
	gateForm.setGate(null);
	gateForm.setWaitingLearnerList(null);
	gateForm.setAllowedToPassLearnerList(null);
	gateForm.setForbiddenLearnerList(null);
	gateForm.setStartingTime(null);
	gateForm.setEndingTime(null);
	gateForm.setScheduleDate(null);

	gateForm.setGate(gate);

	// setup the total learners
	int totalLearners = 0;
	for (Group group : learnerService.getGroupsForGate(gate)) {
	    // users collection is extra-lazy, so checking its size will not trigger full load
	    totalLearners += group.getUsers().size();
	}
	gateForm.setTotalLearners(totalLearners);

	// dispatch the view according to the type of the gate.
	if (gate.isSynchGate()) {
	    Integer waitingLearnerCount = lessonService.getCountLearnersInCurrentActivity(gate);
	    gateForm.setWaitingLearners(waitingLearnerCount);
	    return "gate/sychGateContent";
	}

	if (gate.isScheduleGate()) {
	    Integer waitingLearnerCount = lessonService.getCountLearnersInCurrentActivity(gate);
	    gateForm.setWaitingLearners(waitingLearnerCount);
	    return viewScheduleGate(gateForm, (ScheduleGateActivity) gate);
	}

	if (gate.isPermissionGate() || gate.isSystemGate() || gate.isConditionGate() || gate.isPasswordGate()) {
	    List<User> waitingLearnersList = monitoringService.getLearnersAttemptedActivity(gate);
	    Collections.sort(waitingLearnersList, IUserDetails.COMPARATOR);
	    gateForm.setWaitingLearners(waitingLearnersList.size());
	    gateForm.setWaitingLearnerList(waitingLearnersList);
	    gateForm.setAllowedToPassLearnerList(gate.getAllowedToPassLearners());
	    Set<Group> learnerGroups = learnerService.getGroupsForGate(gate);
	    Collection<User> forbiddenUsers = new TreeSet<>();
	    for (Group learnerGroup : learnerGroups) {
		// only here users are fetched from DB as it is an extra-lazy collection
		forbiddenUsers.addAll(learnerGroup.getUsers());
	    }
	    forbiddenUsers.removeAll(gate.getAllowedToPassLearners());
	    gateForm.setForbiddenLearnerList(forbiddenUsers);
	    if (gate.isConditionGate()) {
		return "gate/conditionGateContent";
	    } else if (gate.isPasswordGate()) {
		gateForm.setKey(((PasswordGateActivity) gate).getGatePassword());
		return "gate/passwordGateContent";
	    }

	    return "gate/permissionGateContent";
	}

	throw new MonitoringServiceException(
		"Invalid gate activity. " + "gate id [" + gate.getActivityId() + "] - the type ["
			+ gate.getActivityTypeId() + "] is not a gate type");
    }

    /**
     * Set up the form attributes specific to the schedule gate and navigate to the schedule gate view.
     */
    @RequestMapping("/viewScheduleGate")
    public String viewScheduleGate(@ModelAttribute GateForm gateForm, ScheduleGateActivity scheduleGate) {

	if (Boolean.TRUE.equals(scheduleGate.getGateActivityCompletionBased())) {
	    gateForm.setActivityCompletionBased(true);
	} else {
	    gateForm.setActivityCompletionBased(false);
	    Lesson lesson = learnerService.getLessonByActivity(scheduleGate);
	    Date lessonStartingTime = lesson.getStartDateTime();
	    if (lessonStartingTime == null && Lesson.NOT_STARTED_STATE.equals(lesson.getLessonStateId())) {
		// Assume the lesson will start at the scheduled time
		lessonStartingTime = lesson.getScheduleStartDate();
	    }
	    Calendar gateStartingTime = new GregorianCalendar(TimeZone.getDefault());
	    gateStartingTime.setTime(lessonStartingTime);
	    gateStartingTime.add(Calendar.MINUTE, scheduleGate.getGateStartTimeOffset().intValue());
	    gateForm.setStartingTime(gateStartingTime.getTime());
	}

	return "gate/scheduleGateContent";
    }
}