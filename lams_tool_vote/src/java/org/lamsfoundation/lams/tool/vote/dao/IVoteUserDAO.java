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
    
    public VoteQueUsr getMcUserByUID(Long uid);

    public VoteQueUsr findMcUserById(Long userId);
    
    public VoteQueUsr getMcUserBySession(Long userId, Long sessionId);
    
    public void saveMcUser(VoteQueUsr mcUser);
    
    public void updateMcUser(VoteQueUsr mcUser);
    
    public void removeMcUser(VoteQueUsr mcUser);
    
    public void removeMcUserById(Long userId);
    
    public int getNumberOfUsers(VoteSession mcSession);
    
    public List getMcUserBySessionOnly(final VoteSession mcSession);
    
    public int getTotalNumberOfUsers();
}
