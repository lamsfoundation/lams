package org.lamsfoundation.lams.tool.survey.dao.hibernate;

import org.lamsfoundation.lams.tool.survey.dao.SurveyQuestionDAO;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;

public class SurveyQuestionDAOHibernate  extends BaseDAOHibernate implements SurveyQuestionDAO {

	public SurveyQuestion getByUid(Long questionUid) {
		
		return (SurveyQuestion) this.getObject(SurveyQuestion.class, questionUid);
	}


}
