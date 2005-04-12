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
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learning.web.util.LessonLearnerDataManager;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * 
 * @author Jacky Fang
 * @since  2005-4-7
 * @version 1.1
 * 
 */
public class TestGateAction extends AbstractLamsStrutsTestCase
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestGateAction.class);

    private static final String TEST_LERNER_PROGRESS_ID = "1";
    private static final String TEST_LEARNER_ID = "2";
    private static final String TEST_LESSON_ID = "2";
    
    private ILearnerService learnerService;

    private static final String TEST_GATE_ACTIVITY_ID = "31";
    /**
     * Constructor for TestGateAction.
     * @param testName
     */
    public TestGateAction(String testName)
    {
        super(testName);
    }
    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getContextConfigLocation()
     */
    protected String getContextConfigLocation()
    {
        return "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml "
  			   +"/org/lamsfoundation/lams/tool/toolApplicationContext.xml "
  			   +"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml "
  			   +"/WEB-INF/spring/applicationContext.xml "
  			   +"/WEB-INF/spring/learningApplicationContext.xml";
    }
    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getHibernateSessionFactoryBeanName()
     */
    protected String getHibernateSessionFactoryBeanName()
    {
        return "coreSessionFactory";
    }
    /*
     * @see AbstractLamsStrutsTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        setConfigFile("/WEB-INF/struts/struts-config.xml");
        setRequestPathInfo("/gate.do");
        
        learnerService =  (ILearnerService)this.wac.getBean("learnerService");
    }

    /*
     * @see AbstractLamsStrutsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testKnockClosedGate()
    {
        addRequestParameter("method", "knockGate");
        addRequestParameter(LearningWebUtil.PARAM_PROGRESS_ID,TEST_LERNER_PROGRESS_ID);
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_LEARNER_ID);
        addRequestParameter(LearningWebUtil.PARAM_ACTIVITY_ID,TEST_GATE_ACTIVITY_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        
        initializeLearnerProgress();
        initializeUserMap(false);
        
        actionPerform();
        
        verifyNoActionErrors();
        verifyTilesForward("waiting",".gateWaiting");
    }

    public void testKnockOpenGate()
    {
        addRequestParameter("method", "knockGate");
        addRequestParameter(LearningWebUtil.PARAM_PROGRESS_ID,TEST_LERNER_PROGRESS_ID);
        addRequestParameter(LearningWebUtil.PARAM_USER_ID, TEST_LEARNER_ID);
        addRequestParameter(LearningWebUtil.PARAM_ACTIVITY_ID,TEST_GATE_ACTIVITY_ID);
        addRequestParameter(LearningWebUtil.PARAM_LESSON_ID, TEST_LESSON_ID);
        
        initializeLearnerProgress();
        initializeUserMap(true);
        
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
    private void initializeUserMap(boolean singleUser)
    {
        User testUser = LearningWebUtil.getUserData(request,context);
        Lesson lesson = LearningWebUtil.getLessonData(request,context);
        LessonLearnerDataManager.cacheLessonUser(context,lesson,testUser);
        
        if(!singleUser)
        {
            request.getSession().removeAttribute("user");
            addRequestParameter(LearningWebUtil.PARAM_USER_ID, "1");
            User testUser2 = LearningWebUtil.getUserData(request,context);
            LessonLearnerDataManager.cacheLessonUser(context,lesson,testUser2);
        }

    }
}
