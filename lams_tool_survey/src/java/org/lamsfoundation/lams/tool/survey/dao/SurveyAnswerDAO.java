package org.lamsfoundation.lams.tool.survey.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;

public interface SurveyAnswerDAO extends DAO {

	SurveyAnswer getAnswer(Long uid, Long userUid);
	
	/**
	 * Get all answers from same session for same question. 
	 * @param sessionId
	 * @param questionUid
	 * @return
	 */
	List<SurveyAnswer> getSessionAnswer(Long sessionId,Long questionUid);

}
