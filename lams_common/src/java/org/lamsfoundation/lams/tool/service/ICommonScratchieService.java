package org.lamsfoundation.lams.tool.service;

public interface ICommonScratchieService {
    void recalculateScratchieMarksForVsaQuestion(Long qbQuestionUid, String answer);
}