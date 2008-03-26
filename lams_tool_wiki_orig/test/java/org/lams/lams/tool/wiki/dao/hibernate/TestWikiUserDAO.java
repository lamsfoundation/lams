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
package org.lams.lams.tool.wiki.dao.hibernate;

import org.lams.lams.tool.wiki.WikiDataAccessTestCase;
import org.lams.lams.tool.wiki.WikiUser;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiContent;

/**
 * @author mtruong
 */
public class TestWikiUserDAO extends WikiDataAccessTestCase {
    
    private WikiUser wikiUser;
    private WikiSession wikiSession;
    
    private WikiContent content;
   
    private boolean cleanContentData = true;
    
    public TestWikiUserDAO(String name)
    {
        super(name);
    }
    
    /**
     * @see WikiDataAccessTestCase#setUp()
     */
	 protected void setUp() throws Exception {
	 	super.setUp();
       
        initAllData();
     
    }
	 
	 protected void tearDown() throws Exception {
	    
	     if(cleanContentData)
	     {
	     	cleanWikiContentData(TEST_NB_ID);
	     }
	 }
	 
	

	 public void testGetWikiUserByID()
	 {
	     wikiUser = wikiUserDAO.getWikiUserByID(TEST_USER_ID);
	     
	     assertEqualsForWikiUser(wikiUser);
	     
	     Long nonExistentUserId = new Long(23321);
	     assertUserObjectIsNull(nonExistentUserId);
	 }
	 
	 public void testSaveWikiUser()
	 {
	     WikiSession sessionToReference = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
	    
	     Long newUserId = new Long(3849);
	     
	     WikiUser newUserObj = new WikiUser(newUserId,
	             											sessionToReference);
	        
	     sessionToReference.getWikiUsers().add(newUserObj);
	     wikiSessionDAO.updateWikiSession(sessionToReference);
	     
	     wikiUserDAO.saveWikiUser(newUserObj);   
	        
	     //Retrieve the newly added session object and test its values
	     wikiUser = wikiUserDAO.getWikiUserByID(newUserId);
	     
	     assertEquals(wikiUser.getUserId(), newUserId);
	     assertEquals(wikiUser.getWikiSession().getWikiSessionId(),TEST_SESSION_ID);
	 } 
	 public void testUpdateWikiUser()
	 {
	     wikiUser = wikiUserDAO.getWikiUserByID(TEST_USER_ID);
	     wikiUser.setUserStatus(WikiUser.COMPLETED);
	     wikiUserDAO.updateWikiUser(wikiUser);
	     
	     WikiUser modifiedUser = wikiUserDAO.getWikiUserByID(TEST_USER_ID);
	     assertEquals(modifiedUser.getUserStatus(), WikiUser.COMPLETED);
	 }
	 
	 public void testRemoveWikiUserById()
	 {
	    wikiUser = wikiUserDAO.getWikiUserByID(TEST_USER_ID);
	    wikiSession = wikiUser.getWikiSession();
	    wikiSession.getWikiUsers().remove(wikiUser);
	     
	     wikiUserDAO.removeWikiUser(TEST_USER_ID);
	     wikiSessionDAO.updateWikiSession(wikiSession);
	    
	     assertUserObjectIsNull(TEST_USER_ID);
	 }
	 
	 public void testRemoveWikiUser()
	 {
	     wikiUser = wikiUserDAO.getWikiUserByID(TEST_USER_ID);
		 wikiSession = wikiUser.getWikiSession();
		 wikiSession.getWikiUsers().remove(wikiUser);
		     
		 wikiUserDAO.removeWikiUser(wikiUser);
		 wikiSessionDAO.updateWikiSession(wikiSession);
		    
		 assertUserObjectIsNull(TEST_USER_ID);
	 }
	 
	 public void testGetNumberOfUsers()
	 {
	     wikiUser = wikiUserDAO.getWikiUserByID(TEST_USER_ID);
	     wikiSession = wikiUser.getWikiSession();
	     int numberOfUsers = wikiUserDAO.getNumberOfUsers(wikiSession);
	     System.out.println(numberOfUsers);
	     assertEquals(numberOfUsers, 1);
	 } 
	 
	 public void testGetWikiUserBySession()
	 {
	 	Long newSessionId = new Long(3456);
	 	
	 	WikiContent content = wikiDAO.findWikiContentById(TEST_NB_ID);
	 	
	 	WikiSession newSession = new WikiSession(newSessionId, "Session "+newSessionId, content);
	 	content.getWikiSessions().add(newSession);
	 	wikiDAO.updateWikiContent(content);
	 	wikiSessionDAO.saveWikiSession(newSession);
	 	
	 	//add the test user to a new session
	 	WikiUser existingUserNewSession = new WikiUser(TEST_USER_ID, newSession);
	 	newSession.getWikiUsers().add(existingUserNewSession);
	 	wikiSessionDAO.updateWikiSession(newSession);
	 	
	 	wikiUserDAO.saveWikiUser(existingUserNewSession);
	 	
	 	//add a different user to the session
	 	Long newUserId= new Long(3458);
	 	WikiUser newUser = new WikiUser(newUserId, newSession);
	 	newSession.getWikiUsers().add(newUser);
	 	wikiSessionDAO.updateWikiSession(newSession);
	 	wikiUserDAO.saveWikiUser(newUser);
	 	
	 	//retrieve test user by session	id	
	 	WikiUser retrievedUser = wikiUserDAO.getWikiUserBySession(TEST_USER_ID, newSessionId);
	 	assertEquals(retrievedUser.getUserId(), TEST_USER_ID);
	 	assertEquals(retrievedUser.getWikiSession().getWikiSessionId(), newSessionId);
	 	
	 	WikiUser retrievedUser2 = wikiUserDAO.getWikiUserBySession(newUserId, newSessionId);
	 	assertEquals(retrievedUser2.getUserId(), newUserId);
	 	assertEquals(retrievedUser.getWikiSession().getWikiSessionId(), newSessionId);
	 	
	 } 
	 
	 
	 
	


}
