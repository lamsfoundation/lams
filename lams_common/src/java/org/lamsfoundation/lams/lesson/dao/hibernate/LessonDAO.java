/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao.hibernate;

import java.util.List;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.HibernateTemplate;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.usermanagement.User;
/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class LessonDAO extends HibernateDaoSupport implements ILessonDAO
{
    
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public Lesson getLesson(Long lessonId)
    {
        return (Lesson)getHibernateTemplate().get(Lesson.class, lessonId);
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
    	/*Session session = this.getSession();
    	try {
	    	Query query = session.getNamedQuery("activeLessons");
	    	query.setInteger("userId", learner.getUserId().intValue());
	    	lessons = query.list();
    	}
    	catch (HibernateException e) {
    		throw new DataRetrievalFailureException(e.getMessage(), e);
    	}*/
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
     * Retrieves the LearnerProgress
     * @param learner the User in the Lesson
     * @param lesson the Lesson
     * @return LearnerProgess object containing the progress and state data.
     */
    public LearnerProgress getLearnerProgress(User learner, Lesson lesson)
    {
        String queryString = "from LearnerProgress l where l.user = ? and l.lesson ! = ?";
        return (LearnerProgress) ((getHibernateTemplate().find(queryString, new Object[]{learner, lesson})).get(0));//shoud only ever be one of these   
    }

}
