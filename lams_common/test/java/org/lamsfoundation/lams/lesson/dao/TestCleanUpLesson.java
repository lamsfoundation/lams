/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 4/02/2005
 ******************************************************************************** */

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
