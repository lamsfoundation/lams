package org.lamsfoundation.lams.learning.discussion.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.lamsfoundation.lams.learning.discussion.dao.IDiscussionSentimentDAO;
import org.lamsfoundation.lams.learning.discussion.model.DiscussionSentimentVote;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DiscussionSentimentService implements IDiscussionSentimentService {
    private IDiscussionSentimentDAO discussionSentimentDAO;

    private ILearnerFullService learnerService;

    // we are caching active discussions as many students can ask for their details in short period of time
    private Map<Long, Map<Long, DiscussionSentimentVote>> cachedActiveDiscussionTokens = new ConcurrentHashMap<>();
    private static long ACTIVE_DISCUSSION_TOKEN_REFRESH_PERIOD = 5 * 1000;

    @Override
    public long startDiscussionForMonitor(long toolQuestionUid, Long burningQuestionUid) {
	long toolContentId = getToolContentId(toolQuestionUid);
	long lessonId = getLessonIdByToolContentId(toolContentId);
	// stop current discussion, if any
	stopDiscussionInternal(lessonId);

	// put a token entry in DB with no user ID, indicating which question ID is currently discussed in given lesson
	DiscussionSentimentVote activeDiscussionToken = new DiscussionSentimentVote(lessonId, toolQuestionUid,
		burningQuestionUid);
	discussionSentimentDAO.insert(activeDiscussionToken);

	// update cached token
	discussionSentimentDAO.releaseFromCache(activeDiscussionToken);
	cachedActiveDiscussionTokens.put(lessonId, Map.of(System.currentTimeMillis(), activeDiscussionToken));

	// tell all learners in the lesson that teacher has (re)started a discussion
	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("discussion", "startLearner");

	learnerService.createCommandForLessonLearners(toolContentId, jsonCommand.toString());

	return lessonId;
    }

    @Override
    public void stopDiscussionForMonitor(long toolQuestionUid) {
	long toolContentId = getToolContentId(toolQuestionUid);
	long lessonId = getLessonIdByToolContentId(toolContentId);

	// stop discussion
	DiscussionSentimentVote activeDiscussionToken = stopDiscussionInternal(lessonId);
	if (activeDiscussionToken != null) {
	    // tell all learners in the lesson that discussion has stopped
	    ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	    jsonCommand.put("discussion", "stopLearner");
	    learnerService.createCommandForLessonLearners(toolContentId, jsonCommand.toString());
	}
    }

    /**
     * Run when a learner connects to a websocket.
     * Checks if a discussion is running and the widget needs to be shown.
     */
    @Override
    public void startDiscussionForLearner(long lessonId, String login) {
	DiscussionSentimentVote cachedActiveDiscussionToken = getCachedActiveDiscussion(lessonId);
	if (cachedActiveDiscussionToken == null) {
	    return;
	}

	// tell just this learner that discussion is running
	ObjectNode jsonCommand = JsonNodeFactory.instance.objectNode();
	jsonCommand.put("discussion", "startLearner");
	learnerService.createCommandForLearner(lessonId, login, jsonCommand.toString());
    }

    @Override
    public boolean addDiscussionVoteForLearner(long lessonId, int userId, int selectedOption) {
	DiscussionSentimentVote cachedActiveDiscussionToken = getCachedActiveDiscussion(lessonId);
	if (cachedActiveDiscussionToken == null) {
	    // no discussion is running - why widget was shown at all? maybe a monitor just stopped the discussion
	    return false;
	}

	// just one vote per learner, so remove the previous vote if it exists
	DiscussionSentimentVote learnerVote = discussionSentimentDAO.getDiscussionVote(
		cachedActiveDiscussionToken.getToolQuestionUid(), cachedActiveDiscussionToken.getBurningQuestionUid(),
		userId);
	if (learnerVote != null) {
	    discussionSentimentDAO.delete(learnerVote);
	}

	// add the vote
	// it is different to discussion token as it contains user ID
	learnerVote = new DiscussionSentimentVote(lessonId, cachedActiveDiscussionToken.getToolQuestionUid(),
		cachedActiveDiscussionToken.getBurningQuestionUid());
	learnerVote.setBurningQuestionUid(cachedActiveDiscussionToken.getBurningQuestionUid());
	learnerVote.setUserId(userId);
	learnerVote.setSelectedOption(selectedOption);

	discussionSentimentDAO.insert(learnerVote);

	return true;
    }

    @Override
    public Integer getDiscussionVoteSelectedOption(long lessonId, int userId) {
	DiscussionSentimentVote cachedActiveDiscussionToken = getCachedActiveDiscussion(lessonId);
	if (cachedActiveDiscussionToken == null) {
	    // no discussion is running - why widget was shown at all? maybe a monitor just stopped the discussion
	    return null;
	}

	// just asking for selected option, not all discussion details
	DiscussionSentimentVote vote = discussionSentimentDAO.getDiscussionVote(
		cachedActiveDiscussionToken.getToolQuestionUid(), cachedActiveDiscussionToken.getBurningQuestionUid(),
		userId);
	return vote == null ? null : vote.getSelectedOption();
    }

    @Override
    public Set<DiscussionSentimentVote> getDiscussions(long toolContentId) {
	long lessonId = getLessonIdByToolContentId(toolContentId);
	return discussionSentimentDAO.getDiscussions(lessonId);
    }

    @Override
    public Map<Integer, Long> getDiscussionAggregatedVotes(long toolQuestionUid, Long burningQuestionUid) {
	return discussionSentimentDAO.getDiscussionAggregatedVotes(toolQuestionUid, burningQuestionUid);
    }

    @Override
    public DiscussionSentimentVote getActiveDiscussionByQuestion(long toolQuestionUid) {
	long toolContentId = getToolContentId(toolQuestionUid);
	long lessonId = getLessonIdByToolContentId(toolContentId);
	return getCachedActiveDiscussion(lessonId);
    }

    private DiscussionSentimentVote getCachedActiveDiscussion(long lessonId) {
	Map<Long, DiscussionSentimentVote> cachedActiveDiscussion = cachedActiveDiscussionTokens.get(lessonId);

	// if cache is fresh, return the discussion token
	if (cachedActiveDiscussion != null && System.currentTimeMillis()
		- cachedActiveDiscussion.keySet().iterator().next() < ACTIVE_DISCUSSION_TOKEN_REFRESH_PERIOD) {
	    return cachedActiveDiscussion.values().iterator().next();
	}

	// there is a token in cache, but there is no discussion in DB, so clear cache
	DiscussionSentimentVote activeDiscussionToken = discussionSentimentDAO.getActiveDiscussion(lessonId);
	if (activeDiscussionToken == null) {
	    if (cachedActiveDiscussion != null) {
		cachedActiveDiscussionTokens.remove(lessonId);
	    }
	    return null;
	}

	// cache the token
	discussionSentimentDAO.releaseFromCache(activeDiscussionToken);
	cachedActiveDiscussion = Map.of(System.currentTimeMillis(), activeDiscussionToken);
	cachedActiveDiscussionTokens.put(lessonId, cachedActiveDiscussion);

	return activeDiscussionToken;
    }

    private DiscussionSentimentVote stopDiscussionInternal(long lessonId) {
	// remove discussion token from DB
	DiscussionSentimentVote currentActiveDiscussionToken = discussionSentimentDAO.getActiveDiscussion(lessonId);
	if (currentActiveDiscussionToken != null) {
	    discussionSentimentDAO.delete(currentActiveDiscussionToken);
	}

	// remove cached discussion token
	cachedActiveDiscussionTokens.remove(lessonId);

	return currentActiveDiscussionToken;
    }

    @Override
    public long getLessonIdByQuestion(long toolQuestionUid) {
	long toolContentId = getToolContentId(toolQuestionUid);
	return getLessonIdByToolContentId(toolContentId);
    }

    private long getToolContentId(long toolQuestionUid) {
	return discussionSentimentDAO.find(QbToolQuestion.class, toolQuestionUid).getToolContentId();
    }

    private long getLessonIdByToolContentId(long toolContentId) {
	return discussionSentimentDAO.findByProperty(ToolActivity.class, "toolContentId", toolContentId).get(0)
		.getLearningDesign().getLessons().iterator().next().getLessonId();
    }

    public void setDiscussionSentimentDAO(IDiscussionSentimentDAO discussionSentimentDAO) {
	this.discussionSentimentDAO = discussionSentimentDAO;
    }

    public void setLearnerService(ILearnerFullService learnerService) {
	this.learnerService = learnerService;
    }
}