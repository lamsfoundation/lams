package org.lamsfoundation.lams.qb.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;

public interface IQbDAO extends IBaseDAO {
    // finds next question ID for Question Bank question
    int getMaxQuestionId();

    // finds next version for given question ID for Question Bank question
    int getMaxQuestionVersion(Integer qbQuestionId);
}