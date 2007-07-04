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

import java.util.ArrayList;
import java.util.Iterator;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.NullActivity;


/**
 * The progress calculation strategy for branching activity. A learner can only do one branch.
 * 
 * @author Mitchell Seaton
 * @version 2.1
 */
public class BranchingActivityStrategy extends ComplexActivityStrategy
{
	
	private static final long serialVersionUID = -1861859105441615028L;

	private BranchingActivity branchingActivity = null;
	
	public BranchingActivityStrategy(BranchingActivity branchingActivity) {
		this.branchingActivity = branchingActivity;
	}

    /**
     * @todo The real strategy is ???
     * 
     * If there isn't a current child, give it any old branch, otherwise return the NulLActivity (can only do one branch).
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#getNextActivityByParent(Activity, Activity)
     */
    public Activity getNextActivityByParent(ComplexActivity activity, Activity currentChild)
    {
   
    	if ( currentChild == null || currentChild.isNull() ) {
    		Iterator iter = activity.getActivities().iterator();
    		if ( iter.hasNext() ) 
    			return (Activity) iter.next();
    	}
    	
        return new NullActivity();
    }

    /**
     * Return the completion status of children activities within a branching
     * activity. A branching activity is marked as complete if one child activity (one branch)
     * is completed.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.ComplexActivityStrategy#isComplete(int)
     */
    protected boolean isComplete(int numOfCompletedActivities)
    {
    	return numOfCompletedActivities == 1;
    }

    /** 
     * Get the strategy's activity as a Complex Activity.  
     */
    protected ComplexActivity getComplexActivity() {
    	return branchingActivity;
    }
    

    /**
     * Setup contribution type for chosen branching activities. 
     * @param contributionTypes the list that holds contribution types.
     */
   protected void setUpContributionType(ArrayList<Integer> contributionTypes)
    {
		if ( branchingActivity != null && branchingActivity.isChosenBranchingActivity()) {
			    contributionTypes.add(ContributionTypes.CHOSEN_BRANCHING);
		}
    }
    

    
}
