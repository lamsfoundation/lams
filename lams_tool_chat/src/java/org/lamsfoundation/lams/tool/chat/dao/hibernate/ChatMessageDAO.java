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

package org.lamsfoundation.lams.tool.chat.dao.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatMessageDAO;
import org.lamsfoundation.lams.tool.chat.model.ChatMessage;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;
import org.springframework.stereotype.Repository;

@Repository
public class ChatMessageDAO extends LAMSBaseDAO implements IChatMessageDAO {

    protected final Log logger = LogFactory.getLog(getClass());

    // public static final String SQL_QUERY_FIND_USER_MESSAGE_HISTORY = "from "
    // + ChatMessage.class.getName() + " as f where "
    // + "f.chatSession=? and (f.type='groupchat' or "
    // + "(f.type='chat' and (f.fromUser.userId=? or f.toUser.userId=?)))";

    public static final String SQL_QUERY_FIND_USER_MESSAGE_HISTORY = "from " + ChatMessage.class.getName()
	    + " as f where "
	    + "f.chatSession.uid=? and f.hidden='false' and (f.type='groupchat' or (f.type='chat' and (f.fromUser.uid=? or f.toUser.uid=?)))";

    public static final String SQL_QUERY_FIND_MESSAGE_BY_UID = "from " + ChatMessage.class.getName() + " where uid=?";

    public static final String SQL_QUERY_FIND_MESSAGE_BY_SESSION_ORDER_BY_DATE = "from " + ChatMessage.class.getName()
	    + " as f where f.chatSession.uid=:chatSessionUid order by f.sendDate ";

    public static final String SQL_QUERY_FIND_MESSAGE_COUNT_BY_FROM_USER = "select f.fromUser.uid, count(*) from "
	    + ChatMessage.class.getName() + " as f where f.chatSession.uid=? group by f.fromUser";

    public static final String SQL_QUERY_FIND_MESSAGE_COUNT_BY_SESSION = "select f.chatSession.uid, count(*) from "
	    + ChatMessage.class.getName() + " as f where f.chatSession.chat.uid=? group by f.chatSession";

    public static final String SQL_QUERY_FIND_MESSAGES_SENT_BY_USER = "FROM " + ChatMessage.class.getName()
	    + " AS f WHERE f.fromUser.uid=?";
    private static final String SQL_QUERY_TOPICS_NUMBER_BY_USER_SESSION = "select count(*) from "
	    + ChatMessage.class.getName() + " m " + " where m.fromUser.userId=? and m.chatSession.sessionId=?";

    @Override
    public void saveOrUpdate(ChatMessage chatMessage) {
	getSession().saveOrUpdate(chatMessage);
	getSession().flush();
    }

    @Override
    public List getForUser(ChatUser chatUser) {
	return doFind(ChatMessageDAO.SQL_QUERY_FIND_USER_MESSAGE_HISTORY,
		new Object[] { chatUser.getChatSession().getUid(), chatUser.getUid(), chatUser.getUid() });
    }

    @Override
    public ChatMessage getByUID(Long uid) {
	// TODO Auto-generated method stub
	List list = doFind(ChatMessageDAO.SQL_QUERY_FIND_MESSAGE_BY_UID, new Object[] { uid });

	if ((list != null) && (list.size() > 0)) {
	    return (ChatMessage) list.get(0);
	} else {
	    return null;
	}

    }

    @Override
    public List getLatest(ChatSession chatSession, Integer max, boolean orderAsc) {
	try {
	    Query query = getSessionFactory().getCurrentSession().createQuery(
		    ChatMessageDAO.SQL_QUERY_FIND_MESSAGE_BY_SESSION_ORDER_BY_DATE + (orderAsc ? "asc" : "desc"));
	    query.setParameter("chatSessionUid", chatSession.getUid());
	    if (max != null) {
		query.setMaxResults(max);
	    }
	    return query.list();
	} catch (HibernateException he) {
	    logger.error("getLatest: hibernate exception");
	    return null;
	}
    }

    @Override
    public Map<Long, Integer> getCountBySession(Long chatUID) {
	List list = doFind(ChatMessageDAO.SQL_QUERY_FIND_MESSAGE_COUNT_BY_SESSION, new Object[] { chatUID });

	Map<Long, Integer> resultMap = new HashMap<Long, Integer>();
	for (Iterator iter = list.iterator(); iter.hasNext();) {
	    Object[] row = (Object[]) iter.next();
	    resultMap.put((Long) row[0], ((Number) row[1]).intValue());
	}
	return resultMap;
    }

    @Override
    public Map<Long, Integer> getCountByFromUser(Long sessionUID) {
	List list = doFind(ChatMessageDAO.SQL_QUERY_FIND_MESSAGE_COUNT_BY_FROM_USER, new Object[] { sessionUID });

	Map<Long, Integer> resultMap = new HashMap<Long, Integer>();
	for (Iterator iter = list.iterator(); iter.hasNext();) {
	    Object[] row = (Object[]) iter.next();
	    resultMap.put((Long) row[0], ((Number) row[1]).intValue());
	}
	return resultMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ChatMessage> getSentByUser(Long userUid) {
	return (List<ChatMessage>) doFind(ChatMessageDAO.SQL_QUERY_FIND_MESSAGES_SENT_BY_USER,
		new Object[] { userUid });
    }

    @Override
    public int getTopicsNum(Long userID, Long sessionId) {
	List list = this.doFind(SQL_QUERY_TOPICS_NUMBER_BY_USER_SESSION, new Object[] { userID, sessionId });
	if ((list != null) && (list.size() > 0)) {
	    return ((Number) list.get(0)).intValue();
	} else {
	    return 0;
	}
    }
}
