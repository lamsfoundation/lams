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
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.OptionsActivityStrategy;

/**
 * @author Manpreet Minhas
 * @hibernate.class
 */
public class OptionsActivity extends ComplexActivity implements Serializable {

    /** nullable persistent field */
    private Integer maxNumberOfOptions;

    /** nullable persistent field */
    private Integer minNumberOfOptions;

    /** nullable persistent field */
    private String optionsInstructions;

    /** full constructor */
    public OptionsActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Boolean stopAfterActivity, Set inputActivities, Set activities,
	    Activity defaultActivity, Integer maxNumberOfOptions, Integer minNumberOfOptions,
	    String options_instructions, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, activities, defaultActivity,
		branchActivityEntries);
	this.maxNumberOfOptions = maxNumberOfOptions;
	this.minNumberOfOptions = minNumberOfOptions;
	this.optionsInstructions = options_instructions;
	super.activityStrategy = new OptionsActivityStrategy(this);
    }

    /** default constructor */
    public OptionsActivity() {
	super.activityStrategy = new OptionsActivityStrategy(this);
    }

    /** minimal constructor */
    public OptionsActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Set activities) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, activities);
	super.activityStrategy = new OptionsActivityStrategy(this);
    }

    public Activity createCopy(int uiidOffset) {
	OptionsActivity newOptionsActivity = new OptionsActivity();
	copyToNewComplexActivity(newOptionsActivity, uiidOffset);

	/** OptionsActivity Specific Attributes */
	newOptionsActivity.setMaxNumberOfOptions(this.getMaxNumberOfOptions());
	newOptionsActivity.setMinNumberOfOptions(this.getMinNumberOfOptions());
	newOptionsActivity.setOptionsInstructions(this.getOptionsInstructions());

	return newOptionsActivity;
    }

    /**
     * @hibernate.property column="max_number_of_options" length="5"
     */
    public Integer getMaxNumberOfOptions() {
	return this.maxNumberOfOptions;
    }

    /**
     * Get the maximum number of options, guaranteed not to return null. If the value is null in the database, returns
     * the number of activities in this optional activity.
     */
    public Integer getMaxNumberOfOptionsNotNull() {
	return maxNumberOfOptions != null ? maxNumberOfOptions : getActivities().size();
    }

    public void setMaxNumberOfOptions(Integer maxNumberOfOptions) {
	this.maxNumberOfOptions = maxNumberOfOptions;
    }

    /**
     * @hibernate.property column="min_number_of_options" length="5"
     */
    public Integer getMinNumberOfOptions() {
	return this.minNumberOfOptions;
    }

    /**
     * Get the manimum number of options, guaranteed not to return null. If the value is null in the database, returns 0
     */
    public Integer getMinNumberOfOptionsNotNull() {
	return minNumberOfOptions != null ? minNumberOfOptions : new Integer(0);
    }

    public void setMinNumberOfOptions(Integer minNumberOfOptions) {
	this.minNumberOfOptions = minNumberOfOptions;
    }

    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    public String getOptionsInstructions() {
	return optionsInstructions;
    }

    public void setOptionsInstructions(String options_instructions) {
	this.optionsInstructions = options_instructions;
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull() {
	return false;
    }
}
