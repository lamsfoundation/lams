/*
 * ILearnerService.java
 *
 * Created on 18 January 2005, 15:23
 */

package org.lamsfoundation.lams.learning.service;

import java.util.List;

import com.lamsinternational.lams.lesson.Lesson;
import com.lamsinternational.lams.lesson.LearnerProgress;
import com.lamsinternational.lams.usermanagement.User;
/**
 *
 * @author chris
 */
public interface ILearnerService
{



    /**
     * Returns a list of all the active Lessons a User is a Learner in.
     * @param User the learner
     * @return List of Lessons
     * @throws LearnerServiceException in case of problems.
     */
    public List getActiveLessons(User learner);


    /**
     * Used to allow a User to resume a Lesson they have already started
     * @param learner the Learner
     * @param the lesson to resume
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress resumeLesson(User learner, Lesson lesson);

    
    /**
     * Gets the lesson object for the given key.
     *
     */
    public Lesson getLesson(long lessonID);

    
 
    /**
     * Joins a User to a a new lesson as a learner
     * @param learner the Learner
     * @param lessionID identifies the Lesson to start
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress startLesson(User learner, Lesson lesson);

    

    /**
     * Used to view a completed activity (read only?).
     * @param activityID identifies the activity to view
     * @param learner the Learner in the activity
     * @param lesson the Lesson in which the activity took place
     * @throws LearnerServiceException in case of problems.
     * @return bean containing Learner Display data for the activity.
     */
    //public Bean viewFinishedActivity(long activityID, User learner, Lesson lesson);

    

    /**
     * Used to indicate when a Learner leaves a lesson.
     * @param learner the Learner
     * @param lesson the Lesson to exit from.
     */
    public void exitLesson(User learner, Lesson lesson);

    

    /**
     * Returns the current progress data of the User.
     * @param learner the Learner
     * @param lesson the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress getProgress(User learner, Lesson lesson);

    
    /**
     * Calculates learner progress and returns the data required to be displayed to the learner (including URL(s)).
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lesson the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(long completedActivityID, User learner, Lesson lesson);

}
