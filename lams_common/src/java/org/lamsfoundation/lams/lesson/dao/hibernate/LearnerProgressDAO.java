/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao.hibernate;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class LearnerProgressDAO extends HibernateDaoSupport implements ILearnerProgressDAO
{
    private final static String LOAD_PROGRESS_BY_LEARNER = 
        "from LearnerProgress p where p.user = :learner and p.lesson = :lesson";

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

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO#getLearnerProgressByLeaner(org.lamsfoundation.lams.usermanagement.User)
     */
    public LearnerProgress getLearnerProgressByLearner(final User learner, final Lesson lesson)
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (LearnerProgress)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                     return session.createQuery(LOAD_PROGRESS_BY_LEARNER)
                     			   .setEntity("learner",learner)
                     			   .setEntity("lesson",lesson)
                     			   .uniqueResult();
                 }
             }
       );     
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO#updateLearnerProgress(org.lamsfoundation.lams.lesson.LearnerProgress)
     */
    public void updateLearnerProgress(LearnerProgress learnerProgress)
    {
        this.getHibernateTemplate().update(learnerProgress);
    }
}
