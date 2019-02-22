package org.lamsfoundation.lams.qb.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public class QbDAO extends LAMSBaseDAO implements IQbDAO {

    private static final String FIND_MAX_QUESTION_ID = "SELECT MAX(questionId) FROM QbQuestion";
    private static final String FIND_MAX_VERSION = "SELECT MAX(version) FROM QbQuestion AS q WHERE q.questionId = :questionId";
    private static final String FIND_QUESTION_ACTIVITIES = "SELECT a FROM ToolActivity AS a, QbToolQuestion AS q "
	    + "WHERE a.toolContentId = q.toolContentId AND a.learningDesign.lessons IS NOT EMPTY AND q.qbQuestion.uid = :qbQuestionUid";
    private static final String FIND_QUESTION_VERSIONS = "SELECT q FROM QbQuestion AS q, QbQuestion AS r "
	    + "WHERE q.questionId = r.questionId AND q.uid <> r.uid AND r.uid = :qbQuestionUid";
    private static final String FIND_ANSWER_STATS = "SELECT a.qbOption.uid, COUNT(a.uid) FROM QbToolAnswer AS a "
	    + "WHERE a.qbOption.qbQuestion.uid = :qbQuestionUid GROUP BY a.qbOption.uid";

    @Override
    public int getMaxQuestionId() {
	Object result = this.getSession().createQuery(FIND_MAX_QUESTION_ID).uniqueResult();
	Integer max = (Integer) result;
	return max == null ? 1 : max + 1;
    }

    @Override
    public int getMaxQuestionVersion(Integer qbQuestionId) {
	Object result = this.getSession().createQuery(FIND_MAX_VERSION).setParameter("questionId", qbQuestionId)
		.uniqueResult();
	Integer max = (Integer) result;
	return max == null ? 1 : max + 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ToolActivity> getQuestionActivities(long qbQuestionUid) {
	return this.getSession().createQuery(FIND_QUESTION_ACTIVITIES).setParameter("qbQuestionUid", qbQuestionUid)
		.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<QbQuestion> getQuestionVersions(long qbQuestionUid) {
	return this.getSession().createQuery(FIND_QUESTION_VERSIONS).setParameter("qbQuestionUid", qbQuestionUid)
		.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Long> getAnswerStats(long qbQuestionUid) {
	List<Object[]> result = this.getSession().createQuery(FIND_ANSWER_STATS)
		.setParameter("qbQuestionUid", qbQuestionUid).list();
	Map<Long, Long> map = new HashMap<>(result.size());
	for (Object[] answerStat : result) {
	    map.put((Long) answerStat[0], (Long) answerStat[1]);
	}
	return map;
    }
}