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

/* $$Id$$ */

package org.lamsfoundation.lams.tool.forum.persistence;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * 
 * @hibernate.class table="tl_lafrum11_forum_user"
 * @author Steve.Ni
 * 
 * @version $Revision$
 * @serialData -7043502180037866257L
 */
public class ForumUser implements Serializable, Cloneable {

    private static final long serialVersionUID = -7043502180037866257L;
    private static Logger log = Logger.getLogger(ForumUser.class);

    private Long uid;
    private Long userId;
    private String firstName;
    private String lastName;
    private String loginName;
    private boolean sessionFinished;

    private ForumToolSession session;

    public ForumUser() {
    }

    /** Create the user based on the DTO in the session */
    public ForumUser(UserDTO user, ForumToolSession session) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.sessionFinished = false;
    }
    
    /** Create the user based on the details in the JSON call - used for authoring so no session exists. */
    public ForumUser(Long userId, String firstName, String lastName, String loginName) {
	this.userId = userId;
	this.firstName = firstName;
	this.lastName = lastName;
	this.loginName = loginName;
	this.session = null;
	this.sessionFinished = false;
    }

    // **********************************************************
    // Function method for ForumUser
    // **********************************************************
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() {

	Object obj = null;
	try {
	    obj = super.clone();
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ForumUser.class + " failed");
	}
	return obj;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
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
     * @hibernate.property column="user_id" length="20"
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
     * @hibernate.property length="255" column="last_name"
     * @return
     */
    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @hibernate.property length="255" column="first_name"
     * @return
     */
    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @hibernate.many-to-one column="session_id" cascade="none"
     * @return
     */
    public ForumToolSession getSession() {
	return session;
    }

    public void setSession(ForumToolSession session) {
	this.session = session;
    }

    /**
     * @hibernate.property column="login_name"
     * @return
     */
    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    /**
     * @hibernate.property column="session_finished"
     * @return
     */
    public boolean isSessionFinished() {
	return sessionFinished;
    }

    public void setSessionFinished(boolean sessionFinished) {
	this.sessionFinished = sessionFinished;
    }

    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!(obj instanceof ForumUser))
	    return false;

	final ForumUser user = (ForumUser) obj;

	return new EqualsBuilder().append(this.uid, user.uid).append(this.firstName, user.firstName)
		.append(this.lastName, user.lastName).append(this.loginName, user.loginName).isEquals();

    }

    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(firstName).append(lastName).append(loginName).toHashCode();
    }

}
