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
import java.util.Date;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.lesson.CompletedActivityProgress;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Activity strategy that deals with the calculation specific to schedule gate activity. The major part of this strategy
 * will be overiding the methods that defined in the abstract level.
 *
 * @author Jacky Fang
 */
public class ScheduleGateActivityStrategy extends GateActivityStrategy {
    private static final long serialVersionUID = -6702911163273937832L;

    public ScheduleGateActivityStrategy(GateActivity gateActivity) {
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
	contributionTypes.add(ContributionTypes.SCHEDULE_GATE);
    }

    /**
     * Regarding schedule gate, we don't validate the open condition for the learner because the decision of opening the
     * gate or not comes from the system scheduler. Lams opens the gate when the start time is reached and closes the
     * gate when the end time is reached.
     *
     * @see org.lamsfoundation.lams.learningdesign.strategy.GateActivityStrategy#isOpenConditionMet()
     */
    @Override
    protected boolean isOpenConditionMet(int expectedLearnerCount, int waitingLearnerCount) {
	return false;
    }

    @Override
    public boolean shouldOpenGateFor(User learner, int expectedLearnerCount, int waitingLearnerCount) {
	ScheduleGateActivity scheduleGate = (ScheduleGateActivity) gateActivity;
	if (Boolean.TRUE.equals((scheduleGate.getGateActivityCompletionBased()))) {
	    Date previousActivityCompletionTime = ScheduleGateActivityStrategy
		    .getPreviousActivityCompletionDate(scheduleGate, learner);
	    if (previousActivityCompletionTime != null) {
		Date openTime = scheduleGate.getGateOpenTime(previousActivityCompletionTime);
		if (openTime.compareTo(new Date()) <= 0) {
		    gateActivity.getAllowedToPassLearners().add(learner);
		    return true;
		}
	    }
	    return false;
	}
	return super.shouldOpenGateFor(learner, expectedLearnerCount, waitingLearnerCount);
    }

    public static Date getPreviousActivityCompletionDate(ScheduleGateActivity scheduleGate, User learner) {
	Activity previousActivity = scheduleGate.getTransitionTo().getFromActivity();
	if (previousActivity != null) {
	    for (LearnerProgress progress : (Set<LearnerProgress>) learner.getLearnerProgresses()) {
		CompletedActivityProgress completion = progress.getCompletedActivities().get(previousActivity);
		if (completion != null) {
		    return completion.getFinishDate();
		}
	    }
	}
	return null;
    }
}