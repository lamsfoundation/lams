package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="lams_user_organisation_role"
 *     
*/
public class UserOrganisationRole implements Serializable {

    /** identifier field */
    private Integer userOrganisationRoleId;

    /** persistent field */
    private UserOrganisation userOrganisation;

    /** persistent field */
    private Role role;

    /** full constructor */
    public UserOrganisationRole(UserOrganisation userOrganisation, Role role) {
        this.userOrganisation = userOrganisation;
        this.role = role;
    }

    /** default constructor */
    public UserOrganisationRole() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="user_organisation_role_id"
     *         
     */
    public Integer getUserOrganisationRoleId() {
        return this.userOrganisationRoleId;
    }

    public void setUserOrganisationRoleId(Integer userOrganisationRoleId) {
        this.userOrganisationRoleId = userOrganisationRoleId;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="user_organisation_id"         
     *         
     */
    public UserOrganisation getUserOrganisation() {
        return this.userOrganisation;
    }

    public void setUserOrganisation(UserOrganisation userOrganisation) {
        this.userOrganisation = userOrganisation;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="role_id"         
     *         
     */
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userOrganisationRoleId", getUserOrganisationRoleId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof UserOrganisationRole) ) return false;
        UserOrganisationRole castOther = (UserOrganisationRole) other;
        return new EqualsBuilder()
            .append(this.getUserOrganisationRoleId(), castOther.getUserOrganisationRoleId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUserOrganisationRoleId())
            .toHashCode();
    }

}
