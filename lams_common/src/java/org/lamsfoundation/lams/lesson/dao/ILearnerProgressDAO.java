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

package org.lamsfoundation.lams.lesson.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LearnerProgressArchive;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Inteface defines Lesson DAO Methods
 *
 * @author chris
 */
public interface ILearnerProgressDAO {

    /**
     * Retrieves the Lesson
     *
     * @param lessonId
     *            identifies the lesson to get
     * @return the lesson
     */
    LearnerProgress getLearnerProgress(Long learnerProgressId);

    /**
     * Retrieves the learner progress object for user in a lesson.
     *
     * @param learnerId
     *            the user who owns the learner progress data.
     * @param lessonId
     *            the lesson for which the progress data applies
     * @return the user's progress data
     */
    LearnerProgress getLearnerProgressByLearner(Integer learnerId, Long lessonId);
    
    /**
     * Retrieves the learner progress object for user in all lessons in specified organisation.
     *
     * @param learnerId
     *            the user who owns the learner progress data.
     * @param organisationId
     *            lessons belong to this organisation
     * @return the user's progress datas
     */
    List<LearnerProgress> getLearnerProgressByOrgAndLearner(Integer learnerId, Integer organisationId);

    /**
     * Saves or Updates learner progress data.
     *
     * @param learnerProgress
     *            holds the learne progress data
     */
    void saveLearnerProgress(LearnerProgress learnerProgress);

    /**
     * Deletes a LearnerProgress data <b>permanently</b>.
     *
     * @param learnerProgress
     */
    void deleteLearnerProgress(LearnerProgress learnerProgress);

    /**
     * Update learner progress data.
     *
     * @param learnerProgress
     */
    void updateLearnerProgress(LearnerProgress learnerProgress);

    /**
     * Get all the learner progress records where the current, previous or next activity is the given activity.
     *
     * @param activity
     * @return List<LearnerProgress>
     */
    List<LearnerProgress> getLearnerProgressReferringToActivity(Activity activity);

    /**
     * Get learners who most recently entered the activity.
     */
    List<User> getLearnersLatestByActivity(Long activityId, Integer limit, Integer offset);

    /**
     * Get learners who are at the given activities at the moment.
     */
    List<User> getLearnersByActivities(Long[] activityIds, Integer limit, Integer offset, boolean orderAscending);

    /**
     * Get learners who most recently finished the lesson.
     */
    List<User> getLearnersLatestCompletedForLesson(Long lessonId, Integer limit, Integer offset);

    /**
     * Get learners whose first name, last name or login match any of the tokens from search phrase. Sort by most
     * progressed first, then by name. Used by Learners tab in Monitoring interface.
     */
    List<User> getLearnersByMostProgress(Long lessonId, String searchPhrase, Integer limit, Integer offset);

    /**
     * Get learner progress records for a lesson where the progress is marked as completed.
     *
     * @param lessonId
     * @return List<LearnerProgress>
     */
    List<LearnerProgress> getCompletedLearnerProgressForLesson(Long lessonId, Integer limit, Integer offset,
	    boolean orderAscending);

    /**
     * Get all the learner progress records for a lesson.
     *
     * @param lessonId
     * @return
     */
    List<LearnerProgress> getLearnerProgressForLesson(Long lessonId);

    /**
     * Get all the learner progress records for a lesson restricted by list of these user ids.
     *
     * @param lessonId
     * @param userIds
     *            return progresses for only these users
     * @return
     */
    List<LearnerProgress> getLearnerProgressForLesson(Long lessonId, List<Integer> userIds);

    /**
     * Get all the learner progresses for a lesson list.
     *
     * @param lessonIds
     * @return
     */
    List<LearnerProgress> getLearnerProgressForLessons(List<Long> lessonIds);

    /**
     * Get all the users records where the user has attempted the given activity, but has not completed it yet. Uses the
     * progress records to determine the users.
     */
    List<User> getLearnersAttemptedActivity(Activity activity);

    /**
     * Get all the users records where the user has completed the given activity. Uses the progress records to determine
     * the users.
     */
    List<User> getLearnersCompletedActivity(Activity activity);

    /**
     * Get all the users records where the user has ever attempted the given activity. Uses the progress records to
     * determine the users.
     *
     * @param activityId
     * @return List<User>
     */
    List<User> getLearnersAttemptedOrCompletedActivity(Activity activity);

    /**
     * Count of the number of users that have attempted or completed an activity. Useful for activities that don't have
     * tool sessions.
     *
     * @param activityId
     * @return List<User>
     */
    Integer getNumUsersAttemptedActivity(Activity activity);

    /**
     * Count of the number of users that have completed an activity. Useful for activities that don't have tool
     * sessions.
     *
     * @param activityId
     * @return List<User>
     */
    Integer getNumUsersCompletedActivity(Activity activity);

    /**
     * Get number of learners whose first name, last name or login match any of the tokens from search phrase.
     */
    Integer getNumUsersByLesson(Long lessonId, String searchPhrase);
    
    /**
     * Get number of learners that attempted lessons from the given organisation.
     */
    Map<Long, Integer> getCountAttemptedUsersByOrganisationLessons(Integer organisationId);
    
    /**
     * Get number of learners that completed lessons from the given organisation.
     */
    Map<Long, Integer> getCountCompletedUsersByOrganisationLessons(Integer organisationId);

    /**
     * Get number of learners who finished the given lesson.
     */
    Integer getNumUsersCompletedLesson(Long lessonId);

    /**
     * Get number of learners who are at the given activities at the moment.
     */
    Map<Long, Integer> getNumUsersCurrentActivities(Long[] activityIds);

    /**
     * Get the last attempt ID for the given learner and lesson.
     */
    Integer getLearnerProgressArchiveMaxAttemptID(Integer userId, Long lessonId);

    /** Get the number of learners who are in a particular activity at the moment */
    Integer getNumUsersCurrentActivity(Activity activity);

    LearnerProgressArchive getLearnerProgressArchive(Long lessonId, Integer userId, Date archiveDate);
}