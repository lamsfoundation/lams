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
import org.lamsfoundation.lams.AbstractLamsStrutsTestCase;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.lesson.LearnerProgress;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-8
 * @version
 * 
 */
public class TestLearnerAction extends AbstractLamsStrutsTestCase
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestLearnerAction.class);
	
    private static final String TEST_USER_ID = "2";
    private static final String TEST_LESSON_ID = "2";
    private static final String TEST_ACTIVITY_ID = "27";
    
    //private static SessionBean joinedLessonBean = null;
    private static LearnerProgress testLearnerProgress = null;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        setConfigFile("/WEB-INF/struts/struts-config.xml");
        setRequestPathInfo("/learner.do");
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getContextConfigLocation()
     */
    private static String getContextConfigLocation()
    {
        return "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml "
  			   +"/org/lamsfoundation/lams/tool/toolApplicationContext.xml "
  			   +"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml "
  			   +"applicationContext.xml "
  			   +"/WEB-INF/spring/learningApplicationContext.xml";
    }
    
    /**
     * Constructor for TestLearnerAction.
     * @param testName
     */
    public TestLearnerAction(String testName)
    {
        super(testName,getContextConfigLocation());
    }

    public void testGetActiveLessons()
    {
        addRequestParameter("method", "getActiveLessons");
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_USER_ID);

        actionPerform();

        verifyNoActionErrors();
    }
    
    public void testJoinLesson()
    {
        addRequestParameter("method", "joinLesson");
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_USER_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        
        actionPerform();
        
        verifyNoActionErrors();
        
        //joinedLessonBean = (SessionBean)httpSession.getAttribute(SessionBean.NAME);
        testLearnerProgress = (LearnerProgress)httpSession.getAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE);

        //assertNotNull("verify the session bean",testLearnerProgress);
        assertNotNull("verify the learner progress",testLearnerProgress);
        assertEquals("verify the learner in the session bean",TEST_USER_ID,testLearnerProgress.getUser().getUserId().toString());
        assertEquals("verify the lesson in the session bean",TEST_LESSON_ID,testLearnerProgress.getLesson().getLessonId().toString());

    }

    public void testGetFlashProgressData()
    {
        httpSession.setAttribute(ActivityAction.LEARNER_PROGRESS_REQUEST_ATTRIBUTE,testLearnerProgress);
        addRequestParameter("method", "getFlashProgressData");
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_USER_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        
        actionPerform();
        verifyNoActionErrors();
    }
    
    public void testExitLesson()
    {
        addRequestParameter("method", "exitLesson");
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_USER_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        
        actionPerform();
        
        verifyForward("welcome");
        verifyTilesForward("welcome",".welcome");
        verifyNoActionErrors();
    }

    public void testGetLearnerActivityURL()
    {
        addRequestParameter("method", "getLearnerActivityURL");
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_USER_ID);
        addRequestParameter(LearningWebUtil.PARAM_ACTIVITY_ID, TEST_ACTIVITY_ID);
        
        actionPerform();
        
        verifyNoActionErrors();
    }
}
