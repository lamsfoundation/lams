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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class AuthenticationMethod implements Serializable {

    public static final Integer DB = 1;
    public static final Integer LDAP = 3;

    /** identifier field */
    private Integer authenticationMethodId;

    /** persistent field */
    private String authenticationMethodName;

    /** persistent field */
    private AuthenticationMethodType authenticationMethodType;

    /** persistent field */
    private Set users;

    private boolean enabled = true;

    /** full constructor */
    public AuthenticationMethod(String authenticationMethodName, AuthenticationMethodType authenticationMethodType,
	    Set users) {
	this.authenticationMethodName = authenticationMethodName;
	this.authenticationMethodType = authenticationMethodType;
	this.users = users;
    }

    /** default constructor */
    public AuthenticationMethod() {
    }

    public Integer getAuthenticationMethodId() {
	return this.authenticationMethodId;
    }

    public void setAuthenticationMethodId(Integer authenticationMethodId) {
	this.authenticationMethodId = authenticationMethodId;
    }

    public String getAuthenticationMethodName() {
	return this.authenticationMethodName;
    }

    public void setAuthenticationMethodName(String authenticationMethodName) {
	this.authenticationMethodName = authenticationMethodName;
    }

    public AuthenticationMethodType getAuthenticationMethodType() {
	return this.authenticationMethodType;
    }

    public void setAuthenticationMethodType(AuthenticationMethodType AuthenticationMethodType) {
	this.authenticationMethodType = AuthenticationMethodType;
    }

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
     * @param enabled
     *            The enabled to set.
     */
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("authenticationMethodId", getAuthenticationMethodId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof AuthenticationMethod)) {
	    return false;
	}
	AuthenticationMethod castOther = (AuthenticationMethod) other;
	return new EqualsBuilder().append(this.getAuthenticationMethodId(), castOther.getAuthenticationMethodId())
		.isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getAuthenticationMethodId()).toHashCode();
    }

}
