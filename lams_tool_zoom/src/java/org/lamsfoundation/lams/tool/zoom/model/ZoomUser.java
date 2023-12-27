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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

@Entity
@Table(name = "tl_lazoom10_user")
public class ZoomUser implements java.io.Serializable {
    private static final long serialVersionUID = 8663555378960558429L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zoom_session_uid")
    private ZoomSession zoomSession;

    @Column
    private boolean finishedActivity;

    @Column(name = "meeting_join_url")
    private String meetingJoinUrl;

    public ZoomUser() {
    }

    public ZoomUser(UserDTO userDTO, ZoomSession zoomSession) {
	this.userId = userDTO.getUserID();
	this.firstName = userDTO.getFirstName();
	this.lastName = userDTO.getLastName();
	this.email = userDTO.getEmail();
	this.zoomSession = zoomSession;
	this.finishedActivity = false;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Integer getUserId() {
	return this.userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public boolean isFinishedActivity() {
	return finishedActivity;
    }

    public void setFinishedActivity(boolean finishedActivity) {
	this.finishedActivity = finishedActivity;
    }

    public ZoomSession getZoomSession() {
	return this.zoomSession;
    }

    public void setZoomSession(ZoomSession zoomSession) {
	this.zoomSession = zoomSession;
    }

    public String getMeetingJoinUrl() {
	return meetingJoinUrl;
    }

    public void setMeetingJoinUrl(String meetingJoinUrl) {
	this.meetingJoinUrl = meetingJoinUrl;
    }

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