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

package org.lamsfoundation.lams.learning.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.form.GateForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.dto.GateActivityDTO;
import org.lamsfoundation.lams.learningdesign.strategy.ScheduleGateActivityStrategy;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * The action servlet that deals with gate activity. This class allows the learner to knock gate when they reach the
 * gate. The knocking process will be triggered by the lams progress engine in the first place. The learner can also
 * trigger the knocking process by clicking on the button on the waiting page.
 * </p>
 *
 * <p>
 * Learner will progress to the next activity if the gate is open. Otherwise, the learner should see the waiting page.
 * </p>
 *
 * <p>
 * Has a special override key - if the parameter force is set and the lesson is a preview lesson, then the gate will be
 * opened straight away. This allows the author to see gate shut initially but override it and open it rather than being
 * held up by the gate.
 * </p>
 *
 * @author Jacky Fang
 */
@Controller
@RequestMapping("/gate")
public class GateController {

    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;
    
    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    // private static Logger log = Logger.getLogger(GateAction.class);

    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------
    private static final String VIEW_PERMISSION_GATE = "permissionGate";
    private static final String VIEW_SCHEDULE_GATE = "scheduleGate";
    private static final String VIEW_SYNCH_GATE = "synchGate";
    private static final String VIEW_CONDITION_GATE = "conditionGate";

    /** Input parameter. Boolean value */
    public static final String PARAM_FORCE_GATE_OPEN = "force";

    // ---------------------------------------------------------------------
    // Struts Dispatch Method
    // ---------------------------------------------------------------------
    /**
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("knockGate")
    public String knockGate(@ModelAttribute GateForm gateForm, HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	boolean forceGate = WebUtil.readBooleanParam(request, GateController.PARAM_FORCE_GATE_OPEN, false);
	Long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	// initialize service object
	Activity activity = learnerService.getActivity(activityId);
	User learner = LearningWebUtil.getUser(learnerService);
	Lesson lesson = learnerService.getLesson(lessonId);

	LearnerProgress learnerProgress = learnerService.getProgress(learner.getUserId(), lessonId);

	if (activity != null) {
	    // knock the gate
	    GateActivityDTO gateDTO = learnerService.knockGate(activityId, learner, forceGate);

	    if (gateDTO == null) {
		throw new LearnerServiceException("Gate missing. gate id [" + activityId + "]");
	    }

	    // if the gate is closed, ask the learner to wait ( updating the cached learner progress on the way )
	    if (!gateDTO.getAllowToPass()) {
		String forward = findViewByGateType(gateForm, gateDTO, lesson);
		return forward;
	    }
	}

	// gate is open, so let the learner go to the next activity ( updating the cached learner progress on the way )
	return learnerService.completeActivity(activityMapping, learnerProgress, activity, learner.getUserId(), true);
    }

    // ---------------------------------------------------------------------
    // Helper methods
    // ---------------------------------------------------------------------
    /**
     * Dispatch view the according to the gate type.
     *
     * @param mapping
     *            An ActionMapping class that will be used by the Action class to tell the ActionServlet where to send
     *            the end-user.
     * @param gateForm
     *            The ActionForm class that will contain any data submitted by the end-user via a form.
     * @param permissionGate
     *            the gate activity object
     * @param totalNumActiveLearners
     *            total number of active learners in the lesson (may not all be logged in)
     * @return An ActionForward class that will be returned to the ActionServlet indicating where the user is to go
     *         next.
     */
    @RequestMapping("/findViewByGateType")
    private String findViewByGateType(@ModelAttribute GateForm gateForm, GateActivityDTO gateDTO, Lesson lesson) {
	gateForm.setTotalLearners(gateDTO.getExpectedLearnerCount());
	gateForm.setWaitingLearners(gateDTO.getWaitingLearnerCount());
	gateForm.setPreviewLesson(lesson.isPreviewLesson());
	gateForm.setMonitorCanOpenGate(true);
	GateActivity gate = gateDTO.getGate();
	gateForm.setActivityID(gate.getActivityId());
	gateForm.setLessonID(lesson.getLessonId());
	gateForm.setGate(gate);
	if (gate.isSynchGate()) {
	    return "gate/synchGateContent";
	} else if (gate.isScheduleGate()) {
	    ScheduleGateActivity scheduleGate = (ScheduleGateActivity) gate;
	    if (Boolean.TRUE.equals(scheduleGate.getGateActivityCompletionBased())) {
		// so it is in seconds
		gateForm.setStartOffset(scheduleGate.getGateStartTimeOffset() * 60);

		User learner = LearningWebUtil.getUser(learnerService);
		Date reachTime = ScheduleGateActivityStrategy.getPreviousActivityCompletionDate(scheduleGate, learner);
		gateForm.setReachDate(reachTime);

		Calendar startingTime = new GregorianCalendar(TimeZone.getDefault());
		startingTime.setTime(reachTime);
		startingTime.add(Calendar.MINUTE, scheduleGate.getGateStartTimeOffset().intValue());
		gateForm.setStartingTime(startingTime.getTime());
		long diff = startingTime.getTimeInMillis() - new Date().getTime();
		long remainTime = diff / 1000;
		gateForm.setRemainTime(remainTime);
		gateForm.setEndingTime(null);
	    } else {
		gateForm.setStartOffset(null);
		gateForm.setReachDate(null);
		Calendar startingTime = new GregorianCalendar(TimeZone.getDefault());
		startingTime.setTime(lesson.getStartDateTime());
		startingTime.add(Calendar.MINUTE, scheduleGate.getGateStartTimeOffset().intValue());
		gateForm.setStartingTime(startingTime.getTime());
		long diff = startingTime.getTimeInMillis() - new Date().getTime();
		long remainTime = diff / 1000;
		gateForm.setRemainTime(remainTime);
		gateForm.setEndingTime(null);
	    }
	    return "gate/scheduleGateContent";
	} else if (gate.isConditionGate()) {
	    gateForm.setMonitorCanOpenGate(false);
	    return "gate/conditionGateContent";
	} else if (gate.isPermissionGate() || gate.isSystemGate()) {
	    return "gate/permissionGateContent";
	} else {
	    throw new LearnerServiceException("Invalid gate activity. " + "gate id [" + gate.getActivityId()
		    + "] - the type [" + gate.getActivityTypeId() + "] is not a gate type");
	}
    }
}