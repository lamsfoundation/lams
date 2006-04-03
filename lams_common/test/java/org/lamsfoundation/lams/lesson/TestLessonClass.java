/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
