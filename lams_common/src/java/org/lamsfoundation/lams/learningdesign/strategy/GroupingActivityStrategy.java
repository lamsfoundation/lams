/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

package org.lamsfoundation.lams.learningdesign.strategy;

import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;

/**
 * Activity strategy that deals with the calculation specific to tool activity.
 * The major part of this strategy will be overiding the methods that defined
 * in the abstract level.
 * 
 * @author Jacky Fang
 * @author Minhas
 * @version 1.1
 */
public class GroupingActivityStrategy extends SimpleActivityStrategy {

    //---------------------------------------------------------------------
    // Overriden methods
    //---------------------------------------------------------------------
    /**
     * @see org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy#setUpContributionType(org.lamsfoundation.lams.learningdesign.Activity, java.util.ArrayList)
     */
    protected void setUpContributionType(Activity activity, ArrayList contributionTypes)
    {
		GroupingActivity groupingActivity =(GroupingActivity)activity;
		if(groupingActivity.getCreateGrouping().getGroupingTypeId()== Grouping.CHOSEN_GROUPING_TYPE)
		    contributionTypes.add(CHOSEN_GROUPING);	
    }

}
