package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

/**
 * User: conradb
 * Date: 7/06/2005
 * Time: 10:38:21
 */
public class ForumTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateAndDeleteForum()  {
        //Populate a Forum entity for test purposes
        Forum entity = new Forum();
        entity.setTitle("Lams Forum");
        entity.setLockWhenFinished(false);
        entity.setRunOffline(true);
        entity.setAllowAnonym(true);
        entity.setCreatedBy(new Long("1000"));

        Set attachments = new HashSet();

        AttachmentDao attachmentDao = new AttachmentDao();
        Attachment instructions = new Attachment();
        //instructions.setType(true);
        attachments.add(instructions);
        attachmentDao.saveOrUpdate(instructions);

        entity.setAttachments(attachments);

        //save
        ForumDao dao = new ForumDao();
        dao.saveOrUpdate(entity);
        assertNotNull(entity.getUid());
        assertNotNull("date created is null", entity.getCreated());
        assertNotNull("date updated is null", entity.getUpdated());
        assertEquals("date created and updated are different for first save", entity.getCreated(), entity.getUpdated());

        //load
        Forum reloaded = (Forum) dao.getById(entity.getUid());
        //just because MySQL will wrap millisecond to zero. it is nonsesnce to compare data at this care.
        entity.setCreated(reloaded.getCreated());
        entity.setUpdated(reloaded.getUpdated());
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
        assertNull("object not deleted", dao.getById(entity.getUid()) );
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
