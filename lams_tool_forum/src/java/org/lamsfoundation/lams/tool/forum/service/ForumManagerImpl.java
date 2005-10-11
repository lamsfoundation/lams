package org.lamsfoundation.lams.tool.forum.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.tool.forum.core.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.AttachmentDao;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumDao;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.MessageDao;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 8/06/2005
 * Time: 15:13:54
 * To change this template use File | Settings | File Templates.
 */
public class ForumManagerImpl implements ForumManager {
	private ForumDao forumDao;
	private AttachmentDao attachmentDao;
	private MessageDao messageDao;
	
    public Forum createForum(Forum forum, Map attachments, Map topics) throws PersistenceException {
        if (attachments != null && attachments.size() !=0) {
            Set documents = new HashSet();
            Collection attachmentList = attachments.values();
            Iterator it = attachmentList.iterator();
            while (it.hasNext()) {
                Attachment attachment = (Attachment) it.next();
                attachmentDao.saveOrUpdate(attachment);
                documents.add(attachment);
            }
            forum.setAttachments(documents);
        }
        forumDao.saveOrUpdate(forum);

        //save topics of forum
        if (topics != null && topics.size() !=0) {
          Collection topicList = topics.values();
          Iterator it = topicList.iterator();
          while (it.hasNext()) {
                Message message = (Message) it.next();
                message.setIsAuthored(true);
                this.createMessage(forum.getUuid(), message);
          }
        }
        return forum;
    }

    public Forum editForum(Forum forum, Map attachments, Map topics) throws PersistenceException {
        Forum reloaded = this.getForum(forum.getUuid());
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
                attachmentDao.saveOrUpdate(attachment);
                documents.add(attachment);
            }
            forum.setAttachments(documents);
        }
        forumDao.saveOrUpdate(reloaded);

        //save topics of forum
        if (topics != null && topics.size() !=0) {
          Collection topicList = topics.values();
          Iterator it = topicList.iterator();
          while (it.hasNext()) {
                Message message = (Message) it.next();
                this.createMessage(forum.getUuid(), message);
          }
        }

        return forum;
    }

    public Forum getForum(Long forumId) throws PersistenceException {
        return (Forum) forumDao.getById(forumId);
    }

    public void deleteForum(Long forumId) throws PersistenceException {
        Forum forum = this.getForum(forumId);
        forumDao.delete(forum);
    }

    public List getTopics(Long forumId) throws PersistenceException {
        return messageDao.findByNamedQuery("allAuthoredMessagesOfForum", forumId);
    }

    public void deleteForumAttachment(Long attachmentId) throws PersistenceException {
        Attachment attachment = (Attachment) attachmentDao.getById(attachmentId);
        attachmentDao.delete(attachment);

    }

    public Message createMessage(Long forumId, Message message) throws PersistenceException {
        message.setForum(this.getForum(forumId));
        messageDao.saveOrUpdate(message);
        return message;
    }

     public Message editMessage(Message message) throws PersistenceException {
        Message reloaded = this.getMessage(message.getUuid());
        reloaded.setModifiedBy(message.getModifiedBy());
        reloaded.setIsAnnonymous(message.getIsAnnonymous());
        reloaded.setIsAuthored(message.getIsAuthored());
        reloaded.setSubject(message.getSubject());
        reloaded.setBody(message.getBody());
        messageDao.saveOrUpdate(message);
        return message;
    }

    public Message getMessage(Long messageId) throws PersistenceException {
        return (Message) messageDao.getById(messageId);
    }

    public void deleteMessage(Long messageId) throws PersistenceException {
        Message message = this.getMessage(messageId);
        messageDao.delete(message);
     }

    public Message replyToMessage(Long messageId, Message replyMessage) throws PersistenceException {
        Message message = this.getMessage(messageId);
        replyMessage.setForum(message.getForum());
        replyMessage.setParent(message);
        messageDao.saveOrUpdate(replyMessage);
        Set replies = message.getReplies();
        if (replies == null) {
            replies = new HashSet();
        }
        replies.add(replyMessage);
        message.setReplies(replies);
        messageDao.saveOrUpdate(message);
        return replyMessage;
    }

	public AttachmentDao getAttachmentDao() {
		return attachmentDao;
	}

	public void setAttachmentDao(AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	public ForumDao getForumDao() {
		return forumDao;
	}

	public void setForumDao(ForumDao forumDao) {
		this.forumDao = forumDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
}
