/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao.hibernate;

import java.util.List;


import net.sf.hibernate.FetchMode;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;

import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class LessonDAO extends HibernateDaoSupport implements ILessonDAO
{
	private static final String TABLENAME ="lams_lesson";
	private final static String FIND_BY_USER="from " + TABLENAME + 
											 " in class " + Lesson.class.getName() +
											 " where user_id=?";
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public Lesson getLesson(Long lessonId)
    {
        return (Lesson)getHibernateTemplate().get(Lesson.class, lessonId);
    }
    
    
    public Lesson getLessonWithEagerlyFetchedProgress(final Long lessonId)
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (Lesson)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                     return session.createCriteria(Lesson.class)
                     			   .add(Expression.like("lessonId",lessonId))
                     			   .setFetchMode("learnerProgresses",FetchMode.EAGER)
                     			   .uniqueResult();
                 }
             }
       ); 
    }
    
    public List getAllLessons()
    {
        return getHibernateTemplate().loadAll(Lesson.class);
    }
    /**
     * Gets all lessons that are active for a learner.
     * @param learner a User that identifies the learner.
     * @return a List with all active lessons in it.
     */
    public List getActiveLessonsForLearner(final User learner)
    {
    	List lessons = null;
    	
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
    	lessons = (List)hibernateTemplate.execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
        	    	Query query = session.getNamedQuery("activeLessons");
        	    	query.setInteger("userId", learner.getUserId().intValue());
        	    	List result = query.list();
                    return result;
                }
            }
        );
        return lessons;
    }
    
    /**
     * Saves or Updates a Lesson.
     * @param lesson
     */
    public void saveLesson(Lesson lesson)
    {
        getHibernateTemplate().save(lesson);
    }
    
    /**
     * Deletes a Lesson <b>permanently</b>.
     * @param lesson
     */
    public void deleteLesson(Lesson lesson)
    {
        getHibernateTemplate().delete(lesson);
    }
    


    /**
     * Update the lesson data
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#updateLesson(org.lamsfoundation.lams.lesson.Lesson)
     */
    public void updateLesson(Lesson lesson)
    {
        getHibernateTemplate().update(lesson);
    }
    public List getLessonsForUser(Integer userID){
    	List lessons = this.getHibernateTemplate().find(FIND_BY_USER,userID);
    	return lessons;
    }

}
