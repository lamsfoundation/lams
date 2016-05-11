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


package org.lamsfoundation.lams.tool.bbb.dto;

import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.bbb.model.BbbSession;
import org.lamsfoundation.lams.tool.bbb.model.BbbUser;

public class SessionDTO implements Comparable<SessionDTO> {

    Long sessionID;

    String sessionName;

    Set<UserDTO> userDTOs = new TreeSet<UserDTO>();

    int numberOfLearners;

    int numberOfFinishedLearners;;

    public SessionDTO(BbbSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();

	numberOfFinishedLearners = 0;

	for (BbbUser bbbUser : session.getBbbUsers()) {
	    UserDTO userDTO = new UserDTO(bbbUser);
	    if (userDTO.getNotebookEntryUID() != null) {
		numberOfFinishedLearners++;
	    }
	    userDTOs.add(userDTO);
	}

	numberOfLearners = userDTOs.size();

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

    public Set<UserDTO> getUserDTOs() {
	return userDTOs;
    }

    public void setUserDTOs(Set<UserDTO> bbbUsers) {
	this.userDTOs = bbbUsers;
    }

    public int getNumberOfLearners() {
	return numberOfLearners;
    }

    public void setNumberOfLearners(int numberOfLearners) {
	this.numberOfLearners = numberOfLearners;
    }

    public int getNumberOfFinishedLearners() {
	return numberOfFinishedLearners;
    }

    public void setNumberOfFinishedLearners(int numberOfFinishedLearners) {
	this.numberOfFinishedLearners = numberOfFinishedLearners;
    }
}
