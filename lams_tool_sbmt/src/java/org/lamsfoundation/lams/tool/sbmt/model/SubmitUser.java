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


package org.lamsfoundation.lams.tool.sbmt.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.service.IUserDetails;

/**
 * @author Steve.Ni
 */
@Entity
@Table(name = "tl_lasbmt11_user")
public class SubmitUser implements Serializable, Cloneable, IUserDetails {
    private static final long serialVersionUID = 4951104689120529660L;
    private static Logger log = Logger.getLogger(SubmitUser.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "user_id")
    private Integer userID;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "login_name")
    private String login;
    
    @Column(name = "session_id")
    private Long sessionID;
    
    @Column(name = "content_id")
    private Long contentID;

    @Column
    private boolean finished;

    @Override
    public Object clone() {
	SubmitUser user = null;
	try {
	    user = (SubmitUser) super.clone();
	    user.setUid(null);
	    // never clone session
	    user.setSessionID(null);
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + SubmitUser.class + " failed");
	}
	return user;
    }

    // ***********************************************************
    // Get / Set methods
    // ***********************************************************
    
    /**
     * @return Returns the learnerID.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param learnerID
     *            The learnerID to set.
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the userID.
     */
    public Integer getUserID() {
	return userID;
    }

    /**
     * @param userID
     *            The userID to set.
     */
    public void setUserID(Integer userID) {
	this.userID = userID;
    }

    /**
     * @return Returns the finished.
     */
    public boolean isFinished() {
	return finished;
    }

    /**
     * @param finished
     *            The finished to set.
     */
    public void setFinished(boolean finished) {
	this.finished = finished;
    }

    /**
     * @return Returns the sessionID.
     */
    public Long getSessionID() {
	return sessionID;
    }

    /**
     * @param sessionID
     *            The sessionID to set.
     */
    public void setSessionID(Long sessionID) {
	this.sessionID = sessionID;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String loginName) {
	this.login = loginName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String secondName) {
	this.lastName = secondName;
    }

    public Long getContentID() {
	return contentID;
    }

    public void setContentID(Long contentID) {
	this.contentID = contentID;
    }
}