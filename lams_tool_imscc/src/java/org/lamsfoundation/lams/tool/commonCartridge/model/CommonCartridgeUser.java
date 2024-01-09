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

package org.lamsfoundation.lams.tool.commonCartridge.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

/**
 * CommonCartridge
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laimsc11_user")
public class CommonCartridgeUser implements Cloneable, IUserDetails {
    private static Logger log = Logger.getLogger(CommonCartridgeUser.class);

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
    private CommonCartridgeSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commonCartridge_uid")
    private CommonCartridge commonCartridge;

    //=============== NON Persisit value: for display use ===========

    //the user access some reousrce item date time. Use in monitoring summary page
    @Transient
    private Date accessDate;

    public CommonCartridgeUser() {
    }

    public CommonCartridgeUser(UserDTO user, CommonCartridgeSession session) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.commonCartridge = null;
	this.sessionFinished = false;
    }

    public CommonCartridgeUser(UserDTO user, CommonCartridge content) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = null;
	this.commonCartridge = content;
	this.sessionFinished = false;
    }

    @Override
    public Object clone() {
	CommonCartridgeUser user = null;
	try {
	    user = (CommonCartridgeUser) super.clone();
	    user.setUid(null);
	    //never clone session
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + CommonCartridgeUser.class + " failed");
	}

	return user;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof CommonCartridgeUser)) {
	    return false;
	}

	final CommonCartridgeUser user = (CommonCartridgeUser) obj;

	return new EqualsBuilder().append(this.uid, user.uid).append(this.firstName, user.firstName)
		.append(this.lastName, user.lastName).append(this.loginName, user.loginName).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(firstName).append(lastName).append(loginName).toHashCode();
    }

    //  **********************************************************
    //		Get/Set methods
    //  **********************************************************

    /**
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     * 	The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     * @return Returns the userId.
     */
    public Long getUserId() {
	return userId;
    }

    /**
     * @param userId
     * 	The userId to set.
     */
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

    @Override
    public String getLogin() {
	return getLoginName();
    }

    public CommonCartridgeSession getSession() {
	return session;
    }

    public void setSession(CommonCartridgeSession session) {
	this.session = session;
    }

    public CommonCartridge getCommonCartridge() {
	return commonCartridge;
    }

    public void setCommonCartridge(CommonCartridge content) {
	this.commonCartridge = content;
    }

    public boolean isSessionFinished() {
	return sessionFinished;
    }

    public void setSessionFinished(boolean sessionFinished) {
	this.sessionFinished = sessionFinished;
    }

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }

}