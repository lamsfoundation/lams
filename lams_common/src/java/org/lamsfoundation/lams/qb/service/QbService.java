package org.lamsfoundation.lams.qb.service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.qb.QbConstants;
import org.lamsfoundation.lams.qb.QbUtils;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.dto.QbStatsActivityDTO;
import org.lamsfoundation.lams.qb.dto.QbStatsDTO;
import org.lamsfoundation.lams.qb.form.QbQuestionForm;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbQuestionUnit;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.service.IQbToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class QbService implements IQbService {

    protected Logger log = Logger.getLogger(QbService.class);

    private IQbDAO qbDAO;

    private IGradebookService gradebookService;

    private ILamsCoreToolService lamsCoreToolService;

    private IUserManagementService userManagementService;

    private ILogEventService logEventService;

    private ILamsToolService toolService;

    public static final Comparator<QbCollection> COLLECTION_NAME_COMPARATOR = Comparator
	    .comparing(QbCollection::getName);

    @Override
    public QbQuestion getQuestionByUid(Long qbQuestionUid) {
	return qbDAO.getQuestionByUid(qbQuestionUid);
    }

    @Override
    public List<QbQuestion> getQuestionsByQuestionId(Integer questionId) {
	return qbDAO.getQuestionsByQuestionId(questionId);
    }

    @Override
    public QbQuestion getQuestionByUUID(UUID uuid) {
	if (uuid == null) {
	    return null;
	}
	List<QbQuestion> result = qbDAO.findByProperty(QbQuestion.class, "uuid", uuid);
	return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public <T> List<T> getToolQuestionForToolContentId(Class<T> clazz, long toolContentId, long otherToolQuestionUid) {
	return qbDAO.getToolQuestionForToolContentId(clazz, toolContentId, otherToolQuestionUid);
    }

    @Override
    public QbOption getOptionByUid(Long optionUid) {
	QbOption option = qbDAO.find(QbOption.class, optionUid);
	qbDAO.releaseFromCache(option);
	qbDAO.releaseFromCache(option.getQbQuestion());
	return option;
    }

    @Override
    public QbQuestionUnit getQuestionUnitByUid(Long unitUid) {
	QbQuestionUnit unit = qbDAO.find(QbQuestionUnit.class, unitUid);
	qbDAO.releaseFromCache(unit);
	qbDAO.releaseFromCache(unit.getQbQuestion());
	return unit;
    }

    @Override
    public int generateNextQuestionId() {
	return qbDAO.generateNextQuestionId();
    }

    @Override
    public void updateMaxQuestionId() {
	qbDAO.updateMaxQuestionId();
    }

    @Override
    public int getMaxQuestionVersion(Integer qbQuestionId) {
	return qbDAO.getMaxQuestionVersion(qbQuestionId);
    }

    @Override
    public List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids,
	    Long onlyInSameLearningDesignAsToolContentID, int page, int size, String sortBy, String sortOrder,
	    String searchString) {
	Long learningDesignId = null;
	if (onlyInSameLearningDesignAsToolContentID != null) {
	    List<ToolActivity> toolActivities = qbDAO.findByProperty(ToolActivity.class, "toolContentId",
		    onlyInSameLearningDesignAsToolContentID);
	    if (!toolActivities.isEmpty()) {
		learningDesignId = toolActivities.get(0).getLearningDesign().getLearningDesignId();
	    }
	}
	return qbDAO.getPagedQuestions(questionTypes, collectionUids, learningDesignId, page, size, sortBy, sortOrder,
		searchString);
    }

    @Override
    public List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	return getPagedQuestions(questionTypes, collectionUids, null, page, size, sortBy, sortOrder, searchString);
    }

    @Override
    public List<BigInteger> getAllQuestionUids(String collectionUids, String sortBy, String sortOrder,
	    String searchString) {
	return qbDAO.getAllQuestionUids(collectionUids, sortBy, sortOrder, searchString);
    }

    @Override
    public int getCountQuestions(String questionTypes, String collectionUids, String searchString) {
	return qbDAO.getCountQuestions(questionTypes, collectionUids, searchString);
    }

    @Override
    public QbStatsDTO getQuestionStats(long qbQuestionUid) {
	QbStatsDTO stats = new QbStatsDTO();
	QbQuestion qbQuestion = qbDAO.find(QbQuestion.class, qbQuestionUid);
	List<QbOption> qbOptions = qbQuestion.getQbOptions();
	stats.setQuestion(qbQuestion);
	Map<String, Long> burningQuestions = qbDAO.getBurningQuestions(qbQuestionUid);
	stats.setBurningQuestions(burningQuestions);

	List<ToolActivity> activities = qbDAO.getQuestionActivities(qbQuestionUid);
	List<QbStatsActivityDTO> activityDTOs = new LinkedList<>();

	Set<Long> correctOptionUids = new HashSet<>();
	for (QbOption option : qbOptions) {
	    if (option.isCorrect()) {
		correctOptionUids.add(option.getUid());
	    }
	}
	// calculate correct answer average for each activity
	for (ToolActivity activity : activities) {
	    QbStatsActivityDTO activityDTO = getActivityStats(activity.getActivityId(), qbQuestionUid,
		    correctOptionUids);
	    activityDTOs.add(activityDTO);
	}
	stats.setActivities(activityDTOs);

	stats.setVersions(qbDAO.getQuestionVersions(qbQuestionUid));
	Map<Long, Long> answersRaw = qbDAO.getAnswerStatsForQuestion(qbQuestionUid);
	stats.setAnswersRaw(answersRaw);

	ArrayNode answersJSON = JsonNodeFactory.instance.arrayNode();
	double total = 0;
	for (Long answerCount : answersRaw.values()) {
	    total += answerCount;
	}
	Map<Long, Integer> answerPercent = new HashMap<>();
	for (int answerIndex = 0; answerIndex < qbOptions.size(); answerIndex++) {
	    QbOption option = qbOptions.get(answerIndex);
	    Long answerCount = answersRaw.get(option.getUid());
	    int value = answerCount == null ? 0 : Long.valueOf(Math.round(answerCount / total * 100)).intValue();
	    answerPercent.put(option.getUid(), value);

	    ObjectNode answerJSON = JsonNodeFactory.instance.objectNode();
	    String name = WebUtil.removeHTMLtags(option.getName());
	    name = (answerIndex + 1) + ". " + (name.length() > 30 ? name.substring(0, 30) + "..." : name);
	    answerJSON.put("name", name);
	    answerJSON.put("value", value);
	    answersJSON.add(answerJSON);
	}
	stats.setAnswersPercent(answerPercent);
	stats.setAnswersJSON(answersJSON.toString());
	return stats;
    }

    @Override
    public int countQuestionVersions(int qbQuestionId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("questionId", qbQuestionId);
	return Long.valueOf(qbDAO.countByProperties(QbQuestion.class, properties)).intValue();
    }

    @Override
    public QbStatsActivityDTO getActivityStatsByContentId(Long toolContentId, Long qbQuestionUid) {
	Activity activity = qbDAO.findByProperty(ToolActivity.class, "toolContentId", toolContentId).get(0);
	return getActivityStats(activity.getActivityId(), qbQuestionUid);
    }

    @Override
    public QbStatsActivityDTO getActivityStats(Long activityId, Long qbQuestionUid) {
	QbQuestion qbQuestion = qbDAO.find(QbQuestion.class, qbQuestionUid);
	Set<Long> correctOptionUids = new HashSet<>();
	for (QbOption option : qbQuestion.getQbOptions()) {
	    if (option.isCorrect()) {
		correctOptionUids.add(option.getUid());
	    }
	}
	return getActivityStats(activityId, qbQuestionUid, correctOptionUids);
    }

    @Override
    public QbStatsActivityDTO getActivityStats(Long activityId, Long qbQuestionUid,
	    Collection<Long> correctOptionUids) {
	ToolActivity activity = qbDAO.find(ToolActivity.class, activityId);
	LearningDesign learningDesign = activity.getLearningDesign();
	Lesson lesson = learningDesign.getLessons().iterator().next();
	Long lessonId = lesson.getLessonId();
	List<GradebookUserLesson> userLessonGrades = gradebookService.getGradebookUserLesson(lessonId);
	userLessonGrades = userLessonGrades.stream().filter(g -> g.getMark() != null).collect(Collectors.toList());
	int participantCount = userLessonGrades.size();

	QbStatsActivityDTO activityDTO = new QbStatsActivityDTO();
	activityDTO.setActivity(activity);

	String monitorUrl = "/lams/" + lamsCoreToolService.getToolMonitoringURL(lessonId, activity)
		+ "&contentFolderID=" + learningDesign.getContentFolderID();
	activityDTO.setMonitorURL(monitorUrl);

	// if there is only 1 participant, there is no point in calculating question indexes
	if (participantCount >= Configuration.getAsInt(ConfigurationKeys.QB_STATS_MIN_PARTICIPANTS)) {
	    // mapping of user ID -> option UID
	    Map<Integer, Long> activityAnswers = qbDAO.getAnswersForActivity(activity.getActivityId(), qbQuestionUid);
	    // take only learners who finished (not only submitted) this activity
	    userLessonGrades = userLessonGrades.stream()
		    .filter(g -> activityAnswers.containsKey(g.getLearner().getUserId())).collect(Collectors.toList());
	    participantCount = userLessonGrades.size();
	    activityDTO.setParticipantCount(participantCount);

	    // see who answered correctly
	    Set<Integer> correctUserIds = new HashSet<>();
	    for (Entry<Integer, Long> answer : activityAnswers.entrySet()) {
		Integer userId = answer.getKey();
		Long optionUid = answer.getValue();
		if (correctOptionUids.contains(optionUid)) {
		    correctUserIds.add(userId);
		}
	    }

	    int correctUserCount = correctUserIds.size();
	    int incorrectUserCount = participantCount - correctUserCount;
	    int topUsersCorrect = 0;
	    int bottomUsersCorrect = 0;
	    double allUserMarkSum = 0;
	    double correctUserMarkSum = 0;
	    double incorrectUserMarkSum = 0;

	    // sort grades by highest mark
	    Collections.sort(userLessonGrades, Comparator.comparing(GradebookUserLesson::getMark).reversed());
	    // see how many learners should be in top/bottom 27% of the group
	    int groupCount = (int) Math
		    .ceil(Configuration.getAsInt(ConfigurationKeys.QB_STATS_GROUP_SIZE) / 100.0 * participantCount);

	    // go through each grade and gather data for indexes
	    for (int userIndex = 0; userIndex < participantCount; userIndex++) {
		GradebookUserLesson grade = userLessonGrades.get(userIndex);
		double mark = grade.getMark();
		allUserMarkSum += mark;
		boolean isCorrect = correctUserIds.contains(grade.getLearner().getUserId());
		if (isCorrect) {
		    correctUserMarkSum += mark;
		    if (userIndex < groupCount) {
			topUsersCorrect++;
		    } else if (userIndex >= participantCount - groupCount) {
			bottomUsersCorrect++;
		    }
		} else {
		    incorrectUserMarkSum += mark;
		}
	    }

	    // calculate standard deviation needed for point biserial
	    double deviation = 0;
	    double markAverage = allUserMarkSum / participantCount;
	    for (int userIndex = 0; userIndex < participantCount; userIndex++) {
		GradebookUserLesson grade = userLessonGrades.get(userIndex);
		double mark = grade.getMark();
		deviation += Math.pow(mark - markAverage, 2);
	    }
	    deviation = Math.sqrt(deviation / participantCount);

	    activityDTO.setDifficultyIndex(participantCount == 0 ? null : (double) correctUserCount / participantCount);
	    activityDTO.setDiscriminationIndex((double) (topUsersCorrect - bottomUsersCorrect) / groupCount);
	    if (correctUserCount == 0 || incorrectUserCount == 0) {
		activityDTO.setPointBiserial(0d);
	    } else {
		activityDTO.setPointBiserial(
			(correctUserMarkSum / correctUserCount - incorrectUserMarkSum / incorrectUserCount) / deviation
				* Math.sqrt(correctUserCount * incorrectUserCount / Math.pow(participantCount, 2)));
	    }
	}

	return activityDTO;
    }

    @Override
    public QbCollection getCollectionByUid(Long collectionUid) {
	return qbDAO.find(QbCollection.class, collectionUid);
    }

    @Override
    public QbCollection getPublicCollection() {
	return qbDAO.find(QbCollection.class, 1L);
    }

    @Override
    public QbCollection getUserPrivateCollection(int userId) {
	Map<String, Object> map = new HashMap<>();
	map.put("userId", userId);
	map.put("personal", true);
	List<QbCollection> result = qbDAO.findByProperties(QbCollection.class, map);
	if (!result.isEmpty()) {
	    return result.get(0);
	}

	// is an user does not have a private collection yet, create it
	QbCollection collection = new QbCollection();
	collection.setName("My questions");
	collection.setUserId(userId);
	collection.setPersonal(true);
	qbDAO.insert(collection);
	return collection;
    }

    @Override
    public List<QbCollection> getUserOwnCollections(int userId) {
	Map<String, Object> map = new HashMap<>();
	map.put("userId", userId);
	map.put("personal", false);
	List<QbCollection> result = qbDAO.findByProperties(QbCollection.class, map);
	Collections.sort(result, COLLECTION_NAME_COMPARATOR);
	return result;
    }

    @Override
    public List<QbQuestion> getCollectionQuestions(long collectionUid) {
	return qbDAO.getCollectionQuestions(collectionUid);
    }

    @Override
    public List<QbQuestion> getCollectionQuestions(long collectionUid, Integer offset, Integer limit, String orderBy,
	    String orderDirection, String search) {
	return qbDAO.getCollectionQuestions(collectionUid, offset, limit, orderBy, orderDirection, search);
    }

    @Override
    public int getCountCollectionQuestions(long collectionUid, String search) {
	return qbDAO.getCountCollectionQuestions(collectionUid, search);
    }

    @Override
    public List<QbCollection> getQuestionCollectionsByUid(long qbQuestionUid) {
	return qbDAO.getQuestionCollectionsByUid(qbQuestionUid);
    }

    @Override
    public List<QbCollection> getQuestionCollectionsByQuestionId(int qbQuestionId) {
	return qbDAO.getQuestionCollectionsByQuestionId(qbQuestionId);
    }

    @Override
    public QbCollection addCollection(int userId, String name) {
	QbCollection collection = new QbCollection();
	collection.setName(name);
	collection.setUserId(userId);
	qbDAO.insert(collection);

	if (log.isDebugEnabled()) {
	    log.debug("User " + userId + " created a new QB collection: " + name);
	}

	return collection;
    }

    @Override
    public void removeCollection(long collectionUid) {
	if (getCountCollectionQuestions(collectionUid, null) > 0) {
	    throw new InvalidParameterException("Can not remove a collection with questions");
	}
	QbCollection collection = qbDAO.find(QbCollection.class, collectionUid);
	if (collection.getUserId() == null || collection.isPersonal()) {
	    throw new InvalidParameterException("Attempt to remove a private or the public question bank collection");
	}

	if (log.isDebugEnabled()) {
	    log.debug("Removed collection with UID: " + collectionUid + " and name: " + collection.getName());
	}

	qbDAO.delete(collection);

    }

    @Override
    public boolean removeQuestionByUid(long qbQuestionUid) {
	boolean removeQuestionPossible = removeQuestionPossibleByUid(qbQuestionUid);
	if (!removeQuestionPossible) {
	    // if the question is used in a Learning Design, do not allow to remove it
	    return false;
	}

	if (log.isDebugEnabled()) {
	    log.debug("Removed QB question with UID: " + qbQuestionUid);
	}

	qbDAO.deleteById(QbQuestion.class, qbQuestionUid);
	return true;
    }

    @Override
    public boolean removeQuestionByQuestionId(int qbQuestionId) {
	boolean removeQuestionPossible = removeQuestionPossibleByQuestionId(qbQuestionId);
	if (!removeQuestionPossible) {
	    // if the question is used in a Learning Design, do not allow to remove it
	    return false;
	}
	Map<String, Object> properties = new HashMap<>();
	properties.put("questionId", qbQuestionId);
	qbDAO.deleteByProperties(QbQuestion.class, properties);

	if (log.isDebugEnabled()) {
	    log.debug("Removed QB questions with question ID: " + qbQuestionId);
	}

	return true;
    }

    @Override
    public boolean removeQuestionPossibleByUid(long qbQuestionUid) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("qbQuestion.uid", qbQuestionUid);
	return qbDAO.countByProperties(QbToolQuestion.class, properties) == 0;
    }

    @Override
    public boolean removeQuestionPossibleByQuestionId(int qbQuestionId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("qbQuestion.questionId", qbQuestionId);
	return qbDAO.countByProperties(QbToolQuestion.class, properties) == 0;
    }

    @Override
    public void removeAnswersByToolContentId(long toolContentId) {
	qbDAO.removeAnswersByToolContentId(toolContentId);
    }

    @Override
    public Organisation shareCollection(long collectionUid, int organisationId) {
	QbCollection collection = qbDAO.find(QbCollection.class, collectionUid);
	if (collection.getUserId() == null || collection.isPersonal()) {
	    throw new InvalidParameterException("Attempt to share a private or the public question bank collection");
	}
	Organisation organisation = qbDAO.find(Organisation.class, organisationId);
	collection.getOrganisations().add(organisation);
	qbDAO.update(collection);
	return organisation;
    }

    @Override
    public void unshareCollection(long collectionUid, int organisationId) {
	QbCollection collection = qbDAO.find(QbCollection.class, collectionUid);
	if (collection.getUserId() == null || collection.isPersonal()) {
	    throw new InvalidParameterException("Attempt to unshare a private or the public question bank collection");
	}
	Iterator<Organisation> orgIterator = collection.getOrganisations().iterator();
	while (orgIterator.hasNext()) {
	    if (orgIterator.next().getOrganisationId().equals(organisationId)) {
		orgIterator.remove();
		break;
	    }
	}
	qbDAO.update(collection);
    }

    @Override
    public void addQuestionToCollection(long collectionUid, int qbQuestionId, boolean copy) {
	int addQbQuestionId = qbQuestionId;
	if (copy) {
	    List<QbQuestion> questions = getQuestionsByQuestionId(qbQuestionId);
	    QbQuestion question = questions.get(0);
	    QbQuestion newQuestion = question.clone();
	    addQbQuestionId = generateNextQuestionId();
	    newQuestion.setQuestionId(addQbQuestionId);
	    newQuestion.setVersion(1);
	    newQuestion.setCreateDate(new Date());
	    newQuestion.clearID();
	    qbDAO.insert(newQuestion);
	}
	qbDAO.addCollectionQuestion(collectionUid, addQbQuestionId);

	if (log.isDebugEnabled()) {
	    log.debug("Added QB questions with question ID: " + qbQuestionId + " to collection with UID: "
		    + collectionUid);
	}
    }

    @Override
    public void addQuestionToCollection(long sourceCollectionUid, long targetCollectionUid,
	    Collection<Integer> excludedQbQuestionIds, boolean copy) {
	Collection<Integer> includedIds = qbDAO.getCollectionQuestionIdsExcluded(sourceCollectionUid,
		excludedQbQuestionIds);
	for (Integer questionId : includedIds) {
	    addQuestionToCollection(targetCollectionUid, questionId, copy);
	}
    }

    @Override
    public boolean removeQuestionFromCollectionByUid(long collectionUid, long qbQuestionUid) {
	QbQuestion question = getQuestionByUid(qbQuestionUid);
	return removeQuestionFromCollectionByQuestionId(collectionUid, question.getQuestionId(), true);
    }

    @Override
    public boolean removeQuestionFromCollectionByQuestionId(long collectionUid, int qbQuestionId,
	    boolean tryRemovingQuestion) {
	Collection<QbCollection> collections = getQuestionCollectionsByQuestionId(qbQuestionId);
	int size = collections.size();
	if (size <= 1) {
	    // if the question is in its last collection, try to remove it permanently
	    return removeQuestionByQuestionId(qbQuestionId);
	}
	qbDAO.removeCollectionQuestion(collectionUid, qbQuestionId);

	if (log.isDebugEnabled()) {
	    log.debug("Removed QB questions with question ID: " + qbQuestionId + " from collection with UID: "
		    + collectionUid);
	}

	return true;
    }

    @Override
    public Collection<Integer> removeQuestionFromCollection(long collectionUid,
	    Collection<Integer> excludedQbQuestionIds) {
	Collection<Integer> includedIds = qbDAO.getCollectionQuestionIdsExcluded(collectionUid, excludedQbQuestionIds);
	Collection<Integer> retainedQuestionIds = new HashSet<>();
	for (Integer questionId : includedIds) {
	    boolean deleted = removeQuestionFromCollectionByQuestionId(collectionUid, questionId, true);
	    if (!deleted) {
		retainedQuestionIds.add(questionId);
	    }
	}
	return retainedQuestionIds;
    }

    @Override
    public List<Organisation> getShareableWithOrganisations(long collectionUid, int userId) {
	QbCollection collection = qbDAO.find(QbCollection.class, collectionUid);
	if (collection.getUserId() == null || collection.isPersonal()) {
	    return null;
	}

	List<Organisation> result = new ArrayList<>();
	Map<Integer, Set<Integer>> roles = userManagementService.getRolesForUser(userId);
	for (Entry<Integer, Set<Integer>> roleEntry : roles.entrySet()) {
	    Integer organisationId = roleEntry.getKey();
	    for (Organisation organisation : collection.getOrganisations()) {
		if (organisation.getOrganisationId().equals(organisationId)) {
		    organisationId = null;
		    break;
		}
	    }
	    if (organisationId != null && roleEntry.getValue().contains(Role.ROLE_AUTHOR)) {
		Organisation organisation = qbDAO.find(Organisation.class, organisationId);
		result.add(organisation);
	    }
	}

	return result;
    }

    @Override
    public void releaseFromCache(Object object) {
	qbDAO.releaseFromCache(object);
    }

    @Override
    public List<QbCollection> getUserCollections(int userId) {
	List<QbCollection> collections = new LinkedList<>();

	QbCollection privateCollection = getUserPrivateCollection(userId);
	collections.add(privateCollection);

	collections.addAll(getUserOwnCollections(userId));

	QbCollection publicCollection = getPublicCollection();
	collections.add(publicCollection);

	return collections;
    }

    @Override
    public QbCollection getCollection(long collectionUid) {
	return qbDAO.find(QbCollection.class, collectionUid);
    }

    @Override
    public int getCountQuestionActivitiesByUid(long qbQuestionUid) {
	return qbDAO.getCountQuestionActivitiesByUid(qbQuestionUid);
    }

    @Override
    public int getCountQuestionActivitiesByQuestionId(int qbQuestionId) {
	return qbDAO.getCountQuestionActivitiesByQuestionId(qbQuestionId);
    }

    @Override
    public void changeCollectionName(long collectionUid, String name) {
	QbCollection collection = getCollection(collectionUid);
	collection.setName(name);
	qbDAO.update(collection);
    }

    @Override
    public boolean isQuestionInUserCollection(int qbQuestionId, int userId) {
	return qbDAO.isQuestionInUserCollection(userId, qbQuestionId);
    }

    @Override
    public boolean isQuestionInPublicCollection(int qbQuestionId) {
	return qbDAO.isQuestionInPublicCollection(qbQuestionId);
    }

    /**
     * Cascades in QbToolQuestion, QbQuestion and QbOptions do not seem to work on insert.
     * New QbQuestions need to be saved step by step.
     */
    @Override
    public void insertQuestion(QbQuestion qbQuestion) {
	// question identification may be overlapping with existing data
	// it is a new question, so reset it
	qbQuestion.setQuestionId(generateNextQuestionId());
	qbQuestion.setVersion(1);

	if (qbQuestion.getContentFolderId() == null) {
	    qbQuestion.setContentFolderId(FileUtil.generateUniqueContentFolderID());
	}

	Collection<QbOption> qbOptions = qbQuestion.getQbOptions() == null ? null
		: new ArrayList<>(qbQuestion.getQbOptions());
	if (qbOptions != null) {
	    qbQuestion.getQbOptions().clear();
	}

	Collection<QbQuestionUnit> units = qbQuestion.getUnits() == null ? null
		: new ArrayList<>(qbQuestion.getUnits());
	if (units != null) {
	    qbQuestion.getUnits().clear();
	}

	qbDAO.insert(qbQuestion);

	if (units != null) {
	    qbQuestion.getUnits().addAll(units);
	    for (QbQuestionUnit unit : units) {
		unit.setQbQuestion(qbQuestion);
		qbDAO.insert(unit);
	    }
	}

	if (qbOptions != null) {
	    qbQuestion.getQbOptions().addAll(qbOptions);
	    for (QbOption qbOption : qbOptions) {
		qbOption.setQbQuestion(qbQuestion);
		qbDAO.insert(qbOption);
	    }
	}

	if (log.isDebugEnabled()) {
	    log.debug("Created a new QB question with UID: " + qbQuestion.getUid());
	}
    }

    /**
     * When exporting a LD, QbQuestion's server-specific detail need not be exported
     */
    @Override
    public void prepareQuestionForExport(QbQuestion qbQuestion) {
	qbQuestion.clearID();
	releaseFromCache(qbQuestion);
	qbQuestion.setQuestionId(null);
	qbQuestion.setVersion(null);
	// use plain Java collections instead of Hibernate ones, so XML is more simple
	qbQuestion.setQbOptions(new ArrayList<>(qbQuestion.getQbOptions()));
	qbQuestion.setUnits(new ArrayList<>(qbQuestion.getUnits()));
    }

    @Override
    public int mergeQuestions(long sourceQbQuestionUid, long targetQbQuestionUid) {
	QbQuestion sourceQuestion = getQuestionByUid(sourceQbQuestionUid);
	QbQuestion targetQuestion = getQuestionByUid(targetQbQuestionUid);

	if (sourceQuestion == null) {
	    throw new InvalidParameterException("Source question does not exist");
	}
	if (targetQuestion == null) {
	    throw new InvalidParameterException("Target question does not exist");
	}

	if (!sourceQuestion.getType().equals(targetQuestion.getType())) {
	    throw new InvalidParameterException("Source question type is different to target question type");
	}

	if (sourceQuestion.getQbOptions().size() != targetQuestion.getQbOptions().size()) {
	    throw new InvalidParameterException("Number of options in source and target questions does not match");
	}

	int answersChanged = qbDAO.mergeQuestions(sourceQbQuestionUid, targetQbQuestionUid);
	qbDAO.deleteById(QbQuestion.class, sourceQbQuestionUid);

	logEventService.logEvent(LogEvent.TYPE_QUESTIONS_MERGED, QbService.getUserId(), null, null, null,
		new StringBuilder("Question UID ").append(sourceQbQuestionUid).append(" merged into question UID ")
			.append(targetQbQuestionUid).toString());
	return answersChanged;
    }

    @Override
    public boolean isQuestionDefaultInTool(long qbQuestionUid, String toolSignature) {
	long defaultContentId = toolService.getToolDefaultContentIdBySignature(toolSignature);
	Collection<QbQuestion> qbQuestions = qbDAO.getQuestionsByToolContentId(defaultContentId);
	return qbQuestions.stream().anyMatch(q -> q.getUid().equals(qbQuestionUid));
    }

    @Override
    public Collection<ToolContent> getQuestionActivities(long qbQuestionUid, Collection<Long> toolContentIds) {
	return qbDAO.getQuestionActivities(qbQuestionUid, toolContentIds);
    }

    @Override
    public void replaceQuestionInToolActivities(Collection<Long> toolContentIds, long oldQbQuestionUid,
	    long newQbQuestionUid) {
	for (Long toolContentId : toolContentIds) {
	    ToolContent toolContent = qbDAO.findByProperty(ToolContent.class, "toolContentId", toolContentId).get(0);
	    Object toolService = lamsCoreToolService.findToolService(toolContent.getTool());
	    if (toolService instanceof IQbToolService) {
		try {
		    ((IQbToolService) toolService).replaceQuestion(toolContentId, oldQbQuestionUid, newQbQuestionUid);
		} catch (UnsupportedOperationException e) {
		    log.warn("Could not replace a question for activity with tool content ID " + toolContentId
			    + " as the tool does not support question replacement");
		}
	    }
	}
    }

    @Override
    public void fillVersionMap(QbQuestion qbQuestion) {
	List<QbQuestion> allVersions = getQuestionsByQuestionId(qbQuestion.getQuestionId());
	Map<Integer, Long> versionMap = new TreeMap<>();
	for (QbQuestion questionVersion : allVersions) {
	    versionMap.put(questionVersion.getVersion(), questionVersion.getUid());
	}
	qbQuestion.setVersionMap(versionMap);
    }

    /**
     * Allocate learner's answer into one of the available answer groups.
     *
     * @return if present, it contains optionUid of the option group containing duplicate (added there presumably by
     *         another teacher working in parallel)
     */
    @Override
    public Long allocateVSAnswerToOption(Long toolQuestionUid, Long targetOptionUid, Long previousOptionUid,
	    String answer) {
	QbToolQuestion toolQuestion = qbDAO.find(QbToolQuestion.class, toolQuestionUid);
	QbQuestion qbQuestion = toolQuestion.getQbQuestion();
	boolean isExactMatch = qbQuestion.isExactMatch();

	String normalisedAnswer = QbUtils.normaliseVSAnswer(answer, isExactMatch);
	if (normalisedAnswer == null && previousOptionUid.equals(-1L)) {
	    return null;
	}
	answer = answer.strip();

	Long qbQuestionUid = qbQuestion.getUid();
	boolean isQuestionCaseSensitive = qbQuestion.isCaseSensitive();

	QbOption previousOption = null;
	QbOption targetOption = null;

	// look for source and target options
	for (QbOption option : qbQuestion.getQbOptions()) {
	    if (previousOptionUid.equals(-1L)) {
		// new allocation, check if the answer was not allocated anywhere already
		String name = option.getName();
		boolean isAnswerAllocated = QbUtils.isVSAnswerAllocated(name, normalisedAnswer, isQuestionCaseSensitive,
			isExactMatch);
		if (isAnswerAllocated) {
		    return option.getUid();
		}
	    } else if (previousOption == null && option.getUid().equals(previousOptionUid)) {
		previousOption = option;
	    }
	    if (targetOption == null && !targetOptionUid.equals(-1L) && option.getUid().equals(targetOptionUid)) {
		targetOption = option;
	    }
	}

	if (!targetOptionUid.equals(-1L) && targetOption == null) {
	    // front end provided incorrect target option UID
	    log.error("Target option with UID " + targetOptionUid + " was not found in question with UID "
		    + qbQuestionUid + " to allocate answer " + answer);
	    return null;
	}

	// remove from already allocated option
	if (previousOption != null) {
	    String name = previousOption.getName();
	    String[] alternatives = name.split(QbUtils.VSA_ANSWER_DELIMITER);

	    Set<String> nameWithoutUserAnswer = new LinkedHashSet<>(List.of(alternatives));
	    nameWithoutUserAnswer.remove(answer);
	    name = nameWithoutUserAnswer.isEmpty() ? ""
		    : nameWithoutUserAnswer.stream().filter(a -> QbUtils.normaliseVSAnswer(a, isExactMatch) != null)
			    .collect(Collectors.joining(QbUtils.VSA_ANSWER_DELIMITER));
	    previousOption.setName(name);
	    qbDAO.update(previousOption);
	    qbDAO.flush();

	    if (log.isInfoEnabled()) {
		log.info("Removed VS answer \"" + answer + "\" from option " + previousOptionUid + " in question "
			+ qbQuestionUid);
	    }
	}

	if (targetOption != null) {
	    String name = targetOption.getName();

	    boolean isAnswerAllocated = QbUtils.isVSAnswerAllocated(name, normalisedAnswer, isQuestionCaseSensitive,
		    isExactMatch);
	    if (isAnswerAllocated) {
		// the answer has been already allocated to the target option
		return targetOptionUid;
	    }

	    // append new answer to option
	    name += (StringUtils.isBlank(name) ? "" : QbUtils.VSA_ANSWER_DELIMITER) + answer;
	    targetOption.setName(name);
	    qbDAO.update(targetOption);
	    qbDAO.flush();

	    if (log.isInfoEnabled()) {
		log.info("Allocated VS  answer \"" + answer + "\" to option " + targetOptionUid + " in question "
			+ qbQuestionUid);
	    }
	}

	return null;
    }

    /**
     * Extract web form content to QB question.
     *
     * BE CAREFUL: This method will copy necessary info from request form to an old or new AssessmentQuestion
     * instance. It gets all info EXCEPT AssessmentQuestion.createDate, which need be set when
     * persisting this assessment Question.
     *
     * @return qbQuestionModified
     */
    @Override
    public int extractFormToQbQuestion(QbQuestion qbQuestion, QbQuestionForm form, HttpServletRequest request) {
	QbQuestion oldQuestion = qbQuestion.clone();
	// evict everything manually as we do not use DTOs, just real entities
	// without eviction changes would be saved immediately into DB
	releaseFromCache(oldQuestion);
	releaseFromCache(qbQuestion);

	qbQuestion.setName(form.getTitle().strip());
	qbQuestion.setDescription(form.getDescription().strip());

	if (!form.isAuthoringRestricted()) {
	    qbQuestion.setMaxMark(form.getMaxMark());
	}
	qbQuestion.setFeedback(form.getFeedback());
	qbQuestion.setContentFolderId(form.getContentFolderID());

	Integer type = form.getQuestionType();
	if (type == QbQuestion.TYPE_MULTIPLE_CHOICE) {
	    qbQuestion.setMultipleAnswersAllowed(form.isMultipleAnswersAllowed());
	    boolean incorrectAnswerNullifiesMark = form.isMultipleAnswersAllowed()
		    ? form.isIncorrectAnswerNullifiesMark()
		    : false;
	    qbQuestion.setIncorrectAnswerNullifiesMark(incorrectAnswerNullifiesMark);
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setShuffle(form.isShuffle());
	    qbQuestion.setPrefixAnswersWithLetters(form.isPrefixAnswersWithLetters());
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnPartiallyCorrect(form.getFeedbackOnPartiallyCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());

	} else if ((type == QbQuestion.TYPE_MATCHING_PAIRS)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setShuffle(form.isShuffle());

	} else if ((type == QbQuestion.TYPE_VERY_SHORT_ANSWERS)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setCaseSensitive(form.isCaseSensitive());
	    qbQuestion.setExactMatch(form.isExactMatch());
	    qbQuestion.setAutocompleteEnabled(form.isAutocompleteEnabled());

	} else if ((type == QbQuestion.TYPE_NUMERICAL)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));

	} else if ((type == QbQuestion.TYPE_TRUE_FALSE)) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setCorrectAnswer(form.isCorrectAnswer());
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());

	} else if ((type == QbQuestion.TYPE_ESSAY)) {
	    qbQuestion.setAllowRichEditor(form.isAllowRichEditor());
	    qbQuestion.setMaxWordsLimit(form.getMaxWordsLimit());
	    qbQuestion.setMinWordsLimit(form.getMinWordsLimit());
	    qbQuestion.setCodeStyle(
		    form.getCodeStyle() == null || form.getCodeStyle().equals(0) ? null : form.getCodeStyle());

	} else if (type == QbQuestion.TYPE_ORDERING) {
	    qbQuestion.setPenaltyFactor(Float.parseFloat(form.getPenaltyFactor()));
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());

	} else if (type == QbQuestion.TYPE_MARK_HEDGING) {
	    qbQuestion.setShuffle(form.isShuffle());
	    qbQuestion.setFeedbackOnCorrect(form.getFeedbackOnCorrect());
	    qbQuestion.setFeedbackOnPartiallyCorrect(form.getFeedbackOnPartiallyCorrect());
	    qbQuestion.setFeedbackOnIncorrect(form.getFeedbackOnIncorrect());
	    qbQuestion.setHedgingJustificationEnabled(form.isHedgingJustificationEnabled());
	}

	// set options
	if ((type == QbQuestion.TYPE_MULTIPLE_CHOICE) || (type == QbQuestion.TYPE_ORDERING)
		|| (type == QbQuestion.TYPE_MATCHING_PAIRS) || (type == QbQuestion.TYPE_VERY_SHORT_ANSWERS)
		|| (type == QbQuestion.TYPE_NUMERICAL) || (type == QbQuestion.TYPE_MARK_HEDGING)) {
	    Set<QbOption> optionList = getOptionsFromRequest(request, true);
	    List<QbOption> options = new ArrayList<>();
	    int displayOrder = 0;
	    for (QbOption option : optionList) {
		option.setDisplayOrder(displayOrder++);
		options.add(option);
	    }
	    qbQuestion.setQbOptions(options);
	}
	// set units
	if (type == QbQuestion.TYPE_NUMERICAL) {
	    Set<QbQuestionUnit> unitList = getUnitsFromRequest(request, true);
	    qbQuestion.getUnits().clear();
	    int displayOrder = 0;
	    for (QbQuestionUnit unit : unitList) {
		unit.setQbQuestion(qbQuestion);
		unit.setDisplayOrder(displayOrder++);
		qbQuestion.getUnits().add(unit);
	    }
	}

	return qbQuestion.isQbQuestionModified(oldQuestion);
    }

    /**
     * Get answer options from <code>HttpRequest</code>
     *
     * @param request
     * @param isForSaving
     *            whether the blank options will be preserved or not
     */
    @Override
    public TreeSet<QbOption> getOptionsFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, QbConstants.ATTR_OPTION_LIST);

	int count = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_COUNT));
	int questionType = WebUtil.readIntParam(request, QbConstants.ATTR_QUESTION_TYPE);
	Integer correctOptionIndex = (paramMap.get(QbConstants.ATTR_OPTION_CORRECT) == null) ? null
		: NumberUtils.toInt(paramMap.get(QbConstants.ATTR_OPTION_CORRECT));

	TreeSet<QbOption> optionList = new TreeSet<>();
	for (int i = 0; i < count; i++) {

	    String displayOrder = paramMap.get(QbConstants.ATTR_OPTION_DISPLAY_ORDER_PREFIX + i);
	    //displayOrder is null, in case this item was removed using Remove button
	    if (displayOrder == null) {
		continue;
	    }

	    QbOption option = null;
	    String uidStr = paramMap.get(QbConstants.ATTR_OPTION_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		option = getOptionByUid(uid);

	    } else {
		option = new QbOption();
	    }
	    option.setDisplayOrder(NumberUtils.toInt(displayOrder));

	    if ((questionType == QbQuestion.TYPE_MULTIPLE_CHOICE)
		    || (questionType == QbQuestion.TYPE_VERY_SHORT_ANSWERS)) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if (name == null && isForSaving && !(questionType == QbQuestion.TYPE_VERY_SHORT_ANSWERS && i < 2)) {
		    continue;
		}

		option.setName(name);
		float maxMark = Float.valueOf(paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i));
		option.setMaxMark(maxMark);
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));

	    } else if (questionType == QbQuestion.TYPE_MATCHING_PAIRS) {
		String matchingPair = paramMap.get(QbConstants.ATTR_MATCHING_PAIR_PREFIX + i);
		if ((matchingPair == null) && isForSaving) {
		    continue;
		}

		option.setName(paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i));
		option.setMatchingPair(matchingPair);

	    } else if (questionType == QbQuestion.TYPE_NUMERICAL) {
		String numericalOptionStr = paramMap.get(QbConstants.ATTR_NUMERICAL_OPTION_PREFIX + i);
		String acceptedErrorStr = paramMap.get(QbConstants.ATTR_OPTION_ACCEPTED_ERROR_PREFIX + i);
		String maxMarkStr = paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i);
		if (numericalOptionStr.equals("0.0") && numericalOptionStr.equals("0.0") && maxMarkStr.equals("0.0")
			&& isForSaving) {
		    continue;
		}

		try {
		    float numericalOption = Float.valueOf(numericalOptionStr);
		    option.setNumericalOption(numericalOption);
		} catch (Exception e) {
		    option.setNumericalOption(0);
		}
		try {
		    float acceptedError = Float.valueOf(acceptedErrorStr);
		    option.setAcceptedError(acceptedError);
		} catch (Exception e) {
		    option.setAcceptedError(0);
		}
		float maxMark = Float.valueOf(paramMap.get(QbConstants.ATTR_OPTION_MAX_MARK_PREFIX + i));
		option.setMaxMark(maxMark);
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));

	    } else if (questionType == QbQuestion.TYPE_ORDERING) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);

	    } else if (questionType == QbQuestion.TYPE_MARK_HEDGING) {
		String name = paramMap.get(QbConstants.ATTR_OPTION_NAME_PREFIX + i);
		if ((name == null) && isForSaving) {
		    continue;
		}

		option.setName(name);
		if ((correctOptionIndex != null) && correctOptionIndex.equals(Integer.valueOf(displayOrder))) {
		    option.setCorrect(true);
		}
		option.setFeedback(paramMap.get(QbConstants.ATTR_OPTION_FEEDBACK_PREFIX + i));
	    }

	    optionList.add(option);
	}

//	//in case of VSA make sure it has 2 option groups, one of which having 0 maxMark
//	if (questionType == QbQuestion.TYPE_VERY_SHORT_ANSWERS && optionList.size() == 1) {
//	    QbOption option = new QbOption();
//	    option.setDisplayOrder(1);
//	    option.setName("");
//	    option.setMaxMark(0);
//	    option.setFeedback("");
//	    optionList.add(option);
//	}

	return optionList;
    }

    /**
     * Get units from <code>HttpRequest</code>
     */
    @Override
    public TreeSet<QbQuestionUnit> getUnitsFromRequest(HttpServletRequest request, boolean isForSaving) {
	Map<String, String> paramMap = splitRequestParameter(request, QbConstants.ATTR_UNIT_LIST);

	int count = NumberUtils.toInt(paramMap.get(QbConstants.ATTR_UNIT_COUNT));
	TreeSet<QbQuestionUnit> unitList = new TreeSet<>();
	for (int i = 0; i < count; i++) {
	    String name = paramMap.get(QbConstants.ATTR_UNIT_NAME_PREFIX + i);
	    if (StringUtils.isBlank(name) && isForSaving) {
		continue;
	    }

	    QbQuestionUnit unit = null;
	    String uidStr = paramMap.get(QbConstants.ATTR_UNIT_UID_PREFIX + i);
	    if (uidStr != null) {
		Long uid = NumberUtils.toLong(uidStr);
		unit = getQuestionUnitByUid(uid);

	    } else {
		unit = new QbQuestionUnit();
	    }
	    String displayOrder = paramMap.get(QbConstants.ATTR_UNIT_DISPLAY_ORDER_PREFIX + i);
	    unit.setDisplayOrder(NumberUtils.toInt(displayOrder));
	    unit.setName(name);
	    float multiplier = Float.valueOf(paramMap.get(QbConstants.ATTR_UNIT_MULTIPLIER_PREFIX + i));
	    unit.setMultiplier(multiplier);
	    unitList.add(unit);
	}

	return unitList;
    }

    private Map<String, String> splitRequestParameter(HttpServletRequest request, String parameterName) {
	String list = request.getParameter(parameterName);
	if (list == null) {
	    return null;
	}

	String[] params = list.split("&");
	Map<String, String> paramMap = new HashMap<>();
	String[] pair;
	for (String item : params) {
	    pair = item.split("=");
	    if ((pair == null) || (pair.length != 2)) {
		continue;
	    }
	    try {
		paramMap.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		log.error("Error occurs when decode instruction string:" + e.toString());
	    }
	}
	return paramMap;
    }

    private static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    public void setQbDAO(IQbDAO qbDAO) {
	this.qbDAO = qbDAO;
    }

    public void setGradebookService(IGradebookService gradebookService) {
	this.gradebookService = gradebookService;
    }

    public void setLamsCoreToolService(ILamsCoreToolService lamsCoreToolService) {
	this.lamsCoreToolService = lamsCoreToolService;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setLogEventService(ILogEventService logEventService) {
	this.logEventService = logEventService;
    }

    public void setToolService(ILamsToolService toolService) {
	this.toolService = toolService;
    }
}