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

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.NullActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;

/**
 * Progress calculation strategy for sequence activity.
 *
 * @author Jacky Fang, Fiona Malikoff
 */
public class SequenceActivityStrategy extends ComplexActivityStrategy {
    protected Logger log = Logger.getLogger(SequenceActivityStrategy.class);

    protected SequenceActivity sequenceActivity = null;

    public SequenceActivityStrategy(SequenceActivity sequenceActivity) {
	this.sequenceActivity = sequenceActivity;
    }

    /**
     * <p>
     * Return the next activity for a incomplete sequence activity.
     *
     * <p>
     * Normally all the activities in a sequence activity will be linked
     * via transitions, so we are only interested in child activities that
     * start a series of linked activities.
     *
     * <p>
     * If the sequence has a firstActivity (according to the firstActivity field)
     * then: If the currentChild is the NullActivity then it will return the first
     * Activity, otherwise it will return the NullActivity.
     *
     * <p>
     * If no firstActivity exists then it looks for the child activity that doesn't
     * have in input transition with the order id greater the
     * currentChild. If the currentChild is the NullActivity then
     * it will return the first such child. This case should never be used but it is
     * a backup in case the firstChild isn't set for some reason.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity,
     *      Activity)
     */
    @Override
    public Activity getNextActivityByParent(ComplexActivity parent, Activity currentChild) {
	Activity firstActivity = sequenceActivity.getDefaultActivity();
	if (firstActivity != null) {
	    return (currentChild == null || currentChild.isNull()) ? firstActivity : new NullActivity();
	}

	if (sequenceActivity.getActivities().size() > 0) {

	    log.warn(
		    "getNextActivityByParent: child activities exist but no firstActivity is set up. Trying to work it out based on transitions. SequenceActivity "
			    + sequenceActivity);

	    Activity inputChild = currentChild;
	    if (inputChild == null) {
		inputChild = new NullActivity();
	    }

	    // getActivities is ordered by order id ()
	    boolean pastCurrentChild = false;
	    Iterator i = sequenceActivity.getActivities().iterator();
	    while (i.hasNext()) {
		Activity curChild = (Activity) i.next();
		if ((inputChild.isNull() || pastCurrentChild) && curChild.getTransitionTo() == null) {
		    return curChild;
		} else if (inputChild.equals(curChild)) {
		    pastCurrentChild = true;
		}
	    }
	}

	return new NullActivity();
    }

    /**
     * Implementation for sequence activity. It returns true if all children
     * of this sequence activity appear in the completed activities set from
     * current learner progress.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int,
     *      org.lamsfoundation.lams.learningdesign.ComplexActivity)
     */
    @Override
    protected boolean isComplete(int numOfCompletedActivities) {
	if (sequenceActivity != null) {
	    return numOfCompletedActivities == sequenceActivity.getActivities().size() ? true : false;
	}
	return true;
    }

    /**
     * Get the strategy's activity as a Complex Activity.
     */
    @Override
    protected ComplexActivity getComplexActivity() {
	return sequenceActivity;
    }
}
