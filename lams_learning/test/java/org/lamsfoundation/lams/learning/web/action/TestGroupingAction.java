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
package org.lamsfoundation.lams.learning.web.action;

import java.util.List;

import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learning.web.util.LessonLearnerDataManager;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * 
 * @author Jacky Fang
 * @since  2005-4-1
 * @version
 * 
 */
public class TestGroupingAction extends AbstractTestAction
{

    private static final String TEST_LERNER_PROGRESS_ID = "1";
    private static final String TEST_RGRP_ACTIVITY_ID = "29";
    private static final String TEST_LEARNER_ID = "2";
    private static final String TEST_LESSON_ID = "2";
    private static Activity groupingActivity;
    private ILearnerService learnerService;
    /**
     * @param arg0
     * @param arg1
     */
    public TestGroupingAction(String testName)
    {
        super(testName);
    }

    /*
     * @see AbstractLamsStrutsTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        setConfigFile("/WEB-INF/struts/struts-config.xml");
        setRequestPathInfo("/grouping.do");
        
        learnerService =  (ILearnerService)this.wac.getBean("learnerService");
    }

    /*
     * @see AbstractLamsStrutsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testPerformGrouping()
    {
        addRequestParameter("method", "performGrouping");
        addRequestParameter(LearningWebUtil.PARAM_PROGRESS_ID,TEST_LERNER_PROGRESS_ID);
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_LEARNER_ID);
        addRequestParameter(LearningWebUtil.PARAM_ACTIVITY_ID,TEST_RGRP_ACTIVITY_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        
        initializeLearnerProgress();
        
        initializeUserMap();
        
        actionPerform();

        verifyNoActionErrors();
        verifyForward("viewGrouping");
        
        groupingActivity = (Activity)request.getAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE);
    
        assertNotNull("verify the acitivity in the request",groupingActivity);
        assertTrue("verify the activity type",groupingActivity instanceof GroupingActivity);
        
    }

    public void testViewGrouping()
    {
        addRequestParameter("method", "viewGrouping");
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_LEARNER_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        LearningWebUtil.putActivityInRequest(request,groupingActivity,learnerService);
        
        actionPerform();
    
        verifyNoActionErrors();
        verifyTilesForward("showGroup",".grouping");
        
        List groups = (List)httpSession.getAttribute("groups");
        assertNotNull("verify groups",groups);
        assertEquals("verify number of groups",2,groups.size());
    }

    public void testCompleteActivity()
    {
        addRequestParameter("method", "completeActivity");
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_LEARNER_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        request.setAttribute(ActivityAction.ACTIVITY_REQUEST_ATTRIBUTE,groupingActivity);

        actionPerform();
        
        verifyNoActionErrors();
    }
    /**
     * 
     */
    private void initializeLearnerProgress()
    {
        Activity activity = LearningWebUtil.getActivityFromRequest(request,learnerService);
        LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgressByID(request,context);
        learnerProgress.setNextActivity(activity);
        httpSession.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,
                                 learnerProgress);
    }
    
    /**
     * 
     */
    private void initializeUserMap()
    {
        User testUser = LearningWebUtil.getUserData(context);
        Lesson lesson = LearningWebUtil.getLessonData(request,context);
        
        LessonLearnerDataManager.cacheLessonUser(context,lesson,testUser);
    }
}
