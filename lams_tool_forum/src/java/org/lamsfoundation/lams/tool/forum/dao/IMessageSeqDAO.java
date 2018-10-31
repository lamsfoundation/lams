package org.lamsfoundation.lams.tool.forum.dao;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.forum.model.MessageSeq;

public interface IMessageSeqDAO extends IBaseDAO {

    MessageSeq getById(Long messageSeqId);

    MessageSeq getByMessageId(Long messageId);

    List getThreadByThreadId(final Long threadMessageId);

    List getNextThreadByThreadId(final Long rootTopicId, final Long previousThreadMessageId);

    List getCompleteTopic(Long rootTopicId);

    MessageSeq getByTopicId(Long messageId);

    void save(MessageSeq msgSeq);

    void deleteByTopicId(Long topicUid);

    int getNumOfPostsByTopic(Long userID, Long topicID);

    /**
     * Get number of messages newer than specified date.
     * 
     * @param rootMessageId
     * @param userId
     * @return
     */
    int getNumOfPostsNewerThan(Long rootMessageId, Date date);

}
