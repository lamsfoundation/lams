/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.dao.TestLearnerProgressDAO;


/**
 * 
 * @author Jacky Fang 2005-2-24
 * 
 */
public class TestActivityStrategy extends TestLearnerProgressDAO
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
        testSubOptionsActivityNB = activityDAO.getActivityByActivityId(TEST_NB_ACTIVITY_ID);
        testSubOptionsActivityMC = activityDAO.getActivityByActivityId(TEST_MC_ACTIVITY_ID);
        testSubParallelActivityQA = activityDAO.getActivityByActivityId(TEST_QA_ACTIVITY_ID);
        testSubParallelActivityMB = activityDAO.getActivityByActivityId(TEST_MB_ACTIVITY_ID);
        testSubSeuquencActivitySR = activityDAO.getActivityByActivityId(TEST_SR_ACTIVITY_ID);
        testSubSeuqenceActivityQNA = activityDAO.getActivityByActivityId(TEST_QNA_ACTIVITY_ID);
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
    public TestActivityStrategy(String name)
    {
        super(name);
    }
    
    public void testChildrenCompletedForParallelActivity()
    {
        super.testLearnerProgress.setProgressState(testSubParallelActivityQA,LearnerProgress.ACTIVITY_COMPLETED);
        super.testLearnerProgress.setProgressState(testSubParallelActivityMB,LearnerProgress.ACTIVITY_COMPLETED);
        
        assertTrue("should be completed",this.testParallelActivity.areChildrenCompleted(super.testLearnerProgress));
    }

    public void testChildrenInCompletedForParallelActivity()
    {
        super.testLearnerProgress.setProgressState(testSubParallelActivityQA,LearnerProgress.ACTIVITY_COMPLETED);

        assertTrue("should not be completed",!this.testParallelActivity.areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenCompletedForSequenceActivity()
    {
        super.testLearnerProgress.setProgressState(testSubSeuquencActivitySR,LearnerProgress.ACTIVITY_COMPLETED);
        super.testLearnerProgress.setProgressState(testSubSeuqenceActivityQNA,LearnerProgress.ACTIVITY_COMPLETED);
        assertTrue("should be completed",this.testSequenceActivity.areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenInCompletedForSequenceActivity()
    {
        super.testLearnerProgress.setProgressState(testSubSeuquencActivitySR,LearnerProgress.ACTIVITY_COMPLETED);
        assertTrue("should not be completed",!this.testSequenceActivity.areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenCompletedForOptionsActivity()
    {
        super.testLearnerProgress.setProgressState(testSubOptionsActivityNB,LearnerProgress.ACTIVITY_COMPLETED);
        assertTrue("should be completed",this.testOptionsActivity.areChildrenCompleted(super.testLearnerProgress));
    }
    
    public void testChildrenInCompletedForOptionsActivity()
    {
        assertTrue("should not be completed",!this.testOptionsActivity.areChildrenCompleted(super.testLearnerProgress));
    }
    
}
