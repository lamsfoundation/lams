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
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;

import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-29
 * @version 1.1
 * 
 */
public class TestChosenGrouper extends TestCase
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestChosenGrouper.class);
	
	private ChosenGrouping chosenGrouping;
    private List userList = new ArrayList();
    private static final int NUM_OF_TEST_USERS = 10;
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        chosenGrouping = new ChosenGrouping(new Long(1),
                                            new TreeSet(),
                                            new TreeSet());
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

    /**
     * Constructor for TestChosenGrouper.
     * @param arg0
     */
    public TestChosenGrouper(String arg0)
    {
        super(arg0);
    }

    /*
     * Class under test for void doGrouping(Grouping, List)
     */
    public void testDoGroupingLearnerList()
    {
        chosenGrouping.doGrouping(userList);
        
        List groups = new ArrayList(chosenGrouping.getGroups());
        
        assertEquals("verify the number of group",1, groups.size());
        assertEquals("verify group members",NUM_OF_TEST_USERS,((Group)groups.get(0)).getUsers().size());
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
