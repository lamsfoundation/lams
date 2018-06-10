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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

public class Zoom implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = -336708242652214225L;

    private static final Logger logger = Logger.getLogger(Zoom.class);

    private IToolContentHandler toolContentHandler;

    // Persistent Fields

    private Long uid;

    private Date createDate;

    private Date updateDate;

    private Long createBy;

    private String title;

    private String instructions;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean contentInUse;

    private boolean defineLater;

    private Long toolContentId;

    private ZoomApi api;

    private String meetingId;

    private String meetingStartUrl;

    private Set<ZoomSession> zoomSessions;

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     *
     */

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     *
     *
     */

    public Long getCreateBy() {
	return this.createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     *
     *
     */

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     *
     */

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     *
     */

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

    public IToolContentHandler getToolContentHandler() {
	return toolContentHandler;
    }

    public void setToolContentHandler(IToolContentHandler toolContentHandler) {
	this.toolContentHandler = toolContentHandler;
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
	    zoom.zoomSessions = new HashSet<ZoomSession>();

	} catch (CloneNotSupportedException cnse) {
	    logger.error("Error cloning " + Zoom.class);
	}
	return zoom;
    }

    /**
     * toString
     *
     * @return String
     */
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
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof Zoom)) {
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

    public static Zoom newInstance(Zoom fromContent, Long toContentId, IToolContentHandler zoomToolContentHandler) {
	Zoom toContent = new Zoom();
	fromContent.toolContentHandler = zoomToolContentHandler;
	toContent = (Zoom) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }
}
