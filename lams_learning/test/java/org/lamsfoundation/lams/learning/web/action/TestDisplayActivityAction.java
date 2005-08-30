/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.learning.web.action;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.lesson.LearnerProgress;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-8
 * @version
 * 
 */
public class TestDisplayActivityAction extends AbstractTestAction
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestDisplayActivityAction.class);

    
    private static final String TEST_USER_ID = "2";
    private static final String TEST_LESSON_ID = "2";
    //private static SessionBean testBean = null;
    private static LearnerProgress learnerProgress = null;
    /**
     * Constructor for TestDisplayActivityAction.
     * @param arg0
     */
    public TestDisplayActivityAction(String arg0)
    {
        super(arg0);
    }


    /*
     * @see AbstractLamsStrutsTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        setConfigFile("/WEB-INF/struts/struts-config.xml");
    }

    /*
     * @see AbstractLamsStrutsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    /*
     * Class under test for ActionForward execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
     */
    public void testDisplayToolLoadingPage()
    {
        //join the learner to the lesson before we test the display activity
        //we don't assert here because it is meant be ensured by TestLearnerAction.
        setRequestPathInfo("/learner.do");
        addRequestParameter("method", "joinLesson");
        addRequestParameter("userId", TEST_USER_ID);
        addRequestParameter("lessonId", TEST_LESSON_ID);
        actionPerform();
        
        //testBean = (SessionBean)httpSession.getAttribute(SessionBean.NAME);
        learnerProgress = (LearnerProgress)httpSession.getAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);
        //test page loading.
        setRequestPathInfo("/DisplayActivity.do");
        actionPerform();
        verifyForward("loadToolActivity");
    }
    
    public void testDisplayToolActivityFollowingParallelActivity()
    {
        //setup the session bean to display option page.
        //LearnerProgress progress = testBean.getLearnerProgress();
        ParallelActivity parallelActivity= new ParallelActivity();
        learnerProgress.setPreviousActivity(parallelActivity);
        //testBean.setLearnerProgress(progress);
        httpSession.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);
        
        setRequestPathInfo("/DisplayActivity.do");
        actionPerform();
        verifyForwardPath("/requestDisplay.do?url=http://localhost:8080/lams_learning/LoadToolActivity.do?activityId=26");
        
        //restore the progress
        //progress = testBean.getLearnerProgress();
        learnerProgress.setPreviousActivity(null);
        //testBean.setLearnerProgress(progress);
    }
    
    public void testDisplayOptionsActivity()
    {
        //setup the session bean to display option page.
        //LearnerProgress progress = testBean.getLearnerProgress();
        OptionsActivity optionActivity= new OptionsActivity();
        learnerProgress.setNextActivity(optionActivity);
        //testBean.setLearnerProgress(progress);
        //httpSession.setAttribute(SessionBean.NAME,testBean);
        httpSession.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);
        
        setRequestPathInfo("/DisplayActivity.do");
        actionPerform();
        verifyForward("displayOptionsActivity");
    }

    public void testDisplayParallelActivity()
    {
        //setup the session bean to display option page.
        //LearnerProgress progress = testBean.getLearnerProgress();
        ParallelActivity parallelActivity= new ParallelActivity();
        learnerProgress.setNextActivity(parallelActivity);
        //testBean.setLearnerProgress(progress);
        //httpSession.setAttribute(SessionBean.NAME,testBean);
        httpSession.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);
        
        setRequestPathInfo("/DisplayActivity.do");
        actionPerform();
        verifyForward("displayParallelActivity");
    }
    
    
    
    public void testDisplayWaitingParallelActivity()
    {
        //setup the session bean to display parallel waiting.
        //LearnerProgress progress = testBean.getLearnerProgress();
        learnerProgress.setParallelWaiting(true);
        //testBean.setLearnerProgress(progress);
        //httpSession.setAttribute(SessionBean.NAME,testBean);
        httpSession.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);
        
        setRequestPathInfo("/DisplayActivity.do");
        actionPerform();
        verifyForward("parallelWait");
    }

    public void testDisplayCompletionPage()
    {
        //setup the session bean to display completion page.
        //LearnerProgress progress = testBean.getLearnerProgress();
        learnerProgress.setLessonComplete(true);
        //testBean.setLearnerProgress(progress);
        //httpSession.setAttribute(SessionBean.NAME,testBean);
        httpSession.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,learnerProgress);
        
        setRequestPathInfo("/DisplayActivity.do");
        actionPerform();
        verifyForward("lessonComplete");
        
    }
}
