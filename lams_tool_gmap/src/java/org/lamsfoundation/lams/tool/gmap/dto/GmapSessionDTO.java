/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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



package org.lamsfoundation.lams.tool.gmap.dto;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;

public class GmapSessionDTO implements Comparable {

    Long sessionID;

    String sessionName;

    Set<GmapUserDTO> userDTOs = new TreeSet<GmapUserDTO>();

    int numberOfLearners;

    int numberOfFinishedLearners;

    /**
     * This set has been added here to simplify the ambiguity with gmap to marker set mappings
     * There is one set of markers for each gmap session, whereas there are several for each gmap
     * So much simpler to make the set a property of the gmap session object rather than the gmap object
     */
    Set<GmapMarkerDTO> markerDTOs = new HashSet<GmapMarkerDTO>();

    public GmapSessionDTO(GmapSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();

	numberOfFinishedLearners = 0;
	for (Iterator<GmapUser> iterator = session.getGmapUsers().iterator(); iterator.hasNext();) {
	    GmapUser user = iterator.next();
	    GmapUserDTO userDTO = new GmapUserDTO(user);
	    userDTOs.add(userDTO);
	    if (user.isFinishedActivity()) {
		numberOfFinishedLearners++;
	    }
	}
	numberOfLearners = userDTOs.size();
    }

    public GmapSessionDTO() {
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
	GmapSessionDTO toSession = (GmapSessionDTO) o;
	returnValue = this.sessionName.compareTo(toSession.sessionName);
	if (returnValue == 0) {
	    returnValue = this.sessionID.compareTo(toSession.sessionID);
	}
	return returnValue;
    }

    public Set<GmapUserDTO> getUserDTOs() {
	return userDTOs;
    }

    public void setUserDTOs(Set<GmapUserDTO> gmapUsers) {
	this.userDTOs = gmapUsers;
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

    public Set<GmapMarkerDTO> getMarkerDTOs() {
	return markerDTOs;
    }

    public void setMarkerDTOs(Set<GmapMarkerDTO> markerDTOs) {
	this.markerDTOs = markerDTOs;
    }

    public void setMarkerDTOs(List<GmapMarker> markers) {
	for (GmapMarker marker : markers) {
	    GmapMarkerDTO markerDTO = new GmapMarkerDTO(marker);
	    markerDTOs.add(markerDTO);
	}
    }

}
