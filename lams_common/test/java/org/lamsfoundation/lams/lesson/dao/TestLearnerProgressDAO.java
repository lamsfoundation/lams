/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2005-2-17
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson.dao;

import net.sf.hibernate.HibernateException;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LessonDataAccessTestCase;


/**
 * 
 * @author Jacky Fang 2005-2-17
 * 
 */
public class TestLearnerProgressDAO extends LessonDataAccessTestCase
{

    /*
     * @see LessonDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        super.initializeTestLesson();
        super.initLearnerProgressData();
    }

    /*
     * @see LessonDataAccessTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        super.cleanUpTestLesson();
    }

    /**
     * Constructor for TestLearnerProgressDAO.
     * @param testName
     */
    public TestLearnerProgressDAO(String testName)
    {
        super(testName);
    }

    public void testGetNullLearnerProgressByLearner()
    {
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLeaner(testUser,testLesson);
        assertNull("should not get any progress data",progress);
    }

    public void testSaveLearnerProgress()
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);
        
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLeaner(testUser,testLesson);
        assertLearnerProgressInitialization(progress);
    }

    public void testGetLearnerProgressByLeaner()
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLeaner(testUser,testLesson);
        assertLearnerProgressInitialization(progress);
    }
    
    public void testGetLearnerProgress()
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);

        LearnerProgress progress = learnerProgressDao.getLearnerProgress(testLearnerProgress.getLearnerProgressId());
        assertLearnerProgressInitialization(progress);
    }


    public void testDeleteLearnerProgress() throws HibernateException
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);

        LearnerProgress progress = learnerProgressDao.getLearnerProgress(testLearnerProgress.getLearnerProgressId());
        assertLearnerProgressInitialization(progress);
        
        super.learnerProgressDao.deleteLearnerProgress(testLearnerProgress);
        super.getSession().flush();
        LearnerProgress nullProgress= learnerProgressDao.getLearnerProgressByLeaner(testUser,testLesson);
        assertNull("should not get any progress data",nullProgress);

    }

    public void testUpdateLearnerProgress()
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLeaner(testUser,testLesson);

        
        
        
    }

    /**
     * @param progress
     */
    private void assertLearnerProgressInitialization(LearnerProgress progress)
    {
        assertNotNull("There should be a learner progress",progress);
        assertEquals("verify user",progress.getUser().getUserId(),testUser.getUserId());
        assertEquals("verify Lesson",progress.getLesson().getLessonId(),testLesson.getLessonId());
    }
}
