package org.lamsfoundation.lams.comments.dao;

import java.util.List;
import java.util.SortedSet;

import org.lamsfoundation.lams.comments.Comment;

public interface ICommentDAO {

    /* Standard DAO call */
    public abstract void saveOrUpdate(Comment comment);

    public abstract void update(Comment comment);

    public abstract Comment getById(Long commentId);

    /**
     * Get the root (dummy / top level) topic in a Session.
     * 
     * @param sessionId
     * @return
     */
    public abstract Comment getRootTopic(Long externalId, Integer externalIdType, String externalSignature);

    public abstract SortedSet<Comment> getThreadByThreadId(Long threadCommentId, Integer userId);

    public abstract SortedSet<Comment> getNextThreadByThreadId(final Long rootTopicId, final Long previousThreadMessageId, Integer numberOfThreads, Integer userId);

}