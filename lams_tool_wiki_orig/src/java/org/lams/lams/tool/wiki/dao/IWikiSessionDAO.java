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

import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;


/**
 * <p>Interface for the WikiSession DAO, defines methods needed to access/modify
 * wiki session</p>
 * @author mtruong
 */
public interface IWikiSessionDAO {
	
   
    /**
	 * <p> Return the persistent instance of a WikiSession
	 * with the given tool session id <code>wikiSessionId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param wikiSessionId The tool session id
	 * @return the persistent instance of a WikiSession or null if not found.
	 */
    public WikiSession findWikiSessionById(Long wikiSessionId);
	
	
    /**
	 * <p>Persist the given persistent instance of WikiSession.</p>
	 * 
	 * @param wikiSession The instance of WikiSession to persist.
	 */
    public void saveWikiSession(WikiSession wikiSession);
    
    /**
     * <p>Update the given persistent instance of WikiSession.</p>
     * 
     * @param wikiContent The instance of WikiSession to persist.
     */
    public void updateWikiSession(WikiSession wikiSession);
    
    
    /**
     * <p>Delete the given instance of WikiSession</p>
     * 
     * @param wikiSession The instance of WikiSession to delete. 
     */
    public void removeWikiSession(WikiSession wikiSession);
    
    /**
     * <p>Delete the given instance of WikiSession with the
     * given tool session id <code>wikiSessionid</code>
     * 
     * @param wikiSessionId The tool session Id. 
     */
    public void removeWikiSession(Long wikiSessionId);
    
    /**
     * <p> Returns the persistent instance of WikiSession
     * associated with the given wiki user, with user id <code>userId</code>, 
     * returns null if not found.
     * 
     * @param userId The wiki user id
     * @return a persistent instance of WikiSessions or null if not found.
     */	
    public WikiSession getWikiSessionByUser(Long userId); 
    
    /**
     * <p>Deletes all instances of WikiUser that are associated
     * with the given instance of WikiSession</p>
     * 
     * @param wikiSession The instance of WikiSession in which corresponding instances of WikiUser should be deleted.
     */
    public void removeWikiUsers(WikiSession wikiSession); 

    /**
     * <p>Creates and persists an instance of WikiUser which is associated
     * with the WikiSession with tool session id <code>wikiSessionId</code> </p>
     * 
     * @param wikiSessionId The tool session id
     * @param user The instance of WikiUser
     */
    public void addWikiUsers(Long wikiSessionId, WikiUser user);
    
    /**
     * Returns a list of tool session ids which are associated with this
     * instance of WikiContent. 
     * <p>For example, if the given instance <code>wikiContent</code> has a tool content id
     * of 3, and consider an extract of the tl_lawiki10_session table:</p>
     * <pre> 
     * 		 ----------------------------
     * 		 attachmentId | toolSessionId
     * 		 ----------------------------
     * 			1		  | 	3
     * 			2		  | 	3
     * 			3		  | 	3
     * 			4 		  | 	1
     * 		 ----------------------------
     * </pre>
     * Then a call to <code>getSessionsFromContent</code> will return a list containing the values
     * 1, 2 and 3. 
     * @param wikiContent The instance of WikiContent in which you want the list of toolSessionIds
     * @return a list of tool session Ids
     */
    public List getSessionsFromContent(WikiContent wikiContent);
}
