package org.lamsfoundation.lams.tool.forum.persistence;

import org.lamsfoundation.lams.tool.forum.core.FactoryException;
import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;

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

    public void testCreateAndDeleteForum() throws FactoryException {
        //Populate an Attachment entity for test purposes
        AttachmentDao attachmentDao = (AttachmentDao) GenericObjectFactoryImpl.getInstance().lookup(AttachmentDao.class);
        Attachment instructions = new Attachment();
        instructions.setName("instructions");
        instructions.setType(false);
        attachmentDao.saveOrUpdate(instructions);

        AttachmentDao dao = (AttachmentDao) GenericObjectFactoryImpl.getInstance().lookup(AttachmentDao.class);
        dao.saveOrUpdate(instructions);
        assertNotNull(instructions.getId());

        //load

        Attachment reloaded = (Attachment) dao.getById(instructions.getId());
        assertEquals("persisted and reloaded byte array should be equal", "instructions", instructions.getName());

        //find
        List values = dao.findByNamedQuery("allAttachments");
        assertTrue("find all result not containing object", values.contains(instructions));

        //delete
        dao.delete(reloaded);
        assertNull("object not deleted", dao.getById(instructions.getId()) );
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
