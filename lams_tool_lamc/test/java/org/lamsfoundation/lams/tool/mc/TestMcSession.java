/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.mc;

import java.util.Date;
import java.util.HashSet;

import org.lamsfoundation.lams.tool.service.ILamsToolService;



/*
 * 
 * @author ozgurd
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
    }
    
}