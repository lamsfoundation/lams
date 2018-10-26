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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the user attempt for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lavote11_usr_attempt
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class VoteUsrAttempt implements Serializable, Comparable<VoteUsrAttempt> {

    /**
     *
     */
    private static final long serialVersionUID = 6756874212158405114L;

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long attemptId;

    /** nullable persistent field */
    private Date attemptTime;

    /** nullable persistent field */
    private String timeZone;

    private String userEntry;

    private Long queUsrId;

    private boolean visible;

    private boolean singleUserEntry;

    /** persistent field */
    private VoteQueContent voteQueContent;

    /** persistent field */
    private VoteQueUsr voteQueUsr;

    public VoteUsrAttempt(Date attemptTime, String timeZone, VoteQueContent voteQueContent, VoteQueUsr voteQueUsr,
	    String userEntry, boolean visible) {
	this.attemptTime = attemptTime;
	this.timeZone = timeZone;
	this.voteQueContent = voteQueContent;
	//this.voteQueContentId = voteQueContent.getVoteQueContentId();
	this.voteQueUsr = voteQueUsr;
	this.userEntry = userEntry;
	this.visible = visible;
    }

    /** default constructor */
    public VoteUsrAttempt() {
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getAttemptId() {
	return this.attemptId;
    }

    public void setAttemptId(Long attemptId) {
	this.attemptId = attemptId;
    }

    public Date getAttemptTime() {
	return this.attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
	this.attemptTime = attemptTime;
    }

    public String getTimeZone() {
	return this.timeZone;
    }

    public void setTimeZone(String timeZone) {
	this.timeZone = timeZone;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("nomination:", voteQueContent.getQuestion())
		.append("userEntry:", userEntry).toString();
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
     * @return Returns the voteQueContent.
     */
    public VoteQueContent getVoteQueContent() {
	return voteQueContent;
    }

    /**
     * @param voteQueContent
     *            The voteQueContent to set.
     */
    public void setVoteQueContent(VoteQueContent voteQueContent) {
	this.voteQueContent = voteQueContent;
    }

    /**
     * @return Returns the voteQueUsr.
     */
    public VoteQueUsr getVoteQueUsr() {
	return voteQueUsr;
    }

    /**
     * @param voteQueUsr
     *            The voteQueUsr to set.
     */
    public void setVoteQueUsr(VoteQueUsr voteQueUsr) {
	this.voteQueUsr = voteQueUsr;
    }

    /**
     * @return Returns the userEntry.
     */
    public String getUserEntry() {
	return userEntry;
    }

    /**
     * @param userEntry
     *            The userEntry to set.
     */
    public void setUserEntry(String userEntry) {
	this.userEntry = userEntry;
    }

    /**
     * @return Returns the singleUserEntry.
     */
    public boolean isSingleUserEntry() {
	return singleUserEntry;
    }

    /**
     * @param singleUserEntry
     *            The singleUserEntry to set.
     */
    public void setSingleUserEntry(boolean singleUserEntry) {
	this.singleUserEntry = singleUserEntry;
    }

    /**
     * @return Returns the visible.
     */
    public boolean isVisible() {
	return visible;
    }

    /**
     * @param visible
     *            The visible to set.
     */
    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    @Override
    public int compareTo(VoteUsrAttempt other) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - other.uid.longValue());
	}
    }

}
