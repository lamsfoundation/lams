package org.lamsfoundation.lams.qb.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.qb.dao.IQbDAO;

public class QbDAO extends LAMSBaseDAO implements IQbDAO {

    private static final String FIND_MAX_QUESTION_ID = "SELECT MAX(questionId) FROM QbQuestion";
    private static final String FIND_MAX_VERSION = "SELECT MAX(version) FROM QbQuestion AS q WHERE q.questionId = :questionId";

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
}