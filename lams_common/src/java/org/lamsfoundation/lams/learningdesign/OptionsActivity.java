package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class 
*/
public class OptionsActivity extends ComplexActivity implements Serializable {

    /** nullable persistent field */
    private Integer maxNumberOfOptions;

    /** nullable persistent field */
    private Integer minNumberOfOptions;

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
            LearningDesign learningDesign, 
            Grouping grouping, 
            Integer activityTypeId,  
            java.util.Set transitionsByToActivityId, 
            java.util.Set transitionsByFromActivityId,
            java.util.Set activities, 
            Integer maxNumberOfOptions, 
            Integer minNumberOfOptions) {
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
                learningDesign, 
                grouping, 
                activityTypeId,  
                transitionsByToActivityId,
                transitionsByFromActivityId, 
                activities);
        this.maxNumberOfOptions = maxNumberOfOptions;
        this.minNumberOfOptions = minNumberOfOptions;
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
            java.util.Set transitionsByToActivityId, 
            java.util.Set transitionsByFromActivityId, 
            java.util.Set activities) {
      super(activityId, 
              defineLater, 
              createDateTime, 
              learningLibrary, 
              parentActivity, 
              learningDesign, 
              grouping, 
              activityTypeId, 
              transitionsByToActivityId, 
              transitionsByFromActivityId, 
              activities);
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

}
