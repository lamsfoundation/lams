package org.lamsfoundation.lams.qb.service;

public interface IQbService {
    // finds next question ID for Question Bank question
    int getMaxQuestionId();

    // finds next version for given question ID for Question Bank question
    int getMaxQuestionVersion(Integer qbQuestionId);
}
