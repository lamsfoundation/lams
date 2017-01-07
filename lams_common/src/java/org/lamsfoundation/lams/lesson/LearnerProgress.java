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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.dto.CompletedActivityDTO;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressCompletedDTO;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * <p>
 * Holds data that describes the Users progress through a lesson. It records the
 * exact position that a learner is in regarding a lesson.
 * </p>
 *
 * <p>
 * It also helps lams to rebuild the learner page and progress bar whenever an
 * unexpected error condition is identified.
 * </p>
 *
 * @author Chris
 * @author Jacky Fang
 * @version 1.1
 *
 */
public class LearnerProgress implements Serializable {
    private static final long serialVersionUID = -7866830317967062822L;

    private static Logger log = Logger.getLogger(LearnerProgress.class);

    //---------------------------------------------------------------------
    // Class level constants
    //---------------------------------------------------------------------
    /** Indicates activity has been completed */
    public static final byte ACTIVITY_COMPLETED = 1;
    /** Indicates activity has been attempted but not completed */
    public static final byte ACTIVITY_ATTEMPTED = 2;
    /** Indicates activity has not been attempted yet */
    public static final byte ACTIVITY_NOT_ATTEMPTED = 3;

    /** Parallel waiting state: Not waiting in any way */
    public static final byte PARALLEL_NO_WAIT = 0;
    /**
     * Parallel waiting state: One activity complete, the others still to be
     * completed
     */
    public static final byte PARALLEL_WAITING = 1;
    /**
     * Parallel waiting state: All activities completed, break out of parallel
     * frames
     */
    public static final byte PARALLEL_WAITING_COMPLETE = 2;

    /** Learner has not completed the lesson */
    public static final byte LESSON_NOT_COMPLETE = 0;
    /** Learner has completed the lesson in the normal manner. */
    public static final byte LESSON_END_OF_DESIGN_COMPLETE = 1;
    /**
     * Learner has completed the lesson by reaching a "Stop After Activity"
     * point
     */
    public static final byte LESSON_IN_DESIGN_COMPLETE = 2;

    //---------------------------------------------------------------------
    // attributes
    //---------------------------------------------------------------------
    /** Identifier field */
    private Long learnerProgressId;

    /** The User to whom this progress data belongs. */
    private User user;

    /** The Lesson this progress data is for */
    private Lesson lesson;

    /** Map of attempted activities with their start date */
    private Map<Activity, Date> attemptedActivities;

    /**
     * Set of completed activities that includes all completed activities before
     * current activity
     */
    private Map<Activity, CompletedActivityProgress> completedActivities;

    /**
     * The activity that user just completed. The purpose of this activity is to
     * allow lams to remove unecessary frame for next activity.
     */
    private Activity previousActivity;

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
     * The activity that progress engine is about to progress to. It could be
     * next activity following the transition or leaf activity within a complex
     * activity.
     */
    private Activity nextActivity;

    /**
     * Indicates is the User has completed this lesson.
     */
    private Byte lessonComplete;

    /**
     * Indicates the learner progress is in a incomplete parallel activity or
     * not.
     */
    private byte parallelWaiting;

    /**
     * A list of completed activities ids before move on to next activity
     * following transition. This is created to help UI calculation what has
     * *just* been done.
     */
    private List currentCompletedActivitiesList;

    /** Indicate whether the learning progress is restarting or not */
    private boolean restarting;

    private Date startDate;
    private Date finishDate;

    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------
    /** default constructor */
    public LearnerProgress() {
	this.lessonComplete = new Byte(LESSON_NOT_COMPLETE);
    }

    /**
     * Chain constructor to create new learner progress with minimum data.
     *
     * @param user
     *            the learner.
     * @param lesson
     *            the lesson that currently is running.
     */
    public LearnerProgress(User user, Lesson lesson) {
	this(null, user, lesson, new TreeMap<Activity, Date>(new ActivityOrderComparator()),
		new TreeMap<Activity, CompletedActivityProgress>(new ActivityOrderComparator()));
    }

    /** full constructor */
    public LearnerProgress(Long learnerProgressId, User user, Lesson lesson, Map<Activity, Date> attemptedActivities,
	    Map<Activity, CompletedActivityProgress> completedActivities) {
	this.learnerProgressId = learnerProgressId;
	this.user = user;
	this.lesson = lesson;
	this.attemptedActivities = attemptedActivities;
	this.completedActivities = completedActivities;
	this.lessonComplete = new Byte(LESSON_NOT_COMPLETE);
    }

    //---------------------------------------------------------------------
    // Getters and Setters
    //---------------------------------------------------------------------
    /**
     *
     *
     */
    public Long getLearnerProgressId() {
	return this.learnerProgressId;
    }

    public void setLearnerProgressId(Long learnerProgressId) {
	this.learnerProgressId = learnerProgressId;
    }

    /**
     *
     */
    public User getUser() {
	return this.user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /**
     *
     */
    public Lesson getLesson() {
	return this.lesson;
    }

    public void setLesson(Lesson lesson) {
	this.lesson = lesson;
    }

    /**
     *
     *
     */
    public Map<Activity, Date> getAttemptedActivities() {
	StringBuilder bldr = new StringBuilder("Progress ").append(this.getLearnerProgressId()).append(" user ")
		.append(this.getUser().getLogin()).append(" attempted fetch: ");
	for (Activity act : attemptedActivities.keySet()) {
	    bldr.append(act.getActivityId()).append(act.getTitle()).append(", ");
	}
	log.debug(bldr.toString());
	return this.attemptedActivities;
    }

    public void setAttemptedActivities(Map<Activity, Date> attemptedActivities) {
	this.attemptedActivities = attemptedActivities;
    }

    /**
     *
     *
     */
    public Map<Activity, CompletedActivityProgress> getCompletedActivities() {
	StringBuilder bldr = new StringBuilder("Progress ").append(this.getLearnerProgressId()).append(" user ")
		.append(this.getUser().getLogin()).append(" completed fetch: ");
	for (Activity act : completedActivities.keySet()) {
	    bldr.append(act.getActivityId()).append(act.getTitle()).append(", ");
	}
	return this.completedActivities;
    }

    public void setCompletedActivities(Map<Activity, CompletedActivityProgress> completedActivities) {

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
	if (!(other instanceof LearnerProgress)) {
	    return false;
	}
	LearnerProgress castOther = (LearnerProgress) other;
	return new EqualsBuilder().append(this.getLearnerProgressId(), castOther.getLearnerProgressId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getLearnerProgressId()).toHashCode();
    }

    /**
     * Getter for property currentActivity.
     *
     * @return Value of property currentActivity.
     */
    public Activity getCurrentActivity() {
	return this.currentActivity;
    }

    /**
     * Setter for property currentActivity.
     *
     * @param currentActivity
     *            New value of property currentActivity.
     */
    public void setCurrentActivity(Activity currentActivity) {
	this.currentActivity = currentActivity;
    }

    /**
     * Gives the progress state of the specific activity.
     *
     * @param the
     *            activity whose progress state is required.
     * @return <code>ACTIVITY_COMPLETED</code>,
     *         <code>ACTIVITY_ATTEMPTED</code> or
     *         <code>ACTIVITY_NOT_ATTEMPTED</code>.
     */
    public byte getProgressState(Activity activity) {
	if (getCompletedActivities().containsKey(activity)) {
	    return ACTIVITY_COMPLETED;
	} else if (getAttemptedActivities().containsKey(activity)) {
	    return ACTIVITY_ATTEMPTED;
	} else {
	    return ACTIVITY_NOT_ATTEMPTED;
	}
    }

    /**
     * Sets the progress state for an activity.
     *
     * If the activity is moving from completed to not completed, then the call
     * is recursive - it will reset all contained completed activities to the
     * input state.
     *
     * Only want to "take action" ie add/remove if the state has really changed.
     * Otherwise the recursive call to remove the completed flag will cause
     * unexpected side effects when a Completed activity is reset to Completed
     *
     * @param activity
     *            whose progress is to be set
     * @param state
     *            one of <code>ACTIVITY_COMPLETED</code>,
     *            <code>ACTIVITY_ATTEMPTED</code> or
     *            <code>ACTIVITY_NOT_ATTEMPTED</code>.
     * @param activityDAO
     *            needed to get any child activities correctly from
     *            Hibernate (grr - shouldn't be required)
     */
    public synchronized void setProgressState(Activity activity, byte state, IActivityDAO activityDAO) {

	StringBuilder bldr = new StringBuilder("Progress ").append(this.getLearnerProgressId())
		.append(" before dump: activity ").append(activity.getActivityId()).append(" state ").append(state)
		.append(" attempted ");
	for (Activity act : getAttemptedActivities().keySet()) {
	    bldr.append(act.getActivityId()).append(act.getTitle()).append(", ");
	}
	bldr.append(" completed ");
	for (Activity act : getCompletedActivities().keySet()) {
	    bldr.append(act.getActivityId()).append(act.getTitle()).append(", ");
	}
	log.debug(bldr.toString());

	// remove activity from current set
	byte oldState = getProgressState(activity);
	if (oldState == state) {
	    log.debug("Progress " + this.getLearnerProgressId() + " does not change state " + state + " of activity "
		    + activity.getActivityId());
	    // no real change, forget the rest of the method
	    return;
	}

	Date activityStartDate = getAttemptedActivities().get(activity);
	if (activityStartDate != null && state == LearnerProgress.ACTIVITY_ATTEMPTED) {
	    log.warn("Progress " + this.getLearnerProgressId() + " found newly attempted activity "
		    + activity.getActivityId() + " already in in the attempted activities set.");
	} else if (activityStartDate == null && state != LearnerProgress.ACTIVITY_ATTEMPTED) {
	    log.warn("Progress " + this.getLearnerProgressId() + " found NULL start date of activity "
		    + activity.getActivityId() + ". Activity missing from attempted activities list.");
	}

	if (oldState == LearnerProgress.ACTIVITY_NOT_ATTEMPTED) {
	    log.debug("Progress " + this.getLearnerProgressId() + " does not remove not attempted activity "
		    + activity.getActivityId());
	    ;
	} else if (oldState == LearnerProgress.ACTIVITY_ATTEMPTED) {
	    log.debug("Progress " + this.getLearnerProgressId() + " removes attempted activity "
		    + activity.getActivityId());
	    this.getAttemptedActivities().remove(activity);
	} else if (oldState == LearnerProgress.ACTIVITY_COMPLETED) {
	    log.debug("Progress " + this.getLearnerProgressId() + " removes completed activity "
		    + activity.getActivityId());
	    this.getCompletedActivities().remove(activity);
	    if (activity.isComplexActivity()) {
		ComplexActivity complex = (ComplexActivity) activityDAO
			.getActivityByActivityId(activity.getActivityId(), ComplexActivity.class);
		Iterator iter = complex.getActivities().iterator();
		while (iter.hasNext()) {
		    Activity child = (Activity) iter.next();
		    setProgressState(child, state, activityDAO);
		}
	    }
	}

	// add activity to new set
	if (state == LearnerProgress.ACTIVITY_NOT_ATTEMPTED) {
	    log.debug("Progress " + this.getLearnerProgressId() + " does not add not attempted activity "
		    + activity.getActivityId());
	    ;
	} else if (state == LearnerProgress.ACTIVITY_ATTEMPTED) {
	    log.debug(
		    "Progress " + this.getLearnerProgressId() + " adds attempted activity " + activity.getActivityId());
	    this.getAttemptedActivities().put(activity, new Date());
	} else if (state == LearnerProgress.ACTIVITY_COMPLETED) {
	    log.debug(
		    "Progress " + this.getLearnerProgressId() + " adds completed activity " + activity.getActivityId());
	    this.getCompletedActivities().put(activity,
		    new CompletedActivityProgress(this, activity, activityStartDate, new Date()));
	}

	bldr = new StringBuilder("Progress ").append(this.getLearnerProgressId()).append(" after dump: activity ")
		.append(activity.getActivityId()).append(" state ").append(state).append(" attempted ");
	for (Activity act : getAttemptedActivities().keySet()) {
	    bldr.append(act.getActivityId()).append(act.getTitle()).append(", ");
	}
	bldr.append(" completed ");
	for (Activity act : getCompletedActivities().keySet()) {
	    bldr.append(act.getActivityId()).append(act.getTitle()).append(", ");
	}
	log.debug(bldr.toString());

//	if (this.getLearnerProgressId() != null) {
//	    activityDAO.update(this);
//	}
    }

    /**
     * Has the user completed the lesson? We don't care how (ie at end of
     * sequence or after a "stop after activity")
     */
    public boolean isComplete() {

	return lessonComplete == LESSON_END_OF_DESIGN_COMPLETE || lessonComplete == LESSON_IN_DESIGN_COMPLETE;
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

    /**
     * Getter for property nextActivity.
     *
     * @return Value of property nextActivity.
     */
    public Activity getNextActivity() {

	return this.nextActivity;
    }

    /**
     * Setter for property nextActivity.
     *
     * @param nextActivity
     *            New value of property nextActivity.
     */
    public void setNextActivity(Activity nextActivity) {

	this.nextActivity = nextActivity;
    }

    /**
     * @return Returns the previousActivity.
     */
    public Activity getPreviousActivity() {
	return previousActivity;
    }

    /**
     * @param previousActivity
     *            The previousActivity to set.
     */
    public void setPreviousActivity(Activity previousActivity) {
	this.previousActivity = previousActivity;
    }

    /**
     * @return Returns the isParallelWaiting.
     */
    public byte getParallelWaiting() {
	return parallelWaiting;
    }

    /**
     * @param isParallelWaiting
     *            The isParallelWaiting to set.
     */
    public void setParallelWaiting(byte parallelWaiting) {
	this.parallelWaiting = parallelWaiting;
    }

    /**
     * @return Returns the currentCompletedActivitiesList.
     */
    public List getCurrentCompletedActivitiesList() {
	return currentCompletedActivitiesList;
    }

    /**
     * @param completedActivitiesList
     *            The currentCompletedActivitiesList to set.
     */
    public void setCurrentCompletedActivitiesList(List completedActivitiesList) {
	this.currentCompletedActivitiesList = new LinkedList();
	this.currentCompletedActivitiesList.addAll(completedActivitiesList);
    }

    /**
     * @return Returns the isRestarting.
     */
    public boolean isRestarting() {
	return restarting;
    }

    /**
     * @param isRestarting
     *            The isRestarting to set.
     */
    public void setRestarting(boolean restarting) {
	this.restarting = restarting;
    }

    //---------------------------------------------------------------------
    // Service methods
    //---------------------------------------------------------------------
    /**
     * Returns the learner progress data transfer object.
     */
    public LearnerProgressDTO getLearnerProgressData() {

	return new LearnerProgressDTO(this.lesson.getLessonId(), this.lesson.getLessonName(), this.user.getLogin(),
		this.user.getLastName(), this.user.getFirstName(), this.user.getUserId(),
		this.currentActivity != null ? this.currentActivity.getActivityId() : null,
		this.createIdArrayFrom(this.getAttemptedActivities().keySet()),
		this.createIdArrayFrom(this.getCompletedActivities().keySet()), isComplete());
    }

    public LearnerProgressCompletedDTO getLearnerProgressCompletedData() {
	return new LearnerProgressCompletedDTO(this.lesson.getLessonId(), this.lesson.getLessonName(),
		this.user.getLogin(), this.user.getLastName(), this.user.getFirstName(), this.user.getUserId(),
		createCompletedActivityArrayFromMap(this.getCompletedActivities()), isComplete(),
		this.lesson.getStartDateTime().getTime(), this.startDate.getTime());

    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------
    /**
     * Extract the Id from activities and set them into an array.
     *
     * @param activities
     *            the activities that is being used to create the array.
     */
    private Long[] createIdArrayFrom(Set activities) {
	if (activities == null) {
	    throw new IllegalArgumentException("Fail to create id array" + " from null activity set");
	}

	ArrayList<Long> activitiesIds = new ArrayList<Long>();
	for (Iterator i = activities.iterator(); i.hasNext();) {
	    Activity activity = (Activity) i.next();
	    activitiesIds.add(activity.getActivityId());
	}

	return activitiesIds.toArray(new Long[activitiesIds.size()]);
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private CompletedActivityDTO[] createCompletedActivityArrayFromMap(
	    Map<Activity, CompletedActivityProgress> completedActivityProgs) {
	if (completedActivityProgs == null) {
	    throw new IllegalArgumentException("Fail to create id array" + " from null activity set");
	}

	ArrayList<CompletedActivityDTO> activitiesCompleted = new ArrayList<CompletedActivityDTO>();

	for (Entry<Activity, CompletedActivityProgress> ent : completedActivityProgs.entrySet()) {
	    Activity activity = ent.getKey();
	    CompletedActivityProgress compProg = ent.getValue();

	    if (compProg != null) {
		Date end = compProg.getFinishDate();
		Date start = compProg.getStartDate();
		if (end != null && start != null && startDate != null) {
		    Long completedTime = end.getTime() - startDate.getTime();
		    Long startTime = start.getTime() - startDate.getTime();
		    activitiesCompleted.add(new CompletedActivityDTO(activity, startTime, completedTime));
		}
	    }
	}

	return activitiesCompleted.toArray(new CompletedActivityDTO[activitiesCompleted.size()]);
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
}
