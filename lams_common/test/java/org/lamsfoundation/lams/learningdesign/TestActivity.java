/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2005-2-18
 ******************************************************************************** */

package org.lamsfoundation.lams.learningdesign;

import java.util.Set;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;


/**
 * 
 * @author Jacky Fang 2005-2-18
 * 
 */
public class TestActivity extends AbstractLamsTestCase
{
	protected ActivityDAO activityDAO;
	
	private static final Long TEST_SEQUENCE_ACTIVITY=new Long(14);
	
	private static final Long TEST_SURVEY_ACTIVITY = new Long(20);
    /*
     * @see AbstractLamsTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
    }

    /*
     * @see AbstractLamsTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestActivity.
     * @param arg0
     */
    public TestActivity(String arg0)
    {
        super(arg0);
    }

    public void testGetAllToolActivitiesFromComplexActivity()
    {
        Activity complexActivity = activityDAO.getActivityByActivityId(TEST_SEQUENCE_ACTIVITY);
        
        Set toolActivities = complexActivity.getAllToolActivitiesFrom(complexActivity);
        
        assertEquals("verify the number tool activity we should get",3,toolActivities.size());
    }

    public void testGetAllToolActivitiesFromToolActivity()
    {
        Activity simpleActivity = activityDAO.getActivityByActivityId(TEST_SURVEY_ACTIVITY);
        
        Set toolActivities = simpleActivity.getAllToolActivitiesFrom(simpleActivity);
        
        assertEquals("verify the number tool activity we should get",1,toolActivities.size());

    }
    /**
     * @see org.lamsfoundation.lams.AbstractLamsTestCase#getContextConfigLocation()
     */
    protected String[] getContextConfigLocation()
    {
		return new String[] {"/org/lamsfoundation/lams/learningdesign/learningDesignApplicationContext.xml",
		 "applicationContext.xml"};
    }

}
