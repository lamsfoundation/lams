package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @hibernate.class
 */
public class GroupingActivity extends SimpleActivity implements Serializable
{
    
    /** persistent field */
    public org.lamsfoundation.lams.learningdesign.Grouping createGrouping;
    
    /** full constructor */
    public GroupingActivity(Long activityId, 
            Integer id, 
            String description, 
            String title, 
            Integer xcoord, 
            Integer ycoord, 
            Integer orderId, 
            Boolean defineLater, 
            java.util.Date createDateTime, 
            String offlineInstructions, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId,  
            Transition transitionTo,
            Transition transitionFrom,
            Grouping createGrouping)
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
                transitionTo,
				transitionFrom);
        this.createGrouping = createGrouping;
    }
    
    /** default constructor */
    public GroupingActivity()
    {
    }
    
    /** minimal constructor */
    public GroupingActivity(Long activityId, 
            Boolean defineLater,
            java.util.Date createDateTime,
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
            org.lamsfoundation.lams.learningdesign.Activity parentActivity,
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
            org.lamsfoundation.lams.learningdesign.Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Grouping createGrouping)
    {
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
        this.createGrouping = createGrouping;
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("activityId", getActivityId())
        .toString();
    }
    
	/**
	 * @return Returns the createGrouping.
	 */
	public Grouping getCreateGrouping() {
		return createGrouping;
	}
	/**
	 * @param createGrouping The createGrouping to set.
	 */
	public void setCreateGrouping(Grouping createGrouping) {
		this.createGrouping = createGrouping;
	}
}
