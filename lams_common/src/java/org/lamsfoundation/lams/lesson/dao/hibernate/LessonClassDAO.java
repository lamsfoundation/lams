/*
 * LessonDAO.java
 *
 * Created on 13 January 2005, 10:32
 */

package org.lamsfoundation.lams.lesson.dao.hibernate;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;

/**
 * Hibernate implementation of ILessonDAO
 * @author chris
 */
public class LessonClassDAO extends HibernateDaoSupport implements ILessonClassDAO
{
    
    public LessonClass getLessonClass(Long lessonClassId)
    {
        return (LessonClass)getHibernateTemplate().get(LessonClass.class, lessonClassId);
    }
    
    /**
     * Saves or Updates a Lesson.
     * @param lesson
     */
    public void saveOrUpdateLessonClass(LessonClass lessonClass)
    {
        getHibernateTemplate().save(lessonClass);
    }
    
    /**
     * Deletes a Lesson <b>permanently</b>.
     * @param lesson
     */
    public void deleteLessonClass(LessonClass lessonClass)
    {
        getHibernateTemplate().delete(lessonClass);
    }
    
}
