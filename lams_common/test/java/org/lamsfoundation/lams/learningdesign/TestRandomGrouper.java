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

package org.lamsfoundation.lams.learningdesign;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;

import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-24
 * @version
 * 
 */
public class TestRandomGrouper extends TestCase
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestRandomGrouper.class);
	
    private RandomGrouping groupingByNumofGroups;
    private RandomGrouping groupingByLearnerPerGroups;
    private List userList = new ArrayList();
    private static final int NUM_OF_TEST_USERS = 10;
    
    
    /**
     * Constructor for TestRandomGrouper.
     * @param arg0
     */
    public TestRandomGrouper(String testName)
    {
        super(testName);
    }
    
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        //initialize grouping
        groupingByNumofGroups = new RandomGrouping(new Long(1),
                                                   new TreeSet(),
                                                   new TreeSet(),
                                                   new Integer(3),//number of groups
                                                   null);
        groupingByLearnerPerGroups = new RandomGrouping(new Long(1),
                                                        new TreeSet(),
                                                        new TreeSet(),
                                                        null,
                                                        new Integer(4));//learner per groups
        //initialize users
        for(int i =0; i<NUM_OF_TEST_USERS;i++)
            userList.add(createUser(new Integer(i+1),"tester"+i));
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }



    /*
     * Class under test for void doGrouping(Grouping, List)
     */
    public void testDoGroupingLearnerListByNumOfGroups()
    {
        this.groupingByNumofGroups.doGrouping(userList);
        Group groupWithLeastMember = groupingByNumofGroups.getGroupWithLeastMember();
        
        for(Iterator i = groupingByNumofGroups.getGroups().iterator();i.hasNext();)
        {
            Group group = (Group)i.next();
            log.info("Number of Leaner in Group["+group.getOrderId()+"]:"+group.getUsers().size());
        }
        
        assertEquals("verify number of groups created",3,groupingByNumofGroups.getGroups().size());
        assertEquals("verify group with least member",3,groupWithLeastMember.getUsers().size());
    }

    /*
     * Class under test for void doGrouping(Grouping, User)
     */
    public void testDoGroupingOneLearnerByNumOfGroups()
    {
        this.groupingByNumofGroups.doGrouping((User)userList.get(5));
        Group groupWithLeastMember = groupingByNumofGroups.getGroupWithLeastMember();
        
        for(Iterator i = groupingByNumofGroups.getGroups().iterator();i.hasNext();)
        {
            Group group = (Group)i.next();
            log.info("Number of Leaner in Group["+group.getOrderId()+"]:"+group.getUsers().size());
        }
        
        assertEquals("verify number of groups created",3,groupingByNumofGroups.getGroups().size());
        assertEquals("verify group with least member",0,groupWithLeastMember.getUsers().size());
    }
    
    /*
     * Class under test for void doGrouping(Grouping, List)
     */
    public void testDoGroupingLearnerListByLearnerPerGroups()
    {
        this.groupingByLearnerPerGroups.doGrouping(userList);
        Group groupWithLeastMember = groupingByLearnerPerGroups.getGroupWithLeastMember();

        for(Iterator i = groupingByLearnerPerGroups.getGroups().iterator();i.hasNext();)
        {
            Group group = (Group)i.next();
            log.info("Number of Leaner in Group["+group.getOrderId()+"]:"+group.getUsers().size());
        }
        
        assertEquals("verify number of groups created",3,groupingByLearnerPerGroups.getGroups().size());
        assertEquals("verify group with least member",3,groupWithLeastMember.getUsers().size());
        

    }

    /*
     * Class under test for void doGrouping(Grouping, User)
     */
    public void testDoGroupingOneLearnerByLearnerPerGroups()
    {
        this.groupingByLearnerPerGroups.doGrouping((User)userList.get(5));
        Group groupWithLeastMember = groupingByLearnerPerGroups.getGroupWithLeastMember();
        
        for(Iterator i = groupingByLearnerPerGroups.getGroups().iterator();i.hasNext();)
        {
            Group group = (Group)i.next();
            log.info("Number of Leaner in Group["+group.getOrderId()+"]:"+group.getUsers().size());
        }
        
        assertEquals("verify number of groups created",1,groupingByLearnerPerGroups.getGroups().size());
        assertEquals("verify group with least member",1,groupWithLeastMember.getUsers().size());
        

        this.groupingByLearnerPerGroups.doGrouping((User)userList.get(1));
        this.groupingByLearnerPerGroups.doGrouping((User)userList.get(2));
        this.groupingByLearnerPerGroups.doGrouping((User)userList.get(3));
        groupWithLeastMember = groupingByLearnerPerGroups.getGroupWithLeastMember();
        assertEquals("verify group with least member",4,groupWithLeastMember.getUsers().size());

        this.groupingByLearnerPerGroups.doGrouping((User)userList.get(4));
        groupWithLeastMember = groupingByLearnerPerGroups.getGroupWithLeastMember();
        assertEquals("verify group with least member",1,groupWithLeastMember.getUsers().size());

    }
    
    /*
     * Class under test for void doGrouping(Grouping, List)
     */
    public void testDoGroupingDuplicateLearnerByLearnerPerGroups()
    {
        this.groupingByLearnerPerGroups.doGrouping(userList);
        Group groupWithLeastMember = groupingByLearnerPerGroups.getGroupWithLeastMember();

        for(Iterator i = groupingByLearnerPerGroups.getGroups().iterator();i.hasNext();)
        {
            Group group = (Group)i.next();
            log.info("Number of Leaner in Group["+group.getOrderId()+"]:"+group.getUsers().size());
        }
        
        assertEquals("verify number of groups created",3,groupingByLearnerPerGroups.getGroups().size());
        assertEquals("verify group with least member",3,groupWithLeastMember.getUsers().size());
        
        this.groupingByLearnerPerGroups.doGrouping(createUser(new Integer(11),"test11"));
        this.groupingByLearnerPerGroups.doGrouping(createUser(new Integer(12),"test12"));
        assertEquals("verify number of groups created",3,groupingByLearnerPerGroups.getGroups().size());
        this.groupingByLearnerPerGroups.doGrouping(createUser(new Integer(11),"test11"));
        assertEquals("verify number of groups created",3,groupingByLearnerPerGroups.getGroups().size());
        this.groupingByLearnerPerGroups.doGrouping(createUser(new Integer(15),"test15"));
        assertEquals("verify number of groups created",4,groupingByLearnerPerGroups.getGroups().size());

    }
    //---------------------------------------------------------------------
    // Helpers
    //---------------------------------------------------------------------

    /**
     * @param userId
     * @param userName
     */
    private User createUser(Integer userId, String userName)
    {
        User user = new User();
        user.setUserId(userId);
        user.setLogin(userName);
        return user;
    }
}
