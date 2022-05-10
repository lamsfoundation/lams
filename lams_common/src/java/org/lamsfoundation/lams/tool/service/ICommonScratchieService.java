package org.lamsfoundation.lams.tool.service;

import java.util.Map;

import org.lamsfoundation.lams.qb.model.QbToolQuestion;

public interface ICommonScratchieService {
    boolean recalculateMarksForVsaQuestion(Long qbQuestionUid, String answer);

    /**
     * Counts how many questions were answered correctly on first attempt by the given user, regardless of mark given.
     */
    Integer countCorrectAnswers(long toolContentId, int userId);

    /**
     * Returns VS answers which require allocation for the given activity
     */
    Map<QbToolQuestion, Map<String, Integer>> getUnallocatedVSAnswers(long toolContentId);
}