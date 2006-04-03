/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;

import junit.framework.TestCase;


/**
 * 
 * @author Jacky Fang
 * @since  2005-4-6
 * @version
 * 
 */
public class TestGateActivityStrategy extends TestCase
{
    //---------------------------------------------------------------------
    // Instance variables
    //---------------------------------------------------------------------
	private static Logger log = Logger.getLogger(TestGateActivityStrategy.class);
	
	
    private GateActivity scheduleGate = new ScheduleGateActivity();
    private GateActivity permissionGate = new PermissionGateActivity();
    private GateActivity synchGate = new SynchGateActivity();
    
    private static List testLessonUsers = new ArrayList();
    private static final int NUM_OF_TEST_USERS = 5;
    private static final int TEST_USER_ID = 1;
    
    protected void setUp() throws Exception
    {

        //initialize lesson users
        for(int i =0; i<NUM_OF_TEST_USERS;i++)
            testLessonUsers.add(createUser(new Integer(i+1),"tester"+i));
    }
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    public void testShouldCloseSynchGate()
    {
        User testLearner = createUser(new Integer(TEST_USER_ID),"tester"+TEST_USER_ID);;
        boolean gateOpen = synchGate.shouldOpenGateFor(testLearner,testLessonUsers);
        
        assertTrue("gate should be closed",!gateOpen);
        assertEquals("there should be one learner",1,synchGate.getWaitingLearners().size());
    }
    
    public void testShouldOpenSynchGate()
    {
        //create the number of learner that is the same as the number of all 
        //lesson learners.
        List testLearners = new ArrayList();
        //initialize test learners
        for(int i =0; i<NUM_OF_TEST_USERS;i++)
            testLearners.add(createUser(new Integer(i+1),"tester"+i));
        
        boolean gateOpen = false;
        //learners arrived at synch gate one by one until the open condition
        //is met.
        for(int i =0; i<testLearners.size();i++)
        {
            User learner = (User)testLearners.get(i);
            log.info("learner "+learner.getUserId()+" arrived");
            
            gateOpen = synchGate.shouldOpenGateFor(learner,testLessonUsers);
            
            log.info("gate status: "+(gateOpen?"opened":"closed"));
            if((i+1)!=testLearners.size())
                assertEquals("verify the number of learners",i+1,synchGate.getWaitingLearners().size());
        }
        
        assertTrue("gate should be closed",gateOpen);
        assertEquals("there should be no learner waiting for an opened gate",0,synchGate.getWaitingLearners().size());
    }
    
    public void testShouldCloseScheduleGate()
    {
        User testLearner = createUser(new Integer(TEST_USER_ID),"tester"+TEST_USER_ID);;
        
        boolean gateOpen = scheduleGate.shouldOpenGateFor(testLearner,testLessonUsers);
        
        assertTrue("gate should be closed",!gateOpen);
        assertEquals("there should be one learner",1,scheduleGate.getWaitingLearners().size());
    }
    
    public void testShouldOpenScheduleGate()
    {
        User testLearner = createUser(new Integer(TEST_USER_ID),"tester"+TEST_USER_ID);;
        //set gate to open which should be triggered by lams scheduler
        scheduleGate.setGateOpen(new Boolean(true));
        
        boolean gateOpen = scheduleGate.shouldOpenGateFor(testLearner,testLessonUsers);

        assertTrue("gate should be closed",gateOpen);
        assertEquals("there should be no learner waiting for an opened gate",0,scheduleGate.getWaitingLearners().size());
    }
    
    public void testShouldClosePermissionGate()
    {
        User testLearner = createUser(new Integer(TEST_USER_ID),"tester"+TEST_USER_ID);;
        
        boolean gateOpen = permissionGate.shouldOpenGateFor(testLearner,testLessonUsers);
        
        assertTrue("gate should be closed",!gateOpen);
        assertEquals("there should be one learner",1,permissionGate.getWaitingLearners().size());
    }
    
    public void testShouldOpenPermissionGate()
    {
        User testLearner = createUser(new Integer(TEST_USER_ID),"tester"+TEST_USER_ID);;
        //set gate to open which should be triggered by lams scheduler
        permissionGate.setGateOpen(new Boolean(true));
        
        boolean gateOpen = permissionGate.shouldOpenGateFor(testLearner,testLessonUsers);

        assertTrue("gate should be closed",gateOpen);
        assertEquals("there should be no learner waiting for an opened gate",0,permissionGate.getWaitingLearners().size());
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
