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
package org.lamsfoundation.lams.tool.forum.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Attachment;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumCondition;
import org.lamsfoundation.lams.tool.forum.persistence.ForumReport;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.persistence.PersistenceException;
import org.lamsfoundation.lams.tool.forum.persistence.Timestamp;
import org.lamsfoundation.lams.util.audit.IAuditService;

/**
 * User: conradb Date: 8/06/2005 Time: 14:49:59
 */
public interface IForumService {
    // ************************************************************************************
    // Forum Method
    // ************************************************************************************
    /**
     * Create a Forum instance according to the default content. <BR>
     * Note, this new instance won't save into database until called persist method.
     * 
     * @param contentID
     * @return
     */
    public Forum getDefaultContent(Long contentID);

    /**
     * Update forum by given <code>Forum</code>. If forum does not exist, the create a new forum.
     * 
     * @param forum
     * @return
     * @throws PersistenceException
     */
    public Forum updateForum(Forum forum) throws PersistenceException;

    /**
     * Upload instruction file
     * 
     * @param file
     * @param type
     * @return
     * @throws PersistenceException
     */
    public Attachment uploadInstructionFile(FormFile file, String type) throws PersistenceException;

    /**
     * Get forum by forum UID
     * 
     * @param forumUid
     * @return
     * @throws PersistenceException
     */
    public Forum getForum(Long forumUid) throws PersistenceException;

    /**
     * Get forum by forum ID(not record UID)
     * 
     * @param contentID
     * @return
     * @throws PersistenceException
     */
    public Forum getForumByContentId(Long contentID) throws PersistenceException;

    /**
     * Delete authoring page instruction files.
     * 
     * @param attachmentId
     * @throws PersistenceException
     */
    public void deleteForumAttachment(Long attachmentId) throws PersistenceException;

    // ************************************************************************************
    // Topic Method
    // ************************************************************************************
    
    /**
     * Get number of new postings.
     * 
     * @param messageId
     * @param userId
     * @return 
     */
    public int getNewMessagesNum(Long messageId, Long userId);
    
    /**
     * Get last topic date.
     * 
     * @param messageId
     * @return 
     */
    public Date getLastTopicDate(Long messageId);
    
    /**
     * Get timestamp.
     * 
     * @param messageId
     * @param forumUserId
     * @return 
     */
    public Timestamp getTimestamp(Long MessageId, Long forumUserId);
    
    /**
     * Save timestamp.
     * 
     * @param timestamp
     * @return 
     */
    public void saveTimestamp(Timestamp timestamp);
    
    /**
     * Create a root topic.
     * 
     * @param forumId
     * @param sessionId
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message createRootTopic(Long forumId, Long sessionId, Message message) throws PersistenceException;

    /**
     * Update a topic by give <code>Message</code> instance.
     * 
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message updateTopic(Message message) throws PersistenceException;

    /**
     * Hide a message by given <code>Message</code> instance
     * 
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message updateMessageHideFlag(Long messageId, boolean hideFlag) throws PersistenceException;

    /**
     * Reply a topic.
     * 
     * @param parentId
     * @param sessionId
     *                ToolSessionID
     * @param message
     * @return
     * @throws PersistenceException
     */
    public Message replyTopic(Long parentId, Long sessionId, Message message) throws PersistenceException;

    /**
     * Delete the topic by given topic ID. The function will delete all children topics under this topic.
     * 
     * @param topicId
     * @throws PersistenceException
     */
    public void deleteTopic(Long topicId) throws PersistenceException;

    /**
     * Upload message attachment file into repository. This method only upload the given file into system repository. It
     * does not execute any database operation.
     * 
     * @param file
     * @return Attachment A new instance of attachment has uploaded file VersionID and UUID information.
     * @throws PersistenceException
     */
    public Attachment uploadAttachment(FormFile file) throws PersistenceException;

    /**
     * Delete file from repository.
     * 
     * @param uuID
     * @param versionID
     * @throws PersistenceException
     */
    public void deleteFromRepository(Long uuID, Long versionID) throws PersistenceException;

    // ************************************************************************************
    // *********************Get topic methods **********************
    // ************************************************************************************
    /**
     * Get topic and its children list by given root topic ID. Note that the return type is DTO.
     * 
     * @param rootTopicId
     * @return List of MessageDTO
     */
    public List getTopicThread(Long rootTopicId);

    /**
     * Get root topics by a given sessionID value. Simultanousely, it gets back topics, which author posted in authoring
     * page for this forum, which is related with the given sessionID value.
     * 
     * This method will used by user to display initial topic page for a forum.
     * 
     * Note that the return type is DTO.
     * 
     * @param sessionId
     * @return List of MessageDTO
     */
    public List<MessageDTO> getRootTopics(Long sessionId);

    /**
     * Get topics posted by author role. Note that the return type is DTO.
     * 
     * @return List of MessageDTO
     */
    public List getAuthoredTopics(Long forumId);

    /**
     * This method will look up root topic ID by any level topicID.
     * 
     * @param topicId
     * @return
     */
    public Long getRootTopicId(Long topicId);

    /**
     * Get message by given message UID
     * 
     * @param messageUid
     * @return Message
     * @throws PersistenceException
     */
    public Message getMessage(Long messageUid) throws PersistenceException;

    /**
     * Get message list posted by given user. Note that the return type is DTO.
     * 
     * @param userId
     * @return
     */
    public List getMessagesByUserUid(Long userId, Long sessionId);

    /**
     * Get how many post of this user post in a special session. DOES NOT include posts from author.
     * 
     * @param userID
     * @param sessionId
     * @return
     */
    public int getTopicsNum(Long userID, Long sessionId);

    /**
     * Returns the number of posts this user has made in this topic.
     * 
     * @param userId
     * @param topicId
     * @return
     */
    public int getNumOfPostsByTopic(Long userId, Long topicId);

    // ************************************************************************************
    // Session Method
    // ************************************************************************************
    /**
     * Get Forum tool session by Session ID (not record UID).
     * 
     * @param sessionId
     * @return
     */
    public ForumToolSession getSessionBySessionId(Long sessionId);

    /**
     * Get session list according to content ID.
     * 
     * @param contentID
     * @return List
     */
    public List getSessionsByContentId(Long contentID);

    /**
     * Get all message according to the given session ID.
     * 
     * @param sessionID
     * @return
     */
    public List<MessageDTO> getAllTopicsFromSession(Long sessionID);

    // ************************************************************************************
    // User Method
    // ************************************************************************************
    /**
     * Create a new user in database.
     * 
     * @param forumUser
     */
    public void createUser(ForumUser forumUser);

    /**
     * Get user by user ID (not record UID).
     * 
     * @param userId
     * @return
     */
    public ForumUser getUserByUserAndSession(Long userId, Long sessionId);

    /**
     * Get user list by given session ID.
     * 
     * @param sessionID
     * @return
     */
    public List getUsersBySessionId(Long sessionID);

    /**
     * Get user by uid
     * 
     * @param userUid
     * @return
     */
    public ForumUser getUser(Long userUid);

    /**
     * Get user by user ID
     * 
     * @param userId
     * @return
     */
    public ForumUser getUserByID(Long userId);

    /**
     * Update forum message report.
     * 
     * @param report
     */
    public void updateReport(ForumReport report);

    // ************************************************************************************
    // Report Method
    // ************************************************************************************

    // ************************************************************************************
    // Miscellaneous Method
    // ************************************************************************************
    public void releaseMarksForSession(Long sessionID);

    /** The topic updates (for monitoring) are done in the web layer, so need the audit service to log the updates */
    public IAuditService getAuditService();

    /**
     * Mark user completing a session.
     * 
     * @param currentUser
     */
    public void finishUserSession(ForumUser currentUser);

    /**
     * Create refection entry into notebook tool.
     * 
     * @param sessionId
     * @param notebook_tool
     * @param tool_signature
     * @param userId
     * @param entryText
     */
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText);

    /**
     * Get reflection entry from notebook tool.
     * 
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

    public IEventNotificationService getEventNotificationService();

    /**
     * Gets a message from resource bundle. Same as <code><fmt:message></code> in JSP pages.
     * 
     * @param key
     *                key of the message
     * @param args
     *                arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    /**
     * Creates an unique name for a ForumCondition. It consists of the tool output definition name and a unique positive
     * integer number.
     * 
     * @param existingConditions
     *                existing conditions; required to check if a condition with the same name does not exist.
     * @return unique ForumCondition name
     */
    public String createTextSearchConditionName(Collection<ForumCondition> existingConditions);

    public void deleteCondition(ForumCondition condition) throws PersistenceException;
    
    void sendNotificationsOnNewPosting(Long forumId, Long sessionId, Message message);
    
    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     * 
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
}
