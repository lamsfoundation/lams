/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 8/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.learningdesign;

import java.util.HashSet;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.usermanagement.User;


import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * 
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
        insertUserIntoGroup(user1, group1);
        
        
        User user2 = new User();
        user2.setUserId(new Integer(2));
        Group group2 = new Group();
        insertUserIntoGroup(user2, group2);
        
        assertEquals("verify number of learners",2,grouping.getLearners().size());
    }

    /**
     * @param user1
     * @param group1
     */
    private void insertUserIntoGroup(User user, Group group)
    {
        group.setUsers(new HashSet());
        group.getUsers().add(user);
        group.setOrderId(grouping.getNextGroupOrderId());
        grouping.getGroups().add(group);
    }
}
