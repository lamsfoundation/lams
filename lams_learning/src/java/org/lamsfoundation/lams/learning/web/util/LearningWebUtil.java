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

package org.lamsfoundation.lams.learning.web.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Jacky Fang
 */
public class LearningWebUtil {
    // ---------------------------------------------------------------------
    // Class level constants - session attributes
    // ---------------------------------------------------------------------

    /**
     * Helper method to retrieve the user data. Gets the id from the user details in the shared session
     *
     * @return the user id
     */
    public static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return learner != null ? learner.getUserID() : null;
    }

    /**
     * Get the current learner progress. Check the request - in some cases it may be there.
     *
     * If not, the learner progress id might be in the request (if we've just come from complete activity). If so, get
     * it from the db using the learner progress.
     *
     * If the learner progress id isn't available, then we have to look it up using activity based on the activity /
     * activity id in the request.
     */
    public static LearnerProgress getLearnerProgress(HttpServletRequest request, ILearnerFullService learnerService) {
	LearnerProgress learnerProgress = null;

	Long learnerProgressId = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNER_PROGRESS_ID, true);
	// temp hack until UI side updates it call.
	if (learnerProgressId == null) {
	    learnerProgressId = WebUtil.readLongParam(request, "progressId", true);
	}

	if (learnerProgressId != null) {
	    learnerProgress = learnerService.getProgressById(learnerProgressId);
	}

	if (learnerProgress == null) {
	    long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	    Activity activity = learnerService.getActivity(activityId);
	    Lesson lesson = learnerService.getLessonByActivity(activity);

	    Integer learnerId = LearningWebUtil.getUserId();
	    learnerProgress = learnerService.getProgress(learnerId, lesson.getLessonId());
	}

	return learnerProgress;
    }

    /**
     * Get the activity from request. We assume there is a parameter coming in. Then the activity id parameter is used
     * to retrieve from database.
     *
     * @param request
     * @return
     */
    public static Activity getActivityFromRequest(HttpServletRequest request, ILearnerFullService learnerService) {
	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Activity activity = learnerService.getActivity(activityId);
	return activity;
    }
}