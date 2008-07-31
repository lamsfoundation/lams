package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.LinkedList;
import java.util.List;

import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerDAO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummarySingleAnswerDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;

public class DacoAnswerDAOHibernate extends BaseDAOHibernate implements DacoAnswerDAO {

	private static final String FIND_BY_USER_UID = "from " + DacoAnswer.class.getName()
			+ " as p where p.user.uid=? order by record_id,question_uid";
	private static final String FIND_BY_QUESTION_UID = "from " + DacoAnswer.class.getName()
			+ " as p where p.question.uid=? order by user.uid,record_id";

	private static final String FIND_BY_USER_UID_AND_DACO = "from " + DacoAnswer.class.getName()
			+ " as p where p.user.uid=? and p.question.uid in ([QUESTION_UID_LIST]) order by record_id,question_uid";

	private static final String FIND_USER_NUMBER_SUMMARY = "SELECT q.uid, "
			+ "SUM(a.answer),AVG(a.answer) FROM "
			+ DacoAnswer.class.getName()
			+ " AS a, "
			+ DacoQuestion.class.getName()
			+ " AS q WHERE a.question.uid=q.uid AND q.daco.uid=:contentUid AND a.user.uid=:userUid AND q.type=:numberQuestionType AND a.answer IS NOT NULL "
			+ "GROUP BY q.uid ORDER BY q.uid";

	private static final String FIND_ALL_NUMBER_SUMMARY = "SELECT q.uid, "
			+ "SUM(a.answer),AVG(a.answer) FROM "
			+ DacoAnswer.class.getName()
			+ " AS a, "
			+ DacoQuestion.class.getName()
			+ " AS q WHERE a.question.uid=q.uid AND q.daco.uid=:contentUid AND q.type=:numberQuestionType AND a.answer IS NOT NULL "
			+ "GROUP BY q.uid ORDER BY q.uid";

	private static final String FIND_USER_ANSWER_ENUMERATION_QUERY = "SELECT DISTINCT q.uid, a.answer, q.type, COUNT(*) FROM "
			+ DacoAnswer.class.getName() + " AS a, " + DacoQuestion.class.getName()
			+ " AS q WHERE a.question.uid=q.uid	AND q.daco.uid=:contentUid AND a.user.uid=:userUid "
			+ "AND q.type IN (:numberQuestionType,:radioQuestionType,:dropdownQuestionType,:checkboxQuestionType) "
			+ "AND a.answer IS NOT NULL	GROUP BY q.uid, a.answer ORDER BY q.uid,a.answer";

	private static final String FIND_ALL_ANSWER_ENUMERATION_QUERY = "SELECT DISTINCT q.uid, a.answer, q.type, COUNT(*) FROM "
			+ DacoAnswer.class.getName() + " AS a, " + DacoQuestion.class.getName()
			+ " AS q WHERE a.question.uid=q.uid	AND q.daco.uid=:contentUid "
			+ "AND q.type IN (:numberQuestionType,:radioQuestionType,:dropdownQuestionType,:checkboxQuestionType) "
			+ "AND a.answer IS NOT NULL	GROUP BY q.uid, a.answer ORDER BY q.uid,a.answer";

	private static final String FIND_ANSWER_COUNT = "SELECT COUNT(*) FROM " + DacoAnswer.class.getName()
			+ " AS a WHERE a.question.uid=? AND a.answer IS NOT NULL";

	private static final String FIND_TOTAL_RECORD_COUNT = "SELECT COUNT (DISTINCT a.recordId) FROM " + DacoAnswer.class.getName()
			+ " AS a WHERE a.question.daco.contentId=?";

	private static final String QUESTION_UID_LIST_MARKER = "[QUESTION_UID_LIST]";

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
		for (DacoQuestion question : daco.getDacoQuestions()) {
			set.append(question.getUid()).append(',');
		}
		set.deleteCharAt(set.length() - 1);
		String modifiedQuery = DacoAnswerDAOHibernate.FIND_BY_USER_UID_AND_DACO.replace(
				DacoAnswerDAOHibernate.QUESTION_UID_LIST_MARKER, set.toString());
		List<DacoAnswer> list = getHibernateTemplate().find(modifiedQuery, userUid);
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

	public List<QuestionSummaryDTO> getQuestionSummaries(Long contentUid, Long userUid, List<QuestionSummaryDTO> summaries) {

		List<Object[]> result = getHibernateTemplate().findByNamedParam(DacoAnswerDAOHibernate.FIND_USER_NUMBER_SUMMARY,
				new String[] { "contentUid", "userUid", "numberQuestionType" },
				new Object[] { contentUid, userUid, DacoConstants.QUESTION_TYPE_NUMBER });

		for (Object[] objectRow : result) {
			addNumberSummary(summaries, objectRow, true);
		}

		result = getHibernateTemplate().findByNamedParam(DacoAnswerDAOHibernate.FIND_ALL_NUMBER_SUMMARY,
				new String[] { "contentUid", "numberQuestionType" },
				new Object[] { contentUid, DacoConstants.QUESTION_TYPE_NUMBER });

		for (Object[] objectRow : result) {
			addNumberSummary(summaries, objectRow, false);
		}

		result = getHibernateTemplate().findByNamedParam(
				DacoAnswerDAOHibernate.FIND_USER_ANSWER_ENUMERATION_QUERY,
				new String[] { "contentUid", "userUid", "numberQuestionType", "radioQuestionType", "dropdownQuestionType",
						"checkboxQuestionType" },
				new Object[] { contentUid, userUid, DacoConstants.QUESTION_TYPE_NUMBER, DacoConstants.QUESTION_TYPE_RADIO,
						DacoConstants.QUESTION_TYPE_DROPDOWN, DacoConstants.QUESTION_TYPE_CHECKBOX });

		for (Object[] objectRow : result) {
			addAnswerEnumerationSummary(summaries, objectRow, true);
		}

		result = getHibernateTemplate().findByNamedParam(
				DacoAnswerDAOHibernate.FIND_ALL_ANSWER_ENUMERATION_QUERY,
				new String[] { "contentUid", "numberQuestionType", "radioQuestionType", "dropdownQuestionType",
						"checkboxQuestionType" },
				new Object[] { contentUid, DacoConstants.QUESTION_TYPE_NUMBER, DacoConstants.QUESTION_TYPE_RADIO,
						DacoConstants.QUESTION_TYPE_DROPDOWN, DacoConstants.QUESTION_TYPE_CHECKBOX });

		for (Object[] objectRow : result) {
			addAnswerEnumerationSummary(summaries, objectRow, false);
		}
		return summaries;
	}

	private void addAnswerEnumerationSummary(List<QuestionSummaryDTO> summaries, Object[] objectRow, boolean isUserSummary) {
		String[] row = rewriteRow(objectRow);
		long currentUid = Long.parseLong(row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_QUESTION_UID]);
		QuestionSummaryDTO summary = summaries.get(findQuestionSequenceNumber(currentUid, summaries));
		QuestionSummarySingleAnswerDTO singleAnswer = new QuestionSummarySingleAnswerDTO();
		singleAnswer.setAnswer(row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_ANSWER]);
		singleAnswer.setCount(row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_COUNT]);
		Integer answerCount = (Integer) getHibernateTemplate().find(DacoAnswerDAOHibernate.FIND_ANSWER_COUNT, currentUid).get(0);
		singleAnswer.setAverage(Math.round(Float.parseFloat(singleAnswer.getCount()) / answerCount * 100) + "%");

		if (Short.parseShort(row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_QUESTION_TYPE]) == DacoConstants.QUESTION_TYPE_NUMBER) {
			QuestionSummarySingleAnswerDTO currentSingleAnswer = null;
			int answerIndex = 1;
			do {
				currentSingleAnswer = isUserSummary ? summary.getUserSummarySingleAnswer(answerIndex) : summary
						.getAllSummarySingleAnswer(answerIndex);
				if (currentSingleAnswer == null) {
					if (isUserSummary) {
						summary.addUserSummarySingleAnswer(answerIndex, singleAnswer);
					}
					else {
						summary.addAllSummarySingleAnswer(answerIndex, singleAnswer);
					}
					currentSingleAnswer = null;
				}
				answerIndex++;
			}
			while (currentSingleAnswer != null);
		}
		else {
			int answerIndex = Integer.parseInt(singleAnswer.getAnswer()) - 1;
			if (isUserSummary) {
				summary.addUserSummarySingleAnswer(answerIndex, singleAnswer);
			}
			else {
				summary.addAllSummarySingleAnswer(answerIndex, singleAnswer);
			}
		}
	}

	private void addNumberSummary(List<QuestionSummaryDTO> summaries, Object[] objectRow, boolean isUserSummary) {
		String[] row = rewriteRow(objectRow);
		long currentUid = Long.parseLong(row[DacoConstants.QUESTION_DB_NUMBER_SUMMARY_QUESTION_UID]);
		QuestionSummaryDTO summary = summaries.get(findQuestionSequenceNumber(currentUid, summaries));
		QuestionSummarySingleAnswerDTO singleAnswer = isUserSummary ? summary.getUserSummarySingleAnswer(0) : summary
				.getAllSummarySingleAnswer(0);
		singleAnswer.setSum(row[DacoConstants.QUESTION_DB_NUMBER_SUMMARY_SUM]);
		singleAnswer.setAverage(row[DacoConstants.QUESTION_DB_NUMBER_SUMMARY_AVERAGE]);
	}

	private int findQuestionSequenceNumber(Long uid, List<QuestionSummaryDTO> summaries) {
		for (int summaryNumber = 0; summaryNumber < summaries.size(); summaryNumber++) {
			QuestionSummaryDTO summary = summaries.get(summaryNumber);
			if (summary != null && uid.equals(summary.getQuestionUid())) {
				return summaryNumber;
			}
		}
		return -1;
	}

	private String[] rewriteRow(Object[] objectRow) {
		String[] row = new String[objectRow.length];
		for (int fieldNumber = 0; fieldNumber < objectRow.length; fieldNumber++) {
			row[fieldNumber] = String.valueOf(objectRow[fieldNumber]);
		}
		return row;
	}

	public Integer getTotalRecordCount(Long contentId) {
		return (Integer) getHibernateTemplate().find(DacoAnswerDAOHibernate.FIND_TOTAL_RECORD_COUNT, contentId).get(0);
	}
}