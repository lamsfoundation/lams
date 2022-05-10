package org.lamsfoundation.lams.logevent.dao.hibernate;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.logevent.LearnerInteractionEvent;
import org.lamsfoundation.lams.logevent.dao.ILearnerInteractionDAO;

public class LearnerInteractionDAO extends LAMSBaseDAO implements ILearnerInteractionDAO {
    private static final String FIND_FIRST_LEARNER_INTERACTIONS = "SELECT i.* FROM lams_learner_interaction_event AS i "
	    + "JOIN lams_qb_tool_question AS q ON i.qb_tool_question_uid = q.tool_question_uid "
	    + "WHERE q.tool_content_id = :toolContentId AND i.user_id = :userId GROUP BY i.qb_tool_question_uid";

    @Override
    public Map<Long, LearnerInteractionEvent> getFirstLearnerInteractions(long toolContentId, int userId) {
	return getSession().createNativeQuery(FIND_FIRST_LEARNER_INTERACTIONS, LearnerInteractionEvent.class)
		.setParameter("toolContentId", toolContentId).setParameter("userId", userId).list().stream()
		.collect(Collectors.toMap(LearnerInteractionEvent::getQbToolQuestionUid, Function.identity()));
    }
}