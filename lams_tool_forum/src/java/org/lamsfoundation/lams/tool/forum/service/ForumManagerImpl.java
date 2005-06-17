package org.lamsfoundation.lams.tool.forum.service;

import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.core.PersistenceDelegate;
import org.lamsfoundation.lams.tool.forum.core.PersistenceException;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;

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

    public Forum createForum(Forum forum, List attachments) throws PersistenceException {
        Set documents = new HashSet();
        Iterator it = attachments.iterator();
        while (it.hasNext()) {
            Attachment attachment = (Attachment) it.next();
            persistenceDelegate.saveOrUpdate(attachment);
            documents.add(attachment);
        }
        forum.setAttachments(documents);
        persistenceDelegate.saveOrUpdate(forum);
        return forum;
    }

    public Forum editForum(Forum forum, List attachments) throws PersistenceException {
        Forum reloaded = this.getForum(forum.getId());
        reloaded.setTitle(forum.getTitle());
        reloaded.setCreatedBy(forum.getCreatedBy());
        reloaded.setLockWhenFinished(forum.getLockWhenFinished());
        reloaded.setForceOffLine(forum.getForceOffLine());
        reloaded.setAllowAnnomity(forum.getAllowAnnomity());
        reloaded.setInstructions(forum.getInstructions());
        reloaded.setOnlineInstructions(forum.getOnlineInstructions());
        reloaded.setOfflineInstructions(forum.getOfflineInstructions());
        Set documents = new HashSet();
        Iterator it = attachments.iterator();
        while (it.hasNext()) {
            Attachment attachment = (Attachment) it.next();
            persistenceDelegate.saveOrUpdate(attachment);
            documents.add(attachment);
        }
        reloaded.setAttachments(documents);
        persistenceDelegate.saveOrUpdate(reloaded);
        return forum;
    }

    public Forum getForum(Long forumId) throws PersistenceException {
        return (Forum) persistenceDelegate.getById(Forum.class, forumId);
    }

    public void deleteForum(Long forumId) throws PersistenceException {
        Forum forum = this.getForum(forumId);
        persistenceDelegate.delete(forum);
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
