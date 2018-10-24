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



package org.lamsfoundation.lams.tool.leaderselection.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;

public class LeaderselectionSessionDTO implements Comparable {

    private Long sessionID;

    private String sessionName;

    private LeaderselectionUser groupLeader;

    private Set<LeaderselectionUserDTO> userDTOs = new TreeSet<LeaderselectionUserDTO>();

    private int numberOfLearners;

    private int numberOfFinishedLearners;;

    public LeaderselectionSessionDTO(LeaderselectionSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();
	this.groupLeader = session.getGroupLeader();

	numberOfFinishedLearners = 0;
	for (Iterator iterator = session.getUsers().iterator(); iterator.hasNext();) {
	    LeaderselectionUser user = (LeaderselectionUser) iterator.next();
	    LeaderselectionUserDTO userDTO = new LeaderselectionUserDTO(user);
	    if (userDTO.isFinishedActivity()) {
		numberOfFinishedLearners++;
	    }
	    userDTOs.add(userDTO);
	}

	numberOfLearners = userDTOs.size();

    }

    public LeaderselectionSessionDTO() {
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

    public LeaderselectionUser getGroupLeader() {
	return groupLeader;
    }

    public void setGroupLeader(LeaderselectionUser groupLeader) {
	this.groupLeader = groupLeader;
    }

    @Override
    public int compareTo(Object o) {
	int returnValue;
	LeaderselectionSessionDTO toSession = (LeaderselectionSessionDTO) o;
	returnValue = this.sessionName.compareTo(toSession.sessionName);
	if (returnValue == 0) {
	    returnValue = this.sessionID.compareTo(toSession.sessionID);
	}
	return returnValue;
    }

    public Set<LeaderselectionUserDTO> getUserDTOs() {
	return userDTOs;
    }

    public void setUserDTOs(Set<LeaderselectionUserDTO> leaderselectionUsers) {
	this.userDTOs = leaderselectionUsers;
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
