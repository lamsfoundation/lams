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

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.lesson.Lesson;
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
    public Lesson getLesson(Long lessonId);

    /** Get all the lessons in the database. This includes the disabled lessons. */
    public List getAllLessons();

    public Lesson getLessonWithJoinFetchedProgress(Long lessonId);

    /**
     * Gets all lessons that are active for a learner. TODO to be removed when the dummy interface is no longer needed
     * 
     * @param learner
     *            a User that identifies the learner.
     * @return a Set with all active lessons in it.
     */
    public List getActiveLessonsForLearner(User learner);

    /**
     * Gets all lessons that are active for a learner, in a given organisation
     * 
     * @param learnerId
     *            a User that identifies the learner.
     * @param organisationId
     *            the desired organisation .
     * @return a List with all active lessons in it.
     */
    public List<Lesson> getActiveLessonsForLearner(Integer learnerId, Integer organisationID);

    /**
     * Saves or Updates a Lesson.
     * 
     * @param lesson
     *            the Lesson to save
     */
    public void saveLesson(Lesson lesson);

    /**
     * Deletes a Lesson <b>permanently</b>.
     * 
     * @param lesson
     *            the Lesson to remove.
     */
    public void deleteLesson(Lesson lesson);

    /**
     * Update a requested lesson.
     * 
     * @param createdLesson
     */
    public void updateLesson(Lesson lesson);

    /**
     * Returns the list of available Lessons created by a given user. Does not return disabled lessons or preview
     * lessons.
     * 
     * @param userID
     *            The user_id of the user
     * @return List The list of Lessons for the given user
     */
    public List getLessonsCreatedByUser(Integer userID);

    /**
     * Gets all lessons in the given organisation, for which this user is in the staff group. Does not return disabled
     * lessons or preview lessons. This is the list of lessons that a user may monitor/moderate/manage.
     * 
     * @param user
     *            a User that identifies the teacher/staff member.
     * @return a List with all appropriate lessons in it.
     */
    public List getLessonsForMonitoring(int userID, int organisationID);

    /**
     * Returns the all the learners that have started the requested lesson.
     * 
     * @param lessonId
     *            the id of the requested lesson.
     * @return the list of learners.
     */
    public List getActiveLearnerByLesson(long lessonId);

    /**
     * Returns the all the learners that have started the requested lesson and are in the given group.
     * 
     * @param lessonId
     *            the id of the requested lesson.
     * @param groupId
     *            the id of the requested group.
     * @return the list of learners.
     */
    public List getActiveLearnerByLessonAndGroup(long lessonId, long groupId);

    /**
     * Returns the count of all the learners that have started the requested lesson.
     * 
     * @param lessonId
     *            the id of the requested lesson.
     * @return the count of the learners.
     */
    public Integer getCountActiveLearnerByLesson(long lessonId);

    /**
     * Returns the count of all the learners that are a part of the lesson class.
     */
    public Integer getCountLearnerByLesson(long lessonId);

    /**
     * Get all the preview lessons more with the creation date before the given date.
     * 
     * @param startDate
     *            UTC date
     * @return the list of Lessons
     */
    public List getPreviewLessonsBeforeDate(Date startDate);

    /**
     * Get the lesson that applies to this activity. Not all activities have an attached lesson.
     */
    public Lesson getLessonForActivity(long activityId);

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
    public List getLessonsByOrgAndUserWithCompletedFlag(Integer userId, Integer orgId,
	    Integer userRole);

    /**
     * Gets all non-removed lessons for a user in a group including sub-groups
     * 
     * @param userId
     *            a user id that identifies the user.
     * @param orgId
     *            an org id that identifies the organisation.
     * @return a List containing a list of tuples containing lesson details and the lesson completed flag for the user.
     */
    public List getLessonsByGroupAndUser(Integer userId, Integer orgId);

    /**
     * Gets all non-removed lessons for a group.
     * 
     * @param orgId
     * @return
     */
    public List getLessonsByGroup(Integer orgId);

    /**
     * Get lessons based on learning designs where the original learning design has the given id.
     * 
     * @param ldId
     * @param orgId
     * @return list of lessons
     */
    public List getLessonsByOriginalLearningDesign(Long ldId, Integer orgId);

    /**
     * Finds out which lesson the given tool content belongs to and returns its monitoring users.
     * 
     * @param sessionId
     *            tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId);

    /**
     * Gets lesson for tools based on toolSessionID
     * 
     * @param sessionID
     * @return
     */
    public Lesson getLessonFromSessionID(Long toolSessionID);
}
