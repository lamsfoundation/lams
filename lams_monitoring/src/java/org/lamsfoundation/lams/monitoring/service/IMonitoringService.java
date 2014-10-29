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
package org.lamsfoundation.lams.monitoring.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Interface defines all monitoring services needed by presentation tier.
 * 
 * @author Jacky Fang 2/02/2005
 * @author Manpreet Minhas
 */
public interface IMonitoringService {
    /** Get the message service, which gives access to the I18N text */
    MessageService getMessageService();

    /**
     * Intialise lesson without creating Learning Design copy, i.e. the original LD will be used.
     */
    Lesson initializeLessonWithoutLDcopy(String lessonName, String lessonDescription, long learningDesignID, User user,
	    String customCSV, Boolean enableLessonIntro, Boolean displayDesignImage, Boolean learnerExportAvailable,
	    Boolean learnerPresenceAvailable, Boolean learnerImAvailable, Boolean liveEditEnabled,
	    Boolean enableLessonNotifications, Boolean learnerRestart, Integer scheduledNumberDaysToLessonFinish,
	    Lesson precedingLesson);

    /**
     * <p>
     * Create new lesson according to the learning design specified by the user. This involves following major steps:
     * </P>
     * 
     * <li>1. Make a runtime copy of static learning design defined in authoring</li> <li>2. Go through all the tool
     * activities defined in the learning design, create a runtime copy of all tool's content.</li>
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
     * @param learnerExportAvailable
     *            should the export portfolio option be made available to the learner?
     * @param enableNotifications
     *            enable "email notifications" link for the current lesson
     * @param numberDaysToLessonFinish
     *            number of days the lesson will be available to user since he starts it. (lesson finish scheduling
     *            feature)
     * @return the lesson initialized.
     */
    Lesson initializeLesson(String lessonName, String lessonDescription, long learningDesignId, Integer organisationId,
	    Integer userID, String customCSV, Boolean enableLessonIntro, Boolean displayDesignImage,
	    Boolean learnerExportAvailable, Boolean learnerPresenceAvailable, Boolean learnerImAvailable,
	    Boolean liveEditEnabled, Boolean enableNotifications, Boolean learnerRestart,
	    Integer numberDaysToLessonFinish, Long precedingLessonId);

    /**
     * Initialize a new lesson so as to start the learning process for a normal or preview learning session. It needs to
     * notify lams which learning design it belongs to. The initialize process doesn't involve the setup of lesson class
     * and organization.
     * 
     * @param creatorUserId
     *            the user who want to create this lesson.
     * @param lessonPacket
     *            The WDDX packet containing the required initialisation paramaters
     * @return WDDX message packet containing the Lesson ID
     * @throws Exception
     */
    String initializeLesson(Integer creatorUserId, String lessonPacket) throws Exception;

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
     * @return the lesson with lesson class and organization
     */
    Lesson createLessonClassForLesson(long lessonId, Organisation organisation, String learnerGroupName,
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
     * <p>
     * Runs the system scheduler to start the scheduling for opening gate and closing gate. It invlovs a couple of steps
     * to start the scheduler:
     * </p>
     * <li>1. Initialize the resource needed by scheduling job by setting them into the job data map.</li> <li>2. Create
     * customized triggers for the scheduling.</li> <li>3. start the scheduling job</li>
     * 
     * @param scheduleGate
     *            the gate that needs to be scheduled.
     * @param schedulingStartTime
     *            the time on which the gate open should be based if an offset is used. For starting a lesson, this is
     *            the lessonStartTime. For live edit, it is now.
     * @param lessonName
     *            the name lesson incorporating this gate - used for the description of the Quartz job. Optional.
     * @returns An updated gate, that should be saved by the calling code.
     */
    ScheduleGateActivity runGateScheduler(ScheduleGateActivity scheduleGate, Date schedulingStartTime, String lessonName);

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
     * Finish a lesson on scheduled datetime.
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
     * Finish a lesson.A Finished lesson can be viewed on the monitoring interface. It should be an "inactive" lesson. A
     * Finished lesson is listed on the learner interface but all the learner can do is view the progress bar and do an
     * export portfolio - they cannot access any of the tool screens.
     * 
     * @param lessonId
     * @param userId
     *            checks that the user is a staff member for this lesson
     * @param endDate
     *            teh lesson end date and time.
     */
    void finishLesson(long lessonId, Integer userId) throws UserAccessDeniedException;

    /**
     * Set whether or not the export portfolio button is available in learner. Sets it to FALSE if
     * learnerExportAvailable is null. Checks that the user is a staff member of this lesson before updating.
     * 
     * @param lessonId
     * @param userId
     * @param learnerExportAvailable
     * @return new value for learnerExportAvailable. Normally will be same as input parameter, will only be different if
     *         the value cannot be updated for some reason.
     */
    Boolean setLearnerPortfolioAvailable(long lessonId, Integer userId, Boolean learnerExportAvailable);

    /**
     * Set whether or not the learner presence button is available in monitor. Sets it to FALSE if
     * learnerExportAvailable is null. Checks that the user is a staff member of this lesson before updating.
     * 
     * @param lessonId
     * @param userId
     * @param learnerPresenceAvailable
     * @return new value for learnerPresenceAvailable. Normally will be same as input parameter, will only be different
     *         if the value cannot be updated for some reason.
     */
    Boolean setPresenceAvailable(long lessonId, Integer userId, Boolean learnerPresenceAvailable);

    /**
     * Set whether or not the learner IM button is available in monitor. Sets it to FALSE if learnerExportAvailable is
     * null. Checks that the user is a staff member of this lesson before updating.
     * 
     * @param lessonId
     * @param userId
     * @param learnerImPresenceAvailable
     * @return new value for learnerPresenceImAvailable. Normally will be same as input parameter, will only be
     *         different if the value cannot be updated for some reason.
     */
    Boolean setPresenceImAvailable(long lessonId, Integer userId, Boolean learnerPresenceImAvailable);

    /**
     * Set whether or not the live edit is available in monitor. Sets it to FALSE if learnerExportAvailable is null.
     * Checks that the user is a staff member of this lesson before updating.
     * 
     * @param lessonId
     * @param userId
     * @param liveEditEnabled
     * @return new value for liveEditEnabled. Normally will be same as input parameter, will only be different if the
     *         value cannot be updated for some reason.
     */
    Boolean setLiveEditEnabled(long lessonId, Integer userId, Boolean liveEditEnabled);

    String forceCompleteActivitiesByUser(Integer learnerId, Integer requesterId, long lessonId, Long activityId,
	    boolean removeLearnerContent);

    /**
     * Archive the specified lesson. When archived, the data is retained but the learners cannot access the details.
     * 
     * @param lessonId
     *            the specified the lesson id.
     * @param userId
     *            checks that the user is a staff member for this lesson
     */
    void archiveLesson(long lessonId, Integer userId) throws UserAccessDeniedException;

    /**
     * Unarchive the specified the lesson. Reverts back to its previous state.
     * 
     * @param lessonId
     *            the specified the lesson id.
     */
    void unarchiveLesson(long lessonId, Integer userId);

    /**
     * A lesson can only be suspended if it is started. The purpose of suspending is to hide the lesson from learners
     * temporarily.
     * 
     * @param lessonId
     *            the lesson ID which will be suspended.
     * @param userId
     *            checks that the user is a staff member for this lesson
     */
    void suspendLesson(long lessonId, Integer userId) throws UserAccessDeniedException;

    /**
     * Unsuspend a lesson, which state must be Lesson.SUSPEND_STATE. Returns the lesson back to its previous state.
     * Otherwise an exception will be thrown.
     * 
     * @param lessonId
     * @param userId
     *            checks that the user is a staff member for this lesson
     */
    void unsuspendLesson(long lessonId, Integer userId) throws UserAccessDeniedException;

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
    void removeLesson(long lessonId, Integer userId) throws UserAccessDeniedException;

    /**
     * Set the gate to open to let all the learners through. This learning service is triggerred by the system
     * scheduler. Will return true GateActivity (or subclass) object, rather than a hibernate proxy. This is needed so
     * that the class can be returned to the web layer for proper handling.
     * 
     * @param gate
     *            the id of the gate we need to open.
     */
    GateActivity openGate(Long gateId);

    /**
     * Allows a single learner to pass the gate.
     * 
     * @param gateId
     * @param userId
     * @return
     */
    GateActivity openGateForSingleUser(Long gateId, Integer userId);

    /**
     * Set the gate to closed.
     * 
     * @param gate
     *            the id of the gate we need to close.
     */
    GateActivity closeGate(Long gateId);

    /**
     * Returns a list of learners participating in the given Lesson
     * 
     * @param lessonID
     *            The lesson_id of the Lesson
     * @param userID
     *            The user id of the user requesting the lesson learners
     * @return String The requested list in wddx format
     * 
     * @throws IOException
     */
    String getLessonLearners(Long lessonID, Integer userID) throws IOException;

    /**
     * Returns a list of staff participating in the given Lesson
     * 
     * @param lessonID
     *            The lesson_id of the Lesson
     * @param userID
     *            The user id of the user requesting the lesson staff members
     * @return String The requested list in wddx format
     * 
     * @throws IOException
     */
    String getLessonStaff(Long lessonID, Integer userID) throws IOException;

    /**
     * Returns users by search type criteria. It's sorted by first and last user names.
     * 
     * @param searchType
     *            one of 11 constants from <code>MonitoringConstants</code> defining search type
     * @param lessonId
     * @param lessonIds
     * @param activityId
     * @param xDaystoFinish
     * @param orgId
     * @return
     */
    Collection<User> getUsersByEmailNotificationSearchType(int searchType, Long lessonId, String[] lessonIds,
	    Long activityId, Integer xDaystoFinish, Integer orgId);

    /**
     * This method returns the LearningDesign details for a given Lesson
     * 
     * @param lessonID
     *            The lesson_id of the Lesson whose LearningDesign details are required
     * @return String The requested details in wddx format
     * @throws IOException
     */
    String getLearningDesignDetails(Long lessonID) throws IOException;

    List<ContributeActivityDTO> getAllContributeActivityDTO(Long lessonID);

    /**
     * This method returns the url associated with the activity in the monitoring enviornment. This is the URL that
     * opens up when the user/teacher clicks on the activity in the monitoring enviornment and then selects a learner OR
     * in the LEARNER tab when a learner's activity is clicked.
     * 
     * This is also known as the learner progress url.
     * 
     * @param lessonID
     *            The lesson_id of the Lesson for which the information has to be fetched.
     * @param activityID
     *            The activity_id of the activity for which the URL is required
     * @param learnerID
     *            The user_id of the Learner for whom the URL is being fetched
     * @param requesterID
     *            The user_id of the user who is requesting the url
     * @return String The required information in WDDX format
     * @throws IOException
     * @throws LamsToolServiceException
     */
    String getLearnerActivityURL(Long lessonID, Long activityID, Integer learnerUserID, Integer requestingUserId)
	    throws IOException, LamsToolServiceException;

    /**
     * This method returns the monitor url for the given activity
     * 
     * @param lessonID
     *            The lesson_id of the Lesson for which the information has to be fetched.
     * @param activityID
     *            The activity_id of the Activity whose URL will be returned
     * @param userID
     *            The user id of the user requesting the url.
     * @return String The required information in WDDX format
     * @throws IOException
     */
    String getActivityMonitorURL(Long lessonID, Long activityID, String contentFolderID, Integer userID)
	    throws IOException, LamsToolServiceException;

    /**
     * This method moves the learning design corresponding to the given Lesson into the specified workspaceFolder. But
     * before this action is performed it checks whether the user is authorized to do so. If not, Flash is notified of
     * the same. As of now it is assumed that only the owner of lesson/learning design can move it
     * 
     * @param lessonID
     *            The lesson_id of the Lesson which has to be moved
     * @param targetWorkspaceFolderID
     *            The workspace_folder_id of the WorkspaceFolder to which the lesson has to be moved
     * @param userID
     *            The user_id of the User who has requested this operation
     * @return String The acknowledgement message/error in WDDX format
     * @throws IOException
     */
    String moveLesson(Long lessonID, Integer targetWorkspaceFolderID, Integer userID) throws IOException;

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
    Activity getActivityById(Long activityId, Class clasz);

    /**
     * Return an activity object based on the requested id.
     * 
     * @param activityId
     *            id of the activity.
     * @return the requested activity object.
     */
    GroupingActivity getGroupingActivityById(Long activityID);

    /**
     * Returns an acknowledgement that the gate has been released. Returns true if the gate has been released and false
     * otherwise. This information is packaged in WDDX format
     * 
     * @param activityID
     *            The activity_id of the Activity whose gate must be checked
     * @param lessonID
     *            The lesson_id of the Lesson
     * @return
     */
    String releaseGate(Long activityID) throws IOException;

    /**
     * Perform chosen grouping. The groups contains a list of Hashtable which contain following information for each
     * group:<br>
     * <ol>
     * <li>List of learners in this group</li>
     * <li>Order ID</li>
     * <li>Group name</li>
     * </ol>
     * 
     * @param groupingActivity
     * @param groups
     *            list of group information.
     */
    void performChosenGrouping(GroupingActivity groupingActivity, List groups) throws LessonServiceException;

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
     * @return Lesson
     */
    abstract Lesson createPreviewClassForLesson(int userID, long lessonID) throws UserAccessDeniedException;

    /* Supports the Chosen Groupings and Branching */
    /**
     * Get all the active learners in the lesson who are not in a group or in a branch.
     * 
     * If the activity is a grouping activity, then set useCreatingGrouping = true to base the list on the create
     * grouping. Otherwise leave it false and it will use the grouping applied to the activity - this is used for
     * branching activities.
     * 
     * @param activityID
     * @param lessonID
     * @param useCreateGrouping
     *            true/false for GroupingActivities, always false for non-GroupingActivities
     * @return Sorted set of Users, sorted by surname
     */
    SortedSet<User> getClassMembersNotGrouped(Long lessonID, Long activityID, boolean useCreateGrouping);

    /**
     * Add a new group to a grouping activity. If name already exists or the name is blank, does not add a new group. If
     * the activity is a grouping activity, then set useCreatingGrouping = true to base the list on the create grouping.
     * Otherwise leave it false and it will use the grouping applied to the activity - this is used for branching
     * activities.
     * 
     * If it is a teacher chosen branching activity and the grouping doesn't exist, it creates one.
     * 
     * @param activityID
     *            id of the grouping activity
     * @param name
     *            group name
     * @throws LessonServiceException
     *             , MonitoringServiceException
     */
    abstract Group addGroup(Long activityID, String name, boolean overrideMaxNumberOfGroups)
	    throws LessonServiceException, MonitoringServiceException;

    /**
     * Remove a group to from a grouping activity. If the group does not exists then nothing happens. If the group is
     * already used (e.g. a tool session exists) then it throws a LessonServiceException.
     * 
     * If the activity is a grouping activity, then set useCreatingGrouping = true to base the list on the create
     * grouping. Otherwise leave it false and it will use the grouping applied to the activity - this is used for
     * branching activities.
     * 
     * If it is a teacher chosen branching activity and the grouping doesn't exist, it creates one.
     * 
     * @param activityID
     *            id of the grouping activity
     * @param name
     *            group name
     * @throws LessonServiceException
     **/
    abstract void removeGroup(Long activityID, Long groupID) throws LessonServiceException;

    /**
     * Add learners to a group. Doesn't necessarily check if the user is already in another group.
     */
    abstract void addUsersToGroup(Long activityID, Long groupID, String learnerIDs[]) throws LessonServiceException;

    /**
     * Remove a user to a group. If the user is not in the group, then nothing is changed.
     * 
     * @throws LessonServiceException
     */
    abstract void removeUsersFromGroup(Long activityID, Long groupID, String learnerIDs[])
	    throws LessonServiceException;

    /**
     * Add learners to a branch. Doesn't necessarily check if the user is already in another branch. Assumes there
     * should only be one group for this branch. Use for Teacher Chosen Branching. Don't use for Group Based Branching
     * as there could be more than one group for the branch.
     * 
     * @param sequenceActivityID
     *            Activity id of the sequenceActivity representing this branch
     * @param learnerIDs
     *            the IDS of the learners to be added.
     */
    void addUsersToBranch(Long sequenceActivityID, String learnerIDs[]) throws LessonServiceException;

    /**
     * Remove learners from a branch. Assumes there should only be one group for this branch. Use for Teacher Chosen
     * Branching. Don't use for Group Based Branching as there could be more than one group for the branch.
     * 
     * @param sequenceActivityID
     *            Activity id of the sequenceActivity representing this branch
     * @param learnerIDs
     *            the IDS of the learners to be added.
     */
    void removeUsersFromBranch(Long sequenceActivityID, String learnerIDs[]) throws LessonServiceException;

    /**
     * Has anyone started this branch / branching activity ? Irrespective of the groups. Used to determine if a branch
     * mapping can be removed.
     */
    boolean isActivityAttempted(Activity activity) throws LessonServiceException;

    /**
     * Match group(s) to a branch. Doesn't necessarily check if the group is already assigned to another branch. Use for
     * Group Based Branching.
     * 
     * @param sequenceActivityID
     *            Activity id of the sequenceActivity representing this branch
     * @param learnerIDs
     *            the IDS of the learners to be added.
     */
    void addGroupToBranch(Long sequenceActivityID, String groupIDs[]) throws LessonServiceException;

    /**
     * Remove group / branch mapping. Cannot be done if any users in the group have started the branch. Used for group
     * based branching in define later.
     * 
     * @param sequenceActivityID
     *            Activity id of the sequenceActivity representing this branch
     * @param learnerIDs
     *            the IDS of the learners to be added.
     */
    void removeGroupFromBranch(Long sequenceActivityID, String groupIDs[]) throws LessonServiceException;

    /**
     * Get all the groups that exist for the related grouping activity that have not been allocated to a branch.
     * 
     * @param branchingActivityID
     *            Activity id of the branchingActivity
     */
    SortedSet<Group> getGroupsNotAssignedToBranch(Long branchingActivityID) throws LessonServiceException;

    /**
     * Get the list of users who have attempted an activity. This is based on the progress engine records. This will
     * give the users in all tool sessions for an activity (if it is a tool activity) or it will give all the users who
     * have attempted an activity that doesn't have any tool sessions, i.e. system activities such as branching.
     */
    List<User> getLearnersHaveAttemptedActivity(Activity activity) throws LessonServiceException;

    /** Get the record of the learner's progress for a particular lesson */
    LearnerProgress getLearnerProgress(Integer learnerId, Long lessonId);

    /**
     * Set a groups name
     */
    void setGroupName(Long groupID, String name);

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
}
