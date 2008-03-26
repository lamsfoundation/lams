/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lams.lams.tool.wiki.service;

import java.util.Date;
import java.util.List;
import java.util.Iterator;


import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lams.lams.tool.wiki.WikiApplicationException;
import org.lams.lams.tool.wiki.WikiDataAccessTestCase;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiConstants;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;
import org.lams.lams.tool.wiki.WikiAttachment;
import org.lams.lams.tool.wiki.service.IWikiService;


/**
 * @author mtruong
 *
 */
public class TestWikiServicePOJO extends WikiDataAccessTestCase 
{
	
	private IWikiService wikiService = null;
/*	private WikiContent wikiContent;
	private WikiSession wikiSession;
	private WikiUser wikiUser; 
	private WikiAttachment wikiAttachment;*/
	
	private boolean cleanContentData = true;

	
	public TestWikiServicePOJO(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		
		wikiService = (IWikiService)this.context.getBean("wikiService");
		initAllData();
		
	}
	
	protected void tearDown() throws Exception
	{
		//delete data
		if(cleanContentData)
        {
        	cleanWikiContentData(TEST_NB_ID);
        }
       
	}
	
	/* ==============================================================================
	 * Methods for access to WikiContent objects
	 * ==============================================================================
	 */
		
	public void testRetrieveWiki()
	{
	    wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
		
	    assertContentEqualsTestData(wikiContent);
	
	}
	
	public void testUpdateWiki()
	{
	    String newContent = "New updated content";
	    wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
	    wikiContent.setContent(newContent);
		
		//save the new changes
		wikiService.saveWiki(wikiContent);
		
		//check whether the changes has been saved
		WikiContent newWiki = wikiService.retrieveWiki(TEST_NB_ID);
		
		assertEquals(newWiki.getTitle(), TEST_TITLE);
    	assertEquals(newWiki.getContent(), newContent);
    	assertEquals(newWiki.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    	assertEquals(newWiki.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    	assertEquals(newWiki.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(newWiki.isForceOffline(), TEST_FORCE_OFFLINE);
    	assertEquals(newWiki.getCreatorUserId(), TEST_CREATOR_USER_ID);
    	assertEquals(newWiki.getDateCreated(), TEST_DATE_CREATED); 
	}
	
	public void testSaveWiki()
	{
	    Long testToolContentId = new Long(8000);
		String testTitle = "TestCase: saveWiki()";
		String testContent = "This is to test the saveWiki() function";
		String testOnlineInstructions = "online instructions";
		String testOfflineInstructions = "offline instructions";
		
		WikiContent content = new WikiContent(testToolContentId,
		        											testTitle,
		        											testContent,
		        											testOnlineInstructions,
		        											testOfflineInstructions,
		        											new Date(System.currentTimeMillis()));
		wikiService.saveWiki(content);
		
		WikiContent retrievedObject = wikiService.retrieveWiki(testToolContentId);
		assertEquals(retrievedObject.getTitle(), testTitle);
		assertEquals(retrievedObject.getContent(), testContent);
		assertEquals(retrievedObject.getOnlineInstructions(), testOnlineInstructions);
		assertEquals(retrievedObject.getOfflineInstructions(), testOfflineInstructions);
		
		//remove test data
		
		wikiService.removeWiki(testToolContentId);
	}
	
	public void testremoveWikiSessions()
	{
	    wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
	    
	    wikiService.removeWikiSessionsFromContent(wikiContent);
	    
	    wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	    assertNull(wikiSession);
	    
	} 
	
	public void testRemoveWikiByID()
	{
	    cleanContentData = false;
	    wikiService.removeWiki(TEST_NB_ID);
	    
	    WikiContent wiki = wikiService.retrieveWiki(TEST_NB_ID);
	    assertNull(wiki);
	}
	
	public void testRemoveWiki()
	{
	    cleanContentData = false;
	    wikiService.removeWiki(wikiService.retrieveWiki(TEST_NB_ID));
	    
	    WikiContent wiki = wikiService.retrieveWiki(TEST_NB_ID);
	    assertNull(wiki);
	}
	
	/* ==============================================================================
	 * Methods for access to WikiSession objects
	 * ==============================================================================
	 */
	
	public void testRetrieveWikiSession()
	{
	    wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	    assertEqualsForSessionContent(wikiSession);
	}
	
	
	public void testSaveWikiSession()
	{
	    Long testSessionId = new Long(9000);
	    Long testContentId = new Long(9500);
	    Date created = new Date(System.currentTimeMillis());
	    
	    WikiContent wikiContent = new WikiContent();
	    wikiContent.setWikiContentId(testContentId);
	    wikiService.saveWiki(wikiContent);
	    
	   WikiSession wikiSession = new WikiSession(testSessionId,
			   													"Session "+testSessionId,
	            												wikiContent,
	            												created,
	            												WikiSession.NOT_ATTEMPTED); 
	    wikiService.addSession(testContentId, wikiSession);
	 
	    
	   	WikiSession retrievedSession = wikiService.retrieveWikiSession(testSessionId);
	    
	    assertEquals(retrievedSession.getWikiContent().getWikiContentId(), testContentId);
	    assertEquals(retrievedSession.getSessionStartDate(), created);
	    assertEquals(retrievedSession.getSessionStatus(), WikiSession.NOT_ATTEMPTED);
	    
	    //remove test data
	    wikiService.removeWiki(testContentId);
	}
	
	public void testUpdateWikiSession()
	{
	    wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	   	Date date = new Date(System.currentTimeMillis());
	   
	   	wikiSession.setSessionStatus(WikiSession.COMPLETED);
	   	wikiSession.setSessionEndDate(date);
	   	
	   	wikiService.updateWikiSession(wikiSession);
	   	
	   	WikiSession retrievedSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	   	
	   	assertEquals(retrievedSession.getSessionStatus(), WikiSession.COMPLETED);
	   	assertEquals(retrievedSession.getSessionEndDate(), date);
	}
	
	public void testRemoveSessionByID()
	{
	 
	    wikiService.removeSession(TEST_SESSION_ID);
	 	    
	    assertSessionObjectIsNull(TEST_SESSION_ID);
	}
	
	public void testRemoveSession()
	{
	    wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	    
	    wikiService.removeSession(wikiSession);
		    
	    assertSessionObjectIsNull(TEST_SESSION_ID);
	    
	}
	
	public void testRemoveWikiUsersFromSession()
	{
	    wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);	    
	    
	    wikiService.removeWikiUsersFromSession(wikiSession);
	    
	    wikiUser = wikiService.retrieveWikiUser(TEST_USER_ID);
	    assertNull(wikiUser);
	    
	}
	
	/* ==============================================================================
	 * Methods for access to WikiUser objects
	 * ==============================================================================
	 */
	public void testRetrieveWikiUser()
	{
	    wikiUser = wikiService.retrieveWikiUser(TEST_USER_ID);
	    
	    assertEqualsForWikiUser(wikiUser);
	}

	
	public void testSaveWikiUser()
	{
	    Long newUserId = new Long(8756);
	    wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	    
	    WikiUser user = new WikiUser(newUserId, wikiSession);
	    
	  
	    wikiService.saveWikiUser(user);
	    
	    WikiUser userInDb = wikiService.retrieveWikiUser(newUserId);
	    assertEquals(userInDb.getUserId(), newUserId);
	    assertEquals(userInDb.getWikiSession().getWikiSessionId(), TEST_SESSION_ID);
	}
	
	public void testUpdateWikiUser()
	{
	    wikiUser = wikiService.retrieveWikiUser(TEST_USER_ID);
	    wikiUser.setUserStatus(WikiUser.COMPLETED);
	    
	    wikiService.updateWikiUser(wikiUser);
	    
	    WikiUser updatedUser = wikiService.retrieveWikiUser(TEST_USER_ID);
	    
	    assertEquals(updatedUser.getUserStatus(), WikiUser.COMPLETED);
	}
	
	public void testRemoveUserById()
	{
	  
	    wikiService.removeUser(TEST_USER_ID);
	    
	    assertUserObjectIsNull(TEST_USER_ID);
	  
	}
	
	public void testRemoveUser()
	{
	    wikiUser = wikiService.retrieveWikiUser(TEST_USER_ID);
	
	    wikiService.removeUser(wikiUser);
	    
	    assertUserObjectIsNull(TEST_USER_ID);
	  
	}
	
	public void testAddSession()
	{
	    Long toolSessionId = new Long(99);
	    WikiSession newSession = new WikiSession(toolSessionId);
        wikiService.addSession(TEST_NB_ID, newSession);
        
        WikiSession session = wikiService.retrieveWikiSession(toolSessionId);
        assertEquals(session.getWikiContent().getWikiContentId(), TEST_NB_ID);
       
	}
	
	public void testAddUser()
	{
	    Long userId = new Long(88);
	    WikiUser newUser = new WikiUser(userId);
        wikiService.addUser(TEST_SESSION_ID, newUser);
        
        WikiUser user = wikiService.retrieveWikiUser(userId);
        assertEquals(user.getWikiSession().getWikiSessionId(), TEST_SESSION_ID);
	}
	
	public void testGetSessionIdsFromContent()
	{
	    WikiContent content = wikiService.retrieveWiki(TEST_NB_ID);
	    List list = wikiService.getSessionIdsFromContent(content);
	    
	    assertEquals(list.size(), 1);
	    Iterator i = list.iterator();
        
        while (i.hasNext())
        {
            Long sessionID = (Long)i.next();
            assertEquals(sessionID, TEST_SESSION_ID);
        }
	}
	
	public void testGetNumberOfUsersInSession()
	{
	    WikiSession session = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	    int numberOfUsers = wikiService.getNumberOfUsersInSession(session);
	    assertEquals(numberOfUsers, 1);
	    
	    //now add more users in the session
	    Long userId1 = new Long(34);
	    Long userId2 = new Long(35);
	    WikiUser user1 = new WikiUser(userId1, session);
	    WikiUser user2 = new WikiUser(userId2, session);
	    wikiService.saveWikiUser(user1);
	    wikiService.saveWikiUser(user2);
	    
	    //now retrieve and there should be 3 users for this session
	    wikiSession = wikiService.retrieveWikiSession(TEST_SESSION_ID);
	    int newNumberOfUsers = wikiService.getNumberOfUsersInSession(wikiSession);
	    assertEquals(newNumberOfUsers, 3);
	    
	  //  int totalNumberOfLearners = wikiService.calculateTotalNumberOfUsers(TEST_NB_ID);
	    //assertEquals(totalNumberOfLearners, 3);
	}
	



	public void testCalculateTotalNumberOfUsers()
	{
	    /* add more sessions relating to the test tool content id and add more users in each session
	     * then calculate the total number of users for this tool activity
	     */
	    Long sessionId1, sessionId2, sessionId3;
	    Long userId1, userId2, userId3, userId4, userId5, userId6;
	    WikiSession session1, session2, session3;
	    WikiUser user1Sess1, user2Sess1, user3Sess1, user4Sess2, user5Sess2, user6Sess3;
	    wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
	    //create more sessions
	    sessionId1 = new Long(456); sessionId2 = new Long(457); sessionId3 = new Long(458);
	    userId1 = new Long(567); userId2 = new Long(568); userId3 = new Long(569);
	    userId4 = new Long(570); userId5 = new Long(571); userId6 = new Long(572);
	    
	    session1 = new WikiSession(sessionId1, "Session "+sessionId1, wikiContent);
	    session2 = new WikiSession(sessionId2, "Session "+sessionId1, wikiContent);
	    session3 = new WikiSession(sessionId3, "Session "+sessionId1, wikiContent);
	    
	    wikiContent.getWikiSessions().add(session1);
	    wikiContent.getWikiSessions().add(session2);
	    wikiContent.getWikiSessions().add(session3);
	    
	    wikiService.saveWiki(wikiContent);
	    
	    user1Sess1 = new WikiUser(userId1, session1);
	    user2Sess1 = new WikiUser(userId2, session1);
	    user3Sess1 = new WikiUser(userId3, session1);
	    user4Sess2 = new WikiUser(userId4, session2);
	    user5Sess2 = new WikiUser(userId5, session2);
	    user6Sess3 = new WikiUser(userId6, session3);
	    
	    session1.getWikiUsers().add(user1Sess1);
	    session1.getWikiUsers().add(user2Sess1);  
	    session1.getWikiUsers().add(user3Sess1);
	    
	    session2.getWikiUsers().add(user4Sess2);
	    session2.getWikiUsers().add(user5Sess2);
	    
	    session3.getWikiUsers().add(user6Sess3);
	    
	    wikiService.saveWikiSession(session1);
	    wikiService.saveWikiSession(session2);
	    wikiService.saveWikiSession(session3);
	    

	    //now test the function
	    int totalUsers = wikiService.calculateTotalNumberOfUsers(TEST_NB_ID);
	    assertEquals("testing the total number of users", totalUsers, 7);
	    
	    
	}







/* ==============================================================================
	 * Methods for access to WikiAttachment objects
	 * ==============================================================================
	 */
	
	public void testRetrieveAttachment()
	{
	    initWikiAttachmentData();
	    //test retrieveAttachmentByUuid
	    wikiAttachment = wikiService.retrieveAttachmentByUuid(TEST_UUID);
	    
	    assertAttachmentData(wikiAttachment);
	    /* test getAttachmentsFromContent which will return a list of wikiAttachment ids, which we can use in the next method call to
	     retrieveAttachment which takes in the attachmentId as the parameter. */
	    List idList = wikiService.getAttachmentIdsFromContent(wikiService.retrieveWiki(TEST_NB_ID));
	    
	    //test retrieveAttachment (by attachmentId, which was retrieved from the previous method)
	    wikiAttachment = wikiService.retrieveAttachment((Long)idList.get(0));
	    assertAttachmentData(wikiAttachment);
	    
	    //test retrieveAttachmentByFilename;
	    wikiAttachment = wikiService.retrieveAttachmentByFilename(TEST_FILENAME);
	    assertAttachmentData(wikiAttachment);
	}
	
	 public void testRetrieveAttachmentWithNullParameters() throws WikiApplicationException
	 {
	     //retrieve wikiAttachment by filename
	     try
	     {
	         wikiService.retrieveAttachmentByFilename(null);
	         fail("An exception should have been thrown as a null value has been given for the argument");
	     }
	     catch (WikiApplicationException e)
	     {
	         assertTrue(true);
	     }
	     
	     //retrieve wikiAttachment by wikiAttachment id
	     try
	     {
	         wikiService.retrieveAttachment(null);
	         fail("An exception should have been thrown as a null value has been given for the argument");
	     }
	     catch (WikiApplicationException e)
	     {
	         assertTrue(true);
	     }
	     
	     //retrieve wikiAttachment by uuid
	     try
	     {
	         wikiService.retrieveAttachmentByUuid(null);
	         fail("An exception should have been thrown as a null value has been given for the argument");
	     }
	     catch (WikiApplicationException e)
	     {
	         assertTrue(true);
	     }
	     
	 }
	
	public void testSaveAttachment()
	{
	  /*  String newFilename = "WikiInstructions.txt";
	    initWikiAttachmentData();
	    
	    wikiAttachment = wikiService.retrieveAttachmentByUuid(TEST_UUID);
	    wikiAttachment.setFilename(newFilename);
	    
	    wikiService.saveAttachment(wikiAttachment);
	    
	    wikiAttachment = wikiService.retrieveAttachmentByUuid(TEST_UUID);
	    assertEquals(wikiAttachment.getFilename(), newFilename); */
	    String filename = "OnlineInstructions.txt";
	    boolean isOnline = true;
	    Long uuid = new Long(2);
	    
	    WikiAttachment file = new WikiAttachment();
	    wikiContent = wikiService.retrieveWiki(TEST_NB_ID);
	    file.setWikiContent(wikiContent);
	    file.setFilename(filename);
	    file.setOnlineFile(isOnline);
	    file.setUuid(uuid);
	    
	    wikiService.saveAttachment(wikiContent, file);
	    
	}
	
/* This method fails because the attachment isnt really uploaded to the content repository sow hen it tries to delete from repository it fails */
		
	/*public void testRemoveAttachment() 
	{
	    initWikiAttachmentData();
	    wikiAttachment = wikiService.retrieveAttachmentByUuid(TEST_UUID);
	    
	    try {
			wikiService.removeAttachment(wikiService.retrieveWiki(TEST_NB_ID), wikiAttachment);
		} catch (RepositoryCheckedException e) {
			fail("Repository exception thrown"+e.getMessage());
		}
	   
	    wikiAttachment = wikiService.retrieveAttachmentByUuid(TEST_UUID);
	    
	    assertNull(wikiAttachment);
	    
	} */
	
	/*public void testGetToolDefaultContentIdBySignature()
	{
	    Long defaultToolContentId = wikiService.getToolDefaultContentIdBySignature(WikiConstants.TOOL_SIGNATURE);
	    assertNotNull(defaultToolContentId);
	} */
	
	public void testRetrieveWikiUserBySession()
	{
		 Long newSessionId1 = new Long(3457);
		 WikiSession newSession1 = new WikiSession(newSessionId1);
	     wikiService.addSession(TEST_NB_ID, newSession1);
	     
	     WikiUser oldUserInSession1 = new WikiUser(TEST_USER_ID, newSession1);
	     wikiService.saveWikiUser(oldUserInSession1);
	     
	     //associate the same test user to another new session
	     Long newSessionId2 = new Long(3458);
		 WikiSession newSession2 = new WikiSession(newSessionId2);
	     wikiService.addSession(TEST_NB_ID, newSession2);
	     
	     WikiUser oldUserInSession2 = new WikiUser(TEST_USER_ID, newSession2);
	     wikiService.saveWikiUser(oldUserInSession2);
	     
	     //create another user in one of the existing sessions
	     Long newUserId = new Long(3459);
	     WikiUser user2 = new WikiUser(newUserId, newSession1);
	     wikiService.saveWikiUser(user2);
	     
	     //try to get the test user using newSessionId1
	     wikiUser = wikiService.retrieveWikiUserBySession(TEST_USER_ID, newSessionId1);
	     assertEquals(wikiUser.getUserId(), TEST_USER_ID);
	     assertEquals(wikiUser.getWikiSession().getWikiSessionId(), newSessionId1);
	     
	     //try to get the test user using newSessionId2
	     wikiUser = wikiService.retrieveWikiUserBySession(TEST_USER_ID, newSessionId2);
	     assertEquals(wikiUser.getUserId(), TEST_USER_ID);
	     assertEquals(wikiUser.getWikiSession().getWikiSessionId(), newSessionId2);
	     
	     //try to get the new user that was created
	     wikiUser = wikiService.retrieveWikiUserBySession(newUserId, newSessionId1);
	     assertEquals(wikiUser.getUserId(), newUserId);
	     assertEquals(wikiUser.getWikiSession().getWikiSessionId(), newSessionId1);
	     
	     //try to get data that does not exist, should return null
	     wikiUser = wikiService.retrieveWikiUserBySession(newUserId, newSessionId2);
	     assertNull(wikiUser);
	     
	
	}
		
}
	
	
