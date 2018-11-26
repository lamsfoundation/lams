/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.scribe.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 *
 * Caches the user details. This allows the tool to be more efficient at
 * displaying user names but means that when people's names change, they won't
 * change in the "old" tool data.
 *
 *
 */

@Entity
@Table(name = "tl_lascrb11_user")
public class ScribeUser implements java.io.Serializable {

    private static final long serialVersionUID = -3701664859818409197L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "login_name")
    private String loginName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scribe_session_uid")
    private ScribeSession scribeSession;

    @Column
    private boolean finishedActivity;

    @Column(name = "report_approved")
    private boolean reportApproved;

    @Column(name = "started_activity")
    private boolean startedActivity;

    /** default constructor */
    public ScribeUser() {
    }

    public ScribeUser(UserDTO user, ScribeSession scribeSession) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.scribeSession = scribeSession;
	this.finishedActivity = false;
	this.startedActivity = false;
    }

    /** full constructor */
    public ScribeUser(Long userId, String lastName, String firstName, ScribeSession scribeSession) {
	this.userId = userId;
	this.lastName = lastName;
	this.firstName = firstName;
	this.scribeSession = scribeSession;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getUserId() {
	return this.userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getLastName() {
	return this.lastName;
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

    public String getFirstName() {
	return this.firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }

    public ScribeSession getScribeSession() {
	return this.scribeSession;
    }

    public void setScribeSession(ScribeSession scribeSession) {
	this.scribeSession = scribeSession;
    }

    public boolean isReportApproved() {
	return reportApproved;
    }

    public void setReportApproved(boolean reportApproved) {
	this.reportApproved = reportApproved;
    }

    public boolean isStartedActivity() {
	return startedActivity;
    }

    public void setStartedActivity(boolean startedActivity) {
	this.startedActivity = startedActivity;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("userId").append("='").append(getUserId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof ScribeUser)) {
	    return false;
	}
	ScribeUser castOther = (ScribeUser) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }
}
