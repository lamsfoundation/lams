package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.lang.builder.ToStringBuilder;


/** 
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
            String offlineInstructions, 
            LearningLibrary learningLibrary, 
            Activity parentActivity, 
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign, 
            Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            java.util.Set activities, 
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
                offlineInstructions, 
                learningLibrary, 
                parentActivity, 
				libraryActivity,
				parentUIID,
                learningDesign, 
                grouping, 
                activityTypeId,  
                transitionTo,
				transitionFrom,
                activities);
        this.maxNumberOfOptions = maxNumberOfOptions;
        this.minNumberOfOptions = minNumberOfOptions;
        this.optionsInstructions = options_instructions;
    }

    /** default constructor */
    public OptionsActivity() {
    	
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
            java.util.Set activities) {
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
    }
    public static OptionsActivity createCopy(OptionsActivity originalActivity){
    	OptionsActivity newOptionsActivity = new OptionsActivity();
    	
    	newOptionsActivity.setMaxNumberOfOptions(originalActivity.getMaxNumberOfOptions());
    	newOptionsActivity.setMinNumberOfOptions(originalActivity.getMinNumberOfOptions());
    	newOptionsActivity.setOptionsInstructions(originalActivity.getOptionsInstructions());
    	
    	newOptionsActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newOptionsActivity.setDescription(originalActivity.getDescription());
    	newOptionsActivity.setTitle(originalActivity.getTitle());
    	newOptionsActivity.setXcoord(originalActivity.getXcoord());
    	newOptionsActivity.setYcoord(originalActivity.getYcoord());
    	newOptionsActivity.setDefineLater(originalActivity.getDefineLater());
    	newOptionsActivity.setCreateDateTime(new Date());
    	newOptionsActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newOptionsActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newOptionsActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	newOptionsActivity.setActivities(new HashSet());
    	
    	/** TODO */
    	//newOptionsActivity.setTransitionTo();
		//newOptionsActivity.setTransitionFrom();
    	
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
}
