/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.chat.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.chat.Chat;
import org.lamsfoundation.lams.tool.chat.dao.IChatDAO;

/**
 * DAO for accessing the Chat objects - Hibernate specific code.
 */
public class ChatDAO extends BaseDAO implements IChatDAO {

	private static final String FIND_FORUM_BY_CONTENTID = "from Chat chat where chat.toolContentId=?";
	
	public Chat getByContentId(Long toolContentId) {
		List list = getHibernateTemplate().find(FIND_FORUM_BY_CONTENTID,toolContentId);
		if(list != null && list.size() > 0)
			return (Chat) list.get(0);
		else
			return null;
	}

	public void saveOrUpdate(Chat chat) {
		this.getHibernateTemplate().saveOrUpdate(chat);
		this.getHibernateTemplate().flush();
	}
}
