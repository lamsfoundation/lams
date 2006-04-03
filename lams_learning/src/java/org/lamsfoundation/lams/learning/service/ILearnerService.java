/*
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
*/

package org.lamsfoundation.lams.learning.service;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dto.LessonDTO;
import org.lamsfoundation.lams.usermanagement.User;
/**
 *
 * @author chris
 */
public interface ILearnerService
{


    /**
     * Gets the lesson object for the given key.
     *
     */
    public Lesson getLesson(Long lessonID);

    
 
    /**
     * Joins a User to a a new lesson as a learner
     * @param learner the Learner
     * @param lessionID identifies the Lesson to start
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress joinLesson(User learner, Long lessonID) ;
    

    /**
     * Returns the current progress data of the User.
     * @param learner the Learner
     * @param lesson the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress getProgress(User learner, Lesson lesson);
    
    /**
     * Return the current progress data against progress id.
     * @param progressId
     * @return
     */
    public LearnerProgress getProgressById(Long progressId);
    
    /**
     * Marks an activity as attempted. Called when a user selects an OptionsActivity.
     * @param learner the Learner
     * @param lesson the Lesson to get progress from.
     * @param activity the activity being attempted.
     * @return LearnerProgress
     */
    public LearnerProgress chooseActivity(User learner, Lesson lesson, Activity activity);

    
    /**
     * Calculates learner progress and returns the data required to be displayed to the learner (including URL(s)).
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lesson the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(Activity completedActivity, User learner, Lesson lesson); 

    
    /**
     * Marks an tool session as complete and calculates the next activity against
     * the learning design. This method is for tools to redirect the client on 
     * complete.
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
     * grouping and gate. It might also be triggerred by complete tool session
     * progress from tool. Therefore, the transaction demarcation needs to be 
     * configured as <code>REQURIED</code>.
     * 
     * 
     * @param learner the learner who are running this activity in the design.
     * @param activity the activity is being runned.
     * @param lesson the lesson this learner is currently in.
     * @return the url for next activity.
     */
    public String completeActivity(User learner,Activity activity,Lesson lesson);
  
    /**
     * Retrieve all lessons that has been started, suspended or finished. All
     * finished but archived lesson should not be loaded.
     * 
     * @param learner the user who intend to start a lesson
     * @return a list of active lessons.
     */
    public LessonDTO[] getActiveLessonsFor(User learner);
    
    /**
     * Mark the learner progress as restarting to indicate the current learner
     * has exit the lesson
     * @param progress the current learner progress.
     */
    public void exitLesson(LearnerProgress progress);
    
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
     * Perform random grouping for a list of learners based on the grouping
     * activity.
     * @param groupingActivity the activity that has create grouping.
     * @param learners the list of learners need to be grouped.
     */
    public void performGrouping(GroupingActivity groupingActivity, List learners);
    
    /**
     * Perform random grouping a single learner based on the grouping activity.
     * @param groupingActivity the activity that has create grouping.
     * @param learner the learner needs to be grouped
     */
    public void performGrouping(GroupingActivity groupingActivity, User learner);
    
    /**
     * Check up the gate status to go through the gate.
     * @param gate the gate that current learner is facing. It could be 
     * 			   synch gate, schedule gate or permission gate.
     * @param knocker the learner who wants to go through the gate.
     * @param lessonLearners the entire lesson learners.
     */
    public boolean knockGate(GateActivity gate,User knocker,List lessonLearners);
}
