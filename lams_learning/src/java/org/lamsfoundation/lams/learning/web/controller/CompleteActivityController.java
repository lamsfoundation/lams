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
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceException;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author daveg
 */
@Controller
public class CompleteActivityController {
    private static Logger log = Logger.getLogger(CompleteActivityController.class);

    protected static String className = "CompleteActivity";

    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;

    /**
     * Sets the current activity as complete and uses the progress engine to find the next activity (may be null).
     *
     * Called when completing an optional activity, or triggered by completeToolSession (via a tool call). The activity
     * to be marked as complete must
     *
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/CompleteActivity")
    public String execute(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	Integer learnerId = LearningWebUtil.getUserId();

	// This must get the learner progress from the progress id, not cached from the request,
	// otherwise we may be using an old version of a lesson while a teacher is starting a
	// live edit, and then the lock flag can't be checked correctly.
	LearnerProgress progress = learnerService
		.getProgressById(WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNER_PROGRESS_ID, true));
	if (!progress.getUser().getUserId().equals(learnerId)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Current learner does not own the given progress");
	    log.error("Learner " + learnerId + " tried to complete an activity for progress "
		    + progress.getLearnerProgressId() + " which does not belong to him");
	    return null;
	}

	long lessonId = progress.getLesson().getLessonId();
	if (progress.isComplete() && progress.getLesson().getAllowLearnerRestart()) {
	    request.setAttribute("lessonID", lessonId);
	}

	try {
	    // LTI Advantage also pushes marks on each activity completion, not only on lesson finish
	    long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	    Activity activity = learnerService.getActivity(activityId);
	    String lessonFinishCallbackUrl = null;
	    if (progress.isComplete() || activity.isToolActivity()) {
		lessonFinishCallbackUrl = integrationService.getLessonFinishCallbackUrl(progress.getUser(),
			progress.getLesson(), activityId);
		if (lessonFinishCallbackUrl != null) {
		    // if user has already completed the lesson - we need to let non-LTI integrations server know to come and pick up
		    // updated marks (as it won't happen at lessoncomplete.jsp page)
		    request.setAttribute("lessonFinishUrl", lessonFinishCallbackUrl);
		}
	    }
	    // Set activity as complete

	    String forward = learnerService.completeActivity(activityMapping, progress, activity, learnerId, false);
	    if (lessonFinishCallbackUrl != null && forward.startsWith("redirect")) {
		// loadToolActivity.jsp will make an Ajax call to LTI Advantage servlet
		forward = WebUtil.appendParameterToURL(forward, "activityFinishUrl",
			URLEncoder.encode(lessonFinishCallbackUrl, "UTF8"));
		if (progress.isComplete()) {
		    // so we can update the last activity score even on lesson finish
		    forward = WebUtil.appendParameterToURL(forward, "finishedActivityId", String.valueOf(activityId));
		}
	    }

	    return forward;
	} catch (LearnerServiceException e) {
	    return "error";
	}
    }
}