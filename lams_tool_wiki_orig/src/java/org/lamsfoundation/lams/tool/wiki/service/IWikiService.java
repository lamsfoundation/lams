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
package org.lamsfoundation.lams.tool.wiki.service;

import java.util.List;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.wiki.dto.MessageDTO;
import org.lamsfoundation.lams.tool.wiki.dto.UserDTO;
import org.lamsfoundation.lams.tool.wiki.persistence.Attachment;
import org.lamsfoundation.lams.tool.wiki.persistence.Wiki;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiReport;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiToolSession;
import org.lamsfoundation.lams.tool.wiki.persistence.WikiUser;
import org.lamsfoundation.lams.tool.wiki.persistence.Message;
import org.lamsfoundation.lams.tool.wiki.persistence.PersistenceException;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * User: conradb
 * Date: 8/06/2005
 * Time: 14:49:59
 */
public interface IWikiService {
	//************************************************************************************
	// Wiki Method
	//************************************************************************************
    /**
     * Create a Wiki instance according to the default content. <BR> 
     * Note, this new insstance won't save into database until called persist method.
     * 
     * @param contentID
     * @return
     */
    public Wiki getDefaultContent(Long contentID);
	/**
	 * Update wiki by given <code>Wiki</code>. If wiki does not exist, the create a new wiki.
	 * @param wiki
	 * @return
	 * @throws PersistenceException
	 */
    public Wiki updateWiki(Wiki wiki) throws PersistenceException;
    /**
     * Upload instruction file
     * @param file
     * @param type
     * @return
     * @throws PersistenceException
     */
    public Attachment uploadInstructionFile(FormFile file, String type) throws PersistenceException;
    /**
     * Get wiki by wiki UID 
     * @param wikiUid
     * @return
     * @throws PersistenceException
     */
    public Wiki getWiki(Long wikiUid) throws PersistenceException;
    /**
     * Get wiki by wiki ID(not record UID)
     * @param contentID
     * @return
     * @throws PersistenceException
     */
    public Wiki getWikiByContentId(Long contentID) throws PersistenceException;

    /**
     * Delete authoring page instruction files.
     * @param attachmentId
     * @throws PersistenceException
     */
    public void deleteWikiAttachment(Long attachmentId) throws PersistenceException;
    
	//************************************************************************************
	//Topic Method
	//************************************************************************************
    /**
     * Create a root topic.
     * @param wikiId
     * @param sessionId
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message createRootTopic(Long wikiId, Long sessionId, Message message) throws PersistenceException ;
    /**
     * Update a topic by give <code>Message</code> instance.
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message updateTopic(Message message) throws PersistenceException;
    /**
     * Hide a message by given <code>Message</code> instance
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message updateMessageHideFlag(Long messageId, boolean hideFlag) throws PersistenceException;
    /**
     * Reply a topic.
     * @param parentId
     * @param sessionId ToolSessionID
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message replyTopic(Long parentId,  Long sessionId, Message message) throws PersistenceException;

    /**
     * Delete the topic by given topic ID. The function will delete all children topics under this topic.
     * @param topicId
     * @throws PersistenceException
     */
    public void deleteTopic(Long topicId) throws PersistenceException;
	/**
	 * Upload message attachment file into repository.
	 * This method only upload the given file into system repository. It does not execute any database operation.
	 * 
	 * @param file
	 * @return Attachment 
	 * 		A new instance of attachment has uploaded file VersionID and UUID information.
	 * @throws PersistenceException
	 */
	public Attachment uploadAttachment(FormFile file) throws PersistenceException;

	/**
	 * Delete  file from repository.
	 * @param uuID
	 * @param versionID
	 * @throws PersistenceException
	 */
	public void deleteFromRepository(Long uuID, Long versionID) throws PersistenceException;
	
	//************************************************************************************
	//*********************Get topic methods **********************
	//************************************************************************************
	/** 
	 * Get topic and its children list by given root topic ID.  
	 * Note that the return type is DTO.
	 * 
	 * @param rootTopicId
	 * @return
	 * 		List of MessageDTO
	 */
	public List getTopicThread(Long rootTopicId);
	/**
	 * Get root topics by a given sessionID value. Simultanousely, it gets back topics, which author 
	 * posted in authoring page for this wiki, which is related with the given sessionID value.
	 * 
	 * This method will used by  user to display initial topic page for a wiki. 
	 * 
	 * Note that the return type is DTO.
	 * @param sessionId
	 * @return
	 * 		List of MessageDTO
	 */
	public List getRootTopics(Long sessionId);
	/**
	 * Get topics posted by author role. Note that the return type is DTO.
	 * @return
	 * 		List of MessageDTO
	 */
	public List getAuthoredTopics(Long wikiId);
	/**
	 * This method will look up root topic ID by any level topicID.
	 * @param topicId
	 * @return
	 */
	public Long getRootTopicId(Long topicId);
    /**
     * Get message by given message UID
     * @param messageUid
     * @return 
     * 		Message 
     * @throws PersistenceException
     */
    public Message getMessage(Long messageUid) throws PersistenceException;
    /**
     * Get message list posted by given user.
     * Note that the return type is DTO.
     * @param userId
     * @return
     */
    public List getMessagesByUserUid(Long userId, Long sessionId);
    /**
     * Get how many post of this user post in a special session. DOES NOT include posts from author.
     * @param userID
     * @param sessionId
     * @return
     */
	public int getTopicsNum(Long userID, Long sessionId);

	//************************************************************************************
	// Session Method
	//************************************************************************************
	/**
	 * Get Wiki tool session by Session ID (not record UID).
	 * @param sessionId
	 * @return
	 */
	public WikiToolSession getSessionBySessionId(Long sessionId);

	/**
	 * Get session list according to content ID. 
	 * @param contentID
	 * @return List
	 */
	public List getSessionsByContentId(Long contentID);
	/**
	 * Get all message according to the given session ID.
	 * @param sessionID
	 * @return
	 */
	public List<MessageDTO> getAllTopicsFromSession(Long sessionID);
	//************************************************************************************
	// User  Method
	//************************************************************************************
    /**
     * Create a new user in database.
     * @param wikiUser
     */
    public void createUser(WikiUser wikiUser);
    /**
     * Get user by user ID (not record UID).
     * @param userId
     * @return
     */
    public WikiUser getUserByUserAndSession(Long userId,Long sessionId);
    /**
     * Get user list by given session ID.
     * @param sessionID
     * @return
     */
    public List getUsersBySessionId(Long sessionID);
	/**
	 * Get user by uid
	 * @param userUid
	 * @return
	 */
	public WikiUser getUser(Long userUid);
	/**
	 * Get user by user ID
	 * @param userId
	 * @return
	 */
	public WikiUser getUserByID(Long userId);
	/**
	 * Update wiki message report.
	 * @param report
	 */
	public void updateReport(WikiReport report);
	//************************************************************************************
	// Report  Method
	//************************************************************************************
	
	//************************************************************************************
	// Miscellaneous Method
	//************************************************************************************	
	public void releaseMarksForSession(Long sessionID);
	
	/** The topic updates (for monitoring) are done in the web layer, so need the audit service to log the updates */
	public IAuditService getAuditService( );
	/**
	 * Mark user completing a session.  
	 * @param currentUser
	 */
	public void finishUserSession(WikiUser currentUser);
	/**
	 * Create refection entry into notebook tool.
	 * @param sessionId
	 * @param notebook_tool
	 * @param tool_signature
	 * @param userId
	 * @param entryText
	 */
	public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId, String entryText);
	/**
	 * Get reflection entry from notebook tool.
	 * @param sessionId
	 * @param idType
	 * @param signature
	 * @param userID
	 * @return
	 */
	public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);
	
	/**
	 * @param notebookEntry
	 */
	public void updateEntry(NotebookEntry notebookEntry);
}
