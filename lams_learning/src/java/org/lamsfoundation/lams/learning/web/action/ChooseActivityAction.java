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


package org.lamsfoundation.lams.learning.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.web.action.LamsAction;

/**
 * @author daveg
 */
public class ChooseActivityAction extends ActivityAction {

    protected static String className = "ChooseActivity";

    /**
     * Gets an activity from the request (attribute) and forwards onto the required jsp (SingleActivity or
     * ParallelActivity).
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	ActivityMapping actionMappings = LearningWebUtil.getActivityMapping(this.getServlet().getServletContext());

	// check token
	if (!this.isTokenValid(request, true)) {
	    // didn't come here from options page
	    LamsAction.log.info(ChooseActivityAction.className + ": No valid token in request");
	    return mapping.findForward(ActivityMapping.DOUBLE_SUBMIT_ERROR);
	}

	ILearnerFullService learnerService = getLearnerService();

	// Get learner and lesson details.
	Integer learnerId = LearningWebUtil.getUserId();
	LearnerProgress progress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Lesson lesson = progress.getLesson();

	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);

	if (activity != null) {
	    progress = learnerService.chooseActivity(learnerId, lesson.getLessonId(), activity, false);
	} else {
	    // Something has gone wrong - maybe due to Live Edit. Need to recalculate their current location.
	    progress = learnerService.joinLesson(learnerId, lesson.getLessonId());
	}

	ActionForward forward = actionMappings.getActivityForward(activity, progress, true);
	return forward;
    }
}