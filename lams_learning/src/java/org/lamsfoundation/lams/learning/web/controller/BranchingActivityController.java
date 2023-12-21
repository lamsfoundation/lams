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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.web.form.BranchingForm;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Action class to display an OptionsActivity.
 *
 * @author daveg
 */
@Controller
@RequestMapping("/branching")
public class BranchingActivityController {
    private static Logger log = Logger.getLogger(BranchingActivityController.class);

    /** Input parameter. Boolean value */
    public static final String PARAM_FORCE_GROUPING = "force";

    @Autowired
    private ILearnerFullService learnerService;
    @Autowired
    private ActivityMapping activityMapping;
    
    /**
     * Gets an options activity from the request (attribute) and forwards to the display JSP.
     */
    @RequestMapping("/performBranching")
    public String performBranching(@ModelAttribute BranchingForm branchingForm,
	    HttpServletRequest request, HttpServletResponse response) {
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	
	Integer learnerId = LearningWebUtil.getUserId();
	boolean forceGroup = WebUtil.readBooleanParam(request, BranchingActivityController.PARAM_FORCE_GROUPING, false);

	String forward = null;

	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Activity activity = learnerService.getActivity(activityId);
	if (activity == null) {
	    learnerProgress = learnerService.joinLesson(learnerId, learnerProgress.getLesson().getLessonId());
	    forward = activityMapping.getActivityForward(activity, learnerProgress, true);

	} else if (!(activity instanceof BranchingActivity)) {
	    log.error("activity not BranchingActivity " + activity.getActivityId());
	    forward = "error";

	} else {

	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    SequenceActivity branch = learnerService.determineBranch(learnerProgress.getLesson(), branchingActivity,
		    learnerId);

	    branchingForm.setActivityID(activity.getActivityId());
	    branchingForm.setProgressID(learnerProgress.getLearnerProgressId());
	    branchingForm.setShowFinishButton(Boolean.TRUE);
	    branchingForm.setTitle(activity.getTitle());
	    // lessonId needed for the progress bar
	    request.setAttribute(AttributeNames.PARAM_LESSON_ID, learnerProgress.getLesson().getLessonId());

	    if (learnerProgress.getLesson().isPreviewLesson()) {

		// The preview version gives you a choice of branches
		// If a "normal" branch can be determined based on the group, tool marks, etc then it is marked as the default branch

		branchingForm.setPreviewLesson(Boolean.TRUE);
		forward = "branching/preview";

		List<ActivityURL> activityURLs = new ArrayList<>();
		Iterator<Activity> i = branchingActivity.getActivities().iterator();
		int completedCount = 0;
		while (i.hasNext()) {
		    Activity nextBranch = i.next();
		    ActivityURL activityURL = activityMapping.getActivityURL(learnerProgress, nextBranch,
			    (branch != null) && branch.equals(nextBranch), false);
		    if (activityURL.isComplete()) {
			completedCount++;
		    }
		    activityURLs.add(activityURL);
		}
		branchingForm.setActivityURLs(activityURLs);

	    } else if (branch == null) {

		// show the learner waiting page
		branchingForm.setPreviewLesson(Boolean.FALSE);
		forward = "branching/wait";
		branchingForm.setShowNextButton(Boolean.TRUE);

		if (branchingActivity.isChosenBranchingActivity()) {
		    branchingForm.setType(BranchingActivity.CHOSEN_TYPE);
		} else if (branchingActivity.isGroupBranchingActivity()) {
		    branchingForm.setType(BranchingActivity.GROUP_BASED_TYPE);
		} else if (branchingActivity.isToolBranchingActivity()) {
		    branchingForm.setType(BranchingActivity.TOOL_BASED_TYPE);
		}
	    } else {
		// forward to the sequence activity.
		if (log.isDebugEnabled()) {
		    log.debug("Branching: selecting the branch " + branch + " for user " + learnerId);
		}

		// Set the branch as the current part of the sequence and display it
		learnerProgress = learnerService.chooseActivity(learnerId, learnerProgress.getLesson().getLessonId(),
			branch, true);
		forward = activityMapping.getActivityForward(branch, learnerProgress, true);
	    }
	}

	return forward;
    }

    /**
     * We are in the preview lesson and the author has selected a particular branch. Force it to take that branch.
     */
    @RequestMapping("/forceBranching")
    public String forceBranching(@ModelAttribute BranchingForm branchingForm,
	    HttpServletRequest request) {
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Integer learnerId = LearningWebUtil.getUserId();
	String forward = null;

	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Activity activity = learnerService.getActivity(activityId);
	if (activity == null) {
	    learnerProgress = learnerService.joinLesson(learnerId, learnerProgress.getLesson().getLessonId());
	    forward = activityMapping.getActivityForward(activity, learnerProgress, true);

	} else if (!(activity instanceof BranchingActivity)) {
	    log.error("activity not BranchingActivity " + activity.getActivityId());
	    forward = "error";

	} else {
	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    Long branchId = WebUtil.readLongParam(request, "branchID", false);

	    SequenceActivity branch = learnerService.selectBranch(learnerProgress.getLesson(), branchingActivity,
		    learnerId, branchId);

	    if (branch == null) {
		log.error("branch id from request is not valid. Activity id " + activity.getActivityId() + " branch id "
			+ branchId);
		forward = "error";
	    }

	    // forward to the sequence activity.
	    if (log.isDebugEnabled()) {
		log.debug("Branching: selecting the branch " + branch + " for user " + learnerId);
	    }

	    // Set the branch as the current part of the sequence and display it
	    learnerProgress = learnerService.chooseActivity(learnerId, learnerProgress.getLesson().getLessonId(),
		    branch, true);
	    forward = activityMapping.getActivityForward(branch, learnerProgress, true);
	}

	return forward;
    }
}
