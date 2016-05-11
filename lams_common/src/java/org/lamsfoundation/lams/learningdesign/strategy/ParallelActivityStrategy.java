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

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.lesson.ParallelWaitActivity;

/**
 * The progress calculation strategy for parallel activity.
 *
 * @author Jacky Fang 2005-2-24
 * @version 1.1
 */
public class ParallelActivityStrategy extends ComplexActivityStrategy {
    private ParallelActivity parallelActivity = null;

    public ParallelActivityStrategy(ParallelActivity parallelActivity) {
	this.parallelActivity = parallelActivity;
    }

    /**
     * Regarding incomplete parallel activity, the next activity will always
     * be a waiting activity, which will finally translated into waiting
     * message.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity,
     *      Activity)
     */
    @Override
    public Activity getNextActivityByParent(ComplexActivity activity, Activity currentChild) {
	return new ParallelWaitActivity();
    }

    /**
     * Return the completion status of children activities within a parallel
     * activity. A parallel activity is marked as complete if all children
     * activities are completed.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int)
     */
    @Override
    protected boolean isComplete(int numOfCompletedActivities) {
	if (parallelActivity != null) {
	    return numOfCompletedActivities == parallelActivity.getActivities().size() ? true : false;
	} else {
	    return true;
	}
    }

    /**
     * Get the strategy's activity as a Complex Activity.
     */
    @Override
    protected ComplexActivity getComplexActivity() {
	return parallelActivity;
    }
}
