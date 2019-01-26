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

package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;
import java.util.Iterator;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolBranchingActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;

/**
 * The progress calculation strategy for branching activity. A learner can only do one branch.
 *
 * @author Mitchell Seaton
 * @version 2.1
 */
public class BranchingActivityStrategy extends ComplexActivityStrategy {

    private static final long serialVersionUID = -1861859105441615028L;

    private BranchingActivity branchingActivity = null;

    public BranchingActivityStrategy(BranchingActivity branchingActivity) {
	this.branchingActivity = branchingActivity;
    }

    /**
     * Return the next activity for a incomplete branching activity. In terms of
     * incomplete branching activity, the next activity will always be the branching
     * activity itself so as to select a branch either determined by the server or to display
     * the wait screen (for the normal learner) or for the author to select a particular
     * branch (in preview).
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity,
     *      Activity)
     */
    @Override
    public Activity getNextActivityByParent(ComplexActivity parent, Activity currentChild) {
	return parent;
    }

    /**
     * <p>
     * Check up all children completion status for a branching activity. Normally
     * it will be complete if one branch is complete, but if this is preview, then only complete
     * the branch when all branches have been completed.
     * </p>
     *
     * Precondition: the activity should a complex activity that has children.
     *
     * @param activity
     *            the complex activity
     * @param learnerProgress
     *            the progress data that record what has been
     *            completed
     * @return true if all children are completed.
     */
    @Override
    public boolean areChildrenCompleted(LearnerProgress learnerProgress) {
	BranchingActivity branchingActivity = (BranchingActivity) getComplexActivity();
	boolean isPreview = learnerProgress.getLesson().isPreviewLesson();
	Boolean isOrderedAsc = branchingActivity.isToolBranchingActivity()
		? ((ToolBranchingActivity) branchingActivity).getBranchingOrderedAsc()
		: null;

	if (branchingActivity != null && branchingActivity.getActivities().size() > 0) {
	    for (Iterator i = branchingActivity.getActivities().iterator(); i.hasNext();) {
		// we need the real activity, not proxy
		SequenceActivity sequenceActivity = (SequenceActivity) activityDAO
			.getActivityByActivityId(((Activity) i.next()).getActivityId());
		boolean actComplete = learnerProgress.getCompletedActivities().containsKey(sequenceActivity);
		if (actComplete) {
		    // if activity is complete and it is not preview nor ordered branching where all branches need to get passed,
		    // then branching activity is complete
		    if (!isPreview && isOrderedAsc == null) {
			return true;
		    }
		    // if it is preview or there are not completed branches with conditions in ordered branching,
		    // then this branching activity is not finished yet
		} else if (isPreview || (isOrderedAsc != null && !sequenceActivity.getBranchEntries().isEmpty())) {
		    return false;
		}
	    }
	    // We've checked all the activities. If we are in preview or ordered branching, then they are all
	    // completed so return true. If we are not in preview (ie normal learner) then
	    // we haven't found any that were completed so we return false. So we can just
	    // return isPreview as that is the correct boolean state.
	    return isPreview || isOrderedAsc != null;
	}

	// didn't find any child activities so we are complete
	return true;
    }

    /**
     * Do not use (for this class only) - isComplete() is not called by the areChildrenCompleted() method in this class.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int)
     */
    @Override
    protected boolean isComplete(int numOfCompletedActivities) {
	return true;
    }

    /**
     * Get the strategy's activity as a Complex Activity.
     */
    @Override
    protected ComplexActivity getComplexActivity() {
	return branchingActivity;
    }

    /**
     * Setup contribution type for chosen branching activities.
     *
     * @param contributionTypes
     *            the list that holds contribution types.
     */
    @Override
    protected void setUpContributionType(ArrayList<Integer> contributionTypes) {
	if (branchingActivity != null) {
	    if (branchingActivity.isChosenBranchingActivity()) {
		contributionTypes.add(ContributionTypes.CHOSEN_BRANCHING);
	    }
	}
    }

}
