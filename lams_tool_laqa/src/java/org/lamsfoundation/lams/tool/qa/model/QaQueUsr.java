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

/**
 * 
 * Represents tool users.
 * 
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_laqa11_que_usr")
public class QaQueUsr implements Serializable, Comparable<QaQueUsr> {

    private static final long serialVersionUID = -6768077344827699440L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "que_usr_id")
    private Long queUsrId;

    @Column
    private String username;

    @Column
    private String fullname;

    @Column
    private boolean responseFinalized;

    @Column
    private boolean learnerFinished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qa_session_id")
    private QaSession qaSession;

    @OneToMany(mappedBy = "qaQueUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<QaUsrResp> qaUsrResps;

    public QaQueUsr() {
	this.qaUsrResps = new TreeSet<QaUsrResp>();
    };

    /** full constructor */
    public QaQueUsr(Long queUsrId, String username, String fullname, QaSession qaSession, Set<QaUsrResp> qaUsrResps) {
	this.queUsrId = queUsrId;
	this.username = username;
	this.fullname = fullname;
	this.qaSession = qaSession;
	this.qaUsrResps = qaUsrResps != null ? qaUsrResps : new TreeSet<QaUsrResp>();
    }

    /**
     * Copy constructor; We copy all data except the hibernate id field.
     *
     * @param queUsr
     *            the original survey question user object.
     * @return the survey question user object.
     */
    public QaQueUsr newInstance(QaQueUsr queUsr) {
	return new QaQueUsr(queUsr.getQueUsrId(), queUsr.getUsername(), queUsr.getFullname(), queUsr.getQaSession(),
		queUsr.getQaUsrResps());
    }

    /**
     * @return Returns the fullname.
     */
    public String getFullname() {
	return fullname;
    }

    /**
     * @param fullName
     *            The fullName to set.
     */
    public void setFullname(String fullname) {
	this.fullname = fullname;
    }

    /**
     * @return Returns the qaSession.
     */
    public QaSession getQaSession() {
	return qaSession;
    }

    /**
     * @param qaSession
     *            The qaSession to set.
     */
    public void setQaSession(QaSession qaSession) {
	this.qaSession = qaSession;
    }

    /**
     * @return Returns the qaUsrResps.
     */
    public Set<QaUsrResp> getQaUsrResps() {
	return this.qaUsrResps;
    }

    /**
     * @param qaUsrResps
     *            The qaUsrResps to set.
     */
    public void setQaUsrResps(Set<QaUsrResp> qaUsrResps) {
	this.qaUsrResps = qaUsrResps;
    }

    /**
     * @return Returns the queUsrId.
     */
    public Long getQueUsrId() {
	return queUsrId;
    }

    /**
     * @param queUsrId
     *            The queUsrId to set.
     */
    public void setQueUsrId(Long queUsrId) {
	this.queUsrId = queUsrId;
    }

    /**
     * @return Returns the username.
     */
    public String getUsername() {
	return username;
    }

    /**
     * @param username
     *            The username to set.
     */
    public void setUsername(String username) {
	this.username = username;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("queUsrId", getQueUsrId()).append("username", getUsername())
		.append("full name", getFullname()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof QaQueUsr)) {
	    return false;
	}
	QaQueUsr castOther = (QaQueUsr) other;
	return new EqualsBuilder().append(this.getQueUsrId(), castOther.getQueUsrId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getQueUsrId()).toHashCode();
    }

    /**
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the responseFinalized.
     */
    public boolean isResponseFinalized() {
	return responseFinalized;
    }

    /**
     * @param responseFinalized
     *            The responseFinalized to set.
     */
    public void setResponseFinalized(boolean responseFinalized) {
	this.responseFinalized = responseFinalized;
    }

    /**
     * @return Returns the learnerFinished.
     */
    public boolean isLearnerFinished() {
	return learnerFinished;
    }

    /**
     * @param learnerFinished
     *            The learnerFinished to set.
     */
    public void setLearnerFinished(boolean learnerFinished) {
	this.learnerFinished = learnerFinished;
    }

    @Override
    public int compareTo(QaQueUsr user) {
	if (user.getUid() != null && uid != null) {
	    return user.getUid().compareTo(uid) * -1;
	} else {
	    return 1;
	}
    }

}
