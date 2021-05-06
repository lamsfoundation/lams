package org.lamsfoundation.lams.learning.discussion.dao;

import java.util.Map;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learning.discussion.model.DiscussionSentimentVote;

public interface IDiscussionSentimentDAO extends IBaseDAO {

    DiscussionSentimentVote getActiveDiscussion(long lessonId);

    Map<Integer, Long> getDiscussionAggregatedVotes(long lessonId, long toolContentId, Long burningQuestionUid);

    DiscussionSentimentVote getDiscussionVote(long lessonId, long toolContentId, Long burningQuestionUid, int userId);
}