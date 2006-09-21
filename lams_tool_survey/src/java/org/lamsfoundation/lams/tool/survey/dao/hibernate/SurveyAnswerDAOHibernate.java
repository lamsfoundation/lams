package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.survey.dao.SurveyAnswerDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;

public class SurveyAnswerDAOHibernate extends BaseDAOHibernate implements SurveyAnswerDAO {
private static final String GET_LEARNER_ANSWER = "from "+SurveyAnswer.class.getName()+" as a where a.surveyQuestion.uid=? and a.user.uid=?";
	
	public SurveyAnswer getAnswer(Long QuestionUid, Long userUid) {
		List list = getHibernateTemplate().find(GET_LEARNER_ANSWER,new Object[]{QuestionUid,userUid});
		if(list.size() > 0)
			return (SurveyAnswer) list.get(0);
		else
			return null;
	}

}
