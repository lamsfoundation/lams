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


package org.lamsfoundation.lams.tool.noticeboard;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * The NoticeboardSession class represents a tool session for a noticeboard activity. Each tool session may represent a
 * group of users, if grouping is used, or may represent one learner if there is no grouping for this particular
 * activity.
 * </p>
 * <br>
 * <p>
 * The session status has three possible status':
 * <ul>
 * <li>NOT_ATTEMPTED: which means that the tool session has been established, but no learners have reached this activity
 * yet
 * <li>INCOMPLETE: which means that a learner has reached this activity
 * <li>COMPLETED: The session status will never be set to complete as you don't know when this tool is going to end.
 * </ul>
 * </p>
 *
 * @author mtruong
 *
 */

public class NoticeboardSession implements Serializable {

    public final static String NOT_ATTEMPTED = "NOT_ATTEMPTED";

    public final static String INCOMPLETE = "INCOMPLETE";

    public static final String COMPLETED = "COMPLETED";

    /** identifier field */
    private Long uid;

    /** persistent field */
    private Long nbSessionId;

    /** persistent field */
    private String nbSessionName;

    /** nullable persistent field */
    private Date sessionStartDate;

    /** nullable persistent field */
    private Date sessionEndDate;

    /** nullable persistent field */
    private String sessionStatus;

    /** persistent field */
    private NoticeboardContent nbContent;

    /** persistent field */
    private Set<NoticeboardUser> nbUsers = new HashSet<NoticeboardUser>();

    /** default constructor */
    public NoticeboardSession() {

    }

    /** full constructor */
    public NoticeboardSession(Long nbSessionId, String nbSessionName, NoticeboardContent nbContent,
	    Date sessionStartDate, Date sessionEndDate, String sessionStatus) {
	this.nbSessionId = nbSessionId;
	this.nbSessionName = nbSessionName;
	// this.nbContentId = nbContentId;
	this.nbContent = nbContent;
	this.sessionStartDate = sessionStartDate;
	this.sessionEndDate = sessionEndDate;
	this.sessionStatus = sessionStatus;
    }

    /**
     * Constructor used when creating a new noticeboardSession given the noticeboardContent id
     */

    public NoticeboardSession(Long nbSessionId, String nbSessionName, NoticeboardContent nbContent,
	    Date sessionStartDate, String sessionStatus) {
	this.nbSessionId = nbSessionId;
	this.nbSessionName = nbSessionName;
	this.nbContent = nbContent;
	this.sessionStartDate = sessionStartDate;
	this.sessionEndDate = null;
	this.sessionStatus = sessionStatus;
    }

    public NoticeboardSession(Long nbSessionId, String nbSessionName, NoticeboardContent nbContent) {
	this.nbSessionId = nbSessionId;
	this.nbSessionName = nbSessionName;
	this.nbContent = nbContent;
	this.sessionStartDate = new Date(System.currentTimeMillis());
	this.sessionStatus = NoticeboardSession.INCOMPLETE;
    }

    public NoticeboardSession(Long nbSessionId, String nbSessionName) {
	this.nbSessionId = nbSessionId;
	this.nbSessionName = nbSessionName;
	this.sessionStartDate = new Date(System.currentTimeMillis());
	this.sessionStatus = NoticeboardSession.INCOMPLETE;
    }

    public NoticeboardSession(Long nbSessionId) {
	this.nbSessionId = nbSessionId;
	this.sessionStartDate = new Date(System.currentTimeMillis());
	this.sessionStatus = NoticeboardSession.INCOMPLETE;
    }

    /**
     *
     *
     */
    public NoticeboardContent getNbContent() {
	return nbContent;
    }

    public void setNbContent(NoticeboardContent nbContent) {
	this.nbContent = nbContent;
    }

    /**
     *
     */
    public Long getNbSessionId() {
	return nbSessionId;
    }

    public void setNbSessionId(Long nbSessionId) {
	this.nbSessionId = nbSessionId;
    }

    /**
     *
     */
    public String getNbSessionName() {
	return nbSessionName;
    }

    public void setNbSessionName(String nbSessionName) {
	this.nbSessionName = nbSessionName;
    }

    /**
     *
     */
    public Date getSessionEndDate() {
	return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
	this.sessionEndDate = sessionEndDate;
    }

    /**
     *
     */
    public Date getSessionStartDate() {
	return sessionStartDate;
    }

    public void setSessionStartDate(Date sessionStartDate) {
	this.sessionStartDate = sessionStartDate;
    }

    /**
     *
     */
    public String getSessionStatus() {
	return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
	this.sessionStatus = sessionStatus;
    }

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     *
     *
     */
    public Set<NoticeboardUser> getNbUsers() {
	if (this.nbUsers == null) {
	    setNbUsers(new HashSet<NoticeboardUser>());
	}

	return nbUsers;
    }

    /**
     * @param nbUsers
     *            The nbUsers to set.
     */
    public void setNbUsers(Set<NoticeboardUser> nbUsers) {
	this.nbUsers = nbUsers;
    }

}
