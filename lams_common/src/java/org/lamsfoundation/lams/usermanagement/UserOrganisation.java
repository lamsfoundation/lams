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
 *         table="lams_user_organisation"
 *     
*/
public class UserOrganisation implements Serializable {

    /** identifier field */
    private Integer userOrganisationId;

    /** persistent field */
    private User user;

    /** persistent field */
    private Organisation organisation;

    /** persistent field */
    private Set userOrganisationRoles;

    /** full constructor */
    public UserOrganisation(User user, Organisation organisation, Set userOrganisationRoles) {
        this.user = user;
        this.organisation = organisation;
        this.userOrganisationRoles = userOrganisationRoles;
    }
    
    /**minimal constructor */
    public UserOrganisation(User user, Organisation organisation){
    	this.user = user;
        this.organisation = organisation;
    }

    /** default constructor */
    public UserOrganisation() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="user_organisation_id"
     *         
     */
    public Integer getUserOrganisationId() {
        return this.userOrganisationId;
    }

    public void setUserOrganisationId(Integer userOrganisationId) {
        this.userOrganisationId = userOrganisationId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="user_id"         
     *         
     */
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="organisation_id"         
     *         
     */
    public Organisation getOrganisation() {
        return this.organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    /** 
     *            @hibernate.set
     *             lazy="false"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="user_organisation_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.hibernate.userOrganisationRole"
     *         
     */
    public Set getUserOrganisationRoles() {
        return this.userOrganisationRoles;
    }

    public void setUserOrganisationRoles(Set userOrganisationRoles) {
        this.userOrganisationRoles = userOrganisationRoles;
    }
    public void addUserOrganisationRole(UserOrganisationRole userOrganisationRole){
    	if(userOrganisationRoles==null)
    		userOrganisationRoles = new HashSet();
    	userOrganisationRoles.add(userOrganisationRole);
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userOrganisationId", getUserOrganisationId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof UserOrganisation) ) return false;
        UserOrganisation castOther = (UserOrganisation) other;
        return new EqualsBuilder()
            .append(this.getUserOrganisationId(), castOther.getUserOrganisationId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserOrganisationId())
            .toHashCode();
    }

}
