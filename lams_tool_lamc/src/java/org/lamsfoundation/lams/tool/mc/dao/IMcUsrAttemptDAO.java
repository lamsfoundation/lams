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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;

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
     * Get the highest attempt order for a user for a particular question
     */
    McUsrAttempt getUserAttemptByQuestion(Long queUsrUid, Long mcQueContentId);

    /**
     * Count how many attempts done to this option
     *
     * @param optionUid
     * @return
     */
    int getAttemptsCountPerOption(Long optionUid);

    /**
     * Calculate what is the total mark scored by user in this activity. Only responseFinalised is taken into account.
     *
     * @param userUid
     * @return
     */
    int getUserTotalMark(final Long userUid);

    McUsrAttempt getUserAttemptByUid(Long uid);

}
