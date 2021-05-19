package org.lamsfoundation.lams.learning.discussion.service;

import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.learning.discussion.model.DiscussionSentimentVote;

public interface IDiscussionSentimentService {

    long startDiscussionForMonitor(long toolQuestionUid, Long burningQuestionUid);

    void stopDiscussionForMonitor(long toolQuestionUid);

    void startDiscussionForLearner(long lessonId, String login);

    boolean addDiscussionVoteForLearner(long lessonId, int userId, int selectedOption);

    Integer getDiscussionVoteSelectedOption(long lessonId, int userId);

    Map<Integer, Long> getDiscussionAggregatedVotes(long toolQuestionUid, Long burningQuestionUid);

    DiscussionSentimentVote getActiveDiscussionByQuestion(long toolQuestionUid);

    Set<DiscussionSentimentVote> getDiscussions(long toolContentId);

    long getLessonIdByQuestion(long toolQuestionUid);
}