package org.lamsfoundation.lams.learning.discussion.dao;

import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learning.discussion.model.DiscussionSentimentVote;

public interface IDiscussionSentimentDAO extends IBaseDAO {

    DiscussionSentimentVote getActiveDiscussion(long lessonId);

    Set<DiscussionSentimentVote> getDiscussions(long lessonId);

    Map<Integer, Long> getDiscussionAggregatedVotes(long toolQuestionUid, Long burningQuestionUid);

    DiscussionSentimentVote getDiscussionVote(long toolQuestionUid, Long burningQuestionUid, int userId);
}