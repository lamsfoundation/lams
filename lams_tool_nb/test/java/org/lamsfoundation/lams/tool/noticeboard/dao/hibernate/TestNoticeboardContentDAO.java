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
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardContentDAO;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;

/**
 * @author mtruong
 *
 * JUnit Test Cases to test the NoticeboardContentDAO class
 */
public class TestNoticeboardContentDAO extends NbDataAccessTestCase
{
	private NoticeboardContentDAO noticeboardDAO;
	private NoticeboardSessionDAO nbSessionDAO;
	private boolean cleanContentData = true;
    
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
       	super.initAllData();
    }
	 
	/**
	 * @see NbDataAccessTestCase#tearDown()
	 */
    protected void tearDown() throws Exception 
    {
        super.tearDown();
        //remove noticeboard content after each test
        if(cleanContentData)
        {
        	super.cleanNbContentData(TEST_NB_ID);
        }
    }
   
   public void testfindNbContentByID()
    {
        nbContent = noticeboardDAO.findNbContentById(TEST_NB_ID);
       
        assertContentEqualsTestData(nbContent);
    	
    
    	 // Test to see if trying to retrieve a non-existent object would 
    	 // return null or not.
    	 
    	Long nonExistentId = new Long(88777);
    	assertNbContentIsNull(nonExistentId);
    } 
 

   public void testremoveNoticeboard()
   {
       cleanContentData = false;
      
       
       nbContent = noticeboardDAO.findNbContentById(TEST_NB_ID);
       
       noticeboardDAO.removeNoticeboard(nbContent);
       
       assertNbSessionIsNull(TEST_SESSION_ID); //check if child table is deleted
  	   assertNbContentIsNull(TEST_NB_ID);
   } 
  
  public void testremoveNoticeboardById()
    {
        cleanContentData = false;
   	 	
	   
   	 	noticeboardDAO.removeNoticeboard(TEST_NB_ID);
   	 	
   	 	assertNbSessionIsNull(TEST_SESSION_ID);
   	 	assertNbContentIsNull(TEST_NB_ID);
    } 
    
    
    public void testgetNbContentBySession()
    {
    	nbContent = noticeboardDAO.getNbContentBySession(TEST_SESSION_ID);
    	
    	assertContentEqualsTestData(nbContent);
    }
    
    public void testsaveNbContent()
    {
    	/** 
    	 * an object already created when setUp() is called, so dont need to save another instance 
    	 * TODO: change this, actually test the save method
    	 */
    	
    
    	nbContent = noticeboardDAO.findNbContentById(getTestNoticeboardId());
    	
    	assertContentEqualsTestData(nbContent);
    	
    } 
    
    public void testupdateNbContent()
    {
    	// Update the noticeboard to have a new value for its content field 
    	String newContent = "New updated content";
    	
    	nbContent = noticeboardDAO.findNbContentById(getTestNoticeboardId());
    	nbContent.setContent(newContent);
    	
    	noticeboardDAO.updateNbContent(nbContent);
    	
    	//Check whether the value has been updated
    	
    	nbContent = noticeboardDAO.findNbContentById(getTestNoticeboardId());
    	
    	assertEquals(nbContent.getContent(), newContent);
    	
    }  
    
    public void testremoveNbSessions()
    {
    	
    	nbContent = noticeboardDAO.findNbContentById(getTestNoticeboardId());
    	
    	
    	noticeboardDAO.removeNbSessions(nbContent);
    	nbContent.getNbSessions().clear(); //Have to remove/empty the collection before deleting it.
    	//otherwise exception will occur
    	noticeboardDAO.updateNbContent(nbContent);
    	NoticeboardContent nb = noticeboardDAO.findNbContentById(getTestNoticeboardId());
    	assertNotNull(nb);
    	assertNbSessionIsNull(TEST_SESSION_ID);   	
    }
   
    
    public void testAddSession()
    {
        Long newSessionId = new Long(87);
        NoticeboardSession newSession = new NoticeboardSession(newSessionId);
        
        noticeboardDAO.addNbSession(TEST_NB_ID, newSession);
        
        NoticeboardSession retrievedSession = nbSessionDAO.findNbSessionById(newSessionId);
        
        assertEquals(retrievedSession.getNbContent().getNbContentId(), TEST_NB_ID);
        
    }
}
