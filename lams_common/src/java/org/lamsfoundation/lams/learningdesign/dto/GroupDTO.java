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

package org.lamsfoundation.lams.learningdesign.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.usermanagement.User;

/** Grouping object, suitable for sending to Authoring */
public class GroupDTO {

    private Long groupID;
    private String groupName;
    private int orderID;
    //list of org.lamsfoundation.lams.usermanagement.dto.UserDTO
    private List userList;
    private Integer groupUIID;

    /**
     * Get the DTO for this group. Does not include the GroupBranchActivities as they will
     * be in a separate array for Authoring.
     * 
     * @param group
     */
    @SuppressWarnings("unchecked")
    public GroupDTO(Group group, boolean setupUserList) {
	groupID = group.getGroupId();
	groupName = group.getGroupName();
	orderID = group.getOrderId();
	groupUIID = group.getGroupUIID();
	userList = new ArrayList();
	if (setupUserList && group.getUsers() != null) {
	    Iterator iter = group.getUsers().iterator();
	    while (iter.hasNext()) {
		userList.add(((User) iter.next()).getUserBasicDTO());
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

    public Integer getGroupUIID() {
	return groupUIID;
    }

    public void setGroupUIID(Integer groupUIID) {
	this.groupUIID = groupUIID;
    }

    public List getUserList() {
	return userList;
    }

    public void setUserList(List userList) {
	this.userList = userList;
    }
}
