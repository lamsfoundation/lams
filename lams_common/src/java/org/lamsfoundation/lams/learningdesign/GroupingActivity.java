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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.GroupingActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;

/**
 * A GroupingActivity creates Grouping.
 *
 * @author Manpreet Minhas
 */
@Entity
@DiscriminatorValue("2")
public class GroupingActivity extends SimpleActivity implements Serializable, ISystemToolActivity {
    private static final long serialVersionUID = -6876312193538019171L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "system_tool_id")
    private SystemTool systemTool;

    /** The grouping_ui_id of the Grouping that this activity creates */
    @Column(name = "create_grouping_ui_id")
    private Integer createGroupingUIID;

    /** The grouping that this activity creates */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_grouping_id")
    public Grouping createGrouping;

    /** default constructor */
    public GroupingActivity() {
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

    public Grouping getCreateGrouping() {
	return createGrouping;
    }

    public void setCreateGrouping(Grouping createGrouping) {
	this.createGrouping = createGrouping;
    }

    public Integer getCreateGroupingUIID() {
	return createGroupingUIID;
    }

    public void setCreateGroupingUIID(Integer create_grouping_ui_id) {
	this.createGroupingUIID = create_grouping_ui_id;
    }

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