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

package org.lamsfoundation.lams.lesson.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.dao.ILessonClassDAO;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of ILessonDAO
 * 
 * @author chris
 */
@Repository
public class LessonClassDAO extends LAMSBaseDAO implements ILessonClassDAO {

    @Override
    public LessonClass getLessonClass(Long lessonClassId) {
	return (LessonClass) getSession().get(LessonClass.class, lessonClassId);
    }

    /**
     * Saves or Updates a Lesson.
     * 
     * @param lesson
     */
    @Override
    public void saveLessonClass(LessonClass lessonClass) {
	getSession().save(lessonClass);
    }

    /**
     * Deletes a Lesson <b>permanently</b>.
     * 
     * @param lesson
     */
    @Override
    public void deleteLessonClass(LessonClass lessonClass) {
	getSession().delete(lessonClass);
    }

    /**
     * @see org.lamsfoundation.lams.lesson.dao.ILessonClassDAO#updateLessonClass(org.lamsfoundation.lams.lesson.LessonClass)
     */
    @Override
    public void updateLessonClass(LessonClass lessonClass) {
	getSession().update(lessonClass);

    }

}
