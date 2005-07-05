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
 * Created on May 11, 2005
 * Modified: 1 July 2005
 */

package org.lamsfoundation.lams.tool.noticeboard;

import org.lamsfoundation.lams.AbstractLamsTestCase;
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardContentDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardSessionDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardUserDAO;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import java.util.Date;

/**
 * @author mtruong
 */
public class NbDataAccessTestCase extends AbstractLamsTestCase
{

    //---------------------------------------------------------------------
    // DAO instances for initializing data
    //---------------------------------------------------------------------
    private NoticeboardContentDAO noticeboardDAO;
    private NoticeboardSessionDAO nbSessionDAO;
    private NoticeboardUserDAO nbUserDAO;
   
    //---------------------------------------------------------------------
    // Domain Object instances
    //---------------------------------------------------------------------
    protected NoticeboardContent nbContent;
    protected NoticeboardSession nbSession;
    protected NoticeboardUser nbUser;
    
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
	protected final Date TEST_DATE_CREATED = new Date(System.currentTimeMillis());
	protected final Date TEST_DATE_UPDATED = new Date();
	protected final Long TEST_CREATOR_USER_ID = new Long(1300);
	
	protected final Long TEST_SESSION_ID = new Long(1400);
	protected final Date TEST_SESSION_START_DATE = new Date(System.currentTimeMillis());
	protected final Date TEST_SESSION_END_DATE = new Date(System.currentTimeMillis() + ONE_DAY);
	protected final String TEST_SESSION_STATUS = NoticeboardSession.NOT_ATTEMPTED;
	
	protected final Long TEST_USER_ID = new Long(1600);
	protected final String TEST_USERNAME = "testUsername";
	protected final String TEST_FULLNAME = "Hamish Andy";
	protected final String TEST_USER_STATUS = NoticeboardUser.INCOMPLETE;
	
	//---------------------------------------------------------------------
    // DEFAULT DATA INSERTED BY BUILD-DB ANT TASK
    //---------------------------------------------------------------------
	protected final Long DEFAULT_CONTENT_ID = new Long(2500);
	protected final String DEFAULT_TITLE = "Welcome";
	protected final String DEFAULT_CONTENT = "Welcome to these activities";
	protected final String DEFAULT_ONLINE_INSTRUCTIONS = "Enter the online instructions here";
	protected final String DEFAULT_OFFLINE_INSTRUCTIONS = "Enter the offline instructions here";
	protected final boolean DEFAULT_DEFINE_LATER = false;
	protected final boolean DEFAULT_FORCE_OFFLINE = false;
	protected final Long DEFAULT_CREATOR_USER_ID = new Long(2300);
	protected final Long DEFAULT_SESSION_ID = new Long(2400);
	protected final String DEFAULT_SESSION_STATUS = NoticeboardSession.NOT_ATTEMPTED;
	protected final Long DEFAULT_USER_ID = new Long(2600);
	protected final String DEFAULT_USERNAME = "test";
	protected final String DEFAULT_FULLNAME = "test";
	protected final String DEFAULT_USER_STATUS = NoticeboardUser.INCOMPLETE;
	
	
	
	/** Default Constructor */
	public NbDataAccessTestCase(String name)
	{
		super(name);
	}
	
	//---------------------------------------------------------------------
    // Inherited Methods
    //---------------------------------------------------------------------
	
    protected void setUp() throws Exception {
        super.setUp();
        noticeboardDAO = (NoticeboardContentDAO) this.context.getBean("nbContentDAO");
        nbSessionDAO = (NoticeboardSessionDAO) this.context.getBean("nbSessionDAO");
        nbUserDAO = (NoticeboardUserDAO) this.context.getBean("nbUserDAO");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

	 /** Define the context files. Overrides method in AbstractLamsTestCase */
    protected String[] getContextConfigLocation() {
    	return new String[] {
    	       "org/lamsfoundation/lams/tool/noticeboard/testApplicationContext.xml"};
    }
    
    /** Define the sessionFactory bean name located in testApplication.xml. */
    protected String getHibernateSessionFactoryName()
    {
    	return "nbSessionFactory";
    }
    
    protected void initNbContentData()
    {
    	  	nbContent = new NoticeboardContent(TEST_NB_ID,	
												TEST_TITLE,
												TEST_CONTENT,
												TEST_ONLINE_INSTRUCTIONS,
												TEST_OFFLINE_INSTRUCTIONS,
												TEST_DEFINE_LATER,
												TEST_FORCE_OFFLINE,
												TEST_CREATOR_USER_ID,
												TEST_DATE_CREATED,
												TEST_DATE_UPDATED);
	    	
	    	noticeboardDAO.saveNbContent(nbContent);
	 
	}
    
    protected void cleanNbContentData(Long contentId)
    {
    	noticeboardDAO.removeNoticeboard(contentId);
    	//it correspondingly removes all the sessions and users along with it.
    }
    
   
    protected Long getTestNoticeboardId()
    {
        return this.TEST_NB_ID;
    }
    
    protected void initNbSessionContent()
    {

    	NoticeboardContent nb = noticeboardDAO.findNbContentById(TEST_NB_ID);
    	
    	nbSession = new NoticeboardSession(TEST_SESSION_ID,
    									   nb,
										   TEST_SESSION_START_DATE,
										   TEST_SESSION_END_DATE,
										   TEST_SESSION_STATUS);
    		
    	nbSessionDAO.saveNbSession(nbSession);
    	
    	//associate the session with the content
    	nb.getNbSessions().add(nbSession);
    	
    }
    
    protected void initNbUserData()
    {
        NoticeboardSession ns = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        
        NoticeboardUser user = new NoticeboardUser(TEST_USER_ID,
                									ns,
                									TEST_USERNAME,
                									TEST_FULLNAME,
                									TEST_USER_STATUS);
        
        nbUserDAO.saveNbUser(user);
        
        ns.getNbUsers().add(user);
    }
   
    protected void initAllData()
    {
    	initNbContentData();
    	initNbSessionContent();    
    	initNbUserData();
    }
    
    //===========================
    // Helper Methods
    //===========================
     
    protected void assertNbSessionIsNull(Long id)
    {
        NoticeboardSession nbSession = nbSessionDAO.findNbSessionById(id);
   	   assertNull(nbSession);
    }
    
    protected void assertNbContentIsNull(Long id)
    {
        NoticeboardContent nbContent = noticeboardDAO.findNbContentById(id);
   	   assertNull(nbContent);
    }
    
    protected void assertContentEqualsTestData(NoticeboardContent content)
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
    
    protected void assertContentEqualsDefaultData(NoticeboardContent content)
    {
        	
    		assertEquals(content.getTitle(), DEFAULT_TITLE);
    		assertEquals(content.getContent(), DEFAULT_CONTENT);
    		assertEquals(content.getOnlineInstructions(), DEFAULT_ONLINE_INSTRUCTIONS);
    		assertEquals(content.getOfflineInstructions(), DEFAULT_OFFLINE_INSTRUCTIONS);
    		assertEquals(content.isDefineLater(), DEFAULT_DEFINE_LATER);
    		assertEquals(content.isForceOffline(), DEFAULT_FORCE_OFFLINE);
    		assertEquals(content.getCreatorUserId(), DEFAULT_CREATOR_USER_ID);
     } 
    
    protected void assertEqualsForSessionContent(NoticeboardSession ns)
    {
        assertEquals("Validate session id ",ns.getNbSessionId(), TEST_SESSION_ID);
		assertEquals("Validate content id ",ns.getNbContent().getNbContentId(), TEST_NB_ID);
		assertEquals("Validate session start date", ns.getSessionStartDate(), TEST_SESSION_START_DATE);
		assertEquals("Validate session end date", ns.getSessionEndDate(), TEST_SESSION_END_DATE);
		assertEquals("Validate session status", ns.getSessionStatus(), TEST_SESSION_STATUS);
    }
    
    protected void assertSessionObjectIsNull(Long sessionId)
    {
        NoticeboardSession nsObject = nbSessionDAO.findNbSessionById(sessionId);
        assertNull(nsObject);
    }
    
    protected void assertEqualsForNbUser(NoticeboardUser user)
    {
        assertEquals("Validate user id",user.getUserId(), TEST_USER_ID);
		assertEquals("Validate username",user.getUsername(), TEST_USERNAME);
		assertEquals("Validate fullname", user.getFullname(), TEST_FULLNAME);
		assertEquals("Validate user status", user.getUserStatus(), TEST_USER_STATUS);	
		assertEquals("Validate session id",user.getNbSession().getNbSessionId(), TEST_SESSION_ID);
		
    }
    
    protected void assertEqualsForDefaultNbUser(NoticeboardUser user)
    {
        assertEquals("Validate user id",user.getUserId(), DEFAULT_USER_ID);
		assertEquals("Validate username",user.getUsername(), DEFAULT_USERNAME);
		assertEquals("Validate fullname", user.getFullname(), DEFAULT_FULLNAME);
		assertEquals("Validate user status", user.getUserStatus(), DEFAULT_USER_STATUS);	
		assertEquals("Validate session id",user.getNbSession().getNbSessionId(), DEFAULT_SESSION_ID);
		
    }
  
    protected void assertUserObjectIsNull(Long userId)
    {
        NoticeboardUser user = nbUserDAO.getNbUserByID(userId);
        assertNull(user);
    }
}
