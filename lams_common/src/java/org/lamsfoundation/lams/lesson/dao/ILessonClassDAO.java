/*
 * ILessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao;

import org.lamsfoundation.lams.lesson.LessonClass;

/**
 * Inteface defines Lesson DAO Methods
 * @author chris
 */
public interface ILessonClassDAO
{
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public LessonClass getLessonClass(Long lessonClassId);
    
    /**
     * Saves or Updates a Lesson.
     * @param lesson the Lesson to save
     */
    public void saveOrUpdateLessonClass(LessonClass lessonClass);
    
    /**
     * Deletes a Lesson <b>permanently</b>.
     * @param lesson the Lesson to remove.
     */
    public void deleteLessonClass(LessonClass lessonClass);
    
    
}
