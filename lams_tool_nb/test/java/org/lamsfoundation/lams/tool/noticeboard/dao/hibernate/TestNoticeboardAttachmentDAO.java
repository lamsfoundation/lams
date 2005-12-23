/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import org.lamsfoundation.lams.tool.noticeboard.NbDataAccessTestCase;
import org.lamsfoundation.lams.tool.noticeboard.dao.hibernate.NoticeboardAttachmentDAO;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import java.util.List;

/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestNoticeboardAttachmentDAO extends NbDataAccessTestCase {

  /*  private NoticeboardAttachmentDAO attachmentDao;
    private NoticeboardContentDAO contentDao; 
    private NoticeboardAttachment nbAttachment = null;
    private NoticeboardContent nbContent = null; */
  
   
    public TestNoticeboardAttachmentDAO(String name)
    {
        super(name);
    }
    

    /**
     * @see NbDataAccessTestCase#setUp()
     */
	 protected void setUp() throws Exception {
	     super.setUp();
	     
	     initAllData();
	     initNbAttachmentData();
	 }
	 
	 /**
	  * @see NbDataAccessTestCase#tearDown()
	  */
	 protected void tearDown() throws Exception {
	    
	     cleanNbContentData(TEST_NB_ID);
	 }
	 
	 
	
	 public void testRetrieveAttachment()
	 {
	    
	    //test retrieveAttachmentByUuid
	    nbAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	    assertAttachmentData(nbAttachment);
	     
	     /* test getAttachmentsFromContent which will return a list of attachment ids, which we can use in the next method call to
	     retrieveAttachment which takes in the attachmentId as the parameter. */
	     List attachmentIds = attachmentDAO.getAttachmentIdsFromContent(noticeboardDAO.findNbContentById(TEST_NB_ID));
	     
	     //test retrieveAttachment (by attachmentId, which was retrieved from the previous method)
	     nbAttachment = attachmentDAO.retrieveAttachment((Long)attachmentIds.get(0));
	     assertAttachmentData(nbAttachment);
	     
	     //test retrieveAttachment (by filename as the parameter
	     nbAttachment = attachmentDAO.retrieveAttachmentByFilename(TEST_FILENAME);
	     assertAttachmentData(nbAttachment);
	 }
	 

	 
	 public void testSaveAttachment()
	 {
	     String newFilename = "new filename";
	     nbAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     nbAttachment.setFilename(newFilename);
	     
	     attachmentDAO.saveAttachment(nbAttachment);
	     
	     nbAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertEquals("Validating the new filename", nbAttachment.getFilename(), newFilename);
	 }
	
	 public void testRemoveAttachment()
	 {
	     nbAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     //remove any associations
	     nbContent = noticeboardDAO.findNbContentById(TEST_NB_ID);
	     nbContent.getNbAttachments().remove(nbAttachment);
	     
	     attachmentDAO.removeAttachment(nbAttachment);
	     
	     nbAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertNull(nbAttachment);
	     
	 } 
	 
	 public void testRemoveAttachmentByUuid()
	 {
	     nbAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertNotNull(nbAttachment);
	     
	     //	   remove any associations
	     nbContent = noticeboardDAO.findNbContentById(TEST_NB_ID);
	     nbContent.getNbAttachments().remove(nbAttachment);	
	     
	     attachmentDAO.removeAttachment(TEST_UUID);
	     
	     nbAttachment = attachmentDAO.retrieveAttachmentByUuid(TEST_UUID);
	     assertNull(nbAttachment);
	     
	 } 
	 
	
	
	 
	 
}
