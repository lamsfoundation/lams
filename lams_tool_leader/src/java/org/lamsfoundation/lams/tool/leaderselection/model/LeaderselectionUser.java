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


package org.lamsfoundation.lams.tool.leaderselection.model;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 *
 * Caches the user details. This allows the tool to be more efficient at displaying user names but means that when
 * people's names change, they won't change in the "old" tool data.
 *
 *
 */

public class LeaderselectionUser implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3701664859818409197L;

    // Fields
    private Long uid;

    private Long userId;

    private String lastName;

    private String firstName;

    private String loginName;

    private LeaderselectionSession leaderselectionSession;

    private boolean finishedActivity;

    // Constructors

    /** default constructor */
    public LeaderselectionUser() {
    }

    public LeaderselectionUser(UserDTO user, LeaderselectionSession leaderselectionSession) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.leaderselectionSession = leaderselectionSession;
	this.finishedActivity = false;
    }

    /** full constructor */
    public LeaderselectionUser(Long userId, String lastName, String firstName,
	    LeaderselectionSession leaderselectionSession) {
	this.userId = userId;
	this.lastName = lastName;
	this.firstName = firstName;
	this.leaderselectionSession = leaderselectionSession;
    }

    // Property accessors
    /**
     *
     */
    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */
    public Long getUserId() {
	return this.userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    /**
     *
     *
     */
    public String getLastName() {
	return this.lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     *
     *
     */
    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    /**
     *
     *
     */
    public String getFirstName() {
	return this.firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     *
     */
    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }

    /**
     *
     *
     *
     */
    public LeaderselectionSession getLeaderselectionSession() {
	return this.leaderselectionSession;
    }

    public void setLeaderselectionSession(LeaderselectionSession leaderselectionSession) {
	this.leaderselectionSession = leaderselectionSession;
    }

    /**
     * toString
     *
     * @return String
     */
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
	if (!(other instanceof LeaderselectionUser)) {
	    return false;
	}
	LeaderselectionUser castOther = (LeaderselectionUser) other;

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
