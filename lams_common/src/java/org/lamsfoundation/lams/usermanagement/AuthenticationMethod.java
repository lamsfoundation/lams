package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Set;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="lams_authentication_method"
 *     
*/
public class AuthenticationMethod implements Serializable {
	
    /** identifier field */
    private Integer authenticationMethodId;

    /** persistent field */
    private String authenticationMethodName;

    /** persistent field */
    private AuthenticationMethodType authenticationMethodType;

    /** persistent field */
    private Set users;
    
    private boolean enabled = true;
    
    private List authenticationMethodParameters;

    /** full constructor */
    public AuthenticationMethod(String authenticationMethodName, AuthenticationMethodType authenticationMethodType, Set users) {
    	this.authenticationMethodName = authenticationMethodName;
        this.authenticationMethodType = authenticationMethodType;
        this.users = users;
    }

    /** default constructor */
    public AuthenticationMethod() {
    }

    
    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="authentication_method_id"
     *         
     */
    public Integer getAuthenticationMethodId() {
        return this.authenticationMethodId;
    }

    public void setAuthenticationMethodId(Integer authenticationMethodId) {
        this.authenticationMethodId = authenticationMethodId;
    }

    /** 
     *            @hibernate.property
     *             column="authentication_method_name"
     *             unique="true"
     *             length="64"
     *             not-null="true"
     *         
     */
    public String getAuthenticationMethodName() {
        return this.authenticationMethodName;
    }

    public void setAuthenticationMethodName(String authenticationMethodName) {
        this.authenticationMethodName = authenticationMethodName;
    }
    
    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *             lazy="false"
     *            @hibernate.column name="authentication_method_type_id"         
     *         
     */
    public AuthenticationMethodType getAuthenticationMethodType() {
        return this.authenticationMethodType;
    }

    public void setAuthenticationMethodType(AuthenticationMethodType AuthenticationMethodType) {
        this.authenticationMethodType = AuthenticationMethodType;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="authentication_method_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.User"
     *         
     */
    public Set getUsers() {
        return this.users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }

	/**
	 * @return Returns the enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled The enabled to set.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
    
	/**
	 * @return Returns the authenticationMethodParameters.
	 */
	public List getAuthenticationMethodParameters() {
		return authenticationMethodParameters;
	}
	/**
	 * @param authenticationMethodParameters The authenticationMethodParameters to set.
	 */
	public void setAuthenticationMethodParameters(
			List authenticationMethodParameters) {
		this.authenticationMethodParameters = authenticationMethodParameters;
	}
	
	public AuthenticationMethodParameter getParameterByName(String name){
		for(int i=0; i<authenticationMethodParameters.size(); i++){
			if(((AuthenticationMethodParameter)authenticationMethodParameters.get(i)).getName().equals(name)){
				return (AuthenticationMethodParameter)authenticationMethodParameters.get(i);
			}
		}
		return null;
	}
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("authenticationMethodId", getAuthenticationMethodId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AuthenticationMethod) ) return false;
        AuthenticationMethod castOther = (AuthenticationMethod) other;
        return new EqualsBuilder()
            .append(this.getAuthenticationMethodId(), castOther.getAuthenticationMethodId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getAuthenticationMethodId())
            .toHashCode();
    }

}
