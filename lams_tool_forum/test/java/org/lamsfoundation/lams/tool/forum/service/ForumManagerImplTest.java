package org.lamsfoundation.lams.tool.forum.service;

import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Message;

import java.util.*;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * User: conradb
 * Date: 9/06/2005
 * Time: 10:59:06
 */
public class ForumManagerImplTest extends TestCase {
    protected IForumService forumManager;

    protected void setUp() throws Exception {
        super.setUp();
        forumManager = (IForumService) GenericObjectFactoryImpl.getTestInstance().lookup("forumManager");
    }

    public void testCreateAndDeleteForum() throws PersistenceException {
        Map attachments = new HashMap();
        Attachment attachment = new Attachment();
        attachments.put("test file", attachment);

        Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), attachments, null);
        assertNotNull("Forum Id is null", forum.getUuid());
        Forum reloadedForum = forumManager.getForum(forum.getUuid());
        assertEquals(reloadedForum.getTitle(), "TEST");
        assertEquals(reloadedForum.getCreatedBy(), new Long("1002"));
        assertEquals(reloadedForum.getLockWhenFinished(), true);
        assertEquals(reloadedForum.getRunOffline(), true);
        assertEquals(reloadedForum.getAllowAnonym(), false);
        assertEquals(reloadedForum.getInstructions(), "TEST INSTRUCTIONS");
        assertEquals(reloadedForum.getOnlineInstructions(), "TEST ONLINEINSTRUCTIONS");
        assertEquals(reloadedForum.getOfflineInstructions(), "TEST OFFLINEINSTRUCTIONS");
        Set reloadedAttachments = reloadedForum.getAttachments();
        Attachment reloadedAttachment = (Attachment) (reloadedAttachments.toArray(new Attachment[0]))[0];
        assertTrue("Forum should contains attachment", reloadedAttachments.contains(attachment));

        forumManager.deleteForum(forum.getUuid());
        try {
            Forum forum2 = forumManager.getForum(forum.getUuid());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateAndDeleteForumWithNullAttachments() throws PersistenceException {
        Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), null, null);
        assertNotNull("Forum Id is null", forum.getUuid());
        Forum reloadedForum = forumManager.getForum(forum.getUuid());
        assertEquals(reloadedForum.getTitle(), "TEST");
        assertEquals(reloadedForum.getCreatedBy(), new Long("1002"));
        assertEquals(reloadedForum.getLockWhenFinished(), true);
        assertEquals(reloadedForum.getRunOffline(), true);
        assertEquals(reloadedForum.getAllowAnonym(), false);
        assertEquals(reloadedForum.getInstructions(), "TEST INSTRUCTIONS");
        assertEquals(reloadedForum.getOnlineInstructions(), "TEST ONLINEINSTRUCTIONS");
        assertEquals(reloadedForum.getOfflineInstructions(), "TEST OFFLINEINSTRUCTIONS");
        Set reloadedAttachments = reloadedForum.getAttachments();
        assertEquals("Forum should contains null attachments", 0, reloadedAttachments.size());

        forumManager.deleteForum(forum.getUuid());
        try {
            forumManager.getForum(forum.getUuid());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateAndDeleteForumWithNullInstructions() throws PersistenceException {
        Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), false, false , false, "", "", ""), null, null);
        assertNotNull("Forum Id is null", forum.getUuid());
        Forum reloadedForum = forumManager.getForum(forum.getUuid());
        assertEquals(reloadedForum.getTitle(), "TEST");
        assertEquals(reloadedForum.getCreatedBy(), new Long("1002"));
        assertEquals(reloadedForum.getLockWhenFinished(), false);
        assertEquals(reloadedForum.getRunOffline(), false);
        assertEquals(reloadedForum.getAllowAnonym(), false);
        assertEquals(reloadedForum.getInstructions(), "");
        assertEquals(reloadedForum.getOnlineInstructions(), "");
        assertEquals(reloadedForum.getOfflineInstructions(), "");
        Set reloadedAttachments = reloadedForum.getAttachments();
        assertEquals("Forum should contains null attachments", 0, reloadedAttachments.size());

        forumManager.deleteForum(forum.getUuid());
        try {
            forumManager.getForum(forum.getUuid());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateModifyAndDeleteMessage() throws PersistenceException {
      Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), new HashMap(), new HashMap());
      Message message = forumManager.createMessage(forum.getUuid(), this.getMessage(new Long("1002"), "TEST", "TEST", true, true));
      assertNotNull("Message Id is null", message.getUuid());

      Message reloaded = forumManager.getMessage(message.getUuid());

      assertEquals(reloaded.getToolSession(), forum);
      assertEquals(reloaded.getCreatedBy(), new Long("1002"));
      assertEquals(reloaded.getIsAnonymous(), true);
      assertEquals(reloaded.getIsAuthored(), true);
      assertEquals(reloaded.getSubject(), "TEST");
      assertEquals(reloaded.getBody(), "TEST");

      message.setModifiedBy(new Long("1004"));
      message.setSubject("MODIFIED SUBJECT");
      message.setBody("MODIFIED BODY");
      message.setIsAnonymous(false);

      forumManager.editMessage(message);
      message = forumManager.getMessage(message.getUuid());
      assertEquals(message.getToolSession(), forum);
      assertEquals(message.getCreatedBy(), new Long("1002"));
      assertEquals(message.getModifiedBy(), new Long("1004"));
      assertEquals(message.getIsAnonymous(), false);
      assertEquals(message.getSubject(), "MODIFIED SUBJECT");
      assertEquals(message.getBody(), "MODIFIED BODY");

      forumManager.deleteMessage(message.getUuid());

      forumManager.deleteForum(forum.getUuid());
      try {
        forumManager.getMessage(forum.getUuid());
        fail("getMessage should have barfed for non existing message");
      } catch (Exception e) {

      }
    }

    public void testReplyToMessage() throws PersistenceException {
      Forum forum = forumManager.createForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"), new HashMap(), new HashMap());
      Message message = forumManager.createMessage(forum.getUuid(), this.getMessage(new Long("1002"), "TEST", "TEST", true, true));
      assertNotNull("Message Id is null", message.getUuid());
      Message reloaded = forumManager.getMessage(message.getUuid());
      assertEquals(reloaded.getToolSession(), forum);
      assertEquals(reloaded.getCreatedBy(), new Long("1002"));
      assertEquals(reloaded.getIsAnonymous(), true);
      assertEquals(reloaded.getIsAuthored(), true);
      assertEquals(reloaded.getSubject(), "TEST");
      assertEquals(reloaded.getBody(), "TEST");

      Message reply = forumManager.replyToMessage(message.getUuid(), this.getMessage(new Long("1008"), "REPLY MESSAGE", "REPLY MESSAGE", true, false));
      assertNotNull("Message Id is null", reply.getUuid());
      reply = forumManager.getMessage(reply.getUuid());
      assertEquals(message.getToolSession(), forum);
      assertEquals(reply.getCreatedBy(), new Long("1008"));
      assertEquals(reply.getIsAnonymous(), true);
      assertEquals(reply.getIsAuthored(), false);
      assertEquals(reply.getSubject(), "REPLY MESSAGE");
      assertEquals(reply.getBody(), "REPLY MESSAGE");

      //assert that the original message has the reply in the reply Set.
      reloaded = forumManager.getMessage(message.getUuid());
      Set reloadedReplies = reloaded.getReplies();

      assertTrue("original message does not contain reply message in its reply set", reloadedReplies.contains(reply));

      forumManager.deleteMessage(message.getUuid());
      try {
        forumManager.getMessage(reply.getUuid());
        fail("getMessage should have barfed for non existing message");
      } catch (Exception e) {

      }
      Message deleted = null;
      try {
          deleted = forumManager.getMessage(message.getUuid());
      } catch (Exception e) {
        assertNull(deleted);
      }
      Message deletedReply = null;
      try {
          deleted = forumManager.getMessage(reply.getUuid());
      } catch (Exception e) {
            assertNull(deletedReply);
      }
      forumManager.deleteForum(forum.getUuid());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected Forum getForum(String title, Long createdBy, boolean lockWhenFinished,
                    boolean forceOffLine, boolean allowAnnomity, String instructions,
                    String  onlineInstructions, String offlineInstructions) {
        Forum forum = new Forum();
        forum.setTitle(title);
        forum.setCreatedBy(createdBy);
        forum.setLockWhenFinished(lockWhenFinished);
        forum.setRunOffline(forceOffLine);
        forum.setAllowAnonym(allowAnnomity);
        forum.setInstructions(instructions);
        forum.setOfflineInstructions(offlineInstructions);
        forum.setOnlineInstructions(onlineInstructions);
        return forum;
    }

    protected Message getMessage(Long createdBy, String subject, String body, boolean isAnnonymous,
                                 boolean isAuthored) {
        Message message = new Message();
        message.setSubject(subject);
        message.setBody(body);
        message.setCreatedBy(createdBy);
        message.setIsAnonymous(isAnnonymous);
        message.setIsAuthored(isAuthored);
        return message;
    }

}
