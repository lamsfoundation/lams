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
package org.lamsfoundation.lams.learningdesign;

import java.util.Set;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.test.AbstractCommonTestCase;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.hibernate.UserDAO;


/**
 * 
 * @author Jacky Fang 2005-2-18
 * 
 */
public class TestActivity extends AbstractCommonTestCase
{
	protected ActivityDAO activityDAO;
	protected IUserDAO userDAO; 
	protected User testUser;
	
	
	private static final Long TEST_SEQUENCE_ACTIVITY=new Long(14);
	private static final Long TEST_SURVEY_ACTIVITY = new Long(20);
	private static final Long TEST_MB_ACTIVITY = new Long(19);
	private static final Integer TEST_USER_ID = new Integer(2);
	private static final long TEST_CHILD_QNA_ACTIVITY_ID = 22;
	
    protected void setUp() throws Exception
    {
        super.setUp();
		activityDAO =(ActivityDAO) context.getBean("activityDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
		testUser = userDAO.getUserById(TEST_USER_ID);
		
    }

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
        
        Set toolActivities = complexActivity.getAllToolActivities();
        
        assertEquals("verify the number tool activity we should get",2,toolActivities.size());
    }

    public void testGetAllToolActivitiesFromToolActivity()
    {
        Activity simpleActivity = activityDAO.getActivityByActivityId(TEST_SURVEY_ACTIVITY);
        
        Set toolActivities = simpleActivity.getAllToolActivities();
        
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
        ComplexActivity complexActivity = (ComplexActivity) activityDAO.getActivityByActivityId(TEST_SEQUENCE_ACTIVITY);
        
        Activity child = complexActivity.getChildActivityById(TEST_CHILD_QNA_ACTIVITY_ID);
        assertNotNull(child);
        assertEquals("verify id",TEST_CHILD_QNA_ACTIVITY_ID,child.getActivityId().longValue());
    }
}
