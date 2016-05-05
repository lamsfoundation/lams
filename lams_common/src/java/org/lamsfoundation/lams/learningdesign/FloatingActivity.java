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
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.FloatingActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;

/**
 * @author Mitchell Seaton
 * @version 2.3
 *
 * @hibernate.class
 */
public class FloatingActivity extends ComplexActivity implements Serializable {

    /** preset value for maximum number of floating activities in a design */
    public static final int MAX_NO_OF_ACTIVITIES = 6;

    /** nullable persistent field */
    private Integer maxNumberOfActivities;
    private SystemTool systemTool;

    /** full constructor */
    public FloatingActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Boolean stopAfterActivity, Set inputActivities, Set activities,
	    Activity defaultActivity, Integer maxNumberOfActivities, SystemTool sysTool, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, activities, defaultActivity,
		branchActivityEntries);
	this.maxNumberOfActivities = maxNumberOfActivities;
	this.systemTool = sysTool;
	super.activityStrategy = new FloatingActivityStrategy(this);
    }

    /** default constructor */
    public FloatingActivity() {
	super.activityStrategy = new FloatingActivityStrategy(this);
    }

    /** minimal constructor */
    public FloatingActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Set activities) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, activities);
	super.activityStrategy = new FloatingActivityStrategy(this);
    }

    @Override
    public Activity createCopy(int uiidOffset) {
	FloatingActivity newFloatingActivity = new FloatingActivity();
	copyToNewComplexActivity(newFloatingActivity, uiidOffset);

	/** OptionsActivity Specific Attributes */
	newFloatingActivity.setMaxNumberOfActivities(this.getMaxNumberOfActivities());

	return newFloatingActivity;
    }

    /**
     * @hibernate.property column="max_number_of_options" length="5"
     */
    public Integer getMaxNumberOfActivities() {
	return this.maxNumberOfActivities;
    }

    /**
     * Get the maximum number of activities, guaranteed not to return null. If the value is null in the database,
     * returns the number of activities in this floating activity.
     */
    public Integer getMaxNumberOfActivitiesNotNull() {
	return maxNumberOfActivities != null ? maxNumberOfActivities : getActivities().size();
    }

    public void setMaxNumberOfActivities(Integer maxNumberOfActivities) {
	this.maxNumberOfActivities = maxNumberOfActivities;
    }

    /**
     * @hibernate.many-to-one lazy="false"
     * @hibernate.column name="system_tool_id"
     */
    public SystemTool getSystemTool() {
	return systemTool;
    }

    public void setSystemTool(SystemTool systemTool) {
	this.systemTool = systemTool;
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
