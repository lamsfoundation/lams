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
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
/**
 * 
 * @hibernate.class table="tl_lafrum11_forum_user"
 * @author Steve.Ni
 * 
 * @version $Revision$
 * @serialData -7043502180037866257L
 */
public class ForumUser implements Serializable,Cloneable{

	private static final long serialVersionUID = -7043502180037866257L;
	private static Logger log = Logger.getLogger(ForumUser.class);
	
    private Long uid;
	private Long userId;
	private String firstName;
	private String lastName;
	private String loginName;
	private ForumToolSession session;
	
	public ForumUser(){
	}
	
	public ForumUser(UserDTO user,  ForumToolSession session) {
		this.userId = new Long(user.getUserID().intValue());
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.loginName = user.getLogin();
		this.session = session;
	}
	

//  **********************************************************
  	//		Function method for ForumUser
//  **********************************************************
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + ForumUser.class + " failed");
		}
		return obj;
	}


//  **********************************************************
  	//		Get/Set methods
//  **********************************************************
	/**
	 * @hibernate.id generator-class="identity" type="java.lang.Long" column="uid"
	 * @return Returns the uid.
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * @param uid The uid to set.
	 */
	public void setUid(Long userID) {
		this.uid = userID;
	}

	/**
	 * @hibernate.property column="user_id" length="20"
	 * @return Returns the userId.
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(Long userID) {
		this.userId = userID;
	}

	/**
	 * @hibernate.property length="255" column="last_name"
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @hibernate.property length="255" column="first_name"
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @hibernate.many-to-one column="session_id"
	 * 			cascade="none"
	 * @return
	 */
	public ForumToolSession getSession() {
		return session;
	}

	public void setSession(ForumToolSession session) {
		this.session = session;
	}

	/**
	 * @hibernate.property column="login_name"
	 * @return
	 */
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

}
