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

package org.lamsfoundation.lams.monitoring.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;

/**
 * Interface defines all monitoring services needed by presentation tier.
 *
 * @author Jacky Fang 2/02/2005
 * @author Manpreet Minhas
 */
public interface IMonitoringService {

    /**
     * Intialise lesson without creating Learning Design copy, i.e. the original LD will be used.
     */
    Lesson initializeLessonWithoutLDcopy(String lessonName, String lessonDescription, long learningDesignID, User user,
	    String customCSV, Boolean enableLessonIntro, Boolean displayDesignImage, Boolean learnerPresenceAvailable,
	    Boolean learnerImAvailable, Boolean liveEditEnabled, Boolean enableLessonNotifications,
	    Boolean forceLearnerRestart, Boolean allowLearnerRestart, Boolean gradebookOnComplete,
	    Integer scheduledNumberDaysToLessonFinish, Lesson precedingLesson);

    /**
     * <p>
     * Create new lesson according to the learning design specified by the user. This involves following major steps:
     * </P>
     *
     * <li>1. Make a runtime copy of static learning design defined in authoring</li>
     * <li>2. Go through all the tool activities defined in the learning design, create a runtime copy of all tool's
     * content.</li>
     *
     * <P>
     * As a runtime design, it is not copied into any folder.
     * </P>
     *
     * <p>
     * The initialization process doesn't involve the setup of lesson class and organization.
     * </p>
     *
     * @param lessonName
     *            the name of the lesson
     * @param lessonDescription
     *            the description of the lesson.
     * @param learningDesignId
     *            the selected learning design
     * @param organisationId
     *            the copied sequence will be put in the default runtime sequence folder for this org, if such a folder
     *            exists.
     * @param userId
     *            the user who want to create this lesson.
     * @param customCSV
     *            the custom comma separated values to be used by toolAdapters
     * @param enableLessonIntro
     *            display an intro page to the lesson
     * @param displayDesignImage
     *            display the design image or not
     * @param enableNotifications
     *            enable "email notifications" link for the current lesson
     * @param numberDaysToLessonFinish
     *            number of days the lesson will be available to user since he starts it. (lesson finish scheduling
     *            feature)
     * @return the lesson initialized.
     */
    Lesson initializeLesson(String lessonName, String lessonDescription, long learningDesignId, Integer organisationId,
	    Integer userID, String customCSV, Boolean enableLessonIntro, Boolean displayDesignImage,
	    Boolean learnerPresenceAvailable, Boolean learnerImAvailable, Boolean liveEditEnabled,
	    Boolean enableNotifications, Boolean forceLearnerRestart, Boolean allowLearnerRestart,
	    Boolean gradebookOnComplete, Integer numberDaysToLessonFinish, Long precedingLessonId);

    /**
     * Create new lesson according to the learning design specified by the user, but for a preview session rather than a
     * normal learning session. The design is not assigned to any workspace folder.
     */
    Lesson initializeLessonForPreview(String lessonName, String lessonDescription, long learningDesignId,
	    Integer userID, String customCSV, Boolean learnerPresenceAvailable, Boolean learnerImAvailable,
	    Boolean liveEditEnabled);

    /**
     * Setup the lesson class and organization for a lesson according to the input from monitoring GUI interface.
     *
     * <p>
     * Pre-condition: This method must be called under the condition of the the new lesson exists (without lesson
     * class).
     * </p>
     * <p>
     * A lesson class record should be inserted and organization should be setup after execution of this service.
     * </p>
     *
     * @param lessonId
     *            the lesson without lesson class and organization
     * @param organisation
     *            the organization this lesson belongs to.
     * @param learnerGroupName
     *            name of learner group
     * @param organizationUsers
     *            a list of learner will be in this new lessons.
     * @param staffGroupName
     *            name of staff group
     * @param staffs
     *            a list of staffs who will be in charge of this lesson.
     */
    void createLessonClassForLesson(long lessonId, Organisation organisation, String learnerGroupName,
	    List<User> organizationUsers, String staffGroupName, List<User> staffs, Integer userID)
	    throws UserAccessDeniedException;

    /**
     * Start the specified the lesson. It must be created before calling this service.
     *
     * @param lessonId
     *            the specified the lesson id.
     * @param userId
     *            checks that the user is a staff member for this lesson
     * @throws LamsToolServiceException
     *             the exception occurred during the lams and tool interaction to start a lesson.
     */
    void startLesson(long lessonId, Integer userId) throws UserAccessDeniedException;

    /**
     * Do any normal initialisation needed for gates and branching. Done both when a lesson is started, or for new
     * activities added during a Live Edit. Returns a new MaxID for the design if needed. If MaxID is returned, update
     * the design with this new value and save the whole design (as initialiseSystemActivities has changed the design).
     */
    Integer startSystemActivity(Activity activity, Integer currentMaxId, Date lessonStartTime, String lessonName);

    /**
     * Start a lesson on scheduled datetime.
     *
     * @param lessonId
     * @param startDate
     *            the lesson start date and time.
     * @param userId
     *            checks that the user is a staff member for this lesson
     * @see org.lamsfoundation.lams.monitoring.service#startLesson(long)
     */
    void startLessonOnSchedule(long lessonId, Date startDate, Integer userId) throws UserAccessDeniedException;

    /**
     * Finish a lesson on scheduled datetime based on days in the lesson runs for
     *
     * @param lessonId
     * @param endDate
     *            number of days since lesson start when the lesson should be closed.
     * @param userId
     *            checks that the user is a staff member for this lesson
     */
    void finishLessonOnSchedule(long lessonId, int scheduledNumberDaysToLessonFinish, Integer userId)
	    throws UserAccessDeniedException;

    /**
     * Finish a lesson on scheduled datetime.
     *
     * @param lessonId
     * @param endDate
     *            number of days since lesson start when the lesson should be closed.
     * @param userId
     *            checks that the user is a staff member for this lesson
     */
    void finishLessonOnSchedule(long lessonId, Date endDate, Integer userId) throws UserAccessDeniedException;

    /**
     * <P>
     * Teachers sometimes find that there are just too many "old" designs and wish to remove them and never access them
     * again. This function disables the lesson - it does not remove the contents from the database
     * </P>
     *
     * @param lessonId
     *            the specified the lesson id.
     * @param userId
     *            checks that the user is a staff member for this lesson
     */
    void removeLesson(long lessonId, Integer userId) throws SecurityException;

    /**
     * Removes the lesson and all referenced resources (learning design, tool content etc.) from the database.
     */
    void removeLessonPermanently(long lessonId, Integer userId) throws SecurityException;

    /**
     * Return an activity object based on the requested id.
     *
     * @param activityId
     *            id of the activity.
     * @return the requested activity object.
     */
    Activity getActivityById(Long activityId);

    /**
     * Return an activity object based on the requested id. Where possible, give it the type we want so that it can be
     * cast properly.
     *
     * @param activityId
     *            id of the activity.
     * @return the requested activity object.
     */
    @SuppressWarnings("rawtypes")
    Activity getActivityById(Long activityId, Class clasz);

    List<GroupingActivity> getGroupingActivitiesByLearningDesignId(Long learningDesignId);

    /**
     * Return an activity object based on the requested id.
     *
     * @param activityId
     *            id of the activity.
     * @return the requested activity object.
     */
    GroupingActivity getGroupingActivityById(Long activityID);

    // ---------------------------------------------------------------------
    // Preview Methods
    // ---------------------------------------------------------------------
    /**
     * Create the lesson class and the staff class for a preview lesson. The lesson is not attached to any organisation.
     *
     * @param userID
     *            User ID of the teacher running the preview. Mandatory.
     * @param lessonID
     *            ID of the lesson
     */
    abstract void createPreviewClassForLesson(int userID, long lessonID) throws UserAccessDeniedException;

    /**
     * Get all the users records where the user has attempted the given activity, but has not completed it yet. Uses the
     * progress records to determine the users.
     */
    List<User> getLearnersAttemptedActivity(Activity activity);

    /**
     * give the users in all tool sessions for an activity (if it is a tool activity) or it will give all the users who
     * have attempted an activity that doesn't have any tool sessions, i.e. system activities such as branching.
     */
    List<User> getLearnersAttemptedOrCompletedActivity(Activity activity) throws LessonServiceException;

    /**
     * Get learners who most recently entered finished the lesson.
     */
    List<User> getLearnersLatestCompleted(Long lessonId, Integer limit, Integer offset);

    /**
     * Get learners whose first name, last name or login match any of the tokens from search phrase. Sorts either by
     * name or, if orderByCompletion is set, by most progressed first. Used mainly by Learners tab in Monitoring
     * interface.
     */
    List<User> getLearnersByMostProgress(Long lessonId, String searchPhrase, Integer limit, Integer offset);

    /**
     * Get learners who most recently entered the activity.
     */
    List<User> getLearnersLatestByActivity(Long activityId, Integer limit, Integer offset);

    /**
     * Get learners who are at the given activities at the moment.
     */
    List<User> getLearnersByActivities(Long[] activityIds, Integer limit, Integer offset, boolean orderAscending);

    /**
     * Get number of learners whose first name, last name or login match any of the tokens from search phrase.
     */
    Integer getCountLearnersFromProgress(Long lessonId, String searchPhrase);

    /**
     * Get number of learners who are at the given activities at the moment.
     */
    Map<Long, Integer> getCountLearnersCurrentActivities(Long[] activityIds);

    /**
     * Get number of learners who finished the given lesson.
     */
    Integer getCountLearnersCompletedLesson(Long lessonId);

    /** Get Organisation by organisationId */
    Organisation getOrganisation(Integer organisationId);

    /**
     * Used in admin to clone lessons using the given lesson Ids (from another group) into the given group. Given staff
     * and learner ids should already be members of the group.
     *
     * @param lessonIds
     * @param addAllStaff
     * @param addAllLearners
     * @param staffIds
     * @param learnerIds
     * @param group
     * @return number of lessons created.
     * @throws MonitoringServiceException
     */
    int cloneLessons(String[] lessonIds, Boolean addAllStaff, Boolean addAllLearners, String[] staffIds,
	    String[] learnerIds, Organisation group) throws MonitoringServiceException;

    /**
     * Same as cloneLessons method, except it clones only one lesson.
     *
     * @param lessonId
     * @param creatorId
     * @param addAllStaff
     * @param addAllLearners
     * @param staffIds
     * @param learnerIds
     * @param group
     * @return
     * @throws MonitoringServiceException
     */
    Long cloneLesson(Long lessonId, Integer creatorId, Boolean addAllStaff, Boolean addAllLearners, String[] staffIds,
	    String[] learnerIds, Organisation group) throws MonitoringServiceException;

    void removeLearnerContent(Long lessonId, Integer learnerId);

    /**
     * Get list of users who completed the given lesson.
     */
    List<User> getUsersCompletedLesson(Long lessonId, Integer limit, Integer offset, boolean orderAscending);
}