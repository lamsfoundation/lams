/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.dao;

import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.tool.vote.dto.OpenTextAnswerDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteStatsDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteUsrAttempt;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * @author Ozgur Demirtas
 *
 *         <p>
 *         Interface that defines the contract for VoteUsrAttempt access
 *         </p>
 */
public interface IVoteUsrAttemptDAO {

    void saveVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    List<VoteUsrAttempt> getAttemptsForUser(final Long queUsrId);

    List<VoteUsrAttempt> getUserAttempts(final Long voteContentUid, final String userEntry);

    Set<String> getUserEntries(final Long voteContentUid);

    List<VoteUsrAttempt> getSessionOpenTextUserEntries(final Long voteSessionUid);

    VoteUsrAttempt getAttemptByUID(Long uid);

    int getSessionEntriesCount(final Long voteSessionId);

    int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid);

    List<VoteUsrAttempt> getAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId,
	    final Long voteSessionUid);

    void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid);

    VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId, final Long voteQueContentId,
	    final Long sessionUid);

    void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    Set<String> getAttemptsForUserAndSession(final Long queUsrId, final Long sessionUid);

    List<VoteUsrAttempt> getAttemptsForUserAndSessionUseOpenAnswer(final Long queUsrId, final Long sessionUid);

    List<VoteUsrAttempt> getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId);

    // Tablesorter methods
    /**
     * Gets the basic details about an attempt for a nomination. questionUid must not be null, sessionUid may be NULL.
     * This is
     * unusual for these methods - usually sessionId may not be null. In this case if sessionUid is null then you get
     * the values for the whole class, not just the group.
     *
     * Will return List<[login (String), fullname(String), attemptTime(Timestamp]>
     */
    List<Object[]> getUserAttemptsForTablesorter(Long sessionUid, Long questionUid, int page, int size, int sorting,
	    String searchString, IUserManagementService userManagementService);

    /**
     * Get the count of all possible users for getUserAttemptsForTablesorter(). Either sessionUid or questionUid may
     * be null but not both.
     */
    int getCountUsersBySession(Long sessionUid, Long questionUid, String searchString);

    List<VoteStatsDTO> getStatisticsBySession(Long toolContentId);

    /** Gets the details for the open text nominations */
    List<OpenTextAnswerDTO> getUserOpenTextAttemptsForTablesorter(Long sessionUid, Long contentUid, int page, int size,
	    int sorting, String searchStringVote, String searchStringUsername, IUserManagementService userManagementService);

    int getCountUsersForOpenTextEntries(Long sessionUid, Long contentUid, String searchStringVote,
	    String searchStringUsername);
}
