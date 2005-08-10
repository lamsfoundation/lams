/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */


/*
 * Created on Jul 1, 2005
 */
package org.lamsfoundation.lams.tool.noticeboard.dao;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardUser;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;

/**
 * @author mtruong
 * 
 * <p>Interface for the NoticeboardSession DAO, defines methods needed to access/modify
 * noticeboard users (learners of the noticeboard activity) </p>
 */
public interface INoticeboardUserDAO {
    
    /**
	 * <p>Return the persistent instance of a NoticeboardUser 
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the NoticeboardUser.
	 * @return the persistent instance of a NoticeboardUser or null if not found
	 */
    public NoticeboardUser getNbUserByUID(Long uid);
   
    /**
	 * <p> Return the persistent instance of a NoticeboardUser
	 * with the given user id <code>userId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param userId The id of a NoticeboardUser
	 * @return the persistent instance of a NoticeboardUser or null if not found.
	 */
    public NoticeboardUser getNbUserByID(Long userId);
    
    /**
	 * <p>Persist the given persistent instance of NoticeboardUser.</p>
	 * 
	 * @param nbUser The instance of NoticeboardUser to persist.
	 */
    public void saveNbUser(NoticeboardUser nbUser);
    
    /**
     * <p>Update the given persistent instance of NoticeboardUser.</p>
     * 
     * @param nbUser The instance of NoticeboardUser to persist.
     */
    public void updateNbUser(NoticeboardUser nbUser);
    
    /**
     * <p>Delete the given instance of NoticeboardUser</p>
     * 
     * @param nbUser The instance of NoticeboardUser to delete. 
     */
    public void removeNbUser(NoticeboardUser nbUser);
    
    /**
     * <p>Delete the given instance of NoticeboardUser with the
     * given user id <code>userId</code>
     * 
     * @param userId The noticeboard user id.
     */
    public void removeNbUser(Long userId);
    
    /**
     * Returns the number of users that are in this particular
     * session.
     * 
     * @param nbSession
     * @return the number of users that are in this session
     */
    public int getNumberOfUsers(NoticeboardSession nbSession);
   
}
