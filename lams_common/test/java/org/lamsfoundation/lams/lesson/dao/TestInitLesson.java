/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 3/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson.dao;

import org.lamsfoundation.lams.lesson.LessonDataAccessTestCase;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonClassDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;


/**
 * 
 * @author Jacky Fang 3/02/2005
 * 
 */
public class TestInitLesson extends LessonDataAccessTestCase
{
    
    private ILessonDAO lessonDao;
    private ILessonClassDAO lessonClassDao;
    /*
     * @see LessonDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        lessonDao = (LessonDAO)this.ac.getBean("lessonDAO");
        lessonClassDao = (LessonClassDAO)this.ac.getBean("lessonClassDAO");
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

    public void testInitLesson()
    {
        super.initLessonData();
        super.initLessonClassData();
        lessonClassDao.saveLessonClass(this.testLessonClass);
        lessonDao.saveLesson(testLesson);
        assertEquals("",1,1);
    }
}
