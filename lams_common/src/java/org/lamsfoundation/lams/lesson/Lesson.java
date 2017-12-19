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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * A Lesson is a learning sequence that is assocated with a number of users for use in learning. A lesson needs a run
 * time copy of learning design to interact with.
 */
public class Lesson implements Serializable {

    private static final long serialVersionUID = 5733920851084229175L;

    // ---------------------------------------------------------------------
    // Class level constants
    // ---------------------------------------------------------------------
    /**
     * The state for newly created lesson. The learning design has been copied. The lesson class may or may not have
     * been configured. It is seen on the staff interface but not on the learning interface.
     */
    public static final Integer CREATED = new Integer(1);
    /** The state for lessons that have been scheduled. */
    public static final Integer NOT_STARTED_STATE = new Integer(2);
    /** The state for started lesson */
    public static final Integer STARTED_STATE = new Integer(3);
    /**
     * The state for lessons that have been suspended by the teacher. The lesson can be seen on the staff interface but
     * not on the learning interface
     */
    public static final Integer SUSPENDED_STATE = new Integer(4);
    /**
     * The state for lessons that have been finished. A finished lesson is shown as inactive on the staff interface, and
     * is shown on the learner interface but the learner is to only see the overall progress and be able to export data
     * - they should not be able to iteract with the tools
     */
    public static final Integer FINISHED_STATE = new Integer(5);
    /**
     * The state for lesssons that are shown as inactive on the staff interface but no longer visible to the learners.
     */
    public static final Integer ARCHIVED_STATE = new Integer(6);
    /** The state for lesssons that are removed and never can be accessed again */
    public static final Integer REMOVED_STATE = new Integer(7);

    // ---------------------------------------------------------------------
    // attributes
    // ---------------------------------------------------------------------
    /** identifier field */
    private Long lessonId;

    /** persistent field */
    private String lessonName;

    /** persistent field */
    private Date createDateTime;

    /** nullable persistent field */
    private Date startDateTime;

    /** nullable persistent field */
    private Date endDateTime;

    /** nullable persistent field */
    private Date scheduleStartDate;

    /** nullable persistent field */
    private Date scheduleEndDate;

    /** nullable persistent field */
    private Integer scheduledNumberDaysToLessonFinish;

    /** persistent field */
    private User user;

    /** persistent field */
    private Integer lessonStateId;

    /** persistent field */
    private Integer previousLessonStateId;

    /** persistent field */
    private LearningDesign learningDesign;

    /** persistent field */
    private LessonClass lessonClass;

    /** persistent field */
    private Organisation organisation;

    /** persistent field */
    private Set learnerProgresses;

    /** persistent field */
    private Set<GradebookUserLesson> gradebookUserLessons;

    /** Persistent field. Defaults to FALSE if not set to anything by a constructor parameter. */
    private Boolean enableLessonIntro;

    /** persistent field */
    private String lessonDescription;

    /** Persistent field. Defaults to FALSE if not set to anything by a constructor parameter. */
    private Boolean displayDesignImage;

    /** Persistent field. Defaults to FALSE - is not included in the constructor anywhere. */
    private Boolean lockedForEdit;

    /** Persistent field. Defaults to FALSE if not set to anything by a constructor parameter. */
    private Boolean learnerPresenceAvailable;

    /** Persistent field. Defaults to FALSE if not set to anything by a constructor parameter. */
    private Boolean learnerImAvailable;

    /** Persistent field. Defaults to FALSE if not set to anything by a constructor parameter. */
    private Boolean liveEditEnabled;

    /** Persistent field. Defaults to FALSE if not set to anything by a constructor parameter. */
    private Boolean enableLessonNotifications;

    /** Persistent field. Defaults to FALSE if not set to anything by a constructor parameter. */
    private Boolean marksReleased;

    /**
     * Should Learner start the lesson from the beginning each time he enters it.
     * Content is not removed, LessonProgress is deleted, not archived.
     */
    private Boolean forceLearnerRestart;

    /**
     * Should Learners be allowed to restart the lesson after finishing it.
     * Content is not removed, LessonProgress is archived and then deleted.
     */
    private Boolean allowLearnerRestart;

    /**
     * Should learners be displayed activity gradebook on lesson complete.
     */
    private Boolean gradebookOnComplete;

    /**
     * For lesson conditional release
     */
    private Set<Lesson> precedingLessons;
    private Set<Lesson> succeedingLessons;

    // ---------------------------------------------------------------------
    // constructors
    // ---------------------------------------------------------------------
    /** default constructor */
    public Lesson() {
    }

    /**
     * Minimum constructor that initialize the lesson data. It doesn't include organization and class information. Chain
     * constructor pattern implementation.
     */
    public Lesson(String name, String description, Date createDateTime, User user, Integer lessonStateId,
	    Integer previousLessonStateId, LearningDesign learningDesign, Set learnerProgresses,
	    Boolean enableLessonIntro, Boolean displayDesignImage, Boolean learnerPresenceAvailable,
	    Boolean learnerImAvailable, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean forceLearnerRestart, Boolean allowLearnerRestart, Boolean gradebookOnComplete,
	    Integer scheduledNumberDaysTolessonFinish) {
	this(null, name, description, createDateTime, null, null, user, lessonStateId, previousLessonStateId,
		enableLessonIntro, displayDesignImage, false, learningDesign, null, null, learnerProgresses,
		learnerPresenceAvailable, learnerImAvailable, liveEditEnabled, enableLessonNotifications,
		forceLearnerRestart, allowLearnerRestart, gradebookOnComplete, scheduledNumberDaysTolessonFinish);
    }

    /** full constructor */
    public Lesson(Long lessonId, String name, String description, Date createDateTime, Date startDateTime,
	    Date endDateTime, User user, Integer lessonStateId, Integer previousLessonStateId,
	    Boolean enableLessonIntro, Boolean displayDesignImage, Boolean lockedForEdit, LearningDesign learningDesign,
	    LessonClass lessonClass, Organisation organisation, Set learnerProgresses, Boolean learnerPresenceAvailable,
	    Boolean learnerImAvailable, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean forceLearnerRestart, Boolean allowLearnerRestart, Boolean gradebookOnComplete,
	    Integer scheduledNumberDaysToLessonFinish) {
	this.lessonId = lessonId;
	this.lessonName = name;
	this.lessonDescription = description;
	this.createDateTime = createDateTime;
	this.startDateTime = startDateTime;
	this.endDateTime = endDateTime;
	this.user = user;
	this.lessonStateId = lessonStateId;
	this.previousLessonStateId = previousLessonStateId;
	this.enableLessonIntro = enableLessonIntro != null ? enableLessonIntro : Boolean.FALSE;
	this.displayDesignImage = displayDesignImage != null ? displayDesignImage : Boolean.FALSE;
	this.learnerPresenceAvailable = learnerPresenceAvailable != null ? learnerPresenceAvailable : Boolean.FALSE;
	this.learnerImAvailable = learnerImAvailable != null ? learnerImAvailable : Boolean.FALSE;
	this.lockedForEdit = false;
	this.learningDesign = learningDesign;
	this.lessonClass = lessonClass;
	this.organisation = organisation;
	this.learnerProgresses = learnerProgresses;
	this.liveEditEnabled = liveEditEnabled;
	this.enableLessonNotifications = enableLessonNotifications;
	this.forceLearnerRestart = forceLearnerRestart;
	this.allowLearnerRestart = allowLearnerRestart;
	this.gradebookOnComplete = gradebookOnComplete;
	this.gradebookUserLessons = new HashSet<GradebookUserLesson>();
	this.marksReleased = false;
	this.scheduledNumberDaysToLessonFinish = scheduledNumberDaysToLessonFinish;
    }

    /**
     * Factory method that create a new lesson with lesson class and organization. It is design to allow user create a
     * lesson first and modify organization and lesson class data later.
     *
     * @param user
     *            the user who want to create a lesson.
     * @param ld
     *            the learning design that this lesson is based on.
     * @return the lesson created.
     */
    public static Lesson createNewLessonWithoutClass(String lessonName, String lessonDescription, User user,
	    LearningDesign ld, Boolean enableLessonIntro, Boolean displayDesignImage, Boolean learnerPresenceAvailable,
	    Boolean learnerImAvailable, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean forceLearnerRestart, Boolean allowLearnerRestart, Boolean gradebookOnComplete,
	    Integer scheduledNumberDaysToLessonFinish) {
	return new Lesson(lessonName, lessonDescription, new Date(System.currentTimeMillis()), user, Lesson.CREATED,
		null, ld, new HashSet(), enableLessonIntro, displayDesignImage, learnerPresenceAvailable,
		learnerImAvailable, liveEditEnabled, enableLessonNotifications, forceLearnerRestart,
		allowLearnerRestart, gradebookOnComplete, scheduledNumberDaysToLessonFinish);
    }

    // ---------------------------------------------------------------------
    // Getters and Setters
    // ---------------------------------------------------------------------
    public Long getLessonId() {
	return this.lessonId;
    }

    public void setLessonId(Long lessonId) {
	this.lessonId = lessonId;
    }

    /**
     * @return Returns the lessonName.
     */
    public String getLessonName() {
	return lessonName;
    }

    /**
     * @param lessonName
     *            The lessonName to set.
     */
    public void setLessonName(String lessonName) {
	this.lessonName = lessonName;
    }

    /**
     * @return Returns the lessonDescription.
     */
    public String getLessonDescription() {
	return lessonDescription;
    }

    /**
     * @param lessonDescription
     *            The lessonDescription to set.
     */
    public void setLessonDescription(String lessonDescription) {
	this.lessonDescription = lessonDescription;
    }

    public Date getCreateDateTime() {
	return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    public Date getStartDateTime() {
	return this.startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
	this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
	return this.endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
	this.endDateTime = endDateTime;
    }

    /**
     * @return Returns the scheduleEndDate.
     */
    public Date getScheduleEndDate() {
	return scheduleEndDate;
    }

    /**
     * @param scheduleEndDate
     *            The scheduleEndDate to set.
     */
    public void setScheduleEndDate(Date scheduleEndDate) {
	this.scheduleEndDate = scheduleEndDate;
    }

    /**
     * @return Returns whether there is an end date for individual user.
     */
    public boolean isScheduledToCloseForIndividuals() {
	return (scheduledNumberDaysToLessonFinish != null);
    }

    /**
     * @return Returns the number of days the lesson will be available to user since he starts it. (It's ON only if the
     *         lesson was scheduled to be finished and individual option was selected)
     */
    public Integer getScheduledNumberDaysToLessonFinish() {
	return scheduledNumberDaysToLessonFinish;
    }

    /**
     * @param scheduledNumberDaysToLessonFinish
     *            the number of days the lesson will be available to user since he starts it. (It's ON only if the
     *            lesson was scheduled to be finished and individual option was selected)
     */
    public void setScheduledNumberDaysToLessonFinish(Integer scheduledNumberDaysToLessonFinish) {
	this.scheduledNumberDaysToLessonFinish = scheduledNumberDaysToLessonFinish;
    }

    /**
     * @return Returns the scheduleStartDate.
     */
    public Date getScheduleStartDate() {
	return scheduleStartDate;
    }

    /**
     * @param scheduleStartDate
     *            The scheduleStartDate to set.
     */
    public void setScheduleStartDate(Date scheduleStartDate) {
	this.scheduleStartDate = scheduleStartDate;
    }

    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Integer getLessonStateId() {
	return this.lessonStateId;
    }

    public void setLessonStateId(Integer lessonStateId) {
	this.lessonStateId = lessonStateId;
    }

    public Integer getPreviousLessonStateId() {
	return this.previousLessonStateId;
    }

    public void setPreviousLessonStateId(Integer previousLessonStateId) {
	this.previousLessonStateId = previousLessonStateId;
    }

    public Boolean isEnableLessonIntro() {
	return enableLessonIntro;
    }

    public void setEnableLessonIntro(Boolean enableLessonIntro) {
	this.enableLessonIntro = enableLessonIntro;
    }

    public Boolean isDisplayDesignImage() {
	return displayDesignImage;
    }

    public void setDisplayDesignImage(Boolean displayDesignImage) {
	this.displayDesignImage = displayDesignImage;
    }

    public Boolean getLearnerPresenceAvailable() {
	return learnerPresenceAvailable;
    }

    public void setLearnerPresenceAvailable(Boolean learnerPresenceAvailable) {
	this.learnerPresenceAvailable = learnerPresenceAvailable;
    }

    public Boolean getLearnerImAvailable() {
	return learnerImAvailable;
    }

    public void setLearnerImAvailable(Boolean learnerImAvailable) {
	this.learnerImAvailable = learnerImAvailable;
    }

    public Boolean getLiveEditEnabled() {
	return liveEditEnabled;
    }

    public void setLiveEditEnabled(Boolean liveEditEnabled) {
	this.liveEditEnabled = liveEditEnabled;
    }

    public Boolean getEnableLessonNotifications() {
	return enableLessonNotifications;
    }

    public void setEnableLessonNotifications(Boolean enableLessonNotifications) {
	this.enableLessonNotifications = enableLessonNotifications;
    }

    public Boolean getLockedForEdit() {
	return lockedForEdit;
    }

    public void setLockedForEdit(Boolean lockedForEdit) {
	this.lockedForEdit = lockedForEdit;
    }

    public LearningDesign getLearningDesign() {
	return this.learningDesign;
    }

    public void setLearningDesign(LearningDesign learningDesign) {
	this.learningDesign = learningDesign;
    }

    public LessonClass getLessonClass() {
	return this.lessonClass;
    }

    public void setLessonClass(LessonClass lessonClass) {
	this.lessonClass = lessonClass;
    }

    public Organisation getOrganisation() {
	return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
	this.organisation = organisation;
    }

    public Set getLearnerProgresses() {
	return this.learnerProgresses;
    }

    public void setLearnerProgresses(Set learnerProgresses) {
	this.learnerProgresses = learnerProgresses;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("lessonId", getLessonId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof Lesson)) {
	    return false;
	}
	Lesson castOther = (Lesson) other;
	return new EqualsBuilder().append(this.getLessonId(), castOther.getLessonId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getLessonId()).toHashCode();
    }

    public Set getAllLearners() {
	return lessonClass == null ? null : lessonClass.getLearners();
    }

    /**
     * Create lesson data transfer object for UI and java monitoring interaction. Includes counts of the learners.
     *
     * @return the monitoring lesson data transfer object.
     */
    public LessonDetailsDTO getLessonDetails() {
	return new LessonDetailsDTO(this);
    }

    /** Is this lesson a preview lesson? ie is it attached to a preview learning design? */
    public boolean isPreviewLesson() {
	Integer copyTypeID = getLearningDesign().getCopyTypeID();
	return ((copyTypeID != null) && (LearningDesign.COPY_TYPE_PREVIEW == copyTypeID.intValue()));
    }

    /**
     * Has this lesson ever been started? Considered started if it is started, finished, archived or removed or if the
     * previousLessonStateId is one of these states (to pick up suspended started)
     */
    public boolean isLessonStarted() {
	return isStarted(lessonStateId) || isStarted(previousLessonStateId);
    }

    private boolean isStarted(Integer stateId) {
	return ((stateId != null) && (stateId.equals(Lesson.STARTED_STATE) || stateId.equals(Lesson.FINISHED_STATE)
		|| stateId.equals(Lesson.ARCHIVED_STATE) || stateId.equals(Lesson.REMOVED_STATE)));
    }

    /**
     * Checks whether learners are allowed to access the lesson based on its state. As they can only access Started or
     * Finished lessons.
     */
    public boolean isLessonAccessibleForLearner() {
	return ((lessonStateId != null)
		&& (lessonStateId.equals(Lesson.STARTED_STATE) || lessonStateId.equals(Lesson.FINISHED_STATE)));
    }

    public Set<GradebookUserLesson> getGradebookUserLessons() {
	return gradebookUserLessons;
    }

    public void setGradebookUserLessons(Set<GradebookUserLesson> gradebookUserLessons) {
	this.gradebookUserLessons = gradebookUserLessons;
    }

    public Boolean getMarksReleased() {
	return marksReleased;
    }

    public void setMarksReleased(Boolean marksReleased) {
	this.marksReleased = marksReleased;
    }

    public Boolean getForceLearnerRestart() {
	return forceLearnerRestart;
    }

    public void setForceLearnerRestart(Boolean forceLearnerRestart) {
	this.forceLearnerRestart = forceLearnerRestart;
    }

    public Boolean getAllowLearnerRestart() {
	return allowLearnerRestart;
    }

    public void setAllowLearnerRestart(Boolean allowLearnerRestart) {
	this.allowLearnerRestart = allowLearnerRestart;
    }

    public Boolean getGradebookOnComplete() {
	return gradebookOnComplete;
    }

    public void setGradebookOnComplete(Boolean gradebookOnComplete) {
	this.gradebookOnComplete = gradebookOnComplete;
    }

    public Set<Lesson> getPrecedingLessons() {
	return precedingLessons;
    }

    public void setPrecedingLessons(Set<Lesson> precedingLessons) {
	this.precedingLessons = precedingLessons;
    }

    public Set<Lesson> getSucceedingLessons() {
	return succeedingLessons;
    }

    public void setSucceedingLessons(Set<Lesson> succeedingLessons) {
	this.succeedingLessons = succeedingLessons;
    }
}