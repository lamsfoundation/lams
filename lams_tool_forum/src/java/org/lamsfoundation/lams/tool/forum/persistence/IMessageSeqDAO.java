package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;

public interface IMessageSeqDAO extends IBaseDAO {


	List getTopicThread(Long rootTopicId);

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
