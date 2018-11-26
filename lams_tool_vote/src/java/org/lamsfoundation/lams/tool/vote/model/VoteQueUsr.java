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
import java.util.HashSet;
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
 * Persistent object/bean that defines the user for the Voting tool.
 * Provides accessors and mutators to get/set attributes
 * It maps to database table: tl_lavote11_que_usr
 * </p>
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_lavote11_usr")
public class VoteQueUsr implements Serializable, Comparable<VoteQueUsr> {

    private static final long serialVersionUID = 7303944502340276133L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "user_id")
    private Long queUsrId;

    @Column
    private String username;

    @Column
    private String fullname;

    @Column
    private boolean responseFinalised;

    @Column
    private boolean finalScreenRequested;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_session_id")
    private VoteSession voteSession;

    @OneToMany(mappedBy = "voteQueUsr", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VoteUsrAttempt> voteUsrAttempts;

    public VoteQueUsr(Long queUsrId, String username, String fullname, VoteSession voteSession,
	    Set<VoteUsrAttempt> voteUsrAttempts) {
	this.queUsrId = queUsrId;
	this.username = username;
	this.fullname = fullname;
	this.voteSession = voteSession;
	this.voteUsrAttempts = voteUsrAttempts;
    }

    public VoteQueUsr() {
    }

    public VoteQueUsr(Long queUsrId, Set<VoteUsrAttempt> voteUsrAttempts) {
	this.queUsrId = queUsrId;
	this.voteUsrAttempts = voteUsrAttempts;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getQueUsrId() {
	return this.queUsrId;
    }

    public void setQueUsrId(Long queUsrId) {
	this.queUsrId = queUsrId;
    }

    public String getUsername() {
	return this.username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getFullname() {
	return this.fullname;
    }

    public void setFullname(String fullname) {
	this.fullname = fullname;
    }

    public Set<VoteUsrAttempt> getVoteUsrAttempts() {
	if (this.voteUsrAttempts == null) {
	    setVoteUsrAttempts(new HashSet<VoteUsrAttempt>());
	}
	return this.voteUsrAttempts;
    }

    public void setMcUsrAttempts(Set<VoteUsrAttempt> voteUsrAttempts) {
	this.voteUsrAttempts = voteUsrAttempts;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    public VoteSession getVoteSession() {
	return voteSession;
    }

    public void setVoteSession(VoteSession voteSession) {
	this.voteSession = voteSession;
    }

    public void setVoteUsrAttempts(Set<VoteUsrAttempt> voteUsrAttempts) {
	this.voteUsrAttempts = voteUsrAttempts;
    }

    public boolean isResponseFinalised() {
	return responseFinalised;
    }

    public void setResponseFinalised(boolean responseFinalised) {
	this.responseFinalised = responseFinalised;
    }

    public boolean isFinalScreenRequested() {
	return finalScreenRequested;
    }

    public void setFinalScreenRequested(boolean finalScreenRequested) {
	this.finalScreenRequested = finalScreenRequested;
    }

    @Override
    public int compareTo(VoteQueUsr other) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - other.uid.longValue());
	}
    }
}