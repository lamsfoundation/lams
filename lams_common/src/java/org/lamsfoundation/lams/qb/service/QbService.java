package org.lamsfoundation.lams.qb.service;

import org.lamsfoundation.lams.qb.dao.IQbDAO;

public class QbService implements IQbService {

    private IQbDAO qbDAO;

    @Override
    public int getMaxQuestionId() {
	return qbDAO.getMaxQuestionId();
    }

    @Override
    public int getMaxQuestionVersion(Integer qbQuestionId) {
	return qbDAO.getMaxQuestionVersion(qbQuestionId);
    }

    public void setQbDAO(IQbDAO qbDAO) {
	this.qbDAO = qbDAO;
    }
}