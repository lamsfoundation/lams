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



package org.lamsfoundation.lams.learning.presence.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learning.presence.dao.IPresenceChatDAO;
import org.lamsfoundation.lams.learning.presence.model.PresenceChatMessage;
import org.lamsfoundation.lams.learning.presence.model.PresenceChatUser;
import org.springframework.stereotype.Repository;

@Repository
public class PresenceChatDAO extends LAMSBaseDAO implements IPresenceChatDAO {
    // main query for getting new messages
    private static final String MESSAGE_NEW = "FROM " + PresenceChatMessage.class.getName()
	    + " msg WHERE msg.lessonId=:lessonId AND msg.dateSent > :lastCheck ORDER BY msg.dateSent ASC";

    // main query for getting new messages
    private static final String MESSAGE_BY_CONVERSATION = "FROM " + PresenceChatMessage.class.getName()
	    + " msg WHERE msg.lessonId=:lessonId AND ((msg.to = :to AND msg.from = :from) OR (msg.from = :to AND msg.to = :from)) "
	    + "ORDER BY msg.dateSent ASC";

    private static final String USER_BY_LESSON_ID_AND_TIME = "from " + PresenceChatUser.class.getName() + " r"
	    + " where r.lessonId=? and r.lastPresence > ?";

    @Override
    public void insertOrUpdate(Object object) {
	super.insertOrUpdate(object);
	flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PresenceChatMessage> getNewMessages(Long lessonId, Date lastCheck) {
	return (List<PresenceChatMessage>) (doFindByNamedParam(PresenceChatDAO.MESSAGE_NEW,
		new String[] { "lessonId", "lastCheck" }, new Object[] { lessonId, lastCheck }));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PresenceChatMessage> getMessagesByConversation(Long lessonId, String from, String to) {
	return (List<PresenceChatMessage>) (doFindByNamedParam(PresenceChatDAO.MESSAGE_BY_CONVERSATION,
		new String[] { "lessonId", "from", "to" }, new Object[] { lessonId, from, to }));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PresenceChatUser> getUsersByLessonIdAndLastPresence(Long lessonId, Date oldestLastPresence) {
	return (List<PresenceChatUser>) this.doFind(PresenceChatDAO.USER_BY_LESSON_ID_AND_TIME,
		new Object[] { lessonId, oldestLastPresence });
    }
}