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
 * Created on May 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.service;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;


/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestNoticeboardServicePOJO extends NbDataAccessTestCase 
{
	
	private INoticeboardService nbService = null;
	//private INoticeboardContentDAO nbContentDAO = null;
	private NoticeboardContent nb;
	
	private boolean cleanContentData = true;
	private boolean cleanAllData = true;
	
	public TestNoticeboardServicePOJO(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		//setup some data
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
	        else if(cleanContentData)
	        {
	        	super.cleanNbContentData();
	        }
       
	}
	
	public void testretrieveNoticeboard()
	{
		nb = nbService.retrieveNoticeboard(TEST_NB_ID);
		
		assertEquals(nb.getNbContentId(), TEST_NB_ID);
    	
    	assertEquals(nb.getTitle(), TEST_TITLE);
    	assertEquals(nb.getContent(), TEST_CONTENT);
    	assertEquals(nb.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    	assertEquals(nb.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    	assertEquals(nb.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(nb.isForceOffline(), TEST_FORCE_OFFLINE);
    	assertEquals(nb.getCreatorUserId(), TEST_CREATOR_USER_ID);
    	assertEquals(nb.getDateCreated(), TEST_DATE_CREATED); 
	
	}
	
	public void testupdateNoticeboard()
	{
		String newContent = "New updated content";
		nb = nbService.retrieveNoticeboard(TEST_NB_ID);
		nb.setContent(newContent);
		
		//save the new changes
		nbService.updateNoticeboard(nb);
		
		//check whether the changes has been saved
		NoticeboardContent newNb = nbService.retrieveNoticeboard(TEST_NB_ID);
		
		assertEquals(newNb.getTitle(), TEST_TITLE);
    	assertEquals(newNb.getContent(), newContent);
    	assertEquals(newNb.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    	assertEquals(newNb.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    	assertEquals(newNb.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(newNb.isForceOffline(), TEST_FORCE_OFFLINE);
    	assertEquals(newNb.getCreatorUserId(), TEST_CREATOR_USER_ID);
    	assertEquals(newNb.getDateCreated(), TEST_DATE_CREATED); 
	}
	
	public void testsaveNoticeboard()
	{
		//same as test retrieve
		/**
		 * TODO: rewrite these tests
		 */
		
	}
	
	public void testremoveNoticeboard()
	{
		
		cleanAllData = false;
		cleanContentData = false;
		//remove associated sessions first, then remove the noticeboard content
		nb = nbService.retrieveNoticeboard(TEST_NB_ID);
		
		nbService.removeNoticeboardSessions(nb);
		nbService.removeNoticeboard(TEST_NB_ID);
		
    	NoticeboardContent emptyNbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
   	 	assertNull(emptyNbContent);
	} 
	
//	public void testremoveNoticeboardSessions()
	//{
		/**
		 * TODO: do this later when the session stuff is added into the survey service
		 */
	//}
	
	
	
}
