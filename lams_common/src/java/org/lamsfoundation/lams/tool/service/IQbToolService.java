package org.lamsfoundation.lams.tool.service;

import java.util.List;

import org.lamsfoundation.lams.qb.model.QbQuestion;

/**
 * It is an optional interface implemented by tools' services.
 * It facilitates work with Question Bank.
 * 
 * @author Marcin Cieslak
 *
 */
public interface IQbToolService {
    /**
     * Replaces existing questions in an activity with ones provided as a parameter. 
     */
    void replaceQuestions(long toolContentId, List<QbQuestion> newQuestions);
}