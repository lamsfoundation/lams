package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class 
*/
public class ScheduleGateActivity extends GateActivity implements Serializable {

    /** nullable persistent field */
    private Date gateStartTimeOffset;

    /** nullable persistent field */
    private Date gateEndTimeOffset;
    
    /** nullable persistent field */
    private Date gateStartDateTime;

    /** nullable persistent field */
    private Date gateEndDateTime;

	public Date getGateEndDateTime() {
		return gateEndDateTime;
	}
	public void setGateEndDateTime(Date gateEndDateTime) {
		this.gateEndDateTime = gateEndDateTime;
	}
	public Date getGateStartDateTime() {
		return gateStartDateTime;
	}
	public void setGateStartDateTime(Date gateStartDateTime) {
		this.gateStartDateTime = gateStartDateTime;
	}
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
            LearningLibrary learningLibrary,
            Activity parentActivity,
            Activity libraryActivity,
			Integer parentUIID,
            LearningDesign learningDesign,
            Grouping grouping,
            Integer activityTypeId,
            Transition transitionTo,
            Transition transitionFrom,
            Integer gateActivityLevelId,
            Date gateStartTimeOffset, 
            Date gateEndTimeOffset) {
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
                gateActivityLevelId);
        this.gateStartTimeOffset = gateStartTimeOffset;
        this.gateEndTimeOffset = gateEndTimeOffset;
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
            Date gateStartTimeOffset, 
            Date gateEndTimeOffset) {
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
      this.gateStartTimeOffset = gateStartTimeOffset;
      this.gateEndTimeOffset = gateEndTimeOffset;
    }
    public static ScheduleGateActivity createCopy(ScheduleGateActivity originalActivity){
    	ScheduleGateActivity newScheduleGateActivity = new ScheduleGateActivity();
    	
    	newScheduleGateActivity.setGateEndTimeOffset(originalActivity.getGateEndTimeOffset());
    	newScheduleGateActivity.setGateStartTimeOffset(originalActivity.getGateStartTimeOffset());
    	
    	newScheduleGateActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newScheduleGateActivity.setDescription(originalActivity.getDescription());
    	newScheduleGateActivity.setTitle(originalActivity.getTitle());
    	newScheduleGateActivity.setXcoord(originalActivity.getXcoord());
    	newScheduleGateActivity.setYcoord(originalActivity.getYcoord());
    	newScheduleGateActivity.setDefineLater(originalActivity.getDefineLater());
    	newScheduleGateActivity.setCreateDateTime(new Date());
    	newScheduleGateActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newScheduleGateActivity.setLearningLibrary(originalActivity.getLearningLibrary());
    	newScheduleGateActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	newScheduleGateActivity.setGateActivityLevelId(originalActivity.getGateActivityLevelId());
    	
    	
    	/**
    	 * TODO calculate how these two values would be set for COPY 
    	 * */
    	//newScheduleGateActivity.setTransitionTo();
    	//newScheduleGateActivity.setTransitionFrom();
    	
    	return newScheduleGateActivity;
    }

    /** 
     * @hibernate.property column="gate_start_date_time" length="19"
     */
    public Date getGateStartTimeOffset() {
        return this.gateStartTimeOffset;
    }

    public void setGateStartTimeOffset(Date gateStartTimeOffset) {
        this.gateStartTimeOffset = gateStartTimeOffset;
    }

    /** 
     * @hibernate.property column="gate_end_date_time" length="19" 
     */
    public Date getGateEndTimeOffset() {
        return this.gateEndTimeOffset;
    }

    public void setGateEndTimeOffset(Date gateEndTimeOffset) {
        this.gateEndTimeOffset = gateEndTimeOffset;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("activityId", getActivityId())
            .toString();
    }

}
