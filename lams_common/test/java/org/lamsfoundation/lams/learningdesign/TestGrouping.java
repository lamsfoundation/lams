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

import java.util.HashSet;

import org.lamsfoundation.lams.usermanagement.User;


import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang 
 * @since 8/02/2005
 * @version 1.1
 */
public class TestGrouping extends TestCase
{

    private Grouping grouping;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        grouping = new RandomGrouping();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestGrouping.
     * @param arg0
     */
    public TestGrouping(String arg0)
    {
        super(arg0);
    }

    /**
     * Test the order generation without concurrency contention.
     */
    public void testGetNextGroupOrderId()
    {

        assertEquals(1,grouping.getNextGroupOrderId());
        
        grouping.setGroups(new HashSet());
        Group group1 = new Group();
        group1.setOrderId(grouping.getNextGroupOrderId());
        grouping.getGroups().add(group1);
        assertEquals(2,grouping.getNextGroupOrderId());
        
        Group group2 = new Group();
        group2.setOrderId(grouping.getNextGroupOrderId());
        grouping.getGroups().add(group2);
        assertEquals(3,grouping.getNextGroupOrderId());
    }
    
    public void testGetLearners()
    {
        grouping.setGroups(new HashSet());
        
        User user1 = new User();
        user1.setUserId(new Integer(1));
        Group group1 = new Group();
        insertUserIntoGroup(user1, group1,grouping.getNextGroupOrderId());
        
        
        User user2 = new User();
        user2.setUserId(new Integer(2));
        Group group2 = new Group();
        insertUserIntoGroup(user2, group2,grouping.getNextGroupOrderId());
        
        assertEquals("verify number of learners",2,grouping.getLearners().size());
    }

    public void testGetGroupByLearner()
    {
        grouping.setGroups(new HashSet());
        User user1 = new User();
        user1.setUserId(new Integer(1));
        Group group1 = new Group();
        insertUserIntoGroup(user1, group1,grouping.getNextGroupOrderId());
         
        User user2 = new User();
        user2.setUserId(new Integer(2));
        Group group2 = new Group();
        insertUserIntoGroup(user2, group2,grouping.getNextGroupOrderId());
        
        Group group = grouping.getGroupBy(user2);
        assertEquals("verify group retrieved",group2.getOrderId(),group.getOrderId());
    }
    
    public void testGetNullGroupByLearner()
    {
        grouping.setGroups(new HashSet());
        User user1 = new User();
        user1.setUserId(new Integer(1));
        Group group1 = new Group();
        insertUserIntoGroup(user1, group1,grouping.getNextGroupOrderId());
        
        User user2 = new User();
        user2.setUserId(new Integer(2));
        
        Group group = grouping.getGroupBy(user2);
        assertTrue("verify group retrieved",group.isNull());

    }
    
    public void testGetGroupWithLeastMember()
    {
        int group1_orderId=grouping.getNextGroupOrderId();
        grouping.setGroups(new HashSet());
        User user1 = new User();
        user1.setUserId(new Integer(1));
        Group group1 = new Group();
        insertUserIntoGroup(user1,group1,group1_orderId );
         
        User user3 = new User();
        user3.setUserId(new Integer(3));
        group1.getUsers().add(user3);
        
        User user2 = new User();
        user2.setUserId(new Integer(2));
        Group group2 = new Group();
        insertUserIntoGroup(user2, group2,grouping.getNextGroupOrderId());
        
        Group group = grouping.getGroupWithLeastMember();
        assertEquals("verify group",2,group.getOrderId());
        
    }
    /**
     * @param user1
     * @param group1
     */
    private void insertUserIntoGroup(User user, Group group,int orderId)
    {
        group.setUsers(new HashSet());
        group.getUsers().add(user);
        group.setOrderId(orderId);
        grouping.getGroups().add(group);
    }
}
