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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.usermanagement.service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.BulkUpdateResultDTO;

/**
 * @author jliew
 */
public interface ILdapService {

    public static final String SYNC_RESULTS = "syncResults";

    /**
     * Updates a LAMS user's profile with LDAP attributes.
     *
     * @param user
     * @param attrs
     */
    public void updateLDAPUser(User user, Attributes attrs);

    /**
     * Creates a LAMS user from LDAP attributes. Returns false on failure.
     *
     * @param attrs
     * @return boolean
     */
    public boolean createLDAPUser(Attributes attrs);

    /**
     * Returns LDAP attribute name, removing prefixed '!' char if necessary, which is used to toggle the
     * enabled/disabled meaning of the ldap attribute.
     *
     * @param ldapAttr
     * @return ldapAttr
     */
    public String getLdapAttr(String ldapAttr);

    public String getSingleAttributeString(Attribute attr);

    /**
     * Convert the LDAP disabled attribute value string to a boolean.
     *
     * @param attrs
     * @return boolean
     */
    public boolean getDisabledBoolean(Attributes attrs);

    /**
     * Adds user to organisation with roles specified by the LDAPOrgField, LDAPOrgAttr, LDAPRolesAttr attributes.
     * Returns false if it can't do one of these tasks.
     *
     * @param attrs
     * @param userId
     * @return boolean
     */
    public boolean addLDAPUser(Attributes attrs, Integer userId);

    /**
     * Bulk updates LAMS with LDAP users.
     *
     * @return stats on result of bulk update.
     */
    public BulkUpdateResultDTO bulkUpdate();
}
