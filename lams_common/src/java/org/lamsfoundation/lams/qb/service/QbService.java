package org.lamsfoundation.lams.qb.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.gradebook.GradebookUserLesson;
import org.lamsfoundation.lams.gradebook.service.IGradebookService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.dto.QbStatsActivityDTO;
import org.lamsfoundation.lams.qb.dto.QbStatsDTO;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class QbService implements IQbService {
    private static Logger log = Logger.getLogger(QbService.class);

    private IQbDAO qbDAO;

    private IGradebookService gradebookService;

    private ILamsCoreToolService lamsCoreToolService;

    @Override
    public QbQuestion getQbQuestionByUid(Long qbQuestionUid) {
	return qbDAO.getQbQuestionByUid(qbQuestionUid);
    }

    @Override
    public List<QbQuestion> getQbQuestionsByQuestionId(Integer questionId) {
	return qbDAO.getQbQuestionsByQuestionId(questionId);
    }

    @Override
    public int getMaxQuestionId() {
	return qbDAO.getMaxQuestionId();
    }

    @Override
    public int getMaxQuestionVersion(Integer qbQuestionId) {
	return qbDAO.getMaxQuestionVersion(qbQuestionId);
    }

    @Override
    public List<QbQuestion> getPagedQbQuestions(Integer questionType, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return qbDAO.getPagedQbQuestions(questionType, page, size, sortBy, sortOrder, searchString);
    }

    @Override
    public int getCountQbQuestions(Integer questionType, String searchString) {
	return qbDAO.getCountQbQuestions(questionType, searchString);
    }

    @Override
    public QbStatsDTO getQbQuestionStats(long qbQuestionUid) {
	QbStatsDTO stats = new QbStatsDTO();
	QbQuestion qbQuestion = (QbQuestion) qbDAO.find(QbQuestion.class, qbQuestionUid);
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
	Map<Long, Long> answersRaw = qbDAO.getAnswerStatsForQbQuestion(qbQuestionUid);
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
	    String name = (answerIndex + 1) + ". "
		    + (option.getName().length() > 30 ? option.getName().substring(0, 30) + "..." : option.getName());
	    name = WebUtil.removeHTMLtags(name);
	    answerJSON.put("name", name);
	    answerJSON.put("value", value);
	    answersJSON.add(answerJSON);
	}
	stats.setAnswersPercent(answerPercent);
	stats.setAnswersJSON(answersJSON.toString());
	return stats;
    }

    @SuppressWarnings("unchecked")
    @Override
    public QbStatsActivityDTO getActivityStatsByContentId(Long toolContentId, Long qbQuestionUid) {
	Activity activity = ((List<Activity>) qbDAO.findByProperty(ToolActivity.class, "toolContentId", toolContentId))
		.get(0);
	return getActivityStats(activity.getActivityId(), qbQuestionUid);
    }

    @Override
    public QbStatsActivityDTO getActivityStats(Long activityId, Long qbQuestionUid) {
	QbQuestion qbQuestion = (QbQuestion) qbDAO.find(QbQuestion.class, qbQuestionUid);
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
	ToolActivity activity = (ToolActivity) qbDAO.find(ToolActivity.class, activityId);
	LearningDesign learningDesign = activity.getLearningDesign();
	Lesson lesson = learningDesign.getLessons().iterator().next();
	Long lessonId = lesson.getLessonId();
	List<GradebookUserLesson> userLessonGrades = gradebookService.getGradebookUserLesson(lessonId);
	int participantCount = userLessonGrades.size();

	QbStatsActivityDTO activityDTO = new QbStatsActivityDTO();
	activityDTO.setActivity(activity);
	activityDTO.setParticipantCount(participantCount);

	String monitorUrl = "/lams/" + lamsCoreToolService.getToolMonitoringURL(lessonId, activity)
		+ "&contentFolderID=" + learningDesign.getContentFolderID();
	activityDTO.setMonitorURL(monitorUrl);

	// if there is only 1 participant, there is no point in calculating question indexes
	if (participantCount >= Configuration.getAsInt(ConfigurationKeys.QB_STATS_MIN_PARTICIPANTS)) {
	    // mapping of user ID -> option UID
	    Map<Integer, Long> activityAnswers = qbDAO.getAnswersForActivity(activity.getActivityId(), qbQuestionUid);
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
	    Collections.sort(userLessonGrades, (a, b) -> a.getMark().compareTo(b.getMark()));
	    // see how many learners should be in top/bottom 27% of the group
	    int groupCount = (int) Math.ceil(
		    Configuration.getAsInt(ConfigurationKeys.QB_STATS_MIN_PARTICIPANTS) / 100.0 * participantCount);

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
    public QbCollection getPublicCollection() {
	return (QbCollection) qbDAO.find(QbCollection.class, 1L);
    }

    @Override
    @SuppressWarnings("unchecked")
    public QbCollection getUserPrivateCollection(int userId) {
	Map<String, Object> map = new HashMap<>();
	map.put("userId", userId);
	map.put("personal", true);
	List<QbCollection> result = qbDAO.findByProperties(QbCollection.class, map);
	if (!result.isEmpty()) {
	    return result.get(0);
	}

	QbCollection collection = new QbCollection();
	collection.setName("Private questions");
	collection.setUserId(userId);
	collection.setPersonal(true);
	qbDAO.insert(collection);
	return collection;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<QbCollection> getUserCollections(int userId) {
	Map<String, Object> map = new HashMap<>();
	map.put("userId", userId);
	map.put("personal", false);
	return qbDAO.findByProperties(QbCollection.class, map);
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
    public int countCollectionQuestions(long collectionUid, String search) {
	return qbDAO.countCollectionQuestions(collectionUid, search);
    }

    @Override
    public QbCollection addCollection(int userId, String name) {
	QbCollection collection = new QbCollection();
	collection.setName(name);
	collection.setUserId(userId);
	qbDAO.insert(collection);
	return collection;
    }

    @Override
    public void removeCollection(long collectionUid) {
	qbDAO.deleteById(QbCollection.class, collectionUid);
    }

    @Override
    public Organisation shareCollection(QbCollection collection, int organisationId) {
	Organisation organisation = (Organisation) qbDAO.find(Organisation.class, organisationId);
	collection.getOrganisations().add(organisation);
	qbDAO.update(collection);
	return organisation;
    }

    @Override
    public void unshareCollection(QbCollection collection, int organisationId) {
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
    public void addQuestionToCollection(long collectionUid, long qbQuestionUid) {
	qbDAO.addCollectionQuestion(collectionUid, qbQuestionUid);
    }

    @Override
    public void removeQuestionFromCollection(long collectionUid, long qbQuestionUid) {
	qbDAO.removeCollectionQuestion(collectionUid, qbQuestionUid);
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
}