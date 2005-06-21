package org.lamsfoundation.lams.tool.forum.service;

import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Message;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 9/06/2005
 * Time: 10:59:06
 * To change this template use File | Settings | File Templates.
 */
public class ForumManagerImplTest extends TestCase {
    protected ForumManager forumManager;

    protected void setUp() throws Exception {
        super.setUp();
        forumManager = (ForumManager) GenericObjectFactoryImpl.getInstance().lookup("forumManager");
    }

    public void testCreateAndDeleteForum() throws PersistenceException {
        List attachments = new ArrayList();
        Attachment attachment = new Attachment();
        attachment.setData("test data".getBytes());
        attachment.setType(Attachment.TYPE_ONLINE);
        attachments.add(attachment);

        Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), attachments);
        assertNotNull("Forum Id is null", forum.getId());
        Forum reloadedForum = forumManager.getForum(forum.getId());
        assertEquals(reloadedForum.getTitle(), "TEST");
        assertEquals(reloadedForum.getCreatedBy(), new Long("1002"));
        assertEquals(reloadedForum.getLockWhenFinished(), true);
        assertEquals(reloadedForum.getForceOffLine(), true);
        assertEquals(reloadedForum.getAllowAnnomity(), false);
        assertEquals(reloadedForum.getInstructions(), "TEST INSTRUCTIONS");
        assertEquals(reloadedForum.getOnlineInstructions(), "TEST ONLINEINSTRUCTIONS");
        assertEquals(reloadedForum.getOfflineInstructions(), "TEST OFFLINEINSTRUCTIONS");
        Set reloadedAttachments = reloadedForum.getAttachments();
        Attachment reloadedAttachment = (Attachment) (reloadedAttachments.toArray(new Attachment[0]))[0];
        assertEquals(reloadedAttachment.getType(), attachment.getType());
        assertEquals(new String(reloadedAttachment.getData()), new String(attachment.getData()));

        assertTrue("Forum should contains attachment", reloadedAttachments.contains(attachment));

        forumManager.deleteForum(forum.getId());
        try {
            forumManager.getForum(forum.getId());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateAndDeleteForumWithNullAttachments() throws PersistenceException {
        Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), null);
        assertNotNull("Forum Id is null", forum.getId());
        Forum reloadedForum = forumManager.getForum(forum.getId());
        assertEquals(reloadedForum.getTitle(), "TEST");
        assertEquals(reloadedForum.getCreatedBy(), new Long("1002"));
        assertEquals(reloadedForum.getLockWhenFinished(), true);
        assertEquals(reloadedForum.getForceOffLine(), true);
        assertEquals(reloadedForum.getAllowAnnomity(), false);
        assertEquals(reloadedForum.getInstructions(), "TEST INSTRUCTIONS");
        assertEquals(reloadedForum.getOnlineInstructions(), "TEST ONLINEINSTRUCTIONS");
        assertEquals(reloadedForum.getOfflineInstructions(), "TEST OFFLINEINSTRUCTIONS");
        Set reloadedAttachments = reloadedForum.getAttachments();
        assertEquals("Forum should contains null attachments", 0, reloadedAttachments.size());

        forumManager.deleteForum(forum.getId());
        try {
            forumManager.getForum(forum.getId());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateAndDeleteForumWithNullInstructions() throws PersistenceException {
        Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), false, false , false, "", "", ""), null);
        assertNotNull("Forum Id is null", forum.getId());
        Forum reloadedForum = forumManager.getForum(forum.getId());
        assertEquals(reloadedForum.getTitle(), "TEST");
        assertEquals(reloadedForum.getCreatedBy(), new Long("1002"));
        assertEquals(reloadedForum.getLockWhenFinished(), false);
        assertEquals(reloadedForum.getForceOffLine(), false);
        assertEquals(reloadedForum.getAllowAnnomity(), false);
        assertEquals(reloadedForum.getInstructions(), "");
        assertEquals(reloadedForum.getOnlineInstructions(), "");
        assertEquals(reloadedForum.getOfflineInstructions(), "");
        Set reloadedAttachments = reloadedForum.getAttachments();
        assertEquals("Forum should contains null attachments", 0, reloadedAttachments.size());

        forumManager.deleteForum(forum.getId());
        try {
            forumManager.getForum(forum.getId());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateModifyAndDeleteMessage() throws PersistenceException {
      Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), new ArrayList());
      Message message = forumManager.createMessage(forum.getId(), this.getMessage(new Long("1002"), "TEST", "TEST", true, true));
      assertNotNull("Message Id is null", message.getId());

      Message reloaded = forumManager.getMessage(message.getId());

      assertEquals(reloaded.getForum(), forum);
      assertEquals(reloaded.getCreatedBy(), new Long("1002"));
      assertEquals(reloaded.getIsAnnonymous(), true);
      assertEquals(reloaded.getIsAuthored(), true);
      assertEquals(reloaded.getSubject(), "TEST");
      assertEquals(reloaded.getBody(), "TEST");

      message.setModifiedBy(new Long("1004"));
      message.setSubject("MODIFIED SUBJECT");
      message.setBody("MODIFIED BODY");
      message.setIsAnnonymous(false);

      forumManager.editMessage(message);
      message = forumManager.getMessage(message.getId());
      assertEquals(message.getForum(), forum);
      assertEquals(message.getCreatedBy(), new Long("1002"));
      assertEquals(message.getModifiedBy(), new Long("1004"));
      assertEquals(message.getIsAnnonymous(), false);
      assertEquals(message.getSubject(), "MODIFIED SUBJECT");
      assertEquals(message.getBody(), "MODIFIED BODY");

      forumManager.deleteMessage(message.getId());

      forumManager.deleteForum(forum.getId());
      try {
        forumManager.getMessage(forum.getId());
        fail("getMessage should have barfed for non existing message");
      } catch (Exception e) {

      }
    }

    public void testReplyToMessage() throws PersistenceException {
      Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), new ArrayList());
      Message message = forumManager.createMessage(forum.getId(), this.getMessage(new Long("1002"), "TEST", "TEST", true, true));
      assertNotNull("Message Id is null", message.getId());
      Message reloaded = forumManager.getMessage(message.getId());
      assertEquals(reloaded.getForum(), forum);
      assertEquals(reloaded.getCreatedBy(), new Long("1002"));
      assertEquals(reloaded.getIsAnnonymous(), true);
      assertEquals(reloaded.getIsAuthored(), true);
      assertEquals(reloaded.getSubject(), "TEST");
      assertEquals(reloaded.getBody(), "TEST");

      Message reply = forumManager.replyToMessage(message.getId(), this.getMessage(new Long("1008"), "REPLY MESSAGE", "REPLY MESSAGE", true, false));
      assertNotNull("Message Id is null", reply.getId());
      reply = forumManager.getMessage(reply.getId());
      assertEquals(message.getForum(), forum);
      assertEquals(reply.getCreatedBy(), new Long("1008"));
      assertEquals(reply.getIsAnnonymous(), true);
      assertEquals(reply.getIsAuthored(), false);
      assertEquals(reply.getSubject(), "REPLY MESSAGE");
      assertEquals(reply.getBody(), "REPLY MESSAGE");

      //assert that the original message has the reply in the reply Set.
      reloaded = forumManager.getMessage(message.getId());
      Set reloadedReplies = reloaded.getReplies();

      assertTrue("original message does not contain reply message in its reply set", reloadedReplies.contains(reply));

      forumManager.deleteMessage(message.getId());
      try {
        forumManager.getMessage(reply.getId());
        fail("getMessage should have barfed for non existing message");
      } catch (Exception e) {

      }
      Message deleted = null;
      try {
          deleted = forumManager.getMessage(message.getId());
      } catch (Exception e) {
        assertNull(deleted);
      }
      Message deletedReply = null;
      try {
          deleted = forumManager.getMessage(reply.getId());
      } catch (Exception e) {
            assertNull(deletedReply);
      }
      forumManager.deleteForum(forum.getId());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected Forum getForum(String title, Long createdBy, boolean lockWhenFinished,
             boolean forceOffLine, boolean allowAnnomity, String instructions, String  onlineInstructions, String offlineInstructions) {        Forum forum = new Forum();
        forum.setTitle(title);
        forum.setCreatedBy(createdBy);
        forum.setLockWhenFinished(lockWhenFinished);
        forum.setForceOffLine(forceOffLine);
        forum.setAllowAnnomity(allowAnnomity);
        forum.setInstructions(instructions);
        forum.setOfflineInstructions(offlineInstructions);
        forum.setOnlineInstructions(onlineInstructions);
        return forum;
    }

    protected Message getMessage(Long createdBy, String subject, String body, boolean isAnnonymous, boolean isAuthored) {
        Message message = new Message();
        message.setSubject(subject);
        message.setBody(body);
        message.setCreatedBy(createdBy);
        message.setIsAnnonymous(isAnnonymous);
        message.setIsAuthored(isAuthored);
        return message;
    }

}
