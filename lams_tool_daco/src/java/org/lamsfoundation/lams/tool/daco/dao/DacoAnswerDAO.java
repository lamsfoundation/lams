package org.lamsfoundation.lams.tool.daco.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;

public interface DacoAnswerDAO extends DAO {

	List<QuestionSummaryDTO> getQuestionSummaries(Long userUid, List<QuestionSummaryDTO> blankSummary);

	Integer getGroupRecordCount(Long sessionId);
}