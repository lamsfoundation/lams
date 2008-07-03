package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerOptionDAO;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswerOption;

public class DacoAnswerOptionDAOHibernate extends BaseDAOHibernate implements
		DacoAnswerOptionDAO {
	private static final String FIND_BY_QUESTION_UID = "from "
			+ DacoAnswerOption.class.getName()
			+ " as p where p.question_uid=? order by sequence_num";

	public List<String> getOptionsByQuestionUid(Long questionUid) {
		return getHibernateTemplate().find(FIND_BY_QUESTION_UID, questionUid);
	}
}