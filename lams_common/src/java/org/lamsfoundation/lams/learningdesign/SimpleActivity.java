package org.lamsfoundation.lams.learningdesign;

import org.lamsfoundation.lams.tool.Tool;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @hibernate.class
 */
public abstract class SimpleActivity extends Activity implements Serializable
{
    
    /** full constructor */
    public SimpleActivity(Long activityId, 
            Integer id, 
            String description, 
            String title, 
            Integer xcoord, 
            Integer ycoord, 
            Integer orderId, 
            Boolean defineLater, 
            Date createDateTime, 
            String offlineInstructions, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId, Set transitionsByToActivityId, 
            Set transitionsByFromActivityId)
    {
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
                transitionsByFromActivityId);
    }
    
    /** default constructor */
    public SimpleActivity()
    {
    }
    
    /** minimal constructor */
    public SimpleActivity(Long activityId, 
            Boolean defineLater, 
            Date createDateTime, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId, 
            Set transitionsByToActivityId, 
            Set transitionsByFromActivityId)
    {
        super(activityId, 
                defineLater, 
                createDateTime, 
                learningLibrary, 
                parentActivity, 
                learningDesign, 
                grouping, 
                activityTypeId, 
                transitionsByToActivityId, 
                transitionsByFromActivityId);
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("activityId", getActivityId())
        .toString();
    }
    
}
