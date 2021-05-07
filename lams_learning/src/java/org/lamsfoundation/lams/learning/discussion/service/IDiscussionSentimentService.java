package org.lamsfoundation.lams.learning.discussion.service;

import java.util.Map;

public interface IDiscussionSentimentService {

    void startDiscussionForMonitor(long toolQuestionUid, Long burningQuestionUid);

    void stopDiscussionForMonitor(long toolQuestionUid);

    void startDiscussionForLearner(long lessonId, String login);

    boolean addDiscussionVoteForLearner(long lessonId, int userId, int selectedOption);

    Integer getDiscussionVoteSelectedOption(long lessonId, int userId);

    Map<Integer, Long> getDiscussionAggregatedVotes(long lessonId, long toolQuestionUid, Long burningQuestionUid);
}