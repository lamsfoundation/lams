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

import org.lamsfoundation.lams.AbstractLamsStrutsTestCase;
import org.lamsfoundation.lams.learning.web.bean.SessionBean;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-8
 * @version
 * 
 */
public class TestLearnerAction extends AbstractLamsStrutsTestCase
{
   
    private static final String TEST_USER_ID = "2";
    private static final String TEST_LESSON_ID = "2";
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
        addRequestParameter("userId", TEST_USER_ID);

        actionPerform();

        verifyNoActionErrors();
    }
    
    public void testJoinLesson()
    {
        addRequestParameter("method", "joinLesson");
        addRequestParameter("userId", TEST_USER_ID);
        addRequestParameter("lessonId", TEST_LESSON_ID);
        
        actionPerform();
        
        verifyNoActionErrors();
        
        SessionBean bean = (SessionBean)httpSession.getAttribute(SessionBean.NAME);
        assertNotNull("verify the session bean",bean);
        assertEquals("verify the learner in the session bean",TEST_USER_ID,bean.getLearner().getUserId().toString());
        assertEquals("verify the lesson in the session bean",TEST_LESSON_ID,bean.getLesson().getLessonId().toString());
        assertNotNull("verify the learner progress",bean.getLearnerProgress());
    }

}
