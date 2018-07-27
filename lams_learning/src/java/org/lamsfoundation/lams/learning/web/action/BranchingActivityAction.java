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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.learning.service.ICoreLearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Action class to display an OptionsActivity.
 *
 * @author daveg
 *
 *         XDoclet definition:
 *
 *
 *
 *
 *
 *
 */
public class BranchingActivityAction extends LamsDispatchAction {

    /** Input parameter. Boolean value */
    public static final String PARAM_FORCE_GROUPING = "force";

    private ICoreLearnerService learnerService = null;

    protected ICoreLearnerService getLearnerService() {
	if (learnerService == null) {
	    learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	}
	return learnerService;
    }

    /**
     * Gets an options activity from the request (attribute) and forwards to the display JSP.
     */
    public ActionForward performBranching(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ActivityMapping actionMappings = LearningWebUtil.getActivityMapping(this.getServlet().getServletContext());
	getLearnerService(); // initialise the learner service, if necessary

	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
	Integer learnerId = LearningWebUtil.getUserId();
	boolean forceGroup = WebUtil.readBooleanParam(request, BranchingActivityAction.PARAM_FORCE_GROUPING, false);

	ActionForward forward = null;

	if (activity == null) {
	    learnerProgress = learnerService.joinLesson(learnerId, learnerProgress.getLesson().getLessonId());
	    forward = actionMappings.getActivityForward(activity, learnerProgress, true);

	} else if (!(activity instanceof BranchingActivity)) {
	    LamsDispatchAction.log.error(
		    LamsDispatchAction.className + ": activity not BranchingActivity " + activity.getActivityId());
	    forward = mapping.findForward(ActivityMapping.ERROR);

	} else {

	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    SequenceActivity branch = learnerService.determineBranch(learnerProgress.getLesson(), branchingActivity,
		    learnerId);

	    DynaActionForm branchForm = (DynaActionForm) actionForm;
	    branchForm.set("activityID", activity.getActivityId());
	    branchForm.set("progressID", learnerProgress.getLearnerProgressId());
	    branchForm.set("showFinishButton", Boolean.TRUE);
	    branchForm.set("title", activity.getTitle());

	    if (learnerProgress.getLesson().isPreviewLesson()) {

		// The preview version gives you a choice of branches
		// If a "normal" branch can be determined based on the group, tool marks, etc then it is marked as the default branch

		branchForm.set("previewLesson", Boolean.TRUE);
		forward = mapping.findForward("displayBranchingPreview");

		List<ActivityURL> activityURLs = new ArrayList<ActivityURL>();
		Iterator i = branchingActivity.getActivities().iterator();
		int completedCount = 0;
		while (i.hasNext()) {
		    Activity nextBranch = (Activity) i.next();
		    ActivityURL activityURL = LearningWebUtil.getActivityURL(actionMappings, learnerProgress,
			    nextBranch, (branch != null) && branch.equals(nextBranch), false);
		    if (activityURL.isComplete()) {
			completedCount++;
		    }
		    activityURLs.add(activityURL);
		}
		branchForm.set("activityURLs", activityURLs);

	    } else if (branch == null) {

		// show the learner waiting page
		branchForm.set("previewLesson", Boolean.FALSE);
		forward = mapping.findForward("displayBranchingWait");
		branchForm.set("showNextButton", Boolean.TRUE);

		if (branchingActivity.isChosenBranchingActivity()) {
		    branchForm.set("type", BranchingActivity.CHOSEN_TYPE);
		} else if (branchingActivity.isGroupBranchingActivity()) {
		    branchForm.set("type", BranchingActivity.GROUP_BASED_TYPE);
		} else if (branchingActivity.isToolBranchingActivity()) {
		    branchForm.set("type", BranchingActivity.TOOL_BASED_TYPE);
		}
		// lessonId needed for the progress bar
		request.setAttribute(AttributeNames.PARAM_LESSON_ID, learnerProgress.getLesson().getLessonId());
	    } else {
		// forward to the sequence activity.
		if (LamsDispatchAction.log.isDebugEnabled()) {
		    LamsDispatchAction.log
			    .debug("Branching: selecting the branch " + branch + " for user " + learnerId);
		}

		// Set the branch as the current part of the sequence and display it
		learnerProgress = learnerService.chooseActivity(learnerId, learnerProgress.getLesson().getLessonId(),
			branch, true);
		forward = actionMappings.getActivityForward(branch, learnerProgress, true);
	    }
	}

	return forward;
    }

    /**
     * We are in the preview lesson and the author has selected a particular branch. Force it to take that branch.
     */
    public ActionForward forceBranching(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	ActivityMapping actionMappings = LearningWebUtil.getActivityMapping(this.getServlet().getServletContext());
	getLearnerService(); // initialise the learner service, if necessary

	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
	Integer learnerId = LearningWebUtil.getUserId();
	ActionForward forward = null;

	if (activity == null) {
	    learnerProgress = learnerService.joinLesson(learnerId, learnerProgress.getLesson().getLessonId());
	    forward = actionMappings.getActivityForward(activity, learnerProgress, true);

	} else if (!(activity instanceof BranchingActivity)) {
	    LamsDispatchAction.log.error(
		    LamsDispatchAction.className + ": activity not BranchingActivity " + activity.getActivityId());
	    forward = mapping.findForward(ActivityMapping.ERROR);

	} else {

	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    Long branchId = WebUtil.readLongParam(request, "branchID", false);

	    SequenceActivity branch = learnerService.selectBranch(learnerProgress.getLesson(), branchingActivity,
		    learnerId, branchId);

	    if (branch == null) {
		LamsDispatchAction.log
			.error(LamsDispatchAction.className + ": branch id from request is not valid. Activity id "
				+ activity.getActivityId() + " branch id " + branchId);
		forward = mapping.findForward(ActivityMapping.ERROR);
	    }

	    // forward to the sequence activity.
	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Branching: selecting the branch " + branch + " for user " + learnerId);
	    }

	    // Set the branch as the current part of the sequence and display it
	    learnerProgress = learnerService.chooseActivity(learnerId, learnerProgress.getLesson().getLessonId(),
		    branch, true);
	    forward = actionMappings.getActivityForward(branch, learnerProgress, true);
	}

	return forward;
    }
}
