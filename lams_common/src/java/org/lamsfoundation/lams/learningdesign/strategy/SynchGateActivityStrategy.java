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

import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.GateActivity;

/**
 * Activity strategy that deals with the calculation specific to schedule gate activity. The major part of this strategy
 * will be overiding the methods that defined in the abstract level.
 *
 * @author Jacky Fang
 * @author Fiona Malikoff
 */
public class SynchGateActivityStrategy extends GateActivityStrategy {
    public SynchGateActivityStrategy(GateActivity gateActivity) {
	super(gateActivity);
    }

    // ---------------------------------------------------------------------
    // Overriden methods
    // ---------------------------------------------------------------------
    /**
     * <p>
     * Check up the waiting learners list and lesson learner list. If all lesson learner appears in the waiting list, we
     * assume the open condition for the sync gate is met.
     * </p>
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#isOpenConditionMet()
     */
    @Override
    protected boolean isOpenConditionMet(int expectedLearnerCount, int waitingLearnerCount) {
	return waitingLearnerCount >= expectedLearnerCount;
    }

    /**
     * @see org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy#setUpContributionType(org.lamsfoundation.lams.learningdesign.Activity,
     *      java.util.ArrayList)
     */
    @Override
    protected void setUpContributionType(ArrayList<Integer> contributionTypes) {
	contributionTypes.add(ContributionTypes.SYNC_GATE);
    }
}