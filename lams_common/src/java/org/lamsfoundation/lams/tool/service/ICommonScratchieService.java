package org.lamsfoundation.lams.tool.service;

public interface ICommonScratchieService {
    boolean recalculateMarksForVsaQuestion(Long qbQuestionUid, String answer);

    /**
     * Counts how many questions were answered correctly on first attempt by the given user, regardless of mark given.
     */
    Integer countCorrectAnswers(long toolContentId, int userId);
}