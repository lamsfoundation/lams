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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

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
@NamedNativeQueries({
	@NamedNativeQuery(resultClass = User.class, name = "monitorsByToolSessionId", query = "SELECT DISTINCT user.*"
		+ " FROM lams_lesson AS l JOIN lams_grouping AS ging ON l.class_grouping_id = ging.grouping_id"
		+ " JOIN lams_group AS g ON ging.staff_group_id = g.group_id"
		+ " JOIN lams_user_group AS ug ON g.group_id = ug.group_id"
		+ " JOIN lams_user AS user ON user.user_id = ug.user_id"
		+ " JOIN lams_learning_design AS ld ON ld.copy_type_id = 2"
		+ " AND l.learning_design_id = ld.learning_design_id"
		+ " JOIN lams_tool_session AS s ON s.tool_session_id = :sessionId AND s.lesson_id = l.lesson_id"),
	@NamedNativeQuery(resultClass = Lesson.class, name = "activeLessonsAllOrganisations", query = "SELECT DISTINCT lesson.*"
		+ " FROM lams_lesson AS lesson JOIN lams_learning_design AS ld ON ld.copy_type_id = 2"
		+ " AND lesson.learning_design_id = ld.learning_design_id"
		+ " JOIN lams_grouping AS ging ON lesson.lesson_state_id IN (3,5)"
		+ " AND lesson.class_grouping_id = ging.grouping_id"
		+ " JOIN lams_group AS g ON ging.grouping_id = g.grouping_id AND g.group_id != ging.staff_group_id"
		+ " JOIN lams_user_group AS ug ON ug.user_id = :userId AND g.group_id = ug.group_id"),
	@NamedNativeQuery(name = "learnerLessonsByOrgAndUserWithCompletedFlag", resultSetMapping = "lessonsByOrgAndUserWithCompletedFlag", query = "SELECT l.lesson_id, l.name, l.description, l.lesson_state_id,"
		+ " lp.lesson_completed_flag, l.enable_lesson_notifications,"
		+ " (SELECT TRUE FROM lams_lesson_dependency ld WHERE ld.lesson_id = l.lesson_id LIMIT 1) AS dependent,"
		+ "  l.schedule_end_date_time IS NOT NULL OR l.scheduled_number_days_to_lesson_finish IS NOT NULL AS scheduledFinish"
		+ "  FROM lams_lesson AS l JOIN lams_learning_design AS ld ON l.organisation_id = :orgId"
		+ "  AND ld.copy_type_id != 3 AND l.lesson_state_id != 7"
		+ "  AND l.learning_design_id = ld.learning_design_id"
		+ "  JOIN lams_group AS g ON l.class_grouping_id = g.grouping_id"
		+ "  JOIN lams_user_group AS ug ON ug.user_id = :userId AND ug.group_id = g.group_id"
		+ "  JOIN lams_grouping AS gi ON gi.grouping_id = g.grouping_id"
		+ "  AND g.group_id != gi.staff_group_id"
		+ "  LEFT JOIN lams_learner_progress AS lp ON lp.user_id = ug.user_id"
		+ "  AND lp.lesson_id = l.lesson_id"),
	@NamedNativeQuery(name = "staffLessonsByOrgAndUserWithCompletedFlag", resultSetMapping = "lessonsByOrgAndUserWithCompletedFlag", query = "SELECT l.lesson_id, l.name, l.description, l.lesson_state_id,"
		+ " lp.lesson_completed_flag, l.enable_lesson_notifications,"
		+ " (SELECT TRUE FROM lams_lesson_dependency ld WHERE ld.lesson_id = l.lesson_id LIMIT 1) AS dependent,"
		+ "  l.schedule_end_date_time IS NOT NULL OR l.scheduled_number_days_to_lesson_finish IS NOT NULL AS scheduledFinish"
		+ " FROM lams_lesson AS l JOIN lams_learning_design AS ld ON l.organisation_id = :orgId"
		+ " AND ld.copy_type_id != 3 AND l.lesson_state_id != 7"
		+ " AND l.learning_design_id = ld.learning_design_id"
		+ " JOIN lams_group AS g ON l.class_grouping_id = g.grouping_id"
		+ " JOIN lams_user_group AS ug ON ug.user_id = :userId AND ug.group_id = g.group_id"
		+ " JOIN lams_grouping AS gi ON gi.grouping_id = g.grouping_id AND g.group_id = gi.staff_group_id"
		+ " LEFT JOIN lams_learner_progress AS lp ON lp.user_id = ug.user_id"
		+ " AND lp.lesson_id = l.lesson_id"),
	@NamedNativeQuery(name = "allLessonsByOrgAndUserWithCompletedFlag", resultSetMapping = "lessonsByOrgAndUserWithCompletedFlag", query = "SELECT l.lesson_id, l.name, l.description, l.lesson_state_id,"
		+ " lp.lesson_completed_flag, l.enable_lesson_notifications,"
		+ " (SELECT TRUE FROM lams_lesson_dependency ld WHERE ld.lesson_id = l.lesson_id LIMIT 1) AS dependent,"
		+ " l.schedule_end_date_time IS NOT NULL OR l.scheduled_number_days_to_lesson_finish IS NOT NULL AS scheduledFinish"
		+ " FROM lams_lesson AS l JOIN lams_learning_design AS ld ON ld.copy_type_id != 3"
		+ " AND l.lesson_state_id != 7 AND l.organisation_id = :orgId"
		+ " AND l.learning_design_id = ld.learning_design_id"
		+ " LEFT JOIN lams_learner_progress lp ON lp.user_id = :userId AND lp.lesson_id = l.lesson_id"),
	@NamedNativeQuery(resultClass = Lesson.class, name = "lessonsByOrgAndUserWithChildOrgs", query = "SELECT DISTINCT lesson.*"
		+ " FROM lams_lesson AS lesson JOIN lams_learning_design AS ld ON ld.copy_type_id != 3"
		+ " AND lesson.lesson_state_id != 7 AND lesson.learning_design_id = ld.learning_design_id"
		+ " JOIN lams_organisation AS lo ON lesson.organisation_id = lo.organisation_id"
		+ " AND (lo.organisation_id = :orgId OR lo.parent_organisation_id = :orgId)"
		+ " JOIN lams_group AS g ON lesson.class_grouping_id = g.grouping_id"
		+ " JOIN lams_user_group AS ug ON ug.user_id = :userId" + " AND ug.group_id = g.group_id") })
@SqlResultSetMappings(@SqlResultSetMapping(name = "lessonsByOrgAndUserWithCompletedFlag", columns = {
	@ColumnResult(name = "lesson_id", type = Long.class), @ColumnResult(name = "name", type = String.class),
	@ColumnResult(name = "description", type = String.class),
	@ColumnResult(name = "lesson_state_id", type = Integer.class),
	@ColumnResult(name = "lesson_completed_flag", type = Boolean.class),
	@ColumnResult(name = "enable_lesson_notifications", type = Boolean.class),
	@ColumnResult(name = "dependent", type = Boolean.class),
	@ColumnResult(name = "scheduledFinish", type = Boolean.class) }))
@Entity
@Table(name = "lams_lesson")
public class Lesson implements Serializable {

    private static final long serialVersionUID = 5733920851084229175L;

    // ---------------------------------------------------------------------
    // Class level constants
    // ---------------------------------------------------------------------
    /**
     * The state for newly created lesson. The learning design has been copied. The lesson class may or may not have
     * been configured. It is seen on the staff interface but not on the learning interface.
     */
    public static final Integer CREATED = 1;
    /** The state for lessons that have been scheduled. */
    public static final Integer NOT_STARTED_STATE = 2;
    /** The state for started lesson */
    public static final Integer STARTED_STATE = 3;
    /**
     * The state for lessons that have been suspended by the teacher. The lesson can be seen on the staff interface but
     * not on the learning interface
     */
    public static final Integer SUSPENDED_STATE = 4;
    /**
     * The state for lessons that have been finished. A finished lesson is shown as inactive on the staff interface, and
     * is shown on the learner interface but the learner is to only see the overall progress and be able to export data
     * - they should not be able to iteract with the tools
     */
    public static final Integer FINISHED_STATE = 5;
    /**
     * The state for lesssons that are shown as inactive on the staff interface but no longer visible to the learners.
     */
    public static final Integer ARCHIVED_STATE = 6;
    /** The state for lesssons that are removed and never can be accessed again */
    public static final Integer REMOVED_STATE = 7;

    @Id
    @Column(name = "lesson_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @Column(name = "name")
    private String lessonName;

    @Column(name = "create_date_time")
    private Date createDateTime;

    @Column(name = "start_date_time")
    private Date startDateTime;

    @Column(name = "end_date_time")
    private Date endDateTime;

    @Column(name = "schedule_start_date_time")
    private Date scheduleStartDate;

    @Column(name = "schedule_end_date_time")
    private Date scheduleEndDate;

    @Column(name = "scheduled_number_days_to_lesson_finish")
    private Integer scheduledNumberDaysToLessonFinish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "lesson_state_id")
    private Integer lessonStateId;

    @Column(name = "previous_state_id")
    private Integer previousLessonStateId;

    @ManyToOne
    @JoinColumn(name = "learning_design_id")
    private LearningDesign learningDesign;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "class_grouping_id")
    private LessonClass lessonClass;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LearnerProgress> learnerProgresses = new HashSet<>();

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GradebookUserLesson> gradebookUserLessons = new HashSet<>();

    @Column(name = "enable_lesson_intro")
    private boolean enableLessonIntro = false;

    @Column(name = "description")
    private String lessonDescription;

    @Column(name = "display_design_image")
    private boolean displayDesignImage = false;

    @Column(name = "locked_for_edit")
    private boolean lockedForEdit = false;

    @Column(name = "live_edit_enabled")
    private boolean liveEditEnabled = false;

    @Column(name = "enable_lesson_notifications")
    private boolean enableLessonNotifications = false;

    @Column(name = "marks_released")
    private boolean marksReleased = false;

    /**
     * Should Learner start the lesson from the beginning each time he enters it.
     * Content is not removed, LessonProgress is deleted, not archived.
     */
    @Column(name = "force_restart")
    private boolean forceLearnerRestart = false;

    /**
     * Should Learners be allowed to restart the lesson after finishing it.
     * Content is not removed, LessonProgress is archived and then deleted.
     */
    @Column(name = "allow_restart")
    private boolean allowLearnerRestart = false;

    /**
     * Should learners be displayed activity gradebook on lesson complete.
     */
    @Column(name = "gradebook_on_complete")
    private boolean gradebookOnComplete = false;

    /**
     * For lesson conditional release
     */
    @ManyToMany
    @JoinTable(name = "lams_lesson_dependency", joinColumns = @JoinColumn(name = "lesson_id"), inverseJoinColumns = @JoinColumn(name = "preceding_lesson_id"))
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<Lesson> precedingLessons = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "lams_lesson_dependency", joinColumns = @JoinColumn(name = "preceding_lesson_id"), inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<Lesson> succeedingLessons = new HashSet<>();

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
	    Integer previousLessonStateId, LearningDesign learningDesign, Set<LearnerProgress> learnerProgresses,
	    Boolean enableLessonIntro, Boolean displayDesignImage, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean forceLearnerRestart, Boolean allowLearnerRestart, Boolean gradebookOnComplete,
	    Integer scheduledNumberDaysTolessonFinish) {
	this(null, name, description, createDateTime, null, null, user, lessonStateId, previousLessonStateId, enableLessonIntro, displayDesignImage, false,
		learningDesign, null, null, learnerProgresses, liveEditEnabled, enableLessonNotifications, forceLearnerRestart, allowLearnerRestart,
		gradebookOnComplete, scheduledNumberDaysTolessonFinish);
    }

    /** full constructor */
    public Lesson(Long lessonId, String name, String description, Date createDateTime, Date startDateTime,
	    Date endDateTime, User user, Integer lessonStateId, Integer previousLessonStateId,
	    Boolean enableLessonIntro, Boolean displayDesignImage, Boolean lockedForEdit, LearningDesign learningDesign,
	    LessonClass lessonClass, Organisation organisation, Set<LearnerProgress> learnerProgresses,
	    Boolean liveEditEnabled,
	    Boolean enableLessonNotifications, Boolean forceLearnerRestart, Boolean allowLearnerRestart,
	    Boolean gradebookOnComplete, Integer scheduledNumberDaysToLessonFinish) {
	this.lessonId = lessonId;
	this.lessonName = name;
	this.lessonDescription = description;
	this.createDateTime = createDateTime;
	this.startDateTime = startDateTime;
	this.endDateTime = endDateTime;
	this.user = user;
	this.lessonStateId = lessonStateId;
	this.previousLessonStateId = previousLessonStateId;
	this.enableLessonIntro = enableLessonIntro != null ? enableLessonIntro : false;
	this.displayDesignImage = displayDesignImage != null ? displayDesignImage : false;
	this.lockedForEdit = false;
	this.learningDesign = learningDesign;
	this.lessonClass = lessonClass;
	this.organisation = organisation;
	this.learnerProgresses = learnerProgresses;
	this.liveEditEnabled = liveEditEnabled != null ? liveEditEnabled : false;
	this.enableLessonNotifications = enableLessonNotifications != null ? enableLessonNotifications : false;
	this.forceLearnerRestart = forceLearnerRestart != null ? forceLearnerRestart : false;
	this.allowLearnerRestart = allowLearnerRestart != null ? allowLearnerRestart : false;
	this.gradebookOnComplete = gradebookOnComplete != null ? gradebookOnComplete : false;
	this.gradebookUserLessons = new HashSet<>();
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
	    LearningDesign ld, Boolean enableLessonIntro, Boolean displayDesignImage, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean forceLearnerRestart, Boolean allowLearnerRestart, Boolean gradebookOnComplete,
	    Integer scheduledNumberDaysToLessonFinish) {
	return new Lesson(lessonName, lessonDescription, new Date(System.currentTimeMillis()), user, Lesson.CREATED,
		null, ld, new HashSet<LearnerProgress>(), enableLessonIntro, displayDesignImage,
		liveEditEnabled, enableLessonNotifications, forceLearnerRestart, allowLearnerRestart, 
		gradebookOnComplete, scheduledNumberDaysToLessonFinish);
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

    public Set<LearnerProgress> getLearnerProgresses() {
	return this.learnerProgresses;
    }

    public void setLearnerProgresses(Set<LearnerProgress> learnerProgresses) {
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

    public Set<User> getAllLearners() {
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

    public boolean isEnableLessonIntro() {
	return enableLessonIntro;
    }

    public void setEnableLessonIntro(boolean enableLessonIntro) {
	this.enableLessonIntro = enableLessonIntro;
    }

    public boolean isDisplayDesignImage() {
	return displayDesignImage;
    }

    public void setDisplayDesignImage(boolean displayDesignImage) {
	this.displayDesignImage = displayDesignImage;
    }

    public boolean getLockedForEdit() {
	return lockedForEdit;
    }

    public void setLockedForEdit(boolean lockedForEdit) {
	this.lockedForEdit = lockedForEdit;
    }

    public boolean getLiveEditEnabled() {
	return liveEditEnabled;
    }

    public void setLiveEditEnabled(boolean liveEditEnabled) {
	this.liveEditEnabled = liveEditEnabled;
    }

    public boolean getEnableLessonNotifications() {
	return enableLessonNotifications;
    }

    public void setEnableLessonNotifications(boolean enableLessonNotifications) {
	this.enableLessonNotifications = enableLessonNotifications;
    }

    public boolean getMarksReleased() {
	return marksReleased;
    }

    public void setMarksReleased(boolean marksReleased) {
	this.marksReleased = marksReleased;
    }

    public boolean getForceLearnerRestart() {
	return forceLearnerRestart;
    }

    public void setForceLearnerRestart(boolean forceLearnerRestart) {
	this.forceLearnerRestart = forceLearnerRestart;
    }

    public boolean getAllowLearnerRestart() {
	return allowLearnerRestart;
    }

    public void setAllowLearnerRestart(boolean allowLearnerRestart) {
	this.allowLearnerRestart = allowLearnerRestart;
    }

    public boolean getGradebookOnComplete() {
	return gradebookOnComplete;
    }

    public void setGradebookOnComplete(boolean gradebookOnComplete) {
	this.gradebookOnComplete = gradebookOnComplete;
    }
}