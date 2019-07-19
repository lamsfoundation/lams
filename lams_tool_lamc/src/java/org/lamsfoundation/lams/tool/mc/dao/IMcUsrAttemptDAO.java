/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;

/**
 *
 * @author Ozgur Demirtas *
 *         <p>
 *         Interface for the McUsrAttempt DAO, defines methods needed to access/modify user attempt data
 *         </p>
 *
 */
public interface IMcUsrAttemptDAO {

    /**
     * *
     * <p>
     * saves McUsrAttempt with the given identifier <code>mcUsrAttempt</code>
     * </p>
     *
     * @param uid
     * @return
     */
    void saveMcUsrAttempt(McUsrAttempt mcUsrAttempt);

    /**
     * *
     * <p>
     * updates McUsrAttempt with the given identifier <code>mcUsrAttempt</code>
     * </p>
     *
     * @param mcUsrAttempt
     * @return
     */
    void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt);

    /**
     * *
     * <p>
     * removes McUsrAttempt with the given identifier <code>mcUsrAttempt</code>
     * </p>
     *
     * @param mcUsrAttempt
     * @return
     */
    void removeAllUserAttempts(Long queUserUid);

    void removeAttempt(McUsrAttempt userAttempt);

    /**
     * Get the most recent attempts (for all questions) for one user in one tool session
     *
     * @param queUserUid
     * @return
     */
    List<McUsrAttempt> getFinalizedUserAttempts(Long queUserUid);

    /**
     * Returns attempts and portraitUiids left by all users in the specified session. It's used only by
     * ToolSessionManager.getConfidenceLevels().
     *
     * @param sessionId
     * @return
     */
    List<Object[]> getFinalizedAttemptsBySessionId(final Long sessionId);

    /**
     * Returns attempts and portraitUiids left by all leaders in the specified activity. It's used only by
     * ToolSessionManager.getConfidenceLevels().
     *
     * @param contentId
     * @return
     */
    List<Object[]> getLeadersFinalizedAttemptsByContentId(final Long contentId);

    /**
     * Get the highest attempt order for a user for a particular questionDescription
     */
    McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId);

    /**
     * Get the highest attempt order for a all users in a session for a particular questionDescription
     */
    List<McUsrAttempt> getUserAttemptsByQuestionSession(final Long sessionUid, final Long mcQueContentId);

    /**
     * Count how many attempts done to this option
     *
     * @param optionUid
     * @return
     */
    int getAttemptsCountPerOption(Long optionUid, Long mcQueContentId);

    /**
     * Checks whether anyone has attempted this assessment.
     */
    boolean isMcContentAttempted(Long mcContentUid);

    /**
     * Calculate what is the total mark scored by user in this activity. Only responseFinalised is taken into account.
     *
     * @param userUid
     * @return
     */
    int getUserTotalMark(final Long userUid);

    McUsrAttempt getUserAttemptByUid(Long uid);

    /**
     * Returns all existing total marks for users belonging to tool content.
     *
     * @param toolContentId
     * @return
     */
    List<ToolOutputDTO> getLearnerMarksByContentId(Long toolContentId);

}
