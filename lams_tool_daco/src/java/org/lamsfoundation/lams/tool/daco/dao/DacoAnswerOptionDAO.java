package org.lamsfoundation.lams.tool.daco.dao;

import java.util.List;

public interface DacoAnswerOptionDAO extends DAO {
	List<String> getOptionsByQuestionUid(Long questionUid);
}