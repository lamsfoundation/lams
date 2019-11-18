package org.lamsfoundation.lams.learning.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.learning.command.model.Command;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.learningdesign.dto.GateActivityDTO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Contains methods intended for internal usage by lams_learning.
 * 
 * @author Andrey Balan
 */
public interface ILearnerFullService extends ILearnerService {

    /**
     * Get the last attempt ID for the given learner and lesson.
     */
    Integer getProgressArchiveMaxAttemptID(Integer userId, Long lessonId);

    ActivityPositionDTO getActivityPosition(Long activityId);

    List<Command> getCommandsForLesson(Long lessonId, Date laterThan);

    /**
     * Return the current progress data against progress id.
     *
     * @param progressId
     * @return
     */
    LearnerProgress getProgressById(Long progressId);

    /**
     * Marks an activity as attempted. Called when a user selects an OptionsActivity.
     *
     * @param learnerId
     *            the Learner's userID
     * @param lessonId
     *            the Lesson to get progress from.
     * @param activity
     *            the activity being attempted.
     * @param clearCompletedFlag
     *            If the lesson is completed but this activity is unstarted, should we mark it as incomplete? Used for
     *            branching and optional sequences for skipped sequences (e.g. force completed branching)
     * @return LearnerProgress
     */
    LearnerProgress chooseActivity(Integer learnerId, Long lessonId, Activity activity, Boolean clearCompletedFlag);
    
    /**
     * "Complete" an activity from the web layer's perspective. Used for CompleteActivityAction and the Gate and
     * Grouping actions. Calls the completeActivity(Integer, Activity, Long) to actually complete the activity and
     * progress.
     *
     * @param redirect
     *            Should this call redirect to the next screen (true) or use a forward (false)
     */
    String completeActivity(ActivityMapping actionMappings, LearnerProgress progress, Activity currentActivity,
	    Integer learnerId, boolean redirect) throws UnsupportedEncodingException;

    /**
     * Perform grouping for the learners who have started the lesson, based on the grouping activity.
     *
     * @param lessonId
     *            lesson id
     * @param groupingActivityId
     *            the activity that has create grouping.
     * @param learnerId
     *            the learner who triggers the grouping.
     * @param forceGrouping
     *            if forceGrouping==true and the lesson is a preview lesson then the groupings is done irrespective of
     *            the grouping type
     * @return true if grouping done, false if waiting for grouping to occur
     */
    boolean performGrouping(Long lessonId, Long groupingActivityId, Integer learnerId, boolean forceGrouping);

    /**
     * Perform grouping for the learner, depending on his/hers choice.
     *
     * @param lessonId
     *            lesson id
     * @param groupingActivityId
     *            the activity that create grouping.
     * @param groupId
     *            id of the group chosen by the learner
     * @param learnerId
     *            the learner who triggers the grouping.
     * @throws LearnerServiceException
     */
    void learnerChooseGroup(Long lessonId, Long groupingActivityId, Long groupId, Integer learnerId)
	    throws LearnerServiceException;

    /**
     * Returns the maximum number of learners per group in learner's choice grouping.
     *
     * @param lessonId
     *            id of the lesson
     * @param groupingId
     *            id of the grouping activity
     * @return the maximum number of learners per group;<code>null</code> if the requirement for equal number of
     *         learners in groups was not set
     */
    Integer calculateMaxNumberOfLearnersPerGroup(Long lessonId, Long groupingId);

    Grouping getGrouping(Long groupingId);

    /**
     * Check up the gate status to go through the gate. This also updates the gate. This method should be used when we
     * do not have an grouping activity that is already part of the Hibernate session.
     *
     * @param gateid
     *            the gate that current learner is facing. It could be synch gate, schedule gate or permission gate.
     * @param knocker
     *            the learner who wants to go through the gate.
     * @param forceGate
     *            if forceGate==true and the lesson is a preview lesson then the gate is opened straight away.
     * @return Updated gate details
     */
    GateActivityDTO knockGate(Long gateActivityId, User knocker, boolean forceGate);

    /**
     *
     * @param learnerId
     *            the learner who triggers the move
     * @param lessonId
     *            lesson id
     * @param fromActivity
     *            Activity moving from
     * @param toActivity
     *            Activity moving to (being run)
     * @return updated Learner Progress
     */
    LearnerProgress moveToActivity(Integer learnerId, Long lessonId, Activity fromActivity, Activity toActivity);

    /**
     * Work out which branch to which a user should go. If the current lesson is a preview lesson, it will force the
     * user to a branch if at all possible.
     *
     * @param lesson
     *            current lesson.
     * @param BranchingActivity
     *            the branching activity
     * @param learnerId
     *            the learner who triggers the grouping.
     * @throws LearnerServiceException
     */
    SequenceActivity determineBranch(Lesson lesson, BranchingActivity branchingActivity, Integer learnerId)
	    throws LearnerServiceException;

    /**
     * Select a particular branch - we are in preview mode and the author has selected a particular activity.
     *
     * @param lesson
     *            current lesson.
     * @param BranchingActivity
     *            the branching activity
     * @param learnerId
     *            the learner who triggers the grouping.
     * @return branchId of the desired branch
     * @throws LearnerServiceException
     */
    SequenceActivity selectBranch(Lesson lesson, BranchingActivity branchingActivity, Integer learnerId, Long branchId)
	    throws LearnerServiceException;

    /* Added for RepopulateProgressMarksServlet - can be removed later */
    String[] recalcProgressForLearner(Lesson lesson, ArrayList<Activity> activityList, LearnerProgress learnerProgress,
	    boolean updateGradebookForAll);
}
