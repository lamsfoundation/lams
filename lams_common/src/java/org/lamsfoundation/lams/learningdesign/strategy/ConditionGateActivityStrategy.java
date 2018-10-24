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

import java.util.ArrayList;

import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.GateActivity;

/**
 * Gate activity strategy that is based on tools' output and conditions.
 *
 * @author Marcin Cieslak
 */
public class ConditionGateActivityStrategy extends GateActivityStrategy {

    public ConditionGateActivityStrategy(GateActivity gateActivity) {
	super(gateActivity);
    }

    // ---------------------------------------------------------------------
    // Overriden methods
    // ---------------------------------------------------------------------
    /**
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#setUpContributionType(org.lamsfoundation.lams.learningdesign.Activity,
     *      java.util.ArrayList)
     */
    @Override
    protected void setUpContributionType(ArrayList<Integer> contributionTypes) {
	contributionTypes.add(ContributionTypes.CONDITION_GATE);
    }

    /**
     * Since the gate is open only for individuals and never for the whole class, this method returns <code>false</code>
     * .
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#isOpenConditionMet()
     */
    @Override
    protected boolean isOpenConditionMet(int expectedLearnerCount, int waitingLearnerCount) {
	return false;
    }
}