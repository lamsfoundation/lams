package org.lamsfoundation.lams.tool.forum.service;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.lamsfoundation.lams.tool.forum.core.GenericObjectFactoryImpl;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;

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
        Set attachments = new HashSet();
        Attachment attachment = new Attachment();
        attachments.add(attachment);
        Forum inForum = this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS");
        inForum.setAttachments(attachments);
        
        Forum forum = forumManager.editForum(inForum);
        assertNotNull("Forum Id is null", forum.getUid());
        Forum reloadedForum = forumManager.getForum(forum.getUid());
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

        forumManager.deleteForum(forum.getUid());
        try {
            Forum forum2 = forumManager.getForum(forum.getUid());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateAndDeleteForumWithNullAttachments() throws PersistenceException {
        Forum forum = forumManager.editForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"));
        assertNotNull("Forum Id is null", forum.getUid());
        Forum reloadedForum = forumManager.getForum(forum.getUid());
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

        forumManager.deleteForum(forum.getUid());
        try {
            forumManager.getForum(forum.getUid());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateAndDeleteForumWithNullInstructions() throws PersistenceException {
        Forum forum = forumManager.editForum(this.getForum("TEST", new Long("1002"), false, false , false, "", "", ""));
        assertNotNull("Forum Id is null", forum.getUid());
        Forum reloadedForum = forumManager.getForum(forum.getUid());
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

        forumManager.deleteForum(forum.getUid());
        try {
            forumManager.getForum(forum.getUid());
            fail("getForum should have barfed for non existing forum");
        } catch (Exception e) {

        }
    }

    public void testCreateModifyAndDeleteMessage() throws PersistenceException {
      Forum forum = forumManager.editForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"));
      Message message = forumManager.createMessage(forum.getUid(), this.getMessage(new Long("1002"), "TEST", "TEST", true, true));
      assertNotNull("Message Id is null", message.getUid());

      Message reloaded = forumManager.getMessage(message.getUid());

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

      forumManager.updateTopic(message);
      message = forumManager.getMessage(message.getUid());
      assertEquals(message.getToolSession(), forum);
      assertEquals(message.getCreatedBy(), new Long("1002"));
      assertEquals(message.getModifiedBy(), new Long("1004"));
      assertEquals(message.getIsAnonymous(), false);
      assertEquals(message.getSubject(), "MODIFIED SUBJECT");
      assertEquals(message.getBody(), "MODIFIED BODY");

      forumManager.deleteTopic(message.getUid());

      forumManager.deleteForum(forum.getUid());
      try {
        forumManager.getMessage(forum.getUid());
        fail("getMessage should have barfed for non existing message");
      } catch (Exception e) {

      }
    }

    public void testReplyToMessage() throws PersistenceException {
      Forum forum = forumManager.editForum(this.getForum("TEST", new Long("1002"), true, true, false, "TEST INSTRUCTIONS", "TEST ONLINEINSTRUCTIONS", "TEST OFFLINEINSTRUCTIONS"));
      Message message = forumManager.createMessage(forum.getUid(), this.getMessage(new Long("1002"), "TEST", "TEST", true, true));
      assertNotNull("Message Id is null", message.getUid());
      Message reloaded = forumManager.getMessage(message.getUid());
      assertEquals(reloaded.getToolSession(), forum);
      assertEquals(reloaded.getCreatedBy(), new Long("1002"));
      assertEquals(reloaded.getIsAnonymous(), true);
      assertEquals(reloaded.getIsAuthored(), true);
      assertEquals(reloaded.getSubject(), "TEST");
      assertEquals(reloaded.getBody(), "TEST");

      Message reply = forumManager.replyTopic(message.getUid(), this.getMessage(new Long("1008"), "REPLY MESSAGE", "REPLY MESSAGE", true, false));
      assertNotNull("Message Id is null", reply.getUid());
      reply = forumManager.getMessage(reply.getUid());
      assertEquals(message.getToolSession(), forum);
      assertEquals(reply.getCreatedBy(), new Long("1008"));
      assertEquals(reply.getIsAnonymous(), true);
      assertEquals(reply.getIsAuthored(), false);
      assertEquals(reply.getSubject(), "REPLY MESSAGE");
      assertEquals(reply.getBody(), "REPLY MESSAGE");

      //assert that the original message has the reply in the reply Set.
      reloaded = forumManager.getMessage(message.getUid());
      Set reloadedReplies = reloaded.getReplies();

      assertTrue("original message does not contain reply message in its reply set", reloadedReplies.contains(reply));

      forumManager.deleteTopic(message.getUid());
      try {
        forumManager.getMessage(reply.getUid());
        fail("getMessage should have barfed for non existing message");
      } catch (Exception e) {

      }
      Message deleted = null;
      try {
          deleted = forumManager.getMessage(message.getUid());
      } catch (Exception e) {
        assertNull(deleted);
      }
      Message deletedReply = null;
      try {
          deleted = forumManager.getMessage(reply.getUid());
      } catch (Exception e) {
            assertNull(deletedReply);
      }
      forumManager.deleteForum(forum.getUid());
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
