/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson.dao;

import java.util.List;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonDataAccessTestCase;

import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang 2/02/2005
 */
public class TestLessonDAO extends LessonDataAccessTestCase
{
    
    /**
     * @param name
     */
    public TestLessonDAO(String name)
    {
        super(name);
    }
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        super.initializeTestLesson();
    }
    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.cleanUpTestLesson();
        super.tearDown();

    }
    
    public void testGetLessonWithEagerlyFetchedProgress()
    {
        Lesson createdLesson = this.lessonDao.getLessonWithEagerlyFetchedProgress(this.testLesson.getLessonId());
        
        //assert the data retrieved.
        super.assertLesson(createdLesson);
        
    }

    public void testGetActiveLessonsForLearner()
    {
        List lessons = this.lessonDao.getActiveLessonsForLearner(this.testUser);
        
        assertEquals("verify the number of lesson we get",0,lessons.size());
    }
    
    public void testUpdateLesson()
    {
        Lesson createdLesson = this.lessonDao.getLesson(this.testLesson.getLessonId());
        
        assertEquals("should be created state now",Lesson.CREATED,createdLesson.getLessonStateId());
        createdLesson.setLessonStateId(Lesson.STARTED_STATE);
        
        lessonDao.updateLesson(createdLesson);
        
        Lesson updatedLesson = lessonDao.getLesson(testLesson.getLessonId());
        
        assertEquals("verify the updated lesson state",Lesson.STARTED_STATE,updatedLesson.getLessonStateId());
    
        List lessons = this.lessonDao.getActiveLessonsForLearner(this.testUser);
        
        assertEquals("verify the number of lesson we get",1,lessons.size());

    }
}
