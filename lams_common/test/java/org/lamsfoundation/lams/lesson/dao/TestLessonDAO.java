/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.lesson.dao;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.LessonDataAccessTestCase;
import org.springframework.orm.hibernate3.HibernateSystemException;


/**
 * 
 * @author Jacky Fang 2/02/2005
 */
public class TestLessonDAO extends LessonDataAccessTestCase
{
    private static final Long TEST_EXT_LESSON_ID = new Long(1);
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
    
    public void testGetLessonWithJoinFetchedProgress()
    {
        Lesson createdLesson = this.lessonDao.getLessonWithJoinFetchedProgress(this.testLesson.getLessonId());
        
        //assert the data retrieved.
        super.assertLesson(createdLesson);
        
    }

    public void testGetExistingLesson()
    {
        Lesson existingLesson = this.lessonDao.getLesson(TEST_EXT_LESSON_ID);
        
        assertNotNull(existingLesson);
    }
    
    public void testGetActiveLessonsForLearner()
    {
        List lessons = this.lessonDao.getActiveLessonsForLearner(this.testUser);
        
        assertEquals("verify the number of lesson we get",0,lessons.size());
    }
    
    public void testGetActiveLearnersByLesson()
    {
        List learners = this.lessonDao.getActiveLearnerByLesson(2);
        assertNotNull(learners);
    }
    
    public void testUpdateLesson()
    {
        Lesson createdLesson = this.lessonDao.getLesson(this.testLesson.getLessonId());
        assertEquals("should be created state now",Lesson.CREATED,createdLesson.getLessonStateId());
        assertNull("ensure we are testing right lesson",createdLesson.getScheduleEndDate());
        
        createdLesson.setLessonStateId(Lesson.STARTED_STATE);
        createdLesson.setScheduleEndDate(new Date(System.currentTimeMillis()+60));
        lessonDao.updateLesson(createdLesson);
        
        Lesson updatedLesson = lessonDao.getLesson(testLesson.getLessonId());
        
        assertEquals("verify the updated lesson state",Lesson.STARTED_STATE,updatedLesson.getLessonStateId());
        assertNotNull("verify the sechdule end date",updatedLesson.getScheduleEndDate());
        
        List lessons = this.lessonDao.getActiveLessonsForLearner(this.testUser);
        
        assertEquals("verify the number of lesson we get",1,lessons.size());
    }
    
    public void testCreateLessonWithoutName()
    {
        Lesson lessonWithoutName = Lesson.createNewLessonWithoutClass(null,
                                                                      null,
                                                                      testUser,
                                                                      testLearningDesign);
        
        try
        {
            this.lessonDao.saveLesson(lessonWithoutName);
            fail("we are meant to get failure of inserting a lesson without name");
        }
        catch (HibernateSystemException e)
        {
            assertTrue("excpetion expected.",true);
            this.setShouldFlush(false);
        }
    }
}
