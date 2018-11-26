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
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.usermanagement.User;

@Entity
@Table(name = "lams_learner_progress_archive")
public class LearnerProgressArchive implements Serializable {
    private static final long serialVersionUID = -6114747067804692585L;

    @Id
    @Column(name = "learner_progress_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learnerProgressId;

    /** The User to whom this progress data belongs. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** The Lesson this progress data is for */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "attempt_id")
    private Integer attemptId;

    /** Map of attempted activities with their start date */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lams_progress_attempted_archive", joinColumns = @JoinColumn(name = "learner_progress_id"))
    @MapKeyJoinColumn(name = "activity_id")
    @Column(name = "start_date_time")
    private Map<Activity, Date> attemptedActivities = new HashMap<Activity, Date>();

    /**
     * Set of completed activities that includes all completed activities before
     * current activity
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lams_progress_completed_archive", joinColumns = @JoinColumn(name = "learner_progress_id"))
    @MapKeyJoinColumn(name = "activity_id")
    private Map<Activity, CompletedActivityProgressArchive> completedActivities = new HashMap<Activity, CompletedActivityProgressArchive>();

    /**
     * The current activity always present the activity with transition, which
     * means it won't be leaf node of a complex activity. To understand the
     * activity tree, please read relevant documentation and comment. The
     * current content could be the same as next activity if next activity is
     * not the leaf node. The main purpose of current activity is to restore the
     * progress states if the user exist without finishing the activity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_activity_id")
    private Activity currentActivity;

    /**
     * Indicates is the User has completed this lesson.
     */
    @Column(name = "lesson_completed_flag")
    private Byte lessonComplete;

    @Column(name = "start_date_time")
    private Date startDate;

    @Column(name = "finish_date_time")
    private Date finishDate;

    @Column(name = "archive_date")
    private Date archiveDate;

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

    public Byte getLessonComplete() {
	return lessonComplete;
    }

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