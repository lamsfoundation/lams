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

package org.lamsfoundation.lams.learning.service;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.dto.ActivityPositionDTO;
import org.lamsfoundation.lams.learningdesign.dto.ActivityURL;
import org.lamsfoundation.lams.learningdesign.dto.GateActivityDTO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * All Learner service methods that are available within the core.
 */
public interface ILearnerService {
    // how often Command Websocket Server checks for new commands
    public static final long COMMAND_WEBSOCKET_CHECK_INTERVAL = 5000;

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
     * Marks an tool session as complete and calculates the next activity against the learning design. This method is
     * for tools to redirect the client on complete.
     *
     * Do not change learnerId to Integer (to match the other calls) as all the tools expect this to be a Long.
     *
     * @param toolSessionId
     *            , session ID for completed tool
     * @param learnerId
     *            the learner who is completing the tool session.
     * @return the URL for the next activity
     * @throws LearnerServiceException
     *             in case of problems.
     */
    String completeToolSession(Long toolSessionId, Long learnerId);

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
     * Complete the activity in the progress engine and delegate to the progress engine to calculate the next activity
     * in the learning design. It is currently triggered by various progress engine related action classes, which then
     * calculate the url to go to next, based on the ActivityMapping class.
     *
     * @param learnerId
     *            the learner who are running this activity in the design.
     * @param activity
     *            the activity is being run.
     * @return the updated learner progress
     */
    void completeActivity(Integer learnerId, Activity activity, Long progressID);

    /**
     * Complete the activity in the progress engine and delegate to the progress engine to calculate the next activity
     * in the learning design. It is currently triggered by various progress engine related action classes, which then
     * calculate the url to go to next, based on the ActivityMapping class.
     *
     * @param learnerId
     *            the learner who are running this activity in the design.
     * @param activity
     *            the activity is being run.
     * @return the updated learner progress
     */
    String completeActivity(LearnerProgress progress, Activity currentActivity, Integer learnerId,
	    boolean redirect) throws UnsupportedEncodingException;

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
     * @param key 
     * 		  additional information provided by user to open gate, for example password
     * @return Updated gate details
     */
    GateActivityDTO knockGate(GateActivity gateActivity, User knocker, boolean forceGate, Object key);

    Set<Group> getGroupsForGate(GateActivity gate);

    /**
     * Get the lesson for this activity. If the activity is not part of a lesson (ie is from an authoring design then it
     * will return null.
     */
    Lesson getLessonByActivity(Activity activity);

    boolean isKumaliveDisabledForOrganisation(Integer organisationId);

    ActivityPositionDTO getActivityPositionByToolSessionId(Long toolSessionId);

    void createCommandForLearner(Long lessonId, String userName, String jsonCommand);

    void createCommandForLearners(Long toolContentId, Collection<Integer> userIds, String jsonCommand);
}