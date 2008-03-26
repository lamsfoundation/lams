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

import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;

/**
 * <p>Interface for the WikiContent DAO, defines methods needed to access/modify
 * wiki content</p>
 * @author mtruong
 */
public interface IWikiContentDAO {
    
    
    /**
	 * <p>Return the persistent instance of a WikiContent  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the WikiContent instance.
	 * @return the persistent instance of a WikiContent or null if not found
	 */
	public WikiContent getWikiContentByUID(Long uid);
	
	/**
	 * <p> Return the persistent instance of a WikiContent
	 * with the given tool content id <code>wikiContentId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param wikiContentId The tool content id
	 * @return the persistent instance of a WikiContent or null if not found.
	 */
	public WikiContent findWikiContentById(Long wikiContentId);
	
	/**
     * <p> Returns the persistent instance of WikiContent
     * with the given tool session id <code>wikiSessionId</code>, returns null if not found.
     * 
     * @param wikiSessionId The tool session id
     * @return a persistent instance of WikiContent or null if not found.
     */
	public WikiContent getWikiContentBySession(Long wikiSessionId);
    
	/**
	 * <p>Persist the given persistent instance of WikiContent.</p>
	 * 
	 * @param wikiContent The instance of WikiContent to persist.
	 */
    public void saveWikiContent(WikiContent wikiContent);
    
    /**
     * <p>Update the given persistent instance of WikiContent.</p>
     * 
     * @param wikiContent The instance of WikiContent to persist.
     */
    public void updateWikiContent(WikiContent wikiContent);

    /**
     * <p>Delete the given instance of WikiContent</p>
     * 
     * @param wikiContent The instance of WikiContent to delete. 
     */
    public void removeWiki(WikiContent wikiContent);
    
    /**
     * <p>Delete the given instance of WikiContent with the
     * given tool content id <code>wikiContentId</code>
     * 
     * @param wikiContentId The tool content Id. 
     */
    public void removeWiki(Long wikiContentId);
   
    
    /**
     * <p>Deletes all instances of WikiSession that are associated
     * with the given instance of WikiContent</p>
     * 
     * @param wikiContent The instance of WikiContent in which corresponding instances of WikiSession should be deleted.
     */
    public void removeWikiSessions(WikiContent wikiContent);
    
    /**
     * <p>Creates a persistent instance of WikiSession which is associated
     * with the WikiContent with tool content id <code>wikiContentId</code> 
     * </p>
     * 
     * @param wikiContentId The tool content id
     * @param wikiSession The instance of WikiSession to add
     */
    public void addWikiSession(Long wikiContentId, WikiSession wikiSession);
    
  }
