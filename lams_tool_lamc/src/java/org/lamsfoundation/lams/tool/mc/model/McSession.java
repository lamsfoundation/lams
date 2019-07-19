/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * Persistent object/bean that defines the content for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_session
 * </p>
 *
 * @author Ozgur Demirtas
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tl_lamc11_session")
public class McSession implements Serializable, Comparable<McSession> {

    public final static String INCOMPLETE = "INCOMPLETE";

    public static final String COMPLETED = "COMPLETED";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "mc_session_id")
    private Long mcSessionId;

    @Column(name = "session_start_date")
    private Date sessionStartDate;

    @Column(name = "session_end_date")
    private Date sessionEndDate;

    @Column(name = "session_status")
    private String sessionStatus;

    @Column(name = "session_name")
    private String session_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mc_content_id")
    private org.lamsfoundation.lams.tool.mc.model.McContent mcContent;

    @OneToMany(mappedBy = "mcSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<McQueUsr> mcQueUsers;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "mc_group_leader_uid")
    private McQueUsr groupLeader;

    /** full constructor */
    public McSession(Long mcSessionId, Date sessionStartDate, Date sessionEndDate, String sessionStatus,
	    org.lamsfoundation.lams.tool.mc.model.McContent mcContent, List<McQueUsr> mcQueUsers) {
	this.mcSessionId = mcSessionId;
	this.sessionStartDate = sessionStartDate;
	this.sessionEndDate = sessionEndDate;
	this.sessionStatus = sessionStatus;
	this.mcContent = mcContent;
	this.mcQueUsers = mcQueUsers != null ? mcQueUsers : new ArrayList<>();
    }

    public McSession(Long mcSessionId, Date sessionStartDate, String sessionStatus, String session_name,
	    org.lamsfoundation.lams.tool.mc.model.McContent mcContent, List<McQueUsr> mcQueUsers) {
	this.mcSessionId = mcSessionId;
	this.sessionStartDate = sessionStartDate;
	this.sessionStatus = sessionStatus;
	this.session_name = session_name;
	this.mcContent = mcContent;
	this.mcQueUsers = mcQueUsers != null ? mcQueUsers : new ArrayList<>();
    }

    public McSession(Long mcSessionId, Date sessionStartDate, String sessionStatus,
	    org.lamsfoundation.lams.tool.mc.model.McContent mcContent, List<McQueUsr> mcQueUsers) {
	this.mcSessionId = mcSessionId;
	this.sessionStartDate = sessionStartDate;
	this.sessionStatus = sessionStatus;
	this.mcContent = mcContent;
	this.mcQueUsers = mcQueUsers != null ? mcQueUsers : new ArrayList<>();
    }

    /** default constructor */
    public McSession() {
	this.mcQueUsers = new ArrayList<>();
    }

    /** minimal constructor */
    public McSession(Long mcSessionId, List<McQueUsr> mcQueUsers) {
	this.mcSessionId = mcSessionId;
	this.mcQueUsers = mcQueUsers != null ? mcQueUsers : new ArrayList<>();
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getMcSessionId() {
	return this.mcSessionId;
    }

    public void setMcSessionId(Long mcSessionId) {
	this.mcSessionId = mcSessionId;
    }

    public org.lamsfoundation.lams.tool.mc.model.McContent getMcContent() {
	return this.mcContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.mc.model.McContent mcContent) {
	this.mcContent = mcContent;
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
     * @return Returns the mcQueUsers.
     */

    public List<McQueUsr> getMcQueUsers() {
	return this.mcQueUsers;
    }

    /**
     * @param mcQueUsers
     *            The mcQueUsers to set.
     */
    public void setMcQueUsers(List<McQueUsr> mcQueUsers) {
	this.mcQueUsers = mcQueUsers;
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
     * @return groupLeader
     */
    public McQueUsr getGroupLeader() {
	return this.groupLeader;
    }

    /**
     * @param groupLeader
     */
    public void setGroupLeader(McQueUsr groupLeader) {
	this.groupLeader = groupLeader;
    }

    public boolean isUserGroupLeader(McQueUsr user) {

	McQueUsr groupLeader = this.getGroupLeader();

	boolean isUserLeader = (groupLeader != null) && user.getUid().equals(groupLeader.getUid());
	return isUserLeader;
    }

    @Override
    public int compareTo(McSession other) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - other.uid.longValue());
	}
    }

}
