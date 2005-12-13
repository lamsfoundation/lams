package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 7/06/2005
 * Time: 15:06:55
 * To change this template use File | Settings | File Templates.
 */
public class AttachmentTest extends TestCase {
   protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateAndDeleteForum() {
        //Populate an Attachment entity for test purposes
        AttachmentDao attachmentDao = new AttachmentDao();
        Attachment instructions = new Attachment();
        attachmentDao.saveOrUpdate(instructions);

        AttachmentDao dao = new AttachmentDao();
        dao.saveOrUpdate(instructions);
        assertNotNull(instructions.getUid());

        //load

        Attachment reloaded = (Attachment) dao.getById(instructions.getUid());
        //find
        List values = dao.findByNamedQuery("allAttachments");
        assertTrue("find all result not containing object", values.contains(instructions));

        //delete
        dao.delete(reloaded);
        assertNull("object not deleted", dao.getById(instructions.getUid()) );
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
