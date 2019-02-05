package org.lamsfoundation.lams.qb.service;

public interface IQbService {

    // statuses of comparing QB question coming from authoring with data existing in DB
    
    // no change detected
    static final int QUESTION_MODIFIED_NONE = 0;
    // change is minor, so the original question will be modified
    static final int QUESTION_MODIFIED_UPDATE = 1;
    // it is a new version of the old question
    static final int QUESTION_MODIFIED_VERSION_BUMP = 2;
    // it is a new question
    static final int QUESTION_MODIFIED_ID_BUMP = 3;

    // finds next question ID for Question Bank question
    int getMaxQuestionId();

    // finds next version for given question ID for Question Bank question
    int getMaxQuestionVersion(Integer qbQuestionId);
}
