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
import java.util.List;
import java.util.Iterator;


import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;


/**
 * @author mtruong
 *
 */
public class TestNoticeboardServicePOJO extends NbDataAccessTestCase 
{
	
	private INoticeboardService nbService = null;
/*	private NoticeboardContent nbContent;
	private NoticeboardSession nbSession;
	private NoticeboardUser nbUser; 
	private NoticeboardAttachment nbAttachment;*/
	
	private boolean cleanContentData = true;

	
	public TestNoticeboardServicePOJO(String name)
	{
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		
		nbService = (INoticeboardService)this.context.getBean("nbService");
		initAllData();
		
	}
	
	protected void tearDown() throws Exception
	{
		//delete data
		if(cleanContentData)
        {
        	cleanNbContentData(TEST_NB_ID);
        }
       
	}
	
	/* ==============================================================================
	 * Methods for access to NoticeboardContent objects
	 * ==============================================================================
	 */
		
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
		nbService.saveNoticeboard(nbContent);
		
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
	
	public void testremoveNoticeboardSessions()
	{
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
	    
	    nbService.removeNoticeboardSessionsFromContent(nbContent);
	    
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    assertNull(nbSession);
	    
	} 
	
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
	
	/* ==============================================================================
	 * Methods for access to NoticeboardSession objects
	 * ==============================================================================
	 */
	
	public void testRetrieveNoticeboardSession()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    assertEqualsForSessionContent(nbSession);
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
	    nbService.addSession(testContentId, nbSession);
	 
	    
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
	 
	    nbService.removeSession(TEST_SESSION_ID);
	 	    
	    assertSessionObjectIsNull(TEST_SESSION_ID);
	}
	
	public void testRemoveSession()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    
	    nbService.removeSession(nbSession);
		    
	    assertSessionObjectIsNull(TEST_SESSION_ID);
	    
	}
	
	public void testRemoveNoticeboardUsersFromSession()
	{
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);	    
	    
	    nbService.removeNoticeboardUsersFromSession(nbSession);
	    
	    nbUser = nbService.retrieveNoticeboardUser(TEST_USER_ID);
	    assertNull(nbUser);
	    
	}
	
	/* ==============================================================================
	 * Methods for access to NoticeboardUser objects
	 * ==============================================================================
	 */
	public void testRetrieveNoticeboardUser()
	{
	    nbUser = nbService.retrieveNoticeboardUser(TEST_USER_ID);
	    
	    assertEqualsForNbUser(nbUser);
	}

	
	public void testSaveNoticeboardUser()
	{
	    Long newUserId = new Long(8756);
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    
	    NoticeboardUser user = new NoticeboardUser(newUserId, nbSession);
	    
	  
	    nbService.saveNoticeboardUser(user);
	    
	    NoticeboardUser userInDb = nbService.retrieveNoticeboardUser(newUserId);
	    assertEquals(userInDb.getUserId(), newUserId);
	    assertEquals(userInDb.getNbSession().getNbSessionId(), TEST_SESSION_ID);
	}
	
	public void testUpdateNoticeboardUser()
	{
	    nbUser = nbService.retrieveNoticeboardUser(TEST_USER_ID);
	    nbUser.setUserStatus(NoticeboardUser.COMPLETED);
	    
	    nbService.updateNoticeboardUser(nbUser);
	    
	    NoticeboardUser updatedUser = nbService.retrieveNoticeboardUser(TEST_USER_ID);
	    
	    assertEquals(updatedUser.getUserStatus(), NoticeboardUser.COMPLETED);
	}
	
	public void testRemoveUserById()
	{
	  
	    nbService.removeUser(TEST_USER_ID);
	    
	    assertUserObjectIsNull(TEST_USER_ID);
	  
	}
	
	public void testRemoveUser()
	{
	    nbUser = nbService.retrieveNoticeboardUser(TEST_USER_ID);
	
	    nbService.removeUser(nbUser);
	    
	    assertUserObjectIsNull(TEST_USER_ID);
	  
	}
	
	public void testAddSession()
	{
	    Long toolSessionId = new Long(99);
	    NoticeboardSession newSession = new NoticeboardSession(toolSessionId);
        nbService.addSession(TEST_NB_ID, newSession);
        
        NoticeboardSession session = nbService.retrieveNoticeboardSession(toolSessionId);
        assertEquals(session.getNbContent().getNbContentId(), TEST_NB_ID);
       
	}
	
	public void testAddUser()
	{
	    Long userId = new Long(88);
	    NoticeboardUser newUser = new NoticeboardUser(userId);
        nbService.addUser(TEST_SESSION_ID, newUser);
        
        NoticeboardUser user = nbService.retrieveNoticeboardUser(userId);
        assertEquals(user.getNbSession().getNbSessionId(), TEST_SESSION_ID);
	}
	
	public void testGetSessionIdsFromContent()
	{
	    NoticeboardContent content = nbService.retrieveNoticeboard(TEST_NB_ID);
	    List list = nbService.getSessionIdsFromContent(content);
	    
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
	    NoticeboardSession session = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    int numberOfUsers = nbService.getNumberOfUsersInSession(session);
	    assertEquals(numberOfUsers, 1);
	    
	    //now add more users in the session
	    Long userId1 = new Long(34);
	    Long userId2 = new Long(35);
	    NoticeboardUser user1 = new NoticeboardUser(userId1, session);
	    NoticeboardUser user2 = new NoticeboardUser(userId2, session);
	    nbService.saveNoticeboardUser(user1);
	    nbService.saveNoticeboardUser(user2);
	    
	    //now retrieve and there should be 3 users for this session
	    nbSession = nbService.retrieveNoticeboardSession(TEST_SESSION_ID);
	    int newNumberOfUsers = nbService.getNumberOfUsersInSession(nbSession);
	    assertEquals(newNumberOfUsers, 3);
	    
	  //  int totalNumberOfLearners = nbService.calculateTotalNumberOfUsers(TEST_NB_ID);
	    //assertEquals(totalNumberOfLearners, 3);
	}
	



	public void testCalculateTotalNumberOfUsers()
	{
	    /* add more sessions relating to the test tool content id and add more users in each session
	     * then calculate the total number of users for this tool activity
	     */
	    Long sessionId1, sessionId2, sessionId3;
	    Long userId1, userId2, userId3, userId4, userId5, userId6;
	    NoticeboardSession session1, session2, session3;
	    NoticeboardUser user1Sess1, user2Sess1, user3Sess1, user4Sess2, user5Sess2, user6Sess3;
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
	    //create more sessions
	    sessionId1 = new Long(456); sessionId2 = new Long(457); sessionId3 = new Long(458);
	    userId1 = new Long(567); userId2 = new Long(568); userId3 = new Long(569);
	    userId4 = new Long(570); userId5 = new Long(571); userId6 = new Long(572);
	    
	    session1 = new NoticeboardSession(sessionId1, nbContent);
	    session2 = new NoticeboardSession(sessionId2, nbContent);
	    session3 = new NoticeboardSession(sessionId3, nbContent);
	    
	    nbContent.getNbSessions().add(session1);
	    nbContent.getNbSessions().add(session2);
	    nbContent.getNbSessions().add(session3);
	    
	    nbService.saveNoticeboard(nbContent);
	    
	    user1Sess1 = new NoticeboardUser(userId1, session1);
	    user2Sess1 = new NoticeboardUser(userId2, session1);
	    user3Sess1 = new NoticeboardUser(userId3, session1);
	    user4Sess2 = new NoticeboardUser(userId4, session2);
	    user5Sess2 = new NoticeboardUser(userId5, session2);
	    user6Sess3 = new NoticeboardUser(userId6, session3);
	    
	    session1.getNbUsers().add(user1Sess1);
	    session1.getNbUsers().add(user2Sess1);  
	    session1.getNbUsers().add(user3Sess1);
	    
	    session2.getNbUsers().add(user4Sess2);
	    session2.getNbUsers().add(user5Sess2);
	    
	    session3.getNbUsers().add(user6Sess3);
	    
	    nbService.saveNoticeboardSession(session1);
	    nbService.saveNoticeboardSession(session2);
	    nbService.saveNoticeboardSession(session3);
	    

	    //now test the function
	    int totalUsers = nbService.calculateTotalNumberOfUsers(TEST_NB_ID);
	    assertEquals("testing the total number of users", totalUsers, 7);
	    
	    
	}







/* ==============================================================================
	 * Methods for access to NoticeboardAttachment objects
	 * ==============================================================================
	 */
	
	public void testRetrieveAttachment()
	{
	    initNbAttachmentData();
	    //test retrieveAttachmentByUuid
	    nbAttachment = nbService.retrieveAttachmentByUuid(TEST_UUID);
	    
	    assertAttachmentData(nbAttachment);
	    /* test getAttachmentsFromContent which will return a list of nbAttachment ids, which we can use in the next method call to
	     retrieveAttachment which takes in the attachmentId as the parameter. */
	    List idList = nbService.getAttachmentIdsFromContent(nbService.retrieveNoticeboard(TEST_NB_ID));
	    
	    //test retrieveAttachment (by attachmentId, which was retrieved from the previous method)
	    nbAttachment = nbService.retrieveAttachment((Long)idList.get(0));
	    assertAttachmentData(nbAttachment);
	    
	    //test retrieveAttachmentByFilename;
	    nbAttachment = nbService.retrieveAttachmentByFilename(TEST_FILENAME);
	    assertAttachmentData(nbAttachment);
	}
	
	 public void testRetrieveAttachmentWithNullParameters() throws NbApplicationException
	 {
	     //retrieve nbAttachment by filename
	     try
	     {
	         nbService.retrieveAttachmentByFilename(null);
	         fail("An exception should have been thrown as a null value has been given for the argument");
	     }
	     catch (NbApplicationException e)
	     {
	         assertTrue(true);
	     }
	     
	     //retrieve nbAttachment by nbAttachment id
	     try
	     {
	         nbService.retrieveAttachment(null);
	         fail("An exception should have been thrown as a null value has been given for the argument");
	     }
	     catch (NbApplicationException e)
	     {
	         assertTrue(true);
	     }
	     
	     //retrieve nbAttachment by uuid
	     try
	     {
	         nbService.retrieveAttachmentByUuid(null);
	         fail("An exception should have been thrown as a null value has been given for the argument");
	     }
	     catch (NbApplicationException e)
	     {
	         assertTrue(true);
	     }
	     
	 }
	
	public void testSaveAttachment()
	{
	  /*  String newFilename = "NoticeboardInstructions.txt";
	    initNbAttachmentData();
	    
	    nbAttachment = nbService.retrieveAttachmentByUuid(TEST_UUID);
	    nbAttachment.setFilename(newFilename);
	    
	    nbService.saveAttachment(nbAttachment);
	    
	    nbAttachment = nbService.retrieveAttachmentByUuid(TEST_UUID);
	    assertEquals(nbAttachment.getFilename(), newFilename); */
	    String filename = "OnlineInstructions.txt";
	    boolean isOnline = true;
	    Long uuid = new Long(2);
	    
	    NoticeboardAttachment file = new NoticeboardAttachment();
	    nbContent = nbService.retrieveNoticeboard(TEST_NB_ID);
	    file.setNbContent(nbContent);
	    file.setFilename(filename);
	    file.setOnlineFile(isOnline);
	    file.setUuid(uuid);
	    
	    nbService.saveAttachment(nbContent, file);
	    
	}
	
/* This method fails because the attachment isnt really uploaded to the content repository sow hen it tries to delete from repository it fails */
		
	/*public void testRemoveAttachment() 
	{
	    initNbAttachmentData();
	    nbAttachment = nbService.retrieveAttachmentByUuid(TEST_UUID);
	    
	    try {
			nbService.removeAttachment(nbService.retrieveNoticeboard(TEST_NB_ID), nbAttachment);
		} catch (RepositoryCheckedException e) {
			fail("Repository exception thrown"+e.getMessage());
		}
	   
	    nbAttachment = nbService.retrieveAttachmentByUuid(TEST_UUID);
	    
	    assertNull(nbAttachment);
	    
	} */
	
	/*public void testGetToolDefaultContentIdBySignature()
	{
	    Long defaultToolContentId = nbService.getToolDefaultContentIdBySignature(NoticeboardConstants.TOOL_SIGNATURE);
	    assertNotNull(defaultToolContentId);
	} */
	
	public void testRetrieveNbUserBySession()
	{
		 Long newSessionId1 = new Long(3457);
		 NoticeboardSession newSession1 = new NoticeboardSession(newSessionId1);
	     nbService.addSession(TEST_NB_ID, newSession1);
	     
	     NoticeboardUser oldUserInSession1 = new NoticeboardUser(TEST_USER_ID, newSession1);
	     nbService.saveNoticeboardUser(oldUserInSession1);
	     
	     //associate the same test user to another new session
	     Long newSessionId2 = new Long(3458);
		 NoticeboardSession newSession2 = new NoticeboardSession(newSessionId2);
	     nbService.addSession(TEST_NB_ID, newSession2);
	     
	     NoticeboardUser oldUserInSession2 = new NoticeboardUser(TEST_USER_ID, newSession2);
	     nbService.saveNoticeboardUser(oldUserInSession2);
	     
	     //create another user in one of the existing sessions
	     Long newUserId = new Long(3459);
	     NoticeboardUser user2 = new NoticeboardUser(newUserId, newSession1);
	     nbService.saveNoticeboardUser(user2);
	     
	     //try to get the test user using newSessionId1
	     nbUser = nbService.retrieveNbUserBySession(TEST_USER_ID, newSessionId1);
	     assertEquals(nbUser.getUserId(), TEST_USER_ID);
	     assertEquals(nbUser.getNbSession().getNbSessionId(), newSessionId1);
	     
	     //try to get the test user using newSessionId2
	     nbUser = nbService.retrieveNbUserBySession(TEST_USER_ID, newSessionId2);
	     assertEquals(nbUser.getUserId(), TEST_USER_ID);
	     assertEquals(nbUser.getNbSession().getNbSessionId(), newSessionId2);
	     
	     //try to get the new user that was created
	     nbUser = nbService.retrieveNbUserBySession(newUserId, newSessionId1);
	     assertEquals(nbUser.getUserId(), newUserId);
	     assertEquals(nbUser.getNbSession().getNbSessionId(), newSessionId1);
	     
	     //try to get data that does not exist, should return null
	     nbUser = nbService.retrieveNbUserBySession(newUserId, newSessionId2);
	     assertNull(nbUser);
	     
	
	}
		
}
	
	