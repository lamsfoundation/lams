/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.dto;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * DTO that holds the details of an open text answer, used for Monitoring
 */
public class OpenTextAnswerDTO {
    protected Long userUid;
    protected String login;
    protected String fullName;
    protected Long userEntryUid;
    protected String userEntry;
    protected Date attemptTime;
    protected Boolean visible;
    protected String portraitId;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("login: ", login).append("userUid: ", userUid)
		.append("userEntry: ", userEntry).append("userEntryUid: ", userEntryUid).append("visible: ", visible)
		.toString();
    }

    public Long getUserUid() {
	return userUid;
    }

    public void setUserUid(Long userUid) {
	this.userUid = userUid;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public Long getUserEntryUid() {
	return userEntryUid;
    }

    public void setUserEntryUid(Long userEntryUid) {
	this.userEntryUid = userEntryUid;
    }

    public String getUserEntry() {
	return userEntry;
    }

    public void setUserEntry(String userEntry) {
	this.userEntry = userEntry;
    }

    public Date getAttemptTime() {
	return attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
	this.attemptTime = attemptTime;
    }

    public Boolean isVisible() {
	return visible;
    }

    public void setVisible(Boolean visible) {
	this.visible = visible;
    }

    public String getPortraitId() {
	return portraitId;
    }

    public void setPortraitId(String portraitId) {
	this.portraitId = portraitId;
    }

}
