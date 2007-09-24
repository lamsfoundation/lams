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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.lesson.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class LearnerProgressDAO extends HibernateDaoSupport implements ILearnerProgressDAO
{

	protected Logger log = Logger.getLogger(LearnerProgressDAO.class);	

	private final static String LOAD_PROGRESS_BY_LEARNER = 
        "from LearnerProgress p where p.user.id = :learnerId and p.lesson.id = :lessonId";
    private final static String LOAD_PROGRESS_BY_ACTIVITY = 
        "from LearnerProgress p where p.previousActivity = :activity or p.currentActivity = :activity or p.nextActivity = :activity ";
   // +
   // 	"or activity in elements(p.previousActivity) or activity in elements(p.completedActivities)";
	private final static String LOAD_COMPLETED_PROGRESS_BY_LESSON = 
        "from LearnerProgress p where p.lessonComplete = true and p.lesson.id = :lessonId";

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
     * @see org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO#getLearnerProgressByLeaner(java.lang.Integer,java.lang.Long)
     */
    public LearnerProgress getLearnerProgressByLearner(final Integer learnerId, final Long lessonId)
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (LearnerProgress)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                     return session.createQuery(LOAD_PROGRESS_BY_LEARNER)
                     			   .setInteger("learnerId",learnerId)
                     			   .setLong("lessonId",lessonId)
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
    
    /**
     * Get all the learner progress records where the current, previous or next activity is the given activity.
     * @param activity
     * @return List<LearnerProgress>
     */
    public List getLearnerProgressReferringToActivity(final Activity activity) 
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (List)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                     return session.createQuery(LOAD_PROGRESS_BY_ACTIVITY)
                     	.setEntity("activity",activity)
                     	.list();
                 }
             }
       );     
    }

    /**
     * Get all the learner progress records for a lesson where the progress is marked as completed.
     * @param lessonId
     * @return List<LearnerProgress>
     */
    public List getCompletedLearnerProgressForLesson(final Long lessonId) 
    {
        HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());

        return (List)hibernateTemplate.execute(
             new HibernateCallback() 
             {
                 public Object doInHibernate(Session session) throws HibernateException 
                 {
                     return session.createQuery(LOAD_COMPLETED_PROGRESS_BY_LESSON)
                     	.setLong("lessonId",lessonId)
                     	.list();
                 }
            }
       );     
    }

    /**
     * Get all the users records where the user has attempted the given activity. Uses the progress records
     * to determine the users.
     * 
     * @param activityId
     * @return List<User>
     */
	@SuppressWarnings("unchecked")
	public List<User> getLearnersHaveAttemptedActivity(final Activity activity) 
    {
		List<User> learners = null;
		
	    HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	    learners = (List<User>) hibernateTemplate.execute(
	            new HibernateCallback() {
	                public Object doInHibernate(Session session) throws HibernateException {
	        	    	return session.getNamedQuery("usersAttemptedActivity")
	        	    		.setLong("activityId", activity.getActivityId().longValue())
	        	    		.list();
	                }
	            }
	        );
	    
	    return learners;
    }
	
	/**
     * Get all the users records where the user has completed the given activity. Uses the progress records
     * to determine the users.
     * 
     * @param activityId
     * @return List<User>
     */
	@SuppressWarnings("unchecked")
	public List<User> getLearnersHaveCompletedActivity(final Activity activity) 
    {
		List<User> learners = null;
		
	    HibernateTemplate hibernateTemplate = new HibernateTemplate(this.getSessionFactory());
	    learners = (List<User>) hibernateTemplate.execute(
	            new HibernateCallback() {
	                public Object doInHibernate(Session session) throws HibernateException {
	        	    	return session.getNamedQuery("usersCompletedActivity")
	        	    		.setLong("activityId", activity.getActivityId().longValue())
	        	    		.list();
	                }
	            }
	        );
	    
	    return learners;
    }
}
