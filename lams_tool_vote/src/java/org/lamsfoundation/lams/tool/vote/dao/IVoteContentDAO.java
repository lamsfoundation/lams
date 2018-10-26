/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General License for more details.
 *
 * You should have received a copy of the GNU General License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.dao;

import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;

/**
 * Interface that defines the contract for VoteContent access
 *
 * @author Ozgur Demirtas
 */
public interface IVoteContentDAO {
    VoteContent getVoteContentByUID(Long uid);

    VoteContent getVoteContentByContentId(Long voteContentId);

    VoteContent getVoteContentBySession(Long voteSessionId);

    void saveVoteContent(VoteContent voteContent);

    void updateVoteContent(VoteContent voteContent);

    void saveOrUpdateVote(VoteContent voteContent);

    void removeVoteById(Long voteContentId);

    void removeVoteSessions(VoteContent voteContent);

    void addVoteSession(Long voteContentId, VoteSession voteSession);

    void removeQuestionsFromCache(VoteContent voteContent);

    void removeVoteContentFromCache(VoteContent voteContent);

    void delete(Object object);
}