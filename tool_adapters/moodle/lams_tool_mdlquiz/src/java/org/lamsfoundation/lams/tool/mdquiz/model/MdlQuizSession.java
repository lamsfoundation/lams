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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.mdquiz.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * Represents the tool session.
 * 
 * @hibernate.class table="tl_mdquiz10_session"
 */

public class MdlQuizSession implements java.io.Serializable {

    private static Logger log = Logger.getLogger(MdlQuizSession.class);
    /**
     * 
     */
    private static final long serialVersionUID = 2736481298379482738L;

    // Fields
    private Long uid;

    private Date sessionEndDate;

    private Date sessionStartDate;

    private Integer status;

    private Long sessionId;

    private Long extSessionId;

    private String sessionName;

    private MdlQuiz mdlQuiz;

    private Set<MdlQuizUser> mdlQuizUsers;

    // Constructors

    /** default constructor */
    public MdlQuizSession() {
    }

    /** full constructor */
    public MdlQuizSession(Date sessionEndDate, Date sessionStartDate, Integer status, Long sessionId,
	    Long extSessionId, String sessionName, MdlQuiz mdlQuiz, Set<MdlQuizUser> mdlQuizUsers) {
	this.sessionEndDate = sessionEndDate;
	this.sessionStartDate = sessionStartDate;
	this.status = status;
	this.sessionId = sessionId;
	this.sessionName = sessionName;
	this.mdlQuiz = mdlQuiz;
	this.mdlQuizUsers = mdlQuizUsers;
    }

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
     * @hibernate.property column="ext_session_id" length="20"
     * @return
     */
    public Long getExtSessionId() {
	return extSessionId;
    }

    public void setExtSessionId(Long extSessionId) {
	this.extSessionId = extSessionId;
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
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="mdlQuiz_uid"
     * 
     */

    public MdlQuiz getMdlQuiz() {
	return this.mdlQuiz;
    }

    public void setMdlQuiz(MdlQuiz mdlQuiz) {
	this.mdlQuiz = mdlQuiz;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="mdlQuiz_session_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.mdquiz.model.MdlQuizUser"
     * 
     */

    public Set<MdlQuizUser> getMdlQuizUsers() {
	return this.mdlQuizUsers;
    }

    public void setMdlQuizUsers(Set<MdlQuizUser> mdlQuizUsers) {
	this.mdlQuizUsers = mdlQuizUsers;
    }

    /**
     * toString
     * 
     * @return String
     */
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

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof MdlQuizSession))
	    return false;
	MdlQuizSession castOther = (MdlQuizSession) other;

	return ((this.getUid() == castOther.getUid()) || (this.getUid() != null && castOther.getUid() != null && this
		.getUid().equals(castOther.getUid())));
    }

    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public Object clone() {

	MdlQuizSession session = null;
	try {
	    session = (MdlQuizSession) super.clone();
	    session.mdlQuizUsers = new HashSet<MdlQuizUser>();
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + MdlQuizSession.class + " failed");
	}
	return session;
    }

}
