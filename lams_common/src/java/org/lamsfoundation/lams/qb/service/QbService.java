package org.lamsfoundation.lams.qb.service;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.dto.QbStatsActivityDTO;
import org.lamsfoundation.lams.qb.dto.QbStatsDTO;
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
    public List<QbQuestion> getPagedQuestions(String questionTypes, String collectionUids, int page, int size,
	    String sortBy, String sortOrder, String searchString) {
	return qbDAO.getPagedQuestions(questionTypes, collectionUids, page, size, sortBy, sortOrder, searchString);
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
	    userLessonGrades = userLessonGrades.stream().filter(g -> activityAnswers.containsKey(g.getLearner().getUserId()))
		    .collect(Collectors.toList());
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
	releaseFromCache(qbQuestion);
	qbQuestion.clearID();
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