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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.monitoring.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.MessageService;


/**
 * TODO Missing methods
 * 		archiveLesson
 *   
 * Interface defines all monitoring services needed by presentation tier.
 * @author Jacky Fang 2/02/2005
 * @author Manpreet Minhas
 */
public interface IMonitoringService
{

	/** Get the message service, which gives access to the I18N text */
	public MessageService getMessageService();

    /**
     * Initialize a new lesson so as to start the learning process. It needs to 
     * notify lams which learning design it belongs to. The initialize process
     * doesn't involve the setup of lesson class and organization.
     * 
     * @param lessonName the name of the lesson
     * @param lessonDescription the description of the lesson.
     * @param learningDesignId the selected learning design
     * @param organisationId the copied sequence will be put in the default runtime sequence folder for this org, if such a folder exists.
     * @param userId the user who want to create this lesson.
     * @return the lesson initialized.
     */
    public Lesson initializeLesson(String lessonName, String lessonDescription,long learningDesignId,Integer organisationId,Integer userID);
    /**
     * Create new lesson according to the learning design specified by the 
     * user, but for a preview session rather than a normal learning session.
     * The design is not assigned to any workspace folder.
     */
    public Lesson initializeLessonForPreview(String lessonName,String lessonDescription,long learningDesignId,Integer userID); 

    /**
     * Create a lession according to the input lession WDDX package. The sample package is following:
     * <code>
     * <pre>
     * <wddxPacket version="1.0"><header /><data><struct>
	 *	<var name="lessonID"><number>135</number></var>
	 *	<var name="organisationID"><number>10</number></var>
 	 *	<var name="staff"><array length="2"><number>7</number><number>8</number></array></var>
	 *	<var name="learners"><array length="6"><number>1</number><number>2</number>
	 *			<number>3</number><number>4</number><number>5</number><number>6</number></array></var>
	 *	</struct></data></wddxPacket>
     * </pre>
     * </code>
     * <P>The lesson will includes creator who create this lesson as staff and learner.
     * 
     * @param creatorUserId The lesson creator, who will be add to lesson as staff and learner.
     * @param lessionPackage
     * @return
     */
    public String createLessonClassForLessonWDDX(Integer creatorUserId,String lessionPackage) throws UserAccessDeniedException;
    
    /**
     * Setup the lesson class and organization for a lesson according to the 
     * input from monitoring GUI interface.
     * 
     * @param lessonId the lesson without lesson class and organization
     * @param organisation  the organization this lesson belongs to.
     * @param name of learner group
     * @param organizationUsers a list of learner will be in this new lessons.
     * @param name of staff group
     * @param staffs a list of staffs who will be in charge of this lesson.
     * @return the lesson with lesson class and organization
     */
    public Lesson createLessonClassForLesson(long lessonId, Organisation organisation,String leanerGroupName, List organizationUsers,String staffGroupName, List staffs, Integer userID) throws UserAccessDeniedException;
    
    /**
     * Start the specified the lesson. It must be created before calling this
     * service.
     * @param lessonId the specified the lesson id.
     * @param userId checks that the user is a staff member for this lesson
     * @throws LamsToolServiceException the exception occurred during the
     * 									lams and tool interaction to start a
     * 									lesson.
     */
    public void startLesson(long lessonId, Integer userId) throws UserAccessDeniedException;
    /**
     * Start a lesson on schedule datetime.
     * @param lessonId
     * @param startDate the lesson start date and time.
     * @param userId checks that the user is a staff member for this lesson
     * @see org.lamsfoundation.lams.monitoring.service#startLesson(long)
     */
    public void startLessonOnSchedule(long lessonId, Date startDate, Integer userId) throws UserAccessDeniedException;
    /**
     * Finish a lesson on schedule datetime.
     * @param lessonId
     * @param endDate the lesson end date and time.
     * @param userId checks that the user is a staff member for this lesson
     */
    public void finishLessonOnSchedule(long lessonId, Date endDate, Integer userId) throws UserAccessDeniedException;
    /**
     * Finish a lesson.A Finished lesson can be viewed on the monitoring interface. 
     * It should be an "inactive" lesson. A Finished lesson is listed on the learner 
     * interface but all the learner can do is view the progress bar and do an 
     * export portfolio - they cannot access any of the tool screens.
     *  
     * @param lessonId
     * @param userId checks that the user is a staff member for this lesson
     * @param endDate teh lesson end date and time.
     */
    public void finishLesson(long lessonId, Integer userId) throws UserAccessDeniedException;
    
    /**
     * Force Complete works on an individual user. The teacher may complete it up to a particular activity, 
     * or till the end of the sequence which activity id is null indicating complete to end. Note, the give 
     * activity will be complete as well.
     * 
     * @param learnerId
     * @param lessonId
     * @param activityId force complete to this activity(this activity will be force complete as well). 
     * 		If null value, force will complete all activities in this lesson.
     * @return success message.
     */
    public String forceCompleteLessonByUser(Integer learnerId,long lessonId,Long activityId);
    
    /**
     * Archive the specified lesson. When archived, the data is retained
     * but the learners cannot access the details. 
     * @param lessonId the specified the lesson id.
     * @param userId checks that the user is a staff member for this lesson
     */
    public void archiveLesson(long lessonId, Integer userId) throws UserAccessDeniedException;
    /**
     * A lesson can only be suspended if it is started. The purpose of suspending is 
     * to hide the lesson from learners temporarily. If the teacher tries to suspend a lesson that 
     * is not in the STARTED_STATE, then an error should be returned. 
     * @param lessonId the lesson ID which will be suspended.
     * @param userId checks that the user is a staff member for this lesson
     */
    public void suspendLesson(long lessonId, Integer userId) throws UserAccessDeniedException;
    /**
     * Unsuspend a lesson, which state must be Lesson.SUSPEND_STATE. Otherwise an exception will be thrown.
     * @param lessonId
     * @param userId checks that the user is a staff member for this lesson
     */
    public void unsuspendLesson(long lessonId, Integer userId) throws UserAccessDeniedException;

    /**
     * <P>
     * Teachers sometimes find that there are just too many "old" designs and 
     * wish to remove them and never access them again. This function disables 
     * the lesson - it does not remove the contents from the database
     * </P>
     * @param lessonId the specified the lesson id.
     * @param userId checks that the user is a staff member for this lesson
     */
	public void removeLesson(long lessonId, Integer userId) throws UserAccessDeniedException;
     /**
     * Set the gate to open to let all the learners through. This learning service
     * is triggerred by the system scheduler. Will return true GateActivity (or subclass)
     * object, rather than a hibernate proxy. This is needed so that the class can 
     * be returned to the web layer for proper handling.
     * 
     * @param gate the id of the gate we need to open.
     */
    public GateActivity openGate(Long gateId);
    
    /**
     * Set the gate to closed.
     * @param gate the id of the gate we need to close.
     */
    public GateActivity closeGate(Long gateId);
    
    /**
     * This method returns a string representing a list of all lessons
     * for the current user. See getAllLessons(). 
     * 
     * @return String The requested list of Lessons in wddx format
     * @throws IOException
     */
    public String getAllLessonsWDDX(Integer userID) throws IOException;
    
    /**
    * This method returns a list of all available Lessons. This is all the lessons
     * created by the current user and all the lessons for which the user is in the
     * staff group. It does not return removed lessons.
     * 
     * If the data is to be sent to Flash, then use  getAllLessonsWDDX()
     *
     * @param userID The user_id of the user for whom the lessons 
     * 				 are being fetched.
     * @return List The requested list of Lessons
     * @throws IOException
     */
    public List getAllLessons(Integer userID) throws IOException;
    
    /**
     * This method returns the details for the given Lesson in
     * WDDX format
     * 
     * @param lessonID The lesson_id of the Lesson for which the details have 
     * 				   to be fetched
     * @return String The requested details in wddx format
     * @throws IOException
     */
    public String getLessonDetails(Long lessonID)throws IOException;
    
    /**
     * This method returns the data for the given Lesson in
     * WDDX format
     * 
     * @param lessonID The lesson_id of the Lesson for which the details have 
     * 				   to be fetched
     * @return String The requested data in wddx format
     * @throws IOException
     */
    public String getLessonData(Long lessonID)throws IOException;
    
    
    /**
     * Returns a list of learners participating in the given Lesson
     * 
     * @param lessonID The lesson_id of the Lesson
     * @return String The requested list in wddx format
     * 
     * @throws IOException
     */
    public String getLessonLearners(Long lessonID)throws IOException;
    
    /**
     * This method returns the LearningDesign details for a given Lesson
     * 
     * @param lessonID The lesson_id of the Lesson whose LearningDesign details are required
     * @return String The requested details in wddx format
     * @throws IOException
     */
    public String getLearningDesignDetails(Long lessonID)throws IOException;
    
    /**
     * This method returns the progress information of all learners
     * in a given Lesson.
     * 
     * @param lessonID The lesson_id of the Lesson whose progress information is required
     * @return String The requested information in wddx format
     * @throws IOException
     */
    public String getAllLearnersProgress(Long lessonID)throws IOException;
    
    /**
     * This method is called when the user clicks the 'Contribute' tab in the 
     * monitoring enviornment. It returns a list of activities "in the 
     * order" they have to be performed and with additional information as to 
     * what kind of contribution (Define later content, Moderation, Contribution, 
     * Permission for gate activity, Chosen Grouing etc.) is reuired from the 
     * user(teacher/staff).
     *   
     * @param lessonID The lesson_id of the Lesson for which the information has
     * 					to be fetched. 
     * @return String The required information in WDDX format
     * @throws IOException
     */
    public String getAllContributeActivities(Long lessonID)throws IOException, LearningDesignProcessorException;
    
    /**
     * This method returns the url associated with the activity in the monitoring
     * enviornment. This is the URL that opens up when the user/teacher clicks on
     * the activity in the monitoring enviornment and then selects a learner OR
     * in the LEARNER tab when a learner's activity is clicked.
     * 
     * This is also known as the learner progress url.
     *  
     * @param lessonID The lesson_id of the Lesson for which the information has
     * 					to be fetched. 
     * @param activityID The activity_id of the activity for which the URL is required 
     * @param userID The user_id of the Learner for whom the URL is being fetched
     * @return String The required information in WDDX format
     * @throws IOException
     * @throws LamsToolServiceException
     */
    public String getLearnerActivityURL(Long lessonID, Long activityID,Integer userID)throws IOException,LamsToolServiceException;
    
    /**
     * This method returns the define later url for the given activity
     * 
     * @param lessonID The lesson_id of the Lesson for which the information has
     * 					to be fetched. 
     * @param activityID The activity_id of the Activity whose URL will be returned
     * @return String The required information in WDDX format
     * @throws IOException
     */
    public String getActivityDefineLaterURL(Long lessonID, Long activityID)throws IOException, LamsToolServiceException;

    /**
     * This method returns the monitor url for the given activity
     * 
     * @param lessonID The lesson_id of the Lesson for which the information has
     * 					to be fetched. 
     * @param activityID The activity_id of the Activity whose URL will be returned
     * @return String The required information in WDDX format
     * @throws IOException
     */
    public String getActivityMonitorURL(Long lessonID, Long activityID)throws IOException, LamsToolServiceException;
  
   /**
     * This method moves the learning design corresponding to the given
     * Lesson into the specified workspaceFolder. But before this action
     * is performed it checks whether the user is authorized to do so. 
     * If not, Flash is notified of the same. As of now it is assumed that 
     * only the owner of lesson/learning design can move it 
     *  
     * @param lessonID The lesson_id of the Lesson which has to be moved
     * @param targetWorkspaceFolderID The workspace_folder_id of the WorkspaceFolder
     * 								  to which the lesson has to be moved 
     * @param userID The user_id of the User who has requested this operation
     * @return String The acknowledgement message/error in WDDX format 
     * @throws IOException
     */
    public String moveLesson(Long lessonID, Integer targetWorkspaceFolderID,Integer userID)throws IOException;
    /**
     * This method changes the name of an existing Lesson to the
     * one specified.
     *   
     * @param lessonID The lesson_id of the Lesson whose name has to be changed
     * @param newName The new name of the Lesson
     * @param userID The user_id of the User who has requested this operation
     * @return String The acknowledgement message/error in WDDX format 
     * @throws IOException
     */
    public String renameLesson(Long lessonID, String newName, Integer userID)throws IOException;
    
    /**
     * Return an activity object based on the requested id.
     * @param activityId id of the activity.
     * @return the requested activity object.
     */
    public Activity getActivityById(long activityId);
    
    /**
     * Returns the status of the gate in WDDX format. 
     * 
     * @param activityID The activity_id of the Activity whose gate must be checked
     * @param lessonID The lesson_id of the Lesson
     * @return
     */
    public String checkGateStatus(Long activityID, Long lessonID) throws IOException;
    
    /**
     * Returns an acknowledgement that the gate has been released.
     * Returns true if the gate has been released and false otherwise.
     * This information is packaged in WDDX format
     * 
     * @param activityID The activity_id of the Activity whose gate must be checked
     * @param lessonID The lesson_id of the Lesson
     * @return
     */
    public String releaseGate(Long activityID) throws IOException;
    
    /**
     * Perform chosen grouping. The groups contains a list of Hashtable which contain 
     * following information for each group:<br>
     * <ol>
     *   <li>List of learners in this group</li>
     *   <li>Order ID</li>
     *   <li>Group name</li>
     * </ol>
     * 
     * @param groupingActivity
     * @param groups list of group information. 
     */
    public void performChosenGrouping(GroupingActivity groupingActivity, List groups);
    
	//---------------------------------------------------------------------
	// Preview Methods
	//---------------------------------------------------------------------
	/**
	 * Create the lesson class and the staff class for a preview lesson.
	 * The lesson is not attached to any organisation.
	 * 
	 * @param userID User ID of the teacher running the preview. Mandatory.
	 * @param lessonID ID of the lesson
	 * @return Lesson
	 */
	public abstract Lesson createPreviewClassForLesson(int userID,
			long lessonID) throws UserAccessDeniedException;

	/**
	 * Remove all the details for a particular preview lessons. The transaction 
	 * handling for this method should be REQUIRES_NEW, which allows
	 * each lesson to be deleted separately. 
	 * 
	 * @param lessonID ID of the lesson which is the preview session. Mandatory.
	 */
	public abstract void deletePreviewLesson(long lessonID);

	/**
	 * Remove all the "old" preview lessons. Removes all preview lessons older than 
	 * the number of days specified in the configuration file.  
	 * <p>
	 * Calls deletePreviewLesson(long lessonID) to do the actual deletion, so
	 * if one lesson throws a database exception when deleting, the other lessons
	 * should delete okay (as  deletePreviewLesson uses a REQUIRES_NEW transaction)
	 * 
	 * @return number of lessons deleted.
	 */
	public abstract int deleteAllOldPreviewLessons();

    /* TODO Dummy methods - to be removed */
    public List getLearningDesigns(Long userId);
	

}
