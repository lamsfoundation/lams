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
import java.util.List;

import org.lamsfoundation.lams.learningdesign.GateActivity;


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
public class ScheduleGateActivityStrategy extends GateActivityStrategy
{
	public ScheduleGateActivityStrategy(GateActivity gateActivity) {
		super(gateActivity);
	}


    //---------------------------------------------------------------------
    // Overriden methods
    //---------------------------------------------------------------------

    /**
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#setUpContributionType(org.lamsfoundation.lams.learningdesign.Activity, java.util.ArrayList)
     */
    protected void setUpContributionType(ArrayList contributionTypes)
    {
        contributionTypes.add(SCHEDULE_GATE);        
    }

    /**
     * Regarding schedule gate, we don't validate the open condition for the 
     * learner because the decision of opening the gate or not comes from the 
     * system scheduler. Lams opens the gate when the start time is reached and 
     * closes the gate when the end time is reached.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#isOpenConditionMet()
     */
    protected boolean isOpenConditionMet(List lessonLearners)
    {
    	if ( gateActivity != null ) {
    		return gateActivity.getGateOpen().booleanValue();
    	}
    	return true;
    }
}
