package org.lamsfoundation.lams.qb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.dto.QbStatsDTO;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class QbService implements IQbService {

    private IQbDAO qbDAO;
    
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
    public QbStatsDTO getStats(long qbQuestionUid) {
	QbStatsDTO stats = new QbStatsDTO();
	stats.setQuestion((QbQuestion) qbDAO.find(QbQuestion.class, qbQuestionUid));
	stats.setActivities(qbDAO.getQuestionActivities(qbQuestionUid));
	stats.setVersions(qbDAO.getQuestionVersions(qbQuestionUid));
	Map<Long, Long> answersRaw = qbDAO.getAnswerStats(qbQuestionUid);
	stats.setAnswersRaw(answersRaw);

	ArrayNode answersJSON = JsonNodeFactory.instance.arrayNode();
	double total = 0;
	for (Long answerCount : answersRaw.values()) {
	    total += answerCount;
	}
	Map<Long, Integer> answerPercent = new HashMap<>();
	List<QbOption> qbOptions = stats.getQuestion().getQbOptions();
	for (int answerIndex = 0; answerIndex < qbOptions.size(); answerIndex++) {
	    QbOption option = qbOptions.get(answerIndex);
	    Long answerCount = answersRaw.get(option.getUid());
	    int value = answerCount == null ? 0 : Long.valueOf(Math.round(answerCount / total * 100)).intValue();
	    answerPercent.put(option.getUid(), value);

	    ObjectNode answerJSON = JsonNodeFactory.instance.objectNode();
	    answerJSON.put("name", (answerIndex + 1) + ". "
		    + (option.getName().length() > 20 ? option.getName().substring(0, 20) + "..." : option.getName()));
	    answerJSON.put("value", value);
	    answersJSON.add(answerJSON);
	}
	stats.setAnswersPercent(answerPercent);
	stats.setAnswersJSON(answersJSON.toString());
	return stats;
    }

    public void setQbDAO(IQbDAO qbDAO) {
	this.qbDAO = qbDAO;
    }
}