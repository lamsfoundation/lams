/*
 * ILessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao;

import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Inteface defines Lesson DAO Methods
 * @author chris
 */
public interface ILessonDAO
{
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public Lesson getLesson(Long lessonId);
    
    /**
     * Gets all lessons that are active for a learner.
     * @param learner a User that identifies the learner.
     * @return a Set with all active lessons in it.
     */
    public List getActiveLessonsForLearner(User learner);
    
    /**
     * Saves or Updates a Lesson.
     * @param lesson the Lesson to save
     */
    public void saveLesson(Lesson lesson);
    
    /**
     * Deletes a Lesson <b>permanently</b>.
     * @param lesson the Lesson to remove.
     */
    public void deleteLesson(Lesson lesson);
    
    /**
     * Retrieves the LearnerProgress
     * @param learner the User in the Lesson
     * @param lesson the Lesson
     * @return LearnerProgess object containing the progress and state data.
     */
    public LearnerProgress getLearnerProgress(User learner, Lesson lesson);
    
}
