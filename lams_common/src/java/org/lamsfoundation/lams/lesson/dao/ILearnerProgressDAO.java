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
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

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
    LearnerProgress getLearnerProgressByLearner(final Integer learnerId, final Long lessonId);

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
    List getLearnerProgressReferringToActivity(final Activity activity);

    /**
     * Get all the learner progress records for a lesson where the progress is marked as completed.
     * 
     * @param lessonId
     * @return List<LearnerProgress>
     */
    List getCompletedLearnerProgressForLesson(final Long lessonId);
    
    /**
     * Get all the learner progress records for a lesson.
     * 
     * @param lessonId
     * @return
     */
    List getLearnerProgressForLesson(final Long lessonId);

    /**
     * Get all the users records where the user has attempted the given activity. Uses the progress records to determine
     * the users.
     * 
     * @param activityId
     * @return List<User>
     */
    List<User> getLearnersHaveAttemptedActivity(final Activity activity);

    /**
     * Get all the users records where the user has completed the given activity. Uses the progress records to determine
     * the users.
     * 
     * @param activityId
     * @return List<User>
     */
    List<User> getLearnersHaveCompletedActivity(final Activity activity);

    /**
     * Count of the number of users that have attempted or completed an activity. Useful for activities that don't have
     * tool sessions.
     * 
     * @param activityId
     * @return List<User>
     */
    Integer getNumUsersAttemptedActivity(final Activity activity);

    /**
     * Count of the number of users that have completed an activity. Useful for activities that don't have tool
     * sessions.
     * 
     * @param activityId
     * @return List<User>
     */
    Integer getNumUsersCompletedActivity(final Activity activity);

    /**
     * Get the count of all learner progress records for an lesson without loading the records.
     * 
     * @return Number of learner progress records for this lesson
     */
    Integer getNumAllLearnerProgress(final Long lessonId);

    /**
     * Get a batch of learner progress records (size batchSize) for an lesson, sorted by surname and the first name.
     * Start at the beginning of the table if no previousUserId is given, otherwise get the batch after lastUserId.
     * 
     * @param lessonId
     * @param lastUserId
     * @param batchSize
     * @return List<LearnerProgress>
     */
    List<LearnerProgress> getBatchLearnerProgress(final Long lessonId, final User lastUser, final int batchSize);

}
