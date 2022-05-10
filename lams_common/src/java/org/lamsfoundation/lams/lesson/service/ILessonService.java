/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.lesson.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LessonDetailsDTO;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Access the general lesson details and access to grouping.
 *
 * A lesson has two different "lists" of learners.
 * <OL>
 * <LI>The learners who are in the learner group attached to the lesson. This is fixed when the lesson is started and is
 * a list of all the learners who could ever participate in to the lesson. This is available via lesson.getAllLearners()
 * <LI>The learners who have started the lesson. They may or may not be logged in currently, or if they are logged in
 * they may or may not be doing this lesson. This is available via getActiveLessonLearners().
 * </OL>
 *
 * There used to be a list of all the learners who were logged into a lesson. This has been removed as we do not need
 * the functionality at present. If this is required later it should be combined with the user's shared session logic
 * and will need to purge users who haven't done anything for a while - otherwise a user whose PC has crashed and then
 * never returns to a lesson will staying in the cache forever.
 *
 * @author lfoxton
 *
 */
public interface ILessonService {

    /** Get all the learners who have started the lesson. They may not be currently online. */
    List getActiveLessonLearners(Long lessonId);

    /**
     * Get the count of all the learners who have started the lesson. They may not be currently online.
     */
    Integer getCountActiveLessonLearners(Long lessonId);

    /**
     * Get learners who are part of the lesson class.
     */
    List<User> getLessonLearners(Long lessonId, String searchPhrase, Integer limit, Integer offset,
	    boolean orderAscending);

    /**
     * Maps users from an organisation with the given role to a boolean value saying whether they participate in the
     * given lesson.
     */
    Map<User, Boolean> getUsersWithLessonParticipation(Long lessonId, String role, String searchPhrase, Integer limit,
	    Integer offset, boolean orderByLastName, boolean orderAscending);

    /**
     * Get the count of all the learners who are a part of the lesson class.
     */
    Integer getCountLessonLearners(Long lessonId, String searchPhrase);

    /**
     * Get the lesson details for the LAMS client. Suitable for the monitoring client. Contains a count of the total
     * number of learners in the lesson and the number of active learners. This is a pretty intensive call as it counts
     * all the learners in the lessons' learner group, and determines the number of active learners.
     *
     * @param lessonId
     * @return lesson details
     */
    LessonDetailsDTO getLessonDetails(Long lessonId);

    /**
     * Get the lesson object.
     *
     * @param lessonId
     * @return lesson details
     */
    Lesson getLesson(Long lessonId);

    Lesson getLessonByToolContentId(long toolContentId);

    /**
     * If the supplied learner is not already in a group, then perform grouping for the learners who have started the
     * lesson, based on the grouping activity. Currently used for random grouping. This method should be used when we do
     * have an grouping activity and learner that is already part of the Hibernate session. (e.g. from the
     * ForceComplete)
     *
     * @param lessonId
     *            lesson id (mandatory)
     * @param groupingActivity
     *            the activity that has create grouping. (mandatory)
     * @param learner
     *            the learner to be check before grouping. (mandatory)
     */
    void performGrouping(Long lessonId, GroupingActivity groupingActivity, User learner) throws LessonServiceException;

    /**
     * Perform the grouping, setting the given list of learners as one group.
     *
     * @param groupingActivity
     *            the activity that has create grouping. (mandatory)
     * @param groupName
     *            (optional)
     * @param learners
     *            to form one group (mandatory)
     */
    void performGrouping(GroupingActivity groupingActivity, String groupName, List learners)
	    throws LessonServiceException;

    /**
     * Perform the grouping, setting the given list of learners as one group. Used in suitations where there is a
     * grouping but no grouping activity (e.g. in branching).
     *
     * @param grouping
     *            the object on which to perform the grouing. (mandatory)
     * @param groupName
     *            (optional)
     * @param learners
     *            to form one group (mandatory)
     */
    void performGrouping(Grouping grouping, String groupName, List learners) throws LessonServiceException;

    /**
     * Perform grouping for all the learners who have started the lesson, based on the grouping. Currently used for
     * chosen grouping and branching
     *
     * @param lessonId
     *            lesson id (mandatory)
     * @param groupId
     *            group id (mandatory)
     * @param grouping
     *            the object on which to perform the grouing. (mandatory)
     */
    void performGrouping(Grouping grouping, Long groupId, List learners) throws LessonServiceException;

    /**
     * Perform grouping for the given learner.
     *
     * @param grouping
     *            the object on which to perform the grouing. (mandatory)
     * @param groupId
     *            group id (mandatory)
     * @param learner
     *            learner to group (mandatory)
     * @throws LessonServiceException
     */
    void performGrouping(Grouping grouping, Long groupId, User learner) throws LessonServiceException;

    /**
     * Remove learners from the given group.
     *
     * @param grouping
     *            the grouping from which to remove the learners (mandatory)
     * @param groupName
     *            if not null only remove user from this group, if null remove learner from any group.
     * @param learners
     *            the learners to be removed (mandatory)
     */
    void removeLearnersFromGroup(Grouping grouping, Long groupId, List<User> learners) throws LessonServiceException;

    /**
     * Remove all the learners from the given grouping but leave the groups.
     *
     * @param grouping
     *            the grouping from which to remove the learners (mandatory)
     */
    void removeAllLearnersFromGrouping(Grouping grouping) throws LessonServiceException;

    /**
     * Create an empty group for the given grouping. If the group name already exists then it will force the name to be
     * unique.
     *
     * @param grouping
     *            the grouping. (mandatory)
     * @param groupName
     *            (mandatory)
     * @return the new group
     */
    Group createGroup(Grouping grouping, String name) throws LessonServiceException;

    /**
     * Remove a group for the given grouping. If the group is already used (e.g. a tool session exists) then it throws a
     * GroupingException.
     *
     * @param grouping
     *            the grouping that contains the group to remove. (mandatory)
     * @param groupName
     *            (mandatory)
     */
    void removeGroup(Grouping grouping, Long groupId) throws LessonServiceException;

    /**
     * Add a learner to the lesson class. Checks for duplicates.
     *
     * @paran userId new learner id
     * @return true if added user, returns false if the user already a learner and hence not added.
     */
    boolean addLearner(Long lessonId, Integer userId) throws LessonServiceException;

    /**
     * Add a set of learners to the lesson class.
     *
     * If version of the method is designed to be called from Moodle or some other external system, and is less
     * efficient in that it has to look up the user from the user id. If we don't do this, then we may get a a session
     * closed issue if this code is called from the LoginRequestValve (as the users will be from a previous session)
     *
     * @param lessonId
     *            new learner id
     * @param userIds
     *            array of new learner ids
     */
    void addLearners(Long lessonId, Integer[] userIds) throws LessonServiceException;

    /**
     * Add a set of learners to the lesson class. To be called within LAMS - see addLearners(Long lessonId, Integer[]
     * userIds) if calling from an external system.
     *
     * @param lesson
     *            lesson
     * @param users
     *            the users to add as learners
     */
    void addLearners(Lesson lesson, Collection<User> users) throws LessonServiceException;

    /**
     * Removes the learner from the lesson.
     */
    boolean removeLearner(Long lessonId, Integer userId);

    /**
     * Set the learners in a lesson class. Learners not in the users collection will be removed. To be called within
     * LAMS.
     *
     * @param lesson
     *            lesson
     * @param users
     *            the users to set as staff
     */
    void updateLearners(Lesson lesson, Collection<User> users) throws LessonServiceException;

    /**
     * Add a new staff member to the lesson class. Checks for duplicates.
     *
     * @paran userId new learner id
     * @return true if added user, returns false if the user already a staff member and hence not added.
     */
    boolean addStaffMember(Long lessonId, Integer userId) throws LessonServiceException;

    /**
     * Add a set of staff to the lesson class.
     *
     * If version of the method is designed to be called from Moodle or some other external system, and is less
     * efficient in that it has to look up the user from the user id. If we don't do this, then we may get a a session
     * closed issue if this code is called from the LoginRequestValve (as the users will be from a previous session)
     *
     * @param lessonId
     * @param userIds
     *            array of new staff ids
     */
    void addStaffMembers(Long lessonId, Integer[] userIds) throws LessonServiceException;

    /**
     * Add a set of staff members to the lesson class. To be called within LAMS - see addLearners(Long lessonId,
     * Integer[] userIds) if calling from an external system.
     *
     * @param lesson
     *            lesson
     * @param users
     *            the users to add as learners
     */
    void addStaffMembers(Lesson lesson, Collection<User> users) throws LessonServiceException;

    // removes the staff member from the lesson
    boolean removeStaffMember(Long lessonId, Integer userId);

    /**
     * Set the staff members in a lesson class. Staff members not in the users collection will be removed. To be called
     * within LAMS.
     *
     * @param lesson
     *            lesson
     * @param users
     *            the users to set as staff
     */
    void updateStaffMembers(Lesson lesson, Collection<User> users) throws LessonServiceException;

    /**
     * Remove references to an activity from all learner progress entries. Used by Live Edit, to remove any references
     * to the system gates
     *
     * @param activity
     *            The activity for which learner progress references should be removed.
     */
    void removeProgressReferencesToActivity(Activity activity) throws LessonServiceException;

    /**
     * Mark any learner progresses for this lesson as not completed. Called when Live Edit ends, to ensure that if there
     * were any completed progress records, and the design was extended, then they are no longer marked as completed.
     *
     * @param lessonId
     *            The lesson for which learner progress entries should be updated.
     */
    void performMarkLessonUncompleted(Long lessonId, Long firstAddedActivityId) throws LessonServiceException;

    /**
     * Get the list of users who have attempted an activity. This is based on the progress engine records. This will
     * give the users in all tool sessions for an activity (if it is a tool activity) or it will give all the users who
     * have attempted an activity that doesn't have any tool sessions, i.e. system activities such as branching.
     */
    List<User> getLearnersAttemptedOrCompletedActivity(Activity activity) throws LessonServiceException;

    /**
     * Gets the count of the users who have attempted or completed an activity. This is based on the progress engine
     * records. This
     * will work on all activities, including ones that don't have any tool sessions, i.e. system activities such as
     * branching.
     */
    Integer getCountLearnersHaveAttemptedOrCompletedActivity(Activity activity) throws LessonServiceException;

    Integer getCountLearnersHaveAttemptedActivity(Activity activity);

    /** Gets the count of the users who are currently in an activity */
    Integer getCountLearnersInCurrentActivity(Activity activity);

    /**
     * Returns map of lessons in an organisation for a particular learner or staff user.
     *
     * @param userId
     *            user's id
     * @param orgId
     *            org's id
     * @param userRole
     *            return lessons where user is learner or monitor. or returns all lessons in case of group manager
     * @return map of lesson beans used in the index page
     */
    Map<Long, IndexLessonBean> getLessonsByOrgAndUserWithCompletedFlag(Integer userId, Integer orgId, Integer userRole);

    /**
     *
     * Returns list of lessons in a group (including sub-groups) for a particular user
     *
     * @param userId
     * @param organisationId
     * @return list of lessons
     */
    List<Lesson> getLessonsByGroupAndUser(Integer userId, Integer organisationId);

    /**
     * Return list of organisation's non-removed lessons.
     *
     * @param organisationId
     * @return list of lessons
     */
    List<Lesson> getLessonsByGroup(Integer organisationId);

    /**
     * Gets the learner's progress details for a particular lesson. Will return null if the user has not started the
     * lesson.
     *
     * @param learnerId
     *            user's id
     * @param lessonId
     *            lesson's id
     * @return learner's progress or null
     */
    LearnerProgress getUserProgressForLesson(Integer learnerId, Long lessonId);

    /**
     * Gets the progresses for learners in a particular lesson.
     *
     * @param lessonId
     *            lesson's id
     * @return learner's progress
     */
    List<LearnerProgress> getUserProgressForLesson(Long lessonId);

    /**
     * Gets list of lessons which are originally based on the given learning design id.
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
     * Check if preceding lessons have been completed and the given lesson is available to the user.
     */
    boolean checkLessonReleaseConditions(Long lessonId, Integer learnerId);

    /**
     * Find lessons which just got available after the given lesson has been completed.
     */
    Set<Lesson> getReleasedSucceedingLessons(Long completedLessonId, Integer learnerId);

    void removeLearnerProgress(Long lessonId, Integer userId);

    void saveLesson(Lesson lesson);

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
}