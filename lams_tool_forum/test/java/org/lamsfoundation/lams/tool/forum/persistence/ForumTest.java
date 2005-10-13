package org.lamsfoundation.lams.tool.forum.persistence;

import org.lamsfoundation.lams.tool.forum.core.FactoryException;
import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 7/06/2005
 * Time: 10:38:21
 * To change this template use File | Settings | File Templates.
 */
public class ForumTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateAndDeleteForum() throws FactoryException {
        //Populate a Forum entity for test purposes
        Forum entity = new Forum();
        entity.setTitle("Lams Forum");
        entity.setLockWhenFinished(false);
        entity.setRunOffline(true);
        entity.setAllowAnonym(true);
        entity.setCreatedBy(new Long("1000"));

        Set attachments = new HashSet();

        AttachmentDao attachmentDao = (AttachmentDao) GenericObjectFactoryImpl.getTestInstance().lookup(AttachmentDao.class);
        Attachment instructions = new Attachment();
        //instructions.setType(true);
        attachments.add(instructions);
        attachmentDao.saveOrUpdate(instructions);

        entity.setAttachments(attachments);

        //save
        ForumDao dao = (ForumDao) GenericObjectFactoryImpl.getTestInstance().lookup(ForumDao.class);
        dao.saveOrUpdate(entity);
        assertNotNull(entity.getUuid());
        assertNotNull("date created is null", entity.getCreated());
        assertNotNull("date updated is null", entity.getUpdated());
        assertEquals("date created and updated are different for first save", entity.getCreated(), entity.getUpdated());

        //load
        Forum reloaded = (Forum) dao.getById(entity.getUuid());
        //just because MySQL will wrap millisecond to zero. it is nonsesnce to compare data at this care.
        entity.setCreated(reloaded.created);
        entity.setUpdated(reloaded.updated);
        assertEquals("reloaded object not equal", entity, reloaded);
        assertEquals("date difference in database and memory", entity.getCreated().getTime()/1000, reloaded.getCreated().getTime()/1000);
        assertEquals("date difference in database and memory", entity.getUpdated().getTime()/1000, reloaded.getUpdated().getTime()/1000);
         assertEquals("title should be Lams Forum", "Lams Forum", reloaded.getTitle());
        assertEquals("lockWhenFinished should be false", false, reloaded.getLockWhenFinished());
        assertEquals("forceOffline should be true", true, reloaded.getRunOffline());
        assertEquals("allowAnnomity should be true", true, reloaded.getAllowAnonym());
            //validate attachment relations
        assertEquals("should have 1 attachments", 1, reloaded.getAttachments().size());
        Set reloadedSet = reloaded.getAttachments();

        assertTrue("reloaded set does not contain instructions attachment", reloadedSet.contains(instructions));
        Attachment[] child = (Attachment[]) reloadedSet.toArray(new Attachment[0]);

        //find
        List values = dao.findByNamedQuery("allForums");
        assertTrue("find all result not containing object", values.contains(entity));

        //delete
        dao.delete(reloaded);
        assertNull("object not deleted", dao.getById(entity.getUuid()) );
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
