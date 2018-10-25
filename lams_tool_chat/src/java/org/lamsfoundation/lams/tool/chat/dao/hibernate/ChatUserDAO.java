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

package org.lamsfoundation.lams.tool.chat.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatUserDAO;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.springframework.stereotype.Repository;

/**
 * DAO for accessing the ChatUser objects - Hibernate specific code.
 */
@Repository
public class ChatUserDAO extends LAMSBaseDAO implements IChatUserDAO {

    public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + ChatUser.class.getName() + " as f"
	    + " where user_id=? and f.chatSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from " + ChatUser.class.getName()
	    + " as f where login_name=? and f.chatSession.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_UID = "from " + ChatUser.class.getName() + " where uid=?";

    private static final String SQL_QUERY_FIND_BY_NICKNAME_AND_SESSION = "from " + ChatUser.class.getName()
	    + " as f where nickname=? and f.chatSession.sessionId=?";

    public static final String SQL_QUERY_FIND_BY_SESSION_ID_AND_TIME = "from " + ChatUser.class.getName() + " as f"
	    + " where f.chatSession.sessionId=? and f.lastPresence > ?";

    @Override
    public ChatUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
	List list = doFind(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID, new Object[] { userId, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (ChatUser) list.get(0);
    }

    @Override
    public ChatUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

	List list = doFind(SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID, new Object[] { loginName, toolSessionId });

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (ChatUser) list.get(0);

    }

    @Override
    public void saveOrUpdate(ChatUser chatUser) {
	getSession().saveOrUpdate(chatUser);
	getSession().flush();
    }

    @Override
    public ChatUser getByUID(Long uid) {
	List list = doFind(SQL_QUERY_FIND_BY_UID, new Object[] { uid });
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (ChatUser) list.get(0);
    }

    @Override
    public ChatUser getByNicknameAndSessionID(String nickname, Long sessionID) {
	List list = doFind(SQL_QUERY_FIND_BY_NICKNAME_AND_SESSION, new Object[] { nickname, sessionID });
	if (list == null || list.isEmpty()) {
	    return null;
	}
	return (ChatUser) list.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ChatUser> getBySessionIdAndLastPresence(Long toolSessionID, Date oldestLastPresence) {
	return (List<ChatUser>) doFind(SQL_QUERY_FIND_BY_SESSION_ID_AND_TIME,
		new Object[] { toolSessionID, oldestLastPresence });
    }
}
