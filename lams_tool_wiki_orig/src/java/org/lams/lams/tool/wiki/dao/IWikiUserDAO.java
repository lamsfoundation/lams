/****************************************************************
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
 * ****************************************************************
 */

/* $$Id$$ */
package org.lams.lams.tool.wiki.dao;

import java.util.List;

import org.lams.lams.tool.wiki.WikiUser;
import org.lams.lams.tool.wiki.WikiSession;

/**
 * @author mtruong
 * 
 * <p>Interface for the WikiSession DAO, defines methods needed to access/modify
 * wiki users (learners of the wiki activity) </p>
 */
public interface IWikiUserDAO {
    
   
    /**
	 * <p> Return the persistent instance of a WikiUser
	 * with the given user id <code>userId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param userId The id of a WikiUser
	 * @return the persistent instance of a WikiUser or null if not found.
	 */
    public WikiUser getWikiUser(Long userId, Long toolSessionId);
    
    /**
	 * <p> Return the persistent instance of a WikiUser
	 * who has the user id <code>userId</code> and tool session id
	 * <code>sessionId</code>
	 * returns null if not found.</p>
	 * 
	 * @param userId. The id of the learner
	 * @param sessionId. The tool session id to which this user belongs to.
	 * @return the persistent instance of a WikiUser or null if not found.
	 */
    public WikiUser getWikiUserBySession(Long userId, Long sessionId);
    
    /**
	 * <p>Persist the given persistent instance of WikiUser.</p>
	 * 
	 * @param wikiUser The instance of WikiUser to persist.
	 */
    public void saveWikiUser(WikiUser wikiUser);
    
    /**
     * <p>Update the given persistent instance of WikiUser.</p>
     * 
     * @param wikiUser The instance of WikiUser to persist.
     */
    public void updateWikiUser(WikiUser wikiUser);
    
    /**
     * <p>Delete the given instance of WikiUser</p>
     * 
     * @param wikiUser The instance of WikiUser to delete. 
     */
    public void removeWikiUser(WikiUser wikiUser);
    
    /**
     * <p>Delete the given instance of WikiUser with the
     * given user id <code>userId</code>
     * 
     * @param userId The wiki user id.
     */
    public void removeWikiUser(Long userId);
    
    /**
     * Returns the number of users that are in this particular
     * session.
     * 
     * @param wikiSession
     * @return the number of users that are in this session
     */
    public int getNumberOfUsers(WikiSession wikiSession);
   
    public List getWikiUsersBySession(Long sessionId);
}
