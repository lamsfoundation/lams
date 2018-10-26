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

import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.model.VoteSession;

/**
 * <p>
 * Interface that defines the contract for VoteSession access
 * </p>
 *
 * @author Ozgur Demirtas
 *
 */
public interface IVoteSessionDAO {
    VoteSession getVoteSessionByUID(Long uid);

    VoteSession getSessionBySessionId(Long voteSessionId);

    int countSessionComplete();

    void saveVoteSession(VoteSession voteSession);

    void updateVoteSession(VoteSession voteSession);

    void removeVoteSessionByUID(Long uid);

    void removeVoteSessionById(Long voteSessionId);

    void removeVoteSession(VoteSession voteSession);

    VoteSession getVoteSessionByUser(final Long userId);

    void removeVoteUsers(VoteSession voteSession);

    void addVoteUsers(Long voteSessionId, VoteQueUsr user);

    List<Long> getSessionsFromContent(VoteContent voteContent);

}