package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class 
*/
public class ScheduleGateActivity extends GateActivity implements Serializable {

    /** nullable persistent field */
    private Date gateStartDateTime;

    /** nullable persistent field */
    private Date gateEndDateTime;

    /** full constructor */
    public ScheduleGateActivity(Long activityId,
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
            Integer gateActivityLevelId,
            Date gateStartDateTime, 
            Date gateEndDateTime) {
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
				transitionFrom,
                gateActivityLevelId);
        this.gateStartDateTime = gateStartDateTime;
        this.gateEndDateTime = gateEndDateTime;
    }

    /** default constructor */
    public ScheduleGateActivity() {
    }

    /** minimal constructor */
    public ScheduleGateActivity(Long activityId, 
            Boolean defineLater,
            java.util.Date createDateTime,
            org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
            org.lamsfoundation.lams.learningdesign.Activity parentActivity,
            org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
            org.lamsfoundation.lams.learningdesign.Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Integer gateActivityLevelId,
            Date gateStartDateTime, 
            Date gateEndDateTime) {
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
                gateActivityLevelId);
      this.gateStartDateTime = gateStartDateTime;
      this.gateEndDateTime = gateEndDateTime;
    }

    /** 
     * @hibernate.property column="gate_start_date_time" length="19"
     */
    public Date getGateStartDateTime() {
        return this.gateStartDateTime;
    }

    public void setGateStartDateTime(Date gateStartDateTime) {
        this.gateStartDateTime = gateStartDateTime;
    }

    /** 
     * @hibernate.property column="gate_end_date_time" length="19" 
     */
    public Date getGateEndDateTime() {
        return this.gateEndDateTime;
    }

    public void setGateEndDateTime(Date gateEndDateTime) {
        this.gateEndDateTime = gateEndDateTime;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

}
