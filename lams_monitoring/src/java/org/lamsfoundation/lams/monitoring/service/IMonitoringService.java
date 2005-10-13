/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.monitoring.service;

import java.io.IOException;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * TODO Missing methods
 * 		archiveLesson
 * 		getMonitorURL
 *   
 * Interface defines all monitoring services needed by presentation tier.
 * @author Jacky Fang 2/02/2005
 * @author Manpreet Minhas
 */
public interface IMonitoringService
{

    /**
     * Initialize a new lesson so as to start the learning process. It needs to 
     * notify lams which learning design it belongs to. The initialize process
     * doesn't involve the setup of lesson class and organization.
     * 
     * @param lessonName the name of the lesson
     * @param lessonDescription the description of the lesson.
     * @param learningDesignId the selected learning design
     * @param user user the user who want to create this lesson.
     * @return the lesson initialized.
     */
    public Lesson initializeLesson(String lessonName, String lessonDescription,long learningDesignId,User user);
    
    
    /**
     * Setup the lesson class and organization for a lesson according to the 
     * input from monitoring GUI interface.
     * 
     * @param lessonId the lesson without lesson class and organization
     * @param organisation  the organization this lesson belongs to.
     * @param organizationUsers a list of learner will be in this new lessons.
     * @param staffs a list of staffs who will be in charge of this lesson.
     * @return the lesson with lesson class and organization
     */
    public Lesson createLessonClassForLesson(long lessonId,Organisation organisation,List organizationUsers,List staffs);
    
    /**
     * Start the specified the lesson. It must be created before calling this
     * service.
     * @param lessonId the specified the lesson id.
     * @throws LamsToolServiceException the exception occurred during the
     * 									lams and tool interaction to start a
     * 									lesson.
     */
    public void startlesson(long lessonId);
    
    /**
     * Force the learner to complete all the activities for current lesson.
     * @param learnerProgressId the learner progress belongs to the user who
     * 							will be force complete
     */
    public void forceCompleteLessonByUser(long learnerProgressId);
    
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
     * This method returns a A list of all available Lessons.
     * If the data is to be sent to Flash, then use  getAllLessonsWDDX()
     * 
     * @return List The requested list of Lessons
     * @throws IOException
     */
    public List getAllLessons() throws IOException;

    /**
     * This method returns a string representing a list of all 
     * available Lessons in the WDDX format
     * 
     * @return String The requested list of Lessons in wddx format
     * @throws IOException
     */
    public String getAllLessonsWDDX() throws IOException;
    
    /**
     * This method returns a string representing a list of all 
     * available Lessons for a given user. If the data is to be 
     * sent to Flash, then use  getAllLessonsWDDX(Integer userID)
     * 
     * @param userID The user_id of the user for whom the lessons 
     * 				 are being fetched.
     * @return List The requested list of Lessons
     * @throws IOException
     */
    public List getAllLessons(Integer userID)throws IOException;
    

    /**
     * This method returns a string representing a list of all 
     * available Lessons for a given user in the WDDX format
     * 
     * @param userID The user_id of the user for whom the lessons 
     * 				 are being fetched.
     * @return String The requested list of Lessons in wddx format
     * @throws IOException
     */
    public String getAllLessonsWDDX(Integer userID)throws IOException;
    
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
    public String getAllContributeActivities(Long lessonID)throws IOException;
    
    /**
     * This method returns the url associated with the activity in the monitoring
     * enviornment. This is the URL that opens up when the user/teacher clicks on
     * the activity in the monitoring enviornment and then selects a learner OR
     * in the LEARNER tab when a learner's activity is clicked.
     *  
     * @param activityID The activity_id of the activity for which the URL is required 
     * @param userID The user_id of the Learner for whom the URL is being fetched
     * @return String The required information in WDDX format
     * @throws IOException
     * @throws LamsToolServiceException
     */
    public String getLearnerActivityURL(Long activityID,Integer userID)throws IOException,LamsToolServiceException;
    
    /**
     * This method returns the contribute url for the given activity
     * 
     * @param activityID The activity_id of the Activity whose URL will be returned
     * @return String The required information in WDDX format
     * @throws IOException
     */
    public String getActivityContributionURL(Long activityID)throws IOException;
    
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
}
