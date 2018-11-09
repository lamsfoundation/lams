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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.FloatingActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;

/**
 * @author Mitchell Seaton
 */
@Entity
@DiscriminatorValue("15")
public class FloatingActivity extends ComplexActivity implements Serializable {
    private static final long serialVersionUID = 6726503617240839444L;

    /** preset value for maximum number of floating activities in a design */
    public static final int MAX_NO_OF_ACTIVITIES = 6;

    @Column(name = "max_number_of_options")
    private Integer maxNumberOfActivities;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "system_tool_id")
    private SystemTool systemTool;

    /** default constructor */
    public FloatingActivity() {
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

    @Override
    public boolean isNull() {
	return false;
    }
}