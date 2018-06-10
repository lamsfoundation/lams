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

package org.lamsfoundation.lams.tool.zoom.dto;

import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.zoom.model.ZoomSession;

public class SessionDTO implements Comparable<SessionDTO> {

    Long sessionID;

    String sessionName;

    Set<ZoomUserDTO> userDTOs = new TreeSet<ZoomUserDTO>();

    public SessionDTO(ZoomSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();
    }

    @Override
    public int compareTo(SessionDTO other) {
	int ret = this.sessionName.compareToIgnoreCase(other.sessionName);
	if (ret == 0) {
	    ret = this.sessionID.compareTo(other.sessionID);
	}
	return ret;
    }

    public SessionDTO() {
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

    public Set<ZoomUserDTO> getUserDTOs() {
	return userDTOs;
    }

    public void setUserDTOs(Set<ZoomUserDTO> zoomUsers) {
	this.userDTOs = zoomUsers;
    }
}