package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @hibernate.class
 */
public abstract class GateActivity extends SimpleActivity implements Serializable
{
    
    public static final int LEARNER_GATE_LEVEL = 1;
    
    public static final int GROUP_GATE_LEVEL = 2;
    
    public static final int CLASS_GATE_LEVEL = 3;
    
    /** persistent field */
    private Integer gateActivityLevelId;
    
    /** full constructor */
    public GateActivity(Long activityId,
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
            Integer gateActivityLevelId)
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
        this.gateActivityLevelId = gateActivityLevelId;
    }
    
    /** default constructor */
    public GateActivity()
    {
    }
    
    /** minimal constructor */
    public GateActivity(Long activityId,
            Boolean defineLater,
            java.util.Date createDateTime,
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
            org.lamsfoundation.lams.learningdesign.Activity parentActivity,
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
            org.lamsfoundation.lams.learningdesign.Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Integer gateActivityLevelId)
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
        this.gateActivityLevelId = gateActivityLevelId;
    }
    
    /**
     * @hibernate.many-to-one not-null="true"
     *         							        @hibernate.column name="gate_activity_level_id"
     *
     */
    public Integer getGateActivityLevelId()
    {
        return this.gateActivityLevelId;
    }
    
    public void setGateActivityLevelId(Integer gateActivityLevelId)
    {
        this.gateActivityLevelId = gateActivityLevelId;
    }
    
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("activityId", getActivityId())
        .toString();
    }
    
}
