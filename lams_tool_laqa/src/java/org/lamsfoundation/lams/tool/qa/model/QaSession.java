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

package org.lamsfoundation.lams.tool.qa.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.qa.Nullable;

/**
 * Holds tool sessions
 *
 * @author Ozgur Demirtas
 * 
 */
@Entity
@Table(name = "tl_laqa11_session")
public class QaSession implements Serializable, Comparable, Nullable {

    public final static String INCOMPLETE = "INCOMPLETE";

    public static final String COMPLETED = "COMPLETED";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "qa_session_id")
    private Long qaSessionId;

    @Column(name = "session_start_date")
    private Date session_start_date;

    @Column(name = "session_end_date")
    private Date session_end_date;

    @Column(name = "session_status")
    private String session_status;

    @Column(name = "session_name")
    private String session_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qa_content_id")
    private QaContent qaContent;

    @OneToMany(mappedBy = "qaSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QaQueUsr> qaQueUsers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qa_group_leader_uid")
    private QaQueUsr groupLeader;

    public QaSession() {
	this.qaQueUsers = new TreeSet<QaQueUsr>();
    };

    public QaSession(Long qaSessionId, Date session_start_date, Date session_end_date, String session_status,
	    QaContent qaContent, Set<QaQueUsr> qaQueUsers) {
	this.qaSessionId = qaSessionId;
	this.session_start_date = session_start_date;
	this.session_end_date = session_end_date;
	this.session_status = session_status;
	this.qaContent = qaContent;
	this.qaQueUsers = qaQueUsers != null ? qaQueUsers : new TreeSet<QaQueUsr>();
    }

    public QaSession(Long qaSessionId, Date session_start_date, String session_status, String session_name,
	    QaContent qaContent, Set<QaQueUsr> qaQueUsers) {
	this.qaSessionId = qaSessionId;
	this.session_start_date = session_start_date;
	this.session_status = session_status;
	this.session_name = session_name;
	this.qaContent = qaContent;
	this.qaQueUsers = qaQueUsers != null ? qaQueUsers : new TreeSet<QaQueUsr>();
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

    public Set<QaQueUsr> getQaQueUsers() {
	return this.qaQueUsers;
    }

    public void setQaQueUsers(Set<QaQueUsr> qaQueUsers) {
	this.qaQueUsers = qaQueUsers;
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
