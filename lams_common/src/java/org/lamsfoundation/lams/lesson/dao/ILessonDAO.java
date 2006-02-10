/*
 * ILessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
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
    
    /** Get all the lessons in the database. This includes the disabled lessons. */
    public List getAllLessons();
    
    public Lesson getLessonWithJoinFetchedProgress(Long lessonId);
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
     * Update a requested lesson.
     * @param createdLesson
     */
    public void updateLesson(Lesson lesson);
    
    /**
      * Returns the list of available Lessons created by
     * a given user. Does not return disabled lessons or preview lessons.
    * 
     * @param userID The user_id of the user
     * @return List The list of Lessons for the given user
     */
    public List getLessonsCreatedByUser(Integer userID);
    
    /**
     * Returns the all the learners that have started the requested lesson.
     * 
     * @param lessonId the id of the requested lesson.
     * @return the list of learners.
     */
    public List getActiveLearnerByLesson(final long lessonId);

    /**
     * Get all the preview lessons more with the creation date before the given date.
     * 
     * @param startDate UTC date 
     * @return the list of Lessons
     */
    public List getPreviewLessonsBeforeDate(final Date startDate);
}
