package org.lamsfoundation.lams.qb.dao;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.ToolContent;

public interface IQbDAO extends IBaseDAO {

    /**
     * @param qbQuestionUid
     * @return QbQuestion object with the specified uid
     */
    QbQuestion getQuestionByUid(Long qbQuestionUid);

    /**
     * @param questionId
     * @return questions sharing the same questionId
     */
    List<QbQuestion> getQuestionsByQuestionId(Integer questionId);

    <T> List<T> getToolQuestionForToolContentId(Class<T> clazz, long toolContentId, long otherToolQuestionUid);

    List<QbQuestion> getQuestionsByToolContentId(long toolContentId);

    // finds next question ID for Question Bank question
    int generateNextQuestionId();

    void updateMaxQuestionId();

    // finds next version for given question ID for Question Bank question
    int getMaxQuestionVersion(Integer qbQuestionId);

    List<ToolActivity> getQuestionActivities(long qbQuestionUid);

    List<ToolContent> getQuestionActivities(long qbQuestionUid, Collection<Long> filteringToolContentIds);

    int getCountQuestionActivitiesByUid(long qbQuestionUid);

    int getCountQuestionActivitiesByQuestionId(int qbQuestionId);

    List<QbQuestion> getQuestionVersions(long qbQuestionUid);

    Map<Long, Long> getAnswerStatsForQuestion(long qbQuestionUid);

    Map<Integer, Long> getAnswersForActivity(long activityId, long qbQuestionUid);

    Map<String, Long> getBurningQuestions(long qbQuestionUid);

    List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, int page, int size, String sortBy,
	    String sortOrder, String searchString);

    List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, Long learningDesignId, int page,
	    int size, String sortBy, String sortOrder, String searchString);

    List<BigInteger> getAllQuestionUids(String collectionUids, String sortBy, String sortOrder, String searchString);

    int getCountQuestions(String questionTypes, String collectionUids, String searchString);

    List<QbQuestion> getCollectionQuestions(long collectionUid);

    List<QbQuestion> getCollectionQuestions(long collectionUid, Integer offset, Integer limit, String orderBy,
	    String orderDirection, String search);

    List<QbCollection> getQuestionCollectionsByUid(long qbQuestionUid);

    List<QbCollection> getQuestionCollectionsByQuestionId(int qbQuestionId);

    List<QbCollection> getSharedQuestionCollections(int userId);

    int getCountCollectionQuestions(long collectionUid, String search);

    void addCollectionQuestion(long collectionUid, int qbQuestionId);

    void removeCollectionQuestion(long collectionUid, int qbQuestionId);

    Set<Integer> getCollectionQuestionIdsExcluded(long collectionUid, Collection<Integer> qbQuestionIds);

    boolean isQuestionInUserCollection(int userId, int qbQuestionId);

    boolean isQuestionInPublicCollection(int qbQuestionId);

    int mergeQuestions(long sourceQbQUestionUid, long targetQbQuestionUid);

    void removeAnswersByToolContentId(long toolContentId);
}