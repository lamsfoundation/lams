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


package org.lamsfoundation.lams.tool.kaltura.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 *
 * Caches the user details. This allows the tool to be more efficient at displaying user names but means that when
 * people's names change, they won't change in the "old" tool data.
 *
 *
 */

public class KalturaUser implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = -3701664859818409197L;
    private static Logger log = Logger.getLogger(KalturaUser.class);

    // Fields
    private Long uid;

    private Long userId;

    private String lastName;

    private String firstName;

    private String loginName;

    private KalturaSession session;

    private Kaltura kaltura;

    private boolean finishedActivity;

    // Constructors

    /** default constructor */
    public KalturaUser() {
    }

    public KalturaUser(UserDTO user, KalturaSession session) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = session;
	this.finishedActivity = false;
    }

    public KalturaUser(UserDTO user, Kaltura kaltura) {
	this.userId = new Long(user.getUserID().intValue());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.loginName = user.getLogin();
	this.session = null;
	this.kaltura = kaltura;
	this.finishedActivity = false;
    }

    @Override
    public Object clone() {

	KalturaUser user = null;
	try {
	    user = (KalturaUser) super.clone();
	    user.setUid(null);
	    // never clone session
	    user.setSession(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + KalturaUser.class + " failed");
	}

	return user;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}

	if (!(obj instanceof KalturaUser)) {
	    return false;
	}

	final KalturaUser user = (KalturaUser) obj;

	return new EqualsBuilder().append(this.uid, user.uid).append(this.firstName, user.firstName)
		.append(this.lastName, user.lastName).append(this.loginName, user.loginName).isEquals();
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
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
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
    public KalturaSession getSession() {
	return this.session;
    }

    public void setSession(KalturaSession kalturaSession) {
	this.session = kalturaSession;
    }

    /**
     *
     */
    public Kaltura getKaltura() {
	return kaltura;
    }

    public void setKaltura(Kaltura kaltura) {
	this.kaltura = kaltura;
    }
}
