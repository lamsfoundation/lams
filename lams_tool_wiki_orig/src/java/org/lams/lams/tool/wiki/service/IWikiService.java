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
package org.lams.lams.tool.wiki.service;

import java.io.InputStream;
import java.util.List;

import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lams.lams.tool.wiki.WikiAttachment;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.WikiSession;
import org.lams.lams.tool.wiki.WikiUser;


/**
 * Defines the contract that the tool service provider must follow
 * 
 * @author mtruong
 */
public interface IWikiService {
	
    //===================================================================
    // WikiContent access methods
    //===================================================================	
    
  
    
    /**
     * <p> Returns the persistent instance of WikiContent
     * with the given tool session id <code>wikiSessionId</code>, returns null if not found.
     * 
     * @param wikiSessionId The tool session id
     * @return a persistent instance of WikiContent or null if not found.
     */	
	public WikiContent retrieveWikiBySessionID(Long wikiSessionId);
    
    /**
	 * <p>Retrieve an instance of WikiContent with the given
     * tool content id <code>wikiContentId</code> </p>
     * @param wikiContentId The tool content id
     * @return an instance of WikiContent
     */
	public WikiContent retrieveWiki(Long wikiContentId);
	
	
	/**
	 * <p>Persist/Update the given persistent instance of WikiContent.</p>
	 * 
	 * @param wikiContent The instance of WikiContent to persist.
	 */
	public void saveWiki(WikiContent wikiContent);
	
	
	 /**
     * <p>Deletes all instances of WikiSession that are associated
     * with the given instance of WikiContent</p>
     * 
     * @param wikiContent The instance of WikiContent in which corresponding instances of WikiSession should be deleted.
     */
	public void removeWikiSessionsFromContent(WikiContent wikiContent);
	
	/**
     * <p>Delete the given instance of WikiContent with the
     * given tool content id <code>wikiContentId</code>
     * 
     * @param wikiContentId The tool content Id. 
     */
	public void removeWiki(Long wikiContentId);
	
	/**
     * <p>Delete the given instance of WikiContent</p>
     * 
     * @param wikiContent The instance of WikiContent to delete. 
     */
    public void removeWiki(WikiContent wikiContent);
	
	
    //===================================================================
    // WikiSession access methods
    //===================================================================
    /**
	 * <p> Return the persistent instance of a WikiSession
	 * with the given tool session id <code>wikiSessionId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param wikiSessionId The tool session id
	 * @return the persistent instance of a WikiSession or null if not found.
	 */
	public WikiSession retrieveWikiSession(Long wikiSessionId);
	

	/**
	 * Persists the new WikiSession object into the database.
	 * 
	 * @param wikiSession the WikiSession object to persist
	 */
	public void saveWikiSession(WikiSession wikiSession);
	
	
	/**
	 * Updates the values of the wiki session.
	 * @param wikiSession
	 */
	public void updateWikiSession(WikiSession wikiSession);
	
	/**
	 * Remove the wiki session object with session id of
	 * that specified in the argument.
	 * 
	 * @param wikiSessionId The id of the requested wiki object
	 *
	 */	
	public void removeSession(Long wikiSessionId);
	
	 /**
     * <p>Delete the given instance of WikiSession</p>
     * 
     * @param wikiSession The instance of WikiSession to delete. 
     */
    public void removeSession(WikiSession wikiSession);
    
     
    /**
     * <p>Deletes all instances of WikiUser that are associated
     * with the given instance of WikiSession</p>
     * 
     * @param wikiSession The instance of WikiSession in which corresponding instances of WikiUser should be deleted.
     */
	public void removeWikiUsersFromSession(WikiSession wikiSession);
	
	  /**
     * <p> Returns the persistent instance of WikiSession
     * with the given wiki user id<code>userId</code>, returns null if not found.
     * 
     * @param userId The user id
     * @return a persistent instance of WikiSession or null if not found.
     */	
	public WikiSession retrieveWikiSessionByUserID(Long userId);
    
    //===================================================================
    // WikiUser access methods
    //===================================================================
    /**
	 * <p> Return the persistent instance of a WikiUser
	 * with the given user id<code>wikiUserId</code>,
	 * returns null if not found.</p>
	 * 
	 * @param wikiUserId The user id of the instance of WikiUser
	 * @return the persistent instance of a WikiUser or null if not found.
	 */
	public WikiUser retrieveWikiUser(Long wikiUserId, Long toolSessionId);
	
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
	public WikiUser retrieveWikiUserBySession(Long userId, Long sessionId);
	
	/**
	 * Persists the new WikiUser object into the database.
	 * 
	 * @param wikiUser the WikiUser object to persist
	 */
	public void saveWikiUser(WikiUser wikiUser);
	
	
	/**
	 * Updates the values of the wiki user.
	 * @param wikiUser
	 */
	public void updateWikiUser(WikiUser wikiUser);
	
	/**
	 * Remove the wiki user object with user id of
	 * that specified in the argument.
	 * 
	 * @param wikiUserId The id of the requested wiki object
	 * @param toolSessionId The id of the wikiUser's associated wikiSession
	 *
	 */	
	public void removeUser(Long wikiUserId, Long toolSessionId);
	
	 /**
     * <p>Delete the given instance of WikiUser</p>
     * 
     * @param wikiUser The instance of WikiUser to delete. 
     */
    public void removeUser(WikiUser wikiUser);
 
    /**
     * <p> Saves the instance of WikiSession to the database. 
     * This instance is added to the collection of sessions from 
     * WikiContent with tool content id <code>wikiContentId</code> </p>
     * 
     * @param wikiContentId The tool content Id
     * @param session The instance of WikiSession to persist
     */
    public void addSession(Long wikiContentId, WikiSession session);
    
    /**
     * <p>Saves the instance of WikiUser to the database.
     * <code>wikiUser</code> is added to the collection of users from 
     * WikiSession with tool session id <code>wikiSessionId</code> </p>
     * 
     * @param wikiSessionId The tool session id
     * @param user The instance of WikiUser to persist
     */
    public void addUser(Long wikiSessionId, WikiUser wikiUser);
    
    /**
     * <p>Retrieves a list of the session IDs from the given instance of WikiContent</p> 
     * @param content 
     * @return list of session ids (Long)
     */
    public List getSessionIdsFromContent(WikiContent content);
    
    /**
     * <p>Returns the number of users in this session</p>
     * @param session
     * @return The number of users in the session
     */
    public int getNumberOfUsersInSession(WikiSession session);
    
    /**
     * <p>Finds the number of learners that have participated in this tool activity
     * with the given toolContentId. It finds all the toolSessionIds relating to this
     * toolContentId, and calculates the number of users in each tool session(group).
     * Returns the total number of users across all sessions </p>
     * @param toolContentId 
     * @return the total number of users for this tool activity
     */
    public int calculateTotalNumberOfUsers(Long toolContentId);
    
    //===================================================================
    // WikiAttachment access methods
    //===================================================================
    
    /**
	 * <p>Retrieve an instance of WikiAttachment with the given
     * attachment id <code>attachmentId</code> </p>
     * @param attachmentId The id for the attachment file
     * @return an instance of WikiAttachment
     */
    public WikiAttachment retrieveAttachment(Long attachmentId);
    
    /**
	 * <p>Retrieve the file attachment with the given uuid </p>
     * @param uuid The unique identifier for the file, corresponds to the uuid for the file stored in content repository
     * @return an instance of WikiAttachment
     */
    public WikiAttachment retrieveAttachmentByUuid(Long uuid);
    
    /**
	 * <p>Retrieve an instance of WikiAttachment with the 
     * filename <code>filename</code> </p>
     * @param filename The filename of the attachment that you want to retrieve
     * @return an instance of WikiAttachment
     */
    public WikiAttachment retrieveAttachmentByFilename(String filename);
    
    /**
     * <p>Retrieve the list of attachment ids with the given instance of WikiContent</p>
     * @param wikiContent The given instance of WikiContent
     * @return List. the list of attachment ids (java.lang.Long)
     */
    public List getAttachmentIdsFromContent(WikiContent wikiContent);
    
    /**
     * <p> Saves (persists) or update the WikiAttachment object in the
     * database.</p>
     * @param content The overall wiki content object to which the attachment is to be added
     * @param attachment The instance of WikiAttachment to save
     */
    public void saveAttachment(WikiContent content, WikiAttachment attachment);
    
    /**
     * Removes the WikiAttachment object from the database.
     * @param content The overall wiki content object to which the attachment is to be added
     * @param attachment The instance of WikiAttachment to delete.
     */
    public void removeAttachment(WikiContent content, WikiAttachment attachment) throws RepositoryCheckedException;
    
	/** 
	 * Add a file to the content repository. Does not add a record to the wiki tables.
	 * @throws RepositoryCheckedException 
	 */
	public NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType) throws RepositoryCheckedException;

    /**
     * This method retrieves the default content id.
     * @param toolSignature The tool signature which is defined in lams_tool table.
     * @return the default content id
     */
    public Long getToolDefaultContentIdBySignature(String toolSignature);
    
    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);
    
    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);
    
	public void updateEntry(NotebookEntry notebookEntry);
    
    public List getUsersBySession(Long sessionId);
}
