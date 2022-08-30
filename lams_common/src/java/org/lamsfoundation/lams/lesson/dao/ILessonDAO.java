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

package org.lamsfoundation.lams.lesson.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.ActivityTimeLimitDTO;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Inteface defines Lesson DAO Methods
 *
 * @author chris
 */
public interface ILessonDAO extends IBaseDAO {

    /**
     * Retrieves the Lesson
     *
     * @param lessonId
     *            identifies the lesson to get
     * @return the lesson
     */
    Lesson getLesson(Long lessonId);

    Lesson getLessonWithJoinFetchedProgress(Long lessonId);

    /**
     * Gets all lessons that are active for a learner. TODO to be removed when the dummy interface is no longer needed
     *
     * @param learner
     *            a User that identifies the learner.
     * @return a Set with all active lessons in it.
     */
    List getActiveLessonsForLearner(User learner);

    /**
     * Saves or Updates a Lesson.
     *
     * @param lesson
     *            the Lesson to save
     */
    void saveLesson(Lesson lesson);

    /**
     * Deletes a Lesson <b>permanently</b>.
     *
     * @param lesson
     *            the Lesson to remove.
     */
    void deleteLesson(Lesson lesson);

    /**
     * Update a requested lesson.
     *
     * @param createdLesson
     */
    void updateLesson(Lesson lesson);

    /**
     * Returns the list of available Lessons created by a given user. Does not return disabled lessons or preview
     * lessons.
     *
     * @param userID
     *            The user_id of the user
     * @return List The list of Lessons for the given user
     */
    List getLessonsCreatedByUser(Integer userID);

    /**
     * Returns the all the learners that have started the requested lesson.
     *
     * @param lessonId
     *            the id of the requested lesson.
     * @return the list of learners.
     */
    List getActiveLearnerByLesson(long lessonId);

    /**
     * Returns the count of all the learners that have started the requested lesson.
     *
     * @param lessonId
     *            the id of the requested lesson.
     * @return the count of the learners.
     */
    Integer getCountActiveLearnerByLesson(long lessonId);

    /**
     * Get learners who are part of the lesson class.
     */
    List<User> getLearnersByLesson(Long lessonId, String searchPhrase, Integer limit, Integer offset,
	    boolean orderAscending);

    /**
     * Returns the count of all the learners that are a part of the lesson class.
     */
    Integer getCountLearnersByLesson(long lessonId, String searchPhrase);

    /**
     * Maps users from an organisation with the given role to a boolean value saying whether they participate in the
     * given lesson.
     */
    Map<User, Boolean> getUsersWithLessonParticipation(Long lessonId, String role, String searchPhrase, Integer limit,
	    Integer offset, boolean orderByLastName, boolean orderAscending);

    /**
     * Get all the preview lessons more with the creation date before the given date.
     *
     * @param startDate
     *            UTC date
     * @return the list of Lessons
     */
    List getPreviewLessonsBeforeDate(Date startDate);

    /**
     * Get the lesson that applies to this activity. Not all activities have an attached lesson.
     */
    Lesson getLessonForActivity(long activityId);

    /**
     * Get the lesson and activity ids that apply to the tool activity associated with this tool content id.
     * Returns an array of two longs.
     */
    Long[] getLessonActivityIdsForToolContentId(long toolContentId);

    /**
     * Gets all non-removed lessons for a user in an org; set userRole parameter to learner if you want lessons where
     * user is in the learner list, or to monitor if in the staff list.
     *
     * @param userId
     *            a user id that identifies the user.
     * @param orgId
     *            an org id that identifies the organisation.
     * @param userRole
     *            return lessons where user is learner or monitor. or returns all lessons in case of group manager
     *
     * @return a List containing a list of tuples containing lesson details and the lesson completed flag for the user.
     */
    List getLessonsByOrgAndUserWithCompletedFlag(Integer userId, Integer orgId, Integer userRole);

    /**
     * Gets all non-removed lessons for a user in a group including sub-groups
     *
     * @param userId
     *            a user id that identifies the user.
     * @param orgId
     *            an org id that identifies the organisation.
     * @return a List containing a list of tuples containing lesson details and the lesson completed flag for the user.
     */
    List getLessonsByGroupAndUser(Integer userId, Integer orgId);

    /**
     * Gets all non-removed lessons for a group.
     *
     * @param orgId
     * @return
     */
    List getLessonsByGroup(Integer orgId);

    /**
     * Get lessons based on learning designs where the original learning design has the given id.
     *
     * @param ldId
     * @param orgId
     * @return list of lessons
     */
    List getLessonsByOriginalLearningDesign(Long ldId, Integer orgId);

    /**
     * Finds out which lesson the given tool content belongs to and returns its monitoring users.
     *
     * @param sessionId
     *            tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    List<User> getMonitorsByToolSessionId(Long sessionId);

    /**
     * Finds number of preview and all lessons.
     */
    long[] getPreviewLessonCount();

    /**
     * Finds IDs of preview lessons.
     */
    List<Long> getPreviewLessons(Integer limit);

    /**
     * Finds IDs of all lessons in an organisation. When calling MonitoringService.removeLessonPermanently() you cannot
     * load the Lessons
     * or a Hibernate error occurs. So we need a way to get the ids withouth calling Organisation.getLessons()
     */
    List<Long> getOrganisationLessons(Integer organisationId);

    /**
     * Finds activities in the given lesson which have non-expired absolute time limit set up.
     */
    List<ActivityTimeLimitDTO> getRunningAbsoluteTimeLimits(long lessonId);
}