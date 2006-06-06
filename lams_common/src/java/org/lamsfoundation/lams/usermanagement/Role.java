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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="lams_role"
 *     
*/
public class Role implements Serializable {

	public static final String ADMIN = "ADMIN";

	public static final String LEARNER = "LEARNER";
	
	public static final String STAFF = "STAFF";
	
	public static final String AUTHOR = "AUTHOR";
	
	public static final String COURSE_MANAGER = "COURSE MANAGER";
	
	public static final String COURSE_ADMIN = "COURSE ADMIN";
	
	public static final String SYSADMIN = "SYSADMIN";//for future use
	
	/**
	 * Added by Manpreet Minhas
	 * ***********************
	 * final static variables indicating the various
	 * roles available for a given user ar per the
	 * database. New roles may be added/deleted in the
	 * near future
	 * 
	 ************************************************************/
		public static final Integer ROLE_SYSADMIN =new Integer(1);
		public static final Integer ROLE_COURSE_MANAGER =new Integer(2);
		public static final Integer ROLE_AUTHOR =new Integer(3);
		public static final Integer ROLE_STAFF =new Integer(4);
		public static final Integer ROLE_LEARNER =new Integer(5);
		public static final Integer ROLE_COURSE_ADMIN =new Integer(6);
	/***********************************************************/
	
    /** identifier field */
    private Integer roleId;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Set userOrganisationRoles;

    /** persistent field */
    private Set rolePrivileges;

    /** full constructor */
    public Role(String name, String description, Set userOrganisationRoles) {
        this.name = name;
        this.description = description;
        this.userOrganisationRoles = userOrganisationRoles;
    }

    /** default constructor */
    public Role() {
    }

    /** minimal constructor */
    public Role(String name, Set userOrganisationRoles) {
        this.name = name;
        this.userOrganisationRoles = userOrganisationRoles;
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="role_id"
     *         
     */
    public Integer getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             length="64"
     *             not-null="true"
     *         
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="65535"
     *         
     */
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="delete-orphan"
     *            @hibernate.collection-key
     *             column="role_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.UserOrganisationRole"
     *         
     */
    public Set getUserOrganisationRoles() {
        return this.userOrganisationRoles;
    }

    public void setUserOrganisationRoles(Set userOrganisationRoles) {
        this.userOrganisationRoles = userOrganisationRoles;
    }
    
    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="delete-orphan"
     *            @hibernate.collection-key
     *             column="role_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.RolePrivilege"
     *         
     */
    public Set getRolePrivileges() {
		return rolePrivileges;
	}

	public void setRolePrivileges(Set rolePrivileges) {
		this.rolePrivileges = rolePrivileges;
	}

	public void addUserOrganisationRole(UserOrganisationRole userOrganisationRole){
    	if(userOrganisationRoles==null)
    		userOrganisationRoles = new HashSet();
    	userOrganisationRoles.add(userOrganisationRole);
    }
    public String toString() {
        return new ToStringBuilder(this)
            .append("roleId", getRoleId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Role) ) return false;
        Role castOther = (Role) other;
        return new EqualsBuilder()
            .append(this.getRoleId(), castOther.getRoleId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getRoleId())
            .toHashCode();
    }
    public boolean isAuthor(){
    	return this.roleId.equals(ROLE_AUTHOR);
    }
    public boolean isCourseManager(){
    	return this.roleId.equals(ROLE_COURSE_MANAGER);
    }
    
    public boolean isCourseAdmin(){
    	return this.roleId.equals(ROLE_COURSE_ADMIN);
    }
    
    public boolean isStaff(){
    	return this.roleId.equals(ROLE_STAFF);
    }

    public boolean isSysAdmin(){
    	return this.roleId.equals(ROLE_SYSADMIN);
    }
}
