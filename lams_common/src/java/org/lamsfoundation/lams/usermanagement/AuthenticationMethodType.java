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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "lams_auth_method_type")
public class AuthenticationMethodType implements Serializable {
    private static final long serialVersionUID = 1220342429769830966L;

    public static final String LAMS = "LAMS";
    public static final String WEB_AUTH = "WEB_AUTH";
    public static final String LDAP = "LDAP";

    @Id
    @Column(name = "authentication_method_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authenticationMethodTypeId;

    @Column
    private String description;

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