/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * Activity strategy that deals with the calculation specific to schedule gate 
 * activity. The major part of this strategy will be overiding the methods that 
 * defined in the abstract level.
 * 
 * @author Jacky Fang
 * @since  2005-4-6
 * @version 1.1
 * 
 */
public class SynchGateActivityStrategy extends GateActivityStrategy
{
	public SynchGateActivityStrategy(GateActivity gateActivity) {
		super(gateActivity);
	}

    //---------------------------------------------------------------------
    // Overriden methods
    //---------------------------------------------------------------------
    /**
     * <p>Check up the waiting learners list and lesson learner list. If all 
     * lesson learner appears in the waiting list, we assume the open condition
     * for the sync gate is met. </p>
     * 
     * <p>Note, simply compares the size of two list might be proper. Waiting
     * learners might not want to wait any more and exit the lesson, who will
     * be removed from current lesson learner list. Therefore, it is possible
     * that the waiting learner list is larger than the current lesson learner
     * list. </p>
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#isOpenConditionMet()
     */
    protected boolean isOpenConditionMet(List lessonLearners)
    {
    	if ( gateActivity != null ) {
	        for(Iterator i = lessonLearners.iterator();i.hasNext();)
	        {
	            User learner = (User)i.next();
	            if (!gateActivity.getWaitingLearners().contains(learner))
	                return false;
	        }
    	}
        return true;
    }
    
    /**
     * @see org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy#setUpContributionType(org.lamsfoundation.lams.learningdesign.Activity, java.util.ArrayList)
     */
    protected void setUpContributionType(ArrayList contributionTypes)
    {
        contributionTypes.add(SYNC_GATE);   
    }


}
