package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="lams_authentication_method_type"
 *     
*/
public class AuthenticationMethodType implements Serializable {

    /** identifier field */
    private Integer authenticationMethodTypeId;

    /** persistent field */
    private String description;

    /** persistent field */
    private Set authenticationMethods;

    /** full constructor */
    public AuthenticationMethodType(String description, Set authenticationMethods) {
        this.description = description;
        this.authenticationMethods = authenticationMethods;
    }

    /** default constructor */
    public AuthenticationMethodType() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="authentication_method_type_id"
     *         
     */
    public Integer getAuthenticationMethodTypeId() {
        return this.authenticationMethodTypeId;
    }

    public void setAuthenticationMethodTypeId(Integer authenticationMethodTypeId) {
        this.authenticationMethodTypeId = authenticationMethodTypeId;
    }

    /** 
     *            @hibernate.property
     *             column="description"
     *             length="64"
     *             not-null="true"
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
     *             column="authentication_method_type_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.AuthenticationMethod"
     *         
     */
    public Set getAuthenticationMethods() {
        return this.authenticationMethods;
    }

    public void setAuthenticationMethods(Set authenticationMethods) {
        this.authenticationMethods = authenticationMethods;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("authenticationMethodTypeId", getAuthenticationMethodTypeId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AuthenticationMethodType) ) return false;
        AuthenticationMethodType castOther = (AuthenticationMethodType) other;
        return new EqualsBuilder()
            .append(this.getAuthenticationMethodTypeId(), castOther.getAuthenticationMethodTypeId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAuthenticationMethodTypeId())
            .toHashCode();
    }

}
