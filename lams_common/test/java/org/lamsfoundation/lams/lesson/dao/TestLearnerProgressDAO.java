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

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.LessonDataAccessTestCase;


/**
 * 
 * @author Jacky Fang 2005-2-17
 * 
 */
public class TestLearnerProgressDAO extends LessonDataAccessTestCase
{
    private IActivityDAO activityDAO;
    private Activity testToolActivity;
    private Activity testComplexActivity;
    
    //this is survey id we inserted in test data sql script
    private static final Long TEST_TOOL_ACTIVITY_ID = new Long(20);
    private static final Long TEST_COMPLEX_ACTIVITY_ID = new Long(13);
    /*
     * @see LessonDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        super.initializeTestLesson();
        super.initLearnerProgressData();
        
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
		testToolActivity = activityDAO.getActivityByActivityId(TEST_TOOL_ACTIVITY_ID);
		testComplexActivity = activityDAO.getActivityByActivityId(TEST_COMPLEX_ACTIVITY_ID);
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

        Activity firstActivity = this.testLesson.getLearningDesign().getFirstActivity();
        
        progress.setProgressState(firstActivity,LearnerProgress.ACTIVITY_COMPLETED);
        progress.setProgressState(testComplexActivity,LearnerProgress.ACTIVITY_COMPLETED);
        progress.setProgressState(testToolActivity,LearnerProgress.ACTIVITY_ATTEMPTED);
        
        super.learnerProgressDao.updateLearnerProgress(progress);
        
        LearnerProgress updatedProgress= learnerProgressDao.getLearnerProgressByLeaner(testUser,testLesson);
        assertLearnerProgressInitialization(progress);
        assertEquals("verify completed activity",2,updatedProgress.getCompletedActivities().size());
        assertEquals("verify attempted activity",1,updatedProgress.getAttemptedActivities().size());
        
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
