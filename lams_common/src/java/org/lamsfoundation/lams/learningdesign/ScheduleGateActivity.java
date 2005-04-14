package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.exception.ActivityBehaviorException;
import org.lamsfoundation.lams.learningdesign.strategy.ScheduleGateActivityStrategy;


/**
 * <p>The hibernate object that wraps the information to schedule a gate in the
 * sequence engine. The schedule gate is defined either by offset to the
 * lesson start time or by the absolute time set by the teacher.<p> 
 * 
 * <p>Interms of gate level, schedule gate only cares about class level at
 * the moment. we leaves the complexity of apply schdule gate to group
 * and individual level to the future release.</p>
 * 
 * @author Chris Perfect
 * @author Jacky Fang
 * 
 * @hibernate.class 
 */
public class ScheduleGateActivity extends GateActivity implements Serializable {

    /**
     * The relative gate open time from the lesson start time. For example, 
     * if the lesson starts at 3:00pm and offset is 1, the gate will be opened
     * at 4:00pm.
     */
    private Long gateStartTimeOffset;

    /**
     * <p>The relative time that gate will be closed from the lesson start time.
     * For example, if the lesson starts at 3:00pm and offset is 2, the gate
     * will be closed at 5:00pm.</p> 
     * Note it must be larger than <code>gateStartTimeOffset</code>.
     */
    private Long gateEndTimeOffset;
    
    /**
     * The absolute start time of the gate activity. If this is set, we are
     * expecting <code>gateStartTimeOffset</code> is set to null.
     */
    private Date gateStartDateTime;

    /**
     * The absolute end time of the gate activity. If this is set, we are 
     * expecting <code>gateEndTimeOffset</code> is set to null.
     */
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
            Long gateStartTimeOffset, 
            Long gateEndTimeOffset,
            Set waitingLearners) {
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
                gateActivityLevelId,
                waitingLearners);
        //validate pre-condition.
        if(gateStartTimeOffset.intValue()>gateEndTimeOffset.intValue())
            throw new IllegalStateException("End time offset must be larger" +
            		" than start time offset");
        
        this.gateStartTimeOffset = gateStartTimeOffset;
        this.gateEndTimeOffset = gateEndTimeOffset;
        this.simpleActivityStrategy = new ScheduleGateActivityStrategy();
    }

    /** default constructor */
    public ScheduleGateActivity() {
        this.simpleActivityStrategy = new ScheduleGateActivityStrategy();
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
            Long gateStartTimeOffset, 
            Long gateEndTimeOffset,
            Set waitingLearners) {
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
                gateActivityLevelId,
                waitingLearners);
      if(gateStartTimeOffset.intValue()>gateEndTimeOffset.intValue())
          throw new IllegalStateException("End time offset must be larger" +
          		" than start time offset");
      this.gateStartTimeOffset = gateStartTimeOffset;
      this.gateEndTimeOffset = gateEndTimeOffset;
      this.simpleActivityStrategy = new ScheduleGateActivityStrategy();
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
    	
    	newScheduleGateActivity.setGroupingSupportType(originalActivity.getGroupingSupportType());
    	newScheduleGateActivity.setApplyGrouping(originalActivity.getApplyGrouping());
    	newScheduleGateActivity.setActivityCategoryID(originalActivity.getActivityCategoryID());
    	
    	newScheduleGateActivity.setGrouping(originalActivity.getGrouping());
    	newScheduleGateActivity.setGroupingUIID(originalActivity.getGroupingUIID());
    	newScheduleGateActivity.setLearningLibrary(originalActivity.getLearningLibrary());    	
    	newScheduleGateActivity.setDefineLater(originalActivity.getDefineLater());
    	newScheduleGateActivity.setCreateDateTime(new Date());
    	newScheduleGateActivity.setRunOffline(originalActivity.getRunOffline());
    	
    	newScheduleGateActivity.setOfflineInstructions(originalActivity.getOfflineInstructions());
    	newScheduleGateActivity.setOnlineInstructions(originalActivity.getOnlineInstructions());
    	
    	newScheduleGateActivity.setLibraryActivity(originalActivity.getLibraryActivity());
    	newScheduleGateActivity.setLibraryActivityUiImage(originalActivity.getLibraryActivityUiImage());
    	
    	return newScheduleGateActivity;
    }

    public Date getGateEndDateTime() 
    {
		return gateEndDateTime;
	}
    
	public void setGateEndDateTime(Date gateEndDateTime) 
	{
		this.gateEndDateTime = gateEndDateTime;
	}
	
	public Date getGateStartDateTime() 
	{
		return gateStartDateTime;
	}
	
	public void setGateStartDateTime(Date gateStartDateTime) 
	{
		this.gateStartDateTime = gateStartDateTime;
	}
    /** 
     * @hibernate.property column="gate_start_date_time" length="20"
     */
    public Long getGateStartTimeOffset() 
    {
        return this.gateStartTimeOffset;
    }

    public void setGateStartTimeOffset(Long gateStartTimeOffset) 
    {
        this.gateStartTimeOffset = gateStartTimeOffset;
    }

    /** 
     * @hibernate.property column="gate_end_date_time" length="20" 
     */
    public Long getGateEndTimeOffset() 
    {
        return this.gateEndTimeOffset;
    }

    public void setGateEndTimeOffset(Long gateEndTimeOffset) 
    {
        this.gateEndTimeOffset = gateEndTimeOffset;
    }

    /**
     * <p>Returns the real gate open time according to the settings done by the 
     * author.</p>
     * <p>If the gate is scheduled against time offset, the real gate open 
     * time will be the current system time plus the time offset. Otherwise,
     * the real gate open time will be the same as start time. </p>
     * <b>Note:</b> the time will also be translated against server timezone.
     *  
     * @return the date time that the gate will be opened.
     */
    public Date getRealGateOpenTime()
    {
               
        Calendar openTime = new GregorianCalendar(TimeZone.getDefault());
        long offset  = openTime.get(Calendar.ZONE_OFFSET)+openTime.get(Calendar.DST_OFFSET);
        //compute the real opening time based on the  
        if(isScheduledByTimeOffset())
            openTime.add(Calendar.MINUTE,getGateStartTimeOffset().intValue());
        else if(isScheduledByDateTime())
            openTime.setTime(new Date(getGateStartDateTime().getTime()+offset));
        else
            throw new ActivityBehaviorException("No way of scheduling has " +
            		"been setup - this usually should be done at authoring " +
            		"interface. Fail to calculate gate open time.");
        
        return openTime.getTime();
    }
    
    /**
     * <p>Returns the real gate close time according to the settings done by the 
     * author.</p>
     * <p>If the gate is scheduled against time offset, the real gate close 
     * time will be the current system time plus the time offset. Otherwise,
     * the real gate open time will be the same as close time. </p>
     * <b>Note:</b> the time will also be translated against proper timezone.
     *  
     * @return the date time that the gate will be closed.
     */
    public Date getRealGateCloseTime()
    {
        Calendar closeTime = new GregorianCalendar(TimeZone.getDefault());
        long offset  = closeTime.get(Calendar.ZONE_OFFSET)+closeTime.get(Calendar.DST_OFFSET);

        //compute the real opening time based on the  
        if(isScheduledByTimeOffset())
            closeTime.add(Calendar.MINUTE,getGateEndTimeOffset().intValue());
        else if(isScheduledByDateTime())
            closeTime.setTime(new Date(getGateEndDateTime().getTime()+offset));
        else
            throw new ActivityBehaviorException("No way of scheduling has " +
            		"been setup - this usually should be done at authoring " +
            		"interface. Fail to calculate gate close time.");
        
        return closeTime.getTime();
    }
    
    public String toString() 
    {
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

    /**
     * Helper method that determines the way of sheduling gate.
     * @return is the gate scheduled by time offset
     */
    private boolean isScheduledByTimeOffset()
    {
         return getGateStartTimeOffset()!=null&&getGateEndTimeOffset()!=null;
    }
    
    /**
     * Helper method that determines the way of sheduling gate.
     * @return is the gate scheduled by the exact date time.
     */
    private boolean isScheduledByDateTime()
    {
        return getGateStartDateTime()!=null&&getGateEndDateTime()!=null;
    }
}
