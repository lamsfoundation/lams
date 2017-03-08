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

package org.lamsfoundation.lams.tool.scribe.model;

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

public class ScribeSession implements java.io.Serializable {

    private static Logger log = Logger.getLogger(ScribeSession.class);
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

    private Scribe scribe;

    private Set scribeUsers;

    private Set scribeReportEntries;

    private ScribeUser appointedScribe;

    private boolean forceComplete;

    private boolean reportSubmitted;
    // Constructors

    /** default constructor */
    public ScribeSession() {
    }

    /** full constructor */
    public ScribeSession(Date sessionEndDate, Date sessionStartDate, Integer status, Long sessionId, String sessionName,
	    Scribe scribe, Set scribeUsers) {
	this.sessionEndDate = sessionEndDate;
	this.sessionStartDate = sessionStartDate;
	this.status = status;
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	this.scribe = scribe;
	this.scribeUsers = scribeUsers;
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
     */

    public Date getSessionEndDate() {
	return this.sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
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
     */

    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    /**
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
     */

    public Scribe getScribe() {
	return this.scribe;
    }

    public void setScribe(Scribe scribe) {
	this.scribe = scribe;
    }

    /**
     *
     *
     *
     */
    public Set getScribeUsers() {
	return this.scribeUsers;
    }

    public void setScribeUsers(Set scribeUsers) {
	this.scribeUsers = scribeUsers;
    }

    /**
     *
     *
     *
     */
    public Set getScribeReportEntries() {
	return scribeReportEntries;
    }

    public void setScribeReportEntries(Set scribeReportEntries) {
	this.scribeReportEntries = scribeReportEntries;
    }

    /**
     *
     *
     */
    public ScribeUser getAppointedScribe() {
	return appointedScribe;
    }

    public void setAppointedScribe(ScribeUser appointedScribe) {
	this.appointedScribe = appointedScribe;
    }

    /**
     *
     */
    public boolean isForceComplete() {
	return forceComplete;
    }

    public void setForceComplete(boolean forceComplete) {
	this.forceComplete = forceComplete;
    }

    /**
     *
     */
    public boolean isReportSubmitted() {
	return reportSubmitted;
    }

    public void setReportSubmitted(boolean reportSubmitted) {
	this.reportSubmitted = reportSubmitted;
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
	if (!(other instanceof ScribeSession)) {
	    return false;
	}
	ScribeSession castOther = (ScribeSession) other;

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

	ScribeSession session = null;
	try {
	    session = (ScribeSession) super.clone();
	    session.scribeUsers = new HashSet();
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ScribeSession.class + " failed");
	}
	return session;
    }
}
