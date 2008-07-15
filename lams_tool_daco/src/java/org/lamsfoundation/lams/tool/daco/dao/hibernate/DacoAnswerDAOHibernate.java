package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerDAO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.util.QuestionSummary;

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

	public List<QuestionSummary> getSummaries(Long contentUid, Long userUid, List<QuestionSummary> summaries) {

		List<Object[]> result = getHibernateTemplate().findByNamedQueryAndNamedParam("userNumberQuery",
				new String[] { "contentUid", "userUid", "numberQuestionType" },
				new Object[] { contentUid, userUid, DacoConstants.QUESTION_TYPE_NUMBER });

		for (Object[] objectRow : result) {
			addNumberSummary(summaries, objectRow, true);
		}

		result = getHibernateTemplate().findByNamedQueryAndNamedParam("allNumberQuery",
				new String[] { "contentUid", "numberQuestionType" },
				new Object[] { contentUid, DacoConstants.QUESTION_TYPE_NUMBER });

		for (Object[] objectRow : result) {
			addNumberSummary(summaries, objectRow, false);
		}

		result = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"userAnswerEnumerationQuery",
				new String[] { "contentUid", "userUid", "numberQuestionType", "radioQuestionType", "dropdownQuestionType",
						"checkboxQuestionType" },
				new Object[] { contentUid, userUid, DacoConstants.QUESTION_TYPE_NUMBER, DacoConstants.QUESTION_TYPE_RADIO,
						DacoConstants.QUESTION_TYPE_DROPDOWN, DacoConstants.QUESTION_TYPE_CHECKBOX });

		for (Object[] objectRow : result) {
			addAnswerEnumerationSummary(summaries, objectRow, true);
		}

		result = getHibernateTemplate().findByNamedQueryAndNamedParam(
				"allAnswerEnumerationQuery",
				new String[] { "contentUid", "numberQuestionType", "radioQuestionType", "dropdownQuestionType",
						"checkboxQuestionType" },
				new Object[] { contentUid, DacoConstants.QUESTION_TYPE_NUMBER, DacoConstants.QUESTION_TYPE_RADIO,
						DacoConstants.QUESTION_TYPE_DROPDOWN, DacoConstants.QUESTION_TYPE_CHECKBOX });

		for (Object[] objectRow : result) {
			addAnswerEnumerationSummary(summaries, objectRow, false);
		}
		return summaries;
	}

	private void addAnswerEnumerationSummary(List<QuestionSummary> summaries, Object[] objectRow, boolean isUserSummary) {
		String[] row = rewriteRow(objectRow);
		long currentUid = Long.parseLong(row[DacoConstants.QUESTION_DB_SUMMARY_QUESTION_UID]);
		QuestionSummary summary = summaries.get(findQuestionSequenceNumber(currentUid, summaries));
		String[] columns = new String[DacoConstants.QUESTION_SUMMARY_COLUMN_COUNT];
		columns[DacoConstants.QUESTION_SUMMARY_ANSWER] = row[DacoConstants.QUESTION_DB_SUMMARY_ANSWER];
		columns[DacoConstants.QUESTION_SUMMARY_AVERAGE] = row[DacoConstants.QUESTION_DB_SUMMARY_AVERAGE];
		columns[DacoConstants.QUESTION_SUMMARY_COUNT] = row[DacoConstants.QUESTION_DB_SUMMARY_COUNT];

		if (Short.parseShort(row[DacoConstants.QUESTION_DB_SUMMARY_QUESTION_TYPE]) == DacoConstants.QUESTION_TYPE_NUMBER) {
			String[] currentColumns = null;
			int answerIndex = 1;
			do {
				currentColumns = isUserSummary ? summary.getUserSummaryRow(answerIndex) : summary.getAllSummaryRow(answerIndex);
				if (currentColumns == null) {
					if (isUserSummary) {
						summary.addUserSummaryRow(answerIndex, columns);
					}
					else {
						summary.addAllSummaryRow(answerIndex, columns);
					}
					currentColumns = null;
				}
				answerIndex++;
			}
			while (currentColumns != null);
		}
		else {
			String answer = row[DacoConstants.QUESTION_DB_SUMMARY_ANSWER];
			int answerIndex = Integer.parseInt(answer) - 1;
			if (isUserSummary) {
				summary.addUserSummaryRow(answerIndex, columns);
			}
			else {
				summary.addAllSummaryRow(answerIndex, columns);
			}
		}
	}

	private void addNumberSummary(List<QuestionSummary> summaries, Object[] objectRow, boolean isUserSummary) {
		String[] row = rewriteRow(objectRow);
		long currentUid = Long.parseLong(row[DacoConstants.QUESTION_DB_SUMMARY_QUESTION_UID]);
		QuestionSummary summary = summaries.get(findQuestionSequenceNumber(currentUid, summaries));
		String[] columns = isUserSummary ? summary.getUserSummaryRow(0) : summary.getAllSummaryRow(0);
		columns[DacoConstants.QUESTION_SUMMARY_SUM] = row[DacoConstants.QUESTION_DB_SUMMARY_SUM];
		columns[DacoConstants.QUESTION_SUMMARY_AVERAGE] = row[DacoConstants.QUESTION_DB_SUMMARY_AVERAGE];
	}

	private int findQuestionSequenceNumber(Long uid, List<QuestionSummary> summaries) {
		for (int summaryNumber = 0; summaryNumber < summaries.size(); summaryNumber++) {
			QuestionSummary summary = summaries.get(summaryNumber);
			if (summary != null && uid.equals(summary.getQuestionUid())) {
				return summaryNumber;
			}
		}
		return -1;
	}

	private String[] rewriteRow(Object[] objectRow) {
		String[] row = new String[objectRow.length];
		for (int fieldNumber = 0; fieldNumber < objectRow.length; fieldNumber++) {
			row[fieldNumber] = (String) objectRow[fieldNumber];
		}
		return row;
	}
}