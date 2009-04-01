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

/* $Id$ */

package org.lamsfoundation.lams.presence.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.presence.dao.IPresenceChatMessageDAO;
import org.lamsfoundation.lams.presence.model.PresenceChatMessage;

public class PresenceChatMessageDAO extends BaseDAO implements IPresenceChatMessageDAO {

	private static final String BY_MESSAGE_ID = "from " + PresenceChatMessage.class.getName() 
		+ " where uid=? order by create_date desc";
	
	private static final String BY_CONVERSATION = "from " + PresenceChatMessage.class.getName() 
		+ " where from_user=? and to_user=? order by create_date asc";
	
	private static final String BY_ROOM_NAME = "from " + NotebookEntry.class.getName() 
		+ " where room_name=? order by create_date asc";
	
	public void saveOrUpdate(PresenceChatMessage presenceChatMessage) {
		this.getHibernateTemplate().saveOrUpdate(presenceChatMessage);
		this.getHibernateTemplate().flush();
	}
	
	public PresenceChatMessage getMessageById(Long id) {
		List list = (List<PresenceChatMessage>)(getHibernateTemplate().find(BY_CONVERSATION, new Object[]{id}));
		
		if(!list.isEmpty()){
			return (PresenceChatMessage)list.get(0);
		}
		else return null;
	}

	public List<PresenceChatMessage> getMessagesByConversation(String from,
			String to) {
		return (List<PresenceChatMessage>)(getHibernateTemplate().find(BY_CONVERSATION, new Object[]{from, to}));
	}

	public List<PresenceChatMessage> getMessagesByRoomName(String roomName) {
		return (List<PresenceChatMessage>)(getHibernateTemplate().find(BY_ROOM_NAME, new Object[]{roomName}));
	}
}
