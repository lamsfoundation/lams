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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.rsrc.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
/**
 * Resource
 * @author Dapeng Ni
 *
 * @hibernate.class  table="tl_larsrc11_user"
 *
 */
public class ResourceUser {
	private static final long serialVersionUID = -7043502180037866257L;
	private static Logger log = Logger.getLogger(ResourceUser.class);
	
    private Long uid;
	private Long userId;
	private String firstName;
	private String lastName;
	private String loginName;
	private ResourceSession session;
	
	public ResourceUser(){
	}
	public ResourceUser(UserDTO user, ResourceSession session){
		this.userId = new Long(user.getUserID().intValue());
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.loginName = user.getLogin();
		this.session = session;
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
	 * @hibernate.property column="login_name"
	 * @return
	 */
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	/**
	 * @hibernate.many-to-one column="session_id"
	 * 			cascade="none"
	 * @return
	 */
	public ResourceSession getSession() {
		return session;
	}

	public void setSession(ResourceSession session) {
		this.session = session;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ResourceUser))
			return false;

		final ResourceUser user = (ResourceUser) obj;
		
		return new EqualsBuilder().append(this.uid, user.uid).append(this.firstName, user.firstName)
			.append(this.lastName,user.lastName).append(this.loginName, user.loginName).isEquals();
		
	}

	public int hashCode() {
		return new HashCodeBuilder().append(uid).append(firstName)
		.append(lastName).append(loginName)
		.toHashCode();
	}

}
