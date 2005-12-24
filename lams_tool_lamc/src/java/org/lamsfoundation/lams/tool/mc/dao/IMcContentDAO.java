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
import org.lamsfoundation.lams.tool.mc.McSession;

/**
 * @author Ozgur Demirtas
 * <p>Interface for the McContent DAO, defines methods needed to access/modify mc content</p>
 * 
 */
public interface IMcContentDAO {
    /**
	 * <p>Return the persistent instance of a McContent  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the McContent instance.
	 * @return the persistent instance of a McContent or null if not found
	 */
	public McContent getMcContentByUID(Long uid);
	
	/**
	 * <p> Return the persistent instance of a McContent
	 * with the given tool content id <code>mcContentId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param mcContentId The tool content id
	 * @return the persistent instance of a McContent or null if not found.
	 */
	public McContent findMcContentById(Long mcContentId);
	
	/**
     * <p> Returns the persistent instance of McContent
     * with the given tool session id <code>mcSessionId</code>, returns null if not found.
     * 
     * @param mcSessionId The tool session id
     * @return a persistent instance of McContent or null if not found.
     */
	public McContent getMcContentBySession(Long mcSessionId);
    
	/**
	 * <p>Persist the given persistent instance of McContent.</p>
	 * 
	 * @param mcContent The instance of McContent to persist.
	 */
    public void saveMcContent(McContent mcContent);
    
    /**
     * <p>Update the given persistent instance of McContent.</p>
     * 
     * @param mcContent The instance of McContent to persist.
     */
    public void updateMcContent(McContent mcContent);

    /**
     * <p>Delete the given instance of McContent</p>
     * 
     * @param mcContent The instance of McContent to delete. 
     */
    public void removeMc(McContent mcContent);
    
    
    /**
     * <p>Delete the given instance of McContent with the
     * given tool content id <code>mcContentId</code>
     * 
     * @param mcContentId The tool content Id. 
     */
    public void removeMcById(Long mcContentId);
   
    
    /**
     * <p>Deletes all instances of McSession that are associated
     * with the given instance of McContent</p>
     * 
     * @param mcContent The instance of McContent in which corresponding instances of McSession should be deleted.
     */
    public void removeMcSessions(McContent mcContent);
    
    /**
     * <p>Creates a persistent instance of McSession which is associated
     * with the McContent with tool content id <code>mcContentId</code> 
     * </p>
     * 
     * @param mcContentId The tool content id
     * @param mcSession The instance of McSession to add
     */
    public void addMcSession(Long mcContentId, McSession mcSession);
    
    public List findAll(Class objClass);
    
    public void flush();
  }