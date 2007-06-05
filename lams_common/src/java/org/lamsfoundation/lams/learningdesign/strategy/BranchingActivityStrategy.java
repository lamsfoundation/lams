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
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign.strategy;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;


/**
 * The progress calculation strategy for branching activity.
 * 
 * @author Mitchell Seaton
 * @version 2.1
 */
public class BranchingActivityStrategy extends ComplexActivityStrategy
{
	private BranchingActivity branchingActivity = null;
	
	public BranchingActivityStrategy(BranchingActivity branchingActivity) {
		this.branchingActivity = branchingActivity;
	}

    /**
     * Regarding incomplete parallel activity, the next activity will always
     * be a waiting activity, which will finally translated into waiting
     * message.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity, Activity)
     */
    public Activity getNextActivityByParent(ComplexActivity activity, Activity currentChild)
    {
        return null;
    }

    /**
     * Return the completion status of children activities within a branching
     * activity. A branching activity is marked as complete if all children
     * activities are completed.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int)
     */
    protected boolean isComplete(int numOfCompletedActivities)
    {
    	if ( branchingActivity != null ) {
    		return numOfCompletedActivities==branchingActivity.getActivities().size()?true:false;
    	} else {
    		return true;
    	}
    }

    /** 
     * Get the strategy's activity as a Complex Activity.  
     */
    protected ComplexActivity getComplexActivity() {
    	return branchingActivity;
    }
}
