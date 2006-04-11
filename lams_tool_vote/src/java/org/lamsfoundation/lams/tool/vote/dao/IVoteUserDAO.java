/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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

import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;

/**
 * @author Ozgur Demirtas
 * <p></p>
 */
public interface IVoteUserDAO {
    public VoteQueUsr getVoteUserByUID(Long uid);
	
	public VoteQueUsr findVoteUserById(Long userId);
	
	public List getVoteUserBySessionOnly(final VoteSession voteSession);
	
	public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long voteSessionId);
	
	public void saveVoteUser(VoteQueUsr voteUser);
	
    public void updateVoteUser(VoteQueUsr voteUser);

    public void removeVoteUserById(Long userId);

    public void removeVoteUser(VoteQueUsr voteUser);
    
    public int getNumberOfUsers(VoteSession voteSession);
    
    public int getTotalNumberOfUsers(); 
}
