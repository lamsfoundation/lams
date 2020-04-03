package org.lamsfoundation.lams.monitoring.service;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.lamsfoundation.lams.events.EmailNotificationArchive;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.lamsfoundation.lams.util.excel.ExcelSheet;

/**
 * Contains methods intended for internal usage by lams_monitoring.
 * 
 * @author Andrey Balan
 */
public interface IMonitoringFullService extends IMonitoringService {
    
    /** Get the message service, which gives access to the I18N text */
    MessageService getMessageService();

    /**
     * Set whether or not the learner presence button is available in monitor. Checks that the user is a staff member of
     * this lesson before updating.
     *
     * @param lessonId
     * @param userId
     * @param learnerPresenceAvailable
     * @return new value for learnerPresenceAvailable. Normally will be same as input parameter, will only be different
     *         if the value cannot be updated for some reason.
     */
    Boolean togglePresenceAvailable(long lessonId, Integer userId, Boolean learnerPresenceAvailable);

    /**
     * Set whether or not the learner IM button is available in monitor. Checks that the user is a staff member of this
     * lesson before updating.
     *
     * @param lessonId
     * @param userId
     * @param learnerImPresenceAvailable
     * @return new value for learnerPresenceImAvailable. Normally will be same as input parameter, will only be
     *         different if the value cannot be updated for some reason.
     */
    Boolean togglePresenceImAvailable(long lessonId, Integer userId, Boolean learnerPresenceImAvailable);

    /** Set whether or not to display the gradebook activity scores at the end of a lesson */
    Boolean toggleGradebookOnComplete(long lessonId, Integer userId, Boolean gradebookOnComplete);
    
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
     * Suspend lesson now! A lesson can only be suspended if it is started. The purpose of suspending is to hide the lesson from learners
     * temporarily.
     *
     * @param lessonId
     *            the lesson ID which will be suspended.
     * @param userId
     *            checks that the user is a staff member for this lesson
     * @param clearScheduleDetails
     * 		  should it remove any triggers set up to suspend the lesson and clear the schedule date field. true if user suspending right now,
     * 		  false if this is being called by the trigger
     */
    void suspendLesson(long lessonId, Integer userId, boolean removeTriggers) throws UserAccessDeniedException;
    
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
     * Set the gate to open to let all the learners through. This learning service is triggerred by the system
     * scheduler. Will return true GateActivity (or subclass) object, rather than a hibernate proxy. This is needed so
     * that the class can be returned to the web layer for proper handling.
     *
     * @param gate
     *            the id of the gate we need to open.
     */
    GateActivity openGate(Long gateId, Integer openerId);

    /**
     * Allows a single learner to pass the gate.
     *
     * @param gateId
     * @param userId
     * @return
     */
    GateActivity openGateForSingleUser(Long gateId, Integer[] userIds);

    /**
     * Set the gate to closed.
     *
     * @param gate
     *            the id of the gate we need to close.
     */
    GateActivity closeGate(Long gateId);

    /** Update the schedule gate date/time */
    GateActivity scheduleGate(Long gateId, Date schedulingDatetime, Integer userId);
    
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
     * @throws IOException
     */
    String getActivityMonitorURL(Long lessonID, Long activityID, String contentFolderID, Integer userID)
	    throws IOException, LamsToolServiceException;

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
     * Add learners to a group based on their logins. Doesn't necessarily check if the user is already in another group.
     */
    abstract int addUsersToGroupByLogins(Long activityID, String groupName, Set<String> logins) throws LessonServiceException;
    
    /**
     * Remove a user to a group. If the user is not in the group, then nothing is changed.
     *
     * @throws LessonServiceException
     */
    abstract void removeUsersFromGroup(Long activityID, Long groupID, String learnerIDs[])
	    throws LessonServiceException;

    /**
     * Creates groups for branches, if they do not already exist.
     */
    void createChosenBranchingGroups(Long branchingActivityID);

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
     * Generate an email containing the progress details for individual activities in a lesson.
     *
     * @return String[] {subject, email body}
     */
    String[] generateLessonProgressEmail(Long lessonId, Integer userId);

    /**
     * Save information about an email notification sent to learners.
     */
    void archiveEmailNotification(Integer organisationId, Long lessonId, Integer searchType, String body,
	    Set<Integer> recipients);

    /**
     * Gets archived notifications for the given organisation.
     */
    List<EmailNotificationArchive> getArchivedEmailNotifications(Integer organisationId);

    /**
     * Gets archived notifications for the given lesson.
     */
    List<EmailNotificationArchive> getArchivedEmailNotifications(Long lessonId);

    /**
     * Gets pages recipients of the given archived email notification.
     */
    List<User> getArchivedEmailNotificationRecipients(Long emailNotificationUid, Integer limit, Integer offset);

    /**
     * Exports the given email notification to Excel sheet
     */
    List<ExcelSheet> exportArchivedEmailNotification(Long emailNotificationUid);

    /**
     * Set a groups name
     */
    void setGroupName(Long groupID, String name);

    /** Get the record of the learner's progress for a particular lesson */
    LearnerProgress getLearnerProgress(Integer learnerId, Long lessonId);
    
    /**
     * Returns list of contributed activities, and null if none available. Besides, it also updates lesson's
     * hasContributeActivities flag.
     * 
     * @param lessonId
     * @param skipCompletedBranching
     * @return
     */
    List<ContributeActivityDTO> calculateContributeActivities(Long lessonId, boolean skipCompletedBranching);

}
