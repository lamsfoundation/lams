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

package org.lamsfoundation.lams.learning.progress;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.ParallelWaitActivity;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;

/**
 * The Progress Engine controls how a learner progresses through a sequence.
 *
 * Code must be re-entrant, as there is one progress engine object called by a
 * singleton LearnerService.
 *
 * @author chris, Jacky
 */
public class ProgressEngine {
    protected Logger log = Logger.getLogger(ProgressEngine.class);

    public static final String AUDIT_ACTIVITY_START_KEY = "audit.activity.started";
    public static final String AUDIT_ACTIVITY_STOP_KEY = "audit.activity.stopped";
    public static final String AUDIT_LESSON_COMPLETE_KEY = "audit.learner.lesson.complete";

    private IActivityDAO activityDAO;
    private ILogEventService logEventService;
    private MessageService messageService;

    /**
     * Method determines next step for a learner based on the activity they have
     * just completed. Will clear the Parallel Waiting Complete value if it is
     * currently set.
     *
     * @param learner
     *            The <CODE>User</CODE> who is progressing through the
     *            <CODE>Lesson</CODE>.
     * @param completedActivity
     *            The <CODE>Activity</CODE> the learner has just
     *            completed.
     * @param lesson
     *            The <CODE>Lesson</CODE> the learner needs progress for.
     * @param learnerProgress
     * @return Progress The VO that contains the data needed to send the learner
     *         to the next step.
     * @throws ProgressException
     *             if progress cannot be calculated successfully.
     */
    public void calculateProgress(User learner, Activity completedActivity, LearnerProgress learnerProgress)
	    throws ProgressException {
	if (learnerProgress.getParallelWaiting() == LearnerProgress.PARALLEL_WAITING_COMPLETE) {
	    learnerProgress.setParallelWaiting(LearnerProgress.PARALLEL_NO_WAIT);
	}
	doCalculateProgress(learner, completedActivity, learnerProgress, new LinkedList<Long>());
    }

    /**
     * Internal method used for recursion. Does the actual "work" of
     * calculateProgress.
     */
    private void doCalculateProgress(User learner, Activity completedActivity, LearnerProgress learnerProgress,
	    List<Long> completedActivityList) throws ProgressException {
	learnerProgress.setProgressState(completedActivity, LearnerProgress.ACTIVITY_COMPLETED, activityDAO);
	completedActivityList.add(completedActivity.getActivityId());

	if (completedActivity.isStopAfterActivity()) {
	    // special case - terminate the lesson here.
	    learnerProgress.setProgressState(completedActivity, LearnerProgress.ACTIVITY_COMPLETED, activityDAO);
	    for (Activity parentActivity = completedActivity
		    .getParentActivity(); parentActivity != null; parentActivity = parentActivity.getParentActivity()) {
		learnerProgress.setProgressState(parentActivity, LearnerProgress.ACTIVITY_COMPLETED, activityDAO);
		completedActivityList.add(parentActivity.getActivityId());
	    }
	    populateCurrentCompletedActivityList(learnerProgress, completedActivityList);
	    learnerProgress.setFinishDate(new Date());
	    setLessonComplete(learnerProgress, LearnerProgress.LESSON_IN_DESIGN_COMPLETE);
	} else if (completedActivity.isFloating() && !completedActivity.getParentActivity().isParallelActivity()) {
	    // special case - floating activity and not parallel activity (floating) child.
	} else {
	    Transition transition = completedActivity.getTransitionFrom();
	    if (transition != null) {
		progressCompletedActivity(learner, completedActivity, learnerProgress, transition,
			completedActivityList);
	    } else {
		progressParentActivity(learner, completedActivity, learnerProgress, completedActivityList);
	    }
	}
    }

    /**
     * Method determines the start point for a learner when they begin a Lesson.
     *
     * It is also reused to calculate where the learner should be should the
     * progress "go wrong". For example, the teacher does live edit and the
     * learner moves to the stop gate created for the live edit. When the edit
     * is completed, the stop gate is removed. But now there is no current
     * activity for the learner.
     *
     * @param LearnerProgress
     *            The user's progress details for the <CODE>User</CODE>
     *            who is starting the <CODE>Lesson</CODE>.
     * @return LearnerProgress The updated user's progress details.
     * @throws ProgressException
     *             if the start point cannot be calculated successfully.
     */
    public void setUpStartPoint(LearnerProgress progress) throws ProgressException {

	LearningDesign ld = progress.getLesson().getLearningDesign();

	if (progress.getLesson().getLockedForEdit()) {
	    // special case - currently setting up the stop gates for live edit.
	    clearProgressNowhereToGoNotCompleted(progress, "setUpStartPoint");
	} else if (progress.isComplete()) {
	    return;
	} else if (ld.getFirstActivity() == null) {
	    throw new ProgressException("Could not find first activity for " + "learning design [" + ld.getTitle()
		    + "], id[" + ld.getLearningDesignId().longValue() + "]");
	} else if (progress.getCompletedActivities().containsKey(ld.getFirstActivity())) {
	    // special case - recalculating the appropriate current activity.
	    doCalculateProgress(progress.getUser(), ld.getFirstActivity(), progress, new LinkedList<Long>());
	} else if (canDoActivity(progress.getLesson(), ld.getFirstActivity())) {
	    // normal case
	    progress.setCurrentActivity(ld.getFirstActivity());
	    progress.setNextActivity(ld.getFirstActivity());
	    setActivityAttempted(progress, ld.getFirstActivity());
	} else {
	    // special case - trying to get to a whole new activity (past the stop gate)
	    // during a live edit
	    clearProgressNowhereToGoNotCompleted(progress, "setUpStartPoint");
	}
    }

    /**
     * Is it okay for me to do this activity? Most of the time yes - but you can
     * do it if the learning design is marked for edit (due to live edit) and
     * the activity isn't read only. This case should only occur if you have
     * snuck past the stop gates while live edit was being set up. Hopefully
     * never!
     *
     * See the live edit documentation on the wiki for more details on the
     * Lesson.lockedForEdit and LearningDesign.activityReadOnly flags. These are
     * set up in AuthoringService.setupEditOnFlyLock()
     *
     * @param design
     * @param activity
     * @return
     */
    private boolean canDoActivity(Lesson lesson, Activity activity) {
	LearningDesign design = lesson.getLearningDesign();
	return !lesson.getLockedForEdit() && (!design.getEditOverrideLock() || activity.isActivityReadOnly());
    }

    /**
     * Oh, dear - nowhere to go. Probably because the sequence is being edited
     * while I'm trying to move to an untouched activity, or it is in the
     * process of setting up the stop gates for live edit.
     *
     * Set the current activity and next activity to null, and the progress
     * engine should then show the "Sequence Broken" screen.
     *
     * Writes a warning to the log if callingMethod is not null. If it is null,
     * we assume the calling code has written out a warning/error already.
     */
    private void clearProgressNowhereToGoNotCompleted(LearnerProgress progress, String callingMethod) {
	if (callingMethod != null) {
	    log.warn("Learner " + progress.getUser().getFullName() + "(" + progress.getUser().getUserId()
		    + ") has a problem with the progress for lesson " + progress.getLesson().getLessonName() + "("
		    + progress.getLesson().getLessonId() + "). Completed activities so far was "
		    + progress.getCurrentCompletedActivitiesList()
		    + ". Setting current and next activity to null. Problem detected in method " + callingMethod + ".");
	}

	progress.setCurrentActivity(null);
	progress.setNextActivity(null);
	progress.setLessonComplete(LearnerProgress.LESSON_NOT_COMPLETE);
    }

    /**
     * Set the current activity as attempted. If it is a parallel activity, mark
     * its children as attempted too.
     */
    @SuppressWarnings("rawtypes")
    public void setActivityAttempted(LearnerProgress progress, Activity activity) {
	progress.setProgressState(activity, LearnerProgress.ACTIVITY_ATTEMPTED, activityDAO);
	activity.setReadOnly(true);

	if (activity.isParallelActivity()) {
	    ParallelActivity parallel = (ParallelActivity) activityDAO.getActivityByActivityId(activity.getActivityId(),
		    ParallelActivity.class);
	    Iterator iter = parallel.getActivities().iterator();
	    while (iter.hasNext()) {
		Activity element = (Activity) iter.next();
		setActivityAttempted(progress, element);
	    }
	}

	logEventService.logEvent(LogEvent.TYPE_LEARNER_ACTIVITY_START, progress.getUser().getUserId(),
		progress.getUser().getUserId(), progress.getLesson().getLessonId(), activity.getActivityId(),
		messageService.getMessage(AUDIT_ACTIVITY_START_KEY, new Object[] { progress.getUser().getLogin(),
			progress.getUser().getUserId(), activity.getTitle(), activity.getActivityId() }));

	// update activity
	activityDAO.insertOrUpdate(activity);
    }

    /**
     * We setup the progress data for a completed activity. This happens when we
     * find a transition to progress to. It should setup all activity states
     * that allow web layer to calculate the url to move one to.
     *
     * @param completedActivity
     *            the activity finished either by user or the lams. In terms
     *            of lams completed activity, it would be
     *            <code>ParallelActivity</code>,
     *            <code>SequenceActivity</code>,
     *            <code>OptionsActivity</code> and other system driven
     *            activities. Whereas user activity will be mostly tool
     *            activities.
     * @param learnerProgress
     *            the progress we based on.
     * @param transition
     *            the transition we progress to.
     * @return the learner progress data we calculated.
     * @throws ProgressException
     */
    private void progressCompletedActivity(User learner, Activity completedActivity, LearnerProgress learnerProgress,
	    Transition transition, List<Long> completedActivityList) throws ProgressException {
	Activity nextActivity = transition.getToActivity();

	if (!learnerProgress.getCompletedActivities().containsKey(nextActivity)) {
	    // normal case - the next activity is still yet to be done

	    learnerProgress.setPreviousActivity(completedActivity);

	    populateCurrentCompletedActivityList(learnerProgress, completedActivityList);

	    if (canDoActivity(learnerProgress.getLesson(), nextActivity)) {

		learnerProgress.setCurrentActivity(nextActivity);
		learnerProgress.setNextActivity(nextActivity);
		setActivityAttempted(learnerProgress, nextActivity);
		if (learnerProgress.getParallelWaiting() == LearnerProgress.PARALLEL_WAITING) {
		    learnerProgress.setParallelWaiting(LearnerProgress.PARALLEL_WAITING_COMPLETE);
		}

	    } else {
		clearProgressNowhereToGoNotCompleted(learnerProgress, "progressCompletedActivity");
	    }

	} else {
	    // abnormal case: next activity already done. Must have jumped back to an earlier
	    // optional activity, done another activity and then kept going
	    doCalculateProgress(learner, nextActivity, learnerProgress, completedActivityList);
	}

    }

    /**
     * Calculate the progress data for a parent activity if we can not find the
     * transition for a completed activity. Most likely, the completed activity
     * is in the leaf node of an activity hierarchy. And we need to travesal the
     * activity hierarchy upwards to find the progress information.
     *
     * @param learner
     *            the current learner.
     * @param lesson
     *            the lesson that current learner progress belongs to.
     * @param completedActivity
     *            the activity finished either by user or the lams. In terms
     *            of lams completed activity, it would be
     *            <code>ParallelActivity</code>,
     *            <code>SequenceActivity</code>,
     *            <code>OptionsActivity</code> and other system driven
     *            activities. Whereas user activity will be mostly tool
     *            activities.
     * @param learnerProgress
     *            the progress we based on.
     * @return the learner progress data we calculated.
     * @throws ProgressException
     */
    private LearnerProgress progressParentActivity(User learner, Activity completedActivity,
	    LearnerProgress learnerProgress, List<Long> completedActivityList) throws ProgressException {
	Activity parent = completedActivity.getParentActivity();

	if (parent != null) {
	    if (!(parent.isComplexActivity())) {
		throw new ProgressException(
			"Parent activity is always expected" + " to the complex activity. But activity type"
				+ parent.getActivityTypeId() + " has been found");
		//move to next activity within parent if not all children are completed.
	    }

	    ComplexActivity complexParent = (ComplexActivity) activityDAO
		    .getActivityByActivityId(parent.getActivityId(), ComplexActivity.class);
	    complexParent.getComplexActivityStrategy().setActivityDAO(activityDAO);
	    if (!learnerProgress.getCompletedActivities().containsKey(complexParent)
		    && !complexParent.areChildrenCompleted(learnerProgress)) {
		Activity nextActivity = complexParent.getNextActivityByParent(completedActivity);

		if (!isNextActivityValid(nextActivity)) {
		    log.error(
			    "Error occurred in progress engine." + " Unexpected Null activity received when progressing"
				    + " to the next activity within a incomplete parent activity:"
				    + " Parent activity id [" + parent.getActivityId() + "]");
		    clearProgressNowhereToGoNotCompleted(learnerProgress, null);
		} else if (isParallelWaitActivity(nextActivity)) {
		    learnerProgress.setParallelWaiting(LearnerProgress.PARALLEL_WAITING);
		    // learnerProgress.setNextActivity(null);
		    populateCurrentCompletedActivityList(learnerProgress, completedActivityList);
		} else if (canDoActivity(learnerProgress.getLesson(), nextActivity)) {
		    learnerProgress.setNextActivity(nextActivity);
		    setActivityAttempted(learnerProgress, nextActivity);
		    populateCurrentCompletedActivityList(learnerProgress, completedActivityList);
		} else {
		    clearProgressNowhereToGoNotCompleted(learnerProgress, "progressParentActivity");
		}
	    }
	    //recurvisely call back to calculateProgress to calculate completed
	    //parent activity.
	    else {
		learnerProgress.setPreviousActivity(complexParent);
		doCalculateProgress(learner, parent, learnerProgress, completedActivityList);
		if (learnerProgress.getParallelWaiting() == LearnerProgress.PARALLEL_WAITING) {
		    learnerProgress.setParallelWaiting(LearnerProgress.PARALLEL_WAITING_COMPLETE);
		}
	    }
	}
	//lesson is meant to be completed if there is no transition and no parent.
	else {
	    learnerProgress.setFinishDate(new Date());
	    learnerProgress = setLessonComplete(learnerProgress, LearnerProgress.LESSON_END_OF_DESIGN_COMPLETE);
	}

	return learnerProgress;
    }

    /**
     * Set the lesson to complete for this learner.
     *
     * @param learnerProgress
     * @return updated learnerProgress
     */
    private LearnerProgress setLessonComplete(LearnerProgress learnerProgress, byte completionStatus) {
	learnerProgress.setCurrentActivity(null);
	learnerProgress.setNextActivity(null);
	learnerProgress.setLessonComplete(completionStatus);

	// log learner has completed the current lesson event
	logEventService.logEvent(LogEvent.TYPE_LEARNER_LESSON_COMPLETE, learnerProgress.getUser().getUserId(),
		learnerProgress.getUser().getUserId(), learnerProgress.getLesson().getLessonId(), null,
		messageService.getMessage(AUDIT_LESSON_COMPLETE_KEY,
			new Object[] { learnerProgress.getUser().getLogin(), learnerProgress.getUser().getUserId(),
				learnerProgress.getLesson().getLessonName(),
				learnerProgress.getLesson().getLessonId() }));

	return learnerProgress;
    }

    /**
     * The helper method to setup the completed activity list since the last
     * transition.
     *
     * @param learnerProgress
     */
    private void populateCurrentCompletedActivityList(LearnerProgress learnerProgress,
	    List<Long> completedActivityList) {
	learnerProgress.setCurrentCompletedActivitiesList(completedActivityList);
	completedActivityList.clear();
    }

    /**
     * The next valid is valid if it is not null activity or if it is a parallel
     * waiting activity.
     *
     * @param nextActivity
     *            the next activity we progress to.
     * @return is the next activity valid.
     */
    private boolean isNextActivityValid(Activity nextActivity) {
	return !nextActivity.isNull() || isParallelWaitActivity(nextActivity);
    }

    /**
     * Check up the object type to see whether it is a parallel waiting
     * activity.
     *
     * @param nextActivity
     *            the next activity we progress to.
     * @return is the next activity the type of parallel activity.
     */
    private boolean isParallelWaitActivity(Activity nextActivity) {
	return nextActivity.getActivityTypeId() != null
		&& nextActivity.getActivityTypeId().intValue() == ParallelWaitActivity.PARALLEL_WAIT_ACTIVITY_TYPE;
    }

    public void setActivityDAO(IActivityDAO activityDAO) {
	this.activityDAO = activityDAO;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

}