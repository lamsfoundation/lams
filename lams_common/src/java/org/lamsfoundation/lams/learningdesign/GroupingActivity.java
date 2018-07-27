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
import org.lamsfoundation.lams.learningdesign.strategy.GroupingActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;

/**
 *
 *
 * A GroupingActivity creates Grouping.
 * 
 * @author Manpreet Minhas
 */
public class GroupingActivity extends SimpleActivity implements Serializable, ISystemToolActivity {
    /** persistent field */
    private SystemTool systemTool;

    /** The grouping_ui_id of the Grouping that this activity creates */
    private Integer createGroupingUIID;

    /** The grouping that this activity creates */
    public Grouping createGrouping;

    /** full constructor */
    public GroupingActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    Grouping createGrouping, Integer grouping_ui_id, Integer create_grouping_ui_id, String languageFile,
	    Boolean stopAfterActivity, Set inputActivities, SystemTool sysTool, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, branchActivityEntries);
	this.createGrouping = createGrouping;
	this.createGroupingUIID = create_grouping_ui_id;
	this.systemTool = sysTool;
	super.simpleActivityStrategy = new GroupingActivityStrategy(this);
    }

    /** default constructor */
    public GroupingActivity() {
	super.simpleActivityStrategy = new GroupingActivityStrategy(this);
    }

    /** minimal constructor */
    public GroupingActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Grouping createGrouping, Integer grouping_ui_id, Integer create_grouping_ui_id) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom);
	this.createGrouping = createGrouping;
	this.createGroupingUIID = create_grouping_ui_id;
	super.simpleActivityStrategy = new GroupingActivityStrategy(this);
    }

    /**
     * This function creates a deep copy of the GroupingActivity of the current object. However each time a
     * GroupingActivity is deep copied it would result in creation of new Groups as well.
     * <p>
     * Note: the grouping object will be created here but the calling class will need to insert it into the database
     * otherwise it won't be persisted (can't easily set it up to cascade). Call getCreateGrouping() to get the grouping
     * object to be stored via the session.
     *
     * @return GroupActivity, which is copy of this activity.
     */
    @Override
    public Activity createCopy(int uiidOffset) {
	GroupingActivity groupingActivity = new GroupingActivity();
	copyToNewActivity(groupingActivity, uiidOffset);
	groupingActivity.setCreateGroupingUIID(LearningDesign.addOffset(this.createGroupingUIID, uiidOffset));
	Grouping currentGrouping = this.getCreateGrouping();
	Grouping newGrouping = currentGrouping.createCopy(uiidOffset);
	groupingActivity.setCreateGrouping(newGrouping);
	return groupingActivity;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * @return Returns the createGrouping.
     */
    public Grouping getCreateGrouping() {
	return createGrouping;
    }

    /**
     * @param createGrouping
     *            The createGrouping to set.
     */
    public void setCreateGrouping(Grouping createGrouping) {
	this.createGrouping = createGrouping;
    }

    public Integer getCreateGroupingUIID() {
	return createGroupingUIID;
    }

    public void setCreateGroupingUIID(Integer create_grouping_ui_id) {
	this.createGroupingUIID = create_grouping_ui_id;
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    @Override
    public SystemTool getSystemTool() {
	return systemTool;
    }

    @Override
    public void setSystemTool(SystemTool systemTool) {
	this.systemTool = systemTool;
    }

    protected void copyToNewActivity(GroupingActivity newActivity, int uiidOffset) {

	super.copyToNewActivity(newActivity, uiidOffset);
	newActivity.setSystemTool(this.getSystemTool());
    }

}
