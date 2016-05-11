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


package org.lamsfoundation.lams.tool.pixlr.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 *
 * Represents the tool session.
 *
 *
 */

public class PixlrSession implements java.io.Serializable {

    private static Logger log = Logger.getLogger(PixlrSession.class);
    /**
     * 
     */
    private static final long serialVersionUID = 4407078136514639026L;

    // Fields
    private Long uid;

    private Date sessionEndDate;

    private Date sessionStartDate;

    private Integer status;

    private Long sessionId;

    private String sessionName;

    private Pixlr pixlr;

    private Set<PixlrUser> pixlrUsers;

    // Constructors

    /** default constructor */
    public PixlrSession() {
    }

    /** full constructor */
    public PixlrSession(Date sessionEndDate, Date sessionStartDate, Integer status, Long sessionId, String sessionName,
	    Pixlr pixlr, Set<PixlrUser> pixlrUsers) {
	this.sessionEndDate = sessionEndDate;
	this.sessionStartDate = sessionStartDate;
	this.status = status;
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	this.pixlr = pixlr;
	this.pixlrUsers = pixlrUsers;
    }

    // Property accessors
    /**
     *
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

    public Date getSessionEndDate() {
	return this.sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     *
     * 
     */

    public Date getSessionStartDate() {
	return this.sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     *
     * 
     */

    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    /**
     *
     * 
     */

    public Long getSessionId() {
	return this.sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     *
     * 
     */

    public String getSessionName() {
	return this.sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    /**
     *
     *
     * 
     */

    public Pixlr getPixlr() {
	return this.pixlr;
    }

    public void setPixlr(Pixlr pixlr) {
	this.pixlr = pixlr;
    }

    /**
     *
     *
     *
     * 
     */

    public Set<PixlrUser> getPixlrUsers() {
	return this.pixlrUsers;
    }

    public void setPixlrUsers(Set<PixlrUser> pixlrUsers) {
	this.pixlrUsers = pixlrUsers;
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
	buffer.append("sessionEndDate").append("='").append(getSessionEndDate()).append("' ");
	buffer.append("sessionStartDate").append("='").append(getSessionStartDate()).append("' ");
	buffer.append("status").append("='").append(getStatus()).append("' ");
	buffer.append("sessionID").append("='").append(getSessionId()).append("' ");
	buffer.append("sessionName").append("='").append(getSessionName()).append("' ");
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
	if (!(other instanceof PixlrSession)) {
	    return false;
	}
	PixlrSession castOther = (PixlrSession) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    @Override
    public Object clone() {

	PixlrSession session = null;
	try {
	    session = (PixlrSession) super.clone();
	    session.pixlrUsers = new HashSet<PixlrUser>();
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + PixlrSession.class + " failed");
	}
	return session;
    }

}
