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
import org.lamsfoundation.lams.learningdesign.OptionsActivity;

/**
 *
 * Progress calculation strategy for options activity.
 * 
 * @author Jacky Fang 2005-2-24
 * @version 1.1
 */
public class OptionsActivityStrategy extends ComplexActivityStrategy {
    private OptionsActivity optionsActivity = null;

    public OptionsActivityStrategy(OptionsActivity optionsActivity) {
	this.optionsActivity = optionsActivity;
    }

    /**
     * Return the next activity for a incomplete options activity. In terms of
     * incomplete option activity, the next activity will always be the option
     * activity itself so as to display the options page again when the user
     * finish one option.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity,
     *      Activity)
     */
    @Override
    public Activity getNextActivityByParent(ComplexActivity parent, Activity currentChild) {
	return parent;
    }

    /**
     * Return the completion status of children activities within a options
     * activity. A option activity is marked as complete if the maximum
     * number of options requirement is met.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int,
     *      org.lamsfoundation.lams.learningdesign.ComplexActivity)
     */
    @Override
    protected boolean isComplete(int numOfCompletedActivities) {
	if (optionsActivity != null) {

	    return numOfCompletedActivities >= optionsActivity.getMaxNumberOfOptionsNotNull().intValue() ? true : false;
	}
	return true;
    }

    /**
     * Get the strategy's activity as a Complex Activity.
     */
    @Override
    protected ComplexActivity getComplexActivity() {
	return optionsActivity;
    }
}
