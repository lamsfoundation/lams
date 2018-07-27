/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General License for more details.
 *
 * You should have received a copy of the GNU General License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learning.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learning.web.bean.ActivityURL;
import org.lamsfoundation.lams.learning.web.bean.GateActivityDTO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 *
 * All Learner service methods that are available within the core. These methods may require all the tool's Spring
 * context files to be loaded, in addition to the core Spring context files. Hence it should only be used from
 * lams-learning, lams-monitoring, lams-central wars.
 */
public interface ICoreLearnerService extends ILearnerService {
    /** Get the user service. Used when the action needs the real user object, not just the userId */
    IUserManagementService getUserManagementService();

    /**
     * Gets the lesson object for the given key.
     *
     */
    Lesson getLesson(Long lessonID);

    /**
     * Joins a User to a a new lesson as a learner
     *
     * @param learnerId
     *            the Learner's userID
     * @param lessionID
     *            identifies the Lesson to start
     * @throws LearnerServiceException
     *             in case of problems.
     */
    LearnerProgress joinLesson(Integer learnerId, Long lessonID);

    /**
     * This method navigate through all the tool activities for the given activity. For each tool activity, we look up
     * the database to check up the existance of correspondent tool session. If the tool session doesn't exist, we
     * create a new tool session instance.
     *
     * @param learnerProgress
     *            the learner progress we are processing.
     * @throws LamsToolServiceException
     */
    void createToolSessionsIfNecessary(Activity activity, LearnerProgress learnerProgress);

    /**
     * Returns the current progress data of the User.
     *
     * @param learnerId
     *            the Learner's userID
     * @param lessonId
     *            the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException
     *             in case of problems.
     */
    LearnerProgress getProgress(Integer learnerId, Long lessonId);

    /**
     * Get the last attempt ID for the given learner and lesson.
     */
    Integer getProgressArchiveMaxAttemptID(Integer userId, Long lessonId);

    /**
     * Returns the current progress data, in the DTO format required by the jsp progress screen, of the User.
     *
     * @param learnerId
     *            the Learner's userID
     * @param lessonId
     *            the Lesson to get progress from.
     * @return Array of two objects. [0] List<ActivityURL>, [1] Activity ID of the current activity
     * @throws LearnerServiceException
     *             in case of problems.
     */
    Object[] getStructuredActivityURLs(Integer learnerId, Long lessonId);

    List<ActivityURL> getStructuredActivityURLs(Long lessonId);

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
     * Calculates learner progress and returns the data required to be displayed to the learner (including URL(s)). This
     * method is included in the interface for testing purposes.
     *
     * @param completedActivityID
     *            identifies the activity just completed
     * @param learner
     *            the Learner
     * @throws LearnerServiceException
     *             in case of problems.
     */
    void calculateProgress(Activity completedActivity, Integer learnerId, LearnerProgress learnerProgress);

    /**
     * Complete the activity in the progress engine and delegate to the progress engine to calculate the next activity
     * in the learning design. It is currently triggered by various progress engine related action classes, which then
     * calculate the url to go to next, based on the ActivityMapping class.
     */
    void completeActivity(Integer learnerId, Activity activity, Long progressID);

    /**
     * If specified activity is set to produce ToolOutput, calculates and stores mark to gradebook.
     *
     * @param toolActivity
     * @param progress
     */
    void updateGradebookMark(Activity activity, LearnerProgress progress);

    /**
     * Retrieve all lessons that has been started, suspended or finished. All finished but archived lesson should not be
     * loaded.
     *
     * @param learner
     *            the user who intend to start a lesson
     * @return a list of active lessons.
     */
    LessonDTO[] getActiveLessonsFor(Integer learnerId);

    /**
     * Returns an activity according to the activity id.
     *
     * @param activityId
     *            the activity id.
     * @return the activity requested.
     */
    Activity getActivity(Long activityId);

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
     * Check up the gate status to go through the gate. This also updates the gate. This method should be used when we
     * do have an grouping activity that is already part of the Hibernate session.
     *
     * @param gate
     *            the gate that current learner is facing. It could be synch gate, schedule gate or permission gate.
     *            Don't supply the actual gate from the cached web version as it might be out of date or not attached to
     *            the session
     * @param knocker
     *            the learner who wants to go through the gate.
     * @param forceGate
     *            if forceGate==true and the lesson is a preview lesson then the gate is opened straight away.
     * @return Updated gate details
     */
    GateActivityDTO knockGate(GateActivity gateActivity, User knocker, boolean forceGate);

    Set<Group> getGroupsForGate(GateActivity gate);

    /**
     * Get the lesson for this activity. If the activity is not part of a lesson (ie is from an authoring design then it
     * will return null.
     */
    Lesson getLessonByActivity(Activity activity);

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

    IActivityDAO getActivityDAO();

}
