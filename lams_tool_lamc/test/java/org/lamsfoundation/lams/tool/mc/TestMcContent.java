/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.mc;

import java.util.TreeSet;

import org.lamsfoundation.lams.tool.service.ILamsToolService;



/*
 * 
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


public class TestMcContent extends McDataAccessTestCase
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

    public TestMcContent(String name)
    {
        super(name);
    }
    
  
    public void testCreateNewMcContent()
    {
    	//create new mc content
    	McContent mc = new McContent();
		mc.setMcContentId(new Long(4));
		mc.setTitle("New - Put Title Here");
		mc.setInstructions("New - Put instructions here.");
		mc.setQuestionsSequenced(false);
		mc.setUsernameVisible(false);
		mc.setCreatedBy(0);
		mc.setMonitoringReportTitle("New-Monitoring Report title");
		mc.setReportTitle("New-Report title");
		mc.setRunOffline(false);
	    mc.setDefineLater(false);
	    mc.setSynchInMonitor(false);
	    mc.setOnlineInstructions("New- online instructions");
	    mc.setOfflineInstructions("New- offline instructions");
	    mc.setEndLearningMessage("New- endLearningMessage");
	    mc.setContentInUse(false);
	    mc.setRetries(false);
	    mc.setShowFeedback(false);
	    mc.setShowTopUsers(false);
		
	    mc.setMcQueContents(new TreeSet());
	    mc.setMcSessions(new TreeSet());
	    
	    mcContentDAO.saveMcContent(mc);
   }
    
}