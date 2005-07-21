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
 * Created on Jul 1, 2005
 */
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;

/**
 * @author mtruong
 */
public class TestNoticeboardUserDAO extends NbDataAccessTestCase {
    
    private NoticeboardUserDAO nbUserDAO;
    private NoticeboardSessionDAO nbSessionDAO;
    
    private NoticeboardUser nbUser;
    private NoticeboardSession nbSession;
    
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
        nbSessionDAO = (NoticeboardSessionDAO) this.context.getBean("nbSessionDAO");
        nbUserDAO = (NoticeboardUserDAO) this.context.getBean("nbUserDAO");
        super.initAllData();
     
    }
	 
	 protected void tearDown() throws Exception {
	        
	     super.tearDown();
	     if(cleanContentData)
	     {
	     	super.cleanNbContentData(TEST_NB_ID);
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

}
