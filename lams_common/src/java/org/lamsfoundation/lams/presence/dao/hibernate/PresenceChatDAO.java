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

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.presence.dao.IPresenceChatDAO;
import org.lamsfoundation.lams.presence.model.PresenceChatMessage;
import org.lamsfoundation.lams.presence.model.PresenceChatUser;
import org.springframework.stereotype.Repository;

@Repository
public class PresenceChatDAO extends LAMSBaseDAO implements IPresenceChatDAO {

    private static final String MESSAGE_BY_MESSAGE_ID = "from " + PresenceChatMessage.class.getName() + " msg"
	    + " where msg.uid=?";

    private static final String MESSAGE_BY_CONVERSATION = "from "
	    + PresenceChatMessage.class.getName()
	    + " msg"
	    + " where (msg.from=:from and msg.to=:to) or (msg.from=:to and msg.to=:from) and msg.lessonId=:lessonId order by msg.dateSent asc";

    private static final String MESSAGE_BY_LESSON_ID = "from " + PresenceChatMessage.class.getName() + " msg"
	    + " where msg.lessonId=? and msg.to is null order by msg.dateSent asc";

    // main query for getting new messages
    private static final String MESSAGE_NEW = "FROM "
	    + PresenceChatMessage.class.getName()
	    + " msg"
	    + " WHERE msg.lessonId=:lessonId AND"
	    // below: get messages for opened tab, only if they have not been read yet;
	    // in HQL, CASE statement does notwork :( and this trick with 'NULL'=:to is really necessary
	    + " (msg.uid > :lastMessageUid AND (('NULL'=:to AND msg.to IS NULL) OR (msg.to=:to AND msg.from=:from) OR (msg.to=:from AND msg.from=:to)))"
	    // below: get new messages from other tabs/users, if they have not been checked yet
	    + " OR (msg.dateSent > :lastCheck AND (msg.to IS NULL OR msg.to=:from))" + " ORDER BY msg.dateSent ASC";

    public static final String USER_BY_LESSON_ID_AND_TIME = "from " + PresenceChatUser.class.getName() + " r"
	    + " where r.lessonId=? and r.lastPresence > ?";

    @Override
    public void saveOrUpdate(Object object) {
	this.saveOrUpdate(object);
	this.getSession().flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public PresenceChatMessage getMessageById(Long id) {
	List<PresenceChatMessage> list = (List<PresenceChatMessage>) (doFind(PresenceChatDAO.MESSAGE_BY_CONVERSATION,
		new Object[] { id }));

	if (!list.isEmpty()) {
	    return list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PresenceChatMessage> getMessagesByConversation(Long lessonId, String from, String to) {
	return (List<PresenceChatMessage>) (doFindByNamedParam(PresenceChatDAO.MESSAGE_BY_CONVERSATION, new String[] { "from",
		"to", "lessonId" }, new Object[] { from, to, lessonId }));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PresenceChatMessage> getMessagesByLessonId(Long lessonId) {
	return (List<PresenceChatMessage>) (doFind(PresenceChatDAO.MESSAGE_BY_LESSON_ID, new Object[] { lessonId }));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PresenceChatMessage> getNewMessages(Long lessonId, String from, String to, Long lastMessageUid,
	    Date lastCheck) {
	return (List<PresenceChatMessage>) (doFindByNamedParam(PresenceChatDAO.MESSAGE_NEW, new String[] { "lessonId", "from",
		"to", "lastMessageUid", "lastCheck" }, new Object[] { lessonId, from, to == null ? "NULL" : to,
		lastMessageUid == null ? 0 : lastMessageUid, lastCheck == null ? new Date(0) : lastCheck }));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PresenceChatUser> getUsersByLessonIdAndLastPresence(Long lessonId, Date oldestLastPresence) {
	return (List<PresenceChatUser>) this.doFind(PresenceChatDAO.USER_BY_LESSON_ID_AND_TIME,
		new Object[] { lessonId, oldestLastPresence });

    }
}
