/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.lesson;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.usermanagement.User;

public class LearnerProgressArchive implements Serializable {
    /** Identifier field */
    private Long learnerProgressId;

    /** The User to whom this progress data belongs. */
    private User user;

    /** The Lesson this progress data is for */
    private Lesson lesson;

    private Integer attemptId;

    /** Map of attempted activities with their start date */
    private Map<Activity, Date> attemptedActivities;

    /**
     * Set of completed activities that includes all completed activities before
     * current activity
     */
    private Map<Activity, CompletedActivityProgressArchive> completedActivities;

    /**
     * The current activity always present the activity with transition, which
     * means it won't be leaf node of a complex activity. To understand the
     * activity tree, please read relevant documentation and comment. The
     * current content could be the same as next activity if next activity is
     * not the leaf node. The main purpose of current activity is to restore the
     * progress states if the user exist without finishing the activity.
     */
    private Activity currentActivity;

    /**
     * Indicates is the User has completed this lesson.
     */
    private Byte lessonComplete;

    private Date startDate;
    private Date finishDate;

    private Date archiveDate;

    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------
    /** default constructor */
    public LearnerProgressArchive() {
    }

    public LearnerProgressArchive(User user, Lesson lesson, Integer attemptId, Map<Activity, Date> attemptedActivities,
	    Map<Activity, CompletedActivityProgressArchive> completedActivities, Activity currentActivity,
	    Byte lessonComplete, Date startDate, Date finishDate, Date archiveDate) {
	this.user = user;
	this.lesson = lesson;
	this.attemptId = attemptId;
	this.attemptedActivities = attemptedActivities;
	this.completedActivities = completedActivities;
	this.currentActivity = currentActivity;
	this.lessonComplete = lessonComplete;
	this.startDate = startDate;
	this.finishDate = finishDate;
	this.archiveDate = archiveDate;
    }

    //---------------------------------------------------------------------
    // Getters and Setters
    //---------------------------------------------------------------------
    public Long getLearnerProgressId() {
	return this.learnerProgressId;
    }

    public void setLearnerProgressId(Long learnerProgressId) {
	this.learnerProgressId = learnerProgressId;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Lesson getLesson() {
	return this.lesson;
    }

    public void setLesson(Lesson lesson) {
	this.lesson = lesson;
    }

    public Integer getAttemptId() {
	return attemptId;
    }

    public void setAttemptId(Integer attemptId) {
	this.attemptId = attemptId;
    }

    public Map<Activity, Date> getAttemptedActivities() {
	return this.attemptedActivities;
    }

    public void setAttemptedActivities(Map<Activity, Date> attemptedActivities) {
	this.attemptedActivities = attemptedActivities;
    }

    public Map<Activity, CompletedActivityProgressArchive> getCompletedActivities() {
	return this.completedActivities;
    }

    public void setCompletedActivities(Map<Activity, CompletedActivityProgressArchive> completedActivities) {
	this.completedActivities = completedActivities;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("learnerProgressId", getLearnerProgressId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof LearnerProgressArchive)) {
	    return false;
	}
	LearnerProgressArchive castOther = (LearnerProgressArchive) other;
	return new EqualsBuilder().append(this.getLearnerProgressId(), castOther.getLearnerProgressId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getLearnerProgressId()).toHashCode();
    }

    public Activity getCurrentActivity() {
	return this.currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
	this.currentActivity = currentActivity;
    }

    public byte getProgressState(Activity activity) {
	if (getCompletedActivities().containsKey(activity)) {
	    return LearnerProgress.ACTIVITY_COMPLETED;
	} else if (getAttemptedActivities().containsKey(activity)) {
	    return LearnerProgress.ACTIVITY_ATTEMPTED;
	} else {
	    return LearnerProgress.ACTIVITY_NOT_ATTEMPTED;
	}
    }

    /**
     * Has the user completed the lesson? We don't care how (ie at end of
     * sequence or after a "stop after activity")
     */
    public boolean isComplete() {
	return lessonComplete == LearnerProgress.LESSON_END_OF_DESIGN_COMPLETE
		|| lessonComplete == LearnerProgress.LESSON_IN_DESIGN_COMPLETE;
    }

    /**
     * The "real" value for lessonComplete.
     *
     * @return LESSON_NOT_COMPLETE, LESSON_END_OF_DESIGN_COMPLETE,
     *         LESSON_IN_DESIGN_COMPLETE
     */
    public Byte getLessonComplete() {
	return lessonComplete;
    }

    /**
     * Setter for property lessonComplete.
     *
     * @param lessonComplete
     *            New value of property lessonComplete.
     */
    public void setLessonComplete(Byte lessonComplete) {
	this.lessonComplete = lessonComplete;
    }

    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getArchiveDate() {
	return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
	this.archiveDate = archiveDate;
    }
}