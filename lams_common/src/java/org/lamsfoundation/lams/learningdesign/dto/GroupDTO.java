/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.usermanagement.User;

public class GroupDTO {
	
	private Long groupID;
	private String groupName;
	private int orderID;
	//list of org.lamsfoundation.lams.usermanagement.dto.UserDTO
	private List userList;
	
	public GroupDTO(Group group) {
		groupID = group.getGroupId();
		groupName = group.getGroupName();
		orderID = group.getOrderId();
		userList = new ArrayList();
		if(group.getUsers() != null){
			Iterator iter = group.getUsers().iterator();
			while(iter.hasNext()){
				userList.add(((User)iter.next()).getUserDTO());
			}
		}
	}
	public Long getGroupID() {
		return groupID;
	}
	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getOrderID() {
		return orderID;
	}
	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}
	public List getUserList() {
		return userList;
	}
	public void setUserList(List userList) {
		this.userList = userList;
	} 
}
