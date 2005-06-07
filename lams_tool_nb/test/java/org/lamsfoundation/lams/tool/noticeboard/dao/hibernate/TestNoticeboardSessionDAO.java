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
 * Created on May 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
//import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
//import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;


/**
 * @author mtruong
 *
 */
public class TestNoticeboardSessionDAO extends NbDataAccessTestCase {
	
	private NoticeboardSessionDAO nbSessionDAO;
	private NoticeboardContentDAO nbContentDAO;
	
	private boolean cleanSessionContentData = true;
	
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
      
        /* initialise data for noticeboard content and create a session containing this data */
        this.initNbContentData();
        this.initNbSessionContent();
    }
	 
	/**
	 * @see NbDataAccessTestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
        //remove noticeboard content after each test
        if (cleanSessionContentData)
        {
        	
        	super.cleanAllData();
        	
        }
        else
        {
        	super.cleanNbContentData();
        }
    }

    public void testgetNbSessionById()
    {
       	
		//NoticeboardSession newNbSession = nbSessionDAO.getNbSessionById(TEST_SESSION_ID);
    	nbSession = nbSessionDAO.getNbSessionById(TEST_SESSION_ID);
    	
    	
		assertEquals("Validate session id ",nbSession.getNbSessionId(), TEST_SESSION_ID);
		assertEquals("Validate content id ",nbSession.getNbContent().getNbContentId(), TEST_NB_ID);
		assertEquals("Validate session start date", nbSession.getSessionStartDate(), TEST_SESSION_START_DATE);
		assertEquals("Validate session end date", nbSession.getSessionEndDate(), TEST_SESSION_END_DATE);
		assertEquals("Validate session status", nbSession.getSessionStatus(), TEST_SESSION_STATUS);
		
		
    }
 
    public void testloadNbSessionById()
    {
    	nbSession = nbSessionDAO.loadNbSessionById(TEST_SESSION_ID);
    
		assertEquals("Validate session id ",nbSession.getNbSessionId(), TEST_SESSION_ID);
		assertEquals("Validate content id ",nbSession.getNbContent().getNbContentId(), TEST_NB_ID);
		assertEquals("Validate session start date", nbSession.getSessionStartDate(), TEST_SESSION_START_DATE);
		assertEquals("Validate session end date", nbSession.getSessionEndDate(), TEST_SESSION_END_DATE);
		assertEquals("Validate session status", nbSession.getSessionStatus(), TEST_SESSION_STATUS);
		
    }
   
    public void testsaveNbSession()
    {
    	/* remove data that has been setup by setUp() */
    	this.cleanNbSessionContent();
    	
    	NoticeboardContent nb = nbContentDAO.getNbContentById(TEST_NB_ID);
    	
    	nbSession = new NoticeboardSession(TEST_SESSION_ID,
    									   nb,
										   TEST_SESSION_START_DATE,
										   TEST_SESSION_END_DATE,
										   TEST_SESSION_STATUS);
    		
    	nbSessionDAO.saveNbSession(nbSession);
    	
    	assertEquals("Validate session id ",nbSession.getNbSessionId(), TEST_SESSION_ID);
		assertEquals("Validate content id ",nbSession.getNbContent().getNbContentId(), TEST_NB_ID);
		assertEquals("Validate session start date", nbSession.getSessionStartDate(), TEST_SESSION_START_DATE);
		assertEquals("Validate session end date", nbSession.getSessionEndDate(), TEST_SESSION_END_DATE);
		assertEquals("Validate session status", nbSession.getSessionStatus(), TEST_SESSION_STATUS);
    	
    	
    }
    
 
    public void testupdateNbSession()
    {
    	String sessionStatus = "Suspended";
    	
    	nbSession = nbSessionDAO.getNbSessionById(TEST_SESSION_ID);
    	
    	nbSession.setSessionStatus(sessionStatus);
    	
    	nbSessionDAO.updateNbSession(nbSession);
    	
    	assertEquals("Validate session id ",nbSession.getNbSessionId(), TEST_SESSION_ID);
    	assertEquals("Validate new session status",nbSession.getSessionStatus(), sessionStatus);
	}
    
  
    public void testremoveNbSession()
    {
    	cleanSessionContentData = false;
    	nbSessionDAO.removeNbSession(TEST_SESSION_ID);
    	
    	NoticeboardSession session = new NoticeboardSession();
    	
    	assertNull(session.getNbSessionId());
   
    }
    
    
}
