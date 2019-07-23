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

    private static final String FIND_QUESTION_ACTIVITIES = "SELECT a FROM QbToolQuestion AS q, ToolActivity AS a JOIN a.learningDesign.lessons AS l "
	    + "WHERE a.toolContentId = q.toolContentId AND l IS NOT EMPTY AND l.lessonStateId IN (3,4,5,6) AND q.qbQuestion.uid = :qbQuestionUid "
	    + "ORDER BY l.organisation.name, l.lessonName";

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
	    + "JOIN lams_qb_question AS q ON cq.qb_question_id = q.question_id WHERE "
	    + "q.version = (SELECT MAX(version) FROM lams_qb_question WHERE question_id = q.question_id) "
	    + "AND cq.collection_uid = :collectionUid ?";

    private static final String FIND_COLLECTION_QUESTIONS_BY_USAGE = "SELECT q.* FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_question AS q ON cq.qb_question_id = q.question_id "
	    + "JOIN lams_qb_tool_question AS tq ON q.uid = tq.qb_question_uid "
	    + "JOIN lams_learning_activity AS a USING (tool_content_id) "
	    + "JOIN lams_lesson AS l USING (learning_design_id) WHERE l.lesson_state_id IN (3,4,5,6) "
	    + "AND q.version = (SELECT MAX(version) FROM lams_qb_question WHERE question_id = q.question_id) "
	    + "AND cq.collection_uid = :collectionUid ? GROUP BY q.question_id ORDER BY COUNT(l.lesson_id)";

    private static final String FIND_QUESTION_COLLECTIONS_BY_UID = "SELECT c.* FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_collection AS c ON cq.collection_uid = c.uid JOIN lams_qb_question AS q ON cq.qb_question_id = q.question_id "
	    + "WHERE q.uid = :qbQuestionUid";

    private static final String FIND_QUESTION_COLLECTIONS_BY_QUESTION_ID = "SELECT c.* FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_collection AS c ON cq.collection_uid = c.uid WHERE cq.qb_question_id = :qbQuestionId";

    private static final String ADD_COLLECTION_QUESTION = "INSERT INTO lams_qb_collection_question VALUES (:collectionUid, :qbQuestionId)";

    private static final String EXISTS_COLLECTION_QUESTION = "SELECT 1 FROM lams_qb_collection_question WHERE collection_uid = :collectionUid "
	    + "AND qb_question_id = :qbQuestionId";

    private static final String REMOVE_COLLECTION_QUESTION = "DELETE FROM lams_qb_collection_question WHERE collection_uid = :collectionUid "
	    + "AND qb_question_id = :qbQuestionId";

    private static final String FIND_COLLECTION_QUESTIONS_EXCLUDED = "SELECT qb_question_id FROM lams_qb_collection_question "
	    + "WHERE collection_uid = :collectionUid AND qb_question_id NOT IN :qbQuestionIds";

    private static final String FIND_QUESTIONS_BY_TOOL_CONTENT_ID = "SELECT tq.qbQuestion FROM QbToolQuestion AS tq "
	    + "WHERE tq.toolContentId = :toolContentId";

    private static final String IS_QUESTION_IN_USER_COLLECTION = "SELECT DISTINCT 1 FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_collection AS c ON cq.collection_uid = c.uid AND "
	    + "(c.user_id = :userId OR c.user_id IS NULL) AND cq.qb_question_id = :qbQuestionId";

    @Override
    public QbQuestion getQuestionByUid(Long qbQuestionUid) {
	return this.find(QbQuestion.class, qbQuestionUid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QbQuestion> getQuestionsByQuestionId(Integer questionId) {
	final String FIND_QUESTIONS_BY_QUESTION_ID = "FROM " + QbQuestion.class.getName()
		+ " WHERE questionId = :questionId ORDER BY version DESC";

	Query q = getSession().createQuery(FIND_QUESTIONS_BY_QUESTION_ID, QbQuestion.class);
	q.setParameter("questionId", questionId);
	return q.getResultList();
    }

    @Override
    public List<QbQuestion> getQuestionsByToolContentId(long toolContentId) {
	return getSession().createQuery(FIND_QUESTIONS_BY_TOOL_CONTENT_ID, QbQuestion.class)
		.setParameter("toolContentId", toolContentId).getResultList();
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
    public int getCountQuestionActivitiesByUid(long qbQuestionUid) {
	return ((Long) getSession().createQuery(FIND_QUESTION_ACTIVITIES.replace("SELECT a", "SELECT COUNT(a)"))
		.setParameter("qbQuestionUid", qbQuestionUid).getSingleResult()).intValue();
    }

    @Override
    public int getCountQuestionActivitiesByQuestionId(int qbQuestionId) {
	return ((Long) getSession()
		.createQuery(FIND_QUESTION_ACTIVITIES.replace("SELECT a", "SELECT COUNT(a)")
			.replace("uid = :qbQuestionUid", "questionId = :qbQuestionId"))
		.setParameter("qbQuestionId", qbQuestionId).getSingleResult()).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<QbQuestion> getQuestionVersions(long qbQuestionUid) {
	return this.getSession().createQuery(FIND_QUESTION_VERSIONS).setParameter("qbQuestionUid", qbQuestionUid)
		.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Long, Long> getAnswerStatsForQuestion(long qbQuestionUid) {
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
    public List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	return (List<QbQuestion>) getPagedQuestions(questionTypes, collectionUids, page, size, sortBy, sortOrder,
		searchString, false);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<BigInteger> getAllQuestionUids(String collectionUids, String sortBy, String sortOrder, String searchString) {
	return (List<BigInteger>) getPagedQuestions(null, collectionUids, 0, 100000, sortBy, sortOrder,
		searchString, true);
    }

    private List<?> getPagedQuestions(String questionTypes, String collectionUids, int page, int size, String sortBy,
	    String sortOrder, String searchString, boolean onlyUidsRequested) {
	String RETURN_VALUE = onlyUidsRequested ? "question.uid" : "question.*";
	
	//we sort of strip out HTML tags from the search by using REGEXP_REPLACE which skips all the content between < >
	final String SELECT_QUESTIONS = "SELECT DISTINCT " + RETURN_VALUE + " FROM lams_qb_question question  "
		+ " LEFT OUTER JOIN lams_qb_option qboption ON qboption.qb_question_uid = question.uid "
		+ " LEFT OUTER JOIN lams_qb_collection_question collection ON question.question_id = collection.qb_question_id "
		+ " LEFT JOIN ("//help finding questions with the max available version
		+ "	SELECT biggerQuestion.* FROM lams_qb_question biggerQuestion "
		+ " LEFT OUTER JOIN lams_qb_collection_question collection ON biggerQuestion.question_id = collection.qb_question_id "
		+ " 		LEFT OUTER JOIN lams_qb_option qboption1 "
		+ "		ON qboption1.qb_question_uid = biggerQuestion.uid WHERE "
		+ (questionTypes == null ? "" : " biggerQuestion.type in (:questionTypes) AND ")
		+ (collectionUids == null ? "" : " collection.collection_uid in (:collectionUids) AND ")
		+ "	(REGEXP_REPLACE(biggerQuestion.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " 	OR biggerQuestion.name LIKE CONCAT('%', :searchString, '%') "
		+ " 	OR REGEXP_REPLACE(qboption1.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) "
		+ ") AS biggerQuestion ON question.question_id = biggerQuestion.question_id AND question.version < biggerQuestion.version "
		+ " WHERE biggerQuestion.version is NULL " 
		+ (questionTypes == null ? "" : " AND question.type in (:questionTypes) ")
		+ (collectionUids == null ? "" : " AND collection.collection_uid in (:collectionUids) ")
		+ " AND (REGEXP_REPLACE(question.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " OR question.name LIKE CONCAT('%', :searchString, '%') "
		+ " OR REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) ";
	final String ORDER_BY_NAME = "ORDER BY question.name, question.description ";
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
	if (questionTypes != null) {
	    query.setParameterList("questionTypes", questionTypes.split(","));
	}
	if (collectionUids != null) {
	    query.setParameterList("collectionUids", collectionUids.split(","));
	}
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	if (!onlyUidsRequested) {
	    query.addEntity(QbQuestion.class);    
	}
	List<?> queryResults = query.list();

	return queryResults;
    }

    @Override
    public int getCountQuestions(String questionTypes, String collectionUids, String searchString) {
	final String SELECT_QUESTIONS = "SELECT COUNT(DISTINCT question.uid) count "
		+ " FROM lams_qb_question question  "
		+ " LEFT OUTER JOIN lams_qb_collection_question collection ON question.question_id = collection.qb_question_id "
		+ " LEFT OUTER JOIN lams_qb_option qboption "
		+ "	ON qboption.qb_question_uid = question.uid " 
		+ " LEFT JOIN ("//help finding questions with the max available version
		+ "	SELECT biggerQuestion.* FROM lams_qb_question biggerQuestion "
		+ " LEFT OUTER JOIN lams_qb_collection_question collection ON biggerQuestion.question_id = collection.qb_question_id "
		+ " 		LEFT OUTER JOIN lams_qb_option qboption1 "
		+ "		ON qboption1.qb_question_uid = biggerQuestion.uid "
		+ (questionTypes == null ? "" : " WHERE biggerQuestion.type in (:questionTypes) ")
		+ (collectionUids == null ? "" : " AND collection.collection_uid in (:collectionUids) ")
		+ "	AND (REGEXP_REPLACE(biggerQuestion.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " 	OR biggerQuestion.name LIKE CONCAT('%', :searchString, '%') "
		+ " 	OR REGEXP_REPLACE(qboption1.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) "
		+ ") AS biggerQuestion ON question.question_id = biggerQuestion.question_id AND question.version < biggerQuestion.version "
		+ " WHERE biggerQuestion.version is NULL " 
		+ (questionTypes == null ? "" : " AND question.type in (:questionTypes) ")
		+ (collectionUids == null ? "" : " AND collection.collection_uid in (:collectionUids) ")
		+ " AND (REGEXP_REPLACE(question.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')"
		+ " OR question.name LIKE CONCAT('%', :searchString, '%') "
		+ " OR REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')) ";

	NativeQuery<?> query = getSession().createNativeQuery(SELECT_QUESTIONS).addScalar("count", IntegerType.INSTANCE);
	if (questionTypes != null) {
	    query.setParameterList("questionTypes", questionTypes.split(","));
	}
	if (collectionUids != null) {
	    query.setParameterList("collectionUids", collectionUids.split(","));
	}
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
    public List<QbCollection> getQuestionCollectionsByUid(long qbQuestionUid) {
	return getSession().createNativeQuery(FIND_QUESTION_COLLECTIONS_BY_UID)
		.setParameter("qbQuestionUid", qbQuestionUid).addEntity(QbCollection.class).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QbCollection> getQuestionCollectionsByQuestionId(int qbQuestionId) {
	return getSession().createNativeQuery(FIND_QUESTION_COLLECTIONS_BY_QUESTION_ID)
		.setParameter("qbQuestionId", qbQuestionId).addEntity(QbCollection.class).list();
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
    public void addCollectionQuestion(long collectionUid, int qbQuestionId) {
	if (!questionInCollectionExists(collectionUid, qbQuestionId)) {
	    getSession().createNativeQuery(ADD_COLLECTION_QUESTION).setParameter("collectionUid", collectionUid)
		    .setParameter("qbQuestionId", qbQuestionId).executeUpdate();
	}
    }

    @Override
    public void removeCollectionQuestion(long collectionUid, int qbQuestionId) {
	getSession().createNativeQuery(REMOVE_COLLECTION_QUESTION).setParameter("collectionUid", collectionUid)
		.setParameter("qbQuestionId", qbQuestionId).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Integer> getCollectionQuestionIdsExcluded(long collectionUid, Collection<Integer> qbQuestionIds) {
	List<BigInteger> queryResult = getSession().createNativeQuery(FIND_COLLECTION_QUESTIONS_EXCLUDED)
		.setParameter("collectionUid", collectionUid).setParameterList("qbQuestionIds", qbQuestionIds)
		.getResultList();
	Set<Integer> result = new HashSet<>();
	for (BigInteger questionId : queryResult) {
	    result.add(questionId.intValue());
	}
	return result;
    }

    @Override
    public boolean isQuestionInUserCollection(int userId, int qbQuestionId) {
	return getSession().createNativeQuery(IS_QUESTION_IN_USER_COLLECTION).setParameter("userId", userId)
		.setParameter("qbQuestionId", qbQuestionId).getSingleResult() != null;
    }

    private boolean questionInCollectionExists(long collectionUid, int qbQuestionId) {
	return !getSession().createNativeQuery(EXISTS_COLLECTION_QUESTION).setParameter("collectionUid", collectionUid)
		.setParameter("qbQuestionId", qbQuestionId).getResultList().isEmpty();
    }

    private Query prepareCollectionQuestionsQuery(long collectionUid, String orderBy, String orderDirection,
	    String search, boolean isCount) {
	StringBuilder queryBuilder = new StringBuilder(FIND_COLLECTION_QUESTIONS);

	if (!isCount && StringUtils.isNotBlank(orderBy)) {
	    if (orderBy.equalsIgnoreCase("usage")) {
		queryBuilder = new StringBuilder(FIND_COLLECTION_QUESTIONS_BY_USAGE);
	    } else {
		queryBuilder.append(" ORDER BY ").append(orderBy);
	    }
	    if (StringUtils.isNotBlank(orderDirection)) {
		queryBuilder.append(" ").append(orderDirection);
	    }
	}

	String queryText = queryBuilder.toString();
	if (isCount) {
	    queryText = queryText.replace("q.*", "COUNT(*)");
	}

	queryText = queryText.replace("?",
		(StringUtils.isBlank(search) ? "" : " AND (q.name LIKE :search OR q.description LIKE :search)"));

	Query query = isCount ? getSession().createNativeQuery(queryText)
		: getSession().createNativeQuery(queryText, QbQuestion.class);
	query.setParameter("collectionUid", collectionUid);
	if (StringUtils.isNotBlank(search)) {
	    query.setParameter("search", "%" + search.trim() + "%");
	}

	return query;
    }
}