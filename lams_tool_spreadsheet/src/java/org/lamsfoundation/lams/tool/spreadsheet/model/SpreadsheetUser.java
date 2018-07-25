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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Spreadsheet
 * 
 * @author Andrey Balan
 *
 *
 *
 */
public class SpreadsheetUser implements Cloneable, Serializable {
    private static final long serialVersionUID = -7043502180037866257L;
    private static Logger log = Logger.getLogger(SpreadsheetUser.class);

    private Long uid;
    private Long userId;
    private String firstName;
    private String lastName;
    private String loginName;
    private boolean sessionFinished;

    private SpreadsheetSession session;
    private Spreadsheet spreadsheet;
    private UserModifiedSpreadsheet userModifiedSpreadsheet;

    //=============== NON Persisit value: for display use ===========
    //the user access some reousrce item date time. Use in monitoring summary page
    private Date accessDate;

    public SpreadsheetUser() {
    }

    public SpreadsheetUser(UserDTO user, SpreadsheetSession session) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.spreadsheet = null;
	this.sessionFinished = false;
    }

    public SpreadsheetUser(UserDTO user, Spreadsheet content) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = null;
	this.spreadsheet = content;
	this.sessionFinished = false;
    }

    /**
     * Clone method from <code>java.lang.Object</code>
     */
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

//  **********************************************************
    //		Get/Set methods
//  **********************************************************
    /**
     *
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     *
     * @return Returns the userId.
     */
    public Long getUserId() {
	return userId;
    }

    /**
     * @param userId
     *            The userId to set.
     */
    public void setUserId(Long userID) {
	this.userId = userID;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     *
     * @return
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
     * @return
     */
    public SpreadsheetSession getSession() {
	return session;
    }

    public void setSession(SpreadsheetSession session) {
	this.session = session;
    }

    /**
     *
     *
     * @return
     */
    public Spreadsheet getSpreadsheet() {
	return spreadsheet;
    }

    public void setSpreadsheet(Spreadsheet spreadsheet) {
	this.spreadsheet = spreadsheet;
    }

    /**
     *
     *
     * 
     * @return
     */
    public UserModifiedSpreadsheet getUserModifiedSpreadsheet() {
	return userModifiedSpreadsheet;
    }

    public void setUserModifiedSpreadsheet(UserModifiedSpreadsheet userModifiedSpreadsheet) {
	this.userModifiedSpreadsheet = userModifiedSpreadsheet;
    }

    /**
     *
     * @return
     */
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

    public Date getAccessDate() {
	return accessDate;
    }

    public void setAccessDate(Date accessDate) {
	this.accessDate = accessDate;
    }
    
    /** Username displayed in monitoring */
    public String getFullUsername() {
	StringBuilder buf = new StringBuilder();
	buf.append(getLastName())
	   .append(" ")
	   .append(getFirstName())
	   .append(" (")
	   .append(getLoginName())
	   .append(")");
	return buf.toString();
    }

}
