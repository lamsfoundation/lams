package org.lamsfoundation.lams.tool.daco.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;

public interface DacoAnswerDAO extends DAO {
	List<List<DacoAnswer>> getRecordsByUserUid(Long userUid);

	List<DacoAnswer> getRecord(Long userUid, Integer recordId);

	List<List<DacoAnswer>> getAnswersByQuestionUid(Long questionUid);

	List<List<DacoAnswer>> getRecordsByUserUidAndDaco(Long userUid, Daco daco);
}