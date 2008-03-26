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

import java.util.Date;
import java.util.List;
import java.util.Iterator;


import org.lams.lams.tool.wiki.WikiDataAccessTestCase;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;



/**
 * @author mtruong
 *
 * JUnit Test Cases to test the WikiSessionDAO class
 */
public class TestWikiSessionDAO extends WikiDataAccessTestCase {
	
	
	private WikiSession wikiSession = null;
	private WikiContent wikiContent = null;
	
	
	//private boolean cleanSessionContentData = false;
	private boolean cleanContentData = true;
	
	public TestWikiSessionDAO(String name)
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
	 
	/**
	 * @see WikiDataAccessTestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        
        if(cleanContentData)
        {
        	super.cleanWikiContentData(TEST_NB_ID);
        }
       
    }

  /* public void testgetWikiSessionByUID()
    {
       wikiSession = wikiSessionDAO.getWikiSessionByUID(new Long(1)); //default test data which is always in db
        
       assertEquals(wikiSession.getWikiSessionId(), DEFAULT_SESSION_ID);
       assertEquals(wikiSession.getSessionStatus(), DEFAULT_SESSION_STATUS);
        
    } */
    
    public void testfindWikiSessionById()
    {
        wikiSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        assertEqualsForSessionContent(wikiSession);
        
        Long nonExistentSessionId = new Long(7657);
        assertSessionObjectIsNull(nonExistentSessionId); 
    }
    
    public void testsaveWikiSession()
    {
        WikiContent wikiContentToReference = wikiDAO.findWikiContentById(TEST_NB_ID);
        
        Long newSessionId = new Long(2222);
        Date newDateCreated = new Date(System.currentTimeMillis());
        WikiSession newSessionObject = new WikiSession(newSessionId,
        															"Session "+newSessionId,
                													wikiContentToReference,
                													newDateCreated,
                													WikiSession.NOT_ATTEMPTED);
        
        wikiContentToReference.getWikiSessions().add(newSessionObject);
        wikiDAO.updateWikiContent(wikiContentToReference);
        
        wikiSessionDAO.saveWikiSession(newSessionObject);
        
        //Retrieve the newly added session object and test its values
        
        wikiSession = wikiSessionDAO.findWikiSessionById(newSessionId);
        
        assertEquals(wikiSession.getWikiSessionId(), newSessionId);
        assertEquals(wikiSession.getSessionStartDate(), newDateCreated);
        
    } 
    
    public void testupdateWikiSession()
    {
        wikiSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        
        wikiSession.setSessionStatus(WikiSession.COMPLETED);
        
        wikiSessionDAO.updateWikiSession(wikiSession);
        
        WikiSession updatedSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        
        assertEquals(updatedSession.getSessionStatus(), WikiSession.COMPLETED);
    } 
    
  /*  public void testremoveWikiSessionByUID()
    {
        WikiSession existingSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        Long uid = existingSession.getUid();
        
        WikiContent referencedContent = existingSession.getWikiContent();
        
        wikiSessionDAO.removeWikiSessionByUID(uid);
        referencedContent.getWikiSessions().remove(existingSession);
        
        wikiDAO.updateWikiContent(referencedContent);
        
        assertSessionObjectIsNull(TEST_SESSION_ID);
    } */
    
    
    public void testremoveWikiSessionById()
    {
        wikiSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        wikiContent = wikiSession.getWikiContent();
        wikiContent.getWikiSessions().remove(wikiSession);
        
        wikiSessionDAO.removeWikiSession(TEST_SESSION_ID);
        
        wikiDAO.updateWikiContent(wikiContent);
        
        assertSessionObjectIsNull(TEST_SESSION_ID);
        
    }
    
    public void testremoveWikiSession()
    {
        wikiSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        wikiContent = wikiSession.getWikiContent();
        wikiContent.getWikiSessions().remove(wikiSession);
        
        wikiSessionDAO.removeWikiSession(wikiSession);
        
        wikiDAO.updateWikiContent(wikiContent);
        
        assertSessionObjectIsNull(TEST_SESSION_ID);
    } 
    
    public void testGetWikiSessionByUser()
    {
        wikiSession = wikiSessionDAO.getWikiSessionByUser(TEST_USER_ID);
        assertEqualsForSessionContent(wikiSession);
    }
    
    public void testRemoveWikiUsers()
    {
        wikiSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        
        wikiSessionDAO.removeWikiUsers(wikiSession);
        wikiSession.getWikiUsers().clear();
        wikiSessionDAO.updateWikiSession(wikiSession);
        
        WikiSession ns = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        
        assertNotNull(ns);
        assertUserObjectIsNull(TEST_USER_ID);
    }
    
    public void testAddUsers()
    {
        Long newUserId = new Long(123);
        WikiUser newUser = new WikiUser(newUserId);
        
        wikiSessionDAO.addWikiUsers(TEST_SESSION_ID, newUser);
        
        WikiUser retrievedUser = wikiUserDAO.getWikiUserByID(newUserId);
        
        assertEquals(retrievedUser.getWikiSession().getWikiSessionId(), TEST_SESSION_ID);
    }
    
    public void testGetSessionsFromContent()
    {
        wikiSession = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        WikiContent content = wikiSession.getWikiContent();
        List list = wikiSessionDAO.getSessionsFromContent(content);
        assertEquals(list.size(), 1);
        
        Iterator i = list.iterator();
        
        while (i.hasNext())
        {
            Long sessionID = (Long)i.next();
            assertEquals(sessionID, TEST_SESSION_ID);
        }
        
    }
   
}
