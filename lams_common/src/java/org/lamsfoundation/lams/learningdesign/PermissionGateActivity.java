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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.PermissionGateActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;

/**
 * @author Manpreet Minhas
 *
 */
@Entity
@DiscriminatorValue("5")
public class PermissionGateActivity extends GateActivity implements Serializable {
    private static final long serialVersionUID = 7871269712887929284L;

    /** full constructor */
    public PermissionGateActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Boolean stopAfterActivity, Set<Activity> inputActivities, Integer gateActivityLevelId,
	    SystemTool sysTool, Set<BranchActivityEntry> branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, gateActivityLevelId, sysTool,
		branchActivityEntries);
	super.simpleActivityStrategy = new PermissionGateActivityStrategy(this);
    }

    /** default constructor */
    public PermissionGateActivity() {
	super.simpleActivityStrategy = new PermissionGateActivityStrategy(this);
    }

    /** minimal constructor */
    public PermissionGateActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Integer gateActivityLevelId) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, gateActivityLevelId);
	super.simpleActivityStrategy = new PermissionGateActivityStrategy(this);
    }

    /**
     * Makes a copy of the PermissionGateActivity for authoring, preview and monitoring enviornment
     *
     * @return PermissionGateActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {
	PermissionGateActivity newPermissionGateActivity = new PermissionGateActivity();
	copyToNewActivity(newPermissionGateActivity, uiidOffset);
	newPermissionGateActivity.setGateOpen(false);
	newPermissionGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
	newPermissionGateActivity.setGateStopAtPrecedingActivity(this.getGateStopAtPrecedingActivity());
	return newPermissionGateActivity;

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