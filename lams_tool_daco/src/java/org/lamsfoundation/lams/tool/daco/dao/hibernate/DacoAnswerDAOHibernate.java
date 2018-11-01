package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dao.DacoAnswerDAO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummarySingleAnswerDTO;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.springframework.stereotype.Repository;

@Repository
public class DacoAnswerDAOHibernate extends LAMSBaseDAO implements DacoAnswerDAO {

    protected final Log log = LogFactory.getLog(getClass());

    private static final String FIND_USER_NUMBER_SUMMARY = "SELECT a.question.uid, "
	    + "SUM(a.answer),AVG(a.answer) FROM " + DacoAnswer.class.getName()
	    + " AS a WHERE a.question.type=:numberQuestionType AND a.user.uid=:userUid AND a.answer IS NOT NULL "
	    + "GROUP BY a.question.uid ORDER BY a.question.uid";

    private static final String FIND_GROUP_NUMBER_SUMMARY = "SELECT a.question.uid, "
	    + "SUM(a.answer),AVG(a.answer) FROM " + DacoAnswer.class.getName() + " AS a, " + DacoUser.class.getName()
	    + " AS u WHERE a.question.type=:numberQuestionType AND u.uid=:userUid AND a.user.session.sessionId=u.session.sessionId AND a.answer IS NOT NULL "
	    + "GROUP BY a.question.uid ORDER BY a.question.uid";

    private static final String FIND_USER_ANSWER_ENUMERATION_QUERY = "SELECT DISTINCT a.question.uid, a.answer, a.question.type, COUNT(*) FROM "
	    + DacoAnswer.class.getName()
	    + " AS a WHERE a.user.uid=:userUid AND a.question.type IN (:numberQuestionType,:radioQuestionType,:dropdownQuestionType,:checkboxQuestionType) "
	    + "AND a.answer IS NOT NULL	GROUP BY a.question.uid, a.answer ORDER BY a.question.uid,a.answer";

    private static final String FIND_GROUP_ANSWER_ENUMERATION_QUERY = "SELECT DISTINCT a.question.uid, a.answer, a.question.type, COUNT(*) FROM "
	    + DacoAnswer.class.getName() + " AS a, " + DacoUser.class.getName()
	    + " AS u WHERE a.question.type IN (:numberQuestionType,:radioQuestionType,:dropdownQuestionType,:checkboxQuestionType) "
	    + " AND u.uid=:userUid AND a.user.session.sessionId=u.session.sessionId AND a.answer IS NOT NULL GROUP BY a.question.uid, a.answer ORDER BY a.question.uid,a.answer";

    private static final String FIND_ANSWER_COUNT = "SELECT COUNT(*) FROM " + DacoAnswer.class.getName()
	    + " AS a WHERE a.question.uid=? AND a.answer IS NOT NULL";

    private static final String FIND_USER_RECORD_COUNT = "SELECT COUNT (DISTINCT a.recordId) FROM "
	    + DacoAnswer.class.getName() + " AS a WHERE a.user.userId=:userId AND a.user.session.sessionId=:sessionId";

    private static final String FIND_SESSION_RECORD_COUNT = "SELECT COUNT (DISTINCT a.recordId) FROM "
	    + DacoAnswer.class.getName() + " AS a WHERE a.user.session.sessionId=:sessionId";

    @Override
    @SuppressWarnings("unchecked")
    public List<QuestionSummaryDTO> getQuestionSummaries(Long userUid, List<QuestionSummaryDTO> summaries) {

	List<Object[]> result = (List<Object[]>) doFindByNamedParam(DacoAnswerDAOHibernate.FIND_USER_NUMBER_SUMMARY,
		new String[] { "userUid", "numberQuestionType" },
		new Object[] { userUid, DacoConstants.QUESTION_TYPE_NUMBER });

	for (Object[] objectRow : result) {
	    addNumberSummary(summaries, objectRow, true);
	}

	result = (List<Object[]>) doFindByNamedParam(DacoAnswerDAOHibernate.FIND_GROUP_NUMBER_SUMMARY,
		new String[] { "userUid", "numberQuestionType" },
		new Object[] { userUid, DacoConstants.QUESTION_TYPE_NUMBER });

	for (Object[] objectRow : result) {
	    addNumberSummary(summaries, objectRow, false);
	}

	result = (List<Object[]>) doFindByNamedParam(DacoAnswerDAOHibernate.FIND_USER_ANSWER_ENUMERATION_QUERY,
		new String[] { "userUid", "numberQuestionType", "radioQuestionType", "dropdownQuestionType",
			"checkboxQuestionType" },
		new Object[] { userUid, DacoConstants.QUESTION_TYPE_NUMBER, DacoConstants.QUESTION_TYPE_RADIO,
			DacoConstants.QUESTION_TYPE_DROPDOWN, DacoConstants.QUESTION_TYPE_CHECKBOX });

	for (Object[] objectRow : result) {
	    addAnswerEnumerationSummary(summaries, objectRow, true);
	}

	result = (List<Object[]>) doFindByNamedParam(DacoAnswerDAOHibernate.FIND_GROUP_ANSWER_ENUMERATION_QUERY,
		new String[] { "userUid", "numberQuestionType", "radioQuestionType", "dropdownQuestionType",
			"checkboxQuestionType" },
		new Object[] { userUid, DacoConstants.QUESTION_TYPE_NUMBER, DacoConstants.QUESTION_TYPE_RADIO,
			DacoConstants.QUESTION_TYPE_DROPDOWN, DacoConstants.QUESTION_TYPE_CHECKBOX });

	for (Object[] objectRow : result) {
	    addAnswerEnumerationSummary(summaries, objectRow, false);
	}
	return summaries;
    }

    private void addAnswerEnumerationSummary(List<QuestionSummaryDTO> summaries, Object[] objectRow,
	    boolean isUserSummary) {
	String[] row = rewriteRow(objectRow);
	long currentUid = Long.parseLong(row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_QUESTION_UID]);
	QuestionSummaryDTO summary = summaries.get(findQuestionSequenceNumber(currentUid, summaries));
	QuestionSummarySingleAnswerDTO singleAnswer = new QuestionSummarySingleAnswerDTO();
	singleAnswer.setAnswer(row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_ANSWER]);
	singleAnswer.setCount(row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_COUNT]);
	Long answerCount = (Long) doFind(DacoAnswerDAOHibernate.FIND_ANSWER_COUNT, currentUid).get(0);
	singleAnswer.setAverage(Math.round(Float.parseFloat(singleAnswer.getCount()) / answerCount * 100) + "%");

	if (Short.parseShort(
		row[DacoConstants.QUESTION_DB_ANSWER_ENUMERATION_SUMMARY_QUESTION_TYPE]) == DacoConstants.QUESTION_TYPE_NUMBER) {
	    QuestionSummarySingleAnswerDTO currentSingleAnswer = null;
	    int answerIndex = 1;
	    do {
		currentSingleAnswer = isUserSummary ? summary.getUserSummarySingleAnswer(answerIndex)
			: summary.getGroupSummarySingleAnswer(answerIndex);
		if (currentSingleAnswer == null) {
		    if (isUserSummary) {
			summary.addUserSummarySingleAnswer(answerIndex, singleAnswer);
		    } else {
			summary.addGroupSummarySingleAnswer(answerIndex, singleAnswer);
		    }
		    currentSingleAnswer = null;
		}
		answerIndex++;
	    } while (currentSingleAnswer != null);
	} else {
	    try {
		int answerIndex = Integer.parseInt(singleAnswer.getAnswer()) - 1;
		if (isUserSummary) {
		    summary.addUserSummarySingleAnswer(answerIndex, singleAnswer);
		} else {
		    summary.addGroupSummarySingleAnswer(answerIndex, singleAnswer);
		}
	    } catch (NumberFormatException e) {
		log.debug("Found non-integer value '" + singleAnswer.getAnswer() + "' for question uid " + currentUid);
	    }
	}
    }

    private void addNumberSummary(List<QuestionSummaryDTO> summaries, Object[] objectRow, boolean isUserSummary) {
	String[] row = rewriteRow(objectRow);
	long currentUid = Long.parseLong(row[DacoConstants.QUESTION_DB_NUMBER_SUMMARY_QUESTION_UID]);
	QuestionSummaryDTO summary = summaries.get(findQuestionSequenceNumber(currentUid, summaries));
	QuestionSummarySingleAnswerDTO singleAnswer = isUserSummary ? summary.getUserSummarySingleAnswer(0)
		: summary.getGroupSummarySingleAnswer(0);
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

    @Override
    public Integer getUserRecordCount(Long userId, Long sessionId) {
	return ((Number) doFindByNamedParam(DacoAnswerDAOHibernate.FIND_USER_RECORD_COUNT,
		new String[] { "userId", "sessionId" }, new Object[] { userId, sessionId }).get(0)).intValue();
    }

    @Override
    public Integer getSessionRecordCount(Long sessionId) {
	return ((Number) doFindByNamedParam(DacoAnswerDAOHibernate.FIND_SESSION_RECORD_COUNT,
		new String[] { "sessionId" }, new Object[] { sessionId }).get(0)).intValue();
    }
}