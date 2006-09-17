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

/* $Id$ */

package org.lamsfoundation.lams.tool.scribe.dto;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;

public class ScribeSessionDTO implements Comparable {
	
	Long sessionID;

	String sessionName;
	
	Set<ScribeUserDTO> userDTOs;
	
	int numberOfPosts;
	
	int numberOfLearners;
	
	public ScribeSessionDTO(ScribeSession session) {
		this.sessionID = session.getSessionId();
		this.sessionName = session.getSessionName();
		
		userDTOs = new TreeSet<ScribeUserDTO>();
	}
	
	public ScribeSessionDTO() {
		userDTOs = new TreeSet<ScribeUserDTO>();
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

	public int compareTo(Object o) {
		int returnValue;
		ScribeSessionDTO toSession = (ScribeSessionDTO)o;
		returnValue = this.sessionName.compareTo(toSession.sessionName);
		if (returnValue == 0) {
			returnValue = this.sessionID.compareTo(toSession.sessionID);			
		}
		return returnValue;		
	}

	public Set<ScribeUserDTO> getUserDTOs() {
		return userDTOs;
	}

	public void setUserDTOs(Set<ScribeUserDTO> scribeUsers) {
		this.userDTOs = scribeUsers;
	}

	public int getNumberOfLearners() {
		return numberOfLearners;
	}

	public void setNumberOfLearners(int numberOfLearners) {
		this.numberOfLearners = numberOfLearners;
	}

	public int getNumberOfPosts() {
		return numberOfPosts;
	}

	public void setNumberOfPosts(int numberOfPosts) {
		this.numberOfPosts = numberOfPosts;
	}
}
