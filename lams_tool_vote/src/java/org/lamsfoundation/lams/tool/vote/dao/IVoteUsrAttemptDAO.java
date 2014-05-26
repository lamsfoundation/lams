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

    int getUserEnteredVotesCountForContent(final Long voteContentUid);

    Set<VoteUsrAttempt> getSessionUserEntriesSet(final Long voteSessionUid);

    VoteUsrAttempt getAttemptByUID(Long uid);

    int getSessionEntriesCount(final Long voteSessionId);

    List<VoteUsrAttempt> getStandardAttemptsByQuestionUid(final Long questionUid);

    int getAttemptsForQuestionContent(final Long voteQueContentId);

    int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid);

    List<VoteUsrAttempt> getAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId,
	    final Long voteSessionUid);

    void removeAttemptsForUserandSession(final Long queUsrId, final Long sessionUid);

    VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId, final Long voteQueContentId,
	    final Long sessionUid);

    void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);

    Set<String> getAttemptsForUserAndSession(final Long queUsrId, final Long sessionUid);

    Set<String> getAttemptsForUserAndSessionUseOpenAnswer(final Long queUsrId, final Long sessionUid);

    List<VoteUsrAttempt> getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId);
    
}
