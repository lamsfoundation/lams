/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

/*
 * Created on May 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.service;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;



/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestToolContentManager extends NbDataAccessTestCase {
	
	private ToolContentManager nbContentManager = null;
	//private INoticeboardContentDAO nbContentDAO = null;
	//private NoticeboardContent nb;
	private INoticeboardService nbService = null;
	
	private boolean cleanContentData = true;
	private boolean cleanAllData = true;
	private boolean cleanCopyContent = false;
	
	
	public TestToolContentManager(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		//setup some data
		nbContentManager = (ToolContentManager)this.context.getBean("nbService");
		nbService = (INoticeboardService)this.context.getBean("nbService");
		this.initNbContentData();
	    this.initNbSessionContent();
	}
	
	protected void tearDown() throws Exception
	{
		super.tearDown();
		
		//delete data
		 if (cleanAllData)
	     {
		 	super.cleanAllData();
	     }
	     else
	     {
	     	 if(cleanContentData)
	     	 {
	     	 	super.cleanNbContentData();
	     	 }
	     }
	     
		 if(cleanCopyContent)
	     {
	     	super.cleanNbCopiedContent();
	     }
     }
	
	public void testcopyToolContent()
	{
		//ensure that the copied data is deleted after use
		cleanCopyContent = true;
		
		nbContentManager.copyToolContent(TEST_NB_ID, TEST_COPYNB_ID);
		
		nbContent = nbService.retrieveNoticeboard(TEST_COPYNB_ID);
		
		// check whether this new object contains the right content
		assertEquals(nbContent.getNbContentId(), TEST_COPYNB_ID);
	    	
	    assertEquals(nbContent.getTitle(), TEST_TITLE);
	   	assertEquals(nbContent.getContent(), TEST_CONTENT);
	   	assertEquals(nbContent.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
	   	assertEquals(nbContent.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
	   	assertEquals(nbContent.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(nbContent.isForceOffline(), TEST_FORCE_OFFLINE);	    	
    	assertEquals(nbContent.getCreatorUserId(), TEST_CREATOR_USER_ID);
	    assertEquals(nbContent.getDateCreated(), TEST_DATE_CREATED); 
	}
	
	public void testsetAsDefineLater()
	{
		nbContentManager.setAsDefineLater(TEST_NB_ID);
		
		nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
		
		assertTrue(nbContent.isDefineLater());
	}
	
	public void testsetAsRunOffline()
	{
		nbContentManager.setAsRunOffline(TEST_NB_ID);
		nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
		assertTrue(nbContent.isForceOffline());
	}
	
	public void testremoveToolContent()
	{
		cleanAllData = false;
		cleanContentData = false;
		nbContentManager.removeToolContent(TEST_NB_ID);
		
			nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
			assertNull(nbContent);
	
	}
	
	
}
