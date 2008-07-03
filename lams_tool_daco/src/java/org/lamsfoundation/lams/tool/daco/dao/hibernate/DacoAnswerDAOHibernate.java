package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerDAO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;

public class DacoAnswerDAOHibernate extends BaseDAOHibernate implements DacoAnswerDAO {
	private static final String FIND_BY_USER_UID = "from " + DacoAnswer.class.getName()
			+ " as p where p.user.uid=? order by record_id,question_uid";
	private static final String FIND_BY_QUESTION_UID = "from " + DacoAnswer.class.getName()
			+ " as p where p.question.uid=? order by user.uid,record_id";

	private static final String FIND_BY_USER_UID_AND_DACO = "from " + DacoAnswer.class.getName()
			+ " as p where p.user.uid=? and p.question.uid in (?) order by record_id,question_uid";

	public List<List<DacoAnswer>> getRecordsByUserUid(Long userUid) {
		List<DacoAnswer> list = getHibernateTemplate().find(DacoAnswerDAOHibernate.FIND_BY_USER_UID, userUid);
		if (list == null || list.size() == 0) {
			return null;
		}
		int recordId = 1;
		List<List<DacoAnswer>> result = new LinkedList<List<DacoAnswer>>();
		List<DacoAnswer> record = new LinkedList<DacoAnswer>();
		for (DacoAnswer answer : list) {
			if (recordId != answer.getRecordId()) {
				result.add(record);
				record = new LinkedList<DacoAnswer>();
			}
			record.add(answer);
		}

		result.add(record);
		return result;
	}

	public List<DacoAnswer> getRecord(Long userUid, Integer recordId) {
		List list = getRecordsByUserUid(userUid);
		if (list == null) {
			return null;
		}
		return (List<DacoAnswer>) list.get(recordId);
	}

	public List<List<DacoAnswer>> getAnswersByQuestionUid(Long questionUid) {
		List<DacoAnswer> list = getHibernateTemplate().find(DacoAnswerDAOHibernate.FIND_BY_QUESTION_UID, questionUid);
		if (list == null || list.size() == 0) {
			return null;
		}
		int userUid = 1;
		List<List<DacoAnswer>> result = new LinkedList<List<DacoAnswer>>();
		List<DacoAnswer> user = new LinkedList<DacoAnswer>();
		for (DacoAnswer answer : list) {
			if (userUid != answer.getUser().getUid()) {
				result.add(user);
				user = new LinkedList<DacoAnswer>();
			}
			user.add(answer);
		}
		result.add(user);
		return result;
	}

	public List<List<DacoAnswer>> getRecordsByUserUidAndDaco(Long userUid, Daco daco) {
		StringBuilder set = new StringBuilder();
		List<List<DacoAnswer>> result = new LinkedList<List<DacoAnswer>>();
		for (DacoQuestion question : (Set<DacoQuestion>) daco.getDacoQuestions()) {
			set.append(question.getUid()).append(',');
		}
		set.deleteCharAt(set.length() - 1);
		List<DacoAnswer> list = getHibernateTemplate().find(DacoAnswerDAOHibernate.FIND_BY_USER_UID_AND_DACO,
				new Object[] { userUid, set.toString() });
		if (list != null && list.size() > 0) {
			int recordNumber = -1;
			List<DacoAnswer> record = null;
			for (DacoAnswer answer : list) {
				if (answer.getRecordId() != recordNumber) {
					if (record != null) {
						result.add(record);
					}
					record = new LinkedList<DacoAnswer>();
					recordNumber = answer.getRecordId();
				}
				record.add(answer);
			}
			result.add(record);
		}
		return result;
	}
}