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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;

/**
 * @author mtruong
 */
public class TestNoticeboardUserDAO extends NbDataAccessTestCase {
    
    private NoticeboardUser nbUser;
    private NoticeboardSession nbSession;
    
    private NoticeboardContent content;
   
    private boolean cleanContentData = true;
    
    public TestNoticeboardUserDAO(String name)
    {
        super(name);
    }
    
    /**
     * @see NbDataAccessTestCase#setUp()
     */
	 protected void setUp() throws Exception {
	 	super.setUp();
       
        initAllData();
     
    }
	 
	 protected void tearDown() throws Exception {
	    
	     if(cleanContentData)
	     {
	     	cleanNbContentData(TEST_NB_ID);
	     }
	 }
	 
	

	 public void testGetNbUserByID()
	 {
	     nbUser = nbUserDAO.getNbUserByID(TEST_USER_ID);
	     
	     assertEqualsForNbUser(nbUser);
	     
	     Long nonExistentUserId = new Long(23321);
	     assertUserObjectIsNull(nonExistentUserId);
	 }
	 
	 public void testSaveNbUser()
	 {
	     NoticeboardSession sessionToReference = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
	    
	     Long newUserId = new Long(3849);
	     
	     NoticeboardUser newUserObj = new NoticeboardUser(newUserId,
	             											sessionToReference);
	        
	     sessionToReference.getNbUsers().add(newUserObj);
	     nbSessionDAO.updateNbSession(sessionToReference);
	     
	     nbUserDAO.saveNbUser(newUserObj);   
	        
	     //Retrieve the newly added session object and test its values
	     nbUser = nbUserDAO.getNbUserByID(newUserId);
	     
	     assertEquals(nbUser.getUserId(), newUserId);
	     assertEquals(nbUser.getNbSession().getNbSessionId(),TEST_SESSION_ID);
	 } 
	 public void testUpdateNbUser()
	 {
	     nbUser = nbUserDAO.getNbUserByID(TEST_USER_ID);
	     nbUser.setUserStatus(NoticeboardUser.COMPLETED);
	     nbUserDAO.updateNbUser(nbUser);
	     
	     NoticeboardUser modifiedUser = nbUserDAO.getNbUserByID(TEST_USER_ID);
	     assertEquals(modifiedUser.getUserStatus(), NoticeboardUser.COMPLETED);
	 }
	 
	 public void testRemoveNbUserById()
	 {
	    nbUser = nbUserDAO.getNbUserByID(TEST_USER_ID);
	    nbSession = nbUser.getNbSession();
	    nbSession.getNbUsers().remove(nbUser);
	     
	     nbUserDAO.removeNbUser(TEST_USER_ID);
	     nbSessionDAO.updateNbSession(nbSession);
	    
	     assertUserObjectIsNull(TEST_USER_ID);
	 }
	 
	 public void testRemoveNbUser()
	 {
	     nbUser = nbUserDAO.getNbUserByID(TEST_USER_ID);
		 nbSession = nbUser.getNbSession();
		 nbSession.getNbUsers().remove(nbUser);
		     
		 nbUserDAO.removeNbUser(nbUser);
		 nbSessionDAO.updateNbSession(nbSession);
		    
		 assertUserObjectIsNull(TEST_USER_ID);
	 }
	 
	 public void testGetNumberOfUsers()
	 {
	     nbUser = nbUserDAO.getNbUserByID(TEST_USER_ID);
	     nbSession = nbUser.getNbSession();
	     int numberOfUsers = nbUserDAO.getNumberOfUsers(nbSession);
	     System.out.println(numberOfUsers);
	     assertEquals(numberOfUsers, 1);
	 } 
	 
	 public void testGetNbUserBySession()
	 {
	 	Long newSessionId = new Long(3456);
	 	
	 	NoticeboardContent content = noticeboardDAO.findNbContentById(TEST_NB_ID);
	 	
	 	NoticeboardSession newSession = new NoticeboardSession(newSessionId, content);
	 	content.getNbSessions().add(newSession);
	 	noticeboardDAO.updateNbContent(content);
	 	nbSessionDAO.saveNbSession(newSession);
	 	
	 	//add the test user to a new session
	 	NoticeboardUser existingUserNewSession = new NoticeboardUser(TEST_USER_ID, newSession);
	 	newSession.getNbUsers().add(existingUserNewSession);
	 	nbSessionDAO.updateNbSession(newSession);
	 	
	 	nbUserDAO.saveNbUser(existingUserNewSession);
	 	
	 	//add a different user to the session
	 	Long newUserId= new Long(3458);
	 	NoticeboardUser newUser = new NoticeboardUser(newUserId, newSession);
	 	newSession.getNbUsers().add(newUser);
	 	nbSessionDAO.updateNbSession(newSession);
	 	nbUserDAO.saveNbUser(newUser);
	 	
	 	//retrieve test user by session	id	
	 	NoticeboardUser retrievedUser = nbUserDAO.getNbUserBySession(TEST_USER_ID, newSessionId);
	 	assertEquals(retrievedUser.getUserId(), TEST_USER_ID);
	 	assertEquals(retrievedUser.getNbSession().getNbSessionId(), newSessionId);
	 	
	 	NoticeboardUser retrievedUser2 = nbUserDAO.getNbUserBySession(newUserId, newSessionId);
	 	assertEquals(retrievedUser2.getUserId(), newUserId);
	 	assertEquals(retrievedUser.getNbSession().getNbSessionId(), newSessionId);
	 	
	 } 
	 
	 
	 
	


}
