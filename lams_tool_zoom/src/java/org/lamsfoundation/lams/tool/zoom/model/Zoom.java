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

package org.lamsfoundation.lams.tool.zoom.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "tl_lazoom10_zoom")
public class Zoom implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = -336708242652214225L;

    private static final Logger logger = Logger.getLogger(Zoom.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "create_by")
    private Long createBy;

    @Column
    private String title;

    @Column
    private String instructions;

    @Column(name = "start_in_monitor")
    private boolean startInMonitor;

    @Column
    private Integer duration = 120;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id")
    private ZoomApi api;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "meeting_start_url")
    private String meetingStartUrl;

    @Column(name = "meeting_password")
    private String meetingPassword;

    @OneToMany(mappedBy = "zoom")
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<ZoomSession> zoomSessions;

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public Long getCreateBy() {
	return this.createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isStartInMonitor() {
	return startInMonitor;
    }

    public void setStartInMonitor(boolean startInMonitor) {
	this.startInMonitor = startInMonitor;
    }

    public Integer getDuration() {
	return duration;
    }

    public void setDuration(Integer duration) {
	this.duration = duration;
    }

    public boolean isContentInUse() {
	return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Set<ZoomSession> getZoomSessions() {
	return this.zoomSessions;
    }

    public void setZoomSessions(Set<ZoomSession> zoomSessions) {
	this.zoomSessions = zoomSessions;
    }

    public String getMeetingId() {
	return meetingId;
    }

    public void setMeetingId(String meetingId) {
	this.meetingId = meetingId;
    }

    public String getMeetingStartUrl() {
	return meetingStartUrl;
    }

    public void setMeetingStartUrl(String meetingStartUrl) {
	this.meetingStartUrl = meetingStartUrl;
    }

    public String getMeetingPassword() {
	return meetingPassword;
    }

    public void setMeetingPassword(String meetingPassword) {
	this.meetingPassword = meetingPassword;
    }

    public ZoomApi getApi() {
	return api;
    }

    public void setApi(ZoomApi api) {
	this.api = api;
    }

    @Override
    protected Object clone() {

	Zoom zoom = null;
	try {
	    zoom = (Zoom) super.clone();
	    zoom.setUid(null);

	    // create an empty set for the zoomSession
	    zoom.zoomSessions = new HashSet<>();

	} catch (CloneNotSupportedException cnse) {
	    logger.error("Error cloning " + Zoom.class);
	}
	return zoom;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null) || !(other instanceof Zoom)) {
	    return false;
	}
	Zoom castOther = (Zoom) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Zoom newInstance(Zoom fromContent, Long toContentId) {
	Zoom toContent = new Zoom();
	toContent = (Zoom) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }
}