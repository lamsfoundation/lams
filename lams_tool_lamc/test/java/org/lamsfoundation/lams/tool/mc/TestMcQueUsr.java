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


package org.lamsfoundation.lams.tool.mc;

import java.util.HashSet;

import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;


/**
 * This test is designed to test all service provided by SurveyQueUsr domain 
 * class
 * @author Ozgur Demirtas
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
