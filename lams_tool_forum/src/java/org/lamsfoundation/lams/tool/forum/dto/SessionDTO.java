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

package org.lamsfoundation.lams.tool.forum.dto;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.model.ForumUser;

/**
 * @author Anthony Sukkar
 */
public class SessionDTO {

    private Long sessionID;
    private String sessionName;
    private boolean marksReleased;
    //used for storing data for MonitoringAction.getUsers() serving tablesorter paging
    private Map<ForumUser, List<MessageDTO>> topicsByUser;

    public SessionDTO(ForumToolSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();
	this.marksReleased = session.isMarkReleased();
    }

    public Long getSessionID() {
	return sessionID;
    }

    public void setSessionID(Long sessionID) {
	this.sessionID = sessionID;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }
    
    public boolean isMarksReleased() {
	return marksReleased;
    }

    public void setMarksReleased(boolean marksReleased) {
	this.marksReleased = marksReleased;
    }

    public Map<ForumUser, List<MessageDTO>> getTopicsByUser() {
	return topicsByUser;
    }

    public void setTopicsByUser(Map<ForumUser, List<MessageDTO>> topicsByUser) {
	this.topicsByUser = topicsByUser;
    }

}
