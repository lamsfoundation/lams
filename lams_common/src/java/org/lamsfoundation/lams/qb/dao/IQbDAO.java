package org.lamsfoundation.lams.qb.dao;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public interface IQbDAO extends IBaseDAO {
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
}