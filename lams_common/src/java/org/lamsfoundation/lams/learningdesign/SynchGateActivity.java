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

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.SynchGateActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;

/**
 * @author Manpreet Minhas
 *
 */
public class SynchGateActivity extends GateActivity implements Serializable {

    /** full constructor */
    public SynchGateActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, String languageFile, Boolean stopAfterActivity, Set inputActivities,
	    Integer gateActivityLevelId, Set waitingLearners, SystemTool sysTool, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, gateActivityLevelId, sysTool,
		branchActivityEntries);
	super.simpleActivityStrategy = new SynchGateActivityStrategy(this);
    }

    /** default constructor */
    public SynchGateActivity() {
	super.simpleActivityStrategy = new SynchGateActivityStrategy(this);
    }

    /** minimal constructor */
    public SynchGateActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Integer gateActivityLevelId, Set waitingLearners) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, gateActivityLevelId);
	super.simpleActivityStrategy = new SynchGateActivityStrategy(this);
    }

    /**
     * Makes a copy of the SynchGateActivity for authoring, preview and monitoring enviornment
     *
     * @return SynchGateActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {
	SynchGateActivity newSynchGateActivity = new SynchGateActivity();
	copyToNewActivity(newSynchGateActivity, uiidOffset);
	newSynchGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
	newSynchGateActivity.setGateOpen(new Boolean(false));
	return newSynchGateActivity;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

}
