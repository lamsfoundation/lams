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
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;

/**
 * Scratchie
 *
 * @author Andrey Balan
 *
 * @hibernate.class table="tl_lascrt11_session"
 *
 */
public class ScratchieSession {

    private static Logger log = Logger.getLogger(ScratchieSession.class);

    private Long uid;
    private Long sessionId;
    private String sessionName;
    private Scratchie scratchie;
    private Date sessionStartDate;
    private Date sessionEndDate;
    //date when user has started activity (pressed start button) that has time limitation
    private Date timeLimitLaunchedDate;
    // finish or not
    private int status;
    // scratchie Items
    private Set scratchieItems;
    private ScratchieUser groupLeader;
    private int mark;
    private boolean scratchingFinished;

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the learnerID.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	this.uid = uuid;
    }

    /**
     * @hibernate.property column="session_end_date"
     * @return
     */
    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }
    
    /**
     * @hibernate.property column="time_limit_launched_date"
     * @return
     */
    public Date getTimeLimitLaunchedDate() {
	return timeLimitLaunchedDate;
    }

    public void setTimeLimitLaunchedDate(Date timeLimitLaunchedDate) {
	this.timeLimitLaunchedDate = timeLimitLaunchedDate;
    }

    /**
     * @hibernate.property column="session_start_date"
     *
     * @return
     */
    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     * @hibernate.property
     * @return
     */
    public int getStatus() {
	return status;
    }

    public void setStatus(int status) {
	this.status = status;
    }

    /**
     * @hibernate.many-to-one column="scratchie_uid" cascade="none"
     * @return
     */
    public Scratchie getScratchie() {
	return scratchie;
    }

    public void setScratchie(Scratchie scratchie) {
	this.scratchie = scratchie;
    }

    /**
     * @hibernate.property column="session_id"
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @hibernate.property column="session_name" length="250"
     * @return Returns the session name
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     *
     * @param sessionName
     *            The session name to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    /**
     *
     *
     * @hibernate.set lazy="true" inverse="false" cascade="all" order-by="create_date desc"
     * @hibernate.collection-key column="session_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem"
     *
     * @return
     */
    public Set getScratchieItems() {
	return scratchieItems;
    }

    public void setScratchieItems(Set scratchieItems) {
	this.scratchieItems = scratchieItems;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="group_leader_uid"
     */
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
     * @hibernate.property column="mark"
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
     * @hibernate.property column="scratching_finished"
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
