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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.integration.dto;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.util.AlphanumComparator;

public class ExtGroupDTO implements Comparable {
    private String groupId;
    private String groupName;

    private int numberUsers;
    // list of org.lamsfoundation.lams.usermanagement.dto.UserDTO
    private List users;

    @Override
    public int compareTo(Object o) {
	ExtGroupDTO castOther = (ExtGroupDTO) o;

	String grp1Name = castOther != null && castOther.getGroupName() != null
		? StringUtils.lowerCase(castOther.getGroupName())
		: "";
	String grp2Name = this.groupName != null ? StringUtils.lowerCase(this.groupName) : "";

	AlphanumComparator comparator = new AlphanumComparator();
	return comparator.compare(grp1Name, grp2Name);
    }

    public String getGroupId() {
	return groupId;
    }

    public void setGroupId(String groupId) {
	this.groupId = groupId;
    }

    public String getGroupName() {
	return groupName;
    }

    public void setGroupName(String groupName) {
	this.groupName = groupName;
    }

    public int getNumberUsers() {
	return numberUsers;
    }

    public void setNumberUsers(int numberUsers) {
	this.numberUsers = numberUsers;
    }

    public List getUsers() {
	return users;
    }

    public void setUsers(List userList) {
	this.users = userList;
    }
}
