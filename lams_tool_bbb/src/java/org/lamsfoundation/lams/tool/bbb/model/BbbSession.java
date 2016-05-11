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


package org.lamsfoundation.lams.tool.bbb.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 *
 * Represents the tool session.
 *
 * @hibernate.class table="tl_labbb10_session"
 */

public class BbbSession implements java.io.Serializable {

    private static final Logger logger = Logger.getLogger(BbbSession.class);

    private static final long serialVersionUID = 4407078136514639026L;

    // Fields

    private Long uid;

    private Date sessionEndDate;

    private Date sessionStartDate;

    private Integer status;

    private Long sessionId;

    private String sessionName;

    private Bbb bbb;

    private Set<BbbUser> bbbUsers;

    private boolean meetingCreated;

    private String attendeePassword;

    private String moderatorPassword;

    // Property accessors

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     *
     */
    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="session_end_date"
     *
     */
    public Date getSessionEndDate() {
	return this.sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     * @hibernate.property column="session_start_date"
     *
     */
    public Date getSessionStartDate() {
	return this.sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     * @hibernate.property column="status" length="11"
     *
     */
    public Integer getStatus() {
	return this.status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    /**
     * @hibernate.property column="session_id" length="20"
     *
     */
    public Long getSessionId() {
	return this.sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @hibernate.property column="session_name" length="250"
     *
     */
    public String getSessionName() {
	return this.sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    /**
     * @hibernate.many-to-one not-null="true" foreign-key="fk_labbb10_bbb_session_to_bbb"
     * @hibernate.column name="bbb_uid"
     *
     */
    public Bbb getBbb() {
	return this.bbb;
    }

    public void setBbb(Bbb bbb) {
	this.bbb = bbb;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="bbb_session_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.bbb.model.BbbUser"
     *
     */
    public Set<BbbUser> getBbbUsers() {
	return this.bbbUsers;
    }

    public void setBbbUsers(Set<BbbUser> bbbUsers) {
	this.bbbUsers = bbbUsers;
    }

    /**
     * @hibernate.property column="meeting_created"
     * @return
     */
    public boolean isMeetingCreated() {
	return meetingCreated;
    }

    public void setMeetingCreated(boolean meetingCreated) {
	this.meetingCreated = meetingCreated;
    }

    /**
     * @hibernate.property column="attendee_password"
     * @return the attendeePassword
     */
    public String getAttendeePassword() {
	return attendeePassword;
    }

    public void setAttendeePassword(String attendeePassword) {
	this.attendeePassword = attendeePassword;
    }

    /**
     * @hibernate.property column="moderator_password"
     * @return the moderatorPassword
     */
    public String getModeratorPassword() {
	return moderatorPassword;
    }

    public void setModeratorPassword(String moderatorPassword) {
	this.moderatorPassword = moderatorPassword;
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
	if (!(other instanceof BbbSession)) {
	    return false;
	}
	BbbSession castOther = (BbbSession) other;

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

	BbbSession session = null;
	try {
	    session = (BbbSession) super.clone();
	    session.bbbUsers = new HashSet<BbbUser>();
	} catch (CloneNotSupportedException e) {
	    logger.error("When clone " + BbbSession.class + " failed");
	}
	return session;
    }

}
