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

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
public class ComplexLearnerProgressController {
    private static Logger log = Logger.getLogger(ComplexLearnerProgressController.class);

    @Autowired
    private IMonitoringFullService monitoringService;

    @RequestMapping("/complexProgress")
    public String execute(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, false);
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID, false);
	Integer userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false);

	Activity activity = monitoringService.getActivityById(activityID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	if (activity.isParallelActivity()) {

	    ArrayList<String> urls = new ArrayList<>();
	    ParallelActivity parallelActivity = (ParallelActivity) activity;
	    Set parallels = parallelActivity.getActivities();
	    Iterator i = parallels.iterator();
	    try {
		while (i.hasNext()) {
		    Activity a = (Activity) i.next();
		    // get learner progress url for this parallel activity
		    urls.add(monitoringService.getLearnerActivityURL(lessonID, a.getActivityId(), userID,
			    user.getUserID()));
		}
	    } catch (SecurityException e) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		return null;
	    }

	    request.setAttribute("parallelUrls", urls);
	    return "parallelProgress";
	}

	else {

	    HashMap<Long, Byte> statusMap = new HashMap<>();
	    HashMap<Long, String> urlMap = new HashMap<>();
	    LearnerProgress learnerProgress = monitoringService.getLearnerProgress(userID, lessonID);
	    request.setAttribute("hasSequenceActivity", false);
	    List<ContributeActivityDTO> subActivities = new ArrayList<>();

	    if (activity.isOptionsActivity() || activity.isBranchingActivity()) {

		ComplexActivity complexActivity = (ComplexActivity) activity;

		Iterator i = complexActivity.getActivities().iterator();

		// iterate through each optional or branching activity
		while (i.hasNext()) {
		    Activity aNext = (Activity) i.next();

		    // make sure have castable object, not a CGLIB class
		    Activity a = monitoringService.getActivityById(aNext.getActivityId());
		    ContributeActivityDTO dto = new ContributeActivityDTO(a);
		    subActivities.add(dto);

		    Byte status = learnerProgress.getProgressState(a);
		    statusMap.put(a.getActivityId(), status);

		    if (a.isSequenceActivity()) {
			request.setAttribute("hasSequenceActivity", true);
			// map learner progress urls of each activity in the sequence
			SequenceActivity sequenceActivity = (SequenceActivity) a;
			dto.setChildActivities(new Vector<ContributeActivityDTO>());
			processSequenceChildren(lessonID, userID, monitoringService, user, statusMap, urlMap,
				learnerProgress, sequenceActivity, dto, null);
		    } else {
			if (status.equals(LearnerProgress.ACTIVITY_ATTEMPTED)
				|| status.equals(LearnerProgress.ACTIVITY_COMPLETED)) {
			    urlMap.put(a.getActivityId(), monitoringService.getLearnerActivityURL(lessonID,
				    a.getActivityId(), userID, user.getUserID()));
			}
		    }
		}

	    } else if (activity.isSequenceActivity()) {
		SequenceActivity sequenceActivity = (SequenceActivity) activity;
		try {
		    processSequenceChildren(lessonID, userID, monitoringService, user, statusMap, urlMap,
			    learnerProgress, sequenceActivity, null, subActivities);
		} catch (SecurityException e) {
		    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the lesson");
		    return null;
		}

	    } else {
		ComplexLearnerProgressController.log.error(
			"ComplexLearnerProgress trying to deal with a activity type it doesn't expect. Activity is "
				+ activity);
		return null;
	    }

	    // learner progress urls for children of the sequence activities
	    request.setAttribute("urlMap", urlMap);
	    // boolean flags for whether an activity is started
	    request.setAttribute("statusMap", statusMap);
	    // set of child activities
	    request.setAttribute("subActivities", subActivities);
	    // main activity title
	    request.setAttribute("activityTitle", activity.getTitle());

	    return "complexProgress";
	}

    }

    /**
     * Process the children of the sequence. Best done by traversing the transitions, with the first activity being the
     * default activity for the sequence.
     *
     * If the page is for a SequenceActivity the subActivities list should be included as a parameter and
     * parentContributeActivityDTO will be null.
     *
     * If the page is for a Branching or Optional Sequence activity then subActivities will be null (as the sequence
     * activities go in the subactivities list) but parentContributeActivityDTO should not be null.
     */
    private void processSequenceChildren(Long lessonID, Integer userID, IMonitoringFullService monitoringService,
	    UserDTO user, HashMap<Long, Byte> statusMap, HashMap<Long, String> urlMap, LearnerProgress learnerProgress,
	    SequenceActivity sequenceActivity, ContributeActivityDTO parentContributeActivityDTO,
	    List<ContributeActivityDTO> subActivities) throws IOException {
	Activity child = sequenceActivity.getDefaultActivity();
	while (child != null) {
	    Byte status = learnerProgress.getProgressState(child);
	    statusMap.put(child.getActivityId(), status);
	    if (status.equals(LearnerProgress.ACTIVITY_ATTEMPTED)
		    || status.equals(LearnerProgress.ACTIVITY_COMPLETED)) {
		// learner progress url
		urlMap.put(child.getActivityId(), monitoringService.getLearnerActivityURL(lessonID,
			child.getActivityId(), userID, user.getUserID()));
	    }

	    ContributeActivityDTO dto = new ContributeActivityDTO(child);
	    if (subActivities != null) {
		subActivities.add(dto);
	    }
	    if (parentContributeActivityDTO != null) {
		parentContributeActivityDTO.getChildActivities().add(dto);
	    }

	    if (child.getTransitionFrom() != null) {
		child = child.getTransitionFrom().getToActivity();
	    } else {
		child = null;
	    }
	}
    }
}
