package org.lamsfoundation.lams.qb.service;

import java.util.List;

import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.model.QbQuestion;

public class QbService implements IQbService {

    private IQbDAO qbDAO;
    
    @Override
    public QbQuestion getQbQuestionByUid(Long qbQuestionUid) {
	return qbDAO.getQbQuestionByUid(qbQuestionUid);
    }

    @Override
    public int getMaxQuestionId() {
	return qbDAO.getMaxQuestionId();
    }

    @Override
    public int getMaxQuestionVersion(Integer qbQuestionId) {
	return qbDAO.getMaxQuestionVersion(qbQuestionId);
    }
    
    @Override
    public List<QbQuestion> getPagedQbQuestions(Integer questionType, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return qbDAO.getPagedQbQuestions(questionType, page, size, sortBy, sortOrder, searchString);
    }
    
    @Override
    public int getCountQbQuestions(Integer questionType, String searchString) {
	return qbDAO.getCountQbQuestions(questionType, searchString);
    }

    public void setQbDAO(IQbDAO qbDAO) {
	this.qbDAO = qbDAO;
    }
}