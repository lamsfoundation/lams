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
import org.lamsfoundation.lams.learningdesign.FloatingActivity;

/**
 *
 * Progress calculation strategy for floating activity.
 * 
 * @author Mitchell Seaton
 * @version 2.3
 */
public class FloatingActivityStrategy extends ComplexActivityStrategy {
    private FloatingActivity floatingActivity = null;

    public FloatingActivityStrategy(FloatingActivity floatingActivity) {
	this.floatingActivity = floatingActivity;
    }

    /**
     * Return the next activity for a incomplete floating activity. The next activity will calculated by the normal
     * progress state.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity,
     *      Activity)
     */
    @Override
    public Activity getNextActivityByParent(ComplexActivity parent, Activity currentChild) {
	return null;
    }

    /**
     * Return the completion status of children activities within a options
     * activity. For now a floating activity will not have a progress status dependent on the children.
     * There is no rule for a minimum number of activities to be completed.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int,
     *      org.lamsfoundation.lams.learningdesign.ComplexActivity)
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
	return floatingActivity;
    }
}
