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

package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Ozgur Demirtas
 *
 *         QaSession Value Object The value object that maps to our model database
 *         table: tl_laqa11_session The relevant hibernate mapping resides in:
 *         QaSession.hbm.xml
 *
 *         Holds tool sessions
 */
public class QaSession implements Serializable, Comparable, Nullable {

    public final static String INCOMPLETE = "INCOMPLETE";

    public static final String COMPLETED = "COMPLETED";

    /** identifier field */
    private Long uid;

    /** identifier field */
    private Long qaSessionId;

    /** nullable persistent field */
    private Date session_start_date;

    /** nullable persistent field */
    private Date session_end_date;

    /** nullable persistent field */
    private String session_status;

    /** nullable persistent field */
    private String session_name;

    /** persistent field */
    private QaContent qaContent;

    /** persistent field */
    private Set qaQueUsers;

    private QaQueUsr groupLeader;

    public QaSession() {
    };

    public QaSession(Long qaSessionId, Date session_start_date, Date session_end_date, String session_status,
	    QaContent qaContent, Set qaQueUsers) {
	this.qaSessionId = qaSessionId;
	this.session_start_date = session_start_date;
	this.session_end_date = session_end_date;
	this.session_status = session_status;
	this.qaContent = qaContent;
	this.qaQueUsers = qaQueUsers;
    }

    public QaSession(Long qaSessionId, Date session_start_date, String session_status, String session_name,
	    QaContent qaContent, Set qaQueUsers) {
	this.qaSessionId = qaSessionId;
	this.session_start_date = session_start_date;
	this.session_status = session_status;
	this.session_name = session_name;
	this.qaContent = qaContent;
	this.qaQueUsers = qaQueUsers;
    }

    public Long getQaSessionId() {
	return this.qaSessionId;
    }

    public void setQaSessionId(Long qaSessionId) {
	this.qaSessionId = qaSessionId;
    }

    public Date getSession_start_date() {
	return this.session_start_date;
    }

    public void setSession_start_date(Date session_start_date) {
	this.session_start_date = session_start_date;
    }

    public Date getSession_end_date() {
	return this.session_end_date;
    }

    public void setSession_end_date(Date session_end_date) {
	this.session_end_date = session_end_date;
    }

    public String getSession_status() {
	return session_status;
    }

    public void setSession_status(String session_status) {
	this.session_status = session_status;
    }

    public QaContent getQaContent() {
	return this.qaContent;
    }

    public void setQaContent(QaContent qaContent) {
	this.qaContent = qaContent;
    }

    public Set getQaQueUsers() {
	if (this.qaQueUsers == null) {
	    setQaQueUsers(new TreeSet());
	}
	return this.qaQueUsers;
    }

    public void setQaQueUsers(Set qaQueUsers) {
	this.qaQueUsers = qaQueUsers;
    }

    public void removeQueUsersBy(List responses) {
	Set queUserSet = new TreeSet(this.getQaQueUsers());
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("qaSessionId", getQaSessionId())
		.append("session start date", getSession_start_date()).append("session end date", getSession_end_date())
		.append("session status", getSession_status()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof QaSession)) {
	    return false;
	}

	QaSession castOther = (QaSession) other;
	return new EqualsBuilder().append(this.getQaSessionId(), castOther.getQaSessionId()).isEquals();

    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getQaSessionId()).toHashCode();

    }

    @Override
    public int compareTo(Object o) {
	QaSession qaSession = (QaSession) o;
	return (int) (qaSessionId.longValue() - qaSession.qaSessionId.longValue());
    }

    @Override
    public boolean isNull() {
	return false;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the session_name.
     */
    public String getSession_name() {
	return session_name;
    }

    /**
     * @param session_name
     *            The session_name to set.
     */
    public void setSession_name(String session_name) {
	this.session_name = session_name;
    }

    public QaQueUsr getGroupLeader() {
	return this.groupLeader;
    }

    public void setGroupLeader(QaQueUsr groupLeader) {
	this.groupLeader = groupLeader;
    }
}
