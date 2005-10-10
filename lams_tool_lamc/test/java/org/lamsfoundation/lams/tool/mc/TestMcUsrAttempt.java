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

import org.lamsfoundation.lams.tool.service.ILamsToolService;



/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class TestMcUsrAttempt extends McDataAccessTestCase
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

    public TestMcUsrAttempt(String name)
    {
        super(name);
    }
    
  
    public void testCreateNewMcUsrAttempt()
    {    
    	McQueContent mcQueContent = mcQueContentDAO.getMcQueContentByUID(new Long(1));
    	
    	McOptsContent mcOptionsContent=mcOptionsContentDAO.getMcOptionsContentByUID(new Long(1));
    	
    	McQueUsr mcQueUsr=mcUserDAO.findMcUserById(TEST_MY_USER_ID); 
	    
    	
	    McUsrAttempt mcUsrAttempt= new McUsrAttempt(new Long(33),
	    											new Date(System.currentTimeMillis()),
													"Sydney",
													mcQueContent,	
													mcQueUsr,
													mcOptionsContent					
	    											);

	    McUsrAttempt mcUsrAttempt2= new McUsrAttempt(new Long(34),
				new Date(System.currentTimeMillis()),
				"ACT",
				mcQueContent,	
				mcQueUsr,
				mcOptionsContent					
				);
	    
	    mcUsrAttemptDAO.saveMcUsrAttempt(mcUsrAttempt);
	    mcUsrAttemptDAO.saveMcUsrAttempt(mcUsrAttempt2);
    }
    
}