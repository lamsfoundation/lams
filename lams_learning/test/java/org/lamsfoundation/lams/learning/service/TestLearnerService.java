/*
	Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
    USA

    http://www.gnu.org/licenses/gpl.txt
*/
package org.lamsfoundation.lams.learning.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.AbstractLamsTestCase;

import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolSessionDAO;
import org.lamsfoundation.lams.tool.service.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;


/**
 * 
 * @author Jacky Fang 2005-2-22
 * 
 */
public class TestLearnerService extends AbstractLamsTestCase
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestLearnerService.class);
	
    private ILearnerService learnerService;
    private IUserManagementService usermanageService;
    private ILessonDAO lessonDao; 
    private ILearnerProgressDAO learnerProgressDao;
    private IToolSessionDAO toolSessionDao;
    //---------------------------------------------------------------------
    // Testing Data - Constants
    //---------------------------------------------------------------------
    private final Integer TEST_USER_ID = new Integer(2);
    private final Long Test_Lesson_ID = new Long(2);
    private final long TEST_TOOL_SESSION_ID = 1;
    //---------------------------------------------------------------------
    // Testing Data - Instance Variables
    //---------------------------------------------------------------------
    private User testUser;
    private Lesson testLesson;
    private static LearnerProgress testProgress;
    private static final long TEST_SYNCHGATE_ACTIVITY_ID = 31;
    private static final long TEST_RGRP_ACTIVITY_ID = 23;
    private static final long TEST_CNB_ACTIVITY_ID = 33;
    private static final long TEST_CQNA_ACTIVITY_ID = 36;
    private static final long TEST_MB_ACTIVITY_ID = 37;
    
    private static final long TEST_SYNCHGATE_ACTIVITY_UIID = 26;
    private static final long TEST_NOTEBOOK_ACTIVITY_UIID = 1;
    private static final long TEST_RGRP_ACTIVITY_UIID = 23;
    private static final long TEST_CHAT_ACTIVITY_UIID = 7;
    private static final long TEST_QNA_ACTIVITY_UIID = 24;
    private static final long TEST_OPTIONS_ACTIVITY_UIID = 12;
    private static final long TEST_CNB_ACTIVITY_UIID = 2;
    private static final long TEST_PARALLEL_ACTIVITY_UIID = 13;
    private static final long TEST_CQNA_ACTIVITY_UIID = 4;
    private static final long TEST_WAITING_ACTIVITY_UIID = -1;
    private static final long TEST_MB_ACTIVITY_UIID = 5;
    private static final long TEST_SEQUENCE_ACTIVITY_UIID = 14;
    private static final long TEST_SR_ACTIVITY_UIID = 8;
    private static final long TEST_SQNA_ACTIVITY_UIID = 25;
    private static final String HOST="http://localhost:8080/lams_learning";
    private static final String LOAD_TOOL_URL="/LoadToolActivity.do";
    private static final String GATE_URL="/gate.do";
    private static final String PARAM_ACTIVITY_ID="?activityId=";
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        learnerService = (ILearnerService)this.context.getBean("learnerService");
        usermanageService = (IUserManagementService)this.context.getBean("userManagementService");
        lessonDao = (LessonDAO)this.context.getBean("lessonDAO");
        learnerProgressDao = (LearnerProgressDAO)this.context.getBean("learnerProgressDAO");
        toolSessionDao = (ToolSessionDAO)this.context.getBean("toolSessionDAO");
        
        testUser = usermanageService.getUserById(TEST_USER_ID);
        testLesson = lessonDao.getLesson(Test_Lesson_ID);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestLearnerService.
     * @param name
     */
    public TestLearnerService(String name)
    {
        super(name);
    }
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
  			  				  "/org/lamsfoundation/lams/tool/toolApplicationContext.xml",
  			  				"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
                              "/org/lamsfoundation/lams/tool/survey/dataAccessContext.xml",
                              "/org/lamsfoundation/lams/tool/survey/surveyApplicationContext.xml",          					  
        					  "applicationContext.xml",
    			  			  "/WEB-INF/spring/learningApplicationContext.xml"};
    }
    
    public void testJoinLesson() throws ProgressException,LamsToolServiceException
    {
        learnerService.joinLesson(testUser,testLesson);
        
        testProgress=learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        
        assertNotNull(testProgress);
        assertNotNull("verify next activity",testProgress.getNextActivity());
        assertEquals("verify id of next activity-survey",6,testProgress.getNextActivity().getActivityUIID().intValue());
        assertNotNull("verify current activity",testProgress.getCurrentActivity());
        assertEquals("verify id of current activity-survey",6,testProgress.getCurrentActivity().getActivityUIID().intValue());
        assertEquals("verify attempted activity",1,testProgress.getAttemptedActivities().size());
        assertEquals("verify completed activity",0,testProgress.getCompletedActivities().size());
        assertNotNull("verify correspondent tool session for next activity",
                      ((ToolActivity)testProgress.getNextActivity()).getToolSessions());
        assertEquals("verify number of tool sessions created",1,
                     ((ToolActivity)testProgress.getNextActivity()).getToolSessions().size());
    }


    public void testCompleteToolSession()
    {
        String urlForNextActivity = learnerService.completeToolSession(new Long(TEST_TOOL_SESSION_ID),testUser);
        
        ToolSession toolSession = toolSessionDao.getToolSession(new Long(TEST_TOOL_SESSION_ID));
        
        assertNotNull("verify the existance of tool session",toolSession);
        assertEquals("verify tool session state",ToolSession.ENDED_STATE,toolSession.getToolSessionStateId());
        
        assertEquals("verify the returned url",HOST+GATE_URL+PARAM_ACTIVITY_ID+TEST_SYNCHGATE_ACTIVITY_ID+"&progressId=1",urlForNextActivity);
        
    }
    
    public void testCalculateProgress() throws ProgressException
    {
        Activity testCompletedActivity = testProgress.getNextActivity();
        Activity testRootPreviousActivity = testProgress.getPreviousActivity();
        //progress from survey to synch gate
        testRootPreviousActivity = testCompletedActivity;        
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SYNCHGATE_ACTIVITY_UIID,TEST_SYNCHGATE_ACTIVITY_UIID,1,1,"sycn-gate","sycn-gate");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());

        //progress from synch gate to notebook
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_NOTEBOOK_ACTIVITY_UIID,TEST_NOTEBOOK_ACTIVITY_UIID,2,1,"random grouping","random grouping");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());

        
        //progress from notebook to random grouping activity
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_RGRP_ACTIVITY_UIID,TEST_RGRP_ACTIVITY_UIID,3,1,"random grouping","random grouping");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from random grouping activity to chat
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_CHAT_ACTIVITY_UIID,TEST_CHAT_ACTIVITY_UIID,4,1,"chat","chat");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from chat to QNA
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_QNA_ACTIVITY_UIID,TEST_QNA_ACTIVITY_UIID,5,1,"QNA","QNA");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from QNA to options activity
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_OPTIONS_ACTIVITY_UIID,TEST_OPTIONS_ACTIVITY_UIID,6,1,"OPTIONS","OPTIONS");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub option(notice board) to parallel activity
        testCompletedActivity = ((OptionsActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_CNB_ACTIVITY_ID);
        testRootPreviousActivity = testCompletedActivity.getParentActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_PARALLEL_ACTIVITY_UIID,TEST_PARALLEL_ACTIVITY_UIID,8,1,"PARALLEL","PARALLEL");
        assertEquals("verify temp completed activities",2,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub parallel(QNA) to waiting
        testCompletedActivity = ((ParallelActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_CQNA_ACTIVITY_ID);
        //the previous activity should not be changed.
        testRootPreviousActivity = testProgress.getPreviousActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_PARALLEL_ACTIVITY_UIID,TEST_WAITING_ACTIVITY_UIID,9,1,"PARALLEL","WAITING");
        assertTrue("verify waiting flag-should be waiting.",testProgress.isParallelWaiting());
        assertTrue("verify next activity",testProgress.getNextActivity()==null);
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub parallel(message board) to sub sequence activity(share resource)
        testCompletedActivity = ((ParallelActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_MB_ACTIVITY_ID);
        testRootPreviousActivity = testCompletedActivity.getParentActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SEQUENCE_ACTIVITY_UIID,TEST_SR_ACTIVITY_UIID,11,1,"SEQUENCE","SHARE RESOURCE");
        assertTrue("verify waiting flag-should not be waiting",!testProgress.isParallelWaiting());
        assertEquals("verify temp completed activities",2,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub sequence(share resource) to sub sequence(QNA)
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testProgress.getPreviousActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SEQUENCE_ACTIVITY_UIID,TEST_SQNA_ACTIVITY_UIID,12,2,"SEQUENCE","QNA");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress sub sequence(QNA) to complete lesson
        testCompletedActivity = testProgress.getNextActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertTrue("verify lesson complete",testProgress.isLessonComplete());
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
    }

    public void testExitLesson()
    {
        learnerService.exitLesson(testProgress);
        
        LearnerProgress progress = learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);

        assertTrue("verify it should be restarting",progress.isRestarting());
        
    }
    
    public void testPerformGrouping()
    {
        Activity randomGroupingActivity = learnerService.getActivity(new Long(TEST_RGRP_ACTIVITY_ID));
        
        Grouping randomGrouping = ((GroupingActivity)randomGroupingActivity).getCreateGrouping();
        
        assertTrue("verify the existance of test user",!randomGrouping.doesLearnerExist(testUser));
        
        learnerService.performGrouping((GroupingActivity)randomGroupingActivity,testUser);
    
        assertTrue("verify the existance of test user",randomGrouping.doesLearnerExist(testUser));
    }
    /**
     * @param numberOfAttemptedAct 
     * @param testCompletedActivity
     */
    private void assertLearnerProgress(Activity testRootPreviousActivity,
                                       long curActivityUIId,
                                       long nextActivityUIId,
                                       int numberOfCompletedActivities,
                                       int numberOfAttemptedActivities,
                                       String currentAct,
                                       String nextAct)
    {
        assertEquals("verify the expected previous activity",
                     testRootPreviousActivity.getActivityId(),
                     testProgress.getPreviousActivity().getActivityId());
        assertEquals("verify the expected current activity - "+currentAct,
                     curActivityUIId,
                     testProgress.getCurrentActivity().getActivityUIID().longValue());
        //assert next activity, id is set to 0 if it is waiting activity
        assertEquals("verify the expected next activity - "+ nextAct,
                     nextActivityUIId,
                     nextActivityUIId!=-1?testProgress.getNextActivity().getActivityUIID().longValue():-1);
        assertEquals("verify the completed activities",numberOfCompletedActivities,testProgress.getCompletedActivities().size());
        assertEquals("verify the attempted activities",numberOfAttemptedActivities,testProgress.getAttemptedActivities().size());
    }
}
