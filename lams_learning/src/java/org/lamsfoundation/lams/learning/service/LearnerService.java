/*
 * LearnerService.java
 *
 * Created on 11 January 2005, 15:42
 */
package org.lamsfoundation.lams.learning.service;

import java.util.List;

import org.lamsfoundation.lams.learning.progress.ProgressEngine;
import org.lamsfoundation.lams.learning.progress.dao.IProgressDAO;


import com.lamsinternational.lams.lesson.Lesson;
import com.lamsinternational.lams.lesson.dao.ILessonDAO;
import com.lamsinternational.lams.lesson.LearnerProgress;
import com.lamsinternational.lams.usermanagement.User;
/**
 * This class is a facade over the Learning middle tier.
 * @author chris
 */
public class LearnerService implements ILearnerService
{
    private IProgressDAO progressDAO = null;
    private ILessonDAO lessonDAO = null;
    
    /** Creates a new instance of LearnerService */
    public LearnerService()
    {
    }

    /**
     * Returns a list of all the active Lessons a User is a Learner in.
     * @param User the learner
     * @return List of Lessons
     * @throws LearnerServiceException in case of problems.
     */
    public List getActiveLessons(User learner)
    {
        
        return lessonDAO.getActiveLessons(learner);
        
    }

    /**
     * Used to allow a User to resume a Lesson they have already started
     * @param learner the Learner
     * @param the lesson to resume
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress resumeLesson(User learner, Lesson lesson)
    {
        return ProgressEngine.resumeLesson(learner, lesson);
    }
    
    public Lesson getLesson(long lessonID)
    {
        return lessonDAO.getLession(lessionID);
    }
    
 
    /**
     * Joins a User to a a new lesson as a learner
     * @param learner the Learner
     * @param lessionID identifies the Lesson to start
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress startLesson(User learner, Lesson lesson)
    {
        return ProgressEngine.startLesson(leaner, lesson);
    }
    

    /**
     * Used to view a completed activity (read only?).
     * @param activityID identifies the activity to view
     * @param learner the Learner in the activity
     * @param lesson the Lesson in which the activity took place
     * @throws LearnerServiceException in case of problems.
     * @return bean containing Learner Display data for the activity.
     */
//    public Bean viewFinishedActivity(long activityID, User learner, Lesson lesson)
//    {
//        return ProgressEngine.viewFinishedActivity(activityID, learner, lesson);
//    }
    

    /**
     * Used to indicate when a Learner leaves a lesson.
     * @param learner the Learner
     * @param lesson the Lesson to exit from.
     */
    public void exitLesson(User learner, Lesson lesson)
    {
        ProgressEngine.exitLesson(learner, lesson);
    }
    

    /**
     * Returns the current progress data of the User.
     * @param learner the Learner
     * @param lesson the Lesson to get progress from.
     * @return LearnerProgess contains the learner's progress for the lesson.
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress getProgress(User learner, Lesson lesson)
    {
        return progressDAO.getProgress(learner, lesson);
    }
    
    /**
     * Calculates learner progress and returns the data required to be displayed to the learner (including URL(s)).
     * @param completedActivityID identifies the activity just completed
     * @param learner the Learner
     * @param lesson the Lesson in progress.
     * @return the bean containing the display data for the Learner
     * @throws LearnerServiceException in case of problems.
     */
    public LearnerProgress calculateProgress(long completedActivityID, User learner, Lesson lesson)
    {
        return ProgressEngine.getNextActivity(completedActivityID, learner, lesson);
    }
}
