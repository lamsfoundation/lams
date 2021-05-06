package org.lamsfoundation.lams.learning.discussion.service;

import java.util.Map;

public interface IDiscussionSentimentService {

    void startDiscussion(long lessonId, long toolContentId, Long burningQuestionUid);

    void stopDiscussion(long lessonId);

    void restartDiscussionForLearner(long lessonId, String login);

    void addDiscussionVoteForLearner(long lessonId, int userId, int selectedOption);

    Map<Integer, Long> getDiscussionAggregatedVotes(long lessonId, long toolContentId, Long burningQuestionUid);

}