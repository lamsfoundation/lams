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


import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

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
    public void saveLessonClass(LessonClass lessonClass)
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

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonClassDAO#updateLessonClass(org.lamsfoundation.lams.lesson.LessonClass)
     */
    public void updateLessonClass(LessonClass lessonClass)
    {
        getHibernateTemplate().update(lessonClass);
        
    }
    
}
