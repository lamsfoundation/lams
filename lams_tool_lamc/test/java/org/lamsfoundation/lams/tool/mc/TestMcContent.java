/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
package org.lamsfoundation.lams.tool.mc;


import org.lamsfoundation.lams.tool.service.ILamsToolService;



/*
 * 
 * @author Ozgur Demirtas
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