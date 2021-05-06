package org.lamsfoundation.lams.learning.discussion.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lamsfoundation.lams.learning.discussion.dao.IDiscussionSentimentDAO;
import org.lamsfoundation.lams.learning.discussion.model.DiscussionSentimentVote;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

public class DiscussionSentimentService implements IDiscussionSentimentService {
    private IDiscussionSentimentDAO discussionSentimentDAO;

    private ILearnerService learnerService;

    private IUserManagementService userManagementService;

    private Map<Long, Map<Long, DiscussionSentimentVote>> cachedActiveDiscussionTokens = new ConcurrentHashMap<>();
    private static long ACTIVE_DISCUSSION_TOKEN_REFRESH_PERIOD = 5 * 1000;

    @Override
    public void startDiscussion(long lessonId, long toolContentId, Long burningQuestionUid) {
	stopDiscussionInternal(lessonId);

	DiscussionSentimentVote activeDiscussionToken = new DiscussionSentimentVote(lessonId, toolContentId);
	activeDiscussionToken.setBurningQuestionUid(burningQuestionUid);
	discussionSentimentDAO.insert(activeDiscussionToken);

	cachedActiveDiscussionTokens.put(lessonId, Map.of(System.currentTimeMillis(), activeDiscussionToken));

	learnerService.createCommandForLessonLearners(toolContentId, "discussion-start");
    }

    @Override
    public void stopDiscussion(long lessonId) {
	cachedActiveDiscussionTokens.remove(lessonId);

	DiscussionSentimentVote activeDiscussionToken = stopDiscussionInternal(lessonId);
	if (activeDiscussionToken != null) {
	    learnerService.createCommandForLessonLearners(activeDiscussionToken.getToolContentId(), "discussion-stop");
	}
    }

    @Override
    public void restartDiscussionForLearner(long lessonId, String login) {
	DiscussionSentimentVote cachedActiveDiscussionToken = getCachedActiveDiscussion(lessonId);
	if (cachedActiveDiscussionToken == null) {
	    return;
	}
	User learner = userManagementService.getUserByLogin(login);
	if (learner == null) {
	    return;
	}

	DiscussionSentimentVote learnerVote = discussionSentimentDAO.getDiscussionVote(lessonId,
		cachedActiveDiscussionToken.getToolContentId(), cachedActiveDiscussionToken.getBurningQuestionUid(),
		learner.getUserId());

	learnerService.createCommandForLearner(lessonId, login,
		"discussion-start" + (learnerVote == null ? "" : "-vote-" + learnerVote.getSelectedOption()));
    }

    @Override
    public void addDiscussionVoteForLearner(long lessonId, int userId, int selectedOption) {
	DiscussionSentimentVote cachedActiveDiscussionToken = getCachedActiveDiscussion(lessonId);
	if (cachedActiveDiscussionToken == null) {
	    return;
	}

	DiscussionSentimentVote learnerVote = discussionSentimentDAO.getDiscussionVote(lessonId,
		cachedActiveDiscussionToken.getToolContentId(), cachedActiveDiscussionToken.getBurningQuestionUid(),
		userId);
	if (learnerVote != null) {
	    discussionSentimentDAO.delete(learnerVote);
	}

	learnerVote = new DiscussionSentimentVote(lessonId, cachedActiveDiscussionToken.getToolContentId());
	learnerVote.setBurningQuestionUid(cachedActiveDiscussionToken.getBurningQuestionUid());
	learnerVote.setUserId(userId);
	learnerVote.setSelectedOption(selectedOption);

	discussionSentimentDAO.insert(learnerVote);
    }

    @Override
    public Map<Integer, Long> getDiscussionAggregatedVotes(long lessonId, long toolContentId, Long burningQuestionUid) {
	return discussionSentimentDAO.getDiscussionAggregatedVotes(lessonId, toolContentId, burningQuestionUid);
    }

    private DiscussionSentimentVote getCachedActiveDiscussion(long lessonId) {
	Map<Long, DiscussionSentimentVote> cachedActiveDiscussion = cachedActiveDiscussionTokens.get(lessonId);

	if (cachedActiveDiscussion != null && System.currentTimeMillis()
		- cachedActiveDiscussion.keySet().iterator().next() < ACTIVE_DISCUSSION_TOKEN_REFRESH_PERIOD) {
	    return cachedActiveDiscussion.values().iterator().next();
	}

	DiscussionSentimentVote activeDiscussionToken = discussionSentimentDAO.getActiveDiscussion(lessonId);
	if (activeDiscussionToken == null) {
	    if (cachedActiveDiscussion != null) {
		cachedActiveDiscussionTokens.remove(lessonId);
	    }
	    return null;
	}

	cachedActiveDiscussion = Map.of(System.currentTimeMillis(), activeDiscussionToken);
	cachedActiveDiscussionTokens.put(lessonId, cachedActiveDiscussion);

	return activeDiscussionToken;
    }

    private DiscussionSentimentVote stopDiscussionInternal(long lessonId) {
	DiscussionSentimentVote currentActiveDiscussionToken = discussionSentimentDAO.getActiveDiscussion(lessonId);
	if (currentActiveDiscussionToken != null) {
	    discussionSentimentDAO.delete(currentActiveDiscussionToken);
	}
	return currentActiveDiscussionToken;
    }
}