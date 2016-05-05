package org.lamsfoundation.lams.tool.daco.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;

public interface DacoAnswerDAO extends DAO {
    /**
     * Gets the summary values for the questions.
     *
     * @param userUid
     *            user for who the summary should be created
     * @param blankSummary
     *            the empty structure where the summary values will be filled into
     * @return list of question summaries
     */
    List<QuestionSummaryDTO> getQuestionSummaries(Long userUid, List<QuestionSummaryDTO> blankSummary);

    /**
     * Gets the number of records entered by user.
     *
     * @param sessionId
     *            session ID of the group
     * @return number of records for that user
     */
    Integer getUserRecordCount(Long userId, Long sessionId);

    /**
     * Gets the number of records entered by users in this session.
     */
    Integer getSessionRecordCount(Long sessionId);
}