package org.lamsfoundation.lams.tool.service;

import java.util.List;

import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;

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
    List<QbToolQuestion> replaceQuestions(long toolContentId, String newActivityName, List<QbQuestion> newQuestions);

    /**
     * Replaces existing question in an activity with one provided as a parameter.
     */
    void replaceQuestion(long toolContentId, long oldQbQuestionUid, long newQbQuestionUid);
}