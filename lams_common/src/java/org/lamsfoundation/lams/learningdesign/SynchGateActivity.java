package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class 
*/
public class SynchGateActivity extends GateActivity implements Serializable {

    /** full constructor */
    public SynchGateActivity(Long activityId, 
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
            java.util.Set transitionsByToActivityId, 
            java.util.Set transitionsByFromActivityId,
            Integer gateActivityLevelId) {
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
                gateActivityLevelId);
    }

    /** default constructor */
    public SynchGateActivity() {
    }

    /** minimal constructor */
    public SynchGateActivity(Long activityId, 
            Boolean defineLater,
            java.util.Date createDateTime, 
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary, 
            org.lamsfoundation.lams.learningdesign.Activity parentActivity, 
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign, 
            org.lamsfoundation.lams.learningdesign.Grouping grouping, 
            Integer activityTypeId, 
            java.util.Set transitionsByToActivityId, 
            java.util.Set transitionsByFromActivityId,
            Integer gateActivityLevelId) {
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
              gateActivityLevelId);
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

}
