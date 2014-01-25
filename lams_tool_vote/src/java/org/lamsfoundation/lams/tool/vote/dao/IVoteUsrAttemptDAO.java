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

import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;

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

    List getUserRecords(final String userEntry);

    Set getUserEntries();

    int getUserEnteredVotesCountForContent(final Long voteContentUid);

    List getSessionUserEntries(final Long voteSessionUid);

    Set getSessionUserEntriesSet(final Long voteSessionUid);

    List getUserEnteredVotesForSession(final String userEntry, final Long voteSessionUid);

    VoteUsrAttempt getAttemptByUID(Long uid);

    int getCompletedSessionEntriesCount(final Long voteSessionUid);

    int getSessionEntriesCount(final Long voteSessionId);

    List<VoteUsrAttempt> getStandardAttemptsForQuestionContentAndContentUid(final Long voteQueContentId);

    int getAttemptsForQuestionContent(final Long voteQueContentId);

    int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid);

    List getStandardAttemptUsersForQuestionContentAndSessionUid(final Long voteQueContentId,
	    final Long voteSessionUid);

    void removeAttemptsForUserandSession(final Long queUsrId, final Long voteSessionId);

    VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId,
	    final Long voteQueContentId, final Long voteSessionId);

    void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    Set getAttemptsForUserAndSession(final Long queUsrId, final Long voteSessionUid);

    Set getAttemptsForUserAndSessionUseOpenAnswer(final Long queUsrId, final Long voteSessionId);

    List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId);
}
