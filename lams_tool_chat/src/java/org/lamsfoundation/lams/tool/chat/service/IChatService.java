/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.chat.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.util.ChatMessageFilter;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Chat Service
 */
public interface IChatService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    public Chat copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Chat tools default content.
     *
     * @return
     */
    public Chat getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public Chat getChatByContentId(Long toolContentID);

    /**
     * @param chat
     */
    public void saveOrUpdateChat(Chat chat);

    /**
     * @param toolSessionId
     * @return
     */
    public ChatSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param chatSession
     */
    public void saveOrUpdateChatSession(ChatSession chatSession);

    public List<ChatUser> getUsersActiveBySessionId(Long toolSessionId);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    public ChatUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param loginName
     * @param sessionID
     * @return
     */
    public ChatUser getUserByLoginNameAndSessionId(String loginName, Long sessionId);

    /**
     *
     * @param uid
     * @return
     */
    public ChatUser getUserByUID(Long uid);

    /**
     *
     * @param nickname
     * @param sessionID
     * @return
     */
    public ChatUser getUserByNicknameAndSessionID(String nickname, Long sessionID);

    /**
     * Get how many post of this user post in a special session. DOES NOT include posts from author.
     *
     * @param userID
     * @param sessionId
     * @return
     */
    int getTopicsNum(Long userID, Long sessionId);

    public void updateUserPresence(Long toolSessionId, Set<String> activeUsers);

    /**
     *
     * @param chatUser
     */
    public void saveOrUpdateChatUser(ChatUser chatUser);

    /**
     *
     * @param chatUser
     * @return
     */
    public List<ChatMessage> getMessagesForUser(ChatUser chatUser);

    /**
     *
     * @param chatMessage
     */
    public void saveOrUpdateChatMessage(ChatMessage chatMessage);

    /**
     *
     * @param user
     * @param chatSession
     * @return
     */
    public ChatUser createChatUser(UserDTO user, ChatSession chatSession);

    /**
     *
     * @param toolContentID
     * @param pattern
     */
    public ChatMessageFilter updateMessageFilters(Chat chat);

    public String filterMessage(String message, Chat chat);

    /**
     *
     * @param messageUID
     * @return
     */
    public ChatMessage getMessageByUID(Long messageUID);

    public List<ChatMessage> getLastestMessages(ChatSession chatSession, Integer max, boolean orderAsc);

    public void auditEditMessage(ChatMessage chatMessage, String messageBody);

    public void auditHideShowMessage(ChatMessage chatMessage, boolean messageHidden);

    public Map<Long, Integer> getMessageCountBySession(Long chatUID);

    public Map<Long, Integer> getMessageCountByFromUser(Long sessionUID);

    public Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    public NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    public void updateEntry(NotebookEntry notebookEntry);

    public String createConditionName(Collection<ChatCondition> existingConditions);

    public void deleteCondition(ChatCondition condition);

    /**
     * Gets all messages sent by the given user.
     *
     * @param userUid
     *            UID of the user
     * @return list of his/hers messages
     */
    public List<ChatMessage> getMessagesSentByUser(Long userUid);

    void releaseConditionsFromCache(Chat chat);

    boolean isGroupedActivity(long toolContentID);
    
    /**
     * Audit log the teacher has started editing activity in monitor.
     * 
     * @param toolContentID
     */
    void auditLogStartEditingActivityInMonitor(long toolContentID);
}