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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_authentication_method")
public class AuthenticationMethod implements Serializable {
    private static final long serialVersionUID = -43956990418742273L;

    public static final Integer DB = 1;
    public static final Integer LDAP = 3;

    @Id
    @Column(name = "authentication_method_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authenticationMethodId;

    @Column(name = "authentication_method_name")
    private String authenticationMethodName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authentication_method_type_id")
    private AuthenticationMethodType authenticationMethodType;

    @OneToMany(mappedBy = "authenticationMethod")
    private Set<User> users = new HashSet<User>();

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

    public Set<User> getUsers() {
	return this.users;
    }

    public void setUsers(Set<User> users) {
	this.users = users;
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