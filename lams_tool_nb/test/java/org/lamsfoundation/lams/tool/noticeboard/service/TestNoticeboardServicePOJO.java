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
 * @author Mai Ling Truong
 * modified: June 22, 2005
 */
package org.lamsfoundation.lams.tool.noticeboard.service;

import java.util.Date;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
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
	private NoticeboardContent nbContent;
	private NoticeboardSession nbSession;
	
	private boolean cleanContentData = true;

	
	public TestNoticeboardServicePOJO(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		//setup some data
		nbService = (INoticeboardService)this.context.getBean("nbService");
		super.initAllData();
	}
	
	protected void tearDown() throws Exception
	{
		super.tearDown();
		
		//delete data
		if(cleanContentData)
        {
        	super.cleanNbContentData(TEST_NB_ID);
        }
       
	}

	public void testRetrieveNoticeboardByUID()
	{
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
	    Long uid = nbContent.getUid();
	    
	    NoticeboardContent testObject = nbService.retrieveNoticeboardByUID(uid);
		
	    assertContentEqualsTestData(testObject);
	}
	
	public void testRetrieveNoticeboard()
	{
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
		
	    assertContentEqualsTestData(nbContent);
	
	}
	
	public void testUpdateNoticeboard()
	{
	    String newContent = "New updated content";
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
	    nbContent.setContent(newContent);
		
		//save the new changes
		nbService.updateNoticeboard(nbContent);
		
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
	
	public void testSaveNoticeboard()
	{
	    Long testToolContentId = new Long(8000);
		String testTitle = "TestCase: saveNoticeboard()";
		String testContent = "This is to test the saveNoticeboard() function";
		String testOnlineInstructions = "online instructions";
		String testOfflineInstructions = "offline instructions";
		
		NoticeboardContent content = new NoticeboardContent(testToolContentId,
		        											testTitle,
		        											testContent,
		        											testOnlineInstructions,
		        											testOfflineInstructions,
		        											new Date(System.currentTimeMillis()));
		nbService.saveNoticeboard(content);
		
		NoticeboardContent retrievedObject = nbService.retrieveNoticeboard(testToolContentId);
		assertEquals(retrievedObject.getTitle(), testTitle);
		assertEquals(retrievedObject.getContent(), testContent);
		assertEquals(retrievedObject.getOnlineInstructions(), testOnlineInstructions);
		assertEquals(retrievedObject.getOfflineInstructions(), testOfflineInstructions);
		
		//remove test data
		
		nbService.removeNoticeboard(testToolContentId);
	}
	
	/**
	 * TODO: finish this one after testing sesssion
	 *
	 */
	/*public void testremoveNoticeboardSessions()
	{
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
	    nbService.removeNoticeboardSessions(nbContent);
	    
	} */
	
	public void testRemoveNoticeboardByID()
	{
	    cleanContentData = false;
	    nbService.removeNoticeboard(TEST_NB_ID);
	    
	    NoticeboardContent nb = nbService.retrieveNoticeboard(TEST_NB_ID);
	    assertNull(nb);
	}
	
	public void testRemoveNoticeboard()
	{
	    cleanContentData = false;
	    nbService.removeNoticeboard(nbService.retrieveNoticeboard(TEST_NB_ID));
	    
	    NoticeboardContent nb = nbService.retrieveNoticeboard(TEST_NB_ID);
	    assertNull(nb);
	}
	
	public void testRetrieveNoticeboardSession()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    assertEqualsForSessionContent(nbSession);
	}
	
	public void testRetrieveNoticeboardSessionByUID()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    Long uid = nbSession.getUid();
	    
	    NoticeboardSession testSession = nbService.retrieveNoticeboardSessionByUID(uid);
		
	    assertEqualsForSessionContent(testSession);
	}
	
	public void testSaveNoticeboardSession()
	{
	    Long testSessionId = new Long(9000);
	    Long testContentId = new Long(9500);
	    Date created = new Date(System.currentTimeMillis());
	    
	    NoticeboardContent nbContent = new NoticeboardContent();
	    nbContent.setNbContentId(testContentId);
	    nbService.saveNoticeboard(nbContent);
	    
	   NoticeboardSession nbSession = new NoticeboardSession(testSessionId,
	            												nbContent,
	            												created,
	            												NoticeboardSession.NOT_ATTEMPTED); 
	   nbContent.getNbSessions().add(nbSession);
	   nbService.saveNoticeboardSession(nbSession);
	 
	    
	   NoticeboardSession retrievedSession = nbService.retrieveNoticeboardSession(testSessionId);
	    
	    assertEquals(retrievedSession.getNbContent().getNbContentId(), testContentId);
	    assertEquals(retrievedSession.getSessionStartDate(), created);
	    assertEquals(retrievedSession.getSessionStatus(), NoticeboardSession.NOT_ATTEMPTED);
	    
	    //remove test data
	    nbService.removeNoticeboard(testContentId);
	}
	
	public void testUpdateNoticeboardSession()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	   	Date date = new Date(System.currentTimeMillis());
	   
	   	nbSession.setSessionStatus(NoticeboardSession.COMPLETED);
	   	nbSession.setSessionEndDate(date);
	   	
	   	nbService.updateNoticeboardSession(nbSession);
	   	
	   	NoticeboardSession retrievedSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	   	
	   	assertEquals(retrievedSession.getSessionStatus(), NoticeboardSession.COMPLETED);
	   	assertEquals(retrievedSession.getSessionEndDate(), date);
	}
	
	public void testRemoveSessionByID()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    
	    nbContent = nbSession.getNbContent();
	    nbContent.getNbSessions().remove(nbSession);
	    
	    nbService.removeSession(TEST_SESSION_ID);
	    nbService.updateNoticeboard(nbContent);
	    
	    assertSessionObjectIsNull(TEST_SESSION_ID);
	}
	
	public void testRemoveSession()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    
	    nbContent = nbSession.getNbContent();
	    nbContent.getNbSessions().remove(nbSession);
	    
	    nbService.removeSession(nbSession);
	    nbService.updateNoticeboard(nbContent);
	    
	    assertSessionObjectIsNull(TEST_SESSION_ID);
	    
	}
	
	public void testRemoveSessionByUID()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    Long uid = nbSession.getUid();
	    
	    nbContent = nbSession.getNbContent();
	    nbContent.getNbSessions().remove(nbSession);
	    
	    nbService.removeSessionByUID(uid);
	    nbService.updateNoticeboard(nbContent);
	    
	    assertSessionObjectIsNull(TEST_SESSION_ID);

	}

}
	
	