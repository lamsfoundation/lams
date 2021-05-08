package org.lamsfoundation.lams.learning.discussion.dao.hibernate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learning.discussion.dao.IDiscussionSentimentDAO;
import org.lamsfoundation.lams.learning.discussion.model.DiscussionSentimentVote;

public class DiscussionSentimentDAO extends LAMSBaseDAO implements IDiscussionSentimentDAO {
    private static final String FIND_ACTIVE_DISCUSSION = "FROM " + DiscussionSentimentVote.class.getName()
	    + " WHERE lessonId = :lessonId AND userId IS NULL";

    private static final String FIND_ALL_LESSON_DISCUSSIONS = "SELECT toolQuestionUid FROM "
	    + DiscussionSentimentVote.class.getName() + " WHERE lessonId = :lessonId GROUP BY toolQuestionUid";

    @Override
    public DiscussionSentimentVote getActiveDiscussion(long lessonId) {
	return getSession().createQuery(FIND_ACTIVE_DISCUSSION, DiscussionSentimentVote.class)
		.setParameter("lessonId", lessonId).uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<DiscussionSentimentVote> getDiscussions(long lessonId) {
	List<Long> queryResult = getSession().createQuery(FIND_ALL_LESSON_DISCUSSIONS)
		.setParameter("lessonId", lessonId).getResultList();
	Set<DiscussionSentimentVote> result = new HashSet<>();

	for (Long toolQuestionUid : queryResult) {
	    DiscussionSentimentVote token = new DiscussionSentimentVote(lessonId, toolQuestionUid, null);
	    result.add(token);
	}
	return result;
    }

    @Override
    public Map<Integer, Long> getDiscussionAggregatedVotes(long toolQuestionUid, Long burningQuestionUid) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("toolQuestionUid", toolQuestionUid);
	if (burningQuestionUid != null) {
	    properties.put("burningQuestionUid", burningQuestionUid);
	}
	List<DiscussionSentimentVote> votes = findByProperties(DiscussionSentimentVote.class, properties);
	return votes.stream().filter(v -> v.getUserId() != null).collect(
		Collectors.groupingBy(DiscussionSentimentVote::getSelectedOption, TreeMap::new, Collectors.counting()));
    }

    @Override
    public DiscussionSentimentVote getDiscussionVote(long toolQuestionUid, Long burningQuestionUid, int userId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("toolQuestionUid", toolQuestionUid);
	if (burningQuestionUid != null) {
	    properties.put("burningQuestionUid", burningQuestionUid);
	}
	properties.put("userId", userId);
	List<DiscussionSentimentVote> votes = findByProperties(DiscussionSentimentVote.class, properties);
	return votes.isEmpty() ? null : votes.get(0);
    }
}