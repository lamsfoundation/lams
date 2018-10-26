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
public class VoteQueUsr implements Serializable, Comparable<VoteQueUsr> {

    private static final long serialVersionUID = 7303944502340276133L;

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long queUsrId;

    /** nullable persistent field */
    private String username;

    /** nullable persistent field */
    private String fullname;

    private boolean responseFinalised;

    private boolean finalScreenRequested;

    private Long voteSessionId;

    /** nullable persistent field */
    private VoteSession voteSession;

    /** persistent field */
    private Set<VoteUsrAttempt> voteUsrAttempts;

    /** full constructor */
    public VoteQueUsr(Long queUsrId, String username, String fullname, VoteSession voteSession,
	    Set<VoteUsrAttempt> voteUsrAttempts) {
	this.queUsrId = queUsrId;
	this.username = username;
	this.fullname = fullname;
	this.voteSession = voteSession;
	this.voteSessionId = voteSession.getUid();
	this.voteUsrAttempts = voteUsrAttempts;
    }

    /** default constructor */
    public VoteQueUsr() {
    }

    /** minimal constructor */
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

    /**
     * Beware, it references Votesession.uid field (not the Votesession.voteSessionId).
     *
     * @return Returns the voteSessionId.
     */
    public Long getVoteSessionId() {
	return voteSessionId;
    }

    /**
     * Beware, it references Votesession.uid field (not the Votesession.voteSessionId).
     *
     * @param voteSessionId
     *            The voteSessionId to set.
     */
    public void setVoteSessionId(Long voteSessionId) {
	this.voteSessionId = voteSessionId;
    }

    /**
     * @return Returns the voteSession.
     */
    public VoteSession getVoteSession() {
	return voteSession;
    }

    /**
     * @param voteSession
     *            The voteSession to set.
     */
    public void setVoteSession(VoteSession voteSession) {
	this.voteSession = voteSession;
    }

    /**
     * @param voteUsrAttempts
     *            The voteUsrAttempts to set.
     */
    public void setVoteUsrAttempts(Set<VoteUsrAttempt> voteUsrAttempts) {
	this.voteUsrAttempts = voteUsrAttempts;
    }

    /**
     * @return Returns the responseFinalised.
     */
    public boolean isResponseFinalised() {
	return responseFinalised;
    }

    /**
     * @param responseFinalised
     *            The responseFinalised to set.
     */
    public void setResponseFinalised(boolean responseFinalised) {
	this.responseFinalised = responseFinalised;
    }

    /**
     * @return Returns the finalScreenRequested.
     */
    public boolean isFinalScreenRequested() {
	return finalScreenRequested;
    }

    /**
     * @param finalScreenRequested
     *            The finalScreenRequested to set.
     */
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
