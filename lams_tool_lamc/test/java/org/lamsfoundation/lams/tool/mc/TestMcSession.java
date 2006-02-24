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

import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.service.ILamsToolService;



/*
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class TestMcSession extends McDataAccessTestCase
{
	public org.lamsfoundation.lams.tool.dao.IToolDAO toolDAO;
	public ILamsToolService lamsToolService;
	
	protected void setUp() throws Exception
    {
        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public TestMcSession(String name)
    {
        super(name);
    }
    
/*  
    public void testCreateNewMcSession()
    {    
	    McContent mcContent = mcContentDAO.findMcContentById(TEST_CONTENT_ID);
	    
	    McSession mcSession = new McSession(TEST_SESSION_ID_OTHER,
                                   new Date(System.currentTimeMillis()),
                                   new Date(System.currentTimeMillis()+ ONE_DAY),
                                   this.NOT_ATTEMPTED, 
                                   mcContent,
                                   new HashSet());
    
	    mcSessionDAO.saveMcSession(mcSession);
	    assertEquals(mcSession.getMcSessionId(),new Long(21));
	    
	    
	    McSession mcSession2 = new McSession(TEST_SESSION_ID,
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis()+ ONE_DAY),
                this.INCOMPLETE , 
                mcContent,
                new HashSet());

		mcSessionDAO.saveMcSession(mcSession);
		assertEquals(mcSession.getMcSessionId(),new Long(20));
	    
		McSession mcSession3 = new McSession(new Long(55),
                                   new Date(System.currentTimeMillis()),
                                   new Date(System.currentTimeMillis()+ ONE_DAY),
                                   this.NOT_ATTEMPTED, 
                                   mcContent,
                                   new HashSet());
    
	    mcSessionDAO.saveMcSession(mcSession3);
    }
    
    */
    
    public void testFindMcSession()
    {
    	McSession mcSession=mcSessionDAO.findMcSessionById(new Long(555));
    	System.out.println("Is null session?: " + mcSession); 
    }
    
}