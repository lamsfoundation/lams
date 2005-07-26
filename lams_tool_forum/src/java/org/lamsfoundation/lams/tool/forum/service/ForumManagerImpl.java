package org.lamsfoundation.lams.tool.forum.service;

import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.core.PersistenceDelegate;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 8/06/2005
 * Time: 15:13:54
 * To change this template use File | Settings | File Templates.
 */
public class ForumManagerImpl implements ForumManager {

    protected PersistenceDelegate persistenceDelegate;

    public void setPersistenceDelegate(PersistenceDelegate persistenceDelegate) {
        this.persistenceDelegate = persistenceDelegate;
    }

    public Forum createForum(Forum forum, Map attachments, Map topics) throws PersistenceException {
        if (attachments != null && attachments.size() !=0) {
            Set documents = new HashSet();
            Collection attachmentList = attachments.values();
            Iterator it = attachmentList.iterator();
            while (it.hasNext()) {
                Attachment attachment = (Attachment) it.next();
                persistenceDelegate.saveOrUpdate(attachment);
                documents.add(attachment);
            }
            forum.setAttachments(documents);
        }
        persistenceDelegate.saveOrUpdate(forum);

        //save topics of forum
        if (topics != null && topics.size() !=0) {
          Collection topicList = topics.values();
          Iterator it = topicList.iterator();
          while (it.hasNext()) {
                Message message = (Message) it.next();
                message.setIsAuthored(true);
                this.createMessage(forum.getId(), message);
          }
        }
        return forum;
    }

    public Forum editForum(Forum forum, Map attachments, Map topics) throws PersistenceException {
        Forum reloaded = this.getForum(forum.getId());
        reloaded.setTitle(forum.getTitle());
        reloaded.setCreatedBy(forum.getCreatedBy());
        reloaded.setLockWhenFinished(forum.getLockWhenFinished());
        reloaded.setForceOffline(forum.getForceOffline());
        reloaded.setAllowAnnomity(forum.getAllowAnnomity());
        reloaded.setInstructions(forum.getInstructions());
        reloaded.setOnlineInstructions(forum.getOnlineInstructions());
        reloaded.setOfflineInstructions(forum.getOfflineInstructions());
        if (attachments != null && attachments.size() !=0) {
            Set documents = reloaded.getAttachments();
            Collection attachmentList = attachments.values();
            Iterator it = attachmentList.iterator();
            while (it.hasNext()) {
                Attachment attachment = (Attachment) it.next();
                persistenceDelegate.saveOrUpdate(attachment);
                documents.add(attachment);
            }
            forum.setAttachments(documents);
        }
        persistenceDelegate.saveOrUpdate(reloaded);

        //save topics of forum
        if (topics != null && topics.size() !=0) {
          Collection topicList = topics.values();
          Iterator it = topicList.iterator();
          while (it.hasNext()) {
                Message message = (Message) it.next();
                this.createMessage(forum.getId(), message);
          }
        }

        return forum;
    }

    public Forum getForum(Long forumId) throws PersistenceException {
        return (Forum) persistenceDelegate.getById(Forum.class, forumId);
    }

    public void deleteForum(Long forumId) throws PersistenceException {
        Forum forum = this.getForum(forumId);
        persistenceDelegate.delete(forum);
    }

    public List getTopics(Long forumId) throws PersistenceException {
        return persistenceDelegate.findByNamedQuery(Message.class, "allAuthoredMessagesOfForum", forumId);
    }

    public void deleteForumAttachment(Long attachmentId) throws PersistenceException {
        Attachment attachment = (Attachment) this.persistenceDelegate.getById(Attachment.class, attachmentId);
        persistenceDelegate.delete(attachment);

    }

    public Message createMessage(Long forumId, Message message) throws PersistenceException {
        message.setForum(this.getForum(forumId));
        persistenceDelegate.saveOrUpdate(message);
        return message;
    }

     public Message editMessage(Message message) throws PersistenceException {
        Message reloaded = this.getMessage(message.getId());
        reloaded.setModifiedBy(message.getModifiedBy());
        reloaded.setIsAnnonymous(message.getIsAnnonymous());
        reloaded.setIsAuthored(message.getIsAuthored());
        reloaded.setSubject(message.getSubject());
        reloaded.setBody(message.getBody());
        persistenceDelegate.saveOrUpdate(message);
        return message;
    }

    public Message getMessage(Long messageId) throws PersistenceException {
        return (Message) persistenceDelegate.getById(Message.class, messageId);
    }

    public void deleteMessage(Long messageId) throws PersistenceException {
        Message message = this.getMessage(messageId);
        persistenceDelegate.delete(message);
     }

    public Message replyToMessage(Long messageId, Message replyMessage) throws PersistenceException {
        Message message = this.getMessage(messageId);
        replyMessage.setForum(message.getForum());
        replyMessage.setParent(message);
        persistenceDelegate.saveOrUpdate(replyMessage);
        Set replies = message.getReplies();
        if (replies == null) {
            replies = new HashSet();
        }
        replies.add(replyMessage);
        message.setReplies(replies);
        persistenceDelegate.saveOrUpdate(message);
        return replyMessage;
    }
}
