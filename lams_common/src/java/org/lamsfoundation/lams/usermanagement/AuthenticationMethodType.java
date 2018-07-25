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

public class AuthenticationMethodType implements Serializable {

    public static final String LAMS = "LAMS";
    public static final String WEB_AUTH = "WEB_AUTH";
    public static final String LDAP = "LDAP";

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

    public Integer getAuthenticationMethodTypeId() {
	return this.authenticationMethodTypeId;
    }

    public void setAuthenticationMethodTypeId(Integer authenticationMethodTypeId) {
	this.authenticationMethodTypeId = authenticationMethodTypeId;
    }

    public String getDescription() {
	return this.description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Set getAuthenticationMethods() {
	return this.authenticationMethods;
    }

    public void setAuthenticationMethods(Set authenticationMethods) {
	this.authenticationMethods = authenticationMethods;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("authenticationMethodTypeId", getAuthenticationMethodTypeId())
		.toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof AuthenticationMethodType)) {
	    return false;
	}
	AuthenticationMethodType castOther = (AuthenticationMethodType) other;
	return new EqualsBuilder()
		.append(this.getAuthenticationMethodTypeId(), castOther.getAuthenticationMethodTypeId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getAuthenticationMethodTypeId()).toHashCode();
    }

}
