/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao.hibernate;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class LearnerProgressDAO extends HibernateDaoSupport implements ILearnerProgressDAO
{
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public LearnerProgress getLearnerProgress(Long learnerProgressId)
    {
        return (LearnerProgress)getHibernateTemplate().get(LearnerProgress.class, learnerProgressId);
    }
    
    /**
     * Saves or Updates learner progress data.
     * @param learnerProgress holds the learne progress data
     */
    public void saveLearnerProgress(LearnerProgress learnerProgress)
    {
        getHibernateTemplate().save(learnerProgress);
    }
    
    /**
     * Deletes a LearnerProgress data <b>permanently</b>.
     * @param learnerProgress
     */
    public void deleteLearnerProgress(LearnerProgress learnerProgress)
    {
        getHibernateTemplate().delete(learnerProgress);
    }
    
}
