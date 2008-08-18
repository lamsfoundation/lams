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

package org.lamsfoundation.lams.tool.dlfrum.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumSession;
import org.lamsfoundation.lams.tool.dlfrum.model.DotLRNForumUser;

public class DotLRNForumSessionDTO implements Comparable {
	
	Long sessionID;
	
	Long extSessionID;

	String sessionName;
	
	Set<DotLRNForumUserDTO> userDTOs = new TreeSet<DotLRNForumUserDTO>();
	
	int numberOfLearners;
	
	int numberOfFinishedLearners;
	

	public DotLRNForumSessionDTO(DotLRNForumSession session) {
		this.sessionID = session.getSessionId();
		this.sessionName = session.getSessionName();
		this.extSessionID = session.getExtSessionId();
		numberOfFinishedLearners = 0;
		for (Iterator iterator = session.getDotLRNForumUsers().iterator(); iterator.hasNext();) {
			DotLRNForumUser user = (DotLRNForumUser) iterator.next();
			DotLRNForumUserDTO userDTO = new DotLRNForumUserDTO(user);
			userDTOs.add(userDTO);
		}
		numberOfLearners = userDTOs.size();	
	}
	
	public DotLRNForumSessionDTO() {}
	
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
		DotLRNForumSessionDTO toSession = (DotLRNForumSessionDTO)o;
		returnValue = this.sessionName.compareTo(toSession.sessionName);
		if (returnValue == 0) {
			returnValue = this.sessionID.compareTo(toSession.sessionID);			
		}
		return returnValue;		
	}

	public Set<DotLRNForumUserDTO> getUserDTOs() {
		return userDTOs;
	}

	public void setUserDTOs(Set<DotLRNForumUserDTO> dotLRNForumUsers) {
		this.userDTOs = dotLRNForumUsers;
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

	public Long getExtSessionID() {
		return extSessionID;
	}

	public void setExtSessionID(Long extSessionID) {
		this.extSessionID = extSessionID;
	}
}
