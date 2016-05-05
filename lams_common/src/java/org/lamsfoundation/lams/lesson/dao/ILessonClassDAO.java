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
package org.lamsfoundation.lams.lesson.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.lesson.LessonClass;

/**
 * Inteface defines Lesson DAO Methods
 * 
 * @author chris
 */
public interface ILessonClassDAO extends IBaseDAO {

    /**
     * Retrieves the Lesson. Maybe a Hibernate Proxy that requires lazy loading to get to data.
     * 
     * @param lessonId
     *            identifies the lesson to get
     * @return the lesson
     */
    public LessonClass getLessonClass(Long lessonClassId);

    /**
     * Saves a new Lesson.
     * 
     * @param lesson
     *            the Lesson to save
     */
    public void saveLessonClass(LessonClass lessonClass);

    /**
     * Update an existing lesson class.
     * 
     * @param lessonClass
     */
    public void updateLessonClass(LessonClass lessonClass);

    /**
     * Deletes a Lesson <b>permanently</b>.
     * 
     * @param lesson
     *            the Lesson to remove.
     */
    public void deleteLessonClass(LessonClass lessonClass);

}
