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
package org.lamsfoundation.lams.learningdesign;

import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.ParallelWaitActivity;
import org.lamsfoundation.lams.lesson.dao.TestLearnerProgressDAO;


/**
 * 
 * @author Jacky Fang 2005-2-24
 * 
 */
public class TestComplexActivityStrategy extends TestLearnerProgressDAO
{

    private Activity testSubOptionsActivityNB;
    private Activity testSubOptionsActivityMC;
    private Activity testSubParallelActivityQA;
    private Activity testSubParallelActivityMB;
    private Activity testSubSeuquencActivitySR;
    private Activity testSubSeuqenceActivityQNA;
    
    //this is survey id we inserted in test data sql script
    protected static final Long TEST_NB_ACTIVITY_ID = new Long(16);
    protected static final Long TEST_MC_ACTIVITY_ID = new Long(17);
    protected static final Long TEST_QA_ACTIVITY_ID = new Long(18);
    protected static final Long TEST_MB_ACTIVITY_ID = new Long(19);
    protected static final Long TEST_SR_ACTIVITY_ID = new Long(22);
    protected static final Long TEST_QNA_ACTIVITY_ID = new Long(25);
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
     * Constructor for TestActivityStrategy.
     * @param arg0
     */
    public TestComplexActivityStrategy(String name)
    {
        super(name);
    }
    
    public void testChildrenCompletedForParallelActivity()
    {
        testSubParallelActivityQA = activityDAO.getActivityByActivityId(TEST_QA_ACTIVITY_ID);
        testSubParallelActivityMB = activityDAO.getActivityByActivityId(TEST_MB_ACTIVITY_ID);
        
        super.testLearnerProgress.setProgressState(testSubParallelActivityQA,LearnerProgress.ACTIVITY_COMPLETED);
        super.testLearnerProgress.setProgressState(testSubParallelActivityMB,LearnerProgress.ACTIVITY_COMPLETED);
        
        assertTrue("should be completed",((ComplexActivity)testParallelActivity).areChildrenCompleted(super.testLearnerProgress));
    }

    public void testChildrenInCompletedForParallelActivity()
    {
        testSubParallelActivityQA = activityDAO.getActivityByActivityId(TEST_QA_ACTIVITY_ID);
       
        super.testLearnerProgress.setProgressState(testSubParallelActivityQA,LearnerProgress.ACTIVITY_COMPLETED);

        assertTrue("should not be completed",!((ComplexActivity)testParallelActivity).areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenCompletedForSequenceActivity()
    {
        testSubSeuquencActivitySR = activityDAO.getActivityByActivityId(TEST_SR_ACTIVITY_ID);
        testSubSeuqenceActivityQNA = activityDAO.getActivityByActivityId(TEST_QNA_ACTIVITY_ID);

        
        super.testLearnerProgress.setProgressState(testSubSeuquencActivitySR,LearnerProgress.ACTIVITY_COMPLETED);
        super.testLearnerProgress.setProgressState(testSubSeuqenceActivityQNA,LearnerProgress.ACTIVITY_COMPLETED);
        assertTrue("should be completed",((ComplexActivity)testSequenceActivity).areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenInCompletedForSequenceActivity()
    {
        testSubSeuquencActivitySR = activityDAO.getActivityByActivityId(TEST_SR_ACTIVITY_ID);
        
        super.testLearnerProgress.setProgressState(testSubSeuquencActivitySR,LearnerProgress.ACTIVITY_COMPLETED);
        assertTrue("should not be completed",!((ComplexActivity)testSequenceActivity).areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenCompletedForOptionsActivity()
    {
        testSubOptionsActivityNB = activityDAO.getActivityByActivityId(TEST_NB_ACTIVITY_ID);
        testSubOptionsActivityMC = activityDAO.getActivityByActivityId(TEST_MC_ACTIVITY_ID);
        
        super.testLearnerProgress.setProgressState(testSubOptionsActivityNB,LearnerProgress.ACTIVITY_COMPLETED);
        assertTrue("should be completed",((ComplexActivity)testOptionsActivity).areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenInCompletedForOptionsActivity()
    {
        assertTrue("should not be completed",!((ComplexActivity)testOptionsActivity).areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testGetNextActivityBySequenceParentActivity()
    {
        testSubSeuquencActivitySR = activityDAO.getActivityByActivityId(TEST_SR_ACTIVITY_ID);
        testSubSeuqenceActivityQNA = activityDAO.getActivityByActivityId(TEST_QNA_ACTIVITY_ID);
        
        Activity nextActivity = testSequenceActivity.getNextActivityByParent(testSubSeuquencActivitySR);
        
        assertNotNull("we should have a next activity",nextActivity);
        assertEquals("it should be qna",this.testSubSeuqenceActivityQNA.getActivityId().longValue(),nextActivity.getActivityId().longValue());
    }
    
    public void testGetFirstActivityWithinSequenceParentActivity()
    {
        testSubSeuquencActivitySR = activityDAO.getActivityByActivityId(TEST_SR_ACTIVITY_ID);

        Activity nextActivity = ((ComplexActivity)testSequenceActivity).getNextActivityByParent(new NullActivity());
        
        assertNotNull("we should have a next activity",nextActivity);
        assertEquals("it should be share resource",this.testSubSeuquencActivitySR.getActivityId().longValue(),nextActivity.getActivityId().longValue());
    }
    
    
    public void testGetNextActivityByOptionsParentActivity()
    {
        testSubOptionsActivityNB = activityDAO.getActivityByActivityId(TEST_NB_ACTIVITY_ID);

        Activity nextActivity = ((ComplexActivity)testOptionsActivity).getNextActivityByParent(testSubOptionsActivityNB);

        assertNotNull("we should have a next activity",nextActivity);
        assertEquals("it should be option activity itself",
                     this.testOptionsActivity.getActivityId().longValue(),
                     nextActivity.getActivityId().longValue());
    }
    
    public void testGetNextActivityByParallelParentActivity()
    {
        testSubParallelActivityQA = activityDAO.getActivityByActivityId(TEST_QA_ACTIVITY_ID);

        Activity nextActivity = ((ComplexActivity)testParallelActivity).getNextActivityByParent(testSubParallelActivityQA);
        
        assertNotNull("we should have a next activity",nextActivity);

        assertTrue("It should be kind of null activity",nextActivity.isNull());
        assertTrue("It should waiting activity",nextActivity.getActivityTypeId().intValue()==ParallelWaitActivity.PARALLEL_WAIT_ACTIVITY_TYPE);

    }
}
