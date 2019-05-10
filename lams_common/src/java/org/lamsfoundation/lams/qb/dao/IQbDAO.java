package org.lamsfoundation.lams.qb.dao;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public interface IQbDAO extends IBaseDAO {

    /**
     * @param qbQuestionUid
     * @return QbQuestion object with the specified uid
     */
    QbQuestion getQbQuestionByUid(Long qbQuestionUid);

    /**
     * @param questionId
     * @return questions sharing the same questionId
     */
    List<QbQuestion> getQbQuestionsByQuestionId(Integer questionId);

    // finds next question ID for Question Bank question
    int getMaxQuestionId();

    // finds next version for given question ID for Question Bank question
    int getMaxQuestionVersion(Integer qbQuestionId);

    List<ToolActivity> getQuestionActivities(long qbQuestionUid);

    List<QbQuestion> getQuestionVersions(long qbQuestionUid);

    Map<Long, Long> getAnswerStatsForQbQuestion(long qbQuestionUid);

    Map<Long, Long> geAnswerStatsForQbToolQuestion(long qbToolQuestionUid);

    Map<Long, Long> getAnswerStatsForActivity(long activityId);

    Map<String, Long> getBurningQuestions(long qbQuestionUid);

    List<QbQuestion> getPagedQbQuestions(Integer questionType, int page, int size, String sortBy, String sortOrder,
	    String searchString);

    int getCountQbQuestions(Integer questionType, String searchString);

}