package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.SimpleActivityStrategy;


/**
 * @hibernate.class
 */
public abstract class SimpleActivity extends Activity implements Serializable
{
    protected SimpleActivityStrategy simpleActivityStrategy;
    
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
            LearningLibrary learningLibrary, 
            Activity parentActivity,
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign, 
            Grouping grouping, 
            Integer activityTypeId, Transition transitionTo,
			Transition transitionFrom){
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
				transitionFrom);
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
            Transition transitionTo,
			Transition transitionFrom){
        super(activityId, 
                defineLater, 
                createDateTime, 
                learningLibrary, 
                parentActivity, 
                learningDesign, 
                grouping, 
                activityTypeId, 
                transitionTo,
				transitionFrom);
    }    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("activityId", getActivityId())
        .toString();
    }

    
	public Integer[] getContributionType(){
		return simpleActivityStrategy.getContributionType(this);
	}
}
