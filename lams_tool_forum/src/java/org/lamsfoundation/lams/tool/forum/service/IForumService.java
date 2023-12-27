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

package org.lamsfoundation.lams.tool.forum.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.forum.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.model.Attachment;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.lamsfoundation.lams.tool.forum.model.ForumConfigItem;
import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.model.ForumUser;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.model.MessageSeq;
import org.lamsfoundation.lams.tool.forum.util.PersistenceException;
import org.lamsfoundation.lams.tool.service.ICommonToolService;

/**
 * User: conradb Date: 8/06/2005 Time: 14:49:59
 */
public interface IForumService extends ICommonToolService {
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
    Forum getDefaultContent(Long contentID);

    /**
     * Update forum by given <code>Forum</code>. If forum does not exist, the create a new forum.
     *
     * @param forum
     * @return
     * @throws PersistenceException
     */
    Forum updateForum(Forum forum) throws PersistenceException;

    /**
     * Get forum by forum UID
     *
     * @param forumUid
     * @return
     * @throws PersistenceException
     */
    Forum getForum(Long forumUid) throws PersistenceException;

    /**
     * Get forum by forum ID(not record UID)
     *
     * @param contentID
     * @return
     * @throws PersistenceException
     */
    Forum getForumByContentId(Long contentID) throws PersistenceException;

    List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString);

    int getCountUsersBySession(Long sessionId, String searchString);

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
    int getNewMessagesNum(Message message, Long userId);

    /**
     * Saves timestamp
     *
     * @param rootTopicId
     * @param forumUser
     */
    void saveTimestamp(Long rootTopicId, ForumUser forumUser);

    /**
     * Create a root topic.
     *
     * @param forumId
     * @param sessionId
     * @param message
     * @return
     * @throws PersistenceException
     */
    Message createRootTopic(Long forumId, Long sessionId, Message message) throws PersistenceException;

    /**
     * Update a topic by give <code>Message</code> instance.
     *
     * @param message
     * @return
     * @throws PersistenceException
     */
    Message updateTopic(Message message) throws PersistenceException;

    /**
     * Hide a message by given <code>Message</code> instance
     *
     * @param message
     * @return
     * @throws PersistenceException
     */
    Message updateMessageHideFlag(Long messageId, boolean hideFlag) throws PersistenceException;

    /**
     * Reply a topic.
     *
     * @param parentId
     * @param sessionId
     *            ToolSessionID
     * @param message
     * @return
     * @throws PersistenceException
     */
    MessageSeq replyTopic(Long parentId, Long sessionId, Message message) throws PersistenceException;

    /**
     * Delete the topic by given topic ID. The function will delete all children topics under this topic.
     *
     * @param topicId
     * @throws PersistenceException
     */
    void deleteTopic(Long topicId) throws PersistenceException;

    /**
     * Upload message attachment file into repository. This method only upload the given file into system repository. It
     * does not execute any database operation.
     *
     * @param file
     * @return Attachment A new instance of attachment has uploaded file VersionID and UUID information.
     * @throws PersistenceException
     */
    Attachment uploadAttachment(File file) throws PersistenceException;

    // ************************************************************************************
    // *********************Get topic methods **********************
    // ************************************************************************************
    /**
     * Get a complete topic and its children list by given root topic ID. Note that the return type is DTO.
     *
     * @param rootTopicId
     * @return List of MessageDTO
     */
    List<MessageDTO> getTopicThread(Long rootTopicId);

    /**
     * Get topic and its children list by given root topic ID, starting from after the sequence number specified.
     * Return the number of entries indicated by the paging number. Note that the return type is DTO.
     *
     * @param rootTopicId
     * @param afterSequenceId
     * @param pagingSize
     * @return List of MessageDTO
     */
    public List getTopicThread(Long rootTopicId, Long afterSequenceId, Long pagingSize);

    /**
     * Get one complete thread within a topic Note that the return type is DTO.
     *
     * @param threadId
     * @return List of MessageDTO
     */
    public List getThread(Long threadId);

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
    List<MessageDTO> getRootTopics(Long sessionId);

    /**
     * Get topics posted by author role. Note that the return type is DTO.
     *
     * @return List of MessageDTO
     */
    List<MessageDTO> getAuthoredTopics(Long forumId);

    /**
     * This method will look up root topic ID by any level topicID.
     *
     * @param topicId
     * @return
     */
    Long getRootTopicId(Long topicId);

    /**
     * Get message by given message UID
     *
     * @param messageUid
     * @return Message
     * @throws PersistenceException
     */
    Message getMessage(Long messageUid) throws PersistenceException;

    /**
     * Get message by given message UID, wrapped up in the usual DTO list that is used for the view code in learner.
     *
     * @param messageUid
     * @return Message
     * @throws PersistenceException
     */
    List<MessageDTO> getMessageAsDTO(Long messageUid) throws PersistenceException;

    /**
     * Get message list posted by given user. Note that the return type is DTO.
     *
     * @param userId
     * @return
     */
    List<MessageDTO> getMessagesByUserUid(Long userId, Long sessionId);

    /**
     * Get how many post of this user post in a special session. DOES NOT include posts from author.
     *
     * @param userID
     * @param sessionId
     * @return
     */
    int getTopicsNum(Long userID, Long sessionId);

    /**
     * Returns the number of posts this user has made in this topic.
     *
     * @param userId
     * @param topicId
     * @return
     */
    int getNumOfPostsByTopic(Long userId, Long topicId);

    // ************************************************************************************
    // Session Method
    // ************************************************************************************
    /**
     * Get Forum tool session by Session ID (not record UID).
     *
     * @param sessionId
     * @return
     */
    ForumToolSession getSessionBySessionId(Long sessionId);

    /**
     * Get session list according to content ID.
     *
     * @param contentID
     * @return List
     */
    List<ForumToolSession> getSessionsByContentId(Long contentID);

    /**
     * Get all message according to the given session ID.
     *
     * @param sessionID
     * @return
     */
    List<MessageDTO> getAllTopicsFromSession(Long sessionID);

    /** From ToolSessionManager interface */
    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    // ************************************************************************************
    // User Method
    // ************************************************************************************
    /**
     * Create a new user in database.
     *
     * @param forumUser
     */
    void createUser(ForumUser forumUser);

    /**
     * Get user by user ID (not record UID).
     *
     * @param userId
     * @return
     */
    ForumUser getUserByUserAndSession(Long userId, Long sessionId);

    /**
     * Get user list by given session ID.
     *
     * @param sessionID
     * @return
     */
    List<ForumUser> getUsersBySessionId(Long sessionID);

    /**
     * Get user by uid
     *
     * @param userUid
     * @return
     */
    ForumUser getUser(Long userUid);

    /**
     * Get user by user ID
     *
     * @param userId
     * @return
     */
    ForumUser getUserByID(Long userId);

    /**
     * Update mark and mark comment. Send marks to gradebook, if marks are released for that session
     *
     * @param message
     *            specified message
     */
    void updateMark(Message message);

    // ************************************************************************************
    // Report Method
    // ************************************************************************************

    // ************************************************************************************
    // Miscellaneous Method
    // ************************************************************************************
    void releaseMarksForSession(Long sessionID);

    /** The topic updates (for monitoring) are done in the web layer, so need the audit service to log the updates */
    ILogEventService getLogEventService();

    /**
     * Mark user completing a session.
     *
     * @param currentUser
     */
    void finishUserSession(ForumUser currentUser);


    IEventNotificationService getEventNotificationService();

    /**
     * Gets a message from resource bundle. Same as <code><fmt:message></code> in JSP pages.
     *
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    /**
     * Creates an unique name for a ForumCondition. It consists of the tool output definition name and a unique positive
     * integer number.
     *
     * @param existingConditions
     *            existing conditions; required to check if a condition with the same name does not exist.
     * @return unique ForumCondition name
     */
    String createTextSearchConditionName(Collection<ForumCondition> existingConditions);

    void deleteCondition(ForumCondition condition) throws PersistenceException;

    void sendNotificationsOnNewPosting(Long forumId, Long sessionId, Message message);

    /**
     * Return content folder (unique to each learner and lesson) which is used for storing user generated content.
     * It's been used by CKEditor.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String getLearnerContentFolder(Long toolSessionId, Long userId);

    AverageRatingDTO rateMessage(Long messageId, Long userId, Long toolSessionID, float rating);

    AverageRatingDTO getAverageRatingDTOByMessage(Long responseId);

    /**
     * Return total number of posts done by current user in this forum activity
     *
     * @param userUid
     * @param forumUid
     * @return
     */
    int getNumOfRatingsByUserAndForum(Long userUid, Long forumUid);

    ForumConfigItem getConfigItem(String key);

    void saveForumConfigItem(ForumConfigItem item);
}