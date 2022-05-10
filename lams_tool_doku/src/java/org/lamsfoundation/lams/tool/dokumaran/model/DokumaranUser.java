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

package org.lamsfoundation.lams.tool.dokumaran.model;

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

/**
 * Dokumaran user
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_ladoku11_user")
public class DokumaranUser implements Cloneable {
    private static Logger log = Logger.getLogger(DokumaranUser.class);

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

    @Column
    private boolean leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_uid")
    private DokumaranSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dokumaran_uid")
    private Dokumaran dokumaran;

    // date when user has started activity
    @Column(name = "time_limit_launched_date")
    private LocalDateTime timeLimitLaunchedDate;

    public DokumaranUser() {
    }

    public DokumaranUser(UserDTO user, DokumaranSession session) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.dokumaran = session.getDokumaran();
	this.sessionFinished = false;
	this.leader = false;
    }

    public DokumaranUser(UserDTO user, Dokumaran content) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = null;
	this.dokumaran = content;
	this.sessionFinished = false;
	this.leader = false;
    }

    @Override
    public Object clone() {
	DokumaranUser user = null;
	try {
	    user = (DokumaranUser) super.clone();
	    user.setUid(null);
	    // never clone session
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + DokumaranUser.class + " failed");
	}

	return user;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof DokumaranUser)) {
	    return false;
	}

	final DokumaranUser user = (DokumaranUser) obj;

	return new EqualsBuilder().append(this.uid, user.uid).append(this.firstName, user.firstName)
		.append(this.lastName, user.lastName).append(this.loginName, user.loginName).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(firstName).append(lastName).append(loginName).toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

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

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    public DokumaranSession getSession() {
	return session;
    }

    public void setSession(DokumaranSession session) {
	this.session = session;
    }

    public Dokumaran getDokumaran() {
	return dokumaran;
    }

    public void setDokumaran(Dokumaran content) {
	this.dokumaran = content;
    }

    public boolean isSessionFinished() {
	return sessionFinished;
    }

    public void setSessionFinished(boolean sessionFinished) {
	this.sessionFinished = sessionFinished;
    }

    public boolean isLeader() {
	return leader;
    }

    public void setLeader(boolean leader) {
	this.leader = leader;
    }

    public LocalDateTime getTimeLimitLaunchedDate() {
	return timeLimitLaunchedDate;
    }

    public void setTimeLimitLaunchedDate(LocalDateTime timeLimitLaunchedDate) {
	this.timeLimitLaunchedDate = timeLimitLaunchedDate;
    }
}