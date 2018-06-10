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

public class ZoomUser implements java.io.Serializable {

    private static final long serialVersionUID = 8663555378960558429L;

    // Persistent Fields

    private Long uid;

    private Integer userId;

    private ZoomSession zoomSession;

    private boolean finishedActivity;

    private Long notebookEntryUID;

    private String meetingJoinUrl;

    // Constructors

    public ZoomUser() {

    }

    public ZoomUser(Integer userID, ZoomSession zoomSession) {
	this.userId = userID;
	this.zoomSession = zoomSession;
	this.finishedActivity = false;
    }

    // Property accessors

    /**
     *
     */
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
    public Integer getUserId() {
	return this.userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    /**
     *
     */
    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }

    /**
     *
     *
     *
     */
    public ZoomSession getZoomSession() {
	return this.zoomSession;
    }

    public void setZoomSession(ZoomSession zoomSession) {
	this.zoomSession = zoomSession;
    }

    /**
     *
     */
    public Long getNotebookEntryUID() {
	return notebookEntryUID;
    }

    public void setNotebookEntryUID(Long notebookEntryUID) {
	this.notebookEntryUID = notebookEntryUID;
    }

    public String getMeetingJoinUrl() {
	return meetingJoinUrl;
    }

    public void setMeetingJoinUrl(String meetingJoinUrl) {
	this.meetingJoinUrl = meetingJoinUrl;
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
	buffer.append("userId").append("='").append(getUserId()).append("' ");
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
	if (!(other instanceof ZoomUser)) {
	    return false;
	}
	ZoomUser castOther = (ZoomUser) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }
}
