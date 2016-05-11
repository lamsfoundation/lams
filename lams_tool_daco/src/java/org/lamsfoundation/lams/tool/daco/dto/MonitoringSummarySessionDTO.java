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


package org.lamsfoundation.lams.tool.daco.dto;

import java.util.List;

/**
 * Represents a group of users with details that are useful for displaying summary in monitoring.
 *
 * @author Marcin Cieslak
 *
 */
public class MonitoringSummarySessionDTO {

    private Long sessionId;
    private String sessionName;
    private List<MonitoringSummaryUserDTO> users;

    public MonitoringSummarySessionDTO(Long sessionId, String sessionName) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public List<MonitoringSummaryUserDTO> getUsers() {
	return users;
    }

    public void setUsers(List<MonitoringSummaryUserDTO> users) {
	this.users = users;
    }
}