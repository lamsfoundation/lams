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
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardContentDAO;

import org.springframework.orm.hibernate.HibernateObjectRetrievalFailureException;


//import java.util.*;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestNoticeboardContentDAO extends NbDataAccessTestCase
{
	private NoticeboardContentDAO noticeboardDAO;
	private NoticeboardSessionDAO nbSessionDAO;
	//private NoticeboardContent noticeboardContent;
    private boolean cleanContentData = true;
	private boolean cleanAllData = true;
    
	public TestNoticeboardContentDAO(String name)
	{
		super(name);
	}
	
	 /**
     * @see NbDataAccessTestCase#setUp()
     */
	 protected void setUp() throws Exception {
        super.setUp();
        //set up default noticeboard content for each test
        noticeboardDAO = (NoticeboardContentDAO) this.context.getBean("nbContentDAO");
        nbSessionDAO = (NoticeboardSessionDAO) this.context.getBean("nbSessionDAO");
        this.initNbContentData();
	    this.initNbSessionContent();
        //super.initAllData();
    }
	 
	/**
	 * @see NbDataAccessTestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
        //remove noticeboard content after each test
        if (cleanAllData)
        {
        	super.cleanAllData();
        }
        else if(cleanContentData)
        {
        	super.cleanNbContentData();
        }
    }
    
    public void testgetNbContentById()
    {
    	/* Retrieve the previously saved noticeboard object from the database */
    	
    	nbContent = noticeboardDAO.getNbContentById(getTestNoticeboardId());
    	
    	/* Ensure that all content is what is was suppose to be */
    	//assertEquals(noticeboardContent.getNoticeboardId(), TEST_NB_ID);
    	
    	assertEquals(nbContent.getTitle(), TEST_TITLE);
    	assertEquals(nbContent.getContent(), TEST_CONTENT);
    	assertEquals(nbContent.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    	assertEquals(nbContent.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    	assertEquals(nbContent.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(nbContent.isForceOffline(), TEST_FORCE_OFFLINE);
    	assertEquals(nbContent.getCreatorUserId(), TEST_CREATOR_USER_ID);
    	assertEquals(nbContent.getDateCreated(), TEST_DATE_CREATED);
    	
    	
    	
    }
    
    public void testloadNbContentById(Long nbContentId)
	{
    	/* Retrieve the previously saved noticeboard object from the database */
    	
    	nbContent = noticeboardDAO.loadNbContentById(getTestNoticeboardId());
    	
    	/* Ensure that all content is what is was suppose to be */
    	//assertEquals(noticeboardContent.getNoticeboardId(), TEST_NB_ID);
    	
    	assertEquals(nbContent.getTitle(), TEST_TITLE);
    	assertEquals(nbContent.getContent(), TEST_CONTENT);
    	assertEquals(nbContent.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    	assertEquals(nbContent.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    	assertEquals(nbContent.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(nbContent.isForceOffline(), TEST_FORCE_OFFLINE);
    	assertEquals(nbContent.getCreatorUserId(), TEST_CREATOR_USER_ID);
    	assertEquals(nbContent.getDateCreated(), TEST_DATE_CREATED);
	}
    
    public void testgetNbContentBySession()
    {
    	nbContent = noticeboardDAO.getNbContentBySession(TEST_SESSION_ID);
    	
    	assertEquals(nbContent.getTitle(), TEST_TITLE);
    	assertEquals(nbContent.getContent(), TEST_CONTENT);
    	assertEquals(nbContent.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    	assertEquals(nbContent.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    	assertEquals(nbContent.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(nbContent.isForceOffline(), TEST_FORCE_OFFLINE);
    	assertEquals(nbContent.getCreatorUserId(), TEST_CREATOR_USER_ID);
    	assertEquals(nbContent.getDateCreated(), TEST_DATE_CREATED);
    }
    
    public void testsaveNbContent()
    {
    	/** 
    	 * an object already created when setUp() is called, so dont need to save another instance 
    	 * TODO: change this, actually test the save method
    	 */
    	
    	
    	nbContent = noticeboardDAO.loadNbContentById(getTestNoticeboardId());
    	
    	assertEquals(nbContent.getNbContentId(), getTestNoticeboardId());
    	
    	assertEquals(nbContent.getTitle(), TEST_TITLE);
    	assertEquals(nbContent.getContent(), TEST_CONTENT);
    	assertEquals(nbContent.getOnlineInstructions(), TEST_ONLINE_INSTRUCTIONS);
    	assertEquals(nbContent.getOfflineInstructions(), TEST_OFFLINE_INSTRUCTIONS);
    	assertEquals(nbContent.isDefineLater(), TEST_DEFINE_LATER);
    	assertEquals(nbContent.isForceOffline(), TEST_FORCE_OFFLINE);
    	assertEquals(nbContent.getCreatorUserId(), TEST_CREATOR_USER_ID);
    	assertEquals(nbContent.getDateCreated(), TEST_DATE_CREATED);
    	
    }
    
    public void testupdateNbContent()
    {
    	// Update the noticeboard to have a new value for its content field 
    	String newContent = "New updated content";
    	
    	nbContent = noticeboardDAO.loadNbContentById(getTestNoticeboardId());
    	nbContent.setContent(newContent);
    	
    	noticeboardDAO.updateNbContent(nbContent);
    	
    	//Check whether the value has been updated
    	
    	nbContent = noticeboardDAO.getNbContentById(getTestNoticeboardId());
    	
    	assertEquals(nbContent.getContent(), newContent);
    	
    }
  
    public void testremoveNoticeboard() 
    {
    	 cleanContentData = false;
    	 cleanAllData = false;
    	 NoticeboardContent nb = noticeboardDAO.getNbContentById(TEST_NB_ID);
    	 noticeboardDAO.removeNbSessions(nb);
    	 noticeboardDAO.removeNoticeboard(TEST_NB_ID);
    	 try
		 {	
    	 	
    	 	NoticeboardContent emptyNbContent = noticeboardDAO.getNbContentById(TEST_NB_ID);
    	 	fail("An exception should be raised as the object has already been deleted");
		 }
    	 catch (HibernateObjectRetrievalFailureException e)
		 {
    	 	assertTrue(true);
		 } 
    	 
    }
 
    public void testremoveNbSessions()
    {
    	
    	cleanAllData = false;
    	cleanContentData = true;
    	
    	nbContent = noticeboardDAO.getNbContentById(getTestNoticeboardId());
    	
    	noticeboardDAO.removeNbSessions(nbContent);
    	NoticeboardContent nb = noticeboardDAO.getNbContentById(getTestNoticeboardId());
    	try
		{
    		NoticeboardSession nbSession = nbSessionDAO.getNbSessionById(TEST_SESSION_ID);
    		fail("An exception should be raised");
		}
    	catch (HibernateObjectRetrievalFailureException e)
		{
    		assertTrue(true);
		}
   	
    }
   
    
}
