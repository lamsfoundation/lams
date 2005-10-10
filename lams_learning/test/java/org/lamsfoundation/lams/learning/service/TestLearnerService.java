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

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GateActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dao.ILearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.ILessonDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LearnerProgressDAO;
import org.lamsfoundation.lams.lesson.dao.hibernate.LessonDAO;
import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolSessionDAO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;


/**
 * You must run the lams_monitoring tests before running these tests,
 * as the lams_monitoring tests set up the necessary test data.
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
    private IActivityDAO activityDao;
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
    private static final long TEST_SYNCHGATE_ACTIVITY_ID = 33;
    private static final long TEST_RGRP_ACTIVITY_ID = 23;
    private static final long TEST_CNB_ACTIVITY_ID = 37;
    private static final long TEST_CQNA_ACTIVITY_ID = 40;
    private static final long TEST_MB_ACTIVITY_ID = 41;
    
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
    private static final long TEST_SCHEDULE_GATE_ACTIVITY_UIID = 27;
    private static final long TEST_PERMISSION_GATE_ACTIVITY_UIID = 28;
    private static final String HOST="http://localhost:8080/lams/learning";
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
        activityDao = (IActivityDAO)this.context.getBean("activityDAO");
        
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
        return new String[] { 
        		"/org/lamsfoundation/lams/localApplicationContext.xml",
        		"/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
        		"/org/lamsfoundation/lams/tool/toolApplicationContext.xml",
        		"/org/lamsfoundation/lams/tool/survey/applicationContext.xml",            
    			"/org/lamsfoundation/lams/learning/learningApplicationContext.xml"};
    }
    
    public void testJoinLesson() throws ProgressException,LamsToolServiceException
    {
        learnerService.joinLesson(testUser,testLesson);
        
        testProgress=learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        assertNotNull(testProgress);

        // get the next activity, and get it by its proper class so we can analyse it!
        Activity nextActivity = testProgress.getNextActivity();
        Activity currentActivity = testProgress.getCurrentActivity();
        assertNotNull("verify next activity",nextActivity);
        assertEquals("verify id of next activity-survey",6,nextActivity.getActivityUIID().intValue());
        assertNotNull("verify current activity",currentActivity);
        assertEquals("verify id of current activity-survey",6,currentActivity.getActivityUIID().intValue());
        assertEquals("verify attempted activity",1,testProgress.getAttemptedActivities().size());
        assertEquals("verify completed activity",0,testProgress.getCompletedActivities().size());
        
        ToolActivity nextToolActivity = (ToolActivity) activityDao.getActivityByActivityId(nextActivity.getActivityId());
        assertNotNull("verify correspondent tool session for next activity",
                      nextToolActivity.getToolSessions());
        assertEquals("verify number of tool sessions created",1,
        				nextToolActivity.getToolSessions().size());
    }


    public void testCompleteToolSession()
    {
        String urlForNextActivity = learnerService.completeToolSession(new Long(TEST_TOOL_SESSION_ID),testUser);
        
        ToolSession toolSession = toolSessionDao.getToolSession(new Long(TEST_TOOL_SESSION_ID));
        
        assertNotNull("verify the existance of tool session",toolSession);
        assertEquals("verify tool session state",ToolSession.ENDED_STATE,toolSession.getToolSessionStateId());
        
        System.out.println("HOST+GATE_URL+PARAM_ACTIVITY_ID+TEST_SYNCHGATE_ACTIVITY_ID+&progressId=1:  "+HOST+GATE_URL+PARAM_ACTIVITY_ID+TEST_SYNCHGATE_ACTIVITY_ID+"&progressId=1");
        System.out.println("urlForNextActivity:  "+urlForNextActivity);
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

        //progress from random grouping activity to schedule gate
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SCHEDULE_GATE_ACTIVITY_UIID,TEST_SCHEDULE_GATE_ACTIVITY_UIID,4,1,"schedule gate","schedule gate");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());

        
        //progress from schedule gate activity to chat
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_CHAT_ACTIVITY_UIID,TEST_CHAT_ACTIVITY_UIID,5,1,"chat","chat");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());

        //progress from chat activity to QNA
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_QNA_ACTIVITY_UIID,TEST_QNA_ACTIVITY_UIID,6,1,"QNA","QNA");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        
        //progress from QNA to permission gate
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_PERMISSION_GATE_ACTIVITY_UIID,TEST_PERMISSION_GATE_ACTIVITY_UIID,7,1,"permission gate","permission gate");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from permission gate to options activity
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_OPTIONS_ACTIVITY_UIID,TEST_OPTIONS_ACTIVITY_UIID,8,1,"OPTIONS","OPTIONS");
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub option(notice board) to parallel activity
        testCompletedActivity = ((OptionsActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_CNB_ACTIVITY_ID);
        testRootPreviousActivity = testCompletedActivity.getParentActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_PARALLEL_ACTIVITY_UIID,TEST_PARALLEL_ACTIVITY_UIID,10,1,"PARALLEL","PARALLEL");
        assertEquals("verify temp completed activities",2,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub parallel(QNA) to waiting
        testCompletedActivity = ((ParallelActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_CQNA_ACTIVITY_ID);
        //the previous activity should not be changed.
        testRootPreviousActivity = testProgress.getPreviousActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_PARALLEL_ACTIVITY_UIID,TEST_WAITING_ACTIVITY_UIID,11,1,"PARALLEL","WAITING");
        assertTrue("verify waiting flag-should be waiting.",testProgress.isParallelWaiting());
        assertTrue("verify next activity",testProgress.getNextActivity()==null);
        assertEquals("verify temp completed activities",1,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub parallel(message board) to sub sequence activity(share resource)
        testCompletedActivity = ((ParallelActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_MB_ACTIVITY_ID);
        testRootPreviousActivity = testCompletedActivity.getParentActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SEQUENCE_ACTIVITY_UIID,TEST_SR_ACTIVITY_UIID,13,1,"SEQUENCE","SHARE RESOURCE");
        assertTrue("verify waiting flag-should not be waiting",!testProgress.isParallelWaiting());
        assertEquals("verify temp completed activities",2,testProgress.getCurrentCompletedActivitiesList().size());
        
        //progress from sub sequence(share resource) to sub sequence(QNA)
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testProgress.getPreviousActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SEQUENCE_ACTIVITY_UIID,TEST_SQNA_ACTIVITY_UIID,14,2,"SEQUENCE","QNA");
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
    
    public void testKnockSynchGateClosed()
    {
        //setup lesson learner list
        List lessonLearners = new LinkedList();
        lessonLearners.add(testUser);
        User testUser2= usermanageService.getUserById(new Integer(1));
        lessonLearners.add(testUser2);
        
        //get sync gate
        GateActivity synchGate = (GateActivity)learnerService.getActivity(new Long(TEST_SYNCHGATE_ACTIVITY_ID));
        
        boolean gateOpen = learnerService.knockGate(synchGate,testUser,lessonLearners);
        
        assertTrue("gate is closed",!gateOpen);
        synchGate = (GateActivity)learnerService.getActivity(new Long(TEST_SYNCHGATE_ACTIVITY_ID));
        
        assertEquals("one learner is waiting",1,synchGate.getWaitingLearners().size());
    }
    
    public void testKnockSynchGateOpen()
    {
        List lessonLearners = new LinkedList();
        lessonLearners.add(testUser);
        User testUser2= usermanageService.getUserById(new Integer(1));
        lessonLearners.add(testUser2);
        
        //get sync gate
        GateActivity synchGate = (GateActivity)learnerService.getActivity(new Long(TEST_SYNCHGATE_ACTIVITY_ID));

        boolean gateOpen = learnerService.knockGate(synchGate,testUser2,lessonLearners);
        assertTrue("gate is closed",gateOpen);

        synchGate = (GateActivity)learnerService.getActivity(new Long(TEST_SYNCHGATE_ACTIVITY_ID));

        assertEquals("Gate opened, no one should be waiting",0,synchGate.getWaitingLearners().size());

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

    /**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getHibernateSessionFactoryName()
     */
    protected String getHibernateSessionFactoryName()
    {
        return "coreSessionFactory";
    }
}
