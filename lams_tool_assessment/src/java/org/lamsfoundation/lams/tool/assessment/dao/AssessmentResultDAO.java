/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.assessment.dao;

import org.lamsfoundation.lams.tool.assessment.dto.AssessmentUserDTO;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentUser;

import java.util.List;
import java.util.Map;

public interface AssessmentResultDAO extends DAO {

    List<AssessmentResult> getAssessmentResults(Long assessmentUid, Long userId);

    List<AssessmentResult> getAssessmentResultsByQbToolQuestionAndAnswer(Long toolQuestionUid, String answer);

    List<AssessmentResult> getAssessmentResultsBySession(Long sessionId, Long userId);

    List<AssessmentResult> getFinishedAssessmentResultsByUser(Long sessionId, Long userId);

    List<AssessmentResult> getLastAssessmentResults(Long assessmentUid);

    AssessmentResult getLastAssessmentResult(Long assessmentUid, Long userId);

    /**
     * Checks whether the last attempt started by user is finished.
     *
     * @param user
     * @return true if user has finished it, false otherwise
     */
    Boolean isLastAttemptFinishedByUser(AssessmentUser user);

    AssessmentResult getLastFinishedAssessmentResult(Long assessmentUid, Long userId);

    Float getLastTotalScoreByUser(Long assessmentUid, Long userId);

    List<AssessmentUserDTO> getLastTotalScoresByContentId(Long assessmentUid);

    Float getBestTotalScoreByUser(Long sessionId, Long userId);

    List<AssessmentUserDTO> getBestTotalScoresByContentId(Long assessmentUid);

    Float getFirstTotalScoreByUser(Long sessionId, Long userId);

    List<AssessmentUserDTO> getFirstTotalScoresByContentId(Long assessmentUid);

    Float getAvergeTotalScoreByUser(Long sessionId, Long userId);

    List<AssessmentUserDTO> getAverageTotalScoresByContentId(Long assessmentUid);

    Integer getLastFinishedAssessmentResultTimeTaken(Long assessmentUid, Long userId);

    AssessmentResult getLastFinishedAssessmentResultByUser(Long sessionId, Long userId);

    /**
     * Returns all last finished results for the specified assessment.
     *
     * @param contentId
     * @return
     */
    List<AssessmentResult> getLastFinishedAssessmentResults(Long contentId);

    List<Object[]> getLastFinishedAssessmentResultsBySession(Long sessionId);

    /**
     * Get results for all leaders in current activity.
     *
     * @param contentId
     * @return
     */
    List<Object[]> getLeadersLastFinishedAssessmentResults(Long contentId);

    int getAssessmentResultCount(Long assessmentUid, Long userId);

    /**
     * Checks whether anyone has attempted this assessment.
     */
    boolean isAssessmentAttempted(Long assessmentUid);

    AssessmentResult getAssessmentResultByUid(Long assessmentResultUid);

    /**
     * Count how many last attempts selected specified option.
     */
    int countAttemptsPerOption(Long toolContentId, Long optionUid, boolean finishedAttemptsOnly);

    Map<Integer, List<String[]>> getAnsweredQuestionsByUsersForCompletionChart(long toolContentId);

    List<Object[]> getLearnersWithFinishedCurrentAttemptForCompletionChart(Long contentId);
}