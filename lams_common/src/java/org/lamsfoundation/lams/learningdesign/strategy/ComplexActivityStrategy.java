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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;

/**
 * Typical implementation of strategy pattern avoid complicated conditional
 * logic to process activity object hierarchy. It is responsible for all
 * polymorphism caculation regarding different type of activity.
 *
 * @author Jacky Fang 2005-2-23
 * @version 1.1
 */
public abstract class ComplexActivityStrategy implements Serializable, IContributionTypeStrategy {

    protected static IActivityDAO activityDAO;

    /**
     * <p>
     * Check up all children completion status for a complex activity.
     * </p>
     * Precondition: the activity should a complex activity that has children.
     *
     * @param activity
     *            the complex activity
     * @param learnerProgress
     *            the progress data that record what has been
     *            completed
     * @return true if all children are completed.
     */
    public boolean areChildrenCompleted(LearnerProgress learnerProgress) {
	ComplexActivity complexActivity = getComplexActivity();

	int numOfCompletedActivities = 0;

	if (complexActivity != null) {
	    for (Iterator i = complexActivity.getActivities().iterator(); i.hasNext();) {
		Activity currentActivity = (Activity) i.next();
		if (learnerProgress.getCompletedActivities().containsKey(currentActivity)) {
		    numOfCompletedActivities++;
		}
	    }
	}

	return isComplete(numOfCompletedActivities);

    }

    /**
     * Check up the children activity completion status against number of
     * activities that has been completed within target complex activity.
     * 
     * @param numOfCompletedActivities
     *            the number of completed activities in the
     *            progress data
     * @return true if the completion condition is met.
     */
    protected abstract boolean isComplete(int numOfCompletedActivities);

    /**
     * This method get next activity that should be progressed against the
     * requested incomplete parent activity. This is designed to be used
     * by the progress engine, so for some complex activities it will be the
     * parent activity (for an options activity) or a special waiting activity
     * (for the parallel activity)
     *
     * Changes made to remove casting (by Fiona Malikoff) made the assumption
     * that the parent is always a ComplexActivity. If this is not correct
     * we may have a problem.
     * 
     * @param parent
     *            The requested incomplete parent activity.
     * @param currentChild
     *            the current children we have just completed.
     *
     * @return the activity we should progress to.
     */
    public abstract Activity getNextActivityByParent(ComplexActivity parent, Activity currentChild);

    /**
     * Get the strategy's activity as a Complex Activity. Needed for areChildrenCompleted()
     */
    protected abstract ComplexActivity getComplexActivity();

    /**
     * Set the getActivity() to return the result of getComplexActivity() - no need for
     * subclasses to create two methods that do much the same thing.
     */
    protected Activity getActivity() {
	return getComplexActivity();
    }

    //---------------------------------------------------------------------
    // Implementation of IContributeTypeStrategy
    //---------------------------------------------------------------------
    /**
     * Template method that get contribute type according its sub concrete
     * activity.
     * 
     * @param activity
     *            the activity that has contribute type.
     * @return an array of contribute types.
     */
    @Override
    public Integer[] getContributionType() {
	ArrayList<Integer> contributionTypes = new ArrayList<Integer>();

	//abstract method to polymorphically setup contribute type.
	setUpContributionType(contributionTypes);

	return contributionTypes.toArray(new Integer[contributionTypes.size()]);
    }

    /**
     * Setup contribution type polymorphically according its activity type.
     * Most complex activities don't have a contribution type, so default to setting up nothing
     * 
     * @param contributionTypes
     *            the list that holds contribution types.
     */
    protected void setUpContributionType(ArrayList<Integer> contributionTypes) {

    }

    /**
     * Ugly, but sometimes we need to get real child activity, not proxy
     */
    public void setActivityDAO(IActivityDAO activityDAO) {
	if (ComplexActivityStrategy.activityDAO == null) {
	    ComplexActivityStrategy.activityDAO = activityDAO;
	}
    }
}
