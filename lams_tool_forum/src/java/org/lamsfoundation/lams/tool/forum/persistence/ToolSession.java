/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.Date;

/**
 * @hibernate.class table="tl_lafrum11_tool_session"
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ToolSession {
	private Long uid;
	private Long sessionId;
	private Forum forum;
	private Date sessionStartDate;
	private Date sessionEndDate;
	private int status;
	
	/**
	 * @hibernate.id generator-class="identity" type="java.lang.Long" column="uid"
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
 	 * @hibernate.many-to-one  column="forum_uid"
 	 * cascade="none"
	 * @return
	 */
	public Forum getForum() {
		return forum;
	}
	public void setForum(Forum forum) {
		this.forum = forum;
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


}
