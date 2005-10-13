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

import java.io.Serializable;

import org.apache.log4j.Logger;
/**
 * 
 * @hibernate.class table="tl_lafrum11_forum_learners"
 * @author Steve.Ni
 * 
 * @version $Revision$
 * @serialData -7043502180037866257L
 */
public class Learner implements Serializable,Cloneable{

	private static final long serialVersionUID = -7043502180037866257L;

	private static Logger log = Logger.getLogger(Learner.class);
	
    private Long uuid;
	private Long userID;
	private String userName;
	private String fullName;
	private Long sessionID;
	private boolean status;
	
	/**
	 * @hibernate.id generator-class="identity" type="java.lang.Long" column="uuid"
	 * @return Returns the uuid.
	 */
	public Long getUuid() {
		return uuid;
	}
	/**
	 * @param uuid The uuid to set.
	 */
	public void setUuid(Long learnerID) {
		this.uuid = learnerID;
	}

	/**
	 * @hibernate.property column="user_id" length="20"
	 * @return Returns the userID.
	 */
	public Long getUserID() {
		return userID;
	}
	/**
	 * @param userID
	 *            The userID to set.
	 */
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	/**
	 * @hibernate.property column="status" length="1"
	 * @return Returns the status.
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(boolean finished) {
		this.status = finished;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + Learner.class + " failed");
		}
		return obj;
	}
	/**
	 * @hibernate.property column="session_id" length="20"
	 * @return Returns the sessionID.
	 */
	public Long getSessionID() {
		return sessionID;
	}
	/**
	 * @param sessionID The sessionID to set.
	 */
	public void setSessionID(Long sessionID) {
		this.sessionID = sessionID;
	}
	/**
	 * @hibernate.property length="255"
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @hibernate.property length="255"
	 * @return
	 */
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
