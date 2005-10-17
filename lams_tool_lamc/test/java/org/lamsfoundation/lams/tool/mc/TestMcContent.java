/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-23
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.mc;


import java.util.HashSet;

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
    
 
/*
    public void testCreateNewMcContent()
    {
    	//create new mc content
    	McContent mc = new McContent();
		mc.setMcContentId(TEST_CONTENT_ID);
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
		
	    mc.setMcQueContents(new HashSet());
	    mc.setMcSessions(new HashSet());
	    
	    mcContentDAO.saveMcContent(mc);
	    assertNotNull(mc);
   }
  
    
    
    public void testCreateNewMcContentOther()
    {
    	//create new mc content
    	McContent mc = new McContent();
		mc.setMcContentId(TEST_CONTENT_ID_OTHER);
		mc.setTitle("Other - Put Title Here");
		mc.setInstructions("Other - Put instructions here.");
		mc.setQuestionsSequenced(false);
		mc.setUsernameVisible(false);
		mc.setCreatedBy(0);
		mc.setMonitoringReportTitle("Other-Monitoring Report title");
		mc.setReportTitle("Other-Report title");
		mc.setRunOffline(false);
	    mc.setDefineLater(false);
	    mc.setSynchInMonitor(false);
	    mc.setOnlineInstructions("Other- online instructions");
	    mc.setOfflineInstructions("Other- offline instructions");
	    mc.setEndLearningMessage("Other- endLearningMessage");
	    mc.setContentInUse(false);
	    mc.setRetries(false);
	    mc.setShowFeedback(false);
		
	    mc.setMcQueContents(new HashSet());
	    mc.setMcSessions(new HashSet());
	    
	    mcContentDAO.saveMcContent(mc);
	    assertNotNull(mc);
   }
   
    
    public void testRemoveMcContent()
    {
    	McContent mcContent = mcContentDAO.findMcContentById(TEST_CONTENT_ID);
    	mcContentDAO.removeMc(mcContent);
    	assertNull(mcContent);
    }
    
    
    public void testRemoveMcById()
    {
    	mcContentDAO.removeMcById(TEST_CONTENT_ID_OTHER);
    }
*/
       
    
    
}