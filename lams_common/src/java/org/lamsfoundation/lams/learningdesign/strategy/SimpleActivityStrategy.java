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

import java.io.Serializable;
import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.Activity;

/**
 * Activity strategy that deals with the calculation for all simple activity
 * strategies. It is the abstraction of all sub-activity strategies. Template
 * method, where possible, should be created to avoid as much duplicate
 * code as possible. Abstract methods sit at this level is designed to be 
 * overidden by sub simple activities to polymorphically achieve the behavior
 * specific to particular conrete activity. 
 * 
 * @author Jacky Fang
 * @author Minhas
 * @version 1.1
 */
public abstract class SimpleActivityStrategy implements Serializable
{
    //---------------------------------------------------------------------
    // Class level constants - Contribution types
    //---------------------------------------------------------------------
    public static final Integer MODERATION = new Integer(1);
    public static final Integer DEFINE_LATER = new Integer(2);
    public static final Integer PERMISSION_GATE = new Integer(3);
    public static final Integer SYNC_GATE = new Integer(4);
    public static final Integer SCHEDULE_GATE = new Integer(5);
    public static final Integer CHOSEN_GROUPING = new Integer(6);
    public static final Integer CONTRIBUTION = new Integer(7);

    //---------------------------------------------------------------------
    // Template methods
    //---------------------------------------------------------------------
    /**
     * Template method that get contribute type according its sub concrete
     * activity.
     * @param activity the activity that has contribute type.
     * @return an array of contribute types.
     */
    public Integer[] getContributionType(Activity activity)
    {
		ArrayList contributionTypes = new ArrayList();
		
		//abstract method to polymorphically setup contribute type.
		setUpContributionType(activity,contributionTypes);
		
		return (Integer[])contributionTypes.toArray(new Integer[contributionTypes.size()]);
    }

    //---------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------
    /**
     * Setup contribution type polymorphically according its activity type.
     * @param activity the activity that we need to setup contribution type
     * 				   for.
     * @param contributionTypes the list that holds contribution types.
     */
    protected abstract void setUpContributionType(Activity activity,ArrayList contributionTypes);
}