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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
 * <p>
 * When a particular strategy is created, the activity should be
 * stored in the strategy. This allows the strategy to have the
 * correct activity type and (hopefully) stops the need for casting,
 * which is difficult with Hibernates proxied objects. Each strategy
 * needs to define a method getActivity() so that shared methods
 * (such as getContributionType()) can access the activity.
 * <p>
 * 
 * @author Jacky Fang
 * @author Minhas
 * @version 1.1
 */
public abstract class SimpleActivityStrategy implements Serializable, IContributionTypeStrategy {

    //---------------------------------------------------------------------
    // Template methods
    //---------------------------------------------------------------------
    /**
     * Template method that get contribute type according its sub concrete
     * activity.
     * 
     * @param activity
     *            the activity that has contribute type.
     * @return an array of contribute types.
     */
    @Override
    public Integer[] getContributionType() {
	ArrayList<Integer> contributionTypes = new ArrayList<Integer>();

	//abstract method to polymorphically setup contribute type.
	setUpContributionType(contributionTypes);

	return contributionTypes.toArray(new Integer[contributionTypes.size()]);
    }

    //---------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------
    /**
     * Setup contribution type polymorphically according its activity type.
     * 
     * @param activity
     *            the activity that we need to setup contribution type
     *            for.
     * @param contributionTypes
     *            the list that holds contribution types.
     */
    protected abstract void setUpContributionType(ArrayList<Integer> contributionTypes);

    /**
     * Get the activity for this strategy. The activity should be set
     * when the strategy is created.
     */
    protected abstract Activity getActivity();
}