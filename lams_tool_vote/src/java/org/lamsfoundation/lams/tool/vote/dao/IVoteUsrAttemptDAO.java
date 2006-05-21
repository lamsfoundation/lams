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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
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
 * 
 * @author Ozgur Demirtas
 * * <p></p>
 *
 */
public interface IVoteUsrAttemptDAO
{
 	public VoteUsrAttempt getVoteUserAttemptByUID(Long uid);
	
	public void saveVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);
	
	public List getAttemptsForUser(final Long queUsrId);
	
	public List getUserRecords(final String userEntry);
	
	public List getContentEntries(final Long voteContentUid);
	
	public Set getUserEntries();
	
	public int getAllEntriesCount();
	
	public List getSessionUserEntries(final Long voteSessionUid);
	
	public Set getSessionUserEntriesSet(final Long voteSessionUid);
	
	public List getUserEnteredVotesForSession(final String userEntry, final Long voteSessionUid);
	
	public VoteUsrAttempt getAttemptByUID(Long uid);
	
	public int getCompletedSessionEntriesCount(final Long voteSessionUid);
	
	public int getSessionEntriesCount(final Long voteSessionId);
	
	public int getStandardAttemptsForQuestionContentAndContentUid(final Long voteQueContentId, final Long voteContentUid);
	
	public int getSessionUserRecordsEntryCount(final String userEntry, final Long voteSessionUid, IVoteService voteService);
	
	public int getUserRecordsEntryCount(final String userEntry);
	
	public int getAttemptsForQuestionContent(final Long voteQueContentId);
	
	public int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid);
	
	public void removeAttemptsForUser(final Long queUsrId);
	
	public void removeAttemptsForUserandSession(final Long queUsrId, final Long voteSessionId);
	
	public int  getLastNominationCount(Long userId);
	
	public VoteUsrAttempt getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId);
	
	public VoteUsrAttempt getAttemptsForUserAndQuestionContentAndSession(final Long queUsrId, final Long voteQueContentId, final Long voteSessionId);
	
	public List getAttemptsListForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId);
	
	public List getAttemptForQueContent(final Long queUsrId, final Long voteQueContentId);
	
	public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);
	
	public void removeVoteUsrAttemptByUID(Long uid);
	
	public void removeVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt);
}


