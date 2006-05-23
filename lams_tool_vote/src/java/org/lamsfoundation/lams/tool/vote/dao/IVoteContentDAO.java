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

import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;

/**
 * @author Ozgur Demirtas
 * 
 * <p>Interface that defines the contract for VoteContent access </p>
 */
public interface IVoteContentDAO {
	public VoteContent getVoteContentByUID(Long uid);

	public VoteContent findVoteContentById(Long mcContentId);

	public VoteContent getVoteContentBySession(Long mcSessionId);

    public void saveVoteContent(VoteContent mcContent);

    public void updateVoteContent(VoteContent mcContent);

    public void removeVote(VoteContent mcContent);

    public void removeVoteById(Long mcContentId);

    public void removeVoteSessions(VoteContent mcContent);

    public void addVoteSession(Long mcContentId, VoteSession mcSession);
    
    public List findAll(Class objClass);
    
    public void flush();
  }