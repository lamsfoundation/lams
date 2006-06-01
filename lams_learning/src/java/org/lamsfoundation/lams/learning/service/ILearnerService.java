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
package org.lamsfoundation.lams.learning.service;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;
/**
 *
 * @author chris
 */
public interface ILearnerService
{

	/** Get the I18N service. Used by actions for internationalising errors that go back to Flash */
   public MessageService getMessageService();

    /**
     * Gets the lesson object for the given key.
     *
     */
    public Lesson getLesson(Long lessonID);

    /**
     * Get the lesson data for a particular lesson. In a DTO format suitable for sending to the client.
     */
    public LessonDTO getLessonData(Long lessonId);

 
    /**
     * Joins a User to a a new lesson as a learner
     * @param learnerId the Learner's userID
     * @param lessionID identifies the Lesson to start
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress joinLesson(Integer learnerId, Long lessonID) ;
    

    /**
     * Returns the current progress data of the User.
     * @param learnerId the Learner's userID
     * @param lessonId the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress getProgress(Integer learnerId, Long lessonId);
    
    /**
     * Return the current progress data against progress id.
     * @param progressId
     * @return
     */
    public LearnerProgress getProgressById(Long progressId);
    
    /**
     * Return the current progress data against progress id.
     * Returns a DTO suitable to send to Flash.
     * @param progressId
     * @return
     */
    public LearnerProgressDTO getProgressDTOById(Long progressId);

    /**
     * Marks an activity as attempted. Called when a user selects an OptionsActivity.
     * @param learnerId the Learner's userID
     * @param lessonId the Lesson to get progress from.
     * @param activity the activity being attempted.
     * @return LearnerProgress
     */
    public LearnerProgress chooseActivity(Integer learnerId, Long lessonId, Activity activity);

    
    /**
     * Calculates learner progress and returns the data required to be displayed to the learner (including URL(s)).
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lessonId the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(Activity completedActivity, Integer learnerId, Long lessonId); 

    
    /**
     * Marks an tool session as complete and calculates the next activity against
     * the learning design. This method is for tools to redirect the client on 
     * complete.
     * 
     * Do not change learnerId to Integer (to match the other calls)
     * as all the tools expect this to be a Long.
     * 
     * @param toolSessionId, session ID for completed tool
     * @param learnerId the learner who is completing the tool session.
     * @return the URL for the next activity
     * @throws LearnerServiceException in case of problems.
     */
    public String completeToolSession(Long toolSessionId, Long learnerId);
    
    /**
     * Complete the activity in the progress engine and delegate to the progress 
     * engine to calculate the next activity in the learning design. This 
     * process might be triggerred by system controlled the activity, such as
     * grouping and gate. This method should be used when we don't have an activity
     * or a lesson that is already part of the Hibernate session. 
     * 
     * @param learnerId the learner who are running this activity in the design.
     * @param activity the activity is being run.
     * @param lesson id the lesson this learner is currently in.
     * @return the url for next activity.
     */
    public String completeActivity(Integer learnerId,Long activityId,Long lessonId);
  
    /**
     * Complete the activity in the progress engine and delegate to the progress 
     * engine to calculate the next activity in the learning design. This method should 
     * be used when we t have an activity that is already part of the Hibernate session. 
     * It is currently triggered by complete tool session progress from tool.
     * 
     * @param learnerId the learner who are running this activity in the design.
     * @param activity the activity is being run.
     * @param lessonId the lesson this learner is currently in.
     * @return the url for next activity.
     */
    public String completeActivity(Integer learnerId,Activity activity,Long lessonId);

    /**
     * Retrieve all lessons that has been started, suspended or finished. All
     * finished but archived lesson should not be loaded.
     * TODO to be removed when dummy learner interface is removed
     * @param learner the user who intend to start a lesson
     * @return a list of active lessons.
     */
    public LessonDTO[] getActiveLessonsFor(Integer learnerId);
    
    /**
     * Mark the learner progress as restarting to indicate the current learner
     * has exit the lesson. Doesn't use the cached progress object in case it 
     * 
     * @param progressId the current learner progress.
     */
    public void exitLesson(Long progressId);
    
    /**
     * Returns an activity according to the activity id.
     * @param activityId the activity id.
     * @return the activity requested.
     */
    public Activity getActivity(Long activityId);
    
    /**
     * Returns all the active learners by the lesson id.
     * @param lessonId the requested lesson id.
     * @return the list of learners.
     */
    public List getActiveLearnersByLesson(long lessonId);
    
    /**
     * Returns a count of all the active learners by lesson id.
     * More efficient than calling getActiveLearnersByLesson(lessonId).size()
     */
    public Integer getCountActiveLearnersByLesson(long lessonId);

    /**
     * Perform random grouping for the learners who have started the lesson,
     * based on the grouping activity. This method should be used when we don't 
     * have an grouping activity that is already part of the Hibernate session. 
     * 
     * @param lessonId lesson id
     * @param groupingActivityId the activity that has create grouping.
     */
    public void performGrouping(Long lessonId, Long groupingActivityId);
    

    /**
     * Perform random grouping for the learners who have started the lesson,
     * based on the grouping activity. This method should be used when we do 
     * have an grouping activity that is already part of the Hibernate session.
     *  
     * @param lessonId lesson id
     * @param groupingActivityId the activity that has create grouping.
     */
    public void performGrouping(Long lessonId, GroupingActivity groupingActivity);
    
    /**
     * Perform random grouping a single learner based on the grouping activity.
     * @param groupingActivityId the activity that has create grouping.
     * @param learner the learner needs to be grouped
     */
    public void performGrouping(Long groupingActivityId, User learner);
    
    /**
     * Check up the gate status to go through the gate. This also updates the gate.
     * This method should be used when we do not have an grouping activity
     * that is already part of the Hibernate session. 
     * @param lessonId lesson id
     * @param gateid the gate that current learner is facing. It could be 
     * 			   synch gate, schedule gate or permission gate.
     * @param knocker the learner who wants to go through the gate.
     */
    public boolean knockGate(Long lessonId, Long gateActivityId, User knocker);

    /**
     * Check up the gate status to go through the gate. This also updates the gate.
     * This method should be used when we do have an grouping activity
     * that is already part of the Hibernate session. 
     * @param lessonId lesson id
     * @param gate the gate that current learner is facing. It could be 
     * 			   synch gate, schedule gate or permission gate.
     * 			   Don't supply the actual gate from the cached web version
     * 			   as it might be out of date or not attached to the session
     * @param knocker the learner who wants to go through the gate.
     */
    public boolean knockGate(Long lessonId, GateActivity gateActivity, User knocker);
}
