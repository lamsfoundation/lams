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

package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.Date;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;



/**
 * @author mtruong
 *
 * JUnit Test Cases to test the NoticeboardSessionDAO class
 */
public class TestNoticeboardSessionDAO extends NbDataAccessTestCase {
	
	private NoticeboardSessionDAO nbSessionDAO;
	private NoticeboardContentDAO nbContentDAO;
	private NoticeboardUserDAO nbUserDAO;
	private NoticeboardSession nbSession = null;
	private NoticeboardContent nbContent = null;
	
	
	//private boolean cleanSessionContentData = false;
	private boolean cleanContentData = true;
	
	public TestNoticeboardSessionDAO(String name)
	{
		super(name);
	}
	
	 /**
     * @see NbDataAccessTestCase#setUp()
     */
	 protected void setUp() throws Exception {
	 	super.setUp();
        
        nbContentDAO = (NoticeboardContentDAO) this.context.getBean("nbContentDAO");
        nbSessionDAO = (NoticeboardSessionDAO) this.context.getBean("nbSessionDAO");
        nbUserDAO = (NoticeboardUserDAO) this.context.getBean("nbUserDAO");
        super.initAllData();
    }
	 
	/**
	 * @see NbDataAccessTestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
        if(cleanContentData)
        {
        	super.cleanNbContentData(TEST_NB_ID);
        }
       
    }

   public void testgetNbSessionByUID()
    {
       nbSession = nbSessionDAO.getNbSessionByUID(new Long(1)); //default test data which is always in db
        
       assertEquals(nbSession.getNbSessionId(), DEFAULT_SESSION_ID);
       assertEquals(nbSession.getSessionStatus(), DEFAULT_SESSION_STATUS);
        
    }
    
    public void testfindNbSessionById()
    {
        nbSession = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        assertEqualsForSessionContent(nbSession);
        
        Long nonExistentSessionId = new Long(7657);
        assertSessionObjectIsNull(nonExistentSessionId); 
    }
    
    public void testsaveNbSession()
    {
        NoticeboardContent nbContentToReference = nbContentDAO.findNbContentById(TEST_NB_ID);
        
        Long newSessionId = new Long(2222);
        Date newDateCreated = new Date(System.currentTimeMillis());
        NoticeboardSession newSessionObject = new NoticeboardSession(newSessionId,
                													nbContentToReference,
                													newDateCreated,
                													NoticeboardSession.NOT_ATTEMPTED);
        
        nbContentToReference.getNbSessions().add(newSessionObject);
        nbContentDAO.updateNbContent(nbContentToReference);
        
        nbSessionDAO.saveNbSession(newSessionObject);
        
        //Retrieve the newly added session object and test its values
        
        nbSession = nbSessionDAO.findNbSessionById(newSessionId);
        
        assertEquals(nbSession.getNbSessionId(), newSessionId);
        assertEquals(nbSession.getSessionStartDate(), newDateCreated);
        
    } 
    
    public void testupdateNbSession()
    {
        nbSession = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        
        nbSession.setSessionStatus(NoticeboardSession.COMPLETED);
        
        nbSessionDAO.updateNbSession(nbSession);
        
        NoticeboardSession updatedSession = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        
        assertEquals(updatedSession.getSessionStatus(), NoticeboardSession.COMPLETED);
    } 
    
    public void testremoveNbSessionByUID()
    {
        NoticeboardSession existingSession = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        Long uid = existingSession.getUid();
        
        NoticeboardContent referencedContent = existingSession.getNbContent();
        
        nbSessionDAO.removeNbSessionByUID(uid);
        referencedContent.getNbSessions().remove(existingSession);
        
        nbContentDAO.updateNbContent(referencedContent);
        
        assertSessionObjectIsNull(TEST_SESSION_ID);
    }
    
    
    public void testremoveNbSessionById()
    {
        nbSession = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        nbContent = nbSession.getNbContent();
        nbContent.getNbSessions().remove(nbSession);
        
        nbSessionDAO.removeNbSession(TEST_SESSION_ID);
        
        nbContentDAO.updateNbContent(nbContent);
        
        assertSessionObjectIsNull(TEST_SESSION_ID);
        
    }
    
    public void testremoveNbSession()
    {
        nbSession = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        nbContent = nbSession.getNbContent();
        nbContent.getNbSessions().remove(nbSession);
        
        nbSessionDAO.removeNbSession(nbSession);
        
        nbContentDAO.updateNbContent(nbContent);
        
        assertSessionObjectIsNull(TEST_SESSION_ID);
    } 
    
    public void testGetNbSessionByUser()
    {
        nbSession = nbSessionDAO.getNbSessionByUser(TEST_USER_ID);
        assertEqualsForSessionContent(nbSession);
    }
    
    public void testRemoveNbUsers()
    {
        nbSession = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        
        nbSessionDAO.removeNbUsers(nbSession);
        nbSession.getNbUsers().clear();
        nbSessionDAO.updateNbSession(nbSession);
        
        NoticeboardSession ns = nbSessionDAO.findNbSessionById(TEST_SESSION_ID);
        
        assertNotNull(ns);
        assertUserObjectIsNull(TEST_USER_ID);
    }
    
    public void testAddUsers()
    {
        Long newUserId = new Long(123);
        NoticeboardUser newUser = new NoticeboardUser(newUserId);
        
        nbSessionDAO.addNbUsers(TEST_SESSION_ID, newUser);
        
        NoticeboardUser retrievedUser = nbUserDAO.getNbUserByID(newUserId);
        
        assertEquals(retrievedUser.getNbSession().getNbSessionId(), TEST_SESSION_ID);
    }
   
}
