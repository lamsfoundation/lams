/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class LessonDAO extends HibernateDaoSupport implements ILessonDAO
{
	private final static String FIND_LESSON_BY_CREATOR="from "
					+ Lesson.class.getName()
					+ " lesson where lesson.user.userId=? and lesson.lessonStateId <= 6 and "
					+" lesson.learningDesign.copyTypeID="
					+ LearningDesign.COPY_TYPE_LESSON;
	private final static String FIND_PREVIEW_BEFORE_START_DATE=	"from "
					+ Lesson.class.getName()
					+ " lesson where lesson.learningDesign.copyTypeID="
					+ LearningDesign.COPY_TYPE_PREVIEW 
					+ "and lesson.startDateTime is not null and lesson.startDateTime < ?";
	
    /**
     * Retrieves the Lesson
     * @param lessonId identifies the lesson to get
     * @return the lesson
     */
    public Lesson getLesson(Long lessonId)
    {
        return (Lesson)getHibernateTemplate().get(Lesson.class, lessonId);
    }
    
    
    public Lesson getLessonWithJoinFetchedProgress(final Long lessonId)
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (Lesson)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                     return session.createCriteria(Lesson.class)
                     			   .add(Expression.like("lessonId",lessonId))
                     			   .setFetchMode("learnerProgresses",FetchMode.JOIN)
                     			   .uniqueResult();
                 }
             }
       ); 
    }
  
    /** Get all the lessons in the database. This includes the disabled lessons. */
    public List getAllLessons()
    {
        return getHibernateTemplate().loadAll(Lesson.class);
    }
    
     /**
     * Gets all lessons that are active for a learner.
     * TODO remove when the dummy interface is no longer needed. Replaced with getActiveLessonsForLearner(user,org)
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
        	    	Query query = session.getNamedQuery("activeLessonsAllOrganisations");
        	    	query.setInteger("userId", learner.getUserId().intValue());
        	    	List result = query.list();
                    return result;
                }
            }
        );
        return lessons;
    }
    
    /**
     * Gets all lessons that are active for a learner, in a given organisation
     * @param learnerId a User that identifies the learner.
     * @param organisationId the desired organisation.
     * @return a List with all active lessons in it.
     */
    public List getActiveLessonsForLearner(final Integer learnerId, final Integer organisationId)
    {
    	List lessons = null;
    	
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
    	lessons = (List)hibernateTemplate.execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
        	    	Query query = session.getNamedQuery("activeLessons");
        	    	query.setInteger("userId", learnerId);
        	    	query.setInteger("organisationId", organisationId);
        	    	List result = query.list();
                    return result;
                }
            }
        );
        return lessons;
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonDAO#getActiveLearnerByLesson(long)
     */
    public List getActiveLearnerByLesson(final long lessonId)
    {
    	List learners = null;
    	
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
        learners = (List)hibernateTemplate.execute(
            new HibernateCallback() {
                public Object doInHibernate(Session session) throws HibernateException {
        	    	Query query = session.getNamedQuery("activeLearners");
        	    	query.setLong("lessonId", lessonId);
        	    	List result = query.list();
                    return result;
                }
            }
        );
        return learners;
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
    
    /**
     * Returns the list of available Lessons created by
     * a given user. Does not return disabled lessons or preview lessons.
     * 
     * @param userID The user_id of the user
     * @return List The list of Lessons for the given user
     */
   public List getLessonsCreatedByUser(Integer userID){
    	List lessons = this.getHibernateTemplate().find(FIND_LESSON_BY_CREATOR,userID);
    	return lessons;
    }
   
   /**
    * Gets all lessons for which this user is in the staff group. Does not return 
    * disabled lessons or preview lessons. This is the list of lessons that a user may monitor/moderate/manage.
    * TODO remove once the dummy interface is removed - it is replaced by getLessonsForMonitoring(userId, organisationId)
    * TODO remove the matching query in the Hibernate file.
    * @param user a User that identifies the teacher/staff member.
    * @return a List with all appropriate lessons in it.
    */
   public List getLessonsForMonitoring(final int userID)
   {
	   List lessons = null;
   	
       HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
   		lessons = (List)hibernateTemplate.execute(
           new HibernateCallback() {
               public Object doInHibernate(Session session) throws HibernateException {
       	    	Query query = session.getNamedQuery("lessonsForMonitoringByUser");
       	    	query.setInteger("userId", userID);
       	    	List result = query.list();
                   return result;
               }
           }
       );
       return lessons;
   }
   /**
    * Gets all lessons in the given organisation, for which this user is in the staff group. Does not return 
    * disabled lessons or preview lessons. This is the list of lessons that a user may monitor/moderate/manage.
    * @param user a User that identifies the teacher/staff member.
    * @return a List with all appropriate lessons in it.
    */
   public List getLessonsForMonitoring(final int userID, final int organisationID)
   {
	   List lessons = null;
   	
       HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
   		lessons = (List)hibernateTemplate.execute(
           new HibernateCallback() {
               public Object doInHibernate(Session session) throws HibernateException {
       	    	Query query = session.getNamedQuery("lessonsForMonitoring");
       	    	query.setInteger("userId", userID);
       	    	query.setInteger("organisationId", organisationID);
       	    	List result = query.list();
                   return result;
               }
           }
       );
       return lessons;
   }
   /**
    * Get all the preview lessons more with the creation date before the given date.
    * 
    * @param startDate UTC date 
    * @return the list of Lessons
    */
   public List getPreviewLessonsBeforeDate(final Date startDate){
   	List lessons = this.getHibernateTemplate().find(FIND_PREVIEW_BEFORE_START_DATE,startDate);
	return lessons;
   }

}
