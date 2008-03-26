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
package org.lams.lams.tool.wiki;

import org.lamsfoundation.lams.test.AbstractLamsTestCase;
import org.lams.lams.tool.wiki.dao.hibernate.WikiContentDAO;
import org.lams.lams.tool.wiki.dao.hibernate.WikiSessionDAO;
import org.lams.lams.tool.wiki.dao.hibernate.WikiUserDAO;
import org.lams.lams.tool.wiki.dao.hibernate.WikiAttachmentDAO;
import org.lams.lams.tool.wiki.WikiContent;
import java.util.Date;
import org.lams.lams.tool.wiki.WikiConstants;

/**
 * @author mtruong
 */
public class WikiDataAccessTestCase extends AbstractLamsTestCase
{

    //---------------------------------------------------------------------
    // DAO instances for initializing data
    //---------------------------------------------------------------------
    protected WikiContentDAO wikiDAO;
    protected WikiSessionDAO wikiSessionDAO;
    protected WikiUserDAO wikiUserDAO;
    protected WikiAttachmentDAO attachmentDAO;
   
    //---------------------------------------------------------------------
    // Domain Object instances
    //---------------------------------------------------------------------
    protected WikiContent wikiContent;
    protected WikiSession wikiSession;
    protected WikiUser wikiUser;
    protected WikiAttachment wikiAttachment;

    
    //---------------------------------------------------------------------
    // DATA USED FOR TESTING PURPOSES ONLY
    //---------------------------------------------------------------------
    
    protected final long ONE_DAY = 60 * 60 * 1000 * 24;
    
    protected final Long TEST_NB_ID = new Long(1500);
    protected final Long TEST_COPYNB_ID = new Long(3500);
    
    protected final String TEST_TITLE = "Test Title";
    protected final String TEST_CONTENT = "Welcome! We hope you enjoy the activities that are set out.";
	protected final String TEST_ONLINE_INSTRUCTIONS = "Put your online instructions here";
	protected final String TEST_OFFLINE_INSTRUCTIONS = "Put your offline instructions here";
	protected final boolean TEST_DEFINE_LATER = false;
	protected final boolean TEST_FORCE_OFFLINE = false;
	protected final boolean TEST_CONTENT_IN_USE = false;
	protected final Date TEST_DATE_CREATED = new Date(System.currentTimeMillis());
	protected final Date TEST_DATE_UPDATED = new Date();
	protected final Long TEST_CREATOR_USER_ID = new Long(1300);
	
	protected final Long TEST_SESSION_ID = new Long(1400);
	protected final String TEST_SESSION_NAME = "Session 1400";
	protected final Date TEST_SESSION_START_DATE = new Date(System.currentTimeMillis());
	protected final Date TEST_SESSION_END_DATE = new Date(System.currentTimeMillis() + ONE_DAY);
	protected final String TEST_SESSION_STATUS = WikiSession.NOT_ATTEMPTED;
	
	protected final Long TEST_USER_ID = new Long(1600);
	protected final String TEST_USERNAME = "testUsername";
	protected final String TEST_FULLNAME = "Test User Fullname";
	protected final String TEST_USER_STATUS = WikiUser.INCOMPLETE;
	
	protected final String TEST_FILENAME = "testFilename";
	protected final boolean TEST_IS_ONLINE_FILE = true;
	protected final Long TEST_UUID =  new Long(2002);
	    
	
	//---------------------------------------------------------------------
    // DEFAULT DATA INSERTED BY BUILD-DB ANT TASK
    //---------------------------------------------------------------------
	protected final Long DEFAULT_CONTENT_ID = WikiConstants.DEFAULT_CONTENT_ID;
	protected final String DEFAULT_TITLE = "Welcome";
	protected final String DEFAULT_CONTENT = "Welcome to these activities";
	protected final String DEFAULT_ONLINE_INSTRUCTIONS = "Enter the online instructions here";
	protected final String DEFAULT_OFFLINE_INSTRUCTIONS = "Enter the offline instructions here";
	protected final boolean DEFAULT_DEFINE_LATER = false;
	protected final boolean DEFAULT_FORCE_OFFLINE = false;
	protected final boolean DEFAULT_CONTENT_IN_USE = false;
	protected final Long DEFAULT_CREATOR_USER_ID = WikiConstants.DEFAULT_CREATOR_ID;
	protected final Long DEFAULT_SESSION_ID = WikiConstants.DEFAULT_SESSION_ID;
	protected final String DEFAULT_SESSION_STATUS = WikiSession.INCOMPLETE;
	protected final Long DEFAULT_USER_ID = new Long(2600);
	protected final String DEFAULT_USERNAME = "test";
	protected final String DEFAULT_FULLNAME = "test";
	protected final String DEFAULT_USER_STATUS = WikiUser.INCOMPLETE;
	
	
	
	/** Default Constructor */
	public WikiDataAccessTestCase(String name)
	{
		super(name);
	}
	
	//---------------------------------------------------------------------
    // Inherited Methods
    //---------------------------------------------------------------------
	
    protected void setUp() throws Exception {
    	super.setUp();
        wikiDAO = (WikiContentDAO) this.context.getBean("wikiContentDAO");
        wikiSessionDAO = (WikiSessionDAO) this.context.getBean("wikiSessionDAO");
        wikiUserDAO = (WikiUserDAO) this.context.getBean("wikiUserDAO");
        attachmentDAO = (WikiAttachmentDAO)this.context.getBean("wikiAttachmentDAO");
    }

    protected void tearDown() throws Exception 
    {
    	super.tearDown();
    }

	 /** Define the context files. Overrides method in AbstractLamsTestCase */
    protected String[] getContextConfigLocation() {
    	return new String[] {
    	        "org/lamsfoundation/lams/localApplicationContext.xml",
    			"org/lamsfoundation/lams/lesson/lessonApplicationContext.xml",
    			"org/lamsfoundation/lams/toolApplicationContext.xml",
    			"org/lamsfoundation/lams/learning/learningApplicationContext.xml",
    			"org/lams/lams/tool/wiki/testApplicationContext.xml"};
    }
    
    /** Define the sessionFactory bean name located in testApplication.xml. */
    protected String getHibernateSessionFactoryName()
    {
    	return "wikiSessionFactory";
    }
    
    protected void initWikiContentData()
    {
    	  	wikiContent = new WikiContent(TEST_NB_ID,	
												TEST_TITLE,
												TEST_CONTENT,
												TEST_ONLINE_INSTRUCTIONS,
												TEST_OFFLINE_INSTRUCTIONS,
												TEST_DEFINE_LATER,
												TEST_CONTENT_IN_USE,
												TEST_FORCE_OFFLINE,
												TEST_CREATOR_USER_ID,
												TEST_DATE_CREATED,
												TEST_DATE_UPDATED);
	    	
	    	wikiDAO.saveWikiContent(wikiContent);
	 
	}
    
    protected void cleanWikiContentData(Long contentId)
    {
    	wikiDAO.removeWiki(contentId);
    	//it correspondingly removes all the sessions and users along with it.
    }
    
   
    protected Long getTestWikiId()
    {
        return this.TEST_NB_ID;
    }
    
    protected void initWikiSessionContent()
    {

    	WikiContent wiki = wikiDAO.findWikiContentById(TEST_NB_ID);
    	
    	wikiSession = new WikiSession(TEST_SESSION_ID,
    									   TEST_SESSION_NAME,
    									   wiki,
										   TEST_SESSION_START_DATE,
										   TEST_SESSION_END_DATE,
										   TEST_SESSION_STATUS);
    	wikiSessionDAO.saveWikiSession(wikiSession);
    	
    	//associate the session with the content
    	wiki.getWikiSessions().add(wikiSession);
    	
    }
    
    protected void initWikiUserData()
    {
        WikiSession ns = wikiSessionDAO.findWikiSessionById(TEST_SESSION_ID);
        
        WikiUser user = new WikiUser(TEST_USER_ID,
                									ns,
                									TEST_USERNAME,
                									TEST_FULLNAME,
                									TEST_USER_STATUS);
        
        wikiUserDAO.saveWikiUser(user);
        
        ns.getWikiUsers().add(user);
    }
    
    /* TODO: have to upload attachment to repository */
    protected void initWikiAttachmentData()
    {
        WikiAttachment attachment = new WikiAttachment();
        WikiContent wiki = wikiDAO.findWikiContentById(TEST_NB_ID);
        
        attachment.setFilename(TEST_FILENAME);
        attachment.setOnlineFile(TEST_IS_ONLINE_FILE);
        attachment.setWikiContent(wikiContent);
        attachment.setUuid(TEST_UUID);
	     
	    attachmentDAO.saveAttachment(attachment);
	    
	    wiki.getWikiAttachments().add(attachment);        
    }
   
    protected void initAllData()
    {
    	initWikiContentData();
    	initWikiSessionContent();    
    	initWikiUserData();
    }
    
    protected void restoreDefaultContent(Long defaultContentId)
    {
        wikiContent = new WikiContent(defaultContentId,	
                DEFAULT_TITLE,
                DEFAULT_CONTENT,
                DEFAULT_ONLINE_INSTRUCTIONS,
                DEFAULT_OFFLINE_INSTRUCTIONS,
                DEFAULT_DEFINE_LATER,
                DEFAULT_FORCE_OFFLINE,
                DEFAULT_CONTENT_IN_USE,
				null,
				TEST_DATE_CREATED,
				null);

        wikiDAO.saveWikiContent(wikiContent);
 
    }
    
    //===========================
    // Helper Methods
    //===========================
     
    protected void assertWikiSessionIsNull(Long id)
    {
        WikiSession wikiSession = wikiSessionDAO.findWikiSessionById(id);
   	   assertNull(wikiSession);
    }
    
    protected void assertWikiContentIsNull(Long id)
    {
        WikiContent wikiContent = wikiDAO.findWikiContentById(id);
   	   assertNull(wikiContent);
    }
    
    protected void assertContentEqualsTestData(WikiContent content)
    {
        	
    		assertEquals(content.getTitle(), TEST_TITLE);
    		assertEquals(content.getContent(), TEST_CONTENT);
    		assertEquals(content.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    		assertEquals(content.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    		assertEquals(content.isDefineLater(), TEST_DEFINE_LATER);
    		assertEquals(content.isForceOffline(), TEST_FORCE_OFFLINE);
    		assertEquals(content.getCreatorUserId(), TEST_CREATOR_USER_ID);
    		assertEquals(content.getDateCreated(), TEST_DATE_CREATED);
    }
    
    protected void assertContentEqualsDefaultData(WikiContent content)
    {
        	
    		assertEquals(content.getTitle(), DEFAULT_TITLE);
    		assertEquals(content.getContent(), DEFAULT_CONTENT);
    		assertEquals(content.getOnlineInstructions(), DEFAULT_ONLINE_INSTRUCTIONS);
    		assertEquals(content.getOfflineInstructions(), DEFAULT_OFFLINE_INSTRUCTIONS);
    		assertEquals(content.isDefineLater(), DEFAULT_DEFINE_LATER);
    		assertEquals(content.isForceOffline(), DEFAULT_FORCE_OFFLINE);
    		     } 
    
    protected void assertEqualsForSessionContent(WikiSession ns)
    {
        assertEquals("Validate session id ",ns.getWikiSessionId(), TEST_SESSION_ID);
		assertEquals("Validate content id ",ns.getWikiContent().getWikiContentId(), TEST_NB_ID);
		assertEquals("Validate session start date", ns.getSessionStartDate(), TEST_SESSION_START_DATE);
		assertEquals("Validate session end date", ns.getSessionEndDate(), TEST_SESSION_END_DATE);
		assertEquals("Validate session status", ns.getSessionStatus(), TEST_SESSION_STATUS);
    }
    
    protected void assertSessionObjectIsNull(Long sessionId)
    {
        WikiSession nsObject = wikiSessionDAO.findWikiSessionById(sessionId);
        assertNull(nsObject);
    }
    
    protected void assertEqualsForWikiUser(WikiUser user)
    {
        assertEquals("Validate user id",user.getUserId(), TEST_USER_ID);
		assertEquals("Validate username",user.getUsername(), TEST_USERNAME);
		assertEquals("Validate fullname", user.getFullname(), TEST_FULLNAME);
		assertEquals("Validate user status", user.getUserStatus(), TEST_USER_STATUS);	
		assertEquals("Validate session id",user.getWikiSession().getWikiSessionId(), TEST_SESSION_ID);
		
    }
    
    protected void assertEqualsForDefaultWikiUser(WikiUser user)
    {
        assertEquals("Validate user id",user.getUserId(), DEFAULT_USER_ID);
		assertEquals("Validate username",user.getUsername(), DEFAULT_USERNAME);
		assertEquals("Validate fullname", user.getFullname(), DEFAULT_FULLNAME);
		assertEquals("Validate user status", user.getUserStatus(), DEFAULT_USER_STATUS);	
		assertEquals("Validate session id",user.getWikiSession().getWikiSessionId(), DEFAULT_SESSION_ID);
		
    }
  
    protected void assertUserObjectIsNull(Long userId)
    {
        WikiUser user = wikiUserDAO.getWikiUserByID(userId);
        assertNull(user);
    }
    
    protected void assertAttachmentData(WikiAttachment attachment)
	 {
	     assertEquals("Validating the filename:", attachment.getFilename(), TEST_FILENAME);
	     assertEquals("Validating whether it is an online file", attachment.isOnlineFile(), TEST_IS_ONLINE_FILE);
	     assertEquals("Validating the tool content id", attachment.getWikiContent().getWikiContentId(), TEST_NB_ID);
	     assertEquals("Validating the Uuid", attachment.getUuid(), TEST_UUID);
	 }
}
