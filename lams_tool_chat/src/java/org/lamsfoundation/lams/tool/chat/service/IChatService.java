/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General License as published by
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

import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatCondition;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.lamsfoundation.lams.tool.chat.util.ChatMessageFilter;
import org.lamsfoundation.lams.tool.service.ICommonToolService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the Chat Service
 */
public interface IChatService extends ICommonToolService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     *
     * @params newContentID
     * @return
     */
    Chat copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the Chat tools default content.
     *
     * @return
     */
    Chat getDefaultContent();

    /**
     * @param toolContentID
     * @return
     */
    Chat getChatByContentId(Long toolContentID);

    /**
     * @param chat
     */
    void saveOrUpdateChat(Chat chat);
    
    String finishToolSession(Long userUid);

    /**
     * @param toolSessionId
     * @return
     */
    ChatSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param chatSession
     */
    void saveOrUpdateChatSession(ChatSession chatSession);

    List<ChatUser> getUsersActiveBySessionId(Long toolSessionId);

    /**
     *
     * @param userId
     * @param toolSessionId
     * @return
     */
    ChatUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     *
     * @param loginName
     * @param sessionID
     * @return
     */
    ChatUser getUserByLoginNameAndSessionId(String loginName, Long sessionId);

    /**
     *
     * @param uid
     * @return
     */
    ChatUser getUserByUID(Long uid);

    /**
     *
     * @param nickname
     * @param sessionID
     * @return
     */
    ChatUser getUserByNicknameAndSessionID(String nickname, Long sessionID);

    /**
     * Get how many post of this user post in a special session. DOES NOT include posts from author.
     *
     * @param userID
     * @param sessionId
     * @return
     */
    int getTopicsNum(Long userID, Long sessionId);

    void updateUserPresence(Long toolSessionId, Set<String> activeUsers);

    /**
     *
     * @param chatUser
     */
    void saveOrUpdateChatUser(ChatUser chatUser);

    /**
     *
     * @param chatUser
     * @return
     */
    List<ChatMessage> getMessagesForUser(ChatUser chatUser);

    /**
     *
     * @param chatMessage
     */
    void saveOrUpdateChatMessage(ChatMessage chatMessage);

    /**
     *
     * @param user
     * @param chatSession
     * @return
     */
    ChatUser createChatUser(UserDTO user, ChatSession chatSession);

    /**
     *
     * @param toolContentID
     * @param pattern
     */
    ChatMessageFilter updateMessageFilters(Chat chat);

    String filterMessage(String message, Chat chat);

    /**
     *
     * @param messageUID
     * @return
     */
    ChatMessage getMessageByUID(Long messageUID);

    List<ChatMessage> getLastestMessages(ChatSession chatSession, Integer max, boolean orderAsc);

    void auditEditMessage(ChatMessage chatMessage, String messageBody);

    void auditHideShowMessage(ChatMessage chatMessage, boolean messageHidden);

    Map<Long, Integer> getMessageCountBySession(Long chatUID);

    Map<Long, Integer> getMessageCountByFromUser(Long sessionUID);

    String createConditionName(Collection<ChatCondition> existingConditions);

    void deleteCondition(ChatCondition condition);

    /**
     * Gets all messages sent by the given user.
     *
     * @param userUid
     *            UID of the user
     * @return list of his/hers messages
     */
    List<ChatMessage> getMessagesSentByUser(Long userUid);

    void releaseConditionsFromCache(Chat chat);
}