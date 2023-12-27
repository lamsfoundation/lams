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

package org.lamsfoundation.lams.tool.zoom.dto;

import org.lamsfoundation.lams.tool.zoom.model.ZoomUser;

public class ZoomUserDTO implements Comparable<ZoomUserDTO> {
    private Long uid;

    private String loginName;

    private String firstName;

    private String lastName;

    private String email;

    private boolean finishedActivity;

    public ZoomUserDTO(ZoomUser zoomUser) {
	this.uid = zoomUser.getUid();
	this.firstName = zoomUser.getFirstName();
	this.lastName = zoomUser.getLastName();
	this.email = zoomUser.getEmail();
	this.finishedActivity = zoomUser.isFinishedActivity();
    }

    @Override
    public int compareTo(ZoomUserDTO other) {
	int ret = this.lastName.compareToIgnoreCase(other.lastName);
	if (ret == 0) {
	    ret = this.uid.compareTo(other.uid);
	}
	return ret;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
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

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }
}
