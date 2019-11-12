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

package org.lamsfoundation.lams.tool.dokumaran.model;

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

import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;

/**
 * Dokumaran session
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_ladoku11_session")
public class DokumaranSession {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "session_id")
    private Long sessionId;

    @Column(name = "session_name")
    private String sessionName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dokumaran_uid")
    private Dokumaran dokumaran;

    @Column(name = "session_start_date")
    private Date sessionStartDate;

    @Column(name = "session_end_date")
    private Date sessionEndDate;

    // finished or not
    @Column
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_leader_uid")
    private DokumaranUser groupLeader;

    @Column(name = "etherpad_group_id")
    private String etherpadGroupId;

    @Column(name = "etherpad_read_only_id")
    private String etherpadReadOnlyId;

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @return Returns the learnerID.
     */
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

    public Dokumaran getDokumaran() {
	return dokumaran;
    }

    public void setDokumaran(Dokumaran dokumaran) {
	this.dokumaran = dokumaran;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
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

    public DokumaranUser getGroupLeader() {
	return this.groupLeader;
    }

    public void setGroupLeader(DokumaranUser groupLeader) {
	this.groupLeader = groupLeader;
    }

    /**
     * @return Returns the etherpadReadOnlyId
     */
    public String getEtherpadGroupId() {
	return etherpadGroupId;
    }

    /**
     * @param etherpadReadOnlyId
     *            The etherpadReadOnlyId to set.
     */
    public void setEtherpadGroupId(String etherpadGroupId) {
	this.etherpadGroupId = etherpadGroupId;
    }

    /**
     * @return Returns the etherpadReadOnlyId
     */
    public String getEtherpadReadOnlyId() {
	return etherpadReadOnlyId;
    }

    /**
     * @param etherpadReadOnlyId
     *            The etherpadReadOnlyId to set.
     */
    public void setEtherpadReadOnlyId(String etherpadReadOnlyId) {
	this.etherpadReadOnlyId = etherpadReadOnlyId;
    }

    public String getPadId() {
	// HashUtil.sha1(DokumaranConstants.DEFAULT_PAD_NAME + sessionId);
	return etherpadGroupId + "$" + DokumaranConstants.DEFAULT_PAD_NAME;
    }

}
