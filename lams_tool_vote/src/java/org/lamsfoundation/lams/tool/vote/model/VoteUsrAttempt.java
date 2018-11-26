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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * Persistent object/bean that defines the user attempt for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lavote11_usr_attempt
 * </p>
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_lavote11_usr_attempt")
public class VoteUsrAttempt implements Serializable, Comparable<VoteUsrAttempt> {

    private static final long serialVersionUID = 6756874212158405114L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "attempt_time")
    private Date attemptTime;

    @Column(name = "time_zone")
    private String timeZone;

    @Column
    private String userEntry;

    @Column
    private boolean visible;

    @Column
    private boolean singleUserEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_nomination_content_id")
    private VoteQueContent voteQueContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "que_usr_id")
    private VoteQueUsr voteQueUsr;

    public VoteUsrAttempt(Date attemptTime, String timeZone, VoteQueContent voteQueContent, VoteQueUsr voteQueUsr,
	    String userEntry, boolean visible) {
	this.attemptTime = attemptTime;
	this.timeZone = timeZone;
	this.voteQueContent = voteQueContent;
	this.voteQueUsr = voteQueUsr;
	this.userEntry = userEntry;
	this.visible = visible;
    }

    public VoteUsrAttempt() {
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
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

    public VoteQueContent getVoteQueContent() {
	return voteQueContent;
    }

    public void setVoteQueContent(VoteQueContent voteQueContent) {
	this.voteQueContent = voteQueContent;
    }

    public VoteQueUsr getVoteQueUsr() {
	return voteQueUsr;
    }

    public void setVoteQueUsr(VoteQueUsr voteQueUsr) {
	this.voteQueUsr = voteQueUsr;
    }

    public String getUserEntry() {
	return userEntry;
    }

    public void setUserEntry(String userEntry) {
	this.userEntry = userEntry;
    }

    public boolean isSingleUserEntry() {
	return singleUserEntry;
    }

    public void setSingleUserEntry(boolean singleUserEntry) {
	this.singleUserEntry = singleUserEntry;
    }

    public boolean isVisible() {
	return visible;
    }

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