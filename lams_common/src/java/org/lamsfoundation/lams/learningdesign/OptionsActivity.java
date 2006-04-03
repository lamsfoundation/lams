/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
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
    public OptionsActivity(Long activityId, 
            Integer id, 
            String description, 
            String title, 
            Integer xcoord, 
            Integer ycoord, 
            Integer orderId, 
            Boolean defineLater, 
            java.util.Date createDateTime, 
            LearningLibrary learningLibrary, 
            Activity parentActivity, 
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign, 
            Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            String languageFile,
            Set activities, 
            Integer maxNumberOfOptions, 
            Integer minNumberOfOptions,
			String options_instructions) {
        super(activityId, 
                id, 
                description, 
                title, 
                xcoord, 
                ycoord, 
                orderId, 
                defineLater, 
                createDateTime, 
                learningLibrary, 
                parentActivity, 
				libraryActivity,
				parentUIID,
                learningDesign, 
                grouping, 
                activityTypeId,  
                transitionTo,
				transitionFrom,
				languageFile,
                activities);
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
    public OptionsActivity(Long activityId, 
            Boolean defineLater, 
            java.util.Date createDateTime, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            Set activities) {
      super(activityId, 
              defineLater, 
              createDateTime, 
              learningLibrary, 
              parentActivity, 
              learningDesign, 
              grouping, 
              activityTypeId, 
              transitionTo,
              transitionFrom,
              activities);
      super.activityStrategy = new OptionsActivityStrategy(this);
    }
    public Activity createCopy(){
    	OptionsActivity newOptionsActivity = new OptionsActivity();
    	
    	/** OptionsActivity Specific Attributes */
    	newOptionsActivity.setMaxNumberOfOptions(this.getMaxNumberOfOptions());
    	newOptionsActivity.setMinNumberOfOptions(this.getMinNumberOfOptions());
    	newOptionsActivity.setOptionsInstructions(this.getOptionsInstructions());
    	
    	/** Generic Activity Attributes */
    	newOptionsActivity.setActivityUIID(this.getActivityUIID());
    	newOptionsActivity.setDescription(this.getDescription());
    	newOptionsActivity.setTitle(this.getTitle());
    	newOptionsActivity.setHelpText(this.getHelpText());
    	newOptionsActivity.setXcoord(this.getXcoord());
    	newOptionsActivity.setYcoord(this.getYcoord());
    	newOptionsActivity.setActivityTypeId(this.getActivityTypeId());
    	
    	newOptionsActivity.setGroupingSupportType(this.getGroupingSupportType());
    	newOptionsActivity.setApplyGrouping(this.getApplyGrouping());
    	newOptionsActivity.setActivityCategoryID(this.getActivityCategoryID());    	
    	
    	newOptionsActivity.setGrouping(this.getGrouping());
    	newOptionsActivity.setGroupingUIID(this.getGroupingUIID());
    	newOptionsActivity.setDefineLater(this.getDefineLater());
    	newOptionsActivity.setLearningLibrary(this.getLearningLibrary());
    	newOptionsActivity.setCreateDateTime(new Date());
    	newOptionsActivity.setRunOffline(this.getRunOffline());
    	newOptionsActivity.setLibraryActivityUiImage(this.getLibraryActivityUiImage());
    	newOptionsActivity.setLibraryActivity(this.getLibraryActivity());
    	newOptionsActivity.setLanguageFile(this.getLanguageFile());

    	return newOptionsActivity;
    }

    /** 
     * @hibernate.property column="max_number_of_options" length="5"
     */
    public Integer getMaxNumberOfOptions() {
        return this.maxNumberOfOptions;
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

    public void setMinNumberOfOptions(Integer minNumberOfOptions) {
        this.minNumberOfOptions = minNumberOfOptions;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
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
    public boolean isNull()
    {
        return false;
    }
}
