/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.noticeboard.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author mtruong
 *
 *         <p>
 *         The NoticeboardUser class represents the learners that will participate
 *         the noticeboard activity. Each learner will have a tool session id which represents
 *         which group they belong to, or there is no grouping, then each learner will have their own
 *         tool session id.
 *         </p>
 *         <p>
 *         The userStatus can be of two values
 *         <ul>
 *         <li>INCOMPLETE: Which means that the user has not yet finished this activity.</li>
 *         <li>COMPLETED: Which means the user has completed this activity (when the user clicks on finish)</li>
 *         <ul>
 *         </p>
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tl_lanb11_user")
public class NoticeboardUser implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nb_session_uid")
    private NoticeboardSession nbSession;

    @Column
    private String username;

    @Column
    private String fullname;

    @Column(name = "user_status")
    private String userStatus;

    public final static String INCOMPLETE = "INCOMPLETE";

    public static final String COMPLETED = "COMPLETED";

    public NoticeboardUser() {

    }

    /** minimal constructor */
    public NoticeboardUser(Long userId, NoticeboardSession nbSession) {
	this.userId = userId;
	this.nbSession = nbSession;
	this.userStatus = NoticeboardUser.INCOMPLETE;
    }

    public NoticeboardUser(Long userId) {
	this.userId = userId;
	this.userStatus = NoticeboardUser.INCOMPLETE;
    }

    /** full constructor */
    public NoticeboardUser(Long userId, NoticeboardSession nbSession, String username, String fullname, String status) {
	this.userId = userId;
	this.nbSession = nbSession;
	this.username = username;
	this.fullname = fullname;
	this.userStatus = status;
    }

    public String getFullname() {
	return fullname;
    }

    public void setFullname(String fullname) {
	this.fullname = fullname;
    }

    public NoticeboardSession getNbSession() {
	return nbSession;
    }

    public void setNbSession(NoticeboardSession nbSession) {
	this.nbSession = nbSession;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getUserStatus() {
	return userStatus;
    }

    public void setUserStatus(String userStatus) {
	this.userStatus = userStatus;
    }
}