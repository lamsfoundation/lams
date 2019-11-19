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

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Action class to display a sequence activity.
 *
 * Normally this will display the first activity inside a sequence activity. If there are no activities within the
 * sequence activity then it will display an "empty" message.
 */
@Controller
public class SequenceActivityController {
    private static Logger log = Logger.getLogger(SequenceActivityController.class);
    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;

    /**
     * Gets an sequence activity from the request (attribute) and forwards to either the first activity in the sequence
     * activity or the "empty" JSP.
     *
     * @throws UnsupportedEncodingException
     * @throws LearnerServiceException
     */
    @RequestMapping("/SequenceActivity")
    public String execute(HttpServletRequest request, HttpServletResponse response)
	    throws LearnerServiceException, UnsupportedEncodingException {
	Integer learnerId = LearningWebUtil.getUserId();

	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);

	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Activity activity = learnerService.getActivity(activityId);
	if (!(activity instanceof SequenceActivity)) {
	    log.error("activity not SequenceActivity " + activity.getActivityId());
	    return "error";
	}

	SequenceActivity sequenceActivity = (SequenceActivity) activity;

	Activity firstIncompletedActivity = null;
	// getActivities is ordered by order id
	for (Activity activityIter : sequenceActivity.getActivities()) {
	    if (!learnerProgress.getCompletedActivities().containsKey(activityIter)) {
		firstIncompletedActivity = activityIter;
		break;
	    }
	}

	if (firstIncompletedActivity != null) {
	    // Set the first incompleted activity as the current activity and display it
	    learnerProgress = learnerService.chooseActivity(learnerId, learnerProgress.getLesson().getLessonId(),
		    firstIncompletedActivity, true);
	    return activityMapping.getActivityForward(firstIncompletedActivity, learnerProgress, true);

	} else {
	    // No activities exist in the sequence, so go to the next activity.
	    return learnerService.completeActivity(activityMapping, learnerProgress, activity, learnerId, true);
	}
    }
}