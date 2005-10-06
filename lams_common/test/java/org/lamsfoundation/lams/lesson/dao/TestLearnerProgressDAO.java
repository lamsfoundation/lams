/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2005-2-17
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson.dao;

import org.hibernate.HibernateException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
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
    protected IActivityDAO activityDAO;
    protected ToolActivity testToolActivity;
    protected ParallelActivity testParallelActivity;
    protected OptionsActivity testOptionsActivity;
    protected SequenceActivity testSequenceActivity;
    
    //this is survey id we inserted in test data sql script
    protected static final Long TEST_TOOL_ACTIVITY_ID = new Long(15);
    protected static final Long TEST_PARALLEL_ACTIVITY_ID = new Long(13);
    protected static final Long TEST_OPTIONS_ACTIVITY_ID = new Long(12);
    protected static final Long TEST_SEQUENCE_ACTIVITY_ID = new Long(14);
    /*
     * @see LessonDataAccessTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        super.initializeTestLesson();
        super.initLearnerProgressData();
        
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
		testToolActivity = (ToolActivity) activityDAO.getActivityByActivityId(TEST_TOOL_ACTIVITY_ID);
		testParallelActivity = (ParallelActivity) activityDAO.getActivityByActivityId(TEST_PARALLEL_ACTIVITY_ID);
		testOptionsActivity = (OptionsActivity) activityDAO.getActivityByActivityId(TEST_OPTIONS_ACTIVITY_ID);
		testSequenceActivity = (SequenceActivity) activityDAO.getActivityByActivityId(TEST_SEQUENCE_ACTIVITY_ID);
    }

    /*
     * @see LessonDataAccessTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.cleanUpTestLesson();
        super.tearDown();

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
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        assertNull("should not get any progress data",progress);
    }

    
    public void testSaveLearnerProgress() throws HibernateException
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);
        
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        assertLearnerProgressInitialization(progress);
        
        super.learnerProgressDao.deleteLearnerProgress(testLearnerProgress);
        super.getSession().flush();
    }
    
    public void testDeleteLearnerProgress() throws HibernateException
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);

        LearnerProgress progress = learnerProgressDao.getLearnerProgress(testLearnerProgress.getLearnerProgressId());
        assertLearnerProgressInitialization(progress);
        
        super.learnerProgressDao.deleteLearnerProgress(testLearnerProgress);
        super.getSession().flush();
        LearnerProgress nullProgress= learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        assertNull("should not get any progress data",nullProgress);

    }
    public void testGetLearnerProgressByLeaner() throws HibernateException
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        assertLearnerProgressInitialization(progress);
        super.learnerProgressDao.deleteLearnerProgress(testLearnerProgress);
        super.getSession().flush();
    }
    
    public void testGetLearnerProgress() throws HibernateException
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);

        LearnerProgress progress = learnerProgressDao.getLearnerProgress(testLearnerProgress.getLearnerProgressId());
        assertLearnerProgressInitialization(progress);
        
        super.learnerProgressDao.deleteLearnerProgress(testLearnerProgress);
        super.getSession().flush();
    }




    public void testUpdateLearnerProgress() throws HibernateException
    {
        super.learnerProgressDao.saveLearnerProgress(this.testLearnerProgress);
        LearnerProgress progress= learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);

        Activity firstActivity = this.testLesson.getLearningDesign().getFirstActivity();
        
        progress.setProgressState(firstActivity,LearnerProgress.ACTIVITY_COMPLETED);
        progress.setProgressState(testParallelActivity,LearnerProgress.ACTIVITY_COMPLETED);
        progress.setProgressState(testToolActivity,LearnerProgress.ACTIVITY_ATTEMPTED);
        progress.setRestarting(true);
        
        super.learnerProgressDao.updateLearnerProgress(progress);
        
        LearnerProgress updatedProgress= learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        assertLearnerProgressInitialization(progress);
        assertEquals("verify completed activity",2,updatedProgress.getCompletedActivities().size());
        assertEquals("verify attempted activity",1,updatedProgress.getAttemptedActivities().size());
        assertTrue("is restarting",updatedProgress.isRestarting());
        super.learnerProgressDao.deleteLearnerProgress(testLearnerProgress);
        super.getSession().flush();
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
