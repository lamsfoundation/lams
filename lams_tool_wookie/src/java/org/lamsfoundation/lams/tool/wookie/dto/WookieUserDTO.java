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

import org.lamsfoundation.lams.tool.wookie.model.WookieUser;

public class WookieUserDTO implements Comparable<Object> {

    public Long uid;

    public Long userId;

    public String loginName;

    public String firstName;

    public String lastName;

    public boolean finishedActivity;

    public String notebookEntry;

    public Long entryUID;

    public String imageFileName;

    public boolean finishedReflection;

    public WookieUserDTO(WookieUser user) {
	this.uid = user.getUid();
	this.loginName = user.getLoginName();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.finishedActivity = user.isFinishedActivity();
	this.entryUID = user.getEntryUID();
	this.userId = user.getUserId();
    }

    @Override
    public int compareTo(Object o) {
	int returnValue;
	WookieUserDTO toUser = (WookieUserDTO) o;
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

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getNotebookEntry() {
	return notebookEntry;
    }

    public void setNotebookEntry(String notebookEntry) {
	this.notebookEntry = notebookEntry;
    }

    public Long getEntryUID() {
	return entryUID;
    }

    public void setEntryUID(Long entryUID) {
	this.entryUID = entryUID;
    }

    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }

    public String getImageFileName() {
	return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    public boolean isFinishedReflection() {
	return finishedReflection;
    }

    public void setFinishedReflection(boolean finishedReflection) {
	this.finishedReflection = finishedReflection;
    }

    public Long getUserId() {
	return userId;
    }
}
