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



package org.lamsfoundation.lams.tool.wookie.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.wookie.model.WookieSession;
import org.lamsfoundation.lams.tool.wookie.model.WookieUser;

public class WookieSessionDTO implements Comparable<Object> {

    Long sessionID;

    String sessionName;

    Set<WookieUserDTO> userDTOs = new TreeSet<WookieUserDTO>();

    int numberOfLearners;

    int numberOfFinishedLearners;;

    // Wookie properties
    String sessionUserWidgetUrl;
    String widgetSharedDataKey;
    Integer widgetHeight;
    Integer widgetWidth;
    Boolean widgetMaximise;
    String widgetIdentifier;

    public WookieSessionDTO(WookieSession session) {
	this.sessionID = session.getSessionId();
	this.sessionName = session.getSessionName();

	numberOfFinishedLearners = 0;
	for (Iterator<WookieUser> iterator = session.getWookieUsers().iterator(); iterator.hasNext();) {
	    WookieUser user = iterator.next();
	    WookieUserDTO userDTO = new WookieUserDTO(user);
	    if (userDTO.getEntryUID() != null) {
		numberOfFinishedLearners++;
	    }
	    userDTOs.add(userDTO);
	}

	numberOfLearners = userDTOs.size();

	// Set the wookie properties
	this.widgetHeight = session.getWidgetHeight();
	this.widgetIdentifier = session.getWidgetIdentifier();
	this.widgetMaximise = session.getWidgetMaximise();
	this.widgetSharedDataKey = session.getWidgetSharedDataKey();
	this.widgetWidth = session.getWidgetWidth();
    }

    public WookieSessionDTO() {
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
	WookieSessionDTO toSession = (WookieSessionDTO) o;
	returnValue = this.sessionName.compareTo(toSession.sessionName);
	if (returnValue == 0) {
	    returnValue = this.sessionID.compareTo(toSession.sessionID);
	}
	return returnValue;
    }

    public Set<WookieUserDTO> getUserDTOs() {
	return userDTOs;
    }

    public void setUserDTOs(Set<WookieUserDTO> wookieUsers) {
	this.userDTOs = wookieUsers;
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

    public String getWidgetSharedDataKey() {
	return widgetSharedDataKey;
    }

    public void setWidgetSharedDataKey(String widgetSharedDataKey) {
	this.widgetSharedDataKey = widgetSharedDataKey;
    }

    public Integer getWidgetHeight() {
	return widgetHeight;
    }

    public void setWidgetHeight(Integer widgetHeight) {
	this.widgetHeight = widgetHeight;
    }

    public Integer getWidgetWidth() {
	return widgetWidth;
    }

    public void setWidgetWidth(Integer widgetWidth) {
	this.widgetWidth = widgetWidth;
    }

    public Boolean getWidgetMaximise() {
	return widgetMaximise;
    }

    public void setWidgetMaximise(Boolean widgetMaximise) {
	this.widgetMaximise = widgetMaximise;
    }

    public String getWidgetIdentifier() {
	return widgetIdentifier;
    }

    public void setWidgetIdentifier(String widgetIdentifier) {
	this.widgetIdentifier = widgetIdentifier;
    }

    public String getSessionUserWidgetUrl() {
	return sessionUserWidgetUrl;
    }

    public void setSessionUserWidgetUrl(String sessionUserWidgetUrl) {
	this.sessionUserWidgetUrl = sessionUserWidgetUrl;
    }
}
