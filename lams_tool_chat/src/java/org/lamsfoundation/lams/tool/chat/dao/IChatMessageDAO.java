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

package org.lamsfoundation.lams.tool.chat.dao;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;

/**
 * DAO for accessing the ChatMessage objects - interface defining methods to be implemented by the Hibernate or other
 * implementation.
 */
public interface IChatMessageDAO extends IBaseDAO {

    void saveOrUpdate(ChatMessage chatMessage);

    List getForUser(ChatUser chatUser);

    ChatMessage getByUID(Long uid);

    List getLatest(ChatSession chatSession, Integer max, boolean orderAsc);

    Map<Long, Integer> getCountBySession(Long chatUID);

    Map<Long, Integer> getCountByFromUser(Long sessionUID);

    List<ChatMessage> getSentByUser(Long userUid);

    /**
     * Return how many post from this user and session. DOES NOT include posts from author.
     *
     * @param userID
     * @param sessionId
     * @return
     */
    int getTopicsNum(Long userID, Long sessionId);
}