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
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;


/**
 * 
 * @author Jacky Fang 2005-2-18
 * 
 */
public class TestActivity extends AbstractLamsTestCase
{
	protected ActivityDAO activityDAO;
	protected IUserDAO userDAO; 
	protected User testUser;
	
	
	private static final Long TEST_SEQUENCE_ACTIVITY=new Long(14);
	private static final Long TEST_SURVEY_ACTIVITY = new Long(20);
	private static final Long TEST_MB_ACTIVITY = new Long(19);
	private static final Integer TEST_USER_ID = new Integer(2);
	private static final long TEST_CHILD_QNA_ACTIVITY_ID = 22;
	
    /*
     * @see AbstractLamsTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
		testUser = userDAO.getUserById(TEST_USER_ID);
		
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
        
        assertEquals("verify the number tool activity we should get",2,toolActivities.size());
    }

    public void testGetAllToolActivitiesFromToolActivity()
    {
        Activity simpleActivity = activityDAO.getActivityByActivityId(TEST_SURVEY_ACTIVITY);
        
        Set toolActivities = simpleActivity.getAllToolActivitiesFrom(simpleActivity);
        
        assertEquals("verify the number tool activity we should get",1,toolActivities.size());

    }
    
    public void testGetGroupForUser()
    {
        Activity groupingAwareActivity = activityDAO.getActivityByActivityId(TEST_MB_ACTIVITY);
        
        Group testGroup = groupingAwareActivity.getGroupFor(testUser);
        
        assertNotNull(testGroup);
        assertEquals("verify group id",88,testGroup.getGroupId().longValue());
        assertEquals("verify the grouping this group belongs to",100,testGroup.getGrouping().getGroupingId().longValue());
        assertEquals("verify the order of this group",1,testGroup.getOrderId());
        
    }
    
    public void testGetChildActivityById()
    {
        Activity complexActivity = activityDAO.getActivityByActivityId(TEST_SEQUENCE_ACTIVITY);
        
        Activity child = ((ComplexActivity)complexActivity).getChildActivityById(TEST_CHILD_QNA_ACTIVITY_ID);
        assertNotNull(child);
        assertEquals("verify id",TEST_CHILD_QNA_ACTIVITY_ID,child.getActivityId().longValue());
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
