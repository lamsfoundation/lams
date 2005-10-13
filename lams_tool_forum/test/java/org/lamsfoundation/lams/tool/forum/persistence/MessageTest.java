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
 * Time: 12:42:05
 * To change this template use File | Settings | File Templates.
 */
public class MessageTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateAndDeleteMessage() throws FactoryException {
        //Populate a Forum entity for test purposes
        Forum forum = new Forum();

        //save
        ForumDao dao = (ForumDao) GenericObjectFactoryImpl.getTestInstance().lookup(ForumDao.class);
        dao.saveOrUpdate(forum);

        Message message = new Message();
        message.setBody("Test Message");
        message.setSubject("Test Message");
        message.setForum(forum);
        message.setIsAnnonymous(false);
        message.setIsAuthored(true);
        message.setCreatedBy(new Long(1000));
        message.setModifiedBy(new Long(1002));

        MessageDao messageDao = (MessageDao) GenericObjectFactoryImpl.getTestInstance().lookup(MessageDao.class);
        messageDao.saveOrUpdate(message);

        assertNotNull(message.getUuid());
        assertNotNull("date created is null", message.getCreated());
        assertNotNull("date updated is null", message.getUpdated());
        assertEquals("date created and updated are different for first save", message.getCreated(), message.getUpdated());

        //load
        Message reloaded = (Message) messageDao.getById(message.getUuid());
        //just because MySQL will wrap millisecond to zero. it is nonsesnce to compare data at this care.
        message.setCreated(reloaded.created);
        message.setUpdated(reloaded.updated);
        assertEquals("reloaded message not equal", message, reloaded);
        assertEquals("reloaded message body should be: Test Message", "Test Message", reloaded.getBody());
        assertEquals("reloaded message Subject should be: Test Message", "Test Message", reloaded.getSubject());
        assertEquals("reloaded message Forum not equal", forum.getUuid(), reloaded.getForum().getUuid());
        assertEquals("reloaded message isAnnonymous not equal", false, reloaded.getIsAnonymous());
        assertEquals("reloaded message isAuthored not equal", true, reloaded.getIsAuthored());
        assertEquals("reloaded message createdBy not equal", new Long(1000), reloaded.getCreatedBy());
        assertEquals("reloaded message modifiedBy not equal", new Long(1002), reloaded.getModifiedBy());

        Message message2 = new Message();
        message2.setBody("Test Message2");
        message2.setSubject("Test Message2");
        message2.setForum(forum);
        message2.setIsAnnonymous(true);
        message2.setIsAuthored(true);
        message2.setCreatedBy(new Long(1005));
        message2.setModifiedBy(new Long(1006));

        messageDao.saveOrUpdate(message2);
        Message reloaded2 = (Message) messageDao.getById(message2.getUuid());
        //just because MySQL will wrap millisecond to zero. it is nonsesnce to compare data at this care.
        message2.setCreated(reloaded2.created);
        message2.setUpdated(reloaded2.updated);

        assertEquals("reloaded message not equal", message2, reloaded2);
        assertEquals("reloaded message body should be: Test Message", "Test Message2", reloaded2.getBody());
        assertEquals("reloaded message Subject should be: Test Message", "Test Message2", reloaded2.getSubject());
        assertEquals("reloaded message Forum not equal", forum.getUuid(), reloaded2.getForum().getUuid());
        assertEquals("reloaded message isAnnonymous not equal", true, reloaded2.getIsAnonymous());
        assertEquals("reloaded message isAuthored not equal", true, reloaded2.getIsAuthored());
        assertEquals("reloaded message createdBy not equal", new Long(1005), reloaded2.getCreatedBy());
        assertEquals("reloaded message modifiedBy not equal", new Long(1006), reloaded2.getModifiedBy());

        //find
        List values = dao.findByNamedQuery("allMessages");
        assertTrue("find all result not containing object", values.contains(message));
        assertTrue("find all result not containing object", values.contains(message2));

        Message message3 = new Message();
        message3.setBody("Test Message2");
        message3.setSubject("Test Message2");
        message3.setForum(forum);
        message3.setIsAnnonymous(true);
        message3.setIsAuthored(true);
        message3.setCreatedBy(new Long(1005));
        message3.setModifiedBy(new Long(1006));
        Set replies = new HashSet();
        replies.add(message3);
        reloaded.setReplies(replies);

        messageDao.saveOrUpdate(reloaded);

        reloaded = (Message) messageDao.getById(reloaded.getUuid());
        Set reloadedReplies = reloaded.getReplies();
        assertTrue("reloaded message does not have a child", reloadedReplies.contains(message3));


        //delete
        
        messageDao.delete(reloaded);
        messageDao.deleteForumMessage(forum.getUuid());
        dao.delete(forum);
        assertNull("message object not deleted", messageDao.getById(message.getUuid()));
        assertNull("reply message object not deleted", messageDao.getById(message2.getUuid()));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
