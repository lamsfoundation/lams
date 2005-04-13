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
	
	public static final String SYSADMIN = "SYSADMIN";//for future use
	
    /** identifier field */
    private Integer roleId;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Set userOrganisationRoles;

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
     *             cascade="none"
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

}
