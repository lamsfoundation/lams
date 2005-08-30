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
 * @author Jacky Fang
 * @since  2005-4-18
 * @version
 * 
 */
public class TestGateAction extends AbstractLamsStrutsTestCase
{

    private static final String TEST_LESSON_ID = "2";
    private static final String TEST_SYNCH_GATE_ID = "33";
    private static final String TEST_SCHEUDLE_GATE_ID = "34";
    private static final String TEST_PERMISSION_GATE_ID = "35";

    /*
     * @see AbstractLamsStrutsTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        setConfigFile("/WEB-INF/struts/struts-config.xml");
        setRequestPathInfo("/gate.do");
    }

    /*
     * @see AbstractLamsStrutsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

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
        return 	"/org/lamsfoundation/lams/localApplicationContext.xml "+
		"/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml "+
		"/org/lamsfoundation/lams/tool/toolApplicationContext.xml "+
  		"/org/lamsfoundation/lams/learning/learningApplicationContext.xml "+        					  
  		"/org/lamsfoundation/lams/authoring/authoringApplicationContext.xml "+
  		"/org/lamsfoundation/lams/monitoring/monitoringApplicationContext.xml "+
		"/org/lamsfoundation/lams/tool/survey/applicationContext.xml ";  

    }
  /*  protected String getContextConfigLocation()
    {
        return 	"/WEB-INF/spring/applicationContext.xml "
    	+"/WEB-INF/spring/monitoringApplicationContext.xml "
		+"/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml "
		+"/WEB-INF/spring/authoringApplicationContext.xml "
		+"/WEB-INF/spring/learningApplicationContext.xml "
		+"/org/lamsfoundation/lams/tool/toolApplicationContext.xml "
		+"/org/lamsfoundation/lams/tool/survey/dataAccessContext.xml "
		+"/org/lamsfoundation/lams/tool/survey/surveyApplicationContext.xml";
    } */
    /**
     * @see org.lamsfoundation.lams.AbstractLamsStrutsTestCase#getHibernateSessionFactoryBeanName()
     */
    protected String getHibernateSessionFactoryBeanName()
    {
        return "coreSessionFactory";
    }
    
    public void testViewSynchGate()
    {
        addRequestParameter("method", "viewGate");
        addRequestParameter("lessonId",TEST_LESSON_ID);
        addRequestParameter("activityId",TEST_SYNCH_GATE_ID);
        
        actionPerform();
        
        verifyNoActionErrors();
        verifyTilesForward("viewSynchGate",".viewSynchGate");
    }

    public void testViewScheduleGate()
    {
        addRequestParameter("method", "viewGate");
        addRequestParameter("lessonId",TEST_LESSON_ID);
        addRequestParameter("activityId",TEST_SCHEUDLE_GATE_ID); 
        
        actionPerform();
        
        verifyNoActionErrors();
        verifyTilesForward("viewScheduleGate",".viewScheduleGate");
    }

    public void testViewPermissionGate()
    {
        addRequestParameter("method", "viewGate");
        addRequestParameter("lessonId",TEST_LESSON_ID);
        addRequestParameter("activityId",TEST_PERMISSION_GATE_ID);
        
        actionPerform();
        
        verifyNoActionErrors();
        verifyTilesForward("viewPermissionGate",".viewPermissionGate");
    }

    public void testOpenGate()
    {
        addRequestParameter("method", "openGate");
        addRequestParameter("activityId",TEST_SYNCH_GATE_ID);
        
        actionPerform();
        
        verifyNoActionErrors();
        verifyTilesForward("viewSynchGate",".viewSynchGate");
    }
}
