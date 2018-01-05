package org.lamsfoundation.lams.comments.dao;

import java.util.SortedSet;

import org.lamsfoundation.lams.comments.Comment;

public interface ICommentDAO {

    public abstract void saveOrUpdate(Comment comment);

    public abstract Comment getById(Long commentId);

    /**
     * Get the root (dummy / top level) topic in a Session.
     *
     * @param sessionId
     * @return
     */
    public abstract Comment getRootTopic(Long externalId, Long externalSecondaryId, Integer externalIdType, String externalSignature);

    public abstract SortedSet<Comment> getThreadByThreadId(Long threadCommentId, Integer sortBy, Integer userId);

    public abstract SortedSet<Comment> getNextThreadByThreadId(final Long rootTopicId,
	    final Long previousThreadMessageId, Integer numberOfThreads, Integer sortBy, String extraSortParam,
	    Integer userId);

    public abstract SortedSet<Comment> getStickyThreads(final Long rootTopicId, Integer sortBy, String extraSortParam,
	    Integer userId);

}