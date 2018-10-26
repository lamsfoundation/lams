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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the content for the Voting tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lavote11_session
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class VoteSession implements Serializable, Comparable<VoteSession> {

    /**
     *
     */
    private static final long serialVersionUID = 5053800653198292982L;

    public final static String INCOMPLETE = "INCOMPLETE";

    public static final String COMPLETED = "COMPLETED";

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long voteSessionId;

    /** nullable persistent field */
    private Date sessionStartDate;

    /** nullable persistent field */
    private Date sessionEndDate;

    /** nullable persistent field */
    private String sessionStatus;

    private String session_name;

    /** nullable persistent field */
    private Long voteContentId;

    /** nullable persistent field */
    private VoteContent voteContent;

    /** persistent field */
    private Set<VoteQueUsr> voteQueUsers;

    /** persistent field */
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

    /** default constructor */
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

    /**
     * @return Returns the sessionEndDate.
     */
    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    /**
     * @param sessionEndDate
     *            The sessionEndDate to set.
     */
    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     * @return Returns the sessionStartDate.
     */
    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    /**
     * @param sessionStartDate
     *            The sessionStartDate to set.
     */
    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     * @return Returns the sessionStatus.
     */
    public String getSessionStatus() {
	return sessionStatus;
    }

    /**
     * @param sessionStatus
     *            The sessionStatus to set.
     */
    public void setSessionStatus(String sessionStatus) {
	this.sessionStatus = sessionStatus;
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

    /**
     * @return Returns the voteSessionId.
     */
    public Long getVoteSessionId() {
	return voteSessionId;
    }

    /**
     * @param voteSessionId
     *            The voteSessionId to set.
     */
    public void setVoteSessionId(Long voteSessionId) {
	this.voteSessionId = voteSessionId;
    }

    /**
     * @return Returns the voteContentId.
     */
    public Long getVoteContentId() {
	return voteContentId;
    }

    /**
     * @param voteContentId
     *            The voteContentId to set.
     */
    public void setVoteContentId(Long voteContentId) {
	this.voteContentId = voteContentId;
    }

    /**
     * @return Returns the voteContent.
     */
    public VoteContent getVoteContent() {
	return voteContent;
    }

    /**
     * @param voteContent
     *            The voteContent to set.
     */
    public void setVoteContent(VoteContent voteContent) {
	this.voteContent = voteContent;
    }

    /**
     * @return Returns the voteQueUsers.
     */
    public Set<VoteQueUsr> getVoteQueUsers() {
	return voteQueUsers;
    }

    /**
     * @param voteQueUsers
     *            The voteQueUsers to set.
     */
    public void setVoteQueUsers(Set<VoteQueUsr> voteQueUsers) {
	this.voteQueUsers = voteQueUsers;
    }

    /**
     * @return Returns the groupLeader.
     */
    public VoteQueUsr getGroupLeader() {
	return this.groupLeader;
    }

    /**
     * @param groupLeader
     *            The groupLeader to set.
     */
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
