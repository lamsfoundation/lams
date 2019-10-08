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

package org.lamsfoundation.lams.tool.scratchie.model;

import java.util.Date;
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

import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;

/**
 * Scratchie
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lascrt11_session")
public class ScratchieSession {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "session_name")
    private String sessionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scratchie_uid")
    private Scratchie scratchie;

    @Column(name = "session_start_date")
    private Date sessionStartDate;

    @Column(name = "session_end_date")
    private Date sessionEndDate;

    //date when user has started activity (pressed start button) that has time limitation
    @Column(name = "time_limit_launched_date")
    private Date timeLimitLaunchedDate;

    // finish or not
    @Column
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_leader_uid")
    private ScratchieUser groupLeader;

    @Column
    private int mark;

    @Column(name = "scratching_finished")
    private boolean scratchingFinished;

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	this.uid = uuid;
    }

    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    public Date getTimeLimitLaunchedDate() {
	return timeLimitLaunchedDate;
    }

    public void setTimeLimitLaunchedDate(Date timeLimitLaunchedDate) {
	this.timeLimitLaunchedDate = timeLimitLaunchedDate;
    }

    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    public Scratchie getScratchie() {
	return scratchie;
    }

    public void setScratchie(Scratchie scratchie) {
	this.scratchie = scratchie;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     *
     * @return Returns the session name
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     * @param sessionName
     *            The session name to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public ScratchieUser getGroupLeader() {
	return this.groupLeader;
    }

    public void setGroupLeader(ScratchieUser groupLeader) {
	this.groupLeader = groupLeader;
    }

    public boolean isUserGroupLeader(Long userUid) {
	boolean isUserLeader = (this.groupLeader != null) && userUid.equals(this.groupLeader.getUid());
	return isUserLeader;
    }

    /**
     * Mark scored by a leader and shared by all users in a group.
     *
     * @return
     */
    public int getMark() {
	return mark;
    }

    public void setMark(int mark) {
	this.mark = mark;
    }

    /**
     * Indicates whether leader has pressed Continue button in learning thus finishing scratching. And is shared by all
     * users in a group.
     *
     * @return
     */
    public boolean isScratchingFinished() {
	return scratchingFinished;
    }

    public void setScratchingFinished(boolean scratchingFinished) {
	this.scratchingFinished = scratchingFinished;
    }

    public boolean isSessionFinished() {
	boolean isSessionFinished = (status == ScratchieConstants.COMPLETED);
	return isSessionFinished;
    }

}
