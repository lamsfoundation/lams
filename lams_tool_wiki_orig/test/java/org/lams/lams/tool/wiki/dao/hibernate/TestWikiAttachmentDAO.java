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
import org.lams.lams.tool.wiki.dao.hibernate.WikiAttachmentDAO;
import org.lams.lams.tool.wiki.WikiAttachment;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiApplicationException;
import java.util.List;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestWikiAttachmentDAO extends WikiDataAccessTestCase {

  /*  private WikiAttachmentDAO attachmentDao;
    private WikiContentDAO contentDao; 
    private WikiAttachment wikiAttachment = null;
    private WikiContent wikiContent = null; */
  
   
    public TestWikiAttachmentDAO(String name)
    {
        super(name);
    }
    

    /**
     * @see WikiDataAccessTestCase#setUp()
     */
	 protected void setUp() throws Exception {
	     super.setUp();
	     
	     initAllData();
	     initWikiAttachmentData();
	 }
	 
	 /**
	  * @see WikiDataAccessTestCase#tearDown()
	  */
	 protected void tearDown() throws Exception {
	    
	     cleanWikiContentData(TEST_NB_ID);
	 }
	 
	 
	
	 public void testRetrieveAttachment()
	 {
	    
	    //test retrieveAttachmentByUuid
	    wikiAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	    assertAttachmentData(wikiAttachment);
	     
	     /* test getAttachmentsFromContent which will return a list of attachment ids, which we can use in the next method call to
	     retrieveAttachment which takes in the attachmentId as the parameter. */
	     List attachmentIds = attachmentDAO.getAttachmentIdsFromContent(wikiDAO.findWikiContentById(TEST_NB_ID));
	     
	     //test retrieveAttachment (by attachmentId, which was retrieved from the previous method)
	     wikiAttachment = attachmentDAO.retrieveAttachment((Long)attachmentIds.get(0));
	     assertAttachmentData(wikiAttachment);
	     
	     //test retrieveAttachment (by filename as the parameter
	     wikiAttachment = attachmentDAO.retrieveAttachmentByFilename(TEST_FILENAME);
	     assertAttachmentData(wikiAttachment);
	 }
	 

	 
	 public void testSaveAttachment()
	 {
	     String newFilename = "new filename";
	     wikiAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     wikiAttachment.setFilename(newFilename);
	     
	     attachmentDAO.saveAttachment(wikiAttachment);
	     
	     wikiAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertEquals("Validating the new filename", wikiAttachment.getFilename(), newFilename);
	 }
	
	 public void testRemoveAttachment()
	 {
	     wikiAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     //remove any associations
	     wikiContent = wikiDAO.findWikiContentById(TEST_NB_ID);
	     wikiContent.getWikiAttachments().remove(wikiAttachment);
	     
	     attachmentDAO.removeAttachment(wikiAttachment);
	     
	     wikiAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertNull(wikiAttachment);
	     
	 } 
	 
	 public void testRemoveAttachmentByUuid()
	 {
	     wikiAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertNotNull(wikiAttachment);
	     
	     //	   remove any associations
	     wikiContent = wikiDAO.findWikiContentById(TEST_NB_ID);
	     wikiContent.getWikiAttachments().remove(wikiAttachment);	
	     
	     attachmentDAO.removeAttachment(TEST_UUID);
	     
	     wikiAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertNull(wikiAttachment);
	     
	 } 
	 
	
	
	 
	 
}
