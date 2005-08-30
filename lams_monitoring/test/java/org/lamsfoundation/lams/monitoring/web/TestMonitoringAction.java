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

package org.lamsfoundation.lams.monitoring.web;

import org.lamsfoundation.lams.test.AbstractLamsStrutsTestCase;

/**
 * 
 * Doesn't work - throws a duplicate key error. Probably due to the 
 * test lesson already being started!
 * 
 * @author Jacky Fang
 * @since  2005-4-15
 * @version
 * 
 */
public class TestMonitoringAction extends AbstractLamsStrutsTestCase
{
    private static final String TEST_LESSON_ID = "2";
    /**
     * @param arg0
     */
    public TestMonitoringAction(String testName)
    {
        super(testName);
    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getHibernateSessionFactoryBeanName()
     */
    protected String getHibernateSessionFactoryBeanName()
    {
        return "coreSessionFactory";
    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getContextConfigLocation()
     */
    protected String getContextConfigLocation()
    {
        return 	"/org/lamsfoundation/lams/localApplicationContext.xml "+
		"/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml "+
		"/org/lamsfoundation/lams/tool/toolApplicationContext.xml "+
  		"/org/lamsfoundation/lams/learning/learningApplicationContext.xml "+        					  
  		"/org/lamsfoundation/lams/authoring/authoringApplicationContext.xml "+
  		"/org/lamsfoundation/lams/monitoring/monitoringApplicationContext.xml "+
		"/org/lamsfoundation/lams/tool/survey/applicationContext.xml ";  
    }
    /*
     * @see AbstractLamsStrutsTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        setConfigFile("/WEB-INF/struts/struts-config.xml");
        setRequestPathInfo("/monitoring.do");
        
        //learnerService =  (ILearnerService)this.wac.getBean("learnerService");
    }

    /*
     * @see AbstractLamsStrutsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testStartLesson()
    {
        addRequestParameter("method", "startLesson");
        addRequestParameter("lessonId",TEST_LESSON_ID);
        
        actionPerform();
        
        verifyNoActionErrors();
        verifyForward("scheduler");
    }
}
