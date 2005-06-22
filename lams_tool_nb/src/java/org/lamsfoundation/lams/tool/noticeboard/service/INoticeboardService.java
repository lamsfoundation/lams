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

package org.lamsfoundation.lams.tool.noticeboard.service;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
;

/**
 * Defines the contract that the tool service provider must follow
 * 
 *
 */
public interface INoticeboardService {
	
    //===================================================================
    // NoticeboardContent access methods
    //===================================================================	
    
    /**
     * <p>Retrieve an instance of NoticeboardContent with the given
     * identifier <code>uid</code> </p>
     * @param uid The given unique identifier of the NoticeboardContent instance
     * @return an instance of NoticeboardContent
     */
    public NoticeboardContent retrieveNoticeboardByUID(Long uid);
    
    /**
     * <p> Returns the persistent instance of NoticeboardContent
     * with the given tool session id <code>nbSessionId</code>, returns null if not found.
     * 
     * @param nbSessionId The tool session id
     * @return a persistent instance of NoticeboardContent or null if not found.
     */	
	public NoticeboardContent retrieveNoticeboardBySessionID(Long nbSessionId);
    
    /**
	 * <p>Retrieve an instance of NoticeboardContent with the given
     * tool content id <code>nbContentId</code> </p>
     * @param nbContentId The tool content id
     * @return an instance of NoticeboardContent
     */
	public NoticeboardContent retrieveNoticeboard(Long nbContentId);
	
	
	 /**
     * <p>Update the given persistent instance of NoticeboardContent.</p>
     * 
     * @param nbContent The instance of NoticeboardContent to persist.
     */
	public void updateNoticeboard(NoticeboardContent nbContent);
	
	
	/**
	 * <p>Persist the given persistent instance of NoticeboardContent.</p>
	 * 
	 * @param nbContent The instance of NoticeboardContent to persist.
	 */
	public void saveNoticeboard(NoticeboardContent nbContent);
	
	
	 /**
     * <p>Deletes all instances of NoticeboardSession that are associated
     * with the given instance of NoticeboardContent</p>
     * 
     * @param nbContent The instance of NoticeboardContent in which corresponding instances of NoticeboardSession should be deleted.
     */
	public void removeNoticeboardSessions(NoticeboardContent nbContent);
	
	/**
     * <p>Delete the given instance of NoticeboardContent with the
     * given tool content id <code>nbContentId</code>
     * 
     * @param nbContentId The tool content Id. 
     */
	public void removeNoticeboard(Long nbContentId);
	
	/**
     * <p>Delete the given instance of NoticeboardContent</p>
     * 
     * @param nbContent The instance of NoticeboardContent to delete. 
     */
    public void removeNoticeboard(NoticeboardContent nbContent);
	
	
    //===================================================================
    // NoticeboardSession access methods
    //===================================================================
    /**
	 * <p> Return the persistent instance of a NoticeboardSession
	 * with the given tool session id <code>nbSessionId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param nbSessionId The tool session id
	 * @return the persistent instance of a NoticeboardSession or null if not found.
	 */
	public NoticeboardSession retrieveNoticeboardSession(Long nbSessionId);
	
	/**
	 * <p>Return the persistent instance of a NoticeboardSession  
	 * with the given identifier <code>uid</code>, returns null if not found. </p>
	 * 
	 * @param uid an identifier for the NoticeboardSession object.
	 * @return the persistent instance of a NoticeboardSession or null if not found
	 */
	public NoticeboardSession retrieveNoticeboardSessionByUID(Long uid);
	
	/**
	 * Persists the new NoticeboardSession object into the database.
	 * 
	 * @param nbSession the NoticeboardSession object to persist
	 */
	public void saveNoticeboardSession(NoticeboardSession nbSession);
	
	
	/**
	 * Updates the values of the noticeboard session.
	 * @param nbSession
	 */
	public void updateNoticeboardSession(NoticeboardSession nbSession);
	
	/**
	 * Remove the noticeboard session object with session id of
	 * that specified in the argument.
	 * 
	 * @param nbSessionId The id of the requested noticeboard object
	 *
	 */	
	public void removeSession(Long nbSessionId);
	
	 /**
     * <p>Delete the given instance of NoticeboardSession</p>
     * 
     * @param nbSession The instance of NoticeboardSession to delete. 
     */
    public void removeSession(NoticeboardSession nbSession);
    
    /**
     * <p>Delete the given instance of NoticeboardSession with the
     * given identifier <code>uid</code>
     * 
     * @param uid an identifier for the NoticeboardSession instance. 
     */
    public void removeSessionByUID(Long uid);
}
