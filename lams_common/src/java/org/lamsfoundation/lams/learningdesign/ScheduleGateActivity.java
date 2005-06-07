/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
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
     * 
     * Note it must be larger than <code>gateStartTimeOffset</code>.
     */
    private Long gateEndTimeOffset;
    
    /**
     * </p>The absolute start time of the gate activity. If this is set, we are
     * expecting <code>gateStartTimeOffset</code> is set to null.</p>
     * 
     * <p>All time value that used for persistent should be UTC time</p>
     */
    private Date gateStartDateTime;

    /**
     * <p>The absolute end time of the gate activity. If this is set, we are 
     * expecting <code>gateEndTimeOffset</code> is set to null.</p>
     * 
     * <p>All time value that used for persistent should be UTC time</p>
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
     * <p>Returns the gate open time for a particular lesson according to the s
     * ettings done by the author.</p>
     * 
     * <p>If the gate is scheduled against time offset and the scheduler has 
     * never run this gate before, the lesson gate open time will be the lesson
     *  start time plus the time offset. Otherwise, the lesson gate open time 
     *  will be the same as start time. </p>
     * 
     * <b>Note:</b> the time will also be translated against server timezone.
     * 
     * @param lessonStartTime the start time of the lesson. this should be
     * 						  the server local time. the UTC time is only used
     * 						  for persistent.
     *  
     * @return the server local date time that the gate will be opened.
     */
    public Date getLessonGateOpenTime(Date lessonStartTime)
    {
        Calendar openTime = new GregorianCalendar(TimeZone.getDefault());
        openTime.setTime(lessonStartTime);
        //compute the real opening time based on the lesson start time. 
        if(isScheduledByTimeOffset())
        {
            openTime.add(Calendar.MINUTE,getGateStartTimeOffset().intValue());
            this.setGateStartDateTime(openTime.getTime());
        }

        else if(isScheduledByDateTime())
            openTime.setTime( getGateStartDateTime());
        else
            throw new ActivityBehaviorException("No way of scheduling has " +
            		"been setup - this usually should be done at authoring " +
            		"interface. Fail to calculate gate open time for lesson.");
        
        return openTime.getTime();
    }
    
    /**
     * <p>Returns the real gate close time for a particular lesson according to 
     * the settings done by the author.</p>
     * 
     * <p>If the gate is scheduled against time offset, the lesson gate close 
     * time will be the lesson start time plus the time offset. Otherwise,
     * the lesson gate open time will be the same as close time. </p>
     * 
     * <b>Note:</b> the time will also be translated against proper timezone.
     * 
     * @param lessonStartTime 
     *  
     * @return the date time that the gate will be closed.
     */
    public Date getLessonGateCloseTime(Date lessonStartTime)
    {
        Calendar closeTime = new GregorianCalendar(TimeZone.getDefault());
        closeTime.setTime(lessonStartTime);
        //compute the real opening time based on the  
        if(isScheduledByTimeOffset())
        {
            closeTime.add(Calendar.MINUTE,getGateEndTimeOffset().intValue());
            this.setGateEndDateTime(closeTime.getTime());
        }

        else if(isScheduledByDateTime())
            closeTime.setTime(getGateEndDateTime());
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
