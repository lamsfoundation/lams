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



package org.lamsfoundation.lams.learning.presence.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.learning.presence.dao.IPresenceChatDAO;
import org.lamsfoundation.lams.learning.presence.model.PresenceChatMessage;
import org.lamsfoundation.lams.learning.presence.model.PresenceChatUser;

public class PresenceChatService implements IPresenceChatService {

    private IPresenceChatDAO presenceChatDAO;

    @Override
    public void createPresenceChatMessage(Long lessonId, String from, String to, Date dateSent, String message) {
	PresenceChatMessage presenceChatMessage = new PresenceChatMessage(lessonId, from, to, dateSent, message);
	presenceChatDAO.insertOrUpdate(presenceChatMessage);
    }

    /**
     * Stores information when users with given UIDs were last seen in their Chat session.
     */
    @Override
    public void updateUserPresence(Long lessonId, Set<String> users) {
	Date now = new Date();
	for (String nickname : users) {
	    PresenceChatUser rosterEntry = new PresenceChatUser(nickname, lessonId, now);
	    presenceChatDAO.insertOrUpdate(rosterEntry);
	}
    }

    @Override
    public List<PresenceChatUser> getUsersActiveByLessonId(Long lessonId) {
	Date oldestLastPresence = new Date(System.currentTimeMillis() - IPresenceChatService.PRESENCE_IDLE_TIMEOUT);
	return presenceChatDAO.getUsersByLessonIdAndLastPresence(lessonId, oldestLastPresence);
    }

    @Override
    public List<PresenceChatMessage> getNewMessages(Long lessonId, Date lastCheck) {
	return presenceChatDAO.getNewMessages(lessonId, lastCheck);
    }

    @Override
    public List<PresenceChatMessage> getMessagesByConversation(Long lessonId, String from, String to) {
	return presenceChatDAO.getMessagesByConversation(lessonId, from, to);
    }

    public void setPresenceChatDAO(IPresenceChatDAO presenceChatDAO) {
	this.presenceChatDAO = presenceChatDAO;
    }
}