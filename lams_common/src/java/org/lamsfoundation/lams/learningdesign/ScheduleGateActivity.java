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
    /**
     * Makes a copy of the ScheduleGateActivity for authoring, preview and monitoring enviornment 
     * 
     * @param originalActivity The activity that needs to be deep-copied
     * @return ScheduleGateActivity Returns a deep-copy of the originalActivity
     */
    public static ScheduleGateActivity createCopy(ScheduleGateActivity originalActivity){
    	ScheduleGateActivity newScheduleGateActivity = new ScheduleGateActivity();
    	
    	newScheduleGateActivity.setGateActivityLevelId(originalActivity.getGateActivityLevelId());
    	newScheduleGateActivity.setGateOpen(new Boolean(false));
    	
    	newScheduleGateActivity.setGateEndTimeOffset(originalActivity.getGateEndTimeOffset());
    	newScheduleGateActivity.setGateStartTimeOffset(originalActivity.getGateStartTimeOffset());
    	newScheduleGateActivity.setGateEndDateTime(originalActivity.getGateEndDateTime());
		newScheduleGateActivity.setGateStartDateTime(originalActivity.getGateStartDateTime());
		
    	newScheduleGateActivity.setActivityUIID(originalActivity.getActivityUIID());
    	newScheduleGateActivity.setDescription(originalActivity.getDescription());
    	newScheduleGateActivity.setTitle(originalActivity.getTitle());
    	newScheduleGateActivity.setHelpText(originalActivity.getHelpText());
    	newScheduleGateActivity.setXcoord(originalActivity.getXcoord());
    	newScheduleGateActivity.setYcoord(originalActivity.getYcoord());
    	newScheduleGateActivity.setActivityTypeId(originalActivity.getActivityTypeId());
    	newScheduleGateActivity.setLearningLibrary(originalActivity.getLearningLibrary());    	
    	newScheduleGateActivity.setDefineLater(originalActivity.getDefineLater());
    	newScheduleGateActivity.setCreateDateTime(new Date());
    	newScheduleGateActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());    	
    	newScheduleGateActivity.setLibraryActivity(originalActivity.getLibraryActivity());
    	newScheduleGateActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());
    	
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
    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    public boolean isNull()
    {
        return false;
    }

}
