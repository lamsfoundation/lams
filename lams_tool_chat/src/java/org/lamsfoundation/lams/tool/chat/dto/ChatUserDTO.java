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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.tool.chat.dto;

import org.lamsfoundation.lams.tool.chat.model.ChatUser;

public class ChatUserDTO implements Comparable{
	
	public Long uid;
	
	public String loginName;
	
	public String jabberNickname;
	
	public String firstName;
	
	public String lastName;
	
	public int postCount;
	
	public ChatUserDTO(ChatUser user) {
		this.uid = user.getUid();
		this.loginName = user.getLoginName();
		this.jabberNickname = user.getJabberNickname();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		postCount=0; // this needs to be set manually.
	}

	public int compareTo(Object o) {
		int returnValue;
		ChatUserDTO toUser = (ChatUserDTO)o;
		returnValue = this.lastName.compareTo(toUser.lastName);
		if (returnValue == 0) {
			returnValue = this.uid.compareTo(toUser.uid);			
		}
		return returnValue;		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getJabberNickname() {
		return jabberNickname;
	}

	public void setJabberNickname(String jabberNickname) {
		this.jabberNickname = jabberNickname;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
	
}
