package org.lamsfoundation.lams.tool.service;

import java.util.Collection;
import java.util.Map;

import org.lamsfoundation.lams.confidencelevel.VsaAnswerDTO;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;

public interface ICommonAssessmentService {

    /**
     * Returns answers learners left for VSA questions in Assessment activity (together with according confidence
     * levels, if such option is turned on in Assessment). Currently only Assessment tool is capable of producing VSA
     * answers.
     */
    Collection<VsaAnswerDTO> getVSAnswers(Long toolSessionId);

    /**
     * Returns VS answers which require allocation for the given activity
     */
    Map<QbToolQuestion, Map<String, Integer>> getUnallocatedVSAnswers(long toolContentId);

    /**
     * Counts how many questions were answered correctly by the given user, regardless of the mark given.
     * Currently it only works for MCQ and mark hedging questions.
     */
    Integer countCorrectAnswers(long toolContentId, int userId);

    /**
     * Counts how many questions were answered correctly by all users in the given activity, regardless of the mark
     * given.
     * Currently it only works for MCQ and mark hedging questions.
     *
     * @return map user ID -> correct answer count
     */
    Map<Integer, Integer> countCorrectAnswers(long toolContentId);

    boolean recalculateMarksForVsaQuestion(Long toolQuestionUid, String answer);
}