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

package org.lamsfoundation.lams.tool.daco.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

/**
 * Daco
 *
 * @author Dapeng Ni
 */
@Entity
@Table(name = "tl_ladaco10_users")
public class DacoUser implements Cloneable, IUserDetails {
    private static Logger log = Logger.getLogger(DacoUser.class);

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
    private DacoSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_uid")
    private Daco daco;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @OrderBy("recordId ASC, uid ASC")
    private Set<DacoAnswer> answers = new LinkedHashSet<DacoAnswer>();

    // the user access some resource question date time. Use in monitoring summary page
    @Transient
    private Date accessDate;

    public DacoUser() {
    }

    public DacoUser(UserDTO user, DacoSession session) {
	userId = new Long(user.getUserID().intValue());
	firstName = user.getFirstName();
	lastName = user.getLastName();
	loginName = user.getLogin();
	this.session = session;
	daco = null;
	sessionFinished = false;
    }

    public DacoUser(UserDTO user, Daco content) {
	userId = new Long(user.getUserID().intValue());
	firstName = user.getFirstName();
	lastName = user.getLastName();
	loginName = user.getLogin();
	session = null;
	daco = content;
	sessionFinished = false;
    }

    /**
     * Clone method from <code>java.lang.Object</code>
     */
    @Override
    public Object clone() {

	DacoUser user = null;
	try {
	    user = (DacoUser) super.clone();
	    user.setUid(null);
	    // never clone session
	    user.setAnswers(new LinkedHashSet<DacoAnswer>(answers.size()));
	    for (DacoAnswer answer : answers) {
		user.getAnswers().add((DacoAnswer) answer.clone());
	    }
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    DacoUser.log.error("When clone " + DacoUser.class + " failed");
	}

	return user;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	uid = userID;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userID) {
	userId = userID;
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

    public String getLogin() {
	return getLoginName();
    }

    public DacoSession getSession() {
	return session;
    }

    public void setSession(DacoSession session) {
	this.session = session;
    }

    public Daco getDaco() {
	return daco;
    }

    public void setDaco(Daco content) {
	daco = content;
    }

    public boolean isSessionFinished() {
	return sessionFinished;
    }

    public void setSessionFinished(boolean sessionFinished) {
	this.sessionFinished = sessionFinished;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof DacoUser)) {
	    return false;
	}

	final DacoUser user = (DacoUser) obj;

	return new EqualsBuilder().append(uid, user.uid).append(firstName, user.firstName)
		.append(lastName, user.lastName).append(loginName, user.loginName).isEquals();

    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(firstName).append(lastName).append(loginName).toHashCode();
    }

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

    public Set<DacoAnswer> getAnswers() {
	return answers;
    }

    public void setAnswers(Set<DacoAnswer> answers) {
	this.answers = answers;
    }
}