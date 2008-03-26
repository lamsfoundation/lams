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
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.dao.hibernate.WikiContentDAO;
import org.lams.lams.tool.wiki.WikiSession;

/**
 * @author mtruong
 *
 * JUnit Test Cases to test the WikiContentDAO class
 */
public class TestWikiContentDAO extends WikiDataAccessTestCase
{
	private boolean cleanContentData = true;
    
	public TestWikiContentDAO(String name)
	{
		super(name);
	}
	
	 /**
     * @see WikiDataAccessTestCase#setUp()
     */
	 protected void setUp() throws Exception {
        super.setUp();

        //set up default wiki content for each test
       	initAllData();
    }
	 
	/**
	 * @see WikiDataAccessTestCase#tearDown()
	 */
    protected void tearDown() throws Exception 
    {    	
        //remove wiki content after each test
       if(cleanContentData)
        {
        	cleanWikiContentData(TEST_NB_ID);
        }
    }
   
   public void testfindWikiContentByID()
    {
       wikiContent = wikiDAO.findWikiContentById(TEST_NB_ID);
       
        assertContentEqualsTestData(wikiContent);
    	
    
    	 // Test to see if trying to retrieve a non-existent object would 
    	 // return null or not.
    	 
    	Long nonExistentId = new Long(88777);
    	assertWikiContentIsNull(nonExistentId);
       
//	   WikiContent wiki = new WikiContent(new Long(3600),	
//				TEST_TITLE,
//				TEST_CONTENT,
//				TEST_ONLINE_INSTRUCTIONS,
//				TEST_OFFLINE_INSTRUCTIONS,
//				TEST_DEFINE_LATER,
//				TEST_CONTENT_IN_USE,
//				TEST_FORCE_OFFLINE,
//				TEST_CREATOR_USER_ID,
//				TEST_DATE_CREATED,
//				TEST_DATE_UPDATED);
//
//	   wikiDAO.saveWikiContent(wiki);
	   
	 // wikiDAO.removeWiki(new Long(3600));
	//   wikiDAO.removeWiki(wikiDAO.findWikiContentById(new Long(3600)));
	   
	   
    } 
   
   public void testremoveWiki()
   {
       cleanContentData = false;     
       
       wikiContent = wikiDAO.findWikiContentById(TEST_NB_ID);
       
       wikiDAO.removeWiki(wikiContent);
       
       assertWikiSessionIsNull(TEST_SESSION_ID); //check if child table is deleted
  	   assertWikiContentIsNull(TEST_NB_ID);
   } 
   
   public void testremoveWikiById()
   {
       cleanContentData = false;
  	 	
	   
  	 	wikiDAO.removeWiki(TEST_NB_ID);
  	 	
  	 	assertWikiSessionIsNull(TEST_SESSION_ID);
  	 	assertWikiContentIsNull(TEST_NB_ID);
   }
   
   public void testgetWikiContentBySession()
   {
	   	wikiContent = wikiDAO.getWikiContentBySession(TEST_SESSION_ID);
	   	
	   	assertContentEqualsTestData(wikiContent);
   }
   

   public void testsaveWikiContent()
   {
   	/** 
   	 * an object already created when setUp() is called, so dont need to save another instance 
   	 * TODO: change this, actually test the save method
   	 */
   	
   	wikiContent = wikiDAO.findWikiContentById(getTestWikiId());
   	
   	assertContentEqualsTestData(wikiContent);
   	
   }
   
   public void testupdateWikiContent()
   {
   	// Update the wiki to have a new value for its content field 
   	String newContent = "New updated content";
   	
   	wikiContent = wikiDAO.findWikiContentById(getTestWikiId());
   	wikiContent.setContent(newContent);
   	
   	wikiDAO.updateWikiContent(wikiContent);
   	
   	//Check whether the value has been updated
   	
   	wikiContent = wikiDAO.findWikiContentById(getTestWikiId());
   	
   	assertEquals(wikiContent.getContent(), newContent);
   	
   } 
      
   public void testremoveWikiSessions()
   {
   	
	   	wikiContent = wikiDAO.findWikiContentById(getTestWikiId());
	   	
	   	
	   	wikiDAO.removeWikiSessions(wikiContent);
	   	wikiContent.getWikiSessions().clear(); //Have to remove/empty the collection before deleting it.
	   	//otherwise exception will occur
	   	wikiDAO.updateWikiContent(wikiContent);
	   	WikiContent wiki = wikiDAO.findWikiContentById(getTestWikiId());
	   	assertNotNull(wiki);
	   	assertWikiSessionIsNull(TEST_SESSION_ID);   	
   }
   
   public void testAddSession()
   {
       Long newSessionId = new Long(87);
       WikiSession newSession = new WikiSession(newSessionId);
       
       wikiDAO.addWikiSession(TEST_NB_ID, newSession);
       
       WikiSession retrievedSession = wikiSessionDAO.findWikiSessionById(newSessionId);
       
       assertEquals(retrievedSession.getWikiContent().getWikiContentId(), TEST_NB_ID);
       
   }
   
   
 

  
}
