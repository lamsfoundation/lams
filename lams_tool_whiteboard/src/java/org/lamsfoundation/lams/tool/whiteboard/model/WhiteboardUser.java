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

package org.lamsfoundation.lams.tool.whiteboard.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

@Entity
@Table(name = "tl_lawhiteboard11_user")
public class WhiteboardUser implements Cloneable, IUserDetails {
    private static Logger log = Logger.getLogger(WhiteboardUser.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "session_finished")
    private boolean sessionFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_uid")
    private WhiteboardSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "whiteboard_uid")
    private Whiteboard whiteboard;

    // date when user has started activity
    @Column(name = "time_limit_launched_date")
    private LocalDateTime timeLimitLaunchedDate;

    public WhiteboardUser() {
    }

    public WhiteboardUser(UserDTO user, WhiteboardSession session) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.whiteboard = session.getWhiteboard();
	this.sessionFinished = false;
    }

    public WhiteboardUser(UserDTO user, Whiteboard content) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = null;
	this.whiteboard = content;
	this.sessionFinished = false;
    }

    @Override
    public Object clone() {
	WhiteboardUser user = null;
	try {
	    user = (WhiteboardUser) super.clone();
	    user.setUid(null);
	    // never clone session
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + WhiteboardUser.class + " failed");
	}

	return user;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof WhiteboardUser)) {
	    return false;
	}

	final WhiteboardUser user = (WhiteboardUser) obj;

	return new EqualsBuilder().append(this.uid, user.uid).append(this.firstName, user.firstName)
		.append(this.lastName, user.lastName).append(this.loginName, user.loginName).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(firstName).append(lastName).append(loginName).toHashCode();
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userID) {
	this.userId = userID;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLoginName() {
	return loginName;
    }

    @Override
    public String getLogin() {
	return getLoginName();
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    public WhiteboardSession getSession() {
	return session;
    }

    public void setSession(WhiteboardSession session) {
	this.session = session;
    }

    public Whiteboard getWhiteboard() {
	return whiteboard;
    }

    public void setWhiteboard(Whiteboard content) {
	this.whiteboard = content;
    }

    public boolean isSessionFinished() {
	return sessionFinished;
    }

    public void setSessionFinished(boolean sessionFinished) {
	this.sessionFinished = sessionFinished;
    }

    public LocalDateTime getTimeLimitLaunchedDate() {
	return timeLimitLaunchedDate;
    }

    public void setTimeLimitLaunchedDate(LocalDateTime timeLimitLaunchedDate) {
	this.timeLimitLaunchedDate = timeLimitLaunchedDate;
    }
}