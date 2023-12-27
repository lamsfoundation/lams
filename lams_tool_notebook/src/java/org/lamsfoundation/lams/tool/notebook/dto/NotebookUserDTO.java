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

package org.lamsfoundation.lams.tool.notebook.dto;

import org.apache.commons.lang.StringEscapeUtils;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;

public class NotebookUserDTO implements Comparable {

    private Long uid;

    private String loginName;

    private String firstName;

    private String lastName;

    private boolean finishedActivity;

    private String notebookEntry;

    private String teachersComment;

    public NotebookUserDTO(NotebookUser user) {
	this.uid = user.getUid();
	this.loginName = user.getLoginName();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.finishedActivity = user.isFinishedActivity();
	this.notebookEntry = user.getNotebookEntry();
	if (user.getTeachersComment() != null) {
	    this.teachersComment = StringEscapeUtils.escapeJavaScript(user.getTeachersComment());
	}
    }

    @Override
    public int compareTo(Object o) {
	int returnValue;
	NotebookUserDTO toUser = (NotebookUserDTO) o;
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

    public String getTeachersComment() {
	return teachersComment;
    }

    public void setTeachersComment(String teachersComment) {
	this.teachersComment = teachersComment;
    }

    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }
}
