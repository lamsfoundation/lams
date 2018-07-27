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



package org.lamsfoundation.lams.tool.kaltura.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.kaltura.model.KalturaSession;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaUser;

public class KalturaSessionDTO implements Comparable {

    private Long sessionID;

    private String sessionName;

    private Set<KalturaUserDTO> userDTOs = new TreeSet<KalturaUserDTO>();

    private int numberOfLearners;

    private int numberOfFinishedLearners;

    public KalturaSessionDTO(KalturaSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();

	for (Iterator iterator = session.getKalturaUsers().iterator(); iterator.hasNext();) {
	    KalturaUser user = (KalturaUser) iterator.next();
	    KalturaUserDTO userDTO = new KalturaUserDTO(user);
	    if (userDTO.isFinishedActivity()) {
		numberOfFinishedLearners++;
	    }

	    userDTOs.add(userDTO);
	}

	numberOfLearners = userDTOs.size();

    }

    public KalturaSessionDTO() {
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

    @Override
    public int compareTo(Object o) {
	int returnValue;
	KalturaSessionDTO toSession = (KalturaSessionDTO) o;
	returnValue = this.sessionName.compareTo(toSession.sessionName);
	if (returnValue == 0) {
	    returnValue = this.sessionID.compareTo(toSession.sessionID);
	}
	return returnValue;
    }

    public Set<KalturaUserDTO> getUserDTOs() {
	return userDTOs;
    }

    public void setUserDTOs(Set<KalturaUserDTO> kalturaUsers) {
	this.userDTOs = kalturaUsers;
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
