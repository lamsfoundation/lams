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
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;

/**
 * @author Ozgur Demirtas
 * <p></p>
 * 
 */
public interface IVoteSessionDAO {
    public VoteSession getVoteSessionByUID(Long uid);
	
    public VoteSession findVoteSessionById(Long voteSessionId);
    
    public int countSessionComplete();

    public int countSessionIncomplete();
    
    public void saveVoteSession(VoteSession voteSession);
    
    public void updateVoteSession(VoteSession voteSession);
   
    public void removeVoteSessionByUID(Long uid);
    
    public void removeVoteSessionById(Long voteSessionId);

    public void removeVoteSession(VoteSession voteSession);

    public VoteSession getVoteSessionByUser(final Long userId);
	 
    public void removeVoteUsers(VoteSession voteSession);
	
    public void addVoteUsers(Long voteSessionId, VoteQueUsr user);
	
	public List getSessionsFromContent(VoteContent voteContent);
	
	public List getSessionNamesFromContent(VoteContent voteContent);

}