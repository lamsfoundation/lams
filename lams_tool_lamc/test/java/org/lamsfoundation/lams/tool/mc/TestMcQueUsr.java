/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-20
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.mc;

import java.util.HashSet;


/**
 * This test is designed to test all service provided by SurveyQueUsr domain 
 * class
 * @author ozgurd
 * 
 */

/**
 * Test case for TestQaQueUsr
 */

public class TestMcQueUsr extends McDataAccessTestCase
{
	
	private final Long TEST_NEW_USER_ID = new Long(100);
	
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();

    }

    
    public TestMcQueUsr(String name)
    {
        super(name);
    }
    
    
    public void testCreateNewUser()
    {
    	McSession mcSession = mcSessionDAO.findMcSessionById(TEST_SESSION_ID_OTHER);
        McQueUsr mcQueUsr= new McQueUsr(TEST_NEW_USER_ID,
    									"john",
										"John Baker",
										mcSession, 
										new HashSet());
        mcUserDAO.saveMcUser(mcQueUsr);
        
        McQueUsr mcQueUsr2= new McQueUsr(new Long(77),
				"ozgur",
				"Ozgur Demirtas",
				mcSession, 
				new HashSet());

        mcUserDAO.saveMcUser(mcQueUsr2);
    }
    
    
    public void testRemoveMcUserById()
    {
    	mcUserDAO.removeMcUserById(TEST_NEW_USER_ID);
    }
    
}
