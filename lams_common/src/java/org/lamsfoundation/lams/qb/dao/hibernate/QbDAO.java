package org.lamsfoundation.lams.qb.dao.hibernate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
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
    private static final String FIND_ANSWER_STATS_BY_QB_QUESTION = "SELECT a.qbOption.uid, COUNT(a.uid) FROM QbToolAnswer AS a "
	    + "WHERE a.qbOption.qbQuestion.uid = :qbQuestionUid GROUP BY a.qbOption.uid";
    private static final String FIND_ANSWER_STATS_BY_TOOL_QUESTION = "SELECT a.qbOption.uid, COUNT(a.uid) FROM QbToolAnswer AS a "
	    + "WHERE a.qbToolQuestion.uid = :qbToolQuestionUid GROUP BY a.qbOption.uid";
    private static final String FIND_ANSWER_STATS_BY_ACTIVITY = "SELECT a.qbOption.uid, COUNT(a.uid) FROM QbToolAnswer AS a, "
	    + " ToolActivity AS act WHERE a.qbToolQuestion.toolContentId = act.toolContentId AND act.activityId = :activityId GROUP BY a.qbOption.uid";
    private static final String FIND_BURNING_QUESTIONS = "SELECT b.question, COUNT(bl.uid) FROM ScratchieBurningQuestion b LEFT OUTER JOIN "
	    + "BurningQuestionLike AS bl ON bl.burningQuestion = b WHERE b.scratchieItem.qbQuestion.uid = :qbQuestionUid "
	    + "GROUP BY b.question ORDER BY COUNT(bl.uid) DESC";

    @Override
    public QbQuestion getQbQuestionByUid(Long qbQuestionUid) {
	return (QbQuestion) this.find(QbQuestion.class, qbQuestionUid);
    }

    @Override
    public List<QbQuestion> getQbQuestionsByQuestionId(Integer questionId) {
	final String FIND_QUESTIONS_BY_QUESTION_ID = "FROM " + QbQuestion.class.getName()
		+ " WHERE questionId = :questionId AND local = 0 ORDER BY version ASC";

	Query<QbQuestion> q = getSession().createQuery(FIND_QUESTIONS_BY_QUESTION_ID, QbQuestion.class);
	q.setParameter("questionId", questionId);
	return q.list();
    }

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
    public Map<Long, Long> getAnswerStatsForQbQuestion(long qbQuestionUid) {
	List<Object[]> result = this.getSession().createQuery(FIND_ANSWER_STATS_BY_QB_QUESTION)
		.setParameter("qbQuestionUid", qbQuestionUid).list();
	Map<Long, Long> map = new HashMap<>(result.size());
	for (Object[] answerStat : result) {
	    map.put((Long) answerStat[0], (Long) answerStat[1]);
	}
	return map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QbQuestion> getPagedQbQuestions(Integer questionType, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	//we sort of strip out HTML tags from the search by using REGEXP_REPLACE which skips all the content between < >
	final String SELECT_QUESTIONS = "SELECT DISTINCT question.* " + " FROM lams_qb_question question  "
		+ " LEFT OUTER JOIN lams_qb_option qboption " + "	ON qboption.qb_question_uid = question.uid "
		+ " LEFT JOIN ("//help finding questions with the max available version
		+ "	SELECT biggerQuestion.* FROM lams_qb_question biggerQuestion "
		+ " 		LEFT OUTER JOIN lams_qb_option qboption1 "
		+ "		ON qboption1.qb_question_uid = biggerQuestion.uid "
		+ "	WHERE biggerQuestion.type = :questionType " + " 	AND biggerQuestion.local = 0 "
		+ "	AND (REGEXP_REPLACE(biggerQuestion.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " 	OR biggerQuestion.name LIKE CONCAT('%', :searchString, '%') "
		+ " 	OR REGEXP_REPLACE(qboption1.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) "
		+ ") AS biggerQuestion ON question.question_id = biggerQuestion.question_id AND question.version < biggerQuestion.version "
		+ " WHERE biggerQuestion.version is NULL " + " AND question.type = :questionType "
		+ " AND question.local = 0 "
		+ " AND (REGEXP_REPLACE(question.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " OR question.name LIKE CONCAT('%', :searchString, '%') "
		+ " OR REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) ";
	final String ORDER_BY_NAME = "ORDER BY question.name ";
	final String ORDER_BY_SMTH_ELSE = "ORDER BY question.question_id ";

	//TODO check the following query with real data. and see maybe it's better than the current (it's unlikely though) [https://stackoverflow.com/a/28090544/10331386 and https://stackoverflow.com/a/612268/10331386]
//	SELECT t1.*
//	FROM lams_qb_question t1
//	INNER JOIN
//	(
//	    SELECT `question_id`, MAX(version) AS max_version
//	    FROM lams_qb_question as t3
//			LEFT OUTER JOIN lams_qb_option qboption
//				ON qboption.qb_question_uid = t3.uid
//		WHERE REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')
//	    GROUP BY `question_id`
//	) t2
//	    ON t1.`question_id` = t2.`question_id` AND t1.version = t2.max_version;

	StringBuilder bldr = new StringBuilder(SELECT_QUESTIONS);
	if ("smth_else".equalsIgnoreCase(sortBy)) {
	    bldr.append(ORDER_BY_SMTH_ELSE);
	} else {
	    bldr.append(ORDER_BY_NAME);
	}
	bldr.append(sortOrder);

	NativeQuery<?> query = getSession().createNativeQuery(bldr.toString());
	query.setParameter("questionType", questionType);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	query.addEntity(QbQuestion.class);
	List<QbQuestion> queryResults = (List<QbQuestion>) query.list();

	return queryResults;
    }

    @Override
    public int getCountQbQuestions(Integer questionType, String searchString) {
	final String SELECT_QUESTIONS = "SELECT COUNT(DISTINCT question.uid) " + " FROM lams_qb_question question  "
		+ " LEFT OUTER JOIN lams_qb_option qboption " + "	ON qboption.qb_question_uid = question.uid "
		+ " LEFT JOIN ("//help finding questions with the max available version
		+ "	SELECT biggerQuestion.* FROM lams_qb_question biggerQuestion "
		+ " 		LEFT OUTER JOIN lams_qb_option qboption1 "
		+ "		ON qboption1.qb_question_uid = biggerQuestion.uid "
		+ "	WHERE biggerQuestion.type = :questionType " + " 	AND biggerQuestion.local = 0 "
		+ "	AND (REGEXP_REPLACE(biggerQuestion.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " 	OR biggerQuestion.name LIKE CONCAT('%', :searchString, '%') "
		+ " 	OR REGEXP_REPLACE(qboption1.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) "
		+ ") AS biggerQuestion ON question.question_id = biggerQuestion.question_id AND question.version < biggerQuestion.version "
		+ " WHERE biggerQuestion.version is NULL " + " AND question.type = :questionType "
		+ " AND question.local = 0 "
		+ " AND (REGEXP_REPLACE(question.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " OR question.name LIKE CONCAT('%', :searchString, '%') "
		+ " OR REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) ";

	Query<?> query = getSession().createNativeQuery(SELECT_QUESTIONS);
	query.setParameter("questionType", questionType);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	int result = ((Number) query.uniqueResult()).intValue();
	return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Long> geAnswerStatsForQbToolQuestion(long qbToolQuestionUid) {
	List<Object[]> result = this.getSession().createQuery(FIND_ANSWER_STATS_BY_TOOL_QUESTION)
		.setParameter("qbToolQuestionUid", qbToolQuestionUid).list();
	Map<Long, Long> map = new HashMap<>(result.size());
	for (Object[] answerStat : result) {
	    map.put((Long) answerStat[0], (Long) answerStat[1]);
	}
	return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Long> getAnswerStatsForActivity(long activityId) {
	List<Object[]> result = this.getSession().createQuery(FIND_ANSWER_STATS_BY_ACTIVITY)
		.setParameter("activityId", activityId).list();
	Map<Long, Long> map = new HashMap<>(result.size());
	for (Object[] answerStat : result) {
	    map.put((Long) answerStat[0], (Long) answerStat[1]);
	}
	return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Long> getBurningQuestions(long qbQuestionUid) {
	List<Object[]> result = this.getSession().createQuery(FIND_BURNING_QUESTIONS)
		.setParameter("qbQuestionUid", qbQuestionUid).list();
	Map<String, Long> map = new LinkedHashMap<>(result.size());
	for (Object[] burningQuestion : result) {
	    map.put((String) burningQuestion[0], (Long) burningQuestion[1]);
	}
	return map;
    }
}