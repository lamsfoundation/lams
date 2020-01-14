package org.lamsfoundation.lams.qb.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.lamsfoundation.lams.qb.dto.QbStatsActivityDTO;
import org.lamsfoundation.lams.qb.dto.QbStatsDTO;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbQuestionUnit;
import org.lamsfoundation.lams.usermanagement.Organisation;

public interface IQbService {

    // statuses of comparing QB question coming from authoring with data existing in DB

    // no change detected
    static final int QUESTION_MODIFIED_NONE = 0;
    // change is minor, so the original question will be modified
    static final int QUESTION_MODIFIED_UPDATE = 1;
    // it is a new version of the old question
    static final int QUESTION_MODIFIED_VERSION_BUMP = 2;
    // it is a new question
    static final int QUESTION_MODIFIED_ID_BUMP = 3;

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

    QbQuestion getQuestionByUUID(UUID uuid);

    /**
     * @param optionUid
     * @return QbOption by its uid. Besides, it releases returned object and associated qbQuestion from the cache.
     */
    QbOption getOptionByUid(Long optionUid);

    /**
     * @param unitUid
     * @return QbQuestionUnit by its uid. Besides, it releases returned object and associated qbQuestion from the cache.
     */
    QbQuestionUnit getQuestionUnitByUid(Long unitUid);

    // finds next question ID for Question Bank question
    int generateNextQuestionId();

    // finds next version for given question ID for Question Bank question
    int getMaxQuestionVersion(Integer qbQuestionId);

    QbStatsDTO getQuestionStats(long qbQuestionUid);

    int countQuestionVersions(int qbQuestionId);

    QbStatsActivityDTO getActivityStatsByContentId(Long toolContentId, Long qbQuestionUid);

    QbStatsActivityDTO getActivityStats(Long activityId, Long qbQuestionUid);

    QbStatsActivityDTO getActivityStats(Long activityId, Long qbQuestionUid, Collection<Long> correctOptionUids);

    List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, int page, int size, String sortBy,
	    String sortOrder, String searchString);

    List<BigInteger> getAllQuestionUids(String collectionUids, String sortBy, String sortOrder, String searchString);

    int getCountQuestions(String questionTypes, String collectionUids, String searchString);

    QbCollection getCollectionByUid(Long collectionUid);

    QbCollection getPublicCollection();

    QbCollection getUserPrivateCollection(int userId);

    List<QbCollection> getUserCollections(int userId);

    List<QbCollection> getUserOwnCollections(int userId);

    List<QbQuestion> getCollectionQuestions(long collectionUid);

    List<QbQuestion> getCollectionQuestions(long collectionUid, Integer offset, Integer limit, String orderBy,
	    String orderDirection, String search);

    int getCountCollectionQuestions(long collectionUid, String search);

    List<QbCollection> getQuestionCollectionsByUid(long qbQuestionUid);

    List<QbCollection> getQuestionCollectionsByQuestionId(int qbQuestionId);

    QbCollection addCollection(int userId, String name);

    void removeCollection(long collectionUid);

    List<Organisation> getShareableWithOrganisations(long collectionUid, int userId);

    Organisation shareCollection(long collectionUid, int organisationId);

    void unshareCollection(long collectionUid, int organisationId);

    void addQuestionToCollection(long collectionUid, int qbQuestionId, boolean copy);

    void addQuestionToCollection(long sourceCollectionUid, long targetCollectionUid,
	    Collection<Integer> excludedQbQuestionIds, boolean copy);

    boolean removeQuestionFromCollectionByUid(long collectionUid, long qbQuestionUid);

    boolean removeQuestionFromCollectionByQuestionId(long collectionUid, int qbQuestionId, boolean tryRemovingQuestion);

    Collection<Integer> removeQuestionFromCollection(long collectionUid, Collection<Integer> excludedQbQuestionIds);

    boolean removeQuestionByUid(long qbQuestionUid);

    boolean removeQuestionByQuestionId(int qbQuestionId);

    boolean removeQuestionPossibleByUid(long qbQuestionUid);

    boolean removeQuestionPossibleByQuestionId(int qbQuestionId);

    QbCollection getCollection(long collectionUid);

    int getCountQuestionActivitiesByUid(long qbQuestionUid);

    int getCountQuestionActivitiesByQuestionId(int qbQuestionId);

    void changeCollectionName(long collectionUid, String name);

    void releaseFromCache(Object object);

    boolean isQuestionInUserCollection(int qbQuestionId, int userId);

    boolean isQuestionInPublicCollection(int qbQuestionId);

    void insertQuestion(QbQuestion qbQuestion);

    void prepareQuestionForExport(QbQuestion qbQuestion);

    int mergeQuestions(long sourceQbQuestionUid, long targetQbQuestionUid);
}
