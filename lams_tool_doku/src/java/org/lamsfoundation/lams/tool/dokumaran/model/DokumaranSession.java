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

import org.hibernate.annotations.SortComparator;
import org.lamsfoundation.lams.etherpad.util.EtherpadUtil;
import org.lamsfoundation.lams.tool.dokumaran.util.DokumaranSessionComparator;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Dokumaran session
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_ladoku11_session")
public class DokumaranSession {

    public static final DokumaranSessionComparator SESSION_NAME_COMPARATOR = new DokumaranSessionComparator();

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tl_ladoku11_gallery_walk_cluster", joinColumns = {
	    @JoinColumn(name = "source_session_uid") }, inverseJoinColumns = {
	    @JoinColumn(name = "target_session_uid") })
    @SortComparator(DokumaranSessionComparator.class)
    private Set<DokumaranSession> galleryWalkCluster = new TreeSet<>(SESSION_NAME_COMPARATOR);

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
     * 	The session name to set.
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
     * 	The etherpadReadOnlyId to set.
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
     * 	The etherpadReadOnlyId to set.
     */
    public void setEtherpadReadOnlyId(String etherpadReadOnlyId) {
	this.etherpadReadOnlyId = etherpadReadOnlyId;
    }

    public String getPadId() {
	return EtherpadUtil.getPadId(etherpadGroupId);
    }

    public Set<DokumaranSession> getGalleryWalkCluster() {
	return galleryWalkCluster;
    }

    public void setGalleryWalkCluster(Set<DokumaranSession> galleryWalkCluster) {
	this.galleryWalkCluster = galleryWalkCluster;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	DokumaranSession that = (DokumaranSession) o;
	return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
	return Objects.hash(uid);
    }
}