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
package org.lamsfoundation.lams.lesson.dao;

import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolDataAccessTestCase;


/**
 * 
 * @author Jacky Fang 4/02/2005
 * 
 */
public class TestCleanUpLesson extends LessonDataAccessTestCase
{
    /**
     * @param name
     */
    public TestCleanUpLesson(String name)
    {
        super(name);
    }
    
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

    public void testCleanUpLesson()
    {
        List lessons = lessonDao.getAllLessons();
        
        for(Iterator i = lessons.iterator();i.hasNext();)
        {
            Lesson curLesson = (Lesson)i.next();
            if(curLesson.getLessonId().longValue()!=ToolDataAccessTestCase.TEST_LESSON_ID.longValue())
                super.cleanUpLesson(curLesson);
        }
        List cleanedLessons = lessonDao.getAllLessons();
        assertEquals("There should be only one lesson in the db",1,cleanedLessons.size());
    }
}
