/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
package org.lamsfoundation.lams.lesson.dao;

import org.hibernate.HibernateException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.lesson.LessonDataAccessTestCase;


/**
 * 
 * @author Jacky Fang 3/02/2005
 * 
 */
public class TestInitLesson extends LessonDataAccessTestCase
{
    

    /*
     * @see LessonDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /*
     * @see LessonDataAccessTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestInitLesson.
     * @param arg0
     */
    public TestInitLesson(String arg0)
    {
        super(arg0);
    }

    public void testInitLesson() throws HibernateException
    {
        super.initializeTestLesson();
        
        LessonClass createdLessonClass = lessonClassDao.getLessonClass(this.testLessonClass.getGroupingId());
        Lesson createdLesson = lessonDao.getLesson(this.testLesson.getLessonId());

        //checking initialization result of lesson class
        assertLessonClass(createdLessonClass);
        //checking initialization result of lesson
        assertLesson(createdLesson);
        
    }
}
