/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 14/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson;

import java.util.HashSet;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.usermanagement.User;

import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang 14/02/2005
 * 
 */
public class TestLessonClass extends TestCase
{

    LessonClass lessonClass;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        lessonClass = new LessonClass();
        lessonClass.setGroups(new HashSet());
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for TestLessonClass.
     * @param arg0
     */
    public TestLessonClass(String arg0)
    {
        super(arg0);
    }

    public void testGetLearners()
    {
        User user1 = new User();
        user1.setUserId(new Integer(1));
        Group group1 = new Group();
        group1.setGroupId(new Long(1));
        group1.setUsers(new HashSet());
        group1.getUsers().add(user1);
        group1.setOrderId(lessonClass.getNextGroupOrderId());
        lessonClass.getGroups().add(group1);
        
        User staff = new User();
        staff.setUserId(new Integer(3));
        Group staffGroup = new Group();
        staffGroup.setGroupId(new Long(2));
        staffGroup.setUsers(new HashSet());
        staffGroup.getUsers().add(staff);
        staffGroup.setOrderId(lessonClass.getNextGroupOrderId());
        lessonClass.getGroups().add(staffGroup);
        lessonClass.setStaffGroup(staffGroup);
        
        assertEquals("verify number of learners",1,lessonClass.getLearners().size());

   }

}
