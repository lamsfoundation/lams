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

package org.lamsfoundation.lams.tool.dimdim.dto;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;

public class DimdimUserDTO implements Comparable<DimdimUserDTO> {

	public Long uid;

	public String loginName;

	public String firstName;

	public String lastName;

	public boolean finishedActivity;

	public NotebookEntryDTO entryDTO;

	public Long entryUID;

	public DimdimUserDTO(DimdimUser user, NotebookEntry entry) {
		this.uid = user.getUid();
		this.loginName = user.getLoginName();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.finishedActivity = user.isFinishedActivity();
		this.entryUID = user.getEntryUID();
		this.entryDTO = new NotebookEntryDTO(entry);
	}

	public DimdimUserDTO(DimdimUser user) {
		this.uid = user.getUid();
		this.loginName = user.getLoginName();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.finishedActivity = user.isFinishedActivity();
		this.entryUID = user.getEntryUID();
	}

	public int compareTo(DimdimUserDTO other) {
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

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public NotebookEntryDTO getEntryDTO() {
		return entryDTO;
	}

	public void setEntryDTO(NotebookEntryDTO entryDTO) {
		this.entryDTO = entryDTO;
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
}
