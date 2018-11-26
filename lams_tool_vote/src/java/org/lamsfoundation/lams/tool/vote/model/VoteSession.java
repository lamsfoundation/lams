/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the content for the Voting tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lavote11_session
 * </p>
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_lavote11_session")
public class VoteSession implements Serializable, Comparable<VoteSession> {

    private static final long serialVersionUID = 5053800653198292982L;

    public final static String INCOMPLETE = "INCOMPLETE";

    public static final String COMPLETED = "COMPLETED";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "vote_session_id")
    private Long voteSessionId;

    @Column(name = "session_start_date")
    private Date sessionStartDate;

    @Column(name = "session_end_date")
    private Date sessionEndDate;

    @Column(name = "session_status")
    private String sessionStatus;

    @Column
    private String session_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_content_id")
    private VoteContent voteContent;

    @OneToMany(mappedBy = "voteSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VoteQueUsr> voteQueUsers;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_leader_uid")
    private VoteQueUsr groupLeader;

    public VoteSession(Long voteSessionId, Date sessionStartDate, String sessionStatus, String session_name,
	    VoteContent voteContent, Set<VoteQueUsr> voteQueUsers) {
	this.voteSessionId = voteSessionId;
	this.sessionStartDate = sessionStartDate;
	this.sessionStatus = sessionStatus;
	this.session_name = session_name;
	this.voteContent = voteContent;
	this.voteQueUsers = voteQueUsers;
    }

    public VoteSession() {
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    public String getSessionStatus() {
	return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
	this.sessionStatus = sessionStatus;
    }

    public String getSession_name() {
	return session_name;
    }

    public void setSession_name(String session_name) {
	this.session_name = session_name;
    }

    public Long getVoteSessionId() {
	return voteSessionId;
    }

    public void setVoteSessionId(Long voteSessionId) {
	this.voteSessionId = voteSessionId;
    }

    public VoteContent getVoteContent() {
	return voteContent;
    }

    public void setVoteContent(VoteContent voteContent) {
	this.voteContent = voteContent;
    }

    public Set<VoteQueUsr> getVoteQueUsers() {
	return voteQueUsers;
    }

    public void setVoteQueUsers(Set<VoteQueUsr> voteQueUsers) {
	this.voteQueUsers = voteQueUsers;
    }

    public VoteQueUsr getGroupLeader() {
	return this.groupLeader;
    }

    public void setGroupLeader(VoteQueUsr groupLeader) {
	this.groupLeader = groupLeader;
    }

    @Override
    public int compareTo(VoteSession other) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - other.uid.longValue());
	}
    }
}