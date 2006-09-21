package org.lamsfoundation.lams.tool.survey.dao;

import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;

public interface SurveyAnswerDAO extends DAO {

	SurveyAnswer getAnswer(Long uid, Long userUid);

}
