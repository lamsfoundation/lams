package org.lamsfoundation.lams.qb.dao.hibernate;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public class QbDAO extends LAMSBaseDAO implements IQbDAO {

    private static final String FIND_MAX_QUESTION_ID = "SELECT MAX(questionId) FROM QbQuestion";
    private static final String FIND_MAX_VERSION = "SELECT MAX(version) FROM QbQuestion AS q WHERE q.questionId = :questionId";
    private static final String FIND_QUESTION_ACTIVITIES = "SELECT a FROM ToolActivity AS a, QbToolQuestion AS q "
	    + "WHERE a.toolContentId = q.toolContentId AND a.learningDesign.lessons IS NOT EMPTY AND q.qbQuestion.uid = :qbQuestionUid";
    private static final String FIND_QUESTION_VERSIONS = "SELECT q FROM QbQuestion AS q, QbQuestion AS r "
	    + "WHERE q.questionId = r.questionId AND q.uid <> r.uid AND r.uid = :qbQuestionUid";
    private static final String FIND_ANSWER_STATS_BY_QB_QUESTION = "SELECT COALESCE(a.qb_option_uid, aa.question_option_uid) AS opt, COUNT(a.answer_uid) "
	    + "FROM lams_qb_tool_question AS tq JOIN lams_qb_tool_answer AS a USING (tool_question_uid) "
	    + "LEFT JOIN tl_lascrt11_answer_log AS sa ON a.answer_uid = sa.uid "
	    + "LEFT JOIN tl_lascrt11_session AS ss ON sa.session_id = ss.session_id "
	    + "LEFT JOIN tl_lascrt11_user AS su ON ss.uid = su.session_uid "
	    + "LEFT JOIN tl_laasse10_option_answer AS aa ON a.answer_uid = aa.question_result_uid AND aa.answer_boolean = 1 "
	    + "WHERE tq.qb_question_uid = :qbQuestionUid GROUP BY opt HAVING opt IS NOT NULL";
    private static final String FIND_ANSWERS_BY_ACTIVITY = "SELECT COALESCE(mcu.que_usr_id, su.user_id, au.user_id), "
	    + "COALESCE(a.qb_option_uid, aa.question_option_uid) AS opt "
	    + "FROM lams_learning_activity AS act JOIN lams_qb_tool_question AS tq USING (tool_content_id) "
	    + "JOIN lams_qb_tool_answer AS a USING (tool_question_uid) "
	    + "LEFT JOIN tl_lamc11_usr_attempt AS mca ON a.answer_uid = mca.uid "
	    + "LEFT JOIN tl_lamc11_que_usr AS mcu ON mca.que_usr_id = mcu.uid "
	    + "LEFT JOIN tl_lascrt11_answer_log AS sa ON a.answer_uid = sa.uid "
	    + "LEFT JOIN tl_lascrt11_session AS ss ON sa.session_id = ss.session_id "
	    + "LEFT JOIN tl_lascrt11_user AS su ON ss.uid = su.session_uid "
	    + "LEFT JOIN tl_laasse10_option_answer AS aa ON a.answer_uid = aa.question_result_uid AND aa.answer_boolean = 1 "
	    + "LEFT JOIN tl_laasse10_question_result AS aq ON a.answer_uid = aq.uid "
	    + "LEFT JOIN tl_laasse10_assessment_result AS ar ON aq.result_uid = ar.uid "
	    + "LEFT JOIN tl_laasse10_user AS au ON ar.user_uid = au.uid "
	    + "WHERE act.activity_id = :activityId AND tq.qb_question_uid = :qbQuestionUid HAVING opt IS NOT NULL";
    private static final String FIND_BURNING_QUESTIONS = "SELECT b.question, COUNT(bl.uid) FROM ScratchieBurningQuestion b LEFT OUTER JOIN "
	    + "BurningQuestionLike AS bl ON bl.burningQuestion = b WHERE b.scratchieItem.qbQuestion.uid = :qbQuestionUid "
	    + "GROUP BY b.question ORDER BY COUNT(bl.uid) DESC";

    private static final String FIND_COLLECTION_QUESTIONS = "SELECT q.* FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_question AS q ON cq.qb_question_uid = q.uid WHERE cq.collection_uid = :collectionUid";

    private static final String FIND_QUESTION_COLLECTIONS = "SELECT c.* FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_collection AS c ON cq.collection_uid = c.uid WHERE cq.qb_question_uid = :qbQuestionUid";

    private static final String ADD_COLLECTION_QUESTION = "INSERT INTO lams_qb_collection_question VALUES (:collectionUid, :qbQuestionUid)";

    private static final String EXISTS_COLLECTION_QUESTION = "SELECT 1 FROM lams_qb_collection_question WHERE collection_uid = :collectionUid "
	    + "AND qb_question_uid = :qbQuestionUid";

    private static final String REMOVE_COLLECTION_QUESTION = "DELETE FROM lams_qb_collection_question WHERE collection_uid = :collectionUid "
	    + "AND qb_question_uid = :qbQuestionUid";

    private static final String FIND_COLLECTION_QUESTIONS_EXCLUDED = "SELECT qb_question_uid FROM lams_qb_collection_question "
	    + "WHERE collection_uid = :collectionUid AND qb_question_uid NOT IN :qbQuestionUids";

    @Override
    public QbQuestion getQbQuestionByUid(Long qbQuestionUid) {
	return this.find(QbQuestion.class, qbQuestionUid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QbQuestion> getQbQuestionsByQuestionId(Integer questionId) {
	final String FIND_QUESTIONS_BY_QUESTION_ID = "FROM " + QbQuestion.class.getName()
		+ " WHERE questionId = :questionId ORDER BY version ASC";

	Query q = getSession().createQuery(FIND_QUESTIONS_BY_QUESTION_ID, QbQuestion.class);
	q.setParameter("questionId", questionId);
	return q.getResultList();
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
    public int getCountQuestionActivities(long qbQuestionUid) {
	return ((Long) getSession().createQuery(FIND_QUESTION_ACTIVITIES.replace("SELECT a", "SELECT COUNT(a)"))
		.setParameter("qbQuestionUid", qbQuestionUid).getSingleResult()).intValue();
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
	List<Object[]> result = this.getSession().createSQLQuery(FIND_ANSWER_STATS_BY_QB_QUESTION)
		.setParameter("qbQuestionUid", qbQuestionUid).list();
	Map<Long, Long> map = new HashMap<>(result.size());
	for (Object[] answerStat : result) {
	    map.put(((BigInteger) answerStat[0]).longValue(), ((BigInteger) answerStat[1]).longValue());
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
		+ "	WHERE biggerQuestion.type = :questionType "
		+ "	AND (REGEXP_REPLACE(biggerQuestion.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " 	OR biggerQuestion.name LIKE CONCAT('%', :searchString, '%') "
		+ " 	OR REGEXP_REPLACE(qboption1.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) "
		+ ") AS biggerQuestion ON question.question_id = biggerQuestion.question_id AND question.version < biggerQuestion.version "
		+ " WHERE biggerQuestion.version is NULL " + " AND question.type = :questionType "
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
	final String SELECT_QUESTIONS = "SELECT COUNT(DISTINCT question.uid) count " + " FROM lams_qb_question question  "
		+ " LEFT OUTER JOIN lams_qb_option qboption " + "	ON qboption.qb_question_uid = question.uid "
		+ " LEFT JOIN ("//help finding questions with the max available version
		+ "	SELECT biggerQuestion.* FROM lams_qb_question biggerQuestion "
		+ " 		LEFT OUTER JOIN lams_qb_option qboption1 "
		+ "		ON qboption1.qb_question_uid = biggerQuestion.uid "
		+ "	WHERE biggerQuestion.type = :questionType "
		+ "	AND (REGEXP_REPLACE(biggerQuestion.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " 	OR biggerQuestion.name LIKE CONCAT('%', :searchString, '%') "
		+ " 	OR REGEXP_REPLACE(qboption1.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) "
		+ ") AS biggerQuestion ON question.question_id = biggerQuestion.question_id AND question.version < biggerQuestion.version "
		+ " WHERE biggerQuestion.version is NULL " + " AND question.type = :questionType "
		+ " AND (REGEXP_REPLACE(question.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " OR question.name LIKE CONCAT('%', :searchString, '%') "
		+ " OR REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) ";

	Query query = getSession().createNativeQuery(SELECT_QUESTIONS).addScalar("count", IntegerType.INSTANCE);;
	query.setParameter("questionType", questionType);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	int result = (int) query.getSingleResult();
	return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Integer, Long> getAnswersForActivity(long activityId, long qbQuestionUid) {
	List<Object[]> result = this.getSession().createSQLQuery(FIND_ANSWERS_BY_ACTIVITY)
		.setParameter("activityId", activityId).setParameter("qbQuestionUid", qbQuestionUid).list();
	Map<Integer, Long> map = new HashMap<>(result.size());
	for (Object[] answerStat : result) {
	    map.put(((BigInteger) answerStat[0]).intValue(), ((BigInteger) answerStat[1]).longValue());
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

    @Override
    public List<QbQuestion> getCollectionQuestions(long collectionUid) {
	return getCollectionQuestions(collectionUid, null, null, null, null, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QbCollection> getQuestionCollections(long qbQuestionUid) {
	return getSession().createNativeQuery(FIND_QUESTION_COLLECTIONS).setParameter("qbQuestionUid", qbQuestionUid)
		.addEntity(QbCollection.class).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QbQuestion> getCollectionQuestions(long collectionUid, Integer offset, Integer limit, String orderBy,
	    String orderDirection, String search) {
	Query query = prepareCollectionQuestionsQuery(collectionUid, orderBy, orderDirection, search, false);
	if (offset != null) {
	    query.setFirstResult(offset);
	}
	if (limit != null) {
	    query.setMaxResults(limit);
	}
	return query.getResultList();
    }

    @Override
    public int getCountCollectionQuestions(long collectionUid, String search) {
	Query query = prepareCollectionQuestionsQuery(collectionUid, null, null, search, true);
	return ((BigInteger) query.getSingleResult()).intValue();
    }

    @Override
    public void addCollectionQuestion(long collectionUid, long qbQuestionUid) {
	if (!questionInCollectionExists(collectionUid, qbQuestionUid)) {
	    getSession().createNativeQuery(ADD_COLLECTION_QUESTION).setParameter("collectionUid", collectionUid)
		    .setParameter("qbQuestionUid", qbQuestionUid).executeUpdate();
	}
    }

    @Override
    public void removeCollectionQuestion(long collectionUid, long qbQuestionUid) {
	getSession().createNativeQuery(REMOVE_COLLECTION_QUESTION).setParameter("collectionUid", collectionUid)
		.setParameter("qbQuestionUid", qbQuestionUid).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Long> getCollectionQuestionUidsExcluded(long collectionUid, Collection<Long> qbQuestionUids) {
	List<BigInteger> queryResult = getSession().createNativeQuery(FIND_COLLECTION_QUESTIONS_EXCLUDED)
		.setParameter("collectionUid", collectionUid).setParameterList("qbQuestionUids", qbQuestionUids)
		.getResultList();
	Set<Long> result = new HashSet<>();
	for (BigInteger uid : queryResult) {
	    result.add(uid.longValue());
	}
	return result;
    }

    private boolean questionInCollectionExists(long collectionUid, long qbQuestionUid) {
	return !getSession().createNativeQuery(EXISTS_COLLECTION_QUESTION).setParameter("collectionUid", collectionUid)
		.setParameter("qbQuestionUid", qbQuestionUid).getResultList().isEmpty();
    }

    private Query prepareCollectionQuestionsQuery(long collectionUid, String orderBy, String orderDirection,
	    String search, boolean isCount) {
	StringBuilder queryBuilder = new StringBuilder(FIND_COLLECTION_QUESTIONS);

	if (StringUtils.isNotBlank(search)) {
	    queryBuilder.append(" AND (q.name LIKE :search OR q.description LIKE :search)");
	}

	if (!isCount && StringUtils.isNotBlank(orderBy)) {
	    queryBuilder.append(" ORDER BY ").append(orderBy);
	    if (StringUtils.isNotBlank(orderDirection)) {
		queryBuilder.append(" ").append(orderDirection);
	    }
	}

	String queryText = queryBuilder.toString();
	if (isCount) {
	    queryText = queryText.replace("q.*", "COUNT(*)");
	}

	Query query = isCount ? getSession().createNativeQuery(queryText)
		: getSession().createNativeQuery(queryText, QbQuestion.class);
	query.setParameter("collectionUid", collectionUid);
	if (StringUtils.isNotBlank(search)) {
	    query.setParameter("search", "%" + search.trim() + "%");
	}

	return query;
    }
}