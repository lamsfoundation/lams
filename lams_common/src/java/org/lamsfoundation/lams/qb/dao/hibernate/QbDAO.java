package org.lamsfoundation.lams.qb.dao.hibernate;

import java.util.List;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public class QbDAO extends LAMSBaseDAO implements IQbDAO {

    private static final String FIND_MAX_QUESTION_ID = "SELECT MAX(questionId) FROM QbQuestion";
    private static final String FIND_MAX_VERSION = "SELECT MAX(version) FROM QbQuestion AS q WHERE q.questionId = :questionId";
    
    @Override
    public QbQuestion getQbQuestionByUid(Long qbQuestionUid) {
	return (QbQuestion) this.find(QbQuestion.class, qbQuestionUid);
    }

    @Override
    public int getMaxQuestionId() {
	Object result = this.getSession().createQuery(FIND_MAX_QUESTION_ID).uniqueResult();
	Integer max = (Integer) result;
	return max == null ? 1 : max + 1;
    }

    @Override
    public int getMaxQuestionVersion(Integer qbQuestionId) {
	Object result = this.getSession().createQuery(FIND_MAX_VERSION).setParameter("questionId", qbQuestionId)
		.uniqueResult();
	Integer max = (Integer) result;
	return max == null ? 1 : max + 1;
    }
    
    @Override
    public List<QbQuestion> getPagedQbQuestions(Integer questionType, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	final String SELECT_QUESTIONS = "SELECT DISTINCT question.* "
		+ " FROM lams_qb_question question  "
		+ " LEFT OUTER JOIN lams_qb_option qboption ON qboption.qb_question_uid = question.uid "
		+ " WHERE question.type = :questionType "
		+ " AND question.local = 0 "
		+ " AND (question.description LIKE CONCAT('%', :searchString, '%')"
		+ " OR question.name LIKE CONCAT('%', :searchString, '%') "
		+ " OR qboption.name LIKE CONCAT('%', :searchString, '%')) ";
	final String ORDER_BY_NAME = "ORDER BY question.name ";
	final String ORDER_BY_SMTH_ELSE = "ORDER BY question.question_id ";

	StringBuilder bldr = new StringBuilder(SELECT_QUESTIONS);
	if ("smth_else".equalsIgnoreCase(sortBy)) {
	    bldr.append(ORDER_BY_SMTH_ELSE);
	} else {
	    bldr.append(ORDER_BY_NAME);
	}
	bldr.append(sortOrder);
	
	NativeQuery<?> query = getSession().createNativeQuery(bldr.toString());
	query.setParameter("questionType", questionType);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	query.addEntity(QbQuestion.class);
	List<QbQuestion> queryResults = (List<QbQuestion>) query.list();

//	ArrayList<QbQuestion> userDtos = new ArrayList<QbQuestion>();
//	if (queryResults != null && queryResults.size() > 0) {
//	    for (Object[] element : queryResults) {
//
//		Long userId = ((Number) element[0]).longValue();
//		String firstName = (String) element[1];
//		String lastName = (String) element[2];
//		String login = (String) element[3];
//		float grade = element[4] == null ? 0 : ((Number) element[4]).floatValue();
//		Long portraitId = element[5] == null ? null : ((Number) element[5]).longValue();
//
//		QbQuestion userDto = new QbQuestion();
//		userDto.setUserId(userId);
//		userDto.setFirstName(firstName);
//		userDto.setLastName(lastName);
//		userDto.setLogin(login);
//		userDto.setGrade(grade);
//		userDto.setPortraitId(portraitId);
//		userDtos.add(userDto);
//	    }
//
//	}

	return queryResults;
    }

    @Override
    public int getCountQbQuestions(Integer questionType, String searchString) {
	final String SELECT_QUESTIONS = "SELECT COUNT(*) "
		+ " FROM lams_qb_question question "
		+ " LEFT OUTER JOIN lams_qb_option qboption ON qboption.qb_question_uid = question.uid "
		+ " AND question.type = :questionType "
		+ " AND (question.description LIKE CONCAT('%', :searchString, '%')"
		+ " OR question.name LIKE CONCAT('%', :searchString, '%') "
		+ " OR qboption.name LIKE CONCAT('%', :searchString, '%')) ";

	Query query = getSession().createNativeQuery(SELECT_QUESTIONS);
	query.setParameter("questionType", questionType);
	// support for custom search from a toolbar
	searchString = searchString == null ? "" : searchString;
	query.setParameter("searchString", searchString);
	int result = ((Number) query.uniqueResult()).intValue();
	return result;
    }
}