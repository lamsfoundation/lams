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

package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.McContent;
import org.lamsfoundation.lams.tool.mc.McQueUsr;
import org.lamsfoundation.lams.tool.mc.McSession;


/**
 * @author Ozgur Demirtas
 * <p>Interface for the McSession DAO, defines methods needed to access/modify mc session</p>
 * 
 */
public interface IMcSessionDAO {
	
	/**
	 * <p>Return the persistent instance of a McSession  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the McSession object.
	 * @return the persistent instance of a McSession or null if not found
	 */
	
    public McSession getMcSessionByUID(Long uid);
    
    /**
	 * <p> Return the persistent instance of a McSession
	 * with the given tool session id <code>mcSessionId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param mcSessionId The tool session id
	 * @return the persistent instance of a McSession or null if not found.
	 */
    public McSession findMcSessionById(Long mcSessionId);
	
	
	
    /**
	 * <p>Persist the given persistent instance of McSession.</p>
	 * 
	 * @param mcSession The instance of McSession to persist.
	 */
    public void saveMcSession(McSession mcSession);
    
    /**
     * <p>Update the given persistent instance of McSession.</p>
     * 
     * @param mcContent The instance of McSession to persist.
     */
    public void updateMcSession(McSession mcSession);

    /**
     * <p>Delete the given instance of McSession with the
     * given identifier <code>uid</code>
     * 
     * @param uid an identifier for the McSession instance. 
     */
    public void removeMcSessionByUID(Long uid);
    
    public void removeMcSessionById(Long mcSessionId);

    public void removeMcSession(McSession mcSession);
    
    
    /**
     * <p> Returns the persistent instance of McSession
     * associated with the given mc user, with user id <code>userId</code>, 
     * returns null if not found.
     * 
     * @param userId The mc user id
     * @return a persistent instance of McSessions or null if not found.
     */	
    public McSession getMcSessionByUser(Long userId);
    
   

    /**
     * <p>Creates and persists an instance of McQueUsr which is associated
     * with the McSession with tool session id <code>mcSessionId</code> </p>
     * 
     * @param mcSessionId The tool session id
     * @param user The instance of McQueUsr
     */
    public void addMcUsers(Long mcSessionId, McQueUsr user);
    
    /**
     * Returns a list of tool session ids which are associated with this
     * instance of McContent. 
     
     * @param nbContent The instance of McContent in which you want the list of toolSessionIds
     * @return a list of tool session Ids
     */
    public List getSessionsFromContent(McContent mcContent);
}