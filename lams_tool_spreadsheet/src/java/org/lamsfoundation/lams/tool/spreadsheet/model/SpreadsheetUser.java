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

package org.lamsfoundation.lams.tool.spreadsheet.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
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
 * Spreadsheet user
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lasprd10_user")
public class SpreadsheetUser implements Cloneable, Serializable, IUserDetails {
    private static final long serialVersionUID = -7043502180037866257L;
    private static Logger log = Logger.getLogger(SpreadsheetUser.class);

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
    private SpreadsheetSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spreadsheet_uid")
    private Spreadsheet spreadsheet;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_modified_spreadsheet_uid")
    private UserModifiedSpreadsheet userModifiedSpreadsheet;

    //=============== NON Persisit value: for display use ===========

    //the user access some reousrce item date time. Use in monitoring summary page
    @Transient
    private Date accessDate;

    public SpreadsheetUser() {
    }

    public SpreadsheetUser(UserDTO user, SpreadsheetSession session) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.spreadsheet = null;
	this.sessionFinished = false;
    }

    public SpreadsheetUser(UserDTO user, Spreadsheet content) {
	this.userId = user.getUserID().longValue();
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = null;
	this.spreadsheet = content;
	this.sessionFinished = false;
    }

    @Override
    public Object clone() {
	SpreadsheetUser user = null;
	try {
	    user = (SpreadsheetUser) super.clone();
	    user.setUid(null);
	    //never clone session
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SpreadsheetUser.class + " failed");
	}

	return user;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof SpreadsheetUser)) {
	    return false;
	}

	final SpreadsheetUser user = (SpreadsheetUser) obj;

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

    @Override
    public String getLogin() {
	return getLoginName();
    }

    public SpreadsheetSession getSession() {
	return session;
    }

    public void setSession(SpreadsheetSession session) {
	this.session = session;
    }

    public Spreadsheet getSpreadsheet() {
	return spreadsheet;
    }

    public void setSpreadsheet(Spreadsheet spreadsheet) {
	this.spreadsheet = spreadsheet;
    }

    public UserModifiedSpreadsheet getUserModifiedSpreadsheet() {
	return userModifiedSpreadsheet;
    }

    public void setUserModifiedSpreadsheet(UserModifiedSpreadsheet userModifiedSpreadsheet) {
	this.userModifiedSpreadsheet = userModifiedSpreadsheet;
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

    /** Username displayed in monitoring */
    public String getFullUsername() {
	StringBuilder buf = new StringBuilder();
	buf.append(getFullName()).append(" (").append(getLoginName()).append(")");
	return buf.toString();
    }

}