/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;

/**
 * Activity strategy that deals with the calculation specific to gate activity.
 * The major part of this strategy will be overiding the methods that defined
 * in the abstract level.
 * 
 * @author Jacky Fang
 * @author Minhas
 * @version 1.1
 */
public class PermissionGateActivityStrategy extends GateActivityStrategy {
    
    //---------------------------------------------------------------------
    // Overriden methods
    //---------------------------------------------------------------------
    /**
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#setUpContributionType(org.lamsfoundation.lams.learningdesign.Activity, java.util.ArrayList)
     */
    protected void setUpContributionType(Activity activity, ArrayList contributionTypes)
    {
        contributionTypes.add(PERMISSION_GATE);
    }

    /**
     * Regarding permission gate, we don't validate the open condition for the 
     * learner because the decision of opening the gate or not comes from the 
     * teacher. The teacher may open or close the gate at monitoring interface.
     * 
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#isOpenConditionMet()
     */
    protected boolean isOpenConditionMet(GateActivity activity,List lessonLearners)
    {
        return activity.getGateOpen().booleanValue();
    }

}
