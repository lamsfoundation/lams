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

import org.lamsfoundation.lams.AbstractLamsTestCase;

import org.lamsfoundation.lams.learning.progress.ProgressException;
import org.lamsfoundation.lams.learningdesign.Activity;
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
    private static final long TEST_NB_ACTIVITY_ID = 35;
    private static final long TEST_RGRP_ACTIVITY_ID = 38;
    private static final long TEST_CHAT_ACTIVITY_ID = 37;
    private static final long TEST_QNA_ACTIVITY_ID = 39;
    private static final long TEST_OPTIONS_ACTIVITY_ID = 26;
    private static final long TEST_CNB_ACTIVITY_ID = 27;
    private static final long TEST_PARALLEL_ACTIVITY_ID = 29;
    private static final long TEST_CQNA_ACTIVITY_ID = 30;
    private static final long TEST_WAITING_ACTIVITY_ID = -1;
    private static final long TEST_MB_ACTIVITY_ID = 31;
    private static final long TEST_SEQUENCE_ACTIVITY_ID = 32;
    private static final long TEST_SR_ACTIVITY_ID = 33;
    private static final long TEST_SQNA_ACTIVITY_ID = 34;
    private static final String HOST="http://localhost:8080/lams_learning/";
    private static final String LOAD_TOOL_URL="/LoadToolActivity.do";
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
        return new String[] { "/WEB-INF/spring/learningApplicationContext.xml",
  			  				  "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
  			  				  "/org/lamsfoundation/lams/tool/toolApplicationContext.xml",					  
                              "/org/lamsfoundation/lams/tool/survey/dataAccessContext.xml",
                              "/org/lamsfoundation/lams/tool/survey/surveyApplicationContext.xml",          					  
        					  "applicationContext.xml"};
    }
    
    public void testJoinLesson() throws ProgressException,LamsToolServiceException
    {
        learnerService.joinLesson(testUser,testLesson);
        
        testProgress=learnerProgressDao.getLearnerProgressByLearner(testUser,testLesson);
        
        assertNotNull(testProgress);
        assertNotNull("verify next activity",testProgress.getNextActivity());
        assertEquals("verify id of next activity-survey",36,testProgress.getNextActivity().getActivityId().longValue());
        assertNotNull("verify current activity",testProgress.getCurrentActivity());
        assertEquals("verify id of current activity-survey",36,testProgress.getCurrentActivity().getActivityId().longValue());
        assertEquals("verify attempted activity",1,testProgress.getAttemptedActivities().size());
        assertEquals("verify completed activity",0,testProgress.getCompletedActivities().size());
        assertNotNull("verify correspondent tool session for next activity",
                      ((ToolActivity)testProgress.getNextActivity()).getToolSessions());
        assertEquals("verify number of tool sessions created",1,
                     ((ToolActivity)testProgress.getNextActivity()).getToolSessions().size());
    }


    public void testCompleteToolSession()
    {
        String urlForNextActivity = learnerService.completeToolSession(TEST_TOOL_SESSION_ID,testUser);
        
        ToolSession toolSession = toolSessionDao.getToolSession(new Long(TEST_TOOL_SESSION_ID));
        
        assertNotNull("verify the existance of tool session",toolSession);
        assertEquals("verify tool session state",ToolSession.ENDED_STATE,toolSession.getToolSessionStateId());
        
        assertEquals("verify the returned url",HOST+LOAD_TOOL_URL+PARAM_ACTIVITY_ID+TEST_NB_ACTIVITY_ID,urlForNextActivity);
        
    }
    
    public void testCalculateProgress() throws ProgressException
    {
        Activity testCompletedActivity = testProgress.getNextActivity();
        Activity testRootPreviousActivity = testProgress.getPreviousActivity();
        //progress from survey to notice board
        testRootPreviousActivity = testCompletedActivity;        
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_NB_ACTIVITY_ID,TEST_NB_ACTIVITY_ID,1,1,"nb","nb");

        //progress from notice board to random grouping activity
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_RGRP_ACTIVITY_ID,TEST_RGRP_ACTIVITY_ID,2,1,"random grouping","random grouping");
        
        //progress from random grouping activity to chat
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_CHAT_ACTIVITY_ID,TEST_CHAT_ACTIVITY_ID,3,1,"chat","chat");
       
        //progress from chat to QNA
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_QNA_ACTIVITY_ID,TEST_QNA_ACTIVITY_ID,4,1,"QNA","QNA");

        //progress from QNA to options activity
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testCompletedActivity;
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_OPTIONS_ACTIVITY_ID,TEST_OPTIONS_ACTIVITY_ID,5,1,"OPTIONS","OPTIONS");

        //progress from sub option(notice board) to parallel activity
        testCompletedActivity = ((OptionsActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_CNB_ACTIVITY_ID);
        testRootPreviousActivity = testCompletedActivity.getParentActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_PARALLEL_ACTIVITY_ID,TEST_PARALLEL_ACTIVITY_ID,7,1,"PARALLEL","PARALLEL");

        //progress from sub parallel(QNA) to waiting
        testCompletedActivity = ((ParallelActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_CQNA_ACTIVITY_ID);
        //the previous activity should not be changed.
        testRootPreviousActivity = testProgress.getPreviousActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_PARALLEL_ACTIVITY_ID,TEST_WAITING_ACTIVITY_ID,8,1,"PARALLEL","WAITING");
        assertTrue("verify waiting flag-should be waiting.",testProgress.isParallelWaiting());
        assertTrue("verify next activity",testProgress.getNextActivity()==null);

        //progress from sub parallel(message board) to sub sequence activity(share resource)
        testCompletedActivity = ((ParallelActivity)testProgress.getCurrentActivity()).getChildActivityById(TEST_MB_ACTIVITY_ID);
        testRootPreviousActivity = testCompletedActivity.getParentActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SEQUENCE_ACTIVITY_ID,TEST_SR_ACTIVITY_ID,10,1,"SEQUENCE","SHARE RESOURCE");
        assertTrue("verify waiting flag-should not be waiting",!testProgress.isParallelWaiting());
        
        //progress from sub sequence(share resource) to sub sequence(QNA)
        testCompletedActivity = testProgress.getNextActivity();
        testRootPreviousActivity = testProgress.getPreviousActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertLearnerProgress(testRootPreviousActivity,TEST_SEQUENCE_ACTIVITY_ID,TEST_SQNA_ACTIVITY_ID,11,2,"SEQUENCE","QNA");

        //progress sub sequence(QNA) to complete lesson
        testCompletedActivity = testProgress.getNextActivity();
        testProgress = learnerService.calculateProgress(testCompletedActivity,
                                                        testUser,
                                                        testLesson);
        assertTrue("verify lesson complete",testProgress.isLessonComplete());
    }

    /**
     * @param numberOfAttemptedAct 
     * @param testCompletedActivity
     */
    private void assertLearnerProgress(Activity testRootPreviousActivity,
                                       long curActivityId,
                                       long nextActivityId,
                                       int numberOfCompletedActivities,
                                       int numberOfAttemptedActivities,
                                       String currentAct,
                                       String nextAct)
    {
        assertEquals("verify the expected previous activity",
                     testRootPreviousActivity.getActivityId(),
                     testProgress.getPreviousActivity().getActivityId());
        assertEquals("verify the expected current activity - "+currentAct,
                     curActivityId,
                     testProgress.getCurrentActivity().getActivityId().longValue());
        //assert next activity, id is set to 0 if it is waiting activity
        assertEquals("verify the expected next activity - "+ nextAct,
                     nextActivityId,
                     nextActivityId!=-1?testProgress.getNextActivity().getActivityId().longValue():-1);
        assertEquals("verify the completed activities",numberOfCompletedActivities,testProgress.getCompletedActivities().size());
        assertEquals("verify the attempted activities",numberOfAttemptedActivities,testProgress.getAttemptedActivities().size());
    }
}
