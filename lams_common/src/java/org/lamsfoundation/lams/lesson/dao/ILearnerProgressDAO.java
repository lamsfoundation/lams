/*
 * ILessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Inteface defines Lesson DAO Methods
 * @author chris
 */
public interface ILearnerProgressDAO
{
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public LearnerProgress getLearnerProgress(Long learnerProgressId);
        
    /**
     * Saves or Updates learner progress data.
     * @param learnerProgress holds the learne progress data
     */
    public void saveOrUpdateLearnerProgress(LearnerProgress learnerProgress);
    
    /**
     * Deletes a LearnerProgress data <b>permanently</b>.
     * @param learnerProgress 
     */
    public void deleteLearnerProgress(LearnerProgress learnerProgress);
}
