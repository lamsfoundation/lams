/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
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
import java.util.Vector;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.strategy.ScheduleGateActivityStrategy;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.util.MessageService;

/**
 * <p>
 * The hibernate object that wraps the information to schedule a gate in the sequence engine. The schedule gate is
 * defined either by offset to the lesson start time or by the absolute time set by the teacher.
 * <p>
 *
 * <p>
 * Interms of gate level, schedule gate only cares about class level at the moment. we leaves the complexity of apply
 * schdule gate to group and individual level to the future release.
 * </p>
 *
 * @author Chris Perfect
 * @author Jacky Fang
 *
 *
 */
public class ScheduleGateActivity extends GateActivity implements Serializable {

    /**
     * The relative gate open time from the lesson start time. For example, if the lesson starts at 3:00pm and offset is
     * 1, the gate will be opened at 4:00pm.
     */
    private Long gateStartTimeOffset;

    /**
     * <p>
     * The relative time that gate will be closed from the lesson start time. For example, if the lesson starts at
     * 3:00pm and offset is 2, the gate will be closed at 5:00pm.
     * </p>
     *
     * Note it must be larger than <code>gateStartTimeOffset</code>.
     */
    private Long gateEndTimeOffset;

    private Boolean gateActivityCompletionBased;

    /** full constructor */
    @SuppressWarnings("rawtypes")
    public ScheduleGateActivity(Long activityId, Integer id, String description, String title, Integer xcoord,
	    Integer ycoord, Integer orderId, java.util.Date createDateTime, LearningLibrary learningLibrary,
	    Activity parentActivity, Activity libraryActivity, Integer parentUIID, LearningDesign learningDesign,
	    Grouping grouping, Integer activityTypeId, Transition transitionTo, Transition transitionFrom,
	    String languageFile, Boolean stopAfterActivity, Set inputActivities, Integer gateActivityLevelId,
	    Long gateStartTimeOffset, Long gateEndTimeOffset, SystemTool sysTool, Set branchActivityEntries) {
	super(activityId, id, description, title, xcoord, ycoord, orderId, createDateTime, learningLibrary,
		parentActivity, libraryActivity, parentUIID, learningDesign, grouping, activityTypeId, transitionTo,
		transitionFrom, languageFile, stopAfterActivity, inputActivities, gateActivityLevelId, sysTool,
		branchActivityEntries);
	// validate pre-condition.
	if ((gateStartTimeOffset != null) && (gateEndTimeOffset != null)
		&& (gateStartTimeOffset.intValue() > gateEndTimeOffset.intValue())) {
	    throw new IllegalStateException("End time offset must be larger" + " than start time offset");
	}

	this.gateStartTimeOffset = gateStartTimeOffset;
	this.gateEndTimeOffset = gateEndTimeOffset;
	this.simpleActivityStrategy = new ScheduleGateActivityStrategy(this);
    }

    /** default constructor */
    public ScheduleGateActivity() {
	this.simpleActivityStrategy = new ScheduleGateActivityStrategy(this);
    }

    /** minimal constructor */
    public ScheduleGateActivity(Long activityId, java.util.Date createDateTime,
	    org.lamsfoundation.lams.learningdesign.LearningLibrary learningLibrary,
	    org.lamsfoundation.lams.learningdesign.Activity parentActivity,
	    org.lamsfoundation.lams.learningdesign.LearningDesign learningDesign,
	    org.lamsfoundation.lams.learningdesign.Grouping grouping, Integer activityTypeId, Transition transitionTo,
	    Transition transitionFrom, Integer gateActivityLevelId, Long gateStartTimeOffset, Long gateEndTimeOffset) {
	super(activityId, createDateTime, learningLibrary, parentActivity, learningDesign, grouping, activityTypeId,
		transitionTo, transitionFrom, gateActivityLevelId);
	if ((gateStartTimeOffset != null) && (gateEndTimeOffset != null)
		&& (gateStartTimeOffset.intValue() > gateEndTimeOffset.intValue())) {
	    throw new IllegalStateException("End time offset must be larger" + " than start time offset");
	}

	this.gateStartTimeOffset = gateStartTimeOffset;
	this.gateEndTimeOffset = gateEndTimeOffset;
	this.simpleActivityStrategy = new ScheduleGateActivityStrategy(this);
    }

    /**
     * Makes a copy of the ScheduleGateActivity for authoring, preview and monitoring enviornment
     *
     * @return ScheduleGateActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {
	ScheduleGateActivity newScheduleGateActivity = new ScheduleGateActivity();
	copyToNewActivity(newScheduleGateActivity, uiidOffset);
	newScheduleGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
	newScheduleGateActivity.setGateOpen(new Boolean(false));

	newScheduleGateActivity.setGateEndTimeOffset(this.getGateEndTimeOffset());
	newScheduleGateActivity.setGateStartTimeOffset(this.getGateStartTimeOffset());
	newScheduleGateActivity.setGateActivityCompletionBased(this.getGateActivityCompletionBased());
	return newScheduleGateActivity;
    }

    /**
     *
     */
    public Long getGateStartTimeOffset() {
	return this.gateStartTimeOffset;
    }

    public void setGateStartTimeOffset(Long gateStartTimeOffset) {
	this.gateStartTimeOffset = gateStartTimeOffset;
    }

    /**
     *
     */
    public Long getGateEndTimeOffset() {
	return this.gateEndTimeOffset;
    }

    public void setGateEndTimeOffset(Long gateEndTimeOffset) {
	this.gateEndTimeOffset = gateEndTimeOffset;
    }

    public Boolean getGateActivityCompletionBased() {
	return gateActivityCompletionBased;
    }

    public void setGateActivityCompletionBased(Boolean gateActivityCompletionBased) {
	this.gateActivityCompletionBased = gateActivityCompletionBased;
    }

    /**
     * <p>
     * Returns the gate open time for a particular lesson according to the s ettings done by the author.
     * </p>
     *
     * <p>
     * If the gate is scheduled against time offset and the scheduler has never run this gate before, the lesson gate
     * open time will be the lesson start time plus the time offset. Otherwise, the lesson gate open time will be the
     * same as start time.
     * </p>
     *
     * <b>Note:</b> the time will also be translated against server timezone.
     *
     * @param referenceTime
     *            the start time of the lesson. this should be the server local time. the UTC time is only used for
     *            persistent.
     *
     * @return the server local date time that the gate will be opened.
     */
    public Date getGateOpenTime(Date referenceTime) {
	Calendar openTime = new GregorianCalendar(TimeZone.getDefault());
	openTime.setTime(referenceTime);
	// compute the real opening time based on the lesson start time.
	if (isScheduledByStartTimeOffset()) {
	    openTime.add(Calendar.MINUTE, getGateStartTimeOffset().intValue());
	}

	return openTime.getTime();
    }

    /**
     * <p>
     * Returns the real gate close time for a particular lesson according to the settings done by the author.
     * </p>
     *
     * <p>
     * If the gate is scheduled against time offset, the lesson gate close time will be the lesson start time plus the
     * time offset. Otherwise, the lesson gate open time will be the same as close time.
     * </p>
     *
     * <b>Note:</b> the time will also be translated against proper timezone.
     *
     * @param referenceTime
     *
     * @return the date time that the gate will be closed.
     */
    public Date getGateCloseTime(Date referenceTime) {
	Calendar closeTime = new GregorianCalendar(TimeZone.getDefault());
	closeTime.setTime(referenceTime);
	// compute the real opening time based on the
	if (isScheduledByEndTimeOffset()) {
	    closeTime.add(Calendar.MINUTE, getGateEndTimeOffset().intValue());
	}

	return closeTime.getTime();
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    /**
     * Validate schedule gate activity (offset conditions)
     *
     * @return error message key
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Vector validateActivity(MessageService messageService) {
	Vector listOfValidationErrors = new Vector();
	if (isScheduledByTimeOffset()) {
	    if (getGateStartTimeOffset().equals(getGateEndTimeOffset())) {
		listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.SCHEDULE_GATE_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.SCHEDULE_GATE_ERROR_TYPE1_KEY),
			this.getActivityUIID()));
	    } else if (getGateStartTimeOffset().compareTo(getGateEndTimeOffset()) > 0) {
		listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.SCHEDULE_GATE_ERROR_CODE,
			messageService.getMessage(ValidationErrorDTO.SCHEDULE_GATE_ERROR_TYPE2_KEY),
			this.getActivityUIID()));
	    }

	}
	return listOfValidationErrors;
    }

    /**
     * Helper method that determines the way of sheduling gate.
     *
     * @return is the gate scheduled by time offset
     */
    private boolean isScheduledByTimeOffset() {
	return (getGateStartTimeOffset() != null) && (getGateEndTimeOffset() != null);
    }

    /**
     * Helper method that determines way of scheduling a gate.
     *
     * @return is the gate scheduled by start time offset
     */
    private boolean isScheduledByStartTimeOffset() {
	return getGateStartTimeOffset() != null;
    }

    /**
     * Helper method that determines way of scheduling a gate.
     *
     * @return is the gate scheduled by start time offset
     */
    private boolean isScheduledByEndTimeOffset() {
	return getGateEndTimeOffset() != null;
    }
}