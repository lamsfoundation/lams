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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */

package org.lamsfoundation.lams.tool.chat.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.chat.dao.IChatUserDAO;
import org.lamsfoundation.lams.tool.chat.model.ChatUser;

/**
 * DAO for accessing the ChatUser objects - Hibernate specific code.
 */
public class ChatUserDAO extends BaseDAO implements IChatUserDAO {

	public static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from "
			+ ChatUser.class.getName() + " as f"
			+ " where user_id=? and f.chatSession.sessionId=?";
	
	public static final String SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID = "from "
		+ ChatUser.class.getName() + " as f"
		+ " where login_name=? and f.chatSession.sessionId=?";

	public ChatUser getByUserIdAndSessionId(Long userId, Long toolSessionId) {
		List list = this.getHibernateTemplate().find(
				SQL_QUERY_FIND_BY_USER_ID_SESSION_ID,
				new Object[] { userId, toolSessionId });

		if (list == null || list.isEmpty())
			return null;

		return (ChatUser) list.get(0);
	}

	public ChatUser getByLoginNameAndSessionId(String loginName, Long toolSessionId) {

			List list = this.getHibernateTemplate().find(
					SQL_QUERY_FIND_BY_LOGIN_NAME_SESSION_ID,
					new Object[] { loginName, toolSessionId });

			if (list == null || list.isEmpty())
				return null;

			return (ChatUser) list.get(0);
		
	}
	
	public void saveOrUpdate(ChatUser chatUser) {
		this.getHibernateTemplate().saveOrUpdate(chatUser);
		this.getHibernateTemplate().flush();
	}
}
