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
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.tool.ToolContent;

public class QbDAO extends LAMSBaseDAO implements IQbDAO {

    private static final String FIND_MAX_QUESTION_ID = "SELECT IFNULL(MAX(question_id), 0) FROM lams_qb_question";

    private static final String FIND_MAX_QUESTION_ID_FROM_GENERATOR = "SELECT IFNULL(MAX(lams_qb_question_question_id), 0) FROM lams_sequence_generator";

    private static final String FIND_MAX_VERSION = "SELECT MAX(version) FROM QbQuestion AS q WHERE q.questionId = :questionId";

    private static final String FIND_QUESTION_ACTIVITIES = "SELECT a FROM QbToolQuestion AS q, ToolActivity AS a JOIN a.learningDesign.lessons AS l "
	    + "WHERE a.toolContentId = q.toolContentId AND l IS NOT EMPTY AND l.lessonStateId IN (3,4,5,6) AND q.qbQuestion.uid = :qbQuestionUid "
	    + "ORDER BY l.organisation.name, l.lessonName";

    private static final String FIND_QUESTION_ACTIVITIES_FILTERED_BY_TOOL_CONTENT_ID = "SELECT c FROM QbToolQuestion AS q, ToolContent AS c "
	    + "WHERE c.toolContentId = q.toolContentId AND c.tool.toolSignature IN ('laasse10', 'lascrt11') AND "
	    + "q.qbQuestion.uid = :qbQuestionUid AND q.toolContentId IN (:toolContentIds)";

    private static final String FIND_QUESTION_VERSIONS = "SELECT q FROM QbQuestion AS q, QbQuestion AS r "
	    + "WHERE q.questionId = r.questionId AND q.uid <> r.uid AND r.uid = :qbQuestionUid";

    private static final String FIND_ANSWER_STATS_BY_QB_QUESTION = "SELECT COALESCE(a.qb_option_uid, aa.question_option_uid) AS opt, COUNT(a.answer_uid) "
	    + "FROM lams_qb_tool_question AS tq JOIN lams_qb_tool_answer AS a USING (tool_question_uid) "
	    + "LEFT JOIN tl_lascrt11_answer_log AS sa ON a.answer_uid = sa.uid "
	    + "LEFT JOIN tl_lascrt11_session AS ss ON sa.session_id = ss.session_id AND ss.scratching_finished = 1 "
	    + "LEFT JOIN tl_lascrt11_user AS su ON ss.uid = su.session_uid "
	    + "LEFT JOIN tl_laasse10_option_answer AS aa ON a.answer_uid = aa.question_result_uid AND aa.answer_boolean = 1 "
	    + "WHERE tq.qb_question_uid = :qbQuestionUid GROUP BY opt HAVING opt IS NOT NULL";

    private static final String FIND_ANSWERS_BY_ACTIVITY = "SELECT COALESCE(mcu.que_usr_id, su.user_id, au.user_id) AS user_id, "
	    + "IF(su.user_id IS NULL, COALESCE(a.qb_option_uid, aa.question_option_uid), IF(COUNT(a.qb_option_uid) > 1, -1, a.qb_option_uid)) AS opt "
	    + "FROM lams_learning_activity AS act JOIN lams_qb_tool_question AS tq USING (tool_content_id) "
	    + "JOIN lams_qb_tool_answer AS a USING (tool_question_uid) "
	    + "LEFT JOIN tl_lamc11_usr_attempt AS mca ON a.answer_uid = mca.uid "
	    + "LEFT JOIN tl_lamc11_que_usr AS mcu ON mca.que_usr_id = mcu.uid "
	    + "LEFT JOIN tl_lascrt11_answer_log AS sa ON a.answer_uid = sa.uid "
	    + "LEFT JOIN tl_lascrt11_session AS ss ON sa.session_id = ss.session_id AND ss.scratching_finished = 1 "
	    + "LEFT JOIN tl_lascrt11_user AS su ON ss.uid = su.session_uid AND su.session_finished = 1 "
	    + "LEFT JOIN tl_laasse10_option_answer AS aa ON a.answer_uid = aa.question_result_uid AND aa.answer_boolean = 1 "
	    + "LEFT JOIN tl_laasse10_question_result AS aq ON a.answer_uid = aq.uid "
	    + "LEFT JOIN tl_laasse10_assessment_result AS ar ON aq.result_uid = ar.uid AND ar.finish_date IS NOT NULL AND ar.latest = 1 "
	    + "LEFT JOIN tl_laasse10_user AS au ON ar.user_uid = au.uid AND au.session_finished = 1 "
	    + "WHERE act.activity_id = :activityId AND tq.qb_question_uid = :qbQuestionUid GROUP BY user_id "
	    + "HAVING opt IS NOT NULL AND user_id IS NOT NULL";

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

    private static final String IS_QUESTION_IN_COLLECTION = "SELECT 1 FROM lams_qb_collection_question WHERE collection_uid = :collectionUid "
	    + "AND qb_question_id = :qbQuestionId";

    private static final String REMOVE_COLLECTION_QUESTION = "DELETE FROM lams_qb_collection_question WHERE collection_uid = :collectionUid "
	    + "AND qb_question_id = :qbQuestionId";

    private static final String FIND_COLLECTION_QUESTIONS_EXCLUDED = "SELECT qb_question_id FROM lams_qb_collection_question "
	    + "WHERE collection_uid = :collectionUid AND qb_question_id NOT IN :qbQuestionIds";

    private static final String FIND_QUESTIONS_BY_TOOL_CONTENT_ID = "SELECT tq.qbQuestion FROM QbToolQuestion AS tq "
	    + "WHERE tq.toolContentId = :toolContentId";

    private static final String IS_QUESTION_IN_USER_COLLECTION = "SELECT DISTINCT 1 FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_collection AS c ON cq.collection_uid = c.uid AND "
	    + "c.user_id = :userId AND cq.qb_question_id = :qbQuestionId";

    private static final String IS_QUESTION_IN_PUBLIC_COLLECTION = "SELECT DISTINCT 1 FROM lams_qb_collection_question AS cq "
	    + "JOIN lams_qb_collection AS c ON cq.collection_uid = c.uid AND "
	    + "c.user_id IS NULL AND cq.qb_question_id = :qbQuestionId";

    private static final String GENERATE_QUESTION_ID = "INSERT INTO lams_sequence_generator(lams_qb_question_question_id) VALUES (:qbQuestionId)";

    private static final String MERGE_TOOL_QUESTIONS = "UPDATE QbToolQuestion SET qbQuestion.uid = :targetQbQuestionUid WHERE qbQuestion.uid = :sourceQbQuestionUid";

    private static final String MERGE_QUESTION_ANSWERS = "UPDATE lams_qb_tool_answer AS tas "
	    + "JOIN lams_qb_option AS os ON os.qb_question_uid = :sourceQbQuestionUid AND tas.qb_option_uid = os.uid "
	    + "JOIN lams_qb_option AS ot ON ot.qb_question_uid = :targetQbQuestionUid AND os.display_order = ot.display_order "
	    + "SET tas.qb_option_uid = ot.uid";

    private static final String MERGE_ASSESSMENT_ANSWERS = "UPDATE lams_qb_option AS os "
	    + "JOIN lams_qb_option AS ot ON os.qb_question_uid = :sourceQbQuestionUid AND ot.qb_question_uid = :targetQbQuestionUid "
	    + "AND os.display_order = ot.display_order "
	    + "JOIN tl_laasse10_option_answer AS aa ON aa.question_option_uid = os.uid "
	    + "SET aa.question_option_uid = ot.uid";

    private static final String REMOVE_ANSWERS_BY_TOOL_CONTENT_ID = "DELETE a FROM lams_qb_tool_answer AS a JOIN lams_qb_tool_question AS tq "
	    + "USING (tool_question_uid) WHERE tq.tool_content_id = :toolContentId";

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
	q.setHint(QueryHints.HINT_CACHEABLE, true);
	return q.getResultList();
    }

    @Override
    public List<QbQuestion> getQuestionsByToolContentId(long toolContentId) {
	return getSession().createQuery(FIND_QUESTIONS_BY_TOOL_CONTENT_ID, QbQuestion.class)
		.setParameter("toolContentId", toolContentId).setCacheable(true).getResultList();
    }

    @Override
    public <T> List<T> getToolQuestionForToolContentId(Class<T> clazz, long toolContentId, long otherToolQuestionUid) {
	QbToolQuestion toolQuestion = find(QbToolQuestion.class, otherToolQuestionUid);

	String queryText = "FROM " + clazz.getName() + " AS tq "
		+ "WHERE tq.toolContentId = :toolContentId AND tq.qbQuestion.uid = :qbQuestionUid";

	return getSession().createQuery(queryText, clazz).setParameter("toolContentId", toolContentId)
		.setParameter("qbQuestionUid", toolQuestion.getQbQuestion().getUid()).setCacheable(true)
		.getResultList();
    }

    @Override
    public int generateNextQuestionId() {
	int max = ((BigInteger) this.getSession().createNativeQuery(FIND_MAX_QUESTION_ID_FROM_GENERATOR).uniqueResult())
		.intValue();
	max++;
	this.getSession().createNativeQuery(GENERATE_QUESTION_ID).setParameter("qbQuestionId", max).executeUpdate();
	return max;
    }

    @Override
    public void updateMaxQuestionId() {
	int maxGenerator = ((BigInteger) this.getSession().createNativeQuery(FIND_MAX_QUESTION_ID_FROM_GENERATOR)
		.uniqueResult()).intValue();
	int maxTable = ((BigInteger) this.getSession().createNativeQuery(FIND_MAX_QUESTION_ID).uniqueResult())
		.intValue();
	if (maxGenerator < maxTable) {
	    this.getSession().createNativeQuery(GENERATE_QUESTION_ID).setParameter("qbQuestionId", maxTable)
		    .executeUpdate();
	}
    }

    @Override
    public int getMaxQuestionVersion(Integer qbQuestionId) {
	Object result = this.getSession().createQuery(FIND_MAX_VERSION).setParameter("questionId", qbQuestionId)
		.uniqueResult();
	Integer max = (Integer) result;
	return max == null ? 1 : max;
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
    public List<ToolContent> getQuestionActivities(long qbQuestionUid, Collection<Long> filteringToolContentIds) {
	return this.getSession().createQuery(FIND_QUESTION_ACTIVITIES_FILTERED_BY_TOOL_CONTENT_ID)
		.setParameter("qbQuestionUid", qbQuestionUid)
		.setParameterList("toolContentIds", filteringToolContentIds).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<QbQuestion> getQuestionVersions(long qbQuestionUid) {
	return this.getSession().createQuery(FIND_QUESTION_VERSIONS).setParameter("qbQuestionUid", qbQuestionUid)
		.setCacheable(true).list();
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
    public List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, Long learningDesignId,
	    int page, int size, String sortBy, String sortOrder, String searchString) {
	return (List<QbQuestion>) getPagedQuestions(questionTypes, collectionUids, learningDesignId, page, size, sortBy,
		sortOrder, searchString, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	return (List<QbQuestion>) getPagedQuestions(questionTypes, collectionUids, null, page, size, sortBy, sortOrder,
		searchString, false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BigInteger> getAllQuestionUids(String collectionUids, String sortBy, String sortOrder,
	    String searchString) {
	return (List<BigInteger>) getPagedQuestions(null, collectionUids, null, 0, 100000, sortBy, sortOrder,
		searchString, true);
    }

    private List<?> getPagedQuestions(String questionTypes, String collectionUids, Long learningDesignId, int page,
	    int size, String sortBy, String sortOrder, String searchString, boolean onlyUidsRequested) {

	StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT ").append(onlyUidsRequested ? "q.uid" : "q.*")
		.append(" FROM (SELECT question.* FROM lams_qb_question question");
	if (searchString != null) {
	    queryBuilder.append(" LEFT JOIN lams_qb_option qboption ON qboption.qb_question_uid = question.uid");
	}
	if (collectionUids != null) {
	    queryBuilder.append(
		    " JOIN lams_qb_collection_question collection ON question.question_id = collection.qb_question_id");
	}
	if (learningDesignId != null) {
	    queryBuilder.append(
		    " JOIN lams_qb_tool_question tool_question ON question.uid = tool_question.qb_question_uid")
		    .append(" JOIN lams_learning_activity activity USING (tool_content_id)");
	}

	queryBuilder.append(" WHERE");
	if (questionTypes != null) {
	    queryBuilder.append(" question.type in (:questionTypes) AND");
	}
	if (collectionUids != null) {
	    queryBuilder.append(" collection.collection_uid in (:collectionUids) AND");
	}
	if (learningDesignId != null) {
	    queryBuilder.append(" activity.learning_design_id = :learningDesignId AND");
	}
	if (searchString == null) {
	    // there has to be something after AND or even after just WHERE
	    queryBuilder.append(" TRUE");
	} else {
	    // we sort of strip out HTML tags from the search by using REGEXP_REPLACE which skips all the content between < >
	    queryBuilder.append(
		    " (REGEXP_REPLACE(question.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')")
		    .append(" OR question.name LIKE CONCAT('%', :searchString, '%')")
		    .append(" OR REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%'))");
	}

	queryBuilder.append(" ORDER BY question.version DESC) AS q GROUP BY q.question_id");

	if ("smth_else".equalsIgnoreCase(sortBy)) {
	    queryBuilder.append(" ORDER BY q.question_id ");
	} else {
	    queryBuilder.append(" ORDER BY q.name, q.description ");
	}

	LAMSBaseDAO.sanitiseQueryPart(sortOrder);
	queryBuilder.append(sortOrder);

	NativeQuery<?> query = getSession().createNativeQuery(queryBuilder.toString());
	if (questionTypes != null) {
	    query.setParameterList("questionTypes", questionTypes.split(","));
	}
	if (collectionUids != null) {
	    query.setParameterList("collectionUids", collectionUids.split(","));
	}
	if (learningDesignId != null) {
	    query.setParameter("learningDesignId", learningDesignId);
	}
	if (searchString != null) {
	    // support for custom search from the toolbar
	    searchString = searchString == null ? "" : searchString;
	    query.setParameter("searchString", searchString);
	}

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
	StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(DISTINCT c.uid) AS cnt FROM (SELECT q.uid FROM ")
		.append(" (SELECT question.uid, question.question_id FROM lams_qb_question question");
	if (searchString != null) {
	    queryBuilder.append(" LEFT JOIN lams_qb_option qboption ON qboption.qb_question_uid = question.uid");
	}
	if (collectionUids != null) {
	    queryBuilder.append(
		    " LEFT JOIN lams_qb_collection_question collection ON question.question_id = collection.qb_question_id");
	}

	queryBuilder.append(" WHERE");
	if (questionTypes != null) {
	    queryBuilder.append(" question.type in (:questionTypes) AND");
	}
	if (collectionUids != null) {
	    queryBuilder.append(" collection.collection_uid in (:collectionUids) AND");
	}
	if (searchString == null) {
	    // there has to be something after AND or even after just WHERE
	    queryBuilder.append(" TRUE");
	} else {
	    // we sort of strip out HTML tags from the search by using REGEXP_REPLACE which skips all the content between < >
	    queryBuilder.append(
		    " (REGEXP_REPLACE(question.description, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%')")
		    .append(" OR question.name LIKE CONCAT('%', :searchString, '%')")
		    .append(" OR REGEXP_REPLACE(qboption.name, '<[^>]*>+', '') LIKE CONCAT('%', :searchString, '%'))");
	}

	queryBuilder.append(") AS q GROUP BY q.question_id) AS c");

	NativeQuery<?> query = getSession().createNativeQuery(queryBuilder.toString()).addScalar("cnt",
		IntegerType.INSTANCE);
	if (questionTypes != null) {
	    query.setParameterList("questionTypes", questionTypes.split(","));
	}
	if (collectionUids != null) {
	    query.setParameterList("collectionUids", collectionUids.split(","));
	}
	// support for custom search from a toolbar
	if (searchString != null) {
	    // support for custom search from a toolbar
	    searchString = searchString == null ? "" : searchString;
	    query.setParameter("searchString", searchString);
	}
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
	if (!isQuestionInCollection(collectionUid, qbQuestionId)) {
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
		.setParameter("qbQuestionId", qbQuestionId).uniqueResult() != null;
    }

    @Override
    public boolean isQuestionInPublicCollection(int qbQuestionId) {
	return getSession().createNativeQuery(IS_QUESTION_IN_PUBLIC_COLLECTION)
		.setParameter("qbQuestionId", qbQuestionId).uniqueResult() != null;
    }

    private boolean isQuestionInCollection(long collectionUid, int qbQuestionId) {
	return !getSession().createNativeQuery(IS_QUESTION_IN_COLLECTION).setParameter("collectionUid", collectionUid)
		.setParameter("qbQuestionId", qbQuestionId).getResultList().isEmpty();
    }

    @Override
    public int mergeQuestions(long sourceQbQUestionUid, long targetQbQuestionUid) {
	int result = getSession().createNativeQuery(MERGE_QUESTION_ANSWERS)
		.setParameter("sourceQbQuestionUid", sourceQbQUestionUid)
		.setParameter("targetQbQuestionUid", targetQbQuestionUid).executeUpdate();

	result += getSession().createNativeQuery(MERGE_ASSESSMENT_ANSWERS)
		.setParameter("sourceQbQuestionUid", sourceQbQUestionUid)
		.setParameter("targetQbQuestionUid", targetQbQuestionUid).executeUpdate();

	getSession().createQuery(MERGE_TOOL_QUESTIONS).setParameter("sourceQbQuestionUid", sourceQbQUestionUid)
		.setParameter("targetQbQuestionUid", targetQbQuestionUid).executeUpdate();

	return result;
    }

    @Override
    public void removeAnswersByToolContentId(long toolContentId) {
	getSession().createNativeQuery(REMOVE_ANSWERS_BY_TOOL_CONTENT_ID).setParameter("toolContentId", toolContentId)
		.executeUpdate();
    }

    private Query prepareCollectionQuestionsQuery(long collectionUid, String orderBy, String orderDirection,
	    String search, boolean isCount) {
	StringBuilder queryBuilder = new StringBuilder(FIND_COLLECTION_QUESTIONS);

	if (!isCount && StringUtils.isNotBlank(orderBy)) {
	    if (orderBy.equalsIgnoreCase("usage")) {
		queryBuilder = new StringBuilder(FIND_COLLECTION_QUESTIONS_BY_USAGE);
	    } else {
		LAMSBaseDAO.sanitiseQueryPart(orderBy);
		queryBuilder.append(" ORDER BY ").append(orderBy);
	    }
	    if (StringUtils.isNotBlank(orderDirection)) {
		LAMSBaseDAO.sanitiseQueryPart(orderDirection);
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