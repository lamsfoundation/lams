/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 3/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson.dao;

import net.sf.hibernate.HibernateException;

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
